package com.neoware.brazilplanner;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.neoware.brazilplanner.E12DataService.E12DataServiceBinder;

public abstract class E12ServiceActivity extends SherlockFragmentActivity {

    protected E12DataServiceBinder mBinder;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isServiceRunning()) {
            startService(new Intent(this, E12DataService.class));
        }

        bindService(new Intent(this, E12DataService.class), mConnection,
                Context.BIND_AUTO_CREATE);
    }

    public boolean isServiceRunning() {

        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (E12DataService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBinder != null) {
            unbindService(mConnection);
        }
    }

    protected final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            mBinder = (E12DataServiceBinder)binder;
            E12ServiceActivity.this.onServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            E12ServiceActivity.this.onServiceDisconnected();
            mBinder = null;
        }
    };

    protected abstract void onServiceConnected();
    protected abstract void onServiceDisconnected();
}
