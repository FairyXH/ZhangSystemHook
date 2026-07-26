package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class SoundDoseHelper {
    static final java.lang.String ACTION_CHECK_MUSIC_ACTIVE = "com.android.server.audio.action.CHECK_MUSIC_ACTIVE";
    private static final int CSD_WARNING_TIMEOUT_MS_ACCUMULATION_START = -1;
    private static final int CSD_WARNING_TIMEOUT_MS_DOSE_1X = 7000;
    private static final int CSD_WARNING_TIMEOUT_MS_DOSE_5X = 5000;
    private static final int CSD_WARNING_TIMEOUT_MS_MOMENTARY_EXPOSURE = 5000;
    private static final long GLOBAL_TIME_OFFSET_UNINITIALIZED = -1;
    private static final int MAX_NUMBER_OF_CACHED_RECORDS = 655;
    private static final int MAX_RECORDS_STRING_LENGTH = 50;
    private static final int MAX_SETTINGS_LENGTH = 32768;
    private static final int MOMENTARY_EXPOSURE_TIMEOUT_MS = 72000000;
    private static final int MOMENTARY_EXPOSURE_TIMEOUT_UNINITIALIZED = -1;
    static final int MSG_CONFIGURE_SAFE_MEDIA = 1001;
    static final int MSG_CONFIGURE_SAFE_MEDIA_FORCED = 1002;
    static final int MSG_CSD_UPDATE_ATTENUATION = 1006;
    static final int MSG_LOWER_VOLUME_TO_RS1 = 1007;
    static final int MSG_PERSIST_CSD_VALUES = 1005;
    static final int MSG_PERSIST_MUSIC_ACTIVE_MS = 1004;
    static final int MSG_PERSIST_SAFE_VOLUME_STATE = 1003;
    private static final int MUSIC_ACTIVE_POLL_PERIOD_MS = 60000;
    private static final java.lang.String PERSIST_CSD_RECORD_FIELD_SEPARATOR = ",";
    private static final java.lang.String PERSIST_CSD_RECORD_SEPARATOR = "\\|";
    private static final java.lang.String PERSIST_CSD_RECORD_SEPARATOR_CHAR = "|";
    private static final int REQUEST_CODE_CHECK_MUSIC_ACTIVE = 1;
    private static final int SAFE_MEDIA_VOLUME_ACTIVE = 3;
    private static final int SAFE_MEDIA_VOLUME_DISABLED = 1;
    private static final int SAFE_MEDIA_VOLUME_INACTIVE = 2;
    private static final int SAFE_MEDIA_VOLUME_NOT_CONFIGURED = 0;
    private static final int SAFE_MEDIA_VOLUME_UNINITIALIZED = -1;
    private static final int SAFE_VOLUME_CONFIGURE_TIMEOUT_MS = 30000;
    private static final java.lang.String SYSTEM_PROPERTY_SAFEMEDIA_BYPASS = "audio.safemedia.bypass";
    private static final java.lang.String SYSTEM_PROPERTY_SAFEMEDIA_CSD_FORCE = "audio.safemedia.csd.force";
    private static final java.lang.String SYSTEM_PROPERTY_SAFEMEDIA_FORCE = "audio.safemedia.force";
    private static final java.lang.String TAG = "AS.SoundDoseHelper";
    private static final int UNSAFE_VOLUME_MUSIC_ACTIVE_MS_MAX = 72000000;
    private final android.app.AlarmManager mAlarmManager;
    private final com.android.server.audio.AudioService.AudioHandler mAudioHandler;
    private final com.android.server.audio.AudioService mAudioService;
    private final android.content.Context mContext;
    private int mMusicActiveMs;
    private com.android.server.audio.SoundDoseHelper.StreamVolumeCommand mPendingVolumeCommand;
    private float mSafeMediaVolumeDbfs;
    private int mSafeMediaVolumeIndex;
    private int mSafeMediaVolumeState;
    private final com.android.server.audio.SettingsAdapter mSettings;
    private final com.android.server.audio.AudioService.ISafeHearingVolumeController mVolumeController;
    private final com.android.server.utils.EventLogger mLogger = new com.android.server.utils.EventLogger(30, "CSD updates");
    private int mMcc = -1;
    private final java.lang.Object mSafeMediaVolumeStateLock = new java.lang.Object();
    private final android.util.SparseIntArray mSafeMediaVolumeDevices = new android.util.SparseIntArray();
    private long mLastMusicActiveTimeMs = 0;
    private int mCurrentUserId = 0;
    private int mPreviousUserId = 0;
    private long mUserSwitchTimeMs = 0;
    private android.app.PendingIntent mMusicActiveIntent = null;
    private final java.util.concurrent.atomic.AtomicBoolean mEnableCsd = new java.util.concurrent.atomic.AtomicBoolean(false);
    private final java.util.concurrent.atomic.AtomicBoolean mForceCsdProperty = new java.util.concurrent.atomic.AtomicBoolean(false);
    private final java.lang.Object mCsdAsAFeatureLock = new java.lang.Object();
    private boolean mIsCsdAsAFeatureAvailable = false;
    private boolean mIsCsdAsAFeatureEnabled = false;
    private final java.util.ArrayList<android.media.ISoundDose.AudioDeviceCategory> mCachedAudioDeviceCategories = new java.util.ArrayList<>();
    private com.android.server.audio.SoundDoseHelper.SoundDoseHelperWrapper mSdhWrapper = new com.android.server.audio.SoundDoseHelper.SoundDoseHelperWrapper();
    private final java.lang.Object mCsdStateLock = new java.lang.Object();
    private final java.util.concurrent.atomic.AtomicReference<android.media.ISoundDose> mSoundDose = new java.util.concurrent.atomic.AtomicReference<>();
    private float mCurrentCsd = 0.0f;
    private long mLastMomentaryExposureTimeMs = -1;
    private float mNextCsdWarning = 1.0f;
    private final java.util.List<android.media.SoundDoseRecord> mDoseRecords = new java.util.ArrayList();
    private long mGlobalTimeOffsetInSecs = -1;
    private final android.media.ISoundDoseCallback.Stub mSoundDoseCallback = new android.media.ISoundDoseCallback.Stub() { // from class: com.android.server.audio.SoundDoseHelper.1
        public void onMomentaryExposure(float currentMel, int deviceId) {
            if (!com.android.server.audio.SoundDoseHelper.this.mEnableCsd.get()) {
                android.util.Log.w(com.android.server.audio.SoundDoseHelper.TAG, "onMomentaryExposure: csd not supported, ignoring callback");
                return;
            }
            android.util.Log.w(com.android.server.audio.SoundDoseHelper.TAG, "DeviceId " + deviceId + " triggered momentary exposure with value: " + currentMel);
            com.android.server.audio.SoundDoseHelper.this.mLogger.enqueue(com.android.server.audio.AudioServiceEvents.SoundDoseEvent.getMomentaryExposureEvent(currentMel));
            boolean postWarning = false;
            synchronized (com.android.server.audio.SoundDoseHelper.this.mCsdStateLock) {
                if (com.android.server.audio.SoundDoseHelper.this.mLastMomentaryExposureTimeMs < 0 || java.lang.System.currentTimeMillis() - com.android.server.audio.SoundDoseHelper.this.mLastMomentaryExposureTimeMs >= 72000000) {
                    com.android.server.audio.SoundDoseHelper.this.mLastMomentaryExposureTimeMs = java.lang.System.currentTimeMillis();
                    postWarning = true;
                }
            }
            if (postWarning) {
                com.android.server.audio.SoundDoseHelper.this.mVolumeController.postDisplayCsdWarning(3, com.android.server.audio.SoundDoseHelper.this.getTimeoutMsForWarning(3));
            }
        }

        public void onNewCsdValue(float currentCsd, android.media.SoundDoseRecord[] records) {
            if (!com.android.server.audio.SoundDoseHelper.this.mEnableCsd.get()) {
                android.util.Log.w(com.android.server.audio.SoundDoseHelper.TAG, "onNewCsdValue: csd not supported, ignoring value");
                return;
            }
            android.util.Log.i(com.android.server.audio.SoundDoseHelper.TAG, "onNewCsdValue: " + currentCsd);
            synchronized (com.android.server.audio.SoundDoseHelper.this.mCsdStateLock) {
                if (com.android.server.audio.SoundDoseHelper.this.mCurrentCsd < currentCsd) {
                    if (com.android.server.audio.SoundDoseHelper.this.mCurrentCsd < com.android.server.audio.SoundDoseHelper.this.mNextCsdWarning && currentCsd >= com.android.server.audio.SoundDoseHelper.this.mNextCsdWarning) {
                        if (com.android.server.audio.SoundDoseHelper.this.mNextCsdWarning == 5.0f) {
                            com.android.server.audio.SoundDoseHelper.this.mVolumeController.postDisplayCsdWarning(2, com.android.server.audio.SoundDoseHelper.this.getTimeoutMsForWarning(2));
                            com.android.server.audio.SoundDoseHelper.this.mAudioService.postLowerVolumeToRs1();
                        } else {
                            com.android.server.audio.SoundDoseHelper.this.mVolumeController.postDisplayCsdWarning(1, com.android.server.audio.SoundDoseHelper.this.getTimeoutMsForWarning(1));
                        }
                        com.android.server.audio.SoundDoseHelper.this.mNextCsdWarning += 1.0f;
                    }
                } else if (currentCsd < com.android.server.audio.SoundDoseHelper.this.mNextCsdWarning - 1.0f && com.android.server.audio.SoundDoseHelper.this.mNextCsdWarning >= 2.0f) {
                    com.android.server.audio.SoundDoseHelper.this.mNextCsdWarning -= 1.0f;
                }
                com.android.server.audio.SoundDoseHelper.this.mCurrentCsd = currentCsd;
                com.android.server.audio.SoundDoseHelper.this.updateSoundDoseRecords_l(records, currentCsd);
            }
        }
    };

    SoundDoseHelper(com.android.server.audio.AudioService audioService, android.content.Context context, com.android.server.audio.AudioService.AudioHandler audioHandler, com.android.server.audio.SettingsAdapter settings, com.android.server.audio.AudioService.ISafeHearingVolumeController volumeController) {
        this.mAudioService = audioService;
        this.mAudioHandler = audioHandler;
        this.mSettings = settings;
        this.mVolumeController = volumeController;
        this.mContext = context;
        initSafeVolumes();
        this.mSafeMediaVolumeState = this.mSettings.getGlobalInt(audioService.getContentResolver(), "audio_safe_volume_state", 0);
        this.mSafeMediaVolumeIndex = this.mContext.getResources().getInteger(android.R.integer.config_phonenumber_compare_min_match) * 10;
        this.mSafeMediaVolumeIndex = this.mAudioService.getWrapper().getExtImpl().getValidSafeMediaVolumeIndex(this.mSafeMediaVolumeIndex);
        this.mSoundDose.set(android.media.AudioSystem.getSoundDoseInterface(this.mSoundDoseCallback));
        initCsd();
        this.mAlarmManager = (android.app.AlarmManager) this.mContext.getSystemService(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
    }

    void initSafeVolumes() {
        this.mSafeMediaVolumeDevices.append(4, -1);
        this.mSafeMediaVolumeDevices.append(8, -1);
        this.mSafeMediaVolumeDevices.append(67108864, -1);
        this.mSafeMediaVolumeDevices.append(536870912, -1);
        this.mSafeMediaVolumeDevices.append(536870914, -1);
        this.mSafeMediaVolumeDevices.append(256, -1);
        this.mSafeMediaVolumeDevices.append(128, -1);
        this.mSafeMediaVolumeDevices.append(16, -1);
        this.mSafeMediaVolumeDevices.append(32, -1);
    }

    float getOutputRs2UpperBound() {
        if (!this.mEnableCsd.get()) {
            return 0.0f;
        }
        android.media.ISoundDose soundDose = this.mSoundDose.get();
        if (soundDose == null) {
            android.util.Log.w(TAG, "Sound dose interface not initialized");
            return 0.0f;
        }
        try {
            return soundDose.getOutputRs2UpperBound();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Exception while getting the RS2 exposure value", e);
            return 0.0f;
        }
    }

    void setOutputRs2UpperBound(float rs2Value) {
        if (!this.mEnableCsd.get()) {
            return;
        }
        android.media.ISoundDose soundDose = this.mSoundDose.get();
        if (soundDose == null) {
            android.util.Log.w(TAG, "Sound dose interface not initialized");
            return;
        }
        try {
            soundDose.setOutputRs2UpperBound(rs2Value);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Exception while setting the RS2 exposure value", e);
        }
    }

    private boolean updateCsdForTestApi() {
        if (this.mForceCsdProperty.get() != android.os.SystemProperties.getBoolean(SYSTEM_PROPERTY_SAFEMEDIA_CSD_FORCE, false)) {
            updateCsdEnabled("SystemPropertiesChangeCallback");
        }
        return this.mEnableCsd.get();
    }

    float getCsd() {
        if (!this.mEnableCsd.get() && !updateCsdForTestApi()) {
            return -1.0f;
        }
        android.media.ISoundDose soundDose = this.mSoundDose.get();
        if (soundDose == null) {
            android.util.Log.w(TAG, "Sound dose interface not initialized");
            return -1.0f;
        }
        try {
            return soundDose.getCsd();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Exception while getting the CSD value", e);
            return -1.0f;
        }
    }

    void setCsd(float csd) {
        android.media.SoundDoseRecord[] doseRecordsArray;
        if (!this.mEnableCsd.get() && !updateCsdForTestApi()) {
            return;
        }
        synchronized (this.mCsdStateLock) {
            this.mCurrentCsd = csd;
            this.mNextCsdWarning = (float) java.lang.Math.floor(((double) csd) + 1.0d);
            this.mDoseRecords.clear();
            if (this.mCurrentCsd > 0.0f) {
                android.media.SoundDoseRecord record = new android.media.SoundDoseRecord();
                record.timestamp = android.os.SystemClock.elapsedRealtime() / 1000;
                record.value = csd;
                this.mDoseRecords.add(record);
            }
            doseRecordsArray = (android.media.SoundDoseRecord[]) this.mDoseRecords.toArray(new android.media.SoundDoseRecord[0]);
        }
        android.media.ISoundDose soundDose = this.mSoundDose.get();
        if (soundDose == null) {
            android.util.Log.w(TAG, "Sound dose interface not initialized");
            return;
        }
        try {
            soundDose.resetCsd(csd, doseRecordsArray);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Exception while setting the CSD value", e);
        }
    }

    void resetCsdTimeouts() {
        if (!this.mEnableCsd.get() && !updateCsdForTestApi()) {
            return;
        }
        synchronized (this.mCsdStateLock) {
            this.mLastMomentaryExposureTimeMs = -1L;
        }
    }

    void forceUseFrameworkMel(boolean useFrameworkMel) {
        if (!this.mEnableCsd.get() && !updateCsdForTestApi()) {
            return;
        }
        android.media.ISoundDose soundDose = this.mSoundDose.get();
        if (soundDose == null) {
            android.util.Log.w(TAG, "Sound dose interface not initialized");
            return;
        }
        try {
            soundDose.forceUseFrameworkMel(useFrameworkMel);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Exception while forcing the internal MEL computation", e);
        }
    }

    void forceComputeCsdOnAllDevices(boolean computeCsdOnAllDevices) {
        if (!this.mEnableCsd.get() && !updateCsdForTestApi()) {
            return;
        }
        android.media.ISoundDose soundDose = this.mSoundDose.get();
        if (soundDose == null) {
            android.util.Log.w(TAG, "Sound dose interface not initialized");
            return;
        }
        try {
            soundDose.forceComputeCsdOnAllDevices(computeCsdOnAllDevices);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Exception while forcing CSD computation on all devices", e);
        }
    }

    boolean isCsdEnabled() {
        if (!this.mEnableCsd.get()) {
            return false;
        }
        android.media.ISoundDose soundDose = this.mSoundDose.get();
        if (soundDose == null) {
            android.util.Log.w(TAG, "Sound dose interface not initialized");
            return false;
        }
        try {
            return soundDose.isSoundDoseHalSupported();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Exception while querying the csd enabled status", e);
            return false;
        }
    }

    boolean isCsdAsAFeatureAvailable() {
        boolean z;
        synchronized (this.mCsdAsAFeatureLock) {
            z = this.mIsCsdAsAFeatureAvailable;
        }
        return z;
    }

    boolean isCsdAsAFeatureEnabled() {
        boolean z;
        synchronized (this.mCsdAsAFeatureLock) {
            z = this.mIsCsdAsAFeatureEnabled;
        }
        return z;
    }

    void setCsdAsAFeatureEnabled(boolean csdAsAFeatureEnabled) {
        boolean doUpdate;
        synchronized (this.mCsdAsAFeatureLock) {
            int i = 1;
            doUpdate = this.mIsCsdAsAFeatureEnabled != csdAsAFeatureEnabled && this.mIsCsdAsAFeatureAvailable;
            this.mIsCsdAsAFeatureEnabled = csdAsAFeatureEnabled;
            long callingIdentity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.audio.SettingsAdapter settingsAdapter = this.mSettings;
                android.content.ContentResolver contentResolver = this.mAudioService.getContentResolver();
                if (!this.mIsCsdAsAFeatureEnabled) {
                    i = 0;
                }
                settingsAdapter.putSecureIntForUser(contentResolver, "audio_safe_csd_as_a_feature_enabled", i, -2);
            } finally {
                android.os.Binder.restoreCallingIdentity(callingIdentity);
            }
        }
        if (doUpdate) {
            updateCsdEnabled("setCsdAsAFeatureEnabled");
        }
    }

    void setAudioDeviceCategory(java.lang.String address, int internalAudioType, boolean isHeadphone) {
        if (!this.mEnableCsd.get()) {
            return;
        }
        android.media.ISoundDose soundDose = this.mSoundDose.get();
        if (soundDose == null) {
            android.util.Log.w(TAG, "Sound dose interface not initialized");
            return;
        }
        try {
            android.media.ISoundDose.AudioDeviceCategory audioDeviceCategory = new android.media.ISoundDose.AudioDeviceCategory();
            audioDeviceCategory.address = address;
            audioDeviceCategory.internalAudioType = internalAudioType;
            audioDeviceCategory.csdCompatible = isHeadphone;
            soundDose.setAudioDeviceCategory(audioDeviceCategory);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Exception while setting the audio device category", e);
        }
    }

    void initCachedAudioDeviceCategories(java.util.Collection<com.android.server.audio.AdiDeviceState> deviceStates) {
        for (com.android.server.audio.AdiDeviceState state : deviceStates) {
            if (state.getAudioDeviceCategory() != 0) {
                android.media.ISoundDose.AudioDeviceCategory audioDeviceCategory = new android.media.ISoundDose.AudioDeviceCategory();
                audioDeviceCategory.address = state.getDeviceAddress();
                audioDeviceCategory.internalAudioType = state.getInternalDeviceType();
                audioDeviceCategory.csdCompatible = state.getAudioDeviceCategory() == 3;
                this.mCachedAudioDeviceCategories.add(audioDeviceCategory);
            }
        }
    }

    int safeMediaVolumeIndex(int device) {
        int vol = this.mSafeMediaVolumeDevices.get(device);
        if (vol == -1) {
            return com.android.server.audio.AudioService.MAX_STREAM_VOLUME[3];
        }
        return vol;
    }

    void restoreMusicActiveMs() {
        synchronized (this.mSafeMediaVolumeStateLock) {
            this.mMusicActiveMs = android.util.MathUtils.constrain(this.mSettings.getSecureIntForUser(this.mAudioService.getContentResolver(), "unsafe_volume_music_active_ms", 0, -2), 0, 72000000);
        }
    }

    void enforceSafeMediaVolumeIfActive(java.lang.String caller) {
        synchronized (this.mSafeMediaVolumeStateLock) {
            if (this.mSafeMediaVolumeState == 3) {
                enforceSafeMediaVolume(caller);
            }
        }
    }

    void enforceSafeMediaVolume(java.lang.String caller) throws java.lang.Throwable {
        com.android.server.audio.AudioService.VolumeStreamState streamState = this.mAudioService.getVssVolumeForStream(3);
        for (int i = 0; i < this.mSafeMediaVolumeDevices.size(); i++) {
            int deviceType = this.mSafeMediaVolumeDevices.keyAt(i);
            int index = streamState.getIndex(deviceType);
            int safeIndex = safeMediaVolumeIndex(deviceType);
            if (index > safeIndex) {
                streamState.storeVolume(deviceType, index);
                streamState.setIndex(safeIndex, deviceType, caller, true);
                this.mAudioHandler.sendMessageAtTime(this.mAudioHandler.obtainMessage(0, deviceType, 0, streamState), 0L);
                if (android.media.AudioSystem.isStreamActive(3, 0) && android.media.AudioSystem.DEVICE_OUT_ALL_A2DP_SET.contains(java.lang.Integer.valueOf(deviceType)) && this.mAudioService.getWrapper().getDeviceBroker() != null) {
                    this.mAudioService.getWrapper().getDeviceBroker().postSetAvrcpAbsoluteVolumeIndex(streamState.getIndex(deviceType) / 10);
                }
                if (this.mAudioService.getWrapper().getExtImpl().getBluetoothVolSyncSupported() && android.media.AudioSystem.isStreamActive(3, 0) && deviceType == 536870912) {
                    this.mAudioService.getWrapper().getDeviceBroker().postSetLeAudioVolumeIndex(streamState.getIndex(deviceType), this.mAudioService.getMaxVssVolumeForStream(3), 3);
                }
            }
        }
    }

    boolean checkSafeMediaVolume(int streamType, int index, int device) {
        boolean result;
        synchronized (this.mSafeMediaVolumeStateLock) {
            result = checkSafeMediaVolume_l(streamType, index, device);
        }
        return result;
    }

    private boolean checkSafeMediaVolume_l(int streamType, int index, int device) {
        return this.mSafeMediaVolumeState == 3 && com.android.server.audio.AudioService.mStreamVolumeAlias[streamType] == 3 && safeDevicesContains(device) && index > safeMediaVolumeIndex(device) && !this.mAudioService.getWrapper().getExtImpl().isSpeakerA2dpDevice(device);
    }

    boolean willDisplayWarningAfterCheckVolume(int streamType, int index, int device, int flags) {
        synchronized (this.mSafeMediaVolumeStateLock) {
            if (checkSafeMediaVolume_l(streamType, index, device)) {
                int uid = android.os.Binder.getCallingUid();
                java.lang.String callingPackage = this.mContext.getPackageManager().getNameForUid(uid);
                int tempflags = this.mAudioService.getWrapper().getExtImpl().isNeedShowUiWarnings(flags, callingPackage);
                this.mVolumeController.postDisplaySafeVolumeWarning(tempflags);
                this.mPendingVolumeCommand = new com.android.server.audio.SoundDoseHelper.StreamVolumeCommand(streamType, index, flags, device);
                return true;
            }
            return false;
        }
    }

    void disableSafeMediaVolume(java.lang.String callingPackage) {
        synchronized (this.mSafeMediaVolumeStateLock) {
            long identity = android.os.Binder.clearCallingIdentity();
            setSafeMediaVolumeEnabled(false, callingPackage);
            android.os.Binder.restoreCallingIdentity(identity);
            if (this.mPendingVolumeCommand != null) {
                int streamTypeAlias = com.android.server.audio.AudioService.mStreamVolumeAlias[this.mPendingVolumeCommand.mStreamType];
                if (streamTypeAlias == 3 && ((android.media.AudioSystem.DEVICE_OUT_ALL_A2DP_SET.contains(java.lang.Integer.valueOf(this.mPendingVolumeCommand.mDevice)) || (this.mAudioService.getWrapper().getExtImpl().getBluetoothVolSyncSupported() && this.mPendingVolumeCommand.mDevice == 536870912)) && (this.mPendingVolumeCommand.mFlags & 64) == 0 && this.mAudioService.getWrapper().getDeviceBroker() != null && this.mAudioService.getWrapper().getExtImpl().getBluetoothVolSyncSupported())) {
                    if (this.mPendingVolumeCommand.mDevice == 536870912) {
                        this.mAudioService.getWrapper().getDeviceBroker().postSetLeAudioVolumeIndex(this.mPendingVolumeCommand.mIndex, this.mAudioService.getMaxVssVolumeForStream(3), 3);
                    } else {
                        this.mAudioService.getWrapper().getDeviceBroker().postSetAvrcpAbsoluteVolumeIndex(this.mPendingVolumeCommand.mIndex / 10);
                    }
                }
                this.mAudioService.onSetStreamVolume(this.mPendingVolumeCommand.mStreamType, this.mPendingVolumeCommand.mIndex, this.mPendingVolumeCommand.mFlags, this.mPendingVolumeCommand.mDevice, callingPackage, true, true);
                this.mPendingVolumeCommand = null;
            }
        }
    }

    void scheduleMusicActiveCheck() {
        synchronized (this.mSafeMediaVolumeStateLock) {
            cancelMusicActiveCheck();
            this.mMusicActiveIntent = android.app.PendingIntent.getBroadcast(this.mContext, 1, new android.content.Intent(ACTION_CHECK_MUSIC_ACTIVE), android.hardware.audio.common.V2_0.AudioFormat.DTS_HD);
            this.mAlarmManager.setExactAndAllowWhileIdle(2, android.os.SystemClock.elapsedRealtime() + 60000, this.mMusicActiveIntent);
        }
    }

    void onCheckMusicActive(java.lang.String caller, boolean isStreamActive) {
        synchronized (this.mSafeMediaVolumeStateLock) {
            if (this.mSafeMediaVolumeState == 2) {
                int device = this.mAudioService.getDeviceForStream(3);
                if (safeDevicesContains(device) && isStreamActive) {
                    scheduleMusicActiveCheck();
                    int index = this.mAudioService.getVssVolumeForDevice(3, device);
                    if (index > safeMediaVolumeIndex(device)) {
                        long curTimeMs = android.os.SystemClock.elapsedRealtime();
                        if (this.mLastMusicActiveTimeMs != 0) {
                            this.mMusicActiveMs += (int) (curTimeMs - this.mLastMusicActiveTimeMs);
                        } else {
                            this.mMusicActiveMs += 60000;
                        }
                        this.mLastMusicActiveTimeMs = curTimeMs;
                        android.util.Log.d(TAG, "20H onCheckMusicActive:" + this.mMusicActiveMs);
                        if (this.mMusicActiveMs > 72000000) {
                            setSafeMediaVolumeEnabled(true, caller);
                            this.mMusicActiveMs = 0;
                        }
                        saveMusicActiveMs();
                    }
                } else {
                    cancelMusicActiveCheck();
                    this.mLastMusicActiveTimeMs = 0L;
                }
            }
        }
    }

    void configureSafeMedia(boolean forced, java.lang.String caller) {
        int msg = forced ? 1002 : 1001;
        this.mAudioHandler.removeMessages(msg);
        long time = 0;
        if (forced) {
            time = android.os.SystemClock.uptimeMillis() + ((long) (android.os.SystemProperties.getBoolean(SYSTEM_PROPERTY_SAFEMEDIA_BYPASS, false) ? 0 : 30000));
        }
        this.mAudioHandler.sendMessageAtTime(this.mAudioHandler.obtainMessage(msg, 0, 0, caller), time);
    }

    void initSafeMediaVolumeIndex() {
        for (int i = 0; i < this.mSafeMediaVolumeDevices.size(); i++) {
            int deviceType = this.mSafeMediaVolumeDevices.keyAt(i);
            if (this.mSafeMediaVolumeDevices.valueAt(i) == -1) {
                this.mSafeMediaVolumeDevices.put(deviceType, getSafeDeviceMediaVolumeIndex(deviceType));
            }
        }
    }

    int getSafeMediaVolumeIndex(int device) {
        if (this.mSafeMediaVolumeState == 3 && safeDevicesContains(device)) {
            return safeMediaVolumeIndex(device);
        }
        return -1;
    }

    boolean raiseVolumeDisplaySafeMediaVolume(int streamType, int index, int device, int flags) {
        if (!checkSafeMediaVolume(streamType, index, device)) {
            return false;
        }
        this.mVolumeController.postDisplaySafeVolumeWarning(flags);
        return true;
    }

    boolean safeDevicesContains(int device) {
        return this.mSafeMediaVolumeDevices.get(device, -1) >= 0;
    }

    void invalidatePendingVolumeCommand() {
        synchronized (this.mSafeMediaVolumeStateLock) {
            this.mPendingVolumeCommand = null;
        }
    }

    void handleMessage(android.os.Message msg) {
        boolean isAbsoluteVolume;
        switch (msg.what) {
            case 1001:
            case 1002:
                isAbsoluteVolume = msg.what == 1002;
                onConfigureSafeMedia(isAbsoluteVolume, (java.lang.String) msg.obj);
                break;
            case 1003:
                int musicActiveMs = msg.arg1;
                onPersistSafeVolumeState(musicActiveMs);
                break;
            case 1004:
                int musicActiveMs2 = msg.arg1;
                this.mSettings.putSecureIntForUser(this.mAudioService.getContentResolver(), "unsafe_volume_music_active_ms", musicActiveMs2, -2);
                break;
            case 1005:
                onPersistSoundDoseRecords();
                break;
            case 1006:
                int device = msg.arg1;
                isAbsoluteVolume = msg.arg2 == 1;
                com.android.server.audio.AudioService.VolumeStreamState streamState = (com.android.server.audio.AudioService.VolumeStreamState) msg.obj;
                updateDoseAttenuation(streamState.getIndex(device), device, streamState.getStreamType(), isAbsoluteVolume);
                break;
            case 1007:
                onLowerVolumeToRs1();
                break;
            default:
                android.util.Log.e(TAG, "Unexpected msg to handle: " + msg.what);
                break;
        }
    }

    void dump(java.io.PrintWriter pw) {
        pw.print("  mEnableCsd=");
        pw.println(this.mEnableCsd.get());
        if (this.mEnableCsd.get()) {
            synchronized (this.mCsdStateLock) {
                pw.print("  mCurrentCsd=");
                pw.println(this.mCurrentCsd);
            }
        }
        pw.print("  mSafeMediaVolumeState=");
        pw.println(safeMediaVolumeStateToString(this.mSafeMediaVolumeState));
        pw.print("  mSafeMediaVolumeIndex=");
        pw.println(this.mSafeMediaVolumeIndex);
        for (int i = 0; i < this.mSafeMediaVolumeDevices.size(); i++) {
            pw.print("  mSafeMediaVolumeIndex[");
            pw.print(this.mSafeMediaVolumeDevices.keyAt(i));
            pw.print("]=");
            pw.println(this.mSafeMediaVolumeDevices.valueAt(i));
        }
        pw.print("  mSafeMediaVolumeDbfs=");
        pw.println(this.mSafeMediaVolumeDbfs);
        pw.print("  mMusicActiveMs=");
        pw.println(this.mMusicActiveMs);
        pw.print("  mMcc=");
        pw.println(this.mMcc);
        pw.print("  mPendingVolumeCommand=");
        pw.println(this.mPendingVolumeCommand);
        pw.println();
        this.mLogger.dump(pw);
        pw.println();
    }

    void reset(boolean resetISoundDose) {
        android.util.Log.d(TAG, "Reset the sound dose helper");
        if (resetISoundDose) {
            this.mSoundDose.set(android.media.AudioSystem.getSoundDoseInterface(this.mSoundDoseCallback));
        }
        synchronized (this.mCsdStateLock) {
            try {
                android.media.ISoundDose soundDose = this.mSoundDose.get();
                if (soundDose != null && soundDose.asBinder().isBinderAlive() && this.mCurrentCsd != 0.0f) {
                    android.util.Log.d(TAG, "Resetting the saved sound dose value " + this.mCurrentCsd);
                    android.media.SoundDoseRecord[] records = (android.media.SoundDoseRecord[]) this.mDoseRecords.toArray(new android.media.SoundDoseRecord[0]);
                    soundDose.resetCsd(this.mCurrentCsd, records);
                }
            } catch (android.os.RemoteException e) {
            }
        }
    }

    private void updateDoseAttenuation(int newIndex, int device, int streamType, boolean isAbsoluteVolume) {
        if (!this.mEnableCsd.get()) {
            return;
        }
        android.media.ISoundDose soundDose = this.mSoundDose.get();
        if (soundDose == null) {
            android.util.Log.w(TAG, "Can not apply attenuation. ISoundDose itf is null.");
            return;
        }
        try {
            if (!isAbsoluteVolume) {
                soundDose.updateAttenuation(0.0f, device);
            } else if (com.android.server.audio.AudioService.mStreamVolumeAlias[streamType] == 3 && safeDevicesContains(device)) {
                soundDose.updateAttenuation(-android.media.AudioSystem.getStreamVolumeDB(3, (newIndex + 5) / 10, device), device);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Could not apply the attenuation for MEL calculation with volume index " + newIndex, e);
        }
    }

    private void initCsd() {
        android.media.ISoundDose soundDose = this.mSoundDose.get();
        if (soundDose == null) {
            android.util.Log.w(TAG, "ISoundDose instance is null.");
            return;
        }
        try {
            soundDose.setCsdEnabled(this.mEnableCsd.get());
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Cannot disable CSD", e);
        }
        if (!this.mEnableCsd.get()) {
            return;
        }
        android.util.Log.v(TAG, "Initializing sound dose");
        try {
            if (!this.mCachedAudioDeviceCategories.isEmpty()) {
                soundDose.initCachedAudioDeviceCategories((android.media.ISoundDose.AudioDeviceCategory[]) this.mCachedAudioDeviceCategories.toArray(new android.media.ISoundDose.AudioDeviceCategory[0]));
                this.mCachedAudioDeviceCategories.clear();
            }
        } catch (android.os.RemoteException e2) {
            android.util.Log.e(TAG, "Exception while initializing the cached audio device categories", e2);
        }
        synchronized (this.mCsdAsAFeatureLock) {
            this.mIsCsdAsAFeatureEnabled = this.mSettings.getSecureIntForUser(this.mAudioService.getContentResolver(), "audio_safe_csd_as_a_feature_enabled", 0, -2) != 0;
        }
        synchronized (this.mCsdStateLock) {
            if (this.mGlobalTimeOffsetInSecs == -1) {
                this.mGlobalTimeOffsetInSecs = java.lang.System.currentTimeMillis() / 1000;
            }
            float prevCsd = this.mCurrentCsd;
            this.mCurrentCsd = parseGlobalSettingFloat("audio_safe_csd_current_value", 0.0f);
            if (this.mCurrentCsd != prevCsd) {
                this.mNextCsdWarning = parseGlobalSettingFloat("audio_safe_csd_next_warning", 1.0f);
                java.util.List<android.media.SoundDoseRecord> records = persistedStringToRecordList(this.mSettings.getGlobalString(this.mAudioService.getContentResolver(), "audio_safe_csd_dose_records"), this.mGlobalTimeOffsetInSecs);
                if (records != null) {
                    this.mDoseRecords.addAll(records);
                    sanitizeDoseRecords_l();
                }
            }
        }
        reset(false);
    }

    private void onConfigureSafeMedia(boolean force, java.lang.String caller) {
        updateCsdEnabled(caller);
        synchronized (this.mSafeMediaVolumeStateLock) {
            int mcc = this.mContext.getResources().getConfiguration().mcc;
            if (this.mMcc != mcc || (this.mMcc == 0 && force)) {
                this.mSafeMediaVolumeIndex = this.mContext.getResources().getInteger(android.R.integer.config_phonenumber_compare_min_match) * 10;
                this.mSafeMediaVolumeIndex = this.mAudioService.getWrapper().getExtImpl().getValidSafeMediaVolumeIndex(this.mSafeMediaVolumeIndex);
                initSafeMediaVolumeIndex();
                updateSafeMediaVolume_l(caller);
                this.mMcc = mcc;
            }
        }
    }

    private void updateSafeMediaVolume_l(java.lang.String caller) throws java.lang.Throwable {
        int persistedState;
        boolean safeMediaVolumeBypass = android.os.SystemProperties.getBoolean(SYSTEM_PROPERTY_SAFEMEDIA_BYPASS, false) || this.mEnableCsd.get();
        boolean safeMediaVolumeForce = android.os.SystemProperties.getBoolean(SYSTEM_PROPERTY_SAFEMEDIA_FORCE, false);
        boolean mccEnforcedSafeMediaVolume = this.mContext.getResources().getBoolean(android.R.bool.config_pinnerAssistantApp);
        boolean safeVolumeEnabled = (mccEnforcedSafeMediaVolume || safeMediaVolumeForce) && !safeMediaVolumeBypass;
        if (safeVolumeEnabled) {
            persistedState = 3;
            if (this.mSafeMediaVolumeState != 2) {
                if (this.mMusicActiveMs == 0) {
                    this.mSafeMediaVolumeState = 3;
                    enforceSafeMediaVolume(caller);
                } else {
                    this.mLastMusicActiveTimeMs = 0L;
                }
            }
        } else {
            this.mSafeMediaVolumeState = 1;
            persistedState = 1;
        }
        this.mAudioHandler.sendMessageAtTime(this.mAudioHandler.obtainMessage(1003, persistedState, 0, null), 0L);
    }

    private void updateCsdEnabled(java.lang.String caller) {
        this.mForceCsdProperty.set(android.os.SystemProperties.getBoolean(SYSTEM_PROPERTY_SAFEMEDIA_CSD_FORCE, false));
        boolean mccEnforcedSafeMedia = this.mContext.getResources().getBoolean(android.R.bool.config_pinnerAssistantApp);
        boolean csdEnable = this.mContext.getResources().getBoolean(android.R.bool.config_pinnerCameraApp);
        boolean newEnabledCsd = (mccEnforcedSafeMedia && csdEnable) || this.mForceCsdProperty.get();
        synchronized (this.mCsdAsAFeatureLock) {
            if (!mccEnforcedSafeMedia && csdEnable) {
                this.mIsCsdAsAFeatureAvailable = true;
                newEnabledCsd = this.mIsCsdAsAFeatureEnabled || this.mForceCsdProperty.get();
                android.util.Log.v(TAG, caller + ": CSD as a feature is not enforced and enabled: " + newEnabledCsd);
            } else {
                this.mIsCsdAsAFeatureAvailable = false;
            }
        }
        if (this.mEnableCsd.compareAndSet(newEnabledCsd ? false : true, newEnabledCsd)) {
            android.util.Log.i(TAG, caller + ": enabled CSD " + newEnabledCsd);
            initCsd();
            synchronized (this.mSafeMediaVolumeStateLock) {
                initSafeMediaVolumeIndex();
                updateSafeMediaVolume_l(caller);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTimeoutMsForWarning(int csdWarning) {
        switch (csdWarning) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                android.util.Log.e(TAG, "Invalid CSD warning " + csdWarning, new java.lang.Exception());
                break;
        }
        return -1;
    }

    private void setSafeMediaVolumeEnabled(boolean on, java.lang.String caller) throws java.lang.Throwable {
        if (this.mSafeMediaVolumeState == 0 || this.mSafeMediaVolumeState == 1) {
            return;
        }
        if (on && this.mSafeMediaVolumeState == 2) {
            this.mSafeMediaVolumeState = 3;
            this.mAudioService.getWrapper().broadcastSafeVolume();
            enforceSafeMediaVolume(caller);
        } else if (!on && this.mSafeMediaVolumeState == 3) {
            this.mSafeMediaVolumeState = 2;
            this.mMusicActiveMs = 1;
            this.mLastMusicActiveTimeMs = 0L;
            saveMusicActiveMs();
            scheduleMusicActiveCheck();
        }
    }

    private void cancelMusicActiveCheck() {
        if (this.mMusicActiveIntent != null) {
            this.mAlarmManager.cancel(this.mMusicActiveIntent);
            this.mMusicActiveIntent = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveMusicActiveMs() {
        this.mAudioHandler.obtainMessage(1004, this.mMusicActiveMs, 0).sendToTarget();
    }

    private int getSafeDeviceMediaVolumeIndex(int deviceType) {
        if ((deviceType == 8 || deviceType == 4) && !this.mEnableCsd.get()) {
            return this.mSafeMediaVolumeIndex;
        }
        int min = com.android.server.audio.AudioService.MIN_STREAM_VOLUME[3];
        int max = com.android.server.audio.AudioService.MAX_STREAM_VOLUME[3];
        this.mSafeMediaVolumeDbfs = this.mContext.getResources().getInteger(android.R.integer.config_pictureInPictureMaxNumberOfActions) / 100.0f;
        while (java.lang.Math.abs(max - min) > 1) {
            int index = (max + min) / 2;
            float gainDB = android.media.AudioSystem.getStreamVolumeDB(3, index, deviceType);
            if (java.lang.Float.isNaN(gainDB) || gainDB == this.mSafeMediaVolumeDbfs) {
                break;
            }
            if (gainDB < this.mSafeMediaVolumeDbfs) {
                min = index;
            } else {
                max = index;
            }
        }
        int tempSafeUsbMediaVolumeIndex = this.mContext.getResources().getInteger(android.R.integer.config_phonenumber_compare_min_match) * 10;
        return this.mAudioService.getWrapper().getExtImpl().getValidSafeMediaVolumeIndex(tempSafeUsbMediaVolumeIndex);
    }

    private void onPersistSafeVolumeState(int state) {
        this.mSettings.putGlobalInt(this.mAudioService.getContentResolver(), "audio_safe_volume_state", state);
    }

    private static java.lang.String safeMediaVolumeStateToString(int state) {
        switch (state) {
            case 0:
                return "SAFE_MEDIA_VOLUME_NOT_CONFIGURED";
            case 1:
                return "SAFE_MEDIA_VOLUME_DISABLED";
            case 2:
                return "SAFE_MEDIA_VOLUME_INACTIVE";
            case 3:
                return "SAFE_MEDIA_VOLUME_ACTIVE";
            default:
                return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSoundDoseRecords_l(android.media.SoundDoseRecord[] newRecords, float currentCsd) {
        long totalDuration = 0;
        for (final android.media.SoundDoseRecord record : newRecords) {
            android.util.Log.i(TAG, "  new record: " + record);
            totalDuration += (long) record.duration;
            if (record.value < 0.0f) {
                if (!this.mDoseRecords.removeIf(new java.util.function.Predicate() { // from class: com.android.server.audio.SoundDoseHelper$$ExternalSyntheticLambda1
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.audio.SoundDoseHelper.lambda$updateSoundDoseRecords_l$0(record, (android.media.SoundDoseRecord) obj);
                    }
                })) {
                    android.util.Log.w(TAG, "Could not find cached record to remove: " + record);
                }
            } else if (record.value > 0.0f) {
                this.mDoseRecords.add(record);
            }
        }
        sanitizeDoseRecords_l();
        this.mAudioHandler.sendMessageAtTime(this.mAudioHandler.obtainMessage(1005, 0, 0, null), 0L);
        this.mLogger.enqueue(com.android.server.audio.AudioServiceEvents.SoundDoseEvent.getDoseUpdateEvent(currentCsd, totalDuration));
    }

    static /* synthetic */ boolean lambda$updateSoundDoseRecords_l$0(android.media.SoundDoseRecord record, android.media.SoundDoseRecord r) {
        return r.value == (-record.value) && r.timestamp == record.timestamp && r.averageMel == record.averageMel && r.duration == record.duration;
    }

    private void sanitizeDoseRecords_l() {
        if (this.mDoseRecords.size() > MAX_NUMBER_OF_CACHED_RECORDS) {
            int nrToRemove = this.mDoseRecords.size() - MAX_NUMBER_OF_CACHED_RECORDS;
            android.util.Log.w(TAG, "Removing " + nrToRemove + " records from the total of " + this.mDoseRecords.size());
            java.util.Iterator<android.media.SoundDoseRecord> recordIterator = this.mDoseRecords.iterator();
            while (recordIterator.hasNext() && nrToRemove > 0) {
                recordIterator.next();
                recordIterator.remove();
                nrToRemove--;
            }
        }
    }

    private void onPersistSoundDoseRecords() {
        synchronized (this.mCsdStateLock) {
            if (this.mGlobalTimeOffsetInSecs == -1) {
                this.mGlobalTimeOffsetInSecs = java.lang.System.currentTimeMillis() / 1000;
            }
            this.mSettings.putGlobalString(this.mAudioService.getContentResolver(), "audio_safe_csd_current_value", java.lang.Float.toString(this.mCurrentCsd));
            this.mSettings.putGlobalString(this.mAudioService.getContentResolver(), "audio_safe_csd_next_warning", java.lang.Float.toString(this.mNextCsdWarning));
            this.mSettings.putGlobalString(this.mAudioService.getContentResolver(), "audio_safe_csd_dose_records", (java.lang.String) this.mDoseRecords.stream().map(new java.util.function.Function() { // from class: com.android.server.audio.SoundDoseHelper$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return this.f$0.lambda$onPersistSoundDoseRecords$1((android.media.SoundDoseRecord) obj);
                }
            }).collect(java.util.stream.Collectors.joining(PERSIST_CSD_RECORD_SEPARATOR_CHAR)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.String lambda$onPersistSoundDoseRecords$1(android.media.SoundDoseRecord record) {
        return recordToPersistedString(record, this.mGlobalTimeOffsetInSecs);
    }

    private static java.lang.String recordToPersistedString(android.media.SoundDoseRecord record, long globalTimeOffsetInSecs) {
        return convertToGlobalTime(record.timestamp, globalTimeOffsetInSecs) + PERSIST_CSD_RECORD_FIELD_SEPARATOR + record.duration + PERSIST_CSD_RECORD_FIELD_SEPARATOR + java.lang.String.format("%.3f", java.lang.Float.valueOf(record.value)) + PERSIST_CSD_RECORD_FIELD_SEPARATOR + java.lang.String.format("%.3f", java.lang.Float.valueOf(record.averageMel));
    }

    private static long convertToGlobalTime(long bootTimeInSecs, long globalTimeOffsetInSecs) {
        return bootTimeInSecs + globalTimeOffsetInSecs;
    }

    private static long convertToBootTime(long globalTimeInSecs, long globalTimeOffsetInSecs) {
        return globalTimeInSecs - globalTimeOffsetInSecs;
    }

    private static java.util.List<android.media.SoundDoseRecord> persistedStringToRecordList(java.lang.String records, final long globalTimeOffsetInSecs) {
        if (records == null || records.isEmpty()) {
            return null;
        }
        return (java.util.List) java.util.Arrays.stream(android.text.TextUtils.split(records, PERSIST_CSD_RECORD_SEPARATOR)).map(new java.util.function.Function() { // from class: com.android.server.audio.SoundDoseHelper$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.audio.SoundDoseHelper.persistedStringToRecord((java.lang.String) obj, globalTimeOffsetInSecs);
            }
        }).filter(new java.util.function.Predicate() { // from class: com.android.server.audio.SoundDoseHelper$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return java.util.Objects.nonNull((android.media.SoundDoseRecord) obj);
            }
        }).collect(java.util.stream.Collectors.toList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.media.SoundDoseRecord persistedStringToRecord(java.lang.String record, long globalTimeOffsetInSecs) {
        if (record == null || record.isEmpty()) {
            return null;
        }
        java.lang.String[] fields = android.text.TextUtils.split(record, PERSIST_CSD_RECORD_FIELD_SEPARATOR);
        if (fields.length != 4) {
            android.util.Log.w(TAG, "Expecting 4 fields for a SoundDoseRecord, parsed " + fields.length);
            return null;
        }
        android.media.SoundDoseRecord sdRecord = new android.media.SoundDoseRecord();
        try {
            sdRecord.timestamp = convertToBootTime(java.lang.Long.parseLong(fields[0]), globalTimeOffsetInSecs);
            sdRecord.duration = java.lang.Integer.parseInt(fields[1]);
            sdRecord.value = java.lang.Float.parseFloat(fields[2]);
            sdRecord.averageMel = java.lang.Float.parseFloat(fields[3]);
            return sdRecord;
        } catch (java.lang.NumberFormatException e) {
            android.util.Log.e(TAG, "Unable to parse persisted SoundDoseRecord: " + record, e);
            return null;
        }
    }

    private float parseGlobalSettingFloat(java.lang.String audioSafeCsdCurrentValue, float defaultValue) {
        java.lang.String stringValue = this.mSettings.getGlobalString(this.mAudioService.getContentResolver(), audioSafeCsdCurrentValue);
        if (stringValue == null || stringValue.isEmpty()) {
            android.util.Log.v(TAG, "No value stored in settings " + audioSafeCsdCurrentValue);
            return defaultValue;
        }
        try {
            float value = java.lang.Float.parseFloat(stringValue);
            return value;
        } catch (java.lang.NumberFormatException e) {
            android.util.Log.e(TAG, "Error parsing float from settings " + audioSafeCsdCurrentValue, e);
            return defaultValue;
        }
    }

    private void onLowerVolumeToRs1() {
        int nativeDeviceType;
        android.media.AudioDeviceAttributes ada;
        this.mLogger.enqueue(com.android.server.audio.AudioServiceEvents.SoundDoseEvent.getLowerVolumeToRs1Event());
        java.util.ArrayList<android.media.AudioDeviceAttributes> devices = this.mAudioService.getDevicesForAttributesInt(new android.media.AudioAttributes.Builder().setUsage(1).build(), true);
        if (!devices.isEmpty()) {
            ada = devices.get(0);
            nativeDeviceType = ada.getInternalType();
        } else {
            nativeDeviceType = 67108864;
            ada = new android.media.AudioDeviceAttributes(67108864, "");
        }
        int index = safeMediaVolumeIndex(nativeDeviceType);
        this.mAudioService.setStreamVolumeWithAttributionInt(3, index / 10, 0, ada, this.mContext.getOpPackageName(), null, true);
    }

    private static class StreamVolumeCommand {
        public final int mDevice;
        public final int mFlags;
        public final int mIndex;
        public final int mStreamType;

        StreamVolumeCommand(int streamType, int index, int flags, int device) {
            this.mStreamType = streamType;
            this.mIndex = index;
            this.mFlags = flags;
            this.mDevice = device;
        }

        public java.lang.String toString() {
            return "{streamType=" + this.mStreamType + ",index=" + this.mIndex + ",flags=" + this.mFlags + ",device=" + this.mDevice + '}';
        }
    }

    public com.android.server.audio.ISoundDoseHelperWrapper getWrapper() {
        return this.mSdhWrapper;
    }

    private class SoundDoseHelperWrapper implements com.android.server.audio.ISoundDoseHelperWrapper {
        private SoundDoseHelperWrapper() {
        }

        @Override // com.android.server.audio.ISoundDoseHelperWrapper
        public int getSafeMediaVolumeState() {
            int i;
            synchronized (com.android.server.audio.SoundDoseHelper.this.mSafeMediaVolumeStateLock) {
                i = com.android.server.audio.SoundDoseHelper.this.mSafeMediaVolumeState;
            }
            return i;
        }

        @Override // com.android.server.audio.ISoundDoseHelperWrapper
        public void setSafeMediaVolumeState(int active) {
            synchronized (com.android.server.audio.SoundDoseHelper.this.mSafeMediaVolumeStateLock) {
                com.android.server.audio.SoundDoseHelper.this.mSafeMediaVolumeState = active;
            }
        }

        @Override // com.android.server.audio.ISoundDoseHelperWrapper
        public void setMusicActiveMs(int active) {
            synchronized (com.android.server.audio.SoundDoseHelper.this.mSafeMediaVolumeStateLock) {
                com.android.server.audio.SoundDoseHelper.this.mMusicActiveMs = active;
            }
        }

        @Override // com.android.server.audio.ISoundDoseHelperWrapper
        public void saveMusicActiveMs() {
            com.android.server.audio.SoundDoseHelper.this.saveMusicActiveMs();
        }

        @Override // com.android.server.audio.ISoundDoseHelperWrapper
        public void checkMusicActive() {
            com.android.server.audio.SoundDoseHelper.this.scheduleMusicActiveCheck();
        }
    }

    public void updateCurrentUserInfo(int userId) {
        synchronized (this.mSafeMediaVolumeStateLock) {
            this.mPreviousUserId = this.mCurrentUserId;
            this.mUserSwitchTimeMs = android.os.SystemClock.elapsedRealtime();
            this.mCurrentUserId = userId;
        }
    }

    public int getSafeVolumeIntentOwnerId() {
        int i;
        synchronized (this.mSafeMediaVolumeStateLock) {
            long currentTimeMS = android.os.SystemClock.elapsedRealtime();
            i = currentTimeMS - this.mUserSwitchTimeMs > 60000 ? this.mCurrentUserId : this.mPreviousUserId;
        }
        return i;
    }
}
