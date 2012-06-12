package com.neoware.europlanner;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;

public class ReadDataAsyncTask extends AsyncTask<String, Integer, Boolean> {

    public enum DataType {
        ETramData,
        EBusData,
        EMetroData,
        EFgcData
    };

    public static final String SQUAD_DEFN_DATA_URL = "http://dl.dropbox.com/u/82143078/squaddefn.xml";
    public static final String TOURN_DEFN_DATA_URL = "http://dl.dropbox.com/u/82143078/tournamentdefn.xml";

    //USE FOR TESTING
    //public static final String TOURN_DEFN_DATA_URL = "http://dl.dropbox.com/u/82143078/test_defn.xml";

    private final OnDataLoadedCallback mCallback;
    private String mErrorMessage;
    private String mData;

    public ReadDataAsyncTask(OnDataLoadedCallback callback) {
        mCallback = callback;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpParams httpParams = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
            HttpConnectionParams.setSoTimeout(httpParams, 30000);

            HttpGet get = new HttpGet(params[0]);
            mData = httpClient.execute(get, new BasicResponseHandler());

            return true;

        } catch (ClientProtocolException ex) {
            mErrorMessage = ex.getMessage();
        } catch (IOException ex) {
            mErrorMessage = ex.getMessage();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if(result) {
            mCallback.onDataLoaded(mData);
        }
        else {
            mCallback.onDataLoadingFailed(mErrorMessage);
        }
    }

    public interface OnDataLoadedCallback {
        public void onDataLoaded(String data);
        public void onDataLoadingFailed(String errorMessage);
    }
}
