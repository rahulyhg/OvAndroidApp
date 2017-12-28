package ovenues.com.ovenue.adapter;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.modelpojo.IdNameUrlGrideRaw;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.VenuesSearchFilter.str_pref_amenities_id;

public class AmenitiesGridListAdapter extends BaseAdapter {

    private ArrayList<IdNameUrlGrideRaw> vault_items;
    private Activity context;
    private LayoutInflater inflater;
    String str_scree_name;

    public AmenitiesGridListAdapter(Activity context, ArrayList<IdNameUrlGrideRaw> vault_items,String screen_name) {
        this.context = context;
        this.vault_items = vault_items;
        this.inflater = LayoutInflater.from(context);
        this.str_scree_name=screen_name;

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

            convertView = inflater.inflate(R.layout.row_withcheckbox_amenities_grid, null, false);

            holder = new ViewHolder();

            holder.linear = (LinearLayout) convertView.findViewById(R.id.layout);
            holder.icon = (ImageView) convertView.findViewById(R.id.mainicon);
            holder.title = (CheckBox) convertView.findViewById(R.id.cb_title);

            holder.title.setClickable(false);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int width =  (getScreenWidth() - 21)/3;

       // holder.linear.setLayoutParams(new LinearLayout.LayoutParams(width , width-15));

        //holder.linear.setBackgroundColor(Color.parseColor(vault_items.get(position).getColors()));
        //https://s3-us-west-1.amazonaws.com/ovenues-amenities/orange/amenity_icon_[amenity_id].png

        Glide.with(context).load(Const.AMAZON_AMENITIES_IMAGE_URL+vault_items.get(position).getId()+".png")
                .placeholder(R.drawable.loading_image_pic)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into( holder.icon);

        holder.title.setText(vault_items.get(position).getTitle());

        if(str_scree_name.equalsIgnoreCase("venuefilter")){
            holder.title.setGravity(Gravity.LEFT|Gravity.CENTER);
        }else {
            holder.title.setChecked(true);
            convertView.setClickable(false);
            convertView.setEnabled(false);

            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(200, 200);
            layoutParams.gravity=Gravity.CENTER;
           holder.icon.setLayoutParams(layoutParams);

            holder.title.setGravity(Gravity.CENTER);
            Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
            holder.title.setButtonDrawable(transparentDrawable);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!holder.title.isChecked()){
                    holder.title.setChecked(true);
                    str_pref_amenities_id = str_pref_amenities_id+","+vault_items.get(position).getId();
                }else{
                    holder.title.setChecked(false);
                    str_pref_amenities_id=str_pref_amenities_id.replace(vault_items.get(position).getId().toString(),"");

                    if(str_pref_amenities_id.contains(vault_items.get(position).getId())){

                    }
                }
                //Log.e("Amenitites===",str_pref_amenities_id);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        LinearLayout linear;
        ImageView  icon;
        CheckBox  title;
    }

       private  int getScreenWidth()
       {
           Display display = context.getWindowManager().getDefaultDisplay();
           int width = display.getWidth();
           return  width;
       }

}
