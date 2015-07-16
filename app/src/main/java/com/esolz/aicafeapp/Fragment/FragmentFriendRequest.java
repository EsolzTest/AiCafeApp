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

import com.esolz.aicafeapp.Adapter.FriendRequestAdapter;
import com.esolz.aicafeapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ltp on 08/07/15.
 */
public class FragmentFriendRequest extends Fragment {

    View view;
    LinearLayout llPipeContainer, slidingNow, llBack, profileDrawer;
    RelativeLayout rlMSGContainer;
    TextView txtPageTitle, txtMSGCounter;
    ImageView imgBack, imgMSG;
    DrawerLayout drawerLayout;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    ListView listFriendRequest;
    ProgressBar pbarFriendRequest;

    ArrayList<HashMap<String, String>> data;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_friendrequest, container, false);

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
        //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        profileDrawer = (LinearLayout) getActivity().findViewById(R.id.profile_drawer);
        //drawerLayout.closeDrawer(profileDrawer);

        data = new ArrayList<HashMap<String, String>>();
        listFriendRequest = (ListView) view.findViewById(R.id.list_friendrequest);

        for (int i = 0; i < 7; i++) {
            HashMap<String, String> hMap = new HashMap<String, String>();
            hMap.put("value", "" + i);
            data.add(hMap);
        }

        FriendRequestAdapter dietAdapter = new FriendRequestAdapter(getActivity(), 0, 0, data);
        listFriendRequest.setAdapter(dietAdapter);


        llPipeContainer.setVisibility(View.VISIBLE);
        slidingNow.setVisibility(View.VISIBLE);
        rlMSGContainer.setVisibility(View.VISIBLE);
        txtPageTitle.setVisibility(View.VISIBLE);
        imgBack.setVisibility(View.GONE);
        llBack.setVisibility(View.GONE);
        imgMSG.setVisibility(View.GONE);
        txtMSGCounter.setVisibility(View.GONE);

        txtPageTitle.setText("Friend Request");

        return view;
    }
}
