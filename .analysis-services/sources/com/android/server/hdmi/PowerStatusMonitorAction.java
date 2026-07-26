package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class PowerStatusMonitorAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int INVALID_POWER_STATUS = -2;
    private static final int MONITORING_INTERVAL_MS = 60000;
    private static final int REPORT_POWER_STATUS_TIMEOUT_MS = 5000;
    private static final int STATE_WAIT_FOR_NEXT_MONITORING = 2;
    private static final int STATE_WAIT_FOR_REPORT_POWER_STATUS = 1;
    private static final java.lang.String TAG = "PowerStatusMonitorAction";
    private final android.util.SparseIntArray mPowerStatus;

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public /* bridge */ /* synthetic */ void addCallback(android.hardware.hdmi.IHdmiControlCallback iHdmiControlCallback) {
        super.addCallback(iHdmiControlCallback);
    }

    PowerStatusMonitorAction(com.android.server.hdmi.HdmiCecLocalDevice source) {
        super(source);
        this.mPowerStatus = new android.util.SparseIntArray();
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        queryPowerStatus();
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (this.mState == 1 && cmd.getOpcode() == 144) {
            return handleReportPowerStatus(cmd);
        }
        return false;
    }

    private boolean handleReportPowerStatus(com.android.server.hdmi.HdmiCecMessage cmd) {
        int sourceAddress = cmd.getSource();
        int oldStatus = this.mPowerStatus.get(sourceAddress, -2);
        if (oldStatus == -2) {
            return false;
        }
        int newStatus = cmd.getParams()[0] & 255;
        updatePowerStatus(sourceAddress, newStatus, true);
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
        switch (this.mState) {
            case 1:
                handleTimeout();
                break;
            case 2:
                queryPowerStatus();
                break;
        }
    }

    private void handleTimeout() {
        for (int i = 0; i < this.mPowerStatus.size(); i++) {
            int logicalAddress = this.mPowerStatus.keyAt(i);
            updatePowerStatus(logicalAddress, -1, false);
        }
        this.mPowerStatus.clear();
        this.mState = 2;
    }

    private void resetPowerStatus(java.util.List<android.hardware.hdmi.HdmiDeviceInfo> deviceInfos) {
        this.mPowerStatus.clear();
        for (android.hardware.hdmi.HdmiDeviceInfo info : deviceInfos) {
            if (localDevice().mService.getCecVersion() < 6 || info.getCecVersion() < 6) {
                this.mPowerStatus.append(info.getLogicalAddress(), info.getDevicePowerStatus());
            }
        }
    }

    private void queryPowerStatus() {
        java.util.List<android.hardware.hdmi.HdmiDeviceInfo> deviceInfos = localDevice().mService.getHdmiCecNetwork().getDeviceInfoList(false);
        resetPowerStatus(deviceInfos);
        for (android.hardware.hdmi.HdmiDeviceInfo info : deviceInfos) {
            if (localDevice().mService.getCecVersion() < 6 || info.getCecVersion() < 6) {
                final int logicalAddress = info.getLogicalAddress();
                sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildGiveDevicePowerStatus(getSourceAddress(), logicalAddress), new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.PowerStatusMonitorAction.1
                    @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
                    public void onSendCompleted(int error) {
                        if (error != 0) {
                            com.android.server.hdmi.PowerStatusMonitorAction.this.updatePowerStatus(logicalAddress, -1, true);
                        }
                    }
                });
            }
        }
        this.mState = 1;
        addTimer(2, 60000);
        addTimer(1, 5000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePowerStatus(int logicalAddress, int newStatus, boolean remove) {
        localDevice().mService.getHdmiCecNetwork().updateDevicePowerStatus(logicalAddress, newStatus);
        if (remove) {
            this.mPowerStatus.delete(logicalAddress);
        }
    }
}
