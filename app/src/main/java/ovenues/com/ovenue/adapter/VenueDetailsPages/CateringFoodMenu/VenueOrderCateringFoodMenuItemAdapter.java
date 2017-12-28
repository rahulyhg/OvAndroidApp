package ovenues.com.ovenue.adapter.VenueDetailsPages.CateringFoodMenu;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.modelpojo.ServiceProviderChargesMenuModel.FoodMenuAddItemModel;

import static ovenues.com.ovenue.VenueCateringActivites.VenueCateringFoodAddItem.selected_items_array;


/**
 * Created by Jay-Andriod on 10-May-17.
 */


public class VenueOrderCateringFoodMenuItemAdapter extends
        RecyclerView.Adapter<VenueOrderCateringFoodMenuItemAdapter.ViewHolder> {

    int checked_count=0;
    Context mContext;
    static private List<FoodMenuAddItemModel> stList;

    public VenueOrderCateringFoodMenuItemAdapter(Context mContext,List<FoodMenuAddItemModel> students) {
        this.stList = students;
        this.mContext=mContext;

    }

    // Create new views
    @Override
    public VenueOrderCateringFoodMenuItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_servicefood_addmenu_item,  parent,false);
        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.setIsRecyclable(false);

        if(position%2==0){
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#00F7F9F9"));
        }

        viewHolder.tvName.setText(stList.get(position).getItem_name());

        if(stList.get(position).getIs_additional_charges().equalsIgnoreCase("1")){
            viewHolder.tv_extra.setText("additional per plate $ "+stList.get(position).getPrice_per_item());
        }else{
            viewHolder.tv_extra.setText("");
        }


        /*if(selected_items_array.get(position).getAsJsonObject().has())*/


        viewHolder.chkSelected.setChecked(stList.get(position).isSelected());

        viewHolder.chkSelected.setTag(stList.get(position));

        if(selected_items_array!=null) {

            for (int j = 0; j < selected_items_array.size(); j++) {

                if (stList.get(viewHolder.getAdapterPosition()).getItem_id().equalsIgnoreCase(selected_items_array.get(j).getAsJsonObject().get("item_id").getAsString())) {

                    viewHolder.chkSelected.setChecked(true);
                    checked_count = checked_count + 1;
                    Log.d("checked_count adapter",""+checked_count);
                    stList.get(position).setSelected(true);
                    break;
                }else {
                    viewHolder.chkSelected.setChecked(false);
                    stList.get(position).setSelected(false);
                }

            }
        }


        viewHolder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked==true){
                    checked_count=checked_count+1;
                    stList.get(position).setSelected(true);
                    if(checked_count > Integer.parseInt(stList.get(position).getMax_item_selectn_allowed())){
                        viewHolder.chkSelected.setChecked(false);
                        stList.get(position).setSelected(false);
                        checked_count=checked_count-1;
                    }else {
                    }
                }else{
                    checked_count=checked_count-1;
                    stList.get(position).setSelected(false);
                }
                for (int i = 0; i < stList.size(); i++) {
                    FoodMenuAddItemModel singleStudent = stList.get(i);
                    if (singleStudent.isSelected() == true) {
                        Log.d("get all true items",stList.get(i).getItem_id());
                    }
                }

                /*FoodMenuAddItemModel contact = (FoodMenuAddItemModel) viewHolder.chkSelected.getTag();
                contact.setSelected(viewHolder.chkSelected.isChecked());
                stList.get(position).setSelected(viewHolder.chkSelected.isChecked());*/
                    /*Toast.makeText(
                            chkSelected.getContext(),
                            "Clicked on Checkbox: " + chkSelected.getText() + " is "
                                    + chkSelected.isChecked(), Toast.LENGTH_LONG).show();*/
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewHolder.chkSelected.isChecked()) {
                    viewHolder.chkSelected.setChecked(false);
                } else {
                    viewHolder.chkSelected.setChecked(true);
                }
            }
        });

    }


    // Return the size arraylist
    @Override
    public int getItemCount() {
        return stList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tv_extra;

        public CheckBox chkSelected;

        public FoodMenuAddItemModel singlestudent;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tvName = (TextView) itemLayoutView.findViewById(R.id.tvName);

            tv_extra = (TextView) itemLayoutView.findViewById(R.id.tv_extra);
            chkSelected = (CheckBox) itemLayoutView
                    .findViewById(R.id.chkSelected);
            chkSelected.setEnabled(false);


        }
    }
    // method to access in activity after updating selection
    public List<FoodMenuAddItemModel> getStudentist() {
        return stList;
    }



}
