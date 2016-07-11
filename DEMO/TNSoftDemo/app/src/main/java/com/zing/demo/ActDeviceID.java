package com.zing.demo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.zing.demo.utils.ToastUtils;

public class ActDeviceID extends ActBase {

    TextView tvDeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_deviceid);

        final String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String resultText = "Device ID/Android ID: " + android_id + "\nHashcode: " + android_id.hashCode();
        tvDeviceId = (TextView) findViewById(R.id.tvDeviceId);
        tvDeviceId.setText(resultText);
        findViewById(R.id.btnCopy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("deviceId", android_id);
                clipboard.setPrimaryClip(clip);
                ToastUtils.showToast(v.getContext(), "Copied");
            }
        });
    }
}
