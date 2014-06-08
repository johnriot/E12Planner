package com.neoware.brazilplanner;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

public class GetRemoteDrawable extends AsyncTask<String, Void, Drawable> {

    public interface DrawableRetrievedCallback {
        public void drawableRetrieved(Drawable result);
    }

    private final DrawableRetrievedCallback mCallback;

    public GetRemoteDrawable(DrawableRetrievedCallback callback) {
        mCallback = callback;
    }

    @Override
    protected Drawable doInBackground(String... params) {

        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(params[0]).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();

            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return new BitmapDrawable(bitmap);

        } catch (MalformedURLException exc) {
            exc.printStackTrace();
            return null;
        } catch (IOException exc) {
            exc.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Drawable result) {
        mCallback.drawableRetrieved(result);
    }
}
