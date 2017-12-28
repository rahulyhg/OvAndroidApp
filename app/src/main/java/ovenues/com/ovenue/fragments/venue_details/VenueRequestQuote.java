package ovenues.com.ovenue.fragments.venue_details;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ovenues.com.ovenue.Login;
import ovenues.com.ovenue.R;
import ovenues.com.ovenue.VenueDetailsMainFragment;
import ovenues.com.ovenue.adapter.Spiners.SpinerWithDynamicArrayList;
import ovenues.com.ovenue.modelpojo.Spiners.SearchVenueSpiners;
import ovenues.com.ovenue.utils.Const;
import ovenues.com.ovenue.utils.Utils;

import static android.content.Context.MODE_PRIVATE;
import static ovenues.com.ovenue.VenueDetailsMainFragment.str_venue_id;
import static ovenues.com.ovenue.utils.APICall.post;


public class VenueRequestQuote extends Fragment {

    TextView tv_submit;
    EditText et_date , et_start_time , et_end_time,et_guestCount,et_description ;
    CheckBox cb_terms;
    private int mYear, mMonth, mDay, mHour, mMinute;

    SharedPreferences sharepref;

    String str_date=null,str_start=null ,str_end=null,str_event_type_id,str_et_guestCount ,str_et_description;

    public static Spinner sp_eventType;
    public static SpinerWithDynamicArrayList eventTypeAdapter;
    public static ArrayList<SearchVenueSpiners> sp_eventType_aarayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for convertView fragment
        View convertView =  inflater.inflate(R.layout.fragment_venue_request_quote, container, false);

        sharepref = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        final int mHour = c.get(Calendar.HOUR_OF_DAY);
        final int mMinute = c.get(Calendar.MINUTE);



        et_date = (EditText) convertView.findViewById(R.id.et_date);
        et_start_time = (EditText) convertView.findViewById(R.id.et_start_time);
        et_end_time = (EditText) convertView.findViewById(R.id.et_end_time);
        et_guestCount= (EditText) convertView.findViewById(R.id.et_guestCount);
        et_description= (EditText) convertView.findViewById(R.id.et_description);

        cb_terms= (CheckBox)convertView.findViewById(R.id.cb_terms);

        new VenueDetailsMainFragment.GetEventTyesVenues().execute();

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
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
                                et_date.setText(str_month + "-" + str_day + "-" +year );
                                et_date.setError(null);
                                str_date = Utils.convertDateStringToString(et_date.getText().toString(),"MM-dd-yyyy","yyyy-MM-dd");
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis() + 1 * 1000 * 24 * 3600);
                datePickerDialog.show();
            }
        });
        et_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                               /* et_start_time.setText(String.format("%02d:%02d", hourOfDay, minute));*/

                                String am_pm = "";
                                Calendar datetime = Calendar.getInstance();
                                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                datetime.set(Calendar.MINUTE, minute);

                                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                                    am_pm = "AM";
                                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                                    am_pm = "PM";
                                String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";

                                et_start_time.setText(String.format(strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm ));
                                et_start_time.setError(null);
                                str_start = Utils.convertDateStringToString(et_start_time.getText().toString(),"hh:mm aa","HH:mm:ss");
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        et_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                               /* et_end_time.setText(String.format("%02d:%02d", hourOfDay, minute));*/

                                String am_pm = "";
                                Calendar datetime = Calendar.getInstance();
                                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                datetime.set(Calendar.MINUTE, minute);

                                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                                    am_pm = "AM";
                                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                                    am_pm = "PM";
                                String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";

                                et_end_time.setText(String.format(strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm ));
                                et_end_time.setError(null);
                                str_end = Utils.convertDateStringToString(et_end_time.getText().toString(),"hh:mm aa","HH:mm:ss");

                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
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
                    if (str_date==null ||
                            str_start==null ||
                            str_end==null ||
                            str_date.equalsIgnoreCase("") ||
                            str_start.equalsIgnoreCase("") ||
                            str_end.equalsIgnoreCase("")) {

                        Toast.makeText(getContext(), "Some Value Missing.", Toast.LENGTH_LONG).show();

                    } else if(et_guestCount.getText().toString().length()==0){
                        et_guestCount.setError("Enter Guest Numbers !");
                    }
                    else if(et_description.getText().length()<20){
                        et_description.setError("Please Enter description min 20 character.");
                    }
                    else {
                        str_event_type_id = sp_eventType_aarayList.get(sp_eventType.getSelectedItemPosition()).getId();
                        str_et_guestCount = et_guestCount.getText().toString();
                        str_et_description = et_description.getText().toString();


                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        try {
                            String time1 = et_date.getText().toString() + " " + et_start_time.getText().toString();
                            String time2 = et_date.getText().toString() + " " + et_end_time.getText().toString();
                            //Log.d("Timse are ===",time1+"=="+time2);

                            Date date1 = formatter.parse(time1);
                            Date date2 = formatter.parse(time2);

                            //Log.d("date1",date1.toString()+"=="+date2.toString());

                            if (date2.before(date1)) {
                                Toast.makeText(getContext(), "Time not proper", Toast.LENGTH_LONG).show();
                                final AlertDialog.Builder build = new AlertDialog.Builder(getContext());

                                build.setTitle("Alert !");
                                build.setMessage("Time are not Proper,\nPlease Check.");
                                build.setCancelable(false);

                                build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                build.create().show();
                                return;
                            } else {
                                if(sp_eventType.getSelectedItemPosition()==0){
                                    Toast.makeText(getContext(),"Please Select Event Type !",Toast.LENGTH_LONG).show();
                                }else if(!cb_terms.isChecked()){
                                    Toast.makeText(getContext(),"Please accept Terms & Conditions .",Toast.LENGTH_LONG).show();
                                }else{
                                    new SendRequest().execute();
                                }
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    Toast.makeText(getContext(), "Login as User.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getContext(),Login.class));
                    getActivity().finish();
                }
            }
        });


        return  convertView;
    }

    String res;
    class SendRequest extends AsyncTask<Object, Void, String> {

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
                req.put("venue_id",str_venue_id);
                req.put("booking_date",str_date);
                req.put("booking_time_from",str_date+" "+str_start);
                req.put("booking_time_to",str_date+" "+str_end);
                req.put("event_type_id",str_event_type_id);
                req.put("description",str_et_description);
                req.put("number_of_guests",str_et_guestCount);

                //Log.d("REq Json======", req.toString());

                String response = post(Const.SERVER_URL_API +"/booking_request_quote", req.toString(),"post");//post("http://54.153.127.215/api/login", req.toString());
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
                            new android.support.v7.app.AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Successfully Sent !");
                    builder.setMessage(user_obj);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().onBackPressed();
                            getActivity().finish();
                        }
                    });
                    //builder.setNegativeButton("Cancel", null);
                    builder.show();

                }

                else{
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
