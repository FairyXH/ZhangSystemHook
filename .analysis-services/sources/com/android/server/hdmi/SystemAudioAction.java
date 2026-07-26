package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
abstract class SystemAudioAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int MAX_SEND_RETRY_COUNT = 2;
    private static final int OFF_TIMEOUT_MS = 2000;
    private static final int ON_TIMEOUT_MS = 5000;
    private static final int STATE_CHECK_ROUTING_IN_PRGRESS = 1;
    private static final int STATE_WAIT_FOR_SET_SYSTEM_AUDIO_MODE = 2;
    private static final java.lang.String TAG = "SystemAudioAction";
    protected final int mAvrLogicalAddress;
    private int mSendRetryCount;
    protected boolean mTargetAudioStatus;

    SystemAudioAction(com.android.server.hdmi.HdmiCecLocalDevice source, int avrAddress, boolean targetStatus, android.hardware.hdmi.IHdmiControlCallback callback) {
        super(source, callback);
        this.mSendRetryCount = 0;
        if (!com.android.server.hdmi.HdmiUtils.verifyAddressType(avrAddress, 5)) {
            android.util.Slog.w(TAG, "Device type mismatch, stop the action.");
            finish();
        }
        this.mAvrLogicalAddress = avrAddress;
        this.mTargetAudioStatus = targetStatus;
    }

    protected void sendSystemAudioModeRequest() {
        java.util.List<com.android.server.hdmi.RoutingControlAction> routingActions = getActions(com.android.server.hdmi.RoutingControlAction.class);
        if (!routingActions.isEmpty()) {
            this.mState = 1;
            com.android.server.hdmi.RoutingControlAction routingAction = routingActions.get(0);
            routingAction.addOnFinishedCallback(this, new java.lang.Runnable() { // from class: com.android.server.hdmi.SystemAudioAction.1
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.hdmi.SystemAudioAction.this.sendSystemAudioModeRequestInternal();
                }
            });
            return;
        }
        sendSystemAudioModeRequestInternal();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendSystemAudioModeRequestInternal() {
        com.android.server.hdmi.HdmiCecMessage command = com.android.server.hdmi.HdmiCecMessageBuilder.buildSystemAudioModeRequest(getSourceAddress(), this.mAvrLogicalAddress, getSystemAudioModeRequestParam(), this.mTargetAudioStatus);
        sendCommand(command, new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.SystemAudioAction.2
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public void onSendCompleted(int error) {
                if (error != 0) {
                    com.android.server.hdmi.HdmiLogger.debug("Failed to send <System Audio Mode Request>:" + error, new java.lang.Object[0]);
                    com.android.server.hdmi.SystemAudioAction.this.setSystemAudioMode(false);
                    com.android.server.hdmi.SystemAudioAction.this.finishWithCallback(7);
                }
            }
        });
        this.mState = 2;
        addTimer(this.mState, this.mTargetAudioStatus ? 5000 : 2000);
    }

    private int getSystemAudioModeRequestParam() {
        if (tv().getActiveSource().isValid()) {
            return tv().getActiveSource().physicalAddress;
        }
        int param = tv().getActivePath();
        if (param != 65535) {
            return param;
        }
        return 0;
    }

    private void handleSendSystemAudioModeRequestTimeout() {
        if (this.mTargetAudioStatus) {
            int i = this.mSendRetryCount;
            this.mSendRetryCount = i + 1;
            if (i < 2) {
                sendSystemAudioModeRequest();
                return;
            }
        }
        com.android.server.hdmi.HdmiLogger.debug("[T]:wait for <Set System Audio Mode>.", new java.lang.Object[0]);
        setSystemAudioMode(false);
        finishWithCallback(1);
    }

    protected void setSystemAudioMode(boolean mode) {
        tv().setSystemAudioMode(mode);
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    final boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (cmd.getSource() != this.mAvrLogicalAddress) {
            return false;
        }
        switch (this.mState) {
            case 2:
                if (cmd.getOpcode() == 0 && (cmd.getParams()[0] & 255) == 112) {
                    com.android.server.hdmi.HdmiLogger.debug("Failed to start system audio mode request.", new java.lang.Object[0]);
                    setSystemAudioMode(false);
                    finishWithCallback(5);
                    break;
                } else if (cmd.getOpcode() == 114 && com.android.server.hdmi.HdmiUtils.checkCommandSource(cmd, this.mAvrLogicalAddress, TAG)) {
                    boolean receivedStatus = com.android.server.hdmi.HdmiUtils.parseCommandParamSystemAudioStatus(cmd);
                    if (receivedStatus == this.mTargetAudioStatus) {
                        setSystemAudioMode(receivedStatus);
                        finish();
                    } else {
                        com.android.server.hdmi.HdmiLogger.debug("Unexpected system audio mode request:" + receivedStatus, new java.lang.Object[0]);
                        finishWithCallback(5);
                    }
                    break;
                }
                break;
        }
        return false;
    }

    protected void removeSystemAudioActionInProgress() {
        removeActionExcept(com.android.server.hdmi.SystemAudioActionFromTv.class, this);
        removeActionExcept(com.android.server.hdmi.SystemAudioActionFromAvr.class, this);
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    final void handleTimerEvent(int state) {
        if (this.mState != state) {
        }
        switch (this.mState) {
            case 2:
                handleSendSystemAudioModeRequestTimeout();
                break;
        }
    }
}
