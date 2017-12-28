package ovenues.com.ovenue.bookingdetails;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.utils.Const;
import ovenues.com.ovenue.utils.Utils;

import static ovenues.com.ovenue.utils.APICall.post;
import static ovenues.com.ovenue.utils.Utils.convertDateStringToString;

/**
 * Created by Jay-Andriod on 03-Jul-17.
 */

public class BookingListMainAdapter extends RecyclerView.Adapter<BookingListMainAdapter.DataObject_postHolder> {
    static private ArrayList<BookingVenueModel> mDataset;
    static private Context mContext;

    SharedPreferences sharepref;
    Dialog dialog;
    int data_index,sp_booking=0,venue_booking=0;

    String str_booking_id;

    public static class DataObject_postHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_title,tv_orderDate,tv_eventDate,tv_eventTime,tv_bookingID ,tv_orderStatus ;
        ImageView img_booking,tv_removeOrder;


        public DataObject_postHolder(final View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_orderDate = (TextView) itemView.findViewById(R.id.tv_orderDate);
            tv_eventDate = (TextView) itemView.findViewById(R.id.tv_eventDate);
            tv_eventTime= (TextView) itemView.findViewById(R.id.tv_eventTime);

            tv_bookingID = (TextView) itemView.findViewById(R.id.tv_bookingID);
            tv_orderStatus= (TextView) itemView.findViewById(R.id.tv_orderStatus);
            tv_removeOrder = (ImageView) itemView.findViewById(R.id.tv_removeOrder);

            img_booking=(ImageView)itemView.findViewById(R.id.img_booking);

           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*/

        }

        @Override
        public void onClick(View v) {
            //     myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

   /* public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }*/

    public BookingListMainAdapter(ArrayList<BookingVenueModel> myDataset, Context context) {
        mDataset = myDataset;
        mContext=context;
    }

    @Override
    public BookingListMainAdapter.DataObject_postHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_bookinglist_main, parent, false);
        BookingListMainAdapter.DataObject_postHolder dataObjectHolder = new BookingListMainAdapter.DataObject_postHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final BookingListMainAdapter.DataObject_postHolder holder, final int position) {

        holder.setIsRecyclable(false);
        sharepref = mContext.getApplicationContext().getSharedPreferences("MyPref",mContext.MODE_PRIVATE);

        try {

            if(mDataset.get(position).getBooking_type_icon().toString()!=null) {
                final String thumb_icon= Const.WEBSITE_PIC_URL + mDataset.get(position).getBooking_type_icon().toString();
                //Log.d("imageURL====",venue_pic);
                Glide.with(mContext)
                        .load(thumb_icon)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .crossFade()
                        .dontAnimate()
                        .skipMemoryCache(true)
                        .error(R.drawable.ic_noimage_placeholder)
                        .into(holder.img_booking);


            }

        }catch (Exception expbitmap){
            expbitmap.printStackTrace();
        }

        if(mDataset.get(position).getBooking_type().equalsIgnoreCase("1")){
            holder.tv_title.setText(mDataset.get(position).getVenue_name());
            holder.tv_eventDate.setText(convertDateStringToString(mDataset.get(position).getBooking_time_from(),"yyyy-MM-dd HH:mm:ss","MM-dd-yyyy"));
            holder.tv_eventTime.setText(convertDateStringToString(mDataset.get(position).getBooking_time_from(),"yyyy-MM-dd HH:mm:ss","hh:mm aa")
                    +" - "+convertDateStringToString(mDataset.get(position).getBooking_time_to(),"yyyy-MM-dd HH:mm:ss","hh:mm aa"));

        }else if(mDataset.get(position).getBooking_type().equalsIgnoreCase("2") || mDataset.get(position).getBooking_type().equalsIgnoreCase("3")){
            holder.tv_title.setText(mDataset.get(position).getProvider_name());
            holder.tv_eventDate.setText(convertDateStringToString(mDataset.get(position).getBooking_time(),"yyyy-MM-dd HH:mm:ss","MM-dd-yyyy"));
            holder.tv_eventTime.setText(convertDateStringToString(mDataset.get(position).getBooking_time(),"yyyy-MM-dd HH:mm:ss","hh:mm aa"));
        }
        holder.tv_orderDate.setText(convertDateStringToString(mDataset.get(position).getBooked_on(),"yyyy-MM-dd HH:mm:ss","MM-dd-yyyy")
                /*+"\n"
                +convertDateStringToString(mDataset.get(position).getBooked_on(),"yyyy-MM-dd HH:mm:ss","hh:mm aa")*/);
        holder.tv_bookingID.setText("Booking ID : "+mDataset.get(position).getBooking_id());
        holder.tv_orderStatus.setText(mDataset.get(position).getBooking_status());

        if(mDataset.get(position).getBooking_status().equalsIgnoreCase("Request Pending")){
            holder.tv_removeOrder.setVisibility(View.VISIBLE);
        }else {
            holder.tv_removeOrder.setVisibility(View.GONE);
        }

        holder.tv_removeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                str_booking_id = mDataset.get(position).getBooking_id();
                if(mDataset.get(position).getVenue_id()!=null){
                    venue_booking=1;
                    sp_booking=0;
                }else if(mDataset.get(position).getService_id()!=null){
                    venue_booking=0;
                    sp_booking=1;
                }


                final AlertDialog.Builder alertbox = new AlertDialog.Builder(mContext);
                alertbox.setMessage("Are you sure to cancel booking?");
                alertbox.setTitle("Delete ?.");
                alertbox.setIcon(R.mipmap.ic_launcher);

                alertbox.setNeutralButton("YES",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0,int arg1) {

                                data_index = position ;
                                new CancelBooking().execute();


                            }
                        });

                alertbox.setPositiveButton("NO",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0,int arg1) {
                                arg0.dismiss();
                            }
                        });
                alertbox.show();

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_dialog_full_bookingdetails);

                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                // display size in pixels
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;

                dialog.setCanceledOnTouchOutside(true);
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.width = width;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.LEFT;
                lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                lp.dimAmount = 0.81f;
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN );
                dialog.getWindow().setAttributes(lp);

               // CardView cv_bookingdetails ,cv_delivery_details ,cv_itemdetails;
                final TextView tv_providerName,tv_bookingnumber,guestNumber,tv_address,tv_deliverytime,tv_total_booking_amount,
                        tv_bookingDetails_orderDate ,tv_bookingDetails_eventDate,dialog_tv_orderDate;
                TableLayout tl_booking_item_details;
                //TableRow tr_head;
                LinearLayout ll_delivery_details;
                boolean is_extracharge=false;

                ImageView dialog_img_booking,dialog_tv_removeOrder;

                //cv_bookingdetails = (CardView)dialog.findViewById(R.id.cv_bookingdetails);
                //cv_delivery_details = (CardView)dialog.findViewById(R.id.cv_delivery_details);
                //cv_itemdetails = (CardView)dialog.findViewById(R.id.cv_itemdetails);

                ll_delivery_details= (LinearLayout)dialog.findViewById(R.id.ll_delivery_details);
                tv_providerName  = (TextView) dialog.findViewById(R.id.tv_providerName);
                tv_providerName.setText(holder.tv_title.getText().toString());

                tv_bookingnumber = (TextView) dialog.findViewById(R.id.tv_bookingnumber);
                guestNumber = (TextView) dialog.findViewById(R.id.guestNumber);
                tv_address = (TextView) dialog.findViewById(R.id.tv_address);
                tv_deliverytime = (TextView) dialog.findViewById(R.id.tv_deliverytime);
                dialog_tv_orderDate  = (TextView) dialog.findViewById(R.id.tv_orderDate);
                tv_bookingDetails_orderDate = (TextView) dialog.findViewById(R.id.tv_bookingDetails_orderDate);
                tv_bookingDetails_eventDate = (TextView) dialog.findViewById(R.id.tv_bookingDetails_eventDate);

                dialog_tv_removeOrder = (ImageView) dialog.findViewById(R.id.tv_removeOrder);
                dialog_img_booking =(ImageView)dialog.findViewById(R.id.img_booking);


                if(mDataset.get(position).getBooking_type_icon().toString()!=null) {
                    final String thumb_icon= Const.WEBSITE_PIC_URL + mDataset.get(position).getBooking_type_icon().toString();
                    //Log.d("imageURL====",venue_pic);
                    Glide.with(mContext)
                            .load(thumb_icon)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .crossFade()
                            .dontAnimate()
                            .skipMemoryCache(true)
                            .error(R.drawable.ic_noimage_placeholder)
                            .into(dialog_img_booking);


                }



                dialog_tv_orderDate.setText(holder.tv_orderDate.getText().toString());
                tv_bookingDetails_orderDate.setText(holder.tv_eventDate.getText().toString());
                tv_bookingDetails_eventDate.setText(holder.tv_eventTime.getText().toString());

                guestNumber.setText(mDataset.get(position).getNumber_of_guests());
                tv_bookingnumber.setText(holder.tv_bookingID.getText().toString());

                tl_booking_item_details=(TableLayout)dialog.findViewById(R.id.tl_booking_item_details);
                tv_total_booking_amount = (TextView) dialog.findViewById(R.id.tv_total_booking_amount);

                /** Creating a TextView to add to the row **/
                TableRow tr_head_def = new TableRow(mContext);
                TextView tv_name = new TextView(mContext);
                tv_name.setTypeface(Utils.getFont(mContext, 100));
                tv_name.setText("NAME");
                tv_name.setTextSize(17);
                tv_name.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                tv_name.setTextColor(Color.BLACK);
                tv_name.setPadding(5, 5, 5, 0);
                tv_name.setGravity(Gravity.LEFT);
                tr_head_def.addView(tv_name);  // Adding textView to tablerow.

                /** Creating another textview **/
                TextView tv_price = new TextView(mContext);
                tv_price.setTypeface(Utils.getFont(mContext, 100));
                tv_price.setGravity(Gravity.RIGHT);
                tv_price.setTextSize(17);
                tv_price.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                tv_price.setText("PRICE ($)");
                tv_price.setTextColor(Color.BLACK);
                tv_price.setPadding(5, 5, 5, 0);

                tr_head_def.addView(tv_price); // Adding textView to tablerow.

                // Add the TableRow to the TableLayout
                tl_booking_item_details.addView(tr_head_def, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));



                ///================VENUES BOOKING DETAILS==============================================================================================
                if(mDataset.get(position).getBooking_type().equalsIgnoreCase("1")){

                    //cv_delivery_details.setVisibility(View.GONE);
                    ll_delivery_details.setVisibility(View.GONE);

                    View new_view_def = new View(mContext);
                    new_view_def.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 3));
                    new_view_def.setBackgroundResource(R.drawable.line_divider_view);
                    tl_booking_item_details.addView(new_view_def, new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    TableRow tr_head_booking = new TableRow(mContext);
                    TextView tv_name_booking_cost = new TextView(mContext);
                    tv_name_booking_cost.setTypeface(Utils.getFont(mContext, 100));
                    tv_name_booking_cost.setText("Booking Rent");
                    tv_name_booking_cost.setTextSize(17);
                    tv_name_booking_cost.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                    tv_name_booking_cost.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                    tv_name_booking_cost.setPadding(5, 5, 5, 0);
                    tv_name_booking_cost.setGravity(Gravity.LEFT);
                    tr_head_booking.addView(tv_name_booking_cost);  // Adding textView to tablerow.

                    /** Creating another textview **/
                    TextView tv_price_booking_cost = new TextView(mContext);
                    tv_price_booking_cost.setTypeface(Utils.getFont(mContext, 100));
                    tv_price_booking_cost.setGravity(Gravity.RIGHT);
                    tv_price_booking_cost.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                    tv_price_booking_cost.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                    tv_price_booking_cost.setText(Const.GLOBAL_FORMATTER.format(Double.parseDouble(mDataset.get(position).getBooking_rent())));
                    tv_price_booking_cost.setPadding(5, 5, 5, 0);

                    tr_head_booking.addView(tv_price_booking_cost); // Adding textView to tablerow.

                    // Add the TableRow to the TableLayout
                    tl_booking_item_details.addView(tr_head_booking, new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    /////==== CATERING MENU VIEW====================================================================================
                    if(mDataset.get(position).getCatering_price()!=null && mDataset.get(position).getCatering_price().size()>0 ){


                        View new_view = new View(mContext);
                        new_view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 3));
                        new_view.setBackgroundResource(R.drawable.line_divider_view);
                        tl_booking_item_details.addView(new_view, new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));

                        JsonArray catering_array = mDataset.get(position).getCatering_price();

                        double catring_full_total=0,menu_def_price=0;

                        for(int cat_array_index =0;cat_array_index <catering_array.size();cat_array_index++) {

                            JsonObject menu_obj = catering_array.get(cat_array_index).getAsJsonObject();
                            String menu_title = menu_obj.get("menu_desc").getAsString();
                            String total_menu_price = menu_obj.get("total_food_price").getAsString();
                            String guest_count = menu_obj.get("guest_count").getAsString();
                            catring_full_total=catring_full_total+Double.parseDouble(total_menu_price);
                        }

                        TableRow tr_head_venu_catering = new TableRow(mContext);
                        TextView tv_name_catering_cost = new TextView(mContext);
                        tv_name_catering_cost.setTypeface(Utils.getFont(mContext, 100));
                        tv_name_catering_cost.setText("Catering Cost");
                        tv_name_catering_cost.setTextSize(17);
                        tv_name_catering_cost.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                        tv_name_catering_cost.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                        tv_name_catering_cost.setPadding(5, 5, 5, 0);
                        tv_name_catering_cost.setGravity(Gravity.LEFT);
                        tr_head_venu_catering.addView(tv_name_catering_cost);  // Adding textView to tablerow.

                       /* * Creating another textview **/
                        TextView tv_catering_booking_cost = new TextView(mContext);
                        tv_catering_booking_cost.setTypeface(Utils.getFont(mContext, 100));
                        tv_catering_booking_cost.setGravity(Gravity.RIGHT);
                        tv_catering_booking_cost.setTextSize(17);
                        tv_catering_booking_cost.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                        tv_catering_booking_cost.setText(Const.GLOBAL_FORMATTER.format(catring_full_total));
                        tv_catering_booking_cost.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                        tv_catering_booking_cost.setPadding(5, 5, 5, 0);

                        tr_head_venu_catering.addView(tv_catering_booking_cost); // Adding textView to tablerow.

                        // Add the TableRow to the TableLayout
                        tl_booking_item_details.addView(tr_head_venu_catering, new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));


                        //=======CATERING MENU TITLE

                        for(int cat_array_index =0;cat_array_index <catering_array.size();cat_array_index++) {

                            JsonObject menu_obj = catering_array.get(cat_array_index).getAsJsonObject();
                            String menu_title = menu_obj.get("menu_desc").getAsString();
                            String total_menu_price = menu_obj.get("total_food_price").getAsString();
                            String guest_count = menu_obj.get("guest_count").getAsString();

                            menu_def_price=Double.parseDouble(total_menu_price);
                            catring_full_total=catring_full_total+Double.parseDouble(total_menu_price);

                            JsonArray cat_course_array = menu_obj.get("courses").getAsJsonArray();
                            for (int course_index = 0; course_index < cat_course_array.size(); course_index++) {
                                String course_title = cat_course_array.get(course_index).getAsJsonObject().get("course_title").getAsString();

                                JsonArray cat_item_array = cat_course_array.get(course_index).getAsJsonObject().get("items").getAsJsonArray();
                                for (int item_index = 0; item_index < cat_item_array.size(); item_index++) {
                                    String item_name = cat_item_array.get(item_index).getAsJsonObject().get("item_name").getAsString();
                                    String is_additional_charges = cat_item_array.get(item_index).getAsJsonObject().get("is_additional_charges").getAsString();
                                    String price_per_item = cat_item_array.get(item_index).getAsJsonObject().get("price_per_item").getAsString();
                                    if(is_additional_charges.equalsIgnoreCase("1")){
                                        menu_def_price = menu_def_price - (Double.parseDouble(price_per_item) * Double.parseDouble(guest_count));
                                    }
                                }
                            }

                            TableRow tr_venue_catring_menu = new TableRow(mContext);

                            TextView tv_cat_menu_title = new TextView(mContext);
                            tv_cat_menu_title.setTypeface(Utils.getFont(mContext, 100));
                            tv_cat_menu_title.setText(menu_title);
                            tv_cat_menu_title.setTextSize(15);
                            tv_cat_menu_title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                            tv_cat_menu_title.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                            tv_cat_menu_title.setPadding(5, 5, 5, 0);
                            tv_cat_menu_title.setGravity(Gravity.LEFT);
                            tr_venue_catring_menu.addView(tv_cat_menu_title);  // Adding textView to tablerow.

                       /* * Creating another textview **/
                            TextView tv_cat_menu_total = new TextView(mContext);
                            tv_cat_menu_total.setTypeface(Utils.getFont(mContext, 100));
                            tv_cat_menu_total.setGravity(Gravity.RIGHT);
                            tv_cat_menu_total.setTextSize(15);
                            tv_cat_menu_total.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                            tv_cat_menu_total.setText(Const.GLOBAL_FORMATTER.format(menu_def_price));
                            tv_cat_menu_total.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                            tv_cat_menu_total.setPadding(5, 5, 5, 0);

                            tr_venue_catring_menu.addView(tv_cat_menu_total); // Adding textView to tablerow.

                            // Add the TableRow to the TableLayout
                            tl_booking_item_details.addView(tr_venue_catring_menu, new TableLayout.LayoutParams(
                                    TableLayout.LayoutParams.MATCH_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));


                            //=========COURSE AND ITEM NAME IN ONE ROW=========================

                            for (int course_index = 0; course_index < cat_course_array.size(); course_index++) {
                                String course_title = cat_course_array.get(course_index).getAsJsonObject().get("course_title").getAsString();

                                TableRow tr_venue_catring_course = new TableRow(mContext);
                                TableRow.LayoutParams row_parms= new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                row_parms.setMargins(25,0,0,0);


                                TextView tv_cat_course_title = new TextView(mContext);
                                tv_cat_course_title.setTypeface(Utils.getFont(mContext, 100));
                                tv_cat_course_title.setText((int)(course_index+1)+") "+course_title);
                                tv_cat_course_title.setTextSize(12);
                                tv_cat_course_title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                                tv_cat_course_title.setTextColor(Color.DKGRAY);
                                tv_cat_course_title.setPadding(5, 5, 5, 0);
                                tv_cat_course_title.setGravity(Gravity.LEFT);
                                tr_venue_catring_course.addView(tv_cat_course_title,row_parms);  // Adding textView to tablerow.

                                // Add the TableRow to the TableLayout
                                tl_booking_item_details.addView(tr_venue_catring_course, new TableLayout.LayoutParams(
                                        TableLayout.LayoutParams.MATCH_PARENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT));


                                //======item name===================
                                JsonArray cat_item_array = cat_course_array.get(course_index).getAsJsonObject().get("items").getAsJsonArray();
                                for (int item_index = 0; item_index < cat_item_array.size(); item_index++) {

                                    String item_name = cat_item_array.get(item_index).getAsJsonObject().get("item_name").getAsString();
                                    String is_additional_charges = cat_item_array.get(item_index).getAsJsonObject().get("is_additional_charges").getAsString();
                                    String price_per_item = cat_item_array.get(item_index).getAsJsonObject().get("price_per_item").getAsString();

                                    //==if 1 means item isadditional charged=====
                                    if(is_additional_charges.equalsIgnoreCase("1")){

                                        double addition_charge = Double.parseDouble(price_per_item) * Double.parseDouble(guest_count);

                                        TableRow tr_venue_catring_item = new TableRow(mContext);
                                        TableRow.LayoutParams row_parms_items = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                                        row_parms_items.setMargins(50,0,0,0);

                                        TextView tv_cat_item_title = new TextView(mContext);
                                        tv_cat_item_title.setTypeface(Utils.getFont(mContext, 100));
                                        tv_cat_item_title.setText(item_name + Html.fromHtml("<sup>*</sup>"));
                                        tv_cat_item_title.setTextSize(12);
                                        tv_cat_item_title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                                        tv_cat_item_title.setTextColor(Color.GRAY);
                                        tv_cat_item_title.setPadding(5, 5, 5, 0);
                                        tv_cat_item_title.setGravity(Gravity.LEFT);
                                        tr_venue_catring_item.addView(tv_cat_item_title,row_parms_items);  // Adding textView to tablerow.
                                        is_extracharge=true;


                                        TextView tv_cat_qty = new TextView(mContext);
                                        tv_cat_qty.setTypeface(Utils.getFont(mContext, 100));
                                        tv_cat_qty.setText("$ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(price_per_item)) +" x "+guest_count);
                                        tv_cat_qty.setTextSize(12);
                                        tv_cat_qty.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.2f));
                                        tv_cat_qty.setTextColor(Color.GRAY);
                                        tv_cat_qty.setPadding(5, 5, 5, 0);
                                        tv_cat_qty.setGravity(Gravity.LEFT);
                                        tr_venue_catring_item.addView(tv_cat_qty);  // Adding textView to tablerow.

                                        /* * Creating another textview **/
                                        TextView tv_cat_item_total = new TextView(mContext);
                                        tv_cat_item_total.setTypeface(Utils.getFont(mContext, 100));
                                        tv_cat_item_total.setGravity(Gravity.RIGHT);
                                        tv_cat_item_total.setTextSize(12);
                                        tv_cat_item_total.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.80f));
                                        tv_cat_item_total.setText(Const.GLOBAL_FORMATTER.format(addition_charge));
                                        tv_cat_item_total.setTextColor(Color.GRAY);
                                        tv_cat_item_total.setPadding(5, 5, 5, 0);

                                        tr_venue_catring_item.addView(tv_cat_item_total); // Adding textView to tablerow.

                                        // Add the TableRow to the TableLayout
                                        tl_booking_item_details.addView(tr_venue_catring_item, new TableLayout.LayoutParams(
                                                TableLayout.LayoutParams.MATCH_PARENT,
                                                TableLayout.LayoutParams.WRAP_CONTENT));

                                    }else {

                                        TableRow tr_venue_catring_item = new TableRow(mContext);
                                        TableRow.LayoutParams row_parms_items = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                        row_parms_items.setMargins(50,0,0,0);


                                        TextView tv_cat_item_title = new TextView(mContext);
                                        tv_cat_item_title.setTypeface(Utils.getFont(mContext, 100));
                                        tv_cat_item_title.setText(item_name);
                                        tv_cat_item_title.setTextSize(12);
                                        tv_cat_item_title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                                        tv_cat_item_title.setTextColor(Color.GRAY);
                                        tv_cat_item_title.setPadding(5, 5, 5, 0);
                                        tv_cat_item_title.setGravity(Gravity.LEFT);
                                        tr_venue_catring_item.addView(tv_cat_item_title,row_parms_items);  // Adding textView to tablerow.


                                        // Add the TableRow to the TableLayout
                                        tl_booking_item_details.addView(tr_venue_catring_item, new TableLayout.LayoutParams(
                                                TableLayout.LayoutParams.MATCH_PARENT,
                                                TableLayout.LayoutParams.WRAP_CONTENT));
                                    }
                                }
                            }

                        }
                    }//=====VENUE CATERING MENU COMPLETED===============

                    /////==== Food MENU VIEW====================================================================================
                    if(mDataset.get(position).getRestaurant_food_price()!=null && mDataset.get(position).getRestaurant_food_price().size()>0 ){


                        View new_view = new View(mContext);
                        new_view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 3));
                        new_view.setBackgroundResource(R.drawable.line_divider_view);
                        tl_booking_item_details.addView(new_view, new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));



                        JsonArray restaurant_array = mDataset.get(position).getRestaurant_food_price();

                        double venue_food_total = 0.0;
                        for(int cat_array_index =0;cat_array_index <restaurant_array.size();cat_array_index++) {
                            //======item name===================
                            JsonArray cat_item_array = restaurant_array.get(cat_array_index).getAsJsonObject().get("items").getAsJsonArray();
                            for (int item_index = 0; item_index < cat_item_array.size(); item_index++) {
                                String item_name = cat_item_array.get(item_index).getAsJsonObject().get("item_name").getAsString();
                                String guest_count = cat_item_array.get(item_index).getAsJsonObject().get("guest_count").getAsString();
                                String price_per_plate = cat_item_array.get(item_index).getAsJsonObject().get("price_per_plate").getAsString();
                                String total_item_price = cat_item_array.get(item_index).getAsJsonObject().get("total_item_price").getAsString();
                                venue_food_total = venue_food_total + Double.parseDouble(total_item_price);
                            }
                        }
                            TableRow tr_head_venu_catering = new TableRow(mContext);
                            TextView tv_name_catering_cost = new TextView(mContext);
                            tv_name_catering_cost.setTypeface(Utils.getFont(mContext, 100));
                            tv_name_catering_cost.setText("Food Order");
                            tv_name_catering_cost.setTextSize(17);
                            tv_name_catering_cost.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                            tv_name_catering_cost.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                            tv_name_catering_cost.setPadding(5, 5, 5, 0);
                            tv_name_catering_cost.setGravity(Gravity.LEFT);
                            tr_head_venu_catering.addView(tv_name_catering_cost);  // Adding textView to tablerow.

                       /* * Creating another textview **/
                            TextView tv_catering_booking_cost = new TextView(mContext);
                            tv_catering_booking_cost.setTypeface(Utils.getFont(mContext, 100));
                            tv_catering_booking_cost.setGravity(Gravity.RIGHT);
                            tv_catering_booking_cost.setTextSize(17);
                            tv_catering_booking_cost.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                            tv_catering_booking_cost.setText(Const.GLOBAL_FORMATTER.format(venue_food_total));
                            tv_catering_booking_cost.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                            tv_catering_booking_cost.setPadding(5, 5, 5, 0);

                            tr_head_venu_catering.addView(tv_catering_booking_cost); // Adding textView to tablerow.

                            // Add the TableRow to the TableLayout
                            tl_booking_item_details.addView(tr_head_venu_catering, new TableLayout.LayoutParams(
                                    TableLayout.LayoutParams.MATCH_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));



                        for(int cat_array_index =0;cat_array_index <restaurant_array.size();cat_array_index++) {

                            String menu_title = restaurant_array.get(cat_array_index).getAsJsonObject().get("menu_desc").getAsString();



                            TableRow tr_venue_catring_course = new TableRow(mContext);
                                TableRow.LayoutParams row_parms= new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                row_parms.setMargins(25,0,0,0);


                                TextView tv_cat_course_title = new TextView(mContext);
                                tv_cat_course_title.setTypeface(Utils.getFont(mContext, 100));
                                tv_cat_course_title.setText((int)(cat_array_index+1)+") "+menu_title);
                                tv_cat_course_title.setTextSize(12);
                                tv_cat_course_title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                                tv_cat_course_title.setTextColor(Color.DKGRAY);
                                tv_cat_course_title.setPadding(5, 5, 5, 0);
                                tv_cat_course_title.setGravity(Gravity.LEFT);
                                tr_venue_catring_course.addView(tv_cat_course_title,row_parms);  // Adding textView to tablerow.



                                // Add the TableRow to the TableLayout
                                tl_booking_item_details.addView(tr_venue_catring_course, new TableLayout.LayoutParams(
                                        TableLayout.LayoutParams.MATCH_PARENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT));


                                //======item name===================
                                JsonArray cat_item_array = restaurant_array.get(cat_array_index).getAsJsonObject().get("items").getAsJsonArray();
                                for (int item_index = 0; item_index < cat_item_array.size(); item_index++) {
                                    String item_name = cat_item_array.get(item_index).getAsJsonObject().get("item_name").getAsString();
                                    String guest_count = cat_item_array.get(item_index).getAsJsonObject().get("guest_count").getAsString();
                                    String price_per_plate = cat_item_array.get(item_index).getAsJsonObject().get("price_per_plate").getAsString();
                                    String total_item_price = cat_item_array.get(item_index).getAsJsonObject().get("total_item_price").getAsString();

                                        TableRow tr_venue_catring_item = new TableRow(mContext);
                                        TableRow.LayoutParams row_parms_items = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                                        row_parms_items.setMargins(50,0,0,0);


                                        TextView tv_cat_item_title = new TextView(mContext);
                                        tv_cat_item_title.setTypeface(Utils.getFont(mContext, 100));
                                        tv_cat_item_title.setText(item_name);
                                        tv_cat_item_title.setTextSize(12);
                                        tv_cat_item_title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                                        tv_cat_item_title.setTextColor(Color.GRAY);
                                        tv_cat_item_title.setPadding(5, 5, 5, 0);
                                        tv_cat_item_title.setGravity(Gravity.LEFT);
                                        tr_venue_catring_item.addView(tv_cat_item_title,row_parms_items);  // Adding textView to tablerow.


                                        TextView tv_cat_qty = new TextView(mContext);
                                        tv_cat_qty.setTypeface(Utils.getFont(mContext, 100));
                                        tv_cat_qty.setText("$ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(price_per_plate)) +" x "+guest_count);
                                        tv_cat_qty.setTextSize(12);
                                        tv_cat_qty.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.2f));
                                        tv_cat_qty.setTextColor(Color.GRAY);
                                        tv_cat_qty.setPadding(5, 5, 5, 0);
                                        tv_cat_qty.setGravity(Gravity.LEFT);
                                        tr_venue_catring_item.addView(tv_cat_qty);  // Adding textView to tablerow.

                                        /* * Creating another textview **/
                                        TextView tv_cat_item_total = new TextView(mContext);
                                        tv_cat_item_total.setTypeface(Utils.getFont(mContext, 100));
                                        tv_cat_item_total.setGravity(Gravity.RIGHT);
                                        tv_cat_item_total.setTextSize(12);
                                        tv_cat_item_total.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.80f));
                                        tv_cat_item_total.setText(Const.GLOBAL_FORMATTER.format(Double.parseDouble(total_item_price)));
                                        tv_cat_item_total.setTextColor(Color.GRAY);
                                        tv_cat_item_total.setPadding(5, 5, 5, 0);

                                        tr_venue_catring_item.addView(tv_cat_item_total); // Adding textView to tablerow.



                                        // Add the TableRow to the TableLayout
                                        tl_booking_item_details.addView(tr_venue_catring_item, new TableLayout.LayoutParams(
                                                TableLayout.LayoutParams.MATCH_PARENT,
                                                TableLayout.LayoutParams.WRAP_CONTENT));

                                }
                            }


                    }//====Venue FOOD MENU COMPLETED===========================

                    //===venue beverages VIEW STARTS======================================
                    if(mDataset.get(position).getBaverage_price()!=null && mDataset.get(position).getBaverage_price().size()>0 ) {

                        View new_view = new View(mContext);
                        new_view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 3));
                        new_view.setBackgroundResource(R.drawable.line_divider_view);
                        tl_booking_item_details.addView(new_view, new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));


                        JsonArray beverages_array = mDataset.get(position).getBaverage_price();
                        double venue_BEVERAGES_total = 0.0;
                        for(int cat_array_index =0;cat_array_index <beverages_array.size();cat_array_index++) {

                            String total_baverage_price = beverages_array.get(cat_array_index).getAsJsonObject().get("total_baverage_price").getAsString();
                            venue_BEVERAGES_total = venue_BEVERAGES_total + Double.parseDouble(total_baverage_price);
                            //======item name===================
                        }
                        TableRow tr_head_venu_catering = new TableRow(mContext);
                        TextView tv_name_catering_cost = new TextView(mContext);
                        tv_name_catering_cost.setTypeface(Utils.getFont(mContext, 100));
                        tv_name_catering_cost.setText("Beverages ");
                        tv_name_catering_cost.setTextSize(17);
                        tv_name_catering_cost.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                        tv_name_catering_cost.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                        tv_name_catering_cost.setPadding(5, 5, 5, 0);
                        tv_name_catering_cost.setGravity(Gravity.LEFT);
                        tr_head_venu_catering.addView(tv_name_catering_cost);  // Adding textView to tablerow.

                       /* * Creating another textview **/
                        TextView tv_catering_booking_cost = new TextView(mContext);
                        tv_catering_booking_cost.setTypeface(Utils.getFont(mContext, 100));
                        tv_catering_booking_cost.setGravity(Gravity.RIGHT);
                        tv_catering_booking_cost.setTextSize(17);
                        tv_catering_booking_cost.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                        tv_catering_booking_cost.setText(Const.GLOBAL_FORMATTER.format(venue_BEVERAGES_total));
                        tv_catering_booking_cost.setTextColor(Color.BLACK);
                        tv_catering_booking_cost.setPadding(5, 5, 5, 0);

                        tr_head_venu_catering.addView(tv_catering_booking_cost); // Adding textView to tablerow.

                        // Add the TableRow to the TableLayout
                        tl_booking_item_details.addView(tr_head_venu_catering, new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));

                        for(int cat_array_index =0;cat_array_index <beverages_array.size();cat_array_index++) {

                            String total_baverage_price = beverages_array.get(cat_array_index).getAsJsonObject().get("total_baverage_price").getAsString();
                            String option_desc  = beverages_array.get(cat_array_index).getAsJsonObject().get("option_desc").getAsString();
                            String guest_count  = beverages_array.get(cat_array_index).getAsJsonObject().get("guest_count").getAsString();
                            String is_hour_extn_changes  = !beverages_array.get(cat_array_index).getAsJsonObject().get("is_hour_extn_changes").isJsonNull()
                                    ?beverages_array.get(cat_array_index).getAsJsonObject().get("is_hour_extn_changes").getAsString():"";
                            String extension_charges  = !beverages_array.get(cat_array_index).getAsJsonObject().get("extension_charges").isJsonNull()
                                    ?beverages_array.get(cat_array_index).getAsJsonObject().get("extension_charges").getAsString():"";

                            TableRow tr_venue_beverages_item = new TableRow(mContext);
                            TableRow.LayoutParams row_parms_items = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                            row_parms_items.setMargins(50,0,0,0);


                            TextView tv_cat_item_title = new TextView(mContext);
                            tv_cat_item_title.setTypeface(Utils.getFont(mContext, 100));
                            tv_cat_item_title.setText(option_desc);
                            tv_cat_item_title.setTextSize(12);
                            tv_cat_item_title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv_cat_item_title.setTextColor(Color.GRAY);
                            tv_cat_item_title.setPadding(5, 5, 5, 0);
                            tv_cat_item_title.setGravity(Gravity.LEFT);
                            tr_venue_beverages_item.addView(tv_cat_item_title,row_parms_items);  // Adding textView to tablerow.


                           /* TextView tv_cat_qty = new TextView(mContext);
                            tv_cat_qty.setTypeface(Utils.getFont(mContext, 100));
                            tv_cat_qty.setText("$ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(is_hour_extn_changes)) +" x "+guest_count);
                            tv_cat_qty.setTextSize(12);
                            tv_cat_qty.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv_cat_qty.setTextColor(Color.GRAY);
                            tv_cat_qty.setPadding(5, 5, 5, 0);
                            tv_cat_qty.setGravity(Gravity.LEFT);
                            tr_venue_beverages_item.addView(tv_cat_qty);  // Adding textView to tablerow.*/

                                        /* * Creating another textview **/
                            TextView tv_cat_item_total = new TextView(mContext);
                            tv_cat_item_total.setTypeface(Utils.getFont(mContext, 100));
                            tv_cat_item_total.setGravity(Gravity.RIGHT);
                            tv_cat_item_total.setTextSize(12);
                            tv_cat_item_total.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.80f));
                            tv_cat_item_total.setText(Const.GLOBAL_FORMATTER.format(Double.parseDouble(total_baverage_price)));
                            tv_cat_item_total.setTextColor(Color.GRAY);
                            tv_cat_item_total.setPadding(5, 5, 5, 0);

                            tr_venue_beverages_item.addView(tv_cat_item_total); // Adding textView to tablerow.
                            // Add the TableRow to the TableLayout
                            tl_booking_item_details.addView(tr_venue_beverages_item, new TableLayout.LayoutParams(
                                    TableLayout.LayoutParams.MATCH_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));

                        }
                    }//==VENUE BEVERAGES COMPLETED

                    //===venues SERVICE VIEW STARTS======================================
                    if(mDataset.get(position).getServices()!=null && mDataset.get(position).getServices().size()>0 ) {


                        View new_view = new View(mContext);
                        new_view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 3));
                        new_view.setBackgroundResource(R.drawable.line_divider_view);
                        tl_booking_item_details.addView(new_view, new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));

                        JsonArray services_array = mDataset.get(position).getServices();

                        for(int cat_array_index =0;cat_array_index <services_array.size();cat_array_index++) {

                            String service_name = services_array.get(cat_array_index).getAsJsonObject().get("service_name").getAsString();
                       /* double venue_services_total = 0.0;
                        for(int cat_array_index =0;cat_array_index <services_array.size();cat_array_index++) {

                            //======item name===================
                            JsonArray cat_item_array = services_array.get(cat_array_index).getAsJsonObject().get("service_options").getAsJsonArray();
                            for (int item_index = 0; item_index < cat_item_array.size(); item_index++) {
                                String item_name = cat_item_array.get(item_index).getAsJsonObject().get("item_name").getAsString();
                                String guest_count = cat_item_array.get(item_index).getAsJsonObject().get("guest_count").getAsString();
                                String price_per_plate = cat_item_array.get(item_index).getAsJsonObject().get("price_per_plate").getAsString();
                                String total_item_price = cat_item_array.get(item_index).getAsJsonObject().get("total_item_price").getAsString();
                                venue_services_total = venue_services_total + Double.parseDouble(total_item_price);
                            }
                        }*/
                            TableRow tr_head_venu_service_title = new TableRow(mContext);
                            TextView tv_name_srevice_title = new TextView(mContext);
                            tv_name_srevice_title.setTypeface(Utils.getFont(mContext, 100));
                            tv_name_srevice_title.setText(service_name);
                            tv_name_srevice_title.setTextSize(17);
                            tv_name_srevice_title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                            tv_name_srevice_title.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                            tv_name_srevice_title.setPadding(5, 5, 5, 0);
                            tv_name_srevice_title.setGravity(Gravity.LEFT);
                            tr_head_venu_service_title.addView(tv_name_srevice_title);  // Adding textView to tablerow.

                       /* TextView tv_catering_booking_cost = new TextView(mContext);
                        tv_catering_booking_cost.setTypeface(Utils.getFont(mContext, 100));
                        tv_catering_booking_cost.setGravity(Gravity.RIGHT);
                        tv_catering_booking_cost.setTextSize(17);
                        tv_catering_booking_cost.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                        tv_catering_booking_cost.setText(Const.GLOBAL_FORMATTER.format(venue_food_total));
                        tv_catering_booking_cost.setTextColor(Color.BLACK);
                        tv_catering_booking_cost.setPadding(5, 5, 5, 0);

                        tr_head_venu_catering.addView(tv_catering_booking_cost); // Adding textView to tablerow.*/

                            // Add the TableRow to the TableLayout
                            tl_booking_item_details.addView(tr_head_venu_service_title, new TableLayout.LayoutParams(
                                    TableLayout.LayoutParams.MATCH_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));

                            //======item name===================
                            JsonArray cat_item_array = services_array.get(cat_array_index).getAsJsonObject().get("service_options").getAsJsonArray();
                            for (int item_index = 0; item_index < cat_item_array.size(); item_index++) {

                                String option_desc = cat_item_array.get(item_index).getAsJsonObject().get("option_desc").getAsString();
                                String option_charges = cat_item_array.get(item_index).getAsJsonObject().get("option_charges").getAsString();
                                String option_details = !cat_item_array.get(item_index).getAsJsonObject().get("option_details").isJsonNull()
                                        ?cat_item_array.get(item_index).getAsJsonObject().get("option_details").getAsString():"";
                                String is_flat_charges = cat_item_array.get(item_index).getAsJsonObject().get("is_flat_charges").getAsString();
                                String is_per_person_charges = cat_item_array.get(item_index).getAsJsonObject().get("is_per_person_charges").getAsString();
                                String is_per_hour_charges = cat_item_array.get(item_index).getAsJsonObject().get("is_per_hour_charges").getAsString();
                                String is_hour_extn_changes = cat_item_array.get(item_index).getAsJsonObject().get("is_hour_extn_changes").getAsString();
                                String extension_charges = cat_item_array.get(item_index).getAsJsonObject().get("extension_charges").getAsString();
                                String is_group_size = cat_item_array.get(item_index).getAsJsonObject().get("is_group_size").getAsString();
                                String group_size_from = cat_item_array.get(item_index).getAsJsonObject().get("group_size_from").getAsString();
                                String group_size_to = cat_item_array.get(item_index).getAsJsonObject().get("group_size_to").getAsString();
                                String total_service_charges = cat_item_array.get(item_index).getAsJsonObject().get("total_service_charges").getAsString();

                                TableRow tr_venue_catring_item = new TableRow(mContext);
                                TableRow.LayoutParams row_parms_items = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                                row_parms_items.setMargins(50,0,0,0);

                                TextView tv_cat_item_title = new TextView(mContext);
                                tv_cat_item_title.setTypeface(Utils.getFont(mContext, 100));
                                tv_cat_item_title.setText(option_desc);
                                tv_cat_item_title.setTextSize(12);
                                tv_cat_item_title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                                tv_cat_item_title.setTextColor(Color.GRAY);
                                tv_cat_item_title.setPadding(5, 5, 5, 0);
                                tv_cat_item_title.setGravity(Gravity.LEFT);
                                tr_venue_catring_item.addView(tv_cat_item_title,row_parms_items);  // Adding textView to tablerow.

                               /* String set_text="";
                                if(is_flat_charges.equalsIgnoreCase("1")){
                                    set_text= Const.GLOBAL_FORMATTER.format(Double.parseDouble(option_charges)) + " x " + "1";
                                }else if(is_per_person_charges.equalsIgnoreCase("1")){
                                    set_text=  Const.GLOBAL_FORMATTER.format(Double.parseDouble(option_charges)) + " x " + "1";
                                }else if(is_per_hour_charges.equalsIgnoreCase("1")){
                                    set_text=  Const.GLOBAL_FORMATTER.format(Double.parseDouble(option_charges)) + " x " + "1";
                                }

                                if(is_flat_charges.equalsIgnoreCase("1")) {
                                    TextView tv_cat_qty = new TextView(mContext);
                                    tv_cat_qty.setTypeface(Utils.getFont(mContext, 100));
                                    tv_cat_qty.setText("$" + set_text);
                                    tv_cat_qty.setTextSize(12);
                                    tv_cat_qty.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.2f));
                                    tv_cat_qty.setTextColor(Color.GRAY);
                                    tv_cat_qty.setPadding(5, 5, 5, 0);
                                    tv_cat_qty.setGravity(Gravity.LEFT);
                                    tr_venue_catring_item.addView(tv_cat_qty);  // Adding textView to tablerow.
                                }*/



                                        /* * Creating another textview **/
                                TextView tv_cat_item_total = new TextView(mContext);
                                tv_cat_item_total.setTypeface(Utils.getFont(mContext, 100));
                                tv_cat_item_total.setGravity(Gravity.RIGHT);
                                tv_cat_item_total.setTextSize(12);
                                tv_cat_item_total.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.80f));
                                tv_cat_item_total.setText(Const.GLOBAL_FORMATTER.format(Double.parseDouble(total_service_charges)));
                                tv_cat_item_total.setTextColor(Color.BLACK);
                                tv_cat_item_total.setPadding(5, 5, 5, 0);

                                tr_venue_catring_item.addView(tv_cat_item_total); // Adding textView to tablerow.

                                // Add the TableRow to the TableLayout
                                tl_booking_item_details.addView(tr_venue_catring_item, new TableLayout.LayoutParams(
                                        TableLayout.LayoutParams.MATCH_PARENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT));

                            }
                        }
                    }//===VENUES SERVICE ARRAY COMPLETED========================================================================

                    //=====VENUES EXTRA SERVICE STARTS===============================================
                    if(mDataset.get(position).getExtra_services()!=null && mDataset.get(position).getExtra_services().size()>0 ) {

                        JsonArray services_array = mDataset.get(position).getExtra_services();
                        for(int cat_array_index =0;cat_array_index <services_array.size();cat_array_index++) {

                            String service_name = services_array.get(cat_array_index).getAsJsonObject().get("extra_service_name").getAsString();

                            TableRow tr_head_venu_service_title = new TableRow(mContext);
                            TextView tv_name_srevice_title = new TextView(mContext);
                            tv_name_srevice_title.setTypeface(Utils.getFont(mContext, 100));
                            tv_name_srevice_title.setText(service_name);
                            tv_name_srevice_title.setTextSize(17);
                            tv_name_srevice_title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                            tv_name_srevice_title.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                            tv_name_srevice_title.setPadding(5, 5, 5, 0);
                            tv_name_srevice_title.setGravity(Gravity.LEFT);
                            tr_head_venu_service_title.addView(tv_name_srevice_title);  // Adding textView to tablerow.

                            // Add the TableRow to the TableLayout
                            tl_booking_item_details.addView(tr_head_venu_service_title, new TableLayout.LayoutParams(
                                    TableLayout.LayoutParams.MATCH_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));
                            //======item name===================
                            JsonArray cat_item_array = services_array.get(cat_array_index).getAsJsonObject().get("service_options").getAsJsonArray();
                            for (int item_index = 0; item_index < cat_item_array.size(); item_index++) {

                                String option_desc = cat_item_array.get(item_index).getAsJsonObject().get("option_desc").getAsString();
                                String option_charges = cat_item_array.get(item_index).getAsJsonObject().get("option_charges").getAsString();
                                String option_details = !cat_item_array.get(item_index).getAsJsonObject().get("option_details").isJsonNull()
                                        ?cat_item_array.get(item_index).getAsJsonObject().get("option_details").getAsString():"";
                                String is_flat_charges = cat_item_array.get(item_index).getAsJsonObject().get("is_flat_charges").getAsString();
                                String is_per_person_charges = cat_item_array.get(item_index).getAsJsonObject().get("is_per_person_charges").getAsString();
                                String is_per_hour_charges = cat_item_array.get(item_index).getAsJsonObject().get("is_per_hour_charges").getAsString();
                                String is_hour_extn_changes = cat_item_array.get(item_index).getAsJsonObject().get("is_hour_extn_changes").getAsString();
                                String extension_charges = cat_item_array.get(item_index).getAsJsonObject().get("extension_charges").getAsString();
                                String is_group_size = cat_item_array.get(item_index).getAsJsonObject().get("is_group_size").getAsString();
                                String group_size_from = cat_item_array.get(item_index).getAsJsonObject().get("group_size_from").getAsString();
                                String group_size_to = cat_item_array.get(item_index).getAsJsonObject().get("group_size_to").getAsString();
                                String total_service_charges = cat_item_array.get(item_index).getAsJsonObject().get("total_extra_service_charges").getAsString();

                                TableRow tr_venue_catring_item = new TableRow(mContext);
                                TableRow.LayoutParams row_parms_items = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                                row_parms_items.setMargins(50,0,0,0);


                                TextView tv_cat_item_title = new TextView(mContext);
                                tv_cat_item_title.setTypeface(Utils.getFont(mContext, 100));
                                tv_cat_item_title.setText(option_desc);
                                tv_cat_item_title.setTextSize(12);
                                tv_cat_item_title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                                tv_cat_item_title.setTextColor(Color.GRAY);
                                tv_cat_item_title.setPadding(5, 5, 5, 0);
                                tv_cat_item_title.setGravity(Gravity.LEFT);
                                tr_venue_catring_item.addView(tv_cat_item_title,row_parms_items);  // Adding textView to tablerow.

                                TextView tv_cat_item_total = new TextView(mContext);
                                tv_cat_item_total.setTypeface(Utils.getFont(mContext, 100));
                                tv_cat_item_total.setGravity(Gravity.RIGHT);
                                tv_cat_item_total.setTextSize(12);
                                tv_cat_item_total.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.80f));
                                tv_cat_item_total.setText(Const.GLOBAL_FORMATTER.format(Double.parseDouble(total_service_charges)));
                                tv_cat_item_total.setTextColor(Color.BLACK);
                                tv_cat_item_total.setPadding(5, 5, 5, 0);

                                tr_venue_catring_item.addView(tv_cat_item_total); // Adding textView to tablerow.

                                // Add the TableRow to the TableLayout
                                tl_booking_item_details.addView(tr_venue_catring_item, new TableLayout.LayoutParams(
                                        TableLayout.LayoutParams.MATCH_PARENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT));

                            }
                        }
                    }//==VENEUE EXTRA SERVICE COMPLETED================================================


                }else if(mDataset.get(position).getBooking_type().equalsIgnoreCase("2")) {

                    //cv_delivery_details.setVisibility(View.VISIBLE);
                    ll_delivery_details.setVisibility(View.VISIBLE);
                    if(mDataset.get(position).getIs_pickup().equalsIgnoreCase("1")){
                        tv_address.setText("Pickup");
                    }else if(mDataset.get(position).getIs_onsite_service().equalsIgnoreCase("1")){
                        tv_address.setText("Onsite");
                    }else if(mDataset.get(position).getIs_delivery().equalsIgnoreCase("1")){
                        tv_address.setText(mDataset.get(position).getDelivery_address());
                    }
                    tv_deliverytime.setText(convertDateStringToString(mDataset.get(position).getBooking_time(),"yyyy-MM-dd HH:mm:ss","MM-dd-yyyy hh:mm aa"));


                    View new_view_def = new View(mContext);
                    new_view_def.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 3));
                    new_view_def.setBackgroundResource(R.drawable.line_divider_view);
                    tl_booking_item_details.addView(new_view_def, new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    TableRow tr_head_servie = new TableRow(mContext);
                    TextView tv_name_service_cost = new TextView(mContext);
                   tv_name_service_cost.setTypeface(Utils.getFont(mContext, 100));
                   tv_name_service_cost.setText(mDataset.get(position).getService_title());
                   tv_name_service_cost.setTextSize(15);
                   tv_name_service_cost.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                   tv_name_service_cost.setTextColor(Color.BLACK);
                   tv_name_service_cost.setPadding(5, 5, 5, 0);
                   tv_name_service_cost.setGravity(Gravity.LEFT);
                    tr_head_servie.addView(tv_name_service_cost);  // Adding textView to tablerow.

                    if(Integer.parseInt(mDataset.get(position).getQuantity())>1){
                        TextView tv_name_service_qty_amount = new TextView(mContext);
                        tv_name_service_qty_amount.setTypeface(Utils.getFont(mContext, 100));
                        tv_name_service_qty_amount.setText(mDataset.get(position).getQuantity() + " * " + Const.GLOBAL_FORMATTER.format(Double.parseDouble(mDataset.get(position).getTotal_amount())));
                        tv_name_service_qty_amount.setTextSize(12);
                        tv_name_service_qty_amount.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                        tv_name_service_qty_amount.setTextColor(Color.BLACK);
                        tv_name_service_qty_amount.setPadding(5, 5, 5, 0);
                        tv_name_service_qty_amount.setGravity(Gravity.LEFT);
                        tr_head_servie.addView(tv_name_service_qty_amount);  // Adding textView to tablerow.
                    }

                    /** Creating another textview **/
                    TextView tv_price_service_cost = new TextView(mContext);
                    tv_price_service_cost.setTypeface(Utils.getFont(mContext, 100));
                    tv_price_service_cost.setGravity(Gravity.RIGHT);
                    tv_price_service_cost.setTextSize(15);
                    tv_price_service_cost.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                    tv_price_service_cost.setText(Const.GLOBAL_FORMATTER.format(Double.parseDouble(mDataset.get(position).getTotal_amount())));
                    tv_price_service_cost.setTextColor(Color.BLACK);
                    tv_price_service_cost.setPadding(5, 5, 5, 0);

                    tr_head_servie.addView(tv_price_service_cost); // Adding textView to tablerow.

                    // Add the TableRow to the TableLayout
                    tl_booking_item_details.addView(tr_head_servie, new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
                }
                //======CATRING srvice providersview starts here
                else if(mDataset.get(position).getBooking_type().equalsIgnoreCase("3")) {

                    //cv_delivery_details.setVisibility(View.VISIBLE);
                    ll_delivery_details.setVisibility(View.VISIBLE);

                    if(mDataset.get(position).getIs_pickup().equalsIgnoreCase("1")){
                        tv_address.setText("Pickup");
                    }else if(mDataset.get(position).getIs_onsite_service().equalsIgnoreCase("1")){
                        tv_address.setText("Onsite");
                    }else if(mDataset.get(position).getIs_delivery().equalsIgnoreCase("1")){
                        tv_address.setText(mDataset.get(position).getDelivery_address());
                    }
                    tv_deliverytime.setText(convertDateStringToString(mDataset.get(position).getBooking_time(), "yyyy-MM-dd HH:mm:ss", "MM-dd-yyyy hh:mm aa"));

                    View new_view_def = new View(mContext);
                    new_view_def.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
                    new_view_def.setBackgroundResource(R.drawable.line_divider_view);
                    tl_booking_item_details.addView(new_view_def, new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    /////==== CATERING MENU VIEW====================================================================================
                    if(mDataset.get(position).getCatering_price()!=null && mDataset.get(position).getCatering_price().size()>0 ){

                        View new_view = new View(mContext);
                        new_view.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 3));
                        new_view.setBackgroundResource(R.drawable.line_divider_view);
                        tl_booking_item_details.addView(new_view, new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));

                        JsonArray catering_array = mDataset.get(position).getCatering_price();

                        double catring_full_total=0,menu_def_price=0;

                        for(int cat_array_index =0;cat_array_index <catering_array.size();cat_array_index++) {

                            JsonObject menu_obj = catering_array.get(cat_array_index).getAsJsonObject();
                            String menu_title = menu_obj.get("menu_desc").getAsString();
                            String total_menu_price = menu_obj.get("total_food_price").getAsString();
                            String guest_count = menu_obj.get("guest_count").getAsString();
                            catring_full_total=catring_full_total+Double.parseDouble(total_menu_price);
                        }

                        TableRow tr_head_venu_catering = new TableRow(mContext);
                        TextView tv_name_catering_cost = new TextView(mContext);
                        tv_name_catering_cost.setTypeface(Utils.getFont(mContext, 100));
                        tv_name_catering_cost.setText("Catering Cost");
                        tv_name_catering_cost.setTextSize(17);
                        tv_name_catering_cost.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                        tv_name_catering_cost.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                        tv_name_catering_cost.setPadding(5, 5, 5, 0);
                        tv_name_catering_cost.setGravity(Gravity.LEFT);
                        tr_head_venu_catering.addView(tv_name_catering_cost);  // Adding textView to tablerow.

                       /* * Creating another textview **/
                        TextView tv_catering_booking_cost = new TextView(mContext);
                        tv_catering_booking_cost.setTypeface(Utils.getFont(mContext, 100));
                        tv_catering_booking_cost.setGravity(Gravity.RIGHT);
                        tv_catering_booking_cost.setTextSize(17);
                        tv_catering_booking_cost.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                        tv_catering_booking_cost.setText(Const.GLOBAL_FORMATTER.format(catring_full_total));
                        tv_catering_booking_cost.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                        tv_catering_booking_cost.setPadding(5, 5, 5, 0);

                        tr_head_venu_catering.addView(tv_catering_booking_cost); // Adding textView to tablerow.

                        // Add the TableRow to the TableLayout
                        tl_booking_item_details.addView(tr_head_venu_catering, new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));


                        //=======CATERING MENU TITLE

                        for(int cat_array_index =0;cat_array_index <catering_array.size();cat_array_index++) {

                            JsonObject menu_obj = catering_array.get(cat_array_index).getAsJsonObject();
                            String menu_title = menu_obj.get("menu_desc").getAsString();
                            String total_menu_price = menu_obj.get("total_food_price").getAsString();
                            String guest_count = menu_obj.get("guest_count").getAsString();

                            menu_def_price=Double.parseDouble(total_menu_price);
                            catring_full_total=catring_full_total+Double.parseDouble(total_menu_price);

                            JsonArray cat_course_array = menu_obj.get("courses").getAsJsonArray();
                            for (int course_index = 0; course_index < cat_course_array.size(); course_index++) {
                                String course_title = cat_course_array.get(course_index).getAsJsonObject().get("course_title").getAsString();

                                JsonArray cat_item_array = cat_course_array.get(course_index).getAsJsonObject().get("items").getAsJsonArray();
                                for (int item_index = 0; item_index < cat_item_array.size(); item_index++) {
                                    String item_name = cat_item_array.get(item_index).getAsJsonObject().get("item_name").getAsString();
                                    String is_additional_charges = cat_item_array.get(item_index).getAsJsonObject().get("is_additional_charges").getAsString();
                                    String price_per_item = cat_item_array.get(item_index).getAsJsonObject().get("price_per_item").getAsString();
                                    if(is_additional_charges.equalsIgnoreCase("1")){
                                        menu_def_price = menu_def_price - (Double.parseDouble(price_per_item) * Double.parseDouble(guest_count));
                                    }
                                }
                            }

                            TableRow tr_venue_catring_menu = new TableRow(mContext);

                            TextView tv_cat_menu_title = new TextView(mContext);
                            tv_cat_menu_title.setTypeface(Utils.getFont(mContext, 100));
                            tv_cat_menu_title.setText(menu_title);
                            tv_cat_menu_title.setTextSize(15);
                            tv_cat_menu_title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                            tv_cat_menu_title.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                            tv_cat_menu_title.setPadding(5, 5, 5, 0);
                            tv_cat_menu_title.setGravity(Gravity.LEFT);
                            tr_venue_catring_menu.addView(tv_cat_menu_title);  // Adding textView to tablerow.

                       /* * Creating another textview **/
                            TextView tv_cat_menu_total = new TextView(mContext);
                            tv_cat_menu_total.setTypeface(Utils.getFont(mContext, 100));
                            tv_cat_menu_total.setGravity(Gravity.RIGHT);
                            tv_cat_menu_total.setTextSize(15);
                            tv_cat_menu_total.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                            tv_cat_menu_total.setText(Const.GLOBAL_FORMATTER.format(menu_def_price));
                            tv_cat_menu_total.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                            tv_cat_menu_total.setPadding(5, 5, 5, 0);

                            tr_venue_catring_menu.addView(tv_cat_menu_total); // Adding textView to tablerow.

                            // Add the TableRow to the TableLayout
                            tl_booking_item_details.addView(tr_venue_catring_menu, new TableLayout.LayoutParams(
                                    TableLayout.LayoutParams.MATCH_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));


                            //=========COURSE AND ITEM NAME IN ONE ROW=========================

                            for (int course_index = 0; course_index < cat_course_array.size(); course_index++) {
                                String course_title = cat_course_array.get(course_index).getAsJsonObject().get("course_title").getAsString();

                                TableRow tr_venue_catring_course = new TableRow(mContext);
                                TableRow.LayoutParams row_parms= new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                row_parms.setMargins(25,0,0,0);


                                TextView tv_cat_course_title = new TextView(mContext);
                                tv_cat_course_title.setTypeface(Utils.getFont(mContext, 100));
                                tv_cat_course_title.setText((int)(course_index+1)+") "+course_title);
                                tv_cat_course_title.setTextSize(12);
                                tv_cat_course_title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                                tv_cat_course_title.setTextColor(Color.DKGRAY);
                                tv_cat_course_title.setPadding(5, 5, 5, 0);
                                tv_cat_course_title.setGravity(Gravity.LEFT);
                                tr_venue_catring_course.addView(tv_cat_course_title,row_parms);  // Adding textView to tablerow.

                                // Add the TableRow to the TableLayout
                                tl_booking_item_details.addView(tr_venue_catring_course, new TableLayout.LayoutParams(
                                        TableLayout.LayoutParams.MATCH_PARENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT));


                                //======item name===================
                                JsonArray cat_item_array = cat_course_array.get(course_index).getAsJsonObject().get("items").getAsJsonArray();
                                for (int item_index = 0; item_index < cat_item_array.size(); item_index++) {
                                    String item_name = cat_item_array.get(item_index).getAsJsonObject().get("item_name").getAsString();
                                    String is_additional_charges = cat_item_array.get(item_index).getAsJsonObject().get("is_additional_charges").getAsString();
                                    String price_per_item = cat_item_array.get(item_index).getAsJsonObject().get("price_per_item").getAsString();

                                    //==if 1 means item i sadditional charged=====
                                    if(is_additional_charges.equalsIgnoreCase("1")){

                                        double addition_charge = Double.parseDouble(price_per_item) * Double.parseDouble(guest_count);

                                        TableRow tr_venue_catring_item = new TableRow(mContext);
                                        TableRow.LayoutParams row_parms_items = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                                        row_parms_items.setMargins(50,0,0,0);

                                        TextView tv_cat_item_title = new TextView(mContext);
                                        tv_cat_item_title.setTypeface(Utils.getFont(mContext, 100));
                                        tv_cat_item_title.setText(item_name + Html.fromHtml("<sup>*</sup>"));
                                        tv_cat_item_title.setTextSize(12);
                                        tv_cat_item_title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                                        tv_cat_item_title.setTextColor(Color.GRAY);
                                        tv_cat_item_title.setPadding(5, 5, 5, 0);
                                        tv_cat_item_title.setGravity(Gravity.LEFT);
                                        tr_venue_catring_item.addView(tv_cat_item_title,row_parms_items);  // Adding textView to tablerow.
                                        is_extracharge=true;

                                        TextView tv_cat_qty = new TextView(mContext);
                                        tv_cat_qty.setTypeface(Utils.getFont(mContext, 100));
                                        tv_cat_qty.setText("$ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(price_per_item)) +" x "+guest_count);
                                        tv_cat_qty.setTextSize(12);
                                        tv_cat_qty.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.2f));
                                        tv_cat_qty.setTextColor(Color.GRAY);
                                        tv_cat_qty.setPadding(5, 5, 5, 0);
                                        tv_cat_qty.setGravity(Gravity.LEFT);
                                        tr_venue_catring_item.addView(tv_cat_qty);  // Adding textView to tablerow.

                                        /* * Creating another textview **/
                                        TextView tv_cat_item_total = new TextView(mContext);
                                        tv_cat_item_total.setTypeface(Utils.getFont(mContext, 100));
                                        tv_cat_item_total.setGravity(Gravity.RIGHT);
                                        tv_cat_item_total.setTextSize(12);
                                        tv_cat_item_total.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.80f));
                                        tv_cat_item_total.setText(Const.GLOBAL_FORMATTER.format(addition_charge));
                                        tv_cat_item_total.setTextColor(Color.GRAY);
                                        tv_cat_item_total.setPadding(5, 5, 5, 0);

                                        tr_venue_catring_item.addView(tv_cat_item_total); // Adding textView to tablerow.

                                        // Add the TableRow to the TableLayout
                                        tl_booking_item_details.addView(tr_venue_catring_item, new TableLayout.LayoutParams(
                                                TableLayout.LayoutParams.MATCH_PARENT,
                                                TableLayout.LayoutParams.WRAP_CONTENT));

                                    }else {

                                        TableRow tr_venue_catring_item = new TableRow(mContext);
                                        TableRow.LayoutParams row_parms_items = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                        row_parms_items.setMargins(50,0,0,0);


                                        TextView tv_cat_item_title = new TextView(mContext);
                                        tv_cat_item_title.setTypeface(Utils.getFont(mContext, 100));
                                        tv_cat_item_title.setText(item_name);
                                        tv_cat_item_title.setTextSize(12);
                                        tv_cat_item_title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                                        tv_cat_item_title.setTextColor(Color.GRAY);
                                        tv_cat_item_title.setPadding(5, 5, 5, 0);
                                        tv_cat_item_title.setGravity(Gravity.LEFT);
                                        tr_venue_catring_item.addView(tv_cat_item_title,row_parms_items);  // Adding textView to tablerow.


                                        // Add the TableRow to the TableLayout
                                        tl_booking_item_details.addView(tr_venue_catring_item, new TableLayout.LayoutParams(
                                                TableLayout.LayoutParams.MATCH_PARENT,
                                                TableLayout.LayoutParams.WRAP_CONTENT));
                                    }
                                }
                            }

                        }
                    }//=====SP CATERING MENU COMPLETED===============

                    /////==== Food MENU VIEW====================================================================================
                    if(mDataset.get(position).getRestaurant_food_price()!=null && mDataset.get(position).getRestaurant_food_price().size()>0 ){


                        View new_view = new View(mContext);
                        new_view.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 3));
                        new_view.setBackgroundResource(R.drawable.line_divider_view);
                        tl_booking_item_details.addView(new_view, new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));



                        JsonArray restaurant_array = mDataset.get(position).getRestaurant_food_price();

                        double venue_food_total = 0.0;
                        for(int cat_array_index =0;cat_array_index <restaurant_array.size();cat_array_index++) {


                            //======item name===================
                            JsonArray cat_item_array = restaurant_array.get(cat_array_index).getAsJsonObject().get("items").getAsJsonArray();
                            for (int item_index = 0; item_index < cat_item_array.size(); item_index++) {
                                String item_name = cat_item_array.get(item_index).getAsJsonObject().get("item_name").getAsString();
                                String guest_count = cat_item_array.get(item_index).getAsJsonObject().get("guest_count").getAsString();
                                String price_per_plate = cat_item_array.get(item_index).getAsJsonObject().get("price_per_plate").getAsString();
                                String total_item_price = cat_item_array.get(item_index).getAsJsonObject().get("total_item_price").getAsString();
                                venue_food_total = venue_food_total + Double.parseDouble(total_item_price);
                            }
                        }
                        TableRow tr_head_venu_catering = new TableRow(mContext);
                        TextView tv_name_catering_cost = new TextView(mContext);
                        tv_name_catering_cost.setTypeface(Utils.getFont(mContext, 100));
                        tv_name_catering_cost.setText("Food Order");
                        tv_name_catering_cost.setTextSize(17);
                        tv_name_catering_cost.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                        tv_name_catering_cost.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                        tv_name_catering_cost.setPadding(5, 5, 5, 0);
                        tv_name_catering_cost.setGravity(Gravity.LEFT);
                        tr_head_venu_catering.addView(tv_name_catering_cost);  // Adding textView to tablerow.

                       /* * Creating another textview **/
                        TextView tv_catering_booking_cost = new TextView(mContext);
                        tv_catering_booking_cost.setTypeface(Utils.getFont(mContext, 100));
                        tv_catering_booking_cost.setGravity(Gravity.RIGHT);
                        tv_catering_booking_cost.setTextSize(17);
                        tv_catering_booking_cost.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                        tv_catering_booking_cost.setText(Const.GLOBAL_FORMATTER.format(venue_food_total));
                        tv_catering_booking_cost.setTextColor(Color.BLACK);
                        tv_catering_booking_cost.setPadding(5, 5, 5, 0);

                        tr_head_venu_catering.addView(tv_catering_booking_cost); // Adding textView to tablerow.

                        // Add the TableRow to the TableLayout
                        tl_booking_item_details.addView(tr_head_venu_catering, new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));



                        for(int cat_array_index =0;cat_array_index <restaurant_array.size();cat_array_index++) {

                            String menu_title = restaurant_array.get(cat_array_index).getAsJsonObject().get("menu_desc").getAsString();



                            TableRow tr_venue_catring_course = new TableRow(mContext);
                            TableRow.LayoutParams row_parms= new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                            row_parms.setMargins(25,0,0,0);


                            TextView tv_cat_course_title = new TextView(mContext);
                            tv_cat_course_title.setTypeface(Utils.getFont(mContext, 100));
                            tv_cat_course_title.setText((int)(cat_array_index+1)+") "+menu_title);
                            tv_cat_course_title.setTextSize(12);
                            tv_cat_course_title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv_cat_course_title.setTextColor(Color.DKGRAY);
                            tv_cat_course_title.setPadding(5, 5, 5, 0);
                            tv_cat_course_title.setGravity(Gravity.LEFT);
                            tr_venue_catring_course.addView(tv_cat_course_title,row_parms);  // Adding textView to tablerow.



                            // Add the TableRow to the TableLayout
                            tl_booking_item_details.addView(tr_venue_catring_course, new TableLayout.LayoutParams(
                                    TableLayout.LayoutParams.MATCH_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));


                            //======item name===================
                            JsonArray cat_item_array = restaurant_array.get(cat_array_index).getAsJsonObject().get("items").getAsJsonArray();
                            for (int item_index = 0; item_index < cat_item_array.size(); item_index++) {
                                String item_name = cat_item_array.get(item_index).getAsJsonObject().get("item_name").getAsString();
                                String guest_count = cat_item_array.get(item_index).getAsJsonObject().get("guest_count").getAsString();
                                String price_per_plate = cat_item_array.get(item_index).getAsJsonObject().get("price_per_plate").getAsString();
                                String total_item_price = cat_item_array.get(item_index).getAsJsonObject().get("total_item_price").getAsString();

                                TableRow tr_venue_catring_item = new TableRow(mContext);
                                TableRow.LayoutParams row_parms_items = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                                row_parms_items.setMargins(50,0,0,0);


                                TextView tv_cat_item_title = new TextView(mContext);
                                tv_cat_item_title.setTypeface(Utils.getFont(mContext, 100));
                                tv_cat_item_title.setText(item_name);
                                tv_cat_item_title.setTextSize(12);
                                tv_cat_item_title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                                tv_cat_item_title.setTextColor(Color.GRAY);
                                tv_cat_item_title.setPadding(5, 5, 5, 0);
                                tv_cat_item_title.setGravity(Gravity.LEFT);
                                tr_venue_catring_item.addView(tv_cat_item_title,row_parms_items);  // Adding textView to tablerow.


                                TextView tv_cat_qty = new TextView(mContext);
                                tv_cat_qty.setTypeface(Utils.getFont(mContext, 100));
                                tv_cat_qty.setText("$ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(price_per_plate)) +" x "+guest_count);
                                tv_cat_qty.setTextSize(12);
                                tv_cat_qty.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.2f));
                                tv_cat_qty.setTextColor(Color.GRAY);
                                tv_cat_qty.setPadding(5, 5, 5, 0);
                                tv_cat_qty.setGravity(Gravity.LEFT);
                                tr_venue_catring_item.addView(tv_cat_qty);  // Adding textView to tablerow.

                                        /* * Creating another textview **/
                                TextView tv_cat_item_total = new TextView(mContext);
                                tv_cat_item_total.setTypeface(Utils.getFont(mContext, 100));
                                tv_cat_item_total.setGravity(Gravity.RIGHT);
                                tv_cat_item_total.setTextSize(12);
                                tv_cat_item_total.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.80f));
                                tv_cat_item_total.setText(Const.GLOBAL_FORMATTER.format(Double.parseDouble(total_item_price)));
                                tv_cat_item_total.setTextColor(Color.GRAY);
                                tv_cat_item_total.setPadding(5, 5, 5, 0);

                                tr_venue_catring_item.addView(tv_cat_item_total); // Adding textView to tablerow.



                                // Add the TableRow to the TableLayout
                                tl_booking_item_details.addView(tr_venue_catring_item, new TableLayout.LayoutParams(
                                        TableLayout.LayoutParams.MATCH_PARENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT));

                            }
                        }


                    }//====Venue FOOD MENU COMPLETED===========================

                    //===venue beverages VIEW STARTS======================================
                    if(mDataset.get(position).getBaverage_price()!=null && mDataset.get(position).getBaverage_price().size()>0 ) {


                        View new_view = new View(mContext);
                        new_view.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 3));
                        new_view.setBackgroundResource(R.drawable.line_divider_view);
                        tl_booking_item_details.addView(new_view, new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));


                        JsonArray beverages_array = mDataset.get(position).getBaverage_price();
                        double venue_BEVERAGES_total = 0.0;
                        for(int cat_array_index =0;cat_array_index <beverages_array.size();cat_array_index++) {

                            String total_baverage_price = beverages_array.get(cat_array_index).getAsJsonObject().get("total_baverage_price").getAsString();
                            venue_BEVERAGES_total = venue_BEVERAGES_total + Double.parseDouble(total_baverage_price);
                            //======item name===================
                        }
                        TableRow tr_head_venu_catering = new TableRow(mContext);
                        TextView tv_name_catering_cost = new TextView(mContext);
                        tv_name_catering_cost.setTypeface(Utils.getFont(mContext, 100));
                        tv_name_catering_cost.setText("Beverages ");
                        tv_name_catering_cost.setTextSize(17);
                        tv_name_catering_cost.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                        tv_name_catering_cost.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                        tv_name_catering_cost.setPadding(5, 5, 5, 0);
                        tv_name_catering_cost.setGravity(Gravity.LEFT);
                        tr_head_venu_catering.addView(tv_name_catering_cost);  // Adding textView to tablerow.

                       /* * Creating another textview **/
                        TextView tv_catering_booking_cost = new TextView(mContext);
                        tv_catering_booking_cost.setTypeface(Utils.getFont(mContext, 100));
                        tv_catering_booking_cost.setGravity(Gravity.RIGHT);
                        tv_catering_booking_cost.setTextSize(17);
                        tv_catering_booking_cost.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f));
                        tv_catering_booking_cost.setText(Const.GLOBAL_FORMATTER.format(venue_BEVERAGES_total));
                        tv_catering_booking_cost.setTextColor(Color.BLACK);
                        tv_catering_booking_cost.setPadding(5, 5, 5, 0);

                        tr_head_venu_catering.addView(tv_catering_booking_cost); // Adding textView to tablerow.

                        // Add the TableRow to the TableLayout
                        tl_booking_item_details.addView(tr_head_venu_catering, new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));

                        for(int cat_array_index =0;cat_array_index <beverages_array.size();cat_array_index++) {

                            String total_baverage_price = beverages_array.get(cat_array_index).getAsJsonObject().get("total_baverage_price").getAsString();
                            String option_desc  = beverages_array.get(cat_array_index).getAsJsonObject().get("option_desc").getAsString();
                            String guest_count  = beverages_array.get(cat_array_index).getAsJsonObject().get("guest_count").getAsString();
                            String is_hour_extn_changes  = !beverages_array.get(cat_array_index).getAsJsonObject().get("is_hour_extn_changes").isJsonNull()
                                    ?beverages_array.get(cat_array_index).getAsJsonObject().get("is_hour_extn_changes").getAsString():"";
                            String extension_charges  = !beverages_array.get(cat_array_index).getAsJsonObject().get("extension_charges").isJsonNull()
                                    ?beverages_array.get(cat_array_index).getAsJsonObject().get("extension_charges").getAsString():"";

                            TableRow tr_venue_beverages_item = new TableRow(mContext);
                            TableRow.LayoutParams row_parms_items = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                            row_parms_items.setMargins(50,0,0,0);


                            TextView tv_cat_item_title = new TextView(mContext);
                            tv_cat_item_title.setTypeface(Utils.getFont(mContext, 100));
                            tv_cat_item_title.setText(option_desc);
                            tv_cat_item_title.setTextSize(12);
                            tv_cat_item_title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv_cat_item_title.setTextColor(Color.GRAY);
                            tv_cat_item_title.setPadding(5, 5, 5, 0);
                            tv_cat_item_title.setGravity(Gravity.LEFT);
                            tr_venue_beverages_item.addView(tv_cat_item_title,row_parms_items);  // Adding textView to tablerow.


                           /* TextView tv_cat_qty = new TextView(mContext);
                            tv_cat_qty.setTypeface(Utils.getFont(mContext, 100));
                            tv_cat_qty.setText("$ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(is_hour_extn_changes)) +" x "+guest_count);
                            tv_cat_qty.setTextSize(12);
                            tv_cat_qty.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                            tv_cat_qty.setTextColor(Color.GRAY);
                            tv_cat_qty.setPadding(5, 5, 5, 0);
                            tv_cat_qty.setGravity(Gravity.LEFT);
                            tr_venue_beverages_item.addView(tv_cat_qty);  // Adding textView to tablerow.*/

                                        /* * Creating another textview **/
                            TextView tv_cat_item_total = new TextView(mContext);
                            tv_cat_item_total.setTypeface(Utils.getFont(mContext, 100));
                            tv_cat_item_total.setGravity(Gravity.RIGHT);
                            tv_cat_item_total.setTextSize(12);
                            tv_cat_item_total.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.80f));
                            tv_cat_item_total.setText(Const.GLOBAL_FORMATTER.format(Double.parseDouble(total_baverage_price)));
                            tv_cat_item_total.setTextColor(Color.GRAY);
                            tv_cat_item_total.setPadding(5, 5, 5, 0);

                            tr_venue_beverages_item.addView(tv_cat_item_total); // Adding textView to tablerow.
                            // Add the TableRow to the TableLayout
                            tl_booking_item_details.addView(tr_venue_beverages_item, new TableLayout.LayoutParams(
                                    TableLayout.LayoutParams.MATCH_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));

                        }
                    }//==VENUE BEVERAGES COMPLETED




                }

                    double discount = 0.0;
                if(mDataset.get(position).getDiscount_amount()!=null && !mDataset.get(position).getDiscount_amount().equalsIgnoreCase("0")) {

                    View new_view_def = new View(mContext);
                    new_view_def.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 3));
                    new_view_def.setBackgroundResource(R.drawable.line_divider_view);
                    tl_booking_item_details.addView(new_view_def, new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    /** Creating another textview **/
                    TableRow tr_head_subtotal = new TableRow(mContext);

                    TextView tv_subtotatl_static = new TextView(mContext);
                    tv_subtotatl_static.setTypeface(Utils.getFont(mContext, 100));
                    tv_subtotatl_static.setGravity(Gravity.RIGHT);
                    tv_subtotatl_static.setTextSize(15);
                    tv_subtotatl_static.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                    tv_subtotatl_static.setText("Sub Total ($) ");
                    tv_subtotatl_static.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                    tv_subtotatl_static.setPadding(5, 5, 5, 0);
                    tr_head_subtotal.addView(tv_subtotatl_static); // Adding textView to tablerow.

                    TextView tv_subtotal = new TextView(mContext);
                    tv_subtotal.setTypeface(Utils.getFont(mContext, 100));
                    tv_subtotal.setGravity(Gravity.RIGHT);
                    tv_subtotal.setTextSize(15);
                    tv_subtotal.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                    tv_subtotal.setText(Const.GLOBAL_FORMATTER.format(Double.parseDouble(mDataset.get(position).getTotal_amount())));
                    tv_subtotal.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                    tv_subtotal.setPadding(5, 5, 5, 0);

                    tr_head_subtotal.addView(tv_subtotal); // Adding textView to tablerow.

                    // Add the TableRow to the TableLayout
                    tl_booking_item_details.addView(tr_head_subtotal, new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
                    /** Creating another textview **/
                    TableRow tr_head_discount = new TableRow(mContext);
                    discount = Double.parseDouble(mDataset.get(position).getDiscount_amount());

                    //======discount view==============================================
                    TextView tv_Discount_static = new TextView(mContext);
                    tv_Discount_static.setTypeface(Utils.getFont(mContext, 100));
                    tv_Discount_static.setGravity(Gravity.RIGHT);
                    tv_Discount_static.setTextSize(15);
                    tv_Discount_static.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                    tv_Discount_static.setText("Discount ($) ");
                    tv_Discount_static.setTextColor(Color.BLACK);
                    tv_Discount_static.setPadding(5, 5, 5, 0);
                    tr_head_discount.addView(tv_Discount_static); // Adding textView to tablerow.

                    TextView tv_Discount = new TextView(mContext);
                    tv_Discount.setTypeface(Utils.getFont(mContext, 100));
                    tv_Discount.setGravity(Gravity.RIGHT);
                    tv_Discount.setTextSize(15);
                    tv_Discount.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                    tv_Discount.setText(" - "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(mDataset.get(position).getDiscount_amount())));
                    tv_Discount.setTextColor(Color.RED);
                    tv_Discount.setPadding(5, 5, 5, 0);

                    tr_head_discount.addView(tv_Discount); // Adding textView to tablerow.

                    // Add the TableRow to the TableLayout
                    tl_booking_item_details.addView(tr_head_discount, new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));


                    View new_view_def2 = new View(mContext);
                    new_view_def2.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 3));
                    new_view_def2.setBackgroundResource(R.drawable.line_divider_view);
                    tl_booking_item_details.addView(new_view_def2, new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
                }


                TableRow tr_aditoianl_note = new TableRow(mContext);
                TextView tv_aditionalNOTE = new TextView(mContext);
                tv_aditionalNOTE.setTypeface(Utils.getFont(mContext, 100));
                tv_aditionalNOTE.setGravity(Gravity.LEFT);
                tv_aditionalNOTE.setTextSize(10);
                tv_aditionalNOTE.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                tv_aditionalNOTE.setText(" * = Additional Charges. ");
                tv_aditionalNOTE.setTextColor(Color.LTGRAY);
                tv_aditionalNOTE.setPadding(5, 5, 5, 0);

                if(is_extracharge==true) {
                    tr_aditoianl_note.addView(tv_aditionalNOTE); // Adding textView to tablerow.
                    // Add the TableRow to the TableLayout
                    tl_booking_item_details.addView(tr_aditoianl_note, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                }

                tv_total_booking_amount.setText("Total Payable($) : "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(mDataset.get(position).getTotal_amount()) - discount));

                dialog.show();

            }
        });




    }

    public void addItem(BookingVenueModel dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    String res;
    class CancelBooking extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //Log.d("pre execute", "Pre execute Done");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");




           // Log.d("post execute", "Executando doInBackground   ingredients");
            try {

                JSONObject req = new JSONObject();
                req.put("booking_id",str_booking_id);
                req.put("user_id",sharepref.getString(Const.PREF_USER_ID,""));

                //Log.d("REq Json======", req.toString());

                if(sp_booking==1){
                    String response = post(Const.SERVER_URL_API +"booking_sp_cancel", req.toString(),"post");//post("http://54.153.127.215/api/login", req.toString());
                    //Log.d("REsponce Json====",response);
                    res = response;
                }else if(venue_booking==1){
                    String response = post(Const.SERVER_URL_API +"booking_cancel", req.toString(),"post");//post("http://54.153.127.215/api/login", req.toString());
                    //Log.d("REsponce Json====",response);
                    res = response;
                }

            }catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return res;

        }



        @Override
        protected void onPostExecute(String result)
        { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            super.onPostExecute(result);
        }
            //System.out.println("OnpostExecute----done-------");
            progressDialog.dismiss();
            //Log.i("RES=delet booking==", res);
            if (res == null || res.equals("")) {

                progressDialog.dismiss();
                Toast.makeText(mContext, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject obj = new JSONObject(res);


                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.

                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi



                if(response_string.equals("success")){

                   /* mDataset.remove(data_index);
                    notifyDataSetChanged();*/
                    mContext.startActivity(new Intent(mContext, BookingListActivity.class));
                    ((Activity)mContext).finish();
                }

                else{
                    String message=obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(((Activity)mContext).findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
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


        @Override
        protected void onCancelled() {
        }
    }
}