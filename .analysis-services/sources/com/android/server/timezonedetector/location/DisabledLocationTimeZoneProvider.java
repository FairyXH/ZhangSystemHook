package com.android.server.timezonedetector.location;

/* JADX INFO: loaded from: classes3.dex */
class DisabledLocationTimeZoneProvider extends com.android.server.timezonedetector.location.LocationTimeZoneProvider {
    DisabledLocationTimeZoneProvider(com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderMetricsLogger providerMetricsLogger, com.android.server.timezonedetector.location.ThreadingDomain threadingDomain, java.lang.String providerName, boolean recordStateChanges) {
        super(providerMetricsLogger, threadingDomain, providerName, new com.android.server.timezonedetector.location.TimeZoneProviderEventPreProcessor() { // from class: com.android.server.timezonedetector.location.DisabledLocationTimeZoneProvider$$ExternalSyntheticLambda0
            @Override // com.android.server.timezonedetector.location.TimeZoneProviderEventPreProcessor
            public final android.service.timezone.TimeZoneProviderEvent preProcess(android.service.timezone.TimeZoneProviderEvent timeZoneProviderEvent) {
                return com.android.server.timezonedetector.location.DisabledLocationTimeZoneProvider.lambda$new$0(timeZoneProviderEvent);
            }
        }, recordStateChanges);
    }

    static /* synthetic */ android.service.timezone.TimeZoneProviderEvent lambda$new$0(android.service.timezone.TimeZoneProviderEvent x) {
        return x;
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProvider
    boolean onInitialize() {
        return false;
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProvider
    void onDestroy() {
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProvider
    void onStartUpdates(java.time.Duration initializationTimeout, java.time.Duration eventFilteringAgeThreshold) {
        throw new java.lang.UnsupportedOperationException("Provider is disabled");
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProvider
    void onStopUpdates() {
        throw new java.lang.UnsupportedOperationException("Provider is disabled");
    }

    @Override // com.android.server.timezonedetector.Dumpable
    public void dump(android.util.IndentingPrintWriter ipw, java.lang.String[] args) {
        synchronized (this.mSharedLock) {
            ipw.println("{DisabledLocationTimeZoneProvider}");
            ipw.println("mProviderName=" + this.mProviderName);
            ipw.println("mCurrentState=" + this.mCurrentState);
        }
    }

    public java.lang.String toString() {
        java.lang.String str;
        synchronized (this.mSharedLock) {
            str = "DisabledLocationTimeZoneProvider{mProviderName=" + this.mProviderName + ", mCurrentState=" + this.mCurrentState + '}';
        }
        return str;
    }
}
