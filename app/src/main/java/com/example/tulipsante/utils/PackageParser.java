package com.example.tulipsante.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class PackageParser {

    private OxiParams mOxiParams;
    private OnDataChangeListener mOnDataChangeListener;

    public PackageParser(OnDataChangeListener onDataChangeListener)
    {
        this.mOnDataChangeListener = onDataChangeListener;

        mOxiParams = new OxiParams();
    }

    public void parse(int[] packageDat) {
        System.out.println("====== Package Dat =====");
        System.out.println(packageDat);

        int spo2, pulseRate, pi;

        spo2      = packageDat[4];
        pulseRate = packageDat[3] | ((packageDat[2] & 0x40) << 1);
        pi        = packageDat[0] & 0x0f;

//        spo2      = packageDat[4] & 0x7F;
//        pulseRate = packageDat[3] & 0xFF;
//        pi        = packageDat[1];

        if(spo2 != mOxiParams.spo2 || pulseRate != mOxiParams.pulseRate || pi != mOxiParams.pi)
        {
            mOxiParams.update(spo2,pulseRate,pi);
            mOnDataChangeListener.onParamsChanged();
        }
        mOnDataChangeListener.onWaveChanged(packageDat[1]);
    }


    /**
     * interface for parameters changed.
     */
    public interface OnDataChangeListener
    {
        void onParamsChanged();
        void onWaveChanged(int wave);
    }


    /**
     * a small collection of Oximeter parameters.
     * you can add more parameters as the manual.
     *
     * spo2          Pulse Oxygen Saturation
     * pulseRate     pulse rate
     * pi            perfusion index
     *
     */
    public class OxiParams
    {
        public int PI_INVALID_VALUE = 15;

        public int PULSE_RATE_INVALID_VALUE = 255;

        public int SPO2_INVALID_VALUE = 127;

        private int pi;

        private int pulseRate;

        private int spo2;           //perfusion index

        private void update(int spo2, int pulseRate, int pi) {
            this.spo2 = spo2;
            this.pulseRate = pulseRate;
            this.pi = pi;
        }

        public int getSpo2() {
            return spo2;
        }

        public int getPulseRate() {
            return pulseRate;
        }

        public int getPi() {
            return pi;
        }

        @Override
        public String toString() {
            return "OxiParams{" +
                    "pi=" + pi +
                    ", pulseRate=" + pulseRate +
                    ", spo2=" + spo2 +
                    '}';
        }
    }

    public OxiParams getOxiParams()
    {
        return mOxiParams;
    }

}