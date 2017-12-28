package ovenues.com.ovenue.VenueOrderServicesStickyHeader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.modelpojo.VenueOrderModel.VenueOrderServiceModel;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.VenueDetailsMainFragment.str_cart_id;
import static ovenues.com.ovenue.VenueDetailsMainFragment.str_price_pkg_id;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.et_date;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.et_guestCount;
import static ovenues.com.ovenue.fragments.venue_details.VenuePricingPlans.str_timeslot;
import static ovenues.com.ovenue.utils.APICall.post;

public class VenueOrderServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements StickyHeaderHandler {

    private static final int HEADER_TYPE = 0;
    private static final int BODY_TYPE = 1;
    private final ArrayList<VenueOrderServiceModel> data;
    Context mContext;
    double total_venue =0;

    private String str_id, str_service_title, str_service_inclusions, /*str_charges,*/ str_is_flat_charges, str_is_per_person_charges, str_pacakage_hours,
            str_hour_extension_charges, str_extension_duration, str_extra_person_charges, str_max_guest_extension, str_max_hour_extension, str_is_group_size, str_group_size_from,
            str_group_size_to, str_photo_url, str_is_delivery, str_is_delivery_paid, str_delivery_charges, str_is_pickup, str_is_onsite_service, str_is_delivery_na;

    private String str_service_id,str_service_name ,str_service_option_id ,str_option_desc ,str_option_charges,str_is_hour_extn_changes ,str_extension_charges,str_total_service_charges;


    public VenueOrderServicesAdapter(Context context, ArrayList<VenueOrderServiceModel> data) {
        this.data = data;
        this.mContext=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        switch (viewType) {
            case HEADER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_only_textview_stickyheaderdivider, parent, false);
                mContext = parent.getContext();
                return new HeaderViewViewHolder(view);
            case BODY_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_venueorder_services_layout, parent, false);
                mContext = parent.getContext();
                return new BodyViewViewHolder(view);
        }
        return null;
    }

    @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        holder.setIsRecyclable(false);

        if (data != null) {
            switch (data.get(position).getmUiType()) {
                case HEADER_TYPE:
                    /*((HeaderViewViewHolder) holder).row_text_view_only.setText(Html.fromHtml("<h6>"+data.get(position).getService_name()+"</h6><p>"+data.get(position).getService_desc()+"</p>", Html.FROM_HTML_MODE_COMPACT));
                    ;*/
                    //((HeaderViewViewHolder) holder).row_text_view_only.setText(data.get(position).getService_name()+"\n"+data.get(position).getService_desc());

                    ((HeaderViewViewHolder) holder).row_text_view_only.setText(Html.fromHtml("<font color=\"#757575\">" + data.get(position).getService_name() + "</font>"
                            + "<br/><small> <font color=\"#757575\">" + data.get(position).getService_desc() + "</small></font>"));
                    ((HeaderViewViewHolder) holder).row_text_view_only.setTextSize(15);

                    break;
                case BODY_TYPE:

                    if(data.get(position).isSelected()==true){
                        ((BodyViewViewHolder) holder).tv_addtocart.setText(" UPDATE ");
                        ((BodyViewViewHolder) holder).tv_addtocart.setBackgroundResource(R.drawable.rounded_corner_orange_white_borde);
                        ((BodyViewViewHolder) holder).tv_addtocart.setTextColor(Color.WHITE);
                        ((BodyViewViewHolder) holder).tv_addtocart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }else {

                    }

                    /*
                    if(position%2==0){
                        holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                    }else{
                        holder.itemView.setBackgroundColor(Color.parseColor("#00F7F9F9"));
                    }*/

                    str_service_title= data.get(position).getOptiondesc();
                    str_service_inclusions= data.get(position).getOption_details();

                   // str_charges= data.get(position).getOption_charges();

                    str_is_flat_charges= data.get(position).getIs_flat_charges();
                    str_is_per_person_charges= data.get(position).getIs_per_person_charges();
                    //str_is_per_item= mDataset.get(position).getIs_per_item();

                    // str_pacakage_hours= mDataset.get(position).getPacakage_hours();
                    str_hour_extension_charges= data.get(position).getIs_hour_extn_changes();
                    //str_extension_duration= mDataset.get(position).getExtension_duration();
                    //str_extra_person_charges= mDataset.get(position).getExtra_person_charges();

                    str_max_guest_extension= data.get(position).getGroup_size_to();
                    str_max_hour_extension= data.get(position).getGroup_size_from();
                    str_is_group_size= data.get(position).getIs_group_size();
                    str_group_size_from= data.get(position).getGroup_size_from();
                    str_group_size_to= data.get(position).getGroup_size_to();
                    //str_photo_url= mDataset.get(position).getPhoto_url();

                    //textView_contain.setText(getItem(position));

                    if(str_is_flat_charges.equalsIgnoreCase("1")){
                        ((BodyViewViewHolder)holder).tv_perplate.setText("* flat rate");
                        ((BodyViewViewHolder)holder).ll_duration.setVisibility(View.GONE);
                        ((BodyViewViewHolder)holder).ll_itemcount.setVisibility(View.GONE);
                    }else if(data.get(position).getIs_per_person_charges().equalsIgnoreCase("1")){
                        ((BodyViewViewHolder)holder).ll_duration.setVisibility(View.GONE);
                        ((BodyViewViewHolder)holder).ll_itemcount.setVisibility(View.VISIBLE);
                        ((BodyViewViewHolder)holder).tv_price_copy.setText(data.get(position).getOption_charges());
                        ((BodyViewViewHolder)holder).tv_perplate.setText("* per person");
                    }else if(data.get(position).getIs_per_hour_charges().equalsIgnoreCase("1")){
                        ((BodyViewViewHolder)holder).ll_itemcount.setVisibility(View.GONE);
                        ((BodyViewViewHolder)holder).ll_duration.setVisibility(View.VISIBLE);
                        ((BodyViewViewHolder)holder).tv_perplate.setText("*per hour"+" ( ext : $ "+data.get(position).getExtension_charges()+" )");
                    }
                    ((BodyViewViewHolder)holder).tv_srNo.setText(""+(int)(position+1));
                    ((BodyViewViewHolder)holder).tv_title.setText(data.get(position).getOptiondesc()/*+"\n"+data.get(position).getOptiondesc()*/);
                    ((BodyViewViewHolder)holder).tv_price.setText("$ "+data.get(position).getOption_charges());



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

                    final ArrayAdapter<String> dataAdapter_hh = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, list_hh);
                    dataAdapter_hh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ((BodyViewViewHolder)holder).sp_hh.setAdapter(dataAdapter_hh);


                    List<String> list_mm = new ArrayList<String>();
                    list_mm.add("MM");
                    list_mm.add("00");
                    list_mm.add("15");
                    list_mm.add("30");
                    list_mm.add("45");

                    ArrayAdapter<String> dataAdapter_mm = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, list_mm);
                    dataAdapter_mm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ((BodyViewViewHolder)holder).sp_mm.setAdapter(dataAdapter_mm);



                    ((BodyViewViewHolder)holder).sp_hh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int positionSP, long id) {

                            if(((BodyViewViewHolder)holder).sp_mm.getSelectedItemPosition()>1 && positionSP>1){
                                double total = (double) (((BodyViewViewHolder)holder).sp_hh.getSelectedItemPosition()  +1) * Double.parseDouble(data.get(position).getOption_charges());
                                ((BodyViewViewHolder)holder).tv_price_total.setText("Total : $ "+Double.toString(total));
                            }else{
                                if(positionSP > 1) {
                                    double total = (double) (((BodyViewViewHolder)holder).sp_hh.getSelectedItemPosition())* Double.parseDouble(data.get(position).getOption_charges());
                                    ((BodyViewViewHolder) holder).tv_price_total.setText("Total : $ " + Double.toString(total));
                                }else{
                                    double total = (double)0.0;
                                    ((BodyViewViewHolder) holder).tv_price_total.setText("Total : $ " + Double.toString(total));
                                }
                            }
                           /* Toast.makeText(parent.getContext(), "Time : " +
                                     ((BodyViewViewHolder)holder).sp_hh.getItemAtPosition(((BodyViewViewHolder)holder).sp_hh.getSelectedItemPosition()).toString()
                                    + ":"
                                    +String.valueOf(((BodyViewViewHolder)holder).sp_mm.getSelectedItem()), Toast.LENGTH_SHORT).show();*/
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });



                    ((BodyViewViewHolder)holder).sp_mm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int positionSP, long id) {
                            if(positionSP>1){
                                double total = (double) (((BodyViewViewHolder)holder).sp_hh.getSelectedItemPosition() + 1 )* Double.parseDouble(data.get(position).getOption_charges());
                                ((BodyViewViewHolder)holder).tv_price_total.setText("Total : $ "+Double.toString(total));
                            }else{
                                if(((BodyViewViewHolder)holder).sp_hh.getSelectedItemPosition()>0){
                                    double total = (double) (((BodyViewViewHolder)holder).sp_hh.getSelectedItemPosition())* Double.parseDouble(data.get(position).getOption_charges());
                                    ((BodyViewViewHolder)holder).tv_price_total.setText("Total : $ "+Double.toString(total));
                                }else{
                                    double total = (double)0.0;
                                    ((BodyViewViewHolder)holder).tv_price_total.setText("Total : $ "+Double.toString(total));
                                }
                            }
                            /*Toast.makeText(parent.getContext(), "Time : " + ((BodyViewViewHolder)holder).sp_hh.getItemAtPosition(((BodyViewViewHolder)holder).sp_hh.getSelectedItemPosition()).toString()
                                    + ":" +((BodyViewViewHolder)holder).sp_mm.getItemAtPosition(((BodyViewViewHolder)holder).sp_mm.getSelectedItemPosition()).toString(), Toast.LENGTH_SHORT).show();*/

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    ((BodyViewViewHolder)holder).np_itemcount.setListener(new ScrollableNumberPickerListener() {
                        @Override
                        public void onNumberPicked(int value) {
                            double total = (double)value * Double.parseDouble(data.get(position).getOption_charges());
                            ((BodyViewViewHolder)holder).tv_price_total.setText("Total : $ "+Double.toString(total));
                        }
                    });
                    ((BodyViewViewHolder)holder).tv_addtocart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (et_date.getText().length() < 1 || et_guestCount.getText().length() < 1 || (str_timeslot.length()<1
                                    || str_timeslot.equalsIgnoreCase("Select Time"))) {
                                Toast.makeText(mContext, "Add Date ,Time and Guest details.", Toast.LENGTH_LONG).show();
                            } else {

                                str_service_id = data.get(position).getService_id();
                                str_service_name = data.get(position).getService_name();
                                str_service_option_id = data.get(position).getService_option_id();
                                str_option_desc = data.get(position).getService_desc();
                                str_option_charges = data.get(position).getOption_charges();
                                str_is_hour_extn_changes = "0";
                                str_extension_charges = "0";

                                double total_cart = 0;
                                if (((BodyViewViewHolder) holder).ll_itemcount.getVisibility() == View.VISIBLE) {
                                    total_cart = (double) ((BodyViewViewHolder) holder).np_itemcount.getValue() * Double.parseDouble(data.get(position).getOption_charges());
                                    str_total_service_charges = Double.toString(total_cart);
                                    Log.d("str_total_service_chrg", str_total_service_charges);

                                } else if (((BodyViewViewHolder) holder).ll_duration.getVisibility() == View.VISIBLE) {
                                    if (((BodyViewViewHolder) holder).sp_mm.getSelectedItemPosition() > 0) {
                                        total_cart = ((double) ((BodyViewViewHolder) holder).sp_hh.getSelectedItemPosition()+2) * Double.parseDouble(data.get(position).getOption_charges());
                                    } else {
                                        total_cart = (double) ((BodyViewViewHolder) holder).sp_hh.getSelectedItemPosition() * Double.parseDouble(data.get(position).getOption_charges());
                                    }
                                    str_total_service_charges = Double.toString(total_cart);
                                    Log.d("str_total_service_chrg", str_total_service_charges);
                                } else if (data.get(position).getIs_flat_charges().equalsIgnoreCase("1")) {
                                    str_total_service_charges = data.get(position).getOption_charges();
                                    Log.d("str_total_service_chrg", str_total_service_charges);
                                }


                                if (((BodyViewViewHolder) holder).ll_itemcount.getVisibility() == View.VISIBLE && ((BodyViewViewHolder) holder).np_itemcount.getValue() < 1) {
                                    Toast.makeText(mContext, "Select QTY please", Toast.LENGTH_LONG).show();
                                } else if (((BodyViewViewHolder) holder).ll_duration.getVisibility() == View.VISIBLE
                                        && ((BodyViewViewHolder) holder).sp_hh.getSelectedItemPosition() ==0
                                        && ((BodyViewViewHolder) holder).sp_mm.getSelectedItemPosition() ==0) {
                                    Toast.makeText(mContext, "Select Duration please", Toast.LENGTH_LONG).show();
                                } else {
                                    if (data.get(position).getServiceType().equalsIgnoreCase("0")) {
                                        if (str_price_pkg_id.length()<1 || str_price_pkg_id == null) {
                                            Toast.makeText(mContext, "Select at list one package.", Toast.LENGTH_LONG).show();
                                        }else {
                                            new AddCartVenueService().execute();
                                            ((BodyViewViewHolder) holder).tv_addtocart.setText(" UPDATE ");
                                            ((BodyViewViewHolder) holder).tv_addtocart.setBackgroundResource(R.drawable.rounded_corner_orange_white_borde);
                                            ((BodyViewViewHolder) holder).tv_addtocart.setTextColor(Color.WHITE);
                                            ((BodyViewViewHolder) holder).tv_addtocart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                            data.get(position).setSelected(true);
                                        }
                                    } else if (data.get(position).getServiceType().equalsIgnoreCase("1")) {

                                        if (str_price_pkg_id.length()<1 || str_price_pkg_id == null) {
                                            Toast.makeText(mContext, "Select at list one package.", Toast.LENGTH_LONG).show();
                                        } else {
                                            new AddCartVenueExtraService().execute();

                                            ((BodyViewViewHolder) holder).tv_addtocart.setText(" UPDATE ");
                                            ((BodyViewViewHolder) holder).tv_addtocart.setBackgroundResource(R.drawable.rounded_corner_orange_white_borde);
                                            ((BodyViewViewHolder) holder).tv_addtocart.setTextColor(Color.WHITE);
                                            ((BodyViewViewHolder) holder).tv_addtocart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                            data.get(position).setSelected(true);
                                        }
                                    }
                                }
                            }
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

       /* if (data.get(position) instanceof StickyHeader) {
            holder.itemView.setBackgroundResource(R.color.colorPrimaryDark);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }*/
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

        private LinearLayout ll_duration,ll_itemcount;
        TextView tv_perplate,tv_srNo,tv_title ,tv_price,tv_addtocart,tv_price_copy,tv_price_total;
        ScrollableNumberPicker np_itemcount;
        Spinner sp_hh,sp_mm;

        BodyViewViewHolder(View itemView) {
            super(itemView);

            sp_hh = (Spinner)itemView.findViewById(R.id.sp_hh);
            sp_mm = (Spinner)itemView.findViewById(R.id.sp_mm);



            np_itemcount= (ScrollableNumberPicker)itemView.findViewById(R.id.np_itemcount);


            ll_duration =(LinearLayout)itemView.findViewById(R.id.ll_duration);
            ll_itemcount =(LinearLayout)itemView.findViewById(R.id.ll_itemcount);

            tv_perplate= (TextView) itemView.findViewById(R.id.tv_perplate);
            tv_srNo = (TextView) itemView.findViewById(R.id.tv_srNo);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_addtocart= (TextView) itemView.findViewById(R.id.tv_addtocart);

            tv_price_copy = (TextView) itemView.findViewById(R.id.tv_price_copy);
            tv_price_total= (TextView) itemView.findViewById(R.id.tv_price_total);
        }
    }

    String res;
    private class AddCartVenueService extends AsyncTask<Object, Void, String> {

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
                req.put("service_id",str_service_id);
                req.put("service_name",str_service_name);
                req.put("service_option_id",str_service_option_id);
                req.put("option_desc",str_option_desc);
                req.put("option_charges",str_option_charges);
                req.put("is_hour_extn_changes",str_is_hour_extn_changes);
                req.put("extension_charges",str_extension_charges);
                req.put("total_service_charges",str_total_service_charges);

                Log.d("REq Json======", req.toString());

                String response = post(Const.SERVER_URL_API +"/cart_service", req.toString(),"put");//post("http://54.153.127.215/api/login", req.toString());
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
                           /* .setAction("OK", new View.OnClickListener() {
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


    String res_extService;
    private class AddCartVenueExtraService extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog2;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //Log.d("pre execute", "Pre execute Done");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog2 = ProgressDialog.show(mContext, "Loading", "Please wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");
            //Log.d("post execute", "Executando doInBackground   ingredients");
            try {


                JSONObject req = new JSONObject();
                req.put("cart_id",str_cart_id);
                req.put("extra_service_id",str_service_id);
                req.put("extra_service_name",str_service_name);
                req.put("extra_service_option_id",str_service_option_id);
                req.put("option_desc",str_option_desc);
                req.put("option_charges",str_option_charges);
                req.put("is_hour_extn_changes",str_is_hour_extn_changes);
                req.put("extension_charges",str_extension_charges);
                req.put("total_extra_service_charges",str_total_service_charges);

                Log.d("REq Json======", req.toString());


                String response = post(Const.SERVER_URL_API +"/cart_extra_service", req.toString(),"put");

                //Log.d("REsponce Json====",response);
                res_extService = response;
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return res_extService;

        }
        @Override
        protected void onPostExecute(String result)
        { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            super.onPostExecute(result);
        }
            //System.out.println("OnpostExecute----done-------");
            progressDialog2.dismiss();

            if (res_extService == null || res_extService.equals("")) {

                progressDialog2.dismiss();
                Toast.makeText(mContext, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject obj = new JSONObject(res_extService);
                Log.i("RESPONSE=venue fooods", res_extService);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.

                res_extService=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(res_extService.equals("success")){

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
                           /* .setAction("OK", new View.OnClickListener() {
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
                progressDialog2.dismiss();

            }
        }

        @Override
        protected void onCancelled() {
        }
    }
}
