package com.esolz.aicafeapp.Fragment;

import android.content.SharedPreferences;
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
import com.android.volley.toolbox.StringRequest;
import com.esolz.aicafeapp.Adapter.BlockListAdapter;
import com.esolz.aicafeapp.Customviews.OpenSansRegularTextView;
import com.esolz.aicafeapp.Customviews.OpenSansSemiboldTextView;
import com.esolz.aicafeapp.Datatype.BlockListDataType;
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
 * Created by ltp on 31/07/15.
 */
public class FragmentBlockListSettings extends Fragment {

    View view;
    LinearLayout llPipeContainer, slidingNow, llBack;
    RelativeLayout rlMSGContainer;
    OpenSansSemiboldTextView txtPageTitle;
    OpenSansRegularTextView txtMSGCounter;
    ImageView imgBack, imgMSG;
    DrawerLayout drawerLayout;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    ConnectionDetector cd;

    OpenSansRegularTextView txtError;
    ProgressBar pbarBlockList;
    ListView listBlocklist;

    ArrayList<BlockListDataType> blockListDataTypeArrayList;
    BlockListDataType blockListDataType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_blocklistsettings, container, false);

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

        pbarBlockList = (ProgressBar) view.findViewById(R.id.pbar_blocklist);
        listBlocklist = (ListView) view.findViewById(R.id.list_blocklist);
        txtError = (OpenSansRegularTextView) view.findViewById(R.id.txt_error);
        pbarBlockList.setVisibility(View.GONE);
        listBlocklist.setVisibility(View.GONE);
        txtError.setVisibility(View.GONE);

        if (cd.isConnectingToInternet()) {
            getSettingsInformation(
                    "http://www.esolz.co.in/lab9/aiCafe/iosapp/show_status.php", AppData.loginDataType.getId()
            );
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

        txtPageTitle.setText("Block List Setting");

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentSettings fragmentSettings = new FragmentSettings();
                fragmentTransaction.replace(R.id.fragment_container, fragmentSettings);
                int count = fragmentManager.getBackStackEntryCount();
                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();

            }
        });

        return view;
    }

    public void getSettingsInformation(final String URL, final String ID) {

        pbarBlockList.setVisibility(View.VISIBLE);
        listBlocklist.setVisibility(View.GONE);
        txtError.setVisibility(View.GONE);

        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);
                        pbarBlockList.setVisibility(View.GONE);
                        listBlocklist.setVisibility(View.VISIBLE);
                        try {
                            JSONObject response = new JSONObject(stringResponse);
                            JSONArray jsonArray = response.getJSONArray("block_list");
                            blockListDataTypeArrayList = new ArrayList<BlockListDataType>();
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectIn = jsonArray.getJSONObject(i);
                                    blockListDataType = new BlockListDataType(
                                            jsonObjectIn.getString("id"),
                                            jsonObjectIn.getString("name"),
                                            jsonObjectIn.getString("sex"),
                                            jsonObjectIn.getString("email"),
                                            "",
                                            jsonObjectIn.getString("about"),
                                            jsonObjectIn.getString("business"),
                                            jsonObjectIn.getString("dob"),
                                            jsonObjectIn.getString("photo"),
                                            jsonObjectIn.getString("photo_thumb"),
                                            jsonObjectIn.getString("registerDate"),
                                            jsonObjectIn.getString("facebookid"),
                                            jsonObjectIn.getString("last_sync"),
                                            jsonObjectIn.getString("fb_pic_url"),
                                            "" + jsonObjectIn.getInt("age")
                                    );
                                    blockListDataTypeArrayList.add(blockListDataType);
                                }
                                BlockListAdapter blockListAdapter = new BlockListAdapter(
                                        getActivity(), 0, 0,
                                        blockListDataTypeArrayList
                                );
                                listBlocklist.setAdapter(blockListAdapter);
                            } else {
                                pbarBlockList.setVisibility(View.GONE);
                                listBlocklist.setVisibility(View.GONE);
                                txtError.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            Log.d("JSONException", e.toString());
                            Toast.makeText(getActivity(), "Server not responding...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbarBlockList.setVisibility(View.GONE);
                listBlocklist.setVisibility(View.GONE);
                txtError.setVisibility(View.VISIBLE);
                VolleyLog.d("Output : ", "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Server not responding...!", Toast.LENGTH_LONG).show();
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
}

