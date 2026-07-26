package com.android.server.timedetector;

/* JADX INFO: loaded from: classes3.dex */
public class TimeDetectorInternalImpl implements com.android.server.timedetector.TimeDetectorInternal {
    private final android.content.Context mContext;
    private final com.android.server.timezonedetector.CurrentUserIdentityInjector mCurrentUserIdentityInjector;
    private final android.os.Handler mHandler;
    private final com.android.server.timedetector.ServiceConfigAccessor mServiceConfigAccessor;
    private final com.android.server.timedetector.TimeDetectorStrategy mTimeDetectorStrategy;

    public TimeDetectorInternalImpl(android.content.Context context, android.os.Handler handler, com.android.server.timezonedetector.CurrentUserIdentityInjector currentUserIdentityInjector, com.android.server.timedetector.ServiceConfigAccessor serviceConfigAccessor, com.android.server.timedetector.TimeDetectorStrategy timeDetectorStrategy) {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mHandler = (android.os.Handler) java.util.Objects.requireNonNull(handler);
        this.mCurrentUserIdentityInjector = (com.android.server.timezonedetector.CurrentUserIdentityInjector) java.util.Objects.requireNonNull(currentUserIdentityInjector);
        this.mServiceConfigAccessor = (com.android.server.timedetector.ServiceConfigAccessor) java.util.Objects.requireNonNull(serviceConfigAccessor);
        this.mTimeDetectorStrategy = (com.android.server.timedetector.TimeDetectorStrategy) java.util.Objects.requireNonNull(timeDetectorStrategy);
    }

    @Override // com.android.server.timedetector.TimeDetectorInternal
    public android.app.time.TimeCapabilitiesAndConfig getCapabilitiesAndConfigForDpm() {
        int currentUserId = this.mCurrentUserIdentityInjector.getCurrentUserId();
        com.android.server.timedetector.ConfigurationInternal configurationInternal = this.mServiceConfigAccessor.getConfigurationInternal(currentUserId);
        return configurationInternal.createCapabilitiesAndConfig(true);
    }

    @Override // com.android.server.timedetector.TimeDetectorInternal
    public boolean updateConfigurationForDpm(android.app.time.TimeConfiguration configuration) {
        java.util.Objects.requireNonNull(configuration);
        int currentUserId = this.mCurrentUserIdentityInjector.getCurrentUserId();
        return this.mServiceConfigAccessor.updateConfiguration(currentUserId, configuration, true);
    }

    @Override // com.android.server.timedetector.TimeDetectorInternal
    public boolean setManualTimeForDpm(android.app.timedetector.ManualTimeSuggestion suggestion) {
        java.util.Objects.requireNonNull(suggestion);
        int userId = this.mCurrentUserIdentityInjector.getCurrentUserId();
        return this.mTimeDetectorStrategy.suggestManualTime(userId, suggestion, false);
    }

    @Override // com.android.server.timedetector.TimeDetectorInternal
    public void suggestNetworkTime(final com.android.server.timedetector.NetworkTimeSuggestion suggestion) {
        java.util.Objects.requireNonNull(suggestion);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.timedetector.TimeDetectorInternalImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$suggestNetworkTime$0(suggestion);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$suggestNetworkTime$0(com.android.server.timedetector.NetworkTimeSuggestion suggestion) {
        this.mTimeDetectorStrategy.suggestNetworkTime(suggestion);
    }

    @Override // com.android.server.timedetector.TimeDetectorInternal
    public void addNetworkTimeUpdateListener(com.android.server.timezonedetector.StateChangeListener networkTimeUpdateListener) {
        java.util.Objects.requireNonNull(networkTimeUpdateListener);
        this.mTimeDetectorStrategy.addNetworkTimeUpdateListener(networkTimeUpdateListener);
    }

    @Override // com.android.server.timedetector.TimeDetectorInternal
    public com.android.server.timedetector.NetworkTimeSuggestion getLatestNetworkSuggestion() {
        return this.mTimeDetectorStrategy.getLatestNetworkSuggestion();
    }

    @Override // com.android.server.timedetector.TimeDetectorInternal
    public void suggestGnssTime(final com.android.server.timedetector.GnssTimeSuggestion suggestion) {
        java.util.Objects.requireNonNull(suggestion);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.timedetector.TimeDetectorInternalImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$suggestGnssTime$1(suggestion);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$suggestGnssTime$1(com.android.server.timedetector.GnssTimeSuggestion suggestion) {
        this.mTimeDetectorStrategy.suggestGnssTime(suggestion);
    }
}
