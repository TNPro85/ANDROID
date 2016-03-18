package com.tnpro85.mytvchannels.uicontrols;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import com.tnpro85.mytvchannels.R;

public class LoadingDialog extends Dialog {

    private TextView tvMessage;

    public LoadingDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setContentView(R.layout.dialog_loading);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
    }

    public void setMessage(CharSequence message) {
        if(tvMessage != null)
            tvMessage.setText(message);
    }
}
