package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class RequestActiveSourceAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int MAX_SEND_RETRY_COUNT = 1;
    private static final int STATE_WAIT_FOR_ACTIVE_SOURCE = 2;
    private static final int STATE_WAIT_FOR_LAUNCHERX_API_CALL = 1;
    private static final java.lang.String TAG = "RequestActiveSourceAction";
    private int mSendRetryCount;

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public /* bridge */ /* synthetic */ void addCallback(android.hardware.hdmi.IHdmiControlCallback iHdmiControlCallback) {
        super.addCallback(iHdmiControlCallback);
    }

    RequestActiveSourceAction(com.android.server.hdmi.HdmiCecLocalDevice source, android.hardware.hdmi.IHdmiControlCallback callback) {
        super(source, callback);
        this.mSendRetryCount = 0;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        android.util.Slog.v(TAG, "RequestActiveSourceAction started.");
        this.mState = 1;
        addTimer(this.mState, 4000);
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (cmd.getOpcode() == 130) {
            finishWithCallback(0);
        }
        return false;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
        if (this.mState != state) {
        }
        switch (this.mState) {
            case 1:
                this.mState = 2;
                sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildRequestActiveSource(getSourceAddress()));
                addTimer(this.mState, 2000);
                break;
            case 2:
                int i = this.mSendRetryCount;
                this.mSendRetryCount = i + 1;
                if (i < 1) {
                    sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildRequestActiveSource(getSourceAddress()));
                    addTimer(this.mState, 2000);
                } else {
                    finishWithCallback(1);
                }
                break;
        }
    }
}
