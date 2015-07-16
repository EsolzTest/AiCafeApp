package com.esolz.aicafeapp.Adapter;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.esolz.aicafeapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ltp on 16/07/15.
 */
public class InboxAdapter extends ArrayAdapter<HashMap<String, String>> {

    Context context;
    ArrayList<HashMap<String, String>> data;
    LayoutInflater inflator;
    RelativeLayout listitemContainer;

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    public InboxAdapter(Context context, int resource, int textViewResourceId,
                        ArrayList<HashMap<String, String>> objects) {
        super(context, resource, textViewResourceId, objects);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = objects;
        inflator = ((Activity) this.context).getLayoutInflater();
        fragmentManager = ((FragmentActivity) this.context)
                .getSupportFragmentManager();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = inflator.inflate(R.layout.inbox_list_item, parent, false);
        }

        return convertView;
    }

}

