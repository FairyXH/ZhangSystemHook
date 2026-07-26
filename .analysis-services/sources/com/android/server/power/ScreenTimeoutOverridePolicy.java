package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
final class ScreenTimeoutOverridePolicy {
    public static final int RELEASE_REASON_NON_INTERACTIVE = 1;
    public static final int RELEASE_REASON_SCREEN_LOCK = 2;
    public static final int RELEASE_REASON_UNKNOWN = -1;
    public static final int RELEASE_REASON_USER_ACTIVITY_ACCESSIBILITY = 7;
    public static final int RELEASE_REASON_USER_ACTIVITY_ATTENTION = 3;
    public static final int RELEASE_REASON_USER_ACTIVITY_BUTTON = 5;
    public static final int RELEASE_REASON_USER_ACTIVITY_OTHER = 4;
    public static final int RELEASE_REASON_USER_ACTIVITY_TOUCH = 6;
    private static final java.lang.String TAG = "ScreenTimeoutOverridePolicy";
    private int mLastAutoReleaseReason = -1;
    private com.android.server.power.ScreenTimeoutOverridePolicy.PolicyCallback mPolicyCallback;
    private long mScreenTimeoutOverrideConfig;

    interface PolicyCallback {
        void releaseAllScreenTimeoutOverrideWakelocks(int i);
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ReleaseReason {
    }

    ScreenTimeoutOverridePolicy(android.content.Context context, long minimumScreenOffTimeoutConfig, com.android.server.power.ScreenTimeoutOverridePolicy.PolicyCallback callback) {
        this.mScreenTimeoutOverrideConfig = context.getResources().getInteger(android.R.integer.config_reduceBrightColorsStrengthMax);
        if (this.mScreenTimeoutOverrideConfig < minimumScreenOffTimeoutConfig) {
            android.util.Slog.w(TAG, "Screen timeout override is smaller than the minimum timeout : " + this.mScreenTimeoutOverrideConfig);
            this.mScreenTimeoutOverrideConfig = -1L;
        }
        this.mPolicyCallback = callback;
    }

    long getScreenTimeoutOverrideLocked(int wakeLockSummary, long screenOffTimeout) {
        if (!isWakeLockAcquired(wakeLockSummary) || this.mScreenTimeoutOverrideConfig < 0) {
            return screenOffTimeout;
        }
        return java.lang.Math.min(this.mScreenTimeoutOverrideConfig, screenOffTimeout);
    }

    void onUserActivity(int wakeLockSummary, int event) {
        if (!isWakeLockAcquired(wakeLockSummary)) {
        }
        switch (event) {
            case 0:
                releaseAllWakeLocks(4);
                break;
            case 1:
                releaseAllWakeLocks(5);
                break;
            case 2:
                releaseAllWakeLocks(6);
                break;
            case 3:
                releaseAllWakeLocks(7);
                break;
            case 4:
                releaseAllWakeLocks(3);
                break;
        }
    }

    void checkScreenWakeLock(int wakeLockSummary) {
        if (isWakeLockAcquired(wakeLockSummary) && (wakeLockSummary & 14) != 0) {
            releaseAllWakeLocks(2);
        }
    }

    void onWakefulnessChange(int wakeLockSummary, int globalWakefulness) {
        if (isWakeLockAcquired(wakeLockSummary) && globalWakefulness != 1) {
            releaseAllWakeLocks(1);
        }
    }

    private boolean isWakeLockAcquired(int wakeLockSummary) {
        return (wakeLockSummary & 256) != 0;
    }

    private void logReleaseReason() {
        android.util.Slog.i(TAG, "Releasing all screen timeout override wake lock. (reason=" + this.mLastAutoReleaseReason + ")");
    }

    private void releaseAllWakeLocks(int reason) {
        this.mPolicyCallback.releaseAllScreenTimeoutOverrideWakelocks(reason);
        this.mLastAutoReleaseReason = reason;
        logReleaseReason();
    }

    void dump(java.io.PrintWriter pw) {
        android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw, "  ");
        ipw.println();
        ipw.println("ScreenTimeoutOverridePolicy:");
        ipw.increaseIndent();
        ipw.println("mScreenTimeoutOverrideConfig=" + this.mScreenTimeoutOverrideConfig);
        ipw.println("mLastAutoReleaseReason=" + this.mLastAutoReleaseReason);
    }
}
