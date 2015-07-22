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
import com.esolz.aicafeapp.R;

/**
 * Created by ltp on 08/07/15.
 */
public class FragmentStoreNow extends Fragment {

    View view;
    LinearLayout llPipeContainer, slidingNow, llBack, profileDrawer;
    RelativeLayout rlMSGContainer;
    OpenSansSemiboldTextView txtPageTitle;
    OpenSansRegularTextView txtMSGCounter;
    ImageView imgBack, imgMSG;
    DrawerLayout drawerLayout;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    LinearLayout llStoreName, llHereNow, llStoreOffer, llFbLike;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_storenow, container, false);

        fragmentManager = getFragmentManager();

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


        llStoreName = (LinearLayout) view.findViewById(R.id.ll_store_name);
        llHereNow = (LinearLayout) view.findViewById(R.id.ll_here_now);
        llStoreOffer = (LinearLayout) view.findViewById(R.id.ll_store_offer);
        llFbLike = (LinearLayout) view.findViewById(R.id.ll_fb_like);

        llStoreName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        llHereNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentAllFriends fragmentAllFriends = new FragmentAllFriends();
                fragmentTransaction.replace(R.id.fragment_container, fragmentAllFriends);
                fragmentTransaction.commit();
            }
        });
        llStoreOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        llFbLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        llPipeContainer.setVisibility(View.VISIBLE);
        slidingNow.setVisibility(View.VISIBLE);
        rlMSGContainer.setVisibility(View.VISIBLE);
        txtPageTitle.setVisibility(View.VISIBLE);
        imgBack.setVisibility(View.GONE);
        llBack.setVisibility(View.GONE);
        imgMSG.setVisibility(View.GONE);
        txtMSGCounter.setVisibility(View.GONE);

        imgMSG.setBackgroundResource(R.drawable.chat);

        txtPageTitle.setText("Store Now");

        return view;
    }
}
