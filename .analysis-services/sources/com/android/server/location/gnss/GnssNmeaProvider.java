package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
class GnssNmeaProvider extends com.android.server.location.gnss.GnssListenerMultiplexer<java.lang.Void, android.location.IGnssNmeaListener, java.lang.Void> implements com.android.server.location.gnss.hal.GnssNative.BaseCallbacks, com.android.server.location.gnss.hal.GnssNative.NmeaCallbacks {
    private final com.android.server.location.injector.AppOpsHelper mAppOpsHelper;
    private final com.android.server.location.gnss.hal.GnssNative mGnssNative;
    private com.android.server.location.gnss.IGnssNmeaProviderWrapper mGnssNmeaProviderWrapper;
    private final byte[] mNmeaBuffer;
    private com.android.server.location.interfaces.IOplusLBSMainClass mOplusLbsClass;
    private boolean mPreciseLocationSupported;

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected /* bridge */ /* synthetic */ boolean registerWithService(java.lang.Object obj, java.util.Collection collection) {
        return registerWithService((java.lang.Void) obj, (java.util.Collection<com.android.server.location.gnss.GnssListenerMultiplexer<java.lang.Void, android.location.IGnssNmeaListener, java.lang.Void>.GnssListenerRegistration>) collection);
    }

    GnssNmeaProvider(com.android.server.location.injector.Injector injector, com.android.server.location.gnss.hal.GnssNative gnssNative) {
        super(injector);
        this.mNmeaBuffer = new byte[120];
        this.mOplusLbsClass = null;
        this.mPreciseLocationSupported = false;
        this.mGnssNmeaProviderWrapper = new com.android.server.location.gnss.GnssNmeaProvider.GnssNmeaProviderWrapper();
        this.mAppOpsHelper = injector.getAppOpsHelper();
        this.mGnssNative = gnssNative;
        this.mGnssNative.addBaseCallbacks(this);
        this.mGnssNative.addNmeaCallbacks(this);
        this.mOplusLbsClass = (com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, null);
        if (this.mOplusLbsClass != null) {
            this.mPreciseLocationSupported = this.mOplusLbsClass.isPreciseLocationSupported();
        }
    }

    @Override // com.android.server.location.gnss.GnssListenerMultiplexer
    public void addListener(android.location.util.identity.CallerIdentity identity, android.location.IGnssNmeaListener listener) {
        super.addListener(identity, listener);
    }

    protected boolean registerWithService(java.lang.Void ignored, java.util.Collection<com.android.server.location.gnss.GnssListenerMultiplexer<java.lang.Void, android.location.IGnssNmeaListener, java.lang.Void>.GnssListenerRegistration> registrations) {
        if (this.mGnssNative.startNmeaMessageCollection()) {
            if (com.android.server.location.gnss.GnssManagerService.D) {
                android.util.Log.d(com.android.server.location.gnss.GnssManagerService.TAG, "starting gnss nmea messages collection");
                return true;
            }
            return true;
        }
        android.util.Log.e(com.android.server.location.gnss.GnssManagerService.TAG, "error starting gnss nmea messages collection");
        return false;
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void unregisterWithService() {
        if (this.mGnssNative.stopNmeaMessageCollection()) {
            if (com.android.server.location.gnss.GnssManagerService.D) {
                android.util.Log.d(com.android.server.location.gnss.GnssManagerService.TAG, "stopping gnss nmea messages collection");
                return;
            }
            return;
        }
        android.util.Log.e(com.android.server.location.gnss.GnssManagerService.TAG, "error stopping gnss nmea messages collection");
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.BaseCallbacks
    public void onHalRestarted() {
        resetService();
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.NmeaCallbacks
    public void onReportNmea(long timestamp) {
        deliverToListeners(new com.android.server.location.gnss.GnssNmeaProvider.AnonymousClass1(timestamp));
    }

    /* JADX INFO: renamed from: com.android.server.location.gnss.GnssNmeaProvider$1, reason: invalid class name */
    class AnonymousClass1 implements java.util.function.Function<com.android.server.location.gnss.GnssListenerMultiplexer<java.lang.Void, android.location.IGnssNmeaListener, java.lang.Void>.GnssListenerRegistration, com.android.internal.listeners.ListenerExecutor.ListenerOperation<android.location.IGnssNmeaListener>> {
        private java.lang.String mNmea;
        final /* synthetic */ long val$timestamp;

        AnonymousClass1(long j) {
            this.val$timestamp = j;
        }

        @Override // java.util.function.Function
        public com.android.internal.listeners.ListenerExecutor.ListenerOperation<android.location.IGnssNmeaListener> apply(com.android.server.location.gnss.GnssListenerMultiplexer<java.lang.Void, android.location.IGnssNmeaListener, java.lang.Void>.GnssListenerRegistration registration) {
            if (com.android.server.location.gnss.GnssNmeaProvider.this.mAppOpsHelper.noteOpNoThrow(1, registration.getIdentity())) {
                if (this.mNmea == null) {
                    int length = com.android.server.location.gnss.GnssNmeaProvider.this.mGnssNative.readNmea(com.android.server.location.gnss.GnssNmeaProvider.this.mNmeaBuffer, com.android.server.location.gnss.GnssNmeaProvider.this.mNmeaBuffer.length);
                    this.mNmea = new java.lang.String(com.android.server.location.gnss.GnssNmeaProvider.this.mNmeaBuffer, 0, length);
                    if (com.android.server.location.gnss.GnssNmeaProvider.this.mPreciseLocationSupported && com.android.server.location.gnss.GnssNmeaProvider.this.mOplusLbsClass != null) {
                        this.mNmea = com.android.server.location.gnss.GnssNmeaProvider.this.mOplusLbsClass.reduceAccuracyOfNmeaSentences(this.mNmea);
                    }
                }
                final long j = this.val$timestamp;
                return new com.android.internal.listeners.ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.gnss.GnssNmeaProvider$1$$ExternalSyntheticLambda0
                    public final void operate(java.lang.Object obj) throws java.lang.Exception {
                        this.f$0.lambda$apply$0(j, (android.location.IGnssNmeaListener) obj);
                    }
                };
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$apply$0(long timestamp, android.location.IGnssNmeaListener listener) throws java.lang.Exception {
            listener.onNmeaReceived(timestamp, this.mNmea);
        }
    }

    public com.android.server.location.gnss.IGnssNmeaProviderWrapper getGnssNmeaProviderWrapper() {
        return this.mGnssNmeaProviderWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class GnssNmeaProviderWrapper implements com.android.server.location.gnss.IGnssNmeaProviderWrapper {
        private GnssNmeaProviderWrapper() {
        }

        @Override // com.android.server.location.gnss.IGnssNmeaProviderWrapper
        public void onReportNmea(long timestamp, java.lang.String nmea) {
            com.android.server.location.gnss.GnssNmeaProvider.this.deliverToListeners(new com.android.server.location.gnss.GnssNmeaProvider.GnssNmeaProviderWrapper.AnonymousClass1(timestamp, nmea));
        }

        /* JADX INFO: renamed from: com.android.server.location.gnss.GnssNmeaProvider$GnssNmeaProviderWrapper$1, reason: invalid class name */
        class AnonymousClass1 implements java.util.function.Function<com.android.server.location.gnss.GnssListenerMultiplexer<java.lang.Void, android.location.IGnssNmeaListener, java.lang.Void>.GnssListenerRegistration, com.android.internal.listeners.ListenerExecutor.ListenerOperation<android.location.IGnssNmeaListener>> {
            private java.lang.String mNmea;
            final /* synthetic */ java.lang.String val$nmea;
            final /* synthetic */ long val$timestamp;

            AnonymousClass1(long j, java.lang.String str) {
                this.val$timestamp = j;
                this.val$nmea = str;
            }

            @Override // java.util.function.Function
            public com.android.internal.listeners.ListenerExecutor.ListenerOperation<android.location.IGnssNmeaListener> apply(com.android.server.location.gnss.GnssListenerMultiplexer<java.lang.Void, android.location.IGnssNmeaListener, java.lang.Void>.GnssListenerRegistration registration) {
                if (com.android.server.location.gnss.GnssNmeaProvider.this.mAppOpsHelper.noteOpNoThrow(1, registration.getIdentity())) {
                    final long j = this.val$timestamp;
                    final java.lang.String str = this.val$nmea;
                    return new com.android.internal.listeners.ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.gnss.GnssNmeaProvider$GnssNmeaProviderWrapper$1$$ExternalSyntheticLambda0
                        public final void operate(java.lang.Object obj) {
                            ((android.location.IGnssNmeaListener) obj).onNmeaReceived(j, str);
                        }
                    };
                }
                return null;
            }
        }
    }
}
