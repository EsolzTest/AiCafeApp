package com.esolz.aicafeapp.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.esolz.aicafeapp.Adapter.SingleChatAdapter;
import com.esolz.aicafeapp.Adapter.StikersGridAdapter;
import com.esolz.aicafeapp.Datatype.ChatViewDataType;
import com.esolz.aicafeapp.Datatype.FriendListDataType;
import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.Helper.ConnectionDetector;
import com.esolz.aicafeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ltp on 21/07/15.
 */
public class FragmentSingleChat extends Fragment {

    View view;
    LinearLayout llPipeContainer, slidingNow, llBack;
    RelativeLayout rlMSGContainer;
    TextView txtPageTitle, txtMSGCounter;
    ImageView imgBack, imgMSG;
    DrawerLayout drawerLayout;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    LinearLayout llSendChat, llStickerContainer;
    RelativeLayout llStickerChat;
    GridView gridView;
    HorizontalScrollView horizontalScrollView;
    ListView listSingleChat;

    ChatViewDataType chatViewDataType;
    ArrayList<ChatViewDataType> chatViewDataTypeArrayList;

    ProgressBar pbarSingleChat;

    ConnectionDetector cd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_singlechat, container, false);

        fragmentManager = getFragmentManager();

        cd = new ConnectionDetector(getActivity());

        llPipeContainer = (LinearLayout) getActivity().findViewById(R.id.ll_pipe_container);
        slidingNow = (LinearLayout) getActivity().findViewById(R.id.slidingnow);
        rlMSGContainer = (RelativeLayout) getActivity().findViewById(R.id.rl_msgcontainer);
        txtPageTitle = (TextView) getActivity().findViewById(R.id.txt_page_title);
        imgBack = (ImageView) getActivity().findViewById(R.id.img_back);
        llBack = (LinearLayout) getActivity().findViewById(R.id.ll_back);
        txtMSGCounter = (TextView) getActivity().findViewById(R.id.txt_msg_counter);
        imgMSG = (ImageView) getActivity().findViewById(R.id.img_msg);

        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        llStickerChat = (RelativeLayout) view.findViewById(R.id.ll_sticker_chat);
        llSendChat = (LinearLayout) view.findViewById(R.id.ll_send_chat);
        horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.hscr_view);
        horizontalScrollView.setVisibility(View.GONE);

        pbarSingleChat = (ProgressBar) view.findViewById(R.id.pbar_single_chat);
        pbarSingleChat.setVisibility(View.GONE);

        listSingleChat = (ListView) view.findViewById(R.id.list_single_chat);
        listSingleChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                horizontalScrollView.setVisibility(View.GONE);
            }
        });

        gridView = (GridView) view.findViewById(R.id.grid);

        AppData.stikersHolder = new ArrayList<>();
        AppData.stikersHolder.add(R.drawable.aione);
        AppData.stikersHolder.add(R.drawable.aitwo);
        AppData.stikersHolder.add(R.drawable.aithree);
        AppData.stikersHolder.add(R.drawable.aifour);
        AppData.stikersHolder.add(R.drawable.aifive);
        AppData.stikersHolder.add(R.drawable.aisix);
        AppData.stikersHolder.add(R.drawable.aiseven);
        AppData.stikersHolder.add(R.drawable.aieight);

        StikersGridAdapter stikersGridAdapter = new StikersGridAdapter(getActivity(), AppData.stikersHolder);
        gridView.setAdapter(stikersGridAdapter);
        int cols = (int) Math.ceil((double) (AppData.stikersHolder.size()) / 2);
        gridView.setNumColumns(cols);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });

        if (cd.isConnectingToInternet()) {
//            getAllChatDetails("http://203.196.159.37//lab9/aiCafe/iosapp/chat_view.php?send_id=" + AppData.loginDataType.getId() + "&rec_id=25&start=1&end=5");
            getAllChatDetails("http://203.196.159.37//lab9/aiCafe/iosapp/chat_view.php?send_id=21&rec_id=40&start=1&end=5");
        } else {
            Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
        }

        llStickerChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.VISIBLE);
            }
        });
        llSendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
            }
        });

        llPipeContainer.setVisibility(View.GONE);
        slidingNow.setVisibility(View.GONE);
        rlMSGContainer.setVisibility(View.GONE);
        txtPageTitle.setVisibility(View.VISIBLE);
        imgBack.setVisibility(View.VISIBLE);
        llBack.setVisibility(View.VISIBLE);
        imgMSG.setVisibility(View.GONE);
        txtMSGCounter.setVisibility(View.GONE);

        txtPageTitle.setText("Chat");

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("USER_ID", getArguments().getString("USER_ID"));

                if (getArguments().getString("Page").equals("FragmentUserInformation")) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentUserInformation fragmentUserInformation = new FragmentUserInformation();
                    fragmentUserInformation.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fragment_container, fragmentUserInformation);
                    fragmentTransaction.commit();
                } else {

                }
            }
        });

        return view;
    }

    private void getAllChatDetails(String URL) {

        //pbarSingleChat.setVisibility(View.VISIBLE);
        // listSingleChat.setVisibility(View.GONE);

        Log.d("Single chat url", URL);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                //pbarSingleChat.setVisibility(View.GONE);
                //listSingleChat.setVisibility(View.VISIBLE);
                try {
                    JSONArray jsonArray = response.getJSONArray("details");
                    chatViewDataTypeArrayList = new ArrayList<ChatViewDataType>();
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            chatViewDataType = new ChatViewDataType(
                                    jsonObject.getString("chat_id"),
                                    jsonObject.getString("send_from"),
                                    jsonObject.getString("send_to"),
                                    jsonObject.getString("message"),
                                    jsonObject.getString("type"),
                                    jsonObject.getString("stickername"),
                                    jsonObject.getString("chat_time"),
                                    jsonObject.getString("chat_date"),
                                    jsonObject.getString("status"),
                                    jsonObject.getString("file_link"),
                                    jsonObject.getString("file_available"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("photo"),
                                    jsonObject.getString("photo_thumb")
                            );
                            chatViewDataTypeArrayList.add(chatViewDataType);
                            SingleChatAdapter singleChatAdapter = new SingleChatAdapter(getActivity(), 0, 0, chatViewDataTypeArrayList);
                            listSingleChat.setAdapter(singleChatAdapter);
                        }
                    }
                } catch (JSONException e) {
                    Log.d("JSONException", e.toString());
                    Toast.makeText(getActivity(), "Server not responding...", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}

