package com.tnpro85.mytvchannels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public abstract class ActBase extends AppCompatActivity {

    protected View vContainer;
    protected ActionBar mActionBar;

    protected ProgressDialog mLoadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setTitle(R.string.str_loading);

        mActionBar = getSupportActionBar();
        if(mActionBar != null) {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
                mActionBar.setElevation(0);
            mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_color)));
        }

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

    protected void showLoadingDlg(int textId) {
        if(!mLoadingDialog.isShowing()) {
            mLoadingDialog.setTitle(textId);
            mLoadingDialog.show();
        }
    }

    protected void hideLoadingDlg() {
        if(mLoadingDialog.isShowing())
            mLoadingDialog.hide();
    }
}
