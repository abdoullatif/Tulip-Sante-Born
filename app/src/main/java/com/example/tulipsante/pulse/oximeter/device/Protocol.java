package com.example.tulipsante.pulse.oximeter.device;

import java.util.ArrayList;
import java.util.List;

public class Protocol {
    private byte[] m_ui8aBuffer = new byte[1024];
    private int m_i32BuffLength = 0;
    private byte[] m_ui8aFrameBuff = new byte[10];
    private int m_i32FrameLen = 0;

    public synchronized List<SpoBean> decode(byte[] buff, int len) {
        List<SpoBean> list = new ArrayList<>();
        if (len > 0 && m_i32BuffLength + len < 1024) {
            for (int i = 0; i < len; i++) {
                m_ui8aBuffer[m_i32BuffLength + i] = buff[i];
            }
            m_i32BuffLength += len;

            int length = m_i32BuffLength;
            for (int i = 0; i < length; i++) {
                if (0x80 == (m_ui8aBuffer[i] & 0x80)) {
                    if (m_i32FrameLen == 5 || m_i32FrameLen == 7) {
                        SpoBean bean = new SpoBean();
                        bean.setSearchTooLong(0x10 == (m_ui8aFrameBuff[0] & 0x10));
                        bean.setProbeOff(0x20 == (m_ui8aFrameBuff[0] & 0x20));
                        if ((0x40 == (m_ui8aFrameBuff[0] & 0x40))) {
                            bean.setHeartFlag(true);
                        }
                        bean.setFingerOff(0x10 == (m_ui8aFrameBuff[2] & 0x10));
                        bean.setSearching(0x20 == (m_ui8aFrameBuff[2] & 0x20));
                        bean.setStrength((byte) (m_ui8aFrameBuff[0] & 0x0F));
                        bean.setBarGraph((byte) (m_ui8aFrameBuff[2] & 0x0F));
                        bean.setPlethWave(m_ui8aFrameBuff[1] & 0x7F);
                        int pr = 0;
                        if (0x40 == (m_ui8aFrameBuff[2] & 0x40)) {
                            pr = 128;
                        }
                        pr += m_ui8aFrameBuff[3];
                        bean.setPR(pr);
                        bean.setSpO2(m_ui8aFrameBuff[4]);
                        list.add(bean);
                    }

                    m_ui8aFrameBuff[0] = m_ui8aBuffer[i];
                    m_i32FrameLen = 1;
                } else {
                    m_ui8aFrameBuff[m_i32FrameLen++] = m_ui8aBuffer[i];
                    // 防止溢出
                    if (m_i32FrameLen > 9) m_i32FrameLen = 0;
                }
            }
            m_i32BuffLength = 0;
        }
        return list;
    }
}
