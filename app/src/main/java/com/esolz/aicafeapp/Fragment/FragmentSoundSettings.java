package com.esolz.aicafeapp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.esolz.aicafeapp.Customviews.OpenSansRegularTextView;
import com.esolz.aicafeapp.Customviews.OpenSansSemiboldTextView;
import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.Helper.ConnectionDetector;
import com.esolz.aicafeapp.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ltp on 31/07/15.
 */
public class FragmentSoundSettings extends Fragment {

    View view;
    LinearLayout llPipeContainer, slidingNow, llBack;
    RelativeLayout rlMSGContainer;
    OpenSansSemiboldTextView txtPageTitle;
    OpenSansRegularTextView txtMSGCounter, txtError;
    ImageView imgBack, imgMSG;
    DrawerLayout drawerLayout;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    ConnectionDetector cd;

    OpenSansSemiboldTextView txtNotification;
    Switch switchSound;

    SharedPreferences sharedPreferencesSwitch;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_sound_settings, container, false);

        cd = new ConnectionDetector(getActivity());

        fragmentManager = getFragmentManager();

        sharedPreferencesSwitch = getActivity().getSharedPreferences("SOUND", Context.MODE_PRIVATE);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");

        llPipeContainer = (LinearLayout) getActivity().findViewById(R.id.ll_pipe_container);
        slidingNow = (LinearLayout) getActivity().findViewById(R.id.slidingnow);
        rlMSGContainer = (RelativeLayout) getActivity().findViewById(R.id.rl_msgcontainer);
        txtPageTitle = (OpenSansSemiboldTextView) getActivity().findViewById(R.id.txt_page_title);
        imgBack = (ImageView) getActivity().findViewById(R.id.img_back);
        llBack = (LinearLayout) getActivity().findViewById(R.id.ll_back);
        txtMSGCounter = (OpenSansRegularTextView) getActivity().findViewById(R.id.txt_msg_counter);
        imgMSG = (ImageView) getActivity().findViewById(R.id.img_msg);

        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        txtNotification = (OpenSansSemiboldTextView) view.findViewById(R.id.txt_sound);
        switchSound = (Switch) view.findViewById(R.id.switch_sound);

        // switchNotification.setChecked(false);
        try {
            if (sharedPreferencesSwitch.getString("Sound", "").equals("OFF")) {
                switchSound.setChecked(false);
            } else {
                switchSound.setChecked(true);
            }
        } catch (Exception e) {
            Log.i("Sound Problrm  ", e.toString());
        }
        switchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (cd.isConnectingToInternet()) {
                    if (isChecked) {
                        txtNotification.setText("ON");
//                        SharedPreferences.Editor editor = sharedPreferencesSwitch.edit();
//                        editor.putString("SWITCH", "ON");
//                        editor.commit();

                        AppController.setIsSoundON("ON");
                        setSoundSettings(
                                "http://www.esolz.co.in/lab9/aiCafe/iosapp/insert_status.php",
                                AppData.loginDataType.getId(),
                                "Y"
                        );
                    } else {
                        txtNotification.setText("OFF");
//                        SharedPreferences.Editor editor = sharedPreferencesSwitch.edit();
//                        editor.putString("SWITCH", "OFF");
//                        editor.commit();

                        AppController.setIsSoundON("OFF");
                        setSoundSettings(
                                "http://www.esolz.co.in/lab9/aiCafe/iosapp/insert_status.php",
                                AppData.loginDataType.getId(),
                                "N"
                        );
                    }
                } else {
                    Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (switchSound.isChecked()) {
            txtNotification.setText("ON");
        } else {
            txtNotification.setText("OFF");
        }

        llPipeContainer.setVisibility(View.GONE);
        slidingNow.setVisibility(View.GONE);
        rlMSGContainer.setVisibility(View.GONE);
        txtPageTitle.setVisibility(View.VISIBLE);
        imgBack.setVisibility(View.VISIBLE);
        llBack.setVisibility(View.VISIBLE);
        imgMSG.setVisibility(View.GONE);
        txtMSGCounter.setVisibility(View.GONE);

        txtPageTitle.setText("Sound Setting");

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentSettings fragmentSettings = new FragmentSettings();
                fragmentTransaction.replace(R.id.fragment_container, fragmentSettings);
                int count = fragmentManager.getBackStackEntryCount();
                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();

            }
        });

        return view;
    }

    public void setSoundSettings(final String URL, final String ID, final String SOUND) {

        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);
                        progressDialog.dismiss();
                        try {
                            if (stringResponse.equals("success")) {
                                if (sharedPreferencesSwitch.getString("Sound", "").equals("OFF")) {
                                    SharedPreferences.Editor editor = sharedPreferencesSwitch.edit();
                                    editor.putString("Sound", "ON");
                                    editor.commit();
                                    AppController.setIsSoundON("ON");
                                } else {
                                    SharedPreferences.Editor editor = sharedPreferencesSwitch.edit();
                                    editor.putString("Sound", "OFF");
                                    editor.commit();

                                    txtNotification.setText("OFF");
                                    AppController.setIsSoundON("OFF");
                                }
                            }
                        } catch (Exception e) {
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
                params.put("mode", "sound");
                params.put("sound", SOUND);
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