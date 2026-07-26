package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
class DisplayFoldDurationLogger {
    private static final int LOG_SUBTYPE_DURATION_MASK = Integer.MIN_VALUE;
    private static final int LOG_SUBTYPE_FOLDED = 1;
    private static final int LOG_SUBTYPE_UNFOLDED = 0;
    static final int SCREEN_STATE_OFF = 0;
    static final int SCREEN_STATE_ON_FOLDED = 2;
    static final int SCREEN_STATE_ON_UNFOLDED = 1;
    static final int SCREEN_STATE_UNKNOWN = -1;
    private volatile int mScreenState = -1;
    private volatile java.lang.Long mLastChanged = null;
    private final com.android.internal.logging.MetricsLogger mLogger = new com.android.internal.logging.MetricsLogger();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ScreenState {
    }

    DisplayFoldDurationLogger() {
    }

    void onFinishedWakingUp(java.lang.Boolean folded) {
        if (folded == null) {
            this.mScreenState = -1;
        } else if (folded.booleanValue()) {
            this.mScreenState = 2;
        } else {
            this.mScreenState = 1;
        }
        this.mLastChanged = java.lang.Long.valueOf(android.os.SystemClock.uptimeMillis());
    }

    void onFinishedGoingToSleep() {
        log();
        this.mScreenState = 0;
        this.mLastChanged = null;
    }

    void setDeviceFolded(boolean folded) {
        if (!isOn()) {
            return;
        }
        log();
        this.mScreenState = folded ? 2 : 1;
        this.mLastChanged = java.lang.Long.valueOf(android.os.SystemClock.uptimeMillis());
    }

    void logFocusedAppWithFoldState(boolean z, java.lang.String str) {
        this.mLogger.write(new android.metrics.LogMaker(1594).setType(4).setSubtype(z ? 1 : 0).setPackageName(str));
    }

    private void log() {
        int subtype;
        java.lang.Long lastChanged = this.mLastChanged;
        if (lastChanged == null) {
            return;
        }
        switch (this.mScreenState) {
            case 1:
                subtype = Integer.MIN_VALUE;
                break;
            case 2:
                subtype = -2147483647;
                break;
            default:
                return;
        }
        this.mLogger.write(new android.metrics.LogMaker(1594).setType(4).setSubtype(subtype).setLatency(android.os.SystemClock.uptimeMillis() - lastChanged.longValue()));
    }

    private boolean isOn() {
        return this.mScreenState == 1 || this.mScreenState == 2;
    }
}
