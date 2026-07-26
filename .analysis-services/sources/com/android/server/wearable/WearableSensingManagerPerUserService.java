package com.android.server.wearable;

/* JADX INFO: loaded from: classes3.dex */
final class WearableSensingManagerPerUserService extends com.android.server.infra.AbstractPerUserSystemService<com.android.server.wearable.WearableSensingManagerPerUserService, com.android.server.wearable.WearableSensingManagerService> {
    private static final java.lang.String TAG = com.android.server.wearable.WearableSensingManagerPerUserService.class.getSimpleName();
    private android.content.ComponentName mComponentName;
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    com.android.server.wearable.RemoteWearableSensingService mRemoteService;
    private com.android.server.wearable.WearableSensingSecureChannel mSecureChannel;
    private final java.lang.Object mSecureChannelLock;
    private android.service.voice.VoiceInteractionManagerInternal mVoiceInteractionManagerInternal;

    WearableSensingManagerPerUserService(com.android.server.wearable.WearableSensingManagerService master, java.lang.Object lock, int userId) {
        super(master, lock, userId);
        this.mSecureChannelLock = new java.lang.Object();
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
    }

    public static void notifyStatusCallback(android.os.RemoteCallback statusCallback, int statusCode) {
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putInt("android.app.wearable.WearableSensingStatusBundleKey", statusCode);
        statusCallback.sendResult(bundle);
    }

    void destroyLocked() {
        android.util.Slog.d(TAG, "Trying to cancel the remote request. Reason: Service destroyed.");
        if (this.mRemoteService != null) {
            synchronized (this.mLock) {
                this.mRemoteService.unbind();
                this.mRemoteService = null;
            }
        }
        synchronized (this.mSecureChannelLock) {
            if (this.mSecureChannel != null) {
                this.mSecureChannel.close();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureRemoteServiceInitiated() {
        if (this.mRemoteService == null) {
            this.mRemoteService = new com.android.server.wearable.RemoteWearableSensingService(getContext(), this.mComponentName, getUserId());
        }
    }

    private boolean ensureVoiceInteractionManagerInternalInitiated() {
        if (this.mVoiceInteractionManagerInternal == null) {
            this.mVoiceInteractionManagerInternal = (android.service.voice.VoiceInteractionManagerInternal) com.android.server.LocalServices.getService(android.service.voice.VoiceInteractionManagerInternal.class);
        }
        return this.mVoiceInteractionManagerInternal != null;
    }

    android.content.ComponentName getComponentName() {
        return this.mComponentName;
    }

    boolean setUpServiceIfNeeded() {
        if (this.mComponentName == null) {
            this.mComponentName = updateServiceInfoLocked();
        }
        if (this.mComponentName == null) {
            return false;
        }
        try {
            android.content.pm.ServiceInfo serviceInfo = android.app.AppGlobals.getPackageManager().getServiceInfo(this.mComponentName, 0L, this.mUserId);
            return serviceInfo != null;
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "RemoteException while setting up service");
            return false;
        }
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected android.content.pm.ServiceInfo newServiceInfoLocked(android.content.ComponentName serviceComponent) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            android.content.pm.ServiceInfo serviceInfo = android.app.AppGlobals.getPackageManager().getServiceInfo(serviceComponent, 0L, this.mUserId);
            if (serviceInfo != null) {
                java.lang.String permission = serviceInfo.permission;
                if (!"android.permission.BIND_WEARABLE_SENSING_SERVICE".equals(permission)) {
                    throw new java.lang.SecurityException(java.lang.String.format("Service %s requires %s permission. Found %s permission", serviceInfo.getComponentName(), "android.permission.BIND_WEARABLE_SENSING_SERVICE", serviceInfo.permission));
                }
            }
            return serviceInfo;
        } catch (android.os.RemoteException e) {
            throw new android.content.pm.PackageManager.NameNotFoundException("Could not get service for " + serviceComponent);
        }
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected void dumpLocked(java.lang.String prefix, java.io.PrintWriter pw) {
        synchronized (this.mLock) {
            super.dumpLocked(prefix, pw);
        }
        if (this.mRemoteService != null) {
            this.mRemoteService.dump("", new android.util.IndentingPrintWriter(pw, "  "));
        }
    }

    public void onProvideConnection(android.os.ParcelFileDescriptor wearableConnection, android.app.wearable.IWearableSensingCallback wearableSensingCallback, final android.os.RemoteCallback statusCallback) {
        android.util.Slog.i(TAG, "onProvideConnection in per user service.");
        synchronized (this.mLock) {
            if (!setUpServiceIfNeeded()) {
                android.util.Slog.w(TAG, "Detection service is not available at this moment.");
                notifyStatusCallback(statusCallback, 3);
                return;
            }
            final android.app.wearable.IWearableSensingCallback wrappedWearableSensingCallback = wrapWearableSensingCallback(wearableSensingCallback);
            synchronized (this.mSecureChannelLock) {
                if (this.mSecureChannel != null) {
                    this.mSecureChannel.close();
                }
                try {
                    final java.util.concurrent.atomic.AtomicReference<com.android.server.wearable.WearableSensingSecureChannel> currentSecureChannelRef = new java.util.concurrent.atomic.AtomicReference<>();
                    this.mSecureChannel = com.android.server.wearable.WearableSensingSecureChannel.create((android.companion.CompanionDeviceManager) getContext().getSystemService(android.companion.CompanionDeviceManager.class), wearableConnection, new com.android.server.wearable.WearableSensingSecureChannel.SecureTransportListener() { // from class: com.android.server.wearable.WearableSensingManagerPerUserService.1
                        @Override // com.android.server.wearable.WearableSensingSecureChannel.SecureTransportListener
                        public void onSecureTransportAvailable(android.os.ParcelFileDescriptor secureTransport) {
                            android.util.Slog.i(com.android.server.wearable.WearableSensingManagerPerUserService.TAG, "calling over to remote service.");
                            synchronized (com.android.server.wearable.WearableSensingManagerPerUserService.this.mLock) {
                                com.android.server.wearable.WearableSensingManagerPerUserService.this.ensureRemoteServiceInitiated();
                                com.android.server.wearable.WearableSensingManagerPerUserService.this.mRemoteService.provideSecureConnection(secureTransport, wrappedWearableSensingCallback, statusCallback);
                            }
                        }

                        @Override // com.android.server.wearable.WearableSensingSecureChannel.SecureTransportListener
                        public void onError() {
                            if (com.android.internal.hidden_from_bootclasspath.android.app.wearable.Flags.enableRestartWssProcess()) {
                                synchronized (com.android.server.wearable.WearableSensingManagerPerUserService.this.mSecureChannelLock) {
                                    if (com.android.server.wearable.WearableSensingManagerPerUserService.this.mSecureChannel != null && com.android.server.wearable.WearableSensingManagerPerUserService.this.mSecureChannel == currentSecureChannelRef.get()) {
                                        com.android.server.wearable.WearableSensingManagerPerUserService.this.mRemoteService.killWearableSensingServiceProcess();
                                        com.android.server.wearable.WearableSensingManagerPerUserService.this.mSecureChannel = null;
                                    }
                                }
                            }
                            if (com.android.internal.hidden_from_bootclasspath.android.app.wearable.Flags.enableProvideWearableConnectionApi()) {
                                com.android.server.wearable.WearableSensingManagerPerUserService.notifyStatusCallback(statusCallback, 7);
                            }
                        }
                    });
                    currentSecureChannelRef.set(this.mSecureChannel);
                } catch (java.io.IOException ex) {
                    android.util.Slog.e(TAG, "Unable to create the secure channel.", ex);
                    if (com.android.internal.hidden_from_bootclasspath.android.app.wearable.Flags.enableProvideWearableConnectionApi()) {
                        notifyStatusCallback(statusCallback, 7);
                    }
                }
            }
        }
    }

    public void onProvideDataStream(android.os.ParcelFileDescriptor parcelFileDescriptor, android.app.wearable.IWearableSensingCallback wearableSensingCallback, android.os.RemoteCallback statusCallback) {
        android.util.Slog.i(TAG, "onProvideDataStream in per user service. Is data stream read-only? " + isReadOnly(parcelFileDescriptor));
        synchronized (this.mLock) {
            if (!setUpServiceIfNeeded()) {
                android.util.Slog.w(TAG, "Detection service is not available at this moment.");
                notifyStatusCallback(statusCallback, 3);
            } else {
                android.util.Slog.i(TAG, "calling over to remote servvice.");
                ensureRemoteServiceInitiated();
                this.mRemoteService.provideDataStream(parcelFileDescriptor, wrapWearableSensingCallback(wearableSensingCallback), statusCallback);
            }
        }
    }

    public void onProvidedData(android.os.PersistableBundle data, android.os.SharedMemory sharedMemory, android.os.RemoteCallback callback) {
        synchronized (this.mLock) {
            if (!setUpServiceIfNeeded()) {
                android.util.Slog.w(TAG, "Detection service is not available at this moment.");
                notifyStatusCallback(callback, 3);
            } else {
                ensureRemoteServiceInitiated();
                if (sharedMemory != null) {
                    sharedMemory.setProtect(android.system.OsConstants.PROT_READ);
                }
                this.mRemoteService.provideData(data, sharedMemory, callback);
            }
        }
    }

    public void onRegisterDataRequestObserver(int dataType, android.os.RemoteCallback dataRequestObserver, int dataRequestObserverId, java.lang.String packageName, android.os.RemoteCallback statusCallback) {
        synchronized (this.mLock) {
            if (!setUpServiceIfNeeded()) {
                android.util.Slog.w(TAG, "Detection service is not available at this moment.");
                notifyStatusCallback(statusCallback, 3);
            } else {
                ensureRemoteServiceInitiated();
                this.mRemoteService.registerDataRequestObserver(dataType, dataRequestObserver, dataRequestObserverId, packageName, statusCallback);
            }
        }
    }

    public void onUnregisterDataRequestObserver(int dataType, int dataRequestObserverId, java.lang.String packageName, android.os.RemoteCallback statusCallback) {
        synchronized (this.mLock) {
            if (!setUpServiceIfNeeded()) {
                android.util.Slog.w(TAG, "Detection service is not available at this moment.");
                notifyStatusCallback(statusCallback, 3);
            } else {
                ensureRemoteServiceInitiated();
                this.mRemoteService.unregisterDataRequestObserver(dataType, dataRequestObserverId, packageName, statusCallback);
            }
        }
    }

    public void onStartHotwordRecognition(android.content.ComponentName targetVisComponentName, android.os.RemoteCallback statusCallback) {
        synchronized (this.mLock) {
            if (!setUpServiceIfNeeded()) {
                android.util.Slog.w(TAG, "Detection service is not available at this moment.");
                notifyStatusCallback(statusCallback, 3);
            } else if (!ensureVoiceInteractionManagerInternalInitiated()) {
                android.util.Slog.w(TAG, "Voice interaction manager is not available at this moment.");
                notifyStatusCallback(statusCallback, 3);
            } else {
                ensureRemoteServiceInitiated();
                this.mRemoteService.startHotwordRecognition(createWearableHotwordCallback(targetVisComponentName), statusCallback);
            }
        }
    }

    public void onStopHotwordRecognition(android.os.RemoteCallback statusCallback) {
        synchronized (this.mLock) {
            if (!setUpServiceIfNeeded()) {
                android.util.Slog.w(TAG, "Detection service is not available at this moment.");
                notifyStatusCallback(statusCallback, 3);
            } else {
                ensureRemoteServiceInitiated();
                this.mRemoteService.stopHotwordRecognition(statusCallback);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onValidatedByHotwordDetectionService() {
        synchronized (this.mLock) {
            if (!setUpServiceIfNeeded()) {
                android.util.Slog.w(TAG, "Wearable sensing service is not available at this moment.");
            } else {
                ensureRemoteServiceInitiated();
                this.mRemoteService.onValidatedByHotwordDetectionService();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopActiveHotwordAudio() {
        synchronized (this.mLock) {
            if (!setUpServiceIfNeeded()) {
                android.util.Slog.w(TAG, "Wearable sensing service is not available at this moment.");
            } else {
                ensureRemoteServiceInitiated();
                this.mRemoteService.stopActiveHotwordAudio();
            }
        }
    }

    private android.os.RemoteCallback createWearableHotwordCallback(final android.content.ComponentName targetVisComponentName) {
        return new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.wearable.WearableSensingManagerPerUserService$$ExternalSyntheticLambda0
            public final void onResult(android.os.Bundle bundle) {
                this.f$0.lambda$createWearableHotwordCallback$0(targetVisComponentName, bundle);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createWearableHotwordCallback$0(android.content.ComponentName targetVisComponentName, android.os.Bundle result) {
        android.service.voice.HotwordAudioStream hotwordAudioStream = (android.service.voice.HotwordAudioStream) result.getParcelable("android.app.wearable.HotwordAudioStreamBundleKey", android.service.voice.HotwordAudioStream.class);
        if (hotwordAudioStream == null) {
            android.util.Slog.w(TAG, "No hotword audio stream received, unable to process hotword.");
            return;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mVoiceInteractionManagerInternal.startListeningFromWearable(hotwordAudioStream.getAudioStreamParcelFileDescriptor(), hotwordAudioStream.getAudioFormat(), hotwordAudioStream.getMetadata(), targetVisComponentName, getUserId(), createHotwordDetectionCallback());
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private android.service.voice.VoiceInteractionManagerInternal.WearableHotwordDetectionCallback createHotwordDetectionCallback() {
        return new android.service.voice.VoiceInteractionManagerInternal.WearableHotwordDetectionCallback() { // from class: com.android.server.wearable.WearableSensingManagerPerUserService.2
            public void onDetected() {
                android.util.Slog.i(com.android.server.wearable.WearableSensingManagerPerUserService.TAG, "hotwordDetectionCallback onDetected.");
                com.android.server.wearable.WearableSensingManagerPerUserService.this.onValidatedByHotwordDetectionService();
            }

            public void onRejected() {
                android.util.Slog.i(com.android.server.wearable.WearableSensingManagerPerUserService.TAG, "hotwordDetectionCallback onRejected.");
                com.android.server.wearable.WearableSensingManagerPerUserService.this.stopActiveHotwordAudio();
            }

            public void onError(java.lang.String errorMessage) {
                android.util.Slog.i(com.android.server.wearable.WearableSensingManagerPerUserService.TAG, "hotwordDetectionCallback onError. ErrorMessage: " + errorMessage);
                com.android.server.wearable.WearableSensingManagerPerUserService.this.stopActiveHotwordAudio();
            }
        };
    }

    private android.app.wearable.IWearableSensingCallback wrapWearableSensingCallback(android.app.wearable.IWearableSensingCallback callbackFromAppProcess) {
        if (callbackFromAppProcess == null) {
            return null;
        }
        if (this.mComponentName == null) {
            android.util.Slog.w(TAG, "Cannot create WearableSensingCallback because mComponentName is null.");
            return null;
        }
        if (android.os.Binder.getCallingUid() != this.mPackageManagerInternal.getPackageUid(this.mComponentName.getPackageName(), 0L, this.mUserId)) {
            android.util.Slog.d(TAG, "Caller does not belong to the package that provides the WearableSensingService implementation. Do not forward WearableSensingCallback to WearableSensingService.");
            return null;
        }
        return new com.android.server.wearable.WearableSensingManagerPerUserService.AnonymousClass3(callbackFromAppProcess);
    }

    /* JADX INFO: renamed from: com.android.server.wearable.WearableSensingManagerPerUserService$3, reason: invalid class name */
    class AnonymousClass3 extends android.app.wearable.IWearableSensingCallback.Stub {
        final /* synthetic */ android.app.wearable.IWearableSensingCallback val$callbackFromAppProcess;

        AnonymousClass3(android.app.wearable.IWearableSensingCallback iWearableSensingCallback) {
            this.val$callbackFromAppProcess = iWearableSensingCallback;
        }

        public void openFile(final java.lang.String filename, final com.android.internal.infra.AndroidFuture<android.os.ParcelFileDescriptor> futureFromWearableSensingService) throws android.os.RemoteException {
            com.android.internal.infra.AndroidFuture<android.os.ParcelFileDescriptor> futureFromSystemServer = new com.android.internal.infra.AndroidFuture().whenComplete(new java.util.function.BiConsumer() { // from class: com.android.server.wearable.WearableSensingManagerPerUserService$3$$ExternalSyntheticLambda0
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    com.android.server.wearable.WearableSensingManagerPerUserService.AnonymousClass3.lambda$openFile$0(filename, futureFromWearableSensingService, (android.os.ParcelFileDescriptor) obj, (java.lang.Throwable) obj2);
                }
            });
            this.val$callbackFromAppProcess.openFile(filename, futureFromSystemServer);
        }

        static /* synthetic */ void lambda$openFile$0(java.lang.String filename, com.android.internal.infra.AndroidFuture futureFromWearableSensingService, android.os.ParcelFileDescriptor pfdFromApp, java.lang.Throwable throwable) {
            if (throwable != null) {
                android.util.Slog.e(com.android.server.wearable.WearableSensingManagerPerUserService.TAG, "Error when reading file " + filename, throwable);
                futureFromWearableSensingService.complete((java.lang.Object) null);
            } else if (pfdFromApp == null) {
                futureFromWearableSensingService.complete((java.lang.Object) null);
            } else if (com.android.server.wearable.WearableSensingManagerPerUserService.isReadOnly(pfdFromApp)) {
                futureFromWearableSensingService.complete(pfdFromApp);
            } else {
                android.util.Slog.w(com.android.server.wearable.WearableSensingManagerPerUserService.TAG, "Received writable ParcelFileDescriptor from app process. To prevent arbitrary data egress, sending null to WearableSensingService instead.");
                futureFromWearableSensingService.complete((java.lang.Object) null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isReadOnly(android.os.ParcelFileDescriptor parcelFileDescriptor) {
        try {
            int readMode = android.system.Os.fcntlInt(parcelFileDescriptor.getFileDescriptor(), android.system.OsConstants.F_GETFL, 0) & android.system.OsConstants.O_ACCMODE;
            return readMode == android.system.OsConstants.O_RDONLY;
        } catch (android.system.ErrnoException ex) {
            android.util.Slog.w(TAG, "Error encountered when trying to determine if the parcelFileDescriptor is read-only. Treating it as not read-only", ex);
            return false;
        }
    }
}
