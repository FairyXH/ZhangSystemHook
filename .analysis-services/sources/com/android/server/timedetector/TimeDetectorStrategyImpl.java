package com.android.server.timedetector;

/* JADX INFO: loaded from: classes3.dex */
public final class TimeDetectorStrategyImpl implements com.android.server.timedetector.TimeDetectorStrategy {
    private static final boolean DBG = false;
    private static final int KEEP_SUGGESTION_HISTORY_SIZE = 10;
    private static final java.lang.String LOG_TAG = "time_detector";
    static final long MAX_SUGGESTION_TIME_AGE_MILLIS = 86400000;
    private static final long SYSTEM_CLOCK_PARANOIA_THRESHOLD_MILLIS = 2000;
    private static final int TELEPHONY_BUCKET_COUNT = 24;
    static final int TELEPHONY_BUCKET_SIZE_MILLIS = 3600000;
    private static final int TELEPHONY_INVALID_SCORE = -1;
    private com.android.server.timedetector.ConfigurationInternal mCurrentConfigurationInternal;
    private final com.android.server.timedetector.TimeDetectorStrategyImpl.Environment mEnvironment;
    private android.app.time.UnixEpochTime mLastAutoSystemClockTimeSet;
    private final com.android.server.timedetector.ServiceConfigAccessor mServiceConfigAccessor;
    private final java.util.List<com.android.server.timezonedetector.StateChangeListener> mStateChangeListeners = new java.util.ArrayList();
    private final com.android.server.timezonedetector.ArrayMapWithHistory<java.lang.Integer, android.app.timedetector.TelephonyTimeSuggestion> mSuggestionBySlotIndex = new com.android.server.timezonedetector.ArrayMapWithHistory<>(10);
    private final com.android.server.timezonedetector.ReferenceWithHistory<com.android.server.timedetector.NetworkTimeSuggestion> mLastNetworkSuggestion = new com.android.server.timezonedetector.ReferenceWithHistory<>(10);
    private final com.android.server.timezonedetector.ReferenceWithHistory<com.android.server.timedetector.GnssTimeSuggestion> mLastGnssSuggestion = new com.android.server.timezonedetector.ReferenceWithHistory<>(10);
    private final com.android.server.timezonedetector.ReferenceWithHistory<android.app.time.ExternalTimeSuggestion> mLastExternalSuggestion = new com.android.server.timezonedetector.ReferenceWithHistory<>(10);
    private final android.util.ArraySet<com.android.server.timezonedetector.StateChangeListener> mNetworkTimeUpdateListeners = new android.util.ArraySet<>();

    public interface Environment {
        void acquireWakeLock();

        void addDebugLogEntry(java.lang.String str);

        void dumpDebugLog(android.util.IndentingPrintWriter indentingPrintWriter);

        long elapsedRealtimeMillis();

        void releaseWakeLock();

        void runAsync(java.lang.Runnable runnable);

        void setSystemClock(long j, int i, java.lang.String str);

        void setSystemClockConfidence(int i, java.lang.String str);

        int systemClockConfidence();

        long systemClockMillis();
    }

    static com.android.server.timedetector.TimeDetectorStrategy create(android.content.Context context, android.os.Handler handler, com.android.server.timedetector.ServiceConfigAccessor serviceConfigAccessor) {
        com.android.server.timedetector.TimeDetectorStrategyImpl.Environment environment = new com.android.server.timedetector.EnvironmentImpl(context, handler);
        return new com.android.server.timedetector.TimeDetectorStrategyImpl(environment, serviceConfigAccessor);
    }

    TimeDetectorStrategyImpl(com.android.server.timedetector.TimeDetectorStrategyImpl.Environment environment, com.android.server.timedetector.ServiceConfigAccessor serviceConfigAccessor) {
        this.mEnvironment = (com.android.server.timedetector.TimeDetectorStrategyImpl.Environment) java.util.Objects.requireNonNull(environment);
        this.mServiceConfigAccessor = (com.android.server.timedetector.ServiceConfigAccessor) java.util.Objects.requireNonNull(serviceConfigAccessor);
        synchronized (this) {
            com.android.server.timezonedetector.StateChangeListener stateChangeListener = new com.android.server.timezonedetector.StateChangeListener() { // from class: com.android.server.timedetector.TimeDetectorStrategyImpl$$ExternalSyntheticLambda1
                @Override // com.android.server.timezonedetector.StateChangeListener
                public final void onChange() {
                    this.f$0.handleConfigurationInternalMaybeChanged();
                }
            };
            this.mServiceConfigAccessor.addConfigurationInternalChangeListener(stateChangeListener);
            updateCurrentConfigurationInternalIfRequired("TimeDetectorStrategyImpl:");
        }
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized void suggestExternalTime(android.app.time.ExternalTimeSuggestion suggestion) {
        com.android.server.timedetector.ConfigurationInternal configurationInternal = this.mCurrentConfigurationInternal;
        java.util.Objects.requireNonNull(suggestion);
        android.app.time.UnixEpochTime newUnixEpochTime = suggestion.getUnixEpochTime();
        if (validateAutoSuggestionTime(newUnixEpochTime, suggestion)) {
            this.mLastExternalSuggestion.set(suggestion);
            java.lang.String reason = "External time suggestion received: suggestion=" + suggestion;
            doAutoTimeDetection(reason);
        }
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized void suggestGnssTime(com.android.server.timedetector.GnssTimeSuggestion suggestion) {
        com.android.server.timedetector.ConfigurationInternal configurationInternal = this.mCurrentConfigurationInternal;
        java.util.Objects.requireNonNull(suggestion);
        android.app.time.UnixEpochTime newUnixEpochTime = suggestion.getUnixEpochTime();
        if (validateAutoSuggestionTime(newUnixEpochTime, suggestion)) {
            this.mLastGnssSuggestion.set(suggestion);
            java.lang.String reason = "GNSS time suggestion received: suggestion=" + suggestion;
            doAutoTimeDetection(reason);
        }
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized boolean suggestManualTime(int userId, android.app.timedetector.ManualTimeSuggestion suggestion, boolean bypassUserPolicyChecks) {
        com.android.server.timedetector.ConfigurationInternal currentUserConfig = this.mCurrentConfigurationInternal;
        if (currentUserConfig.getUserId() != userId) {
            android.util.Slog.w(LOG_TAG, "Manual suggestion received but user != current user, userId=" + userId + " suggestion=" + suggestion);
            return false;
        }
        java.util.Objects.requireNonNull(suggestion);
        java.lang.String cause = "Manual time suggestion received: suggestion=" + suggestion;
        android.app.time.TimeCapabilitiesAndConfig capabilitiesAndConfig = currentUserConfig.createCapabilitiesAndConfig(bypassUserPolicyChecks);
        android.app.time.TimeCapabilities capabilities = capabilitiesAndConfig.getCapabilities();
        if (capabilities.getSetManualTimeCapability() != 40) {
            android.util.Slog.i(LOG_TAG, "User does not have the capability needed to set the time manually: capabilities=" + capabilities + ", suggestion=" + suggestion + ", cause=" + cause);
            return false;
        }
        android.app.time.UnixEpochTime newUnixEpochTime = suggestion.getUnixEpochTime();
        if (!validateManualSuggestionTime(newUnixEpochTime, suggestion)) {
            return false;
        }
        return setSystemClockAndConfidenceIfRequired(2, newUnixEpochTime, cause);
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized void suggestNetworkTime(com.android.server.timedetector.NetworkTimeSuggestion suggestion) {
        com.android.server.timedetector.ConfigurationInternal configurationInternal = this.mCurrentConfigurationInternal;
        java.util.Objects.requireNonNull(suggestion);
        if (validateAutoSuggestionTime(suggestion.getUnixEpochTime(), suggestion)) {
            com.android.server.timedetector.NetworkTimeSuggestion lastNetworkSuggestion = this.mLastNetworkSuggestion.get();
            if (lastNetworkSuggestion == null || !lastNetworkSuggestion.equals(suggestion)) {
                this.mLastNetworkSuggestion.set(suggestion);
                notifyNetworkTimeUpdateListenersAsynchronously();
            }
            java.lang.String reason = "New network time suggested. suggestion=" + suggestion;
            doAutoTimeDetection(reason);
        }
    }

    private void notifyNetworkTimeUpdateListenersAsynchronously() {
        for (com.android.server.timezonedetector.StateChangeListener listener : this.mNetworkTimeUpdateListeners) {
            com.android.server.timedetector.TimeDetectorStrategyImpl.Environment environment = this.mEnvironment;
            java.util.Objects.requireNonNull(listener);
            environment.runAsync(new com.android.server.timedetector.TimeDetectorStrategyImpl$$ExternalSyntheticLambda0(listener));
        }
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized void addNetworkTimeUpdateListener(com.android.server.timezonedetector.StateChangeListener networkSuggestionUpdateListener) {
        this.mNetworkTimeUpdateListeners.add(networkSuggestionUpdateListener);
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized com.android.server.timedetector.NetworkTimeSuggestion getLatestNetworkSuggestion() {
        return this.mLastNetworkSuggestion.get();
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized void clearLatestNetworkSuggestion() {
        this.mLastNetworkSuggestion.set(null);
        notifyNetworkTimeUpdateListenersAsynchronously();
        doAutoTimeDetection("Network time cleared");
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized android.app.time.TimeState getTimeState() {
        boolean userShouldConfirmTime;
        android.app.time.UnixEpochTime unixEpochTime;
        userShouldConfirmTime = this.mEnvironment.systemClockConfidence() < 100;
        unixEpochTime = new android.app.time.UnixEpochTime(this.mEnvironment.elapsedRealtimeMillis(), this.mEnvironment.systemClockMillis());
        return new android.app.time.TimeState(unixEpochTime, userShouldConfirmTime);
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized void setTimeState(android.app.time.TimeState timeState) {
        java.util.Objects.requireNonNull(timeState);
        int confidence = timeState.getUserShouldConfirmTime() ? 0 : 100;
        this.mEnvironment.acquireWakeLock();
        try {
            android.app.time.UnixEpochTime unixEpochTime = timeState.getUnixEpochTime();
            setSystemClockAndConfidenceUnderWakeLock(2, unixEpochTime, confidence, "setTimeState()");
        } finally {
            this.mEnvironment.releaseWakeLock();
        }
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized boolean confirmTime(android.app.time.UnixEpochTime confirmationTime) {
        boolean timeConfirmed;
        java.util.Objects.requireNonNull(confirmationTime);
        this.mEnvironment.acquireWakeLock();
        try {
            long currentElapsedRealtimeMillis = this.mEnvironment.elapsedRealtimeMillis();
            long currentSystemClockMillis = this.mEnvironment.systemClockMillis();
            timeConfirmed = isTimeWithinConfidenceThreshold(confirmationTime, currentElapsedRealtimeMillis, currentSystemClockMillis);
            if (timeConfirmed) {
                try {
                    int currentTimeConfidence = this.mEnvironment.systemClockConfidence();
                    boolean confidenceUpgradeRequired = currentTimeConfidence < 100;
                    if (confidenceUpgradeRequired) {
                        java.lang.String logMsg = "Confirm system clock time. confirmationTime=" + confirmationTime + " newTimeConfidence=100 currentElapsedRealtimeMillis=" + currentElapsedRealtimeMillis + " currentSystemClockMillis=" + currentSystemClockMillis + " (old) currentTimeConfidence=" + currentTimeConfidence;
                        this.mEnvironment.setSystemClockConfidence(100, logMsg);
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    this.mEnvironment.releaseWakeLock();
                    throw th;
                }
            }
            this.mEnvironment.releaseWakeLock();
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
        return timeConfirmed;
    }

    private void notifyStateChangeListenersAsynchronously() {
        for (com.android.server.timezonedetector.StateChangeListener listener : this.mStateChangeListeners) {
            com.android.server.timedetector.TimeDetectorStrategyImpl.Environment environment = this.mEnvironment;
            java.util.Objects.requireNonNull(listener);
            environment.runAsync(new com.android.server.timedetector.TimeDetectorStrategyImpl$$ExternalSyntheticLambda0(listener));
        }
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized void addChangeListener(com.android.server.timezonedetector.StateChangeListener listener) {
        this.mStateChangeListeners.add(listener);
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized android.app.time.TimeCapabilitiesAndConfig getCapabilitiesAndConfig(int userId, boolean bypassUserPolicyChecks) {
        com.android.server.timedetector.ConfigurationInternal configurationInternal;
        if (this.mCurrentConfigurationInternal.getUserId() == userId) {
            configurationInternal = this.mCurrentConfigurationInternal;
        } else {
            configurationInternal = this.mServiceConfigAccessor.getConfigurationInternal(userId);
        }
        return configurationInternal.createCapabilitiesAndConfig(bypassUserPolicyChecks);
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized boolean updateConfiguration(int userId, android.app.time.TimeConfiguration configuration, boolean bypassUserPolicyChecks) {
        boolean updateSuccessful;
        updateSuccessful = this.mServiceConfigAccessor.updateConfiguration(userId, configuration, bypassUserPolicyChecks);
        if (updateSuccessful) {
            java.lang.String logMsg = "updateConfiguration: userId=" + userId + ", configuration=" + configuration + ", bypassUserPolicyChecks=" + bypassUserPolicyChecks;
            updateCurrentConfigurationInternalIfRequired(logMsg);
        }
        return updateSuccessful;
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized void suggestTelephonyTime(android.app.timedetector.TelephonyTimeSuggestion suggestion) {
        if (suggestion.getUnixEpochTime() == null) {
            return;
        }
        if (validateAutoSuggestionTime(suggestion.getUnixEpochTime(), suggestion)) {
            if (storeTelephonySuggestion(suggestion)) {
                java.lang.String reason = "New telephony time suggested. suggestion=" + suggestion;
                doAutoTimeDetection(reason);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleConfigurationInternalMaybeChanged() {
        updateCurrentConfigurationInternalIfRequired("handleConfigurationInternalMaybeChanged:");
    }

    private void updateCurrentConfigurationInternalIfRequired(java.lang.String logMsg) {
        com.android.server.timedetector.ConfigurationInternal newCurrentConfigurationInternal = this.mServiceConfigAccessor.getCurrentUserConfigurationInternal();
        com.android.server.timedetector.ConfigurationInternal oldCurrentConfigurationInternal = this.mCurrentConfigurationInternal;
        if (!newCurrentConfigurationInternal.equals(oldCurrentConfigurationInternal)) {
            this.mCurrentConfigurationInternal = newCurrentConfigurationInternal;
            addDebugLogEntry(logMsg + " [oldConfiguration=" + oldCurrentConfigurationInternal + ", newConfiguration=" + newCurrentConfigurationInternal + "]");
            notifyStateChangeListenersAsynchronously();
            boolean autoDetectionEnabled = this.mCurrentConfigurationInternal.getAutoDetectionEnabledBehavior();
            if (autoDetectionEnabled) {
                doAutoTimeDetection("Auto time detection config changed.");
            } else {
                this.mLastAutoSystemClockTimeSet = null;
            }
        }
    }

    private void addDebugLogEntry(java.lang.String logMsg) {
        this.mEnvironment.addDebugLogEntry(logMsg);
    }

    @Override // com.android.server.timezonedetector.Dumpable
    public synchronized void dump(android.util.IndentingPrintWriter ipw, java.lang.String[] args) {
        ipw.println("TimeDetectorStrategy:");
        ipw.increaseIndent();
        ipw.println("mLastAutoSystemClockTimeSet=" + this.mLastAutoSystemClockTimeSet);
        ipw.println("mCurrentConfigurationInternal=" + this.mCurrentConfigurationInternal);
        ipw.println("[Capabilities=" + this.mCurrentConfigurationInternal.createCapabilitiesAndConfig(false) + "]");
        ipw.println("mEnvironment:");
        ipw.increaseIndent();
        this.mEnvironment.dumpDebugLog(ipw);
        ipw.decreaseIndent();
        ipw.println("Time change log:");
        ipw.increaseIndent();
        com.android.server.SystemClockTime.dump(ipw);
        ipw.decreaseIndent();
        ipw.println("Telephony suggestion history:");
        ipw.increaseIndent();
        this.mSuggestionBySlotIndex.dump(ipw);
        ipw.decreaseIndent();
        ipw.println("Network suggestion history:");
        ipw.increaseIndent();
        this.mLastNetworkSuggestion.dump(ipw);
        ipw.decreaseIndent();
        ipw.println("Gnss suggestion history:");
        ipw.increaseIndent();
        this.mLastGnssSuggestion.dump(ipw);
        ipw.decreaseIndent();
        ipw.println("External suggestion history:");
        ipw.increaseIndent();
        this.mLastExternalSuggestion.dump(ipw);
        ipw.decreaseIndent();
        ipw.decreaseIndent();
    }

    public synchronized com.android.server.timedetector.ConfigurationInternal getCachedCapabilitiesAndConfigForTests() {
        return this.mCurrentConfigurationInternal;
    }

    private boolean storeTelephonySuggestion(android.app.timedetector.TelephonyTimeSuggestion suggestion) {
        android.app.time.UnixEpochTime newUnixEpochTime = suggestion.getUnixEpochTime();
        int slotIndex = suggestion.getSlotIndex();
        android.app.timedetector.TelephonyTimeSuggestion previousSuggestion = this.mSuggestionBySlotIndex.get(java.lang.Integer.valueOf(slotIndex));
        if (previousSuggestion != null) {
            if (previousSuggestion.getUnixEpochTime() == null) {
                android.util.Slog.w(LOG_TAG, "Previous suggestion is null or has a null time. previousSuggestion=" + previousSuggestion + ", suggestion=" + suggestion);
                return false;
            }
            long referenceTimeDifference = android.app.time.UnixEpochTime.elapsedRealtimeDifference(newUnixEpochTime, previousSuggestion.getUnixEpochTime());
            if (referenceTimeDifference < 0) {
                android.util.Slog.w(LOG_TAG, "Out of order telephony suggestion received. referenceTimeDifference=" + referenceTimeDifference + " previousSuggestion=" + previousSuggestion + " suggestion=" + suggestion);
                return false;
            }
        }
        this.mSuggestionBySlotIndex.put(java.lang.Integer.valueOf(slotIndex), suggestion);
        return true;
    }

    private boolean validateSuggestionCommon(android.app.time.UnixEpochTime newUnixEpochTime, java.lang.Object suggestion) {
        long elapsedRealtimeMillis = this.mEnvironment.elapsedRealtimeMillis();
        if (elapsedRealtimeMillis < newUnixEpochTime.getElapsedRealtimeMillis()) {
            android.util.Slog.w(LOG_TAG, "New elapsed realtime is in the future? Ignoring. elapsedRealtimeMillis=" + elapsedRealtimeMillis + ", suggestion=" + suggestion);
            return false;
        }
        if (newUnixEpochTime.getUnixEpochTimeMillis() > this.mCurrentConfigurationInternal.getSuggestionUpperBound().toEpochMilli()) {
            android.util.Slog.w(LOG_TAG, "Suggested value is above max time supported by this device. suggestion=" + suggestion);
            return false;
        }
        return true;
    }

    private boolean validateAutoSuggestionTime(android.app.time.UnixEpochTime newUnixEpochTime, java.lang.Object suggestion) {
        java.time.Instant lowerBound = this.mCurrentConfigurationInternal.getAutoSuggestionLowerBound();
        return validateSuggestionCommon(newUnixEpochTime, suggestion) && validateSuggestionAgainstLowerBound(newUnixEpochTime, suggestion, lowerBound);
    }

    private boolean validateManualSuggestionTime(android.app.time.UnixEpochTime newUnixEpochTime, java.lang.Object suggestion) {
        java.time.Instant lowerBound = this.mCurrentConfigurationInternal.getManualSuggestionLowerBound();
        return validateSuggestionCommon(newUnixEpochTime, suggestion) && validateSuggestionAgainstLowerBound(newUnixEpochTime, suggestion, lowerBound);
    }

    private boolean validateSuggestionAgainstLowerBound(android.app.time.UnixEpochTime newUnixEpochTime, java.lang.Object suggestion, java.time.Instant lowerBound) {
        if (lowerBound.toEpochMilli() > newUnixEpochTime.getUnixEpochTimeMillis()) {
            android.util.Slog.w(LOG_TAG, "Suggestion points to time before lower bound, skipping it. suggestion=" + suggestion + ", lower bound=" + lowerBound);
            return false;
        }
        return true;
    }

    private void doAutoTimeDetection(java.lang.String detectionReason) {
        int[] originPriorities = this.mCurrentConfigurationInternal.getAutoOriginPriorities();
        for (int origin : originPriorities) {
            android.app.time.UnixEpochTime newUnixEpochTime = null;
            java.lang.String cause = null;
            if (origin == 1) {
                android.app.timedetector.TelephonyTimeSuggestion bestTelephonySuggestion = findBestTelephonySuggestion();
                if (bestTelephonySuggestion != null) {
                    newUnixEpochTime = bestTelephonySuggestion.getUnixEpochTime();
                    cause = "Found good telephony suggestion., bestTelephonySuggestion=" + bestTelephonySuggestion + ", detectionReason=" + detectionReason;
                }
            } else if (origin == 3) {
                com.android.server.timedetector.NetworkTimeSuggestion networkSuggestion = findLatestValidNetworkSuggestion();
                if (networkSuggestion != null) {
                    newUnixEpochTime = networkSuggestion.getUnixEpochTime();
                    cause = "Found good network suggestion., networkSuggestion=" + networkSuggestion + ", detectionReason=" + detectionReason;
                }
            } else if (origin == 4) {
                com.android.server.timedetector.GnssTimeSuggestion gnssSuggestion = findLatestValidGnssSuggestion();
                if (gnssSuggestion != null) {
                    newUnixEpochTime = gnssSuggestion.getUnixEpochTime();
                    cause = "Found good gnss suggestion., gnssSuggestion=" + gnssSuggestion + ", detectionReason=" + detectionReason;
                }
            } else if (origin == 5) {
                android.app.time.ExternalTimeSuggestion externalSuggestion = findLatestValidExternalSuggestion();
                if (externalSuggestion != null) {
                    newUnixEpochTime = externalSuggestion.getUnixEpochTime();
                    cause = "Found good external suggestion., externalSuggestion=" + externalSuggestion + ", detectionReason=" + detectionReason;
                }
            } else {
                android.util.Slog.w(LOG_TAG, "Unknown or unsupported origin=" + origin + " in " + java.util.Arrays.toString(originPriorities) + ": Skipping");
            }
            if (newUnixEpochTime != null) {
                if (this.mCurrentConfigurationInternal.getAutoDetectionEnabledBehavior()) {
                    setSystemClockAndConfidenceIfRequired(origin, newUnixEpochTime, cause);
                    return;
                } else {
                    upgradeSystemClockConfidenceIfRequired(newUnixEpochTime, cause);
                    return;
                }
            }
        }
    }

    private android.app.timedetector.TelephonyTimeSuggestion findBestTelephonySuggestion() {
        long elapsedRealtimeMillis = this.mEnvironment.elapsedRealtimeMillis();
        android.app.timedetector.TelephonyTimeSuggestion bestSuggestion = null;
        int bestScore = -1;
        for (int i = 0; i < this.mSuggestionBySlotIndex.size(); i++) {
            java.lang.Integer slotIndex = this.mSuggestionBySlotIndex.keyAt(i);
            android.app.timedetector.TelephonyTimeSuggestion candidateSuggestion = this.mSuggestionBySlotIndex.valueAt(i);
            if (candidateSuggestion == null) {
                android.util.Slog.w(LOG_TAG, "Latest suggestion unexpectedly null for slotIndex. slotIndex=" + slotIndex);
            } else if (candidateSuggestion.getUnixEpochTime() == null) {
                android.util.Slog.w(LOG_TAG, "Latest suggestion unexpectedly empty.  candidateSuggestion=" + candidateSuggestion);
            } else {
                int candidateScore = scoreTelephonySuggestion(elapsedRealtimeMillis, candidateSuggestion);
                if (candidateScore != -1) {
                    if (bestSuggestion == null || bestScore < candidateScore) {
                        bestSuggestion = candidateSuggestion;
                        bestScore = candidateScore;
                    } else if (bestScore == candidateScore) {
                        int candidateSlotIndex = candidateSuggestion.getSlotIndex();
                        int bestSlotIndex = bestSuggestion.getSlotIndex();
                        if (candidateSlotIndex < bestSlotIndex) {
                            bestSuggestion = candidateSuggestion;
                        }
                    }
                }
            }
        }
        return bestSuggestion;
    }

    private static int scoreTelephonySuggestion(long elapsedRealtimeMillis, android.app.timedetector.TelephonyTimeSuggestion suggestion) {
        android.app.time.UnixEpochTime unixEpochTime = suggestion.getUnixEpochTime();
        if (!validateSuggestionUnixEpochTime(elapsedRealtimeMillis, unixEpochTime)) {
            android.util.Slog.w(LOG_TAG, "Existing suggestion found to be invalid elapsedRealtimeMillis=" + elapsedRealtimeMillis + ", suggestion=" + suggestion);
            return -1;
        }
        long ageMillis = elapsedRealtimeMillis - unixEpochTime.getElapsedRealtimeMillis();
        int bucketIndex = (int) (ageMillis / 3600000);
        if (bucketIndex >= 24) {
            return -1;
        }
        return 24 - bucketIndex;
    }

    private com.android.server.timedetector.NetworkTimeSuggestion findLatestValidNetworkSuggestion() {
        com.android.server.timedetector.NetworkTimeSuggestion networkSuggestion = this.mLastNetworkSuggestion.get();
        if (networkSuggestion == null) {
            return null;
        }
        android.app.time.UnixEpochTime unixEpochTime = networkSuggestion.getUnixEpochTime();
        long elapsedRealTimeMillis = this.mEnvironment.elapsedRealtimeMillis();
        if (!validateSuggestionUnixEpochTime(elapsedRealTimeMillis, unixEpochTime)) {
            return null;
        }
        return networkSuggestion;
    }

    private com.android.server.timedetector.GnssTimeSuggestion findLatestValidGnssSuggestion() {
        com.android.server.timedetector.GnssTimeSuggestion gnssTimeSuggestion = this.mLastGnssSuggestion.get();
        if (gnssTimeSuggestion == null) {
            return null;
        }
        android.app.time.UnixEpochTime unixEpochTime = gnssTimeSuggestion.getUnixEpochTime();
        long elapsedRealTimeMillis = this.mEnvironment.elapsedRealtimeMillis();
        if (!validateSuggestionUnixEpochTime(elapsedRealTimeMillis, unixEpochTime)) {
            return null;
        }
        return gnssTimeSuggestion;
    }

    private android.app.time.ExternalTimeSuggestion findLatestValidExternalSuggestion() {
        android.app.time.ExternalTimeSuggestion externalTimeSuggestion = this.mLastExternalSuggestion.get();
        if (externalTimeSuggestion == null) {
            return null;
        }
        android.app.time.UnixEpochTime unixEpochTime = externalTimeSuggestion.getUnixEpochTime();
        long elapsedRealTimeMillis = this.mEnvironment.elapsedRealtimeMillis();
        if (!validateSuggestionUnixEpochTime(elapsedRealTimeMillis, unixEpochTime)) {
            return null;
        }
        return externalTimeSuggestion;
    }

    private boolean setSystemClockAndConfidenceIfRequired(int origin, android.app.time.UnixEpochTime time, java.lang.String cause) {
        boolean isOriginAutomatic = isOriginAutomatic(origin);
        if (isOriginAutomatic) {
            if (!this.mCurrentConfigurationInternal.getAutoDetectionEnabledBehavior()) {
                return false;
            }
        } else if (this.mCurrentConfigurationInternal.getAutoDetectionEnabledBehavior()) {
            return false;
        }
        this.mEnvironment.acquireWakeLock();
        try {
            return setSystemClockAndConfidenceUnderWakeLock(origin, time, 100, cause);
        } finally {
            this.mEnvironment.releaseWakeLock();
        }
    }

    private void upgradeSystemClockConfidenceIfRequired(android.app.time.UnixEpochTime autoDetectedUnixEpochTime, java.lang.String cause) {
        int currentTimeConfidence = this.mEnvironment.systemClockConfidence();
        boolean confidenceUpgradeRequired = currentTimeConfidence < 100;
        if (!confidenceUpgradeRequired) {
            return;
        }
        this.mEnvironment.acquireWakeLock();
        try {
            long currentElapsedRealtimeMillis = this.mEnvironment.elapsedRealtimeMillis();
            long currentSystemClockMillis = this.mEnvironment.systemClockMillis();
            boolean updateConfidenceRequired = isTimeWithinConfidenceThreshold(autoDetectedUnixEpochTime, currentElapsedRealtimeMillis, currentSystemClockMillis);
            if (updateConfidenceRequired) {
                java.lang.String logMsg = "Upgrade system clock confidence. autoDetectedUnixEpochTime=" + autoDetectedUnixEpochTime + " newTimeConfidence=100 cause=" + cause + " currentElapsedRealtimeMillis=" + currentElapsedRealtimeMillis + " currentSystemClockMillis=" + currentSystemClockMillis + " currentTimeConfidence=" + currentTimeConfidence;
                this.mEnvironment.setSystemClockConfidence(100, logMsg);
            }
        } finally {
            this.mEnvironment.releaseWakeLock();
        }
    }

    private static boolean isOriginAutomatic(int origin) {
        return origin != 2;
    }

    private boolean isTimeWithinConfidenceThreshold(android.app.time.UnixEpochTime timeToCheck, long currentElapsedRealtimeMillis, long currentSystemClockMillis) {
        long adjustedAutoDetectedUnixEpochMillis = timeToCheck.at(currentElapsedRealtimeMillis).getUnixEpochTimeMillis();
        long absTimeDifferenceMillis = java.lang.Math.abs(adjustedAutoDetectedUnixEpochMillis - currentSystemClockMillis);
        int confidenceUpgradeThresholdMillis = this.mCurrentConfigurationInternal.getSystemClockConfidenceThresholdMillis();
        return absTimeDifferenceMillis <= ((long) confidenceUpgradeThresholdMillis);
    }

    private boolean setSystemClockAndConfidenceUnderWakeLock(int origin, android.app.time.UnixEpochTime newTime, int newTimeConfidence, java.lang.String cause) {
        long elapsedRealtimeMillis = this.mEnvironment.elapsedRealtimeMillis();
        boolean isOriginAutomatic = isOriginAutomatic(origin);
        long actualSystemClockMillis = this.mEnvironment.systemClockMillis();
        if (isOriginAutomatic && this.mLastAutoSystemClockTimeSet != null) {
            long expectedTimeMillis = this.mLastAutoSystemClockTimeSet.at(elapsedRealtimeMillis).getUnixEpochTimeMillis();
            long absSystemClockDifference = java.lang.Math.abs(expectedTimeMillis - actualSystemClockMillis);
            if (absSystemClockDifference > SYSTEM_CLOCK_PARANOIA_THRESHOLD_MILLIS) {
                android.util.Slog.w(LOG_TAG, "System clock has not tracked elapsed real time clock. A clock may be inaccurate or something unexpectedly set the system clock. origin=" + com.android.server.timedetector.TimeDetectorStrategy.originToString(origin) + " elapsedRealtimeMillis=" + elapsedRealtimeMillis + " expectedTimeMillis=" + expectedTimeMillis + " actualTimeMillis=" + actualSystemClockMillis + " cause=" + cause);
            }
        }
        long newSystemClockMillis = newTime.at(elapsedRealtimeMillis).getUnixEpochTimeMillis();
        long absTimeDifference = java.lang.Math.abs(newSystemClockMillis - actualSystemClockMillis);
        boolean updateSystemClockRequired = absTimeDifference >= ((long) this.mCurrentConfigurationInternal.getSystemClockUpdateThresholdMillis());
        int currentTimeConfidence = this.mEnvironment.systemClockConfidence();
        boolean updateConfidenceRequired = newTimeConfidence != currentTimeConfidence;
        if (updateSystemClockRequired) {
            java.lang.String logMsg = "Set system clock & confidence. origin=" + com.android.server.timedetector.TimeDetectorStrategy.originToString(origin) + " newTime=" + newTime + " newTimeConfidence=" + newTimeConfidence + " cause=" + cause + " elapsedRealtimeMillis=" + elapsedRealtimeMillis + " (old) actualSystemClockMillis=" + actualSystemClockMillis + " newSystemClockMillis=" + newSystemClockMillis + " currentTimeConfidence=" + currentTimeConfidence;
            this.mEnvironment.setSystemClock(newSystemClockMillis, newTimeConfidence, logMsg);
            if (!isOriginAutomatic(origin)) {
                this.mLastAutoSystemClockTimeSet = null;
            } else {
                this.mLastAutoSystemClockTimeSet = newTime;
            }
        } else if (updateConfidenceRequired) {
            java.lang.String logMsg2 = "Set system clock confidence. origin=" + com.android.server.timedetector.TimeDetectorStrategy.originToString(origin) + " newTime=" + newTime + " newTimeConfidence=" + newTimeConfidence + " cause=" + cause + " elapsedRealtimeMillis=" + elapsedRealtimeMillis + " (old) actualSystemClockMillis=" + actualSystemClockMillis + " newSystemClockMillis=" + newSystemClockMillis + " currentTimeConfidence=" + currentTimeConfidence;
            this.mEnvironment.setSystemClockConfidence(newTimeConfidence, logMsg2);
        }
        return true;
    }

    public synchronized android.app.timedetector.TelephonyTimeSuggestion findBestTelephonySuggestionForTests() {
        return findBestTelephonySuggestion();
    }

    public synchronized com.android.server.timedetector.NetworkTimeSuggestion findLatestValidNetworkSuggestionForTests() {
        return findLatestValidNetworkSuggestion();
    }

    public synchronized com.android.server.timedetector.GnssTimeSuggestion findLatestValidGnssSuggestionForTests() {
        return findLatestValidGnssSuggestion();
    }

    public synchronized android.app.time.ExternalTimeSuggestion findLatestValidExternalSuggestionForTests() {
        return findLatestValidExternalSuggestion();
    }

    public synchronized android.app.timedetector.TelephonyTimeSuggestion getLatestTelephonySuggestion(int slotIndex) {
        return this.mSuggestionBySlotIndex.get(java.lang.Integer.valueOf(slotIndex));
    }

    public synchronized com.android.server.timedetector.GnssTimeSuggestion getLatestGnssSuggestion() {
        return this.mLastGnssSuggestion.get();
    }

    public synchronized android.app.time.ExternalTimeSuggestion getLatestExternalSuggestion() {
        return this.mLastExternalSuggestion.get();
    }

    private static boolean validateSuggestionUnixEpochTime(long currentElapsedRealtimeMillis, android.app.time.UnixEpochTime unixEpochTime) {
        long suggestionElapsedRealtimeMillis = unixEpochTime.getElapsedRealtimeMillis();
        if (suggestionElapsedRealtimeMillis > currentElapsedRealtimeMillis) {
            return false;
        }
        long ageMillis = currentElapsedRealtimeMillis - suggestionElapsedRealtimeMillis;
        return ageMillis <= 86400000;
    }
}
