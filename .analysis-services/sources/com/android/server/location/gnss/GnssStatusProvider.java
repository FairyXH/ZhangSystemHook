package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
public class GnssStatusProvider extends com.android.server.location.gnss.GnssListenerMultiplexer<java.lang.Void, android.location.IGnssStatusListener, java.lang.Void> implements com.android.server.location.gnss.hal.GnssNative.BaseCallbacks, com.android.server.location.gnss.hal.GnssNative.StatusCallbacks, com.android.server.location.gnss.hal.GnssNative.SvStatusCallbacks {
    private final com.android.server.location.injector.AppOpsHelper mAppOpsHelper;
    private final com.android.server.location.gnss.hal.GnssNative mGnssNative;
    private boolean mIsNavigating;
    private final com.android.server.location.injector.LocationUsageLogger mLogger;

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected /* bridge */ /* synthetic */ boolean registerWithService(java.lang.Object obj, java.util.Collection collection) {
        return registerWithService((java.lang.Void) obj, (java.util.Collection<com.android.server.location.gnss.GnssListenerMultiplexer<java.lang.Void, android.location.IGnssStatusListener, java.lang.Void>.GnssListenerRegistration>) collection);
    }

    public GnssStatusProvider(com.android.server.location.injector.Injector injector, com.android.server.location.gnss.hal.GnssNative gnssNative) {
        super(injector);
        this.mIsNavigating = false;
        this.mAppOpsHelper = injector.getAppOpsHelper();
        this.mLogger = injector.getLocationUsageLogger();
        this.mGnssNative = gnssNative;
        gnssNative.addBaseCallbacks(this);
        gnssNative.addStatusCallbacks(this);
        gnssNative.addSvStatusCallbacks(this);
    }

    @Override // com.android.server.location.gnss.GnssListenerMultiplexer
    public void addListener(android.location.util.identity.CallerIdentity identity, android.location.IGnssStatusListener listener) {
        super.addListener(identity, listener);
    }

    protected boolean registerWithService(java.lang.Void ignored, java.util.Collection<com.android.server.location.gnss.GnssListenerMultiplexer<java.lang.Void, android.location.IGnssStatusListener, java.lang.Void>.GnssListenerRegistration> registrations) {
        if (this.mGnssNative.startSvStatusCollection()) {
            if (com.android.server.location.gnss.GnssManagerService.D) {
                android.util.Log.d(com.android.server.location.gnss.GnssManagerService.TAG, "starting gnss sv status");
                return true;
            }
            return true;
        }
        android.util.Log.e(com.android.server.location.gnss.GnssManagerService.TAG, "error starting gnss sv status");
        return false;
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void unregisterWithService() {
        if (this.mGnssNative.stopSvStatusCollection()) {
            if (com.android.server.location.gnss.GnssManagerService.D) {
                android.util.Log.d(com.android.server.location.gnss.GnssManagerService.TAG, "stopping gnss sv status");
                return;
            }
            return;
        }
        android.util.Log.e(com.android.server.location.gnss.GnssManagerService.TAG, "error stopping gnss sv status");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    public void onRegistrationAdded(android.os.IBinder key, com.android.server.location.gnss.GnssListenerMultiplexer<java.lang.Void, android.location.IGnssStatusListener, java.lang.Void>.GnssListenerRegistration registration) {
        this.mLogger.logLocationApiUsage(0, 3, registration.getIdentity().getPackageName(), registration.getIdentity().getAttributionTag(), null, null, true, false, null, registration.isForeground());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    public void onRegistrationRemoved(android.os.IBinder key, com.android.server.location.gnss.GnssListenerMultiplexer<java.lang.Void, android.location.IGnssStatusListener, java.lang.Void>.GnssListenerRegistration registration) {
        this.mLogger.logLocationApiUsage(1, 3, registration.getIdentity().getPackageName(), registration.getIdentity().getAttributionTag(), null, null, true, false, null, registration.isForeground());
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.BaseCallbacks
    public void onHalRestarted() {
        resetService();
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.StatusCallbacks
    public void onReportStatus(int gnssStatus) {
        boolean isNavigating;
        switch (gnssStatus) {
            case 1:
                isNavigating = true;
                break;
            case 2:
            case 4:
                isNavigating = false;
                break;
            case 3:
            default:
                isNavigating = this.mIsNavigating;
                break;
        }
        if (isNavigating != this.mIsNavigating) {
            this.mIsNavigating = isNavigating;
            if (isNavigating) {
                deliverToListeners(new com.android.internal.listeners.ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.gnss.GnssStatusProvider$$ExternalSyntheticLambda3
                    public final void operate(java.lang.Object obj) {
                        ((android.location.IGnssStatusListener) obj).onGnssStarted();
                    }
                });
            } else {
                deliverToListeners(new com.android.internal.listeners.ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.gnss.GnssStatusProvider$$ExternalSyntheticLambda4
                    public final void operate(java.lang.Object obj) {
                        ((android.location.IGnssStatusListener) obj).onGnssStopped();
                    }
                });
            }
        }
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.StatusCallbacks
    public void onReportFirstFix(final int ttff) {
        deliverToListeners(new com.android.internal.listeners.ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.gnss.GnssStatusProvider$$ExternalSyntheticLambda0
            public final void operate(java.lang.Object obj) {
                ((android.location.IGnssStatusListener) obj).onFirstFix(ttff);
            }
        });
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.SvStatusCallbacks
    public void onReportSvStatus(final android.location.GnssStatus gnssStatus) {
        deliverToListeners(new java.util.function.Function() { // from class: com.android.server.location.gnss.GnssStatusProvider$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.lambda$onReportSvStatus$2(gnssStatus, (com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.internal.listeners.ListenerExecutor.ListenerOperation lambda$onReportSvStatus$2(final android.location.GnssStatus gnssStatus, com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration registration) {
        if (this.mAppOpsHelper.noteOpNoThrow(1, registration.getIdentity())) {
            return new com.android.internal.listeners.ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.gnss.GnssStatusProvider$$ExternalSyntheticLambda1
                public final void operate(java.lang.Object obj) {
                    ((android.location.IGnssStatusListener) obj).onSvStatusChanged(gnssStatus);
                }
            };
        }
        return null;
    }
}
