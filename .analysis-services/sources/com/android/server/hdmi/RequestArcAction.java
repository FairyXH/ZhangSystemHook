package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
abstract class RequestArcAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    protected static final int STATE_WATING_FOR_REQUEST_ARC_REQUEST_RESPONSE = 1;
    private static final java.lang.String TAG = "RequestArcAction";
    protected final int mAvrAddress;

    RequestArcAction(com.android.server.hdmi.HdmiCecLocalDevice source, int avrAddress, android.hardware.hdmi.IHdmiControlCallback callback) {
        super(source, callback);
        if (!com.android.server.hdmi.HdmiUtils.verifyAddressType(getSourceAddress(), 0) || !com.android.server.hdmi.HdmiUtils.verifyAddressType(avrAddress, 5)) {
            android.util.Slog.w(TAG, "Device type mismatch, stop the action.");
            finish();
        }
        this.mAvrAddress = avrAddress;
    }

    RequestArcAction(com.android.server.hdmi.HdmiCecLocalDevice source, int avrAddress) {
        this(source, avrAddress, null);
    }

    protected final void disableArcTransmission() {
        com.android.server.hdmi.SetArcTransmissionStateAction action = new com.android.server.hdmi.SetArcTransmissionStateAction(localDevice(), this.mAvrAddress, false);
        addAndStartAction(action);
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    final void handleTimerEvent(int state) {
        if (this.mState != state || state != 1) {
            return;
        }
        com.android.server.hdmi.HdmiLogger.debug("[T] RequestArcAction.", new java.lang.Object[0]);
        disableArcTransmission();
        finishWithCallback(1);
    }
}
