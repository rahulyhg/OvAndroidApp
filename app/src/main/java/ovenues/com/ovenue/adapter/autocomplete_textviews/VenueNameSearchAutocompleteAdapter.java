package ovenues.com.ovenue.adapter.autocomplete_textviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.signature.StringSignature;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.util.ArrayList;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.SVGImageLoader.SvgDecoder;
import ovenues.com.ovenue.SVGImageLoader.SvgDrawableTranscoder;
import ovenues.com.ovenue.SVGImageLoader.SvgSoftwareLayerSetter;
import ovenues.com.ovenue.modelpojo.autocompleteSearchviewCity.SearchVenueSPCityModel;

/**
 * Created by Jay-Andriod on 02-Jun-17.
 */

public class VenueNameSearchAutocompleteAdapter extends ArrayAdapter<SearchVenueSPCityModel> {

    Context context;
    int resource, textViewResourceId;
    ArrayList<SearchVenueSPCityModel> items, tempItems, suggestions;
    //ArrayList<SearchVenueSPCityModel> searchcitymodel;

    public VenueNameSearchAutocompleteAdapter(Context context, int resource, int textViewResourceId, ArrayList<SearchVenueSPCityModel> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<SearchVenueSPCityModel>(items); // this makes the difference.
        suggestions = new ArrayList<SearchVenueSPCityModel>();
//        Log.d("item details",items.get(1).getName().toString());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_image_textview, parent, false);
        }
        SearchVenueSPCityModel people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.row_text_view_only);
            ImageView img_hint =(ImageView)view.findViewById(R.id.img_hint);

            if (lblName != null){
                lblName.setText(people.getName());
                lblName.setTextColor(Color.BLACK);
            }
            //Log.d("filter flag name=", people.getName());

            if(people.getServiceID().equalsIgnoreCase("0")){

                Glide.with(context)
                        .load(R.drawable.ic_map_grey_verysmall)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .crossFade()
                        .dontAnimate()
                        .skipMemoryCache(true)
                        .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                        .error(R.drawable.ic_map_grey_verysmall)
                        .into(img_hint);
            }else {

                String ImageURL=null;
                if(people.getServiceID().equalsIgnoreCase("2")){
                    ImageURL = "https://s3-us-west-1.amazonaws.com/ovenues-services-icons/svg/icon-cake.svg";
                }else{
                    ImageURL=people.getSvg_icon_url().toString();
                }


                Log.e("image URL----",ImageURL);
                //https://s3-us-west-1.amazonaws.com/ovenues-services-icons/svg/icon-catering.svg

                GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable>
                        requestBuilder = Glide.with(context)
                        .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                        .from(Uri.class)
                        .as(SVG.class)
                        .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                        .sourceEncoder(new StreamEncoder())
                        .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder(50, 50)))
                        .decoder(new SvgDecoder(50, 50))
                        .placeholder(R.drawable.loading_image_pic)
                        .error(R.drawable.loading_image_pic)
                        .listener(new SvgSoftwareLayerSetter<Uri>());

                Uri uri = Uri.parse(ImageURL);
                requestBuilder
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .load(uri)
                        .into(img_hint);
            }



        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((SearchVenueSPCityModel) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (SearchVenueSPCityModel people : tempItems) {
                    if (people.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<SearchVenueSPCityModel> filterList = (ArrayList<SearchVenueSPCityModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (SearchVenueSPCityModel people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}