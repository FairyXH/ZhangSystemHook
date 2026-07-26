package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public interface IPlaybackActivityMonitorExt {
    default void setVolumeForUid(float gain, int uid, java.lang.String paramString, boolean isEternalSet) {
    }

    default void updatePlayerVolumeByApc(android.media.AudioPlaybackConfiguration paramApc, android.content.Context paramContext) {
    }

    default void setExAudioFocusState(boolean isExAudioFocusState) {
    }

    default boolean getExAudioFocusState() {
        return false;
    }

    default boolean isInMusicVolumeMap(android.media.AudioPlaybackConfiguration paramApc, android.content.Context paramContext) {
        return false;
    }

    default void resetTrackVolumeForAll() {
    }

    default void updateAppVolumeMaptoAudioServer() {
    }

    default boolean isNeedReUnmute(int pid) {
        return false;
    }
}
