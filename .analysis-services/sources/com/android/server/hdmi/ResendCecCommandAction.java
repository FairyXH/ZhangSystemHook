package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class ResendCecCommandAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int RETRANSMISSION_COUNT = 1;
    static final int SEND_COMMAND_RETRY_MS = 300;
    private static final int STATE_WAIT_FOR_RESEND_COMMAND = 1;
    private static final java.lang.String TAG = "ResendCecCommandAction";
    private final com.android.server.hdmi.HdmiControlService.SendMessageCallback mCallback;
    private final com.android.server.hdmi.HdmiCecMessage mCommand;
    private final com.android.server.hdmi.HdmiControlService.SendMessageCallback mResultCallback;
    private int mRetransmissionCount;

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public /* bridge */ /* synthetic */ void addCallback(android.hardware.hdmi.IHdmiControlCallback iHdmiControlCallback) {
        super.addCallback(iHdmiControlCallback);
    }

    ResendCecCommandAction(com.android.server.hdmi.HdmiCecLocalDevice source, com.android.server.hdmi.HdmiCecMessage command, com.android.server.hdmi.HdmiControlService.SendMessageCallback callback) {
        super(source);
        this.mRetransmissionCount = 0;
        this.mCallback = new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.ResendCecCommandAction.1
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public void onSendCompleted(int result) {
                if (result != 0) {
                    com.android.server.hdmi.ResendCecCommandAction resendCecCommandAction = com.android.server.hdmi.ResendCecCommandAction.this;
                    int i = resendCecCommandAction.mRetransmissionCount;
                    resendCecCommandAction.mRetransmissionCount = i + 1;
                    if (i < 1) {
                        com.android.server.hdmi.ResendCecCommandAction.this.mState = 1;
                        com.android.server.hdmi.ResendCecCommandAction.this.addTimer(com.android.server.hdmi.ResendCecCommandAction.this.mState, 300);
                        return;
                    }
                }
                if (com.android.server.hdmi.ResendCecCommandAction.this.mResultCallback != null) {
                    com.android.server.hdmi.ResendCecCommandAction.this.mResultCallback.onSendCompleted(result);
                }
                com.android.server.hdmi.ResendCecCommandAction.this.finish();
            }
        };
        this.mCommand = command;
        this.mResultCallback = callback;
        this.mState = 1;
        addTimer(this.mState, 300);
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        android.util.Slog.d(TAG, "ResendCecCommandAction started");
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
        if (this.mState != state) {
            android.util.Slog.w(TAG, "Timeout in invalid state:[Expected:" + this.mState + ", Actual:" + state + "]");
        } else if (this.mState == 1) {
            android.util.Slog.d(TAG, "sendCommandWithoutRetries failed, retry");
            sendCommandWithoutRetries(this.mCommand, this.mCallback);
        }
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage command) {
        return false;
    }
}
