package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
class RateEstimator {
    private static final double MINIMUM_DT = 5.0E-4d;
    private static final double RATE_ALPHA = 0.7d;
    private double mInterarrivalTime = 1000.0d;
    private java.lang.Long mLastEventTime;

    public void update(long now) {
        if (this.mLastEventTime != null) {
            this.mInterarrivalTime = getInterarrivalEstimate(now);
        }
        this.mLastEventTime = java.lang.Long.valueOf(now);
    }

    public float getRate(long now) {
        if (this.mLastEventTime == null) {
            return 0.0f;
        }
        return (float) (1.0d / getInterarrivalEstimate(now));
    }

    private double getInterarrivalEstimate(long now) {
        double dt = (now - this.mLastEventTime.longValue()) / 1000.0d;
        return (this.mInterarrivalTime * RATE_ALPHA) + (0.30000000000000004d * java.lang.Math.max(dt, MINIMUM_DT));
    }
}
