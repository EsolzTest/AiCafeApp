package com.esolz.aicafeapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.esolz.aicafeapp.Customviews.OpenSansSemiboldTextView;
import com.esolz.aicafeapp.R;

import java.util.ArrayList;

/**
 * Created by ltp on 15/07/15.
 */
public class AdapterCustomSpinner extends ArrayAdapter<String> {

    ArrayList<String> data;
    LayoutInflater sInflater;
    String provinceNames;
    Context context;
    OpenSansSemiboldTextView txtvw;

    public AdapterCustomSpinner(Context context, int resource,
                                ArrayList<String> provinceItem2) {
        super(context, resource, provinceItem2);
        // TODO Auto-generated constructor stub

        this.data = provinceItem2;
        this.context = context;
        sInflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        provinceNames = data.get(position);

        // Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();

        if (convertView == null) {

            convertView = sInflater.inflate(R.layout.gender_drop_down, null);
            txtvw = (OpenSansSemiboldTextView) convertView
                    .findViewById(R.id.spinneritems);

        } else {

        }

        txtvw.setText(provinceNames);

        return convertView;

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return getView(position, convertView, parent);
    }

}
