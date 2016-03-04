package com.tnpro85.mytvchannels;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class ActBase extends AppCompatActivity {

    protected View vContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
                actionBar.setElevation(0);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_color)));
        }
    }

    protected void initUI(Bundle savedInstanceState) {}
    protected void initData(Bundle savedInstanceState) {}

    @Override
    public void finish() {
        super.finish();
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
}
