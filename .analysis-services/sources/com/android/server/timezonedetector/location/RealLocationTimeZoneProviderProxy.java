package com.android.server.timezonedetector.location;

/* JADX INFO: loaded from: classes3.dex */
class RealLocationTimeZoneProviderProxy extends com.android.server.timezonedetector.location.LocationTimeZoneProviderProxy implements com.android.server.servicewatcher.ServiceWatcher.ServiceListener<com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo> {
    private com.android.server.timezonedetector.location.RealLocationTimeZoneProviderProxy.ManagerProxy mManagerProxy;
    private com.android.server.timezonedetector.location.TimeZoneProviderRequest mRequest;
    private final com.android.server.servicewatcher.ServiceWatcher mServiceWatcher;

    RealLocationTimeZoneProviderProxy(android.content.Context context, android.os.Handler handler, com.android.server.timezonedetector.location.ThreadingDomain threadingDomain, java.lang.String action, java.lang.String providerPackageName, boolean isTestProvider) {
        com.android.server.servicewatcher.CurrentUserServiceSupplier serviceSupplier;
        super(context, threadingDomain);
        this.mManagerProxy = null;
        this.mRequest = com.android.server.timezonedetector.location.TimeZoneProviderRequest.createStopUpdatesRequest();
        java.util.Objects.requireNonNull(providerPackageName);
        if (!isTestProvider) {
            serviceSupplier = com.android.server.servicewatcher.CurrentUserServiceSupplier.create(context, action, providerPackageName, "android.permission.BIND_TIME_ZONE_PROVIDER_SERVICE", "android.permission.INSTALL_LOCATION_TIME_ZONE_PROVIDER_SERVICE");
        } else {
            serviceSupplier = com.android.server.servicewatcher.CurrentUserServiceSupplier.createUnsafeForTestsOnly(context, action, providerPackageName, "android.permission.BIND_TIME_ZONE_PROVIDER_SERVICE", null);
        }
        this.mServiceWatcher = com.android.server.servicewatcher.ServiceWatcher.create(context, handler, "RealLocationTimeZoneProviderProxy", serviceSupplier, this);
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProviderProxy
    void onInitialize() {
        if (!register()) {
            throw new java.lang.IllegalStateException("Unable to register binder proxy");
        }
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProviderProxy
    void onDestroy() {
        this.mServiceWatcher.unregister();
    }

    private boolean register() {
        boolean resolves = this.mServiceWatcher.checkServiceResolves();
        if (resolves) {
            this.mServiceWatcher.register();
        }
        return resolves;
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceListener
    public void onBind(android.os.IBinder binder, com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo boundService) {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            this.mManagerProxy = new com.android.server.timezonedetector.location.RealLocationTimeZoneProviderProxy.ManagerProxy();
            this.mListener.onProviderBound();
            trySendCurrentRequest();
        }
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceListener
    public void onUnbind() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            this.mManagerProxy = null;
            this.mListener.onProviderUnbound();
        }
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProviderProxy
    final void setRequest(com.android.server.timezonedetector.location.TimeZoneProviderRequest request) {
        this.mThreadingDomain.assertCurrentThread();
        java.util.Objects.requireNonNull(request);
        synchronized (this.mSharedLock) {
            this.mRequest = request;
            trySendCurrentRequest();
        }
    }

    private void trySendCurrentRequest() {
        final com.android.server.timezonedetector.location.RealLocationTimeZoneProviderProxy.ManagerProxy managerProxy = this.mManagerProxy;
        final com.android.server.timezonedetector.location.TimeZoneProviderRequest request = this.mRequest;
        this.mServiceWatcher.runOnBinder(new com.android.server.servicewatcher.ServiceWatcher.BinderOperation() { // from class: com.android.server.timezonedetector.location.RealLocationTimeZoneProviderProxy$$ExternalSyntheticLambda0
            @Override // com.android.server.servicewatcher.ServiceWatcher.BinderOperation
            public final void run(android.os.IBinder iBinder) throws android.os.RemoteException {
                com.android.server.timezonedetector.location.RealLocationTimeZoneProviderProxy.lambda$trySendCurrentRequest$0(request, managerProxy, iBinder);
            }
        });
    }

    static /* synthetic */ void lambda$trySendCurrentRequest$0(com.android.server.timezonedetector.location.TimeZoneProviderRequest request, com.android.server.timezonedetector.location.RealLocationTimeZoneProviderProxy.ManagerProxy managerProxy, android.os.IBinder binder) throws android.os.RemoteException {
        android.service.timezone.ITimeZoneProvider service = android.service.timezone.ITimeZoneProvider.Stub.asInterface(binder);
        if (request.sendUpdates()) {
            service.startUpdates(managerProxy, request.getInitializationTimeout().toMillis(), request.getEventFilteringAgeThreshold().toMillis());
        } else {
            service.stopUpdates();
        }
    }

    @Override // com.android.server.timezonedetector.Dumpable
    public void dump(android.util.IndentingPrintWriter ipw, java.lang.String[] args) {
        synchronized (this.mSharedLock) {
            ipw.println("{RealLocationTimeZoneProviderProxy}");
            ipw.println("mRequest=" + this.mRequest);
            this.mServiceWatcher.dump(ipw);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ManagerProxy extends android.service.timezone.ITimeZoneProviderManager.Stub {
        private ManagerProxy() {
        }

        public void onTimeZoneProviderEvent(android.service.timezone.TimeZoneProviderEvent event) {
            synchronized (com.android.server.timezonedetector.location.RealLocationTimeZoneProviderProxy.this.mSharedLock) {
                if (com.android.server.timezonedetector.location.RealLocationTimeZoneProviderProxy.this.mManagerProxy != this) {
                    return;
                }
                com.android.server.timezonedetector.location.RealLocationTimeZoneProviderProxy.this.handleTimeZoneProviderEvent(event);
            }
        }
    }
}
