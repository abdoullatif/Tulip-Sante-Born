package com.example.tulipsante.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

class WaveformView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = ">>>WAVEFORM VIEW<<<";

    private boolean isSurfaceViewAvailable;

    private boolean isWaveformVisible = false;

    private Paint mBackgroundPaint;

    private int mBufferSize;

    private Canvas mCanvas;

    private int[] mDataBuffer;

    private int mDataBufferIndex;

    private int mHeight;

    private Point mLastPoint;

    private float mLineWidth;

    private int mMaxValue;

    private SurfaceHolder mSurfaceHolder;

    private Paint mWavePaint;

    private int mWidth;

    private float pointStep;

    public WaveformView(Context paramContext) {
        this(paramContext, (AttributeSet)null);
    }

    public WaveformView(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public WaveformView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        DisplayMetrics displayMetrics = paramContext.getResources().getDisplayMetrics();
//        TypedArray typedArray = paramContext.getTheme().obtainStyledAttributes(paramAttributeSet, R.styleable.WaveformView, paramInt, 0);
//        paramInt = typedArray.getColor( Color.argb(255, 47, 211, 217));
//        this.mLineWidth = typedArray.getDimension(2, TypedValue.applyDimension(1, 1.6F, displayMetrics));
//        this.pointStep = typedArray.getDimension(4, TypedValue.applyDimension(1, 1.0F, displayMetrics));
//        this.mBufferSize = typedArray.getInt(1, 5);
//        this.mMaxValue = typedArray.getInteger(3, 100);
        this.mWavePaint = new Paint();
        this.mWavePaint.setColor(paramInt);
        this.mWavePaint.setStrokeWidth(this.mLineWidth);
        this.mWavePaint.setStyle(Paint.Style.STROKE);
        this.mWavePaint.setStrokeCap(Paint.Cap.ROUND);
        this.mWavePaint.setStrokeJoin(Paint.Join.ROUND);
//        paramInt = typedArray.getColor(0, getResources().getColor(R.color.grey));
        this.mBackgroundPaint = new Paint();
        this.mBackgroundPaint.setColor(paramInt);
        this.mSurfaceHolder = getHolder();
        this.mSurfaceHolder.addCallback(this);
        this.mDataBuffer = new int[this.mBufferSize * 2];
        this.mDataBufferIndex = 0;
        setBackgroundColor(paramInt);
        setZOrderOnTop(true);
        getHolder().setFormat(-3);
    }

    public void addAmp(int paramInt) {
        if (!this.isSurfaceViewAvailable) {
            this.mDataBufferIndex = 0;
            return;
        }
        if (!this.isWaveformVisible)
            return;
        Point point = this.mLastPoint;
        if (point == null) {
            this.mLastPoint = new Point();
            point = this.mLastPoint;
            point.x = 0;
            int j = this.mHeight;
            double d1 = j;
            double d2 = (j / this.mMaxValue * paramInt);
            Double.isNaN(d2);
            Double.isNaN(d1);
            point.y = (int)(d1 - d2 * 0.5D);
            return;
        }
        int[] arrayOfInt = this.mDataBuffer;
        int i = this.mDataBufferIndex;
        double d = paramInt;
        Double.isNaN(d);
        arrayOfInt[i] = (int)(d * 0.5D);
        this.mDataBufferIndex = i + 1;
        if (this.mDataBufferIndex >= this.mBufferSize) {
            this.mDataBufferIndex = 0;
            i = (int)((this.mWidth - point.x) / this.pointStep);
            int j = this.mBufferSize;
            paramInt = i;
            if (i > j)
                paramInt = j;
            i = (int)(this.mLastPoint.x + this.pointStep * paramInt);
            SurfaceHolder surfaceHolder = this.mSurfaceHolder;
            j = this.mLastPoint.x;
            float f = i;
            this.mCanvas = surfaceHolder.lockCanvas(new Rect(j, 0, (int)(this.pointStep * 2.0F + f), (int)(this.mHeight + this.mLineWidth)));
            Canvas canvas = this.mCanvas;
            if (canvas == null)
                return;
            canvas.drawRect(new Rect(this.mLastPoint.x, 0, (int)(f + this.pointStep * 2.0F), (int)(this.mHeight + this.mLineWidth)), this.mBackgroundPaint);
            for (i = 0; i < paramInt; i++) {
                Point point1 = new Point();
                point1.x = (int)(this.mLastPoint.x + this.pointStep);
                j = this.mHeight;
                point1.y = (int)(j - j / this.mMaxValue * this.mDataBuffer[i]);
                this.mCanvas.drawLine(this.mLastPoint.x, this.mLastPoint.y, point1.x, point1.y, this.mWavePaint);
                this.mLastPoint = point1;
            }
            this.mSurfaceHolder.unlockCanvasAndPost(this.mCanvas);
            postInvalidate();
            if ((int)((this.mWidth - this.mLastPoint.x) / this.pointStep) < 1)
                this.mLastPoint.x = 0;
            i = this.mBufferSize;
            if (paramInt < i) {
                this.mDataBufferIndex = i - paramInt;
                for (i = 0; i < this.mDataBufferIndex; i++) {
                    int[] arrayOfInt1 = this.mDataBuffer;
                    arrayOfInt1[i] = arrayOfInt1[paramInt + i];
                }
                this.mLastPoint.x = 0;
            }
        }
    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        super.onMeasure(paramInt1, paramInt2);
        paramInt1 = View.MeasureSpec.getSize(paramInt1);
        if (paramInt1 > this.mWidth)
            this.mWidth = paramInt1;
        double d = View.MeasureSpec.getSize(paramInt2);
        Double.isNaN(d);
        paramInt1 = (int)(d * 0.95D);
        if (paramInt1 > this.mHeight)
            this.mHeight = paramInt1;
    }

    public void reset() {
        this.mDataBufferIndex = 0;
        int i = this.mHeight;
        this.mLastPoint = new Point(0, (int)(i - i / this.mMaxValue * 128.0F));
        Canvas canvas = this.mSurfaceHolder.lockCanvas();
        canvas.drawRect(new Rect(0, 0, this.mWidth, this.mHeight), this.mBackgroundPaint);
        this.mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void setWaveformVisiable(boolean paramBoolean) {
        this.isWaveformVisible = paramBoolean;
        if (this.isWaveformVisible) {
            setVisibility(INVISIBLE);
            setKeepScreenOn(true);
            return;
        }
        setVisibility(VISIBLE);
        setKeepScreenOn(false);
    }

    public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3) {
        Canvas canvas = paramSurfaceHolder.lockCanvas();
        canvas.drawRect(new Rect(0, 0, this.mWidth, this.mHeight), this.mBackgroundPaint);
        paramSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
        if (this.mLastPoint != null)
            this.mLastPoint = null;
        this.isSurfaceViewAvailable = true;
    }

    public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder) {
        this.isSurfaceViewAvailable = false;
    }
}


/* Location:              C:\fof\boom\dex2jar-2.0\classes-dex2jar.jar!\com\berry_med\spo2\custom_view\WaveformView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */