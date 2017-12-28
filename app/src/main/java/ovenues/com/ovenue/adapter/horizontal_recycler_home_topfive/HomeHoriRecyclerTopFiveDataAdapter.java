package ovenues.com.ovenue.adapter.horizontal_recycler_home_topfive;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ovenues.com.ovenue.MainNavigationScreen;
import ovenues.com.ovenue.R;
import ovenues.com.ovenue.ServicesList;
import ovenues.com.ovenue.VenuesList;
import ovenues.com.ovenue.modelpojo.SectionDataModel;
import ovenues.com.ovenue.utils.Const;

import static android.content.Context.MODE_PRIVATE;
import static ovenues.com.ovenue.ServicesList.str_service_id;

public class HomeHoriRecyclerTopFiveDataAdapter extends RecyclerView.Adapter<HomeHoriRecyclerTopFiveDataAdapter.ItemRowHolder> {

    private ArrayList<SectionDataModel> dataList;
    private Context mContext;
    SharedPreferences sharepref;
    String screen_name;

    public HomeHoriRecyclerTopFiveDataAdapter(Context context, ArrayList<SectionDataModel> dataList, String screen_name) {
        this.dataList = dataList;
        this.mContext = context;
        this.screen_name=screen_name;
        sharepref = mContext.getSharedPreferences("MyPref", MODE_PRIVATE);
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_recycle_raw_list_topfiveitem, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder itemRowHolder, final int i) {
        itemRowHolder.setIsRecyclable(false);
        final String sectionName = dataList.get(i).getHeaderTitle();

        final ArrayList singleSectionItems = dataList.get(i).getAllItemsInSection();

        itemRowHolder.itemTitle.setText(sectionName);
        itemRowHolder.itemTitle.setTextSize(21);
        itemRowHolder.itemTitle.setTag(200);
        RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(20,5,0,5);
        itemRowHolder.itemTitle.setLayoutParams(llp);
        itemRowHolder.itemTitle.setGravity(Gravity.LEFT);

        final SectionListDataTopFiveAdapter itemListDataAdapter = new SectionListDataTopFiveAdapter(mContext, singleSectionItems,screen_name);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);


         itemRowHolder.recycler_view_list.setNestedScrollingEnabled(false);


       /*  itemRowHolder.recycler_view_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        //Allow ScrollView to intercept touch events once again.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                // Handle RecyclerView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });*/

       if(screen_name.equalsIgnoreCase("home")){
           itemRowHolder.btnMore.setVisibility(View.VISIBLE);
           itemRowHolder.itemTitle.setVisibility(View.VISIBLE);
       }
        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dataList.get(i).getHeaderTitle().equalsIgnoreCase("Venues")) {
                    Intent venulist = new Intent(mContext, VenuesList.class);
                    mContext.startActivity(venulist);
                }else{
                    sharepref.edit().putString(Const.PREF_STR_SERVICE_NAME, dataList.get(i).getHeaderTitle().toString()).commit();
                    sharepref.edit().putString(Const.PREF_STR_SERVICE_ID, dataList.get(i).getAllItemsInSection().get(i).getService_id().toString()).commit();

                    //str_service_id = sharepref.getString(Const.PREF_STR_SERVICE_ID, "").toString();
                    Log.e("clicked service ID",sharepref.getString(Const.PREF_STR_SERVICE_ID, "").toString());
                    mContext.startActivity(new Intent(mContext, ServicesList.class)
                            .putExtra("index_selectedServiceType", "0"));
                }
                //Toast.makeText(v.getContext(), "click event on more, "+sectionName , Toast.LENGTH_SHORT).show();



            }
        });

       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;

        protected RecyclerView recycler_view_list;

        protected Button btnMore;

        public ItemRowHolder(View view) {
            super(view);
            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.btnMore= (Button) view.findViewById(R.id.btnMore);
        }

    }

}