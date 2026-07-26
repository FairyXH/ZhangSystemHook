package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class SystemAudioAutoInitiationAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    static final int RETRIES_ON_TIMEOUT = 1;
    private static final int STATE_WAITING_FOR_SYSTEM_AUDIO_MODE_STATUS = 1;
    private final int mAvrAddress;
    private int mRetriesOnTimeOut;

    SystemAudioAutoInitiationAction(com.android.server.hdmi.HdmiCecLocalDevice source, int avrAddress) {
        super(source);
        this.mRetriesOnTimeOut = 1;
        this.mAvrAddress = avrAddress;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        this.mState = 1;
        addTimer(this.mState, 2000);
        sendGiveSystemAudioModeStatus();
        return true;
    }

    private void sendGiveSystemAudioModeStatus() {
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildGiveSystemAudioModeStatus(getSourceAddress(), this.mAvrAddress), new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.SystemAudioAutoInitiationAction.1
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public void onSendCompleted(int error) {
                if (error != 0) {
                    com.android.server.hdmi.SystemAudioAutoInitiationAction.this.handleSystemAudioModeStatusTimeout();
                }
            }
        });
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (this.mState != 1 || this.mAvrAddress != cmd.getSource() || cmd.getOpcode() != 126) {
            return false;
        }
        handleSystemAudioModeStatusMessage(com.android.server.hdmi.HdmiUtils.parseCommandParamSystemAudioStatus(cmd));
        return true;
    }

    private void handleSystemAudioModeStatusMessage(boolean currentSystemAudioMode) {
        if (!canChangeSystemAudio()) {
            com.android.server.hdmi.HdmiLogger.debug("Cannot change system audio mode in auto initiation action.", new java.lang.Object[0]);
            finish();
            return;
        }
        boolean targetSystemAudioMode = tv().isSystemAudioControlFeatureEnabled();
        if (currentSystemAudioMode != targetSystemAudioMode) {
            addAndStartAction(new com.android.server.hdmi.SystemAudioActionFromTv(tv(), this.mAvrAddress, targetSystemAudioMode, null));
        } else {
            tv().setSystemAudioMode(targetSystemAudioMode);
        }
        finish();
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
        if (this.mState != state) {
        }
        switch (this.mState) {
            case 1:
                if (this.mRetriesOnTimeOut > 0) {
                    this.mRetriesOnTimeOut--;
                    addTimer(this.mState, 2000);
                    sendGiveSystemAudioModeStatus();
                } else {
                    handleSystemAudioModeStatusTimeout();
                }
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSystemAudioModeStatusTimeout() {
        if (!canChangeSystemAudio()) {
            com.android.server.hdmi.HdmiLogger.debug("Cannot change system audio mode in auto initiation action.", new java.lang.Object[0]);
            finish();
        } else {
            addAndStartAction(new com.android.server.hdmi.SystemAudioActionFromTv(tv(), this.mAvrAddress, tv().isSystemAudioControlFeatureEnabled(), null));
            finish();
        }
    }

    private boolean canChangeSystemAudio() {
        return (tv().hasAction(com.android.server.hdmi.SystemAudioActionFromTv.class) || tv().hasAction(com.android.server.hdmi.SystemAudioActionFromAvr.class)) ? false : true;
    }
}
