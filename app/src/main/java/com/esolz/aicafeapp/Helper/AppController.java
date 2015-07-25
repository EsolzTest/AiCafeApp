package com.esolz.aicafeapp.Helper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by ltp on 08/07/15.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    public  static SharedPreferences preferCheckAppState;
    public  static SharedPreferences sateAppState;
    public  static SharedPreferences.Editor edit;
    public String isAppRunning;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        preferCheckAppState = getSharedPreferences("CHECK_APP_STATE", Context.MODE_PRIVATE);
    }

    public static String isAppRunning() {
        return preferCheckAppState.getString("AppState", "");
    }

    public static void setIsAppRunning(String isAppRunning) {
        edit = preferCheckAppState.edit();
        edit.putString("AppState", isAppRunning);
        edit.commit();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}