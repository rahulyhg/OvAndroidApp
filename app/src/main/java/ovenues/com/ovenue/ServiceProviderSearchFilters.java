package ovenues.com.ovenue;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ovenues.com.ovenue.adapter.autocomplete_textviews.SearchCityAdapter;
import ovenues.com.ovenue.modelpojo.autocompleteSearchviewCity.SearchVenueSPCityModel;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.utils.APICall.post;

public class ServiceProviderSearchFilters extends AppCompatActivity {

    static Activity activity;
    static SharedPreferences sharepref;
    public static TextView tv_milesAway;
    TextView tv_clear_filter,tv_apply;

    String milesAway_count_min,milesAway_count_max;
    public static String str_search_radius ="10", str_service_id ="", sort_by ="";
    AutoCompleteTextView et_city;
    SearchCityAdapter adapter_city ;
    ArrayList<SearchVenueSPCityModel> searchcitymodel = new ArrayList<>();

    String city_id="",priceranges="",nearby;
    ImageView img_close;
    CheckBox cb_SPInexpensive,cb_SPModerate,cb_SPExpensive ,cb_SPUltraHighEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_search_filters);


        activity=this;
        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        cb_SPInexpensive = (CheckBox)this.findViewById(R.id.cb_SPInexpensive);
        cb_SPModerate = (CheckBox)this.findViewById(R.id.cb_SPModerate);
        cb_SPExpensive = (CheckBox)this.findViewById(R.id.cb_SPExpensive);
        cb_SPUltraHighEnd = (CheckBox)this.findViewById(R.id.cb_SPUltraHighEnd);

        img_close = (ImageView)this.findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        new GetVenuesListCity().execute();

        tv_clear_filter = (TextView) myToolbar.findViewById(R.id.tv_clear_filter);
        tv_apply = (TextView) myToolbar.findViewById(R.id.tv_apply);

//        final int  index_selectedServiceType = Integer.parseInt(getIntent().getStringExtra("index_selectedServiceType")) ;

        tv_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharepref.edit().putString(Const.PREF_CITY_ID_SP,city_id).apply();
                sharepref.edit().putString(Const.PREF_CITY_NAME_SP,et_city.getText().toString()).apply();
                sharepref.edit().putString(Const.PREF_NEARBY_SP,nearby).apply();

                if(cb_SPInexpensive.isChecked()==true){
                    priceranges="$";
                }
                if(cb_SPModerate.isChecked()==true){
                    priceranges=priceranges+",$$";
                }
                if(cb_SPExpensive.isChecked()==true){
                    priceranges=priceranges+",$$$";
                }
                if(cb_SPUltraHighEnd.isChecked()==true){
                    priceranges=priceranges+",$$$$";
                }

                if(priceranges.substring(0).contains(",")){
                    priceranges.substring(1,priceranges.length());
                }else{
                    priceranges=priceranges;
                }

                sharepref.edit().putString(Const.PREF_PRICE_PRICERANGE_SP,priceranges).apply();

                startActivity(new Intent(ServiceProviderSearchFilters.this,ServicesList.class)
                        .putExtra("index_selectedServiceType","0"));
                finish();

            }
        });


        tv_clear_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertbox = new AlertDialog.Builder(ServiceProviderSearchFilters.this);
                alertbox.setMessage("filter Will not applied , want to clear ?");
                alertbox.setTitle("Cancel ! ");
                alertbox.setIcon(R.mipmap.ic_launcher);

                alertbox.setNeutralButton("YES",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0,int arg1) {
                                sharepref.edit().putString(Const.PREF_CITY_ID_SP,"").apply();
                                sharepref.edit().putString(Const.PREF_CITY_NAME_SP,"").apply();
                                sharepref.edit().putString(Const.PREF_NEARBY_SP,"").apply();
                                sharepref.edit().putString(Const.PREF_PRICE_PRICERANGE_SP,"").apply();

                                startActivity(new Intent(ServiceProviderSearchFilters.this,ServicesList.class)
                                        .putExtra("index_selectedServiceType","0"));
                                finish();
                            }
                        });
                alertbox.show();
            }
        });



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

                city_id= adapter_city.getItem(position).getId();

                sharepref.edit().putString(Const.PREF_CITY_ID_SP,adapter_city.getItem(position).getId()).apply();
                sharepref.edit().putString(Const.PREF_CITY_NAME_SP,adapter_city.getItem(position).getName()+","+adapter_city.getItem(position).getCounty()).apply();
            }
        });
        tv_milesAway=(TextView)this.findViewById(R.id.tv_milesAway);


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
                str_search_radius =milesAway_count_max;
                nearby = str_search_radius;
                //Log.e("values ===",String.valueOf(tickIndex));

                //Toast.makeText(ServicesList.this,"values =="+rightPinValue,Toast.LENGTH_LONG).show();
            }
        });





    }

    String res2;
    protected ProgressDialog progressDialog2;
    class GetVenuesListCity extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog2 = ProgressDialog.show(ServiceProviderSearchFilters.this, "Loading", "Please Wait...", true, false);
        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {
                String response = post(Const.SERVER_URL_API +"cities_having_sp", "","get");
                //Log.d("URL ====",Const.SERVER_URL_API+"filter_venues?");
                res2=response;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res2;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try{
                JSONObject obj = new JSONObject(res2);
                //Log.i("RESPONSE", res2);
                response_string=obj.getString("status");
                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(res2).getAsJsonObject();


                if(response_string.equals("success")){

                    JsonArray cityObj = rootObj.getAsJsonArray("message");

                    for (int j = 0; j < cityObj.size(); j++) {
                        String city_id = cityObj.get(j).getAsJsonObject().get("city_id").getAsString();
                        String city_name  =cityObj.get(j).getAsJsonObject().get("city_name").getAsString();
                        String county  =cityObj.get(j).getAsJsonObject().get("county").getAsString();

                        searchcitymodel.add(new SearchVenueSPCityModel(city_id,city_name,county,"","","",""));
                    }

                    adapter_city = new SearchCityAdapter(ServiceProviderSearchFilters.this, searchcitymodel);
                    et_city.setAdapter(adapter_city);

                    //Log.d("adapterServiceTypesList.getItemCount()=", Integer.toString(adapter_venuesCity.getCount()));

                    progressDialog2.dismiss();
                }else{
                    progressDialog2.dismiss();
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


                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(ServiceProviderSearchFilters.this);
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
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        /*startActivity(new Intent(VenuesSearchFilter.this,VenuesList.class)
        .putExtra("city",str_city));*/
    }

}
