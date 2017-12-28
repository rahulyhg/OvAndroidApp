package ovenues.com.ovenue.bookingdetails;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;

import ovenues.com.ovenue.MainNavigationScreen;
import ovenues.com.ovenue.R;
import ovenues.com.ovenue.VenuesSearchFilter;
import ovenues.com.ovenue.utils.ConnectionDetector;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.utils.APICall.post;

public class BookingListActivity extends AppCompatActivity {
    boolean loading_data=false;
    private int visibleThreshold = 5;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;



    RecyclerView mRecyclerView;
    ArrayList results;
    private RecyclerView.Adapter bookingListAdapter;


    Boolean isInternetPresent = false;
    SwipeRefreshLayout mSwipeRefreshLayout;
    View footerLayout;
    int int_from=0,int_to=15;

    SharedPreferences sharepref;

    public AutoCompleteTextView et_booking_search;
    String search_keyword="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Bookings");

        // new GetVenuesListCity().execute();
        et_booking_search=(AutoCompleteTextView)this.findViewById(R.id.et_booking_search);

        et_booking_search.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    // your additional processing...
                    if(et_booking_search.getText().length()>=3){

                        search_keyword = et_booking_search.getText().toString();
                        int_from=0;
                        int_to=15;
                        new GetBookingSearch().execute();
                    }else{
                        Toast.makeText(BookingListActivity.this,"Please Enter Booking ID / Vendor Name / Status .",Toast.LENGTH_LONG).show();

                    }
                }

                return false;
            }
        });


        // Fetching footer layout
        footerLayout = this.findViewById(R.id.footerView);
        footerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingListActivity.this,VenuesSearchFilter.class));
                finish();
            }
        });

        // Initialize recycler view
        mRecyclerView = (RecyclerView)this.findViewById(R.id.rv_venuelist);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(BookingListActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        results = new ArrayList<BookingListActivity>();
        bookingListAdapter = new BookingListMainAdapter(results, BookingListActivity.this);

        mRecyclerView.setAdapter(bookingListAdapter);



        mSwipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                if(et_booking_search.getText().length()>0){
                    et_booking_search.setText("");
                    search_keyword = "";
                }
                results.clear();
                bookingListAdapter.notifyDataSetChanged();

                int_from=0;
                int_to=10;

                new GetBookingList().execute();

                mRecyclerView.setEnabled(false);
                mRecyclerView.setNestedScrollingEnabled(false);
                Toast.makeText(BookingListActivity.this,"Loading...",Toast.LENGTH_LONG).show();


            }
        });


        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests

            int_from=0;
            int_to=10;

            new GetBookingList().execute();

        } else {
            Toast.makeText(BookingListActivity.this, "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();
        }


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (mRecyclerView == null || mRecyclerView.getChildCount() == 0) ? 0 : mRecyclerView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
//=========================================================================================================
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    //onHide();


                    footerLayout.animate().translationY(footerLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();

                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    //onShow();
                    footerLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

                    controlsVisible = true;
                    scrolledDistance = 0;


                }

                if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                    scrolledDistance += dy;
                }

                //======================================================================================================

                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                   /* if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            textView_loadmore.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                            textView_loadmore.setVisibility(View.VISIBLE);
                        }
                    }*/

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount /*&& (totalItemCount - visibleItemCount) <= (pastVisiblesItems + visibleThreshold)*/) {
                        // End has been reached
                        // Do something

                        if(loading_data!=true) {
                            if(int_from==0){
                                int_from=int_from+11;
                                int_to=int_to+10;
                            }else{
                                int_from=int_from+10;
                                int_to=int_to+10;
                            }
                            new GetBookingList().execute();
                        }
                    }
                }

                //=======================================================================================================
            }

            @Override
            public void onScrollStateChanged(RecyclerView mRecyclerView, int newState) {
                super.onScrollStateChanged(mRecyclerView, newState);
            }
        });



    }

    String res_booking;
    protected ProgressDialog progressDialog;
    private class GetBookingList extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(BookingListActivity.this, "Loading", "Please Wait...", true, false);
            loading_data=true;
        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {

                String str_from=Integer.toString(int_from);
                String str_to=Integer.toString(int_to);

                /*   http://54.153.127.215/api/bookings_list_for_user?index_from=1&index_to=15&user_id=&search_key=[Optional]*/

                String upend = "&index_from="+str_from+"&index_to="+str_to+"&user_id="+sharepref.getString(Const.PREF_USER_ID,"")+"&search_key="+search_keyword.trim();
                String response = post(Const.SERVER_URL_API +"user_profile_bookings?"+upend, "","get");
                 //Log.d("URL ====",Const.SERVER_URL_API +"bookings_list_for_user?"+upend);
                res_booking=response;
            } catch (Exception e) {
                e.printStackTrace();
            }


            return res_booking;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);
           // Log.i("RESPONSE res_booking", res_booking);

               try{
                JSONObject obj = new JSONObject(res_booking);

                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(res_booking).getAsJsonObject();
                    JsonArray bookings = rootObj.getAsJsonObject("message")
                            .getAsJsonArray("bookings");

                    if(bookings.size()>0){

                        for (int j = 0; j < bookings.size(); j++) {

                            String booking_type = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_type").isJsonNull()
                                    ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_type").getAsString():null;
                            //Log.e("index_selectedServiceType stoped==",""+booking_type);
                            //== BOOKING TYPE , 1= VENUE, 2 = SERVICE , 3 = CATERING SERVICE;

                            //=====THIS IS FOR VENUE AND CATERING OBJECT CODING
                            if(booking_type.equalsIgnoreCase("1")){

                                String booking_type_icon ,booking_id,booked_on ,booking_date,booking_time_from ,booking_time_to,booking_status ,number_of_guests,booking_rent ,total_amount,
                                        coupon_id ,discount_amount, venue_id ,venue_name,min_occupancy ,max_occupancy,cost_per_person;
                                JsonArray catering_price=null,restaurant_food_price =null,baverage_price=null,services=null ,extra_services=null;


                                booking_type = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_type").isJsonNull()
                                                ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_type").getAsString():null;

                                 booking_type_icon = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_type_icon").isJsonNull()
                                                 ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_type_icon").getAsString():null;
                                 booking_id = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_id").isJsonNull()
                                                 ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_id").getAsString():null;
                                 booked_on = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booked_on").isJsonNull()
                                                 ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booked_on").getAsString():null;
                                 booking_date = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_date").isJsonNull()
                                                 ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_date").getAsString():null;
                                 booking_time_from = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_time_from").isJsonNull()
                                                 ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_time_from").getAsString():null;
                                 booking_time_to = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_time_to").isJsonNull()
                                                 ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_time_to").getAsString():null;
                                 booking_status = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_status").isJsonNull()
                                                 ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_status").getAsString():null;
                                 number_of_guests = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("number_of_guests").isJsonNull()
                                                 ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("number_of_guests").getAsString():null;
                                 booking_rent = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_rent").isJsonNull()
                                                 ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_rent").getAsString():null;
                                 total_amount = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("total_amount").isJsonNull()
                                                 ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("total_amount").getAsString():null;

                                 coupon_id= bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().has("coupon_id")
                                         && !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("coupon_id").isJsonNull()
                                                 ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("coupon_id").getAsString():"";
                                 discount_amount =  bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().has("discount_amount")
                                         && !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("discount_amount").isJsonNull()
                                                 ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("discount_amount").getAsString():null;
                                 venue_id = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("venue_id").isJsonNull()
                                                 ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("venue_id").getAsString():null;
                                 venue_name = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("venue_name").isJsonNull()
                                                 ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("venue_name").getAsString():null;
                                 min_occupancy = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("min_occupancy").isJsonNull()
                                                 ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("min_occupancy").getAsString():null;
                                 max_occupancy = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("max_occupancy").isJsonNull()
                                                 ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("max_occupancy").getAsString():null;
                                 cost_per_person = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("cost_per_person").isJsonNull()
                                                ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("cost_per_person").getAsString():null;


                                 if(bookings.get(j).getAsJsonObject().has("catering_price")) {
                                     catering_price = !bookings.get(j).getAsJsonObject().get("catering_price").isJsonNull()
                                             ? bookings.get(j).getAsJsonObject().get("catering_price").getAsJsonArray() : null;
                                 }
                                if(bookings.get(j).getAsJsonObject().has("restaurant_food_price")) {
                                    restaurant_food_price = !bookings.get(j).getAsJsonObject().get("restaurant_food_price").isJsonNull()
                                            ? bookings.get(j).getAsJsonObject().get("restaurant_food_price").getAsJsonArray() : null;
                                }
                                if(bookings.get(j).getAsJsonObject().has("baverage_price")) {
                                    baverage_price = !bookings.get(j).getAsJsonObject().get("baverage_price").isJsonNull()
                                            ? bookings.get(j).getAsJsonObject().get("baverage_price").getAsJsonArray() : null;
                                }
                                if(bookings.get(j).getAsJsonObject().has("services")) {
                                     services  = !bookings.get(j).getAsJsonObject().get("services").isJsonNull()
                                            ? bookings.get(j).getAsJsonObject().get("services").getAsJsonArray():null;

                                }
                                if(bookings.get(j).getAsJsonObject().has("extra_services")) {
                                    extra_services = !bookings.get(j).getAsJsonObject().get("extra_services").isJsonNull()
                                            ? bookings.get(j).getAsJsonObject().get("extra_services").getAsJsonArray() : null;
                                }

                                results.add(new BookingVenueModel(booking_type,  booking_type_icon,  booking_id,  booked_on,  booking_date,  booking_time_from,  booking_time_to,  booking_status,
                                        number_of_guests,  booking_rent,  total_amount,  coupon_id,  discount_amount,  venue_id,  venue_name,  min_occupancy,  max_occupancy,  cost_per_person,
                                         catering_price,  restaurant_food_price,  baverage_price,  services,  extra_services));

                                 //Log.e("result set booking---",""+result.toString());

                            } else if(booking_type.equalsIgnoreCase("2") || booking_type.equalsIgnoreCase("3")){

                                String booking_type_icon = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_type_icon").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_type_icon").getAsString():null;

                                String booking_id = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_id").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_id").getAsString():null;
                                //Log.e("id---",id);
                                String booked_on  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booked_on").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booked_on").getAsString():null;

                                String booking_date  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_date").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_date").getAsString():null;

                                String booking_time  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_time").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_time").getAsString():null;

                                String service_hours  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("service_hours").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("service_hours").getAsString():null;

                                String booking_status  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_status").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_status").getAsString():null;

                                String number_of_guests  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("number_of_guests").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("number_of_guests").getAsString():null;

                                String quantity  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("quantity").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("quantity").getAsString():null;

                                String total_amount  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("total_amount").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("total_amount").getAsString():null;


                                String coupon_id  = bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().has("coupon_id")
                                        && !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("coupon_id").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("coupon_id").getAsString():null;

                                String discount_amount  =  bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().has("discount_amount")
                                        && !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("discount_amount").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("discount_amount").getAsString():null;

                                String service_id  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("service_id").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("service_id").getAsString():null;


                                String note  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("note").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("note").getAsString():null;


                                String service_provider_id  =  bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().has("service_provider_id")
                                        ?!bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("service_provider_id").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("service_provider_id").getAsString():null
                                        :null;

                                String provider_name  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("provider_name").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("provider_name").getAsString():null;


                                String is_delivery  =  !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_delivery").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_delivery").getAsString():null;


                                String is_delivery_paid  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_delivery_paid").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_delivery_paid").getAsString():null;

                                String is_pickup  =  !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_pickup").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_pickup").getAsString():null;


                                String is_onsite_service  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_onsite_service").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_onsite_service").getAsString():null;

                                String is_delivery_na  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_delivery_na").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_delivery_na").getAsString():null;

                                String delivery_address = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("delivery_address").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("delivery_address").getAsString():null;



                                if(booking_type.equalsIgnoreCase("2")){

                                    String service_title = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("service_title").isJsonNull()
                                            ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("service_title").getAsString():"";

                                    results.add(new BookingVenueModel(booking_type,  booking_type_icon,  booking_id,  booked_on,  booking_date,  booking_time,  service_hours,  booking_status,
                                            number_of_guests,  quantity,  total_amount,  coupon_id,  discount_amount,  service_id,  note,  service_provider_id,  provider_name,  is_delivery,
                                            is_delivery_paid,  is_pickup,  is_onsite_service,  is_delivery_na,  delivery_address,  service_title,
                                            null,null,null,null,null));
                                }else if(booking_type.equalsIgnoreCase("3")) {

                                    JsonArray catering_price=null, restaurant_food_price=null, baverage_price=null, services=null, extra_services=null;

                                    if (bookings.get(j).getAsJsonObject().has("catering_price")) {

                                        catering_price = !bookings.get(j).getAsJsonObject().get("catering_price").isJsonNull()
                                                ? bookings.get(j).getAsJsonObject().get("catering_price").getAsJsonArray() : null;
                                    }

                                    if (bookings.get(j).getAsJsonObject().has("restaurant_food_price")) {

                                    restaurant_food_price = !bookings.get(j).getAsJsonObject().get("restaurant_food_price").isJsonNull()
                                            ? bookings.get(j).getAsJsonObject().get("restaurant_food_price").getAsJsonArray() : null;
                                    }

                                    if(bookings.get(j).getAsJsonObject().has("baverage_price")) {
                                        baverage_price = !bookings.get(j).getAsJsonObject().get("baverage_price").isJsonNull()
                                                ? bookings.get(j).getAsJsonObject().get("baverage_price").getAsJsonArray() : null;
                                    }
                                    results.add(new BookingVenueModel(booking_type,  booking_type_icon,  booking_id,  booked_on,  booking_date,  booking_time,  service_hours,  booking_status,
                                            number_of_guests,  quantity,  total_amount,  coupon_id,  discount_amount,  service_id,  note,  service_provider_id,  provider_name,  is_delivery,
                                            is_delivery_paid,  is_pickup,  is_onsite_service,  is_delivery_na,  delivery_address,  "",  catering_price,
                                            restaurant_food_price,  baverage_price,  null,  null));
                                }
                            }
                            //Log.d("for start",service_name);
                        }

                        if(mSwipeRefreshLayout.isRefreshing()){
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        bookingListAdapter.notifyDataSetChanged();
                    }else{
                        /*results.clear();
                        bookingListAdapter.notifyDataSetChanged();*/
                        loading_data=false;
                        progressDialog.dismiss();

                        final AlertDialog.Builder alertbox = new AlertDialog.Builder(BookingListActivity.this);
                        alertbox.setMessage("No data found.");
                        alertbox.setTitle("Sorry ! ");
                        alertbox.setIcon(R.mipmap.ic_launcher);

                        alertbox.setNeutralButton("OK",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0,int arg1) {
                                        onBackPressed();
                                    }
                                });
                        alertbox.show();
                    }
                }else{
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


                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(BookingListActivity.this);
                    alertbox.setMessage(message);
                    alertbox.setTitle("Sorry ! ");
                    alertbox.setIcon(R.mipmap.ic_launcher);

                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0,int arg1) {
                                    startActivity(new Intent(BookingListActivity.this,MainNavigationScreen.class));
                                    finish();
                                }
                            });
                    alertbox.show();
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(mSwipeRefreshLayout.isRefreshing()){
                mSwipeRefreshLayout.setRefreshing(false);
            }
            loading_data=false;
            progressDialog.dismiss();
        }
    }

    //protected ProgressDialog progressDialog3;
    private class GetBookingSearch extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            //progressDialog3 = ProgressDialog.show(BookingListActivity.this, "Loading", "Please Wait...", true, false);
        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {

                String str_from=Integer.toString(int_from);
                String str_to=Integer.toString(int_to);

                /*   http://54.153.127.215/api/bookings_list_for_user?index_from=1&index_to=15&user_id=&search_key=[Optional]*/

                String upend = "&index_from="+str_from+"&index_to="+str_to+"&user_id="+sharepref.getString(Const.PREF_USER_ID,"")+"&search_key="+search_keyword.trim();
                String response = post(Const.SERVER_URL_API +"user_profile_bookings?"+upend, "","get");
                //.d("URL ====",Const.SERVER_URL_API +"bookings_list_for_user?"+upend);
                res_booking=response;
            } catch (Exception e) {
                e.printStackTrace();
            }


            return res_booking;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);
            //Log.i("RESPONSE res_booking", res_booking);

            try{
                JSONObject obj = new JSONObject(res_booking);

                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(res_booking).getAsJsonObject();
                    JsonArray bookings = rootObj.getAsJsonObject("message")
                            .getAsJsonArray("bookings");

                    results.clear();
                    bookingListAdapter.notifyDataSetChanged();
                    if(bookings.size()>0){

                        for (int j = 0; j < bookings.size(); j++) {

                            String booking_type = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_type").isJsonNull()
                                    ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_type").getAsString():null;
                            //Log.e("index_selectedServiceType stoped==",""+booking_type);
                            //== BOOKING TYPE , 1= VENUE, 2 = SERVICE , 3 = CATERING SERVICE;

                            //=====THIS IS FOR VENUE AND CATERING OBJECT CODING
                            if(booking_type.equalsIgnoreCase("1")){

                                String booking_type_icon ,booking_id,booked_on ,booking_date,booking_time_from ,booking_time_to,booking_status ,number_of_guests,booking_rent ,total_amount,
                                        coupon_id ,discount_amount, venue_id ,venue_name,min_occupancy ,max_occupancy,cost_per_person;
                                JsonArray catering_price,restaurant_food_price ,baverage_price,services ,extra_services;


                                booking_type = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_type").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_type").getAsString():null;

                                booking_type_icon = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_type_icon").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_type_icon").getAsString():null;
                                booking_id = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_id").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_id").getAsString():null;
                                booked_on = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booked_on").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booked_on").getAsString():null;
                                booking_date = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_date").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_date").getAsString():null;
                                booking_time_from = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_time_from").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_time_from").getAsString():null;
                                booking_time_to = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_time_to").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_time_to").getAsString():null;
                                booking_status = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_status").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_status").getAsString():null;
                                number_of_guests = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("number_of_guests").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("number_of_guests").getAsString():null;
                                booking_rent = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_rent").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_rent").getAsString():null;
                                total_amount = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("total_amount").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("total_amount").getAsString():null;

                                coupon_id= bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().has("coupon_id")
                                        && !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("coupon_id").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("coupon_id").getAsString():"";
                                discount_amount =  bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().has("discount_amount")
                                        && !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("discount_amount").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("discount_amount").getAsString():null;
                                venue_id = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("venue_id").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("venue_id").getAsString():null;
                                venue_name = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("venue_name").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("venue_name").getAsString():null;
                                min_occupancy = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("min_occupancy").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("min_occupancy").getAsString():null;
                                max_occupancy = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("max_occupancy").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("max_occupancy").getAsString():null;
                                cost_per_person = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("cost_per_person").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("cost_per_person").getAsString():null;


                                catering_price = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("catering_price").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("catering_price").getAsJsonArray():null;
                                restaurant_food_price  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("restaurant_food_price").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("restaurant_food_price").getAsJsonArray():null;
                                baverage_price  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("baverage_price").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("baverage_price").getAsJsonArray():null;
                                services  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("services").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("services").getAsJsonArray():null;
                                extra_services  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("extra_services").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("extra_services").getAsJsonArray():null;

                                results.add(new BookingVenueModel(booking_type,  booking_type_icon,  booking_id,  booked_on,  booking_date,  booking_time_from,  booking_time_to,  booking_status,
                                        number_of_guests,  booking_rent,  total_amount,  coupon_id,  discount_amount,  venue_id,  venue_name,  min_occupancy,  max_occupancy,  cost_per_person,
                                        catering_price,  restaurant_food_price,  baverage_price,  services,  extra_services));

                            } else if(booking_type.equalsIgnoreCase("2") || booking_type.equalsIgnoreCase("3")){

                                String booking_type_icon = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_type_icon").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_type_icon").getAsString():null;

                                String booking_id = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_id").isJsonNull()
                                        ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_id").getAsString():null;
                                //Log.e("id---",id);
                                String booked_on  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booked_on").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booked_on").getAsString():null;

                                String booking_date  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_date").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_date").getAsString():null;

                                String booking_time  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_time").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_time").getAsString():null;

                                String service_hours  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("service_hours").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("service_hours").getAsString():null;

                                String booking_status  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_status").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("booking_status").getAsString():null;

                                String number_of_guests  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("number_of_guests").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("number_of_guests").getAsString():null;

                                String quantity  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("quantity").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("quantity").getAsString():null;

                                String total_amount  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("total_amount").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("total_amount").getAsString():null;


                                String coupon_id  = bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().has("coupon_id")
                                        && !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("coupon_id").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("coupon_id").getAsString():null;

                                String discount_amount  =  bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().has("discount_amount")
                                        && !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("discount_amount").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("discount_amount").getAsString():null;

                                String service_id  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("service_id").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("service_id").getAsString():null;


                                String note  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("note").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("note").getAsString():null;


                                String service_provider_id  =  !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("service_provider_id").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("service_provider_id").getAsString():null;

                                String provider_name  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("provider_name").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("provider_name").getAsString():null;


                                String is_delivery  =  !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_delivery").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_delivery").getAsString():null;


                                String is_delivery_paid  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_delivery_paid").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_delivery_paid").getAsString():null;

                                String is_pickup  =  !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_pickup").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_pickup").getAsString():null;


                                String is_onsite_service  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_onsite_service").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_onsite_service").getAsString():null;

                                String is_delivery_na  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_delivery_na").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("is_delivery_na").getAsString():null;

                                String delivery_address = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("delivery_address").isJsonNull()
                                        ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("delivery_address").getAsString():null;



                                if(booking_type.equalsIgnoreCase("2")){

                                    String service_title = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("service_title").isJsonNull()
                                            ?bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("service_title").getAsString():"";

                                    results.add(new BookingVenueModel(booking_type,  booking_type_icon,  booking_id,  booked_on,  booking_date,  booking_time,  service_hours,  booking_status,
                                            number_of_guests,  quantity,  total_amount,  coupon_id,  discount_amount,  service_id,  note,  service_provider_id,  provider_name,  is_delivery,
                                            is_delivery_paid,  is_pickup,  is_onsite_service,  is_delivery_na,  delivery_address,  service_title,
                                            null,null,null,null,null));
                                }else if(booking_type.equalsIgnoreCase("3")){

                                    JsonArray catering_price,restaurant_food_price ,baverage_price,services ,extra_services;

                                    catering_price = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("catering_price").isJsonNull()
                                            ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("catering_price").getAsJsonArray():null;
                                    restaurant_food_price  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("restaurant_food_price").isJsonNull()
                                            ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("restaurant_food_price").getAsJsonArray():null;
                                    baverage_price  = !bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("baverage_price").isJsonNull()
                                            ? bookings.get(j).getAsJsonObject().get("booking_detail").getAsJsonObject().get("baverage_price").getAsJsonArray():null;

                                    results.add(new BookingVenueModel(booking_type,  booking_type_icon,  booking_id,  booked_on,  booking_date,  booking_time,  service_hours,  booking_status,
                                            number_of_guests,  quantity,  total_amount,  coupon_id,  discount_amount,  service_id,  note,  service_provider_id,  provider_name,  is_delivery,
                                            is_delivery_paid,  is_pickup,  is_onsite_service,  is_delivery_na,  delivery_address,  "",  catering_price,
                                            restaurant_food_price,  baverage_price,  null,  null));
                                }
                            }
                            //Log.d("for start",service_name);
                        }

                        if(mSwipeRefreshLayout.isRefreshing()){
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        bookingListAdapter.notifyDataSetChanged();
                    }else{
                        /*results.clear();
                        bookingListAdapter.notifyDataSetChanged();*/
                        loading_data=false;
                        progressDialog.dismiss();

                        final AlertDialog.Builder alertbox = new AlertDialog.Builder(BookingListActivity.this);
                        alertbox.setMessage("No data found.");
                        alertbox.setTitle("Sorry ! ");
                        alertbox.setIcon(R.mipmap.ic_launcher);

                        alertbox.setNeutralButton("OK",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0,int arg1) {
                                        arg0.dismiss();
                                        startActivity(new Intent(BookingListActivity.this,BookingListActivity.class));
                                        finish();
                                    }
                                });
                        alertbox.show();
                    }
                }else{
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


                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(BookingListActivity.this);
                    alertbox.setMessage(message);
                    alertbox.setTitle("Sorry ! ");
                    alertbox.setIcon(R.mipmap.ic_launcher);

                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0,int arg1) {
                                   arg0.dismiss();
                                }
                            });
                    alertbox.show();
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(mSwipeRefreshLayout.isRefreshing()){
                mSwipeRefreshLayout.setRefreshing(false);
            }
            loading_data=false;
            progressDialog.dismiss();
        }
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();


        finish();
       /* if(sharepref.getString("key_login","yes").equals("yes")){

            finish();
        }else{
            System.exit(0);
            finish();
        }*/
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
