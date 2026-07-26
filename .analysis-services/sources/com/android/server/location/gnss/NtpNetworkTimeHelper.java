package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
class NtpNetworkTimeHelper extends com.android.server.location.gnss.NetworkTimeHelper {
    private static final long MAX_RETRY_INTERVAL = 14400000;
    static final long NTP_INTERVAL = 86400000;
    static final long RETRY_INTERVAL = 300000;
    private static final int STATE_IDLE = 2;
    private static final int STATE_PENDING_NETWORK = 0;
    private static final int STATE_RETRIEVING_AND_INJECTING = 1;
    private static final java.lang.String WAKELOCK_KEY = "NtpTimeHelper";
    private static final long WAKELOCK_TIMEOUT_MILLIS = 60000;
    private final com.android.server.location.gnss.NetworkTimeHelper.InjectTimeCallback mCallback;
    private final android.net.ConnectivityManager mConnMgr;
    private final android.util.LocalLog mDumpLog;
    private final android.os.Handler mHandler;
    private int mInjectNtpTimeState;
    private final com.android.server.location.gnss.ExponentialBackOff mNtpBackOff;
    private final android.util.NtpTrustedTime mNtpTime;
    private boolean mPeriodicTimeInjection;
    private final android.os.PowerManager.WakeLock mWakeLock;
    private static final java.lang.String TAG = "NtpNetworkTimeHelper";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);

    NtpNetworkTimeHelper(android.content.Context context, android.os.Looper looper, com.android.server.location.gnss.NetworkTimeHelper.InjectTimeCallback callback, android.util.NtpTrustedTime ntpTime) {
        this.mDumpLog = new android.util.LocalLog(10, false);
        this.mNtpBackOff = new com.android.server.location.gnss.ExponentialBackOff(300000L, 14400000L);
        this.mInjectNtpTimeState = 0;
        this.mConnMgr = (android.net.ConnectivityManager) context.getSystemService("connectivity");
        this.mCallback = callback;
        this.mNtpTime = ntpTime;
        this.mHandler = new android.os.Handler(looper);
        android.os.PowerManager powerManager = (android.os.PowerManager) context.getSystemService("power");
        this.mWakeLock = powerManager.newWakeLock(1, WAKELOCK_KEY);
    }

    NtpNetworkTimeHelper(android.content.Context context, android.os.Looper looper, com.android.server.location.gnss.NetworkTimeHelper.InjectTimeCallback callback) {
        this(context, looper, callback, android.util.NtpTrustedTime.getInstance(context));
    }

    @Override // com.android.server.location.gnss.NetworkTimeHelper
    synchronized void setPeriodicTimeInjectionMode(boolean periodicTimeInjectionEnabled) {
        if (periodicTimeInjectionEnabled) {
            this.mPeriodicTimeInjection = true;
        }
    }

    @Override // com.android.server.location.gnss.NetworkTimeHelper
    void demandUtcTimeInjection() {
        lambda$blockingGetNtpTimeAndInject$0("demandUtcTimeInjection");
    }

    @Override // com.android.server.location.gnss.NetworkTimeHelper
    synchronized void onNetworkAvailable() {
        if (this.mInjectNtpTimeState == 0) {
            lambda$blockingGetNtpTimeAndInject$0("onNetworkAvailable");
        }
    }

    @Override // com.android.server.location.gnss.NetworkTimeHelper
    void dump(java.io.PrintWriter pw) {
        pw.println("NtpNetworkTimeHelper:");
        android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw, "  ");
        ipw.increaseIndent();
        synchronized (this) {
            ipw.println("mInjectNtpTimeState=" + this.mInjectNtpTimeState);
            ipw.println("mPeriodicTimeInjection=" + this.mPeriodicTimeInjection);
            ipw.println("mNtpBackOff=" + this.mNtpBackOff);
        }
        ipw.println("Debug log:");
        ipw.increaseIndent();
        this.mDumpLog.dump(ipw);
        ipw.decreaseIndent();
        ipw.println("NtpTrustedTime:");
        ipw.increaseIndent();
        this.mNtpTime.dump(ipw);
        ipw.decreaseIndent();
    }

    private boolean isNetworkConnected() {
        android.net.NetworkInfo activeNetworkInfo = this.mConnMgr.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: retrieveAndInjectNtpTime, reason: merged with bridge method [inline-methods] */
    public synchronized void lambda$blockingGetNtpTimeAndInject$0(java.lang.String reason) {
        if (this.mInjectNtpTimeState == 1) {
            return;
        }
        if (!isNetworkConnected()) {
            maybeInjectCachedNtpTime(reason + "[Network not connected]");
            this.mInjectNtpTimeState = 0;
        } else {
            this.mInjectNtpTimeState = 1;
            this.mWakeLock.acquire(60000L);
            new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.location.gnss.NtpNetworkTimeHelper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.blockingGetNtpTimeAndInject();
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void blockingGetNtpTimeAndInject() {
        long delayMillis;
        long debugId = android.os.SystemClock.elapsedRealtime();
        boolean refreshSuccess = true;
        android.util.NtpTrustedTime.TimeResult ntpResult = this.mNtpTime.getCachedTimeResult();
        if (ntpResult == null || ntpResult.getAgeMillis() >= 86400000) {
            refreshSuccess = this.mNtpTime.forceRefresh();
        }
        synchronized (this) {
            this.mInjectNtpTimeState = 2;
            java.lang.String injectReason = "blockingGetNtpTimeAndInject:, debugId=" + debugId + ", refreshSuccess=" + refreshSuccess;
            if (maybeInjectCachedNtpTime(injectReason)) {
                delayMillis = 86400000;
                this.mNtpBackOff.reset();
            } else {
                logWarn("maybeInjectCachedNtpTime() returned false");
                delayMillis = this.mNtpBackOff.nextBackoffMillis();
            }
            if (this.mPeriodicTimeInjection || !refreshSuccess) {
                java.lang.String debugMsg = "blockingGetNtpTimeAndInject: Scheduling later NTP retrieval, debugId=" + debugId + ", mPeriodicTimeInjection=" + this.mPeriodicTimeInjection + ", refreshSuccess=" + refreshSuccess + ", delayMillis=" + delayMillis;
                logDebug(debugMsg);
                final java.lang.String reason = "scheduled: debugId=" + debugId;
                this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.location.gnss.NtpNetworkTimeHelper$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$blockingGetNtpTimeAndInject$0(reason);
                    }
                }, delayMillis);
            }
        }
        this.mWakeLock.release();
    }

    private synchronized boolean maybeInjectCachedNtpTime(java.lang.String reason) {
        android.util.NtpTrustedTime.TimeResult ntpResult = this.mNtpTime.getCachedTimeResult();
        if (ntpResult != null && ntpResult.getAgeMillis() < 86400000) {
            final long unixEpochTimeMillis = ntpResult.getTimeMillis();
            long currentTimeMillis = java.lang.System.currentTimeMillis();
            java.lang.String debugMsg = "maybeInjectCachedNtpTime: Injecting latest NTP time, reason=" + reason + ", ntpResult=" + ntpResult + ", System time offset millis=" + (unixEpochTimeMillis - currentTimeMillis);
            logDebug(debugMsg);
            final long timeReferenceMillis = ntpResult.getElapsedRealtimeMillis();
            final int uncertaintyMillis = ntpResult.getUncertaintyMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.location.gnss.NtpNetworkTimeHelper$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$maybeInjectCachedNtpTime$1(unixEpochTimeMillis, timeReferenceMillis, uncertaintyMillis);
                }
            });
            return true;
        }
        java.lang.String debugMsg2 = "maybeInjectCachedNtpTime: Not injecting latest NTP time, reason=" + reason + ", ntpResult=" + ntpResult;
        logDebug(debugMsg2);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeInjectCachedNtpTime$1(long unixEpochTimeMillis, long timeReferenceMillis, int uncertaintyMillis) {
        this.mCallback.injectTime(unixEpochTimeMillis, timeReferenceMillis, uncertaintyMillis);
    }

    private void logWarn(java.lang.String logMsg) {
        this.mDumpLog.log(logMsg);
        android.util.Log.e(TAG, logMsg);
    }

    private void logDebug(java.lang.String debugMsg) {
        this.mDumpLog.log(debugMsg);
        if (DEBUG) {
            android.util.Log.d(TAG, debugMsg);
        }
    }
}
