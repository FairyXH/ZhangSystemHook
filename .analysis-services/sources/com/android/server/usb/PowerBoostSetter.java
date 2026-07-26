package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
public class PowerBoostSetter {
    private static final int POWER_BOOST_TIMEOUT_MS = 15000;
    private static final java.lang.String TAG = "PowerBoostSetter";
    android.os.PowerManagerInternal mPowerManagerInternal;
    java.time.Instant mPreviousTimeout = null;

    PowerBoostSetter() {
        this.mPowerManagerInternal = null;
        this.mPowerManagerInternal = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
    }

    public void boostPower() {
        if (this.mPowerManagerInternal == null) {
            this.mPowerManagerInternal = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
        }
        if (this.mPowerManagerInternal == null) {
            android.util.Log.w(TAG, "PowerManagerInternal null");
        } else if (this.mPreviousTimeout == null || java.time.Instant.now().isAfter(this.mPreviousTimeout.plusMillis(7500L))) {
            this.mPreviousTimeout = java.time.Instant.now();
            this.mPowerManagerInternal.setPowerBoost(0, 15000);
        }
    }
}
