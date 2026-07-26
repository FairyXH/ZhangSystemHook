package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public class SoundTriggerHalConcurrentCaptureHandler implements com.android.server.soundtrigger_middleware.ISoundTriggerHal, com.android.server.soundtrigger_middleware.ICaptureStateNotifier.Listener {
    private static final java.lang.String TAG = "SoundTriggerHalConcurrentCaptureHandler";
    private boolean mCaptureState;
    private final com.android.server.soundtrigger_middleware.ISoundTriggerHal mDelegate;
    private com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback mGlobalCallback;
    private final com.android.server.soundtrigger_middleware.ICaptureStateNotifier mNotifier;
    private final java.lang.Object mStartStopLock = new java.lang.Object();
    private final java.util.Map<java.lang.Integer, com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.LoadedModel> mLoadedModels = new java.util.concurrent.ConcurrentHashMap();
    private final java.util.Set<java.lang.Integer> mActiveModels = new java.util.HashSet();
    private final java.util.Map<android.os.IBinder.DeathRecipient, android.os.IBinder.DeathRecipient> mDeathRecipientMap = new java.util.concurrent.ConcurrentHashMap();
    private final com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.CallbackThread mCallbackThread = new com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.CallbackThread();

    /* JADX INFO: Access modifiers changed from: private */
    static class LoadedModel {
        public final com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback callback;
        public final int type;

        LoadedModel(int type, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback callback) {
            this.type = type;
            this.callback = callback;
        }
    }

    public SoundTriggerHalConcurrentCaptureHandler(com.android.server.soundtrigger_middleware.ISoundTriggerHal delegate, com.android.server.soundtrigger_middleware.ICaptureStateNotifier notifier) {
        this.mDelegate = delegate;
        this.mNotifier = notifier;
        this.mCaptureState = this.mNotifier.registerListener(this);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void startRecognition(int modelHandle, int deviceHandle, int ioHandle, android.media.soundtrigger.RecognitionConfig config) {
        synchronized (this.mStartStopLock) {
            synchronized (this.mActiveModels) {
                android.util.Log.d(TAG, "startRecognition, mCaptureState = " + this.mCaptureState);
                if (this.mCaptureState) {
                    throw new com.android.server.soundtrigger_middleware.RecoverableException(1);
                }
                this.mDelegate.startRecognition(modelHandle, deviceHandle, ioHandle, config);
                this.mActiveModels.add(java.lang.Integer.valueOf(modelHandle));
            }
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void stopRecognition(int modelHandle) {
        boolean wasActive;
        synchronized (this.mStartStopLock) {
            synchronized (this.mActiveModels) {
                wasActive = this.mActiveModels.remove(java.lang.Integer.valueOf(modelHandle));
            }
            if (wasActive) {
                this.mDelegate.stopRecognition(modelHandle);
            }
        }
        this.mCallbackThread.flush();
    }

    @Override // com.android.server.soundtrigger_middleware.ICaptureStateNotifier.Listener
    public void onCaptureStateChange(boolean active) {
        synchronized (this.mStartStopLock) {
            if (active) {
                abortAllActiveModels();
            } else if (this.mGlobalCallback != null) {
                this.mGlobalCallback.onResourcesAvailable();
            }
            this.mCaptureState = active;
            android.util.Log.d(TAG, "onCaptureStateChange, mCaptureState = " + this.mCaptureState);
        }
    }

    private void abortAllActiveModels() {
        final int toStop;
        while (true) {
            synchronized (this.mActiveModels) {
                java.util.Iterator<java.lang.Integer> iterator = this.mActiveModels.iterator();
                if (!iterator.hasNext()) {
                    return;
                }
                toStop = iterator.next().intValue();
                this.mActiveModels.remove(java.lang.Integer.valueOf(toStop));
            }
            this.mDelegate.stopRecognition(toStop);
            final com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.LoadedModel model = this.mLoadedModels.get(java.lang.Integer.valueOf(toStop));
            this.mCallbackThread.push(new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.notifyAbort(toStop, model);
                }
            });
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int loadSoundModel(android.media.soundtrigger.SoundModel soundModel, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback callback) {
        int handle = this.mDelegate.loadSoundModel(soundModel, new com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.CallbackWrapper(callback));
        this.mLoadedModels.put(java.lang.Integer.valueOf(handle), new com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.LoadedModel(1, callback));
        return handle;
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int loadPhraseSoundModel(android.media.soundtrigger.PhraseSoundModel soundModel, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback callback) {
        int handle = this.mDelegate.loadPhraseSoundModel(soundModel, new com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.CallbackWrapper(callback));
        this.mLoadedModels.put(java.lang.Integer.valueOf(handle), new com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.LoadedModel(0, callback));
        return handle;
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void unloadSoundModel(int modelHandle) {
        this.mLoadedModels.remove(java.lang.Integer.valueOf(modelHandle));
        this.mDelegate.unloadSoundModel(modelHandle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerCallback$1(com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback callback) {
        com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.CallbackThread callbackThread = this.mCallbackThread;
        java.util.Objects.requireNonNull(callback);
        callbackThread.push(new com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler$$ExternalSyntheticLambda0(callback));
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void registerCallback(final com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback callback) {
        this.mGlobalCallback = new com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler$$ExternalSyntheticLambda1
            @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback
            public final void onResourcesAvailable() {
                this.f$0.lambda$registerCallback$1(callback);
            }
        };
        this.mDelegate.registerCallback(this.mGlobalCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$linkToDeath$2(final android.os.IBinder.DeathRecipient recipient) {
        com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.CallbackThread callbackThread = this.mCallbackThread;
        java.util.Objects.requireNonNull(recipient);
        callbackThread.push(new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                recipient.binderDied();
            }
        });
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void linkToDeath(final android.os.IBinder.DeathRecipient recipient) {
        android.os.IBinder.DeathRecipient wrapper = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler$$ExternalSyntheticLambda3
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                this.f$0.lambda$linkToDeath$2(recipient);
            }
        };
        this.mDelegate.linkToDeath(wrapper);
        this.mDeathRecipientMap.put(recipient, wrapper);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void unlinkToDeath(android.os.IBinder.DeathRecipient recipient) {
        this.mDelegate.unlinkToDeath(this.mDeathRecipientMap.remove(recipient));
    }

    /* JADX INFO: Access modifiers changed from: private */
    class CallbackWrapper implements com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback {
        private final com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback mDelegateCallback;

        private CallbackWrapper(com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback delegateCallback) {
            this.mDelegateCallback = delegateCallback;
        }

        @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback
        public void recognitionCallback(final int modelHandle, final android.media.soundtrigger_middleware.RecognitionEventSys event) {
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.this.mActiveModels) {
                if (com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.this.mActiveModels.contains(java.lang.Integer.valueOf(modelHandle))) {
                    if (!event.recognitionEvent.recognitionStillActive) {
                        com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.this.mActiveModels.remove(java.lang.Integer.valueOf(modelHandle));
                    }
                    com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.this.mCallbackThread.push(new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler$CallbackWrapper$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$recognitionCallback$0(modelHandle, event);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$recognitionCallback$0(int modelHandle, android.media.soundtrigger_middleware.RecognitionEventSys event) {
            this.mDelegateCallback.recognitionCallback(modelHandle, event);
        }

        @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback
        public void phraseRecognitionCallback(final int modelHandle, final android.media.soundtrigger_middleware.PhraseRecognitionEventSys event) {
            synchronized (com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.this.mActiveModels) {
                if (com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.this.mActiveModels.contains(java.lang.Integer.valueOf(modelHandle))) {
                    if (android.os.Build.isMtkPlatform()) {
                        int eventDataLength = 0;
                        if (event.phraseRecognitionEvent.common.data != null) {
                            eventDataLength = event.phraseRecognitionEvent.common.data.length;
                        }
                        if (!event.phraseRecognitionEvent.common.recognitionStillActive && (eventDataLength == 0 || event.phraseRecognitionEvent.common.data[0] != 1)) {
                            com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.this.mActiveModels.remove(java.lang.Integer.valueOf(modelHandle));
                        }
                    } else if (!event.phraseRecognitionEvent.common.recognitionStillActive) {
                        com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.this.mActiveModels.remove(java.lang.Integer.valueOf(modelHandle));
                    }
                    com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.this.mCallbackThread.push(new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler$CallbackWrapper$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$phraseRecognitionCallback$1(modelHandle, event);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$phraseRecognitionCallback$1(int modelHandle, android.media.soundtrigger_middleware.PhraseRecognitionEventSys event) {
            this.mDelegateCallback.phraseRecognitionCallback(modelHandle, event);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$modelUnloaded$2(int modelHandle) {
            this.mDelegateCallback.modelUnloaded(modelHandle);
        }

        @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback
        public void modelUnloaded(final int modelHandle) {
            com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.this.mCallbackThread.push(new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler$CallbackWrapper$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$modelUnloaded$2(modelHandle);
                }
            });
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void flushCallbacks() {
        this.mDelegate.flushCallbacks();
        this.mCallbackThread.flush();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void clientAttached(android.os.IBinder binder) {
        this.mDelegate.clientAttached(binder);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void clientDetached(android.os.IBinder binder) {
        this.mDelegate.clientDetached(binder);
    }

    private static class CallbackThread implements java.lang.Runnable {
        private static final java.lang.String TAG = "SoundTriggerMiddlewareCallbackThread";
        private final java.util.Queue<java.lang.Runnable> mList = new java.util.LinkedList();
        private final java.util.concurrent.locks.ReentrantLock mRtLock = new java.util.concurrent.locks.ReentrantLock();
        private final java.util.concurrent.locks.Condition mCondition = this.mRtLock.newCondition();
        private int mPushCount = 0;
        private int mProcessedCount = 0;
        private boolean mQuitting = false;
        private final java.lang.Thread mThread = new java.lang.Thread(this, "STHAL Concurrent Capture Handler Callback");

        CallbackThread() {
            this.mThread.start();
        }

        @Override // java.lang.Runnable
        public void run() {
            while (true) {
                try {
                    java.lang.Runnable toRun = pop();
                    if (toRun == null) {
                        return;
                    }
                    toRun.run();
                    this.mRtLock.lock();
                    try {
                        this.mProcessedCount++;
                        this.mCondition.signalAll();
                        this.mRtLock.unlock();
                    } finally {
                    }
                } catch (java.lang.InterruptedException e) {
                    return;
                }
            }
        }

        boolean push(java.lang.Runnable runnable) {
            this.mRtLock.lock();
            try {
                if (!this.mQuitting) {
                    this.mList.add(runnable);
                    this.mPushCount++;
                    this.mCondition.signalAll();
                    return true;
                }
                this.mRtLock.unlock();
                return false;
            } finally {
                this.mRtLock.unlock();
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:8:0x0017, code lost:
        
            android.util.Log.w(com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.CallbackThread.TAG, "Wait for flush timed out.");
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        void flush() {
            /*
                r5 = this;
                java.util.concurrent.locks.ReentrantLock r0 = r5.mRtLock
                r0.lock()
                int r0 = r5.mPushCount     // Catch: java.lang.Throwable -> L1f java.lang.InterruptedException -> L26
            L7:
                int r1 = r5.mProcessedCount     // Catch: java.lang.Throwable -> L1f java.lang.InterruptedException -> L26
                if (r1 == r0) goto L27
                java.util.concurrent.locks.Condition r1 = r5.mCondition     // Catch: java.lang.Throwable -> L1f java.lang.InterruptedException -> L26
                java.util.concurrent.TimeUnit r2 = java.util.concurrent.TimeUnit.SECONDS     // Catch: java.lang.Throwable -> L1f java.lang.InterruptedException -> L26
                r3 = 1
                boolean r1 = r1.await(r3, r2)     // Catch: java.lang.Throwable -> L1f java.lang.InterruptedException -> L26
                if (r1 != 0) goto L7
                java.lang.String r1 = "SoundTriggerMiddlewareCallbackThread"
                java.lang.String r2 = "Wait for flush timed out."
                android.util.Log.w(r1, r2)     // Catch: java.lang.Throwable -> L1f java.lang.InterruptedException -> L26
                goto L27
            L1f:
                r0 = move-exception
                java.util.concurrent.locks.ReentrantLock r1 = r5.mRtLock
                r1.unlock()
                throw r0
            L26:
                r0 = move-exception
            L27:
                java.util.concurrent.locks.ReentrantLock r0 = r5.mRtLock
                r0.unlock()
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.CallbackThread.flush():void");
        }

        void quit() {
            this.mRtLock.lock();
            try {
                this.mQuitting = true;
                this.mCondition.signalAll();
            } finally {
                this.mRtLock.unlock();
            }
        }

        private java.lang.Runnable pop() throws java.lang.InterruptedException {
            this.mRtLock.lock();
            while (this.mList.isEmpty() && !this.mQuitting) {
                try {
                    this.mCondition.await();
                } finally {
                    this.mRtLock.unlock();
                }
            }
            if (!this.mList.isEmpty() || !this.mQuitting) {
                return this.mList.remove();
            }
            this.mRtLock.unlock();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyAbort(int modelHandle, com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.LoadedModel model) {
        switch (model.type) {
            case 0:
                model.callback.phraseRecognitionCallback(modelHandle, com.android.server.soundtrigger_middleware.AidlUtil.newAbortPhraseEvent());
                break;
            case 1:
                model.callback.recognitionCallback(modelHandle, com.android.server.soundtrigger_middleware.AidlUtil.newAbortEvent());
                break;
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void detach() {
        this.mDelegate.detach();
        this.mNotifier.unregisterListener(this);
        this.mCallbackThread.quit();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void reboot() {
        this.mDelegate.reboot();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public android.media.soundtrigger.Properties getProperties() {
        return this.mDelegate.getProperties();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void forceRecognitionEvent(int modelHandle) {
        this.mDelegate.forceRecognitionEvent(modelHandle);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int getModelParameter(int modelHandle, int param) {
        return this.mDelegate.getModelParameter(modelHandle, param);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void setModelParameter(int modelHandle, int param, int value) {
        this.mDelegate.setModelParameter(modelHandle, param, value);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public android.media.soundtrigger.ModelParameterRange queryParameter(int modelHandle, int param) {
        return this.mDelegate.queryParameter(modelHandle, param);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public java.lang.String interfaceDescriptor() {
        return this.mDelegate.interfaceDescriptor();
    }
}
