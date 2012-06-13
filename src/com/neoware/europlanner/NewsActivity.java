package com.neoware.europlanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.neoware.europlanner.FeedsAdapter.FeedDefn;
import com.neoware.rss.RSSFault;
import com.neoware.rss.RSSItem;
import com.neoware.rss.RSSReader;
import com.neoware.rss.RSSReaderException;

public class NewsActivity extends SherlockActivity {

    private MenuItem mRefreshItem;
    private FeedsAdapter mAdapter;

    //TODO: Removing progress dialog for now. People can then read the loaded news
    //       while other news loads. Going to leave it just commented for now in case
    //       we need it again
    //private ProgressDialog mProgressDialog;
    private TournamentDefinition mDefn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDefn = TournamentDefinition.getTournamentDefnInstance(this);

        setContentView(R.layout.news_layout);

        BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.toolbar_bg);
        bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(bg);
        bar.setTitle(R.string.news_title);
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayUseLogoEnabled(true);

        final ExpandableListView listView = (ExpandableListView)
                findViewById(R.id.newsList);

        //String loadingNews = getResources().getString(R.string.loadingNews);
        //mProgressDialog = ProgressDialog.show(this, "", loadingNews, true);

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

    @Override
    public void onResume() {

        super.onResume();

        if(Settings.USE_LIVE_ADS) {
            AdView adview = (AdView)findViewById(R.id.adViewNews);
            Assert.assertNotNull(adview);

            AdRequest req = new AdRequest();

            String [] keywords = getResources().getStringArray(R.array.adKeywords);
            Set<String> keywordsSet = new HashSet<String>(Arrays.asList(keywords));
            req.setKeywords(keywordsSet);

            adview.loadAd(req);
        }
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
            } catch (RSSFault ex) {
                mErrorMessage = ex.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(FeedDefn result) {
            super.onPostExecute(result);
            if(result != null) {
                mFeedDefn.loaded = true;
            }
            mReaderComplete.onReaderComplete(result, mErrorMessage);
        }
    }

    private boolean allFeedsLoaded() {
        for(FeedDefn feed : mDefn.getFeeds()) {
            if(!feed.loaded) return false;
        }

        return true;
    }

    private void loadFeeds() {

        AsyncReader.ReaderCompleteCallback readerCallback =
                new AsyncReader.ReaderCompleteCallback() {

            private boolean mErrorShown = false;

            @Override
            public void onReaderComplete(FeedDefn feed, String errorMessage) {

                if(allFeedsLoaded()) {
                    //mProgressDialog.dismiss();
                }

                if(feed == null) {

                    if(!mErrorShown) {
                        Toast.makeText(NewsActivity.this, R.string.feedLoadingError, Toast.LENGTH_SHORT).show();
                        mErrorShown = true;
                    }

                    return;
                }

                mAdapter.addFeed(feed);
            }
        };

        for(FeedDefn feed : mDefn.getFeeds()) {
            if(!feed.loaded) {
                AsyncReader reader = new AsyncReader(readerCallback, feed);
                reader.execute();
            }
            else {
                mAdapter.addFeed(feed);
            }
        }

        //if(allFeedsLoaded()) mProgressDialog.dismiss();
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

            Intent homeIntent = new Intent(this, PlannerHomeActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);

            return true;
        }
        else if(item == mRefreshItem) {

            //String loadingNews = getResources().getString(R.string.loadingNews);
            //mProgressDialog = ProgressDialog.show(this, "", loadingNews, true);
            mAdapter.clearFeeds();

            for(FeedDefn feed : mDefn.getFeeds()) feed.loaded = false;
            loadFeeds();
        }

        return super.onMenuItemSelected(featureId, item);
    }
}
