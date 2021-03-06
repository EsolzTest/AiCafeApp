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

    public static SharedPreferences preferCheckAppState;
    public static SharedPreferences sateAppState;
    public static SharedPreferences.Editor edit;
    public String isAppRunning;

    public static SharedPreferences checkNotificationSetting;
    public static SharedPreferences.Editor editNotification;
    public String isNotificationON;

    public static SharedPreferences checkSoundSetting;
    public static SharedPreferences.Editor editSound;
    public String isSoundON;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        preferCheckAppState = getSharedPreferences("CHECK_APP_STATE", Context.MODE_PRIVATE);
        checkNotificationSetting = getSharedPreferences("CHECK_NOTIFICATION_SETTING", Context.MODE_PRIVATE);
        checkSoundSetting = getSharedPreferences("CHECK_SOUND_SETTING", Context.MODE_PRIVATE);
    }

    public static String isSoundON() {
        return checkSoundSetting.getString("SoundState", "");
    }

    public static void setIsSoundON(String isSoundON) {
        editSound = checkSoundSetting.edit();
        editSound.putString("SoundState", isSoundON);
        editSound.commit();
    }

    public static String isNotificationON() {
        return checkNotificationSetting.getString("NotificationState", "");
    }

    public static void setIsNotificationON(String isNotificationON) {
        editNotification = checkNotificationSetting.edit();
        editNotification.putString("NotificationState", isNotificationON);
        editNotification.commit();
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