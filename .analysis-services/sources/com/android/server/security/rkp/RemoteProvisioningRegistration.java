package com.android.server.security.rkp;

/* JADX INFO: loaded from: classes3.dex */
final class RemoteProvisioningRegistration extends android.security.rkp.IRegistration.Stub {
    static final java.lang.String TAG = "RemoteProvisionSysSvc";
    private final java.util.concurrent.Executor mExecutor;
    private final android.security.rkp.service.RegistrationProxy mRegistration;
    private final java.util.concurrent.ConcurrentHashMap<android.os.IBinder, android.os.CancellationSignal> mGetKeyOperations = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.Set<android.os.IBinder> mStoreUpgradedKeyOperations = java.util.concurrent.ConcurrentHashMap.newKeySet();

    interface CallbackRunner {
        void run() throws java.lang.Exception;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class GetKeyReceiver implements android.os.OutcomeReceiver<android.security.rkp.service.RemotelyProvisionedKey, java.lang.Exception> {
        android.security.rkp.IGetKeyCallback mCallback;

        GetKeyReceiver(android.security.rkp.IGetKeyCallback callback) {
            this.mCallback = callback;
        }

        @Override // android.os.OutcomeReceiver
        public void onResult(android.security.rkp.service.RemotelyProvisionedKey result) {
            com.android.server.security.rkp.RemoteProvisioningRegistration.this.mGetKeyOperations.remove(this.mCallback.asBinder());
            android.util.Log.i("RemoteProvisionSysSvc", "Successfully fetched key for client " + this.mCallback.asBinder().hashCode());
            final android.security.rkp.RemotelyProvisionedKey parcelable = new android.security.rkp.RemotelyProvisionedKey();
            parcelable.keyBlob = result.getKeyBlob();
            parcelable.encodedCertChain = result.getEncodedCertChain();
            com.android.server.security.rkp.RemoteProvisioningRegistration.this.wrapCallback(new com.android.server.security.rkp.RemoteProvisioningRegistration.CallbackRunner() { // from class: com.android.server.security.rkp.RemoteProvisioningRegistration$GetKeyReceiver$$ExternalSyntheticLambda0
                @Override // com.android.server.security.rkp.RemoteProvisioningRegistration.CallbackRunner
                public final void run() throws java.lang.Exception {
                    this.f$0.lambda$onResult$0(parcelable);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onResult$0(android.security.rkp.RemotelyProvisionedKey parcelable) throws java.lang.Exception {
            this.mCallback.onSuccess(parcelable);
        }

        @Override // android.os.OutcomeReceiver
        public void onError(final java.lang.Exception e) {
            com.android.server.security.rkp.RemoteProvisioningRegistration.this.mGetKeyOperations.remove(this.mCallback.asBinder());
            if (e instanceof android.os.OperationCanceledException) {
                android.util.Log.i("RemoteProvisionSysSvc", "Operation cancelled for client " + this.mCallback.asBinder().hashCode());
                com.android.server.security.rkp.RemoteProvisioningRegistration remoteProvisioningRegistration = com.android.server.security.rkp.RemoteProvisioningRegistration.this;
                final android.security.rkp.IGetKeyCallback iGetKeyCallback = this.mCallback;
                java.util.Objects.requireNonNull(iGetKeyCallback);
                remoteProvisioningRegistration.wrapCallback(new com.android.server.security.rkp.RemoteProvisioningRegistration.CallbackRunner() { // from class: com.android.server.security.rkp.RemoteProvisioningRegistration$GetKeyReceiver$$ExternalSyntheticLambda1
                    @Override // com.android.server.security.rkp.RemoteProvisioningRegistration.CallbackRunner
                    public final void run() {
                        iGetKeyCallback.onCancel();
                    }
                });
                return;
            }
            if (e instanceof android.security.rkp.service.RkpProxyException) {
                android.util.Log.e("RemoteProvisionSysSvc", "RKP error fetching key for client " + this.mCallback.asBinder().hashCode() + ": " + e.getMessage());
                final android.security.rkp.service.RkpProxyException rkpException = (android.security.rkp.service.RkpProxyException) e;
                com.android.server.security.rkp.RemoteProvisioningRegistration.this.wrapCallback(new com.android.server.security.rkp.RemoteProvisioningRegistration.CallbackRunner() { // from class: com.android.server.security.rkp.RemoteProvisioningRegistration$GetKeyReceiver$$ExternalSyntheticLambda2
                    @Override // com.android.server.security.rkp.RemoteProvisioningRegistration.CallbackRunner
                    public final void run() throws java.lang.Exception {
                        this.f$0.lambda$onError$1(rkpException, e);
                    }
                });
            } else {
                android.util.Log.e("RemoteProvisionSysSvc", "Unknown error fetching key for client " + this.mCallback.asBinder().hashCode() + ": " + e.getMessage());
                com.android.server.security.rkp.RemoteProvisioningRegistration.this.wrapCallback(new com.android.server.security.rkp.RemoteProvisioningRegistration.CallbackRunner() { // from class: com.android.server.security.rkp.RemoteProvisioningRegistration$GetKeyReceiver$$ExternalSyntheticLambda3
                    @Override // com.android.server.security.rkp.RemoteProvisioningRegistration.CallbackRunner
                    public final void run() throws java.lang.Exception {
                        this.f$0.lambda$onError$2(e);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onError$1(android.security.rkp.service.RkpProxyException rkpException, java.lang.Exception e) throws java.lang.Exception {
            this.mCallback.onError(com.android.server.security.rkp.RemoteProvisioningRegistration.this.toGetKeyError(rkpException), e.getMessage());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onError$2(java.lang.Exception e) throws java.lang.Exception {
            this.mCallback.onError((byte) 1, e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public byte toGetKeyError(android.security.rkp.service.RkpProxyException exception) {
        switch (exception.getError()) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                android.util.Log.e("RemoteProvisionSysSvc", "Unexpected error code in RkpProxyException", exception);
                break;
        }
        return (byte) 1;
    }

    RemoteProvisioningRegistration(android.security.rkp.service.RegistrationProxy registration, java.util.concurrent.Executor executor) {
        this.mRegistration = registration;
        this.mExecutor = executor;
    }

    public void getKey(int keyId, final android.security.rkp.IGetKeyCallback callback) {
        android.os.CancellationSignal cancellationSignal = new android.os.CancellationSignal();
        if (this.mGetKeyOperations.putIfAbsent(callback.asBinder(), cancellationSignal) != null) {
            android.util.Log.e("RemoteProvisionSysSvc", "Client can only request one call at a time " + callback.asBinder().hashCode());
            throw new java.lang.IllegalArgumentException("Callback is already associated with an existing operation: " + callback.asBinder().hashCode());
        }
        try {
            android.util.Log.i("RemoteProvisionSysSvc", "Fetching key " + keyId + " for client " + callback.asBinder().hashCode());
            this.mRegistration.getKeyAsync(keyId, cancellationSignal, this.mExecutor, new com.android.server.security.rkp.RemoteProvisioningRegistration.GetKeyReceiver(callback));
        } catch (java.lang.Exception e) {
            android.util.Log.e("RemoteProvisionSysSvc", "getKeyAsync threw an exception for client " + callback.asBinder().hashCode(), e);
            this.mGetKeyOperations.remove(callback.asBinder());
            wrapCallback(new com.android.server.security.rkp.RemoteProvisioningRegistration.CallbackRunner() { // from class: com.android.server.security.rkp.RemoteProvisioningRegistration$$ExternalSyntheticLambda0
                @Override // com.android.server.security.rkp.RemoteProvisioningRegistration.CallbackRunner
                public final void run() {
                    callback.onError((byte) 1, e.getMessage());
                }
            });
        }
    }

    public void cancelGetKey(android.security.rkp.IGetKeyCallback callback) {
        android.os.CancellationSignal cancellationSignal = this.mGetKeyOperations.remove(callback.asBinder());
        if (cancellationSignal == null) {
            throw new java.lang.IllegalArgumentException("Invalid client in cancelGetKey: " + callback.asBinder().hashCode());
        }
        android.util.Log.i("RemoteProvisionSysSvc", "Requesting cancellation for client " + callback.asBinder().hashCode());
        cancellationSignal.cancel();
    }

    public void storeUpgradedKeyAsync(byte[] oldKeyBlob, byte[] newKeyBlob, final android.security.rkp.IStoreUpgradedKeyCallback callback) {
        if (!this.mStoreUpgradedKeyOperations.add(callback.asBinder())) {
            throw new java.lang.IllegalArgumentException("Callback is already associated with an existing operation: " + callback.asBinder().hashCode());
        }
        try {
            this.mRegistration.storeUpgradedKeyAsync(oldKeyBlob, newKeyBlob, this.mExecutor, new com.android.server.security.rkp.RemoteProvisioningRegistration.AnonymousClass1(callback));
        } catch (java.lang.Exception e) {
            android.util.Log.e("RemoteProvisionSysSvc", "storeUpgradedKeyAsync threw an exception for client " + callback.asBinder().hashCode(), e);
            this.mStoreUpgradedKeyOperations.remove(callback.asBinder());
            wrapCallback(new com.android.server.security.rkp.RemoteProvisioningRegistration.CallbackRunner() { // from class: com.android.server.security.rkp.RemoteProvisioningRegistration$$ExternalSyntheticLambda1
                @Override // com.android.server.security.rkp.RemoteProvisioningRegistration.CallbackRunner
                public final void run() {
                    callback.onError(e.getMessage());
                }
            });
        }
    }

    /* JADX INFO: renamed from: com.android.server.security.rkp.RemoteProvisioningRegistration$1, reason: invalid class name */
    class AnonymousClass1 implements android.os.OutcomeReceiver<java.lang.Void, java.lang.Exception> {
        final /* synthetic */ android.security.rkp.IStoreUpgradedKeyCallback val$callback;

        AnonymousClass1(android.security.rkp.IStoreUpgradedKeyCallback iStoreUpgradedKeyCallback) {
            this.val$callback = iStoreUpgradedKeyCallback;
        }

        @Override // android.os.OutcomeReceiver
        public void onResult(java.lang.Void result) {
            com.android.server.security.rkp.RemoteProvisioningRegistration.this.mStoreUpgradedKeyOperations.remove(this.val$callback.asBinder());
            com.android.server.security.rkp.RemoteProvisioningRegistration remoteProvisioningRegistration = com.android.server.security.rkp.RemoteProvisioningRegistration.this;
            final android.security.rkp.IStoreUpgradedKeyCallback iStoreUpgradedKeyCallback = this.val$callback;
            java.util.Objects.requireNonNull(iStoreUpgradedKeyCallback);
            remoteProvisioningRegistration.wrapCallback(new com.android.server.security.rkp.RemoteProvisioningRegistration.CallbackRunner() { // from class: com.android.server.security.rkp.RemoteProvisioningRegistration$1$$ExternalSyntheticLambda1
                @Override // com.android.server.security.rkp.RemoteProvisioningRegistration.CallbackRunner
                public final void run() {
                    iStoreUpgradedKeyCallback.onSuccess();
                }
            });
        }

        @Override // android.os.OutcomeReceiver
        public void onError(final java.lang.Exception e) {
            com.android.server.security.rkp.RemoteProvisioningRegistration.this.mStoreUpgradedKeyOperations.remove(this.val$callback.asBinder());
            com.android.server.security.rkp.RemoteProvisioningRegistration remoteProvisioningRegistration = com.android.server.security.rkp.RemoteProvisioningRegistration.this;
            final android.security.rkp.IStoreUpgradedKeyCallback iStoreUpgradedKeyCallback = this.val$callback;
            remoteProvisioningRegistration.wrapCallback(new com.android.server.security.rkp.RemoteProvisioningRegistration.CallbackRunner() { // from class: com.android.server.security.rkp.RemoteProvisioningRegistration$1$$ExternalSyntheticLambda0
                @Override // com.android.server.security.rkp.RemoteProvisioningRegistration.CallbackRunner
                public final void run() {
                    iStoreUpgradedKeyCallback.onError(e.getMessage());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void wrapCallback(com.android.server.security.rkp.RemoteProvisioningRegistration.CallbackRunner callback) {
        try {
            callback.run();
        } catch (java.lang.Exception e) {
            android.util.Log.e("RemoteProvisionSysSvc", "Error invoking callback on client binder", e);
        }
    }
}
