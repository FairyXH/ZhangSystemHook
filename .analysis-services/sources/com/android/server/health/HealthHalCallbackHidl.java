package com.android.server.health;

/* JADX INFO: loaded from: classes2.dex */
class HealthHalCallbackHidl extends android.hardware.health.V2_1.IHealthInfoCallback.Stub implements com.android.server.health.HealthServiceWrapperHidl.Callback {
    private static final java.lang.String TAG = com.android.server.health.HealthHalCallbackHidl.class.getSimpleName();
    private com.android.server.health.HealthInfoCallback mCallback;

    private static void traceBegin(java.lang.String name) {
        android.os.Trace.traceBegin(524288L, name);
    }

    private static void traceEnd() {
        android.os.Trace.traceEnd(524288L);
    }

    HealthHalCallbackHidl(com.android.server.health.HealthInfoCallback callback) {
        this.mCallback = callback;
    }

    @Override // android.hardware.health.V2_0.IHealthInfoCallback
    public void healthInfoChanged(android.hardware.health.V2_0.HealthInfo props) {
        android.hardware.health.V2_1.HealthInfo propsLatest = new android.hardware.health.V2_1.HealthInfo();
        propsLatest.legacy = props;
        propsLatest.batteryCapacityLevel = -1;
        propsLatest.batteryChargeTimeToFullNowSeconds = -1L;
        this.mCallback.update(android.hardware.health.Translate.h2aTranslate(propsLatest));
    }

    @Override // android.hardware.health.V2_1.IHealthInfoCallback
    public void healthInfoChanged_2_1(android.hardware.health.V2_1.HealthInfo props) {
        this.mCallback.update(android.hardware.health.Translate.h2aTranslate(props));
    }

    @Override // com.android.server.health.HealthServiceWrapperHidl.Callback
    public void onRegistration(android.hardware.health.V2_0.IHealth oldService, android.hardware.health.V2_0.IHealth newService, java.lang.String instance) {
        int r;
        if (newService == null) {
            return;
        }
        traceBegin("HealthUnregisterCallback");
        if (oldService != null) {
            try {
                try {
                    int r2 = oldService.unregisterCallback(this);
                    if (r2 != 0) {
                        android.util.Slog.w(TAG, "health: cannot unregister previous callback: " + android.hardware.health.V2_0.Result.toString(r2));
                    }
                } catch (android.os.RemoteException ex) {
                    android.util.Slog.w(TAG, "health: cannot unregister previous callback (transaction error): " + ex.getMessage());
                }
            } finally {
            }
        }
        traceEnd();
        traceBegin("HealthRegisterCallback");
        try {
            try {
                r = newService.registerCallback(this);
            } catch (android.os.RemoteException ex2) {
                android.util.Slog.e(TAG, "health: cannot register callback (transaction error): " + ex2.getMessage());
            }
            if (r != 0) {
                android.util.Slog.w(TAG, "health: cannot register callback: " + android.hardware.health.V2_0.Result.toString(r));
            } else {
                newService.update();
            }
        } finally {
        }
    }
}
