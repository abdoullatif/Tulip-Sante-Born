package com.example.tulipsante.utils;

import java.util.ArrayList;
import java.util.List;

/// this is the protocol to parse the data
public class PulseParser {
    private int m_i32BuffLength = 0;

    private int m_i32FrameLen = 0;

    private byte[] m_ui8aBuffer = new byte[1024];

    private byte[] m_ui8aFrameBuff = new byte[10];

    public List<SpoBean> decode(byte[] ui8aBuffer, int n) {
        while (true) {
            ArrayList<SpoBean> list;
            int i;
            int i32BuffLength;
            byte b;
            int i32FrameLen;
            SpoBean spoBean;
            boolean searchTooLong;
            boolean probeOff;
            boolean fingerOff;
            boolean searching;
            byte[] array;
            int i32FrameLen2;
            try {
                list = new ArrayList<SpoBean>();
                if (n > 0 && this.m_i32BuffLength + n < 1024) {
                    for (int j = 0; j < n; ++j) {
                        this.m_ui8aBuffer[this.m_i32BuffLength + j] = ui8aBuffer[j];
                    }
                    i32BuffLength = this.m_i32BuffLength + n;
                    this.m_i32BuffLength = i32BuffLength;
                    n = 0;
                    if (n < i32BuffLength) {
                        ui8aBuffer = this.m_ui8aBuffer;
                        b = ui8aBuffer[n];
                        i = 128;
                        if (0x80 == (b & 0x80)) {
                            i32FrameLen = this.m_i32FrameLen;
                            if (i32FrameLen == 5 || i32FrameLen == 7) {
                                spoBean = new SpoBean();
                                if (0x10 != (this.m_ui8aFrameBuff[0] & 0x10)) {
                                }
                                searchTooLong = true;
                                spoBean.setSearchTooLong(searchTooLong);
                                if (0x20 != (this.m_ui8aFrameBuff[0] & 0x20)) {
                                }
                                probeOff = true;
                                spoBean.setProbeOff(probeOff);
                                if (0x40 == (this.m_ui8aFrameBuff[0] & 0x40)) {
                                    spoBean.setHeartFlag(true);
                                }
                                if (0x10 != (this.m_ui8aFrameBuff[2] & 0x10)) {
                                }
                                fingerOff = true;
                                spoBean.setFingerOff(fingerOff);
                                if (0x20 != (this.m_ui8aFrameBuff[2] & 0x20)) {
                                }
                                searching = true;
                                spoBean.setSearching(searching);
                                spoBean.setStrength((byte)(this.m_ui8aFrameBuff[0] & 0xF));
                                spoBean.setBarGraph((byte)(this.m_ui8aFrameBuff[2] & 0xF));
                                spoBean.setPlethWave(this.m_ui8aFrameBuff[1] & 0x7F);
                                array = this.m_ui8aFrameBuff;
                                if (0x40 != (array[2] & 0x40)) {
                                }
                                spoBean.setPR(i + array[3]);
                                spoBean.setSpO2((int)this.m_ui8aFrameBuff[4]);
                                list.add(spoBean);
                            }
                            this.m_ui8aFrameBuff[0] = this.m_ui8aBuffer[n];
                            this.m_i32FrameLen = 1;
                        }
                        array = this.m_ui8aFrameBuff;
                        i = this.m_i32FrameLen;
                        i32FrameLen2 = i + 1;
                        this.m_i32FrameLen = i32FrameLen2;
                        array[i] = ui8aBuffer[n];
                        if (i32FrameLen2 > 9) {
                            this.m_i32FrameLen = 0;
                        }
                    }
                    else {
                        this.m_i32BuffLength = 0;
                    }
                }
                return list;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
