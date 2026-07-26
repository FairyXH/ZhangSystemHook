package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public class SoundTriggerHalMaxModelLimiter implements com.android.server.soundtrigger_middleware.ISoundTriggerHal {
    private final com.android.server.soundtrigger_middleware.ISoundTriggerHal mDelegate;
    private com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback mGlobalCallback;
    private final int mMaxModels;
    private int mNumLoadedModels = 0;

    public SoundTriggerHalMaxModelLimiter(com.android.server.soundtrigger_middleware.ISoundTriggerHal delegate, int maxModels) {
        this.mDelegate = delegate;
        this.mMaxModels = maxModels;
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void reboot() {
        this.mDelegate.reboot();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void detach() {
        this.mDelegate.detach();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public android.media.soundtrigger.Properties getProperties() {
        return this.mDelegate.getProperties();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void registerCallback(com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback callback) {
        this.mGlobalCallback = callback;
        this.mDelegate.registerCallback(this.mGlobalCallback);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int loadSoundModel(android.media.soundtrigger.SoundModel soundModel, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback callback) {
        int result;
        synchronized (this) {
            if (this.mNumLoadedModels == this.mMaxModels) {
                throw new com.android.server.soundtrigger_middleware.RecoverableException(1);
            }
            result = this.mDelegate.loadSoundModel(soundModel, callback);
            this.mNumLoadedModels++;
        }
        return result;
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int loadPhraseSoundModel(android.media.soundtrigger.PhraseSoundModel soundModel, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback callback) {
        int result;
        synchronized (this) {
            if (this.mNumLoadedModels == this.mMaxModels) {
                throw new com.android.server.soundtrigger_middleware.RecoverableException(1);
            }
            result = this.mDelegate.loadPhraseSoundModel(soundModel, callback);
            this.mNumLoadedModels++;
        }
        return result;
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void unloadSoundModel(int modelHandle) throws java.lang.Exception {
        boolean wasAtMaxCapacity;
        synchronized (this) {
            int i = this.mNumLoadedModels;
            this.mNumLoadedModels = i - 1;
            wasAtMaxCapacity = i == this.mMaxModels;
        }
        try {
            this.mDelegate.unloadSoundModel(modelHandle);
            if (wasAtMaxCapacity) {
                this.mGlobalCallback.onResourcesAvailable();
            }
        } catch (java.lang.Exception e) {
            synchronized (this) {
                this.mNumLoadedModels++;
                throw e;
            }
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void stopRecognition(int modelHandle) {
        this.mDelegate.stopRecognition(modelHandle);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void startRecognition(int modelHandle, int deviceHandle, int ioHandle, android.media.soundtrigger.RecognitionConfig config) {
        this.mDelegate.startRecognition(modelHandle, deviceHandle, ioHandle, config);
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
    public void linkToDeath(android.os.IBinder.DeathRecipient recipient) {
        this.mDelegate.linkToDeath(recipient);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void unlinkToDeath(android.os.IBinder.DeathRecipient recipient) {
        this.mDelegate.unlinkToDeath(recipient);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public java.lang.String interfaceDescriptor() {
        return this.mDelegate.interfaceDescriptor();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void flushCallbacks() {
        this.mDelegate.flushCallbacks();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void clientAttached(android.os.IBinder binder) {
        this.mDelegate.clientAttached(binder);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void clientDetached(android.os.IBinder binder) {
        this.mDelegate.clientDetached(binder);
    }
}
