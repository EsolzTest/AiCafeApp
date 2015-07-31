package com.esolz.aicafeapp.Fragment;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.esolz.aicafeapp.Adapter.ChatRecyclerAdapter;
import com.esolz.aicafeapp.Adapter.GroupChatAdapter;
import com.esolz.aicafeapp.Adapter.SingleChatAdapter;
import com.esolz.aicafeapp.Adapter.StikersGridAdapter;
import com.esolz.aicafeapp.Adapter.UserListIngAdapter;
import com.esolz.aicafeapp.Customviews.OpenSansRegularTextView;
import com.esolz.aicafeapp.Customviews.OpenSansSemiboldEditText;
import com.esolz.aicafeapp.Datatype.ChatViewDataType;
import com.esolz.aicafeapp.Datatype.UserListingDataType;
import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.Helper.CircleTransform;
import com.esolz.aicafeapp.Helper.ConnectionDetector;
import com.esolz.aicafeapp.Helper.RecyclerItemClickListener;
import com.esolz.aicafeapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Esolz Pvt. Ltd. on 08/07/15.
 */
public class FragmentChatRoom extends Fragment {

    View view;
    LinearLayout llLoyaltyPoints, llCoupons, llAiCafeFriends;
    LinearLayout llPipeContainer, slidingNow, llBack, profileDrawer;
    RelativeLayout rlMSGContainer;
    TextView txtPageTitle, txtMSGCounter;
    ImageView imgBack, imgMSG;
    DrawerLayout drawerLayout;
    View cView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    //========================================

    LinearLayout allMemberHeader;
    ArrayList<UserListingDataType> userListingDataTypeArrayList;
    LayoutInflater inflater;
    ProgressBar toploader;
    final int CHUNK_SIZE = 10;
    int totalResponseValue = 0;
    ArrayList<ChatViewDataType> chatViewDataTypeArrayList;
    SingleChatAdapter singleChatAdapter;
    ListView listSingleChat;
    LinearLayout llSendChat, llStickerContainer;
    RelativeLayout llStickerChat;
    GridView gridView;
    HorizontalScrollView horizontalScrollView;
    OpenSansRegularTextView txtError;
    OpenSansSemiboldEditText etChatSend;
    ProgressBar pbarSingleChat;
    LinearLayout progressPanelForSent;

    ConnectionDetector cd;

    LinearLayoutManager llm;
    RecyclerView rv;
    ChatRecyclerAdapter adapter;

    ArrayList<String> arrSenderId;
    String getAllSenderIdId = "";

    GroupChatAdapter groupChatAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_chatroom, container, false);

        fragmentManager = getFragmentManager();

        cd = new ConnectionDetector(getActivity());

        llLoyaltyPoints = (LinearLayout) view.findViewById(R.id.ll_loyalty_points);
        llCoupons = (LinearLayout) view.findViewById(R.id.ll_coupons);
        llAiCafeFriends = (LinearLayout) view.findViewById(R.id.ll_aicafe_friends);

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

        llPipeContainer.setVisibility(View.VISIBLE);
        slidingNow.setVisibility(View.VISIBLE);
        rlMSGContainer.setVisibility(View.VISIBLE);
        txtPageTitle.setVisibility(View.VISIBLE);
        imgBack.setVisibility(View.GONE);
        llBack.setVisibility(View.GONE);
        imgMSG.setVisibility(View.GONE);
        txtMSGCounter.setVisibility(View.GONE);

        txtPageTitle.setText("Chat Room");

        rv = (RecyclerView) view.findViewById(R.id.recycler); //kk
        llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//
//                String name = userListingDataTypeArrayList.get(position).getId();
//                Bundle bundle = new Bundle();
//                bundle.putString("USER_ID", name);
//
//                fragmentTransaction = fragmentManager.beginTransaction();
//                FragmentUserInformation fragmentUserInformation = new FragmentUserInformation();
//                fragmentUserInformation.setArguments(bundle);
//                fragmentTransaction.replace(R.id.fragment_container, fragmentUserInformation);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
                String name = userListingDataTypeArrayList.get(position).getId();
                Bundle bundle = new Bundle();
                bundle.putString("USER_ID", name);
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentUserInformation fragmentUserInformation = new FragmentUserInformation();
                fragmentUserInformation.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, fragmentUserInformation);
                int count = fragmentManager.getBackStackEntryCount();
                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));


        //.............Main Code addition....
//        allMemberHeader = (LinearLayout) view.findViewById(R.id.alluserchatroom);
//        this.inflater = LayoutInflater.from(getActivity());

//.........Fetching All member List....


        //--------------------today ----------

        toploader = (ProgressBar) view.findViewById(R.id.pbar_single_chat);
        progressPanelForSent = (LinearLayout) view.findViewById(R.id.loaderbucket);
        progressPanelForSent.setVisibility(View.GONE);


        llStickerChat = (RelativeLayout) view.findViewById(R.id.ll_sticker_chat);
        llSendChat = (LinearLayout) view.findViewById(R.id.ll_send_chat);
        horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.hscr_view);
        horizontalScrollView.setVisibility(View.GONE);

        txtError = (OpenSansRegularTextView) view.findViewById(R.id.txt_error);
        txtError.setVisibility(View.GONE);

        pbarSingleChat = (ProgressBar) view.findViewById(R.id.pbar_single_chat);
        pbarSingleChat.setVisibility(View.GONE);

        etChatSend = (OpenSansSemiboldEditText) view.findViewById(R.id.et_chat_send);

        listSingleChat = (ListView) view.findViewById(R.id.list_single_chat);
        listSingleChat.setDivider(null);
        listSingleChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                horizontalScrollView.setVisibility(View.GONE);
            }
        });

        if (cd.isConnectingToInternet()) {
            makeJsonObjectRequest("http://203.196.159.37/lab9/aiCafe/iosapp/user_listing.php", AppData.loginDataType.getId());
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

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
                Toast.makeText(getActivity(), "" + getStikerNo(position), Toast.LENGTH_SHORT).show();
                if (cd.isConnectingToInternet()) {
                    horizontalScrollView.setVisibility(View.GONE);
                    try {
                        if (AppController.isSoundON().equals("ON")) {
                            MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.beep4);
                            mp.start();
                        } else {
                        }

                        sendMessage("http://www.esolz.co.in/lab9/aiCafe/iosapp/sendGroupUser.php",
                                AppData.loginDataType.getId(),
                                getAllSenderIdId,
                                "test",
                                "s",
                                getStikerNo(position),
                                "G");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                if (cd.isConnectingToInternet()) {
                    try {


                        if (AppController.isSoundON().equals("ON")) {
                            MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.beep4);
                            mp.start();
                        } else {
                        }

                        sendMessage("http://www.esolz.co.in/lab9/aiCafe/iosapp/sendGroupUser.php",
                                AppData.loginDataType.getId(),
                                getAllSenderIdId,
                                URLEncoder.encode(etChatSend.getText().toString().trim(), "UTF-8"),
                                "m",
                                "",
                                "G");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // --------------------end ----------


        return view;
    }

    public String getStikerNo(int position) {
        if (position == 0) {
            return "1.png";
        } else if (position == 1) {
            return "2.png";
        } else if (position == 2) {
            return "3.png";
        } else if (position == 3) {
            return "4.png";
        } else if (position == 4) {
            return "5.png";
        } else if (position == 5) {
            return "6.png";
        } else if (position == 6) {
            return "7.png";
        } else {
            return "8.png";
        }
    }

    private void sendMessage(final String URL, final String sendID, final String recID,
                             final String message, final String type, final String stikername, final String chatType) {
        progressPanelForSent.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.i("CHAT RES @@@", stringResponse);
                        Log.i("CHAT send", URL);
                        try {
                            JSONObject response = new JSONObject(stringResponse);
                            if (response.getString("status").equals("success")) {
                                JSONObject jInnerObj = response.getJSONObject("details");
                                ChatViewDataType chatViewDataType = new ChatViewDataType(
                                        jInnerObj.getString("" + "chat_id"),
                                        jInnerObj.getString("" + "send_from"),
                                        jInnerObj.getString("send_to"),
                                        jInnerObj.getString("message"),
                                        jInnerObj.getString("type"),
                                        jInnerObj.getString("stickername"),
                                        jInnerObj.getString("chat_time"),
                                        jInnerObj.getString("chat_date"),
                                        jInnerObj.getString("status"),
                                        jInnerObj.getString("file_link"),
                                        jInnerObj.getString("file_available"),
                                        jInnerObj.getString("name"),
                                        jInnerObj.getString("photo"),
                                        jInnerObj.getString("photo_thumb")
                                );
                                groupChatAdapter.addFromReceiver(chatViewDataType);
                                progressPanelForSent.setVisibility(View.GONE);
                                etChatSend.setText("");
                                Toast.makeText(getActivity(), "Message send.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Message not send.", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("CHAT RES SAKU", e.toString());
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
                params.put("send_id", sendID);
                params.put("rec_id", recID);
                params.put("message", message);
                params.put("type", type);
                params.put("stickername", stikername);
                params.put("chat_type", chatType);

                Log.d("M send_id", sendID);
                Log.d("M rec_id", recID);
                Log.d("M message", message);

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


    private void makeJsonObjectRequest(final String URL, final String ID) {

        getAllSenderIdId = "";

        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);
                        try {
                            JSONObject response = new JSONObject(stringResponse);
                            JSONArray jsonArray = response.getJSONArray("details");
                            userListingDataTypeArrayList = new ArrayList<UserListingDataType>();

                            arrSenderId = new ArrayList<String>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UserListingDataType userListingDataType = new UserListingDataType(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("sex"),
                                        jsonObject.getString("email"),
                                        "",
                                        jsonObject.getString("about"),
                                        jsonObject.getString("business"),
                                        jsonObject.getString("dob"),
                                        jsonObject.getString("photo"),
                                        jsonObject.getString("photo_thumb"),
                                        jsonObject.getString("registerDate"),
                                        jsonObject.getString("facebookid"),
                                        jsonObject.getString("last_sync"),
                                        jsonObject.getString("fb_pic_url"),
                                        "" + jsonObject.getInt("age"),
                                        jsonObject.getString("friend")
                                );
                                userListingDataTypeArrayList.add(userListingDataType);
                                arrSenderId.add(jsonObject.getString("id"));

//                                inflater = LayoutInflater.from(getActivity());
//                                cView = inflater.inflate(R.layout.chat_room_headerdata, null);
//                                ImageView iView = (ImageView) cView.findViewById(R.id.imageView);
//                                Picasso.with(getActivity()).load("http://www.esolz.co.in/lab9/aiCafe/" + userListingDataType.getPhoto_thumb())
//                                        .fit().centerCrop().transform(new CircleTransform()).into(iView);
//                                allMemberHeader.addView(cView);
//                                allMemberHeader.requestLayout();
                            }
                        } catch (JSONException e) {
                            Log.d("JSONException", e.toString());
                            Toast.makeText(getActivity(), "Server not responding...", Toast.LENGTH_SHORT).show();
                        }
                        adapter = new ChatRecyclerAdapter(getActivity(), userListingDataTypeArrayList);
                        rv.setAdapter(adapter);

                        getAllChatDetails(
                                "http://www.esolz.co.in//lab9/aiCafe/iosapp/group_chat_view.php",
                                AppData.loginDataType.getId()
                        );

                        getAllSenderIdId = TextUtils.join(",", arrSenderId);
                        Log.d("@@ String Sender Id @@", getAllSenderIdId);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Output : ", "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Server not responding...!", Toast.LENGTH_LONG)
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


    private void getAllChatDetails(final String URL, final String sendID) {

        Log.d("Single chat url", URL);

        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);

                        try {
                            JSONObject response = new JSONObject(stringResponse);
                            totalResponseValue = Integer.parseInt(response.getString("total"));
                            if (response.getString("status").equals("success")) {
                                JSONArray jsonArray = response.getJSONArray("details");
                                chatViewDataTypeArrayList = new ArrayList<ChatViewDataType>();
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        ChatViewDataType chatViewDataType = new ChatViewDataType(
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
                                        //Collections.reverse(chatViewDataTypeArrayList);
                                    }
                                    Collections.reverse(chatViewDataTypeArrayList);
                                    groupChatAdapter = new GroupChatAdapter(getActivity(),
                                            0, 0,
                                            chatViewDataTypeArrayList,
                                            totalResponseValue,
                                            sendID, toploader, listSingleChat);
                                    listSingleChat.setAdapter(groupChatAdapter);
                                }
                            } else {
                                // txtError.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            Log.d("JSONException", e.toString());
                            Toast.makeText(getActivity(), "Server not responding...", Toast.LENGTH_SHORT).show();
                        }
                        //listSingleChat.setVisibility(View.VISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Output : ", "Error: " + error.getMessage());
                Log.d("Volley error : ", error.toString());
                Toast.makeText(getActivity(), "Server not responding...!volley", Toast.LENGTH_LONG).show();
                // pBAR.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("send_id", sendID);
                params.put("start", "0");
                params.put("records", "" + CHUNK_SIZE);

//                Log.d("send_id", sendID);
//                Log.d("rec_id", recID);
//                Log.d("start", start);
//                Log.d("records", records);

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