package ovenues.com.ovenue.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import movile.com.creditcardguide.APICall;
import ovenues.com.ovenue.R;
import ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderGenericPricingFragment;
import ovenues.com.ovenue.modelpojo.ServiceProviderChargesDefaultModel;
import ovenues.com.ovenue.utils.Const;
import ovenues.com.ovenue.utils.Utils;

import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.activitySP;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.open_hours;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.providerAddress;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.str_service_provider_id;
import static ovenues.com.ovenue.utils.APICall.post;
import static ovenues.com.ovenue.utils.Const.GLOBAL_FORMATTER;

/**
 * Created by Jay-Andriod on 02-May-17.
 */




public class ServiceProviderChargesDefaultAdapter extends RecyclerView
        .Adapter<ServiceProviderChargesDefaultAdapter
        .DataObject_postHolder> implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener{

    TextView tv_final_price,tv_total_textonly;
    AutoCompleteTextView et_address_line;

    boolean IsGuestEntered = false;
    JSONObject service_provider_item_obj;
    static private ArrayList<ServiceProviderChargesDefaultModel> mDataset;
    static private Context mContext;
    Dialog dialog;

    String str_charge_id,diff,str_deliveryAddress;
    int extra_person=0;
    SharedPreferences sharepref;
    String str_id, str_service_id, str_service_title, str_service_inclusions, str_charges, str_is_flat_charges, str_is_per_person_charges, str_is_per_item, str_pacakage_hours,
            str_hour_extension_charges, str_extension_duration, str_extra_person_charges, str_max_guest_extension, str_max_hour_extension, str_is_group_size, str_group_size_from,
            str_group_size_to, str_photo_url, str_is_delivery, str_is_delivery_paid, str_delivery_charges, str_is_pickup, str_is_onsite_service, str_is_delivery_na;

    private String str_date=null,str_delivery_time=null,str_event_type_id,str_et_guestCount ,str_et_message,str_et_specialmessage,str_address1,str_send_type,pincode,SP_address,str_place_id;

     String latitude, longitude;

    String toatl_max_duration="";

   public Double mainPrice=0.0,extraPersonPrice=0.0,extraItemPrice=0.0,extraDuratoinPrice=0.0,deliveryPrice=0.0,finalPrice;

   static GoogleApiClient mGoogleApiClient;
    ArrayList<String> placeID = null;
    ArrayList<String> termsList = null;

    @Override
    public void onClick(View v) {

    }


    private class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements
            Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public android.widget.Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    // String filterString = constraint.toString();
                    // System.out.println("Text:::::::::::"+filterString);
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        //System.out.println("Text:::::::::::" + constraint.toString());
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint,
                                              FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return (String) resultList.get(index);
        }
    }


    public ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(Const.PLACES_API_BASE
                    + Const.TYPE_AUTOCOMPLETE + Const.OUT_JSON);
            sb.append("?key=AIzaSyDtr3qaj_rlFGjLyPW4OClB7r7oO6S5jj4");
            // sb.append("&components=country:il");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            URL url = new URL(sb.toString());
            //System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {

            return resultList;
        } catch (IOException e) {

            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            if (placeID == null)
            {placeID = new ArrayList<String>();}
            else{
                placeID.clear();

                // Create a JSON object hierarchy from the results
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
                JSONArray termsJsonArray = null;
                // System.out.println(predsJsonArray);

                // Extract the Place descriptions from the results
                resultList = new ArrayList(predsJsonArray.length());
                termsList = new ArrayList<String>();

                for (int i = 0; i < predsJsonArray.length(); i++) {
                    //System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                    //System.out.println("============================================================");
                    resultList.add(predsJsonArray.getJSONObject(i).getString("description"));

                    placeID.add(predsJsonArray.getJSONObject(i).getString("place_id"));
                    //Log.d("place id",predsJsonArray.getJSONObject(i).getString("place_id"));
                    termsJsonArray = predsJsonArray.getJSONObject(i).getJSONArray(
                            "terms");

                    //System.out.println("termsJsonArray:::::::" + termsJsonArray);

                    int lenght = termsJsonArray.length() - 1;

                    //System.out.println("lenght:::::::" + lenght);

                    termsList.add(termsJsonArray.getJSONObject(lenght).getString("value"));
                    // System.out.println("termsList:::::::::::" + termsList);
                    // }
                }
            }
        } catch (JSONException e) {

        }

        return resultList;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    public static class DataObject_postHolder extends RecyclerView.ViewHolder {

        TextView tv_title,tv_time,tv_guestcount,tv_delivery ,tv_price,tv_extracharge ,tv_chargeType,tv_description,tv_readmore,tv_addtocart;
        ImageView img_charge;
        LinearLayout ll_raw_sp_charges_default;

        public DataObject_postHolder(final View itemView) {
            super(itemView);

            ll_raw_sp_charges_default = (LinearLayout)itemView.findViewById(R.id.ll_raw_sp_charges_default);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_guestcount = (TextView) itemView.findViewById(R.id.tv_guestcount);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_chargeType  = (TextView) itemView.findViewById(R.id.tv_chargeType);
            tv_extracharge= (TextView) itemView.findViewById(R.id.tv_extracharge);
            tv_delivery= (TextView) itemView.findViewById(R.id.tv_delivery);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_readmore = (TextView) itemView.findViewById(R.id.tv_readmore);
            tv_addtocart= (TextView) itemView.findViewById(R.id.tv_addtocart);

            img_charge=(ImageView)itemView.findViewById(R.id.img_charge);

            mGoogleApiClient = new GoogleApiClient
                    .Builder(mContext)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();

        }
    }

   /* public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }*/

    public ServiceProviderChargesDefaultAdapter(ArrayList<ServiceProviderChargesDefaultModel> myDataset, Context context) {
        mDataset = myDataset;
        mContext=context;
    }

    @Override
    public DataObject_postHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_serviceprovider_charges_default, parent, false);
        DataObject_postHolder dataObjectHolder = new DataObject_postHolder(view);


        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObject_postHolder holder,final int position) {

        holder.setIsRecyclable(false);
        /*if(position%2==0){
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#00F7F9F9"));
        }*/

        sharepref = mContext.getApplicationContext().getSharedPreferences("MyPref",mContext.MODE_PRIVATE);

        str_id= mDataset.get(position).getId();
        str_service_id= mDataset.get(position).getService_id();

        str_service_title= mDataset.get(position).getService_title();
        str_service_inclusions= mDataset.get(position).getService_inclusions();

        str_charges= mDataset.get(position).getCharges();

        str_is_flat_charges= mDataset.get(position).getIs_flat_charges();
        str_is_per_person_charges= mDataset.get(position).getIs_per_person_charges();
        str_is_per_item= mDataset.get(position).getIs_per_item();

        str_pacakage_hours= mDataset.get(position).getPacakage_hours();
        str_pacakage_hours = str_pacakage_hours==null
        ?"00:00":Utils.convertDateStringToString(str_pacakage_hours,"HH:mm:ss","HH:mm");
        str_hour_extension_charges= mDataset.get(position).getHour_extension_charges();
        str_extension_duration= mDataset.get(position).getExtension_duration();
        str_extra_person_charges= mDataset.get(position).getExtra_person_charges();

        str_max_guest_extension= mDataset.get(position).getMax_guest_extension();
        str_max_hour_extension= mDataset.get(position).getMax_hour_extension();
        str_is_group_size= mDataset.get(position).getIs_group_size();
        str_group_size_from= mDataset.get(position).getGroup_size_from();
        str_group_size_to= mDataset.get(position).getGroup_size_to();
        str_photo_url= mDataset.get(position).getPhoto_url();

        str_is_delivery= mDataset.get(position).getIs_delivery();
        str_is_delivery_paid= mDataset.get(position).getIs_delivery_paid();
        str_delivery_charges= mDataset.get(position).getDelivery_charges();
        str_is_pickup= mDataset.get(position).getIs_pickup();
        str_is_onsite_service= mDataset.get(position).getIs_onsite_service();
        str_is_delivery_na= mDataset.get(position).getIs_delivery_na();

        try {
            if(str_photo_url!=null) {
                final String venue_pic= str_photo_url!=null ? str_photo_url : null;
                //Log.d("imageURL====",venue_pic);

                Glide.with(mContext)
                        .load(venue_pic)
                        .asBitmap()
                        .placeholder(R.drawable.loading_image_pic)
                        /*.error(R.drawable.no_image)*/
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                // Do something with bitmap here.
                                holder.img_charge.setImageBitmap(bitmap);
                                //Log.e("GalleryAdapter","Glide getMedium ");

                                Glide.with(mContext)
                                        .load(venue_pic)
                                        .asBitmap()
                                        /*.error(R.drawable.no_image)*/
                                        .placeholder(R.drawable.loading_image_pic)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                                // Do something with bitmap here.
                                                holder.img_charge.setImageBitmap(bitmap);
                                                //Log.e("GalleryAdapter","Glide getLarge ");
                                            }
                                        });
                            }
                        });
            }else{

            }
        }catch (Exception expbitmap){
            expbitmap.printStackTrace();
        }

        if(str_is_flat_charges.equalsIgnoreCase("1")){
            holder.tv_price.setText("$"+str_charges);
            holder.tv_chargeType.setVisibility(View.GONE);
        }else if(str_is_per_person_charges.equalsIgnoreCase("1")){
            holder.tv_price.setText("$"+str_charges);
            holder.tv_chargeType.setVisibility(View.VISIBLE);
            holder.tv_chargeType.setText("per Person");
        }else if(str_is_per_item.equalsIgnoreCase("1")){
            holder.tv_price.setText("$"+str_charges);
            holder.tv_chargeType.setVisibility(View.VISIBLE);
            holder.tv_chargeType.setText("per Item");
        }else{
            holder.tv_price.setText("$"+str_charges);
            holder.tv_chargeType.setVisibility(View.GONE);
        }


        holder.tv_title.setText(mDataset.get(position).getService_title());
        if(str_pacakage_hours!=null) {
            if (!str_pacakage_hours.equalsIgnoreCase("00:00")) {
                if (str_max_hour_extension != null &&
                        (!str_max_hour_extension.equalsIgnoreCase("00:00") && !str_max_hour_extension.equalsIgnoreCase("00:00:00"))) {
                    holder.tv_time.setText(str_pacakage_hours + " hr ." + "( ext upto : " + str_max_hour_extension + " )*");
                } else {
                    Log.e("pkg_hr==",str_pacakage_hours);
                    holder.tv_time.setText(str_pacakage_hours + " hr .");
                }
            }else{
                holder.tv_time.setVisibility(View.GONE);
            }
        }else{
            holder.tv_time.setVisibility(View.GONE);
        }

        if(str_is_group_size.equalsIgnoreCase("1")){
            if(str_max_guest_extension!=null && str_max_guest_extension.length()>0){
                int ext_toatl_guest = Integer.parseInt(str_group_size_to)+Integer.parseInt(str_max_guest_extension);
                holder.tv_guestcount.setText(str_group_size_from+"-"+str_group_size_to+" ( max : "+ext_toatl_guest+" )*");
            }else{
                holder.tv_guestcount.setText(str_group_size_from+"-"+str_group_size_to);
            }
        }else{
            holder.tv_guestcount.setVisibility(View.GONE);
        }

        String extra_guest_charge="",extra_duration_charge="";
        if(str_extra_person_charges!=null && !str_extra_person_charges.equalsIgnoreCase("0"))
        {
            extra_guest_charge="Extra\n$ "+str_extra_person_charges+"/ person";
        }
        if(str_extension_duration!=null && !str_extension_duration.equalsIgnoreCase("0")){
            String[] time = mDataset.get(position).getExtension_duration().split(":");
            String hh = time[0];
            String mm = time[1];
            String ss = time[2];

            if(hh.equalsIgnoreCase("00")){
                if(!mm.equalsIgnoreCase("00")){
                    extra_duration_charge="Extra\n$ "+str_hour_extension_charges +" / "+mm+" min";
                    Log.d("time min",hh+mm+ss);
                }else{
                    extra_duration_charge="Extra\n$ "+str_hour_extension_charges +" / "+ss+" sec";
                    Log.d("time sec",hh+mm+ss);
                }
            }else{
                extra_duration_charge="Extra\n$ "+str_hour_extension_charges +" / "+str_extension_duration+" hours";
            }
        }

        if(!extra_guest_charge.equalsIgnoreCase("") || !extra_duration_charge.equalsIgnoreCase("")){
            if(extra_guest_charge!=null){
                holder.tv_extracharge.setText(extra_guest_charge+"\n"+extra_duration_charge);
            }else{
                holder.tv_extracharge.setText(extra_duration_charge);

            }
        }else{
            holder.tv_extracharge.setVisibility(View.GONE);
        }



        if(str_is_delivery.equalsIgnoreCase("1")) {
            if (str_is_delivery_paid.equalsIgnoreCase("1")) {
                holder.tv_delivery.setText("Delivery Charge :  " + str_delivery_charges);
            } else {
                holder.tv_delivery.setVisibility(View.GONE);
            }
        }

        holder.tv_description.setText(mDataset.get(position).getService_inclusions());
        holder.tv_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        holder.tv_readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertbox = new AlertDialog.Builder(mContext);
                alertbox.setMessage(mDataset.get(position).getService_inclusions().toString());
                alertbox.setTitle("Description ");
                alertbox.setIcon(R.mipmap.ic_launcher);

                alertbox.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0,int arg1) {

                            }
                        });
                alertbox.show();
            }
        });

        holder.ll_raw_sp_charges_default.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {

                dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_dialog_service_addtocart);

                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                final Display display = wm.getDefaultDisplay();
                // display size in pixels
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;

                dialog.setCanceledOnTouchOutside(true);
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.width = width;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                lp.dimAmount = 0.81f;
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                dialog.getWindow().setAttributes(lp);

                final TextView tv_title,tv_time,tv_guestcount,tv_delivery ,dia_tv_price,tv_extracharge,tv_delivery_freecharge ,tv_description,tv_readmore,tv_final_addtocart
                        ,tv_duration_title;
                final ImageView img_charge;
                final LinearLayout ll_duration,ll_itemcount;
                final ScrollableNumberPicker np_items;
                final Spinner sp_hh,sp_mm;


                final RadioGroup radioTypeGroup;
                final RadioButton rb_delivery,rb_pickup,rb_onsite;
                final TextView SP_servicename,tv_error_datetime;
                final EditText et_date ,et_guestCount,/*et_message,*/et_delivery_time/*,et_special_message*/ ;

                final  int mYear, mMonth, mDay;
                final TextInputLayout/* til_guest_count_hint ,til_date ,til_time ,*/til_address;

                final List<String> timeslotlist=new ArrayList<String>();

                mainPrice=0.0;extraPersonPrice=0.0;extraItemPrice=0.0;extraDuratoinPrice=0.0;deliveryPrice=0.0;

                img_charge=(ImageView)dialog.findViewById(R.id.img_charge);

                SP_servicename = (TextView) dialog.findViewById(R.id.SP_servicename);
                SP_servicename.setText(holder.tv_title.getText().toString());

                tv_title = (TextView) dialog.findViewById(R.id.tv_title);
                tv_time = (TextView) dialog.findViewById(R.id.tv_time);
                tv_guestcount = (TextView) dialog.findViewById(R.id.tv_guestcount);
                dia_tv_price = (TextView) dialog.findViewById(R.id.tv_price);
                tv_extracharge= (TextView) dialog.findViewById(R.id.tv_extracharge);
                tv_delivery= (TextView) dialog.findViewById(R.id.tv_delivery);
                tv_delivery_freecharge =(TextView) dialog.findViewById(R.id.tv_delivery_freecharge);
                tv_description = (TextView) dialog.findViewById(R.id.tv_description);
                tv_readmore = (TextView) dialog.findViewById(R.id.tv_readmore);
                tv_final_price = (TextView) dialog.findViewById(R.id.tv_final_price);
                tv_total_textonly =  (TextView) dialog.findViewById(R.id.tv_total_textonly);
                tv_final_addtocart = (TextView) dialog.findViewById(R.id.tv_addtocart);
                tv_duration_title=(TextView)dialog.findViewById(R.id.tv_duration_title);
                tv_error_datetime=(TextView)dialog.findViewById(R.id.tv_error_datetime);
                tv_error_datetime.setVisibility(View.GONE);


                ll_duration = (LinearLayout)dialog.findViewById(R.id.ll_duration);
                sp_hh = (Spinner)dialog.findViewById(R.id.sp_hh);
                sp_mm = (Spinner)dialog.findViewById(R.id.sp_mm);

                mainPrice=Double.parseDouble(mDataset.get(position).getCharges().toString());

                List<String> list_hh = new ArrayList<String>();
                list_hh.add("HH");

                ArrayAdapter<String> dataAdapter_hh = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, list_hh);
                dataAdapter_hh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_hh.setAdapter(dataAdapter_hh);

                List<String> list_mm = new ArrayList<String>();
                list_mm.add("MM");
                list_mm.add("00");
                list_mm.add("15");
                list_mm.add("30");
                list_mm.add("45");

                ArrayAdapter<String> dataAdapter_mm = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, list_mm);
                dataAdapter_mm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_mm.setAdapter(dataAdapter_mm);


               /* til_guest_count_hint=(TextInputLayout)dialog.findViewById(R.id.til_guest_count_hint);
                til_date =(TextInputLayout)dialog.findViewById(R.id.til_date);
                til_time =(TextInputLayout)dialog.findViewById(R.id.til_time);*/
                til_address =(TextInputLayout)dialog.findViewById(R.id.til_address);

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                final int mHour = c.get(Calendar.HOUR_OF_DAY);
                final int mMinute = c.get(Calendar.MINUTE);

                et_date = (EditText) dialog.findViewById(R.id.et_date);
                et_guestCount= (EditText) dialog.findViewById(R.id.et_guestCount);
                /*et_message= (EditText) dialog.findViewById(R.id.et_message);*/
                et_address_line= (AutoCompleteTextView) dialog.findViewById(R.id.et_address_line);

                et_delivery_time= (EditText) dialog.findViewById(R.id.et_delivery_time);
                /*et_special_message= (EditText) dialog.findViewById(R.id.et_special_message);*/

                radioTypeGroup=(RadioGroup)dialog.findViewById(R.id.radioGroup);

                rb_delivery=(RadioButton)dialog.findViewById(R.id.rb_delivery) ;
                rb_pickup=(RadioButton)dialog.findViewById(R.id.rb_pickup);
                rb_onsite=(RadioButton)dialog.findViewById(R.id.rb_onsite);

                if(mDataset.get(position).getIs_delivery().equalsIgnoreCase("0")){
                    rb_delivery.setVisibility(View.GONE);
                }
                if(mDataset.get(position).getIs_pickup().equalsIgnoreCase("0")){
                    rb_pickup.setVisibility(View.GONE);
                }
                if(mDataset.get(position).getIs_onsite_service().equalsIgnoreCase("0")){
                    rb_onsite.setVisibility(View.GONE);
                }

                ll_itemcount = (LinearLayout)dialog.findViewById(R.id.ll_itemcount);
                np_items = (ScrollableNumberPicker)dialog.findViewById(R.id.np_items);

                tv_title.setText(holder.tv_title.getText().toString());
                tv_time.setText(holder.tv_time.getText().toString());
                tv_guestcount.setText(holder.tv_guestcount.getText().toString());
                dia_tv_price.setText(holder.tv_price.getText().toString());
                tv_delivery.setText(holder.tv_delivery.getText().toString());
                tv_delivery_freecharge.setText(holder.tv_delivery.getText().toString());

                tv_extracharge.setText(holder.tv_extracharge.getText().toString());
                tv_description.setText(mDataset.get(position).getService_inclusions());


                try {
                    if( mDataset.get(position).getPhoto_url()!=null) {
                        final String venue_pic=  mDataset.get(position).getPhoto_url()!=null ?  mDataset.get(position).getPhoto_url() : null;
                        //Log.d("imageURL====",venue_pic);

                        Glide.with(mContext)
                                .load(venue_pic)
                                .asBitmap()
                                .placeholder(R.drawable.loading_image_pic)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                        // Do something with bitmap here.
                                        img_charge.setImageBitmap(bitmap);
                                        //Log.e("GalleryAdapter","Glide getMedium ");

                                        Glide.with(mContext)
                                                .load(venue_pic)
                                                .asBitmap()
                                                .placeholder(R.drawable.loading_image_pic)
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(new SimpleTarget<Bitmap>() {
                                                    @Override
                                                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                                        // Do something with bitmap here.
                                                        img_charge.setImageBitmap(bitmap);
                                                        //Log.e("GalleryAdapter","Glide getLarge ");
                                                    }
                                                });
                                    }
                                });
                    }else{

                    }
                }catch (Exception expbitmap){
                    expbitmap.printStackTrace();
                }

                tv_readmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder alertbox = new AlertDialog.Builder(mContext);
                        alertbox.setMessage(mDataset.get(position).getService_inclusions().toString());
                        alertbox.setTitle("Description ");
                        alertbox.setIcon(R.mipmap.ic_launcher);

                        alertbox.setNeutralButton("OK",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0,int arg1) {

                                    }
                                });
                        alertbox.show();
                    }
                });


                tv_final_price.setText(GLOBAL_FORMATTER.format(mainPrice));
                Log.d("main price firstofall=",""+mDataset.get(position).getCharges().toString());

                if(mDataset.get(position).getIs_flat_charges().equalsIgnoreCase("1")){
                    dia_tv_price.setText(mDataset.get(position).getCharges());
                    ll_itemcount.setVisibility(View.GONE);
                }else if(mDataset.get(position).getIs_per_person_charges().equalsIgnoreCase("1")){
                    dia_tv_price.setText(mDataset.get(position).getCharges()+"/ Person.");
                    ll_itemcount.setVisibility(View.GONE);
                }else if(mDataset.get(position).getIs_per_item().equalsIgnoreCase("1")){
                    dia_tv_price.setText(mDataset.get(position).getCharges()+"/ Item.");
                    ll_itemcount.setVisibility(View.VISIBLE);
                    np_items.setListener(new ScrollableNumberPickerListener() {
                        @Override
                        public void onNumberPicked(int value) {
                            extraItemPrice = (Double.parseDouble(mDataset.get(position).getCharges()) * (double) value) - mainPrice;

                            if(deliveryPrice<1){deliveryPrice=0.00;}
                            Log.d("All prices ",""+(mainPrice +"\b,"+ extraPersonPrice+"\b," + extraItemPrice+"\b," + extraDuratoinPrice));
                            tv_final_price.setText(GLOBAL_FORMATTER.format(mainPrice+extraPersonPrice + extraItemPrice + extraDuratoinPrice)
                                    +"\n"+GLOBAL_FORMATTER.format(deliveryPrice)
                                    +"\n"+GLOBAL_FORMATTER.format(mainPrice+extraPersonPrice + extraItemPrice + extraDuratoinPrice+deliveryPrice));
                            tv_total_textonly.setText("Price($)\nDelivery($)"
                                    +"\nTotal($)");
                        }
                    });
                }else{
                    ll_duration.setVisibility(View.GONE);
                    sp_hh.setVisibility(View.GONE);
                    sp_mm.setVisibility(View.GONE);
                    tv_duration_title.setVisibility(View.GONE);
                    ll_itemcount.setVisibility(View.GONE);
                }

                if(mDataset.get(position).getIs_delivery()!=null && mDataset.get(position).getIs_delivery().equalsIgnoreCase("1"))
                {
                    rb_delivery.setVisibility(View.VISIBLE);
                    et_delivery_time.setVisibility(View.VISIBLE);
                } else {
                    rb_delivery.setVisibility(View.GONE);
                }

                if(mDataset.get(position).getIs_onsite_service()!=null && mDataset.get(position).getIs_onsite_service().equalsIgnoreCase("1"))
                {
                    rb_onsite.setVisibility(View.VISIBLE);
                } else {
                    rb_onsite.setVisibility(View.GONE);
                }

                if(mDataset.get(position).getIs_pickup()!=null && mDataset.get(position).getIs_pickup().equalsIgnoreCase("1"))
                {
                    rb_pickup.setVisibility(View.VISIBLE);
                } else {
                    rb_pickup.setVisibility(View.GONE);
                }




                if(mDataset.get(position).getIs_group_size().equalsIgnoreCase("1")){
                    if(mDataset.get(position).getMax_guest_extension()!=null){
                        int total_guest_hint = Integer.parseInt(mDataset.get(position).getGroup_size_to()) + Integer.parseInt(mDataset.get(position).getMax_guest_extension());
                        //Log.d("total guest----",""+total_guest_hint);
                        et_guestCount.setHint("( "+mDataset.get(position).getGroup_size_from()+" - "+(int)total_guest_hint+" )");
                    }else{
                        et_guestCount.setHint("( "+mDataset.get(position).getGroup_size_from()+" - "+mDataset.get(position).getGroup_size_to() +" )");
                    }
                }
                /*if(mDataset.get(position).getIs_group_size().equalsIgnoreCase("0")){
                    tv_guestcount.setVisibility(View.GONE);
                }*/

                et_guestCount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(s.length()>0){
                            String s1=s.toString();
                            int sCount = Integer.parseInt(s1.trim());

                            if(mDataset.get(position).getIs_per_person_charges().equalsIgnoreCase("1") && mDataset.get(position).getIs_group_size().equalsIgnoreCase("0")){
                                Log.d("guest count conditon ","1");
                                // mainPrice = sCount * Double.parseDouble(mDataset.get(position).getCharges());
                                extraPersonPrice = (double)((sCount-1)*Integer.parseInt(mDataset.get(position).getCharges()));
                                IsGuestEntered=true;
                            }else if(mDataset.get(position).getIs_per_person_charges().equalsIgnoreCase("1") && mDataset.get(position).getIs_group_size().equalsIgnoreCase("1")){
                                Log.d("guest count  conditon ","2");
                                int guest_MAX_extenstion=0;
                                if(mDataset.get(position).getMax_guest_extension()!=null){
                                    guest_MAX_extenstion=Integer.parseInt(mDataset.get(position).getMax_guest_extension());
                                }

                                if (sCount > (Integer.parseInt(mDataset.get(position).getGroup_size_to()) + guest_MAX_extenstion)) {
                                    et_guestCount.setError("Maximum " + (int) (Integer.parseInt(mDataset.get(position).getGroup_size_to()) + guest_MAX_extenstion)+" Guests Allowed.");
                                    IsGuestEntered=false;
                                }else if(sCount < (Integer.parseInt(mDataset.get(position).getGroup_size_from() ) )){
                                    et_guestCount.setError("Minimum " + (int) (Integer.parseInt(mDataset.get(position).getGroup_size_from()))+" Guests Required.");
                                    IsGuestEntered=false;
                                } else {
                                    if (mDataset.get(position).getExtra_person_charges() != null) {
                                        if (sCount > Integer.parseInt(mDataset.get(position).getGroup_size_to())) {
                                            extra_person = sCount - Integer.parseInt(mDataset.get(position).getGroup_size_to());
                                            double extra_perPersonPrice = Double.parseDouble(mDataset.get(position).getExtra_person_charges());
                                            extraPersonPrice = extra_person * extra_perPersonPrice;

                                            double per_person_mainPrice = (sCount-1-extra_person) * Double.parseDouble(mDataset.get(position).getCharges());

                                            extraPersonPrice = extraPersonPrice +per_person_mainPrice;
                                            IsGuestEntered=true;
                                        }
                                    }else{
                                        extraPersonPrice = (sCount-1) * Double.parseDouble(mDataset.get(position).getCharges());
                                        IsGuestEntered=true;
                                    }
                                }
                            }else if(mDataset.get(position).getIs_per_person_charges().equalsIgnoreCase("0") && mDataset.get(position).getIs_group_size().equalsIgnoreCase("1")){
                                Log.d("guest count conditon ","3");
                                int guest_MAX_extenstion=0;
                                if(mDataset.get(position).getMax_guest_extension()!=null){
                                    guest_MAX_extenstion=Integer.parseInt(mDataset.get(position).getMax_guest_extension());
                                }

                                if (sCount > (Integer.parseInt(mDataset.get(position).getGroup_size_to()) + guest_MAX_extenstion)) {
                                    et_guestCount.setError("Maximum " + (int) (Integer.parseInt(mDataset.get(position).getGroup_size_to()) + guest_MAX_extenstion)+" Guests Allowed.");
                                    IsGuestEntered=false;
                                }else if(sCount < (Integer.parseInt(mDataset.get(position).getGroup_size_from() ) )){
                                    et_guestCount.setError("Minimum " + (int) (Integer.parseInt(mDataset.get(position).getGroup_size_from()))+" Guests Required.");
                                    IsGuestEntered =false;
                                } else {
                                    if (mDataset.get(position).getExtra_person_charges() != null) {

                                        if (sCount > Integer.parseInt(mDataset.get(position).getGroup_size_to())) {

                                            extra_person = sCount - Integer.parseInt(mDataset.get(position).getGroup_size_to());
                                            double extra_perPersonPrice = Double.parseDouble(mDataset.get(position).getExtra_person_charges());
                                            extraPersonPrice = (double)extra_person * extra_perPersonPrice;
                                            IsGuestEntered=true;
                                            Log.d("ext person,ext price ",""+extra_person +"\b"+extraPersonPrice);
                                        }else{
                                            extraPersonPrice=0.0;

                                            IsGuestEntered=true;
                                        }
                                    }else{
                                        extraPersonPrice=0.0;
                                        // mainPrice = Double.parseDouble(mDataset.get(position).getCharges());
                                        IsGuestEntered=true;
                                    }
                                }
                            }else if(mDataset.get(position).getIs_per_person_charges().equalsIgnoreCase("0") && mDataset.get(position).getIs_group_size().equalsIgnoreCase("0")){
                                Log.d("guest count  conditon ","4");
                                //mainPrice = Double.parseDouble(mDataset.get(position).getCharges());
                                IsGuestEntered=true;
                            }
                            Log.d("All prices ",""+(mainPrice +"\b,"+ extraPersonPrice+"\b," + extraItemPrice+"\b," + extraDuratoinPrice));
                            //mainPrice = Double.parseDouble(mDataset.get(position).getCharges());
                            //tv_final_price.setText(GLOBAL_FORMATTER.format((mainPrice + extraPersonPrice + extraItemPrice + extraDuratoinPrice)));
                            if(deliveryPrice<1){deliveryPrice=0.00;}
                            tv_final_price.setText(GLOBAL_FORMATTER.format(mainPrice+extraPersonPrice + extraItemPrice + extraDuratoinPrice)
                                   +"\n"+GLOBAL_FORMATTER.format(deliveryPrice)
                                   +"\n"+GLOBAL_FORMATTER.format(mainPrice+extraPersonPrice + extraItemPrice + extraDuratoinPrice+deliveryPrice));
                            tv_total_textonly.setText("Price($)\nDelivery($)"
                                    +"\nTotal($)");
                        }
                    }
                });

                //==extenstion duraton is null but onsite service keep show duration any how ,Order by Jolly form US.
                if(mDataset.get(position).getExtension_duration()!=null || mDataset.get(position).getIs_onsite_service().equalsIgnoreCase("1") && !mDataset.get(position).getService_id().equalsIgnoreCase("2") && !mDataset.get(position).getService_id().equalsIgnoreCase("1")) {
                    ll_duration.setVisibility(View.VISIBLE);
                    tv_duration_title.setVisibility(View.VISIBLE);

                    sp_hh.setVisibility(View.VISIBLE);
                    sp_mm.setVisibility(View.VISIBLE);


                    if(mDataset.get(position).getPacakage_hours()!=null){
                    String[] pkg_hours = mDataset.get(position).getPacakage_hours().split(":");
                    String str_pkg_hr = pkg_hours[0];
                    String str_pkg_min = pkg_hours[1];
                    }else{

                    }


                    if(mDataset.get(position).getMax_hour_extension() != null){

                        String[] max_hour_extenstion = mDataset.get(position).getMax_hour_extension().split(":");
                        String max_ext_hh = max_hour_extenstion[0];
                        String max_ext_min = max_hour_extenstion[1];
                        String max_ext_sec = max_hour_extenstion[2];
                        Log.d("max_hour_extenstion cond 1",max_ext_hh+"---"
                                +"---"+max_ext_min
                                +"---"+max_ext_sec);

                        if (!max_ext_hh.equalsIgnoreCase("00") || !max_ext_min.equalsIgnoreCase("00") || !max_ext_sec.equalsIgnoreCase("00")) {



                            String time1 = mDataset.get(position).getPacakage_hours();
                            String time2 = mDataset.get(position).getMax_hour_extension();

                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                            try {
                                Date date1 = timeFormat.parse(time1);
                                Date date2 = timeFormat.parse(time2);
                                long sum = date1.getTime() + date2.getTime();

                                toatl_max_duration = timeFormat.format(new Date(sum));

                              for(int i=0;i<=Integer.parseInt(max_ext_hh);i++) {
                                  list_hh.add(String.format("%02d",i));
                              }
                              dataAdapter_hh.notifyDataSetChanged();

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        } else if (max_ext_hh.equalsIgnoreCase("00") && max_ext_min.equalsIgnoreCase("00") && max_ext_sec.equalsIgnoreCase("00")) {
                            Log.d("max_hour_extenstion cond 2",""+mDataset.get(position).getMax_hour_extension().split(":").toString());
                            String time1 = mDataset.get(position).getPacakage_hours();
                            String time2 = "11:00:00";

                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                            try {
                                Date date1 = timeFormat.parse(time1);
                                Date date2 = timeFormat.parse(time2);
                                long sum = date1.getTime() + date2.getTime();

                                toatl_max_duration = timeFormat.format(new Date(sum));

                                for(int i=0;i<=11;i++) {
                                    list_hh.add(String.format("%02d",i));
                                }
                                dataAdapter_hh.notifyDataSetChanged();

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    //===max hour ext is not null but its onsite service so its hour base service so dureation requiored that info given by Jolly QA from US.

                    else if(mDataset.get(position).getMax_hour_extension() == null && mDataset.get(position).getIs_onsite_service().equalsIgnoreCase("1")){

                        String temp_max_ext_duration = "00:00:00";
                        String[] max_hour_extenstion = temp_max_ext_duration.split(":");
                        String max_ext_hh = max_hour_extenstion[0];
                        String max_ext_min = max_hour_extenstion[1];
                        String max_ext_sec = max_hour_extenstion[2];
                        Log.d("max_hour_extenstion cond 1",max_ext_hh+"---"
                                +"---"+max_ext_min
                                +"---"+max_ext_sec);

                        if (max_ext_hh.equalsIgnoreCase("00") && max_ext_min.equalsIgnoreCase("00") && max_ext_sec.equalsIgnoreCase("00")) {
                            String time1 = "12:00:00";
                            String time2 = "11:00:00";

                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                            try {
                                Date date1 = timeFormat.parse(time1);
                                Date date2 = timeFormat.parse(time2);
                                long sum = date1.getTime() + date2.getTime();

                                toatl_max_duration = timeFormat.format(new Date(sum));

                                for(int i=0;i<=11;i++) {
                                    list_hh.add(String.format("%02d",i));
                                }
                                dataAdapter_hh.notifyDataSetChanged();

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    sp_hh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position_sphh, long id) {


                            int hour=position_sphh>0
                                    ? Integer.parseInt(parent.getSelectedItem().toString()):00;

                            int minutes = sp_mm.getSelectedItemPosition()>0
                                    ? Integer.parseInt(sp_mm.getSelectedItem().toString()):00;

                            String seleccted_duration  = Integer.toString(hour)+":"+Integer.toString(minutes)+":00";

                            try {
                                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                                timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                                Date date_seleccted_duration  = timeFormat.parse(seleccted_duration);
                                Date date_toatl_max_duration = timeFormat.parse(toatl_max_duration);
                                Date date_packHours=null;
                                if(mDataset.get(position).getPacakage_hours()==null){
                                    date_packHours = timeFormat.parse("12:00:00");
                                }else{
                                     date_packHours = timeFormat.parse(mDataset.get(position).getPacakage_hours().toString());
                                }
                                if(date_seleccted_duration.after(date_toatl_max_duration)){
                                    Toast.makeText(mContext,"Duration timing large than max extension allowed.",Toast.LENGTH_LONG).show();
                                    sp_hh.setSelection(sp_hh.getSelectedItemPosition()-1);
                                   }else{
                                    if(date_seleccted_duration.after(date_packHours)){
                                        long timeDiff = Math.abs( date_seleccted_duration.getTime() - date_packHours.getTime());
                                        long diff_hh = TimeUnit.MILLISECONDS.toHours(timeDiff);
                                        long diff_mm = TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff));

                                        diff = String.format("%2d : %2d", TimeUnit.MILLISECONDS.toHours(timeDiff),
                                                TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff)));
                                        tv_duration_title.setText("Duration "+" ( extra "+diff+" )");

                                        if(mDataset.get(position).getExtension_duration()!=null){
                                            String ext_time[] = mDataset.get(position).getExtension_duration().split(":");
                                            String hh= ext_time[0];
                                            String mm =ext_time[1];

                                            int HH = Integer.parseInt(hh);
                                            int MM = Integer.parseInt(mm);

                                            int conver_total_per_ext_min =( HH * 60 ) + ( MM );
                                            long total_diff_min = (diff_hh * 60) + diff_mm;

                                            double extra_duration_rate =(double) (total_diff_min * Integer.parseInt(mDataset.get(position).getHour_extension_charges())) / conver_total_per_ext_min;
                                            Long L = Math.round(extra_duration_rate);
                                            int final_extra_rate = Integer.valueOf(L.intValue());
                                            extraDuratoinPrice = (double) final_extra_rate;
                                            Log.d("extra duratin price",""+final_extra_rate);
                                        }

                                    }else{
                                        tv_duration_title.setText("Duration ");
                                        extraDuratoinPrice = 0.0;
                                        //mainPrice = Double.parseDouble(mDataset.get(position).getCharges());
                                    }
                                }
                                Log.d("hour dur fialprice =",""+mainPrice+extraPersonPrice + extraItemPrice + extraDuratoinPrice);
                                //tv_final_price.setText(GLOBAL_FORMATTER.format(mainPrice+extraPersonPrice + extraItemPrice + extraDuratoinPrice));
                                if(deliveryPrice<1){deliveryPrice=0.00;}
                                tv_final_price.setText(GLOBAL_FORMATTER.format(mainPrice+extraPersonPrice + extraItemPrice + extraDuratoinPrice)
                                        +"\n"+GLOBAL_FORMATTER.format(deliveryPrice)
                                        +"\n"+GLOBAL_FORMATTER.format(mainPrice+extraPersonPrice + extraItemPrice + extraDuratoinPrice+deliveryPrice));
                                tv_total_textonly.setText("Price($)\nDelivery($)"
                                        +"\nTotal($)");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    sp_mm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int positionsp_mm, long id) {

                            int hour=sp_hh.getSelectedItemPosition()>0
                                    ? Integer.parseInt(sp_hh.getSelectedItem().toString()):00;
                            int minutes = positionsp_mm>0
                                    ? Integer.parseInt(parent.getSelectedItem().toString()):00;

                            String seleccted_duration  = Integer.toString(hour)+":"+Integer.toString(minutes)+":00";

                            try {
                                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                                timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                                Date date_seleccted_duration  = timeFormat.parse(seleccted_duration);
                                Date date_toatl_max_duration = timeFormat.parse(toatl_max_duration);

                                if(date_seleccted_duration.after(date_toatl_max_duration)){
                                    sp_mm.setSelection(0);
                                    tv_duration_title.setText("Duration "+" ( maximum "+toatl_max_duration+" )");
                                    Toast.makeText(mContext,"Duration timing large than max extension allowed.",Toast.LENGTH_LONG).show();
                                }else{
                                    Date date_packHours=null;
                                    if(mDataset.get(position).getPacakage_hours()==null){
                                         date_packHours = timeFormat.parse("12:00:00");
                                    }else{
                                         date_packHours = timeFormat.parse(mDataset.get(position).getPacakage_hours().toString());
                                    }
                                    if(date_seleccted_duration.after(date_packHours)){

                                        long timeDiff = Math.abs( date_seleccted_duration.getTime() - date_packHours.getTime());
                                        long diff_hh = TimeUnit.MILLISECONDS.toHours(timeDiff);
                                        long diff_mm = TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff));

                                        diff = String.format("%2d : %2d", TimeUnit.MILLISECONDS.toHours(timeDiff),
                                                TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff)));
                                        tv_duration_title.setText("Duration "+" ( extra "+diff+" )");

                                        if(mDataset.get(position).getExtension_duration()!=null){
                                            String ext_time[] = mDataset.get(position).getExtension_duration().split(":");
                                            String hh= ext_time[0];
                                            String mm =ext_time[1];

                                            int HH = Integer.parseInt(hh);
                                            int MM = Integer.parseInt(mm);

                                            int conver_total_per_ext_min =( HH * 60 ) + ( MM );
                                            long total_diff_min = (diff_hh * 60) + diff_mm;

                                            double extra_duration_rate =(double) (total_diff_min * Integer.parseInt(mDataset.get(position).getHour_extension_charges())) / conver_total_per_ext_min;
                                            Long L = Math.round(extra_duration_rate);
                                            int final_extra_rate = Integer.valueOf(L.intValue());
                                            extraDuratoinPrice = (double) final_extra_rate;
                                            Log.d("extra duratin price",""+final_extra_rate);
                                        }

                                    }else{
                                        tv_duration_title.setText("Duration");
                                        extraDuratoinPrice = 0.0;
                                        //mainPrice = Double.parseDouble(mDataset.get(position).getCharges());
                                    }
                                }
                                Log.d("min dur fialprice =",""+mainPrice+extraPersonPrice + extraItemPrice + extraDuratoinPrice);
                                //tv_final_price.setText(GLOBAL_FORMATTER.format(mainPrice+extraPersonPrice + extraItemPrice + extraDuratoinPrice));
                                if(deliveryPrice<1){deliveryPrice=0.00;}
                                tv_final_price.setText(GLOBAL_FORMATTER.format(mainPrice+extraPersonPrice + extraItemPrice + extraDuratoinPrice)
                                        +"\n"+GLOBAL_FORMATTER.format(deliveryPrice)
                                        +"\n"+GLOBAL_FORMATTER.format(mainPrice+extraPersonPrice + extraItemPrice + extraDuratoinPrice+deliveryPrice));
                                tv_total_textonly.setText("Price($)\nDelivery($)"
                                        +"\nTotal($)");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }else{
                    ll_duration.setVisibility(View.GONE);
                    tv_duration_title.setVisibility(View.GONE);

                    sp_hh.setVisibility(View.GONE);
                    sp_mm.setVisibility(View.GONE);
                }

                rb_delivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(rb_delivery.isChecked()){
                            til_address .setHint("Delivery Address");

                            et_address_line.setText("");
                            et_address_line.setEnabled(true);
                            tv_delivery_freecharge.setVisibility(View.VISIBLE);
                        }
                    }
                });

                rb_pickup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(rb_pickup.isChecked()){
                            til_address.setHint("Pickup Address");

                            et_address_line.setText(providerAddress);
                            et_address_line.setEnabled(false);
                            tv_delivery_freecharge.setVisibility(View.GONE);
                            deliveryPrice=0.00;

                        }else{

                            tv_delivery_freecharge.setVisibility(View.VISIBLE);
                            et_address_line.setText("");
                            et_address_line.setEnabled(true);
                        }

                        Log.e("main price",""+mainPrice);
                        if(deliveryPrice<1){deliveryPrice=0.00;}
                        tv_final_price.setText(GLOBAL_FORMATTER.format(mainPrice+extraPersonPrice + extraItemPrice + extraDuratoinPrice)
                               +"\n"+GLOBAL_FORMATTER.format(deliveryPrice)
                                +"\n"+GLOBAL_FORMATTER.format(mainPrice+extraPersonPrice + extraItemPrice + extraDuratoinPrice+deliveryPrice));
                        tv_total_textonly.setText("Price($)\nDelivery($)"
                                +"\nTotal($)");
                    }
                });


                rb_onsite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(rb_onsite.isChecked()){
                            til_address.setHint("Address");

                            et_address_line.setText("");
                            et_address_line.setEnabled(true);
                            tv_delivery_freecharge.setVisibility(View.GONE);
                        }
                    }
                });

                et_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        // Get Current Date
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR);
                        int mMonth = c.get(Calendar.MONTH);
                        int mDay = c.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        String str_day ,str_month;
                                        if(dayOfMonth < 10){
                                            str_day = "0"+dayOfMonth;
                                        }else {
                                            str_day = Integer.toString(dayOfMonth);
                                        }
                                        if(monthOfYear + 1 < 10){
                                            str_month = "0"+Integer.toString(monthOfYear + 1);
                                        }else {
                                            str_month  =Integer.toString(monthOfYear + 1);
                                        }
                                        et_date.setError(null);
                                        et_date.setText(str_month + "-" + str_day+ "-" + year );
                                        str_date = Utils.convertDateStringToString(et_date.getText().toString(),"MM-dd-yyyy","yyyy-MM-dd");

                                        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                                        Date date = new Date(year, monthOfYear, dayOfMonth-1);
                                        String selected_dayOfWeek = simpledateformat.format(date);

                                        Log.e("SElected day--",selected_dayOfWeek);
                                        String openTime = "";
                                        String closeTime = "";

                                        if(open_hours.getAsJsonArray().size()>0){
                                            for(int a=0;a<open_hours.size();a++){
                                                String got_Day = open_hours.get(a).getAsJsonObject().get("week_day").getAsString();
                                                Log.e("got day---",got_Day);
                                                if(got_Day.equalsIgnoreCase(selected_dayOfWeek)){
                                                    openTime =open_hours.get(a).getAsJsonObject().get("open_time").getAsString();
                                                    closeTime =open_hours.get(a).getAsJsonObject().get("close_time").getAsString();
                                                    Log.e("openClose time---",""+ openTime + closeTime);
                                                }
                                            }
                                            if(openTime.length()<1 || closeTime.length()<1){
                                                et_date.setText("");
                                                et_delivery_time.setText("");
                                                tv_error_datetime.setVisibility(View.VISIBLE);
                                                tv_error_datetime.setText(" ! No time slots available. Please select a different date.");
                                                if(timeslotlist.size()>0) {
                                                    timeslotlist.clear() ;
                                                }
                                            }else{
                                                tv_error_datetime.setVisibility(View.GONE);
                                                String timeValue = openTime;
                                                SimpleDateFormat sdf= new SimpleDateFormat("HH:mm:ss");
                                                try {
                                                    Calendar startCalendar = Calendar.getInstance();
                                                    startCalendar.setTime(sdf.parse(timeValue));

                                                if (startCalendar.get(Calendar.MINUTE) < 15) {
                                                    startCalendar.set(Calendar.MINUTE, 15);
                                                } else {
                                                    startCalendar.add(Calendar.MINUTE, 15); // overstep hour and clear minutes
                                                    startCalendar.clear(Calendar.MINUTE);
                                                }

                                                    Calendar endCalendar = Calendar.getInstance();
                                                    endCalendar.setTime(sdf.parse(closeTime));

                                                    // if you want dates for whole next day, uncomment next line
                                                    //endCalendar.add(Calendar.DAY_OF_YEAR, 1);
                                                  /*  endCalendar.add(Calendar.HOUR_OF_DAY, 24 - startCalendar.get(Calendar.HOUR_OF_DAY));

                                                    endCalendar.clear(Calendar.MINUTE);
                                                    endCalendar.clear(Calendar.SECOND);
                                                    endCalendar.clear(Calendar.MILLISECOND);*/

                                                    SimpleDateFormat slotTime = new SimpleDateFormat("HH:mm");
                                                    SimpleDateFormat slotDate = new SimpleDateFormat(", dd/MM/yy");
                                                    while (endCalendar.after(startCalendar)) {
                                                        String slotStartTime = slotTime.format(startCalendar.getTime());
                                                        String slotStartDate = slotDate.format(startCalendar.getTime());

                                                        startCalendar.add(Calendar.MINUTE, 15);
                                                        String slotEndTime = slotTime.format(startCalendar.getTime());

                                                        Log.d("DATE", slotStartTime + " - " + slotEndTime + slotStartDate);
                                                        timeslotlist.add(Utils.convertDateStringToString(slotStartTime,"HH:mm","hh:mm aa"));
                                                    }

                                                } catch (ParseException e) {
                                                    // date in wrong format
                                                }

                                            }
                                        }
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis() +(1 * 1000 * 24 * 3600));
                        datePickerDialog.show();
                    }
                });

                et_delivery_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Launch Time Picker Dialog
                      /*  TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                        *//*int hour = hourOfDay;
                                        int minutes = minute;
                                        String timeSet = "";
                                        if (hour > 12) {
                                            hour -= 12;
                                            timeSet = "PM";
                                        } else if (hour == 0) {
                                            hour += 12;
                                            timeSet = "AM";
                                        } else if (hour == 12)
                                            timeSet = "PM";
                                        else
                                            timeSet = "AM";
                                        String min = "";
                                        if (minutes < 10)
                                            min = "0" + minutes;
                                        else
                                            min = String.valueOf(minutes);
                                        // Append in a StringBuilder
                                        String aTime = new StringBuilder().append(hour).append(':')
                                                .append(min).append(" ").append(timeSet).toString();*//*
                                et_delivery_time.setText(String.format("%02d:%02d", hourOfDay, minute));
                                str_delivery_time = et_delivery_time.getText().toString();
                            }
                        }, mHour, mMinute, true);
                        timePickerDialog.show();*/


                        if(timeslotlist.size()>0){
                            final String items[]=timeslotlist.toArray(new String[timeslotlist.size()]);
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("Available Time Slot.");
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                 //   Toast.makeText(mContext.getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                                    et_delivery_time.setText(items[item]);
                                    et_delivery_time.setError(null);
                                    str_delivery_time = Utils.convertDateStringToString(et_delivery_time.getText().toString(),"hh:mm aa","HH:mm:ss");
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                });




                tv_final_addtocart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (et_date.getText().toString().length() < 1) {
                            et_date.setError("Enter Date !");
                        } else if (et_delivery_time.getText().toString().length() < 1) {
                            et_delivery_time.setError("Enter Time !");
                        }else if(et_address_line.getText().toString().length()<1) {
                            et_address_line.setError("Enter Address !");
                        }else if (et_guestCount.getText().toString().length() < 1) {
                            et_guestCount.setError("Enter Guest Numbers !");
                        } else if (rb_delivery.isChecked()==false && rb_pickup.isChecked()==false && rb_onsite.isChecked()==false) {
                            Toast.makeText(mContext, "Select Delivery / Pickup /On Site ?", Toast.LENGTH_LONG).show();
                        }else if(sp_hh.getVisibility()==View.VISIBLE && sp_hh.getSelectedItemPosition()<=1){
                            ((TextView)sp_hh.getSelectedView()).setError("Select duration hour.");
                        }else if(sp_mm.getVisibility()==View.VISIBLE && sp_mm.getSelectedItemPosition()<1){
                            ((TextView)sp_mm.getSelectedView()).setError("Select duration minutes.");
                        }
                        else {
                            //str_et_message = et_message.getText().toString();
                            //str_et_specialmessage = et_special_message.getText().toString();
                            str_et_guestCount = et_guestCount.getText().toString();
                            str_address1 = et_address_line.getText().toString();

                            int selectedId = radioTypeGroup.getCheckedRadioButtonId();
                            RadioButton radioTypeButton = (RadioButton)dialog.findViewById(selectedId);


                            if( mDataset.get(position).getIs_onsite_service().equalsIgnoreCase("1")){
                                str_send_type ="On Site Service";
                            }else{
                                str_send_type = radioTypeButton.getText().toString();
                            }

                            str_charge_id = mDataset.get(position).getId();

                            try {
                                //assumes your server already has English as locale
                                NumberFormat nf = NumberFormat.getInstance();

                                service_provider_item_obj= new JSONObject();
                                service_provider_item_obj.put("service_title",mDataset.get(position).getService_title());
                                if(ll_itemcount.getVisibility()==View.VISIBLE){
                                    service_provider_item_obj.put("quantity",np_items.getValue());
                                }else{
                                    service_provider_item_obj.put("quantity","0");
                                }
                                service_provider_item_obj.put("number_of_guests",et_guestCount.getText().toString());
                                service_provider_item_obj.put("extra_guests",Integer.toString(extra_person));
                                if(ll_duration.getVisibility()==View.VISIBLE){
                                    if(diff!=null && !diff.equalsIgnoreCase("")){
                                        String[] ext__dif_dur_time = diff.split(":");
                                        String ext_dif_dur_hh=ext__dif_dur_time[0];
                                        String ext_dif_dur_min=ext__dif_dur_time[1];
                                        //String ext_dif_dur_sec=ext__dif_dur_time[2];
                                        int toatl_extra_min = (nf.parse(ext_dif_dur_hh).intValue() *60) + nf.parse(ext_dif_dur_min).intValue();
                                        service_provider_item_obj.put("extra_minutes",toatl_extra_min);
                                        Log.d("Extra mmins=",""+toatl_extra_min);
                                    }else{
                                        service_provider_item_obj.put("extra_minutes","00");
                                    }
                                }else{
                                    service_provider_item_obj.put("extra_minutes","00");
                                }


                                //...
                                //double total_final_amount = nf.parse(tv_final_price.getText().toString()).doubleValue();
                                if(deliveryPrice<1){deliveryPrice=0.00;}
                                double total_final_amount = nf.parse(GLOBAL_FORMATTER.format(mainPrice+extraPersonPrice + extraItemPrice + extraDuratoinPrice+deliveryPrice)).doubleValue();
                                service_provider_item_obj.put("total_amount",total_final_amount);

                                service_provider_item_obj.put("booking_date",str_date);
                                service_provider_item_obj.put("booking_time",str_date+" "+str_delivery_time);
                                service_provider_item_obj.put("service_hours",mDataset.get(position).getPacakage_hours());

                                service_provider_item_obj.put("item_charges",mDataset.get(position).getCharges());






                                if(rb_delivery.isChecked()==true){
                                    service_provider_item_obj.put("is_delivery","1");
                                    service_provider_item_obj.put("is_pickup","0");
                                    service_provider_item_obj.put("is_onsite_service","0");
                                }else if(rb_pickup.isChecked()==true){
                                    service_provider_item_obj.put("is_delivery","0");
                                    service_provider_item_obj.put("is_pickup","1");
                                    service_provider_item_obj.put("is_onsite_service","0");
                                }else if(rb_onsite.isChecked()==true){
                                    service_provider_item_obj.put("is_delivery","0");
                                    service_provider_item_obj.put("is_pickup","0");
                                    service_provider_item_obj.put("is_onsite_service","1");
                                }else{
                                    Toast.makeText(mContext,"Please Select Delivery/Pickup/OnSite",Toast.LENGTH_SHORT).show();
                                }
                                service_provider_item_obj.put("is_delivery_na",mDataset.get(position).getIs_delivery_na());
                                service_provider_item_obj.put("delivery_address",et_address_line.getText().toString());

                                if(mDataset.get(position).getIs_delivery_paid().equalsIgnoreCase("1")){
                                    service_provider_item_obj.put("is_delivery_paid",mDataset.get(position).getIs_delivery_paid());
                                    if(deliveryPrice > 0){
                                        service_provider_item_obj.put("delivery_charges",GLOBAL_FORMATTER.format(deliveryPrice));
                                    }else{
                                        service_provider_item_obj.put("delivery_charges","0.00");
                                    }
                                }else{
                                    service_provider_item_obj.put("is_delivery_paid","0");
                                    service_provider_item_obj.put("delivery_charges","0");
                                }

                                //Log.d("josn array",cart_beverages_array.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(IsGuestEntered==true){
                                new AddCartChargesServiceProviders().execute();
                            }else if(et_address_line.getText().length()<1){
                                et_address_line.setError("Enter Address ");
                            }else{
                                Toast.makeText(mContext,"Guest coundition not matched.",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });


                et_address_line.setAdapter(new GooglePlacesAutocompleteAdapter(mContext , R.layout.text_row_list_item));
                et_address_line.setThreshold(1);
                et_address_line.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        str_place_id =placeID.get(position).toString();
                        // Log.d("Log place clicked===",str_place_id);
                        new GetPlaceDteailsViaGooglePlace().execute();
                        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et_address_line.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    }
                });


                dialog.show();
                if(dialog.isShowing()!=true){
                    mainPrice=0.0;extraPersonPrice=0.0;extraItemPrice=0.0;extraDuratoinPrice=0.0;
                }
            }
        });


        holder.tv_addtocart.setVisibility(View.GONE);
        holder.tv_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    String res;
    private class AddCartChargesServiceProviders extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

       /* protected ProgressDialog progressDialog;*/
        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //Log.d("pre execute", "Pre execute Done");

            //inicia diálogo de progress, mostranto processamento com servidor.
           // progressDialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true, false);
        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");
            //Log.d("post execute", "Executando doInBackground   ingredients");
            try {

                JSONObject req = new JSONObject();
                if(sharepref.getString(Const.PREF_USER_ID,"").equalsIgnoreCase("0")
                        || sharepref.getString(Const.PREF_USER_ID,"")==null){
                    req.put("user_id",sharepref.getString(Const.PREF_USER_ID,"0"));
                    req.put("token",sharepref.getString(Const.PREF_USER_TOKEN,"0"));
                }else{
                    req.put("user_id",sharepref.getString(Const.PREF_USER_ID,"0"));
                }
                req.put("service_provider_id",str_service_provider_id);
                req.put("service_id",str_service_id);
                req.put("charge_id",str_charge_id);
                req.put("flag","2");
                req.put("service_charge_details",service_provider_item_obj);

                Log.d("REq Json======", req.toString());

                String response = post(Const.SERVER_URL_API +"/cart_service_provider", req.toString(),"put");//post("http://54.153.127.215/api/login", req.toString());
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
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            super.onPostExecute(result);
            }

            //progressDialog.dismiss();

            if (res == null || res.equals("")) {

                //progressDialog.dismiss();
                Toast.makeText(mContext, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                Log.i("RES--servPro default", res);
                JSONObject obj = new JSONObject(res);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.

                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    /*JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(res).getAsJsonObject();

                    String cart_id = rootObj.getAsJsonObject("message").get("cart_id").getAsString();
                    String flag = rootObj.getAsJsonObject("message").get("flag").getAsString();
                    str_cart_id = cart_id;
                    venue_cart_flag =flag;*/

                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                    mainPrice=0.0;extraPersonPrice=0.0;extraItemPrice=0.0;extraDuratoinPrice=0.0;

                    new GetGenCartDetails().execute();
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
                    int previous_cart_item_count = Integer.parseInt(sharepref.getString(Const.PREF_USER_CART_TOTAL_ITEMS,""));
                    int new_cart_item_count = previous_cart_item_count+ 1;
                    sharepref.edit().putString(Const.PREF_USER_CART_TOTAL_ITEMS,Integer.toString(new_cart_item_count)).apply();

                    if (Build.VERSION.SDK_INT >= 11)
                    {
                        ServiceproviderGenericPricingFragment.VersionHelper.refreshActionBarMenu((Activity)mContext);
                    }

                } else{
                    mainPrice=0.0;extraPersonPrice=0.0;extraItemPrice=0.0;extraDuratoinPrice=0.0;
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }

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

    String res_google_place;
    class GetPlaceDteailsViaGooglePlace extends AsyncTask<Object, Void, String> {

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

                String response = APICall.post("https://maps.googleapis.com/maps/api/place/details/json?placeid="+str_place_id+"&key=AIzaSyDtr3qaj_rlFGjLyPW4OClB7r7oO6S5jj4", "","get");
                //Log.d("URL====","https://maps.googleapis.com/maps/api/place/details/json?placeid="+str_place_id+"&key=AIzaSyDtr3qaj_rlFGjLyPW4OClB7r7oO6S5jj4");
                //https://maps.googleapis.com/maps/api/place/details/json?placeid="+str_place_id+"&key=AIzaSyDtr3qaj_rlFGjLyPW4OClB7r7oO6S5jj4
                res_google_place=response;
                //Log.d("RESPONCE PLACE====",response);
            }/*catch (JSONException e) {
                e.printStackTrace();
            }*/ catch (IOException e) {
                e.printStackTrace();
            }


            return res_google_place;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try{
                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(res_google_place).getAsJsonObject();

                String street_number="",route="",city="",state="",postalcode="";
                JsonArray address_components = rootObj.getAsJsonObject("result").getAsJsonArray("address_components");
                for(int a=0;a<address_components.size();a++){

                    Gson gson = new Gson();
                    JsonArray types_array = address_components.get(a).getAsJsonObject().getAsJsonArray("types");

                    //Log.d("address type array",types_array.toString());

                    Type type = new TypeToken<List<String>>(){}.getType();
                    List<String> typeList = gson.fromJson(types_array.toString(), type);

                    if(typeList.get(0).equalsIgnoreCase("street_number")){
                        street_number=address_components.get(a).getAsJsonObject().get("long_name").getAsString();
                    }
                    if(typeList.get(0).equalsIgnoreCase("route")){
                        route=address_components.get(a).getAsJsonObject().get("long_name").getAsString();
                    }
                    if(typeList.get(0).equalsIgnoreCase("locality")){
                        city = address_components.get(a).getAsJsonObject().get("long_name").getAsString();
                    }
                    if(typeList.get(0).equalsIgnoreCase("administrative_area_level_1")){
                        state = address_components.get(a).getAsJsonObject().get("long_name").getAsString() ;
                    }
                    if(typeList.get(0).equalsIgnoreCase("postal_code")){
                        postalcode = address_components.get(a).getAsJsonObject().get("long_name").getAsString();
                    }
                }

                et_address_line.setText(street_number+" "+route+","+city+","+state+","+postalcode);
                et_address_line.clearFocus();

                //===California VALIDATION , ONLLY California CITY PLACE IS ALLOWED==========
                if(!state.equalsIgnoreCase("California")){
                    if(!state.equalsIgnoreCase("CA")) {
                        et_address_line.setText("");
                        Toast.makeText(mContext, "sorry! now we are serving only in california .", Toast.LENGTH_LONG).show();
                    }else{
                        str_deliveryAddress = et_address_line.getText().toString();
                        new GetDeliveryRate().execute();
                    }
                }else{
                    str_deliveryAddress = et_address_line.getText().toString();
                    new GetDeliveryRate().execute();
                }


                /*String adr_address = rootObj.getAsJsonObject("result").get("adr_address").getAsString();
                adr_address=Html.fromHtml(adr_address)+"";
                Log.d("got address is ==",adr_address);
                String[] addre = adr_address.split(",");
                for(int j=0;j<addre.length;j++){
                    if(j==0){et_address_line.setText(addre[0].toString());                    }
                    if(j==1){et_city.setText(addre[1].toString());}
                    //if(j==2){et_state.setText(addre[2].toString());}
                    if(j==3){et_zipcode.setText(addre[2].toString().substring(addre[2].toString().length()-6));}
                }*/
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    String res_delRate;
    class GetDeliveryRate extends AsyncTask<Object, Void, String> {

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
                String response;
                response = post(Const.SERVER_URL_API +"ovenues_delivery_charges?from_address="+providerAddress+"&to_address="+str_deliveryAddress, "","get");
                Log.e("GET RaTE----",Const.SERVER_URL_API +"ovenues_delivery_charges?from_address="+providerAddress+"&to_address="+str_deliveryAddress+"\n"+response);
                res_delRate=response;
            }/*catch (JSONException e) {
                e.printStackTrace();
            }*/ catch (Exception e) {
                e.printStackTrace();
            }

            return res_delRate;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

           // {"status":"success","message":{"rounded_distance":"6.53","delivery_charges":"15.00"},"http_response":200}
            try{
                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(res_delRate).getAsJsonObject();
                if(rootObj.get("status").getAsString().equalsIgnoreCase("Success")) {
                    if (!rootObj.get("message").getAsJsonObject().get("delivery_charges").isJsonNull()) {
                        deliveryPrice = Double.parseDouble(rootObj.get("message").getAsJsonObject().get("delivery_charges").getAsString());
                    }
                }else{
                    Toast.makeText(mContext,rootObj.get("message").getAsString(),Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if(deliveryPrice<1){deliveryPrice=0.00;}
            tv_final_price.setText(GLOBAL_FORMATTER.format(mainPrice+extraPersonPrice + extraItemPrice + extraDuratoinPrice)
                   +"\n"+GLOBAL_FORMATTER.format(deliveryPrice)
                   +"\n" +GLOBAL_FORMATTER.format(mainPrice+extraPersonPrice + extraItemPrice + extraDuratoinPrice+deliveryPrice));
            tv_total_textonly.setText("Price($)\nDelivery($)"
                    +"\nTotal($)");
        }
    }

    String res_cart;
    class GetGenCartDetails extends AsyncTask<Object, Void, String> {

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
                String response;
                if(sharepref.getString(Const.PREF_USER_ID,"").equalsIgnoreCase("0")){
                    response = post(Const.SERVER_URL_API +"cart_total_items/user_id/"+sharepref.getString(Const.PREF_USER_ID,"")+"/token/"+sharepref.getString(Const.PREF_USER_TOKEN,""), "","get");
                }else{
                    response = post(Const.SERVER_URL_API +"cart_total_items/user_id/"+sharepref.getString(Const.PREF_USER_ID,""), "","get");
                }
                //Log.d("REsponce Json====",response);
                res_cart=response;
            }/*catch (JSONException e) {
                e.printStackTrace();
            }*/ catch (IOException e) {
                e.printStackTrace();
            }


            return res_cart;

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String response_string = "";

//            Log.i("RESPONSE GEN Cart", res_cart);
            try{
                JSONObject obj = new JSONObject(res_cart);

                response_string=obj.getString("status");

                if(response_string.equals("success")) {


                    activitySP.invalidateOptionsMenu();

                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(res_cart).getAsJsonObject();

                    JsonObject cart_items = rootObj.getAsJsonObject("message").get("cart_items").getAsJsonObject();

                    String venues = cart_items.getAsJsonObject().get("venues").getAsString();
                    String services = cart_items.getAsJsonObject().get("services").getAsString();
                    String catering_services = cart_items.getAsJsonObject().get("catering_services").getAsString();
                    String total_amount = cart_items.getAsJsonObject().get("total_amount").getAsString();

                    int total_item = Integer.parseInt(venues) + Integer.parseInt(services) + Integer.parseInt(catering_services);

                    sharepref.edit().putString(Const.PREF_USER_CART_TOTAL_ITEMS, Integer.toString(total_item)).apply();
                    sharepref.edit().putString(Const.PREF_USER_CART_TOTAL_AMOUNT, total_amount).apply();

                    sharepref.edit().putString(Const.PREF_USER_CART_VENUES, venues).apply();
                    sharepref.edit().putString(Const.PREF_USER_CART_CATERINGS, catering_services).apply();
                    sharepref.edit().putString(Const.PREF_USER_CART_SERVICES, services).apply();
                  /*  mContext.startActivity(new Intent(mContext,ServiceProviderChargesDefault.class)
                            .putExtra("service_provider_id",str_service_provider_id));
                    ((Activity)mContext).finish();*/

                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}