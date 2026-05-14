package com.cocos.game;

import android.os.Bundle;
import android.content.Intent;
import android.content.res.Configuration;

import com.cocos.service.SDKWrapper;
import com.cocos.lib.CocosActivity;

import io.appmetrica.analytics.AppMetrica;
import io.appmetrica.analytics.AppMetricaConfig;

public class AppActivity extends CocosActivity {

    private static AppActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // DO OTHER INITIALIZATION BELOW
        SDKWrapper.shared().init(this);

        instance = this;

        AppMetricaConfig config =
                AppMetricaConfig.newConfigBuilder("d98e2576-3ac8-43e8-8ecf-ef546d4d571f").build();

        AppMetrica.activate(this, config);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SDKWrapper.shared().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SDKWrapper.shared().onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Workaround in https://stackoverflow.com/questions/16283079/re-launch-of-activity-on-home-button-but-only-the-first-time/16447508
        if (!isTaskRoot()) {
            return;
        }
        SDKWrapper.shared().onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SDKWrapper.shared().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SDKWrapper.shared().onNewIntent(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SDKWrapper.shared().onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SDKWrapper.shared().onStop();
    }

    @Override
    public void onBackPressed() {
        SDKWrapper.shared().onBackPressed();
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        SDKWrapper.shared().onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        SDKWrapper.shared().onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        SDKWrapper.shared().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        SDKWrapper.shared().onStart();
        super.onStart();
    }

    @Override
    public void onLowMemory() {
        SDKWrapper.shared().onLowMemory();
        super.onLowMemory();
    }

    public static AppActivity getInstance() {
        return instance;
    }

    public static void sendEmail(String email, String subject, String body) {
        AppActivity activity = AppActivity.getInstance();

        try {
            String fullBody = body + "\n" + getDeviceInfo();

            String uriText = "mailto:" + email +
                    "?subject=" + android.net.Uri.encode(subject) +
                    "&body=" + android.net.Uri.encode(fullBody);

            android.net.Uri uri = android.net.Uri.parse(uriText);

            android.content.Intent intent =
                    new android.content.Intent(android.content.Intent.ACTION_SENDTO);

            intent.setData(uri);

            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getDeviceInfo() {
        AppActivity activity = AppActivity.getInstance();
        StringBuilder info = new StringBuilder();

        info.append("deviceModel ")
                .append(android.os.Build.MANUFACTURER)
                .append(" ")
                .append(android.os.Build.MODEL)
                .append("\n");

        info.append("operatingSystem Android OS ")
                .append(android.os.Build.VERSION.RELEASE)
                .append(" / API-")
                .append(android.os.Build.VERSION.SDK_INT)
                .append(" (")
                .append(android.os.Build.DISPLAY)
                .append(")\n");

        String deviceName = android.os.Build.MODEL;
        try {
            String name = android.provider.Settings.Global.getString(
                    activity.getContentResolver(), "device_name");
            if (name != null && !name.isEmpty()) {
                deviceName = name;
            }
        } catch (Exception ignored) {
        }
        info.append("deviceName ").append(deviceName).append("\n");

        String glRenderer = "unknown";
        String glVersion = "unknown";
        try {
            android.app.ActivityManager am =
                    (android.app.ActivityManager) activity.getSystemService(android.content.Context.ACTIVITY_SERVICE);
            android.content.pm.ConfigurationInfo ci = am.getDeviceConfigurationInfo();
            glVersion = "OpenGL ES " + ci.getGlEsVersion();
        } catch (Exception ignored) {
        }
        info.append("graphicsDeviceName ").append(glRenderer).append("\n");
        info.append("graphicsMemorySize 0\n");
        info.append("graphicsDeviceVersion ").append(glVersion).append("\n");

        info.append("processorCount ")
                .append(Runtime.getRuntime().availableProcessors())
                .append("\n");

        try {
            android.app.ActivityManager am =
                    (android.app.ActivityManager) activity.getSystemService(android.content.Context.ACTIVITY_SERVICE);
            android.app.ActivityManager.MemoryInfo mi = new android.app.ActivityManager.MemoryInfo();
            am.getMemoryInfo(mi);
            info.append("systemMemorySize ").append(mi.totalMem / (1024 * 1024)).append("\n");
        } catch (Exception e) {
            info.append("systemMemorySize 0\n");
        }

        String androidId = "unknown";
        try {
            androidId = android.provider.Settings.Secure.getString(
                    activity.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
        } catch (Exception ignored) {
        }
        info.append("deviceUniqueIdentifier ").append(androidId).append("\n");

        return info.toString();
    }

    public static void openStore(String packageName) {
        AppActivity activity = AppActivity.getInstance();

        try {
            android.net.Uri uri = android.net.Uri.parse("market://details?id=" + packageName);
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);

        } catch (android.content.ActivityNotFoundException e) {
            android.net.Uri uri = android.net.Uri.parse(
                    "https://play.google.com/store/apps/details?id=" + packageName
            );

            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);
        }
    }

    public static String getInstaller() {
        AppActivity activity = AppActivity.getInstance();

        try {

            android.content.pm.PackageManager pm = activity.getPackageManager();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                android.content.pm.InstallSourceInfo info =
                        pm.getInstallSourceInfo(activity.getPackageName());
                return info.getInstallingPackageName();
            } else {
                return pm.getInstallerPackageName(activity.getPackageName());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void reportEvent(String name) {
        AppMetrica.reportEvent(name);
    }

    public static void openUrl(String url) {
        AppActivity activity = AppActivity.getInstance();

        try {
            android.net.Uri uri = android.net.Uri.parse(url);
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);

        } catch (android.content.ActivityNotFoundException e) {
            android.net.Uri uri = android.net.Uri.parse(url);

            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);
        }
    }
}
