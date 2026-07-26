package com.android.server.wearable;

/* JADX INFO: loaded from: classes3.dex */
public class WearableSensingManagerService extends com.android.server.infra.AbstractMasterSystemService<com.android.server.wearable.WearableSensingManagerService, com.android.server.wearable.WearableSensingManagerPerUserService> {
    private static final boolean DEFAULT_SERVICE_ENABLED = true;
    private static final java.lang.String KEY_SERVICE_ENABLED = "service_enabled";
    public static final int MAX_TEMPORARY_SERVICE_DURATION_MS = 30000;
    private static final java.lang.String RATE_LIMITER_PACKAGE_NAME = "android";
    private final android.content.Context mContext;
    private final java.util.Set<com.android.server.wearable.WearableSensingManagerService.DataRequestObserverContext> mDataRequestObserverContexts;
    private volatile com.android.server.utils.quota.MultiRateLimiter mDataRequestRateLimiter;
    volatile boolean mIsServiceEnabled;
    private final java.util.concurrent.atomic.AtomicInteger mNextDataRequestObserverId;
    private static final java.lang.String TAG = com.android.server.wearable.WearableSensingManagerService.class.getSimpleName();
    private static final java.lang.String RATE_LIMITER_TAG = com.android.server.wearable.WearableSensingManagerService.class.getSimpleName();

    private static final class DataRequestObserverContext {
        final int mDataRequestObserverId;
        final android.app.PendingIntent mDataRequestPendingIntent;
        final android.os.RemoteCallback mDataRequestRemoteCallback;
        final int mDataType;
        final int mUserId;

        DataRequestObserverContext(int dataType, int userId, int dataRequestObserverId, android.app.PendingIntent dataRequestPendingIntent, android.os.RemoteCallback dataRequestRemoteCallback) {
            this.mDataType = dataType;
            this.mUserId = userId;
            this.mDataRequestObserverId = dataRequestObserverId;
            this.mDataRequestPendingIntent = dataRequestPendingIntent;
            this.mDataRequestRemoteCallback = dataRequestRemoteCallback;
        }
    }

    public WearableSensingManagerService(android.content.Context context) {
        super(context, new com.android.server.infra.FrameworkResourcesServiceNameResolver(context, android.R.string.config_displayWhiteBalanceColorTemperatureSensorName), null, 68);
        this.mNextDataRequestObserverId = new java.util.concurrent.atomic.AtomicInteger(1);
        this.mDataRequestObserverContexts = new java.util.HashSet();
        this.mContext = context;
        this.mDataRequestRateLimiter = new com.android.server.utils.quota.MultiRateLimiter.Builder(context).addRateLimit(android.app.wearable.WearableSensingDataRequest.getRateLimit(), android.app.wearable.WearableSensingDataRequest.getRateLimitWindowSize()).build();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("wearable_sensing", new com.android.server.wearable.WearableSensingManagerService.WearableSensingManagerInternal());
    }

    @Override // com.android.server.infra.AbstractMasterSystemService, com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            android.provider.DeviceConfig.addOnPropertiesChangedListener("wearable_sensing", getContext().getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.wearable.WearableSensingManagerService$$ExternalSyntheticLambda1
                public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                    this.f$0.lambda$onBootPhase$0(properties);
                }
            });
            this.mIsServiceEnabled = android.provider.DeviceConfig.getBoolean("wearable_sensing", KEY_SERVICE_ENABLED, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$0(android.provider.DeviceConfig.Properties properties) {
        onDeviceConfigChange(properties.getKeyset());
    }

    private void onDeviceConfigChange(java.util.Set<java.lang.String> keys) {
        if (keys.contains(KEY_SERVICE_ENABLED)) {
            this.mIsServiceEnabled = android.provider.DeviceConfig.getBoolean("wearable_sensing", KEY_SERVICE_ENABLED, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public com.android.server.wearable.WearableSensingManagerPerUserService newServiceLocked(int resolvedUserId, boolean disabled) {
        return new com.android.server.wearable.WearableSensingManagerPerUserService(this, this.mLock, resolvedUserId);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public void onServiceRemoved(com.android.server.wearable.WearableSensingManagerPerUserService service, int userId) {
        android.util.Slog.d(TAG, "onServiceRemoved");
        service.destroyLocked();
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServicePackageRestartedLocked(int userId) {
        android.util.Slog.d(TAG, "onServicePackageRestartedLocked.");
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServicePackageUpdatedLocked(int userId) {
        android.util.Slog.d(TAG, "onServicePackageUpdatedLocked.");
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void enforceCallingPermissionForManagement() {
        getContext().enforceCallingPermission("android.permission.ACCESS_AMBIENT_CONTEXT_EVENT", TAG);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected int getMaximumTemporaryServiceDurationMs() {
        if (android.os.Build.isDebuggable()) {
            return Integer.MAX_VALUE;
        }
        return 30000;
    }

    public android.content.ComponentName getComponentName(int userId) {
        synchronized (this.mLock) {
            com.android.server.wearable.WearableSensingManagerPerUserService service = getServiceForUserLocked(userId);
            if (service != null) {
                return service.getComponentName();
            }
            return null;
        }
    }

    void provideDataStream(int userId, android.os.ParcelFileDescriptor parcelFileDescriptor, android.os.RemoteCallback callback) {
        synchronized (this.mLock) {
            com.android.server.wearable.WearableSensingManagerPerUserService mService = getServiceForUserLocked(userId);
            if (mService != null) {
                mService.onProvideDataStream(parcelFileDescriptor, null, callback);
            } else {
                android.util.Slog.w(TAG, "Service not available.");
            }
        }
    }

    void provideData(int userId, android.os.PersistableBundle data, android.os.SharedMemory sharedMemory, android.os.RemoteCallback callback) {
        synchronized (this.mLock) {
            com.android.server.wearable.WearableSensingManagerPerUserService mService = getServiceForUserLocked(userId);
            if (mService != null) {
                mService.onProvidedData(data, sharedMemory, callback);
            } else {
                android.util.Slog.w(TAG, "Service not available.");
            }
        }
    }

    void setDataRequestRateLimitWindowSize(java.time.Duration windowSize) {
        android.util.Slog.w(TAG, android.text.TextUtils.formatSimple("Setting the data request rate limit window size to %s. This also resets the current limit and should only be callable from a test.", new java.lang.Object[]{windowSize}));
        this.mDataRequestRateLimiter = new com.android.server.utils.quota.MultiRateLimiter.Builder(this.mContext).addRateLimit(android.app.wearable.WearableSensingDataRequest.getRateLimit(), windowSize).build();
    }

    void resetDataRequestRateLimitWindowSize() {
        android.util.Slog.w(TAG, "Resetting the data request rate limit window size back to the default value. This also resets the current limit and should only be callable from a test.");
        this.mDataRequestRateLimiter = new com.android.server.utils.quota.MultiRateLimiter.Builder(this.mContext).addRateLimit(android.app.wearable.WearableSensingDataRequest.getRateLimit(), android.app.wearable.WearableSensingDataRequest.getRateLimitWindowSize()).build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.wearable.WearableSensingManagerService.DataRequestObserverContext getDataRequestObserverContext(int dataType, int userId, android.app.PendingIntent dataRequestPendingIntent) {
        synchronized (this.mDataRequestObserverContexts) {
            for (com.android.server.wearable.WearableSensingManagerService.DataRequestObserverContext observerContext : this.mDataRequestObserverContexts) {
                if (observerContext.mDataType == dataType && observerContext.mUserId == userId && observerContext.mDataRequestPendingIntent.equals(dataRequestPendingIntent)) {
                    return observerContext;
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.os.RemoteCallback createDataRequestRemoteCallback(final android.app.PendingIntent dataRequestPendingIntent, final int userId) {
        return new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.wearable.WearableSensingManagerService$$ExternalSyntheticLambda0
            public final void onResult(android.os.Bundle bundle) throws java.lang.Throwable {
                this.f$0.lambda$createDataRequestRemoteCallback$1(userId, dataRequestPendingIntent, bundle);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createDataRequestRemoteCallback$1(int userId, android.app.PendingIntent dataRequestPendingIntent, android.os.Bundle bundle) throws java.lang.Throwable {
        android.app.wearable.WearableSensingDataRequest dataRequest = (android.app.wearable.WearableSensingDataRequest) bundle.getParcelable("android.app.wearable.WearableSensingDataRequestBundleKey", android.app.wearable.WearableSensingDataRequest.class);
        if (dataRequest == null) {
            android.util.Slog.e(TAG, "Received data request callback without a request.");
            return;
        }
        android.os.RemoteCallback dataRequestStatusCallback = (android.os.RemoteCallback) bundle.getParcelable("android.app.wearable.WearableSensingDataRequestStatusCallbackBundleKey", android.os.RemoteCallback.class);
        if (dataRequestStatusCallback == null) {
            android.util.Slog.e(TAG, "Received data request callback without a status callback.");
            return;
        }
        if (dataRequest.getDataSize() > android.app.wearable.WearableSensingDataRequest.getMaxRequestSize()) {
            android.util.Slog.w(TAG, android.text.TextUtils.formatSimple("WearableSensingDataRequest size exceeds the maximum allowed size of %s bytes. Dropping the request.", new java.lang.Object[]{java.lang.Integer.valueOf(android.app.wearable.WearableSensingDataRequest.getMaxRequestSize())}));
            com.android.server.wearable.WearableSensingManagerPerUserService.notifyStatusCallback(dataRequestStatusCallback, 3);
            return;
        }
        if (!this.mDataRequestRateLimiter.isWithinQuota(userId, "android", RATE_LIMITER_TAG)) {
            android.util.Slog.w(TAG, "Data request exceeded rate limit. Dropping the request.");
            com.android.server.wearable.WearableSensingManagerPerUserService.notifyStatusCallback(dataRequestStatusCallback, 4);
            return;
        }
        android.content.Intent intent = new android.content.Intent();
        intent.putExtra("android.app.wearable.extra.WEARABLE_SENSING_DATA_REQUEST", (android.os.Parcelable) dataRequest);
        android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
        options.setPendingIntentBackgroundActivityStartMode(2);
        this.mDataRequestRateLimiter.noteEvent(userId, "android", RATE_LIMITER_TAG);
        long previousCallingIdentity = android.os.Binder.clearCallingIdentity();
        try {
            try {
                try {
                    dataRequestPendingIntent.send(getContext(), 0, intent, null, null, null, options.toBundle());
                    com.android.server.wearable.WearableSensingManagerPerUserService.notifyStatusCallback(dataRequestStatusCallback, 1);
                    android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("Sending data request to %s: %s", new java.lang.Object[]{dataRequestPendingIntent.getCreatorPackage(), dataRequest.toExpandedString()}));
                    android.os.Binder.restoreCallingIdentity(previousCallingIdentity);
                } catch (android.app.PendingIntent.CanceledException e) {
                    try {
                        android.util.Slog.w(TAG, "Could not deliver pendingIntent: " + dataRequestPendingIntent);
                        com.android.server.wearable.WearableSensingManagerPerUserService.notifyStatusCallback(dataRequestStatusCallback, 2);
                        android.os.Binder.restoreCallingIdentity(previousCallingIdentity);
                    } catch (java.lang.Throwable th) {
                        th = th;
                        android.os.Binder.restoreCallingIdentity(previousCallingIdentity);
                        throw th;
                    }
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                android.os.Binder.restoreCallingIdentity(previousCallingIdentity);
                throw th;
            }
        } catch (android.app.PendingIntent.CanceledException e2) {
        } catch (java.lang.Throwable th3) {
            th = th3;
            android.os.Binder.restoreCallingIdentity(previousCallingIdentity);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callPerUserServiceIfExist(java.util.function.Consumer<com.android.server.wearable.WearableSensingManagerPerUserService> serviceConsumer, android.os.RemoteCallback statusCallback) {
        int userId = android.os.UserHandle.getCallingUserId();
        synchronized (this.mLock) {
            com.android.server.wearable.WearableSensingManagerPerUserService service = getServiceForUserLocked(userId);
            if (service == null) {
                android.util.Slog.w(TAG, "Service not available for userId " + userId);
                com.android.server.wearable.WearableSensingManagerPerUserService.notifyStatusCallback(statusCallback, 3);
            } else {
                serviceConsumer.accept(service);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class WearableSensingManagerInternal extends android.app.wearable.IWearableSensingManager.Stub {
        private WearableSensingManagerInternal() {
        }

        public void provideConnection(final android.os.ParcelFileDescriptor wearableConnection, final android.app.wearable.IWearableSensingCallback wearableSensingCallback, final android.os.RemoteCallback statusCallback) {
            android.util.Slog.i(com.android.server.wearable.WearableSensingManagerService.TAG, "WearableSensingManagerInternal provideConnection.");
            java.util.Objects.requireNonNull(wearableConnection);
            java.util.Objects.requireNonNull(statusCallback);
            com.android.server.wearable.WearableSensingManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_WEARABLE_SENSING_SERVICE", com.android.server.wearable.WearableSensingManagerService.TAG);
            if (!com.android.server.wearable.WearableSensingManagerService.this.mIsServiceEnabled) {
                android.util.Slog.w(com.android.server.wearable.WearableSensingManagerService.TAG, "Service not available.");
                com.android.server.wearable.WearableSensingManagerPerUserService.notifyStatusCallback(statusCallback, 3);
            } else {
                com.android.server.wearable.WearableSensingManagerService.this.callPerUserServiceIfExist(new java.util.function.Consumer() { // from class: com.android.server.wearable.WearableSensingManagerService$WearableSensingManagerInternal$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.wearable.WearableSensingManagerPerUserService) obj).onProvideConnection(wearableConnection, wearableSensingCallback, statusCallback);
                    }
                }, statusCallback);
            }
        }

        public void provideDataStream(final android.os.ParcelFileDescriptor parcelFileDescriptor, final android.app.wearable.IWearableSensingCallback wearableSensingCallback, final android.os.RemoteCallback statusCallback) {
            android.util.Slog.i(com.android.server.wearable.WearableSensingManagerService.TAG, "WearableSensingManagerInternal provideDataStream.");
            java.util.Objects.requireNonNull(parcelFileDescriptor);
            java.util.Objects.requireNonNull(statusCallback);
            com.android.server.wearable.WearableSensingManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_WEARABLE_SENSING_SERVICE", com.android.server.wearable.WearableSensingManagerService.TAG);
            if (!com.android.server.wearable.WearableSensingManagerService.this.mIsServiceEnabled) {
                android.util.Slog.w(com.android.server.wearable.WearableSensingManagerService.TAG, "Service not available.");
                com.android.server.wearable.WearableSensingManagerPerUserService.notifyStatusCallback(statusCallback, 3);
            } else {
                com.android.server.wearable.WearableSensingManagerService.this.callPerUserServiceIfExist(new java.util.function.Consumer() { // from class: com.android.server.wearable.WearableSensingManagerService$WearableSensingManagerInternal$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.wearable.WearableSensingManagerPerUserService) obj).onProvideDataStream(parcelFileDescriptor, wearableSensingCallback, statusCallback);
                    }
                }, statusCallback);
            }
        }

        public void provideData(final android.os.PersistableBundle data, final android.os.SharedMemory sharedMemory, final android.os.RemoteCallback callback) {
            android.util.Slog.d(com.android.server.wearable.WearableSensingManagerService.TAG, "WearableSensingManagerInternal provideData.");
            java.util.Objects.requireNonNull(data);
            java.util.Objects.requireNonNull(callback);
            com.android.server.wearable.WearableSensingManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_WEARABLE_SENSING_SERVICE", com.android.server.wearable.WearableSensingManagerService.TAG);
            if (!com.android.server.wearable.WearableSensingManagerService.this.mIsServiceEnabled) {
                android.util.Slog.w(com.android.server.wearable.WearableSensingManagerService.TAG, "Service not available.");
                com.android.server.wearable.WearableSensingManagerPerUserService.notifyStatusCallback(callback, 3);
            } else {
                com.android.server.wearable.WearableSensingManagerService.this.callPerUserServiceIfExist(new java.util.function.Consumer() { // from class: com.android.server.wearable.WearableSensingManagerService$WearableSensingManagerInternal$$ExternalSyntheticLambda5
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.wearable.WearableSensingManagerPerUserService) obj).onProvidedData(data, sharedMemory, callback);
                    }
                }, callback);
            }
        }

        public void registerDataRequestObserver(final int dataType, final android.app.PendingIntent dataRequestPendingIntent, final android.os.RemoteCallback statusCallback) throws java.lang.Throwable {
            int dataRequestObserverId;
            android.os.RemoteCallback dataRequestCallback;
            android.util.Slog.i(com.android.server.wearable.WearableSensingManagerService.TAG, "WearableSensingManagerInternal registerDataRequestObserver.");
            java.util.Objects.requireNonNull(dataRequestPendingIntent);
            com.android.server.wearable.WearableSensingManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_WEARABLE_SENSING_SERVICE", com.android.server.wearable.WearableSensingManagerService.TAG);
            if (!com.android.server.wearable.WearableSensingManagerService.this.mIsServiceEnabled) {
                android.util.Slog.w(com.android.server.wearable.WearableSensingManagerService.TAG, "Service not available.");
                com.android.server.wearable.WearableSensingManagerPerUserService.notifyStatusCallback(statusCallback, 3);
                return;
            }
            int userId = android.os.UserHandle.getCallingUserId();
            synchronized (com.android.server.wearable.WearableSensingManagerService.this.mDataRequestObserverContexts) {
                try {
                    try {
                        com.android.server.wearable.WearableSensingManagerService.DataRequestObserverContext previousObserverContext = com.android.server.wearable.WearableSensingManagerService.this.getDataRequestObserverContext(dataType, userId, dataRequestPendingIntent);
                        if (previousObserverContext != null) {
                            android.util.Slog.i(com.android.server.wearable.WearableSensingManagerService.TAG, "Received duplicate data request observer.");
                            android.os.RemoteCallback dataRequestCallback2 = previousObserverContext.mDataRequestRemoteCallback;
                            int i = previousObserverContext.mDataRequestObserverId;
                            dataRequestCallback = dataRequestCallback2;
                            dataRequestObserverId = i;
                        } else {
                            android.os.RemoteCallback dataRequestCallback3 = com.android.server.wearable.WearableSensingManagerService.this.createDataRequestRemoteCallback(dataRequestPendingIntent, userId);
                            int dataRequestObserverId2 = com.android.server.wearable.WearableSensingManagerService.this.mNextDataRequestObserverId.getAndIncrement();
                            com.android.server.wearable.WearableSensingManagerService.this.mDataRequestObserverContexts.add(new com.android.server.wearable.WearableSensingManagerService.DataRequestObserverContext(dataType, userId, dataRequestObserverId2, dataRequestPendingIntent, dataRequestCallback3));
                            dataRequestObserverId = dataRequestObserverId2;
                            dataRequestCallback = dataRequestCallback3;
                        }
                        final android.os.RemoteCallback remoteCallback = dataRequestCallback;
                        final int i2 = dataRequestObserverId;
                        com.android.server.wearable.WearableSensingManagerService.this.callPerUserServiceIfExist(new java.util.function.Consumer() { // from class: com.android.server.wearable.WearableSensingManagerService$WearableSensingManagerInternal$$ExternalSyntheticLambda6
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                com.android.server.wearable.WearableSensingManagerPerUserService wearableSensingManagerPerUserService = (com.android.server.wearable.WearableSensingManagerPerUserService) obj;
                                wearableSensingManagerPerUserService.onRegisterDataRequestObserver(dataType, remoteCallback, i2, dataRequestPendingIntent.getCreatorPackage(), statusCallback);
                            }
                        }, statusCallback);
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        }

        public void unregisterDataRequestObserver(final int dataType, android.app.PendingIntent dataRequestPendingIntent, final android.os.RemoteCallback statusCallback) {
            android.util.Slog.i(com.android.server.wearable.WearableSensingManagerService.TAG, "WearableSensingManagerInternal unregisterDataRequestObserver.");
            java.util.Objects.requireNonNull(dataRequestPendingIntent);
            java.util.Objects.requireNonNull(statusCallback);
            com.android.server.wearable.WearableSensingManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_WEARABLE_SENSING_SERVICE", com.android.server.wearable.WearableSensingManagerService.TAG);
            if (!com.android.server.wearable.WearableSensingManagerService.this.mIsServiceEnabled) {
                android.util.Slog.w(com.android.server.wearable.WearableSensingManagerService.TAG, "Service not available.");
                com.android.server.wearable.WearableSensingManagerPerUserService.notifyStatusCallback(statusCallback, 3);
                return;
            }
            int userId = android.os.UserHandle.getCallingUserId();
            synchronized (com.android.server.wearable.WearableSensingManagerService.this.mDataRequestObserverContexts) {
                com.android.server.wearable.WearableSensingManagerService.DataRequestObserverContext previousObserverContext = com.android.server.wearable.WearableSensingManagerService.this.getDataRequestObserverContext(dataType, userId, dataRequestPendingIntent);
                if (previousObserverContext == null) {
                    android.util.Slog.w(com.android.server.wearable.WearableSensingManagerService.TAG, "Previous observer not found, cannot unregister.");
                    return;
                }
                com.android.server.wearable.WearableSensingManagerService.this.mDataRequestObserverContexts.remove(previousObserverContext);
                final int previousDataRequestObserverId = previousObserverContext.mDataRequestObserverId;
                final java.lang.String pendingIntentCreatorPackage = previousObserverContext.mDataRequestPendingIntent.getCreatorPackage();
                com.android.server.wearable.WearableSensingManagerService.this.callPerUserServiceIfExist(new java.util.function.Consumer() { // from class: com.android.server.wearable.WearableSensingManagerService$WearableSensingManagerInternal$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.wearable.WearableSensingManagerPerUserService) obj).onUnregisterDataRequestObserver(dataType, previousDataRequestObserverId, pendingIntentCreatorPackage, statusCallback);
                    }
                }, statusCallback);
            }
        }

        public void startHotwordRecognition(final android.content.ComponentName targetVisComponentName, final android.os.RemoteCallback statusCallback) {
            android.util.Slog.i(com.android.server.wearable.WearableSensingManagerService.TAG, "WearableSensingManagerInternal startHotwordRecognition.");
            java.util.Objects.requireNonNull(statusCallback);
            com.android.server.wearable.WearableSensingManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_WEARABLE_SENSING_SERVICE", com.android.server.wearable.WearableSensingManagerService.TAG);
            if (!com.android.server.wearable.WearableSensingManagerService.this.mIsServiceEnabled) {
                android.util.Slog.w(com.android.server.wearable.WearableSensingManagerService.TAG, "Service not available.");
                com.android.server.wearable.WearableSensingManagerPerUserService.notifyStatusCallback(statusCallback, 3);
            } else {
                com.android.server.wearable.WearableSensingManagerService.this.callPerUserServiceIfExist(new java.util.function.Consumer() { // from class: com.android.server.wearable.WearableSensingManagerService$WearableSensingManagerInternal$$ExternalSyntheticLambda3
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.wearable.WearableSensingManagerPerUserService) obj).onStartHotwordRecognition(targetVisComponentName, statusCallback);
                    }
                }, statusCallback);
            }
        }

        public void stopHotwordRecognition(final android.os.RemoteCallback statusCallback) {
            android.util.Slog.i(com.android.server.wearable.WearableSensingManagerService.TAG, "WearableSensingManagerInternal stopHotwordRecognition.");
            java.util.Objects.requireNonNull(statusCallback);
            com.android.server.wearable.WearableSensingManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_WEARABLE_SENSING_SERVICE", com.android.server.wearable.WearableSensingManagerService.TAG);
            if (!com.android.server.wearable.WearableSensingManagerService.this.mIsServiceEnabled) {
                android.util.Slog.w(com.android.server.wearable.WearableSensingManagerService.TAG, "Service not available.");
                com.android.server.wearable.WearableSensingManagerPerUserService.notifyStatusCallback(statusCallback, 3);
            } else {
                com.android.server.wearable.WearableSensingManagerService.this.callPerUserServiceIfExist(new java.util.function.Consumer() { // from class: com.android.server.wearable.WearableSensingManagerService$WearableSensingManagerInternal$$ExternalSyntheticLambda4
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.wearable.WearableSensingManagerPerUserService) obj).onStopHotwordRecognition(statusCallback);
                    }
                }, statusCallback);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.wearable.WearableSensingShellCommand(com.android.server.wearable.WearableSensingManagerService.this).exec(this, in, out, err, args, callback, resultReceiver);
        }
    }
}
