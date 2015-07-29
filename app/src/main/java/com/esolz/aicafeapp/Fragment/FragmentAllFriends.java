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
import android.widget.ListView;
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
import com.esolz.aicafeapp.Adapter.FriendAdapter;
import com.esolz.aicafeapp.Adapter.InboxAdapter;
import com.esolz.aicafeapp.Adapter.UserListIngAdapter;
import com.esolz.aicafeapp.Customviews.OpenSansRegularTextView;
import com.esolz.aicafeapp.Customviews.OpenSansSemiboldTextView;
import com.esolz.aicafeapp.Datatype.FriendListDataType;
import com.esolz.aicafeapp.Datatype.UserListingDataType;
import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.Helper.ConnectionDetector;
import com.esolz.aicafeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Esolz Pvt. Ltd. on 20/07/15.
 */
public class FragmentAllFriends extends Fragment {

    View view;
    LinearLayout llPipeContainer, slidingNow, llBack;
    RelativeLayout rlMSGContainer;
    OpenSansSemiboldTextView txtPageTitle;
    OpenSansRegularTextView txtMSGCounter, txtError;
    ImageView imgBack, imgMSG;
    DrawerLayout drawerLayout;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    ListView listInbox;
    ProgressBar pbarInbox;

    ArrayList<UserListingDataType> userListingDataTypeArrayList;
    UserListingDataType userListingDataType;

    ConnectionDetector cd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_inbox, container, false);

        cd = new ConnectionDetector(getActivity());

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
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        listInbox = (ListView) view.findViewById(R.id.list_inbox);
        pbarInbox = (ProgressBar) view.findViewById(R.id.pbar_inbox);
        txtError = (OpenSansRegularTextView) view.findViewById(R.id.txt_error);
        txtError.setVisibility(View.GONE);

        if (cd.isConnectingToInternet()) {
            makeJsonObjectRequest("http://203.196.159.37/lab9/aiCafe/iosapp/user_listing.php", AppData.loginDataType.getId());
        } else {
            Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
        }

        llPipeContainer.setVisibility(View.GONE);
        slidingNow.setVisibility(View.GONE);
        rlMSGContainer.setVisibility(View.GONE);
        txtPageTitle.setVisibility(View.VISIBLE);
        imgBack.setVisibility(View.VISIBLE);
        llBack.setVisibility(View.VISIBLE);
        imgMSG.setVisibility(View.GONE);
        txtMSGCounter.setVisibility(View.GONE);

        txtPageTitle.setText("All Users");

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentStoreNow fragmentStoreNow = new FragmentStoreNow();
                fragmentTransaction.replace(R.id.fragment_container, fragmentStoreNow);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void makeJsonObjectRequest(final String URL, final String ID) {

        pbarInbox.setVisibility(View.VISIBLE);

        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);
                        try {
                            JSONObject response = new JSONObject(stringResponse);
                            JSONArray jsonArray = response.getJSONArray("details");
                            userListingDataTypeArrayList = new ArrayList<UserListingDataType>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                userListingDataType = new UserListingDataType(
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
                            }
                            UserListIngAdapter userListIngAdapter = new UserListIngAdapter(getActivity(), 0, 0, userListingDataTypeArrayList);
                            listInbox.setAdapter(userListIngAdapter);
                        } catch (JSONException e) {
                            Log.d("JSONException", e.toString());
                            Toast.makeText(getActivity(), "Server not responding...", Toast.LENGTH_SHORT).show();
                        }
                        pbarInbox.setVisibility(View.GONE);
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
//                    JSONArray jsonArray = response.getJSONArray("details");
//                    userListingDataTypeArrayList = new ArrayList<UserListingDataType>();
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        userListingDataType = new UserListingDataType(
//                                jsonObject.getString("id"),
//                                jsonObject.getString("name"),
//                                jsonObject.getString("sex"),
//                                jsonObject.getString("email"),
//                                "",
//                                jsonObject.getString("about"),
//                                jsonObject.getString("business"),
//                                jsonObject.getString("dob"),
//                                jsonObject.getString("photo"),
//                                jsonObject.getString("photo_thumb"),
//                                jsonObject.getString("registerDate"),
//                                jsonObject.getString("facebookid"),
//                                jsonObject.getString("last_sync"),
//                                jsonObject.getString("fb_pic_url"),
//                                "" + jsonObject.getInt("age"),
//                                jsonObject.getString("friend")
//                        );
//                        userListingDataTypeArrayList.add(userListingDataType);
//                        UserListIngAdapter userListIngAdapter = new UserListIngAdapter(getActivity(), 0, 0, userListingDataTypeArrayList);
//                        listInbox.setAdapter(userListIngAdapter);
//                    }
//                } catch (JSONException e) {
//                    Log.d("JSONException", e.toString());
//                    Toast.makeText(getActivity(), "Server not responding...", Toast.LENGTH_SHORT).show();
//                }
//                pbarInbox.setVisibility(View.GONE);
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



