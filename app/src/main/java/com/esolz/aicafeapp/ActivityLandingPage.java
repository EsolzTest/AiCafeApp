package com.esolz.aicafeapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esolz.aicafeapp.Fragment.FragmentAbout;
import com.esolz.aicafeapp.Fragment.FragmentAddCoupon;
import com.esolz.aicafeapp.Fragment.FragmentChatRoom;
import com.esolz.aicafeapp.Fragment.FragmentFriendRequest;
import com.esolz.aicafeapp.Fragment.FragmentProfile;
import com.esolz.aicafeapp.Fragment.FragmentSettings;
import com.esolz.aicafeapp.Fragment.FragmentStoreMap;
import com.esolz.aicafeapp.Fragment.FragmentStoreNow;

/**
 * Created by ltp on 08/07/15.
 */
public class ActivityLandingPage extends AppCompatActivity {

    LinearLayout slidingNow, profileDrawer, llProfile, llStoreNow, llChatRoom,
            llStoreMap, llAddCoupon, llFriendRequest, llSettings, llAbout, llLogout,
            llPipeContainer, llBack;
    RelativeLayout rlMSGContainer;
    TextView txtPageTitle, txtMSGCounter;
    ImageView imgBack;

    DrawerLayout drawerLayout;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landingpage);

        fragmentManager = getSupportFragmentManager();

        llPipeContainer = (LinearLayout)findViewById(R.id.ll_pipe_container);
        rlMSGContainer = (RelativeLayout) findViewById(R.id.rl_msgcontainer);
        txtPageTitle = (TextView) findViewById(R.id.txt_page_title);
        imgBack = (ImageView) findViewById(R.id.img_back);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        txtMSGCounter = (TextView)findViewById(R.id.txt_msg_counter);

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
                drawerLayout.openDrawer(profileDrawer);
            }
        });

        fragmentTransaction = fragmentManager.beginTransaction();
        FragmentProfile fragmentProfile = new FragmentProfile();
        fragmentTransaction.replace(R.id.fragment_container, fragmentProfile);
        fragmentTransaction.commit();

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

                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentProfile fragmentProfile = new FragmentProfile();
                fragmentTransaction.replace(R.id.fragment_container, fragmentProfile);
                fragmentTransaction.commit();

                drawerLayout.closeDrawer(profileDrawer);
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

                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentStoreNow fragmentStoreNow = new FragmentStoreNow();
                fragmentTransaction.replace(R.id.fragment_container, fragmentStoreNow);
                fragmentTransaction.commit();

                drawerLayout.closeDrawer(profileDrawer);
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

                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentChatRoom fragmentChatRoom = new FragmentChatRoom();
                fragmentTransaction.replace(R.id.fragment_container, fragmentChatRoom);
                fragmentTransaction.commit();

                drawerLayout.closeDrawer(profileDrawer);
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

                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentStoreMap fragmentStoreMap = new FragmentStoreMap();
                fragmentTransaction.replace(R.id.fragment_container, fragmentStoreMap);
                fragmentTransaction.commit();

                drawerLayout.closeDrawer(profileDrawer);
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

                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentAddCoupon fragmentAddCoupon = new FragmentAddCoupon();
                fragmentTransaction.replace(R.id.fragment_container, fragmentAddCoupon);
                fragmentTransaction.commit();

                drawerLayout.closeDrawer(profileDrawer);
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

                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentFriendRequest fragmentFriendRequest = new FragmentFriendRequest();
                fragmentTransaction.replace(R.id.fragment_container, fragmentFriendRequest);
                fragmentTransaction.commit();

                drawerLayout.closeDrawer(profileDrawer);
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

                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentSettings fragmentSettings = new FragmentSettings();
                fragmentTransaction.replace(R.id.fragment_container, fragmentSettings);
                fragmentTransaction.commit();

                drawerLayout.closeDrawer(profileDrawer);
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

                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentAbout fragmentAbout = new FragmentAbout();
                fragmentTransaction.replace(R.id.fragment_container, fragmentAbout);
                fragmentTransaction.commit();

                drawerLayout.closeDrawer(profileDrawer);
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

                //drawerLayout.closeDrawer(profileDrawer);

                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
