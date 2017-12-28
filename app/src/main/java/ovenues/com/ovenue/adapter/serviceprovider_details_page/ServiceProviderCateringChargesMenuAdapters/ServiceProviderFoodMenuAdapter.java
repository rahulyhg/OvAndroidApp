package ovenues.com.ovenue.adapter.serviceprovider_details_page.ServiceProviderCateringChargesMenuAdapters;


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

import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;

import java.util.ArrayList;

import ovenues.com.ovenue.ServiceProviderCateringActivity.ServiceProviderFoodmenuSubCategory;
import ovenues.com.ovenue.R;
import ovenues.com.ovenue.modelpojo.ServiceProviderChargesMenuModel.FoodMenuModel;

/**
 * Created by Jay-Andriod on 08-May-17.
 */


public class ServiceProviderFoodMenuAdapter extends RecyclerView
        .Adapter<ServiceProviderFoodMenuAdapter
        .DataObject_postHolder> {
    static private ArrayList<FoodMenuModel> mDataset;
    static private Context mContext;

    SharedPreferences sharepref;


    public static class DataObject_postHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ScrollableNumberPicker np_itemcount;
        TextView tv_srNo,tv_title ,tv_course_totalcount,tv_price,tv_price_copy,tv_price_total;


        public DataObject_postHolder(final View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_srNo = (TextView) itemView.findViewById(R.id.tv_srNo);
            tv_course_totalcount = (TextView) itemView.findViewById(R.id.tv_course_totalcount);
            tv_price= (TextView) itemView.findViewById(R.id.tv_price);
            tv_price_copy= (TextView) itemView.findViewById(R.id.tv_price_copy);
            tv_price_total= (TextView) itemView.findViewById(R.id.tv_price_total);

            np_itemcount=(ScrollableNumberPicker)itemView.findViewById(R.id.np_itemcount);

        }

        @Override
        public void onClick(View v) {
            //     myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

   /* public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }*/

    public ServiceProviderFoodMenuAdapter(ArrayList<FoodMenuModel> myDataset, Context context) {
        mDataset = myDataset;
        mContext=context;
    }

    @Override
    public DataObject_postHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_foodmenu_courselist, parent, false);
        ServiceProviderFoodMenuAdapter.DataObject_postHolder dataObjectHolder = new ServiceProviderFoodMenuAdapter.DataObject_postHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObject_postHolder holder, final int position) {

        holder.setIsRecyclable(false);
        sharepref = mContext.getApplicationContext().getSharedPreferences("MyPref",mContext.MODE_PRIVATE);

        if(position%2==0){
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#00F7F9F9"));
        }

        holder.tv_title.setText(mDataset.get(position).getMenu_desc());
        holder.tv_srNo.setText(""+((int)position+1));
        holder.tv_course_totalcount.setText("Total Course :"+mDataset.get(position).getTotal_courses());
        holder.tv_price.setText(" $ "+mDataset.get(position).getPrice_per_plate());
        holder.tv_price_copy.setText(" $ "+mDataset.get(position).getPrice_per_plate());

        Log.d("is groupsize",""+mDataset.get(position).getIs_group_size());
        if(mDataset.get(position).getIs_group_size()!=null && mDataset.get(position).getIs_group_size().equalsIgnoreCase("1")){
            holder.np_itemcount.setMaxValue(Integer.parseInt(mDataset.get(position).getGroup_size_to()));
            holder.np_itemcount.setMinValue(Integer.parseInt(mDataset.get(position).getGroup_size_from()));
            holder.np_itemcount.setValue(Integer.parseInt(mDataset.get(position).getGroup_size_from()));

            holder.tv_course_totalcount.setText(holder.tv_course_totalcount.getText().toString()+"\n QTY ( "+mDataset.get(position).getGroup_size_from()+
                    " - "+mDataset.get(position).getGroup_size_to()+" )");
        }

        holder.np_itemcount.setListener(new ScrollableNumberPickerListener() {
            @Override
            public void onNumberPicked(int value) {
                double total_item = (double)value * Double.parseDouble(mDataset.get(position).getPrice_per_plate());
                holder.tv_price_total.setText("Total : $ "+total_item);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ServiceProviderFoodmenuSubCategory.class)
                        .putExtra("menu_id",mDataset.get(position).getMenu_id())
                        .putExtra("title",mDataset.get(position).getMenu_desc())
                        .putExtra("guest_count",Integer.toString(holder.np_itemcount.getValue())));
                Log.d("guest_count",""+holder.np_itemcount.getValue());
            }
        });
    }



    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}