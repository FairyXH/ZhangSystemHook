package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class DefaultAudioDeviceVolumeManagerWrapper implements com.android.server.hdmi.AudioDeviceVolumeManagerWrapper {
    private static final java.lang.String TAG = "AudioDeviceVolumeManagerWrapper";
    private final android.media.AudioDeviceVolumeManager mAudioDeviceVolumeManager;

    public DefaultAudioDeviceVolumeManagerWrapper(android.content.Context context) {
        this.mAudioDeviceVolumeManager = new android.media.AudioDeviceVolumeManager(context);
    }

    @Override // com.android.server.hdmi.AudioDeviceVolumeManagerWrapper
    public void addOnDeviceVolumeBehaviorChangedListener(java.util.concurrent.Executor executor, android.media.AudioDeviceVolumeManager.OnDeviceVolumeBehaviorChangedListener listener) throws java.lang.SecurityException {
        this.mAudioDeviceVolumeManager.addOnDeviceVolumeBehaviorChangedListener(executor, listener);
    }

    @Override // com.android.server.hdmi.AudioDeviceVolumeManagerWrapper
    public void removeOnDeviceVolumeBehaviorChangedListener(android.media.AudioDeviceVolumeManager.OnDeviceVolumeBehaviorChangedListener listener) {
        this.mAudioDeviceVolumeManager.removeOnDeviceVolumeBehaviorChangedListener(listener);
    }

    @Override // com.android.server.hdmi.AudioDeviceVolumeManagerWrapper
    public void setDeviceAbsoluteVolumeBehavior(android.media.AudioDeviceAttributes device, android.media.VolumeInfo volume, java.util.concurrent.Executor executor, android.media.AudioDeviceVolumeManager.OnAudioDeviceVolumeChangedListener vclistener, boolean handlesVolumeAdjustment) {
        this.mAudioDeviceVolumeManager.setDeviceAbsoluteVolumeBehavior(device, volume, executor, vclistener, handlesVolumeAdjustment);
    }

    @Override // com.android.server.hdmi.AudioDeviceVolumeManagerWrapper
    public void setDeviceAbsoluteVolumeAdjustOnlyBehavior(android.media.AudioDeviceAttributes device, android.media.VolumeInfo volume, java.util.concurrent.Executor executor, android.media.AudioDeviceVolumeManager.OnAudioDeviceVolumeChangedListener vclistener, boolean handlesVolumeAdjustment) {
        this.mAudioDeviceVolumeManager.setDeviceAbsoluteVolumeAdjustOnlyBehavior(device, volume, executor, vclistener, handlesVolumeAdjustment);
    }
}
