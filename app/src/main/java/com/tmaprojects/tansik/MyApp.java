package com.tmaprojects.tansik;

import android.app.Application;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tmaprojects.tansik.io.TansikLocal;
import com.tmaprojects.tansik.model.TansikYear;

import timber.log.Timber;

/**
 * Created by tarekkma on 8/19/17.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }else{
            Timber.plant(new CrashReportingTree());
        }
        new TansikLocal(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
    }



    private static class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            if (t != null) {
                if (priority == Log.ERROR) {
                    FirebaseCrash.report(new Exception(message,t));
                } else if (priority == Log.WARN) {
                    //FirebaseCrash.report(new Exception(message,t));
                }
            }
        }
    }
}
