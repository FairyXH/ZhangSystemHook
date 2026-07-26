package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public interface IAudioServiceExt {
    public static final java.lang.String ACTION_ATLAS_STREAM_MUTE_STATE = "atlas_stream_mute_state";
    public static final java.lang.String ACTION_AUDIO_DEVICE_ROUTE_CHANGED = "android.media.ACTION_AUDIO_DEVICE_ROUTE_CHANGED";
    public static final java.lang.String ALERT_SLIDER_MANAGER = "AlertSliderManager";
    public static final java.lang.String AUDIO_INPUT_CHANNEL = "oplus_customize_sound_input_channel";
    public static final int CHECK_INACTIVE_ROUTE_CLIENT = 1;
    public static final int CHECK_MICROPHONE_MUTE_DELAY = 6000;
    public static final int CHECK_MODE_CHANGE_FOR_ROUTE = 0;
    public static final int CHECK_PRIVACY_CALL_DELAY = 500;
    public static final int CHECK_ROUTE_FOR_MODE_CHANGE = 0;
    public static final int CHECK_ROUTING_CLIENT_ACTIVE_DELAY = 6000;
    public static final int CHECK_SELECT_ROUTE_CLIENT = 1;
    public static final int CHECK_SELECT_ROUTE_ISAPPCALL = 2;
    public static final int CHECK_TIMEOUT_INACTIVE_ROUTE_CLIENT = 2;
    public static final int CHECK_VOCAL_PROMINENCE_DELAY = 500;
    public static final java.lang.String EXTRA_ATLAS_STREAM_MUTE_STATE = "ATLAS_STREAM_MUTE_STATE";
    public static final java.lang.String GET_DEVICE_TYPE_FROM_ATLAS = "android.media.EXTRA_DEVICE_TYPE";
    public static final int KEY_VOICE_CALL_NC_STATE_CLOSE = 0;
    public static final int MSG_CHECK_INACTIVE_ROUTE_CLIENT = 82;
    public static final int MSG_CHECK_MICROPHONE_MUTE = 83;
    public static final int MSG_MUTE_PHONE = 62;
    public static final int MSG_OTHER_CHECK_PRIVACY_CALL = 112;
    public static final int MSG_OTHER_CHECK_VOCAL_PROMINENCE = 116;
    public static final int MSG_PERSIST_SUPPER_VOLUME = 64;
    public static final int MSG_PER_SPEAKER_MUSIC_VOLUME = 63;
    public static final int MSG_SET_PRIVACY_CALL_SOFTWARE_MODE_STATE = 120;
    public static final int MSG_VOLUME_FADE = 61;
    public static final java.lang.String OPLUS_MODE_RINGER = "oplus_mode_ringer";
    public static final int OPLUS_PRIVACY_CALL_SOFTWARE_MODE_CLOSE = 0;
    public static final int OPLUS_PRIVACY_CALL_SOFTWARE_MODE_OPEN = 1;
    public static final java.lang.String PHONE_MUTE_STATE_FOR_CUSTOM = "phone_mute_state_for_custom";
    public static final int RECOVER_ROUTE_FOR_SCO = 0;
    public static final int STEPLESS_VOLUME_INDEX_FLAG = 4096;
    public static final int STEPLESS_VOLUME_INDEX_MAX_LOWER_BOUND = 100;
    public static final int STEPLESS_VOLUME_SCALE_FACTOR = 10;
    public static final int STEPLESS_VOLUME_STEP = 100;
    public static final int STEPLESS_VSS_VOLUME_INDEX_MAX_LOWER_BOUND = 1000;

    default void init(android.content.Context context) {
    }

    default void oplusSetParameters(java.lang.String keyValuePairs, int uid) {
    }

    default java.lang.String oplusGetParameters(java.lang.String keyValuePairs, int uid) {
        return null;
    }

    default void initDeviceBroker() {
    }

    default boolean isAdjustVolumeForbidden(int streamType, java.lang.String callingPackage) {
        return false;
    }

    default void resetSystemVolume() {
    }

    default int oplusGetNewRingMode(int newRingerMode) {
        return 0;
    }

    default int oplusCheckForRingerModeChangeHelper(int oldIndex, int direction, int step, java.lang.String caller, int flags, android.app.NotificationManager oplusNm) {
        return 0;
    }

    default int readRingModeSetting(int ringerMode, android.content.ContentResolver cr) {
        return 0;
    }

    default boolean isRingVolumeDefault() {
        return false;
    }

    default void setAtlasMusicMuteState(boolean mutestate) {
    }

    default boolean isNeedGetOplusStreamVolume(int streamType, int uid) {
        return false;
    }

    default int getOplusStreamVolume(int streamStatesLength, int streamType, int uid) {
        return 0;
    }

    default void updateInputDevice(android.content.ContentResolver cr) {
    }

    default boolean getMuteStateForUser(int streamType) {
        return false;
    }

    default void putMuteStateForUser(int streamType, boolean mute) {
    }

    default void setStreamVolumeForUser() {
    }

    default int isNeedShowUiWarnings(int flags, java.lang.String callingPackage) {
        return 0;
    }

    default void checkSafeAndSetAvrcpAbsoluteVolume(int streamTypeAlias, int device, int streamType, int index, int oldIndex, int flags) {
    }

    default void onOplusRestoreVolumeBeforeSafeMediaVolume() {
    }

    default boolean isSpeakerA2dpDevice(int device) {
        return false;
    }

    default boolean getDialogOn() {
        return false;
    }

    default int getA2dpVolume(boolean cmpToSafeVolume, int a2dpVolume) {
        return 0;
    }

    default int getBleVolume(boolean cmpToSafeVolume, int bleVolume) {
        return 0;
    }

    default void audioDebugLogInit() {
    }

    default void notifyAtlasServiceModesUpdate(boolean onlyRead) {
    }

    default void notifyAtlasServiceRingerModeUpdate(java.lang.String caller, int ringerMode) {
    }

    default void notifyAdjustVolumeUpdate(int dir, java.lang.String caller) {
    }

    default boolean isAlertSliderSupported() {
        return false;
    }

    default void createAlerSliderManager() {
    }

    default int reAdjustDirectionbyAlerSlider(int streamType, int index, int direction, java.lang.String caller) {
        return 0;
    }

    default boolean isRingerModeContorlbyAlerSlider(int ringerMode, java.lang.String caller, int ringerModeExternal, boolean external) {
        return false;
    }

    default void storePreMediaVolume(int index, int device, java.lang.String caller) {
    }

    default void onStorePreMediaVolume(int index) {
    }

    default int rescaleIndexByAlertSlider(int streamType, int index, java.lang.String caller) {
        return 0;
    }

    default boolean cancelBroadcastVolumeChange(int streamType, int device, java.lang.String caller) {
        return false;
    }

    default boolean isAssistantVolumeSupported() {
        return false;
    }

    default boolean isSuperVolumeSupported() {
        return false;
    }

    default boolean isSuperVolumeSupported(int device, int streamType) {
        return false;
    }

    default boolean isSuperVolumeApp(java.lang.String mPackagename) {
        return false;
    }

    default void persistSuperVolume(int state, java.lang.String settingName) {
    }

    default void setSuperVolume(boolean enable, int streamType, int device) {
    }

    default boolean handleVolumeKey(android.view.KeyEvent event, java.lang.String caller) {
        return false;
    }

    default boolean isTriggerByVolumeKey() {
        return false;
    }

    default void updateAllSuperVolumeToPolicy() {
    }

    default void readPersistSuperVolume() {
    }

    default boolean isStreamSuperVolumeOn(int streamType) {
        return false;
    }

    default boolean oplusReadCameraSoundForced() {
        return false;
    }

    default void oplusMediaVolumeUpdateNotifyEffectExt(int stream, int device, int index) {
    }

    default boolean canHeadsetFadeIn(int stream, int device) {
        return false;
    }

    default void setAvrcpSupportsAbsoluteVolume(boolean support) {
    }

    default boolean getAvrcpSupportsAbsoluteVolume() {
        return false;
    }

    default void setAvrcpAbsoluteVolumeIndexDelay(int index, int delay) {
    }

    default void setMusicDeviceVolumeStateDelay(int device, int volume, int delay) {
    }

    default void updateDeviceChangeForMusic(int prevDevices, int newDevices) {
    }

    default void registerTelePhony() {
    }

    default boolean isPhoneCallIdle() {
        return false;
    }

    default boolean rejectBluetoothSco(int uid, int pid) {
        return false;
    }

    default boolean oplusSetStreamVolumePermission(int callingPid, int callingUid) {
        return false;
    }

    default boolean needJudgeScoActualy(int uid) {
        return false;
    }

    default boolean isOplusA2dpSmallVolumeCusEnable(int index, int indexMax) {
        return false;
    }

    default int getOplusA2dpSmallVolumeIndex(int index, int indexMax) {
        return 0;
    }

    default int getOplusPlatformType(android.content.Context context) {
        return 0;
    }

    default void preDispatchMode(int mode) {
    }

    default void oplusRegisterModeDispatcher(android.media.IAudioModeDispatcher dispatcher) {
    }

    default void unregisterModeDispatcher(android.media.IAudioModeDispatcher dispatcher) {
    }

    default void setBluetoothScoSpecialUid(java.lang.String mCallingApp, int uid) {
    }

    default void clearBluetoothScoClientAfterPhonebyuid(int mMode, java.lang.String mPackagename) {
    }

    default void onIPDeviceConnectionChange(android.media.AudioDeviceAttributes attributes, int state, java.lang.String caller, android.os.IBinder cb) {
    }

    default boolean getPrivacyCallSupport() {
        return false;
    }

    default void oplusClosePrivacyCall() {
    }

    default void oplusCheckPrivacyCall(int device, int streamType, int checktype) {
    }

    default boolean getPrivacyCallSoftwareModeSupport() {
        return false;
    }

    default boolean getPrivacyCallSoftwareModeSettingEnable() {
        return false;
    }

    default int getPrivacyCallSoftwareModeOn() {
        return 0;
    }

    default void setPrivacyCallSoftwareModeOn(int on, boolean setParam) {
    }

    default void checkPrivacyCallSoftwareMode(int device, int streamType, int checktype) {
    }

    default boolean isAudioRouteSupported() {
        return false;
    }

    default boolean manageRouteSettings(int uid, int pid, boolean on) {
        return true;
    }

    default void routeCheckForModeChange(int pid, int mode, java.lang.String packageName) {
    }

    default void clearRouteSettingCheck(int cmdType, int arg1, int arg2, java.lang.Object obj) {
    }

    default boolean selectRouteSetting(int cmdType, int arg1, int arg2, java.lang.Object obj) {
        return false;
    }

    default void recoverRouteSetting(int cmdType, int arg1, int arg2, java.lang.Object obj) {
    }

    default void checkTimeoutInactiveRouteClient() {
    }

    default void updateModeOwnerInfo(int pid, int uid, int mode) {
    }

    default void updatePreferredCommunicationDevice(android.media.AudioDeviceAttributes device) {
    }

    default void removeRouteClientActiveState(int uid) {
    }

    default void addRouteClientActiveState(int uid, int deviceType) {
    }

    default void audioRouteEventCheckForModeChange(int mode) {
    }

    default int getUidByPid(int pid) {
        return -1;
    }

    default int getLatestPreferredDeviceType() {
        return 0;
    }

    default int getLatestModeOwnerUid() {
        return -1;
    }

    default int getLatestModeOwnerPid() {
        return 0;
    }

    default boolean isBleDeviceCommunicationDevice() {
        return false;
    }

    default boolean getBleRingPlaybackActive() {
        return false;
    }

    default void setBleForceStream(int streamType) {
    }

    default boolean getBluetoothVolSyncSupported() {
        return false;
    }

    default void setBleRingPlaybackActive(java.util.List<android.media.AudioPlaybackConfiguration> configs) {
    }

    default void readAbsA2dpVolume() {
    }

    default boolean getSpatializerSpeakerSupported() {
        return false;
    }

    default boolean getOplusSpatializerSupported() {
        return false;
    }

    default void setSpatializerSpeakerState(boolean state) {
    }

    default void setSpatialAudioState(java.lang.String addresss, boolean state) {
    }

    default boolean getAudioEffectCombined() {
        return false;
    }

    default boolean getVocalProminenceSupport() {
        return false;
    }

    default void oplusCheckVocalProminence(int device, int streamType, int checkType) {
    }

    default void oplusSetVocalProminence(int state, int mode, boolean update) {
    }

    default void resetDownlinkMuteStatus() {
    }

    default boolean isCarkitBt() {
        return false;
    }

    default boolean isUnsupportHoloDevice(java.lang.String device) {
        return false;
    }

    default void recordMicMuteEventInfo(boolean on, java.lang.String callingPackage, int userId, int uid) {
    }

    default void checkAndClearMicMuteEvent(int userId) {
    }

    default void resetExAudioFocusState(int uid) {
    }

    default int getDownlinkMuteStatus() {
        return 0;
    }

    default boolean isNeedRetNotifiStream(boolean isVolumeChangeDispatch) {
        return true;
    }

    default boolean isVolumeDefaultAdjustSupported() {
        return false;
    }

    default boolean hasActivePlaybackOnForeground(int streamType) {
        return false;
    }

    default int getValidVolumeIdxForTargetStreams(int srcIndex, int streamType, boolean isVssVolIdx) {
        return srcIndex;
    }

    default int getValidSafeMediaVolumeIndex(int index) {
        return index;
    }

    default boolean isForceUseDefaultStep(java.lang.String caller) {
        return false;
    }

    default boolean oplusIsInBinderOptList(java.lang.String pkgName) {
        return false;
    }

    default boolean isGameModeSwitchOpen() {
        return false;
    }
}
