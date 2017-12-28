package ovenues.com.ovenue.adapter.serviceprovider_details_page.ServiceProviderCateringChargesMenuAdapters;


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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.modelpojo.ServiceProviderChargesMenuModel.FoodMenuModel;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.fragments.serviceproviders_details.ServiceproviderCateringPricingFragment.str_cart_ID_SP_Main;
import static ovenues.com.ovenue.utils.APICall.post;

/**
 * Created by Jay-Andriod on 08-May-17.
 */


public class ServiceProviderBeveragesMenuAdapter extends RecyclerView
        .Adapter<ServiceProviderBeveragesMenuAdapter
        .DataObject_postHolder> {
    static private ArrayList<FoodMenuModel> mDataset;
    static private Context mContext;

    SharedPreferences sharepref;
    String str_beverage_id ,str_option_desc,str_option_charges;
    int int_Guest_count,int_total_baverage_price;


    public static class DataObject_postHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout ll_itemcount;
        TextView tv_srNo,tv_title ,tv_addtocart,tv_price,tv_price_copy,tv_price_total,tv_subtitle;
        ScrollableNumberPicker np_itemcount;

        public DataObject_postHolder(final View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_subtitle= (TextView) itemView.findViewById(R.id.tv_subtitle);

            tv_srNo = (TextView) itemView.findViewById(R.id.tv_srNo);
            tv_price= (TextView) itemView.findViewById(R.id.tv_price);



            tv_addtocart = (TextView) itemView.findViewById(R.id.tv_addtocart);
            np_itemcount=(ScrollableNumberPicker)itemView.findViewById(R.id.np_itemcount);


            ll_itemcount =(LinearLayout)itemView.findViewById(R.id.ll_itemcount);

            tv_price_copy = (TextView) itemView.findViewById(R.id.tv_price_copy);
            tv_price_total= (TextView) itemView.findViewById(R.id.tv_price_total);
            tv_price_total.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                }
            });

        }

        @Override
        public void onClick(View v) {
            //     myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

   /* public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }*/

    public ServiceProviderBeveragesMenuAdapter(ArrayList<FoodMenuModel> myDataset, Context context) {
        mDataset = myDataset;
        mContext=context;
    }

    @Override
    public DataObject_postHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_foodmenu_beverageslist, parent, false);
        ServiceProviderBeveragesMenuAdapter.DataObject_postHolder dataObjectHolder = new ServiceProviderBeveragesMenuAdapter.DataObject_postHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final ServiceProviderBeveragesMenuAdapter.DataObject_postHolder holder, final int position) {

        holder.setIsRecyclable(false);
        sharepref = mContext.getApplicationContext().getSharedPreferences("MyPref",mContext.MODE_PRIVATE);
        if(position%2==0){
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));/*
            holder.itemView.setBackgroundColor(Color.parseColor("#00F7F9F9"));*/
        }


        holder.tv_title.setText(mDataset.get(position).getMenu_desc());
        if(mDataset.get(position).getIs_group_size().equalsIgnoreCase("1")){
            holder.tv_subtitle.setText("Qty "+mDataset.get(position).getGroup_size_from()+" - "+mDataset.get(position).getGroup_size_to());
            holder.np_itemcount.setMinValue(Integer.parseInt(mDataset.get(position).getGroup_size_from()));
            holder.np_itemcount.setMaxValue(Integer.parseInt(mDataset.get(position).getGroup_size_to()));
        }else{
            holder.tv_subtitle.setVisibility(View.GONE);
        }
        holder.tv_srNo.setText(""+((int)position+1));
        holder.tv_price.setText(" $ "+mDataset.get(position).getPrice_per_plate());
        holder.tv_price_copy.setText(" $ "+mDataset.get(position).getPrice_per_plate());
        holder.tv_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"Item Added to cart.",Toast.LENGTH_LONG).show();
            }
        });

        if(mDataset.get(position).getIs_group_size()!=null && mDataset.get(position).getIs_group_size().equalsIgnoreCase("1")){
            holder.np_itemcount.setMaxValue(Integer.parseInt(mDataset.get(position).getGroup_size_to()));
            holder.tv_title.setText(holder.tv_title.getText().toString()+"\n QTY ( "+mDataset.get(position).getGroup_size_from()+
                    " - "+mDataset.get(position).getGroup_size_to()+" )");
        }
        holder.np_itemcount.setListener(new ScrollableNumberPickerListener() {
            @Override
            public void onNumberPicked(int value) {
                double total = (double)holder.np_itemcount.getValue() * Double.parseDouble(mDataset.get(position).getPrice_per_plate());
                holder.tv_price_total.setText("Total : $ "+Double.toString(total));
                holder.tv_price_copy.setText(" $ "+Double.toString(total));
            }
        });

        holder.tv_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_beverage_id = mDataset.get(position).getMenu_id();
                str_option_desc=mDataset.get(position).getMenu_desc();
                str_option_charges=mDataset.get(position).getPrice_per_plate();
                int_Guest_count = holder.np_itemcount.getValue();

                double total = (double)holder.np_itemcount.getValue() * Double.parseDouble(mDataset.get(position).getPrice_per_plate());
                int_total_baverage_price =(int)total;
                if(str_cart_ID_SP_Main.equalsIgnoreCase("") || str_cart_ID_SP_Main.length()<1){
                    Toast.makeText(mContext,"Please add Delivery / Pickup Details .",Toast.LENGTH_SHORT).show();
                }else if((mDataset.get(position).getIs_group_size()!=null && mDataset.get(position).getIs_group_size().equalsIgnoreCase("1"))
                        && (holder.np_itemcount.getValue()<Integer.parseInt(mDataset.get(position).getGroup_size_from()) || holder.np_itemcount.getValue() > Integer.parseInt(mDataset.get(position).getGroup_size_to()))){
                    Toast.makeText(mContext,"QTY must be "+mDataset.get(position).getGroup_size_from()+" - "+mDataset.get(position).getGroup_size_to(),Toast.LENGTH_LONG).show();
                }else{
                    new AddCartSPBeverages().execute();
                    holder.tv_addtocart.setText(" UPDATE ");
                    holder.tv_addtocart.setBackgroundResource(R.drawable.rounded_corner_orange_white_borde);
                    holder.tv_addtocart.setTextColor(Color.WHITE);
                    holder.tv_addtocart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    String res;
    protected ProgressDialog progressDialogSPBevrages;
    private class AddCartSPBeverages extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";


        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //Log.d("pre execute", "Pre execute Done");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialogSPBevrages = ProgressDialog.show(mContext, "Loading", "Please wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {


            System.out.println("On do in back ground----done-------");
            //Log.d("post execute", "Executando doInBackground   ingredients");
            try {

                JSONObject req = new JSONObject();
                req.put("cart_id",Integer.parseInt(str_cart_ID_SP_Main));
                req.put("beverage_id",Integer.parseInt(str_beverage_id));
                req.put("option_desc",str_option_desc);
                req.put("option_charges",Integer.parseInt(str_option_charges));

                req.put("guest_count",int_Guest_count);
                req.put("total_baverage_price",int_total_baverage_price);
                req.put("is_hour_extn_changes","0");
                req.put("extension_charges","0");

                Log.d("REq Json======", req.toString());

                String response = post(Const.SERVER_URL_API +"/cart_catering_beverage", req.toString(),"put");//post("http://54.153.127.215/api/login", req.toString());
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
            progressDialogSPBevrages.dismiss();
        }
            //System.out.println("OnpostExecute----done-------");
            progressDialogSPBevrages.dismiss();

            if (res == null || res.equals("")) {

                progressDialogSPBevrages.dismiss();
                Toast.makeText(mContext, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                Log.i("RESPONSE=venue fooods", res);
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