package com.esolz.aicafeapp.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.android.volley.toolbox.StringRequest;
import com.esolz.aicafeapp.Customviews.OpenSansRegularTextView;
import com.esolz.aicafeapp.Datatype.ChatViewDataType;
import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.Helper.CircleTransform;
import com.esolz.aicafeapp.Helper.ConnectionDetector;
import com.esolz.aicafeapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ltp on 22/07/15.
 */
public class SingleChatAdapter extends ArrayAdapter<ChatViewDataType> {

    ViewHolder holder;
    Context context;
    LayoutInflater inflator;
    ArrayList<ChatViewDataType> chatViewDataTypeArrayList, chatViewDataTypeArrayListLazy, chatViewDataTypeArrayListNoti;
    ChatViewDataType chatViewDataType;
    ConnectionDetector cd;
    int totalResponseValue;
    String idRec;

    int lazyCount = 0;

    public SingleChatAdapter(Context context, int resource, int textViewResourceId,
                             ArrayList<ChatViewDataType> chatViewDataTypeArrayList,
                             int totalResponseValue,
                             String idRec) {

        super(context, resource, textViewResourceId, chatViewDataTypeArrayList);

        this.context = context;
        this.chatViewDataTypeArrayList = chatViewDataTypeArrayList;
        this.totalResponseValue = totalResponseValue;
        this.idRec = idRec;

        inflator = ((Activity) this.context).getLayoutInflater();

        cd = new ConnectionDetector(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = inflator.inflate(R.layout.single_chat_listitem, parent, false);
            holder = new ViewHolder();

            //----------------------------------------Send
            holder.imgSender = (ImageView) convertView.findViewById(R.id.img_sender);
            holder.txtSenderName = (OpenSansRegularTextView) convertView.findViewById(R.id.txt_sender_name);
            holder.txtSendTime = (OpenSansRegularTextView) convertView.findViewById(R.id.txt_send_time);
            holder.txtSend = (OpenSansRegularTextView) convertView.findViewById(R.id.txt_send);
            holder.rlSenderMsg = (RelativeLayout) convertView.findViewById(R.id.rl_sender_msg);
            holder.llSenderChatContainer = (LinearLayout) convertView.findViewById(R.id.ll_sender_chat_container);

            // -- Sender Stiker
            holder.rlSenderStiker = (RelativeLayout) convertView.findViewById(R.id.rl_sender_stiker);
            holder.imgSenderSticker = (ImageView) convertView.findViewById(R.id.img_sender_sticker);
            holder.imgStickerSend = (ImageView) convertView.findViewById(R.id.img_sticker_send);

            //----------------------------------------Receive
            holder.imgReceiver = (ImageView) convertView.findViewById(R.id.img_receiver);
            holder.txtReceiverName = (OpenSansRegularTextView) convertView.findViewById(R.id.txt_receiver_name);
            holder.txtReceiverTime = (OpenSansRegularTextView) convertView.findViewById(R.id.txt_receive_time);
            holder.txtReceiver = (OpenSansRegularTextView) convertView.findViewById(R.id.txt_receive);
            holder.rlReceiverMsg = (RelativeLayout) convertView.findViewById(R.id.rl_receiver_msg);
            holder.llReceiverChatContainer = (LinearLayout) convertView.findViewById(R.id.ll_receiver_chat_container);

            // -- Receiver Stiker
            holder.rlReceiverStiker = (RelativeLayout) convertView.findViewById(R.id.rl_receiver_stiker);
            holder.imgReceiverSticker = (ImageView) convertView.findViewById(R.id.img_receiver_stiker);
            holder.imgStickerReceive = (ImageView) convertView.findViewById(R.id.img_sticker_receive);

            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar);
            holder.progressBar.setVisibility(View.GONE);
            holder.chatContainer = (LinearLayout) convertView.findViewById(R.id.chat_container);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //----------------------------------------Send
        holder.rlSenderMsg.setVisibility(View.GONE);
        // -- Sender Stiker
        holder.rlSenderStiker.setVisibility(View.GONE);

        //----------------------------------------Receive
        holder.rlReceiverMsg.setVisibility(View.GONE);
        // -- Receiver Stiker
        holder.rlReceiverStiker.setVisibility(View.GONE);

        if (chatViewDataTypeArrayList.get(position).getSend_from().equals(AppData.loginDataType.getId())) {
            if (chatViewDataTypeArrayList.get(position).getType().equals("s")) {
                holder.rlReceiverStiker.setVisibility(View.VISIBLE);
                holder.rlSenderStiker.setVisibility(View.GONE);

                Log.d("Stiker", chatViewDataTypeArrayList.get(position).getStickername().substring(0, 1) + "    " + getStikerImg(chatViewDataTypeArrayList.get(position).getStickername().substring(0, 1)));

                Picasso.with(context).load(getStikerImg(chatViewDataTypeArrayList.get(position).getStickername().substring(0, 1))).
                        centerCrop().fit().into(holder.imgReceiverSticker);

                Picasso.with(context).load("http://www.esolz.co.in/lab9/aiCafe/" + AppData.loginDataType.getPhoto_thumb())
                        .transform(new CircleTransform()).into(holder.imgStickerReceive);
                Picasso.with(context).load("http://www.esolz.co.in/lab9/aiCafe/" + chatViewDataTypeArrayList.get(position).getPhoto_thumb())
                        .transform(new CircleTransform()).into(holder.imgStickerSend);
            } else {
                holder.rlReceiverMsg.setVisibility(View.VISIBLE);
                holder.rlSenderMsg.setVisibility(View.GONE);

                holder.txtReceiverName.setText(chatViewDataTypeArrayList.get(position).getName());
                holder.txtReceiverTime.setText(chatViewDataTypeArrayList.get(position).getChat_date());
                holder.txtReceiver.setText(/*Html.fromHtml(*/chatViewDataTypeArrayList.get(position).getMessage()/*)*/);

                Picasso.with(context).load("http://www.esolz.co.in/lab9/aiCafe/" + AppData.loginDataType.getPhoto_thumb())
                        .transform(new CircleTransform()).into(holder.imgReceiver);
                Picasso.with(context).load("http://www.esolz.co.in/lab9/aiCafe/" + chatViewDataTypeArrayList.get(position).getPhoto_thumb())
                        .transform(new CircleTransform()).into(holder.imgSender);
            }
        } else {
            if (chatViewDataTypeArrayList.get(position).getType().equals("s")) {
                holder.rlReceiverStiker.setVisibility(View.GONE);
                holder.rlSenderStiker.setVisibility(View.VISIBLE);

                Log.d("Stiker", chatViewDataTypeArrayList.get(position).getStickername().substring(0, 1) + "    " + getStikerImg(chatViewDataTypeArrayList.get(position).getStickername().substring(0, 1)));

                Picasso.with(context).load(getStikerImg(chatViewDataTypeArrayList.get(position).getStickername().substring(0, 1))).
                        centerCrop().fit().into(holder.imgStickerSend);

                Picasso.with(context).load("http://www.esolz.co.in/lab9/aiCafe/" + chatViewDataTypeArrayList.get(position).getPhoto_thumb())
                        .transform(new CircleTransform()).into(holder.imgSenderSticker);
                Picasso.with(context).load("http://www.esolz.co.in/lab9/aiCafe/" + AppData.loginDataType.getPhoto_thumb())
                        .transform(new CircleTransform()).into(holder.imgStickerReceive);
            } else {
                holder.rlReceiverMsg.setVisibility(View.GONE);
                holder.rlSenderMsg.setVisibility(View.VISIBLE);

                holder.txtSenderName.setText(chatViewDataTypeArrayList.get(position).getName());
                holder.txtSendTime.setText(chatViewDataTypeArrayList.get(position).getChat_date());
                holder.txtSend.setText(/*Html.fromHtml(*/chatViewDataTypeArrayList.get(position).getMessage()/*)*/);

                Picasso.with(context).load("http://www.esolz.co.in/lab9/aiCafe/" + chatViewDataTypeArrayList.get(position).getPhoto_thumb())
                        .transform(new CircleTransform()).into(holder.imgSender);
                Picasso.with(context).load("http://www.esolz.co.in/lab9/aiCafe/" + AppData.loginDataType.getPhoto_thumb())
                        .transform(new CircleTransform()).into(holder.imgReceiver);
            }
        }

        if (position == 0) {

            lazyCount = (lazyCount + 1);
//            Toast.makeText(context, position + "   Size :  " + chatViewDataTypeArrayList.size(), Toast.LENGTH_SHORT).show();
//            if (chatViewDataTypeArrayList.size() < totalResponseValue) {
//                if (cd.isConnectingToInternet()) {
//                    getAllChatDetails("http://203.196.159.37//lab9/aiCafe/iosapp/android_chat_view.php",
//                            AppData.loginDataType.getId(),
//                            idRec,
//                            "" + chatViewDataTypeArrayList.size(),
//                            "5");
//                } else {
//                    Toast.makeText(context, "No internet connection.", Toast.LENGTH_SHORT).show();
//                }
//            }
        }

        return convertView;
    }

    private void getAllChatDetails(final String URL, final String sendID, final String recID, final String start, final String records) {

        Log.d("Single chat url", URL);

        holder.progressBar.setVisibility(View.GONE);
        holder.chatContainer.setVisibility(View.GONE);

        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);

                        holder.progressBar.setVisibility(View.GONE);
                        holder.chatContainer.setVisibility(View.GONE);
                        try {
                            JSONObject response = new JSONObject(stringResponse);
                            if (response.getString("status").equals("success")) {
                                JSONArray jsonArray = response.getJSONArray("details");
                                chatViewDataTypeArrayListLazy = new ArrayList<ChatViewDataType>();
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
                                        chatViewDataTypeArrayListLazy.add(chatViewDataType);
                                    }
                                    Collections.reverse(chatViewDataTypeArrayListLazy);
                                    chatViewDataTypeArrayList.addAll(0, chatViewDataTypeArrayListLazy);
                                    //Collections.reverse(chatViewDataTypeArrayList);
                                    notifyDataSetChanged();
                                }
                            } else {

                            }
                        } catch (JSONException e) {
                            Log.d("JSONException", e.toString());
                            Toast.makeText(context, "Server not responding...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Output : ", "Error: " + error.getMessage());
                Toast.makeText(context,
                        "Server not responding...!", Toast.LENGTH_LONG)
                        .show();
                holder.progressBar.setVisibility(View.GONE);
                holder.chatContainer.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("send_id", sendID);
                params.put("rec_id", recID);
                params.put("start", start);
                params.put("records", records);
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

    public int getStikerImg(String jsonStiker) {

        if (jsonStiker.equals("1")) {
            return R.drawable.aione;
        } else if (jsonStiker.equals("2")) {
            return R.drawable.aitwo;
        } else if (jsonStiker.equals("3")) {
            return R.drawable.aithree;
        } else if (jsonStiker.equals("4")) {
            return R.drawable.aifour;
        } else if (jsonStiker.equals("5")) {
            return R.drawable.aifive;
        } else if (jsonStiker.equals("6")) {
            return R.drawable.aisix;
        } else if (jsonStiker.equals("7")) {
            return R.drawable.aiseven;
        } else {
            return R.drawable.aieight;
        }

    }

    public class ViewHolder {

        //----------------------------------------Send
        ImageView imgSender;
        OpenSansRegularTextView txtSenderName, txtSendTime, txtSend;
        RelativeLayout rlSenderMsg;
        LinearLayout llSenderChatContainer;

        // -- Sender Stiker
        RelativeLayout rlSenderStiker;
        ImageView imgSenderSticker, imgStickerSend;

        //----------------------------------------Receive
        ImageView imgReceiver;
        OpenSansRegularTextView txtReceiverName, txtReceiverTime, txtReceiver;
        RelativeLayout rlReceiverMsg;
        LinearLayout llReceiverChatContainer;

        // -- Receiver Stiker
        RelativeLayout rlReceiverStiker;
        ImageView imgReceiverSticker, imgStickerReceive;

        ProgressBar progressBar;
        LinearLayout chatContainer;
    }

}
