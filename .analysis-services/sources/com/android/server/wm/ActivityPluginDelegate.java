package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityPluginDelegate {
    private static final java.lang.String FOREGROUND_ACTIVITY_TRIGGER = "foreground_activity_trigger";
    private static final boolean LOGV = false;
    private static final int MAX_CONNECT_RETRIES = 15;
    private static final java.lang.String TAG = "ActivityPluginDelegate";
    private static java.lang.Class activityServiceClass = null;
    private static java.lang.Object activityServiceObj = null;
    private static boolean extJarAvail = true;
    static int mGetFeatureEnableRetryCount = 15;
    static boolean isEnabled = false;

    public static void activityInvokeNotification(java.lang.String appName, boolean isFullScreen) {
        if (!getFeatureFlag() || !extJarAvail || !loadActivityExtJar()) {
            return;
        }
        try {
            activityServiceClass.getMethod("sendActivityInvokeNotification", java.lang.String.class, java.lang.Boolean.TYPE).invoke(activityServiceObj, appName, java.lang.Boolean.valueOf(isFullScreen));
        } catch (java.lang.NoSuchMethodException | java.lang.SecurityException | java.lang.reflect.InvocationTargetException e) {
        } catch (java.lang.Exception e2) {
        }
    }

    public static void activitySuspendNotification(java.lang.String appName, boolean isFullScreen, boolean isBg) {
        if (!getFeatureFlag() || !extJarAvail || !loadActivityExtJar()) {
            return;
        }
        try {
            activityServiceClass.getMethod("sendActivitySuspendNotification", java.lang.String.class, java.lang.Boolean.TYPE, java.lang.Boolean.TYPE).invoke(activityServiceObj, appName, java.lang.Boolean.valueOf(isFullScreen), java.lang.Boolean.valueOf(isBg));
        } catch (java.lang.NoSuchMethodException | java.lang.SecurityException | java.lang.reflect.InvocationTargetException e) {
        } catch (java.lang.Exception e2) {
        }
    }

    private static synchronized boolean loadActivityExtJar() {
        java.lang.String realProviderPath = android.os.Environment.getSystemExtDirectory().getAbsolutePath() + "/framework/ActivityExt.jar";
        if (activityServiceClass != null && activityServiceObj != null) {
            return true;
        }
        boolean zExists = new java.io.File(realProviderPath).exists();
        extJarAvail = zExists;
        if (!zExists) {
            return extJarAvail;
        }
        if (activityServiceClass == null && activityServiceObj == null) {
            try {
                dalvik.system.PathClassLoader classLoader = new dalvik.system.PathClassLoader(realProviderPath, java.lang.ClassLoader.getSystemClassLoader());
                activityServiceClass = classLoader.loadClass("com.qualcomm.qti.activityextension.ActivityNotifier");
                activityServiceObj = activityServiceClass.newInstance();
            } catch (java.lang.ClassNotFoundException | java.lang.IllegalAccessException | java.lang.InstantiationException e) {
                extJarAvail = false;
                return false;
            } catch (java.lang.Exception e2) {
                extJarAvail = false;
                return false;
            }
        }
        return true;
    }

    public static synchronized boolean getFeatureFlag() {
        if (!isEnabled && mGetFeatureEnableRetryCount != 0) {
            isEnabled = android.provider.Settings.Global.getInt(android.app.ActivityThread.currentApplication().getApplicationContext().getContentResolver(), FOREGROUND_ACTIVITY_TRIGGER, 1) == 1;
            mGetFeatureEnableRetryCount--;
            return isEnabled;
        }
        return isEnabled;
    }
}
