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

import com.tnpro.core.uicontrols.MultiStateView;
import com.tnpro.core.utils.AnimationUtils;
import com.tnpro.core.utils.KeyboardUtils;
import com.tnpro.core.utils.ViewUtils;
import com.tnpro85.mytvchannels.adapter.DevicesAdapter;
import com.tnpro85.mytvchannels.data.Const;
import com.tnpro85.mytvchannels.db.DBHelper;
import com.tnpro85.mytvchannels.db.Global;
import com.tnpro85.mytvchannels.listener.DeviceItemClickListener;
import com.tnpro85.mytvchannels.models.Device;
import com.tnpro85.mytvchannels.uicontrols.DividerItemDecoration;
import com.tnpro85.mytvchannels.utils.LocaleUtil;
import com.tnpro85.mytvchannels.utils.Utils;

import java.util.ArrayList;

public class ActMain extends ActBase {

    private boolean confirmExit = true;
    private ArrayList<Device> lsDevices;
    private DevicesAdapter mAdapter;
    private ActionMode mDeviceActionMode;
    private ActionMode.Callback mDeviceActionModeCB;

    // Search views
    private EditText mSearchEt;
    private boolean mSearchOpened;
    private MenuItem mSearchAction;

    // Main layouts
    private RecyclerView rvDevices;
    private FloatingActionButton fabAddDevice;
    private Snackbar sbError;
    private MultiStateView layoutMultiStateView;

    @Override
    protected void initUI(Bundle savedInstanceState) {
        super.initUI(savedInstanceState);
        setContentView(R.layout.act_main);

        vContainer = findViewById(R.id.container);
        layoutMultiStateView = (MultiStateView) findViewById(R.id.layoutMultiStateView);
        layoutMultiStateView.show(MultiStateView.STATE_LOADING);

        DividerItemDecoration mDivider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mDivider.setMargin(ViewUtils.dpToPx(this, 68), 0, ViewUtils.dpToPx(this, 10), 0);
        mDivider.setDividerHeight(ViewUtils.dpToPx(this, 0.5f));

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvDevices = (RecyclerView) findViewById(R.id.rvDevices);
        rvDevices.setLayoutManager(llm);
        rvDevices.addItemDecoration(mDivider);
        rvDevices.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mSearchOpened)
                    return;

                if (dy > 0)
                    AnimationUtils.goneViewWithAnim(fabAddDevice, R.anim.bottom_sheet_slide_out);
                else
                    AnimationUtils.showViewWithAnim(fabAddDevice, R.anim.bottom_sheet_slide_in);
            }
        });

        mDeviceActionModeCB = new ActionMode.Callback() {
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
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete: {
                        new AlertDialog.Builder(ActMain.this)
                                .setTitle(getString(R.string.str_confirm))
                                .setMessage(getString(R.string.str_device_delete_confirm))
                                .setPositiveButton(getString(R.string.str_delete), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        showLoadingDlg(R.string.str_doing, true);

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                boolean result = false;
                                                try {
                                                    SparseBooleanArray selected = mAdapter.getSelectedIds();
                                                    int size = selected.size();
                                                    for (int i = size - 1; i >= 0; i--) {
                                                        if (selected.valueAt(i)) {
                                                            Device selectedItem = mAdapter.getItem(selected.keyAt(i));
                                                            result = DBHelper.getInstance().deleteDevice(selectedItem);
                                                            mAdapter.remove(selectedItem);
                                                        }
                                                    }

                                                    // Reset selected list and update ListView
                                                    lsDevices = new ArrayList<>(mAdapter.getData());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                } finally {
                                                    final String msg = result ? getString(R.string.str_deleted) : getString(R.string.str_error_general);
                                                    runOnUI(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            updateLayout();
                                                            mode.finish();
                                                            hideLoadingDlg();
                                                            Utils.showMsg(ActMain.this, msg);
                                                        }
                                                    });
                                                }
                                            }
                                        }).start();
                                    }
                                })
                                .setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mDeviceActionMode = null;
                mAdapter.getSelectedIds().clear();
                mAdapter.notifyDataSetChanged();
            }
        };

        fabAddDevice = (FloatingActionButton) findViewById(R.id.myFAB);
        fabAddDevice.setVisibility(View.GONE);
        fabAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActMain.this, ActDevice.class);
                ActMain.this.startActivityForResult(intent, Const.REQCODE.ADD_DEVICE);
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mAdapter = new DevicesAdapter(ActMain.this, new DeviceItemClickListener() {
            @Override
            public void onItemClick(int position, Object obj) {
                if (mDeviceActionMode != null) {
                    toggleDeviceItem(position);
                } else if (position < mAdapter.getItemCount()) {
                    Device selectedDevice = mAdapter.getItem(position);
                    Intent intent = new Intent(ActMain.this, ActChannelList.class);
                    intent.putExtra(Const.EXTRA.DEVICE, selectedDevice);
                    ActMain.this.startActivity(intent);
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
                        if (obj instanceof Device) {
                            Device selected = (Device) obj;
                            Intent intent = new Intent(ActMain.this, ActDevice.class);
                            intent.putExtra(Const.EXTRA.DEVICE, selected);
                            ActMain.this.startActivityForResult(intent, Const.REQCODE.EDIT_DEVICE);
                        }
                        break;
                    case R.id.action_delete:
                        new AlertDialog.Builder(ActMain.this)
                                .setTitle(getString(R.string.str_confirm))
                                .setMessage(getString(R.string.str_device_delete_confirm))
                                .setPositiveButton(getString(R.string.str_delete), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        boolean result = false;
                                        try {
                                            if (obj instanceof Device) {
                                                showLoadingDlg(R.string.str_doing, false);
                                                Device selected = (Device) obj;
                                                DBHelper.getInstance().deleteDevice(selected);
                                                mAdapter.remove(selected);
                                                mAdapter.notifyDataSetChanged();
                                                lsDevices = new ArrayList<>(mAdapter.getData());
                                                updateLayout();
                                                result = true;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        } finally {
                                            hideLoadingDlg();
                                            Utils.showMsg(ActMain.this, result ? R.string.str_deleted : R.string.str_error_general);
                                        }
                                    }
                                })
                                .setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
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

        rvDevices.setAdapter(mAdapter);
        refreshData(false);
    }

    private void toggleDeviceItem(int position) {
        mAdapter.toggleSelection(position);
        SparseBooleanArray selectedItem = mAdapter.getSelectedIds();
        if (selectedItem.size() > 0) {
            if (mDeviceActionMode == null)
                mDeviceActionMode = startSupportActionMode(mDeviceActionModeCB);
            mDeviceActionMode.setTitle(selectedItem.size() + " " + getString(R.string.str_selected));
        } else if (mDeviceActionMode != null)
            mDeviceActionMode.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Global.isNeedToRefreshMain) {
            Global.isNeedToRefreshMain = false;
            refreshData(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Const.REQCODE.ADD_DEVICE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Device device = data.getParcelableExtra(Const.EXTRA.DEVICE);
                        if (device != null) {
                            lsDevices.add(device);
                            mAdapter.setData(lsDevices);
                            mAdapter.notifyDataSetChanged();

                            if (lsDevices.size() < 100)
                                rvDevices.smoothScrollToPosition(lsDevices.size() - 1);
                            else
                                rvDevices.scrollToPosition(lsDevices.size() - 1);

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
            case Const.REQCODE.EDIT_DEVICE: {
                if (resultCode == RESULT_OK) {
                    closeSearchBar();
                    if (data != null) {
                        Device device = data.getParcelableExtra(Const.EXTRA.DEVICE);
                        String resultType = data.getStringExtra(Const.EXTRA.RESULT_TYPE);
                        if (device != null) {
                            if (resultType.equals(ActDevice.RESULT_ADD)) {
                                lsDevices.add(device);
                                mAdapter.setData(lsDevices);
                                mAdapter.notifyDataSetChanged();

                                if (lsDevices.size() < 100)
                                    rvDevices.smoothScrollToPosition(lsDevices.size() - 1);
                                else
                                    rvDevices.scrollToPosition(lsDevices.size() - 1);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sbError = Snackbar.make(vContainer, getString(R.string.str_added), Snackbar.LENGTH_SHORT);
                                        sbError.show();
                                    }
                                }, 1000);
                            } else if (resultType.equals(ActDevice.RESULT_UPDATE)) {
                                for (int i = 0; i < lsDevices.size(); i++) {
                                    if (lsDevices.get(i).dName.equals(device.dName)) {
                                        lsDevices.get(i).dDesc = device.dDesc;
                                        break;
                                    }
                                }

                                mAdapter.setData(lsDevices);
                                mAdapter.notifyDataSetChanged();
                            }

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
                }
            }

            case Const.REQCODE.GO_SETTINGS:
                LocaleUtil.loadLocale(getBaseContext());
                invalidateOptionsMenu();
                updateLayout();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_main, menu);
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

            case R.id.action_refresh: {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshData(true);
                        hideLoadingDlg();
                        Utils.showMsg(ActMain.this, R.string.str_refreshed);
                    }
                }, 700);
                return true;
            }

            case R.id.action_settings: {
                Intent intent = new Intent(ActMain.this, ActSetting.class);
                ActMain.this.startActivityForResult(intent, Const.REQCODE.GO_SETTINGS);
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

                if (confirmExit) {
                    confirmExit = false;
                    Utils.showMsg(ActMain.this, R.string.str_back_confirm);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            confirmExit = true;
                        }
                    }, 2000);
                    return true;
                }
            }
        }

        return super.onKeyUp(keyCode, event);
    }

    private void refreshData(final boolean showLoading) {
        mAdapter.getSelectedIds().clear();
        if(showLoading)
            showLoadingDlg(R.string.str_doing, false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                lsDevices = DBHelper.getInstance().getAllDevices();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(lsDevices);
                        mAdapter.notifyDataSetChanged();
                        updateLayout();

                        if(showLoading)
                            hideLoadingDlg();
                    }
                });
            }
        }).start();
    }

    private void openSearchBar() {
        // Set custom view on action bar.
        if (mActionBar != null) {
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setCustomView(R.layout.actionbar_search_view);

            // Search edit text field setup.
            if (mActionBar.getCustomView() != null) {
                mSearchEt = (EditText) mActionBar.getCustomView().findViewById(R.id.etSearch);
                mSearchEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    @Override
                    public void afterTextChanged(Editable s) {
                        mAdapter.getFilter().filter(s);
                    }
                });
            }
        }

        // Show keyboard right away
        KeyboardUtils.showKeyboard(mSearchEt);

        // Change search icon accordingly.
        mSearchOpened = true;
        mSearchAction.setIcon(R.drawable.abc_ic_clear_mtrl_alpha);

        AnimationUtils.goneViewWithAnim(fabAddDevice, R.anim.bottom_sheet_slide_out);
    }

    private void closeSearchBar() {
        // Hide keyboard
        KeyboardUtils.hideKeyboard(mSearchEt);

        // Reset adapter filter to display full datalist
        mAdapter.getFilter().filter("");

        // Change search icon accordingly.
        mSearchAction.setIcon(R.drawable.abc_ic_search_api_mtrl_alpha);
        mSearchOpened = false;

        // Remove custom view.
        if (mActionBar != null) {
            mActionBar.setDisplayShowCustomEnabled(false);
        }

        AnimationUtils.showViewWithAnim(fabAddDevice, R.anim.bottom_sheet_slide_in);
    }

    private void updateLayout() {
        if (lsDevices.size() > 0) {
            layoutMultiStateView.hide();
            rvDevices.setVisibility(View.VISIBLE);
        } else {
            layoutMultiStateView.show(MultiStateView.STATE_EMPTY);
            layoutMultiStateView.setEmptyText(getResources().getString(R.string.str_empty_devices));
            layoutMultiStateView.setEmptySubText(getResources().getString(R.string.str_empty_devices_hint));
            layoutMultiStateView.setEmptyDrawable(R.drawable.ic_empty_tv);
            rvDevices.setVisibility(View.GONE);
        }

        if (!mSearchOpened)
            AnimationUtils.showViewWithAnim(fabAddDevice, R.anim.bottom_sheet_slide_in);
    }
}
