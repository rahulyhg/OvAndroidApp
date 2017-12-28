package ovenues.com.ovenue.fragments.serviceproviders_details;

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
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.utils.Const;
import ovenues.com.ovenue.utils.PhoneNumberUtil;

import static android.content.Context.MODE_PRIVATE;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.activitySP;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.delivery_charges;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.is_delivery;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.is_delivery_paid;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.is_onsite_service;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.is_pickup;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.providerAddress;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.providerDescription;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.providerName;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.providerPhonenumber;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.providerTerms;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.str_has_number;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.str_is_register;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.str_service_provider_id;
import static ovenues.com.ovenue.utils.APICall.post;


public class ServiceproviderOverviewFragment extends Fragment {

    private static SharedPreferences sharepref;
    String str_t_and_c;
    ImageView img_ic_map,ic_delivery,ic_pickup ,ic_onsite;
    public static CardView cv_tandC;
    LinearLayout ll_delivery,ll_pickup ,ll_onsite;
    static double latitude,longitude;
    MapView map_serviceprovider;
    public static LinearLayout  ll_numberDetails;
    boolean is_contactnumClicked=false;

   static TextView tv_description,tv_terms,tv_readmore,tv_service_provider_name,tv_service_provider_address,tv_shownumber,tv_deliverycharge;

    public ServiceproviderOverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView =  inflater.inflate(R.layout.fragment_serviceprovider_overview_fragment, container, false);
        sharepref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        cv_tandC = (CardView)convertView.findViewById(R.id.cv_tandC);
        ll_numberDetails=(LinearLayout)convertView.findViewById(R.id.ll_numberDetails);

        if(str_is_register.equalsIgnoreCase("1") || !str_has_number.equalsIgnoreCase("1")){
            ll_numberDetails.setVisibility(View.GONE);
        }else{
            ll_numberDetails.setVisibility(View.VISIBLE);
        }


        tv_service_provider_name=(TextView)convertView.findViewById(R.id.tv_service_provider_name);
        tv_service_provider_address=(TextView)convertView.findViewById(R.id.tv_service_provider_address);

        tv_service_provider_name.setText(providerName);
        tv_service_provider_address.setText(providerAddress);

        img_ic_map=(ImageView)convertView.findViewById(R.id.img_ic_map);

        getLatLong(getLocationInfo(tv_service_provider_address.getText().toString()));

        map_serviceprovider = (MapView)convertView.findViewById(R.id.map_serviceprovider);
        map_serviceprovider.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Marker"));
            }
        });
        tv_deliverycharge  =(TextView)convertView.findViewById(R.id.tv_deliverycharge);
        ll_delivery =(LinearLayout) convertView.findViewById(R.id.ll_delivery);
        ll_pickup =(LinearLayout) convertView.findViewById(R.id.ll_pickup);
        ll_onsite =(LinearLayout) convertView.findViewById(R.id.ll_onsite);
        ic_delivery  =(ImageView)convertView.findViewById(R.id.ic_delivery);
        ic_pickup =(ImageView)convertView.findViewById(R.id.ic_pickup);
        ic_onsite =(ImageView)convertView.findViewById(R.id.ic_onsite);

        if(is_delivery.equalsIgnoreCase("1")){
            ll_delivery.setVisibility(View.VISIBLE);
        }else {
            ll_delivery.setVisibility(View.GONE);
        }

        if(is_pickup.equalsIgnoreCase("1")){
            ll_pickup.setVisibility(View.VISIBLE);
        }else {
            ll_pickup.setVisibility(View.GONE);
        }

        if(is_onsite_service.equalsIgnoreCase("1")){
            ll_onsite.setVisibility(View.VISIBLE);
        }else {
            ll_onsite.setVisibility(View.GONE);
        }

        if(is_delivery_paid!=null && is_delivery_paid.equalsIgnoreCase("1")){
            tv_deliverycharge.setText(delivery_charges);
        }else{
            tv_deliverycharge.setVisibility(View.GONE);
        }

        img_ic_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                getLocationInfo(tv_venue_address.getText().toString());
                getLatLong(getLocationInfo(tv_service_provider_address.getText().toString()));

                // System.out.println("URL ====longitude"+longitude+"latitude"+latitude);

                String label = tv_service_provider_name.getText().toString();
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


        tv_description=(TextView)convertView.findViewById(R.id.tv_description);
        tv_description.setText(providerDescription);

        tv_terms =(TextView)convertView.findViewById(R.id.tv_terms);



        if(providerTerms==null || providerTerms.equalsIgnoreCase("")){
            cv_tandC.setVisibility(View.GONE);
        }else{
            cv_tandC.setVisibility(View.VISIBLE);
        }


        tv_shownumber  =(TextView)convertView.findViewById(R.id.tv_shownumber);
        tv_shownumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_shownumber.getText().toString().equalsIgnoreCase("Show Number")){
                    if(str_has_number.length()>0 && str_has_number.equalsIgnoreCase("1")) {
                        new GetContact().execute();
                    }else{
                      //  Toast.makeText(activitySP,"Sorry!!!Number not found",Toast.LENGTH_LONG).show();
                    }

                }else{
                    tv_shownumber.setText("Show Number");
                }
            }
        });


        tv_shownumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_shownumber.getText().toString().equalsIgnoreCase("Show Number")) {

                    if (is_contactnumClicked == false) {
                        if (str_has_number.equalsIgnoreCase("1")) {
                            new GetContact().execute();
                            is_contactnumClicked = true;
                        } else {
                            //Toast.makeText(activity_venueDetails, "Sorry!!!Number not found", Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }
        });

        tv_readmore=(TextView)convertView.findViewById(R.id.tv_readmore);
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
        return  convertView;
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


                req.put("service_id",sharepref.getString(Const.PREF_STR_SERVICE_ID,null));
                req.put("service_provider_id",str_service_provider_id);
                   /* req.put("vendor_page_url","");*/



               String response = post(Const.SERVER_URL_API +"request_sp_guest_vendor_contactnum", req.toString(),"post");

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
                     String message = rootObj.get("message").getAsString();
                     tv_shownumber.setText(PhoneNumberUtil.formateToPhoneNumber(message,"(XXX)-XXX-XXXX",10));


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
