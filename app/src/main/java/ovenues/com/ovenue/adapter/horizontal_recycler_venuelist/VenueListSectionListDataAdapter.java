package ovenues.com.ovenue.adapter.horizontal_recycler_venuelist;


import android.content.Context;
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

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.SVGImageLoader.SvgDecoder;
import ovenues.com.ovenue.SVGImageLoader.SvgDrawableTranscoder;
import ovenues.com.ovenue.SVGImageLoader.SvgSoftwareLayerSetter;
import ovenues.com.ovenue.modelpojo.HomeHoriRecyclerSingleItem;
import ovenues.com.ovenue.utils.Const;

public class VenueListSectionListDataAdapter extends RecyclerView.Adapter<VenueListSectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<HomeHoriRecyclerSingleItem> itemsList;
    private Context mContext;

    public VenueListSectionListDataAdapter(Context context, ArrayList<HomeHoriRecyclerSingleItem> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_recycle_raw_list_single_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        HomeHoriRecyclerSingleItem singleItem = itemsList.get(i);

        holder.tvTitle.setText(singleItem.getName());

        //Log.e("service icons---",/*Const.WEBSITE_PIC_URL+*/"http://13.56.92.252/"+itemsList.get(i).getUrl());
        /*Glide.with(mContext)
                .load(*//*Const.WEBSITE_PIC_URL+*//*"http://13.56.92.252/"+itemsList.get(i).getUrl())
                .fitCenter()
                .placeholder(R.drawable.loading_image_pic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                .into(holder.itemImage);*/

        GenericRequestBuilder<Uri,InputStream,SVG,PictureDrawable>
                requestBuilder = Glide.with(mContext)
                .using(Glide.buildStreamModelLoader(Uri.class, mContext), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder(50,50)))
                .decoder(new SvgDecoder(50,50))
                .placeholder(R.drawable.loading_image_pic)
                .error(R.drawable.loading_image_pic)
                .listener(new SvgSoftwareLayerSetter<Uri>());

        Uri uri = Uri.parse(itemsList.get(i).getUrl());
        requestBuilder
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .load(uri)
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
            tvTitle.setVisibility(View.GONE);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            itemImage.requestLayout();
            itemImage.getLayoutParams().height = 80;
            itemImage.getLayoutParams().width = 80;


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();

                }
            });


        }

    }

}