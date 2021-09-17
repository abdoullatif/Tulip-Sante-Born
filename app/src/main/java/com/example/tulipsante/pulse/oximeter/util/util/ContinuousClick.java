package com.example.tulipsante.pulse.oximeter.util.util;

import android.os.SystemClock;
import android.util.Log;

public class ContinuousClick {
    private final String TAG = "continuousClick";
    private final long DURATION = 5000;// 点击总的有效时间
    private final int OVERTIME = 2000; // 单次间隔2s以上点击超时
    private int COUNTS; // 连续点击的次数
    private OnContiClick mClick; //连续点击成功回调
    private long[] mHits;
    private long mLastTick = 0;

    public ContinuousClick(int counts, OnContiClick click) {
        this.COUNTS = counts;
        this.mClick = click;
        this.mHits = new long[this.COUNTS];
    }

    public void click() {
        long tick = SystemClock.uptimeMillis();
        if (mLastTick != 0) {
            if (tick - mLastTick > OVERTIME) {
                mHits = new long[COUNTS];//重新初始化数组
                Log.e(TAG, "点击超时，清空连续点击");
            }
        }
        mLastTick = tick;

        //每次点击时，数组向前移动一位
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        //为数组最后一位赋值
        mHits[mHits.length - 1] = tick;
        if (mHits[0] >= (tick - DURATION)) {
            mHits = new long[COUNTS];//重新初始化数组
            Log.e(TAG, "连续点击了" + COUNTS + "次");
            if (mClick != null) {
                mClick.onContiClick();
            }
        }
    }

    public interface OnContiClick {
        void onContiClick();
    }
}
