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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ovenues.com.ovenue.VenueOrderServicesStickyHeader.HeaderItemVenueOrderServices;
import ovenues.com.ovenue.adapter.ImageViewPagerAdapter;
import ovenues.com.ovenue.cart.CartSummaryScreen;
import ovenues.com.ovenue.fragments.venue_details.VenueOverView;
import ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans;
import ovenues.com.ovenue.fragments.venue_details.VenueRequestQuote;
import ovenues.com.ovenue.modelpojo.HomeHoriRecyclerSingleItem;
import ovenues.com.ovenue.modelpojo.IdNameUrlGrideRaw;
import ovenues.com.ovenue.modelpojo.ServiceproviderCateringPricingMenuModel;
import ovenues.com.ovenue.modelpojo.Spiners.SearchVenueSpiners;
import ovenues.com.ovenue.modelpojo.VenueOrderModel.VenueOrderPriceModel;
import ovenues.com.ovenue.modelpojo.VenueOrderModel.VenueOrderServiceModel;
import ovenues.com.ovenue.utils.Const;
import ovenues.com.ovenue.utils.Utils;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static ovenues.com.ovenue.VenueDetailsMainFragment.TabType.ICONS_ONLY;
import static ovenues.com.ovenue.adapter.VenueDetailsPages.VenueOrderPriceAdapter.final_total;
import static ovenues.com.ovenue.adapter.VenueDetailsPages.VenueOrderPriceAdapter.isPkgAdded;
import static ovenues.com.ovenue.fragments.venue_details.VenueOverView.adapterHoriScroll_venuesOverView;
import static ovenues.com.ovenue.fragments.venue_details.VenueOverView.allSampleData_venuesOverviews;
import static ovenues.com.ovenue.fragments.venue_details.VenueOverView.dm_venue_venue_overview;
import static ovenues.com.ovenue.fragments.venue_details.VenueOverView.exp_grid_adapter;
import static ovenues.com.ovenue.fragments.venue_details.VenueOverView.listamenities;
import static ovenues.com.ovenue.fragments.venue_details.VenueOverView.ll_amenities;
import static ovenues.com.ovenue.fragments.venue_details.VenueOverView.ll_tandC;
import static ovenues.com.ovenue.fragments.venue_details.VenueOverView.singleItem_venue_overview;
import static ovenues.com.ovenue.fragments.venue_details.VenueOverView.str_t_and_c;
import static ovenues.com.ovenue.fragments.venue_details.VenueOverView.tvTitleDescription;
import static ovenues.com.ovenue.fragments.venue_details.VenueOverView.tvTitleVenueSuiteablefor;
import static ovenues.com.ovenue.fragments.venue_details.VenueOverView.tv_description;
import static ovenues.com.ovenue.fragments.venue_details.VenueOverView.tv_terms;
import static ovenues.com.ovenue.fragments.venue_details.VenueOverView.tv_vanuesuitable;
import static ovenues.com.ovenue.fragments.venue_details.VenueOverView.tv_venue_address;
import static ovenues.com.ovenue.fragments.venue_details.VenueOverView.tv_venue_name_overview;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.adapterCateringPricingAdapterVenue;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.beveragesJSONVenues;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.food_menu;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.img_foodbev;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.img_service;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.mAdapterVenuePricePKG;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.mAdapterVenueServices;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.recyclerview_venueService;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.restaurant_menu;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.results_CateringPricingVenue;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.rv_foodbev;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.sp_timeslotTypeAdapter;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.sp_timeslotType_aarayList;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.str_dateselected;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.str_guest_count;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.str_timeslot;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.venueorderpriceList;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.venueserviceList;
import static ovenues.com.ovenue.fragments.venue_details.VenueRequestQuote.eventTypeAdapter;
import static ovenues.com.ovenue.fragments.venue_details.VenueRequestQuote.sp_eventType_aarayList;
import static ovenues.com.ovenue.utils.APICall.post;

public class VenueDetailsMainFragment extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static CollapsingToolbarLayout collapse_toolbar;

    public  static TextView tv_title,tv_error_datetime;

    public static String str_cart_id = null,str_venue_name="";
    public static String venue_cart_flag=null;
    public static String str_price_pkg_id="";
    public static String str_no_of_hour="";
    public static String str_venue_id=null;
    public static String str_venue_charge=null;
    public static String str_is_register="",str_has_number="",str_venue_charge_duration=null;
    public static Activity activity_venueDetails;
    public static  FragmentManager fmVenue;

    public static int max_occupancy,min_occupancy;
    private static final int MENU_VENUE = Menu.FIRST;
    private static final int MENU_CATERING = Menu.FIRST + 1;
    private static final int MENU_SERVICES = Menu.FIRST + 2;
    private static final int MENU_TOTALAMOUNT = Menu.FIRST + 3;
    private static final int MENU_LOGOUT = Menu.FIRST + 4;
    TextView tv_cartItemCount;

   
    //========================================================================
    Bitmap myBitmap;
    //Uri picUri;
    File file;
    Uri fileUri;
    String file_path, filename;


    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 107;
//========================================================================


    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public final int MY_REQUEST_CODE = 500;
    public final int MY_REQUEST_CODE_STORAGE = 600;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Ovenues Images";


    public static GoogleApiClient mGoogleApiClient;
    public static ArrayList<String> placeID;
    public static ArrayList<String> termsList = null;
    public static double latitude, longitude;

    static ViewPagerAdapter adapter;
    public static SharedPreferences sharepref;


    public static String is_delivery, is_delivery_paid, delivery_charges, is_pickup, is_onsite_service, is_delivery_na, providerName, providerDescription, providerAddress, providerPhonenumber, providerTerms, str_guest_count_catering_CourseMenu;


    static ViewPager intro_images;
    static LinearLayout pager_indicator;
    static int dotsCount;
    static ImageView[] dots;
    static ImageViewPagerAdapter mAdapterpagerImages;
    static ArrayList<String> mImageResources = new ArrayList<String>();

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private static FrameLayout fl_imagescrrols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venue_details_main_fragment);


        activity_venueDetails = this;
        tabType = TabType.DEFAULT;//(TabType) getIntent().getSerializableExtra(TAB_TYPE);//get the type of tab

        collapse_toolbar = (CollapsingToolbarLayout) this.findViewById(R.id.collapse_toolbar);
        fl_imagescrrols = (FrameLayout) this.findViewById(R.id.fl_imagescrrols);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimaryDark));
        }

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        if (getIntent().hasExtra("venue_id")) {
            str_venue_name=getIntent().getStringExtra("venue_name");
            str_venue_id = getIntent().getStringExtra("venue_id");
            getSupportActionBar().setTitle(str_venue_name);
        }



        fmVenue = getSupportFragmentManager();

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


      /*  //=======GENERIC SERVICE PROVIDERS CHARGES RECYSLERVIEW ADDAPTER SET DATA =========================
        results_GenericPricing = new ArrayList<VenueDetailsMainFragment>();
        adapterGenericPricingAdapter = new ServiceProviderChargesDefaultAdapter(results_GenericPricing, VenueDetailsMainFragment.this);

        //=======CAtering SERVICE PROVIDERS CHARGES RECYSLERVIEW ADDAPTER SET DATA =========================
        results_CateringPricing = new ArrayList<VenueDetailsMainFragment>();
        adapterCateringPricingAdapter = new ServiceproviderVenueBothCateringPricingMenuAdapter(results_CateringPricing, VenueDetailsMainFragment.this);*/


        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // ==== VIEWPAGER SCROLLING IMAGES CODE STARTS==========
        pager_indicator = (LinearLayout) this.findViewById(R.id.viewPagerCountDots);
        intro_images = (ViewPager) this.findViewById(R.id.pager_introduction);


        mAdapterpagerImages = new ImageViewPagerAdapter(VenueDetailsMainFragment.this, mImageResources);

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


        viewPager = (ViewPager) this.findViewById(R.id.viewPager);
//        setupViewPager(viewPager);
        viewPager.setAdapter(adapter);
        new GetVenueDetails().execute();



        tabLayout = (TabLayout) this.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);//setting tab over viewpager

        //Implementing tab selected listener over tablayout
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());//setting current selected item over viewpager
                switch (tab.getPosition()) {
                    case 0:
                        //Log.e("TAG", "TAB1");
                        break;
                    case 1:
                        //Log.e("TAG", "TAB2");
                        break;
                    case 2:
                       //Log.e("TAG", "TAB3");
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


     static void setUiPageViewController() {
        dotsCount = mAdapterpagerImages.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(activity_venueDetails);
            dots[i].setImageDrawable(activity_venueDetails.getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }
        dots[0].setImageDrawable(activity_venueDetails.getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    static class UpdateTimeTask extends TimerTask {
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


    boolean enabletotalItem = true;
    boolean enabletotalAmount = true;
    ImageView ic_cart_actionbar;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        // Inflate the menu; this adds items to the action bar if it is present.
       /* getMenuInflater().inflate(R.menu.cart_option, menu);
        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.cart_badge_layout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);*/


        tv_cartItemCount.setText(sharepref.getString(Const.PREF_USER_CART_TOTAL_ITEMS, ""));

        ic_cart_actionbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VenueDetailsMainFragment.this, CartSummaryScreen.class));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // menu.clear();
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_option, menu);
        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.cart_badge_layout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);

        tv_cartItemCount = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
        tv_cartItemCount.setText(sharepref.getString(Const.PREF_USER_CART_TOTAL_ITEMS, ""));
        ic_cart_actionbar = (ImageView) notifCount.findViewById(R.id.ic_cart_actionbar);

        ic_cart_actionbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VenueDetailsMainFragment.this, CartSummaryScreen.class));

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
            case R.id.badge:
                startActivity(new Intent(VenueDetailsMainFragment.this, CartSummaryScreen.class));
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


    private VenueDetailsMainFragment.TabType tabType;

    public enum TabType {
        DEFAULT, ICON_TEXT, ICONS_ONLY, CUSTOM;
    }


    /**
     * on the basis of tab type call respective method
     **/
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


/**
 * method to set icon over tab
 **//*
    private void tabWithIcon() {
        for (int i = 0; i < tabIcons.length; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);//get tab via position
            if (tab != null)
                tab.setIcon(tabIcons[i]);//set icon
        }
    }*/

    /**
     * set custom layout over tab
     **//*
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
        new android.app.AlertDialog.Builder(VenueDetailsMainFragment.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
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

            if (placeID == null) {
                placeID = new ArrayList<String>();
            } else {
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

        Geocoder coder = new Geocoder(VenueDetailsMainFragment.this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 20);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            //Log.e("location ", location.getLatitude() + " " + location.getLongitude());
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

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
    private Uri getCaptureImageOutputUri() {
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
                file_path = getPath(this, fileUri);
                filename = file_path;// file_path.substring(file_path.lastIndexOf("/")+1);

                    /*filename = getRealPathFromUri(getBaseContext(),picUri);*/
                //file = new File(getRealPathFromUri(getBaseContext(),getPickImageResultUri(data)));
                file = new File(file_path);

            } else {

                bitmap = (Bitmap) data.getExtras().get("data");
                myBitmap = bitmap;
                imageView.setImageBitmap(myBitmap);

                fileUri = getImageUri(this, myBitmap);
                file_path = getPath(this, fileUri);
                filename = file_path.substring(file_path.lastIndexOf("/") + 1);

                file = new File(file_path);


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
     * @param data the returned data of the activity_venueDetails result
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
    private String getPath(final Context context, final Uri uri) {
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
                final String[] selectionArgs = new String[]{
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

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
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

    static String res_venue_detailsRESP;
    static ProgressDialog progressDialogVenueDetails;

    public static class GetVenueDetails extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialogVenueDetails = ProgressDialog.show(activity_venueDetails, "Loading", "Please Wait...", true, false);

        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {
                /*String upend = "city="+sharepref.getString(Const.PREF_CITY_ID,"")+"&event_type="+event_type_id+"&venue_type="+venue_type_id
                        +"&amenities="+amenty_id+"&guest_count_min="+guest_count_min+"&guest_count_max="+guest_count_max
                        +"&price_min="+price_min+"&price_max&"+price_max+"&from="+str_from+"&to="+str_to+"&sort_by="+sort_by+"&user_id="+sharepref.getString(Const.PREF_USER_ID,"");*/
                String response = post(Const.SERVER_URL_API + "mob_venue_detail_left/" + str_venue_id, "", "get");
                Log.d("URL ====", Const.SERVER_URL_API + "mob_venue_detail_left/" + str_venue_id);
                res_venue_detailsRESP = response;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res_venue_detailsRESP;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try {

                Log.i("RESPONSE venueDetails", res_venue_detailsRESP);

                JSONObject obj = new JSONObject(res_venue_detailsRESP);
                response_string = obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if (response_string.equals("success")) {

                    adapter.addFrag(new VenueOverView(),"OverView");
                    adapter.notifyDataSetChanged();

                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(res_venue_detailsRESP).getAsJsonObject();

                    String venue_name = rootObj.getAsJsonObject("message").get("venue_name").getAsString();
                    String address1 = rootObj.getAsJsonObject("message").get("address1").getAsString();
                    String address2 = rootObj.getAsJsonObject("message").get("address2").getAsString();
                    String city_name  = rootObj.getAsJsonObject("message").get("city_name").getAsString();
                    String statae = rootObj.getAsJsonObject("message").get("statae").getAsString();
                    String zipcode = rootObj.getAsJsonObject("message").get("zipcode").getAsString();
                    str_is_register=  rootObj.getAsJsonObject("message").has("is_registered")
                            ?rootObj.getAsJsonObject("message").get("is_registered").getAsString():"";
                    str_has_number= rootObj.getAsJsonObject("message").has("has_contact_num")
                            ?rootObj.getAsJsonObject("message").get("has_contact_num").getAsString():"";


                    //Log.e("venue name",venue_name);
                    tv_venue_name_overview.setText(venue_name);
                    str_venue_name = venue_name;
                    tv_venue_address.setText(address1 + "\n" + city_name);

                    String venue_type_name = rootObj.getAsJsonObject("message").get("venue_type_name").getAsString();
                    /*String max_occupancy = rootObj.getAsJsonObject("message").get("max_occupancy").getAsString();
                    String min_occupancy = rootObj.getAsJsonObject("message").get("min_occupancy").getAsString();*/


                    String minimum_occupsy = rootObj.getAsJsonObject("message").has("min_occupancy")
                            && !rootObj.getAsJsonObject("message").get("min_occupancy").isJsonNull()
                            ?rootObj.getAsJsonObject("message").get("min_occupancy").getAsString():"0";
                    String maximum_occupsy = rootObj.getAsJsonObject("message").has("max_occupancy")
                            && !rootObj.getAsJsonObject("message").get("max_occupancy").isJsonNull()
                            ?rootObj.getAsJsonObject("message").get("max_occupancy").getAsString():"0";
                    //String venue_type_name = rootObj.getAsJsonObject("message").get("venue_type_name").getAsString();
                    max_occupancy = Integer.parseInt(maximum_occupsy);
                    min_occupancy = Integer.parseInt(minimum_occupsy);



                    //==this is codee for horizontal service and
                    String str_cost_type = "", str_average_cost = "";
                    str_average_cost = rootObj.getAsJsonObject("message").get("average_cost").getAsString();
                    String cost_type = rootObj.getAsJsonObject("message").get("cost_type").getAsString();
                    if (cost_type.equalsIgnoreCase("1")) {
                        str_cost_type = "*per person";
                    } else if (cost_type.equalsIgnoreCase("2")) {
                        str_cost_type = "*per hour";
                    } else if (cost_type.equalsIgnoreCase("3")) {
                        str_cost_type = "*starting price";
                    } else {

                    }

                    if(rootObj.getAsJsonObject("message").get("charges_set").getAsString().equalsIgnoreCase("0")){
                        str_cost_type = "Request Quote !";
                        adapter.addFrag(new VenueRequestQuote(),"Request Quote");
                        adapter.notifyDataSetChanged();
                    }
                    dm_venue_venue_overview.setAllItemsInSection(singleItem_venue_overview);





                    //singleItem_venue_overview.add(new HomeHoriRecyclerSingleItem(" $ " + str_average_cost + "\n" + str_cost_type, "img/icons/o-guests01.png", "", Const.CONST_VENUE));
                    singleItem_venue_overview.add(new HomeHoriRecyclerSingleItem(/*"VENUE TYPE" + "\n" +*/ venue_type_name, Const.SERVER_URL_ONLY+"img/icons/o-venuetype01.png", "", Const.CONST_VENUE));
                    singleItem_venue_overview.add(new HomeHoriRecyclerSingleItem(/*" CAPACITY" + "\n" + */min_occupancy + "-" + max_occupancy, Const.SERVER_URL_ONLY+"img/icons/o-guests01.png", "", Const.CONST_VENUE));

                    JsonArray venue_servicesArray = rootObj.getAsJsonObject("message").has("venue_services")
                            ?rootObj.getAsJsonObject("message").getAsJsonArray("venue_services"):null;
                    JsonObject catering_icon = rootObj.getAsJsonObject("message").has("catering_icon") ?
                            rootObj.getAsJsonObject("message").get("catering_icon").getAsJsonObject() : null;
                    if (catering_icon!=null) {
                        venue_servicesArray.add(catering_icon);
                    }
                    if (venue_servicesArray != null && venue_servicesArray.size() > 0) {
                        for (int j = 0; j < venue_servicesArray.size(); j++) {
                            String service_id = venue_servicesArray.get(j).getAsJsonObject().get("service_id").getAsString();
                            String service_name = venue_servicesArray.get(j).getAsJsonObject().get("service_name").getAsString();
                            String icon_url = venue_servicesArray.get(j).getAsJsonObject().get("icon_url").getAsString();
                            singleItem_venue_overview.add(new HomeHoriRecyclerSingleItem(service_name, Const.SERVER_URL_ONLY+icon_url, service_id, Const.CONST_VENUE));
                        }

                    } else {

                        /*singleItem_venue_overview.add(new HomeHoriRecyclerSingleItem("VENUE TYPE" + "\n" + venue_type_name, "img/icons/o-venuetype01.png", "", Const.CONST_VENUE));
                        singleItem_venue_overview.add(new HomeHoriRecyclerSingleItem(" CAPACITY" + "\n" + min_occupancy + "-" + max_occupancy, "img/icons/o-guests01.png", "", Const.CONST_VENUE));*/
                    }
                    dm_venue_venue_overview.setAllItemsInSection(singleItem_venue_overview);
                    allSampleData_venuesOverviews.add(dm_venue_venue_overview);
                    adapterHoriScroll_venuesOverView.notifyDataSetChanged();

                    String description = rootObj.getAsJsonObject("message").has("description") && !rootObj.getAsJsonObject("message").get("description").isJsonNull()
                            ? rootObj.getAsJsonObject("message").get("description").getAsString():null;
                    if(description==null || description.equalsIgnoreCase("")){
                        tvTitleDescription.setVisibility(View.GONE);
                        tv_description.setVisibility(View.GONE);
                    }else{
                        tv_description.setText(description);
                    }

                    String t_and_c = !rootObj.getAsJsonObject("message").get("t_and_c").isJsonNull()
                            ? rootObj.getAsJsonObject("message").get("t_and_c").getAsString() : null;

                    if (t_and_c != null) {
                        str_t_and_c=t_and_c;
                        tv_terms.setText(t_and_c);
                    } else {
                        ll_tandC.setVisibility(View.GONE);
                        tv_terms.setText("NA");
                    }

                    JsonArray photo_urlsObj = !rootObj.getAsJsonObject("message").getAsJsonArray("photo_urls").isJsonNull()
                            ?rootObj.getAsJsonObject("message").getAsJsonArray("photo_urls").getAsJsonArray() : null;
                   // Log.e("venue's all images---",""+photo_urlsObj.size());
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
                    } else {
                        fl_imagescrrols.setVisibility(View.GONE);
                    }



                    JsonArray suitable_eventsObj = rootObj.getAsJsonObject("message")
                            .getAsJsonArray("suitable_events");


                    if (suitable_eventsObj.size() > 0) {
                        String str_suitable_for = "";

                        for (int j = 0; j < suitable_eventsObj.size(); j++) {
                            String event_type_id = suitable_eventsObj.get(j).getAsJsonObject().get("event_type_id").getAsString();
                            String event_type = suitable_eventsObj.get(j).getAsJsonObject().get("event_type").getAsString();
                            str_suitable_for = str_suitable_for + "\u25BA" + event_type + "\n\n";
                        }
                        tv_vanuesuitable.setText(str_suitable_for);
                    } else {
                        tvTitleVenueSuiteablefor.setVisibility(View.GONE);
                        tv_vanuesuitable.setVisibility(View.GONE);
                        tv_vanuesuitable.setText("NA");
                    }

                    JsonArray amenitiesObj = rootObj.getAsJsonObject("message")
                            .getAsJsonArray("amenities");

                    if (amenitiesObj.size() > 0) {
                        listamenities.clear();
                        for (int j = 0; j < amenitiesObj.size(); j++) {

                            String amenity_id = !amenitiesObj.get(j).getAsJsonObject().get("amenity_id").isJsonNull()
                                    ?amenitiesObj.get(j).getAsJsonObject().get("amenity_id").getAsString():"";
                            String amenity_name = !amenitiesObj.get(j).getAsJsonObject().get("amenity_name").isJsonNull()
                                    ?amenitiesObj.get(j).getAsJsonObject().get("amenity_name").getAsString():"NA";
                            //String amenity_icon_url=amenitiesObj.get(j).getAsJsonObject().get("amenity_icon_url").getAsString();

                            if(amenity_id!=null && amenity_name!=null){
                                listamenities.add(new IdNameUrlGrideRaw(amenity_id, amenity_name,"","", "", "", ""));
                            }

                        }
                        exp_grid_adapter.notifyDataSetChanged();
                    } else {
                        ll_amenities.setVisibility(View.GONE);
                    }

                    adapter.notifyDataSetChanged();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new GetPricingPackages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,0);
                        //new GetVenueDetailsRight().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        new GetPricingPackages().execute(0);
                       // new GetVenueDetailsRight().execute();
                    }

                } else {
                    String message = obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(activity_venueDetails.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
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


                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(activity_venueDetails);
                    alertbox.setMessage(message);
                    alertbox.setTitle("Sorry ! ");
                    alertbox.setIcon(R.mipmap.ic_launcher);

                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                   /* startActivity(new Intent(VenueDetailsMainFragment.this,MainNavigationScreen.class));
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
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            progressDialogVenueDetails.dismiss();
        }
    }

    static String res_timeslot_venue;
    public static class GetTimeSlot extends AsyncTask<Object, Void, String> {

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
                String response = post(Const.SERVER_URL_API +"timeslot_suggestions_for_date/"+str_venue_id+"/"+str_dateselected, "","get");
                //Log.d("REsponce Json====",response);
                res_timeslot_venue=response;
            }/*catch (JSONException e) {
                e.printStackTrace();
            }*/ catch (IOException e) {
                e.printStackTrace();
            }


            return res_timeslot_venue;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try{JSONObject obj = new JSONObject(res_timeslot_venue);
                //Log.i("RESPONSE", res);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){
                    tv_error_datetime.setText("");
                    tv_error_datetime.setVisibility(View.GONE);
                    sp_timeslotType_aarayList.clear();
                    sp_timeslotTypeAdapter.notifyDataSetChanged();

                    JsonParser parser = new JsonParser();

                    JsonObject rootObj = parser.parse(res_timeslot_venue).getAsJsonObject();


                    JsonArray events_typeObj = rootObj.getAsJsonArray("message");
                    sp_timeslotType_aarayList.add(new SearchVenueSpiners("0","Select Time"));
                    for (int j = 0; j < events_typeObj.size(); j++) {
                        String time=events_typeObj.get(j).getAsString();
                        time = Utils.convertDateStringToString(time,"HH:mm:ss","hh:mm aa");
                        //this is for just dyummydata set it will replace by original API comes...
                        SearchVenueSpiners  event_type_sp = new SearchVenueSpiners("" , time);
                        sp_timeslotType_aarayList.add(event_type_sp);

                    }
                    sp_timeslotTypeAdapter.notifyDataSetChanged();
                } else{

                    String message=obj.getString("message");
                    tv_error_datetime.setVisibility(View.VISIBLE);
                    tv_error_datetime.setText(message);
                    Snackbar snackbar = Snackbar
                            .make(activity_venueDetails.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
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

    static String res_venu_pricing_pkg=null;
    static ProgressDialog progressDialogpricing;
    static int is_firsttime=0;
    public static class GetPricingPackages extends AsyncTask<Integer, Void, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //pg_bar.setVisibility(View.VISIBLE);

            progressDialogpricing = ProgressDialog.show(activity_venueDetails, "Loading", "Please wait...", true, false);
        }

        @Override
        protected String doInBackground(Integer... parametros) {
            is_firsttime =  parametros[0];
            try {
                /*String response = post(Const.SERVER_URL_API+"mobile_venue_rent_charges_for_datetime?date="+str_dateselected+"&group_size="+str_guest_count+"&time_from="+str_timeslot+"&venue_id="+str_venue_id, "","get");
                Log.d("Res venues order==",Const.SERVER_URL_API+"mobile_venue_rent_charges_for_datetime?date="+str_dateselected+"&group_size="+str_guest_count+"&time_from="+str_timeslot+"&venue_id="+str_venue_id);
                */
              /*  if(str_dateselected.length()>0) {*/

              String date=str_dateselected!=null
                      ? Utils.convertDateStringToString(str_dateselected, "MM-dd-yyyy", "yyyy-MM-dd"): null,
                      timeslot=str_timeslot!=null
                              ?Utils.convertDateStringToString(str_timeslot, "hh:mm aa", "HH:mm:ss") :null;

                    String response = post(Const.SERVER_URL_API + "venue_rent_charges_for_datetime?date=" +date  + "&group_size=" + str_guest_count + "&time_from=" +timeslot + "&venue_id=" + str_venue_id, "", "get");
                    Log.d("Res venues order==", Const.SERVER_URL_API + "venue_rent_charges_for_datetime?date=" +date  + "&group_size=" + str_guest_count + "&time_from=" +timeslot + "&venue_id=" + str_venue_id);
                    res_venu_pricing_pkg = response;
                /*}else{
                    GetPricingPackages.this.cancel(true);
                }*/
                /*catch (JSONException e) {
                e.printStackTrace();
            }*/
            }catch (IOException e) {
                e.printStackTrace();
            }

            return res_venu_pricing_pkg;
        }

        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if(res_venu_pricing_pkg==null){
                progressDialogpricing.dismiss();
            }else{
                progressDialogpricing.dismiss();
            try{
                JSONObject obj = new JSONObject(res_venu_pricing_pkg);

                response_string=obj.getString("status");

                if(response_string.equals("success")){


                    JsonParser parser = new JsonParser();

                    JsonObject rootObj = parser.parse(res_venu_pricing_pkg).getAsJsonObject();
                    JsonArray price_Obj =rootObj.getAsJsonArray("message");
                    if(venueorderpriceList!=null){
                        venueorderpriceList.clear();
                        mAdapterVenuePricePKG.notifyDataSetChanged();
                    }
                    //Log.e("pricing size",""+price_Obj.size());

                    if(price_Obj.size()>0 && is_firsttime==0){
                        adapter.addFrag(new VenuePricingPlans(),"Bookings");
                        adapter.notifyDataSetChanged();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new GetVenueDetailsRight().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            new GetVenueDetailsRight().execute();
                        }
                    }


                    for (int j = 0; j < price_Obj.size(); j++) {


                        String id = !price_Obj.get(j).getAsJsonObject().get("id").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("id").getAsString():null;

                        String package_name = !price_Obj.get(j).getAsJsonObject().get("package_name").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("package_name").getAsString():null;

                        String charges = !price_Obj.get(j).getAsJsonObject().get("charges").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("charges").getAsString():null;


                        String week_days =  !price_Obj.get(j).getAsJsonObject().get("week_days").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("week_days").getAsString():null;
                        String  time_from =  !price_Obj.get(j).getAsJsonObject().get("time_from").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("time_from").getAsString():null;
                        String time_to =  !price_Obj.get(j).getAsJsonObject().get("time_to").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("time_to").getAsString():null;

                        String is_flat_charges = !price_Obj.get(j).getAsJsonObject().get("is_flat_charges").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("is_flat_charges").getAsString():null;

                        String is_perperson_charges = !price_Obj.get(j).getAsJsonObject().get("is_perperson_charges").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("is_perperson_charges").getAsString():null;

                        String is_group_charges = !price_Obj.get(j).getAsJsonObject().get("is_group_charges").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("is_group_charges").getAsString():null;

                        String group_size_from= !price_Obj.get(j).getAsJsonObject().get("group_size_from").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("group_size_from").getAsString():null;

                        String group_size_to= !price_Obj.get(j).getAsJsonObject().get("group_size_to").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("group_size_to").getAsString():null;

                        String is_extra_person_charges = !price_Obj.get(j).getAsJsonObject().get("is_extra_person_charges").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("is_extra_person_charges").getAsString():null;

                        String extra_person_charges = !price_Obj.get(j).getAsJsonObject().get("extra_person_charges").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("extra_person_charges").getAsString():null;

                        String chrages_inclusion = !price_Obj.get(j).getAsJsonObject().get("chrages_inclusion").isJsonNull()
                                ?price_Obj.get(j).getAsJsonObject().get("chrages_inclusion").getAsString():null;

                        String is_perhour_charges = !price_Obj.get(j).getAsJsonObject().get("is_perhour_charges").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("is_perhour_charges").getAsString():null;

                        String pacakage_hours = !price_Obj.get(j).getAsJsonObject().get("pacakage_hours").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("pacakage_hours").getAsString():null;

                        String is_hour_extension_charges = !price_Obj.get(j).getAsJsonObject().get("is_hour_extension_charges").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("is_hour_extension_charges").getAsString():null;

                        String  hour_extension_charges= !price_Obj.get(j).getAsJsonObject().get("hour_extension_charges").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("hour_extension_charges").getAsString():null;

                        String extension_hours  = !price_Obj.get(j).getAsJsonObject().get("extension_hours").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("extension_hours").getAsString():null;

                        String  is_applicable= !price_Obj.get(j).getAsJsonObject().get("is_applicable").isJsonNull()
                                ? price_Obj.get(j).getAsJsonObject().get("is_applicable").getAsString():null;

                        venueorderpriceList.add(new VenueOrderPriceModel(id,package_name,charges,week_days,time_from ,time_to ,is_flat_charges,is_perperson_charges , is_group_charges,group_size_from,group_size_to,is_extra_person_charges ,extra_person_charges,chrages_inclusion ,
                                is_perhour_charges,pacakage_hours ,is_hour_extension_charges,hour_extension_charges ,extension_hours,is_applicable,false));
                       /* if(is_applicable.equalsIgnoreCase("1")){
                            //Log.i("order price ", price_Obj.get(j).toString());
                            venueorderpriceList.add(new VenueOrderPriceModel(id,package_name,charges ,is_flat_charges,is_perperson_charges , is_group_charges,group_size_from,group_size_to,is_extra_person_charges ,extra_person_charges,chrages_inclusion ,
                                    is_perhour_charges,pacakage_hours ,is_hour_extension_charges,hour_extension_charges ,extension_hours,is_applicable,false));
                        }*/
                    }
                    adapter.notifyDataSetChanged();
                    mAdapterVenuePricePKG.notifyDataSetChanged();

                   /* if(venueorderpriceList.size()>0){
                        ovenues.com.ovenue.fragments.VenuesFragments.FragmentPricing.tv_menutitle_venuepricing.setText("Pricing Plans");
                        ovenues.com.ovenue.fragments.VenuesFragments.FragmentPricing.tv_menutitle_venuepricing.setTextSize(15);
                    }*/


                } else{

                    if(str_dateselected==null ||str_guest_count==null || str_timeslot==null){

                    }else {
                        String message = obj.getString("message");
                        if (message.equalsIgnoreCase("Rent charges not found for given venue, date and time.")) {

                        } else {
                            Snackbar snackbar = Snackbar
                                    .make(activity_venueDetails.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
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
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                progressDialogpricing.dismiss();
            }
            }
        }
    }

    static String res_evnent;
    public static class GetEventTyesVenues extends AsyncTask<Object, Void, String> {

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
                res_evnent=response;
            }catch (IOException e) {
                e.printStackTrace();
            }
            return res_evnent;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try{JSONObject obj = new JSONObject(res_evnent);
                Log.i("RESPONSE", res_evnent);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    JsonParser parser = new JsonParser();

                    JsonObject rootObj = parser.parse(res_evnent).getAsJsonObject();

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
                            .make(activity_venueDetails.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
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

    static String res_venueDetailsRight;
    static ProgressDialog progressDialogvenueRight;
    public static class GetVenueDetailsRight extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialogvenueRight = ProgressDialog.show(activity_venueDetails, "Loading", "Please Wait...", true, false);

        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {
                /*String upend = "city="+sharepref.getString(Const.PREF_CITY_ID,"")+"&event_type="+event_type_id+"&venue_type="+venue_type_id
                        +"&amenities="+amenty_id+"&guest_count_min="+guest_count_min+"&guest_count_max="+guest_count_max
                        +"&price_min="+price_min+"&price_max&"+price_max+"&from="+str_from+"&to="+str_to+"&sort_by="+sort_by+"&user_id="+sharepref.getString(Const.PREF_USER_ID,"");*/
                String response = post(Const.SERVER_URL_API +"mob_venue_detail_right/"+str_venue_id, "","get");
                //Log.d("URL ====",Const.SERVER_URL_API+"mob_venue_detail_right/"+str_venue_id);
                res_venueDetailsRight=response;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res_venueDetailsRight;
        }

        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            progressDialogvenueRight.dismiss();

            try{

                // Log.i("RESPONSE", res);
                JSONObject obj = new JSONObject(res_venueDetailsRight);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")) {

                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(res_venueDetailsRight).getAsJsonObject();

                    String venue_name= rootObj.getAsJsonObject("message").get("venue_name").getAsString();
                    String address1 = rootObj.getAsJsonObject("message").get("address1").getAsString();
                    String address2 = rootObj.getAsJsonObject("message").get("address2").getAsString();
                    String statae = rootObj.getAsJsonObject("message").get("statae").getAsString();
                    String zipcode = rootObj.getAsJsonObject("message").get("zipcode").getAsString();

                    tv_venue_address.setText(address1 + "\n" + address2 + " , " + statae + " - " + zipcode);

                    //==this is codee for horizontal service and
                    String str_cost_type = "", str_average_cost = "";
                    str_average_cost = rootObj.getAsJsonObject("message").get("average_cost").getAsString();
                    String cost_type = rootObj.getAsJsonObject("message").get("cost_type").getAsString();
                    if (cost_type.equalsIgnoreCase("1")) {
                        str_cost_type = "*per person";
                    } else if (cost_type.equalsIgnoreCase("2")) {
                        str_cost_type = "*per hour";
                    } else if (cost_type.equalsIgnoreCase("3")) {
                        str_cost_type = "*starting price";
                    } else {
                        str_cost_type = "Request Quote !";
                    }

                    JsonArray venue_servicesObj = rootObj.getAsJsonObject("message")
                            .getAsJsonArray("venue_services");
                    if (venue_servicesObj != null && venue_servicesObj.size() > 0) {

                    } else {
                    }


                    String t_and_c = !rootObj.getAsJsonObject("message").get("t_and_c").isJsonNull()
                            ? rootObj.getAsJsonObject("message").get("t_and_c").getAsString() : null;


                    /*//===FOOD FRAGMENT DATA==============================================================================
                    JsonArray array_restaurant_menu = !rootObj.getAsJsonObject("message").getAsJsonArray("restaurant_menu").isJsonNull()
                            ? rootObj.getAsJsonObject("message").getAsJsonArray("restaurant_menu") : null;
                    venueorderfoodList.clear();
                    for (int j = 0; j < array_restaurant_menu.size(); j++) {

                        String menu_id = array_restaurant_menu.get(j).getAsJsonObject().get("menu_id").getAsString();
                        String menu_desc = array_restaurant_menu.get(j).getAsJsonObject().get("menu_desc").getAsString();
                        //String price_per_plate = array_restaurant_menu.get(k).getAsJsonObject().get("price_per_plate").getAsString();
                        venueorderfoodList.add(new HeaderItem( menu_id,menu_desc,"","","","",0));

                        JsonArray array_item_menu = !array_restaurant_menu.get(j).getAsJsonObject().getAsJsonArray("items").isJsonNull()
                                ? array_restaurant_menu.get(j).getAsJsonObject().getAsJsonArray("items") : null;

                        if (!array_item_menu.isJsonNull() && array_item_menu.size() > 0) {

                            for (int k = 0; k < array_item_menu.size(); k++) {

                                String item_id = array_item_menu.get(k).getAsJsonObject().get("item_id").getAsString();
                                String item_name = array_item_menu.get(k).getAsJsonObject().get("item_name").getAsString();
                                String price_per_plate = array_item_menu.get(k).getAsJsonObject().get("price_per_plate").getAsString();
                                // items.add(new Item("Item at " + i, "Item description at " + i));
                                venueorderfoodList.add(new VenueOrderFoodMenuModel(menu_id,menu_desc,item_id,item_name,"" ,price_per_plate,1));
                            }

                            venueorderfoodAdapter.notifyDataSetChanged();
                            *//*if(venueorderfoodList.size()>0){
                                ovenues.com.ovenue.fragments.VenuesFragments.FragmentCatering.tv_venue_foodpkg_menutitle.setText("Food Items");
                                ovenues.com.ovenue.fragments.VenuesFragments.FragmentCatering.tv_venue_foodpkg_menutitle.setTextSize(15);
                            }*//*

                        }
                    }

                    //====CATERING MENU OPTIOIN DATA====================================================================
                    JsonArray array_food_menu_options = !rootObj.getAsJsonObject("message").getAsJsonArray("food_menu_options").isJsonNull()
                            ? rootObj.getAsJsonObject("message").getAsJsonArray("food_menu_options"):null;
                    cateringFoodList.clear();
                    if(!array_food_menu_options.isJsonNull() && array_food_menu_options.size()>0){

                        for (int j = 0; j < array_food_menu_options.size(); j++) {

                            String menu_id = array_food_menu_options.get(j).getAsJsonObject().get("menu_id").getAsString();
                            String price_per_plate = array_food_menu_options.get(j).getAsJsonObject().get("price_per_plate").getAsString();
                            String menu_desc = array_food_menu_options.get(j).getAsJsonObject().get("menu_desc").getAsString();
                            String total_courses = array_food_menu_options.get(j).getAsJsonObject().get("total_courses").getAsString();

                            String is_group_size=  !array_food_menu_options.get(j).getAsJsonObject().get("is_group_size").isJsonNull()
                                    ? array_food_menu_options.get(j).getAsJsonObject().get("is_group_size").getAsString() : null;
                            String group_size_from =!array_food_menu_options.get(j).getAsJsonObject().get("group_size_from").isJsonNull()
                                    ? array_food_menu_options.get(j).getAsJsonObject().get("group_size_from").getAsString(): null;
                            String group_size_to = !array_food_menu_options.get(j).getAsJsonObject().get("group_size_to").isJsonNull()
                                    ? array_food_menu_options.get(j).getAsJsonObject().get("group_size_to").getAsString(): null;

                            cateringFoodList.add(new FoodMenuModel(menu_id,menu_desc,total_courses ,price_per_plate,is_group_size ,group_size_from ,group_size_to));
                        }
                        cateringFoodadapter.notifyDataSetChanged();
                        *//*if(cateringFoodList.size()>0){
                            ovenues.com.ovenue.fragments.VenuesFragments.VenueRestaurantMenu.tv_venue_food_menutitle.setText("Food Menu");
                            ovenues.com.ovenue.fragments.VenuesFragments.VenueRestaurantMenu.tv_venue_food_menutitle.setTextSize(15);
                        }*//*
                    }

                    //==================BEVERAGES DATA=======================
                    JsonArray beverages_Obj =!rootObj.getAsJsonObject("message").getAsJsonArray("baverage_options").isJsonNull()
                            ? rootObj.getAsJsonObject("message").getAsJsonArray("baverage_options"):null;
                    venueorderbeveragesList.clear();
                    for (int j = 0; j < beverages_Obj.size(); j++) {


                        String beverage_id = !beverages_Obj.get(j).getAsJsonObject().get("beverage_id").isJsonNull()
                                ? beverages_Obj.get(j).getAsJsonObject().get("beverage_id").getAsString() : null;
                        String venue_id = !beverages_Obj.get(j).getAsJsonObject().get("venue_id").isJsonNull()
                                ? beverages_Obj.get(j).getAsJsonObject().get("venue_id").getAsString() : null;
                        String option_desc = !beverages_Obj.get(j).getAsJsonObject().get("option_desc").isJsonNull()
                                ? beverages_Obj.get(j).getAsJsonObject().get("option_desc").getAsString() : null;
                        String option_charges = !beverages_Obj.get(j).getAsJsonObject().get("option_charges").isJsonNull()
                                ? beverages_Obj.get(j).getAsJsonObject().get("option_charges").getAsString() : null;
                        String is_flat_charges = !beverages_Obj.get(j).getAsJsonObject().get("is_flat_charges").isJsonNull()
                                ? beverages_Obj.get(j).getAsJsonObject().get("is_flat_charges").getAsString() : null;
                        String is_per_person_charges = !beverages_Obj.get(j).getAsJsonObject().get("is_per_person_charges").isJsonNull()
                                ? beverages_Obj.get(j).getAsJsonObject().get("is_per_person_charges").getAsString() : null;
                        String is_per_hour_charges = !beverages_Obj.get(j).getAsJsonObject().get("is_per_hour_charges").isJsonNull()
                                ? beverages_Obj.get(j).getAsJsonObject().get("is_per_hour_charges").getAsString() : null;
                        String is_hour_extn_changes = !beverages_Obj.get(j).getAsJsonObject().get("is_hour_extn_changes").isJsonNull()
                                ? beverages_Obj.get(j).getAsJsonObject().get("is_hour_extn_changes").getAsString() : null;
                        String extension_charges = !beverages_Obj.get(j).getAsJsonObject().get("extension_charges").isJsonNull()
                                ? beverages_Obj.get(j).getAsJsonObject().get("extension_charges").getAsString() : null;
                        String is_group_size = !beverages_Obj.get(j).getAsJsonObject().get("is_group_size").isJsonNull()
                                ? beverages_Obj.get(j).getAsJsonObject().get("is_group_size").getAsString() : null;
                        String group_size_from = !beverages_Obj.get(j).getAsJsonObject().get("group_size_from").isJsonNull()
                                ? beverages_Obj.get(j).getAsJsonObject().get("group_size_from").getAsString() : null;
                        String group_size_to = !beverages_Obj.get(j).getAsJsonObject().get("group_size_to").isJsonNull()
                                ? beverages_Obj.get(j).getAsJsonObject().get("group_size_to").getAsString() : null;

                        venueorderbeveragesList.add(new VenueOrderBeveragesModel( beverage_id,venue_id,option_desc ,option_charges,is_flat_charges ,
                                is_per_person_charges,is_per_hour_charges ,is_hour_extn_changes,extension_charges ,is_group_size,group_size_from ,group_size_to,false));

                    } mAdapterBeverages.notifyDataSetChanged();

                    *//*if(venueorderbeveragesList.size()>0){
                        ovenues.com.ovenue.fragments.VenuesFragments.FragmentBeverages.tv_venue_beverages_menutitle.setText("Beverages");
                        ovenues.com.ovenue.fragments.VenuesFragments.FragmentBeverages.tv_venue_beverages_menutitle.setTextSize(15);
                    }*//*
*/



                    food_menu = !rootObj.getAsJsonObject("message").getAsJsonArray("food_menu_options").isJsonNull()
                            ? rootObj.getAsJsonObject("message").getAsJsonArray("food_menu_options"):null;
                    restaurant_menu = !rootObj.getAsJsonObject("message").getAsJsonArray("restaurant_menu").isJsonNull()
                            ? rootObj.getAsJsonObject("message").getAsJsonArray("restaurant_menu"):null;
                    beveragesJSONVenues = !rootObj.getAsJsonObject("message").getAsJsonArray("baverage_options").isJsonNull()
                            ? rootObj.getAsJsonObject("message").getAsJsonArray("baverage_options"):null;

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
                           // Log.d("is groupsize",""+is_group_size);
                            results_CateringPricingVenue.add(new ServiceproviderCateringPricingMenuModel(menu_id,menu_desc,"1",true));

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
                                results_CateringPricingVenue.add(new ServiceproviderCateringPricingMenuModel(menu_id,menu_desc,"2",true));
                            }
                        }
                    }

                    if(!beveragesJSONVenues.isJsonNull() && beveragesJSONVenues.size()>0){

                        for (int j = 0; j < beveragesJSONVenues.size(); j++) {
                            String beverage_id = beveragesJSONVenues.get(j).getAsJsonObject().get("beverage_id").getAsString();
                            String option_desc = beveragesJSONVenues.get(j).getAsJsonObject().get("option_desc").getAsString();
                            String option_charges = beveragesJSONVenues.get(j).getAsJsonObject().get("option_charges").getAsString();
                            String is_group_size = !beveragesJSONVenues.get(j).getAsJsonObject().get("is_group_size").isJsonNull()
                                    ? beveragesJSONVenues.get(j).getAsJsonObject().get("is_group_size").getAsString() : null;
                            String group_size_from = !beveragesJSONVenues.get(j).getAsJsonObject().get("group_size_from").isJsonNull()
                                    ? beveragesJSONVenues.get(j).getAsJsonObject().get("group_size_from").getAsString() : null;
                            String group_size_to = !beveragesJSONVenues.get(j).getAsJsonObject().get("group_size_to").isJsonNull()
                                    ? beveragesJSONVenues.get(j).getAsJsonObject().get("group_size_to").getAsString() : null;

                            //results_CateringPricing.add(new ServiceproviderCateringPricingMenuModel(beverage_id,option_desc,"3"));
                        }
                        results_CateringPricingVenue.add(new ServiceproviderCateringPricingMenuModel("0","Beverages","3",true));
                    }
                    if(food_menu.size()<1 &&
                            restaurant_menu.size()<1 &&
                            beveragesJSONVenues.size()<1){
                        img_foodbev.setVisibility(View.GONE);
                        rv_foodbev.setVisibility(View.GONE);
                    }else{
                        adapterCateringPricingAdapterVenue.notifyDataSetChanged();
                    }




                    //=====================VENEUS SERVICES DATA==========================================
                    JsonArray venue_services_Obj =rootObj.getAsJsonObject("message").getAsJsonArray("venue_services");
                    venueserviceList.clear();
                    if(venue_services_Obj!=null) {
                        for (int j = 0; j < venue_services_Obj.size(); j++) {

                            String id = !venue_services_Obj.get(j).getAsJsonObject().get("id").isJsonNull()
                                    ? venue_services_Obj.get(j).getAsJsonObject().get("id").getAsString() : null;
                            String service_id = !venue_services_Obj.get(j).getAsJsonObject().get("service_id").isJsonNull()
                                    ? venue_services_Obj.get(j).getAsJsonObject().get("service_id").getAsString() : null;
                            String venue_id = !venue_services_Obj.get(j).getAsJsonObject().get("venue_id").isJsonNull()
                                    ? venue_services_Obj.get(j).getAsJsonObject().get("venue_id").getAsString() : null;
                            String service_desc = !venue_services_Obj.get(j).getAsJsonObject().get("service_desc").isJsonNull()
                                    ? venue_services_Obj.get(j).getAsJsonObject().get("service_desc").getAsString() : null;
                            String service_charges = !venue_services_Obj.get(j).getAsJsonObject().get("service_charges").isJsonNull()
                                    ? venue_services_Obj.get(j).getAsJsonObject().get("service_charges").getAsString() : null;
                            String service_name = !venue_services_Obj.get(j).getAsJsonObject().get("service_name").isJsonNull()
                                    ? venue_services_Obj.get(j).getAsJsonObject().get("service_name").getAsString() : null;
                            String has_options = !venue_services_Obj.get(j).getAsJsonObject().get("has_options").isJsonNull()
                                    ? venue_services_Obj.get(j).getAsJsonObject().get("has_options").getAsString() : null;


                            JsonArray venue_servicesarray_Obj = !venue_services_Obj.get(j).getAsJsonObject().get("service_options").getAsJsonArray().isJsonNull()
                                    ? venue_services_Obj.get(j).getAsJsonObject().get("service_options").getAsJsonArray() : null;
                            venueserviceList.add(new HeaderItemVenueOrderServices(service_id, service_desc, service_name, "", "", "", "", "", "", "", "", "", "", "", "", "", 0, false));

                            if(venue_services_Obj!=null) {
                                for (int k = 0; k < venue_servicesarray_Obj.size(); k++) {


                                    String service_option_id = !venue_servicesarray_Obj.get(k).getAsJsonObject().get("service_option_id").isJsonNull()
                                            ? venue_servicesarray_Obj.get(k).getAsJsonObject().get("service_option_id").getAsString() : null;

                                    String optiondesc = !venue_servicesarray_Obj.get(k).getAsJsonObject().get("option_desc").isJsonNull()
                                            ? venue_servicesarray_Obj.get(k).getAsJsonObject().get("option_desc").getAsString() : null;

                                    String option_charges = !venue_servicesarray_Obj.get(k).getAsJsonObject().get("option_charges").isJsonNull()
                                            ? venue_servicesarray_Obj.get(k).getAsJsonObject().get("option_charges").getAsString() : null;

                                    String option_details = !venue_servicesarray_Obj.get(k).getAsJsonObject().get("option_details").isJsonNull()
                                            ? venue_servicesarray_Obj.get(k).getAsJsonObject().get("option_details").getAsString() : null;

                                    String is_flat_charges = !venue_servicesarray_Obj.get(k).getAsJsonObject().get("is_flat_charges").isJsonNull()
                                            ? venue_servicesarray_Obj.get(k).getAsJsonObject().get("is_flat_charges").getAsString() : null;

                                    String is_per_person_charges = !venue_servicesarray_Obj.get(k).getAsJsonObject().get("is_per_person_charges").isJsonNull()
                                            ? venue_servicesarray_Obj.get(k).getAsJsonObject().get("is_per_person_charges").getAsString() : null;

                                    String is_per_hour_charges = !venue_servicesarray_Obj.get(k).getAsJsonObject().get("is_per_hour_charges").isJsonNull()
                                            ? venue_servicesarray_Obj.get(k).getAsJsonObject().get("is_per_hour_charges").getAsString() : null;
                                    String is_hour_extn_changes = !venue_servicesarray_Obj.get(k).getAsJsonObject().get("is_hour_extn_changes").isJsonNull()
                                            ? venue_servicesarray_Obj.get(k).getAsJsonObject().get("is_hour_extn_changes").getAsString() : null;

                                    String extension_charges = !venue_servicesarray_Obj.get(k).getAsJsonObject().get("extension_charges").isJsonNull()
                                            ? venue_servicesarray_Obj.get(k).getAsJsonObject().get("extension_charges").getAsString() : null;

                                    String is_group_size = !venue_servicesarray_Obj.get(k).getAsJsonObject().get("is_group_size").isJsonNull()
                                            ? venue_servicesarray_Obj.get(k).getAsJsonObject().get("is_group_size").getAsString() : null;

                                    String group_size_from = !venue_servicesarray_Obj.get(k).getAsJsonObject().get("group_size_from").isJsonNull()
                                            ? venue_servicesarray_Obj.get(k).getAsJsonObject().get("group_size_from").getAsString() : null;
                                    String group_size_to = !venue_servicesarray_Obj.get(k).getAsJsonObject().get("group_size_to").isJsonNull()
                                            ? venue_servicesarray_Obj.get(k).getAsJsonObject().get("group_size_to").getAsString() : null;


                                    venueserviceList.add(new VenueOrderServiceModel(service_id, service_desc, service_name, service_option_id, optiondesc, option_charges, option_details, is_flat_charges, is_per_person_charges, is_per_hour_charges, is_hour_extn_changes, extension_charges, is_group_size, group_size_from,
                                            group_size_to, "0", 1, false));
                                    //mAdapterVenueServices.notifyDataSetChanged();

                                }
                            }
                        }
                    }

                    //====VENUES EXTRA SERVICES DATTA===================
                    JsonArray venue_extra_services =rootObj.getAsJsonObject("message").has("venue_extra_services")
                            ?rootObj.getAsJsonObject("message").getAsJsonArray("venue_extra_services") : null;

                    if(venue_extra_services!=null){
                    for (int j = 0; j < venue_extra_services.size(); j++) {

                        String extra_service_id = !venue_extra_services.get(j).getAsJsonObject().get("extra_service_id").isJsonNull()
                                ? venue_extra_services.get(j).getAsJsonObject().get("extra_service_id").getAsString() : null;
                        String venue_id = !venue_extra_services.get(j).getAsJsonObject().get("venue_id").isJsonNull()
                                ? venue_extra_services.get(j).getAsJsonObject().get("venue_id").getAsString() : null;
                        String extra_service_name = !venue_extra_services.get(j).getAsJsonObject().get("extra_service_name").isJsonNull()
                                ? venue_extra_services.get(j).getAsJsonObject().get("extra_service_name").getAsString() : null;
                        String extra_service_desc = !venue_extra_services.get(j).getAsJsonObject().get("extra_service_desc").isJsonNull()
                                ? venue_extra_services.get(j).getAsJsonObject().get("extra_service_desc").getAsString() : null;
                        String extra_service_charges = !venue_extra_services.get(j).getAsJsonObject().get("extra_service_charges").isJsonNull()
                                ? venue_extra_services.get(j).getAsJsonObject().get("extra_service_charges").getAsString() : null;
                        String has_options = !venue_extra_services.get(j).getAsJsonObject().get("has_options").isJsonNull()
                                ? venue_extra_services.get(j).getAsJsonObject().get("has_options").getAsString() : null;

                        venueserviceList.add(new HeaderItemVenueOrderServices(extra_service_id,extra_service_desc, extra_service_name, "", "", "", "", "", "", "", "", "", "", "", "","",0,false));

                        JsonArray venue_extra_service_options_Obj = venue_extra_services.get(j).getAsJsonObject().has("extra_service_options")
                               && !venue_extra_services.get(j).getAsJsonObject().get("extra_service_options").getAsJsonArray().isJsonNull()
                                ? venue_extra_services.get(j).getAsJsonObject().get("extra_service_options").getAsJsonArray() : null;

                        if(venue_extra_service_options_Obj!=null) {
                            for (int k = 0; k < venue_extra_service_options_Obj.size(); k++) {
                                // Log.d("ext services ==",""+venue_extra_service_options_Obj.size());

                                String extra_service_option_id = !venue_extra_service_options_Obj.get(k).getAsJsonObject().get("extra_service_option_id").isJsonNull()
                                        ? venue_extra_service_options_Obj.get(k).getAsJsonObject().get("extra_service_option_id").getAsString() : null;

                                String option_desc = !venue_extra_service_options_Obj.get(k).getAsJsonObject().get("option_desc").isJsonNull()
                                        ? venue_extra_service_options_Obj.get(k).getAsJsonObject().get("option_desc").getAsString() : null;

                                String option_charges = !venue_extra_service_options_Obj.get(k).getAsJsonObject().get("option_charges").isJsonNull()
                                        ? venue_extra_service_options_Obj.get(k).getAsJsonObject().get("option_charges").getAsString() : null;

                                String option_details = !venue_extra_service_options_Obj.get(k).getAsJsonObject().get("option_details").isJsonNull()
                                        ? venue_extra_service_options_Obj.get(k).getAsJsonObject().get("option_details").getAsString() : null;

                                String is_flat_charges = !venue_extra_service_options_Obj.get(k).getAsJsonObject().get("is_flat_charges").isJsonNull()
                                        ? venue_extra_service_options_Obj.get(k).getAsJsonObject().get("is_flat_charges").getAsString() : null;

                                String is_per_person_charges = !venue_extra_service_options_Obj.get(k).getAsJsonObject().get("is_per_person_charges").isJsonNull()
                                        ? venue_extra_service_options_Obj.get(k).getAsJsonObject().get("is_per_person_charges").getAsString() : null;

                                String is_per_hour_charges = !venue_extra_service_options_Obj.get(k).getAsJsonObject().get("is_per_hour_charges").isJsonNull()
                                        ? venue_extra_service_options_Obj.get(k).getAsJsonObject().get("is_per_hour_charges").getAsString() : null;
                                String is_hour_extn_changes = !venue_extra_service_options_Obj.get(k).getAsJsonObject().get("is_hour_extn_changes").isJsonNull()
                                        ? venue_extra_service_options_Obj.get(k).getAsJsonObject().get("is_hour_extn_changes").getAsString() : null;

                                String extension_charges = !venue_extra_service_options_Obj.get(k).getAsJsonObject().get("extension_charges").isJsonNull()
                                        ? venue_extra_service_options_Obj.get(k).getAsJsonObject().get("extension_charges").getAsString() : null;

                                String is_group_size = !venue_extra_service_options_Obj.get(k).getAsJsonObject().get("is_group_size").isJsonNull()
                                        ? venue_extra_service_options_Obj.get(k).getAsJsonObject().get("is_group_size").getAsString() : null;

                                String group_size_from = !venue_extra_service_options_Obj.get(k).getAsJsonObject().get("group_size_from").isJsonNull()
                                        ? venue_extra_service_options_Obj.get(k).getAsJsonObject().get("group_size_from").getAsString() : null;
                                String group_size_to = !venue_extra_service_options_Obj.get(k).getAsJsonObject().get("group_size_to").isJsonNull()
                                        ? venue_extra_service_options_Obj.get(k).getAsJsonObject().get("group_size_to").getAsString() : null;


                                venueserviceList.add(new VenueOrderServiceModel(extra_service_id, extra_service_desc, extra_service_name, extra_service_option_id,
                                        option_desc, option_charges, option_details, is_flat_charges, is_per_person_charges, is_per_hour_charges, is_hour_extn_changes,
                                        extension_charges, is_group_size, group_size_from, group_size_to, "1", 1, false));
                            }
                        }

                    }
                }

                    if(venueserviceList.size()<1){
                        img_service.setVisibility(View.GONE);
                        recyclerview_venueService.setVisibility(View.GONE);
                    }else{
                        mAdapterVenueServices.notifyDataSetChanged();
                    }



                }else{
                    String message=obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(activity_venueDetails.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
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


                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(activity_venueDetails);
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
            progressDialogvenueRight.dismiss();
        }
    }


    static String res_venue_pricing_package;
    static ProgressDialog progressDialogAddVenuePricing;
    public static class AddVenuePricingPackage extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";
        String booking_time_from,booking_time_to;

        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //Log.d("pre execute", "Pre execute Done");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialogAddVenuePricing = ProgressDialog.show(activity_venueDetails, "Loading", "Please wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");
            //Log.d("post execute", "Executando doInBackground   ingredients");

            try {
                JSONObject req = new JSONObject();

                if(sharepref.getString(Const.PREF_USER_ID,"").equalsIgnoreCase("0") ||
                        sharepref.getString(Const.PREF_USER_ID,"")==null){
                    req.put("user_id",sharepref.getString(Const.PREF_USER_ID,"0"));
                    req.put("token",sharepref.getString(Const.PREF_USER_TOKEN,"0"));
                }else{
                    req.put("user_id",sharepref.getString(Const.PREF_USER_ID,"0"));
                }



                req.put("service_id","0");
                req.put("service_provider_id","0");
                req.put("charge_id",str_venue_id);
                //req.put("charge_id",);
                req.put("flag","1");


                JSONObject pricing_packages = new JSONObject();

                SimpleDateFormat MDY_hmA = new SimpleDateFormat("MM-dd-yyyy hh:mm aa");
                try {

                    String[] separated = str_venue_charge_duration.split(":");
                    String hr  = separated[0].trim(); // this will contain "HOUR"
                    String min = separated[1].trim(); // this will contain " minutes"
                    String sec = separated[2].trim(); // this will contain " seconds"

                    Date date_time_from = MDY_hmA.parse(str_dateselected +" "+str_timeslot);
                    //System.out.println(date_time_from);

                    Calendar calendarAdd = Calendar.getInstance();
                    calendarAdd.setTime(date_time_from);
                    calendarAdd.add(Calendar.HOUR, Integer.parseInt(hr));
                    calendarAdd.add(Calendar.MINUTE,Integer.parseInt(min));
                    calendarAdd.add(Calendar.SECOND,Integer.parseInt(sec));

                    booking_time_from = MDY_hmA.format(date_time_from);
                    booking_time_to=MDY_hmA.format(calendarAdd.getTime());

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                pricing_packages.put("venue_id",str_venue_id);
                pricing_packages.put("package_id",str_price_pkg_id);
                pricing_packages.put("no_of_hours",str_no_of_hour);
                pricing_packages.put("number_of_guests",str_guest_count);
                pricing_packages.put("booking_date",Utils.convertDateStringToString(str_dateselected,"MM-dd-yyyy","yyyy-MM-dd"));
                pricing_packages.put("booking_time_from",Utils.convertDateStringToString(booking_time_from,"MM-dd-yyyy hh:mm aa","yyyy-MM-dd HH:mm:ss"));
                pricing_packages.put("booking_time_to",Utils.convertDateStringToString(booking_time_to,"MM-dd-yyyy hh:mm aa","yyyy-MM-dd HH:mm:ss"));
                pricing_packages.put("booking_rent",str_venue_charge);
                pricing_packages.put("total_amount",final_total);

                req.put("pricing_packages",pricing_packages);

              //  Log.d("Venue pkg req JSON---", req.toString()+"\n"+Const.SERVER_URL_API +"cart_venue");


                String response = post(Const.SERVER_URL_API +"cart_venue", req.toString(),"put");//post("http://54.153.127.215/api/login", req.toString());
               // Log.d("Venue pkg resp Json====",response);
                res_venue_pricing_package = response;
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return res_venue_pricing_package;

        }
        @Override
        protected void onPostExecute(String result)
        { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            super.onPostExecute(result);
        }
            //System.out.println("OnpostExecute----done-------");
            progressDialogAddVenuePricing.dismiss();

            if (res_venue_pricing_package == null || res_venue_pricing_package.equals("")) {

                progressDialogAddVenuePricing.dismiss();
                return;
            }
            try {
                JSONObject obj = new JSONObject(res_venue_pricing_package);
                //Log.i("RESPONSE=venue priceing", res_venue_pricing_package);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.

                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(res_venue_pricing_package).getAsJsonObject();

                    String cart_id = rootObj.getAsJsonObject("message").get("cart_id").getAsString();
                    String flag = rootObj.getAsJsonObject("message").get("flag").getAsString();
                    str_cart_id = cart_id;
                    venue_cart_flag =flag;

                    Snackbar snackbar = Snackbar
                            .make(activity_venueDetails.findViewById(android.R.id.content), "Package Added to cart.", Snackbar.LENGTH_LONG)
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

                    activity_venueDetails.invalidateOptionsMenu();

                    new GetGenCartDetails().execute();

                } else{
                    String message=obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(activity_venueDetails.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
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
            progressDialogAddVenuePricing.dismiss();
        }

        @Override
        protected void onCancelled() {
        }
    }


    static String res_cart;
    public static class GetGenCartDetails extends AsyncTask<Object, Void, String> {

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

           // Log.i("RESPONSE GEN Cart", res_cart);
            try{
                JSONObject obj = new JSONObject(res_cart);

                response_string=obj.getString("status");

                if(response_string.equals("success")){
                    activity_venueDetails.invalidateOptionsMenu();

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

                   /* tv_cartItemCount.setText(sharepref.getString(Const.PREF_USER_CART_TOTAL_ITEMS,""));
                    if(tv_cartItemCount.getText().length()==0
                            ||tv_cartItemCount.getText()==null){
                        tv_cartItemCount.setVisibility(View.GONE);
                    }else{
                        if(tv_cartItemCount.getText().toString().equalsIgnoreCase("0")){
                            tv_cartItemCount.setVisibility(View.GONE);
                        }else{
                            tv_cartItemCount.setVisibility(View.VISIBLE);
                        }
                    }*/



                } else{
                    String message=obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(activity_venueDetails.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
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

    public static boolean getLatLong(JSONObject jsonObject) {

        try {

            longitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");
        } catch (JSONException e) {
            return false;

        }

        return true;
    }

    public static JSONObject getLocationInfo(String address) {

        JSONObject jsonObject = new JSONObject();
        try {
            address = address.replaceAll(" ","%20");
            String response = post("http://maps.google.com/maps/api/geocode/json?address="+address+ "&sensor=false", "","get");

            jsonObject = new JSONObject(response);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isPkgAdded=false;
    }
}

