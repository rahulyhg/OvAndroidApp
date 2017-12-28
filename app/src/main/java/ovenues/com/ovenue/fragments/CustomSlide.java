package ovenues.com.ovenue.fragments;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import agency.tango.materialintroscreen.SlideFragment;
import ovenues.com.ovenue.R;

public class CustomSlide extends SlideFragment {
    private CheckBox checkBox;
    TextView tv_htmlText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_custom_slide, container, false);
        checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        tv_htmlText =(TextView)view.findViewById(R.id.tv_htmlText);

        String htmlAsString = getString(R.string.html);      // used by WebView
        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString); // used by TextView
        tv_htmlText.setText(htmlAsSpanned);

        return view;
    }

    @Override
    public int backgroundColor() {
        return R.color.custom_slide_background;
    }

    @Override
    public int buttonsColor() {
        return R.color.custom_slide_buttons;
    }

    @Override
    public boolean canMoveFurther() {
        return checkBox.isChecked();
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.error_message);
    }
}