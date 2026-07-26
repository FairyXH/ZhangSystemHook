package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class SystemAudioActionFromAvr extends com.android.server.hdmi.SystemAudioAction {
    private static final java.lang.String TAG = "SystemAudioActionFromAvr";

    SystemAudioActionFromAvr(com.android.server.hdmi.HdmiCecLocalDevice source, int avrAddress, boolean targetStatus, android.hardware.hdmi.IHdmiControlCallback callback) {
        super(source, avrAddress, targetStatus, callback);
        if (!com.android.server.hdmi.HdmiUtils.verifyAddressType(getSourceAddress(), 0)) {
            android.util.Slog.w(TAG, "Device type mismatch, stop the action.");
            finish();
        }
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        removeSystemAudioActionInProgress();
        handleSystemAudioActionFromAvr();
        return true;
    }

    private void handleSystemAudioActionFromAvr() {
        if (this.mTargetAudioStatus == tv().isSystemAudioActivated()) {
            finishWithCallback(0);
            return;
        }
        if (tv().isProhibitMode()) {
            sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildFeatureAbortCommand(getSourceAddress(), this.mAvrLogicalAddress, 114, 4));
            this.mTargetAudioStatus = false;
            sendSystemAudioModeRequest();
            return;
        }
        removeAction(com.android.server.hdmi.SystemAudioAutoInitiationAction.class);
        if (this.mTargetAudioStatus) {
            setSystemAudioMode(true);
            finish();
        } else {
            setSystemAudioMode(false);
            finishWithCallback(0);
        }
    }
}
