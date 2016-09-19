package com.jiajie.design;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * MyApplication
 * Created by jiajie on 16/9/19.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }


}
