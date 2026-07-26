package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class DetectTvSystemAudioModeSupportAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    static final int MAX_RETRY_COUNT = 5;
    private static final int STATE_WAITING_FOR_FEATURE_ABORT = 1;
    private static final int STATE_WAITING_FOR_SET_SAM = 2;
    private com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.TvSystemAudioModeSupportedCallback mCallback;
    private int mSendSetSystemAudioModeRetryCount;

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public /* bridge */ /* synthetic */ void addCallback(android.hardware.hdmi.IHdmiControlCallback iHdmiControlCallback) {
        super.addCallback(iHdmiControlCallback);
    }

    DetectTvSystemAudioModeSupportAction(com.android.server.hdmi.HdmiCecLocalDevice source, com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.TvSystemAudioModeSupportedCallback callback) {
        super(source);
        this.mSendSetSystemAudioModeRetryCount = 0;
        this.mCallback = callback;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        this.mState = 1;
        addTimer(this.mState, 2000);
        sendSetSystemAudioMode();
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (cmd.getOpcode() != 0 || this.mState != 1 || com.android.server.hdmi.HdmiUtils.getAbortFeatureOpcode(cmd) != 114) {
            return false;
        }
        if (com.android.server.hdmi.HdmiUtils.getAbortReason(cmd) == 1) {
            this.mActionTimer.clearTimerMessage();
            this.mState = 2;
            addTimer(this.mState, 300);
        } else {
            finishAction(false);
        }
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
        if (this.mState != state) {
        }
        switch (this.mState) {
            case 1:
                finishAction(true);
                break;
            case 2:
                this.mSendSetSystemAudioModeRetryCount++;
                if (this.mSendSetSystemAudioModeRetryCount < 5) {
                    this.mState = 1;
                    addTimer(this.mState, 2000);
                    sendSetSystemAudioMode();
                } else {
                    finishAction(false);
                }
                break;
        }
    }

    protected void sendSetSystemAudioMode() {
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildSetSystemAudioMode(getSourceAddress(), 0, true), new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.DetectTvSystemAudioModeSupportAction$$ExternalSyntheticLambda0
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public final void onSendCompleted(int i) {
                this.f$0.lambda$sendSetSystemAudioMode$0(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSetSystemAudioMode$0(int result) {
        if (result != 0) {
            finishAction(false);
        }
    }

    private void finishAction(boolean supported) {
        this.mCallback.onResult(supported);
        audioSystem().setTvSystemAudioModeSupport(supported);
        finish();
    }
}
