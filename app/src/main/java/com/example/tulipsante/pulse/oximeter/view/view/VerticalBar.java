/*package com.example.tulipsante.pulse.oximeter.view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.tulipsante.R;

public class VerticalBar extends View {
    private int MAX = 20;
    private int level = 0;
    private int left;
    private int top;
    private int height;
    private int width;
    private RectF rectF;
    private Paint paint;
    private BarTestThread mThread;

    public VerticalBar(Context context) {
        super(context);
    }

    public VerticalBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        rectF = new RectF();
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.red));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float yDiff = height * level / MAX;
        rectF = new RectF(left, top + height - yDiff, left + width, top + height);
        canvas.drawRect(rectF, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        left = getPaddingLeft();
        top = getPaddingTop();
        height = h - getPaddingTop() - getPaddingBottom();
        width = w - getPaddingLeft() - getPaddingRight();
    }

    public void setMAX(int max) {
        this.MAX = max;
    }

    public void clearLevel() {
        this.level = 0;
        postInvalidate();
    }

    public void setLevel(int level) {
        if (level < 0 || level > MAX) {
            return;
        }
        this.level = level;
        postInvalidate();
    }

    public void startDemo() {
        if (mThread == null) {
            mThread = new BarTestThread();
            mThread.setRun(true);
            mThread.start();
        }
    }

    public void stoptDemo() {
        if (mThread != null) {
            mThread.setRun(false);
            mThread = null;
        }
    }

    public class BarTestThread extends Thread {

        private boolean isRunning;

        public BarTestThread() {
        }

        public void setRun(boolean run) {
            this.isRunning = run;
        }

        @Override
        public void run() {
            super.run();
            while (isRunning) {
                setLevel(level);
                if (++level > MAX) {
                    level = 0;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

 */
