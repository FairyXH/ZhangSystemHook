package com.android.server.voiceinteraction;

/* JADX INFO: loaded from: classes3.dex */
public interface IEnrolledModelDb {
    boolean deleteKeyphraseSoundModel(int i, int i2, java.lang.String str);

    void dump(java.io.PrintWriter printWriter);

    android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(int i, int i2, java.lang.String str);

    android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(java.lang.String str, int i, java.lang.String str2);

    boolean updateKeyphraseSoundModel(android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel keyphraseSoundModel);
}
