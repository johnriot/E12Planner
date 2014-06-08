package com.neoware.brazilplanner;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.Assert;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Service;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.neoware.brazilplanner.FeedsAdapter.FeedDefn;
import com.neoware.brazilplanner.ReadDataAsyncTask.OnDataLoadedCallback;

public class E12DataService extends Service {

	private static final String SQUAD_DATA = "squaddefn.xml";
	private static final String TOURN_DATA = "tournamentdefn.xml";

	private static final long MS_IN_HOUR = 1000 * 60 * 60;
	private static final long REFRESH_INTERVAL_MS = MS_IN_HOUR * 2;
	private static final String TIMESTAMP_KEY_TOURN = "TIMESTAMP_TOURN";
	private static final String TIMESTAMP_KEY_SQUAD = "TIMESTAMP_SQUAD";

	public interface DataLoadedCallback {
		public void dataReady();

		public void errorLoadingData(String error);
	}

	// Same process so don't worry about IPC
	public class E12DataServiceBinder extends Binder {

		public void loadSquadsDefnFromServer(DataLoadedCallback callback,
				boolean forceLoad) {
			loadSquadsDefn(callback, forceLoad);
		}

		public void loadNewsFeeds(DataLoadedCallback callback) {
			loadFeeds(callback);
		}

		public void loadTournamentDefnFromServer(DataLoadedCallback callback,
				boolean forceLoad) {
			loadTournamentDefn(callback, forceLoad);
		}
	}

	private final IBinder mBinder = new E12DataServiceBinder();

	private boolean serverReadRequiredForSquads() {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplication());

		long now = System.currentTimeMillis();
		long lastRefresh = prefs.getLong(TIMESTAMP_KEY_SQUAD, 0);

		return ((now - lastRefresh) > REFRESH_INTERVAL_MS);
	}

	private void loadSquadsDefn(final DataLoadedCallback callback,
			boolean forceLoad) {

		if (!forceLoad && !serverReadRequiredForSquads()) {
			callback.dataReady();
			return;
		}

		new ReadDataAsyncTask(new OnDataLoadedCallback() {

			@Override
			public void onDataLoadingFailed(String errorMessage) {

				Log.w(getClass().getSimpleName(),
						"Warning: unable to load tournament defn: "
								+ errorMessage);

				String sanitisedError = getResources().getString(
						R.string.loadingSquadError);
				callback.errorLoadingData(sanitisedError);
			}

			@Override
			public void onDataLoaded(String data) {
				if (dataIsValid(data)) {
					writeData(SQUAD_DATA, data);
					SquadsDefinition
							.refreshSquadsDefnInstance(E12DataService.this);

					SharedPreferences prefs = PreferenceManager
							.getDefaultSharedPreferences(getApplication());
					SharedPreferences.Editor editor = prefs.edit();
					editor.putLong(TIMESTAMP_KEY_SQUAD,
							System.currentTimeMillis());
					editor.commit();

					callback.dataReady();
				} else {
					Log.w(getClass().getSimpleName(),
							"Warning: XML On server is not valid");

					String sanitisedError = getResources().getString(
							R.string.loadingSquadError);
					callback.errorLoadingData(sanitisedError);
				}
			}
		}).execute(ReadDataAsyncTask.SQUAD_DEFN_DATA_URL);
	}

	private boolean serverReadRequiredForTournament() {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplication());

		long now = System.currentTimeMillis();
		long lastRefresh = prefs.getLong(TIMESTAMP_KEY_TOURN, 0);

		return ((now - lastRefresh) > REFRESH_INTERVAL_MS);
	}

	private void loadTournamentDefn(final DataLoadedCallback callback,
			boolean forceLoad) {

		if (!forceLoad && !serverReadRequiredForTournament()) {
			callback.dataReady();
			return;
		}

		new ReadDataAsyncTask(new OnDataLoadedCallback() {

			@Override
			public void onDataLoadingFailed(String errorMessage) {

				Log.w(getClass().getSimpleName(),
						"Warning: unable to load tournament defn: "
								+ errorMessage);

				String sanitisedError = getResources().getString(
						R.string.loadingTournError);
				callback.errorLoadingData(sanitisedError);
			}

			@Override
			public void onDataLoaded(String data) {
				if (dataIsValid(data)) {
					writeData(TOURN_DATA, data);
					TournamentDefinition
							.refreshTournamentDefnInstance(E12DataService.this);

					SharedPreferences prefs = PreferenceManager
							.getDefaultSharedPreferences(getApplication());
					SharedPreferences.Editor editor = prefs.edit();
					editor.putLong(TIMESTAMP_KEY_TOURN,
							System.currentTimeMillis());
					editor.commit();

					callback.dataReady();
				} else {
					Log.w(getClass().getSimpleName(),
							"Warning: XML On server is not valid");

					String sanitisedError = getResources().getString(
							R.string.loadingTournError);
					callback.errorLoadingData(sanitisedError);
				}
			}
		}).execute(ReadDataAsyncTask.TOURN_DEFN_DATA_URL);
	}

	private void loadFeeds(final DataLoadedCallback callback) {

		final FeedsReader reader = new FeedsReader(this);

		reader.loadFeeds(new AsyncFeedsReader.ReaderCompleteCallback() {

			@Override
			public void onReaderComplete(FeedDefn feed, String errorMessage) {

				if (feed == null) {

					Log.w(getClass().getSimpleName(),
							"Error: Feed is not valid");
					String sanitisedError = getResources().getString(
							R.string.loadingFeedError);
					callback.errorLoadingData(sanitisedError);
					return;
				}

				if (reader.allFeedsLoaded()) {
					callback.dataReady();
				}

				reader.addFeedToAdapter(feed);
			}
		});
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		copyRawFiles();

		return START_NOT_STICKY;
	}

	private void copyRawFiles() {

		copyRawFile(TOURN_DATA, R.raw.tournamentdefn);
		copyRawFile(SQUAD_DATA, R.raw.squaddefn);
	}

	private void copyRawFile(String destName, int rawId) {

		Log.e("STEO", "Copying mother " + destName);
		ContextWrapper wrapper = new ContextWrapper(this);
		File homeDir = wrapper.getFilesDir();
		File destFile = new File(homeDir, destName);

		if (!destFile.exists()) {

			InputStream defnInputStream = getResources().openRawResource(
					rawId);

			try {
				OutputStream fos = new FileOutputStream(destFile);
				byte copyBuffer[] = new byte[1024];
				int lengthCopied = 0;
				while ((lengthCopied = defnInputStream.read(copyBuffer)) > 0) {
					fos.write(copyBuffer, 0, lengthCopied);
				}
				fos.close();
			} catch (IOException ex) {
				Assert.fail("Failed to copy " + destName + ": "
						+ ex.getMessage());
			}
		}
	}

	private void writeData(String dest, String data) {

		ContextWrapper wrapper = new ContextWrapper(this);
		File homeDir = wrapper.getFilesDir();
		File dataFile = new File(homeDir, dest);

		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					dataFile));
			out.writeBytes(data);
			out.close();
		} catch (IOException ex) {
			Assert.fail("Failed to write data from server to file: "
					+ ex.getMessage());
		}
	}

	private boolean dataIsValid(String data) {

		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();

			XMLReader reader = parser.getXMLReader();
			reader.parse(new InputSource(new ByteArrayInputStream(data
					.getBytes())));

		} catch (SAXException ex) {
			return false;
		} catch (IOException ex) {
			return false;
		} catch (ParserConfigurationException ex) {
			return false;
		}

		return true;
	}
}
