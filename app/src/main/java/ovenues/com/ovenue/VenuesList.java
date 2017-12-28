package ovenues.com.ovenue;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
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
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ovenues.com.ovenue.adapter.VenuesListAdapter;
import ovenues.com.ovenue.adapter.autocomplete_textviews.VenueNameSearchAutocompleteAdapter;
import ovenues.com.ovenue.cart.CartSummaryScreen;
import ovenues.com.ovenue.modelpojo.VenuesListModel;
import ovenues.com.ovenue.modelpojo.autocompleteSearchviewCity.SearchVenueSPCityModel;
import ovenues.com.ovenue.utils.ConnectionDetector;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.utils.APICall.post;

public class VenuesList extends AppCompatActivity {

    boolean is_allDataLoaded=false;
    Activity activity;
    private static final int MENU_PRICE_LOW_TO_HIGH = Menu.FIRST;
    private static final int MENU_PRICE_HIGH_TO_LOW= Menu.FIRST + 1;
    private static final int MENU_POPLAR = Menu.FIRST + 2;

    boolean loading_data=false;
    private int visibleThreshold = 5;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;



    RecyclerView mRecyclerView;
    ArrayList results;
    private RecyclerView.Adapter venuelist_adapter;


    Boolean isInternetPresent = false;
    SwipeRefreshLayout mSwipeRefreshLayout;
    View footerLayout;
    int int_from=0,int_to=10;

    SharedPreferences sharepref;
    String str_venue_name=null,city_id = "",event_type_id = "",venue_type_id = "" ,amenty_id= "",guest_count_min  = "",guest_count_max = "",price_min  = "",price_max = "",sort_by = "";

    public AutoCompleteTextView et_venuename_search;
    VenueNameSearchAutocompleteAdapter adapter_venue_name_search ;
    ArrayList<SearchVenueSPCityModel> searchcitymodel;
    TextView tv_sort,tv_filter;
    public AutoCompleteTextView gen_search;
    AlertDialog alertDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venues_list);


        activity=this;
        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        final Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("");

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
                    new GetVenuesNames().execute();
                }

            }
        });

        gen_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                startActivity(new Intent(VenuesList.this,VenueDetailsMainFragment.class)
                        .putExtra("venue_name",adapter_venue_name_search.getItem(position).getName())
                        .putExtra("venue_id",adapter_venue_name_search.getItem(position).getId()));

                gen_search.setText("");
                gen_search.clearFocus();
                /*Toast.makeText(MainNavigationScreen.this, adapter_venue_name_search.getItem(position).getId().toString(),Toast.LENGTH_SHORT).show();*/

            }
        });


        // new GetVenuesListCity().execute();
        et_venuename_search=(AutoCompleteTextView)this.findViewById(R.id.et_venuename_search);
        et_venuename_search.setText("");

        searchcitymodel = new ArrayList<SearchVenueSPCityModel>();

        et_venuename_search.addTextChangedListener(new TextWatcher() {
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
                    new GetVenuesNames().execute();
                }

            }
        });

        et_venuename_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                startActivity(new Intent(VenuesList.this,VenueDetailsMainFragment.class)
                        .putExtra("venue_name",adapter_venue_name_search.getItem(position).getName())
                        .putExtra("venue_id",adapter_venue_name_search.getItem(position).getId()));

                /*Toast.makeText(VenuesList.this, adapter_venue_name_search.getItem(position).getId().toString(),Toast.LENGTH_SHORT).show();*/

            }
        });

        city_id=sharepref.getString(Const.PREF_CITY_ID,"");
        event_type_id=sharepref.getString(Const.PREF_EVENT_TYPE_ID,"");
        venue_type_id=sharepref.getString(Const.PREF_VENUE_TYPE_ID,"");
        amenty_id=sharepref.getString(Const.PREF_AMENITIES_ID,"");
        guest_count_min=sharepref.getString(Const.PREF_GUEST_COUNT_MIN,"");
        guest_count_max=sharepref.getString(Const.PREF_GUEST_COUNT_MAX,"");
        price_min=sharepref.getString(Const.PREF_PRICE_MIN,"");
        price_max=sharepref.getString(Const.PREF_PRICE_MAX,"");
        sort_by=sharepref.getString(Const.PREF_SORT_BY,"");


        final String[] option = new String[] { "Price Low To High ", "Price High To Low" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Option");
        builder.setSingleChoiceItems(option, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:
                        sort_by="1";
                        sharepref.edit().putString(Const.PREF_SORT_BY,"1").apply();
                        results.clear();
                        venuelist_adapter.notifyDataSetChanged();
                        int_from=0;
                        int_to=10;
                        new GetVenuesListDefault().execute();
                        dialog.dismiss();

                        break;
                    case 1:

                        sort_by="2";
                        sharepref.edit().putString(Const.PREF_SORT_BY,"2").apply();
                        results.clear();
                        venuelist_adapter.notifyDataSetChanged();
                        int_from=0;
                        int_to=10;
                        new GetVenuesListDefault().execute();

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

        // Fetching footer layout
        footerLayout = this.findViewById(R.id.footerView);
        tv_sort = (TextView)this.findViewById(R.id.tv_sort);
        tv_filter = (TextView)this.findViewById(R.id.tv_filter);

        tv_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openOptionsMenu();
               // myToolbar.showOverflowMenu();
                alertDialog1.show();

            }
        });

        tv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VenuesList.this,VenuesSearchFilter.class));
                //finish();
            }
        });

        // Initialize recycler view
        mRecyclerView = (RecyclerView)this.findViewById(R.id.rv_venuelist);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(VenuesList.this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        results = new ArrayList<VenuesList>();
        venuelist_adapter = new VenuesListAdapter(results, VenuesList.this);

        mRecyclerView.setAdapter(venuelist_adapter);



        mSwipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                results.clear();
                venuelist_adapter.notifyDataSetChanged();

                int_from=0;
                int_to=10;

                if(is_allDataLoaded==true){
                    is_allDataLoaded=false;
                }
                new GetVenuesListDefault().execute();

                mRecyclerView.setEnabled(false);
                mRecyclerView.setNestedScrollingEnabled(false);
                Toast.makeText(VenuesList.this,"Loading...",Toast.LENGTH_LONG).show();


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

            new GetVenuesListDefault().execute();

        } else {
            Toast.makeText(VenuesList.this, "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();
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
                            if(is_allDataLoaded==false){
                                new GetVenuesListDefault().execute();
                            }
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

    String res;
    protected ProgressDialog progressDialog;
    private class GetVenuesListDefault extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(VenuesList.this, "Loading", "Please Wait...", true, false);
            loading_data=true;
        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {
                String str_from=Integer.toString(int_from);
                String str_to=Integer.toString(int_to);

                String upend = "city="+sharepref.getString(Const.PREF_CITY_ID,"")+"&event_type="+event_type_id+"&venue_type="+venue_type_id
                        +"&amenities="+amenty_id+"&guest_count_min="+guest_count_min+"&guest_count_max="+guest_count_max
                                +"&price_min="+price_min+"&price_max"+price_max+"&from="+str_from+"&to="+str_to+"&sort_by="+sort_by+"&user_id="+sharepref.getString(Const.PREF_USER_ID,"");
                String response = post(Const.SERVER_URL_API +"filter_venues?"+upend, "","get");
               // Log.d("URL ====",Const.SERVER_URL_API+"filter_venues?"+upend);
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
              // Log.i("RESPONSE venue list", res);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi
                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(res).getAsJsonObject();
                if(response_string.equals("success")){


                    JsonArray venuesObj = rootObj.getAsJsonObject("message")
                            .getAsJsonArray("venues");

                    if(venuesObj.size()>0){

                            String status = rootObj.get("status").getAsString();
                            String total = rootObj.getAsJsonObject("message").get("total").getAsString();
                            //String lng = locObj.get("lng").getAsString();
                           String message=obj.getString("message");
                            for (int j = 0; j < venuesObj.size(); j++) {
                                String id = venuesObj.get(j).getAsJsonObject().get("venue_id").getAsString();
                                //Log.e("id---",id);
                                String venue_id  =venuesObj.get(j).getAsJsonObject().get("venue_id").getAsString();
                                String city_id  =venuesObj.get(j).getAsJsonObject().get("city_id").getAsString();
                                String address1  =venuesObj.get(j).getAsJsonObject().get("address1").getAsString();
                                String address2  =venuesObj.get(j).getAsJsonObject().get("city_name").getAsString();
                                String statae  =venuesObj.get(j).getAsJsonObject().get("statae").getAsString();
                                String zipcode  =venuesObj.get(j).getAsJsonObject().get("zipcode").getAsString();
                                String venue_type_id  =venuesObj.get(j).getAsJsonObject().get("venue_type_id").getAsString();
                                String max_occupancy  =venuesObj.get(j).getAsJsonObject().get("max_occupancy").getAsString();

                                String min_occupancy  =venuesObj.get(j).getAsJsonObject().get("min_occupancy").getAsString();
                                String time_extention_allowed  =venuesObj.get(j).getAsJsonObject().has("time_extention_allowed")
                                        ? venuesObj.get(j).getAsJsonObject().get("time_extention_allowed").getAsString():null;

                                String t_and_c  =venuesObj.get(j).getAsJsonObject().has("t_and_c")
                                             ? venuesObj.get(j).getAsJsonObject().get("t_and_c").getAsString():null;


                                String is_active  =venuesObj.get(j).getAsJsonObject().get("is_active").getAsString();
                                String is_registered = venuesObj.get(j).getAsJsonObject().get("is_registered").getAsString();

                                //String security_deposit  =venuesObj.get(j).getAsJsonObject().get("security_deposit").getAsString();
                                String security_deposit  = venuesObj.get(j).getAsJsonObject().has("security_deposit")
                                        ? venuesObj.get(j).getAsJsonObject().get("security_deposit").getAsString():null;

                                String venue_name  =!venuesObj.get(j).getAsJsonObject().get("venue_name").isJsonNull()
                                        ? venuesObj.get(j).getAsJsonObject().get("venue_name").getAsString():null;

                                String average_cost  = !venuesObj.get(j).getAsJsonObject().get("average_charges").isJsonNull()
                                        ? venuesObj.get(j).getAsJsonObject().get("average_charges").getAsString():null;

                                String cost_type  =venuesObj.get(j).getAsJsonObject().get("charges_type").getAsString();
                                String is_request_quote=venuesObj.get(j).getAsJsonObject().get("is_request_quote").getAsString();

                                String package_hours=venuesObj.get(j).getAsJsonObject().get("package_hours").getAsString();

                                String photo_url  = !venuesObj.get(j).getAsJsonObject().get("photo_url").isJsonNull()
                                        ? venuesObj.get(j).getAsJsonObject().get("photo_url").getAsString():null;

                                String city_name  =venuesObj.get(j).getAsJsonObject().get("city_name").getAsString();

                                String yelp_rating = !venuesObj.get(j).getAsJsonObject().get("yelp_rating").isJsonNull()
                                        ? venuesObj.get(j).getAsJsonObject().get("yelp_rating").getAsString():null;
                                String yelp_review_count = !venuesObj.get(j).getAsJsonObject().get("yelp_review_count").isJsonNull()
                                        ? venuesObj.get(j).getAsJsonObject().get("yelp_review_count").getAsString():null;
                                String yelp_price_range =  !venuesObj.get(j).getAsJsonObject().get("yelp_price_range").isJsonNull()
                                        ? venuesObj.get(j).getAsJsonObject().get("yelp_price_range").getAsString():null;
                                String yelp_id =  !venuesObj.get(j).getAsJsonObject().get("yelp_id").isJsonNull()
                                        ? venuesObj.get(j).getAsJsonObject().get("yelp_id").getAsString():null;

                                JsonArray serviceArray = !venuesObj.get(j).getAsJsonObject().get("services").isJsonNull()
                                        ?venuesObj.get(j).getAsJsonObject().get("services").getAsJsonArray():null;


                               /* JsonObject catering_icon = venuesObj.get(j).getAsJsonObject().has("catering_icon")?
                                        venuesObj.get(j).getAsJsonObject().get("catering_icon").getAsJsonObject():null;
                                if(catering_icon ==null){
                                    serviceArray.add(catering_icon);
                                }*/

                                String fav_count  =venuesObj.get(j).getAsJsonObject().get("fav_count").getAsString();
                                String is_favourite =venuesObj.get(j).getAsJsonObject().get("is_favourite").getAsString();
                                //Log.e("services ==",venue_name+"\t== "+serviceArray.toString());
                                results.add(new VenuesListModel(id,  venue_id,  city_id,  address1,  address2,  statae,  zipcode,  venue_type_id,  max_occupancy,
                                        min_occupancy,  time_extention_allowed,  t_and_c,  is_active,  security_deposit,  venue_name,  average_cost,
                                        cost_type, is_request_quote,package_hours, photo_url,  city_name,  serviceArray.toString(),  fav_count,  is_favourite ,is_registered,yelp_rating,yelp_review_count,
                                        yelp_price_range,yelp_id));
                            }

                            if(mSwipeRefreshLayout.isRefreshing()){
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            venuelist_adapter.notifyDataSetChanged();
                    }else{
                       /* results.clear();
                        venuelist_adapter.notifyDataSetChanged();*/
                        loading_data=false;
                        progressDialog.dismiss();

                        final AlertDialog.Builder alertbox = new AlertDialog.Builder(VenuesList.this);
                        alertbox.setMessage("No data found.");
                        alertbox.setTitle("Sorry ! ");
                        alertbox.setIcon(R.mipmap.ic_launcher);

                        alertbox.setNeutralButton("OK",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0,int arg1) {
                                   /* startActivity(new Intent(VenuesList.this,MainNavigationScreen.class));
                                    sharepref.edit().putString(Const.PREF_CITY_ID,"").apply();
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

                    }else { Snackbar snackbar = Snackbar
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


                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(VenuesList.this);
                    alertbox.setMessage(message);
                    alertbox.setTitle("Sorry ! ");
                    alertbox.setIcon(R.mipmap.ic_launcher);

                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0,int arg1) {
                                   /* startActivity(new Intent(VenuesList.this,MainNavigationScreen.class));
                                    sharepref.edit().putString(Const.PREF_CITY_ID,"").apply();
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
                    is_allDataLoaded=true;
                    }
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

    String res3;
    //protected ProgressDialog progressDialog3;
    private class GetVenuesNames extends AsyncTask<Object, Void, String> {

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

                    adapter_venue_name_search = new VenueNameSearchAutocompleteAdapter(VenuesList.this, R.layout.activity_venues_list, R.id.row_text_view_only, searchcitymodel);
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


                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(VenuesList.this);
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



    ImageView ic_cart_actionbar;
    TextView tv_cartItemCount;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.cart_option, menu);
        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.cart_badge_layout);
        RelativeLayout notifCount = (RelativeLayout)   MenuItemCompat.getActionView(item);

        ic_cart_actionbar = (ImageView) notifCount.findViewById(R.id.ic_cart_actionbar);
        tv_cartItemCount = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
        tv_cartItemCount.setText(sharepref.getString(Const.PREF_USER_CART_TOTAL_ITEMS,"0"));


        ic_cart_actionbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VenuesList.this,CartSummaryScreen.class));

            }
        });

        //menu.add(0, MENU_PRICE_LOW_TO_HIGH, Menu.NONE, "Price Low To High ");
        //menu.add(0, MENU_PRICE_HIGH_TO_LOW, Menu.NONE, "Price High To Low");
        //menu.add(0, MENU_POPLAR, Menu.NONE, "Popular Venues");


        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //menu.clear();
        // Inflate the menu; this adds items to the action bar if it is present.
       /* getMenuInflater().inflate(R.menu.cart_option, menu);
        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.cart_badge_layout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);*/


        tv_cartItemCount.setText(sharepref.getString(Const.PREF_USER_CART_TOTAL_ITEMS, ""));

        ic_cart_actionbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VenuesList.this, CartSummaryScreen.class));

            }
        });


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
            case R.id.badge:
                // API 5+ solution
                startActivity(new Intent(VenuesList.this,CartSummaryScreen.class));
                return true;

                case MENU_PRICE_LOW_TO_HIGH:
                    item.setChecked(true);
                    sort_by="1";
                    sharepref.edit().putString(Const.PREF_SORT_BY,"1").apply();
                    results.clear();
                    venuelist_adapter.notifyDataSetChanged();
                    int_from=0;
                    int_to=10;
                    new GetVenuesListDefault().execute();
                    return true;
                case MENU_PRICE_HIGH_TO_LOW:
                    item.setChecked(true);
                    sort_by="2";
                    sharepref.edit().putString(Const.PREF_SORT_BY,"2").apply();
                    results.clear();
                    venuelist_adapter.notifyDataSetChanged();
                    int_from=0;
                    int_to=10;
                    new GetVenuesListDefault().execute();
                    return true;
                case MENU_POPLAR:
                    item.setChecked(true);
                    sort_by="0";
                    sharepref.edit().putString(Const.PREF_SORT_BY,"").apply();
                    results.clear();
                    venuelist_adapter.notifyDataSetChanged();
                    int_from=0;
                    int_to=10;
                    new GetVenuesListDefault().execute();
                    return true;


            default:
                return super.onOptionsItemSelected(item);

        }
    }

    protected void setMenuBackground(){
        // Log.d(TAG, "Enterting setMenuBackGround");
        getLayoutInflater().setFactory( new LayoutInflater.Factory() {
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                if ( name.equalsIgnoreCase( "com.android.internal.view.menu.IconMenuItemView" ) ) {
                    try { // Ask our inflater to create the view
                        LayoutInflater f = getLayoutInflater();
                        final View view = f.createView( name, null, attrs );
                        /* The background gets refreshed each time a new item is added the options menu.
                        * So each time Android applies the default background we need to set our own
                        * background. This is done using a thread giving the background change as runnable
                        * object */
                        new Handler().post(new Runnable() {
                            public void run () {
                                // sets the background color
                                view.setBackgroundResource( R.color.white);
                                // sets the text color
                                ((TextView) view).setTextColor(Color.BLACK);
                                // sets the text size
                                ((TextView) view).setTextSize(18);
                            }
                        } );
                        return view;
                    }
                    catch ( InflateException e ) {}
                    catch ( ClassNotFoundException e ) {}
                }
                return null;
            }});
    }

    /*@Override
    protected void onRestart() {
        super.onRestart();
       // new GetGenCartDetails().execute();
    }

    @Override
    protected void onResume() {
        //super.onResume();
        new GetGenCartDetails().execute();
    }*/

    String res_cart;
    class GetGenCartDetails extends AsyncTask<Object, Void, String> {

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
                String response;
                if(sharepref.getString(Const.PREF_USER_ID,"").equalsIgnoreCase("0")){
                    response = post(Const.SERVER_URL_API +"cart_total_items/user_id/"+sharepref.getString(Const.PREF_USER_ID,"")+"/token/"+sharepref.getString(Const.PREF_USER_TOKEN,""), "","get");
                }else{
                    response = post(Const.SERVER_URL_API +"cart_total_items/user_id/"+sharepref.getString(Const.PREF_USER_ID,""), "","get");
                }
               // Log.d("REsponce Json====",response);
                res_cart=response;
            }/*catch (JSONException e) {
                e.printStackTrace();
            }*/ catch (IOException e) {
                e.printStackTrace();
            }


            return res_cart;

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String response_string = "";

           // Log.i("RESPONSE GEN Cart", res_cart);
            try{
                JSONObject obj = new JSONObject(res_cart);

                response_string=obj.getString("status");

                if(response_string.equals("success")){

                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(res_cart).getAsJsonObject();

                    JsonObject cart_items = rootObj.getAsJsonObject("message").get("cart_items").getAsJsonObject();

                    String venues = cart_items.getAsJsonObject().get("venues").getAsString();
                    String services  =cart_items.getAsJsonObject().get("services").getAsString();
                    String catering_services  =cart_items.getAsJsonObject().get("catering_services").getAsString();
                    String total_amount = cart_items.getAsJsonObject().get("total_amount").getAsString();

                    int total_item = Integer.parseInt(venues) + Integer.parseInt(services) + Integer.parseInt(catering_services);

                    sharepref.edit().putString(Const.PREF_USER_CART_TOTAL_ITEMS,Integer.toString(total_item)).apply();
                    sharepref.edit().putString(Const.PREF_USER_CART_TOTAL_AMOUNT,total_amount).apply();

                    sharepref.edit().putString(Const.PREF_USER_CART_VENUES,venues).apply();
                    sharepref.edit().putString(Const.PREF_USER_CART_CATERINGS,catering_services).apply();
                    sharepref.edit().putString(Const.PREF_USER_CART_SERVICES,services).apply();

                    tv_cartItemCount.setText(sharepref.getString(Const.PREF_USER_CART_TOTAL_ITEMS,""));
                    if(tv_cartItemCount.getText().length()==0
                            ||tv_cartItemCount.getText()==null){
                        tv_cartItemCount.setVisibility(View.GONE);
                    }else{
                        if(tv_cartItemCount.getText().toString().equalsIgnoreCase("0")){
                            tv_cartItemCount.setVisibility(View.GONE);
                        }else{
                            tv_cartItemCount.setVisibility(View.VISIBLE);
                        }
                    }
                    activity.invalidateOptionsMenu();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

            sharepref.edit().putString(Const.PREF_CITY_ID,"").apply();
            sharepref.edit().putString(Const.PREF_CITY_NAME,"").apply();
            sharepref.edit().putString(Const.PREF_EVENT_TYPE_ID,"").apply();
            sharepref.edit().putString(Const.PREF_VENUE_TYPE_ID,"").apply();
            VenuesSearchFilter.str_pref_amenities_id="";
            sharepref.edit().putString(Const.PREF_AMENITIES_ID,"").apply();
            sharepref.edit().putString(Const.PREF_GUEST_COUNT_MIN,"").apply();
            sharepref.edit().putString(Const.PREF_GUEST_COUNT_MAX,"").apply();
            sharepref.edit().putString(Const.PREF_PRICE_MIN,"").apply();
            sharepref.edit().putString(Const.PREF_PRICE_MAX,"").apply();
            sharepref.edit().putString(Const.PREF_SORT_BY,"").apply();

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

        sharepref.edit().putString(Const.PREF_CITY_ID,"").apply();
        sharepref.edit().putString(Const.PREF_CITY_NAME,"").apply();
        sharepref.edit().putString(Const.PREF_EVENT_TYPE_ID,"").apply();
        sharepref.edit().putString(Const.PREF_VENUE_TYPE_ID,"").apply();
        VenuesSearchFilter.str_pref_amenities_id="";
        sharepref.edit().putString(Const.PREF_AMENITIES_ID,"").apply();
        sharepref.edit().putString(Const.PREF_GUEST_COUNT_MIN,"").apply();
        sharepref.edit().putString(Const.PREF_GUEST_COUNT_MAX,"").apply();
        sharepref.edit().putString(Const.PREF_PRICE_MIN,"").apply();
        sharepref.edit().putString(Const.PREF_PRICE_MAX,"").apply();

    }
}
