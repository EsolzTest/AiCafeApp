package com.esolz.aicafeapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esolz.aicafeapp.Customviews.OpenSansRegularTextView;
import com.esolz.aicafeapp.Customviews.OpenSansSemiboldTextView;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.Helper.CircleTransform;
import com.esolz.aicafeapp.Helper.Trns;
import com.esolz.aicafeapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by ltp on 08/07/15.
 */
public class FragmentProfile extends Fragment {

    View view;
    LinearLayout llLoyaltyPoints, llCoupons, llAiCafeFriends;
    LinearLayout llPipeContainer, slidingNow, llBack, profileDrawer;
    RelativeLayout rlMSGContainer;
    OpenSansSemiboldTextView txtPageTitle;
    OpenSansRegularTextView txtMSGCounter;
    ImageView imgBack, imgMSG;
    DrawerLayout drawerLayout;

    ImageView imgProfile;
    LinearLayout llEdit;
    OpenSansSemiboldTextView txtProfileName;
    OpenSansRegularTextView txtAge, txtGender, txtStatus;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_profile, container, false);

        fragmentManager = getFragmentManager();

        llLoyaltyPoints = (LinearLayout) view.findViewById(R.id.ll_loyalty_points);
        llCoupons = (LinearLayout) view.findViewById(R.id.ll_coupons);
        llAiCafeFriends = (LinearLayout) view.findViewById(R.id.ll_aicafe_friends);

        llPipeContainer = (LinearLayout) getActivity().findViewById(R.id.ll_pipe_container);
        slidingNow = (LinearLayout) getActivity().findViewById(R.id.slidingnow);
        rlMSGContainer = (RelativeLayout) getActivity().findViewById(R.id.rl_msgcontainer);
        txtPageTitle = (OpenSansSemiboldTextView) getActivity().findViewById(R.id.txt_page_title);
        imgBack = (ImageView) getActivity().findViewById(R.id.img_back);
        llBack = (LinearLayout) getActivity().findViewById(R.id.ll_back);
        txtMSGCounter = (OpenSansRegularTextView) getActivity().findViewById(R.id.txt_msg_counter);
        imgMSG = (ImageView) getActivity().findViewById(R.id.img_msg);

        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        // drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        profileDrawer = (LinearLayout) getActivity().findViewById(R.id.profile_drawer);
        //drawerLayout.closeDrawer(profileDrawer);

        llPipeContainer.setVisibility(View.VISIBLE);
        slidingNow.setVisibility(View.VISIBLE);
        rlMSGContainer.setVisibility(View.VISIBLE);
        txtPageTitle.setVisibility(View.VISIBLE);
        imgBack.setVisibility(View.GONE);
        llBack.setVisibility(View.GONE);
        imgMSG.setVisibility(View.VISIBLE);
        txtMSGCounter.setVisibility(View.GONE);
        txtPageTitle.setText("Profile");

        imgMSG.setBackgroundResource(R.drawable.message);

        imgProfile = (ImageView) view.findViewById(R.id.img_profile);
        llEdit = (LinearLayout) view.findViewById(R.id.ll_edit);
        txtProfileName = (OpenSansSemiboldTextView) view.findViewById(R.id.txt_profile_name);
        txtAge = (OpenSansRegularTextView) view.findViewById(R.id.txt_age);
        txtGender = (OpenSansRegularTextView) view.findViewById(R.id.txt_gender);
        txtStatus = (OpenSansRegularTextView) view.findViewById(R.id.txt_status);

        txtProfileName.setText(AppData.loginDataType.getName());
        txtAge.setText("Age : " + AppData.loginDataType.getAge());
        if (AppData.loginDataType.getSex().equals("M")) {
            txtGender.setText("Male");
        } else {
            txtGender.setText("Female");
        }
        txtStatus.setText(AppData.loginDataType.getAbout());

        Picasso.with(getActivity()).load("http://www.esolz.co.in/lab9/aiCafe/" + AppData.loginDataType.getPhoto())
                .transform(new CircleTransform()).into(imgProfile);

        llLoyaltyPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentLoyaltyPoints fragmentLoyaltyPoints = new FragmentLoyaltyPoints();
                fragmentTransaction.replace(R.id.fragment_container, fragmentLoyaltyPoints);
                fragmentTransaction.commit();
            }
        });

        llCoupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentCoupons fragmentCoupons = new FragmentCoupons();
                fragmentTransaction.replace(R.id.fragment_container, fragmentCoupons);
                fragmentTransaction.commit();
            }
        });

        llAiCafeFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentAiCafeFriends fragmentAiCafeFriends = new FragmentAiCafeFriends();
                fragmentTransaction.replace(R.id.fragment_container, fragmentAiCafeFriends);
                fragmentTransaction.commit();
            }
        });

        rlMSGContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentInbox fragmentInbox = new FragmentInbox();
                fragmentTransaction.replace(R.id.fragment_container, fragmentInbox);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
