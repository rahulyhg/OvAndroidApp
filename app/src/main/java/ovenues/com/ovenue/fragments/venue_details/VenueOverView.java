package ovenues.com.ovenue.fragments.venue_details;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.adapter.AmenitiesGridListAdapter;
import ovenues.com.ovenue.adapter.horizontal_recycler_home.HomeHoriRecyclerDataAdapter;
import ovenues.com.ovenue.modelpojo.HomeHoriRecyclerSingleItem;
import ovenues.com.ovenue.modelpojo.IdNameUrlGrideRaw;
import ovenues.com.ovenue.modelpojo.SectionDataModel;
import ovenues.com.ovenue.utils.Const;
import ovenues.com.ovenue.utils.ExpandableHeightsGridView;
import ovenues.com.ovenue.utils.PhoneNumberUtil;

import static ovenues.com.ovenue.VenueDetailsMainFragment.activity_venueDetails;
import static ovenues.com.ovenue.VenueDetailsMainFragment.getLatLong;
import static ovenues.com.ovenue.VenueDetailsMainFragment.getLocationInfo;
import static ovenues.com.ovenue.VenueDetailsMainFragment.latitude;
import static ovenues.com.ovenue.VenueDetailsMainFragment.longitude;
import static ovenues.com.ovenue.VenueDetailsMainFragment.str_has_number;
import static ovenues.com.ovenue.VenueDetailsMainFragment.str_is_register;
import static ovenues.com.ovenue.VenueDetailsMainFragment.str_venue_id;
import static ovenues.com.ovenue.utils.APICall.post;

/**
 * A simple {@link Fragment} subclass.
 */
public class VenueOverView extends Fragment {


    SharedPreferences sharepref;
    public static String str_t_and_c;
    ImageView img_ic_map;
    public static LinearLayout ll_amenities,ll_numberDetails,ll_tandC;

    //BELOW CODE IS FOR GRIDE VENDOR DYNAMIC VIEW==========================================
     public static ExpandableHeightsGridView exp_gridview;
     public static ArrayList<IdNameUrlGrideRaw> listamenities =new ArrayList<>();
     public static AmenitiesGridListAdapter exp_grid_adapter;
    //BELOW CODE IS DYNAMIC VIEWPAGER SCROLLING IMAGES HOME FORNT SCREEN ===================================
    // private ImageButton btnNext, btnFinish;

    //===CUSTOME HORIZONTAL RECYCLE VIEW WITH SECTION=========
    public static ArrayList<SectionDataModel> allSampleData_venuesOverviews;

    //===SECTION 1 FOR SERVICE=============
    public static SectionDataModel dm_venue_venue_overview = new SectionDataModel();
    public static ArrayList<HomeHoriRecyclerSingleItem> singleItem_venue_overview ;

    public static RecyclerView my_recycler_view_venuesOverview;
    public static HomeHoriRecyclerDataAdapter adapterHoriScroll_venuesOverView;
    public static TextView tv_description,tv_vanuesuitable,tv_terms,tv_readmore,tv_venue_name_overview,tv_venue_address,tv_shownumber,
            tvTitleDescription, tvTitleVenueSuiteablefor;

    boolean is_contactnumClicked=false;


    public VenueOverView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for convretView fragment
        View convretView = inflater.inflate(R.layout.fragment_venue__over_view, container, false);

        ll_tandC=(LinearLayout)convretView.findViewById(R.id.ll_tandC);
        ll_amenities=(LinearLayout)convretView.findViewById(R.id.ll_amenities);
        ll_numberDetails=(LinearLayout)convretView.findViewById(R.id.ll_numberDetails);
        if(str_is_register!=null && str_is_register.equalsIgnoreCase("1") || !str_has_number.equalsIgnoreCase("1")){
            ll_numberDetails.setVisibility(View.GONE);
        }else{
            ll_numberDetails.setVisibility(View.VISIBLE);
        }

        tv_venue_name_overview=(TextView)convretView.findViewById(R.id.tv_venue_name);
        tv_venue_address=(TextView)convretView.findViewById(R.id.tv_venue_address);

        tvTitleDescription =(TextView)convretView.findViewById(R.id.tvTitleDescription);
        tvTitleVenueSuiteablefor =(TextView)convretView.findViewById(R.id.tvTitleVenueSuiteablefor);

        img_ic_map=(ImageView)convretView.findViewById(R.id.img_ic_map);
        img_ic_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                getLocationInfo(tv_venue_address.getText().toString());
                getLatLong(getLocationInfo(tv_venue_address.getText().toString()));

                // System.out.println("URL ====longitude"+longitude+"latitude"+latitude);

                String label = tv_venue_name_overview.getText().toString();
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(mapIntent);
                }

                /*String address = address = tv_venue_address.getText().toString().replaceAll("\n","");
                address=address.replace(","," ");
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+address);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);*/
            }
        });

        tv_shownumber  =(TextView)convretView.findViewById(R.id.tv_shownumber);
        tv_shownumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_shownumber.getText().toString().equalsIgnoreCase("Show Number")) {

                    if (is_contactnumClicked == false) {
                        if (str_has_number.equalsIgnoreCase("1")) {
                            new GetContact().execute();
                            is_contactnumClicked = true;
                        } else {
                           // Toast.makeText(activity_venueDetails, "Sorry!!!Number not found", Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }
        });

        tv_description=(TextView)convretView.findViewById(R.id.tv_description);
        tv_vanuesuitable=(TextView)convretView.findViewById(R.id.tv_vanuesuitable);
        tv_terms =(TextView)convretView.findViewById(R.id.tv_terms);
        tv_readmore=(TextView)convretView.findViewById(R.id.tv_readmore);
        tv_readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertbox = new AlertDialog.Builder(getContext());
                alertbox.setMessage(str_t_and_c);
                alertbox.setTitle("Terms & Conditions. ");
                alertbox.setIcon(R.mipmap.ic_launcher);

                alertbox.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0,int arg1) {

                            }
                        });
                alertbox.show();
            }
        });

        //====CREARTE CUSTOME HORIZONTAL RECYCLERVIEW =======
        allSampleData_venuesOverviews = new ArrayList<SectionDataModel>();

        singleItem_venue_overview = new ArrayList<HomeHoriRecyclerSingleItem>();

        my_recycler_view_venuesOverview = (RecyclerView)convretView.findViewById(R.id.my_recycler_view);
        my_recycler_view_venuesOverview.setHasFixedSize(true);
        adapterHoriScroll_venuesOverView= new HomeHoriRecyclerDataAdapter(getContext(), allSampleData_venuesOverviews,"venue_details");
        my_recycler_view_venuesOverview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        my_recycler_view_venuesOverview.setAdapter(adapterHoriScroll_venuesOverView);



         //====GRIDE VIEW CODE STARTS=====================================================
        exp_gridview = (ExpandableHeightsGridView)convretView. findViewById(R.id.grid_amenities);

        exp_grid_adapter = new AmenitiesGridListAdapter(getActivity() , listamenities,"venue_details");
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


        return  convretView;
    }

    static String res_contact;
    static class GetContact extends AsyncTask<Object, Void, String> {

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
                JSONObject req = new JSONObject();

                req.put("venue_id",str_venue_id);
                /*req.put("venue_name",str_venue_name);
                req.put("vendor_page_url","");*/

                String response = post(Const.SERVER_URL_API +"request_venue_guest_vendor_contactnum", req.toString(),"post");

                //Log.d("REsponce Json====",response);
                res_contact=response;
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return res_contact;

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String response_string = "";

            Log.i("RESPONSE contact SP--", res_contact);
            try{
                JSONObject obj = new JSONObject(res_contact);

                response_string=obj.getString("status");
                if(response_string.equals("success")){

                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(res_contact).getAsJsonObject();

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
}
