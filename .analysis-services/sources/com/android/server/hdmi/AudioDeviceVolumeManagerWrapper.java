package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public interface AudioDeviceVolumeManagerWrapper {
    void addOnDeviceVolumeBehaviorChangedListener(java.util.concurrent.Executor executor, android.media.AudioDeviceVolumeManager.OnDeviceVolumeBehaviorChangedListener onDeviceVolumeBehaviorChangedListener);

    void removeOnDeviceVolumeBehaviorChangedListener(android.media.AudioDeviceVolumeManager.OnDeviceVolumeBehaviorChangedListener onDeviceVolumeBehaviorChangedListener);

    void setDeviceAbsoluteVolumeAdjustOnlyBehavior(android.media.AudioDeviceAttributes audioDeviceAttributes, android.media.VolumeInfo volumeInfo, java.util.concurrent.Executor executor, android.media.AudioDeviceVolumeManager.OnAudioDeviceVolumeChangedListener onAudioDeviceVolumeChangedListener, boolean z);

    void setDeviceAbsoluteVolumeBehavior(android.media.AudioDeviceAttributes audioDeviceAttributes, android.media.VolumeInfo volumeInfo, java.util.concurrent.Executor executor, android.media.AudioDeviceVolumeManager.OnAudioDeviceVolumeChangedListener onAudioDeviceVolumeChangedListener, boolean z);
}
