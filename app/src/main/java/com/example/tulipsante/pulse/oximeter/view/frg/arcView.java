package com.example.tulipsante.pulse.oximeter.view.frg;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.Shader;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

public class arcView extends View {
    private Path path;
    private Paint paint;
    private static final int DEFAULT_LONG_POINTER_SIZE = 1;

    private Paint mPaint;
    private Paint paint2;
    private float mStrokeWidth;
    private int mStrokeColor;
    private RectF mRect;
    private RectF mRect2;
    private String mStrokeCap;
    private int mStartAngle;
    private int mSweepAngle;
    private int mStartValue;
    private int mEndValue;
    private int mValue;
    private double mPointAngle;
    private int mPoint;
    private int mPointSize;
    private int mPointStartColor;
    private int mPointEndColor;
    private int mDividerColor;
    private int mDividerSize;
    private int mDividerStepAngle;
    private int mDividersCount;
    private boolean mDividerDrawFirst;
    private boolean mDividerDrawLast;
    public arcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        path = new Path();
        paint = new Paint();
        paint2 = new Paint();

        mPaint = new Paint();

        TypedArray a = context.obtainStyledAttributes(attrs, pl.pawelkleczkowski.customgauge.R.styleable.CustomGauge, 0, 0);

        // stroke style
        setStrokeWidth(a.getDimension(pl.pawelkleczkowski.customgauge.R.styleable.CustomGauge_gaugeStrokeWidth, 10));
        setStrokeColor(a.getColor(pl.pawelkleczkowski.customgauge.R.styleable.CustomGauge_gaugeStrokeColor, ContextCompat.getColor(context, android.R.color.darker_gray)));
        setStrokeCap(a.getString(pl.pawelkleczkowski.customgauge.R.styleable.CustomGauge_gaugeStrokeCap));

        // angle start and sweep (opposite direction 0, 270, 180, 90)
        setStartAngle(a.getInt(pl.pawelkleczkowski.customgauge.R.styleable.CustomGauge_gaugeStartAngle, 0));
        setSweepAngle(a.getInt(pl.pawelkleczkowski.customgauge.R.styleable.CustomGauge_gaugeSweepAngle, 360));

        // scale (from mStartValue to mEndValue)
        setStartValue(a.getInt(pl.pawelkleczkowski.customgauge.R.styleable.CustomGauge_gaugeStartValue, 0));
        setEndValue(a.getInt(pl.pawelkleczkowski.customgauge.R.styleable.CustomGauge_gaugeEndValue, 1000));

        // pointer size and color
        setPointSize(a.getInt(pl.pawelkleczkowski.customgauge.R.styleable.CustomGauge_gaugePointSize, 0));
        setPointStartColor(a.getColor(pl.pawelkleczkowski.customgauge.R.styleable.CustomGauge_gaugePointStartColor, ContextCompat.getColor(context, android.R.color.white)));
        setPointEndColor(a.getColor(pl.pawelkleczkowski.customgauge.R.styleable.CustomGauge_gaugePointEndColor, ContextCompat.getColor(context, android.R.color.white)));

        // divider options
        int dividerSize = a.getInt(pl.pawelkleczkowski.customgauge.R.styleable.CustomGauge_gaugeDividerSize, 0);
        setDividerColor(a.getColor(pl.pawelkleczkowski.customgauge.R.styleable.CustomGauge_gaugeDividerColor, ContextCompat.getColor(context, android.R.color.white)));
        int dividerStep = a.getInt(pl.pawelkleczkowski.customgauge.R.styleable.CustomGauge_gaugeDividerStep, 0);
        setDividerDrawFirst(a.getBoolean(pl.pawelkleczkowski.customgauge.R.styleable.CustomGauge_gaugeDividerDrawFirst, true));
        setDividerDrawLast(a.getBoolean(pl.pawelkleczkowski.customgauge.R.styleable.CustomGauge_gaugeDividerDrawLast, true));

        // calculating one point sweep

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        float padding = getStrokeWidth();
        float size = getWidth()<getHeight() ? getWidth() : getHeight();
        float width = size - (2*padding);
        float height = size - (2*padding);
//        float radius = (width > height ? width/2 : height/2);
        float radius = ((width < height ? width/2 : height/2)-7);



        float rectLeft = ((getWidth()) - (2*padding))/2 - radius + padding;
        float rectTop = ((getHeight()) - (2*padding))/2 - radius + padding;
        float rectRight = ((getWidth()) - (2*padding))/2 - radius + padding + width;
        float rectBottom = ((getHeight()) - (2*padding))/2 - radius + padding + height;
        final RectF oval = new RectF();
        oval.set(rectLeft, rectTop, rectRight, rectBottom);



        float x = canvas.getWidth()/2;
        float y = canvas.getHeight()/2;

         radius = ((width < height ? ((width/2)-5) : ((height/2)-7)-5));
        paint.setStyle(Paint.Style.STROKE);
       paint.setStrokeWidth(50);


        // Draw circle
        paint.setColor(Color.rgb(182,223,250));


        paint.setShader(new LinearGradient(getWidth(), getHeight()+30, 0, 0, Color.rgb(182,223,250),Color.rgb(182,223,250),Shader.TileMode.CLAMP));
        canvas.drawArc(oval, 135, 200, false, paint);
        paint.setColor(Color.rgb(182,223,250));
        paint.setShader(new LinearGradient(getWidth(), getHeight(), 0, 0, Color.rgb(182,223,250),Color.rgb(182,223,250) ,Shader.TileMode.CLAMP));
        canvas.drawArc(oval, 335, 16, false, paint);
        paint.setColor(Color.rgb(79,162,221));
        paint.setShader(new LinearGradient(getWidth()+30, getHeight()+30, 0, 0,Color.rgb(79,162,221), Color.rgb(79,162,221),Shader.TileMode.CLAMP));
        canvas.drawArc(oval, 351, 54, false, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        mPaint.setColor(Color.WHITE);

        path.reset();


        float angle = (((mSweepAngle-70)*270)/30)+135;
        float l = 1.2f;
        float a = angle*(float)Math.PI/180;
        // Draw arrow
        path.moveTo(x+ (float)Math.cos(a) *radius, y + (float)Math.sin(a) * radius);
        path.lineTo(x+ (float)Math.cos(a+0.1) *radius*l, y + (float)Math.sin(a+0.1) * radius*l);
        path.lineTo(x+ (float)Math.cos(a-0.1) *radius*l, y + (float)Math.sin(a-0.1) * radius*l);
        path.lineTo(x+ (float)Math.cos(a) *radius, y + (float)Math.sin(a) * radius);


        canvas.drawPath(path, mPaint);

    }

      public void setValue(int value) {
        mValue = value;
        mPoint = (int) (mStartAngle + (mValue-mStartValue) * mPointAngle);

    }

    public int getValue() {
        return mValue;
    }

    @SuppressWarnings("unused")
    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        mStrokeWidth = strokeWidth;
    }

    @SuppressWarnings("unused")
    public int getStrokeColor() {
        return mStrokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        mStrokeColor = strokeColor;
    }

    @SuppressWarnings("unused")
    public String getStrokeCap() {
        return mStrokeCap;
    }

    public void setStrokeCap(String strokeCap) {
        mStrokeCap = strokeCap;
        if(mPaint != null) {
            if (mStrokeCap.equals("BUTT")) {
                mPaint.setStrokeCap(Paint.Cap.BUTT);
            } else if (mStrokeCap.equals("ROUND")) {
                mPaint.setStrokeCap(Paint.Cap.ROUND);
            }
        }
    }

    @SuppressWarnings("unused")
    public int getStartAngle() {
        return mStartAngle;
    }

    public void setStartAngle(int startAngle) {
        mStartAngle = startAngle;
    }

    @SuppressWarnings("unused")
    public int getSweepAngle() {
        return mSweepAngle;
    }

    public void setSweepAngle(int sweepAngle) {

        mSweepAngle = sweepAngle;
        postInvalidate();

    }

    @SuppressWarnings("unused")
    public int getStartValue() {
        return mStartValue;
    }

    public void setStartValue(int startValue) {
        mStartValue = startValue;
    }

    @SuppressWarnings("unused")
    public int getEndValue() {
        return mEndValue;
    }

    public void setEndValue(int endValue) {
        mEndValue = endValue;
        mPointAngle = ((double) Math.abs(mSweepAngle) / (mEndValue - mStartValue));
        invalidate();
    }

    @SuppressWarnings("unused")
    public int getPointSize() {
        return mPointSize;
    }

    public void setPointSize(int pointSize) {
        mPointSize = pointSize;
    }

    @SuppressWarnings("unused")
    public int getPointStartColor() {
        return mPointStartColor;
    }

    public void setPointStartColor(int pointStartColor) {
        mPointStartColor = pointStartColor;
    }

    @SuppressWarnings("unused")
    public int getPointEndColor() {
        return mPointEndColor;
    }

    public void setPointEndColor(int pointEndColor) {
        mPointEndColor = pointEndColor;
    }

    @SuppressWarnings("unused")
    public int getDividerColor() {
        return mDividerColor;
    }

    public void setDividerColor(int dividerColor) {
        mDividerColor = dividerColor;
    }

    @SuppressWarnings("unused")
    public boolean isDividerDrawFirst() {
        return mDividerDrawFirst;
    }

    public void setDividerDrawFirst(boolean dividerDrawFirst) {
        mDividerDrawFirst = dividerDrawFirst;
    }

    @SuppressWarnings("unused")
    public boolean isDividerDrawLast() {
        return mDividerDrawLast;
    }

    public void setDividerDrawLast(boolean dividerDrawLast) {
        mDividerDrawLast = dividerDrawLast;
    }

}

