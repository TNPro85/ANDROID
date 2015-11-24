package com.zing.demo.uicontrol;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by CPU10819-local on 24/11/2015.
 */
public class GradientView extends RelativeLayout {

    public GradientView(Context context) {
        super(context);
        init();
    }

    public GradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, getHeight(), 0x000000FF, 0x600000FF, Shader.TileMode.CLAMP));
//        canvas.drawRect(new RectF(0, 0, getWidth(), getHeight()), p);
        canvas.drawPaint(p);
    }
}
