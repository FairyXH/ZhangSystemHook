package com.android.server;

/* JADX INFO: loaded from: classes.dex */
class ExplicitHealthCheckController {
    private static final java.lang.String TAG = "ExplicitHealthCheckController";
    private android.content.ServiceConnection mConnection;
    private final android.content.Context mContext;
    private boolean mEnabled;
    private final java.lang.Object mLock = new java.lang.Object();
    private java.lang.Runnable mNotifySyncRunnable;
    private java.util.function.Consumer<java.lang.String> mPassedConsumer;
    private android.service.watchdog.IExplicitHealthCheckService mRemoteService;
    private java.util.function.Consumer<java.util.List<android.service.watchdog.ExplicitHealthCheckService.PackageConfig>> mSupportedConsumer;

    ExplicitHealthCheckController(android.content.Context context) {
        this.mContext = context;
    }

    public void setEnabled(boolean enabled) {
        synchronized (this.mLock) {
            android.util.Slog.i(TAG, "Explicit health checks " + (enabled ? "enabled." : "disabled."));
            this.mEnabled = enabled;
        }
    }

    public void setCallbacks(java.util.function.Consumer<java.lang.String> passedConsumer, java.util.function.Consumer<java.util.List<android.service.watchdog.ExplicitHealthCheckService.PackageConfig>> supportedConsumer, java.lang.Runnable notifySyncRunnable) {
        synchronized (this.mLock) {
            if (this.mPassedConsumer != null || this.mSupportedConsumer != null || this.mNotifySyncRunnable != null) {
                android.util.Slog.wtf(TAG, "Resetting health check controller callbacks");
            }
            this.mPassedConsumer = (java.util.function.Consumer) java.util.Objects.requireNonNull(passedConsumer);
            this.mSupportedConsumer = (java.util.function.Consumer) java.util.Objects.requireNonNull(supportedConsumer);
            this.mNotifySyncRunnable = (java.lang.Runnable) java.util.Objects.requireNonNull(notifySyncRunnable);
        }
    }

    public void syncRequests(final java.util.Set<java.lang.String> newRequestedPackages) {
        boolean enabled;
        synchronized (this.mLock) {
            enabled = this.mEnabled;
        }
        if (!enabled) {
            android.util.Slog.i(TAG, "Health checks disabled, no supported packages");
            this.mSupportedConsumer.accept(java.util.Collections.emptyList());
        } else {
            getSupportedPackages(new java.util.function.Consumer() { // from class: com.android.server.ExplicitHealthCheckController$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$syncRequests$3(newRequestedPackages, (java.util.List) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$syncRequests$3(final java.util.Set newRequestedPackages, final java.util.List supportedPackageConfigs) {
        this.mSupportedConsumer.accept(supportedPackageConfigs);
        getRequestedPackages(new java.util.function.Consumer() { // from class: com.android.server.ExplicitHealthCheckController$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$syncRequests$2(supportedPackageConfigs, newRequestedPackages, (java.util.List) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$syncRequests$2(java.util.List supportedPackageConfigs, java.util.Set newRequestedPackages, java.util.List previousRequestedPackages) {
        synchronized (this.mLock) {
            java.util.Set<java.lang.String> supportedPackages = new android.util.ArraySet<>();
            java.util.Iterator it = supportedPackageConfigs.iterator();
            while (it.hasNext()) {
                android.service.watchdog.ExplicitHealthCheckService.PackageConfig config = (android.service.watchdog.ExplicitHealthCheckService.PackageConfig) it.next();
                supportedPackages.add(config.getPackageName());
            }
            newRequestedPackages.retainAll(supportedPackages);
            actOnDifference(previousRequestedPackages, newRequestedPackages, new java.util.function.Consumer() { // from class: com.android.server.ExplicitHealthCheckController$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$syncRequests$0((java.lang.String) obj);
                }
            });
            actOnDifference(newRequestedPackages, previousRequestedPackages, new java.util.function.Consumer() { // from class: com.android.server.ExplicitHealthCheckController$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$syncRequests$1((java.lang.String) obj);
                }
            });
            if (newRequestedPackages.isEmpty()) {
                android.util.Slog.i(TAG, "No more health check requests, unbinding...");
                unbindService();
            }
        }
    }

    private void actOnDifference(java.util.Collection<java.lang.String> collection1, java.util.Collection<java.lang.String> collection2, java.util.function.Consumer<java.lang.String> action) {
        for (java.lang.String packageName : collection1) {
            if (!collection2.contains(packageName)) {
                action.accept(packageName);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: request, reason: merged with bridge method [inline-methods] */
    public void lambda$syncRequests$1(java.lang.String packageName) {
        synchronized (this.mLock) {
            if (prepareServiceLocked("request health check for " + packageName)) {
                android.util.Slog.i(TAG, "Requesting health check for package " + packageName);
                try {
                    this.mRemoteService.request(packageName);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Failed to request health check for package " + packageName, e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: cancel, reason: merged with bridge method [inline-methods] */
    public void lambda$syncRequests$0(java.lang.String packageName) {
        synchronized (this.mLock) {
            if (prepareServiceLocked("cancel health check for " + packageName)) {
                android.util.Slog.i(TAG, "Cancelling health check for package " + packageName);
                try {
                    this.mRemoteService.cancel(packageName);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Failed to cancel health check for package " + packageName, e);
                }
            }
        }
    }

    private void getSupportedPackages(final java.util.function.Consumer<java.util.List<android.service.watchdog.ExplicitHealthCheckService.PackageConfig>> consumer) {
        synchronized (this.mLock) {
            if (prepareServiceLocked("get health check supported packages")) {
                android.util.Slog.d(TAG, "Getting health check supported packages");
                try {
                    this.mRemoteService.getSupportedPackages(new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.ExplicitHealthCheckController$$ExternalSyntheticLambda5
                        public final void onResult(android.os.Bundle bundle) {
                            com.android.server.ExplicitHealthCheckController.lambda$getSupportedPackages$4(consumer, bundle);
                        }
                    }));
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Failed to get health check supported packages", e);
                }
            }
        }
    }

    static /* synthetic */ void lambda$getSupportedPackages$4(java.util.function.Consumer consumer, android.os.Bundle result) {
        java.util.ArrayList parcelableArrayList = result.getParcelableArrayList("android.service.watchdog.extra.supported_packages", android.service.watchdog.ExplicitHealthCheckService.PackageConfig.class);
        android.util.Slog.i(TAG, "Explicit health check supported packages " + parcelableArrayList);
        consumer.accept(parcelableArrayList);
    }

    private void getRequestedPackages(final java.util.function.Consumer<java.util.List<java.lang.String>> consumer) {
        synchronized (this.mLock) {
            if (prepareServiceLocked("get health check requested packages")) {
                android.util.Slog.d(TAG, "Getting health check requested packages");
                try {
                    this.mRemoteService.getRequestedPackages(new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.ExplicitHealthCheckController$$ExternalSyntheticLambda3
                        public final void onResult(android.os.Bundle bundle) {
                            com.android.server.ExplicitHealthCheckController.lambda$getRequestedPackages$5(consumer, bundle);
                        }
                    }));
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Failed to get health check requested packages", e);
                }
            }
        }
    }

    static /* synthetic */ void lambda$getRequestedPackages$5(java.util.function.Consumer consumer, android.os.Bundle result) {
        java.util.ArrayList<java.lang.String> stringArrayList = result.getStringArrayList("android.service.watchdog.extra.requested_packages");
        android.util.Slog.i(TAG, "Explicit health check requested packages " + stringArrayList);
        consumer.accept(stringArrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindService() {
        synchronized (this.mLock) {
            if (this.mEnabled && this.mConnection == null && this.mRemoteService == null) {
                android.content.ComponentName component = getServiceComponentNameLocked();
                if (component == null) {
                    android.util.Slog.wtf(TAG, "Explicit health check service not found");
                    return;
                }
                android.content.Intent intent = new android.content.Intent();
                intent.setComponent(component);
                this.mConnection = new android.content.ServiceConnection() { // from class: com.android.server.ExplicitHealthCheckController.1
                    @Override // android.content.ServiceConnection
                    public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
                        android.util.Slog.i(com.android.server.ExplicitHealthCheckController.TAG, "Explicit health check service is connected " + name);
                        com.android.server.ExplicitHealthCheckController.this.initState(service);
                    }

                    @Override // android.content.ServiceConnection
                    public void onServiceDisconnected(android.content.ComponentName name) {
                        android.util.Slog.i(com.android.server.ExplicitHealthCheckController.TAG, "Explicit health check service is disconnected " + name);
                        synchronized (com.android.server.ExplicitHealthCheckController.this.mLock) {
                            com.android.server.ExplicitHealthCheckController.this.mRemoteService = null;
                        }
                    }

                    @Override // android.content.ServiceConnection
                    public void onBindingDied(android.content.ComponentName name) {
                        android.util.Slog.i(com.android.server.ExplicitHealthCheckController.TAG, "Explicit health check service binding is dead. Rebind: " + name);
                        com.android.server.ExplicitHealthCheckController.this.unbindService();
                        com.android.server.ExplicitHealthCheckController.this.bindService();
                    }

                    @Override // android.content.ServiceConnection
                    public void onNullBinding(android.content.ComponentName name) {
                        android.util.Slog.wtf(com.android.server.ExplicitHealthCheckController.TAG, "Explicit health check service binding is null?? " + name);
                    }
                };
                this.mContext.bindServiceAsUser(intent, this.mConnection, 1, android.os.UserHandle.SYSTEM);
                android.util.Slog.i(TAG, "Explicit health check service is bound");
                return;
            }
            if (!this.mEnabled) {
                android.util.Slog.i(TAG, "Not binding to service, service disabled");
            } else if (this.mRemoteService != null) {
                android.util.Slog.i(TAG, "Not binding to service, service already connected");
            } else {
                android.util.Slog.i(TAG, "Not binding to service, service already connecting");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindService() {
        synchronized (this.mLock) {
            if (this.mRemoteService != null) {
                this.mContext.unbindService(this.mConnection);
                this.mRemoteService = null;
                this.mConnection = null;
            }
            android.util.Slog.i(TAG, "Explicit health check service is unbound");
        }
    }

    private android.content.pm.ServiceInfo getServiceInfoLocked() {
        java.lang.String packageName = this.mContext.getPackageManager().getServicesSystemSharedLibraryPackageName();
        if (packageName == null) {
            android.util.Slog.w(TAG, "no external services package!");
            return null;
        }
        android.content.Intent intent = new android.content.Intent("android.service.watchdog.ExplicitHealthCheckService");
        intent.setPackage(packageName);
        android.content.pm.ResolveInfo resolveInfo = this.mContext.getPackageManager().resolveService(intent, 132);
        if (resolveInfo == null || resolveInfo.serviceInfo == null) {
            android.util.Slog.w(TAG, "No valid components found.");
            return null;
        }
        return resolveInfo.serviceInfo;
    }

    private android.content.ComponentName getServiceComponentNameLocked() {
        android.content.pm.ServiceInfo serviceInfo = getServiceInfoLocked();
        if (serviceInfo == null) {
            return null;
        }
        android.content.ComponentName name = new android.content.ComponentName(serviceInfo.packageName, serviceInfo.name);
        if (!"android.permission.BIND_EXPLICIT_HEALTH_CHECK_SERVICE".equals(serviceInfo.permission)) {
            android.util.Slog.w(TAG, name.flattenToShortString() + " does not require permission android.permission.BIND_EXPLICIT_HEALTH_CHECK_SERVICE");
            return null;
        }
        return name;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initState(android.os.IBinder service) {
        synchronized (this.mLock) {
            if (!this.mEnabled) {
                android.util.Slog.w(TAG, "Attempting to connect disabled service?? Unbinding...");
                unbindService();
                return;
            }
            this.mRemoteService = android.service.watchdog.IExplicitHealthCheckService.Stub.asInterface(service);
            try {
                this.mRemoteService.setCallback(new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.ExplicitHealthCheckController$$ExternalSyntheticLambda6
                    public final void onResult(android.os.Bundle bundle) {
                        this.f$0.lambda$initState$6(bundle);
                    }
                }));
                android.util.Slog.i(TAG, "Service initialized, syncing requests");
            } catch (android.os.RemoteException e) {
                android.util.Slog.wtf(TAG, "Could not setCallback on explicit health check service");
            }
            this.mNotifySyncRunnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initState$6(android.os.Bundle result) {
        java.lang.String packageName = result.getString("android.service.watchdog.extra.health_check_passed_package");
        if (!android.text.TextUtils.isEmpty(packageName)) {
            if (this.mPassedConsumer == null) {
                android.util.Slog.wtf(TAG, "Health check passed for package " + packageName + "but no consumer registered.");
                return;
            } else {
                this.mPassedConsumer.accept(packageName);
                return;
            }
        }
        android.util.Slog.wtf(TAG, "Empty package passed explicit health check?");
    }

    private boolean prepareServiceLocked(java.lang.String action) {
        if (this.mRemoteService != null && this.mEnabled) {
            return true;
        }
        android.util.Slog.i(TAG, "Service not ready to " + action + (this.mEnabled ? ". Binding..." : ". Disabled"));
        if (this.mEnabled) {
            bindService();
            return false;
        }
        return false;
    }
}
