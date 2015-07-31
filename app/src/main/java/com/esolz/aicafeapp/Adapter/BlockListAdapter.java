package com.esolz.aicafeapp.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.esolz.aicafeapp.Customviews.OpenSansRegularTextView;
import com.esolz.aicafeapp.Customviews.OpenSansSemiboldTextView;
import com.esolz.aicafeapp.Datatype.BlockListDataType;
import com.esolz.aicafeapp.Datatype.FriendRequestPendingDataType;
import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.Helper.CircleTransform;
import com.esolz.aicafeapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ltp on 31/07/15.
 */
public class BlockListAdapter extends ArrayAdapter<BlockListDataType> {

    Context context;
    ArrayList<BlockListDataType> blockListDataTypeArrayList;
    LayoutInflater inflator;
    ViewHolder holder;
    ProgressDialog progressDialog;
    String urlResponseAccept = "", exceptionAccept = "", urlResponseReject = "", exceptionReject = "";

    public BlockListAdapter(Context context, int resource, int textViewResourceId,
                            ArrayList<BlockListDataType> blockListDataTypeArrayList) {
        super(context, resource, textViewResourceId, blockListDataTypeArrayList);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.blockListDataTypeArrayList = blockListDataTypeArrayList;
        inflator = ((Activity) this.context).getLayoutInflater();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = inflator.inflate(R.layout.blocklistsetting_listitem, parent, false);
            holder = new ViewHolder();

            holder.imgFriend = (ImageView) convertView.findViewById(R.id.img_friend);

            holder.txtFriendName = (OpenSansSemiboldTextView) convertView.findViewById(R.id.txt_friendname);
            holder.txtFriendBusiness = (OpenSansRegularTextView) convertView.findViewById(R.id.txt_friendbusi);

            holder.btnUnblock = (LinearLayout) convertView.findViewById(R.id.btn_requestreject);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtFriendName.setText(blockListDataTypeArrayList.get(position).getName());
        holder.txtFriendBusiness.setText(blockListDataTypeArrayList.get(position).getBusiness());

        Picasso.with(context).load("http://www.esolz.co.in/lab9/aiCafe/" + blockListDataTypeArrayList.get(position).getPhoto_thumb())
                .transform(new CircleTransform()).into(holder.imgFriend);

        holder.btnUnblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnblock(
                        "http://www.esolz.co.in/lab9/aiCafe/iosapp/unblock_user.php",
                        AppData.loginDataType.getId(),
                        blockListDataTypeArrayList.get(position).getIdBlock(),
                        position
                );
            }
        });

        return convertView;
    }

    public class ViewHolder {
        ImageView imgFriend;
        OpenSansSemiboldTextView txtFriendName;
        OpenSansRegularTextView txtFriendBusiness;
        LinearLayout btnUnblock;
    }

    public void setUnblock(final String URL, final String ID, final String unBlock, final int position) {

        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);
                        progressDialog.dismiss();
                        try {
                            if (stringResponse.equals("success")) {
                                remove(blockListDataTypeArrayList.get(position));
                                notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            Log.d("JSONException", e.toString());
                            Toast.makeText(context, "Server not responding...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Output : ", "Error: " + error.getMessage());
                Toast.makeText(context, "Server not responding...!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", ID);
                params.put("block_id", unBlock);
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
