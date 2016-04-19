package com.tnpro85.mytvchannels.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tnpro.core.uicontrols.PopupManager;
import com.tnpro85.mytvchannels.R;
import com.tnpro85.mytvchannels.listener.DeviceItemClickListener;
import com.tnpro85.mytvchannels.models.Device;
import com.tnpro85.mytvchannels.uicontrols.DeviceIcon;

import java.util.ArrayList;

public class DevicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private LayoutInflater mInflater;
    private ArrayList<Device> originalData;
    private ArrayList<Device> filteredData;
    private DeviceFilter deviceFilter;
    private SparseBooleanArray mSelectedItemsIds;
    private DeviceItemClickListener onDeviceItemClickListener;

    public DevicesAdapter(Context context, DeviceItemClickListener deviceItemClickListener) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSelectedItemsIds = new SparseBooleanArray();
        this.onDeviceItemClickListener = deviceItemClickListener;
    }

    public void setData(ArrayList<Device> data) {
        this.originalData = new ArrayList<>(data);
        this.filteredData = new ArrayList<>(data);
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

    static class DeviceHolder extends RecyclerView.ViewHolder {
        public View vContainer, vPopupMenu;
        public DeviceIcon vIndex;
        public TextView tvDeviceName, tvDeviceDesc;
        public Device mDevice;
        public int mPos;

        public DeviceHolder(View v, final DeviceItemClickListener listener) {
            super(v);
            vContainer = v.findViewById(R.id.container);
            vPopupMenu = v.findViewById(R.id.vPopupMenu);
            vPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    PopupManager.getInstance().buildMenu(v.getContext(),
                            v, R.menu.menu_device_action, new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    if (listener != null)
                                        listener.onItemMenuClick(item.getItemId(), mDevice);
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
            tvDeviceName = (TextView) v.findViewById(R.id.tvName);
            tvDeviceDesc = (TextView) v.findViewById(R.id.tvDesc);
            vIndex = (DeviceIcon) v.findViewById(R.id.vIndex);
            vContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onItemClick(mPos, mDevice);
                }
            });
            vContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener != null)
                        listener.onItemLongClick(mPos, mDevice);
                    return true;
                }
            });
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.row_device, parent, false);
        return new DeviceHolder(v, onDeviceItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Device selectedDevice = getItem(position);
        String pos = position + 1 + "";
        if(selectedDevice != null && holder instanceof DeviceHolder) {
            DeviceHolder dHolder = (DeviceHolder)holder;
            dHolder.mDevice = selectedDevice;
            dHolder.mPos = position;
            dHolder.vIndex.setText(pos);
            dHolder.tvDeviceName.setText(selectedDevice.dName);
            dHolder.tvDeviceDesc.setText(selectedDevice.dDesc);
            dHolder.vPopupMenu.setVisibility(mSelectedItemsIds.size() > 0 ? View.GONE : View.VISIBLE);
            dHolder.vContainer.setBackgroundResource(mSelectedItemsIds.get(position) ? R.color.list_selected_bg : R.drawable.selector);
        }
    }

    public Device getItem(int position) {
        if (filteredData != null && position >= 0 && position < filteredData.size())
            return filteredData.get(position);
        return null;
    }

    @Override
    public int getItemCount() {
        if (filteredData != null)
            return filteredData.size();
        return 0;
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
