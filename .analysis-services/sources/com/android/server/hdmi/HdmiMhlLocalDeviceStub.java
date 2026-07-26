package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class HdmiMhlLocalDeviceStub {
    private static final android.hardware.hdmi.HdmiDeviceInfo INFO = android.hardware.hdmi.HdmiDeviceInfo.mhlDevice(65535, -1, -1, -1);
    private final int mPortId;
    private final com.android.server.hdmi.HdmiControlService mService;

    protected HdmiMhlLocalDeviceStub(com.android.server.hdmi.HdmiControlService service, int portId) {
        this.mService = service;
        this.mPortId = portId;
    }

    void onDeviceRemoved() {
    }

    android.hardware.hdmi.HdmiDeviceInfo getInfo() {
        return INFO;
    }

    void setBusMode(int cbusmode) {
    }

    void onBusOvercurrentDetected(boolean on) {
    }

    void setDeviceStatusChange(int adopterId, int deviceId) {
    }

    int getPortId() {
        return this.mPortId;
    }

    void turnOn(android.hardware.hdmi.IHdmiControlCallback callback) {
    }

    void sendKeyEvent(int keycode, boolean isPressed) {
    }

    void sendStandby() {
    }
}
