package com.android.server.timezonedetector.location;

/* JADX INFO: loaded from: classes3.dex */
class LocationTimeZoneProviderControllerEnvironmentImpl extends com.android.server.timezonedetector.location.LocationTimeZoneProviderController.Environment {
    private final com.android.server.timezonedetector.StateChangeListener mConfigurationInternalChangeListener;
    private final com.android.server.timezonedetector.ServiceConfigAccessor mServiceConfigAccessor;

    LocationTimeZoneProviderControllerEnvironmentImpl(com.android.server.timezonedetector.location.ThreadingDomain threadingDomain, com.android.server.timezonedetector.ServiceConfigAccessor serviceConfigAccessor, final com.android.server.timezonedetector.location.LocationTimeZoneProviderController controller) {
        super(threadingDomain);
        this.mServiceConfigAccessor = (com.android.server.timezonedetector.ServiceConfigAccessor) java.util.Objects.requireNonNull(serviceConfigAccessor);
        this.mConfigurationInternalChangeListener = new com.android.server.timezonedetector.StateChangeListener() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneProviderControllerEnvironmentImpl$$ExternalSyntheticLambda1
            @Override // com.android.server.timezonedetector.StateChangeListener
            public final void onChange() {
                this.f$0.lambda$new$0(controller);
            }
        };
        this.mServiceConfigAccessor.addConfigurationInternalChangeListener(this.mConfigurationInternalChangeListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(final com.android.server.timezonedetector.location.LocationTimeZoneProviderController controller) {
        com.android.server.timezonedetector.location.ThreadingDomain threadingDomain = this.mThreadingDomain;
        java.util.Objects.requireNonNull(controller);
        threadingDomain.post(new java.lang.Runnable() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneProviderControllerEnvironmentImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                controller.onConfigurationInternalChanged();
            }
        });
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProviderController.Environment
    void destroy() {
        this.mServiceConfigAccessor.removeConfigurationInternalChangeListener(this.mConfigurationInternalChangeListener);
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProviderController.Environment
    com.android.server.timezonedetector.ConfigurationInternal getCurrentUserConfigurationInternal() {
        return this.mServiceConfigAccessor.getCurrentUserConfigurationInternal();
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProviderController.Environment
    java.time.Duration getProviderInitializationTimeout() {
        return this.mServiceConfigAccessor.getLocationTimeZoneProviderInitializationTimeout();
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProviderController.Environment
    java.time.Duration getProviderInitializationTimeoutFuzz() {
        return this.mServiceConfigAccessor.getLocationTimeZoneProviderInitializationTimeoutFuzz();
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProviderController.Environment
    java.time.Duration getUncertaintyDelay() {
        return this.mServiceConfigAccessor.getLocationTimeZoneUncertaintyDelay();
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProviderController.Environment
    java.time.Duration getProviderEventFilteringAgeThreshold() {
        return this.mServiceConfigAccessor.getLocationTimeZoneProviderEventFilteringAgeThreshold();
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProviderController.Environment
    long elapsedRealtimeMillis() {
        return android.os.SystemClock.elapsedRealtime();
    }
}
