package com.tnpro85.cathay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tnpro85.cathay.models.LocationItem;

import java.util.ArrayList;

/**
 * Created by TUAN on 14/06/2015.
 */
public class LocationAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<LocationItem> data;

    public LocationAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<LocationItem> data) {
        this.data = new ArrayList<>(data);
    }

    @Override
    public int getCount() {
        if (data != null)
            return data.size();
        return 0;
    }

    @Override
    public LocationItem getItem(int position) {
        if (data != null && position >= 0 && position < data.size())
            return data.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_location_row, null);
            holder.tvLocationTitle = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvLocationAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            holder.tvLocationIndex = (TextView) convertView.findViewById(R.id.tvIndex);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        LocationItem item = getItem(position);
        holder.tvLocationIndex.setText(position + 1 + "");

        if(item != null) {
            holder.tvLocationTitle.setText(item.mTitle);
            holder.tvLocationAddress.setText(item.mDesc);
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView tvLocationTitle, tvLocationAddress, tvLocationIndex;
    }
}
