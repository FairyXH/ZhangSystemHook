package com.android.server.wearable;

/* JADX INFO: loaded from: classes3.dex */
final class RemoteWearableSensingService extends com.android.internal.infra.ServiceConnector.Impl<android.service.wearable.IWearableSensingService> {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = com.android.server.wearable.RemoteWearableSensingService.class.getSimpleName();
    private com.android.server.wearable.RemoteWearableSensingService.SecureWearableConnectionContext mNextSecureConnectionContext;
    private final java.lang.Object mSecureConnectionLock;
    private boolean mSecureConnectionProvided;

    RemoteWearableSensingService(android.content.Context context, android.content.ComponentName serviceName, int userId) {
        super(context, new android.content.Intent("android.service.wearable.WearableSensingService").setComponent(serviceName), 67112960, userId, new com.android.server.ambientcontext.RemoteWearableSensingService$$ExternalSyntheticLambda1());
        this.mSecureConnectionLock = new java.lang.Object();
        this.mSecureConnectionProvided = false;
        connect();
    }

    protected long getAutoDisconnectTimeoutMs() {
        return -1L;
    }

    public void provideSecureConnection(android.os.ParcelFileDescriptor secureWearableConnection, android.app.wearable.IWearableSensingCallback wearableSensingCallback, android.os.RemoteCallback statusCallback) {
        if (!com.android.internal.hidden_from_bootclasspath.android.app.wearable.Flags.enableRestartWssProcess()) {
            android.util.Slog.d(TAG, "FLAG_ENABLE_RESTART_WSS_PROCESS is disabled. Do not attempt to restart the WearableSensingService process");
            provideSecureConnectionInternal(secureWearableConnection, wearableSensingCallback, statusCallback);
            return;
        }
        synchronized (this.mSecureConnectionLock) {
            if (this.mNextSecureConnectionContext != null) {
                android.util.Slog.i(TAG, "A new wearable connection is provided before the process restart triggered by the previous connection is complete. Discarding the previous connection.");
                if (com.android.internal.hidden_from_bootclasspath.android.app.wearable.Flags.enableProvideWearableConnectionApi()) {
                    com.android.server.wearable.WearableSensingManagerPerUserService.notifyStatusCallback(this.mNextSecureConnectionContext.mStatusCallback, 7);
                }
                this.mNextSecureConnectionContext = new com.android.server.wearable.RemoteWearableSensingService.SecureWearableConnectionContext(secureWearableConnection, wearableSensingCallback, statusCallback);
                return;
            }
            if (!this.mSecureConnectionProvided) {
                provideSecureConnectionInternal(secureWearableConnection, wearableSensingCallback, statusCallback);
                this.mSecureConnectionProvided = true;
            } else {
                this.mNextSecureConnectionContext = new com.android.server.wearable.RemoteWearableSensingService.SecureWearableConnectionContext(secureWearableConnection, wearableSensingCallback, statusCallback);
                killWearableSensingServiceProcess();
            }
        }
    }

    private void provideSecureConnectionInternal(final android.os.ParcelFileDescriptor secureWearableConnection, final android.app.wearable.IWearableSensingCallback wearableSensingCallback, final android.os.RemoteCallback statusCallback) {
        android.util.Slog.d(TAG, "Providing secure wearable connection.");
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.wearable.RemoteWearableSensingService$$ExternalSyntheticLambda4
            public final void runNoResult(java.lang.Object obj) throws java.lang.Exception {
                com.android.server.wearable.RemoteWearableSensingService.lambda$provideSecureConnectionInternal$0(secureWearableConnection, wearableSensingCallback, statusCallback, (android.service.wearable.IWearableSensingService) obj);
            }
        });
    }

    static /* synthetic */ void lambda$provideSecureConnectionInternal$0(android.os.ParcelFileDescriptor secureWearableConnection, android.app.wearable.IWearableSensingCallback wearableSensingCallback, android.os.RemoteCallback statusCallback, android.service.wearable.IWearableSensingService service) throws java.lang.Exception {
        service.provideSecureConnection(secureWearableConnection, wearableSensingCallback, statusCallback);
        try {
            secureWearableConnection.close();
        } catch (java.io.IOException ex) {
            android.util.Slog.w(TAG, "Unable to close the local parcelFileDescriptor.", ex);
        }
    }

    public void binderDied() {
        super.binderDied();
        synchronized (this.mSecureConnectionLock) {
            if (this.mNextSecureConnectionContext != null) {
                provideSecureConnectionInternal(this.mNextSecureConnectionContext.mSecureConnection, this.mNextSecureConnectionContext.mWearableSensingCallback, this.mNextSecureConnectionContext.mStatusCallback);
                this.mNextSecureConnectionContext = null;
            } else {
                this.mSecureConnectionProvided = false;
                android.util.Slog.w(TAG, "Binder died but there is no secure wearable connection to provide.");
            }
        }
    }

    public void killWearableSensingServiceProcess() {
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.wearable.RemoteWearableSensingService$$ExternalSyntheticLambda0
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.wearable.IWearableSensingService) obj).killProcess();
            }
        });
    }

    public void provideDataStream(final android.os.ParcelFileDescriptor parcelFileDescriptor, final android.app.wearable.IWearableSensingCallback wearableSensingCallback, final android.os.RemoteCallback callback) {
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.wearable.RemoteWearableSensingService$$ExternalSyntheticLambda3
            public final void runNoResult(java.lang.Object obj) throws java.lang.Exception {
                com.android.server.wearable.RemoteWearableSensingService.lambda$provideDataStream$2(parcelFileDescriptor, wearableSensingCallback, callback, (android.service.wearable.IWearableSensingService) obj);
            }
        });
    }

    static /* synthetic */ void lambda$provideDataStream$2(android.os.ParcelFileDescriptor parcelFileDescriptor, android.app.wearable.IWearableSensingCallback wearableSensingCallback, android.os.RemoteCallback callback, android.service.wearable.IWearableSensingService service) throws java.lang.Exception {
        service.provideDataStream(parcelFileDescriptor, wearableSensingCallback, callback);
        try {
            parcelFileDescriptor.close();
        } catch (java.io.IOException ex) {
            android.util.Slog.w(TAG, "Unable to close the local parcelFileDescriptor.", ex);
        }
    }

    public void provideData(final android.os.PersistableBundle data, final android.os.SharedMemory sharedMemory, final android.os.RemoteCallback callback) {
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.wearable.RemoteWearableSensingService$$ExternalSyntheticLambda8
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.wearable.IWearableSensingService) obj).provideData(data, sharedMemory, callback);
            }
        });
    }

    public void registerDataRequestObserver(final int dataType, final android.os.RemoteCallback dataRequestCallback, final int dataRequestObserverId, final java.lang.String packageName, final android.os.RemoteCallback statusCallback) {
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.wearable.RemoteWearableSensingService$$ExternalSyntheticLambda5
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.wearable.IWearableSensingService) obj).registerDataRequestObserver(dataType, dataRequestCallback, dataRequestObserverId, packageName, statusCallback);
            }
        });
    }

    public void unregisterDataRequestObserver(final int dataType, final int dataRequestObserverId, final java.lang.String packageName, final android.os.RemoteCallback statusCallback) {
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.wearable.RemoteWearableSensingService$$ExternalSyntheticLambda9
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.wearable.IWearableSensingService) obj).unregisterDataRequestObserver(dataType, dataRequestObserverId, packageName, statusCallback);
            }
        });
    }

    public void startHotwordRecognition(final android.os.RemoteCallback wearableHotwordCallback, final android.os.RemoteCallback statusCallback) {
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.wearable.RemoteWearableSensingService$$ExternalSyntheticLambda2
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.wearable.IWearableSensingService) obj).startHotwordRecognition(wearableHotwordCallback, statusCallback);
            }
        });
    }

    public void stopHotwordRecognition(final android.os.RemoteCallback statusCallback) {
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.wearable.RemoteWearableSensingService$$ExternalSyntheticLambda6
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.wearable.IWearableSensingService) obj).stopHotwordRecognition(statusCallback);
            }
        });
    }

    public void onValidatedByHotwordDetectionService() {
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.wearable.RemoteWearableSensingService$$ExternalSyntheticLambda1
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.wearable.IWearableSensingService) obj).onValidatedByHotwordDetectionService();
            }
        });
    }

    public void stopActiveHotwordAudio() {
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.wearable.RemoteWearableSensingService$$ExternalSyntheticLambda7
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.wearable.IWearableSensingService) obj).stopActiveHotwordAudio();
            }
        });
    }

    private static class SecureWearableConnectionContext {
        final android.os.ParcelFileDescriptor mSecureConnection;
        final android.os.RemoteCallback mStatusCallback;
        final android.app.wearable.IWearableSensingCallback mWearableSensingCallback;

        SecureWearableConnectionContext(android.os.ParcelFileDescriptor secureWearableConnection, android.app.wearable.IWearableSensingCallback wearableSensingCallback, android.os.RemoteCallback statusCallback) {
            this.mSecureConnection = secureWearableConnection;
            this.mWearableSensingCallback = wearableSensingCallback;
            this.mStatusCallback = statusCallback;
        }
    }
}
