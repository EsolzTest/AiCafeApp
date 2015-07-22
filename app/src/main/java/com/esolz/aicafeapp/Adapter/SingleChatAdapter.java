package com.esolz.aicafeapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.esolz.aicafeapp.Customviews.OpenSansRegularTextView;
import com.esolz.aicafeapp.Datatype.ChatViewDataType;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ltp on 22/07/15.
 */
public class SingleChatAdapter extends ArrayAdapter<ChatViewDataType> {

    ViewHolder holder;
    Context context;
    LayoutInflater inflator;
    ArrayList<ChatViewDataType> chatViewDataTypeArrayList;

    public SingleChatAdapter(Context context, int resource, int textViewResourceId,
                             ArrayList<ChatViewDataType> chatViewDataTypeArrayList) {

        super(context, resource, textViewResourceId, chatViewDataTypeArrayList);

        this.context = context;
        this.chatViewDataTypeArrayList = chatViewDataTypeArrayList;
        inflator = ((Activity) this.context).getLayoutInflater();
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

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //----------------------------------------Send
//        holder.imgSender.setVisibility(View.GONE);
//        holder.txtSenderName.setVisibility(View.GONE);
//        holder.txtSendTime.setVisibility(View.GONE);
//        holder.txtSend.setVisibility(View.GONE);
//        holder.llSenderChatContainer.setVisibility(View.GONE);
        holder.rlSenderMsg.setVisibility(View.GONE);

        // -- Sender Stiker
        holder.rlSenderStiker.setVisibility(View.GONE);
//        holder.imgSenderSticker.setVisibility(View.GONE);
//        holder.imgStickerSend.setVisibility(View.GONE);

        //----------------------------------------Receive
//        holder.imgReceiver.setVisibility(View.GONE);
//        holder.txtReceiverName.setVisibility(View.GONE);
//        holder.txtReceiverTime.setVisibility(View.GONE);
//        holder.txtReceiver.setVisibility(View.GONE);
//        holder.llReceiverChatContainer.setVisibility(View.GONE);
        holder.rlReceiverMsg.setVisibility(View.GONE);

        // -- Receiver Stiker
        holder.rlReceiverStiker.setVisibility(View.GONE);
//        holder.imgReceiverSticker.setVisibility(View.GONE);
//        holder.imgStickerReceive.setVisibility(View.GONE);

        if (chatViewDataTypeArrayList.get(position).getSend_to().equals(AppData.loginDataType.getId())) {
            holder.rlReceiverMsg.setVisibility(View.VISIBLE);
            holder.rlSenderMsg.setVisibility(View.GONE);
        } else {
            holder.rlReceiverMsg.setVisibility(View.GONE);
            holder.rlSenderMsg.setVisibility(View.VISIBLE);
        }

        return convertView;
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
    }

}
