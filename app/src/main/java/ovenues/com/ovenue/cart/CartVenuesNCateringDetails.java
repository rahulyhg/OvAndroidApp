package ovenues.com.ovenue.cart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.cart.CartItemStickyHeader.CartVenueItemsAdapter;
import ovenues.com.ovenue.cart.CartItemStickyHeader.HeaderItemCartVenues;
import ovenues.com.ovenue.VenueOrderServicesStickyHeader.TopSnappedStickyLayoutManagerVenueOrderServices;
import ovenues.com.ovenue.modelpojo.Cart.CartItemsModel;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.utils.APICall.post;


public class CartVenuesNCateringDetails extends AppCompatActivity {


    public static TextView tv_total_cart;

    public static CartVenueItemsAdapter mAdapterCartItems ;
    public static ArrayList<CartItemsModel> cartItemList;
    SharedPreferences sharepref;

    public static String cart_details_cart_id,cart_details_detailView;/* for 1 = venues details
                for 2 = catering details comes from intent*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_venues_details);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setStatusBarColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimaryDark));

        cart_details_cart_id= getIntent().getStringExtra("cart_id").toString();
        cart_details_detailView= getIntent().getStringExtra("detailView").toString();

        if(cart_details_detailView.equalsIgnoreCase("1")){
            getSupportActionBar().setTitle("Venue Booking Details");
        }else if(cart_details_detailView.equalsIgnoreCase("2")){
            getSupportActionBar().setTitle("Catering Booking Details");
        }

        cartItemList=new ArrayList<CartItemsModel>();
        mAdapterCartItems = new CartVenueItemsAdapter(CartVenuesNCateringDetails.this,cartItemList);


        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        tv_total_cart = (TextView) this.findViewById(R.id.tv_total_cart);
        final RecyclerView mRecyclerView = (RecyclerView)this.findViewById(R.id.recyclerview_venueService);

        StickyLayoutManager layoutManager = new TopSnappedStickyLayoutManagerVenueOrderServices(CartVenuesNCateringDetails.this, mAdapterCartItems);
        layoutManager.elevateHeaders(true);
        // Default elevation of 5dp
        // You can also specify a specific dp for elevation
        layoutManager.elevateHeaders(0);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapterCartItems);
        layoutManager.setStickyHeaderListener(new StickyHeaderListener() {
            @Override
            public void headerAttached(View headerView, int adapterPosition) {
                //Log.d("Listener", "Attached with position: " + adapterPosition);
            }

            @Override
            public void headerDetached(View headerView, int adapterPosition) {
                //Log.d("Listener", "Detached with position: " + adapterPosition);
            }
        });

        new GetCartItems().execute();

    }

    String res;
    class GetCartItems extends AsyncTask<Object, Void, String> {
        protected ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //pg_bar.setVisibility(View.VISIBLE);

            progressDialog = ProgressDialog.show(CartVenuesNCateringDetails.this, "Loading", "Please wait...", true, false);
        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {
                String response;
                if(sharepref.getString(Const.PREF_USER_ID,"").equalsIgnoreCase("0")){
                    response= post(Const.SERVER_URL_API +"cart_whole_detail?user_id="+sharepref.getString(Const.PREF_USER_ID,"0")+"&token="+sharepref.getString(Const.PREF_USER_TOKEN,""), "","get");
                }else{
                     response = post(Const.SERVER_URL_API +"cart_whole_detail?user_id="+sharepref.getString(Const.PREF_USER_ID,"0"), "","get");
                }
                res=response;
            }/*catch (JSONException e) {
                e.printStackTrace();
            }*/ catch (IOException e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);
            String got_cart_id,flag ,venue_id,booking_date,booking_time_from ,booking_time_to,number_of_guests ,booking_rent,venue_name,total_amount;
            //Log.d("Cart_Details",res);

            String user_id ,service_provider_id ,provider_name,booking_time,quantity,catering_food_pric;

            try{JSONObject obj = new JSONObject(res);

                response_string=obj.getString("status");

                if(response_string.equals("success")){

                    JsonParser parser = new JsonParser();

                    JsonObject rootObj = parser.parse(res).getAsJsonObject();

                    JsonArray venue_price_Obj_array,catering_service_provider_details;


                    if(cart_details_detailView.equalsIgnoreCase("1")) {
                        venue_price_Obj_array = rootObj.getAsJsonObject("message").getAsJsonArray("venue_details");
                        if(venue_price_Obj_array.size()<1 || venue_price_Obj_array==null){
                            startActivity(new Intent(CartVenuesNCateringDetails.this, CartSummaryScreen.class));
                            finish();
                        }
                        cartItemList.clear();
                        for (int a = 0; a < venue_price_Obj_array.size(); a++) {

                            got_cart_id = !venue_price_Obj_array.get(a).getAsJsonObject().get("cart_id").isJsonNull()
                                    ? venue_price_Obj_array.get(a).getAsJsonObject().get("cart_id").getAsString() : null;
                            flag = !venue_price_Obj_array.get(a).getAsJsonObject().get("flag").isJsonNull()
                                    ? venue_price_Obj_array.get(a).getAsJsonObject().get("flag").getAsString() : null;
                            venue_id = !venue_price_Obj_array.get(a).getAsJsonObject().get("venue_id").isJsonNull()
                                    ? venue_price_Obj_array.get(a).getAsJsonObject().get("venue_id").getAsString() : null;
                            booking_date = !venue_price_Obj_array.get(a).getAsJsonObject().get("booking_date").isJsonNull()
                                    ? venue_price_Obj_array.get(a).getAsJsonObject().get("booking_date").getAsString() : null;
                            booking_time_from = !venue_price_Obj_array.get(a).getAsJsonObject().get("booking_time_from").isJsonNull()
                                    ? venue_price_Obj_array.get(a).getAsJsonObject().get("booking_time_from").getAsString() : null;
                            booking_time_to = !venue_price_Obj_array.get(a).getAsJsonObject().get("booking_time_to").isJsonNull()
                                    ? venue_price_Obj_array.get(a).getAsJsonObject().get("booking_time_to").getAsString() : null;
                            number_of_guests = !venue_price_Obj_array.get(a).getAsJsonObject().get("number_of_guests").isJsonNull()
                                    ? venue_price_Obj_array.get(a).getAsJsonObject().get("number_of_guests").getAsString() : null;
                            ;
                            booking_rent = !venue_price_Obj_array.get(a).getAsJsonObject().get("booking_rent").isJsonNull()
                                    ? venue_price_Obj_array.get(a).getAsJsonObject().get("booking_rent").getAsString() : null;
                            venue_name = !venue_price_Obj_array.get(a).getAsJsonObject().get("venue_name").isJsonNull()
                                    ? venue_price_Obj_array.get(a).getAsJsonObject().get("venue_name").getAsString() : null;
                            total_amount = !venue_price_Obj_array.get(a).getAsJsonObject().get("total_amount").isJsonNull()
                                    ? venue_price_Obj_array.get(a).getAsJsonObject().get("total_amount").getAsString() : null;

                            if (got_cart_id.equalsIgnoreCase(cart_details_cart_id)) {

                                if(cart_details_detailView.equalsIgnoreCase("1")){
                                    getSupportActionBar().setTitle(venue_name);
                                }else if(cart_details_detailView.equalsIgnoreCase("2")){
                                    getSupportActionBar().setTitle("Catering Booking Details");
                                }

                                cartItemList.add(new HeaderItemCartVenues(got_cart_id, flag, venue_id, booking_date, booking_time_from, booking_time_to, number_of_guests, booking_rent,
                                        venue_name, "1", "", "", "", venue_name, "", "", total_amount,"","","",null,null,null,null,null, 0));
                                cartItemList.add(new CartItemsModel(got_cart_id, flag, venue_id, booking_date, booking_time_from, booking_time_to, number_of_guests, booking_rent,
                                        venue_name, "1", "", "", "", venue_name, "", "", "","","","",null,null,null,null,null, 1));
                                Log.d("pricing_plan", "done");
                       /* mVenueOrdCatType
                            1=pricingplan;2=food;3=catering;4=beverage;5=service;6=extra_service*/
                       /*id types =
                       * id1=menu_id; id2 = course_id;  id3=item_id(Generic ID);*/


                                //===CATERING MENU ARRAY STARTS HERE====================================================================================================

                                JsonArray venue_catering_price_Obj = venue_price_Obj_array.get(a).getAsJsonObject().getAsJsonArray("booking_food_menu");

                                if(venue_catering_price_Obj.size()>0) {

                                    cartItemList.add(new HeaderItemCartVenues(got_cart_id, flag, venue_id, booking_date, booking_time_from, booking_time_to, number_of_guests, booking_rent,
                                            venue_name, "3", "", "", "", "Catering Order", "", "", "", "", "", "", venue_catering_price_Obj, null, null, null, null, 0));

                                    cartItemList.add(new CartItemsModel(got_cart_id, flag, venue_id, booking_date, booking_time_from, booking_time_to, number_of_guests, booking_rent,
                                            venue_name, "3", "", "", "", "", "", "", "", "", "", "", venue_catering_price_Obj, null, null, null, null, 1));
                                }

                                //===RESTAURANT_FOOD_PRICE MENU ARRAY STARTS HERE====================================================================================================

                                JsonArray venue_restaurant_food_price_Obj = venue_price_Obj_array.get(a).getAsJsonObject().getAsJsonArray("booking_food");
                                for (int j = 0; j < venue_restaurant_food_price_Obj.size(); j++) {

                                    String mVenueOrdCatType = "2";
                                    String id2 = !venue_restaurant_food_price_Obj.get(j).getAsJsonObject().get("menu_id").isJsonNull()
                                            ? venue_restaurant_food_price_Obj.get(j).getAsJsonObject().get("menu_id").getAsString() : null;
                                    String menu_desc = !venue_restaurant_food_price_Obj.get(j).getAsJsonObject().get("menu_desc").isJsonNull()
                                            ? venue_restaurant_food_price_Obj.get(j).getAsJsonObject().get("menu_desc").getAsString() : null;

                                    if (j == 0) {

                                        double final_total_food=0;
                                        for (int j1 = 0; j1 < venue_restaurant_food_price_Obj.size(); j1++) {
                                            JsonArray array_item_menu1 = !venue_restaurant_food_price_Obj.get(j).getAsJsonObject().getAsJsonArray("items").isJsonNull()
                                                    ? venue_restaurant_food_price_Obj.get(j1).getAsJsonObject().getAsJsonArray("items") : null;
                                            for (int start = 0; start < array_item_menu1.size(); start++) {
                                                final_total_food = final_total_food + Double.parseDouble(array_item_menu1.get(start).getAsJsonObject().get("total_item_price").getAsString());
                                               // Log.e("food total-=====", Double.toString(final_total_food));
                                            }
                                        }

                                        cartItemList.add(new HeaderItemCartVenues(got_cart_id, flag, venue_id, booking_date, booking_time_from, booking_time_to, number_of_guests, booking_rent,
                                                venue_name, mVenueOrdCatType, "", id2, "", "Food Items", "", "", "","","",Double.toString(final_total_food),null,null,null,null,null, 0)); //===passing total in delivery address Parms
                                    }


                                    JsonArray array_item_menu = !venue_restaurant_food_price_Obj.get(j).getAsJsonObject().getAsJsonArray("items").isJsonNull()
                                            ? venue_restaurant_food_price_Obj.get(j).getAsJsonObject().getAsJsonArray("items") : null;

                                    if (!array_item_menu.isJsonNull() && array_item_menu.size() > 0) {


                                        for (int l = 0; l < array_item_menu.size(); l++) {

                                            String id3 = array_item_menu.get(l).getAsJsonObject().get("item_id").getAsString();
                                            String item_name = array_item_menu.get(l).getAsJsonObject().get("item_name").getAsString();
                                            String guest_count = array_item_menu.get(l).getAsJsonObject().get("guest_count").getAsString();
                                            String price_per_plate = array_item_menu.get(l).getAsJsonObject().get("price_per_plate").getAsString();
                                            String total_item_price = array_item_menu.get(l).getAsJsonObject().get("total_item_price").getAsString();

                                            cartItemList.add(new CartItemsModel(got_cart_id, flag, venue_id, booking_date, booking_time_from, booking_time_to, number_of_guests, booking_rent,
                                                    venue_name, mVenueOrdCatType, "", id2, id3, menu_desc + " > " + item_name, guest_count, price_per_plate, total_item_price,"","","",
                                                    null,venue_restaurant_food_price_Obj,null,null,null, 1));
                                            Log.d("food menu", "done");
                                        }
                                    }

                                }

                                //===BAVERAGE_PRICE MENU ARRAY STARTS HERE====================================================================================================

                                JsonArray venue_baverage_price_Obj = venue_price_Obj_array.get(a).getAsJsonObject().getAsJsonArray("booking_beverages");

                                for (int j = 0; j < venue_baverage_price_Obj.size(); j++) {

                                    String mVenueOrdCatType = "4";
                                    String id3 = !venue_baverage_price_Obj.get(j).getAsJsonObject().get("beverage_id").isJsonNull()
                                            ? venue_baverage_price_Obj.get(j).getAsJsonObject().get("beverage_id").getAsString() : null;
                                    String option_desc = !venue_baverage_price_Obj.get(j).getAsJsonObject().get("option_desc").isJsonNull()
                                            ? venue_baverage_price_Obj.get(j).getAsJsonObject().get("option_desc").getAsString() : null;
                                    String option_charges = !venue_baverage_price_Obj.get(j).getAsJsonObject().get("option_charges").isJsonNull()
                                            ? venue_baverage_price_Obj.get(j).getAsJsonObject().get("option_charges").getAsString() : null;
                                    String guest_count = !venue_baverage_price_Obj.get(j).getAsJsonObject().get("guest_count").isJsonNull()
                                            ? venue_baverage_price_Obj.get(j).getAsJsonObject().get("guest_count").getAsString() : null;
                                    String total_baverage_price = !venue_baverage_price_Obj.get(j).getAsJsonObject().get("total_baverage_price").isJsonNull()
                                            ? venue_baverage_price_Obj.get(j).getAsJsonObject().get("total_baverage_price").getAsString() : null;
                                    String is_hour_extn_changes = !venue_baverage_price_Obj.get(j).getAsJsonObject().get("is_hour_extn_changes").isJsonNull()
                                            ? venue_baverage_price_Obj.get(j).getAsJsonObject().get("is_hour_extn_changes").getAsString() : null;
                                    String extension_charges = !venue_baverage_price_Obj.get(j).getAsJsonObject().get("extension_charges").isJsonNull()
                                            ? venue_baverage_price_Obj.get(j).getAsJsonObject().get("extension_charges").getAsString() : null;

                                    if (j == 0) {

                                        double final_total_beverages =0;
                                        for(int start = 0; start<venue_baverage_price_Obj.size();start++){
                                            final_total_beverages = final_total_beverages
                                                    + Double.parseDouble(!venue_baverage_price_Obj.get(start).getAsJsonObject().get("total_baverage_price").isJsonNull()
                                                    ? venue_baverage_price_Obj.get(start).getAsJsonObject().get("total_baverage_price").getAsString() : "0");
                                        }
                                        cartItemList.add(new HeaderItemCartVenues(got_cart_id, flag, venue_id, booking_date, booking_time_from, booking_time_to, number_of_guests, booking_rent,
                                                venue_name, "4", "", "", "", "Beverages", "", "", "","","",Double.toString(final_total_beverages),null,null,null,null,null, 0));
                                    }


                                    cartItemList.add(new CartItemsModel(got_cart_id, flag, venue_id, booking_date, booking_time_from, booking_time_to, number_of_guests, booking_rent,
                                            venue_name, mVenueOrdCatType, "", "", id3, option_desc, guest_count, option_charges, total_baverage_price,"","","",null,null,venue_baverage_price_Obj,null,null, 1));
                                    Log.d("beverages menu", "done");

                                }

                                //===SERVICES ARRAY STARTS HERE====================================================================================================

                                JsonArray venue_services_Obj = venue_price_Obj_array.get(a).getAsJsonObject().getAsJsonArray("booking_services");
                                for (int j = 0; j < venue_services_Obj.size(); j++) {

                                    String mVenueOrdCatType = "5";
                                    String id2 = !venue_services_Obj.get(j).getAsJsonObject().get("service_id").isJsonNull()
                                            ? venue_services_Obj.get(j).getAsJsonObject().get("service_id").getAsString() : null;
                                    String service_name = !venue_services_Obj.get(j).getAsJsonObject().get("service_name").isJsonNull()
                                            ? venue_services_Obj.get(j).getAsJsonObject().get("service_name").getAsString() : null;

                                    JsonArray array_service_options = !venue_services_Obj.get(j).getAsJsonObject().getAsJsonArray("service_options").isJsonNull()
                                            ? venue_services_Obj.get(j).getAsJsonObject().getAsJsonArray("service_options") : null;

                                    double final_total_service =0;
                                    if (!array_service_options.isJsonNull() && array_service_options.size() > 0) {
                                        //Log.d("service array===",""+array_service_options);
                                        for (int start = 0; start < array_service_options.size(); start++) {
                                            final_total_service=final_total_service
                                                    +Double.parseDouble( !array_service_options.get(start).getAsJsonObject().get("total_service_charges").isJsonNull()
                                                    ? array_service_options.get(start).getAsJsonObject().get("total_service_charges").getAsString() : "0");
                                        }
                                    }
                                    cartItemList.add(new HeaderItemCartVenues(got_cart_id, flag, venue_id, booking_date, booking_time_from, booking_time_to, number_of_guests, booking_rent,
                                            venue_name, mVenueOrdCatType, "", id2, "", service_name, "1", "", "","","",Double.toString(final_total_service), null,null,null,null,null,0));


                                    if (!array_service_options.isJsonNull() && array_service_options.size() > 0) {

                                        //Log.d("service array===",""+array_service_options);

                                        for (int l = 0; l < array_service_options.size(); l++) {
                                            String id3 = !array_service_options.get(l).getAsJsonObject().get("service_option_id").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("service_option_id").getAsString() : null;
                                            String option_desc = !array_service_options.get(l).getAsJsonObject().get("option_desc").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("option_desc").getAsString() : null;
                                            String option_charges = !array_service_options.get(l).getAsJsonObject().get("option_charges").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("option_charges").getAsString() : null;
                                            String option_details = !array_service_options.get(l).getAsJsonObject().get("option_details").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("option_details").getAsString() : null;
                                            String is_per_person_charges = !array_service_options.get(l).getAsJsonObject().get("is_per_person_charges").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("is_per_person_charges").getAsString() : null;
                                            String is_per_hour_charges = !array_service_options.get(l).getAsJsonObject().get("is_per_hour_charges").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("is_per_hour_charges").getAsString() : null;
                                            String is_hour_extn_changes = !array_service_options.get(l).getAsJsonObject().get("is_hour_extn_changes").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("is_hour_extn_changes").getAsString() : null;
                                            String extension_charges = !array_service_options.get(l).getAsJsonObject().get("extension_charges").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("extension_charges").getAsString() : null;

                                            String is_group_size = !array_service_options.get(l).getAsJsonObject().get("is_group_size").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("is_group_size").getAsString() : null;
                                            String group_size_from = !array_service_options.get(l).getAsJsonObject().get("group_size_from").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("group_size_from").getAsString() : null;
                                            String group_size_to = !array_service_options.get(l).getAsJsonObject().get("group_size_to").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("group_size_to").getAsString() : null;

                                            String total_service_charges = !array_service_options.get(l).getAsJsonObject().get("total_service_charges").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("total_service_charges").getAsString() : null;

                                            /*if(is_per_person_charges.equalsIgnoreCase("1")){
                                                double extraguest=0;
                                                if(is_group_size.equalsIgnoreCase("1")){
                                                    if(Double.parseDouble(number_of_guests)>Double.parseDouble(group_size_to)){
                                                        extraguest = Double.parseDouble(number_of_guests) - Double.parseDouble(group_size_to);
                                                        total_service_charges = Double.toString(Double.parseDouble(option_charges) + (Double.parseDouble(option_charges) * extraguest));
                                                    }else{
                                                        total_service_charges = Double.toString(Double.parseDouble(option_charges) * Double.parseDouble(number_of_guests));
                                                    }
                                                }
                                            }else if(is_hour_extn_changes.equalsIgnoreCase("1")){
                                                total_service_charges = Double.toString(Double.parseDouble(option_charges) + (Double.parseDouble(extension_charges)));
                                            }*/
                                            cartItemList.add(new CartItemsModel(got_cart_id, flag, venue_id, booking_date, booking_time_from, booking_time_to, number_of_guests, booking_rent,
                                                    venue_name, mVenueOrdCatType, "", id2, id3, option_desc, "1", option_charges, total_service_charges,"","","",null,null,null,venue_services_Obj,null, 1));
                                            Log.d("serviecs ", "done");

                                        }
                                    }
                                }

                                //===EXTRA_SERVICES ARRAY STARTS HERE====================================================================================================

                                JsonArray venue_extra_services_Obj = venue_price_Obj_array.get(a).getAsJsonObject().getAsJsonArray("booking_extra_services");

                                for (int j = 0; j < venue_extra_services_Obj.size(); j++) {

                                    String mVenueOrdCatType = "6";
                                    String id2 = !venue_extra_services_Obj.get(j).getAsJsonObject().get("extra_service_id").isJsonNull()
                                            ? venue_extra_services_Obj.get(j).getAsJsonObject().get("extra_service_id").getAsString() : null;
                                    String extra_service_name = !venue_extra_services_Obj.get(j).getAsJsonObject().get("extra_service_name").isJsonNull()
                                            ? venue_extra_services_Obj.get(j).getAsJsonObject().get("extra_service_name").getAsString() : null;

                                    JsonArray array_service_options = !venue_extra_services_Obj.get(j).getAsJsonObject().getAsJsonArray("service_options").isJsonNull()
                                            ? venue_extra_services_Obj.get(j).getAsJsonObject().getAsJsonArray("service_options") : null;

                                    double final_total_ext_service =0;
                                    if (!array_service_options.isJsonNull() && array_service_options.size() > 0) {
                                        //Log.d("service array===",""+array_service_options);
                                        for (int l = 0; l < array_service_options.size(); l++) {
                                            final_total_ext_service=final_total_ext_service
                                                    + Double.parseDouble( !array_service_options.get(l).getAsJsonObject().get("total_extra_service_charges").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("total_extra_service_charges").getAsString() : "0");
                                        }
                                    }
                                    cartItemList.add(new HeaderItemCartVenues(got_cart_id, flag, venue_id, booking_date, booking_time_from, booking_time_to, number_of_guests, booking_rent,
                                            venue_name, mVenueOrdCatType, "", id2, "", extra_service_name, "1", "", "","","",Double.toString(final_total_ext_service),null,null,null,null,null, 0));

                                    if (!array_service_options.isJsonNull() && array_service_options.size() > 0) {

                                        for (int l = 0; l < array_service_options.size(); l++) {
                                            String id3 = !array_service_options.get(l).getAsJsonObject().get("extra_service_option_id").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("extra_service_option_id").getAsString() : null;
                                            String option_desc = !array_service_options.get(l).getAsJsonObject().get("option_desc").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("option_desc").getAsString() : null;
                                            String option_charges = !array_service_options.get(l).getAsJsonObject().get("option_charges").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("option_charges").getAsString() : null;
                                            String option_details = !array_service_options.get(l).getAsJsonObject().get("option_details").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("option_details").getAsString() : null;

                                            String is_per_person_charges = !array_service_options.get(l).getAsJsonObject().get("is_per_person_charges").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("is_per_person_charges").getAsString() : null;
                                            String is_per_hour_charges = !array_service_options.get(l).getAsJsonObject().get("is_per_hour_charges").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("is_per_hour_charges").getAsString() : null;
                                            String is_hour_extn_changes = !array_service_options.get(l).getAsJsonObject().get("is_hour_extn_changes").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("is_hour_extn_changes").getAsString() : null;
                                            String extension_charges = !array_service_options.get(l).getAsJsonObject().get("extension_charges").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("extension_charges").getAsString() : null;

                                            String is_group_size = !array_service_options.get(l).getAsJsonObject().get("is_group_size").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("is_group_size").getAsString() : null;
                                            String group_size_from = !array_service_options.get(l).getAsJsonObject().get("group_size_from").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("group_size_from").getAsString() : null;
                                            String group_size_to = !array_service_options.get(l).getAsJsonObject().get("group_size_to").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("group_size_to").getAsString() : null;

                                            String total_service_charges = !array_service_options.get(l).getAsJsonObject().get("total_extra_service_charges").isJsonNull()
                                                    ? array_service_options.get(l).getAsJsonObject().get("total_extra_service_charges").getAsString() : null;

                                            /*if(is_per_person_charges.equalsIgnoreCase("1")){
                                                double extraguest=0;
                                                if(is_group_size.equalsIgnoreCase("1")){
                                                    if(Double.parseDouble(number_of_guests)>Double.parseDouble(group_size_to)){
                                                        extraguest = Double.parseDouble(number_of_guests) - Double.parseDouble(group_size_to);
                                                        total_service_charges = Double.toString(Double.parseDouble(option_charges) + (Double.parseDouble(option_charges) * extraguest));
                                                    }else{
                                                        total_service_charges = Double.toString(Double.parseDouble(option_charges) * Double.parseDouble(number_of_guests));
                                                    }
                                                }
                                            }else if(is_hour_extn_changes.equalsIgnoreCase("1")){
                                                total_service_charges = Double.toString(Double.parseDouble(option_charges) + (Double.parseDouble(extension_charges)));
                                            }*/

                                            cartItemList.add(new CartItemsModel(got_cart_id, flag, venue_id, booking_date, booking_time_from, booking_time_to, number_of_guests, booking_rent,
                                                    venue_name, mVenueOrdCatType, "", id2, id3, option_desc, "1", option_charges, total_service_charges,"","","",null,null,null,null,venue_extra_services_Obj, 1));

                                    /*got_cart_id,  flag,  venue_id,  booking_date,  booking_time_from,  booking_time_to,  number_of_guests,  booking_rent,
                                            venue_name,  mVenueOrdCatType,  id1,  id2,  id3,  name,  count,  item_price,  total_itemprice, int mUiType*/
                                            Log.d("extra serv", "done");
                                        }
                                    }
                                }
                            }

                            mAdapterCartItems.notifyDataSetChanged();
                        }
                    }
                    //======================
                    //======================
                    //======================
                    // ====THIS IS THE CODE FOR ONLY AND INLY WHEN CARTING ITEM CLICK FROM PREVIOUS SCREEN ONLY=====================
                    else if(cart_details_detailView.equalsIgnoreCase("2")){
                        String is_delivery ,is_pickup ,delivery_address;
                        catering_service_provider_details = rootObj.getAsJsonObject("message").getAsJsonArray("catering_service_provider_details");
                        cartItemList.clear();

                        if(catering_service_provider_details.size()<1 || catering_service_provider_details==null){
                            startActivity(new Intent(CartVenuesNCateringDetails.this, CartSummaryScreen.class));
                            finish();
                        }
                        for (int a = 0; a < catering_service_provider_details.size(); a++) {

                            got_cart_id = !catering_service_provider_details.get(a).getAsJsonObject().get("cart_id").isJsonNull()
                                    ? catering_service_provider_details.get(a).getAsJsonObject().get("cart_id").getAsString() : null;
                            user_id = !catering_service_provider_details.get(a).getAsJsonObject().get("user_id").isJsonNull()
                                    ? catering_service_provider_details.get(a).getAsJsonObject().get("user_id").getAsString() : null;
                            flag = !catering_service_provider_details.get(a).getAsJsonObject().get("flag").isJsonNull()
                                    ? catering_service_provider_details.get(a).getAsJsonObject().get("flag").getAsString() : null;
                            service_provider_id = !catering_service_provider_details.get(a).getAsJsonObject().get("service_provider_id").isJsonNull()
                                    ? catering_service_provider_details.get(a).getAsJsonObject().get("service_provider_id").getAsString() : null;
                            provider_name = !catering_service_provider_details.get(a).getAsJsonObject().get("provider_name").isJsonNull()
                                    ? catering_service_provider_details.get(a).getAsJsonObject().get("provider_name").getAsString() : null;
                            booking_date = !catering_service_provider_details.get(a).getAsJsonObject().get("booking_date").isJsonNull()
                                    ? catering_service_provider_details.get(a).getAsJsonObject().get("booking_date").getAsString() : null;
                            booking_time = !catering_service_provider_details.get(a).getAsJsonObject().get("booking_time").isJsonNull()
                                    ? catering_service_provider_details.get(a).getAsJsonObject().get("booking_time").getAsString() : null;
                            number_of_guests = !catering_service_provider_details.get(a).getAsJsonObject().get("number_of_guests").isJsonNull()
                                    ? catering_service_provider_details.get(a).getAsJsonObject().get("number_of_guests").getAsString() : null;
                            quantity = !catering_service_provider_details.get(a).getAsJsonObject().get("quantity").isJsonNull()
                                    ? catering_service_provider_details.get(a).getAsJsonObject().get("quantity").getAsString() : null;
                            total_amount= !catering_service_provider_details.get(a).getAsJsonObject().get("total_amount").isJsonNull()
                                    ? catering_service_provider_details.get(a).getAsJsonObject().get("total_amount").getAsString() : null;

                            is_delivery  = !catering_service_provider_details.get(a).getAsJsonObject().get("is_delivery").isJsonNull()
                                    ? catering_service_provider_details.get(a).getAsJsonObject().get("is_delivery").getAsString() : null;

                            is_pickup  = !catering_service_provider_details.get(a).getAsJsonObject().get("is_pickup").isJsonNull()
                                    ? catering_service_provider_details.get(a).getAsJsonObject().get("is_pickup").getAsString() : null;

                            delivery_address =  !catering_service_provider_details.get(a).getAsJsonObject().get("delivery_address").isJsonNull()
                                    ? catering_service_provider_details.get(a).getAsJsonObject().get("delivery_address").getAsString() : null;


                            if (got_cart_id.equalsIgnoreCase(cart_details_cart_id)) {

                                if(cart_details_detailView.equalsIgnoreCase("1")){
                                    getSupportActionBar().setTitle("");
                                }else if(cart_details_detailView.equalsIgnoreCase("2")){
                                    getSupportActionBar().setTitle(provider_name);
                                }

                                cartItemList.add(new HeaderItemCartVenues(got_cart_id, flag, service_provider_id, booking_date, booking_time,"", number_of_guests, total_amount,
                                        provider_name, "3", "", "", "", provider_name, "0", total_amount, total_amount,is_delivery,is_pickup,delivery_address,null,null,null,null,null, 0));
                                cartItemList.add(new CartItemsModel(got_cart_id, flag, service_provider_id, booking_date, booking_time,"", number_of_guests, total_amount,
                                        provider_name, "3", "", "", "", "Order Details", "0", total_amount, total_amount,is_delivery,is_pickup,delivery_address,null,null,null,null,null, 1));
                                Log.d("pricing_plan", "done");

                                //===SERVICE PROVIDER CATERING food MENU ARRAY STARTS HERE====================================================================================================

                                JsonArray catering_food_price = catering_service_provider_details.get(a).getAsJsonObject().getAsJsonArray("booking_food_menu");

                                if(catering_food_price.size()>0) {
                                    cartItemList.add(new HeaderItemCartVenues(got_cart_id, flag, service_provider_id, booking_date, booking_time, booking_time, number_of_guests, total_amount,
                                            provider_name, "3", "", "", "", "Catering Food Items", "", "", "", "", "", "", null, null, null, null, null, 0));

                                    cartItemList.add(new CartItemsModel(got_cart_id, flag, service_provider_id, booking_date, booking_time, booking_time, number_of_guests, total_amount,
                                            provider_name, "3", "", "", "", "", "", "", "", is_delivery, is_pickup, delivery_address, catering_food_price, null, null, null, null, 1));
                                }

                                //===SERVICE PROVIDER CATERING rasturant MENU ARRAY STARTS HERE====================================================================================================

                                JsonArray restaurant_food_price = catering_service_provider_details.get(a).getAsJsonObject().getAsJsonArray("booking_food");

                                for (int j = 0; j < restaurant_food_price.size(); j++) {

                                    String mVenueOrdCatType = "2";
                                    String id2 = !restaurant_food_price.get(j).getAsJsonObject().get("menu_id").isJsonNull()
                                            ? restaurant_food_price.get(j).getAsJsonObject().get("menu_id").getAsString() : null;
                                    String menu_desc = !restaurant_food_price.get(j).getAsJsonObject().get("menu_desc").isJsonNull()
                                            ? restaurant_food_price.get(j).getAsJsonObject().get("menu_desc").getAsString() : null;



                                    if (j == 0) {

                                        double final_food_total = 0;

                                        for (int j1 = 0; j1 < restaurant_food_price.size(); j1++) {
                                            JsonArray array_item_menu1 = !restaurant_food_price.get(j1).getAsJsonObject().getAsJsonArray("items").isJsonNull()
                                                    ? restaurant_food_price.get(j1).getAsJsonObject().getAsJsonArray("items") : null;
                                            if (!array_item_menu1.isJsonNull() && array_item_menu1.size() > 0) {

                                                for (int l = 0; l < array_item_menu1.size(); l++) {

                                                    String total_item_price = array_item_menu1.get(l).getAsJsonObject().get("total_item_price").getAsString();
                                                    final_food_total = final_food_total + Double.parseDouble(total_item_price);
                                                }
                                            }
                                        }

                                        cartItemList.add(new HeaderItemCartVenues(got_cart_id, flag, service_provider_id, booking_date, booking_time, booking_time, number_of_guests, "",
                                                provider_name, mVenueOrdCatType, "", id2, "", "Food Items", "", "", "", "", "", Double.toString(final_food_total), null, null, null, null, null, 0));
                                    }

                                    JsonArray array_item_menu = !restaurant_food_price.get(j).getAsJsonObject().getAsJsonArray("items").isJsonNull()
                                            ? restaurant_food_price.get(j).getAsJsonObject().getAsJsonArray("items") : null;

                                    if (!array_item_menu.isJsonNull() && array_item_menu.size() > 0) {

                                        for (int l = 0; l < array_item_menu.size(); l++) {

                                            String id3 = array_item_menu.get(l).getAsJsonObject().get("item_id").getAsString();
                                            String item_name = array_item_menu.get(l).getAsJsonObject().get("item_name").getAsString();
                                            String guest_count = array_item_menu.get(l).getAsJsonObject().get("guest_count").getAsString();
                                            String price_per_plate = array_item_menu.get(l).getAsJsonObject().get("price_per_plate").getAsString();
                                            String total_item_price = array_item_menu.get(l).getAsJsonObject().get("total_item_price").getAsString();

                                            cartItemList.add(new CartItemsModel(got_cart_id, flag, service_provider_id, booking_date, booking_time, booking_time, number_of_guests, total_item_price,
                                                    provider_name, mVenueOrdCatType, "", id2, id3, menu_desc + " > " + item_name, guest_count, price_per_plate, total_item_price,"","","",
                                                    null,restaurant_food_price,null,null,null, 1));
                                            Log.d("food menu", "done");
                                        }
                                    }

                                }

                                //===CATERING Beverages MENU ARRAY STARTS HERE====================================================================================================

                                JsonArray baverage_price = catering_service_provider_details.get(a).getAsJsonObject().getAsJsonArray("booking_beverages");

                                for (int j = 0; j < baverage_price.size(); j++) {


                                    String mVenueOrdCatType = "4";
                                    String id3 = !baverage_price.get(j).getAsJsonObject().get("beverage_id").isJsonNull()
                                            ? baverage_price.get(j).getAsJsonObject().get("beverage_id").getAsString() : null;
                                    String option_desc = !baverage_price.get(j).getAsJsonObject().get("option_desc").isJsonNull()
                                            ? baverage_price.get(j).getAsJsonObject().get("option_desc").getAsString() : null;
                                    String option_charges = !baverage_price.get(j).getAsJsonObject().get("option_charges").isJsonNull()
                                            ? baverage_price.get(j).getAsJsonObject().get("option_charges").getAsString() : null;
                                    String guest_count = !baverage_price.get(j).getAsJsonObject().get("guest_count").isJsonNull()
                                            ? baverage_price.get(j).getAsJsonObject().get("guest_count").getAsString() : null;
                                    String total_baverage_price = !baverage_price.get(j).getAsJsonObject().get("total_baverage_price").isJsonNull()
                                            ? baverage_price.get(j).getAsJsonObject().get("total_baverage_price").getAsString() : null;
                                    String is_hour_extn_changes = !baverage_price.get(j).getAsJsonObject().get("is_hour_extn_changes").isJsonNull()
                                            ? baverage_price.get(j).getAsJsonObject().get("is_hour_extn_changes").getAsString() : null;
                                    String extension_charges = !baverage_price.get(j).getAsJsonObject().get("extension_charges").isJsonNull()
                                            ? baverage_price.get(j).getAsJsonObject().get("extension_charges").getAsString() : null;

                                    if (j == 0) {
                                        double final_bevrages_total =0;
                                        for (int j1 = 0; j < baverage_price.size(); j1++) {
                                            final_bevrages_total = final_bevrages_total+ Double.parseDouble(!baverage_price.get(j).getAsJsonObject().get("option_charges").isJsonNull()
                                                    ? baverage_price.get(j).getAsJsonObject().get("option_charges").getAsString() : null);
                                        }

                                            cartItemList.add(new HeaderItemCartVenues(got_cart_id, flag, service_provider_id, booking_date, "", booking_time, number_of_guests, "",
                                                provider_name, "4", "", "", id3, "Beverages", "", "", "",
                                                is_delivery ,is_pickup, Double.toString(final_bevrages_total),null,null,null,null,null, 0));
                                    }

                                    cartItemList.add(new CartItemsModel(got_cart_id, flag, service_provider_id, booking_date, "", booking_time, number_of_guests, "",
                                            provider_name, "4", "", "", id3, option_desc, guest_count,option_charges , total_baverage_price,
                                            is_delivery ,is_pickup, delivery_address,null,null,baverage_price,null,null, 1));
                                    Log.d("catering menu", "done");


                                }

                            }

                            mAdapterCartItems.notifyDataSetChanged();
                        }
                    }
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
                progressDialog.dismiss();
            }
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CartVenuesNCateringDetails.this, CartSummaryScreen.class));
        finish();

    }
}
