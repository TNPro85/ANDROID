package com.zing.demo.uicontrol;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by CPU10819-local on 13/07/2015.
 */
public class DrawImage extends ImageView {

    public float mStrokeSize = 1.0f;
    public int mPaddingLeft = 4, mPaddingRight = 4;

    public int mStrokeColor, mMainColor;
    private float mDensity;

    public DrawImage(Context context) {
        super(context);
        init();
    }

    public DrawImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDensity = getResources().getDisplayMetrics().density;
        mStrokeSize = 1.0f * mDensity;
        mPaddingLeft = (int)(4.0f * mDensity);
        mStrokeColor = Color.parseColor("#b3e4fd");
        mMainColor = Color.parseColor("#68c9fc");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int center = w / 2;

        int strokeRadius = w / 2;
        int bgRadius = strokeRadius - 1;
        int mainRadius = strokeRadius - mPaddingLeft;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setXfermode(null);
        paint.setColor(mStrokeColor);
        canvas.drawCircle(center, center, strokeRadius, paint);

        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawCircle(center, center, bgRadius, paint);

        paint.setColor(mMainColor);
        canvas.drawCircle(center, center, mainRadius, paint);

        int x = (int)Math.sqrt((mainRadius * mainRadius)/2);

        // scale here
        Drawable localDrawable = getDrawable();
        if (localDrawable != null) {
//            localDrawable.setBounds(left, left, bottom, bottom);
//            localDrawable.draw(canvas);

//            Bitmap drawableBitmap = Bitmap.createBitmap(localDrawable.getIntrinsicWidth(),
//                    localDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//            Canvas localCanvas = new Canvas(drawableBitmap);
//            localDrawable.setBounds(0, 0, getWidth(), getHeight());
//            localDrawable.draw(localCanvas);

            if(localDrawable instanceof BitmapDrawable) {
                Bitmap drawableBitmap = ((BitmapDrawable)localDrawable).getBitmap();
                int sourceW = drawableBitmap.getWidth(), sourceH = drawableBitmap.getHeight();
                int newW = (x * 2) - (int)(2 * mDensity);

                float xScale = (float) newW / sourceW;
                float yScale = (float) newW / sourceH;
                float scale = Math.max(xScale, yScale);

                float scaleW = scale * sourceW;
                float scaleH = scale * sourceH;

                float l = center - scaleW/2, t = center - scaleH/2;

                drawableBitmap = scaleToFill(drawableBitmap, (int)scaleW, (int)scaleH);
                RectF rectF = new RectF(l, t, l + scaleW, t + scaleH);

                paint.setColor(Color.RED);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(rectF, paint);

                canvas.drawBitmap(drawableBitmap, null, rectF, null);
            }
        }
    }

    private Bitmap scaleToFill(Bitmap b, int width, int height) {
        float factorH = height / (float) b.getWidth();
        float factorW = width / (float) b.getWidth();
        float factorToUse = (factorH > factorW) ? factorW : factorH;
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factorToUse), (int) (b.getHeight() * factorToUse), true);
    }
}
