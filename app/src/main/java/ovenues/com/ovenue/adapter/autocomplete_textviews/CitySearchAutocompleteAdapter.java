package ovenues.com.ovenue.adapter.autocomplete_textviews;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.modelpojo.autocompleteSearchview.SearchCityModel;

/**
 * Created by Jay-Andriod on 02-Jun-17.
 */

public class CitySearchAutocompleteAdapter extends ArrayAdapter<SearchCityModel> {

    Context context;
    int resource, textViewResourceId;
    ArrayList<SearchCityModel> items, tempItems, suggestions;
    //ArrayList<SearchVenueSPCityModel> searchcitymodel;

    public CitySearchAutocompleteAdapter(Context context, int resource, int textViewResourceId, ArrayList<SearchCityModel> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<SearchCityModel>(items); // this makes the difference.
        suggestions = new ArrayList<SearchCityModel>();
//        Log.d("item details",items.get(1).getName().toString());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_only_autocomplete_textview ,parent, false);
        }
        SearchCityModel people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.row_text_view_only);
            if (lblName != null){
                lblName.setText(people.getName());
                lblName.setTextColor(Color.BLACK);
            }
            Log.d("name=", people.getName());
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
            String str = ((SearchCityModel) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (SearchCityModel people : tempItems) {
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
            ArrayList<SearchCityModel> filterList = (ArrayList<SearchCityModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (SearchCityModel people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}