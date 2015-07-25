package com.esolz.aicafeapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.esolz.aicafeapp.Customviews.OpenSansRegularTextView;
import com.esolz.aicafeapp.Customviews.OpenSansSemiboldTextView;
import com.esolz.aicafeapp.Datatype.FriendListDataType;
import com.esolz.aicafeapp.Fragment.FragmentSingleChat;
import com.esolz.aicafeapp.Fragment.FragmentUserInformation;
import com.esolz.aicafeapp.Helper.CircleTransform;
import com.esolz.aicafeapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ltp on 16/07/15.
 */
public class FriendAdapter extends ArrayAdapter<FriendListDataType> {

    Context context;
    LayoutInflater inflator;
    ArrayList<FriendListDataType> friendListDataTypeArrayList;
    ViewHolder holder;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public FriendAdapter(Context context, int resource, int textViewResourceId, ArrayList<FriendListDataType> friendListDataTypeArrayList) {
        super(context, resource, textViewResourceId, friendListDataTypeArrayList);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.friendListDataTypeArrayList = friendListDataTypeArrayList;
        inflator = ((Activity) this.context).getLayoutInflater();

        fragmentManager = ((FragmentActivity) this.context).getSupportFragmentManager();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = inflator.inflate(R.layout.friend_list_item, parent, false);
            holder = new ViewHolder();

            holder.mainContainer = (RelativeLayout) convertView.findViewById(R.id.rl_main_container);

            holder.imgFriend = (ImageView) convertView.findViewById(R.id.img_friend);
            holder.imgOnline = (ImageView) convertView.findViewById(R.id.img_online);

            holder.txtFriendName = (OpenSansSemiboldTextView) convertView.findViewById(R.id.txt_friendname);
            holder.txtBusiness = (OpenSansRegularTextView) convertView.findViewById(R.id.txt_friendbusi);

            holder.btnChat = (LinearLayout) convertView.findViewById(R.id.btn_friend_chat);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtFriendName.setText(friendListDataTypeArrayList.get(position).getName());
        holder.txtBusiness.setText(friendListDataTypeArrayList.get(position).getBusiness());

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
                bundle.putString("Page", "FragmentAiCafeFriends");

                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentUserInformation fragmentUserInformation = new FragmentUserInformation();
                fragmentUserInformation.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, fragmentUserInformation);
                fragmentTransaction.commit();

                Toast.makeText(context, friendListDataTypeArrayList.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("USER_ID", friendListDataTypeArrayList.get(position).getId());
                bundle.putString("Page", "FragmentAiCafeFriends");
                bundle.putString("FriendImg", friendListDataTypeArrayList.get(position).getPhoto_thumb());

                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentSingleChat fragmentSingleChat = new FragmentSingleChat();
                fragmentSingleChat.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, fragmentSingleChat);
                fragmentTransaction.commit();
            }
        });

        return convertView;
    }

    public class ViewHolder {
        ImageView imgFriend, imgOnline;
        OpenSansSemiboldTextView txtFriendName;
        OpenSansRegularTextView txtBusiness;
        RelativeLayout mainContainer;
        LinearLayout btnChat;
    }

}

