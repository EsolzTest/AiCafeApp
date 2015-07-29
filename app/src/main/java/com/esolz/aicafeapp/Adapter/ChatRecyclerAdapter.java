package com.esolz.aicafeapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import  android.widget.*;
import com.esolz.aicafeapp.Datatype.UserListingDataType;
import com.esolz.aicafeapp.Helper.CircleTransform;
import com.esolz.aicafeapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by su on 28/7/15.
 */
public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.CustomViewHolder> {
    private ArrayList<UserListingDataType> feedItemList;
    private Context mContext;

    public ChatRecyclerAdapter(Context context, ArrayList<UserListingDataType> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_room_headerdata, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        UserListingDataType feedItem = feedItemList.get(i);

        //Download image using picasso library
        Picasso.with(mContext).load("http://www.esolz.co.in/lab9/aiCafe/" + feedItem.getPhoto_thumb())
                .fit().centerCrop().transform(new CircleTransform())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(customViewHolder.imageView);


    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.imageView);

        }
    }
}

