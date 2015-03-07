package com.florianmski.coderwall;

import android.app.Application;
import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;
import com.florianmski.coderwall.data.CoderwallPrefs;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class CoderwallApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        CoderwallPrefs.create(getApplicationContext());

        if(BuildConfig.DEBUG)
        {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectNetwork()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());

            Timber.plant(new Timber.DebugTree());
        }
        else
            Fabric.with(this, new Crashlytics());
    }
}
