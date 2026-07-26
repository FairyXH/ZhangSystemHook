package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class NetPluginDelegate {
    private static final boolean LOGV = true;
    private static final java.lang.String TAG = "NetPluginDelegate";
    private static java.lang.Class tcpBufferRelay = null;
    private static java.lang.Object tcpBufferManagerObj = null;
    private static boolean extJarAvail = true;
    private static java.lang.Class vendorPropRelay = null;
    private static java.lang.Object vendorPropManagerObj = null;
    private static boolean vendorPropJarAvail = true;

    public static java.lang.String get5GTcpBuffers(java.lang.String currentTcpBuffer, android.net.NetworkSpecifier sepcifier) {
        android.util.Slog.v(TAG, "get5GTcpBuffers");
        if (!extJarAvail || !loadConnExtJar()) {
            return currentTcpBuffer;
        }
        try {
            java.lang.Object ret = tcpBufferRelay.getMethod("get5GTcpBuffers", java.lang.String.class, android.net.NetworkSpecifier.class).invoke(tcpBufferManagerObj, currentTcpBuffer, sepcifier);
            if (ret == null || !(ret instanceof java.lang.String)) {
                return currentTcpBuffer;
            }
            java.lang.String tcpBuffer = (java.lang.String) ret;
            return tcpBuffer;
        } catch (java.lang.NoSuchMethodException | java.lang.SecurityException | java.lang.reflect.InvocationTargetException e) {
            android.util.Log.w(TAG, "Failed to invoke get5GTcpBuffers()");
            e.printStackTrace();
            extJarAvail = false;
            return currentTcpBuffer;
        } catch (java.lang.Exception e2) {
            android.util.Log.w(TAG, "Error calling get5GTcpBuffers Method on extension jar");
            e2.printStackTrace();
            extJarAvail = false;
            return currentTcpBuffer;
        }
    }

    public static void registerHandler(android.os.Handler mHandler) {
        android.util.Slog.v(TAG, "registerHandler");
        if (!extJarAvail || !loadConnExtJar()) {
            return;
        }
        try {
            tcpBufferRelay.getMethod("registerHandler", android.os.Handler.class).invoke(tcpBufferManagerObj, mHandler);
        } catch (java.lang.NoSuchMethodException | java.lang.SecurityException | java.lang.reflect.InvocationTargetException e) {
            android.util.Log.w(TAG, "Failed to call registerHandler");
            e.printStackTrace();
            extJarAvail = false;
        } catch (java.lang.Exception e2) {
            android.util.Log.w(TAG, "Error calling registerHandler Method on extension jar");
            e2.printStackTrace();
            extJarAvail = false;
        }
    }

    private static synchronized boolean loadConnExtJar() {
        java.lang.String realProviderPath = android.os.Environment.getSystemExtDirectory().getAbsolutePath() + "/framework/ConnectivityExt.jar";
        if (tcpBufferRelay != null && tcpBufferManagerObj != null) {
            return true;
        }
        extJarAvail = new java.io.File(realProviderPath).exists();
        if (!extJarAvail) {
            android.util.Log.w(TAG, "ConnectivityExt jar file not present");
            return false;
        }
        if (tcpBufferRelay == null && tcpBufferManagerObj == null) {
            android.util.Slog.v(TAG, "loading ConnectivityExt jar");
            try {
                try {
                    dalvik.system.PathClassLoader classLoader = new dalvik.system.PathClassLoader(realProviderPath, java.lang.ClassLoader.getSystemClassLoader());
                    tcpBufferRelay = classLoader.loadClass("com.qualcomm.qti.net.connextension.TCPBufferManager");
                    tcpBufferManagerObj = tcpBufferRelay.newInstance();
                    android.util.Slog.v(TAG, "ConnectivityExt jar loaded");
                } catch (java.lang.Exception e) {
                    android.util.Log.w(TAG, "unable to load ConnectivityExt jar");
                    e.printStackTrace();
                    extJarAvail = false;
                    return false;
                }
            } catch (java.lang.ClassNotFoundException | java.lang.IllegalAccessException | java.lang.InstantiationException e2) {
                android.util.Log.w(TAG, "Failed to find, instantiate or access ConnectivityExt jar ");
                e2.printStackTrace();
                extJarAvail = false;
                return false;
            }
        }
        return true;
    }
}
