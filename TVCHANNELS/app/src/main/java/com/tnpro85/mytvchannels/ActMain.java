package com.tnpro85.mytvchannels;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.tnpro.core.utils.KeyboardUtils;
import com.tnpro85.mytvchannels.adapter.DeviceAdapter;
import com.tnpro85.mytvchannels.data.Const;
import com.tnpro85.mytvchannels.db.DBHelper;
import com.tnpro85.mytvchannels.models.Device;

import java.util.ArrayList;


public class ActMain extends ActBase {

    private ArrayList<Device> lsDevices;
    private DeviceAdapter adapterDevices;

    // Search views
    private EditText mSearchEt;
    private boolean mSearchOpened;
    private MenuItem mSearchAction;

    // Main layouts
    private ListView lvDevices;
    private FloatingActionButton fabAddDevice;
    private Snackbar sbError;
    private View layoutMultiStateView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    protected void initUI(Bundle savedInstanceState) {
        setContentView(R.layout.act_main);

        vContainer = findViewById(R.id.container);
        layoutMultiStateView = findViewById(R.id.layoutMultiStateView);
        layoutMultiStateView.setVisibility(View.GONE);

        lvDevices = (ListView) findViewById(R.id.lvDevices);
        lvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position < lsDevices.size()) {
                    Device selectedDevice = lsDevices.get(position);
                    Toast.makeText(ActMain.this, selectedDevice.dName, Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabAddDevice = (FloatingActionButton) findViewById(R.id.myFAB);
        fabAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActMain.this, ActDevice.class);
                ActMain.this.startActivityForResult(intent, Const.REQCODE.ADD_DEVICE);
            }
        });

        super.initUI(savedInstanceState);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        adapterDevices = new DeviceAdapter(this);
        lvDevices.setAdapter(adapterDevices);

        lsDevices = DBHelper.getInstance().getAllDevices();
        adapterDevices.setData(lsDevices);
        adapterDevices.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Const.REQCODE.ADD_DEVICE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Device device = data.getParcelableExtra("device");
                        if (device != null) {
                            lsDevices.add(device);
                            adapterDevices.setData(lsDevices);
                            adapterDevices.notifyDataSetChanged();
                            lvDevices.smoothScrollToPosition(lsDevices.size() - 1);

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
                }
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
        int id = item.getItemId();
        if (id == R.id.action_search) {
            if (mSearchOpened) {
                closeSearchBar();
            } else {
                openSearchBar();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSearchBar() {
        fabAddDevice.setVisibility(View.GONE);

        // Set custom view on action bar.
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.actionbar_search_view);
        }

        // Search edit text field setup.
        mSearchEt = (EditText) actionBar.getCustomView().findViewById(R.id.etSearch);
        mSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                adapterDevices.getFilter().filter(s);
            }
        });

        // Show keyboard right away
        KeyboardUtils.showKeyboard(mSearchEt.getContext(), mSearchEt);

        // Change search icon accordingly.
        mSearchOpened = true;
        mSearchAction.setIcon(R.drawable.abc_ic_clear_mtrl_alpha);
    }

    private void closeSearchBar() {
        fabAddDevice.setVisibility(View.VISIBLE);

        // Remove custom view.
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(false);
        }

        // Hide keyboard
        KeyboardUtils.hideKeyboard(mSearchEt.getContext(), mSearchEt);

        // Reset adapter filter to display full datalist
        adapterDevices.getFilter().filter("");

        // Change search icon accordingly.
        mSearchAction.setIcon(R.drawable.abc_ic_search_api_mtrl_alpha);
        mSearchOpened = false;
    }
}
