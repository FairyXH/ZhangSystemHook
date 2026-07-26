package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class SystemAudioActionFromTv extends com.android.server.hdmi.SystemAudioAction {
    private static final java.lang.String TAG = "SystemAudioActionFromTv";

    SystemAudioActionFromTv(com.android.server.hdmi.HdmiCecLocalDevice sourceAddress, int avrAddress, boolean targetStatus, android.hardware.hdmi.IHdmiControlCallback callback) {
        super(sourceAddress, avrAddress, targetStatus, callback);
        if (!com.android.server.hdmi.HdmiUtils.verifyAddressType(getSourceAddress(), 0)) {
            android.util.Slog.w(TAG, "Device type mismatch, stop the action.");
            finish();
        }
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        removeSystemAudioActionInProgress();
        sendSystemAudioModeRequest();
        return true;
    }
}
