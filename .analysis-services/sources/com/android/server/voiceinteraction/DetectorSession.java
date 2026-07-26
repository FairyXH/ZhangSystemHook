package com.android.server.voiceinteraction;

/* JADX INFO: loaded from: classes3.dex */
abstract class DetectorSession {
    static final boolean DEBUG = false;
    private static final long EXTERNAL_HOTWORD_CLEANUP_MILLIS = 2000;
    private static final int EXTERNAL_SOURCE_DETECT_SECURITY_EXCEPTION = 13;
    private static final java.lang.String HOTWORD_DETECTION_OP_MESSAGE = "Providing hotword detection result to VoiceInteractionService";
    private static final int HOTWORD_EVENT_TYPE_DETECTION = 1;
    private static final int HOTWORD_EVENT_TYPE_REJECTION = 2;
    private static final int HOTWORD_EVENT_TYPE_TRAINING_DATA = 3;
    private static final java.time.Duration MAX_UPDATE_TIMEOUT_DURATION = java.time.Duration.ofMillis(30000);
    private static final long MAX_UPDATE_TIMEOUT_MILLIS = 30000;
    private static final int METRICS_CALLBACK_ON_STATUS_REPORTED_EXCEPTION = 14;
    private static final int METRICS_EXTERNAL_SOURCE_DETECTED = 11;
    private static final int METRICS_EXTERNAL_SOURCE_REJECTED = 12;
    private static final int METRICS_INIT_CALLBACK_STATE_ERROR = 1;
    private static final int METRICS_INIT_CALLBACK_STATE_SUCCESS = 0;
    private static final int METRICS_INIT_UNKNOWN_NO_VALUE = 2;
    private static final int METRICS_INIT_UNKNOWN_OVER_MAX_CUSTOM_VALUE = 3;
    private static final int METRICS_INIT_UNKNOWN_TIMEOUT = 4;
    static final int METRICS_KEYPHRASE_TRIGGERED_DETECT_SECURITY_EXCEPTION = 8;
    static final int METRICS_KEYPHRASE_TRIGGERED_DETECT_UNEXPECTED_CALLBACK = 7;
    static final int METRICS_KEYPHRASE_TRIGGERED_REJECT_UNEXPECTED_CALLBACK = 9;
    static final int ONDETECTED_GOT_SECURITY_EXCEPTION = 5;
    static final int ONDETECTED_STREAM_COPY_ERROR = 6;
    private static final java.lang.String TAG = "DetectorSession";
    final android.app.AppOpsManager mAppOpsManager;
    android.attention.AttentionManagerInternal mAttentionManagerInternal;
    final com.android.internal.app.IHotwordRecognitionStatusCallback mCallback;
    final android.content.Context mContext;
    android.os.ParcelFileDescriptor mCurrentAudioSink;
    boolean mDebugHotwordLogging;
    final com.android.server.voiceinteraction.HotwordAudioStreamCopier mHotwordAudioStreamCopier;
    final java.lang.Object mLock;
    boolean mPerformingExternalSourceHotwordDetection;
    com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnection mRemoteDetectionService;
    com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.DetectorRemoteExceptionListener mRemoteExceptionListener;
    final java.util.concurrent.ScheduledExecutorService mScheduledExecutorService;
    final android.os.IBinder mToken;
    final int mUserId;
    final int mVoiceInteractionServiceUid;
    final android.media.permission.Identity mVoiceInteractorIdentity;
    private final java.util.concurrent.Executor mAudioCopyExecutor = java.util.concurrent.Executors.newCachedThreadPool();
    final java.util.concurrent.atomic.AtomicBoolean mUpdateStateAfterStartFinished = new java.util.concurrent.atomic.AtomicBoolean(false);
    final android.attention.AttentionManagerInternal.ProximityUpdateCallbackInternal mProximityCallbackInternal = new android.attention.AttentionManagerInternal.ProximityUpdateCallbackInternal() { // from class: com.android.server.voiceinteraction.DetectorSession$$ExternalSyntheticLambda0
        public final void onProximityUpdate(double d) {
            this.f$0.setProximityValue(d);
        }
    };
    private double mProximityMeters = -1.0d;
    private boolean mInitialized = false;
    private boolean mDestroyed = false;

    abstract void informRestartProcessLocked();

    DetectorSession(com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnection remoteDetectionService, java.lang.Object lock, android.content.Context context, android.os.IBinder token, com.android.internal.app.IHotwordRecognitionStatusCallback callback, int voiceInteractionServiceUid, android.media.permission.Identity voiceInteractorIdentity, java.util.concurrent.ScheduledExecutorService scheduledExecutorService, boolean logging, com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.DetectorRemoteExceptionListener listener, int userId) {
        this.mAttentionManagerInternal = null;
        this.mDebugHotwordLogging = false;
        this.mRemoteExceptionListener = listener;
        this.mRemoteDetectionService = remoteDetectionService;
        this.mLock = lock;
        this.mContext = context;
        this.mToken = token;
        this.mUserId = userId;
        this.mCallback = callback;
        this.mVoiceInteractionServiceUid = voiceInteractionServiceUid;
        this.mVoiceInteractorIdentity = voiceInteractorIdentity;
        this.mAppOpsManager = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        if (getDetectorType() != 3) {
            this.mHotwordAudioStreamCopier = new com.android.server.voiceinteraction.HotwordAudioStreamCopier(this.mAppOpsManager, getDetectorType(), this.mVoiceInteractorIdentity.uid, this.mVoiceInteractorIdentity.packageName, this.mVoiceInteractorIdentity.attributionTag);
        } else {
            this.mHotwordAudioStreamCopier = null;
        }
        this.mScheduledExecutorService = scheduledExecutorService;
        this.mDebugHotwordLogging = logging;
        this.mAttentionManagerInternal = (android.attention.AttentionManagerInternal) com.android.server.LocalServices.getService(android.attention.AttentionManagerInternal.class);
        if (this.mAttentionManagerInternal != null && this.mAttentionManagerInternal.isProximitySupported()) {
            this.mAttentionManagerInternal.onStartProximityUpdates(this.mProximityCallbackInternal);
        }
    }

    void notifyOnDetectorRemoteException() {
        android.util.Slog.d(TAG, "notifyOnDetectorRemoteException: mRemoteExceptionListener=" + this.mRemoteExceptionListener);
        if (this.mRemoteExceptionListener != null) {
            this.mRemoteExceptionListener.onDetectorRemoteException(this.mToken, getDetectorType());
        }
    }

    private void updateStateAfterProcessStartLocked(final android.os.PersistableBundle options, final android.os.SharedMemory sharedMemory) {
        com.android.internal.infra.AndroidFuture<java.lang.Void> voidFuture = this.mRemoteDetectionService.postAsync(new com.android.internal.infra.ServiceConnector.Job() { // from class: com.android.server.voiceinteraction.DetectorSession$$ExternalSyntheticLambda1
            public final java.lang.Object run(java.lang.Object obj) {
                return this.f$0.lambda$updateStateAfterProcessStartLocked$0(options, sharedMemory, (android.service.voice.ISandboxedDetectionService) obj);
            }
        }).whenComplete(new java.util.function.BiConsumer() { // from class: com.android.server.voiceinteraction.DetectorSession$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$updateStateAfterProcessStartLocked$1((java.lang.Void) obj, (java.lang.Throwable) obj2);
            }
        });
        if (voidFuture == null) {
            android.util.Slog.w(TAG, "Failed to create AndroidFuture");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.util.concurrent.CompletableFuture lambda$updateStateAfterProcessStartLocked$0(android.os.PersistableBundle options, android.os.SharedMemory sharedMemory, android.service.voice.ISandboxedDetectionService service) throws java.lang.Exception {
        final com.android.internal.infra.AndroidFuture<java.lang.Void> future = new com.android.internal.infra.AndroidFuture<>();
        try {
            service.updateState(options, sharedMemory, new android.os.IRemoteCallback.Stub() { // from class: com.android.server.voiceinteraction.DetectorSession.1
                public void sendResult(android.os.Bundle bundle) throws android.os.RemoteException {
                    future.complete((java.lang.Object) null);
                    if (com.android.server.voiceinteraction.DetectorSession.this.mUpdateStateAfterStartFinished.getAndSet(true)) {
                        android.util.Slog.w(com.android.server.voiceinteraction.DetectorSession.TAG, "call callback after timeout");
                        if (com.android.server.voiceinteraction.DetectorSession.this.getDetectorType() != 3) {
                            com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(com.android.server.voiceinteraction.DetectorSession.this.getDetectorType(), 5, com.android.server.voiceinteraction.DetectorSession.this.mVoiceInteractionServiceUid);
                            return;
                        }
                        return;
                    }
                    android.util.Pair<java.lang.Integer, java.lang.Integer> statusResultPair = com.android.server.voiceinteraction.DetectorSession.getInitStatusAndMetricsResult(bundle);
                    int status = ((java.lang.Integer) statusResultPair.first).intValue();
                    int initResultMetricsResult = ((java.lang.Integer) statusResultPair.second).intValue();
                    try {
                        com.android.server.voiceinteraction.DetectorSession.this.mCallback.onStatusReported(status);
                        if (com.android.server.voiceinteraction.DetectorSession.this.getDetectorType() != 3) {
                            com.android.server.voiceinteraction.HotwordMetricsLogger.writeServiceInitResultEvent(com.android.server.voiceinteraction.DetectorSession.this.getDetectorType(), initResultMetricsResult, com.android.server.voiceinteraction.DetectorSession.this.mVoiceInteractionServiceUid);
                        }
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.w(com.android.server.voiceinteraction.DetectorSession.TAG, "Failed to report initialization status: " + e);
                        if (com.android.server.voiceinteraction.DetectorSession.this.getDetectorType() != 3) {
                            com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(com.android.server.voiceinteraction.DetectorSession.this.getDetectorType(), 14, com.android.server.voiceinteraction.DetectorSession.this.mVoiceInteractionServiceUid);
                        }
                        com.android.server.voiceinteraction.DetectorSession.this.notifyOnDetectorRemoteException();
                    }
                }
            });
            if (getDetectorType() != 3) {
                com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(getDetectorType(), 4, this.mVoiceInteractionServiceUid);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to updateState for HotwordDetectionService", e);
            if (getDetectorType() != 3) {
                com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(getDetectorType(), 19, this.mVoiceInteractionServiceUid);
            }
        }
        return future.orTimeout(30000L, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateStateAfterProcessStartLocked$1(java.lang.Void res, java.lang.Throwable err) {
        if (err instanceof java.util.concurrent.TimeoutException) {
            android.util.Slog.w(TAG, "updateState timed out");
            if (this.mUpdateStateAfterStartFinished.getAndSet(true)) {
                return;
            }
            try {
                this.mCallback.onStatusReported(100);
                if (getDetectorType() != 3) {
                    com.android.server.voiceinteraction.HotwordMetricsLogger.writeServiceInitResultEvent(getDetectorType(), 4, this.mVoiceInteractionServiceUid);
                    return;
                }
                return;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to report initialization status UNKNOWN", e);
                if (getDetectorType() != 3) {
                    com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(getDetectorType(), 14, this.mVoiceInteractionServiceUid);
                }
                notifyOnDetectorRemoteException();
                return;
            }
        }
        if (err != null) {
            android.util.Slog.w(TAG, "Failed to update state: " + err);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.util.Pair<java.lang.Integer, java.lang.Integer> getInitStatusAndMetricsResult(android.os.Bundle bundle) {
        int metricsResult;
        int i = 2;
        if (bundle == null) {
            return new android.util.Pair<>(100, 2);
        }
        int status = bundle.getInt("initialization_status", 100);
        if (status > android.service.voice.HotwordDetectionService.getMaxCustomInitializationStatus()) {
            if (status != 100) {
                i = 3;
            }
            return new android.util.Pair<>(100, java.lang.Integer.valueOf(i));
        }
        if (status == 0) {
            metricsResult = 0;
        } else {
            metricsResult = 1;
        }
        return new android.util.Pair<>(java.lang.Integer.valueOf(status), java.lang.Integer.valueOf(metricsResult));
    }

    void updateStateLocked(final android.os.PersistableBundle options, final android.os.SharedMemory sharedMemory, java.time.Instant lastRestartInstant) {
        if (getDetectorType() != 3) {
            com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(getDetectorType(), 8, this.mVoiceInteractionServiceUid);
        }
        if (!this.mUpdateStateAfterStartFinished.get() && java.time.Instant.now().minus((java.time.temporal.TemporalAmount) MAX_UPDATE_TIMEOUT_DURATION).isBefore(lastRestartInstant)) {
            android.util.Slog.v(TAG, "call updateStateAfterProcessStartLocked");
            updateStateAfterProcessStartLocked(options, sharedMemory);
        } else {
            this.mRemoteDetectionService.run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.voiceinteraction.DetectorSession$$ExternalSyntheticLambda6
                public final void runNoResult(java.lang.Object obj) {
                    ((android.service.voice.ISandboxedDetectionService) obj).updateState(options, sharedMemory, (android.os.IRemoteCallback) null);
                }
            });
        }
    }

    void startListeningFromExternalSourceLocked(android.os.ParcelFileDescriptor audioStream, android.media.AudioFormat audioFormat, android.os.PersistableBundle options, android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback callback) {
        handleExternalSourceHotwordDetectionLocked(audioStream, audioFormat, options, callback, true, true);
    }

    void startListeningFromWearableLocked(android.os.ParcelFileDescriptor audioStream, android.media.AudioFormat audioFormat, android.os.PersistableBundle options, final android.service.voice.VoiceInteractionManagerInternal.WearableHotwordDetectionCallback wearableCallback) {
        android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback voiceInteractionCallback = new android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback() { // from class: com.android.server.voiceinteraction.DetectorSession.2
            public void onDetected(android.service.voice.HotwordDetectedResult hotwordDetectedResult, android.media.AudioFormat audioFormatFromCallback, android.os.ParcelFileDescriptor audioStreamFromCallback) {
                wearableCallback.onDetected();
                try {
                    com.android.server.voiceinteraction.DetectorSession.this.mCallback.onKeyphraseDetectedFromExternalSource(hotwordDetectedResult);
                } catch (android.os.RemoteException ex) {
                    android.util.Slog.w(com.android.server.voiceinteraction.DetectorSession.TAG, "RemoteException when sending HotwordDetectedResult to VoiceInteractionService.", ex);
                    wearableCallback.onError("RemoteException when sending HotwordDetectedResult to VoiceInteractionService.");
                    com.android.server.voiceinteraction.DetectorSession.this.notifyOnDetectorRemoteException();
                }
                for (android.service.voice.HotwordAudioStream resultAudioStream : hotwordDetectedResult.getAudioStreams()) {
                    try {
                        resultAudioStream.getAudioStreamParcelFileDescriptor().close();
                    } catch (java.io.IOException ex2) {
                        android.util.Slog.i(com.android.server.voiceinteraction.DetectorSession.TAG, "Unable to close audio stream parcel file descriptor,", ex2);
                    }
                }
            }

            public void onHotwordDetectionServiceFailure(android.service.voice.HotwordDetectionServiceFailure hotwordDetectionServiceFailure) {
                wearableCallback.onError("onHotwordDetectionServiceFailure: " + hotwordDetectionServiceFailure);
            }

            public void onRejected(android.service.voice.HotwordRejectedResult hotwordRejectedResult) {
                wearableCallback.onRejected();
            }

            public android.os.IBinder asBinder() {
                return null;
            }
        };
        handleExternalSourceHotwordDetectionLocked(audioStream, audioFormat, options, voiceInteractionCallback, false, false);
    }

    private void handleExternalSourceHotwordDetectionLocked(android.os.ParcelFileDescriptor audioStream, final android.media.AudioFormat audioFormat, final android.os.PersistableBundle options, final android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback callback, final boolean shouldCloseAudioStreamWithDelayOnDetect, final boolean shouldCheckPermissionsAndAppOpsOnDetected) {
        if (this.mPerformingExternalSourceHotwordDetection) {
            android.util.Slog.i(TAG, "Hotword validation is already in progress for external source.");
            return;
        }
        final java.io.InputStream audioSource = new android.os.ParcelFileDescriptor.AutoCloseInputStream(audioStream);
        android.util.Pair<android.os.ParcelFileDescriptor, android.os.ParcelFileDescriptor> clientPipe = createPipe();
        if (clientPipe == null) {
            return;
        }
        final android.os.ParcelFileDescriptor serviceAudioSink = (android.os.ParcelFileDescriptor) clientPipe.second;
        final android.os.ParcelFileDescriptor serviceAudioSource = (android.os.ParcelFileDescriptor) clientPipe.first;
        this.mCurrentAudioSink = serviceAudioSink;
        this.mPerformingExternalSourceHotwordDetection = true;
        this.mAudioCopyExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.voiceinteraction.DetectorSession$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$handleExternalSourceHotwordDetectionLocked$3(audioSource, serviceAudioSink, callback);
            }
        });
        this.mRemoteDetectionService.run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.voiceinteraction.DetectorSession$$ExternalSyntheticLambda4
            public final void runNoResult(java.lang.Object obj) throws java.lang.Exception {
                this.f$0.lambda$handleExternalSourceHotwordDetectionLocked$4(options, shouldCloseAudioStreamWithDelayOnDetect, serviceAudioSource, audioFormat, serviceAudioSink, audioSource, callback, shouldCheckPermissionsAndAppOpsOnDetected, (android.service.voice.ISandboxedDetectionService) obj);
            }
        });
        com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(getDetectorType(), 10, this.mVoiceInteractionServiceUid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0042 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public /* synthetic */ void lambda$handleExternalSourceHotwordDetectionLocked$3(java.io.InputStream r8, android.os.ParcelFileDescriptor r9, android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback r10) {
        /*
            r7 = this;
            r0 = r8
            r1 = 0
            android.os.ParcelFileDescriptor$AutoCloseOutputStream r2 = new android.os.ParcelFileDescriptor$AutoCloseOutputStream     // Catch: java.lang.Throwable -> L3f
            r2.<init>(r9)     // Catch: java.lang.Throwable -> L3f
            r3 = 1024(0x400, float:1.435E-42)
            byte[] r4 = new byte[r3]     // Catch: java.lang.Throwable -> L35
        Lb:
            int r5 = r0.read(r4, r1, r3)     // Catch: java.lang.Throwable -> L35
            if (r5 >= 0) goto L31
            java.lang.String r3 = "DetectorSession"
            java.lang.String r6 = "Reached end of stream for external hotword"
            android.util.Slog.i(r3, r6)     // Catch: java.lang.Throwable -> L35
            r2.close()     // Catch: java.lang.Throwable -> L3f
            if (r0 == 0) goto L21
            r0.close()     // Catch: java.lang.Throwable -> L4b java.io.IOException -> L4d
        L21:
            java.lang.Object r2 = r7.mLock
            monitor-enter(r2)
            r7.mPerformingExternalSourceHotwordDetection = r1     // Catch: java.lang.Throwable -> L2e
            java.lang.String r0 = "start external source"
            r7.closeExternalAudioStreamLocked(r0)     // Catch: java.lang.Throwable -> L2e
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L2e
            goto L9a
        L2e:
            r0 = move-exception
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L2e
            throw r0
        L31:
            r2.write(r4, r1, r5)     // Catch: java.lang.Throwable -> L35
            goto Lb
        L35:
            r3 = move-exception
            r2.close()     // Catch: java.lang.Throwable -> L3a
            goto L3e
        L3a:
            r4 = move-exception
            r3.addSuppressed(r4)     // Catch: java.lang.Throwable -> L3f
        L3e:
            throw r3     // Catch: java.lang.Throwable -> L3f
        L3f:
            r2 = move-exception
            if (r0 == 0) goto L4a
            r0.close()     // Catch: java.lang.Throwable -> L46
            goto L4a
        L46:
            r3 = move-exception
            r2.addSuppressed(r3)     // Catch: java.lang.Throwable -> L4b java.io.IOException -> L4d
        L4a:
            throw r2     // Catch: java.lang.Throwable -> L4b java.io.IOException -> L4d
        L4b:
            r0 = move-exception
            goto L9f
        L4d:
            r0 = move-exception
            java.lang.String r2 = "DetectorSession"
            java.lang.String r3 = "Failed supplying audio data to validator"
            android.util.Slog.w(r2, r3, r0)     // Catch: java.lang.Throwable -> L4b
            r2 = 3
            android.service.voice.HotwordDetectionServiceFailure r3 = new android.service.voice.HotwordDetectionServiceFailure     // Catch: java.lang.Throwable -> L4b android.os.RemoteException -> L61
            java.lang.String r4 = "Copy audio data failure for external source detection."
            r3.<init>(r2, r4)     // Catch: java.lang.Throwable -> L4b android.os.RemoteException -> L61
            r10.onHotwordDetectionServiceFailure(r3)     // Catch: java.lang.Throwable -> L4b android.os.RemoteException -> L61
            goto L8e
        L61:
            r3 = move-exception
            java.lang.String r4 = "DetectorSession"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L4b
            r5.<init>()     // Catch: java.lang.Throwable -> L4b
            java.lang.String r6 = "Failed to report onHotwordDetectionServiceFailure status: "
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch: java.lang.Throwable -> L4b
            java.lang.StringBuilder r5 = r5.append(r3)     // Catch: java.lang.Throwable -> L4b
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> L4b
            android.util.Slog.w(r4, r5)     // Catch: java.lang.Throwable -> L4b
            int r4 = r7.getDetectorType()     // Catch: java.lang.Throwable -> L4b
            if (r4 == r2) goto L8b
            int r2 = r7.getDetectorType()     // Catch: java.lang.Throwable -> L4b
            int r4 = r7.mVoiceInteractionServiceUid     // Catch: java.lang.Throwable -> L4b
            r5 = 15
            com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(r2, r5, r4)     // Catch: java.lang.Throwable -> L4b
        L8b:
            r7.notifyOnDetectorRemoteException()     // Catch: java.lang.Throwable -> L4b
        L8e:
            java.lang.Object r0 = r7.mLock
            monitor-enter(r0)
            r7.mPerformingExternalSourceHotwordDetection = r1     // Catch: java.lang.Throwable -> L9c
            java.lang.String r1 = "start external source"
            r7.closeExternalAudioStreamLocked(r1)     // Catch: java.lang.Throwable -> L9c
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L9c
        L9a:
            return
        L9c:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L9c
            throw r1
        L9f:
            java.lang.Object r2 = r7.mLock
            monitor-enter(r2)
            r7.mPerformingExternalSourceHotwordDetection = r1     // Catch: java.lang.Throwable -> Lac
            java.lang.String r1 = "start external source"
            r7.closeExternalAudioStreamLocked(r1)     // Catch: java.lang.Throwable -> Lac
            monitor-exit(r2)     // Catch: java.lang.Throwable -> Lac
            throw r0
        Lac:
            r0 = move-exception
            monitor-exit(r2)     // Catch: java.lang.Throwable -> Lac
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.voiceinteraction.DetectorSession.lambda$handleExternalSourceHotwordDetectionLocked$3(java.io.InputStream, android.os.ParcelFileDescriptor, android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleExternalSourceHotwordDetectionLocked$4(android.os.PersistableBundle options, boolean shouldCloseAudioStreamWithDelayOnDetect, android.os.ParcelFileDescriptor serviceAudioSource, android.media.AudioFormat audioFormat, android.os.ParcelFileDescriptor serviceAudioSink, java.io.InputStream audioSource, android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback callback, boolean shouldCheckPermissionsAndAppOpsOnDetected, android.service.voice.ISandboxedDetectionService service) throws java.lang.Exception {
        android.os.PersistableBundle optionsToSend = options;
        if (com.android.internal.hidden_from_bootclasspath.android.app.wearable.Flags.enableHotwordWearableSensingApi()) {
            if (optionsToSend == null) {
                optionsToSend = new android.os.PersistableBundle();
            }
            optionsToSend.putBoolean("android.service.voice.HotwordDetectionService.KEY_SYSTEM_WILL_CLOSE_AUDIO_STREAM_AFTER_CALLBACK", shouldCloseAudioStreamWithDelayOnDetect);
        }
        service.detectFromMicrophoneSource(serviceAudioSource, 2, audioFormat, optionsToSend, new com.android.server.voiceinteraction.DetectorSession.AnonymousClass3(serviceAudioSink, audioSource, callback, shouldCloseAudioStreamWithDelayOnDetect, shouldCheckPermissionsAndAppOpsOnDetected));
        bestEffortClose(serviceAudioSource);
    }

    /* JADX INFO: renamed from: com.android.server.voiceinteraction.DetectorSession$3, reason: invalid class name */
    class AnonymousClass3 extends android.service.voice.IDspHotwordDetectionCallback.Stub {
        final /* synthetic */ java.io.InputStream val$audioSource;
        final /* synthetic */ android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback val$callback;
        final /* synthetic */ android.os.ParcelFileDescriptor val$serviceAudioSink;
        final /* synthetic */ boolean val$shouldCheckPermissionsAndAppOpsOnDetected;
        final /* synthetic */ boolean val$shouldCloseAudioStreamWithDelayOnDetect;

        AnonymousClass3(android.os.ParcelFileDescriptor parcelFileDescriptor, java.io.InputStream inputStream, android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback iMicrophoneHotwordDetectionVoiceInteractionCallback, boolean z, boolean z2) {
            this.val$serviceAudioSink = parcelFileDescriptor;
            this.val$audioSource = inputStream;
            this.val$callback = iMicrophoneHotwordDetectionVoiceInteractionCallback;
            this.val$shouldCloseAudioStreamWithDelayOnDetect = z;
            this.val$shouldCheckPermissionsAndAppOpsOnDetected = z2;
        }

        public void onRejected(android.service.voice.HotwordRejectedResult result) throws android.os.RemoteException {
            synchronized (com.android.server.voiceinteraction.DetectorSession.this.mLock) {
                com.android.server.voiceinteraction.DetectorSession.this.mPerformingExternalSourceHotwordDetection = false;
                com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(com.android.server.voiceinteraction.DetectorSession.this.getDetectorType(), 12, com.android.server.voiceinteraction.DetectorSession.this.mVoiceInteractionServiceUid);
                java.util.concurrent.ScheduledExecutorService scheduledExecutorService = com.android.server.voiceinteraction.DetectorSession.this.mScheduledExecutorService;
                final android.os.ParcelFileDescriptor parcelFileDescriptor = this.val$serviceAudioSink;
                final java.io.InputStream inputStream = this.val$audioSource;
                scheduledExecutorService.schedule(new java.lang.Runnable() { // from class: com.android.server.voiceinteraction.DetectorSession$3$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.voiceinteraction.DetectorSession.bestEffortClose(parcelFileDescriptor, inputStream);
                    }
                }, com.android.server.voiceinteraction.DetectorSession.EXTERNAL_HOTWORD_CLEANUP_MILLIS, java.util.concurrent.TimeUnit.MILLISECONDS);
                try {
                    this.val$callback.onRejected(result);
                    if (result != null) {
                        android.util.Slog.i(com.android.server.voiceinteraction.DetectorSession.TAG, "Egressed 'hotword rejected result' from hotword trusted process");
                        if (com.android.server.voiceinteraction.DetectorSession.this.mDebugHotwordLogging) {
                            android.util.Slog.i(com.android.server.voiceinteraction.DetectorSession.TAG, "Egressed detected result: " + result);
                        }
                    }
                } catch (android.os.RemoteException e) {
                    com.android.server.voiceinteraction.DetectorSession.this.notifyOnDetectorRemoteException();
                    throw e;
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:23:0x00a4 A[Catch: all -> 0x00e6, TryCatch #2 {, blocks: (B:4:0x0005, B:6:0x001d, B:7:0x0031, B:9:0x0035, B:19:0x006a, B:20:0x0075, B:21:0x007c, B:23:0x00a4, B:24:0x00bc, B:30:0x00c7, B:31:0x00ce, B:32:0x00dc, B:35:0x00df, B:36:0x00e5, B:27:0x00bf, B:28:0x00c5, B:12:0x003c, B:13:0x0052, B:14:0x0060, B:17:0x0063, B:18:0x0069), top: B:44:0x0005, inners: #0, #1, #3, #4, #5 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onDetected(android.service.voice.HotwordDetectedResult r8) throws android.os.RemoteException {
            /*
                Method dump skipped, instruction units count: 233
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.voiceinteraction.DetectorSession.AnonymousClass3.onDetected(android.service.voice.HotwordDetectedResult):void");
        }
    }

    void initialize(android.os.PersistableBundle options, android.os.SharedMemory sharedMemory) {
        synchronized (this.mLock) {
            if (!this.mInitialized && !this.mDestroyed) {
                updateStateAfterProcessStartLocked(options, sharedMemory);
                this.mInitialized = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroyLocked() {
        this.mDestroyed = true;
        this.mDebugHotwordLogging = false;
        this.mRemoteDetectionService = null;
        this.mRemoteExceptionListener = null;
        if (this.mAttentionManagerInternal != null) {
            this.mAttentionManagerInternal.onStopProximityUpdates(this.mProximityCallbackInternal);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDebugHotwordLoggingLocked(boolean logging) {
        android.util.Slog.v(TAG, "setDebugHotwordLoggingLocked: " + logging);
        this.mDebugHotwordLogging = logging;
    }

    void updateRemoteSandboxedDetectionServiceLocked(com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnection remoteDetectionService) {
        this.mRemoteDetectionService = remoteDetectionService;
    }

    private void reportErrorGetRemoteException() {
        if (getDetectorType() != 3) {
            com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(getDetectorType(), 15, this.mVoiceInteractionServiceUid);
        }
        notifyOnDetectorRemoteException();
    }

    void reportErrorLocked(android.service.voice.HotwordDetectionServiceFailure hotwordDetectionServiceFailure) {
        try {
            this.mCallback.onHotwordDetectionServiceFailure(hotwordDetectionServiceFailure);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to call onHotwordDetectionServiceFailure: " + e);
            reportErrorGetRemoteException();
        }
    }

    void reportErrorLocked(android.service.voice.VisualQueryDetectionServiceFailure visualQueryDetectionServiceFailure) {
        try {
            this.mCallback.onVisualQueryDetectionServiceFailure(visualQueryDetectionServiceFailure);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to call onVisualQueryDetectionServiceFailure: " + e);
            reportErrorGetRemoteException();
        }
    }

    void reportErrorLocked(java.lang.String errorMessage) {
        try {
            this.mCallback.onUnknownFailure(errorMessage);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to call onUnknownFailure: " + e);
            reportErrorGetRemoteException();
        }
    }

    boolean isSameCallback(com.android.internal.app.IHotwordRecognitionStatusCallback callback) {
        synchronized (this.mLock) {
            if (callback == null) {
                return false;
            }
            return this.mCallback.asBinder().equals(callback.asBinder());
        }
    }

    boolean isSameToken(android.os.IBinder token) {
        synchronized (this.mLock) {
            if (token == null) {
                return false;
            }
            return this.mToken == token;
        }
    }

    boolean isDestroyed() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mDestroyed;
        }
        return z;
    }

    private static android.util.Pair<android.os.ParcelFileDescriptor, android.os.ParcelFileDescriptor> createPipe() {
        try {
            android.os.ParcelFileDescriptor[] fileDescriptors = android.os.ParcelFileDescriptor.createPipe();
            return android.util.Pair.create(fileDescriptors[0], fileDescriptors[1]);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to create audio stream pipe", e);
            return null;
        }
    }

    void saveProximityValueToBundle(android.service.voice.HotwordDetectedResult result) {
        synchronized (this.mLock) {
            if (result != null) {
                if (this.mProximityMeters != -1.0d) {
                    result.setProximity(this.mProximityMeters);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setProximityValue(double proximityMeters) {
        synchronized (this.mLock) {
            this.mProximityMeters = proximityMeters;
        }
    }

    void closeExternalAudioStreamLocked(java.lang.String reason) {
        if (this.mCurrentAudioSink != null) {
            android.util.Slog.i(TAG, "Closing external audio stream to hotword detector: " + reason);
            bestEffortClose(this.mCurrentAudioSink);
            this.mCurrentAudioSink = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void bestEffortClose(java.io.Closeable... closeables) {
        for (java.io.Closeable closeable : closeables) {
            bestEffortClose(closeable);
        }
    }

    private static void bestEffortClose(java.io.Closeable closeable) {
        try {
            closeable.close();
        } catch (java.io.IOException e) {
        }
    }

    void enforcePermissionsForDataDelivery() {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.voiceinteraction.DetectorSession$$ExternalSyntheticLambda5
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$enforcePermissionsForDataDelivery$5();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enforcePermissionsForDataDelivery$5() throws java.lang.Exception {
        synchronized (this.mLock) {
            if (com.android.server.policy.AppOpsPolicy.isHotwordDetectionServiceRequired(this.mContext.getPackageManager())) {
                int result = android.content.PermissionChecker.checkPermissionForPreflight(this.mContext, "android.permission.RECORD_AUDIO", -1, this.mVoiceInteractorIdentity.uid, this.mVoiceInteractorIdentity.packageName);
                if (result != 0) {
                    throw new java.lang.SecurityException("Failed to obtain permission RECORD_AUDIO for identity " + this.mVoiceInteractorIdentity);
                }
                int opMode = this.mAppOpsManager.unsafeCheckOpNoThrow(android.app.AppOpsManager.opToPublicName(com.android.server.policy.AppOpsPolicy.getVoiceActivationOp()), this.mVoiceInteractorIdentity.uid, this.mVoiceInteractorIdentity.packageName);
                if (opMode != 3 && opMode != 0) {
                    throw new java.lang.SecurityException("The app op OP_RECEIVE_SANDBOX_TRIGGER_AUDIO is denied for identity" + this.mVoiceInteractorIdentity);
                }
                this.mAppOpsManager.noteOpNoThrow(com.android.server.policy.AppOpsPolicy.getVoiceActivationOp(), this.mVoiceInteractorIdentity.uid, this.mVoiceInteractorIdentity.packageName, this.mVoiceInteractorIdentity.attributionTag, HOTWORD_DETECTION_OP_MESSAGE);
            } else {
                enforcePermissionForDataDelivery(this.mContext, this.mVoiceInteractorIdentity, "android.permission.RECORD_AUDIO", HOTWORD_DETECTION_OP_MESSAGE);
            }
            enforcePermissionForDataDelivery(this.mContext, this.mVoiceInteractorIdentity, "android.permission.CAPTURE_AUDIO_HOTWORD", HOTWORD_DETECTION_OP_MESSAGE);
        }
    }

    protected static void enforcePermissionForDataDelivery(android.content.Context context, android.media.permission.Identity identity, java.lang.String permission, java.lang.String reason) {
        int status = android.media.permission.PermissionUtil.checkPermissionForDataDelivery(context, identity, permission, reason);
        if (status != 0) {
            throw new java.lang.SecurityException(android.text.TextUtils.formatSimple("Failed to obtain permission %s for identity %s", new java.lang.Object[]{permission, identity}));
        }
    }

    void enforceExtraKeyphraseIdNotLeaked(android.service.voice.HotwordDetectedResult result, android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent recognitionEvent) {
        if (!android.app.compat.CompatChanges.isChangeEnabled(com.android.server.voiceinteraction.HotwordDetectionConnection.ENFORCE_HOTWORD_PHRASE_ID, this.mVoiceInteractionServiceUid)) {
            return;
        }
        for (android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra keyphrase : recognitionEvent.keyphraseExtras) {
            if (keyphrase.getKeyphraseId() == result.getHotwordPhraseId()) {
                return;
            }
        }
        throw new java.lang.SecurityException("Ignoring #onDetected due to trusted service sharing a keyphrase ID which the DSP did not detect");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDetectorType() {
        if (this instanceof com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession) {
            return 1;
        }
        if (this instanceof com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession) {
            return 2;
        }
        if (this instanceof com.android.server.voiceinteraction.VisualQueryDetectorSession) {
            return 3;
        }
        android.util.Slog.v(TAG, "Unexpected detector type");
        return -1;
    }

    public void dumpLocked(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.print(prefix);
        pw.print("mCallback=");
        pw.println(this.mCallback);
        pw.print(prefix);
        pw.print("mUpdateStateAfterStartFinished=");
        pw.println(this.mUpdateStateAfterStartFinished);
        pw.print(prefix);
        pw.print("mInitialized=");
        pw.println(this.mInitialized);
        pw.print(prefix);
        pw.print("mDestroyed=");
        pw.println(this.mDestroyed);
        pw.print(prefix);
        pw.print("DetectorType=");
        pw.println(android.service.voice.HotwordDetector.detectorTypeToString(getDetectorType()));
        pw.print(prefix);
        pw.print("mPerformingExternalSourceHotwordDetection=");
        pw.println(this.mPerformingExternalSourceHotwordDetection);
    }
}
