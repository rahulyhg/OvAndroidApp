package ovenues.com.ovenue.fragments.venue_details;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.VenueDetailsMainFragment;
import ovenues.com.ovenue.VenueOrderServicesStickyHeader.TopSnappedStickyLayoutManagerVenueOrderServices;
import ovenues.com.ovenue.VenueOrderServicesStickyHeader.VenueOrderServicesAdapter;
import ovenues.com.ovenue.adapter.Spiners.SpinerWithDynamicArrayList;
import ovenues.com.ovenue.adapter.VenueDetailsPages.VenueOrderPriceAdapter;
import ovenues.com.ovenue.adapter.ServiceproviderVenueBothCateringPricingMenuAdapter;
import ovenues.com.ovenue.modelpojo.Spiners.SearchVenueSpiners;
import ovenues.com.ovenue.modelpojo.VenueOrderModel.VenueOrderPriceModel;
import ovenues.com.ovenue.modelpojo.VenueOrderModel.VenueOrderServiceModel;

import static ovenues.com.ovenue.VenueDetailsMainFragment.max_occupancy;
import static ovenues.com.ovenue.VenueDetailsMainFragment.min_occupancy;
import static ovenues.com.ovenue.VenueDetailsMainFragment.tv_error_datetime;
import static ovenues.com.ovenue.VenueDetailsMainFragment.tv_title;

/**
 * A simple {@link Fragment} subclass.
 */
public class VenuePricingPlans extends Fragment {

    public static EditText et_date,et_guestCount;
    public static Spinner sp_timeslot;
    public static String str_dateselected=null,str_guest_count="",str_timeslot=null;
    public static ImageView img_pkg,img_foodbev ,img_service;
    public static RecyclerView rv_pacakges,rv_foodbev ,recyclerview_venueService;

    private int mYear, mMonth, mDay, mHour, mMinute;

    public static SpinerWithDynamicArrayList sp_timeslotTypeAdapter;
    public static ArrayList<SearchVenueSpiners> sp_timeslotType_aarayList;

    public static RecyclerView.Adapter mAdapterVenuePricePKG;
    public static List<VenueOrderPriceModel> venueorderpriceList;

    public static JsonArray food_menu,restaurant_menu ,beveragesJSONVenues;
    public static RecyclerView.Adapter adapterCateringPricingAdapterVenue;
    public static ArrayList results_CateringPricingVenue;


    public static VenueOrderServicesAdapter mAdapterVenueServices ;
    public static ArrayList<VenueOrderServiceModel> venueserviceList;

    public VenuePricingPlans() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for view fragment
        View view= inflater.inflate(R.layout.fragment_venue_pricing_plans, container, false);


        return view;


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        tv_title = (TextView)view.findViewById(R.id.tv_title);
        tv_error_datetime = (TextView)view.findViewById(R.id.tv_error_datetime);
        tv_error_datetime.setVisibility(View.GONE);



        et_date = (EditText) view.findViewById(R.id.et_date);
        et_guestCount= (EditText) view.findViewById(R.id.et_guestCount);
        et_guestCount.setHint("( "+min_occupancy+" - "+max_occupancy+" )");


        //===Spiner venue type config here=========
        sp_timeslot = (Spinner)view.findViewById(R.id.sp_timeslot);
        sp_timeslotType_aarayList =new ArrayList<>();
        sp_timeslotTypeAdapter = new SpinerWithDynamicArrayList(getContext() , R.layout.row_spiners_venue_search_filter, sp_timeslotType_aarayList);
        sp_timeslot.setAdapter(sp_timeslotTypeAdapter);

        sp_timeslotType_aarayList.add(new SearchVenueSpiners("0","Select Time"));
        sp_timeslotTypeAdapter.notifyDataSetChanged();


        sp_timeslot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               /* et_guestCount.setFocusableInTouchMode(true);
                et_guestCount.requestFocus();*/
                final InputMethodManager inputMethodManager = (InputMethodManager)getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_guestCount, InputMethodManager.SHOW_IMPLICIT);
                et_guestCount.requestFocusFromTouch();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        et_guestCount.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_GO) {

                    if (et_date.getText().length() < 1) {
                        tv_error_datetime.setVisibility(View.VISIBLE);
                        et_date.setError("Date required to select pricing.");
                        Toast.makeText(getContext(), "Date required  to select pricing.", Toast.LENGTH_LONG).show();

                    } else if (sp_timeslot.getSelectedItemPosition() < 1 || sp_timeslot.getSelectedItemPosition() == 0) {
                        tv_error_datetime.setText("Time required  to select pricing.");
                        Toast.makeText(getContext(), "Time required  to select pricing.", Toast.LENGTH_LONG).show();
                    } else if (et_guestCount.getText().length() < 1) {
                        tv_error_datetime.setVisibility(View.VISIBLE);
                        et_guestCount.setError("Guests number required to select pricing.");
                        Toast.makeText(getContext(), "Guests count is required to select pricing.", Toast.LENGTH_LONG).show();
                    } else {
                        tv_error_datetime.setVisibility(View.GONE);
                        if (Integer.parseInt(et_guestCount.getText().toString()) > max_occupancy || Integer.parseInt(et_guestCount.getText().toString()) < min_occupancy) {
                            if (Integer.parseInt(et_guestCount.getText().toString()) > max_occupancy) {
                                et_guestCount.setError("Max of " + max_occupancy + " guests allowed.");
                            } else if (Integer.parseInt(et_guestCount.getText().toString()) < min_occupancy) {
                                et_guestCount.setError("Min of " + min_occupancy + " guests allowed.");
                            }
                            et_guestCount.requestFocus();
                            et_guestCount.setText("");
                        } else {
                            Log.d("opccupancy---", "" + min_occupancy + max_occupancy);
                            str_guest_count = et_guestCount.getText().toString();
                            str_timeslot = sp_timeslotType_aarayList.get(sp_timeslot.getSelectedItemPosition()).getType();
                            new VenueDetailsMainFragment.GetPricingPackages().execute(1);
                            et_guestCount.clearFocus();
                            /*InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            in.hideSoftInputFromWindow(et_guestCount.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);*/
                        }

                    }
                }
                return false;
            }
        });


        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String str_day ,str_month;
                                if(dayOfMonth < 10){
                                    str_day = "0"+dayOfMonth;
                                }else {
                                    str_day = Integer.toString(dayOfMonth);
                                }
                                if(monthOfYear + 1 < 10){
                                    str_month = "0"+Integer.toString(monthOfYear + 1);
                                }else {
                                    str_month  =Integer.toString(monthOfYear + 1);
                                }
                                et_date.setText(str_month + "-" + str_day + "-" +year);
                                et_date.setError(null);
                                str_dateselected = et_date.getText().toString();
                                new VenueDetailsMainFragment.GetTimeSlot().execute();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis() + 1 * 1000 * 24 * 3600);
                datePickerDialog.show();
            }
        });
        venueorderpriceList = new ArrayList<VenueOrderPriceModel>();
        // create an Object for Adapter
        mAdapterVenuePricePKG = new VenueOrderPriceAdapter(getActivity(),venueorderpriceList);

        //=======CAtering SERVICE PROVIDERS CHARGES RECYSLERVIEW ADDAPTER SET DATA =========================
        results_CateringPricingVenue = new ArrayList<VenuePricingPlans>();
        adapterCateringPricingAdapterVenue = new ServiceproviderVenueBothCateringPricingMenuAdapter(results_CateringPricingVenue, getContext());



        venueserviceList=new ArrayList<VenueOrderServiceModel>();
        mAdapterVenueServices = new VenueOrderServicesAdapter(getContext(),venueserviceList);

        img_pkg = (ImageView)view.findViewById(R.id.img_pkg);
        img_foodbev = (ImageView)view.findViewById(R.id.img_foodbev);
        img_service = (ImageView)view.findViewById(R.id.img_service);

        rv_pacakges =(RecyclerView)view.findViewById(R.id.rv_pacakges);
        rv_foodbev =(RecyclerView)view.findViewById(R.id.rv_foodbev);
        recyclerview_venueService =(RecyclerView)view.findViewById(R.id.recyclerview_venueService);


        tv_title.setText("Packages");

        rv_pacakges.setVisibility(View.VISIBLE);
        rv_foodbev.setVisibility(View.GONE);
        recyclerview_venueService.setVisibility(View.GONE);


        final RecyclerView.LayoutManager mLayoutManagerPricePkg;
        mLayoutManagerPricePkg = new LinearLayoutManager(getActivity());
        rv_pacakges.setLayoutManager(mLayoutManagerPricePkg);
        //rv_pacakges.getRecycledViewPool().setMaxRecycledViews(VIEW_TYPE,0);
        rv_pacakges.setAdapter(mAdapterVenuePricePKG);


        final RecyclerView.LayoutManager mLayoutManager_foodBev;
        mLayoutManager_foodBev = new LinearLayoutManager(getActivity());
        rv_foodbev.setLayoutManager(mLayoutManager_foodBev);
        rv_foodbev.setNestedScrollingEnabled(true);
        rv_foodbev.setAdapter(adapterCateringPricingAdapterVenue);



        StickyLayoutManager layoutManager = new TopSnappedStickyLayoutManagerVenueOrderServices(getActivity(), mAdapterVenueServices);
        layoutManager.elevateHeaders(true);
        // Default elevation of 5dp
        // You can also specify a specific dp for elevation
        layoutManager.elevateHeaders(7);

        recyclerview_venueService.setLayoutManager(layoutManager);
        recyclerview_venueService.setAdapter(mAdapterVenueServices);

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


        img_pkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                tv_title.setText("Packages");

                img_pkg.setImageResource(R.drawable.ic_packages_white_venue);
                img_pkg.setBackgroundResource(R.color.colorAccent);

                img_foodbev.setImageResource(R.drawable.ic_foodbev_orange_venue);
                img_foodbev.setBackgroundResource(R.color.white);

                img_service.setImageResource(R.drawable.ic_service_orange_venue);
                img_service.setBackgroundResource(R.color.white);

                rv_pacakges.setVisibility(View.VISIBLE);
                rv_foodbev.setVisibility(View.GONE);
                recyclerview_venueService.setVisibility(View.GONE);


            }
        });
        img_foodbev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                tv_title.setText("Food & Beverages");

                img_pkg.setImageResource(R.drawable.ic_packages_orange_venue);
                img_pkg.setBackgroundResource(R.color.white);

                img_foodbev.setImageResource(R.drawable.ic_foodbev_white_venue);
                img_foodbev.setBackgroundResource(R.color.colorAccent);

                img_service.setImageResource(R.drawable.ic_service_orange_venue);
                img_service.setBackgroundResource(R.color.white);

                rv_pacakges.setVisibility(View.GONE);
                rv_foodbev.setVisibility(View.VISIBLE);
                recyclerview_venueService.setVisibility(View.GONE);

            }
        });

        img_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                tv_title.setText("Services");

                img_pkg.setImageResource(R.drawable.ic_packages_orange_venue);
                img_pkg.setBackgroundResource(R.color.white);

                img_foodbev.setImageResource(R.drawable.ic_foodbev_orange_venue);
                img_foodbev.setBackgroundResource(R.color.white);

                img_service.setImageResource(R.drawable.ic_service_white_venue);
                img_service.setBackgroundResource(R.color.colorAccent);;

                rv_pacakges.setVisibility(View.GONE);
                rv_foodbev.setVisibility(View.GONE);
                recyclerview_venueService.setVisibility(View.VISIBLE);

            }
        });



    }




}
