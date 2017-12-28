package ovenues.com.ovenue.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.ServiceProviderCateringActivity.ServiceProviderBeveragesActivity;
import ovenues.com.ovenue.ServiceProviderCateringActivity.ServiceProviderFoodmenuSubCategory;
import ovenues.com.ovenue.VenueCateringActivites.VenueBeveragesActivity;
import ovenues.com.ovenue.VenueCateringActivites.VenueFoodmenuSubCategory;
import ovenues.com.ovenue.fragments.VenuesFragments.VenueRestaurantMenu;
import ovenues.com.ovenue.fragments.serviceproviders_details.ServiceProviderChargesMenu.ServiceProviderRestaurantMenu;
import ovenues.com.ovenue.modelpojo.ServiceproviderCateringPricingMenuModel;

import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.fm;
import static ovenues.com.ovenue.VenueDetailsMainFragment.fmVenue;

/**
 * Created by Jay-Andriod on 18-Aug-17.
 */

public class ServiceproviderVenueBothCateringPricingMenuAdapter extends RecyclerView
        .Adapter<ServiceproviderVenueBothCateringPricingMenuAdapter
        .DataObject_postHolder> {
static private ArrayList<ServiceproviderCateringPricingMenuModel> mDataset;
static private Context mContext;
    Activity activity;

        SharedPreferences sharepref;


public static class DataObject_postHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    TextView tv_title;
    ImageView img_nextarrow;


    public DataObject_postHolder(final View itemView) {
        super(itemView);



        tv_title= (TextView) itemView.findViewById(R.id.tv_title);
        img_nextarrow = (ImageView)itemView.findViewById(R.id.img_nextarrow);

    }

    @Override
    public void onClick(View v) {
        //     myClickListener.onItemClick(getAdapterPosition(), v);
    }
}

   /* public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }*/

    public ServiceproviderVenueBothCateringPricingMenuAdapter(ArrayList<ServiceproviderCateringPricingMenuModel> myDataset, Context context) {
        mDataset = myDataset;
        mContext=context;

    }

    @Override
    public DataObject_postHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_serviceprovider_catering_pricingmenu, parent, false);
        ServiceproviderVenueBothCateringPricingMenuAdapter.DataObject_postHolder dataObjectHolder = new ServiceproviderVenueBothCateringPricingMenuAdapter.DataObject_postHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObject_postHolder holder, final int position) {


        if(position%2==0){
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
          //  holder.itemView.setBackgroundColor(Color.parseColor("#00F7F9F9"));
        }
        holder.setIsRecyclable(false);
        sharepref = mContext.getApplicationContext().getSharedPreferences("MyPref",mContext.MODE_PRIVATE);

        String title = mDataset.get(position).getTitle().substring(0,1).toUpperCase()+mDataset.get(position).getTitle().substring(1).toLowerCase();
        holder.tv_title.setText(title);

        holder.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mDataset.get(position).getFlag().equalsIgnoreCase("1") && mDataset.get(position).isVenue()==true) {
                    mContext.startActivity(new Intent(mContext, VenueFoodmenuSubCategory.class)
                            .putExtra("menu_id", mDataset.get(position).getId())
                            .putExtra("title", mDataset.get(position).getTitle()));
                }else if(mDataset.get(position).getFlag().equalsIgnoreCase("1") && mDataset.get(position).isVenue()==false) {
                    mContext.startActivity(new Intent(mContext, ServiceProviderFoodmenuSubCategory.class)
                            .putExtra("menu_id", mDataset.get(position).getId())
                            .putExtra("title", mDataset.get(position).getTitle()));
                }


                else if(mDataset.get(position).getFlag().equalsIgnoreCase("2") && mDataset.get(position).isVenue()==true ){
                    VenueRestaurantMenu dFragment = new VenueRestaurantMenu();
                    // Show DialogFragment
                    Bundle args = new Bundle();
                    args.putString("menuId", mDataset.get(position).getId());
                    //args.putString("service_providerID", mDataset.get(position).get());
                    dFragment.setArguments(args);
                    dFragment.show(fmVenue, "Dialog Fragment");
                }else if(mDataset.get(position).getFlag().equalsIgnoreCase("2") && mDataset.get(position).isVenue()==false ){
                    ServiceProviderRestaurantMenu dFragment = new ServiceProviderRestaurantMenu();
                    // Show DialogFragment
                    Bundle args = new Bundle();
                    args.putString("menuId", mDataset.get(position).getId());
                    //args.putString("service_providerID", mDataset.get(position).get());
                    dFragment.setArguments(args);
                    dFragment.show(fm, "Dialog Fragment");
                }


                else if(mDataset.get(position).getFlag().equalsIgnoreCase("3") &&  mDataset.get(position).isVenue()==true){
                    /*ServiceProviderBeveragesActivity dFragment = new ServiceProviderBeveragesActivity();
                    // Show DialogFragment
                    Bundle args = new Bundle();
                    args.putString("menuId", mDataset.get(position).getId());
                    //args.putString("service_providerID", mDataset.get(position).get());
                    dFragment.setArguments(args);
                    dFragment.show(fm, "Dialog Fragment");*/
                    ((Activity)mContext).startActivity(new Intent(mContext,VenueBeveragesActivity.class));
                }else if(mDataset.get(position).getFlag().equalsIgnoreCase("3") &&  mDataset.get(position).isVenue()==false){
                    /*ServiceProviderBeveragesActivity dFragment = new ServiceProviderBeveragesActivity();
                    // Show DialogFragment
                    Bundle args = new Bundle();
                    args.putString("menuId", mDataset.get(position).getId());
                    //args.putString("service_providerID", mDataset.get(position).get());
                    dFragment.setArguments(args);
                    dFragment.show(fm, "Dialog Fragment");*/
                    ((Activity)mContext).startActivity(new Intent(mContext,ServiceProviderBeveragesActivity.class));
                }
             /*   dFragment.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dFragment.getDialog().setCanceledOnTouchOutside(true);


                DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;

                WindowManager.LayoutParams lp = dFragment.getDialog().getWindow().getAttributes();
                lp.width = width;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                lp.dimAmount = 0.81f;
                dFragment.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN );
                dFragment.getDialog().getWindow().setAttributes(lp);*/


             /*   mContext.startActivity(new Intent(mContext, ServiceProviderFoodmenuSubCategory.class)
                        .putExtra("menu_id",mDataset.get(position).getMenu_id())
                        .putExtra("title",mDataset.get(position).getMenu_desc())
                        .putExtra("guest_count",Integer.toString(holder.np_itemcount.getValue())));
                Log.d("guest_count",""+holder.np_itemcount.getValue());*/

            }
        });
    }



    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}