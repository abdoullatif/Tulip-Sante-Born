package com.example.tulipsante.utils;

import android.app.Application;

import com.jiangdg.usbcamera.UVCCameraHelper;
import com.example.tulipsante.utils.CrashHandler;


class MyApplication extends Application {
    private CrashHandler mCrashHandler;
    // File Directory in sd card
    public static final String DIRECTORY_NAME = "USBCamera";

    @Override
    public void onCreate() {
        super.onCreate();
        mCrashHandler = CrashHandler.getInstance();
        mCrashHandler.init(getApplicationContext(), getClass());
    }
}
