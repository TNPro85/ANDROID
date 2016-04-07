package com.tnpro85.mytvchannels;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tnpro.core.uicontrols.MultiStateView;
import com.tnpro.core.utils.AnimationUtils;
import com.tnpro.core.utils.KeyboardUtils;
import com.tnpro.core.utils.ViewUtils;
import com.tnpro85.mytvchannels.adapter.ChannelsAdapter;
import com.tnpro85.mytvchannels.adapter.DevicePickerAdapter;
import com.tnpro85.mytvchannels.data.Const;
import com.tnpro85.mytvchannels.db.DBHelper;
import com.tnpro85.mytvchannels.listener.ChannelItemClickListener;
import com.tnpro85.mytvchannels.models.Channel;
import com.tnpro85.mytvchannels.models.Device;
import com.tnpro85.mytvchannels.uicontrols.DividerItemDecoration;

import java.util.ArrayList;

public class ActChannelList extends ActBase {

    private Device curDevice;
    private ArrayList<Channel> lsChannels;
    private ChannelsAdapter adapterChannel;
    private ActionMode mChannelActionMode;
    private ActionMode.Callback mChannelActionModeCB;

    // Search views
    private EditText mSearchEt;
    private boolean mSearchOpened;
    private MenuItem mSearchAction;

    // Main layouts
    private RecyclerView rvChannel;
    private FloatingActionButton fabAddChannel;
    private Snackbar sbError;
    private MultiStateView layoutMultiStateView;

    @Override
    protected void initUI(Bundle savedInstanceState) {
        super.initUI(savedInstanceState);
        setContentView(R.layout.act_channel);
        showHomeAsUpEnable(true);

        vContainer = findViewById(R.id.container);
        layoutMultiStateView = (MultiStateView) findViewById(R.id.layoutMultiStateView);
        layoutMultiStateView.show(MultiStateView.STATE_LOADING);

        DividerItemDecoration mDivider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mDivider.setMargin(ViewUtils.dpToPx(this, 68), 0, ViewUtils.dpToPx(this, 10), 0);
        mDivider.setDividerHeight(ViewUtils.dpToPx(this, 0.5f));

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvChannel = (RecyclerView) findViewById(R.id.rvChannel);
        rvChannel.setLayoutManager(llm);
        rvChannel.addItemDecoration(mDivider);
        rvChannel.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mSearchOpened)
                    return;

                if (dy > 0)
                    AnimationUtils.goneViewWithAnim(fabAddChannel, R.anim.bottom_sheet_slide_out);
                else
                    AnimationUtils.showViewWithAnim(fabAddChannel, R.anim.bottom_sheet_slide_in);
            }
        });

        mChannelActionModeCB = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_act_delete, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.action_delete) {
                    new AlertDialog.Builder(ActChannelList.this)
                            .setTitle(R.string.str_confirm)
                            .setMessage(R.string.str_channel_delete_multi_confirm)
                            .setPositiveButton(R.string.str_delete, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    boolean result = false;
                                    showLoadingDlg(R.string.str_doing, true);
                                    try {
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
                                        result = true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        hideLoadingDlg();
                                        Toast.makeText(ActChannelList.this, result ? getString(R.string.str_deleted) : getString(R.string.str_error_general), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mChannelActionMode = null;
                adapterChannel.getSelectedIds().clear();
                adapterChannel.notifyDataSetChanged();
            }
        };

        fabAddChannel = (FloatingActionButton) findViewById(R.id.myFAB);
        fabAddChannel.setVisibility(View.GONE);
        fabAddChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActChannelList.this, ActChannelAdd.class);
                intent.putExtra(Const.EXTRA.DEVICE, curDevice);
                ActChannelList.this.startActivityForResult(intent, Const.REQCODE.ADD_CHANNEL);
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            curDevice = data.getParcelable(Const.EXTRA.DEVICE);
            if (curDevice != null)
                setTitle(curDevice.dName);
        }

        adapterChannel = new ChannelsAdapter(this, new ChannelItemClickListener() {
            @Override
            public void onItemClick(int position, Object obj) {
                if(mChannelActionMode != null) {
                    toggleDeviceItem(position);
                }
            }

            @Override
            public void onItemLongClick(int position, Object obj) {
                toggleDeviceItem(position);
            }

            @Override
            public void onItemMenuClick(int menuId, final Object obj) {
                switch (menuId) {
                    case R.id.action_edit:
                        if (obj instanceof Channel) {
                            Channel selected = (Channel) obj;
                            Intent intent = new Intent(ActChannelList.this, ActChannelAdd.class);
                            intent.putExtra(Const.EXTRA.DEVICE, curDevice);
                            intent.putExtra(Const.EXTRA.CHANNEL, selected);
                            ActChannelList.this.startActivityForResult(intent, Const.REQCODE.EDIT_CHANNEL);
                        }
                        break;
                    case R.id.action_copy:
                        final ArrayList<Device> lsDevices = DBHelper.getInstance().getAllDevices();
                        for(Device d : lsDevices) {
                            if(d.dName.equals(curDevice.dName)) {
                                lsDevices.remove(d);
                                break;
                            }
                        }
                        final DevicePickerAdapter arrDeviceName = new DevicePickerAdapter(ActChannelList.this, lsDevices);
                        new AlertDialog.Builder(ActChannelList.this)
                                .setTitle(R.string.str_device_picker)
                                .setAdapter(arrDeviceName, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int pos) {
                                        if (obj instanceof Channel) {
                                            Channel selected = (Channel) obj;
                                            Intent intent = new Intent(ActChannelList.this, ActChannelAdd.class);
                                            intent.putExtra(Const.EXTRA.DEVICE, lsDevices.get(pos));
                                            intent.putExtra(Const.EXTRA.CHANNEL, selected);
                                            intent.putExtra(Const.EXTRA.COPYING_CHANNEL, true);
                                            ActChannelList.this.startActivityForResult(intent, Const.REQCODE.COPY_CHANNEL);
                                        }
                                    }
                                }).show();
                        break;
                    case R.id.action_delete:
                        new AlertDialog.Builder(ActChannelList.this)
                                .setTitle(R.string.str_confirm)
                                .setMessage(R.string.str_channel_delete_single_confirm)
                                .setPositiveButton(R.string.str_delete, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        boolean result = false;
                                        try {
                                            if (obj instanceof Channel) {
                                                showLoadingDlg(R.string.str_doing, false);
                                                Channel selected = (Channel) obj;
                                                DBHelper.getInstance().deleteChannel(selected);
                                                adapterChannel.remove(selected);
                                                adapterChannel.notifyDataSetChanged();
                                                lsChannels = new ArrayList<>(adapterChannel.getData());
                                                updateLayout();
                                                result = true;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        } finally {
                                            hideLoadingDlg();
                                            Toast.makeText(ActChannelList.this, result ? getString(R.string.str_deleted) : getString(R.string.str_error_general), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                        break;
                }
            }
        });

        rvChannel.setAdapter(adapterChannel);

        lsChannels = DBHelper.getInstance().getAllChannel(curDevice);
        adapterChannel.setData(lsChannels);
        adapterChannel.notifyDataSetChanged();

        updateLayout();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Const.REQCODE.ADD_CHANNEL:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Channel channel = data.getParcelableExtra(Const.EXTRA.CHANNEL);
                        if (channel != null) {
                            lsChannels.add(channel);
                            adapterChannel.setData(lsChannels);
                            adapterChannel.notifyDataSetChanged();
                            rvChannel.smoothScrollToPosition(lsChannels.size() - 1);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    sbError = Snackbar.make(vContainer, getString(R.string.str_added), Snackbar.LENGTH_SHORT);
                                    sbError.show();
                                }
                            }, 1000);
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    sbError = Snackbar.make(vContainer, getString(R.string.str_error_general), Snackbar.LENGTH_SHORT);
                                    sbError.show();
                                }
                            }, 1000);
                        }
                    }

                    updateLayout();
                }
                break;

            case Const.REQCODE.EDIT_CHANNEL:
                if(resultCode == RESULT_OK) {
                    if (data != null) {
                        Channel channel = data.getParcelableExtra(Const.EXTRA.CHANNEL);
                        for (int i = 0; i < lsChannels.size(); i++) {
                            Channel c = lsChannels.get(i);
                            if (c.cDevice.equals(channel.cDevice) && c.cNum == channel.cNum) {
                                c.cName = channel.cName;
                                c.cDesc = channel.cDesc;
                                break;
                            }
                        }

                        adapterChannel.setData(lsChannels);
                        adapterChannel.notifyDataSetChanged();
                    }
                }
                break;

            case Const.REQCODE.COPY_CHANNEL:
                if(resultCode == RESULT_OK) {
                    sbError = Snackbar.make(vContainer, getString(R.string.str_copied), Snackbar.LENGTH_SHORT);
                    sbError.show();
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
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (mSearchOpened) {
                    closeSearchBar();
                    return true;
                }
            }
        }

        return super.onKeyUp(keyCode, event);
    }

    private void toggleDeviceItem(int position) {
        adapterChannel.toggleSelection(position);
        SparseBooleanArray selectedItem = adapterChannel.getSelectedIds();
        if(selectedItem.size() > 0) {
            if(mChannelActionMode == null)
                mChannelActionMode = startSupportActionMode(mChannelActionModeCB);
            mChannelActionMode.setTitle(selectedItem.size() + " " + getString(R.string.str_selected));
        }
        else if(mChannelActionMode != null)
            mChannelActionMode.finish();
    }
    
    private void updateLayout() {
        if (lsChannels.size() > 0) {
            layoutMultiStateView.hide();
            rvChannel.setVisibility(View.VISIBLE);
        } else {
            layoutMultiStateView.show(MultiStateView.STATE_EMPTY);
            layoutMultiStateView.setEmptyText(getResources().getString(R.string.str_empty_channels));
            layoutMultiStateView.setEmptySubText(getString(R.string.str_empty_channels_hint));
            rvChannel.setVisibility(View.GONE);
        }

        if (mActionBar != null) {
            mActionBar.setSubtitle(lsChannels.size()
                    + " " + (lsChannels.size() > 1 ?
                    getString(R.string.str_channel_plural).toLowerCase() : getString(R.string.str_channel_singular).toLowerCase()));
        }

        if (!mSearchOpened)
            AnimationUtils.showViewWithAnim(fabAddChannel, R.anim.bottom_sheet_slide_in);
    }

    private void openSearchBar() {
        // Set custom view on action bar.
        if (mActionBar != null) {
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setCustomView(R.layout.actionbar_search_view);

            if (mActionBar.getCustomView() != null) {
                // Search edit text field setup.
                mSearchEt = (EditText) mActionBar.getCustomView().findViewById(R.id.etSearch);
                mSearchEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

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

        // Hide Floating Action Button
        AnimationUtils.goneViewWithAnim(fabAddChannel, R.anim.bottom_sheet_slide_out);
    }

    private void closeSearchBar() {
        // Hide keyboard
        KeyboardUtils.hideKeyboard(mSearchEt);

        // Reset adapter filter to display full datalist
        adapterChannel.getFilter().filter("");

        // Change search icon accordingly.
        mSearchAction.setIcon(R.drawable.abc_ic_search_api_mtrl_alpha);
        mSearchOpened = false;

        // Remove custom view.
        if (mActionBar != null) {
            mActionBar.setDisplayShowCustomEnabled(false);
        }

        // Show Floating Action Button
        AnimationUtils.showViewWithAnim(fabAddChannel, R.anim.bottom_sheet_slide_in);
    }
}
