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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.esolz.aicafeapp.Adapter.InboxAdapter;
import com.esolz.aicafeapp.Customviews.OpenSansRegularTextView;
import com.esolz.aicafeapp.Customviews.OpenSansSemiboldTextView;
import com.esolz.aicafeapp.Datatype.FriendListDataType;
import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.Helper.CircleTransform;
import com.esolz.aicafeapp.Helper.ConnectionDetector;
import com.esolz.aicafeapp.Helper.Trns;
import com.esolz.aicafeapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    OpenSansRegularTextView txtAge, txtGender, txtStatus, txtFriendCount, txtBusinessPro;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    ArrayList<FriendListDataType> friendListDataTypeArrayList;
    FriendListDataType friendListDataType;

    ConnectionDetector cd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_profile, container, false);

        cd = new ConnectionDetector(getActivity());

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
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        profileDrawer = (LinearLayout) getActivity().findViewById(R.id.profile_drawer);

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
        txtBusinessPro = (OpenSansRegularTextView) view.findViewById(R.id.txt_business_pro);

        txtFriendCount = (OpenSansRegularTextView) view.findViewById(R.id.txt_friendcount);

        if (cd.isConnectingToInternet()) {
            Picasso.with(getActivity()).load("http://www.esolz.co.in/lab9/aiCafe/" + AppData.loginDataType.getPhoto_thumb())
                    .transform(new CircleTransform()).into(imgProfile);
            txtProfileName.setText(AppData.loginDataType.getName());
            txtAge.setText("Age : " + AppData.loginDataType.getAge());
            if (AppData.loginDataType.getSex().equals("M")) {
                txtGender.setText("Male");
            } else {
                txtGender.setText("Female");
            }
            txtStatus.setText(AppData.loginDataType.getBusiness());
            //txtBusinessPro.setText(AppData.loginDataType.getBusiness());
            makeJsonObjectRequest("http://www.esolz.co.in/lab9/aiCafe/iosapp/friend_list.php", AppData.loginDataType.getId());
        } else {
            Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
        }

        llLoyaltyPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fragmentTransaction = fragmentManager.beginTransaction();
//                FragmentLoyaltyPoints fragmentLoyaltyPoints = new FragmentLoyaltyPoints();
//                fragmentTransaction.replace(R.id.fragment_container, fragmentLoyaltyPoints);
//                fragmentTransaction.commit();
            }
        });

        llCoupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fragmentTransaction = fragmentManager.beginTransaction();
//                FragmentCoupons fragmentCoupons = new FragmentCoupons();
//                fragmentTransaction.replace(R.id.fragment_container, fragmentCoupons);
//                fragmentTransaction.commit();
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

    private void makeJsonObjectRequest(final String URL, final String ID) {

        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);
                        try {
                            JSONObject response = new JSONObject(stringResponse);
                            if (response.getString("auth").equals("success")) {
                                JSONArray jsonArray = response.getJSONArray("details");
                                friendListDataTypeArrayList = new ArrayList<FriendListDataType>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    friendListDataType = new FriendListDataType(
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
                                            jsonObject.getString("online"),
                                            jsonObject.getString("last_chat")
                                    );
                                    friendListDataTypeArrayList.add(friendListDataType);
                                    if (friendListDataTypeArrayList.size() == 0 || friendListDataTypeArrayList.size() == 1) {
                                        txtFriendCount.setText("" + friendListDataTypeArrayList.size() + " friend");
                                    } else {
                                        txtFriendCount.setText("" + friendListDataTypeArrayList.size() + " friends");
                                    }
                                }
                            } else {
                                txtFriendCount.setText("0 friend");
                            }
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
//                try {
//                    if (response.getString("auth").equals("success")) {
//                        JSONArray jsonArray = response.getJSONArray("details");
//                        friendListDataTypeArrayList = new ArrayList<FriendListDataType>();
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            friendListDataType = new FriendListDataType(
//                                    jsonObject.getString("id"),
//                                    jsonObject.getString("name"),
//                                    jsonObject.getString("sex"),
//                                    jsonObject.getString("email"),
//                                    "",
//                                    jsonObject.getString("about"),
//                                    jsonObject.getString("business"),
//                                    jsonObject.getString("dob"),
//                                    jsonObject.getString("photo"),
//                                    jsonObject.getString("photo_thumb"),
//                                    jsonObject.getString("registerDate"),
//                                    jsonObject.getString("facebookid"),
//                                    jsonObject.getString("last_sync"),
//                                    jsonObject.getString("fb_pic_url"),
//                                    "" + jsonObject.getInt("age"),
//                                    jsonObject.getString("online"),
//                                    jsonObject.getString("last_chat")
//                            );
//                            friendListDataTypeArrayList.add(friendListDataType);
//                            if (friendListDataTypeArrayList.size() == 0 || friendListDataTypeArrayList.size() == 1) {
//                                txtFriendCount.setText("" + friendListDataTypeArrayList.size() + " friend");
//                            } else {
//                                txtFriendCount.setText("" + friendListDataTypeArrayList.size() + " friends");
//                            }
//                        }
//                    } else {
//                        txtFriendCount.setText("0 friend");
//                    }
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
