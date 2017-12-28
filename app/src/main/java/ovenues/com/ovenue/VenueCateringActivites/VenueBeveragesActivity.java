package ovenues.com.ovenue.VenueCateringActivites;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;

import java.util.ArrayList;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.ServiceProviderCateringActivity.ServiceProviderBeveragesActivity;
import ovenues.com.ovenue.adapter.VenueDetailsPages.VenueBeveragesMenuAdapter;
import ovenues.com.ovenue.modelpojo.ServiceProviderChargesMenuModel.FoodMenuModel;
import ovenues.com.ovenue.utils.ConnectionDetector;

import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.beveragesJSONVenues;


public class VenueBeveragesActivity extends AppCompatActivity {



    boolean loading_data=false;
    private int visibleThreshold = 5;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;


    String menuId;
    RecyclerView mRecyclerView;
    ArrayList results;
    private RecyclerView.Adapter adapter;


    Boolean isInternetPresent = false;
    // View footerLayout;

    SharedPreferences sharepref;
    TextView title_beveragesmenu_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_beverages);

        // Make sure we use vector drawables
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        sharepref = getBaseContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo_toolbar);
        getSupportActionBar().setTitle("");

        title_beveragesmenu_service = (TextView)this.findViewById(R.id.title_beveragesmenu_service);

        // Initialize recycler view
        mRecyclerView = (RecyclerView)this.findViewById(R.id.rv_venuelist);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(VenueBeveragesActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        results = new ArrayList<ServiceProviderBeveragesActivity>();
        adapter = new VenueBeveragesMenuAdapter(results, VenueBeveragesActivity.this);

        mRecyclerView.setAdapter(adapter);

        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(VenueBeveragesActivity.this);
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests

            new GetMenu().execute();

        } else {
            Toast.makeText(VenueBeveragesActivity.this, "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();
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


    protected ProgressDialog progressDialog;
    public class GetMenu extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(VenueBeveragesActivity.this, "Loading", "Please Wait...", true, false);

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


                JsonArray array_beverages_menu = beveragesJSONVenues;

                if(!array_beverages_menu.isJsonNull() && array_beverages_menu.size()>0){

                    for (int j = 0; j < array_beverages_menu.size(); j++) {
                        String beverage_id = array_beverages_menu.get(j).getAsJsonObject().get("beverage_id").getAsString();
                        String option_desc = array_beverages_menu.get(j).getAsJsonObject().get("option_desc").getAsString();
                        String option_charges = array_beverages_menu.get(j).getAsJsonObject().get("option_charges").getAsString();
                        String is_group_size = !array_beverages_menu.get(j).getAsJsonObject().get("is_group_size").isJsonNull()
                                ? array_beverages_menu.get(j).getAsJsonObject().get("is_group_size").getAsString() : null;
                        String group_size_from = !array_beverages_menu.get(j).getAsJsonObject().get("group_size_from").isJsonNull()
                                ? array_beverages_menu.get(j).getAsJsonObject().get("group_size_from").getAsString() : null;
                        String group_size_to = !array_beverages_menu.get(j).getAsJsonObject().get("group_size_to").isJsonNull()
                                ? array_beverages_menu.get(j).getAsJsonObject().get("group_size_to").getAsString() : null;

                        results.add(new FoodMenuModel(beverage_id,option_desc,"" ,option_charges,is_group_size,group_size_from,group_size_to));

                    }
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();



                }

            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }


}
