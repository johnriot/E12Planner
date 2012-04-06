package com.steo.europlanner;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;

import com.inmobi.androidsdk.IMAdRequest;
import com.inmobi.androidsdk.IMAdView;
import com.steo.europlanner.FeedsAdapter.FeedWrapper;
import com.steo.rss.RSSFeed;
import com.steo.rss.RSSItem;
import com.steo.rss.RSSReader;
import com.steo.rss.RSSReaderException;

public class NewsActivity extends Activity {

    private static final String UEFA_RSS =
            "http://www.uefa.com/rssfeed/uefaeuro2012/rss.xml";
    private static final String UEFA_RSS_DESC = "UEFA Official Feed";
    private static final int UEFA_ICON_RESID = R.drawable.uefa;

    private static final String BBC_RSS =
            "http://feeds.bbci.co.uk/sport/0/football/rss.xml";
    private static final String BBC_RSS_DESC = "BBC Official Feed";
    private static final int BBC_ICON_RESID = R.drawable.bbcicon;

    private static final class FeedDefn {

        public String url;
        public String description;
        public boolean loaded;
        public int iconId;

        public FeedDefn(String url, String description, int iconId) {
            this.url = url;
            this.description = description;
            this.iconId = iconId;
        }
    }

    private final FeedDefn[] mFeeds = {
            new FeedDefn(UEFA_RSS, UEFA_RSS_DESC, UEFA_ICON_RESID),
            new FeedDefn(BBC_RSS, BBC_RSS_DESC, BBC_ICON_RESID)
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.news_layout);

        IMAdView adView = (IMAdView) findViewById(R.id.adViewNews);
        IMAdRequest adRequest = new IMAdRequest();
        adRequest.setTestMode(true);
        adView.setIMAdRequest(adRequest);
        adView.loadNewAd();

        ImageButton homeButton = (ImageButton)findViewById(R.id.newsHomeButton);
        homeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final ExpandableListView listView = (ExpandableListView)
                findViewById(R.id.newsList);

        String loadingNews = getResources().getString(R.string.loadingNews);
        final ProgressDialog progress = ProgressDialog.show(this, "", loadingNews, true);

        final FeedsAdapter adapter = new FeedsAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {

                ArrayList<FeedWrapper> feeds = adapter.getFeeds();
                Intent webviewIntent = new Intent(NewsActivity.this,
                        WebViewActivity.class);
                RSSItem item = feeds.get(groupPosition).feed.getItems().
                        get(childPosition);

                webviewIntent.putExtra(WebViewActivity.URL_EXTRA,
                        item.getLink().toString());

                startActivity(webviewIntent);
                return true;
            }
        });

        AsyncReader.ReaderCompleteCallback readerCallback =
                new AsyncReader.ReaderCompleteCallback() {

            @Override
            public void onReaderComplete(RSSFeed feed, String errorMessage,
                    String feedDescription, int iconId) {
                adapter.addFeed(new FeedWrapper(feedDescription, feed, iconId));
                if(allFeedsLoaded()) {
                    progress.dismiss();
                }
            }
        };

        for(FeedDefn feed : mFeeds) {
            AsyncReader reader = new AsyncReader(readerCallback, feed);
            reader.execute();
        }
    }

    private static class AsyncReader extends AsyncTask<String, Void, RSSFeed> {

        public interface ReaderCompleteCallback {
            public void onReaderComplete(RSSFeed feed, String errorMessage,
                    String feedDescription, int iconId);
        }

        private final ReaderCompleteCallback mReaderComplete;

        private String mErrorMessage;
        private RSSFeed mFeed;
        private final FeedDefn mFeedDefn;

        public AsyncReader(ReaderCompleteCallback callback, FeedDefn defn) {
            mReaderComplete = callback;
            mFeedDefn = defn;
        }

        @Override
        protected RSSFeed doInBackground(String... params) {
            RSSReader reader = new RSSReader();
            try {
                mFeed = reader.load(mFeedDefn.url);
                return mFeed;
            } catch (RSSReaderException ex) {
                mErrorMessage = ex.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(RSSFeed result) {
            super.onPostExecute(result);
            mFeedDefn.loaded = true;
            mReaderComplete.onReaderComplete(mFeed, mErrorMessage,
                    mFeedDefn.description, mFeedDefn.iconId);
        }
    }

    private boolean allFeedsLoaded() {
        for(FeedDefn feed : mFeeds) {
            if(!feed.loaded) return false;
        }

        return true;
    }
}
