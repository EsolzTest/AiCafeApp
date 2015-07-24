package com.esolz.aicafeapp.Fragment;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    OpenSansRegularTextView txtError;
    OpenSansSemiboldEditText etChatSend;

    ConnectionDetector cd;
    SingleChatAdapter singleChatAdapter;
    int totalResponseValue = 0;

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

//                    chatViewDataType.setType("s");
//                    chatViewDataType.setStickername(getStikerNo(position));
//                    singleChatAdapter.add(chatViewDataType);

                    sendStiker(
                            "http://www.esolz.co.in/lab9/aiCafe/iosapp/sendChatUser.php",
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
            getAllChatDetails("http://203.196.159.37//lab9/aiCafe/iosapp/android_chat_view.php",
                    AppData.loginDataType.getId(),
                    getArguments().getString("USER_ID"),
                    "0",
                    "50");
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
                        sendMessage("http://www.esolz.co.in/lab9/aiCafe/iosapp/sendChatUser.php",
                                AppData.loginDataType.getId(),
                                getArguments().getString("USER_ID"),
                                URLEncoder.encode(etChatSend.getText().toString().trim(), "UTF-8"),
                                "m",
                                "",
                                "O");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

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

        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);
                        try {
                            JSONObject response = new JSONObject(stringResponse);
                            if (response.getString("status").equals("success")) {

                                getAllChatDetails("http://203.196.159.37//lab9/aiCafe/iosapp/android_chat_view.php",
                                        AppData.loginDataType.getId(),
                                        getArguments().getString("USER_ID"),
                                        "0",
                                        "50");


                                etChatSend.setText("");

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

                                getAllChatDetails("http://203.196.159.37//lab9/aiCafe/iosapp/android_chat_view.php",
                                        AppData.loginDataType.getId(),
                                        getArguments().getString("USER_ID"),
                                        "0",
                                        "50");

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
                                    singleChatAdapter = new SingleChatAdapter(getActivity(),
                                            0, 0,
                                            chatViewDataTypeArrayList,
                                            totalResponseValue,
                                            getArguments().getString("USER_ID"));
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
                Toast.makeText(getActivity(),
                        "Server not responding...!", Toast.LENGTH_LONG)
                        .show();
                // pBAR.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("send_id", sendID);
                params.put("rec_id", recID);
                params.put("start", start);
                params.put("records", records);

                Log.d("send_id", sendID);
                Log.d("rec_id", recID);
                Log.d("start", start);
                Log.d("records", records);

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

        /*JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
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
                pbarSingleChat.setVisibility(View.GONE);
                listSingleChat.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);*/
    }
}

