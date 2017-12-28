package ovenues.com.ovenue.adapter.horizontal_recycler_home_topfive;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import ovenues.com.ovenue.AskOvenues;
import ovenues.com.ovenue.R;
import ovenues.com.ovenue.SVGImageLoader.SvgDecoder;
import ovenues.com.ovenue.SVGImageLoader.SvgDrawableTranscoder;
import ovenues.com.ovenue.SVGImageLoader.SvgSoftwareLayerSetter;
import ovenues.com.ovenue.ServiceProvidersDetailsMainFragment;
import ovenues.com.ovenue.ServicesList;
import ovenues.com.ovenue.VenueDetailsMainFragment;
import ovenues.com.ovenue.VenuesList;
import ovenues.com.ovenue.modelpojo.HomeHoriRecyclerSingleItem;
import ovenues.com.ovenue.utils.Const;

import static android.content.Context.MODE_PRIVATE;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.str_service_provider_id;
import static ovenues.com.ovenue.ServicesList.str_service_id;

public class SectionListDataTopFiveAdapter extends RecyclerView.Adapter<SectionListDataTopFiveAdapter.SingleItemRowHolder> {

    private ArrayList<HomeHoriRecyclerSingleItem> itemsList;
    private Context mContext;
    SharedPreferences sharepref;
    String screen_name;
    JsonObject jsonObj;


    public SectionListDataTopFiveAdapter(Context context, ArrayList<HomeHoriRecyclerSingleItem> itemsList, String screen_name) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.screen_name=screen_name;

        sharepref = mContext.getSharedPreferences("MyPref", MODE_PRIVATE);
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = null;

        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_recycle_raw_topfivelist_single_card, null);

        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder,final int i) {
        holder.setIsRecyclable(false);

        final HomeHoriRecyclerSingleItem singleItem = itemsList.get(i);
        jsonObj = itemsList.get(i).getJsonObj();

        if(jsonObj.get("is_registered").getAsString().equalsIgnoreCase("1")){
            holder.img_feature.setVisibility(View.VISIBLE);
        }else{
            holder.img_feature.setVisibility(View.GONE);
        }

        holder.tv_addressline1.setText(jsonObj.get("city_name").getAsString());
        float yelp_rating = Float.parseFloat(jsonObj.get("yelp_rating").getAsString());

        //float yelp_rating = Float.parseFloat("1");

        GenericRequestBuilder<Uri,InputStream,SVG,PictureDrawable>
                requestBuilder = Glide.with(mContext)
                .using(Glide.buildStreamModelLoader(Uri.class, mContext), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder(200,25)))
                .decoder(new SvgDecoder(200,25))
                .placeholder(R.drawable.loading_image_pic)
                .error(R.drawable.loading_image_pic)
                .listener(new SvgSoftwareLayerSetter<Uri>());

        Uri uri = Uri.parse(Const.SERVER_URL_ONLY+"img/star_rating/"+yelp_rating+"star.svg");
        //Log.e("svg rating image",Const.SERVER_URL_ONLY+"img/star_rating/"+yelp_rating+"star.svg");
        requestBuilder
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .load(uri)
                .into(holder.img_rating);

        if(jsonObj.get("yelp_review_count")!=null && !jsonObj.get("yelp_review_count").getAsString().equalsIgnoreCase("0")){
            holder.tv_yelpcount.setText(" ( "+jsonObj.get("yelp_review_count").getAsString()+" reviews ) ");
        }else if(jsonObj.get("yelp_review_count")!=null && jsonObj.get("yelp_review_count").getAsString().equalsIgnoreCase("0")){
            holder.img_yelp.setVisibility(View.INVISIBLE);
            holder.img_rating.setVisibility(View.INVISIBLE);
            holder.tv_yelpcount.setVisibility(View.INVISIBLE);
            holder.ll_Yelp.setVisibility(View.GONE);
        }

        if(jsonObj.get("yelp_id")!=null && jsonObj.get("yelp_id").getAsString().length()>1){
            /*holder.img_yelp.setVisibility(View.VISIBLE);*/

            holder.img_yelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://www.yelp.com/biz/"+ jsonObj.get("yelp_id").getAsString();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mContext.startActivity(i);
                }
            });

        }else{
            holder.img_yelp.setVisibility(View.GONE);
        }

        String yelp_price_range = !jsonObj.get("yelp_price_range").isJsonNull()
                ?jsonObj.get("yelp_price_range").getAsString():null;
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
        holder.tvTitle.setText(singleItem.getName());

       /* if(itemsList.get(i).getType().equalsIgnoreCase(Const.CONST_TOPFIVE)){
            holder.tvTitle.setText(singleItem.getName());
            ViewGroup.LayoutParams layoutParamstv = holder.tvTitle.getLayoutParams();
            layoutParamstv.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParamstv.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.tvTitle.setLayoutParams(layoutParamstv);

        }else {
            holder.tvTitle.setText(singleItem.getName());
            ViewGroup.LayoutParams layoutParamstv = holder.tvTitle.getLayoutParams();
            layoutParamstv.width = 120;
            layoutParamstv.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.tvTitle.setLayoutParams(layoutParamstv);
        }*/

        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        if (itemsList.get(i).getType().equalsIgnoreCase(Const.CONST_TOPFIVE)) {

            if(itemsList.get(i).getService_id().equalsIgnoreCase("0")) {
                final String venue_pic = itemsList.get(i).getPhotourl().length()>1
                        ? itemsList.get(i).getPhotourl().toString() : Const.AMAZON_PLACEHOLDER_IMAGE_URL + "venue-placeholder.jpg";
               // Log.e("image URL---",venue_pic);
                Glide.with(mContext)
                        .load(venue_pic)
                        .centerCrop()
                        .placeholder(R.drawable.loading_image_pic)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_noimage_placeholder)
                        .into(holder.itemImage);

                holder.tv_cuisines.setVisibility(View.GONE);

            }else{
                String venue_pic="";
                if(itemsList.get(i).getService_id()!=null){
                    if(itemsList.get(i).getPhotourl()==null){
                        venue_pic= Const.AMAZON_PLACEHOLDER_IMAGE_URL+ URLEncoder.encode(itemsList.get(i).getName().trim().toLowerCase()).replace("+","-")+"-placeholder.jpg";
                    }else{
                        venue_pic=itemsList.get(i).getPhotourl().toString();
                    }
                }

                if(jsonObj.get("cuisines")!=null && jsonObj.get("cuisines").getAsString().length()>1){
                    holder.tv_cuisines.setText( jsonObj.get("cuisines").getAsString().toString());
                }else{
                    holder.tv_cuisines.setVisibility(View.GONE);
                }


                Glide.with(mContext)
                        .load(venue_pic)
                        .placeholder(R.drawable.loading_image_pic)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_noimage_placeholder)
                        .into(holder.itemImage);
            }
        }

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle,tv_addressline1,tv_pricerange,tv_cuisines,tv_yelpcount;
        protected ImageView itemImage,img_feature,img_rating,img_yelp;
        android.support.design.widget.FloatingActionButton fb_favourite;
        LinearLayout ll_Yelp;



        public SingleItemRowHolder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.tv_addressline1 = (TextView) view.findViewById(R.id.tv_addressline1);
            this.tv_pricerange = (TextView) view.findViewById(R.id.tv_pricerange);
            this.tv_cuisines = (TextView) itemView.findViewById(R.id.tv_cuisines);
            this.tv_yelpcount  = (TextView) itemView.findViewById(R.id.tv_yelpcount);

            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            this.img_feature = (ImageView) view.findViewById(R.id.img_feature);
            this.img_rating = (ImageView) view.findViewById(R.id.img_rating);
            this.img_yelp = (ImageView) itemView.findViewById(R.id.img_yelp);
            fb_favourite =  (android.support.design.widget.FloatingActionButton) view.findViewById(R.id.fb_favourite);

            ll_Yelp = (LinearLayout)itemView.findViewById(R.id.ll_Yelp);

            itemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    jsonObj = itemsList.get(getAdapterPosition()).getJsonObj();

                    if(itemsList.get(getAdapterPosition()).getService_id().equalsIgnoreCase("0")) {
                        mContext.startActivity(new Intent(mContext, VenueDetailsMainFragment.class)
                                .putExtra("venue_name", jsonObj.get("venue_name").getAsString())
                                .putExtra("venue_id", jsonObj.get("venue_id").getAsString()));
                        Log.e("venue name and ID---",jsonObj.get("venue_name").getAsString()+"___"+jsonObj.get("venue_id").getAsString());
                    }else{
                        sharepref.edit().putString(Const.PREF_STR_SERVICE_ID,itemsList.get(getAdapterPosition()).getService_id()).apply();
                        sharepref.edit().putString(Const.PREF_STR_SERVICE_NAME, itemsList.get(getAdapterPosition()).getName()).apply();


                        mContext.startActivity(new Intent(mContext,ServiceProvidersDetailsMainFragment.class)
                                .putExtra("service_provider_id",jsonObj.get("id").getAsString()));


                        Log.e("SP name and ID---",jsonObj.get("id").getAsString());
                    }

                }
            });


            view.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    jsonObj = itemsList.get(getAdapterPosition()).getJsonObj();

                    if(itemsList.get(getAdapterPosition()).getService_id().equalsIgnoreCase("0")) {
                        mContext.startActivity(new Intent(mContext, VenueDetailsMainFragment.class)
                                .putExtra("venue_name", jsonObj.get("venue_name").getAsString())
                                .putExtra("venue_id", jsonObj.get("venue_id").getAsString()));
                        Log.e("venue name and ID---",jsonObj.get("venue_name").getAsString()+"___"+jsonObj.get("venue_id").getAsString());
                    }else{
                        sharepref.edit().putString(Const.PREF_STR_SERVICE_ID,itemsList.get(getAdapterPosition()).getService_id()).apply();
                        sharepref.edit().putString(Const.PREF_STR_SERVICE_NAME, itemsList.get(getAdapterPosition()).getName()).apply();


                        mContext.startActivity(new Intent(mContext,ServiceProvidersDetailsMainFragment.class)
                                .putExtra("service_provider_id",jsonObj.get("id").getAsString()));


                        Log.e("SP name and ID---",jsonObj.get("id").getAsString());
                    }

                  //  Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();

                }
            });


        }

    }

}