package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface SoundTriggerInternal {
    public static final int STATUS_ERROR = Integer.MIN_VALUE;
    public static final int STATUS_OK = 0;

    public interface Session {
        void detach();

        android.hardware.soundtrigger.SoundTrigger.ModuleProperties getModuleProperties();

        int getParameter(int i, int i2);

        android.hardware.soundtrigger.SoundTrigger.ModelParamRange queryParameter(int i, int i2);

        int setParameter(int i, int i2, int i3);

        int startRecognition(int i, android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel keyphraseSoundModel, android.hardware.soundtrigger.IRecognitionStatusCallback iRecognitionStatusCallback, android.hardware.soundtrigger.SoundTrigger.RecognitionConfig recognitionConfig, boolean z);

        int stopRecognition(int i, android.hardware.soundtrigger.IRecognitionStatusCallback iRecognitionStatusCallback);

        int unloadKeyphraseModel(int i);
    }

    com.android.server.SoundTriggerInternal.Session attach(android.os.IBinder iBinder, android.hardware.soundtrigger.SoundTrigger.ModuleProperties moduleProperties, boolean z);

    java.util.List<android.hardware.soundtrigger.SoundTrigger.ModuleProperties> listModuleProperties(android.media.permission.Identity identity);
}
