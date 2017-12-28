package ovenues.com.ovenue;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import ovenues.com.ovenue.fragments.HowitworkFragments.User;
import ovenues.com.ovenue.fragments.HowitworkFragments.Vendor;

import static ovenues.com.ovenue.HowitWorkTab.TabType.ICONS_ONLY;

public class HowitWorkTab extends AppCompatActivity {


    public static FragmentManager fm;
    static Activity activity;
    public static SharedPreferences sharepref;

    private ViewPager viewPagerTab;
    private TabLayout tabLayout;
    ViewPagerAdapter adapterTab ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howit_work_tab);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark,getTheme()));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("How it work ?");

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        fm = getSupportFragmentManager();


        activity = this;
        tabType = TabType.DEFAULT;//(TabType) getIntent().getSerializableExtra(TAB_TYPE);//get the type of tab


        //====VIEW PAGETR ADAPTER AUTO IMAGE SCROLLING ===================
        adapterTab = new ViewPagerAdapter(getSupportFragmentManager());


        viewPagerTab = (ViewPager)this.findViewById(R.id.viewPager);
        viewPagerTab.setAdapter(adapterTab);
        setupViewPager(viewPagerTab);


        tabLayout = (TabLayout)this.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPagerTab);//setting tab over viewpager

        //Implementing tab selected listener over tablayout
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerTab.setCurrentItem(tab.getPosition());//setting current selected item over viewpager
                switch (tab.getPosition()) {

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        //Call tab type method
        onTabType();
    }



    private TabType tabType;
    public enum  TabType {
        DEFAULT, ICON_TEXT,ICONS_ONLY,CUSTOM;
    }

    //Setting View Pager
    private void setupViewPager(ViewPager viewPager) {

        adapterTab.addFrag(new User(), "Host");
        adapterTab.addFrag(new Vendor(), "Vendor");
        adapterTab.notifyDataSetChanged();


    }


    /**  on the basis of tab type call respective method **/
    private void onTabType() {
        switch (tabType) {
            case DEFAULT:
                //don't do anything here
                break;
            case ICON_TEXT:
            case ICONS_ONLY:
                //for both Types call set Icons method
                //tabWithIcon();
                break;
            case CUSTOM:
                //Call custom tab method
                //setUpCustomTabs();
                break;
        }
    }


    //View Pager fragments setting adapter class
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();//fragment arraylist
        private final List<String> mFragmentTitleList = new ArrayList<>();//title arraylist

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        //adding fragments and title method
        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (tabType == ICONS_ONLY)
                return "";
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
