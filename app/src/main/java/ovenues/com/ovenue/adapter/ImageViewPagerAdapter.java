package ovenues.com.ovenue.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import ovenues.com.ovenue.R;

/**
 
 */
public class ImageViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    ArrayList<String> mResourcesDynamic = new ArrayList<String>();


    public ImageViewPagerAdapter(Context mContext, ArrayList<String> mResourcesDynamic ) {
        this.mContext = mContext;
        this.mResourcesDynamic = mResourcesDynamic;
    }

    @Override
    public int getCount() {
        return mResourcesDynamic.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
       /* imageView.setImageResource(mResourcesDynamic[position]);;*/

        Glide.with(mContext)
                .load(mResourcesDynamic.get(position))
                .asBitmap()
                .placeholder(R.drawable.loading_image_pic)
                /*.error(R.drawable.no_image)*/
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        // Do something with bitmap here.
                        imageView.setImageBitmap(bitmap);
                        //Log.e("GalleryAdapter","Glide getMedium ");

                        Glide.with(mContext)
                                .load(mResourcesDynamic.get(position))
                                .asBitmap()/*
                                .error(R.drawable.no_image)*/
                                .placeholder(R.drawable.loading_image_pic)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                        // Do something with bitmap here.
                                        imageView.setImageBitmap(bitmap);
                                        //Log.e("GalleryAdapter","Glide getLarge ");
                                    }
                                });
                    }
                });


       /* imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position==1){
                    Toast.makeText(container.getContext(),"Comming Soon . . .",Toast.LENGTH_LONG).show();
                }else if(position==2){
                    container.getContext().startActivity(new Intent(container.getContext(),Allinone.class));
                }
            }
        });*/

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}