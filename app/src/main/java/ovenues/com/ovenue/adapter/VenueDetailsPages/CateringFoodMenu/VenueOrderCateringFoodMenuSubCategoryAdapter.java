package ovenues.com.ovenue.adapter.VenueDetailsPages.CateringFoodMenu;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.VenueCateringActivites.VenueCateringFoodAddItem;
import ovenues.com.ovenue.modelpojo.ServiceProviderChargesMenuModel.FoodMenuModel;

import static ovenues.com.ovenue.VenueCateringActivites.VenueFoodmenuSubCategory.service_food_guest_count;


/**
 * Created by Jay-Andriod on 08-May-17.
 */


public class VenueOrderCateringFoodMenuSubCategoryAdapter extends RecyclerView.Adapter<VenueOrderCateringFoodMenuSubCategoryAdapter
        .DataObject_postHolder> {
    static private ArrayList<FoodMenuModel> mDataset;
    static private Context mContext;

    SharedPreferences sharepref;


    public static class DataObject_postHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_srNo,tv_title ,tv_course_totalcount,tv_price,tv_perplate;


        public DataObject_postHolder(final View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_srNo = (TextView) itemView.findViewById(R.id.tv_srNo);
            tv_course_totalcount = (TextView) itemView.findViewById(R.id.tv_course_totalcount);
            tv_price= (TextView) itemView.findViewById(R.id.tv_price);
            tv_perplate= (TextView) itemView.findViewById(R.id.tv_perplate);
            tv_perplate.setVisibility(View.GONE);

        }

        @Override
        public void onClick(View v) {
            //     myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

   /* public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }*/

    public VenueOrderCateringFoodMenuSubCategoryAdapter(ArrayList<FoodMenuModel> myDataset, Context context) {
        mDataset = myDataset;
        mContext=context;
    }

    @Override
    public DataObject_postHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_restaurantmenu_courselist, parent, false);
        DataObject_postHolder dataObjectHolder = new DataObject_postHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObject_postHolder holder, final int position) {

        holder.setIsRecyclable(false);
        sharepref = mContext.getApplicationContext().getSharedPreferences("MyPref",mContext.MODE_PRIVATE);

        if(position%2==0){
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            //holder.itemView.setBackgroundColor(Color.parseColor("#00F7F9F9"));
        }

        holder.tv_title.setText(mDataset.get(position).getMenu_desc());
        holder.tv_srNo.setText(""+((int)position+1));
        holder.tv_course_totalcount.setText("Max Items Allowed :"+mDataset.get(position).getTotal_courses());
        holder.tv_price.setText(" $ "+mDataset.get(position).getPrice_per_plate());
        holder.tv_price.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, VenueCateringFoodAddItem.class)
                        .putExtra("menu_id",mDataset.get(position).getMenu_id())
                        .putExtra("course_id",mDataset.get(position).getCourseID())
                        .putExtra("title",mDataset.get(position).getMenu_desc())
                        .putExtra("guest_count",service_food_guest_count));

                //Log.d("course_id id",mDataset.get(getAdapterPosition()).getMenu_id());
            }
        });
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}