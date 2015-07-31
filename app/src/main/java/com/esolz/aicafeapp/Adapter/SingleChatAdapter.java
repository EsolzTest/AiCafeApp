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

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Esolz Pvt. Ltd. on 22/07/15.
 */
public class SingleChatAdapter extends ArrayAdapter<ChatViewDataType> {

    ViewHolder holder;
    Context context;
    LayoutInflater inflator;
    ArrayList<ChatViewDataType> chatViewDataTypeArrayList, chatViewDataTypeArrayListLazy, chatViewDataTypeArrayListNoti;
    ChatViewDataType chatViewDataType;
    ListView lView;
    ConnectionDetector cd;
    int totalResponseValue;
    final int CHUNK_SIZE = 20;
    boolean isDataRetrving = false;
    String idRec;
    ProgressBar toploader;

    int lazyCount = 0;

    public SingleChatAdapter(Context context, int resource, int textViewResourceId,
                             ArrayList<ChatViewDataType> chatViewDataTypeArrayList,
                             int totalResponseValue,
                             String idRec, ProgressBar toploader, ListView lView) {

        super(context, resource, textViewResourceId, chatViewDataTypeArrayList);

        this.context = context;
        this.chatViewDataTypeArrayList = chatViewDataTypeArrayList;
        this.totalResponseValue = totalResponseValue;
        this.idRec = idRec;
        this.lView = lView;
        this.toploader = toploader;
        inflator = ((Activity) this.context).getLayoutInflater();

        cd = new ConnectionDetector(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = inflator.inflate(R.layout.single_chat_listitem, parent, false);
            holder = new ViewHolder();
            //============New Addition
            holder.meChatBucket = (RelativeLayout) convertView.findViewById(R.id.rl_receiver_msg);
            holder.meStikerBucket = (RelativeLayout) convertView.findViewById(R.id.rl_receiver_stiker);
            holder.youChatBucket = (RelativeLayout) convertView.findViewById(R.id.rl_sender_msg);
            holder.youStikerBucket = (RelativeLayout) convertView.findViewById(R.id.rl_sender_stiker);
            //....................
            holder.youStiker = (ImageView) convertView.findViewById(R.id.img_sticker_send);
            holder.meStiker = (ImageView) convertView.findViewById(R.id.img_receiver_stiker);
            //...................
            holder.chatMeProfileImage = (ImageView) convertView.findViewById(R.id.img_receiver);
            holder.stickerMeProfileImage = (ImageView) convertView.findViewById(R.id.img_sticker_receive);
            holder.chatYouProfileImage = (ImageView) convertView.findViewById(R.id.img_sender);
            holder.stikerYouProfileImage = (ImageView) convertView.findViewById(R.id.img_sender_sticker);
            //....................
            holder.youChatHeaderLeft = (OpenSansRegularTextView) convertView.findViewById(R.id.txt_sender_name);
            holder.youChatHeaderRight = (OpenSansRegularTextView) convertView.findViewById(R.id.txt_send_time);
            holder.youChatContent = (OpenSansRegularTextView) convertView.findViewById(R.id.txt_send);
            holder.meChatHeaderLeft = (OpenSansRegularTextView) convertView.findViewById(R.id.txt_receiver_name);
            holder.meChatHeaderRight = (OpenSansRegularTextView) convertView.findViewById(R.id.txt_receive_time);
            holder.meChatContent = (OpenSansRegularTextView) convertView.findViewById(R.id.txt_receive);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        ChatViewDataType dtaFunal = getItem(position);

        if (dtaFunal.getSend_from().equals(AppData.loginDataType.getId())) {

            if (dtaFunal.getType().equals("s")) {
                holder.meChatBucket.setVisibility(View.GONE);
                holder.meStikerBucket.setVisibility(View.VISIBLE);
                holder.youChatBucket.setVisibility(View.GONE);
                holder.youStikerBucket.setVisibility(View.GONE);

                // Log.d("Stiker", chatViewDataTypeArrayList.get(position).getStickername().substring(0, 1) + "    " + getStikerImg(chatViewDataTypeArrayList.get(position).getStickername().substring(0, 1)));

                Picasso.with(context).load(getStikerImg(dtaFunal.getStickername().substring(0, 1))).
                        fit().into(holder.meStiker);

                Picasso.with(context).load("http://www.esolz.co.in/lab9/aiCafe/" + AppData.loginDataType.getPhoto_thumb()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder)
                        .fit().centerCrop().transform(new CircleTransform()).into(holder.stickerMeProfileImage);

            } else {
                holder.meChatBucket.setVisibility(View.VISIBLE);
                holder.meStikerBucket.setVisibility(View.GONE);
                holder.youChatBucket.setVisibility(View.GONE);
                holder.youStikerBucket.setVisibility(View.GONE);

                // Log.d("Stiker", chatViewDataTypeArrayList.get(position).getStickername().substring(0, 1) + "    " + getStikerImg(chatViewDataTypeArrayList.get(position).getStickername().substring(0, 1)));

//                Picasso.with(context).load(getStikerImg(chatViewDataTypeArrayList.get(position).getStickername().substring(0, 1))).
//                        fit().into(holder.meStiker);

                Picasso.with(context).load("http://www.esolz.co.in/lab9/aiCafe/" + AppData.loginDataType.getPhoto_thumb()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder)
                        .fit().centerCrop().transform(new CircleTransform()).into(holder.chatMeProfileImage);


//                holder.rlReceiverMsg.setVisibility(View.VISIBLE);
//                holder.rlSenderMsg.setVisibility(View.GONE);

                holder.meChatHeaderLeft.setText(dtaFunal.getName());
                holder.meChatHeaderRight.setText(dtaFunal.getChat_date());
                try {
                    holder.meChatContent.setText(/*Html.fromHtml(*/URLDecoder.decode(dtaFunal.getMessage(), "UTF-8")/*)*/);

                } catch (Exception e) {

                }

//                Picasso.with(context).load("http://www.esolz.co.in/lab9/aiCafe/" + AppData.loginDataType.getPhoto_thumb()).fit().centerCrop()
//                        .transform(new CircleTransform()).into(holder.imgReceiver);
//                Picasso.with(context).load("http://www.esolz.co.in/lab9/aiCafe/" + chatViewDataTypeArrayList.get(position).getPhoto_thumb())
//                        .fit().centerCrop().transform(new CircleTransform()).into(holder.imgSender);
            }
        } else {
            if (dtaFunal.getType().equals("s")) {
                holder.meChatBucket.setVisibility(View.GONE);
                holder.meStikerBucket.setVisibility(View.GONE);
                holder.youChatBucket.setVisibility(View.GONE);
                holder.youStikerBucket.setVisibility(View.VISIBLE);

                // Log.d("Stiker", chatViewDataTypeArrayList.get(position).getStickername().substring(0, 1) + "    " + getStikerImg(chatViewDataTypeArrayList.get(position).getStickername().substring(0, 1)));

                Picasso.with(context).load(getStikerImg(dtaFunal.getStickername().substring(0, 1))).
                        fit().into(holder.youStiker);

                Picasso.with(context).load("http://www.esolz.co.in/lab9/aiCafe/" + dtaFunal.getPhoto_thumb()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder)
                        .fit().centerCrop().transform(new CircleTransform()).into(holder.stikerYouProfileImage);

            } else {
                holder.meChatBucket.setVisibility(View.GONE);
                holder.meStikerBucket.setVisibility(View.GONE);
                holder.youChatBucket.setVisibility(View.VISIBLE);
                holder.youStikerBucket.setVisibility(View.GONE);

                // Log.d("Stiker", chatViewDataTypeArrayList.get(position).getStickername().substring(0, 1) + "    " + getStikerImg(chatViewDataTypeArrayList.get(position).getStickername().substring(0, 1)));

//                Picasso.with(context).load(getStikerImg(chatViewDataTypeArrayList.get(position).getStickername().substring(0, 1))).
//                        fit().into(holder.meStiker);

                Picasso.with(context).load("http://www.esolz.co.in/lab9/aiCafe/" + dtaFunal.getPhoto_thumb()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder)
                        .fit().centerCrop().transform(new CircleTransform()).into(holder.chatYouProfileImage);


//                holder.rlReceiverMsg.setVisibility(View.VISIBLE);
//                holder.rlSenderMsg.setVisibility(View.GONE);

                holder.youChatHeaderLeft.setText(dtaFunal.getName());
                holder.youChatHeaderRight.setText(dtaFunal.getChat_date());
                //holder.youChatContent.setText(/*Html.fromHtml(*/dtaFunal.getMessage()/*)*/);
                try {
                    holder.youChatContent.setText(/*Html.fromHtml(*/URLDecoder.decode(dtaFunal.getMessage(), "UTF-8")/*)*/);

                } catch (Exception e) {

                }
            }
        }

        if (totalResponseValue > getCount()) {
            if (position == (getCount() - 1)) {
                if (isDataRetrving == false) {
                    //======Lazy loading start.....
                    getAllChatDetails(AppData.loginDataType.getId(), idRec,
                            "" + getCount(), "");
                    //   Toast.makeText(context, "here..", Toast.LENGTH_SHORT).show();
                    //Log.i("CHAT", "Fire here..");
                }
            }
        }


        return convertView;
    }

    //============LazyLoading
    private void getAllChatDetails(final String sendID, final String recID, final String start, final String records) {
        toploader.setVisibility(View.VISIBLE);
        final String URL = "http://203.196.159.37//lab9/aiCafe/iosapp/chat_view.php";
        Log.d("Single chat url", URL);
        isDataRetrving = true;
        // holder.progressBar.setVisibility(View.GONE);
        //holder.chatContainer.setVisibility(View.GONE);

        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.i("CHAT ", stringResponse);

                        // holder.progressBar.setVisibility(View.GONE);
                        // holder.chatContainer.setVisibility(View.GONE);
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
//                                        lView.post(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                lView.smoothScrollToPosition(getPosition(chatViewDataTypeArrayListLazy.get(0)));
//                                            }
//                                        });
//                                        chatViewDataTypeArrayList.add(0, chatViewDataType);
                                        // insert(chatViewDataType, 0);
                                        //add(chatViewDataType);
                                    }
//                                    Collections.reverse(chatViewDataTypeArrayListLazy);
//                                    chatViewDataTypeArrayList.addAll(0, chatViewDataTypeArrayListLazy);
//                                    //Collections.reverse(chatViewDataTypeArrayList);
//
                                    lView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            addAll(chatViewDataTypeArrayListLazy);
                                        }
                                    });
//                                    notifyDataSetChanged();
                                    isDataRetrving = false;
                                }
                            } else {

                            }
                            toploader.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            Log.i("CHAT", e.toString());
                            Toast.makeText(context, "Server not responding...Retry!", Toast.LENGTH_SHORT).show();
                            toploader.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Output : ", "Error: " + error.getMessage());
                Toast.makeText(context,
                        "Server not responding...!", Toast.LENGTH_LONG)
                        .show();
                //holder.progressBar.setVisibility(View.GONE);
                //holder.chatContainer.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("send_id", sendID);
                params.put("rec_id", recID);
                params.put("start", start);
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
//        ImageView imgSender;
//        OpenSansRegularTextView txtSenderName, txtSendTime, txtSend;
//        RelativeLayout rlSenderMsg;
//        LinearLayout llSenderChatContainer;
//
//        // -- Sender Stiker
//        RelativeLayout rlSenderStiker;
//        ImageView imgSenderSticker, imgStickerSend;
//
//        //----------------------------------------Receive
//        ImageView imgReceiver;
//        OpenSansRegularTextView txtReceiverName, txtReceiverTime, txtReceiver;
//        RelativeLayout rlReceiverMsg;
//        LinearLayout llReceiverChatContainer;
//
//        // -- Receiver Stiker
//        RelativeLayout rlReceiverStiker;
//        ImageView imgReceiverSticker, imgStickerReceive;
//
//        //ProgressBar progressBar;
//        LinearLayout chatContainer;

        //==========new design

        RelativeLayout meStikerBucket, youStikerBucket, meChatBucket, youChatBucket;
        ImageView chatMeProfileImage, stickerMeProfileImage, chatYouProfileImage, stikerYouProfileImage, meStiker, youStiker;
        OpenSansRegularTextView youChatHeaderLeft, youChatHeaderRight, youChatContent, meChatHeaderLeft, meChatHeaderRight, meChatContent;
    }


    public void addFromReceiver(ChatViewDataType data) {
        //add(data);
        insert(data, 0);
        // Toast.makeText(context, "Add in adapter...", Toast.LENGTH_SHORT).show();

    }

}
