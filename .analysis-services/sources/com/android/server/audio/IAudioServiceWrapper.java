package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public interface IAudioServiceWrapper {
    default com.android.server.audio.IAudioServiceExt getExtImpl() {
        return new com.android.server.audio.IAudioServiceExt() { // from class: com.android.server.audio.IAudioServiceWrapper.1
        };
    }

    default com.android.server.audio.AudioDeviceBroker getDeviceBroker() {
        return null;
    }

    default com.android.server.audio.AudioSystemAdapter getAudioSystem() {
        return null;
    }

    default com.android.server.audio.SystemServerAdapter getSystemServer() {
        return null;
    }

    default void setDebugLog(boolean on) {
    }

    default int[] getMaxStreamVolume() {
        return null;
    }

    default int[] getMinStreamVolume() {
        return null;
    }

    default int getSetModeDeathHandlersLength() {
        return 0;
    }

    default int getUidOfSetModeDeathHandler(int index) {
        return 0;
    }

    default int getPidOfSetModeDeathHandler(int index) {
        return 0;
    }

    default int getModeOfSetModeDeathHandler(int index) {
        return 0;
    }

    default android.content.ContentResolver getContentResolver() {
        return null;
    }

    default int getPlatformType() {
        return 0;
    }

    default int getFlagAdjustVolume() {
        return 0;
    }

    default android.media.VolumePolicy getVolumePolicy() {
        return null;
    }

    default boolean getHasVibrator() {
        return false;
    }

    default void setPrevVolDirection(int direction) {
    }

    default void sendMessage(int msg, int existingMsgPolicy, int arg1, int arg2, java.lang.Object obj, int delay) {
    }

    default int[] getStreamVolumeAlias() {
        return null;
    }

    default com.android.server.audio.MediaFocusControl getMediaFocusControl() {
        return null;
    }

    default com.android.server.audio.RecordingActivityMonitor getRecordMonitor() {
        return null;
    }

    default com.android.server.audio.PlaybackActivityMonitor getPlaybackMonitor() {
        return null;
    }

    default void setRingerMode(int ringerMode, java.lang.String caller, boolean external) {
    }

    default int getDeviceForStream(int stream) {
        return 0;
    }

    default void setStreamVolumeInt(int streamType, int index, int device, boolean force, java.lang.String caller, boolean hasModifyAudioSettings) {
    }

    default int getRingerModeInternal() {
        return 0;
    }

    default void avrcpSupportsAbsoluteVolume(java.lang.String address, boolean support) {
    }

    default boolean getAvrcpAbsVolSupported() {
        return false;
    }

    default java.lang.String getVolumeIndexSettingNameForIsMutedForStream(int streamType) {
        return null;
    }

    default void sendMsgForStream(int msg, int existingMsgPolicy, int arg1, int arg2, int streamType, int delay) {
    }

    default void oplusSendMsg(int msg, int existingMsgPolicy, int arg1, int arg2, java.lang.Object obj, int delay) {
    }

    default int getMediaVolumeIndexByDevice(int device) {
        return 0;
    }

    default boolean setIndexForStream(int streamType, int index, int device, java.lang.String caller, boolean hasModifyAudioSettings) {
        return false;
    }

    default int getIndex(int streamType, int device) {
        return 0;
    }

    default boolean mutePhoneForStream(int streamType, boolean state) {
        return false;
    }

    default int getStreamMaxIndex(int streamType) {
        return 0;
    }

    default boolean getStreamMuteState(int streamType) {
        return false;
    }

    default void updateDeviceChangeForMusic(int prevDevices, int newDevice) {
    }

    default boolean getAudioModeOwnerHandlerCheck() {
        return false;
    }

    default int getCurrentAudioModeOwnerPid() {
        return -1;
    }

    default int getCurrentAudioModeOwnerUid() {
        return -1;
    }

    default java.lang.Object getCurrentAudioModeOwnerCb() {
        return null;
    }

    default void setSpatializerEnabled(boolean enable) {
    }

    default void setDesiredHeadTrackingMode(int mode) {
    }

    default void setHeadTrackerEnabled(boolean enabled, android.media.AudioDeviceAttributes ada) {
    }

    default int getDesiredHeadTrackingMode() {
        return 0;
    }

    default com.android.server.audio.SoundDoseHelper getSoundDoseHelper() {
        return null;
    }

    default void broadcastSafeVolume() {
    }

    default void setHoloDeviceSupportState(boolean flag, boolean isBleDevice) {
    }

    default void sendMsgIsLionBleDevice(boolean status) {
    }

    default boolean isUnsupportHoloDevice(java.lang.String device) {
        return false;
    }

    default java.lang.String getCurrentAudioModeOwnerPkgName() {
        return null;
    }

    default boolean getCurrentAudioModeOwnerRecordingStatus() {
        return false;
    }

    default boolean getMicMuteFromApi() {
        return false;
    }

    default int getCurrentDeviceRoute() {
        return 0;
    }
}
