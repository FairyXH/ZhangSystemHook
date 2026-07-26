package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public interface ISoundDoseHelperWrapper {
    default int getSafeMediaVolumeState() {
        return 0;
    }

    default void setSafeMediaVolumeState(int active) {
    }

    default void setMusicActiveMs(int active) {
    }

    default void saveMusicActiveMs() {
    }

    default void checkMusicActive() {
    }
}
