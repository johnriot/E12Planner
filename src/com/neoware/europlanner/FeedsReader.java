package com.neoware.europlanner;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.neoware.europlanner.FeedsAdapter.FeedDefn;
import com.neoware.rss.RSSItem;

// Can unify with Feeds Reading code in NewsActivity
public class FeedsReader {

    private final FeedsAdapter mAdapter;
    private final  TournamentDefinition mDefn;

    public FeedsReader(Context context) {

        mDefn = TournamentDefinition.getTournamentDefnInstance(context);
        mAdapter = new FeedsAdapter(context);
    }

    public static ArrayList<String> getAllTitles(int groupPosition, Context context) {

        ArrayList<String> titles = new ArrayList<String>();
        FeedsAdapter.FeedDefn fifaFeed = TournamentDefinition.getTournamentDefnInstance(context).getFeeds().get(groupPosition);
        List<RSSItem> feedList = fifaFeed.rssFeed.getItems();
        for(RSSItem rssItem : feedList) {
            titles.add(rssItem.getTitle());
        }
        return titles;
    }

    public void loadFeeds(AsyncFeedsReader.ReaderCompleteCallback callback) {

        for(FeedDefn feed : mDefn.getFeeds()) {
            if(!feed.loaded) {
                AsyncFeedsReader reader = new AsyncFeedsReader(feed, callback);
                reader.execute();
            }
            else {
                mAdapter.addFeed(feed);
            }
        }
    }

    // Overload for case when feeds persisted in TournamentDefn
    public void loadFeeds() {
        for(FeedDefn feed : mDefn.getFeeds()) {
            mAdapter.addFeed(feed);
        }
    }

    public boolean allFeedsLoaded() {
        for(FeedDefn feed : mDefn.getFeeds()) {
            if(!feed.loaded) return false;
        }

        return true;
    }

    public void addFeedToAdapter(FeedDefn feed) {
        mAdapter.addFeed(feed);
    }
}
