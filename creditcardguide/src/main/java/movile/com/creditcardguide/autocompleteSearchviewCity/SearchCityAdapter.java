package movile.com.creditcardguide.autocompleteSearchviewCity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import movile.com.creditcardguide.R;


/**
 * Created by Jay-Andriod on 13-Apr-17.
 */

public class SearchCityAdapter extends ArrayAdapter<SearchCityModel> implements Filterable {
    ArrayList<SearchCityModel> customers, tempCustomer, suggestions;

    public SearchCityAdapter(Context context, ArrayList<SearchCityModel> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.customers = objects;
        this.tempCustomer = new ArrayList<SearchCityModel>(objects);
        this.suggestions = new ArrayList<SearchCityModel>(objects);
        //Log.d("tempCustomer",tempCustomer.toString());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchCityModel SearchCityModel = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_city_list, parent, false);
        }
        TextView txtCity = (TextView) convertView.findViewById(R.id.tv_city_name);

        if (txtCity != null)
            txtCity.setText(SearchCityModel.getName() /*+ " " + SearchCityModel.getCounty()*/);

        // Now assign alternate color for rows
        /*if (position % 2 == 0)
        convertView.setBackgroundColor(getContext().getColor(R.color.odd));
        else
        convertView.setBackgroundColor(getContext().getColor(R.color.even));*/

        return convertView;
    }


    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            SearchCityModel SearchCityModel = (SearchCityModel) resultValue;
            return SearchCityModel.getName() /*+ " , " + SearchCityModel.getCounty()*/;
        }


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (SearchCityModel people : tempCustomer) {
                    if (people.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
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
            ArrayList<SearchCityModel> c = (ArrayList<SearchCityModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (SearchCityModel cust : c) {
                    add(cust);
                    notifyDataSetChanged();
                }
            }
        }
    };
}