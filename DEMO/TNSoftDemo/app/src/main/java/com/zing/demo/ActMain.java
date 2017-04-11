package com.zing.demo;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.zing.demo.utils.KeyboardUtils;

import java.util.Arrays;

public class ActMain extends ActionBarActivity {

    private ListView lvDemoCategory;
    private EditText mSearchEt;

    private String []s;
    private ArrayAdapter<String> mDemoCategoryAdapter;

    private MenuItem mSearchAction;

    private boolean mSearchOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle data = getIntent().getExtras();
        if(data != null) {
            boolean forceClose = data.getBoolean("forceClose");
            if(forceClose) {
                supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                super.onCreate(savedInstanceState);
                getSupportActionBar().hide();
                setContentView(R.layout.act_lock);
                DevicePolicyManager deviceManger = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
                ComponentName compName = new ComponentName(this, ActLockScreen.MyAdmin.class);
                if (deviceManger.isAdminActive(compName)) {
                    deviceManger.lockNow();
                    finish();

                    return;
                }
            }
            else
                super.onCreate(savedInstanceState);
        }
        else
            super.onCreate(savedInstanceState);


        setContentView(R.layout.act_main);
        setTitle("TNSoft - Demo");

        s = new String[] {
                "Unique Id",
                "Subsequence Emoji",
                "File Observer",
                "Convert number to K,M,B",
                "Draw on View",
                "Split string and combine",
                "Folder last access",
                "Custom View Group",
                "AES128 Base64",
                "NDK JNI",
                "Lock Screen",
                "Device ID",
                "Detect Wifi/Gsm strength",
                "Circle Animation",
                "Gradient Animation"
        };
        Arrays.sort(s);

        mDemoCategoryAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, s);
        lvDemoCategory = (ListView)findViewById(R.id.lvDemoCategory);
        lvDemoCategory.setAdapter(mDemoCategoryAdapter);
        lvDemoCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (mDemoCategoryAdapter.getItem(position)) {
                    case "Unique Id":
                        intent = new Intent(ActMain.this, ActUniqueId.class);
                        break;
                    case "Subsequence Emoji":
                        intent = new Intent(ActMain.this, ActSubsequenceEmoji.class);
                        break;
                    case "File Observer":
                        intent = new Intent(ActMain.this, ActFileObserver.class);
                        break;
                    case "Convert number to K,M,B":
                        intent = new Intent(ActMain.this, ActConvertNumToKMB.class);
                        break;
                    case "Draw on View":
                        intent = new Intent(ActMain.this, ActDrawOnView.class);
                        break;
                    case "Split string and combine":
                        intent = new Intent(ActMain.this, ActSplitString.class);
                        break;
                    case "Folder last access":
                        intent = new Intent(ActMain.this, ActFolderAttribute.class);
                        break;
                    case "Custom View Group":
                        intent = new Intent(ActMain.this, ActCustomViewGroup.class);
                        break;
                    case "AES128 Base64":
                        intent = new Intent(ActMain.this, ActAES128.class);
                        break;
                    case "NDK JNI":
                        intent = new Intent(ActMain.this, ActJNI.class);
                        break;
                    case "Lock Screen":
                        intent = new Intent(ActMain.this, ActLockScreen.class);
                        break;
                    case "Device ID":
                        intent = new Intent(ActMain.this, ActDeviceID.class);
                        break;
                    case "Detect Wifi/Gsm strength":
                        intent = new Intent(ActMain.this, ActNetworkStrength.class);
                        break;
                    case "Circle Animation":
                        intent = new Intent(ActMain.this, ActCircleAnimation.class);
                        break;
                    case "Gradient Animation":
                        intent = new Intent(ActMain.this, ActGradientAnimation.class);
                        break;
                }

                if(intent != null)
                    ActMain.this.startActivity(intent);
            }
        });
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mDemoCategoryAdapter.getFilter().filter(s);
            }
        });

        // Show keyboard right away
        KeyboardUtils.showKeyboard(mSearchEt.getContext(), mSearchEt);

        // Change search icon accordingly.
        mSearchOpened = true;
        mSearchAction.setIcon(R.drawable.abc_ic_clear_mtrl_alpha);
    }

    private void closeSearchBar() {

        // Remove custom view.
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(false);
        }

        // Hide keyboard
        KeyboardUtils.hideKeyboard(mSearchEt.getContext(), mSearchEt);

        // Reset adapter filter to display full datalist
        mDemoCategoryAdapter.getFilter().filter("");

        // Change search icon accordingly.
        mSearchAction.setIcon(R.drawable.abc_ic_search_api_mtrl_alpha);
        mSearchOpened = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(mSearchOpened)
                closeSearchBar();
            else
                finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
