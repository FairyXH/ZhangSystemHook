package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class OplusDisplayModeProxy {
    private static com.android.server.wm.OplusDisplayModeProxy sInstance;
    private static final java.lang.Object sLock = new java.lang.Object();

    public interface OnDisplayResolutionChangeListener {
        void onDisplayResolutionChange();
    }

    protected OplusDisplayModeProxy() {
    }

    public static com.android.server.wm.OplusDisplayModeProxy getInstance(android.content.Context context) {
        if (sInstance == null) {
            synchronized (sLock) {
                if (sInstance == null) {
                    sInstance = newInstance(context);
                }
            }
        }
        return sInstance;
    }

    public static com.android.server.wm.OplusDisplayModeProxy newInstance(android.content.Context context) {
        try {
            com.android.server.wm.OplusDisplayModeProxy instance = (com.android.server.wm.OplusDisplayModeProxy) java.lang.Class.forName("com.android.server.wm.OplusDisplayModeProxyImpl").getDeclaredConstructor(new java.lang.Class[0]).newInstance(new java.lang.Object[0]);
            return instance;
        } catch (java.lang.Exception e) {
            com.android.server.wm.OplusDisplayModeProxy instance2 = new com.android.server.wm.OplusDisplayModeProxy();
            return instance2;
        }
    }

    public int getDefaultDisplayWidth() {
        return 0;
    }

    public int getDefaultDisplayHeight() {
        return 0;
    }

    public void registerOnDisplayResolutionChangeListener(com.android.server.wm.OplusDisplayModeProxy.OnDisplayResolutionChangeListener l) {
    }
}
