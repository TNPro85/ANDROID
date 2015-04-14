package tnsoft.android.swipelistview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tnsoft.android.swipelistview.R;
import vn.com.vng.swipelistview.MenuItemRow;
import vn.com.vng.swipelistview.SwipeListView;
import vn.com.vng.swipelistview.TypeListView;

/**
 * Created by NhaN on 1/22/2015.
 */
public class ItemAdapter extends BaseAdapter{

    private Context context ;
    private List<Item> lstData = new ArrayList<Item>();
    private TypeListView type ;
    private Integer typeMenu ;
    private Integer swipeAction ;

    public ItemAdapter(Context context,List<Item> lstData, TypeListView type, Integer typeMenu, Integer swipeAction){
        this.context = context ;
        this.lstData = lstData ;
        this.type = type ;
        this.typeMenu = typeMenu;
        this.swipeAction = swipeAction ;
    }

    public ItemAdapter(Context context, List<Item> lstData,Integer swipeAction){
        this.context = context ;
        this.lstData = lstData ;
        this.swipeAction = swipeAction ;
    }


    @Override
    public int getCount() {
        return lstData.size();
    }

    @Override
    public Object getItem(int position) {
        return lstData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView ;
        Item item = (Item) getItem(position);
        ViewHolder viewHolder ;
        if (view == null) {
            view = mInflateView();
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.name.setText(item.getName());
        return view;
    }

    private View mInflateView(){
        View view = null ;
        switch (this.swipeAction){
            case SwipeListView.SWIPE_ACTION_REVEAL :
                view = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_item_row_reveal,null);
                break;
            case SwipeListView.SWIPE_ACTION_TODO :
                view = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_item_row_todo,null);
                break;
            case SwipeListView.SWIPE_ACTION_MENU_FILL_ALL :
            case SwipeListView.SWIPE_ACTION_MENU_FILL_PART :
                view = new MenuItemRow(this.context,this.type, this.typeMenu,R.layout.layout_item_row_menu);
                break;
        }
        return view ;
    }


    static class ViewHolder{
        TextView name ;
        Button btn ;
    }

    public TypeListView getListViewType(){
        return this.type ;
    }
}
