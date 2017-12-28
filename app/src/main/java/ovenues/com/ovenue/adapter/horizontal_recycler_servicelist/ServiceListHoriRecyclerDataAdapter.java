package ovenues.com.ovenue.adapter.horizontal_recycler_servicelist;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.modelpojo.SectionDataModel;
import ovenues.com.ovenue.utils.Const;


public class ServiceListHoriRecyclerDataAdapter extends RecyclerView.Adapter<ServiceListHoriRecyclerDataAdapter.ItemRowHolder> {

    private ArrayList<SectionDataModel> dataList;
    private Context mContext;

    public ServiceListHoriRecyclerDataAdapter(Context context, ArrayList<SectionDataModel> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_recycle_raw_list_item, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder itemRowHolder, int i) {

        final String sectionName = dataList.get(i).getHeaderTitle();

        ArrayList singleSectionItems = dataList.get(i).getAllItemsInSection();

        if(dataList.get(i).getAllItemsInSection().get(i).getType().equalsIgnoreCase(Const.CONST_SERVICE)){
            itemRowHolder.itemTitle.setText("Services Offered");
        }else{
            itemRowHolder.itemTitle.setText(sectionName);
        }

        ServiceProvidersListScreenServiceListDataAdapter itemListDataAdapter = new ServiceProvidersListScreenServiceListDataAdapter(mContext, singleSectionItems);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);


         itemRowHolder.recycler_view_list.setNestedScrollingEnabled(false);

         if(dataList.get(i).getAllItemsInSection().get(i).getType().equalsIgnoreCase(Const.CONST_SERVICE)) {
             for (int a = 0; a < singleSectionItems.size(); a++) {
                 if (dataList.get(i).getAllItemsInSection().get(a).isIs_selected() == true) {
                     itemRowHolder.recycler_view_list.smoothScrollToPosition(a+2);
                 }
             }
         }
       // Log.e("got index_selectedSerType--",""+ index_selectedServiceType);

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

        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  if(itemRowHolder.recycler_view_list.getAdapter().getItemCount()>9){
                    itemRowHolder.recycler_view_list.smoothScrollToPosition(index_selectedServiceType);
                }else if(itemRowHolder.recycler_view_list.getAdapter().getItemCount()>5){
                    itemRowHolder.recycler_view_list.smoothScrollToPosition(5);
                }*/
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