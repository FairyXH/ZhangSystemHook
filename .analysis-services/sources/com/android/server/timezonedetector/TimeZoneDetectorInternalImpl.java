package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
public final class TimeZoneDetectorInternalImpl implements com.android.server.timezonedetector.TimeZoneDetectorInternal {
    private final android.content.Context mContext;
    private final com.android.server.timezonedetector.CurrentUserIdentityInjector mCurrentUserIdentityInjector;
    private final android.os.Handler mHandler;
    private final com.android.server.timezonedetector.TimeZoneDetectorStrategy mTimeZoneDetectorStrategy;

    public TimeZoneDetectorInternalImpl(android.content.Context context, android.os.Handler handler, com.android.server.timezonedetector.CurrentUserIdentityInjector currentUserIdentityInjector, com.android.server.timezonedetector.TimeZoneDetectorStrategy timeZoneDetectorStrategy) {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mHandler = (android.os.Handler) java.util.Objects.requireNonNull(handler);
        this.mCurrentUserIdentityInjector = (com.android.server.timezonedetector.CurrentUserIdentityInjector) java.util.Objects.requireNonNull(currentUserIdentityInjector);
        this.mTimeZoneDetectorStrategy = (com.android.server.timezonedetector.TimeZoneDetectorStrategy) java.util.Objects.requireNonNull(timeZoneDetectorStrategy);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorInternal
    public android.app.time.TimeZoneCapabilitiesAndConfig getCapabilitiesAndConfigForDpm() {
        int currentUserId = this.mCurrentUserIdentityInjector.getCurrentUserId();
        return this.mTimeZoneDetectorStrategy.getCapabilitiesAndConfig(currentUserId, true);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorInternal
    public boolean updateConfigurationForDpm(android.app.time.TimeZoneConfiguration configuration) {
        java.util.Objects.requireNonNull(configuration);
        int currentUserId = this.mCurrentUserIdentityInjector.getCurrentUserId();
        return this.mTimeZoneDetectorStrategy.updateConfiguration(currentUserId, configuration, true);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorInternal
    public boolean setManualTimeZoneForDpm(android.app.timezonedetector.ManualTimeZoneSuggestion suggestion) {
        java.util.Objects.requireNonNull(suggestion);
        int currentUserId = this.mCurrentUserIdentityInjector.getCurrentUserId();
        return this.mTimeZoneDetectorStrategy.suggestManualTimeZone(currentUserId, suggestion, true);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorInternal
    public void handleLocationAlgorithmEvent(final com.android.server.timezonedetector.LocationAlgorithmEvent locationAlgorithmEvent) {
        java.util.Objects.requireNonNull(locationAlgorithmEvent);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.timezonedetector.TimeZoneDetectorInternalImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$handleLocationAlgorithmEvent$0(locationAlgorithmEvent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleLocationAlgorithmEvent$0(com.android.server.timezonedetector.LocationAlgorithmEvent locationAlgorithmEvent) {
        this.mTimeZoneDetectorStrategy.handleLocationAlgorithmEvent(locationAlgorithmEvent);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorInternal
    public com.android.server.timezonedetector.MetricsTimeZoneDetectorState generateMetricsState() {
        return this.mTimeZoneDetectorStrategy.generateMetricsState();
    }
}
