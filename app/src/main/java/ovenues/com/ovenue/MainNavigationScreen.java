package ovenues.com.ovenue;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ovenues.com.ovenue.adapter.autocomplete_textviews.VenueNameSearchAutocompleteAdapter;
import ovenues.com.ovenue.bookingdetails.BookingListActivity;
import ovenues.com.ovenue.cart.CartSummaryScreen;
import ovenues.com.ovenue.customviews.CustomTypefaceSpan;
import ovenues.com.ovenue.fragments.ListBusiness;
import ovenues.com.ovenue.fragments.MainHomeFragment;
import ovenues.com.ovenue.fragments.TermsUser;
import ovenues.com.ovenue.modelpojo.autocompleteSearchviewCity.SearchVenueSPCityModel;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.utils.APICall.post;

public class MainNavigationScreen extends AppCompatActivity {

    private static final int MENU_VENUE = Menu.FIRST;
    private static final int MENU_CATERING= Menu.FIRST + 1;
    private static final int MENU_SERVICES = Menu.FIRST + 2;
    private static final int MENU_TOTALAMOUNT = Menu.FIRST + 3;
    private static final int MENU_LOGOUT = Menu.FIRST + 4;

    SharedPreferences sharepref;
    public AutoCompleteTextView gen_search;
    VenueNameSearchAutocompleteAdapter adapter_venue_name_search ;
    ArrayList<SearchVenueSPCityModel> searchcitymodel;
    String str_venue_name=null;

    DrawerLayout drawer;
    private MenuItem activeMenuItem;
    NavigationView navigationView;

    public static TextView tv_name, tv_email;
    ImageView img_userdp;
    String res;
    LinearLayout ll_nav_neader_signin;
    Activity activity;

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "LatoRegular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*sharepref.edit().putString(Const.PREF_CITY_ID,"").apply();
        sharepref.edit().putString(Const.PREF_CITY_NAME,"").apply();*/
        sharepref.edit().putString(Const.PREF_EVENT_TYPE_ID,"").apply();
        sharepref.edit().putString(Const.PREF_VENUE_TYPE_ID,"").apply();
        sharepref.edit().putString(Const.PREF_AMENITIES_ID,"").apply();
        sharepref.edit().putString(Const.PREF_GUEST_COUNT_MIN,"").apply();
        sharepref.edit().putString(Const.PREF_GUEST_COUNT_MAX,"").apply();
        sharepref.edit().putString(Const.PREF_PRICE_MIN,"").apply();
        sharepref.edit().putString(Const.PREF_PRICE_MAX,"").apply();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation_screen);
        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        if(sharepref.getString(Const.PREF_USER_EMAIL,"").equalsIgnoreCase("")){
            startActivity(new Intent(MainNavigationScreen.this,Login.class));
            finish();
        }

        activity = this;

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("");
        //getSupportActionBar().setSubtitle("Be a smart host");
//

        FragmentTransaction tx;
        tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.frame, new MainHomeFragment());
        tx.commit();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle Toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };
        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(Toggle);
        //calling sync state is necessay or else your hamburger icon wont show up
        Toggle.syncState();


        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        View header = navigationView.getHeaderView(0);
        img_userdp=(ImageView)header.findViewById(R.id.img_user);
        ll_nav_neader_signin= (LinearLayout)header.findViewById(R.id.ll_nav_neader_signin);


      /*  try {
            String pro_pic= Const.WEBSITE_PIC_URL + sharepref.getString(Const.PREF_PROFILE_PIC_URL,"");
            Log.d("pro_pic",pro_pic);
            Picasso.with(MainNavigationScreen.this)
                    .load(pro_pic)
                    .placeholder(R.drawable.noimage) // optional
                    .error(R.drawable.noimage)         // optional
                    .memoryPolicy(MemoryPolicy.NO_CACHE )
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(img_userdp);
        }catch (Exception expbitmap){
            expbitmap.printStackTrace();
        }*/

        // new GetVenuesListCity().execute();
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
                startActivity(new Intent(MainNavigationScreen.this,VenueDetailsMainFragment.class)
                        .putExtra("venue_name",adapter_venue_name_search.getItem(position).getName())
                        .putExtra("venue_id",adapter_venue_name_search.getItem(position).getId()));
                gen_search.setText("");

                /*Toast.makeText(MainNavigationScreen.this, adapter_venue_name_search.getItem(position).getId().toString(),Toast.LENGTH_SHORT).show();*/

            }
        });


        tv_name = (TextView) header.findViewById(R.id.tv_name);
        tv_email = (TextView) header.findViewById(R.id.tv_email);

        tv_name.setText(sharepref.getString(Const.PREF_USER_FULL_NAME,""));
        tv_email.setText(sharepref.getString(Const.PREF_USER_EMAIL,""));

        if(tv_name.getText().toString().equalsIgnoreCase("Guest")
                || tv_email.getText().toString().equalsIgnoreCase("guest")){
            ll_nav_neader_signin.setVisibility(View.VISIBLE);
        }else{
            ll_nav_neader_signin.setVisibility(View.GONE);
        }

        TextView tv_login = (TextView)header.findViewById(R.id.tv_login);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainNavigationScreen.this,Login.class));
                finish();
            }
        });
        TextView tv_signup = (TextView)header.findViewById(R.id.tv_signup);
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainNavigationScreen.this,Signup.class));
                finish();
            }
        });

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!sharepref.getString(Const.PREF_USER_ID, "").equalsIgnoreCase("0")) {
                    Intent editprofile = new Intent(MainNavigationScreen.this, EditProfile.class);
                    overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                    startActivity(editprofile);
                } else {
                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(MainNavigationScreen.this);
                    alertbox.setMessage("If you have already registered.\n\nPlease Login OR Sign up.");
                    alertbox.setTitle("Guest user found !");
                    alertbox.setIcon(R.mipmap.ic_launcher);
                    alertbox.setPositiveButton("Sign Up", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainNavigationScreen.this, Signup.class));
                            finish();
                        }
                    });
                    alertbox.setNeutralButton("Login",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    startActivity(new Intent(MainNavigationScreen.this, Login.class));
                                    finish();
                                }
                            });
                    alertbox.show();
                }
            }
        });


        // navigationView.setNavigationItemSelectedListener(this);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        assert navigationView != null;
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setItemIconTintList(null);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                navigationView.getMenu().getItem(0).setChecked(false);

                //Checking if the item is in checked state or not, if not make it in checked state
                if (activeMenuItem != null) activeMenuItem.setChecked(false);
                activeMenuItem = menuItem;
                menuItem.setChecked(true);
                //else menuItem.setChecked(true);

                //Closing drawer on item click
                drawer.closeDrawers();
                Fragment fragment = null;
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        // Toast.makeText(getApplicationContext(),"Shop",Toast.LENGTH_SHORT).show();
                        fragment = new MainHomeFragment();
                        getSupportActionBar().setTitle("");
                        break;
                    case R.id.nav_offers:
                        /*Intent notification = new Intent(MainNavigationScreen.this,NotificationsScreen.class);
                        startActivity(notification);*/
                        break;
                    case R.id.nav_services:
                        Intent service_list = new Intent(MainNavigationScreen.this,ServicesList.class);
                        service_list.putExtra("index_selectedServiceType","1");
                        startActivity(service_list);
                        overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                        break;

                    case R.id.nav_bookings:
                        Intent bookingIntent = new Intent(MainNavigationScreen.this,BookingListActivity.class);
                        startActivity(bookingIntent);
                        overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                        break;

                    case R.id.nav_askovenues:
                        Intent nav_askovenues = new Intent(MainNavigationScreen.this,AskOvenues.class);
                        startActivity(nav_askovenues);
                        overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                        break;



                    case R.id.nav_venues:
                        Intent venulist = new Intent(MainNavigationScreen.this,VenuesList.class);
                        startActivity(venulist);
                        overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                        break;
                    case R.id.nav_listbusiness:
                        fragment= new ListBusiness();
                        getSupportActionBar().setTitle("List Your Business");
                        break;
                    case R.id.nav_termsofuse:
                        fragment= new TermsUser();
                        getSupportActionBar().setTitle("Terms of Use");
                        break;

                    case R.id.nav_aboutus:
                        Intent a = new Intent(MainNavigationScreen.this, AboutUsExpandable.class);
                        a.putExtra("aboutus","aboutus");
                        startActivity(a);
                        break;

                    case R.id.nav_howitwork:
                        startActivity(new Intent(MainNavigationScreen.this,HowitWorkTab.class));
                        //overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                        break;

                    case R.id.nav_contactus:
                        Intent contafctus = new Intent(MainNavigationScreen.this,ContactUs.class);
                        startActivity(contafctus);
                        overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                        break;

                    case R.id.nav_signout:
                        new Logout().execute();
                        break;

                    default:
                        Toast.makeText(getApplicationContext(), "Coming Soon...", Toast.LENGTH_SHORT).show();

                        break;

                }

                if (fragment != null) {

                    fragmentTransaction.replace(R.id.frame, fragment);
                    //getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
                    fragmentTransaction.addToBackStack(null).commit();

                } else {
                    menuItem.setChecked(false);
                }

                return true;
            }
        });

        new GetGenCartDetails().execute();

    }

    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return;
            } else if(getFragmentManager().getBackStackEntryCount() == 0) {
                this.doubleBackToExitPressedOnce = true;
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new MainHomeFragment()).commit();
                //getSupportActionBar().setTitle("Ovenues");
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            }


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }


    }



    boolean enabletotalItem =true;
    boolean enabletotalAmount =true;
    TextView tv_cartItemCount;
    ImageView ic_cart_actionbar;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_option, menu);

        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.cart_badge_layout);
        RelativeLayout notifCount = (RelativeLayout)   MenuItemCompat.getActionView(item);

        tv_cartItemCount = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
        ic_cart_actionbar = (ImageView) notifCount.findViewById(R.id.ic_cart_actionbar);

        ic_cart_actionbar .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainNavigationScreen.this,CartSummaryScreen.class));

            }
        });


        if(enabletotalItem)
            menu.add(0, MENU_VENUE, Menu.NONE, "Venues   Bookings : "+sharepref.getString(Const.PREF_USER_CART_VENUES,"0"));
        if(enabletotalItem)
            menu.add(0, MENU_CATERING, Menu.NONE,"Catering Bookings : "+sharepref.getString(Const.PREF_USER_CART_CATERINGS,"0"));
        if(enabletotalItem)
            menu.add(0, MENU_SERVICES, Menu.NONE, "Services Bookings : "+sharepref.getString(Const.PREF_USER_CART_SERVICES,"0"));

        if(sharepref.getString(Const.PREF_USER_CART_TOTAL_AMOUNT,"").equalsIgnoreCase("")){
            enabletotalAmount=false;
        }
        if(enabletotalAmount)
            menu.add(0, MENU_TOTALAMOUNT, Menu.NONE, "Total Amount    : $ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(sharepref.getString(Const.PREF_USER_CART_TOTAL_AMOUNT,"0.0")) ) );

        return true;

    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
     /*   menu.clear();
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_option, menu);
        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.cart_badge_layout);*/


        tv_cartItemCount.setText(sharepref.getString(Const.PREF_USER_CART_TOTAL_ITEMS, ""));

        ic_cart_actionbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainNavigationScreen.this, CartSummaryScreen.class));

            }
        });

       /* if (enabletotalItem)
            menu.add(0, MENU_VENUE, Menu.NONE, "Venues   Bookings : " + sharepref.getString(Const.PREF_USER_CART_VENUES, "")).setIcon(R.drawable.fab_add);
        if (enabletotalItem)
            menu.add(0, MENU_CATERING, Menu.NONE, "Catering Bookings : " + sharepref.getString(Const.PREF_USER_CART_CATERINGS, "")).setIcon(R.drawable.fab_add);
        if (enabletotalItem)
            menu.add(0, MENU_SERVICES, Menu.NONE, "Services Bookings : " + sharepref.getString(Const.PREF_USER_CART_SERVICES, "")).setIcon(R.drawable.fab_add);

        if (sharepref.getString(Const.PREF_USER_CART_TOTAL_AMOUNT, "").equalsIgnoreCase("")) {
            enabletotalAmount = false;
        }

        if (enabletotalAmount)
            menu.add(0, MENU_TOTALAMOUNT, Menu.NONE, "Total Amount    : $ " + Const.GLOBAL_FORMATTER.format(Double.parseDouble(sharepref.getString(Const.PREF_USER_CART_TOTAL_AMOUNT, "0.0")))).setIcon(R.drawable.ic_collapse);*/


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.badge) {

            startActivity(new Intent(MainNavigationScreen.this,CartSummaryScreen.class));
            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainNavigationScreen.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
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
            progressDialog = ProgressDialog.show(MainNavigationScreen.this, "Loading", "Please Wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }
        @Override
        protected String doInBackground(Object... parametros) {
            // System.out.println("On do in back ground----done-------");
            //Log.d("post execute", "Executando doInBackground   ingredients");
            try {

                JSONObject req = new JSONObject();
                req.put("user_id",sharepref.getString(Const.PREF_USER_ID,""));

                //Log.d("REq Json======", req.toString());

                String response = post(Const.SERVER_URL_API +"/mob_logout", req.toString(),"post");//post("http://54.153.127.215/api/login", req.toString());
                 //Log.d("REsponce Json====",response);
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

                //Toast.makeText(MainNavigationScreen.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
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
                Toast.makeText(MainNavigationScreen.this, "Sign out Done !", Toast.LENGTH_LONG).show();

            progressDialog.dismiss();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new GetGenCartDetails().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetGenCartDetails().execute();
    }

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
                //Log.d("REsponce Json====",response);
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

            //Log.i("RESPONSE GEN Cart", res_cart);
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

                    adapter_venue_name_search = new VenueNameSearchAutocompleteAdapter(MainNavigationScreen.this, R.layout.activity_venues_list, R.id.row_text_view_only, searchcitymodel);
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
                        }else if(group.equalsIgnoreCase("2")){
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


                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(MainNavigationScreen.this);
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



}
