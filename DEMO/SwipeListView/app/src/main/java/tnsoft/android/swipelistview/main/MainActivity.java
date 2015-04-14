package tnsoft.android.swipelistview.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tnsoft.android.swipelistview.adapter.Item;
import tnsoft.android.swipelistview.adapter.ItemAdapter;
import vn.com.vng.swipelistview.BaseSwipeListViewListener;
import vn.com.vng.swipelistview.MenuItemRow;
import vn.com.vng.swipelistview.SwipeListView;
import vn.com.vng.swipelistview.TypeListView;
import tnsoft.android.swipelistview.R;

public class MainActivity extends ActionBarActivity {

    ItemAdapter adapter ;
    List<Item> lstData ;
    SwipeListView swipeListView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeListView = (SwipeListView) findViewById(R.id.list_view);

        lstData = new ArrayList<>();
        lstData.add(new Item("1"));
        lstData.add(new Item("2"));
        lstData.add(new Item("3"));
        lstData.add(new Item("4"));
        lstData.add(new Item("5"));
        lstData.add(new Item("6"));
        lstData.add(new Item("7"));
        lstData.add(new Item("8"));
        lstData.add(new Item("9"));
        lstData.add(new Item("10"));
        lstData.add(new Item("11"));
        lstData.add(new Item("12"));
        lstData.add(new Item("13"));
        lstData.add(new Item("14"));
        lstData.add(new Item("15"));
        lstData.add(new Item("16"));

        adapter = new ItemAdapter(this,lstData, SwipeListView.SWIPE_ACTION_REVEAL);

        swipeListView.setSwipeListViewListener(new BaseSwipeListViewListener(){
            public void onClickBackView(int i) {
                Log.d("swipe", String.format("onClickBackView %d", new Object[]{Integer.valueOf(i)}));
            }

            public void onClickFrontView(int i) {
                Log.d("swipe", String.format("onClickFrontView %d", new Object[]{Integer.valueOf(i)}));
            }

            public void onOpened(int i, boolean z) {
            }

            public void onClosed(int i, boolean z) {
            }

            public void onDismiss(int[] positions) {
                for (int i : positions){
                    MainActivity.this.lstData.remove(i);
                }

                MainActivity.this.adapter.notifyDataSetChanged();
            }

            public void onMove(int i, float f) {
                Log.d("Move",String.valueOf(f));
            }


            public void onStartClose(int i, boolean z) {
                Log.d("swipe", String.format("onStartClose %d", new Object[]{Integer.valueOf(i)}));
            }

            public void onStartOpen(int i, int i2, boolean z) {
                Log.d("swipe", String.format("onStartOpen %d - action %d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)}));
            }

            public void onPlusItem(int position){
                int start =  swipeListView.getFirstVisiblePosition();
                TextView textView = (TextView) swipeListView.getChildAt(position- start).findViewById(R.id.name);
                String text = textView.getText().toString();
                int plus = Integer.valueOf(text) + 1;
                textView.setText(String.valueOf(plus));
            }

            public void onSubItem(int position){
                int start =  swipeListView.getFirstVisiblePosition();
                TextView textView = (TextView) swipeListView.getChildAt(position - start).findViewById(R.id.name);
                String text = textView.getText().toString();
                int plus = Integer.valueOf(text) - 1;
                textView.setText(String.valueOf(plus));
            }

            public void onEditItem(int position){
                Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                startActivity(intent);
            }

            public void onMenuCloseClick(int position){

            }

            public void onMenuEditClick(int position){
                Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                startActivity(intent);
            }

            public void onMenuDeleteClick(int position){

            }

        });

        swipeListView.setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_reveal :
                swipeListView.setSwipeAction(SwipeListView.SWIPE_ACTION_REVEAL);
                swipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_BOTH);
                adapter = new ItemAdapter(this,lstData, SwipeListView.SWIPE_ACTION_REVEAL);
                swipeListView.setAdapter(adapter);
                return true ;
            case R.id.action_todo :
                swipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_BOTH);
                swipeListView.setSwipeAction(SwipeListView.SWIPE_ACTION_TODO);
                adapter = new ItemAdapter(this,lstData, SwipeListView.SWIPE_ACTION_TODO);
                swipeListView.setAdapter(adapter);
                return true ;

            case R.id.action_menu_fill_part:
                swipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
                swipeListView.setSwipeAction(SwipeListView.SWIPE_ACTION_MENU_FILL_PART);
                swipeListView.setTypeListView(TypeListView.EMAIL);
                adapter = new ItemAdapter(this,lstData, TypeListView.EMAIL, MenuItemRow.FILL_PART_TYPE, SwipeListView.SWIPE_ACTION_MENU_FILL_PART);
                swipeListView.setAdapter(adapter);
                return true ;
            case R.id.action_menu_fill_all :
                swipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
                swipeListView.setSwipeAction(SwipeListView.SWIPE_ACTION_MENU_FILL_ALL);
                swipeListView.setTypeListView(TypeListView.EMAIL);
                adapter = new ItemAdapter(this,lstData, TypeListView.EMAIL, MenuItemRow.FILL_ALL_TYPE, SwipeListView.SWIPE_ACTION_MENU_FILL_ALL);
                swipeListView.setAdapter(adapter);
                return true ;
        }

        return super.onOptionsItemSelected(item);
    }
}
