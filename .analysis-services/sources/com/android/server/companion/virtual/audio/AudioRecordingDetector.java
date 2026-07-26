package com.android.server.companion.virtual.audio;

/* JADX INFO: loaded from: classes.dex */
final class AudioRecordingDetector extends android.media.AudioManager.AudioRecordingCallback {
    private final android.media.AudioManager mAudioManager;
    private com.android.server.companion.virtual.audio.AudioRecordingDetector.AudioRecordingCallback mAudioRecordingCallback;

    interface AudioRecordingCallback {
        void onRecordingConfigChanged(java.util.List<android.media.AudioRecordingConfiguration> list);
    }

    AudioRecordingDetector(android.content.Context context) {
        this.mAudioManager = (android.media.AudioManager) context.getSystemService(android.media.AudioManager.class);
    }

    void register(com.android.server.companion.virtual.audio.AudioRecordingDetector.AudioRecordingCallback callback) {
        this.mAudioRecordingCallback = callback;
        this.mAudioManager.registerAudioRecordingCallback(this, null);
    }

    void unregister() {
        if (this.mAudioRecordingCallback != null) {
            this.mAudioRecordingCallback = null;
            this.mAudioManager.unregisterAudioRecordingCallback(this);
        }
    }

    @Override // android.media.AudioManager.AudioRecordingCallback
    public void onRecordingConfigChanged(java.util.List<android.media.AudioRecordingConfiguration> configs) {
        super.onRecordingConfigChanged(configs);
        if (this.mAudioRecordingCallback != null) {
            this.mAudioRecordingCallback.onRecordingConfigChanged(configs);
        }
    }
}
