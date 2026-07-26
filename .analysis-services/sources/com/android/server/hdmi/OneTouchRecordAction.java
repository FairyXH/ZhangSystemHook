package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class OneTouchRecordAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int RECORD_STATUS_TIMEOUT_MS = 120000;
    private static final int STATE_RECORDING_IN_PROGRESS = 2;
    private static final int STATE_WAITING_FOR_RECORD_STATUS = 1;
    private static final java.lang.String TAG = "OneTouchRecordAction";
    private final byte[] mRecordSource;
    private final int mRecorderAddress;

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public /* bridge */ /* synthetic */ void addCallback(android.hardware.hdmi.IHdmiControlCallback iHdmiControlCallback) {
        super.addCallback(iHdmiControlCallback);
    }

    OneTouchRecordAction(com.android.server.hdmi.HdmiCecLocalDevice source, int recorderAddress, byte[] recordSource) {
        super(source);
        this.mRecorderAddress = recorderAddress;
        this.mRecordSource = recordSource;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        sendRecordOn();
        return true;
    }

    private void sendRecordOn() {
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildRecordOn(getSourceAddress(), this.mRecorderAddress, this.mRecordSource), new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.OneTouchRecordAction.1
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public void onSendCompleted(int error) {
                if (error != 0) {
                    com.android.server.hdmi.OneTouchRecordAction.this.tv().announceOneTouchRecordResult(com.android.server.hdmi.OneTouchRecordAction.this.mRecorderAddress, 49);
                    com.android.server.hdmi.OneTouchRecordAction.this.finish();
                }
            }
        });
        this.mState = 1;
        addTimer(this.mState, 120000);
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (this.mState != 1 || this.mRecorderAddress != cmd.getSource()) {
            return false;
        }
        switch (cmd.getOpcode()) {
            case 10:
                return handleRecordStatus(cmd);
            default:
                return false;
        }
    }

    private boolean handleRecordStatus(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (cmd.getSource() != this.mRecorderAddress) {
            return false;
        }
        int recordStatus = cmd.getParams()[0];
        tv().announceOneTouchRecordResult(this.mRecorderAddress, recordStatus);
        android.util.Slog.i(TAG, "Got record status:" + recordStatus + " from " + cmd.getSource());
        switch (recordStatus) {
            case 1:
            case 2:
            case 3:
            case 4:
                this.mState = 2;
                this.mActionTimer.clearTimerMessage();
                return true;
            default:
                finish();
                return true;
        }
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
        if (this.mState != state) {
            android.util.Slog.w(TAG, "Timeout in invalid state:[Expected:" + this.mState + ", Actual:" + state + "]");
        } else {
            tv().announceOneTouchRecordResult(this.mRecorderAddress, 49);
            finish();
        }
    }

    int getRecorderAddress() {
        return this.mRecorderAddress;
    }
}
