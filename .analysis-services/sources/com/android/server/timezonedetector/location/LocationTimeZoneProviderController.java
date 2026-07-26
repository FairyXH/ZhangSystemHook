package com.android.server.timezonedetector.location;

/* JADX INFO: loaded from: classes3.dex */
class LocationTimeZoneProviderController implements com.android.server.timezonedetector.Dumpable {
    static final java.lang.String STATE_CERTAIN = "CERTAIN";
    static final java.lang.String STATE_DESTROYED = "DESTROYED";
    static final java.lang.String STATE_FAILED = "FAILED";
    static final java.lang.String STATE_INITIALIZING = "INITIALIZING";
    static final java.lang.String STATE_PROVIDERS_INITIALIZING = "PROVIDERS_INITIALIZING";
    static final java.lang.String STATE_STOPPED = "STOPPED";
    static final java.lang.String STATE_UNCERTAIN = "UNCERTAIN";
    static final java.lang.String STATE_UNKNOWN = "UNKNOWN";
    private com.android.server.timezonedetector.location.LocationTimeZoneProviderController.Callback mCallback;
    private com.android.server.timezonedetector.ConfigurationInternal mCurrentUserConfiguration;
    private com.android.server.timezonedetector.location.LocationTimeZoneProviderController.Environment mEnvironment;
    private com.android.server.timezonedetector.LocationAlgorithmEvent mLastEvent;
    private final com.android.server.timezonedetector.location.LocationTimeZoneProviderController.MetricsLogger mMetricsLogger;
    private final com.android.server.timezonedetector.location.LocationTimeZoneProvider mPrimaryProvider;
    private final boolean mRecordStateChanges;
    private final com.android.server.timezonedetector.location.LocationTimeZoneProvider mSecondaryProvider;
    private final java.lang.Object mSharedLock;
    private final com.android.server.timezonedetector.location.ThreadingDomain mThreadingDomain;
    private final com.android.server.timezonedetector.location.ThreadingDomain.SingleRunnableQueue mUncertaintyTimeoutQueue;
    private final java.util.ArrayList<java.lang.String> mRecordedStates = new java.util.ArrayList<>(0);
    private final com.android.server.timezonedetector.ReferenceWithHistory<java.lang.String> mState = new com.android.server.timezonedetector.ReferenceWithHistory<>(10);

    interface MetricsLogger {
        void onStateChange(java.lang.String str);
    }

    @java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface State {
    }

    LocationTimeZoneProviderController(com.android.server.timezonedetector.location.ThreadingDomain threadingDomain, com.android.server.timezonedetector.location.LocationTimeZoneProviderController.MetricsLogger metricsLogger, com.android.server.timezonedetector.location.LocationTimeZoneProvider primaryProvider, com.android.server.timezonedetector.location.LocationTimeZoneProvider secondaryProvider, boolean recordStateChanges) {
        this.mThreadingDomain = (com.android.server.timezonedetector.location.ThreadingDomain) java.util.Objects.requireNonNull(threadingDomain);
        this.mSharedLock = threadingDomain.getLockObject();
        this.mUncertaintyTimeoutQueue = threadingDomain.createSingleRunnableQueue();
        this.mMetricsLogger = (com.android.server.timezonedetector.location.LocationTimeZoneProviderController.MetricsLogger) java.util.Objects.requireNonNull(metricsLogger);
        this.mPrimaryProvider = (com.android.server.timezonedetector.location.LocationTimeZoneProvider) java.util.Objects.requireNonNull(primaryProvider);
        this.mSecondaryProvider = (com.android.server.timezonedetector.location.LocationTimeZoneProvider) java.util.Objects.requireNonNull(secondaryProvider);
        this.mRecordStateChanges = recordStateChanges;
        synchronized (this.mSharedLock) {
            this.mState.set(STATE_UNKNOWN);
        }
    }

    void initialize(com.android.server.timezonedetector.location.LocationTimeZoneProviderController.Environment environment, com.android.server.timezonedetector.location.LocationTimeZoneProviderController.Callback callback) {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("initialize()");
            this.mEnvironment = (com.android.server.timezonedetector.location.LocationTimeZoneProviderController.Environment) java.util.Objects.requireNonNull(environment);
            this.mCallback = (com.android.server.timezonedetector.location.LocationTimeZoneProviderController.Callback) java.util.Objects.requireNonNull(callback);
            this.mCurrentUserConfiguration = environment.getCurrentUserConfigurationInternal();
            com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderListener providerListener = new com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderListener() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneProviderController$$ExternalSyntheticLambda0
                @Override // com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderListener
                public final void onProviderStateChange(com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState providerState) {
                    this.f$0.onProviderStateChange(providerState);
                }
            };
            setState(STATE_PROVIDERS_INITIALIZING);
            this.mPrimaryProvider.initialize(providerListener);
            this.mSecondaryProvider.initialize(providerListener);
            setStateAndReportStatusOnlyEvent(STATE_STOPPED, "initialize()");
            alterProvidersStartedStateIfRequired(null, this.mCurrentUserConfiguration);
        }
    }

    void onConfigurationInternalChanged() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("onConfigChanged()");
            com.android.server.timezonedetector.ConfigurationInternal oldConfig = this.mCurrentUserConfiguration;
            com.android.server.timezonedetector.ConfigurationInternal newConfig = this.mEnvironment.getCurrentUserConfigurationInternal();
            this.mCurrentUserConfiguration = newConfig;
            if (!newConfig.equals(oldConfig)) {
                if (newConfig.getUserId() != oldConfig.getUserId()) {
                    java.lang.String reason = "User changed. old=" + oldConfig.getUserId() + ", new=" + newConfig.getUserId();
                    com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("Stopping providers: " + reason);
                    stopProviders(reason);
                    alterProvidersStartedStateIfRequired(null, newConfig);
                } else {
                    alterProvidersStartedStateIfRequired(oldConfig, newConfig);
                }
            }
        }
    }

    boolean isUncertaintyTimeoutSet() {
        return this.mUncertaintyTimeoutQueue.hasQueued();
    }

    long getUncertaintyTimeoutDelayMillis() {
        return this.mUncertaintyTimeoutQueue.getQueuedDelayMillis();
    }

    void destroy() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            stopProviders("destroy()");
            this.mPrimaryProvider.destroy();
            this.mSecondaryProvider.destroy();
            setStateAndReportStatusOnlyEvent(STATE_DESTROYED, "destroy()");
        }
    }

    private void setStateAndReportStatusOnlyEvent(java.lang.String state, java.lang.String reason) {
        setState(state);
        com.android.server.timezonedetector.LocationAlgorithmEvent event = new com.android.server.timezonedetector.LocationAlgorithmEvent(generateCurrentAlgorithmStatus(), null);
        event.addDebugInfo(reason);
        reportEvent(event);
    }

    private void reportSuggestionEvent(com.android.server.timezonedetector.GeolocationTimeZoneSuggestion suggestion, java.lang.String reason) {
        android.app.time.LocationTimeZoneAlgorithmStatus algorithmStatus = generateCurrentAlgorithmStatus();
        com.android.server.timezonedetector.LocationAlgorithmEvent event = new com.android.server.timezonedetector.LocationAlgorithmEvent(algorithmStatus, suggestion);
        event.addDebugInfo(reason);
        reportEvent(event);
    }

    private void reportEvent(com.android.server.timezonedetector.LocationAlgorithmEvent event) {
        com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("makeSuggestion: suggestion=" + event);
        this.mCallback.sendEvent(event);
        this.mLastEvent = event;
    }

    private void setState(java.lang.String state) {
        if (!java.util.Objects.equals(this.mState.get(), state)) {
            this.mState.set(state);
            if (this.mRecordStateChanges) {
                this.mRecordedStates.add(state);
            }
            this.mMetricsLogger.onStateChange(state);
        }
    }

    private void stopProviders(java.lang.String reason) {
        stopProviderIfStarted(this.mPrimaryProvider);
        stopProviderIfStarted(this.mSecondaryProvider);
        cancelUncertaintyTimeout();
        setStateAndReportStatusOnlyEvent(STATE_STOPPED, "Providers stopped: " + reason);
    }

    private void stopProviderIfStarted(com.android.server.timezonedetector.location.LocationTimeZoneProvider provider) {
        if (provider.getCurrentState().isStarted()) {
            stopProvider(provider);
        }
    }

    private void stopProvider(com.android.server.timezonedetector.location.LocationTimeZoneProvider provider) {
        com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState providerState = provider.getCurrentState();
        switch (providerState.stateEnum) {
            case 1:
            case 2:
            case 3:
                com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("Stopping " + provider);
                provider.stopUpdates();
                break;
            case 4:
                com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("No need to stop " + provider + ": already stopped");
                break;
            case 5:
            case 6:
                com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("Unable to stop " + provider + ": it is terminated.");
                break;
            default:
                com.android.server.timezonedetector.location.LocationTimeZoneManagerService.warnLog("Unknown provider state: " + provider);
                break;
        }
    }

    private void alterProvidersStartedStateIfRequired(com.android.server.timezonedetector.ConfigurationInternal oldConfiguration, com.android.server.timezonedetector.ConfigurationInternal newConfiguration) {
        boolean oldIsGeoDetectionExecutionEnabled = oldConfiguration != null && oldConfiguration.isGeoDetectionExecutionEnabled();
        boolean newIsGeoDetectionExecutionEnabled = newConfiguration.isGeoDetectionExecutionEnabled();
        if (oldIsGeoDetectionExecutionEnabled == newIsGeoDetectionExecutionEnabled) {
            return;
        }
        if (newIsGeoDetectionExecutionEnabled) {
            setStateAndReportStatusOnlyEvent(STATE_INITIALIZING, "initializing()");
            tryStartProvider(this.mPrimaryProvider, newConfiguration);
            com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState newPrimaryState = this.mPrimaryProvider.getCurrentState();
            if (!newPrimaryState.isStarted()) {
                tryStartProvider(this.mSecondaryProvider, newConfiguration);
                com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState newSecondaryState = this.mSecondaryProvider.getCurrentState();
                if (!newSecondaryState.isStarted()) {
                    java.lang.String reason = "Providers are failed: primary=" + this.mPrimaryProvider.getCurrentState() + " secondary=" + this.mPrimaryProvider.getCurrentState();
                    setStateAndReportStatusOnlyEvent(STATE_FAILED, reason);
                    return;
                }
                return;
            }
            return;
        }
        stopProviders("Geo detection behavior disabled");
    }

    private void tryStartProvider(com.android.server.timezonedetector.location.LocationTimeZoneProvider provider, com.android.server.timezonedetector.ConfigurationInternal configuration) {
        com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState providerState = provider.getCurrentState();
        switch (providerState.stateEnum) {
            case 1:
            case 2:
            case 3:
                com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("No need to start " + provider + ": already started");
                return;
            case 4:
                com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("Enabling " + provider);
                provider.startUpdates(configuration, this.mEnvironment.getProviderInitializationTimeout(), this.mEnvironment.getProviderInitializationTimeoutFuzz(), this.mEnvironment.getProviderEventFilteringAgeThreshold());
                return;
            case 5:
            case 6:
                com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("Unable to start " + provider + ": it is terminated");
                return;
            default:
                throw new java.lang.IllegalStateException("Unknown provider state: provider=" + provider);
        }
    }

    void onProviderStateChange(com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState providerState) {
        this.mThreadingDomain.assertCurrentThread();
        com.android.server.timezonedetector.location.LocationTimeZoneProvider provider = providerState.provider;
        assertProviderKnown(provider);
        synchronized (this.mSharedLock) {
            if (java.util.Objects.equals(this.mState.get(), STATE_PROVIDERS_INITIALIZING)) {
                com.android.server.timezonedetector.location.LocationTimeZoneManagerService.warnLog("onProviderStateChange: Ignoring provider state change because both providers have not yet completed initialization. providerState=" + providerState);
                return;
            }
            switch (providerState.stateEnum) {
                case 1:
                case 4:
                case 6:
                    com.android.server.timezonedetector.location.LocationTimeZoneManagerService.warnLog("onProviderStateChange: Unexpected state change for provider, provider=" + provider);
                    break;
                case 2:
                case 3:
                    com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("onProviderStateChange: Received notification of a state change while started, provider=" + provider);
                    handleProviderStartedStateChange(providerState);
                    break;
                case 5:
                    com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("Received notification of permanent failure for provider=" + provider);
                    handleProviderFailedStateChange(providerState);
                    break;
                default:
                    com.android.server.timezonedetector.location.LocationTimeZoneManagerService.warnLog("onProviderStateChange: Unexpected provider=" + provider);
                    break;
            }
        }
    }

    private void assertProviderKnown(com.android.server.timezonedetector.location.LocationTimeZoneProvider provider) {
        if (provider != this.mPrimaryProvider && provider != this.mSecondaryProvider) {
            throw new java.lang.IllegalArgumentException("Unknown provider: " + provider);
        }
    }

    private void handleProviderFailedStateChange(com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState providerState) {
        com.android.server.timezonedetector.location.LocationTimeZoneProvider failedProvider = providerState.provider;
        com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState primaryCurrentState = this.mPrimaryProvider.getCurrentState();
        com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState secondaryCurrentState = this.mSecondaryProvider.getCurrentState();
        if (failedProvider == this.mPrimaryProvider) {
            if (!secondaryCurrentState.isTerminated()) {
                tryStartProvider(this.mSecondaryProvider, this.mCurrentUserConfiguration);
            }
        } else if (failedProvider == this.mSecondaryProvider && primaryCurrentState.stateEnum != 3 && !primaryCurrentState.isTerminated()) {
            com.android.server.timezonedetector.location.LocationTimeZoneManagerService.warnLog("Secondary provider unexpected reported a failure: failed provider=" + failedProvider.getName() + ", primary provider=" + this.mPrimaryProvider + ", secondary provider=" + this.mSecondaryProvider);
        }
        if (primaryCurrentState.isTerminated() && secondaryCurrentState.isTerminated()) {
            cancelUncertaintyTimeout();
            java.lang.String reason = "Both providers are terminated: primary=" + primaryCurrentState.provider + ", secondary=" + secondaryCurrentState.provider;
            setStateAndReportStatusOnlyEvent(STATE_FAILED, reason);
        }
    }

    private void handleProviderStartedStateChange(com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState providerState) {
        com.android.server.timezonedetector.location.LocationTimeZoneProvider provider = providerState.provider;
        android.service.timezone.TimeZoneProviderEvent event = providerState.event;
        if (event == null) {
            long uncertaintyStartedElapsedMillis = this.mEnvironment.elapsedRealtimeMillis();
            handleProviderUncertainty(provider, uncertaintyStartedElapsedMillis, "provider=" + provider + ", implicit uncertainty, event=null");
        }
        if (!this.mCurrentUserConfiguration.isGeoDetectionExecutionEnabled()) {
            com.android.server.timezonedetector.location.LocationTimeZoneManagerService.warnLog("Provider=" + provider + " is started, but currentUserConfiguration=" + this.mCurrentUserConfiguration + " suggests it shouldn't be.");
        }
        switch (event.getType()) {
            case 1:
                com.android.server.timezonedetector.location.LocationTimeZoneManagerService.warnLog("Provider=" + provider + " is started, but event suggests it shouldn't be");
                break;
            case 2:
                handleProviderSuggestion(provider, event);
                break;
            case 3:
                long uncertaintyStartedElapsedMillis2 = event.getCreationElapsedMillis();
                handleProviderUncertainty(provider, uncertaintyStartedElapsedMillis2, "provider=" + provider + ", explicit uncertainty. event=" + event);
                break;
            default:
                com.android.server.timezonedetector.location.LocationTimeZoneManagerService.warnLog("Unknown eventType=" + event.getType());
                break;
        }
    }

    private void handleProviderSuggestion(com.android.server.timezonedetector.location.LocationTimeZoneProvider provider, android.service.timezone.TimeZoneProviderEvent providerEvent) {
        cancelUncertaintyTimeout();
        if (provider == this.mPrimaryProvider) {
            stopProviderIfStarted(this.mSecondaryProvider);
        }
        android.service.timezone.TimeZoneProviderSuggestion providerSuggestion = providerEvent.getSuggestion();
        setState(STATE_CERTAIN);
        long effectiveFromElapsedMillis = providerSuggestion.getElapsedRealtimeMillis();
        com.android.server.timezonedetector.GeolocationTimeZoneSuggestion suggestion = com.android.server.timezonedetector.GeolocationTimeZoneSuggestion.createCertainSuggestion(effectiveFromElapsedMillis, providerSuggestion.getTimeZoneIds());
        java.lang.String debugInfo = "Provider event received: provider=" + provider + ", providerEvent=" + providerEvent + ", suggestionCreationTime=" + this.mEnvironment.elapsedRealtimeMillis();
        reportSuggestionEvent(suggestion, debugInfo);
    }

    @Override // com.android.server.timezonedetector.Dumpable
    public void dump(android.util.IndentingPrintWriter ipw, java.lang.String[] args) {
        synchronized (this.mSharedLock) {
            ipw.println("LocationTimeZoneProviderController:");
            ipw.increaseIndent();
            ipw.println("mCurrentUserConfiguration=" + this.mCurrentUserConfiguration);
            ipw.println("providerInitializationTimeout=" + this.mEnvironment.getProviderInitializationTimeout());
            ipw.println("providerInitializationTimeoutFuzz=" + this.mEnvironment.getProviderInitializationTimeoutFuzz());
            ipw.println("uncertaintyDelay=" + this.mEnvironment.getUncertaintyDelay());
            ipw.println("mState=" + this.mState.get());
            ipw.println("mLastEvent=" + this.mLastEvent);
            ipw.println("State history:");
            ipw.increaseIndent();
            this.mState.dump(ipw);
            ipw.decreaseIndent();
            ipw.println("Primary Provider:");
            ipw.increaseIndent();
            this.mPrimaryProvider.dump(ipw, args);
            ipw.decreaseIndent();
            ipw.println("Secondary Provider:");
            ipw.increaseIndent();
            this.mSecondaryProvider.dump(ipw, args);
            ipw.decreaseIndent();
            ipw.decreaseIndent();
        }
    }

    private void cancelUncertaintyTimeout() {
        this.mUncertaintyTimeoutQueue.cancel();
    }

    void handleProviderUncertainty(final com.android.server.timezonedetector.location.LocationTimeZoneProvider provider, final long uncertaintyStartedElapsedMillis, java.lang.String reason) {
        java.util.Objects.requireNonNull(provider);
        if (!this.mUncertaintyTimeoutQueue.hasQueued()) {
            if (STATE_UNCERTAIN.equals(this.mState.get())) {
                com.android.server.timezonedetector.GeolocationTimeZoneSuggestion suggestion = com.android.server.timezonedetector.GeolocationTimeZoneSuggestion.createUncertainSuggestion(uncertaintyStartedElapsedMillis);
                java.lang.String debugInfo = "Uncertainty received from " + provider.getName() + ": primary=" + this.mPrimaryProvider + ", secondary=" + this.mSecondaryProvider + ", uncertaintyStarted=" + java.time.Duration.ofMillis(uncertaintyStartedElapsedMillis);
                reportSuggestionEvent(suggestion, debugInfo);
            } else {
                com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("Starting uncertainty timeout: reason=" + reason);
                final java.time.Duration uncertaintyDelay = this.mEnvironment.getUncertaintyDelay();
                this.mUncertaintyTimeoutQueue.runDelayed(new java.lang.Runnable() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneProviderController$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleProviderUncertainty$0(provider, uncertaintyStartedElapsedMillis, uncertaintyDelay);
                    }
                }, uncertaintyDelay.toMillis());
            }
        }
        if (provider == this.mPrimaryProvider) {
            tryStartProvider(this.mSecondaryProvider, this.mCurrentUserConfiguration);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onProviderUncertaintyTimeout, reason: merged with bridge method [inline-methods] */
    public void lambda$handleProviderUncertainty$0(com.android.server.timezonedetector.location.LocationTimeZoneProvider provider, long uncertaintyStartedElapsedMillis, java.time.Duration uncertaintyDelay) {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            long afterUncertaintyTimeoutElapsedMillis = this.mEnvironment.elapsedRealtimeMillis();
            setState(STATE_UNCERTAIN);
            com.android.server.timezonedetector.GeolocationTimeZoneSuggestion suggestion = com.android.server.timezonedetector.GeolocationTimeZoneSuggestion.createUncertainSuggestion(uncertaintyStartedElapsedMillis);
            java.lang.String debugInfo = "Uncertainty timeout triggered for " + provider.getName() + ": primary=" + this.mPrimaryProvider + ", secondary=" + this.mSecondaryProvider + ", uncertaintyStarted=" + java.time.Duration.ofMillis(uncertaintyStartedElapsedMillis) + ", afterUncertaintyTimeout=" + java.time.Duration.ofMillis(afterUncertaintyTimeoutElapsedMillis) + ", uncertaintyDelay=" + uncertaintyDelay;
            reportSuggestionEvent(suggestion, debugInfo);
        }
    }

    private android.app.time.LocationTimeZoneAlgorithmStatus generateCurrentAlgorithmStatus() {
        java.lang.String controllerState = this.mState.get();
        com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState primaryProviderState = this.mPrimaryProvider.getCurrentState();
        com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState secondaryProviderState = this.mSecondaryProvider.getCurrentState();
        return createAlgorithmStatus(controllerState, primaryProviderState, secondaryProviderState);
    }

    private static android.app.time.LocationTimeZoneAlgorithmStatus createAlgorithmStatus(java.lang.String controllerState, com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState primaryProviderState, com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState secondaryProviderState) {
        int algorithmStatus = mapControllerStateToDetectionAlgorithmStatus(controllerState);
        int primaryProviderStatus = primaryProviderState.getProviderStatus();
        int secondaryProviderStatus = secondaryProviderState.getProviderStatus();
        return new android.app.time.LocationTimeZoneAlgorithmStatus(algorithmStatus, primaryProviderStatus, primaryProviderState.getReportedStatus(), secondaryProviderStatus, secondaryProviderState.getReportedStatus());
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:29:0x005a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static int mapControllerStateToDetectionAlgorithmStatus(java.lang.String r3) {
        /*
            int r0 = r3.hashCode()
            r1 = 2
            r2 = 3
            switch(r0) {
                case -1166336595: goto L50;
                case -468307734: goto L46;
                case 433141802: goto L3c;
                case 478389753: goto L32;
                case 872357833: goto L28;
                case 1386911874: goto L1e;
                case 1917201485: goto L14;
                case 2066319421: goto La;
                default: goto L9;
            }
        L9:
            goto L5a
        La:
            java.lang.String r0 = "FAILED"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L9
            r0 = 6
            goto L5b
        L14:
            java.lang.String r0 = "INITIALIZING"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L9
            r0 = 0
            goto L5b
        L1e:
            java.lang.String r0 = "CERTAIN"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L9
            r0 = r1
            goto L5b
        L28:
            java.lang.String r0 = "UNCERTAIN"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L9
            r0 = r2
            goto L5b
        L32:
            java.lang.String r0 = "DESTROYED"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L9
            r0 = 5
            goto L5b
        L3c:
            java.lang.String r0 = "UNKNOWN"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L9
            r0 = 7
            goto L5b
        L46:
            java.lang.String r0 = "PROVIDERS_INITIALIZING"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L9
            r0 = 1
            goto L5b
        L50:
            java.lang.String r0 = "STOPPED"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L9
            r0 = 4
            goto L5b
        L5a:
            r0 = -1
        L5b:
            switch(r0) {
                case 0: goto L5f;
                case 1: goto L5f;
                case 2: goto L5f;
                case 3: goto L5f;
                default: goto L5e;
            }
        L5e:
            return r1
        L5f:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.timezonedetector.location.LocationTimeZoneProviderController.mapControllerStateToDetectionAlgorithmStatus(java.lang.String):int");
    }

    void clearRecordedStates() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            this.mRecordedStates.clear();
            this.mPrimaryProvider.clearRecordedStates();
            this.mSecondaryProvider.clearRecordedStates();
        }
    }

    com.android.server.timezonedetector.location.LocationTimeZoneManagerServiceState getStateForTests() {
        com.android.server.timezonedetector.location.LocationTimeZoneManagerServiceState locationTimeZoneManagerServiceStateBuild;
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            com.android.server.timezonedetector.location.LocationTimeZoneManagerServiceState.Builder builder = new com.android.server.timezonedetector.location.LocationTimeZoneManagerServiceState.Builder();
            if (this.mLastEvent != null) {
                builder.setLastEvent(this.mLastEvent);
            }
            builder.setControllerState(this.mState.get()).setStateChanges(this.mRecordedStates).setPrimaryProviderStateChanges(this.mPrimaryProvider.getRecordedStates()).setSecondaryProviderStateChanges(this.mSecondaryProvider.getRecordedStates());
            locationTimeZoneManagerServiceStateBuild = builder.build();
        }
        return locationTimeZoneManagerServiceStateBuild;
    }

    static abstract class Environment {
        protected final java.lang.Object mSharedLock;
        protected final com.android.server.timezonedetector.location.ThreadingDomain mThreadingDomain;

        abstract void destroy();

        abstract long elapsedRealtimeMillis();

        abstract com.android.server.timezonedetector.ConfigurationInternal getCurrentUserConfigurationInternal();

        abstract java.time.Duration getProviderEventFilteringAgeThreshold();

        abstract java.time.Duration getProviderInitializationTimeout();

        abstract java.time.Duration getProviderInitializationTimeoutFuzz();

        abstract java.time.Duration getUncertaintyDelay();

        Environment(com.android.server.timezonedetector.location.ThreadingDomain threadingDomain) {
            this.mThreadingDomain = (com.android.server.timezonedetector.location.ThreadingDomain) java.util.Objects.requireNonNull(threadingDomain);
            this.mSharedLock = threadingDomain.getLockObject();
        }
    }

    static abstract class Callback {
        protected final com.android.server.timezonedetector.location.ThreadingDomain mThreadingDomain;

        abstract void sendEvent(com.android.server.timezonedetector.LocationAlgorithmEvent locationAlgorithmEvent);

        Callback(com.android.server.timezonedetector.location.ThreadingDomain threadingDomain) {
            this.mThreadingDomain = (com.android.server.timezonedetector.location.ThreadingDomain) java.util.Objects.requireNonNull(threadingDomain);
        }
    }
}
