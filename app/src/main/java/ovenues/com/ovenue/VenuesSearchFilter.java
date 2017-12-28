package ovenues.com.ovenue;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ovenues.com.ovenue.adapter.AmenitiesGridListAdapter;
import ovenues.com.ovenue.adapter.Spiners.SpinerWithDynamicArrayList;
import ovenues.com.ovenue.adapter.autocomplete_textviews.SearchCityAdapter;
import ovenues.com.ovenue.modelpojo.IdNameUrlGrideRaw;
import ovenues.com.ovenue.modelpojo.Spiners.SearchVenueSpiners;
import ovenues.com.ovenue.modelpojo.autocompleteSearchviewCity.SearchVenueSPCityModel;
import ovenues.com.ovenue.utils.Const;
import ovenues.com.ovenue.utils.ExpandableHeightsGridView;

import static ovenues.com.ovenue.utils.APICall.post;

public class VenuesSearchFilter extends AppCompatActivity {

    AutoCompleteTextView et_city_venue;
    SearchCityAdapter adapter ;
    ArrayList<SearchVenueSPCityModel> searchcitymodel = new ArrayList<>();

    SharedPreferences sharepref;
    //BELOW CODE IS FOR GRIDE VENDOR DYNAMIC VIEW==========================================
    ExpandableHeightsGridView exp_gridview;
    ArrayList<IdNameUrlGrideRaw> listamenities =new ArrayList<>();
    AmenitiesGridListAdapter exp_grid_adapter;

    TextView tv_guestCount,tv_priceRange,tv_clear_filter,tv_apply;

    Spinner sp_venueType,sp_eventType;
    SpinerWithDynamicArrayList eventTypeAdapter,venueTypeAdapter;
    ArrayList<SearchVenueSpiners> sp_eventType_aarayList,sp_venueType_aarayList;

    public static String str_city_id="",str_city_name="",str_pref_amenities_id= "",guest_count_min  = "",guest_count_max = "",price_min  = "",price_max = "",sort_by = "";
    ImageView img_close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_venues);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);



        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        img_close = (ImageView)this.findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_clear_filter = (TextView) myToolbar.findViewById(R.id.tv_clear_filter);
        tv_apply = (TextView) myToolbar.findViewById(R.id.tv_apply);

        tv_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharepref.edit().putString(Const.PREF_CITY_ID,str_city_id).apply();
                sharepref.edit().putString(Const.PREF_CITY_NAME,et_city_venue.getText().toString()).apply();
                sharepref.edit().putString(Const.PREF_EVENT_TYPE_ID,sp_eventType_aarayList.get(sp_eventType.getSelectedItemPosition()).getId()).apply();
                sharepref.edit().putString(Const.PREF_VENUE_TYPE_ID,sp_venueType_aarayList.get(sp_venueType.getSelectedItemPosition()).getId()).apply();

            /*if(sp_eventType.getSelectedItemPosition()!=0){

            }
            if(sp_venueType.getSelectedItemPosition()!=0){

            }*/

                str_pref_amenities_id=str_pref_amenities_id.replaceAll("(.)\\1{1,}", "$1");
                if(str_pref_amenities_id!=null){
                    if(str_pref_amenities_id.length()>1 && str_pref_amenities_id.substring(0,1).equalsIgnoreCase(",")){
                        str_pref_amenities_id=str_pref_amenities_id.substring(1);
                    }
                }
                Log.d("str_pref_amenities_id==",str_pref_amenities_id);
                sharepref.edit().putString(Const.PREF_AMENITIES_ID,str_pref_amenities_id).apply();
                sharepref.edit().putString(Const.PREF_GUEST_COUNT_MIN,guest_count_min).apply();
                sharepref.edit().putString(Const.PREF_GUEST_COUNT_MAX,guest_count_max).apply();
                sharepref.edit().putString(Const.PREF_PRICE_MIN,price_min).apply();
                sharepref.edit().putString(Const.PREF_PRICE_MAX,price_max).apply();

                startActivity(new Intent(VenuesSearchFilter.this,VenuesList.class));
                finish();

            }
        });


        tv_clear_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertbox = new AlertDialog.Builder(VenuesSearchFilter.this);
                alertbox.setMessage("filter Will not applied , want to clear ?");
                alertbox.setTitle("Cancel ! ");
                alertbox.setIcon(R.mipmap.ic_launcher);

                alertbox.setNeutralButton("YES",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0,int arg1) {
                                sharepref.edit().putString(Const.PREF_CITY_ID,"").apply();
                                sharepref.edit().putString(Const.PREF_CITY_NAME,"").apply();
                                sharepref.edit().putString(Const.PREF_EVENT_TYPE_ID,"").apply();
                                sharepref.edit().putString(Const.PREF_VENUE_TYPE_ID,"").apply();
                                sharepref.edit().putString(Const.PREF_AMENITIES_ID,"").apply();

                                sharepref.edit().putString(Const.PREF_GUEST_COUNT_MIN,"").apply();
                                sharepref.edit().putString(Const.PREF_GUEST_COUNT_MAX,"").apply();
                                sharepref.edit().putString(Const.PREF_PRICE_MIN,"").apply();
                                sharepref.edit().putString(Const.PREF_PRICE_MAX,"").apply();

                                startActivity(new Intent(VenuesSearchFilter.this,VenuesList.class));
                                finish();
                            }
                        });
                alertbox.show();
            }
        });

       // et_city=(EditText)this.findViewById(R.id.et_city_venue);


        /*      searchcitymodel = populateCustomerData(searchcitymodel);*/
        et_city_venue = (AutoCompleteTextView)this.findViewById(R.id.et_city_venue);



        et_city_venue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
               /* Toast.makeText(VenuesSearchFilter.this,
                        adapter.getItem(position).getId().toString(),
                        Toast.LENGTH_SHORT).show();*/

                str_city_id=adapter.getItem(position).getId().toString();
                str_city_name =adapter.getItem(position).getName().toString()+adapter.getItem(position).getCounty().toString() ;
            }
        });

        // GUEST COUNT BAAR====get seekbar from view
        final RangeBar rangeBar_Guest = (RangeBar) this.findViewById(R.id.rangeBar_Guest);
        rangeBar_Guest.setSeekPinByIndex(0);
        // / get min and max text view
        tv_guestCount = (TextView) this.findViewById(R.id.tv_guestCount);
        // set listener
        rangeBar_Guest.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {

                if(rightPinIndex==0){
                    tv_guestCount.setText("Guest Count");
                    guest_count_min = String.valueOf(leftPinValue);
                    guest_count_max = String.valueOf(rightPinValue);
                }else{
                    tv_guestCount.setText("Guest Count ( 0 "+" - "+String.valueOf(rightPinValue)+" )");
                    guest_count_min = String.valueOf(leftPinValue);
                    guest_count_max = String.valueOf(rightPinValue);
                }

            }
        });


        //====PRICE SEEKBAR======
        final RangeBar rangeBar_Price = (RangeBar) this.findViewById(R.id.rangeBar_Price);
        rangeBar_Price.setSeekPinByIndex(0);
        tv_priceRange = (TextView) this.findViewById(R.id.tv_priceRange);
        // set listener
        rangeBar_Price.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {

                if(rightPinIndex==0){
                    tv_priceRange.setText("Budget");
                    guest_count_min = String.valueOf(leftPinValue);
                    guest_count_max = String.valueOf(rightPinValue);
                }else{
                    tv_priceRange.setText("Budget $ ( 0 "+" - "+String.valueOf(rightPinValue)+" )");
                    price_min = String.valueOf(leftPinValue);
                    price_max = String.valueOf(rightPinValue);
                }

            }
        });
        //===Spiner venue type config here=========
        sp_venueType = (Spinner)this.findViewById(R.id.sp_venueType);
        sp_venueType_aarayList =new ArrayList<>();
        venueTypeAdapter = new SpinerWithDynamicArrayList(VenuesSearchFilter.this , R.layout.row_spiners_venue_search_filter, sp_venueType_aarayList);
        sp_venueType.setAdapter(venueTypeAdapter);

        /*sp_venueType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                event_type_id = sp_venueType_aarayList.get(position).getId();
                Log.e("empid" , event_type_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        //===Spiner Event type config here=========
        sp_eventType = (Spinner)this.findViewById(R.id.sp_eventType);
        sp_eventType_aarayList =new ArrayList<>();
        eventTypeAdapter = new SpinerWithDynamicArrayList(VenuesSearchFilter.this , R.layout.row_spiners_venue_search_filter, sp_eventType_aarayList);
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



        //====GRIDE VIEW CODE STARTS=====================================================
        exp_gridview = (ExpandableHeightsGridView)this. findViewById(R.id.grid_amenities);

        exp_grid_adapter = new AmenitiesGridListAdapter(this , listamenities,"venuefilter");
        exp_gridview.setAdapter(exp_grid_adapter);
        exp_gridview.setExpanded(true);
        exp_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               /* Intent intent =new Intent(getActivity() , Product_List.class);
                intent.putExtra("catid" , i+1);
                intent.putExtra("category" , category);
                startActivity(intent);*/
            }
        });


        new GetVenuesFilterParams().execute();

    }


    String res;
    class GetVenuesFilterParams extends AsyncTask<Object, Void, String> {

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

                String response = post(Const.SERVER_URL_API +"city_amenities_and_venuetype_eventtype", "","get");
                //Log.d("REsponce Json====",response);
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


            try{JSONObject obj = new JSONObject(res);
                Log.i("RESPONSE", res);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    JsonParser parser = new JsonParser();

                    JsonObject rootObj = parser.parse(res).getAsJsonObject();

                    JsonArray citiesObj = rootObj.getAsJsonObject("message")
                            .getAsJsonArray("cities");
                    JsonArray aminitesObj = rootObj.getAsJsonObject("message")
                            .getAsJsonArray("amenties");
                    JsonArray venue_typeObj = rootObj.getAsJsonObject("message")
                            .getAsJsonArray("venue_type");
                    JsonArray events_typeObj = rootObj.getAsJsonObject("message")
                            .getAsJsonArray("events_type");


                    sp_venueType_aarayList.add(new SearchVenueSpiners("0","All Venue Type"));
                    sp_eventType_aarayList.add(new SearchVenueSpiners("0","All Event Type"));


                    for (int j = 0; j < citiesObj.size(); j++) {
                        String city_id=citiesObj.get(j).getAsJsonObject().get("city_id").getAsString();
                        String city_name=citiesObj.get(j).getAsJsonObject().get("city_name").getAsString();
                        String county=citiesObj.get(j).getAsJsonObject().get("county").getAsString();

                        searchcitymodel.add(new SearchVenueSPCityModel(city_id,city_name,county,"","","",""));

                    }


                    for (int j = 0; j < aminitesObj.size(); j++) {
                        String amenity_id=aminitesObj.get(j).getAsJsonObject().get("amenity_id").getAsString();
                        String amenity_name=aminitesObj.get(j).getAsJsonObject().get("amenity_name").getAsString();
                        String amenity_icon_url=aminitesObj.get(j).getAsJsonObject().get("amenity_icon_url").getAsString();

                        listamenities.add(new IdNameUrlGrideRaw(amenity_id , amenity_name ,amenity_icon_url,"","","",""));

                    }


                    for (int j = 0; j < venue_typeObj.size(); j++) {
                        String venue_type_id=venue_typeObj.get(j).getAsJsonObject().get("venue_type_id").getAsString();
                        String venue_type_name=venue_typeObj.get(j).getAsJsonObject().get("venue_type_name").getAsString();

                         //this is for just dyummydata set it will replace by original API comes...
                        SearchVenueSpiners  venue_types = new SearchVenueSpiners(venue_type_id , venue_type_name);
                        sp_venueType_aarayList.add(venue_types);

                    }


                    for (int j = 0; j < events_typeObj.size(); j++) {
                        String event_type_id=events_typeObj.get(j).getAsJsonObject().get("event_type_id").getAsString();
                        String event_type=events_typeObj.get(j).getAsJsonObject().get("event_type").getAsString();

                        //this is for just dyummydata set it will replace by original API comes...
                        SearchVenueSpiners  event_type_sp = new SearchVenueSpiners(event_type_id , event_type);
                        sp_eventType_aarayList.add(event_type_sp);

                    }

                    exp_grid_adapter.notifyDataSetChanged();
                    venueTypeAdapter.notifyDataSetChanged();
                    eventTypeAdapter.notifyDataSetChanged();

                    adapter = new SearchCityAdapter(VenuesSearchFilter.this, searchcitymodel);
                    et_city_venue.setAdapter(adapter);
                    adapter.notifyDataSetChanged();





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

  /*  private ArrayList<SearchVenueSPCityModel> populateCustomerData(ArrayList<SearchVenueSPCityModel> searchcitymodel) {
      searchcitymodel.add(new SearchVenueSPCityModel("aa","aa","aa"));
      *//*  for(int k=0;k<adapter.getCount();k++){
            searchcitymodel.add(new SearchVenueSPCityModel(adapter.getItem(k).getId(),adapter.getItem(k).getName(),adapter.getItem(k).getCounty()));
        }*//*
        return searchcitymodel;
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.done_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.done_action) {

            sharepref.edit().putString(Const.PREF_CITY_ID,str_city_id).apply();
            sharepref.edit().putString(Const.PREF_CITY_NAME,et_city_venue.getText().toString()).apply();
            sharepref.edit().putString(Const.PREF_EVENT_TYPE_ID,sp_eventType_aarayList.get(sp_eventType.getSelectedItemPosition()).getId()).apply();
            sharepref.edit().putString(Const.PREF_VENUE_TYPE_ID,sp_venueType_aarayList.get(sp_venueType.getSelectedItemPosition()).getId()).apply();

            /*if(sp_eventType.getSelectedItemPosition()!=0){

            }
            if(sp_venueType.getSelectedItemPosition()!=0){

            }*/

            str_pref_amenities_id=str_pref_amenities_id.replaceAll("(.)\\1{1,}", "$1");
            if(str_pref_amenities_id!=null){
                if(str_pref_amenities_id.length()>1 && str_pref_amenities_id.substring(0,1).equalsIgnoreCase(",")){
                    str_pref_amenities_id=str_pref_amenities_id.substring(1);
                }
            }
            Log.d("str_pref_amenities_id==",str_pref_amenities_id);
            sharepref.edit().putString(Const.PREF_AMENITIES_ID,str_pref_amenities_id).apply();
            sharepref.edit().putString(Const.PREF_GUEST_COUNT_MIN,guest_count_min).apply();
            sharepref.edit().putString(Const.PREF_GUEST_COUNT_MAX,guest_count_max).apply();
            sharepref.edit().putString(Const.PREF_PRICE_MIN,price_min).apply();
            sharepref.edit().putString(Const.PREF_PRICE_MAX,price_max).apply();

            startActivity(new Intent(VenuesSearchFilter.this,VenuesList.class));
            finish();

            return true;
        }
        if(id==android.R.id.home) {
            // API 5+ solution
            final AlertDialog.Builder alertbox = new AlertDialog.Builder(VenuesSearchFilter.this);
            alertbox.setMessage("filter Will not applied , want to close ?");
            alertbox.setTitle("Cancel ! ");
            alertbox.setIcon(R.mipmap.ic_launcher);

            alertbox.setNeutralButton("YES",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0,int arg1) {
                            startActivity(new Intent(VenuesSearchFilter.this,VenuesList.class));
                            finish();
                        }
                    });
            alertbox.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        /*startActivity(new Intent(VenuesSearchFilter.this,VenuesList.class)
        .putExtra("city",str_city));*/
    }
}
