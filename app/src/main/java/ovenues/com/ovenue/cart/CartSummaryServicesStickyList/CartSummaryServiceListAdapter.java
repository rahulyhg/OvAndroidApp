package ovenues.com.ovenue.cart.CartSummaryServicesStickyList;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brandongogetap.stickyheaders.exposed.StickyHeader;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.cart.CartSummaryScreen;
import ovenues.com.ovenue.modelpojo.Cart.CartSummaryServiceListModel;
import ovenues.com.ovenue.utils.Const;

import static android.content.Context.MODE_PRIVATE;
import static ovenues.com.ovenue.utils.APICall.post;
import static ovenues.com.ovenue.utils.Utils.convertDateStringToString;

public class CartSummaryServiceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderHandler {

    private static final int HEADER_TYPE = 0;
    private static final int BODY_TYPE = 1;
    private final ArrayList<CartSummaryServiceListModel> data;
    Context mContext;
    double total_venue =0;
    SharedPreferences sharepref;
    String delete_str_cart_id,venuTypeId,restaurant_menu_id,catering_menu_id,beverage_id ,service_option_id,extra_service_option_id;
    Dialog dialog;

    public CartSummaryServiceListAdapter(Context context, ArrayList<CartSummaryServiceListModel> data) {
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart_summary_services, parent, false);
                return new BodyViewViewHolder(view);
        }
        return null;
    }

    @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        holder.setIsRecyclable(false);
        if (data != null) {
            switch (data.get(position).getmUiType()) {
                case HEADER_TYPE:
                    ((HeaderViewViewHolder) holder).row_text_view_only.setText(data.get(position).getProvider_name());
                    ((HeaderViewViewHolder) holder).row_text_view_only.setTextColor(Color.WHITE);
                    ((HeaderViewViewHolder) holder).row_text_view_only.setGravity(Gravity.LEFT);



                    ((HeaderViewViewHolder) holder).row_text_view_only_2.setText("$ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(data.get(position).getDelivery_address())));
                    ((HeaderViewViewHolder) holder).row_text_view_only_2.setTextColor(Color.WHITE);
                    ((HeaderViewViewHolder) holder).row_text_view_only_2.setGravity(Gravity.RIGHT);
                    // ((HeaderViewViewHolder) holder).row_text_view_only.setText(data.get(position).getName());
                    break;
                case BODY_TYPE:

                    String guest_count  = data.get(position).getNumber_of_guests();
                    ((BodyViewViewHolder) holder).tv_title.setText(data.get(position).getProvider_name());
                    ((BodyViewViewHolder) holder).tv_title2.setText(data.get(position).getService_title());
                    ((BodyViewViewHolder) holder).tv_price.setText("$ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(data.get(position).getTotal_amount())));

                    String ext_min="";
                    if(data.get(position).getExtra_minutes()!=null){
                        ext_min=" ( Ext.Min : "+data.get(position).getExtra_minutes()+" )";
                    }
                 ((BodyViewViewHolder) holder).tv_time.setText(convertDateStringToString(data.get(position).getBooking_time(),"yyyy-MM-dd HH:mm:ss","MM-dd-yyyy hh:mm aa")
                         +ext_min);

                    String ext_guest="";
                    if(data.get(position).getExtra_guests()!=null){
                        ext_guest=" ( Ext.Guest : "+data.get(position).getExtra_guests()+" )";
                    }
                 ((BodyViewViewHolder) holder).tv_guestcount.setText(data.get(position).getNumber_of_guests()+ext_guest);
                 //((BodyViewViewHolder) holder).tv_price.setText("$ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(data.get(position).getTotal_amount())));
                 ((BodyViewViewHolder) holder).tv_extracharge.setVisibility(View.GONE);

                    String type ="";
                    if(data.get(position).getIs_delivery().equalsIgnoreCase("1")){
                        type="Delivery";
                    }else if(data.get(position).getIs_onsite_service().equalsIgnoreCase("1")){
                        type="On Site";
                    }else if(data.get(position).getIs_pickup().equalsIgnoreCase("1")){
                        type="Pickup";
                    }
                 ((BodyViewViewHolder) holder).tv_address.setText(Html.fromHtml(data.get(position).getDelivery_address()+"<font color='#F8A058'>  ( "+type+" )</font>."), TextView.BufferType.SPANNABLE);
                    if(data.get(position).getNote()!=null){
                        ((BodyViewViewHolder) holder).tv_description.setText("Note : "+data.get(position).getNote());
                    }else{
                        ((BodyViewViewHolder) holder).tv_description.setVisibility(View.GONE);
                    }

                   /* ((BodyViewViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog = new Dialog(mContext);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.popup_dialog_service_cart_sumaary);

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
                            lp.gravity = Gravity.CENTER;
                            lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                            lp.dimAmount = 0.81f;
                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN );
                            dialog.getWindow().setAttributes(lp);

                            final TextView tv_providerName,tv_title,tv_time,tv_guestcount ,tv_price,tv_extracharge ,tv_address,tv_description,tv_remove;


                            tv_providerName = (TextView) dialog.findViewById(R.id.tv_providerName);
                            tv_title = (TextView) dialog.findViewById(R.id.tv_title);
                            tv_time = (TextView) dialog.findViewById(R.id.tv_time);
                            tv_guestcount = (TextView) dialog.findViewById(R.id.tv_guestcount);
                            tv_price= (TextView) dialog.findViewById(R.id.tv_price);
                            tv_extracharge= (TextView) dialog.findViewById(R.id.tv_extracharge);
                            tv_address = (TextView) dialog.findViewById(R.id.tv_address);
                            tv_description = (TextView) dialog.findViewById(R.id.tv_description);
                            //tv_remove = (TextView) dialog.findViewById(R.id.tv_remove);

                            tv_providerName.setText(data.get(position).getProvider_name());
                            tv_title.setText(data.get(position).getService_title());
                            tv_time.setText(convertDateStringToString(data.get(position).getBooking_time(),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd hh:mm aa"));
                            tv_guestcount.setText(data.get(position).getNumber_of_guests());
                            tv_price.setText("$ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(data.get(position).getTotal_amount())));
                            tv_extracharge.setText("Ext.Guest : "+data.get(position).getExtra_guests()+"\nExt.Min : "+data.get(position).getExtra_minutes());
                            //tv_address.setText(data.get(position).getProvider_name());
                            tv_description.setText("Note : "+data.get(position).getNote());
                            //tv_remove.setText(data.get(position).getProvider_name());



                            dialog.show();

                        }
                    });*/

                    ((BodyViewViewHolder) holder).btn_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertdialog("Are you sure to delete service?");
                            delete_str_cart_id=data.get(position).getCart_id();
                            venuTypeId = "5";
                        }
                    });



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

        TextView tv_title,tv_title2,tv_price,tv_details ,btn_remove;
        LinearLayout ll_itemcount;
        TextView tv_time,tv_guestcount ,tv_extracharge ,tv_address,tv_description;



        BodyViewViewHolder(View itemView) {
            super(itemView);

            tv_title= (TextView) itemView.findViewById(R.id.tv_title);
            tv_title2= (TextView) itemView.findViewById(R.id.tv_title2);
            tv_details = (TextView) itemView.findViewById(R.id.tv_details);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            btn_remove = (TextView) itemView.findViewById(R.id.btn_remove);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_guestcount = (TextView) itemView.findViewById(R.id.tv_guestcount);
            tv_price= (TextView) itemView.findViewById(R.id.tv_price);
            tv_extracharge= (TextView) itemView.findViewById(R.id.tv_extracharge);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);

            ll_itemcount = (LinearLayout)itemView.findViewById(R.id.ll_itemcount);

        }
    }


    public void alertdialog(String message){
        final AlertDialog.Builder alertbox = new AlertDialog.Builder(mContext);
        alertbox.setMessage(message);
        alertbox.setTitle("Delete ?.");
        alertbox.setIcon(R.mipmap.ic_launcher);

        alertbox.setNeutralButton("YES",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0,int arg1) {

                            new RemoveItem().execute();

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
//for extra service - extra_service_id,extra_service_option_id,cart_id                                                                                                                                                                                "



                /* mVenueOrdCatType
                    1=pricingplan;2=food;3=catering;4=beverage;5=service;6=extra_service*/
               /*id types =
               * id1=menu_id; id2 = course_id;  id3=item_id(Generic ID);*/


                if(venuTypeId.equalsIgnoreCase("1")){
                    String response = post(Const.SERVER_URL_API +"remove_cart?cart_id="+delete_str_cart_id+"&full_delete=1", req.toString(),"get");
                   // Log.d("delete_str_cart_id====",Const.SERVER_URL_API+"cart_detail/cart_id/"+delete_str_cart_id+"/full_delete/1");
                    res = response;
                }else if(venuTypeId.equalsIgnoreCase("2")){
                    String response = post(Const.SERVER_URL_API +"remove_cart?cart_id="+delete_str_cart_id+"&restaurant_menu_id="+restaurant_menu_id, req.toString(),"get");
                    // Log.d("delete_str_cart_id====",Const.SERVER_URL_API+"cart_detail/cart_id/"+delete_str_cart_id+"/full_delete/1");
                    res = response;
                }else if(venuTypeId.equalsIgnoreCase("3")){
                    String response = post(Const.SERVER_URL_API +"remove_cart?cart_id="+delete_str_cart_id+"&catering_menu_id="+catering_menu_id, req.toString(),"get");
                    // Log.d("delete_str_cart_id====",Const.SERVER_URL_API+"cart_detail/cart_id/"+delete_str_cart_id+"/full_delete/1");
                    res = response;
                }else if(venuTypeId.equalsIgnoreCase("4")){
                    String response = post(Const.SERVER_URL_API +"remove_cart?cart_id="+delete_str_cart_id+"&beverage_id="+beverage_id, req.toString(),"get");
                    // Log.d("delete_str_cart_id====",Const.SERVER_URL_API+"cart_detail/cart_id/"+delete_str_cart_id+"/full_delete/1");
                    res = response;
                }else if(venuTypeId.equalsIgnoreCase("5")){
                    String response = post(Const.SERVER_URL_API +"remove_cart?cart_id="+delete_str_cart_id+"&full_delete=1", req.toString(),"get");
                    // Log.d("delete_str_cart_id====",Const.SERVER_URL_API+"cart_detail/cart_id/"+delete_str_cart_id+"/full_delete/1");
                    res = response;
                }else if(venuTypeId.equalsIgnoreCase("6")){
                    String response = post(Const.SERVER_URL_API +"remove_cart?cart_id="+delete_str_cart_id+"&extra_service_id="+extra_service_option_id, req.toString(),"get");
                    // Log.d("delete_str_cart_id====",Const.SERVER_URL_API+"cart_detail/cart_id/"+delete_str_cart_id+"/full_delete/1");
                    res = response;
                }
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

                    mContext.startActivity(new Intent(mContext, CartSummaryScreen.class));
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
