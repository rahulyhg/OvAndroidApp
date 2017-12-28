package ovenues.com.ovenue.adapter;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v13.app.ActivityCompat;
import android.support.v13.view.ViewCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.SVGImageLoader.SvgDecoder;
import ovenues.com.ovenue.SVGImageLoader.SvgDrawableTranscoder;
import ovenues.com.ovenue.SVGImageLoader.SvgSoftwareLayerSetter;
import ovenues.com.ovenue.ServiceProvidersDetailsMainFragment;
import ovenues.com.ovenue.adapter.horizontal_recycler_venuelist.VenueListHoriRecyclerDataAdapter;
import ovenues.com.ovenue.modelpojo.HomeHoriRecyclerSingleItem;
import ovenues.com.ovenue.modelpojo.SectionDataModel;
import ovenues.com.ovenue.modelpojo.ServicesListModel;
import ovenues.com.ovenue.utils.Const;


/**
 * Created by Testing on 18-Oct-16.
 */




public class ServicesListAdapter extends RecyclerView.Adapter<ServicesListAdapter.DataObject_postHolder> {
    static private ArrayList<ServicesListModel> mDataset;
    static private Context mContext;

    SharedPreferences sharepref;

    //==dynamic hori scroll===

    //===CUSTOME HORIZONTAL RECYCLE VIEW WITH SECTION=========
    static ArrayList<SectionDataModel> allSampleData;

    //===SECTION 1 FOR SERVICE=============
    static SectionDataModel dm = new SectionDataModel();

    static ArrayList<HomeHoriRecyclerSingleItem> singleItem ;
    static RecyclerView my_recycler_view;
    static VenueListHoriRecyclerDataAdapter adapter;

    public static class DataObject_postHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_title,tv_capacity,tv_addressline1,tv_cityname ,tv_price,tvdemo,tv_pricerange,tv_yelpcount,tv_cuisines;
        ImageView img_venue,img_feature,img_rating,img_yelp;
        LinearLayout ll_Yelp;


        public DataObject_postHolder(final View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_addressline1 = (TextView) itemView.findViewById(R.id.tv_addressline1);
            tv_cityname= (TextView) itemView.findViewById(R.id.tv_cityname);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_capacity= (TextView) itemView.findViewById(R.id.tv_capacity);
            tvdemo = (TextView) itemView.findViewById(R.id.tvdemo);
            tv_yelpcount  = (TextView) itemView.findViewById(R.id.tv_yelpcount);
            tv_pricerange = (TextView) itemView.findViewById(R.id.tv_pricerange);
            tv_cuisines = (TextView) itemView.findViewById(R.id.tv_cuisines);
            ll_Yelp = (LinearLayout)itemView.findViewById(R.id.ll_Yelp);


            img_yelp = (ImageView) itemView.findViewById(R.id.img_yelp);
            img_venue=(ImageView)itemView.findViewById(R.id.img_venue);
            img_feature = (ImageView) itemView.findViewById(R.id.img_feature);
            img_rating = (ImageView) itemView.findViewById(R.id.img_rating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Build.VERSION.SDK_INT >= 21) {
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(((Activity)mContext), img_venue, ViewCompat.getTransitionName(img_venue));
                        mContext.startActivity(new Intent(mContext, ServiceProvidersDetailsMainFragment.class)
                                .putExtra("service_provider_id",mDataset.get(getAdapterPosition()).getId()),options.toBundle());
                    }
                    else {
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(((Activity)mContext), img_venue, ViewCompat.getTransitionName(img_venue));
                        ActivityCompat.startActivity(mContext,new Intent(mContext, ServiceProvidersDetailsMainFragment.class)
                                .putExtra("service_provider_id",mDataset.get(getAdapterPosition()).getId()),options.toBundle());
                    }



                }
            });

            //====CREARTE CUSTOME HORIZONTAL RECYCLERVIEW =======
            allSampleData = new ArrayList<SectionDataModel>();
            singleItem = new ArrayList<HomeHoriRecyclerSingleItem>();

            my_recycler_view = (RecyclerView)itemView.findViewById(R.id.my_recycler_view);
            my_recycler_view.setHasFixedSize(true);
            adapter = new VenueListHoriRecyclerDataAdapter(mContext, allSampleData);
            my_recycler_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            my_recycler_view.setAdapter(adapter);

        }

        @Override
        public void onClick(View v) {
            //     myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

   /* public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }*/

    public ServicesListAdapter(ArrayList<ServicesListModel> myDataset, Context context) {
        mDataset = myDataset;
        mContext=context;
    }

    @Override
    public DataObject_postHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_services_list, parent, false);
        DataObject_postHolder dataObjectHolder = new DataObject_postHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObject_postHolder holder,final int position) {
        String venue_pic="";
      final String venue_pic2=venue_pic;

        holder.setIsRecyclable(false);
        sharepref = mContext.getApplicationContext().getSharedPreferences("MyPref",mContext.MODE_PRIVATE);


        if(mDataset.get(position).getIs_registered().equalsIgnoreCase("1")){
            holder.img_feature.setVisibility(View.VISIBLE);
        }else{
            holder.img_feature.setVisibility(View.GONE);
        }

        if(mDataset.get(position).getCuisines()!=null && mDataset.get(position).getCuisines().toString().length()>1){
            holder.tv_cuisines.setText(mDataset.get(position).getCuisines().toString());
        }else{
            holder.tv_cuisines.setVisibility(View.GONE);
        }

        float yelp_rating = Float.parseFloat(mDataset.get(position).getYelp_rating());
        //float yelp_rating = Float.parseFloat("1");

        GenericRequestBuilder<Uri,InputStream,SVG,PictureDrawable>
                requestBuilder = Glide.with(mContext)
                .using(Glide.buildStreamModelLoader(Uri.class, mContext), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder(180,25)))
                .decoder(new SvgDecoder(180,25))
                .placeholder(R.drawable.loading_image_pic)
                .error(R.drawable.loading_image_pic)
                .listener(new SvgSoftwareLayerSetter<Uri>());

        Uri uri = Uri.parse(Const.SERVER_URL_ONLY+"img/star_rating/"+yelp_rating+"star.svg");
       // Log.e("svg rating image",Const.SERVER_URL_ONLY+"img/star_rating/"+yelp_rating+"star.svg");
        requestBuilder
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .load(uri)
                .into(holder.img_rating);

        if(mDataset.get(position).getYelp_review_count()!=null && !mDataset.get(position).getYelp_review_count().equalsIgnoreCase("0")){
            holder.tv_yelpcount.setText(" ( "+mDataset.get(position).getYelp_review_count().toString()+" reviews ) ");
        }else if(mDataset.get(position).getYelp_review_count()!=null && mDataset.get(position).getYelp_review_count().equalsIgnoreCase("0")){

            holder.img_yelp.setVisibility(View.INVISIBLE);
            holder.img_rating.setVisibility(View.INVISIBLE);
            holder.tv_yelpcount.setVisibility(View.INVISIBLE);
            holder.ll_Yelp.setVisibility(View.GONE);

        }

        if(mDataset.get(position).getYelp_id()!=null){/*
            holder.img_yelp.setVisibility(View.VISIBLE);*/

            holder.img_yelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://www.yelp.com/biz/"+mDataset.get(position).getYelp_id();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mContext.startActivity(i);
                }
            });

        }else{
            holder.img_yelp.setVisibility(View.GONE);
        }


        String yelp_price_range = mDataset.get(position).getYelp_price_range();

        if(yelp_price_range.length()==1){
            holder.tv_pricerange.setText(Html.fromHtml("<font color=\"#F8A058\">" + "$"+ "</font>"+"<font color=\"#E0E0E0\">" + " $ $ $"+ "</font>"));
        }else if(yelp_price_range.length()==2){
            holder.tv_pricerange.setText(Html.fromHtml("<font color=\"#F8A058\">" + "$ $"+ "</font>"+"<font color=\"#E0E0E0\">" + " $ $"+ "</font>"));
        }else if(yelp_price_range.length()==3){
            holder.tv_pricerange.setText(Html.fromHtml("<font color=\"#F8A058\">" + "$ $ $" + "</font>"+"<font color=\"#E0E0E0\">" + " $"+ "</font>"));
        }else if(yelp_price_range.length()==4){
            holder.tv_pricerange.setText(Html.fromHtml("<font color=\"#F8A058\">" + "$ $ $ $"));
        }else{
            holder.tv_pricerange.setText(Html.fromHtml("<font color=\"#E0E0E0\">" + "$ $ $ $"));
            //holder.tv_pricerange.setVisibility(View.INVISIBLE);
        }

        holder.tv_title.setText(mDataset.get(position).getService_providerName());
        holder.tv_addressline1.setText(mDataset.get(position).getAddress()+" , "+mDataset.get(position).getCityname());
        holder.tv_cityname.setText(mDataset.get(position).getCityname());

       /* holder.tv_addressline2.setVisibility(View.GONE);
        holder.tv_price.setText("$"+mDataset.get(position).getAverage_cost());
        holder.tv_capacity.setText("Capacity :\n"+mDataset.get(position).getMin_occupancy()+" - "+mDataset.get(position).getMax_occupancy());

        if(mDataset.get(position).getCost_type().equalsIgnoreCase("1")){
            holder.tvdemo.setText("*per person");
        }else if(mDataset.get(position).getCost_type().equalsIgnoreCase("2")){
            holder.tvdemo.setText("*per hour");
        }else if(mDataset.get(position).getCost_type().equalsIgnoreCase("3")){
            holder.tvdemo.setText("*starting price");
        }else{
            holder.tv_price.setText("Request Quote !");
            holder.tvdemo.setText("*");
        }*/

        try {



            //Log.e("provider img---",mDataset.get(position).getImag_url().toString());
            if(mDataset.get(position).getImag_url().toString()!=null) {
                if(mDataset.get(position).getImag_url().toString()!=null){
                    if(mDataset.get(position).getImag_url().toString().contains("img/placeholder_services")){
                        venue_pic= Const.AMAZON_PLACEHOLDER_IMAGE_URL+URLEncoder.encode(sharepref.getString(Const.PREF_STR_SERVICE_NAME,"").trim().toLowerCase()).replace("+","-")+"-placeholder.jpg";
                    }else{
                        venue_pic=mDataset.get(position).getImag_url().toString();
                    }
                }



                Glide.with(mContext)
                        .load(venue_pic)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .crossFade()
                        .dontAnimate()
                        .skipMemoryCache(true)
                        .error(R.drawable.ic_noimage_placeholder)
                        .into(holder.img_venue);


            }else{

            }

        }catch (Exception expbitmap){
            expbitmap.printStackTrace();
        }


    }

    public void addItem(ServicesListModel dataObj, int index) {
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
}