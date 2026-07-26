package com.android.server.voiceinteraction;

/* JADX INFO: loaded from: classes3.dex */
final class HotwordDetectionConnection {
    static final boolean DEBUG = false;
    private static final int DETECTION_SERVICE_TYPE_HOTWORD = 1;
    private static final int DETECTION_SERVICE_TYPE_VISUAL_QUERY = 2;
    public static final long ENFORCE_HOTWORD_PHRASE_ID = 215066299;
    private static final int MAX_ISOLATED_PROCESS_NUMBER = 10;
    private static final long RESET_DEBUG_HOTWORD_LOGGING_TIMEOUT_MILLIS = 3600000;
    private static final java.lang.String TAG = "HotwordDetectionConnection";
    private android.os.IBinder mAudioFlinger;
    private final java.util.concurrent.ScheduledFuture<?> mCancellationTaskFuture;
    final android.content.Context mContext;
    private int mDetectorType;
    final android.content.ComponentName mHotwordDetectionComponentName;
    private final com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnectionFactory mHotwordDetectionServiceConnectionFactory;
    private com.android.internal.app.IHotwordRecognitionStatusCallback mHotwordRecognitionCallback;
    volatile android.service.voice.VoiceInteractionManagerInternal.HotwordDetectionServiceIdentity mIdentity;
    private java.time.Instant mLastRestartInstant;
    final java.lang.Object mLock;
    private com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.DetectorRemoteExceptionListener mRemoteExceptionListener;
    private com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnection mRemoteHotwordDetectionService;
    private com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnection mRemoteVisualQueryDetectionService;
    final int mUserId;
    final android.content.ComponentName mVisualQueryDetectionComponentName;
    private final com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnectionFactory mVisualQueryDetectionServiceConnectionFactory;
    final int mVoiceInteractionServiceUid;
    private final android.media.permission.Identity mVoiceInteractorIdentity;
    private static final java.lang.String KEY_RESTART_PERIOD_IN_SECONDS = "restart_period_in_seconds";
    private static final int RESTART_PERIOD_SECONDS = android.provider.DeviceConfig.getInt("voice_interaction", KEY_RESTART_PERIOD_IN_SECONDS, 0);
    private static final boolean SYSPROP_VISUAL_QUERY_SERVICE_ENABLED = android.os.SystemProperties.getBoolean("ro.hotword.visual_query_service_enabled", false);
    private final java.util.concurrent.ScheduledThreadPoolExecutor mScheduledExecutorService = new java.util.concurrent.ScheduledThreadPoolExecutor(1);
    private final android.os.IBinder.DeathRecipient mAudioServerDeathRecipient = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$$ExternalSyntheticLambda10
        @Override // android.os.IBinder.DeathRecipient
        public final void binderDied() {
            this.f$0.audioServerDied();
        }
    };
    private java.util.concurrent.ScheduledFuture<?> mDebugHotwordLoggingTimeoutFuture = null;
    private int mRestartCount = 0;
    private boolean mDebugHotwordLogging = false;
    private final android.util.SparseArray<com.android.server.voiceinteraction.DetectorSession> mDetectorSessions = new android.util.SparseArray<>();
    private final android.app.AppOpsManager.OnOpChangedListener mOnOpChangedListener = new android.app.AppOpsManager.OnOpChangedListener() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection.1
        @Override // android.app.AppOpsManager.OnOpChangedListener
        public void onOpChanged(java.lang.String op, java.lang.String packageName) {
            if (op.equals("android:receive_sandbox_trigger_audio")) {
                android.app.AppOpsManager appOpsManager = (android.app.AppOpsManager) com.android.server.voiceinteraction.HotwordDetectionConnection.this.mContext.getSystemService(android.app.AppOpsManager.class);
                synchronized (com.android.server.voiceinteraction.HotwordDetectionConnection.this.mLock) {
                    int checkOp = appOpsManager.unsafeCheckOpNoThrow("android:receive_sandbox_trigger_audio", com.android.server.voiceinteraction.HotwordDetectionConnection.this.mVoiceInteractorIdentity.uid, com.android.server.voiceinteraction.HotwordDetectionConnection.this.mVoiceInteractorIdentity.packageName);
                    if (checkOp == 2) {
                        android.util.Slog.i(com.android.server.voiceinteraction.HotwordDetectionConnection.TAG, "Shutdown hotword detection service on voice activation op disabled.");
                        com.android.server.voiceinteraction.HotwordDetectionConnection.this.safelyShutdownHotwordDetectionOnVoiceActivationDisabledLocked();
                    }
                }
            }
        }
    };
    private final int mReStartPeriodSeconds = android.provider.DeviceConfig.getInt("voice_interaction", KEY_RESTART_PERIOD_IN_SECONDS, 0);
    final com.android.server.voiceinteraction.HotwordDetectionConnection.AccessibilitySettingsListener mAccessibilitySettingsListener = new com.android.server.voiceinteraction.HotwordDetectionConnection.AccessibilitySettingsListener();

    /* JADX INFO: Access modifiers changed from: private */
    final class AccessibilitySettingsListener extends com.android.internal.app.IVoiceInteractionAccessibilitySettingsListener.Stub {
        private AccessibilitySettingsListener() {
        }

        public void onAccessibilityDetectionChanged(boolean enable) {
            synchronized (com.android.server.voiceinteraction.HotwordDetectionConnection.this.mLock) {
                com.android.server.voiceinteraction.VisualQueryDetectorSession session = com.android.server.voiceinteraction.HotwordDetectionConnection.this.getVisualQueryDetectorSessionLocked();
                if (session != null) {
                    session.updateAccessibilityEgressStateLocked(enable);
                }
            }
        }
    }

    HotwordDetectionConnection(java.lang.Object lock, android.content.Context context, int voiceInteractionServiceUid, android.media.permission.Identity voiceInteractorIdentity, android.content.ComponentName hotwordDetectionServiceName, android.content.ComponentName visualQueryDetectionServiceName, int userId, boolean bindInstantServiceAllowed, int detectorType, com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.DetectorRemoteExceptionListener listener) {
        this.mLock = lock;
        this.mContext = context;
        this.mVoiceInteractionServiceUid = voiceInteractionServiceUid;
        this.mVoiceInteractorIdentity = voiceInteractorIdentity;
        this.mHotwordDetectionComponentName = hotwordDetectionServiceName;
        this.mVisualQueryDetectionComponentName = visualQueryDetectionServiceName;
        this.mUserId = userId;
        this.mDetectorType = detectorType;
        this.mRemoteExceptionListener = listener;
        android.content.Intent hotwordDetectionServiceIntent = new android.content.Intent("android.service.voice.HotwordDetectionService");
        hotwordDetectionServiceIntent.setComponent(this.mHotwordDetectionComponentName);
        android.content.Intent visualQueryDetectionServiceIntent = new android.content.Intent("android.service.voice.VisualQueryDetectionService");
        visualQueryDetectionServiceIntent.setComponent(this.mVisualQueryDetectionComponentName);
        initAudioFlinger();
        this.mHotwordDetectionServiceConnectionFactory = new com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnectionFactory(hotwordDetectionServiceIntent, bindInstantServiceAllowed, 1);
        this.mVisualQueryDetectionServiceConnectionFactory = new com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnectionFactory(visualQueryDetectionServiceIntent, bindInstantServiceAllowed, 2);
        this.mLastRestartInstant = java.time.Instant.now();
        android.app.AppOpsManager appOpsManager = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        appOpsManager.startWatchingMode(136, this.mVoiceInteractorIdentity.packageName, this.mOnOpChangedListener);
        if (this.mReStartPeriodSeconds <= 0) {
            this.mCancellationTaskFuture = null;
        } else {
            this.mScheduledExecutorService.setRemoveOnCancelPolicy(true);
            this.mCancellationTaskFuture = this.mScheduledExecutorService.scheduleAtFixedRate(new java.lang.Runnable() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$new$0();
                }
            }, this.mReStartPeriodSeconds, this.mReStartPeriodSeconds, java.util.concurrent.TimeUnit.SECONDS);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        android.util.Slog.v(TAG, "Time to restart the process, TTL has passed");
        synchronized (this.mLock) {
            restartProcessLocked();
            if (this.mDetectorType != 3) {
                com.android.server.voiceinteraction.HotwordMetricsLogger.writeServiceRestartEvent(this.mDetectorType, 2, this.mVoiceInteractionServiceUid);
            }
        }
    }

    private void initAudioFlinger() {
        android.os.IBinder audioFlinger = android.os.ServiceManager.waitForService("media.audio_flinger");
        if (audioFlinger == null) {
            setAudioFlinger(null);
            throw new java.lang.IllegalStateException("Service media.audio_flinger wasn't found.");
        }
        try {
            audioFlinger.linkToDeath(this.mAudioServerDeathRecipient, 0);
            setAudioFlinger(audioFlinger);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Audio server died before we registered a DeathRecipient; retrying init.", e);
            initAudioFlinger();
        }
    }

    private void setAudioFlinger(android.os.IBinder audioFlinger) {
        synchronized (this.mLock) {
            this.mAudioFlinger = audioFlinger;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void audioServerDied() {
        android.util.Slog.w(TAG, "Audio server died; restarting the HotwordDetectionService.");
        initAudioFlinger();
        synchronized (this.mLock) {
            restartProcessLocked();
            if (this.mDetectorType != 3) {
                com.android.server.voiceinteraction.HotwordMetricsLogger.writeServiceRestartEvent(this.mDetectorType, 1, this.mVoiceInteractionServiceUid);
            }
        }
    }

    void cancelLocked() {
        android.util.Slog.v(TAG, "cancelLocked");
        clearDebugHotwordLoggingTimeoutLocked();
        this.mRemoteExceptionListener = null;
        runForEachDetectorSessionLocked(new java.util.function.Consumer() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.voiceinteraction.DetectorSession) obj).destroyLocked();
            }
        });
        this.mDetectorSessions.clear();
        this.mDebugHotwordLogging = false;
        unbindVisualQueryDetectionService();
        unbindHotwordDetectionService();
        if (this.mCancellationTaskFuture != null) {
            this.mCancellationTaskFuture.cancel(true);
        }
        if (this.mAudioFlinger != null) {
            this.mAudioFlinger.unlinkToDeath(this.mAudioServerDeathRecipient, 0);
            this.mAudioFlinger = null;
        }
        android.app.AppOpsManager appOpsManager = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        appOpsManager.stopWatchingMode(this.mOnOpChangedListener);
    }

    private void unbindVisualQueryDetectionService() {
        if (this.mRemoteVisualQueryDetectionService != null) {
            this.mRemoteVisualQueryDetectionService.unbind();
            this.mRemoteVisualQueryDetectionService = null;
        }
        resetDetectionProcessIdentityIfEmptyLocked();
    }

    private void unbindHotwordDetectionService() {
        if (this.mRemoteHotwordDetectionService != null) {
            this.mRemoteHotwordDetectionService.unbind();
            this.mRemoteHotwordDetectionService = null;
        }
        resetDetectionProcessIdentityIfEmptyLocked();
    }

    private void resetDetectionProcessIdentityIfEmptyLocked() {
        if (this.mRemoteHotwordDetectionService == null && this.mRemoteVisualQueryDetectionService == null) {
            ((com.android.server.pm.permission.PermissionManagerServiceInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionManagerServiceInternal.class)).setHotwordDetectionServiceProvider(null);
            if (this.mIdentity != null) {
                removeServiceUidForAudioPolicy(this.mIdentity.getIsolatedUid());
            }
            this.mIdentity = null;
        }
    }

    void updateStateLocked(android.os.PersistableBundle options, android.os.SharedMemory sharedMemory, android.os.IBinder token) {
        com.android.server.voiceinteraction.DetectorSession session = getDetectorSessionByTokenLocked(token);
        if (session == null) {
            android.util.Slog.v(TAG, "Not found the detector by token");
        } else {
            session.updateStateLocked(options, sharedMemory, this.mLastRestartInstant);
        }
    }

    void startListeningFromMicLocked(android.media.AudioFormat audioFormat, android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback callback) {
        com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession session = getSoftwareTrustedHotwordDetectorSessionLocked();
        if (session == null) {
            return;
        }
        session.startListeningFromMicLocked(audioFormat, callback);
    }

    public void setVisualQueryDetectionAttentionListenerLocked(com.android.internal.app.IVisualQueryDetectionAttentionListener listener) {
        com.android.server.voiceinteraction.VisualQueryDetectorSession session = getVisualQueryDetectorSessionLocked();
        if (session == null) {
            return;
        }
        session.setVisualQueryDetectionAttentionListenerLocked(listener);
    }

    boolean startPerceivingLocked(android.service.voice.IVisualQueryDetectionVoiceInteractionCallback callback) {
        com.android.server.voiceinteraction.VisualQueryDetectorSession session = getVisualQueryDetectorSessionLocked();
        if (session == null) {
            return false;
        }
        return session.startPerceivingLocked(callback);
    }

    boolean stopPerceivingLocked() {
        com.android.server.voiceinteraction.VisualQueryDetectorSession session = getVisualQueryDetectorSessionLocked();
        if (session == null) {
            return false;
        }
        return session.stopPerceivingLocked();
    }

    public void startListeningFromExternalSourceLocked(android.os.ParcelFileDescriptor audioStream, android.media.AudioFormat audioFormat, android.os.PersistableBundle options, android.os.IBinder token, android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback callback) {
        com.android.server.voiceinteraction.DetectorSession session = getDetectorSessionByTokenLocked(token);
        if (session == null) {
            android.util.Slog.v(TAG, "Not found the detector by token");
        } else {
            session.startListeningFromExternalSourceLocked(audioStream, audioFormat, options, callback);
        }
    }

    public void startListeningFromWearableLocked(android.os.ParcelFileDescriptor audioStream, android.media.AudioFormat audioFormat, android.os.PersistableBundle options, android.service.voice.VoiceInteractionManagerInternal.WearableHotwordDetectionCallback callback) {
        com.android.server.voiceinteraction.DetectorSession trustedSession = getDspTrustedHotwordDetectorSessionLocked();
        if (trustedSession == null) {
            callback.onError("Unable to start listening from wearable because the trusted hotword detection session is not available.");
        } else {
            trustedSession.startListeningFromWearableLocked(audioStream, audioFormat, options, callback);
        }
    }

    void stopListeningFromMicLocked() {
        com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession session = getSoftwareTrustedHotwordDetectorSessionLocked();
        if (session == null) {
            return;
        }
        session.stopListeningFromMicLocked();
    }

    void triggerHardwareRecognitionEventForTestLocked(android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent event, com.android.internal.app.IHotwordRecognitionStatusCallback callback) {
        detectFromDspSource(event, callback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void detectFromDspSource(android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent recognitionEvent, com.android.internal.app.IHotwordRecognitionStatusCallback externalCallback) {
        synchronized (this.mLock) {
            com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession session = getDspTrustedHotwordDetectorSessionLocked();
            if (session != null && session.isSameCallback(externalCallback)) {
                session.detectFromDspSourceLocked(recognitionEvent, externalCallback);
                return;
            }
            android.util.Slog.v(TAG, "Not found the Dsp detector by callback");
        }
    }

    void forceRestart() {
        android.util.Slog.v(TAG, "Requested to restart the service internally. Performing the restart");
        synchronized (this.mLock) {
            restartProcessLocked();
        }
    }

    void setDebugHotwordLoggingLocked(final boolean logging) {
        android.util.Slog.v(TAG, "setDebugHotwordLoggingLocked: " + logging);
        clearDebugHotwordLoggingTimeoutLocked();
        this.mDebugHotwordLogging = logging;
        runForEachDetectorSessionLocked(new java.util.function.Consumer() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.voiceinteraction.DetectorSession) obj).setDebugHotwordLoggingLocked(logging);
            }
        });
        if (logging) {
            this.mDebugHotwordLoggingTimeoutFuture = this.mScheduledExecutorService.schedule(new java.lang.Runnable() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setDebugHotwordLoggingLocked$4();
                }
            }, 3600000L, java.util.concurrent.TimeUnit.MILLISECONDS);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setDebugHotwordLoggingLocked$4() {
        android.util.Slog.v(TAG, "Timeout to reset mDebugHotwordLogging to false");
        synchronized (this.mLock) {
            this.mDebugHotwordLogging = false;
            runForEachDetectorSessionLocked(new java.util.function.Consumer() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.voiceinteraction.DetectorSession) obj).setDebugHotwordLoggingLocked(false);
                }
            });
        }
    }

    void setDetectorType(int detectorType) {
        this.mDetectorType = detectorType;
    }

    private void clearDebugHotwordLoggingTimeoutLocked() {
        if (this.mDebugHotwordLoggingTimeoutFuture != null) {
            this.mDebugHotwordLoggingTimeoutFuture.cancel(true);
            this.mDebugHotwordLoggingTimeoutFuture = null;
        }
    }

    private void restartProcessLocked() {
        android.util.Slog.v(TAG, "Restarting hotword detection process");
        com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnection oldHotwordConnection = this.mRemoteHotwordDetectionService;
        com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnection oldVisualQueryDetectionConnection = this.mRemoteVisualQueryDetectionService;
        android.service.voice.VoiceInteractionManagerInternal.HotwordDetectionServiceIdentity previousIdentity = this.mIdentity;
        this.mLastRestartInstant = java.time.Instant.now();
        this.mRestartCount++;
        if (oldHotwordConnection != null) {
            this.mRemoteHotwordDetectionService = this.mHotwordDetectionServiceConnectionFactory.createLocked();
        }
        if (oldVisualQueryDetectionConnection != null) {
            this.mRemoteVisualQueryDetectionService = this.mVisualQueryDetectionServiceConnectionFactory.createLocked();
        }
        android.util.Slog.v(TAG, "Started the new process, dispatching processRestarted to detector");
        runForEachDetectorSessionLocked(new java.util.function.Consumer() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$restartProcessLocked$5((com.android.server.voiceinteraction.DetectorSession) obj);
            }
        });
        if (oldHotwordConnection != null) {
            oldHotwordConnection.ignoreConnectionStatusEvents();
            oldHotwordConnection.unbind();
        }
        if (oldVisualQueryDetectionConnection != null) {
            oldVisualQueryDetectionConnection.ignoreConnectionStatusEvents();
            oldVisualQueryDetectionConnection.unbind();
        }
        if (previousIdentity != null) {
            removeServiceUidForAudioPolicy(previousIdentity.getIsolatedUid());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$restartProcessLocked$5(com.android.server.voiceinteraction.DetectorSession session) {
        com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnection newRemoteService = session instanceof com.android.server.voiceinteraction.VisualQueryDetectorSession ? this.mRemoteVisualQueryDetectionService : this.mRemoteHotwordDetectionService;
        session.updateRemoteSandboxedDetectionServiceLocked(newRemoteService);
        session.informRestartProcessLocked();
    }

    void safelyShutdownHotwordDetectionOnVoiceActivationDisabledLocked() {
        android.util.Slog.v(TAG, "safelyShutdownHotwordDetectionOnVoiceActivationDisabled");
        try {
            clearDebugHotwordLoggingTimeoutLocked();
            this.mRemoteExceptionListener = null;
            runForEachDetectorSessionLocked(new java.util.function.Consumer() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$$ExternalSyntheticLambda15
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.voiceinteraction.HotwordDetectionConnection.lambda$safelyShutdownHotwordDetectionOnVoiceActivationDisabledLocked$6((com.android.server.voiceinteraction.DetectorSession) obj);
                }
            });
            this.mDetectorSessions.delete(1);
            this.mDetectorSessions.delete(2);
            this.mDebugHotwordLogging = false;
            unbindHotwordDetectionService();
            if (this.mCancellationTaskFuture != null) {
                this.mCancellationTaskFuture.cancel(true);
            }
            if (this.mAudioFlinger != null) {
                this.mAudioFlinger.unlinkToDeath(this.mAudioServerDeathRecipient, 0);
                this.mAudioFlinger = null;
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Swallowing error while shutting down hotword detection.Error message: " + e.getMessage());
        }
    }

    static /* synthetic */ void lambda$safelyShutdownHotwordDetectionOnVoiceActivationDisabledLocked$6(com.android.server.voiceinteraction.DetectorSession session) {
        if (!(session instanceof com.android.server.voiceinteraction.VisualQueryDetectorSession)) {
            session.reportErrorLocked(new android.service.voice.HotwordDetectionServiceFailure(10, "Shutdown hotword detection service on voice activation op disabled!"));
            session.destroyLocked();
        }
    }

    static final class SoundTriggerCallback extends android.hardware.soundtrigger.IRecognitionStatusCallback.Stub {
        private final android.content.Context mContext;
        private final com.android.internal.app.IHotwordRecognitionStatusCallback mExternalCallback;
        private final com.android.server.voiceinteraction.HotwordDetectionConnection mHotwordDetectionConnection;
        private final android.media.permission.Identity mVoiceInteractorIdentity;

        SoundTriggerCallback(android.content.Context context, com.android.internal.app.IHotwordRecognitionStatusCallback callback, com.android.server.voiceinteraction.HotwordDetectionConnection connection, android.media.permission.Identity voiceInteractorIdentity) {
            this.mContext = context;
            this.mHotwordDetectionConnection = connection;
            this.mExternalCallback = callback;
            this.mVoiceInteractorIdentity = voiceInteractorIdentity;
        }

        public void onKeyphraseDetected(android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent recognitionEvent) throws android.os.RemoteException {
            boolean useHotwordDetectionService = this.mHotwordDetectionConnection != null;
            if (useHotwordDetectionService) {
                com.android.server.voiceinteraction.HotwordMetricsLogger.writeKeyphraseTriggerEvent(1, 0, this.mVoiceInteractorIdentity.uid);
                this.mHotwordDetectionConnection.detectFromDspSource(recognitionEvent, this.mExternalCallback);
                return;
            }
            int result = ((android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class)).noteOpNoThrow(102, this.mVoiceInteractorIdentity.uid, this.mVoiceInteractorIdentity.packageName, this.mVoiceInteractorIdentity.attributionTag, "Non-HDS keyphrase recognition to VoiceInteractionService");
            if (result != 0) {
                android.util.Slog.w(com.android.server.voiceinteraction.HotwordDetectionConnection.TAG, "onKeyphraseDetected suppressed, permission check returned: " + result);
                this.mExternalCallback.onRecognitionPaused();
            } else {
                com.android.server.voiceinteraction.HotwordMetricsLogger.writeKeyphraseTriggerEvent(0, 0, this.mVoiceInteractorIdentity.uid);
                this.mExternalCallback.onKeyphraseDetected(recognitionEvent, (android.service.voice.HotwordDetectedResult) null);
            }
        }

        public void onGenericSoundTriggerDetected(android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent recognitionEvent) throws android.os.RemoteException {
            this.mExternalCallback.onGenericSoundTriggerDetected(recognitionEvent);
        }

        public void onPreempted() throws android.os.RemoteException {
            this.mExternalCallback.onSoundTriggerFailure(new android.service.voice.SoundTriggerFailure(3, "Unexpected startRecognition on already started ST session"));
        }

        public void onModuleDied() throws android.os.RemoteException {
            this.mExternalCallback.onSoundTriggerFailure(new android.service.voice.SoundTriggerFailure(1, "STHAL died"));
        }

        public void onResumeFailed(int status) throws android.os.RemoteException {
            this.mExternalCallback.onSoundTriggerFailure(new android.service.voice.SoundTriggerFailure(2, "STService recognition resume failed with: " + status));
        }

        public void onPauseFailed(int status) throws android.os.RemoteException {
            this.mExternalCallback.onSoundTriggerFailure(new android.service.voice.SoundTriggerFailure(2, "STService recognition pause failed with: " + status));
        }

        public void onRecognitionPaused() throws android.os.RemoteException {
            this.mExternalCallback.onRecognitionPaused();
        }

        public void onRecognitionResumed() throws android.os.RemoteException {
            this.mExternalCallback.onRecognitionResumed();
        }
    }

    public void dump(final java.lang.String prefix, final java.io.PrintWriter pw) {
        synchronized (this.mLock) {
            pw.print(prefix);
            pw.print("mReStartPeriodSeconds=");
            pw.println(this.mReStartPeriodSeconds);
            pw.print(prefix);
            pw.print("bound for HotwordDetectionService=");
            boolean z = true;
            pw.println(this.mRemoteHotwordDetectionService != null && this.mRemoteHotwordDetectionService.isBound());
            pw.print(prefix);
            pw.print("bound for VisualQueryDetectionService=");
            if (this.mRemoteVisualQueryDetectionService == null || this.mRemoteHotwordDetectionService == null || !this.mRemoteHotwordDetectionService.isBound()) {
                z = false;
            }
            pw.println(z);
            pw.print(prefix);
            pw.print("mRestartCount=");
            pw.println(this.mRestartCount);
            pw.print(prefix);
            pw.print("mLastRestartInstant=");
            pw.println(this.mLastRestartInstant);
            pw.print(prefix);
            pw.println("DetectorSession(s):");
            pw.print(prefix);
            pw.print("Num of DetectorSession(s)=");
            pw.println(this.mDetectorSessions.size());
            runForEachDetectorSessionLocked(new java.util.function.Consumer() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$$ExternalSyntheticLambda12
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.voiceinteraction.DetectorSession) obj).dumpLocked(prefix, pw);
                }
            });
        }
    }

    private class ServiceConnectionFactory {
        private final int mBindingFlags;
        private final int mDetectionServiceType;
        private final android.content.Intent mIntent;

        ServiceConnectionFactory(android.content.Intent intent, boolean bindInstantServiceAllowed, int detectionServiceType) {
            this.mIntent = intent;
            this.mDetectionServiceType = detectionServiceType;
            int flags = bindInstantServiceAllowed ? 4194304 : 0;
            if (com.android.server.voiceinteraction.HotwordDetectionConnection.SYSPROP_VISUAL_QUERY_SERVICE_ENABLED && com.android.server.voiceinteraction.HotwordDetectionConnection.this.mVisualQueryDetectionComponentName != null && com.android.server.voiceinteraction.HotwordDetectionConnection.this.mHotwordDetectionComponentName != null) {
                flags |= 8192;
            }
            this.mBindingFlags = flags;
        }

        com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnection createLocked() {
            com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnection connection = com.android.server.voiceinteraction.HotwordDetectionConnection.this.new ServiceConnection(com.android.server.voiceinteraction.HotwordDetectionConnection.this.mContext, this.mIntent, this.mBindingFlags, com.android.server.voiceinteraction.HotwordDetectionConnection.this.mUserId, new java.util.function.Function() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$ServiceConnectionFactory$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return android.service.voice.ISandboxedDetectionService.Stub.asInterface((android.os.IBinder) obj);
                }
            }, com.android.server.voiceinteraction.HotwordDetectionConnection.this.mRestartCount % 10, this.mDetectionServiceType);
            connection.connect();
            com.android.server.voiceinteraction.HotwordDetectionConnection.updateAudioFlinger(connection, com.android.server.voiceinteraction.HotwordDetectionConnection.this.mAudioFlinger);
            com.android.server.voiceinteraction.HotwordDetectionConnection.updateContentCaptureManager(connection);
            com.android.server.voiceinteraction.HotwordDetectionConnection.updateSpeechService(connection);
            com.android.server.voiceinteraction.HotwordDetectionConnection.this.updateServiceIdentity(connection);
            com.android.server.voiceinteraction.HotwordDetectionConnection.this.updateStorageService(connection);
            return connection;
        }
    }

    class ServiceConnection extends com.android.internal.infra.ServiceConnector.Impl<android.service.voice.ISandboxedDetectionService> {
        private final int mBindingFlags;
        private final int mDetectionServiceType;
        private final int mInstanceNumber;
        private final android.content.Intent mIntent;
        private boolean mIsBound;
        private boolean mIsLoggedFirstConnect;
        private final java.lang.Object mLock;
        private boolean mRespectServiceConnectionStatusChanged;

        ServiceConnection(android.content.Context context, android.content.Intent serviceIntent, int bindingFlags, int userId, java.util.function.Function<android.os.IBinder, android.service.voice.ISandboxedDetectionService> binderAsInterface, int instanceNumber, int detectionServiceType) {
            super(context, serviceIntent, bindingFlags, userId, binderAsInterface);
            this.mLock = new java.lang.Object();
            this.mRespectServiceConnectionStatusChanged = true;
            this.mIsBound = false;
            this.mIsLoggedFirstConnect = false;
            this.mIntent = serviceIntent;
            this.mBindingFlags = bindingFlags;
            this.mInstanceNumber = instanceNumber;
            this.mDetectionServiceType = detectionServiceType;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onServiceConnectionStatusChanged(android.service.voice.ISandboxedDetectionService service, boolean connected) {
            synchronized (this.mLock) {
                if (!this.mRespectServiceConnectionStatusChanged) {
                    android.util.Slog.v(com.android.server.voiceinteraction.HotwordDetectionConnection.TAG, "Ignored onServiceConnectionStatusChanged event");
                    return;
                }
                this.mIsBound = connected;
                if (!connected) {
                    if (this.mDetectionServiceType != 2) {
                        com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(com.android.server.voiceinteraction.HotwordDetectionConnection.this.mDetectorType, 7, com.android.server.voiceinteraction.HotwordDetectionConnection.this.mVoiceInteractionServiceUid);
                    }
                } else if (!this.mIsLoggedFirstConnect) {
                    this.mIsLoggedFirstConnect = true;
                    if (this.mDetectionServiceType != 2) {
                        com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(com.android.server.voiceinteraction.HotwordDetectionConnection.this.mDetectorType, 2, com.android.server.voiceinteraction.HotwordDetectionConnection.this.mVoiceInteractionServiceUid);
                    }
                }
            }
        }

        protected long getAutoDisconnectTimeoutMs() {
            return -1L;
        }

        public void binderDied() {
            super.binderDied();
            android.util.Slog.w(com.android.server.voiceinteraction.HotwordDetectionConnection.TAG, "binderDied mDetectionServiceType = " + this.mDetectionServiceType);
            synchronized (this.mLock) {
                if (!this.mRespectServiceConnectionStatusChanged) {
                    android.util.Slog.v(com.android.server.voiceinteraction.HotwordDetectionConnection.TAG, "Ignored #binderDied event");
                    return;
                }
                synchronized (com.android.server.voiceinteraction.HotwordDetectionConnection.this.mLock) {
                    com.android.server.voiceinteraction.HotwordDetectionConnection.this.runForEachDetectorSessionLocked(new java.util.function.Consumer() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$ServiceConnection$$ExternalSyntheticLambda1
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.reportBinderDiedLocked((com.android.server.voiceinteraction.DetectorSession) obj);
                        }
                    });
                }
                if (this.mDetectionServiceType != 2) {
                    com.android.server.voiceinteraction.HotwordMetricsLogger.writeKeyphraseTriggerEvent(com.android.server.voiceinteraction.HotwordDetectionConnection.this.mDetectorType, 4, com.android.server.voiceinteraction.HotwordDetectionConnection.this.mVoiceInteractionServiceUid);
                }
            }
        }

        protected boolean bindService(android.content.ServiceConnection serviceConnection) {
            try {
                if (this.mDetectionServiceType != 2) {
                    com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(com.android.server.voiceinteraction.HotwordDetectionConnection.this.mDetectorType, 1, com.android.server.voiceinteraction.HotwordDetectionConnection.this.mVoiceInteractionServiceUid);
                }
                boolean bindResult = this.mContext.bindIsolatedService(this.mIntent, 67108865 | this.mBindingFlags, "hotword_detector_" + this.mInstanceNumber, this.mExecutor, serviceConnection);
                if (!bindResult) {
                    android.util.Slog.w(com.android.server.voiceinteraction.HotwordDetectionConnection.TAG, "bindService failure mDetectionServiceType = " + this.mDetectionServiceType);
                    synchronized (com.android.server.voiceinteraction.HotwordDetectionConnection.this.mLock) {
                        com.android.server.voiceinteraction.HotwordDetectionConnection.this.runForEachDetectorSessionLocked(new java.util.function.Consumer() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$ServiceConnection$$ExternalSyntheticLambda0
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                this.f$0.reportBindServiceFailureLocked((com.android.server.voiceinteraction.DetectorSession) obj);
                            }
                        });
                    }
                    if (this.mDetectionServiceType != 2) {
                        com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(com.android.server.voiceinteraction.HotwordDetectionConnection.this.mDetectorType, 3, com.android.server.voiceinteraction.HotwordDetectionConnection.this.mVoiceInteractionServiceUid);
                    }
                }
                return bindResult;
            } catch (java.lang.IllegalArgumentException e) {
                if (this.mDetectionServiceType != 2) {
                    com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorEvent(com.android.server.voiceinteraction.HotwordDetectionConnection.this.mDetectorType, 3, com.android.server.voiceinteraction.HotwordDetectionConnection.this.mVoiceInteractionServiceUid);
                }
                android.util.Slog.wtf(com.android.server.voiceinteraction.HotwordDetectionConnection.TAG, "Can't bind to the hotword detection service!", e);
                return false;
            }
        }

        boolean isBound() {
            boolean z;
            synchronized (this.mLock) {
                z = this.mIsBound;
            }
            return z;
        }

        void ignoreConnectionStatusEvents() {
            synchronized (this.mLock) {
                this.mRespectServiceConnectionStatusChanged = false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void reportBinderDiedLocked(com.android.server.voiceinteraction.DetectorSession detectorSession) {
            if (this.mDetectionServiceType == 1 && ((detectorSession instanceof com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession) || (detectorSession instanceof com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession))) {
                detectorSession.reportErrorLocked(new android.service.voice.HotwordDetectionServiceFailure(2, "Detection service is dead."));
            } else if (this.mDetectionServiceType == 2 && (detectorSession instanceof com.android.server.voiceinteraction.VisualQueryDetectorSession)) {
                detectorSession.reportErrorLocked(new android.service.voice.VisualQueryDetectionServiceFailure(2, "Detection service is dead."));
            } else {
                detectorSession.reportErrorLocked("Detection service is dead with unknown detection service type.");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void reportBindServiceFailureLocked(com.android.server.voiceinteraction.DetectorSession detectorSession) {
            if (this.mDetectionServiceType == 1 && ((detectorSession instanceof com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession) || (detectorSession instanceof com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession))) {
                detectorSession.reportErrorLocked(new android.service.voice.HotwordDetectionServiceFailure(1, "Bind detection service failure."));
            } else if (this.mDetectionServiceType == 2 && (detectorSession instanceof com.android.server.voiceinteraction.VisualQueryDetectorSession)) {
                detectorSession.reportErrorLocked(new android.service.voice.VisualQueryDetectionServiceFailure(1, "Bind detection service failure."));
            } else {
                detectorSession.reportErrorLocked("Bind detection service failure with unknown detection service type.");
            }
        }
    }

    void createDetectorLocked(android.os.PersistableBundle options, android.os.SharedMemory sharedMemory, android.os.IBinder token, com.android.internal.app.IHotwordRecognitionStatusCallback callback, int detectorType) {
        com.android.server.voiceinteraction.DetectorSession session;
        com.android.server.voiceinteraction.DetectorSession removeSession = this.mDetectorSessions.get(detectorType);
        if (removeSession != null) {
            removeSession.destroyLocked();
            this.mDetectorSessions.remove(detectorType);
        }
        if (detectorType == 1) {
            if (this.mRemoteHotwordDetectionService == null) {
                this.mRemoteHotwordDetectionService = this.mHotwordDetectionServiceConnectionFactory.createLocked();
            }
            session = new com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession(this.mRemoteHotwordDetectionService, this.mLock, this.mContext, token, callback, this.mVoiceInteractionServiceUid, this.mVoiceInteractorIdentity, this.mScheduledExecutorService, this.mDebugHotwordLogging, this.mRemoteExceptionListener, this.mUserId);
        } else if (detectorType == 3) {
            if (this.mRemoteVisualQueryDetectionService == null) {
                this.mRemoteVisualQueryDetectionService = this.mVisualQueryDetectionServiceConnectionFactory.createLocked();
            }
            session = new com.android.server.voiceinteraction.VisualQueryDetectorSession(this.mRemoteVisualQueryDetectionService, this.mLock, this.mContext, token, callback, this.mVoiceInteractionServiceUid, this.mVoiceInteractorIdentity, this.mScheduledExecutorService, this.mDebugHotwordLogging, this.mRemoteExceptionListener, this.mUserId);
        } else {
            if (this.mRemoteHotwordDetectionService == null) {
                this.mRemoteHotwordDetectionService = this.mHotwordDetectionServiceConnectionFactory.createLocked();
            }
            session = new com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession(this.mRemoteHotwordDetectionService, this.mLock, this.mContext, token, callback, this.mVoiceInteractionServiceUid, this.mVoiceInteractorIdentity, this.mScheduledExecutorService, this.mDebugHotwordLogging, this.mRemoteExceptionListener, this.mUserId);
        }
        this.mHotwordRecognitionCallback = callback;
        this.mDetectorSessions.put(detectorType, session);
        session.initialize(options, sharedMemory);
    }

    void destroyDetectorLocked(android.os.IBinder token) {
        com.android.server.voiceinteraction.DetectorSession session = getDetectorSessionByTokenLocked(token);
        if (session == null) {
            return;
        }
        session.destroyLocked();
        int index = this.mDetectorSessions.indexOfValue(session);
        if (index >= 0) {
            boolean z = true;
            if (index > this.mDetectorSessions.size() - 1) {
                return;
            }
            this.mDetectorSessions.removeAt(index);
            if (session instanceof com.android.server.voiceinteraction.VisualQueryDetectorSession) {
                unbindVisualQueryDetectionService();
            }
            if (this.mDetectorSessions.size() != 0 && (this.mDetectorSessions.size() != 1 || !(this.mDetectorSessions.get(0) instanceof com.android.server.voiceinteraction.VisualQueryDetectorSession))) {
                z = false;
            }
            boolean allHotwordDetectionServiceSessionsRemoved = z;
            if (allHotwordDetectionServiceSessionsRemoved) {
                unbindHotwordDetectionService();
            }
        }
    }

    private com.android.server.voiceinteraction.DetectorSession getDetectorSessionByTokenLocked(android.os.IBinder token) {
        if (token == null) {
            return null;
        }
        for (int i = 0; i < this.mDetectorSessions.size(); i++) {
            com.android.server.voiceinteraction.DetectorSession session = this.mDetectorSessions.valueAt(i);
            if (!session.isDestroyed() && session.isSameToken(token)) {
                return session;
            }
        }
        return null;
    }

    private com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession getDspTrustedHotwordDetectorSessionLocked() {
        com.android.server.voiceinteraction.DetectorSession session = this.mDetectorSessions.get(1);
        if (session == null || session.isDestroyed()) {
            android.util.Slog.v(TAG, "Not found the Dsp detector");
            return null;
        }
        return (com.android.server.voiceinteraction.DspTrustedHotwordDetectorSession) session;
    }

    private com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession getSoftwareTrustedHotwordDetectorSessionLocked() {
        com.android.server.voiceinteraction.DetectorSession session = this.mDetectorSessions.get(2);
        if (session == null || session.isDestroyed()) {
            android.util.Slog.v(TAG, "Not found the software detector");
            return null;
        }
        return (com.android.server.voiceinteraction.SoftwareTrustedHotwordDetectorSession) session;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.voiceinteraction.VisualQueryDetectorSession getVisualQueryDetectorSessionLocked() {
        com.android.server.voiceinteraction.DetectorSession session = this.mDetectorSessions.get(3);
        if (session == null || session.isDestroyed()) {
            android.util.Slog.v(TAG, "Not found the visual query detector");
            return null;
        }
        return (com.android.server.voiceinteraction.VisualQueryDetectorSession) session;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runForEachDetectorSessionLocked(java.util.function.Consumer<com.android.server.voiceinteraction.DetectorSession> action) {
        for (int i = 0; i < this.mDetectorSessions.size(); i++) {
            com.android.server.voiceinteraction.DetectorSession session = this.mDetectorSessions.valueAt(i);
            action.accept(session);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateAudioFlinger(com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnection connection, final android.os.IBinder audioFlinger) {
        connection.run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$$ExternalSyntheticLambda9
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.voice.ISandboxedDetectionService) obj).updateAudioFlinger(audioFlinger);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateContentCaptureManager(com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnection connection) {
        android.os.IBinder b = android.os.ServiceManager.getService("content_capture");
        final android.view.contentcapture.IContentCaptureManager binderService = android.view.contentcapture.IContentCaptureManager.Stub.asInterface(b);
        connection.run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$$ExternalSyntheticLambda2
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.voice.ISandboxedDetectionService) obj).updateContentCaptureManager(binderService, new android.content.ContentCaptureOptions((android.util.ArraySet) null));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateSpeechService(com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnection connection) {
        android.os.IBinder b = android.os.ServiceManager.getService("speech_recognition");
        final android.speech.IRecognitionServiceManager binderService = android.speech.IRecognitionServiceManager.Stub.asInterface(b);
        connection.run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$$ExternalSyntheticLambda13
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.voice.ISandboxedDetectionService) obj).updateRecognitionServiceManager(binderService);
            }
        });
    }

    /* JADX INFO: renamed from: com.android.server.voiceinteraction.HotwordDetectionConnection$2, reason: invalid class name */
    class AnonymousClass2 extends android.os.IRemoteCallback.Stub {
        AnonymousClass2() {
        }

        public void sendResult(android.os.Bundle bundle) throws android.os.RemoteException {
            final int uid = android.os.Binder.getCallingUid();
            ((com.android.server.pm.permission.PermissionManagerServiceInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionManagerServiceInternal.class)).setHotwordDetectionServiceProvider(new com.android.server.pm.permission.PermissionManagerServiceInternal.HotwordDetectionServiceProvider() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$2$$ExternalSyntheticLambda0
                @Override // com.android.server.pm.permission.PermissionManagerServiceInternal.HotwordDetectionServiceProvider
                public final int getUid() {
                    return com.android.server.voiceinteraction.HotwordDetectionConnection.AnonymousClass2.lambda$sendResult$0(uid);
                }
            });
            com.android.server.voiceinteraction.HotwordDetectionConnection.this.mIdentity = new android.service.voice.VoiceInteractionManagerInternal.HotwordDetectionServiceIdentity(uid, com.android.server.voiceinteraction.HotwordDetectionConnection.this.mVoiceInteractionServiceUid);
            com.android.server.voiceinteraction.HotwordDetectionConnection.this.addServiceUidForAudioPolicy(uid);
        }

        static /* synthetic */ int lambda$sendResult$0(int uid) {
            return uid;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateServiceIdentity$11(android.service.voice.ISandboxedDetectionService service) throws java.lang.Exception {
        service.ping(new com.android.server.voiceinteraction.HotwordDetectionConnection.AnonymousClass2());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateServiceIdentity(com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnection connection) {
        connection.run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$$ExternalSyntheticLambda8
            public final void runNoResult(java.lang.Object obj) throws java.lang.Exception {
                this.f$0.lambda$updateServiceIdentity$11((android.service.voice.ISandboxedDetectionService) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStorageService(com.android.server.voiceinteraction.HotwordDetectionConnection.ServiceConnection connection) {
        connection.run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$$ExternalSyntheticLambda7
            public final void runNoResult(java.lang.Object obj) throws java.lang.Exception {
                this.f$0.lambda$updateStorageService$12((android.service.voice.ISandboxedDetectionService) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateStorageService$12(android.service.voice.ISandboxedDetectionService service) throws java.lang.Exception {
        service.registerRemoteStorageService(new android.service.voice.IDetectorSessionStorageService.Stub() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection.3
            public void openFile(java.lang.String filename, com.android.internal.infra.AndroidFuture future) throws android.os.RemoteException {
                android.util.Slog.v(com.android.server.voiceinteraction.HotwordDetectionConnection.TAG, "BinderCallback#onFileOpen");
                try {
                    com.android.server.voiceinteraction.HotwordDetectionConnection.this.mHotwordRecognitionCallback.onOpenFile(filename, future);
                } catch (android.os.RemoteException e) {
                    e.rethrowFromSystemServer();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addServiceUidForAudioPolicy(final int uid) {
        this.mScheduledExecutorService.execute(new java.lang.Runnable() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.voiceinteraction.HotwordDetectionConnection.lambda$addServiceUidForAudioPolicy$13(uid);
            }
        });
    }

    static /* synthetic */ void lambda$addServiceUidForAudioPolicy$13(int uid) {
        android.media.AudioManagerInternal audioManager = (android.media.AudioManagerInternal) com.android.server.LocalServices.getService(android.media.AudioManagerInternal.class);
        if (audioManager != null) {
            audioManager.addAssistantServiceUid(uid);
        }
    }

    private void removeServiceUidForAudioPolicy(final int uid) {
        this.mScheduledExecutorService.execute(new java.lang.Runnable() { // from class: com.android.server.voiceinteraction.HotwordDetectionConnection$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.voiceinteraction.HotwordDetectionConnection.lambda$removeServiceUidForAudioPolicy$14(uid);
            }
        });
    }

    static /* synthetic */ void lambda$removeServiceUidForAudioPolicy$14(int uid) {
        android.media.AudioManagerInternal audioManager = (android.media.AudioManagerInternal) com.android.server.LocalServices.getService(android.media.AudioManagerInternal.class);
        if (audioManager != null) {
            audioManager.removeAssistantServiceUid(uid);
        }
    }
}
