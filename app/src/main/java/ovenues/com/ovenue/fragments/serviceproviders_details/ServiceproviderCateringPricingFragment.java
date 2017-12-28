package ovenues.com.ovenue.fragments.serviceproviders_details;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.ServiceProvidersDetailsMainFragment;
import ovenues.com.ovenue.cart.CartSummaryScreen;
import ovenues.com.ovenue.utils.Const;
import ovenues.com.ovenue.utils.Utils;

import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.adapterCateringPricingAdapter;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.deliveryPrice;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.delivery_charges;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.is_delivery;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.is_delivery_na;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.is_delivery_paid;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.is_onsite_service;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.is_pickup;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.open_hours_cateringSP;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.placeID;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.providerAddress;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.sharepref;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.str_place_id;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.str_service_provider_id;
import static ovenues.com.ovenue.utils.APICall.post;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceproviderCateringPricingFragment extends Fragment {


    final List<String> timeslotlist=new ArrayList<String>();

    public static RecyclerView mRecyclerViewCateringServiceProviderCharages;
    public static String str_cart_ID_SP_Main="",SP_cart_flag_Main="";

    public static TextView tv_guestcount, tv_delivery_freecharge_cateringSP;

    RadioGroup radioTypeGroup;
    RadioButton rb_delivery,rb_pickup,rb_onsite;
    TextView tv_error_datetime;
    EditText et_date ,et_guestCount,/*et_message,*/et_delivery_time/*,et_special_message*/ ;
    AutoCompleteTextView et_address_line;

     int mYear, mMonth, mDay;
    //TextInputLayout til_guest_count_hint ,til_date ,til_time ,til_address;

    public static String str_address;
    String str_date,str_delivery_time ,str_send_type;
    LinearLayout ll_catering_booking;
    TextView tv_submitdynamic;

    boolean booking_layout_display = true;

    public ServiceproviderCateringPricingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_serviceprovider_catering_pricing, container, false);


        // Initialize recycler view
        mRecyclerViewCateringServiceProviderCharages = (RecyclerView)convertView.findViewById(R.id.rv_charges);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewCateringServiceProviderCharages.setLayoutManager(mLayoutManager);
        mRecyclerViewCateringServiceProviderCharages.setNestedScrollingEnabled(false);
        mRecyclerViewCateringServiceProviderCharages.setAdapter(adapterCateringPricingAdapter);


        tv_guestcount = (TextView) convertView.findViewById(R.id.tv_guestcount);
        tv_delivery_freecharge_cateringSP =(TextView) convertView.findViewById(R.id.tv_delivery_freecharge);
       if(is_delivery_paid != null && is_delivery_paid.equalsIgnoreCase("1")){
            tv_delivery_freecharge_cateringSP.setText("Delivery Charge : "+delivery_charges);
        }


        /*til_guest_count_hint=(TextInputLayout)convertView.findViewById(R.id.til_guest_count_hint);
        til_date =(TextInputLayout)convertView.findViewById(R.id.til_date);
        til_time =(TextInputLayout)convertView.findViewById(R.id.til_time);
        til_address =(TextInputLayout)convertView.findViewById(R.id.til_address);*/

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        final int mHour = c.get(Calendar.HOUR_OF_DAY);
        final int mMinute = c.get(Calendar.MINUTE);

        et_date = (EditText) convertView.findViewById(R.id.et_date);
        tv_error_datetime=(TextView)convertView.findViewById(R.id.tv_error_datetime);
        tv_error_datetime.setVisibility(View.GONE);
        et_guestCount= (EditText) convertView.findViewById(R.id.et_guestCount);


        et_delivery_time= (EditText) convertView.findViewById(R.id.et_delivery_time);
        et_address_line= (AutoCompleteTextView) convertView.findViewById(R.id.et_address_line); radioTypeGroup=(RadioGroup)convertView.findViewById(R.id.radioGroup);

        rb_delivery=(RadioButton)convertView.findViewById(R.id.rb_delivery) ;
        rb_pickup=(RadioButton)convertView.findViewById(R.id.rb_pickup);
        rb_onsite=(RadioButton)convertView.findViewById(R.id.rb_onsite);

        if(is_delivery.equalsIgnoreCase("0")){
            rb_delivery.setVisibility(View.GONE);
        }
        if(is_pickup.equalsIgnoreCase("0")){
            rb_pickup.setVisibility(View.GONE);
        }
        if(is_onsite_service.equalsIgnoreCase("0")){
            rb_onsite.setVisibility(View.GONE);
        }

        if(is_delivery_paid.equalsIgnoreCase("1")){
            tv_delivery_freecharge_cateringSP.setText(delivery_charges);
        }else{
            tv_delivery_freecharge_cateringSP.setVisibility(View.GONE);
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

                    deliveryPrice = 0.0;
                    tv_delivery_freecharge_cateringSP.setText("Delivery : "+Const.GLOBAL_FORMATTER.format(deliveryPrice));
                }else{
                    et_address_line.setText("");
                    et_address_line.setEnabled(true);
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
                                et_date.setText(str_month + "-" + str_day+ "-" + year );
                                et_date.setError(null);
                                str_date = Utils.convertDateStringToString(et_date.getText().toString(),"MM-dd-yyyy","yyyy-MM-dd");

                                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                                Date date = new Date(year, monthOfYear, dayOfMonth-1);
                                String selected_dayOfWeek = simpledateformat.format(date);

                                Log.e("SElected day--",selected_dayOfWeek);
                                String openTime = "";
                                String closeTime = "";

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
                /*TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                                        *//*int hour = hourOfDay;
                                        int minutes = minute;
                                        String timeSet = "";
                                        if (hour > 12) {
                                            hour -= 12;
                                            timeSet = "PM";
                                        } else if (hour == 0) {
                                            hour += 12;
                                            timeSet = "AM";
                                        } else if (hour == 12)
                                            timeSet = "PM";
                                        else
                                            timeSet = "AM";
                                        String min = "";
                                        if (minutes < 10)
                                            min = "0" + minutes;
                                        else
                                            min = String.valueOf(minutes);
                                        // Append in a StringBuilder
                                        String aTime = new StringBuilder().append(hour).append(':')
                                                .append(min).append(" ").append(timeSet).toString();*//*
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
                            str_delivery_time=Utils.convertDateStringToString(et_delivery_time.getText().toString(),"hh:mm aa","HH:mm");
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
        ll_catering_booking = (LinearLayout)convertView.findViewById(R.id. ll_catering_booking);

        ll_catering_booking.setVisibility(View.VISIBLE);
        tv_delivery_freecharge_cateringSP = (TextView) convertView.findViewById(R.id.tv_delivery_freecharge);
        tv_submitdynamic = (TextView) convertView.findViewById(R.id.tv_submitdynamic);
        tv_submitdynamic.setText("Submit");
        tv_submitdynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(booking_layout_display==false){
                    ll_catering_booking.setVisibility(View.VISIBLE);
                    booking_layout_display=true;
                    tv_submitdynamic.setText("Submit");
                }else{
                    ll_catering_booking.setVisibility(View.GONE);
                    booking_layout_display = false;



                    if(rb_delivery.isChecked()==true){
                        str_send_type= "delivery" ;
                    }else if( rb_pickup.isChecked()==true){
                        str_send_type= "pickup" ;
                    }else if(rb_onsite.isChecked()==true) {
                        str_send_type= "On Site Service";
                    }
                    if(et_date.getText().length()<0 || et_date.getText().length()<0){
                        Toast.makeText(getContext(), "Please select Date and Time.", Toast.LENGTH_LONG).show();
                          tv_submitdynamic.setText("Add Booking Details");
                    }else if(et_address_line.getText().length()<0){
                        Toast.makeText(getContext(), "Please add address for Delivery/pickup.", Toast.LENGTH_LONG).show();
                          tv_submitdynamic.setText("Add Booking Details");
                    } else if(rb_delivery.isChecked()==false && rb_pickup.isChecked()==false && rb_onsite.isChecked()==false) {
                        Toast.makeText(getContext(), "Please select Delivery / Pickup ? then Date & Time", Toast.LENGTH_LONG).show();
                          tv_submitdynamic.setText("Add Booking Details");
                    }else{
                        if(et_date.getText().length()<1){
                            Toast.makeText(getContext(), "Please add Date then Time.", Toast.LENGTH_LONG).show();
                            tv_submitdynamic.setText("Add Booking Details");
                        }else{
                            tv_submitdynamic.setText("Update Booking Details");
                            str_address = et_address_line.getText().toString();
                            new GetCartIDForSP().execute();
                        }
                    }
                }
            }
        });


        et_address_line.setAdapter(new ServiceProvidersDetailsMainFragment.GooglePlacesAutocompleteAdapter(getContext(), R.layout.text_row_list_item));
        et_address_line.setThreshold(1);
        et_address_line.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Log.d("clicked place id",placeID.get(position));
                str_place_id =placeID.get(position).toString();

                //Log.d("Log place clicked===",str_place_id);
                new ServiceProvidersDetailsMainFragment.GetPlaceDteailsViaGooglePlace().execute();
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_address_line.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                str_address = et_address_line.getText().toString();

            }
        });




        return convertView;
    }




    boolean enabletotalItem =true;
    boolean enabletotalAmount =true;
    ImageView ic_cart_actionbar;
    private static final int MENU_VENUE = Menu.FIRST;
    private static final int MENU_CATERING= Menu.FIRST + 1;
    private static final int MENU_SERVICES = Menu.FIRST + 2;
    private static final int MENU_TOTALAMOUNT = Menu.FIRST + 3;
    private static final int MENU_LOGOUT = Menu.FIRST + 4;
    public static TextView tv_cartItemCount;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.cart_option, menu);
        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.cart_badge_layout);
        RelativeLayout notifCount = (RelativeLayout)   MenuItemCompat.getActionView(item);

        tv_cartItemCount= (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
        tv_cartItemCount.setText(sharepref.getString(Const.PREF_USER_CART_TOTAL_ITEMS,""));
        ic_cart_actionbar = (ImageView) notifCount.findViewById(R.id.ic_cart_actionbar);

        ic_cart_actionbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),CartSummaryScreen.class));
            }
        });

        if(enabletotalItem)
            menu.add(0, MENU_VENUE, Menu.NONE, "Venues   Bookings : "+sharepref.getString(Const.PREF_USER_CART_VENUES,"")).setIcon(R.drawable.fab_add);
        if(enabletotalItem)
            menu.add(0, MENU_CATERING, Menu.NONE,"Catering Bookings : "+sharepref.getString(Const.PREF_USER_CART_CATERINGS,"")).setIcon(R.drawable.fab_add);
        if(enabletotalItem)
            menu.add(0, MENU_SERVICES, Menu.NONE, "Services Bookings : "+sharepref.getString(Const.PREF_USER_CART_SERVICES,"")).setIcon(R.drawable.fab_add);

        if(sharepref.getString(Const.PREF_USER_CART_TOTAL_AMOUNT,"").equalsIgnoreCase("")){
            enabletotalAmount=false;
        }

        if(enabletotalAmount)
            menu.add(0, MENU_TOTALAMOUNT, Menu.NONE, "Total Amount    : $ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(sharepref.getString(Const.PREF_USER_CART_TOTAL_AMOUNT,"0.0")) ) ).setIcon(R.drawable.ic_collapse);



        super.onCreateOptionsMenu(menu,inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
          /*  case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                finish();
                return true;*/

            //noinspection SimplifiableIfStatement
            case R.id.badge :
                startActivity(new Intent(getActivity(),CartSummaryScreen.class));
                return true;

        /*    case R.id.action_custom_indicator:
                mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                break;
            case R.id.action_custom_child_animation:
                mDemoSlider.setCustomAnimation(new ChildAnimationExample());
                break;
            case R.id.action_restore_default:
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                break;
            case R.id.action_github:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/daimajia/AndroidImageSlider"));
                startActivity(browserIntent);
                break;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class VersionHelper
    {
        public static void refreshActionBarMenu(Activity activity)
        {
            activity.invalidateOptionsMenu();
        }
    }


    String response_string;
    private class GetCartIDForSP extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog2;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //Log.d("pre execute", "Pre execute Done");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog2 = ProgressDialog.show(getContext(), "", "Adding to cart...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");

            http://ovenues.co.us/




            try {
                JSONObject req = new JSONObject();
                if(sharepref.getString(Const.PREF_USER_ID,"").equalsIgnoreCase("0") ||
                        sharepref.getString(Const.PREF_USER_ID,"")==null){
                    req.put("user_id",sharepref.getString(Const.PREF_USER_ID,"0"));
                    req.put("token",sharepref.getString(Const.PREF_USER_TOKEN,"0"));
                }else{
                    req.put("user_id",sharepref.getString(Const.PREF_USER_ID,"0"));
                }
                req.put("service_provider_id",str_service_provider_id);
                req.put("service_id","1");
                req.put("charge_id",0);
                req.put("flag","3");

                JSONObject service_charge_details_obj = new JSONObject();
                service_charge_details_obj.put("number_of_guests",0);
                service_charge_details_obj.put("quantity",0);
                service_charge_details_obj.put("total_amount",0);
                service_charge_details_obj.put("booking_date",str_date);
                service_charge_details_obj.put("booking_time",str_date+" "+str_delivery_time);
                service_charge_details_obj.put("delivery_address",str_address);

                if(str_send_type.equalsIgnoreCase("delivery")){
                    service_charge_details_obj.put("is_delivery","1");
                    service_charge_details_obj.put("is_pickup", "0");
                    service_charge_details_obj.put("is_onsite_service","0");
                }else if(str_send_type.equalsIgnoreCase("pickup")) {
                    service_charge_details_obj.put("is_delivery","0");
                    service_charge_details_obj.put("is_pickup", "1");
                    service_charge_details_obj.put("is_onsite_service","0");
                }else if(str_send_type.equalsIgnoreCase("On Site Service")) {
                    service_charge_details_obj.put("is_delivery","0");
                    service_charge_details_obj.put("is_pickup", "0");
                    service_charge_details_obj.put("is_onsite_service","1");
                }

                service_charge_details_obj.put("item_charges","0");

                if(str_send_type.equalsIgnoreCase("delivery") && is_delivery_paid.equalsIgnoreCase("1")){
                    service_charge_details_obj.put("is_delivery_paid",is_delivery_paid);
                    service_charge_details_obj.put("delivery_charges",delivery_charges);
                }else{
                    service_charge_details_obj.put("is_delivery_paid","0");
                    service_charge_details_obj.put("delivery_charges","0");
                }
                service_charge_details_obj.put("is_delivery_na",is_delivery_na);


                req.put("service_charge_details",service_charge_details_obj);


                Log.d("Req Json======", req.toString());

                String response = post(Const.SERVER_URL_API +"cart_catering_service_provider", req.toString(),"put");
                //Log.d("REsponce Json====",response);
                response_string = response;
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response_string;

        }
        @Override
        protected void onPostExecute(String result) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                super.onPostExecute(result);
            }
            //System.out.println("OnpostExecute----done-------");
            progressDialog2.dismiss();
            Log.i("RESPONSE=SP CARTID", response_string);

            if (response_string == null || response_string.equals("")) {

                progressDialog2.dismiss();
                Toast.makeText(getContext(), "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject obj = new JSONObject(response_string);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.

                String status=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(status.equals("success")){

                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(response_string).getAsJsonObject();

                    String cart_id = rootObj.getAsJsonObject("message").get("cart_id").getAsString();
                    String flag = rootObj.getAsJsonObject("message").get("flag").getAsString();
                    str_cart_ID_SP_Main = cart_id;
                    SP_cart_flag_Main =flag;



                    Snackbar snackbar = Snackbar
                            .make(getActivity().findViewById(android.R.id.content), "Details added.", Snackbar.LENGTH_LONG)
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

                    int previous_cart_item_count = Integer.parseInt(sharepref.getString(Const.PREF_USER_CART_TOTAL_ITEMS,""));
                    int new_cart_item_count = previous_cart_item_count+ 1;
                    sharepref.edit().putString(Const.PREF_USER_CART_TOTAL_ITEMS,Integer.toString(new_cart_item_count)).apply();

                    if (Build.VERSION.SDK_INT >= 11)
                    {
                        ServiceproviderGenericPricingFragment.VersionHelper.refreshActionBarMenu((Activity)getActivity());
                    }

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
            }catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

}
