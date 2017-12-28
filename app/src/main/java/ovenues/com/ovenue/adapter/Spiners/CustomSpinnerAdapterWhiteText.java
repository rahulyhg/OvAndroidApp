package ovenues.com.ovenue.adapter.Spiners;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import ovenues.com.ovenue.R;


/**
 * Created by Shanni on 11/12/2016.
 */

public class CustomSpinnerAdapterWhiteText extends BaseAdapter implements SpinnerAdapter {

    private final Context activity;
    private String[] asr;

    public CustomSpinnerAdapterWhiteText(Context context, String[] asr) {
        this.asr=asr;
        activity = context;
    }



    public int getCount()
    {
        return asr.length;
    }

    public Object getItem(int i)
    {
        return asr[i];
    }

    public long getItemId(int i)
    {
        return (long)i;
    }



    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(activity);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(20);
        txt.setGravity(Gravity.CENTER_HORIZONTAL);
        txt.setText(asr[position]);
        txt.setBackgroundColor(Color.parseColor("#ffffff"));
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(activity);
        txt.setGravity(Gravity.LEFT);
        txt.setPadding(45, 20, 50, 20);//left,top,right,bottom
        txt.setTextSize(20);
        //txt.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.txt_spinner));
        txt.setCompoundDrawablesWithIntrinsicBounds(0 , 0 , R.drawable.dropdown_black, 0);
        txt.setText(asr[i]);
        txt.setTextColor(Color.parseColor("#767878"));
        return  txt;
    }

}