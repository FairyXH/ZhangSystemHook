package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class DevicePowerStatusAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int STATE_WAITING_FOR_REPORT_POWER_STATUS = 1;
    private static final java.lang.String TAG = "DevicePowerStatusAction";
    private int mRetriesOnTimeout;
    private final int mTargetAddress;

    static com.android.server.hdmi.DevicePowerStatusAction create(com.android.server.hdmi.HdmiCecLocalDevice source, int targetAddress, android.hardware.hdmi.IHdmiControlCallback callback) {
        if (source == null || callback == null) {
            android.util.Slog.e(TAG, "Wrong arguments");
            return null;
        }
        return new com.android.server.hdmi.DevicePowerStatusAction(source, targetAddress, callback);
    }

    private DevicePowerStatusAction(com.android.server.hdmi.HdmiCecLocalDevice localDevice, int targetAddress, android.hardware.hdmi.IHdmiControlCallback callback) {
        super(localDevice, callback);
        this.mRetriesOnTimeout = 1;
        this.mTargetAddress = targetAddress;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        android.hardware.hdmi.HdmiDeviceInfo deviceInfo;
        int powerStatus;
        com.android.server.hdmi.HdmiControlService service = localDevice().mService;
        if (service.getCecVersion() >= 6 && (deviceInfo = service.getHdmiCecNetwork().getCecDeviceInfo(this.mTargetAddress)) != null && deviceInfo.getCecVersion() >= 6 && (powerStatus = deviceInfo.getDevicePowerStatus()) != -1) {
            finishWithCallback(powerStatus);
            return true;
        }
        queryDevicePowerStatus();
        this.mState = 1;
        addTimer(this.mState, 2000);
        return true;
    }

    private void queryDevicePowerStatus() {
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildGiveDevicePowerStatus(getSourceAddress(), this.mTargetAddress), new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.DevicePowerStatusAction$$ExternalSyntheticLambda0
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public final void onSendCompleted(int i) {
                this.f$0.lambda$queryDevicePowerStatus$0(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$queryDevicePowerStatus$0(int error) {
        if (error == 1) {
            finishWithCallback(-1);
        }
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (this.mState != 1 || this.mTargetAddress != cmd.getSource() || cmd.getOpcode() != 144) {
            return false;
        }
        int status = cmd.getParams()[0];
        finishWithCallback(status);
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
        if (this.mState == state && state == 1) {
            if (this.mRetriesOnTimeout > 0) {
                this.mRetriesOnTimeout--;
                start();
            } else {
                finishWithCallback(-1);
            }
        }
    }
}
