package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class OneTouchPlayAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int LOOP_COUNTER_MAX = 10;
    static final int STATE_CHECK_STANDBY_PROCESS_STARTED = 2;
    static final int STATE_WAITING_FOR_REPORT_POWER_STATUS = 1;
    private static final java.lang.String TAG = "OneTouchPlayAction";
    private final boolean mIsCec20;
    private int mPowerStatusCounter;
    private com.android.server.hdmi.HdmiCecLocalDeviceSource mSource;
    private final int mTargetAddress;

    static com.android.server.hdmi.OneTouchPlayAction create(com.android.server.hdmi.HdmiCecLocalDeviceSource source, int targetAddress, android.hardware.hdmi.IHdmiControlCallback callback) {
        if (source == null || callback == null) {
            android.util.Slog.e(TAG, "Wrong arguments");
            return null;
        }
        return new com.android.server.hdmi.OneTouchPlayAction(source, targetAddress, callback);
    }

    private OneTouchPlayAction(com.android.server.hdmi.HdmiCecLocalDevice localDevice, int targetAddress, android.hardware.hdmi.IHdmiControlCallback callback) {
        this(localDevice, targetAddress, callback, localDevice.getDeviceInfo().getCecVersion() >= 6 && getTargetCecVersion(localDevice, targetAddress) >= 6);
    }

    OneTouchPlayAction(com.android.server.hdmi.HdmiCecLocalDevice localDevice, int targetAddress, android.hardware.hdmi.IHdmiControlCallback callback, boolean isCec20) {
        super(localDevice, callback);
        this.mPowerStatusCounter = 0;
        this.mTargetAddress = targetAddress;
        this.mIsCec20 = isCec20;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        this.mSource = source();
        if (!this.mSource.mService.getPowerManager().isInteractive()) {
            android.util.Slog.d(TAG, "PowerManager is not interactive. Delay the action to check if standby started!");
            this.mState = 2;
            addTimer(this.mState, 2000);
            return true;
        }
        startAction();
        return true;
    }

    private void startAction() {
        int targetPowerStatus;
        android.util.Slog.i(TAG, "Start action.");
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildTextViewOn(getSourceAddress(), this.mTargetAddress));
        boolean is20TargetOnBefore = this.mIsCec20 && getTargetDevicePowerStatus(this.mSource, this.mTargetAddress, -1) == 0;
        setAndBroadcastActiveSource();
        if (shouldTurnOnConnectedAudioSystem()) {
            sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildSystemAudioModeRequest(getSourceAddress(), 5, getSourcePath(), true));
        }
        if (!this.mIsCec20 || (targetPowerStatus = getTargetDevicePowerStatus(this.mSource, this.mTargetAddress, -1)) == -1) {
            queryDevicePowerStatus();
        } else if (targetPowerStatus == 0) {
            if (!is20TargetOnBefore) {
                maySendActiveSource();
            }
            finishWithCallback(0);
            return;
        }
        this.mState = 1;
        addTimer(this.mState, 2000);
    }

    private void setAndBroadcastActiveSource() {
        this.mSource.mService.setAndBroadcastActiveSourceFromOneDeviceType(this.mTargetAddress, getSourcePath(), "OneTouchPlayAction#broadcastActiveSource()");
        if (this.mSource.mService.audioSystem() != null) {
            this.mSource = this.mSource.mService.audioSystem();
        }
        this.mSource.setRoutingPort(0);
        this.mSource.setLocalActivePort(0);
    }

    private void maySendActiveSource() {
        this.mSource.maySendActiveSource(this.mTargetAddress);
    }

    private void queryDevicePowerStatus() {
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildGiveDevicePowerStatus(getSourceAddress(), this.mTargetAddress));
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (this.mState != 1 || this.mTargetAddress != cmd.getSource() || cmd.getOpcode() != 144) {
            return false;
        }
        int status = cmd.getParams()[0];
        if (status == 0) {
            maySendActiveSource();
            finishWithCallback(0);
        }
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
        if (this.mState != state) {
        }
        switch (state) {
            case 1:
                int i = this.mPowerStatusCounter;
                this.mPowerStatusCounter = i + 1;
                if (i < 10) {
                    queryDevicePowerStatus();
                    addTimer(this.mState, 2000);
                } else {
                    finishWithCallback(1);
                }
                break;
            case 2:
                android.util.Slog.d(TAG, "Action was not removed, start the action.");
                startAction();
                break;
        }
    }

    private boolean shouldTurnOnConnectedAudioSystem() {
        com.android.server.hdmi.HdmiControlService service = this.mSource.mService;
        if (service.isAudioSystemDevice()) {
            return false;
        }
        java.lang.String powerControlMode = service.getHdmiCecConfig().getStringValue("power_control_mode");
        return powerControlMode.equals("to_tv_and_audio_system") || powerControlMode.equals("broadcast");
    }

    private static int getTargetCecVersion(com.android.server.hdmi.HdmiCecLocalDevice localDevice, int targetLogicalAddress) {
        android.hardware.hdmi.HdmiDeviceInfo targetDevice = localDevice.mService.getHdmiCecNetwork().getCecDeviceInfo(targetLogicalAddress);
        if (targetDevice != null) {
            return targetDevice.getCecVersion();
        }
        return 5;
    }

    private static int getTargetDevicePowerStatus(com.android.server.hdmi.HdmiCecLocalDevice localDevice, int targetLogicalAddress, int defaultPowerStatus) {
        android.hardware.hdmi.HdmiDeviceInfo targetDevice = localDevice.mService.getHdmiCecNetwork().getCecDeviceInfo(targetLogicalAddress);
        if (targetDevice != null) {
            return targetDevice.getDevicePowerStatus();
        }
        return defaultPowerStatus;
    }
}
