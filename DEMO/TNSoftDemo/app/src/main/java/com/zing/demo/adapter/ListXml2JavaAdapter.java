package com.zing.demo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zing.demo.R;
import com.zing.demo.components.ListItem;
import com.zing.demo.uicontrol.ListItemLayout;

import java.util.ArrayList;

/**
 * Created by CPU10819-local on 26/03/2015.
 */
public class ListXml2JavaAdapter extends BaseAdapter {

    ArrayList<ListItem> data;
    Context context;

    public ListXml2JavaAdapter(Context context) {
        this.context = context;
        data = new ArrayList<ListItem>();
    }

    public void setData(ArrayList<ListItem> data) {
        this.data = new ArrayList<>(data);
    }

    @Override
    public int getCount() {
        if(data != null)
            return data.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder {
        public static ListItemLayout lil;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            long cur = System.currentTimeMillis();
            convertView = holder.lil = (ListItemLayout)inflater.inflate(R.layout.lv_xml2java_item, parent, false);
            Log.i("TIME:", System.currentTimeMillis() - cur + "");
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder)convertView.getTag();

        return convertView;
    }
}
