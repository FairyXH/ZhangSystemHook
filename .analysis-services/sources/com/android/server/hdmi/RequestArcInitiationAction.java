package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class RequestArcInitiationAction extends com.android.server.hdmi.RequestArcAction {
    private static final java.lang.String TAG = "RequestArcInitiationAction";

    RequestArcInitiationAction(com.android.server.hdmi.HdmiCecLocalDevice source, int avrAddress) {
        super(source, avrAddress);
    }

    RequestArcInitiationAction(com.android.server.hdmi.HdmiCecLocalDevice source, int avrAddress, android.hardware.hdmi.IHdmiControlCallback callback) {
        super(source, avrAddress, callback);
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        this.mState = 1;
        addTimer(this.mState, 2000);
        com.android.server.hdmi.HdmiCecMessage command = com.android.server.hdmi.HdmiCecMessageBuilder.buildRequestArcInitiation(getSourceAddress(), this.mAvrAddress);
        sendCommand(command, new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.RequestArcInitiationAction.1
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public void onSendCompleted(int error) {
                if (error != 0) {
                    com.android.server.hdmi.RequestArcInitiationAction.this.tv().disableArc();
                    com.android.server.hdmi.RequestArcInitiationAction.this.finishWithCallback(3);
                }
            }
        });
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (this.mState != 1 || !com.android.server.hdmi.HdmiUtils.checkCommandSource(cmd, this.mAvrAddress, TAG)) {
            return false;
        }
        int opcode = cmd.getOpcode();
        switch (opcode) {
            case 0:
                int originalOpcode = cmd.getParams()[0] & 255;
                if (originalOpcode == 195) {
                    tv().disableArc();
                    finishWithCallback(3);
                }
                break;
            case 192:
                finishWithCallback(0);
                break;
        }
        return false;
    }
}
