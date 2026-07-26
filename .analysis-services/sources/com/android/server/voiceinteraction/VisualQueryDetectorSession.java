package com.android.server.voiceinteraction;

/* JADX INFO: loaded from: classes3.dex */
final class VisualQueryDetectorSession extends com.android.server.voiceinteraction.DetectorSession {
    private static final java.lang.String TAG = "VisualQueryDetectorSession";
    private static final java.lang.String VISUAL_QUERY_DETECTION_AUDIO_OP_MESSAGE = "Providing query detection result from VisualQueryDetectionService to VoiceInteractionService";
    private static final java.lang.String VISUAL_QUERY_DETECTION_CAMERA_OP_MESSAGE = "Providing query detection result from VisualQueryDetectionService to VoiceInteractionService";
    private com.android.internal.app.IVisualQueryDetectionAttentionListener mAttentionListener;
    private boolean mEgressingData;
    private boolean mEnableAccessibilityDataEgress;
    private boolean mQueryStreaming;

    VisualQueryDetectorSession(com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnection remoteService, java.lang.Object lock, android.content.Context context, android.os.IBinder token, com.android.internal.app.IHotwordRecognitionStatusCallback callback, int voiceInteractionServiceUid, android.media.permission.Identity voiceInteractorIdentity, java.util.concurrent.ScheduledExecutorService scheduledExecutorService, boolean logging, com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.DetectorRemoteExceptionListener listener, int userId) {
        super(remoteService, lock, context, token, callback, voiceInteractionServiceUid, voiceInteractorIdentity, scheduledExecutorService, logging, listener, userId);
        this.mEgressingData = false;
        this.mQueryStreaming = false;
        this.mAttentionListener = null;
        this.mEnableAccessibilityDataEgress = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "visual_query_accessibility_detection_enabled", 0, this.mUserId) == 1;
    }

    @Override // com.android.server.voiceinteraction.DetectorSession
    void informRestartProcessLocked() {
        android.util.Slog.v(TAG, "informRestartProcessLocked");
        this.mUpdateStateAfterStartFinished.set(false);
        try {
            this.mCallback.onProcessRestarted();
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to communicate #onProcessRestarted", e);
            notifyOnDetectorRemoteException();
        }
    }

    void setVisualQueryDetectionAttentionListenerLocked(com.android.internal.app.IVisualQueryDetectionAttentionListener listener) {
        this.mAttentionListener = listener;
    }

    boolean startPerceivingLocked(final android.service.voice.IVisualQueryDetectionVoiceInteractionCallback callback) {
        final android.service.voice.IDetectorSessionVisualQueryDetectionCallback.Stub stub = new android.service.voice.IDetectorSessionVisualQueryDetectionCallback.Stub() { // from class: com.android.server.voiceinteraction.VisualQueryDetectorSession.1
            public void onAttentionGained(android.service.voice.VisualQueryAttentionResult attentionResult) {
                android.util.Slog.v(com.android.server.voiceinteraction.VisualQueryDetectorSession.TAG, "BinderCallback#onAttentionGained");
                synchronized (com.android.server.voiceinteraction.VisualQueryDetectorSession.this.mLock) {
                    com.android.server.voiceinteraction.VisualQueryDetectorSession.this.mEgressingData = true;
                    if (com.android.server.voiceinteraction.VisualQueryDetectorSession.this.mAttentionListener == null) {
                        return;
                    }
                    try {
                        com.android.server.voiceinteraction.VisualQueryDetectorSession.this.mAttentionListener.onAttentionGained(attentionResult);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.voiceinteraction.VisualQueryDetectorSession.TAG, "Error delivering attention gained event.", e);
                        try {
                            callback.onVisualQueryDetectionServiceFailure(new android.service.voice.VisualQueryDetectionServiceFailure(3, "Attention listener fails to switch to GAINED state."));
                        } catch (android.os.RemoteException e2) {
                            android.util.Slog.v(com.android.server.voiceinteraction.VisualQueryDetectorSession.TAG, "Fail to call onVisualQueryDetectionServiceFailure");
                        }
                    }
                }
            }

            public void onAttentionLost(int interactionIntention) {
                android.util.Slog.v(com.android.server.voiceinteraction.VisualQueryDetectorSession.TAG, "BinderCallback#onAttentionLost");
                synchronized (com.android.server.voiceinteraction.VisualQueryDetectorSession.this.mLock) {
                    com.android.server.voiceinteraction.VisualQueryDetectorSession.this.mEgressingData = false;
                    if (com.android.server.voiceinteraction.VisualQueryDetectorSession.this.mAttentionListener == null) {
                        return;
                    }
                    try {
                        com.android.server.voiceinteraction.VisualQueryDetectorSession.this.mAttentionListener.onAttentionLost(interactionIntention);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.voiceinteraction.VisualQueryDetectorSession.TAG, "Error delivering attention lost event.", e);
                        try {
                            callback.onVisualQueryDetectionServiceFailure(new android.service.voice.VisualQueryDetectionServiceFailure(3, "Attention listener fails to switch to LOST state."));
                        } catch (android.os.RemoteException e2) {
                            android.util.Slog.v(com.android.server.voiceinteraction.VisualQueryDetectorSession.TAG, "Fail to call onVisualQueryDetectionServiceFailure");
                        }
                    }
                }
            }

            public void onQueryDetected(java.lang.String partialQuery) throws android.os.RemoteException {
                android.util.Slog.v(com.android.server.voiceinteraction.VisualQueryDetectorSession.TAG, "BinderCallback#onQueryDetected");
                synchronized (com.android.server.voiceinteraction.VisualQueryDetectorSession.this.mLock) {
                    java.util.Objects.requireNonNull(partialQuery);
                    if (!com.android.server.voiceinteraction.VisualQueryDetectorSession.this.mEgressingData) {
                        android.util.Slog.v(com.android.server.voiceinteraction.VisualQueryDetectorSession.TAG, "Query should not be egressed within the unattention state.");
                        callback.onVisualQueryDetectionServiceFailure(new android.service.voice.VisualQueryDetectionServiceFailure(4, "Cannot stream queries without attention signals."));
                        return;
                    }
                    try {
                        com.android.server.voiceinteraction.VisualQueryDetectorSession.this.enforcePermissionsForVisualQueryDelivery("android.permission.RECORD_AUDIO", 27, "Providing query detection result from VisualQueryDetectionService to VoiceInteractionService");
                        com.android.server.voiceinteraction.VisualQueryDetectorSession.this.mQueryStreaming = true;
                        callback.onQueryDetected(partialQuery);
                        android.util.Slog.i(com.android.server.voiceinteraction.VisualQueryDetectorSession.TAG, "Egressed from visual query detection process.");
                    } catch (java.lang.SecurityException e) {
                        android.util.Slog.w(com.android.server.voiceinteraction.VisualQueryDetectorSession.TAG, "Ignoring #onQueryDetected due to a SecurityException", e);
                        try {
                            callback.onVisualQueryDetectionServiceFailure(new android.service.voice.VisualQueryDetectionServiceFailure(4, "Cannot stream queries without audio permission."));
                        } catch (android.os.RemoteException e1) {
                            com.android.server.voiceinteraction.VisualQueryDetectorSession.this.notifyOnDetectorRemoteException();
                            throw e1;
                        }
                    }
                }
            }

            /* JADX WARN: Removed duplicated region for block: B:49:0x0083 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onResultDetected(android.service.voice.VisualQueryDetectedResult r7) throws android.os.RemoteException {
                /*
                    r6 = this;
                    java.lang.String r0 = "VisualQueryDetectorSession"
                    java.lang.String r1 = "BinderCallback#onResultDetected"
                    android.util.Slog.v(r0, r1)
                    com.android.server.voiceinteraction.VisualQueryDetectorSession r0 = com.android.server.voiceinteraction.VisualQueryDetectorSession.this
                    java.lang.Object r0 = r0.mLock
                    monitor-enter(r0)
                    java.util.Objects.requireNonNull(r7)     // Catch: java.lang.Throwable -> Lc2
                    com.android.server.voiceinteraction.VisualQueryDetectorSession r1 = com.android.server.voiceinteraction.VisualQueryDetectorSession.this     // Catch: java.lang.Throwable -> Lc2
                    boolean r1 = com.android.server.voiceinteraction.VisualQueryDetectorSession.m10195$$Nest$fgetmEgressingData(r1)     // Catch: java.lang.Throwable -> Lc2
                    r2 = 4
                    if (r1 != 0) goto L2d
                    java.lang.String r1 = "VisualQueryDetectorSession"
                    java.lang.String r3 = "Result should not be egressed within the unattention state."
                    android.util.Slog.v(r1, r3)     // Catch: java.lang.Throwable -> Lc2
                    android.service.voice.IVisualQueryDetectionVoiceInteractionCallback r1 = r2     // Catch: java.lang.Throwable -> Lc2
                    android.service.voice.VisualQueryDetectionServiceFailure r3 = new android.service.voice.VisualQueryDetectionServiceFailure     // Catch: java.lang.Throwable -> Lc2
                    java.lang.String r4 = "Cannot stream results without attention signals."
                    r3.<init>(r2, r4)     // Catch: java.lang.Throwable -> Lc2
                    r1.onVisualQueryDetectionServiceFailure(r3)     // Catch: java.lang.Throwable -> Lc2
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> Lc2
                    return
                L2d:
                    boolean r1 = r6.checkDetectedResultDataLocked(r7)     // Catch: java.lang.Throwable -> Lc2
                    if (r1 != 0) goto L48
                    java.lang.String r1 = "VisualQueryDetectorSession"
                    java.lang.String r3 = "Accessibility data can be egressed only when the isAccessibilityDetectionEnabled() is true."
                    android.util.Slog.v(r1, r3)     // Catch: java.lang.Throwable -> Lc2
                    android.service.voice.IVisualQueryDetectionVoiceInteractionCallback r1 = r2     // Catch: java.lang.Throwable -> Lc2
                    android.service.voice.VisualQueryDetectionServiceFailure r3 = new android.service.voice.VisualQueryDetectionServiceFailure     // Catch: java.lang.Throwable -> Lc2
                    java.lang.String r4 = "Cannot stream accessibility data without enabling the setting."
                    r3.<init>(r2, r4)     // Catch: java.lang.Throwable -> Lc2
                    r1.onVisualQueryDetectionServiceFailure(r3)     // Catch: java.lang.Throwable -> Lc2
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> Lc2
                    return
                L48:
                    byte[] r1 = r7.getAccessibilityDetectionData()     // Catch: java.lang.Throwable -> Lc2
                    if (r1 == 0) goto L79
                    com.android.server.voiceinteraction.VisualQueryDetectorSession r1 = com.android.server.voiceinteraction.VisualQueryDetectorSession.this     // Catch: java.lang.SecurityException -> L5a java.lang.Throwable -> Lc2
                    java.lang.String r3 = "android.permission.CAMERA"
                    java.lang.String r4 = "Providing query detection result from VisualQueryDetectionService to VoiceInteractionService"
                    r5 = 26
                    r1.enforcePermissionsForVisualQueryDelivery(r3, r5, r4)     // Catch: java.lang.SecurityException -> L5a java.lang.Throwable -> Lc2
                    goto L79
                L5a:
                    r1 = move-exception
                    java.lang.String r3 = "VisualQueryDetectorSession"
                    java.lang.String r4 = "Ignoring #onQueryDetected due to a SecurityException"
                    android.util.Slog.w(r3, r4, r1)     // Catch: java.lang.Throwable -> Lc2
                    android.service.voice.IVisualQueryDetectionVoiceInteractionCallback r3 = r2     // Catch: android.os.RemoteException -> L71 java.lang.Throwable -> Lc2
                    android.service.voice.VisualQueryDetectionServiceFailure r4 = new android.service.voice.VisualQueryDetectionServiceFailure     // Catch: android.os.RemoteException -> L71 java.lang.Throwable -> Lc2
                    java.lang.String r5 = "Cannot stream visual only accessibility data without camera permission."
                    r4.<init>(r2, r5)     // Catch: android.os.RemoteException -> L71 java.lang.Throwable -> Lc2
                    r3.onVisualQueryDetectionServiceFailure(r4)     // Catch: android.os.RemoteException -> L71 java.lang.Throwable -> Lc2
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> Lc2
                    return
                L71:
                    r2 = move-exception
                    com.android.server.voiceinteraction.VisualQueryDetectorSession r3 = com.android.server.voiceinteraction.VisualQueryDetectorSession.this     // Catch: java.lang.Throwable -> Lc2
                    r3.notifyOnDetectorRemoteException()     // Catch: java.lang.Throwable -> Lc2
                    throw r2     // Catch: java.lang.Throwable -> Lc2
                L79:
                    java.lang.String r1 = r7.getPartialQuery()     // Catch: java.lang.Throwable -> Lc2
                    boolean r1 = r1.isEmpty()     // Catch: java.lang.Throwable -> Lc2
                    if (r1 != 0) goto Lae
                    com.android.server.voiceinteraction.VisualQueryDetectorSession r1 = com.android.server.voiceinteraction.VisualQueryDetectorSession.this     // Catch: java.lang.SecurityException -> L8f java.lang.Throwable -> Lc2
                    java.lang.String r3 = "android.permission.RECORD_AUDIO"
                    java.lang.String r4 = "Providing query detection result from VisualQueryDetectionService to VoiceInteractionService"
                    r5 = 27
                    r1.enforcePermissionsForVisualQueryDelivery(r3, r5, r4)     // Catch: java.lang.SecurityException -> L8f java.lang.Throwable -> Lc2
                    goto Lae
                L8f:
                    r1 = move-exception
                    java.lang.String r3 = "VisualQueryDetectorSession"
                    java.lang.String r4 = "Ignoring #onQueryDetected due to a SecurityException"
                    android.util.Slog.w(r3, r4, r1)     // Catch: java.lang.Throwable -> Lc2
                    android.service.voice.IVisualQueryDetectionVoiceInteractionCallback r3 = r2     // Catch: android.os.RemoteException -> La6 java.lang.Throwable -> Lc2
                    android.service.voice.VisualQueryDetectionServiceFailure r4 = new android.service.voice.VisualQueryDetectionServiceFailure     // Catch: android.os.RemoteException -> La6 java.lang.Throwable -> Lc2
                    java.lang.String r5 = "Cannot stream queries without audio permission."
                    r4.<init>(r2, r5)     // Catch: android.os.RemoteException -> La6 java.lang.Throwable -> Lc2
                    r3.onVisualQueryDetectionServiceFailure(r4)     // Catch: android.os.RemoteException -> La6 java.lang.Throwable -> Lc2
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> Lc2
                    return
                La6:
                    r2 = move-exception
                    com.android.server.voiceinteraction.VisualQueryDetectorSession r3 = com.android.server.voiceinteraction.VisualQueryDetectorSession.this     // Catch: java.lang.Throwable -> Lc2
                    r3.notifyOnDetectorRemoteException()     // Catch: java.lang.Throwable -> Lc2
                    throw r2     // Catch: java.lang.Throwable -> Lc2
                Lae:
                    com.android.server.voiceinteraction.VisualQueryDetectorSession r1 = com.android.server.voiceinteraction.VisualQueryDetectorSession.this     // Catch: java.lang.Throwable -> Lc2
                    r2 = 1
                    com.android.server.voiceinteraction.VisualQueryDetectorSession.m10199$$Nest$fputmQueryStreaming(r1, r2)     // Catch: java.lang.Throwable -> Lc2
                    android.service.voice.IVisualQueryDetectionVoiceInteractionCallback r1 = r2     // Catch: java.lang.Throwable -> Lc2
                    r1.onResultDetected(r7)     // Catch: java.lang.Throwable -> Lc2
                    java.lang.String r1 = "VisualQueryDetectorSession"
                    java.lang.String r2 = "Egressed from visual query detection process."
                    android.util.Slog.i(r1, r2)     // Catch: java.lang.Throwable -> Lc2
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> Lc2
                    return
                Lc2:
                    r1 = move-exception
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> Lc2
                    throw r1
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.voiceinteraction.VisualQueryDetectorSession.AnonymousClass1.onResultDetected(android.service.voice.VisualQueryDetectedResult):void");
            }

            public void onQueryFinished() throws android.os.RemoteException {
                android.util.Slog.v(com.android.server.voiceinteraction.VisualQueryDetectorSession.TAG, "BinderCallback#onQueryFinished");
                synchronized (com.android.server.voiceinteraction.VisualQueryDetectorSession.this.mLock) {
                    if (!com.android.server.voiceinteraction.VisualQueryDetectorSession.this.mQueryStreaming) {
                        android.util.Slog.v(com.android.server.voiceinteraction.VisualQueryDetectorSession.TAG, "Query streaming state signal FINISHED is block since there is no active query being streamed.");
                        callback.onVisualQueryDetectionServiceFailure(new android.service.voice.VisualQueryDetectionServiceFailure(4, "Cannot send FINISHED signal with no query streamed."));
                    } else {
                        callback.onQueryFinished();
                        com.android.server.voiceinteraction.VisualQueryDetectorSession.this.mQueryStreaming = false;
                    }
                }
            }

            public void onQueryRejected() throws android.os.RemoteException {
                android.util.Slog.v(com.android.server.voiceinteraction.VisualQueryDetectorSession.TAG, "BinderCallback#onQueryRejected");
                synchronized (com.android.server.voiceinteraction.VisualQueryDetectorSession.this.mLock) {
                    if (!com.android.server.voiceinteraction.VisualQueryDetectorSession.this.mQueryStreaming) {
                        android.util.Slog.v(com.android.server.voiceinteraction.VisualQueryDetectorSession.TAG, "Query streaming state signal REJECTED is block since there is no active query being streamed.");
                        callback.onVisualQueryDetectionServiceFailure(new android.service.voice.VisualQueryDetectionServiceFailure(4, "Cannot send REJECTED signal with no query streamed."));
                    } else {
                        callback.onQueryRejected();
                        com.android.server.voiceinteraction.VisualQueryDetectorSession.this.mQueryStreaming = false;
                    }
                }
            }

            private boolean checkDetectedResultDataLocked(android.service.voice.VisualQueryDetectedResult result) {
                return result.getAccessibilityDetectionData() == null || com.android.server.voiceinteraction.VisualQueryDetectorSession.this.mEnableAccessibilityDataEgress;
            }
        };
        return this.mRemoteDetectionService.run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.voiceinteraction.VisualQueryDetectorSession$$ExternalSyntheticLambda1
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.voice.ISandboxedDetectionService) obj).detectWithVisualSignals(stub);
            }
        });
    }

    boolean stopPerceivingLocked() {
        return this.mRemoteDetectionService.run(new com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession$$ExternalSyntheticLambda0());
    }

    @Override // com.android.server.voiceinteraction.DetectorSession
    void startListeningFromExternalSourceLocked(android.os.ParcelFileDescriptor audioStream, android.media.AudioFormat audioFormat, android.os.PersistableBundle options, android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback callback) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("HotwordDetectionService method should not be called from VisualQueryDetectorSession.");
    }

    void updateAccessibilityEgressStateLocked(boolean enable) {
        this.mEnableAccessibilityDataEgress = enable;
    }

    void enforcePermissionsForVisualQueryDelivery(final java.lang.String permission, final int op, final java.lang.String msg) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.voiceinteraction.VisualQueryDetectorSession$$ExternalSyntheticLambda0
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$enforcePermissionsForVisualQueryDelivery$1(permission, msg, op);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enforcePermissionsForVisualQueryDelivery$1(java.lang.String permission, java.lang.String msg, int op) throws java.lang.Exception {
        synchronized (this.mLock) {
            enforcePermissionForDataDelivery(this.mContext, this.mVoiceInteractorIdentity, permission, msg);
            this.mAppOpsManager.noteOpNoThrow(op, this.mVoiceInteractorIdentity.uid, this.mVoiceInteractorIdentity.packageName, this.mVoiceInteractorIdentity.attributionTag, msg);
        }
    }

    @Override // com.android.server.voiceinteraction.DetectorSession
    public void dumpLocked(java.lang.String prefix, java.io.PrintWriter pw) {
        super.dumpLocked(prefix, pw);
        pw.print(prefix);
    }
}
