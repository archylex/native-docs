package com.cocos.game;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

public class AppQuality {

    private static final String TAG = "AppQuality";
    private static final String PREFS_NAME = "app_quality_prefs";
    private static final String OVERRIDE_KEY = "override_quality";
    private static final int MIN_OS_MAJOR_FOR_HD = 10;
    private static final long MIN_RAM_MB_FOR_HD = 2048L;
    private static final int QUALITY_SD = 1;
    private static final int QUALITY_HD = 2;

    public static int getQuality() {
        int override = getOverrideQuality();
        int result = (override == QUALITY_SD || override == QUALITY_HD)
                ? override
                : getDeviceQuality();
        Log.i(TAG, "getQuality=" + result + " (override=" + override + ")");
        return result;
    }

    public static void setOverrideQuality(int q) {
        SharedPreferences prefs = getPrefs();
        if (prefs == null) {
            return;
        }
        prefs.edit().putInt(OVERRIDE_KEY, q).apply();
        Log.i(TAG, "setOverrideQuality=" + q);
    }

    private static int getOverrideQuality() {
        SharedPreferences prefs = getPrefs();
        return prefs == null ? 0 : prefs.getInt(OVERRIDE_KEY, 0);
    }

    private static SharedPreferences getPrefs() {
        AppActivity activity = AppActivity.getInstance();
        if (activity == null) {
            Log.w(TAG, "AppActivity.instance == null");
            return null;
        }
        return activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private static int getDeviceQuality() {
        long ramMb = getTotalMemoryMb();
        int osMajor = getOsMajorVersion();
        boolean isOld = (ramMb > 0 && ramMb <= MIN_RAM_MB_FOR_HD)
                || (osMajor > 0 && osMajor < MIN_OS_MAJOR_FOR_HD);
        Log.i(TAG, "deviceQuality ram=" + ramMb + "MB osMajor=" + osMajor + " isOld=" + isOld);
        return isOld ? QUALITY_SD : QUALITY_HD;
    }

    private static long getTotalMemoryMb() {
        try {
            AppActivity activity = AppActivity.getInstance();
            if (activity == null) {
                return 0L;
            }
            ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(mi);
            return mi.totalMem / (1024L * 1024L);
        } catch (Exception e) {
            return 0L;
        }
    }

    private static int getOsMajorVersion() {
        try {
            String release = Build.VERSION.RELEASE;
            if (release == null || release.isEmpty()) {
                return 0;
            }
            int dot = release.indexOf('.');
            String major = dot >= 0 ? release.substring(0, dot) : release;
            return Integer.parseInt(major);
        } catch (Exception e) {
            return 0;
        }
    }
}
