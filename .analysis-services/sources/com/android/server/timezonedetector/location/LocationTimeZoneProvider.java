package com.android.server.timezonedetector.location;

/* JADX INFO: loaded from: classes3.dex */
abstract class LocationTimeZoneProvider implements com.android.server.timezonedetector.Dumpable {
    private final com.android.server.timezonedetector.location.ThreadingDomain.SingleRunnableQueue mInitializationTimeoutQueue;
    com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderListener mProviderListener;
    private final com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderMetricsLogger mProviderMetricsLogger;
    final java.lang.String mProviderName;
    private final boolean mRecordStateChanges;
    final java.lang.Object mSharedLock;
    final com.android.server.timezonedetector.location.ThreadingDomain mThreadingDomain;
    private final com.android.server.timezonedetector.location.TimeZoneProviderEventPreProcessor mTimeZoneProviderEventPreProcessor;
    private final java.util.ArrayList<com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState> mRecordedStates = new java.util.ArrayList<>(0);
    final com.android.server.timezonedetector.ReferenceWithHistory<com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState> mCurrentState = new com.android.server.timezonedetector.ReferenceWithHistory<>(10);

    interface ProviderListener {
        void onProviderStateChange(com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState providerState);
    }

    interface ProviderMetricsLogger {
        void onProviderStateChanged(int i);
    }

    abstract void onDestroy();

    abstract boolean onInitialize();

    abstract void onStartUpdates(java.time.Duration duration, java.time.Duration duration2);

    abstract void onStopUpdates();

    static class ProviderState {
        static final int PROVIDER_STATE_DESTROYED = 6;
        static final int PROVIDER_STATE_PERM_FAILED = 5;
        static final int PROVIDER_STATE_STARTED_CERTAIN = 2;
        static final int PROVIDER_STATE_STARTED_INITIALIZING = 1;
        static final int PROVIDER_STATE_STARTED_UNCERTAIN = 3;
        static final int PROVIDER_STATE_STOPPED = 4;
        static final int PROVIDER_STATE_UNKNOWN = 0;
        public final com.android.server.timezonedetector.ConfigurationInternal currentUserConfiguration;
        public final android.service.timezone.TimeZoneProviderEvent event;
        private final java.lang.String mDebugInfo;
        private final long mStateEntryTimeMillis = android.os.SystemClock.elapsedRealtime();
        public final com.android.server.timezonedetector.location.LocationTimeZoneProvider provider;
        public final int stateEnum;

        @java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @interface ProviderStateEnum {
        }

        private ProviderState(com.android.server.timezonedetector.location.LocationTimeZoneProvider provider, int stateEnum, android.service.timezone.TimeZoneProviderEvent event, com.android.server.timezonedetector.ConfigurationInternal currentUserConfiguration, java.lang.String debugInfo) {
            this.provider = (com.android.server.timezonedetector.location.LocationTimeZoneProvider) java.util.Objects.requireNonNull(provider);
            this.stateEnum = stateEnum;
            this.event = event;
            this.currentUserConfiguration = currentUserConfiguration;
            this.mDebugInfo = debugInfo;
        }

        static com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState createStartingState(com.android.server.timezonedetector.location.LocationTimeZoneProvider provider) {
            return new com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState(provider, 0, null, null, "Initial state");
        }

        com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState newState(int newStateEnum, android.service.timezone.TimeZoneProviderEvent event, com.android.server.timezonedetector.ConfigurationInternal currentUserConfig, java.lang.String debugInfo) {
            switch (this.stateEnum) {
                case 0:
                    if (newStateEnum != 4) {
                        throw new java.lang.IllegalArgumentException("Must transition from " + prettyPrintStateEnum(0) + " to " + prettyPrintStateEnum(4));
                    }
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    break;
                case 5:
                case 6:
                    throw new java.lang.IllegalArgumentException("Illegal transition out of " + prettyPrintStateEnum(this.stateEnum));
                default:
                    throw new java.lang.IllegalArgumentException("Invalid this.stateEnum=" + this.stateEnum);
            }
            switch (newStateEnum) {
                case 0:
                    throw new java.lang.IllegalArgumentException("Cannot transition to " + prettyPrintStateEnum(0));
                case 1:
                case 2:
                case 3:
                    if (currentUserConfig == null) {
                        throw new java.lang.IllegalArgumentException("Started state: currentUserConfig must not be null");
                    }
                    break;
                case 4:
                    if (event != null || currentUserConfig != null) {
                        throw new java.lang.IllegalArgumentException("Stopped state: event and currentUserConfig must be null, event=" + event + ", currentUserConfig=" + currentUserConfig);
                    }
                    break;
                case 5:
                case 6:
                    if (event != null || currentUserConfig != null) {
                        throw new java.lang.IllegalArgumentException("Terminal state: event and currentUserConfig must be null, newStateEnum=" + newStateEnum + ", event=" + event + ", currentUserConfig=" + currentUserConfig);
                    }
                    break;
                default:
                    throw new java.lang.IllegalArgumentException("Unknown newStateEnum=" + newStateEnum);
            }
            return new com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState(this.provider, newStateEnum, event, currentUserConfig, debugInfo);
        }

        boolean isStarted() {
            return this.stateEnum == 1 || this.stateEnum == 2 || this.stateEnum == 3;
        }

        boolean isTerminated() {
            return this.stateEnum == 5 || this.stateEnum == 6;
        }

        public int getProviderStatus() {
            switch (this.stateEnum) {
                case 1:
                    return 2;
                case 2:
                    return 3;
                case 3:
                    return 4;
                case 4:
                case 6:
                    return 2;
                case 5:
                    return 1;
                default:
                    throw new java.lang.IllegalStateException("Unknown state enum:" + prettyPrintStateEnum(this.stateEnum));
            }
        }

        android.service.timezone.TimeZoneProviderStatus getReportedStatus() {
            if (this.event == null) {
                return null;
            }
            return this.event.getTimeZoneProviderStatus();
        }

        public java.lang.String toString() {
            return "ProviderState{stateEnum=" + prettyPrintStateEnum(this.stateEnum) + ", event=" + this.event + ", currentUserConfiguration=" + this.currentUserConfiguration + ", mStateEntryTimeMillis=" + this.mStateEntryTimeMillis + ", mDebugInfo=" + this.mDebugInfo + '}';
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState state = (com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState) o;
            if (this.stateEnum == state.stateEnum && java.util.Objects.equals(this.event, state.event) && java.util.Objects.equals(this.currentUserConfiguration, state.currentUserConfiguration)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.stateEnum), this.event, this.currentUserConfiguration);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static java.lang.String prettyPrintStateEnum(int state) {
            switch (state) {
                case 1:
                    return "Started initializing (1)";
                case 2:
                    return "Started certain (2)";
                case 3:
                    return "Started uncertain (3)";
                case 4:
                    return "Stopped (4)";
                case 5:
                    return "Perm failure (5)";
                case 6:
                    return "Destroyed (6)";
                default:
                    return "Unknown (" + state + ")";
            }
        }
    }

    LocationTimeZoneProvider(com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderMetricsLogger providerMetricsLogger, com.android.server.timezonedetector.location.ThreadingDomain threadingDomain, java.lang.String providerName, com.android.server.timezonedetector.location.TimeZoneProviderEventPreProcessor timeZoneProviderEventPreProcessor, boolean recordStateChanges) {
        this.mThreadingDomain = (com.android.server.timezonedetector.location.ThreadingDomain) java.util.Objects.requireNonNull(threadingDomain);
        this.mProviderMetricsLogger = (com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderMetricsLogger) java.util.Objects.requireNonNull(providerMetricsLogger);
        this.mInitializationTimeoutQueue = threadingDomain.createSingleRunnableQueue();
        this.mSharedLock = threadingDomain.getLockObject();
        this.mProviderName = (java.lang.String) java.util.Objects.requireNonNull(providerName);
        this.mTimeZoneProviderEventPreProcessor = (com.android.server.timezonedetector.location.TimeZoneProviderEventPreProcessor) java.util.Objects.requireNonNull(timeZoneProviderEventPreProcessor);
        this.mRecordStateChanges = recordStateChanges;
    }

    final void initialize(com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderListener providerListener) {
        boolean initializationSuccess;
        java.lang.String initializationFailureReason;
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            if (this.mProviderListener != null) {
                throw new java.lang.IllegalStateException("initialize already called");
            }
            this.mProviderListener = (com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderListener) java.util.Objects.requireNonNull(providerListener);
            com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState currentState = com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState.createStartingState(this).newState(4, null, null, "initialize");
            setCurrentState(currentState, false);
            try {
                initializationSuccess = onInitialize();
                initializationFailureReason = "onInitialize() returned false";
            } catch (java.lang.RuntimeException e) {
                com.android.server.timezonedetector.location.LocationTimeZoneManagerService.warnLog("Unable to initialize the provider due to exception", e);
                java.lang.String str = "onInitialize() threw exception:" + e.getMessage();
                initializationSuccess = false;
                initializationFailureReason = str;
            }
            if (!initializationSuccess) {
                setCurrentState(currentState.newState(5, null, null, "Failed to initialize: " + initializationFailureReason), true);
            }
        }
    }

    final void destroy() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState currentState = this.mCurrentState.get();
            if (!currentState.isTerminated()) {
                com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState destroyedState = currentState.newState(6, null, null, "destroy");
                setCurrentState(destroyedState, false);
                onDestroy();
            }
        }
    }

    final void clearRecordedStates() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            this.mRecordedStates.clear();
            this.mRecordedStates.trimToSize();
        }
    }

    final java.util.List<com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState> getRecordedStates() {
        java.util.ArrayList arrayList;
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            arrayList = new java.util.ArrayList(this.mRecordedStates);
        }
        return arrayList;
    }

    private void setCurrentState(com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState newState, boolean notifyChanges) {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState oldState = this.mCurrentState.get();
            this.mCurrentState.set(newState);
            onSetCurrentState(newState);
            if (!java.util.Objects.equals(newState, oldState)) {
                this.mProviderMetricsLogger.onProviderStateChanged(newState.stateEnum);
                if (this.mRecordStateChanges) {
                    this.mRecordedStates.add(newState);
                }
                if (notifyChanges) {
                    this.mProviderListener.onProviderStateChange(newState);
                }
            }
        }
    }

    void onSetCurrentState(com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState newState) {
    }

    final com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState getCurrentState() {
        com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState providerState;
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            providerState = this.mCurrentState.get();
        }
        return providerState;
    }

    final java.lang.String getName() {
        this.mThreadingDomain.assertCurrentThread();
        return this.mProviderName;
    }

    final void startUpdates(com.android.server.timezonedetector.ConfigurationInternal currentUserConfiguration, java.time.Duration initializationTimeout, java.time.Duration initializationTimeoutFuzz, java.time.Duration eventFilteringAgeThreshold) {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            assertCurrentState(4);
            com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState currentState = this.mCurrentState.get();
            com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState newState = currentState.newState(1, null, currentUserConfiguration, "startUpdates");
            setCurrentState(newState, false);
            java.time.Duration delay = initializationTimeout.plus(initializationTimeoutFuzz);
            this.mInitializationTimeoutQueue.runDelayed(new java.lang.Runnable() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneProvider$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.handleInitializationTimeout();
                }
            }, delay.toMillis());
            onStartUpdates(initializationTimeout, eventFilteringAgeThreshold);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInitializationTimeout() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState currentState = this.mCurrentState.get();
            if (currentState.stateEnum == 1) {
                com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState newState = currentState.newState(3, null, currentState.currentUserConfiguration, "handleInitializationTimeout");
                setCurrentState(newState, true);
            } else {
                com.android.server.timezonedetector.location.LocationTimeZoneManagerService.warnLog("handleInitializationTimeout: Initialization timeout triggered when in an unexpected state=" + currentState);
            }
        }
    }

    final void stopUpdates() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            assertIsStarted();
            com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState currentState = this.mCurrentState.get();
            com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState newState = currentState.newState(4, null, null, "stopUpdates");
            setCurrentState(newState, false);
            cancelInitializationTimeoutIfSet();
            onStopUpdates();
        }
    }

    final void handleTimeZoneProviderEvent(android.service.timezone.TimeZoneProviderEvent timeZoneProviderEvent) {
        int providerStateEnum;
        this.mThreadingDomain.assertCurrentThread();
        java.util.Objects.requireNonNull(timeZoneProviderEvent);
        android.service.timezone.TimeZoneProviderEvent timeZoneProviderEvent2 = this.mTimeZoneProviderEventPreProcessor.preProcess(timeZoneProviderEvent);
        synchronized (this.mSharedLock) {
            com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("handleTimeZoneProviderEvent: mProviderName=" + this.mProviderName + ", timeZoneProviderEvent=" + timeZoneProviderEvent2);
            com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState currentState = this.mCurrentState.get();
            int eventType = timeZoneProviderEvent2.getType();
            switch (currentState.stateEnum) {
                case 1:
                case 2:
                case 3:
                    switch (eventType) {
                        case 1:
                            java.lang.String msg = "handleTimeZoneProviderEvent: Failure event=" + timeZoneProviderEvent2 + " received for provider=" + this.mProviderName + " in state=" + com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState.prettyPrintStateEnum(currentState.stateEnum) + ", entering permanently failed state";
                            com.android.server.timezonedetector.location.LocationTimeZoneManagerService.warnLog(msg);
                            com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState newState = currentState.newState(5, null, null, msg);
                            setCurrentState(newState, true);
                            cancelInitializationTimeoutIfSet();
                            return;
                        case 2:
                        case 3:
                            if (eventType == 3) {
                                providerStateEnum = 3;
                            } else {
                                providerStateEnum = 2;
                            }
                            com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState newState2 = currentState.newState(providerStateEnum, timeZoneProviderEvent2, currentState.currentUserConfiguration, "handleTimeZoneProviderEvent");
                            setCurrentState(newState2, true);
                            cancelInitializationTimeoutIfSet();
                            return;
                        default:
                            throw new java.lang.IllegalStateException("Unknown eventType=" + timeZoneProviderEvent2);
                    }
                case 4:
                    switch (eventType) {
                        case 1:
                            java.lang.String msg2 = "handleTimeZoneProviderEvent: Failure event=" + timeZoneProviderEvent2 + " received for stopped provider=" + this.mProviderName + ", entering permanently failed state";
                            com.android.server.timezonedetector.location.LocationTimeZoneManagerService.warnLog(msg2);
                            com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState newState3 = currentState.newState(5, null, null, msg2);
                            setCurrentState(newState3, true);
                            cancelInitializationTimeoutIfSet();
                            return;
                        case 2:
                        case 3:
                            com.android.server.timezonedetector.location.LocationTimeZoneManagerService.warnLog("handleTimeZoneProviderEvent: event=" + timeZoneProviderEvent2 + " received for stopped provider=" + this + ", ignoring");
                            return;
                        default:
                            throw new java.lang.IllegalStateException("Unknown eventType=" + timeZoneProviderEvent2);
                    }
                case 5:
                case 6:
                    com.android.server.timezonedetector.location.LocationTimeZoneManagerService.warnLog("handleTimeZoneProviderEvent: Event=" + timeZoneProviderEvent2 + " received for provider=" + this + " when in terminated state");
                    return;
                default:
                    throw new java.lang.IllegalStateException("Unknown providerType=" + currentState);
            }
        }
    }

    final void handleTemporaryFailure(java.lang.String reason) {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState currentState = this.mCurrentState.get();
            switch (currentState.stateEnum) {
                case 1:
                case 2:
                case 3:
                    java.lang.String debugInfo = "handleTemporaryFailure: reason=" + reason + ", currentState=" + com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState.prettyPrintStateEnum(currentState.stateEnum);
                    com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState newState = currentState.newState(3, null, currentState.currentUserConfiguration, debugInfo);
                    setCurrentState(newState, true);
                    cancelInitializationTimeoutIfSet();
                    break;
                case 4:
                    com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("handleProviderLost reason=" + reason + ", mProviderName=" + this.mProviderName + ", currentState=" + currentState + ": No state change required, provider is stopped.");
                    break;
                case 5:
                case 6:
                    com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("handleProviderLost reason=" + reason + ", mProviderName=" + this.mProviderName + ", currentState=" + currentState + ": No state change required, provider is terminated.");
                    break;
                default:
                    throw new java.lang.IllegalStateException("Unknown currentState=" + currentState);
            }
        }
    }

    private void assertIsStarted() {
        com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState currentState = this.mCurrentState.get();
        if (!currentState.isStarted()) {
            throw new java.lang.IllegalStateException("Required a started state, but was " + currentState);
        }
    }

    private void assertCurrentState(int requiredState) {
        com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState currentState = this.mCurrentState.get();
        if (currentState.stateEnum != requiredState) {
            throw new java.lang.IllegalStateException("Required stateEnum=" + requiredState + ", but was " + currentState);
        }
    }

    boolean isInitializationTimeoutSet() {
        boolean zHasQueued;
        synchronized (this.mSharedLock) {
            zHasQueued = this.mInitializationTimeoutQueue.hasQueued();
        }
        return zHasQueued;
    }

    private void cancelInitializationTimeoutIfSet() {
        if (this.mInitializationTimeoutQueue.hasQueued()) {
            this.mInitializationTimeoutQueue.cancel();
        }
    }

    java.time.Duration getInitializationTimeoutDelay() {
        java.time.Duration durationOfMillis;
        synchronized (this.mSharedLock) {
            durationOfMillis = java.time.Duration.ofMillis(this.mInitializationTimeoutQueue.getQueuedDelayMillis());
        }
        return durationOfMillis;
    }
}
