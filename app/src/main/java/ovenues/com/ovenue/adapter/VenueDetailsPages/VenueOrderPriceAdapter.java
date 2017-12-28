package ovenues.com.ovenue.adapter.VenueDetailsPages;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.VenueDetailsMainFragment;
import ovenues.com.ovenue.modelpojo.VenueOrderModel.VenueOrderPriceModel;
import ovenues.com.ovenue.utils.Const;
import ovenues.com.ovenue.utils.Utils;

import static ovenues.com.ovenue.VenueDetailsMainFragment.activity_venueDetails;
import static ovenues.com.ovenue.VenueDetailsMainFragment.sharepref;
import static ovenues.com.ovenue.VenueDetailsMainFragment.str_cart_id;
import static ovenues.com.ovenue.VenueDetailsMainFragment.str_no_of_hour;
import static ovenues.com.ovenue.VenueDetailsMainFragment.str_price_pkg_id;
import static ovenues.com.ovenue.VenueDetailsMainFragment.str_venue_charge;
import static ovenues.com.ovenue.VenueDetailsMainFragment.str_venue_charge_duration;
import static ovenues.com.ovenue.VenueDetailsMainFragment.str_venue_id;
import static ovenues.com.ovenue.VenueDetailsMainFragment.venue_cart_flag;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.str_dateselected;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.str_guest_count;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.str_timeslot;
import static ovenues.com.ovenue.utils.APICall.post;

/**
 * Created by Jay-Andriod on 11-May-17.
 */


public class VenueOrderPriceAdapter extends
        RecyclerView.Adapter<VenueOrderPriceAdapter.DataViewHolder> {

    static Context mContext;
    public static double final_total;
    public static String selected_hr,selected_min;

    static private List<VenueOrderPriceModel> stList;
    static private String str_id, str_charges, str_is_flat_charges, str_is_per_person_charges;
            /*str_hour_extension_charges, str_extra_person_charges=null, str_is_group_size, str_group_size_from,
            str_group_size_to*/;
    public static boolean isPkgAdded=false;

    public VenueOrderPriceAdapter(Context mContext, List<VenueOrderPriceModel> students) {
        this.mContext = mContext;
        this.stList = students;
    }

    // Create new views
    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_venueorder_price, parent,false);
        //Log.e("on create called---","dsdf--111");
        // create ViewHolder

        DataViewHolder viewHolder = new DataViewHolder(itemLayoutView);

        final_total=0;


        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final DataViewHolder holder, final int positiona) {

        holder.setIsRecyclable(false);
        //Log.e("on bind called---","dsdf"+positiona);



        final int position = holder.getAdapterPosition();

        String extra_duration_charge = "";
        str_id= stList.get(position).getId();

        if(stList.get(position).getIs_applicable().equalsIgnoreCase("0")){
            holder.itemView.setEnabled(false);
            holder.itemView.setClickable(false);
            holder.itemView.setBackgroundResource(R.color.md_blue_grey_50);
            holder.tv_addtocart.setVisibility(View.GONE);
        }


        if (stList.get(position).isSelected()==true){
            holder.img_remove.setVisibility(View.VISIBLE);
            holder.tv_addtocart.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_greyselected_white_borde,mContext.getTheme()));
        }else{
            holder.img_remove.setVisibility(View.GONE);
            holder.tv_addtocart.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_orange_white_borde,mContext.getTheme()));
        }

        holder.tvName.setText(stList.get(position).getPackage_name());
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stList.get(position).getChrages_inclusion()!=null){

                    new AlertDialog.Builder(mContext)
                            .setMessage(stList.get(position).getChrages_inclusion())
                            .setPositiveButton("OK", null)
                            .show();
                }else{

                }
            }
        });

        holder.tv_details.setText(stList.get(position).getChrages_inclusion());
        String weekdays = stList.get(position).getWeek_days();
        String[] separated = weekdays.split(",");
        String allweekdays="";
        for(int a=0;a<separated.length;a++){
            allweekdays=allweekdays+separated[a].substring(0,3)+" , ";
        }
        holder.tv_weekdays.setText(allweekdays.substring(0,allweekdays.length()-2));
        holder.tv_weektimes.setText(Utils.convertDateStringToString(stList.get(position).getTime_from(),"HH:mm:ss","hh:mm aa")
                +" - "
                +Utils.convertDateStringToString(stList.get(position).getTime_to(),"HH:mm:ss","hh:mm aa"));

        holder.tv_price.setText(" $ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(stList.get(position).getCharges())));

        if(stList.get(position).getPacakage_hours()==null || stList.get(position).getPacakage_hours().equalsIgnoreCase("00:00:00")){
            holder.tv_duration.setVisibility(View.GONE);
        }else{
            holder.tv_duration.setText(stList.get(position).getPacakage_hours().substring(0,stList.get(position).getPacakage_hours().length()-3)+" hr");
        }

        if(stList.get(position).getIs_group_charges().equalsIgnoreCase("1")){
            holder.tv_guestCount.setText(stList.get(position).getGroup_size_from()
                    +"-"
                    +stList.get(position).getGroup_size_to() +" Guests");
        }else{
            holder.tv_guestCount.setVisibility(View.GONE);
        }



//======EXTRA GUEST AND TIME CONDITION FOR TEXT VIEW ==========================================
        if(stList.get(position).getIs_hour_extension_charges()!=null) {
            if(stList.get(position).getIs_hour_extension_charges().equalsIgnoreCase("1")){
                holder.ll_extra_charge.setVisibility(View.VISIBLE);
                if (stList.get(position).getExtension_hours().substring(0, 2).equalsIgnoreCase("00")) {
                    extra_duration_charge = "$ " + stList.get(position).getHour_extension_charges() + " / extra" + stList.get(position).getExtension_hours().substring(3, 5) + " min";
                } else {
                    extra_duration_charge = "$ " + stList.get(position).getHour_extension_charges() + " / extra" + stList.get(position).getExtension_hours().substring(0, 5) + " hours";
                }

                holder.tv_extra_charge.setText(extra_duration_charge);
            }
        }


        if(stList.get(position).getIs_group_charges().equalsIgnoreCase("1") &&  stList.get(position).getIs_extra_person_charges().equalsIgnoreCase("1")){

            if(extra_duration_charge!=null){
                extra_duration_charge = extra_duration_charge+"\n"+"$ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(stList.get(position).getExtra_person_charges()))+"/ extra person";
            }else{
                extra_duration_charge= "$ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(stList.get(position).getExtra_person_charges()))+"/ extra person";
            }
            holder.tv_extra_charge.setText(extra_duration_charge);
        }


        if(extra_duration_charge==null || extra_duration_charge.length()<1){
            holder.ll_extra_charge.setVisibility(View.GONE);
            if(stList.get(position).getIs_flat_charges().equalsIgnoreCase("1")){
                //holder.tv_pkg_rate.setText("Flat Rate");
            } else if(stList.get(position).getIs_perperson_charges().equalsIgnoreCase("1")){
                holder.tv_pkg_rate.setText("Per Person");
            } else if(stList.get(position).getIs_perhour_charges().equalsIgnoreCase("1")) {
                holder.tv_pkg_rate.setText("Per Hour");
            }
        }else{
            if(stList.get(position).getIs_flat_charges().equalsIgnoreCase("1")){
               // holder.tv_pkg_rate.setText("Flat Rate");
            } else if(stList.get(position).getIs_perperson_charges().equalsIgnoreCase("1")){
                holder.tv_pkg_rate.setText("Per Person");
            } else if(stList.get(position).getIs_perhour_charges().equalsIgnoreCase("1")) {
                holder.tv_pkg_rate.setText("Per Hour");
            }
        }


//==========FLAT PER PERSON PER HOURE CHARGES CONDITION======================================================
        if(stList.get(position).getIs_flat_charges().equalsIgnoreCase("1")){
            holder.ll_extra.setVisibility(View.INVISIBLE);
            // tv_ext_person.setVisibility(View.GONE);

             /*if(str_extra_person_charges==null ||  str_extra_person_charges.equalsIgnoreCase("0")){
                 if(stList.get(position).getIs_flat_charges().equalsIgnoreCase("1")
                    && (str_extra_person_charges!=null)) {
                     if (!str_extra_person_charges.equalsIgnoreCase("0")) {

                         tv_extra_charge.setText("* flat charge");
                         ll_duration.setVisibility(View.GONE);
                         // np_itemcount.setVisibility(View.VISIBLE);
                         // tv_ext_person.setVisibility(View.VISIBLE);
                     }
                 }else {
                 }
             }*/
        } else if(stList.get(position).getIs_perperson_charges().equalsIgnoreCase("1")){

            if(stList.get(position).getIs_group_charges().equalsIgnoreCase("1") &&  stList.get(position).getExtra_person_charges()!=null){
                holder. ll_extra.setVisibility(View.VISIBLE);
                holder.img_clock.setVisibility(View.GONE);
                holder.sp_hh.setVisibility(View.GONE);
                //np_itemcount.setVisibility(View.VISIBLE);
                // tv_ext_person.setVisibility(View.VISIBLE);
                //np_itemcount.setMaxValue(Integer.parseInt(stList.get(position).getGroup_size_to()));
            }else{
                holder.ll_extra.setVisibility(View.INVISIBLE);
                //tv_ext_person.setVisibility(View.GONE);

            }

            if(stList.get(position).getIs_extra_person_charges().equalsIgnoreCase("0")){
                holder.ll_extra.setVisibility(View.INVISIBLE);
                //tv_ext_person.setVisibility(View.GONE);
            }

        }else if(stList.get(position).getIs_perhour_charges().equalsIgnoreCase("1")) {
            holder.ll_extra.setVisibility(View.VISIBLE);
            holder.img_guest.setVisibility(View.GONE);
            holder.sp_qty.setVisibility(View.GONE);
            //ll_duration.setVisibility(View.GONE);
        }


        List<String> list_hh = new ArrayList<String>();
        list_hh.add("HH");
        list_hh.add("01");
        list_hh.add("02");
        list_hh.add("03");
        list_hh.add("04");
        list_hh.add("05");
        list_hh.add("06");
        list_hh.add("07");
        list_hh.add("08");
        list_hh.add("09");
        list_hh.add("10");
        list_hh.add("11");
        list_hh.add("12");

        ArrayAdapter<String> dataAdapter_hh = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, list_hh);
        dataAdapter_hh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.sp_hh.setAdapter(dataAdapter_hh);


        List<String> list_mm = new ArrayList<String>();
        list_mm.add("MM");
        list_mm.add("00");
        list_mm.add("15");
        list_mm.add("30");
        list_mm.add("45");

        ArrayAdapter<String> dataAdapter_mm = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, list_mm);
        dataAdapter_mm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.sp_mm.setAdapter(dataAdapter_mm);

        List<String> list_qty = new ArrayList<String>();
        list_qty.add("Guest");
        for(int a=0;a<100;a++){
            list_qty.add(Integer.toString(a));
        }
        ArrayAdapter<String> dataAdapter_qty = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, list_qty);
        dataAdapter_qty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.sp_qty.setAdapter(dataAdapter_qty);


       /* sp_qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positionSP, long id) {

                    if(positionSP > 1) {
                        final_total = Double.parseDouble(stList.get(position).getCharges())+(Double.parseDouble(stList.get(position).getCharges()) * (double) (sp_hh.getSelectedItemPosition()));
                    }else{
                        final_total = Double.parseDouble(stList.get(position).getCharges());
                    }

                           *//* Toast.makeText(parent.getContext(), "Time : " +
                                     sp_hh.getItemAtPosition(sp_hh.getSelectedItemPosition()).toString()
                                    + ":"
                                    +String.valueOf(sp_mm.getSelectedItem()), Toast.LENGTH_SHORT).show();*//*
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        holder.sp_hh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positionSP, long id) {

                if(holder.sp_mm.getSelectedItemPosition()>1 && positionSP>1){
                    final_total = Double.parseDouble(stList.get(position).getCharges())+(Double.parseDouble(stList.get(position).getCharges()) * (double) (holder.sp_hh.getSelectedItemPosition()  +1) );
                }else{
                    if(positionSP > 1) {
                        final_total = Double.parseDouble(stList.get(position).getCharges())+(Double.parseDouble(stList.get(position).getCharges()) * (double) (holder.sp_hh.getSelectedItemPosition()));
                    }else{
                        final_total = Double.parseDouble(stList.get(position).getCharges());
                    }
                }
                /* Toast.makeText(parent.getContext(), "Time : " +
                                     sp_hh.getItemAtPosition(sp_hh.getSelectedItemPosition()).toString()
                                    + ":"
                                    +String.valueOf(sp_mm.getSelectedItem()), Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        holder.sp_mm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positionSP, long id) {
                if(positionSP>1){
                    final_total = Double.parseDouble(stList.get(position).getCharges())+(Double.parseDouble(stList.get(position).getCharges()) *(double) (holder.sp_hh.getSelectedItemPosition() + 1 ));
                    // tv_menutitle_venuepricing.setText("Pricing Plans "+" ( Total : "+final_total+" )");
                }else{
                    if(holder.sp_hh.getSelectedItemPosition()>0){
                        final_total =Double.parseDouble(stList.get(position).getCharges())+(Double.parseDouble(stList.get(position).getCharges()) * (double) (holder.sp_hh.getSelectedItemPosition()));
                    }else{
                        final_total = Double.parseDouble(stList.get(position).getCharges());
                    }
                }
                            /*Toast.makeText(parent.getContext(), "Time : " + sp_hh.getItemAtPosition(sp_hh.getSelectedItemPosition()).toString()
                                    + ":" +sp_mm.getItemAtPosition(sp_mm.getSelectedItemPosition()).toString(), Toast.LENGTH_SHORT).show();*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        holder.img_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPkgAdded=false;
                stList.get(position).setSelected(false);
               // holder.cb_selectedprice.setChecked(false);
                holder.img_remove.setVisibility(View.GONE);
                holder.tv_addtocart.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_orange_white_borde,mContext.getTheme()));
                holder.tv_addtocart.setEnabled(true);
                final_total=0;
            }
        });

        holder.tv_addtocart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (isPkgAdded == true /*|| holder.cb_selectedprice.isChecked()==true*/) {
                    Toast.makeText(mContext, "First Remove Added Pacakge", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("boooking details --",str_dateselected+str_guest_count+str_timeslot);
                    if (str_dateselected==null || str_guest_count==null || str_timeslot==null) {
                        Toast.makeText(mContext, "Please choose Date ,Time and Guests to selecte pricing.", Toast.LENGTH_LONG).show();
                    } else {

                        isPkgAdded = true;
                        //holder.cb_selectedprice.setChecked(true);
                        holder.img_remove.setVisibility(View.VISIBLE);
                        holder.tv_addtocart.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_greyselected_white_borde,mContext.getTheme()));
                        holder.tv_addtocart.setEnabled(false);

                        if (stList.get(position).getIs_flat_charges().equalsIgnoreCase("1")) {

                            if (stList.get(position).getIs_group_charges().equalsIgnoreCase("1") && stList.get(position).getIs_extra_person_charges().equalsIgnoreCase("1")) {
                                int guest_from = Integer.parseInt(stList.get(position).getGroup_size_from());
                                int guest_to = Integer.parseInt(stList.get(position).getGroup_size_to());
                                int guest = Integer.parseInt(str_guest_count);

                                if (guest > guest_to) {
                                    int extra_guest = guest - guest_to;
                                    final_total = ((double) extra_guest * Double.parseDouble(stList.get(position).getExtra_person_charges()))
                                            + Double.parseDouble(stList.get(position).getCharges());
                                    // tv_menutitle_venuepricing.setText("Pricing Plans " + " ( Total : " + Const.GLOBAL_FORMATTER.format(final_total) + " )");
                                } else {
                                    final_total = Double.parseDouble(stList.get(position).getCharges());
                                    // tv_menutitle_venuepricing.setText("Pricing Plans " + " ( Total : "+ Const.GLOBAL_FORMATTER.format(final_total) + " )");
                                }

                            } else {
                                final_total = Double.parseDouble(stList.get(position).getCharges());
                                // tv_menutitle_venuepricing.setText("Pricing Plans " + " ( Total : "+ Const.GLOBAL_FORMATTER.format(final_total) + " )");
                            }
                        } else if (stList.get(position).getIs_perperson_charges().equalsIgnoreCase("1")) {
                            final_total = Double.parseDouble(stList.get(position).getCharges()) * Double.parseDouble(str_guest_count);
                            //  tv_menutitle_venuepricing.setText("Pricing Plans "+" ( Total : "+final_total+" )");

                        } else if (stList.get(position).getIs_perhour_charges().equalsIgnoreCase("1")) {

                            if (holder.sp_hh.getSelectedItemPosition() > 1 && holder.sp_mm.getSelectedItemPosition() > 1) {

                                selected_hr = holder.sp_hh.getSelectedItem().toString();
                                if (selected_hr.length() < 0) {
                                    selected_hr = "0" + selected_hr;
                                }

                                selected_min = holder.sp_mm.getSelectedItem().toString();
                                if (selected_min.length() < 0) {
                                    selected_min = "0" + selected_min;
                                }

                                final_total = Double.parseDouble(stList.get(position).getCharges()) * (double) (holder.sp_hh.getSelectedItemPosition() + 1);

                            } else if (holder.sp_hh.getSelectedItemPosition() > 1 && holder.sp_mm.getSelectedItemPosition() == 0) {

                                selected_hr = holder.sp_hh.getSelectedItem().toString();
                                if (selected_hr.length() < 0) {
                                    selected_hr = "0" + selected_hr;
                                }

                                selected_min = "0";
                                if (selected_min.length() < 0) {
                                    selected_min = "0" + selected_min;
                                }

                                final_total = Double.parseDouble(stList.get(position).getCharges()) * (double) (holder.sp_hh.getSelectedItemPosition());

                            } else if (holder.sp_hh.getSelectedItemPosition() == 0 && holder.sp_mm.getSelectedItemPosition() > 0) {

                                selected_hr = holder.sp_hh.getSelectedItem().toString();
                                if (selected_hr.length() < 0) {
                                    selected_hr = "0" + selected_hr;
                                }

                                selected_min = "0";
                                if (selected_min.length() < 0) {
                                    selected_min = "0" + selected_min;
                                }

                                final_total = Double.parseDouble(stList.get(position).getCharges()) * (double) (holder.sp_hh.getSelectedItemPosition() + 2);

                            } else {
                                final_total = Double.parseDouble(stList.get(position).getCharges());

                                selected_hr = "00";
                                if (selected_hr.length() < 1) {
                                    selected_hr = "0" + selected_hr;
                                }

                                selected_min = "00";
                                if (selected_min.length() < 1) {
                                    selected_min = "0" + selected_min;
                                }
                            }
                            Toast.makeText(mContext, "" + final_total, Toast.LENGTH_LONG).show();
                        }
                        str_price_pkg_id = stList.get(position).getId();
                        str_venue_charge = stList.get(position).getCharges();
                        if (stList.get(position).getIs_perhour_charges().equalsIgnoreCase("1")) {
                            str_venue_charge_duration = selected_hr + " : " + selected_min + " : 00";
                            str_no_of_hour = str_venue_charge_duration.substring(0, str_venue_charge_duration.length() - 3);
                        } else {
                            str_venue_charge_duration = stList.get(position).getPacakage_hours();
                        }

                        stList.get(position).setSelected(true);
                        new VenueDetailsMainFragment.AddVenuePricingPackage().execute();

                        // Toast.makeText(mContext, "Total : " + final_total, Toast.LENGTH_LONG).show();

                    }

                }
            }
        });

    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return stList.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName,tv_details,tv_weekdays ,tv_weektimes,tv_duration ,tv_guestCount,tv_price,tv_extra_charge,tv_addtocart,tv_pkg_rate;
        /*CheckBox cb_selectedprice;*/
        private LinearLayout ll_extra,ll_extra_charge;
        Spinner sp_hh,sp_mm,sp_qty;
        ImageView img_clock,img_guest,img_remove;

        public DataViewHolder(View itemLayoutView) {
            super(itemLayoutView);


            tvName = (TextView) itemLayoutView.findViewById(R.id.tvName);
            tv_details = (TextView) itemLayoutView.findViewById(R.id.tv_details);
            tv_weekdays = (TextView) itemLayoutView.findViewById(R.id.tv_weekdays);
            tv_weektimes = (TextView) itemLayoutView.findViewById(R.id.tv_weektimes);
            tv_duration = (TextView) itemLayoutView.findViewById(R.id.tv_duration);
            tv_guestCount = (TextView) itemLayoutView.findViewById(R.id.tv_guestCount);
            tv_price =  (TextView) itemLayoutView.findViewById(R.id.tv_price);
            tv_extra_charge =   (TextView)itemLayoutView.findViewById(R.id.tv_extra_charge);
            tv_pkg_rate  =   (TextView)itemLayoutView.findViewById(R.id.tv_pkg_rate);

            img_clock  =   (ImageView) itemLayoutView.findViewById(R.id.img_clock);
            img_guest  =   (ImageView) itemLayoutView.findViewById(R.id.img_guest);
            img_remove  =   (ImageView) itemLayoutView.findViewById(R.id.img_remove);

            sp_hh = (Spinner)itemView.findViewById(R.id.sp_hh);
            sp_mm = (Spinner)itemView.findViewById(R.id.sp_mm);
            sp_qty = (Spinner)itemView.findViewById(R.id.sp_qty);

          //  tv_ext_person= (TextView)viewHolder.itemView.findViewById(R.id.tv_ext_person);

            ll_extra =(LinearLayout)itemView.findViewById(R.id.ll_extra);
            ll_extra_charge  =(LinearLayout)itemView.findViewById(R.id.ll_extra_charge);

            tv_addtocart= (TextView) itemView.findViewById(R.id.tv_addtocart);

            //cb_selectedprice =(CheckBox)itemView.findViewById(R.id.cb_selectedprice);

        }

    }

    // method to access in activity after updating selection
    public List<VenueOrderPriceModel> getStudentist() {
        return stList;
    }


}