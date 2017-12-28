package ovenues.com.ovenue;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ovenues.com.ovenue.adapter.ServicesListAdapter;
import ovenues.com.ovenue.adapter.autocomplete_textviews.SearchCityAdapter;
import ovenues.com.ovenue.adapter.autocomplete_textviews.VenueNameSearchAutocompleteAdapter;
import ovenues.com.ovenue.adapter.horizontal_recycler_servicelist.ServiceListHoriRecyclerDataAdapter;
import ovenues.com.ovenue.cart.CartSummaryScreen;
import ovenues.com.ovenue.modelpojo.HomeHoriRecyclerSingleItem;
import ovenues.com.ovenue.modelpojo.SectionDataModel;
import ovenues.com.ovenue.modelpojo.ServicesListModel;
import ovenues.com.ovenue.modelpojo.autocompleteSearchviewCity.SearchVenueSPCityModel;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.utils.APICall.post;

public class ServicesList extends AppCompatActivity {

    static Activity activity;
    private static final int MENU_PRICE_LOW_TO_HIGH = Menu.FIRST;
    private static final int MENU_PRICE_HIGH_TO_LOW= Menu.FIRST + 1;
    private static final int MENU_POPLAR = Menu.FIRST + 2;


    static SharedPreferences sharepref;
    public static TextView tv_milesAway,tv_serviceTitle;
    String milesAway_count_min,milesAway_count_max;
    //===CUSTOME HORIZONTAL RECYCLE VIEW WITH SECTION=========
    static ArrayList<SectionDataModel> allSampleData;

    //===SECTION 1 FOR SERVICE=============
    static SectionDataModel dm = new SectionDataModel();
    static ArrayList<HomeHoriRecyclerSingleItem> singleItem ;
    static RecyclerView my_recycler_view;
    public static ServiceListHoriRecyclerDataAdapter adapterServiceTypesList;

    //===================================================================
    public static boolean loading_data=false;
    private int visibleThreshold = 5;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    static SwipeRefreshLayout mSwipeRefreshLayout;
    View footerLayout;

    public static ArrayList results;
    public static RecyclerView.Adapter adapter_servicesList;


    Boolean isInternetPresent = false;
    //public static String str_search_radius ="10", sort_by ="";
    public static String str_service_id ="";
    public static int int_from=0;
    public static int int_to=10;

    static RecyclerView mRecyclerViewServiceList;
   // public static int index_selectedServiceType;
    AutoCompleteTextView et_city;
    SearchCityAdapter adapter_city ;


    VenueNameSearchAutocompleteAdapter adapter_venue_name_search ;
    ArrayList<SearchVenueSPCityModel> searchcitymodel;

    TextView tv_sort,tv_filter;
    String str_venue_name=null;
    public AutoCompleteTextView gen_search;
    AlertDialog alertDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_list);


        activity=this;
        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        final Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("");


        sharepref.edit().putString(Const.PREF_STR_SERVICE_ID,"");
        sharepref.edit().putString(Const.PREF_STR_SERVICE_NAME,"");

        tv_milesAway=(TextView)this.findViewById(R.id.tv_milesAway);
        tv_serviceTitle=(TextView)this.findViewById(R.id.tv_serviceTitle);


        //====CREARTE CUSTOME HORIZONTAL RECYCLERVIEW =======
        allSampleData = new ArrayList<SectionDataModel>();

        singleItem = new ArrayList<HomeHoriRecyclerSingleItem>();



        gen_search=(AutoCompleteTextView)this.findViewById(R.id.gen_search);
        gen_search.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);
        gen_search.setText("");

        searchcitymodel = new ArrayList<SearchVenueSPCityModel>();

        gen_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>2) {
                    str_venue_name = s.toString();
                    new GetVendorNamesSearch().execute();
                }

            }
        });

        gen_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                sharepref.edit().putString(Const.PREF_STR_SERVICE_ID,adapter_venue_name_search.getItem(position).getServiceID()).apply();
                sharepref.edit().putString(Const.PREF_STR_SERVICE_NAME, adapter_venue_name_search.getItem(position).getServiceName()).apply();

                startActivity(new Intent(ServicesList.this,ServiceProvidersDetailsMainFragment.class)
                        .putExtra("service_provider_id",adapter_venue_name_search.getItem(position).getId()));
                gen_search.setText("");
                gen_search.clearFocus();
                /*Toast.makeText(MainNavigationScreen.this, adapter_venue_name_search.getItem(position).getId().toString(),Toast.LENGTH_SHORT).show();*/

            }
        });

        new GetServicesTypesDefault().execute();
        //index_selectedServiceType=15;
        str_service_id=sharepref.getString(Const.PREF_STR_SERVICE_ID,"").toString();
        Log.e("got service ID",str_service_id);
        new GetServicesListDefault().execute();
        tv_serviceTitle.setText(sharepref.getString(Const.PREF_STR_SERVICE_NAME,"").toString());


        et_city=(AutoCompleteTextView)this.findViewById(R.id.et_city);
        //et_city.setText(sharepref.getString(Const.PREF_CITY_NAME_SP,""));

        et_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty()||
                        s.toString()==null||
                        et_city.getText().toString().length()<0){
                    sharepref.edit().putString(Const.PREF_CITY_ID_SP,"").apply();
                    sharepref.edit().putString(Const.PREF_CITY_NAME_SP,"").apply();
                    new GetServicesListDefault().execute();
                }else if(!s.toString().isEmpty()||
                        s.toString()!=null||
                        et_city.getText().toString().length()>0){
                    //Log.d("edited","yes");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
               /* Toast.makeText(ServicesList.this,
                        adapter_city.getItem(position).getId().toString(),
                        Toast.LENGTH_SHORT).show();*/

                sharepref.edit().putString(Const.PREF_CITY_ID_SP,adapter_city.getItem(position).getId()).apply();
                sharepref.edit().putString(Const.PREF_CITY_NAME_SP,adapter_city.getItem(position).getName()+","+adapter_city.getItem(position).getCounty()).apply();
                new GetServicesListDefault().execute();
            }
        });



        // MILES COUNT====get seekbar from view
        final RangeBar rangeBar_MilesAway = (RangeBar) this.findViewById(R.id.rangeBar_Milesaway);
        rangeBar_MilesAway.setSeekPinByIndex(0);
        // / get min and max text view
        tv_milesAway = (TextView) this.findViewById(R.id.tv_milesAway);

        rangeBar_MilesAway.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {

                  if(rightPinIndex==0){
                    tv_milesAway.setText("Nearby (No Limit)");
                }else{
                    tv_milesAway.setText("Nearby "+" - "+rightPinValue+" miles");
                }
                milesAway_count_max = rightPinValue;
               // str_search_radius =milesAway_count_max;
                new GetServicesListDefault().execute();
                //Log.e("values ===",String.valueOf(tickIndex));

                //Toast.makeText(ServicesList.this,"values =="+rightPinValue,Toast.LENGTH_LONG).show();
                }
        });



        my_recycler_view = (RecyclerView)this.findViewById(R.id.my_recycler_view);
        my_recycler_view.setHasFixedSize(true);
        adapterServiceTypesList = new ServiceListHoriRecyclerDataAdapter(ServicesList.this, allSampleData);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(ServicesList.this, LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(adapterServiceTypesList);
        my_recycler_view.setNestedScrollingEnabled(false);



        //================================================================
        // Initialize recycler view

        final String[] option = new String[] { "Price Low To High ", "Price High To Low" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Option");
        builder.setSingleChoiceItems(option, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:


                        sharepref.edit().putString(Const.PREF_SORT_BY_SP,"1").apply();
                        results.clear();
                        adapter_servicesList.notifyDataSetChanged();
                        int_from=0;
                        int_to=10;
                        new GetServicesListDefault().execute();
                        dialog.dismiss();

                        break;
                    case 1:

                        sharepref.edit().putString(Const.PREF_SORT_BY_SP,"2").apply();
                        results.clear();
                        adapter_servicesList.notifyDataSetChanged();
                        int_from=0;
                        int_to=10;
                        new GetServicesListDefault().execute();
                        dialog.dismiss();
                        break;
                    case 2:

                        //Toast.makeText(MainActivity.this, "Third Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();

        footerLayout = this.findViewById(R.id.footerView);
        tv_sort = (TextView)this.findViewById(R.id.tv_sort);
        tv_filter = (TextView)this.findViewById(R.id.tv_filter);

        tv_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openOptionsMenu();
                //myToolbar.showOverflowMenu();
                alertDialog1.show();


            }
        });

        tv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ServicesList.this,ServiceProviderSearchFilters.class)/*
                        .putExtra("index_selectedServiceType",Integer.toString(index_selectedServiceType+1))*/);
               // finish();
            }
        });

        results = new ArrayList<ServicesList>();
        adapter_servicesList = new ServicesListAdapter(results, ServicesList.this);

        mRecyclerViewServiceList = (RecyclerView)this.findViewById(R.id.rv_serviceList);
        final LinearLayoutManager mLayoutManagerList;
        mLayoutManagerList = new LinearLayoutManager(ServicesList.this);
        mRecyclerViewServiceList.setLayoutManager(mLayoutManagerList);
        mRecyclerViewServiceList.setAdapter(adapter_servicesList);
        mRecyclerViewServiceList.setHasFixedSize(true);
        mRecyclerViewServiceList.setNestedScrollingEnabled(false);


        mSwipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                results.clear();
                adapter_servicesList.notifyDataSetChanged();

                int_from=0;
                int_to=10;

                new GetServicesListDefault().execute();

                mRecyclerViewServiceList.setEnabled(false);
                mRecyclerViewServiceList.setNestedScrollingEnabled(false);
                Toast.makeText(ServicesList.this,"Loading...",Toast.LENGTH_LONG).show();


            }
        });


        mRecyclerViewServiceList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView mRecyclerViewServiceList, int dx, int dy) {

               // Log.d("scroll effect","effect scroll");
                int topRowVerticalPosition =
                        (mRecyclerViewServiceList == null || mRecyclerViewServiceList.getChildCount() == 0) ? 0 : mRecyclerViewServiceList.getChildAt(0).getTop();

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
                    visibleItemCount = mLayoutManagerList.getChildCount();
                    totalItemCount = mLayoutManagerList.getItemCount();
                    pastVisiblesItems = mLayoutManagerList.findFirstVisibleItemPosition();

                    /*if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            textView_loadmore.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                            textView_loadmore.setVisibility(View.VISIBLE);
                        }
                    }*/

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && (totalItemCount - visibleItemCount) <= (pastVisiblesItems + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        //Log.e("scroll ended","yes");

                        if(loading_data!=true) {
                            if(int_from==0){
                                int_from=int_from+11;
                                int_to=int_to+10;
                            }else{
                                int_from=int_from+10;
                                int_to=int_to+10;
                            }
                            new GetServicesListDefault().execute();
                        }
                    }
                }

                //=======================================================================================================
            }

            @Override
            public void onScrollStateChanged(RecyclerView mRecyclerViewServiceList, int newState) {
                super.onScrollStateChanged(mRecyclerViewServiceList, newState);
            }
        });


    }

    static String res;
    static ProgressDialog progressDialogSErviceList;
   public static class GetServicesListDefault extends AsyncTask<Integer, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialogSErviceList = ProgressDialog.show(activity, "Loading", "Please Wait...", true, false);
            loading_data=true;
        }

        @Override
        protected String doInBackground(Integer... parametros) {
           //index_selectedServiceType =  parametros[0];


            try {

                if(str_service_id==null || str_service_id.equalsIgnoreCase("")){
                    str_service_id="1";
                }

                String str_from=Integer.toString(int_from);
                String str_to=Integer.toString(int_to);
                //Log.d(str_service_id,str_service_id);
                String upend = "search_radius="+sharepref.getString(Const.PREF_NEARBY_SP,"10")+"&cityId="+sharepref.getString(Const.PREF_CITY_ID_SP,"" )
                        +"&service_id="+str_service_id+"&sort_by="+sharepref.getString(Const.PREF_SORT_BY_SP,"")
                        +"&price_range="+sharepref.getString(Const.PREF_PRICE_PRICERANGE_SP,"")
                        +"&from="+str_from
                        +"&to="+str_to
                        +"&user_id="
                        +sharepref.getString(Const.PREF_USER_ID,"");
                String response = post(Const.SERVER_URL_API +"/filter_service_providers?"+upend, "","get");
                Log.d("URL servicelist====",Const.SERVER_URL_API +"filter_service_providers?"+upend);
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
                JSONObject obj = new JSONObject(res);
               // Log.i("RES service pv LIST", res);
                response_string=obj.getString("status");

                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(res).getAsJsonObject();
                if(response_string.equals("success")){


                    JsonArray service_providersObj = rootObj.getAsJsonObject("message")
                            .getAsJsonArray("service_providers");

                    String status = rootObj.get("status").getAsString();
                    String total = rootObj.getAsJsonObject("message").get("total").getAsString();
                    //String lng = locObj.get("lng").getAsString();

                    String message=obj.getString("message");
                    if(int_from==0 && results.size()>0) {
                        results.clear();
                        adapter_servicesList.notifyDataSetChanged();
                    }
                    if(service_providersObj.size()>0) {

                        if(int_from==0 && service_providersObj.size()>0) {
                            mRecyclerViewServiceList.smoothScrollToPosition(0);
                        }

                        for (int j = 0; j < service_providersObj.size(); j++) {

                            String id = service_providersObj.get(j).getAsJsonObject().get("id").getAsString();
                            //Log.e("id---",id);
                            String provider_name = service_providersObj.get(j).getAsJsonObject().get("provider_name").getAsString();
                            String city_name = service_providersObj.get(j).getAsJsonObject().get("city_name").getAsString();
                            String address = service_providersObj.get(j).getAsJsonObject().get("address").getAsString();
                            String photo_url = service_providersObj.get(j).getAsJsonObject().get("photo_url").getAsString();

                            String is_registered = service_providersObj.get(j).getAsJsonObject().get("is_registered").getAsString();

                            String yelp_rating = service_providersObj.get(j).getAsJsonObject().get("yelp_rating").getAsString();

                            String yelp_review_count = service_providersObj.get(j).getAsJsonObject().get("yelp_review_count").getAsString();

                            String yelp_price_range = service_providersObj.get(j).getAsJsonObject().get("yelp_price_range").getAsString();

                            String yelp_id = service_providersObj.get(j).getAsJsonObject().get("yelp_id").getAsString();
                            String cuisines = service_providersObj.get(j).getAsJsonObject().get("cuisines").getAsString();


                            //Log.d("for start",service_name);
                         /*
                        String time_extention_allowed  =!venuesObj.get(j).getAsJsonObject().get("time_extention_allowed").isJsonNull()
                                ? venuesObj.get(j).getAsJsonObject().get("time_extention_allowed").getAsString():null;
                        JsonArray serviceArray = venuesObj.get(j).getAsJsonObject().get("services").getAsJsonArray();
                        //String services  =venuesObj.get(j).getAsJsonObject().get("services").getAsString();
                        //Log.e("servicearray",serviceArray.toString());

                        String fav_count  =venuesObj.get(j).getAsJsonObject().get("fav_count").getAsString();
                        String is_favourite =venuesObj.get(j).getAsJsonObject().get("is_favourite").getAsString();*/
                            results.add(new ServicesListModel(id, provider_name, address,city_name, photo_url,is_registered ,yelp_rating ,yelp_review_count ,yelp_price_range ,yelp_id,cuisines));

                        }
                        adapter_servicesList.notifyDataSetChanged();
                    }else{
                        adapter_servicesList.notifyDataSetChanged();
                        tv_serviceTitle.setText(sharepref.getString(Const.PREF_STR_SERVICE_NAME,"").toString());
                        loading_data=false;
                        //progressDialog.dismiss();

                        final AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
                        alertbox.setMessage(message);
                        alertbox.setTitle("Sorry ! ");
                        alertbox.setIcon(R.mipmap.ic_launcher);

                        alertbox.setNeutralButton("OK",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0,int arg1) {
                                   /* startActivity(new Intent(VenuesList.this,MainNavigationScreen.class));
                                    sharepref.edit().putString(Const.PREF_CITY_ID_SP,"").apply();
                                    sharepref.edit().putString(Const.PREF_EVENT_TYPE_ID,"").apply();
                                    sharepref.edit().putString(Const.PREF_VENUE_TYPE_ID,"").apply();
                                    sharepref.edit().putString(Const.PREF_AMENITIES_ID,"").apply();
                                    sharepref.edit().putString(Const.PREF_GUEST_COUNT_MIN,"").apply();
                                    sharepref.edit().putString(Const.PREF_GUEST_COUNT_MAX,"").apply();
                                    sharepref.edit().putString(Const.PREF_PRICE_MIN,"").apply();
                                    sharepref.edit().putString(Const.PREF_PRICE_MAX,"").apply();
                                    finish();*/
                                    }
                                });
                        alertbox.show();
                    }


                }else{

                    String message=rootObj.has("message") && !rootObj.get("message").getAsString().isEmpty()
                            ?rootObj.get("message").getAsString():null;

                    //results.clear();
                    //adapter_servicesList.notifyDataSetChanged();

                    if(message==null){

                    }else {
                        final AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
                        alertbox.setMessage(message + "for " + sharepref.getString(Const.PREF_STR_SERVICE_NAME, "") + ".");
                        alertbox.setTitle("Sorry ! ");
                        alertbox.setIcon(R.mipmap.ic_launcher);

                        alertbox.setNeutralButton("OK",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0, int arg1) {
                                   /* startActivity(new Intent(ServicesList.this,MainNavigationScreen.class));
                                   *//* sharepref.edit().putString(Const.PREF_CITY_ID_SP,"").apply();
                                    sharepref.edit().putString(Const.PREF_EVENT_TYPE_ID,"").apply();
                                    sharepref.edit().putString(Const.PREF_VENUE_TYPE_ID,"").apply();
                                    sharepref.edit().putString(Const.PREF_AMENITIES_ID,"").apply();
                                    sharepref.edit().putString(Const.PREF_GUEST_COUNT_MIN,"").apply();
                                    sharepref.edit().putString(Const.PREF_GUEST_COUNT_MAX,"").apply();
                                    sharepref.edit().putString(Const.PREF_PRICE_MIN,"").apply();
                                    sharepref.edit().putString(Const.PREF_PRICE_MAX,"").apply();*//*
                                    finish();*/
                                    }
                                });
                        alertbox.show();
                    }
                }
                progressDialogSErviceList.dismiss();
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                progressDialogSErviceList.dismiss();
            }

            tv_serviceTitle.setText(sharepref.getString(Const.PREF_STR_SERVICE_NAME,"").toString());

            if(mSwipeRefreshLayout.isRefreshing()){
                mSwipeRefreshLayout.setRefreshing(false);
            }
            loading_data=false;
            progressDialogSErviceList.dismiss();
        }
    }


    String res3;
    //protected ProgressDialog progressDialog3;
    private class GetVendorNamesSearch extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            //progressDialog3 = ProgressDialog.show(MainNavigationScreen.this, "Loading", "Please Wait...", true, false);
        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {
                String response = post(Const.SERVER_URL_API +"search_by_name/?flags=2,3&keyword="+str_venue_name, "","get");
                //Log.d("URL ====",Const.SERVER_URL_API+"search_by_name/?flags=2&keyword="+str_venue_name);
                res3=response;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res3;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try{
                JSONObject obj = new JSONObject(res3);
                response_string=obj.getString("status");
                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(res3).getAsJsonObject();


                if(response_string.equals("success")){

                    searchcitymodel.clear();
                    JsonArray venueNameObj = rootObj.getAsJsonArray("message");

                    adapter_venue_name_search = new VenueNameSearchAutocompleteAdapter(ServicesList.this, R.layout.activity_venues_list, R.id.row_text_view_only, searchcitymodel);
                    gen_search.setThreshold(1);
                    gen_search.setAdapter(adapter_venue_name_search);
                  /*  DividerItemDecoration decorItem = new DividerItemDecoration( gen_search.getContext(),DividerItemDecoration.VERTICAL);
                    gen_search.addItemDecoration(decorItem);*/

                    for (int j = 0; j < venueNameObj.size(); j++) {

//                        String city_id = cityObj.get(j).getAsJsonObject().get("city_id").getAsString();
//                        String city_name  =cityObj.get(j).getAsJsonObject().get("city_name").getAsString();
                        String id  =venueNameObj.get(j).getAsJsonObject().get("id").getAsString();

                        String name = venueNameObj.get(j).getAsJsonObject().get("name").getAsString();
                        //                       String display_name  =cityObj.get(j).getAsJsonObject().get("display_name").getAsString();

                        String group  =venueNameObj.get(j).getAsJsonObject().get("group").getAsString();
                        if(group.equalsIgnoreCase("3")) {
                            String service_id = venueNameObj.get(j).getAsJsonObject().get("service_id").getAsString();
                            String service_name = venueNameObj.get(j).getAsJsonObject().get("service_name").getAsString();
                            String svg_icon_url =  venueNameObj.get(j).getAsJsonObject().get("svg_icon_url").getAsString();
                            searchcitymodel.add(new SearchVenueSPCityModel(id, name, name, group,service_id,service_name,svg_icon_url));
                        }else{
                            searchcitymodel.add(new SearchVenueSPCityModel(id,name,name,group,"0","0","0"));

                        }

                        /*if(group.equalsIgnoreCase("2")){
                            String is_registered  =venueNameObj.get(j).getAsJsonObject().get("is_registered").getAsString();
                            if(is_registered.equalsIgnoreCase("1")){
                                searchcitymodel.add(new SearchVenueSPCityModel(id,name,name));
                                // Log.i("RESPONSE venu serch", ""+id+name+name);
                            }
                        }*/
                    }
                    adapter_venue_name_search.notifyDataSetChanged();
                    adapter_venue_name_search.setNotifyOnChange(true);
                    gen_search.showDropDown();
                    //progressDialog3.dismiss();
                }else{
                    //progressDialog3.dismiss();
                    String message=rootObj.getAsJsonObject().get("message").getAsString();
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


                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(ServicesList.this);
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
                // progressDialog3.dismiss();
            }
            // progressDialog3.dismiss();
        }
    }


    static String resServices;
    public static class GetServicesTypesDefault extends AsyncTask<Integer, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... parametros) {

            try {

                if(str_service_id==null || str_service_id.equalsIgnoreCase("")){
                    str_service_id="1";
                }

                String response = post(Const.SERVER_URL_API +"/services", "","get");

                resServices=response;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resServices;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);
            try{
                JSONObject obj = new JSONObject(resServices);
                // Log.i("RES service pv LIST", res);
                response_string=obj.getString("status");
                if(response_string.equals("success")){

                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(resServices).getAsJsonObject();

                    allSampleData.clear();
                    singleItem.clear();
                    adapterServiceTypesList.notifyDataSetChanged();
                    JsonArray servicesObj = rootObj.getAsJsonArray("message");

                    dm.setHeaderTitle("");
                    for (int j = 0; j < servicesObj.size(); j++) {

                        String service_id = servicesObj.get(j).getAsJsonObject().get("id").getAsString();
                        String service_name  =servicesObj.get(j).getAsJsonObject().get("service_name").getAsString();
                        String circle_svg_icon_url =  servicesObj.get(j).getAsJsonObject().get("app_icon_svg_url").getAsString();
                        String outline_svg_icon_url  =servicesObj.get(j).getAsJsonObject().get("outline_svg_icon_url").getAsString();
                        /*String filled_icon_url = servicesObj.get(j).getAsJsonObject().get("filled_icon_url").getAsString();
                        String thubnail_icon_url  =servicesObj.get(j).getAsJsonObject().get("thubnail_icon_url").getAsString();*/
                        //Log.d("for start",service_name);
                        boolean is_selected=false;
                        if(service_name.equalsIgnoreCase(sharepref.getString(Const.PREF_STR_SERVICE_NAME,"").toString())){
                            is_selected=true;
                        }
                        singleItem.add(new HomeHoriRecyclerSingleItem(service_name, circle_svg_icon_url,Const.SERVER_URL_ONLY+outline_svg_icon_url,service_id,Const.CONST_SERVICE,is_selected));
                        // System.out.print("model class"+message_details.length()+service_name);
                    }
                    dm.setAllItemsInSection(singleItem);
                    allSampleData.add(dm);
                    adapterServiceTypesList.notifyDataSetChanged();




                }else{
                    String message=obj.getString("message");

                    results.clear();
                    adapter_servicesList.notifyDataSetChanged();

                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
                    alertbox.setMessage(message+"for "+sharepref.getString(Const.PREF_STR_SERVICE_NAME,"")+".");
                    alertbox.setTitle("Sorry ! ");
                    alertbox.setIcon(R.mipmap.ic_launcher);

                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0,int arg1) {
                                   /* startActivity(new Intent(ServicesList.this,MainNavigationScreen.class));
                                   *//* sharepref.edit().putString(Const.PREF_CITY_ID_SP,"").apply();
                                    sharepref.edit().putString(Const.PREF_EVENT_TYPE_ID,"").apply();
                                    sharepref.edit().putString(Const.PREF_VENUE_TYPE_ID,"").apply();
                                    sharepref.edit().putString(Const.PREF_AMENITIES_ID,"").apply();
                                    sharepref.edit().putString(Const.PREF_GUEST_COUNT_MIN,"").apply();
                                    sharepref.edit().putString(Const.PREF_GUEST_COUNT_MAX,"").apply();
                                    sharepref.edit().putString(Const.PREF_PRICE_MIN,"").apply();
                                    sharepref.edit().putString(Const.PREF_PRICE_MAX,"").apply();*//*
                                    finish();*/
                                }
                            });
                    alertbox.show();
                }
                progressDialogSErviceList.dismiss();
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                progressDialogSErviceList.dismiss();
            }

            tv_serviceTitle.setText(sharepref.getString(Const.PREF_STR_SERVICE_NAME,"").toString());

            if(mSwipeRefreshLayout.isRefreshing()){
                mSwipeRefreshLayout.setRefreshing(false);
            }
            loading_data=false;
            progressDialogSErviceList.dismiss();
        }
    }


    ImageView ic_cart_actionbar;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.cart_option, menu);
        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.cart_badge_layout);
        RelativeLayout notifCount = (RelativeLayout)   MenuItemCompat.getActionView(item);

        ic_cart_actionbar = (ImageView) notifCount.findViewById(R.id.ic_cart_actionbar);
        TextView tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
        tv.setText(sharepref.getString(Const.PREF_USER_CART_TOTAL_ITEMS,"0"));


        ic_cart_actionbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ServicesList.this,CartSummaryScreen.class));

            }
        });

       // menu.add(0, MENU_PRICE_LOW_TO_HIGH, Menu.NONE, "Price Low To High ");
        //menu.add(0, MENU_PRICE_HIGH_TO_LOW, Menu.NONE, "Price High To Low");
        //menu.add(0, MENU_POPLAR, Menu.NONE, "Popular Venues");



        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                finish();
                return true;
            case R.id.badge:
                // API 5+ solution
                startActivity(new Intent(ServicesList.this,CartSummaryScreen.class));
                return true;

            case MENU_PRICE_LOW_TO_HIGH:
                item.setChecked(true);
                //sort_by="1";
                sharepref.edit().putString(Const.PREF_SORT_BY_SP,"1").apply();
                results.clear();
                adapter_servicesList.notifyDataSetChanged();
                int_from=0;
                int_to=10;
                new GetServicesListDefault().execute();
                return true;
            case MENU_PRICE_HIGH_TO_LOW:
                item.setChecked(true);
                //sort_by="2";
                sharepref.edit().putString(Const.PREF_SORT_BY_SP,"2").apply();
                results.clear();
                adapter_servicesList.notifyDataSetChanged();
                int_from=0;
                int_to=10;
                new GetServicesListDefault().execute();
                return true;
            case MENU_POPLAR:
                item.setChecked(true);
                //sort_by="0";
                sharepref.edit().putString(Const.PREF_SORT_BY_SP,"0").apply();
                results.clear();
                adapter_servicesList.notifyDataSetChanged();
                int_from=0;
                int_to=10;
                new GetServicesListDefault().execute();
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        sharepref.edit().putString(Const.PREF_STR_SERVICE_NAME,"").apply();
        sharepref.edit().putString(Const.PREF_STR_SERVICE_ID,"").apply();
        sharepref.edit().putString(Const.PREF_CITY_ID_SP,"").apply();
        sharepref.edit().putString(Const.PREF_CITY_NAME_SP,"").apply();
        sharepref.edit().putString(Const.PREF_SORT_BY_SP,"").apply();

        sharepref.edit().putString(Const.PREF_NEARBY_SP,"").apply();
        sharepref.edit().putString(Const.PREF_PRICE_PRICERANGE_SP,"").apply();
        sharepref.edit().putString(Const.PREF_SORT_BY_SP,"").apply();


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

        sharepref.edit().putString(Const.PREF_STR_SERVICE_NAME,"").apply();
        sharepref.edit().putString(Const.PREF_STR_SERVICE_ID,"").apply();
        sharepref.edit().putString(Const.PREF_CITY_ID_SP,"").apply();
        sharepref.edit().putString(Const.PREF_CITY_NAME_SP,"").apply();
        sharepref.edit().putString(Const.PREF_SORT_BY_SP,"").apply();

        sharepref.edit().putString(Const.PREF_NEARBY_SP,"").apply();
        sharepref.edit().putString(Const.PREF_PRICE_PRICERANGE_SP,"").apply();
        sharepref.edit().putString(Const.PREF_SORT_BY_SP,"").apply();


    }
}
