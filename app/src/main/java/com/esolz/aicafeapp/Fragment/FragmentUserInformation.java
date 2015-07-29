package com.esolz.aicafeapp.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.esolz.aicafeapp.Adapter.InboxAdapter;
import com.esolz.aicafeapp.Adapter.UserListIngAdapter;
import com.esolz.aicafeapp.Customviews.OpenSansRegularTextView;
import com.esolz.aicafeapp.Customviews.OpenSansSemiboldEditText;
import com.esolz.aicafeapp.Customviews.OpenSansSemiboldTextView;
import com.esolz.aicafeapp.Datatype.FriendListDataType;
import com.esolz.aicafeapp.Datatype.UserInformationDataType;
import com.esolz.aicafeapp.Datatype.UserListingDataType;
import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.Helper.CircleTransform;
import com.esolz.aicafeapp.Helper.ConnectionDetector;
import com.esolz.aicafeapp.R;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ltp on 20/07/15.
 */
public class FragmentUserInformation extends Fragment {

    View view;
    LinearLayout llPipeContainer, slidingNow, llBack;
    RelativeLayout rlMSGContainer;
    OpenSansSemiboldTextView txtPageTitle, txtreport, txtcancel, txtblock, txtunfriend;
    OpenSansRegularTextView txtMSGCounter;
    ImageView imgBack, imgMSG, addimg;
    DrawerLayout drawerLayout;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    ConnectionDetector cd;
    String frndstatus;
    ImageView imgProfile;
    OpenSansSemiboldTextView txtProfileName;
    OpenSansRegularTextView txtBusinessPro, txtAge, txtGender, txtStatus, txtProfileAbout;
    RelativeLayout rlChat, rlAddFriend, rlReport, mainLayout;
    ProgressBar prgUserInformation;

    UserInformationDataType userInformationDataType;
    ProgressDialog progressDialog;
    String urlResponseAddFriend = "", exceptionAddFriend = "", urlResponseReport = "", exceptionReport = "";

    Dialog dialogReport, dialogmore;
    OpenSansSemiboldEditText etReport;
    ProgressBar reportProBar;
    RelativeLayout rlReportSend;
    ImageView imgCancel;

    String imgSender = "", pageIndicator = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_user_information, container, false);

        cd = new ConnectionDetector(getActivity());

        fragmentManager = getFragmentManager();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");

        llPipeContainer = (LinearLayout) getActivity().findViewById(R.id.ll_pipe_container);
        slidingNow = (LinearLayout) getActivity().findViewById(R.id.slidingnow);
        rlMSGContainer = (RelativeLayout) getActivity().findViewById(R.id.rl_msgcontainer);
        txtPageTitle = (OpenSansSemiboldTextView) getActivity().findViewById(R.id.txt_page_title);
        imgBack = (ImageView) getActivity().findViewById(R.id.img_back);
        llBack = (LinearLayout) getActivity().findViewById(R.id.ll_back);
        txtMSGCounter = (OpenSansRegularTextView) getActivity().findViewById(R.id.txt_msg_counter);
        imgMSG = (ImageView) getActivity().findViewById(R.id.img_msg);

        addimg = (ImageView) getActivity().findViewById(R.id.addimg);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        imgProfile = (ImageView) view.findViewById(R.id.img_profile);
        txtProfileName = (OpenSansSemiboldTextView) view.findViewById(R.id.txt_profile_name);
        txtBusinessPro = (OpenSansRegularTextView) view.findViewById(R.id.txt_business_pro);
        txtAge = (OpenSansRegularTextView) view.findViewById(R.id.txt_age);
        txtGender = (OpenSansRegularTextView) view.findViewById(R.id.txt_gender);
        txtStatus = (OpenSansRegularTextView) view.findViewById(R.id.txt_status);
        txtProfileAbout = (OpenSansRegularTextView) view.findViewById(R.id.txt_profile_about);
        rlChat = (RelativeLayout) view.findViewById(R.id.rl_chat);
        rlAddFriend = (RelativeLayout) view.findViewById(R.id.rl_add_friend);
        rlReport = (RelativeLayout) view.findViewById(R.id.rl_report);
        mainLayout = (RelativeLayout) view.findViewById(R.id.main_layout);
        prgUserInformation = (ProgressBar) view.findViewById(R.id.prg_user_information);
        prgUserInformation.setVisibility(View.GONE);


        dialogReport = new Dialog(getActivity(), R.style.DialogSlideAnim);
        dialogReport.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogReport.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogReport.getWindow().setGravity(Gravity.BOTTOM);
        dialogReport.setContentView(R.layout.dialog_report_send);
        dialogReport.setCanceledOnTouchOutside(true);

        etReport = (OpenSansSemiboldEditText) dialogReport.findViewById(R.id.et_report);
        reportProBar = (ProgressBar) dialogReport.findViewById(R.id.pbar_report);
        reportProBar.setVisibility(View.GONE);
        rlReportSend = (RelativeLayout) dialogReport.findViewById(R.id.rl_report_send);
        imgCancel = (ImageView) dialogReport.findViewById(R.id.img_cancel);

        rlReportSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialogReport.dismiss();
                if (etReport.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Please enter some text.", Toast.LENGTH_SHORT).show();
                } else {
                    if (cd.isConnectingToInternet()) {
                        try {
                            reportProBar.setVisibility(View.VISIBLE);
//                            sendReport("http://www.esolz.co.in/lab9/aiCafe/iosapp/report.php?send_id=" + AppData.loginDataType.getId()
//                                    + "&rec_id=" + getArguments().getString("USER_ID")
//                                    + "&report_reason=" + URLEncoder.encode(etReport.getText().toString().trim(), "UTF-8"));
                            sendReport(
                                    "http://www.esolz.co.in/lab9/aiCafe/iosapp/report.php",
                                    AppData.loginDataType.getId(),
                                    getArguments().getString("USER_ID"),
                                    URLEncoder.encode(etReport.getText().toString().trim(), "UTF-8"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialogmore = new Dialog(getActivity(), R.style.DialogSlideAnim);
        dialogmore.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogmore.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogmore.getWindow().setGravity(Gravity.BOTTOM);
        dialogmore.setContentView(R.layout.dialog_more);
        dialogmore.setCanceledOnTouchOutside(true);
        txtreport = (OpenSansSemiboldTextView) dialogmore.findViewById(R.id.txtreport);
        txtcancel = (OpenSansSemiboldTextView) dialogmore.findViewById(R.id.txtcancel);
        txtblock = (OpenSansSemiboldTextView) dialogmore.findViewById(R.id.txtblock);
        txtunfriend = (OpenSansSemiboldTextView) dialogmore.findViewById(R.id.txtunfriend);

        txtunfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unfriendMethod();
            }
        });
        txtblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Blockuser();
            }
        });
        txtcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogmore.dismiss();
            }
        });
        txtreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogReport.show();
                dialogmore.dismiss();
            }
        });


        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogReport.dismiss();
            }
        });

        if (cd.isConnectingToInternet()) {
            makeJsonObjectRequest("http://www.esolz.co.in/lab9/aiCafe/iosapp/user_detail.php", getArguments().getString("USER_ID"));
        } else {
            Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
        }

        pageIndicator = getArguments().getString("Page");

        rlChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("USER_ID", getArguments().getString("USER_ID"));
                bundle.putString("Page", "FragmentUserInformation");
                // bundle.putString("Page", pageIndicator);
                bundle.putString("FriendImg", imgSender);

                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentSingleChat fragmentSingleChat = new FragmentSingleChat();
                fragmentSingleChat.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, fragmentSingleChat);
                fragmentTransaction.commit();
            }
        });
        rlAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    makeFriendRequestSend("http://www.esolz.co.in/lab9/aiCafe/iosapp/add_friend.php", AppData.loginDataType.getId(), getArguments().getString("USER_ID"));
                } else {
                    Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rlReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogmore.show();
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

        txtPageTitle.setText("Details");


        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (getArguments().getString("Page").equals("Chatroom")) {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentChatRoom fragmentInbox = new FragmentChatRoom();
                        fragmentTransaction.replace(R.id.fragment_container, fragmentInbox);
                        fragmentTransaction.commit();
                    }
//                } else if (getArguments().getString("Page").equals("FragmentAiCafeFriends")) {
//                    fragmentTransaction = fragmentManager.beginTransaction();
//                    FragmentAiCafeFriends fragmentAiCafeFriends = new FragmentAiCafeFriends();
//                    fragmentTransaction.replace(R.id.fragment_container, fragmentAiCafeFriends);
//                    fragmentTransaction.commit();
//                } else {
//                    fragmentTransaction = fragmentManager.beginTransaction();
//                    FragmentAllFriends fragmentAllFriends = new FragmentAllFriends();
//                    fragmentTransaction.replace(R.id.fragment_container, fragmentAllFriends);
//                    fragmentTransaction.commit();
                }
//                else {
                catch (Exception e) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentProfile fragmentProfile = new FragmentProfile();
                    fragmentTransaction.replace(R.id.fragment_container, fragmentProfile);
                    fragmentTransaction.commit();
                }
//                }
            }
        });

        return view;
    }

    private void unfriendMethod() {
        final String sendID = AppData.loginDataType.getId();
        final String RecvID = getArguments().getString("USER_ID");
        final String Url = "http://www.esolz.co.in/lab9/aiCafe/iosapp/friend_request_reject.php";

        StringRequest sr = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Toast.makeText(getActivity(), stringResponse, Toast.LENGTH_LONG).show();
                        dialogmore.dismiss();
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
                params.put("rec_id", RecvID);
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

    private void Blockuser() {
        String userid = getArguments().getString("USER_ID");

        String urlJsonObj = "http://www.esolz.co.in/lab9/aiCafe/iosapp/block_user.php?id=" + AppData.loginDataType.getId() +
                "&block_id=" + userid;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("dsd", response.toString());

                try {
                    String msg = response.getString("status");
                    Log.d("USer Block", msg);
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    dialogmore.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("dssd", "Error: " + error.getMessage());


            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


    private void sendReport(final String URL, final String sendID, final String recID, final String reportReason) {

        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);
                        dialogReport.dismiss();
                        etReport.setText("");
                        reportProBar.setVisibility(View.GONE);
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
                params.put("report_reason", reportReason);
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

//        AsyncTask<Void, Void, Void> reportSend = new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected void onPreExecute() {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                // TODO Auto-generated method stub
//                try {
//                    urlResponseReport = "";
//                    exceptionReport = "";
//                    DefaultHttpClient httpclient = new DefaultHttpClient();
//                    HttpGet httpget = new HttpGet(URL);
//                    HttpResponse response;
//                    response = httpclient.execute(httpget);
//                    HttpEntity httpentity = response.getEntity();
//                    InputStream is = httpentity.getContent();
//                    BufferedReader reader = new BufferedReader(
//                            new InputStreamReader(is, "iso-8859-1"), 8);
//                    StringBuilder sb = new StringBuilder();
//                    String line = null;
//                    while ((line = reader.readLine()) != null) {
//                        sb.append(line + "\n");
//                    }
//                    is.close();
//                    urlResponseReport = sb.toString();
//
//
//                } catch (Exception e) {
//                    exceptionReport = e.toString();
//                }
//
//                Log.d("URL", URL);
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void result) {
//                // TODO Auto-generated method stub
//                super.onPostExecute(result);
//                reportProBar.setVisibility(View.GONE);
//                if (exceptionReport.equals("")) {
//                    dialogReport.dismiss();
//                    Toast.makeText(getActivity(), "Report send.", Toast.LENGTH_LONG).show();
//                    etReport.setText("");
//                } else {
//                    Log.d("Exception Add Friend : ", exceptionReport);
//                    Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
//                }
//            }
//
//        };
//        reportSend.execute();

    }

    private void makeFriendRequestSend(final String URL, final String sendID, final String recID) {

        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Toast.makeText(getActivity(), "Send friend request successfully.", Toast.LENGTH_LONG).show();
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

//        AsyncTask<Void, Void, Void> sendFriendRequest = new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected void onPreExecute() {
//                // TODO Auto-generated method stub
//                super.onPreExecute();
//                progressDialog.show();
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                // TODO Auto-generated method stub
//                try {
//                    urlResponseAddFriend = "";
//                    exceptionAddFriend = "";
//                    DefaultHttpClient httpclient = new DefaultHttpClient();
//                    HttpGet httpget = new HttpGet(URL);
//                    HttpResponse response;
//                    response = httpclient.execute(httpget);
//                    HttpEntity httpentity = response.getEntity();
//                    InputStream is = httpentity.getContent();
//                    BufferedReader reader = new BufferedReader(
//                            new InputStreamReader(is, "iso-8859-1"), 8);
//                    StringBuilder sb = new StringBuilder();
//                    String line = null;
//                    while ((line = reader.readLine()) != null) {
//                        sb.append(line + "\n");
//                    }
//                    is.close();
//                    urlResponseAddFriend = sb.toString();
//
//
//                } catch (Exception e) {
//                    exceptionAddFriend = e.toString();
//                }
//
//                Log.d("URL", URL);
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void result) {
//                // TODO Auto-generated method stub
//                super.onPostExecute(result);
//                progressDialog.dismiss();
//                if (exceptionAddFriend.equals("")) {
//                    Toast.makeText(getActivity(), urlResponseAddFriend, Toast.LENGTH_LONG).show();
//                } else {
//                    Log.d("Exception Add Friend : ", exceptionAddFriend);
//                    Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
//                }
//            }
//
//        };
//        sendFriendRequest.execute();

    }

    private void makeJsonObjectRequest(final String URL, final String ID) {

        prgUserInformation.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);

        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);
                        try {
                            JSONObject response = new JSONObject(stringResponse);
                            JSONObject jsonObject = response.getJSONObject("detail");

                            Picasso.with(getActivity()).load("http://www.esolz.co.in/lab9/aiCafe/" + jsonObject.getString("photo_thumb"))
                                    .transform(new CircleTransform()).into(imgProfile);
                            txtProfileName.setText(jsonObject.getString("name"));
                            txtAge.setText("Age : " + jsonObject.getInt("age"));
                            if (jsonObject.getString("sex").equals("M")) {
                                txtGender.setText("Male");
                            } else {
                                txtGender.setText("Female");
                            }
                            txtStatus.setText(jsonObject.getString("business"));
                            txtProfileAbout.setText(jsonObject.getString("about"));

                            frndstatus = response.getString("friend").trim();
                            Toast.makeText(getActivity(), frndstatus, Toast.LENGTH_SHORT).show();
                            imgSender = jsonObject.getString("photo_thumb");

                            prgUserInformation.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            Log.d("JSONException", e.toString());
                            Toast.makeText(getActivity(), "Server not responding...", Toast.LENGTH_SHORT).show();
                        }
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

//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                URL, null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("TAG", response.toString());
//
//                try {
//                    JSONObject jsonObject = response.getJSONObject("detail");
//                    Picasso.with(getActivity()).load("http://www.esolz.co.in/lab9/aiCafe/" + jsonObject.getString("photo_thumb"))
//                            .transform(new CircleTransform()).into(imgProfile);
//                    txtProfileName.setText(jsonObject.getString("name"));
//                    txtAge.setText("Age : " + jsonObject.getInt("age"));
//                    if (jsonObject.getString("sex").equals("M")) {
//                        txtGender.setText("Male");
//                    } else {
//                        txtGender.setText("Female");
//                    }
//                    txtStatus.setText(jsonObject.getString("business"));
//                    txtProfileAbout.setText(jsonObject.getString("about"));
//
//
//                    prgUserInformation.setVisibility(View.GONE);
//                    mainLayout.setVisibility(View.VISIBLE);
//                } catch (JSONException e) {
//                    Log.d("JSONException", e.toString());
//                    Toast.makeText(getActivity(), "Server not responding...", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("TAG", "Error: " + error.getMessage());
//                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
