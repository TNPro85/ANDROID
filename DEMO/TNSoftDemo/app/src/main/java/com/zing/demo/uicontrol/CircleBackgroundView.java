package com.zing.demo.uicontrol;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by tuannh2 on 3/24/2017.
 */
public class CircleBackgroundView extends View {

    public static final float RANGE_RADIUS = 1.5f;
    public static final int NUMBER_PAINTS = 3;
    private float mDiameter = -1.0f;
    private float mXCenterCir = -1.0f, mYCenterCir = -1.0f;
    private Paint mCircle45Paint, mCircle30Paint, mCircle15Paint;
    private boolean mFirstSetup = false;
    private ObjectAnimator mDrawAnimator;
    private float animationProgress;
    private boolean isRunningAnimation = false;
    private float maxRadius = 0.0f;

    public CircleBackgroundView(Context context) {
        super(context);
        init();
    }

    public CircleBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mXCenterCir < 0f) {
            mXCenterCir = getWidth() / 2f;
        }

        if (mYCenterCir < 0f) {
            mYCenterCir = getHeight() / 2f;
        }

        if (mDiameter < 0f || mXCenterCir < 0f || mYCenterCir < 0f) {
            return;
        }

        if (!mFirstSetup) {
            mFirstSetup = true;
            setup();
        }

        if (!isRunningAnimation) {
            resetPaint();
            canvas.drawCircle(mXCenterCir, mYCenterCir, getRadius() * 4.0f, mCircle15Paint);
            canvas.drawCircle(mXCenterCir, mYCenterCir, getRadius() * 2.7f, mCircle30Paint);
            canvas.drawCircle(mXCenterCir, mYCenterCir, getRadius() * 1.7f, mCircle45Paint);
        } else {
            drawAnimation(canvas, getRadius() + animationProgress, 0);
        }
    }

    private void drawAnimation(Canvas c, float radius, int pos) {
        if (radius < getRadius()) {
            return;
        }
        if (pos < 0 || pos >= NUMBER_PAINTS) {
            pos = 0;
        }

        resetPaint();

        float range = (mXCenterCir > 0 ? mXCenterCir : -mXCenterCir) - getRadius();
        float radius45 = range / 3.0f + getRadius();
        float maxRange = range + 1.5f * getRadius();
        float deltaRange =  maxRange - radius45;
        int alpha = 45;
        if (radius > radius45) {
            alpha = Math.round((radius - radius45) / (deltaRange / 45.0f));
            alpha = 45 - alpha;
        }
        mCircle45Paint.setAlpha(alpha);

        if (alpha >= 0) {
            c.drawCircle(mXCenterCir, mYCenterCir, radius, mCircle45Paint);
        }

        if (pos + 1 < NUMBER_PAINTS) {
            drawAnimation(c, radius - getRadius() * RANGE_RADIUS, (pos + 1) % NUMBER_PAINTS);
        }
    }

    private void resetPaint() {
        mCircle45Paint.setAlpha(45);
        mCircle30Paint.setAlpha(30);
        mCircle15Paint.setAlpha(15);
    }

    private void setup() {
        float width = mXCenterCir > 0 ? mXCenterCir : -mXCenterCir;
        float height = mYCenterCir > 0 ? mYCenterCir : -mYCenterCir;
        maxRadius = Math.max(width, height);
        float range = maxRadius + (RANGE_RADIUS * 2 - 0.5f) * getRadius();
        if (range < 0.0f) {
            range = 50.0f;
        }
        mDrawAnimator.setFloatValues(0.0f, range);
    }

    private float getRadius() {
        return getDiameter() / 2f;
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }

    public void init() {
        mFirstSetup = false;
        isRunningAnimation = false;

        mCircle45Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircle45Paint.setStyle(Paint.Style.FILL);
        mCircle45Paint.setColor(Color.WHITE);
        mCircle45Paint.setAlpha(65);

        mCircle15Paint = new Paint(this.mCircle45Paint);
        mCircle15Paint.setAlpha(25);

        mCircle30Paint = new Paint(this.mCircle45Paint);
        mCircle30Paint.setAlpha(40);

        mDrawAnimator = ObjectAnimator.ofFloat(this, "animationProgress", 0f, 1.0f);
        mDrawAnimator.setDuration(3500);
//        mDrawAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mDrawAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mDrawAnimator.setRepeatMode(ValueAnimator.RESTART);
    }

    public void startAnimation() {
        if (mDrawAnimator != null && !isRunningAnimation && !mDrawAnimator.isRunning()) {
            isRunningAnimation = true;
            mDrawAnimator.start();
        }
    }

    public void stopAnimation() {
        if (mDrawAnimator != null && mDrawAnimator.isRunning()) {
            isRunningAnimation = false;
            mDrawAnimator.cancel();
            animationProgress = 0.0f;
            this.invalidate();
        }
    }

    public void setCenterCircle(float xValue, float yValue) {
        this.mXCenterCir = xValue;
        this.mYCenterCir = yValue;
    }

    public void setCircleColor(int color) {
        mCircle45Paint.setColor(color);
    }

    public float getDiameter() {
        return mDiameter;
    }

    public void setDiameter(float diameter) {
        this.mDiameter = diameter;
    }

    public float getAnimationProgress() {
        return animationProgress;
    }

    public void setAnimationProgress(float animationProgress) {
        this.animationProgress = animationProgress;
        this.invalidate();
    }
}
