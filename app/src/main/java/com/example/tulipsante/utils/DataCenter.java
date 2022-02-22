package com.example.tulipsante.utils;

import android.content.Context;

public class DataCenter {
    private static DataCenter mInstance;

    private IPersist mPersist;

    private final String pkBaudRate = "baud_rate";

    private final String pkGain = "gain";

    private final String pkLanguage = "language_switch";

    private final String pkPrAlarm = "pr_alarm";

    private final String pkPrAlarmVal = "pr_alarm_value";

    private final String pkPulseSound = "pulse_sound";

    private final String pkSpeed = "speed";

    private final String pkSpo2Alarm = "spo2_alarm";

    private final String pkSpo2AlarmVal = "spo2_alarm_value";

    private final String pkWorkMode = "work_mode";

    private DataCenter(Context paramContext) {
//        this.mPersist = PersistFactory.getInstance(paramContext);
    }

//    public static DataCenter getInstance(Context paramContext) {
        // Byte code:
        //   0: getstatic com/spark/oximeter/DataCenter.mInstance : Lcom/spark/oximeter/DataCenter;
        //   3: ifnonnull -> 38
        //   6: ldc com/spark/oximeter/DataCenter
        //   8: monitorenter
        //   9: getstatic com/spark/oximeter/DataCenter.mInstance : Lcom/spark/oximeter/DataCenter;
        //   12: ifnonnull -> 26
        //   15: new com/spark/oximeter/DataCenter
        //   18: dup
        //   19: aload_0
        //   20: invokespecial <init> : (Landroid/content/Context;)V
        //   23: putstatic com/spark/oximeter/DataCenter.mInstance : Lcom/spark/oximeter/DataCenter;
        //   26: ldc com/spark/oximeter/DataCenter
        //   28: monitorexit
        //   29: goto -> 38
        //   32: astore_0
        //   33: ldc com/spark/oximeter/DataCenter
        //   35: monitorexit
        //   36: aload_0
        //   37: athrow
        //   38: getstatic com/spark/oximeter/DataCenter.mInstance : Lcom/spark/oximeter/DataCenter;
        //   41: areturn
        // Exception table:
        //   from	to	target	type
        //   9	26	32	finally
        //   26	29	32	finally
        //   33	36	32	finally
//    }

    public BaudRate getBaudRate() {
        return BaudRate.parseStr((String)this.mPersist.readData("baud_rate", "4800-8-0-1-0"));
    }

//    public EGain getGain() {
//        return EGain.getEnum((String)this.mPersist.readData("gain", EGain.x2.toString()));
//    }
//
//    public ELang getLanguage() {
//        return ELang.getEnum((String)this.mPersist.readData("language_switch", ELang.en.toString()));
//    }

    public Range getPrAlarmVal() {
        String[] arrayOfString = ((String)this.mPersist.readData("pr_alarm_value", "50-100")).split("-");
        return new Range(Integer.parseInt(arrayOfString[0]), Integer.parseInt(arrayOfString[1]));
    }

//    public ESpeed getSpeed() {
//        return ESpeed.getEnum((String)this.mPersist.readData("speed", ESpeed.v12_5mms.toString()));
//    }

    public Range getSpo2AlarmVal() {
        String[] arrayOfString = ((String)this.mPersist.readData("spo2_alarm_value", "80-100")).split("-");
        return new Range(Integer.parseInt(arrayOfString[0]), Integer.parseInt(arrayOfString[1]));
    }

//    public EWorMode getWorkMode() {
//        return EWorMode.getEnum((String)this.mPersist.readData("work_mode", EWorMode.usb.toString()));
//    }

    public boolean isPrAlarm() {
        return ((Boolean)this.mPersist.readData("pr_alarm", Boolean.valueOf(false))).booleanValue();
    }

    public boolean isPulseSound() {
        return ((Boolean)this.mPersist.readData("pulse_sound", Boolean.valueOf(false))).booleanValue();
    }

    public boolean isSpo2Alarm() {
        return ((Boolean)this.mPersist.readData("spo2_alarm", Boolean.valueOf(false))).booleanValue();
    }

    public void setBaudRate(BaudRate paramBaudRate) {
        this.mPersist.writeData("baud_rate", paramBaudRate.toString());
    }

//    public void setGain(EGain paramEGain) {
//        this.mPersist.writeData("gain", paramEGain.toString());
//    }
//
//    public void setLanguage(ELang paramELang) {
//        this.mPersist.writeData("language_switch", paramELang.toString());
//    }

    public void setPrAlarm(boolean paramBoolean) {
        this.mPersist.writeData("pr_alarm", Boolean.valueOf(paramBoolean));
    }

    public void setPrAlarmVal(int paramInt1, int paramInt2) {
        IPersist iPersist = this.mPersist;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(paramInt1);
        stringBuilder.append("-");
        stringBuilder.append(paramInt2);
        iPersist.writeData("pr_alarm_value", stringBuilder.toString());
    }

    public void setPulseSound(boolean paramBoolean) {
        this.mPersist.writeData("pulse_sound", Boolean.valueOf(paramBoolean));
    }

//    public void setSpeed(ESpeed paramESpeed) {
//        this.mPersist.writeData("speed", paramESpeed.toString());
//    }

    public void setSpo2Alarm(boolean paramBoolean) {
        this.mPersist.writeData("spo2_alarm", Boolean.valueOf(paramBoolean));
    }

    public void setSpo2AlarmVal(int paramInt1, int paramInt2) {
        IPersist iPersist = this.mPersist;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(paramInt1);
        stringBuilder.append("-");
        stringBuilder.append(paramInt2);
        iPersist.writeData("spo2_alarm_value", stringBuilder.toString());
    }

//    public void setWorkMode(EWorMode paramEWorMode) {
//        this.mPersist.writeData("work_mode", paramEWorMode.toString());
//    }
}