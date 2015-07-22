package com.esolz.aicafeapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.esolz.aicafeapp.R;

import java.util.List;

/**
 * Created by ltp on 22/07/15.
 */
public class StikersGridAdapter extends BaseAdapter {
    private Context mContext;
    List<Integer> stikersHolder;

    public StikersGridAdapter(Context context, List<Integer> stikersHolder) {
        this.mContext = context;
        this.stikersHolder = stikersHolder;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return stikersHolder.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.stikers_grid_item, null);
            ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
            imageView.setImageResource(stikersHolder.get(position));
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}