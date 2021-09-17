package com.example.tulipsante.pulse.oximeter.device;

import android.util.Log;


import com.example.tulipsante.pulse.oximeter.repository.bean.SpoRecord;

import java.util.LinkedList;

/**
 * SpoStats，Spo Statistics 血氧统计的缩写
 */
public class SpoStats {
    private static final String TAG = "SpoStats";
    private final int minMeasureTime = 15; // 血氧统计有效的最低时长15s；血氧统计的结果是最后10s的平均值
    private long curMeasureTime = 0; // 血氧的真实测量时长
    private volatile boolean isStats;  // 是否开始统计
    private LimitedList<SpoBean> buffer; // 缓存每秒的spo2、pr、pi平均值
    private volatile long lastTick = 0; // 时间参考
    private long cntSec; // 每秒统计次数
    private long spo2Sec, prSec, piSec; // 每秒spo2、pr、pi的累加值

    public SpoStats() {
        isStats = false;
        this.buffer = new LimitedList<>(10);
    }

    public synchronized void startStats() {
        isStats = true;
        buffer.clear();
        curMeasureTime = lastTick = System.currentTimeMillis();
        cntSec = spo2Sec = prSec = piSec = 0;
    }

    public synchronized SpoRecord stopStats() {
        isStats = false;
        long curTick = System.currentTimeMillis();
        curMeasureTime = (curTick - curMeasureTime) / 1000;
        Log.e(TAG, "血氧一共测量：" + curMeasureTime);
        if (curMeasureTime < minMeasureTime) {
            return null;
        }

        if (buffer.size() == 10) {
            SpoRecord record = new SpoRecord();
            long spo2 = 0, pr = 0, pi = 0;
            for (SpoBean bean : buffer) {
                spo2 += bean.getSpO2();
                pr += bean.getPR();
                pi += bean.getStrength();
            }
            record.setTime(curTick);
            record.setSpo2((int) (spo2 / 10));
            record.setPr((int) (pr / 10));
            record.setPi((float) (pi / 10));
            return record;
        }

        return null;
    }

    public synchronized void setSpo(SpoBean bean) {
        if (!isStats) {
            return;
        }

        if (bean.isProbeOff() || bean.isFingerOff() || (bean.getSpO2() == 127) || (bean.getPR() == 255)) {
            buffer.clear();
            curMeasureTime = lastTick = System.currentTimeMillis();
            cntSec = spo2Sec = prSec = piSec = 0;
            return;
        }

        long curTick = System.currentTimeMillis();
        if (curTick - lastTick > 1000) {
            SpoBean average = new SpoBean();
            average.setSpO2((int) (cntSec == 0 ? 0 : spo2Sec / cntSec));
            average.setPR((int) (cntSec == 0 ? 0 : prSec / cntSec));
            average.setStrength((byte) (cntSec == 0 ? 0 : piSec / cntSec));
            buffer.add(average);

            lastTick = curTick;
            cntSec = spo2Sec = prSec = piSec = 0;
        } else {
            spo2Sec += bean.getSpO2();
            prSec += bean.getPR();
            piSec += bean.getStrength();
            cntSec++;
        }
    }

    /**
     * 有长度限制的队列，满了就覆盖最早的
     *
     * @param <E>
     */
    class LimitedList<E> extends LinkedList<E> {
        private int limit;

        public LimitedList(int limit) {
            this.limit = limit;
        }

        @Override
        public boolean add(E o) {
            super.add(o);
            while (size() > limit) {
                super.remove();
            }
            return true;
        }
    }
}
