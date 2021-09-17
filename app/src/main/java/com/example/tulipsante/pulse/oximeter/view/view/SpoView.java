package com.example.tulipsante.pulse.oximeter.view.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;


import com.example.tulipsante.R;
import com.example.tulipsante.pulse.oximeter.bean.EGain;
import com.example.tulipsante.pulse.oximeter.bean.ESpeed;

import java.util.Vector;

public class SpoView extends SurfaceView implements SurfaceHolder.Callback {
    private final String TAG = "SpoView";
    private final int SMALL_GRID_PX = 5; // 背景小格距离
    private final int LARGE_GRID_PX = 40;// 背景大格距离
    private final float GAP_CM = 0f; //波形缺口长度0.2cm
    private final float IN_CM = 2.54f; // 1英寸等于2.54cm
    private final float DENSITY; // 设备的密度因子

    private Rect mDrawRect; // 可绘制的区域
    private Point mXyOrigin; // 坐标原点
    private Bitmap mBgBitmap; // 背景 Bitmap
    private int mSpeedPx; // 速度，每秒走多少个像素 px/s
    private float mGain; // y轴增益系数
    private int mGapPx; // 缺口等于多少个像素点 px
    private int mGapLen; // 缺口占波形的长度
    private int mSamplingFreq; // 设备采集频率
    private float mSamplingPx; // 每个采用点等于多少个像素 px
   private HorizontalScrollView scroll;
    private int mWaveLen; // 波形的长度
    private int mWavePos; // 实时波形当前绘制的位置
    private Vector<Integer> mWaveBuffer; // 波形点的缓存buffer
    private Path mWavePath; // 波形路径1
    private Path mWave2Path; // 波形路径2，缺口把波形 1分为2
    private Paint mWavePaint; // 波形 Paint
    private volatile boolean isDraw = false; // 实时波形绘制中
    private DrawWaveThread mDrawThread;
    private SurfaceHolder mHolder;
    private volatile boolean isFingerOff = false; // 手指脱落

    public SpoView(Context context) {
        this(context, null);
    }

    public SpoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.addCallback(this);
        scroll = findViewById(R.id.scroll);
       // scroll.fullScroll(View.FOCUS_DOWN);

        DENSITY = context.getResources().getDisplayMetrics().density;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        int width = getWidth();
        int height = getHeight()-50;

      //  scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
        initBg(width, height);
      //  scroll.smoothScrollTo(2000,0);


        drawBg();
        initWave();
        if (isDraw && mDrawThread == null) {
            mDrawThread = new DrawWaveThread();
            mDrawThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (isDraw && mDrawThread != null) {
            mDrawThread.close();
            mDrawThread = null;
        }
    }

    private void initBg(int width, int height) {
        // 设置可用绘制的区域
        int nWidth = LARGE_GRID_PX * (width / LARGE_GRID_PX);
        int nHeight = LARGE_GRID_PX * (height / LARGE_GRID_PX);
        int left = (width - nWidth) / 2;
        int right = left + nWidth;
        int top = (height - nHeight) / 2;
        int bottom = top + nHeight;

        mDrawRect = new Rect(left, top, right, bottom);

        // 绘制背景到 mBgBitmap 图片上
        Paint paintThin = new Paint();
        Paint paintWide = new Paint();
        paintThin.setColor(ContextCompat.getColor(getContext(), R.color.wave_thin_line));
        paintWide.setStyle(Paint.Style.STROKE);
        paintWide.setColor(ContextCompat.getColor(getContext(), R.color.wave_wide_line));


        Canvas canvas = new Canvas();
        mBgBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
        canvas.setBitmap(mBgBitmap);
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.white));

        int i;
        for (i = mDrawRect.left; i <= mDrawRect.right; i += SMALL_GRID_PX) {
            canvas.drawLine(i, mDrawRect.top, i, mDrawRect.bottom, paintThin);
        }
        for (i = mDrawRect.top; i <= mDrawRect.bottom; i += SMALL_GRID_PX) {
            canvas.drawLine(mDrawRect.left, i, mDrawRect.right, i, paintThin);
        }
        for (i = mDrawRect.left; i <= mDrawRect.right; i += LARGE_GRID_PX) {
            canvas.drawLine(i, mDrawRect.top, i, mDrawRect.bottom, paintWide);
        }
        for (i = mDrawRect.top; i <= mDrawRect.bottom; i += LARGE_GRID_PX) {
            canvas.drawLine(mDrawRect.left, i, mDrawRect.right, i, paintWide);
        }
    }

    private void initWave() {
        // 设置波形Paint
        mWavePaint = new Paint();
        mWavePaint.setPathEffect(new CornerPathEffect(5));
        mWavePaint.setStyle(Paint.Style.STROKE);
        mWavePaint.setStrokeWidth(3);
        mWavePaint.setAntiAlias(true);
        mWavePaint.setColor(Color.YELLOW);


        mWavePath = new Path();
        mWave2Path = new Path();
        mWaveBuffer = new Vector<>();

        mXyOrigin = new Point(mDrawRect.left, mDrawRect.bottom);
        mGapPx = (int) ((GAP_CM / IN_CM * 160 * DENSITY) + 0.5);
    }

    // 把 mBgBitmap 图片绘制到 SurfaceView上
    private void drawBg() {

        Canvas canvas = null;
        try {
            canvas = mHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawBitmap(mBgBitmap, 0, 0, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    // 绘制波形
    private void drawWave() {
        Canvas canvas = null;
        try {

            canvas = mHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawBitmap(mBgBitmap, 0, 0, null);
                if (!isFingerOff) {
                    calcPath();
                    canvas.drawPath(mWavePath, mWavePaint);
                    canvas.drawPath(mWave2Path, mWavePaint);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private synchronized void clear() {
        if (mWaveBuffer != null) {
            mWaveBuffer.clear();
            mWavePos = 0;
        }
    }

    private synchronized void calcPath() {
        if (mWaveBuffer.size() == 0) {
            return;
        }




        // 原点
        float oriX = mXyOrigin.x;
        float oriY = mXyOrigin.y;
        // 波形的起点
        float startX, startY, start2X, start2Y;
        float curX = oriX;
        mWavePath.reset();
        mWave2Path.reset();


        if (mWavePos < 1) {
            startX = curX;
            startY = oriY - mGain * mWaveBuffer.get(0);
            mWavePath.moveTo(curX, startY);
            for (int i = 1; i < mWaveBuffer.size(); i++) {
                curX += mSamplingPx;
                mWavePath.lineTo(curX, oriY - mGain * mWaveBuffer.get(i));
            }

            // 添加封闭路径
            mWavePath.lineTo(curX, oriY);
            mWavePath.lineTo(startX, oriY);
            mWavePath.lineTo(startX, startY);
        } else {
          //  Toast.makeText(this.getContext(), ""+curX, Toast.LENGTH_LONG).show();
            startX = curX;
            startY = oriY - mGain * mWaveBuffer.get(0);
            mWavePath.moveTo(curX, startY);
            for (int i = 1; i < mWavePos; i++) {
                curX += mSamplingPx;
                mWavePath.lineTo(curX, oriY - mGain * mWaveBuffer.get(i));
            }

            // 添加封闭路径
            mWavePath.lineTo(curX, oriY);
            mWavePath.lineTo(startX, oriY);
            mWavePath.lineTo(startX, startY);

            curX += mGapLen * mSamplingPx;
            start2X = curX;
            start2Y = oriY - mGain * mWaveBuffer.get(mWavePos + mGapLen);
            mWave2Path.moveTo(curX, start2Y);
            for (int i = mWavePos + mGapLen; i < mWaveBuffer.size(); i++) {

                curX += mSamplingPx;
               // scroll.scrollTo(Integer.valueOf((int) curX),Integer.valueOf((int) (oriY - mGain * mWaveBuffer.get(i))));

                mWave2Path.lineTo(curX, oriY - mGain * mWaveBuffer.get(i));
            }
            // 添加封闭路径
            mWave2Path.lineTo(curX, oriY);
            mWave2Path.lineTo(start2X, oriY);
            mWave2Path.lineTo(start2X, start2Y);
        }
    }

    private void setSpeedPx(ESpeed speedMode) {
        int speedPx = 0;
        if (speedMode == ESpeed.v5mms) {
            speedPx = (int) ((ESpeed.v5mms.value() / IN_CM / 10 * 160 * DENSITY) + 0.5);
        } else if (speedMode == ESpeed.v10mms) {
            speedPx = (int) ((ESpeed.v10mms.value() / IN_CM / 10 * 160 * DENSITY) + 0.5);
        } else if (speedMode == ESpeed.v12_5mms) {
            speedPx = (int) ((ESpeed.v12_5mms.value() / IN_CM / 10 * 160 * DENSITY) + 0.5);
        } else if (speedMode == ESpeed.v25mms) {
            speedPx = (int) ((ESpeed.v25mms.value() / IN_CM / 10 * 160 * DENSITY) + 0.5);
        } else if (speedMode == ESpeed.v50mms) {
            speedPx = (int) ((ESpeed.v50mms.value() / IN_CM / 10 * 160 * DENSITY) + 0.5);
        }
        mSpeedPx = speedPx;
        mSamplingPx = 1.0f * mSpeedPx / mSamplingFreq;
        mGapLen = (int) (1.0f * mGapPx / mSamplingPx + 0.5);
        if (mDrawRect != null) {
            mWaveLen = (int) (1.0f * (mDrawRect.right - mDrawRect.left) / mSamplingPx + 0.5);
        }
    }

    public synchronized void addPoint(int val) {
        if (mWaveLen == 0) {
            return;
        }

        if (mWaveBuffer.size() == mWaveLen) {
            mWaveBuffer.set(mWavePos, val);
            if (++mWavePos >= mWaveLen) {
                mWavePos = 0;
            }
        } else {
            mWaveBuffer.add(val);
            mWavePos = 0;
        }
    }

    public synchronized void setFingerOff(boolean fingerOff) {
        if(fingerOff) {
            clear();
        }
        this.isFingerOff = fingerOff;
    }

    public synchronized SpoView setFreq(int freq) {
        mSamplingFreq = freq;
        return this;
    }

    /**
     * @param speed 速度，每秒走多少mm mm/s
     */
    public synchronized SpoView setSpeed(ESpeed speed) {
        setSpeedPx(speed);
        clear();
        return this;
    }

    /**
     * @param gain y轴增益
     */
    public synchronized SpoView setGain(EGain gain) {
        mGain = gain.value();
        clear();
        return this;
    }

    public void startDraw() {
        isDraw = true;
        if (mDrawThread == null) {
            mDrawThread = new DrawWaveThread();
            mDrawThread.start();
        }
    }

    public void stopDraw() {
        isDraw = false;
        if (mDrawThread != null) {
            mDrawThread.close();
            mDrawThread = null;
        }
        drawBg();
    }

    class DrawWaveThread extends Thread {
        public boolean isRunning;

        public DrawWaveThread() {
            isRunning = true;
        }

        @Override
        public synchronized void start() {
            super.start();
            Log.e(TAG, "波形绘制线程运行...");
        }

        public synchronized void close() {
            isRunning = false;
            Log.e(TAG, "波形绘制线程停止...");
        }

        @Override
        public void run() {
            while (isRunning) {
                drawWave();
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
