package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class TimerRecordingAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int STATE_WAITING_FOR_TIMER_STATUS = 1;
    private static final java.lang.String TAG = "TimerRecordingAction";
    private static final int TIMER_STATUS_TIMEOUT_MS = 120000;
    private final byte[] mRecordSource;
    private final int mRecorderAddress;
    private final int mSourceType;

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public /* bridge */ /* synthetic */ void addCallback(android.hardware.hdmi.IHdmiControlCallback iHdmiControlCallback) {
        super.addCallback(iHdmiControlCallback);
    }

    TimerRecordingAction(com.android.server.hdmi.HdmiCecLocalDevice source, int recorderAddress, int sourceType, byte[] recordSource) {
        super(source);
        this.mRecorderAddress = recorderAddress;
        this.mSourceType = sourceType;
        this.mRecordSource = recordSource;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        sendTimerMessage();
        return true;
    }

    private void sendTimerMessage() {
        com.android.server.hdmi.HdmiCecMessage message;
        switch (this.mSourceType) {
            case 1:
                message = com.android.server.hdmi.HdmiCecMessageBuilder.buildSetDigitalTimer(getSourceAddress(), this.mRecorderAddress, this.mRecordSource);
                break;
            case 2:
                message = com.android.server.hdmi.HdmiCecMessageBuilder.buildSetAnalogueTimer(getSourceAddress(), this.mRecorderAddress, this.mRecordSource);
                break;
            case 3:
                message = com.android.server.hdmi.HdmiCecMessageBuilder.buildSetExternalTimer(getSourceAddress(), this.mRecorderAddress, this.mRecordSource);
                break;
            default:
                tv().announceTimerRecordingResult(this.mRecorderAddress, 2);
                finish();
                return;
        }
        sendCommand(message, new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.TimerRecordingAction.1
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public void onSendCompleted(int error) {
                if (error != 0) {
                    com.android.server.hdmi.TimerRecordingAction.this.tv().announceTimerRecordingResult(com.android.server.hdmi.TimerRecordingAction.this.mRecorderAddress, 1);
                    com.android.server.hdmi.TimerRecordingAction.this.finish();
                } else {
                    com.android.server.hdmi.TimerRecordingAction.this.mState = 1;
                    com.android.server.hdmi.TimerRecordingAction.this.addTimer(com.android.server.hdmi.TimerRecordingAction.this.mState, 120000);
                }
            }
        });
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (this.mState != 1 || cmd.getSource() != this.mRecorderAddress) {
            return false;
        }
        switch (cmd.getOpcode()) {
            case 0:
                return handleFeatureAbort(cmd);
            case 53:
                return handleTimerStatus(cmd);
            default:
                return false;
        }
    }

    private boolean handleTimerStatus(com.android.server.hdmi.HdmiCecMessage cmd) {
        byte[] timerStatusData = cmd.getParams();
        if (timerStatusData.length == 1 || timerStatusData.length == 3) {
            tv().announceTimerRecordingResult(this.mRecorderAddress, bytesToInt(timerStatusData));
            android.util.Slog.i(TAG, "Received [Timer Status Data]:" + java.util.Arrays.toString(timerStatusData));
        } else {
            android.util.Slog.w(TAG, "Invalid [Timer Status Data]:" + java.util.Arrays.toString(timerStatusData));
        }
        finish();
        return true;
    }

    private boolean handleFeatureAbort(com.android.server.hdmi.HdmiCecMessage cmd) {
        byte[] params = cmd.getParams();
        int messageType = params[0] & 255;
        switch (messageType) {
            case 52:
            case 151:
            case 162:
                int reason = params[1] & 255;
                android.util.Slog.i(TAG, "[Feature Abort] for " + messageType + " reason:" + reason);
                tv().announceTimerRecordingResult(this.mRecorderAddress, 1);
                finish();
                return true;
            default:
                return false;
        }
    }

    private static int bytesToInt(byte[] data) {
        if (data.length > 4) {
            throw new java.lang.IllegalArgumentException("Invalid data size:" + java.util.Arrays.toString(data));
        }
        int result = 0;
        for (int i = 0; i < data.length; i++) {
            int shift = (3 - i) * 8;
            result |= (data[i] & 255) << shift;
        }
        return result;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
        if (this.mState != state) {
            android.util.Slog.w(TAG, "Timeout in invalid state:[Expected:" + this.mState + ", Actual:" + state + "]");
        } else {
            tv().announceTimerRecordingResult(this.mRecorderAddress, 1);
            finish();
        }
    }
}
