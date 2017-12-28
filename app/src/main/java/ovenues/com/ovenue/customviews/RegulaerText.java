package ovenues.com.ovenue.customviews;

/**
 * Created by Testing on 18-Oct-16.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class RegulaerText extends TextView {


    public RegulaerText(Context context) {
        super(context);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "Montserrat-Bold.ttf");
        this.setTypeface(face);
    }

    public RegulaerText(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "Montserrat-Bold.ttf");
        this.setTypeface(face);
    }

    public RegulaerText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "Montserrat-Bold.ttf");
        this.setTypeface(face);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);


    }

}