package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class SendKeyAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int AWAIT_LONGPRESS_MS = 400;
    private static final int AWAIT_RELEASE_KEY_MS = 1000;
    private static final int STATE_CHECKING_LONGPRESS = 1;
    private static final int STATE_PROCESSING_KEYCODE = 2;
    private static final java.lang.String TAG = "SendKeyAction";
    private int mLastKeycode;
    private long mLastSendKeyTime;
    private final int mTargetAddress;

    SendKeyAction(com.android.server.hdmi.HdmiCecLocalDevice source, int targetAddress, int keycode) {
        super(source);
        this.mTargetAddress = targetAddress;
        this.mLastKeycode = keycode;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean start() {
        sendKeyDown(this.mLastKeycode);
        this.mLastSendKeyTime = getCurrentTime();
        if (!com.android.server.hdmi.HdmiCecKeycode.isRepeatableKey(this.mLastKeycode)) {
            sendKeyUp();
            finish();
            return true;
        }
        this.mState = 1;
        addTimer(this.mState, 400);
        return true;
    }

    private long getCurrentTime() {
        return java.lang.System.currentTimeMillis();
    }

    void processKeyEvent(int keycode, boolean isPressed) {
        if (this.mState != 1 && this.mState != 2) {
            android.util.Slog.w(TAG, "Not in a valid state");
            return;
        }
        if (isPressed) {
            if (keycode != this.mLastKeycode) {
                sendKeyDown(keycode);
                this.mLastSendKeyTime = getCurrentTime();
                if (!com.android.server.hdmi.HdmiCecKeycode.isRepeatableKey(keycode)) {
                    sendKeyUp();
                    finish();
                    return;
                }
            } else if (getCurrentTime() - this.mLastSendKeyTime >= 300) {
                sendKeyDown(keycode);
                this.mLastSendKeyTime = getCurrentTime();
            }
            this.mActionTimer.clearTimerMessage();
            addTimer(this.mState, 1000);
            this.mLastKeycode = keycode;
            return;
        }
        if (keycode == this.mLastKeycode) {
            sendKeyUp();
            finish();
        }
    }

    private void sendKeyDown(int keycode) {
        byte[] cecKeycodeAndParams = com.android.server.hdmi.HdmiCecKeycode.androidKeyToCecKey(keycode);
        if (cecKeycodeAndParams == null) {
            return;
        }
        if (this.mTargetAddress == 5 && localDevice().getDeviceInfo().getLogicalAddress() != 0) {
            sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildUserControlPressed(getSourceAddress(), this.mTargetAddress, cecKeycodeAndParams), new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.SendKeyAction.1
                @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
                public void onSendCompleted(int error) {
                    if (error == 1) {
                        com.android.server.hdmi.HdmiLogger.debug("AVR did not acknowledge <User Control Pressed>", new java.lang.Object[0]);
                        com.android.server.hdmi.SendKeyAction.this.localDevice().mService.setSystemAudioActivated(false);
                    }
                }
            });
        } else {
            sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildUserControlPressed(getSourceAddress(), this.mTargetAddress, cecKeycodeAndParams));
        }
    }

    private void sendKeyUp() {
        if (com.android.server.hdmi.HdmiCecKeycode.isVolumeKeycode(this.mLastKeycode) && localDevice().getService().isAbsoluteVolumeBehaviorEnabled()) {
            sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildUserControlReleased(getSourceAddress(), this.mTargetAddress), new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.SendKeyAction$$ExternalSyntheticLambda0
                @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
                public final void onSendCompleted(int i) {
                    this.f$0.lambda$sendKeyUp$0(i);
                }
            });
        } else {
            sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildUserControlReleased(getSourceAddress(), this.mTargetAddress));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendKeyUp$0(int __) {
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildGiveAudioStatus(getSourceAddress(), localDevice().findAudioReceiverAddress()));
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        return false;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public void handleTimerEvent(int state) {
        switch (this.mState) {
            case 1:
                this.mActionTimer.clearTimerMessage();
                this.mState = 2;
                sendKeyDown(this.mLastKeycode);
                this.mLastSendKeyTime = getCurrentTime();
                addTimer(this.mState, 1000);
                break;
            case 2:
                sendKeyUp();
                finish();
                break;
            default:
                android.util.Slog.w(TAG, "Not in a valid state");
                break;
        }
    }
}
