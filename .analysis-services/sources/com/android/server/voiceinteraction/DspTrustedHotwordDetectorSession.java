package com.android.server.voiceinteraction;

/* JADX INFO: loaded from: classes3.dex */
final class DspTrustedHotwordDetectorSession extends com.android.server.voiceinteraction.DetectorSession {
    private static final long MAX_VALIDATION_TIMEOUT_MILLIS = 4000;
    private static final java.lang.String TAG = "DspTrustedHotwordDetectorSession";
    private static final long VALIDATION_TIMEOUT_MILLIS = 3000;
    private java.util.concurrent.ScheduledFuture<?> mCancellationKeyPhraseDetectionFuture;
    private android.service.voice.HotwordRejectedResult mLastHotwordRejectedResult;
    private boolean mValidatingDspTrigger;

    DspTrustedHotwordDetectorSession(com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnection remoteHotwordDetectionService, java.lang.Object lock, android.content.Context context, android.os.IBinder token, com.android.internal.app.IHotwordRecognitionStatusCallback callback, int voiceInteractionServiceUid, android.media.permission.Identity voiceInteractorIdentity, java.util.concurrent.ScheduledExecutorService scheduledExecutorService, boolean logging, com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.DetectorRemoteExceptionListener listener, int userId) {
        super(remoteHotwordDetectionService, lock, context, token, callback, voiceInteractionServiceUid, voiceInteractorIdentity, scheduledExecutorService, logging, listener, userId);
        this.mValidatingDspTrigger = false;
        this.mLastHotwordRejectedResult = null;
    }

    void detectFromDspSourceLocked(final android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent recognitionEvent, final com.android.internal.app.IHotwordRecognitionStatusCallback externalCallback) {
        final java.util.concurrent.atomic.AtomicBoolean timeoutDetected = new java.util.concurrent.atomic.AtomicBoolean(false);
        final android.service.voice.IDspHotwordDetectionCallback.Stub stub = new android.service.voice.IDspHotwordDetectionCallback.Stub() { // from class: com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.1
            public void onDetected(android.service.voice.HotwordDetectedResult result) throws android.os.RemoteException {
                synchronized (com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mLock) {
                    if (com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mCancellationKeyPhraseDetectionFuture != null) {
                        com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mCancellationKeyPhraseDetectionFuture.cancel(true);
                    }
                    if (timeoutDetected.get()) {
                        return;
                    }
                    com.android.server.voiceinteraction.HotwordMetricsLogger.writeKeyphraseTriggerEvent(1, 5, com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mVoiceInteractionServiceUid);
                    if (!com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mValidatingDspTrigger) {
                        android.util.Slog.i(com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.TAG, "Ignoring #onDetected due to a process restart or previous #onRejected result = " + com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mLastHotwordRejectedResult);
                        com.android.server.voiceinteraction.HotwordMetricsLogger.writeKeyphraseTriggerEvent(1, 7, com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mVoiceInteractionServiceUid);
                        return;
                    }
                    com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mValidatingDspTrigger = false;
                    try {
                        com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.enforcePermissionsForDataDelivery();
                        com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.enforceExtraKeyphraseIdNotLeaked(result, recognitionEvent);
                        com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.saveProximityValueToBundle(result);
                        try {
                            android.service.voice.HotwordDetectedResult newResult = com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mHotwordAudioStreamCopier.startCopyingAudioStreams(result);
                            try {
                                externalCallback.onKeyphraseDetected(recognitionEvent, newResult);
                                android.util.Slog.i(com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.TAG, "Egressed " + android.service.voice.HotwordDetectedResult.getUsageSize(newResult) + " bits from hotword trusted process");
                                if (com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mDebugHotwordLogging) {
                                    android.util.Slog.i(com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.TAG, "Egressed detected result: " + newResult);
                                }
                            } catch (android.os.RemoteException e) {
                                com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.notifyOnDetectorRemoteException();
                                com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(1, 17, com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mVoiceInteractionServiceUid);
                                throw e;
                            }
                        } catch (java.io.IOException e2) {
                            try {
                                android.util.Slog.w(com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.TAG, "Ignoring #onDetected due to a IOException", e2);
                                externalCallback.onHotwordDetectionServiceFailure(new android.service.voice.HotwordDetectionServiceFailure(6, "Copy audio stream failure."));
                            } catch (android.os.RemoteException e1) {
                                com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.notifyOnDetectorRemoteException();
                                throw e1;
                            }
                        }
                    } catch (java.lang.SecurityException e3) {
                        android.util.Slog.w(com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.TAG, "Ignoring #onDetected due to a SecurityException", e3);
                        com.android.server.voiceinteraction.HotwordMetricsLogger.writeKeyphraseTriggerEvent(1, 8, com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mVoiceInteractionServiceUid);
                        try {
                            externalCallback.onHotwordDetectionServiceFailure(new android.service.voice.HotwordDetectionServiceFailure(5, "Security exception occurs in #onDetected method."));
                        } catch (android.os.RemoteException e12) {
                            com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.notifyOnDetectorRemoteException();
                            com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(1, 15, com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mVoiceInteractionServiceUid);
                            throw e12;
                        }
                    }
                }
            }

            public void onRejected(android.service.voice.HotwordRejectedResult result) throws android.os.RemoteException {
                synchronized (com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mLock) {
                    if (com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mCancellationKeyPhraseDetectionFuture != null) {
                        com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mCancellationKeyPhraseDetectionFuture.cancel(true);
                    }
                    if (timeoutDetected.get()) {
                        return;
                    }
                    com.android.server.voiceinteraction.HotwordMetricsLogger.writeKeyphraseTriggerEvent(1, 6, com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mVoiceInteractionServiceUid);
                    if (!com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mValidatingDspTrigger) {
                        android.util.Slog.i(com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.TAG, "Ignoring #onRejected due to a process restart");
                        com.android.server.voiceinteraction.HotwordMetricsLogger.writeKeyphraseTriggerEvent(1, 9, com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mVoiceInteractionServiceUid);
                        return;
                    }
                    com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mValidatingDspTrigger = false;
                    try {
                        externalCallback.onRejected(result);
                        com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mLastHotwordRejectedResult = result;
                        if (com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mDebugHotwordLogging && result != null) {
                            android.util.Slog.i(com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.TAG, "Egressed rejected result: " + result);
                        }
                    } catch (android.os.RemoteException e) {
                        com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.notifyOnDetectorRemoteException();
                        com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(1, 16, com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession.this.mVoiceInteractionServiceUid);
                        throw e;
                    }
                }
            }
        };
        this.mValidatingDspTrigger = true;
        this.mLastHotwordRejectedResult = null;
        this.mRemoteDetectionService.run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession$$ExternalSyntheticLambda0
            public final void runNoResult(java.lang.Object obj) throws java.lang.Exception {
                this.f$0.lambda$detectFromDspSourceLocked$1(timeoutDetected, externalCallback, recognitionEvent, stub, (android.service.voice.ISandboxedDetectionService) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$detectFromDspSourceLocked$1(final java.util.concurrent.atomic.AtomicBoolean timeoutDetected, final com.android.internal.app.IHotwordRecognitionStatusCallback externalCallback, android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent recognitionEvent, android.service.voice.IDspHotwordDetectionCallback internalCallback, android.service.voice.ISandboxedDetectionService service) throws java.lang.Exception {
        this.mCancellationKeyPhraseDetectionFuture = this.mScheduledExecutorService.schedule(new java.lang.Runnable() { // from class: com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$detectFromDspSourceLocked$0(timeoutDetected, externalCallback);
            }
        }, MAX_VALIDATION_TIMEOUT_MILLIS, java.util.concurrent.TimeUnit.MILLISECONDS);
        service.detectFromDspSource(recognitionEvent, recognitionEvent.getCaptureFormat(), 3000L, internalCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$detectFromDspSourceLocked$0(java.util.concurrent.atomic.AtomicBoolean timeoutDetected, com.android.internal.app.IHotwordRecognitionStatusCallback externalCallback) {
        timeoutDetected.set(true);
        android.util.Slog.w(TAG, "Timed out on #detectFromDspSource");
        com.android.server.voiceinteraction.HotwordMetricsLogger.writeKeyphraseTriggerEvent(1, 2, this.mVoiceInteractionServiceUid);
        try {
            externalCallback.onHotwordDetectionServiceFailure(new android.service.voice.HotwordDetectionServiceFailure(4, "Timeout to response to the detection result."));
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to report onError status: ", e);
            com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(1, 15, this.mVoiceInteractionServiceUid);
            notifyOnDetectorRemoteException();
        }
    }

    @Override // com.android.server.voiceinteraction.DetectorSession
    void informRestartProcessLocked() {
        android.util.Slog.v(TAG, "informRestartProcessLocked");
        if (this.mValidatingDspTrigger) {
            try {
                this.mCallback.onRejected(new android.service.voice.HotwordRejectedResult.Builder().build());
                com.android.server.voiceinteraction.HotwordMetricsLogger.writeKeyphraseTriggerEvent(1, 10, this.mVoiceInteractionServiceUid);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to call #rejected");
                com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(1, 16, this.mVoiceInteractionServiceUid);
                notifyOnDetectorRemoteException();
            }
            this.mValidatingDspTrigger = false;
        }
        this.mUpdateStateAfterStartFinished.set(false);
        try {
            this.mCallback.onProcessRestarted();
        } catch (android.os.RemoteException e2) {
            android.util.Slog.w(TAG, "Failed to communicate #onProcessRestarted", e2);
            com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(1, 18, this.mVoiceInteractionServiceUid);
            notifyOnDetectorRemoteException();
        }
        this.mPerformingExternalSourceHotwordDetection = false;
        closeExternalAudioStreamLocked("process restarted");
    }

    @Override // com.android.server.voiceinteraction.DetectorSession
    public void dumpLocked(java.lang.String prefix, java.io.PrintWriter pw) {
        super.dumpLocked(prefix, pw);
        pw.print(prefix);
        pw.print("mValidatingDspTrigger=");
        pw.println(this.mValidatingDspTrigger);
    }
}
