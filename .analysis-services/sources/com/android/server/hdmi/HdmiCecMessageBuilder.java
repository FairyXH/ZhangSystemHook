package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class HdmiCecMessageBuilder {
    private static final int OSD_NAME_MAX_LENGTH = 14;

    private HdmiCecMessageBuilder() {
    }

    static com.android.server.hdmi.HdmiCecMessage buildFeatureAbortCommand(int src, int dest, int originalOpcode, int reason) {
        byte[] params = {(byte) (originalOpcode & 255), (byte) (reason & 255)};
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 0, params);
    }

    static com.android.server.hdmi.HdmiCecMessage buildGivePhysicalAddress(int src, int dest) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 131);
    }

    static com.android.server.hdmi.HdmiCecMessage buildGiveOsdNameCommand(int src, int dest) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 70);
    }

    static com.android.server.hdmi.HdmiCecMessage buildGiveDeviceVendorIdCommand(int src, int dest) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 140);
    }

    static com.android.server.hdmi.HdmiCecMessage buildSetMenuLanguageCommand(int src, java.lang.String language) {
        if (language.length() != 3) {
            return null;
        }
        java.lang.String normalized = language.toLowerCase();
        byte[] params = {(byte) (normalized.charAt(0) & 255), (byte) (normalized.charAt(1) & 255), (byte) (normalized.charAt(2) & 255)};
        return com.android.server.hdmi.HdmiCecMessage.build(src, 15, 50, params);
    }

    static com.android.server.hdmi.HdmiCecMessage buildSetOsdNameCommand(int src, int dest, java.lang.String name) {
        int length = java.lang.Math.min(name.length(), 14);
        try {
            byte[] params = name.substring(0, length).getBytes("US-ASCII");
            return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 71, params);
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
    }

    static com.android.server.hdmi.HdmiCecMessage buildReportPhysicalAddressCommand(int src, int address, int deviceType) {
        byte[] params = {(byte) ((address >> 8) & 255), (byte) (address & 255), (byte) (deviceType & 255)};
        return com.android.server.hdmi.HdmiCecMessage.build(src, 15, 132, params);
    }

    static com.android.server.hdmi.HdmiCecMessage buildDeviceVendorIdCommand(int src, int vendorId) {
        byte[] params = {(byte) ((vendorId >> 16) & 255), (byte) ((vendorId >> 8) & 255), (byte) (vendorId & 255)};
        return com.android.server.hdmi.HdmiCecMessage.build(src, 15, 135, params);
    }

    static com.android.server.hdmi.HdmiCecMessage buildCecVersion(int src, int dest, int version) {
        byte[] params = {(byte) (version & 255)};
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 158, params);
    }

    static com.android.server.hdmi.HdmiCecMessage buildRequestArcInitiation(int src, int dest) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 195);
    }

    static com.android.server.hdmi.HdmiCecMessage buildInitiateArc(int src, int dest) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 192);
    }

    static com.android.server.hdmi.HdmiCecMessage buildTerminateArc(int src, int dest) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 197);
    }

    static com.android.server.hdmi.HdmiCecMessage buildRequestArcTermination(int src, int dest) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 196);
    }

    static com.android.server.hdmi.HdmiCecMessage buildReportArcInitiated(int src, int dest) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 193);
    }

    static com.android.server.hdmi.HdmiCecMessage buildReportArcTerminated(int src, int dest) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 194);
    }

    static com.android.server.hdmi.HdmiCecMessage buildRequestShortAudioDescriptor(int src, int dest, int[] audioFormats) {
        byte[] params = new byte[java.lang.Math.min(audioFormats.length, 4)];
        for (int i = 0; i < params.length; i++) {
            params[i] = (byte) (audioFormats[i] & 255);
        }
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 164, params);
    }

    static com.android.server.hdmi.HdmiCecMessage buildTextViewOn(int src, int dest) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 13);
    }

    static com.android.server.hdmi.HdmiCecMessage buildImageViewOn(int src, int dest) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 4);
    }

    static com.android.server.hdmi.HdmiCecMessage buildRequestActiveSource(int src) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, 15, 133);
    }

    static com.android.server.hdmi.HdmiCecMessage buildActiveSource(int src, int physicalAddress) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, 15, 130, physicalAddressToParam(physicalAddress));
    }

    static com.android.server.hdmi.HdmiCecMessage buildInactiveSource(int src, int physicalAddress) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, 0, 157, physicalAddressToParam(physicalAddress));
    }

    static com.android.server.hdmi.HdmiCecMessage buildSetStreamPath(int src, int streamPath) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, 15, 134, physicalAddressToParam(streamPath));
    }

    static com.android.server.hdmi.HdmiCecMessage buildRoutingChange(int src, int oldPath, int newPath) {
        byte[] param = {(byte) ((oldPath >> 8) & 255), (byte) (oldPath & 255), (byte) ((newPath >> 8) & 255), (byte) (newPath & 255)};
        return com.android.server.hdmi.HdmiCecMessage.build(src, 15, 128, param);
    }

    static com.android.server.hdmi.HdmiCecMessage buildRoutingInformation(int src, int physicalAddress) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, 15, 129, physicalAddressToParam(physicalAddress));
    }

    static com.android.server.hdmi.HdmiCecMessage buildGiveDevicePowerStatus(int src, int dest) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 143);
    }

    static com.android.server.hdmi.HdmiCecMessage buildReportPowerStatus(int src, int dest, int powerStatus) {
        byte[] param = {(byte) (powerStatus & 255)};
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 144, param);
    }

    static com.android.server.hdmi.HdmiCecMessage buildReportMenuStatus(int src, int dest, int menuStatus) {
        byte[] param = {(byte) (menuStatus & 255)};
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 142, param);
    }

    static com.android.server.hdmi.HdmiCecMessage buildSystemAudioModeRequest(int src, int avr, int avrPhysicalAddress, boolean enableSystemAudio) {
        if (enableSystemAudio) {
            return com.android.server.hdmi.HdmiCecMessage.build(src, avr, 112, physicalAddressToParam(avrPhysicalAddress));
        }
        return com.android.server.hdmi.HdmiCecMessage.build(src, avr, 112);
    }

    static com.android.server.hdmi.HdmiCecMessage buildSetSystemAudioMode(int src, int des, boolean systemAudioStatus) {
        return buildCommandWithBooleanParam(src, des, 114, systemAudioStatus);
    }

    static com.android.server.hdmi.HdmiCecMessage buildReportSystemAudioMode(int src, int des, boolean systemAudioStatus) {
        return buildCommandWithBooleanParam(src, des, 126, systemAudioStatus);
    }

    static com.android.server.hdmi.HdmiCecMessage buildReportShortAudioDescriptor(int src, int des, byte[] sadBytes) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, des, 163, sadBytes);
    }

    static com.android.server.hdmi.HdmiCecMessage buildGiveAudioStatus(int src, int dest) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 113);
    }

    static com.android.server.hdmi.HdmiCecMessage buildReportAudioStatus(int src, int dest, int volume, boolean mute) {
        byte status = (byte) (((byte) (mute ? 128 : 0)) | (((byte) volume) & 127));
        byte[] params = {status};
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 122, params);
    }

    static com.android.server.hdmi.HdmiCecMessage buildUserControlPressed(int src, int dest, int uiCommand) {
        return buildUserControlPressed(src, dest, new byte[]{(byte) (uiCommand & 255)});
    }

    static com.android.server.hdmi.HdmiCecMessage buildUserControlPressed(int src, int dest, byte[] commandParam) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 68, commandParam);
    }

    static com.android.server.hdmi.HdmiCecMessage buildUserControlReleased(int src, int dest) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 69);
    }

    static com.android.server.hdmi.HdmiCecMessage buildGiveSystemAudioModeStatus(int src, int dest) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 125);
    }

    public static com.android.server.hdmi.HdmiCecMessage buildStandby(int src, int dest) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 54);
    }

    static com.android.server.hdmi.HdmiCecMessage buildVendorCommand(int src, int dest, byte[] params) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 137, params);
    }

    static com.android.server.hdmi.HdmiCecMessage buildVendorCommandWithId(int src, int dest, int vendorId, byte[] operands) {
        byte[] params = new byte[operands.length + 3];
        params[0] = (byte) ((vendorId >> 16) & 255);
        params[1] = (byte) ((vendorId >> 8) & 255);
        params[2] = (byte) (vendorId & 255);
        java.lang.System.arraycopy(operands, 0, params, 3, operands.length);
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 160, params);
    }

    static com.android.server.hdmi.HdmiCecMessage buildRecordOn(int src, int dest, byte[] params) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 9, params);
    }

    static com.android.server.hdmi.HdmiCecMessage buildRecordOff(int src, int dest) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 11);
    }

    static com.android.server.hdmi.HdmiCecMessage buildSetDigitalTimer(int src, int dest, byte[] params) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 151, params);
    }

    static com.android.server.hdmi.HdmiCecMessage buildSetAnalogueTimer(int src, int dest, byte[] params) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 52, params);
    }

    static com.android.server.hdmi.HdmiCecMessage buildSetExternalTimer(int src, int dest, byte[] params) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 162, params);
    }

    static com.android.server.hdmi.HdmiCecMessage buildClearDigitalTimer(int src, int dest, byte[] params) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 153, params);
    }

    static com.android.server.hdmi.HdmiCecMessage buildClearAnalogueTimer(int src, int dest, byte[] params) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 51, params);
    }

    static com.android.server.hdmi.HdmiCecMessage buildClearExternalTimer(int src, int dest, byte[] params) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 161, params);
    }

    static com.android.server.hdmi.HdmiCecMessage buildGiveFeatures(int src, int dest) {
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, 165);
    }

    private static com.android.server.hdmi.HdmiCecMessage buildCommandWithBooleanParam(int i, int i2, int i3, boolean z) {
        return com.android.server.hdmi.HdmiCecMessage.build(i, i2, i3, new byte[]{z ? (byte) 1 : (byte) 0});
    }

    private static byte[] physicalAddressToParam(int physicalAddress) {
        return new byte[]{(byte) ((physicalAddress >> 8) & 255), (byte) (physicalAddress & 255)};
    }
}
