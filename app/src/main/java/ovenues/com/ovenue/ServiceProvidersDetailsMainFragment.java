package ovenues.com.ovenue;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import movile.com.creditcardguide.APICall;
import ovenues.com.ovenue.adapter.ImageViewPagerAdapter;
import ovenues.com.ovenue.adapter.ServiceproviderVenueBothCateringPricingMenuAdapter;
import ovenues.com.ovenue.adapter.ServiceProviderChargesDefaultAdapter;
import ovenues.com.ovenue.cart.CartSummaryScreen;
import ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderBakeryRequestQuote;
import ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderCateringPricingFragment;
import ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderGenericPricingFragment;
import ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderGenericRequestQuote;
import ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderOverviewFragment;
import ovenues.com.ovenue.modelpojo.ServiceProviderChargesDefaultModel;
import ovenues.com.ovenue.modelpojo.ServiceproviderCateringPricingMenuModel;
import ovenues.com.ovenue.modelpojo.Spiners.SearchVenueSpiners;
import ovenues.com.ovenue.utils.Const;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.TabType.ICONS_ONLY;
import static ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderBakeryRequestQuote.sp_design_cakeAdapter;
import static ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderBakeryRequestQuote.sp_design_cake_aarayList;
import static ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderBakeryRequestQuote.str_upload_bakery_image_url;
import static ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderBakeryRequestQuote.tv_delivery_freecharge_requestQuote_bakerySP;
import static ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderCateringPricingFragment.str_address;
import static ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderCateringPricingFragment.tv_delivery_freecharge_cateringSP;
import static ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderGenericPricingFragment.mRecyclerViewGenricServiceProviderCharages;
import static ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderGenericRequestQuote.eventTypeAdapter;
import static ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderGenericRequestQuote.sp_eventType_aarayList;
import static ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderGenericRequestQuote.tv_delivery_freecharge_requestquote_genricSP;
import static ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderOverviewFragment.ll_numberDetails;
import static ovenues.com.ovenue.utils.APICall.POST_MULTIPART;
import static ovenues.com.ovenue.utils.APICall.post;
import static ovenues.com.ovenue.utils.Const.GLOBAL_FORMATTER;

public class ServiceProvidersDetailsMainFragment extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    public static JsonArray open_hours,open_hours_cateringSP;
    public static Activity activitySP;

    CollapsingToolbarLayout collapse_toolbar;


    private static final int MENU_VENUE = Menu.FIRST;
    private static final int MENU_CATERING= Menu.FIRST + 1;
    private static final int MENU_SERVICES = Menu.FIRST + 2;
    private static final int MENU_TOTALAMOUNT = Menu.FIRST + 3;
    private static final int MENU_LOGOUT = Menu.FIRST + 4;
    TextView tv_cartItemCount;

    public static RecyclerView.Adapter adapterGenericPricingAdapter;
    public ArrayList results_GenericPricing;

    public static RecyclerView.Adapter adapterCateringPricingAdapter;
    public ArrayList results_CateringPricing;

    public static JsonArray food_menu,restaurant_menu ,beverages;
    //========================================================================
    Bitmap myBitmap;
    //Uri picUri;
    File file;
    Uri fileUri;
    String  file_path ,filename;


    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 107;
    //========================================================================

    public static  FragmentManager fm;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public final int MY_REQUEST_CODE=500;
    public final int MY_REQUEST_CODE_STORAGE=600;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Ovenues Images";



    public static GoogleApiClient mGoogleApiClient;
    public static ArrayList<String> placeID;
    public static ArrayList<String> termsList = null;
    public static String str_is_register=null,str_has_number=null;



    ViewPagerAdapter adapter ;
    public static SharedPreferences sharepref;
    public static String str_service_provider_id,str_place_id;


    public static String is_delivery,is_delivery_paid,delivery_charges,is_pickup ,is_onsite_service,is_delivery_na
            ,providerName,providerDescription,providerAddress,providerPhonenumber,providerTerms,str_guest_count_catering_CourseMenu;


    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private ImageViewPagerAdapter mAdapterpagerImages;
    ArrayList<String> mImageResources = new ArrayList<String>();

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private static FrameLayout fl_imagescrrols;

    //private static final Integer[] tabIcons = {R.drawable.ic_home_white_24dp, R.drawable.ic_chat_white_24dp, R.drawable.ic_account_box_white_24dp};//Tab icons array

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_providers_details_main_fragment);


        activitySP = this;
        tabType = TabType.DEFAULT;//(TabType) getIntent().getSerializableExtra(TAB_TYPE);//get the type of tab
        collapse_toolbar =(CollapsingToolbarLayout)this.findViewById(R.id.collapse_toolbar);
        fl_imagescrrols = (FrameLayout) this.findViewById(R.id.fl_imagescrrols);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Services Provider Details");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimaryDark));
        }

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        if(getIntent().hasExtra("service_provider_id")){
            str_service_provider_id=getIntent().getStringExtra("service_provider_id");
        }

         fm = getSupportFragmentManager();

        permissions.add(CAMERA);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissions.add(WRITE_EXTERNAL_STORAGE);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }



        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();


        //=======GENERIC SERVICE PROVIDERS CHARGES RECYSLERVIEW ADDAPTER SET DATA =========================
        results_GenericPricing = new ArrayList<ServiceProvidersDetailsMainFragment>();
        adapterGenericPricingAdapter = new ServiceProviderChargesDefaultAdapter(results_GenericPricing, ServiceProvidersDetailsMainFragment.this);

        //=======CAtering SERVICE PROVIDERS CHARGES RECYSLERVIEW ADDAPTER SET DATA =========================
        results_CateringPricing = new ArrayList<ServiceProvidersDetailsMainFragment>();
        adapterCateringPricingAdapter = new ServiceproviderVenueBothCateringPricingMenuAdapter(results_CateringPricing, ServiceProvidersDetailsMainFragment.this);



        //====VIEW PAGETR ADAPTER AUTO IMAGE SCROLLING ===================
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // ==== VIEWPAGER SCROLLING IMAGES CODE STARTS==========
        pager_indicator = (LinearLayout) this.findViewById(R.id.viewPagerCountDots);
        intro_images = (ViewPager) this.findViewById(R.id.pager_introduction);


        mAdapterpagerImages = new ImageViewPagerAdapter(ServiceProvidersDetailsMainFragment.this, mImageResources);

        intro_images.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                }
                dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                if (position + 1 == dotsCount) {

                } else {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        viewPager = (ViewPager)this.findViewById(R.id.viewPager);
//        setupViewPager(viewPager);
        viewPager.setAdapter(adapter);
        new GetServiceProvidersDetails().execute();

        tabLayout = (TabLayout)this.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);//setting tab over viewpager

        //Implementing tab selected listener over tablayout
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());//setting current selected item over viewpager
                switch (tab.getPosition()) {
                    case 0:
                      //  Log.e("TAG", "TAB1");
                        break;
                    case 1:
                      //  Log.e("TAG", "TAB2");
                        break;
                    case 2:
                       // Log.e("TAG", "TAB3");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        //Call tab type method
        onTabType();

    }

    private void setUiPageViewController() {

        dotsCount = mAdapterpagerImages.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(ServiceProvidersDetailsMainFragment.this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }
        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    class UpdateTimeTask extends TimerTask {
        public void run() {
            intro_images.post(new Runnable() {
                public void run() {

                    if (intro_images.getCurrentItem() < 8) {
                        intro_images.setCurrentItem(intro_images.getCurrentItem() + 1, true);
                        String abc = String.valueOf(intro_images.getCurrentItem());
                        //Log.i("timer_+", abc);
                    } else {
                        intro_images.setCurrentItem(0, true);
                    }
                }
            });
        }








    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        tv_cartItemCount.setText(sharepref.getString(Const.PREF_USER_CART_TOTAL_ITEMS, ""));

        ic_cart_actionbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ServiceProvidersDetailsMainFragment.this, CartSummaryScreen.class));

            }
        });

        if (enabletotalItem)
            menu.add(0, MENU_VENUE, Menu.NONE, "Venues   Bookings : " + sharepref.getString(Const.PREF_USER_CART_VENUES, "")).setIcon(R.drawable.fab_add);
        if (enabletotalItem)
            menu.add(0, MENU_CATERING, Menu.NONE, "Catering Bookings : " + sharepref.getString(Const.PREF_USER_CART_CATERINGS, "")).setIcon(R.drawable.fab_add);
        if (enabletotalItem)
            menu.add(0, MENU_SERVICES, Menu.NONE, "Services Bookings : " + sharepref.getString(Const.PREF_USER_CART_SERVICES, "")).setIcon(R.drawable.fab_add);

        if (sharepref.getString(Const.PREF_USER_CART_TOTAL_AMOUNT, "").equalsIgnoreCase("")) {
            enabletotalAmount = false;
        }

        if (enabletotalAmount)
            menu.add(0, MENU_TOTALAMOUNT, Menu.NONE, "Total Amount    : $ " + Const.GLOBAL_FORMATTER.format(Double.parseDouble(sharepref.getString(Const.PREF_USER_CART_TOTAL_AMOUNT, "0.0")))).setIcon(R.drawable.ic_collapse);


        return super.onPrepareOptionsMenu(menu);
    }

    boolean enabletotalItem =true;
    boolean enabletotalAmount =true;
    ImageView ic_cart_actionbar;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_option, menu);
        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.cart_badge_layout);
        RelativeLayout notifCount = (RelativeLayout)   MenuItemCompat.getActionView(item);

        tv_cartItemCount= (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
        tv_cartItemCount.setText(sharepref.getString(Const.PREF_USER_CART_TOTAL_ITEMS,""));
        ic_cart_actionbar = (ImageView) notifCount.findViewById(R.id.ic_cart_actionbar);

        ic_cart_actionbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ServiceProvidersDetailsMainFragment.this,CartSummaryScreen.class));

            }
        });

        if(enabletotalItem)
            menu.add(0, MENU_VENUE, Menu.NONE, "Venues   Bookings : "+sharepref.getString(Const.PREF_USER_CART_VENUES,"")).setIcon(R.drawable.fab_add);
        if(enabletotalItem)
            menu.add(0, MENU_CATERING, Menu.NONE,"Catering Bookings : "+sharepref.getString(Const.PREF_USER_CART_CATERINGS,"")).setIcon(R.drawable.fab_add);
        if(enabletotalItem)
            menu.add(0, MENU_SERVICES, Menu.NONE, "Services Bookings : "+sharepref.getString(Const.PREF_USER_CART_SERVICES,"")).setIcon(R.drawable.fab_add);

        if(sharepref.getString(Const.PREF_USER_CART_TOTAL_AMOUNT,"").equalsIgnoreCase("")){
            enabletotalAmount=false;
        }

        if(enabletotalAmount)
            menu.add(0, MENU_TOTALAMOUNT, Menu.NONE, "Total Amount    : $ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(sharepref.getString(Const.PREF_USER_CART_TOTAL_AMOUNT,"0.0")) ) ).setIcon(R.drawable.ic_collapse);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                finish();
                return true;

            //noinspection SimplifiableIfStatement
            case R.id.badge :
                startActivity(new Intent(ServiceProvidersDetailsMainFragment.this,CartSummaryScreen.class));
                return true;

        /*    case R.id.action_custom_indicator:
                mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                break;
            case R.id.action_custom_child_animation:
                mDemoSlider.setCustomAnimation(new ChildAnimationExample());
                break;
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


    private TabType tabType;
    public enum  TabType {
        DEFAULT, ICON_TEXT,ICONS_ONLY,CUSTOM;
    }


    /**  on the basis of tab type call respective method **/
    private void onTabType() {
        switch (tabType) {
            case DEFAULT:
                //don't do anything here
                break;
            case ICON_TEXT:
            case ICONS_ONLY:
                //for both Types call set Icons method
                //tabWithIcon();
                break;
            case CUSTOM:
                //Call custom tab method
                //setUpCustomTabs();
                break;
        }
    }

    //Setting View Pager
/*
    private void setupViewPager(ViewPager viewPager) {

       */
/* for (String tab : tabArray)
            adapter.addFrag(DummyFragment.newInstance(tab), tab);*//*


    }
*/


    /**  method to set icon over tab **//*
    private void tabWithIcon() {
        for (int i = 0; i < tabIcons.length; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);//get tab via position
            if (tab != null)
                tab.setIcon(tabIcons[i]);//set icon
        }
    }*/

    /**  set custom layout over tab **//*
    private void setUpCustomTabs() {
        for (int i = 0; i < tabArray.length; i++) {
            TextView customTab = (TextView) LayoutInflater.from(this).inflate(R.layout.service_provider_details_custom_tab_layout, null);//get custom view
            customTab.setText(tabArray[i]);//set text over view
            customTab.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[i], 0, 0);//set icon above the view
            TabLayout.Tab tab = tabLayout.getTabAt(i);//get tab via position
            if (tab != null)
                tab.setCustomView(customTab);//set custom view
        }
    }*/

    //View Pager fragments setting adapter class
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();//fragment arraylist
        private final List<String> mFragmentTitleList = new ArrayList<>();//title arraylist

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        //adding fragments and title method
        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (tabType == ICONS_ONLY)
                return "";
            return mFragmentTitleList.get(position);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(ServiceProvidersDetailsMainFragment.this)
                .setMessage(message)
                .setPositiveButton("OK" , okListener)
                .setNegativeButton("Cancel" , null)
                .create()
                .show();
    }

    
    public static class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements
            Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public android.widget.Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    // String filterString = constraint.toString();
                    // System.out.println("Text:::::::::::"+filterString);
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        //System.out.println("Text:::::::::::" + constraint.toString());
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint,
                                              FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return (String) resultList.get(index);
        }
    }


    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(Const.PLACES_API_BASE
                    + Const.TYPE_AUTOCOMPLETE + Const.OUT_JSON);
            sb.append("?key=AIzaSyDtr3qaj_rlFGjLyPW4OClB7r7oO6S5jj4");
            // sb.append("&components=country:il");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            URL url = new URL(sb.toString());
            //System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {

            return resultList;
        } catch (IOException e) {

            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            if (placeID == null)
            {placeID = new ArrayList<String>();}
            else{
                placeID.clear();

                // Create a JSON object hierarchy from the results
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
                JSONArray termsJsonArray = null;
                // System.out.println(predsJsonArray);

                // Extract the Place descriptions from the results
                resultList = new ArrayList(predsJsonArray.length());
                termsList = new ArrayList<String>();

                for (int i = 0; i < predsJsonArray.length(); i++) {
                    //System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                    //System.out.println("============================================================");
                    resultList.add(predsJsonArray.getJSONObject(i).getString("description"));

                    placeID.add(predsJsonArray.getJSONObject(i).getString("place_id"));
                    //Log.d("place id",predsJsonArray.getJSONObject(i).getString("place_id"));
                    termsJsonArray = predsJsonArray.getJSONObject(i).getJSONArray(
                            "terms");

                    //System.out.println("termsJsonArray:::::::" + termsJsonArray);

                    int lenght = termsJsonArray.length() - 1;

                    //System.out.println("lenght:::::::" + lenght);

                    termsList.add(termsJsonArray.getJSONObject(lenght).getString("value"));
                    // System.out.println("termsList:::::::::::" + termsList);
                    // }
                }
            }
        } catch (JSONException e) {

        }

        return resultList;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(ServiceProvidersDetailsMainFragment.this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress , 20);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            Log.e("location " , location.getLatitude()+" "+location.getLongitude());
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }


    //=========================================================================================

    /**
     * Create a chooser intent to select the source to get image from.<br/>
     * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the intent chooser.
     */
    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }


    /**
     * Get URI to image received from capture by camera.
     */
    private  Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {

            ImageView imageView = (ImageView) findViewById(R.id.img_upload);

            if (getPickImageResultUri(data) != null) {
                //picUri = getPickImageResultUri(data);
                fileUri = data.getData();

                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                    // myBitmap = rotateImageIfRequired(myBitmap, picUri);
                    //myBitmap = getResizedBitmap(myBitmap, 500);

                    imageView.setImageBitmap(myBitmap);


                    //new UploadTask().execute();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                file_path = getPath(this,fileUri);
                filename = file_path;// file_path.substring(file_path.lastIndexOf("/")+1);

                    /*filename = getRealPathFromUri(getBaseContext(),picUri);*/
                //file = new File(getRealPathFromUri(getBaseContext(),getPickImageResultUri(data)));
                file = new File(file_path);

                new UploadImage().execute();

            } else {

                bitmap = (Bitmap) data.getExtras().get("data");
                myBitmap = bitmap;
                imageView.setImageBitmap(myBitmap);

                fileUri = getImageUri(this,myBitmap);
                file_path = getPath(this,fileUri);
                filename =  file_path.substring(file_path.lastIndexOf("/")+1);

                file = new File(file_path);

                new UploadImage().execute();

            }

        }

    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "OvenuesImage", null);
        return Uri.parse(path);
    }


    /**
     * Get the URI of the selected image from {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera and gallery image.
     *
     * @param data the returned data of the activitySP result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }


        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("pic_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("pic_uri");
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }


    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (hasPermission(perms)) {

                    } else {

                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                                //Log.d("API123", "permisionrejected " + permissionsRejected.size());

                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }


    //=========================================================================================



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private  String getPath(final Context context, final Uri uri)
    {
        //check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context , uri , null , null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static String getDataColumn(Context context , Uri uri , String selection , String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri , projection , selection , selectionArgs , null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String res_serviceprovider_details;
    protected static ProgressDialog progressDialog_providerDetails;
    class GetServiceProvidersDetails extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog_providerDetails = ProgressDialog.show(ServiceProvidersDetailsMainFragment.this, "Loading", "Please Wait...", true, false);

        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {
                /*String upend = "city="+sharepref.getString(Const.PREF_CITY_ID,"")+"&event_type="+event_type_id+"&venue_type="+venue_type_id
                        +"&amenities="+amenty_id+"&guest_count_min="+guest_count_min+"&guest_count_max="+guest_count_max
                        +"&price_min="+price_min+"&price_max&"+price_max+"&from="+str_from+"&to="+str_to+"&sort_by="+sort_by+"&user_id="+sharepref.getString(Const.PREF_USER_ID,"");*/
                String response = post(Const.SERVER_URL_API +"service_provider_website_details?service_id="+sharepref.getString(Const.PREF_STR_SERVICE_ID,null)+"&service_provider_id="+str_service_provider_id, "","get");
                Log.d("URL ====",Const.SERVER_URL_API +"service_provider_website_details?service_id="+sharepref.getString(Const.PREF_STR_SERVICE_ID,null)+"&service_provider_id="+str_service_provider_id);
                res_serviceprovider_details=response;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res_serviceprovider_details;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try{
                JSONObject obj = new JSONObject(res_serviceprovider_details);
                //Log.i("RESPONSE", res);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")) {

                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(res_serviceprovider_details).getAsJsonObject();


                    adapter.addFrag(new ServiceproviderOverviewFragment(),"OverView");

                    String venue_name= rootObj.getAsJsonObject("message").getAsJsonObject("details").get("provider_name").getAsString();
                    String address1=!rootObj.getAsJsonObject("message").getAsJsonObject("details").get("address1").isJsonNull()
                            ? rootObj.getAsJsonObject("message").getAsJsonObject("details").get("address1").getAsString()+",":null;
                    String address2= !rootObj.getAsJsonObject("message").getAsJsonObject("details").get("address2").isJsonNull()
                            ? rootObj.getAsJsonObject("message").getAsJsonObject("details").get("address2").getAsString()
                            +",":null;
                    String statae= !rootObj.getAsJsonObject("message").getAsJsonObject("details").get("state").isJsonNull()
                            ? rootObj.getAsJsonObject("message").getAsJsonObject("details").get("state").getAsString():null;
                    String zipcode=!rootObj.getAsJsonObject("message").getAsJsonObject("details").get("zipcode").isJsonNull()
                            ?"-"+ rootObj.getAsJsonObject("message").getAsJsonObject("details").get("zipcode").getAsString():null;
                    String phone_number=rootObj.getAsJsonObject("message").getAsJsonObject("details").has("phone_number")
                            &&!rootObj.getAsJsonObject("message").getAsJsonObject("details").get("phone_number").isJsonNull()
                            ? rootObj.getAsJsonObject("message").getAsJsonObject("details").get("phone_number").getAsString():"";
                    providerPhonenumber = phone_number;

                    getSupportActionBar().setTitle(venue_name);
                   /* if(address2 ==null){
                        providerAddress = address1+statae+zipcode;
                    }else{
                        providerAddress = address1+"\n"+address2+statae+zipcode;
                    }*/


                    providerAddress = address1+statae+zipcode;
                    providerName =venue_name;




                    is_delivery= rootObj.getAsJsonObject("message").getAsJsonObject("details").get("is_delivery").getAsString();

                    if(rootObj.getAsJsonObject("message").getAsJsonObject("details").has("is_delivery_paid")) {
                        is_delivery_paid = !rootObj.getAsJsonObject("message").getAsJsonObject("details").get("is_delivery_paid").isJsonNull()
                                ? rootObj.getAsJsonObject("message").getAsJsonObject("details").get("is_delivery_paid").getAsString() : "0";
                       // Log.e("delivery paid","----"+is_delivery_paid);
                    }else{
                        is_delivery_paid="0";
                    }
                    delivery_charges= !rootObj.getAsJsonObject("message").getAsJsonObject("details").get("delivery_charges").isJsonNull()
                            ? rootObj.getAsJsonObject("message").getAsJsonObject("details").get("delivery_charges").getAsString():null;
                    is_pickup= rootObj.getAsJsonObject("message").getAsJsonObject("details").get("is_pickup").getAsString();
                    is_onsite_service= rootObj.getAsJsonObject("message").getAsJsonObject("details").get("is_onsite_service").getAsString();
                    is_delivery_na= rootObj.getAsJsonObject("message").getAsJsonObject("details").get("is_delivery_na").getAsString();

                    str_is_register=rootObj.getAsJsonObject("message").getAsJsonObject("details").get("is_registered").getAsString();
                    str_has_number= rootObj.getAsJsonObject("message").getAsJsonObject("details").has("has_phone_number")
                            ?rootObj.getAsJsonObject("message").getAsJsonObject("details").get("has_phone_number").getAsString():"";
                    //Log.e("has number--",str_has_number+""+str_is_register);

                    String description = rootObj.getAsJsonObject("message").getAsJsonObject("details").get("description").getAsString();
                    providerDescription = description;

                    String t_and_c = !rootObj.getAsJsonObject("message").getAsJsonObject("details").get("t_and_c").isJsonNull()
                            ? rootObj.getAsJsonObject("message").getAsJsonObject("details").get("t_and_c").getAsString():null;

                    providerTerms = t_and_c;

                    String service_id = !rootObj.getAsJsonObject("message").getAsJsonObject("details").get("service_id").isJsonNull()
                            ? rootObj.getAsJsonObject("message").getAsJsonObject("details").get("service_id").getAsString():null;



                    if(service_id.equalsIgnoreCase("1")){//====SERVICE ID =1 MEANS CATERING SERVICE ======

                        open_hours_cateringSP = !rootObj.getAsJsonObject("message").getAsJsonArray("open_hours").isJsonNull()
                                ? rootObj.getAsJsonObject("message").getAsJsonArray("open_hours"):null;


                         food_menu = !rootObj.getAsJsonObject("message").getAsJsonArray("food_menu").isJsonNull()
                                ? rootObj.getAsJsonObject("message").getAsJsonArray("food_menu"):null;
                         restaurant_menu = !rootObj.getAsJsonObject("message").getAsJsonArray("restaurant_menu").isJsonNull()
                                ? rootObj.getAsJsonObject("message").getAsJsonArray("restaurant_menu"):null;
                         beverages = !rootObj.getAsJsonObject("message").getAsJsonArray("beverages").isJsonNull()
                                ? rootObj.getAsJsonObject("message").getAsJsonArray("beverages"):null;

                        if(!food_menu.isJsonNull() && food_menu.size()>0){

                            for (int j = 0; j < food_menu.size(); j++) {
                                String menu_id = food_menu.get(j).getAsJsonObject().get("menu_id").getAsString();
                                String price_per_plate = food_menu.get(j).getAsJsonObject().get("price_per_plate").getAsString();
                                String menu_desc = food_menu.get(j).getAsJsonObject().get("menu_desc").getAsString();
                                String total_courses = food_menu.get(j).getAsJsonObject().get("total_courses").getAsString();

                                String is_group_size=  !food_menu.get(j).getAsJsonObject().get("is_group_size").isJsonNull()
                                        ? food_menu.get(j).getAsJsonObject().get("is_group_size").getAsString() : null;
                                String group_size_from =!food_menu.get(j).getAsJsonObject().get("group_size_from").isJsonNull()
                                        ? food_menu.get(j).getAsJsonObject().get("group_size_from").getAsString(): null;
                                String group_size_to = !food_menu.get(j).getAsJsonObject().get("group_size_to").isJsonNull()
                                        ? food_menu.get(j).getAsJsonObject().get("group_size_to").getAsString(): null;
                                Log.d("is groupsize",""+is_group_size);
                                results_CateringPricing.add(new ServiceproviderCateringPricingMenuModel(menu_id,menu_desc,"1",false));

                            }
                        }

                        if(!restaurant_menu.isJsonNull() && restaurant_menu.size()>0) {
                            for (int j = 0; j < restaurant_menu.size(); j++) {

                                String menu_id = restaurant_menu.get(j).getAsJsonObject().get("menu_id").getAsString();
                                String menu_desc = restaurant_menu.get(j).getAsJsonObject().get("menu_desc").getAsString();

                                JsonArray array_item_menu = !restaurant_menu.get(j).getAsJsonObject().getAsJsonArray("items").isJsonNull()
                                        ? restaurant_menu.get(j).getAsJsonObject().getAsJsonArray("items") : null;

                                if (!array_item_menu.isJsonNull() && array_item_menu.size() > 0) {

                                    for (int k = 0; k < array_item_menu.size(); k++) {

                                        String item_id = array_item_menu.get(k).getAsJsonObject().get("item_id").getAsString();
                                        String item_name = array_item_menu.get(k).getAsJsonObject().get("item_name").getAsString();
                                        String price_per_plate = array_item_menu.get(k).getAsJsonObject().get("price_per_plate").getAsString();
                                        // items.add(new Item("Item at " + i, "Item description at " + i));
                                    }
                                    results_CateringPricing.add(new ServiceproviderCateringPricingMenuModel(menu_id,menu_desc,"2",false));
                                }
                            }
                        }

                        if(!beverages.isJsonNull() && beverages.size()>0){

                            for (int j = 0; j < beverages.size(); j++) {
                                String beverage_id = beverages.get(j).getAsJsonObject().get("beverage_id").getAsString();
                                String option_desc = beverages.get(j).getAsJsonObject().get("option_desc").getAsString();
                                String option_charges = beverages.get(j).getAsJsonObject().get("option_charges").getAsString();
                                String is_group_size = !beverages.get(j).getAsJsonObject().get("is_group_size").isJsonNull()
                                        ? beverages.get(j).getAsJsonObject().get("is_group_size").getAsString() : null;
                                String group_size_from = !beverages.get(j).getAsJsonObject().get("group_size_from").isJsonNull()
                                        ? beverages.get(j).getAsJsonObject().get("group_size_from").getAsString() : null;
                                String group_size_to = !beverages.get(j).getAsJsonObject().get("group_size_to").isJsonNull()
                                        ? beverages.get(j).getAsJsonObject().get("group_size_to").getAsString() : null;

                                //results_CateringPricing.add(new ServiceproviderCateringPricingMenuModel(beverage_id,option_desc,"3"));
                            }
                            results_CateringPricing.add(new ServiceproviderCateringPricingMenuModel("0","Beverages","3",false));
                        }


                        if(food_menu.size()==0 && restaurant_menu.size()==0 && beverages.size()==0){
                            adapter.addFrag(new ServiceproviderGenericRequestQuote(), "Request Quote");
                            adapter.notifyDataSetChanged();
                        }else {
                            adapter.addFrag(new ServiceproviderCateringPricingFragment(), "Menu");
                            adapter.addFrag(new ServiceproviderGenericRequestQuote(), "Request Quote");
                            adapter.notifyDataSetChanged();
                        }

                        adapterCateringPricingAdapter.notifyDataSetChanged();



                    }else if(service_id.equalsIgnoreCase("2")){//===SERVICVE ID =2 FOR BAKERY SERVICE PROVIDERS...
                         open_hours = !rootObj.getAsJsonObject("message").getAsJsonArray("open_hours").isJsonNull()
                                ? rootObj.getAsJsonObject("message").getAsJsonArray("open_hours"):null;

                        JsonArray arrray_charges = !rootObj.getAsJsonObject("message").getAsJsonArray("charges").isJsonNull()
                                ? rootObj.getAsJsonObject("message").getAsJsonArray("charges"):null;

                            if(!arrray_charges.isJsonNull() && arrray_charges.size()>0){
                                adapter.addFrag(new ServiceproviderGenericPricingFragment(),"Pricing");
                                adapter.addFrag(new ServiceproviderBakeryRequestQuote(),"Request Quote");

                                for (int j = 0; j < arrray_charges.size(); j++) {
                                    String id = arrray_charges.get(j).getAsJsonObject().get("id").getAsString();
                                    String service_title = arrray_charges.get(j).getAsJsonObject().get("service_title").getAsString();
                                    String service_inclusions = !arrray_charges.get(j).getAsJsonObject().get("service_inclusions").isJsonNull()
                                            ? arrray_charges.get(j).getAsJsonObject().get("service_inclusions").getAsString():null;
                                    String charges = arrray_charges.get(j).getAsJsonObject().get("charges").getAsString();
                                    String is_flat_charges = arrray_charges.get(j).getAsJsonObject().get("is_flat_charges").getAsString();
                                    String is_per_person_charges = arrray_charges.get(j).getAsJsonObject().get("is_per_person_charges").getAsString();
                                    String is_per_item = arrray_charges.get(j).getAsJsonObject().get("is_per_item").getAsString();

                                    String pacakage_hours = !arrray_charges.get(j).getAsJsonObject().get("pacakage_hours").isJsonNull()
                                            ? arrray_charges.get(j).getAsJsonObject().get("pacakage_hours").getAsString():null;

                                    String hour_extension_charges = !arrray_charges.get(j).getAsJsonObject().get("hour_extension_charges").isJsonNull()
                                            ? arrray_charges.get(j).getAsJsonObject().get("hour_extension_charges").getAsString():null;

                                    String extension_duration = !arrray_charges.get(j).getAsJsonObject().get("extension_duration").isJsonNull()
                                            ? arrray_charges.get(j).getAsJsonObject().get("extension_duration").getAsString():null;

                                    String extra_person_charges =!arrray_charges.get(j).getAsJsonObject().get("extra_person_charges").isJsonNull()
                                            ? arrray_charges.get(j).getAsJsonObject().get("extra_person_charges").getAsString():null;

                                    String max_guest_extension = !arrray_charges.get(j).getAsJsonObject().get("max_guest_extension").isJsonNull()
                                            ? arrray_charges.get(j).getAsJsonObject().get("max_guest_extension").getAsString():null;

                                    String max_hour_extension = !arrray_charges.get(j).getAsJsonObject().get("max_hour_extension").isJsonNull()
                                            ? arrray_charges.get(j).getAsJsonObject().get("max_hour_extension").getAsString():null;

                                    String is_group_size = !arrray_charges.get(j).getAsJsonObject().get("is_group_size").isJsonNull()
                                            ? arrray_charges.get(j).getAsJsonObject().get("is_group_size").getAsString():null;

                                    String group_size_from = !arrray_charges.get(j).getAsJsonObject().get("group_size_from").isJsonNull()
                                            ? arrray_charges.get(j).getAsJsonObject().get("group_size_from").getAsString():null;
                                    String group_size_to = !arrray_charges.get(j).getAsJsonObject().get("group_size_to").isJsonNull()
                                            ? arrray_charges.get(j).getAsJsonObject().get("group_size_to").getAsString():null;
                                    String photo_url = !arrray_charges.get(j).getAsJsonObject().get("photo_url").isJsonNull()
                                            ? arrray_charges.get(j).getAsJsonObject().get("photo_url").getAsString():null;

                                    String str_chargetype=null;
                                    if(is_flat_charges.equalsIgnoreCase("1")){
                                        str_chargetype="";
                                    }else if(is_per_person_charges.equalsIgnoreCase("1")){
                                        str_chargetype="/ person";
                                    }else if(is_per_item.equalsIgnoreCase("1")){
                                        str_chargetype="/ item";
                                    }
                                    results_GenericPricing.add(new ServiceProviderChargesDefaultModel( id, service_id, service_title, service_inclusions, charges, is_flat_charges, is_per_person_charges, is_per_item, pacakage_hours, hour_extension_charges,
                                            extension_duration, extra_person_charges, max_guest_extension, max_hour_extension, is_group_size, group_size_from, group_size_to, photo_url,
                                            is_delivery ,is_delivery_paid ,delivery_charges ,is_pickup ,is_onsite_service ,is_delivery_na));

                                }
                                adapterGenericPricingAdapter.notifyDataSetChanged();
                            }else {
                                adapter.addFrag(new ServiceproviderBakeryRequestQuote(),"Request Quote");
                                adapter.notifyDataSetChanged();
                            }

                    }else{

                        open_hours = !rootObj.getAsJsonObject("message").getAsJsonArray("open_hours").isJsonNull()
                                ? rootObj.getAsJsonObject("message").getAsJsonArray("open_hours"):null;


                        JsonArray arrray_charges = !rootObj.getAsJsonObject("message").getAsJsonArray("charges").isJsonNull()
                                ? rootObj.getAsJsonObject("message").getAsJsonArray("charges"):null;
                        if(!arrray_charges.isJsonNull() && arrray_charges.size()>0){

                            adapter.addFrag(new ServiceproviderGenericPricingFragment(),"Pricing");
                            adapter.notifyDataSetChanged();

                            mRecyclerViewGenricServiceProviderCharages.setAdapter(adapterGenericPricingAdapter);
                            for (int j = 0; j < arrray_charges.size(); j++) {
                                String id = arrray_charges.get(j).getAsJsonObject().get("id").getAsString();
                                String service_title = arrray_charges.get(j).getAsJsonObject().get("service_title").getAsString();
                                String service_inclusions = !arrray_charges.get(j).getAsJsonObject().get("service_inclusions").isJsonNull()
                                        ? arrray_charges.get(j).getAsJsonObject().get("service_inclusions").getAsString():null;
                                String charges = arrray_charges.get(j).getAsJsonObject().get("charges").getAsString();
                                String is_flat_charges = arrray_charges.get(j).getAsJsonObject().get("is_flat_charges").getAsString();
                                String is_per_person_charges = arrray_charges.get(j).getAsJsonObject().get("is_per_person_charges").getAsString();
                                String is_per_item = arrray_charges.get(j).getAsJsonObject().get("is_per_item").getAsString();

                                String pacakage_hours = !arrray_charges.get(j).getAsJsonObject().get("pacakage_hours").isJsonNull()
                                        ? arrray_charges.get(j).getAsJsonObject().get("pacakage_hours").getAsString():null;

                                String hour_extension_charges = !arrray_charges.get(j).getAsJsonObject().get("hour_extension_charges").isJsonNull()
                                        ? arrray_charges.get(j).getAsJsonObject().get("hour_extension_charges").getAsString():null;

                                String extension_duration = !arrray_charges.get(j).getAsJsonObject().get("extension_duration").isJsonNull()
                                        ? arrray_charges.get(j).getAsJsonObject().get("extension_duration").getAsString():null;

                                String extra_person_charges =!arrray_charges.get(j).getAsJsonObject().get("extra_person_charges").isJsonNull()
                                        ? arrray_charges.get(j).getAsJsonObject().get("extra_person_charges").getAsString():null;

                                String max_guest_extension = !arrray_charges.get(j).getAsJsonObject().get("max_guest_extension").isJsonNull()
                                        ? arrray_charges.get(j).getAsJsonObject().get("max_guest_extension").getAsString():null;

                                String max_hour_extension = !arrray_charges.get(j).getAsJsonObject().get("max_hour_extension").isJsonNull()
                                        ? arrray_charges.get(j).getAsJsonObject().get("max_hour_extension").getAsString():null;

                                String is_group_size = !arrray_charges.get(j).getAsJsonObject().get("is_group_size").isJsonNull()
                                        ? arrray_charges.get(j).getAsJsonObject().get("is_group_size").getAsString():null;

                                String group_size_from = !arrray_charges.get(j).getAsJsonObject().get("group_size_from").isJsonNull()
                                        ? arrray_charges.get(j).getAsJsonObject().get("group_size_from").getAsString():null;
                                String group_size_to = !arrray_charges.get(j).getAsJsonObject().get("group_size_to").isJsonNull()
                                        ? arrray_charges.get(j).getAsJsonObject().get("group_size_to").getAsString():null;
                                String photo_url = !arrray_charges.get(j).getAsJsonObject().get("photo_url").isJsonNull()
                                        ? arrray_charges.get(j).getAsJsonObject().get("photo_url").getAsString():null;

                                String str_chargetype=null;
                                if(is_flat_charges.equalsIgnoreCase("1")){
                                    str_chargetype="";
                                }else if(is_per_person_charges.equalsIgnoreCase("1")){
                                    str_chargetype="/ person";
                                }else if(is_per_item.equalsIgnoreCase("1")){
                                    str_chargetype="/ item";
                                }
                                results_GenericPricing.add(new ServiceProviderChargesDefaultModel( id, service_id, service_title, service_inclusions, charges, is_flat_charges, is_per_person_charges, is_per_item, pacakage_hours, hour_extension_charges,
                                        extension_duration, extra_person_charges, max_guest_extension, max_hour_extension, is_group_size, group_size_from, group_size_to, photo_url,
                                        is_delivery ,is_delivery_paid ,delivery_charges ,is_pickup ,is_onsite_service ,is_delivery_na));

                            }
                            adapterGenericPricingAdapter.notifyDataSetChanged();
                        }
                        adapter.addFrag(new ServiceproviderGenericRequestQuote(),"Request Quote");
                        adapter.notifyDataSetChanged();

                    }
                    adapter.notifyDataSetChanged();


                    JsonArray photo_urlsObj = !rootObj.getAsJsonObject("message").getAsJsonArray("photos").isJsonNull()
                            ? rootObj.getAsJsonObject("message").getAsJsonArray("photos") :null;
                    Log.e("venue's all images---",""+photo_urlsObj.size());
                    if (photo_urlsObj.size() > 0) {
                        mImageResources.clear();
                        for (int j = 0; j < photo_urlsObj.size(); j++) {
                            String photo_url = photo_urlsObj.get(j).getAsJsonObject().get("photo_url").getAsString();

                            /*String security_deposit  = !venuesObj.get(j).getAsJsonObject().get("security_deposit").isJsonNull()
                                    ? venuesObj.get(j).getAsJsonObject().get("security_deposit").getAsString():null;*/
                            mImageResources.add(photo_url);
                            mAdapterpagerImages.notifyDataSetChanged();
                        }
                        intro_images.setAdapter(mAdapterpagerImages);
                        intro_images.setCurrentItem(0);

                        setUiPageViewController();
                        Timer timer = new Timer();
                        timer.schedule(new UpdateTimeTask(), 2000, 6000);
                    }else{
                        fl_imagescrrols.setVisibility(View.GONE);
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


                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(ServiceProvidersDetailsMainFragment.this);
                    alertbox.setMessage(message);
                    alertbox.setTitle("Sorry ! ");
                    alertbox.setIcon(R.mipmap.ic_launcher);

                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0,int arg1) {
                                   /* startActivity(new Intent(VenueDetails.this,MainNavigationScreen.class));
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
            progressDialog_providerDetails.dismiss();
        }
    }

    public static String res_google_place;
    public static class GetPlaceDteailsViaGooglePlace extends AsyncTask<Object, Void, String> {

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

                String response = APICall.post("https://maps.googleapis.com/maps/api/place/details/json?placeid="+str_place_id+"&key=AIzaSyDtr3qaj_rlFGjLyPW4OClB7r7oO6S5jj4&components=country:US", "","get");
                Log.e("Goog URL====","https://maps.googleapis.com/maps/api/place/details/json?placeid="+str_place_id+"&key=AIzaSyDtr3qaj_rlFGjLyPW4OClB7r7oO6S5jj4&components=country:US");
                res_google_place=response;
                Log.d("GOO RESPONCE PLACE====",response);
            }/*catch (JSONException e) {
                e.printStackTrace();
            }*/ catch (IOException e) {
                e.printStackTrace();
            }


            return res_google_place;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try{
                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(res_google_place).getAsJsonObject();

                String street_number="",route="",city="",state="",postalcode="";
                JsonArray address_components = rootObj.getAsJsonObject("result").getAsJsonArray("address_components");
                for(int a=0;a<address_components.size();a++){

                    Gson gson = new Gson();
                    JsonArray types_array = address_components.get(a).getAsJsonObject().getAsJsonArray("types");

                    //Log.d("address type array",types_array.toString());

                    Type type = new TypeToken<List<String>>(){}.getType();
                    List<String> typeList = gson.fromJson(types_array.toString(), type);

                    if(typeList.get(0).equalsIgnoreCase("street_number")){
                        street_number=address_components.get(a).getAsJsonObject().get("long_name").getAsString();
                    }
                    if(typeList.get(0).equalsIgnoreCase("route")){
                        route=address_components.get(a).getAsJsonObject().get("long_name").getAsString();
                    }
                    if(typeList.get(0).equalsIgnoreCase("locality")){
                        city = address_components.get(a).getAsJsonObject().get("long_name").getAsString();
                    }
                    if(typeList.get(0).equalsIgnoreCase("administrative_area_level_1")){
                        state = address_components.get(a).getAsJsonObject().get("long_name").getAsString() ;
                    }
                    if(typeList.get(0).equalsIgnoreCase("postal_code")){
                        postalcode = address_components.get(a).getAsJsonObject().get("long_name").getAsString();
                    }
                }

               /* if(et_address_line_balkery_custom_request.getVisibility()==View.VISIBLE){
                    et_address_line_balkery_custom_request.setText(street_number+" "+route+","+city+","+state+","+postalcode);
                }*/

                //===California VALIDATION , ONLLY California CITY PLACE IS ALLOWED==========
                if(!state.equalsIgnoreCase("California")){
                    if(!state.equalsIgnoreCase("CA")){
                        Toast.makeText(activitySP,"sorry! now we are serving only in california .",Toast.LENGTH_LONG).show();
                    }else{
                        new GetDeliveryRate().execute();
                    }
                }else{
                    new GetDeliveryRate().execute();
                }


                /*String adr_address = rootObj.getAsJsonObject("result").get("adr_address").getAsString();
                adr_address=Html.fromHtml(adr_address)+"";
                Log.d("got address is ==",adr_address);
                String[] addre = adr_address.split(",");
                for(int j=0;j<addre.length;j++){
                    if(j==0){et_address_line_balkery_custom_request.setText(addre[0].toString());                    }
                    if(j==1){et_city.setText(addre[1].toString());}
                    //if(j==2){et_state.setText(addre[2].toString());}
                    if(j==3){et_zipcode.setText(addre[2].toString().substring(addre[2].toString().length()-6));}
                }*/
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    static String res_delRate;
    public static double deliveryPrice;
    static  class GetDeliveryRate extends AsyncTask<Object, Void, String> {

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
                response = post(Const.SERVER_URL_API +"ovenues_delivery_charges?from_address="+providerAddress+"&to_address="+str_address, "","get");
               // Log.e("GET RaTE----",Const.SERVER_URL_API+"ovenues_delivery_charges?from_address="+providerAddress+"&to_address="+str_address+"\n"+response);
                res_delRate=response;
            }/*catch (JSONException e) {
                e.printStackTrace();
            }*/ catch (Exception e) {
                e.printStackTrace();
            }

            return res_delRate;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            // {"status":"success","message":{"rounded_distance":"6.53","delivery_charges":"15.00"},"http_response":200}
            try{
                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(res_delRate).getAsJsonObject();
                if(rootObj.get("status").getAsString().equalsIgnoreCase("Success")) {
                    if (!rootObj.get("message").getAsJsonObject().get("delivery_charges").isJsonNull()) {
                        deliveryPrice = Double.parseDouble(rootObj.get("message").getAsJsonObject().get("delivery_charges").getAsString());
                        delivery_charges = Double.toString(deliveryPrice);
                    }
                }else{
                    Toast.makeText(activitySP,rootObj.get("message").getAsString(),Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if(sharepref.getString(Const.PREF_STR_SERVICE_ID,"").equalsIgnoreCase("1")){
                tv_delivery_freecharge_cateringSP.setText("Delivery ($) : "+GLOBAL_FORMATTER.format(deliveryPrice));
                tv_delivery_freecharge_requestquote_genricSP.setText("Delivery ($) : "+GLOBAL_FORMATTER.format(deliveryPrice));
            }
            if(sharepref.getString(Const.PREF_STR_SERVICE_ID,"").equalsIgnoreCase("2")){
                tv_delivery_freecharge_requestQuote_bakerySP.setText("Delivery ($) : "+GLOBAL_FORMATTER.format(deliveryPrice));
            }
            if(!sharepref.getString(Const.PREF_STR_SERVICE_ID,"").equalsIgnoreCase("1")
                    && !sharepref.getString(Const.PREF_STR_SERVICE_ID,"").equalsIgnoreCase("2")){
                tv_delivery_freecharge_requestquote_genricSP.setText("Delivery ($) : "+GLOBAL_FORMATTER.format(deliveryPrice));
            }

        }
    }

    static String res;
    protected static ProgressDialog progressDialog_imgUpload;
    class UploadImage extends AsyncTask<Object, Void, String> {


        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            progressDialog_imgUpload = ProgressDialog.show(ServiceProvidersDetailsMainFragment.this, "Loading", "Please Wait...", true, false);

        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {
                String response = POST_MULTIPART(Const.SERVER_URL_API +"service_provider_cake_photo",filename.trim(),file,str_service_provider_id.trim());
                Log.d("URL ====",Const.SERVER_URL_API +"service_provider_cake_photo"+filename.trim()+file+str_service_provider_id.trim());
                res=response;
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
                Log.i("RESPONSE", res);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")) {
                    str_upload_bakery_image_url=obj.getString("message");
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


                    final android.support.v7.app.AlertDialog.Builder alertbox = new android.support.v7.app.AlertDialog.Builder(ServiceProvidersDetailsMainFragment.this);
                    alertbox.setMessage("Upload fail please try again...");
                    alertbox.setTitle("Sorry ! ");
                    alertbox.setIcon(R.mipmap.ic_launcher);

                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0,int arg1) {
                                    onBackPressed();
                                    finish();
                                }
                            });
                    alertbox.show();
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            progressDialog_imgUpload.dismiss();
        }
    }

    public static class GetBakeryautofills extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();

          //  progressDialog = ProgressDialog.show(activitySP, "Loading", "Please Wait...", true, false);

        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {
                String response = post(Const.SERVER_URL_API +"service_provider_website_details?service_id="+sharepref.getString(Const.PREF_STR_SERVICE_ID,null)
                        +"&service_provider_id="+str_service_provider_id, "","get");
                Log.d("bakery autofill ====",Const.SERVER_URL_API +"service_provider_website_details?service_id="+sharepref.getString(Const.PREF_STR_SERVICE_ID,null)+"&service_provider_id="+
                        str_service_provider_id);
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
                Log.i("RESPONSE bkrey", res);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")) {

                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(res).getAsJsonObject();
                    sp_design_cake_aarayList.add(new SearchVenueSpiners("0","Select Cake Design"));

                    for(int a=0;a<rootObj.getAsJsonObject("message").getAsJsonArray("charges").size();a++){
                        String id= !rootObj.getAsJsonObject("message").getAsJsonArray("charges").get(a).getAsJsonObject().get("id").isJsonNull()
                                ? rootObj.getAsJsonObject("message").getAsJsonArray("charges").get(a).getAsJsonObject().getAsJsonObject().get("id").getAsString():null;
                        String name=!rootObj.getAsJsonObject("message").getAsJsonArray("charges").get(a).getAsJsonObject().getAsJsonObject().get("service_title").isJsonNull()
                                ? rootObj.getAsJsonObject("message").getAsJsonArray("charges").get(a).getAsJsonObject().getAsJsonObject().get("service_title").getAsString():null;
                        //this is for just dyummydata set it will replace by original API comes...
                        if(id!=null && name!=null){
                            SearchVenueSpiners  event_type_sp = new SearchVenueSpiners(id , name);
                            sp_design_cake_aarayList.add(event_type_sp);
                        }Log.d("design name===",name);
                    }
                    sp_design_cakeAdapter.notifyDataSetChanged();



                }else{
                    String message=obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(activitySP.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
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


                    final android.support.v7.app.AlertDialog.Builder alertbox = new android.support.v7.app.AlertDialog.Builder(activitySP);
                    alertbox.setMessage(message);
                    alertbox.setTitle("Sorry ! ");
                    alertbox.setIcon(R.mipmap.ic_launcher);

                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0,int arg1) {
                                   /* startActivity(new Intent(VenueDetails.this,MainNavigationScreen.class));
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
         //   progressDialog.dismiss();
        }
    }

    public static class GetEventTyes extends AsyncTask<Object, Void, String> {

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

                String response = post(Const.SERVER_URL_API +"events", "","get");
                //Log.d("REsponce Json====",response);
                res=response;
            }catch (IOException e) {
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

                    JsonArray events_typeObj = rootObj.getAsJsonArray("message");
                    sp_eventType_aarayList.add(new SearchVenueSpiners("0","Select Event Type"));

                    for (int j = 0; j < events_typeObj.size(); j++) {
                        String event_type_id=events_typeObj.get(j).getAsJsonObject().get("event_type_id").getAsString();
                        String event_type=events_typeObj.get(j).getAsJsonObject().get("event_type").getAsString();

                        //this is for just dyummydata set it will replace by original API comes...
                        SearchVenueSpiners  event_type_sp = new SearchVenueSpiners(event_type_id , event_type);
                        sp_eventType_aarayList.add(event_type_sp);

                    }
                    eventTypeAdapter.notifyDataSetChanged();
                } else{
                    String message=obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(activitySP.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
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
}