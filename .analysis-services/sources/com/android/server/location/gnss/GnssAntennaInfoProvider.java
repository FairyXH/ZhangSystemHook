package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
public class GnssAntennaInfoProvider extends com.android.server.location.listeners.ListenerMultiplexer<android.os.IBinder, android.location.IGnssAntennaInfoListener, com.android.server.location.listeners.ListenerRegistration<android.location.IGnssAntennaInfoListener>, java.lang.Void> implements com.android.server.location.gnss.hal.GnssNative.BaseCallbacks, com.android.server.location.gnss.hal.GnssNative.AntennaInfoCallbacks {
    private volatile java.util.List<android.location.GnssAntennaInfo> mAntennaInfos;
    private final com.android.server.location.gnss.hal.GnssNative mGnssNative;

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected /* bridge */ /* synthetic */ java.lang.Void mergeRegistrations(java.util.Collection collection) {
        return mergeRegistrations2((java.util.Collection<com.android.server.location.listeners.ListenerRegistration<android.location.IGnssAntennaInfoListener>>) collection);
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected /* bridge */ /* synthetic */ boolean registerWithService(java.lang.Void r1, java.util.Collection collection) {
        return registerWithService2(r1, (java.util.Collection<com.android.server.location.listeners.ListenerRegistration<android.location.IGnssAntennaInfoListener>>) collection);
    }

    protected class AntennaInfoListenerRegistration extends com.android.server.location.listeners.BinderListenerRegistration<android.os.IBinder, android.location.IGnssAntennaInfoListener> {
        private final android.location.util.identity.CallerIdentity mIdentity;

        protected AntennaInfoListenerRegistration(android.location.util.identity.CallerIdentity identity, android.location.IGnssAntennaInfoListener listener) {
            super(identity.isMyProcess() ? com.android.server.FgThread.getExecutor() : com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, listener);
            this.mIdentity = identity;
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        protected java.lang.String getTag() {
            return com.android.server.location.gnss.GnssManagerService.TAG;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.listeners.RemovableListenerRegistration
        public com.android.server.location.gnss.GnssAntennaInfoProvider getOwner() {
            return com.android.server.location.gnss.GnssAntennaInfoProvider.this;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.listeners.BinderListenerRegistration
        public android.os.IBinder getBinderFromKey(android.os.IBinder key) {
            return key;
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        public java.lang.String toString() {
            return this.mIdentity.toString();
        }
    }

    GnssAntennaInfoProvider(com.android.server.location.gnss.hal.GnssNative gnssNative) {
        this.mGnssNative = gnssNative;
        this.mGnssNative.addBaseCallbacks(this);
        this.mGnssNative.addAntennaInfoCallbacks(this);
    }

    java.util.List<android.location.GnssAntennaInfo> getAntennaInfos() {
        return this.mAntennaInfos;
    }

    public boolean isSupported() {
        return this.mGnssNative.isAntennaInfoSupported();
    }

    public void addListener(android.location.util.identity.CallerIdentity callerIdentity, android.location.IGnssAntennaInfoListener listener) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            putRegistration(listener.asBinder(), new com.android.server.location.gnss.GnssAntennaInfoProvider.AntennaInfoListenerRegistration(callerIdentity, listener));
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void removeListener(android.location.IGnssAntennaInfoListener listener) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            removeRegistration(listener.asBinder());
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    /* JADX INFO: renamed from: registerWithService, reason: avoid collision after fix types in other method */
    protected boolean registerWithService2(java.lang.Void merged, java.util.Collection<com.android.server.location.listeners.ListenerRegistration<android.location.IGnssAntennaInfoListener>> listenerRegistrations) {
        return true;
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void unregisterWithService() {
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected boolean isActive(com.android.server.location.listeners.ListenerRegistration<android.location.IGnssAntennaInfoListener> registration) {
        return true;
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    /* JADX INFO: renamed from: mergeRegistrations, reason: avoid collision after fix types in other method */
    protected java.lang.Void mergeRegistrations2(java.util.Collection<com.android.server.location.listeners.ListenerRegistration<android.location.IGnssAntennaInfoListener>> listenerRegistrations) {
        return null;
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.BaseCallbacks
    public void onHalStarted() {
        this.mGnssNative.startAntennaInfoListening();
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.BaseCallbacks
    public void onHalRestarted() {
        this.mGnssNative.startAntennaInfoListening();
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.AntennaInfoCallbacks
    public void onReportAntennaInfo(final java.util.List<android.location.GnssAntennaInfo> antennaInfos) {
        if (antennaInfos.equals(this.mAntennaInfos)) {
            return;
        }
        this.mAntennaInfos = antennaInfos;
        deliverToListeners(new com.android.internal.listeners.ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.gnss.GnssAntennaInfoProvider$$ExternalSyntheticLambda0
            public final void operate(java.lang.Object obj) {
                ((android.location.IGnssAntennaInfoListener) obj).onGnssAntennaInfoChanged(antennaInfos);
            }
        });
    }
}
