package com.tnpro85.mytvchannels.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tnpro85.mytvchannels.R;
import com.tnpro85.mytvchannels.models.Device;
import com.tnpro85.mytvchannels.uicontrols.DeviceIcon;

import java.util.ArrayList;

public class DevicePickerAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private ArrayList<Device> data;
    public DevicePickerAdapter(Context context, ArrayList<Device> data) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = new ArrayList<>(data);
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Device getItem(int position) {
        return data != null && position < data.size() ? data.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DeviceHolder dHolder;
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.row_device, parent, false);
            dHolder = new DeviceHolder();
            dHolder.vPopupMenu = convertView.findViewById(R.id.vPopupMenu);
            dHolder.vPopupMenu.setVisibility(View.GONE);
            dHolder.tvDeviceName = (TextView) convertView.findViewById(R.id.tvName);
            dHolder.tvDeviceDesc = (TextView) convertView.findViewById(R.id.tvDesc);
            dHolder.vIndex = (DeviceIcon) convertView.findViewById(R.id.vIndex);
            convertView.setTag(dHolder);
        }
        else
            dHolder = (DeviceHolder) convertView.getTag();

        Device device = getItem(position);
        if(device != null) {
            dHolder.tvDeviceName.setText(device.dName);
            dHolder.tvDeviceDesc.setText(device.dDesc);
        }

        return convertView;
    }

    public class DeviceHolder {
        public View vPopupMenu;
        public TextView tvDeviceName, tvDeviceDesc;
        public DeviceIcon vIndex;
    }
}
