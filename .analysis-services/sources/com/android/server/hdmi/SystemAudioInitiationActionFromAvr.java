package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class SystemAudioInitiationActionFromAvr extends com.android.server.hdmi.HdmiCecFeatureAction {
    static final int MAX_RETRY_COUNT = 5;
    private static final int STATE_WAITING_FOR_ACTIVE_SOURCE = 1;
    private static final int STATE_WAITING_FOR_TV_SUPPORT = 2;
    private static final java.lang.String TAG = "SystemAudioInitiationActionFromAvr";
    private int mSendRequestActiveSourceRetryCount;
    private int mSendSetSystemAudioModeRetryCount;

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public /* bridge */ /* synthetic */ void addCallback(android.hardware.hdmi.IHdmiControlCallback iHdmiControlCallback) {
        super.addCallback(iHdmiControlCallback);
    }

    SystemAudioInitiationActionFromAvr(com.android.server.hdmi.HdmiCecLocalDevice source) {
        super(source);
        this.mSendRequestActiveSourceRetryCount = 0;
        this.mSendSetSystemAudioModeRetryCount = 0;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        if (audioSystem().getActiveSource().physicalAddress == 65535) {
            this.mState = 1;
            addTimer(this.mState, 2000);
            sendRequestActiveSource();
        } else {
            this.mState = 2;
            queryTvSystemAudioModeSupport();
        }
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        switch (cmd.getOpcode()) {
            case 130:
                if (this.mState == 1) {
                    this.mActionTimer.clearTimerMessage();
                    audioSystem().handleActiveSource(cmd);
                    this.mState = 2;
                    queryTvSystemAudioModeSupport();
                    break;
                }
                break;
        }
        return false;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
        if (this.mState != state) {
        }
        switch (this.mState) {
            case 1:
                handleActiveSourceTimeout();
                break;
        }
    }

    protected void sendRequestActiveSource() {
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildRequestActiveSource(getSourceAddress()), new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.SystemAudioInitiationActionFromAvr$$ExternalSyntheticLambda1
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public final void onSendCompleted(int i) {
                this.f$0.lambda$sendRequestActiveSource$0(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendRequestActiveSource$0(int result) {
        if (result != 0) {
            if (this.mSendRequestActiveSourceRetryCount < 5) {
                this.mSendRequestActiveSourceRetryCount++;
                sendRequestActiveSource();
            } else {
                audioSystem().checkSupportAndSetSystemAudioMode(false);
                finish();
            }
        }
    }

    protected void sendSetSystemAudioMode(final boolean on, final int dest) {
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildSetSystemAudioMode(getSourceAddress(), dest, on), new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.SystemAudioInitiationActionFromAvr$$ExternalSyntheticLambda2
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public final void onSendCompleted(int i) {
                this.f$0.lambda$sendSetSystemAudioMode$1(on, dest, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSetSystemAudioMode$1(boolean on, int dest, int result) {
        if (result != 0) {
            if (this.mSendSetSystemAudioModeRetryCount < 5) {
                this.mSendSetSystemAudioModeRetryCount++;
                sendSetSystemAudioMode(on, dest);
            } else {
                audioSystem().checkSupportAndSetSystemAudioMode(false);
                finish();
            }
        }
    }

    private void handleActiveSourceTimeout() {
        com.android.server.hdmi.HdmiLogger.debug("Cannot get active source.", new java.lang.Object[0]);
        if (audioSystem().mService.isStandbyMessageReceived()) {
            android.util.Slog.d(TAG, "Device is going to sleep, avoid to wake it up.");
            return;
        }
        if (!audioSystem().mService.isPlaybackDevice()) {
            audioSystem().checkSupportAndSetSystemAudioMode(false);
        } else {
            audioSystem().mService.setAndBroadcastActiveSourceFromOneDeviceType(15, getSourcePath(), "SystemAudioInitiationActionFromAvr#handleActiveSourceTimeout()");
            this.mState = 2;
            queryTvSystemAudioModeSupport();
        }
        finish();
    }

    private void queryTvSystemAudioModeSupport() {
        audioSystem().queryTvSystemAudioModeSupport(new com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.TvSystemAudioModeSupportedCallback() { // from class: com.android.server.hdmi.SystemAudioInitiationActionFromAvr$$ExternalSyntheticLambda0
            @Override // com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.TvSystemAudioModeSupportedCallback
            public final void onResult(boolean z) {
                this.f$0.lambda$queryTvSystemAudioModeSupport$2(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$queryTvSystemAudioModeSupport$2(boolean supported) {
        if (supported) {
            if (audioSystem().checkSupportAndSetSystemAudioMode(true)) {
                sendSetSystemAudioMode(true, 15);
            }
            finish();
        } else {
            audioSystem().checkSupportAndSetSystemAudioMode(false);
            finish();
        }
    }
}
