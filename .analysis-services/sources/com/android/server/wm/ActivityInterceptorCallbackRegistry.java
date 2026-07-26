package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
@android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
public class ActivityInterceptorCallbackRegistry {
    private static final com.android.server.wm.ActivityInterceptorCallbackRegistry sInstance = new com.android.server.wm.ActivityInterceptorCallbackRegistry();

    private ActivityInterceptorCallbackRegistry() {
    }

    public static com.android.server.wm.ActivityInterceptorCallbackRegistry getInstance() {
        return sInstance;
    }

    public void registerActivityInterceptorCallback(int mainlineOrderId, com.android.server.wm.ActivityInterceptorCallback callback) {
        if (getCallingUid() != 1000) {
            throw new java.lang.SecurityException("Only system server can register ActivityInterceptorCallback");
        }
        if (!com.android.server.wm.ActivityInterceptorCallback.isValidMainlineOrderId(mainlineOrderId)) {
            throw new java.lang.IllegalArgumentException("id is not in the mainline modules range, please useActivityTaskManagerInternal.registerActivityStartInterceptor(OrderedId, ActivityInterceptorCallback) instead.");
        }
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("The passed ActivityInterceptorCallback can not be null");
        }
        com.android.server.wm.ActivityTaskManagerInternal activityTaskManagerInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
        activityTaskManagerInternal.registerActivityStartInterceptor(mainlineOrderId, callback);
    }

    public void unregisterActivityInterceptorCallback(int mainlineOrderId) {
        if (getCallingUid() != 1000) {
            throw new java.lang.SecurityException("Only system server can register ActivityInterceptorCallback");
        }
        if (!com.android.server.wm.ActivityInterceptorCallback.isValidMainlineOrderId(mainlineOrderId)) {
            throw new java.lang.IllegalArgumentException("id is not in the mainline modules range, please useActivityTaskManagerInternal.unregisterActivityStartInterceptor(OrderedId) instead.");
        }
        com.android.server.wm.ActivityTaskManagerInternal activityTaskManagerInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
        activityTaskManagerInternal.unregisterActivityStartInterceptor(mainlineOrderId);
    }

    int getCallingUid() {
        return android.os.Binder.getCallingUid();
    }
}
