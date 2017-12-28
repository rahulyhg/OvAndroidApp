package ovenues.com.ovenue.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import ovenues.com.ovenue.utils.Utils;


public class UserAutoCompleteText extends AutoCompleteTextView{

	public UserAutoCompleteText(Context context) {
		super(context);
		this.setTypeface(Utils.getFont(context, Integer.parseInt(this.getTag().toString())));
	}

	public UserAutoCompleteText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setTypeface(Utils.getFont(context, Integer.parseInt(this.getTag().toString())));
	}

	public UserAutoCompleteText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setTypeface(Utils.getFont(context, Integer.parseInt(this.getTag().toString())));
	}

}
