package com.zing.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.zing.demo.uicontrol.CircleBackgroundView;
import com.zing.demo.utils.ViewUtils;

public class ActCircleAnimation extends ActBase {

    private View root;
    private CircleBackgroundView cbv;
    private boolean dark = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_circle_animation);
        setTitle("Circle animation");

        root = findViewById(R.id.root);

        cbv = (CircleBackgroundView) findViewById(R.id.cbvCircle);
        cbv.setDiameter(ViewUtils.dpToPx(this, 100f));
        cbv.setCircleColor(Color.parseColor("#8947AD"));
        cbv.setParentView(findViewById(R.id.imvCircle));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cbv.startAnimation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cbv.stopAnimation();
    }

    public void onCircleClick(View v) {
        if(!dark) {
            root.setBackgroundColor(Color.BLACK);
            cbv.setCircleColor(Color.WHITE);
            cbv.invalidate();
        }
        else {
            root.setBackgroundColor(Color.WHITE);
            cbv.setCircleColor(Color.parseColor("#8947AD"));
            cbv.invalidate();
        }

        dark = !dark;
    }
}
