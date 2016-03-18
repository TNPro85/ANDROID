package com.tnpro85.mytvchannels.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tnpro85.mytvchannels.R;
import com.tnpro85.mytvchannels.models.Channel;

import java.util.ArrayList;

public class ChannelAdapter extends BaseAdapter implements Filterable {

    private LayoutInflater mInflater;
    private ArrayList<Channel> originalData;
    private ArrayList<Channel> filteredData;
    private DeviceFilter deviceFilter;
    private SparseBooleanArray mSelectedItemsIds;
    private int mSelectedBgColor;

    public ChannelAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSelectedBgColor = context.getResources().getColor(R.color.list_selected_bg);
        mSelectedItemsIds = new SparseBooleanArray();
    }

    public void setData(ArrayList<Channel> data) {
        this.originalData = new ArrayList<>(data);
        this.filteredData = new ArrayList<>(data);
    }

    public ArrayList<Channel> getData() {
        return originalData;
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

    public void remove(Channel channel) {
        this.originalData.remove(channel);
        this.filteredData.remove(channel);
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
    public Channel getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.row_channel, null);
            holder.vContainer = convertView.findViewById(R.id.container);
            holder.tvChannelName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvChannelDesc = (TextView) convertView.findViewById(R.id.tvDesc);
            holder.tvChannelNum = (TextView) convertView.findViewById(R.id.tvIndex);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        Channel item = getItem(position);
        if(item != null) {
            holder.tvChannelNum.setText(item.cNum + "");
            holder.tvChannelName.setText(item.cName);
            holder.tvChannelDesc.setText(item.cDesc);
        }

        holder.vContainer.setBackgroundColor(mSelectedItemsIds.get(position) ? mSelectedBgColor : 0);

        return convertView;
    }

    public class ViewHolder {
        public View vContainer;
        public TextView tvChannelName, tvChannelDesc, tvChannelNum;
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
                ArrayList<Channel> data = new ArrayList<>();

                for (Channel channel : originalData) {
                    if (channel.cName.toLowerCase().contains(constraint.toString().toLowerCase()))
                        data.add(channel);
                }

                results.values = data;
                results.count = data.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Channel>) results.values;
            notifyDataSetChanged();
        }
    }
}
