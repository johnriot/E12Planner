package com.neoware.europlanner;

import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.inmobi.androidsdk.IMAdRequest;
import com.inmobi.androidsdk.IMAdView;

public class WebViewActivity extends SherlockActivity {

    public static final String URL_EXTRA = "URL";

    /**
     * @brief This is our custom web view client that will force all redirects to stay
     * within the webview itself
     */
    private class LocalRedirectClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.webview);

        BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.toolbar_bg);
        bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(bg);
        bar.setTitle(R.string.news_title);
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayUseLogoEnabled(true);

        WebView wv = (WebView)findViewById(R.id.webView);
        wv.getSettings().setUserAgentString("Android");

        wv.setWebViewClient(new LocalRedirectClient());

        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setBuiltInZoomControls(true);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            wv.loadUrl(extras.getString(URL_EXTRA));
        }
    }

    @Override
    public void onResume() {

        super.onResume();

        IMAdView adView = (IMAdView) findViewById(R.id.adViewWebView);
        IMAdRequest adRequest = new IMAdRequest();
        adRequest.setTestMode(Settings.USE_TEST_ADS);
        adView.setIMAdRequest(adRequest);
        adView.loadNewAd();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        if(item.getItemId() == android.R.id.home) {

            Intent homeIntent = new Intent(this, PlannerHomeActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);

            return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

}
