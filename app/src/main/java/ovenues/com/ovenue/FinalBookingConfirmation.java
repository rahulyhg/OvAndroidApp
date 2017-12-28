package ovenues.com.ovenue;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;

import ovenues.com.ovenue.adapter.FinalBookingConfirmationAdapter;
import ovenues.com.ovenue.modelpojo.FinalBookingConfirmationModel;

public class FinalBookingConfirmation extends AppCompatActivity {

    //JsonObject Json_Bookings=null;
    boolean loading_data=false;
    private int visibleThreshold = 5;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;



    RecyclerView mRecyclerView;
    ArrayList results;
    private RecyclerView.Adapter orderbookings_adapter;


    Boolean isInternetPresent = false;
    View footerLayout;
    int int_from=0,int_to=10;

    SharedPreferences sharepref;
    Button btn_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_booking_confirmation);


        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Booking Complete");

      
        // Initialize recycler view
        mRecyclerView = (RecyclerView)this.findViewById(R.id.rv_venuelist);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(FinalBookingConfirmation.this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        results = new ArrayList<FinalBookingConfirmationModel>();
        orderbookings_adapter = new FinalBookingConfirmationAdapter(results, FinalBookingConfirmation.this);

        mRecyclerView.setAdapter(orderbookings_adapter);


        new GetBookings().execute();


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (mRecyclerView == null || mRecyclerView.getChildCount() == 0) ? 0 : mRecyclerView.getChildAt(0).getTop();

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
                    }
                }

                //=======================================================================================================
            }

            @Override
            public void onScrollStateChanged(RecyclerView mRecyclerView, int newState) {
                super.onScrollStateChanged(mRecyclerView, newState);
            }
        });

        btn_finish = (Button)this.findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FinalBookingConfirmation.this,MainNavigationScreen.class));
                finish();
            }
        });

    }


    String res;
    protected ProgressDialog progressDialog;
    private class GetBookings extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(FinalBookingConfirmation.this, "Loading", "Please Wait...", true, false);
            loading_data=true;
        }

        @Override
        protected String doInBackground(Object... parametros) {


            return res;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            System.out.println("got JSON=="+getIntent().getStringExtra("JSON"));
            super.onPostExecute(result);


            try{
                JSONObject obj = new JSONObject(getIntent().getStringExtra("JSON"));
                // Log.i("RESPONSE venue list", res);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(getIntent().getStringExtra("JSON")).getAsJsonObject();

                    JsonArray bookingObj = !rootObj.getAsJsonObject("message").get("booking").isJsonNull()
                             ? rootObj.getAsJsonObject("message").get("booking").getAsJsonArray() : null;

                    if(bookingObj!=null && bookingObj.size()>0){

                        for(int j=0;j<bookingObj.size();j++) {
                            String cart_id = !bookingObj.get(j).getAsJsonObject().get("cart_id").isJsonNull()
                                    ? bookingObj.get(j).getAsJsonObject().get("cart_id").getAsString() : "";
                            String vendor = bookingObj.get(j).getAsJsonObject().get("vendor").getAsString();
                            String item_name = !bookingObj.get(j).getAsJsonObject().isJsonNull()
                                    ?bookingObj.get(j).getAsJsonObject().get("item_name").getAsString() : "";
                            String error = !bookingObj.get(j).getAsJsonObject().get("error").isJsonNull()
                                    ?bookingObj.get(j).getAsJsonObject().get("error").getAsString():"";
                            String type = bookingObj.get(j).getAsJsonObject().get("type").getAsString();
                            String booking_id =bookingObj.get(j).getAsJsonObject().get("booking_id").getAsString();


                            results.add(new FinalBookingConfirmationModel(type, booking_id, vendor, cart_id, item_name, error));
                        }
                    }

                    JsonArray errorObj = !rootObj.getAsJsonObject("message").get("error").isJsonNull()
                            ?rootObj.getAsJsonObject("message").getAsJsonArray("error").getAsJsonArray() : null;

                    if(errorObj!=null && errorObj.size()>0){

                        for(int j=0;j<errorObj.size();j++) {
                            String cart_id = errorObj.get(j).getAsJsonObject().get("cart_id").getAsString();
                            String vendor = errorObj.get(j).getAsJsonObject().get("vendor").getAsString();
                            String item_name = !errorObj.get(j).getAsJsonObject().isJsonNull()
                                    ?errorObj.get(j).getAsJsonObject().get("item_name").getAsString() : "";
                            String error = errorObj.get(j).getAsJsonObject().get("error").getAsString();
                            String type = !errorObj.get(j).getAsJsonObject().get("type").isJsonNull()
                                    ?errorObj.get(j).getAsJsonObject().get("type").getAsString() : "";
                            String booking_id =!errorObj.get(j).getAsJsonObject().get("booking_id").isJsonNull()
                                    ?errorObj.get(j).getAsJsonObject().get("booking_id").getAsString():"";


                            results.add(new FinalBookingConfirmationModel(type, booking_id, vendor, cart_id, item_name, error));
                        }
                    }
                    orderbookings_adapter.notifyDataSetChanged();

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


                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(FinalBookingConfirmation.this);
                    alertbox.setMessage(message);
                    alertbox.setTitle("Sorry ! ");
                    alertbox.setIcon(R.mipmap.ic_launcher);

                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0,int arg1) {
                                   /* startActivity(new Intent(FinalBookingConfirmation.this,MainNavigationScreen.class));
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
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        /*startActivity(new Intent(FinalBookingConfirmation.this,MainNavigationScreen.class));
        finish();*/
    }
}
