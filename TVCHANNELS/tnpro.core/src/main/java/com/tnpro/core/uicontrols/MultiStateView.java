package com.tnpro.core.uicontrols;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tnpro.core.R;

public class MultiStateView extends FrameLayout {
    public static final int STATE_GONE = 0, STATE_LOADING = 1, STATE_EMPTY = 2, STATE_ERROR = 3;
    public int mState;

    private LayoutInflater mLayoutInflater;
    private View container, layoutLoading, layoutEmpty;
    private TextView tvEmptyText;

    public MultiStateView(Context context) {
        super(context);
        init(context);
    }

    public MultiStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutInflater.inflate(R.layout.multi_stateview, this);

        container = findViewById(R.id.layoutMultiStateView);
        layoutLoading = findViewById(R.id.layoutLoading);
        layoutEmpty = findViewById(R.id.layoutEmpty);

        tvEmptyText = (TextView) findViewById(R.id.tvEmptyText);
    }

    public void show(int state) {
        mState = state;

        if (container != null)
            container.setVisibility(View.VISIBLE);

        switch (mState) {
            case STATE_LOADING:
                layoutEmpty.setVisibility(GONE);
                layoutLoading.setVisibility(VISIBLE);
                break;
            case STATE_EMPTY:
            case STATE_ERROR:
                layoutLoading.setVisibility(GONE);
                layoutEmpty.setVisibility(VISIBLE);
                break;
        }
    }

    public void hide() {
        mState = STATE_GONE;
        if (container != null)
            container.setVisibility(View.GONE);
    }

    public void setEmptyText(String emptyText) {
        if (tvEmptyText != null)
            tvEmptyText.setText(emptyText);
    }
}
