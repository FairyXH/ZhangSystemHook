package com.android.server.ondeviceintelligence;

/* JADX INFO: loaded from: classes2.dex */
public class OnDeviceIntelligenceManagerService extends com.android.server.SystemService {
    private static final boolean DEFAULT_SERVICE_ENABLED = true;
    private static final java.lang.String KEY_SERVICE_ENABLED = "service_enabled";
    private static final int MSG_RESET_BROADCAST_KEYS = 1;
    private static final int MSG_RESET_CONFIG_NAMESPACE = 2;
    private static final int MSG_RESET_TEMPORARY_SERVICE = 0;
    private static final java.lang.String NAMESPACE_ON_DEVICE_INTELLIGENCE = "ondeviceintelligence";
    private static final java.lang.String SYSTEM_PACKAGE = "android";
    private final java.util.concurrent.Executor broadcastExecutor;
    private final java.util.concurrent.Executor callbackExecutor;
    private java.lang.String mBroadcastPackageName;
    private final java.util.concurrent.Executor mConfigExecutor;
    private final android.content.Context mContext;
    private final com.android.server.ondeviceintelligence.InferenceInfoStore mInferenceInfoStore;
    volatile boolean mIsServiceEnabled;
    protected final java.lang.Object mLock;
    private final android.os.Handler mMainHandler;
    private final android.provider.DeviceConfig.OnPropertiesChangedListener mOnPropertiesChangedListener;
    private com.android.server.ondeviceintelligence.RemoteOnDeviceSandboxedInferenceService mRemoteInferenceService;
    private com.android.server.ondeviceintelligence.RemoteOnDeviceIntelligenceService mRemoteOnDeviceIntelligenceService;
    private java.lang.String[] mTemporaryBroadcastKeys;
    private java.lang.String mTemporaryConfigNamespace;
    private android.os.Handler mTemporaryHandler;
    private java.lang.String[] mTemporaryServiceNames;
    private int remoteInferenceServiceUid;
    private final java.util.concurrent.Executor resourceClosingExecutor;
    private static final java.lang.String TAG = com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.class.getSimpleName();
    private static final long MAX_AGE_MS = java.util.concurrent.TimeUnit.HOURS.toMillis(3);

    public OnDeviceIntelligenceManagerService(android.content.Context context) {
        super(context);
        this.resourceClosingExecutor = java.util.concurrent.Executors.newCachedThreadPool();
        this.callbackExecutor = java.util.concurrent.Executors.newCachedThreadPool();
        this.broadcastExecutor = java.util.concurrent.Executors.newCachedThreadPool();
        this.mConfigExecutor = java.util.concurrent.Executors.newCachedThreadPool();
        this.mLock = new java.lang.Object();
        this.remoteInferenceServiceUid = -1;
        this.mOnPropertiesChangedListener = new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.sendUpdatedConfig(properties);
            }
        };
        this.mMainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        this.mContext = context;
        this.mTemporaryServiceNames = new java.lang.String[0];
        this.mInferenceInfoStore = new com.android.server.ondeviceintelligence.InferenceInfoStore(MAX_AGE_MS);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("on_device_intelligence", getOnDeviceIntelligenceManagerService(), true);
        com.android.server.LocalServices.addService(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerInternal.class, new com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerInternal() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$$ExternalSyntheticLambda7
            @Override // com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerInternal
            public final int getInferenceServiceUid() {
                return this.f$0.getRemoteInferenceServiceUid();
            }
        });
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            android.provider.DeviceConfig.addOnPropertiesChangedListener(NAMESPACE_ON_DEVICE_INTELLIGENCE, com.android.internal.os.BackgroundThread.getExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$$ExternalSyntheticLambda4
                public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                    this.f$0.lambda$onBootPhase$0(properties);
                }
            });
            this.mIsServiceEnabled = isServiceEnabled();
        }
        if (phase == 600) {
            try {
                ensureRemoteInferenceServiceInitialized();
                ensureRemoteIntelligenceServiceInitialized();
            } catch (java.lang.Exception e) {
                android.util.Slog.w(TAG, "Couldn't pre-start remote ondeviceintelligence services.", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$0(android.provider.DeviceConfig.Properties properties) {
        onDeviceConfigChange(properties.getKeyset());
    }

    private void onDeviceConfigChange(java.util.Set<java.lang.String> keys) {
        if (keys.contains(KEY_SERVICE_ENABLED)) {
            this.mIsServiceEnabled = isServiceEnabled();
        }
    }

    private boolean isServiceEnabled() {
        return android.provider.DeviceConfig.getBoolean(NAMESPACE_ON_DEVICE_INTELLIGENCE, KEY_SERVICE_ENABLED, true);
    }

    /* JADX INFO: renamed from: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$1, reason: invalid class name */
    class AnonymousClass1 extends android.app.ondeviceintelligence.IOnDeviceIntelligenceManager.Stub {
        AnonymousClass1() {
        }

        public java.lang.String getRemoteServicePackageName() {
            return com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.getRemoteConfiguredPackageName();
        }

        public java.util.List<android.app.ondeviceintelligence.InferenceInfo> getLatestInferenceInfo(long startTimeEpochMillis) {
            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mContext.enforceCallingPermission("android.permission.DUMP", com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG);
            return com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.getLatestInferenceInfo(startTimeEpochMillis);
        }

        public void getVersion(final android.os.RemoteCallback remoteCallback) {
            android.util.Slog.i(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "OnDeviceIntelligenceManagerInternal getVersion");
            java.util.Objects.requireNonNull(remoteCallback);
            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mContext.enforceCallingPermission("android.permission.USE_ON_DEVICE_INTELLIGENCE", com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG);
            if (!com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mIsServiceEnabled) {
                android.util.Slog.w(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "Service not available");
                remoteCallback.sendResult((android.os.Bundle) null);
            } else {
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.ensureRemoteIntelligenceServiceInitialized();
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mRemoteOnDeviceIntelligenceService.postAsync(new com.android.internal.infra.ServiceConnector.Job() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$1$$ExternalSyntheticLambda14
                    public final java.lang.Object run(java.lang.Object obj) {
                        return this.f$0.lambda$getVersion$1(remoteCallback, (android.service.ondeviceintelligence.IOnDeviceIntelligenceService) obj);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ java.util.concurrent.CompletableFuture lambda$getVersion$1(final android.os.RemoteCallback remoteCallback, android.service.ondeviceintelligence.IOnDeviceIntelligenceService service) throws java.lang.Exception {
            final com.android.internal.infra.AndroidFuture future = new com.android.internal.infra.AndroidFuture();
            service.getVersion(new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$1$$ExternalSyntheticLambda5
                public final void onResult(android.os.Bundle bundle) {
                    com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.AnonymousClass1.lambda$getVersion$0(remoteCallback, future, bundle);
                }
            }));
            return future.orTimeout(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.getIdleTimeoutMs(), java.util.concurrent.TimeUnit.MILLISECONDS);
        }

        static /* synthetic */ void lambda$getVersion$0(android.os.RemoteCallback remoteCallback, com.android.internal.infra.AndroidFuture future, android.os.Bundle result) {
            remoteCallback.sendResult(result);
            future.complete((java.lang.Object) null);
        }

        public void getFeature(final int id, final android.app.ondeviceintelligence.IFeatureCallback featureCallback) throws android.os.RemoteException {
            android.util.Slog.i(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "OnDeviceIntelligenceManagerInternal getFeatures");
            java.util.Objects.requireNonNull(featureCallback);
            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mContext.enforceCallingPermission("android.permission.USE_ON_DEVICE_INTELLIGENCE", com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG);
            if (!com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mIsServiceEnabled) {
                android.util.Slog.w(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "Service not available");
                featureCallback.onFailure(100, "OnDeviceIntelligenceManagerService is unavailable", android.os.PersistableBundle.EMPTY);
            } else {
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.ensureRemoteIntelligenceServiceInitialized();
                final int callerUid = android.os.Binder.getCallingUid();
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mRemoteOnDeviceIntelligenceService.postAsync(new com.android.internal.infra.ServiceConnector.Job() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$1$$ExternalSyntheticLambda4
                    public final java.lang.Object run(java.lang.Object obj) {
                        return this.f$0.lambda$getFeature$2(callerUid, id, featureCallback, (android.service.ondeviceintelligence.IOnDeviceIntelligenceService) obj);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ java.util.concurrent.CompletableFuture lambda$getFeature$2(int callerUid, int id, final android.app.ondeviceintelligence.IFeatureCallback featureCallback, android.service.ondeviceintelligence.IOnDeviceIntelligenceService service) throws java.lang.Exception {
            final com.android.internal.infra.AndroidFuture future = new com.android.internal.infra.AndroidFuture();
            service.getFeature(callerUid, id, new android.app.ondeviceintelligence.IFeatureCallback.Stub() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.1.1
                public void onSuccess(android.app.ondeviceintelligence.Feature result) throws android.os.RemoteException {
                    featureCallback.onSuccess(result);
                    future.complete((java.lang.Object) null);
                }

                public void onFailure(int errorCode, java.lang.String errorMessage, android.os.PersistableBundle errorParams) throws android.os.RemoteException {
                    featureCallback.onFailure(errorCode, errorMessage, errorParams);
                    future.completeExceptionally(new java.util.concurrent.TimeoutException());
                }
            });
            return future.orTimeout(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.getIdleTimeoutMs(), java.util.concurrent.TimeUnit.MILLISECONDS);
        }

        public void listFeatures(final android.app.ondeviceintelligence.IListFeaturesCallback listFeaturesCallback) throws android.os.RemoteException {
            android.util.Slog.i(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "OnDeviceIntelligenceManagerInternal getFeatures");
            java.util.Objects.requireNonNull(listFeaturesCallback);
            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mContext.enforceCallingPermission("android.permission.USE_ON_DEVICE_INTELLIGENCE", com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG);
            if (!com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mIsServiceEnabled) {
                android.util.Slog.w(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "Service not available");
                listFeaturesCallback.onFailure(100, "OnDeviceIntelligenceManagerService is unavailable", android.os.PersistableBundle.EMPTY);
            } else {
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.ensureRemoteIntelligenceServiceInitialized();
                final int callerUid = android.os.Binder.getCallingUid();
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mRemoteOnDeviceIntelligenceService.postAsync(new com.android.internal.infra.ServiceConnector.Job() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$1$$ExternalSyntheticLambda3
                    public final java.lang.Object run(java.lang.Object obj) {
                        return this.f$0.lambda$listFeatures$3(callerUid, listFeaturesCallback, (android.service.ondeviceintelligence.IOnDeviceIntelligenceService) obj);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ java.util.concurrent.CompletableFuture lambda$listFeatures$3(int callerUid, final android.app.ondeviceintelligence.IListFeaturesCallback listFeaturesCallback, android.service.ondeviceintelligence.IOnDeviceIntelligenceService service) throws java.lang.Exception {
            final com.android.internal.infra.AndroidFuture future = new com.android.internal.infra.AndroidFuture();
            service.listFeatures(callerUid, new android.app.ondeviceintelligence.IListFeaturesCallback.Stub() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.1.2
                public void onSuccess(java.util.List<android.app.ondeviceintelligence.Feature> result) throws android.os.RemoteException {
                    listFeaturesCallback.onSuccess(result);
                    future.complete((java.lang.Object) null);
                }

                public void onFailure(int errorCode, java.lang.String errorMessage, android.os.PersistableBundle errorParams) throws android.os.RemoteException {
                    listFeaturesCallback.onFailure(errorCode, errorMessage, errorParams);
                    future.completeExceptionally(new java.util.concurrent.TimeoutException());
                }
            });
            return future.orTimeout(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.getIdleTimeoutMs(), java.util.concurrent.TimeUnit.MILLISECONDS);
        }

        public void getFeatureDetails(final android.app.ondeviceintelligence.Feature feature, final android.app.ondeviceintelligence.IFeatureDetailsCallback featureDetailsCallback) throws android.os.RemoteException {
            android.util.Slog.i(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "OnDeviceIntelligenceManagerInternal getFeatureStatus");
            java.util.Objects.requireNonNull(feature);
            java.util.Objects.requireNonNull(featureDetailsCallback);
            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mContext.enforceCallingPermission("android.permission.USE_ON_DEVICE_INTELLIGENCE", com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG);
            if (!com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mIsServiceEnabled) {
                android.util.Slog.w(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "Service not available");
                featureDetailsCallback.onFailure(100, "OnDeviceIntelligenceManagerService is unavailable", android.os.PersistableBundle.EMPTY);
            } else {
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.ensureRemoteIntelligenceServiceInitialized();
                final int callerUid = android.os.Binder.getCallingUid();
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mRemoteOnDeviceIntelligenceService.postAsync(new com.android.internal.infra.ServiceConnector.Job() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$1$$ExternalSyntheticLambda10
                    public final java.lang.Object run(java.lang.Object obj) {
                        return this.f$0.lambda$getFeatureDetails$4(callerUid, feature, featureDetailsCallback, (android.service.ondeviceintelligence.IOnDeviceIntelligenceService) obj);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ java.util.concurrent.CompletableFuture lambda$getFeatureDetails$4(int callerUid, android.app.ondeviceintelligence.Feature feature, final android.app.ondeviceintelligence.IFeatureDetailsCallback featureDetailsCallback, android.service.ondeviceintelligence.IOnDeviceIntelligenceService service) throws java.lang.Exception {
            final com.android.internal.infra.AndroidFuture future = new com.android.internal.infra.AndroidFuture();
            service.getFeatureDetails(callerUid, feature, new android.app.ondeviceintelligence.IFeatureDetailsCallback.Stub() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.1.3
                public void onSuccess(android.app.ondeviceintelligence.FeatureDetails result) throws android.os.RemoteException {
                    future.complete((java.lang.Object) null);
                    featureDetailsCallback.onSuccess(result);
                }

                public void onFailure(int errorCode, java.lang.String errorMessage, android.os.PersistableBundle errorParams) throws android.os.RemoteException {
                    future.completeExceptionally((java.lang.Throwable) null);
                    featureDetailsCallback.onFailure(errorCode, errorMessage, errorParams);
                }
            });
            return future.orTimeout(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.getIdleTimeoutMs(), java.util.concurrent.TimeUnit.MILLISECONDS);
        }

        public void requestFeatureDownload(final android.app.ondeviceintelligence.Feature feature, final com.android.internal.infra.AndroidFuture cancellationSignalFuture, final android.app.ondeviceintelligence.IDownloadCallback downloadCallback) throws android.os.RemoteException {
            android.util.Slog.i(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "OnDeviceIntelligenceManagerInternal requestFeatureDownload");
            java.util.Objects.requireNonNull(feature);
            java.util.Objects.requireNonNull(downloadCallback);
            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mContext.enforceCallingPermission("android.permission.USE_ON_DEVICE_INTELLIGENCE", com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG);
            if (!com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mIsServiceEnabled) {
                android.util.Slog.w(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "Service not available");
                downloadCallback.onDownloadFailed(4, "OnDeviceIntelligenceManagerService is unavailable", android.os.PersistableBundle.EMPTY);
            }
            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.ensureRemoteIntelligenceServiceInitialized();
            final int callerUid = android.os.Binder.getCallingUid();
            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mRemoteOnDeviceIntelligenceService.postAsync(new com.android.internal.infra.ServiceConnector.Job() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$1$$ExternalSyntheticLambda6
                public final java.lang.Object run(java.lang.Object obj) {
                    return this.f$0.lambda$requestFeatureDownload$5(downloadCallback, callerUid, feature, cancellationSignalFuture, (android.service.ondeviceintelligence.IOnDeviceIntelligenceService) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ java.util.concurrent.CompletableFuture lambda$requestFeatureDownload$5(android.app.ondeviceintelligence.IDownloadCallback downloadCallback, int callerUid, android.app.ondeviceintelligence.Feature feature, com.android.internal.infra.AndroidFuture cancellationSignalFuture, android.service.ondeviceintelligence.IOnDeviceIntelligenceService service) throws java.lang.Exception {
            com.android.internal.infra.AndroidFuture future = new com.android.internal.infra.AndroidFuture();
            com.android.server.ondeviceintelligence.callbacks.ListenableDownloadCallback listenableDownloadCallback = new com.android.server.ondeviceintelligence.callbacks.ListenableDownloadCallback(downloadCallback, com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mMainHandler, future, com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.getIdleTimeoutMs());
            service.requestFeatureDownload(callerUid, feature, com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.wrapCancellationFuture(cancellationSignalFuture), listenableDownloadCallback);
            return future;
        }

        public void requestTokenInfo(final android.app.ondeviceintelligence.Feature feature, final android.os.Bundle request, final com.android.internal.infra.AndroidFuture cancellationSignalFuture, final android.app.ondeviceintelligence.ITokenInfoCallback tokenInfoCallback) throws android.os.RemoteException {
            android.util.Slog.i(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "OnDeviceIntelligenceManagerInternal requestTokenInfo");
            com.android.internal.infra.AndroidFuture<java.lang.Void> result = null;
            try {
                java.util.Objects.requireNonNull(feature);
                com.android.server.ondeviceintelligence.BundleUtil.sanitizeInferenceParams(request);
                java.util.Objects.requireNonNull(tokenInfoCallback);
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mContext.enforceCallingPermission("android.permission.USE_ON_DEVICE_INTELLIGENCE", com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG);
                if (!com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mIsServiceEnabled) {
                    android.util.Slog.w(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "Service not available");
                    tokenInfoCallback.onFailure(100, "OnDeviceIntelligenceManagerService is unavailable", android.os.PersistableBundle.EMPTY);
                }
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.ensureRemoteInferenceServiceInitialized();
                final int callerUid = android.os.Binder.getCallingUid();
                result = com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mRemoteInferenceService.postAsync(new com.android.internal.infra.ServiceConnector.Job() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$1$$ExternalSyntheticLambda11
                    public final java.lang.Object run(java.lang.Object obj) {
                        return this.f$0.lambda$requestTokenInfo$6(callerUid, feature, request, cancellationSignalFuture, tokenInfoCallback, (android.service.ondeviceintelligence.IOnDeviceSandboxedInferenceService) obj);
                    }
                });
                result.whenCompleteAsync(new java.util.function.BiConsumer() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$1$$ExternalSyntheticLambda12
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        com.android.server.ondeviceintelligence.BundleUtil.tryCloseResource(request);
                    }
                }, com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.resourceClosingExecutor);
            } finally {
                if (result == null) {
                    com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.resourceClosingExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$1$$ExternalSyntheticLambda13
                        @Override // java.lang.Runnable
                        public final void run() {
                            com.android.server.ondeviceintelligence.BundleUtil.tryCloseResource(request);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ java.util.concurrent.CompletableFuture lambda$requestTokenInfo$6(int callerUid, android.app.ondeviceintelligence.Feature feature, android.os.Bundle request, com.android.internal.infra.AndroidFuture cancellationSignalFuture, android.app.ondeviceintelligence.ITokenInfoCallback tokenInfoCallback, android.service.ondeviceintelligence.IOnDeviceSandboxedInferenceService service) throws java.lang.Exception {
            com.android.internal.infra.AndroidFuture future = new com.android.internal.infra.AndroidFuture();
            service.requestTokenInfo(callerUid, feature, request, com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.wrapCancellationFuture(cancellationSignalFuture), com.android.server.ondeviceintelligence.BundleUtil.wrapWithValidation(tokenInfoCallback, future, com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mInferenceInfoStore));
            return future.orTimeout(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.getIdleTimeoutMs(), java.util.concurrent.TimeUnit.MILLISECONDS);
        }

        public void processRequest(final android.app.ondeviceintelligence.Feature feature, final android.os.Bundle request, final int requestType, final com.android.internal.infra.AndroidFuture cancellationSignalFuture, final com.android.internal.infra.AndroidFuture processingSignalFuture, final android.app.ondeviceintelligence.IResponseCallback responseCallback) throws java.lang.Throwable {
            com.android.internal.infra.AndroidFuture<java.lang.Void> result = null;
            try {
                android.util.Slog.i(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "OnDeviceIntelligenceManagerInternal processRequest");
                java.util.Objects.requireNonNull(feature);
                com.android.server.ondeviceintelligence.BundleUtil.sanitizeInferenceParams(request);
                java.util.Objects.requireNonNull(responseCallback);
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mContext.enforceCallingPermission("android.permission.USE_ON_DEVICE_INTELLIGENCE", com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG);
                if (!com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mIsServiceEnabled) {
                    android.util.Slog.w(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "Service not available");
                    try {
                        responseCallback.onFailure(15, "OnDeviceIntelligenceManagerService is unavailable", android.os.PersistableBundle.EMPTY);
                    } catch (java.lang.Throwable th) {
                        th = th;
                        if (result == null) {
                            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.resourceClosingExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$1$$ExternalSyntheticLambda9
                                @Override // java.lang.Runnable
                                public final void run() {
                                    com.android.server.ondeviceintelligence.BundleUtil.tryCloseResource(request);
                                }
                            });
                        }
                        throw th;
                    }
                }
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.ensureRemoteInferenceServiceInitialized();
                final int callerUid = android.os.Binder.getCallingUid();
                result = com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mRemoteInferenceService.postAsync(new com.android.internal.infra.ServiceConnector.Job() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$1$$ExternalSyntheticLambda7
                    public final java.lang.Object run(java.lang.Object obj) {
                        return this.f$0.lambda$processRequest$9(callerUid, feature, request, requestType, cancellationSignalFuture, processingSignalFuture, responseCallback, (android.service.ondeviceintelligence.IOnDeviceSandboxedInferenceService) obj);
                    }
                });
                result.whenCompleteAsync(new java.util.function.BiConsumer() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$1$$ExternalSyntheticLambda8
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        com.android.server.ondeviceintelligence.BundleUtil.tryCloseResource(request);
                    }
                }, com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.resourceClosingExecutor);
                if (result == null) {
                    com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.resourceClosingExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$1$$ExternalSyntheticLambda9
                        @Override // java.lang.Runnable
                        public final void run() {
                            com.android.server.ondeviceintelligence.BundleUtil.tryCloseResource(request);
                        }
                    });
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ java.util.concurrent.CompletableFuture lambda$processRequest$9(int callerUid, android.app.ondeviceintelligence.Feature feature, android.os.Bundle request, int requestType, com.android.internal.infra.AndroidFuture cancellationSignalFuture, com.android.internal.infra.AndroidFuture processingSignalFuture, android.app.ondeviceintelligence.IResponseCallback responseCallback, android.service.ondeviceintelligence.IOnDeviceSandboxedInferenceService service) throws java.lang.Exception {
            com.android.internal.infra.AndroidFuture future = new com.android.internal.infra.AndroidFuture();
            service.processRequest(callerUid, feature, request, requestType, com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.wrapCancellationFuture(cancellationSignalFuture), com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.wrapProcessingFuture(processingSignalFuture), com.android.server.ondeviceintelligence.BundleUtil.wrapWithValidation(responseCallback, com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.resourceClosingExecutor, future, com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mInferenceInfoStore));
            return future.orTimeout(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.getIdleTimeoutMs(), java.util.concurrent.TimeUnit.MILLISECONDS);
        }

        public void processRequestStreaming(final android.app.ondeviceintelligence.Feature feature, final android.os.Bundle request, final int requestType, final com.android.internal.infra.AndroidFuture cancellationSignalFuture, final com.android.internal.infra.AndroidFuture processingSignalFuture, final android.app.ondeviceintelligence.IStreamingResponseCallback streamingCallback) throws java.lang.Throwable {
            com.android.internal.infra.AndroidFuture<java.lang.Void> result = null;
            try {
                android.util.Slog.i(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "OnDeviceIntelligenceManagerInternal processRequestStreaming");
                java.util.Objects.requireNonNull(feature);
                com.android.server.ondeviceintelligence.BundleUtil.sanitizeInferenceParams(request);
                java.util.Objects.requireNonNull(streamingCallback);
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mContext.enforceCallingPermission("android.permission.USE_ON_DEVICE_INTELLIGENCE", com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG);
                if (!com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mIsServiceEnabled) {
                    android.util.Slog.w(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "Service not available");
                    try {
                        streamingCallback.onFailure(15, "OnDeviceIntelligenceManagerService is unavailable", android.os.PersistableBundle.EMPTY);
                    } catch (java.lang.Throwable th) {
                        th = th;
                        if (result == null) {
                            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.resourceClosingExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$1$$ExternalSyntheticLambda2
                                @Override // java.lang.Runnable
                                public final void run() {
                                    com.android.server.ondeviceintelligence.BundleUtil.tryCloseResource(request);
                                }
                            });
                        }
                        throw th;
                    }
                }
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.ensureRemoteInferenceServiceInitialized();
                final int callerUid = android.os.Binder.getCallingUid();
                result = com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mRemoteInferenceService.postAsync(new com.android.internal.infra.ServiceConnector.Job() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$1$$ExternalSyntheticLambda0
                    public final java.lang.Object run(java.lang.Object obj) {
                        return this.f$0.lambda$processRequestStreaming$12(callerUid, feature, request, requestType, cancellationSignalFuture, processingSignalFuture, streamingCallback, (android.service.ondeviceintelligence.IOnDeviceSandboxedInferenceService) obj);
                    }
                });
                result.whenCompleteAsync(new java.util.function.BiConsumer() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$1$$ExternalSyntheticLambda1
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        com.android.server.ondeviceintelligence.BundleUtil.tryCloseResource(request);
                    }
                }, com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.resourceClosingExecutor);
                if (result == null) {
                    com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.resourceClosingExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$1$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            com.android.server.ondeviceintelligence.BundleUtil.tryCloseResource(request);
                        }
                    });
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ java.util.concurrent.CompletableFuture lambda$processRequestStreaming$12(int callerUid, android.app.ondeviceintelligence.Feature feature, android.os.Bundle request, int requestType, com.android.internal.infra.AndroidFuture cancellationSignalFuture, com.android.internal.infra.AndroidFuture processingSignalFuture, android.app.ondeviceintelligence.IStreamingResponseCallback streamingCallback, android.service.ondeviceintelligence.IOnDeviceSandboxedInferenceService service) throws java.lang.Exception {
            com.android.internal.infra.AndroidFuture future = new com.android.internal.infra.AndroidFuture();
            service.processRequestStreaming(callerUid, feature, request, requestType, com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.wrapCancellationFuture(cancellationSignalFuture), com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.wrapProcessingFuture(processingSignalFuture), com.android.server.ondeviceintelligence.BundleUtil.wrapWithValidation(streamingCallback, com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.resourceClosingExecutor, future, com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mInferenceInfoStore));
            return future.orTimeout(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.getIdleTimeoutMs(), java.util.concurrent.TimeUnit.MILLISECONDS);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.ondeviceintelligence.OnDeviceIntelligenceShellCommand(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this).exec(this, in, out, err, args, callback, resultReceiver);
        }
    }

    private android.os.IBinder getOnDeviceIntelligenceManagerService() {
        return new com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.AnonymousClass1();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureRemoteIntelligenceServiceInitialized() {
        synchronized (this.mLock) {
            if (this.mRemoteOnDeviceIntelligenceService == null) {
                final java.lang.String serviceName = getServiceNames()[0];
                android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$$ExternalSyntheticLambda6
                    public final void runOrThrow() throws java.lang.Exception {
                        this.f$0.lambda$ensureRemoteIntelligenceServiceInitialized$1(serviceName);
                    }
                });
                this.mRemoteOnDeviceIntelligenceService = new com.android.server.ondeviceintelligence.RemoteOnDeviceIntelligenceService(this.mContext, android.content.ComponentName.unflattenFromString(serviceName), android.os.UserHandle.SYSTEM.getIdentifier());
                this.mRemoteOnDeviceIntelligenceService.setServiceLifecycleCallbacks(new com.android.internal.infra.ServiceConnector.ServiceLifecycleCallbacks<android.service.ondeviceintelligence.IOnDeviceIntelligenceService>() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.2
                    public void onConnected(android.service.ondeviceintelligence.IOnDeviceIntelligenceService service) {
                        try {
                            service.registerRemoteServices(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.getRemoteProcessingService());
                            service.ready();
                        } catch (android.os.RemoteException ex) {
                            android.util.Slog.w(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "Failed to send connected event", ex);
                        }
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$ensureRemoteIntelligenceServiceInitialized$1(java.lang.String serviceName) throws java.lang.Exception {
        validateServiceElevated(serviceName, false);
    }

    /* JADX INFO: renamed from: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$3, reason: invalid class name */
    class AnonymousClass3 extends android.service.ondeviceintelligence.IRemoteProcessingService.Stub {
        AnonymousClass3() {
        }

        public void updateProcessingState(final android.os.Bundle processingState, final android.service.ondeviceintelligence.IProcessingUpdateStatusCallback callback) {
            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.callbackExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$3$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateProcessingState$3(processingState, callback);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateProcessingState$3(final android.os.Bundle processingState, final android.service.ondeviceintelligence.IProcessingUpdateStatusCallback callback) {
            com.android.internal.infra.AndroidFuture<java.lang.Void> result = null;
            try {
                com.android.server.ondeviceintelligence.BundleUtil.sanitizeStateParams(processingState);
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.ensureRemoteInferenceServiceInitialized();
                result = com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mRemoteInferenceService.post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$3$$ExternalSyntheticLambda0
                    public final void runNoResult(java.lang.Object obj) {
                        ((android.service.ondeviceintelligence.IOnDeviceSandboxedInferenceService) obj).updateProcessingState(processingState, callback);
                    }
                });
                result.whenCompleteAsync(new java.util.function.BiConsumer() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$3$$ExternalSyntheticLambda1
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        com.android.server.ondeviceintelligence.BundleUtil.tryCloseResource(processingState);
                    }
                }, com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.resourceClosingExecutor);
            } finally {
                if (result == null) {
                    com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.resourceClosingExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$3$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            com.android.server.ondeviceintelligence.BundleUtil.tryCloseResource(processingState);
                        }
                    });
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.service.ondeviceintelligence.IRemoteProcessingService.Stub getRemoteProcessingService() {
        return new com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.AnonymousClass3();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureRemoteInferenceServiceInitialized() {
        synchronized (this.mLock) {
            if (this.mRemoteInferenceService == null) {
                final java.lang.String serviceName = getServiceNames()[1];
                android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$$ExternalSyntheticLambda5
                    public final void runOrThrow() throws java.lang.Exception {
                        this.f$0.lambda$ensureRemoteInferenceServiceInitialized$2(serviceName);
                    }
                });
                this.mRemoteInferenceService = new com.android.server.ondeviceintelligence.RemoteOnDeviceSandboxedInferenceService(this.mContext, android.content.ComponentName.unflattenFromString(serviceName), android.os.UserHandle.SYSTEM.getIdentifier());
                this.mRemoteInferenceService.setServiceLifecycleCallbacks(new com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.AnonymousClass4());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$ensureRemoteInferenceServiceInitialized$2(java.lang.String serviceName) throws java.lang.Exception {
        validateServiceElevated(serviceName, true);
    }

    /* JADX INFO: renamed from: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$4, reason: invalid class name */
    class AnonymousClass4 implements com.android.internal.infra.ServiceConnector.ServiceLifecycleCallbacks<android.service.ondeviceintelligence.IOnDeviceSandboxedInferenceService> {
        AnonymousClass4() {
        }

        public void onConnected(final android.service.ondeviceintelligence.IOnDeviceSandboxedInferenceService service) {
            try {
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.ensureRemoteIntelligenceServiceInitialized();
                service.registerRemoteStorageService(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.getIRemoteStorageService(), new android.os.IRemoteCallback.Stub() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.4.1
                    public void sendResult(android.os.Bundle bundle) {
                        int uid = android.os.Binder.getCallingUid();
                        com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.setRemoteInferenceServiceUid(uid);
                    }
                });
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mRemoteOnDeviceIntelligenceService.run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$4$$ExternalSyntheticLambda0
                    public final void runNoResult(java.lang.Object obj) {
                        ((android.service.ondeviceintelligence.IOnDeviceIntelligenceService) obj).notifyInferenceServiceConnected();
                    }
                });
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.broadcastExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$4$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onConnected$0(service);
                    }
                });
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mConfigExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$4$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onConnected$1();
                    }
                });
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "Failed to send connected event", ex);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onConnected$0(android.service.ondeviceintelligence.IOnDeviceSandboxedInferenceService service) {
            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.registerModelLoadingBroadcasts(service);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onConnected$1() {
            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.registerDeviceConfigChangeListener();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerModelLoadingBroadcasts(android.service.ondeviceintelligence.IOnDeviceSandboxedInferenceService service) {
        try {
            final java.lang.String[] modelBroadcastKeys = getBroadcastKeys();
            android.os.Bundle bundle = new android.os.Bundle();
            bundle.putBoolean("register_model_update_callback", true);
            try {
                service.updateProcessingState(bundle, new android.service.ondeviceintelligence.IProcessingUpdateStatusCallback.Stub() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.5
                    public void onSuccess(android.os.PersistableBundle statusParams) {
                        java.lang.String modelUnloadedBroadcastKey;
                        android.os.Binder.clearCallingIdentity();
                        synchronized (com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mLock) {
                            if (statusParams.containsKey("model_loaded")) {
                                java.lang.String modelLoadedBroadcastKey = modelBroadcastKeys[0];
                                if (modelLoadedBroadcastKey != null && !modelLoadedBroadcastKey.isEmpty()) {
                                    android.content.Intent intent = new android.content.Intent(modelLoadedBroadcastKey);
                                    intent.setPackage(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mBroadcastPackageName);
                                    com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mContext.sendBroadcast(intent, "android.permission.USE_ON_DEVICE_INTELLIGENCE");
                                }
                            } else if (statusParams.containsKey("model_unloaded") && (modelUnloadedBroadcastKey = modelBroadcastKeys[1]) != null && !modelUnloadedBroadcastKey.isEmpty()) {
                                android.content.Intent intent2 = new android.content.Intent(modelUnloadedBroadcastKey);
                                intent2.setPackage(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mBroadcastPackageName);
                                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mContext.sendBroadcast(intent2, "android.permission.USE_ON_DEVICE_INTELLIGENCE");
                            }
                        }
                    }

                    public void onFailure(int errorCode, java.lang.String errorMessage) {
                        android.util.Slog.e(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "Failed to register model loading callback with status code", new android.app.ondeviceintelligence.OnDeviceIntelligenceException(errorCode, errorMessage));
                    }
                });
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Failed to register model loading callback with status code", e);
            }
        } catch (android.content.res.Resources.NotFoundException e2) {
            android.util.Slog.d(TAG, "Skipping model broadcasts as broadcast intents configured.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerDeviceConfigChangeListener() {
        android.util.Log.d(TAG, "registerDeviceConfigChangeListener");
        java.lang.String configNamespace = getConfigNamespace();
        if (configNamespace.isEmpty()) {
            android.util.Slog.e(TAG, "config_defaultOnDeviceIntelligenceDeviceConfigNamespace is empty");
        } else {
            android.provider.DeviceConfig.addOnPropertiesChangedListener(configNamespace, this.mConfigExecutor, this.mOnPropertiesChangedListener);
        }
    }

    private java.lang.String getConfigNamespace() {
        synchronized (this.mLock) {
            if (this.mTemporaryConfigNamespace != null) {
                return this.mTemporaryConfigNamespace;
            }
            return this.mContext.getResources().getString(android.R.string.config_defaultSupervisionProfileOwnerComponent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendUpdatedConfig(android.provider.DeviceConfig.Properties props) {
        android.util.Log.d(TAG, "sendUpdatedConfig");
        android.os.PersistableBundle persistableBundle = new android.os.PersistableBundle();
        for (java.lang.String key : props.getKeyset()) {
            persistableBundle.putString(key, props.getString(key, ""));
        }
        final android.os.Bundle bundle = new android.os.Bundle();
        bundle.putParcelable("device_config_update", persistableBundle);
        ensureRemoteInferenceServiceInitialized();
        this.mRemoteInferenceService.run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$$ExternalSyntheticLambda2
            public final void runNoResult(java.lang.Object obj) throws java.lang.Exception {
                this.f$0.lambda$sendUpdatedConfig$3(bundle, (android.service.ondeviceintelligence.IOnDeviceSandboxedInferenceService) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendUpdatedConfig$3(android.os.Bundle bundle, android.service.ondeviceintelligence.IOnDeviceSandboxedInferenceService service) throws java.lang.Exception {
        service.updateProcessingState(bundle, new android.service.ondeviceintelligence.IProcessingUpdateStatusCallback.Stub() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.6
            public void onSuccess(android.os.PersistableBundle result) {
                android.util.Slog.d(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "Config update successful." + result);
            }

            public void onFailure(int errorCode, java.lang.String errorMessage) {
                android.util.Slog.e(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "Config update failed with code [" + java.lang.String.valueOf(errorCode) + "] and message = " + errorMessage);
            }
        });
    }

    /* JADX INFO: renamed from: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$7, reason: invalid class name */
    class AnonymousClass7 extends android.service.ondeviceintelligence.IRemoteStorageService.Stub {
        AnonymousClass7() {
        }

        public void getReadOnlyFileDescriptor(final java.lang.String filePath, final com.android.internal.infra.AndroidFuture<android.os.ParcelFileDescriptor> future) {
            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.ensureRemoteIntelligenceServiceInitialized();
            final com.android.internal.infra.AndroidFuture<android.os.ParcelFileDescriptor> pfdFuture = new com.android.internal.infra.AndroidFuture<>();
            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mRemoteOnDeviceIntelligenceService.run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$7$$ExternalSyntheticLambda1
                public final void runNoResult(java.lang.Object obj) {
                    ((android.service.ondeviceintelligence.IOnDeviceIntelligenceService) obj).getReadOnlyFileDescriptor(filePath, pfdFuture);
                }
            });
            pfdFuture.whenCompleteAsync(new java.util.function.BiConsumer() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$7$$ExternalSyntheticLambda2
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.AnonymousClass7.lambda$getReadOnlyFileDescriptor$1(future, (android.os.ParcelFileDescriptor) obj, (java.lang.Throwable) obj2);
                }
            }, com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.callbackExecutor);
        }

        static /* synthetic */ void lambda$getReadOnlyFileDescriptor$1(com.android.internal.infra.AndroidFuture future, android.os.ParcelFileDescriptor pfd, java.lang.Throwable error) {
            try {
                if (error != null) {
                    future.completeExceptionally(error);
                } else {
                    com.android.server.ondeviceintelligence.BundleUtil.validatePfdReadOnly(pfd);
                    future.complete(pfd);
                }
            } finally {
                com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.tryClosePfd(pfd);
            }
        }

        public void getReadOnlyFeatureFileDescriptorMap(final android.app.ondeviceintelligence.Feature feature, final android.os.RemoteCallback remoteCallback) {
            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.ensureRemoteIntelligenceServiceInitialized();
            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mRemoteOnDeviceIntelligenceService.run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$7$$ExternalSyntheticLambda4
                public final void runNoResult(java.lang.Object obj) throws java.lang.Exception {
                    this.f$0.lambda$getReadOnlyFeatureFileDescriptorMap$5(feature, remoteCallback, (android.service.ondeviceintelligence.IOnDeviceIntelligenceService) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getReadOnlyFeatureFileDescriptorMap$5(android.app.ondeviceintelligence.Feature feature, final android.os.RemoteCallback remoteCallback, android.service.ondeviceintelligence.IOnDeviceIntelligenceService service) throws java.lang.Exception {
            service.getReadOnlyFeatureFileDescriptorMap(feature, new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$7$$ExternalSyntheticLambda0
                public final void onResult(android.os.Bundle bundle) {
                    this.f$0.lambda$getReadOnlyFeatureFileDescriptorMap$4(remoteCallback, bundle);
                }
            }));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getReadOnlyFeatureFileDescriptorMap$4(final android.os.RemoteCallback remoteCallback, final android.os.Bundle result) {
            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.callbackExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$7$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$getReadOnlyFeatureFileDescriptorMap$3(result, remoteCallback);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getReadOnlyFeatureFileDescriptorMap$3(final android.os.Bundle result, android.os.RemoteCallback remoteCallback) {
            if (result == null) {
                try {
                    remoteCallback.sendResult((android.os.Bundle) null);
                } finally {
                    com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.resourceClosingExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$7$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            com.android.server.ondeviceintelligence.BundleUtil.tryCloseResource(result);
                        }
                    });
                }
            }
            for (java.lang.String key : result.keySet()) {
                android.os.ParcelFileDescriptor pfd = (android.os.ParcelFileDescriptor) result.getParcelable(key, android.os.ParcelFileDescriptor.class);
                com.android.server.ondeviceintelligence.BundleUtil.validatePfdReadOnly(pfd);
            }
            remoteCallback.sendResult(result);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.service.ondeviceintelligence.IRemoteStorageService.Stub getIRemoteStorageService() {
        return new com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.AnonymousClass7();
    }

    private void validateServiceElevated(java.lang.String serviceName, boolean checkIsolated) {
        try {
            if (android.text.TextUtils.isEmpty(serviceName)) {
                throw new java.lang.IllegalStateException("Remote service is not configured to complete the request");
            }
            android.content.ComponentName serviceComponent = android.content.ComponentName.unflattenFromString(serviceName);
            android.content.pm.ServiceInfo serviceInfo = android.app.AppGlobals.getPackageManager().getServiceInfo(serviceComponent, 786432L, android.os.UserHandle.SYSTEM.getIdentifier());
            if (serviceInfo != null) {
                if (!checkIsolated) {
                    checkServiceRequiresPermission(serviceInfo, "android.permission.BIND_ON_DEVICE_INTELLIGENCE_SERVICE");
                    return;
                }
                checkServiceRequiresPermission(serviceInfo, "android.permission.BIND_ON_DEVICE_SANDBOXED_INFERENCE_SERVICE");
                if (!isIsolatedService(serviceInfo)) {
                    throw new java.lang.SecurityException("Call required an isolated service, but the configured service: " + serviceName + ", is not isolated");
                }
                return;
            }
            throw new java.lang.IllegalStateException("Remote service is not configured to complete the request.");
        } catch (android.os.RemoteException e) {
            throw new java.lang.IllegalStateException("Could not fetch service info for remote services", e);
        }
    }

    private static void checkServiceRequiresPermission(android.content.pm.ServiceInfo serviceInfo, java.lang.String requiredPermission) {
        java.lang.String permission = serviceInfo.permission;
        if (!requiredPermission.equals(permission)) {
            throw new java.lang.SecurityException(java.lang.String.format("Service %s requires %s permission. Found %s permission", serviceInfo.getComponentName(), requiredPermission, serviceInfo.permission));
        }
    }

    private static boolean isIsolatedService(android.content.pm.ServiceInfo serviceInfo) {
        return (serviceInfo.flags & 2) != 0 && (serviceInfo.flags & 4) == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.app.ondeviceintelligence.InferenceInfo> getLatestInferenceInfo(long startTimeEpochMillis) {
        return this.mInferenceInfoStore.getLatestInferenceInfo(startTimeEpochMillis);
    }

    public java.lang.String getRemoteConfiguredPackageName() {
        try {
            java.lang.String[] serviceNames = getServiceNames();
            android.content.ComponentName componentName = android.content.ComponentName.unflattenFromString(serviceNames[1]);
            if (componentName != null) {
                return componentName.getPackageName();
            }
            return null;
        } catch (android.content.res.Resources.NotFoundException e) {
            android.util.Slog.e(TAG, "Could not find resource", e);
            return null;
        }
    }

    protected java.lang.String[] getServiceNames() throws android.content.res.Resources.NotFoundException {
        synchronized (this.mLock) {
            if (this.mTemporaryServiceNames != null && this.mTemporaryServiceNames.length == 2) {
                return this.mTemporaryServiceNames;
            }
            return new java.lang.String[]{this.mContext.getResources().getString(android.R.string.config_defaultSystemCaptionsManagerService), this.mContext.getResources().getString(android.R.string.config_defaultTextClassifierPackage)};
        }
    }

    protected java.lang.String[] getBroadcastKeys() throws android.content.res.Resources.NotFoundException {
        synchronized (this.mLock) {
            if (this.mTemporaryBroadcastKeys != null && this.mTemporaryBroadcastKeys.length == 2) {
                return this.mTemporaryBroadcastKeys;
            }
            return new java.lang.String[]{this.mContext.getResources().getString(android.R.string.config_platformVpnConfirmDialogComponent), this.mContext.getResources().getString(android.R.string.config_pluginsProviderJarPath)};
        }
    }

    public void setTemporaryServices(java.lang.String[] componentNames, int durationMs) {
        java.util.Objects.requireNonNull(componentNames);
        enforceShellOnly(android.os.Binder.getCallingUid(), "setTemporaryServices");
        this.mContext.enforceCallingPermission("android.permission.USE_ON_DEVICE_INTELLIGENCE", TAG);
        synchronized (this.mLock) {
            this.mTemporaryServiceNames = componentNames;
            if (this.mRemoteInferenceService != null) {
                this.mRemoteInferenceService.unbind();
                this.mRemoteInferenceService = null;
            }
            if (this.mRemoteOnDeviceIntelligenceService != null) {
                this.mRemoteOnDeviceIntelligenceService.unbind();
                this.mRemoteOnDeviceIntelligenceService = null;
            }
            if (durationMs != -1) {
                getTemporaryHandler().sendEmptyMessageDelayed(0, durationMs);
            }
        }
    }

    public void setModelBroadcastKeys(java.lang.String[] broadcastKeys, java.lang.String receiverPackageName, int durationMs) {
        java.util.Objects.requireNonNull(broadcastKeys);
        enforceShellOnly(android.os.Binder.getCallingUid(), "setModelBroadcastKeys");
        this.mContext.enforceCallingPermission("android.permission.USE_ON_DEVICE_INTELLIGENCE", TAG);
        synchronized (this.mLock) {
            this.mTemporaryBroadcastKeys = broadcastKeys;
            this.mBroadcastPackageName = receiverPackageName;
            if (durationMs != -1) {
                getTemporaryHandler().sendEmptyMessageDelayed(1, durationMs);
            }
        }
    }

    public void setTemporaryDeviceConfigNamespace(java.lang.String configNamespace, int durationMs) {
        java.util.Objects.requireNonNull(configNamespace);
        enforceShellOnly(android.os.Binder.getCallingUid(), "setTemporaryDeviceConfigNamespace");
        this.mContext.enforceCallingPermission("android.permission.USE_ON_DEVICE_INTELLIGENCE", TAG);
        synchronized (this.mLock) {
            this.mTemporaryConfigNamespace = configNamespace;
            if (durationMs != -1) {
                getTemporaryHandler().sendEmptyMessageDelayed(2, durationMs);
            }
        }
    }

    public void resetTemporaryServices() {
        synchronized (this.mLock) {
            if (this.mTemporaryHandler != null) {
                this.mTemporaryHandler.removeMessages(0);
                this.mTemporaryHandler = null;
            }
            this.mRemoteInferenceService = null;
            this.mRemoteOnDeviceIntelligenceService = null;
            this.mTemporaryServiceNames = new java.lang.String[0];
        }
    }

    public static void enforceShellOnly(int callingUid, java.lang.String message) {
        if (callingUid == 2000 || callingUid == 0) {
        } else {
            throw new java.lang.SecurityException(message + ": Only shell user can call it");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.internal.infra.AndroidFuture<android.os.IBinder> wrapCancellationFuture(final com.android.internal.infra.AndroidFuture future) {
        if (future == null) {
            return null;
        }
        com.android.internal.infra.AndroidFuture<android.os.IBinder> cancellationFuture = new com.android.internal.infra.AndroidFuture<>();
        cancellationFuture.whenCompleteAsync(new java.util.function.BiConsumer() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$wrapCancellationFuture$4(future, (android.os.IBinder) obj, (java.lang.Throwable) obj2);
            }
        });
        return cancellationFuture;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$wrapCancellationFuture$4(com.android.internal.infra.AndroidFuture future, final android.os.IBinder c, java.lang.Throwable e) {
        if (e != null) {
            android.util.Log.e(TAG, "Error forwarding ICancellationSignal to manager layer", e);
            future.completeExceptionally(e);
        } else {
            future.complete(new android.os.ICancellationSignal.Stub() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.8
                public void cancel() throws android.os.RemoteException {
                    android.os.ICancellationSignal.Stub.asInterface(c).cancel();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.internal.infra.AndroidFuture<android.os.IBinder> wrapProcessingFuture(final com.android.internal.infra.AndroidFuture future) {
        if (future == null) {
            return null;
        }
        com.android.internal.infra.AndroidFuture<android.os.IBinder> processingSignalFuture = new com.android.internal.infra.AndroidFuture<>();
        processingSignalFuture.whenCompleteAsync(new java.util.function.BiConsumer() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$wrapProcessingFuture$5(future, (android.os.IBinder) obj, (java.lang.Throwable) obj2);
            }
        });
        return processingSignalFuture;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$wrapProcessingFuture$5(com.android.internal.infra.AndroidFuture future, final android.os.IBinder c, java.lang.Throwable e) {
        if (e != null) {
            future.completeExceptionally(e);
        } else {
            future.complete(new android.app.ondeviceintelligence.IProcessingSignal.Stub() { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.9
                public void sendSignal(android.os.PersistableBundle actionParams) throws android.os.RemoteException {
                    android.app.ondeviceintelligence.IProcessingSignal.Stub.asInterface(c).sendSignal(actionParams);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void tryClosePfd(android.os.ParcelFileDescriptor pfd) {
        if (pfd != null) {
            try {
                pfd.close();
            } catch (java.io.IOException e) {
                android.util.Log.e(TAG, "Failed to close parcel file descriptor ", e);
            }
        }
    }

    private synchronized android.os.Handler getTemporaryHandler() {
        if (this.mTemporaryHandler == null) {
            this.mTemporaryHandler = new android.os.Handler(android.os.Looper.getMainLooper(), null, true) { // from class: com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.10
                @Override // android.os.Handler
                public void handleMessage(android.os.Message msg) {
                    synchronized (com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mLock) {
                        if (msg.what == 0) {
                            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.resetTemporaryServices();
                        } else if (msg.what == 1) {
                            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mTemporaryBroadcastKeys = null;
                            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mBroadcastPackageName = "android";
                        } else if (msg.what == 2) {
                            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.this.mTemporaryConfigNamespace = null;
                        } else {
                            android.util.Slog.wtf(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.TAG, "invalid handler msg: " + msg);
                        }
                    }
                }
            };
        }
        return this.mTemporaryHandler;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getIdleTimeoutMs() {
        return android.provider.Settings.Secure.getLongForUser(this.mContext.getContentResolver(), "on_device_intelligence_idle_timeout_ms", java.util.concurrent.TimeUnit.HOURS.toMillis(1L), this.mContext.getUserId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getRemoteInferenceServiceUid() {
        int i;
        synchronized (this.mLock) {
            i = this.remoteInferenceServiceUid;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRemoteInferenceServiceUid(int remoteInferenceServiceUid) {
        synchronized (this.mLock) {
            this.remoteInferenceServiceUid = remoteInferenceServiceUid;
        }
    }
}
