package com.tnpro85.mytvchannels.uicontrols;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.tnpro.core.utils.ViewUtils;
import com.tnpro85.mytvchannels.R;

public class DeviceIcon extends View {

    private int mBgColor;
    private int mLineColor;
    private Paint mPaint;
    private Rect mRect;
    private RectF mRectF;
    private String mText = "TV";
    private Typeface mTypeface;

    private int mRound = 0, mRoundSmall = 0;
    private int mPadding = 0;
    private int mStrokeWidth = 2;
    private int mTextSize;

    public DeviceIcon(Context context) {
        super(context);
        init(context);
    }

    public DeviceIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mBgColor = context.getResources().getColor(R.color.main_color);
        mLineColor = context.getResources().getColor(R.color.white);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRect = new Rect();
        mRectF = new RectF();

        // Lines
        mRound = ViewUtils.dpToPx(context, 5);
        mRoundSmall = ViewUtils.dpToPx(context, 3);
        mPadding = ViewUtils.dpToPx(context, 7);
        mStrokeWidth = ViewUtils.dpToPx(context, 2);

        // Text
        mTypeface = Typeface.DEFAULT;
        mTextSize = ViewUtils.spToPx(context, 18);
        mPaint.setTypeface(mTypeface);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mTextSize);
    }

    public void setText(String text) {
        mText = text;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getMeasuredWidth(), h = getMeasuredHeight();
        mRectF.set(0, 0, w, h);
        mPaint.setColor(mBgColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(mRectF, mRound, mRound, mPaint);

        mRectF.set(mPadding, mPadding, w - mPadding, h - (1.5f * mPadding));
        mPaint.setColor(mLineColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        canvas.drawRoundRect(mRectF, mRoundSmall, mRoundSmall, mPaint);

        canvas.drawLine(mPadding * 2, h - mPadding, w - (2 * mPadding), h - mPadding, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.getTextBounds(mText, 0, mText.length(), mRect);
        int x = w/2;
        int y = (int)(h/2 + (mRect.height() / 2.5));
        canvas.drawText(mText, x, y, mPaint);
    }
}
