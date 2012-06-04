package com.neoware.europlanner;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.Assert;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.neoware.europlanner.ReadDataAsyncTask.OnDataLoadedCallback;

public class E12DataService extends Service {

    private static final String SQUAD_DATA = "squaddefn.xml";
    private static final String TOURN_DATA = "tournamentdefn.xml";

    public interface DataLoadedCallback {
        public void dataReady();
        public void errorLoadingData(String error);
    }

    //Same process so don't worry about IPC
    public class E12DataServiceBinder extends Binder {

        public void loadSquadDataFromServer(DataLoadedCallback callback) {
            loadSquadData(callback);
        }

        public void loadTournamentDefnFromServer(DataLoadedCallback callback) {
            loadTournamentDefn(callback);
        }

        public String getSquadData(Context context) {

            ContextWrapper wrapper = new ContextWrapper(context);
            File homeDir = wrapper.getFilesDir();
            File squadFile = new File(homeDir, SQUAD_DATA);
            return readFile(squadFile);
        }

        public String getTournamentData(Context context) {

            ContextWrapper wrapper = new ContextWrapper(context);
            File homeDir = wrapper.getFilesDir();
            File tournamentFile = new File(homeDir, TOURN_DATA);
            return readFile(tournamentFile);
        }

        private String readFile(File file) {

            String result = null;
            Scanner scanner = null;

            try {

                StringBuilder fileContents = new StringBuilder((int)file.length());
                scanner = new Scanner(file);
                String lineSeparator = System.getProperty("line.separator");

                while(scanner.hasNextLine()) {
                    fileContents.append(scanner.nextLine() + lineSeparator);
                }
                result = fileContents.toString();
            }
            catch(IOException ex) {
                Log.w(getClass().getSimpleName(), "Unable to read file: " + ex.getMessage());
            }
            finally {
                scanner.close();
            }

            return result;
        }
    }

    private final IBinder mBinder = new E12DataServiceBinder();

    private void loadSquadData(final DataLoadedCallback callback) {

        new ReadDataAsyncTask(new OnDataLoadedCallback() {

            @Override
            public void onDataLoadingFailed(String errorMessage) {

                Log.w(getClass().getSimpleName(), "Warning: unable to load tournament defn: " + errorMessage);

                String sanitisedError = getResources().getString(R.string.loadingSquadError);
                callback.errorLoadingData(sanitisedError);
            }

            @Override
            public void onDataLoaded(String data) {
                if(dataIsValid(data)) {
                    writeData(SQUAD_DATA, data);
                    callback.dataReady();
                }
                else {
                   Log.w(getClass().getSimpleName(), "Warning: XML On server is not valid");

                   String sanitisedError = getResources().getString(R.string.loadingSquadError);
                   callback.errorLoadingData(sanitisedError);
                }
            }
        }).execute(ReadDataAsyncTask.SQUAD_DEFN_DATA_URL);
    }

    private void loadTournamentDefn(final DataLoadedCallback callback) {

        new ReadDataAsyncTask(new OnDataLoadedCallback() {

            @Override
            public void onDataLoadingFailed(String errorMessage) {

                Log.w(getClass().getSimpleName(), "Warning: unable to load tournament defn: " + errorMessage);

                String sanitisedError = getResources().getString(R.string.loadingTournError);
                callback.errorLoadingData(sanitisedError);
            }

            @Override
            public void onDataLoaded(String data) {
               if(dataIsValid(data)) {
                    writeData(TOURN_DATA, data);
                    TournamentDefinition.refreshTournamentDefnInstance(E12DataService.this);
                    callback.dataReady();
               }
               else {
                   Log.w(getClass().getSimpleName(), "Warning: XML On server is not valid");

                   String sanitisedError = getResources().getString(R.string.loadingTournError);
                   callback.errorLoadingData(sanitisedError);
               }
            }
        }).execute(ReadDataAsyncTask.TOURN_DEFN_DATA_URL);
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

        ContextWrapper wrapper = new ContextWrapper(this);
        File homeDir = wrapper.getFilesDir();
        File destFile = new File(homeDir, destName);

        if(!destFile.exists()) {

            InputStream defnInputStream = this.getResources().openRawResource(
                    rawId);

            try {
                OutputStream fos = new FileOutputStream(destFile);
                byte copyBuffer[] = new byte[1024];
                int lengthCopied = 0;
                while((lengthCopied=defnInputStream.read(copyBuffer))>0) {
                    fos.write(copyBuffer, 0, lengthCopied);
                }
            }
            catch(IOException ex) {
                Assert.fail("Failed to copy " + destName + ": " + ex.getMessage());
            }
        }
    }

    private void writeData(String dest, String data) {

        ContextWrapper wrapper = new ContextWrapper(this);
        File homeDir = wrapper.getFilesDir();
        File dataFile = new File(homeDir, dest);

        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(dataFile));
            out.writeBytes(data);
            out.close();
        } catch (IOException ex) {
            Assert.fail("Failed to write data from server to file: " + ex.getMessage());
        }
    }

    private boolean dataIsValid(String data) {

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();

            XMLReader reader = parser.getXMLReader();
            reader.parse(new InputSource(new ByteArrayInputStream(data.getBytes())));

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
