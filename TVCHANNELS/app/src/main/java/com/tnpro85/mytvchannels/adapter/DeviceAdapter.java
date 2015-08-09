package com.tnpro85.mytvchannels.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tnpro85.mytvchannels.R;
import com.tnpro85.mytvchannels.models.Device;

import java.util.ArrayList;

/**
 * Created by TUAN on 14/06/2015.
 */
public class DeviceAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Device> data;

    public DeviceAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<Device> data) {
        this.data = new ArrayList<>(data);
    }

    @Override
    public int getCount() {
        if (data != null)
            return data.size();
        return 0;
    }

    @Override
    public Device getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.row_device, null);
            holder.tvDeviceName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvDeviceDesc = (TextView) convertView.findViewById(R.id.tvDesc);
            holder.tvDeviceIndex = (TextView) convertView.findViewById(R.id.tvIndex);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        Device item = getItem(position);
        holder.tvDeviceIndex.setText(position + 1 + "");

        if(item != null) {
            holder.tvDeviceName.setText(item.dName);
            holder.tvDeviceDesc.setText(item.dDesc);
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView tvDeviceName, tvDeviceDesc, tvDeviceIndex;
    }
}
