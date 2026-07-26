package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public class SoundTriggerDuplicateModelHandler implements com.android.server.soundtrigger_middleware.ISoundTriggerHal {
    private final com.android.server.soundtrigger_middleware.ISoundTriggerHal mDelegate;
    private com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback mGlobalCallback;
    private final java.util.List<com.android.server.soundtrigger_middleware.SoundTriggerDuplicateModelHandler.ModelData> mModelList = new java.util.ArrayList();

    /* JADX INFO: Access modifiers changed from: private */
    static final class ModelData {
        private int mModelId;
        private java.lang.String mUuid;
        private boolean mWasContended = false;

        ModelData(int modelId, java.lang.String uuid) {
            this.mModelId = modelId;
            this.mUuid = uuid;
        }

        int getModelId() {
            return this.mModelId;
        }

        java.lang.String getUuid() {
            return this.mUuid;
        }

        boolean getWasContended() {
            return this.mWasContended;
        }

        void setWasContended() {
            this.mWasContended = true;
        }
    }

    public SoundTriggerDuplicateModelHandler(com.android.server.soundtrigger_middleware.ISoundTriggerHal delegate) {
        this.mDelegate = delegate;
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
            checkDuplicateModelUuid(soundModel.uuid);
            result = this.mDelegate.loadSoundModel(soundModel, callback);
            this.mModelList.add(new com.android.server.soundtrigger_middleware.SoundTriggerDuplicateModelHandler.ModelData(result, soundModel.uuid));
        }
        return result;
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int loadPhraseSoundModel(android.media.soundtrigger.PhraseSoundModel soundModel, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback callback) {
        int result;
        synchronized (this) {
            checkDuplicateModelUuid(soundModel.common.uuid);
            result = this.mDelegate.loadPhraseSoundModel(soundModel, callback);
            this.mModelList.add(new com.android.server.soundtrigger_middleware.SoundTriggerDuplicateModelHandler.ModelData(result, soundModel.common.uuid));
        }
        return result;
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void unloadSoundModel(int modelHandle) {
        this.mDelegate.unloadSoundModel(modelHandle);
        for (int i = 0; i < this.mModelList.size(); i++) {
            if (this.mModelList.get(i).getModelId() == modelHandle) {
                com.android.server.soundtrigger_middleware.SoundTriggerDuplicateModelHandler.ModelData modelData = this.mModelList.remove(i);
                if (modelData.getWasContended()) {
                    this.mGlobalCallback.onResourcesAvailable();
                    return;
                }
                return;
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

    private void checkDuplicateModelUuid(final java.lang.String uuid) {
        java.util.Optional<com.android.server.soundtrigger_middleware.SoundTriggerDuplicateModelHandler.ModelData> model = this.mModelList.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerDuplicateModelHandler$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.soundtrigger_middleware.SoundTriggerDuplicateModelHandler.ModelData) obj).getUuid().equals(uuid);
            }
        }).findFirst();
        if (model.isPresent()) {
            model.get().setWasContended();
            throw new com.android.server.soundtrigger_middleware.RecoverableException(1);
        }
    }
}
