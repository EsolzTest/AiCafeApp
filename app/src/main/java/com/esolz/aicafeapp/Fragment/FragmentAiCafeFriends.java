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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esolz.aicafeapp.Adapter.FriendAdapter;
import com.esolz.aicafeapp.Adapter.InboxAdapter;
import com.esolz.aicafeapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ltp on 08/07/15.
 */
public class FragmentAiCafeFriends extends Fragment {

    View view;
    LinearLayout llPipeContainer, slidingNow, llBack;
    RelativeLayout rlMSGContainer;
    TextView txtPageTitle, txtMSGCounter;
    ImageView imgBack, imgMSG;
    DrawerLayout drawerLayout;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    ListView listFriend;
    ProgressBar pbarFriend;

    ArrayList<HashMap<String, String>> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_aicafefriends, container, false);

        fragmentManager = getFragmentManager();

        llPipeContainer = (LinearLayout) getActivity().findViewById(R.id.ll_pipe_container);
        slidingNow = (LinearLayout) getActivity().findViewById(R.id.slidingnow);
        rlMSGContainer = (RelativeLayout) getActivity().findViewById(R.id.rl_msgcontainer);
        txtPageTitle = (TextView) getActivity().findViewById(R.id.txt_page_title);
        imgBack = (ImageView) getActivity().findViewById(R.id.img_back);
        llBack = (LinearLayout) getActivity().findViewById(R.id.ll_back);
        txtMSGCounter = (TextView) getActivity().findViewById(R.id.txt_msg_counter);
        imgMSG = (ImageView) getActivity().findViewById(R.id.img_msg);

        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        data = new ArrayList<HashMap<String, String>>();
        listFriend = (ListView) view.findViewById(R.id.list_friend);

        for (int i = 0; i < 7; i++) {
            HashMap<String, String> hMap = new HashMap<String, String>();
            hMap.put("value", "" + i);
            data.add(hMap);
        }
        FriendAdapter friendAdapter = new FriendAdapter(getActivity(), 0, 0, data);
        listFriend.setAdapter(friendAdapter);

        llPipeContainer.setVisibility(View.GONE);
        slidingNow.setVisibility(View.GONE);
        rlMSGContainer.setVisibility(View.GONE);
        txtPageTitle.setVisibility(View.VISIBLE);
        imgBack.setVisibility(View.VISIBLE);
        llBack.setVisibility(View.VISIBLE);
        imgMSG.setVisibility(View.GONE);
        txtMSGCounter.setVisibility(View.GONE);

        txtPageTitle.setText("AiCafe Friends");

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentProfile fragmentProfile = new FragmentProfile();
                fragmentTransaction.replace(R.id.fragment_container, fragmentProfile);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}

