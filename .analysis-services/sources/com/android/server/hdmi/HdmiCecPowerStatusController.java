package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
class HdmiCecPowerStatusController {
    private final com.android.server.hdmi.HdmiControlService mHdmiControlService;
    private int mPowerStatus = 1;

    HdmiCecPowerStatusController(com.android.server.hdmi.HdmiControlService hdmiControlService) {
        this.mHdmiControlService = hdmiControlService;
    }

    int getPowerStatus() {
        return this.mPowerStatus;
    }

    boolean isPowerStatusOn() {
        return this.mPowerStatus == 0;
    }

    boolean isPowerStatusStandby() {
        return this.mPowerStatus == 1;
    }

    boolean isPowerStatusTransientToOn() {
        return this.mPowerStatus == 2;
    }

    boolean isPowerStatusTransientToStandby() {
        return this.mPowerStatus == 3;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void setPowerStatus(int powerStatus) {
        setPowerStatus(powerStatus, true);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void setPowerStatus(int powerStatus, boolean sendPowerStatusUpdate) {
        if (powerStatus == this.mPowerStatus) {
            return;
        }
        this.mPowerStatus = powerStatus;
        if (sendPowerStatusUpdate && this.mHdmiControlService.getCecVersion() >= 6) {
            sendReportPowerStatus(this.mPowerStatus);
        }
    }

    private void sendReportPowerStatus(int powerStatus) {
        for (com.android.server.hdmi.HdmiCecLocalDevice localDevice : this.mHdmiControlService.getAllCecLocalDevices()) {
            this.mHdmiControlService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildReportPowerStatus(localDevice.getDeviceInfo().getLogicalAddress(), 15, powerStatus));
        }
    }
}
