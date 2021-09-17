package com.example.tulipsante.pulse.oximeter;

import android.content.Context;

import com.example.tulipsante.pulse.oximeter.bean.EGain;
import com.example.tulipsante.pulse.oximeter.bean.ELang;
import com.example.tulipsante.pulse.oximeter.bean.ESpeed;
import com.example.tulipsante.pulse.oximeter.bean.EWorMode;
import com.example.tulipsante.pulse.oximeter.bean.Range;
import com.example.tulipsante.pulse.oximeter.device.usb2serial.bean.BaudRate;
import com.example.tulipsante.pulse.oximeter.util.persist.IPersist;
import com.example.tulipsante.pulse.oximeter.util.persist.impl.PersistFactory;


public class DataCenter {
    private static DataCenter mInstance;
    private IPersist mPersist;

    private final String pkWorkMode = "work_mode"; // usb, ble
    private final String pkSpeed = "speed"; // 5mm/s，10mm/s，12.5mm/s，25mm/s，50mm/s
    private final String pkGain = "gain"; // X0.25，X0.5，X1，X2
    private final String pkPulseSound = "pulse_sound"; // true, false
    private final String pkSpo2Alarm = "spo2_alarm"; // true, false
    private final String pkSpo2AlarmVal = "spo2_alarm_value"; // 50,100
    private final String pkPrAlarm = "pr_alarm"; // true, false
    private final String pkPrAlarmVal = "pr_alarm_value"; // 50,100
    private final String pkLanguage = "language_switch"; // ch, en
    private final String pkBaudRate = "baud_rate"; // 波特率, 数据位, 校验位, 停止位, 流控

    private DataCenter(Context context) {
        mPersist = PersistFactory.getInstance(context);
    }

    public static DataCenter getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DataCenter.class) {
                if (mInstance == null) {
                    mInstance = new DataCenter(context);
                }
            }
        }
        return mInstance;
    }

    public EWorMode getWorkMode() {
        return EWorMode.getEnum((String) mPersist.readData(pkWorkMode, EWorMode.usb.toString()));
    }

    public void setWorkMode(EWorMode mode) {
        mPersist.writeData(pkWorkMode, mode.toString());
    }

    public ESpeed getSpeed() {
        return ESpeed.getEnum((String) mPersist.readData(pkSpeed, ESpeed.v12_5mms.toString()));
    }

    public void setSpeed(ESpeed speed) {
        mPersist.writeData(pkSpeed, speed.toString());
    }

    public EGain getGain() {
        return EGain.getEnum((String) mPersist.readData(pkGain, EGain.x2.toString()));
    }

    public void setGain(EGain gain) {
        mPersist.writeData(pkGain, gain.toString());
    }

    public boolean isPulseSound() {
        return (boolean) mPersist.readData(pkPulseSound, false);
    }

    public void setPulseSound(boolean sound) {
        mPersist.writeData(pkPulseSound, sound);
    }

    public boolean isSpo2Alarm() {
        return (boolean) mPersist.readData(pkSpo2Alarm, false);
    }

    public void setSpo2Alarm(boolean spo2Alarm) {
        mPersist.writeData(pkSpo2Alarm, spo2Alarm);
    }

    public Range getSpo2AlarmVal() {
        String spo2 = (String) mPersist.readData(pkSpo2AlarmVal, Config.cSpo2DefaultRange);
        String[] arr = spo2.split("-");
        return new Range(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
    }

    public void setSpo2AlarmVal(int lower, int upper) {
        mPersist.writeData(pkSpo2AlarmVal, lower + "-" + upper);
    }

    public boolean isPrAlarm() {
        return (boolean) mPersist.readData(pkPrAlarm, false);
    }

    public void setPrAlarm(boolean prAlarm) {
        mPersist.writeData(pkPrAlarm, prAlarm);
    }

    public Range getPrAlarmVal() {
        String pr = (String) mPersist.readData(pkPrAlarmVal, Config.cPrDefaultRange);
        String[] arr = pr.split("-");
        return new Range(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
    }

    public void setPrAlarmVal(int lower, int upper) {
        mPersist.writeData(pkPrAlarmVal, lower + "-" + upper);
    }

    public ELang getLanguage() {
        return ELang.getEnum((String) mPersist.readData(pkLanguage, ELang.en.toString()));
    }

    public void setLanguage(ELang lang) {
        mPersist.writeData(pkLanguage, lang.toString());
    }

    public BaudRate getBaudRate() {
        return BaudRate.parseStr((String) mPersist.readData(pkBaudRate, "4800-8-0-1-0"));
    }

    public void setBaudRate(BaudRate baudRate) {
        mPersist.writeData(pkBaudRate, baudRate.toString());
    }

}
