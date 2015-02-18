package com.ngstudio.organaizer;

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;

import com.activeandroid.ActiveAndroid;

import org.jetbrains.annotations.NotNull;

public class OrganaizersApplication extends Application {

    private volatile static OrganaizersApplication instance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        context = getApplicationContext();

        synchronized (OrganaizersApplication.class) {
            if (BuildConfig.DEBUG) {
                if (instance != null)
                    throw new RuntimeException("Something strange: there is another application instance.");
            }
            instance = this;

            OrganaizersApplication.class.notifyAll();
        }

    }

    public static Context getAppContext() {
        return OrganaizersApplication.context;// = getApplicationContext();
    }

    private String activityVisible;
    private String topActivity;

    public boolean isActivityBackground() {
        return TextUtils.isEmpty(activityVisible);
    }

    public void activityResumed(@NotNull String activity) {
        activityVisible = activity;
        topActivity = activity;
    }

    public void activityPaused(@NotNull String activity) {
        if (activity.equals(activityVisible))
            activityVisible = null;
    }

    @NotNull
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public static OrganaizersApplication getInstance() {
        OrganaizersApplication application = instance;
        if (application == null) {
            synchronized (OrganaizersApplication.class) {
                if (instance == null) {
                    if (BuildConfig.DEBUG) {
                        if (Thread.currentThread() == Looper.getMainLooper().getThread())
                            throw new UnsupportedOperationException(
                                    "Current application's instance has not been initialized yet (wait for onCreate, please).");
                    }
                    try {
                        do {
                            OrganaizersApplication.class.wait();
                        } while ((application = instance) == null);
                    } catch (InterruptedException e) { /* Nothing to do */ }
                }
            }
        }
        return application;
    }
}
