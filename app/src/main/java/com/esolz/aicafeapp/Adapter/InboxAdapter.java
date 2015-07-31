package com.esolz.aicafeapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.esolz.aicafeapp.Customviews.OpenSansRegularTextView;
import com.esolz.aicafeapp.Customviews.OpenSansSemiboldTextView;
import com.esolz.aicafeapp.Datatype.FriendListDataType;
import com.esolz.aicafeapp.Fragment.FragmentSingleChat;
import com.esolz.aicafeapp.Fragment.FragmentUserInformation;
import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.Helper.CircleTransform;
import com.esolz.aicafeapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ltp on 16/07/15.
 */
public class InboxAdapter extends ArrayAdapter<FriendListDataType> {

    Context context;
    LayoutInflater inflator;
    ArrayList<FriendListDataType> friendListDataTypeArrayList, friendListDataTypeArrayListLazy;
    FriendListDataType friendListDataType;
    ViewHolder holder;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    int totalResponseValue;
    final int CHUNK_SIZE = 10;
    ProgressBar pbarFriend;
    ListView listFriend;

    public InboxAdapter(Context context, int resource, int textViewResourceId,
                        ArrayList<FriendListDataType> friendListDataTypeArrayList,
                        ProgressBar pbarFriend, ListView listFriend) {
        super(context, resource, textViewResourceId, friendListDataTypeArrayList);

        this.context = context;
        this.friendListDataTypeArrayList = friendListDataTypeArrayList;
        this.pbarFriend = pbarFriend;
        this.listFriend = listFriend;

        inflator = ((Activity) this.context).getLayoutInflater();

        fragmentManager = ((FragmentActivity) this.context).getSupportFragmentManager();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = inflator.inflate(R.layout.inbox_list_item, parent, false);
            holder = new ViewHolder();

            holder.mainContainer = (RelativeLayout) convertView.findViewById(R.id.main_container);

            holder.imgFriend = (ImageView) convertView.findViewById(R.id.img_friend);
            holder.imgOnline = (ImageView) convertView.findViewById(R.id.img_online);

            holder.txtFriendName = (OpenSansSemiboldTextView) convertView.findViewById(R.id.txt_friendname);
            holder.txtLastChat = (OpenSansRegularTextView) convertView.findViewById(R.id.txt_lastchat);

            holder.btnChat = (LinearLayout) convertView.findViewById(R.id.btn_inbox_chat);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtFriendName.setText(friendListDataTypeArrayList.get(position).getName());
        holder.txtLastChat.setText(friendListDataTypeArrayList.get(position).getLast_chat());

        Picasso.with(context).load("http://www.esolz.co.in/lab9/aiCafe/" + friendListDataTypeArrayList.get(position).getPhoto_thumb())
                .transform(new CircleTransform()).into(holder.imgFriend);

        if (friendListDataTypeArrayList.get(position).getOnline().equals("T")) {
            holder.imgOnline.setVisibility(View.VISIBLE);
        } else {
            holder.imgOnline.setVisibility(View.GONE);
        }

        holder.mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("USER_ID", friendListDataTypeArrayList.get(position).getId());
                bundle.putString("Page", "FragmentInbox");

                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentUserInformation fragmentUserInformation = new FragmentUserInformation();
                fragmentUserInformation.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, fragmentUserInformation);
                int count = fragmentManager.getBackStackEntryCount();
                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();

            }
        });

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("USER_ID", friendListDataTypeArrayList.get(position).getId());
                bundle.putString("Page", "FragmentInbox");
                bundle.putString("FriendImg", friendListDataTypeArrayList.get(position).getPhoto_thumb());

                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentSingleChat fragmentSingleChat = new FragmentSingleChat();
                fragmentSingleChat.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, fragmentSingleChat);
                int count = fragmentManager.getBackStackEntryCount();
                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();

            }
        });

        if (totalResponseValue > getCount()) {
            if (position == (getCount() - 1)) {
                Toast.makeText(context, "Lazy..", Toast.LENGTH_SHORT).show();
                makeJsonObjectRequest(
                        "http://www.esolz.co.in/lab9/aiCafe/iosapp/friend_list.php",
                        AppData.loginDataType.getId(),
                        "" + getCount());
            }
        }

        return convertView;
    }

    private void makeJsonObjectRequest(final String URL, final String ID, final String getStartPosition) {

        pbarFriend.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);
                        try {
                            JSONObject response = new JSONObject(stringResponse);
                            totalResponseValue = response.getInt("total");
                            if (response.getString("auth").equals("success")) {
                                JSONArray jsonArray = response.getJSONArray("details");
                                friendListDataTypeArrayListLazy = new ArrayList<FriendListDataType>();
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
                                    friendListDataTypeArrayListLazy.add(friendListDataType);
                                }

                                listFriend.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        addAll(friendListDataTypeArrayListLazy);
                                    }
                                });
                            } else {

                            }
                        } catch (JSONException e) {
                            Log.d("JSONException", e.toString());
                            Toast.makeText(context, "Server not responding...", Toast.LENGTH_SHORT).show();
                        }
                        pbarFriend.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Output : ", "Error: " + error.getMessage());
                Toast.makeText(context, "Server not responding...!", Toast.LENGTH_LONG).show();
                pbarFriend.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", ID);
                params.put("start", getStartPosition);
                params.put("records", "" + CHUNK_SIZE);
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

    public class ViewHolder {
        ImageView imgFriend, imgOnline;
        OpenSansSemiboldTextView txtFriendName;
        OpenSansRegularTextView txtLastChat;
        RelativeLayout mainContainer;
        LinearLayout btnChat;
    }

}

