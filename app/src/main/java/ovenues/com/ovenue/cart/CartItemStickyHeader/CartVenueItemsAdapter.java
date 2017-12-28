package ovenues.com.ovenue.cart.CartItemStickyHeader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.brandongogetap.stickyheaders.exposed.StickyHeader;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler;
import com.google.gson.JsonArray;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.ServiceProviderCateringActivity.ServiceProviderFoodAddItem;
import ovenues.com.ovenue.VenueCateringActivites.VenueCateringFoodAddItem;
import ovenues.com.ovenue.cart.CartVenuesNCateringDetails;
import ovenues.com.ovenue.modelpojo.Cart.CartItemsModel;
import ovenues.com.ovenue.utils.Const;
import ovenues.com.ovenue.utils.Utils;

import static android.content.Context.MODE_PRIVATE;
import static ovenues.com.ovenue.cart.CartVenuesNCateringDetails.cart_details_cart_id;
import static ovenues.com.ovenue.cart.CartVenuesNCateringDetails.cart_details_detailView;
import static ovenues.com.ovenue.utils.APICall.post;
import static ovenues.com.ovenue.utils.Utils.convertDateStringToString;

public class CartVenueItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements StickyHeaderHandler {

    private static final int HEADER_TYPE = 0;
    private static final int BODY_TYPE = 1;
    private final ArrayList<CartItemsModel> data;
    Context mContext;
    double total_venue =0;
    SharedPreferences sharepref;
    String delete_str_cart_id,venuTypeId,restaurant_menu_item_id,restaurant_menu_id,
            catering_menu_id,catering_menu_item_id,str_remove_menu="0",str_remove_menu_item="0",str_remove_whole_cateringOrder="0",
            beverage_id ,service_id,service_option_id,extra_service_option_id;


    public CartVenueItemsAdapter(Context context, ArrayList<CartItemsModel> data) {
        this.data = data;
        this.mContext=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        sharepref = mContext.getSharedPreferences("MyPref", MODE_PRIVATE);
        View view;
        mContext = parent.getContext();

        switch (viewType) {
            case HEADER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_only_textview_cartheader, parent, false);
                return new HeaderViewViewHolder(view);
            case BODY_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart_venue_details, parent, false);
                return new BodyViewViewHolder(view);
        }
        return null;
    }

    @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        holder.setIsRecyclable(false);
        if (data != null) {
            switch (data.get(position).getmUiType()) {
                case HEADER_TYPE:
                    if(data.get(position).getmVenueOrdCatType().equalsIgnoreCase("1")){
                        ((HeaderViewViewHolder) holder).row_text_view_only.setText(data.get(position).getVenue_name()/*+"\n"+data.get(position).getService_name()*/);
                        ((HeaderViewViewHolder) holder).row_text_view_only_2.setVisibility(View.GONE);
                    }else if(data.get(position).getmVenueOrdCatType().equalsIgnoreCase("3")){
                        ((HeaderViewViewHolder) holder).row_text_view_only.setText(data.get(position).getName());
                        ((HeaderViewViewHolder) holder).row_text_view_only.setTextColor(mContext.getResources().getColor(R.color.white));
                        ((HeaderViewViewHolder) holder).row_text_view_only_2.setVisibility(View.GONE);
                    }else{
                        ((HeaderViewViewHolder) holder).row_text_view_only.setText(data.get(position).getName()/*+"\n"+data.get(position).getService_name()*/);
                        ((HeaderViewViewHolder) holder).row_text_view_only.setTextColor(mContext.getResources().getColor(R.color.white));

                        ((HeaderViewViewHolder) holder).row_text_view_only_2.setText("$ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(data.get(position).getDelivery_address())));
                        ((HeaderViewViewHolder) holder).row_text_view_only_2.setTextColor(Color.WHITE);
                        ((HeaderViewViewHolder) holder).row_text_view_only_2.setGravity(Gravity.RIGHT);
                    }

                    // ((HeaderViewViewHolder) holder).row_text_view_only.setText(data.get(position).getName());

                    break;
                case BODY_TYPE:

                    if(data.get(position).getmVenueOrdCatType().equalsIgnoreCase("1")){//==venues booking======
                        Log.e("venue found",data.get(position).getmVenueOrdCatType());



                        String guest_count  = data.get(position).getNumber_of_guests();
                        ((BodyViewViewHolder) holder).ll_itemcount.setVisibility(View.VISIBLE);
                        ((BodyViewViewHolder) holder).ll_numpiker_itemcount.setVisibility(View.GONE);
                        ((BodyViewViewHolder) holder).tv_title.setText("Booking details");
                        ((BodyViewViewHolder) holder).tv_address.setVisibility(View.GONE);
                        ((BodyViewViewHolder) holder).tv_guest.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_guest_grey, 0, 0, 0);
                        ((BodyViewViewHolder) holder).tv_guest.setText(guest_count);
                        ((BodyViewViewHolder) holder).tv_guest.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_guest_grey, 0, 0, 0);
                        ((BodyViewViewHolder) holder).tv_date_time.setGravity(Gravity.LEFT);
                        ((BodyViewViewHolder) holder).tv_date_time.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calander_grey, 0, 0, 0);
                        ((BodyViewViewHolder) holder).tv_date_time.setText(convertDateStringToString(data.get(position).getBooking_date(),"yyyy-MM-dd","MM-dd-yyyy")
                                +"\n"
                                +convertDateStringToString(data.get(position).getBooking_time_from(),"yyyy-MM-dd HH:mm:ss","hh:mm aa")
                                +" - "+convertDateStringToString(data.get(position).getBooking_time_to(),"yyyy-MM-dd HH:mm:ss","hh:mm aa"));
                        if(data.get(position).getItem_price()!=null || !data.get(position).getItem_price().equalsIgnoreCase("")){
                            ((BodyViewViewHolder) holder).tv_price_total.setText("Rent\n$ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(data.get(position).getBooking_rent())));
                        }else{
                            ((BodyViewViewHolder) holder).tv_price_total.setText("");
                        }
                        ((BodyViewViewHolder) holder).btn_remove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertdialog("Are you sure to delete whole booking ?",position);
                               venuTypeId = data.get(position).getmVenueOrdCatType();
                                delete_str_cart_id=data.get(position).getCart_id();
                            }
                        });
                    }else if(data.get(position).getmVenueOrdCatType().equalsIgnoreCase("2")) {//=====================================================FOOD MENU===============
                        Log.e("resturant menu found",data.get(position).getmVenueOrdCatType());
                        ((BodyViewViewHolder) holder).tv_title.setText(data.get(position).getName()
                                .substring(data.get(position).getName().indexOf(data.get(position).getName().valueOf(">"))+2
                                        ,data.get(position).getName().length()));
                        ((BodyViewViewHolder) holder).ll_itemcount.setVisibility(View.GONE);
                        ((BodyViewViewHolder) holder).ll_numpiker_itemcount.setVisibility(View.VISIBLE);

                        //((BodyViewViewHolder) holder).np_itemcount.setStepSize(0);
                        ((BodyViewViewHolder) holder).np_itemcount.setMinValue(1);
                        ((BodyViewViewHolder) holder).np_itemcount.setValue(Integer.parseInt(data.get(position).getCount()));
                        ((BodyViewViewHolder) holder).np_itemcount.setListener(new ScrollableNumberPickerListener() {
                            @Override
                            public void onNumberPicked(int value) {

                                String CurrentString = data.get(position).getName();
                                String[] separated = CurrentString.split("\\>");
                                String part1=separated[0].trim(); // this will contain "Fruit"
                                String part2=separated[1].trim(); // this will contain " they taste good"
                                //((BodyViewViewHolder) holder).tv_title.setText(part2);

                                String str_menu_id = data.get(position).getId2();
                                String str_menu_desc = separated[0].trim();
                                String str_item_id =data.get(position).getId3();
                                String str_item_name = separated[1].trim();
                                int int_Guest_count = ((BodyViewViewHolder) holder).np_itemcount.getValue();
                                String str_price_per_plate = data.get(position).getItem_price();

                                double total = (double) ((BodyViewViewHolder) holder).np_itemcount.getValue() * Double.parseDouble(str_price_per_plate);
                                String str_total_item_price = Const.GLOBAL_FORMATTER.format(total);
                                new AddCartGenricRestaurantfood().execute(data.get(position).getCart_id(),str_menu_id,str_menu_desc,str_item_id,str_item_name,int_Guest_count,str_price_per_plate,str_total_item_price);

                                ((BodyViewViewHolder) holder).tv_price_total.setText("$"+str_total_item_price);
                            }
                        });

                        ((BodyViewViewHolder) holder).tv_price_copy.setText("$"+Const.GLOBAL_FORMATTER.format(Double.parseDouble(data.get(position).getItem_price())));


                        ((BodyViewViewHolder) holder).tv_price_total.setText("$"+Const.GLOBAL_FORMATTER.format(Double.parseDouble(data.get(position).getTotal_itemprice())));
                        ((BodyViewViewHolder) holder).btn_remove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertdialog("Are you sure to remove ?",position);
                                venuTypeId = data.get(position).getmVenueOrdCatType();
                                delete_str_cart_id=data.get(position).getCart_id();
                                restaurant_menu_id=data.get(position).getId2();
                                restaurant_menu_item_id = data.get(position).getId3();
                            }
                        });



                    }else if(data.get(position).getmVenueOrdCatType().equalsIgnoreCase("3")) {  ///==================================================CATRING MENU=====
                        Log.e("catering menu found", data.get(position).getmVenueOrdCatType());
                        //Log.e("catering array found", data.get(position).toString());
                        ((BodyViewViewHolder) holder).tv_title.setText(data.get(position).getName());
                        if (data.get(position).getName().equalsIgnoreCase("Order Details")) {

                            String guest_count = data.get(position).getNumber_of_guests();
                            String type = "", address = "";

                            if (data.get(position).getIs_delivery().equalsIgnoreCase("1")) {
                                type = "Delivery";
                            } else if (data.get(position).getIs_pickup().equalsIgnoreCase("1")) {
                                type = "Pickup";
                            }else{
                                type="On Site";
                            }

                            ((BodyViewViewHolder) holder).ll_numpiker_itemcount.setVisibility(View.GONE);
                            ((BodyViewViewHolder) holder).tv_address.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_map_grey_small, 0, 0, 0);
                            ((BodyViewViewHolder) holder).tv_address.setText(Html.fromHtml(data.get(position).getDelivery_address()+"<font color='#F8A058'>  ( "+type+" )</font>."),
                                    TextView.BufferType.SPANNABLE);
                            ((BodyViewViewHolder) holder).tv_guest.setText(guest_count);
                            ((BodyViewViewHolder) holder).tv_guest.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_guest_grey, 0, 0, 0);
                            ((BodyViewViewHolder) holder).tv_date_time.setGravity(Gravity.LEFT);
                            ((BodyViewViewHolder) holder).tv_date_time.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calander_grey, 0, 0, 0);
                            ((BodyViewViewHolder) holder).tv_date_time.setText(convertDateStringToString(data.get(position).getBooking_date(), "yyyy-MM-dd", "MM-dd-yyyy")
                                    +"  "+ convertDateStringToString(data.get(position).getBooking_time_from(), "yyyy-MM-dd HH:mm:ss", "hh:mm aa")
                                    /*+ "\nGuest : " + guest_count*/);
                            ((BodyViewViewHolder) holder).tv_price_total.setText("Total\n$ " + Const.GLOBAL_FORMATTER.format(Double.parseDouble(data.get(position).getTotal_itemprice())));
                            //((BodyViewViewHolder) holder).tv_price_total.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,1));
                            //((BodyViewViewHolder) holder).tv_price_total.setTextSize(15);
                            ((BodyViewViewHolder) holder).btn_remove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertdialog("Are you sure to delete whole Catering Order ?",position);
                                    venuTypeId = data.get(position).getmVenueOrdCatType();
                                    delete_str_cart_id=data.get(position).getCart_id();
                                    str_remove_whole_cateringOrder="1";
                                }
                            });
                        } else {

                            ((BodyViewViewHolder) holder).ll_main_row_item.setVisibility(View.GONE);
                            ((BodyViewViewHolder) holder).ll_itemcount.setVisibility(View.GONE);
                            ((BodyViewViewHolder) holder).ll_numpiker_itemcount.setVisibility(View.GONE);
                            ((BodyViewViewHolder) holder).btn_remove.setVisibility(View.GONE);
                            ((BodyViewViewHolder) holder).tv_price_total.setVisibility(View.GONE);

                            JsonArray got_caterring_json = data.get(position).getBooking_food_menu();
                            double Dguest_count = 0.0, price_perplate = 0.0, final_menu_total_per_plat = 0.0, Dtotal_food_price = 0.0;


                            for (int j = 0; j < got_caterring_json.size(); j++) {

                                String mVenueOrdCatType = "3";
                                final String id1 = !got_caterring_json.get(j).getAsJsonObject().get("menu_id").isJsonNull()
                                        ? got_caterring_json.get(j).getAsJsonObject().get("menu_id").getAsString() : null;
                                final String menu_desc = !got_caterring_json.get(j).getAsJsonObject().get("menu_desc").isJsonNull()
                                        ? got_caterring_json.get(j).getAsJsonObject().get("menu_desc").getAsString() : null;
                                String price_per_plate = !got_caterring_json.get(j).getAsJsonObject().get("price_per_plate").isJsonNull()
                                        ? got_caterring_json.get(j).getAsJsonObject().get("price_per_plate").getAsString() : null;
                                final String guest_count = !got_caterring_json.get(j).getAsJsonObject().get("guest_count").isJsonNull()
                                        ? got_caterring_json.get(j).getAsJsonObject().get("guest_count").getAsString() : null;
                                String total_food_price = !got_caterring_json.get(j).getAsJsonObject().get("total_food_price").isJsonNull()
                                        ? got_caterring_json.get(j).getAsJsonObject().get("total_food_price").getAsString() : null;

                                Dguest_count = Double.parseDouble(guest_count);
                                price_perplate = Double.parseDouble(price_per_plate);
                                Dtotal_food_price = Double.parseDouble(total_food_price);

                                /** Creating a TextView to add to the row **/
                                TableRow tr_head_def = new TableRow(mContext);
                                TextView tv_name = new TextView(mContext);
                                tv_name.setTypeface(Utils.getFont(mContext, 100));
                                tv_name.setText(menu_desc);
                                tv_name.setTextSize(15);
                                tv_name.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                                tv_name.setTextColor(mContext.getResources().getColor(R.color.md_grey_600,mContext.getTheme()));
                                tv_name.setPadding(5, 5, 5, 5);
                                tv_name.setGravity(Gravity.LEFT);
                                tr_head_def.addView(tv_name);  // Adding textView to tablerow.
                                // Add the TableRow to the TableLayout
                                ((BodyViewViewHolder) holder).tl_catering_item_details.addView(tr_head_def, new TableLayout.LayoutParams(
                                        TableLayout.LayoutParams.MATCH_PARENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT));

                                JsonArray array_courses_menu = !got_caterring_json.get(j).getAsJsonObject().getAsJsonArray("courses").isJsonNull()
                                        ? got_caterring_json.get(j).getAsJsonObject().getAsJsonArray("courses") : null;

                                if (!array_courses_menu.isJsonNull() && array_courses_menu.size() > 0) {
                                    double total_additional = 0.0;
                                    int alpha=65;
                                    for (int k = 0; k < array_courses_menu.size(); k++) {
                                       alpha=alpha+k;

                                        final String id2 = array_courses_menu.get(k).getAsJsonObject().get("course_id").getAsString();
                                        final String course_title = array_courses_menu.get(k).getAsJsonObject().get("course_title").getAsString();

                                        /** Creating another textview **/
                                        TableRow tr_head_def2 = new TableRow(mContext);
                                        TextView tv_price = new TextView(mContext);
                                        tv_price.setTypeface(Utils.getFont(mContext, 100));
                                        tv_price.setGravity(Gravity.LEFT);
                                        tv_price.setTextSize(15);
                                        tv_price.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                                        tv_price.setText("\t"+(char)alpha+" ) " + course_title );
                                        tv_price.setTextColor(mContext.getResources().getColor(R.color.colorAccent,mContext.getTheme()));
                                        tv_price.setPadding(5, 5, 5, 5);

                                        tr_head_def2.addView(tv_price); // Adding textView to tablerow.

                                        // Add the TableRow to the TableLayout
                                        ((BodyViewViewHolder) holder).tl_catering_item_details.addView(tr_head_def2, new TableLayout.LayoutParams(
                                                TableLayout.LayoutParams.MATCH_PARENT,
                                                TableLayout.LayoutParams.WRAP_CONTENT));



                                        JsonArray array_item_menu = !array_courses_menu.get(k).getAsJsonObject().getAsJsonArray("items").isJsonNull()
                                                ? array_courses_menu.get(k).getAsJsonObject().getAsJsonArray("items") : null;

                                        if (!array_item_menu.isJsonNull() && array_item_menu.size() > 0) {
                                            String item_names_and_additionl_price = "";
                                            for (int l = 0; l < array_item_menu.size(); l++) {
                                                final String id3 = array_item_menu.get(l).getAsJsonObject().get("item_id").getAsString();
                                                final String item_name = array_item_menu.get(l).getAsJsonObject().get("item_name").getAsString();
                                                String is_additional_charges = array_item_menu.get(l).getAsJsonObject().get("is_additional_charges").getAsString();
                                                String price_per_item = array_item_menu.get(l).getAsJsonObject().get("price_per_item").getAsString();
                                                if (is_additional_charges.equalsIgnoreCase("1")) {
                                                    item_names_and_additionl_price = (int) (l + 1) + " ) " + item_name + "\t*( Ext $ " + price_per_item + " /plate)";
                                                    total_additional = total_additional + Double.parseDouble(price_per_item);
                                                } else {
                                                    item_names_and_additionl_price = (int) (l + 1) + " ) " + item_name;
                                                }

                                                /** Creating another textview **/
                                                TableRow tr_head_def3 = new TableRow(mContext);
                                                TextView tv_itemname = new TextView(mContext);
                                                tv_itemname.setTypeface(Utils.getFont(mContext, 100));
                                                tv_itemname.setGravity(Gravity.LEFT);
                                                tv_itemname.setTextSize(12);
                                                tv_itemname.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,4));
                                                tv_itemname.setText(item_names_and_additionl_price);
                                                tv_itemname.setTextColor(mContext.getResources().getColor(R.color.md_grey_600,mContext.getTheme()));
                                                tv_itemname.setPadding(50, 10, 5, 5);

                                                tr_head_def3.addView(tv_itemname); // Adding textView to tablerow.

                                                // Add the TableRow to the TableLayout

                                                ImageView tv_remove_icon = new ImageView(mContext);
                                                tv_remove_icon.setForegroundGravity(Gravity.RIGHT);
                                                //tv_remove_icon.setTextSize(15);
                                                tv_remove_icon.setImageResource(R.drawable.ic_close_gray_24dp);
                                                tv_remove_icon.setLayoutParams(new TableRow.LayoutParams(70, 70,1));
                                                //tv_remove_icon.setText("Remove");
                                                tv_remove_icon.setPadding(5, 5, 5, 5);

                                                tr_head_def3.addView(tv_remove_icon); // Adding textView to tablerow.

                                                // Add the TableRow to the TableLayout
                                                ((BodyViewViewHolder) holder).tl_catering_item_details.addView(tr_head_def3, new TableLayout.LayoutParams(
                                                        TableLayout.LayoutParams.WRAP_CONTENT,
                                                        TableLayout.LayoutParams.WRAP_CONTENT));


                                                //for catring menu - catering_menu_id ,cart_id
                                                //  for catring menu item- catering_menu_id , catering_menu_item_id ,cart_id

                                                tv_remove_icon.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        alertdialog("Are you sure to remove "+item_name+" ?",position);
                                                        venuTypeId = data.get(position).getmVenueOrdCatType();
                                                        delete_str_cart_id=data.get(position).getCart_id();
                                                        str_remove_menu_item="1";
                                                        catering_menu_id=id1;
                                                        catering_menu_item_id = id3;
                                                    }
                                                });

                                                tr_head_def3.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (data.get(position).getFlag().equalsIgnoreCase("3")) {
                                                            mContext.startActivity(new Intent(mContext, ServiceProviderFoodAddItem.class)
                                                                    .putExtra("cart_ID", data.get(position).getCart_id())
                                                                    .putExtra("menu_id", id1)
                                                                    .putExtra("course_id", id2)
                                                                    .putExtra("title", course_title)
                                                                    .putExtra("guest_count", guest_count)
                                                                    .putExtra("sreviceproviderID", data.get(position).getVenue_id()));
                                                        } else if (data.get(position).getFlag().equalsIgnoreCase("1")) {
                                                            Toast.makeText(mContext, "vneue found", Toast.LENGTH_LONG).show();
                                                            mContext.startActivity(new Intent(mContext, VenueCateringFoodAddItem.class)
                                                                    .putExtra("cart_ID", data.get(position).getCart_id())
                                                                    .putExtra("menu_id", id1)
                                                                    .putExtra("course_id", id2)
                                                                    .putExtra("title", course_title)
                                                                    .putExtra("guest_count", guest_count)
                                                                    .putExtra("venue_id", data.get(position).getVenue_id()));
                                                        }
                                                    }
                                                });
                                            }
                                            tr_head_def2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (data.get(position).getFlag().equalsIgnoreCase("3")) {
                                                        mContext.startActivity(new Intent(mContext, ServiceProviderFoodAddItem.class)
                                                                .putExtra("cart_ID", data.get(position).getCart_id())
                                                                .putExtra("menu_id", id1)
                                                                .putExtra("course_id", id2)
                                                                .putExtra("title", course_title)
                                                                .putExtra("guest_count", guest_count)
                                                                .putExtra("sreviceproviderID", data.get(position).getVenue_id()));
                                                    } else if (data.get(position).getFlag().equalsIgnoreCase("1")) {
                                                        Toast.makeText(mContext, "vneue found", Toast.LENGTH_LONG).show();
                                                        mContext.startActivity(new Intent(mContext, VenueCateringFoodAddItem.class)
                                                                .putExtra("cart_ID", data.get(position).getCart_id())
                                                                .putExtra("menu_id", id1)
                                                                .putExtra("course_id", id2)
                                                                .putExtra("title", course_title)
                                                                .putExtra("guest_count", guest_count)
                                                                .putExtra("venue_id", data.get(position).getVenue_id()));
                                                    }
                                                }
                                            });

                                            Log.d("catering menu", "done");

                                        }

                                        TableRow tr_head_deletemenu = new TableRow(mContext);
                                        TextView tv_remove_menu = new TextView(mContext);
                                        tv_remove_menu.setTypeface(Utils.getFont(mContext, 100));
                                        tv_remove_menu.setGravity(Gravity.CENTER);
                                        tv_remove_menu.setTextSize(15);
                                        //tv_remove_menu.setBackgroundResource(R.drawable.small_rounded_corner_borde);

                                        tv_remove_menu.setLayoutParams(new TableRow.LayoutParams(10, TableRow.LayoutParams.MATCH_PARENT));
                                        tv_remove_menu.setText("Remove");
                                        tv_remove_menu.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                                        tv_remove_menu.setPadding(5, 10, 5, 5);

                                        tr_head_deletemenu.addView(tv_remove_menu); // Adding textView to tablerow.

                                        // Add the TableRow to the TableLayout
                                        ((BodyViewViewHolder) holder).tl_catering_item_details.addView(tr_head_deletemenu, new TableLayout.LayoutParams(
                                                10,
                                                TableLayout.LayoutParams.WRAP_CONTENT));


                                        tv_remove_menu.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                alertdialog("Are you sure to remove "+menu_desc+" ?",position);
                                                venuTypeId = data.get(position).getmVenueOrdCatType();
                                                delete_str_cart_id=data.get(position).getCart_id();
                                                str_remove_menu="1";
                                                catering_menu_id=id1;
                                            }
                                        });

                                    }

                                    final_menu_total_per_plat = total_additional + Double.parseDouble(price_per_plate);
                                    TableRow tr_head_def3 = new TableRow(mContext);
                                    TextView tv_calculation_qty = new TextView(mContext);
                                    tv_calculation_qty.setTypeface(Utils.getFont(mContext, 100));
                                    tv_calculation_qty.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_guest_grey, 0, 0, 0);
                                    tv_calculation_qty.setText(guest_count );
                                    tv_calculation_qty.setTextSize(15);
                                    tv_calculation_qty.setCompoundDrawablePadding(20);
                                    tv_calculation_qty.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 0.0f));
                                    tv_calculation_qty.setTextColor(mContext.getResources().getColor(R.color.md_grey_600,mContext.getTheme()));
                                    tv_calculation_qty.setPadding(5, 5, 5, 0);
                                    tv_calculation_qty.setGravity(Gravity.LEFT|Gravity.CENTER);
                                    tr_head_def3.addView(tv_calculation_qty);

                                    TextView tv_calculation_x = new TextView(mContext);
                                    tv_calculation_x.setTypeface(Utils.getFont(mContext, 100));
                                    tv_calculation_x.setText(" x ");
                                    tv_calculation_x.setTextSize(15);
                                    tv_calculation_x.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 0.0f));
                                    tv_calculation_x.setTextColor(mContext.getResources().getColor(R.color.md_grey_600,mContext.getTheme()));
                                    tv_calculation_x.setPadding(5, 5, 5, 0);
                                    tv_calculation_x.setGravity(Gravity.LEFT|Gravity.CENTER);
                                    tr_head_def3.addView(tv_calculation_x);

                                    String extra_perplate_cost = "", sub_total_price_per_plate = "";
                                    if (price_perplate < final_menu_total_per_plat) {
                                        extra_perplate_cost = Double.toString((double) final_menu_total_per_plat - (double) price_perplate);
                                        sub_total_price_per_plate = "\n("
                                                + Const.GLOBAL_FORMATTER.format(price_perplate) + "+"
                                                + Const.GLOBAL_FORMATTER.format(Double.parseDouble(extra_perplate_cost))+")";
                                    }

                                    TextView tv_calculation_price = new TextView(mContext);
                                    tv_calculation_price.setTypeface(Utils.getFont(mContext, 100));
                                    tv_calculation_price.setText("$"+Const.GLOBAL_FORMATTER.format(final_menu_total_per_plat) + sub_total_price_per_plate);
                                    tv_calculation_price.setTextSize(15);
                                    tv_calculation_price.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 0.0f));
                                    tv_calculation_price.setTextColor(mContext.getResources().getColor(R.color.md_grey_600,mContext.getTheme()));
                                    tv_calculation_price.setPadding(5, 5, 5, 0);
                                    tv_calculation_price.setGravity(Gravity.LEFT|Gravity.CENTER);
                                    tr_head_def3.addView(tv_calculation_price);

                                    TextView tv_calculation = new TextView(mContext);
                                    tv_calculation.setTypeface(Utils.getFont(mContext, 100));
                                    tv_calculation.setText(" $ " + Const.GLOBAL_FORMATTER.format(Dtotal_food_price)+" ");
                                    //tv_calculation.setBackgroundResource(R.drawable.small_rounded_corner_borde);
                                    tv_calculation.setTextSize(15);
                                    tv_calculation.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                                    tv_calculation.setTextColor(mContext.getResources().getColor(R.color.md_grey_600,mContext.getTheme()));
                                    tv_calculation.setPadding(5, 5, 5, 5);
                                    tv_calculation.setGravity(Gravity.RIGHT|Gravity.CENTER);
                                    tr_head_def3.addView(tv_calculation);
                                    ((BodyViewViewHolder) holder).tl_catering_item_details.addView(tr_head_def3, new TableLayout.LayoutParams(
                                            TableLayout.LayoutParams.MATCH_PARENT,
                                            TableLayout.LayoutParams.WRAP_CONTENT));


                                    View new_view = new View(mContext);
                                    new_view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 3));
                                    new_view.setBackgroundResource(R.drawable.line_divider_view);

                                    TableLayout.LayoutParams lp=  new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                            TableLayout.LayoutParams.WRAP_CONTENT);
                                    lp.setMargins(0,5,0,5);

                                    ((BodyViewViewHolder) holder).tl_catering_item_details.addView(new_view,lp );

                                }
                            }
                        }
                       /* ((BodyViewViewHolder) holder).btn_remove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertdialog("Are you sure to remove menus?",position);
                                venuTypeId = data.get(position).getmVenueOrdCatType();
                                delete_str_cart_id=data.get(position).getCart_id();
                                beverage_id=data.get(position).getId3();
                            }
                        });*/

                    }

                    else  if(data.get(position).getmVenueOrdCatType().equalsIgnoreCase("4")) { // ==================================for BEVERAGES menu======================================
                        ((BodyViewViewHolder) holder).tv_title.setText(data.get(position).getName());
                        ((BodyViewViewHolder) holder).ll_itemcount.setVisibility(View.GONE);
                        ((BodyViewViewHolder) holder).ll_numpiker_itemcount.setVisibility(View.VISIBLE);

                        ((BodyViewViewHolder) holder).np_itemcount.setValue(Integer.parseInt(data.get(position).getCount()));
                        ((BodyViewViewHolder) holder).np_itemcount.setStepSize(0);
//                        ((BodyViewViewHolder) holder).tv_price_copy.setText("$"+Const.GLOBAL_FORMATTER.format(Double.parseDouble(data.get(position).getItem_price())));

                        ((BodyViewViewHolder) holder).tv_price_total.setText("$"+Const.GLOBAL_FORMATTER.format(Double.parseDouble(data.get(position).getTotal_itemprice())));
                        ((BodyViewViewHolder) holder).btn_remove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertdialog("Are you sure to remove?",position);
                                venuTypeId = data.get(position).getmVenueOrdCatType();
                                delete_str_cart_id=data.get(position).getCart_id();
                                beverage_id=data.get(position).getId3();
                            }
                        });
                    }else if(data.get(position).getmVenueOrdCatType().equalsIgnoreCase("5")
                            || data.get(position).getmVenueOrdCatType().equalsIgnoreCase("6")) { //=====SERVICE and EXTRA SERVICE MENU =======================================================================
                       // Log.d("adapet service",data.get(position).getItem_price());
                        ((BodyViewViewHolder) holder).tv_price_total.setText("$"+Const.GLOBAL_FORMATTER.format(Double.parseDouble(data.get(position).getTotal_itemprice())));
                        ((BodyViewViewHolder) holder).tv_title.setText(data.get(position).getName());
                        ((BodyViewViewHolder) holder).ll_itemcount.setVisibility(View.GONE);
                        ((BodyViewViewHolder) holder).ll_numpiker_itemcount.setVisibility(View.VISIBLE);

                        ((BodyViewViewHolder) holder).np_itemcount.setStepSize(0);
                        ((BodyViewViewHolder) holder).np_itemcount.setValue(Integer.parseInt(data.get(position).getCount()));

                        ((BodyViewViewHolder) holder).tv_price_copy.setText("$"+Const.GLOBAL_FORMATTER.format(Double.parseDouble(data.get(position).getItem_price())));
                        ((BodyViewViewHolder) holder).btn_remove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertdialog("Are you sure to remove?",position);
                                venuTypeId = data.get(position).getmVenueOrdCatType();
                                delete_str_cart_id=data.get(position).getCart_id();
                                service_id=data.get(position).getId2();
                                if(data.get(position).getmVenueOrdCatType().equalsIgnoreCase("5")){
                                    service_option_id=data.get(position).getId3();
                                }else if(data.get(position).getmVenueOrdCatType().equalsIgnoreCase("6")){
                                    extra_service_option_id=data.get(position).getId3();
                                }
                            }
                        });
                    }


                    break;
            }
        }

       /* if (position != 0 && position % data.size() == 0) {
            holder.itemView.setPadding(0, 100, 0, 100);
        } else {
            holder.itemView.setPadding(0, 0, 0, 0);
        }*/

        if (data.get(position) instanceof StickyHeader) {
            holder.itemView.setBackgroundResource(R.color.colorAccent);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override public int getItemCount() {
        return data != null ? data.size() : 0;
    }



    @Override
    public int getItemViewType(int position) {
        if (data != null && data.get(position).getmUiType()==0) {
            return HEADER_TYPE;
        }else if(data!=null && data.get(position).getmUiType()==1){
            return BODY_TYPE;
        }
        return 0;
    }

    @Override public List<?> getAdapterData() {
        return data;
    }

    private static final class HeaderViewViewHolder extends  RecyclerView.ViewHolder {
        TextView row_text_view_only,row_text_view_only_2;
        HeaderViewViewHolder(View itemView) {
            super(itemView);
            row_text_view_only= (TextView) itemView.findViewById(R.id.row_text_view_only);

            row_text_view_only_2= (TextView) itemView.findViewById(R.id.row_text_view_only_2);
            row_text_view_only_2.setVisibility(View.VISIBLE);
        }
    }

    private static final class BodyViewViewHolder extends  RecyclerView.ViewHolder {

        TextView tv_title,tv_date_time,tv_guest,tv_address ,tv_price_total,tv_price_copy;
        TextView btn_remove;
        LinearLayout ll_itemcount,ll_numpiker_itemcount,ll_main_row_item;
        ScrollableNumberPicker np_itemcount;
        TableLayout tl_catering_item_details;


        BodyViewViewHolder(View itemView) {
            super(itemView);

            tv_title= (TextView) itemView.findViewById(R.id.tv_title);
            tv_date_time= (TextView) itemView.findViewById(R.id.tv_date_time);
            tv_guest = (TextView) itemView.findViewById(R.id.tv_guest);
            tv_address =  (TextView) itemView.findViewById(R.id.tv_address);
            tv_price_total = (TextView) itemView.findViewById(R.id.tv_price_total);

            btn_remove=(TextView)itemView.findViewById(R.id.btn_remove);
            ll_itemcount = (LinearLayout)itemView.findViewById(R.id.ll_itemcount);
            ll_numpiker_itemcount=(LinearLayout)itemView.findViewById(R.id.ll_numpiker_itemcount);
            ll_main_row_item =(LinearLayout)itemView.findViewById(R.id.ll_main_row_item);

            np_itemcount = (ScrollableNumberPicker)itemView.findViewById(R.id.np_itemcount);
            tv_price_copy=(TextView)itemView.findViewById(R.id.tv_price_copy);
            tl_catering_item_details = (TableLayout)itemView.findViewById(R.id.tl_catering_item_details);


        }
    }


    public void alertdialog(String message , final int position){
        final AlertDialog.Builder alertbox = new AlertDialog.Builder(mContext);
        alertbox.setMessage(message);
        alertbox.setTitle("Delete ?.");
        alertbox.setIcon(R.mipmap.ic_launcher);

        alertbox.setNeutralButton("YES",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0,int arg1) {

                        new RemoveItem().execute();
                        data.remove(position);
                        notifyDataSetChanged();

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



    String res_venue_resturant_food_addtocart;
    private class AddCartGenricRestaurantfood extends AsyncTask<Object, Void, String> {

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
            //Log.d("post execute", "Executando doInBackground   ingredients");
            try {

                /*data.get(position).getCart_id(),str_menu_id,str_menu_desc,str_item_id,str_item_name,int_Guest_count,str_price_per_plate,str_total_item_price*/

                JSONObject req = new JSONObject();
                req.put("cart_id",parametros[0]);
                req.put("menu_id",parametros[1]);
                req.put("menu_desc",parametros[2]);
                req.put("item_id",parametros[3]);
                req.put("item_name",parametros[4]);
                req.put("guest_count",parametros[5]);
                req.put("price_per_plate",parametros[6]);
                req.put("total_item_price",parametros[7]);

                Log.d("REq Json======", req.toString());

                String response = post(Const.SERVER_URL_API +"/cart_food", req.toString(),"put");//post("http://54.153.127.215/api/login", req.toString());
                //Log.d("REsponce Json====",response);
                res_venue_resturant_food_addtocart = response;
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return res_venue_resturant_food_addtocart;

        }
        @Override
        protected void onPostExecute(String result)
        { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            super.onPostExecute(result);
        }
            //System.out.println("OnpostExecute----done-------");
            progressDialog.dismiss();

            try {
                JSONObject obj = new JSONObject(res_venue_resturant_food_addtocart);
                Log.i("RESPONSE=venue fooods", res_venue_resturant_food_addtocart);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.

                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    /*JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(res).getAsJsonObject();

                    String cart_id = rootObj.getAsJsonObject("message").get("cart_id").getAsString();
                    String flag = rootObj.getAsJsonObject("message").get("flag").getAsString();
                    str_cart_id = cart_id;
                    venue_cart_flag =flag;*/

                    Snackbar snackbar = Snackbar
                            .make(((Activity)mContext).findViewById(android.R.id.content), "Item Added to cart.", Snackbar.LENGTH_LONG);
                            /*.setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });*/
                    // Changing message text color
                    snackbar.setActionTextColor(Color.GREEN);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();

                } else{
                    String message=obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(((Activity)mContext).findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
                            /*.setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });*/
                    // Changing message text color
                    snackbar.setActionTextColor(Color.GREEN);

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

    /*String res;
    private class AddCartSPfood extends AsyncTask<Object, Void, String> {

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
            //Log.d("post execute", "Executando doInBackground   ingredients");
            try {

                JSONObject req = new JSONObject();
                req.put("cart_id",str_cart_ID_SP);
                req.put("menu_id",str_menu_id);
                req.put("menu_desc",str_menu_desc);
                req.put("item_id",str_item_id);
                req.put("item_name",str_item_name);
                req.put("guest_count",int_Guest_count);
                req.put("price_per_plate",str_price_per_plate);
                req.put("total_item_price",str_total_item_price);

                Log.d("REq Json======", req.toString());

                String response = post(Const.SERVER_URL_API+"/cart_food", req.toString(),"put");//post("http://54.153.127.215/api/login", req.toString());
                //Log.d("REsponce Json====",response);
                res = response;
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

            if (res == null || res.equals("")) {

                progressDialog.dismiss();
                Toast.makeText(mContext, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }
            try {

                Log.i("RESPONSE=venue fooods", res);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.
                JSONObject obj = new JSONObject(res);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    *//*JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(res).getAsJsonObject();

                    String cart_id = rootObj.getAsJsonObject("message").get("cart_id").getAsString();
                    String flag = rootObj.getAsJsonObject("message").get("flag").getAsString();
                    str_cart_id = cart_id;
                    venue_cart_flag =flag;*//*

                    Snackbar snackbar = Snackbar
                            .make(((Activity)mContext).findViewById(android.R.id.content), "Item Added to cart.", Snackbar.LENGTH_LONG);
                            *//*.setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });*//*
                    // Changing message text color
                    snackbar.setActionTextColor(Color.GREEN);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();

                } else{
                    String message=obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(((Activity)mContext).findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
                            *//*.setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });*//*
                    // Changing message text color
                    snackbar.setActionTextColor(Color.GREEN);

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
    }*/

    String res;
    class RemoveItem extends AsyncTask<Object, Void, String> {

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


            //Log.d("post execute", "Executando doInBackground   ingredients");
            try {

                JSONObject req = new JSONObject();
                //req.put("user_id",sharepref.getString(Const.PREF_USER_ID,null));
                req.put("cart_id",delete_str_cart_id);

                //for restaurant menu - restaurant_menu_id,cart_id
                //for catring menu - catering_menu_id ,cart_id
                //for beverage - beverage_id ,cart_id
                //for service - service_id,service_option_id ,cart_id
                //for extra service - extra_service_id,extra_service_option_id,cart_id

                /* mVenueOrdCatType
                    1=pricingplan;2=food;3=catering;4=beverage;5=service;6=extra_service*/
               /*id types =
               * id1=menu_id; id2 = course_id;  id3=item_id(Generic ID);*/

                if(venuTypeId.equalsIgnoreCase("1")){
                    String response = post(Const.SERVER_URL_API +"remove_cart?cart_id="+delete_str_cart_id+"&full_delete=1", req.toString(),"get");
                   // Log.d("delete_str_cart_id====",Const.SERVER_URL_API+"cart_detail/cart_id/"+delete_str_cart_id+"/full_delete/1");
                    res = response;
                }else if(venuTypeId.equalsIgnoreCase("2")){
                    String response = post(Const.SERVER_URL_API +"remove_cart?"+"cart_id="+delete_str_cart_id/*+"&full_delete=0"*/+"&restaurant_menu_id="+restaurant_menu_id+"&restaurant_menu_item_id="+restaurant_menu_item_id, req.toString(),"get");
                     Log.d("delete_str_cart_id====",Const.SERVER_URL_API +"remove_cart?"+"cart_id="+delete_str_cart_id/*+"&full_delete=0"*/+"&restaurant_menu_id="+restaurant_menu_id+"&restaurant_menu_item_id="+restaurant_menu_item_id);
                    res = response;
                }else if(venuTypeId.equalsIgnoreCase("3")){
                    if(str_remove_whole_cateringOrder.equalsIgnoreCase("1")){
                        String response = post(Const.SERVER_URL_API +"remove_cart?cart_id="+delete_str_cart_id+"&full_delete=1", req.toString(),"get");
                        // Log.d("delete_str_cart_id====",Const.SERVER_URL_API+"cart_detail/cart_id/"+delete_str_cart_id+"/full_delete/1");
                        res = response;
                    }
                    if(str_remove_menu_item.equalsIgnoreCase("1")) {
                        String response = post(Const.SERVER_URL_API + "remove_cart?cart_id=" + delete_str_cart_id + "&catering_menu_id=" + catering_menu_id+ "&catering_menu_item_id=" + catering_menu_item_id, req.toString(), "get");
                        Log.d("delete_catring item",Const.SERVER_URL_API + "remove_cart?cart_id=" + delete_str_cart_id + "&catering_menu_id=" + catering_menu_id+ "&catering_menu_item_id=" + catering_menu_item_id);
                        res = response;
                    }else if(str_remove_menu.equalsIgnoreCase("1")){
                        String response = post(Const.SERVER_URL_API + "remove_cart?cart_id=" + delete_str_cart_id + "&catering_menu_id=" + catering_menu_id, req.toString(), "get");
                        Log.d("delete_catering menu",Const.SERVER_URL_API + "remove_cart?cart_id=" + delete_str_cart_id + "&catering_menu_id=" + catering_menu_id);
                        res = response;
                    }
                }else if(venuTypeId.equalsIgnoreCase("4")){
                    String response = post(Const.SERVER_URL_API +"remove_cart?cart_id="+delete_str_cart_id+"&beverage_id="+beverage_id, req.toString(),"get");
                    // Log.d("delete_str_cart_id====",Const.SERVER_URL_API+"cart_detail/cart_id/"+delete_str_cart_id+"/full_delete/1");
                    res = response;
                }else if(venuTypeId.equalsIgnoreCase("5")){
                    String response = post(Const.SERVER_URL_API +"remove_cart?cart_id="+delete_str_cart_id+"&service_id="+service_id+"&service_option_id="+service_option_id, req.toString(),"get");
                     Log.d("delete_str_cart_id====",Const.SERVER_URL_API +"remove_cart?cart_id="+delete_str_cart_id+"&service_id="+service_id+"&service_option_id="+service_option_id);
                    res = response;
                }else if(venuTypeId.equalsIgnoreCase("6")){
                    String response = post(Const.SERVER_URL_API +"remove_cart?cart_id="+delete_str_cart_id+"&extra_service_id="+service_id+"&extra_service_option_id="+extra_service_option_id, req.toString(),"get");
                     Log.d("delete_str_cart_id====",Const.SERVER_URL_API +"remove_cart?cart_id="+delete_str_cart_id+"&extra_service_id="+service_id+"&extra_service_option_id="+extra_service_option_id);
                    res = response;
                }


                /*((Activity)mContext).startActivity(new Intent(mContext, CartVenuesNCateringDetails.class));
                ((Activity) mContext).finish();*/


                /*req.put("booking_date",str_date);
                req.put("booking_time_from",str_start);
                req.put("booking_time_to",str_end);
                req.put("event_type_id",str_event_type_id);
                req.put("description",str_et_description);
                req.put("number_of_guests",str_et_guestCount);*/

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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
            Log.i("RESPONSE==deelet cart==", res);
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

                    mContext.startActivity(new Intent(mContext, CartVenuesNCateringDetails.class)
                            .putExtra("cart_id",cart_details_cart_id)
                            .putExtra("detailView",cart_details_detailView));
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
