package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class SetAudioVolumeLevelMessage extends com.android.server.hdmi.HdmiCecMessage {
    private final int mAudioVolumeLevel;

    private SetAudioVolumeLevelMessage(int source, int destination, byte[] params, int audioVolumeLevel) {
        super(source, destination, 115, params, 0);
        this.mAudioVolumeLevel = audioVolumeLevel;
    }

    public static com.android.server.hdmi.HdmiCecMessage build(int source, int destination, int audioVolumeLevel) {
        byte[] params = {(byte) (audioVolumeLevel & 255)};
        int addressValidationResult = validateAddress(source, destination);
        if (addressValidationResult == 0) {
            return new com.android.server.hdmi.SetAudioVolumeLevelMessage(source, destination, params, audioVolumeLevel);
        }
        return new com.android.server.hdmi.HdmiCecMessage(source, destination, 115, params, addressValidationResult);
    }

    public static com.android.server.hdmi.HdmiCecMessage build(int source, int destination, byte[] params) {
        if (params.length == 0) {
            return new com.android.server.hdmi.HdmiCecMessage(source, destination, 115, params, 4);
        }
        int audioVolumeLevel = params[0];
        int addressValidationResult = validateAddress(source, destination);
        if (addressValidationResult == 0) {
            return new com.android.server.hdmi.SetAudioVolumeLevelMessage(source, destination, params, audioVolumeLevel);
        }
        return new com.android.server.hdmi.HdmiCecMessage(source, destination, 115, params, addressValidationResult);
    }

    public static int validateAddress(int source, int destination) {
        return com.android.server.hdmi.HdmiCecMessageValidator.validateAddress(source, destination, 32767, 32767);
    }

    public int getAudioVolumeLevel() {
        return this.mAudioVolumeLevel;
    }
}
