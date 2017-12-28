package ovenues.com.ovenue;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import movile.com.creditcardguide.APICall;
import ovenues.com.ovenue.adapter.Spiners.SpinerWithDynamicArrayList;
import ovenues.com.ovenue.modelpojo.Spiners.SearchVenueSpiners;
import ovenues.com.ovenue.utils.Const;
import ovenues.com.ovenue.utils.Utils;

import static ovenues.com.ovenue.utils.APICall.post;

public class AskOvenues extends AppCompatActivity {



    public static GoogleApiClient mGoogleApiClient;
    public static ArrayList<String> placeID;
    public static ArrayList<String> termsList = null;

    private RadioGroup radioTypeGroup;
    public static TextView tv_delivery_freecharge_requestquote_genricSP;

    final List<String> timeslotlist=new ArrayList<String>();

    private RadioButton radioTypeButton,rb_delivery,rb_pickup,rb_onsite;
    TextView tv_submit,tv_error_datetime;
    EditText et_date ,et_guestCount,et_message,et_delivery_time,et_special_message ;
    AutoCompleteTextView et_address_line;
    CheckBox cb_terms;
    private int mYear, mMonth, mDay, mHour, mMinute;
    TextInputLayout til_date ,til_time ,til_address;

    SharedPreferences sharepref;

    String str_date=null,str_delivery_time=null,str_event_type_id,str_et_guestCount ,str_et_message,
            str_et_specialmessage,str_address1,str_send_type,str_place_id;

    public static Spinner sp_eventType;
    public static SpinerWithDynamicArrayList eventTypeAdapter;
    public static ArrayList<SearchVenueSpiners> sp_eventType_aarayList;

    String str_service_hours,str_hh="00",str_mm="00";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_ovenues);

        final Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Ask Ovenues");

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

// Spinner element
        Spinner spinner = (Spinner) this.findViewById(R.id.sp_hh);
        // Spinner Drop down elements
        DecimalFormat desiformat = new DecimalFormat("##");
        List<String> hh = new ArrayList<String>();
        hh.add("HH");
        for(int a=0;a<=12;a++){
            hh.add((desiformat.format((double)a)));
        }
        // Creating adapter for spinner
        ArrayAdapter<String>dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hh);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_hh = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner element
        Spinner spinnerMM = (Spinner) this.findViewById(R.id.sp_mm);
        // Spinner Drop down elements
        List<String> mm = new ArrayList<String>();
        mm.add("mm");
        mm.add("00");
        mm.add("15");
        mm.add("30");
        mm.add("45");

        // Creating adapter for spinner
        ArrayAdapter<String>dataAdaptermm = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mm);

        // Drop down layout style - list view with radio button
        dataAdaptermm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerMM.setAdapter(dataAdaptermm);


        // Spinner click listener
        spinnerMM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_mm= parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Get Current Time
        final Calendar c = Calendar.getInstance();
        final int mHour = c.get(Calendar.HOUR_OF_DAY);
        final int mMinute = c.get(Calendar.MINUTE);

        tv_error_datetime=(TextView)this.findViewById(R.id.tv_error_datetime);
        tv_error_datetime.setVisibility(View.GONE);



        et_date = (EditText) this.findViewById(R.id.et_date);
        et_guestCount= (EditText) this.findViewById(R.id.et_guestCount);

        et_message= (EditText) this.findViewById(R.id.et_message);

        et_address_line= (AutoCompleteTextView) this.findViewById(R.id.et_address_line);

        et_delivery_time= (EditText) this.findViewById(R.id.et_delivery_time);
        et_special_message= (EditText) this.findViewById(R.id.et_special_message);


        tv_delivery_freecharge_requestquote_genricSP =(TextView)this.findViewById(R.id.tv_delivery_freecharge);
        ;
        /*if(is_delivery_paid.equalsIgnoreCase("1")){
            tv_delivery_freecharge_requestquote_genricSP.setText("Delivery Charge : "+delivery_charges);
        }*/
        til_date =(TextInputLayout)this.findViewById(R.id.til_date);
        til_time =(TextInputLayout)this.findViewById(R.id.til_time);
        til_address =(TextInputLayout)this.findViewById(R.id.til_address);
        radioTypeGroup=(RadioGroup)this.findViewById(R.id.radioGroup);

        rb_delivery=(RadioButton)this.findViewById(R.id.rb_delivery) ;
        rb_pickup=(RadioButton)this.findViewById(R.id.rb_pickup);
        rb_onsite=(RadioButton)this.findViewById(R.id.rb_onsite);


       /* if(is_delivery.equalsIgnoreCase("1"))
        {
            rb_delivery.setVisibility(View.VISIBLE);
            et_delivery_time.setVisibility(View.VISIBLE);
        } else {
            rb_delivery.setVisibility(View.GONE);
        }

        if(is_onsite_service.equalsIgnoreCase("1"))
        {
            rb_onsite.setVisibility(View.VISIBLE);
        } else {
            rb_onsite.setVisibility(View.GONE);
        }

        if(is_pickup.equalsIgnoreCase("1"))
        {
            rb_pickup.setVisibility(View.VISIBLE);
        } else {
            rb_pickup.setVisibility(View.GONE);
        }*/

        rb_delivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rb_delivery.isChecked()){
                    til_address .setHint("Delivery Address");
                    til_date.setHint("Delivery Date");
                    til_time.setHint("Delivery Time");
                    et_address_line.setText("");
                    et_address_line.setEnabled(true);
                }
            }
        });

        rb_pickup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rb_pickup.isChecked()){
                    til_address.setHint("Pickup Address");
                    til_date.setHint("Pickup Date");
                    til_time.setHint("Pickup Time");
                   // et_address_line.setText(providerAddress);
                    et_address_line.setEnabled(false);

                }else{
                    et_address_line.setText("");
                }
            }
        });


        rb_onsite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rb_onsite.isChecked()){
                    til_address.setHint("Address");
                    til_date.setHint("Date");
                    til_time.setHint("Time");
                    et_address_line.setText("");
                    et_address_line.setEnabled(true);
                }
            }
        });




        cb_terms= (CheckBox)this.findViewById(R.id.cb_terms);

        new GetEventTyes().execute();

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                // Get Current Date
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AskOvenues.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String str_day, str_month;
                                if (dayOfMonth < 10) {
                                    str_day = "0" + dayOfMonth;
                                } else {
                                    str_day = Integer.toString(dayOfMonth);
                                }
                                if (monthOfYear + 1 < 10) {
                                    str_month = "0" + Integer.toString(monthOfYear + 1);
                                } else {
                                    str_month = Integer.toString(monthOfYear + 1);
                                }
                                et_date.setText(str_month + "-" + str_day + "-" + year);
                                str_date = Utils.convertDateStringToString(et_date.getText().toString(), "MM-dd-yyyy", "yyyy-MM-dd");

                                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                                Date date = new Date(year, monthOfYear, dayOfMonth - 1);
                                String selected_dayOfWeek = simpledateformat.format(date);

                                Log.e("SElected day--", selected_dayOfWeek);
                                String openTime = "00:00:00";
                                String closeTime = "23:59:00";


                                if (openTime.length() < 1 || closeTime.length() < 1) {
                                    et_date.setText("");
                                    et_delivery_time.setText("");
                                    tv_error_datetime.setVisibility(View.VISIBLE);
                                    tv_error_datetime.setText(" ! No time slots available. Please select a different date.");
                                    if (timeslotlist.size() > 0) {
                                        timeslotlist.clear();
                                    }
                                } else {
                                    tv_error_datetime.setVisibility(View.GONE);
                                    String timeValue = openTime;
                                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                    try {
                                        Calendar startCalendar = Calendar.getInstance();
                                        startCalendar.setTime(sdf.parse(timeValue));

                                        if (startCalendar.get(Calendar.MINUTE) < 15) {
                                            startCalendar.set(Calendar.MINUTE, 15);
                                        } else {
                                            startCalendar.add(Calendar.MINUTE, 15); // overstep hour and clear minutes
                                            startCalendar.clear(Calendar.MINUTE);
                                        }

                                        Calendar endCalendar = Calendar.getInstance();
                                        endCalendar.setTime(sdf.parse(closeTime));

                                        // if you want dates for whole next day, uncomment next line
                                        //endCalendar.add(Calendar.DAY_OF_YEAR, 1);
                                                  /*  endCalendar.add(Calendar.HOUR_OF_DAY, 24 - startCalendar.get(Calendar.HOUR_OF_DAY));

                                                    endCalendar.clear(Calendar.MINUTE);
                                                    endCalendar.clear(Calendar.SECOND);
                                                    endCalendar.clear(Calendar.MILLISECOND);*/

                                        SimpleDateFormat slotTime = new SimpleDateFormat("HH:mm");
                                        SimpleDateFormat slotDate = new SimpleDateFormat(", dd/MM/yy");
                                        while (endCalendar.after(startCalendar)) {
                                            String slotStartTime = slotTime.format(startCalendar.getTime());
                                            String slotStartDate = slotDate.format(startCalendar.getTime());

                                            startCalendar.add(Calendar.MINUTE, 15);
                                            String slotEndTime = slotTime.format(startCalendar.getTime());

                                            Log.d("DATE", slotStartTime + " - " + slotEndTime + slotStartDate);
                                            timeslotlist.add(Utils.convertDateStringToString(slotStartTime, "HH:mm", "hh:mm aa"));
                                        }

                                    } catch (ParseException e) {
                                        // date in wrong format
                                    }

                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis() + (1 * 1000 * 24 * 3600));
                datePickerDialog.show();
            }
        });

        et_delivery_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch Time Picker Dialog
               /* TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {


                                et_delivery_time.setText(String.format("%02d:%02d", hourOfDay, minute));
                                str_delivery_time = et_delivery_time.getText().toString();
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();*/
                if(timeslotlist.size()>0){
                    final String items[]=timeslotlist.toArray(new String[timeslotlist.size()]);
                    AlertDialog.Builder builder = new AlertDialog.Builder(AskOvenues.this);
                    builder.setTitle("Available Time Slot.");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            // Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                            et_delivery_time.setText(items[item]);
                            str_delivery_time = Utils.convertDateStringToString(et_delivery_time.getText().toString(),"hh:mm aa","HH:mm:ss");
                            if(et_delivery_time.getError()!=null){
                                et_delivery_time.setError(null);
                            }
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });


        //===Spiner Event type config here=========
        sp_eventType = (Spinner)this.findViewById(R.id.sp_eventtype);
        sp_eventType_aarayList =new ArrayList<>();
        eventTypeAdapter = new SpinerWithDynamicArrayList(AskOvenues.this , R.layout.row_spiners_venue_search_filter, sp_eventType_aarayList);
        sp_eventType.setAdapter(eventTypeAdapter);

        /*sp_eventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                event_type_id = sp_eventType_aarayList.get(position).getId();
                Log.e("empid" , event_type_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        tv_submit=(TextView)this.findViewById(R.id.tv_submit);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!sharepref.getString(Const.PREF_USER_ID,"").equalsIgnoreCase("0")) {
                    if (cb_terms.isChecked()) {
                        if (et_date.getText().toString().length() <= 0) {
                            et_date.setError("Enter Date !");
                        } else if (et_delivery_time.getText().toString().length() <=0) {
                            et_delivery_time.setError("Enter Time !");
                        } else if (et_address_line.getText().toString().length() <= 0) {
                            et_address_line.setError("Enter Address !");
                        } else if (sp_eventType.getSelectedItemPosition() == 0) {
                            Toast.makeText(getBaseContext(), "Select Event Type !!!", Toast.LENGTH_LONG).show();
                        }/*else if(et_frosting.getText().toString().length()<0){
                    et_frosting.setError("Enter Frosting !");
                    }else if(et_fillings.getText().toString().length()<0){
                    et_fillings.setError("Enter Filling with !");}*/ else if (et_guestCount.getText().toString().length() <= 0) {
                            et_guestCount.setError("Enter Guest Numbers !");
                        } /*else if ((is_delivery.equalsIgnoreCase("1") || is_pickup.equalsIgnoreCase("1")) && is_onsite_service.equalsIgnoreCase("1")
                                && radioTypeGroup.getCheckedRadioButtonId() <= 0) {
                            Toast.makeText(getContext(), "Select Delivery / Pickup /on site?", Toast.LENGTH_LONG).show();
                        } */else {

                            str_et_message = et_message.getText().toString();
                            str_et_specialmessage = et_special_message.getText().toString();
                            str_et_guestCount = et_guestCount.getText().toString();
                            if(str_hh.equalsIgnoreCase("HH")){
                                str_hh="00";
                            }
                            if(str_mm.equalsIgnoreCase("mm")){
                                str_mm="00";
                            }
                            str_service_hours = str_hh+":"+str_mm+":"+"00";

                            str_address1 = et_address_line.getText().toString();

                            int selectedId = radioTypeGroup.getCheckedRadioButtonId();
                            radioTypeButton = (RadioButton) findViewById(selectedId);

                            str_send_type = radioTypeButton.getText().toString();

                            str_event_type_id = sp_eventType_aarayList.get(sp_eventType.getSelectedItemPosition()).getId().toString();
                            new SendCustomOrderService().execute();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "Accept Terms and Conditions.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getBaseContext(), "Login as User.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AskOvenues.this,Login.class));
                    finish();
                }
            }
        });


        et_address_line.setAdapter(new GooglePlacesAutocompleteAdapter(this , R.layout.text_row_list_item));
        et_address_line.setThreshold(1);
        et_address_line.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Log.d("clicked place id",placeID.get(position));
                str_place_id =placeID.get(position).toString();

                //Log.d("Log place clicked===",str_place_id);
                new GetPlaceDteailsViaGooglePlace().execute();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_address_line.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }
    }



    public static class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements
            Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public android.widget.Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    // String filterString = constraint.toString();
                    // System.out.println("Text:::::::::::"+filterString);
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        //System.out.println("Text:::::::::::" + constraint.toString());
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint,
                                              FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return (String) resultList.get(index);
        }
    }


    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(Const.PLACES_API_BASE
                    + Const.TYPE_AUTOCOMPLETE + Const.OUT_JSON);
            sb.append("?key=AIzaSyDtr3qaj_rlFGjLyPW4OClB7r7oO6S5jj4");
            // sb.append("&components=country:il");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            URL url = new URL(sb.toString());
            //System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {

            return resultList;
        } catch (IOException e) {

            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            if (placeID == null)
            {placeID = new ArrayList<String>();}
            else{
                placeID.clear();

                // Create a JSON object hierarchy from the results
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
                JSONArray termsJsonArray = null;
                // System.out.println(predsJsonArray);

                // Extract the Place descriptions from the results
                resultList = new ArrayList(predsJsonArray.length());
                termsList = new ArrayList<String>();

                for (int i = 0; i < predsJsonArray.length(); i++) {
                    //System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                    //System.out.println("============================================================");
                    resultList.add(predsJsonArray.getJSONObject(i).getString("description"));

                    placeID.add(predsJsonArray.getJSONObject(i).getString("place_id"));
                    //Log.d("place id",predsJsonArray.getJSONObject(i).getString("place_id"));
                    termsJsonArray = predsJsonArray.getJSONObject(i).getJSONArray(
                            "terms");

                    //System.out.println("termsJsonArray:::::::" + termsJsonArray);

                    int lenght = termsJsonArray.length() - 1;

                    //System.out.println("lenght:::::::" + lenght);

                    termsList.add(termsJsonArray.getJSONObject(lenght).getString("value"));
                    // System.out.println("termsList:::::::::::" + termsList);
                    // }
                }
            }
        } catch (JSONException e) {

        }

        return resultList;
    }


    public static String res_google_place;
    public class GetPlaceDteailsViaGooglePlace extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //pg_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {

                String response = APICall.post("https://maps.googleapis.com/maps/api/place/details/json?placeid="+str_place_id+"&key=AIzaSyDtr3qaj_rlFGjLyPW4OClB7r7oO6S5jj4", "","get");
                // Log.d("URL====","https://maps.googleapis.com/maps/api/place/details/json?placeid="+str_place_id+"&key=AIzaSyDtr3qaj_rlFGjLyPW4OClB7r7oO6S5jj4");
                res_google_place=response;
                //Log.d("RESPONCE PLACE====",response);
            }/*catch (JSONException e) {
                e.printStackTrace();
            }*/ catch (IOException e) {
                e.printStackTrace();
            }


            return res_google_place;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try{
                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(res_google_place).getAsJsonObject();

                String street_number="",route="",city="",state="",postalcode="";
                JsonArray address_components = rootObj.getAsJsonObject("result").getAsJsonArray("address_components");
                for(int a=0;a<address_components.size();a++){

                    Gson gson = new Gson();
                    JsonArray types_array = address_components.get(a).getAsJsonObject().getAsJsonArray("types");

                    //Log.d("address type array",types_array.toString());

                    Type type = new TypeToken<List<String>>(){}.getType();
                    List<String> typeList = gson.fromJson(types_array.toString(), type);

                    if(typeList.get(0).equalsIgnoreCase("street_number")){
                        street_number=address_components.get(a).getAsJsonObject().get("long_name").getAsString();
                    }
                    if(typeList.get(0).equalsIgnoreCase("route")){
                        route=address_components.get(a).getAsJsonObject().get("long_name").getAsString();
                    }
                    if(typeList.get(0).equalsIgnoreCase("locality")){
                        city = address_components.get(a).getAsJsonObject().get("long_name").getAsString();
                    }
                    if(typeList.get(0).equalsIgnoreCase("administrative_area_level_1")){
                        state = address_components.get(a).getAsJsonObject().get("long_name").getAsString() ;
                    }
                    if(typeList.get(0).equalsIgnoreCase("postal_code")){
                        postalcode = address_components.get(a).getAsJsonObject().get("long_name").getAsString();
                    }
                }

               /* if(et_address_line_balkery_custom_request.getVisibility()==View.VISIBLE){
                    et_address_line_balkery_custom_request.setText(street_number+" "+route+","+city+","+state+","+postalcode);
                }*/

                //===California VALIDATION , ONLLY California CITY PLACE IS ALLOWED==========
                if(!state.equalsIgnoreCase("California")){
                    /*if(et_address_line_balkery_custom_request.getVisibility()==View.VISIBLE){
                        et_address_line_balkery_custom_request.setText("");
                    }*/
                    Toast.makeText(AskOvenues.this,"sorry! now we are serving only in california .",Toast.LENGTH_LONG).show();
                }else{
                    //new GetDeliveryRate().execute();
                }


                /*String adr_address = rootObj.getAsJsonObject("result").get("adr_address").getAsString();
                adr_address=Html.fromHtml(adr_address)+"";
                Log.d("got address is ==",adr_address);
                String[] addre = adr_address.split(",");
                for(int j=0;j<addre.length;j++){
                    if(j==0){et_address_line_balkery_custom_request.setText(addre[0].toString());                    }
                    if(j==1){et_city.setText(addre[1].toString());}
                    //if(j==2){et_state.setText(addre[2].toString());}
                    if(j==3){et_zipcode.setText(addre[2].toString().substring(addre[2].toString().length()-6));}
                }*/
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public String resevent;
    public class GetEventTyes extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //pg_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {

                String response = post(Const.SERVER_URL_API +"events", "","get");
                //Log.d("REsponce Json====",response);
                resevent=response;
            }catch (IOException e) {
                e.printStackTrace();
            }
            return resevent;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try{JSONObject obj = new JSONObject(resevent);
                Log.i("RESPONSE", resevent);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    JsonParser parser = new JsonParser();

                    JsonObject rootObj = parser.parse(resevent).getAsJsonObject();

                    JsonArray events_typeObj = rootObj.getAsJsonArray("message");
                    sp_eventType_aarayList.add(new SearchVenueSpiners("0","Select Event Type"));

                    for (int j = 0; j < events_typeObj.size(); j++) {
                        String event_type_id=events_typeObj.get(j).getAsJsonObject().get("event_type_id").getAsString();
                        String event_type=events_typeObj.get(j).getAsJsonObject().get("event_type").getAsString();

                        //this is for just dyummydata set it will replace by original API comes...
                        SearchVenueSpiners  event_type_sp = new SearchVenueSpiners(event_type_id , event_type);
                        sp_eventType_aarayList.add(event_type_sp);

                    }
                    eventTypeAdapter.notifyDataSetChanged();
                } else{
                    String message=obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                    // Changing message text color
                    snackbar.setActionTextColor(Color.BLUE);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    String res;
    private class SendCustomOrderService extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //Log.d("pre execute", "Pre execute Done");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(AskOvenues.this, "Loading", "Please wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");
            //Log.d("post execute", "Executando doInBackground   ingredients");
            try {

                JSONObject req = new JSONObject();
                req.put("user_id",sharepref.getString(Const.PREF_USER_ID,null));
                //req.put("service_provider_id",str_service_provider_id);
                //req.put("service_id",sharepref.getString(Const.PREF_STR_SERVICE_ID,""));
                req.put("event_type_id",str_event_type_id);
                req.put("number_of_guests",str_et_guestCount);



                req.put("message",str_et_message);
                req.put("note",str_et_specialmessage);
                req.put("booking_date",str_date);
                req.put("booking_time",str_date+" "+str_delivery_time);

                if(str_send_type.equalsIgnoreCase("delivery")){
                    req.put("is_delivery","1");
                    req.put("is_pickup", "0");
                    req.put("is_onsite_service","0");
                }else if(str_send_type.equalsIgnoreCase("pickup")) {
                    req.put("is_delivery","0");
                    req.put("is_pickup", "1");
                    req.put("is_onsite_service","0");
                }else if(str_send_type.equalsIgnoreCase("On Site Service")) {
                    req.put("is_delivery","0");
                    req.put("is_pickup", "0");
                    req.put("is_onsite_service","1");
                }

                /*if(is_delivery_paid.equalsIgnoreCase("1")){
                    req.put("is_delivery_paid","1");
                    req.put("delivery_charges",delivery_charges);
                }else {
                    req.put("is_delivery_paid","0");
                    req.put("delivery_charges",delivery_charges);
                }

                if(is_delivery_na.equalsIgnoreCase("1")){
                    req.put("is_delivery_na","1");
                }else {
                    req.put("is_delivery_na","0");
                }*/
                req.put("is_delivery_na","0");
                req.put("delivery_address",str_address1);

                Log.d("REq Json======", req.toString());

                String response = post(Const.SERVER_URL_API +"ovenues_custom_order", req.toString(),"put");//post("http://54.153.127.215/api/login", req.toString());
                //Log.d("REsponce Json====",response);
                res = response;
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return res;

        }
        @Override
        protected void onPostExecute(String result)
        { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            super.onPostExecute(result);
        }
            //System.out.println("OnpostExecute----done-------");
            progressDialog.dismiss();

            if (res == null || res.equals("")) {

                progressDialog.dismiss();
                Toast.makeText(getBaseContext(), "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }
            Log.i("RESPONSE", res);
            try {
                JSONObject obj = new JSONObject(res);


                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.

                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    String user_obj=obj.getString("message");

                    android.support.v7.app.AlertDialog.Builder builder =
                            new android.support.v7.app.AlertDialog.Builder(AskOvenues.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Successfully Sent !");
                    builder.setMessage("Thank you for making order request online with ovenues.com. Your request has been received.We will mail you the details soon.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            dialog.dismiss();
                        }
                    });
                    //builder.setNegativeButton("Cancel", null);
                    builder.show();

                } else{
                    String message=obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                    // Changing message text color
                    snackbar.setActionTextColor(Color.BLUE);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
}
