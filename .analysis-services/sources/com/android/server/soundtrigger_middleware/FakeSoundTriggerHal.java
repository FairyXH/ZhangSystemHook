package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public class FakeSoundTriggerHal extends android.hardware.soundtrigger3.ISoundTriggerHw.Stub {
    private static final java.lang.String TAG = "FakeSoundTriggerHal";
    private static final int THRESHOLD_MAX = 10;
    private static final int THRESHOLD_MIN = -10;
    private android.os.IBinder.DeathRecipient mDeathRecipient;
    private com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.GlobalCallbackDispatcher mGlobalCallbackDispatcher;
    private final com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.InjectionDispatcher mInjectionDispatcher;
    private final java.lang.Object mLock = new java.lang.Object();
    private boolean mIsResourceContended = false;
    private final java.util.Map<java.lang.Integer, com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession> mModelSessionMap = new java.util.HashMap();
    private int mModelKeyCounter = 101;
    private boolean mIsDead = false;
    private final android.media.soundtrigger.Properties mProperties = createDefaultProperties();
    private final android.media.soundtrigger_middleware.IInjectGlobalEvent.Stub mGlobalEventSession = new com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.AnonymousClass1();

    static class ExecutorHolder {
        static final java.util.concurrent.Executor CALLBACK_EXECUTOR = java.util.concurrent.Executors.newSingleThreadExecutor();
        static final java.util.concurrent.Executor INJECTION_EXECUTOR = java.util.concurrent.Executors.newSingleThreadExecutor();

        ExecutorHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ModelSession extends android.media.soundtrigger_middleware.IInjectModelEvent.Stub {
        private final com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.CallbackDispatcher mCallbackDispatcher;
        private final boolean mIsKeyphrase;
        private boolean mIsUnloaded;
        private final int mModelHandle;
        private com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.RecognitionSession mRecognitionSession;
        private int mThreshold;

        private ModelSession(int modelHandle, com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.CallbackDispatcher callbackDispatcher, boolean isKeyphrase) {
            this.mThreshold = 0;
            this.mIsUnloaded = false;
            this.mModelHandle = modelHandle;
            this.mCallbackDispatcher = callbackDispatcher;
            this.mIsKeyphrase = isKeyphrase;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.RecognitionSession startRecognitionForModel() {
            com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.RecognitionSession recognitionSession;
            synchronized (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mLock) {
                this.mRecognitionSession = new com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.RecognitionSession();
                recognitionSession = this.mRecognitionSession;
            }
            return recognitionSession;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.RecognitionSession stopRecognitionForModel() {
            com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.RecognitionSession session;
            synchronized (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mLock) {
                session = this.mRecognitionSession;
                this.mRecognitionSession = null;
            }
            return session;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void forceRecognitionForModel() {
            synchronized (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mLock) {
                if (this.mIsKeyphrase) {
                    final android.media.soundtrigger.PhraseRecognitionEvent phraseEvent = com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.createDefaultKeyphraseEvent(3);
                    this.mCallbackDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$ModelSession$$ExternalSyntheticLambda2
                        public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                            this.f$0.lambda$forceRecognitionForModel$0(phraseEvent, (android.hardware.soundtrigger3.ISoundTriggerHwCallback) obj);
                        }
                    });
                } else {
                    final android.media.soundtrigger.RecognitionEvent event = com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.createDefaultEvent(3);
                    this.mCallbackDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$ModelSession$$ExternalSyntheticLambda3
                        public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                            this.f$0.lambda$forceRecognitionForModel$1(event, (android.hardware.soundtrigger3.ISoundTriggerHwCallback) obj);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$forceRecognitionForModel$0(android.media.soundtrigger.PhraseRecognitionEvent phraseEvent, android.hardware.soundtrigger3.ISoundTriggerHwCallback cb) throws java.lang.Exception {
            cb.phraseRecognitionCallback(this.mModelHandle, phraseEvent);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$forceRecognitionForModel$1(android.media.soundtrigger.RecognitionEvent event, android.hardware.soundtrigger3.ISoundTriggerHwCallback cb) throws java.lang.Exception {
            cb.recognitionCallback(this.mModelHandle, event);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setThresholdFactor(int value) {
            synchronized (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mLock) {
                this.mThreshold = value;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getThresholdFactor() {
            int i;
            synchronized (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mLock) {
                i = this.mThreshold;
            }
            return i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean getIsUnloaded() {
            boolean z;
            synchronized (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mLock) {
                z = this.mIsUnloaded;
            }
            return z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.RecognitionSession getRecogSession() {
            com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.RecognitionSession recognitionSession;
            synchronized (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mLock) {
                recognitionSession = this.mRecognitionSession;
            }
            return recognitionSession;
        }

        public void triggerUnloadModel() {
            synchronized (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mLock) {
                if (!com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mIsDead && !this.mIsUnloaded) {
                    if (this.mRecognitionSession != null) {
                        this.mRecognitionSession.triggerAbortRecognition();
                    }
                    this.mIsUnloaded = true;
                    this.mCallbackDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$ModelSession$$ExternalSyntheticLambda0
                        public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                            this.f$0.lambda$triggerUnloadModel$2((android.hardware.soundtrigger3.ISoundTriggerHwCallback) obj);
                        }
                    });
                    if (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.getNumLoadedModelsLocked() == com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mProperties.maxSoundModels - 1 && !com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mIsResourceContended) {
                        com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mGlobalCallbackDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$ModelSession$$ExternalSyntheticLambda1
                            public final void acceptOrThrow(java.lang.Object obj) {
                                ((android.hardware.soundtrigger3.ISoundTriggerHwGlobalCallback) obj).onResourcesAvailable();
                            }
                        });
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$triggerUnloadModel$2(android.hardware.soundtrigger3.ISoundTriggerHwCallback cb) throws java.lang.Exception {
            cb.modelUnloaded(this.mModelHandle);
        }

        /* JADX INFO: Access modifiers changed from: private */
        class RecognitionSession extends android.media.soundtrigger_middleware.IInjectRecognitionEvent.Stub {
            private RecognitionSession() {
            }

            public void triggerRecognitionEvent(byte[] data, android.media.soundtrigger.PhraseRecognitionExtra[] phraseExtras) {
                synchronized (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mLock) {
                    if (!com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mIsDead && com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.this.mRecognitionSession == this) {
                        com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.this.mRecognitionSession = null;
                        if (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.this.mIsKeyphrase) {
                            final android.media.soundtrigger.PhraseRecognitionEvent phraseEvent = com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.createDefaultKeyphraseEvent(0);
                            phraseEvent.common.data = data;
                            if (phraseExtras != null) {
                                phraseEvent.phraseExtras = phraseExtras;
                            }
                            com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.this.mCallbackDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$ModelSession$RecognitionSession$$ExternalSyntheticLambda2
                                public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                                    this.f$0.lambda$triggerRecognitionEvent$0(phraseEvent, (android.hardware.soundtrigger3.ISoundTriggerHwCallback) obj);
                                }
                            });
                        } else {
                            final android.media.soundtrigger.RecognitionEvent event = com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.createDefaultEvent(0);
                            event.data = data;
                            com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.this.mCallbackDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$ModelSession$RecognitionSession$$ExternalSyntheticLambda3
                                public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                                    this.f$0.lambda$triggerRecognitionEvent$1(event, (android.hardware.soundtrigger3.ISoundTriggerHwCallback) obj);
                                }
                            });
                        }
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$triggerRecognitionEvent$0(android.media.soundtrigger.PhraseRecognitionEvent phraseEvent, android.hardware.soundtrigger3.ISoundTriggerHwCallback cb) throws java.lang.Exception {
                cb.phraseRecognitionCallback(com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.this.mModelHandle, phraseEvent);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$triggerRecognitionEvent$1(android.media.soundtrigger.RecognitionEvent event, android.hardware.soundtrigger3.ISoundTriggerHwCallback cb) throws java.lang.Exception {
                cb.recognitionCallback(com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.this.mModelHandle, event);
            }

            public void triggerAbortRecognition() {
                synchronized (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mLock) {
                    if (!com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mIsDead && com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.this.mRecognitionSession == this) {
                        com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.this.mRecognitionSession = null;
                        if (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.this.mIsKeyphrase) {
                            com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.this.mCallbackDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$ModelSession$RecognitionSession$$ExternalSyntheticLambda0
                                public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                                    this.f$0.lambda$triggerAbortRecognition$2((android.hardware.soundtrigger3.ISoundTriggerHwCallback) obj);
                                }
                            });
                        } else {
                            com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.this.mCallbackDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$ModelSession$RecognitionSession$$ExternalSyntheticLambda1
                                public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                                    this.f$0.lambda$triggerAbortRecognition$3((android.hardware.soundtrigger3.ISoundTriggerHwCallback) obj);
                                }
                            });
                        }
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$triggerAbortRecognition$2(android.hardware.soundtrigger3.ISoundTriggerHwCallback cb) throws java.lang.Exception {
                cb.phraseRecognitionCallback(com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.this.mModelHandle, com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.createDefaultKeyphraseEvent(1));
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$triggerAbortRecognition$3(android.hardware.soundtrigger3.ISoundTriggerHwCallback cb) throws java.lang.Exception {
                cb.recognitionCallback(com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.this.mModelHandle, com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.createDefaultEvent(1));
            }
        }
    }

    public FakeSoundTriggerHal(android.media.soundtrigger_middleware.ISoundTriggerInjection injection) {
        this.mGlobalCallbackDispatcher = null;
        this.mInjectionDispatcher = new com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.InjectionDispatcher(injection);
        this.mGlobalCallbackDispatcher = null;
        this.mInjectionDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$$ExternalSyntheticLambda0
            public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                this.f$0.lambda$new$0((android.media.soundtrigger_middleware.ISoundTriggerInjection) obj);
            }
        });
    }

    /* JADX INFO: renamed from: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$1, reason: invalid class name */
    class AnonymousClass1 extends android.media.soundtrigger_middleware.IInjectGlobalEvent.Stub {
        AnonymousClass1() {
        }

        public void triggerRestart() {
            synchronized (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mLock) {
                if (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mIsDead) {
                    return;
                }
                com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mIsDead = true;
                com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mInjectionDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$1$$ExternalSyntheticLambda2
                    public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                        this.f$0.lambda$triggerRestart$0((android.media.soundtrigger_middleware.ISoundTriggerInjection) obj);
                    }
                });
                com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mModelSessionMap.clear();
                if (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mDeathRecipient != null) {
                    final android.os.IBinder.DeathRecipient deathRecipient = com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mDeathRecipient;
                    com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ExecutorHolder.CALLBACK_EXECUTOR.execute(new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$1$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$triggerRestart$1(deathRecipient);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$triggerRestart$0(android.media.soundtrigger_middleware.ISoundTriggerInjection cb) throws java.lang.Exception {
            cb.onRestarted(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$triggerRestart$1(android.os.IBinder.DeathRecipient deathRecipient) {
            try {
                deathRecipient.binderDied(com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.asBinder());
            } catch (java.lang.Throwable e) {
                android.util.Slog.wtf(com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.TAG, "Callback dispatch threw", e);
            }
        }

        public void setResourceContention(boolean isResourcesContended, final android.media.soundtrigger_middleware.IAcknowledgeEvent callback) {
            synchronized (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mLock) {
                if (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mIsDead) {
                    return;
                }
                boolean oldIsResourcesContended = com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mIsResourceContended;
                com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mIsResourceContended = isResourcesContended;
                com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mInjectionDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$1$$ExternalSyntheticLambda0
                    public final void acceptOrThrow(java.lang.Object obj) {
                        callback.eventReceived();
                    }
                });
                if (!com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mIsResourceContended && oldIsResourcesContended) {
                    com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mGlobalCallbackDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$1$$ExternalSyntheticLambda1
                        public final void acceptOrThrow(java.lang.Object obj) {
                            ((android.hardware.soundtrigger3.ISoundTriggerHwGlobalCallback) obj).onResourcesAvailable();
                        }
                    });
                }
            }
        }

        public void triggerOnResourcesAvailable() {
            synchronized (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mLock) {
                if (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mIsDead) {
                    return;
                }
                com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.this.mGlobalCallbackDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$1$$ExternalSyntheticLambda4
                    public final void acceptOrThrow(java.lang.Object obj) {
                        ((android.hardware.soundtrigger3.ISoundTriggerHwGlobalCallback) obj).onResourcesAvailable();
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(android.media.soundtrigger_middleware.ISoundTriggerInjection cb) throws java.lang.Exception {
        cb.registerGlobalEventInjection(this.mGlobalEventSession);
    }

    public android.media.soundtrigger_middleware.IInjectGlobalEvent getGlobalEventInjection() {
        return this.mGlobalEventSession;
    }

    public void linkToDeath(android.os.IBinder.DeathRecipient recipient, int flags) {
        synchronized (this.mLock) {
            if (this.mDeathRecipient != null) {
                android.util.Slog.wtf(TAG, "Received two death recipients concurrently");
            }
            this.mDeathRecipient = recipient;
        }
    }

    public boolean unlinkToDeath(android.os.IBinder.DeathRecipient recipient, int flags) {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                return false;
            }
            if (this.mDeathRecipient != recipient) {
                throw new java.util.NoSuchElementException();
            }
            this.mDeathRecipient = null;
            return true;
        }
    }

    public android.media.soundtrigger.Properties getProperties() throws android.os.RemoteException {
        android.media.soundtrigger.Properties properties;
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new android.os.DeadObjectException();
            }
            android.os.Parcel parcel = android.os.Parcel.obtain();
            try {
                this.mProperties.writeToParcel(parcel, 0);
                parcel.setDataPosition(0);
                properties = (android.media.soundtrigger.Properties) android.media.soundtrigger.Properties.CREATOR.createFromParcel(parcel);
            } finally {
                parcel.recycle();
            }
        }
        return properties;
    }

    public void registerGlobalCallback(android.hardware.soundtrigger3.ISoundTriggerHwGlobalCallback callback) throws android.os.RemoteException {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new android.os.DeadObjectException();
            }
            this.mGlobalCallbackDispatcher = new com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.GlobalCallbackDispatcher(callback);
        }
    }

    public int loadSoundModel(final android.media.soundtrigger.SoundModel soundModel, android.hardware.soundtrigger3.ISoundTriggerHwCallback callback) throws android.os.RemoteException {
        int key;
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new android.os.DeadObjectException();
            }
            if (this.mIsResourceContended || getNumLoadedModelsLocked() == this.mProperties.maxSoundModels) {
                throw new android.os.ServiceSpecificException(1);
            }
            key = this.mModelKeyCounter;
            this.mModelKeyCounter = key + 1;
            final com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession session = new com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession(key, new com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.CallbackDispatcher(callback), false);
            this.mModelSessionMap.put(java.lang.Integer.valueOf(key), session);
            this.mInjectionDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$$ExternalSyntheticLambda4
                public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                    this.f$0.lambda$loadSoundModel$1(soundModel, session, (android.media.soundtrigger_middleware.ISoundTriggerInjection) obj);
                }
            });
        }
        return key;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadSoundModel$1(android.media.soundtrigger.SoundModel soundModel, com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession session, android.media.soundtrigger_middleware.ISoundTriggerInjection cb) throws java.lang.Exception {
        cb.onSoundModelLoaded(soundModel, (android.media.soundtrigger.Phrase[]) null, session, this.mGlobalEventSession);
    }

    public int loadPhraseSoundModel(final android.media.soundtrigger.PhraseSoundModel soundModel, android.hardware.soundtrigger3.ISoundTriggerHwCallback callback) throws android.os.RemoteException {
        int key;
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new android.os.DeadObjectException();
            }
            if (this.mIsResourceContended || getNumLoadedModelsLocked() == this.mProperties.maxSoundModels) {
                throw new android.os.ServiceSpecificException(1);
            }
            key = this.mModelKeyCounter;
            this.mModelKeyCounter = key + 1;
            final com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession session = new com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession(key, new com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.CallbackDispatcher(callback), true);
            this.mModelSessionMap.put(java.lang.Integer.valueOf(key), session);
            this.mInjectionDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$$ExternalSyntheticLambda5
                public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                    this.f$0.lambda$loadPhraseSoundModel$2(soundModel, session, (android.media.soundtrigger_middleware.ISoundTriggerInjection) obj);
                }
            });
        }
        return key;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPhraseSoundModel$2(android.media.soundtrigger.PhraseSoundModel soundModel, com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession session, android.media.soundtrigger_middleware.ISoundTriggerInjection cb) throws java.lang.Exception {
        cb.onSoundModelLoaded(soundModel.common, soundModel.phrases, session, this.mGlobalEventSession);
    }

    public void unloadSoundModel(int modelHandle) throws android.os.RemoteException {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new android.os.DeadObjectException();
            }
            final com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession session = this.mModelSessionMap.get(java.lang.Integer.valueOf(modelHandle));
            if (session == null) {
                android.util.Slog.wtf(TAG, "Attempted to unload model which was never loaded");
            }
            if (session.getRecogSession() != null) {
                android.util.Slog.wtf(TAG, "Session unloaded before recog stopped!");
            }
            if (session.getIsUnloaded()) {
                return;
            }
            this.mInjectionDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$$ExternalSyntheticLambda1
                public final void acceptOrThrow(java.lang.Object obj) {
                    ((android.media.soundtrigger_middleware.ISoundTriggerInjection) obj).onSoundModelUnloaded(session);
                }
            });
            if (getNumLoadedModelsLocked() == this.mProperties.maxSoundModels - 1 && !this.mIsResourceContended) {
                this.mGlobalCallbackDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$$ExternalSyntheticLambda2
                    public final void acceptOrThrow(java.lang.Object obj) {
                        ((android.hardware.soundtrigger3.ISoundTriggerHwGlobalCallback) obj).onResourcesAvailable();
                    }
                });
            }
        }
    }

    public void startRecognition(int modelHandle, int deviceHandle, int ioHandle, final android.media.soundtrigger.RecognitionConfig config) throws android.os.RemoteException {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new android.os.DeadObjectException();
            }
            final com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession session = this.mModelSessionMap.get(java.lang.Integer.valueOf(modelHandle));
            if (session == null) {
                android.util.Slog.wtf(TAG, "Attempted to start recognition with invalid handle");
            }
            if (this.mIsResourceContended) {
                throw new android.os.ServiceSpecificException(1);
            }
            if (session.getIsUnloaded()) {
                throw new android.os.ServiceSpecificException(1);
            }
            final com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.RecognitionSession recogSession = session.startRecognitionForModel();
            this.mInjectionDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$$ExternalSyntheticLambda6
                public final void acceptOrThrow(java.lang.Object obj) {
                    ((android.media.soundtrigger_middleware.ISoundTriggerInjection) obj).onRecognitionStarted(-1, config, recogSession, session);
                }
            });
        }
    }

    public void stopRecognition(int modelHandle) throws android.os.RemoteException {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new android.os.DeadObjectException();
            }
            com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession session = this.mModelSessionMap.get(java.lang.Integer.valueOf(modelHandle));
            if (session == null) {
                android.util.Slog.wtf(TAG, "Attempted to stop recognition with invalid handle");
            }
            final com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession.RecognitionSession recogSession = session.stopRecognitionForModel();
            if (recogSession != null) {
                this.mInjectionDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$$ExternalSyntheticLambda7
                    public final void acceptOrThrow(java.lang.Object obj) {
                        ((android.media.soundtrigger_middleware.ISoundTriggerInjection) obj).onRecognitionStopped(recogSession);
                    }
                });
            }
        }
    }

    public void forceRecognitionEvent(int modelHandle) throws android.os.RemoteException {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new android.os.DeadObjectException();
            }
            com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession session = this.mModelSessionMap.get(java.lang.Integer.valueOf(modelHandle));
            if (session == null) {
                android.util.Slog.wtf(TAG, "Attempted to force recognition with invalid handle");
            }
            if (session.getRecogSession() == null) {
                return;
            }
            session.forceRecognitionForModel();
        }
    }

    public android.media.soundtrigger.ModelParameterRange queryParameter(int modelHandle, int modelParam) throws android.os.RemoteException {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new android.os.DeadObjectException();
            }
            com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession session = this.mModelSessionMap.get(java.lang.Integer.valueOf(modelHandle));
            if (session == null) {
                android.util.Slog.wtf(TAG, "Attempted to get param with invalid handle");
            }
        }
        if (modelParam == 0) {
            android.media.soundtrigger.ModelParameterRange range = new android.media.soundtrigger.ModelParameterRange();
            range.minInclusive = -10;
            range.maxInclusive = 10;
            return range;
        }
        return null;
    }

    public int getParameter(int modelHandle, int modelParam) throws android.os.RemoteException {
        int thresholdFactor;
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new android.os.DeadObjectException();
            }
            com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession session = this.mModelSessionMap.get(java.lang.Integer.valueOf(modelHandle));
            if (session == null) {
                android.util.Slog.wtf(TAG, "Attempted to get param with invalid handle");
            }
            if (modelParam != 0) {
                throw new java.lang.IllegalArgumentException();
            }
            thresholdFactor = session.getThresholdFactor();
        }
        return thresholdFactor;
    }

    public void setParameter(int modelHandle, final int modelParam, final int value) throws android.os.RemoteException {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new android.os.DeadObjectException();
            }
            final com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession session = this.mModelSessionMap.get(java.lang.Integer.valueOf(modelHandle));
            if (session == null) {
                android.util.Slog.wtf(TAG, "Attempted to get param with invalid handle");
            }
            if (modelParam != 0 && (value < -10 || value > 10)) {
                throw new java.lang.IllegalArgumentException();
            }
            session.setThresholdFactor(value);
            this.mInjectionDispatcher.wrap(new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$$ExternalSyntheticLambda3
                public final void acceptOrThrow(java.lang.Object obj) {
                    ((android.media.soundtrigger_middleware.ISoundTriggerInjection) obj).onParamSet(modelParam, value, session);
                }
            });
        }
    }

    public int getInterfaceVersion() throws android.os.RemoteException {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new android.os.DeadObjectException();
            }
        }
        return 2;
    }

    public java.lang.String getInterfaceHash() throws android.os.RemoteException {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new android.os.DeadObjectException();
            }
        }
        return "6b24e60ad261e3ff56106efd86ce6aa7ef5621b0";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getNumLoadedModelsLocked() {
        int numModels = 0;
        for (com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ModelSession session : this.mModelSessionMap.values()) {
            if (!session.getIsUnloaded()) {
                numModels++;
            }
        }
        return numModels;
    }

    private static android.media.soundtrigger.Properties createDefaultProperties() {
        android.media.soundtrigger.Properties properties = new android.media.soundtrigger.Properties();
        properties.implementor = com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME;
        properties.description = "AOSP fake STHAL";
        properties.version = 1;
        properties.uuid = "00000001-0002-0003-0004-deadbeefabcd";
        properties.supportedModelArch = "injection";
        properties.maxSoundModels = 8;
        properties.maxKeyPhrases = 2;
        properties.maxUsers = 2;
        properties.recognitionModes = 9;
        properties.captureTransition = true;
        properties.maxBufferMs = 5000;
        properties.concurrentCapture = true;
        properties.triggerInEvent = false;
        properties.powerConsumptionMw = 0;
        properties.audioCapabilities = 0;
        return properties;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.media.soundtrigger.RecognitionEvent createDefaultEvent(int status) {
        android.media.soundtrigger.RecognitionEvent event = new android.media.soundtrigger.RecognitionEvent();
        event.status = status;
        event.type = 1;
        event.captureAvailable = true;
        event.captureDelayMs = 50;
        event.capturePreambleMs = 200;
        event.triggerInData = false;
        event.audioConfig = null;
        event.data = new byte[0];
        event.recognitionStillActive = false;
        return event;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.media.soundtrigger.PhraseRecognitionEvent createDefaultKeyphraseEvent(int status) {
        android.media.soundtrigger.RecognitionEvent event = createDefaultEvent(status);
        event.type = 0;
        android.media.soundtrigger.PhraseRecognitionEvent phraseEvent = new android.media.soundtrigger.PhraseRecognitionEvent();
        phraseEvent.common = event;
        phraseEvent.phraseExtras = new android.media.soundtrigger.PhraseRecognitionExtra[0];
        return phraseEvent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class CallbackDispatcher {
        private final android.hardware.soundtrigger3.ISoundTriggerHwCallback mCallback;

        private CallbackDispatcher(android.hardware.soundtrigger3.ISoundTriggerHwCallback callback) {
            this.mCallback = callback;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void wrap(final com.android.internal.util.FunctionalUtils.ThrowingConsumer<android.hardware.soundtrigger3.ISoundTriggerHwCallback> command) {
            com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ExecutorHolder.CALLBACK_EXECUTOR.execute(new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$CallbackDispatcher$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$wrap$0(command);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$wrap$0(com.android.internal.util.FunctionalUtils.ThrowingConsumer command) {
            try {
                command.accept(this.mCallback);
            } catch (java.lang.Throwable e) {
                android.util.Slog.wtf(com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.TAG, "Callback dispatch threw", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class GlobalCallbackDispatcher {
        private final android.hardware.soundtrigger3.ISoundTriggerHwGlobalCallback mCallback;

        private GlobalCallbackDispatcher(android.hardware.soundtrigger3.ISoundTriggerHwGlobalCallback callback) {
            this.mCallback = callback;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void wrap(final com.android.internal.util.FunctionalUtils.ThrowingConsumer<android.hardware.soundtrigger3.ISoundTriggerHwGlobalCallback> command) {
            com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ExecutorHolder.CALLBACK_EXECUTOR.execute(new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$GlobalCallbackDispatcher$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$wrap$0(command);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$wrap$0(com.android.internal.util.FunctionalUtils.ThrowingConsumer command) {
            try {
                command.accept(this.mCallback);
            } catch (java.lang.Throwable e) {
                android.util.Slog.wtf(com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.TAG, "Callback dispatch threw", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class InjectionDispatcher {
        private final android.media.soundtrigger_middleware.ISoundTriggerInjection mInjection;

        private InjectionDispatcher(android.media.soundtrigger_middleware.ISoundTriggerInjection injection) {
            this.mInjection = injection;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void wrap(final com.android.internal.util.FunctionalUtils.ThrowingConsumer<android.media.soundtrigger_middleware.ISoundTriggerInjection> command) {
            com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ExecutorHolder.INJECTION_EXECUTOR.execute(new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$InjectionDispatcher$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$wrap$0(command);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$wrap$0(com.android.internal.util.FunctionalUtils.ThrowingConsumer command) {
            try {
                command.accept(this.mInjection);
            } catch (java.lang.Throwable e) {
                android.util.Slog.wtf(com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.TAG, "Callback dispatch threw", e);
            }
        }
    }
}
