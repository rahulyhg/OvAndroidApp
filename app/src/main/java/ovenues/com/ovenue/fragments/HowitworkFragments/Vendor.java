package ovenues.com.ovenue.fragments.HowitworkFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ovenues.com.ovenue.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Vendor extends Fragment {


    public Vendor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView =  inflater.inflate(R.layout.fragment_vendor, container, false);

        return  convertView;
    }

}
