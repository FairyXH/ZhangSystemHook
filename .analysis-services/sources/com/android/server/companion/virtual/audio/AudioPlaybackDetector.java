package com.android.server.companion.virtual.audio;

/* JADX INFO: loaded from: classes.dex */
final class AudioPlaybackDetector extends android.media.AudioManager.AudioPlaybackCallback {
    private final android.media.AudioManager mAudioManager;
    private com.android.server.companion.virtual.audio.AudioPlaybackDetector.AudioPlaybackCallback mAudioPlaybackCallback;

    interface AudioPlaybackCallback {
        void onPlaybackConfigChanged(java.util.List<android.media.AudioPlaybackConfiguration> list);
    }

    AudioPlaybackDetector(android.content.Context context) {
        this.mAudioManager = (android.media.AudioManager) context.getSystemService(android.media.AudioManager.class);
    }

    void register(com.android.server.companion.virtual.audio.AudioPlaybackDetector.AudioPlaybackCallback callback) {
        this.mAudioPlaybackCallback = callback;
        this.mAudioManager.registerAudioPlaybackCallback(this, null);
    }

    void unregister() {
        if (this.mAudioPlaybackCallback != null) {
            this.mAudioPlaybackCallback = null;
            this.mAudioManager.unregisterAudioPlaybackCallback(this);
        }
    }

    @Override // android.media.AudioManager.AudioPlaybackCallback
    public void onPlaybackConfigChanged(java.util.List<android.media.AudioPlaybackConfiguration> configs) {
        super.onPlaybackConfigChanged(configs);
        if (this.mAudioPlaybackCallback != null) {
            this.mAudioPlaybackCallback.onPlaybackConfigChanged(configs);
        }
    }
}
