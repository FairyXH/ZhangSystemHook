package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public class SystemDeviceStationaryHelper extends com.android.server.location.injector.DeviceStationaryHelper {
    private com.android.server.DeviceIdleInternal mDeviceIdle;

    public void onSystemReady() {
        this.mDeviceIdle = (com.android.server.DeviceIdleInternal) java.util.Objects.requireNonNull((com.android.server.DeviceIdleInternal) com.android.server.LocalServices.getService(com.android.server.DeviceIdleInternal.class));
    }

    @Override // com.android.server.location.injector.DeviceStationaryHelper
    public void addListener(com.android.server.DeviceIdleInternal.StationaryListener listener) {
        com.android.internal.util.Preconditions.checkState(this.mDeviceIdle != null);
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mDeviceIdle.registerStationaryListener(listener);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @Override // com.android.server.location.injector.DeviceStationaryHelper
    public void removeListener(com.android.server.DeviceIdleInternal.StationaryListener listener) {
        com.android.internal.util.Preconditions.checkState(this.mDeviceIdle != null);
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mDeviceIdle.unregisterStationaryListener(listener);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }
}
