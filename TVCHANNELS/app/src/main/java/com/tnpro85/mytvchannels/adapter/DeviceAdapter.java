package com.tnpro85.mytvchannels.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.PopupMenu;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tnpro.core.listeners.OnMenuClickListener;
import com.tnpro.core.uicontrols.PopupManager;
import com.tnpro85.mytvchannels.R;
import com.tnpro85.mytvchannels.models.Device;
import com.tnpro85.mytvchannels.uicontrols.DeviceIcon;

import java.util.ArrayList;

public class DeviceAdapter extends BaseAdapter implements Filterable {

    private LayoutInflater mInflater;
    private ArrayList<Device> originalData;
    private ArrayList<Device> filteredData;
    private DeviceFilter deviceFilter;
    private SparseBooleanArray mSelectedItemsIds;
    private int mSelectedBgColor;
    private OnMenuClickListener onMenuClickListener;

    public DeviceAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSelectedBgColor = context.getResources().getColor(R.color.list_selected_bg);
        mSelectedItemsIds = new SparseBooleanArray();
    }

    public void setData(ArrayList<Device> data) {
        this.originalData = new ArrayList<>(data);
        this.filteredData = new ArrayList<>(data);
    }

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }

    public ArrayList<Device> getData() {
        return this.originalData;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public void remove(Device device) {
        this.originalData.remove(device);
        this.filteredData.remove(device);
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
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
            holder.vContainer = convertView.findViewById(R.id.container);
            holder.vPopupMenu = convertView.findViewById(R.id.vPopupMenu);
            holder.tvDeviceName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvDeviceDesc = (TextView) convertView.findViewById(R.id.tvDesc);
            holder.vIndex = (DeviceIcon) convertView.findViewById(R.id.vIndex);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        final Device selectedDevice = getItem(position);
        String pos = position + 1 + "";
        holder.vIndex.setText(pos);

        if(selectedDevice != null) {
            holder.tvDeviceName.setText(selectedDevice.dName);
            holder.tvDeviceDesc.setText(selectedDevice.dDesc);
            holder.vPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    PopupManager.getInstance().buildMenu(v.getContext(),
                            v, R.menu.menu_device_action, new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    if (onMenuClickListener != null)
                                        onMenuClickListener.onMenuClick(item.getItemId(), selectedDevice);
                                    return true;
                                }
                            });
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            PopupManager.getInstance().show();
                        }
                    });
                }
            });
        }

        holder.vPopupMenu.setVisibility(mSelectedItemsIds.size() > 0 ? View.GONE : View.VISIBLE);
        holder.vContainer.setBackgroundColor(mSelectedItemsIds.get(position) ? mSelectedBgColor : 0);

        return convertView;
    }

    public class ViewHolder {
        public View vContainer, vPopupMenu;
        public DeviceIcon vIndex;
        public TextView tvDeviceName, tvDeviceDesc;
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
            filteredData = (ArrayList<Device>) results.values;
            notifyDataSetChanged();
        }
    }
}
