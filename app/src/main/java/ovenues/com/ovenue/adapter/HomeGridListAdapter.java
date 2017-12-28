package ovenues.com.ovenue.adapter;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import ovenues.com.ovenue.ServiceProvidersDetailsMainFragment;
import ovenues.com.ovenue.VenueDetailsMainFragment;
import ovenues.com.ovenue.modelpojo.IdNameUrlGrideRaw;
import ovenues.com.ovenue.R;
import ovenues.com.ovenue.utils.Const;

import static android.content.Context.MODE_PRIVATE;

public class HomeGridListAdapter extends BaseAdapter {

    private ArrayList<IdNameUrlGrideRaw> vault_items;
    private Activity context;
    private LayoutInflater inflater;
    SharedPreferences sharepref;



    public HomeGridListAdapter(Activity context, ArrayList<IdNameUrlGrideRaw> vault_items) {
        this.context = context;
        this.vault_items = vault_items;
        this.inflater = LayoutInflater.from(context);
        sharepref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return vault_items.size();
    }

    @Override
    public Object getItem(int position) {
        return vault_items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.vendor_grid_raw, null, false);
            holder = new ViewHolder();

            holder.linear = (LinearLayout) convertView.findViewById(R.id.layout);
            holder.icon = (ImageView) convertView.findViewById(R.id.mainicon);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.details = (TextView) convertView.findViewById(R.id.details);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Glide.with(context)
                .load(Const.WEBSITE_PIC_URL + "/" + (vault_items.get(position).getUrl()))
                .asBitmap()
                                /*.error(R.drawable.no_image)*/
                .placeholder(R.drawable.loading_image_pic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        // Do something with bitmap here.
                        holder.icon.setImageBitmap(bitmap);
                        //Log.e("GalleryAdapter","Glide getLarge ");
                    }
                });

        holder.title.setText(vault_items.get(position).getTitle());
        if(vault_items.get(position).getDescription().length()>150){
            holder.details.setText(vault_items.get(position).getDescription().substring(0,150)+"...");
        }else{
            holder.details.setText(vault_items.get(position).getDescription());
        }

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vault_items.get(position).getIs_venue().equalsIgnoreCase("1")){

                    context.startActivity(new Intent(context, VenueDetailsMainFragment.class)
                            .putExtra("venue_name",vault_items.get(position).getTitle())
                            .putExtra("venue_id",vault_items.get(position).getId()));
                }else if(!vault_items.get(position).getService_id().equalsIgnoreCase("0")){
                    sharepref.edit().putString(Const.PREF_STR_SERVICE_ID,vault_items.get(position).getService_id());
                    context.startActivity(new Intent(context, ServiceProvidersDetailsMainFragment.class)
                            .putExtra("service_provider_id",vault_items.get(position).getId()));

                }/*else if(vault_items.get(position).getService_id().equalsIgnoreCase("2")){

                    sharepref.edit().putString(Const.PREF_STR_SERVICE_ID,vault_items.get(position).getService_id());
                    context.startActivity(new Intent(context, ServiceProvidersDetailsMainFragment.class)
                            .putExtra("service_provider_id",vault_items.get(position).getId()));

                }else if(vault_items.get(position).getService_id().toString().length()>=1){

                    sharepref.edit().putString(Const.PREF_STR_SERVICE_ID,vault_items.get(position).getService_id());
                    context.startActivity(new Intent(context, ServiceProvidersDetailsMainFragment.class)
                            .putExtra("service_provider_id",vault_items.get(position).getId()));

                }*/
            }
        });


        return convertView;
    }

    static class ViewHolder {
        LinearLayout linear;
        ImageView  icon;
        TextView  title,details;
    }

       private  int getScreenWidth()
       {
           Display display = context.getWindowManager().getDefaultDisplay();
           int width = display.getWidth();
           return  width;
       }

}
