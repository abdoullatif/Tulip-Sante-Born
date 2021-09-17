package com.example.tulipsante.pulse.oximeter.device.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

/**
 * // 蓝牙权限是normal级权限，只需要在Manifest里面声明即可，不需要判断和处理
 * <uses-permission android:name="android.permission.BLUETOOTH" />
 * <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
 * // dangerous级权限，需要进行动态申请。
 * // 通过WiFi或移动基站获取粗略定位
 * <uses-permission-sdk-23 android:name="android.permission.ACCESS_COARSE_LOCATION" />
 * // GPS精确定位
 * <uses-permission-sdk-23 android:name="android.permission.ACCESS_FINE_LOCATION" />
 * // 如果需要在后台扫描
 * <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
 * 如果target < 23，则不需要动态申请权限 申明蓝牙后可以直接使用蓝牙api.
 * 如果target < 29，则只要您的应用已声明 ACCESS_COARSE_LOCATION 或 ACCESS_FINE_LOCATION 权限，您就可以使用受影响的 API（WifiP2pManager API 除外）。
 * 如果target >= 29，则它必须具有 ACCESS_FINE_LOCATION 权限才能使用 WLAN、WLAN 感知或蓝牙 API 中的一些方法。如果需要在后台扫描，需要添加ACCESS_BACKGROUND_LOCATION权限
 */
public class LocationPermission {

    private LocationPermission() {
        // Utility class
    }

    private static final int REQUEST_PERMISSION_BLE_SCAN = 9358;

    private static String[] getRecommendedScanRuntimePermissions() {
        if (Build.VERSION.SDK_INT < 23) {
            return new String[]{""};
        } else if (Build.VERSION.SDK_INT < 29) {
            return new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        } else {
            return new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION};
        }
    }

    public static boolean checkLocationPermission(Activity activity) {
        String[] recommendedScanRuntimePermissions = getRecommendedScanRuntimePermissions();
        if (Build.VERSION.SDK_INT < 29) {
            for (String recommendedScanRuntimePermission : recommendedScanRuntimePermissions) {
                if (ActivityCompat.checkSelfPermission(activity, recommendedScanRuntimePermission) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        } else {
            if (ActivityCompat.checkSelfPermission(activity, recommendedScanRuntimePermissions[0]) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    public static void requestLocationPermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{getRecommendedScanRuntimePermissions()[0]},
                REQUEST_PERMISSION_BLE_SCAN
        );
    }

    public static boolean isRequestLocationPermissionGranted(final int requestCode, final String[] permissions, final int[] grantResults) {
        if (requestCode != REQUEST_PERMISSION_BLE_SCAN) {
            return false;
        }

        String[] recommendedScanRuntimePermissions = getRecommendedScanRuntimePermissions();
        for (int i = 0; i < permissions.length; i++) {
            for (String recommendedScanRuntimePermission : recommendedScanRuntimePermissions) {
                if (permissions[i].equals(recommendedScanRuntimePermission) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断是否开启定位服务
     *
     * @param activity
     * @return
     */
    public static boolean isOpenLocation(Activity activity) {
        LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetwork = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return isGPS || isNetwork;
    }

    /**
     * 开启定位服务
     *
     * @param activity
     */
    public static void openLocation(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivity(intent);
    }
}
