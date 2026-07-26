package com.android.server.timedetector;

/* JADX INFO: loaded from: classes3.dex */
public final class TimeDetectorService extends android.app.timedetector.ITimeDetectorService.Stub implements android.os.IBinder.DeathRecipient {
    static final java.lang.String TAG = "time_detector";
    private final com.android.server.timezonedetector.CallerIdentityInjector mCallerIdentityInjector;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final android.util.ArrayMap<android.os.IBinder, android.app.time.ITimeDetectorListener> mListeners = new android.util.ArrayMap<>();
    private final android.util.NtpTrustedTime mNtpTrustedTime;
    private final com.android.server.timedetector.TimeDetectorStrategy mTimeDetectorStrategy;

    public static class Lifecycle extends com.android.server.SystemService {
        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            android.content.Context context = getContext();
            android.os.Handler handler = com.android.server.FgThread.getHandler();
            com.android.server.timedetector.ServiceConfigAccessor serviceConfigAccessor = com.android.server.timedetector.ServiceConfigAccessorImpl.getInstance(context);
            com.android.server.timedetector.TimeDetectorStrategy timeDetectorStrategy = com.android.server.timedetector.TimeDetectorStrategyImpl.create(context, handler, serviceConfigAccessor);
            com.android.server.timezonedetector.CurrentUserIdentityInjector currentUserIdentityInjector = com.android.server.timezonedetector.CurrentUserIdentityInjector.REAL;
            publishLocalService(com.android.server.timedetector.TimeDetectorInternal.class, new com.android.server.timedetector.TimeDetectorInternalImpl(context, handler, currentUserIdentityInjector, serviceConfigAccessor, timeDetectorStrategy));
            com.android.server.timezonedetector.CallerIdentityInjector callerIdentityInjector = com.android.server.timezonedetector.CallerIdentityInjector.REAL;
            publishBinderService(com.android.server.timedetector.TimeDetectorService.TAG, new com.android.server.timedetector.TimeDetectorService(context, handler, callerIdentityInjector, timeDetectorStrategy, android.util.NtpTrustedTime.getInstance(context)));
        }
    }

    public TimeDetectorService(android.content.Context context, android.os.Handler handler, com.android.server.timezonedetector.CallerIdentityInjector callerIdentityInjector, com.android.server.timedetector.TimeDetectorStrategy timeDetectorStrategy, android.util.NtpTrustedTime ntpTrustedTime) {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mHandler = (android.os.Handler) java.util.Objects.requireNonNull(handler);
        this.mCallerIdentityInjector = (com.android.server.timezonedetector.CallerIdentityInjector) java.util.Objects.requireNonNull(callerIdentityInjector);
        this.mTimeDetectorStrategy = (com.android.server.timedetector.TimeDetectorStrategy) java.util.Objects.requireNonNull(timeDetectorStrategy);
        this.mNtpTrustedTime = (android.util.NtpTrustedTime) java.util.Objects.requireNonNull(ntpTrustedTime);
        this.mTimeDetectorStrategy.addChangeListener(new com.android.server.timezonedetector.StateChangeListener() { // from class: com.android.server.timedetector.TimeDetectorService$$ExternalSyntheticLambda4
            @Override // com.android.server.timezonedetector.StateChangeListener
            public final void onChange() {
                this.f$0.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.timedetector.TimeDetectorService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.handleChangeOnHandlerThread();
            }
        });
    }

    public android.app.time.TimeCapabilitiesAndConfig getCapabilitiesAndConfig() {
        int userId = this.mCallerIdentityInjector.getCallingUserId();
        return getTimeCapabilitiesAndConfig(userId);
    }

    private android.app.time.TimeCapabilitiesAndConfig getTimeCapabilitiesAndConfig(int userId) {
        enforceManageTimeDetectorPermission();
        long token = this.mCallerIdentityInjector.clearCallingIdentity();
        try {
            return this.mTimeDetectorStrategy.getCapabilitiesAndConfig(userId, false);
        } finally {
            this.mCallerIdentityInjector.restoreCallingIdentity(token);
        }
    }

    public boolean updateConfiguration(android.app.time.TimeConfiguration configuration) {
        int callingUserId = this.mCallerIdentityInjector.getCallingUserId();
        return updateConfiguration(callingUserId, configuration);
    }

    boolean updateConfiguration(int userId, android.app.time.TimeConfiguration configuration) {
        int resolvedUserId = this.mCallerIdentityInjector.resolveUserId(userId, "updateConfiguration");
        enforceManageTimeDetectorPermission();
        java.util.Objects.requireNonNull(configuration);
        long token = this.mCallerIdentityInjector.clearCallingIdentity();
        try {
            return this.mTimeDetectorStrategy.updateConfiguration(resolvedUserId, configuration, false);
        } finally {
            this.mCallerIdentityInjector.restoreCallingIdentity(token);
        }
    }

    public void addListener(android.app.time.ITimeDetectorListener listener) {
        enforceManageTimeDetectorPermission();
        java.util.Objects.requireNonNull(listener);
        synchronized (this.mListeners) {
            android.os.IBinder listenerBinder = listener.asBinder();
            if (this.mListeners.containsKey(listenerBinder)) {
                return;
            }
            try {
                listenerBinder.linkToDeath(this, 0);
                this.mListeners.put(listenerBinder, listener);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Unable to linkToDeath() for listener=" + listener, e);
            }
        }
    }

    public void removeListener(android.app.time.ITimeDetectorListener listener) {
        enforceManageTimeDetectorPermission();
        java.util.Objects.requireNonNull(listener);
        synchronized (this.mListeners) {
            android.os.IBinder listenerBinder = listener.asBinder();
            boolean removedListener = false;
            if (this.mListeners.remove(listenerBinder) != null) {
                listenerBinder.unlinkToDeath(this, 0);
                removedListener = true;
            }
            if (!removedListener) {
                android.util.Slog.w(TAG, "Client asked to remove listener=" + listener + ", but no listeners were removed. mListeners=" + this.mListeners);
            }
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        android.util.Slog.wtf(TAG, "binderDied() called unexpectedly.");
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied(android.os.IBinder who) {
        synchronized (this.mListeners) {
            boolean removedListener = false;
            int listenerCount = this.mListeners.size();
            int listenerIndex = listenerCount - 1;
            while (true) {
                if (listenerIndex < 0) {
                    break;
                }
                android.os.IBinder listenerBinder = this.mListeners.keyAt(listenerIndex);
                if (!listenerBinder.equals(who)) {
                    listenerIndex--;
                } else {
                    this.mListeners.removeAt(listenerIndex);
                    removedListener = true;
                    break;
                }
            }
            if (!removedListener) {
                android.util.Slog.w(TAG, "Notified of binder death for who=" + who + ", but did not remove any listeners. mListeners=" + this.mListeners);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleChangeOnHandlerThread() {
        synchronized (this.mListeners) {
            int listenerCount = this.mListeners.size();
            for (int listenerIndex = 0; listenerIndex < listenerCount; listenerIndex++) {
                android.app.time.ITimeDetectorListener listener = this.mListeners.valueAt(listenerIndex);
                try {
                    listener.onChange();
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Unable to notify listener=" + listener, e);
                }
            }
        }
    }

    public android.app.time.TimeState getTimeState() {
        enforceManageTimeDetectorPermission();
        long token = this.mCallerIdentityInjector.clearCallingIdentity();
        try {
            return this.mTimeDetectorStrategy.getTimeState();
        } finally {
            this.mCallerIdentityInjector.restoreCallingIdentity(token);
        }
    }

    void setTimeState(android.app.time.TimeState timeState) {
        enforceManageTimeDetectorPermission();
        long token = this.mCallerIdentityInjector.clearCallingIdentity();
        try {
            this.mTimeDetectorStrategy.setTimeState(timeState);
        } finally {
            this.mCallerIdentityInjector.restoreCallingIdentity(token);
        }
    }

    public boolean confirmTime(android.app.time.UnixEpochTime time) {
        enforceManageTimeDetectorPermission();
        java.util.Objects.requireNonNull(time);
        long token = this.mCallerIdentityInjector.clearCallingIdentity();
        try {
            return this.mTimeDetectorStrategy.confirmTime(time);
        } finally {
            this.mCallerIdentityInjector.restoreCallingIdentity(token);
        }
    }

    public boolean setManualTime(android.app.timedetector.ManualTimeSuggestion suggestion) {
        enforceManageTimeDetectorPermission();
        java.util.Objects.requireNonNull(suggestion);
        int userId = this.mCallerIdentityInjector.getCallingUserId();
        long token = this.mCallerIdentityInjector.clearCallingIdentity();
        try {
            return this.mTimeDetectorStrategy.suggestManualTime(userId, suggestion, false);
        } finally {
            this.mCallerIdentityInjector.restoreCallingIdentity(token);
        }
    }

    public void suggestTelephonyTime(final android.app.timedetector.TelephonyTimeSuggestion timeSignal) {
        enforceSuggestTelephonyTimePermission();
        java.util.Objects.requireNonNull(timeSignal);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.timedetector.TimeDetectorService$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$suggestTelephonyTime$1(timeSignal);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$suggestTelephonyTime$1(android.app.timedetector.TelephonyTimeSuggestion timeSignal) {
        this.mTimeDetectorStrategy.suggestTelephonyTime(timeSignal);
    }

    public boolean suggestManualTime(android.app.timedetector.ManualTimeSuggestion timeSignal) {
        enforceSuggestManualTimePermission();
        java.util.Objects.requireNonNull(timeSignal);
        int userId = this.mCallerIdentityInjector.getCallingUserId();
        long token = this.mCallerIdentityInjector.clearCallingIdentity();
        try {
            return this.mTimeDetectorStrategy.suggestManualTime(userId, timeSignal, false);
        } finally {
            this.mCallerIdentityInjector.restoreCallingIdentity(token);
        }
    }

    void suggestNetworkTime(final com.android.server.timedetector.NetworkTimeSuggestion suggestion) {
        enforceSuggestNetworkTimePermission();
        java.util.Objects.requireNonNull(suggestion);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.timedetector.TimeDetectorService$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$suggestNetworkTime$2(suggestion);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$suggestNetworkTime$2(com.android.server.timedetector.NetworkTimeSuggestion suggestion) {
        this.mTimeDetectorStrategy.suggestNetworkTime(suggestion);
    }

    void clearLatestNetworkTime() {
        enforceSuggestNetworkTimePermission();
        long token = this.mCallerIdentityInjector.clearCallingIdentity();
        try {
            this.mTimeDetectorStrategy.clearLatestNetworkSuggestion();
        } finally {
            this.mCallerIdentityInjector.restoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    public android.app.time.UnixEpochTime latestNetworkTime() throws android.os.ParcelableException {
        com.android.server.timedetector.NetworkTimeSuggestion latestNetworkTime;
        if (com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.isInUse()) {
            latestNetworkTime = this.mTimeDetectorStrategy.getLatestNetworkSuggestion();
        } else {
            android.util.NtpTrustedTime.TimeResult ntpResult = this.mNtpTrustedTime.getCachedTimeResult();
            if (ntpResult != null) {
                latestNetworkTime = new com.android.server.timedetector.NetworkTimeSuggestion(new android.app.time.UnixEpochTime(ntpResult.getElapsedRealtimeMillis(), ntpResult.getTimeMillis()), ntpResult.getUncertaintyMillis());
            } else {
                latestNetworkTime = null;
            }
        }
        if (latestNetworkTime == null) {
            throw new android.os.ParcelableException(new java.time.DateTimeException("Missing network time fix"));
        }
        return latestNetworkTime.getUnixEpochTime();
    }

    com.android.server.timedetector.NetworkTimeSuggestion getLatestNetworkSuggestion() {
        return this.mTimeDetectorStrategy.getLatestNetworkSuggestion();
    }

    void suggestGnssTime(final com.android.server.timedetector.GnssTimeSuggestion timeSignal) {
        enforceSuggestGnssTimePermission();
        java.util.Objects.requireNonNull(timeSignal);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.timedetector.TimeDetectorService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$suggestGnssTime$3(timeSignal);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$suggestGnssTime$3(com.android.server.timedetector.GnssTimeSuggestion timeSignal) {
        this.mTimeDetectorStrategy.suggestGnssTime(timeSignal);
    }

    public void suggestExternalTime(final android.app.time.ExternalTimeSuggestion timeSignal) {
        enforceSuggestExternalTimePermission();
        java.util.Objects.requireNonNull(timeSignal);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.timedetector.TimeDetectorService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$suggestExternalTime$4(timeSignal);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$suggestExternalTime$4(android.app.time.ExternalTimeSuggestion timeSignal) {
        this.mTimeDetectorStrategy.suggestExternalTime(timeSignal);
    }

    void setNetworkTimeForSystemClockForTests(android.app.time.UnixEpochTime unixEpochTime, int uncertaintyMillis) {
        enforceSuggestNetworkTimePermission();
        if (com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.isInUse()) {
            com.android.server.timedetector.NetworkTimeSuggestion suggestion = new com.android.server.timedetector.NetworkTimeSuggestion(unixEpochTime, uncertaintyMillis);
            suggestion.addDebugInfo("Injected for tests");
            this.mTimeDetectorStrategy.suggestNetworkTime(suggestion);
        } else {
            android.util.NtpTrustedTime.TimeResult timeResult = new android.util.NtpTrustedTime.TimeResult(unixEpochTime.getUnixEpochTimeMillis(), unixEpochTime.getElapsedRealtimeMillis(), uncertaintyMillis, java.net.InetSocketAddress.createUnresolved("time.set.for.tests", 123));
            this.mNtpTrustedTime.setCachedTimeResult(timeResult);
        }
    }

    void clearNetworkTimeForSystemClockForTests() {
        enforceSuggestNetworkTimePermission();
        long token = this.mCallerIdentityInjector.clearCallingIdentity();
        try {
            if (com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.isInUse()) {
                this.mTimeDetectorStrategy.clearLatestNetworkSuggestion();
            } else {
                this.mNtpTrustedTime.clearCachedTimeResult();
            }
        } finally {
            this.mCallerIdentityInjector.restoreCallingIdentity(token);
        }
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw);
            this.mTimeDetectorStrategy.dump(ipw, args);
            ipw.flush();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        new com.android.server.timedetector.TimeDetectorShellCommand(this).exec(this, in, out, err, args, callback, resultReceiver);
    }

    private void enforceSuggestTelephonyTimePermission() {
        this.mContext.enforceCallingPermission("android.permission.SUGGEST_TELEPHONY_TIME_AND_ZONE", "suggest telephony time and time zone");
    }

    private void enforceSuggestManualTimePermission() {
        this.mContext.enforceCallingPermission("android.permission.SUGGEST_MANUAL_TIME_AND_ZONE", "suggest manual time and time zone");
    }

    private void enforceSuggestNetworkTimePermission() {
        this.mContext.enforceCallingPermission("android.permission.SET_TIME", "suggest network time");
    }

    private void enforceSuggestGnssTimePermission() {
        this.mContext.enforceCallingPermission("android.permission.SET_TIME", "suggest gnss time");
    }

    private void enforceSuggestExternalTimePermission() {
        this.mContext.enforceCallingPermission("android.permission.SUGGEST_EXTERNAL_TIME", "suggest time from external source");
    }

    private void enforceManageTimeDetectorPermission() {
        this.mContext.enforceCallingPermission("android.permission.MANAGE_TIME_AND_ZONE_DETECTION", "manage time and time zone detection");
    }
}
