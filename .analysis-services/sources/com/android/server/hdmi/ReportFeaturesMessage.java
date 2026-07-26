package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class ReportFeaturesMessage extends com.android.server.hdmi.HdmiCecMessage {
    private final int mCecVersion;
    private final android.hardware.hdmi.DeviceFeatures mDeviceFeatures;

    private ReportFeaturesMessage(int source, int destination, byte[] params, int cecVersion, android.hardware.hdmi.DeviceFeatures deviceFeatures) {
        super(source, destination, 166, params, 0);
        this.mCecVersion = cecVersion;
        this.mDeviceFeatures = deviceFeatures;
    }

    public static com.android.server.hdmi.HdmiCecMessage build(int source, int cecVersion, java.util.List<java.lang.Integer> allDeviceTypes, int rcProfile, java.util.List<java.lang.Integer> rcFeatures, android.hardware.hdmi.DeviceFeatures deviceFeatures) {
        byte rcProfileByte;
        byte cecVersionByte = (byte) (cecVersion & 255);
        byte deviceTypes = 0;
        for (java.lang.Integer deviceType : allDeviceTypes) {
            deviceTypes = (byte) (((byte) (1 << hdmiDeviceInfoDeviceTypeToShiftValue(deviceType.intValue()))) | deviceTypes);
        }
        byte rcProfileByte2 = (byte) (((byte) (rcProfile << 6)) | 0);
        if (rcProfile == 1) {
            for (java.lang.Integer rcFeature : rcFeatures) {
                rcProfileByte2 = (byte) (((byte) (1 << rcFeature.intValue())) | rcProfileByte2);
            }
            rcProfileByte = rcProfileByte2;
        } else {
            byte rcProfileTv = (byte) (rcFeatures.get(0).intValue() & 65535);
            rcProfileByte = (byte) (rcProfileByte2 | rcProfileTv);
        }
        byte[] fixedOperands = {cecVersionByte, deviceTypes, rcProfileByte};
        byte[] deviceFeaturesBytes = deviceFeatures.toOperand();
        byte[] params = java.util.Arrays.copyOf(fixedOperands, fixedOperands.length + deviceFeaturesBytes.length);
        java.lang.System.arraycopy(deviceFeaturesBytes, 0, params, fixedOperands.length, deviceFeaturesBytes.length);
        int addressValidationResult = validateAddress(source, 15);
        if (addressValidationResult != 0) {
            return new com.android.server.hdmi.HdmiCecMessage(source, 15, 166, params, addressValidationResult);
        }
        return new com.android.server.hdmi.ReportFeaturesMessage(source, 15, params, cecVersion, deviceFeatures);
    }

    private static int hdmiDeviceInfoDeviceTypeToShiftValue(int deviceType) {
        switch (deviceType) {
            case 0:
                return 7;
            case 1:
                return 6;
            case 2:
            default:
                throw new java.lang.IllegalArgumentException("Unhandled device type: " + deviceType);
            case 3:
                return 5;
            case 4:
                return 4;
            case 5:
                return 3;
            case 6:
                return 2;
        }
    }

    static com.android.server.hdmi.HdmiCecMessage build(final int source, final int destination, final byte[] params) {
        java.util.function.Function<java.lang.Integer, com.android.server.hdmi.HdmiCecMessage> invalidMessage = new java.util.function.Function() { // from class: com.android.server.hdmi.ReportFeaturesMessage$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.hdmi.ReportFeaturesMessage.lambda$build$0(source, destination, params, (java.lang.Integer) obj);
            }
        };
        int addressValidationResult = validateAddress(source, destination);
        if (addressValidationResult != 0) {
            return invalidMessage.apply(java.lang.Integer.valueOf(addressValidationResult));
        }
        if (params.length >= 4) {
            int cecVersion = java.lang.Byte.toUnsignedInt(params[0]);
            int rcProfileEnd = com.android.server.hdmi.HdmiUtils.getEndOfSequence(params, 2);
            if (rcProfileEnd != -1) {
                int deviceFeaturesEnd = com.android.server.hdmi.HdmiUtils.getEndOfSequence(params, rcProfileEnd + 1);
                if (deviceFeaturesEnd == -1) {
                    return invalidMessage.apply(4);
                }
                int deviceFeaturesStart = com.android.server.hdmi.HdmiUtils.getEndOfSequence(params, 2) + 1;
                byte[] deviceFeaturesBytes = java.util.Arrays.copyOfRange(params, deviceFeaturesStart, params.length);
                android.hardware.hdmi.DeviceFeatures deviceFeatures = android.hardware.hdmi.DeviceFeatures.fromOperand(deviceFeaturesBytes);
                return new com.android.server.hdmi.ReportFeaturesMessage(source, destination, params, cecVersion, deviceFeatures);
            }
            return invalidMessage.apply(4);
        }
        return invalidMessage.apply(4);
    }

    static /* synthetic */ com.android.server.hdmi.HdmiCecMessage lambda$build$0(int source, int destination, byte[] params, java.lang.Integer validationResult) {
        return new com.android.server.hdmi.HdmiCecMessage(source, destination, 166, params, validationResult.intValue());
    }

    public static int validateAddress(int source, int destination) {
        return com.android.server.hdmi.HdmiCecMessageValidator.validateAddress(source, destination, 32767, 32768);
    }

    public int getCecVersion() {
        return this.mCecVersion;
    }

    public android.hardware.hdmi.DeviceFeatures getDeviceFeatures() {
        return this.mDeviceFeatures;
    }
}
