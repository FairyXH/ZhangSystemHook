package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
public class GnssPositionMode {
    private final boolean mLowPowerMode;
    private final int mMinInterval;
    private final int mMode;
    private final int mPreferredAccuracy;
    private final int mPreferredTime;
    private final int mRecurrence;

    public GnssPositionMode(int mode, int recurrence, int minInterval, int preferredAccuracy, int preferredTime, boolean lowPowerMode) {
        this.mMode = mode;
        this.mRecurrence = recurrence;
        this.mMinInterval = minInterval;
        this.mPreferredAccuracy = preferredAccuracy;
        this.mPreferredTime = preferredTime;
        this.mLowPowerMode = lowPowerMode;
    }

    public boolean equals(java.lang.Object other) {
        if (!(other instanceof com.android.server.location.gnss.GnssPositionMode)) {
            return false;
        }
        com.android.server.location.gnss.GnssPositionMode that = (com.android.server.location.gnss.GnssPositionMode) other;
        return this.mMode == that.mMode && this.mRecurrence == that.mRecurrence && this.mMinInterval == that.mMinInterval && this.mPreferredAccuracy == that.mPreferredAccuracy && this.mPreferredTime == that.mPreferredTime && this.mLowPowerMode == that.mLowPowerMode && getClass() == that.getClass();
    }

    public int hashCode() {
        return java.util.Arrays.hashCode(new java.lang.Object[]{java.lang.Integer.valueOf(this.mMode), java.lang.Integer.valueOf(this.mRecurrence), java.lang.Integer.valueOf(this.mMinInterval), java.lang.Integer.valueOf(this.mPreferredAccuracy), java.lang.Integer.valueOf(this.mPreferredTime), java.lang.Boolean.valueOf(this.mLowPowerMode), getClass()});
    }
}
