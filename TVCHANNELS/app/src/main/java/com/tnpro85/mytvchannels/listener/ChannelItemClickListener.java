package com.tnpro85.mytvchannels.listener;

public interface ChannelItemClickListener {
     void onItemClick(int position, Object obj);
     void onItemLongClick(int position, Object obj);
     void onItemMenuClick(int menuId, Object obj);
}
