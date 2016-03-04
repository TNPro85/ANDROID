package com.tnpro85.mytvchannels.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tnpro85.mytvchannels.R;
import com.tnpro85.mytvchannels.models.Device;

import java.util.ArrayList;

/**
 * Created by TUAN on 14/06/2015.
 */
public class DeviceAdapter extends BaseAdapter implements Filterable {

    private LayoutInflater mInflater;
    private ArrayList<Device> originalData;
    private ArrayList<Device> filteredData;
    private DeviceFilter deviceFilter;

    public DeviceAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<Device> data) {
        this.originalData = new ArrayList<>(data);
        this.filteredData = new ArrayList<>(data);
    }

    @Override
    public int getCount() {
        if (filteredData != null)
            return filteredData.size();
        return 0;
    }

    @Override
    public Device getItem(int position) {
        if (filteredData != null && position >= 0 && position < filteredData.size())
            return filteredData.get(position);
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
        String pos = position + 1 + "";
        holder.tvDeviceIndex.setText(pos);

        if(item != null) {
            holder.tvDeviceName.setText(item.dName);
            holder.tvDeviceDesc.setText(item.dDesc);
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView tvDeviceName, tvDeviceDesc, tvDeviceIndex;
    }

    @Override
    public Filter getFilter() {
        if(deviceFilter == null)
            deviceFilter = new DeviceFilter();

        return deviceFilter;
    }

    public class DeviceFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = originalData;
                results.count = originalData.size();
            }
            else {
                // We perform filtering operation
                ArrayList<Device> data = new ArrayList<>();

                for (Device device : originalData) {
                    if (device.dName.toLowerCase().contains(constraint.toString().toLowerCase()))
                        data.add(device);
                }

                results.values = data;
                results.count = data.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.count == 0)
                notifyDataSetInvalidated();
            else {
                filteredData = (ArrayList<Device>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
