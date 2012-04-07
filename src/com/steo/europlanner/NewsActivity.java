package com.steo.europlanner;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.inmobi.androidsdk.IMAdRequest;
import com.inmobi.androidsdk.IMAdView;
import com.steo.europlanner.FeedsAdapter.FeedDefn;
import com.steo.rss.RSSItem;
import com.steo.rss.RSSReader;
import com.steo.rss.RSSReaderException;

public class NewsActivity extends SherlockActivity {

    private static final String UEFA_RSS =
            "http://www.uefa.com/rssfeed/uefaeuro2012/rss.xml";
    private static final String UEFA_RSS_DESC = "UEFA Official Feed";
    private static final int UEFA_ICON_RESID = R.drawable.uefa;

    private static final String BBC_RSS =
            "http://feeds.bbci.co.uk/sport/0/football/rss.xml";
    private static final String BBC_RSS_DESC = "BBC Official Feed";
    private static final int BBC_ICON_RESID = R.drawable.bbcicon;

    private static final FeedDefn[] mFeeds = {
            new FeedDefn(UEFA_RSS, UEFA_RSS_DESC, UEFA_ICON_RESID),
            new FeedDefn(BBC_RSS, BBC_RSS_DESC, BBC_ICON_RESID)
    };

    private MenuItem mRefreshItem;
    private FeedsAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.news_layout);

        BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.toolbar_bg);
        bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(bg);
        bar.setTitle(R.string.news_title);
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayUseLogoEnabled(true);

        IMAdView adView = (IMAdView) findViewById(R.id.adViewNews);
        IMAdRequest adRequest = new IMAdRequest();
        adRequest.setTestMode(true);
        adView.setIMAdRequest(adRequest);
        adView.loadNewAd();

        final ExpandableListView listView = (ExpandableListView)
                findViewById(R.id.newsList);

        String loadingNews = getResources().getString(R.string.loadingNews);
        mProgressDialog = ProgressDialog.show(this, "", loadingNews, true);

        mAdapter = new FeedsAdapter(this);
        listView.setAdapter(mAdapter);

        listView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {

                ArrayList<FeedDefn> feeds = mAdapter.getFeeds();
                Intent webviewIntent = new Intent(NewsActivity.this,
                        WebViewActivity.class);
                RSSItem item = feeds.get(groupPosition).rssFeed.getItems().
                        get(childPosition);

                webviewIntent.putExtra(WebViewActivity.URL_EXTRA,
                        item.getLink().toString());

                startActivity(webviewIntent);
                return true;
            }
        });

        loadFeeds();
    }

    private static class AsyncReader extends AsyncTask<String, Void, FeedDefn> {

        public interface ReaderCompleteCallback {
            public void onReaderComplete(FeedDefn feed, String errorMessage);
        }

        private final ReaderCompleteCallback mReaderComplete;

        private String mErrorMessage;
        private final FeedDefn mFeedDefn;

        public AsyncReader(ReaderCompleteCallback callback, FeedDefn defn) {
            mReaderComplete = callback;
            mFeedDefn = defn;
        }

        @Override
        protected FeedDefn doInBackground(String... params) {
            RSSReader reader = new RSSReader();
            try {
                mFeedDefn.rssFeed = reader.load(mFeedDefn.url);
                return mFeedDefn;
            } catch (RSSReaderException ex) {
                mErrorMessage = ex.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(FeedDefn result) {
            super.onPostExecute(result);
            mFeedDefn.loaded = true;
            mReaderComplete.onReaderComplete(result, mErrorMessage);
        }
    }

    private boolean allFeedsLoaded() {
        for(FeedDefn feed : mFeeds) {
            if(!feed.loaded) return false;
        }

        return true;
    }

    private void loadFeeds() {

        AsyncReader.ReaderCompleteCallback readerCallback =
                new AsyncReader.ReaderCompleteCallback() {

            @Override
            public void onReaderComplete(FeedDefn feed, String errorMessage) {
                mAdapter.addFeed(feed);
                if(allFeedsLoaded()) {
                    mProgressDialog.dismiss();
                }
            }
        };

        for(FeedDefn feed : mFeeds) {
            if(!feed.loaded) {
                AsyncReader reader = new AsyncReader(readerCallback, feed);
                reader.execute();
            }
            else {
                mAdapter.addFeed(feed);
            }
        }

        if(allFeedsLoaded()) mProgressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        mRefreshItem = menu.add(R.string.refresh_text)
            .setIcon(R.drawable.ic_refresh_dark);
        mRefreshItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        else if(item == mRefreshItem) {

            String loadingNews = getResources().getString(R.string.loadingNews);
            mProgressDialog = ProgressDialog.show(this, "", loadingNews, true);
            mAdapter.clearFeeds();

            for(FeedDefn feed : mFeeds) feed.loaded = false;
            loadFeeds();
        }

        return super.onMenuItemSelected(featureId, item);
    }

}
