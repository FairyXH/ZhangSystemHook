package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class VolumeControlAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int MAX_VOLUME = 100;
    private static final int STATE_WAIT_FOR_NEXT_VOLUME_PRESS = 1;
    private static final java.lang.String TAG = "VolumeControlAction";
    private static final int UNKNOWN_AVR_VOLUME = -1;
    private final int mAvrAddress;
    private boolean mIsVolumeUp;
    private boolean mLastAvrMute;
    private int mLastAvrVolume;
    private long mLastKeyUpdateTime;
    private boolean mSentKeyPressed;

    public static int scaleToCecVolume(int volume, int scale) {
        return (volume * 100) / scale;
    }

    public static int scaleToCustomVolume(int cecVolume, int scale) {
        return (cecVolume * scale) / 100;
    }

    VolumeControlAction(com.android.server.hdmi.HdmiCecLocalDevice source, int avrAddress, boolean isVolumeUp) {
        super(source);
        this.mAvrAddress = avrAddress;
        this.mIsVolumeUp = isVolumeUp;
        this.mLastAvrVolume = -1;
        this.mLastAvrMute = false;
        this.mSentKeyPressed = false;
        updateLastKeyUpdateTime();
    }

    private void updateLastKeyUpdateTime() {
        this.mLastKeyUpdateTime = java.lang.System.currentTimeMillis();
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        this.mState = 1;
        sendVolumeKeyPressed();
        resetTimer();
        return true;
    }

    private void sendVolumeKeyPressed() {
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildUserControlPressed(getSourceAddress(), this.mAvrAddress, this.mIsVolumeUp ? 65 : 66));
        this.mSentKeyPressed = true;
    }

    private void resetTimer() {
        this.mActionTimer.clearTimerMessage();
        addTimer(1, 300);
    }

    void handleVolumeChange(boolean isVolumeUp) {
        if (this.mIsVolumeUp != isVolumeUp) {
            com.android.server.hdmi.HdmiLogger.debug("Volume Key Status Changed[old:%b new:%b]", java.lang.Boolean.valueOf(this.mIsVolumeUp), java.lang.Boolean.valueOf(isVolumeUp));
            sendVolumeKeyReleased();
            this.mIsVolumeUp = isVolumeUp;
            sendVolumeKeyPressed();
            resetTimer();
        }
        updateLastKeyUpdateTime();
    }

    private void sendVolumeKeyReleased() {
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildUserControlReleased(getSourceAddress(), this.mAvrAddress));
        this.mSentKeyPressed = false;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        if (this.mState != 1 || cmd.getSource() != this.mAvrAddress) {
            return false;
        }
        switch (cmd.getOpcode()) {
            case 0:
                return handleFeatureAbort(cmd);
            case 122:
                return handleReportAudioStatus(cmd);
            default:
                return false;
        }
    }

    private boolean handleReportAudioStatus(com.android.server.hdmi.HdmiCecMessage cmd) {
        boolean mute = com.android.server.hdmi.HdmiUtils.isAudioStatusMute(cmd);
        int volume = com.android.server.hdmi.HdmiUtils.getAudioStatusVolume(cmd);
        this.mLastAvrVolume = volume;
        this.mLastAvrMute = mute;
        if (shouldUpdateAudioVolume(mute)) {
            com.android.server.hdmi.HdmiLogger.debug("Force volume change[mute:%b, volume=%d]", java.lang.Boolean.valueOf(mute), java.lang.Integer.valueOf(volume));
            tv().setAudioStatus(mute, volume);
            this.mLastAvrVolume = -1;
            this.mLastAvrMute = false;
            return true;
        }
        return true;
    }

    private boolean shouldUpdateAudioVolume(boolean mute) {
        if (mute) {
            return true;
        }
        com.android.server.hdmi.AudioManagerWrapper audioManager = tv().getService().getAudioManager();
        int currentVolume = audioManager.getStreamVolume(3);
        if (!this.mIsVolumeUp) {
            return currentVolume == 0;
        }
        int maxVolume = audioManager.getStreamMaxVolume(3);
        return currentVolume == maxVolume;
    }

    private boolean handleFeatureAbort(com.android.server.hdmi.HdmiCecMessage cmd) {
        int originalOpcode = cmd.getParams()[0] & 255;
        if (originalOpcode != 68) {
            return false;
        }
        finish();
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    protected void clear() {
        super.clear();
        if (this.mSentKeyPressed) {
            sendVolumeKeyReleased();
        }
        if (this.mLastAvrVolume != -1) {
            tv().setAudioStatus(this.mLastAvrMute, this.mLastAvrVolume);
            this.mLastAvrVolume = -1;
            this.mLastAvrMute = false;
        }
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
        if (state != 1) {
            return;
        }
        if (java.lang.System.currentTimeMillis() - this.mLastKeyUpdateTime >= 300) {
            finish();
        } else {
            sendVolumeKeyPressed();
            resetTimer();
        }
    }
}
