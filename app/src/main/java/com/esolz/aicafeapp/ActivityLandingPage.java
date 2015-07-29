package com.esolz.aicafeapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.esolz.aicafeapp.Customviews.OpenSansRegularTextView;
import com.esolz.aicafeapp.Customviews.OpenSansSemiboldTextView;
import com.esolz.aicafeapp.Fragment.FragmentAbout;
import com.esolz.aicafeapp.Fragment.FragmentAddCoupon;
import com.esolz.aicafeapp.Fragment.FragmentChatRoom;
import com.esolz.aicafeapp.Fragment.FragmentFriendRequest;
import com.esolz.aicafeapp.Fragment.FragmentProfile;
import com.esolz.aicafeapp.Fragment.FragmentSettings;
import com.esolz.aicafeapp.Fragment.FragmentSingleChat;
import com.esolz.aicafeapp.Fragment.FragmentStoreMap;
import com.esolz.aicafeapp.Fragment.FragmentStoreNow;
import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.gcmnotification.GCMIntentService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ltp on 08/07/15.
 */
public class ActivityLandingPage extends AppCompatActivity {

    LinearLayout slidingNow, profileDrawer, llProfile, llStoreNow, llChatRoom,
            llStoreMap, llAddCoupon, llFriendRequest, llSettings, llAbout, llLogout,
            llPipeContainer, llBack;
    RelativeLayout rlMSGContainer;
    OpenSansSemiboldTextView txtPageTitle;
    OpenSansRegularTextView txtMSGCounter;
    ImageView imgBack;

    DrawerLayout drawerLayout;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landingpage);
        sharedPreferences = getSharedPreferences("AppCredit", Context.MODE_PRIVATE);
        fragmentManager = getSupportFragmentManager();

        llPipeContainer = (LinearLayout) findViewById(R.id.ll_pipe_container);
        rlMSGContainer = (RelativeLayout) findViewById(R.id.rl_msgcontainer);
        txtPageTitle = (OpenSansSemiboldTextView) findViewById(R.id.txt_page_title);
        imgBack = (ImageView) findViewById(R.id.img_back);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        txtMSGCounter = (OpenSansRegularTextView) findViewById(R.id.txt_msg_counter);

        slidingNow = (LinearLayout) findViewById(R.id.slidingnow);
        profileDrawer = (LinearLayout) findViewById(R.id.profile_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        llProfile = (LinearLayout) findViewById(R.id.ll_profile);
        llStoreNow = (LinearLayout) findViewById(R.id.ll_store_now);
        llChatRoom = (LinearLayout) findViewById(R.id.ll_chat_room);
        llStoreMap = (LinearLayout) findViewById(R.id.ll_store_map);
        llAddCoupon = (LinearLayout) findViewById(R.id.ll_add_coupon);
        llFriendRequest = (LinearLayout) findViewById(R.id.ll_friend_request);
        llSettings = (LinearLayout) findViewById(R.id.ll_settings);
        llAbout = (LinearLayout) findViewById(R.id.ll_about);
        llLogout = (LinearLayout) findViewById(R.id.ll_logout);

        slidingNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                drawerLayout.openDrawer(profileDrawer);  // change
            }
        });


        try {
//        if(getIntent().getExtras().getString("")) {
            Bundle bundle = new Bundle();
            bundle.putString("USER_ID", getIntent().getExtras().getString("NotiSendId"));
            bundle.putString("Page", "FragmentProfile");

            fragmentTransaction = fragmentManager.beginTransaction();
            FragmentSingleChat fragmentSingleChat = new FragmentSingleChat();
            fragmentSingleChat.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_container, fragmentSingleChat);
            fragmentTransaction.commit();
        } catch (Exception e) {
            fragmentTransaction = fragmentManager.beginTransaction();
            FragmentProfile fragmentProfile = new FragmentProfile();
            fragmentTransaction.replace(R.id.fragment_container, fragmentProfile);
            fragmentTransaction.commit();
        }

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                llProfile.setBackgroundResource(R.drawable.select);
                llStoreNow.setBackgroundResource(0);
                llChatRoom.setBackgroundResource(0);
                llStoreMap.setBackgroundResource(0);
                llAddCoupon.setBackgroundResource(0);
                llFriendRequest.setBackgroundResource(0);
                llSettings.setBackgroundResource(0);
                llAbout.setBackgroundResource(0);
                llLogout.setBackgroundResource(0);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentProfile fragmentProfile = new FragmentProfile();
                        fragmentTransaction.replace(R.id.fragment_container, fragmentProfile);
                        fragmentTransaction.commit();
                    }
                }, 300);

                //drawerLayout.closeDrawer(profileDrawer);
                llProfile.post(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.closeDrawer(profileDrawer);
                    }
                });
            }
        });

        llStoreNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                llProfile.setBackgroundResource(0);
                llStoreNow.setBackgroundResource(R.drawable.select);
                llChatRoom.setBackgroundResource(0);
                llStoreMap.setBackgroundResource(0);
                llAddCoupon.setBackgroundResource(0);
                llFriendRequest.setBackgroundResource(0);
                llSettings.setBackgroundResource(0);
                llAbout.setBackgroundResource(0);
                llLogout.setBackgroundResource(0);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentStoreNow fragmentStoreNow = new FragmentStoreNow();
                        fragmentTransaction.replace(R.id.fragment_container, fragmentStoreNow);
                        fragmentTransaction.commit();
                    }
                }, 300);

                //drawerLayout.closeDrawer(profileDrawer);
                llStoreNow.post(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.closeDrawer(profileDrawer);
                    }
                });
            }
        });

        llChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                llProfile.setBackgroundResource(0);
                llStoreNow.setBackgroundResource(0);
                llChatRoom.setBackgroundResource(R.drawable.select);
                llStoreMap.setBackgroundResource(0);
                llAddCoupon.setBackgroundResource(0);
                llFriendRequest.setBackgroundResource(0);
                llSettings.setBackgroundResource(0);
                llAbout.setBackgroundResource(0);
                llLogout.setBackgroundResource(0);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentChatRoom fragmentChatRoom = new FragmentChatRoom();
                        fragmentTransaction.replace(R.id.fragment_container, fragmentChatRoom);
                        fragmentTransaction.commit();
                    }
                }, 300);

                //drawerLayout.closeDrawer(profileDrawer);
                llChatRoom.post(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.closeDrawer(profileDrawer);
                    }
                });
            }
        });

        llStoreMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                llProfile.setBackgroundResource(0);
                llStoreNow.setBackgroundResource(0);
                llChatRoom.setBackgroundResource(0);
                llStoreMap.setBackgroundResource(R.drawable.select);
                llAddCoupon.setBackgroundResource(0);
                llFriendRequest.setBackgroundResource(0);
                llSettings.setBackgroundResource(0);
                llAbout.setBackgroundResource(0);
                llLogout.setBackgroundResource(0);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentStoreMap fragmentStoreMap = new FragmentStoreMap();
                        fragmentTransaction.replace(R.id.fragment_container, fragmentStoreMap);
                        fragmentTransaction.commit();
                    }
                }, 300);

                // drawerLayout.closeDrawer(profileDrawer);
                llStoreMap.post(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.closeDrawer(profileDrawer);
                    }
                });
            }
        });


        llAddCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                llProfile.setBackgroundResource(0);
                llStoreNow.setBackgroundResource(0);
                llChatRoom.setBackgroundResource(0);
                llStoreMap.setBackgroundResource(0);
                llAddCoupon.setBackgroundResource(R.drawable.select);
                llFriendRequest.setBackgroundResource(0);
                llSettings.setBackgroundResource(0);
                llAbout.setBackgroundResource(0);
                llLogout.setBackgroundResource(0);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentAddCoupon fragmentAddCoupon = new FragmentAddCoupon();
                        fragmentTransaction.replace(R.id.fragment_container, fragmentAddCoupon);
                        fragmentTransaction.commit();
                    }
                }, 300);

                //drawerLayout.closeDrawer(profileDrawer);
                llAddCoupon.post(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.closeDrawer(profileDrawer);
                    }
                });
            }
        });

        llFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                llProfile.setBackgroundResource(0);
                llStoreNow.setBackgroundResource(0);
                llChatRoom.setBackgroundResource(0);
                llStoreMap.setBackgroundResource(0);
                llAddCoupon.setBackgroundResource(0);
                llFriendRequest.setBackgroundResource(R.drawable.select);
                llSettings.setBackgroundResource(0);
                llAbout.setBackgroundResource(0);
                llLogout.setBackgroundResource(0);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentFriendRequest fragmentFriendRequest = new FragmentFriendRequest();
                        fragmentTransaction.replace(R.id.fragment_container, fragmentFriendRequest);
                        fragmentTransaction.commit();
                    }
                }, 300);

                // drawerLayout.closeDrawer(profileDrawer);
                llFriendRequest.post(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.closeDrawer(profileDrawer);
                    }
                });
            }
        });

        llSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                llProfile.setBackgroundResource(0);
                llStoreNow.setBackgroundResource(0);
                llChatRoom.setBackgroundResource(0);
                llStoreMap.setBackgroundResource(0);
                llAddCoupon.setBackgroundResource(0);
                llFriendRequest.setBackgroundResource(0);
                llSettings.setBackgroundResource(R.drawable.select);
                llAbout.setBackgroundResource(0);
                llLogout.setBackgroundResource(0);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentSettings fragmentSettings = new FragmentSettings();
                        fragmentTransaction.replace(R.id.fragment_container, fragmentSettings);
                        fragmentTransaction.commit();
                    }
                }, 300);

                // drawerLayout.closeDrawer(profileDrawer);
                llSettings.post(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.closeDrawer(profileDrawer);
                    }
                });
            }
        });

        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                llProfile.setBackgroundResource(0);
                llStoreNow.setBackgroundResource(0);
                llChatRoom.setBackgroundResource(0);
                llStoreMap.setBackgroundResource(0);
                llAddCoupon.setBackgroundResource(0);
                llFriendRequest.setBackgroundResource(0);
                llSettings.setBackgroundResource(0);
                llAbout.setBackgroundResource(R.drawable.select);
                llLogout.setBackgroundResource(0);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentAbout fragmentAbout = new FragmentAbout();
                        fragmentTransaction.replace(R.id.fragment_container, fragmentAbout);
                        fragmentTransaction.commit();
                    }
                }, 300);

                //drawerLayout.closeDrawer(profileDrawer);
                llAbout.post(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.closeDrawer(profileDrawer);
                    }
                });
            }
        });

        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                llProfile.setBackgroundResource(0);
                llStoreNow.setBackgroundResource(0);
                llChatRoom.setBackgroundResource(0);
                llStoreMap.setBackgroundResource(0);
                llAddCoupon.setBackgroundResource(0);
                llFriendRequest.setBackgroundResource(0);
                llSettings.setBackgroundResource(0);
                llAbout.setBackgroundResource(0);
                llLogout.setBackgroundResource(R.drawable.select);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        //finish();
                        final Dialog dialogChooser = new Dialog(ActivityLandingPage.this, R.style.DialogSlideAnim);
                        dialogChooser.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogChooser.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialogChooser.getWindow().setGravity(Gravity.CENTER);
                        dialogChooser.setContentView(R.layout.dialog_logout_option);
                        dialogChooser.setCanceledOnTouchOutside(true);

                        LinearLayout llNo = (LinearLayout) dialogChooser.findViewById(R.id.ll_no);
                        LinearLayout llYes = (LinearLayout) dialogChooser.findViewById(R.id.ll_yes);

                        dialogChooser.show();

                        llNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogChooser.dismiss();
                            }
                        });
                        llYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogChooser.dismiss();
                                makeJsonObjectLogout("http://www.esolz.co.in/lab9/aiCafe/iosapp/logout.php", AppData.loginDataType.getId());
                                // makeJsonObjectLogout("http://www.esolz.co.in/lab9/aiCafe/iosapp/logout.php?id=" + AppData.loginDataType.getId());
                            }
                        });
                    }
                }, 300);

                //drawerLayout.closeDrawer(profileDrawer);
                llLogout.post(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.closeDrawer(profileDrawer);
                    }
                });
            }
        });

    }

    private void makeJsonObjectLogout(final String URL, final String ID) {

        progressDialog = new ProgressDialog(ActivityLandingPage.this);
//        progressDialog.setMessage("Please wait...");
//        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);
                        try {
                            JSONObject response = new JSONObject(stringResponse);
                            if (response.getString("auth").equals("logout success")) {

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();

                                Intent intent = new Intent(ActivityLandingPage.this, ActivityLogin.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), response.getString("auth"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d("JSONException", e.toString());
                            Toast.makeText(getApplicationContext(), "Server not responding...", Toast.LENGTH_SHORT).show();
                        }
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Output : ", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Server not responding...!", Toast.LENGTH_LONG)
                        .show();
                // pBAR.setVisibility(View.GONE);
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

    @Override
    protected void onStart() {

//        myReceiver = new MyReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(GCMIntentService.MY_EVENT_ACTION);
//        registerReceiver(myReceiver, intentFilter);


        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppController.setIsAppRunning("NO");
        // Toast.makeText(getApplicationContext(), "Destroy" + AppController.isAppRunning(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        // unregisterReceiver(myReceiver);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub

            Toast.makeText(getApplicationContext(), "test receiver", Toast.LENGTH_SHORT).show();


            try {
                Log.d("chat_id =====", intent.getStringExtra("chat_id"));
                Log.d("send_from =====", intent.getStringExtra("send_from"));
                Log.d("send_to =====", intent.getStringExtra("send_to"));
                Log.d("message =====", intent.getStringExtra("message"));
                Log.d("type =====", intent.getStringExtra("type"));
                Log.d("stickername =====", intent.getStringExtra("stickername"));
                Log.d("chat_time =====", intent.getStringExtra("chat_time"));
                Log.d("chat_date =====", intent.getStringExtra("chat_date"));
                Log.d("status =====", intent.getStringExtra("status"));
                Log.d("file_link =====", intent.getStringExtra("file_link"));
                Log.d("file_available =====", intent.getStringExtra("file_available"));
                Log.d("name =====", intent.getStringExtra("name"));
                Log.d("photo =====", intent.getStringExtra("photo"));
                Log.d("photo_thumb =====", intent.getStringExtra("photo_thumb"));
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
            }


        }

    }
}
