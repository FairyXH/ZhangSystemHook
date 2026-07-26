package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
interface ISoundTriggerHal {

    public interface GlobalCallback {
        void onResourcesAvailable();
    }

    public interface ModelCallback {
        void modelUnloaded(int i);

        void phraseRecognitionCallback(int i, android.media.soundtrigger_middleware.PhraseRecognitionEventSys phraseRecognitionEventSys);

        void recognitionCallback(int i, android.media.soundtrigger_middleware.RecognitionEventSys recognitionEventSys);
    }

    void clientAttached(android.os.IBinder iBinder);

    void clientDetached(android.os.IBinder iBinder);

    void detach();

    void flushCallbacks();

    void forceRecognitionEvent(int i);

    int getModelParameter(int i, int i2);

    android.media.soundtrigger.Properties getProperties();

    java.lang.String interfaceDescriptor();

    void linkToDeath(android.os.IBinder.DeathRecipient deathRecipient);

    int loadPhraseSoundModel(android.media.soundtrigger.PhraseSoundModel phraseSoundModel, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback modelCallback);

    int loadSoundModel(android.media.soundtrigger.SoundModel soundModel, com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback modelCallback);

    android.media.soundtrigger.ModelParameterRange queryParameter(int i, int i2);

    void reboot();

    void registerCallback(com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback globalCallback);

    void setModelParameter(int i, int i2, int i3);

    void startRecognition(int i, int i2, int i3, android.media.soundtrigger.RecognitionConfig recognitionConfig);

    void stopRecognition(int i);

    void unlinkToDeath(android.os.IBinder.DeathRecipient deathRecipient);

    void unloadSoundModel(int i);
}
