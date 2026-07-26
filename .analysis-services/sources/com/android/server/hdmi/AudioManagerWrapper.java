package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public interface AudioManagerWrapper {
    void adjustStreamVolume(int i, int i2, int i3);

    int getDeviceVolumeBehavior(android.media.AudioDeviceAttributes audioDeviceAttributes);

    java.util.List<android.media.AudioDeviceAttributes> getDevicesForAttributes(android.media.AudioAttributes audioAttributes);

    int getStreamMaxVolume(int i);

    int getStreamMinVolume(int i);

    int getStreamVolume(int i);

    boolean isStreamMute(int i);

    void setDeviceVolumeBehavior(android.media.AudioDeviceAttributes audioDeviceAttributes, int i);

    int setHdmiSystemAudioSupported(boolean z);

    void setStreamMute(int i, boolean z);

    void setStreamVolume(int i, int i2, int i3);

    void setWiredDeviceConnectionState(int i, int i2, java.lang.String str, java.lang.String str2);

    void setWiredDeviceConnectionState(android.media.AudioDeviceAttributes audioDeviceAttributes, int i);
}
