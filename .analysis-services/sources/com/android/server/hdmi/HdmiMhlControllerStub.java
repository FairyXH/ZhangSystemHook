package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class HdmiMhlControllerStub {
    private static final int INVALID_DEVICE_ROLES = 0;
    private static final int INVALID_MHL_VERSION = 0;
    private static final int NO_SUPPORTED_FEATURES = 0;
    private static final android.util.SparseArray<com.android.server.hdmi.HdmiMhlLocalDeviceStub> mLocalDevices = new android.util.SparseArray<>();
    private static final android.hardware.hdmi.HdmiPortInfo[] EMPTY_PORT_INFO = new android.hardware.hdmi.HdmiPortInfo[0];

    private HdmiMhlControllerStub(com.android.server.hdmi.HdmiControlService service) {
    }

    boolean isReady() {
        return false;
    }

    static com.android.server.hdmi.HdmiMhlControllerStub create(com.android.server.hdmi.HdmiControlService service) {
        return new com.android.server.hdmi.HdmiMhlControllerStub(service);
    }

    android.hardware.hdmi.HdmiPortInfo[] getPortInfos() {
        return EMPTY_PORT_INFO;
    }

    com.android.server.hdmi.HdmiMhlLocalDeviceStub getLocalDevice(int portId) {
        return null;
    }

    com.android.server.hdmi.HdmiMhlLocalDeviceStub getLocalDeviceById(int deviceId) {
        return null;
    }

    android.util.SparseArray<com.android.server.hdmi.HdmiMhlLocalDeviceStub> getAllLocalDevices() {
        return mLocalDevices;
    }

    com.android.server.hdmi.HdmiMhlLocalDeviceStub removeLocalDevice(int portId) {
        return null;
    }

    com.android.server.hdmi.HdmiMhlLocalDeviceStub addLocalDevice(com.android.server.hdmi.HdmiMhlLocalDeviceStub device) {
        return null;
    }

    void clearAllLocalDevices() {
    }

    void sendVendorCommand(int portId, int offset, int length, byte[] data) {
    }

    void setOption(int flag, int value) {
    }

    int getMhlVersion(int portId) {
        return 0;
    }

    int getPeerMhlVersion(int portId) {
        return 0;
    }

    int getSupportedFeatures(int portId) {
        return 0;
    }

    int getEcbusDeviceRoles(int portId) {
        return 0;
    }

    void dump(com.android.internal.util.IndentingPrintWriter pw) {
    }
}
