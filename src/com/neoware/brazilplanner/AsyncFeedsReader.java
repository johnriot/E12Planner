package com.neoware.brazilplanner;

import android.os.AsyncTask;

import com.neoware.brazilplanner.FeedsAdapter.FeedDefn;
import com.neoware.rss.RSSFault;
import com.neoware.rss.RSSReader;
import com.neoware.rss.RSSReaderException;

public class AsyncFeedsReader extends AsyncTask<String, Void, FeedDefn> {

    public interface ReaderCompleteCallback {
        public void onReaderComplete(FeedDefn feed, String errorMessage);
    }

    private final ReaderCompleteCallback mReaderComplete;

    private String mErrorMessage;
    private final FeedDefn mFeedDefn;

    //public AsyncFeedsReader(ReaderCompleteCallback callback, FeedDefn defn) {
    public AsyncFeedsReader(FeedDefn defn, ReaderCompleteCallback callback) {
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
