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
import com.tnpro85.mytvchannels.listener.ChannelItemClickListener;
import com.tnpro85.mytvchannels.models.Channel;

import java.util.ArrayList;

public class ChannelsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private LayoutInflater mInflater;
    private ArrayList<Channel> originalData;
    private ArrayList<Channel> filteredData;
    private ChannelFilter channelFilter;
    private SparseBooleanArray mSelectedItemsIds;
    private ChannelItemClickListener onChannelItemClickListener;

    public ChannelsAdapter(Context context, ChannelItemClickListener onChannelItemClickListener) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSelectedItemsIds = new SparseBooleanArray();
        this.onChannelItemClickListener = onChannelItemClickListener;
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

    public class ChannelHolder extends RecyclerView.ViewHolder {
        public View vContainer, vPopupMenu;
        public TextView tvChannelName, tvChannelDesc, tvChannelNum;
        public Channel mChannel;
        public int mPos;

        public ChannelHolder(View v, final ChannelItemClickListener listener) {
            super(v);
            vContainer = v.findViewById(R.id.container);
            vPopupMenu = v.findViewById(R.id.vPopupMenu);
            vPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupManager.getInstance().buildMenu(v.getContext(),
                            v, R.menu.menu_device_action, new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    if(listener != null)
                                        listener.onItemMenuClick(item.getItemId(), mChannel);
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
            tvChannelName = (TextView) v.findViewById(R.id.tvName);
            tvChannelDesc = (TextView) v.findViewById(R.id.tvDesc);
            tvChannelNum = (TextView) v.findViewById(R.id.tvIndex);
            vContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onItemClick(mPos, mChannel);
                }
            });
            vContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener != null) {
                        listener.onItemLongClick(mPos, mChannel);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.row_channel, parent, false);
        return new ChannelHolder(v, onChannelItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ChannelHolder) {
            ChannelHolder cHolder = (ChannelHolder)holder;
            Channel selectedChannel = getItem(position);
            cHolder.mChannel = selectedChannel;
            cHolder.mPos = position;

            if(selectedChannel != null) {
                String num = selectedChannel.cNum + "";
                cHolder.tvChannelNum.setText(num);
                cHolder.tvChannelName.setText(selectedChannel.cName);
                cHolder.tvChannelDesc.setText(selectedChannel.cDesc);
            }

            cHolder.vPopupMenu.setVisibility(mSelectedItemsIds.size() > 0 ? View.GONE : View.VISIBLE);
            cHolder.vContainer.setBackgroundResource(mSelectedItemsIds.get(position) ? R.color.list_selected_bg : R.drawable.selector);
        }
    }

    @Override
    public int getItemCount() {
        if (filteredData != null)
            return filteredData.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        if (channelFilter == null)
            channelFilter = new ChannelFilter();

        return channelFilter;
    }

    public Channel getItem(int position) {
        if (filteredData != null && position >= 0 && position < filteredData.size())
            return filteredData.get(position);
        return null;
    }

    public class ChannelFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = originalData;
                results.count = originalData.size();
            } else {
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
