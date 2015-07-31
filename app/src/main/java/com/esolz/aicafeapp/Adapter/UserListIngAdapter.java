package com.esolz.aicafeapp.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.esolz.aicafeapp.Datatype.FriendRequestPendingDataType;
import com.esolz.aicafeapp.Datatype.UserListingDataType;
import com.esolz.aicafeapp.Fragment.FragmentSingleChat;
import com.esolz.aicafeapp.Fragment.FragmentUserInformation;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ltp on 20/07/15.
 */
public class UserListIngAdapter extends ArrayAdapter<UserListingDataType> {

    Context context;
    ArrayList<UserListingDataType> userListingDataTypeArrayList;
    LayoutInflater inflator;
    ViewHolder holder;
    ProgressDialog progressDialog;
    String urlResponseAddFriend = "", exceptionAddFriend = "";
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ConnectionDetector cd;

    public UserListIngAdapter(Context context, int resource, int textViewResourceId,
                              ArrayList<UserListingDataType> userListingDataTypeArrayList) {
        super(context, resource, textViewResourceId, userListingDataTypeArrayList);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.userListingDataTypeArrayList = userListingDataTypeArrayList;
        inflator = ((Activity) this.context).getLayoutInflater();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");

        cd = new ConnectionDetector(context);

        fragmentManager = ((FragmentActivity) this.context).getSupportFragmentManager();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = inflator.inflate(R.layout.userlisting_list_item, parent, false);
            holder = new ViewHolder();

            holder.mainContainer = (RelativeLayout) convertView.findViewById(R.id.main_container);

            holder.imgFriend = (ImageView) convertView.findViewById(R.id.img_friend);

            holder.txtFriendName = (OpenSansSemiboldTextView) convertView.findViewById(R.id.txt_friendname);
            holder.txtFriendBusiness = (OpenSansRegularTextView) convertView.findViewById(R.id.txt_friendbusi);

            holder.btnChat = (Button) convertView.findViewById(R.id.btn_chat);

            holder.btnAddFriend = (Button) convertView.findViewById(R.id.btn_add_friend);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtFriendName.setText(userListingDataTypeArrayList.get(position).getName());
        holder.txtFriendBusiness.setText(userListingDataTypeArrayList.get(position).getBusiness());

        Picasso.with(context).load("http://www.esolz.co.in/lab9/aiCafe/" + userListingDataTypeArrayList.get(position).getPhoto_thumb())
                .transform(new CircleTransform()).into(holder.imgFriend);

        if (userListingDataTypeArrayList.get(position).getFriend().equals("T")) {
            holder.btnAddFriend.setVisibility(View.GONE);
        } else {
            holder.btnAddFriend.setVisibility(View.VISIBLE);
        }

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("USER_ID", userListingDataTypeArrayList.get(position).getId());
                bundle.putString("Page", "FragmentAllFriend");

                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentSingleChat fragmentSingleChat = new FragmentSingleChat();
                fragmentSingleChat.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, fragmentSingleChat);
                int count = fragmentManager.getBackStackEntryCount();
                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();
            }
        });
        holder.btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    makeFriendRequestSend("http://www.esolz.co.in/lab9/aiCafe/iosapp/add_friend.php",
                            AppData.loginDataType.getId(),
                            userListingDataTypeArrayList.get(position).getId(),
                            position);
                } else {
                    Toast.makeText(context, "No internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("USER_ID", userListingDataTypeArrayList.get(position).getId());
                bundle.putString("Page", "FragmentAllFriend");
                bundle.putString("FriendImg", userListingDataTypeArrayList.get(position).getPhoto_thumb());

                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentUserInformation fragmentUserInformation = new FragmentUserInformation();
                fragmentUserInformation.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, fragmentUserInformation);
                int count = fragmentManager.getBackStackEntryCount();
                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();

            }
        });

        return convertView;
    }

    public class ViewHolder {
        ImageView imgFriend;
        OpenSansSemiboldTextView txtFriendName;
        OpenSansRegularTextView txtFriendBusiness;
        Button btnChat, btnAddFriend;
        RelativeLayout mainContainer;
    }

    private void makeFriendRequestSend(final String URL, final String sendID, final String recID, final int position) {
        progressDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        progressDialog.dismiss();
                        userListingDataTypeArrayList.get(position).setFriend("T");
                        notifyDataSetChanged();
                        Toast.makeText(context, "Send friend request successfully.", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Output : ", "Error: " + error.getMessage());
                Toast.makeText(context,
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
//                    userListingDataTypeArrayList.get(position).setFriend("T");
//                    notifyDataSetChanged();
//                    Toast.makeText(context, "Send friend request successfully.", Toast.LENGTH_LONG).show();
//                } else {
//                    Log.d("Exception : ", exceptionAddFriend);
//                    Toast.makeText(context, "Server not responding....", Toast.LENGTH_LONG).show();
//                }
//            }
//
//        };
//        sendFriendRequest.execute();

    }
}