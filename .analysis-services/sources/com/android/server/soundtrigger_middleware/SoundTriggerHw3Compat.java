package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public class SoundTriggerHw3Compat implements com.android.server.soundtrigger_middleware.ISoundTriggerHal {
    private final android.hardware.soundtrigger3.ISoundTriggerHw mDriver;
    private final java.lang.Runnable mRebootRunnable;

    public SoundTriggerHw3Compat(android.os.IBinder binder, java.lang.Runnable rebootRunnable) {
        this.mDriver = android.hardware.soundtrigger3.ISoundTriggerHw.Stub.asInterface(binder);
        this.mRebootRunnable = rebootRunnable;
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public android.media.soundtrigger.Properties getProperties() {
        try {
            return this.mDriver.getProperties();
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void registerCallback(com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback callback) {
        try {
            this.mDriver.registerGlobalCallback(new com.android.server.soundtrigger_middleware.SoundTriggerHw3Compat.GlobalCallbackAdaper(callback));
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int loadSoundModel(android.media.soundtrigger.SoundModel soundModel, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback callback) throws android.os.ServiceSpecificException {
        try {
            return this.mDriver.loadSoundModel(soundModel, new com.android.server.soundtrigger_middleware.SoundTriggerHw3Compat.ModelCallbackAdaper(callback));
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        } catch (android.os.ServiceSpecificException e2) {
            if (e2.errorCode == 1) {
                throw new com.android.server.soundtrigger_middleware.RecoverableException(1);
            }
            throw e2;
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int loadPhraseSoundModel(android.media.soundtrigger.PhraseSoundModel soundModel, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback callback) throws android.os.ServiceSpecificException {
        try {
            return this.mDriver.loadPhraseSoundModel(soundModel, new com.android.server.soundtrigger_middleware.SoundTriggerHw3Compat.ModelCallbackAdaper(callback));
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        } catch (android.os.ServiceSpecificException e2) {
            if (e2.errorCode == 1) {
                throw new com.android.server.soundtrigger_middleware.RecoverableException(1);
            }
            throw e2;
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void unloadSoundModel(int modelHandle) {
        try {
            this.mDriver.unloadSoundModel(modelHandle);
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void startRecognition(int modelHandle, int deviceHandle, int ioHandle, android.media.soundtrigger.RecognitionConfig config) throws android.os.ServiceSpecificException {
        try {
            this.mDriver.startRecognition(modelHandle, deviceHandle, ioHandle, config);
        } catch (android.os.ServiceSpecificException e) {
            if (e.errorCode == 1) {
                throw new com.android.server.soundtrigger_middleware.RecoverableException(1);
            }
            throw e;
        } catch (android.os.RemoteException e2) {
            throw e2.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void stopRecognition(int modelHandle) {
        try {
            this.mDriver.stopRecognition(modelHandle);
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void forceRecognitionEvent(int modelHandle) {
        try {
            this.mDriver.forceRecognitionEvent(modelHandle);
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public android.media.soundtrigger.ModelParameterRange queryParameter(int modelHandle, int param) {
        try {
            return this.mDriver.queryParameter(modelHandle, param);
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int getModelParameter(int modelHandle, int param) {
        try {
            return this.mDriver.getParameter(modelHandle, param);
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void setModelParameter(int modelHandle, int param, int value) {
        try {
            this.mDriver.setParameter(modelHandle, param, value);
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public java.lang.String interfaceDescriptor() {
        try {
            return this.mDriver.asBinder().getInterfaceDescriptor();
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void linkToDeath(android.os.IBinder.DeathRecipient recipient) {
        try {
            this.mDriver.asBinder().linkToDeath(recipient, 0);
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void unlinkToDeath(android.os.IBinder.DeathRecipient recipient) {
        this.mDriver.asBinder().unlinkToDeath(recipient, 0);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void flushCallbacks() {
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void clientAttached(android.os.IBinder binder) {
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void clientDetached(android.os.IBinder binder) {
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void reboot() {
        this.mRebootRunnable.run();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void detach() {
    }

    private static class GlobalCallbackAdaper extends android.hardware.soundtrigger3.ISoundTriggerHwGlobalCallback.Stub {
        private final com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback mDelegate;

        public GlobalCallbackAdaper(com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback callback) {
            this.mDelegate = callback;
        }

        public void onResourcesAvailable() {
            java.util.concurrent.Executor executor = com.android.server.FgThread.getExecutor();
            com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback globalCallback = this.mDelegate;
            java.util.Objects.requireNonNull(globalCallback);
            executor.execute(new com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler$$ExternalSyntheticLambda0(globalCallback));
        }

        public int getInterfaceVersion() {
            return 2;
        }

        public java.lang.String getInterfaceHash() {
            return "6b24e60ad261e3ff56106efd86ce6aa7ef5621b0";
        }
    }

    private static class ModelCallbackAdaper extends android.hardware.soundtrigger3.ISoundTriggerHwCallback.Stub {
        private final com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback mDelegate;

        public ModelCallbackAdaper(com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback callback) {
            this.mDelegate = callback;
        }

        public void modelUnloaded(int model) {
            this.mDelegate.modelUnloaded(model);
        }

        public void phraseRecognitionCallback(int model, android.media.soundtrigger.PhraseRecognitionEvent event) {
            event.common.recognitionStillActive |= event.common.status == 3;
            android.media.soundtrigger_middleware.PhraseRecognitionEventSys phraseRecognitionEventSys = new android.media.soundtrigger_middleware.PhraseRecognitionEventSys();
            phraseRecognitionEventSys.phraseRecognitionEvent = event;
            phraseRecognitionEventSys.halEventReceivedMillis = android.os.SystemClock.elapsedRealtimeNanos();
            this.mDelegate.phraseRecognitionCallback(model, phraseRecognitionEventSys);
        }

        public void recognitionCallback(int model, android.media.soundtrigger.RecognitionEvent event) {
            event.recognitionStillActive |= event.status == 3;
            android.media.soundtrigger_middleware.RecognitionEventSys recognitionEventSys = new android.media.soundtrigger_middleware.RecognitionEventSys();
            recognitionEventSys.recognitionEvent = event;
            recognitionEventSys.halEventReceivedMillis = android.os.SystemClock.elapsedRealtimeNanos();
            this.mDelegate.recognitionCallback(model, recognitionEventSys);
        }

        public int getInterfaceVersion() {
            return 2;
        }

        public java.lang.String getInterfaceHash() {
            return "6b24e60ad261e3ff56106efd86ce6aa7ef5621b0";
        }
    }
}
