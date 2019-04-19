package com.example.myapplication.Timer;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.myapplication.R;

import java.util.concurrent.TimeUnit;

public class TimerView extends View {

    private static final int ARC_START_ANGLE = 270; // 12 o'clock

    private Bitmap bitmap;
    private Canvas canvas;

    private RectF rect;
    private Rect textRect;

    private Paint circlePaint;
    private Paint textPaint;

    private float angle;
    private String text;

    private ValueAnimator animationTimer;

    public TimerView(Context context) {
        this(context, null);
    }

    public TimerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        text = "";

        int textColor = ContextCompat.getColor(context, R.color.colorText);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.RED);

        textRect = new Rect();
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(128);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.TRANSPARENT);
            canvas = new Canvas(bitmap);
        }
        super.onSizeChanged(w, h, oldw, oldh);

        rect = new RectF(0, 0, w, h);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        if (angle > 0f) {
            this.canvas.drawArc(rect, ARC_START_ANGLE, angle, true, circlePaint);
        }

        textPaint.getTextBounds(text, 0, text.length(), textRect);
        int yOffset = textRect.height() / 2;

        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawText(text, rect.centerX(), rect.centerY() + yOffset, textPaint);
    }

    public void start(int secs) {
        stop();

        animationTimer = ValueAnimator.ofFloat(0f, 1f);
        animationTimer.setDuration(TimeUnit.SECONDS.toMillis(secs));
        animationTimer.setInterpolator(new LinearInterpolator());
        animationTimer.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setProgress((float) animation.getAnimatedValue());
            }
        });
        animationTimer.start();
    }

    public void stop() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.cancel();
            animationTimer = null;
        }
        setProgress(0f);
    }

    public void setProgress(float progress) {
        angle = 360 * progress;

        invalidate();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public int getColor() {
        return circlePaint.getColor();
    }

    public void setColor(int color) {
        circlePaint.setColor(color);
        invalidate();
    }


}
