package com.android.server.wearable;

/* JADX INFO: loaded from: classes3.dex */
final class WearableSensingSecureChannel {
    private static final java.lang.String CDM_ASSOCIATION_DISPLAY_NAME = "PlaceholderDisplayNameFromWSM";
    private static final int READ_BUFFER_SIZE = 8192;
    private static final java.lang.String TAG = com.android.server.wearable.WearableSensingSecureChannel.class.getSimpleName();
    private final android.companion.CompanionDeviceManager mCompanionDeviceManager;
    private final java.io.InputStream mLocalIn;
    private final java.io.OutputStream mLocalOut;
    private final android.os.ParcelFileDescriptor mRemoteFd;
    private final com.android.server.wearable.WearableSensingSecureChannel.SecureTransportListener mSecureTransportListener;
    private final android.os.ParcelFileDescriptor mUnderlyingTransport;
    private final java.lang.Object mLock = new java.lang.Object();
    private final com.android.server.wearable.WearableSensingSecureChannel.SoftShutdownExecutor mMessageFromWearableExecutor = new com.android.server.wearable.WearableSensingSecureChannel.SoftShutdownExecutor(java.util.concurrent.Executors.newSingleThreadExecutor());
    private final com.android.server.wearable.WearableSensingSecureChannel.SoftShutdownExecutor mMessageToWearableExecutor = new com.android.server.wearable.WearableSensingSecureChannel.SoftShutdownExecutor(java.util.concurrent.Executors.newSingleThreadExecutor());
    private final com.android.server.wearable.WearableSensingSecureChannel.SoftShutdownExecutor mLightWeightExecutor = new com.android.server.wearable.WearableSensingSecureChannel.SoftShutdownExecutor(java.util.concurrent.Executors.newSingleThreadExecutor());
    private final java.util.concurrent.atomic.AtomicBoolean mTransportAvailable = new java.util.concurrent.atomic.AtomicBoolean(false);
    private final java.util.function.Consumer<java.util.List<android.companion.AssociationInfo>> mOnTransportsChangedListener = new java.util.function.Consumer() { // from class: com.android.server.wearable.WearableSensingSecureChannel$$ExternalSyntheticLambda1
        @Override // java.util.function.Consumer
        public final void accept(java.lang.Object obj) {
            this.f$0.onTransportsChanged((java.util.List) obj);
        }
    };
    private final java.util.function.BiConsumer<java.lang.Integer, byte[]> mOnMessageReceivedListener = new java.util.function.BiConsumer() { // from class: com.android.server.wearable.WearableSensingSecureChannel$$ExternalSyntheticLambda2
        @Override // java.util.function.BiConsumer
        public final void accept(java.lang.Object obj, java.lang.Object obj2) {
            this.f$0.onMessageReceived(((java.lang.Integer) obj).intValue(), (byte[]) obj2);
        }
    };
    private boolean mClosed = false;
    private java.lang.Integer mAssociationId = null;

    interface SecureTransportListener {
        void onError();

        void onSecureTransportAvailable(android.os.ParcelFileDescriptor parcelFileDescriptor);
    }

    static com.android.server.wearable.WearableSensingSecureChannel create(android.companion.CompanionDeviceManager companionDeviceManager, android.os.ParcelFileDescriptor underlyingTransport, com.android.server.wearable.WearableSensingSecureChannel.SecureTransportListener secureTransportListener) throws java.io.IOException {
        java.util.Objects.requireNonNull(companionDeviceManager);
        java.util.Objects.requireNonNull(underlyingTransport);
        java.util.Objects.requireNonNull(secureTransportListener);
        android.os.ParcelFileDescriptor[] pair = android.os.ParcelFileDescriptor.createSocketPair();
        com.android.server.wearable.WearableSensingSecureChannel channel = new com.android.server.wearable.WearableSensingSecureChannel(companionDeviceManager, underlyingTransport, secureTransportListener, pair[0], pair[1]);
        channel.initialize();
        return channel;
    }

    private WearableSensingSecureChannel(android.companion.CompanionDeviceManager companionDeviceManager, android.os.ParcelFileDescriptor underlyingTransport, com.android.server.wearable.WearableSensingSecureChannel.SecureTransportListener secureTransportListener, android.os.ParcelFileDescriptor remoteFd, android.os.ParcelFileDescriptor localFd) {
        this.mCompanionDeviceManager = companionDeviceManager;
        this.mUnderlyingTransport = underlyingTransport;
        this.mSecureTransportListener = secureTransportListener;
        this.mRemoteFd = remoteFd;
        this.mLocalIn = new android.os.ParcelFileDescriptor.AutoCloseInputStream(localFd);
        this.mLocalOut = new android.os.ParcelFileDescriptor.AutoCloseOutputStream(localFd);
    }

    private void initialize() {
        long originalCallingIdentity = android.os.Binder.clearCallingIdentity();
        try {
            android.util.Slog.d(TAG, "Requesting CDM association.");
            this.mCompanionDeviceManager.associate(new android.companion.AssociationRequest.Builder().setDisplayName(CDM_ASSOCIATION_DISPLAY_NAME).setSelfManaged(true).build(), this.mLightWeightExecutor, new android.companion.CompanionDeviceManager.Callback() { // from class: com.android.server.wearable.WearableSensingSecureChannel.1
                @Override // android.companion.CompanionDeviceManager.Callback
                public void onAssociationCreated(android.companion.AssociationInfo associationInfo) {
                    com.android.server.wearable.WearableSensingSecureChannel.this.onAssociationCreated(associationInfo.getId());
                }

                @Override // android.companion.CompanionDeviceManager.Callback
                public void onFailure(java.lang.CharSequence error) {
                    android.util.Slog.e(com.android.server.wearable.WearableSensingSecureChannel.TAG, "Failed to create CompanionDeviceManager association: " + ((java.lang.Object) error));
                    com.android.server.wearable.WearableSensingSecureChannel.this.onError();
                }
            });
        } finally {
            android.os.Binder.restoreCallingIdentity(originalCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAssociationCreated(int associationId) {
        android.util.Slog.i(TAG, "CDM association created.");
        synchronized (this.mLock) {
            if (this.mClosed) {
                return;
            }
            this.mAssociationId = java.lang.Integer.valueOf(associationId);
            this.mCompanionDeviceManager.addOnMessageReceivedListener(this.mMessageFromWearableExecutor, 1131446919, this.mOnMessageReceivedListener);
            this.mCompanionDeviceManager.addOnTransportsChangedListener(this.mLightWeightExecutor, this.mOnTransportsChangedListener);
            this.mCompanionDeviceManager.attachSystemDataTransport(associationId, new android.os.ParcelFileDescriptor.AutoCloseInputStream(this.mUnderlyingTransport), new android.os.ParcelFileDescriptor.AutoCloseOutputStream(this.mUnderlyingTransport));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTransportsChanged(java.util.List<android.companion.AssociationInfo> associationInfos) {
        synchronized (this.mLock) {
            if (this.mClosed) {
                return;
            }
            if (this.mAssociationId == null) {
                android.util.Slog.e(TAG, "mAssociationId is null when transport changed");
                return;
            }
            boolean transportAvailable = associationInfos.stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.wearable.WearableSensingSecureChannel$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return this.f$0.lambda$onTransportsChanged$0((android.companion.AssociationInfo) obj);
                }
            });
            if (transportAvailable && this.mTransportAvailable.compareAndSet(false, true)) {
                onTransportAvailable();
            } else if (!transportAvailable && this.mTransportAvailable.compareAndSet(true, false)) {
                android.util.Slog.i(TAG, "CDM transport is detached. This is not recoverable.");
                onError();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onTransportsChanged$0(android.companion.AssociationInfo info) {
        return info.getId() == this.mAssociationId.intValue();
    }

    private void onTransportAvailable() {
        android.util.Slog.i(TAG, "Transport available");
        this.mMessageToWearableExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.wearable.WearableSensingSecureChannel$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onTransportAvailable$1();
            }
        });
        this.mSecureTransportListener.onSecureTransportAvailable(this.mRemoteFd);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTransportAvailable$1() {
        int[] associationIdsToSendMessageTo = {this.mAssociationId.intValue()};
        byte[] buffer = new byte[8192];
        while (true) {
            try {
                int readLen = this.mLocalIn.read(buffer);
                if (readLen != -1) {
                    byte[] data = new byte[readLen];
                    java.lang.System.arraycopy(buffer, 0, data, 0, readLen);
                    android.util.Slog.v(TAG, "Sending message to wearable");
                    this.mCompanionDeviceManager.sendMessage(1132755335, data, associationIdsToSendMessageTo);
                } else {
                    android.util.Slog.i(TAG, "Reached EOF when reading from remote stream. Reporting this as an error.");
                    onError();
                    return;
                }
            } catch (java.io.IOException e) {
                android.util.Slog.i(TAG, "IOException while reading from remote stream.");
                onError();
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMessageReceived(int associationIdForMessage, byte[] data) {
        if (associationIdForMessage == this.mAssociationId.intValue()) {
            android.util.Slog.v(TAG, "Received message from wearable.");
            try {
                this.mLocalOut.write(data);
                this.mLocalOut.flush();
                return;
            } catch (java.io.IOException e) {
                android.util.Slog.i(TAG, "IOException when writing to remote stream. Closing the secure channel.");
                onError();
                return;
            }
        }
        android.util.Slog.v(TAG, "Received CDM message of type MESSAGE_ONEWAY_FROM_WEARABLE, but it is for another association. Ignoring the message.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onError() {
        synchronized (this.mLock) {
            if (this.mClosed) {
                return;
            }
            this.mSecureTransportListener.onError();
            close();
        }
    }

    void close() {
        synchronized (this.mLock) {
            if (this.mClosed) {
                return;
            }
            android.util.Slog.i(TAG, "Closing WearableSensingSecureChannel.");
            this.mClosed = true;
            if (this.mAssociationId != null) {
                long originalCallingIdentity = android.os.Binder.clearCallingIdentity();
                try {
                    this.mCompanionDeviceManager.removeOnTransportsChangedListener(this.mOnTransportsChangedListener);
                    this.mCompanionDeviceManager.removeOnMessageReceivedListener(1131446919, this.mOnMessageReceivedListener);
                    this.mCompanionDeviceManager.detachSystemDataTransport(this.mAssociationId.intValue());
                    this.mCompanionDeviceManager.disassociate(this.mAssociationId.intValue());
                    android.os.Binder.restoreCallingIdentity(originalCallingIdentity);
                    try {
                        this.mLocalIn.close();
                    } catch (java.io.IOException ex) {
                        android.util.Slog.e(TAG, "Encountered IOException when closing local input stream.", ex);
                    }
                    try {
                        this.mLocalOut.close();
                    } catch (java.io.IOException ex2) {
                        android.util.Slog.e(TAG, "Encountered IOException when closing local output stream.", ex2);
                    }
                    this.mMessageFromWearableExecutor.shutdown();
                    this.mMessageToWearableExecutor.shutdown();
                    this.mLightWeightExecutor.shutdown();
                    return;
                } catch (java.lang.Throwable th) {
                    android.os.Binder.restoreCallingIdentity(originalCallingIdentity);
                    throw th;
                }
            }
            this.mLocalIn.close();
            this.mLocalOut.close();
            this.mMessageFromWearableExecutor.shutdown();
            this.mMessageToWearableExecutor.shutdown();
            this.mLightWeightExecutor.shutdown();
            return;
        }
    }

    private static class SoftShutdownExecutor implements java.util.concurrent.Executor {
        private final java.util.concurrent.ExecutorService mExecutorService;

        SoftShutdownExecutor(java.util.concurrent.ExecutorService executorService) {
            this.mExecutorService = executorService;
        }

        @Override // java.util.concurrent.Executor
        public void execute(java.lang.Runnable runnable) {
            try {
                this.mExecutorService.execute(runnable);
            } catch (java.util.concurrent.RejectedExecutionException e) {
                android.util.Slog.d(com.android.server.wearable.WearableSensingSecureChannel.TAG, "Received new runnable after shutdown. Ignoring.");
            }
        }

        void shutdown() {
            this.mExecutorService.shutdown();
        }
    }
}
