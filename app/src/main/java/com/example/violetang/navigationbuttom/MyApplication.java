package com.example.violetang.navigationbuttom;

import android.app.Application;
import android.content.Context;

/**
 * Author: Jiali
 * Date: Nov. 2018
 * Description: format MainActivity.context();
 */
public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}