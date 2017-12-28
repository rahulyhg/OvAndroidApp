package ovenues.com.ovenue.adapter.horizontal_recycler_home;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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
import java.net.URLEncoder;
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
import static ovenues.com.ovenue.ServicesList.str_service_id;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<HomeHoriRecyclerSingleItem> itemsList;
    private Context mContext;
    SharedPreferences sharepref;
    String screen_name;


    public SectionListDataAdapter(Context context, ArrayList<HomeHoriRecyclerSingleItem> itemsList,String screen_name) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.screen_name=screen_name;

        sharepref = mContext.getSharedPreferences("MyPref", MODE_PRIVATE);
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = null;
        if(screen_name.equalsIgnoreCase("home")){
             v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_recycle_raw_list_single_card, null);
        }else{
             v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.venudetails_recycle_raw_list_single_card, null);

        }
         SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder,final int i) {
        holder.setIsRecyclable(false);

        final HomeHoriRecyclerSingleItem singleItem = itemsList.get(i);




        if (screen_name.equalsIgnoreCase("venue_details")) {
            holder.tvTitle.setTextSize(8);
            android.view.ViewGroup.LayoutParams layoutParams = holder.itemImage.getLayoutParams();
            layoutParams.width = 120;
            layoutParams.height = 120;
            holder.itemImage.setLayoutParams(layoutParams);
        }


        holder.tvTitle.setText(singleItem.getName());
        android.view.ViewGroup.LayoutParams layoutParamstv = holder.tvTitle.getLayoutParams();
        layoutParamstv.width = 120;
        layoutParamstv.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        holder.tvTitle.setLayoutParams(layoutParamstv);

        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Glide.with(mContext)
                .load(itemsList.get(i).getUrl())
                .placeholder(R.drawable.loading_image_pic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_noimage_placeholder)
                .into(holder.itemImage);

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle;

        protected ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();

                }
            });


        }

    }

}