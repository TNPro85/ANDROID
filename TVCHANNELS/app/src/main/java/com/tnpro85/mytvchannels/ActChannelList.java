package com.tnpro85.mytvchannels;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.tnpro.core.uicontrols.MultiStateView;
import com.tnpro.core.utils.AnimationUtils;
import com.tnpro.core.utils.KeyboardUtils;
import com.tnpro85.mytvchannels.adapter.ChannelAdapter;
import com.tnpro85.mytvchannels.data.Const;
import com.tnpro85.mytvchannels.db.DBHelper;
import com.tnpro85.mytvchannels.models.Channel;
import com.tnpro85.mytvchannels.models.Device;

import java.util.ArrayList;

public class ActChannelList extends ActBase {

    private Device curDevice;
    private ArrayList<Channel> lsChannels;
    private ChannelAdapter adapterChannel;

    // Search views
    private EditText mSearchEt;
    private boolean mSearchOpened;
    private MenuItem mSearchAction;

    // Main layouts
    private ListView lvChannel;
    private FloatingActionButton fabAddChannel;
    private Snackbar sbError;
    private MultiStateView layoutMultiStateView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Const.REQCODE.ADD_CHANNEL:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Channel channel = data.getParcelableExtra("channel");
                        if (channel != null) {
                            lsChannels.add(channel);
                            adapterChannel.setData(lsChannels);
                            adapterChannel.notifyDataSetChanged();
                            lvChannel.smoothScrollToPosition(lsChannels.size() - 1);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    sbError = Snackbar.make(vContainer, "Added", Snackbar.LENGTH_SHORT);
                                    sbError.show();
                                }
                            }, 1000);
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    sbError = Snackbar.make(vContainer, "Error! Please try again.", Snackbar.LENGTH_SHORT);
                                    sbError.show();
                                }
                            }, 1000);
                        }
                    }

                    updateLayout();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_channel, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_search: {
                if (mSearchOpened) {
                    closeSearchBar();
                } else {
                    openSearchBar();
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initUI(Bundle savedInstanceState) {
        super.initUI(savedInstanceState);
        setContentView(R.layout.act_channel);

        vContainer = findViewById(R.id.container);
        layoutMultiStateView = (MultiStateView) findViewById(R.id.layoutMultiStateView);
        layoutMultiStateView.show(MultiStateView.STATE_LOADING);

        lvChannel = (ListView) findViewById(R.id.lvChannel);
        lvChannel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position < adapterChannel.getCount()) {
//                    Channel selectedChannel = adapterChannel.getItem(position);
//                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            lvChannel.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
            lvChannel.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    // Prints the count of selected Items in title
                    mode.setTitle(lvChannel.getCheckedItemCount() + " " + getString(R.string.str_selected));
                    // Toggle the state of item after every click on it
                    adapterChannel.toggleSelection(position);
                }

                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.menu_act_delete, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    if (item.getItemId() == R.id.action_delete) {
                        SparseBooleanArray selected = adapterChannel.getSelectedIds();
                        int size = selected.size();
                        for (int i = size - 1; i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                Channel selectedItem = adapterChannel.getItem(selected.keyAt(i));
                                DBHelper.getInstance().deleteChannel(selectedItem);
                                adapterChannel.remove(selectedItem);
                            }
                        }

                        // Reset selected list and update ListView
                        selected.clear();
                        adapterChannel.notifyDataSetChanged();
                        lsChannels = new ArrayList<>(adapterChannel.getData());
                        updateLayout();

                        // Close CAB (Contextual Action Bar)
                        mode.finish();
                        return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    adapterChannel.getSelectedIds().clear();
                }
            });
        }

        fabAddChannel = (FloatingActionButton) findViewById(R.id.myFAB);
        fabAddChannel.setVisibility(View.GONE);
        fabAddChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActChannelList.this, ActChannelAdd.class);
                intent.putExtra("device", curDevice);
                ActChannelList.this.startActivityForResult(intent, Const.REQCODE.ADD_CHANNEL);
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if(mSearchOpened) {
                    closeSearchBar();
                    return true;
                }
            }
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        Bundle data = getIntent().getExtras();
        if(data != null) {
            curDevice = data.getParcelable("device");
            if(curDevice != null) {
                if(mActionBar != null) {
                    mActionBar.setTitle(curDevice.dName);
                    mActionBar.setDisplayHomeAsUpEnabled(true);
                }
            }
        }

        adapterChannel = new ChannelAdapter(this);
        lvChannel.setAdapter(adapterChannel);

        lsChannels = DBHelper.getInstance().getAllChannel(curDevice);
        adapterChannel.setData(lsChannels);
        adapterChannel.notifyDataSetChanged();

        updateLayout();
    }

    private void updateLayout() {
        if(lsChannels.size() > 0) {
            layoutMultiStateView.hide();
            lvChannel.setVisibility(View.VISIBLE);
        }
        else {
            layoutMultiStateView.show(MultiStateView.STATE_EMPTY);
            layoutMultiStateView.setEmptyText(getResources().getString(R.string.str_empty_channels));
            lvChannel.setVisibility(View.GONE);
        }

        if(!mSearchOpened)
            fabAddChannel.setVisibility(View.VISIBLE);

        if(mActionBar != null) {
            mActionBar.setSubtitle(lsChannels.size() + " " + (lsChannels.size() > 1 ? getString(R.string.str_device_plural) : getString(R.string.str_device_singular)));
        }
    }

    private void openSearchBar() {
        fabAddChannel.setVisibility(View.GONE);

        // Set custom view on action bar.
        if(mActionBar != null) {
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setCustomView(R.layout.actionbar_search_view);

            if(mActionBar.getCustomView() != null) {
                // Search edit text field setup.
                mSearchEt = (EditText) mActionBar.getCustomView().findViewById(R.id.etSearch);
                mSearchEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        adapterChannel.getFilter().filter(s);
                    }
                });
            }
        }

        // Show keyboard right away
        KeyboardUtils.showKeyboard(mSearchEt);

        // Change search icon accordingly.
        mSearchOpened = true;
        mSearchAction.setIcon(R.drawable.abc_ic_clear_mtrl_alpha);
    }

    private void closeSearchBar() {
        AnimationUtils.showViewWithAnim(fabAddChannel, R.anim.abc_fade_in);

        // Hide keyboard
        KeyboardUtils.hideKeyboard(mSearchEt);

        // Reset adapter filter to display full datalist
        adapterChannel.getFilter().filter("");

        // Change search icon accordingly.
        mSearchAction.setIcon(R.drawable.abc_ic_search_api_mtrl_alpha);
        mSearchOpened = false;

        // Remove custom view.
        if(mActionBar != null) {
            mActionBar.setDisplayShowCustomEnabled(false);
        }
    }
}
