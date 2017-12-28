package ovenues.com.ovenue.VenueCateringActivites;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.adapter.VenueDetailsPages.CateringFoodMenu.VenueOrderCateringFoodMenuSubCategoryAdapter;
import ovenues.com.ovenue.modelpojo.ServiceProviderChargesMenuModel.FoodMenuModel;
import ovenues.com.ovenue.utils.ConnectionDetector;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.food_menu;
import static ovenues.com.ovenue.utils.APICall.post;

public class VenueFoodmenuSubCategory extends AppCompatActivity {


    boolean loading_data=false;
    private int visibleThreshold = 5;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true,guest_added_view=false;

    RecyclerView mRecyclerView;
    ArrayList results;
    private RecyclerView.Adapter adapter;


    Boolean isInternetPresent = false;
    SwipeRefreshLayout mSwipeRefreshLayout;
    // View footerLayout;

    SharedPreferences sharepref;
    String str_menu_id,str_title,price_per_plate,menu_desc;
    public static String service_food_guest_count;
    TextView tv_menutitle,tv_addGuestCount;


    ScrollableNumberPicker np_itemcount;
    TextView tv_srNo,tv_title ,tv_course_totalcount,tv_price,tv_price_copy,tv_price_total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_foodmenu_sub_category);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Food Menu");




        str_menu_id= getIntent().getStringExtra("menu_id");
        Log.d("Got menuId=",str_menu_id);
        str_title= getIntent().getStringExtra("title");
        //service_food_guest_count= str_guest_count_catering_CourseMenu;

        tv_menutitle = (TextView)this.findViewById(R.id.tv_menutitle);
        tv_menutitle.setText(str_title);


        sharepref = getBaseContext().getSharedPreferences("MyPref", MODE_PRIVATE);

       /* // Fetching footer layout
        footerLayout = this.findViewById(R.id.footerView);
        footerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(this,VenuesSearchFilter.class));
                finish();
            }
        });*/

        // Initialize recycler this
        mRecyclerView = (RecyclerView)this.findViewById(R.id.rv_venuelist);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        results = new ArrayList<VenueFoodmenuSubCategory>();
        adapter = new VenueOrderCateringFoodMenuSubCategoryAdapter(results, this);

        mRecyclerView.setAdapter(adapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                results.clear();
                adapter.notifyDataSetChanged();

                new GetMenu().execute();

                mRecyclerView.setEnabled(false);
                mRecyclerView.setNestedScrollingEnabled(false);
                Toast.makeText(VenueFoodmenuSubCategory.this,"Loading...",Toast.LENGTH_LONG).show();


            }
        });


        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(this);
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests

            new GetMenu().execute();

        } else {
            Toast.makeText(this, "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();
        }

        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_srNo = (TextView) this.findViewById(R.id.tv_srNo);
        tv_course_totalcount = (TextView) this.findViewById(R.id.tv_course_totalcount);
        tv_price= (TextView) this.findViewById(R.id.tv_price);
        tv_price_copy= (TextView) this.findViewById(R.id.tv_price_copy);
        tv_price_total= (TextView) this.findViewById(R.id.tv_price_total);
        tv_price_total.setVisibility(View.INVISIBLE);
        tv_price_total.setVisibility(View.GONE);

        np_itemcount=(ScrollableNumberPicker)this.findViewById(R.id.np_itemcount);
        np_itemcount.setListener(new ScrollableNumberPickerListener() {
            @Override
            public void onNumberPicked(int value) {

                Log.e("got price---",price_per_plate);
                if(value>0){
                    double total_item = (double)value * Double.parseDouble(price_per_plate);
                    tv_price_total.setText("Total : $ "+total_item);
                    tv_price.setText("$ "+total_item);
                }else{
                    tv_price.setText("$ "+price_per_plate);
                }
            }
        });

        tv_addGuestCount= (TextView) this.findViewById(R.id.tv_addGuestCount);
        tv_addGuestCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(guest_added_view=false){
                    service_food_guest_count =Integer.toString(np_itemcount.getValue());
                    guest_added_view=true;
                    tv_addGuestCount.setText("Update");
                    Toast.makeText(VenueFoodmenuSubCategory.this,"Qty Set "+service_food_guest_count,Toast.LENGTH_LONG).show();
                }else{
                    service_food_guest_count =Integer.toString(np_itemcount.getValue());
                    guest_added_view=true;
                    tv_addGuestCount.setText("Update");
                    Toast.makeText(VenueFoodmenuSubCategory.this,"Qty Updated  "+service_food_guest_count,Toast.LENGTH_LONG).show();
                }
            }
        });


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (mRecyclerView == null || mRecyclerView.getChildCount() == 0) ? 0 : mRecyclerView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
//=========================================================================================================
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    //onHide();


                    //footerLayout.animate().translationY(footerLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();

                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    //onShow();
                    //footerLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

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


                        if(loading_data!=true) {
                            //new GetMenu().execute();
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

    protected ProgressDialog progressDialog;
    public class GetMenu extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(VenueFoodmenuSubCategory.this, "Loading", "Please Wait...", true, false);

        }

        @Override
        protected String doInBackground(Object... parametros) {


            return null;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            try{

                results.clear();
                JsonArray array_food_menu = food_menu;

                if(!array_food_menu.isJsonNull() && array_food_menu.size()>0){

                    for (int j = 0; j < array_food_menu.size(); j++) {

                        String menu_id = array_food_menu.get(j).getAsJsonObject().get("menu_id").getAsString();

                        Log.d("before if menuId=",str_menu_id);
                        if(menu_id.equalsIgnoreCase(str_menu_id)){

                            price_per_plate = array_food_menu.get(j).getAsJsonObject().get("price_per_plate").getAsString();
                            menu_desc = array_food_menu.get(j).getAsJsonObject().get("menu_desc").getAsString();
                            String total_courses = array_food_menu.get(j).getAsJsonObject().get("total_courses").getAsString();

                            String is_group_size=  !array_food_menu.get(j).getAsJsonObject().get("is_group_size").isJsonNull()
                                    ? array_food_menu.get(j).getAsJsonObject().get("is_group_size").getAsString() : null;
                            String group_size_from =!array_food_menu.get(j).getAsJsonObject().get("group_size_from").isJsonNull()
                                    ? array_food_menu.get(j).getAsJsonObject().get("group_size_from").getAsString(): null;
                            String group_size_to = !array_food_menu.get(j).getAsJsonObject().get("group_size_to").isJsonNull()
                                    ? array_food_menu.get(j).getAsJsonObject().get("group_size_to").getAsString(): null;
                            //  Log.d("is groupsize",""+is_group_size);

                            JsonArray array_courses = !array_food_menu.get(j).getAsJsonObject().get("courses").getAsJsonArray().isJsonNull()
                                    ? array_food_menu.get(j).getAsJsonObject().get("courses").getAsJsonArray():null;
                            for (int k = 0; k < array_courses.size(); k++) {
                                String course_id = array_courses.get(k).getAsJsonObject().get("course_id").getAsString();
                                String course_title = array_courses.get(k).getAsJsonObject().get("course_title").getAsString();
                                String max_item_selectn_allowed = array_courses.get(k).getAsJsonObject().get("max_item_selectn_allowed").getAsString();

                                results.add(new FoodMenuModel(menu_id,course_id,course_title,max_item_selectn_allowed ,"","","",""));
                            }
                            tv_title.setText(menu_desc);
                            tv_srNo.setText("1");
                            tv_course_totalcount.setText("Total Course :"+total_courses);
                            tv_price.setText(" $ "+price_per_plate);
                            tv_price_copy.setText(" $ "+price_per_plate);

                            Log.d("is groupsize",""+is_group_size);
                            if(is_group_size!=null && is_group_size.equalsIgnoreCase("1")){
                                np_itemcount.setMaxValue(Integer.parseInt(group_size_to));
                                np_itemcount.setMinValue(Integer.parseInt(group_size_from));
                                np_itemcount.setValue(Integer.parseInt(group_size_from));

                                tv_course_totalcount.setText(tv_course_totalcount.getText().toString()+"\n QTY ( "+group_size_from+
                                        " - "+group_size_to+" )");
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();


                }else{
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), "No Data Found.", Snackbar.LENGTH_LONG)
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


                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(getBaseContext());
                    alertbox.setMessage("No Data Found.");
                    alertbox.setTitle("Sorry ! ");
                    alertbox.setIcon(R.mipmap.ic_launcher);

                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0,int arg1) {

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
    }



   /* String res;
    protected ProgressDialog progressDialog;
    class GetVenueDetails extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(VenueFoodmenuSubCategory.this, "Loading", "Please Wait...", true, false);

        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {
                *//*String upend = "city="+sharepref.getString(Const.PREF_CITY_ID,"")+"&event_type="+event_type_id+"&venue_type="+venue_type_id
                        +"&amenities="+amenty_id+"&guest_count_min="+guest_count_min+"&guest_count_max="+guest_count_max
                        +"&price_min="+price_min+"&price_max&"+price_max+"&from="+str_from+"&to="+str_to+"&sort_by="+sort_by+"&user_id="+sharepref.getString(Const.PREF_USER_ID,"");*//*
                String response = post(Const.SERVER_URL_API+"mob_venue_detail_right/"+venue_id, "","get");
                //Log.d("URL ====",Const.SERVER_URL_API+"mob_venue_detail_right/"+str_venue_id);
                res=response;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try{

                // Log.i("RESPONSE", res);
                JSONObject obj = new JSONObject(res);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")) {

                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(res).getAsJsonObject();

                    // String venue_name= rootObj.getAsJsonObject("message").get("venue_name").getAsString();
                    String address1= rootObj.getAsJsonObject("message").get("address1").getAsString();
                    String address2= rootObj.getAsJsonObject("message").get("address2").getAsString();
                    String statae= rootObj.getAsJsonObject("message").get("statae").getAsString();
                    String zipcode= rootObj.getAsJsonObject("message").get("zipcode").getAsString();

*//*

                    //tv_venue_name.setText(venue_name);
                    tv_venue_address.setText(address1+"\n"+address2+" , "+statae+" - "+zipcode);
*//*

                    //String venue_type_name = rootObj.getAsJsonObject("message").get("venue_type_name").getAsString();
                    String max_occupancy = rootObj.getAsJsonObject("message").get("max_occupancy").getAsString();
                    String min_occupancy = rootObj.getAsJsonObject("message").get("min_occupancy").getAsString();


                    //==this is codee for horizontal service and
                    String str_cost_type="",str_average_cost="";
                    str_average_cost= rootObj.getAsJsonObject("message").get("average_cost").getAsString();
                    String cost_type= rootObj.getAsJsonObject("message").get("cost_type").getAsString();
                    if(cost_type.equalsIgnoreCase("1")){
                        str_cost_type ="*per person";
                    }else if(cost_type.equalsIgnoreCase("2")){
                        str_cost_type ="*per hour";
                    }else if(cost_type.equalsIgnoreCase("3")){
                        str_cost_type ="*starting price";
                    }else{
                        str_cost_type = "Request Quote !";
                    }

                    JsonArray venue_servicesObj = rootObj.getAsJsonObject("message")
                            .getAsJsonArray("venue_services");
                    if (venue_servicesObj!=null && venue_servicesObj.size() > 0){

                    }else{
                    }


                    String t_and_c = !rootObj.getAsJsonObject("message").get("t_and_c").isJsonNull()
                            ? rootObj.getAsJsonObject("message").get("t_and_c").getAsString():null;



                    JsonArray array_food_menu = !rootObj.getAsJsonObject("message").getAsJsonArray("food_menu_options").isJsonNull()
                            ? rootObj.getAsJsonObject("message").getAsJsonArray("food_menu_options"):null;

                    if(!array_food_menu.isJsonNull() && array_food_menu.size()>0){

                        for (int j = 0; j < array_food_menu.size(); j++) {

                            String menu_id = array_food_menu.get(j).getAsJsonObject().get("menu_id").getAsString();

                            if(menu_id.equalsIgnoreCase(str_menu_id)){
                                JsonArray array_courses = !array_food_menu.get(j).getAsJsonObject().get("courses").getAsJsonArray().isJsonNull()
                                        ? array_food_menu.get(j).getAsJsonObject().get("courses").getAsJsonArray():null;
                                for (int k = 0; k < array_courses.size(); k++) {
                                    String course_id = array_courses.get(k).getAsJsonObject().get("course_id").getAsString();
                                    String course_title = array_courses.get(k).getAsJsonObject().get("course_title").getAsString();
                                    String max_item_selectn_allowed = array_courses.get(k).getAsJsonObject().get("max_item_selectn_allowed").getAsString();

                                    results.add(new FoodMenuModel(course_id,course_title,max_item_selectn_allowed ,"","","",""));
                                }
                                break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
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


                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(VenueFoodmenuSubCategory.this);
                    alertbox.setMessage(message);
                    alertbox.setTitle("Sorry ! ");
                    alertbox.setIcon(R.mipmap.ic_launcher);

                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0,int arg1) {

                                }
                            });
                    alertbox.show();
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }*/

}
