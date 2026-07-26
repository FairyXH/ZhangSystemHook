package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
public class TimeDetectorNetworkTimeHelper extends com.android.server.location.gnss.NetworkTimeHelper {
    static final int MAX_NETWORK_TIME_AGE_MILLIS = 86400000;
    static final int NTP_REFRESH_INTERVAL_MILLIS = 86400000;
    private final android.util.LocalLog mDumpLog = new android.util.LocalLog(10, false);
    private final com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.Environment mEnvironment;
    private final com.android.server.location.gnss.NetworkTimeHelper.InjectTimeCallback mInjectTimeCallback;
    private boolean mNetworkTimeInjected;
    private boolean mPeriodicTimeInjectionEnabled;
    private static final java.lang.String TAG = "TDNetworkTimeHelper";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);

    interface Environment {
        void clearDelayedTimeQueryCallback();

        long elapsedRealtimeMillis();

        com.android.server.timedetector.NetworkTimeSuggestion getLatestNetworkTime();

        void requestDelayedTimeQueryCallback(com.android.server.location.gnss.TimeDetectorNetworkTimeHelper timeDetectorNetworkTimeHelper, long j);

        void requestImmediateTimeQueryCallback(com.android.server.location.gnss.TimeDetectorNetworkTimeHelper timeDetectorNetworkTimeHelper, java.lang.String str);

        void setNetworkTimeUpdateListener(com.android.server.timezonedetector.StateChangeListener stateChangeListener);
    }

    public static boolean isInUse() {
        return true;
    }

    TimeDetectorNetworkTimeHelper(com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.Environment environment, com.android.server.location.gnss.NetworkTimeHelper.InjectTimeCallback injectTimeCallback) {
        this.mInjectTimeCallback = (com.android.server.location.gnss.NetworkTimeHelper.InjectTimeCallback) java.util.Objects.requireNonNull(injectTimeCallback);
        this.mEnvironment = (com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.Environment) java.util.Objects.requireNonNull(environment);
        this.mEnvironment.setNetworkTimeUpdateListener(new com.android.server.timezonedetector.StateChangeListener() { // from class: com.android.server.location.gnss.TimeDetectorNetworkTimeHelper$$ExternalSyntheticLambda0
            @Override // com.android.server.timezonedetector.StateChangeListener
            public final void onChange() {
                this.f$0.onNetworkTimeAvailable();
            }
        });
    }

    @Override // com.android.server.location.gnss.NetworkTimeHelper
    synchronized void setPeriodicTimeInjectionMode(boolean periodicTimeInjectionEnabled) {
        this.mPeriodicTimeInjectionEnabled = periodicTimeInjectionEnabled;
        if (!periodicTimeInjectionEnabled) {
            removePeriodicNetworkTimeQuery();
        }
        java.lang.String reason = "setPeriodicTimeInjectionMode(" + periodicTimeInjectionEnabled + ")";
        this.mEnvironment.requestImmediateTimeQueryCallback(this, reason);
    }

    void onNetworkTimeAvailable() {
        this.mEnvironment.requestImmediateTimeQueryCallback(this, "onNetworkTimeAvailable");
    }

    @Override // com.android.server.location.gnss.NetworkTimeHelper
    void onNetworkAvailable() {
        synchronized (this) {
            if (!this.mNetworkTimeInjected) {
                this.mEnvironment.requestImmediateTimeQueryCallback(this, "onNetworkAvailable");
            }
        }
    }

    @Override // com.android.server.location.gnss.NetworkTimeHelper
    void demandUtcTimeInjection() {
        this.mEnvironment.requestImmediateTimeQueryCallback(this, "demandUtcTimeInjection");
    }

    void delayedQueryAndInjectNetworkTime() {
        queryAndInjectNetworkTime("delayedTimeQueryCallback");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void queryAndInjectNetworkTime(java.lang.String reason) {
        com.android.server.timedetector.NetworkTimeSuggestion latestNetworkTime = this.mEnvironment.getLatestNetworkTime();
        maybeInjectNetworkTime(latestNetworkTime, reason);
        removePeriodicNetworkTimeQuery();
        if (this.mPeriodicTimeInjectionEnabled) {
            java.lang.String debugMsg = "queryAndInjectNtpTime: Scheduling periodic query reason=" + reason + " latestNetworkTime=" + latestNetworkTime + " maxDelayMillis=86400000";
            logToDumpLog(debugMsg);
            this.mEnvironment.requestDelayedTimeQueryCallback(this, 86400000);
        }
    }

    private long calculateTimeSignalAgeMillis(com.android.server.timedetector.NetworkTimeSuggestion networkTimeSuggestion) {
        if (networkTimeSuggestion == null) {
            return Long.MAX_VALUE;
        }
        long suggestionElapsedRealtimeMillis = networkTimeSuggestion.getUnixEpochTime().getElapsedRealtimeMillis();
        long currentElapsedRealtimeMillis = this.mEnvironment.elapsedRealtimeMillis();
        return currentElapsedRealtimeMillis - suggestionElapsedRealtimeMillis;
    }

    private void maybeInjectNetworkTime(com.android.server.timedetector.NetworkTimeSuggestion latestNetworkTime, java.lang.String reason) {
        if (calculateTimeSignalAgeMillis(latestNetworkTime) > 86400000) {
            java.lang.String debugMsg = "maybeInjectNetworkTime: Not injecting latest network time latestNetworkTime=" + latestNetworkTime + " reason=" + reason;
            logToDumpLog(debugMsg);
            return;
        }
        android.app.time.UnixEpochTime unixEpochTime = latestNetworkTime.getUnixEpochTime();
        long unixEpochTimeMillis = unixEpochTime.getUnixEpochTimeMillis();
        long currentTimeMillis = java.lang.System.currentTimeMillis();
        java.lang.String debugMsg2 = "maybeInjectNetworkTime: Injecting latest network time latestNetworkTime=" + latestNetworkTime + " reason=" + reason + " System time offset millis=" + (unixEpochTimeMillis - currentTimeMillis);
        logToDumpLog(debugMsg2);
        long timeReferenceMillis = unixEpochTime.getElapsedRealtimeMillis();
        int uncertaintyMillis = latestNetworkTime.getUncertaintyMillis();
        this.mInjectTimeCallback.injectTime(unixEpochTimeMillis, timeReferenceMillis, uncertaintyMillis);
        this.mNetworkTimeInjected = true;
    }

    @Override // com.android.server.location.gnss.NetworkTimeHelper
    void dump(java.io.PrintWriter pw) {
        pw.println("TimeDetectorNetworkTimeHelper:");
        android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw, "  ");
        ipw.increaseIndent();
        synchronized (this) {
            ipw.println("mPeriodicTimeInjectionEnabled=" + this.mPeriodicTimeInjectionEnabled);
        }
        ipw.println("Debug log:");
        this.mDumpLog.dump(ipw);
    }

    private void logToDumpLog(java.lang.String message) {
        this.mDumpLog.log(message);
        if (DEBUG) {
            android.util.Log.d(TAG, message);
        }
    }

    private void removePeriodicNetworkTimeQuery() {
        this.mEnvironment.clearDelayedTimeQueryCallback();
    }

    static class EnvironmentImpl implements com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.Environment {
        private final android.os.Handler mHandler;
        private final java.lang.Object mScheduledRunnableToken = new java.lang.Object();
        private final java.lang.Object mImmediateRunnableToken = new java.lang.Object();
        private final com.android.server.timedetector.TimeDetectorInternal mTimeDetectorInternal = (com.android.server.timedetector.TimeDetectorInternal) com.android.server.LocalServices.getService(com.android.server.timedetector.TimeDetectorInternal.class);

        EnvironmentImpl(android.os.Looper looper) {
            this.mHandler = new android.os.Handler(looper);
        }

        @Override // com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.Environment
        public long elapsedRealtimeMillis() {
            return android.os.SystemClock.elapsedRealtime();
        }

        @Override // com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.Environment
        public com.android.server.timedetector.NetworkTimeSuggestion getLatestNetworkTime() {
            return this.mTimeDetectorInternal.getLatestNetworkSuggestion();
        }

        @Override // com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.Environment
        public void setNetworkTimeUpdateListener(com.android.server.timezonedetector.StateChangeListener stateChangeListener) {
            this.mTimeDetectorInternal.addNetworkTimeUpdateListener(stateChangeListener);
        }

        @Override // com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.Environment
        public void requestImmediateTimeQueryCallback(final com.android.server.location.gnss.TimeDetectorNetworkTimeHelper helper, final java.lang.String reason) {
            synchronized (this) {
                this.mHandler.removeCallbacksAndMessages(this.mImmediateRunnableToken);
                this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.location.gnss.TimeDetectorNetworkTimeHelper$EnvironmentImpl$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        helper.queryAndInjectNetworkTime(reason);
                    }
                }, this.mImmediateRunnableToken, 0L);
            }
        }

        @Override // com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.Environment
        public void requestDelayedTimeQueryCallback(final com.android.server.location.gnss.TimeDetectorNetworkTimeHelper helper, long delayMillis) {
            synchronized (this) {
                clearDelayedTimeQueryCallback();
                android.os.Handler handler = this.mHandler;
                java.util.Objects.requireNonNull(helper);
                handler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.location.gnss.TimeDetectorNetworkTimeHelper$EnvironmentImpl$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        helper.delayedQueryAndInjectNetworkTime();
                    }
                }, this.mScheduledRunnableToken, delayMillis);
            }
        }

        @Override // com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.Environment
        public synchronized void clearDelayedTimeQueryCallback() {
            this.mHandler.removeCallbacksAndMessages(this.mScheduledRunnableToken);
        }
    }
}
