package ovenues.com.ovenue.fragments.VenuesFragments;


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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.VenueOrderFoodStickyHeader.HeaderItem;
import ovenues.com.ovenue.VenueOrderFoodStickyHeader.TopSnappedStickyLayoutManager;
import ovenues.com.ovenue.VenueOrderFoodStickyHeader.VenueOrderFoodMenuOrderAdapter;
import ovenues.com.ovenue.modelpojo.ServiceProviderChargesMenuModel.FoodMenuModel;
import ovenues.com.ovenue.modelpojo.VenueOrderModel.VenueOrderFoodMenuModel;

import static android.R.attr.width;
import static android.content.Context.MODE_PRIVATE;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.restaurant_menu;

/**
 * A simple {@link Fragment} subclass.
 */
public class VenueRestaurantMenu extends DialogFragment {

    private RecyclerView mRecyclerView;
    public static ArrayList venueorderfoodList ;
    public static VenueOrderFoodMenuOrderAdapter venueorderfoodAdapter;



    String menuId;
    private Button btnSelection;
    SharedPreferences sharepref;
    String str_course_id, str_title;
    public static TextView tv_venue_food_menutitle;
    public static double venue_food_total =0;

    List<FoodMenuModel> items ;

    public VenueRestaurantMenu() {
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
        View view =  inflater.inflate(R.layout.fragment_fragment_foods, container, false);

        sharepref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        Bundle mArgs = getArguments();
        menuId = mArgs.getString("menuId");



        tv_venue_food_menutitle = (TextView) view.findViewById(R.id.tv_menutitle);
        btnSelection = (Button)view.findViewById(R.id.btnShow);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.my_recycler_view);


        //==here only model class use for view but concept is for food menu show not beverages
        venueorderfoodList = new ArrayList<>();
        venueorderfoodAdapter = new VenueOrderFoodMenuOrderAdapter(getActivity(),venueorderfoodList);


        StickyLayoutManager layoutManager = new TopSnappedStickyLayoutManager(getContext(), venueorderfoodAdapter);
        layoutManager.elevateHeaders(true);
        // Default elevation of 5dp
        // You can also specify a specific dp for elevation
       layoutManager.elevateHeaders(10);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(venueorderfoodAdapter);
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

        new GetMenu().execute();


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
                        venueorderfoodList.add(new HeaderItem( menu_id,menu_desc,"","","","",0));
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
                                venueorderfoodList.add(new VenueOrderFoodMenuModel(menu_id,menu_desc,item_id,item_name,"" ,price_per_plate,1));
                            }
                        }
                        venueorderfoodAdapter.notifyDataSetChanged();
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