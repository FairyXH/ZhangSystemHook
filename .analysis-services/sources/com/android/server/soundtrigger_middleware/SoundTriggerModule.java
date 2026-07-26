package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
class SoundTriggerModule implements android.os.IBinder.DeathRecipient, com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback {
    private static final java.lang.String TAG = "SoundTriggerModule";
    private final java.util.Set<com.android.server.soundtrigger_middleware.SoundTriggerModule.Session> mActiveSessions = new java.util.HashSet();
    private final com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareImpl.AudioSessionProvider mAudioSessionProvider;
    private final com.android.server.soundtrigger_middleware.HalFactory mHalFactory;
    private com.android.server.soundtrigger_middleware.ISoundTriggerHal mHalService;
    private android.media.soundtrigger.Properties mProperties;

    private enum ModelState {
        INIT,
        LOADED,
        ACTIVE
    }

    SoundTriggerModule(com.android.server.soundtrigger_middleware.HalFactory halFactory, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareImpl.AudioSessionProvider audioSessionProvider) {
        this.mHalFactory = (com.android.server.soundtrigger_middleware.HalFactory) java.util.Objects.requireNonNull(halFactory);
        this.mAudioSessionProvider = (com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareImpl.AudioSessionProvider) java.util.Objects.requireNonNull(audioSessionProvider);
        attachToHal();
    }

    synchronized android.media.soundtrigger_middleware.ISoundTriggerModule attach(android.media.soundtrigger_middleware.ISoundTriggerCallback callback) {
        com.android.server.soundtrigger_middleware.SoundTriggerModule.Session session;
        session = new com.android.server.soundtrigger_middleware.SoundTriggerModule.Session(callback);
        this.mActiveSessions.add(session);
        return session;
    }

    synchronized android.media.soundtrigger.Properties getProperties() {
        return this.mProperties;
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        java.util.List<android.media.soundtrigger_middleware.ISoundTriggerCallback> callbacks;
        android.util.Slog.w(TAG, "Underlying HAL driver died.");
        synchronized (this) {
            callbacks = new java.util.ArrayList<>(this.mActiveSessions.size());
            for (com.android.server.soundtrigger_middleware.SoundTriggerModule.Session session : this.mActiveSessions) {
                callbacks.add(session.moduleDied());
            }
            this.mActiveSessions.clear();
            reset();
        }
        for (android.media.soundtrigger_middleware.ISoundTriggerCallback callback : callbacks) {
            try {
                callback.onModuleDied();
            } catch (android.os.RemoteException e) {
                throw e.rethrowAsRuntimeException();
            }
        }
    }

    private void reset() {
        this.mHalService.detach();
        attachToHal();
    }

    private void attachToHal() {
        android.util.Log.d(TAG, "attachToHal begin.");
        if (android.os.Build.isQcomPlatform() || android.os.Build.isMtkPlatform()) {
            this.mHalService = null;
            while (this.mHalService == null) {
                try {
                    this.mHalService = new com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer(new com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog(new com.android.server.soundtrigger_middleware.SoundTriggerDuplicateModelHandler(this.mHalFactory.create())));
                } catch (java.lang.RuntimeException e) {
                    android.util.Log.w(TAG, "Failed to init SoundTriggerHal.", e);
                }
            }
        } else {
            this.mHalService = new com.android.server.soundtrigger_middleware.SoundTriggerHalEnforcer(new com.android.server.soundtrigger_middleware.SoundTriggerHalWatchdog(this.mHalFactory.create()));
        }
        this.mHalService.linkToDeath(this);
        this.mHalService.registerCallback(this);
        this.mProperties = this.mHalService.getProperties();
        android.util.Log.d(TAG, "attachToHal end.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeSession(com.android.server.soundtrigger_middleware.SoundTriggerModule.Session session) {
        this.mActiveSessions.remove(session);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback
    public void onResourcesAvailable() {
        java.util.List<android.media.soundtrigger_middleware.ISoundTriggerCallback> callbacks;
        synchronized (this) {
            callbacks = new java.util.ArrayList<>(this.mActiveSessions.size());
            for (com.android.server.soundtrigger_middleware.SoundTriggerModule.Session session : this.mActiveSessions) {
                callbacks.add(session.mCallback);
            }
        }
        for (android.media.soundtrigger_middleware.ISoundTriggerCallback callback : callbacks) {
            try {
                callback.onResourcesAvailable();
            } catch (android.os.RemoteException e) {
                throw e.rethrowAsRuntimeException();
            }
        }
    }

    private class Session implements android.media.soundtrigger_middleware.ISoundTriggerModule {
        private android.media.soundtrigger_middleware.ISoundTriggerCallback mCallback;
        private final java.util.Map<java.lang.Integer, com.android.server.soundtrigger_middleware.SoundTriggerModule.Session.Model> mLoadedModels;
        private final android.os.IBinder mToken;

        private Session(android.media.soundtrigger_middleware.ISoundTriggerCallback callback) {
            this.mToken = new android.os.Binder();
            this.mLoadedModels = new java.util.HashMap();
            this.mCallback = callback;
            com.android.server.soundtrigger_middleware.SoundTriggerModule.this.mHalService.clientAttached(this.mToken);
        }

        public void detach() {
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerModule.this) {
                if (this.mCallback == null) {
                    return;
                }
                com.android.server.soundtrigger_middleware.SoundTriggerModule.this.removeSession(this);
                this.mCallback = null;
                com.android.server.soundtrigger_middleware.SoundTriggerModule.this.mHalService.clientDetached(this.mToken);
            }
        }

        public int loadModel(android.media.soundtrigger.SoundModel model) {
            int iLoad;
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerModule.this) {
                com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareImpl.AudioSessionProvider.AudioSession audioSession = com.android.server.soundtrigger_middleware.SoundTriggerModule.this.mAudioSessionProvider.acquireSession();
                try {
                    checkValid();
                    com.android.server.soundtrigger_middleware.SoundTriggerModule.Session.Model loadedModel = new com.android.server.soundtrigger_middleware.SoundTriggerModule.Session.Model();
                    iLoad = loadedModel.load(model, audioSession);
                } catch (java.lang.Exception e) {
                    try {
                        com.android.server.soundtrigger_middleware.SoundTriggerModule.this.mAudioSessionProvider.releaseSession(audioSession.mSessionHandle);
                    } catch (java.lang.Exception ee) {
                        android.util.Slog.e(com.android.server.soundtrigger_middleware.SoundTriggerModule.TAG, "Failed to release session.", ee);
                    }
                    throw e;
                }
            }
            return iLoad;
        }

        public int loadPhraseModel(android.media.soundtrigger.PhraseSoundModel model) {
            int result;
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerModule.this) {
                com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareImpl.AudioSessionProvider.AudioSession audioSession = com.android.server.soundtrigger_middleware.SoundTriggerModule.this.mAudioSessionProvider.acquireSession();
                try {
                    checkValid();
                    com.android.server.soundtrigger_middleware.SoundTriggerModule.Session.Model loadedModel = new com.android.server.soundtrigger_middleware.SoundTriggerModule.Session.Model();
                    result = loadedModel.load(model, audioSession);
                    android.util.Slog.d(com.android.server.soundtrigger_middleware.SoundTriggerModule.TAG, java.lang.String.format("loadPhraseModel()->%d", java.lang.Integer.valueOf(result)));
                } catch (java.lang.Exception e) {
                    try {
                        com.android.server.soundtrigger_middleware.SoundTriggerModule.this.mAudioSessionProvider.releaseSession(audioSession.mSessionHandle);
                    } catch (java.lang.Exception ee) {
                        android.util.Slog.e(com.android.server.soundtrigger_middleware.SoundTriggerModule.TAG, "Failed to release session.", ee);
                    }
                    throw e;
                }
            }
            return result;
        }

        public void unloadModel(int modelHandle) {
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerModule.this) {
                checkValid();
                com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareImpl.AudioSessionProvider.AudioSession session = this.mLoadedModels.get(java.lang.Integer.valueOf(modelHandle)).getSession();
                this.mLoadedModels.remove(java.lang.Integer.valueOf(modelHandle));
                com.android.server.soundtrigger_middleware.SoundTriggerModule.this.mAudioSessionProvider.releaseSession(session.mSessionHandle);
            }
            com.android.server.soundtrigger_middleware.SoundTriggerModule.this.mHalService.unloadSoundModel(modelHandle);
        }

        public android.os.IBinder startRecognition(int modelHandle, android.media.soundtrigger.RecognitionConfig config) {
            android.os.IBinder iBinderStartRecognition;
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerModule.this) {
                checkValid();
                iBinderStartRecognition = this.mLoadedModels.get(java.lang.Integer.valueOf(modelHandle)).startRecognition(config);
            }
            return iBinderStartRecognition;
        }

        public void stopRecognition(int modelHandle) {
            com.android.server.soundtrigger_middleware.SoundTriggerModule.Session.Model model;
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerModule.this) {
                checkValid();
                model = this.mLoadedModels.get(java.lang.Integer.valueOf(modelHandle));
            }
            model.stopRecognition();
        }

        public void forceRecognitionEvent(int modelHandle) {
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerModule.this) {
                checkValid();
                this.mLoadedModels.get(java.lang.Integer.valueOf(modelHandle)).forceRecognitionEvent();
            }
        }

        public void setModelParameter(int modelHandle, int modelParam, int value) {
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerModule.this) {
                checkValid();
                this.mLoadedModels.get(java.lang.Integer.valueOf(modelHandle)).setParameter(modelParam, value);
            }
        }

        public int getModelParameter(int modelHandle, int modelParam) {
            int parameter;
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerModule.this) {
                checkValid();
                parameter = this.mLoadedModels.get(java.lang.Integer.valueOf(modelHandle)).getParameter(modelParam);
            }
            return parameter;
        }

        public android.media.soundtrigger.ModelParameterRange queryModelParameterSupport(int modelHandle, int modelParam) {
            android.media.soundtrigger.ModelParameterRange modelParameterRangeQueryModelParameterSupport;
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerModule.this) {
                checkValid();
                modelParameterRangeQueryModelParameterSupport = this.mLoadedModels.get(java.lang.Integer.valueOf(modelHandle)).queryModelParameterSupport(modelParam);
            }
            return modelParameterRangeQueryModelParameterSupport;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.media.soundtrigger_middleware.ISoundTriggerCallback moduleDied() {
            android.media.soundtrigger_middleware.ISoundTriggerCallback callback = this.mCallback;
            this.mCallback = null;
            return callback;
        }

        private void checkValid() {
            if (this.mCallback == null) {
                throw new com.android.server.soundtrigger_middleware.RecoverableException(4);
            }
        }

        public android.os.IBinder asBinder() {
            throw new java.lang.UnsupportedOperationException("This implementation is not intended to be used directly with Binder.");
        }

        private class Model implements com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback {
            public int mHandle;
            private boolean mIsStopping;
            private android.os.IBinder mRecognitionToken;
            private com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareImpl.AudioSessionProvider.AudioSession mSession;
            private com.android.server.soundtrigger_middleware.SoundTriggerModule.ModelState mState;

            private Model() {
                this.mState = com.android.server.soundtrigger_middleware.SoundTriggerModule.ModelState.INIT;
                this.mRecognitionToken = null;
                this.mIsStopping = false;
            }

            private com.android.server.soundtrigger_middleware.SoundTriggerModule.ModelState getState() {
                return this.mState;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareImpl.AudioSessionProvider.AudioSession getSession() {
                return this.mSession;
            }

            private void setState(com.android.server.soundtrigger_middleware.SoundTriggerModule.ModelState state) {
                this.mState = state;
                com.android.server.soundtrigger_middleware.SoundTriggerModule.this.notifyAll();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public int load(android.media.soundtrigger.SoundModel model, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareImpl.AudioSessionProvider.AudioSession audioSession) {
                this.mSession = audioSession;
                this.mHandle = com.android.server.soundtrigger_middleware.SoundTriggerModule.this.mHalService.loadSoundModel(model, this);
                setState(com.android.server.soundtrigger_middleware.SoundTriggerModule.ModelState.LOADED);
                com.android.server.soundtrigger_middleware.SoundTriggerModule.Session.this.mLoadedModels.put(java.lang.Integer.valueOf(this.mHandle), this);
                return this.mHandle;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public int load(android.media.soundtrigger.PhraseSoundModel model, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareImpl.AudioSessionProvider.AudioSession audioSession) {
                this.mSession = audioSession;
                this.mHandle = com.android.server.soundtrigger_middleware.SoundTriggerModule.this.mHalService.loadPhraseSoundModel(model, this);
                setState(com.android.server.soundtrigger_middleware.SoundTriggerModule.ModelState.LOADED);
                com.android.server.soundtrigger_middleware.SoundTriggerModule.Session.this.mLoadedModels.put(java.lang.Integer.valueOf(this.mHandle), this);
                return this.mHandle;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public android.os.IBinder startRecognition(android.media.soundtrigger.RecognitionConfig config) {
                if (this.mIsStopping) {
                    throw new com.android.server.soundtrigger_middleware.RecoverableException(5, "Race occurred");
                }
                com.android.server.soundtrigger_middleware.SoundTriggerModule.this.mHalService.startRecognition(this.mHandle, this.mSession.mDeviceHandle, this.mSession.mIoHandle, config);
                this.mRecognitionToken = new android.os.Binder();
                setState(com.android.server.soundtrigger_middleware.SoundTriggerModule.ModelState.ACTIVE);
                return this.mRecognitionToken;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void stopRecognition() {
                synchronized (com.android.server.soundtrigger_middleware.SoundTriggerModule.this) {
                    if (getState() == com.android.server.soundtrigger_middleware.SoundTriggerModule.ModelState.LOADED) {
                        return;
                    }
                    this.mRecognitionToken = null;
                    this.mIsStopping = true;
                    com.android.server.soundtrigger_middleware.SoundTriggerModule.this.mHalService.stopRecognition(this.mHandle);
                    synchronized (com.android.server.soundtrigger_middleware.SoundTriggerModule.this) {
                        this.mIsStopping = false;
                        setState(com.android.server.soundtrigger_middleware.SoundTriggerModule.ModelState.LOADED);
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void forceRecognitionEvent() {
                if (getState() != com.android.server.soundtrigger_middleware.SoundTriggerModule.ModelState.ACTIVE) {
                    return;
                }
                com.android.server.soundtrigger_middleware.SoundTriggerModule.this.mHalService.forceRecognitionEvent(this.mHandle);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setParameter(int modelParam, int value) {
                com.android.server.soundtrigger_middleware.SoundTriggerModule.this.mHalService.setModelParameter(this.mHandle, com.android.server.soundtrigger_middleware.ConversionUtil.aidl2hidlModelParameter(modelParam), value);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public int getParameter(int modelParam) {
                return com.android.server.soundtrigger_middleware.SoundTriggerModule.this.mHalService.getModelParameter(this.mHandle, com.android.server.soundtrigger_middleware.ConversionUtil.aidl2hidlModelParameter(modelParam));
            }

            /* JADX INFO: Access modifiers changed from: private */
            public android.media.soundtrigger.ModelParameterRange queryModelParameterSupport(int modelParam) {
                return com.android.server.soundtrigger_middleware.SoundTriggerModule.this.mHalService.queryParameter(this.mHandle, modelParam);
            }

            @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback
            public void recognitionCallback(int modelHandle, android.media.soundtrigger_middleware.RecognitionEventSys event) {
                synchronized (com.android.server.soundtrigger_middleware.SoundTriggerModule.this) {
                    if (this.mRecognitionToken == null) {
                        return;
                    }
                    event.token = this.mRecognitionToken;
                    if (!event.recognitionEvent.recognitionStillActive) {
                        setState(com.android.server.soundtrigger_middleware.SoundTriggerModule.ModelState.LOADED);
                        this.mRecognitionToken = null;
                    }
                    android.media.soundtrigger_middleware.ISoundTriggerCallback callback = com.android.server.soundtrigger_middleware.SoundTriggerModule.Session.this.mCallback;
                    if (callback != null) {
                        try {
                            callback.onRecognition(this.mHandle, event, this.mSession.mSessionHandle);
                        } catch (android.os.RemoteException e) {
                            throw e.rethrowAsRuntimeException();
                        }
                    }
                }
            }

            @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback
            public void phraseRecognitionCallback(int modelHandle, android.media.soundtrigger_middleware.PhraseRecognitionEventSys event) {
                synchronized (com.android.server.soundtrigger_middleware.SoundTriggerModule.this) {
                    if (this.mRecognitionToken == null) {
                        return;
                    }
                    event.token = this.mRecognitionToken;
                    if (android.os.Build.isMtkPlatform()) {
                        int phraseRecognitionEventCommonDataLength = 0;
                        if (event.phraseRecognitionEvent.common.data != null) {
                            phraseRecognitionEventCommonDataLength = event.phraseRecognitionEvent.common.data.length;
                        }
                        if (!event.phraseRecognitionEvent.common.recognitionStillActive && (phraseRecognitionEventCommonDataLength == 0 || (phraseRecognitionEventCommonDataLength > 0 && event.phraseRecognitionEvent.common.data[0] != 1))) {
                            android.util.Log.i(com.android.server.soundtrigger_middleware.SoundTriggerModule.TAG, "[phraseRecognitionCallback] ModelState.LOADED ");
                            setState(com.android.server.soundtrigger_middleware.SoundTriggerModule.ModelState.LOADED);
                            this.mRecognitionToken = null;
                        }
                    } else if (!event.phraseRecognitionEvent.common.recognitionStillActive) {
                        setState(com.android.server.soundtrigger_middleware.SoundTriggerModule.ModelState.LOADED);
                        this.mRecognitionToken = null;
                    }
                    android.media.soundtrigger_middleware.ISoundTriggerCallback callback = com.android.server.soundtrigger_middleware.SoundTriggerModule.Session.this.mCallback;
                    if (callback != null) {
                        try {
                            callback.onPhraseRecognition(this.mHandle, event, this.mSession.mSessionHandle);
                        } catch (android.os.RemoteException e) {
                            throw e.rethrowAsRuntimeException();
                        }
                    }
                }
            }

            @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback
            public void modelUnloaded(int modelHandle) {
                android.media.soundtrigger_middleware.ISoundTriggerCallback callback;
                synchronized (com.android.server.soundtrigger_middleware.SoundTriggerModule.this) {
                    callback = com.android.server.soundtrigger_middleware.SoundTriggerModule.Session.this.mCallback;
                }
                if (callback != null) {
                    try {
                        callback.onModelUnloaded(modelHandle);
                    } catch (android.os.RemoteException e) {
                        throw e.rethrowAsRuntimeException();
                    }
                }
            }
        }
    }
}
