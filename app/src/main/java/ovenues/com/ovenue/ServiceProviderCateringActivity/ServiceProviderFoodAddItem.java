package ovenues.com.ovenue.ServiceProviderCateringActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ovenues.com.ovenue.R;

import ovenues.com.ovenue.adapter.serviceprovider_details_page.ServiceProviderCateringChargesMenuAdapters.ServiceProviderFoodMenuItemAdapter;
import ovenues.com.ovenue.modelpojo.ServiceProviderChargesMenuModel.FoodMenuAddItemModel;
import ovenues.com.ovenue.utils.ConnectionDetector;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.food_menu;
import static ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderCateringPricingFragment.str_cart_ID_SP_Main;
import static ovenues.com.ovenue.utils.APICall.post;

public class ServiceProviderFoodAddItem extends AppCompatActivity {

    public static JsonArray selected_items_array;

    boolean loading_data = false;
    private int visibleThreshold = 5;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;
    Boolean isInternetPresent = false;

    JSONObject cateringfood_items_obj;
    JSONArray cart_cateringfood_items_array;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<FoodMenuAddItemModel> fooditemList;

    private Button btnSelection;
    String str_course_id, str_title,guest_count,price_per_plate,menu_id,menu_desc,str_menu_id;
    int int_total_food_price;
    TextView tv_menutitle,tv_menutitle_limit;

    SharedPreferences sharepref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_food_add_item);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        cart_cateringfood_items_array = new JSONArray();

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        str_course_id = getIntent().getStringExtra("course_id");
        str_title = getIntent().getStringExtra("title");
        guest_count= getIntent().getStringExtra("guest_count");
        str_menu_id = getIntent().hasExtra("menu_id")?getIntent().getStringExtra("menu_id"):"";


        tv_menutitle = (TextView) this.findViewById(R.id.tv_menutitle);
        tv_menutitle_limit = (TextView) this.findViewById(R.id.tv_menutitle_limit);

        sharepref = getBaseContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        btnSelection = (Button) findViewById(R.id.btnShow);

        fooditemList = new ArrayList<FoodMenuAddItemModel>();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // create an Object for Adapter
        mAdapter = new ServiceProviderFoodMenuItemAdapter(ServiceProviderFoodAddItem.this,fooditemList);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);

        btnSelection.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (!getIntent().hasExtra("cart_ID")&&(str_cart_ID_SP_Main.equalsIgnoreCase("") || str_cart_ID_SP_Main.isEmpty() || str_cart_ID_SP_Main == null)) {
                    Toast.makeText(ServiceProviderFoodAddItem.this, "Add Delivery / Pickup details First .", Toast.LENGTH_LONG).show();
                }

                /*else if( et_address_line_SP_cateringCharge.getText().length()<0 ||
                        et_address_line_SP_cateringCharge.getText().length()<0 || et_address_line_SP_cateringCharge.getText().length()<0) {
                    Toast.makeText(ServiceProviderFoodAddItem.this, "Please add Date , Time  & address", Toast.LENGTH_LONG).show();
                } else if(rb_delivery_SP_catering_charge.isChecked()==false && rb_pickup_SP_catering_charge.isChecked()==false && rb_onsite_SP_catering_charge.isChecked()==false) {
                    Toast.makeText(ServiceProviderFoodAddItem.this, "Please select Delivery / Pickup ?", Toast.LENGTH_LONG).show();
                }*/else {


                    if(guest_count==null || Integer.parseInt(guest_count)<1){
                        Toast.makeText(ServiceProviderFoodAddItem.this,"Add guest count(Qty) first.",Toast.LENGTH_LONG).show();
                    }else {
                        String data = "";
                        List<FoodMenuAddItemModel> stList = ((ServiceProviderFoodMenuItemAdapter) mAdapter).getStudentist();

                        double extra_charges = 0;
                        for (int i = 0; i < stList.size(); i++) {
                            FoodMenuAddItemModel singleStudent = stList.get(i);
                            if (singleStudent.isSelected() == true) {
                                if (singleStudent.getIs_additional_charges().equalsIgnoreCase("1")) {
                                    extra_charges = extra_charges + Double.parseDouble(singleStudent.getPrice_per_item());
                                }
                                data = data + "\n" + singleStudent.getItem_id().toString();

                                try {
                                    cateringfood_items_obj = new JSONObject();
                                    cateringfood_items_obj.put("item_id", singleStudent.getItem_id());
                                    cateringfood_items_obj.put("item_name", singleStudent.getItem_name());
                                    cateringfood_items_obj.put("is_additional_charges", singleStudent.getIs_additional_charges());
                                    if (singleStudent.getPrice_per_item() == null) {
                                        cateringfood_items_obj.put("price_per_item", "");
                                    } else {
                                        cateringfood_items_obj.put("price_per_item", singleStudent.getPrice_per_item());
                                    }
                                    Log.d("cetring items", cateringfood_items_obj.toString());

                                    cart_cateringfood_items_array.put(cateringfood_items_obj);
                                    //Log.d("josn array",cart_beverages_array.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        double total_price = (Double.parseDouble(stList.get(0).getPrice_per_plate()) + extra_charges) * Double.parseDouble(guest_count);

                        int_total_food_price = (int) total_price;
                        //Toast.makeText(VenueCateringFoodAddItem.this, "Selected Item id : \n" + data, Toast.LENGTH_LONG).show();
                        new AddCartCateringFood().execute();
                        //Toast.makeText(ServiceProviderFoodAddItem.this, "Selected Item id : \n" + data, Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        ConnectionDetector cd = new ConnectionDetector(this);
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests

            new GetMenu().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new GetSelectedItem().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else {
            Toast.makeText(this, "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();
        }


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (mRecyclerView == null || mRecyclerView.getChildCount() == 0) ? 0 : mRecyclerView.getChildAt(0).getTop();
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


                        if (loading_data != true) {
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
            progressDialog = ProgressDialog.show(ServiceProviderFoodAddItem.this, "Loading", "Please Wait...", true, false);

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
                    JsonArray array_food_menu = food_menu;

                    String max_item_selectn_allowed="";
                    if (!array_food_menu.isJsonNull() && array_food_menu.size() > 0) {

                        for (int j = 0; j < array_food_menu.size(); j++) {

                            String menu_id1 = array_food_menu.get(j).getAsJsonObject().get("menu_id").getAsString();
                            String price_per_plate1 = array_food_menu.get(j).getAsJsonObject().get("price_per_plate").getAsString();
                            String menu_desc1 = array_food_menu.get(j).getAsJsonObject().get("menu_desc").getAsString();
                            String total_courses = array_food_menu.get(j).getAsJsonObject().get("total_courses").getAsString();
                            String is_group_size=  array_food_menu.get(j).getAsJsonObject().get("is_group_size").isJsonNull()
                                    ? array_food_menu.get(j).getAsJsonObject().get("is_group_size").getAsString() : null;
                            String group_size_from =array_food_menu.get(j).getAsJsonObject().get("group_size_from").isJsonNull()
                                    ? array_food_menu.get(j).getAsJsonObject().get("group_size_from").getAsString(): null;
                            String group_size_to = array_food_menu.get(j).getAsJsonObject().get("group_size_to").isJsonNull()
                                    ? array_food_menu.get(j).getAsJsonObject().get("group_size_to").getAsString(): null;

                            JsonArray array_courses = !array_food_menu.get(j).getAsJsonObject().get("courses").getAsJsonArray().isJsonNull()
                                    ? array_food_menu.get(j).getAsJsonObject().get("courses").getAsJsonArray() : null;

                            //Log.d("Course array",array_courses.toString());

                            for (int k = 0; k < array_courses.size(); k++) {
                                String course_id = array_courses.get(k).getAsJsonObject().get("course_id").getAsString();
                                String course_title = array_courses.get(k).getAsJsonObject().get("course_title").getAsString();


                                // Log.i("course_id", course_id);
                                if (course_id.equalsIgnoreCase(str_course_id)) {
                                    //Log.i("got course id", str_course_id);
                                    menu_id = array_food_menu.get(j).getAsJsonObject().get("menu_id").getAsString();
                                    menu_desc = array_food_menu.get(j).getAsJsonObject().get("menu_desc").getAsString();
                                    price_per_plate = array_food_menu.get(j).getAsJsonObject().get("price_per_plate").getAsString();
                                     max_item_selectn_allowed = array_courses.get(k).getAsJsonObject().get("max_item_selectn_allowed").getAsString();

                                    getSupportActionBar().setTitle(menu_desc );
                                    tv_menutitle.setText(str_title);
                                    tv_menutitle_limit.setText("( MAX "+max_item_selectn_allowed+" items allowed.)");
                                    JsonArray array_items = !array_courses.get(k).getAsJsonObject().get("items").getAsJsonArray().isJsonNull()
                                            ? array_courses.get(k).getAsJsonObject().get("items").getAsJsonArray() : null;
                                    for (int l = 0; l < array_items.size(); l++) {
                                        //Log.i("array_items", array_items.toString());
                                        String item_id = array_items.get(l).getAsJsonObject().get("item_id").getAsString();
                                        String item_name = array_items.get(l).getAsJsonObject().get("item_name").getAsString();
                                        String is_additional_charges = array_items.get(l).getAsJsonObject().get("is_additional_charges").getAsString();
                                        String price_per_item = !array_items.get(l).getAsJsonObject().get("price_per_item").isJsonNull()
                                                ? array_items.get(l).getAsJsonObject().get("price_per_item").getAsString() : null;

                                        fooditemList.add( new  FoodMenuAddItemModel(menu_id,menu_desc, course_id,course_title, item_id, item_name, is_additional_charges,price_per_item,max_item_selectn_allowed,
                                                price_per_plate,is_group_size ,group_size_from ,group_size_to, false));
                                    }
                                    break;
                                }
                            }

                        }
                        mAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();



                } else {

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
                    alertbox.setMessage("No DAta Found.");
                    alertbox.setTitle("Sorry ! ");
                    alertbox.setIcon(R.mipmap.ic_launcher);

                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    }


    String response_items;
    public class GetSelectedItem extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
        }

        @Override
        protected String doInBackground(Object... parametros) {
            try {

                if(getIntent().hasExtra("cart_ID")) {
                    String response = post(Const.SERVER_URL_API + "cart_catering_item_selected?cart_id=" + getIntent().getStringExtra("cart_ID") + "&menu_id=" + str_menu_id + "&course_id=" + str_course_id, "", "get");
                    Log.d("URL items====", Const.SERVER_URL_API + "cart_catering_item_selected?cart_id=" + getIntent().getStringExtra("cart_ID") + "&menu_id=" + str_menu_id + "&course_id=" + str_course_id);
                    response_items = response;
                }else if(str_cart_ID_SP_Main!=null){
                    String response = post(Const.SERVER_URL_API + "cart_catering_item_selected?cart_id=" + Integer.parseInt(str_cart_ID_SP_Main) + "&menu_id=" + str_menu_id + "&course_id=" + str_course_id, "", "get");
                    Log.d("URL items====", Const.SERVER_URL_API + "cart_catering_item_selected?cart_id=" + Integer.parseInt(str_cart_ID_SP_Main) + "&menu_id=" + str_menu_id + "&course_id=" + str_course_id);
                    response_items = response;
                }/*else{
                    String response = post(Const.SERVER_URL_API + "cart_catering_item_selected?cart_id=" + Integer.parseInt(str_cart_ID_SP_Main) + "&menu_id=" + str_menu_id + "&course_id=" + str_course_id, "", "get");
                    Log.d("URL items====", Const.SERVER_URL_API + "cart_catering_item_selected?cart_id=" + Integer.parseInt(str_cart_ID_SP_Main) + "&menu_id=" + str_menu_id + "&course_id=" + str_course_id);
                    response_items = response;
                }*/

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response_items;
        }
        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            try {
                JSONObject obj = new JSONObject(response_items);
                Log.d("seleted items===",response_items);
                response_string = obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if (response_string.equals("success")) {

                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(response_items).getAsJsonObject();

                    selected_items_array = !rootObj.getAsJsonObject("message").getAsJsonArray("items").isJsonNull()
                            ? rootObj.getAsJsonObject("message").getAsJsonArray("items") : null;


                } else {
                    String message = obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(ServiceProviderFoodAddItem.this.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
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
                    alertbox.setMessage(message);
                    alertbox.setTitle("Sorry ! ");
                    alertbox.setIcon(R.mipmap.ic_launcher);

                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    String response_string;
    private class AddCartCateringFood extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog2;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //Log.d("pre execute", "Pre execute Done");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog2 = ProgressDialog.show(ServiceProviderFoodAddItem.this, "", "Adding to cart...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");

              try {
                JSONObject req = new JSONObject();

                  if(getIntent().hasExtra("cart_ID")){
                      req.put("cart_id",getIntent().getStringExtra("cart_ID"));
                  }else{
                      req.put("cart_id",Integer.parseInt(str_cart_ID_SP_Main));
                  }
                req.put("menu_id",menu_id);
                req.put("menu_desc",menu_desc);
                req.put("price_per_plate",price_per_plate);
                req.put("guest_count",Integer.parseInt(guest_count));
                req.put("total_food_price",int_total_food_price);
                req.put("course_id",str_course_id);
                req.put("course_title",str_title);

                req.put("items",cart_cateringfood_items_array);

                Log.d("Req Json======", req.toString());


                String response = post(Const.SERVER_URL_API +"cart_catering_menu", req.toString(),"put");
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
        protected void onPostExecute(String result)
        { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            super.onPostExecute(result);
        }
            //System.out.println("OnpostExecute----done-------");
            progressDialog2.dismiss();
            Log.i("RESPONSE=venue catfood", response_string);

            if (response_string == null || response_string.equals("")) {

                progressDialog2.dismiss();
                Toast.makeText(ServiceProviderFoodAddItem.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject obj = new JSONObject(response_string);

                response_string=obj.getString("status");

                if(response_string.equals("success")){
                    Snackbar snackbar = Snackbar
                            .make(ServiceProviderFoodAddItem.this.findViewById(android.R.id.content), "Item Added to cart.", Snackbar.LENGTH_LONG);
                            /*.setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });*/
                    // Changing message text color
                    snackbar.setActionTextColor(Color.BLUE);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();


                    Toast.makeText(ServiceProviderFoodAddItem.this,"Item added to cart",Toast.LENGTH_LONG).show();

                    onBackPressed();

                } else{
                    String message=obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(ServiceProviderFoodAddItem.this.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
                            /*.setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });*/
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

        @Override
        protected void onCancelled() {
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
}
