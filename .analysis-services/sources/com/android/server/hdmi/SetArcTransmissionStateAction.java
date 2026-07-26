package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class SetArcTransmissionStateAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int STATE_WAITING_TIMEOUT = 1;
    private static final java.lang.String TAG = "SetArcTransmissionStateAction";
    private final int mAvrAddress;
    private final boolean mEnabled;

    SetArcTransmissionStateAction(com.android.server.hdmi.HdmiCecLocalDevice source, int avrAddress, boolean enabled) {
        super(source);
        if (!com.android.server.hdmi.HdmiUtils.verifyAddressType(getSourceAddress(), 0) || !com.android.server.hdmi.HdmiUtils.verifyAddressType(avrAddress, 5)) {
            android.util.Slog.w(TAG, "Device type mismatch, stop the action.");
            finish();
        }
        this.mAvrAddress = avrAddress;
        this.mEnabled = enabled;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        if (this.mEnabled) {
            com.android.server.hdmi.RequestSadAction action = new com.android.server.hdmi.RequestSadAction(localDevice(), 5, new com.android.server.hdmi.RequestSadAction.RequestSadCallback() { // from class: com.android.server.hdmi.SetArcTransmissionStateAction.1
                @Override // com.android.server.hdmi.RequestSadAction.RequestSadCallback
                public void onRequestSadDone(java.util.List<byte[]> supportedSads) {
                    android.util.Slog.i(com.android.server.hdmi.SetArcTransmissionStateAction.TAG, "Enabling ARC");
                    com.android.server.hdmi.SetArcTransmissionStateAction.this.tv().enableArc(supportedSads);
                    com.android.server.hdmi.SetArcTransmissionStateAction.this.mState = 1;
                    com.android.server.hdmi.SetArcTransmissionStateAction.this.addTimer(com.android.server.hdmi.SetArcTransmissionStateAction.this.mState, 2000);
                    com.android.server.hdmi.SetArcTransmissionStateAction.this.sendReportArcInitiated();
                }
            });
            addAndStartAction(action);
            return true;
        }
        disableArc();
        finish();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendReportArcInitiated() {
        com.android.server.hdmi.HdmiCecMessage command = com.android.server.hdmi.HdmiCecMessageBuilder.buildReportArcInitiated(getSourceAddress(), this.mAvrAddress);
        sendCommand(command, new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.SetArcTransmissionStateAction.2
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public void onSendCompleted(int error) {
                switch (error) {
                    case 1:
                        com.android.server.hdmi.SetArcTransmissionStateAction.this.disableArc();
                        com.android.server.hdmi.HdmiLogger.debug("Failed to send <Report Arc Initiated>.", new java.lang.Object[0]);
                        com.android.server.hdmi.SetArcTransmissionStateAction.this.finish();
                        break;
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disableArc() {
        android.util.Slog.i(TAG, "Disabling ARC");
        tv().disableArc();
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildReportArcTerminated(getSourceAddress(), this.mAvrAddress));
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (this.mState != 1) {
            return false;
        }
        int opcode = cmd.getOpcode();
        if (opcode == 0) {
            int originalOpcode = cmd.getParams()[0] & 255;
            if (originalOpcode == 193) {
                com.android.server.hdmi.HdmiLogger.debug("Feature aborted for <Report Arc Initiated>", new java.lang.Object[0]);
                disableArc();
                finish();
                return true;
            }
        }
        return false;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
        if (this.mState != state || this.mState != 1) {
            return;
        }
        finish();
    }
}
