package com.tnpro85.cathay.uicontrols;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tnpro85.cathay.R;

public class LayoutLoading extends LinearLayout {

    protected ProgressBar pbLoading;
    protected TextView tvLoading;

    public LayoutLoading(Context context) {
        super(context);
        initLayout();
    }

    public LayoutLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    private void initLayout() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_loading_content, this);
        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        tvLoading = (TextView) findViewById(R.id.tvLoading);
    }

    public void setLoadingText(String text) {
        if(tvLoading != null) {
            tvLoading.setText(text);
        }
    }

    public void show() {
        this.setVisibility(VISIBLE);
    }

    public void hide() {
        this.setVisibility(GONE);
    }
}
