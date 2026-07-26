package com.android.server.voiceinteraction;

/* JADX INFO: loaded from: classes3.dex */
final class SoftwareTrustedHotwordDetectorSession extends com.android.server.voiceinteraction.DetectorSession {
    private static final java.lang.String TAG = "SoftwareTrustedHotwordDetectorSession";
    private boolean mPerformingSoftwareHotwordDetection;
    private android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback mSoftwareCallback;

    SoftwareTrustedHotwordDetectorSession(com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnection remoteHotwordDetectionService, java.lang.Object lock, android.content.Context context, android.os.IBinder token, com.android.internal.app.IHotwordRecognitionStatusCallback callback, int voiceInteractionServiceUid, android.media.permission.Identity voiceInteractorIdentity, java.util.concurrent.ScheduledExecutorService scheduledExecutorService, boolean logging, com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.DetectorRemoteExceptionListener listener, int userId) {
        super(remoteHotwordDetectionService, lock, context, token, callback, voiceInteractionServiceUid, voiceInteractorIdentity, scheduledExecutorService, logging, listener, userId);
    }

    void startListeningFromMicLocked(android.media.AudioFormat audioFormat, android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback callback) {
        this.mSoftwareCallback = callback;
        if (this.mPerformingSoftwareHotwordDetection) {
            android.util.Slog.i(TAG, "Hotword validation is already in progress, ignoring.");
        } else {
            this.mPerformingSoftwareHotwordDetection = true;
            startListeningFromMicLocked();
        }
    }

    private void startListeningFromMicLocked() {
        final android.service.voice.IDspHotwordDetectionCallback.Stub stub = new android.service.voice.IDspHotwordDetectionCallback.Stub() { // from class: com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.1
            public void onDetected(android.service.voice.HotwordDetectedResult result) throws android.os.RemoteException {
                synchronized (com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.mLock) {
                    com.android.server.voiceinteraction.HotwordMetricsLogger.writeKeyphraseTriggerEvent(2, 5, com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.mVoiceInteractionServiceUid);
                    if (!com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.mPerformingSoftwareHotwordDetection) {
                        android.util.Slog.i(com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.TAG, "Hotword detection has already completed");
                        com.android.server.voiceinteraction.HotwordMetricsLogger.writeKeyphraseTriggerEvent(2, 7, com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.mVoiceInteractionServiceUid);
                        return;
                    }
                    com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.mPerformingSoftwareHotwordDetection = false;
                    try {
                        com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.enforcePermissionsForDataDelivery();
                        com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.saveProximityValueToBundle(result);
                        try {
                            android.service.voice.HotwordDetectedResult newResult = com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.mHotwordAudioStreamCopier.startCopyingAudioStreams(result);
                            try {
                                com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.mSoftwareCallback.onDetected(newResult, (android.media.AudioFormat) null, (android.os.ParcelFileDescriptor) null);
                                android.util.Slog.i(com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.TAG, "Egressed " + android.service.voice.HotwordDetectedResult.getUsageSize(newResult) + " bits from hotword trusted process");
                                if (com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.mDebugHotwordLogging) {
                                    android.util.Slog.i(com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.TAG, "Egressed detected result: " + newResult);
                                }
                            } catch (android.os.RemoteException e1) {
                                com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.notifyOnDetectorRemoteException();
                                com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(2, 17, com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.mVoiceInteractionServiceUid);
                                throw e1;
                            }
                        } catch (java.io.IOException e) {
                            android.util.Slog.w(com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.TAG, "Ignoring #onDetected due to a IOException", e);
                            try {
                                com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.mSoftwareCallback.onHotwordDetectionServiceFailure(new android.service.voice.HotwordDetectionServiceFailure(6, "Copy audio stream failure."));
                            } catch (android.os.RemoteException e12) {
                                com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.notifyOnDetectorRemoteException();
                                com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(2, 15, com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.mVoiceInteractionServiceUid);
                                throw e12;
                            }
                        }
                    } catch (java.lang.SecurityException e2) {
                        android.util.Slog.w(com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.TAG, "Ignoring #onDetected due to a SecurityException", e2);
                        com.android.server.voiceinteraction.HotwordMetricsLogger.writeKeyphraseTriggerEvent(2, 8, com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.mVoiceInteractionServiceUid);
                        try {
                            com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.mSoftwareCallback.onHotwordDetectionServiceFailure(new android.service.voice.HotwordDetectionServiceFailure(5, "Security exception occurs in #onDetected method."));
                        } catch (android.os.RemoteException e13) {
                            com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.notifyOnDetectorRemoteException();
                            com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(2, 15, com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.mVoiceInteractionServiceUid);
                            throw e13;
                        }
                    }
                }
            }

            public void onRejected(android.service.voice.HotwordRejectedResult result) throws android.os.RemoteException {
                com.android.server.voiceinteraction.HotwordMetricsLogger.writeKeyphraseTriggerEvent(2, 6, com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession.this.mVoiceInteractionServiceUid);
            }
        };
        this.mRemoteDetectionService.run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession$$ExternalSyntheticLambda1
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.voice.ISandboxedDetectionService) obj).detectFromMicrophoneSource((android.os.ParcelFileDescriptor) null, 1, (android.media.AudioFormat) null, (android.os.PersistableBundle) null, stub);
            }
        });
        com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(2, 9, this.mVoiceInteractionServiceUid);
    }

    void stopListeningFromMicLocked() {
        if (!this.mPerformingSoftwareHotwordDetection) {
            android.util.Slog.i(TAG, "Hotword detection is not running");
            return;
        }
        this.mPerformingSoftwareHotwordDetection = false;
        this.mRemoteDetectionService.run(new com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession$$ExternalSyntheticLambda0());
        closeExternalAudioStreamLocked("stopping requested");
    }

    @Override // com.android.server.voiceinteraction.DetectorSession
    void informRestartProcessLocked() {
        android.util.Slog.v(TAG, "informRestartProcessLocked");
        this.mUpdateStateAfterStartFinished.set(false);
        try {
            this.mCallback.onProcessRestarted();
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to communicate #onProcessRestarted", e);
            com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(2, 18, this.mVoiceInteractionServiceUid);
            notifyOnDetectorRemoteException();
        }
        if (this.mPerformingSoftwareHotwordDetection) {
            android.util.Slog.i(TAG, "Process restarted: calling startRecognition() again");
            startListeningFromMicLocked();
        }
        this.mPerformingExternalSourceHotwordDetection = false;
        closeExternalAudioStreamLocked("process restarted");
    }

    @Override // com.android.server.voiceinteraction.DetectorSession
    public void dumpLocked(java.lang.String prefix, java.io.PrintWriter pw) {
        super.dumpLocked(prefix, pw);
        pw.print(prefix);
        pw.print("mPerformingSoftwareHotwordDetection=");
        pw.println(this.mPerformingSoftwareHotwordDetection);
    }
}
