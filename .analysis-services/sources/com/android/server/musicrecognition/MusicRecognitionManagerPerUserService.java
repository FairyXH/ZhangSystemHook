package com.android.server.musicrecognition;

/* JADX INFO: loaded from: classes2.dex */
public final class MusicRecognitionManagerPerUserService extends com.android.server.infra.AbstractPerUserSystemService<com.android.server.musicrecognition.MusicRecognitionManagerPerUserService, com.android.server.musicrecognition.MusicRecognitionManagerService> implements com.android.server.musicrecognition.RemoteMusicRecognitionService.Callbacks {
    private static final int BYTES_PER_SAMPLE = 2;
    private static final java.lang.String KEY_MUSIC_RECOGNITION_SERVICE_ATTRIBUTION_TAG = "android.media.musicrecognition.attributiontag";
    private static final int MAX_STREAMING_SECONDS = 24;
    private static final java.lang.String MUSIC_RECOGNITION_MANAGER_ATTRIBUTION_TAG = "MusicRecognitionManagerService";
    private static final java.lang.String TAG = com.android.server.musicrecognition.MusicRecognitionManagerPerUserService.class.getSimpleName();
    private final android.app.AppOpsManager mAppOpsManager;
    private final java.lang.String mAttributionMessage;
    private java.util.concurrent.CompletableFuture<java.lang.String> mAttributionTagFuture;
    private com.android.server.musicrecognition.RemoteMusicRecognitionService mRemoteService;
    private android.content.pm.ServiceInfo mServiceInfo;

    MusicRecognitionManagerPerUserService(com.android.server.musicrecognition.MusicRecognitionManagerService primary, java.lang.Object lock, int userId) {
        super(primary, lock, userId);
        this.mAppOpsManager = (android.app.AppOpsManager) getContext().createAttributionContext(MUSIC_RECOGNITION_MANAGER_ATTRIBUTION_TAG).getSystemService(android.app.AppOpsManager.class);
        this.mAttributionMessage = java.lang.String.format("MusicRecognitionManager.invokedByUid.%s", java.lang.Integer.valueOf(userId));
        this.mAttributionTagFuture = null;
        this.mServiceInfo = null;
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected android.content.pm.ServiceInfo newServiceInfoLocked(android.content.ComponentName serviceComponent) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            android.content.pm.ServiceInfo si = android.app.AppGlobals.getPackageManager().getServiceInfo(serviceComponent, 128L, this.mUserId);
            if (!"android.permission.BIND_MUSIC_RECOGNITION_SERVICE".equals(si.permission)) {
                android.util.Slog.w(TAG, "MusicRecognitionService from '" + si.packageName + "' does not require permission android.permission.BIND_MUSIC_RECOGNITION_SERVICE");
                throw new java.lang.SecurityException("Service does not require permission android.permission.BIND_MUSIC_RECOGNITION_SERVICE");
            }
            return si;
        } catch (android.os.RemoteException e) {
            throw new android.content.pm.PackageManager.NameNotFoundException("Could not get service for " + serviceComponent);
        }
    }

    private com.android.server.musicrecognition.RemoteMusicRecognitionService ensureRemoteServiceLocked(android.media.musicrecognition.IMusicRecognitionManagerCallback clientCallback) {
        if (this.mRemoteService == null) {
            java.lang.String serviceName = getComponentNameLocked();
            if (serviceName == null) {
                if (((com.android.server.musicrecognition.MusicRecognitionManagerService) this.mMaster).verbose) {
                    android.util.Slog.v(TAG, "ensureRemoteServiceLocked(): not set");
                }
                return null;
            }
            android.content.ComponentName serviceComponent = android.content.ComponentName.unflattenFromString(serviceName);
            this.mRemoteService = new com.android.server.musicrecognition.RemoteMusicRecognitionService(getContext(), serviceComponent, this.mUserId, this, new com.android.server.musicrecognition.MusicRecognitionManagerPerUserService.MusicRecognitionServiceCallback(clientCallback), ((com.android.server.musicrecognition.MusicRecognitionManagerService) this.mMaster).isBindInstantServiceAllowed(), ((com.android.server.musicrecognition.MusicRecognitionManagerService) this.mMaster).verbose);
            try {
                this.mServiceInfo = getContext().getPackageManager().getServiceInfo(this.mRemoteService.getComponentName(), 128);
                this.mAttributionTagFuture = this.mRemoteService.getAttributionTag();
                android.util.Slog.i(TAG, "Remote service bound: " + this.mRemoteService.getComponentName());
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Slog.e(TAG, "Service was not found.", e);
            }
        }
        return this.mRemoteService;
    }

    public void beginRecognitionLocked(final android.media.musicrecognition.RecognitionRequest recognitionRequest, android.os.IBinder callback) {
        final android.media.musicrecognition.IMusicRecognitionManagerCallback clientCallback = android.media.musicrecognition.IMusicRecognitionManagerCallback.Stub.asInterface(callback);
        this.mRemoteService = ensureRemoteServiceLocked(clientCallback);
        if (this.mRemoteService == null) {
            try {
                clientCallback.onRecognitionFailed(3);
                return;
            } catch (android.os.RemoteException e) {
                return;
            }
        }
        android.util.Pair<android.os.ParcelFileDescriptor, android.os.ParcelFileDescriptor> clientPipe = createPipe();
        if (clientPipe == null) {
            try {
                clientCallback.onRecognitionFailed(7);
            } catch (android.os.RemoteException e2) {
            }
        } else {
            final android.os.ParcelFileDescriptor audioSink = (android.os.ParcelFileDescriptor) clientPipe.second;
            android.os.ParcelFileDescriptor clientRead = (android.os.ParcelFileDescriptor) clientPipe.first;
            this.mAttributionTagFuture.thenAcceptAsync(new java.util.function.Consumer() { // from class: com.android.server.musicrecognition.MusicRecognitionManagerPerUserService$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$beginRecognitionLocked$0(recognitionRequest, clientCallback, audioSink, (java.lang.String) obj);
                }
            }, (java.util.concurrent.Executor) ((com.android.server.musicrecognition.MusicRecognitionManagerService) this.mMaster).mExecutorService);
            this.mRemoteService.onAudioStreamStarted(clientRead, recognitionRequest.getAudioFormat());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: streamAudio, reason: merged with bridge method [inline-methods] */
    public void lambda$beginRecognitionLocked$0(java.lang.String attributionTag, android.media.musicrecognition.RecognitionRequest recognitionRequest, android.media.musicrecognition.IMusicRecognitionManagerCallback clientCallback, android.os.ParcelFileDescriptor audioSink) {
        int maxAudioLengthSeconds = java.lang.Math.min(recognitionRequest.getMaxAudioLengthSeconds(), 24);
        if (maxAudioLengthSeconds <= 0) {
            android.util.Slog.i(TAG, "No audio requested. Closing stream.");
            try {
                audioSink.close();
                clientCallback.onAudioStreamClosed();
                return;
            } catch (android.os.RemoteException e) {
                return;
            } catch (java.io.IOException e2) {
                android.util.Slog.e(TAG, "Problem closing stream.", e2);
                return;
            }
        }
        try {
            startRecordAudioOp(attributionTag);
            android.media.AudioRecord audioRecord = createAudioRecord(recognitionRequest, maxAudioLengthSeconds);
            try {
                try {
                    try {
                        java.io.OutputStream fos = new android.os.ParcelFileDescriptor.AutoCloseOutputStream(audioSink);
                        try {
                            streamAudio(recognitionRequest, maxAudioLengthSeconds, audioRecord, fos);
                            fos.close();
                            audioRecord.release();
                            finishRecordAudioOp(attributionTag);
                            clientCallback.onAudioStreamClosed();
                        } catch (java.lang.Throwable th) {
                            try {
                                fos.close();
                            } catch (java.lang.Throwable th2) {
                                th.addSuppressed(th2);
                            }
                            throw th;
                        }
                    } catch (android.os.RemoteException e3) {
                    }
                } catch (java.io.IOException e4) {
                    android.util.Slog.e(TAG, "Audio streaming stopped.", e4);
                    audioRecord.release();
                    finishRecordAudioOp(attributionTag);
                    clientCallback.onAudioStreamClosed();
                }
            } catch (java.lang.Throwable th3) {
                audioRecord.release();
                finishRecordAudioOp(attributionTag);
                try {
                    clientCallback.onAudioStreamClosed();
                } catch (android.os.RemoteException e5) {
                }
                throw th3;
            }
        } catch (java.lang.SecurityException e6) {
            android.util.Slog.e(TAG, "RECORD_AUDIO op not permitted on behalf of " + this.mServiceInfo.getComponentName(), e6);
            try {
                clientCallback.onRecognitionFailed(7);
            } catch (android.os.RemoteException e7) {
            }
        }
    }

    private void streamAudio(android.media.musicrecognition.RecognitionRequest recognitionRequest, int maxAudioLengthSeconds, android.media.AudioRecord audioRecord, java.io.OutputStream outputStream) throws java.io.IOException {
        int halfSecondBufferSize = audioRecord.getBufferSizeInFrames() / maxAudioLengthSeconds;
        byte[] byteBuffer = new byte[halfSecondBufferSize];
        int bytesRead = 0;
        int totalBytesRead = 0;
        int ignoreBytes = recognitionRequest.getIgnoreBeginningFrames() * 2;
        audioRecord.startRecording();
        while (bytesRead >= 0 && totalBytesRead < audioRecord.getBufferSizeInFrames() * 2 && this.mRemoteService != null) {
            bytesRead = audioRecord.read(byteBuffer, 0, byteBuffer.length);
            if (bytesRead > 0) {
                totalBytesRead += bytesRead;
                if (ignoreBytes > 0) {
                    ignoreBytes -= bytesRead;
                    if (ignoreBytes < 0) {
                        outputStream.write(byteBuffer, bytesRead + ignoreBytes, -ignoreBytes);
                    }
                } else {
                    outputStream.write(byteBuffer);
                }
            }
        }
        android.util.Slog.i(TAG, java.lang.String.format("Streamed %s bytes from audio record", java.lang.Integer.valueOf(totalBytesRead)));
    }

    final class MusicRecognitionServiceCallback extends android.media.musicrecognition.IMusicRecognitionServiceCallback.Stub {
        private final android.media.musicrecognition.IMusicRecognitionManagerCallback mClientCallback;

        private MusicRecognitionServiceCallback(android.media.musicrecognition.IMusicRecognitionManagerCallback clientCallback) {
            this.mClientCallback = clientCallback;
        }

        public void onRecognitionSucceeded(android.media.MediaMetadata result, android.os.Bundle extras) {
            try {
                com.android.server.musicrecognition.MusicRecognitionManagerPerUserService.sanitizeBundle(extras);
                this.mClientCallback.onRecognitionSucceeded(result, extras);
            } catch (android.os.RemoteException e) {
            }
            com.android.server.musicrecognition.MusicRecognitionManagerPerUserService.this.destroyService();
        }

        public void onRecognitionFailed(int failureCode) {
            try {
                this.mClientCallback.onRecognitionFailed(failureCode);
            } catch (android.os.RemoteException e) {
            }
            com.android.server.musicrecognition.MusicRecognitionManagerPerUserService.this.destroyService();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.media.musicrecognition.IMusicRecognitionManagerCallback getClientCallback() {
            return this.mClientCallback;
        }
    }

    public void onServiceDied(com.android.server.musicrecognition.RemoteMusicRecognitionService service) {
        try {
            service.getServerCallback().getClientCallback().onRecognitionFailed(5);
        } catch (android.os.RemoteException e) {
        }
        android.util.Slog.w(TAG, "remote service died: " + service);
        destroyService();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroyService() {
        synchronized (this.mLock) {
            if (this.mRemoteService != null) {
                this.mRemoteService.destroy();
                this.mRemoteService = null;
            }
        }
    }

    private void startRecordAudioOp(java.lang.String attributionTag) {
        int status = this.mAppOpsManager.startProxyOp((java.lang.String) java.util.Objects.requireNonNull(android.app.AppOpsManager.permissionToOp("android.permission.RECORD_AUDIO")), this.mServiceInfo.applicationInfo.uid, this.mServiceInfo.packageName, attributionTag, this.mAttributionMessage);
        if (status != 0) {
            throw new java.lang.SecurityException(java.lang.String.format("Failed to obtain RECORD_AUDIO permission (status: %d) for receiving service: %s", java.lang.Integer.valueOf(status), this.mServiceInfo.getComponentName()));
        }
        android.util.Slog.i(TAG, java.lang.String.format("Starting audio streaming. Attributing to %s (%d) with tag '%s'", this.mServiceInfo.packageName, java.lang.Integer.valueOf(this.mServiceInfo.applicationInfo.uid), attributionTag));
    }

    private void finishRecordAudioOp(java.lang.String attributionTag) {
        this.mAppOpsManager.finishProxyOp((java.lang.String) java.util.Objects.requireNonNull(android.app.AppOpsManager.permissionToOp("android.permission.RECORD_AUDIO")), this.mServiceInfo.applicationInfo.uid, this.mServiceInfo.packageName, attributionTag);
    }

    private static android.media.AudioRecord createAudioRecord(android.media.musicrecognition.RecognitionRequest recognitionRequest, int maxAudioLengthSeconds) {
        int sampleRate = recognitionRequest.getAudioFormat().getSampleRate();
        int bufferSize = getBufferSizeInBytes(sampleRate, maxAudioLengthSeconds);
        return new android.media.AudioRecord(recognitionRequest.getAudioAttributes(), recognitionRequest.getAudioFormat(), bufferSize, recognitionRequest.getCaptureSession());
    }

    private static int getBufferSizeInBytes(int sampleRate, int bufferLengthSeconds) {
        return sampleRate * 2 * bufferLengthSeconds;
    }

    private static android.util.Pair<android.os.ParcelFileDescriptor, android.os.ParcelFileDescriptor> createPipe() {
        try {
            android.os.ParcelFileDescriptor[] fileDescriptors = android.os.ParcelFileDescriptor.createPipe();
            if (fileDescriptors.length != 2) {
                android.util.Slog.e(TAG, "Failed to create audio stream pipe, unexpected number of file descriptors");
                return null;
            }
            if (!fileDescriptors[0].getFileDescriptor().valid() || !fileDescriptors[1].getFileDescriptor().valid()) {
                android.util.Slog.e(TAG, "Failed to create audio stream pipe, didn't receive a pair of valid file descriptors.");
                return null;
            }
            return android.util.Pair.create(fileDescriptors[0], fileDescriptors[1]);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to create audio stream pipe", e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sanitizeBundle(android.os.Bundle bundle) {
        if (bundle == null) {
            return;
        }
        for (java.lang.String key : bundle.keySet()) {
            java.lang.Object o = bundle.get(key);
            if (o instanceof android.os.Bundle) {
                sanitizeBundle((android.os.Bundle) o);
            } else if ((o instanceof android.os.IBinder) || (o instanceof android.os.ParcelFileDescriptor)) {
                bundle.remove(key);
            }
        }
    }
}
