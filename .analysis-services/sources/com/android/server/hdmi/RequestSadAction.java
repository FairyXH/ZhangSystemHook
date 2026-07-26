package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class RequestSadAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int MAX_SAD_PER_REQUEST = 4;
    private static final int RETRY_COUNTER_MAX = 1;
    private static final int STATE_WAITING_FOR_REPORT_SAD = 1;
    private static final java.lang.String TAG = "RequestSadAction";
    private final com.android.server.hdmi.RequestSadAction.RequestSadCallback mCallback;
    private final java.util.List<java.lang.Integer> mCecCodecsToQuery;
    private int mQueriedSadCount;
    private final java.util.List<byte[]> mSupportedSads;
    private final int mTargetAddress;
    private int mTimeoutRetry;

    interface RequestSadCallback {
        void onRequestSadDone(java.util.List<byte[]> list);
    }

    RequestSadAction(com.android.server.hdmi.HdmiCecLocalDevice source, int targetAddress, com.android.server.hdmi.RequestSadAction.RequestSadCallback callback) {
        super(source);
        this.mCecCodecsToQuery = new java.util.ArrayList();
        this.mSupportedSads = new java.util.ArrayList();
        this.mQueriedSadCount = 0;
        this.mTimeoutRetry = 0;
        this.mTargetAddress = targetAddress;
        this.mCallback = (com.android.server.hdmi.RequestSadAction.RequestSadCallback) java.util.Objects.requireNonNull(callback);
        com.android.server.hdmi.HdmiCecConfig hdmiCecConfig = localDevice().mService.getHdmiCecConfig();
        if (hdmiCecConfig.getIntValue("query_sad_lpcm") == 1) {
            this.mCecCodecsToQuery.add(1);
        }
        if (hdmiCecConfig.getIntValue("query_sad_dd") == 1) {
            this.mCecCodecsToQuery.add(2);
        }
        if (hdmiCecConfig.getIntValue("query_sad_mpeg1") == 1) {
            this.mCecCodecsToQuery.add(3);
        }
        if (hdmiCecConfig.getIntValue("query_sad_mp3") == 1) {
            this.mCecCodecsToQuery.add(4);
        }
        if (hdmiCecConfig.getIntValue("query_sad_mpeg2") == 1) {
            this.mCecCodecsToQuery.add(5);
        }
        if (hdmiCecConfig.getIntValue("query_sad_aac") == 1) {
            this.mCecCodecsToQuery.add(6);
        }
        if (hdmiCecConfig.getIntValue("query_sad_dts") == 1) {
            this.mCecCodecsToQuery.add(7);
        }
        if (hdmiCecConfig.getIntValue("query_sad_atrac") == 1) {
            this.mCecCodecsToQuery.add(8);
        }
        if (hdmiCecConfig.getIntValue("query_sad_onebitaudio") == 1) {
            this.mCecCodecsToQuery.add(9);
        }
        if (hdmiCecConfig.getIntValue("query_sad_ddp") == 1) {
            this.mCecCodecsToQuery.add(10);
        }
        if (hdmiCecConfig.getIntValue("query_sad_dtshd") == 1) {
            this.mCecCodecsToQuery.add(11);
        }
        if (hdmiCecConfig.getIntValue("query_sad_truehd") == 1) {
            this.mCecCodecsToQuery.add(12);
        }
        if (hdmiCecConfig.getIntValue("query_sad_dst") == 1) {
            this.mCecCodecsToQuery.add(13);
        }
        if (hdmiCecConfig.getIntValue("query_sad_wmapro") == 1) {
            this.mCecCodecsToQuery.add(14);
        }
        if (hdmiCecConfig.getIntValue("query_sad_max") == 1) {
            this.mCecCodecsToQuery.add(15);
        }
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        querySad();
        return true;
    }

    private void querySad() {
        if (this.mQueriedSadCount >= this.mCecCodecsToQuery.size()) {
            wrapUpAndFinish();
            return;
        }
        int[] codecsToQuery = this.mCecCodecsToQuery.subList(this.mQueriedSadCount, java.lang.Math.min(this.mCecCodecsToQuery.size(), this.mQueriedSadCount + 4)).stream().mapToInt(new java.util.function.ToIntFunction() { // from class: com.android.server.hdmi.RequestSadAction$$ExternalSyntheticLambda0
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(java.lang.Object obj) {
                return ((java.lang.Integer) obj).intValue();
            }
        }).toArray();
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildRequestShortAudioDescriptor(getSourceAddress(), this.mTargetAddress, codecsToQuery));
        this.mState = 1;
        addTimer(this.mState, 2000);
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (this.mState != 1 || this.mTargetAddress != cmd.getSource()) {
            return false;
        }
        if (cmd.getOpcode() == 163) {
            if (cmd.getParams() == null || cmd.getParams().length == 0 || cmd.getParams().length % 3 != 0) {
                return true;
            }
            for (int i = 0; i < cmd.getParams().length - 2; i += 3) {
                if (isValidCodec(cmd.getParams()[i])) {
                    byte[] sad = {cmd.getParams()[i], cmd.getParams()[i + 1], cmd.getParams()[i + 2]};
                    updateResult(sad);
                } else {
                    android.util.Slog.w(TAG, "Dropped invalid codec " + ((int) cmd.getParams()[i]) + ".");
                }
            }
            int i2 = this.mQueriedSadCount;
            this.mQueriedSadCount = i2 + 4;
            this.mTimeoutRetry = 0;
            querySad();
            return true;
        }
        if (cmd.getOpcode() == 0 && (cmd.getParams()[0] & 255) == 164) {
            if ((cmd.getParams()[1] & 255) == 0) {
                wrapUpAndFinish();
                return true;
            }
            if ((cmd.getParams()[1] & 255) == 3) {
                this.mQueriedSadCount += 4;
                this.mTimeoutRetry = 0;
                querySad();
                return true;
            }
        }
        return false;
    }

    private boolean isValidCodec(byte codec) {
        int audioFormatCode;
        return (codec & 128) == 0 && (audioFormatCode = (codec & 120) >> 3) > 0 && audioFormatCode <= 15;
    }

    private void updateResult(byte[] sad) {
        this.mSupportedSads.add(sad);
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
        if (this.mState == state && state == 1) {
            int i = this.mTimeoutRetry + 1;
            this.mTimeoutRetry = i;
            if (i <= 1) {
                querySad();
            } else {
                wrapUpAndFinish();
            }
        }
    }

    private void wrapUpAndFinish() {
        this.mCallback.onRequestSadDone(this.mSupportedSads);
        finish();
    }
}
