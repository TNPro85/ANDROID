package com.zing.demo;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.LinearLayout;

/**
 * Here is a great website to find gradient combinations: https://uigradients.com
 */

public class ActGradientAnimation extends ActBase {

    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_gradient_anim);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.root);

        animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(2500);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(animationDrawable != null)
            animationDrawable.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(animationDrawable != null)
            animationDrawable.stop();
    }
}
