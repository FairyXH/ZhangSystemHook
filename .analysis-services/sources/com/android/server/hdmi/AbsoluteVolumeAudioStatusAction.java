package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class AbsoluteVolumeAudioStatusAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int STATE_MONITOR_AUDIO_STATUS = 2;
    private static final int STATE_WAIT_FOR_INITIAL_AUDIO_STATUS = 1;
    private static final java.lang.String TAG = "AbsoluteVolumeAudioStatusAction";
    private int mInitialAudioStatusRetriesLeft;
    private com.android.server.hdmi.AudioStatus mLastAudioStatus;
    private final int mTargetAddress;

    AbsoluteVolumeAudioStatusAction(com.android.server.hdmi.HdmiCecLocalDevice source, int targetAddress) {
        super(source);
        this.mInitialAudioStatusRetriesLeft = 2;
        this.mTargetAddress = targetAddress;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        this.mState = 1;
        sendGiveAudioStatus();
        return true;
    }

    void updateVolume(int volumeIndex) {
        this.mLastAudioStatus = new com.android.server.hdmi.AudioStatus(volumeIndex, this.mLastAudioStatus.getMute());
    }

    private void sendGiveAudioStatus() {
        addTimer(this.mState, 2000);
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildGiveAudioStatus(getSourceAddress(), this.mTargetAddress));
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        switch (cmd.getOpcode()) {
            case 122:
                return handleReportAudioStatus(cmd);
            default:
                return false;
        }
    }

    void requestAndUpdateAudioStatus() {
        if (this.mState == 2) {
            sendGiveAudioStatus();
        }
    }

    private boolean handleReportAudioStatus(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (this.mTargetAddress != cmd.getSource() || cmd.getParams().length == 0) {
            return false;
        }
        boolean mute = com.android.server.hdmi.HdmiUtils.isAudioStatusMute(cmd);
        int volume = com.android.server.hdmi.HdmiUtils.getAudioStatusVolume(cmd);
        if (volume == -1) {
            return true;
        }
        com.android.server.hdmi.AudioStatus audioStatus = new com.android.server.hdmi.AudioStatus(volume, mute);
        if (this.mState == 1) {
            localDevice().getService().enableAbsoluteVolumeBehavior(audioStatus);
            this.mState = 2;
        } else if (this.mState == 2) {
            boolean updateVolume = audioStatus.getVolume() != this.mLastAudioStatus.getVolume();
            if (updateVolume) {
                localDevice().getService().notifyAvbVolumeChange(audioStatus.getVolume());
            }
            if (audioStatus.getMute() != this.mLastAudioStatus.getMute() || updateVolume || localDevice().getService().isTvDevice()) {
                localDevice().getService().notifyAvbMuteChange(audioStatus.getMute());
            }
        }
        this.mLastAudioStatus = audioStatus;
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
        if (this.mState == state && this.mInitialAudioStatusRetriesLeft > 0) {
            this.mInitialAudioStatusRetriesLeft--;
            sendGiveAudioStatus();
        }
    }
}
