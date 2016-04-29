package com.tnpro85.mytvchannels;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.tnpro85.mytvchannels.uicontrols.LoadingDialog;

public abstract class ActBase extends AppCompatActivity {

    protected View vContainer;
    protected ActionBar mActionBar;

    protected LoadingDialog mLoadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();

        initUI(savedInstanceState);
        initData(savedInstanceState);
    }

    protected void initUI(Bundle savedInstanceState) {}
    protected void initData(Bundle savedInstanceState) {}

    @Override
    public void finish() {
        super.finish();
        if(getClass() != ActMain.class)
            overridePendingTransition(R.anim.transition_left_to_right, R.anim.transition_left_to_right_out);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.transition_right_to_left, R.anim.transition_right_to_left_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.transition_right_to_left, R.anim.transition_right_to_left_out);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showHomeAsUpEnable(boolean value) {
        if(mActionBar != null)
            mActionBar.setDisplayHomeAsUpEnabled(value);
    }

    protected  void setTitle(String value) {
        if(mActionBar != null)
            mActionBar.setTitle(value);
    }

    protected void showLoadingDlg(int textId, boolean cancelable) {
        if(mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(ActBase.this);
        }

        if(!mLoadingDialog.isShowing()) {
            mLoadingDialog.setMessage(getString(textId));
            mLoadingDialog.setCancelable(cancelable);
            mLoadingDialog.show();
        }
    }

    protected void hideLoadingDlg() {
        if(mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    protected void runOnUI(Runnable runnable) {
        if(runnable != null && !isFinishing()) {
            new Handler(Looper.getMainLooper()).post(runnable);
        }
    }
}
