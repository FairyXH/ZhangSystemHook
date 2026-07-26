package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public interface IPlaybackActivityMonitorWrapper {
    default com.android.server.audio.IPlaybackActivityMonitorExt getExtImpl() {
        return new com.android.server.audio.IPlaybackActivityMonitorExt() { // from class: com.android.server.audio.IPlaybackActivityMonitorWrapper.1
        };
    }

    default java.lang.Object getPlayerLock() {
        return null;
    }

    default java.util.HashMap<java.lang.Integer, android.media.AudioPlaybackConfiguration> getPlayers() {
        return null;
    }

    default android.content.Context getContext() {
        return null;
    }
}
