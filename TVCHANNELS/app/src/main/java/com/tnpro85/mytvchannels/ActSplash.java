package com.tnpro85.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.Window;
import android.widget.TextView;

import com.tnpro85.android.models.Device;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class ActSplash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_splash);

        String version = getString(R.string.app_copyright)
                + "\n"
                + String.format(getString(R.string.app_version), BuildConfig.VERSION_NAME);
        ((TextView)findViewById(R.id.tvVersion)).setText(version);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ActSplash.this, ActMain.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
