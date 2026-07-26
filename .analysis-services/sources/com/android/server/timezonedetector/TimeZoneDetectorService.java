package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
public final class TimeZoneDetectorService extends android.app.timezonedetector.ITimeZoneDetectorService.Stub implements android.os.IBinder.DeathRecipient {
    static final boolean DBG = false;
    static final java.lang.String TAG = "time_zone_detector";
    private final com.android.server.timezonedetector.CallerIdentityInjector mCallerIdentityInjector;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final com.android.server.timezonedetector.TimeZoneDetectorStrategy mTimeZoneDetectorStrategy;
    private final android.util.ArrayMap<android.os.IBinder, android.app.time.ITimeZoneDetectorListener> mListeners = new android.util.ArrayMap<>();
    private final java.util.List<com.android.server.timezonedetector.Dumpable> mDumpables = new java.util.ArrayList();

    public static final class Lifecycle extends com.android.server.SystemService {
        public Lifecycle(android.content.Context context) {
            super(context);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r8v0, types: [android.os.IBinder, com.android.server.timezonedetector.TimeZoneDetectorService] */
        @Override // com.android.server.SystemService
        public void onStart() {
            android.content.Context context = getContext();
            android.os.Handler handler = com.android.server.FgThread.getHandler();
            com.android.server.timezonedetector.ServiceConfigAccessor serviceConfigAccessor = com.android.server.timezonedetector.ServiceConfigAccessorImpl.getInstance(context);
            final com.android.server.timezonedetector.TimeZoneDetectorStrategy timeZoneDetectorStrategy = com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.create(handler, serviceConfigAccessor);
            com.android.server.timezonedetector.DeviceActivityMonitor deviceActivityMonitor = com.android.server.timezonedetector.DeviceActivityMonitorImpl.create(context, handler);
            deviceActivityMonitor.addListener(new com.android.server.timezonedetector.DeviceActivityMonitor.Listener() { // from class: com.android.server.timezonedetector.TimeZoneDetectorService.Lifecycle.1
                @Override // com.android.server.timezonedetector.DeviceActivityMonitor.Listener
                public void onFlightComplete() {
                    timeZoneDetectorStrategy.enableTelephonyTimeZoneFallback("onFlightComplete()");
                }
            });
            com.android.server.timezonedetector.CurrentUserIdentityInjector currentUserIdentityInjector = com.android.server.timezonedetector.CurrentUserIdentityInjector.REAL;
            publishLocalService(com.android.server.timezonedetector.TimeZoneDetectorInternal.class, new com.android.server.timezonedetector.TimeZoneDetectorInternalImpl(context, handler, currentUserIdentityInjector, timeZoneDetectorStrategy));
            com.android.server.timezonedetector.CallerIdentityInjector callerIdentityInjector = com.android.server.timezonedetector.CallerIdentityInjector.REAL;
            ?? timeZoneDetectorService = new com.android.server.timezonedetector.TimeZoneDetectorService(context, handler, callerIdentityInjector, timeZoneDetectorStrategy);
            timeZoneDetectorService.addDumpable(deviceActivityMonitor);
            publishBinderService(com.android.server.timezonedetector.TimeZoneDetectorService.TAG, timeZoneDetectorService);
        }
    }

    public TimeZoneDetectorService(android.content.Context context, android.os.Handler handler, com.android.server.timezonedetector.CallerIdentityInjector callerIdentityInjector, com.android.server.timezonedetector.TimeZoneDetectorStrategy timeZoneDetectorStrategy) {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mHandler = (android.os.Handler) java.util.Objects.requireNonNull(handler);
        this.mCallerIdentityInjector = (com.android.server.timezonedetector.CallerIdentityInjector) java.util.Objects.requireNonNull(callerIdentityInjector);
        this.mTimeZoneDetectorStrategy = (com.android.server.timezonedetector.TimeZoneDetectorStrategy) java.util.Objects.requireNonNull(timeZoneDetectorStrategy);
        this.mTimeZoneDetectorStrategy.addChangeListener(new com.android.server.timezonedetector.StateChangeListener() { // from class: com.android.server.timezonedetector.TimeZoneDetectorService$$ExternalSyntheticLambda3
            @Override // com.android.server.timezonedetector.StateChangeListener
            public final void onChange() {
                this.f$0.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.timezonedetector.TimeZoneDetectorService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.handleChangeOnHandlerThread();
            }
        });
    }

    public android.app.time.TimeZoneCapabilitiesAndConfig getCapabilitiesAndConfig() {
        int userId = this.mCallerIdentityInjector.getCallingUserId();
        return getCapabilitiesAndConfig(userId);
    }

    android.app.time.TimeZoneCapabilitiesAndConfig getCapabilitiesAndConfig(int userId) {
        enforceManageTimeZoneDetectorPermission();
        int resolvedUserId = this.mCallerIdentityInjector.resolveUserId(userId, "getCapabilitiesAndConfig");
        long token = this.mCallerIdentityInjector.clearCallingIdentity();
        try {
            return this.mTimeZoneDetectorStrategy.getCapabilitiesAndConfig(resolvedUserId, false);
        } finally {
            this.mCallerIdentityInjector.restoreCallingIdentity(token);
        }
    }

    public boolean updateConfiguration(android.app.time.TimeZoneConfiguration configuration) {
        int callingUserId = this.mCallerIdentityInjector.getCallingUserId();
        return updateConfiguration(callingUserId, configuration);
    }

    boolean updateConfiguration(int userId, android.app.time.TimeZoneConfiguration configuration) {
        int resolvedUserId = this.mCallerIdentityInjector.resolveUserId(userId, "updateConfiguration");
        enforceManageTimeZoneDetectorPermission();
        java.util.Objects.requireNonNull(configuration);
        long token = this.mCallerIdentityInjector.clearCallingIdentity();
        try {
            return this.mTimeZoneDetectorStrategy.updateConfiguration(resolvedUserId, configuration, false);
        } finally {
            this.mCallerIdentityInjector.restoreCallingIdentity(token);
        }
    }

    public void addListener(android.app.time.ITimeZoneDetectorListener listener) {
        enforceManageTimeZoneDetectorPermission();
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

    public void removeListener(android.app.time.ITimeZoneDetectorListener listener) {
        enforceManageTimeZoneDetectorPermission();
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

    void handleChangeOnHandlerThread() {
        synchronized (this.mListeners) {
            int listenerCount = this.mListeners.size();
            for (int listenerIndex = 0; listenerIndex < listenerCount; listenerIndex++) {
                android.app.time.ITimeZoneDetectorListener listener = this.mListeners.valueAt(listenerIndex);
                try {
                    listener.onChange();
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Unable to notify listener=" + listener, e);
                }
            }
        }
    }

    void handleLocationAlgorithmEvent(final com.android.server.timezonedetector.LocationAlgorithmEvent locationAlgorithmEvent) {
        enforceSuggestGeolocationTimeZonePermission();
        java.util.Objects.requireNonNull(locationAlgorithmEvent);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.timezonedetector.TimeZoneDetectorService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$handleLocationAlgorithmEvent$1(locationAlgorithmEvent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleLocationAlgorithmEvent$1(com.android.server.timezonedetector.LocationAlgorithmEvent locationAlgorithmEvent) {
        this.mTimeZoneDetectorStrategy.handleLocationAlgorithmEvent(locationAlgorithmEvent);
    }

    public android.app.time.TimeZoneState getTimeZoneState() {
        enforceManageTimeZoneDetectorPermission();
        long token = this.mCallerIdentityInjector.clearCallingIdentity();
        try {
            return this.mTimeZoneDetectorStrategy.getTimeZoneState();
        } finally {
            this.mCallerIdentityInjector.restoreCallingIdentity(token);
        }
    }

    void setTimeZoneState(android.app.time.TimeZoneState timeZoneState) {
        enforceManageTimeZoneDetectorPermission();
        long token = this.mCallerIdentityInjector.clearCallingIdentity();
        try {
            this.mTimeZoneDetectorStrategy.setTimeZoneState(timeZoneState);
        } finally {
            this.mCallerIdentityInjector.restoreCallingIdentity(token);
        }
    }

    public boolean confirmTimeZone(java.lang.String timeZoneId) {
        enforceManageTimeZoneDetectorPermission();
        long token = this.mCallerIdentityInjector.clearCallingIdentity();
        try {
            return this.mTimeZoneDetectorStrategy.confirmTimeZone(timeZoneId);
        } finally {
            this.mCallerIdentityInjector.restoreCallingIdentity(token);
        }
    }

    public boolean setManualTimeZone(android.app.timezonedetector.ManualTimeZoneSuggestion suggestion) {
        enforceManageTimeZoneDetectorPermission();
        int userId = this.mCallerIdentityInjector.getCallingUserId();
        long token = this.mCallerIdentityInjector.clearCallingIdentity();
        try {
            return this.mTimeZoneDetectorStrategy.suggestManualTimeZone(userId, suggestion, false);
        } finally {
            this.mCallerIdentityInjector.restoreCallingIdentity(token);
        }
    }

    public boolean suggestManualTimeZone(android.app.timezonedetector.ManualTimeZoneSuggestion suggestion) {
        enforceSuggestManualTimeZonePermission();
        java.util.Objects.requireNonNull(suggestion);
        int userId = this.mCallerIdentityInjector.getCallingUserId();
        long token = this.mCallerIdentityInjector.clearCallingIdentity();
        try {
            return this.mTimeZoneDetectorStrategy.suggestManualTimeZone(userId, suggestion, false);
        } finally {
            this.mCallerIdentityInjector.restoreCallingIdentity(token);
        }
    }

    public void suggestTelephonyTimeZone(final android.app.timezonedetector.TelephonyTimeZoneSuggestion suggestion) {
        enforceSuggestTelephonyTimeZonePermission();
        java.util.Objects.requireNonNull(suggestion);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.timezonedetector.TimeZoneDetectorService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$suggestTelephonyTimeZone$2(suggestion);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$suggestTelephonyTimeZone$2(android.app.timezonedetector.TelephonyTimeZoneSuggestion suggestion) {
        this.mTimeZoneDetectorStrategy.suggestTelephonyTimeZone(suggestion);
    }

    boolean isTelephonyTimeZoneDetectionSupported() {
        enforceManageTimeZoneDetectorPermission();
        return this.mTimeZoneDetectorStrategy.isTelephonyTimeZoneDetectionSupported();
    }

    boolean isGeoTimeZoneDetectionSupported() {
        enforceManageTimeZoneDetectorPermission();
        return this.mTimeZoneDetectorStrategy.isGeoTimeZoneDetectionSupported();
    }

    void enableTelephonyFallback(java.lang.String reason) {
        enforceManageTimeZoneDetectorPermission();
        this.mTimeZoneDetectorStrategy.enableTelephonyTimeZoneFallback(reason);
    }

    void addDumpable(com.android.server.timezonedetector.Dumpable dumpable) {
        synchronized (this.mDumpables) {
            this.mDumpables.add(dumpable);
        }
    }

    com.android.server.timezonedetector.MetricsTimeZoneDetectorState generateMetricsState() {
        enforceManageTimeZoneDetectorPermission();
        return this.mTimeZoneDetectorStrategy.generateMetricsState();
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw);
            this.mTimeZoneDetectorStrategy.dump(ipw, args);
            synchronized (this.mDumpables) {
                for (com.android.server.timezonedetector.Dumpable dumpable : this.mDumpables) {
                    dumpable.dump(ipw, args);
                }
            }
            ipw.flush();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        new com.android.server.timezonedetector.TimeZoneDetectorShellCommand(this).exec(this, in, out, err, args, callback, resultReceiver);
    }

    private void enforceManageTimeZoneDetectorPermission() {
        this.mContext.enforceCallingPermission("android.permission.MANAGE_TIME_AND_ZONE_DETECTION", "manage time and time zone detection");
    }

    private void enforceSuggestGeolocationTimeZonePermission() {
        this.mContext.enforceCallingPermission("android.permission.SET_TIME_ZONE", "suggest geolocation time zone");
    }

    private void enforceSuggestTelephonyTimeZonePermission() {
        this.mContext.enforceCallingPermission("android.permission.SUGGEST_TELEPHONY_TIME_AND_ZONE", "suggest telephony time and time zone");
    }

    private void enforceSuggestManualTimeZonePermission() {
        this.mContext.enforceCallingPermission("android.permission.SUGGEST_MANUAL_TIME_AND_ZONE", "suggest manual time and time zone");
    }
}
