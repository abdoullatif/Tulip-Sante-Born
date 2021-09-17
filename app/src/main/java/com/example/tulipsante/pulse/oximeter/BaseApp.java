package com.example.tulipsante.pulse.oximeter;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;


import com.example.tulipsante.R;
import com.example.tulipsante.pulse.oximeter.bean.EWorMode;
import com.example.tulipsante.pulse.oximeter.device.usb2serial.UsbMgr;
import com.umeng.cconfig.RemoteConfigSettings;
import com.umeng.cconfig.UMRemoteConfig;
import com.umeng.cconfig.listener.OnConfigStatusChangedListener;
import com.umeng.commonsdk.UMConfigure;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class BaseApp extends Application {
    private static final String TAG = "app";
    private static BaseApp mInstance;
    private Map<String, WeakReference<Activity>> mActivities = new HashMap<>();

    public static BaseApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        SoundMgr.getSoundManager().initSoundLoad(this);
        DataCenter dataCenter = DataCenter.getInstance(this);
        if (!UsbMgr.getInstance(this).UsbFeatureSupported()) {
            dataCenter.setWorkMode(EWorMode.ble);

            Toast.makeText(this, getString(R.string.toast_not_support_usb), Toast.LENGTH_SHORT).show();
        }

        UMConfigure.setLogEnabled(true);
        UMRemoteConfig.getInstance().setConfigSettings(new RemoteConfigSettings.Builder().setAutoUpdateModeEnabled(false).build());
        UMRemoteConfig.getInstance().setOnNewConfigfecthed(new OnConfigStatusChangedListener() {
            @Override
            public void onFetchComplete() {

                UMRemoteConfig.getInstance().activeFetchConfig();
            }

            @Override
            public void onActiveComplete() {

            }
        });
        UMConfigure.init(this, "5fb3a91773749c24fd9b51b4", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void addActivity(Activity activity, String activityName) {
        WeakReference<Activity> ref = new WeakReference<>(activity);
        mActivities.put(activityName, ref);
    }

    public void removeActivity(String activityName) {
        WeakReference<Activity> ref = mActivities.get(activityName);
        if (ref != null) {
            Activity activity = ref.get();
            if (activity != null) {
                activity.finish();
            }
            mActivities.remove(activityName);
        }
    }

    public Activity getActivity(String activityName) {
        WeakReference<Activity> ref = mActivities.get(activityName);
        Activity activity = null;
        if (ref != null) {
            activity = ref.get();
        }

        return activity;
    }

    public void exitApp() {
        for (String key : mActivities.keySet()) {
            removeActivity(key);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
