package com.esolz.aicafeapp.Adapter;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.esolz.aicafeapp.Customviews.OpenSansRegularTextView;
import com.esolz.aicafeapp.Customviews.OpenSansSemiboldTextView;
import com.esolz.aicafeapp.Datatype.FriendRequestPendingDataType;
import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.Helper.CircleTransform;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ltp on 16/07/15.
 */
public class FriendRequestAdapter extends ArrayAdapter<FriendRequestPendingDataType> {

    Context context;
    ArrayList<FriendRequestPendingDataType> friendRequestPendingDataTypeArrayList;
    LayoutInflater inflator;
    ViewHolder holder;
    ProgressDialog progressDialog;
    String urlResponseAccept = "", exceptionAccept = "", urlResponseReject = "", exceptionReject = "";

    public FriendRequestAdapter(Context context, int resource, int textViewResourceId,
                                ArrayList<FriendRequestPendingDataType> friendRequestPendingDataTypeArrayList) {
        super(context, resource, textViewResourceId, friendRequestPendingDataTypeArrayList);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.friendRequestPendingDataTypeArrayList = friendRequestPendingDataTypeArrayList;
        inflator = ((Activity) this.context).getLayoutInflater();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait");
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = inflator.inflate(R.layout.friendrequest_list_item, parent, false);
            holder = new ViewHolder();

            holder.imgFriend = (ImageView) convertView.findViewById(R.id.img_friend);

            holder.txtFriendName = (OpenSansSemiboldTextView) convertView.findViewById(R.id.txt_friendname);
            holder.txtFriendBusiness = (OpenSansRegularTextView) convertView.findViewById(R.id.txt_friendbusi);

            holder.btnRequestAccept = (Button) convertView.findViewById(R.id.btn_requestaccept);

            holder.btnRequestReject = (LinearLayout) convertView.findViewById(R.id.btn_requestreject);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtFriendName.setText(friendRequestPendingDataTypeArrayList.get(position).getName());
        holder.txtFriendBusiness.setText(friendRequestPendingDataTypeArrayList.get(position).getBusiness());

        Picasso.with(context).load("http://www.esolz.co.in/lab9/aiCafe/" + friendRequestPendingDataTypeArrayList.get(position).getPhoto_thumb())
                .transform(new CircleTransform()).into(holder.imgFriend);

        holder.btnRequestAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, friendRequestPendingDataTypeArrayList.get(position).getId(), Toast.LENGTH_SHORT).show();
                makeFriendRequestAccept("http://www.esolz.co.in/lab9/aiCafe/iosapp/friend_request_accept.php",
                        friendRequestPendingDataTypeArrayList.get(position).getId(),
                        AppData.loginDataType.getId());
            }
        });
        holder.btnRequestReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, friendRequestPendingDataTypeArrayList.get(position).getId(), Toast.LENGTH_SHORT).show();
                makeFriendRequestReject("http://www.esolz.co.in/lab9/aiCafe/iosapp/friend_request_reject.php",
                        friendRequestPendingDataTypeArrayList.get(position).getId(),
                        AppData.loginDataType.getId());
            }
        });

        return convertView;
    }

    public class ViewHolder {
        ImageView imgFriend;
        OpenSansSemiboldTextView txtFriendName;
        OpenSansRegularTextView txtFriendBusiness;
        Button btnRequestAccept;
        LinearLayout btnRequestReject;
    }

    private void makeFriendRequestAccept(final String URL, final String sendID, final String recID) {

        progressDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);
                        progressDialog.dismiss();
                        if (stringResponse.equals("accept")) {
                            notifyDataSetChanged();
                        }
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

//        AsyncTask<Void, Void, Void> acceptFriendRequest = new AsyncTask<Void, Void, Void>() {
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
//                    urlResponseAccept = "";
//                    exceptionAccept = "";
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
//                    urlResponseAccept = sb.toString();
//
//
//                } catch (Exception e) {
//                    exceptionAccept = e.toString();
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
//                if (exceptionAccept.equals("")) {
//                    if (urlResponseAccept.equals("accept")) {
//                        notifyDataSetChanged();
//                    }
//                } else {
//                    Log.d("Exception : ", exceptionAccept);
//                    Toast.makeText(context, "Accept Server not responding....", Toast.LENGTH_LONG).show();
//                }
//            }
//
//        };
//        acceptFriendRequest.execute();

    }

    private void makeFriendRequestReject(final String URL, final String sendID, final String recID) {

        progressDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);
                        progressDialog.dismiss();
                        if (stringResponse.equals("accept")) {
                            notifyDataSetChanged();
                        }
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

//        AsyncTask<Void, Void, Void> rejectFriendRequest = new AsyncTask<Void, Void, Void>() {
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
//                    urlResponseAccept = "";
//                    exceptionAccept = "";
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
//                    urlResponseAccept = sb.toString();
//
//
//                } catch (Exception e) {
//                    exceptionAccept = e.toString();
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
//                if (exceptionAccept.equals("")) {
//                    if (urlResponseAccept.equals("accept")) {
//                        notifyDataSetChanged();
//                    }
//                } else {
//                    Log.d("Reject Exception : ", exceptionAccept);
//                    Toast.makeText(context, "Server not responding....", Toast.LENGTH_LONG).show();
//                }
//            }
//
//        };
//        rejectFriendRequest.execute();
    }

}

