package ovenues.com.ovenue.fragments.serviceproviders_details;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ovenues.com.ovenue.Login;
import ovenues.com.ovenue.R;
import ovenues.com.ovenue.ServiceProvidersDetailsMainFragment;
import ovenues.com.ovenue.adapter.Spiners.SpinerWithDynamicArrayList;
import ovenues.com.ovenue.modelpojo.Spiners.SearchVenueSpiners;
import ovenues.com.ovenue.utils.Const;
import ovenues.com.ovenue.utils.Utils;

import static android.content.Context.MODE_PRIVATE;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.delivery_charges;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.is_delivery;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.is_delivery_na;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.is_delivery_paid;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.is_onsite_service;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.is_pickup;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.open_hours;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.open_hours_cateringSP;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.placeID;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.providerAddress;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.str_service_provider_id;
import static ovenues.com.ovenue.utils.APICall.post;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceproviderGenericRequestQuote extends Fragment {

    private RadioGroup radioTypeGroup;
    public static TextView tv_delivery_freecharge_requestquote_genricSP;

    final List<String> timeslotlist=new ArrayList<String>();

    private RadioButton radioTypeButton,rb_delivery,rb_pickup,rb_onsite;
    TextView tv_submit,tv_error_datetime;
    EditText et_date ,et_guestCount,et_message,et_delivery_time,et_special_message ;
    AutoCompleteTextView et_address_line;
    CheckBox cb_terms;
    LinearLayout ll_duration;
    private int mYear, mMonth, mDay, mHour, mMinute;
    //TextInputLayout til_date ,til_time ,til_address;

    SharedPreferences sharepref;

    String str_date=null,str_delivery_time=null,str_event_type_id,str_service_hours,str_et_guestCount ,str_et_message,
            str_et_specialmessage,str_address1,str_send_type,str_place_id;
    String str_hh="00",str_mm="00";

    public static Spinner sp_eventType;
    public static  SpinerWithDynamicArrayList eventTypeAdapter;
    public static ArrayList<SearchVenueSpiners> sp_eventType_aarayList;


    public ServiceproviderGenericRequestQuote() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for convertView fragment
        View convertView =  inflater.inflate(R.layout.fragment_serviceprovider_generic_request_quote, container, false);


        sharepref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        ll_duration = (LinearLayout)convertView.findViewById(R.id.ll_duration);

        // Spinner element
        final Spinner spinner = (Spinner) convertView.findViewById(R.id.sp_hh);
        // Spinner Drop down elements
         DecimalFormat desiformat = new DecimalFormat("##");
        List<String> hh = new ArrayList<String>();
        hh.add("HH");
        for(int a=0;a<=12;a++){
            hh.add((desiformat.format((double)a)));
        }
        // Creating adapter for spinner
        ArrayAdapter<String>dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, hh);

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
        final Spinner spinnerMM = (Spinner) convertView.findViewById(R.id.sp_mm);
        // Spinner Drop down elements
        List<String> mm = new ArrayList<String>();
        mm.add("mm");
        mm.add("00");
        mm.add("15");
        mm.add("30");
        mm.add("45");

        // Creating adapter for spinner
        ArrayAdapter<String>dataAdaptermm = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mm);

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

        tv_error_datetime=(TextView)convertView.findViewById(R.id.tv_error_datetime);
        tv_error_datetime.setVisibility(View.GONE);



        et_date = (EditText) convertView.findViewById(R.id.et_date);
        et_guestCount= (EditText) convertView.findViewById(R.id.et_guestCount);

        et_message= (EditText) convertView.findViewById(R.id.et_message);

        et_address_line= (AutoCompleteTextView) convertView.findViewById(R.id.et_address_line);

        et_delivery_time= (EditText) convertView.findViewById(R.id.et_delivery_time);
        et_special_message= (EditText) convertView.findViewById(R.id.et_special_message);


        tv_delivery_freecharge_requestquote_genricSP =(TextView)convertView.findViewById(R.id.tv_delivery_freecharge);
;
        if(is_delivery_paid.equalsIgnoreCase("1")){
            tv_delivery_freecharge_requestquote_genricSP.setText("Delivery Charge : "+delivery_charges);
        }
        /*til_date =(TextInputLayout)convertView.findViewById(R.id.til_date);
        til_time =(TextInputLayout)convertView.findViewById(R.id.til_time);
        til_address =(TextInputLayout)convertView.findViewById(R.id.til_address);*/
        radioTypeGroup=(RadioGroup)convertView.findViewById(R.id.radioGroup);

        rb_delivery=(RadioButton)convertView.findViewById(R.id.rb_delivery) ;
        rb_pickup=(RadioButton)convertView.findViewById(R.id.rb_pickup);
        rb_onsite=(RadioButton)convertView.findViewById(R.id.rb_onsite);


        if(is_delivery.equalsIgnoreCase("1"))
        {
            rb_delivery.setVisibility(View.VISIBLE);
            et_delivery_time.setVisibility(View.VISIBLE);
        } else {
            rb_delivery.setVisibility(View.GONE);
        }

        if(is_onsite_service.equalsIgnoreCase("1"))
        {
            rb_onsite.setVisibility(View.VISIBLE);
            ll_duration.setVisibility(View.VISIBLE);
        } else {
            rb_onsite.setVisibility(View.GONE);
            ll_duration.setVisibility(View.GONE);
        }

        if(is_pickup.equalsIgnoreCase("1"))
        {
            rb_pickup.setVisibility(View.VISIBLE);
        } else {
            rb_pickup.setVisibility(View.GONE);
        }

        rb_delivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rb_delivery.isChecked()){
                    /*til_address .setHint("Delivery Address");
                    til_date.setHint("Delivery Date");
                    til_time.setHint("Delivery Time");*/
                    et_address_line.setText("");
                    et_address_line.setEnabled(true);
                }
            }
        });

        rb_pickup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rb_pickup.isChecked()){
                    /*til_address.setHint("Pickup Address");
                    til_date.setHint("Pickup Date");
                    til_time.setHint("Pickup Time");*/
                    et_address_line.setText(providerAddress);
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
                    /*til_address.setHint("Address");
                    til_date.setHint("Date");
                    til_time.setHint("Time");*/
                    et_address_line.setText("");
                    et_address_line.setEnabled(true);
                }
            }
        });




        cb_terms= (CheckBox)convertView.findViewById(R.id.cb_terms);

        new ServiceProvidersDetailsMainFragment.GetEventTyes().execute();

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String str_day ,str_month;
                                if(dayOfMonth < 10){
                                    str_day = "0"+dayOfMonth;
                                }else {
                                    str_day = Integer.toString(dayOfMonth);
                                }
                                if(monthOfYear + 1 < 10){
                                    str_month = "0"+Integer.toString(monthOfYear + 1);
                                }else {
                                    str_month  =Integer.toString(monthOfYear + 1);
                                }
                                et_date.setText( str_month + "-" + str_day + "-" +year);
                                et_date.setError(null);
                                str_date = Utils.convertDateStringToString(et_date.getText().toString(),"MM-dd-yyyy","yyyy-MM-dd");

                                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                                Date date = new Date(year, monthOfYear, dayOfMonth-1);
                                String selected_dayOfWeek = simpledateformat.format(date);

                                Log.e("SElected day--",selected_dayOfWeek);
                                String openTime = "";
                                String closeTime = "";

                                if(sharepref.getString(Const.PREF_STR_SERVICE_ID,"").equalsIgnoreCase("1")){

                                    if(open_hours_cateringSP.getAsJsonArray().size()>0){
                                        for(int a=0;a<open_hours_cateringSP.size();a++){
                                            String got_Day = open_hours_cateringSP.get(a).getAsJsonObject().get("week_day").getAsString();
                                            Log.e("got day---",got_Day);
                                            if(got_Day.equalsIgnoreCase(selected_dayOfWeek)){
                                                openTime =open_hours_cateringSP.get(a).getAsJsonObject().get("open_time").getAsString();
                                                closeTime =open_hours_cateringSP.get(a).getAsJsonObject().get("close_time").getAsString();
                                                Log.e("openClose time---",""+ openTime + closeTime);
                                            }
                                        }
                                        if(openTime.length()<1 || closeTime.length()<1){
                                            et_date.setText("");
                                            et_delivery_time.setText("");
                                            tv_error_datetime.setVisibility(View.VISIBLE);
                                            tv_error_datetime.setText(" ! No time slots available. Please select a different date.");
                                            if(timeslotlist.size()>0) {
                                                timeslotlist.clear() ;
                                            }
                                        }else{
                                            tv_error_datetime.setVisibility(View.GONE);
                                            String timeValue = openTime;
                                            SimpleDateFormat sdf= new SimpleDateFormat("HH:mm:ss");
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
                                                    timeslotlist.add(Utils.convertDateStringToString(slotStartTime,"HH:mm","hh:mm aa"));
                                                }

                                            } catch (ParseException e) {
                                                // date in wrong format
                                            }

                                        }
                                    }
                                }else {

                                    if (open_hours.getAsJsonArray().size() > 0) {
                                        for (int a = 0; a < open_hours.size(); a++) {
                                            String got_Day = open_hours.get(a).getAsJsonObject().get("week_day").getAsString();
                                            Log.e("got day---", got_Day);
                                            if (got_Day.equalsIgnoreCase(selected_dayOfWeek)) {
                                                openTime = open_hours.get(a).getAsJsonObject().get("open_time").getAsString();
                                                closeTime = open_hours.get(a).getAsJsonObject().get("close_time").getAsString();
                                                Log.e("openClose time---", "" + openTime + closeTime);
                                            }
                                        }
                                        if (openTime.length() < 1 || closeTime.length() < 1) {
                                            et_date.setText("");
                                            et_delivery_time.setText("");
                                            tv_error_datetime.setVisibility(View.VISIBLE);
                                            tv_error_datetime.setText(" ! No time slots available. Please select a different date.");
                                            if(timeslotlist.size()>0) {
                                                timeslotlist.clear() ;
                                            }
                                        } else {
                                            tv_error_datetime.setVisibility(View.GONE);
                                            if(et_date.getError()!=null){
                                                et_date.setError(null);
                                            }
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
                                                    timeslotlist.add(Utils.convertDateStringToString(slotStartTime,"HH:mm","hh:mm aa"));

                                                }

                                            } catch (ParseException e) {
                                                // date in wrong format
                                            }

                                        }
                                    }
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis() +(1 * 1000 * 24 * 3600));
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Available Time Slot.");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            // Toast.makeText(getActivity().getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
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
        sp_eventType = (Spinner)convertView.findViewById(R.id.sp_eventtype);
        sp_eventType_aarayList =new ArrayList<>();
        eventTypeAdapter = new SpinerWithDynamicArrayList(getContext() , R.layout.row_spiners_venue_search_filter, sp_eventType_aarayList);
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

        tv_submit=(TextView)convertView.findViewById(R.id.tv_submit);

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
                            Toast.makeText(getContext(), "Select Event Type !!!", Toast.LENGTH_LONG).show();
                        }/*else if(et_frosting.getText().toString().length()<0){
                    et_frosting.setError("Enter Frosting !");
                    }else if(et_fillings.getText().toString().length()<0){
                    et_fillings.setError("Enter Filling with !");}*/ else if (et_guestCount.getText().toString().length() <= 0) {
                            et_guestCount.setError("Enter Guest Numbers !");
                        } else if ((is_delivery.equalsIgnoreCase("1") || is_pickup.equalsIgnoreCase("1")) && is_onsite_service.equalsIgnoreCase("1")
                                && radioTypeGroup.getCheckedRadioButtonId() <= 0) {
                            Toast.makeText(getContext(), "Select Delivery / Pickup /on site?", Toast.LENGTH_LONG).show();
                        }else if(spinner.getVisibility()==View.VISIBLE && spinner.getSelectedItemPosition()<=1){
                            ((TextView)spinner.getSelectedView()).setError("Select duration hour.");
                        }else if (spinnerMM.getVisibility()==View.VISIBLE && spinnerMM.getSelectedItemPosition()<1){
                            ((TextView)spinnerMM.getSelectedView()).setError("Select duration minutes.");
                        }
                        else {

                            str_et_message = et_message.getText().toString();
                            str_et_specialmessage = et_special_message.getText().toString();
                            str_et_guestCount = et_guestCount.getText().toString();
                            str_address1 = et_address_line.getText().toString();
                            if(str_hh.equalsIgnoreCase("HH")){
                                str_hh="00";
                            }
                            if(str_mm.equalsIgnoreCase("mm")){
                                str_mm="00";
                            }
                            str_service_hours = str_hh+":"+str_mm+":"+"00";

                            int selectedId = radioTypeGroup.getCheckedRadioButtonId();
                            radioTypeButton = (RadioButton) getActivity().findViewById(selectedId);

                            str_send_type = radioTypeButton.getText().toString();

                            str_event_type_id = sp_eventType_aarayList.get(sp_eventType.getSelectedItemPosition()).getId().toString();
                            new SendCustomOrderService().execute();
                        }
                    } else {
                        Toast.makeText(getContext(), "Accept Terms and Conditions.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Login as User.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(),Login.class));
                    getActivity().finish();
                }
            }
        });


        et_address_line.setAdapter(new ServiceProvidersDetailsMainFragment.GooglePlacesAutocompleteAdapter(getActivity() , R.layout.text_row_list_item));
        et_address_line.setThreshold(1);
        et_address_line.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Log.d("clicked place id",placeID.get(position));
                str_place_id =placeID.get(position).toString();

                //Log.d("Log place clicked===",str_place_id);
                new ServiceProvidersDetailsMainFragment.GetPlaceDteailsViaGooglePlace().execute();
                et_address_line.clearFocus();
                /*InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_address_line.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return convertView;
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
            progressDialog = ProgressDialog.show(getContext(), "Loading", "Please wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");
            //Log.d("post execute", "Executando doInBackground   ingredients");
            try {

                JSONObject req = new JSONObject();
                req.put("user_id",sharepref.getString(Const.PREF_USER_ID,null));
                req.put("service_provider_id",str_service_provider_id);
                req.put("service_id",sharepref.getString(Const.PREF_STR_SERVICE_ID,""));
                req.put("event_type_id",str_event_type_id);
                req.put("number_of_guests",str_et_guestCount);
                req.put("service_hours",str_service_hours);



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

                if(is_delivery_paid.equalsIgnoreCase("1")){
                    req.put("is_delivery_paid","1");
                    //req.put("delivery_charges",delivery_charges);
                }else {
                    req.put("is_delivery_paid","0");
                    //req.put("delivery_charges",delivery_charges);
                }

                if(is_delivery_na.equalsIgnoreCase("1")){
                    req.put("is_delivery_na","1");
                }else {
                    req.put("is_delivery_na","0");
                }

                req.put("delivery_address",str_address1);

                Log.d("REq Json======", req.toString());

                String response = post(Const.SERVER_URL_API +"service_custom_order", req.toString(),"put");//post("http://54.153.127.215/api/login", req.toString());
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
                Toast.makeText(getContext(), "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
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
                            new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Successfully Sent !");
                    builder.setMessage("Thank you for making order request online with ovenues.com. Your request has been sent to the vendor and we will mail you the estimated cost soon.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getActivity(),ServiceProvidersDetailsMainFragment.class)
                                    .putExtra("service_provider_id",str_service_provider_id));
                            getActivity().finish();
                            dialog.dismiss();
                        }
                    });
                    //builder.setNegativeButton("Cancel", null);
                    builder.show();

                } else{
                    String message=obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
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
