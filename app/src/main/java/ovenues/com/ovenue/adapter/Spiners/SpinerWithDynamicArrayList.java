package ovenues.com.ovenue.adapter.Spiners;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.modelpojo.Spiners.SearchVenueSpiners;

/**
 * Created by Jay-Andriod on 07-Apr-17.
 */
public class SpinerWithDynamicArrayList extends ArrayAdapter<SearchVenueSpiners> {

    LayoutInflater inflater;
    ArrayList<SearchVenueSpiners> objects;
    ViewHolder holder = null;
    Context context;
    public SpinerWithDynamicArrayList(Context context, int textViewResourceId, ArrayList<SearchVenueSpiners> objects) {
        super(context, textViewResourceId, objects);

        this.context = context;
        inflater = ((Activity) context).getLayoutInflater();
        this.objects = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SearchVenueSpiners listItem = objects.get(position);
        View row = convertView;

        if (null == row) {
            holder = new ViewHolder();
            row = inflater.inflate(R.layout.row_spiners_venue_search_filter, parent , false);
            holder.text1 = (TextView) row.findViewById(R.id.subtitle);
            holder.imgThumb = (ImageView)row.findViewById(R.id.thumb);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.text1.setTextColor(Color.parseColor("#000000"));

        holder.text1.setText(listItem.getType());

        return row;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        SearchVenueSpiners listItem = objects.get(position);
        View row = convertView;

        if (null == row) {
            holder = new ViewHolder();
            row = inflater.inflate(R.layout.row_spiners_venue_search_filter, parent , false);
            holder.text1 = (TextView) row.findViewById(R.id.subtitle);
            holder.imgThumb = (ImageView)row.findViewById(R.id.thumb);
            holder.imgThumb.setVisibility(View.GONE);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.text1.setText(listItem.getType());


        return row;
    }

    static class ViewHolder {
        TextView  text1;
        ImageView imgThumb;
    }
}
