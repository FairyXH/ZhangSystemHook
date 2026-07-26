package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
final class AudioRoutingUtils {
    static final android.media.AudioAttributes ATTRIBUTES_MEDIA = new android.media.AudioAttributes.Builder().setUsage(1).build();

    static android.media.audiopolicy.AudioProductStrategy getMediaAudioProductStrategy() {
        for (android.media.audiopolicy.AudioProductStrategy strategy : android.media.AudioManager.getAudioProductStrategies()) {
            if (strategy.supportsAudioAttributes(ATTRIBUTES_MEDIA)) {
                return strategy;
            }
        }
        return null;
    }

    private AudioRoutingUtils() {
    }
}
