package com.zing.demo;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

public class ActJNI extends ActBase {

    static {
        try {
            System.loadLibrary("tnprocore");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_jni);

        String sk = null;
        try {
            sk = getKey();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!TextUtils.isEmpty(sk))
            Toast.makeText(this, sk, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }

    private native String getKey();
}
