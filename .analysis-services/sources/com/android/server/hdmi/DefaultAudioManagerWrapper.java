package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class DefaultAudioManagerWrapper implements com.android.server.hdmi.AudioManagerWrapper {
    private static final java.lang.String TAG = "DefaultAudioManagerWrapper";
    private final android.media.AudioManager mAudioManager;

    public DefaultAudioManagerWrapper(android.content.Context context) {
        this.mAudioManager = (android.media.AudioManager) context.getSystemService("audio");
    }

    @Override // com.android.server.hdmi.AudioManagerWrapper
    public void adjustStreamVolume(int streamType, int direction, int flags) {
        this.mAudioManager.adjustStreamVolume(streamType, direction, flags);
    }

    @Override // com.android.server.hdmi.AudioManagerWrapper
    public void setStreamVolume(int streamType, int index, int flags) {
        this.mAudioManager.setStreamVolume(streamType, index, flags);
    }

    @Override // com.android.server.hdmi.AudioManagerWrapper
    public int getStreamVolume(int streamType) {
        return this.mAudioManager.getStreamVolume(streamType);
    }

    @Override // com.android.server.hdmi.AudioManagerWrapper
    public int getStreamMinVolume(int streamType) {
        return this.mAudioManager.getStreamMinVolume(streamType);
    }

    @Override // com.android.server.hdmi.AudioManagerWrapper
    public int getStreamMaxVolume(int streamType) {
        return this.mAudioManager.getStreamMaxVolume(streamType);
    }

    @Override // com.android.server.hdmi.AudioManagerWrapper
    public boolean isStreamMute(int streamType) {
        return this.mAudioManager.isStreamMute(streamType);
    }

    @Override // com.android.server.hdmi.AudioManagerWrapper
    public void setStreamMute(int streamType, boolean state) {
        this.mAudioManager.setStreamMute(streamType, state);
    }

    @Override // com.android.server.hdmi.AudioManagerWrapper
    public int setHdmiSystemAudioSupported(boolean on) {
        return this.mAudioManager.setHdmiSystemAudioSupported(on);
    }

    @Override // com.android.server.hdmi.AudioManagerWrapper
    public void setWiredDeviceConnectionState(android.media.AudioDeviceAttributes attributes, int state) {
        this.mAudioManager.setWiredDeviceConnectionState(attributes, state);
    }

    @Override // com.android.server.hdmi.AudioManagerWrapper
    public void setWiredDeviceConnectionState(int device, int state, java.lang.String address, java.lang.String name) {
        this.mAudioManager.setWiredDeviceConnectionState(device, state, address, name);
    }

    @Override // com.android.server.hdmi.AudioManagerWrapper
    public int getDeviceVolumeBehavior(android.media.AudioDeviceAttributes device) {
        return this.mAudioManager.getDeviceVolumeBehavior(device);
    }

    @Override // com.android.server.hdmi.AudioManagerWrapper
    public void setDeviceVolumeBehavior(android.media.AudioDeviceAttributes device, int deviceVolumeBehavior) {
        this.mAudioManager.setDeviceVolumeBehavior(device, deviceVolumeBehavior);
    }

    @Override // com.android.server.hdmi.AudioManagerWrapper
    public java.util.List<android.media.AudioDeviceAttributes> getDevicesForAttributes(android.media.AudioAttributes attributes) {
        return this.mAudioManager.getDevicesForAttributes(attributes);
    }
}
