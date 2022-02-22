package com.example.tulipsante.utils;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class PermissionUtils {
    private static String[] bt_permissions;

    private static String[] init_permissions = new String[] { "android.permission.BLUETOOTH", "android.permission.BLUETOOTH_ADMIN", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.WRITE_EXTERNAL_STORAGE" };

    static {
        bt_permissions = new String[] { "android.permission.BLUETOOTH", "android.permission.BLUETOOTH_ADMIN", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION" };
    }

    public static void initPermission(Activity paramActivity) {
        ArrayList<String> arrayList = new ArrayList();
        if (Build.VERSION.SDK_INT >= 23) {
            arrayList.clear();
            int i = 0;
            while (true) {
                String[] arrayOfString = init_permissions;
                if (i < arrayOfString.length) {
                    if (ContextCompat.checkSelfPermission(paramActivity, arrayOfString[i]) != 0)
                        arrayList.add(init_permissions[i]);
                    i++;
                    continue;
                }
                if (arrayList.size() > 0)
                    ActivityCompat.requestPermissions(paramActivity, init_permissions, 100);
                break;
            }
        }
    }

    public static boolean isBtPermissionEnable(Context paramContext) {
        return !(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(paramContext, "android.permission.BLUETOOTH_ADMIN") != 0);
    }

    public static boolean isBtPermissionsReady(Context paramContext) {
        if (Build.VERSION.SDK_INT >= 23) {
            (new ArrayList()).clear();
            int i = 0;
            while (true) {
                String[] arrayOfString = bt_permissions;
                if (i < arrayOfString.length) {
                    if (ContextCompat.checkSelfPermission(paramContext, arrayOfString[i]) != 0)
                        return false;
                    i++;
                    continue;
                }
                break;
            }
        }
        return true;
    }

    public static boolean isLocationEnable(Context paramContext) {
        return (Build.VERSION.SDK_INT >= 23) ? (((LocationManager)paramContext.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled("gps")) : true;
    }

    public static boolean isLocationPermissionEnable(Context paramContext) {
        return !(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(paramContext, "android.permission.ACCESS_COARSE_LOCATION") != 0);
    }

    public static boolean isStoragePermissionEnable(Context paramContext) {
        return !(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(paramContext, "android.permission.WRITE_EXTERNAL_STORAGE") != 0);
    }
}
