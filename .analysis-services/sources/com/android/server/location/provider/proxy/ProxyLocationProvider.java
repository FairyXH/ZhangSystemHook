package com.android.server.location.provider.proxy;

/* JADX INFO: loaded from: classes2.dex */
public class ProxyLocationProvider extends com.android.server.location.provider.AbstractLocationProvider implements com.android.server.servicewatcher.ServiceWatcher.ServiceListener<com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo> {
    private static final java.lang.String EXTRA_LOCATION_TAGS = "android:location_allow_listed_tags";
    private static final java.lang.String LOCATION_TAGS_SEPARATOR = ";";
    private static final long RESET_DELAY_MS = 10000;
    com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo mBoundServiceInfo;
    final android.content.Context mContext;
    final java.util.ArrayList<java.lang.Runnable> mFlushListeners;
    final java.lang.Object mLock;
    final java.lang.String mName;
    com.android.server.location.provider.proxy.ProxyLocationProvider.Proxy mProxy;
    private volatile android.location.provider.ProviderRequest mRequest;
    java.lang.Runnable mResetter;
    final com.android.server.servicewatcher.ServiceWatcher mServiceWatcher;

    public static com.android.server.location.provider.proxy.ProxyLocationProvider create(android.content.Context context, java.lang.String provider, java.lang.String action, int enableOverlayResId, int nonOverlayPackageResId) {
        com.android.server.location.provider.proxy.ProxyLocationProvider proxy = new com.android.server.location.provider.proxy.ProxyLocationProvider(context, provider, action, enableOverlayResId, nonOverlayPackageResId);
        if (proxy.checkServiceResolves()) {
            return proxy;
        }
        return null;
    }

    private ProxyLocationProvider(android.content.Context context, java.lang.String provider, java.lang.String action, int enableOverlayResId, int nonOverlayPackageResId) {
        super(com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, null, null, java.util.Collections.emptySet());
        this.mLock = new java.lang.Object();
        this.mFlushListeners = new java.util.ArrayList<>(0);
        this.mContext = context;
        this.mServiceWatcher = com.android.server.servicewatcher.ServiceWatcher.create(context, ((com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, this.mContext)).getHandler(0), provider, com.android.server.servicewatcher.CurrentUserServiceSupplier.createFromConfig(context, action, enableOverlayResId, nonOverlayPackageResId), this);
        this.mName = provider;
        this.mProxy = null;
        this.mRequest = android.location.provider.ProviderRequest.EMPTY_REQUEST;
    }

    private boolean checkServiceResolves() {
        return this.mServiceWatcher.checkServiceResolves();
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceListener
    public void onBind(android.os.IBinder binder, com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo boundServiceInfo) throws android.os.RemoteException {
        android.location.provider.ILocationProvider provider = android.location.provider.ILocationProvider.Stub.asInterface(binder);
        synchronized (this.mLock) {
            this.mProxy = new com.android.server.location.provider.proxy.ProxyLocationProvider.Proxy();
            this.mBoundServiceInfo = boundServiceInfo;
            provider.setLocationProviderManager(this.mProxy);
            android.location.provider.ProviderRequest request = this.mRequest;
            if (!request.equals(android.location.provider.ProviderRequest.EMPTY_REQUEST)) {
                provider.setRequest(request);
            }
        }
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceListener
    public void onUnbind() {
        java.lang.Runnable[] flushListeners;
        synchronized (this.mLock) {
            this.mProxy = null;
            this.mBoundServiceInfo = null;
            if (this.mResetter == null) {
                this.mResetter = new com.android.server.location.provider.proxy.ProxyLocationProvider.AnonymousClass1();
                com.android.server.FgThread.getHandler().postDelayed(this.mResetter, 10000L);
            }
            flushListeners = (java.lang.Runnable[]) this.mFlushListeners.toArray(new java.lang.Runnable[0]);
            this.mFlushListeners.clear();
        }
        for (java.lang.Runnable runnable : flushListeners) {
            runnable.run();
        }
    }

    /* JADX INFO: renamed from: com.android.server.location.provider.proxy.ProxyLocationProvider$1, reason: invalid class name */
    class AnonymousClass1 implements java.lang.Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (com.android.server.location.provider.proxy.ProxyLocationProvider.this.mLock) {
                if (com.android.server.location.provider.proxy.ProxyLocationProvider.this.mResetter == this) {
                    com.android.server.location.provider.proxy.ProxyLocationProvider.this.setState(new java.util.function.UnaryOperator() { // from class: com.android.server.location.provider.proxy.ProxyLocationProvider$1$$ExternalSyntheticLambda0
                        @Override // java.util.function.Function
                        public final java.lang.Object apply(java.lang.Object obj) {
                            return com.android.server.location.provider.AbstractLocationProvider.State.EMPTY_STATE;
                        }
                    });
                }
            }
        }
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onStart() {
        this.mServiceWatcher.register();
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onStop() {
        this.mServiceWatcher.unregister();
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onSetRequest(final android.location.provider.ProviderRequest request) {
        this.mRequest = request;
        this.mServiceWatcher.runOnBinder(new com.android.server.servicewatcher.ServiceWatcher.BinderOperation() { // from class: com.android.server.location.provider.proxy.ProxyLocationProvider$$ExternalSyntheticLambda1
            @Override // com.android.server.servicewatcher.ServiceWatcher.BinderOperation
            public final void run(android.os.IBinder iBinder) throws android.os.RemoteException {
                com.android.server.location.provider.proxy.ProxyLocationProvider.lambda$onSetRequest$0(request, iBinder);
            }
        });
    }

    static /* synthetic */ void lambda$onSetRequest$0(android.location.provider.ProviderRequest request, android.os.IBinder binder) throws android.os.RemoteException {
        android.location.provider.ILocationProvider provider = android.location.provider.ILocationProvider.Stub.asInterface(binder);
        provider.setRequest(request);
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onFlush(final java.lang.Runnable callback) {
        this.mServiceWatcher.runOnBinder(new com.android.server.servicewatcher.ServiceWatcher.BinderOperation() { // from class: com.android.server.location.provider.proxy.ProxyLocationProvider.2
            @Override // com.android.server.servicewatcher.ServiceWatcher.BinderOperation
            public void run(android.os.IBinder binder) throws android.os.RemoteException {
                android.location.provider.ILocationProvider provider = android.location.provider.ILocationProvider.Stub.asInterface(binder);
                synchronized (com.android.server.location.provider.proxy.ProxyLocationProvider.this.mLock) {
                    com.android.server.location.provider.proxy.ProxyLocationProvider.this.mFlushListeners.add(callback);
                }
                provider.flush();
            }

            @Override // com.android.server.servicewatcher.ServiceWatcher.BinderOperation
            public void onError(java.lang.Throwable t) {
                synchronized (com.android.server.location.provider.proxy.ProxyLocationProvider.this.mLock) {
                    com.android.server.location.provider.proxy.ProxyLocationProvider.this.mFlushListeners.remove(callback);
                }
                callback.run();
            }
        });
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    public void onExtraCommand(int uid, int pid, final java.lang.String command, final android.os.Bundle extras) {
        this.mServiceWatcher.runOnBinder(new com.android.server.servicewatcher.ServiceWatcher.BinderOperation() { // from class: com.android.server.location.provider.proxy.ProxyLocationProvider$$ExternalSyntheticLambda0
            @Override // com.android.server.servicewatcher.ServiceWatcher.BinderOperation
            public final void run(android.os.IBinder iBinder) throws android.os.RemoteException {
                com.android.server.location.provider.proxy.ProxyLocationProvider.lambda$onExtraCommand$1(command, extras, iBinder);
            }
        });
    }

    static /* synthetic */ void lambda$onExtraCommand$1(java.lang.String command, android.os.Bundle extras, android.os.IBinder binder) throws android.os.RemoteException {
        android.location.provider.ILocationProvider provider = android.location.provider.ILocationProvider.Stub.asInterface(binder);
        provider.sendExtraCommand(command, extras);
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        this.mServiceWatcher.dump(pw);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class Proxy extends android.location.provider.ILocationProviderManager.Stub {
        Proxy() {
        }

        public void onInitialize(final boolean allowed, final android.location.provider.ProviderProperties properties, java.lang.String attributionTag) {
            synchronized (com.android.server.location.provider.proxy.ProxyLocationProvider.this.mLock) {
                if (com.android.server.location.provider.proxy.ProxyLocationProvider.this.mProxy != this) {
                    return;
                }
                if (com.android.server.location.provider.proxy.ProxyLocationProvider.this.mResetter != null) {
                    com.android.server.FgThread.getHandler().removeCallbacks(com.android.server.location.provider.proxy.ProxyLocationProvider.this.mResetter);
                    com.android.server.location.provider.proxy.ProxyLocationProvider.this.mResetter = null;
                }
                java.lang.String[] attributionTags = new java.lang.String[0];
                if (com.android.server.location.provider.proxy.ProxyLocationProvider.this.mBoundServiceInfo.getMetadata() != null) {
                    java.lang.String tagsStr = com.android.server.location.provider.proxy.ProxyLocationProvider.this.mBoundServiceInfo.getMetadata().getString(com.android.server.location.provider.proxy.ProxyLocationProvider.EXTRA_LOCATION_TAGS);
                    if (!android.text.TextUtils.isEmpty(tagsStr)) {
                        attributionTags = tagsStr.split(com.android.server.location.provider.proxy.ProxyLocationProvider.LOCATION_TAGS_SEPARATOR);
                        android.util.Log.i(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.proxy.ProxyLocationProvider.this.mName + " provider loaded extra attribution tags: " + java.util.Arrays.toString(attributionTags));
                    }
                }
                final android.util.ArraySet<java.lang.String> extraAttributionTags = new android.util.ArraySet<>(attributionTags);
                final android.location.util.identity.CallerIdentity identity = android.location.util.identity.CallerIdentity.fromBinderUnsafe(com.android.server.location.provider.proxy.ProxyLocationProvider.this.mBoundServiceInfo.getComponentName().getPackageName(), attributionTag);
                com.android.server.location.provider.proxy.ProxyLocationProvider.this.setState(new java.util.function.UnaryOperator() { // from class: com.android.server.location.provider.proxy.ProxyLocationProvider$Proxy$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        boolean z = allowed;
                        android.location.provider.ProviderProperties providerProperties = properties;
                        return com.android.server.location.provider.AbstractLocationProvider.State.EMPTY_STATE.withAllowed(z).withProperties(providerProperties).withIdentity(identity).withExtraAttributionTags(extraAttributionTags);
                    }
                });
            }
        }

        public void onSetProperties(android.location.provider.ProviderProperties properties) {
            synchronized (com.android.server.location.provider.proxy.ProxyLocationProvider.this.mLock) {
                if (com.android.server.location.provider.proxy.ProxyLocationProvider.this.mProxy != this) {
                    return;
                }
                com.android.server.location.provider.proxy.ProxyLocationProvider.this.setProperties(properties);
            }
        }

        public void onSetAllowed(boolean allowed) {
            synchronized (com.android.server.location.provider.proxy.ProxyLocationProvider.this.mLock) {
                if (com.android.server.location.provider.proxy.ProxyLocationProvider.this.mProxy != this) {
                    return;
                }
                com.android.server.location.provider.proxy.ProxyLocationProvider.this.setAllowed(allowed);
            }
        }

        public void onReportLocation(android.location.Location location) {
            synchronized (com.android.server.location.provider.proxy.ProxyLocationProvider.this.mLock) {
                if (com.android.server.location.provider.proxy.ProxyLocationProvider.this.mProxy != this) {
                    return;
                }
                com.android.server.location.provider.proxy.ProxyLocationProvider.this.reportLocation(android.location.LocationResult.wrap(new android.location.Location[]{location}));
            }
        }

        public void onReportLocations(java.util.List<android.location.Location> locations) {
            synchronized (com.android.server.location.provider.proxy.ProxyLocationProvider.this.mLock) {
                if (com.android.server.location.provider.proxy.ProxyLocationProvider.this.mProxy != this) {
                    return;
                }
                com.android.server.location.provider.proxy.ProxyLocationProvider.this.reportLocation(android.location.LocationResult.wrap(locations));
            }
        }

        public void onFlushComplete() {
            java.lang.Runnable callback = null;
            synchronized (com.android.server.location.provider.proxy.ProxyLocationProvider.this.mLock) {
                if (com.android.server.location.provider.proxy.ProxyLocationProvider.this.mProxy != this) {
                    return;
                }
                if (!com.android.server.location.provider.proxy.ProxyLocationProvider.this.mFlushListeners.isEmpty()) {
                    callback = com.android.server.location.provider.proxy.ProxyLocationProvider.this.mFlushListeners.remove(0);
                }
                if (callback != null) {
                    callback.run();
                }
            }
        }
    }
}
