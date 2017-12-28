package ovenues.com.ovenue.fragments.serviceproviders_details.ServiceProviderChargesMenu;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;
import com.google.gson.JsonArray;

import java.util.ArrayList;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.VenueOrderFoodStickyHeader.HeaderItem;
import ovenues.com.ovenue.VenueOrderFoodStickyHeader.TopSnappedStickyLayoutManager;
import ovenues.com.ovenue.adapter.serviceprovider_details_page.ServiceProviderCateringChargesMenuAdapters.ServiceProviderRestaurantMenuAdapter;
import ovenues.com.ovenue.modelpojo.VenueOrderModel.VenueOrderFoodMenuModel;
import ovenues.com.ovenue.utils.ConnectionDetector;

import static android.R.attr.width;
import static android.content.Context.MODE_PRIVATE;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.restaurant_menu;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceProviderRestaurantMenu extends DialogFragment {



    boolean loading_data=false;
    private int visibleThreshold = 5;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    String menuId;
    RecyclerView mRecyclerView;
    ArrayList results;
    private ServiceProviderRestaurantMenuAdapter adapter;

    Boolean isInternetPresent = false;
    // View footerLayout;

    SharedPreferences sharepref;
    TextView title_resturantmenu_service;

    public ServiceProviderRestaurantMenu() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        FrameLayout content = (FrameLayout) window.findViewById(android.R.id.content);

// Make the popup fill at least 4/5 of the screen's width
        int minWidth = width*4/5;
        content.setMinimumWidth(minWidth);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_service_provider_restaurant_menu, container, false);
        sharepref = getActivity().getBaseContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        Bundle mArgs = getArguments();
         menuId = mArgs.getString("menuId");

        results = new ArrayList<VenueOrderFoodMenuModel>();
        adapter = new ServiceProviderRestaurantMenuAdapter(getContext(),results);

        title_resturantmenu_service=(TextView)view.findViewById(R.id.title_resturantmenu_service);

        // Initialize recycler view
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_venuelist);

        StickyLayoutManager layoutManager = new TopSnappedStickyLayoutManager(getContext(), adapter);
        layoutManager.elevateHeaders(true);
        // Default elevation of 5dp
        // You can also specify a specific dp for elevation
        layoutManager.elevateHeaders(0);



        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        layoutManager.setStickyHeaderListener(new StickyHeaderListener() {
            @Override
            public void headerAttached(View headerView, int adapterPosition) {
                //Log.d("Listener", "Attached with position: " + adapterPosition);
            }

            @Override
            public void headerDetached(View headerView, int adapterPosition) {
                // Log.d("Listener", "Detached with position: " + adapterPosition);
            }
        });

        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests

            new GetMenu().execute();

        } else {
            Toast.makeText(getContext(), "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();
        }




        return view;
    }

    protected ProgressDialog progressDialog;
    public class GetMenu extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(getContext(), "Loading", "Please Wait...", true, false);

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

                    JsonArray array_restaurant_menu = restaurant_menu;

                    for (int j = 0; j < array_restaurant_menu.size(); j++) {

                        String menu_id = array_restaurant_menu.get(j).getAsJsonObject().get("menu_id").getAsString();
                        String menu_desc = array_restaurant_menu.get(j).getAsJsonObject().get("menu_desc").getAsString();
                        //String price_per_plate = array_restaurant_menu.get(k).getAsJsonObject().get("price_per_plate").getAsString();
                        if(menuId.equalsIgnoreCase(menu_id)){
                            results.add(new HeaderItem( menu_id,menu_desc,"","","","",0));
                        }

                        JsonArray array_item_menu = !array_restaurant_menu.get(j).getAsJsonObject().getAsJsonArray("items").isJsonNull()
                                ? array_restaurant_menu.get(j).getAsJsonObject().getAsJsonArray("items") : null;

                        if (!array_item_menu.isJsonNull() && array_item_menu.size() > 0) {

                            for (int k = 0; k < array_item_menu.size(); k++) {

                                String item_id = array_item_menu.get(k).getAsJsonObject().get("item_id").getAsString();
                                String item_name = array_item_menu.get(k).getAsJsonObject().get("item_name").getAsString();
                                String price_per_plate = array_item_menu.get(k).getAsJsonObject().get("price_per_plate").getAsString();
                                // items.add(new Item("Item at " + i, "Item description at " + i));

                                if(menuId.equalsIgnoreCase(menu_id)){
                                    results.add(new VenueOrderFoodMenuModel(menu_id,menu_desc,item_id,item_name,"" ,price_per_plate,1));
                                }
                            }
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                    }

            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }

}
