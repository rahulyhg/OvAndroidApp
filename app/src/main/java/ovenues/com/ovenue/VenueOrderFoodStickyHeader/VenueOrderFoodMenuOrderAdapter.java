package ovenues.com.ovenue.VenueOrderFoodStickyHeader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
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
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.modelpojo.VenueOrderModel.VenueOrderFoodMenuModel;
import ovenues.com.ovenue.utils.Const;

import static android.content.Context.MODE_PRIVATE;
import static ovenues.com.ovenue.VenueDetailsMainFragment.str_cart_id;
import static ovenues.com.ovenue.VenueDetailsMainFragment.str_price_pkg_id;
import static ovenues.com.ovenue.fragments.VenuesFragments.VenueRestaurantMenu.tv_venue_food_menutitle;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.et_date;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.et_guestCount;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.str_timeslot;
import static ovenues.com.ovenue.utils.APICall.post;

public class VenueOrderFoodMenuOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements StickyHeaderHandler {

    String str_menu_id,str_menu_desc,str_item_id ,str_item_name ,str_price_per_plate,str_total_item_price;
    int int_Guest_count;
    public static final int HEADER_TYPE = 0;
    public static final int BODY_TYPE = 1;
    private final ArrayList<VenueOrderFoodMenuModel> data;
    Context mContext;
    SharedPreferences sharepref;

    public VenueOrderFoodMenuOrderAdapter(Context context, ArrayList<VenueOrderFoodMenuModel> data) {
        this.data = data;
        this.mContext=context;
        sharepref = mContext.getSharedPreferences("MyPref", MODE_PRIVATE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(data.size()==0 || data==null){
            tv_venue_food_menutitle.setText(tv_venue_food_menutitle.getText()+"\nNot Available !!!");
            tv_venue_food_menutitle.setGravity(Gravity.CENTER);
        }else{
            tv_venue_food_menutitle.setText("Food Items");
            tv_venue_food_menutitle.setGravity(Gravity.CENTER);

        }

        View view;

        switch (viewType) {
            case HEADER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_only_textview, parent, false);
                mContext = parent.getContext();
                return new HeaderViewViewHolder(view);
            case BODY_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_foodmenu_beverageslist, parent, false);
                mContext = parent.getContext();
                return new BodyViewViewHolder(view);
        }
        return null;
    }

    @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        if (data != null) {
            switch (data.get(position).getmType()) {
                case HEADER_TYPE:
                    ((HeaderViewViewHolder) holder).row_text_view_only.setText(data.get(position).getMenu_desc());

                    break;
                case BODY_TYPE:

                    if(position%2!=0){
                        ((BodyViewViewHolder) holder).itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                    }else{
                        ((BodyViewViewHolder) holder).itemView.setBackgroundColor(Color.parseColor("#00F7F9F9"));
                    }

                    final VenueOrderFoodMenuModel item = data.get(position);
                    ((BodyViewViewHolder)holder).tv_title.setText(item.getItem_name());
                    ((BodyViewViewHolder)holder).tv_srNo.setText(""+((int)position+1));
                    ((BodyViewViewHolder)holder).tv_price.setText(" $ "+item.getPrice_per_plate());
                    ((BodyViewViewHolder)holder).tv_price_copy.setText(" $ "+item.getPrice_per_plate());

                    ((BodyViewViewHolder)holder).np_itemcount.setListener(new ScrollableNumberPickerListener() {
                        @Override
                        public void onNumberPicked(int value) {

                        }
                    });
                    ((BodyViewViewHolder)holder).tv_addtocart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (et_date.getText().length() < 1 || et_guestCount.getText().length() < 1 || str_timeslot.equalsIgnoreCase("")
                                    || str_timeslot.equalsIgnoreCase("Select Time")) {
                                Toast.makeText(mContext, "Add Date ,Time and Guest details.", Toast.LENGTH_LONG).show();
                            } else if (str_price_pkg_id.length()<1 || str_price_pkg_id == null) {
                                Toast.makeText(mContext, "Select at list one package.", Toast.LENGTH_LONG).show();
                            }else {
                                if (((BodyViewViewHolder) holder).np_itemcount.getValue() > 0) {
                                    ((BodyViewViewHolder) holder).np_itemcount.setValue(((BodyViewViewHolder) holder).np_itemcount.getValue());
                                    //venue_food_total= venue_food_total+ ( (double)((BodyViewViewHolder)holder).np_itemcount.getValue() * Double.parseDouble(item.getPrice_per_plate()));
                                    //tv_venue_food_menutitle.setText("Foods Menu (Total : $ "+ venue_food_total+" )");

                                    str_menu_id = item.getMenu_id();
                                    str_menu_desc = item.getMenu_desc();
                                    str_item_id = item.getItem_id();
                                    str_item_name = item.getItem_name();
                                    int_Guest_count = ((BodyViewViewHolder) holder).np_itemcount.getValue();
                                    str_price_per_plate = item.getPrice_per_plate();

                                    double total = (double) ((BodyViewViewHolder) holder).np_itemcount.getValue() * Double.parseDouble(item.getPrice_per_plate());
                                    str_total_item_price = Double.toString(total);
                                    new AddCartVenuefood().execute();

                                    ((BodyViewViewHolder) holder).tv_addtocart.setText(" UPDATE ");
                                    ((BodyViewViewHolder) holder).tv_addtocart.setBackgroundResource(R.drawable.rounded_corner_orange_white_borde);
                                    ((BodyViewViewHolder) holder).tv_addtocart.setTextColor(Color.WHITE);
                                    ((BodyViewViewHolder) holder).tv_addtocart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    // Toast.makeText(mContext,"item added to cart",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "Please Add Quantity", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });

                    ((BodyViewViewHolder)holder).np_itemcount.setListener(new ScrollableNumberPickerListener() {
                        @Override
                        public void onNumberPicked(int value) {
                            double total = (double)((BodyViewViewHolder)holder).np_itemcount.getValue() * Double.parseDouble(item.getPrice_per_plate());
                            ((BodyViewViewHolder)holder).tv_price_total.setText("Total : $ "+Double.toString(total));
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
            holder.itemView.setBackgroundResource(R.color.colorPrimaryDark);
        } else {
           // holder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override public int getItemCount() {
        return data != null ? data.size() : 0;
    }



    @Override
    public int getItemViewType(int position) {
        if (data != null && data.get(position).getmType()==0) {
            return HEADER_TYPE;
        }else if(data!=null && data.get(position).getmType()==1){
            return BODY_TYPE;
        }
        return HEADER_TYPE;
    }

    @Override public List<?> getAdapterData() {
        return data;
    }

    private static final class HeaderViewViewHolder extends  RecyclerView.ViewHolder {
        TextView row_text_view_only;
        HeaderViewViewHolder(View itemView) {
            super(itemView);
            row_text_view_only= (TextView) itemView.findViewById(R.id.row_text_view_only);
        }
    }

    private static final class BodyViewViewHolder extends  RecyclerView.ViewHolder {



        private LinearLayout ll_itemcount;


        TextView tv_srNo,tv_title ,tv_addtocart,tv_price,tv_price_copy,tv_price_total;
        ScrollableNumberPicker np_itemcount;

        BodyViewViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_srNo = (TextView) itemView.findViewById(R.id.tv_srNo);
            tv_price= (TextView) itemView.findViewById(R.id.tv_price);
            tv_addtocart = (TextView) itemView.findViewById(R.id.tv_addtocart);
            np_itemcount=(ScrollableNumberPicker)itemView.findViewById(R.id.np_itemcount);

            ll_itemcount =(LinearLayout)itemView.findViewById(R.id.ll_itemcount);

            tv_price_copy = (TextView) itemView.findViewById(R.id.tv_price_copy);
            tv_price_total= (TextView) itemView.findViewById(R.id.tv_price_total);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    String res;
    private class AddCartVenuefood extends AsyncTask<Object, Void, String> {

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
                req.put("cart_id",str_cart_id);
                req.put("menu_id",str_menu_id);
                req.put("menu_desc",str_menu_desc);
                req.put("item_id",str_item_id);
                req.put("item_name",str_item_name);
                req.put("guest_count",int_Guest_count);
                req.put("price_per_plate",str_price_per_plate);
                req.put("total_item_price",str_total_item_price);

                Log.d("REq Json======", req.toString());

                String response = post(Const.SERVER_URL_API +"/cart_food", req.toString(),"put");//post("http://54.153.127.215/api/login", req.toString());
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
                JSONObject obj = new JSONObject(res);
                Log.i("RESPONSE=venue fooods", res);

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

}
