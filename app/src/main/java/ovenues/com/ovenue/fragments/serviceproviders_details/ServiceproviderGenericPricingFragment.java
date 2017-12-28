package ovenues.com.ovenue.fragments.serviceproviders_details;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.ServiceProvidersDetailsMainFragment;
import ovenues.com.ovenue.cart.CartSummaryScreen;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.adapterGenericPricingAdapter;
import static ovenues.com.ovenue.ServiceProvidersDetailsMainFragment.sharepref;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceproviderGenericPricingFragment extends Fragment {



    public static RecyclerView mRecyclerViewGenricServiceProviderCharages;


    public ServiceproviderGenericPricingFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView =  inflater.inflate(R.layout.fragment_serviceprovider_generic_pricing, container, false);

        // Initialize recycler view
        mRecyclerViewGenricServiceProviderCharages = (RecyclerView)convertView.findViewById(R.id.rv_charges);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewGenricServiceProviderCharages.setLayoutManager(mLayoutManager);
        mRecyclerViewGenricServiceProviderCharages.setNestedScrollingEnabled(false);
        mRecyclerViewGenricServiceProviderCharages.setAdapter(adapterGenericPricingAdapter);


        return convertView;
    }

    boolean enabletotalItem =true;
    boolean enabletotalAmount =true;
    ImageView ic_cart_actionbar;
    private static final int MENU_VENUE = Menu.FIRST;
    private static final int MENU_CATERING= Menu.FIRST + 1;
    private static final int MENU_SERVICES = Menu.FIRST + 2;
    private static final int MENU_TOTALAMOUNT = Menu.FIRST + 3;
    private static final int MENU_LOGOUT = Menu.FIRST + 4;
    public static TextView tv_cartItemCount;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.cart_option, menu);
        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.cart_badge_layout);
        RelativeLayout notifCount = (RelativeLayout)   MenuItemCompat.getActionView(item);

        tv_cartItemCount= (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
        tv_cartItemCount.setText(sharepref.getString(Const.PREF_USER_CART_TOTAL_ITEMS,""));
        ic_cart_actionbar = (ImageView) notifCount.findViewById(R.id.ic_cart_actionbar);

        ic_cart_actionbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),CartSummaryScreen.class));
            }
        });

        if(enabletotalItem)
            menu.add(0, MENU_VENUE, Menu.NONE, "Venues   Bookings : "+sharepref.getString(Const.PREF_USER_CART_VENUES,"")).setIcon(R.drawable.fab_add);
        if(enabletotalItem)
            menu.add(0, MENU_CATERING, Menu.NONE,"Catering Bookings : "+sharepref.getString(Const.PREF_USER_CART_CATERINGS,"")).setIcon(R.drawable.fab_add);
        if(enabletotalItem)
            menu.add(0, MENU_SERVICES, Menu.NONE, "Services Bookings : "+sharepref.getString(Const.PREF_USER_CART_SERVICES,"")).setIcon(R.drawable.fab_add);

        if(sharepref.getString(Const.PREF_USER_CART_TOTAL_AMOUNT,"").equalsIgnoreCase("")){
            enabletotalAmount=false;
        }

        if(enabletotalAmount)
            menu.add(0, MENU_TOTALAMOUNT, Menu.NONE, "Total Amount    : $ "+Const.GLOBAL_FORMATTER.format(Double.parseDouble(sharepref.getString(Const.PREF_USER_CART_TOTAL_AMOUNT,"0.0")) ) ).setIcon(R.drawable.ic_collapse);



        super.onCreateOptionsMenu(menu,inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
          /*  case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                finish();
                return true;*/

            //noinspection SimplifiableIfStatement
            case R.id.badge :
                startActivity(new Intent(getActivity(),CartSummaryScreen.class));
                return true;

        /*    case R.id.action_custom_indicator:
                mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                break;
            case R.id.action_custom_child_animation:
                mDemoSlider.setCustomAnimation(new ChildAnimationExample());
                break;
            case R.id.action_restore_default:
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                break;
            case R.id.action_github:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/daimajia/AndroidImageSlider"));
                startActivity(browserIntent);
                break;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class VersionHelper
    {
        public static void refreshActionBarMenu(Activity activity)
        {
            activity.invalidateOptionsMenu();
        }
    }

}
