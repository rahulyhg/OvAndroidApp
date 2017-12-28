package ovenues.com.ovenue.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import ovenues.com.ovenue.utils.Utils;


public class UserTextView extends TextView{

	public UserTextView(Context context) {
		super(context);
		this.setTypeface(Utils.getFont(context, Integer.parseInt(this.getTag().toString())));
	}

	public UserTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setTypeface(Utils.getFont(context, Integer.parseInt(this.getTag().toString())));
	}

	public UserTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setTypeface(Utils.getFont(context, Integer.parseInt(this.getTag().toString())));
	}

}
