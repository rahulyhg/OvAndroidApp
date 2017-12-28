package ovenues.com.ovenue.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ovenues.com.ovenue.R;
import ovenues.com.ovenue.modelpojo.FinalBookingConfirmationModel;

/**
 * Created by Jay-Andriod on 03-Aug-17.
 */


public class FinalBookingConfirmationAdapter extends RecyclerView.Adapter<FinalBookingConfirmationAdapter.DataObject_postHolder> {
    static private ArrayList<FinalBookingConfirmationModel> mDataset;
    static private Context mContext;

    SharedPreferences sharepref;


    public static class DataObject_postHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_message,tv_title,tv_sub_title ;
        ImageView img_booking ;


        public DataObject_postHolder(final View itemView) {
            super(itemView);


            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_sub_title = (TextView) itemView.findViewById(R.id.tv_sub_title);

            img_booking  = (ImageView) itemView.findViewById(R.id.img_booking);


        }

        @Override
        public void onClick(View v) {
            //     myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

   /* public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }*/

    public FinalBookingConfirmationAdapter(ArrayList<FinalBookingConfirmationModel> myDataset, Context context) {
        mDataset = myDataset;
        mContext=context;
    }

    @Override
    public DataObject_postHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_booking_confirmation_list, parent, false);
        DataObject_postHolder dataObjectHolder = new DataObject_postHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObject_postHolder holder,final int position) {

        holder.setIsRecyclable(false);
        sharepref = mContext.getApplicationContext().getSharedPreferences("MyPref",mContext.MODE_PRIVATE);


        if(mDataset.get(position).getBooking_id()!=null){
            holder.img_booking.setImageResource(R.drawable.ic_right_success);
            holder.tv_title.setText(mDataset.get(position).getVendor());
            holder.tv_sub_title.setText(mDataset.get(position).getItem_name()+"\nBooking Id : "+mDataset.get(position).getBooking_id());
            holder.tv_message.setText("SUCCESS");
            holder.tv_message.setTextColor(ContextCompat.getColor(mContext,R.color.md_green_900));

        }else{
            holder.img_booking.setImageResource(R.drawable.ic_close_gray_24dp);
            holder.tv_title.setText(mDataset.get(position).getVendor());
            holder.tv_sub_title.setText(mDataset.get(position).getItem_name()+"\nError : "+mDataset.get(position).getError());
            holder.tv_message.setText("FAIL");
            holder.tv_message.setTextColor(ContextCompat.getColor(mContext,R.color.md_red_900));
        }




    }

    public void addItem(FinalBookingConfirmationModel dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}