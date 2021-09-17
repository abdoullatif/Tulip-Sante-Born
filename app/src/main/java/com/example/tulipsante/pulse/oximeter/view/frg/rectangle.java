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

public class rectangle extends View {
    private Path path;
    private Paint paint;




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


    public rectangle(Context context, AttributeSet attrs) {
        super(context, attrs);
        path = new Path();
        paint = new Paint();
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
  /*      paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);


        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(30, 30, 80, 80, paint);
    */

        float padding = 1;
        float size = getWidth() < getHeight() ? getWidth() : getHeight();
        float width = (size - (2 * padding)) + 45;
        float height = size - (2 * padding) - 4;
//        float radius = (width > height ? width/2 : height/2);
        float radius = ((width < height ? width / 2 : height / 2) - 7);


        float rectLeft = ((getWidth()) - (2 * padding)) / 2 - radius + padding;
        float rectTop = ((getHeight()) - (2 * padding)) / 2 - radius + padding;
        float rectRight = ((getWidth()) - (2 * padding)) / 2 - radius + padding + width;
        float rectBottom = ((getHeight()) - (2 * padding)) / 2 - radius + padding + height;
        final RectF oval = new RectF();
        oval.set(rectLeft, rectTop, rectRight, rectBottom);


        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10);


        // Draw circle
        paint.setColor(Color.parseColor("#427B99"));


        paint.setShader(null);
        canvas.drawRect(oval, paint);

        float x = canvas.getWidth() / 2;
        float y = canvas.getHeight() / 2;

        float angle = 0;
        float l = 1.2f;
        float a = angle * (float) Math.PI / 180;
        radius += 100;
        y += -1;
        x += -1;
        // Draw arrow

     if(mSweepAngle==50) {
         paint.setStyle(Paint.Style.STROKE);
         paint.setStrokeWidth(40);
         paint.setColor(Color.WHITE);
         Path path = new Path();
         path.moveTo(120, 4);
         path.lineTo(223, 4);
         path.lineTo(223, 4);

         path.close();
         path.offset(10, 40);
         canvas.drawPath(path, paint);
         path.offset(50, 100);
         canvas.drawPath(path, paint);
     }
// offset is cumlative
// next draw displaces 50,100 from previous

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
        invalidate();

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

