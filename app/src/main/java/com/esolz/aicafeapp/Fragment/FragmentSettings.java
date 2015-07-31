package com.esolz.aicafeapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.esolz.aicafeapp.ActivityLandingPage;
import com.esolz.aicafeapp.Datatype.LoginDataType;
import com.esolz.aicafeapp.Datatype.SettingsDataType;
import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.Helper.ConnectionDetector;
import com.esolz.aicafeapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ltp on 08/07/15.
 */
public class FragmentSettings extends Fragment {

    View view;
    LinearLayout llPipeContainer, slidingNow, llBack, profileDrawer;
    RelativeLayout rlMSGContainer;
    TextView txtPageTitle, txtMSGCounter;
    ImageView imgBack, imgMSG;
    DrawerLayout drawerLayout;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    LinearLayout llNotification, llSound, llBluetooth, llBlock, llSecurity, llPrivacy;

    SettingsDataType settingsDataType;
    ConnectionDetector cd;

    SharedPreferences sharedPreferencesSwitch, sharedPreferencesSound, sharedPreferencesVisibility;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_settings, container, false);

        fragmentManager = getFragmentManager();
        cd = new ConnectionDetector(getActivity());

        sharedPreferencesSwitch = getActivity().getSharedPreferences("NotificationSwitch", Context.MODE_PRIVATE);
        sharedPreferencesSound = getActivity().getSharedPreferences("SOUND", Context.MODE_PRIVATE);
        sharedPreferencesVisibility = getActivity().getSharedPreferences("VISIBILITY", Context.MODE_PRIVATE);

        llPipeContainer = (LinearLayout) getActivity().findViewById(R.id.ll_pipe_container);
        slidingNow = (LinearLayout) getActivity().findViewById(R.id.slidingnow);
        rlMSGContainer = (RelativeLayout) getActivity().findViewById(R.id.rl_msgcontainer);
        txtPageTitle = (TextView) getActivity().findViewById(R.id.txt_page_title);
        imgBack = (ImageView) getActivity().findViewById(R.id.img_back);
        llBack = (LinearLayout) getActivity().findViewById(R.id.ll_back);
        txtMSGCounter = (TextView) getActivity().findViewById(R.id.txt_msg_counter);
        imgMSG = (ImageView) getActivity().findViewById(R.id.img_msg);

        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        profileDrawer = (LinearLayout) getActivity().findViewById(R.id.profile_drawer);

        llPipeContainer.setVisibility(View.VISIBLE);
        slidingNow.setVisibility(View.VISIBLE);
        rlMSGContainer.setVisibility(View.VISIBLE);
        txtPageTitle.setVisibility(View.VISIBLE);
        imgBack.setVisibility(View.GONE);
        llBack.setVisibility(View.GONE);
        imgMSG.setVisibility(View.GONE);
        txtMSGCounter.setVisibility(View.GONE);

        txtPageTitle.setText("Settings");


        llNotification = (LinearLayout) view.findViewById(R.id.ll_notification);
        llSound = (LinearLayout) view.findViewById(R.id.ll_sound);
        llBluetooth = (LinearLayout) view.findViewById(R.id.ll_bluetooth);
        llBlock = (LinearLayout) view.findViewById(R.id.ll_block);
        llSecurity = (LinearLayout) view.findViewById(R.id.ll_security);
        llPrivacy = (LinearLayout) view.findViewById(R.id.ll_privacy);

        if (cd.isConnectingToInternet()) {
            getSettingsInformation(
                    "http://www.esolz.co.in/lab9/aiCafe/iosapp/show_status.php", AppData.loginDataType.getId()
            );
        } else {
            Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
        }

        llNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentNotificationSettings fragmentNotificationSettings = new FragmentNotificationSettings();
                fragmentTransaction.replace(R.id.fragment_container, fragmentNotificationSettings);
                int count = fragmentManager.getBackStackEntryCount();
                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();
            }
        });

        llSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentSoundSettings fragmentSoundSettings = new FragmentSoundSettings();
                fragmentTransaction.replace(R.id.fragment_container, fragmentSoundSettings);
                int count = fragmentManager.getBackStackEntryCount();
                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();
            }
        });

        llBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fragmentTransaction = fragmentManager.beginTransaction();
//                FragmentBluetoothSettings fragmentBluetoothSettings = new FragmentBluetoothSettings();
//                fragmentTransaction.replace(R.id.fragment_container, fragmentBluetoothSettings);
//                int count = fragmentManager.getBackStackEntryCount();
//                fragmentTransaction.addToBackStack(String.valueOf(count));
//                fragmentTransaction.commit();
            }
        });

        llBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentBlockListSettings fragmentBlockListSettings = new FragmentBlockListSettings();
                fragmentTransaction.replace(R.id.fragment_container, fragmentBlockListSettings);
                int count = fragmentManager.getBackStackEntryCount();
                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();
            }
        });

        llSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fragmentTransaction = fragmentManager.beginTransaction();
//                FragmentSecuritySettings fragmentSecuritySettings = new FragmentSecuritySettings();
//                fragmentTransaction.replace(R.id.fragment_container, fragmentSecuritySettings);
//                int count = fragmentManager.getBackStackEntryCount();
//                fragmentTransaction.addToBackStack(String.valueOf(count));
//                fragmentTransaction.commit();
            }
        });

        llPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentPrivacySettings fragmentPrivacySettings = new FragmentPrivacySettings();
                fragmentTransaction.replace(R.id.fragment_container, fragmentPrivacySettings);
                int count = fragmentManager.getBackStackEntryCount();
                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    public void getSettingsInformation(final String URL, final String ID) {
        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);
                        try {
                            JSONObject response = new JSONObject(stringResponse);
                            JSONObject jsonObjectSetting = response.getJSONObject("settings");

                            if (jsonObjectSetting.getString("notification").equals("Y")) {
                                SharedPreferences.Editor editor = sharedPreferencesSwitch.edit();
                                editor.putString("SWITCH", "ON");
                                editor.commit();
                            } else {
                                SharedPreferences.Editor editor = sharedPreferencesSwitch.edit();
                                editor.putString("SWITCH", "OFF");
                                editor.commit();
                            }

                            if (jsonObjectSetting.getString("sound").equals("Y")) {
                                SharedPreferences.Editor editor = sharedPreferencesSound.edit();
                                editor.putString("Sound", "ON");
                                editor.commit();
                            } else {
                                SharedPreferences.Editor editor = sharedPreferencesSound.edit();
                                editor.putString("Sound", "OFF");
                                editor.commit();
                            }

                            if (jsonObjectSetting.getString("visible").equals("Y")) {
                                SharedPreferences.Editor editor = sharedPreferencesVisibility.edit();
                                editor.putString("Visible", "ON");
                                editor.commit();
                            } else {
                                SharedPreferences.Editor editor = sharedPreferencesVisibility.edit();
                                editor.putString("Visible", "OFF");
                                editor.commit();
                            }

                        } catch (JSONException e) {
                            Log.d("JSONException", e.toString());
                            Toast.makeText(getActivity(), "Server not responding...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Output : ", "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Server not responding...!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", ID);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(sr);
    }
}
