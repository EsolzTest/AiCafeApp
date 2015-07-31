package com.esolz.aicafeapp.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
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
import com.esolz.aicafeapp.Adapter.SingleChatAdapter;
import com.esolz.aicafeapp.Adapter.StikersGridAdapter;
import com.esolz.aicafeapp.Customviews.OpenSansRegularTextView;
import com.esolz.aicafeapp.Customviews.OpenSansSemiboldEditText;
import com.esolz.aicafeapp.Datatype.ChatViewDataType;
import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.Helper.ConnectionDetector;
import com.esolz.aicafeapp.R;
import com.esolz.aicafeapp.gcmnotification.GCMIntentService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    MyReceiver myReceiver;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    final int CHUNK_SIZE = 10;
    LinearLayout llSendChat, llStickerContainer;
    RelativeLayout llStickerChat;
    GridView gridView;
    HorizontalScrollView horizontalScrollView;
    ListView listSingleChat;
    ProgressBar toploader;
    ChatViewDataType chatViewDataType;
    ArrayList<ChatViewDataType> chatViewDataTypeArrayList;
    LinearLayout progressPanelForSent;
    ProgressBar pbarSingleChat;
    OpenSansRegularTextView txtError;
    OpenSansSemiboldEditText etChatSend;

    ConnectionDetector cd;
    SingleChatAdapter singleChatAdapter;
    int totalResponseValue = 0;

    String userId = "", pageIndicator = "";


    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub

            //Toast.makeText(getActivity(), "test receiver in fragment", Toast.LENGTH_SHORT).show();


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

                ChatViewDataType chatViewDataType = new ChatViewDataType(
                        intent.getStringExtra("chat_id"),
                        intent.getStringExtra("send_from"),
                        intent.getStringExtra("send_to"),
                        intent.getStringExtra("message"),
                        intent.getStringExtra("type"),
                        intent.getStringExtra("stickername"),
                        intent.getStringExtra("chat_time"),
                        intent.getStringExtra("chat_date"),
                        intent.getStringExtra("status"),
                        intent.getStringExtra("file_link"),
                        intent.getStringExtra("file_available"),
                        intent.getStringExtra("name"),
                        intent.getStringExtra("photo"),
                        intent.getStringExtra("photo_thumb"));
                // Toast.makeText(getActivity(), "Add me here...", Toast.LENGTH_SHORT).show();
                singleChatAdapter.addFromReceiver(chatViewDataType);
                //singleChatAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show();
            }


        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GCMIntentService.MY_EVENT_ACTION);
        getActivity().registerReceiver(myReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        try {
            getActivity().unregisterReceiver(myReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_singlechat, container, false);

        fragmentManager = getFragmentManager();

        cd = new ConnectionDetector(getActivity());

//        Log.d("=======send_id", AppData.loginDataType.getId());
//        Log.d("=======rec_id", getArguments().getString("USER_ID"));

//        try {
//
//        } catch (Exception e) {
//
//        }
        toploader = (ProgressBar) view.findViewById(R.id.pbar_single_chat);
        progressPanelForSent = (LinearLayout) view.findViewById(R.id.loaderbucket);
        progressPanelForSent.setVisibility(View.GONE);
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
                //Toast.makeText(getActivity(), "" + getStikerNo(position), Toast.LENGTH_SHORT).show();
                if (cd.isConnectingToInternet()) {
                    horizontalScrollView.setVisibility(View.GONE);


                    if (AppController.isSoundON().equals("ON")) {
                        MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.beep4);
                        mp.start();
                    } else {
                    }
                    sendMessage(
                            " http://www.esolz.co.in/lab9/aiCafe/iosapp/sendSingleUser.php",
                            AppData.loginDataType.getId(),
                            getArguments().getString("USER_ID"),
                            "test",
                            "s",
                            getStikerNo(position),
                            "O"
                    );
                } else {
                    Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (cd.isConnectingToInternet()) {
            //getAllChatDetails("http://203.196.159.37//lab9/aiCafe/iosapp/android_chat_view.php?send_id=43&rec_id=21&start=0&records=10");
            getAllChatDetails("http://203.196.159.37//lab9/aiCafe/iosapp/chat_view.php",
                    AppData.loginDataType.getId(),
                    getArguments().getString("USER_ID"),
                    "0",
                    "" + CHUNK_SIZE);
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
                if (cd.isConnectingToInternet()) {
                    try {

                        if (AppController.isSoundON().equals("ON")) {
                            MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.beep4);
                            mp.start();
                        } else {
                        }
//
                        Log.i("Replace Et", "" + URLEncoder.encode(etChatSend.getText().toString(), "UTF-8"));
                        Toast.makeText(getActivity(), "" + etChatSend.getText().toString(), Toast.LENGTH_LONG).show();
                        sendMessage("http://www.esolz.co.in/lab9/aiCafe/iosapp/sendSingleUser.php",
                                AppData.loginDataType.getId(),
                                getArguments().getString("USER_ID"),
                                URLEncoder.encode(etChatSend.getText().toString(), "UTF-8"),
                                "m",
                                "",
                                "O");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
                }
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

        pageIndicator = getArguments().getString("Page");

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

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
                        Log.i("CHAT RES SAKU", stringResponse);
                        Log.i("CHAT send", URL);
                        try {
                            JSONObject response = new JSONObject(stringResponse);
                            if (response.getString("status").equals("success")) {
                                JSONObject jInnerObj = response.getJSONObject("details");
                                chatViewDataType = new ChatViewDataType(
                                        jInnerObj.getString("chat_id"),
                                        jInnerObj.getString("send_from"),
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
                                singleChatAdapter.addFromReceiver(chatViewDataType);
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

//                Log.d("M send_id", sendID);
//                Log.d("M rec_id", recID);
                Log.i("DHOP", "" + URL + "?send_id=" + sendID + "&rec_id=" + recID + "&message=" + message + "&type=" + type + "&stickername=" + stikername + "&chat_type=" + chatType);

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

    private void sendStiker(final String URL, final String sendID, final String recID,
                            final String message, final String type, final String stikername, final String chatType) {

        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);
                        try {
                            JSONObject response = new JSONObject(stringResponse);
                            if (response.getString("status").equals("success")) {
                                // singleChatAdapter.notifyDataSetChanged();

                                getAllChatDetails("http://203.196.159.37//lab9/aiCafe/iosapp/chat_view.php",
                                        AppData.loginDataType.getId(),
                                        getArguments().getString("USER_ID"),
                                        "0",
                                        "150");

                                Toast.makeText(getActivity(), "Message send.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Message not send.", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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

                Log.d("St send_id", sendID);
                Log.d("St rec_id", recID);
                Log.d("St message", message);
                Log.d("St type", type);
                Log.d("St stickername", stikername);
                Log.d("St chat_type", chatType);

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

    public static void getMsgFromNotification(String chat_id, String send_from, String send_to, String message,
                                              String type, String stickername, String chat_time, String chat_date,
                                              String status, String file_link, String file_available, String name,
                                              String photo, String photo_thumb) {

    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.setIsAppRunning("YES");
        Toast.makeText(getActivity(), "Start" + AppController.isAppRunning(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        AppController.setIsAppRunning("YES");
        Toast.makeText(getActivity(), "Resume" + AppController.isAppRunning(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        AppController.setIsAppRunning("NO");
        Toast.makeText(getActivity(), "Pause" + AppController.isAppRunning(), Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onStop() {
//        // TODO Auto-generated method stub
//        super.onStop();
//        AppController.setIsAppRunning("NO");
//        Toast.makeText(getActivity(), "Stop" + AppController.isAppRunning(), Toast.LENGTH_SHORT).show();
//    }

    private void getAllChatDetails(final String URL, final String sendID, final String recID, final String start, final String records) {

        pbarSingleChat.setVisibility(View.VISIBLE);
        listSingleChat.setVisibility(View.GONE);

        Log.d("Single chat url", URL);

        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);

                        pbarSingleChat.setVisibility(View.GONE);
                        try {
                            JSONObject response = new JSONObject(stringResponse);
                            totalResponseValue = Integer.parseInt(response.getString("total"));
                            if (response.getString("status").equals("success")) {
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
                                        //Collections.reverse(chatViewDataTypeArrayList);
                                    }
                                    Collections.reverse(chatViewDataTypeArrayList);
                                    singleChatAdapter = new SingleChatAdapter(getActivity(),
                                            0, 0,
                                            chatViewDataTypeArrayList,
                                            totalResponseValue,
                                            AppData.loginDataType.getId(), toploader, listSingleChat);
                                    listSingleChat.setAdapter(singleChatAdapter);
                                }
                            } else {
                                txtError.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            Log.d("JSONException", e.toString());
                            Toast.makeText(getActivity(), "Server not responding...", Toast.LENGTH_SHORT).show();
                        }
                        listSingleChat.setVisibility(View.VISIBLE);
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
                params.put("rec_id", recID);
                params.put("start", start);
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

