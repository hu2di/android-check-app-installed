package com.blogspot.huyhungdinh.v_coin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HUNGDH on 1/6/2016.
 */
public class ListAppsAdapter extends BaseAdapter {
    private ArrayList<MyApps> listApps;
    private static LayoutInflater inflater = null;

    public ListAppsAdapter(Context context, ArrayList<MyApps> listApps) {
        this.listApps = listApps;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listApps.size();
    }

    @Override
    public Object getItem(int position) {
        return listApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyApps app = listApps.get(position);
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.lv_apps_item, null);
        }
        ImageView iv_app_icon = (ImageView) vi.findViewById(R.id.iv_app_icon);
        iv_app_icon.setImageResource(app.getImage());
        TextView tv_app_name = (TextView) vi.findViewById(R.id.tv_app_name);
        TextView tv_app_coin = (TextView) vi.findViewById(R.id.tv_app_coin);
        tv_app_name.setText(app.getName());
        if (app.isInstalled()) {
            if (app.isFinished()) {
                tv_app_coin.setText("Đã hoàn thành");
            } else {
                tv_app_coin.setText("Đã cài đặt");
            }
        } else {
            tv_app_coin.setText("+" + app.getCoin() + " C");
        }
        return vi;
    }
}
