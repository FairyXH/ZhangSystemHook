package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
abstract class HdmiEarcLocalDevice extends com.android.server.hdmi.HdmiLocalDevice {
    private static final java.lang.String TAG = "HdmiEarcLocalDevice";
    protected int mEarcStatus;

    protected abstract void handleEarcCapabilitiesReported(byte[] bArr);

    protected abstract void handleEarcStateChange(int i);

    protected HdmiEarcLocalDevice(com.android.server.hdmi.HdmiControlService service, int deviceType) {
        super(service, deviceType);
    }

    static com.android.server.hdmi.HdmiEarcLocalDevice create(com.android.server.hdmi.HdmiControlService service, int deviceType) {
        switch (deviceType) {
            case 0:
                return new com.android.server.hdmi.HdmiEarcLocalDeviceTx(service);
            default:
                return null;
        }
    }

    protected void disableDevice() {
    }

    protected void dump(android.util.IndentingPrintWriter pw) {
    }
}
