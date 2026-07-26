package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class ArcInitiationActionFromAvr extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int STATE_ARC_INITIATED = 2;
    private static final int STATE_WAITING_FOR_INITIATE_ARC_RESPONSE = 1;
    private static final int TIMEOUT_MS = 1000;

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public /* bridge */ /* synthetic */ void addCallback(android.hardware.hdmi.IHdmiControlCallback iHdmiControlCallback) {
        super.addCallback(iHdmiControlCallback);
    }

    ArcInitiationActionFromAvr(com.android.server.hdmi.HdmiCecLocalDevice source) {
        super(source);
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        audioSystem().setArcStatus(true);
        this.mState = 1;
        addTimer(this.mState, 1000);
        sendInitiateArc();
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (this.mState != 1) {
            return false;
        }
        switch (cmd.getOpcode()) {
            case 0:
                if ((cmd.getParams()[0] & 255) == 192) {
                    audioSystem().setArcStatus(false);
                    finish();
                }
                break;
            case 193:
                this.mState = 2;
                finish();
                break;
            case 194:
                audioSystem().setArcStatus(false);
                finish();
                break;
        }
        return false;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
        if (this.mState != state) {
        }
        switch (this.mState) {
            case 1:
                handleInitiateArcTimeout();
                break;
        }
    }

    protected void sendInitiateArc() {
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildInitiateArc(getSourceAddress(), 0), new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.ArcInitiationActionFromAvr$$ExternalSyntheticLambda0
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public final void onSendCompleted(int i) {
                this.f$0.lambda$sendInitiateArc$0(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendInitiateArc$0(int result) {
        if (result != 0) {
            audioSystem().setArcStatus(false);
            finish();
        }
    }

    private void handleInitiateArcTimeout() {
        com.android.server.hdmi.HdmiLogger.debug("handleInitiateArcTimeout", new java.lang.Object[0]);
        finish();
    }
}
