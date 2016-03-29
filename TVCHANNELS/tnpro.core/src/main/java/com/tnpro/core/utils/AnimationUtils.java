package com.tnpro.core.utils;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;

public class AnimationUtils {
    public static void stop(final View v) {
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                Animation a = v.getAnimation();
                if (a != null) {
                    a.setAnimationListener(null);
                    a.cancel();
                }
                v.setAnimation(null);
            }
        });
    }


    public static void goneViewWithAnim(final View v, final int animationResId) {
        if(v.getVisibility() == View.GONE) return;
        v.setVisibility(View.GONE);
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                Animation a = android.view.animation.AnimationUtils.loadAnimation(v.getContext(), animationResId);
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        v.setVisibility(View.GONE);
                        stop(v);
                    }
                });
                v.startAnimation(a);
            }
        });
    }

    public static void showViewWithAnim(final View v, final int animationResId) {
        if(v.getVisibility() == View.VISIBLE) return;
        v.setVisibility(View.VISIBLE);
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                Animation a = android.view.animation.AnimationUtils.loadAnimation(v.getContext(), animationResId);
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        stop(v);
                    }
                });
                v.startAnimation(a);
            }
        });
    }
}
