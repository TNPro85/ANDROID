package com.zing.demo.uicontrol;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zing.demo.R;

/**
 * Created by CPU10819-local on 26/03/2015.
 */
public class ListItemLayout extends LinearLayout {

    TextView tvText;
    ImageView imvAvatar;

    public ListItemLayout(Context context) {
        super(context);
    }

    public ListItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvText = (TextView) findViewById(R.id.tvText);
        imvAvatar = (ImageView) findViewById(R.id.imvAvatar);
    }
}
