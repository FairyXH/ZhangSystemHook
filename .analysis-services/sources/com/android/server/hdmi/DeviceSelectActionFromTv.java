package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class DeviceSelectActionFromTv extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int LOOP_COUNTER_MAX = 20;
    static final int STATE_WAIT_FOR_DEVICE_POWER_ON = 3;
    private static final int STATE_WAIT_FOR_DEVICE_TO_TRANSIT_TO_STANDBY = 2;
    static final int STATE_WAIT_FOR_REPORT_POWER_STATUS = 1;
    private static final java.lang.String TAG = "DeviceSelect";
    private static final int TIMEOUT_POWER_ON_MS = 5000;
    private static final int TIMEOUT_TRANSIT_TO_STANDBY_MS = 5000;
    private final com.android.server.hdmi.HdmiCecMessage mGivePowerStatus;
    private final boolean mIsCec20;
    private int mPowerStatusCounter;
    private final android.hardware.hdmi.HdmiDeviceInfo mTarget;

    DeviceSelectActionFromTv(com.android.server.hdmi.HdmiCecLocalDeviceTv source, android.hardware.hdmi.HdmiDeviceInfo target, android.hardware.hdmi.IHdmiControlCallback callback) {
        this(source, target, callback, source.getDeviceInfo().getCecVersion() >= 6 && target.getCecVersion() >= 6);
    }

    DeviceSelectActionFromTv(com.android.server.hdmi.HdmiCecLocalDeviceTv source, android.hardware.hdmi.HdmiDeviceInfo target, android.hardware.hdmi.IHdmiControlCallback callback, boolean isCec20) {
        super(source, callback);
        this.mPowerStatusCounter = 0;
        this.mTarget = target;
        this.mGivePowerStatus = com.android.server.hdmi.HdmiCecMessageBuilder.buildGiveDevicePowerStatus(getSourceAddress(), getTargetAddress());
        this.mIsCec20 = isCec20;
    }

    int getTargetAddress() {
        return this.mTarget.getLogicalAddress();
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean start() {
        sendSetStreamPath();
        if (!this.mIsCec20) {
            queryDevicePowerStatus();
        } else {
            int targetPowerStatus = -1;
            android.hardware.hdmi.HdmiDeviceInfo targetDevice = localDevice().mService.getHdmiCecNetwork().getCecDeviceInfo(getTargetAddress());
            if (targetDevice != null) {
                targetPowerStatus = targetDevice.getDevicePowerStatus();
            }
            if (targetPowerStatus == -1) {
                queryDevicePowerStatus();
            } else if (targetPowerStatus == 0) {
                finishWithCallback(0);
                return true;
            }
        }
        this.mState = 1;
        addTimer(this.mState, 2000);
        return true;
    }

    private void queryDevicePowerStatus() {
        sendCommand(this.mGivePowerStatus, new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.DeviceSelectActionFromTv.1
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public void onSendCompleted(int error) {
                if (error != 0) {
                    com.android.server.hdmi.DeviceSelectActionFromTv.this.finishWithCallback(7);
                }
            }
        });
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (cmd.getSource() != getTargetAddress()) {
            return false;
        }
        int opcode = cmd.getOpcode();
        byte[] params = cmd.getParams();
        switch (this.mState) {
            case 1:
                if (opcode == 144) {
                }
                break;
        }
        return false;
    }

    private boolean handleReportPowerStatus(int powerStatus) {
        switch (powerStatus) {
            case 0:
                selectDevice();
                break;
            case 1:
                if (this.mPowerStatusCounter == 0) {
                    turnOnDevice();
                    this.mState = 3;
                    addTimer(this.mState, 5000);
                } else {
                    selectDevice();
                }
                break;
            case 2:
                if (this.mPowerStatusCounter < 20) {
                    this.mState = 3;
                    addTimer(this.mState, 5000);
                } else {
                    selectDevice();
                }
                break;
            case 3:
                if (this.mPowerStatusCounter < 4) {
                    this.mState = 2;
                    addTimer(this.mState, 5000);
                } else {
                    selectDevice();
                }
                break;
        }
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public void handleTimerEvent(int timeoutState) {
        if (this.mState != timeoutState) {
            android.util.Slog.w(TAG, "Timer in a wrong state. Ignored.");
        }
        switch (this.mState) {
            case 1:
                if (tv().isPowerStandbyOrTransient()) {
                    finishWithCallback(6);
                } else {
                    selectDevice();
                }
                break;
            case 2:
            case 3:
                this.mPowerStatusCounter++;
                queryDevicePowerStatus();
                this.mState = 1;
                addTimer(this.mState, 2000);
                break;
        }
    }

    private void turnOnDevice() {
        if (!this.mIsCec20) {
            sendUserControlPressedAndReleased(this.mTarget.getLogicalAddress(), 64);
            sendUserControlPressedAndReleased(this.mTarget.getLogicalAddress(), 109);
        }
    }

    private void selectDevice() {
        if (!this.mIsCec20) {
            sendSetStreamPath();
        }
        finishWithCallback(0);
    }

    private void sendSetStreamPath() {
        tv().getActiveSource().invalidate();
        tv().setActivePath(this.mTarget.getPhysicalAddress());
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildSetStreamPath(getSourceAddress(), this.mTarget.getPhysicalAddress()));
    }
}
