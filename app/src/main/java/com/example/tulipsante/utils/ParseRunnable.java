package com.example.tulipsante.utils;

import java.util.concurrent.LinkedBlockingQueue;

public class ParseRunnable implements Runnable {
    private static int PACKAGE_LEN = 5;

    private boolean isStop = false;

    private OnDataChangeListener mOnDataChangeListener;

    private OxiParams mOxiParams = new OxiParams();

    private LinkedBlockingQueue<Integer> oxiData = new LinkedBlockingQueue<Integer>(256);

    private int[] parseBuf = new int[5];

    public ParseRunnable(OnDataChangeListener paramOnDataChangeListener) {
        this.mOnDataChangeListener = paramOnDataChangeListener;
    }

    private int getData() {
        try {
            return ((Integer)this.oxiData.take()).intValue();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
            return 0;
        }
    }

    public static float getFloatPi(int paramInt) {
        switch (paramInt) {
            default:
                return 0.0F;
            case 8:
                return 20.0F;
            case 7:
                return 10.3F;
            case 6:
                return 5.3F;
            case 5:
                return 2.7F;
            case 4:
                return 1.4F;
            case 3:
                return 0.7F;
            case 2:
                return 0.4F;
            case 1:
                return 0.2F;
            case 0:
                break;
        }
        return 0.1F;
    }

    private int toUnsignedInt(byte paramByte) {
        return paramByte & 0xFF;
    }

    public void add(byte[] paramArrayOfbyte) {
        int j = paramArrayOfbyte.length;
        for (int i = 0; i < j; i++) {
            byte b = paramArrayOfbyte[i];
            try {
                this.oxiData.put(Integer.valueOf(toUnsignedInt(b)));
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }

    public OxiParams getOxiParams() {
        return this.mOxiParams;
    }

    public void run() {
        while (!this.isStop) {
            int i = getData();
            if ((i & 0x80) > 0) {
                this.parseBuf[0] = i;
                for (i = 1; i < PACKAGE_LEN; i++) {
                    int m = getData();
                    if ((m & 0x80) == 0)
                        this.parseBuf[i] = m;
                }
                int[] arrayOfInt = this.parseBuf;
                i = arrayOfInt[4];
                int j = arrayOfInt[3] | (arrayOfInt[2] & 0x40) << 1;
                int k = arrayOfInt[0] & 0xF;
                if (i != this.mOxiParams.spo2 || j != this.mOxiParams.pulseRate || k != this.mOxiParams.pi) {
                    this.mOxiParams.update(i, j, k);
                    this.mOnDataChangeListener.onSpO2ParamsChanged();
                }
                this.mOnDataChangeListener.onSpO2WaveChanged(this.parseBuf[1]);
                if ((this.parseBuf[0] & 0x40) != 0)
                    this.mOnDataChangeListener.onPulseWaveDetected();
            }
        }
    }

    public void stop() {
        this.isStop = false;
    }

    public static interface OnDataChangeListener {
        void onPulseWaveDetected();

        void onSpO2ParamsChanged();

        void onSpO2WaveChanged(int param1Int);
    }

    public class OxiParams {
        public int PI_INVALID_VALUE = 15;

        public int PR_INVALID_VALUE = 255;

        public int SPO2_INVALID_VALUE = 127;

        private int pi;

        private int pulseRate;

        private int spo2;

        private void update(int param1Int1, int param1Int2, int param1Int3) {
            this.spo2 = param1Int1;
            this.pulseRate = param1Int2;
            this.pi = param1Int3;
        }

        public int getPi() {
            return this.pi;
        }

        public int getPulseRate() {
            return this.pulseRate;
        }

        public int getSpo2() {
            return this.spo2;
        }

        public boolean isParamsValid() {
            int i = this.spo2;
            if (i != this.SPO2_INVALID_VALUE) {
                int j = this.pulseRate;
                if (j != this.PR_INVALID_VALUE) {
                    int k = this.pi;
                    if (k != this.PI_INVALID_VALUE && i != 0 && j != 0 && k != 0)
                        return true;
                }
            }
            return false;
        }
    }
}


/* Location:              C:\fof\boom\dex2jar-2.0\classes-dex2jar.jar!\com\berry_med\spo2\bluetooth\ParseRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */