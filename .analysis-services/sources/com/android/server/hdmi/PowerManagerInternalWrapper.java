package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class PowerManagerInternalWrapper {
    private static final java.lang.String TAG = "PowerManagerInternalWrapper";
    private android.os.PowerManagerInternal mPowerManagerInternal = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);

    public boolean wasDeviceIdleFor(long ms) {
        return this.mPowerManagerInternal.wasDeviceIdleFor(ms);
    }
}
