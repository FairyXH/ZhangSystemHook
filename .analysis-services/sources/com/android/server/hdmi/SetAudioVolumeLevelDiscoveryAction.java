package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class SetAudioVolumeLevelDiscoveryAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int STATE_WAITING_FOR_FEATURE_ABORT = 1;
    private static final java.lang.String TAG = "SetAudioVolumeLevelDiscoveryAction";
    private final int mTargetAddress;

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public /* bridge */ /* synthetic */ void addCallback(android.hardware.hdmi.IHdmiControlCallback iHdmiControlCallback) {
        super.addCallback(iHdmiControlCallback);
    }

    public SetAudioVolumeLevelDiscoveryAction(com.android.server.hdmi.HdmiCecLocalDevice source, int targetAddress, android.hardware.hdmi.IHdmiControlCallback callback) {
        super(source, callback);
        this.mTargetAddress = targetAddress;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        sendCommand(com.android.server.hdmi.SetAudioVolumeLevelMessage.build(getSourceAddress(), this.mTargetAddress, 127), new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.SetAudioVolumeLevelDiscoveryAction$$ExternalSyntheticLambda0
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public final void onSendCompleted(int i) {
                this.f$0.lambda$start$0(i);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$0(int result) {
        if (result == 0) {
            this.mState = 1;
            addTimer(this.mState, 2000);
        } else {
            finishWithCallback(7);
        }
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (this.mState != 1) {
            return false;
        }
        switch (cmd.getOpcode()) {
        }
        return false;
    }

    private boolean handleFeatureAbort(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (cmd.getParams().length < 2) {
            return false;
        }
        int originalOpcode = cmd.getParams()[0] & 255;
        if (originalOpcode != 115 || cmd.getSource() != this.mTargetAddress) {
            return false;
        }
        finishWithCallback(0);
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
        if (updateSetAudioVolumeLevelSupport(1)) {
            finishWithCallback(0);
        } else {
            finishWithCallback(5);
        }
    }

    private boolean updateSetAudioVolumeLevelSupport(int setAudioVolumeLevelSupport) {
        com.android.server.hdmi.HdmiCecNetwork network = localDevice().mService.getHdmiCecNetwork();
        android.hardware.hdmi.HdmiDeviceInfo currentDeviceInfo = network.getCecDeviceInfo(this.mTargetAddress);
        if (currentDeviceInfo == null) {
            return false;
        }
        network.updateCecDevice(currentDeviceInfo.toBuilder().setDeviceFeatures(currentDeviceInfo.getDeviceFeatures().toBuilder().setSetAudioVolumeLevelSupport(setAudioVolumeLevelSupport).build()).build());
        return true;
    }

    public int getTargetAddress() {
        return this.mTargetAddress;
    }
}
