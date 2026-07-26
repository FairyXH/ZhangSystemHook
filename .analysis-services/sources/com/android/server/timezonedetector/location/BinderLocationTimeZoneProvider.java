package com.android.server.timezonedetector.location;

/* JADX INFO: loaded from: classes3.dex */
class BinderLocationTimeZoneProvider extends com.android.server.timezonedetector.location.LocationTimeZoneProvider {
    private final com.android.server.timezonedetector.location.LocationTimeZoneProviderProxy mProxy;

    BinderLocationTimeZoneProvider(com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderMetricsLogger providerMetricsLogger, com.android.server.timezonedetector.location.ThreadingDomain threadingDomain, java.lang.String providerName, com.android.server.timezonedetector.location.LocationTimeZoneProviderProxy proxy, boolean recordStateChanges) {
        super(providerMetricsLogger, threadingDomain, providerName, new com.android.server.timezonedetector.location.ZoneInfoDbTimeZoneProviderEventPreProcessor(), recordStateChanges);
        this.mProxy = (com.android.server.timezonedetector.location.LocationTimeZoneProviderProxy) java.util.Objects.requireNonNull(proxy);
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProvider
    boolean onInitialize() {
        this.mProxy.initialize(new com.android.server.timezonedetector.location.LocationTimeZoneProviderProxy.Listener() { // from class: com.android.server.timezonedetector.location.BinderLocationTimeZoneProvider.1
            @Override // com.android.server.timezonedetector.location.LocationTimeZoneProviderProxy.Listener
            public void onReportTimeZoneProviderEvent(android.service.timezone.TimeZoneProviderEvent timeZoneProviderEvent) {
                com.android.server.timezonedetector.location.BinderLocationTimeZoneProvider.this.handleTimeZoneProviderEvent(timeZoneProviderEvent);
            }

            @Override // com.android.server.timezonedetector.location.LocationTimeZoneProviderProxy.Listener
            public void onProviderBound() {
                com.android.server.timezonedetector.location.BinderLocationTimeZoneProvider.this.handleOnProviderBound();
            }

            @Override // com.android.server.timezonedetector.location.LocationTimeZoneProviderProxy.Listener
            public void onProviderUnbound() {
                com.android.server.timezonedetector.location.BinderLocationTimeZoneProvider.this.handleTemporaryFailure("onProviderUnbound()");
            }
        });
        return true;
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProvider
    void onDestroy() {
        this.mProxy.destroy();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnProviderBound() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState currentState = this.mCurrentState.get();
            switch (currentState.stateEnum) {
                case 1:
                case 2:
                case 3:
                    com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("handleOnProviderBound mProviderName=" + this.mProviderName + ", currentState=" + currentState + ": Provider is started.");
                    break;
                case 4:
                    com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("handleOnProviderBound mProviderName=" + this.mProviderName + ", currentState=" + currentState + ": Provider is stopped.");
                    break;
                case 5:
                case 6:
                    com.android.server.timezonedetector.location.LocationTimeZoneManagerService.debugLog("handleOnProviderBound, mProviderName=" + this.mProviderName + ", currentState=" + currentState + ": No state change required, provider is terminated.");
                    break;
                default:
                    throw new java.lang.IllegalStateException("Unknown currentState=" + currentState);
            }
        }
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProvider
    void onStartUpdates(java.time.Duration initializationTimeout, java.time.Duration eventFilteringAgeThreshold) {
        com.android.server.timezonedetector.location.TimeZoneProviderRequest request = com.android.server.timezonedetector.location.TimeZoneProviderRequest.createStartUpdatesRequest(initializationTimeout, eventFilteringAgeThreshold);
        this.mProxy.setRequest(request);
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProvider
    void onStopUpdates() {
        com.android.server.timezonedetector.location.TimeZoneProviderRequest request = com.android.server.timezonedetector.location.TimeZoneProviderRequest.createStopUpdatesRequest();
        this.mProxy.setRequest(request);
    }

    @Override // com.android.server.timezonedetector.Dumpable
    public void dump(android.util.IndentingPrintWriter ipw, java.lang.String[] args) {
        synchronized (this.mSharedLock) {
            ipw.println("{BinderLocationTimeZoneProvider}");
            ipw.println("mProviderName=" + this.mProviderName);
            ipw.println("mCurrentState=" + this.mCurrentState);
            ipw.println("mProxy=" + this.mProxy);
            ipw.println("State history:");
            ipw.increaseIndent();
            this.mCurrentState.dump(ipw);
            ipw.decreaseIndent();
            ipw.println("Proxy details:");
            ipw.increaseIndent();
            this.mProxy.dump(ipw, args);
            ipw.decreaseIndent();
        }
    }

    public java.lang.String toString() {
        java.lang.String str;
        synchronized (this.mSharedLock) {
            str = "BinderLocationTimeZoneProvider{mProviderName=" + this.mProviderName + ", mCurrentState=" + this.mCurrentState + ", mProxy=" + this.mProxy + '}';
        }
        return str;
    }
}
