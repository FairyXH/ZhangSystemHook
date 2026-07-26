package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
public final class TimeZoneDetectorStrategyImpl implements com.android.server.timezonedetector.TimeZoneDetectorStrategy {
    private static final boolean DBG = false;
    private static final int KEEP_SUGGESTION_HISTORY_SIZE = 10;
    private static final java.lang.String LOG_TAG = "time_zone_detector";
    public static final int TELEPHONY_SCORE_HIGH = 3;
    public static final int TELEPHONY_SCORE_HIGHEST = 4;
    public static final int TELEPHONY_SCORE_LOW = 1;
    public static final int TELEPHONY_SCORE_MEDIUM = 2;
    public static final int TELEPHONY_SCORE_NONE = 0;
    public static final int TELEPHONY_SCORE_USAGE_THRESHOLD = 2;
    private com.android.server.timezonedetector.ConfigurationInternal mCurrentConfigurationInternal;
    private android.app.time.TimeZoneDetectorStatus mDetectorStatus;
    private final com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment mEnvironment;
    private final com.android.server.timezonedetector.ServiceConfigAccessor mServiceConfigAccessor;
    private android.os.TimestampedValue<java.lang.Boolean> mTelephonyTimeZoneFallbackEnabled;
    private final com.android.server.timezonedetector.ArrayMapWithHistory<java.lang.Integer, com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.QualifiedTelephonyTimeZoneSuggestion> mTelephonySuggestionsBySlotIndex = new com.android.server.timezonedetector.ArrayMapWithHistory<>(10);
    private final com.android.server.timezonedetector.ReferenceWithHistory<com.android.server.timezonedetector.LocationAlgorithmEvent> mLatestLocationAlgorithmEvent = new com.android.server.timezonedetector.ReferenceWithHistory<>(10);
    private final com.android.server.timezonedetector.ReferenceWithHistory<android.app.timezonedetector.ManualTimeZoneSuggestion> mLatestManualSuggestion = new com.android.server.timezonedetector.ReferenceWithHistory<>(10);
    private final java.util.List<com.android.server.timezonedetector.StateChangeListener> mStateChangeListeners = new java.util.ArrayList();

    public interface Environment {
        void addDebugLogEntry(java.lang.String str);

        void dumpDebugLog(java.io.PrintWriter printWriter);

        long elapsedRealtimeMillis();

        java.lang.String getDeviceTimeZone();

        int getDeviceTimeZoneConfidence();

        void runAsync(java.lang.Runnable runnable);

        void setDeviceTimeZoneAndConfidence(java.lang.String str, int i, java.lang.String str2);
    }

    public static com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl create(android.os.Handler handler, com.android.server.timezonedetector.ServiceConfigAccessor serviceConfigAccessor) {
        com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment environment = new com.android.server.timezonedetector.EnvironmentImpl(handler);
        return new com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl(serviceConfigAccessor, environment);
    }

    public TimeZoneDetectorStrategyImpl(com.android.server.timezonedetector.ServiceConfigAccessor serviceConfigAccessor, com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment environment) {
        this.mEnvironment = (com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment) java.util.Objects.requireNonNull(environment);
        this.mServiceConfigAccessor = (com.android.server.timezonedetector.ServiceConfigAccessor) java.util.Objects.requireNonNull(serviceConfigAccessor);
        this.mTelephonyTimeZoneFallbackEnabled = new android.os.TimestampedValue<>(this.mEnvironment.elapsedRealtimeMillis(), true);
        synchronized (this) {
            com.android.server.timezonedetector.StateChangeListener stateChangeListener = new com.android.server.timezonedetector.StateChangeListener() { // from class: com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl$$ExternalSyntheticLambda0
                @Override // com.android.server.timezonedetector.StateChangeListener
                public final void onChange() {
                    this.f$0.handleConfigurationInternalMaybeChanged();
                }
            };
            this.mServiceConfigAccessor.addConfigurationInternalChangeListener(stateChangeListener);
            updateCurrentConfigurationInternalIfRequired("TimeZoneDetectorStrategyImpl:");
        }
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategy
    public synchronized android.app.time.TimeZoneCapabilitiesAndConfig getCapabilitiesAndConfig(int userId, boolean bypassUserPolicyChecks) {
        com.android.server.timezonedetector.ConfigurationInternal configurationInternal;
        if (this.mCurrentConfigurationInternal.getUserId() == userId) {
            configurationInternal = this.mCurrentConfigurationInternal;
        } else {
            configurationInternal = this.mServiceConfigAccessor.getConfigurationInternal(userId);
        }
        return new android.app.time.TimeZoneCapabilitiesAndConfig(this.mDetectorStatus, configurationInternal.asCapabilities(bypassUserPolicyChecks), configurationInternal.asConfiguration());
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategy
    public synchronized boolean updateConfiguration(int userId, android.app.time.TimeZoneConfiguration configuration, boolean bypassUserPolicyChecks) {
        boolean updateSuccessful;
        updateSuccessful = this.mServiceConfigAccessor.updateConfiguration(userId, configuration, bypassUserPolicyChecks);
        if (updateSuccessful) {
            java.lang.String logMsg = "updateConfiguration: userId=" + userId + ", configuration=" + configuration + ", bypassUserPolicyChecks=" + bypassUserPolicyChecks;
            updateCurrentConfigurationInternalIfRequired(logMsg);
        }
        return updateSuccessful;
    }

    private void updateCurrentConfigurationInternalIfRequired(java.lang.String logMsg) {
        com.android.server.timezonedetector.ConfigurationInternal newCurrentConfigurationInternal = this.mServiceConfigAccessor.getCurrentUserConfigurationInternal();
        com.android.server.timezonedetector.ConfigurationInternal oldCurrentConfigurationInternal = this.mCurrentConfigurationInternal;
        if (!newCurrentConfigurationInternal.equals(oldCurrentConfigurationInternal)) {
            this.mCurrentConfigurationInternal = newCurrentConfigurationInternal;
            java.lang.String logMsg2 = logMsg + " [oldConfiguration=" + oldCurrentConfigurationInternal + ", newConfiguration=" + newCurrentConfigurationInternal + "]";
            logTimeZoneDebugInfo(logMsg2);
            updateDetectorStatus();
            notifyStateChangeListenersAsynchronously();
            doAutoTimeZoneDetection(this.mCurrentConfigurationInternal, logMsg2);
        }
    }

    private void notifyStateChangeListenersAsynchronously() {
        for (com.android.server.timezonedetector.StateChangeListener listener : this.mStateChangeListeners) {
            com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment environment = this.mEnvironment;
            java.util.Objects.requireNonNull(listener);
            environment.runAsync(new com.android.server.timedetector.TimeDetectorStrategyImpl$$ExternalSyntheticLambda0(listener));
        }
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategy
    public synchronized void addChangeListener(com.android.server.timezonedetector.StateChangeListener listener) {
        this.mStateChangeListeners.add(listener);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategy
    public synchronized boolean confirmTimeZone(java.lang.String timeZoneId) {
        java.util.Objects.requireNonNull(timeZoneId);
        java.lang.String currentTimeZoneId = this.mEnvironment.getDeviceTimeZone();
        if (!currentTimeZoneId.equals(timeZoneId)) {
            return false;
        }
        if (this.mEnvironment.getDeviceTimeZoneConfidence() < 100) {
            this.mEnvironment.setDeviceTimeZoneAndConfidence(currentTimeZoneId, 100, "confirmTimeZone: timeZoneId=" + timeZoneId);
        }
        return true;
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategy
    public synchronized android.app.time.TimeZoneState getTimeZoneState() {
        boolean userShouldConfirmId;
        userShouldConfirmId = this.mEnvironment.getDeviceTimeZoneConfidence() < 100;
        return new android.app.time.TimeZoneState(this.mEnvironment.getDeviceTimeZone(), userShouldConfirmId);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategy
    public void setTimeZoneState(android.app.time.TimeZoneState timeZoneState) {
        java.util.Objects.requireNonNull(timeZoneState);
        int confidence = timeZoneState.getUserShouldConfirmId() ? 0 : 100;
        this.mEnvironment.setDeviceTimeZoneAndConfidence(timeZoneState.getId(), confidence, "setTimeZoneState()");
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategy
    public synchronized void handleLocationAlgorithmEvent(com.android.server.timezonedetector.LocationAlgorithmEvent event) {
        com.android.server.timezonedetector.ConfigurationInternal currentUserConfig = this.mCurrentConfigurationInternal;
        java.util.Objects.requireNonNull(event);
        this.mLatestLocationAlgorithmEvent.set(event);
        boolean statusChanged = updateDetectorStatus();
        if (statusChanged) {
            notifyStateChangeListenersAsynchronously();
        }
        if (event.getAlgorithmStatus().couldEnableTelephonyFallback()) {
            enableTelephonyTimeZoneFallback("handleLocationAlgorithmEvent(), event=" + event);
        } else {
            disableTelephonyFallbackIfNeeded();
        }
        java.lang.String reason = "New location algorithm event received. event=" + event;
        doAutoTimeZoneDetection(currentUserConfig, reason);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategy
    public synchronized boolean suggestManualTimeZone(int userId, android.app.timezonedetector.ManualTimeZoneSuggestion suggestion, boolean bypassUserPolicyChecks) {
        com.android.server.timezonedetector.ConfigurationInternal currentUserConfig = this.mCurrentConfigurationInternal;
        if (currentUserConfig.getUserId() != userId) {
            android.util.Slog.w(LOG_TAG, "Manual suggestion received but user != current user, userId=" + userId + " suggestion=" + suggestion);
            return false;
        }
        java.util.Objects.requireNonNull(suggestion);
        java.lang.String timeZoneId = suggestion.getZoneId();
        java.lang.String cause = "Manual time suggestion received: suggestion=" + suggestion;
        android.app.time.TimeZoneCapabilities capabilities = currentUserConfig.asCapabilities(bypassUserPolicyChecks);
        if (capabilities.getSetManualTimeZoneCapability() != 40) {
            android.util.Slog.i(LOG_TAG, "User does not have the capability needed to set the time zone manually: capabilities=" + capabilities + ", timeZoneId=" + timeZoneId + ", cause=" + cause);
            return false;
        }
        this.mLatestManualSuggestion.set(suggestion);
        setDeviceTimeZoneIfRequired(timeZoneId, cause);
        return true;
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategy
    public synchronized void suggestTelephonyTimeZone(android.app.timezonedetector.TelephonyTimeZoneSuggestion suggestion) {
        com.android.server.timezonedetector.ConfigurationInternal currentUserConfig = this.mCurrentConfigurationInternal;
        java.util.Objects.requireNonNull(suggestion);
        int score = scoreTelephonySuggestion(suggestion);
        com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.QualifiedTelephonyTimeZoneSuggestion scoredSuggestion = new com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.QualifiedTelephonyTimeZoneSuggestion(suggestion, score);
        this.mTelephonySuggestionsBySlotIndex.put(java.lang.Integer.valueOf(suggestion.getSlotIndex()), scoredSuggestion);
        java.lang.String reason = "New telephony time zone suggested. suggestion=" + suggestion;
        doAutoTimeZoneDetection(currentUserConfig, reason);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategy
    public synchronized void enableTelephonyTimeZoneFallback(java.lang.String reason) {
        if (!((java.lang.Boolean) this.mTelephonyTimeZoneFallbackEnabled.getValue()).booleanValue()) {
            com.android.server.timezonedetector.ConfigurationInternal currentUserConfig = this.mCurrentConfigurationInternal;
            this.mTelephonyTimeZoneFallbackEnabled = new android.os.TimestampedValue<>(this.mEnvironment.elapsedRealtimeMillis(), true);
            java.lang.String logMsg = "enableTelephonyTimeZoneFallback:  reason=" + reason + ", currentUserConfig=" + currentUserConfig + ", mTelephonyTimeZoneFallbackEnabled=" + this.mTelephonyTimeZoneFallbackEnabled;
            logTimeZoneDebugInfo(logMsg);
            disableTelephonyFallbackIfNeeded();
            if (currentUserConfig.isTelephonyFallbackSupported()) {
                doAutoTimeZoneDetection(currentUserConfig, reason);
            }
        }
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategy
    public synchronized com.android.server.timezonedetector.MetricsTimeZoneDetectorState generateMetricsState() {
        android.app.timezonedetector.TelephonyTimeZoneSuggestion telephonySuggestion;
        com.android.server.timezonedetector.OrdinalGenerator<java.lang.String> tzIdOrdinalGenerator;
        com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.QualifiedTelephonyTimeZoneSuggestion bestQualifiedTelephonySuggestion = findBestTelephonySuggestion();
        telephonySuggestion = bestQualifiedTelephonySuggestion == null ? null : bestQualifiedTelephonySuggestion.suggestion;
        tzIdOrdinalGenerator = new com.android.server.timezonedetector.OrdinalGenerator<>(new com.android.server.timezonedetector.TimeZoneCanonicalizer());
        return com.android.server.timezonedetector.MetricsTimeZoneDetectorState.create(tzIdOrdinalGenerator, this.mCurrentConfigurationInternal, this.mEnvironment.getDeviceTimeZone(), getLatestManualSuggestion(), telephonySuggestion, getLatestLocationAlgorithmEvent());
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategy
    public boolean isTelephonyTimeZoneDetectionSupported() {
        boolean zIsTelephonyDetectionSupported;
        synchronized (this) {
            zIsTelephonyDetectionSupported = this.mCurrentConfigurationInternal.isTelephonyDetectionSupported();
        }
        return zIsTelephonyDetectionSupported;
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategy
    public boolean isGeoTimeZoneDetectionSupported() {
        boolean zIsGeoDetectionSupported;
        synchronized (this) {
            zIsGeoDetectionSupported = this.mCurrentConfigurationInternal.isGeoDetectionSupported();
        }
        return zIsGeoDetectionSupported;
    }

    private static int scoreTelephonySuggestion(android.app.timezonedetector.TelephonyTimeZoneSuggestion suggestion) {
        if (suggestion.getZoneId() == null) {
            return 0;
        }
        int score = suggestion.getMatchType();
        if (score == 5 || suggestion.getMatchType() == 4) {
            return 4;
        }
        if (suggestion.getQuality() == 1) {
            return 3;
        }
        int score2 = suggestion.getQuality();
        if (score2 == 2) {
            return 2;
        }
        int score3 = suggestion.getQuality();
        if (score3 == 3) {
            return 1;
        }
        throw new java.lang.AssertionError();
    }

    private void doAutoTimeZoneDetection(com.android.server.timezonedetector.ConfigurationInternal currentUserConfig, java.lang.String detectionReason) {
        int detectionMode = currentUserConfig.getDetectionMode();
        switch (detectionMode) {
            case 0:
                android.util.Slog.i(LOG_TAG, "Unknown detection mode: " + detectionMode + ", is location off?");
                break;
            case 1:
                break;
            case 2:
                boolean isGeoDetectionCertain = doGeolocationTimeZoneDetection(detectionReason);
                if (!isGeoDetectionCertain && ((java.lang.Boolean) this.mTelephonyTimeZoneFallbackEnabled.getValue()).booleanValue() && currentUserConfig.isTelephonyFallbackSupported()) {
                    doTelephonyTimeZoneDetection(detectionReason + ", telephony fallback mode");
                    break;
                }
                break;
            case 3:
                doTelephonyTimeZoneDetection(detectionReason);
                break;
            default:
                android.util.Slog.wtf(LOG_TAG, "Unknown detection mode: " + detectionMode);
                break;
        }
    }

    private boolean doGeolocationTimeZoneDetection(java.lang.String detectionReason) {
        java.lang.String zoneId;
        com.android.server.timezonedetector.LocationAlgorithmEvent latestLocationAlgorithmEvent = this.mLatestLocationAlgorithmEvent.get();
        if (latestLocationAlgorithmEvent == null || latestLocationAlgorithmEvent.getSuggestion() == null) {
            return false;
        }
        com.android.server.timezonedetector.GeolocationTimeZoneSuggestion suggestion = latestLocationAlgorithmEvent.getSuggestion();
        java.util.List<java.lang.String> zoneIds = suggestion.getZoneIds();
        if (zoneIds == null) {
            return false;
        }
        if (zoneIds.isEmpty()) {
            return true;
        }
        java.lang.String deviceTimeZone = this.mEnvironment.getDeviceTimeZone();
        if (zoneIds.contains(deviceTimeZone)) {
            zoneId = deviceTimeZone;
        } else {
            java.lang.String zoneId2 = zoneIds.get(0);
            zoneId = zoneId2;
        }
        setDeviceTimeZoneIfRequired(zoneId, detectionReason);
        return true;
    }

    private void disableTelephonyFallbackIfNeeded() {
        com.android.server.timezonedetector.LocationAlgorithmEvent latestLocationAlgorithmEvent = this.mLatestLocationAlgorithmEvent.get();
        if (latestLocationAlgorithmEvent == null) {
            return;
        }
        com.android.server.timezonedetector.GeolocationTimeZoneSuggestion suggestion = latestLocationAlgorithmEvent.getSuggestion();
        boolean isLatestSuggestionCertain = (suggestion == null || suggestion.getZoneIds() == null) ? false : true;
        if (isLatestSuggestionCertain && ((java.lang.Boolean) this.mTelephonyTimeZoneFallbackEnabled.getValue()).booleanValue()) {
            boolean latestSuggestionIsNewerThanFallbackEnabled = suggestion.getEffectiveFromElapsedMillis() > this.mTelephonyTimeZoneFallbackEnabled.getReferenceTimeMillis();
            if (latestSuggestionIsNewerThanFallbackEnabled) {
                this.mTelephonyTimeZoneFallbackEnabled = new android.os.TimestampedValue<>(this.mEnvironment.elapsedRealtimeMillis(), false);
                java.lang.String logMsg = "disableTelephonyFallbackIfNeeded: mTelephonyTimeZoneFallbackEnabled=" + this.mTelephonyTimeZoneFallbackEnabled;
                logTimeZoneDebugInfo(logMsg);
            }
        }
    }

    private void logTimeZoneDebugInfo(java.lang.String logMsg) {
        this.mEnvironment.addDebugLogEntry(logMsg);
    }

    private void doTelephonyTimeZoneDetection(java.lang.String detectionReason) {
        com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.QualifiedTelephonyTimeZoneSuggestion bestTelephonySuggestion = findBestTelephonySuggestion();
        if (bestTelephonySuggestion == null) {
            return;
        }
        boolean suggestionGoodEnough = bestTelephonySuggestion.score >= 2;
        if (!suggestionGoodEnough) {
            return;
        }
        java.lang.String zoneId = bestTelephonySuggestion.suggestion.getZoneId();
        if (zoneId == null) {
            android.util.Slog.w(LOG_TAG, "Empty zone suggestion scored higher than expected. This is an error: bestTelephonySuggestion=" + bestTelephonySuggestion + ", detectionReason=" + detectionReason);
        } else {
            java.lang.String cause = "Found good suggestion: bestTelephonySuggestion=" + bestTelephonySuggestion + ", detectionReason=" + detectionReason;
            setDeviceTimeZoneIfRequired(zoneId, cause);
        }
    }

    private void setDeviceTimeZoneIfRequired(java.lang.String newZoneId, java.lang.String cause) {
        java.lang.String currentZoneId = this.mEnvironment.getDeviceTimeZone();
        int currentConfidence = this.mEnvironment.getDeviceTimeZoneConfidence();
        if (!newZoneId.equals(currentZoneId) || 100 > currentConfidence) {
            java.lang.String logInfo = "Set device time zone or higher confidence: newZoneId=" + newZoneId + ", cause=" + cause + ", newConfidence=100";
            this.mEnvironment.setDeviceTimeZoneAndConfidence(newZoneId, 100, logInfo);
        }
    }

    private com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.QualifiedTelephonyTimeZoneSuggestion findBestTelephonySuggestion() {
        com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.QualifiedTelephonyTimeZoneSuggestion bestSuggestion = null;
        for (int i = 0; i < this.mTelephonySuggestionsBySlotIndex.size(); i++) {
            com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.QualifiedTelephonyTimeZoneSuggestion candidateSuggestion = this.mTelephonySuggestionsBySlotIndex.valueAt(i);
            if (candidateSuggestion != null) {
                if (bestSuggestion == null) {
                    bestSuggestion = candidateSuggestion;
                } else if (candidateSuggestion.score > bestSuggestion.score) {
                    bestSuggestion = candidateSuggestion;
                } else if (candidateSuggestion.score == bestSuggestion.score) {
                    int candidateSlotIndex = candidateSuggestion.suggestion.getSlotIndex();
                    int bestSlotIndex = bestSuggestion.suggestion.getSlotIndex();
                    if (candidateSlotIndex < bestSlotIndex) {
                        bestSuggestion = candidateSuggestion;
                    }
                }
            }
        }
        return bestSuggestion;
    }

    public synchronized com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.QualifiedTelephonyTimeZoneSuggestion findBestTelephonySuggestionForTests() {
        return findBestTelephonySuggestion();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleConfigurationInternalMaybeChanged() {
        updateCurrentConfigurationInternalIfRequired("handleConfigurationInternalMaybeChanged:");
    }

    private boolean updateDetectorStatus() {
        android.app.time.TimeZoneDetectorStatus newDetectorStatus = createTimeZoneDetectorStatus(this.mCurrentConfigurationInternal, this.mLatestLocationAlgorithmEvent.get());
        android.app.time.TimeZoneDetectorStatus oldDetectorStatus = this.mDetectorStatus;
        boolean statusChanged = !newDetectorStatus.equals(oldDetectorStatus);
        if (statusChanged) {
            this.mDetectorStatus = newDetectorStatus;
        }
        return statusChanged;
    }

    @Override // com.android.server.timezonedetector.Dumpable
    public synchronized void dump(android.util.IndentingPrintWriter ipw, java.lang.String[] args) {
        ipw.println("TimeZoneDetectorStrategy:");
        ipw.increaseIndent();
        ipw.println("mCurrentConfigurationInternal=" + this.mCurrentConfigurationInternal);
        ipw.println("mDetectorStatus=" + this.mDetectorStatus);
        ipw.println("[Capabilities=" + this.mCurrentConfigurationInternal.asCapabilities(false) + "]");
        ipw.println("mEnvironment.getDeviceTimeZone()=" + this.mEnvironment.getDeviceTimeZone());
        ipw.println("mEnvironment.getDeviceTimeZoneConfidence()=" + this.mEnvironment.getDeviceTimeZoneConfidence());
        ipw.println("Misc state:");
        ipw.increaseIndent();
        ipw.println("mTelephonyTimeZoneFallbackEnabled=" + formatDebugString(this.mTelephonyTimeZoneFallbackEnabled));
        ipw.decreaseIndent();
        ipw.println("Time zone debug log:");
        ipw.increaseIndent();
        this.mEnvironment.dumpDebugLog(ipw);
        ipw.decreaseIndent();
        ipw.println("Manual suggestion history:");
        ipw.increaseIndent();
        this.mLatestManualSuggestion.dump(ipw);
        ipw.decreaseIndent();
        ipw.println("Location algorithm event history:");
        ipw.increaseIndent();
        this.mLatestLocationAlgorithmEvent.dump(ipw);
        ipw.decreaseIndent();
        ipw.println("Telephony suggestion history:");
        ipw.increaseIndent();
        this.mTelephonySuggestionsBySlotIndex.dump(ipw);
        ipw.decreaseIndent();
        ipw.decreaseIndent();
    }

    public synchronized android.app.timezonedetector.ManualTimeZoneSuggestion getLatestManualSuggestion() {
        return this.mLatestManualSuggestion.get();
    }

    public synchronized com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.QualifiedTelephonyTimeZoneSuggestion getLatestTelephonySuggestion(int slotIndex) {
        return this.mTelephonySuggestionsBySlotIndex.get(java.lang.Integer.valueOf(slotIndex));
    }

    public synchronized com.android.server.timezonedetector.LocationAlgorithmEvent getLatestLocationAlgorithmEvent() {
        return this.mLatestLocationAlgorithmEvent.get();
    }

    public synchronized boolean isTelephonyFallbackEnabledForTests() {
        return ((java.lang.Boolean) this.mTelephonyTimeZoneFallbackEnabled.getValue()).booleanValue();
    }

    public synchronized com.android.server.timezonedetector.ConfigurationInternal getCachedCapabilitiesAndConfigForTests() {
        return this.mCurrentConfigurationInternal;
    }

    public synchronized android.app.time.TimeZoneDetectorStatus getCachedDetectorStatusForTests() {
        return this.mDetectorStatus;
    }

    public static final class QualifiedTelephonyTimeZoneSuggestion {
        public final int score;
        public final android.app.timezonedetector.TelephonyTimeZoneSuggestion suggestion;

        public QualifiedTelephonyTimeZoneSuggestion(android.app.timezonedetector.TelephonyTimeZoneSuggestion suggestion, int score) {
            this.suggestion = suggestion;
            this.score = score;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.QualifiedTelephonyTimeZoneSuggestion that = (com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.QualifiedTelephonyTimeZoneSuggestion) o;
            if (this.score == that.score && this.suggestion.equals(that.suggestion)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.score), this.suggestion);
        }

        public java.lang.String toString() {
            return "QualifiedTelephonyTimeZoneSuggestion{suggestion=" + this.suggestion + ", score=" + this.score + '}';
        }
    }

    private static java.lang.String formatDebugString(android.os.TimestampedValue<?> value) {
        return value.getValue() + " @ " + java.time.Duration.ofMillis(value.getReferenceTimeMillis());
    }

    private static android.app.time.TimeZoneDetectorStatus createTimeZoneDetectorStatus(com.android.server.timezonedetector.ConfigurationInternal currentConfigurationInternal, com.android.server.timezonedetector.LocationAlgorithmEvent latestLocationAlgorithmEvent) {
        int detectorStatus;
        if (!currentConfigurationInternal.isAutoDetectionSupported()) {
            detectorStatus = 1;
        } else if (currentConfigurationInternal.getAutoDetectionEnabledBehavior()) {
            detectorStatus = 3;
        } else {
            detectorStatus = 2;
        }
        android.app.time.TelephonyTimeZoneAlgorithmStatus telephonyAlgorithmStatus = createTelephonyAlgorithmStatus(currentConfigurationInternal);
        android.app.time.LocationTimeZoneAlgorithmStatus locationAlgorithmStatus = createLocationAlgorithmStatus(currentConfigurationInternal, latestLocationAlgorithmEvent);
        return new android.app.time.TimeZoneDetectorStatus(detectorStatus, telephonyAlgorithmStatus, locationAlgorithmStatus);
    }

    private static android.app.time.LocationTimeZoneAlgorithmStatus createLocationAlgorithmStatus(com.android.server.timezonedetector.ConfigurationInternal currentConfigurationInternal, com.android.server.timezonedetector.LocationAlgorithmEvent latestLocationAlgorithmEvent) {
        if (latestLocationAlgorithmEvent != null) {
            android.app.time.LocationTimeZoneAlgorithmStatus locationAlgorithmStatus = latestLocationAlgorithmEvent.getAlgorithmStatus();
            return locationAlgorithmStatus;
        }
        if (!currentConfigurationInternal.isGeoDetectionSupported()) {
            android.app.time.LocationTimeZoneAlgorithmStatus locationAlgorithmStatus2 = android.app.time.LocationTimeZoneAlgorithmStatus.NOT_SUPPORTED;
            return locationAlgorithmStatus2;
        }
        if (currentConfigurationInternal.isGeoDetectionExecutionEnabled()) {
            android.app.time.LocationTimeZoneAlgorithmStatus locationAlgorithmStatus3 = android.app.time.LocationTimeZoneAlgorithmStatus.RUNNING_NOT_REPORTED;
            return locationAlgorithmStatus3;
        }
        android.app.time.LocationTimeZoneAlgorithmStatus locationAlgorithmStatus4 = android.app.time.LocationTimeZoneAlgorithmStatus.NOT_RUNNING;
        return locationAlgorithmStatus4;
    }

    private static android.app.time.TelephonyTimeZoneAlgorithmStatus createTelephonyAlgorithmStatus(com.android.server.timezonedetector.ConfigurationInternal currentConfigurationInternal) {
        int algorithmStatus;
        if (!currentConfigurationInternal.isTelephonyDetectionSupported()) {
            algorithmStatus = 1;
        } else {
            algorithmStatus = 3;
        }
        return new android.app.time.TelephonyTimeZoneAlgorithmStatus(algorithmStatus);
    }
}
