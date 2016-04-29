package com.tnpro85.mytvchannels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

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
                overridePendingTransition(R.anim.transition_right_to_left, R.anim.transition_right_to_left_out);
                finish();
            }
        }, 800);
    }
}
