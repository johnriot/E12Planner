package com.steo.europlanner;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

import com.inmobi.androidsdk.IMAdRequest;
import com.inmobi.androidsdk.IMAdView;

public class WebViewActivity extends Activity {

    public static final String URL_EXTRA = "URL";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.webview);

        IMAdView adView = (IMAdView) findViewById(R.id.adViewWebView);
        IMAdRequest adRequest = new IMAdRequest();
        adRequest.setTestMode(true);
        adView.setIMAdRequest(adRequest);
        adView.loadNewAd();

        WebView wv = (WebView)findViewById(R.id.webView);
        wv.getSettings().setUserAgentString("Android");

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            wv.loadUrl(extras.getString(URL_EXTRA));
        }
    }
}
