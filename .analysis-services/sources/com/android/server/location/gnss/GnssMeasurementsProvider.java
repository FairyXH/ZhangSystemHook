package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
public final class GnssMeasurementsProvider extends com.android.server.location.gnss.GnssListenerMultiplexer<android.location.GnssMeasurementRequest, android.location.IGnssMeasurementsListener, android.location.GnssMeasurementRequest> implements com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener, com.android.server.location.gnss.hal.GnssNative.BaseCallbacks, com.android.server.location.gnss.hal.GnssNative.MeasurementCallbacks {
    private final com.android.server.location.injector.AppOpsHelper mAppOpsHelper;
    private android.location.GnssMeasurementRequest mCurrentRequest;
    private com.android.server.location.gnss.IGnssMeasurementsProviderWrapper mGnssMeasurementsProviderWrapper;
    private final com.android.server.location.gnss.hal.GnssNative mGnssNative;
    private android.location.GnssMeasurementsEvent mLastGnssMeasurementsEvent;
    private final com.android.server.location.injector.LocationUsageLogger mLogger;

    @Override // com.android.server.location.gnss.GnssListenerMultiplexer, com.android.server.location.listeners.ListenerMultiplexer
    protected /* bridge */ /* synthetic */ java.lang.Object mergeRegistrations(java.util.Collection collection) {
        return mergeRegistrations((java.util.Collection<com.android.server.location.gnss.GnssListenerMultiplexer<android.location.GnssMeasurementRequest, android.location.IGnssMeasurementsListener, android.location.GnssMeasurementRequest>.GnssListenerRegistration>) collection);
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected /* bridge */ /* synthetic */ boolean registerWithService(java.lang.Object obj, java.util.Collection collection) {
        return registerWithService((android.location.GnssMeasurementRequest) obj, (java.util.Collection<com.android.server.location.gnss.GnssListenerMultiplexer<android.location.GnssMeasurementRequest, android.location.IGnssMeasurementsListener, android.location.GnssMeasurementRequest>.GnssListenerRegistration>) collection);
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected /* bridge */ /* synthetic */ boolean reregisterWithService(java.lang.Object obj, java.lang.Object obj2, java.util.Collection collection) {
        return reregisterWithService((android.location.GnssMeasurementRequest) obj, (android.location.GnssMeasurementRequest) obj2, (java.util.Collection<com.android.server.location.gnss.GnssListenerMultiplexer<android.location.GnssMeasurementRequest, android.location.IGnssMeasurementsListener, android.location.GnssMeasurementRequest>.GnssListenerRegistration>) collection);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class GnssMeasurementListenerRegistration extends com.android.server.location.gnss.GnssListenerMultiplexer<android.location.GnssMeasurementRequest, android.location.IGnssMeasurementsListener, android.location.GnssMeasurementRequest>.GnssListenerRegistration {
        protected GnssMeasurementListenerRegistration(android.location.GnssMeasurementRequest request, android.location.util.identity.CallerIdentity callerIdentity, android.location.IGnssMeasurementsListener listener) {
            super(request, callerIdentity, listener);
        }

        @Override // com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration, com.android.server.location.listeners.BinderListenerRegistration, com.android.server.location.listeners.RemovableListenerRegistration
        protected void onRegister() {
            super.onRegister();
            com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logGnssMeasurementClientRegistered(getIdentity(), (android.location.GnssMeasurementRequest) getRequest());
            executeOperation(new com.android.internal.listeners.ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.gnss.GnssMeasurementsProvider$GnssMeasurementListenerRegistration$$ExternalSyntheticLambda0
                public final void operate(java.lang.Object obj) {
                    ((android.location.IGnssMeasurementsListener) obj).onStatusChanged(1);
                }
            });
        }

        @Override // com.android.server.location.listeners.BinderListenerRegistration, com.android.server.location.listeners.RemovableListenerRegistration, com.android.server.location.listeners.ListenerRegistration
        protected void onUnregister() {
            com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logGnssMeasurementClientUnregistered(getIdentity());
            super.onUnregister();
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        protected void onActive() {
            com.android.server.location.gnss.GnssMeasurementsProvider.this.mAppOpsHelper.startOpNoThrow(42, getIdentity());
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        protected void onInactive() {
            com.android.server.location.gnss.GnssMeasurementsProvider.this.mAppOpsHelper.finishOp(42, getIdentity());
        }
    }

    public GnssMeasurementsProvider(com.android.server.location.injector.Injector injector, com.android.server.location.gnss.hal.GnssNative gnssNative) {
        super(injector);
        this.mGnssMeasurementsProviderWrapper = new com.android.server.location.gnss.GnssMeasurementsProvider.GnssMeasurementsProviderWrapper();
        this.mAppOpsHelper = injector.getAppOpsHelper();
        this.mLogger = injector.getLocationUsageLogger();
        this.mGnssNative = gnssNative;
        this.mGnssNative.addBaseCallbacks(this);
        this.mGnssNative.addMeasurementCallbacks(this);
        ((com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, null)).onGnssMeasurementsProviderInit(this);
    }

    @Override // com.android.server.location.gnss.GnssListenerMultiplexer
    public boolean isSupported() {
        return this.mGnssNative.isMeasurementSupported();
    }

    @Override // com.android.server.location.gnss.GnssListenerMultiplexer
    public void addListener(android.location.GnssMeasurementRequest request, android.location.util.identity.CallerIdentity identity, android.location.IGnssMeasurementsListener listener) {
        super.addListener(request, identity, listener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.gnss.GnssListenerMultiplexer
    public com.android.server.location.gnss.GnssListenerMultiplexer<android.location.GnssMeasurementRequest, android.location.IGnssMeasurementsListener, android.location.GnssMeasurementRequest>.GnssListenerRegistration createRegistration(android.location.GnssMeasurementRequest request, android.location.util.identity.CallerIdentity callerIdentity, android.location.IGnssMeasurementsListener listener) {
        return new com.android.server.location.gnss.GnssMeasurementsProvider.GnssMeasurementListenerRegistration(request, callerIdentity, listener);
    }

    protected boolean registerWithService(android.location.GnssMeasurementRequest request, java.util.Collection<com.android.server.location.gnss.GnssListenerMultiplexer<android.location.GnssMeasurementRequest, android.location.IGnssMeasurementsListener, android.location.GnssMeasurementRequest>.GnssListenerRegistration> registrations) {
        if (request.getIntervalMillis() == Integer.MAX_VALUE) {
            return true;
        }
        android.util.Log.v(com.android.server.location.gnss.GnssManagerService.TAG, "registerWithService and add measurement request");
        this.mCurrentRequest = request;
        if (this.mGnssNative.startMeasurementCollection(request.isFullTracking(), request.isCorrelationVectorOutputsEnabled(), request.getIntervalMillis())) {
            if (com.android.server.location.gnss.GnssManagerService.D) {
                android.util.Log.d(com.android.server.location.gnss.GnssManagerService.TAG, "starting gnss measurements (" + request + ")");
            }
            return true;
        }
        android.util.Log.e(com.android.server.location.gnss.GnssManagerService.TAG, "error starting gnss measurements");
        return false;
    }

    protected boolean reregisterWithService(android.location.GnssMeasurementRequest old, android.location.GnssMeasurementRequest request, java.util.Collection<com.android.server.location.gnss.GnssListenerMultiplexer<android.location.GnssMeasurementRequest, android.location.IGnssMeasurementsListener, android.location.GnssMeasurementRequest>.GnssListenerRegistration> registrations) {
        if (request.getIntervalMillis() == Integer.MAX_VALUE) {
            unregisterWithService();
            return true;
        }
        com.android.server.location.gnss.GnssConfiguration.HalInterfaceVersion halInterfaceVersion = this.mGnssNative.getConfiguration().getHalInterfaceVersion();
        boolean aidlV3Plus = halInterfaceVersion != null && halInterfaceVersion.mMajor == 3 && halInterfaceVersion.mMinor >= 3;
        if (!aidlV3Plus) {
            unregisterWithService();
        }
        return registerWithService(request, registrations);
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void unregisterWithService() {
        android.util.Log.v(com.android.server.location.gnss.GnssManagerService.TAG, "unregisterWithService and remove measurement request");
        this.mCurrentRequest = null;
        if (this.mGnssNative.stopMeasurementCollection()) {
            if (com.android.server.location.gnss.GnssManagerService.D) {
                android.util.Log.d(com.android.server.location.gnss.GnssManagerService.TAG, "stopping gnss measurements");
                return;
            }
            return;
        }
        android.util.Log.e(com.android.server.location.gnss.GnssManagerService.TAG, "error stopping gnss measurements");
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void onActive() {
        this.mSettingsHelper.addOnGnssMeasurementsFullTrackingEnabledChangedListener(this);
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void onInactive() {
        this.mSettingsHelper.removeOnGnssMeasurementsFullTrackingEnabledChangedListener(this);
    }

    @Override // com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener
    public void onSettingChanged() {
        updateService();
    }

    @Override // com.android.server.location.gnss.GnssListenerMultiplexer, com.android.server.location.listeners.ListenerMultiplexer
    protected android.location.GnssMeasurementRequest mergeRegistrations(java.util.Collection<com.android.server.location.gnss.GnssListenerMultiplexer<android.location.GnssMeasurementRequest, android.location.IGnssMeasurementsListener, android.location.GnssMeasurementRequest>.GnssListenerRegistration> registrations) {
        boolean fullTracking = false;
        boolean enableCorrVecOutputs = false;
        int intervalMillis = Integer.MAX_VALUE;
        if (this.mSettingsHelper.isGnssMeasurementsFullTrackingEnabled()) {
            fullTracking = true;
        }
        for (com.android.server.location.gnss.GnssListenerMultiplexer<android.location.GnssMeasurementRequest, android.location.IGnssMeasurementsListener, android.location.GnssMeasurementRequest>.GnssListenerRegistration registration : registrations) {
            android.location.GnssMeasurementRequest request = registration.getRequest();
            if (request.getIntervalMillis() != Integer.MAX_VALUE) {
                if (request.isFullTracking()) {
                    fullTracking = true;
                }
                if (request.isCorrelationVectorOutputsEnabled()) {
                    enableCorrVecOutputs = true;
                }
                intervalMillis = java.lang.Math.min(intervalMillis, request.getIntervalMillis());
            }
        }
        return new android.location.GnssMeasurementRequest.Builder().setFullTracking(fullTracking).setCorrelationVectorOutputsEnabled(enableCorrVecOutputs).setIntervalMillis(intervalMillis).build();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    public void onRegistrationAdded(android.os.IBinder key, com.android.server.location.gnss.GnssListenerMultiplexer<android.location.GnssMeasurementRequest, android.location.IGnssMeasurementsListener, android.location.GnssMeasurementRequest>.GnssListenerRegistration registration) {
        ((com.android.server.location.interfaces.IGnssMeasurementsProviderExt) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IGnssMeasurementsProviderExt.DEFAULT, new java.lang.Object[0])).onRegistrationAdded(registration.getIdentity(), registration.getRequest());
        this.mLogger.logLocationApiUsage(0, 2, registration.getIdentity().getPackageName(), registration.getIdentity().getAttributionTag(), null, null, true, false, null, registration.isForeground());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    public void onRegistrationRemoved(android.os.IBinder key, com.android.server.location.gnss.GnssListenerMultiplexer<android.location.GnssMeasurementRequest, android.location.IGnssMeasurementsListener, android.location.GnssMeasurementRequest>.GnssListenerRegistration registration) {
        ((com.android.server.location.interfaces.IGnssMeasurementsProviderExt) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IGnssMeasurementsProviderExt.DEFAULT, new java.lang.Object[0])).onRegistrationRemoved(registration.getIdentity());
        this.mLogger.logLocationApiUsage(1, 2, registration.getIdentity().getPackageName(), registration.getIdentity().getAttributionTag(), null, null, true, false, null, registration.isForeground());
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.BaseCallbacks
    public void onHalRestarted() {
        resetService();
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.MeasurementCallbacks
    public void onReportMeasurements(final android.location.GnssMeasurementsEvent event) {
        deliverToListeners(new java.util.function.Function() { // from class: com.android.server.location.gnss.GnssMeasurementsProvider$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.lambda$onReportMeasurements$1(event, (com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration) obj);
            }
        });
        synchronized (this.mMultiplexerLock) {
            this.mLastGnssMeasurementsEvent = event;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.internal.listeners.ListenerExecutor.ListenerOperation lambda$onReportMeasurements$1(final android.location.GnssMeasurementsEvent event, com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration registration) {
        if (this.mAppOpsHelper.noteOpNoThrow(1, registration.getIdentity())) {
            com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logGnssMeasurementsDelivered(event.getMeasurements().size(), registration.getIdentity());
            return new com.android.internal.listeners.ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.gnss.GnssMeasurementsProvider$$ExternalSyntheticLambda1
                public final void operate(java.lang.Object obj) {
                    ((android.location.IGnssMeasurementsListener) obj).onGnssMeasurementsReceived(event);
                }
            };
        }
        return null;
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        super.dump(fd, pw, args);
        pw.print("last measurements=");
        pw.println(getLastMeasurementEventSummary());
    }

    private java.lang.String getLastMeasurementEventSummary() {
        synchronized (this.mMultiplexerLock) {
            if (this.mLastGnssMeasurementsEvent == null) {
                return null;
            }
            java.lang.StringBuilder builder = new java.lang.StringBuilder("[");
            builder.append("elapsedRealtimeNs=").append(this.mLastGnssMeasurementsEvent.getClock().getElapsedRealtimeNanos());
            builder.append(" measurementCount=").append(this.mLastGnssMeasurementsEvent.getMeasurements().size());
            float sumBasebandCn0 = 0.0f;
            int countBasebandCn0 = 0;
            for (android.location.GnssMeasurement measurement : this.mLastGnssMeasurementsEvent.getMeasurements()) {
                if (measurement.hasBasebandCn0DbHz()) {
                    sumBasebandCn0 = (float) (((double) sumBasebandCn0) + measurement.getBasebandCn0DbHz());
                    countBasebandCn0++;
                }
            }
            if (countBasebandCn0 > 0) {
                builder.append(" avgBasebandCn0=").append(sumBasebandCn0 / countBasebandCn0);
            }
            builder.append("]");
            return builder.toString();
        }
    }

    public com.android.server.location.gnss.IGnssMeasurementsProviderWrapper getGnssMeasurementsProviderWrapper() {
        return this.mGnssMeasurementsProviderWrapper;
    }

    private class GnssMeasurementsProviderWrapper implements com.android.server.location.gnss.IGnssMeasurementsProviderWrapper {
        private GnssMeasurementsProviderWrapper() {
        }

        @Override // com.android.server.location.gnss.IGnssMeasurementsProviderWrapper
        public void stopMeasurementCollection() {
            if (com.android.server.location.gnss.GnssMeasurementsProvider.this.mGnssNative.stopMeasurementCollection()) {
                android.util.Log.d(com.android.server.location.gnss.GnssManagerService.TAG, "oplus stop measurements");
            } else {
                android.util.Log.e(com.android.server.location.gnss.GnssManagerService.TAG, "oplus error stopping measurements");
            }
        }

        @Override // com.android.server.location.gnss.IGnssMeasurementsProviderWrapper
        public void restart() {
            if (com.android.server.location.gnss.GnssMeasurementsProvider.this.mCurrentRequest == null) {
                android.util.Log.w(com.android.server.location.gnss.GnssManagerService.TAG, "oplus restart measurements while current request is removed");
            } else if (com.android.server.location.gnss.GnssMeasurementsProvider.this.mGnssNative.startMeasurementCollection(com.android.server.location.gnss.GnssMeasurementsProvider.this.mCurrentRequest.isFullTracking(), com.android.server.location.gnss.GnssMeasurementsProvider.this.mCurrentRequest.isCorrelationVectorOutputsEnabled(), com.android.server.location.gnss.GnssMeasurementsProvider.this.mCurrentRequest.getIntervalMillis())) {
                android.util.Log.i(com.android.server.location.gnss.GnssManagerService.TAG, "oplus restart measurements (" + com.android.server.location.gnss.GnssMeasurementsProvider.this.mCurrentRequest + ")");
            } else {
                android.util.Log.e(com.android.server.location.gnss.GnssManagerService.TAG, "oplus error restarting measurements");
            }
        }
    }
}
