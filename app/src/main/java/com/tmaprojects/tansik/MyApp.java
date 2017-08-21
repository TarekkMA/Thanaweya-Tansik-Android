package com.tmaprojects.tansik;

import android.app.Application;

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
        }
        new TansikLocal(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
    }
}
