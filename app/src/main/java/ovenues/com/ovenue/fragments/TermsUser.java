package ovenues.com.ovenue.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ovenues.com.ovenue.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TermsUser extends Fragment {


    public TermsUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_terms_user, container, false);
        final WebView web_termscondition = (WebView)view.findViewById(R.id.img_webview);

        web_termscondition.setWebChromeClient(new WebChromeClient());
        web_termscondition.getSettings().setJavaScriptEnabled(true);
        web_termscondition.getSettings().setDomStorageEnabled(true);

        web_termscondition.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view,String url) {
                return false;
            }
        });

        // get our html content
        String htmlAsString = getString(R.string.html);      // used by WebView
        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString); // used by TextView
        web_termscondition.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null);

        return  view;
    }

}