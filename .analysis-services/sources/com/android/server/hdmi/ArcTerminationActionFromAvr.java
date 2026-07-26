package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class ArcTerminationActionFromAvr extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int STATE_ARC_TERMINATED = 2;
    private static final int STATE_WAITING_FOR_INITIATE_ARC_RESPONSE = 1;
    public static final int TIMEOUT_MS = 1000;

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public /* bridge */ /* synthetic */ void addCallback(android.hardware.hdmi.IHdmiControlCallback iHdmiControlCallback) {
        super.addCallback(iHdmiControlCallback);
    }

    ArcTerminationActionFromAvr(com.android.server.hdmi.HdmiCecLocalDevice source) {
        super(source);
    }

    ArcTerminationActionFromAvr(com.android.server.hdmi.HdmiCecLocalDevice source, android.hardware.hdmi.IHdmiControlCallback callback) {
        super(source, callback);
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        this.mState = 1;
        addTimer(this.mState, 1000);
        sendTerminateArc();
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (this.mState != 1) {
            return false;
        }
        switch (cmd.getOpcode()) {
            case 0:
                int originalOpcode = cmd.getParams()[0] & 255;
                if (originalOpcode == 197) {
                    this.mState = 2;
                    audioSystem().processArcTermination();
                    finishWithCallback(3);
                }
                break;
            case 194:
                this.mState = 2;
                audioSystem().processArcTermination();
                finishWithCallback(0);
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
                handleTerminateArcTimeout();
                break;
        }
    }

    protected void sendTerminateArc() {
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildTerminateArc(getSourceAddress(), 0), new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.ArcTerminationActionFromAvr$$ExternalSyntheticLambda0
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public final void onSendCompleted(int i) {
                this.f$0.lambda$sendTerminateArc$0(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendTerminateArc$0(int result) {
        if (result != 0) {
            if (result == 1) {
                audioSystem().setArcStatus(false);
            }
            com.android.server.hdmi.HdmiLogger.debug("Terminate ARC was not successfully sent.", new java.lang.Object[0]);
            finishWithCallback(3);
        }
    }

    private void handleTerminateArcTimeout() {
        audioSystem().setArcStatus(false);
        com.android.server.hdmi.HdmiLogger.debug("handleTerminateArcTimeout", new java.lang.Object[0]);
        finishWithCallback(1);
    }
}
