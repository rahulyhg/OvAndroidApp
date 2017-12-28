package ovenues.com.ovenue.adapter.horizontal_recycler_servicelist;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.util.ArrayList;

import ovenues.com.ovenue.AskOvenues;
import ovenues.com.ovenue.R;
import ovenues.com.ovenue.SVGImageLoader.SvgDecoder;
import ovenues.com.ovenue.SVGImageLoader.SvgDrawableTranscoder;
import ovenues.com.ovenue.SVGImageLoader.SvgSoftwareLayerSetter;
import ovenues.com.ovenue.ServicesList;
import ovenues.com.ovenue.VenuesList;
import ovenues.com.ovenue.modelpojo.HomeHoriRecyclerSingleItem;
import ovenues.com.ovenue.utils.Const;

import static android.content.Context.MODE_PRIVATE;
import static ovenues.com.ovenue.ServicesList.adapterServiceTypesList;
import static ovenues.com.ovenue.ServicesList.str_service_id;

public class ServiceProvidersListScreenServiceListDataAdapter extends RecyclerView.Adapter<ServiceProvidersListScreenServiceListDataAdapter.SingleItemRowHolder> {


    SharedPreferences sharepref;
    final private ArrayList<HomeHoriRecyclerSingleItem> itemsList;
    private Context mContext;

    public ServiceProvidersListScreenServiceListDataAdapter(Context context, ArrayList<HomeHoriRecyclerSingleItem> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;

        sharepref = mContext.getSharedPreferences("MyPref", MODE_PRIVATE);
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_recycle_raw_list_single_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return mh;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int i) {

        final HomeHoriRecyclerSingleItem singleItem = itemsList.get(i);




        android.view.ViewGroup.LayoutParams layoutParamstv = holder.tvTitle.getLayoutParams();
        layoutParamstv.width = 120;
        layoutParamstv.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        holder.tvTitle.setLayoutParams(layoutParamstv);
        holder.tvTitle.setText(singleItem.getName());


        GenericRequestBuilder<Uri,InputStream,SVG,PictureDrawable>
                requestBuilder = Glide.with(mContext)
                .using(Glide.buildStreamModelLoader(Uri.class, mContext), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder(130,130)))
                .decoder(new SvgDecoder(130,130))
                .placeholder(R.drawable.loading_image_pic)
                .error(R.drawable.loading_image_pic)
                .listener(new SvgSoftwareLayerSetter<Uri>());



        if(singleItem.isIs_selected()==true){
            Uri uri = Uri.parse(itemsList.get(i).getUrl());
            // Log.e("svg orng image",itemsList.get(i).getUrl());
            requestBuilder
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .load(uri)
                    .thumbnail(1.0f)
                    .into(holder.itemImage);
        }else{
            Uri uri = Uri.parse(itemsList.get(i).getUrl2());
            // Log.e("svg orng image",itemsList.get(i).getUrl());
            requestBuilder
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .load(uri)
                    .thumbnail(1.0f)
                    .into(holder.itemImage);
        }


        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemsList.get(i).getType().equalsIgnoreCase(Const.CONST_HOMESERVICE)) {
                    if (itemsList.get(i).getId().equalsIgnoreCase("0")) {
                        mContext.startActivity(new Intent(mContext, VenuesList.class));
                    } else if (itemsList.get(i).getId().equalsIgnoreCase("00")) {
                        //mContext.startActivity(new Intent(mContext, VenuesList.class));
                        // Toast.makeText(mContext, "asked cliked", Toast.LENGTH_LONG).show();
                        mContext.startActivity(new Intent(mContext, AskOvenues.class));
                    } else {
                        sharepref.edit().putString(Const.PREF_STR_SERVICE_NAME, singleItem.getName()).apply();
                        sharepref.edit().putString(Const.PREF_STR_SERVICE_ID, singleItem.getId()).apply();

                        str_service_id = sharepref.getString(Const.PREF_STR_SERVICE_ID, "").toString();
                        mContext.startActivity(new Intent(mContext, ServicesList.class)
                                .putExtra("index_selectedServiceType", String.valueOf(i)));
                    }
                } else if (itemsList.get(i).getType().equalsIgnoreCase(Const.CONST_SERVICE)) {

                    //adapterServiceTypesList.notifyDataSetChanged();
                    sharepref.edit().putString(Const.PREF_STR_SERVICE_NAME,itemsList.get(i).getName()).apply();
                    sharepref.edit().putString(Const.PREF_STR_SERVICE_ID,itemsList.get(i).getId()).apply();

                    str_service_id=sharepref.getString(Const.PREF_STR_SERVICE_ID,"").toString();
                    //Log.d(str_service_id,str_service_id);
                    ServicesList.int_from=0;
                    ServicesList.int_to=10;
                    new ServicesList.GetServicesListDefault().execute();

                    for(int a=0;a<itemsList.size();a++){
                        if(a==i){
                            itemsList.get(a).setIs_selected(true);
                        }else{
                            itemsList.get(a).setIs_selected(false);
                        }
                        //Log.e("is_selectd---",i+""+itemsList.get(a).isIs_selected());
                    }
                   ServiceProvidersListScreenServiceListDataAdapter.this.notifyDataSetChanged();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }


    public class SingleItemRowHolder extends RecyclerView.ViewHolder {


        protected TextView tvTitle;

        protected ImageView itemImage;


        public SingleItemRowHolder(final View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);

        }

    }


}