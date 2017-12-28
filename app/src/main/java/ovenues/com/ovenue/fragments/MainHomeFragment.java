package ovenues.com.ovenue.fragments;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.adapter.horizontal_recycler_home.HomeHoriRecyclerDataAdapter;
import ovenues.com.ovenue.adapter.horizontal_recycler_home_topfive.HomeHoriRecyclerTopFiveDataAdapter;
import ovenues.com.ovenue.adapter.horizontal_recycler_servicelist.ServiceListHoriRecyclerDataAdapter;
import ovenues.com.ovenue.modelpojo.SectionDataModel;
import ovenues.com.ovenue.modelpojo.HomeHoriRecyclerSingleItem;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.utils.APICall.post;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainHomeFragment extends Fragment {



    //===CUSTOME HORIZONTAL RECYCLE VIEW WITH SECTION=========
    ArrayList<SectionDataModel> allSampleData;

    //===SECTION 1 FOR SERVICE=============
    SectionDataModel dm = new SectionDataModel();
    ArrayList<HomeHoriRecyclerSingleItem> singleItem ;

    LinearLayout ll_dynamicrecycleView;
    RecyclerView my_recycler_view;
    ServiceListHoriRecyclerDataAdapter adapter;
    ScrollView nestedscroll;
    TextView tv_scrol_top;


    public MainHomeFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_main_home, container, false);



        //====CREARTE CUSTOME HORIZONTAL RECYCLERVIEW =======
        allSampleData = new ArrayList<SectionDataModel>();

        singleItem = new ArrayList<HomeHoriRecyclerSingleItem>();
        ll_dynamicrecycleView = (LinearLayout)convertView.findViewById(R.id.ll_dynamicrecycleView);

        nestedscroll =(ScrollView)convertView.findViewById(R.id.nestedscroll);
        tv_scrol_top = (TextView)convertView .findViewById(R.id.tv_scrol_top);

        tv_scrol_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedscroll.scrollTo(0, 0);
            }
        });
        nestedscroll.setSmoothScrollingEnabled(true);
        nestedscroll.setNestedScrollingEnabled(false);

        new GetServiceVenodrs().execute();
        new GetServiceTOPFIVE().execute();

        my_recycler_view = (RecyclerView)convertView.findViewById(R.id.my_recycler_view);
        adapter = new ServiceListHoriRecyclerDataAdapter(getActivity(), allSampleData);

        my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(adapter);




        return convertView;
    }



    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        final View view = getView();
        if (view != null) {
            initView(getActivity().getLayoutInflater(), (ViewGroup) view.findViewById(R.id.cordinatelayout));
        }
    }*/

    private void initView(final LayoutInflater inflater, final ViewGroup parent) {
        parent.removeAllViews();
        final View subRoot = inflater.inflate(R.layout.fragment_main_home, null);
        parent.addView(subRoot);
        //do all the stuff
    }

   String res;
    class GetServiceVenodrs extends AsyncTask<Object, Void, String> {

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

               /* JSONObject req = new JSONObject();
                req.put("email_id","vani@ovenues.net");
                req.put("password","test@123");
                Log.d("REq Json======", req.toString());*/

                String response = post(Const.SERVER_URL_API +"services", "","get");
                //Log.d("REsponce Json====",response);
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
            super.onPostExecute(result);
            String response_string = "";

            try{
                JSONObject obj = new JSONObject(res);
                // Log.i("RESPONSE", res);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(res).getAsJsonObject();

                    JsonArray servicesObj = rootObj.getAsJsonArray("message");

                    dm.setHeaderTitle("Services Offered");

                    singleItem.add(new HomeHoriRecyclerSingleItem("Venues", "https://s3-us-west-1.amazonaws.com/ovenues-services-icons/img/orange-icons/svg/cir-o-venue.svg",
                            Const.SERVER_URL_ONLY+"img/outline_icons/cir-o-ol-venue.svg","0",Const.CONST_HOMESERVICE,true));
                    for (int j = 0; j < servicesObj.size(); j++) {
                        String service_id = servicesObj.get(j).getAsJsonObject().get("id").getAsString();
                        String service_name  =servicesObj.get(j).getAsJsonObject().get("service_name").getAsString();
                        String circle_svg_icon_url =  servicesObj.get(j).getAsJsonObject().get("app_icon_svg_url").getAsString();
                        String outline_svg_icon_url  =servicesObj.get(j).getAsJsonObject().get("outline_svg_icon_url").getAsString();
                        /*String filled_icon_url = servicesObj.get(j).getAsJsonObject().get("filled_icon_url").getAsString();
                        String thubnail_icon_url  =servicesObj.get(j).getAsJsonObject().get("thubnail_icon_url").getAsString();*/
                        //Log.d("for start",service_name);
                        singleItem.add(new HomeHoriRecyclerSingleItem(service_name, circle_svg_icon_url,Const.SERVER_URL_ONLY+outline_svg_icon_url,service_id,Const.CONST_HOMESERVICE,true));

                    }
                    /*singleItem.add(new HomeHoriRecyclerSingleItem("AskOvenues","https://s3-us-west-1.amazonaws.com/ovenues-services-icons/img/orange-icons/svg/cir-o-ask.svg",
                            Const.SERVER_URL_ONLY+"img/outline_icons/cir-o-ol-ask.svg","00",Const.CONST_HOMESERVICE,true));*/
                    dm.setAllItemsInSection(singleItem);
                    allSampleData.add(dm);
                    adapter.notifyDataSetChanged();




                } else{
                    String message=obj.getString("message");
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
                    snackbar.show();
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    String resTOPFIVE;
    class GetServiceTOPFIVE extends AsyncTask<Object, Void, String> {

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

               /* JSONObject req = new JSONObject();
                req.put("email_id","vani@ovenues.net");
                req.put("password","test@123");
                Log.d("REq Json======", req.toString());*/

                String response = post(Const.SERVER_URL_API +"first_five_vendors", "","get");
                //Log.d("REsponce Json====",response);
                resTOPFIVE=response;
            }/*catch (JSONException e) {
                e.printStackTrace();
            }*/ catch (IOException e) {
                e.printStackTrace();
            }


            return resTOPFIVE;

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String response_string = "";

            try{
                JSONObject obj = new JSONObject(resTOPFIVE);
                // Log.i("RESPONSE", res);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(resTOPFIVE).getAsJsonObject();

                    JsonArray msgArray = rootObj.getAsJsonArray("message");




                    for(int a =0;a<msgArray.size();a++){
                        String title = msgArray.get(a).getAsJsonObject().get("title").getAsString();
                        String service_id  = msgArray.get(a).getAsJsonObject().get("service_id").getAsString();

                        //===SECTION dynamic FOR SERVICE=============
                        //===CUSTOME HORIZONTAL RECYCLE VIEW WITH SECTION=========
                        ArrayList<SectionDataModel> allSampleData_dynamic =  new ArrayList<SectionDataModel>();;
                        SectionDataModel dm_dynamic = new SectionDataModel();
                        ArrayList<HomeHoriRecyclerSingleItem> singleItemdynamic = new ArrayList<HomeHoriRecyclerSingleItem>();;

                        dm_dynamic.setHeaderTitle(title);

                        JsonArray vendors = msgArray.get(a).getAsJsonObject().get("vendors").getAsJsonArray();

                        HomeHoriRecyclerTopFiveDataAdapter adapter_dynamicR1 = new HomeHoriRecyclerTopFiveDataAdapter(getContext(), allSampleData_dynamic,"home");

                        RecyclerView rv1 = new RecyclerView(getContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(0,5,0,5);
                        rv1.setLayoutParams(lp);
                        rv1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        rv1.setAdapter(adapter_dynamicR1);
                        rv1.setNestedScrollingEnabled(false);

                        ll_dynamicrecycleView.addView(rv1);

                        View v = new View(getContext());
                        LinearLayout.LayoutParams vlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
                        vlp.setMargins(5,10,5,5);
                        v.setLayoutParams(vlp);
                        v.setBackgroundColor(Color.parseColor("#757575"));

                        ll_dynamicrecycleView.addView(v);


                            if (service_id.equalsIgnoreCase("0")) {

                                for(int b=0;b<vendors.size();b++) {

                                String photo_url = !vendors.get(b).getAsJsonObject().get("photo_url").isJsonNull()
                                ? vendors.get(b).getAsJsonObject().get("photo_url").getAsString():"";
                                String venue_name = vendors.get(b).getAsJsonObject().get("venue_name").getAsString();
                                String venue_id = vendors.get(b).getAsJsonObject().get("venue_id").getAsString();
                                singleItemdynamic.add(new HomeHoriRecyclerSingleItem(venue_name, "", venue_id, Const.CONST_TOPFIVE, service_id, photo_url,vendors.get(b).getAsJsonObject()));
                                // System.out.print("model class"+message_details.length()+service_name);
                                }
                                dm_dynamic.setAllItemsInSection(singleItemdynamic);
                                allSampleData_dynamic.add(dm_dynamic);
                                adapter_dynamicR1.notifyDataSetChanged();
                            }else{
                                for(int b=0;b<vendors.size();b++) {

                                    String photo_url = !vendors.get(b).getAsJsonObject().get("photo_url").isJsonNull()
                                            ? vendors.get(b).getAsJsonObject().get("photo_url").getAsString():"";
                                    String provider_name = vendors.get(b).getAsJsonObject().get("provider_name").getAsString();
                                    String id = vendors.get(b).getAsJsonObject().get("id").getAsString();
                                    singleItemdynamic.add(new HomeHoriRecyclerSingleItem(provider_name, "", id, Const.CONST_TOPFIVE, service_id, photo_url,vendors.get(b).getAsJsonObject()));
                                    // System.out.print("model class"+message_details.length()+service_name);
                                }
                                dm_dynamic.setAllItemsInSection(singleItemdynamic);
                                allSampleData_dynamic.add(dm_dynamic);
                                adapter_dynamicR1.notifyDataSetChanged();
                            }

                    }



                } else{
                    String message=obj.getString("message");
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
