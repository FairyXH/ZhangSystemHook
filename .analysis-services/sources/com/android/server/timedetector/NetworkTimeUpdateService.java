package com.android.server.timedetector;

/* JADX INFO: loaded from: classes3.dex */
public class NetworkTimeUpdateService extends android.os.Binder {
    private static final boolean DBG = false;
    private static final java.lang.String NETWORK_VALIDATED = "network validated";
    private static final java.lang.String REASON_AUTOMATIC_TIME_ENABLE = "automatic time enabled";
    private static final java.lang.String TAG = "NetworkTimeUpdateService";
    private final android.net.ConnectivityManager mCM;
    private final android.content.Context mContext;
    private final com.android.server.timedetector.NetworkTimeUpdateService.Engine mEngine;
    private final android.os.Handler mHandler;
    private final android.util.NtpTrustedTime mNtpTrustedTime;
    private final com.android.server.timedetector.NetworkTimeUpdateService.Engine.RefreshCallbacks mRefreshCallbacks;
    private final android.os.PowerManager.WakeLock mWakeLock;
    private final java.lang.Object mLock = new java.lang.Object();
    private com.android.server.INetworkTimeUpdateServiceExt mNetworkTimeUpdateServiceExt = (com.android.server.INetworkTimeUpdateServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.INetworkTimeUpdateServiceExt.class).create();
    private android.net.Network mDefaultNetwork = null;
    private boolean mHasEverValidatedInternetAccess = false;

    interface Engine {

        public interface RefreshCallbacks {
            void scheduleNextRefresh(long j);

            void submitSuggestion(com.android.server.timedetector.NetworkTimeSuggestion networkTimeSuggestion);
        }

        void dump(java.io.PrintWriter printWriter);

        boolean forceRefreshForTests(android.net.Network network, com.android.server.timedetector.NetworkTimeUpdateService.Engine.RefreshCallbacks refreshCallbacks);

        void refreshAndRescheduleIfRequired(android.net.Network network, java.lang.String str, com.android.server.timedetector.NetworkTimeUpdateService.Engine.RefreshCallbacks refreshCallbacks);
    }

    public NetworkTimeUpdateService(android.content.Context context) {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mCM = (android.net.ConnectivityManager) this.mContext.getSystemService(android.net.ConnectivityManager.class);
        this.mWakeLock = ((android.os.PowerManager) context.getSystemService(android.os.PowerManager.class)).newWakeLock(1, TAG);
        this.mNtpTrustedTime = android.util.NtpTrustedTime.getInstance(context);
        java.util.function.Supplier<java.lang.Long> elapsedRealtimeMillisSupplier = new java.util.function.Supplier() { // from class: com.android.server.timedetector.NetworkTimeUpdateService$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Long.valueOf(android.os.SystemClock.elapsedRealtime());
            }
        };
        int tryAgainTimesMax = this.mContext.getResources().getInteger(android.R.integer.config_networkWakeupPacketMask);
        int normalPollingIntervalMillis = this.mContext.getResources().getInteger(android.R.integer.config_networkPolicyDefaultWarning);
        int shortPollingIntervalMillis = this.mContext.getResources().getInteger(android.R.integer.config_networkWakeupPacketMark);
        this.mEngine = new com.android.server.timedetector.NetworkTimeUpdateService.EngineImpl(elapsedRealtimeMillisSupplier, normalPollingIntervalMillis, shortPollingIntervalMillis, tryAgainTimesMax, this.mNtpTrustedTime);
        final android.app.AlarmManager alarmManager = (android.app.AlarmManager) this.mContext.getSystemService(android.app.AlarmManager.class);
        final com.android.server.timedetector.TimeDetectorInternal timeDetectorInternal = (com.android.server.timedetector.TimeDetectorInternal) com.android.server.LocalServices.getService(com.android.server.timedetector.TimeDetectorInternal.class);
        this.mRefreshCallbacks = new com.android.server.timedetector.NetworkTimeUpdateService.Engine.RefreshCallbacks() { // from class: com.android.server.timedetector.NetworkTimeUpdateService.1
            private final android.app.AlarmManager.OnAlarmListener mOnAlarmListener;

            {
                this.mOnAlarmListener = new com.android.server.timedetector.NetworkTimeUpdateService.ScheduledRefreshAlarmListener();
            }

            @Override // com.android.server.timedetector.NetworkTimeUpdateService.Engine.RefreshCallbacks
            public void scheduleNextRefresh(long elapsedRealtimeMillis) {
                alarmManager.cancel(this.mOnAlarmListener);
                alarmManager.set(3, elapsedRealtimeMillis, "NetworkTimeUpdateService.POLL", this.mOnAlarmListener, null);
            }

            @Override // com.android.server.timedetector.NetworkTimeUpdateService.Engine.RefreshCallbacks
            public void submitSuggestion(com.android.server.timedetector.NetworkTimeSuggestion suggestion) {
                timeDetectorInternal.suggestNetworkTime(suggestion);
            }
        };
        android.os.HandlerThread thread = new android.os.HandlerThread(TAG);
        thread.start();
        this.mHandler = thread.getThreadHandler();
        this.mNetworkTimeUpdateServiceExt.init(this.mContext, this);
    }

    public void systemRunning() {
        this.mNetworkTimeUpdateServiceExt.checkSystemTime();
        android.util.Log.d(TAG, "NetworkTimeUpdateService systemReady");
        com.android.server.timedetector.NetworkTimeUpdateService.NetworkConnectivityCallback networkConnectivityCallback = new com.android.server.timedetector.NetworkTimeUpdateService.NetworkConnectivityCallback();
        this.mCM.registerDefaultNetworkCallback(networkConnectivityCallback, this.mHandler);
        android.content.ContentResolver resolver = this.mContext.getContentResolver();
        com.android.server.timedetector.NetworkTimeUpdateService.AutoTimeSettingObserver autoTimeSettingObserver = new com.android.server.timedetector.NetworkTimeUpdateService.AutoTimeSettingObserver(this.mHandler, this.mContext);
        resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("auto_time"), false, autoTimeSettingObserver);
    }

    void setServerConfigForTests(android.util.NtpTrustedTime.NtpConfig ntpConfig) {
        this.mContext.enforceCallingPermission("android.permission.SET_TIME", "set NTP server config for tests");
        long token = android.os.Binder.clearCallingIdentity();
        try {
            this.mNtpTrustedTime.setServerConfigForTests(ntpConfig);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    boolean forceRefreshForTests() {
        android.net.Network network;
        this.mContext.enforceCallingPermission("android.permission.SET_TIME", "force network time refresh");
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                network = this.mDefaultNetwork;
            }
            if (network != null) {
                return this.mEngine.forceRefreshForTests(network, this.mRefreshCallbacks);
            }
            android.os.Binder.restoreCallingIdentity(token);
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPollNetworkTime(java.lang.String reason) {
        android.net.Network network;
        synchronized (this.mLock) {
            network = this.mDefaultNetwork;
        }
        this.mWakeLock.acquire();
        try {
            this.mEngine.refreshAndRescheduleIfRequired(network, reason, this.mRefreshCallbacks);
        } finally {
            this.mWakeLock.release();
        }
    }

    private class ScheduledRefreshAlarmListener implements android.app.AlarmManager.OnAlarmListener, java.lang.Runnable {
        private ScheduledRefreshAlarmListener() {
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            com.android.server.timedetector.NetworkTimeUpdateService.this.mHandler.post(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            com.android.server.timedetector.NetworkTimeUpdateService.this.onPollNetworkTime("scheduled refresh");
        }
    }

    private class NetworkConnectivityCallback extends android.net.ConnectivityManager.NetworkCallback {
        private NetworkConnectivityCallback() {
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(android.net.Network network) {
            android.util.Log.d(com.android.server.timedetector.NetworkTimeUpdateService.TAG, java.lang.String.format("New default network %s; checking time.", network));
            synchronized (com.android.server.timedetector.NetworkTimeUpdateService.this.mLock) {
                com.android.server.timedetector.NetworkTimeUpdateService.this.mDefaultNetwork = network;
                com.android.server.timedetector.NetworkTimeUpdateService.this.mHasEverValidatedInternetAccess = false;
            }
            com.android.server.timedetector.NetworkTimeUpdateService.this.onPollNetworkTime("network available");
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(android.net.Network network, android.net.NetworkCapabilities networkCapabilities) {
            if (network == null || networkCapabilities == null || !networkCapabilities.hasCapability(12)) {
                return;
            }
            boolean isValidated = networkCapabilities.hasCapability(16) || networkCapabilities.hasCapability(24);
            if (isValidated) {
                boolean isEverValidatedStatusChanged = false;
                synchronized (com.android.server.timedetector.NetworkTimeUpdateService.this.mLock) {
                    if (com.android.server.timedetector.NetworkTimeUpdateService.this.mDefaultNetwork != null && network.equals(com.android.server.timedetector.NetworkTimeUpdateService.this.mDefaultNetwork) && !com.android.server.timedetector.NetworkTimeUpdateService.this.mHasEverValidatedInternetAccess) {
                        com.android.server.timedetector.NetworkTimeUpdateService.this.mHasEverValidatedInternetAccess = isValidated;
                        isEverValidatedStatusChanged = isValidated;
                    }
                }
                if (isEverValidatedStatusChanged) {
                    com.android.server.timedetector.NetworkTimeUpdateService.this.onPollNetworkTime(com.android.server.timedetector.NetworkTimeUpdateService.NETWORK_VALIDATED);
                }
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(android.net.Network network) {
            synchronized (com.android.server.timedetector.NetworkTimeUpdateService.this.mLock) {
                if (network.equals(com.android.server.timedetector.NetworkTimeUpdateService.this.mDefaultNetwork)) {
                    com.android.server.timedetector.NetworkTimeUpdateService.this.mDefaultNetwork = null;
                }
            }
        }
    }

    private class AutoTimeSettingObserver extends android.database.ContentObserver {
        private final android.content.Context mContext;

        AutoTimeSettingObserver(android.os.Handler handler, android.content.Context context) {
            super(handler);
            this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            if (isAutomaticTimeEnabled()) {
                com.android.server.timedetector.NetworkTimeUpdateService.this.onPollNetworkTime(com.android.server.timedetector.NetworkTimeUpdateService.REASON_AUTOMATIC_TIME_ENABLE);
            }
        }

        private boolean isAutomaticTimeEnabled() {
            android.content.ContentResolver resolver = this.mContext.getContentResolver();
            return android.provider.Settings.Global.getInt(resolver, "auto_time", 0) != 0;
        }
    }

    @Override // android.os.Binder
    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            synchronized (this.mLock) {
                pw.println("mDefaultNetwork=" + this.mDefaultNetwork);
            }
            this.mEngine.dump(pw);
            pw.println();
        }
    }

    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        new com.android.server.timedetector.NetworkTimeUpdateServiceShellCommand(this).exec(this, in, out, err, args, callback, resultReceiver);
    }

    static class EngineImpl implements com.android.server.timedetector.NetworkTimeUpdateService.Engine {
        private final java.util.function.Supplier<java.lang.Long> mElapsedRealtimeMillisSupplier;
        private java.lang.Long mLastRefreshAttemptElapsedRealtimeMillis;
        private final android.util.LocalLog mLocalDebugLog = new android.util.LocalLog(30, false);
        private final int mNormalPollingIntervalMillis;
        private final android.util.NtpTrustedTime mNtpTrustedTime;
        private final int mShortPollingIntervalMillis;
        private int mTryAgainCounter;
        private final int mTryAgainTimesMax;

        EngineImpl(java.util.function.Supplier<java.lang.Long> elapsedRealtimeMillisSupplier, int normalPollingIntervalMillis, int shortPollingIntervalMillis, int tryAgainTimesMax, android.util.NtpTrustedTime ntpTrustedTime) {
            this.mElapsedRealtimeMillisSupplier = (java.util.function.Supplier) java.util.Objects.requireNonNull(elapsedRealtimeMillisSupplier);
            if (shortPollingIntervalMillis > normalPollingIntervalMillis) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("shortPollingIntervalMillis (%s) > normalPollingIntervalMillis (%s)", java.lang.Integer.valueOf(shortPollingIntervalMillis), java.lang.Integer.valueOf(normalPollingIntervalMillis)));
            }
            this.mNormalPollingIntervalMillis = normalPollingIntervalMillis;
            this.mShortPollingIntervalMillis = shortPollingIntervalMillis;
            this.mTryAgainTimesMax = tryAgainTimesMax;
            this.mNtpTrustedTime = (android.util.NtpTrustedTime) java.util.Objects.requireNonNull(ntpTrustedTime);
        }

        @Override // com.android.server.timedetector.NetworkTimeUpdateService.Engine
        public boolean forceRefreshForTests(android.net.Network network, com.android.server.timedetector.NetworkTimeUpdateService.Engine.RefreshCallbacks refreshCallbacks) {
            boolean refreshSuccessful = tryRefresh(network);
            logToDebugAndDumpsys("forceRefreshForTests: refreshSuccessful=" + refreshSuccessful);
            if (refreshSuccessful) {
                android.util.NtpTrustedTime.TimeResult cachedTimeResult = this.mNtpTrustedTime.getCachedTimeResult();
                if (cachedTimeResult == null) {
                    logToDebugAndDumpsys("forceRefreshForTests: cachedTimeResult unexpectedly null");
                } else {
                    makeNetworkTimeSuggestion(cachedTimeResult, "EngineImpl.forceRefreshForTests()", refreshCallbacks);
                }
            }
            return refreshSuccessful;
        }

        @Override // com.android.server.timedetector.NetworkTimeUpdateService.Engine
        public void refreshAndRescheduleIfRequired(android.net.Network network, java.lang.String reason, com.android.server.timedetector.NetworkTimeUpdateService.Engine.RefreshCallbacks refreshCallbacks) {
            boolean shouldAttemptRefresh;
            boolean refreshSuccessful;
            int i;
            long nextRefreshElapsedRealtimeMillis;
            if (network == null) {
                logToDebugAndDumpsys("refreshIfRequiredAndReschedule: reason=" + reason + ": No default network available. No refresh attempted and no next attempt scheduled.");
                return;
            }
            android.util.NtpTrustedTime.TimeResult initialTimeResult = this.mNtpTrustedTime.getCachedTimeResult();
            synchronized (this) {
                long currentElapsedRealtimeMillis = this.mElapsedRealtimeMillisSupplier.get().longValue();
                long timeResultAgeMillis = calculateTimeResultAgeMillis(initialTimeResult, currentElapsedRealtimeMillis);
                boolean shouldAttemptRefresh2 = timeResultAgeMillis >= ((long) this.mNormalPollingIntervalMillis) && isRefreshAllowed(currentElapsedRealtimeMillis);
                if (!shouldAttemptRefresh2 && this.mTryAgainCounter == 0 && timeResultAgeMillis >= this.mNormalPollingIntervalMillis && reason != null && com.android.server.timedetector.NetworkTimeUpdateService.NETWORK_VALIDATED.equals(reason)) {
                    shouldAttemptRefresh2 = true;
                }
                if (!com.android.server.timedetector.NetworkTimeUpdateService.REASON_AUTOMATIC_TIME_ENABLE.equals(reason)) {
                    shouldAttemptRefresh = shouldAttemptRefresh2;
                } else {
                    shouldAttemptRefresh = true;
                }
            }
            if (!shouldAttemptRefresh) {
                refreshSuccessful = false;
            } else {
                boolean refreshSuccessful2 = tryRefresh(network);
                refreshSuccessful = refreshSuccessful2;
            }
            synchronized (this) {
                android.util.NtpTrustedTime.TimeResult latestTimeResult = this.mNtpTrustedTime.getCachedTimeResult();
                long currentElapsedRealtimeMillis2 = this.mElapsedRealtimeMillisSupplier.get().longValue();
                long latestTimeResultAgeMillis = calculateTimeResultAgeMillis(latestTimeResult, currentElapsedRealtimeMillis2);
                if (shouldAttemptRefresh) {
                    if (refreshSuccessful) {
                        this.mTryAgainCounter = 0;
                    } else if (this.mTryAgainTimesMax >= 0) {
                        this.mTryAgainCounter++;
                        if (this.mTryAgainCounter > this.mTryAgainTimesMax) {
                            this.mTryAgainCounter = 0;
                        }
                    } else {
                        this.mTryAgainCounter = 1;
                    }
                }
                if (latestTimeResultAgeMillis < this.mNormalPollingIntervalMillis) {
                    this.mTryAgainCounter = 0;
                }
                if (latestTimeResultAgeMillis < this.mNormalPollingIntervalMillis) {
                    makeNetworkTimeSuggestion(latestTimeResult, reason, refreshCallbacks);
                }
                if (this.mTryAgainCounter <= 0) {
                    i = this.mNormalPollingIntervalMillis;
                } else {
                    i = this.mShortPollingIntervalMillis;
                }
                long refreshAttemptDelayMillis = i;
                if (latestTimeResultAgeMillis < refreshAttemptDelayMillis) {
                    nextRefreshElapsedRealtimeMillis = latestTimeResult.getElapsedRealtimeMillis() + refreshAttemptDelayMillis;
                } else if (this.mLastRefreshAttemptElapsedRealtimeMillis != null) {
                    nextRefreshElapsedRealtimeMillis = this.mLastRefreshAttemptElapsedRealtimeMillis.longValue() + refreshAttemptDelayMillis;
                } else {
                    android.util.Log.w(com.android.server.timedetector.NetworkTimeUpdateService.TAG, "mLastRefreshAttemptElapsedRealtimeMillis unexpectedly missing. Scheduling using currentElapsedRealtimeMillis");
                    logToDebugAndDumpsys("mLastRefreshAttemptElapsedRealtimeMillis unexpectedly missing. Scheduling using currentElapsedRealtimeMillis");
                    nextRefreshElapsedRealtimeMillis = currentElapsedRealtimeMillis2 + refreshAttemptDelayMillis;
                }
                if (nextRefreshElapsedRealtimeMillis <= currentElapsedRealtimeMillis2) {
                    android.util.Log.w(com.android.server.timedetector.NetworkTimeUpdateService.TAG, "nextRefreshElapsedRealtimeMillis is a time in the past. Scheduling using currentElapsedRealtimeMillis instead");
                    logToDebugAndDumpsys("nextRefreshElapsedRealtimeMillis is a time in the past. Scheduling using currentElapsedRealtimeMillis instead");
                    nextRefreshElapsedRealtimeMillis = currentElapsedRealtimeMillis2 + refreshAttemptDelayMillis;
                }
                refreshCallbacks.scheduleNextRefresh(nextRefreshElapsedRealtimeMillis);
                logToDebugAndDumpsys("refreshIfRequiredAndReschedule: network=" + network + ", reason=" + reason + ", initialTimeResult=" + initialTimeResult + ", shouldAttemptRefresh=" + shouldAttemptRefresh + ", refreshSuccessful=" + refreshSuccessful + ", currentElapsedRealtimeMillis=" + formatElapsedRealtimeMillis(currentElapsedRealtimeMillis2) + ", latestTimeResult=" + latestTimeResult + ", mTryAgainCounter=" + this.mTryAgainCounter + ", refreshAttemptDelayMillis=" + refreshAttemptDelayMillis + ", nextRefreshElapsedRealtimeMillis=" + formatElapsedRealtimeMillis(nextRefreshElapsedRealtimeMillis));
            }
        }

        private static java.lang.String formatElapsedRealtimeMillis(long elapsedRealtimeMillis) {
            return java.time.Duration.ofMillis(elapsedRealtimeMillis) + " (" + elapsedRealtimeMillis + ")";
        }

        private static long calculateTimeResultAgeMillis(android.util.NtpTrustedTime.TimeResult timeResult, long currentElapsedRealtimeMillis) {
            if (timeResult == null) {
                return Long.MAX_VALUE;
            }
            return timeResult.getAgeMillis(currentElapsedRealtimeMillis);
        }

        private boolean isRefreshAllowed(long currentElapsedRealtimeMillis) {
            if (this.mLastRefreshAttemptElapsedRealtimeMillis == null) {
                return true;
            }
            long nextRefreshAllowedElapsedRealtimeMillis = this.mLastRefreshAttemptElapsedRealtimeMillis.longValue() + ((long) this.mShortPollingIntervalMillis);
            return currentElapsedRealtimeMillis >= nextRefreshAllowedElapsedRealtimeMillis;
        }

        private boolean tryRefresh(android.net.Network network) {
            long currentElapsedRealtimeMillis = this.mElapsedRealtimeMillisSupplier.get().longValue();
            synchronized (this) {
                this.mLastRefreshAttemptElapsedRealtimeMillis = java.lang.Long.valueOf(currentElapsedRealtimeMillis);
            }
            return this.mNtpTrustedTime.forceRefresh(network);
        }

        private void makeNetworkTimeSuggestion(android.util.NtpTrustedTime.TimeResult timeResult, java.lang.String debugInfo, com.android.server.timedetector.NetworkTimeUpdateService.Engine.RefreshCallbacks refreshCallbacks) {
            android.app.time.UnixEpochTime timeSignal = new android.app.time.UnixEpochTime(timeResult.getElapsedRealtimeMillis(), timeResult.getTimeMillis());
            com.android.server.timedetector.NetworkTimeSuggestion timeSuggestion = new com.android.server.timedetector.NetworkTimeSuggestion(timeSignal, timeResult.getUncertaintyMillis());
            timeSuggestion.addDebugInfo(debugInfo);
            timeSuggestion.addDebugInfo(timeResult.toString());
            refreshCallbacks.submitSuggestion(timeSuggestion);
        }

        @Override // com.android.server.timedetector.NetworkTimeUpdateService.Engine
        public void dump(java.io.PrintWriter pw) {
            java.lang.String lastRefreshAttemptValue;
            android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw);
            ipw.println("mNormalPollingIntervalMillis=" + this.mNormalPollingIntervalMillis);
            ipw.println("mShortPollingIntervalMillis=" + this.mShortPollingIntervalMillis);
            ipw.println("mTryAgainTimesMax=" + this.mTryAgainTimesMax);
            synchronized (this) {
                if (this.mLastRefreshAttemptElapsedRealtimeMillis == null) {
                    lastRefreshAttemptValue = "null";
                } else {
                    lastRefreshAttemptValue = formatElapsedRealtimeMillis(this.mLastRefreshAttemptElapsedRealtimeMillis.longValue());
                }
                ipw.println("mLastRefreshAttemptElapsedRealtimeMillis=" + lastRefreshAttemptValue);
                ipw.println("mTryAgainCounter=" + this.mTryAgainCounter);
            }
            ipw.println();
            ipw.println("NtpTrustedTime:");
            ipw.increaseIndent();
            this.mNtpTrustedTime.dump(ipw);
            ipw.decreaseIndent();
            ipw.println();
            ipw.println("Debug log:");
            ipw.increaseIndent();
            this.mLocalDebugLog.dump(ipw);
            ipw.decreaseIndent();
            ipw.println();
        }

        private void logToDebugAndDumpsys(java.lang.String logMsg) {
            this.mLocalDebugLog.log(logMsg);
        }
    }
}
