package com.esolz.aicafeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.esolz.aicafeapp.Datatype.LoginDataType;
import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.Helper.ConnectionDetector;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class ActivitySplash extends AppCompatActivity {

    ConnectionDetector cd;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    static final String TAG = "GCMRegistration";
    String SENDER_ID = "104883704281";
    String regid = "", msg = "";
    GoogleCloudMessaging gcm;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("AppCredit", Context.MODE_PRIVATE);

        cd = new ConnectionDetector(ActivitySplash.this);
        if (cd.isConnectingToInternet()) {
            //if (checkPlayServices()) {
                gcm = GoogleCloudMessaging.getInstance(ActivitySplash.this);
                regid = getRegistrationId(ActivitySplash.this);

                if (regid.isEmpty()) {
                    registerInBackground();
                } else {
                    Log.d("Reg", "ID Shared Pref => " + regid);
                    AppData.appRegId = regid;
                    sendRegistrationIdToBackend();
                }
//            } else {
//                Log.i(TAG, "No valid Google Play Services APK found.");
//            }
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

//                try {
//                    AppData.loginDataType = new LoginDataType(
//                            sharedPreferences.getString("ID", ""),
//                            sharedPreferences.getString("NAME", ""),
//                            sharedPreferences.getString("SEX", ""),
//                            sharedPreferences.getString("EMAIL", ""),
//                            sharedPreferences.getString("PASSWORD", ""),
//                            sharedPreferences.getString("ABOUT", ""),
//                            sharedPreferences.getString("BUSINESS", ""),
//                            sharedPreferences.getString("DOB", ""),
//                            sharedPreferences.getString("PHOTO", ""),
//                            sharedPreferences.getString("PHOYOTHUMB", ""),
//                            sharedPreferences.getString("REG_DATE", ""),
//                            sharedPreferences.getString("FACEBOOKID", ""),
//                            sharedPreferences.getString("LASTSYNC", ""),
//                            sharedPreferences.getString("FBURL", ""),
//                            sharedPreferences.getString("AGE", ""),
//                            sharedPreferences.getString("ONLINE", "")
//                    );
//                    Intent intent = new Intent(ActivitySplash.this, ActivityLandingPage.class);
//                    intent.putExtra("NotiSendId", getIntent().getExtras().getString("NotiSendId"));
//                    startActivity(intent);
//                    finish();
//                } catch (Exception e) {
                    if (sharedPreferences.getString("ID", "").equals("")) {
                        Intent intent = new Intent(ActivitySplash.this, ActivityLogin.class);
                        startActivity(intent);
                        finish();
                    } else {
                        AppData.loginDataType = new LoginDataType(
                                sharedPreferences.getString("ID", ""),
                                sharedPreferences.getString("NAME", ""),
                                sharedPreferences.getString("SEX", ""),
                                sharedPreferences.getString("EMAIL", ""),
                                sharedPreferences.getString("PASSWORD", ""),
                                sharedPreferences.getString("ABOUT", ""),
                                sharedPreferences.getString("BUSINESS", ""),
                                sharedPreferences.getString("DOB", ""),
                                sharedPreferences.getString("PHOTO", ""),
                                sharedPreferences.getString("PHOYOTHUMB", ""),
                                sharedPreferences.getString("REG_DATE", ""),
                                sharedPreferences.getString("FACEBOOKID", ""),
                                sharedPreferences.getString("LASTSYNC", ""),
                                sharedPreferences.getString("FBURL", ""),
                                sharedPreferences.getString("AGE", ""),
                                sharedPreferences.getString("ONLINE", "")
                        );
                        Intent intent = new Intent(ActivitySplash.this, ActivityLandingPage.class);
                        startActivity(intent);
                        finish();
                    }
                //}

            }
        }, 3000);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        AppController.setIsAppRunning("YES");
//
//        Toast.makeText(getApplicationContext(), "Start" + AppController.isAppRunning(), Toast.LENGTH_SHORT).show();
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        AppController.setIsAppRunning("NO");
//        Toast.makeText(getApplicationContext(),AppController.isAppRunning(), Toast.LENGTH_SHORT ).show();
//    }

    private void registerInBackground() {
        (new getGCMID()).execute();
    }

    class getGCMID extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(ActivitySplash.this);
                }
                regid = gcm.register(SENDER_ID);
                msg = regid;
            } catch (IOException ex) {
                msg = "";
            }
            Log.d("GCM", msg);
            Log.d("GCM--------", regid);
            return null;
        }

        protected void onPostExecute(Void resultt) {
            super.onPostExecute(resultt);
            if (msg.equals("")) {

            } else {
                sendRegistrationIdToBackend();
            }

        }
    }

    private void sendRegistrationIdToBackend() {
        // Your implementation here.
        Log.d("In", "########My own Method ######");
        Log.v("MY", "Registration ID: " + regid);
        AppData.appRegId = regid;
        SavePreferences("DEVICE_TOKEN", regid);
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(ActivitySplash.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(ActivitySplash.this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, ActivitySplash.this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }
}
