package com.android.server.health;

/* JADX INFO: loaded from: classes2.dex */
public class HealthRegCallbackAidl {
    private static final java.lang.String TAG = "HealthRegCallbackAidl";
    private final android.hardware.health.IHealthInfoCallback mHalInfoCallback = new com.android.server.health.HealthRegCallbackAidl.HalInfoCallback();
    private final com.android.server.health.HealthInfoCallback mServiceInfoCallback;

    HealthRegCallbackAidl(com.android.server.health.HealthInfoCallback healthInfoCallback) {
        this.mServiceInfoCallback = healthInfoCallback;
    }

    public void onRegistration(android.hardware.health.IHealth oldService, android.hardware.health.IHealth newService) {
        if (this.mServiceInfoCallback == null) {
            return;
        }
        android.os.Trace.traceBegin(524288L, "HealthUnregisterCallbackAidl");
        try {
            unregisterCallback(oldService, this.mHalInfoCallback);
            android.os.Trace.traceEnd(524288L);
            android.os.Trace.traceBegin(524288L, "HealthRegisterCallbackAidl");
            try {
                registerCallback(newService, this.mHalInfoCallback);
            } finally {
            }
        } finally {
        }
    }

    private static void unregisterCallback(android.hardware.health.IHealth oldService, android.hardware.health.IHealthInfoCallback cb) {
        if (oldService == null) {
            return;
        }
        try {
            oldService.unregisterCallback(cb);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "health: cannot unregister previous callback (transaction error): " + e.getMessage());
        }
    }

    private static void registerCallback(android.hardware.health.IHealth newService, android.hardware.health.IHealthInfoCallback cb) {
        try {
            newService.registerCallback(cb);
            try {
                newService.update();
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "health: cannot update after registering health info callback", e);
            }
        } catch (android.os.RemoteException e2) {
            android.util.Slog.e(TAG, "health: cannot register callback, framework may cease to receive updates on health / battery info!", e2);
        }
    }

    private class HalInfoCallback extends android.hardware.health.IHealthInfoCallback.Stub {
        private HalInfoCallback() {
        }

        @Override // android.hardware.health.IHealthInfoCallback
        public void healthInfoChanged(android.hardware.health.HealthInfo healthInfo) throws android.os.RemoteException {
            com.android.server.health.HealthRegCallbackAidl.this.mServiceInfoCallback.update(healthInfo);
        }

        @Override // android.hardware.health.IHealthInfoCallback
        public java.lang.String getInterfaceHash() {
            return "3bab6273a5491102b29c9d7a1f0efa749533f46d";
        }

        @Override // android.hardware.health.IHealthInfoCallback
        public int getInterfaceVersion() {
            return 3;
        }
    }
}
