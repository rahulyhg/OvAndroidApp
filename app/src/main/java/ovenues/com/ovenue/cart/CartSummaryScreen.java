package ovenues.com.ovenue.cart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import movile.com.creditcardguide.APICall;
import movile.com.creditcardguide.util.FormatUtils;
import ovenues.com.ovenue.FinalBookingConfirmation;
import ovenues.com.ovenue.Login;
import ovenues.com.ovenue.MainNavigationScreen;
import ovenues.com.ovenue.R;
import ovenues.com.ovenue.cart.CartSummaryServicesStickyList.CartSummaryServiceListAdapter;
import ovenues.com.ovenue.cart.CartSummaryServicesStickyList.HeaderItemCartSummaryServiceList;
import ovenues.com.ovenue.cart.CartSummaryServicesStickyList.TopSnappedStickyLayoutManagerSummaryServiceList;
import ovenues.com.ovenue.CreditCard.CreditCardFragmentActivity;
import ovenues.com.ovenue.utils.Utils;
import ovenues.com.ovenue.modelpojo.Cart.CartSummaryServiceListModel;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.utils.APICall.post;
import static ovenues.com.ovenue.utils.Utils.convertDateStringToString;

public class CartSummaryScreen extends AppCompatActivity {


    public JsonObject coupon_jsonObj = null;
    boolean loading_data=false;
    private int visibleThreshold = 5;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;



    SharedPreferences sharepref;
    TableLayout tl_cart_venue_cart_items;
    TableRow tr_head;

    TableLayout tl_cart_catering_cart_items;
    TextView cateringName,MaincateringTotal;
    TableRow tr_head_catering;
    RecyclerView mRecyclerView;

    TextView venueName,MainvenueTotal,tv_whole_Cart_total;

    double total_venue=0,total_catering=0,total_service=0,final_total=0;
   // CardView cv_venue,cv_catering ;
    LinearLayout ll_venues_items,ll_catering_items;
    NestedScrollView sv_cart_category;

    CartSummaryServiceListAdapter mAdapterServices ;
    ArrayList<CartSummaryServiceListModel> cartSummaryServiceList;


    String discount_amount="",str_coupon_code;

    EditText et_coupnCode;
    Button calc_clear_txt_Prise;
    private Button btPay;
    private TextInputLayout til_coupnhint;
    private Double valueTotal = new Double(0);
    private TextView txtValue,txtValueCart,txtValueDiscount;
    LinearLayout ll_bottomview;
    CardView cv_checkout;
    boolean view_chekout=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_category_screen);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().getThemedContext().setTheme(R.style.AppTheme_PopupOverlay);
        getSupportActionBar().setTitle("Cart Details");
        getWindow().setStatusBarColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimaryDark));

        sv_cart_category= (NestedScrollView)this.findViewById(R.id.sv_cart_category);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        ll_bottomview=(LinearLayout) this.findViewById(R.id.ll_bottomview);
        cv_checkout=(CardView)this.findViewById(R.id.cv_checkout);
        cv_checkout.setVisibility(View.GONE);
        tv_whole_Cart_total=(TextView)this.findViewById(R.id.tv_whole_Cart_total);
        tv_whole_Cart_total.setText("Check Out");

        tv_whole_Cart_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(view_chekout==false){
                    view_chekout=true;
                    cv_checkout.setVisibility(View.VISIBLE);
                    tv_whole_Cart_total.setText("Continue Shopping");
                }else{
                    view_chekout=false;
                    cv_checkout.setVisibility(View.GONE);
                    tv_whole_Cart_total.setText("Check Out");
                }
            }
        });



        txtValueCart = (TextView) this.findViewById(movile.com.creditcardguide.R.id.frg_input_card_txt_value_cart);
        txtValueDiscount = (TextView) this.findViewById(movile.com.creditcardguide.R.id.frg_input_card_txt_value_discount);
        txtValue = (TextView) this.findViewById(movile.com.creditcardguide.R.id.frg_input_card_txt_value);
        til_coupnhint = (TextInputLayout)this.findViewById(movile.com.creditcardguide.R.id.til_coupnhint);
        et_coupnCode = (EditText)this.findViewById(movile.com.creditcardguide.R.id.et_coupnCode);
        calc_clear_txt_Prise =(Button)this.findViewById(movile.com.creditcardguide.R.id.calc_clear_txt_Prise);
        calc_clear_txt_Prise.setVisibility(View.GONE);


        et_coupnCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()<1){
                    et_coupnCode.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_coupnCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (et_coupnCode.getText().length()>0 && actionId == EditorInfo.IME_ACTION_DONE) {
                    str_coupon_code= et_coupnCode.getText().toString();
                    et_coupnCode.performClick();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                    new GetCoupondiscount().execute();
                    return true;
                } if (et_coupnCode.getText().length()>0 && actionId == EditorInfo.IME_FLAG_NO_ENTER_ACTION) {
                    str_coupon_code= et_coupnCode.getText().toString();
                    et_coupnCode.performClick();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                    new GetCoupondiscount().execute();
                    return true;
                }
                return false;
            }
        });

        calc_clear_txt_Prise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new GetRemoveCoupon().execute();

            }
        });

        btPay = (Button) this.findViewById(movile.com.creditcardguide.R.id.frg_input_card_bt_pay);
        btPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sharepref.getString(Const.PREF_USER_ID, "").equalsIgnoreCase("0")) {
                    Toast.makeText(CartSummaryScreen.this, "Please Login to  Continue.", Toast.LENGTH_LONG).show();
                    new  Logout().execute();
                } else {
                    if (final_total > 0) {
                        if(Double.parseDouble(txtValue.getText().toString())>0) {
                            startActivity(new Intent(CartSummaryScreen.this, CreditCardFragmentActivity.class)
                                    .putExtra("billAmount", Double.toString(final_total))
                                    .putExtra("coupon_array", coupon_jsonObj != null
                                            ? coupon_jsonObj.toString() : ""));
                        }else{
                            new FinalOrderBookingAPI().execute();
                        }
                    } else {
                        Toast.makeText(CartSummaryScreen.this, "Cart is Empty.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });



        tl_cart_venue_cart_items=(TableLayout)this.findViewById(R.id.tl_cart_venue_cart_items);

        tl_cart_catering_cart_items=(TableLayout)this.findViewById(R.id.tl_cart_catering_cart_items);


        /*cv_venue = (CardView)this.findViewById(R.id.cv_venue);
        cv_catering = (CardView)this.findViewById(R.id.cv_catering);*/

        ll_venues_items = (LinearLayout)this.findViewById(R.id.ll_venues_items);
        ll_catering_items = (LinearLayout)this.findViewById(R.id.ll_catering_items);

        new GetCartItems().execute();

        tr_head = new TableRow(this);
        tr_head.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        TableRow.LayoutParams tl_lp_venue = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tl_lp_venue.setMargins(25,0,25,0);
        tr_head .setLayoutParams(tl_lp_venue);
        tr_head.setPadding(15,15,15,15);


        /** Creating a TextView to add to the row **/
        venueName = new TextView(CartSummaryScreen.this);
        venueName.setTypeface(Utils.getFont(this,100));
        venueName.setText("Venues");
        venueName.setTextSize(15);
        venueName .setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT,2.0f));
        venueName.setTextColor(getResources().getColor(R.color.white));
        venueName.setPadding(10, 10, 10, 10);
        venueName.setGravity(Gravity.LEFT);
        tr_head.addView(venueName);  // Adding textView to tablerow.

        /** Creating another textview **/
        MainvenueTotal = new TextView(CartSummaryScreen.this);
        MainvenueTotal.setTypeface(Utils.getFont(this,100));
        MainvenueTotal.setGravity(Gravity.RIGHT);
        MainvenueTotal.setTextSize(15);
        MainvenueTotal .setLayoutParams( new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT,2.0f));
        MainvenueTotal.setTextColor(getResources().getColor(R.color.white));
        MainvenueTotal.setPadding(10, 10, 15, 10);
       
        tr_head.addView(MainvenueTotal); // Adding textView to tablerow.

        // Add the TableRow to the TableLayout
        tl_cart_venue_cart_items.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));


        tr_head_catering = new TableRow(this);
        tr_head_catering.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        TableRow.LayoutParams tl_lp_catering = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tl_lp_catering.setMargins(25,0,25,0);
        tr_head_catering .setLayoutParams(tl_lp_catering);
        tr_head_catering.setPadding(15,15,15,15);


        /** Creating a TextView to add to the row **/
        cateringName = new TextView(CartSummaryScreen.this);
        cateringName.setTypeface(Utils.getFont(this,100));
        cateringName.setText("Catering");
        cateringName.setTextSize(15);
        cateringName .setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT,2.0f));
        cateringName.setTextColor(getResources().getColor(R.color.white));
        cateringName.setPadding(10, 10, 10, 10);
        cateringName.setGravity(Gravity.LEFT);
        tr_head_catering.addView(cateringName);  // Adding textView to tablerow.

        /** Creating another textview **/
        MaincateringTotal = new TextView(CartSummaryScreen.this);
        MaincateringTotal.setTypeface(Utils.getFont(this,100));
        MaincateringTotal.setGravity(Gravity.RIGHT);
        MaincateringTotal.setTextSize(15);
        MaincateringTotal .setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT,2.0f));
        MaincateringTotal.setTextColor(getResources().getColor(R.color.white));
        MaincateringTotal.setPadding(10, 10, 15,10);

        tr_head_catering.addView(MaincateringTotal); // Adding textView to tablerow.

        // Add the TableRow to the TableLayout
        tl_cart_catering_cart_items.addView(tr_head_catering, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        cartSummaryServiceList=new ArrayList<CartSummaryServiceListModel>();
        mAdapterServices = new CartSummaryServiceListAdapter(CartSummaryScreen.this,cartSummaryServiceList);

        mRecyclerView = (RecyclerView)this.findViewById(R.id.recyclerview_cart_srevices_list);
        StickyLayoutManager layoutManager = new TopSnappedStickyLayoutManagerSummaryServiceList(CartSummaryScreen.this, mAdapterServices);
        layoutManager.elevateHeaders(true);
        // Default elevation of 5dp
        // You can also specify a specific dp for elevation
        layoutManager.elevateHeaders(5);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapterServices);
        mRecyclerView.setNestedScrollingEnabled(false);

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


        final String TAG = "scroll listner";
        sv_cart_category.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                                               @Override
                                               public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                                                   if (scrollY > oldScrollY) {
                                                       //Log.i(TAG, "Scroll DOWN");
                                                       //tv_whole_Cart_total.animate().translationY(tv_whole_Cart_total.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                                                       ll_bottomview.animate().translationY(ll_bottomview.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                                                       controlsVisible = false;
                                                       scrolledDistance = 0;
                                                   }
                                                   if (scrollY < oldScrollY) {
                                                       //Log.i(TAG, "Scroll UP");
                                                       //tv_whole_Cart_total.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                                                       ll_bottomview.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                                                       controlsVisible = true;
                                                       scrolledDistance = 0;
                                                   }

                                                   if (scrollY == 0) {
                                                       //Log.i(TAG, "TOP SCROLL");
                                                   }

                                                   /*if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                                                       Log.i(TAG, "BOTTOM SCROLL");
                                                       if (viewGroup1.getChildAt(viewGroup1.getChildCount() - 1) instanceof RecyclerView){
                                                           //add code here }
                                                       }

                                                       }*/
                                                   }
                                               });

    }

    public void setTotalValue(Double net_totalValue,Double discount,Double cart_total) {
        valueTotal = net_totalValue;

        txtValueCart.setText(String.format("%s %s","", FormatUtils.getCurrencyFormat().format(cart_total)));
        txtValueDiscount.setText(String.format("%s %s", "-",FormatUtils.getCurrencyFormat().format(discount)));
        txtValue.setText(String.format("%s %s","", FormatUtils.getCurrencyFormat().format(valueTotal)));
    }

    String res;
    class GetCartItems extends AsyncTask<Object, Void, String> {
        protected ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //pg_bar.setVisibility(View.VISIBLE);

            progressDialog = ProgressDialog.show(CartSummaryScreen.this, "Loading", "Please wait...", true, false);
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
            String cart_id,flag ,venue_id,booking_date,booking_time_from ,booking_time_to,number_of_guests ,booking_rent,venue_name,total_amount,total_amount_catering="";
            Log.d("Cart_Details",res);

            try{JSONObject obj = new JSONObject(res);

                response_string=obj.getString("status");

                if(response_string.equals("success")){

                    JsonParser parser = new JsonParser();

                    JsonObject rootObj = parser.parse(res).getAsJsonObject();


                    if(rootObj.getAsJsonObject("message").has("venue_details") && rootObj.getAsJsonObject("message").getAsJsonArray("venue_details").size()>0) {
                        JsonArray venue_price_Obj_array = rootObj.getAsJsonObject("message").getAsJsonArray("venue_details");
                        //cartItemList.clear();
                        for (int a = 0; a < venue_price_Obj_array.size(); a++) {

                            cart_id = !venue_price_Obj_array.get(a).getAsJsonObject().get("cart_id").isJsonNull()
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

                            if(total_amount!=null){
                                total_venue = total_venue + Double.parseDouble(total_amount);
                            }

                            /** Create a TableRow dynamically **/
                            TableRow tr_head = new TableRow(CartSummaryScreen.this);
                            TableRow.LayoutParams tl_lp_venue = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            tl_lp_venue.setMargins(35,20,35,20);
                            tr_head .setLayoutParams(tl_lp_venue);
                            tr_head.setPadding(50,15,50,15);

                            /** Creating a TextView to add to the row **/
                            final TextView venueName_dynamic = new TextView(CartSummaryScreen.this);
                            venueName_dynamic.setText(venue_name);
                            venueName_dynamic.setTextSize(15);
                            venueName_dynamic.setId(Integer.parseInt(cart_id));
                            venueName_dynamic.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                            venueName_dynamic.setGravity(Gravity.LEFT);
                            venueName_dynamic.setTypeface(Utils.getFont(CartSummaryScreen.this, 100));
                            venueName_dynamic.setTextColor(getResources().getColor(R.color.md_blue_grey_600));

                            tr_head.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(CartSummaryScreen.this,CartVenuesNCateringDetails.class)
                                            .putExtra("cart_id",Integer.toString(venueName_dynamic.getId()))
                                            .putExtra("detailView","1"));
                                   // Toast.makeText(CartSummaryScreen.this,"get venueName_dynamic is =="+venueName_dynamic.getId(),Toast.LENGTH_LONG).show();
                                    Log.d("venueName_dynamic==",""+venueName_dynamic.getId());
                                }
                            });


                            tr_head.addView(venueName_dynamic);  // Adding textView to tablerow.

                            /** Creating another textview **/
                            TextView venueTotal = new TextView(CartSummaryScreen.this);
                            venueTotal.setTypeface(Utils.getFont(CartSummaryScreen.this, 100));
                            venueTotal.setTextSize(15);
                            venueTotal.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                            venueTotal.setGravity(Gravity.RIGHT);
                            venueTotal.setText("$ " + Const.GLOBAL_FORMATTER.format(Double.parseDouble(total_amount)));
                            venueTotal.setTextColor(getResources().getColor(R.color.md_blue_grey_600));
                            tr_head.addView(venueTotal);


                            // Add the TableRow to the TableLayout
                            tl_cart_venue_cart_items.addView(tr_head, new TableLayout.LayoutParams(
                                    TableLayout.LayoutParams.MATCH_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));



                            TableRow tr_head_calander = new TableRow(CartSummaryScreen.this);
                            tr_head_calander .setLayoutParams(tl_lp_venue);
                            tr_head_calander.setPadding(50,0,50,0);

                            final TextView venuesEventTime = new TextView(CartSummaryScreen.this);
                            venuesEventTime.setText(convertDateStringToString(booking_date, "yyyy-MM-dd", "MM-dd-yyyy")
                                    +"  "+ convertDateStringToString(booking_time_from, "yyyy-MM-dd HH:mm:ss", "hh:mm aa")+"-"+convertDateStringToString(booking_time_to, "yyyy-MM-dd HH:mm:ss", "hh:mm aa"));
                            venuesEventTime.setTextSize(12);
                            venuesEventTime.setId(Integer.parseInt(cart_id));
                            venuesEventTime.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                            venuesEventTime.setGravity(Gravity.LEFT);
                            venuesEventTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calander_grey, 0, 0, 0);
                            venuesEventTime.setCompoundDrawablePadding(15);
                            venuesEventTime.setTypeface(Utils.getFont(CartSummaryScreen.this, 100));
                            venuesEventTime.setTextColor(getResources().getColor(R.color.md_blue_grey_600));

                            tr_head_calander.addView(venuesEventTime);

                            tl_cart_venue_cart_items.addView(tr_head_calander, new TableLayout.LayoutParams(
                                    TableLayout.LayoutParams.MATCH_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));

                            TableRow tr_headView = new TableRow(CartSummaryScreen.this);
                            TableRow.LayoutParams view_lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                            view_lp.setMargins(35,20,35,20);
                            tr_headView .setLayoutParams(view_lp);
                            tr_headView.setPadding(50,15,50,15);

                            View v1 = new View(CartSummaryScreen.this);
                            v1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
                            v1.setBackgroundColor(getResources().getColor(R.color.md_grey_700,getTheme()));
                            tr_headView.addView(v1);
                            tl_cart_venue_cart_items.addView(tr_headView,new TableLayout.LayoutParams(
                                    TableLayout.LayoutParams.MATCH_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));

                              View new_view = new View(getBaseContext());
                                    new_view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 3));
                                    new_view.setBackgroundResource(R.drawable.line_divider_view);

                                    TableLayout.LayoutParams lp=  new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                            TableLayout.LayoutParams.WRAP_CONTENT);
                                    lp.setMargins(0,5,0,5);

                            tl_cart_venue_cart_items.addView(new_view,lp );

                            Log.d("pricing_plan", "done");
                        }
                        MainvenueTotal.setText("$ "+ Const.GLOBAL_FORMATTER.format(total_venue));
                    }else{
                       // cv_venue.setVisibility(View.GONE);
                        ll_venues_items.setVisibility(View.GONE);
                    }
                    //=====================############################################################===================================


                    if(rootObj.getAsJsonObject("message").has("service_provider_details")) {

                        JsonObject service_provider_details_Obj= rootObj.getAsJsonObject("message").getAsJsonObject("service_provider_details");

                        for (Map.Entry<String, JsonElement> entry : service_provider_details_Obj.entrySet()) {

                            //this gets the dynamic keys
                            String  service_name_AsKey = entry.getKey();
                            //Log.d("service_name_AsKey",""+service_name_AsKey);

                            //you can get any thing now json element,array,object according to json.

                            JsonArray service_provider_details_Obj_array = entry.getValue().getAsJsonArray();
                            //Log.d("jsonArrayDates",""+service_provider_details_Obj_array.toString());

                            //cartItemList.clear();
                            for (int a = 0; a < service_provider_details_Obj_array.size(); a++) {

                                String cart_id_service = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("cart_id").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("cart_id").getAsString() : null;
                                String user_id = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("user_id").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("user_id").getAsString() : null;
                                String flag_srvice = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("flag").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("flag").getAsString() : null;
                                String service_title = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("service_title").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("service_title").getAsString() : null;
                                String service_provider_id = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("service_provider_id").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("service_provider_id").getAsString() : null;
                                String provider_name = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("provider_name").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("provider_name").getAsString() : null;
                                String booking_time = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("booking_time").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("booking_time").getAsString() : null;
                                ;
                                String booking_date_service = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("booking_date").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("booking_date").getAsString() : null;
                                String service_hours = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("service_hours").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("service_hours").getAsString() : null;
                                String number_of_guests_service = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("number_of_guests").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("number_of_guests").getAsString() : null;

                                String quantity = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("quantity").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("quantity").getAsString() : null;
                                String total_amount_service = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("total_amount").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("total_amount").getAsString() : null;
                                String extra_guests = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("extra_guests").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("extra_guests").getAsString() : null;
                                String extra_minutes = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("extra_minutes").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("extra_minutes").getAsString() : null;
                                String note = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("note").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("note").getAsString() : null;

                                String is_delivery = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("is_delivery").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("is_delivery").getAsString() : null;
                                String is_pickup = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("is_pickup").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("is_pickup").getAsString() : null;
                                String is_onsite_service = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("is_onsite_service").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("is_onsite_service").getAsString() : null;
                                String delivery_address = !service_provider_details_Obj_array.get(a).getAsJsonObject().get("delivery_address").isJsonNull()
                                        ? service_provider_details_Obj_array.get(a).getAsJsonObject().get("delivery_address").getAsString() : null;

                                total_service = total_service + Double.parseDouble(total_amount_service);

                                if(a==0){
                                    double total_specific_srvice=0;
                                    for(int start=0;start<service_provider_details_Obj_array.size();start++){
                                        String total_Ser= !service_provider_details_Obj_array.get(start).getAsJsonObject().get("total_amount").isJsonNull()
                                                ? service_provider_details_Obj_array.get(start).getAsJsonObject().get("total_amount").getAsString() : "0";

                                        total_specific_srvice = total_specific_srvice + Double.parseDouble(total_Ser);
                                    }
                                    cartSummaryServiceList.add(new HeaderItemCartSummaryServiceList( cart_id_service,user_id,flag_srvice ,service_name_AsKey,service_provider_id ,
                                            service_name_AsKey,booking_time ,booking_date_service,service_hours,number_of_guests_service,quantity ,total_amount_service,extra_guests
                                            ,extra_minutes,note,is_delivery,is_pickup,is_onsite_service,Double.toString(total_specific_srvice),0));//==here i am passing total as Address parametter=====
                                }
                                cartSummaryServiceList.add(new CartSummaryServiceListModel( cart_id_service,user_id,flag_srvice ,service_title,service_provider_id ,
                                        provider_name,booking_time ,booking_date_service,service_hours,number_of_guests_service,quantity ,total_amount_service,extra_guests
                                        ,extra_minutes,note,is_delivery,is_pickup,is_onsite_service,delivery_address,1));
                            }
                            mAdapterServices.notifyDataSetChanged();
                        }
                    }else{
                        //cv_services.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.GONE);
                    }
                    //=====================############################################################===================================


                    if(rootObj.getAsJsonObject("message").has("catering_service_provider_details") && rootObj.getAsJsonObject("message").getAsJsonArray("catering_service_provider_details").size()>0) {

                        JsonArray catering_service_provider_details_Obj_array = rootObj.getAsJsonObject("message").getAsJsonArray("catering_service_provider_details");
                        //cartItemList.clear();
                        for (int a = 0; a < catering_service_provider_details_Obj_array.size(); a++) {

                            String cart_id_catering = !catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("cart_id").isJsonNull()
                                    ? catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("cart_id").getAsString() : null;
                            String user_id = !catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("user_id").isJsonNull()
                                    ? catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("user_id").getAsString() : null;
                            String flag_catering = !catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("flag").isJsonNull()
                                    ? catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("flag").getAsString() : null;
                            String service_provider_id = !catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("service_provider_id").isJsonNull()
                                    ? catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("service_provider_id").getAsString() : null;
                            String provider_name = !catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("provider_name").isJsonNull()
                                    ? catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("provider_name").getAsString() : null;
                            String booking_date_catering = !catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("booking_date").isJsonNull()
                                    ? catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("booking_date").getAsString() : null;
                            String booking_time = !catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("booking_time").isJsonNull()
                                    ? catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("booking_time").getAsString() : null;
                            ;
                            String number_of_guests_catering = !catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("number_of_guests").isJsonNull()
                                    ? catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("number_of_guests").getAsString() : null;
                            String quantity = !catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("quantity").isJsonNull()
                                    ? catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("quantity").getAsString() : null;
                            total_amount_catering =!catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("total_amount").isJsonNull()
                                    ? catering_service_provider_details_Obj_array.get(a).getAsJsonObject().get("total_amount").getAsString() : null;


                            /** Create a TableRow dynamically **/
                            TableRow tr_head_catering = new TableRow(CartSummaryScreen.this);
                            TableRow.LayoutParams tl_lp_catering = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            tl_lp_catering.setMargins(35,0,35,0);
                            tr_head_catering .setLayoutParams(tl_lp_catering);
                            tr_head_catering.setPadding(50,15,50,15);

                            /** Creating a TextView to add to the row **/
                            final TextView cateringName_dynamic = new TextView(CartSummaryScreen.this);
                            cateringName_dynamic.setText(provider_name);
                            cateringName_dynamic.setTextSize(15);
                            cateringName_dynamic.setId(Integer.parseInt(cart_id_catering));
                            cateringName_dynamic.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                            cateringName_dynamic.setGravity(Gravity.LEFT);
                            cateringName_dynamic.setTypeface(Utils.getFont(CartSummaryScreen.this, 100));
                            cateringName_dynamic.setTextColor(getResources().getColor(R.color.md_blue_grey_600));

                            tr_head_catering.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(CartSummaryScreen.this,CartVenuesNCateringDetails.class)
                                            .putExtra("cart_id",Integer.toString(cateringName_dynamic.getId()))
                                            .putExtra("detailView","2"));
                                   // Toast.makeText(CartSummaryScreen.this,"get venueName_dynamic is =="+cateringName_dynamic.getId(),Toast.LENGTH_LONG).show();
                                    Log.d("venueName_dynamic==",""+cateringName_dynamic.getId());
                                }
                            });
                            tr_head_catering.addView(cateringName_dynamic);  // Adding textView to tablerow.

                            /** Creating another textview **/
                            TextView cateringTotal = new TextView(CartSummaryScreen.this);
                            cateringTotal.setTypeface(Utils.getFont(CartSummaryScreen.this, 100));
                            cateringTotal.setTextSize(15);
                            cateringTotal.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.8f));
                            cateringTotal.setGravity(Gravity.RIGHT);
                            cateringTotal.setText("$ " + Const.GLOBAL_FORMATTER.format(Double.parseDouble(total_amount_catering)));
                            cateringTotal.setTextColor(getResources().getColor(R.color.md_blue_grey_600));

                            tr_head_catering.addView(cateringTotal);

                            // Add the TableRow to the TableLayout
                            tl_cart_catering_cart_items.addView(tr_head_catering, new TableLayout.LayoutParams(
                                    TableLayout.LayoutParams.MATCH_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));

                            TableRow tr_head_calander = new TableRow(CartSummaryScreen.this);
                            tr_head_calander .setLayoutParams(tl_lp_catering);
                            tr_head_calander.setPadding(50,0,50,0);

                            final TextView serviceEventTime = new TextView(CartSummaryScreen.this);
                            serviceEventTime.setText(convertDateStringToString(booking_date_catering, "yyyy-MM-dd", "yyyy-MM-dd")
                                    +"  "+ convertDateStringToString(booking_time, "yyyy-MM-dd HH:mm:ss", "hh:mm aa"));
                          serviceEventTime.setTextSize(12);
                          serviceEventTime.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                          serviceEventTime.setGravity(Gravity.LEFT);
                          serviceEventTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calander_grey, 0, 0, 0);
                          serviceEventTime.setCompoundDrawablePadding(15);
                          serviceEventTime.setTypeface(Utils.getFont(CartSummaryScreen.this, 100));
                          serviceEventTime.setTextColor(getResources().getColor(R.color.md_blue_grey_600));

                            tr_head_calander.addView(serviceEventTime);

                            tl_cart_catering_cart_items.addView(tr_head_calander, new TableLayout.LayoutParams(
                                    TableLayout.LayoutParams.MATCH_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));
                            // Adding textView to tablerow.

                            View new_view = new View(getBaseContext());
                            new_view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 3));
                            new_view.setBackgroundResource(R.drawable.line_divider_view);

                            TableLayout.LayoutParams lp=  new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT);
                            lp.setMargins(0,5,0,5);

                            tl_cart_catering_cart_items.addView(new_view,lp );


                            total_catering = total_catering + Double.parseDouble(total_amount_catering);
                        }
                        MaincateringTotal.setText("$ "+ Const.GLOBAL_FORMATTER.format(total_catering));
                    }else{
                        //cv_catering.setVisibility(View.GONE);
                        ll_catering_items.setVisibility(View.GONE);
                    }

                    final_total = total_venue + total_catering +total_service;

                    if(rootObj.getAsJsonObject("message").has("coupon")
                            && !rootObj.getAsJsonObject("message").get("coupon").isJsonNull()) {
                        coupon_jsonObj = rootObj.getAsJsonObject("message").get("coupon").getAsJsonObject();

                        if(coupon_jsonObj.size()>0 && coupon_jsonObj!=null){


                            String coupon_id = coupon_jsonObj.get("coupon_id").getAsString();
                            String coupon_code = coupon_jsonObj.get("coupon_code").getAsString();
                            String discount_amount = coupon_jsonObj.get("discount_amount").getAsString();
                            String msg = coupon_jsonObj.has("coupon_msg")?coupon_jsonObj.get("coupon_msg").getAsString():"";

                            if(Double.parseDouble(discount_amount) > final_total){
                                //til_coupnhint.setHint(msg);
                                setTotalValue((double)0.0,Double.parseDouble(discount_amount),final_total);
                            }else{
                                setTotalValue((double)(final_total- Double.parseDouble(discount_amount)),Double.parseDouble(discount_amount),final_total);
                            }
                            //til_coupnhint.setHint(msg);
                            setTotalValue((double)(final_total- Double.parseDouble(discount_amount)),Double.parseDouble(discount_amount),final_total);

                            til_coupnhint.setHint(msg+"Discount Amount is : $"+discount_amount);
                            et_coupnCode.setText(coupon_code);
                            et_coupnCode.setEnabled(false);
                            calc_clear_txt_Prise.setVisibility(View.VISIBLE);
                            //savedCard.setCoupon(coupon_code);

                        }else{

                        }
                    }else{
                        setTotalValue(final_total,0.0,final_total);
                    }
                    JsonObject cart_total_items = rootObj.getAsJsonObject("message").getAsJsonObject("cart_total_items");
                    Log.d("cart_total_items",cart_total_items.toString());
                    int venue=0,service=0,catering=0;
                    venue = Integer.parseInt(cart_total_items.get("venues").getAsString());
                    service = Integer.parseInt(cart_total_items.get("services").getAsString());
                    catering = Integer.parseInt(cart_total_items.get("catering_services").getAsString());

                    if(ll_venues_items.getVisibility()==View.GONE && ll_catering_items.getVisibility()==View.GONE && mRecyclerView.getVisibility()==View.GONE){

                        android.support.v7.app.AlertDialog.Builder builder =
                                new android.support.v7.app.AlertDialog.Builder(CartSummaryScreen.this, R.style.AppCompatAlertDialogStyle);
                        builder.setTitle("Cart is Empty !");
                        builder.setCancelable(false);
                        builder.setMessage("Sorry! its looks you do not have any item in cart.");
                        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(CartSummaryScreen.this, MainNavigationScreen.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                            }
                        });
                        builder.show();

                    }else if(venue==0 && service==0 && catering==0 ){
                        android.support.v7.app.AlertDialog.Builder builder =
                                new android.support.v7.app.AlertDialog.Builder(CartSummaryScreen.this, R.style.AppCompatAlertDialogStyle);
                        builder.setTitle("Cart is Empty !");
                        builder.setCancelable(false);
                        builder.setMessage("Sorry! its looks you do not have any item in cart.");
                        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(CartSummaryScreen.this, MainNavigationScreen.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                            }
                        });
                        builder.show();
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

    class Logout extends AsyncTask<Object, Void, String> {
        private final static String TAG = "EntryActivity.EfetuaEntry";
        protected ProgressDialog progressDialog;
        String item_ingre;
        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(CartSummaryScreen.this, "Loading", "Please Wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }
        @Override
        protected String doInBackground(Object... parametros) {
            // System.out.println("On do in back ground----done-------");
            //Log.d("post execute", "Executando doInBackground   ingredients");
            try {

                JSONObject req = new JSONObject();
                req.put("user_id",sharepref.getString(Const.PREF_USER_ID,""));

                Log.d("REq Json======", req.toString());

                String response = post(Const.SERVER_URL_API +"/mob_logout", req.toString(),"post");//post("http://54.153.127.215/api/login", req.toString());
                Log.d("REsponce Json====",response);
                res = response;
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return res;
        }
        @Override
        protected void onPostExecute(String result) {
            int Total_cart_amount = 0;
            int Total_cart_saving_amount = 0;
            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            progressDialog.dismiss();

            //Toast.makeText(CartSummaryScreen.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
            sharepref.edit().clear().commit();

            Intent intenta = new Intent(getApplicationContext(), Login.class);
            intenta.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intenta);
            finish();
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), "  Thank You.!!!!", Snackbar.LENGTH_LONG);
            // Changing message text color
            snackbar.setActionTextColor(Color.BLUE);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            Toast.makeText(CartSummaryScreen.this, "Sign out Done !", Toast.LENGTH_LONG).show();

            progressDialog.dismiss();
        }
    }

    String res_coupn;
    class GetCoupondiscount extends AsyncTask<Object, Void, String> {

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
              /*  http://ovenues.co.us/api/apply_coupen/coupon_code/testflat/user_id/38*/
                String response = APICall.post(Const.SERVER_URL_API +"apply_coupen/coupon_code/"+str_coupon_code+"/user_id/"+sharepref.getString("PREF_USER_ID",""), "","get");
                Log.d("req Json====",Const.SERVER_URL_API +"apply_coupen/coupon_code/"+str_coupon_code+"/user_id/"+sharepref.getString("PREF_USER_ID",""));
                res_coupn=response;
            } catch (IOException e) {
                e.printStackTrace();
            }


            return res_coupn;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);



            try{JSONObject obj = new JSONObject(res_coupn);
                Log.i("RESPONSE", res_coupn);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    JsonParser parser = new JsonParser();

                    JsonObject rootObj = parser.parse(res_coupn).getAsJsonObject();
                    coupon_jsonObj=rootObj;

                    if(!rootObj.get("message").getAsJsonObject().isJsonNull()){
                        String  msg  = rootObj.getAsJsonObject("message").get("msg").getAsString();
                        til_coupnhint.setHint(msg);
                        discount_amount  = rootObj.getAsJsonObject("message").get("discount_amount").getAsString();
                        setTotalValue((double)(final_total- Double.parseDouble(discount_amount)),Double.parseDouble(discount_amount),final_total);

                        coupon_jsonObj.addProperty("coupon_id","0");
                        coupon_jsonObj.addProperty("discount_amount",rootObj.getAsJsonObject("message").get("discount_amount").getAsString());
                        coupon_jsonObj.addProperty("coupon_code",str_coupon_code);
                        coupon_jsonObj.addProperty("coupon_msg",msg);

                        til_coupnhint.setHint(msg+"Discount Amount is : $"+discount_amount);
                        til_coupnhint.setEnabled(false);
                        calc_clear_txt_Prise.setVisibility(View.VISIBLE);

                        if(Double.parseDouble(discount_amount) > final_total){
                            //til_coupnhint.setHint(msg);
                            setTotalValue((double)0.0,Double.parseDouble(discount_amount),final_total);
                        }else{
                            setTotalValue((double)(final_total- Double.parseDouble(discount_amount)),Double.parseDouble(discount_amount),final_total);
                        }

                    }else {

                    }
                } else if(response_string.equals("fail")){

                    JsonParser parser = new JsonParser();

                    JsonObject rootObj = parser.parse(res_coupn).getAsJsonObject();

                    String  msg  = rootObj.getAsJsonObject().get("message").getAsString();
                    til_coupnhint.setHint(msg);
                    discount_amount  = "0.0";
                    setTotalValue((double)(final_total- Double.parseDouble(discount_amount)),Double.parseDouble(discount_amount),final_total);

                    til_coupnhint.setHint(msg);
                    til_coupnhint.setEnabled(false);

                    calc_clear_txt_Prise.setVisibility(View.VISIBLE);

                } else{
                    /*String message=obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
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
                    snackbar.show();*/
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    class GetRemoveCoupon extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //pg_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Object... parametros) {
            http://ovenues.co.us/api//user_id/38

            try {
              /*  http://ovenues.co.us/api/apply_coupen/coupon_code/testflat/user_id/38*/
                String response = APICall.post(Const.SERVER_URL_API +"remove_coupen/user_id/"+sharepref.getString("PREF_USER_ID",""), "","get");
                // Log.d("req Json====",CONST_API_URL+"remove_coupen/user_id/"+sharepref.getString("PREF_USER_ID",""));
                res_coupn=response;
            } catch (IOException e) {
                e.printStackTrace();
            }


            return res_coupn;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);



            try{JSONObject obj = new JSONObject(res_coupn);
                Log.i("RESPONSE", res_coupn);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){


                    et_coupnCode.setText("");
                    til_coupnhint.setHint("Coupon Code");
                    til_coupnhint.setEnabled(true);
                    calc_clear_txt_Prise.setVisibility(View.GONE);
                    coupon_jsonObj=null;
                    //setTotalValue((double)(valueTotal + Double.parseDouble(discount_amount)),0.0,bill_total);
                    setTotalValue(final_total,0.0,final_total);
                    /*JsonParser parser = new JsonParser();

                    JsonObject rootObj = parser.parse(res_coupn).getAsJsonObject();

                    if(!rootObj.get("message").getAsJsonObject().isJsonNull()){
                        String  msg  = rootObj.getAsJsonObject("message").get("msg").getAsString();
                        til_coupnhint.setHint(msg);
                        discount_amount  = rootObj.getAsJsonObject("message").get("discount_amount").getAsString();
                        setTotalValue((double)(bill_total- Double.parseDouble(discount_amount)));

                        til_coupnhint.setHint(msg+"Discount Amount is : $"+discount_amount);
                        calc_clear_txt_Prise.setVisibility(View.VISIBLE);

                    }else {

                    }*/

                } else{
                    til_coupnhint.setEnabled(true);
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

    String res_call_bookingAPI;
    private class FinalOrderBookingAPI extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //Log.d("pre execute", "Pre execute Done");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(CartSummaryScreen.this, "Loading", "Please wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");
            //Log.d("post execute", "Executando doInBackground   ingredients");
            try {



                JSONObject req = new JSONObject();
                req.put("user_id",sharepref.getString(Const.PREF_USER_ID,null));
                req.put("booking_source","Mobile");
                req.put("booking_payment_details","");

                Log.d("REq Json======", req.toString());

                String response = post(Const.SERVER_URL_API +"booking_all", req.toString(),"put");
                res_call_bookingAPI = response;
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return res_call_bookingAPI;

        }
        @Override
        protected void onPostExecute(String result)
        { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            super.onPostExecute(result);
        }
            //System.out.println("OnpostExecute----done-------");
            progressDialog.dismiss();

            if (res_call_bookingAPI == null || res_call_bookingAPI.equals("")) {

                progressDialog.dismiss();
                Toast.makeText(CartSummaryScreen.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject obj = new JSONObject(res_call_bookingAPI);
                Log.i("RESPONSE pymnt", res_call_bookingAPI);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.

                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    /*String user_obj=obj.getString("message");

                    android.support.v7.app.AlertDialog.Builder builder =
                            new android.support.v7.app.AlertDialog.Builder(CreditCardFragmentActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Successfully Sent !");
                    builder.setMessage("Thank you for making order request online with ovenues.com. Your request has been sent to the vendor and we will mail you the Confirmation soon.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(CreditCardFragmentActivity.this, MainNavigationScreen.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        }
                    });
                    //builder.setNegativeButton("Cancel", null);
                    builder.show();*/

                    startActivity( new Intent(CartSummaryScreen.this, FinalBookingConfirmation.class).
                            putExtra("JSON",res_call_bookingAPI));
                    finish();

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
         /*    case R.id.action_custom_indicator:
                 mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                 break;
             case R.id.action_custom_child_animation:
                 mDemoSlider.setCustomAnimation(new ChildAnimationExample());
                 break;



        }

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
        // super.onBackPressed();
         startActivity(new Intent(CartSummaryScreen.this, MainNavigationScreen.class));
         finish();
     }


}
