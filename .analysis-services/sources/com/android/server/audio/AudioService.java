package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class AudioService extends android.media.IAudioService.Stub implements android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener, android.view.accessibility.AccessibilityManager.AccessibilityServicesStateChangeListener, com.android.server.audio.AudioSystemAdapter.OnRoutingUpdatedListener, com.android.server.audio.AudioSystemAdapter.OnVolRangeInitRequestListener {
    private static final java.lang.String AUDIO_HAL_SERVICE_PREFIX = "android.hardware.audio";
    public static final int BECOMING_NOISY_DELAY_MS = 1000;
    private static final int BINAURAL_RECORD_STATE_RESTORE = 3;
    private static final int BINAURAL_RECORD_STATE_SUSPEND = 2;
    private static final java.lang.String BLUETOOTH_PACKAGE_NAME = "com.android.bluetooth";
    private static final int CHECK_MODE_FOR_UID_PERIOD_MS = 6000;
    public static final int CHECK_PRIVACY_CALL_DELAY = 500;
    private static final int CHECK_VOCAL_PROMINENCE_DELAY = 500;
    static final int CONNECTION_STATE_CONNECTED = 1;
    static final int CONNECTION_STATE_DISCONNECTED = 0;
    static final int CONNECTION_STATE_NOMIC_TO_MIC = 2;
    protected static boolean DEBUG_AP = false;
    protected static boolean DEBUG_COMM_RTE = false;
    protected static boolean DEBUG_DEVICES = false;
    protected static final boolean DEBUG_LOG_SOUND_FX = false;
    protected static boolean DEBUG_MODE = false;
    protected static final boolean DEBUG_SCO = true;
    protected static boolean DEBUG_VOL = false;
    private static final byte[] DEFAULT_ARC_AUDIO_DESCRIPTOR;
    private static final int DEFAULT_STREAM_TYPE_OVERRIDE_DELAY_MS = 0;
    protected static final int DEFAULT_VOL_STREAM_NO_PLAYBACK = 3;
    private static final java.util.Set<java.lang.Integer> DEVICE_MEDIA_UNMUTED_ON_PLUG_SET;
    private static final java.lang.String FEATURE_RINGERMODE_FEEDBACK_SUPPORT = "ro.oplus.audio.ringermodeinfo.feedback.support";
    private static final boolean FEATURE_SPEAKER_CLEAN_SUPPORT;
    private static final int FLAG_ADJUST_VOLUME = 1;
    public static final int HD_SAMPLE_RATE = 48000;
    private static final java.lang.String HOLO_DEVICE_COMPAT_STATE = "holoDeviceCompatState=";
    private static final int HOLO_DEVICE_STATE_NORMAL = 0;
    private static final int HOLO_DEVICE_STATE_UNSUPPORT = 1;
    private static final int HOLO_DEVICE_STATE_UNSUP_VOIP = 2;
    private static final java.lang.String HOLO_PARAM_UPDATE_METADATA = "holovoip_update_metadata=true";
    private static final int HOLO_UPDATE_METADATA_MSG_DELAY = 1000;
    private static final int INDICATE_SYSTEM_READY_RETRY_DELAY_MS = 1000;
    private static final java.lang.String KEY_AUDIO_ATTRIBUTES = "audio_attributes";
    private static final java.lang.String KEY_AUDIO_MIXER_ATTRIBUTES = "audio_mixer_attributes";
    protected static final boolean LOGD;
    static final int LOG_NB_EVENTS_DEVICE_CONNECTION = 50;
    static final int LOG_NB_EVENTS_DYN_POLICY = 10;
    static final int LOG_NB_EVENTS_FORCE_USE = 20;
    static final int LOG_NB_EVENTS_LIFECYCLE = 20;
    static final int LOG_NB_EVENTS_LOUDNESS_CODEC = 30;
    static final int LOG_NB_EVENTS_PHONE_STATE = 20;
    static final int LOG_NB_EVENTS_SOUND_DOSE = 30;
    static final int LOG_NB_EVENTS_SPATIAL = 30;
    static final int LOG_NB_EVENTS_VOLUME = 100;
    protected static int[] MAX_STREAM_VOLUME = null;
    protected static final float MIN_ALARM_ATTENUATION_NON_PRIVILEGED_DB = -36.0f;
    protected static int[] MIN_STREAM_VOLUME = null;
    private static final int MSG_ACCESSORY_PLUG_MEDIA_UNMUTE = 21;
    private static final int MSG_ADD_ASSISTANT_SERVICE_UID = 44;
    private static final int MSG_AUDIO_SERVER_DIED = 4;
    private static final int MSG_BINAURAL_RECORD_RESTORE = 81;
    private static final int MSG_BINAURAL_RECORD_SUSPEND = 80;
    private static final int MSG_BROADCAST_MASTER_MUTE = 55;
    private static final int MSG_BROADCAST_MICROPHONE_MUTE = 30;
    private static final int MSG_BT_DEV_CHANGED = 38;
    private static final int MSG_CHECK_MODE_FOR_UID = 31;
    private static final int MSG_CONFIGURATION_CHANGED = 54;
    private static final int MSG_DISABLE_AUDIO_FOR_UID = 100;
    private static final int MSG_DISPATCH_AUDIO_MODE = 40;
    private static final int MSG_DISPATCH_AUDIO_SERVER_STATE = 23;
    private static final int MSG_DISPATCH_DEVICE_VOLUME_BEHAVIOR = 47;
    private static final int MSG_DISPATCH_PREFERRED_MIXER_ATTRIBUTES = 52;
    private static final int MSG_DYN_POLICY_MIX_STATE_UPDATE = 19;
    private static final int MSG_ENABLE_SURROUND_FORMATS = 24;
    private static final int MSG_FOLD_UPDATE = 49;
    private static final int MSG_HDMI_VOLUME_CHECK = 28;
    private static final int MSG_HOLO_UPDATE_METADATA = 118;
    private static final int MSG_INDICATE_SYSTEM_READY = 20;
    private static final int MSG_INIT_ADI_DEVICE_STATES = 103;
    private static final int MSG_INIT_HEADTRACKING_SENSORS = 42;
    private static final int MSG_INIT_SPATIALIZER = 102;
    private static final int MSG_INIT_STREAMS_VOLUMES = 101;
    private static final int MSG_LOAD_SOUND_EFFECTS = 7;
    private static final int MSG_NOTIFY_VOL_EVENT = 22;
    private static final int MSG_NO_LOG_FOR_PLAYER_I = 51;
    private static final int MSG_OBSERVE_DEVICES_FOR_ALL_STREAMS = 27;
    public static final int MSG_OTHER_CHECK_PRIVACY_CALL = 112;
    private static final int MSG_OTHER_CHECK_VOCAL_PROMINENCE = 116;
    private static final int MSG_PERSIST_RINGER_MODE = 3;
    private static final int MSG_PERSIST_VOLUME = 1;
    private static final int MSG_PERSIST_VOLUME_GROUP = 2;
    private static final int MSG_PLAYBACK_CONFIG_CHANGE = 29;
    private static final int MSG_PLAY_SOUND_EFFECT = 5;
    private static final int MSG_PREDISPATCH_AUDIO_MODE = 888;
    private static final int MSG_RECORDING_CONFIG_CHANGE = 37;
    private static final int MSG_REINIT_VOLUMES = 34;
    private static final int MSG_REMOVE_ASSISTANT_SERVICE_UID = 45;
    private static final int MSG_RESET_SPATIALIZER = 50;
    private static final int MSG_ROTATION_UPDATE = 48;
    public static final int MSG_ROUTE_CHECK_PRIVACY_CALL = 113;
    private static final int MSG_ROUTE_CHECK_PRIVACY_CALL_SOFTWARE_MODE = 119;
    private static final int MSG_ROUTE_CHECK_VOCAL_PROMINENCE = 117;
    private static final int MSG_ROUTING_UPDATED = 41;
    private static final int MSG_SET_ALL_VOLUMES = 10;
    private static final int MSG_SET_DEVICE_STREAM_VOLUME = 26;
    static final int MSG_SET_DEVICE_VOLUME = 0;
    private static final int MSG_SET_FORCE_USE = 8;
    private static final int MSG_SET_GAMEMODE = 114;
    private static final int MSG_SET_PRIVACY_CALL_SOFTWARE_MODE_STATE = 120;
    private static final int MSG_STREAM_DEVICES_CHANGED = 32;
    private static final int MSG_SYSTEM_READY = 16;
    private static final int MSG_UNLOAD_SOUND_EFFECTS = 15;
    private static final int MSG_UNMUTE_STREAM_ON_SINGLE_VOL_DEVICE = 18;
    private static final int MSG_UPDATE_A11Y_SERVICE_UIDS = 35;
    private static final int MSG_UPDATE_ACTIVE_ASSISTANT_SERVICE_UID = 46;
    private static final int MSG_UPDATE_AUDIO_MODE = 36;
    private static final int MSG_UPDATE_RINGER_MODE = 25;
    private static final int MSG_UPDATE_VOLUME_STATES_FOR_DEVICE = 33;
    static final int MUSICFX_HELPER_MSG_START = 1100;
    private static final int[] NO_ACTIVE_ASSISTANT_SERVICE_UIDS;
    private static final java.lang.String OPLUS_VC_DOWNLINK_MUTE_MODE = "OPLUS_VC_DOWNLINK_MUTE_MODE=1";
    public static final int OTHER_CHANGE_TRIGGERED = 0;
    private static final java.lang.String PARAMETER_GET_MICROPHONE_FORBID = "OPLUS_AUDIO_GET_MICROPHONE_FORBID";
    private static final int PERSIST_DELAY = 500;
    private static final int PID_TOOL = 10;
    private static final java.lang.String REQUEST_FOCUS_CHECK_BYPASS_LIST = "request-focus-check-bypass-list";
    private static final java.lang.String[] RINGER_MODE_NAMES;
    public static final int ROUTE_CHANGE_TRIGGERED = 1;
    static final int SAFE_MEDIA_VOLUME_MSG_START = 1000;
    private static final int SENDMSG_NOOP = 1;
    private static final int SENDMSG_QUEUE = 2;
    private static final int SENDMSG_REPLACE = 0;
    private static final boolean SPATIAL_AUDIO_ENABLED_DEFAULT = true;
    private static final int[] STREAM_VOLUME_OPS;
    private static final java.lang.String TAG = "AS.AudioService";
    private static final int TOUCH_EXPLORE_STREAM_TYPE_OVERRIDE_DELAY_MS = 1000;
    private static final android.os.VibrationAttributes TOUCH_VIBRATION_ATTRIBUTES;
    private static final int UNMUTE_STREAM_DELAY = 350;
    private static final int UNSET_INDEX = -1;
    private static boolean mBRStateOfCamera = false;
    private static java.util.HashMap<java.lang.String, java.lang.String> mCachedParams = null;
    private static int mCurrentDeviceRoute = 0;
    protected static boolean mDebugLog = false;
    private static final java.lang.String mMetricsId = "audio.service.";
    protected static int[] mStreamVolumeAlias;
    protected static final boolean mVolumeNotShare;
    static android.media.VolumeInfo sDefaultVolumeInfo;
    static final com.android.server.utils.EventLogger sDeviceLogger;
    static final com.android.server.utils.EventLogger sForceUseLogger;
    private static boolean sIndependentA11yVolume;
    static final com.android.server.utils.EventLogger sLifecycleLogger;
    static final com.android.server.utils.EventLogger sMuteLogger;
    protected static volatile int sRingerAndZenModeMutedStreams;
    static final com.android.server.utils.EventLogger sSpatialLogger;
    private static int sStreamOverrideDelayMs;
    private static final android.util.SparseArray<com.android.server.audio.AudioService.VolumeGroupState> sVolumeGroupStates;
    static final com.android.server.utils.EventLogger sVolumeLogger;
    private final int[] STREAM_VOLUME_ALIAS_DEFAULT;
    private final int[] STREAM_VOLUME_ALIAS_NONE;
    private final int[] STREAM_VOLUME_ALIAS_TELEVISION;
    private final int[] STREAM_VOLUME_ALIAS_VOICE;
    java.util.Set<java.lang.Integer> mAbsVolumeMultiModeCaseDevices;
    java.util.Map<java.lang.Integer, com.android.server.audio.AudioService.AbsoluteVolumeDeviceInfo> mAbsoluteVolumeDeviceInfoMap;
    private int[] mAccessibilityServiceUids;
    private final java.lang.Object mAccessibilityServiceUidsLock;
    private int[] mActiveAssistantServiceUids;
    private final android.app.ActivityManagerInternal mActivityManagerInternal;
    private final android.app.AppOpsManager mAppOps;
    private com.android.server.audio.IAudioServiceExt mAsExt;
    public com.android.server.audio.IAudioServiceSocExt mAsSocExt;
    private com.android.server.audio.AudioService.AudioServiceWrapper mAsWrapper;
    private final android.util.ArraySet<java.lang.Integer> mAssistantUids;
    private android.os.PowerManager.WakeLock mAudioEventWakeLock;
    private com.android.server.audio.AudioService.AudioHandler mAudioHandler;
    private final java.util.HashMap<android.os.IBinder, com.android.server.audio.AudioService.AudioPolicyProxy> mAudioPolicies;
    private final com.android.server.audio.AudioPolicyFacade mAudioPolicy;
    private int mAudioPolicyCounter;
    private final java.util.HashMap<android.os.IBinder, com.android.server.audio.AudioService.AsdProxy> mAudioServerStateListeners;
    private final com.android.server.audio.AudioSystemAdapter mAudioSystem;
    private final android.media.AudioSystem.ErrorCallback mAudioSystemCallback;
    private com.android.server.audio.AudioService.AudioSystemThread mAudioSystemThread;
    private final com.android.server.audio.AudioVolumeGroupHelperBase mAudioVolumeGroupHelper;
    private volatile boolean mAvrcpAbsVolSupported;
    private volatile boolean mBleVcAbsVolSupported;
    private final android.os.HandlerThread mBroadcastHandlerThread;
    private boolean mBtScoOnByApp;
    private final java.util.HashMap<java.lang.Integer, java.lang.Integer> mCachedAbsVolDrivingStreams;
    private final java.lang.Object mCachedAbsVolDrivingStreamsLock;
    private boolean mCameraSoundForced;
    private final android.content.ContentResolver mContentResolver;
    final android.content.Context mContext;
    private final com.android.server.audio.AudioDeviceBroker mDeviceBroker;
    final android.os.RemoteCallbackList<android.media.IDeviceVolumeBehaviorDispatcher> mDeviceVolumeBehaviorDispatchers;
    private android.hardware.display.DisplayManager.DisplayListener mDisplayListener;
    private android.hardware.display.DisplayManager mDisplayManager;
    private boolean mDockAudioMediaEnabled;
    private int mDockState;
    private final android.media.AudioSystem.DynamicPolicyCallback mDynPolicyCallback;
    private final com.android.server.utils.EventLogger mDynPolicyLogger;
    private java.lang.String mEnabledSurroundFormats;
    private int mEncodedSurroundMode;
    private android.media.audiopolicy.IAudioPolicyCallback mExtVolumeController;
    private final java.lang.Object mExtVolumeControllerLock;
    java.util.Set<java.lang.Integer> mFixedVolumeDevices;
    private com.android.server.audio.AudioService.ForceControlStreamClient mForceControlStreamClient;
    private final java.lang.Object mForceControlStreamLock;
    java.util.Set<java.lang.Integer> mFullVolumeDevices;
    private final com.android.server.audio.HardeningEnforcer mHardeningEnforcer;
    private final boolean mHasAudioEffectCombined;
    private final boolean mHasSpatializerEffect;
    private final boolean mHasSpeakerSpatializer;
    private final boolean mHasVibrator;
    private android.hardware.hdmi.HdmiAudioSystemClient mHdmiAudioSystemClient;
    private boolean mHdmiCecVolumeControlEnabled;
    private final java.lang.Object mHdmiClientLock;
    private com.android.server.audio.AudioService.MyHdmiControlStatusChangeListenerCallback mHdmiControlStatusChangeListenerCallback;
    private android.hardware.hdmi.HdmiControlManager mHdmiManager;
    private android.hardware.hdmi.HdmiPlaybackClient mHdmiPlaybackClient;
    private boolean mHdmiSystemAudioSupported;
    private android.hardware.hdmi.HdmiTvClient mHdmiTvClient;
    private boolean mHomeSoundEffectEnabled;
    private int mInputMethodServiceUid;
    private final java.lang.Object mInputMethodServiceUidLock;
    private boolean mIsCallScreeningModeSupported;
    private final boolean mIsSingleVolume;
    private int mLatestSetModeUid;
    private final com.android.server.audio.LoudnessCodecHelper mLoudnessCodecHelper;
    private long mLoweredFromNormalToVibrateTime;
    private java.util.concurrent.atomic.AtomicBoolean mMasterMute;
    private final com.android.server.audio.MediaFocusControl mMediaFocusControl;
    private java.util.concurrent.atomic.AtomicBoolean mMediaPlaybackActive;
    private volatile android.media.session.MediaSessionManager mMediaSessionManager;
    private boolean mMicMuteFromApi;
    private boolean mMicMuteFromPrivacyToggle;
    private boolean mMicMuteFromRestrictions;
    private boolean mMicMuteFromSwitch;
    private boolean mMicMuteFromSystemCached;
    private java.util.concurrent.atomic.AtomicInteger mMode;
    final android.os.RemoteCallbackList<android.media.IAudioModeDispatcher> mModeDispatchers;
    private final com.android.server.utils.EventLogger mModeLogger;
    private final boolean mMonitorRotation;
    private final com.android.server.audio.MusicFxHelper mMusicFxHelper;
    private int mMuteAffectedStreams;
    final android.os.RemoteCallbackList<android.media.IMuteAwaitConnectionCallback> mMuteAwaitConnectionDispatchers;
    private final java.lang.Object mMuteAwaitConnectionLock;
    private int[] mMutedUsagesAwaitingConnection;
    private android.media.AudioDeviceAttributes mMutingExpectedDevice;
    private com.android.server.audio.AudioService.MyHdmiCecVolumeControlFeatureListener mMyHdmiCecVolumeControlFeatureListener;
    private boolean mNavigationRepeatSoundEffectsEnabled;
    private android.app.NotificationManager mNm;
    private boolean mNotifAliasRing;
    private int mPendingMode;
    private final com.android.server.audio.AudioServerPermissionProvider mPermissionProvider;
    private final int mPlatformType;
    private final android.media.IPlaybackConfigDispatcher mPlaybackActivityMonitor;
    private final com.android.server.audio.PlaybackActivityMonitor mPlaybackMonitor;
    final android.os.RemoteCallbackList<android.media.IPreferredMixerAttributesDispatcher> mPrefMixerAttrDispatcher;
    private float[] mPrescaleAbsoluteVolume;
    private int mPrevVolDirection;
    private int mPrimaryAssistantUid;
    private android.media.projection.IMediaProjectionManager mProjectionService;
    private final android.content.BroadcastReceiver mReceiver;
    private final com.android.server.audio.RecordingActivityMonitor mRecordMonitor;
    private com.android.server.audio.AudioService.RestorableParameters mRestorableParameters;
    private int mRingerMode;
    private int mRingerModeAffectedStreams;
    private final boolean mRingerModeAffectsAlarm;
    private android.media.AudioManagerInternal.RingerModeDelegate mRingerModeDelegate;
    private int mRingerModeExternal;
    private volatile android.media.IRingtonePlayer mRingtonePlayer;
    private final java.util.ArrayList<com.android.server.audio.AudioService.RmtSbmxFullVolDeathHandler> mRmtSbmxFullVolDeathHandlers;
    private int mRmtSbmxFullVolRefCount;
    com.android.server.audio.AudioService.RoleObserver mRoleObserver;
    private boolean mRttEnabled;
    private final android.hardware.SensorPrivacyManagerInternal mSensorPrivacyManagerInternal;
    final java.util.ArrayList<com.android.server.audio.AudioService.SetModeDeathHandler> mSetModeDeathHandlers;
    private final com.android.server.audio.SettingsAdapter mSettings;
    private final java.lang.Object mSettingsLock;
    private com.android.server.audio.AudioService.SettingsObserver mSettingsObserver;
    private com.android.server.audio.SoundEffectsHelper mSfxHelper;
    private final com.android.server.audio.SoundDoseHelper mSoundDoseHelper;
    private final com.android.server.audio.SpatializerHelper mSpatializerHelper;
    final android.os.RemoteCallbackList<android.media.IStreamAliasingDispatcher> mStreamAliasingDispatchers;
    private com.android.server.audio.AudioService.VolumeStreamState[] mStreamStates;
    private android.telephony.SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionChangedListener;
    private int[] mSupportedSystemUsages;
    private final java.lang.Object mSupportedSystemUsagesLock;
    private boolean mSupportsMicPrivacyToggle;
    private boolean mSurroundModeChanged;
    private boolean mSystemReady;
    private final com.android.server.audio.SystemServerAdapter mSystemServer;
    private final android.app.IUidObserver mUidObserver;
    private final boolean mUseFixedVolume;
    private final boolean mUseVolumeGroupAliases;
    private final com.android.server.pm.UserManagerInternal mUserManagerInternal;
    private int mUserMutableStreams;
    private final com.android.server.pm.UserManagerInternal.UserRestrictionsListener mUserRestrictionsListener;
    private boolean mUserSelectedVolumeControlStream;
    private boolean mUserSwitchedReceived;
    private boolean mUserSwitching;
    private boolean mVendorBeforeAndroidU;
    private int mVibrateSetting;
    private android.os.Vibrator mVibrator;
    private java.util.concurrent.atomic.AtomicBoolean mVoicePlaybackActive;
    private final android.media.IRecordingConfigDispatcher mVoiceRecordingActivityMonitor;
    private int mVolumeControlStream;
    private final com.android.server.audio.AudioService.VolumeController mVolumeController;
    private android.media.VolumePolicy mVolumePolicy;
    private int mZenModeAffectedStreams;
    private volatile boolean mleCallVcSupportsAbsoluteVolume;
    private volatile boolean mleVcSupportsAbsoluteVolume;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface BtProfile {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface BtProfileConnectionState {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ConnectionState {
    }

    public interface ISafeHearingVolumeController {
        void postDisplayCsdWarning(int i, int i2);

        void postDisplaySafeVolumeWarning(int i);
    }

    static {
        LOGD = "eng".equals(android.os.Build.TYPE) || "userdebug".equals(android.os.Build.TYPE);
        mDebugLog = false;
        DEBUG_MODE = LOGD;
        DEBUG_AP = LOGD;
        DEBUG_VOL = LOGD;
        DEBUG_DEVICES = LOGD;
        DEBUG_COMM_RTE = LOGD;
        mBRStateOfCamera = false;
        mCachedParams = new java.util.HashMap<>();
        mCurrentDeviceRoute = 2;
        FEATURE_SPEAKER_CLEAN_SUPPORT = android.os.SystemProperties.getBoolean("ro.oplus.audio.speaker_clean", false);
        NO_ACTIVE_ASSISTANT_SERVICE_UIDS = new int[0];
        MAX_STREAM_VOLUME = new int[]{5, 7, 7, 15, 7, 7, 15, 7, 15, 15, 15, 15};
        MIN_STREAM_VOLUME = new int[]{1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0};
        STREAM_VOLUME_OPS = new int[]{34, 36, 35, 36, 37, 38, 39, 36, 36, 36, 64, 36};
        mVolumeNotShare = android.os.SystemProperties.getBoolean("ro.config.volume_not_share", false);
        TOUCH_VIBRATION_ATTRIBUTES = android.os.VibrationAttributes.createForUsage(18);
        DEFAULT_ARC_AUDIO_DESCRIPTOR = new byte[]{9, 127, 7};
        DEVICE_MEDIA_UNMUTED_ON_PLUG_SET = new java.util.HashSet();
        DEVICE_MEDIA_UNMUTED_ON_PLUG_SET.add(4);
        DEVICE_MEDIA_UNMUTED_ON_PLUG_SET.add(8);
        DEVICE_MEDIA_UNMUTED_ON_PLUG_SET.add(131072);
        DEVICE_MEDIA_UNMUTED_ON_PLUG_SET.add(134217728);
        DEVICE_MEDIA_UNMUTED_ON_PLUG_SET.addAll(android.media.AudioSystem.DEVICE_OUT_ALL_A2DP_SET);
        DEVICE_MEDIA_UNMUTED_ON_PLUG_SET.addAll(android.media.AudioSystem.DEVICE_OUT_ALL_BLE_SET);
        DEVICE_MEDIA_UNMUTED_ON_PLUG_SET.addAll(android.media.AudioSystem.DEVICE_OUT_ALL_USB_SET);
        sVolumeGroupStates = new android.util.SparseArray<>();
        sIndependentA11yVolume = false;
        sLifecycleLogger = new com.android.server.utils.EventLogger(20, "audio services lifecycle");
        sMuteLogger = new com.android.server.utils.EventLogger(30, "mute commands");
        sDeviceLogger = new com.android.server.utils.EventLogger(50, "wired/A2DP/hearing aid device connection");
        sForceUseLogger = new com.android.server.utils.EventLogger(20, "force use (logged before setForceUse() is executed)");
        sVolumeLogger = new com.android.server.utils.EventLogger(100, "volume changes (logged when command received by AudioService)");
        sSpatialLogger = new com.android.server.utils.EventLogger(30, "spatial audio");
        RINGER_MODE_NAMES = new java.lang.String[]{"SILENT", "VIBRATE", com.android.server.utils.PriorityDump.PRIORITY_ARG_NORMAL};
    }

    public void setNotifAliasRingForTest(boolean alias) {
        super.setNotifAliasRingForTest_enforcePermission();
        boolean update = this.mNotifAliasRing != alias;
        this.mNotifAliasRing = alias;
        if (update) {
            updateStreamVolumeAlias(true, "AudioServiceTest");
        }
    }

    boolean isPlatformVoice() {
        return this.mPlatformType == 1;
    }

    boolean isPlatformTelevision() {
        return this.mPlatformType == 2;
    }

    boolean isPlatformAutomotive() {
        return this.mPlatformType == 3;
    }

    int getVssVolumeForDevice(int stream, int device) {
        return this.mStreamStates[stream].getIndex(device);
    }

    com.android.server.audio.AudioService.VolumeStreamState getVssVolumeForStream(int stream) {
        return this.mStreamStates[stream];
    }

    int getMaxVssVolumeForStream(int stream) {
        return this.mStreamStates[stream].getMaxIndex();
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class AbsoluteVolumeDeviceInfo {
        private final android.media.IAudioDeviceVolumeDispatcher mCallback;
        private final android.media.AudioDeviceAttributes mDevice;
        private int mDeviceVolumeBehavior;
        private final boolean mHandlesVolumeAdjustment;
        private final java.util.List<android.media.VolumeInfo> mVolumeInfos;

        private AbsoluteVolumeDeviceInfo(android.media.AudioDeviceAttributes device, java.util.List<android.media.VolumeInfo> volumeInfos, android.media.IAudioDeviceVolumeDispatcher callback, boolean handlesVolumeAdjustment, int behavior) {
            this.mDevice = device;
            this.mVolumeInfos = volumeInfos;
            this.mCallback = callback;
            this.mHandlesVolumeAdjustment = handlesVolumeAdjustment;
            this.mDeviceVolumeBehavior = behavior;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.media.VolumeInfo getMatchingVolumeInfoForStream(final int streamType) {
            for (android.media.VolumeInfo volumeInfo : this.mVolumeInfos) {
                boolean streamTypeMatches = volumeInfo.hasStreamType() && volumeInfo.getStreamType() == streamType;
                boolean volumeGroupMatches = volumeInfo.hasVolumeGroup() && java.util.Arrays.stream(volumeInfo.getVolumeGroup().getLegacyStreamTypes()).anyMatch(new java.util.function.IntPredicate() { // from class: com.android.server.audio.AudioService$AbsoluteVolumeDeviceInfo$$ExternalSyntheticLambda0
                    @Override // java.util.function.IntPredicate
                    public final boolean test(int i) {
                        return com.android.server.audio.AudioService.AbsoluteVolumeDeviceInfo.lambda$getMatchingVolumeInfoForStream$0(streamType, i);
                    }
                });
                if (streamTypeMatches || volumeGroupMatches) {
                    return volumeInfo;
                }
            }
            return null;
        }

        static /* synthetic */ boolean lambda$getMatchingVolumeInfoForStream$0(int streamType, int s) {
            return s == streamType;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class RestorableParameters {
        private java.util.Map<java.lang.String, java.util.function.BooleanSupplier> mMap;

        private RestorableParameters() {
            this.mMap = new java.util.LinkedHashMap<java.lang.String, java.util.function.BooleanSupplier>() { // from class: com.android.server.audio.AudioService.RestorableParameters.1
                private static final int MAX_ENTRIES = 1000;

                @Override // java.util.LinkedHashMap
                protected boolean removeEldestEntry(java.util.Map.Entry<java.lang.String, java.util.function.BooleanSupplier> entry) {
                    if (size() <= 1000) {
                        return false;
                    }
                    android.util.Log.w(com.android.server.audio.AudioService.TAG, "Parameter map exceeds 1000 removing " + ((java.lang.Object) entry.getKey()));
                    return true;
                }
            };
        }

        public int setParameters(java.lang.String id, final java.lang.String parameter) {
            int status;
            java.util.Objects.requireNonNull(id, "id must not be null");
            java.util.Objects.requireNonNull(parameter, "parameter must not be null");
            synchronized (this.mMap) {
                status = android.media.AudioSystem.setParameters(parameter);
                if (status == 0) {
                    queueRestoreWithRemovalIfTrue(id, new java.util.function.BooleanSupplier() { // from class: com.android.server.audio.AudioService$RestorableParameters$$ExternalSyntheticLambda1
                        @Override // java.util.function.BooleanSupplier
                        public final boolean getAsBoolean() {
                            return com.android.server.audio.AudioService.RestorableParameters.lambda$setParameters$0(parameter);
                        }
                    });
                }
            }
            return status;
        }

        static /* synthetic */ boolean lambda$setParameters$0(java.lang.String parameter) {
            return android.media.AudioSystem.setParameters(parameter) != 0;
        }

        public void queueRestoreWithRemovalIfTrue(java.lang.String id, java.util.function.BooleanSupplier supplier) {
            java.util.Objects.requireNonNull(id, "id must not be null");
            synchronized (this.mMap) {
                if (supplier != null) {
                    this.mMap.put(id, supplier);
                } else {
                    this.mMap.remove(id);
                }
            }
        }

        public void restoreAll() {
            synchronized (this.mMap) {
                this.mMap.values().removeIf(new java.util.function.Predicate() { // from class: com.android.server.audio.AudioService$RestorableParameters$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return ((java.util.function.BooleanSupplier) obj).getAsBoolean();
                    }
                });
            }
        }
    }

    public static java.lang.String makeAlsaAddressString(int card, int device) {
        return "card=" + card + ";device=" + device;
    }

    private static class AudioVolumeGroupHelper extends com.android.server.audio.AudioVolumeGroupHelperBase {
        private AudioVolumeGroupHelper() {
        }

        @Override // com.android.server.audio.AudioVolumeGroupHelperBase
        public java.util.List<android.media.audiopolicy.AudioVolumeGroup> getAudioVolumeGroups() {
            return android.media.audiopolicy.AudioVolumeGroup.getAudioVolumeGroups();
        }
    }

    public static final class Lifecycle extends com.android.server.SystemService {
        private com.android.server.audio.AudioService mService;

        public Lifecycle(android.content.Context context) {
            com.android.server.audio.AudioServerPermissionProvider audioServerPermissionProviderInitializeAudioServerPermissionProvider;
            super(context);
            java.util.concurrent.ExecutorService audioserverLifecycleExecutor = java.util.concurrent.Executors.newSingleThreadExecutor();
            com.android.server.audio.DefaultAudioPolicyFacade audioPolicyFacade = new com.android.server.audio.DefaultAudioPolicyFacade(audioserverLifecycleExecutor);
            com.android.server.audio.AudioSystemAdapter defaultAdapter = com.android.server.audio.AudioSystemAdapter.getDefaultAdapter();
            com.android.server.audio.SystemServerAdapter defaultAdapter2 = com.android.server.audio.SystemServerAdapter.getDefaultAdapter(context);
            com.android.server.audio.SettingsAdapter defaultAdapter3 = com.android.server.audio.SettingsAdapter.getDefaultAdapter();
            com.android.server.audio.AudioService.AudioVolumeGroupHelper audioVolumeGroupHelper = new com.android.server.audio.AudioService.AudioVolumeGroupHelper();
            android.app.AppOpsManager appOpsManager = (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
            android.os.PermissionEnforcer permissionEnforcerFromContext = android.os.PermissionEnforcer.fromContext(context);
            if (com.android.media.audio.Flags.audioserverPermissions()) {
                audioServerPermissionProviderInitializeAudioServerPermissionProvider = com.android.server.audio.AudioService.initializeAudioServerPermissionProvider(context, audioPolicyFacade, audioserverLifecycleExecutor);
            } else {
                audioServerPermissionProviderInitializeAudioServerPermissionProvider = null;
            }
            this.mService = new com.android.server.audio.AudioService(context, defaultAdapter, defaultAdapter2, defaultAdapter3, audioVolumeGroupHelper, audioPolicyFacade, null, appOpsManager, permissionEnforcerFromContext, audioServerPermissionProviderInitializeAudioServerPermissionProvider);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            publishBinderService("audio", this.mService);
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (phase == 550) {
                this.mService.systemReady();
            }
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(10:(5:148|83|84|141|85)|(9:87|88|139|89|137|99|100|134|(6:146|102|(5:135|104|105|159|121)(5:108|157|109|110|111)|120|158|121)(3:156|114|115))(3:90|91|155)|92|98|137|99|100|134|(0)(0)|81) */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x05f6, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x05f8, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x05f9, code lost:
    
        r30 = r6;
        r31 = r7;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0630  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0634  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x06d0  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x071d  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x040f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:146:0x0556 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:148:0x04c1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:156:0x05c3 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x03c0  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x03de  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x03e4  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x03ef  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0403  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x044a  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0462  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0469  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0481  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0495  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public AudioService(android.content.Context r33, com.android.server.audio.AudioSystemAdapter r34, com.android.server.audio.SystemServerAdapter r35, com.android.server.audio.SettingsAdapter r36, com.android.server.audio.AudioVolumeGroupHelperBase r37, com.android.server.audio.AudioPolicyFacade r38, android.os.Looper r39, android.app.AppOpsManager r40, android.os.PermissionEnforcer r41, com.android.server.audio.AudioServerPermissionProvider r42) {
        /*
            Method dump skipped, instruction units count: 2106
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.AudioService.<init>(android.content.Context, com.android.server.audio.AudioSystemAdapter, com.android.server.audio.SystemServerAdapter, com.android.server.audio.SettingsAdapter, com.android.server.audio.AudioVolumeGroupHelperBase, com.android.server.audio.AudioPolicyFacade, android.os.Looper, android.app.AppOpsManager, android.os.PermissionEnforcer, com.android.server.audio.AudioServerPermissionProvider):void");
    }

    private void initVolumeStreamStates() {
        int numStreamTypes = android.media.AudioSystem.getNumStreamTypes();
        synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
            for (int streamType = numStreamTypes - 1; streamType >= 0; streamType--) {
                com.android.server.audio.AudioService.VolumeStreamState streamState = this.mStreamStates[streamType];
                int groupId = getVolumeGroupForStreamType(streamType);
                if (groupId != -1 && sVolumeGroupStates.indexOfKey(groupId) >= 0) {
                    streamState.setVolumeGroupState(sVolumeGroupStates.get(groupId));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInitStreamsAndVolumes() {
        synchronized (this.mSettingsLock) {
            this.mCameraSoundForced = readCameraSoundForced();
            sendMsg(this.mAudioHandler, 8, 2, 4, this.mCameraSoundForced ? 11 : 0, new java.lang.String("AudioService ctor"), 0);
        }
        createStreamStates();
        initVolumeGroupStates();
        this.mSoundDoseHelper.initSafeMediaVolumeIndex();
        initVolumeStreamStates();
        sRingerAndZenModeMutedStreams = 0;
        sMuteLogger.enqueue(new com.android.server.audio.AudioServiceEvents.RingerZenMutedStreamsEvent(sRingerAndZenModeMutedStreams, "onInitStreamsAndVolumes"));
        setRingerModeInt(getRingerModeInternal(), false);
        if (!com.android.media.audio.Flags.disablePrescaleAbsoluteVolume()) {
            float[] preScale = {this.mContext.getResources().getFraction(android.R.fraction.config_prescaleAbsoluteVolume_index1, 1, 1), this.mContext.getResources().getFraction(android.R.fraction.config_prescaleAbsoluteVolume_index2, 1, 1), this.mContext.getResources().getFraction(android.R.fraction.config_prescaleAbsoluteVolume_index3, 1, 1)};
            for (int i = 0; i < preScale.length; i++) {
                if (0.0f <= preScale[i] && preScale[i] <= 1.0f) {
                    this.mPrescaleAbsoluteVolume[i] = preScale[i];
                }
            }
        }
        this.mAsSocExt.initAudioServiceExtInstance();
        initExternalEventReceivers();
        checkVolumeRangeInitialization("AudioService()");
        if (this.mAsExt.isSuperVolumeSupported()) {
            this.mAsExt.readPersistSuperVolume();
        }
        this.mAsExt.readAbsA2dpVolume();
        synchronized (this.mCachedAbsVolDrivingStreamsLock) {
            this.mCachedAbsVolDrivingStreams.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda21
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    this.f$0.lambda$onInitStreamsAndVolumes$2((java.lang.Integer) obj, (java.lang.Integer) obj2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onInitStreamsAndVolumes$2(java.lang.Integer dev, java.lang.Integer stream) {
        this.mAudioSystem.setDeviceAbsoluteVolumeEnabled(dev.intValue(), "", true, stream.intValue());
    }

    private android.media.session.MediaSessionManager getMediaSessionManager() {
        if (this.mMediaSessionManager == null) {
            this.mMediaSessionManager = (android.media.session.MediaSessionManager) this.mContext.getSystemService("media_session");
        }
        return this.mMediaSessionManager;
    }

    private void initExternalEventReceivers() {
        this.mSettingsObserver = new com.android.server.audio.AudioService.SettingsObserver();
        android.content.IntentFilter intentFilter = new android.content.IntentFilter("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED");
        if (!this.mDeviceBroker.isScoManagedByAudio()) {
            intentFilter.addAction("android.bluetooth.headset.profile.action.ACTIVE_DEVICE_CHANGED");
        }
        intentFilter.addAction("android.intent.action.DOCK_EVENT");
        if (this.mDisplayManager == null) {
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
        }
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.intent.action.USER_BACKGROUND");
        intentFilter.addAction("android.intent.action.USER_FOREGROUND");
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        intentFilter.addAction("android.intent.action.PACKAGES_SUSPENDED");
        intentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
        if (this.mMonitorRotation) {
            com.android.server.audio.RotationHelper.init(this.mContext, this.mAudioHandler, new java.util.function.Consumer() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$initExternalEventReceivers$3((java.lang.Integer) obj);
                }
            }, new java.util.function.Consumer() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$initExternalEventReceivers$4((java.lang.Boolean) obj);
                }
            });
        }
        intentFilter.addAction("android.media.action.OPEN_AUDIO_EFFECT_CONTROL_SESSION");
        intentFilter.addAction("android.media.action.CLOSE_AUDIO_EFFECT_CONTROL_SESSION");
        intentFilter.addAction("com.android.server.audio.action.CHECK_MUSIC_ACTIVE");
        intentFilter.setPriority(1000);
        intentFilter.addAction(com.android.server.audio.IAudioServiceExt.ACTION_ATLAS_STREAM_MUTE_STATE);
        if (this.mAsExt.getPrivacyCallSupport() || this.mAsExt.getVocalProminenceSupport()) {
            intentFilter.addAction(com.android.server.audio.IAudioServiceExt.ACTION_AUDIO_DEVICE_ROUTE_CHANGED);
        }
        this.mAsSocExt.getBleIntentFilters(intentFilter);
        this.mContext.registerReceiverAsUser(this.mReceiver, android.os.UserHandle.ALL, intentFilter, null, this.mBroadcastHandlerThread.getThreadHandler(), 2);
        android.telephony.SubscriptionManager subscriptionManager = (android.telephony.SubscriptionManager) this.mContext.getSystemService(android.telephony.SubscriptionManager.class);
        if (subscriptionManager == null) {
            android.util.Log.e(TAG, "initExternalEventReceivers cannot create SubscriptionManager!");
        } else {
            subscriptionManager.addOnSubscriptionsChangedListener(this.mSubscriptionChangedListener);
        }
        if (this.mDisplayManager != null) {
            this.mDisplayManager.registerDisplayListener(this.mDisplayListener, this.mAudioHandler);
        }
    }

    public void systemReady() {
        sendMsg(this.mAudioHandler, 16, 2, 0, 0, null, 0);
    }

    private void updateVibratorInfos() {
        android.os.VibratorManager vibratorManager = (android.os.VibratorManager) this.mContext.getSystemService(android.os.VibratorManager.class);
        if (vibratorManager == null) {
            android.util.Slog.e(TAG, "Vibrator manager is not found");
            return;
        }
        int[] vibratorIds = vibratorManager.getVibratorIds();
        if (vibratorIds.length == 0) {
            android.util.Slog.d(TAG, "No vibrator found");
            return;
        }
        java.util.List<android.os.Vibrator> vibrators = new java.util.ArrayList<>(vibratorIds.length);
        for (int id : vibratorIds) {
            android.os.Vibrator vibrator = vibratorManager.getVibrator(id);
            if (vibrator == null) {
                android.util.Slog.w(TAG, "Vibrator(" + id + ") is not found");
            } else {
                vibrators.add(vibrator);
            }
        }
        if (vibrators.isEmpty()) {
            android.util.Slog.w(TAG, "Cannot find any available vibrator");
        } else {
            android.media.AudioSystem.setVibratorInfos(vibrators);
        }
    }

    public void onSystemReady() {
        this.mSystemReady = true;
        scheduleLoadSoundEffects();
        this.mDeviceBroker.onSystemReady();
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.hdmi.cec")) {
            synchronized (this.mHdmiClientLock) {
                this.mHdmiManager = (android.hardware.hdmi.HdmiControlManager) this.mContext.getSystemService(android.hardware.hdmi.HdmiControlManager.class);
                if (this.mHdmiManager != null) {
                    this.mHdmiManager.addHdmiControlStatusChangeListener(this.mHdmiControlStatusChangeListenerCallback);
                    this.mHdmiManager.addHdmiCecVolumeControlFeatureListener(this.mContext.getMainExecutor(), this.mMyHdmiCecVolumeControlFeatureListener);
                }
                this.mHdmiTvClient = this.mHdmiManager.getTvClient();
                if (this.mHdmiTvClient != null) {
                    this.mFixedVolumeDevices.removeAll(android.media.AudioSystem.DEVICE_ALL_HDMI_SYSTEM_AUDIO_AND_SPEAKER_SET);
                }
                this.mHdmiPlaybackClient = this.mHdmiManager.getPlaybackClient();
                this.mHdmiAudioSystemClient = this.mHdmiManager.getAudioSystemClient();
            }
        }
        if (this.mSupportsMicPrivacyToggle) {
            this.mSensorPrivacyManagerInternal.addSensorPrivacyListenerForAllUsers(1, new android.hardware.SensorPrivacyManagerInternal.OnUserSensorPrivacyChangedListener() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda17
                public final void onSensorPrivacyChanged(int i, boolean z) {
                    this.f$0.lambda$onSystemReady$5(i, z);
                }
            });
        }
        this.mNm = (android.app.NotificationManager) this.mContext.getSystemService("notification");
        this.mSoundDoseHelper.configureSafeMedia(true, TAG);
        initA11yMonitoring();
        this.mRoleObserver = new com.android.server.audio.AudioService.RoleObserver();
        this.mRoleObserver.register();
        onIndicateSystemReady();
        this.mMicMuteFromSystemCached = this.mAudioSystem.isMicrophoneMuted();
        setMicMuteFromSwitchInput();
        initMinStreamVolumeWithoutModifyAudioSettings();
        if (this.mAsExt.isAlertSliderSupported()) {
            this.mAsExt.createAlerSliderManager();
        }
        updateVibratorInfos();
        this.mAsSocExt.onSystemReadyExt();
        synchronized (this.mSupportedSystemUsagesLock) {
            android.media.AudioSystem.setSupportedSystemUsages(this.mSupportedSystemUsages);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemReady$5(int userId, boolean enabled) {
        if (userId == getCurrentUserId()) {
            this.mMicMuteFromPrivacyToggle = enabled;
            setMicrophoneMuteNoCallerCheck(getCurrentUserId());
        }
    }

    @Override // com.android.server.audio.AudioSystemAdapter.OnRoutingUpdatedListener
    public void onRoutingUpdatedFromNative() {
        sendMsg(this.mAudioHandler, 41, 0, 0, 0, null, 0);
    }

    void onRoutingUpdatedFromAudioThread() {
        if (this.mHasSpatializerEffect) {
            this.mSpatializerHelper.onRoutingUpdated();
        }
        checkMuteAwaitConnection();
        this.mAsExt.registerTelePhony();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: onRotationUpdate, reason: merged with bridge method [inline-methods] */
    public void lambda$initExternalEventReceivers$3(java.lang.Integer rotation) {
        this.mSpatializerHelper.setDisplayOrientation((float) ((((double) rotation.intValue()) * 3.141592653589793d) / 180.0d));
        java.lang.String rotationParameter = "rotation=" + rotation;
        sendMsg(this.mAudioHandler, 48, 0, 0, 0, rotationParameter, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: onFoldStateUpdate, reason: merged with bridge method [inline-methods] */
    public void lambda$initExternalEventReceivers$4(java.lang.Boolean foldState) {
        this.mSpatializerHelper.setFoldState(foldState.booleanValue());
        java.lang.String foldStateParameter = "device_folded=" + (foldState.booleanValue() ? kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_ON : kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_OFF);
        sendMsg(this.mAudioHandler, 49, 0, 0, 0, foldStateParameter, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: ignorePlayerLogs, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0(android.media.PlayerBase playerToIgnore) {
        sendMsg(this.mAudioHandler, 51, 0, playerToIgnore.getPlayerIId(), 0, null, 0);
    }

    @Override // com.android.server.audio.AudioSystemAdapter.OnVolRangeInitRequestListener
    public void onVolumeRangeInitRequestFromNative() {
        sendMsg(this.mAudioHandler, 34, 0, 0, 0, "onVolumeRangeInitRequestFromNative", 0);
    }

    class RoleObserver implements android.app.role.OnRoleHoldersChangedListener {
        private final java.util.concurrent.Executor mExecutor;
        private android.app.role.RoleManager mRm;

        RoleObserver() {
            this.mExecutor = com.android.server.audio.AudioService.this.mContext.getMainExecutor();
        }

        public void register() {
            this.mRm = (android.app.role.RoleManager) com.android.server.audio.AudioService.this.mContext.getSystemService("role");
            if (this.mRm != null) {
                this.mRm.addOnRoleHoldersChangedListenerAsUser(this.mExecutor, this, android.os.UserHandle.ALL);
                synchronized (com.android.server.audio.AudioService.this.mSettingsLock) {
                    com.android.server.audio.AudioService.this.updateAssistantUIdLocked(true);
                }
            }
        }

        public void onRoleHoldersChanged(java.lang.String roleName, android.os.UserHandle user) {
            if ("android.app.role.ASSISTANT".equals(roleName)) {
                synchronized (com.android.server.audio.AudioService.this.mSettingsLock) {
                    com.android.server.audio.AudioService.this.updateAssistantUIdLocked(false);
                }
            }
        }

        public java.lang.String getAssistantRoleHolder() {
            if (this.mRm == null) {
                return "";
            }
            java.util.List<java.lang.String> assistants = this.mRm.getRoleHolders("android.app.role.ASSISTANT");
            java.lang.String assitantPackage = assistants.size() == 0 ? "" : assistants.get(0);
            return assitantPackage;
        }
    }

    void onIndicateSystemReady() {
        if (android.media.AudioSystem.systemReady() == 0) {
            return;
        }
        sendMsg(this.mAudioHandler, 20, 0, 0, 0, null, 1000);
    }

    public void onAudioServerDied() {
        int forSys;
        if (!this.mSystemReady || android.media.AudioSystem.checkAudioFlinger() != 0) {
            android.util.Log.e(TAG, "Audioserver died.");
            sLifecycleLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("onAudioServerDied() audioserver died"));
            sendMsg(this.mAudioHandler, 4, 1, 0, 0, null, 500);
            return;
        }
        android.util.Log.i(TAG, "Audioserver started.");
        sLifecycleLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("onAudioServerDied() audioserver started"));
        updateAudioHalPids();
        android.media.AudioSystem.setParameters("restarting=true");
        java.lang.String params = new java.lang.String("");
        android.util.Log.i(TAG, "Cached params " + mCachedParams.toString());
        for (java.util.Map.Entry<java.lang.String, java.lang.String> parm : mCachedParams.entrySet()) {
            if (!params.isEmpty()) {
                params = params + ";";
            }
            android.util.Log.i(TAG, "Key " + parm.getKey() + " Value " + parm.getValue());
            params = ((params + parm.getKey()) + "=") + parm.getValue();
            android.util.Log.i(TAG, "Params " + params);
        }
        if (!params.isEmpty()) {
            android.util.Log.i(TAG, "Restore params " + params);
            android.media.AudioSystem.setParameters(params);
        } else {
            android.util.Log.i(TAG, "Empty cached params " + params);
        }
        readAndSetLowRamDevice();
        this.mIsCallScreeningModeSupported = android.media.AudioSystem.isCallScreeningModeSupported();
        this.mDeviceBroker.onAudioServerDied();
        synchronized (this.mDeviceBroker.mSetModeLock) {
            onUpdateAudioMode(-1, android.os.Process.myPid(), this.mContext.getPackageName(), true);
        }
        synchronized (this.mSettingsLock) {
            forSys = this.mCameraSoundForced ? 11 : 0;
        }
        this.mDeviceBroker.setForceUse_Async(4, forSys, "onAudioServerDied");
        onReinitVolumes("after audioserver restart");
        restoreVolumeGroups();
        updateMasterMono(this.mContentResolver);
        updateMasterBalance(this.mContentResolver);
        setRingerModeInt(getRingerModeInternal(), false);
        if (this.mMonitorRotation) {
            com.android.server.audio.RotationHelper.updateOrientation();
        }
        this.mRestorableParameters.restoreAll();
        this.mAsExt.updateInputDevice(this.mContentResolver);
        this.mAsExt.setSpatializerSpeakerState(this.mHasSpeakerSpatializer);
        synchronized (this.mSettingsLock) {
            int forDock = this.mDockAudioMediaEnabled ? 9 : 0;
            this.mDeviceBroker.setForceUse_Async(3, forDock, "onAudioServerDied");
            sendEncodedSurroundMode(this.mContentResolver, "onAudioServerDied");
            sendEnabledSurroundFormats(this.mContentResolver, true);
            android.media.AudioSystem.setRttEnabled(this.mRttEnabled);
            resetAssistantServicesUidsLocked();
        }
        synchronized (this.mAccessibilityServiceUidsLock) {
            android.media.AudioSystem.setA11yServicesUids(this.mAccessibilityServiceUids);
        }
        synchronized (this.mInputMethodServiceUidLock) {
            this.mAudioSystem.setCurrentImeUid(this.mInputMethodServiceUid);
        }
        synchronized (this.mHdmiClientLock) {
            if (this.mHdmiManager != null && this.mHdmiTvClient != null) {
                setHdmiSystemAudioSupported(this.mHdmiSystemAudioSupported);
            }
        }
        synchronized (this.mSupportedSystemUsagesLock) {
            android.media.AudioSystem.setSupportedSystemUsages(this.mSupportedSystemUsages);
        }
        synchronized (this.mAudioPolicies) {
            java.util.ArrayList<com.android.server.audio.AudioService.AudioPolicyProxy> invalidProxies = new java.util.ArrayList<>();
            for (com.android.server.audio.AudioService.AudioPolicyProxy policy : this.mAudioPolicies.values()) {
                int status = policy.connectMixes();
                if (status != 0) {
                    android.util.Log.e(TAG, "onAudioServerDied: error " + android.media.AudioSystem.audioSystemErrorToString(status) + " when connecting mixes for policy " + policy.toLogFriendlyString());
                    invalidProxies.add(policy);
                } else {
                    int deviceAffinitiesStatus = policy.setupDeviceAffinities();
                    if (deviceAffinitiesStatus != 0) {
                        android.util.Log.e(TAG, "onAudioServerDied: error " + android.media.AudioSystem.audioSystemErrorToString(deviceAffinitiesStatus) + " when connecting device affinities for policy " + policy.toLogFriendlyString());
                        invalidProxies.add(policy);
                    }
                }
            }
            invalidProxies.forEach(new java.util.function.Consumer() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda11
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.audio.AudioService.AudioPolicyProxy) obj).release();
                }
            });
        }
        synchronized (this.mPlaybackMonitor) {
            java.util.HashMap<java.lang.Integer, java.lang.Integer> allowedCapturePolicies = this.mPlaybackMonitor.getAllAllowedCapturePolicies();
            for (java.util.Map.Entry<java.lang.Integer, java.lang.Integer> entry : allowedCapturePolicies.entrySet()) {
                int result = this.mAudioSystem.setAllowedCapturePolicy(entry.getKey().intValue(), android.media.AudioAttributes.capturePolicyToFlags(entry.getValue().intValue(), 0));
                if (result != 0) {
                    android.util.Log.e(TAG, "Failed to restore capture policy, uid: " + entry.getKey() + ", capture policy: " + entry.getValue() + ", result: " + result);
                    this.mPlaybackMonitor.setAllowedCapturePolicy(entry.getKey().intValue(), 1);
                }
            }
        }
        this.mSpatializerHelper.reset(this.mHasSpatializerEffect);
        if (this.mMonitorRotation) {
            com.android.server.audio.RotationHelper.forceUpdate();
        }
        onIndicateSystemReady();
        synchronized (this.mCachedAbsVolDrivingStreamsLock) {
            this.mCachedAbsVolDrivingStreams.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda12
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    this.f$0.lambda$onAudioServerDied$7((java.lang.Integer) obj, (java.lang.Integer) obj2);
                }
            });
        }
        android.media.AudioSystem.setParameters("restarting=false");
        sendBroadcastToAll(new android.content.Intent("audio_server_restarted"), null);
        this.mSoundDoseHelper.reset(true);
        sendMsg(this.mAudioHandler, 23, 2, 1, 0, null, 0);
        setMicrophoneMuteNoCallerCheck(getCurrentUserId());
        setMicMuteFromSwitchInput();
        updateVibratorInfos();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAudioServerDied$7(java.lang.Integer dev, java.lang.Integer stream) {
        this.mAudioSystem.setDeviceAbsoluteVolumeEnabled(dev.intValue(), "", true, stream.intValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRemoveAssistantServiceUids(int[] uids) {
        synchronized (this.mSettingsLock) {
            removeAssistantServiceUidsLocked(uids);
        }
    }

    private void removeAssistantServiceUidsLocked(int[] uids) {
        boolean changed = false;
        for (int index = 0; index < uids.length; index++) {
            if (!this.mAssistantUids.remove(java.lang.Integer.valueOf(uids[index]))) {
                android.util.Slog.e(TAG, android.text.TextUtils.formatSimple("Cannot remove assistant service, uid(%d) not present", new java.lang.Object[]{java.lang.Integer.valueOf(uids[index])}));
            } else {
                changed = true;
            }
        }
        if (changed) {
            updateAssistantServicesUidsLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAddAssistantServiceUids(int[] uids) {
        synchronized (this.mSettingsLock) {
            addAssistantServiceUidsLocked(uids);
        }
    }

    private void addAssistantServiceUidsLocked(int[] uids) {
        boolean changed = false;
        for (int index = 0; index < uids.length; index++) {
            if (uids[index] != -1) {
                if (!this.mAssistantUids.add(java.lang.Integer.valueOf(uids[index]))) {
                    android.util.Slog.e(TAG, android.text.TextUtils.formatSimple("Cannot add assistant service, uid(%d) already present", new java.lang.Object[]{java.lang.Integer.valueOf(uids[index])}));
                } else {
                    changed = true;
                }
            }
        }
        if (changed) {
            updateAssistantServicesUidsLocked();
        }
    }

    private void resetAssistantServicesUidsLocked() {
        this.mAssistantUids.clear();
        updateAssistantUIdLocked(true);
    }

    private void updateAssistantServicesUidsLocked() {
        int[] assistantUids = this.mAssistantUids.stream().mapToInt(new com.android.server.audio.AudioService$$ExternalSyntheticLambda0()).toArray();
        android.media.AudioSystem.setAssistantServicesUids(assistantUids);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActiveAssistantServiceUids() {
        int[] activeAssistantServiceUids;
        synchronized (this.mSettingsLock) {
            activeAssistantServiceUids = this.mActiveAssistantServiceUids;
        }
        android.media.AudioSystem.setActiveAssistantServicesUids(activeAssistantServiceUids);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onReinitVolumes(java.lang.String caller) {
        int res;
        int numStreamTypes = android.media.AudioSystem.getNumStreamTypes();
        int status = 0;
        int streamType = numStreamTypes - 1;
        while (true) {
            if (streamType < 0) {
                break;
            }
            com.android.server.audio.AudioService.VolumeStreamState streamState = this.mStreamStates[streamType];
            if (streamType == 4) {
                res = android.media.AudioSystem.initStreamVolume(streamType, 0, streamState.mIndexMax / 10);
            } else {
                int res2 = streamState.mIndexMin;
                res = android.media.AudioSystem.initStreamVolume(streamType, res2 / 10, streamState.mIndexMax / 10);
            }
            if (res != 0) {
                status = res;
                android.util.Log.e(TAG, "Failed to initStreamVolume (" + res + ") for stream " + streamType);
                break;
            } else {
                streamState.applyAllVolumes();
                streamType--;
            }
        }
        if (status != 0) {
            sLifecycleLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(caller + ": initStreamVolume failed with " + status + " will retry").printLog(1, TAG));
            sendMsg(this.mAudioHandler, 34, 1, 0, 0, caller, 2000);
        } else {
            if (!checkVolumeRangeInitialization(caller)) {
                return;
            }
            if (this.mAsExt.isSuperVolumeSupported()) {
                this.mAsExt.updateAllSuperVolumeToPolicy();
            }
            sLifecycleLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(caller + ": initStreamVolume succeeded").printLog(0, TAG));
        }
    }

    private boolean checkVolumeRangeInitialization(java.lang.String caller) {
        boolean success = true;
        int[] basicStreams = {4, 2, 3, 0, 10};
        for (int streamType : basicStreams) {
            android.media.AudioAttributes aa = new android.media.AudioAttributes.Builder().setInternalLegacyStreamType(streamType).build();
            if (android.media.AudioSystem.getMaxVolumeIndexForAttributes(aa) < 0 || android.media.AudioSystem.getMinVolumeIndexForAttributes(aa) < 0) {
                success = false;
                break;
            }
        }
        if (!success) {
            sLifecycleLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(caller + ": initStreamVolume succeeded but invalid mix/max levels, will retry").printLog(2, TAG));
            sendMsg(this.mAudioHandler, 34, 1, 0, 0, caller, 2000);
        }
        return success;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDispatchAudioServerStateChange(boolean state) {
        synchronized (this.mAudioServerStateListeners) {
            for (com.android.server.audio.AudioService.AsdProxy asdp : this.mAudioServerStateListeners.values()) {
                try {
                    asdp.callback().dispatchAudioServerStateChange(state);
                } catch (android.os.RemoteException e) {
                    android.util.Log.w(TAG, "Could not call dispatchAudioServerStateChange()", e);
                }
            }
        }
    }

    private void createAudioSystemThread() {
        this.mAudioSystemThread = new com.android.server.audio.AudioService.AudioSystemThread();
        this.mAudioSystemThread.start();
        waitForAudioHandlerCreation();
    }

    private void waitForAudioHandlerCreation() {
        synchronized (this) {
            while (this.mAudioHandler == null) {
                try {
                    wait();
                } catch (java.lang.InterruptedException e) {
                    android.util.Log.e(TAG, "Interrupted while waiting on volume handler.");
                }
            }
        }
    }

    public void setSupportedSystemUsages(int[] systemUsages) {
        super.setSupportedSystemUsages_enforcePermission();
        verifySystemUsages(systemUsages);
        synchronized (this.mSupportedSystemUsagesLock) {
            android.media.AudioSystem.setSupportedSystemUsages(systemUsages);
            this.mSupportedSystemUsages = systemUsages;
        }
    }

    public int[] getSupportedSystemUsages() {
        int[] iArrCopyOf;
        super.getSupportedSystemUsages_enforcePermission();
        synchronized (this.mSupportedSystemUsagesLock) {
            iArrCopyOf = java.util.Arrays.copyOf(this.mSupportedSystemUsages, this.mSupportedSystemUsages.length);
        }
        return iArrCopyOf;
    }

    private void verifySystemUsages(int[] systemUsages) {
        for (int i = 0; i < systemUsages.length; i++) {
            if (!android.media.AudioAttributes.isSystemUsage(systemUsages[i])) {
                throw new java.lang.IllegalArgumentException("Non-system usage provided: " + systemUsages[i]);
            }
        }
    }

    public java.util.List<android.media.audiopolicy.AudioProductStrategy> getAudioProductStrategies() {
        super.getAudioProductStrategies_enforcePermission();
        return android.media.audiopolicy.AudioProductStrategy.getAudioProductStrategies();
    }

    public java.util.List<android.media.audiopolicy.AudioVolumeGroup> getAudioVolumeGroups() {
        super.getAudioVolumeGroups_enforcePermission();
        return this.mAudioVolumeGroupHelper.getAudioVolumeGroups();
    }

    private void checkAllAliasStreamVolumes() {
        synchronized (this.mSettingsLock) {
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                int numStreamTypes = android.media.AudioSystem.getNumStreamTypes();
                for (int streamType = 0; streamType < numStreamTypes; streamType++) {
                    this.mStreamStates[streamType].setAllIndexes(this.mStreamStates[mStreamVolumeAlias[streamType]], TAG);
                    if (!this.mStreamStates[streamType].mIsMuted) {
                        this.mStreamStates[streamType].applyAllVolumes();
                    }
                }
            }
        }
    }

    void postCheckVolumeCecOnHdmiConnection(int state, java.lang.String caller) {
        sendMsg(this.mAudioHandler, 28, 0, state, 0, caller, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCheckVolumeCecOnHdmiConnection(int state, java.lang.String caller) {
        if (state == 1) {
            if (this.mSoundDoseHelper.safeDevicesContains(1024)) {
                this.mSoundDoseHelper.scheduleMusicActiveCheck();
            }
            if (isPlatformTelevision()) {
                synchronized (this.mHdmiClientLock) {
                    if (this.mHdmiManager != null && this.mHdmiPlaybackClient != null) {
                        updateHdmiCecSinkLocked(this.mFullVolumeDevices.contains(1024));
                    }
                }
            }
            sendEnabledSurroundFormats(this.mContentResolver, true);
            return;
        }
        if (isPlatformTelevision()) {
            synchronized (this.mHdmiClientLock) {
                if (this.mHdmiManager != null) {
                    updateHdmiCecSinkLocked(this.mFullVolumeDevices.contains(1024));
                }
            }
        }
    }

    private void postUpdateVolumeStatesForAudioDevice(int device, java.lang.String caller) {
        sendMsg(this.mAudioHandler, 33, 2, device, 0, caller, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUpdateVolumeStatesForAudioDevice(int device, java.lang.String caller) {
        int numStreamTypes = android.media.AudioSystem.getNumStreamTypes();
        synchronized (this.mSettingsLock) {
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                for (int streamType = 0; streamType < numStreamTypes; streamType++) {
                    updateVolumeStates(device, streamType, caller);
                }
            }
        }
    }

    private void updateVolumeStates(int device, int streamType, java.lang.String caller) throws java.lang.Throwable {
        if (device == 4194304) {
            device = 2;
        }
        if (!this.mStreamStates[streamType].hasIndexForDevice(device)) {
            this.mStreamStates[streamType].setIndex(this.mStreamStates[mStreamVolumeAlias[streamType]].getIndex(1073741824), device, caller, true);
        }
        java.util.List<android.media.AudioDeviceAttributes> devicesForAttributes = getDevicesForAttributesInt(new android.media.AudioAttributes.Builder().setInternalLegacyStreamType(streamType).build(), true);
        for (android.media.AudioDeviceAttributes deviceAttributes : devicesForAttributes) {
            if (deviceAttributes.getType() == android.media.AudioDeviceInfo.convertInternalDeviceToDeviceType(device)) {
                this.mStreamStates[streamType].checkFixedVolumeDevices();
                if (isStreamMute(streamType) && this.mFullVolumeDevices.contains(java.lang.Integer.valueOf(device))) {
                    this.mStreamStates[streamType].mute(false, "updateVolumeStates(" + caller);
                }
            }
        }
    }

    private void checkAllFixedVolumeDevices() {
        int numStreamTypes = android.media.AudioSystem.getNumStreamTypes();
        for (int streamType = 0; streamType < numStreamTypes; streamType++) {
            this.mStreamStates[streamType].checkFixedVolumeDevices();
        }
    }

    private void checkAllFixedVolumeDevices(int streamType) {
        this.mStreamStates[streamType].checkFixedVolumeDevices();
    }

    private void checkMuteAffectedStreams() {
        for (int i = 0; i < this.mStreamStates.length; i++) {
            com.android.server.audio.AudioService.VolumeStreamState vss = this.mStreamStates[i];
            if (vss.mIndexMin > 0 && vss.mStreamType != 0 && vss.mStreamType != 6) {
                this.mMuteAffectedStreams &= ~(1 << vss.mStreamType);
            }
        }
        updateUserMutableStreams();
    }

    private void createStreamStates() {
        int numStreamTypes = android.media.AudioSystem.getNumStreamTypes();
        com.android.server.audio.AudioService.VolumeStreamState[] streams = new com.android.server.audio.AudioService.VolumeStreamState[numStreamTypes];
        this.mStreamStates = streams;
        for (int i = 0; i < numStreamTypes; i++) {
            streams[i] = new com.android.server.audio.AudioService.VolumeStreamState(android.provider.Settings.System.VOLUME_SETTINGS_INT[mStreamVolumeAlias[i]], i);
        }
        checkAllFixedVolumeDevices();
        checkAllAliasStreamVolumes();
        checkMuteAffectedStreams();
        updateDefaultVolumes();
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x002c A[PHI: r1
  0x002c: PHI (r1v4 'streamVolumeAlias' int) = (r1v3 'streamVolumeAlias' int), (r1v5 'streamVolumeAlias' int), (r1v5 'streamVolumeAlias' int) binds: [B:6:0x000c, B:11:0x001f, B:13:0x0025] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void updateDefaultVolumes() {
        /*
            r4 = this;
            r0 = 0
        L1:
            com.android.server.audio.AudioService$VolumeStreamState[] r1 = r4.mStreamStates
            int r1 = r1.length
            if (r0 >= r1) goto L39
            int[] r1 = com.android.server.audio.AudioService.mStreamVolumeAlias
            r1 = r1[r0]
            boolean r2 = r4.mUseVolumeGroupAliases
            if (r2 == 0) goto L2c
            int[] r2 = android.media.AudioSystem.DEFAULT_STREAM_VOLUME
            r2 = r2[r0]
            r3 = -1
            if (r2 == r3) goto L16
            goto L36
        L16:
            r1 = 3
            int r2 = r4.getUiDefaultRescaledIndex(r1, r0)
            int[] r3 = com.android.server.audio.AudioService.MIN_STREAM_VOLUME
            r3 = r3[r0]
            if (r2 < r3) goto L2c
            int[] r3 = com.android.server.audio.AudioService.MAX_STREAM_VOLUME
            r3 = r3[r0]
            if (r2 > r3) goto L2c
            int[] r3 = android.media.AudioSystem.DEFAULT_STREAM_VOLUME
            r3[r0] = r2
            goto L36
        L2c:
            if (r0 == r1) goto L36
            int[] r2 = android.media.AudioSystem.DEFAULT_STREAM_VOLUME
            int r3 = r4.getUiDefaultRescaledIndex(r1, r0)
            r2[r0] = r3
        L36:
            int r0 = r0 + 1
            goto L1
        L39:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.AudioService.updateDefaultVolumes():void");
    }

    private int getUiDefaultRescaledIndex(int srcStream, int dstStream) {
        return (rescaleIndex(android.media.AudioSystem.DEFAULT_STREAM_VOLUME[srcStream] * 10, srcStream, dstStream) + 5) / 10;
    }

    private void dumpStreamStates(java.io.PrintWriter pw) {
        pw.println("\nStream volumes (device: index)");
        int numStreamTypes = android.media.AudioSystem.getNumStreamTypes();
        for (int i = 0; i < numStreamTypes; i++) {
            java.lang.StringBuilder alias = new java.lang.StringBuilder();
            if (mStreamVolumeAlias[i] != i) {
                alias.append(" (aliased to: ").append(android.media.AudioSystem.STREAM_NAMES[mStreamVolumeAlias[i]]).append(")");
            }
            pw.println("- " + android.media.AudioSystem.STREAM_NAMES[i] + ((java.lang.Object) alias) + ":");
            this.mStreamStates[i].dump(pw);
            pw.println("");
        }
        pw.print("\n- mute affected streams = 0x");
        pw.println(java.lang.Integer.toHexString(this.mMuteAffectedStreams));
        pw.print("\n- user mutable streams = 0x");
        pw.println(java.lang.Integer.toHexString(this.mUserMutableStreams));
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x009a  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x009f  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00af  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void updateStreamVolumeAlias(boolean r25, java.lang.String r26) {
        /*
            Method dump skipped, instruction units count: 286
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.AudioService.updateStreamVolumeAlias(boolean, java.lang.String):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readDockAudioSettings(android.content.ContentResolver cr) {
        this.mDockAudioMediaEnabled = this.mSettings.getGlobalInt(cr, "dock_audio_media_enabled", 0) == 1;
        sendMsg(this.mAudioHandler, 8, 2, 3, this.mDockAudioMediaEnabled ? 9 : 0, new java.lang.String("readDockAudioSettings"), 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMasterMono(android.content.ContentResolver cr) {
        boolean masterMono = this.mSettings.getSystemIntForUser(cr, "master_mono", 0, -2) == 1;
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, java.lang.String.format("Master mono %b", java.lang.Boolean.valueOf(masterMono)));
        }
        android.media.AudioSystem.setMasterMono(masterMono);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMasterBalance(android.content.ContentResolver cr) {
        float masterBalance = android.provider.Settings.System.getFloatForUser(cr, "master_balance", 0.0f, -2);
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, java.lang.String.format("Master balance %f", java.lang.Float.valueOf(masterBalance)));
        }
        if (android.media.AudioSystem.setMasterBalance(masterBalance) != 0) {
            android.util.Log.e(TAG, java.lang.String.format("setMasterBalance failed for %f", java.lang.Float.valueOf(masterBalance)));
        }
    }

    private void sendEncodedSurroundMode(android.content.ContentResolver cr, java.lang.String eventSource) {
        int encodedSurroundMode = this.mSettings.getGlobalInt(cr, "encoded_surround_output", 0);
        sendEncodedSurroundMode(encodedSurroundMode, eventSource);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendEncodedSurroundMode(int encodedSurroundMode, java.lang.String eventSource) {
        int forceSetting = 16;
        switch (encodedSurroundMode) {
            case 0:
                forceSetting = 0;
                break;
            case 1:
                forceSetting = 13;
                break;
            case 2:
                forceSetting = 14;
                break;
            case 3:
                forceSetting = 15;
                break;
            default:
                android.util.Log.e(TAG, "updateSurroundSoundSettings: illegal value " + encodedSurroundMode);
                break;
        }
        if (forceSetting != 16) {
            this.mDeviceBroker.setForceUse_Async(6, forceSetting, eventSource);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_AUDIO_POLICY") != 0) {
            throw new java.lang.SecurityException("Missing MANAGE_AUDIO_POLICY permission");
        }
        new com.android.server.audio.AudioManagerShellCommand(this).exec(this, in, out, err, args, callback, resultReceiver);
    }

    public java.util.Map<java.lang.Integer, java.lang.Boolean> getSurroundFormats() {
        java.util.Map<java.lang.Integer, java.lang.Boolean> surroundFormats = new java.util.HashMap<>();
        int status = android.media.AudioSystem.getSurroundFormats(surroundFormats);
        if (status != 0) {
            android.util.Log.e(TAG, "getSurroundFormats failed:" + status);
            return new java.util.HashMap();
        }
        return surroundFormats;
    }

    public java.util.List<java.lang.Integer> getReportedSurroundFormats() {
        java.util.ArrayList<java.lang.Integer> reportedSurroundFormats = new java.util.ArrayList<>();
        int status = android.media.AudioSystem.getReportedSurroundFormats(reportedSurroundFormats);
        if (status != 0) {
            android.util.Log.e(TAG, "getReportedSurroundFormats failed:" + status);
            return new java.util.ArrayList();
        }
        return reportedSurroundFormats;
    }

    public boolean isSurroundFormatEnabled(int audioFormat) {
        boolean zContains;
        if (!isSurroundFormat(audioFormat)) {
            android.util.Log.w(TAG, "audioFormat to enable is not a surround format.");
            return false;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mSettingsLock) {
                java.util.HashSet<java.lang.Integer> enabledFormats = getEnabledFormats();
                zContains = enabledFormats.contains(java.lang.Integer.valueOf(audioFormat));
            }
            return zContains;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public boolean setSurroundFormatEnabled(int audioFormat, boolean enabled) {
        if (!isSurroundFormat(audioFormat)) {
            android.util.Log.w(TAG, "audioFormat to enable is not a surround format.");
            return false;
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_SETTINGS") != 0) {
            throw new java.lang.SecurityException("Missing WRITE_SETTINGS permission");
        }
        java.util.HashSet<java.lang.Integer> enabledFormats = getEnabledFormats();
        if (enabled) {
            enabledFormats.add(java.lang.Integer.valueOf(audioFormat));
        } else {
            enabledFormats.remove(java.lang.Integer.valueOf(audioFormat));
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mSettingsLock) {
                this.mSettings.putGlobalString(this.mContentResolver, "encoded_surround_output_enabled_formats", android.text.TextUtils.join(",", enabledFormats));
            }
            android.os.Binder.restoreCallingIdentity(token);
            return true;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean setEncodedSurroundMode(int mode) {
        setEncodedSurroundMode_enforcePermission();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mSettingsLock) {
                this.mSettings.putGlobalInt(this.mContentResolver, "encoded_surround_output", toEncodedSurroundSetting(mode));
            }
            android.os.Binder.restoreCallingIdentity(token);
            return true;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public int getEncodedSurroundMode(int targetSdkVersion) {
        int encodedSurroundOutputMode;
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mSettingsLock) {
                int encodedSurroundSetting = this.mSettings.getGlobalInt(this.mContentResolver, "encoded_surround_output", 0);
                encodedSurroundOutputMode = toEncodedSurroundOutputMode(encodedSurroundSetting, targetSdkVersion);
            }
            return encodedSurroundOutputMode;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private java.util.HashSet<java.lang.Integer> getEnabledFormats() {
        final java.util.HashSet<java.lang.Integer> formats = new java.util.HashSet<>();
        java.lang.String enabledFormats = this.mSettings.getGlobalString(this.mContentResolver, "encoded_surround_output_enabled_formats");
        if (enabledFormats != null) {
            try {
                java.util.stream.IntStream intStreamMapToInt = java.util.Arrays.stream(android.text.TextUtils.split(enabledFormats, ",")).mapToInt(new com.android.server.audio.AudioService$$ExternalSyntheticLambda23());
                java.util.Objects.requireNonNull(formats);
                intStreamMapToInt.forEach(new java.util.function.IntConsumer() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda24
                    @Override // java.util.function.IntConsumer
                    public final void accept(int i) {
                        formats.add(java.lang.Integer.valueOf(i));
                    }
                });
            } catch (java.lang.NumberFormatException e) {
                android.util.Log.w(TAG, "ENCODED_SURROUND_OUTPUT_ENABLED_FORMATS misformatted.", e);
            }
        }
        return formats;
    }

    private int toEncodedSurroundOutputMode(int encodedSurroundSetting, int targetSdkVersion) {
        if (targetSdkVersion <= 31 && encodedSurroundSetting > 3) {
            return -1;
        }
        switch (encodedSurroundSetting) {
        }
        return -1;
    }

    private int toEncodedSurroundSetting(int encodedSurroundOutputMode) {
        switch (encodedSurroundOutputMode) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return 0;
        }
    }

    private boolean isSurroundFormat(int audioFormat) {
        for (int sf : android.media.AudioFormat.SURROUND_SOUND_ENCODING) {
            if (sf == audioFormat) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendEnabledSurroundFormats(android.content.ContentResolver cr, boolean forceUpdate) {
        if (this.mEncodedSurroundMode != 3) {
            return;
        }
        java.lang.String enabledSurroundFormats = this.mSettings.getGlobalString(cr, "encoded_surround_output_enabled_formats");
        if (enabledSurroundFormats == null) {
            enabledSurroundFormats = "";
        }
        if (!forceUpdate && android.text.TextUtils.equals(enabledSurroundFormats, this.mEnabledSurroundFormats)) {
            return;
        }
        this.mEnabledSurroundFormats = enabledSurroundFormats;
        java.lang.String[] surroundFormats = android.text.TextUtils.split(enabledSurroundFormats, ",");
        java.util.ArrayList<java.lang.Integer> formats = new java.util.ArrayList<>();
        for (java.lang.String format : surroundFormats) {
            try {
                int audioFormat = java.lang.Integer.valueOf(format).intValue();
                if (isSurroundFormat(audioFormat) && !formats.contains(java.lang.Integer.valueOf(audioFormat))) {
                    formats.add(java.lang.Integer.valueOf(audioFormat));
                }
            } catch (java.lang.Exception e) {
                android.util.Log.e(TAG, "Invalid enabled surround format:" + format);
            }
        }
        this.mSettings.putGlobalString(this.mContext.getContentResolver(), "encoded_surround_output_enabled_formats", android.text.TextUtils.join(",", formats));
        sendMsg(this.mAudioHandler, 24, 2, 0, 0, formats, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onEnableSurroundFormats(java.util.ArrayList<java.lang.Integer> enabledSurroundFormats) {
        for (int surroundFormat : android.media.AudioFormat.SURROUND_SOUND_ENCODING) {
            boolean enabled = enabledSurroundFormats.contains(java.lang.Integer.valueOf(surroundFormat));
            int ret = android.media.AudioSystem.setSurroundFormatEnabled(surroundFormat, enabled);
            android.util.Log.i(TAG, "enable surround format:" + surroundFormat + " " + enabled + " " + ret);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAssistantUIdLocked(boolean forceUpdate) {
        int assistantUid = -1;
        java.lang.String packageName = "";
        if (this.mRoleObserver != null) {
            packageName = this.mRoleObserver.getAssistantRoleHolder();
        }
        if (android.text.TextUtils.isEmpty(packageName)) {
            java.lang.String assistantName = this.mSettings.getSecureStringForUser(this.mContentResolver, "voice_interaction_service", -2);
            if (android.text.TextUtils.isEmpty(assistantName)) {
                assistantName = this.mSettings.getSecureStringForUser(this.mContentResolver, "assistant", -2);
            }
            if (!android.text.TextUtils.isEmpty(assistantName)) {
                android.content.ComponentName componentName = android.content.ComponentName.unflattenFromString(assistantName);
                if (componentName == null) {
                    android.util.Slog.w(TAG, "Invalid service name for voice_interaction_service: " + assistantName);
                    return;
                }
                packageName = componentName.getPackageName();
            }
        }
        if (!android.text.TextUtils.isEmpty(packageName)) {
            android.content.pm.PackageManager pm = this.mContext.getPackageManager();
            if (pm.checkPermission("android.permission.CAPTURE_AUDIO_HOTWORD", packageName) == 0) {
                try {
                    assistantUid = pm.getPackageUidAsUser(packageName, getCurrentUserId());
                    android.util.Log.i(TAG, "updateAssistantUId assistantUid=" + assistantUid + " packageName=" + packageName + " getCurrentUserId=" + getCurrentUserId());
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    android.util.Log.e(TAG, "updateAssistantUId() could not find UID for package: " + packageName);
                }
            }
        }
        if (this.mPrimaryAssistantUid != assistantUid || forceUpdate) {
            this.mAssistantUids.remove(java.lang.Integer.valueOf(this.mPrimaryAssistantUid));
            this.mPrimaryAssistantUid = assistantUid;
            addAssistantServiceUidsLocked(new int[]{this.mPrimaryAssistantUid});
        }
    }

    private void readPersistedSettings() {
        int i;
        if (!this.mSystemServer.isPrivileged()) {
            return;
        }
        android.content.ContentResolver cr = this.mContentResolver;
        int i2 = 2;
        int ringerModeFromSettings = this.mAsExt.readRingModeSetting(this.mSettings.getGlobalInt(cr, "mode_ringer", 2), cr);
        int ringerMode = ringerModeFromSettings;
        if (!isValidRingerMode(ringerMode)) {
            ringerMode = 2;
        }
        if (ringerMode == 1 && !this.mHasVibrator) {
            ringerMode = 0;
        }
        if (ringerMode != ringerModeFromSettings) {
            this.mSettings.putGlobalInt(cr, "mode_ringer", ringerMode);
            android.provider.Settings.System.putIntForUser(cr, com.android.server.audio.IAudioServiceExt.OPLUS_MODE_RINGER, ringerMode, -2);
        }
        if (this.mUseFixedVolume || this.mIsSingleVolume) {
            ringerMode = 2;
        }
        synchronized (this.mSettingsLock) {
            this.mRingerMode = ringerMode;
            if (this.mRingerModeExternal == -1 || (this.mRingerModeExternal != this.mRingerMode && getCurrentUserId() != 0)) {
                this.mRingerModeExternal = this.mRingerMode;
            }
            if (this.mHasVibrator) {
                i = 2;
            } else {
                i = 0;
            }
            this.mVibrateSetting = android.media.AudioSystem.getValueForVibrateSetting(0, 1, i);
            int i3 = this.mVibrateSetting;
            if (!this.mHasVibrator) {
                i2 = 0;
            }
            this.mVibrateSetting = android.media.AudioSystem.getValueForVibrateSetting(i3, 0, i2);
            updateRingerAndZenModeAffectedStreams();
            readDockAudioSettings(cr);
            sendEncodedSurroundMode(cr, "readPersistedSettings");
            sendEnabledSurroundFormats(cr, true);
            updateAssistantUIdLocked(true);
            resetActiveAssistantUidsLocked();
            this.mAsExt.updateInputDevice(this.mContentResolver);
            android.media.AudioSystem.setRttEnabled(this.mRttEnabled);
        }
        this.mMuteAffectedStreams = this.mSettings.getSystemIntForUser(cr, "mute_streams_affected", 111, -2);
        updateUserMutableStreams();
        updateMasterMono(cr);
        updateMasterBalance(cr);
        broadcastRingerMode("android.media.RINGER_MODE_CHANGED", this.mRingerModeExternal);
        broadcastRingerMode("android.media.INTERNAL_RINGER_MODE_CHANGED_ACTION", this.mRingerMode);
        broadcastVibrateSetting(0);
        broadcastVibrateSetting(1);
        this.mVolumeController.loadSettings(cr);
    }

    private void updateUserMutableStreams() {
        this.mUserMutableStreams = this.mMuteAffectedStreams;
        this.mUserMutableStreams &= -2;
        this.mUserMutableStreams &= -65;
    }

    private void resetActiveAssistantUidsLocked() {
        this.mActiveAssistantServiceUids = NO_ACTIVE_ASSISTANT_SERVICE_UIDS;
        updateActiveAssistantServiceUids();
    }

    private void readUserRestrictions() {
        if (!this.mSystemServer.isPrivileged()) {
            return;
        }
        int currentUser = getCurrentUserId();
        if (this.mUseFixedVolume) {
            android.media.AudioSystem.setMasterVolume(1.0f);
        }
        boolean masterMute = this.mUserManagerInternal.getUserRestriction(currentUser, "disallow_unmute_device") || this.mUserManagerInternal.getUserRestriction(currentUser, "no_adjust_volume");
        setMasterMuteInternalNoCallerCheck(masterMute, 0, currentUser, "readUserRestrictions");
        this.mMicMuteFromRestrictions = this.mUserManagerInternal.getUserRestriction(currentUser, "no_unmute_microphone");
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, java.lang.String.format("Mic mute %b, user=%d", java.lang.Boolean.valueOf(this.mMicMuteFromRestrictions), java.lang.Integer.valueOf(currentUser)));
        }
        setMicrophoneMuteNoCallerCheck(currentUser);
    }

    private int getIndexRange(int streamType) {
        return this.mStreamStates[streamType].getMaxIndex() - this.mStreamStates[streamType].getMinIndex();
    }

    private int rescaleIndex(android.media.VolumeInfo volumeInfo, int dstStream) {
        if (volumeInfo.getVolumeIndex() == -100 || volumeInfo.getMinVolumeIndex() == -100 || volumeInfo.getMaxVolumeIndex() == -100) {
            android.util.Log.e(TAG, "rescaleIndex: volumeInfo has invalid index or range");
            return this.mStreamStates[dstStream].getMinIndex();
        }
        return rescaleIndex(volumeInfo.getVolumeIndex(), volumeInfo.getMinVolumeIndex(), volumeInfo.getMaxVolumeIndex(), this.mStreamStates[dstStream].getMinIndex(), this.mStreamStates[dstStream].getMaxIndex());
    }

    private int rescaleIndex(int index, int srcStream, android.media.VolumeInfo dstVolumeInfo) {
        int dstMin = dstVolumeInfo.getMinVolumeIndex();
        int dstMax = dstVolumeInfo.getMaxVolumeIndex();
        if (dstMin == -100 || dstMax == -100) {
            return index;
        }
        return rescaleIndex(index, this.mStreamStates[srcStream].getMinIndex(), this.mStreamStates[srcStream].getMaxIndex(), dstMin, dstMax);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int rescaleIndex(int index, int srcStream, int dstStream) {
        return rescaleIndex(index, this.mStreamStates[srcStream].getMinIndex(), this.mStreamStates[srcStream].getMaxIndex(), this.mStreamStates[dstStream].getMinIndex(), this.mStreamStates[dstStream].getMaxIndex());
    }

    private int rescaleIndex(int index, int srcMin, int srcMax, int dstMin, int dstMax) {
        int srcRange = srcMax - srcMin;
        int dstRange = dstMax - dstMin;
        if (srcRange == 0) {
            android.util.Log.e(TAG, "rescaleIndex : index range should not be zero");
            return dstMin;
        }
        return ((((index - srcMin) * dstRange) + (srcRange / 2)) / srcRange) + dstMin;
    }

    private int rescaleStep(int step, int srcStream, int dstStream) {
        int srcRange = getIndexRange(srcStream);
        int dstRange = getIndexRange(dstStream);
        if (srcRange == 0) {
            android.util.Log.e(TAG, "rescaleStep : index range should not be zero");
            return 0;
        }
        return ((step * dstRange) + (srcRange / 2)) / srcRange;
    }

    public int setPreferredDevicesForStrategy(int strategy, java.util.List<android.media.AudioDeviceAttributes> devices) {
        super.setPreferredDevicesForStrategy_enforcePermission();
        if (devices == null) {
            return -1;
        }
        java.util.List<android.media.AudioDeviceAttributes> devices2 = retrieveBluetoothAddresses(devices);
        java.lang.String logString = java.lang.String.format("setPreferredDevicesForStrategy u/pid:%d/%d strat:%d dev:%s", java.lang.Integer.valueOf(android.os.Binder.getCallingUid()), java.lang.Integer.valueOf(android.os.Binder.getCallingPid()), java.lang.Integer.valueOf(strategy), devices2.stream().map(new java.util.function.Function() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda15
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((android.media.AudioDeviceAttributes) obj).toString();
            }
        }).collect(java.util.stream.Collectors.joining(",")));
        sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(logString).printLog(TAG));
        if (devices2.stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda16
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.audio.AudioService.lambda$setPreferredDevicesForStrategy$9((android.media.AudioDeviceAttributes) obj);
            }
        })) {
            android.util.Log.e(TAG, "Unsupported input routing in " + logString);
            return -1;
        }
        int status = this.mDeviceBroker.setPreferredDevicesForStrategySync(strategy, devices2);
        if (status != 0) {
            android.util.Log.e(TAG, java.lang.String.format("Error %d in %s)", java.lang.Integer.valueOf(status), logString));
        }
        return status;
    }

    static /* synthetic */ boolean lambda$setPreferredDevicesForStrategy$9(android.media.AudioDeviceAttributes device) {
        return device.getRole() == 1;
    }

    public int removePreferredDevicesForStrategy(int strategy) {
        super.removePreferredDevicesForStrategy_enforcePermission();
        java.lang.String logString = java.lang.String.format("removePreferredDevicesForStrategy strat:%d", java.lang.Integer.valueOf(strategy));
        sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(logString).printLog(TAG));
        int status = this.mDeviceBroker.removePreferredDevicesForStrategySync(strategy);
        if (status != 0 && status != -2) {
            android.util.Log.e(TAG, java.lang.String.format("Error %d in %s)", java.lang.Integer.valueOf(status), logString));
        }
        return status;
    }

    public java.util.List<android.media.AudioDeviceAttributes> getPreferredDevicesForStrategy(int strategy) {
        super.getPreferredDevicesForStrategy_enforcePermission();
        java.util.List<android.media.AudioDeviceAttributes> devices = new java.util.ArrayList<>();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            int status = android.media.AudioSystem.getDevicesForRoleAndStrategy(strategy, 1, devices);
            if (status != 0) {
                android.util.Log.e(TAG, java.lang.String.format("Error %d in getPreferredDeviceForStrategy(%d)", java.lang.Integer.valueOf(status), java.lang.Integer.valueOf(strategy)));
                return new java.util.ArrayList();
            }
            return anonymizeAudioDeviceAttributesList(devices);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public int setDeviceAsNonDefaultForStrategy(int strategy, android.media.AudioDeviceAttributes device) {
        super.setDeviceAsNonDefaultForStrategy_enforcePermission();
        java.util.Objects.requireNonNull(device);
        android.media.AudioDeviceAttributes device2 = retrieveBluetoothAddress(device);
        java.lang.String logString = java.lang.String.format("setDeviceAsNonDefaultForStrategy u/pid:%d/%d strat:%d dev:%s", java.lang.Integer.valueOf(android.os.Binder.getCallingUid()), java.lang.Integer.valueOf(android.os.Binder.getCallingPid()), java.lang.Integer.valueOf(strategy), device2.toString());
        sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(logString).printLog(TAG));
        if (device2.getRole() == 1) {
            android.util.Log.e(TAG, "Unsupported input routing in " + logString);
            return -1;
        }
        int status = this.mDeviceBroker.setDeviceAsNonDefaultForStrategySync(strategy, device2);
        if (status != 0) {
            android.util.Log.e(TAG, java.lang.String.format("Error %d in %s)", java.lang.Integer.valueOf(status), logString));
        }
        return status;
    }

    public int removeDeviceAsNonDefaultForStrategy(int strategy, android.media.AudioDeviceAttributes device) {
        super.removeDeviceAsNonDefaultForStrategy_enforcePermission();
        java.util.Objects.requireNonNull(device);
        android.media.AudioDeviceAttributes device2 = retrieveBluetoothAddress(device);
        java.lang.String logString = java.lang.String.format("removeDeviceAsNonDefaultForStrategy strat:%d dev:%s", java.lang.Integer.valueOf(strategy), device2.toString());
        sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(logString).printLog(TAG));
        if (device2.getRole() == 1) {
            android.util.Log.e(TAG, "Unsupported input routing in " + logString);
            return -1;
        }
        int status = this.mDeviceBroker.removeDeviceAsNonDefaultForStrategySync(strategy, device2);
        if (status != 0 && status != -2) {
            android.util.Log.e(TAG, java.lang.String.format("Error %d in %s)", java.lang.Integer.valueOf(status), logString));
        }
        return status;
    }

    public java.util.List<android.media.AudioDeviceAttributes> getNonDefaultDevicesForStrategy(int strategy) {
        super.getNonDefaultDevicesForStrategy_enforcePermission();
        java.util.List<android.media.AudioDeviceAttributes> devices = new java.util.ArrayList<>();
        android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
        try {
            int status = android.media.AudioSystem.getDevicesForRoleAndStrategy(strategy, 2, devices);
            if (ignored != null) {
                ignored.close();
            }
            if (status != 0) {
                android.util.Log.e(TAG, java.lang.String.format("Error %d in getNonDefaultDeviceForStrategy(%d)", java.lang.Integer.valueOf(status), java.lang.Integer.valueOf(strategy)));
                return new java.util.ArrayList();
            }
            return anonymizeAudioDeviceAttributesList(devices);
        } catch (java.lang.Throwable th) {
            if (ignored != null) {
                try {
                    ignored.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public void registerStrategyPreferredDevicesDispatcher(android.media.IStrategyPreferredDevicesDispatcher dispatcher) {
        if (dispatcher == null) {
            return;
        }
        enforceModifyAudioRoutingPermission();
        this.mDeviceBroker.registerStrategyPreferredDevicesDispatcher(dispatcher, isBluetoothPrividged());
    }

    public void unregisterStrategyPreferredDevicesDispatcher(android.media.IStrategyPreferredDevicesDispatcher dispatcher) {
        if (dispatcher == null) {
            return;
        }
        enforceModifyAudioRoutingPermission();
        this.mDeviceBroker.unregisterStrategyPreferredDevicesDispatcher(dispatcher);
    }

    public void registerStrategyNonDefaultDevicesDispatcher(android.media.IStrategyNonDefaultDevicesDispatcher dispatcher) {
        if (dispatcher == null) {
            return;
        }
        enforceModifyAudioRoutingPermission();
        this.mDeviceBroker.registerStrategyNonDefaultDevicesDispatcher(dispatcher, isBluetoothPrividged());
    }

    public void unregisterStrategyNonDefaultDevicesDispatcher(android.media.IStrategyNonDefaultDevicesDispatcher dispatcher) {
        if (dispatcher == null) {
            return;
        }
        enforceModifyAudioRoutingPermission();
        this.mDeviceBroker.unregisterStrategyNonDefaultDevicesDispatcher(dispatcher);
    }

    public int setPreferredDevicesForCapturePreset(int capturePreset, java.util.List<android.media.AudioDeviceAttributes> devices) {
        if (devices == null) {
            return -1;
        }
        enforceModifyAudioRoutingPermission();
        java.lang.String logString = java.lang.String.format("setPreferredDevicesForCapturePreset u/pid:%d/%d source:%d dev:%s", java.lang.Integer.valueOf(android.os.Binder.getCallingUid()), java.lang.Integer.valueOf(android.os.Binder.getCallingPid()), java.lang.Integer.valueOf(capturePreset), devices.stream().map(new java.util.function.Function() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda6
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((android.media.AudioDeviceAttributes) obj).toString();
            }
        }).collect(java.util.stream.Collectors.joining(",")));
        sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(logString).printLog(TAG));
        if (devices.stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.audio.AudioService.lambda$setPreferredDevicesForCapturePreset$11((android.media.AudioDeviceAttributes) obj);
            }
        })) {
            android.util.Log.e(TAG, "Unsupported output routing in " + logString);
            return -1;
        }
        int status = this.mDeviceBroker.setPreferredDevicesForCapturePresetSync(capturePreset, retrieveBluetoothAddresses(devices));
        if (status != 0) {
            android.util.Log.e(TAG, java.lang.String.format("Error %d in %s)", java.lang.Integer.valueOf(status), logString));
        }
        return status;
    }

    static /* synthetic */ boolean lambda$setPreferredDevicesForCapturePreset$11(android.media.AudioDeviceAttributes device) {
        return device.getRole() == 2;
    }

    public int clearPreferredDevicesForCapturePreset(int capturePreset) {
        super.clearPreferredDevicesForCapturePreset_enforcePermission();
        java.lang.String logString = java.lang.String.format("removePreferredDeviceForCapturePreset source:%d", java.lang.Integer.valueOf(capturePreset));
        sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(logString).printLog(TAG));
        int status = this.mDeviceBroker.clearPreferredDevicesForCapturePresetSync(capturePreset);
        if (status != 0 && status != -2) {
            android.util.Log.e(TAG, java.lang.String.format("Error %d in %s", java.lang.Integer.valueOf(status), logString));
        }
        return status;
    }

    public java.util.List<android.media.AudioDeviceAttributes> getPreferredDevicesForCapturePreset(int capturePreset) {
        super.getPreferredDevicesForCapturePreset_enforcePermission();
        java.util.List<android.media.AudioDeviceAttributes> devices = new java.util.ArrayList<>();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            int status = android.media.AudioSystem.getDevicesForRoleAndCapturePreset(capturePreset, 1, devices);
            if (status != 0) {
                android.util.Log.e(TAG, java.lang.String.format("Error %d in getPreferredDeviceForCapturePreset(%d)", java.lang.Integer.valueOf(status), java.lang.Integer.valueOf(capturePreset)));
                return new java.util.ArrayList();
            }
            return anonymizeAudioDeviceAttributesList(devices);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void registerCapturePresetDevicesRoleDispatcher(android.media.ICapturePresetDevicesRoleDispatcher dispatcher) {
        if (dispatcher == null) {
            return;
        }
        enforceModifyAudioRoutingPermission();
        this.mDeviceBroker.registerCapturePresetDevicesRoleDispatcher(dispatcher, isBluetoothPrividged());
    }

    public void unregisterCapturePresetDevicesRoleDispatcher(android.media.ICapturePresetDevicesRoleDispatcher dispatcher) {
        if (dispatcher == null) {
            return;
        }
        enforceModifyAudioRoutingPermission();
        this.mDeviceBroker.unregisterCapturePresetDevicesRoleDispatcher(dispatcher);
    }

    /* JADX INFO: renamed from: getDevicesForAttributes, reason: merged with bridge method [inline-methods] */
    public java.util.ArrayList<android.media.AudioDeviceAttributes> m1979getDevicesForAttributes(android.media.AudioAttributes attributes) {
        enforceQueryStateOrModifyRoutingPermission();
        return new java.util.ArrayList<>(anonymizeAudioDeviceAttributesList(getDevicesForAttributesInt(attributes, false)));
    }

    /* JADX INFO: renamed from: getDevicesForAttributesUnprotected, reason: merged with bridge method [inline-methods] */
    public java.util.ArrayList<android.media.AudioDeviceAttributes> m1980getDevicesForAttributesUnprotected(android.media.AudioAttributes attributes) {
        return new java.util.ArrayList<>(anonymizeAudioDeviceAttributesList(getDevicesForAttributesInt(attributes, false)));
    }

    public boolean isMusicActive(boolean remotely) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            if (remotely) {
                return android.media.AudioSystem.isStreamActiveRemotely(3, 0);
            }
            return android.media.AudioSystem.isStreamActive(3, 0);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    protected java.util.ArrayList<android.media.AudioDeviceAttributes> getDevicesForAttributesInt(android.media.AudioAttributes attributes, boolean forVolume) {
        java.util.Objects.requireNonNull(attributes);
        return this.mAudioSystem.getDevicesForAttributes(attributes, forVolume);
    }

    public void addOnDevicesForAttributesChangedListener(android.media.AudioAttributes attributes, android.media.IDevicesForAttributesCallback callback) {
        this.mAudioSystem.addOnDevicesForAttributesChangedListener(attributes, false, callback);
    }

    public void removeOnDevicesForAttributesChangedListener(android.media.IDevicesForAttributesCallback callback) {
        this.mAudioSystem.removeOnDevicesForAttributesChangedListener(callback);
    }

    public void handleVolumeKey(android.view.KeyEvent event, boolean isOnTv, java.lang.String callingPackage, java.lang.String caller) throws java.lang.Throwable {
        int keyEventMode = 0;
        if (this.mAsExt.handleVolumeKey(event, caller)) {
        }
        if (isOnTv) {
            if (event.getAction() == 0) {
                keyEventMode = 1;
            } else {
                keyEventMode = 2;
            }
        } else if (event.getAction() != 0) {
            return;
        }
        switch (event.getKeyCode()) {
            case 24:
                adjustSuggestedStreamVolume(1, Integer.MIN_VALUE, 4101, callingPackage, caller, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), true, keyEventMode);
                break;
            case 25:
                adjustSuggestedStreamVolume(-1, Integer.MIN_VALUE, 4101, callingPackage, caller, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), true, keyEventMode);
                break;
            case 164:
                if (event.getAction() == 0 && event.getRepeatCount() == 0) {
                    adjustSuggestedStreamVolume(101, Integer.MIN_VALUE, 4101, callingPackage, caller, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), true, 0);
                    break;
                }
                break;
            default:
                android.util.Log.e(TAG, "Invalid key code " + event.getKeyCode() + " sent by " + callingPackage);
                break;
        }
    }

    public void setNavigationRepeatSoundEffectsEnabled(boolean enabled) {
        this.mNavigationRepeatSoundEffectsEnabled = enabled;
    }

    public boolean areNavigationRepeatSoundEffectsEnabled() {
        return this.mNavigationRepeatSoundEffectsEnabled;
    }

    public void setHomeSoundEffectEnabled(boolean enabled) {
        this.mHomeSoundEffectEnabled = enabled;
    }

    public boolean isHomeSoundEffectEnabled() {
        return this.mHomeSoundEffectEnabled;
    }

    private void adjustSuggestedStreamVolume(int direction, int suggestedStreamType, int flags, java.lang.String callingPackage, java.lang.String caller, int uid, int pid, boolean hasModifyAudioSettings, int keyEventMode) throws java.lang.Throwable {
        int maybeActiveStreamType;
        boolean activeForReal;
        int direction2;
        int flags2;
        int flags3 = flags;
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "adjustSuggestedStreamVolume() stream=" + suggestedStreamType + ", flags=" + flags3 + ", caller=" + caller + ", volControlStream=" + this.mVolumeControlStream + ", userSelect=" + this.mUserSelectedVolumeControlStream);
        }
        if (direction != 0) {
            sVolumeLogger.enqueue(new com.android.server.audio.AudioServiceEvents.VolumeEvent(0, suggestedStreamType, direction, flags, callingPackage + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + caller + " uid:" + uid));
        }
        boolean hasExternalVolumeController = notifyExternalVolumeController(direction);
        new android.media.MediaMetrics.Item("audio.service.adjustSuggestedStreamVolume").setUid(android.os.Binder.getCallingUid()).set(android.media.MediaMetrics.Property.CALLING_PACKAGE, callingPackage).set(android.media.MediaMetrics.Property.CLIENT_NAME, caller).set(android.media.MediaMetrics.Property.DIRECTION, direction > 0 ? android.net.INetd.IF_STATE_UP : android.net.INetd.IF_STATE_DOWN).set(android.media.MediaMetrics.Property.EXTERNAL, hasExternalVolumeController ? com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_YES : com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_NO).set(android.media.MediaMetrics.Property.FLAGS, java.lang.Integer.valueOf(flags)).record();
        if (hasExternalVolumeController) {
            return;
        }
        synchronized (this.mForceControlStreamLock) {
            if (DEBUG_VOL) {
                android.util.Log.d(TAG, "adjustSuggestedStreamVolume() stream=" + suggestedStreamType + ", flags=" + flags3 + ", caller=" + caller + ", volControlStream=" + this.mVolumeControlStream + ", userSelect=" + this.mUserSelectedVolumeControlStream);
            }
            if (!this.mUserSelectedVolumeControlStream) {
                maybeActiveStreamType = getActiveStreamType(suggestedStreamType, true);
                if (maybeActiveStreamType != 2 && maybeActiveStreamType != 5) {
                    activeForReal = this.mAudioSystem.isStreamActive(maybeActiveStreamType, 0);
                } else {
                    activeForReal = wasStreamActiveRecently(maybeActiveStreamType, 0);
                }
                if (!activeForReal && this.mVolumeControlStream != -1) {
                    maybeActiveStreamType = this.mVolumeControlStream;
                }
            } else {
                maybeActiveStreamType = this.mVolumeControlStream;
            }
        }
        boolean isMute = isMuteAdjust(direction);
        ensureValidStreamType(maybeActiveStreamType);
        int resolvedStream = mStreamVolumeAlias[maybeActiveStreamType];
        if ((flags3 & 4) != 0 && resolvedStream != 2) {
            flags3 &= -5;
        }
        if (this.mVolumeController.suppressAdjustment(resolvedStream, flags3, isMute) && this.mAsExt.isRingVolumeDefault() && !this.mIsSingleVolume) {
            int flags4 = flags3 & (-5) & (-17);
            if (DEBUG_VOL) {
                android.util.Log.d(TAG, "Volume controller suppressed adjustment");
            }
            direction2 = 0;
            flags2 = flags4;
        } else {
            direction2 = direction;
            flags2 = flags3;
        }
        adjustStreamVolume(maybeActiveStreamType, direction2, flags2, callingPackage, caller, uid, pid, null, hasModifyAudioSettings, keyEventMode);
    }

    private boolean notifyExternalVolumeController(int direction) {
        android.media.audiopolicy.IAudioPolicyCallback externalVolumeController;
        synchronized (this.mExtVolumeControllerLock) {
            externalVolumeController = this.mExtVolumeController;
        }
        if (externalVolumeController == null) {
            return false;
        }
        sendMsg(this.mAudioHandler, 22, 2, direction, 0, externalVolumeController, 0);
        return true;
    }

    public void adjustStreamVolume(int streamType, int direction, int flags, java.lang.String callingPackage) throws java.lang.Throwable {
        adjustStreamVolumeWithAttribution(streamType, direction, flags, callingPackage, null);
    }

    public void adjustStreamVolumeWithAttribution(int streamType, int direction, int flags, java.lang.String callingPackage, java.lang.String attributionTag) throws java.lang.Throwable {
        if (this.mHardeningEnforcer.blockVolumeMethod(103)) {
            return;
        }
        if (streamType == 10 && !canChangeAccessibilityVolume()) {
            android.util.Log.w(TAG, "Trying to call adjustStreamVolume() for a11y withoutCHANGE_ACCESSIBILITY_VOLUME / callingPackage=" + callingPackage);
            return;
        }
        com.android.server.audio.AudioServiceEvents.VolumeEvent evt = new com.android.server.audio.AudioServiceEvents.VolumeEvent(1, streamType, direction, flags, callingPackage);
        sVolumeLogger.enqueue(evt);
        if (isMuteAdjust(direction)) {
            sMuteLogger.enqueue(evt);
        }
        adjustStreamVolume(streamType, direction, flags, callingPackage, callingPackage, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), attributionTag, callingHasAudioSettingsPermission(), 0);
        if (streamType == 3) {
            if (direction == -100 || direction == 100) {
                this.mAsExt.notifyAdjustVolumeUpdate(direction, callingPackage);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:209:0x0431  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x046e  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x04e6  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x04f4 A[Catch: all -> 0x0590, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x0590, blocks: (B:224:0x04ad, B:226:0x04b1, B:233:0x04c1, B:240:0x04d0, B:255:0x04f4), top: B:312:0x04ad }] */
    /* JADX WARN: Removed duplicated region for block: B:291:0x057d  */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void adjustStreamVolume(int r31, int r32, int r33, java.lang.String r34, java.lang.String r35, int r36, int r37, java.lang.String r38, boolean r39, int r40) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1498
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.AudioService.adjustStreamVolume(int, int, int, java.lang.String, java.lang.String, int, int, java.lang.String, boolean, int):void");
    }

    private void muteAliasStreams(int streamAlias, final boolean state) {
        synchronized (this.mSettingsLock) {
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                java.util.List<java.lang.Integer> streamsToMute = new java.util.ArrayList<>();
                for (int stream = 0; stream < this.mStreamStates.length; stream++) {
                    com.android.server.audio.AudioService.VolumeStreamState vss = this.mStreamStates[stream];
                    if (streamAlias == mStreamVolumeAlias[stream] && vss.isMutable() && (!this.mCameraSoundForced || vss.getStreamType() != 7)) {
                        boolean changed = vss.mute(state, false, "muteAliasStreams");
                        if (changed) {
                            streamsToMute.add(java.lang.Integer.valueOf(stream));
                        }
                    }
                }
                streamsToMute.forEach(new java.util.function.Consumer() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda22
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$muteAliasStreams$12(state, (java.lang.Integer) obj);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$muteAliasStreams$12(boolean state, java.lang.Integer streamToMute) {
        this.mStreamStates[streamToMute.intValue()].doMute();
        broadcastMuteSetting(streamToMute.intValue(), state);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastMuteSetting(int streamType, boolean isMuted) {
        android.content.Intent intent = new android.content.Intent("android.media.STREAM_MUTE_CHANGED_ACTION");
        intent.putExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", streamType);
        intent.putExtra("android.media.EXTRA_STREAM_VOLUME_MUTED", isMuted);
        sendBroadcastToAll(intent, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUnmuteStreamOnSingleVolDevice(int streamAlias, int flags) {
        boolean wasMuted;
        synchronized (this.mSettingsLock) {
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                com.android.server.audio.AudioService.VolumeStreamState streamState = this.mStreamStates[streamAlias];
                wasMuted = streamState.mute(false, "onUnmuteStreamOnSingleVolDevice");
                if (wasMuted) {
                    muteAliasStreams(streamAlias, false);
                }
                int device = getDeviceForStream(streamAlias);
                int index = streamState.getIndex(device);
                sendVolumeUpdate(streamAlias, index, index, flags, device);
                this.mAsExt.oplusMediaVolumeUpdateNotifyEffectExt(streamAlias, device, index);
            }
            if (streamAlias == 3 && wasMuted) {
                synchronized (this.mHdmiClientLock) {
                    maybeSendSystemAudioStatusCommand(true);
                }
            }
        }
    }

    private void maybeSendSystemAudioStatusCommand(boolean isMuteAdjust) {
        if (this.mHdmiAudioSystemClient == null || !this.mHdmiSystemAudioSupported || !this.mHdmiCecVolumeControlEnabled) {
            return;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mHdmiAudioSystemClient.sendReportAudioStatusCecCommand(isMuteAdjust, getStreamVolume(3), getStreamMaxVolume(3), isStreamMute(3));
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private int getNewRingerMode(int stream, int index, int flags) {
        int newRingerMode;
        if (this.mIsSingleVolume) {
            return getRingerModeExternal();
        }
        if ((flags & 2) != 0 || stream == getUiSoundsStreamType()) {
            if (index == 0) {
                if (this.mHasVibrator) {
                    newRingerMode = 1;
                } else {
                    newRingerMode = this.mVolumePolicy.volumeDownToEnterSilent ? 0 : 2;
                }
                return this.mAsExt.oplusGetNewRingMode(newRingerMode);
            }
            return 2;
        }
        return getRingerModeExternal();
    }

    private boolean isAndroidNPlus(java.lang.String caller) {
        try {
            android.content.pm.ApplicationInfo applicationInfo = this.mContext.getPackageManager().getApplicationInfoAsUser(caller, 0, android.os.UserHandle.getUserId(android.os.Binder.getCallingUid()));
            if (applicationInfo.targetSdkVersion >= 24) {
                return true;
            }
            return false;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return true;
        }
    }

    private boolean wouldToggleZenMode(int newMode) {
        if (getRingerModeExternal() != 0 || newMode == 0) {
            return getRingerModeExternal() != 0 && newMode == 0;
        }
        return true;
    }

    void onSetStreamVolume(int streamType, int index, int flags, int device, java.lang.String caller, boolean hasModifyAudioSettings, boolean canChangeMute) {
        int stream = mStreamVolumeAlias[streamType];
        if ((flags & 2) != 0 || stream == getUiSoundsStreamType()) {
            setRingerMode(getNewRingerMode(stream, index, flags), "AS.AudioService.onSetStreamVolume", false);
        }
        setStreamVolumeInt(stream, index, device, false, caller, hasModifyAudioSettings);
        if (streamType != 6 && canChangeMute) {
            if (stream != 3 || index != 0) {
                muteAliasStreams(stream, index == 0);
            }
        }
    }

    private void enforceModifyAudioRoutingPermission() {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_AUDIO_ROUTING") != 0) {
            throw new java.lang.SecurityException("Missing MODIFY_AUDIO_ROUTING permission");
        }
    }

    private void enforceQueryStateOrModifyRoutingPermission() {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_AUDIO_ROUTING") != 0 && this.mContext.checkCallingOrSelfPermission("android.permission.QUERY_AUDIO_STATE") != 0) {
            throw new java.lang.SecurityException("Missing MODIFY_AUDIO_ROUTING or QUERY_AUDIO_STATE permissions");
        }
    }

    public void setVolumeGroupVolumeIndex(int groupId, int index, int flags, java.lang.String callingPackage, java.lang.String attributionTag) {
        super.setVolumeGroupVolumeIndex_enforcePermission();
        if (sVolumeGroupStates.indexOfKey(groupId) < 0) {
            android.util.Log.e(TAG, ": no volume group found for id " + groupId);
            return;
        }
        com.android.server.audio.AudioService.VolumeGroupState vgs = sVolumeGroupStates.get(groupId);
        sVolumeLogger.enqueue(new com.android.server.audio.AudioServiceEvents.VolumeEvent(8, vgs.name(), index, flags, callingPackage + ", user " + getCurrentUserId()));
        vgs.setVolumeIndex(index, flags);
        for (int groupedStream : vgs.getLegacyStreamTypes()) {
            try {
                ensureValidStreamType(groupedStream);
                setStreamVolume(groupedStream, index, flags, null, callingPackage, callingPackage, attributionTag, android.os.Binder.getCallingUid(), true, true);
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Log.d(TAG, "volume group " + groupId + " has internal streams (" + groupedStream + "), do not change associated stream volume");
            }
        }
    }

    public int getVolumeGroupVolumeIndex(int groupId) {
        int volumeIndex;
        super.getVolumeGroupVolumeIndex_enforcePermission();
        synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
            if (sVolumeGroupStates.indexOfKey(groupId) < 0) {
                throw new java.lang.IllegalArgumentException("No volume group for id " + groupId);
            }
            com.android.server.audio.AudioService.VolumeGroupState vgs = sVolumeGroupStates.get(groupId);
            volumeIndex = vgs.isMuted() ? 0 : vgs.getVolumeIndex();
        }
        return volumeIndex;
    }

    public int getVolumeGroupMaxVolumeIndex(int groupId) {
        int maxIndex;
        super.getVolumeGroupMaxVolumeIndex_enforcePermission();
        synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
            if (sVolumeGroupStates.indexOfKey(groupId) < 0) {
                throw new java.lang.IllegalArgumentException("No volume group for id " + groupId);
            }
            com.android.server.audio.AudioService.VolumeGroupState vgs = sVolumeGroupStates.get(groupId);
            maxIndex = vgs.getMaxIndex();
        }
        return maxIndex;
    }

    public int getVolumeGroupMinVolumeIndex(int groupId) {
        int minIndex;
        super.getVolumeGroupMinVolumeIndex_enforcePermission();
        synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
            if (sVolumeGroupStates.indexOfKey(groupId) < 0) {
                throw new java.lang.IllegalArgumentException("No volume group for id " + groupId);
            }
            com.android.server.audio.AudioService.VolumeGroupState vgs = sVolumeGroupStates.get(groupId);
            minIndex = vgs.getMinIndex();
        }
        return minIndex;
    }

    public void setDeviceVolume(android.media.VolumeInfo vi, android.media.AudioDeviceAttributes ada, java.lang.String callingPackage) {
        super.setDeviceVolume_enforcePermission();
        java.util.Objects.requireNonNull(vi);
        java.util.Objects.requireNonNull(ada);
        java.util.Objects.requireNonNull(callingPackage);
        if (!vi.hasStreamType()) {
            android.util.Log.e(TAG, "Unsupported non-stream type based VolumeInfo", new java.lang.Exception());
            return;
        }
        int index = vi.getVolumeIndex();
        if (index == -100 && !vi.hasMuteCommand()) {
            throw new java.lang.IllegalArgumentException("changing device volume requires a volume index or mute command");
        }
        this.mAudioSystem.clearRoutingCache();
        int currDev = getDeviceForStream(vi.getStreamType());
        boolean skipping = currDev == ada.getInternalType();
        sVolumeLogger.enqueue(new com.android.server.audio.AudioServiceEvents.DeviceVolumeEvent(vi.getStreamType(), index, ada, currDev, callingPackage, skipping));
        if (skipping) {
            return;
        }
        if (vi.hasMuteCommand() && vi.isMuted() && !isStreamMute(vi.getStreamType())) {
            setStreamVolumeWithAttributionInt(vi.getStreamType(), this.mStreamStates[vi.getStreamType()].getMinIndex(), 0, ada, callingPackage, null, false);
            return;
        }
        int index2 = index;
        sVolumeLogger.enqueueAndLog("setDeviceVolume from:" + callingPackage + " " + vi + " " + ada, 0, TAG);
        if (vi.getMinVolumeIndex() == -100 || vi.getMaxVolumeIndex() == -100) {
            if (index2 * 10 < this.mStreamStates[vi.getStreamType()].getMinIndex() || index2 * 10 > this.mStreamStates[vi.getStreamType()].getMaxIndex()) {
                throw new java.lang.IllegalArgumentException("invalid volume index " + index2 + " not between min/max for stream " + vi.getStreamType());
            }
        } else {
            int min = (this.mStreamStates[vi.getStreamType()].getMinIndex() + 5) / 10;
            int max = (this.mStreamStates[vi.getStreamType()].getMaxIndex() + 5) / 10;
            if (vi.getMinVolumeIndex() != min || vi.getMaxVolumeIndex() != max) {
                index2 = rescaleIndex(index2, vi.getMinVolumeIndex(), vi.getMaxVolumeIndex(), min, max);
            }
        }
        setStreamVolumeWithAttributionInt(vi.getStreamType(), index2, 0, ada, callingPackage, null, false);
    }

    public void setStreamVolume(int streamType, int index, int flags, java.lang.String callingPackage) {
        setStreamVolumeWithAttribution(streamType, index, flags, callingPackage, null);
    }

    public void adjustVolumeGroupVolume(int groupId, int direction, int flags, java.lang.String callingPackage) throws java.lang.Throwable {
        ensureValidDirection(direction);
        if (sVolumeGroupStates.indexOfKey(groupId) < 0) {
            android.util.Log.e(TAG, ": no volume group found for id " + groupId);
            return;
        }
        com.android.server.audio.AudioService.VolumeGroupState vgs = sVolumeGroupStates.get(groupId);
        boolean fallbackOnStream = false;
        for (int stream : vgs.getLegacyStreamTypes()) {
            try {
                ensureValidStreamType(stream);
                if (vgs.isVssMuteBijective(stream)) {
                    adjustStreamVolume(stream, direction, flags, callingPackage);
                    if (isMuteAdjust(direction)) {
                        return;
                    } else {
                        fallbackOnStream = true;
                    }
                } else {
                    continue;
                }
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Log.d(TAG, "volume group " + groupId + " has internal streams (" + stream + "), do not change associated stream volume");
            }
        }
        if (fallbackOnStream) {
            return;
        }
        sVolumeLogger.enqueue(new com.android.server.audio.AudioServiceEvents.VolumeEvent(11, vgs.name(), direction, flags, callingPackage));
        vgs.adjustVolume(direction, flags);
    }

    public int getLastAudibleVolumeForVolumeGroup(int groupId) {
        super.getLastAudibleVolumeForVolumeGroup_enforcePermission();
        synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
            if (sVolumeGroupStates.indexOfKey(groupId) < 0) {
                android.util.Log.e(TAG, ": no volume group found for id " + groupId);
                return 0;
            }
            com.android.server.audio.AudioService.VolumeGroupState vgs = sVolumeGroupStates.get(groupId);
            return vgs.getVolumeIndex();
        }
    }

    public boolean isVolumeGroupMuted(int groupId) {
        synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
            if (sVolumeGroupStates.indexOfKey(groupId) < 0) {
                android.util.Log.e(TAG, ": no volume group found for id " + groupId);
                return false;
            }
            com.android.server.audio.AudioService.VolumeGroupState vgs = sVolumeGroupStates.get(groupId);
            return vgs.isMuted();
        }
    }

    public void setStreamVolumeWithAttribution(int streamType, int index, int flags, java.lang.String callingPackage, java.lang.String attributionTag) {
        if (this.mHardeningEnforcer.blockVolumeMethod(100)) {
            return;
        }
        setStreamVolumeWithAttributionInt(streamType, index, flags, null, callingPackage, attributionTag, true);
    }

    protected void setStreamVolumeWithAttributionInt(int streamType, int index, int flags, android.media.AudioDeviceAttributes ada, java.lang.String callingPackage, java.lang.String attributionTag, boolean canChangeMuteAndUpdateController) {
        android.media.AudioDeviceAttributes ada2;
        if (streamType == 10 && !canChangeAccessibilityVolume()) {
            android.util.Log.w(TAG, "Trying to call setStreamVolume() for a11y without CHANGE_ACCESSIBILITY_VOLUME  callingPackage=" + callingPackage);
            return;
        }
        if (this.mStreamStates[2].mIsMuted && !"com.coloros.scenemode".equals(callingPackage) && streamType == 5 && !"android.media.audio.cts".equals(callingPackage)) {
            android.util.Log.d(TAG, "setStreamVolume STREAM_RING muted,don't change STREAM_NOTIFICATION volume");
            return;
        }
        if (streamType == 0 && index == 0 && this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") != 0) {
            android.util.Log.w(TAG, "Trying to call setStreamVolume() for STREAM_VOICE_CALL and index 0 without MODIFY_PHONE_STATE  callingPackage=" + callingPackage);
            return;
        }
        if (streamType == 11 && this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_AUDIO_ROUTING") != 0) {
            android.util.Log.w(TAG, "Trying to call setStreamVolume() for STREAM_ASSISTANT without MODIFY_AUDIO_ROUTING  callingPackage=" + callingPackage);
            return;
        }
        if (ada != null) {
            ada2 = ada;
        } else {
            int deviceType = getDeviceForStream(streamType);
            sVolumeLogger.enqueue(new com.android.server.audio.AudioServiceEvents.VolumeEvent(2, streamType, index, flags, getStreamVolume(streamType, deviceType), callingPackage));
            ada2 = new android.media.AudioDeviceAttributes(deviceType, "");
        }
        setStreamVolume(streamType, index, flags, ada2, callingPackage, callingPackage, attributionTag, android.os.Binder.getCallingUid(), callingOrSelfHasAudioSettingsPermission(), canChangeMuteAndUpdateController);
    }

    public boolean isUltrasoundSupported() {
        super.isUltrasoundSupported_enforcePermission();
        return android.media.AudioSystem.isUltrasoundSupported();
    }

    public boolean isHotwordStreamSupported(boolean lookbackAudio) {
        super.isHotwordStreamSupported_enforcePermission();
        try {
            return this.mAudioPolicy.isHotwordStreamSupported(lookbackAudio);
        } catch (java.lang.IllegalStateException e) {
            android.util.Log.e(TAG, "Suppressing exception calling into AudioPolicy", e);
            return false;
        }
    }

    private boolean canChangeAccessibilityVolume() {
        synchronized (this.mAccessibilityServiceUidsLock) {
            if (this.mContext.checkCallingOrSelfPermission("android.permission.CHANGE_ACCESSIBILITY_VOLUME") == 0) {
                return true;
            }
            if (this.mAccessibilityServiceUids != null) {
                int callingUid = android.os.Binder.getCallingUid();
                for (int i = 0; i < this.mAccessibilityServiceUids.length; i++) {
                    if (this.mAccessibilityServiceUids[i] == callingUid) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public int getBluetoothContextualVolumeStream() {
        return getBluetoothContextualVolumeStream(this.mMode.get());
    }

    private int getBluetoothContextualVolumeStream(int mode) {
        boolean voiceActivityCanOverride = true;
        switch (mode) {
            case 2:
            case 3:
                return 0;
            case 4:
            case 5:
            case 6:
                voiceActivityCanOverride = false;
                break;
        }
        if ((voiceActivityCanOverride && this.mVoicePlaybackActive.get() && android.media.AudioSystem.isStreamActive(0, 0)) || this.mAsExt.getBleRingPlaybackActive()) {
            return 0;
        }
        return 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPlaybackConfigChange(java.util.List<android.media.AudioPlaybackConfiguration> configs) {
        boolean voiceActive = false;
        boolean mediaActive = false;
        for (android.media.AudioPlaybackConfiguration config : configs) {
            int usage = config.getAudioAttributes().getUsage();
            if (config.isActive()) {
                if (usage == 2 || usage == 3) {
                    voiceActive = true;
                }
                if (usage == 1 || usage == 14 || usage == 0) {
                    mediaActive = true;
                }
            }
        }
        if (this.mVoicePlaybackActive.getAndSet(voiceActive) != voiceActive) {
            updateHearingAidVolumeOnVoiceActivityUpdate();
        }
        if (this.mMediaPlaybackActive.getAndSet(mediaActive) != mediaActive && mediaActive) {
            this.mSoundDoseHelper.scheduleMusicActiveCheck();
        }
        this.mLoudnessCodecHelper.updateCodecParameters(configs);
        this.mAsExt.setBleRingPlaybackActive(configs);
        if (this.mAsExt.isAudioRouteSupported()) {
            this.mDeviceBroker.getWrapper().sendIILMsg(82, 1, 0, 0, null, 6000);
        }
        if (this.mAsExt.getVocalProminenceSupport() && this.mStreamStates != null) {
            int device = getDeviceForStream(0);
            sendMsg(this.mAudioHandler, 116, 0, 0, device, null, 500);
        }
        updateAudioModeHandlers(configs, null);
        this.mDeviceBroker.updateCommunicationRouteClientsActivity(configs, null);
    }

    void updateAudioModeHandlers(java.util.List<android.media.AudioPlaybackConfiguration> playbackConfigs, java.util.List<android.media.AudioRecordingConfiguration> recordConfigs) {
        synchronized (this.mDeviceBroker.mSetModeLock) {
            boolean updateAudioMode = false;
            int existingMsgPolicy = 2;
            int delay = 6000;
            for (com.android.server.audio.AudioService.SetModeDeathHandler h : this.mSetModeDeathHandlers) {
                boolean wasActive = h.isActive();
                if (playbackConfigs != null) {
                    h.setPlaybackActive(false);
                    java.util.Iterator<android.media.AudioPlaybackConfiguration> it = playbackConfigs.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        android.media.AudioPlaybackConfiguration config = it.next();
                        int usage = config.getAudioAttributes().getUsage();
                        if (config.getClientUid() == h.getUid() && ((usage == 2 || usage == 3) && config.isActive())) {
                            h.setPlaybackActive(true);
                            break;
                        }
                    }
                }
                if (recordConfigs != null) {
                    h.setRecordingActive(false);
                    java.util.Iterator<android.media.AudioRecordingConfiguration> it2 = recordConfigs.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        android.media.AudioRecordingConfiguration config2 = it2.next();
                        if (config2.getClientUid() == h.getUid() && !config2.isClientSilenced() && config2.getAudioSource() == 7) {
                            h.setRecordingActive(true);
                            break;
                        }
                    }
                }
                if (wasActive != h.isActive()) {
                    updateAudioMode = true;
                    if (h.isActive() && h == getAudioModeOwnerHandler()) {
                        existingMsgPolicy = 0;
                        delay = 0;
                    }
                }
            }
            if (updateAudioMode) {
                sendMsg(this.mAudioHandler, 36, existingMsgPolicy, -1, android.os.Process.myPid(), this.mContext.getPackageName(), delay);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRecordingConfigChange(java.util.List<android.media.AudioRecordingConfiguration> configs) {
        if (this.mAsExt.isAudioRouteSupported()) {
            this.mDeviceBroker.getWrapper().sendIILMsg(82, 1, 0, 0, null, 6000);
        }
        if (this.mMicMuteFromSystemCached) {
            int userId = getCurrentUserId();
            sendMsg(this.mAudioHandler, 83, 2, userId, 0, null, 6000);
        }
        if (this.mAsExt.getVocalProminenceSupport() && this.mStreamStates != null) {
            int device = getDeviceForStream(0);
            sendMsg(this.mAudioHandler, 116, 0, 0, device, null, 500);
        }
        updateAudioModeHandlers(null, configs);
        this.mDeviceBroker.updateCommunicationRouteClientsActivity(null, configs);
    }

    private void dumpFlags(java.io.PrintWriter pw) {
        pw.println("\nFun with Flags:");
        pw.println("\tandroid.media.audio.autoPublicVolumeApiHardening:" + android.media.audio.Flags.autoPublicVolumeApiHardening());
        pw.println("\tandroid.media.audio.Flags.automaticBtDeviceType:" + android.media.audio.Flags.automaticBtDeviceType());
        pw.println("\tandroid.media.audio.featureSpatialAudioHeadtrackingLowLatency:" + android.media.audio.Flags.featureSpatialAudioHeadtrackingLowLatency());
        pw.println("\tandroid.media.audio.focusFreezeTestApi:" + android.media.audio.Flags.focusFreezeTestApi());
        pw.println("\tcom.android.media.audio.audioserverPermissions:" + com.android.media.audio.Flags.audioserverPermissions());
        pw.println("\tcom.android.media.audio.disablePrescaleAbsoluteVolume:" + com.android.media.audio.Flags.disablePrescaleAbsoluteVolume());
        pw.println("\tcom.android.media.audio.setStreamVolumeOrder:" + com.android.media.audio.Flags.setStreamVolumeOrder());
        pw.println("\tandroid.media.audio.roForegroundAudioControl:" + android.media.audio.Flags.roForegroundAudioControl());
        pw.println("\tandroid.media.audio.scoManagedByAudio:" + android.media.audio.Flags.scoManagedByAudio());
        pw.println("\tcom.android.media.audio.vgsVssSyncMuteOrder:" + com.android.media.audio.Flags.vgsVssSyncMuteOrder());
        pw.println("\tcom.android.media.audio.absVolumeIndexFix:" + com.android.media.audio.Flags.absVolumeIndexFix());
    }

    private void dumpAudioMode(java.io.PrintWriter pw) {
        pw.println("\nAudio mode: ");
        pw.println("- Requested mode = " + android.media.AudioSystem.modeToString(getMode()));
        pw.println("- Actual mode = " + android.media.AudioSystem.modeToString(this.mMode.get()));
        pw.println("- Mode owner: ");
        com.android.server.audio.AudioService.SetModeDeathHandler hdlr = getAudioModeOwnerHandler();
        if (hdlr != null) {
            hdlr.dump(pw, -1);
        } else {
            pw.println("   None");
        }
        pw.println("- Mode owner stack: ");
        if (this.mSetModeDeathHandlers.isEmpty()) {
            pw.println("   Empty");
            return;
        }
        for (int i = 0; i < this.mSetModeDeathHandlers.size(); i++) {
            this.mSetModeDeathHandlers.get(i).dump(pw, i);
        }
    }

    private void updateHearingAidVolumeOnVoiceActivityUpdate() {
        int streamType = getBluetoothContextualVolumeStream();
        int index = getStreamVolume(streamType);
        sVolumeLogger.enqueue(new com.android.server.audio.AudioServiceEvents.VolumeEvent(6, this.mVoicePlaybackActive.get(), streamType, index));
        this.mDeviceBroker.postSetHearingAidVolumeIndex(index * 10, streamType);
    }

    void updateAbsVolumeMultiModeDevices(int oldMode, int newMode) {
        if (oldMode == newMode) {
        }
        switch (newMode) {
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                int streamType = getBluetoothContextualVolumeStream(newMode);
                java.util.Set<java.lang.Integer> deviceTypes = getDeviceSetForStreamDirect(streamType);
                java.util.Set<java.lang.Integer> absVolumeMultiModeCaseDevices = android.media.AudioSystem.intersectionAudioDeviceTypes(this.mAbsVolumeMultiModeCaseDevices, deviceTypes);
                if (!absVolumeMultiModeCaseDevices.isEmpty() && android.media.AudioSystem.isSingleAudioDeviceType(absVolumeMultiModeCaseDevices, 134217728)) {
                    int index = getStreamVolume(streamType);
                    sVolumeLogger.enqueue(new com.android.server.audio.AudioServiceEvents.VolumeEvent(7, newMode, streamType, index));
                    this.mDeviceBroker.postSetHearingAidVolumeIndex(index * 10, streamType);
                }
                break;
        }
    }

    private void setLeAudioVolumeOnModeUpdate(int mode, int device, int streamType, int index, int maxIndex) {
        switch (mode) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                if (!android.media.AudioSystem.isLeAudioDeviceType(device)) {
                    android.util.Log.w(TAG, "setLeAudioVolumeOnModeUpdate ignoring invalid device=" + device + ", mode=" + mode + ", index=" + index + " maxIndex=" + maxIndex + " streamType=" + streamType);
                } else {
                    if (DEBUG_VOL) {
                        android.util.Log.d(TAG, "setLeAudioVolumeOnModeUpdate postSetLeAudioVolumeIndex device=" + device + ", mode=" + mode + ", index=" + index + " maxIndex=" + maxIndex + " streamType=" + streamType);
                    }
                    this.mDeviceBroker.postSetLeAudioVolumeIndex(index, maxIndex, streamType);
                    this.mDeviceBroker.postApplyVolumeOnDevice(streamType, device, "setLeAudioVolumeOnModeUpdate");
                }
                break;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:138:0x02d0  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0338  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0389  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0392  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void setStreamVolume(int r22, int r23, int r24, android.media.AudioDeviceAttributes r25, java.lang.String r26, java.lang.String r27, java.lang.String r28, int r29, boolean r30, boolean r31) {
        /*
            Method dump skipped, instruction units count: 956
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.AudioService.setStreamVolume(int, int, int, android.media.AudioDeviceAttributes, java.lang.String, java.lang.String, java.lang.String, int, boolean, boolean):void");
    }

    private void dispatchAbsoluteVolumeChanged(int streamType, com.android.server.audio.AudioService.AbsoluteVolumeDeviceInfo deviceInfo, int index) {
        android.media.VolumeInfo volumeInfo = deviceInfo.getMatchingVolumeInfoForStream(streamType);
        if (volumeInfo != null) {
            try {
                deviceInfo.mCallback.dispatchDeviceVolumeChanged(deviceInfo.mDevice, new android.media.VolumeInfo.Builder(volumeInfo).setVolumeIndex(rescaleIndex(index, streamType, volumeInfo)).build());
            } catch (android.os.RemoteException e) {
                android.util.Log.w(TAG, "Couldn't dispatch absolute volume behavior volume change");
            }
        }
    }

    private void dispatchAbsoluteVolumeAdjusted(int streamType, com.android.server.audio.AudioService.AbsoluteVolumeDeviceInfo deviceInfo, int index, int direction, int mode) {
        android.media.VolumeInfo volumeInfo = deviceInfo.getMatchingVolumeInfoForStream(streamType);
        if (volumeInfo != null) {
            try {
                deviceInfo.mCallback.dispatchDeviceVolumeAdjusted(deviceInfo.mDevice, new android.media.VolumeInfo.Builder(volumeInfo).setVolumeIndex(rescaleIndex(index, streamType, volumeInfo)).build(), direction, mode);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(TAG, "Couldn't dispatch absolute volume behavior volume adjustment");
            }
        }
    }

    private boolean volumeAdjustmentAllowedByDnd(int streamTypeAlias, int flags) {
        switch (this.mNm.getZenMode()) {
            case 1:
            case 2:
            case 3:
                if (!isStreamMutedByRingerOrZenMode(streamTypeAlias) || isUiSoundsStreamType(streamTypeAlias) || (flags & 2) != 0) {
                }
                break;
        }
        return true;
    }

    public void forceVolumeControlStream(int streamType, android.os.IBinder cb) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") != 0) {
            return;
        }
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, java.lang.String.format("forceVolumeControlStream(%d)", java.lang.Integer.valueOf(streamType)));
        }
        synchronized (this.mForceControlStreamLock) {
            if (this.mVolumeControlStream != -1 && streamType != -1) {
                this.mUserSelectedVolumeControlStream = true;
            }
            this.mVolumeControlStream = streamType;
            if (this.mVolumeControlStream == -1) {
                if (this.mForceControlStreamClient != null) {
                    this.mForceControlStreamClient.release();
                    this.mForceControlStreamClient = null;
                }
                this.mUserSelectedVolumeControlStream = false;
            } else if (this.mForceControlStreamClient == null) {
                this.mForceControlStreamClient = new com.android.server.audio.AudioService.ForceControlStreamClient(cb);
            } else if (this.mForceControlStreamClient.getBinder() == cb) {
                android.util.Log.d(TAG, "forceVolumeControlStream cb:" + cb + " is already linked.");
            } else {
                this.mForceControlStreamClient.release();
                this.mForceControlStreamClient = new com.android.server.audio.AudioService.ForceControlStreamClient(cb);
            }
        }
    }

    private class ForceControlStreamClient implements android.os.IBinder.DeathRecipient {
        private android.os.IBinder mCb;

        ForceControlStreamClient(android.os.IBinder cb) {
            if (cb != null) {
                try {
                    cb.linkToDeath(this, 0);
                } catch (android.os.RemoteException e) {
                    android.util.Log.w(com.android.server.audio.AudioService.TAG, "ForceControlStreamClient() could not link to " + cb + " binder death");
                    cb = null;
                }
            }
            this.mCb = cb;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.audio.AudioService.this.mForceControlStreamLock) {
                android.util.Log.w(com.android.server.audio.AudioService.TAG, "SCO client died");
                if (com.android.server.audio.AudioService.this.mForceControlStreamClient != this) {
                    android.util.Log.w(com.android.server.audio.AudioService.TAG, "unregistered control stream client died");
                } else {
                    com.android.server.audio.AudioService.this.mForceControlStreamClient = null;
                    com.android.server.audio.AudioService.this.mVolumeControlStream = -1;
                    com.android.server.audio.AudioService.this.mUserSelectedVolumeControlStream = false;
                }
            }
        }

        public void release() {
            if (this.mCb != null) {
                this.mCb.unlinkToDeath(this, 0);
                this.mCb = null;
            }
        }

        public android.os.IBinder getBinder() {
            return this.mCb;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendBroadcastToAll(android.content.Intent intent, android.os.Bundle options) {
        if (!this.mSystemServer.isPrivileged()) {
            return;
        }
        intent.addFlags(67108864);
        intent.addFlags(268435456);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL, null, options);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private void sendStickyBroadcastToAll(android.content.Intent intent) {
        intent.addFlags(268435456);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mContext.sendStickyBroadcastAsUser(intent, android.os.UserHandle.ALL);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCurrentUserId() {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            android.content.pm.UserInfo currentUser = android.app.ActivityManager.getService().getCurrentUser();
            int i = currentUser.id;
            android.os.Binder.restoreCallingIdentity(ident);
            return i;
        } catch (android.os.RemoteException e) {
            android.os.Binder.restoreCallingIdentity(ident);
            return 0;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
    }

    protected void sendVolumeUpdate(int streamType, int oldIndex, int index, int flags, int device) {
        int streamType2 = mStreamVolumeAlias[streamType];
        if (streamType2 == 3 && isFullVolumeDevice(device)) {
            flags &= -2;
        }
        this.mVolumeController.postVolumeChanged(streamType2, flags);
    }

    private int updateFlagsForTvPlatform(int flags) {
        synchronized (this.mHdmiClientLock) {
            if (this.mHdmiTvClient != null && this.mHdmiSystemAudioSupported && this.mHdmiCecVolumeControlEnabled) {
                flags &= -2;
            }
        }
        return flags;
    }

    private void sendMasterMuteUpdate(boolean z, int i) {
        this.mVolumeController.postMasterMuteChanged(updateFlagsForTvPlatform(i));
        sendMsg(this.mAudioHandler, 55, 2, z ? 1 : 0, 0, null, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setStreamVolumeInt(int streamType, int index, int device, boolean force, java.lang.String caller, boolean hasModifyAudioSettings) {
        if (isFullVolumeDevice(device)) {
            return;
        }
        com.android.server.audio.AudioService.VolumeStreamState streamState = this.mStreamStates[streamType];
        if (streamState.setIndex(index, device, caller, hasModifyAudioSettings) || force) {
            sendMsg(this.mAudioHandler, 0, 2, device, 0, streamState, 0);
        }
    }

    public boolean isStreamMute(int streamType) {
        boolean z;
        if (streamType == Integer.MIN_VALUE) {
            streamType = getActiveStreamType(streamType);
        }
        synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
            ensureValidStreamType(streamType);
            z = this.mStreamStates[streamType].mIsMuted;
        }
        return z;
    }

    private class RmtSbmxFullVolDeathHandler implements android.os.IBinder.DeathRecipient {
        private android.os.IBinder mICallback;

        RmtSbmxFullVolDeathHandler(android.os.IBinder cb) {
            this.mICallback = cb;
            try {
                cb.linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(com.android.server.audio.AudioService.TAG, "can't link to death", e);
            }
        }

        boolean isHandlerFor(android.os.IBinder cb) {
            return this.mICallback.equals(cb);
        }

        void forget() {
            try {
                this.mICallback.unlinkToDeath(this, 0);
            } catch (java.util.NoSuchElementException e) {
                android.util.Log.e(com.android.server.audio.AudioService.TAG, "error unlinking to death", e);
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            android.util.Log.w(com.android.server.audio.AudioService.TAG, "Recorder with remote submix at full volume died " + this.mICallback);
            com.android.server.audio.AudioService.this.forceRemoteSubmixFullVolume(false, this.mICallback);
        }
    }

    private boolean discardRmtSbmxFullVolDeathHandlerFor(android.os.IBinder cb) {
        for (com.android.server.audio.AudioService.RmtSbmxFullVolDeathHandler handler : this.mRmtSbmxFullVolDeathHandlers) {
            if (handler.isHandlerFor(cb)) {
                handler.forget();
                this.mRmtSbmxFullVolDeathHandlers.remove(handler);
                return true;
            }
        }
        return false;
    }

    private boolean hasRmtSbmxFullVolDeathHandlerFor(android.os.IBinder cb) {
        java.util.Iterator<com.android.server.audio.AudioService.RmtSbmxFullVolDeathHandler> it = this.mRmtSbmxFullVolDeathHandlers.iterator();
        while (it.hasNext()) {
            if (it.next().isHandlerFor(cb)) {
                return true;
            }
        }
        return false;
    }

    public void forceRemoteSubmixFullVolume(boolean startForcing, android.os.IBinder cb) {
        if (cb == null) {
            return;
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.CAPTURE_AUDIO_OUTPUT") != 0) {
            android.util.Log.w(TAG, "Trying to call forceRemoteSubmixFullVolume() without CAPTURE_AUDIO_OUTPUT");
            return;
        }
        synchronized (this.mRmtSbmxFullVolDeathHandlers) {
            boolean applyRequired = false;
            if (startForcing) {
                if (!hasRmtSbmxFullVolDeathHandlerFor(cb)) {
                    this.mRmtSbmxFullVolDeathHandlers.add(new com.android.server.audio.AudioService.RmtSbmxFullVolDeathHandler(cb));
                    if (this.mRmtSbmxFullVolRefCount == 0) {
                        this.mFullVolumeDevices.add(32768);
                        this.mFixedVolumeDevices.add(32768);
                        applyRequired = true;
                    }
                    this.mRmtSbmxFullVolRefCount++;
                }
            } else if (discardRmtSbmxFullVolDeathHandlerFor(cb) && this.mRmtSbmxFullVolRefCount > 0) {
                this.mRmtSbmxFullVolRefCount--;
                if (this.mRmtSbmxFullVolRefCount == 0) {
                    this.mFullVolumeDevices.remove(32768);
                    this.mFixedVolumeDevices.remove(32768);
                    applyRequired = true;
                }
            }
            if (applyRequired) {
                checkAllFixedVolumeDevices(3);
                this.mStreamStates[3].applyAllVolumes();
            }
        }
    }

    private void setMasterMuteInternal(boolean mute, int flags, java.lang.String callingPackage, int uid, int userId, int pid, java.lang.String attributionTag) {
        if (uid == 1000) {
            uid = android.os.UserHandle.getUid(userId, android.os.UserHandle.getAppId(uid));
        }
        if (!mute && !checkNoteAppOp(33, uid, callingPackage, attributionTag)) {
            return;
        }
        if (userId != android.os.UserHandle.getCallingUserId() && this.mContext.checkPermission("android.permission.INTERACT_ACROSS_USERS_FULL", pid, uid) != 0) {
            return;
        }
        setMasterMuteInternalNoCallerCheck(mute, flags, userId, "setMasterMute");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setMasterMuteInternalNoCallerCheck(boolean mute, int flags, int userId, java.lang.String eventSource) {
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, android.text.TextUtils.formatSimple("Master mute %s, flags 0x%x, userId=%d from %s", new java.lang.Object[]{java.lang.Boolean.valueOf(mute), java.lang.Integer.valueOf(flags), java.lang.Integer.valueOf(userId), eventSource}));
        }
        if (!isPlatformAutomotive() && this.mUseFixedVolume) {
            mute = false;
        }
        if (((isPlatformAutomotive() && userId == 0) || getCurrentUserId() == userId) && mute != this.mMasterMute.getAndSet(mute)) {
            sVolumeLogger.enqueue(new com.android.server.audio.AudioServiceEvents.VolumeEvent(12, mute));
            this.mAudioSystem.setMasterMute(mute);
            sendMasterMuteUpdate(mute, flags);
        }
    }

    public boolean isMasterMute() {
        return this.mMasterMute.get();
    }

    public void setMasterMute(boolean mute, int flags, java.lang.String callingPackage, int userId, java.lang.String attributionTag) {
        super.setMasterMute_enforcePermission();
        setMasterMuteInternal(mute, flags, callingPackage, android.os.Binder.getCallingUid(), userId, android.os.Binder.getCallingPid(), attributionTag);
    }

    public int getStreamVolume(int streamType) {
        ensureValidStreamType(streamType);
        int device = getDeviceForStream(streamType);
        if (device == 0) {
            android.util.Log.d(TAG, "getDeviceForStream return none, return directly");
            return android.media.AudioSystem.DEFAULT_STREAM_VOLUME[streamType];
        }
        return getStreamVolume(streamType, device);
    }

    private int getStreamVolume(int streamType, int device) {
        synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
            int callingUid = android.os.Binder.getCallingUid();
            if (this.mAsExt.isNeedGetOplusStreamVolume(streamType, callingUid)) {
                return this.mAsExt.getOplusStreamVolume(this.mStreamStates.length, streamType, callingUid);
            }
            int index = this.mStreamStates[streamType].getIndex(device);
            if (this.mStreamStates[streamType].mIsMuted) {
                index = 0;
            }
            if (index != 0 && mStreamVolumeAlias[streamType] == 3 && isFixedVolumeDevice(device)) {
                index = this.mStreamStates[streamType].getMaxIndex();
            }
            return (index + 5) / 10;
        }
    }

    public android.media.VolumeInfo getDeviceVolume(android.media.VolumeInfo vi, android.media.AudioDeviceAttributes ada, java.lang.String callingPackage) {
        int index;
        android.media.VolumeInfo volumeInfoBuild;
        super.getDeviceVolume_enforcePermission();
        java.util.Objects.requireNonNull(vi);
        java.util.Objects.requireNonNull(ada);
        java.util.Objects.requireNonNull(callingPackage);
        if (!vi.hasStreamType()) {
            android.util.Log.e(TAG, "Unsupported non-stream type based VolumeInfo", new java.lang.Exception());
            return getDefaultVolumeInfo();
        }
        int streamType = vi.getStreamType();
        android.media.VolumeInfo.Builder vib = new android.media.VolumeInfo.Builder(vi);
        vib.setMinVolumeIndex((this.mStreamStates[streamType].mIndexMin + 5) / 10);
        vib.setMaxVolumeIndex((this.mStreamStates[streamType].mIndexMax + 5) / 10);
        synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
            if (isFixedVolumeDevice(ada.getInternalType())) {
                index = (this.mStreamStates[streamType].mIndexMax + 5) / 10;
            } else {
                index = (this.mStreamStates[streamType].getIndex(ada.getInternalType()) + 5) / 10;
            }
            vib.setVolumeIndex(index);
            if (this.mStreamStates[streamType].mIsMuted) {
                vib.setMuted(true);
            }
            volumeInfoBuild = vib.build();
        }
        return volumeInfoBuild;
    }

    public int getStreamMaxVolume(int streamType) {
        ensureValidStreamType(streamType);
        return (this.mStreamStates[streamType].getMaxIndex() + 5) / 10;
    }

    public int getStreamMinVolume(int streamType) {
        ensureValidStreamType(streamType);
        boolean isPrivileged = android.os.Binder.getCallingUid() == 1000 || callingHasAudioSettingsPermission() || this.mContext.checkCallingPermission("android.permission.MODIFY_AUDIO_ROUTING") == 0;
        return (this.mStreamStates[streamType].getMinIndex(isPrivileged) + 5) / 10;
    }

    public int getLastAudibleStreamVolume(int streamType) {
        super.getLastAudibleStreamVolume_enforcePermission();
        ensureValidStreamType(streamType);
        int device = getDeviceForStream(streamType);
        return (this.mStreamStates[streamType].getIndex(device) + 5) / 10;
    }

    public android.media.VolumeInfo getDefaultVolumeInfo() {
        if (sDefaultVolumeInfo == null) {
            sDefaultVolumeInfo = new android.media.VolumeInfo.Builder(3).setMinVolumeIndex(getStreamMinVolume(3)).setMaxVolumeIndex(getStreamMaxVolume(3)).build();
        }
        return sDefaultVolumeInfo;
    }

    public void registerStreamAliasingDispatcher(android.media.IStreamAliasingDispatcher isad, boolean register) {
        super.registerStreamAliasingDispatcher_enforcePermission();
        java.util.Objects.requireNonNull(isad);
        if (register) {
            this.mStreamAliasingDispatchers.register(isad);
        } else {
            this.mStreamAliasingDispatchers.unregister(isad);
        }
    }

    protected void dispatchStreamAliasingUpdate() {
        int nbDispatchers = this.mStreamAliasingDispatchers.beginBroadcast();
        for (int i = 0; i < nbDispatchers; i++) {
            try {
                this.mStreamAliasingDispatchers.getBroadcastItem(i).dispatchStreamAliasingChanged();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Error on stream alias update dispatch", e);
            }
        }
        this.mStreamAliasingDispatchers.finishBroadcast();
    }

    /* JADX INFO: renamed from: getIndependentStreamTypes, reason: merged with bridge method [inline-methods] */
    public java.util.ArrayList<java.lang.Integer> m1981getIndependentStreamTypes() {
        super.getIndependentStreamTypes_enforcePermission();
        if (this.mUseVolumeGroupAliases) {
            return new java.util.ArrayList<>(java.util.Arrays.stream(android.media.AudioManager.getPublicStreamTypes()).boxed().toList());
        }
        java.util.ArrayList<java.lang.Integer> res = new java.util.ArrayList<>(1);
        for (int stream : mStreamVolumeAlias) {
            if (!res.contains(java.lang.Integer.valueOf(stream))) {
                res.add(java.lang.Integer.valueOf(stream));
            }
        }
        return res;
    }

    public int getStreamTypeAlias(int sourceStreamType) {
        super.getStreamTypeAlias_enforcePermission();
        ensureValidStreamType(sourceStreamType);
        return mStreamVolumeAlias[sourceStreamType];
    }

    public boolean isVolumeControlUsingVolumeGroups() {
        super.isVolumeControlUsingVolumeGroups_enforcePermission();
        return this.mUseVolumeGroupAliases;
    }

    public int getUiSoundsStreamType() {
        return this.mUseVolumeGroupAliases ? this.STREAM_VOLUME_ALIAS_VOICE[1] : mStreamVolumeAlias[1];
    }

    private boolean isUiSoundsStreamType(int aliasStreamType) {
        return this.mUseVolumeGroupAliases ? this.STREAM_VOLUME_ALIAS_VOICE[aliasStreamType] == this.STREAM_VOLUME_ALIAS_VOICE[1] : aliasStreamType == mStreamVolumeAlias[1];
    }

    public void setMicrophoneMute(boolean on, java.lang.String callingPackage, int userId, java.lang.String attributionTag) {
        int uid = android.os.Binder.getCallingUid();
        if (uid == 1000) {
            uid = android.os.UserHandle.getUid(userId, android.os.UserHandle.getAppId(uid));
        }
        android.media.MediaMetrics.Item mmi = new android.media.MediaMetrics.Item("audio.mic").setUid(uid).set(android.media.MediaMetrics.Property.CALLING_PACKAGE, callingPackage).set(android.media.MediaMetrics.Property.EVENT, "setMicrophoneMute").set(android.media.MediaMetrics.Property.REQUEST, on ? "mute" : "unmute");
        if (!on && !checkNoteAppOp(44, uid, callingPackage, attributionTag)) {
            mmi.set(android.media.MediaMetrics.Property.EARLY_RETURN, "disallow unmuting").record();
            return;
        }
        if (!checkAudioSettingsPermission("setMicrophoneMute()")) {
            mmi.set(android.media.MediaMetrics.Property.EARLY_RETURN, "!checkAudioSettingsPermission").record();
            return;
        }
        if (userId != android.os.UserHandle.getCallingUserId() && this.mContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL") != 0) {
            mmi.set(android.media.MediaMetrics.Property.EARLY_RETURN, com.android.server.permission.access.PermissionUri.SCHEME).record();
            return;
        }
        this.mMicMuteFromApi = on;
        mmi.record();
        this.mAsExt.recordMicMuteEventInfo(on, callingPackage, userId, uid);
        setMicrophoneMuteNoCallerCheck(userId);
    }

    public void setMicrophoneMuteFromSwitch(boolean on) {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 1000) {
            android.util.Log.e(TAG, "setMicrophoneMuteFromSwitch() called from non system user!");
            return;
        }
        this.mMicMuteFromSwitch = on;
        new android.media.MediaMetrics.Item("audio.mic").setUid(callingUid).set(android.media.MediaMetrics.Property.EVENT, "setMicrophoneMuteFromSwitch").set(android.media.MediaMetrics.Property.REQUEST, on ? "mute" : "unmute").record();
        setMicrophoneMuteNoCallerCheck(android.os.UserHandle.getCallingUserId());
    }

    private void setMicMuteFromSwitchInput() {
        android.hardware.input.InputManager im = (android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class);
        int isMicMuted = im.isMicMuted();
        if (isMicMuted != -1) {
            setMicrophoneMuteFromSwitch(im.isMicMuted() != 0);
        }
    }

    public boolean isMicrophoneMuted() {
        return this.mMicMuteFromSystemCached && (!this.mMicMuteFromPrivacyToggle || this.mMicMuteFromApi || this.mMicMuteFromRestrictions || this.mMicMuteFromSwitch);
    }

    private boolean isMicrophoneSupposedToBeMuted() {
        return this.mMicMuteFromSwitch || this.mMicMuteFromRestrictions || this.mMicMuteFromApi || this.mMicMuteFromPrivacyToggle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setMicrophoneMuteNoCallerCheck(int userId) {
        boolean muted = isMicrophoneSupposedToBeMuted();
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, java.lang.String.format("Mic mute %b, user=%d", java.lang.Boolean.valueOf(muted), java.lang.Integer.valueOf(userId)));
        }
        java.lang.String micForbidStr = getParameters(PARAMETER_GET_MICROPHONE_FORBID);
        if (!android.text.TextUtils.isEmpty(micForbidStr) && micForbidStr.equals("true")) {
            android.util.Log.e(TAG, "micphone is forbided");
            return;
        }
        if (getCurrentUserId() == userId || userId == 0) {
            boolean currentMute = this.mAudioSystem.isMicrophoneMuted();
            int callingUid = android.os.Binder.getCallingUid();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                int ret = this.mAudioSystem.muteMicrophone(muted);
                this.mMicMuteFromSystemCached = this.mAudioSystem.isMicrophoneMuted();
                if (ret != 0) {
                    android.util.Log.e(TAG, "Error changing mic mute state to " + muted + " current:" + this.mMicMuteFromSystemCached);
                }
                new android.media.MediaMetrics.Item("audio.mic").setUid(callingUid).set(android.media.MediaMetrics.Property.EVENT, "setMicrophoneMuteNoCallerCheck").set(android.media.MediaMetrics.Property.MUTE, this.mMicMuteFromSystemCached ? kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_ON : kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_OFF).set(android.media.MediaMetrics.Property.REQUEST, muted ? "mute" : "unmute").set(android.media.MediaMetrics.Property.STATUS, java.lang.Integer.valueOf(ret)).record();
                if (muted != currentMute) {
                    sendMsg(this.mAudioHandler, 30, 1, 0, 0, null, 0);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public int getRingerModeExternal() {
        int i;
        synchronized (this.mSettingsLock) {
            i = this.mRingerModeExternal;
        }
        return i;
    }

    public int getRingerModeInternal() {
        int i;
        synchronized (this.mSettingsLock) {
            i = this.mRingerMode;
        }
        return i;
    }

    private void ensureValidRingerMode(int ringerMode) {
        if (!isValidRingerMode(ringerMode)) {
            throw new java.lang.IllegalArgumentException("Bad ringer mode " + ringerMode);
        }
    }

    public boolean isValidRingerMode(int ringerMode) {
        return ringerMode >= 0 && ringerMode <= 2;
    }

    public void setRingerModeExternal(int ringerMode, java.lang.String caller) {
        if (this.mHardeningEnforcer.blockVolumeMethod(200)) {
            return;
        }
        if (isAndroidNPlus(caller) && wouldToggleZenMode(ringerMode) && !this.mNm.isNotificationPolicyAccessGrantedForPackage(caller)) {
            throw new java.lang.SecurityException("Not allowed to change Do Not Disturb state");
        }
        setRingerMode(ringerMode, caller, true);
    }

    public void setRingerModeInternal(int ringerMode, java.lang.String caller) {
        enforceVolumeController("setRingerModeInternal");
        setRingerMode(ringerMode, caller, false);
    }

    public void silenceRingerModeInternal(java.lang.String reason) {
        android.os.VibrationEffect effect = null;
        int ringerMode = 0;
        int toastText = 0;
        int silenceRingerSetting = 0;
        if (this.mContext.getResources().getBoolean(android.R.bool.config_unfoldTransitionEnabled)) {
            silenceRingerSetting = this.mSettings.getSecureIntForUser(this.mContentResolver, "volume_hush_gesture", 0, -2);
        }
        switch (silenceRingerSetting) {
            case 1:
                effect = android.os.VibrationEffect.get(5);
                ringerMode = 1;
                toastText = android.R.string.twilight_service;
                break;
            case 2:
                effect = android.os.VibrationEffect.get(1);
                ringerMode = 0;
                toastText = android.R.string.tutorial_double_tap_to_zoom_message_short;
                break;
        }
        maybeVibrate(effect, reason);
        setRingerModeInternal(ringerMode, reason);
        android.widget.Toast.makeText(this.mContext, toastText, 0).show();
    }

    private boolean maybeVibrate(android.os.VibrationEffect effect, java.lang.String reason) {
        if (!this.mHasVibrator || effect == null) {
            return false;
        }
        this.mVibrator.vibrate(android.os.Binder.getCallingUid(), this.mContext.getOpPackageName(), effect, reason, TOUCH_VIBRATION_ATTRIBUTES);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRingerMode(int ringerMode, java.lang.String caller, boolean external) {
        int ringerMode2;
        if (this.mUseFixedVolume || this.mIsSingleVolume || this.mUseVolumeGroupAliases) {
            return;
        }
        if (caller == null || caller.length() == 0) {
            throw new java.lang.IllegalArgumentException("Bad caller: " + caller);
        }
        if (this.mAsExt.isAlertSliderSupported() && this.mAsExt.isRingerModeContorlbyAlerSlider(ringerMode, caller, this.mRingerModeExternal, external)) {
            return;
        }
        ensureValidRingerMode(ringerMode);
        if (ringerMode == 1 && !this.mHasVibrator) {
            ringerMode2 = 0;
        } else {
            ringerMode2 = ringerMode;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mSettingsLock) {
                int ringerModeInternal = getRingerModeInternal();
                int ringerModeExternal = getRingerModeExternal();
                if (external) {
                    setRingerModeExt(ringerMode2);
                    if (this.mRingerModeDelegate != null) {
                        ringerMode2 = this.mRingerModeDelegate.onSetRingerModeExternal(ringerModeExternal, ringerMode2, caller, ringerModeInternal, this.mVolumePolicy);
                    }
                    if (ringerMode2 != ringerModeInternal) {
                        setRingerModeInt(ringerMode2, true);
                    }
                } else {
                    if (ringerMode2 != ringerModeInternal) {
                        setRingerModeInt(ringerMode2, true);
                    }
                    if (this.mRingerModeDelegate != null) {
                        ringerMode2 = this.mRingerModeDelegate.onSetRingerModeInternal(ringerModeInternal, ringerMode2, caller, ringerModeExternal, this.mVolumePolicy);
                    }
                    setRingerModeExt(ringerMode2);
                }
            }
            android.os.Binder.restoreCallingIdentity(identity);
            boolean ringerModeFeedbackSupported = android.os.SystemProperties.getBoolean(FEATURE_RINGERMODE_FEEDBACK_SUPPORT, false);
            android.util.Log.d(TAG, "setRingerMode ringerModeFeedbackSupported = " + ringerModeFeedbackSupported);
            if (ringerModeFeedbackSupported) {
                this.mAsExt.notifyAtlasServiceRingerModeUpdate(caller, ringerMode2);
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identity);
            throw th;
        }
    }

    private void setRingerModeExt(int ringerMode) {
        synchronized (this.mSettingsLock) {
            if (ringerMode == this.mRingerModeExternal) {
                return;
            }
            this.mRingerModeExternal = ringerMode;
            broadcastRingerMode("android.media.RINGER_MODE_CHANGED", ringerMode);
        }
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:74:? -> B:68:0x0136). Please report as a decompilation issue!!! */
    private void muteRingerModeStreams() throws java.lang.Throwable {
        int numStreamTypes;
        int ringerMode;
        int numStreamTypes2;
        int ringerMode2;
        int numStreamTypes3 = android.media.AudioSystem.getNumStreamTypes();
        if (this.mNm == null) {
            this.mNm = (android.app.NotificationManager) this.mContext.getSystemService("notification");
        }
        int ringerMode3 = this.mRingerMode;
        boolean z = true;
        boolean ringerModeMute = ringerMode3 == 1 || ringerMode3 == 0;
        boolean shouldRingSco = ringerMode3 == 1 && this.mDeviceBroker.isBluetoothScoActive();
        java.lang.String eventSource = "muteRingerModeStreams() from u/pid:" + android.os.Binder.getCallingUid() + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + android.os.Binder.getCallingPid();
        sendMsg(this.mAudioHandler, 8, 2, 7, shouldRingSco ? 3 : 0, eventSource, 0);
        int streamType = numStreamTypes3 - 1;
        while (streamType >= 0) {
            boolean isMuted = isStreamMutedByRingerOrZenMode(streamType);
            boolean muteAllowedBySco = (shouldRingSco && streamType == 2) ? false : z;
            boolean shouldZenMute = isStreamAffectedByCurrentZen(streamType);
            boolean shouldMute = (shouldZenMute || (ringerModeMute && isStreamAffectedByRingerMode(streamType) && muteAllowedBySco)) ? z : false;
            if (isMuted == shouldMute) {
                numStreamTypes = numStreamTypes3;
                ringerMode = ringerMode3;
            } else if (!shouldMute) {
                if (mStreamVolumeAlias[streamType] == 2 || mStreamVolumeAlias[streamType] == 5) {
                    synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                        try {
                            com.android.server.audio.AudioService.VolumeStreamState vss = this.mStreamStates[streamType];
                            int i = 0;
                            while (i < vss.mIndexMap.size()) {
                                int device = vss.mIndexMap.keyAt(i);
                                int value = vss.mIndexMap.valueAt(i);
                                if (value != 0) {
                                    numStreamTypes2 = numStreamTypes3;
                                    ringerMode2 = ringerMode3;
                                } else {
                                    numStreamTypes2 = numStreamTypes3;
                                    ringerMode2 = ringerMode3;
                                    try {
                                        vss.setIndex(10, device, TAG, true);
                                    } catch (java.lang.Throwable th) {
                                        th = th;
                                        throw th;
                                    }
                                }
                                i++;
                                numStreamTypes3 = numStreamTypes2;
                                ringerMode3 = ringerMode2;
                            }
                            numStreamTypes = numStreamTypes3;
                            ringerMode = ringerMode3;
                            int device2 = getDeviceForStream(streamType);
                            sendMsg(this.mAudioHandler, 1, 2, device2, 0, this.mStreamStates[streamType], 500);
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                            throw th;
                        }
                    }
                } else {
                    numStreamTypes = numStreamTypes3;
                    ringerMode = ringerMode3;
                }
                sRingerAndZenModeMutedStreams &= ~(1 << streamType);
                sMuteLogger.enqueue(new com.android.server.audio.AudioServiceEvents.RingerZenMutedStreamsEvent(sRingerAndZenModeMutedStreams, "muteRingerModeStreams"));
                this.mStreamStates[streamType].mute(false, "muteRingerModeStreams");
                z = true;
            } else {
                numStreamTypes = numStreamTypes3;
                ringerMode = ringerMode3;
                sRingerAndZenModeMutedStreams |= 1 << streamType;
                sMuteLogger.enqueue(new com.android.server.audio.AudioServiceEvents.RingerZenMutedStreamsEvent(sRingerAndZenModeMutedStreams, "muteRingerModeStreams"));
                z = true;
                this.mStreamStates[streamType].mute(true, "muteRingerModeStreams");
            }
            streamType--;
            numStreamTypes3 = numStreamTypes;
            ringerMode3 = ringerMode;
        }
    }

    private boolean isAlarm(int streamType) {
        return streamType == 4;
    }

    private boolean isNotificationOrRinger(int streamType) {
        return streamType == 5 || streamType == 2;
    }

    private boolean isMedia(int streamType) {
        return streamType == 3;
    }

    private boolean isSystem(int streamType) {
        return streamType == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRingerModeInt(int ringerMode, boolean persist) {
        boolean change;
        synchronized (this.mSettingsLock) {
            change = this.mRingerMode != ringerMode;
            this.mRingerMode = ringerMode;
            muteRingerModeStreams();
        }
        if (persist) {
            sendMsg(this.mAudioHandler, 3, 0, 0, 0, null, 500);
        }
        if (change) {
            broadcastRingerMode("android.media.INTERNAL_RINGER_MODE_CHANGED_ACTION", ringerMode);
        }
    }

    void postUpdateRingerModeServiceInt() {
        sendMsg(this.mAudioHandler, 25, 2, 0, 0, null, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUpdateRingerModeServiceInt() {
        setRingerModeInt(getRingerModeInternal(), false);
    }

    public boolean shouldVibrate(int vibrateType) {
        if (!this.mHasVibrator) {
            return false;
        }
        switch (getVibrateSetting(vibrateType)) {
            case 1:
                if (getRingerModeExternal() != 0) {
                }
                break;
            case 2:
                if (getRingerModeExternal() == 1) {
                }
                break;
        }
        return false;
    }

    public int getVibrateSetting(int vibrateType) {
        if (this.mHasVibrator) {
            return (this.mVibrateSetting >> (vibrateType * 2)) & 3;
        }
        return 0;
    }

    public void setVibrateSetting(int vibrateType, int vibrateSetting) {
        if (this.mHasVibrator) {
            this.mVibrateSetting = android.media.AudioSystem.getValueForVibrateSetting(this.mVibrateSetting, vibrateType, vibrateSetting);
            broadcastVibrateSetting(vibrateType);
        }
    }

    private class SetModeDeathHandler implements android.os.IBinder.DeathRecipient {
        private final android.os.IBinder mCb;
        private final boolean mIsPrivileged;
        private int mMode;
        private final java.lang.String mPackage;
        private final int mPid;
        private final int mUid;
        private boolean mPlaybackActive = false;
        private boolean mRecordingActive = false;
        private long mUpdateTime = java.lang.System.currentTimeMillis();

        SetModeDeathHandler(android.os.IBinder cb, int pid, int uid, boolean isPrivileged, java.lang.String caller, int mode) {
            this.mMode = mode;
            this.mCb = cb;
            this.mPid = pid;
            this.mUid = uid;
            this.mPackage = caller;
            this.mIsPrivileged = isPrivileged;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.audio.AudioService.this.mDeviceBroker.mSetModeLock) {
                android.util.Log.w(com.android.server.audio.AudioService.TAG, "SetModeDeathHandler client died");
                int index = com.android.server.audio.AudioService.this.mSetModeDeathHandlers.indexOf(this);
                if (index < 0) {
                    android.util.Log.w(com.android.server.audio.AudioService.TAG, "unregistered SetModeDeathHandler client died");
                } else {
                    com.android.server.audio.AudioService.this.mSetModeDeathHandlers.get(index);
                    com.android.server.audio.AudioService.this.mSetModeDeathHandlers.remove(index);
                    com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, 36, 2, -1, android.os.Process.myPid(), com.android.server.audio.AudioService.this.mContext.getPackageName(), 0);
                }
            }
        }

        public int getPid() {
            return this.mPid;
        }

        public void setMode(int mode) {
            this.mMode = mode;
            this.mUpdateTime = java.lang.System.currentTimeMillis();
        }

        public int getMode() {
            return this.mMode;
        }

        public android.os.IBinder getBinder() {
            return this.mCb;
        }

        public int getUid() {
            return this.mUid;
        }

        public java.lang.String getPackage() {
            return this.mPackage;
        }

        public boolean isPrivileged() {
            return this.mIsPrivileged;
        }

        public long getUpdateTime() {
            return this.mUpdateTime;
        }

        public void setPlaybackActive(boolean active) {
            this.mPlaybackActive = active;
        }

        public void setRecordingActive(boolean active) {
            this.mRecordingActive = active;
        }

        public boolean isActive() {
            if (this.mIsPrivileged) {
                return true;
            }
            return (this.mMode == 3 && (this.mRecordingActive || this.mPlaybackActive)) || this.mMode == 1 || this.mMode == 4;
        }

        public void dump(java.io.PrintWriter pw, int index) {
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("MM-dd HH:mm:ss:SSS");
            if (index >= 0) {
                pw.println("  Requester # " + (index + 1) + ":");
            }
            pw.println("  - Mode: " + android.media.AudioSystem.modeToString(this.mMode));
            pw.println("  - Binder: " + this.mCb);
            pw.println("  - Pid: " + this.mPid);
            pw.println("  - Uid: " + this.mUid);
            pw.println("  - Package: " + this.mPackage);
            pw.println("  - Privileged: " + this.mIsPrivileged);
            pw.println("  - Active: " + isActive());
            pw.println("    Playback active: " + this.mPlaybackActive);
            pw.println("    Recording active: " + this.mRecordingActive);
            pw.println("  - update time: " + format.format(new java.util.Date(this.mUpdateTime)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.audio.AudioService.SetModeDeathHandler getAudioModeOwnerHandler() {
        com.android.server.audio.AudioService.SetModeDeathHandler modeOwner = null;
        com.android.server.audio.AudioService.SetModeDeathHandler privilegedModeOwner = null;
        for (com.android.server.audio.AudioService.SetModeDeathHandler h : this.mSetModeDeathHandlers) {
            if (h.isActive()) {
                if (h.isPrivileged()) {
                    if (privilegedModeOwner == null || h.getUpdateTime() > privilegedModeOwner.getUpdateTime()) {
                        privilegedModeOwner = h;
                    }
                } else if (modeOwner == null || h.getUpdateTime() > modeOwner.getUpdateTime()) {
                    modeOwner = h;
                }
            }
        }
        return privilegedModeOwner != null ? privilegedModeOwner : modeOwner;
    }

    com.android.server.audio.AudioDeviceBroker.AudioModeInfo getAudioModeOwner() {
        com.android.server.audio.AudioService.SetModeDeathHandler hdlr = getAudioModeOwnerHandler();
        if (hdlr != null) {
            return new com.android.server.audio.AudioDeviceBroker.AudioModeInfo(hdlr.getMode(), hdlr.getPid(), hdlr.getUid());
        }
        return new com.android.server.audio.AudioDeviceBroker.AudioModeInfo(0, 0, 0);
    }

    int getModeOwnerUid() {
        com.android.server.audio.AudioService.SetModeDeathHandler hdlr = getAudioModeOwnerHandler();
        if (hdlr != null) {
            return hdlr.getUid();
        }
        return 0;
    }

    public void setMode(int mode, android.os.IBinder cb, java.lang.String callingPackage) throws android.os.RemoteException {
        int pid = android.os.Binder.getCallingPid();
        int uid = android.os.Binder.getCallingUid();
        if (this.mAsExt.isGameModeSwitchOpen() && this.mAsExt.oplusIsInBinderOptList(callingPackage)) {
            android.util.Log.d(TAG, "begin to send setgamemode message");
            android.os.Bundle modeParameters = new android.os.Bundle();
            modeParameters.putInt(com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY, mode);
            modeParameters.putBoolean("flag", true);
            modeParameters.putString("packagename", callingPackage);
            modeParameters.putBinder("binder", cb);
            sendBundleMsg(this.mAudioHandler, 114, 2, pid, uid, null, modeParameters, 0);
            return;
        }
        setOriginalMode(mode, cb, callingPackage, pid, uid, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:141:0x012c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void setOriginalMode(int r28, android.os.IBinder r29, java.lang.String r30, int r31, int r32, boolean r33) throws android.os.RemoteException {
        /*
            Method dump skipped, instruction units count: 742
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.AudioService.setOriginalMode(int, android.os.IBinder, java.lang.String, int, int, boolean):void");
    }

    void onUpdateAudioMode(int requestedMode, int requesterPid, java.lang.String requesterPackage, boolean force) throws java.lang.Throwable {
        int requestedMode2;
        int mode;
        int uid;
        int pid;
        if (requestedMode != -1) {
            requestedMode2 = requestedMode;
        } else {
            requestedMode2 = getMode();
        }
        com.android.server.audio.AudioService.SetModeDeathHandler currentModeHandler = getAudioModeOwnerHandler();
        if (currentModeHandler == null) {
            mode = 0;
            uid = 0;
            pid = 0;
        } else {
            int mode2 = currentModeHandler.getMode();
            int uid2 = currentModeHandler.getUid();
            int pid2 = currentModeHandler.getPid();
            currentModeHandler.getBinder();
            mode = mode2;
            uid = uid2;
            pid = pid2;
        }
        if (DEBUG_MODE) {
            android.util.Log.v(TAG, "onUpdateAudioMode() new mode: " + mode + ", current mode: " + this.mMode.get() + " requested mode: " + requestedMode2);
        }
        this.mAsExt.routeCheckForModeChange(requesterPid, requestedMode2, requesterPackage);
        if (mode != this.mMode.get() || force) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                int status = this.mAudioSystem.setPhoneState(mode, uid);
                android.os.Binder.restoreCallingIdentity(identity);
                if (status == 0) {
                    if (DEBUG_MODE) {
                        android.util.Log.v(TAG, "onUpdateAudioMode: mode successfully set to " + mode);
                    }
                    if (mode == 1 && this.mMode.get() == 2 && this.mAsExt.getDownlinkMuteStatus() == 1) {
                        android.media.AudioSystem.setParameters(OPLUS_VC_DOWNLINK_MUTE_MODE);
                    }
                    if (this.mAsExt.getPrivacyCallSupport() && mode == 0) {
                        android.util.Log.d(TAG, "close privacy call");
                        this.mAsExt.oplusClosePrivacyCall();
                        this.mAsExt.resetDownlinkMuteStatus();
                    }
                    if (mode == 2 && this.mAsExt.getPrivacyCallSoftwareModeSupport() && this.mAsExt.getPrivacyCallSoftwareModeSettingEnable()) {
                        this.mAsExt.setPrivacyCallSoftwareModeOn(1, false);
                    }
                    if (this.mAsExt.getVocalProminenceSupport() && mode == 0) {
                        android.util.Log.d(TAG, "close VocalProminence");
                        this.mAsExt.oplusSetVocalProminence(0, 0, false);
                        this.mAsExt.resetDownlinkMuteStatus();
                    }
                    sendMsg(this.mAudioHandler, 40, 0, mode, 0, null, 0);
                    int previousMode = this.mMode.getAndSet(mode);
                    int pid3 = pid;
                    int uid3 = uid;
                    int uid4 = mode;
                    this.mModeLogger.enqueue(new com.android.server.audio.AudioServiceEvents.PhoneStateEvent(requesterPackage, requesterPid, requestedMode2, pid, uid4));
                    int streamType = getActiveStreamType(Integer.MIN_VALUE);
                    int device = getDeviceForStream(streamType);
                    int streamAlias = mStreamVolumeAlias[streamType];
                    if (this.mAsExt.getVocalProminenceSupport() && (mode == 3 || mode == 2)) {
                        int mVpDevice = getDeviceForStream(0);
                        sendMsg(this.mAudioHandler, 116, 0, 0, mVpDevice, null, 500);
                    }
                    if (DEBUG_MODE) {
                        android.util.Log.v(TAG, "onUpdateAudioMode: streamType=" + streamType + ", streamAlias=" + streamAlias);
                    }
                    int index = this.mStreamStates[streamAlias].getIndex(device);
                    int maxIndex = this.mStreamStates[streamAlias].getMaxIndex();
                    int requestedMode3 = mode;
                    setStreamVolumeInt(streamAlias, index, device, true, requesterPackage, true);
                    updateStreamVolumeAlias(true, requesterPackage);
                    updateAbsVolumeMultiModeDevices(previousMode, requestedMode3);
                    setLeAudioVolumeOnModeUpdate(requestedMode3, device, streamAlias, index, maxIndex);
                    synchronized (this.mCachedAbsVolDrivingStreamsLock) {
                        try {
                            this.mCachedAbsVolDrivingStreams.replaceAll(new java.util.function.BiFunction() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda9
                                @Override // java.util.function.BiFunction
                                public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                                    return this.f$0.lambda$onUpdateAudioMode$13((java.lang.Integer) obj, (java.lang.Integer) obj2);
                                }
                            });
                        } catch (java.lang.Throwable th) {
                            th = th;
                            while (true) {
                                try {
                                    throw th;
                                } catch (java.lang.Throwable th2) {
                                    th = th2;
                                }
                            }
                        }
                    }
                    this.mDeviceBroker.postSetModeOwner(requestedMode3, pid3, uid3);
                    this.mAsExt.updateModeOwnerInfo(pid3, uid3, requestedMode3);
                    if (mBRStateOfCamera && requestedMode3 == 0) {
                        setBinauralRecordParameters(true);
                    }
                    if (this.mDeviceBroker.isHoloVoipSupport()) {
                        checkStatusForHoloVoip(requestedMode3);
                    }
                } else {
                    android.util.Log.w(TAG, "onUpdateAudioMode: failed to set audio mode to: " + mode);
                }
                this.mAsExt.notifyAtlasServiceModesUpdate(false);
                return;
            } catch (java.lang.Throwable th3) {
                android.os.Binder.restoreCallingIdentity(identity);
                throw th3;
            }
        }
        this.mAsExt.notifyAtlasServiceModesUpdate(true);
        if (mode != 0) {
            this.mDeviceBroker.postSetModeOwner(mode, pid, uid);
            this.mAsExt.updateModeOwnerInfo(pid, uid, mode);
        }
        if (this.mDeviceBroker.isHoloVoipSupport()) {
            checkStatusForHoloVoip(mode);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$onUpdateAudioMode$13(java.lang.Integer absDev, java.lang.Integer stream) {
        int streamToDriveAbs = getBluetoothContextualVolumeStream();
        if (stream.intValue() != streamToDriveAbs) {
            this.mAudioSystem.setDeviceAbsoluteVolumeEnabled(absDev.intValue(), "", true, streamToDriveAbs);
        }
        return java.lang.Integer.valueOf(streamToDriveAbs);
    }

    private void checkStatusForHoloVoip(int mode) {
        if ((mode == 2 || mode == 3) && this.mDeviceBroker.isHoloBLeDeviceConnected()) {
            sendMsg(this.mAudioHandler, 118, 0, 0, 0, null, 1000);
        }
    }

    public int getMode() {
        int uid = android.os.Binder.getCallingUid();
        java.lang.String callingApp = this.mContext.getPackageManager().getNameForUid(uid);
        android.util.Log.d(TAG, "uid " + uid + ", callingApp = " + callingApp);
        if (this.mAsExt.isGameModeSwitchOpen() && this.mAsExt.oplusIsInBinderOptList(callingApp) && this.mLatestSetModeUid != uid) {
            return this.mPendingMode;
        }
        synchronized (this.mDeviceBroker.mSetModeLock) {
            com.android.server.audio.AudioService.SetModeDeathHandler currentModeHandler = getAudioModeOwnerHandler();
            if (currentModeHandler == null) {
                return 0;
            }
            return currentModeHandler.getMode();
        }
    }

    public boolean isCallScreeningModeSupported() {
        return this.mIsCallScreeningModeSupported;
    }

    protected void dispatchMode(int mode) {
        int nbDispatchers = this.mModeDispatchers.beginBroadcast();
        for (int i = 0; i < nbDispatchers; i++) {
            try {
                this.mModeDispatchers.getBroadcastItem(i).dispatchAudioModeChanged(mode);
            } catch (android.os.RemoteException e) {
            }
        }
        this.mModeDispatchers.finishBroadcast();
    }

    protected void preDispatchMode(int mode) {
        this.mAsExt.preDispatchMode(mode);
    }

    public void registerModeDispatcher(android.media.IAudioModeDispatcher dispatcher) {
        this.mModeDispatchers.register(dispatcher);
    }

    public void oplusRegisterModeDispatcher(android.media.IAudioModeDispatcher dispatcher) {
        this.mModeDispatchers.register(dispatcher);
        this.mAsExt.oplusRegisterModeDispatcher(dispatcher);
    }

    public void unregisterModeDispatcher(android.media.IAudioModeDispatcher dispatcher) {
        this.mModeDispatchers.unregister(dispatcher);
        this.mAsExt.unregisterModeDispatcher(dispatcher);
    }

    public boolean isPstnCallAudioInterceptable() {
        super.isPstnCallAudioInterceptable_enforcePermission();
        boolean uplinkDeviceFound = false;
        boolean downlinkDeviceFound = false;
        android.media.AudioDeviceInfo[] devices = android.media.AudioManager.getDevicesStatic(3);
        for (android.media.AudioDeviceInfo device : devices) {
            if (device.getInternalType() == 65536) {
                uplinkDeviceFound = true;
            } else if (device.getInternalType() == -2147483584) {
                downlinkDeviceFound = true;
            }
            if (uplinkDeviceFound && downlinkDeviceFound) {
                return true;
            }
        }
        return false;
    }

    public void setRttEnabled(boolean rttEnabled) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") != 0) {
            android.util.Log.w(TAG, "MODIFY_PHONE_STATE Permission Denial: setRttEnabled from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid());
            return;
        }
        synchronized (this.mSettingsLock) {
            this.mRttEnabled = rttEnabled;
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                android.media.AudioSystem.setRttEnabled(rttEnabled);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public void adjustSuggestedStreamVolumeForUid(int streamType, int direction, int flags, java.lang.String packageName, int uid, int pid, android.os.UserHandle userHandle, int targetSdkVersion) throws java.lang.Throwable {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("Should only be called from system process");
        }
        adjustSuggestedStreamVolume(direction, streamType, flags, packageName, packageName, uid, pid, hasAudioSettingsPermission(uid, pid), 0);
    }

    public void adjustStreamVolumeForUid(int streamType, int direction, int flags, java.lang.String packageName, int uid, int pid, android.os.UserHandle userHandle, int targetSdkVersion) throws java.lang.Throwable {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("Should only be called from system process");
        }
        if (direction != 0) {
            sVolumeLogger.enqueue(new com.android.server.audio.AudioServiceEvents.VolumeEvent(5, streamType, direction, flags, packageName + " uid:" + uid));
        }
        adjustStreamVolume(streamType, direction, flags, packageName, packageName, uid, pid, null, hasAudioSettingsPermission(uid, pid), 0);
    }

    public void adjustVolume(int direction, int flags) {
        if (this.mHardeningEnforcer.blockVolumeMethod(101)) {
            return;
        }
        getMediaSessionManager().dispatchAdjustVolume(Integer.MIN_VALUE, direction, flags);
    }

    public void adjustSuggestedStreamVolume(int direction, int suggestedStreamType, int flags) {
        if (this.mHardeningEnforcer.blockVolumeMethod(102)) {
            return;
        }
        getMediaSessionManager().dispatchAdjustVolume(suggestedStreamType, direction, flags);
    }

    public void setStreamVolumeForUid(int streamType, int index, int flags, java.lang.String packageName, int uid, int pid, android.os.UserHandle userHandle, int targetSdkVersion) {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("Should only be called from system process");
        }
        setStreamVolume(streamType, index, flags, null, packageName, packageName, null, uid, hasAudioSettingsPermission(uid, pid), true);
    }

    private static final class LoadSoundEffectReply implements com.android.server.audio.SoundEffectsHelper.OnEffectsLoadCompleteHandler {
        private static final int SOUND_EFFECTS_ERROR = -1;
        private static final int SOUND_EFFECTS_LOADED = 0;
        private static final int SOUND_EFFECTS_LOADING = 1;
        private static final int SOUND_EFFECTS_LOAD_TIMEOUT_MS = 5000;
        private int mStatus;

        private LoadSoundEffectReply() {
            this.mStatus = 1;
        }

        @Override // com.android.server.audio.SoundEffectsHelper.OnEffectsLoadCompleteHandler
        public synchronized void run(boolean success) {
            this.mStatus = success ? 0 : -1;
            notify();
        }

        public synchronized boolean waitForLoaded(int attempts) {
            int attempts2;
            while (true) {
                if (this.mStatus != 1) {
                    break;
                }
                int attempts3 = attempts - 1;
                if (attempts <= 0) {
                    break;
                }
                try {
                    wait(5000L);
                } catch (java.lang.InterruptedException e) {
                    android.util.Log.w(com.android.server.audio.AudioService.TAG, "Interrupted while waiting sound pool loaded.");
                }
                attempts = attempts3;
            }
            attempts2 = this.mStatus;
            return attempts2 == 0;
        }
    }

    public void playSoundEffect(int effectType, int userId) {
        if (querySoundEffectsEnabled(userId)) {
            playSoundEffectVolume(effectType, -1.0f);
        }
    }

    private boolean querySoundEffectsEnabled(int user) {
        return this.mSettings.getSystemIntForUser(getContentResolver(), "sound_effects_enabled", 0, user) != 0;
    }

    public void playSoundEffectVolume(int effectType, float volume) {
        if (isStreamMute(1)) {
            return;
        }
        if (effectType >= 16 || effectType < 0) {
            android.util.Log.w(TAG, "AudioService effectType value " + effectType + " out of range");
        } else {
            sendMsg(this.mAudioHandler, 5, 2, effectType, (int) (1000.0f * volume), null, 0);
        }
    }

    public boolean loadSoundEffects() {
        com.android.server.audio.AudioService.LoadSoundEffectReply reply = new com.android.server.audio.AudioService.LoadSoundEffectReply();
        sendMsg(this.mAudioHandler, 7, 2, 0, 0, reply, 0);
        return reply.waitForLoaded(3);
    }

    protected void scheduleLoadSoundEffects() {
        sendMsg(this.mAudioHandler, 7, 2, 0, 0, null, 0);
    }

    public void unloadSoundEffects() {
        sendMsg(this.mAudioHandler, 15, 2, 0, 0, null, 0);
    }

    public void reloadAudioSettings() {
        readAudioSettings(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readAudioSettings(boolean userSwitch) {
        readPersistedSettings();
        readUserRestrictions();
        int numStreamTypes = android.media.AudioSystem.getNumStreamTypes();
        for (int streamType = 0; streamType < numStreamTypes; streamType++) {
            com.android.server.audio.AudioService.VolumeStreamState streamState = this.mStreamStates[streamType];
            streamState.readSettings();
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                if (streamState.mIsMuted && ((!isStreamAffectedByMute(streamType) && !isStreamMutedByRingerOrZenMode(streamType)) || this.mUseFixedVolume)) {
                    streamState.mIsMuted = false;
                }
            }
        }
        readVolumeGroupsSettings(userSwitch);
        setRingerModeInt(getRingerModeInternal(), false);
        checkAllFixedVolumeDevices();
        checkAllAliasStreamVolumes();
        checkMuteAffectedStreams();
        this.mSoundDoseHelper.restoreMusicActiveMs();
        this.mSoundDoseHelper.enforceSafeMediaVolumeIfActive(TAG);
        if (userSwitch) {
            this.mSoundDoseHelper.updateCurrentUserInfo(getCurrentUserId());
        }
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "Restoring device volume behavior");
        }
        restoreDeviceVolumeBehavior();
        if (this.mAsExt.isSuperVolumeSupported()) {
            this.mAsExt.readPersistSuperVolume();
        }
    }

    public int[] getAvailableCommunicationDeviceIds() {
        java.util.List<android.media.AudioDeviceInfo> commDevices = com.android.server.audio.AudioDeviceBroker.getAvailableCommunicationDevices();
        return commDevices.stream().mapToInt(new java.util.function.ToIntFunction() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda4
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(java.lang.Object obj) {
                return ((android.media.AudioDeviceInfo) obj).getId();
            }
        }).toArray();
    }

    /* JADX WARN: Removed duplicated region for block: B:79:0x018e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean setCommunicationDevice(android.os.IBinder r23, int r24) {
        /*
            Method dump skipped, instruction units count: 439
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.AudioService.setCommunicationDevice(android.os.IBinder, int):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0034 A[Catch: all -> 0x0049, TryCatch #0 {all -> 0x0049, blocks: (B:4:0x0016, B:6:0x001e, B:8:0x0026, B:10:0x002c, B:13:0x003d, B:11:0x0034), top: B:21:0x0016 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int getCommunicationDevice() {
        /*
            r6 = this;
            r0 = 0
            r1 = 0
            android.content.Context r2 = r6.mContext
            android.content.pm.PackageManager r2 = r2.getPackageManager()
            int r3 = android.os.Binder.getCallingUid()
            java.lang.String r2 = r2.getNameForUid(r3)
            long r3 = android.os.Binder.clearCallingIdentity()
            if (r2 == 0) goto L34
            java.lang.String r5 = "com.android.server.telecom"
            boolean r5 = r2.equals(r5)     // Catch: java.lang.Throwable -> L49
            if (r5 != 0) goto L26
            java.lang.String r5 = "android.uid.system"
            boolean r5 = r2.contains(r5)     // Catch: java.lang.Throwable -> L49
            if (r5 == 0) goto L34
        L26:
            boolean r5 = r6.isVendorBeforeAndroidU()     // Catch: java.lang.Throwable -> L49
            if (r5 == 0) goto L34
            com.android.server.audio.AudioDeviceBroker r5 = r6.mDeviceBroker     // Catch: java.lang.Throwable -> L49
            android.media.AudioDeviceInfo r5 = r5.getCommunicationDeviceForClient()     // Catch: java.lang.Throwable -> L49
            r0 = r5
            goto L3b
        L34:
            com.android.server.audio.AudioDeviceBroker r5 = r6.mDeviceBroker     // Catch: java.lang.Throwable -> L49
            android.media.AudioDeviceInfo r5 = r5.getCommunicationDevice()     // Catch: java.lang.Throwable -> L49
            r0 = r5
        L3b:
            if (r0 == 0) goto L42
            int r5 = r0.getId()     // Catch: java.lang.Throwable -> L49
            goto L43
        L42:
            r5 = 0
        L43:
            r1 = r5
            android.os.Binder.restoreCallingIdentity(r3)
            return r1
        L49:
            r5 = move-exception
            android.os.Binder.restoreCallingIdentity(r3)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.AudioService.getCommunicationDevice():int");
    }

    public void registerCommunicationDeviceDispatcher(android.media.ICommunicationDeviceDispatcher dispatcher) {
        if (dispatcher == null) {
            return;
        }
        this.mDeviceBroker.registerCommunicationDeviceDispatcher(dispatcher);
    }

    public void unregisterCommunicationDeviceDispatcher(android.media.ICommunicationDeviceDispatcher dispatcher) {
        if (dispatcher == null) {
            return;
        }
        this.mDeviceBroker.unregisterCommunicationDeviceDispatcher(dispatcher);
    }

    public void setSpeakerphoneOn(android.os.IBinder cb, boolean on) {
        if (!checkAudioSettingsPermission("setSpeakerphoneOn()")) {
            return;
        }
        boolean isPrivileged = this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") == 0;
        int uid = android.os.Binder.getCallingUid();
        int pid = android.os.Binder.getCallingPid();
        if (this.mAsExt.isAudioRouteSupported() && !this.mAsExt.manageRouteSettings(uid, pid, on)) {
            return;
        }
        if (on && isVendorBeforeAndroidU() && this.mDeviceBroker.isBluetoothLeAudioRequested()) {
            this.mAsSocExt.setCommunicationDeviceExt(cb, pid, null, "setSpeakerphoneOn");
        }
        java.lang.String eventSource = "setSpeakerphoneOn(" + on + ") from u/pid:" + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + pid;
        android.util.Log.i(TAG, "In setSpeakerphoneOn(), on: " + on + ", eventSource: " + eventSource);
        new android.media.MediaMetrics.Item("audio.device.setSpeakerphoneOn").setUid(uid).setPid(pid).set(android.media.MediaMetrics.Property.STATE, on ? kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_ON : kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_OFF).record();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mDeviceBroker.setSpeakerphoneOn(cb, uid, on, isPrivileged, eventSource);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public boolean isSpeakerphoneOn() {
        return this.mDeviceBroker.isSpeakerphoneOn();
    }

    void resetBluetoothScoOfApp() {
        this.mBtScoOnByApp = false;
    }

    public void setBluetoothScoOn(boolean on) {
        if (!checkAudioSettingsPermission("setBluetoothScoOn()")) {
            return;
        }
        if (isVendorBeforeAndroidU() && this.mAsSocExt.isBluetoothLeTbsDeviceActive()) {
            this.mBtScoOnByApp = false;
            return;
        }
        if (android.os.UserHandle.getCallingAppId() >= 10000) {
            android.util.Log.i(TAG, "In setBluetoothScoOn(), on: " + on + ". The calling application Uid: " + android.os.Binder.getCallingUid() + ", is greater than FIRST_APPLICATION_UID exiting from setBluetoothScoOn()");
            this.mBtScoOnByApp = on;
            return;
        }
        int uid = android.os.Binder.getCallingUid();
        int pid = android.os.Binder.getCallingPid();
        java.lang.String eventSource = "setBluetoothScoOn(" + on + ") from u/pid:" + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + pid;
        android.util.Log.i(TAG, "In setBluetoothScoOn(), eventSource: " + eventSource);
        new android.media.MediaMetrics.Item("audio.device.setBluetoothScoOn").setUid(uid).setPid(pid).set(android.media.MediaMetrics.Property.STATE, on ? kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_ON : kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_OFF).record();
        if (DEBUG_MODE) {
            java.lang.String callingApp = this.mContext.getPackageManager().getNameForUid(android.os.Binder.getCallingUid());
            android.util.Log.d(TAG, eventSource + ", callingApp=" + callingApp);
        }
        this.mDeviceBroker.setBluetoothScoOn(on, eventSource);
    }

    public void setA2dpSuspended(boolean enable) {
        super.setA2dpSuspended_enforcePermission();
        java.lang.String eventSource = "setA2dpSuspended(" + enable + ") from u/pid:" + android.os.Binder.getCallingUid() + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + android.os.Binder.getCallingPid();
        this.mDeviceBroker.setA2dpSuspended(enable, false, eventSource);
    }

    public void setLeAudioSuspended(boolean enable) {
        super.setLeAudioSuspended_enforcePermission();
        java.lang.String eventSource = "setLeAudioSuspended(" + enable + ") from u/pid:" + android.os.Binder.getCallingUid() + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + android.os.Binder.getCallingPid();
        this.mDeviceBroker.setLeAudioSuspended(enable, false, eventSource);
    }

    public boolean isBluetoothScoOn() {
        if (android.os.Build.isMtkPlatform() && this.mAsSocExt.isBluetoothLeTbsDeviceActive()) {
            boolean mBleCSstatus = this.mAsSocExt.isBluetoothLeCgOn();
            if (DEBUG_MODE) {
                android.util.Log.d(TAG, "isBluetoothScoOn mBleCSstatus:" + mBleCSstatus);
            }
            if (mBleCSstatus) {
                return mBleCSstatus;
            }
        }
        int uid = android.os.Binder.getCallingUid();
        android.os.Binder.getCallingPid();
        boolean hasScoClient = true;
        if (this.mAsExt.needJudgeScoActualy(uid) && !this.mDeviceBroker.hasCommunicationRouteClientForUid(uid, 7)) {
            hasScoClient = false;
            android.util.Log.d(TAG, "judge this interface to create sco hasScoClient:false");
        }
        return (this.mBtScoOnByApp && hasScoClient) || this.mDeviceBroker.isBluetoothScoOn();
    }

    public void setBinauralRecordParameters(boolean status) {
        if (mBRStateOfCamera) {
            if (!status) {
                sendMsg(this.mAudioHandler, 80, 0, 0, 0, null, 0);
            } else if (status && this.mMode.get() == 0 && !this.mDeviceBroker.isBluetoothScoOn()) {
                sendMsg(this.mAudioHandler, 81, 0, 0, 0, null, 0);
            }
        }
    }

    public void setBluetoothA2dpOn(boolean on) {
        if (!checkAudioSettingsPermission("setBluetoothA2dpOn()")) {
            return;
        }
        int uid = android.os.Binder.getCallingUid();
        int pid = android.os.Binder.getCallingPid();
        java.lang.String eventSource = "setBluetoothA2dpOn(" + on + ") from u/pid:" + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + pid;
        new android.media.MediaMetrics.Item("audio.device.setBluetoothA2dpOn").setUid(uid).setPid(pid).set(android.media.MediaMetrics.Property.STATE, on ? kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_ON : kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_OFF).record();
        if (DEBUG_MODE) {
            java.lang.String callingApp = this.mContext.getPackageManager().getNameForUid(android.os.Binder.getCallingUid());
            android.util.Log.d(TAG, eventSource + ", callingApp=" + callingApp);
        }
        this.mDeviceBroker.setBluetoothA2dpOn_Async(on, eventSource);
    }

    public boolean isBluetoothA2dpOn() {
        return this.mDeviceBroker.isBluetoothA2dpOn();
    }

    public void startBluetoothSco(android.os.IBinder cb, int targetSdkVersion) {
        android.util.Log.i(TAG, "In startBluetoothSco()");
        android.bluetooth.BluetoothAdapter adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && adapter.getState() == 12) {
            if (!checkAudioSettingsPermission("startBluetoothSco()")) {
                return;
            }
            int uid = android.os.Binder.getCallingUid();
            int pid = android.os.Binder.getCallingPid();
            if (this.mAsExt.isAudioRouteSupported() && !this.mAsExt.manageRouteSettings(uid, pid, true)) {
                return;
            }
            int scoAudioMode = targetSdkVersion < 18 ? 0 : -1;
            java.lang.String eventSource = "startBluetoothSco()) from u/pid:" + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + pid;
            new android.media.MediaMetrics.Item("audio.bluetooth").setUid(uid).setPid(pid).set(android.media.MediaMetrics.Property.EVENT, "startBluetoothSco").set(android.media.MediaMetrics.Property.SCO_AUDIO_MODE, com.android.server.audio.BtHelper.scoAudioModeToString(scoAudioMode)).record();
            this.mAsExt.rejectBluetoothSco(uid, pid);
            if (DEBUG_MODE) {
                java.lang.String callingApp = this.mContext.getPackageManager().getNameForUid(android.os.Binder.getCallingUid());
                android.util.Log.d(TAG, eventSource + ", callingApp=" + callingApp + ", targetSdkVersion=" + targetSdkVersion);
            }
            if (!this.mAsExt.isAudioRouteSupported()) {
                this.mAsExt.setBluetoothScoSpecialUid(this.mContext.getPackageManager().getNameForUid(android.os.Binder.getCallingUid()), uid);
            }
            if (android.os.Build.isMtkPlatform() && this.mAsSocExt.isBluetoothLeTbsDeviceActive()) {
                if (isVendorBeforeAndroidU()) {
                    this.mAsSocExt.startBluetoothLeCg(cb, targetSdkVersion);
                    return;
                }
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    boolean isPrivileged = this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") == 0;
                    this.mDeviceBroker.startBluetoothBleForClient(cb, uid, scoAudioMode, isPrivileged, eventSource);
                    return;
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
            startBluetoothScoInt(cb, uid, scoAudioMode, eventSource);
            return;
        }
        android.util.Log.i(TAG, "startBluetoothSco(), BT is not turned ON or adapter is null");
    }

    public void startBluetoothScoVirtualCall(android.os.IBinder cb) {
        android.util.Log.i(TAG, "In startBluetoothScoVirtualCall()");
        if (!checkAudioSettingsPermission("startBluetoothScoVirtualCall()")) {
            return;
        }
        int uid = android.os.Binder.getCallingUid();
        int pid = android.os.Binder.getCallingPid();
        java.lang.String eventSource = "startBluetoothScoVirtualCall()) from u/pid:" + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + pid;
        new android.media.MediaMetrics.Item("audio.bluetooth").setUid(uid).setPid(pid).set(android.media.MediaMetrics.Property.EVENT, "startBluetoothScoVirtualCall").set(android.media.MediaMetrics.Property.SCO_AUDIO_MODE, com.android.server.audio.BtHelper.scoAudioModeToString(0)).record();
        if (DEBUG_MODE) {
            java.lang.String callingApp = this.mContext.getPackageManager().getNameForUid(android.os.Binder.getCallingUid());
            android.util.Log.d(TAG, eventSource + ", callingApp=" + callingApp);
        }
        startBluetoothScoInt(cb, uid, 0, eventSource);
    }

    void startBluetoothScoInt(android.os.IBinder cb, int uid, int scoAudioMode, java.lang.String eventSource) {
        android.util.Log.i(TAG, "In startBluetoothScoInt(), scoAudioMode: " + scoAudioMode);
        android.media.MediaMetrics.Item mmi = new android.media.MediaMetrics.Item("audio.bluetooth").set(android.media.MediaMetrics.Property.EVENT, "startBluetoothScoInt").set(android.media.MediaMetrics.Property.SCO_AUDIO_MODE, com.android.server.audio.BtHelper.scoAudioModeToString(scoAudioMode));
        if (!checkAudioSettingsPermission("startBluetoothSco()") || !this.mSystemReady) {
            mmi.set(android.media.MediaMetrics.Property.EARLY_RETURN, "permission or systemReady").record();
            return;
        }
        boolean isPrivileged = this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") == 0;
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mDeviceBroker.startBluetoothScoForClient(cb, uid, scoAudioMode, isPrivileged, eventSource);
            android.os.Binder.restoreCallingIdentity(ident);
            mmi.record();
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
    }

    public void stopBluetoothSco(android.os.IBinder cb) {
        long ident;
        android.util.Log.i(TAG, "In stopBluetoothSco()");
        if (!checkAudioSettingsPermission("stopBluetoothSco()") || !this.mSystemReady) {
            return;
        }
        int uid = android.os.Binder.getCallingUid();
        int pid = android.os.Binder.getCallingPid();
        java.lang.String eventSource = "stopBluetoothSco()) from u/pid:" + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + pid;
        boolean isPrivileged = this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") == 0;
        if (DEBUG_MODE) {
            java.lang.String callingApp = this.mContext.getPackageManager().getNameForUid(android.os.Binder.getCallingUid());
            android.util.Log.d(TAG, eventSource + ", callingApp=" + callingApp);
        }
        if (android.os.Build.isMtkPlatform()) {
            if (isVendorBeforeAndroidU()) {
                boolean mCGstatus = this.mAsSocExt.stopBluetoothLeCg(cb);
                if (DEBUG_MODE) {
                    android.util.Log.d(TAG, "stopBluetoothSco, mCGstatus: " + mCGstatus);
                }
                if (mCGstatus) {
                    return;
                }
            } else {
                ident = android.os.Binder.clearCallingIdentity();
                try {
                    this.mDeviceBroker.stopBluetoothBleForClient(cb, uid, isPrivileged, eventSource);
                } finally {
                }
            }
        }
        ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mDeviceBroker.stopBluetoothScoForClient(cb, uid, isPrivileged, eventSource);
            android.os.Binder.restoreCallingIdentity(ident);
            new android.media.MediaMetrics.Item("audio.bluetooth").setUid(uid).setPid(pid).set(android.media.MediaMetrics.Property.EVENT, "stopBluetoothSco").set(android.media.MediaMetrics.Property.SCO_AUDIO_MODE, com.android.server.audio.BtHelper.scoAudioModeToString(-1)).record();
        } finally {
        }
    }

    android.content.ContentResolver getContentResolver() {
        return this.mContentResolver;
    }

    public com.android.server.audio.SettingsAdapter getSettings() {
        return this.mSettings;
    }

    private int checkForRingerModeChange(int oldIndex, int direction, int step, boolean isMuted, java.lang.String caller, int flags) {
        int result = 1;
        if (isPlatformTelevision() || this.mIsSingleVolume) {
            return 1;
        }
        int ringerMode = getRingerModeInternal();
        switch (ringerMode) {
            case 0:
                if (direction == 1 || direction == 101 || direction == 100) {
                    if (!this.mVolumePolicy.volumeUpToExitSilent) {
                        result = 1 | 128;
                    } else {
                        ringerMode = (this.mHasVibrator && direction == 1) ? 1 : 2;
                    }
                }
                result &= -2;
                break;
            case 1:
                if (!this.mHasVibrator) {
                    android.util.Log.e(TAG, "checkForRingerModeChange() current ringer mode is vibratebut no vibrator is present");
                } else {
                    if (direction == -1) {
                        if (this.mPrevVolDirection != -1) {
                            if (this.mVolumePolicy.volumeDownToEnterSilent) {
                                long diff = android.os.SystemClock.uptimeMillis() - this.mLoweredFromNormalToVibrateTime;
                                if (diff > this.mVolumePolicy.vibrateToSilentDebounce && this.mRingerModeDelegate.canVolumeDownEnterSilent()) {
                                    ringerMode = 0;
                                }
                            } else {
                                result = 1 | 2048;
                            }
                        }
                    } else if (direction == 1 || direction == 101 || direction == 100) {
                        ringerMode = 2;
                    }
                    result &= -2;
                }
                break;
            case 2:
                if (direction == -1) {
                    if (this.mHasVibrator) {
                        if (step <= oldIndex && oldIndex < step * 2) {
                            ringerMode = 1;
                            this.mLoweredFromNormalToVibrateTime = android.os.SystemClock.uptimeMillis();
                        }
                    } else if (oldIndex == step && this.mVolumePolicy.volumeDownToEnterSilent) {
                        ringerMode = 0;
                    }
                }
                break;
            default:
                android.util.Log.e(TAG, "checkForRingerModeChange() wrong ringer mode: " + ringerMode);
                break;
        }
        if (isAndroidNPlus(caller) && wouldToggleZenMode(ringerMode) && !this.mNm.isNotificationPolicyAccessGrantedForPackage(caller) && (flags & 4096) == 0) {
            throw new java.lang.SecurityException("Not allowed to change Do Not Disturb state");
        }
        setRingerMode(ringerMode, "AS.AudioService.checkForRingerModeChange", false);
        this.mPrevVolDirection = direction;
        return result;
    }

    public boolean isStreamAffectedByRingerMode(int streamType) {
        return (this.mRingerModeAffectedStreams & (1 << streamType)) != 0;
    }

    public boolean isStreamAffectedByCurrentZen(int streamType) {
        return (this.mZenModeAffectedStreams & (1 << streamType)) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isStreamMutedByRingerOrZenMode(int streamType) {
        return (sRingerAndZenModeMutedStreams & (1 << streamType)) != 0;
    }

    private boolean updateZenModeAffectedStreams() {
        if (!this.mSystemReady) {
            return false;
        }
        int zenModeAffectedStreams = 0;
        int zenMode = this.mNm.getZenMode();
        if (zenMode == 2) {
            zenModeAffectedStreams = 0 | 2 | 32 | 4 | 16 | 8;
        } else if (zenMode == 3) {
            zenModeAffectedStreams = 0 | 2 | 32 | 4;
        } else if (zenMode == 1) {
            android.app.NotificationManager.Policy zenPolicy = this.mNm.getConsolidatedNotificationPolicy();
            if ((zenPolicy.priorityCategories & 32) == 0) {
                zenModeAffectedStreams = 0 | 16;
            }
            if ((zenPolicy.priorityCategories & 64) == 0) {
                zenModeAffectedStreams |= 8;
            }
            if ((zenPolicy.priorityCategories & 128) == 0) {
                zenModeAffectedStreams |= 2;
            }
            if (android.service.notification.ZenModeConfig.areAllPriorityOnlyRingerSoundsMuted(zenPolicy)) {
                zenModeAffectedStreams = zenModeAffectedStreams | 32 | 4;
            }
        }
        if (this.mZenModeAffectedStreams == zenModeAffectedStreams) {
            return false;
        }
        this.mZenModeAffectedStreams = zenModeAffectedStreams;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updateRingerAndZenModeAffectedStreams() {
        int ringerModeAffectedStreams;
        int ringerModeAffectedStreams2;
        boolean updatedZenModeAffectedStreams = updateZenModeAffectedStreams();
        int ringerModeAffectedStreams3 = this.mSettings.getSystemIntForUser(this.mContentResolver, "mode_ringer_streams_affected", 166, -2);
        if (this.mIsSingleVolume) {
            ringerModeAffectedStreams3 = 0;
        } else if (this.mRingerModeDelegate != null) {
            ringerModeAffectedStreams3 = this.mRingerModeDelegate.getRingerModeAffectedStreams(ringerModeAffectedStreams3);
        }
        if (this.mCameraSoundForced) {
            ringerModeAffectedStreams = ringerModeAffectedStreams3 & (-129);
        } else {
            ringerModeAffectedStreams = ringerModeAffectedStreams3 | 128;
        }
        if (mStreamVolumeAlias[8] == 2) {
            ringerModeAffectedStreams2 = ringerModeAffectedStreams | 256;
        } else {
            ringerModeAffectedStreams2 = ringerModeAffectedStreams & (-257);
        }
        if (com.android.media.audio.Flags.ringerModeAffectsAlarm() && this.mRingerModeAffectsAlarm) {
            boolean muteAlarmWithRinger = this.mSettings.getGlobalInt(this.mContentResolver, "mute_alarm_stream_with_ringer_mode", 0) != 0;
            if (muteAlarmWithRinger) {
                ringerModeAffectedStreams2 |= 16;
            } else {
                ringerModeAffectedStreams2 &= -17;
            }
        }
        if (ringerModeAffectedStreams2 != this.mRingerModeAffectedStreams) {
            this.mSettings.putSystemIntForUser(this.mContentResolver, "mode_ringer_streams_affected", ringerModeAffectedStreams2, -2);
            this.mRingerModeAffectedStreams = ringerModeAffectedStreams2;
            return true;
        }
        return updatedZenModeAffectedStreams;
    }

    public boolean isStreamAffectedByMute(int streamType) {
        return (this.mMuteAffectedStreams & (1 << streamType)) != 0;
    }

    public boolean isStreamMutableByUi(int streamType) {
        return (this.mUserMutableStreams & (1 << streamType)) != 0;
    }

    private void ensureValidDirection(int direction) {
        switch (direction) {
            case com.android.server.wm.ITaskExt.SCREEN_ORIENTATION_UNFIXED /* -100 */:
            case -1:
            case 0:
            case 1:
            case 100:
            case 101:
                return;
            default:
                throw new java.lang.IllegalArgumentException("Bad direction " + direction);
        }
    }

    private void ensureValidStreamType(int streamType) {
        if (streamType < 0 || streamType >= this.mStreamStates.length) {
            throw new java.lang.IllegalArgumentException("Bad stream type " + streamType);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMuteAdjust(int adjust) {
        return adjust == -100 || adjust == 100 || adjust == 101;
    }

    public boolean isInCommunication() {
        android.telecom.TelecomManager telecomManager = (android.telecom.TelecomManager) this.mContext.getSystemService("telecom");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            boolean IsInCall = telecomManager.isInCall();
            android.os.Binder.restoreCallingIdentity(ident);
            int mode = this.mMode.get();
            return IsInCall || mode == 3 || mode == 2;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
    }

    private boolean wasStreamActiveRecently(int stream, int delay_ms) {
        return this.mAudioSystem.isStreamActive(stream, delay_ms) || this.mAudioSystem.isStreamActiveRemotely(stream, delay_ms);
    }

    private int getActiveStreamType(int suggestedStreamType) {
        return getActiveStreamType(suggestedStreamType, false);
    }

    private int getActiveStreamType(int suggestedStreamType, boolean isVolumeChangeDispatch) {
        if (this.mIsSingleVolume && suggestedStreamType == Integer.MIN_VALUE) {
            return 3;
        }
        switch (this.mPlatformType) {
            case 1:
                if (isInCommunication() || this.mAudioSystem.isStreamActive(0, 0)) {
                    if (!this.mAsExt.isVolumeDefaultAdjustSupported() || !isVolumeChangeDispatch || !this.mAudioSystem.isStreamActive(3, 0) || this.mAsExt.hasActivePlaybackOnForeground(0) || !this.mAsExt.hasActivePlaybackOnForeground(3)) {
                        return (this.mDeviceBroker.isBluetoothScoActive() || this.mAsExt.isBleDeviceCommunicationDevice()) ? 6 : 0;
                    }
                    if (DEBUG_VOL) {
                        android.util.Log.v(TAG, "getActiveStreamType: Forcing STREAM_MUSIC stream active");
                    }
                    return 3;
                }
                if (suggestedStreamType == Integer.MIN_VALUE) {
                    if (wasStreamActiveRecently(2, sStreamOverrideDelayMs)) {
                        if (DEBUG_VOL) {
                            android.util.Log.v(TAG, "getActiveStreamType: Forcing STREAM_RING stream active");
                        }
                        return 2;
                    }
                    if (wasStreamActiveRecently(5, sStreamOverrideDelayMs) && this.mAsExt.isNeedRetNotifiStream(isVolumeChangeDispatch)) {
                        if (DEBUG_VOL) {
                            android.util.Log.v(TAG, "getActiveStreamType: Forcing STREAM_NOTIFICATION stream active");
                        }
                        return 5;
                    }
                    if (this.mAsExt.isAssistantVolumeSupported() && wasStreamActiveRecently(11, sStreamOverrideDelayMs)) {
                        if (DEBUG_VOL) {
                            android.util.Log.v(TAG, "getActiveStreamType: STREAM_ASSISTANT stream active");
                        }
                        return 11;
                    }
                    if (this.mAsExt.isRingVolumeDefault() && !wasStreamActiveRecently(3, sStreamOverrideDelayMs)) {
                        return 2;
                    }
                    if (FEATURE_SPEAKER_CLEAN_SUPPORT && wasStreamActiveRecently(4, sStreamOverrideDelayMs)) {
                        if (DEBUG_VOL) {
                            android.util.Log.v(TAG, "getActiveStreamType: STREAM_ALARM stream active");
                        }
                        return 4;
                    }
                    if (DEBUG_VOL) {
                        android.util.Log.v(TAG, "getActiveStreamType: Forcing DEFAULT_VOL_STREAM_NO_PLAYBACK(3) b/c default");
                    }
                    return 3;
                }
                if (wasStreamActiveRecently(5, sStreamOverrideDelayMs) && this.mAsExt.isNeedRetNotifiStream(isVolumeChangeDispatch)) {
                    if (DEBUG_VOL) {
                        android.util.Log.v(TAG, "getActiveStreamType: Forcing STREAM_NOTIFICATION stream active");
                    }
                    return 5;
                }
                if (wasStreamActiveRecently(2, sStreamOverrideDelayMs)) {
                    if (DEBUG_VOL) {
                        android.util.Log.v(TAG, "getActiveStreamType: Forcing STREAM_RING stream active");
                    }
                    return 2;
                }
                if (this.mAsExt.isAssistantVolumeSupported() && wasStreamActiveRecently(11, sStreamOverrideDelayMs)) {
                    if (DEBUG_VOL) {
                        android.util.Log.v(TAG, "getActiveStreamType: Forcing STREAM_ASSISTANT stream active");
                    }
                    return 11;
                }
                if (FEATURE_SPEAKER_CLEAN_SUPPORT && wasStreamActiveRecently(4, sStreamOverrideDelayMs)) {
                    if (DEBUG_VOL) {
                        android.util.Log.v(TAG, "getActiveStreamType: Forcing STREAM_ALARM stream active");
                    }
                    return 4;
                }
                break;
        }
        if (isInCommunication()) {
            if (this.mDeviceBroker.isBluetoothScoActive()) {
                if (DEBUG_VOL) {
                    android.util.Log.v(TAG, "getActiveStreamType: Forcing STREAM_BLUETOOTH_SCO");
                }
                return 6;
            }
            if (DEBUG_VOL) {
                android.util.Log.v(TAG, "getActiveStreamType: Forcing STREAM_VOICE_CALL");
            }
            return 0;
        }
        if (this.mAudioSystem.isStreamActive(5, sStreamOverrideDelayMs)) {
            if (DEBUG_VOL) {
                android.util.Log.v(TAG, "getActiveStreamType: Forcing STREAM_NOTIFICATION");
            }
            return 5;
        }
        if (this.mAudioSystem.isStreamActive(2, sStreamOverrideDelayMs)) {
            if (DEBUG_VOL) {
                android.util.Log.v(TAG, "getActiveStreamType: Forcing STREAM_RING");
            }
            return 2;
        }
        if (suggestedStreamType == Integer.MIN_VALUE) {
            if (this.mAudioSystem.isStreamActive(5, sStreamOverrideDelayMs)) {
                if (DEBUG_VOL) {
                    android.util.Log.v(TAG, "getActiveStreamType: Forcing STREAM_NOTIFICATION");
                }
                return 5;
            }
            if (this.mAudioSystem.isStreamActive(2, sStreamOverrideDelayMs)) {
                if (DEBUG_VOL) {
                    android.util.Log.v(TAG, "getActiveStreamType: Forcing STREAM_RING");
                }
                return 2;
            }
            if (DEBUG_VOL) {
                android.util.Log.v(TAG, "getActiveStreamType: Forcing DEFAULT_VOL_STREAM_NO_PLAYBACK(3) b/c default");
            }
            return 3;
        }
        if (DEBUG_VOL) {
            android.util.Log.v(TAG, "getActiveStreamType: Returning suggested type " + suggestedStreamType);
        }
        return suggestedStreamType;
    }

    private void broadcastRingerMode(java.lang.String action, int ringerMode) {
        if (!this.mSystemServer.isPrivileged()) {
            return;
        }
        android.content.Intent broadcast = new android.content.Intent(action);
        broadcast.putExtra("android.media.EXTRA_RINGER_MODE", ringerMode);
        broadcast.addFlags(603979776);
        sendStickyBroadcastToAll(broadcast);
    }

    private void broadcastVibrateSetting(int vibrateType) {
        if (this.mSystemServer.isPrivileged() && this.mActivityManagerInternal.isSystemReady()) {
            android.content.Intent broadcast = new android.content.Intent("android.media.VIBRATE_SETTING_CHANGED");
            broadcast.putExtra("android.media.EXTRA_VIBRATE_TYPE", vibrateType);
            broadcast.putExtra("android.media.EXTRA_VIBRATE_SETTING", getVibrateSetting(vibrateType));
            sendBroadcastToAll(broadcast, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void queueMsgUnderWakeLock(android.os.Handler handler, int msg, int arg1, int arg2, java.lang.Object obj, int delay) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mAudioEventWakeLock.acquire();
            android.os.Binder.restoreCallingIdentity(ident);
            sendMsg(handler, msg, 2, arg1, arg2, obj, delay);
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sendMsg(android.os.Handler handler, int msg, int existingMsgPolicy, int arg1, int arg2, java.lang.Object obj, int delay) {
        if (existingMsgPolicy == 0) {
            handler.removeMessages(msg);
        } else if (existingMsgPolicy == 1 && handler.hasMessages(msg)) {
            return;
        }
        long time = android.os.SystemClock.uptimeMillis() + ((long) delay);
        handler.sendMessageAtTime(handler.obtainMessage(msg, arg1, arg2, obj), time);
    }

    private static void sendBundleMsg(android.os.Handler handler, int msg, int existingMsgPolicy, int arg1, int arg2, java.lang.Object obj, android.os.Bundle bundle, int delay) {
        if (existingMsgPolicy == 0) {
            handler.removeMessages(msg);
        } else if (existingMsgPolicy == 1 && handler.hasMessages(msg)) {
            return;
        }
        long time = android.os.SystemClock.uptimeMillis() + ((long) delay);
        android.os.Message message = handler.obtainMessage(msg, arg1, arg2, obj);
        message.setData(bundle);
        handler.sendMessageAtTime(message, time);
    }

    boolean checkAudioSettingsPermission(java.lang.String method) {
        if (callingOrSelfHasAudioSettingsPermission()) {
            return true;
        }
        java.lang.String msg = "Audio Settings Permission Denial: " + method + " from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid();
        android.util.Log.w(TAG, msg);
        return false;
    }

    private boolean callingOrSelfHasAudioSettingsPermission() {
        return this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_AUDIO_SETTINGS") == 0;
    }

    private boolean callingHasAudioSettingsPermission() {
        return this.mContext.checkCallingPermission("android.permission.MODIFY_AUDIO_SETTINGS") == 0;
    }

    private boolean hasAudioSettingsPermission(int uid, int pid) {
        return this.mContext.checkPermission("android.permission.MODIFY_AUDIO_SETTINGS", pid, uid) == 0;
    }

    protected void initMinStreamVolumeWithoutModifyAudioSettings() {
        int safeIndex;
        int deviceForAlarm = 4194304;
        if (java.lang.Float.isNaN(android.media.AudioSystem.getStreamVolumeDB(4, MIN_STREAM_VOLUME[4], 4194304))) {
            deviceForAlarm = 2;
        }
        int idx = MAX_STREAM_VOLUME[4];
        while (idx >= MIN_STREAM_VOLUME[4] && android.media.AudioSystem.getStreamVolumeDB(4, idx, deviceForAlarm) >= MIN_ALARM_ATTENUATION_NON_PRIVILEGED_DB) {
            idx--;
        }
        if (idx <= MIN_STREAM_VOLUME[4]) {
            safeIndex = MIN_STREAM_VOLUME[4];
        } else {
            safeIndex = java.lang.Math.min(idx + 1, MAX_STREAM_VOLUME[4]);
        }
        for (int stream : mStreamVolumeAlias) {
            if (mStreamVolumeAlias[stream] == 4) {
                this.mStreamStates[stream].updateNoPermMinIndex(safeIndex);
            }
        }
    }

    public int getDeviceForStream(int stream) {
        return selectOneAudioDevice(getDeviceSetForStream(stream));
    }

    private int selectOneAudioDevice(java.util.Set<java.lang.Integer> deviceSet) {
        if (deviceSet.isEmpty()) {
            return 0;
        }
        if (deviceSet.size() == 1) {
            return deviceSet.iterator().next().intValue();
        }
        if (deviceSet.contains(4096)) {
            return 4096;
        }
        if (deviceSet.contains(2)) {
            return 2;
        }
        if (deviceSet.contains(4194304)) {
            return 4194304;
        }
        if (deviceSet.contains(262144)) {
            return 262144;
        }
        if (deviceSet.contains(262145)) {
            return 262145;
        }
        if (deviceSet.contains(2097152)) {
            return 2097152;
        }
        if (deviceSet.contains(524288)) {
            return 524288;
        }
        java.util.Iterator<java.lang.Integer> it = deviceSet.iterator();
        while (it.hasNext()) {
            int deviceType = it.next().intValue();
            if (android.media.AudioSystem.DEVICE_OUT_ALL_A2DP_SET.contains(java.lang.Integer.valueOf(deviceType))) {
                return deviceType;
            }
        }
        android.util.Log.w(TAG, "selectOneAudioDevice returning DEVICE_NONE from invalid device combination " + android.media.AudioSystem.deviceSetToString(deviceSet));
        return 0;
    }

    @java.lang.Deprecated
    public int getDeviceMaskForStream(int streamType) {
        ensureValidStreamType(streamType);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            if (!android.os.Build.isMtkPlatform()) {
                return android.media.AudioSystem.getDeviceMaskFromSet(getDeviceSetForStreamDirect(streamType));
            }
            java.util.Set<java.lang.Integer> deviceTypes = getDeviceSetForStreamDirect(streamType);
            if (streamType == 3 && deviceTypes.contains(536870914)) {
                return 536870914;
            }
            return android.media.AudioSystem.getDeviceMaskFromSet(deviceTypes);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.Set<java.lang.Integer> getDeviceSetForStreamDirect(int stream) {
        android.media.AudioAttributes attr = android.media.audiopolicy.AudioProductStrategy.getAudioAttributesForStrategyWithLegacyStreamType(stream);
        java.util.Set<java.lang.Integer> deviceSet = android.media.AudioSystem.generateAudioDeviceTypesSet(getDevicesForAttributesInt(attr, true));
        return deviceSet;
    }

    public java.util.Set<java.lang.Integer> getDeviceSetForStream(int stream) {
        java.util.Set<java.lang.Integer> setObserveDevicesForStream_syncVSS;
        ensureValidStreamType(stream);
        synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
            setObserveDevicesForStream_syncVSS = this.mStreamStates[stream].observeDevicesForStream_syncVSS(true);
        }
        return setObserveDevicesForStream_syncVSS;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onObserveDevicesForAllStreams(int skipStream) {
        synchronized (this.mSettingsLock) {
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                for (int stream = 0; stream < this.mStreamStates.length; stream++) {
                    if (stream != skipStream) {
                        java.util.Set<java.lang.Integer> deviceSet = this.mStreamStates[stream].observeDevicesForStream_syncVSS(false);
                        for (java.lang.Integer device : deviceSet) {
                            if (!this.mAsExt.canHeadsetFadeIn(stream, device.intValue())) {
                                updateVolumeStates(device.intValue(), stream, "AudioService#onObserveDevicesForAllStreams");
                            }
                        }
                    }
                }
            }
        }
    }

    public void postObserveDevicesForAllStreams() {
        postObserveDevicesForAllStreams(-1);
    }

    public void postObserveDevicesForAllStreams(int skipStream) {
        sendMsg(this.mAudioHandler, 27, 2, skipStream, 0, null, 0);
    }

    public void registerDeviceVolumeDispatcherForAbsoluteVolume(boolean register, android.media.IAudioDeviceVolumeDispatcher cb, java.lang.String packageName, android.media.AudioDeviceAttributes device, java.util.List<android.media.VolumeInfo> volumes, boolean handlesVolumeAdjustment, int deviceVolumeBehavior) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_AUDIO_ROUTING") != 0 && this.mContext.checkCallingOrSelfPermission("android.permission.BLUETOOTH_PRIVILEGED") != 0) {
            throw new java.lang.SecurityException("Missing MODIFY_AUDIO_ROUTING or BLUETOOTH_PRIVILEGED permissions");
        }
        java.util.Objects.requireNonNull(device);
        java.util.Objects.requireNonNull(volumes);
        int deviceOut = device.getInternalType();
        if (register) {
            com.android.server.audio.AudioService.AbsoluteVolumeDeviceInfo info = new com.android.server.audio.AudioService.AbsoluteVolumeDeviceInfo(device, volumes, cb, handlesVolumeAdjustment, deviceVolumeBehavior);
            com.android.server.audio.AudioService.AbsoluteVolumeDeviceInfo oldInfo = this.mAbsoluteVolumeDeviceInfoMap.get(java.lang.Integer.valueOf(deviceOut));
            if (oldInfo != null && oldInfo.mDeviceVolumeBehavior == deviceVolumeBehavior) {
                volumeBehaviorChanged = false;
            }
            if (volumeBehaviorChanged) {
                removeAudioSystemDeviceOutFromFullVolumeDevices(deviceOut);
                removeAudioSystemDeviceOutFromFixedVolumeDevices(deviceOut);
                addAudioSystemDeviceOutToAbsVolumeDevices(deviceOut, info);
                dispatchDeviceVolumeBehavior(device, deviceVolumeBehavior);
            }
            for (android.media.VolumeInfo volumeInfo : volumes) {
                if (volumeInfo.getVolumeIndex() != -100 && volumeInfo.getMinVolumeIndex() != -100 && volumeInfo.getMaxVolumeIndex() != -100) {
                    if (volumeInfo.hasStreamType()) {
                        setStreamVolumeInt(volumeInfo.getStreamType(), rescaleIndex(volumeInfo, volumeInfo.getStreamType()), deviceOut, false, packageName, true);
                    } else {
                        android.media.VolumeInfo volumeInfo2 = volumeInfo;
                        int[] legacyStreamTypes = volumeInfo2.getVolumeGroup().getLegacyStreamTypes();
                        int length = legacyStreamTypes.length;
                        int i = 0;
                        while (i < length) {
                            int streamType = legacyStreamTypes[i];
                            android.media.VolumeInfo volumeInfo3 = volumeInfo2;
                            setStreamVolumeInt(streamType, rescaleIndex(volumeInfo3, streamType), deviceOut, false, packageName, true);
                            i++;
                            length = length;
                            volumeInfo2 = volumeInfo3;
                            legacyStreamTypes = legacyStreamTypes;
                        }
                    }
                }
            }
            return;
        }
        volumeBehaviorChanged = removeAudioSystemDeviceOutFromAbsVolumeDevices(deviceOut) != null;
        boolean wasAbsVol = volumeBehaviorChanged;
        if (wasAbsVol) {
            dispatchDeviceVolumeBehavior(device, 0);
        }
    }

    public void setDeviceVolumeBehavior(android.media.AudioDeviceAttributes device, int deviceVolumeBehavior, java.lang.String pkgName) {
        super.setDeviceVolumeBehavior_enforcePermission();
        java.util.Objects.requireNonNull(device);
        android.media.AudioManager.enforceValidVolumeBehavior(deviceVolumeBehavior);
        android.media.AudioDeviceAttributes device2 = retrieveBluetoothAddress(device);
        sVolumeLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("setDeviceVolumeBehavior: dev:" + android.media.AudioSystem.getOutputDeviceName(device2.getInternalType()) + " addr:" + android.media.AudioDeviceVolumeManager.volumeBehaviorName(deviceVolumeBehavior) + " pack:" + pkgName).printLog(TAG));
        if (pkgName == null) {
            pkgName = "";
        }
        if (device2.getType() == 8) {
            avrcpSupportsAbsoluteVolume(device2.getAddress(), deviceVolumeBehavior == 3);
        } else if (device2.getType() == 26) {
            leVcSupportsAbsoluteVolume(device2.getAddress(), deviceVolumeBehavior == 3);
        } else {
            setDeviceVolumeBehaviorInternal(device2, deviceVolumeBehavior, pkgName);
            persistDeviceVolumeBehavior(device2.getInternalType(), deviceVolumeBehavior);
        }
    }

    private void setDeviceVolumeBehaviorInternal(android.media.AudioDeviceAttributes device, int deviceVolumeBehavior, java.lang.String caller) {
        int audioSystemDeviceOut = device.getInternalType();
        boolean volumeBehaviorChanged = false;
        switch (deviceVolumeBehavior) {
            case 0:
                volumeBehaviorChanged = false | (removeAudioSystemDeviceOutFromAbsVolumeDevices(audioSystemDeviceOut) != null) | removeAudioSystemDeviceOutFromFullVolumeDevices(audioSystemDeviceOut) | removeAudioSystemDeviceOutFromFixedVolumeDevices(audioSystemDeviceOut);
                break;
            case 1:
                volumeBehaviorChanged = false | (removeAudioSystemDeviceOutFromAbsVolumeDevices(audioSystemDeviceOut) != null) | addAudioSystemDeviceOutToFullVolumeDevices(audioSystemDeviceOut) | removeAudioSystemDeviceOutFromFixedVolumeDevices(audioSystemDeviceOut);
                break;
            case 2:
                volumeBehaviorChanged = false | (removeAudioSystemDeviceOutFromAbsVolumeDevices(audioSystemDeviceOut) != null) | removeAudioSystemDeviceOutFromFullVolumeDevices(audioSystemDeviceOut) | addAudioSystemDeviceOutToFixedVolumeDevices(audioSystemDeviceOut);
                break;
            case 3:
            case 4:
            case 5:
                throw new java.lang.IllegalArgumentException("Absolute volume unsupported for now");
        }
        if (volumeBehaviorChanged) {
            sendMsg(this.mAudioHandler, 47, 2, deviceVolumeBehavior, 0, device, 0);
        }
        sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("Volume behavior " + deviceVolumeBehavior + " for dev=0x" + java.lang.Integer.toHexString(audioSystemDeviceOut) + " from:" + caller));
        postUpdateVolumeStatesForAudioDevice(audioSystemDeviceOut, "setDeviceVolumeBehavior:" + caller);
    }

    public int getDeviceVolumeBehavior(android.media.AudioDeviceAttributes device) {
        super.getDeviceVolumeBehavior_enforcePermission();
        java.util.Objects.requireNonNull(device);
        return getDeviceVolumeBehaviorInt(retrieveBluetoothAddress(device));
    }

    private int getDeviceVolumeBehaviorInt(android.media.AudioDeviceAttributes device) {
        int audioSystemDeviceOut = device.getInternalType();
        if (this.mFullVolumeDevices.contains(java.lang.Integer.valueOf(audioSystemDeviceOut))) {
            return 1;
        }
        if (this.mFixedVolumeDevices.contains(java.lang.Integer.valueOf(audioSystemDeviceOut))) {
            return 2;
        }
        if (this.mAbsVolumeMultiModeCaseDevices.contains(java.lang.Integer.valueOf(audioSystemDeviceOut))) {
            return 4;
        }
        if (this.mAbsoluteVolumeDeviceInfoMap.containsKey(java.lang.Integer.valueOf(audioSystemDeviceOut))) {
            return this.mAbsoluteVolumeDeviceInfoMap.get(java.lang.Integer.valueOf(audioSystemDeviceOut)).mDeviceVolumeBehavior;
        }
        if (isA2dpAbsoluteVolumeDevice(audioSystemDeviceOut) || android.media.AudioSystem.isLeAudioDeviceType(audioSystemDeviceOut)) {
            return 3;
        }
        return (audioSystemDeviceOut == 536870912 && this.mBleVcAbsVolSupported) ? 3 : 0;
    }

    public boolean isVolumeFixed() {
        if (this.mUseFixedVolume) {
            return true;
        }
        android.media.AudioAttributes attributes = new android.media.AudioAttributes.Builder().setUsage(1).build();
        java.util.List<android.media.AudioDeviceAttributes> devices = getDevicesForAttributesInt(attributes, true);
        for (android.media.AudioDeviceAttributes device : devices) {
            if (getDeviceVolumeBehaviorInt(device) == 2) {
                return true;
            }
        }
        return false;
    }

    public void setWiredDeviceConnectionState(android.media.AudioDeviceAttributes attributes, int state, java.lang.String caller) {
        super.setWiredDeviceConnectionState_enforcePermission();
        java.util.Objects.requireNonNull(attributes);
        android.media.AudioDeviceAttributes attributes2 = retrieveBluetoothAddress(attributes);
        if (attributes2.getType() == 10 && attributes2.getRole() == 2 && attributes2.getAudioDescriptors().isEmpty()) {
            attributes2 = new android.media.AudioDeviceAttributes(attributes2.getRole(), attributes2.getType(), attributes2.getAddress(), attributes2.getName(), attributes2.getAudioProfiles(), new java.util.ArrayList(java.util.Collections.singletonList(new android.media.AudioDescriptor(1, 0, DEFAULT_ARC_AUDIO_DESCRIPTOR))));
        }
        if (state != 1 && state != 0 && state != 2) {
            throw new java.lang.IllegalArgumentException("Invalid state " + state);
        }
        new android.media.MediaMetrics.Item("audio.service.setWiredDeviceConnectionState").set(android.media.MediaMetrics.Property.ADDRESS, attributes2.getAddress()).set(android.media.MediaMetrics.Property.CLIENT_NAME, caller).set(android.media.MediaMetrics.Property.DEVICE, android.media.AudioSystem.getDeviceName(attributes2.getInternalType())).set(android.media.MediaMetrics.Property.NAME, attributes2.getName()).set(android.media.MediaMetrics.Property.STATE, state == 1 ? "connected" : "disconnected").record();
        if (attributes2.getInternalType() == 131072) {
            android.util.Log.d(TAG, "Ignore line-out device temporary");
            return;
        }
        if (DEBUG_DEVICES) {
            java.lang.String stateInfo = state == 1 ? "Connected" : "Disconnected";
            java.lang.String eventSource = "setWiredDeviceConnectionState()) from u/pid:" + android.os.Binder.getCallingUid() + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + android.os.Binder.getCallingPid() + ", type=" + android.media.AudioSystem.getDeviceName(attributes2.getInternalType()) + ", state=" + state + ("(" + stateInfo + ")") + ", address=" + attributes2.getAddress() + ", name=" + attributes2.getAddress() + ", caller=" + caller;
            android.util.Log.d(TAG, eventSource);
        }
        this.mDeviceBroker.setWiredDeviceConnectionState(attributes2, state, caller);
        if (attributes2.getInternalType() == -2013265920) {
            updateHdmiAudioSystemClient();
        }
    }

    private void updateHdmiAudioSystemClient() {
        android.util.Slog.d(TAG, "Hdmi Audio System Client is updated");
        synchronized (this.mHdmiClientLock) {
            this.mHdmiAudioSystemClient = this.mHdmiManager.getAudioSystemClient();
        }
    }

    public void setIPDeviceConnectionState(android.media.AudioDeviceAttributes attributes, int state, java.lang.String caller, android.os.IBinder cb, boolean suppressNoisyIntent) {
        enforceModifyAudioRoutingPermission();
        if (state != 1 && state != 0) {
            throw new java.lang.IllegalArgumentException("Invalid state " + state);
        }
        this.mDeviceBroker.setWiredDeviceConnectionState(attributes, state, caller, suppressNoisyIntent);
        if (cb != null) {
            this.mAsExt.onIPDeviceConnectionChange(attributes, state, caller, cb);
        }
    }

    public void setTestDeviceConnectionState(android.media.AudioDeviceAttributes audioDeviceAttributes, boolean z) {
        java.util.Objects.requireNonNull(audioDeviceAttributes);
        enforceModifyAudioRoutingPermission();
        this.mDeviceBroker.setTestDeviceConnectionState(retrieveBluetoothAddress(audioDeviceAttributes), z ? 1 : 0);
        sendMsg(this.mAudioHandler, 41, 0, 0, 0, null, 0);
    }

    public void handleBluetoothActiveDeviceChanged(android.bluetooth.BluetoothDevice newDevice, android.bluetooth.BluetoothDevice previousDevice, android.media.BluetoothProfileConnectionInfo info) {
        handleBluetoothActiveDeviceChanged_enforcePermission();
        if (info == null) {
            throw new java.lang.IllegalArgumentException("Illegal null BluetoothProfileConnectionInfo for device " + previousDevice + " -> " + newDevice);
        }
        int profile = info.getProfile();
        if (profile != 2 && profile != 11 && profile != 22 && profile != 26 && profile != 21 && (!this.mDeviceBroker.isScoManagedByAudio() || profile != 1)) {
            throw new java.lang.IllegalArgumentException("Illegal BluetoothProfile profile for device " + previousDevice + " -> " + newDevice + ". Got: " + profile);
        }
        sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("BluetoothActiveDeviceChanged for " + android.bluetooth.BluetoothProfile.getProfileName(profile) + ", device update " + previousDevice + " -> " + newDevice).printLog(TAG));
        com.android.server.audio.AudioDeviceBroker.BtDeviceChangedData data = new com.android.server.audio.AudioDeviceBroker.BtDeviceChangedData(newDevice, previousDevice, info, "AudioService");
        if (DEBUG_DEVICES) {
            java.lang.String eventSource = "handleBluetoothActiveDeviceChanged()) from u/pid:" + android.os.Binder.getCallingUid() + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + android.os.Binder.getCallingPid() + ", profile=" + android.bluetooth.BluetoothProfile.getProfileName(profile) + ", isSuppressNoisyIntent=" + info.isSuppressNoisyIntent() + ", previousDevice=" + previousDevice + " -> , newDevice=" + newDevice + " ," + data + ", isSuppressNoisyIntent=" + info.isSuppressNoisyIntent() + ", volume=" + info.getVolume();
            android.util.Log.d(TAG, eventSource);
        }
        if (android.os.Build.isMtkPlatform() && ((profile == 22 || profile == 26) && previousDevice != null && info.isLeOutput())) {
            setleVcAbsoluteVolumeSupported(false);
        }
        if ((previousDevice != null || newDevice != null) && info.isSuppressNoisyIntent()) {
            this.mDeviceBroker.postCheckMessagesMuteMusic();
            boolean isMtk = android.os.Build.isMtkPlatform();
            if (isMtk && info.getVolume() != -1 && ((profile == 2 || profile == 11) && newDevice != null)) {
                if (this.mAsExt.getBluetoothVolSyncSupported()) {
                    int a2dpVolume = this.mDeviceBroker.getWrapper().getA2dpVolume(false, info.getVolume() * 10);
                    this.mDeviceBroker.postSetVolumeIndexOnDevice(3, a2dpVolume, 128, "handleBluetoothActiveDeviceChanged");
                } else {
                    this.mDeviceBroker.postSetVolumeIndexOnDevice(3, info.getVolume() * 10, 128, "handleBluetoothActiveDeviceChanged");
                }
            }
        }
        sendMsg(this.mAudioHandler, 38, 2, 0, 0, data, 0);
        if ((profile == 2 || profile == 11 || (profile == 22 && info.isLeOutput())) && newDevice != null) {
            this.mSpatializerHelper.resetSpatialAudioState();
        }
    }

    public void setMusicMute(boolean mute) {
        this.mStreamStates[3].muteInternally(mute);
    }

    public void handleBluetoothA2dpActiveDeviceChange(android.bluetooth.BluetoothDevice device, int state, int profile, boolean suppressNoisyIntent, int a2dpVolume) {
        if (device == null) {
            throw new java.lang.IllegalArgumentException("Illegal null device");
        }
        if (profile != 2 && profile != 11) {
            throw new java.lang.IllegalArgumentException("invalid profile " + profile);
        }
        if (state != 2 && state != 0) {
            throw new java.lang.IllegalArgumentException("Invalid state " + state);
        }
        this.mDeviceBroker.queueOnBluetoothActiveDeviceChanged(new com.android.server.audio.AudioDeviceBroker.BtDeviceChangedData(device, device, new android.media.BluetoothProfileConnectionInfo(profile), "AudioService"));
    }

    public void postAccessoryPlugMediaUnmute(int newDevice) {
        sendMsg(this.mAudioHandler, 21, 2, newDevice, 0, null, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAccessoryPlugMediaUnmute(int newDevice) {
        if (DEBUG_VOL) {
            android.util.Log.i(TAG, java.lang.String.format("onAccessoryPlugMediaUnmute newDevice=%d [%s]", java.lang.Integer.valueOf(newDevice), android.media.AudioSystem.getOutputDeviceName(newDevice)));
        }
        if (this.mNm.getZenMode() != 2 && !isStreamMutedByRingerOrZenMode(3) && DEVICE_MEDIA_UNMUTED_ON_PLUG_SET.contains(java.lang.Integer.valueOf(newDevice)) && this.mStreamStates[3].mIsMuted && this.mStreamStates[3].getIndex(newDevice) != 0 && getDeviceSetForStreamDirect(3).contains(java.lang.Integer.valueOf(newDevice))) {
            if (DEBUG_VOL) {
                android.util.Log.i(TAG, java.lang.String.format("onAccessoryPlugMediaUnmute unmuting device=%d [%s]", java.lang.Integer.valueOf(newDevice), android.media.AudioSystem.getOutputDeviceName(newDevice)));
            }
            synchronized (this.mSettingsLock) {
                this.mStreamStates[3].mute(false, "onAccessoryPlugMediaUnmute");
            }
        }
    }

    public boolean hasHapticChannels(android.net.Uri uri) {
        return android.media.AudioManager.hasHapticChannelsImpl(this.mContext, uri);
    }

    private void initVolumeGroupStates() {
        for (android.media.audiopolicy.AudioVolumeGroup avg : getAudioVolumeGroups()) {
            try {
                ensureValidAttributes(avg);
                sVolumeGroupStates.append(avg.getId(), new com.android.server.audio.AudioService.VolumeGroupState(avg));
            } catch (java.lang.IllegalArgumentException e) {
                if (DEBUG_VOL) {
                    android.util.Log.d(TAG, "volume group " + avg.name() + " for internal policy needs");
                }
            }
        }
        synchronized (this.mSettingsLock) {
            for (int i = 0; i < sVolumeGroupStates.size(); i++) {
                com.android.server.audio.AudioService.VolumeGroupState vgs = sVolumeGroupStates.valueAt(i);
                vgs.applyAllVolumes(false);
            }
        }
    }

    private void ensureValidAttributes(android.media.audiopolicy.AudioVolumeGroup avg) {
        boolean hasAtLeastOneValidAudioAttributes = avg.getAudioAttributes().stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda25
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.audio.AudioService.lambda$ensureValidAttributes$14((android.media.AudioAttributes) obj);
            }
        });
        if (!hasAtLeastOneValidAudioAttributes) {
            throw new java.lang.IllegalArgumentException("Volume Group " + avg.name() + " has no valid audio attributes");
        }
    }

    static /* synthetic */ boolean lambda$ensureValidAttributes$14(android.media.AudioAttributes aa) {
        return !aa.equals(android.media.audiopolicy.AudioProductStrategy.getDefaultAttributes());
    }

    private void readVolumeGroupsSettings(boolean userSwitch) {
        synchronized (this.mSettingsLock) {
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                if (DEBUG_VOL) {
                    android.util.Log.d(TAG, "readVolumeGroupsSettings userSwitch=" + userSwitch);
                }
                for (int i = 0; i < sVolumeGroupStates.size(); i++) {
                    com.android.server.audio.AudioService.VolumeGroupState vgs = sVolumeGroupStates.valueAt(i);
                    if (mVolumeNotShare) {
                        vgs.clearIndexCache();
                        vgs.readSettings();
                        vgs.applyAllVolumes(userSwitch);
                    } else {
                        if (!userSwitch || !vgs.isMusic()) {
                            vgs.clearIndexCache();
                            vgs.readSettings();
                        }
                        vgs.applyAllVolumes(userSwitch);
                    }
                }
            }
        }
    }

    private void restoreVolumeGroups() {
        if (DEBUG_VOL) {
            android.util.Log.v(TAG, "restoreVolumeGroups");
        }
        synchronized (this.mSettingsLock) {
            for (int i = 0; i < sVolumeGroupStates.size(); i++) {
                com.android.server.audio.AudioService.VolumeGroupState vgs = sVolumeGroupStates.valueAt(i);
                vgs.applyAllVolumes(false);
            }
        }
    }

    private void dumpVolumeGroups(java.io.PrintWriter pw) {
        pw.println("\nVolume Groups (device: index)");
        for (int i = 0; i < sVolumeGroupStates.size(); i++) {
            com.android.server.audio.AudioService.VolumeGroupState vgs = sVolumeGroupStates.valueAt(i);
            vgs.dump(pw);
            pw.println("");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isCallStream(int stream) {
        return stream == 0 || stream == 6;
    }

    private static int getVolumeGroupForStreamType(int stream) {
        android.media.AudioAttributes attributes = android.media.audiopolicy.AudioProductStrategy.getAudioAttributesForStrategyWithLegacyStreamType(stream);
        if (attributes.equals(new android.media.AudioAttributes.Builder().build())) {
            return -1;
        }
        return android.media.audiopolicy.AudioProductStrategy.getVolumeGroupIdForAudioAttributes(attributes, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class VolumeGroupState {
        private android.media.AudioAttributes mAudioAttributes;
        private final android.media.audiopolicy.AudioVolumeGroup mAudioVolumeGroup;
        private boolean mHasValidStreamType;
        private final android.util.SparseIntArray mIndexMap;
        private int mIndexMax;
        private int mIndexMin;
        private boolean mIsMuted;
        private int mPublicStreamType;
        private java.lang.String mSettingName;

        private int getDeviceForVolume() {
            return com.android.server.audio.AudioService.this.getDeviceForStream(this.mPublicStreamType);
        }

        private VolumeGroupState(android.media.audiopolicy.AudioVolumeGroup avg) {
            this.mIndexMap = new android.util.SparseIntArray(8);
            int i = 0;
            this.mHasValidStreamType = false;
            this.mPublicStreamType = 3;
            this.mAudioAttributes = android.media.audiopolicy.AudioProductStrategy.getDefaultAttributes();
            this.mIsMuted = false;
            this.mAudioVolumeGroup = avg;
            if (com.android.server.audio.AudioService.DEBUG_VOL) {
                android.util.Log.v(com.android.server.audio.AudioService.TAG, "VolumeGroupState for " + avg.toString());
            }
            java.util.Iterator it = avg.getAudioAttributes().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                android.media.AudioAttributes aa = (android.media.AudioAttributes) it.next();
                if (!aa.equals(this.mAudioAttributes)) {
                    this.mAudioAttributes = aa;
                    break;
                }
            }
            int[] streamTypes = this.mAudioVolumeGroup.getLegacyStreamTypes();
            java.lang.String streamSettingName = "";
            if (streamTypes.length != 0) {
                int length = streamTypes.length;
                while (true) {
                    if (i < length) {
                        int streamType = streamTypes[i];
                        if (streamType == -1 || streamType >= android.media.AudioSystem.getNumStreamTypes()) {
                            i++;
                        } else {
                            this.mPublicStreamType = streamType;
                            this.mHasValidStreamType = true;
                            streamSettingName = android.provider.Settings.System.VOLUME_SETTINGS_INT[this.mPublicStreamType];
                            break;
                        }
                    } else {
                        break;
                    }
                }
                this.mIndexMin = com.android.server.audio.AudioService.MIN_STREAM_VOLUME[this.mPublicStreamType];
                this.mIndexMax = com.android.server.audio.AudioService.MAX_STREAM_VOLUME[this.mPublicStreamType];
            } else if (!avg.getAudioAttributes().isEmpty()) {
                this.mIndexMin = android.media.AudioSystem.getMinVolumeIndexForAttributes(this.mAudioAttributes);
                this.mIndexMax = android.media.AudioSystem.getMaxVolumeIndexForAttributes(this.mAudioAttributes);
            } else {
                throw new java.lang.IllegalArgumentException("volume group: " + this.mAudioVolumeGroup.name() + " has neither valid attributes nor valid stream types assigned");
            }
            this.mSettingName = !streamSettingName.isEmpty() ? streamSettingName : "volume_" + name();
            readSettings();
        }

        public int[] getLegacyStreamTypes() {
            return this.mAudioVolumeGroup.getLegacyStreamTypes();
        }

        public java.lang.String name() {
            return this.mAudioVolumeGroup.name();
        }

        public int getId() {
            return this.mAudioVolumeGroup.getId();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isVssMuteBijective(int stream) {
            return com.android.server.audio.AudioService.this.isStreamAffectedByMute(stream) && getMinIndex() == (com.android.server.audio.AudioService.this.mStreamStates[stream].mIndexMin + 5) / 10 && (getMinIndex() == 0 || com.android.server.audio.AudioService.isCallStream(stream));
        }

        private boolean isMutable() {
            return this.mIndexMin == 0 || (this.mHasValidStreamType && isVssMuteBijective(this.mPublicStreamType));
        }

        public boolean mute(boolean muted) {
            if (!isMutable()) {
                if (com.android.server.audio.AudioService.DEBUG_VOL) {
                    android.util.Log.d(com.android.server.audio.AudioService.TAG, "invalid mute on unmutable volume group " + name());
                }
                return false;
            }
            boolean changed = this.mIsMuted != muted;
            if (changed) {
                this.mIsMuted = muted;
                applyAllVolumes(false);
            }
            return changed;
        }

        public boolean isMuted() {
            return this.mIsMuted;
        }

        public void adjustVolume(int direction, int flags) {
            synchronized (com.android.server.audio.AudioService.this.mSettingsLock) {
                synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                    int device = getDeviceForVolume();
                    int previousIndex = getIndex(device);
                    if (com.android.server.audio.AudioService.this.isMuteAdjust(direction) && !isMutable()) {
                        if (com.android.server.audio.AudioService.DEBUG_VOL) {
                            android.util.Log.d(com.android.server.audio.AudioService.TAG, "invalid mute on unmutable volume group " + name());
                        }
                        return;
                    }
                    switch (direction) {
                        case com.android.server.wm.ITaskExt.SCREEN_ORIENTATION_UNFIXED /* -100 */:
                            if (previousIndex != 0) {
                                mute(true);
                            }
                            this.mIsMuted = true;
                            break;
                        case -1:
                            if (isMuted() && previousIndex != 0) {
                                mute(false);
                            } else {
                                int newIndex = java.lang.Math.max(previousIndex - 1, this.mIndexMin);
                                setVolumeIndex(newIndex, device, flags);
                            }
                            break;
                        case 1:
                            setVolumeIndex(java.lang.Math.min(previousIndex + 1, this.mIndexMax), device, flags);
                            break;
                        case 100:
                            mute(false);
                            break;
                        case 101:
                            mute(this.mIsMuted ? false : true);
                            break;
                    }
                }
            }
        }

        public int getVolumeIndex() {
            int index;
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                index = getIndex(getDeviceForVolume());
            }
            return index;
        }

        public void setVolumeIndex(int index, int flags) {
            synchronized (com.android.server.audio.AudioService.this.mSettingsLock) {
                synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                    if (com.android.server.audio.AudioService.this.mUseFixedVolume) {
                        return;
                    }
                    setVolumeIndex(index, getDeviceForVolume(), flags);
                }
            }
        }

        private void setVolumeIndex(int index, int device, int flags) {
            updateVolumeIndex(index, device);
            boolean changed = mute(index == 0);
            if (!changed) {
                setVolumeIndexInt(getValidIndex(index), device, flags);
            }
        }

        public void updateVolumeIndex(int index, int device) {
            if (this.mIndexMap.indexOfKey(device) < 0 || this.mIndexMap.get(device) != index) {
                this.mIndexMap.put(device, getValidIndex(index));
                com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, 2, 2, device, 0, this, 500);
            }
        }

        private void setVolumeIndexInt(int index, int device, int flags) {
            if (this.mHasValidStreamType && isVssMuteBijective(this.mPublicStreamType) && com.android.server.audio.AudioService.this.mStreamStates[this.mPublicStreamType].isFullyMuted()) {
                index = 0;
            } else if (this.mPublicStreamType == 6 && index == 0) {
                index = 1;
            }
            com.android.server.audio.AudioService.this.mAudioSystem.setVolumeIndexForAttributes(this.mAudioAttributes, index, device);
        }

        private int getIndex(int device) {
            int index = this.mIndexMap.get(device, -1);
            return index != -1 ? index : this.mIndexMap.get(1073741824);
        }

        private boolean hasIndexForDevice(int device) {
            return this.mIndexMap.get(device, -1) != -1;
        }

        public int getMaxIndex() {
            return this.mIndexMax;
        }

        public int getMinIndex() {
            return this.mIndexMin;
        }

        private boolean isValidStream(int stream) {
            return stream != -1 && stream < com.android.server.audio.AudioService.this.mStreamStates.length;
        }

        public boolean isMusic() {
            return this.mHasValidStreamType && this.mPublicStreamType == 3;
        }

        public void applyAllVolumes(boolean userSwitch) {
            int i;
            int[] iArr;
            int[] iArr2;
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                int i2 = 0;
                while (true) {
                    i = 1073741824;
                    if (i2 >= this.mIndexMap.size()) {
                        break;
                    }
                    int device = this.mIndexMap.keyAt(i2);
                    int index = this.mIndexMap.valueAt(i2);
                    boolean synced = false;
                    if (device != 1073741824) {
                        int[] legacyStreamTypes = getLegacyStreamTypes();
                        int length = legacyStreamTypes.length;
                        int i3 = 0;
                        while (i3 < length) {
                            int stream = legacyStreamTypes[i3];
                            if (!isValidStream(stream)) {
                                iArr2 = legacyStreamTypes;
                            } else {
                                boolean streamMuted = com.android.server.audio.AudioService.this.mStreamStates[stream].mIsMuted;
                                int deviceForStream = com.android.server.audio.AudioService.this.getDeviceForStream(stream);
                                int indexForStream = (com.android.server.audio.AudioService.this.mStreamStates[stream].getIndex(deviceForStream) + 5) / 10;
                                if (device != deviceForStream) {
                                    iArr2 = legacyStreamTypes;
                                } else if (indexForStream == index && isMuted() == streamMuted && isVssMuteBijective(stream)) {
                                    iArr2 = legacyStreamTypes;
                                    synced = true;
                                } else {
                                    boolean synced2 = com.android.media.audio.Flags.vgsVssSyncMuteOrder();
                                    if (!synced2 || isMuted() == streamMuted || !isVssMuteBijective(stream)) {
                                        iArr2 = legacyStreamTypes;
                                    } else {
                                        iArr2 = legacyStreamTypes;
                                        com.android.server.audio.AudioService.this.mStreamStates[stream].mute(isMuted(), "VGS.applyAllVolumes#1");
                                    }
                                    if (indexForStream != index) {
                                        com.android.server.audio.AudioService.this.mStreamStates[stream].setIndex(index * 10, device, "from vgs", true);
                                    }
                                    if (!com.android.media.audio.Flags.vgsVssSyncMuteOrder() && isMuted() != streamMuted && isVssMuteBijective(stream)) {
                                        com.android.server.audio.AudioService.this.mStreamStates[stream].mute(isMuted(), "VGS.applyAllVolumes#1");
                                    }
                                }
                            }
                            i3++;
                            legacyStreamTypes = iArr2;
                        }
                        if (!synced) {
                            if (com.android.server.audio.AudioService.DEBUG_VOL) {
                                android.util.Log.d(com.android.server.audio.AudioService.TAG, "applyAllVolumes: apply index " + index + ", group " + this.mAudioVolumeGroup.name() + " and device " + android.media.AudioSystem.getOutputDeviceName(device));
                            }
                            setVolumeIndexInt(isMuted() ? 0 : index, device, 0);
                        }
                    }
                    i2++;
                }
                int index2 = getIndex(1073741824);
                boolean synced3 = false;
                int deviceForVolume = getDeviceForVolume();
                boolean forceDeviceSync = userSwitch && this.mIndexMap.indexOfKey(deviceForVolume) < 0;
                int[] legacyStreamTypes2 = getLegacyStreamTypes();
                int length2 = legacyStreamTypes2.length;
                int i4 = 0;
                while (i4 < length2) {
                    int stream2 = legacyStreamTypes2[i4];
                    if (!isValidStream(stream2)) {
                        iArr = legacyStreamTypes2;
                    } else {
                        boolean streamMuted2 = com.android.server.audio.AudioService.this.mStreamStates[stream2].mIsMuted;
                        int defaultStreamIndex = (com.android.server.audio.AudioService.this.mStreamStates[stream2].getIndex(i) + 5) / 10;
                        if (forceDeviceSync) {
                            com.android.server.audio.AudioService.this.mStreamStates[stream2].setIndex(index2 * 10, deviceForVolume, "from vgs", true);
                        }
                        if (defaultStreamIndex == index2 && isMuted() == streamMuted2 && isVssMuteBijective(stream2)) {
                            synced3 = true;
                            iArr = legacyStreamTypes2;
                        } else {
                            if (defaultStreamIndex == index2) {
                                iArr = legacyStreamTypes2;
                            } else {
                                iArr = legacyStreamTypes2;
                                com.android.server.audio.AudioService.this.mStreamStates[stream2].setIndex(index2 * 10, 1073741824, "from vgs", true);
                            }
                            if (isMuted() != streamMuted2 && isVssMuteBijective(stream2)) {
                                com.android.server.audio.AudioService.this.mStreamStates[stream2].mute(isMuted(), "VGS.applyAllVolumes#2");
                            }
                        }
                    }
                    i4++;
                    legacyStreamTypes2 = iArr;
                    i = 1073741824;
                }
                if (!synced3) {
                    if (com.android.server.audio.AudioService.DEBUG_VOL) {
                        android.util.Log.d(com.android.server.audio.AudioService.TAG, "applyAllVolumes: apply default device index " + index2 + ", group " + this.mAudioVolumeGroup.name());
                    }
                    setVolumeIndexInt(isMuted() ? 0 : index2, 1073741824, 0);
                }
                if (forceDeviceSync) {
                    if (com.android.server.audio.AudioService.DEBUG_VOL) {
                        android.util.Log.d(com.android.server.audio.AudioService.TAG, "applyAllVolumes: forceDeviceSync index " + index2 + ", device " + android.media.AudioSystem.getOutputDeviceName(deviceForVolume) + ", group " + this.mAudioVolumeGroup.name());
                    }
                    setVolumeIndexInt(isMuted() ? 0 : index2, deviceForVolume, 0);
                }
            }
        }

        public void clearIndexCache() {
            this.mIndexMap.clear();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void persistVolumeGroup(int device) {
            boolean success;
            if (com.android.server.audio.AudioService.this.mUseFixedVolume || this.mHasValidStreamType) {
                return;
            }
            if (com.android.server.audio.AudioService.DEBUG_VOL) {
                android.util.Log.v(com.android.server.audio.AudioService.TAG, "persistVolumeGroup: storing index " + getIndex(device) + " for group " + this.mAudioVolumeGroup.name() + ", device " + android.media.AudioSystem.getOutputDeviceName(device) + " and User=" + com.android.server.audio.AudioService.this.getCurrentUserId() + " mSettingName: " + this.mSettingName);
            }
            int indexToPersist = getIndex(device);
            if (this.mIndexMax > 100) {
                indexToPersist |= 4096;
            }
            if (com.android.server.audio.AudioService.mVolumeNotShare) {
                success = com.android.server.audio.AudioService.this.mSettings.putSystemIntForUser(com.android.server.audio.AudioService.this.mContentResolver, getSettingNameForDevice(device), indexToPersist, -2);
            } else {
                success = com.android.server.audio.AudioService.this.mSettings.putSystemIntForUser(com.android.server.audio.AudioService.this.mContentResolver, getSettingNameForDevice(device), indexToPersist, isMusic() ? 0 : -2);
            }
            if (!success) {
                android.util.Log.e(com.android.server.audio.AudioService.TAG, "persistVolumeGroup failed for group " + this.mAudioVolumeGroup.name());
            }
        }

        public void readSettings() {
            int defaultIndex;
            int index;
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                if (com.android.server.audio.AudioService.this.mUseFixedVolume) {
                    this.mIndexMap.put(1073741824, this.mIndexMax);
                    return;
                }
                java.util.Iterator it = android.media.AudioSystem.DEVICE_OUT_ALL_SET.iterator();
                while (it.hasNext()) {
                    int device = ((java.lang.Integer) it.next()).intValue();
                    if (device != 1073741824) {
                        defaultIndex = -1;
                    } else {
                        defaultIndex = android.media.AudioSystem.DEFAULT_STREAM_VOLUME[this.mPublicStreamType];
                    }
                    java.lang.String name = getSettingNameForDevice(device);
                    if (com.android.server.audio.AudioService.mVolumeNotShare) {
                        index = com.android.server.audio.AudioService.this.mSettings.getSystemIntForUser(com.android.server.audio.AudioService.this.mContentResolver, name, defaultIndex, -2);
                    } else {
                        index = com.android.server.audio.AudioService.this.mSettings.getSystemIntForUser(com.android.server.audio.AudioService.this.mContentResolver, name, defaultIndex, isMusic() ? 0 : -2);
                    }
                    if (index != -1) {
                        if (this.mPublicStreamType == 7 && com.android.server.audio.AudioService.this.mCameraSoundForced) {
                            index = this.mIndexMax;
                        }
                        if (index != defaultIndex && this.mSettingName.equals(android.provider.Settings.System.VOLUME_SETTINGS_INT[3])) {
                            index = com.android.server.audio.AudioService.this.mAsExt.getValidVolumeIdxForTargetStreams(index, this.mPublicStreamType, false);
                        }
                        if (com.android.server.audio.AudioService.DEBUG_VOL) {
                            android.util.Log.v(com.android.server.audio.AudioService.TAG, "readSettings: found stored index " + getValidIndex(index) + " for group " + this.mAudioVolumeGroup.name() + ", device: " + name + ", User=" + com.android.server.audio.AudioService.this.getCurrentUserId());
                        }
                        this.mIndexMap.put(device, getValidIndex(index));
                    }
                }
            }
        }

        private int getValidIndex(int index) {
            if (index < this.mIndexMin) {
                return this.mIndexMin;
            }
            if (com.android.server.audio.AudioService.this.mUseFixedVolume || index > this.mIndexMax) {
                return this.mIndexMax;
            }
            return index;
        }

        public java.lang.String getSettingNameForDevice(int device) {
            java.lang.String suffix = android.media.AudioSystem.getOutputDeviceName(device);
            if (suffix.isEmpty()) {
                return this.mSettingName;
            }
            return this.mSettingName + "_" + android.media.AudioSystem.getOutputDeviceName(device);
        }

        void setSettingName(java.lang.String settingName) {
            this.mSettingName = settingName;
        }

        java.lang.String getSettingName() {
            return this.mSettingName;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(final java.io.PrintWriter pw) {
            pw.println("- VOLUME GROUP " + this.mAudioVolumeGroup.name() + ":");
            pw.print("   Muted: ");
            pw.println(this.mIsMuted);
            pw.print("   Min: ");
            pw.println(this.mIndexMin);
            pw.print("   Max: ");
            pw.println(this.mIndexMax);
            pw.print("   Current: ");
            for (int i = 0; i < this.mIndexMap.size(); i++) {
                if (i > 0) {
                    pw.print(", ");
                }
                int device = this.mIndexMap.keyAt(i);
                pw.print(java.lang.Integer.toHexString(device));
                java.lang.String deviceName = device == 1073741824 ? "default" : android.media.AudioSystem.getOutputDeviceName(device);
                if (!deviceName.isEmpty()) {
                    pw.print(" (");
                    pw.print(deviceName);
                    pw.print(")");
                }
                pw.print(": ");
                pw.print(this.mIndexMap.valueAt(i));
            }
            pw.println();
            pw.print("   Devices: ");
            int n = 0;
            int devices = getDeviceForVolume();
            java.util.Iterator it = android.media.AudioSystem.DEVICE_OUT_ALL_SET.iterator();
            while (it.hasNext()) {
                int device2 = ((java.lang.Integer) it.next()).intValue();
                if ((devices & device2) == device2) {
                    int n2 = n + 1;
                    if (n > 0) {
                        pw.print(", ");
                    }
                    pw.print(android.media.AudioSystem.getOutputDeviceName(device2));
                    n = n2;
                }
            }
            pw.println();
            pw.print("   Streams: ");
            java.util.Arrays.stream(getLegacyStreamTypes()).forEach(new java.util.function.IntConsumer() { // from class: com.android.server.audio.AudioService$VolumeGroupState$$ExternalSyntheticLambda0
                @Override // java.util.function.IntConsumer
                public final void accept(int i2) {
                    pw.print(android.media.AudioSystem.streamToString(i2) + " ");
                }
            });
        }
    }

    class VolumeStreamState {
        private final android.util.SparseIntArray mIndexMap;
        private int mIndexMax;
        private int mIndexMin;
        private int mIndexMinNoPerm;
        private boolean mIsMuted;
        private boolean mIsMutedInternally;
        private boolean mIsPhoneMuted;
        private final android.util.SparseIntArray mMediaVolumeIndexMap;
        private java.util.Set<java.lang.Integer> mObservedDeviceSet;
        private int mObservedDevices;
        private final android.content.Intent mStreamDevicesChanged;
        private final android.os.Bundle mStreamDevicesChangedOptions;
        private final int mStreamType;
        private final android.content.Intent mVolumeChanged;
        private final java.lang.Object mVolumeChangedLock;
        private final android.os.Bundle mVolumeChangedOptions;
        private com.android.server.audio.AudioService.VolumeGroupState mVolumeGroupState;
        private java.lang.String mVolumeIndexSettingName;
        private java.lang.String mVolumeIndexSettingNameForIsMuted;

        void storeVolume(int device, int index) {
            this.mMediaVolumeIndexMap.put(device, index);
        }

        private VolumeStreamState(java.lang.String settingName, int streamType) {
            int status;
            this.mVolumeGroupState = null;
            this.mIsMuted = false;
            this.mIsMutedInternally = false;
            this.mObservedDeviceSet = new java.util.TreeSet();
            this.mMediaVolumeIndexMap = new android.util.SparseIntArray(8);
            this.mIndexMap = new android.util.SparseIntArray(8) { // from class: com.android.server.audio.AudioService.VolumeStreamState.1
                @Override // android.util.SparseIntArray
                public void put(int key, int value) {
                    super.put(key, value);
                    record("put", key, value);
                }

                @Override // android.util.SparseIntArray
                public void setValueAt(int index, int value) {
                    super.setValueAt(index, value);
                    record("setValueAt", keyAt(index), value);
                }

                private void record(java.lang.String event, int key, int value) {
                    java.lang.String device = key == 1073741824 ? "default" : android.media.AudioSystem.getOutputDeviceName(key);
                    new android.media.MediaMetrics.Item("audio.volume." + android.media.AudioSystem.streamToString(com.android.server.audio.AudioService.VolumeStreamState.this.mStreamType) + "." + device).set(android.media.MediaMetrics.Property.EVENT, event).set(android.media.MediaMetrics.Property.INDEX, java.lang.Integer.valueOf(value)).set(android.media.MediaMetrics.Property.MIN_INDEX, java.lang.Integer.valueOf(com.android.server.audio.AudioService.VolumeStreamState.this.mIndexMin)).set(android.media.MediaMetrics.Property.MAX_INDEX, java.lang.Integer.valueOf(com.android.server.audio.AudioService.VolumeStreamState.this.mIndexMax)).record();
                }
            };
            this.mVolumeChangedLock = new java.lang.Object();
            this.mVolumeIndexSettingName = settingName;
            this.mVolumeIndexSettingNameForIsMuted = this.mVolumeIndexSettingName + "_muted";
            if (streamType == 3) {
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    android.provider.Settings.System.putIntForUser(com.android.server.audio.AudioService.this.mContentResolver, this.mVolumeIndexSettingNameForIsMuted, 0, -2);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }
            this.mStreamType = streamType;
            this.mIndexMin = com.android.server.audio.AudioService.MIN_STREAM_VOLUME[streamType] * 10;
            this.mIndexMinNoPerm = this.mIndexMin;
            this.mIndexMax = com.android.server.audio.AudioService.MAX_STREAM_VOLUME[streamType] * 10;
            if (this.mStreamType == 4) {
                status = android.media.AudioSystem.initStreamVolume(streamType, 0, this.mIndexMax / 10);
            } else {
                int status2 = this.mIndexMin;
                status = android.media.AudioSystem.initStreamVolume(streamType, status2 / 10, this.mIndexMax / 10);
            }
            if (status != 0) {
                com.android.server.audio.AudioService.sLifecycleLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("VSS() stream:" + streamType + " initStreamVolume=" + status).printLog(1, com.android.server.audio.AudioService.TAG));
                com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, 34, 1, 0, 0, "VSS()", 2000);
            }
            readSettings();
            this.mVolumeChanged = new android.content.Intent("android.media.VOLUME_CHANGED_ACTION");
            this.mVolumeChanged.putExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", this.mStreamType);
            android.app.BroadcastOptions volumeChangedOptions = android.app.BroadcastOptions.makeBasic();
            volumeChangedOptions.setDeliveryGroupPolicy(1);
            volumeChangedOptions.setDeliveryGroupMatchingKey("android.media.VOLUME_CHANGED_ACTION", java.lang.String.valueOf(this.mStreamType));
            volumeChangedOptions.setDeferralPolicy(2);
            this.mVolumeChangedOptions = volumeChangedOptions.toBundle();
            this.mStreamDevicesChanged = new android.content.Intent("android.media.STREAM_DEVICES_CHANGED_ACTION");
            this.mStreamDevicesChanged.putExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", this.mStreamType);
            android.app.BroadcastOptions streamDevicesChangedOptions = android.app.BroadcastOptions.makeBasic();
            streamDevicesChangedOptions.setDeliveryGroupPolicy(1);
            streamDevicesChangedOptions.setDeliveryGroupMatchingKey("android.media.STREAM_DEVICES_CHANGED_ACTION", java.lang.String.valueOf(this.mStreamType));
            streamDevicesChangedOptions.setDeferralPolicy(2);
            this.mStreamDevicesChangedOptions = streamDevicesChangedOptions.toBundle();
        }

        public void setVolumeGroupState(com.android.server.audio.AudioService.VolumeGroupState volumeGroupState) {
            this.mVolumeGroupState = volumeGroupState;
            if (this.mVolumeGroupState != null) {
                this.mVolumeGroupState.setSettingName(this.mVolumeIndexSettingName);
            }
        }

        public void updateNoPermMinIndex(int index) {
            this.mIndexMinNoPerm = index * 10;
            if (this.mIndexMinNoPerm < this.mIndexMin) {
                android.util.Log.e(com.android.server.audio.AudioService.TAG, "Invalid mIndexMinNoPerm for stream " + this.mStreamType);
                this.mIndexMinNoPerm = this.mIndexMin;
            }
        }

        public java.util.Set<java.lang.Integer> observeDevicesForStream_syncVSS(boolean checkOthers) {
            if (!com.android.server.audio.AudioService.this.mSystemServer.isPrivileged()) {
                return new java.util.TreeSet();
            }
            java.util.Set<java.lang.Integer> deviceSet = com.android.server.audio.AudioService.this.getDeviceSetForStreamDirect(this.mStreamType);
            if (deviceSet.equals(this.mObservedDeviceSet)) {
                return this.mObservedDeviceSet;
            }
            int devices = android.media.AudioSystem.getDeviceMaskFromSet(deviceSet);
            int prevDevices = android.media.AudioSystem.getDeviceMaskFromSet(this.mObservedDeviceSet);
            this.mObservedDeviceSet = deviceSet;
            if (checkOthers) {
                com.android.server.audio.AudioService.this.postObserveDevicesForAllStreams(this.mStreamType);
            }
            if (com.android.server.audio.AudioService.mStreamVolumeAlias[this.mStreamType] == this.mStreamType) {
                com.android.server.EventLogTags.writeStreamDevicesChanged(this.mStreamType, prevDevices, devices);
            }
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = this.mStreamDevicesChanged;
            args.arg2 = this.mStreamDevicesChangedOptions;
            com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, 32, 2, prevDevices, devices, args, 0);
            return this.mObservedDeviceSet;
        }

        public java.lang.String getSettingNameForDevice(int device) {
            if (!hasValidSettingsName()) {
                return null;
            }
            java.lang.String suffix = android.media.AudioSystem.getOutputDeviceName(device);
            if (suffix.isEmpty()) {
                return this.mVolumeIndexSettingName;
            }
            return this.mVolumeIndexSettingName + "_" + suffix;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hasValidSettingsName() {
            return (this.mVolumeIndexSettingName == null || this.mVolumeIndexSettingName.isEmpty()) ? false : true;
        }

        void setSettingName(java.lang.String settingName) {
            this.mVolumeIndexSettingName = settingName;
            if (this.mVolumeGroupState != null) {
                this.mVolumeGroupState.setSettingName(this.mVolumeIndexSettingName);
            }
        }

        java.lang.String getSettingName() {
            return this.mVolumeIndexSettingName;
        }

        public void readSettings() {
            int defaultIndex;
            int index;
            synchronized (com.android.server.audio.AudioService.this.mSettingsLock) {
                synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                    this.mIndexMap.clear();
                    if (this.mStreamType == 3) {
                        this.mIsMuted = com.android.server.audio.AudioService.this.mAsExt.getMuteStateForUser(this.mStreamType);
                    }
                    if (com.android.server.audio.AudioService.this.mUseFixedVolume) {
                        this.mIndexMap.put(1073741824, this.mIndexMax);
                        return;
                    }
                    if (this.mStreamType != 1 && this.mStreamType != 7) {
                        synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                            java.util.Iterator it = android.media.AudioSystem.DEVICE_OUT_ALL_SET.iterator();
                            while (it.hasNext()) {
                                int device = ((java.lang.Integer) it.next()).intValue();
                                if (device != 1073741824) {
                                    defaultIndex = -1;
                                } else {
                                    defaultIndex = android.media.AudioSystem.DEFAULT_STREAM_VOLUME[this.mStreamType];
                                }
                                if (!hasValidSettingsName()) {
                                    index = defaultIndex;
                                } else {
                                    java.lang.String name = getSettingNameForDevice(device);
                                    index = com.android.server.audio.AudioService.this.mSettings.getSystemIntForUser(com.android.server.audio.AudioService.this.mContentResolver, name, defaultIndex, -2);
                                }
                                if (index != -1) {
                                    if (index != defaultIndex && (this.mStreamType == 3 || com.android.server.audio.AudioService.mStreamVolumeAlias[this.mStreamType] == 3)) {
                                        index = com.android.server.audio.AudioService.this.mAsExt.getValidVolumeIdxForTargetStreams(index, this.mStreamType, false);
                                    }
                                    this.mIndexMap.put(device, getValidIndex(index * 10, true));
                                }
                            }
                        }
                        return;
                    }
                    int index2 = android.media.AudioSystem.DEFAULT_STREAM_VOLUME[this.mStreamType] * 10;
                    if (com.android.server.audio.AudioService.this.mCameraSoundForced) {
                        index2 = this.mIndexMax;
                    }
                    this.mIndexMap.put(1073741824, index2);
                }
            }
        }

        private int getAbsoluteVolumeIndex(int index) {
            if (com.android.media.audio.Flags.absVolumeIndexFix()) {
                return index;
            }
            if (index == 0) {
                return 0;
            }
            if (com.android.server.audio.AudioService.this.mAsExt.isOplusA2dpSmallVolumeCusEnable(index, this.mIndexMax)) {
                int index2 = com.android.server.audio.AudioService.this.mAsExt.getOplusA2dpSmallVolumeIndex(index, this.mIndexMax);
                if (com.android.server.audio.AudioService.DEBUG_VOL) {
                    android.util.Log.d(com.android.server.audio.AudioService.TAG, "getOplusA2dpSmallVolumeIndex :" + index2);
                    return index2;
                }
                return index2;
            }
            if (this.mIndexMax <= 1000 || index <= 0 || index > 30 || com.android.media.audio.Flags.disablePrescaleAbsoluteVolume()) {
                if (!com.android.media.audio.Flags.disablePrescaleAbsoluteVolume() && index > 0 && index <= 3) {
                    return ((int) (this.mIndexMax * com.android.server.audio.AudioService.this.mPrescaleAbsoluteVolume[index - 1])) / 10;
                }
                return (this.mIndexMax + 5) / 10;
            }
            int prescaleIndex = (index - 1) / 10;
            int upperTargetIndex = (int) (this.mIndexMax * com.android.server.audio.AudioService.this.mPrescaleAbsoluteVolume[prescaleIndex]);
            int lowerTargetIndex = prescaleIndex > 0 ? (int) (this.mIndexMax * com.android.server.audio.AudioService.this.mPrescaleAbsoluteVolume[prescaleIndex - 1]) : (int) (this.mIndexMax * (com.android.server.audio.AudioService.this.mPrescaleAbsoluteVolume[0] - 0.2f));
            int indexPerStep = (upperTargetIndex - lowerTargetIndex) / 10;
            return (((index - (prescaleIndex * 10)) * indexPerStep) + lowerTargetIndex) / 10;
        }

        private void setStreamVolumeIndex(int index, int device) {
            if (this.mStreamType == 6 && index == 0 && !isFullyMuted()) {
                index = 1;
            }
            if (com.android.server.audio.AudioService.DEBUG_VOL) {
                android.util.Log.d(com.android.server.audio.AudioService.TAG, "setStreamVolumeIndexAS(" + this.mStreamType + ", " + index + ", " + device + ")");
            }
            com.android.server.audio.AudioService.this.mAudioSystem.setStreamVolumeIndexAS(this.mStreamType, index, device);
            if (this.mStreamType == 2 && com.android.server.audio.AudioService.this.mRingerMode != 2 && com.android.server.audio.AudioService.this.mAsExt.isSuperVolumeSupported(device, this.mStreamType) && com.android.server.audio.AudioService.this.mAsExt.isStreamSuperVolumeOn(this.mStreamType)) {
                android.util.Log.d(com.android.server.audio.AudioService.TAG, "setRingerMode superVolume false mRingerMode " + com.android.server.audio.AudioService.this.mRingerMode);
                com.android.server.audio.AudioService.this.mAsExt.setSuperVolume(false, this.mStreamType, device);
            }
        }

        void applyDeviceVolume_syncVSS(int device) {
            int index;
            if (isFullyMuted()) {
                index = 0;
            } else if ((com.android.server.audio.AudioService.this.isAbsoluteVolumeDevice(device) || com.android.server.audio.AudioService.this.isA2dpAbsoluteVolumeDevice(device)) && this.mStreamType != 0 && this.mStreamType != 5 && this.mStreamType != 11) {
                if (com.android.media.audio.Flags.absVolumeIndexFix() && com.android.server.audio.AudioService.this.isAbsoluteVolumeDevice(device)) {
                    index = getAbsoluteVolumeIndex((this.mIndexMax + 5) / 10);
                } else {
                    int index2 = getIndex(device);
                    index = getAbsoluteVolumeIndex((index2 + 5) / 10);
                }
            } else if (android.media.AudioSystem.isLeAudioDeviceType(device) && com.android.server.audio.AudioService.this.mMode.get() != 2 && com.android.server.audio.AudioService.this.mMode.get() != 3 && this.mStreamType != 5 && this.mStreamType != 11 && com.android.server.audio.AudioService.this.mBleVcAbsVolSupported && this.mStreamType != 0 && this.mStreamType != 6) {
                index = getAbsoluteVolumeIndex((getIndex(device) + 5) / 10);
            } else if (com.android.server.audio.AudioService.this.isFullVolumeDevice(device)) {
                index = (this.mIndexMax + 5) / 10;
            } else if (device == 134217728) {
                if (com.android.media.audio.Flags.absVolumeIndexFix()) {
                    index = getAbsoluteVolumeIndex((getIndex(device) + 5) / 10);
                } else {
                    int index3 = this.mIndexMax;
                    index = (index3 + 5) / 10;
                }
            } else {
                int index4 = getIndex(device);
                index = (index4 + 5) / 10;
            }
            setStreamVolumeIndex(index, device);
        }

        public void applyAllVolumes() {
            int index;
            int index2;
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                boolean isAbsoluteVolume = false;
                for (int i = 0; i < this.mIndexMap.size(); i++) {
                    int device = this.mIndexMap.keyAt(i);
                    if (device != 1073741824) {
                        if (isFullyMuted()) {
                            index2 = 0;
                        } else if (com.android.server.audio.AudioService.this.isAbsoluteVolumeDevice(device) || com.android.server.audio.AudioService.this.isA2dpAbsoluteVolumeDevice(device) || android.media.AudioSystem.isLeAudioDeviceType(device)) {
                            isAbsoluteVolume = true;
                            if (android.media.AudioSystem.isLeAudioDeviceType(device) && com.android.server.audio.AudioService.this.mBleVcAbsVolSupported && ((com.android.server.audio.AudioService.this.mMode.get() == 2 || com.android.server.audio.AudioService.this.mMode.get() == 3) && this.mStreamType == 3)) {
                                index2 = (this.mIndexMap.valueAt(i) + 5) / 10;
                            } else if (com.android.media.audio.Flags.absVolumeIndexFix() && com.android.server.audio.AudioService.this.isAbsoluteVolumeDevice(device)) {
                                index2 = getAbsoluteVolumeIndex((this.mIndexMax + 5) / 10);
                            } else {
                                int index3 = getIndex(device);
                                index2 = getAbsoluteVolumeIndex((index3 + 5) / 10);
                            }
                        } else if (android.media.AudioSystem.DEVICE_OUT_ALL_BLE_SET.contains(java.lang.Integer.valueOf(device)) && com.android.server.audio.AudioService.this.mBleVcAbsVolSupported) {
                            isAbsoluteVolume = true;
                            index2 = getAbsoluteVolumeIndex((getIndex(device) + 5) / 10);
                        } else if (com.android.server.audio.AudioService.this.isFullVolumeDevice(device)) {
                            index2 = (this.mIndexMax + 5) / 10;
                        } else if (device == 134217728) {
                            if (com.android.media.audio.Flags.absVolumeIndexFix()) {
                                isAbsoluteVolume = true;
                                index2 = getAbsoluteVolumeIndex((getIndex(device) + 5) / 10);
                            } else {
                                int index4 = this.mIndexMax;
                                index2 = (index4 + 5) / 10;
                            }
                        } else {
                            index2 = (this.mIndexMap.valueAt(i) + 5) / 10;
                        }
                        com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, 1006, 0, device, isAbsoluteVolume ? 1 : 0, this, 0);
                        setStreamVolumeIndex(index2, device);
                    }
                }
                if (isFullyMuted()) {
                    index = 0;
                } else {
                    int index5 = getIndex(1073741824);
                    index = (index5 + 5) / 10;
                }
                setStreamVolumeIndex(index, 1073741824);
                if ((com.android.server.audio.AudioService.this.mUserSwitching || this.mStreamType == 3 || com.android.server.audio.AudioService.this.isStreamAffectedByRingerMode(this.mStreamType)) && !hasIndexForDevice(2)) {
                    setStreamVolumeIndex(index, 2);
                }
            }
        }

        public boolean adjustIndex(int deltaIndex, int device, java.lang.String caller, boolean hasModifyAudioSettings) {
            if (com.android.server.audio.AudioService.this.mAsExt.isSuperVolumeSupported(device, this.mStreamType)) {
                if (deltaIndex > 0 && getIndex(device) == this.mIndexMax && com.android.server.audio.AudioService.this.mAsExt.isTriggerByVolumeKey()) {
                    com.android.server.audio.AudioService.this.mAsExt.setSuperVolume(true, this.mStreamType, device);
                } else if (com.android.server.audio.AudioService.this.mAsExt.isStreamSuperVolumeOn(this.mStreamType)) {
                    com.android.server.audio.AudioService.this.mAsExt.setSuperVolume(false, this.mStreamType, device);
                    if (getIndex(device) == this.mIndexMax && deltaIndex < 0) {
                        return false;
                    }
                }
            }
            return setIndex(getIndex(device) + deltaIndex, device, caller, hasModifyAudioSettings);
        }

        /* JADX WARN: Removed duplicated region for block: B:140:0x01c6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:90:0x017a  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean setIndex(int r27, int r28, java.lang.String r29, boolean r30) throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 581
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.AudioService.VolumeStreamState.setIndex(int, int, java.lang.String, boolean):boolean");
        }

        public int getIndex(int device) {
            int index;
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                index = this.mIndexMap.get(device, -1);
                if (index == -1) {
                    index = this.mIndexMap.get(1073741824);
                }
            }
            return index;
        }

        public android.media.VolumeInfo getVolumeInfo(int device) {
            android.media.VolumeInfo vi;
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                int index = this.mIndexMap.get(device, -1);
                if (index == -1) {
                    index = this.mIndexMap.get(1073741824);
                }
                vi = new android.media.VolumeInfo.Builder(this.mStreamType).setMinVolumeIndex(this.mIndexMin).setMaxVolumeIndex(this.mIndexMax).setVolumeIndex(index).setMuted(isFullyMuted()).build();
            }
            return vi;
        }

        public boolean hasIndexForDevice(int device) {
            boolean z;
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                z = this.mIndexMap.get(device, -1) != -1;
            }
            return z;
        }

        public int getMaxIndex() {
            return this.mIndexMax;
        }

        public int getMinIndex() {
            return this.mIndexMin;
        }

        public int getMinIndex(boolean isPrivileged) {
            return isPrivileged ? this.mIndexMin : this.mIndexMinNoPerm;
        }

        public void setAllIndexes(com.android.server.audio.AudioService.VolumeStreamState srcStream, java.lang.String caller) throws java.lang.Throwable {
            if (this.mStreamType == srcStream.mStreamType) {
                return;
            }
            int srcStreamType = srcStream.getStreamType();
            int index = srcStream.getIndex(1073741824);
            int index2 = com.android.server.audio.AudioService.this.rescaleIndex(index, srcStreamType, this.mStreamType);
            for (int i = 0; i < this.mIndexMap.size(); i++) {
                this.mIndexMap.put(this.mIndexMap.keyAt(i), index2);
            }
            int ringSpeakerVolumeIndex = -1;
            if (this.mStreamType == 8 && srcStreamType == 2) {
                for (int i2 = 0; i2 < srcStream.mIndexMap.size(); i2++) {
                    if (2 == srcStream.mIndexMap.keyAt(i2)) {
                        ringSpeakerVolumeIndex = srcStream.mIndexMap.valueAt(i2);
                    }
                }
            }
            android.util.SparseIntArray srcMap = srcStream.mIndexMap;
            for (int i3 = 0; i3 < srcMap.size(); i3++) {
                int device = srcMap.keyAt(i3);
                int index3 = srcMap.valueAt(i3);
                if (ringSpeakerVolumeIndex != -1) {
                    index3 = ringSpeakerVolumeIndex;
                }
                setIndex(com.android.server.audio.AudioService.this.rescaleIndex(index3, srcStreamType, this.mStreamType), device, caller, true);
            }
        }

        public void setAllIndexesToMax() {
            for (int i = 0; i < this.mIndexMap.size(); i++) {
                this.mIndexMap.put(this.mIndexMap.keyAt(i), this.mIndexMax);
            }
        }

        private void updateVolumeGroupIndex(int device, boolean forceMuteState) {
            boolean z;
            synchronized (com.android.server.audio.AudioService.this.mSettingsLock) {
                synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                    if (this.mVolumeGroupState != null) {
                        int groupIndex = (getIndex(device) + 5) / 10;
                        if (com.android.server.audio.AudioService.DEBUG_VOL) {
                            android.util.Log.d(com.android.server.audio.AudioService.TAG, "updateVolumeGroupIndex for stream " + this.mStreamType + ", muted=" + this.mIsMuted + ", device=" + device + ", index=" + getIndex(device) + ", group " + this.mVolumeGroupState.name() + " Muted=" + this.mVolumeGroupState.isMuted() + ", Index=" + groupIndex + ", forceMuteState=" + forceMuteState);
                        }
                        this.mVolumeGroupState.updateVolumeIndex(groupIndex, device);
                        if (isMutable()) {
                            com.android.server.audio.AudioService.VolumeGroupState volumeGroupState = this.mVolumeGroupState;
                            if (forceMuteState) {
                                z = this.mIsMuted;
                            } else {
                                z = (groupIndex == 0 && !com.android.server.audio.AudioService.isCallStream(this.mStreamType)) || this.mIsMuted;
                            }
                            volumeGroupState.mute(z);
                        }
                    }
                }
            }
        }

        public boolean mute(boolean state, java.lang.String source) {
            boolean changed;
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                changed = mute(state, true, source);
            }
            if (changed) {
                com.android.server.audio.AudioService.this.broadcastMuteSetting(this.mStreamType, state);
            }
            return changed;
        }

        public boolean muteInternally(boolean state) {
            boolean changed = false;
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                if (state != this.mIsMutedInternally) {
                    changed = true;
                    this.mIsMutedInternally = state;
                    applyAllVolumes();
                }
            }
            if (changed) {
                com.android.server.audio.AudioService.sVolumeLogger.enqueue(new com.android.server.audio.AudioServiceEvents.VolumeEvent(9, this.mStreamType, state));
            }
            return changed;
        }

        public boolean isFullyMuted() {
            return this.mIsMuted || this.mIsMutedInternally || this.mIsPhoneMuted;
        }

        public boolean muteForPhone(boolean state) {
            boolean changed = false;
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                if (state != this.mIsPhoneMuted) {
                    changed = true;
                    this.mIsPhoneMuted = state;
                    com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, 10, 2, 0, 0, this, 0);
                }
            }
            return changed;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isMutable() {
            return com.android.server.audio.AudioService.this.isStreamAffectedByMute(this.mStreamType) && (this.mIndexMin == 0 || com.android.server.audio.AudioService.isCallStream(this.mStreamType));
        }

        public boolean mute(boolean state, boolean apply, java.lang.String src) {
            boolean changed;
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                changed = state != this.mIsMuted;
                if (changed) {
                    com.android.server.audio.AudioService.sMuteLogger.enqueue(new com.android.server.audio.AudioServiceEvents.StreamMuteEvent(this.mStreamType, state, src));
                    if (!state && com.android.server.audio.AudioService.this.isStreamMutedByRingerOrZenMode(this.mStreamType)) {
                        android.util.Log.e(com.android.server.audio.AudioService.TAG, "Unmuting stream " + this.mStreamType + " despite ringer-zen muted stream 0x" + java.lang.Integer.toHexString(com.android.server.audio.AudioService.sRingerAndZenModeMutedStreams), new java.lang.Exception());
                        com.android.server.audio.AudioService.sMuteLogger.enqueue(new com.android.server.audio.AudioServiceEvents.StreamUnmuteErrorEvent(this.mStreamType, com.android.server.audio.AudioService.sRingerAndZenModeMutedStreams));
                    }
                    this.mIsMuted = state;
                    if (this.mStreamType == 3) {
                        com.android.server.audio.AudioService.this.mAsExt.putMuteStateForUser(this.mStreamType, this.mIsMuted);
                    }
                    if (apply) {
                        doMute();
                    }
                }
            }
            return changed;
        }

        public void doMute() {
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                updateVolumeGroupIndex(com.android.server.audio.AudioService.this.getDeviceForStream(this.mStreamType), true);
                com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, 10, 2, 0, 0, this, 0);
            }
        }

        public int getStreamType() {
            return this.mStreamType;
        }

        public void checkFixedVolumeDevices() {
            synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                if (com.android.server.audio.AudioService.mStreamVolumeAlias[this.mStreamType] == 3) {
                    for (int i = 0; i < this.mIndexMap.size(); i++) {
                        int device = this.mIndexMap.keyAt(i);
                        int index = this.mIndexMap.valueAt(i);
                        if (com.android.server.audio.AudioService.this.isFullVolumeDevice(device) || (com.android.server.audio.AudioService.this.isFixedVolumeDevice(device) && index != 0)) {
                            this.mIndexMap.put(device, this.mIndexMax);
                        }
                        applyDeviceVolume_syncVSS(device);
                    }
                }
            }
        }

        private int getValidIndex(int index, boolean hasModifyAudioSettings) {
            int indexMin = hasModifyAudioSettings ? this.mIndexMin : this.mIndexMinNoPerm;
            if (index < indexMin) {
                return indexMin;
            }
            if (com.android.server.audio.AudioService.this.mUseFixedVolume || index > this.mIndexMax) {
                return this.mIndexMax;
            }
            return index;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(java.io.PrintWriter pw) {
            pw.print("   Muted: ");
            pw.println(this.mIsMuted);
            pw.print("   Muted Internally: ");
            pw.println(this.mIsMutedInternally);
            pw.print("   Min: ");
            pw.print((this.mIndexMin + 5) / 10);
            if (this.mIndexMin != this.mIndexMinNoPerm) {
                pw.print(" w/o perm:");
                pw.println((this.mIndexMinNoPerm + 5) / 10);
            } else {
                pw.println();
            }
            pw.print("   Max: ");
            pw.println((this.mIndexMax + 5) / 10);
            pw.print("   streamVolume:");
            pw.println(com.android.server.audio.AudioService.this.getStreamVolume(this.mStreamType));
            pw.print("   Current: ");
            for (int i = 0; i < this.mIndexMap.size(); i++) {
                if (i > 0) {
                    pw.print(", ");
                }
                int device = this.mIndexMap.keyAt(i);
                pw.print(java.lang.Integer.toHexString(device));
                java.lang.String deviceName = device == 1073741824 ? "default" : android.media.AudioSystem.getOutputDeviceName(device);
                if (!deviceName.isEmpty()) {
                    pw.print(" (");
                    pw.print(deviceName);
                    pw.print(")");
                }
                pw.print(": ");
                int index = (this.mIndexMap.valueAt(i) + 5) / 10;
                pw.print(index);
            }
            pw.println();
            pw.print("   Devices: ");
            pw.print(android.media.AudioSystem.deviceSetToString(com.android.server.audio.AudioService.this.getDeviceSetForStream(this.mStreamType)));
            pw.println();
            pw.print("   Volume Group: ");
            pw.println(this.mVolumeGroupState != null ? this.mVolumeGroupState.name() : "n/a");
        }
    }

    private class AudioSystemThread extends java.lang.Thread {
        AudioSystemThread() {
            super("AudioService");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            android.os.Looper.prepare();
            synchronized (com.android.server.audio.AudioService.this) {
                com.android.server.audio.AudioService.this.mAudioHandler = com.android.server.audio.AudioService.this.new AudioHandler();
                com.android.server.audio.AudioService.this.notify();
            }
            android.os.Looper.loop();
        }
    }

    private static final class DeviceVolumeUpdate {
        private static final int NO_NEW_INDEX = -2049;
        final java.lang.String mCaller;
        final int mDevice;
        final int mStreamType;
        private final int mVssVolIndex;

        DeviceVolumeUpdate(int streamType, int vssVolIndex, int device, java.lang.String caller) {
            this.mStreamType = streamType;
            this.mVssVolIndex = vssVolIndex;
            this.mDevice = device;
            this.mCaller = caller;
        }

        DeviceVolumeUpdate(int streamType, int device, java.lang.String caller) {
            this.mStreamType = streamType;
            this.mVssVolIndex = NO_NEW_INDEX;
            this.mDevice = device;
            this.mCaller = caller;
        }

        boolean hasVolumeIndex() {
            return this.mVssVolIndex != NO_NEW_INDEX;
        }

        int getVolumeIndex() throws java.lang.IllegalStateException {
            com.android.internal.util.Preconditions.checkState(this.mVssVolIndex != NO_NEW_INDEX);
            return this.mVssVolIndex;
        }
    }

    public void postSetVolumeIndexOnDevice(int streamType, int vssVolIndex, int device, java.lang.String caller) {
        sendMsg(this.mAudioHandler, 26, 2, 0, 0, new com.android.server.audio.AudioService.DeviceVolumeUpdate(streamType, vssVolIndex, device, caller), 0);
    }

    void postApplyVolumeOnDevice(int streamType, int device, java.lang.String caller) {
        sendMsg(this.mAudioHandler, 26, 2, 0, 0, new com.android.server.audio.AudioService.DeviceVolumeUpdate(streamType, device, caller), 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSetVolumeIndexOnDevice(com.android.server.audio.AudioService.DeviceVolumeUpdate update) throws java.lang.Throwable {
        com.android.server.audio.AudioService.VolumeStreamState streamState = this.mStreamStates[update.mStreamType];
        if (update.hasVolumeIndex()) {
            int index = update.getVolumeIndex();
            if (this.mSoundDoseHelper.checkSafeMediaVolume(update.mStreamType, index, update.mDevice)) {
                index = this.mSoundDoseHelper.safeMediaVolumeIndex(update.mDevice);
            }
            streamState.setIndex(index, update.mDevice, update.mCaller, true);
            sVolumeLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(update.mCaller + " dev:0x" + java.lang.Integer.toHexString(update.mDevice) + " volIdx:" + index));
        } else {
            sVolumeLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(update.mCaller + " update vol on dev:0x" + java.lang.Integer.toHexString(update.mDevice)));
        }
        setDeviceVolume(streamState, update.mDevice);
    }

    void setDeviceVolume(com.android.server.audio.AudioService.VolumeStreamState streamState, int device) {
        int mode = getMode();
        boolean speakerOn = isSpeakerphoneOn();
        if (this.mAsExt.getPrivacyCallSupport() && ((device == 1 || device == 8388608) && streamState.getStreamType() == 0)) {
            sendMsg(this.mAudioHandler, 112, 0, 0, device, null, 500);
        }
        synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
            sendMsg(this.mAudioHandler, 1006, 0, device, (isAbsoluteVolumeDevice(device) || isA2dpAbsoluteVolumeDevice(device) || android.media.AudioSystem.isLeAudioDeviceType(device)) ? 1 : 0, streamState, 0);
            if (device == 1 && streamState.getStreamType() == 0 && mode == 2 && speakerOn) {
                device = getDeviceForStream(streamState.getStreamType());
            }
            streamState.applyDeviceVolume_syncVSS(device);
            int numStreamTypes = android.media.AudioSystem.getNumStreamTypes();
            for (int streamType = numStreamTypes - 1; streamType >= 0; streamType--) {
                if (streamType != streamState.mStreamType && mStreamVolumeAlias[streamType] == streamState.mStreamType) {
                    int streamDevice = getDeviceForStream(streamType);
                    if (device != streamDevice && (isAbsoluteVolumeDevice(device) || isA2dpAbsoluteVolumeDevice(device) || android.media.AudioSystem.isLeAudioDeviceType(device) || (streamState.mStreamType == 2 && device == 2))) {
                        this.mStreamStates[streamType].applyDeviceVolume_syncVSS(device);
                    }
                    this.mStreamStates[streamType].applyDeviceVolume_syncVSS(streamDevice);
                }
            }
        }
        sendMsg(this.mAudioHandler, 1, 2, device, 0, streamState, 500);
    }

    class AudioHandler extends android.os.Handler {
        AudioHandler() {
        }

        AudioHandler(android.os.Looper looper) {
            super(looper);
        }

        private void setAllVolumes(com.android.server.audio.AudioService.VolumeStreamState streamState) {
            streamState.applyAllVolumes();
            int numStreamTypes = android.media.AudioSystem.getNumStreamTypes();
            for (int streamType = numStreamTypes - 1; streamType >= 0; streamType--) {
                if (streamType != streamState.mStreamType && com.android.server.audio.AudioService.mStreamVolumeAlias[streamType] == streamState.mStreamType) {
                    com.android.server.audio.AudioService.this.mStreamStates[streamType].applyAllVolumes();
                }
            }
        }

        private void persistVolume(com.android.server.audio.AudioService.VolumeStreamState streamState, int device) {
            if (com.android.server.audio.AudioService.this.mUseFixedVolume) {
                return;
            }
            if ((!com.android.server.audio.AudioService.this.mIsSingleVolume || streamState.mStreamType == 3) && streamState.mStreamType != 7 && streamState.hasValidSettingsName()) {
                int indexToPersist = (streamState.getIndex(device) + 5) / 10;
                if (streamState.getMaxIndex() > 1000) {
                    indexToPersist |= 4096;
                }
                com.android.server.audio.AudioService.this.mSettings.putSystemIntForUser(com.android.server.audio.AudioService.this.mContentResolver, streamState.getSettingNameForDevice(device), indexToPersist, -2);
            }
        }

        private void persistRingerMode(int ringerMode) {
            if (com.android.server.audio.AudioService.this.mUseFixedVolume) {
                return;
            }
            com.android.server.audio.AudioService.this.mSettings.putGlobalInt(com.android.server.audio.AudioService.this.mContentResolver, "mode_ringer", ringerMode);
            android.provider.Settings.System.putIntForUser(com.android.server.audio.AudioService.this.mContentResolver, com.android.server.audio.IAudioServiceExt.OPLUS_MODE_RINGER, ringerMode, -2);
        }

        private void onNotifyVolumeEvent(android.media.audiopolicy.IAudioPolicyCallback apc, int direction) {
            try {
                apc.notifyVolumeAdjust(direction);
            } catch (java.lang.Exception e) {
            }
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) throws java.lang.Throwable {
            switch (msg.what) {
                case 0:
                    com.android.server.audio.AudioService.this.setDeviceVolume((com.android.server.audio.AudioService.VolumeStreamState) msg.obj, msg.arg1);
                    return;
                case 1:
                    persistVolume((com.android.server.audio.AudioService.VolumeStreamState) msg.obj, msg.arg1);
                    return;
                case 2:
                    com.android.server.audio.AudioService.VolumeGroupState vgs = (com.android.server.audio.AudioService.VolumeGroupState) msg.obj;
                    vgs.persistVolumeGroup(msg.arg1);
                    return;
                case 3:
                    persistRingerMode(com.android.server.audio.AudioService.this.getRingerModeInternal());
                    return;
                case 4:
                    com.android.server.audio.AudioService.this.onAudioServerDied();
                    return;
                case 5:
                    com.android.server.audio.AudioService.this.mSfxHelper.playSoundEffect(msg.arg1, msg.arg2);
                    return;
                case 7:
                    com.android.server.audio.AudioService.LoadSoundEffectReply reply = (com.android.server.audio.AudioService.LoadSoundEffectReply) msg.obj;
                    if (com.android.server.audio.AudioService.this.mSystemReady) {
                        com.android.server.audio.AudioService.this.mSfxHelper.loadSoundEffects(reply);
                        return;
                    }
                    android.util.Log.w(com.android.server.audio.AudioService.TAG, "[schedule]loadSoundEffects() called before boot complete");
                    if (reply != null) {
                        reply.run(false);
                        return;
                    }
                    return;
                case 8:
                    java.lang.String eventSource = (java.lang.String) msg.obj;
                    int useCase = msg.arg1;
                    int config = msg.arg2;
                    if (useCase == 1) {
                        android.util.Log.wtf(com.android.server.audio.AudioService.TAG, "Invalid force use FOR_MEDIA in AudioService from " + eventSource);
                        return;
                    }
                    new android.media.MediaMetrics.Item("audio.forceUse." + android.media.AudioSystem.forceUseUsageToString(useCase)).set(android.media.MediaMetrics.Property.EVENT, "setForceUse").set(android.media.MediaMetrics.Property.FORCE_USE_DUE_TO, eventSource).set(android.media.MediaMetrics.Property.FORCE_USE_MODE, android.media.AudioSystem.forceUseConfigToString(config)).record();
                    com.android.server.audio.AudioService.sForceUseLogger.enqueue(new com.android.server.audio.AudioServiceEvents.ForceUseEvent(useCase, config, eventSource));
                    com.android.server.audio.AudioService.this.mAudioSystem.setForceUse(useCase, config);
                    return;
                case 10:
                    setAllVolumes((com.android.server.audio.AudioService.VolumeStreamState) msg.obj);
                    return;
                case 15:
                    com.android.server.audio.AudioService.this.mSfxHelper.unloadSoundEffects();
                    return;
                case 16:
                    com.android.server.audio.AudioService.this.onSystemReady();
                    return;
                case 18:
                    com.android.server.audio.AudioService.this.onUnmuteStreamOnSingleVolDevice(msg.arg1, msg.arg2);
                    return;
                case 19:
                    com.android.server.audio.AudioService.this.onDynPolicyMixStateUpdate((java.lang.String) msg.obj, msg.arg1);
                    return;
                case 20:
                    com.android.server.audio.AudioService.this.onIndicateSystemReady();
                    return;
                case 21:
                    com.android.server.audio.AudioService.this.onAccessoryPlugMediaUnmute(msg.arg1);
                    return;
                case 22:
                    onNotifyVolumeEvent((android.media.audiopolicy.IAudioPolicyCallback) msg.obj, msg.arg1);
                    return;
                case 23:
                    com.android.server.audio.AudioService.this.onDispatchAudioServerStateChange(msg.arg1 == 1);
                    return;
                case 24:
                    com.android.server.audio.AudioService.this.onEnableSurroundFormats((java.util.ArrayList) msg.obj);
                    return;
                case 25:
                    com.android.server.audio.AudioService.this.onUpdateRingerModeServiceInt();
                    return;
                case 26:
                    com.android.server.audio.AudioService.this.onSetVolumeIndexOnDevice((com.android.server.audio.AudioService.DeviceVolumeUpdate) msg.obj);
                    return;
                case 27:
                    com.android.server.audio.AudioService.this.onObserveDevicesForAllStreams(msg.arg1);
                    return;
                case 28:
                    com.android.server.audio.AudioService.this.onCheckVolumeCecOnHdmiConnection(msg.arg1, (java.lang.String) msg.obj);
                    return;
                case 29:
                    com.android.server.audio.AudioService.this.onPlaybackConfigChange((java.util.List) msg.obj);
                    return;
                case 30:
                    com.android.server.audio.AudioService.this.mSystemServer.sendMicrophoneMuteChangedIntent();
                    return;
                case 31:
                    synchronized (com.android.server.audio.AudioService.this.mDeviceBroker.mSetModeLock) {
                        if (msg.obj != null) {
                            com.android.server.audio.AudioService.SetModeDeathHandler h = (com.android.server.audio.AudioService.SetModeDeathHandler) msg.obj;
                            if (com.android.server.audio.AudioService.this.mSetModeDeathHandlers.indexOf(h) >= 0) {
                                boolean wasActive = h.isActive();
                                h.setPlaybackActive(com.android.server.audio.AudioService.this.isPlaybackActiveForUid(h.getUid()));
                                h.setRecordingActive(com.android.server.audio.AudioService.this.isRecordingActiveForUid(h.getUid()));
                                if (wasActive != h.isActive()) {
                                    com.android.server.audio.AudioService.this.onUpdateAudioMode(-1, android.os.Process.myPid(), com.android.server.audio.AudioService.this.mContext.getPackageName(), false);
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    }
                case 32:
                    com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) msg.obj;
                    android.content.Intent intent = (android.content.Intent) args.arg1;
                    android.os.Bundle options = (android.os.Bundle) args.arg2;
                    args.recycle();
                    com.android.server.audio.AudioService.this.sendBroadcastToAll(intent.putExtra("android.media.EXTRA_PREV_VOLUME_STREAM_DEVICES", msg.arg1).putExtra("android.media.EXTRA_VOLUME_STREAM_DEVICES", msg.arg2), options);
                    return;
                case 33:
                    com.android.server.audio.AudioService.this.onUpdateVolumeStatesForAudioDevice(msg.arg1, (java.lang.String) msg.obj);
                    return;
                case 34:
                    com.android.server.audio.AudioService.this.onReinitVolumes((java.lang.String) msg.obj);
                    return;
                case 35:
                    com.android.server.audio.AudioService.this.onUpdateAccessibilityServiceUids();
                    return;
                case 36:
                    synchronized (com.android.server.audio.AudioService.this.mDeviceBroker.mSetModeLock) {
                        com.android.server.audio.AudioService.this.onUpdateAudioMode(msg.arg1, msg.arg2, (java.lang.String) msg.obj, false);
                        break;
                    }
                    return;
                case 37:
                    com.android.server.audio.AudioService.this.onRecordingConfigChange((java.util.List) msg.obj);
                    return;
                case 38:
                    com.android.server.audio.AudioService.this.mDeviceBroker.queueOnBluetoothActiveDeviceChanged((com.android.server.audio.AudioDeviceBroker.BtDeviceChangedData) msg.obj);
                    return;
                case 40:
                    com.android.server.audio.AudioService.this.dispatchMode(msg.arg1);
                    return;
                case 41:
                    com.android.server.audio.AudioService.this.onRoutingUpdatedFromAudioThread();
                    return;
                case 42:
                    com.android.server.audio.AudioService.this.mSpatializerHelper.onInitSensors();
                    return;
                case 44:
                    com.android.server.audio.AudioService.this.onAddAssistantServiceUids(new int[]{msg.arg1});
                    return;
                case 45:
                    com.android.server.audio.AudioService.this.onRemoveAssistantServiceUids(new int[]{msg.arg1});
                    return;
                case 46:
                    com.android.server.audio.AudioService.this.updateActiveAssistantServiceUids();
                    return;
                case 47:
                    com.android.server.audio.AudioService.this.dispatchDeviceVolumeBehavior((android.media.AudioDeviceAttributes) msg.obj, msg.arg1);
                    return;
                case 48:
                    com.android.server.audio.AudioService.this.mAudioSystem.setParameters((java.lang.String) msg.obj);
                    return;
                case 49:
                    com.android.server.audio.AudioService.this.mAudioSystem.setParameters((java.lang.String) msg.obj);
                    return;
                case 50:
                    com.android.server.audio.AudioService.this.mSpatializerHelper.reset(com.android.server.audio.AudioService.this.mHasSpatializerEffect);
                    return;
                case 51:
                    com.android.server.audio.AudioService.this.mPlaybackMonitor.ignorePlayerIId(msg.arg1);
                    return;
                case 52:
                    com.android.server.audio.AudioService.this.onDispatchPreferredMixerAttributesChanged(msg.getData(), msg.arg1);
                    return;
                case 54:
                    com.android.server.audio.AudioService.this.onConfigurationChanged();
                    return;
                case 55:
                    com.android.server.audio.AudioService.this.mSystemServer.broadcastMasterMuteStatus(msg.arg1 == 1);
                    return;
                case 61:
                    com.android.server.audio.AudioService.this.mAsExt.onOplusRestoreVolumeBeforeSafeMediaVolume();
                    return;
                case 62:
                    android.provider.Settings.Global.putString(com.android.server.audio.AudioService.this.mContentResolver, com.android.server.audio.IAudioServiceExt.PHONE_MUTE_STATE_FOR_CUSTOM, msg.arg1 != 0 ? "true" : "false");
                    android.util.Log.d(com.android.server.audio.AudioService.TAG, "mutephone receive:" + (msg.arg1 != 0));
                    return;
                case 63:
                    if (com.android.server.audio.AudioService.this.mAsExt.isAlertSliderSupported()) {
                        com.android.server.audio.AudioService.this.mAsExt.onStorePreMediaVolume(msg.arg1);
                        return;
                    }
                    return;
                case 64:
                    if (com.android.server.audio.AudioService.this.mAsExt.isSuperVolumeSupported()) {
                        com.android.server.audio.AudioService.this.mAsExt.persistSuperVolume(msg.arg1, (java.lang.String) msg.obj);
                        return;
                    }
                    return;
                case 80:
                    android.media.AudioSystem.setParameters("binaural_recording_switch=2");
                    return;
                case 81:
                    android.media.AudioSystem.setParameters("binaural_recording_switch=3");
                    return;
                case 83:
                    android.util.Log.d(com.android.server.audio.AudioService.TAG, "mMicMuteFromSystemCached = " + com.android.server.audio.AudioService.this.mMicMuteFromSystemCached);
                    if (com.android.server.audio.AudioService.this.mMicMuteFromSystemCached && !com.android.server.audio.AudioService.this.isInCommunication()) {
                        com.android.server.audio.AudioService.this.mAsExt.checkAndClearMicMuteEvent(msg.arg1);
                        return;
                    }
                    return;
                case 100:
                    com.android.server.audio.AudioService.this.mPlaybackMonitor.disableAudioForUid(msg.arg1 == 1, msg.arg2);
                    com.android.server.audio.AudioService.this.mAudioEventWakeLock.release();
                    return;
                case 101:
                    com.android.server.audio.AudioService.this.onInitStreamsAndVolumes();
                    com.android.server.audio.AudioService.this.mAudioEventWakeLock.release();
                    return;
                case 102:
                    com.android.server.audio.AudioService.this.onInitSpatializer();
                    com.android.server.audio.AudioService.this.mDeviceBroker.postSynchronizeAdiDevicesInInventory(null);
                    com.android.server.audio.AudioService.this.mAudioEventWakeLock.release();
                    return;
                case 103:
                    com.android.server.audio.AudioService.this.onInitAdiDeviceStates();
                    com.android.server.audio.AudioService.this.mAudioEventWakeLock.release();
                    return;
                case 112:
                    com.android.server.audio.AudioService.this.mAsExt.oplusCheckPrivacyCall(msg.arg2, 0, 0);
                    return;
                case 113:
                    com.android.server.audio.AudioService.this.mAsExt.oplusCheckPrivacyCall(msg.arg2, 0, 1);
                    return;
                case 114:
                    android.os.Bundle result = msg.getData();
                    com.android.server.audio.AudioService.this.setOriginalMode(result.getInt(com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY), result.getBinder("binder"), result.getString("packagename"), msg.arg1, msg.arg2, result.getBoolean("flag"));
                    return;
                case 116:
                    com.android.server.audio.AudioService.this.mAsExt.oplusCheckVocalProminence(msg.arg2, 0, 0);
                    return;
                case 117:
                    com.android.server.audio.AudioService.this.mAsExt.oplusCheckVocalProminence(msg.arg2, 0, 1);
                    return;
                case 118:
                    android.media.AudioSystem.setParameters(com.android.server.audio.AudioService.HOLO_PARAM_UPDATE_METADATA);
                    return;
                case 119:
                    com.android.server.audio.AudioService.this.mAsExt.checkPrivacyCallSoftwareMode(msg.arg2, 0, 1);
                    return;
                case 120:
                    com.android.server.audio.AudioService.this.mAsExt.setPrivacyCallSoftwareModeOn(msg.arg1, msg.arg2 == 1);
                    return;
                case com.android.server.audio.AudioService.MSG_PREDISPATCH_AUDIO_MODE /* 888 */:
                    com.android.server.audio.AudioService.this.preDispatchMode(msg.arg1);
                    return;
                case 1001:
                case 1002:
                case 1003:
                case 1004:
                case 1005:
                case 1006:
                case 1007:
                    com.android.server.audio.AudioService.this.mSoundDoseHelper.handleMessage(msg);
                    return;
                case 1101:
                    com.android.server.audio.AudioService.this.mMusicFxHelper.handleMessage(msg);
                    return;
                default:
                    android.util.Log.e(com.android.server.audio.AudioService.TAG, "Unsupported msgId " + msg.what);
                    return;
            }
        }
    }

    private class SettingsObserver extends android.database.ContentObserver {
        SettingsObserver() {
            super(new android.os.Handler());
            com.android.server.audio.AudioService.this.mContentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("zen_mode"), false, this);
            com.android.server.audio.AudioService.this.mContentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("zen_mode_config_etag"), false, this);
            com.android.server.audio.AudioService.this.mContentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("mute_alarm_stream_with_ringer_mode"), false, this);
            com.android.server.audio.AudioService.this.mContentResolver.registerContentObserver(android.provider.Settings.System.getUriFor("mode_ringer_streams_affected"), false, this);
            com.android.server.audio.AudioService.this.mContentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("dock_audio_media_enabled"), false, this);
            com.android.server.audio.AudioService.this.mContentResolver.registerContentObserver(android.provider.Settings.System.getUriFor("master_mono"), false, this, -1);
            com.android.server.audio.AudioService.this.mContentResolver.registerContentObserver(android.provider.Settings.System.getUriFor("master_balance"), false, this, -1);
            com.android.server.audio.AudioService.this.mEncodedSurroundMode = com.android.server.audio.AudioService.this.mSettings.getGlobalInt(com.android.server.audio.AudioService.this.mContentResolver, "encoded_surround_output", 0);
            com.android.server.audio.AudioService.this.mContentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("encoded_surround_output"), false, this);
            com.android.server.audio.AudioService.this.mEnabledSurroundFormats = com.android.server.audio.AudioService.this.mSettings.getGlobalString(com.android.server.audio.AudioService.this.mContentResolver, "encoded_surround_output_enabled_formats");
            com.android.server.audio.AudioService.this.mContentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("encoded_surround_output_enabled_formats"), false, this);
            com.android.server.audio.AudioService.this.mContentResolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("voice_interaction_service"), false, this);
            com.android.server.audio.AudioService.this.mContentResolver.registerContentObserver(android.provider.Settings.Secure.getUriFor(com.android.server.audio.IAudioServiceExt.AUDIO_INPUT_CHANNEL), false, this);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            synchronized (com.android.server.audio.AudioService.this.mSettingsLock) {
                if (com.android.server.audio.AudioService.this.updateRingerAndZenModeAffectedStreams()) {
                    com.android.server.audio.AudioService.this.setRingerModeInt(com.android.server.audio.AudioService.this.getRingerModeInternal(), false);
                }
                com.android.server.audio.AudioService.this.readDockAudioSettings(com.android.server.audio.AudioService.this.mContentResolver);
                com.android.server.audio.AudioService.this.updateMasterMono(com.android.server.audio.AudioService.this.mContentResolver);
                com.android.server.audio.AudioService.this.updateMasterBalance(com.android.server.audio.AudioService.this.mContentResolver);
                updateEncodedSurroundOutput();
                com.android.server.audio.AudioService.this.sendEnabledSurroundFormats(com.android.server.audio.AudioService.this.mContentResolver, com.android.server.audio.AudioService.this.mSurroundModeChanged);
                com.android.server.audio.AudioService.this.updateAssistantUIdLocked(false);
                com.android.server.audio.AudioService.this.mAsExt.updateInputDevice(com.android.server.audio.AudioService.this.mContentResolver);
            }
        }

        private void updateEncodedSurroundOutput() {
            int newSurroundMode = com.android.server.audio.AudioService.this.mSettings.getGlobalInt(com.android.server.audio.AudioService.this.mContentResolver, "encoded_surround_output", 0);
            if (com.android.server.audio.AudioService.this.mEncodedSurroundMode != newSurroundMode) {
                com.android.server.audio.AudioService.this.sendEncodedSurroundMode(newSurroundMode, "SettingsObserver");
                com.android.server.audio.AudioService.this.mDeviceBroker.toggleHdmiIfConnected_Async();
                com.android.server.audio.AudioService.this.mEncodedSurroundMode = newSurroundMode;
                com.android.server.audio.AudioService.this.mSurroundModeChanged = true;
                return;
            }
            com.android.server.audio.AudioService.this.mSurroundModeChanged = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void avrcpSupportsAbsoluteVolume(java.lang.String address, boolean support) {
        sVolumeLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("support=" + support).printLog(TAG));
        this.mDeviceBroker.getWrapper().getExtImpl().setAvrcpAbsoluteVolumeSupportedwithAddr(address, support);
        setAvrcpAbsoluteVolumeSupported(support);
    }

    void setAvrcpAbsoluteVolumeSupported(boolean support) {
        if (DEBUG_DEVICES) {
            java.lang.String eventSource = "setAvrcpAbsoluteVolumeSupported(), support=" + support;
            android.util.Log.d(TAG, eventSource);
        }
        this.mAvrcpAbsVolSupported = support;
        android.os.SystemProperties.set("sys.oplus.absolute_volume_supported", "" + (this.mBleVcAbsVolSupported | this.mAvrcpAbsVolSupported));
        if (com.android.media.audio.Flags.absVolumeIndexFix()) {
            final int a2dpDev = 128;
            synchronized (this.mCachedAbsVolDrivingStreamsLock) {
                this.mCachedAbsVolDrivingStreams.compute(128, new java.util.function.BiFunction() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda3
                    @Override // java.util.function.BiFunction
                    public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                        return this.f$0.lambda$setAvrcpAbsoluteVolumeSupported$15(a2dpDev, (java.lang.Integer) obj, (java.lang.Integer) obj2);
                    }
                });
            }
        }
        sendMsg(this.mAudioHandler, 0, 2, 128, 0, this.mStreamStates[3], 0);
        sendMsg(this.mAudioHandler, 0, 2, 128, 0, this.mStreamStates[5], 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$setAvrcpAbsoluteVolumeSupported$15(int a2dpDev, java.lang.Integer dev, java.lang.Integer stream) {
        if (stream != null && !this.mAvrcpAbsVolSupported) {
            this.mAudioSystem.setDeviceAbsoluteVolumeEnabled(a2dpDev, "", false, 0);
            return null;
        }
        int streamToDriveAbs = getBluetoothContextualVolumeStream();
        if (stream == null || stream.intValue() != streamToDriveAbs) {
            this.mAudioSystem.setDeviceAbsoluteVolumeEnabled(a2dpDev, "", true, streamToDriveAbs);
        }
        return java.lang.Integer.valueOf(streamToDriveAbs);
    }

    private void leVcSupportsAbsoluteVolume(java.lang.String address, boolean support) {
        sVolumeLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("leVcSupportsAbsoluteVolume addr=" + address + " support=" + support));
        setleVcAbsoluteVolumeSupported(support);
    }

    void setleVcAbsoluteVolumeSupported(boolean support) {
        if (this.mAsExt.getBluetoothVolSyncSupported()) {
            this.mDeviceBroker.getWrapper().getExtImpl().setLeVcAbsoluteVolumeSupported(support);
        }
        this.mBleVcAbsVolSupported = support;
        if (DEBUG_MODE) {
            android.util.Log.d(TAG, "setleVcAbsoluteVolumeSupported()," + support);
        }
        android.os.SystemProperties.set("sys.oplus.absolute_volume_supported", "" + (this.mBleVcAbsVolSupported | this.mAvrcpAbsVolSupported));
        sendMsg(this.mAudioHandler, 0, 2, 536870912, 0, this.mStreamStates[3], 0);
    }

    public boolean hasMediaDynamicPolicy() {
        synchronized (this.mAudioPolicies) {
            if (this.mAudioPolicies.isEmpty()) {
                return false;
            }
            java.util.Collection<com.android.server.audio.AudioService.AudioPolicyProxy> appColl = this.mAudioPolicies.values();
            for (com.android.server.audio.AudioService.AudioPolicyProxy app : appColl) {
                if (app.hasMixAffectingUsage(1, 3)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void checkMusicActive(int deviceType, java.lang.String caller) {
        if (this.mSoundDoseHelper.safeDevicesContains(deviceType)) {
            this.mSoundDoseHelper.scheduleMusicActiveCheck();
        }
    }

    private class AudioServiceBroadcastReceiver extends android.content.BroadcastReceiver {
        private AudioServiceBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            int config;
            java.lang.String action = intent.getAction();
            if (com.android.server.audio.AudioService.DEBUG_MODE) {
                android.util.Log.d(com.android.server.audio.AudioService.TAG, "onReceive action = " + action);
            }
            if (action.equals("android.intent.action.DOCK_EVENT")) {
                int dockState = intent.getIntExtra("android.intent.extra.DOCK_STATE", 0);
                switch (dockState) {
                    case 1:
                        config = 7;
                        break;
                    case 2:
                        config = 6;
                        break;
                    case 3:
                        config = 8;
                        break;
                    case 4:
                        config = 9;
                        break;
                    default:
                        config = 0;
                        break;
                }
                if (dockState != 3 && (dockState != 0 || com.android.server.audio.AudioService.this.mDockState != 3)) {
                    com.android.server.audio.AudioService.this.mDeviceBroker.setForceUse_Async(3, config, "ACTION_DOCK_EVENT intent");
                }
                com.android.server.audio.AudioService.this.mDockState = dockState;
            } else if (action.equals("android.bluetooth.headset.profile.action.ACTIVE_DEVICE_CHANGED")) {
                com.android.server.audio.AudioService.this.mDeviceBroker.postReceiveBtEvent(intent);
            } else if (action.equals("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED")) {
                boolean isBleTbsIntent = false;
                java.lang.String broadcastType = intent.getStringExtra("android.bluetooth.device.extra.NAME");
                if (broadcastType != null && "fake_hfp_broadcast".equals(broadcastType)) {
                    isBleTbsIntent = true;
                }
                if (isBleTbsIntent && android.os.Build.isMtkPlatform()) {
                    if (com.android.server.audio.AudioService.DEBUG_MODE) {
                        android.util.Log.d(com.android.server.audio.AudioService.TAG, "Skipped BLE TBS CG Audio State intent here,");
                    }
                } else {
                    com.android.server.audio.AudioService.this.mDeviceBroker.postReceiveBtEvent(intent);
                    return;
                }
            } else if (action.equals("android.intent.action.SCREEN_ON")) {
                if (com.android.server.audio.AudioService.this.mMonitorRotation) {
                    com.android.server.audio.RotationHelper.enable();
                }
                android.media.AudioSystem.setParameters("screen_state=on");
            } else if (action.equals("android.intent.action.SCREEN_OFF")) {
                if (com.android.server.audio.AudioService.this.mMonitorRotation) {
                    com.android.server.audio.RotationHelper.disable();
                }
                android.media.AudioSystem.setParameters("screen_state=off");
            } else if (action.equals("android.intent.action.CONFIGURATION_CHANGED")) {
                com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, 54, 0, 0, 0, null, 0);
            } else if (action.equals("android.intent.action.USER_SWITCHED")) {
                boolean audioDiscarded = com.android.server.audio.AudioService.this.mMediaFocusControl.maybeDiscardAudioFocusOwner();
                if (audioDiscarded && com.android.server.audio.AudioService.this.mUserSwitchedReceived) {
                    com.android.server.audio.AudioService.this.mDeviceBroker.postBroadcastBecomingNoisy();
                }
                com.android.server.audio.AudioService.this.mUserSwitchedReceived = true;
                if (com.android.server.audio.AudioService.this.mSupportsMicPrivacyToggle) {
                    com.android.server.audio.AudioService.this.mMicMuteFromPrivacyToggle = com.android.server.audio.AudioService.this.mSensorPrivacyManagerInternal.isSensorPrivacyEnabled(com.android.server.audio.AudioService.this.getCurrentUserId(), 1);
                    com.android.server.audio.AudioService.this.setMicrophoneMuteNoCallerCheck(com.android.server.audio.AudioService.this.getCurrentUserId());
                }
                com.android.server.audio.AudioService.this.mUserSwitching = true;
                com.android.server.audio.AudioService.this.readAudioSettings(true);
                com.android.server.audio.AudioService.this.mUserSwitching = false;
                com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, 10, 2, 0, 0, com.android.server.audio.AudioService.this.mStreamStates[3], 0);
                com.android.server.audio.AudioService.this.mAsExt.setStreamVolumeForUser();
            } else if (action.equals("android.intent.action.USER_BACKGROUND")) {
                int userId = intent.getIntExtra("android.intent.extra.user_handle", -1);
                if (userId >= 0) {
                    android.content.pm.UserInfo userInfo = com.android.server.pm.UserManagerService.getInstance().getUserInfo(userId);
                    if (userInfo != null) {
                        com.android.server.audio.AudioService.this.killBackgroundUserProcessesWithRecordAudioPermission(userInfo);
                    } else {
                        android.util.Log.w(com.android.server.audio.AudioService.TAG, "userInfo is a null object reference, userId: " + userId);
                    }
                }
                try {
                    com.android.server.pm.UserManagerService.getInstance().setUserRestriction("no_record_audio", true, userId);
                } catch (java.lang.IllegalArgumentException e) {
                    android.util.Slog.w(com.android.server.audio.AudioService.TAG, "Failed to apply DISALLOW_RECORD_AUDIO restriction: " + e);
                }
            } else if (action.equals("android.intent.action.USER_FOREGROUND")) {
                try {
                    com.android.server.pm.UserManagerService.getInstance().setUserRestriction("no_record_audio", false, intent.getIntExtra("android.intent.extra.user_handle", -1));
                } catch (java.lang.IllegalArgumentException e2) {
                    android.util.Slog.w(com.android.server.audio.AudioService.TAG, "Failed to apply DISALLOW_RECORD_AUDIO restriction: " + e2);
                }
            } else if (action.equals("android.media.action.OPEN_AUDIO_EFFECT_CONTROL_SESSION") || action.equals("android.media.action.CLOSE_AUDIO_EFFECT_CONTROL_SESSION")) {
                com.android.server.audio.AudioService.this.mMusicFxHelper.handleAudioEffectBroadcast(context, intent);
            } else if (action.equals("android.intent.action.PACKAGES_SUSPENDED")) {
                int[] suspendedUids = intent.getIntArrayExtra("android.intent.extra.changed_uid_list");
                java.lang.String[] suspendedPackages = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
                if (suspendedPackages == null || suspendedUids == null || suspendedPackages.length != suspendedUids.length) {
                    return;
                }
                for (int i = 0; i < suspendedUids.length; i++) {
                    if (!android.text.TextUtils.isEmpty(suspendedPackages[i])) {
                        com.android.server.audio.AudioService.this.mMediaFocusControl.noFocusForSuspendedApp(suspendedPackages[i], suspendedUids[i]);
                    }
                }
            } else if (action.equals("com.android.server.audio.action.CHECK_MUSIC_ACTIVE")) {
                com.android.server.audio.AudioService.this.mSoundDoseHelper.onCheckMusicActive("com.android.server.audio.action.CHECK_MUSIC_ACTIVE", com.android.server.audio.AudioService.this.mAudioSystem.isStreamActive(3, 0));
            } else if (action.equals(com.android.server.audio.IAudioServiceExt.ACTION_ATLAS_STREAM_MUTE_STATE)) {
                try {
                    boolean atlasMusicMuteState = intent.getBooleanExtra(com.android.server.audio.IAudioServiceExt.EXTRA_ATLAS_STREAM_MUTE_STATE, false);
                    com.android.server.audio.AudioService.this.mAsExt.setAtlasMusicMuteState(atlasMusicMuteState);
                    android.util.Log.d(com.android.server.audio.AudioService.TAG, "atlas_stream_mute_state:" + atlasMusicMuteState);
                } catch (java.lang.Exception e3) {
                    e3.printStackTrace();
                }
            } else if (action.equals(com.android.server.audio.IAudioServiceExt.ACTION_AUDIO_DEVICE_ROUTE_CHANGED)) {
                try {
                    int audioDeviceRoute = intent.getIntExtra(com.android.server.audio.IAudioServiceExt.GET_DEVICE_TYPE_FROM_ATLAS, 0);
                    if (audioDeviceRoute != 0) {
                        com.android.server.audio.AudioService.mCurrentDeviceRoute = audioDeviceRoute;
                    }
                    if (com.android.server.audio.AudioService.this.mAsExt.getPrivacyCallSupport() && audioDeviceRoute != 0) {
                        com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, 113, 0, 0, audioDeviceRoute, null, 500);
                    }
                    if (com.android.server.audio.AudioService.this.mAsExt.getPrivacyCallSoftwareModeSupport() && audioDeviceRoute != 0) {
                        com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, 119, 0, 0, audioDeviceRoute, null, 500);
                    }
                    if (com.android.server.audio.AudioService.this.mAsExt.getVocalProminenceSupport() && audioDeviceRoute != 0) {
                        com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, 117, 0, 0, audioDeviceRoute, null, 500);
                    }
                } catch (java.lang.Exception e4) {
                    e4.printStackTrace();
                }
            }
            com.android.server.audio.AudioService.this.mAsSocExt.onReceiveExt(context, intent);
        }
    }

    private class AudioServiceUserRestrictionsListener implements com.android.server.pm.UserManagerInternal.UserRestrictionsListener {
        private AudioServiceUserRestrictionsListener() {
        }

        @Override // com.android.server.pm.UserManagerInternal.UserRestrictionsListener
        public void onUserRestrictionsChanged(int userId, android.os.Bundle newRestrictions, android.os.Bundle prevRestrictions) {
            boolean wasRestricted = prevRestrictions.getBoolean("no_unmute_microphone");
            boolean isRestricted = newRestrictions.getBoolean("no_unmute_microphone");
            if (wasRestricted != isRestricted) {
                com.android.server.audio.AudioService.this.mMicMuteFromRestrictions = isRestricted;
                com.android.server.audio.AudioService.this.setMicrophoneMuteNoCallerCheck(userId);
            }
            boolean z = true;
            boolean wasRestricted2 = prevRestrictions.getBoolean("no_adjust_volume") || prevRestrictions.getBoolean("disallow_unmute_device");
            if (!newRestrictions.getBoolean("no_adjust_volume") && !newRestrictions.getBoolean("disallow_unmute_device")) {
                z = false;
            }
            boolean isRestricted2 = z;
            if (wasRestricted2 != isRestricted2) {
                com.android.server.audio.AudioService.this.setMasterMuteInternalNoCallerCheck(isRestricted2, 0, userId, "onUserRestrictionsChanged");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void killBackgroundUserProcessesWithRecordAudioPermission(android.content.pm.UserInfo oldUser) {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        android.content.ComponentName homeActivityName = null;
        if (!oldUser.isManagedProfile()) {
            homeActivityName = ((com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class)).getHomeActivityForUser(oldUser.id);
        }
        java.lang.String[] permissions = {"android.permission.RECORD_AUDIO"};
        try {
            java.util.List<android.content.pm.PackageInfo> packages = android.app.AppGlobals.getPackageManager().getPackagesHoldingPermissions(permissions, 0L, oldUser.id).getList();
            for (int j = packages.size() - 1; j >= 0; j--) {
                android.content.pm.PackageInfo pkg = packages.get(j);
                if (android.os.UserHandle.getAppId(pkg.applicationInfo.uid) >= 10000 && pm.checkPermission("android.permission.INTERACT_ACROSS_USERS", pkg.packageName) != 0 && (homeActivityName == null || !pkg.packageName.equals(homeActivityName.getPackageName()) || !pkg.applicationInfo.isSystemApp())) {
                    try {
                        int uid = pkg.applicationInfo.uid;
                        android.app.ActivityManager.getService().killUid(android.os.UserHandle.getAppId(uid), android.os.UserHandle.getUserId(uid), "killBackgroundUserProcessesWithAudioRecordPermission");
                    } catch (android.os.RemoteException e) {
                        android.util.Log.w(TAG, "Error calling killUid", e);
                    }
                }
            }
        } catch (android.os.RemoteException e2) {
            throw new android.util.AndroidRuntimeException(e2);
        }
    }

    private boolean forceFocusDuckingForAccessibility(android.media.AudioAttributes aa, int request, int uid) {
        android.os.Bundle extraInfo;
        if (aa == null || aa.getUsage() != 11 || request != 3 || (extraInfo = aa.getBundle()) == null || !extraInfo.getBoolean("a11y_force_ducking")) {
            return false;
        }
        if (uid == 0) {
            return true;
        }
        synchronized (this.mAccessibilityServiceUidsLock) {
            if (this.mAccessibilityServiceUids != null) {
                int callingUid = android.os.Binder.getCallingUid();
                for (int i = 0; i < this.mAccessibilityServiceUids.length; i++) {
                    if (this.mAccessibilityServiceUids[i] == callingUid) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private boolean isSupportedSystemUsage(int usage) {
        synchronized (this.mSupportedSystemUsagesLock) {
            for (int i = 0; i < this.mSupportedSystemUsages.length; i++) {
                if (this.mSupportedSystemUsages[i] == usage) {
                    return true;
                }
            }
            return false;
        }
    }

    private void validateAudioAttributesUsage(android.media.AudioAttributes audioAttributes) {
        int usage = audioAttributes.getSystemUsage();
        if (android.media.AudioAttributes.isSystemUsage(usage)) {
            if ((usage == 17 && (audioAttributes.getAllFlags() & 65536) != 0 && callerHasPermission("android.permission.CALL_AUDIO_INTERCEPTION")) || callerHasPermission("android.permission.MODIFY_AUDIO_ROUTING")) {
                if (!isSupportedSystemUsage(usage)) {
                    throw new java.lang.IllegalArgumentException("Unsupported usage " + android.media.AudioAttributes.usageToString(usage));
                }
                return;
            }
            throw new java.lang.SecurityException("Missing MODIFY_AUDIO_ROUTING permission");
        }
    }

    private boolean isValidAudioAttributesUsage(android.media.AudioAttributes audioAttributes) {
        int usage = audioAttributes.getSystemUsage();
        if (android.media.AudioAttributes.isSystemUsage(usage)) {
            return isSupportedSystemUsage(usage) && ((usage == 17 && (audioAttributes.getAllFlags() & 65536) != 0 && callerHasPermission("android.permission.CALL_AUDIO_INTERCEPTION")) || callerHasPermission("android.permission.MODIFY_AUDIO_ROUTING"));
        }
        return true;
    }

    public int requestAudioFocus(android.media.AudioAttributes aa, int durationHint, android.os.IBinder cb, android.media.IAudioFocusDispatcher fd, java.lang.String clientId, java.lang.String callingPackageName, java.lang.String attributionTag, int flags, android.media.audiopolicy.IAudioPolicyCallback pcb, int sdk) {
        boolean permissionOverridesCheck;
        if ((flags & 8) != 0) {
            throw new java.lang.IllegalArgumentException("Invalid test flag");
        }
        int uid = android.os.Binder.getCallingUid();
        android.media.MediaMetrics.Item mmi = new android.media.MediaMetrics.Item("audio.service.focus").setUid(uid).set(android.media.MediaMetrics.Property.CALLING_PACKAGE, callingPackageName).set(android.media.MediaMetrics.Property.CLIENT_NAME, clientId).set(android.media.MediaMetrics.Property.EVENT, "requestAudioFocus").set(android.media.MediaMetrics.Property.FLAGS, java.lang.Integer.valueOf(flags));
        if (aa != null && !isValidAudioAttributesUsage(aa)) {
            android.util.Log.w(TAG, "Request using unsupported usage");
            mmi.set(android.media.MediaMetrics.Property.EARLY_RETURN, "Request using unsupported usage").record();
            return 0;
        }
        if ((flags & 4) == 4) {
            if ("AudioFocus_For_Phone_Ring_And_Calls".equals(clientId)) {
                if (this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") != 0) {
                    android.util.Log.e(TAG, "Invalid permission to (un)lock audio focus", new java.lang.Exception());
                    mmi.set(android.media.MediaMetrics.Property.EARLY_RETURN, "Invalid permission to (un)lock audio focus").record();
                    return 0;
                }
            } else {
                synchronized (this.mAudioPolicies) {
                    if (!this.mAudioPolicies.containsKey(pcb.asBinder())) {
                        android.util.Log.e(TAG, "Invalid unregistered AudioPolicy to (un)lock audio focus");
                        mmi.set(android.media.MediaMetrics.Property.EARLY_RETURN, "Invalid unregistered AudioPolicy to (un)lock audio focus").record();
                        return 0;
                    }
                }
            }
        }
        if (callingPackageName == null || clientId == null || aa == null) {
            android.media.MediaMetrics.Item mmi2 = mmi;
            android.util.Log.e(TAG, "Invalid null parameter to request audio focus");
            mmi2.set(android.media.MediaMetrics.Property.EARLY_RETURN, "Invalid null parameter to request audio focus").record();
            return 0;
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_AUDIO_SETTINGS_PRIVILEGED") != 0 && this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_AUDIO_ROUTING") != 0 && uid >= 10000) {
            permissionOverridesCheck = false;
        } else {
            permissionOverridesCheck = true;
        }
        long token = android.os.Binder.clearCallingIdentity();
        if (!permissionOverridesCheck) {
            try {
                if (this.mHardeningEnforcer.blockFocusMethod(uid, 300, clientId, durationHint, callingPackageName, attributionTag, sdk)) {
                    boolean isInFocusCheckBypassList = com.oplus.atlas.OplusAtlasManager.getInstance().checkIsInDaemonlistByName(REQUEST_FOCUS_CHECK_BYPASS_LIST, callingPackageName);
                    if (!isInFocusCheckBypassList) {
                        android.util.Log.w(TAG, "Audio focus request blocked by hardening");
                        mmi.set(android.media.MediaMetrics.Property.EARLY_RETURN, "Audio focus request blocked by hardening").record();
                        return 0;
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }
        android.os.Binder.restoreCallingIdentity(token);
        mmi.record();
        return this.mMediaFocusControl.requestAudioFocus(aa, durationHint, cb, fd, clientId, callingPackageName, flags, sdk, forceFocusDuckingForAccessibility(aa, durationHint, uid), -1, permissionOverridesCheck);
    }

    public int requestAudioFocusForTest(android.media.AudioAttributes aa, int durationHint, android.os.IBinder cb, android.media.IAudioFocusDispatcher fd, java.lang.String clientId, java.lang.String callingPackageName, int flags, int fakeUid, int sdk) {
        if (!enforceQueryAudioStateForTest("focus request")) {
            return 0;
        }
        if (callingPackageName == null || clientId == null || aa == null) {
            android.util.Log.e(TAG, "Invalid null parameter to request audio focus");
            return 0;
        }
        return this.mMediaFocusControl.requestAudioFocus(aa, durationHint, cb, fd, clientId, callingPackageName, flags, sdk, false, fakeUid, true);
    }

    public int abandonAudioFocus(android.media.IAudioFocusDispatcher fd, java.lang.String clientId, android.media.AudioAttributes aa, java.lang.String callingPackageName) {
        android.media.MediaMetrics.Item mmi = new android.media.MediaMetrics.Item("audio.service.focus").set(android.media.MediaMetrics.Property.CALLING_PACKAGE, callingPackageName).set(android.media.MediaMetrics.Property.CLIENT_NAME, clientId).set(android.media.MediaMetrics.Property.EVENT, "abandonAudioFocus");
        if (aa != null && !isValidAudioAttributesUsage(aa)) {
            android.util.Log.w(TAG, "Request using unsupported usage.");
            mmi.set(android.media.MediaMetrics.Property.EARLY_RETURN, "unsupported usage").record();
            return 0;
        }
        mmi.record();
        return this.mMediaFocusControl.abandonAudioFocus(fd, clientId, aa, callingPackageName);
    }

    public int abandonAudioFocusForTest(android.media.IAudioFocusDispatcher fd, java.lang.String clientId, android.media.AudioAttributes aa, java.lang.String callingPackageName) {
        if (!enforceQueryAudioStateForTest("focus abandon")) {
            return 0;
        }
        return this.mMediaFocusControl.abandonAudioFocus(fd, clientId, aa, callingPackageName);
    }

    public java.util.List<java.lang.Integer> getFocusDuckedUidsForTest() {
        super.getFocusDuckedUidsForTest_enforcePermission();
        return this.mPlaybackMonitor.getFocusDuckedUids();
    }

    public void unregisterAudioFocusClient(java.lang.String clientId) {
        new android.media.MediaMetrics.Item("audio.service.focus").set(android.media.MediaMetrics.Property.CLIENT_NAME, clientId).set(android.media.MediaMetrics.Property.EVENT, "unregisterAudioFocusClient").record();
        this.mMediaFocusControl.unregisterAudioFocusClient(clientId);
    }

    public int getCurrentAudioFocus() {
        return this.mMediaFocusControl.getCurrentAudioFocus();
    }

    public int getFocusRampTimeMs(int focusGain, android.media.AudioAttributes attr) {
        return com.android.server.audio.MediaFocusControl.getFocusRampTimeMs(focusGain, attr);
    }

    public long getFocusFadeOutDurationForTest() {
        super.getFocusFadeOutDurationForTest_enforcePermission();
        return this.mMediaFocusControl.getFocusFadeOutDurationForTest();
    }

    public long getFocusUnmuteDelayAfterFadeOutForTest() {
        super.getFocusUnmuteDelayAfterFadeOutForTest_enforcePermission();
        return this.mMediaFocusControl.getFocusUnmuteDelayAfterFadeOutForTest();
    }

    public boolean enterAudioFocusFreezeForTest(android.os.IBinder cb, int[] exemptedUids) {
        super.enterAudioFocusFreezeForTest_enforcePermission();
        java.util.Objects.requireNonNull(exemptedUids);
        java.util.Objects.requireNonNull(cb);
        return this.mMediaFocusControl.enterAudioFocusFreezeForTest(cb, exemptedUids);
    }

    public boolean exitAudioFocusFreezeForTest(android.os.IBinder cb) {
        super.exitAudioFocusFreezeForTest_enforcePermission();
        java.util.Objects.requireNonNull(cb);
        return this.mMediaFocusControl.exitAudioFocusFreezeForTest(cb);
    }

    public boolean hasAudioFocusUsers() {
        return this.mMediaFocusControl.hasAudioFocusUsers();
    }

    public long getFadeOutDurationOnFocusLossMillis(android.media.AudioAttributes aa) {
        if (!enforceQueryAudioStateForTest("fade out duration")) {
            return 0L;
        }
        return this.mMediaFocusControl.getFadeOutDurationOnFocusLossMillis(aa);
    }

    private boolean enforceQueryAudioStateForTest(java.lang.String mssg) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.QUERY_AUDIO_STATE") != 0) {
            java.lang.String reason = "Doesn't have QUERY_AUDIO_STATE permission for " + mssg + " test API";
            android.util.Log.e(TAG, reason, new java.lang.Exception());
            return false;
        }
        return true;
    }

    private void enforceModifyDefaultAudioEffectsPermission() {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_DEFAULT_AUDIO_EFFECTS") != 0) {
            throw new java.lang.SecurityException("Missing MODIFY_DEFAULT_AUDIO_EFFECTS permission");
        }
    }

    public int getSpatializerImmersiveAudioLevel() {
        return this.mSpatializerHelper.getCapableImmersiveAudioLevel();
    }

    public boolean isSpatializerEnabled() {
        return this.mSpatializerHelper.isEnabled();
    }

    public boolean isSpatializerAvailable() {
        java.lang.String mCallerName;
        if (this.mAsExt.getOplusSpatializerSupported() && (mCallerName = this.mContext.getPackageManager().getNameForUid(android.os.Binder.getCallingUid())) != null && !mCallerName.equals("android.media.audio.cts")) {
            if (DEBUG_DEVICES) {
                android.util.Log.d(TAG, "OplusSpatializer is true,so isAvailable is true");
                return true;
            }
            return true;
        }
        return this.mSpatializerHelper.isAvailable();
    }

    public boolean isSpatializerAvailableForDevice(android.media.AudioDeviceAttributes device) {
        super.isSpatializerAvailableForDevice_enforcePermission();
        return this.mSpatializerHelper.isAvailableForDevice((android.media.AudioDeviceAttributes) java.util.Objects.requireNonNull(device));
    }

    void reInitSAState(com.android.server.audio.AdiDeviceState device) {
        this.mSpatializerHelper.reInitSAState(device);
    }

    public boolean hasHeadTracker(android.media.AudioDeviceAttributes device) {
        super.hasHeadTracker_enforcePermission();
        return this.mSpatializerHelper.hasHeadTracker((android.media.AudioDeviceAttributes) java.util.Objects.requireNonNull(device));
    }

    public void setHeadTrackerEnabled(boolean enabled, android.media.AudioDeviceAttributes device) {
        super.setHeadTrackerEnabled_enforcePermission();
        this.mSpatializerHelper.setHeadTrackerEnabled(enabled, (android.media.AudioDeviceAttributes) java.util.Objects.requireNonNull(device));
    }

    public boolean isHeadTrackerEnabled(android.media.AudioDeviceAttributes device) {
        super.isHeadTrackerEnabled_enforcePermission();
        return this.mSpatializerHelper.isHeadTrackerEnabled((android.media.AudioDeviceAttributes) java.util.Objects.requireNonNull(device));
    }

    public boolean isHeadTrackerAvailable() {
        return this.mSpatializerHelper.isHeadTrackerAvailable();
    }

    public void setSpatializerEnabled(boolean enabled) {
        super.setSpatializerEnabled_enforcePermission();
        if (this.mHasAudioEffectCombined) {
            this.mSpatializerHelper.setFeatureEnabled(enabled);
        } else {
            this.mSpatializerHelper.setFeatureEnabled(enabled, true);
        }
    }

    public boolean canBeSpatialized(android.media.AudioAttributes attributes, android.media.AudioFormat format) {
        java.util.Objects.requireNonNull(attributes);
        java.util.Objects.requireNonNull(format);
        return this.mSpatializerHelper.canBeSpatialized(attributes, format);
    }

    public void registerSpatializerCallback(android.media.ISpatializerCallback cb) {
        java.util.Objects.requireNonNull(cb);
        this.mSpatializerHelper.registerStateCallback(cb);
    }

    public void unregisterSpatializerCallback(android.media.ISpatializerCallback cb) {
        java.util.Objects.requireNonNull(cb);
        this.mSpatializerHelper.unregisterStateCallback(cb);
    }

    public void registerSpatializerHeadTrackingCallback(android.media.ISpatializerHeadTrackingModeCallback cb) {
        super.registerSpatializerHeadTrackingCallback_enforcePermission();
        java.util.Objects.requireNonNull(cb);
        this.mSpatializerHelper.registerHeadTrackingModeCallback(cb);
    }

    public void unregisterSpatializerHeadTrackingCallback(android.media.ISpatializerHeadTrackingModeCallback cb) {
        super.unregisterSpatializerHeadTrackingCallback_enforcePermission();
        java.util.Objects.requireNonNull(cb);
        this.mSpatializerHelper.unregisterHeadTrackingModeCallback(cb);
    }

    public void registerSpatializerHeadTrackerAvailableCallback(android.media.ISpatializerHeadTrackerAvailableCallback cb, boolean register) {
        java.util.Objects.requireNonNull(cb);
        this.mSpatializerHelper.registerHeadTrackerAvailableCallback(cb, register);
    }

    public void registerHeadToSoundstagePoseCallback(android.media.ISpatializerHeadToSoundStagePoseCallback cb) {
        super.registerHeadToSoundstagePoseCallback_enforcePermission();
        java.util.Objects.requireNonNull(cb);
        this.mSpatializerHelper.registerHeadToSoundstagePoseCallback(cb);
    }

    public void unregisterHeadToSoundstagePoseCallback(android.media.ISpatializerHeadToSoundStagePoseCallback cb) {
        super.unregisterHeadToSoundstagePoseCallback_enforcePermission();
        java.util.Objects.requireNonNull(cb);
        this.mSpatializerHelper.unregisterHeadToSoundstagePoseCallback(cb);
    }

    public java.util.List<android.media.AudioDeviceAttributes> getSpatializerCompatibleAudioDevices() {
        super.getSpatializerCompatibleAudioDevices_enforcePermission();
        return this.mSpatializerHelper.getCompatibleAudioDevices();
    }

    public void addSpatializerCompatibleAudioDevice(android.media.AudioDeviceAttributes ada) {
        super.addSpatializerCompatibleAudioDevice_enforcePermission();
        java.util.Objects.requireNonNull(ada);
        this.mSpatializerHelper.addCompatibleAudioDevice(ada);
    }

    public void removeSpatializerCompatibleAudioDevice(android.media.AudioDeviceAttributes ada) {
        super.removeSpatializerCompatibleAudioDevice_enforcePermission();
        java.util.Objects.requireNonNull(ada);
        this.mSpatializerHelper.removeCompatibleAudioDevice(ada);
    }

    public int[] getSupportedHeadTrackingModes() {
        super.getSupportedHeadTrackingModes_enforcePermission();
        return this.mSpatializerHelper.getSupportedHeadTrackingModes();
    }

    public int getActualHeadTrackingMode() {
        super.getActualHeadTrackingMode_enforcePermission();
        return this.mSpatializerHelper.getActualHeadTrackingMode();
    }

    public int getDesiredHeadTrackingMode() {
        super.getDesiredHeadTrackingMode_enforcePermission();
        return this.mSpatializerHelper.getDesiredHeadTrackingMode();
    }

    public void setSpatializerGlobalTransform(float[] transform) {
        super.setSpatializerGlobalTransform_enforcePermission();
        java.util.Objects.requireNonNull(transform);
        this.mSpatializerHelper.setGlobalTransform(transform);
    }

    public void recenterHeadTracker() {
        super.recenterHeadTracker_enforcePermission();
        this.mSpatializerHelper.recenterHeadTracker();
    }

    public void setDesiredHeadTrackingMode(int mode) {
        super.setDesiredHeadTrackingMode_enforcePermission();
        switch (mode) {
            case -1:
            case 1:
            case 2:
                this.mSpatializerHelper.setDesiredHeadTrackingMode(mode);
                break;
        }
    }

    public void setSpatializerParameter(int key, byte[] value) {
        super.setSpatializerParameter_enforcePermission();
        java.util.Objects.requireNonNull(value);
        this.mSpatializerHelper.setEffectParameter(key, value);
    }

    public void getSpatializerParameter(int key, byte[] value) {
        super.getSpatializerParameter_enforcePermission();
        java.util.Objects.requireNonNull(value);
        this.mSpatializerHelper.getEffectParameter(key, value);
    }

    public int getSpatializerOutput() {
        super.getSpatializerOutput_enforcePermission();
        return this.mSpatializerHelper.getOutput();
    }

    public void registerSpatializerOutputCallback(android.media.ISpatializerOutputCallback cb) {
        super.registerSpatializerOutputCallback_enforcePermission();
        java.util.Objects.requireNonNull(cb);
        this.mSpatializerHelper.registerSpatializerOutputCallback(cb);
    }

    public void unregisterSpatializerOutputCallback(android.media.ISpatializerOutputCallback cb) {
        super.unregisterSpatializerOutputCallback_enforcePermission();
        java.util.Objects.requireNonNull(cb);
        this.mSpatializerHelper.unregisterSpatializerOutputCallback(cb);
    }

    void postInitSpatializerHeadTrackingSensors() {
        sendMsg(this.mAudioHandler, 42, 0, 0, 0, TAG, 0);
    }

    void postResetSpatializer() {
        sendMsg(this.mAudioHandler, 50, 0, 0, 0, TAG, 0);
    }

    void onInitAdiDeviceStates() {
        this.mDeviceBroker.onReadAudioDeviceSettings();
        this.mSoundDoseHelper.initCachedAudioDeviceCategories(this.mDeviceBroker.getImmutableDeviceInventory());
    }

    void onInitSpatializer() {
        this.mSpatializerHelper.init(this.mHasSpatializerEffect);
        if (this.mHasAudioEffectCombined) {
            this.mSpatializerHelper.setFeatureEnabled(this.mHasSpatializerEffect);
        } else {
            this.mSpatializerHelper.setFeatureEnabled(this.mHasSpatializerEffect, false);
        }
    }

    boolean isSADevice(com.android.server.audio.AdiDeviceState deviceState) {
        return this.mSpatializerHelper.isSADevice(deviceState);
    }

    private boolean isBluetoothPrividged() {
        return this.mContext.checkCallingOrSelfPermission("android.permission.BLUETOOTH_CONNECT") == 0 || android.os.Binder.getCallingUid() == 1000;
    }

    java.util.List<android.media.AudioDeviceAttributes> retrieveBluetoothAddresses(java.util.List<android.media.AudioDeviceAttributes> devices) {
        if (isBluetoothPrividged()) {
            return devices;
        }
        java.util.List<android.media.AudioDeviceAttributes> checkedDevices = new java.util.ArrayList<>();
        for (android.media.AudioDeviceAttributes ada : devices) {
            if (ada != null) {
                checkedDevices.add(retrieveBluetoothAddressUncheked(ada));
            }
        }
        return checkedDevices;
    }

    android.media.AudioDeviceAttributes retrieveBluetoothAddress(android.media.AudioDeviceAttributes ada) {
        if (isBluetoothPrividged()) {
            return ada;
        }
        return retrieveBluetoothAddressUncheked(ada);
    }

    android.media.AudioDeviceAttributes retrieveBluetoothAddressUncheked(android.media.AudioDeviceAttributes ada) {
        java.util.Objects.requireNonNull(ada);
        if (android.media.AudioSystem.isBluetoothDevice(ada.getInternalType())) {
            java.lang.String anonymizedAddress = android.media.Utils.anonymizeBluetoothAddress(ada.getAddress());
            java.util.Iterator<com.android.server.audio.AdiDeviceState> it = this.mDeviceBroker.getImmutableDeviceInventory().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                com.android.server.audio.AdiDeviceState ads = it.next();
                if (android.media.AudioSystem.isBluetoothDevice(ads.getInternalDeviceType()) && ada.getInternalType() == ads.getInternalDeviceType() && anonymizedAddress.equals(android.media.Utils.anonymizeBluetoothAddress(ads.getDeviceAddress()))) {
                    ada.setAddress(ads.getDeviceAddress());
                    break;
                }
            }
        }
        return ada;
    }

    private java.util.List<android.media.AudioDeviceAttributes> anonymizeAudioDeviceAttributesList(java.util.List<android.media.AudioDeviceAttributes> devices) {
        if (isBluetoothPrividged()) {
            return devices;
        }
        return anonymizeAudioDeviceAttributesListUnchecked(devices);
    }

    java.util.List<android.media.AudioDeviceAttributes> anonymizeAudioDeviceAttributesListUnchecked(java.util.List<android.media.AudioDeviceAttributes> devices) {
        java.util.List<android.media.AudioDeviceAttributes> anonymizedDevices = new java.util.ArrayList<>();
        for (android.media.AudioDeviceAttributes ada : devices) {
            anonymizedDevices.add(anonymizeAudioDeviceAttributesUnchecked(ada));
        }
        return anonymizedDevices;
    }

    private android.media.AudioDeviceAttributes anonymizeAudioDeviceAttributesUnchecked(android.media.AudioDeviceAttributes ada) {
        if (!android.media.AudioSystem.isBluetoothDevice(ada.getInternalType())) {
            return ada;
        }
        android.media.AudioDeviceAttributes res = new android.media.AudioDeviceAttributes(ada);
        res.setAddress(android.media.Utils.anonymizeBluetoothAddress(ada.getAddress()));
        return res;
    }

    private android.media.AudioDeviceAttributes anonymizeAudioDeviceAttributes(android.media.AudioDeviceAttributes ada) {
        if (isBluetoothPrividged()) {
            return ada;
        }
        return anonymizeAudioDeviceAttributesUnchecked(ada);
    }

    public void registerLoudnessCodecUpdatesDispatcher(android.media.ILoudnessCodecUpdatesDispatcher dispatcher) {
        this.mLoudnessCodecHelper.registerLoudnessCodecUpdatesDispatcher(dispatcher);
    }

    public void unregisterLoudnessCodecUpdatesDispatcher(android.media.ILoudnessCodecUpdatesDispatcher dispatcher) {
        this.mLoudnessCodecHelper.unregisterLoudnessCodecUpdatesDispatcher(dispatcher);
    }

    public void startLoudnessCodecUpdates(int sessionId) {
        this.mLoudnessCodecHelper.startLoudnessCodecUpdates(sessionId);
    }

    public void stopLoudnessCodecUpdates(int sessionId) {
        this.mLoudnessCodecHelper.stopLoudnessCodecUpdates(sessionId);
    }

    public void addLoudnessCodecInfo(int sessionId, int mediaCodecHash, android.media.LoudnessCodecInfo codecInfo) {
        this.mLoudnessCodecHelper.addLoudnessCodecInfo(sessionId, mediaCodecHash, codecInfo);
    }

    public void removeLoudnessCodecInfo(int sessionId, android.media.LoudnessCodecInfo codecInfo) {
        this.mLoudnessCodecHelper.removeLoudnessCodecInfo(sessionId, codecInfo);
    }

    public android.os.PersistableBundle getLoudnessParams(android.media.LoudnessCodecInfo codecInfo) {
        return this.mLoudnessCodecHelper.getLoudnessParams(codecInfo);
    }

    private boolean readCameraSoundForced() {
        if (this.mAsExt.oplusReadCameraSoundForced() || android.os.SystemProperties.getBoolean("audio.camerasound.force", false) || this.mContext.getResources().getBoolean(android.R.bool.config_canRemoveFirstAccount)) {
            return true;
        }
        android.telephony.SubscriptionManager subscriptionManager = (android.telephony.SubscriptionManager) this.mContext.getSystemService(android.telephony.SubscriptionManager.class);
        if (subscriptionManager == null) {
            android.util.Log.e(TAG, "readCameraSoundForced cannot create SubscriptionManager!");
            return false;
        }
        int[] subscriptionIds = subscriptionManager.getActiveSubscriptionIdList(false);
        for (int subId : subscriptionIds) {
            if (android.telephony.SubscriptionManager.getResourcesForSubId(this.mContext, subId).getBoolean(android.R.bool.config_canRemoveFirstAccount)) {
                return true;
            }
        }
        return false;
    }

    public void muteAwaitConnection(final int[] usages, android.media.AudioDeviceAttributes device, long timeOutMs) {
        java.util.Objects.requireNonNull(usages);
        java.util.Objects.requireNonNull(device);
        enforceModifyAudioRoutingPermission();
        final android.media.AudioDeviceAttributes ada = retrieveBluetoothAddress(device);
        if (timeOutMs <= 0 || usages.length == 0) {
            throw new java.lang.IllegalArgumentException("Invalid timeOutMs/usagesToMute");
        }
        android.util.Log.i(TAG, "muteAwaitConnection dev:" + device + " timeOutMs:" + timeOutMs + " usages:" + java.util.Arrays.toString(usages));
        if (this.mDeviceBroker.isDeviceConnected(ada)) {
            android.util.Log.i(TAG, "muteAwaitConnection ignored, device (" + device + ") already connected");
            return;
        }
        synchronized (this.mMuteAwaitConnectionLock) {
            if (this.mMutingExpectedDevice != null) {
                android.util.Log.e(TAG, "muteAwaitConnection ignored, another in progress for device:" + this.mMutingExpectedDevice);
                throw new java.lang.IllegalStateException("muteAwaitConnection already in progress");
            }
            this.mMutingExpectedDevice = ada;
            this.mMutedUsagesAwaitingConnection = usages;
            this.mPlaybackMonitor.muteAwaitConnection(usages, ada, timeOutMs);
        }
        dispatchMuteAwaitConnection(new java.util.function.BiConsumer() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda27
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$muteAwaitConnection$16(ada, usages, (android.media.IMuteAwaitConnectionCallback) obj, (java.lang.Boolean) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$muteAwaitConnection$16(android.media.AudioDeviceAttributes ada, int[] usages, android.media.IMuteAwaitConnectionCallback cb, java.lang.Boolean isPrivileged) {
        android.media.AudioDeviceAttributes dev = ada;
        try {
            if (!isPrivileged.booleanValue()) {
                dev = anonymizeAudioDeviceAttributesUnchecked(ada);
            }
            cb.dispatchOnMutedUntilConnection(dev, usages);
        } catch (android.os.RemoteException e) {
        }
    }

    public android.media.AudioDeviceAttributes getMutingExpectedDevice() {
        android.media.AudioDeviceAttributes audioDeviceAttributesAnonymizeAudioDeviceAttributes;
        super.getMutingExpectedDevice_enforcePermission();
        synchronized (this.mMuteAwaitConnectionLock) {
            audioDeviceAttributesAnonymizeAudioDeviceAttributes = anonymizeAudioDeviceAttributes(this.mMutingExpectedDevice);
        }
        return audioDeviceAttributesAnonymizeAudioDeviceAttributes;
    }

    public void cancelMuteAwaitConnection(android.media.AudioDeviceAttributes device) {
        java.util.Objects.requireNonNull(device);
        enforceModifyAudioRoutingPermission();
        final android.media.AudioDeviceAttributes ada = retrieveBluetoothAddress(device);
        android.util.Log.i(TAG, "cancelMuteAwaitConnection for device:" + device);
        synchronized (this.mMuteAwaitConnectionLock) {
            if (this.mMutingExpectedDevice == null) {
                android.util.Log.i(TAG, "cancelMuteAwaitConnection ignored, no expected device");
                return;
            }
            if (!ada.equalTypeAddress(this.mMutingExpectedDevice)) {
                android.util.Log.e(TAG, "cancelMuteAwaitConnection ignored, got " + device + "] but expected device is" + this.mMutingExpectedDevice);
                throw new java.lang.IllegalStateException("cancelMuteAwaitConnection for wrong device");
            }
            final int[] mutedUsages = this.mMutedUsagesAwaitingConnection;
            this.mMutingExpectedDevice = null;
            this.mMutedUsagesAwaitingConnection = null;
            this.mPlaybackMonitor.cancelMuteAwaitConnection("cancelMuteAwaitConnection dev:" + device);
            dispatchMuteAwaitConnection(new java.util.function.BiConsumer() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda26
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    this.f$0.lambda$cancelMuteAwaitConnection$17(ada, mutedUsages, (android.media.IMuteAwaitConnectionCallback) obj, (java.lang.Boolean) obj2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelMuteAwaitConnection$17(android.media.AudioDeviceAttributes ada, int[] mutedUsages, android.media.IMuteAwaitConnectionCallback cb, java.lang.Boolean isPrivileged) {
        android.media.AudioDeviceAttributes dev = ada;
        try {
            if (!isPrivileged.booleanValue()) {
                dev = anonymizeAudioDeviceAttributesUnchecked(ada);
            }
            cb.dispatchOnUnmutedEvent(3, dev, mutedUsages);
        } catch (android.os.RemoteException e) {
        }
    }

    public void registerMuteAwaitConnectionDispatcher(android.media.IMuteAwaitConnectionCallback cb, boolean register) {
        super.registerMuteAwaitConnectionDispatcher_enforcePermission();
        if (register) {
            this.mMuteAwaitConnectionDispatchers.register(cb, java.lang.Boolean.valueOf(isBluetoothPrividged()));
        } else {
            this.mMuteAwaitConnectionDispatchers.unregister(cb);
        }
    }

    void checkMuteAwaitConnection() {
        synchronized (this.mMuteAwaitConnectionLock) {
            if (this.mMutingExpectedDevice == null) {
                return;
            }
            final android.media.AudioDeviceAttributes device = this.mMutingExpectedDevice;
            final int[] mutedUsages = this.mMutedUsagesAwaitingConnection;
            if (this.mDeviceBroker.isDeviceConnected(device)) {
                this.mMutingExpectedDevice = null;
                this.mMutedUsagesAwaitingConnection = null;
                this.mPlaybackMonitor.cancelMuteAwaitConnection("checkMuteAwaitConnection device " + device + " connected, unmuting");
                dispatchMuteAwaitConnection(new java.util.function.BiConsumer() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda10
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        this.f$0.lambda$checkMuteAwaitConnection$18(device, mutedUsages, (android.media.IMuteAwaitConnectionCallback) obj, (java.lang.Boolean) obj2);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkMuteAwaitConnection$18(android.media.AudioDeviceAttributes device, int[] mutedUsages, android.media.IMuteAwaitConnectionCallback cb, java.lang.Boolean isPrivileged) {
        android.media.AudioDeviceAttributes ada = device;
        try {
            if (!isPrivileged.booleanValue()) {
                ada = anonymizeAudioDeviceAttributesUnchecked(device);
            }
            cb.dispatchOnUnmutedEvent(1, ada, mutedUsages);
        } catch (android.os.RemoteException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: onMuteAwaitConnectionTimeout, reason: merged with bridge method [inline-methods] */
    public void lambda$new$1(final android.media.AudioDeviceAttributes timedOutDevice) {
        synchronized (this.mMuteAwaitConnectionLock) {
            if (timedOutDevice.equals(this.mMutingExpectedDevice)) {
                android.util.Log.i(TAG, "muteAwaitConnection timeout, clearing expected device " + this.mMutingExpectedDevice);
                final int[] mutedUsages = this.mMutedUsagesAwaitingConnection;
                this.mMutingExpectedDevice = null;
                this.mMutedUsagesAwaitingConnection = null;
                dispatchMuteAwaitConnection(new java.util.function.BiConsumer() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda5
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        ((android.media.IMuteAwaitConnectionCallback) obj).dispatchOnUnmutedEvent(2, timedOutDevice, mutedUsages);
                    }
                });
            }
        }
    }

    private void dispatchMuteAwaitConnection(java.util.function.BiConsumer<android.media.IMuteAwaitConnectionCallback, java.lang.Boolean> callback) {
        int nbDispatchers = this.mMuteAwaitConnectionDispatchers.beginBroadcast();
        java.util.ArrayList<android.media.IMuteAwaitConnectionCallback> errorList = null;
        for (int i = 0; i < nbDispatchers; i++) {
            try {
                callback.accept((android.media.IMuteAwaitConnectionCallback) this.mMuteAwaitConnectionDispatchers.getBroadcastItem(i), (java.lang.Boolean) this.mMuteAwaitConnectionDispatchers.getBroadcastCookie(i));
            } catch (java.lang.Exception e) {
                if (errorList == null) {
                    errorList = new java.util.ArrayList<>(1);
                }
                errorList.add(this.mMuteAwaitConnectionDispatchers.getBroadcastItem(i));
            }
        }
        if (errorList != null) {
            for (android.media.IMuteAwaitConnectionCallback errorItem : errorList) {
                this.mMuteAwaitConnectionDispatchers.unregister(errorItem);
            }
        }
        this.mMuteAwaitConnectionDispatchers.finishBroadcast();
    }

    public void registerDeviceVolumeBehaviorDispatcher(boolean register, android.media.IDeviceVolumeBehaviorDispatcher dispatcher) {
        enforceQueryStateOrModifyRoutingPermission();
        java.util.Objects.requireNonNull(dispatcher);
        if (register) {
            this.mDeviceVolumeBehaviorDispatchers.register(dispatcher);
        } else {
            this.mDeviceVolumeBehaviorDispatchers.unregister(dispatcher);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchDeviceVolumeBehavior(android.media.AudioDeviceAttributes device, int volumeBehavior) {
        int dispatchers = this.mDeviceVolumeBehaviorDispatchers.beginBroadcast();
        for (int i = 0; i < dispatchers; i++) {
            try {
                this.mDeviceVolumeBehaviorDispatchers.getBroadcastItem(i).dispatchDeviceVolumeBehaviorChanged(device, volumeBehavior);
            } catch (android.os.RemoteException e) {
            }
        }
        this.mDeviceVolumeBehaviorDispatchers.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConfigurationChanged() {
        try {
            android.content.res.Configuration config = this.mContext.getResources().getConfiguration();
            this.mSoundDoseHelper.configureSafeMedia(false, TAG);
            boolean cameraSoundForced = readCameraSoundForced();
            synchronized (this.mSettingsLock) {
                boolean cameraSoundForcedChanged = cameraSoundForced != this.mCameraSoundForced;
                this.mCameraSoundForced = cameraSoundForced;
                if (cameraSoundForcedChanged) {
                    if (!this.mIsSingleVolume) {
                        synchronized (com.android.server.audio.AudioService.VolumeStreamState.class) {
                            com.android.server.audio.AudioService.VolumeStreamState s = this.mStreamStates[7];
                            if (cameraSoundForced) {
                                s.setAllIndexesToMax();
                                this.mRingerModeAffectedStreams &= -129;
                            } else {
                                s.setAllIndexes(this.mStreamStates[1], TAG);
                                this.mRingerModeAffectedStreams |= 128;
                            }
                        }
                        setRingerModeInt(getRingerModeInternal(), false);
                    }
                    this.mDeviceBroker.setForceUse_Async(4, cameraSoundForced ? 11 : 0, "onConfigurationChanged");
                    sendMsg(this.mAudioHandler, 10, 2, 0, 0, this.mStreamStates[7], 0);
                }
            }
            this.mVolumeController.setLayoutDirection(config.getLayoutDirection());
        } catch (java.lang.Exception e) {
            android.util.Log.e(TAG, "Error handling configuration change: ", e);
        }
    }

    public void setRingtonePlayer(android.media.IRingtonePlayer player) {
        setRingtonePlayer_enforcePermission();
        this.mRingtonePlayer = player;
    }

    public android.media.IRingtonePlayer getRingtonePlayer() {
        return this.mRingtonePlayer;
    }

    public android.media.AudioRoutesInfo startWatchingRoutes(android.media.IAudioRoutesObserver observer) {
        return this.mDeviceBroker.startWatchingRoutes(observer);
    }

    public void disableSafeMediaVolume(java.lang.String callingPackage) {
        enforceVolumeController("disable the safe media volume");
        this.mSoundDoseHelper.disableSafeMediaVolume(callingPackage);
    }

    public void lowerVolumeToRs1(java.lang.String callingPackage) {
        enforceVolumeController("lowerVolumeToRs1");
        postLowerVolumeToRs1();
    }

    void postLowerVolumeToRs1() {
        sendMsg(this.mAudioHandler, 1007, 2, 0, 0, null, 0);
    }

    public float getOutputRs2UpperBound() {
        super.getOutputRs2UpperBound_enforcePermission();
        return this.mSoundDoseHelper.getOutputRs2UpperBound();
    }

    public void setOutputRs2UpperBound(float rs2Value) {
        super.setOutputRs2UpperBound_enforcePermission();
        this.mSoundDoseHelper.setOutputRs2UpperBound(rs2Value);
    }

    public float getCsd() {
        super.getCsd_enforcePermission();
        return this.mSoundDoseHelper.getCsd();
    }

    public void setCsd(float csd) {
        super.setCsd_enforcePermission();
        if (csd < 0.0f) {
            this.mSoundDoseHelper.resetCsdTimeouts();
        } else {
            this.mSoundDoseHelper.setCsd(csd);
        }
    }

    public void forceUseFrameworkMel(boolean useFrameworkMel) {
        super.forceUseFrameworkMel_enforcePermission();
        this.mSoundDoseHelper.forceUseFrameworkMel(useFrameworkMel);
    }

    public void forceComputeCsdOnAllDevices(boolean computeCsdOnAllDevices) {
        super.forceComputeCsdOnAllDevices_enforcePermission();
        this.mSoundDoseHelper.forceComputeCsdOnAllDevices(computeCsdOnAllDevices);
    }

    public boolean isCsdEnabled() {
        super.isCsdEnabled_enforcePermission();
        return this.mSoundDoseHelper.isCsdEnabled();
    }

    public boolean isCsdAsAFeatureAvailable() {
        super.isCsdAsAFeatureAvailable_enforcePermission();
        return this.mSoundDoseHelper.isCsdAsAFeatureAvailable();
    }

    public boolean isCsdAsAFeatureEnabled() {
        super.isCsdAsAFeatureEnabled_enforcePermission();
        return this.mSoundDoseHelper.isCsdAsAFeatureEnabled();
    }

    public void setCsdAsAFeatureEnabled(boolean csdToggleValue) {
        super.setCsdAsAFeatureEnabled_enforcePermission();
        this.mSoundDoseHelper.setCsdAsAFeatureEnabled(csdToggleValue);
    }

    public void setBluetoothAudioDeviceCategory_legacy(java.lang.String address, boolean isBle, int btAudioDeviceCategory) {
        int i;
        int deviceType;
        super.setBluetoothAudioDeviceCategory_legacy_enforcePermission();
        if (android.media.audio.Flags.automaticBtDeviceType()) {
            return;
        }
        java.lang.String addr = (java.lang.String) java.util.Objects.requireNonNull(address);
        com.android.server.audio.AudioDeviceBroker audioDeviceBroker = this.mDeviceBroker;
        int internalType = 536870912;
        if (isBle) {
            i = 536870912;
        } else {
            i = 128;
        }
        com.android.server.audio.AdiDeviceState deviceState = audioDeviceBroker.findBtDeviceStateForAddress(addr, i);
        if (!isBle) {
            internalType = 128;
        } else if (btAudioDeviceCategory != 3) {
            internalType = 536870913;
        }
        if (isBle) {
            deviceType = btAudioDeviceCategory == 3 ? 26 : 27;
        } else {
            deviceType = 8;
        }
        if (deviceState == null) {
            deviceState = new com.android.server.audio.AdiDeviceState(deviceType, internalType, addr);
        }
        deviceState.setAudioDeviceCategory(btAudioDeviceCategory);
        this.mDeviceBroker.addOrUpdateBtAudioDeviceCategoryInInventory(deviceState, true);
        this.mDeviceBroker.postPersistAudioDeviceSettings();
        this.mSpatializerHelper.refreshDevice(deviceState.getAudioDeviceAttributes(), false);
        this.mSoundDoseHelper.setAudioDeviceCategory(addr, internalType, btAudioDeviceCategory == 3);
    }

    public int getBluetoothAudioDeviceCategory_legacy(java.lang.String address, boolean isBle) {
        super.getBluetoothAudioDeviceCategory_legacy_enforcePermission();
        if (android.media.audio.Flags.automaticBtDeviceType()) {
            return 0;
        }
        com.android.server.audio.AdiDeviceState deviceState = this.mDeviceBroker.findBtDeviceStateForAddress((java.lang.String) java.util.Objects.requireNonNull(address), isBle ? 536870912 : 128);
        if (deviceState == null) {
            return 0;
        }
        return deviceState.getAudioDeviceCategory();
    }

    public boolean setBluetoothAudioDeviceCategory(java.lang.String address, int btAudioDeviceCategory) {
        super.setBluetoothAudioDeviceCategory_enforcePermission();
        if (!android.media.audio.Flags.automaticBtDeviceType()) {
            return false;
        }
        java.lang.String addr = (java.lang.String) java.util.Objects.requireNonNull(address);
        if (isBluetoothAudioDeviceCategoryFixed(addr)) {
            android.util.Log.w(TAG, "Cannot set fixed audio device type for address " + android.media.Utils.anonymizeBluetoothAddress(address));
            return false;
        }
        this.mDeviceBroker.addAudioDeviceWithCategoryInInventoryIfNeeded(address, btAudioDeviceCategory);
        return true;
    }

    public int getBluetoothAudioDeviceCategory(java.lang.String address) {
        super.getBluetoothAudioDeviceCategory_enforcePermission();
        if (!android.media.audio.Flags.automaticBtDeviceType()) {
            return 0;
        }
        return this.mDeviceBroker.getAndUpdateBtAdiDeviceStateCategoryForAddress(address);
    }

    public boolean isBluetoothAudioDeviceCategoryFixed(java.lang.String address) {
        super.isBluetoothAudioDeviceCategoryFixed_enforcePermission();
        if (!android.media.audio.Flags.automaticBtDeviceType()) {
            return false;
        }
        return this.mDeviceBroker.isBluetoothAudioDeviceCategoryFixed(address);
    }

    public void onUpdatedAdiDeviceState(com.android.server.audio.AdiDeviceState deviceState, boolean initSA) {
        if (deviceState == null) {
            return;
        }
        this.mSpatializerHelper.refreshDevice(deviceState.getAudioDeviceAttributes(), initSA);
        this.mSoundDoseHelper.setAudioDeviceCategory(deviceState.getDeviceAddress(), deviceState.getInternalDeviceType(), deviceState.getAudioDeviceCategory() == 3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHdmiCecSinkLocked(boolean hdmiCecSink) {
        if (!hasDeviceVolumeBehavior(1024)) {
            if (hdmiCecSink) {
                if (DEBUG_VOL) {
                    android.util.Log.d(TAG, "CEC sink: setting HDMI as full vol device");
                }
                setDeviceVolumeBehaviorInternal(new android.media.AudioDeviceAttributes(1024, ""), 1, "AudioService.updateHdmiCecSinkLocked()");
            } else {
                if (DEBUG_VOL) {
                    android.util.Log.d(TAG, "TV, no CEC: setting HDMI as regular vol device");
                }
                setDeviceVolumeBehaviorInternal(new android.media.AudioDeviceAttributes(1024, ""), 0, "AudioService.updateHdmiCecSinkLocked()");
            }
            postUpdateVolumeStatesForAudioDevice(1024, "HdmiPlaybackClient.DisplayStatusCallback");
        }
    }

    private class MyHdmiControlStatusChangeListenerCallback implements android.hardware.hdmi.HdmiControlManager.HdmiControlStatusChangeListener {
        private MyHdmiControlStatusChangeListenerCallback() {
        }

        public void onStatusChange(int isCecEnabled, boolean isCecAvailable) {
            synchronized (com.android.server.audio.AudioService.this.mHdmiClientLock) {
                if (com.android.server.audio.AudioService.this.mHdmiManager == null) {
                    return;
                }
                boolean cecEnabled = true;
                if (isCecEnabled != 1) {
                    cecEnabled = false;
                }
                com.android.server.audio.AudioService.this.updateHdmiCecSinkLocked(cecEnabled ? isCecAvailable : false);
            }
        }
    }

    private class MyHdmiCecVolumeControlFeatureListener implements android.hardware.hdmi.HdmiControlManager.HdmiCecVolumeControlFeatureListener {
        private MyHdmiCecVolumeControlFeatureListener() {
        }

        public void onHdmiCecVolumeControlFeature(int hdmiCecVolumeControl) {
            synchronized (com.android.server.audio.AudioService.this.mHdmiClientLock) {
                if (com.android.server.audio.AudioService.this.mHdmiManager == null) {
                    return;
                }
                com.android.server.audio.AudioService audioService = com.android.server.audio.AudioService.this;
                boolean z = true;
                if (hdmiCecVolumeControl != 1) {
                    z = false;
                }
                audioService.mHdmiCecVolumeControlEnabled = z;
            }
        }
    }

    public int setHdmiSystemAudioSupported(boolean on) {
        int device = 0;
        synchronized (this.mHdmiClientLock) {
            if (this.mHdmiManager != null) {
                if (this.mHdmiTvClient == null && this.mHdmiAudioSystemClient == null) {
                    android.util.Log.w(TAG, "Only Hdmi-Cec enabled TV or audio system device supportssystem audio mode.");
                    return 0;
                }
                if (this.mHdmiSystemAudioSupported != on) {
                    this.mHdmiSystemAudioSupported = on;
                    int config = on ? 12 : 0;
                    this.mDeviceBroker.setForceUse_Async(5, config, "setHdmiSystemAudioSupported");
                }
                device = getDeviceMaskForStream(3);
            }
            return device;
        }
    }

    public boolean isHdmiSystemAudioSupported() {
        return this.mHdmiSystemAudioSupported;
    }

    private void initA11yMonitoring() {
        android.view.accessibility.AccessibilityManager accessibilityManager = (android.view.accessibility.AccessibilityManager) this.mContext.getSystemService("accessibility");
        updateDefaultStreamOverrideDelay(accessibilityManager.isTouchExplorationEnabled());
        updateA11yVolumeAlias(accessibilityManager.isAccessibilityVolumeStreamActive());
        accessibilityManager.addTouchExplorationStateChangeListener(this, null);
        accessibilityManager.addAccessibilityServicesStateChangeListener(this);
    }

    @Override // android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener
    public void onTouchExplorationStateChanged(boolean enabled) {
        updateDefaultStreamOverrideDelay(enabled);
    }

    private void updateDefaultStreamOverrideDelay(boolean touchExploreEnabled) {
        if (touchExploreEnabled) {
            sStreamOverrideDelayMs = 1000;
        } else {
            sStreamOverrideDelayMs = 0;
        }
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "Touch exploration enabled=" + touchExploreEnabled + " stream override delay is now " + sStreamOverrideDelayMs + " ms");
        }
    }

    @Override // android.view.accessibility.AccessibilityManager.AccessibilityServicesStateChangeListener
    public void onAccessibilityServicesStateChanged(android.view.accessibility.AccessibilityManager accessibilityManager) {
        updateA11yVolumeAlias(accessibilityManager.isAccessibilityVolumeStreamActive());
    }

    private void updateA11yVolumeAlias(boolean a11VolEnabled) {
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "Accessibility volume enabled = " + a11VolEnabled);
        }
        if (this.mIsSingleVolume) {
            if (DEBUG_VOL) {
                android.util.Log.d(TAG, "Accessibility volume is not set on single volume device");
            }
        } else if (sIndependentA11yVolume != a11VolEnabled) {
            sIndependentA11yVolume = a11VolEnabled;
            int i = 1;
            updateStreamVolumeAlias(true, TAG);
            com.android.server.audio.AudioService.VolumeController volumeController = this.mVolumeController;
            if (!sIndependentA11yVolume) {
                i = 0;
            }
            volumeController.setA11yMode(i);
            this.mVolumeController.postVolumeChanged(10, 0);
        }
    }

    public boolean isCameraSoundForced() {
        boolean z;
        synchronized (this.mSettingsLock) {
            z = this.mCameraSoundForced;
        }
        return z;
    }

    private void dumpRingerMode(java.io.PrintWriter pw) {
        pw.println("\nRinger mode: ");
        pw.println("- mode (internal) = " + RINGER_MODE_NAMES[this.mRingerMode]);
        pw.println("- mode (external) = " + RINGER_MODE_NAMES[this.mRingerModeExternal]);
        pw.println("- zen mode:" + android.provider.Settings.Global.zenModeToString(this.mNm.getZenMode()));
        dumpRingerModeStreams(pw, "affected", this.mRingerModeAffectedStreams);
        dumpRingerModeStreams(pw, "muted", sRingerAndZenModeMutedStreams);
        pw.print("- delegate = ");
        pw.println(this.mRingerModeDelegate);
    }

    private void dumpRingerModeStreams(java.io.PrintWriter pw, java.lang.String type, int streams) {
        pw.print("- ringer mode ");
        pw.print(type);
        pw.print(" streams = 0x");
        pw.print(java.lang.Integer.toHexString(streams));
        if (streams != 0) {
            pw.print(" (");
            boolean first = true;
            for (int i = 0; i < android.media.AudioSystem.STREAM_NAMES.length; i++) {
                int stream = 1 << i;
                if ((streams & stream) != 0) {
                    if (!first) {
                        pw.print(',');
                    }
                    pw.print(android.media.AudioSystem.STREAM_NAMES[i]);
                    streams &= ~stream;
                    first = false;
                }
            }
            if (streams != 0) {
                if (!first) {
                    pw.print(',');
                }
                pw.print(streams);
            }
            pw.print(')');
        }
        pw.println();
    }

    private java.util.Set<java.lang.Integer> getAbsoluteVolumeDevicesWithBehavior(final int behavior) {
        return (java.util.Set) this.mAbsoluteVolumeDeviceInfoMap.entrySet().stream().filter(new java.util.function.Predicate() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.audio.AudioService.lambda$getAbsoluteVolumeDevicesWithBehavior$20(behavior, (java.util.Map.Entry) obj);
            }
        }).map(new com.android.server.UiModeManagerService$Stub$$ExternalSyntheticLambda3()).collect(java.util.stream.Collectors.toSet());
    }

    static /* synthetic */ boolean lambda$getAbsoluteVolumeDevicesWithBehavior$20(int behavior, java.util.Map.Entry entry) {
        return ((com.android.server.audio.AudioService.AbsoluteVolumeDeviceInfo) entry.getValue()).mDeviceVolumeBehavior == behavior;
    }

    private java.lang.String dumpDeviceTypes(java.util.Set<java.lang.Integer> deviceTypes) {
        java.util.Iterator<java.lang.Integer> it = deviceTypes.iterator();
        if (!it.hasNext()) {
            return "";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("0x" + java.lang.Integer.toHexString(it.next().intValue()));
        while (it.hasNext()) {
            sb.append(",0x" + java.lang.Integer.toHexString(it.next().intValue()));
        }
        return sb.toString();
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            sLifecycleLogger.dump(pw);
            if (this.mAudioHandler != null) {
                pw.println("\nMessage handler (watch for unhandled messages):");
                this.mAudioHandler.dump(new android.util.PrintWriterPrinter(pw), "  ");
            } else {
                pw.println("\nMessage handler is null");
            }
            dumpFlags(pw);
            this.mHardeningEnforcer.dump(pw);
            this.mMediaFocusControl.dump(pw);
            dumpStreamStates(pw);
            dumpVolumeGroups(pw);
            dumpRingerMode(pw);
            dumpAudioMode(pw);
            pw.println("\nAudio routes:");
            pw.print("  mMainType=0x");
            pw.println(java.lang.Integer.toHexString(this.mDeviceBroker.getCurAudioRoutes().mainType));
            pw.print("  mBluetoothName=");
            pw.println(this.mDeviceBroker.getCurAudioRoutes().bluetoothName);
            pw.println("\nOther state:");
            pw.print("  mUseVolumeGroupAliases=");
            pw.println(this.mUseVolumeGroupAliases);
            pw.print("  mVolumeController=");
            pw.println(this.mVolumeController);
            this.mSoundDoseHelper.dump(pw);
            pw.print("  sIndependentA11yVolume=");
            pw.println(sIndependentA11yVolume);
            pw.print("  mCameraSoundForced=");
            pw.println(isCameraSoundForced());
            pw.print("  mHasVibrator=");
            pw.println(this.mHasVibrator);
            pw.print("  mVolumePolicy=");
            pw.println(this.mVolumePolicy);
            pw.print("  mAvrcpAbsVolSupported=");
            pw.println(this.mAvrcpAbsVolSupported);
            pw.print("  mBtScoOnByApp=");
            pw.println(this.mBtScoOnByApp);
            pw.print("  mIsSingleVolume=");
            pw.println(this.mIsSingleVolume);
            pw.print("  mUseFixedVolume=");
            pw.println(this.mUseFixedVolume);
            pw.print("  mNotifAliasRing=");
            pw.println(this.mNotifAliasRing);
            pw.print("  mFixedVolumeDevices=");
            pw.println(dumpDeviceTypes(this.mFixedVolumeDevices));
            pw.print("  mFullVolumeDevices=");
            pw.println(dumpDeviceTypes(this.mFullVolumeDevices));
            pw.print("  absolute volume devices=");
            pw.println(dumpDeviceTypes(getAbsoluteVolumeDevicesWithBehavior(3)));
            pw.print("  adjust-only absolute volume devices=");
            pw.println(dumpDeviceTypes(getAbsoluteVolumeDevicesWithBehavior(5)));
            pw.print("  pre-scale for bluetooth absolute volume ");
            if (com.android.media.audio.Flags.disablePrescaleAbsoluteVolume()) {
                pw.println("= disabled");
            } else {
                pw.println("=" + this.mPrescaleAbsoluteVolume[0] + ", " + this.mPrescaleAbsoluteVolume[1] + ", " + this.mPrescaleAbsoluteVolume[2]);
            }
            pw.print("  mExtVolumeController=");
            pw.println(this.mExtVolumeController);
            pw.print("  mHdmiAudioSystemClient=");
            pw.println(this.mHdmiAudioSystemClient);
            pw.print("  mHdmiPlaybackClient=");
            pw.println(this.mHdmiPlaybackClient);
            pw.print("  mHdmiTvClient=");
            pw.println(this.mHdmiTvClient);
            pw.print("  mHdmiSystemAudioSupported=");
            pw.println(this.mHdmiSystemAudioSupported);
            synchronized (this.mHdmiClientLock) {
                pw.print("  mHdmiCecVolumeControlEnabled=");
                pw.println(this.mHdmiCecVolumeControlEnabled);
            }
            pw.print("  mIsCallScreeningModeSupported=");
            pw.println(this.mIsCallScreeningModeSupported);
            pw.println("  mic mute FromSwitch=" + this.mMicMuteFromSwitch + " FromRestrictions=" + this.mMicMuteFromRestrictions + " FromApi=" + this.mMicMuteFromApi + " from system=" + this.mMicMuteFromSystemCached);
            pw.print("  mMasterMute=");
            pw.println(this.mMasterMute.get());
            pw.print("  mMonitorRotation=");
            pw.println(this.mMonitorRotation);
            dumpAccessibilityServiceUids(pw);
            dumpAssistantServicesUids(pw);
            pw.print("  supportsBluetoothVariableLatency=");
            pw.println(android.media.AudioSystem.supportsBluetoothVariableLatency());
            pw.print("  isBluetoothVariableLatencyEnabled=");
            pw.println(android.media.AudioSystem.isBluetoothVariableLatencyEnabled());
            dumpAudioPolicies(pw);
            this.mDynPolicyLogger.dump(pw);
            this.mPlaybackMonitor.dump(pw);
            this.mRecordMonitor.dump(pw);
            pw.println("\nAudioDeviceBroker:");
            this.mDeviceBroker.dump(pw, "  ");
            pw.println("\nSoundEffects:");
            this.mSfxHelper.dump(pw, "  ");
            pw.println("\n");
            pw.println("\nEvent logs:");
            this.mModeLogger.dump(pw);
            pw.println("\n");
            sDeviceLogger.dump(pw);
            pw.println("\n");
            sForceUseLogger.dump(pw);
            pw.println("\n");
            sVolumeLogger.dump(pw);
            pw.println("\n");
            sMuteLogger.dump(pw);
            pw.println("\n");
            dumpSupportedSystemUsage(pw);
            pw.println("\n");
            pw.println("\nSpatial audio:");
            pw.println("mHasSpatializerEffect:" + this.mHasSpatializerEffect + " (effect present)");
            pw.println("mHasSpatializerEffect:" + this.mHasSpeakerSpatializer + " (Speaker Spatializer)");
            pw.println("isSpatializerEnabled:" + isSpatializerEnabled() + " (routing dependent)");
            pw.println("mHasBlueVolSync:" + this.mAsExt.getBluetoothVolSyncSupported() + " (blue volume sync)");
            this.mSpatializerHelper.dump(pw);
            sSpatialLogger.dump(pw);
            pw.println("\n");
            pw.println("\nLoudness alignment:");
            this.mLoudnessCodecHelper.dump(pw);
            this.mAudioSystem.dump(pw);
        }
    }

    private void dumpSupportedSystemUsage(java.io.PrintWriter pw) {
        pw.println("Supported System Usages:");
        synchronized (this.mSupportedSystemUsagesLock) {
            for (int i = 0; i < this.mSupportedSystemUsages.length; i++) {
                pw.printf("\t%s\n", android.media.AudioAttributes.usageToString(this.mSupportedSystemUsages[i]));
            }
        }
    }

    private void dumpAssistantServicesUids(java.io.PrintWriter pw) {
        synchronized (this.mSettingsLock) {
            if (this.mAssistantUids.size() > 0) {
                pw.println("  Assistant service UIDs:");
                java.util.Iterator<java.lang.Integer> it = this.mAssistantUids.iterator();
                while (it.hasNext()) {
                    int uid = it.next().intValue();
                    pw.println("  - " + uid);
                }
            } else {
                pw.println("  No Assistant service Uids.");
            }
        }
    }

    private void dumpAccessibilityServiceUids(java.io.PrintWriter pw) {
        synchronized (this.mSupportedSystemUsagesLock) {
            if (this.mAccessibilityServiceUids != null && this.mAccessibilityServiceUids.length > 0) {
                pw.println("  Accessibility service Uids:");
                for (int uid : this.mAccessibilityServiceUids) {
                    pw.println("  - " + uid);
                }
            } else {
                pw.println("  No accessibility service Uids.");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static com.android.server.audio.AudioServerPermissionProvider initializeAudioServerPermissionProvider(android.content.Context context, final com.android.server.audio.AudioPolicyFacade audioPolicy, java.util.concurrent.Executor audioserverExecutor) {
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot snapshot = ((com.android.server.pm.PackageManagerLocal) com.android.server.LocalManagerRegistry.getManager(com.android.server.pm.PackageManagerLocal.class)).withUnfilteredSnapshot();
        try {
            java.util.Collection<com.android.server.pm.pkg.PackageState> packageStates = snapshot.getPackageStates().values();
            if (snapshot != null) {
                snapshot.close();
            }
            final com.android.server.pm.UserManagerInternal umi = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
            final com.android.server.pm.permission.PermissionManagerServiceInternal pmsi = (com.android.server.pm.permission.PermissionManagerServiceInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionManagerServiceInternal.class);
            final com.android.server.audio.AudioServerPermissionProvider provider = new com.android.server.audio.AudioServerPermissionProvider(packageStates, new java.util.function.BiPredicate() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda18
                @Override // java.util.function.BiPredicate
                public final boolean test(java.lang.Object obj, java.lang.Object obj2) {
                    return com.android.server.audio.AudioService.lambda$initializeAudioServerPermissionProvider$21(pmsi, (java.lang.Integer) obj, (java.lang.String) obj2);
                }
            }, new java.util.function.Supplier() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda19
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return umi.getUserIds();
                }
            });
            audioPolicy.registerOnStartTask(new java.lang.Runnable() { // from class: com.android.server.audio.AudioService$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    provider.onServiceStart(audioPolicy.getPermissionController());
                }
            });
            java.lang.Runnable cacheSysPropHandler = new com.android.server.audio.AudioService.AnonymousClass7(audioserverExecutor, provider);
            android.os.SystemProperties.addChangeCallback(cacheSysPropHandler);
            android.content.IntentFilter packageUpdateFilter = new android.content.IntentFilter();
            packageUpdateFilter.addAction("android.intent.action.PACKAGE_ADDED");
            packageUpdateFilter.addAction("android.intent.action.PACKAGE_REMOVED");
            packageUpdateFilter.addDataScheme("package");
            context.registerReceiverForAllUsers(new com.android.server.audio.AudioService.AnonymousClass8(audioserverExecutor, provider), packageUpdateFilter, null, null);
            return provider;
        } catch (java.lang.Throwable th) {
            if (snapshot != null) {
                try {
                    snapshot.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    static /* synthetic */ boolean lambda$initializeAudioServerPermissionProvider$21(com.android.server.pm.permission.PermissionManagerServiceInternal pmsi, java.lang.Integer uid, java.lang.String perm) {
        return pmsi.checkUidPermission(uid.intValue(), perm, 0) == 0;
    }

    /* JADX INFO: renamed from: com.android.server.audio.AudioService$7, reason: invalid class name */
    class AnonymousClass7 implements java.lang.Runnable {
        private java.util.concurrent.atomic.AtomicReference<android.os.SystemProperties.Handle> mHandle = new java.util.concurrent.atomic.AtomicReference<>();
        private java.util.concurrent.atomic.AtomicLong mNonce = new java.util.concurrent.atomic.AtomicLong();
        final /* synthetic */ java.util.concurrent.Executor val$audioserverExecutor;
        final /* synthetic */ com.android.server.audio.AudioServerPermissionProvider val$provider;

        AnonymousClass7(java.util.concurrent.Executor executor, com.android.server.audio.AudioServerPermissionProvider audioServerPermissionProvider) {
            this.val$audioserverExecutor = executor;
            this.val$provider = audioServerPermissionProvider;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mHandle.get() == null) {
                this.mHandle.compareAndSet(null, android.os.SystemProperties.find("cache_key.package_info"));
            }
            android.os.SystemProperties.Handle ref = this.mHandle.get();
            if (ref != null) {
                long nonce = ref.getLong(0L);
                if (nonce != 0 && this.mNonce.getAndSet(nonce) != nonce) {
                    java.util.concurrent.Executor executor = this.val$audioserverExecutor;
                    final com.android.server.audio.AudioServerPermissionProvider audioServerPermissionProvider = this.val$provider;
                    executor.execute(new java.lang.Runnable() { // from class: com.android.server.audio.AudioService$7$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            audioServerPermissionProvider.onPermissionStateChanged();
                        }
                    });
                }
            }
        }
    }

    /* JADX INFO: renamed from: com.android.server.audio.AudioService$8, reason: invalid class name */
    class AnonymousClass8 extends android.content.BroadcastReceiver {
        final /* synthetic */ java.util.concurrent.Executor val$audioserverExecutor;
        final /* synthetic */ com.android.server.audio.AudioServerPermissionProvider val$provider;

        AnonymousClass8(java.util.concurrent.Executor executor, com.android.server.audio.AudioServerPermissionProvider audioServerPermissionProvider) {
            this.val$audioserverExecutor = executor;
            this.val$provider = audioServerPermissionProvider;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            final java.lang.String pkgName = intent.getData().getEncodedSchemeSpecificPart();
            final int uid = intent.getIntExtra("android.intent.extra.UID", -1);
            if (intent.getBooleanExtra("android.intent.extra.REPLACING", false) || intent.getBooleanExtra("android.intent.extra.ARCHIVAL", false)) {
                return;
            }
            if (action.equals("android.intent.action.PACKAGE_ADDED")) {
                java.util.concurrent.Executor executor = this.val$audioserverExecutor;
                final com.android.server.audio.AudioServerPermissionProvider audioServerPermissionProvider = this.val$provider;
                executor.execute(new java.lang.Runnable() { // from class: com.android.server.audio.AudioService$8$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        audioServerPermissionProvider.onModifyPackageState(uid, pkgName, false);
                    }
                });
            } else if (action.equals("android.intent.action.PACKAGE_REMOVED")) {
                java.util.concurrent.Executor executor2 = this.val$audioserverExecutor;
                final com.android.server.audio.AudioServerPermissionProvider audioServerPermissionProvider2 = this.val$provider;
                executor2.execute(new java.lang.Runnable() { // from class: com.android.server.audio.AudioService$8$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        audioServerPermissionProvider2.onModifyPackageState(uid, pkgName, true);
                    }
                });
            }
        }
    }

    private static void readAndSetLowRamDevice() {
        boolean isLowRamDevice = android.app.ActivityManager.isLowRamDeviceStatic();
        long totalMemory = 1073741824;
        try {
            android.app.ActivityManager.MemoryInfo info = new android.app.ActivityManager.MemoryInfo();
            android.app.ActivityManager.getService().getMemoryInfo(info);
            totalMemory = info.totalMem;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(TAG, "Cannot obtain MemoryInfo from ActivityManager, assume low memory device");
            isLowRamDevice = true;
        }
        int status = android.media.AudioSystem.setLowRamDevice(isLowRamDevice, totalMemory);
        if (status != 0) {
            android.util.Log.w(TAG, "AudioFlinger informed of device's low RAM attribute; status " + status);
        }
    }

    private void enforceVolumeController(java.lang.String action) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.STATUS_BAR_SERVICE", "Only SystemUI can " + action);
    }

    public void setVolumeController(final android.media.IVolumeController controller) {
        enforceVolumeController("set the volume controller");
        if (this.mVolumeController.isSameBinder(controller)) {
            return;
        }
        this.mVolumeController.postDismiss();
        if (controller != null) {
            try {
                controller.asBinder().linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.audio.AudioService.9
                    @Override // android.os.IBinder.DeathRecipient
                    public void binderDied() {
                        if (com.android.server.audio.AudioService.this.mVolumeController.isSameBinder(controller)) {
                            android.util.Log.w(com.android.server.audio.AudioService.TAG, "Current remote volume controller died, unregistering");
                            com.android.server.audio.AudioService.this.setVolumeController(null);
                        }
                    }
                }, 0);
            } catch (android.os.RemoteException e) {
            }
        }
        this.mVolumeController.setController(controller);
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "Volume controller: " + this.mVolumeController);
        }
    }

    public android.media.IVolumeController getVolumeController() {
        enforceVolumeController("get the volume controller");
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "Volume controller: " + this.mVolumeController);
        }
        return this.mVolumeController.getController();
    }

    public void notifyVolumeControllerVisible(android.media.IVolumeController controller, boolean visible) {
        enforceVolumeController("notify about volume controller visibility");
        if (!this.mVolumeController.isSameBinder(controller)) {
            return;
        }
        this.mVolumeController.setVisible(visible);
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "Volume controller visible: " + visible);
        }
    }

    public void setVolumePolicy(android.media.VolumePolicy policy) {
        enforceVolumeController("set volume policy");
        if (policy != null && !policy.equals(this.mVolumePolicy)) {
            this.mVolumePolicy = policy;
            if (DEBUG_VOL) {
                android.util.Log.d(TAG, "Volume policy changed: " + this.mVolumePolicy);
            }
        }
    }

    public class VolumeController implements com.android.server.audio.AudioService.ISafeHearingVolumeController {
        private static final java.lang.String TAG = "VolumeController";
        private android.media.IVolumeController mController;
        private int mLongPressTimeout;
        private long mNextLongPress;
        private boolean mVisible;

        public VolumeController() {
        }

        public void setController(android.media.IVolumeController controller) {
            this.mController = controller;
            this.mVisible = false;
        }

        public android.media.IVolumeController getController() {
            return this.mController;
        }

        public void loadSettings(android.content.ContentResolver cr) {
            this.mLongPressTimeout = com.android.server.audio.AudioService.this.mSettings.getSecureIntForUser(cr, "long_press_timeout", 500, -2);
        }

        public boolean suppressAdjustment(int resolvedStream, int flags, boolean isMute) {
            if (isMute || resolvedStream != 3 || this.mController == null) {
                return false;
            }
            if (resolvedStream == 3 && com.android.server.audio.AudioService.this.mAudioSystem.isStreamActive(3, this.mLongPressTimeout)) {
                return false;
            }
            long now = android.os.SystemClock.uptimeMillis();
            if ((flags & 1) != 0 && !this.mVisible) {
                if (this.mNextLongPress < now) {
                    this.mNextLongPress = ((long) this.mLongPressTimeout) + now;
                }
                return true;
            }
            if (this.mNextLongPress <= 0) {
                return false;
            }
            if (now > this.mNextLongPress) {
                this.mNextLongPress = 0L;
                return false;
            }
            return true;
        }

        public void setVisible(boolean visible) {
            this.mVisible = visible;
        }

        public boolean isSameBinder(android.media.IVolumeController controller) {
            return java.util.Objects.equals(asBinder(), binder(controller));
        }

        public android.os.IBinder asBinder() {
            return binder(this.mController);
        }

        private android.os.IBinder binder(android.media.IVolumeController controller) {
            if (controller == null) {
                return null;
            }
            return controller.asBinder();
        }

        public java.lang.String toString() {
            return "VolumeController(" + asBinder() + ",mVisible=" + this.mVisible + ")";
        }

        @Override // com.android.server.audio.AudioService.ISafeHearingVolumeController
        public void postDisplaySafeVolumeWarning(int flags) {
            if (this.mController == null) {
                return;
            }
            try {
                this.mController.displaySafeVolumeWarning(flags | 1);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(TAG, "Error calling displaySafeVolumeWarning", e);
            }
        }

        @Override // com.android.server.audio.AudioService.ISafeHearingVolumeController
        public void postDisplayCsdWarning(int csdWarning, int displayDurationMs) {
            if (this.mController == null) {
                android.util.Log.e(TAG, "Unable to display CSD warning, no controller");
                return;
            }
            try {
                this.mController.displayCsdWarning(csdWarning, displayDurationMs);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(TAG, "Error calling displayCsdWarning for warning " + csdWarning, e);
            }
        }

        public void postVolumeChanged(int streamType, int flags) {
            if (this.mController == null) {
                return;
            }
            try {
                this.mController.volumeChanged(streamType, flags);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(TAG, "Error calling volumeChanged", e);
            }
        }

        public void postMasterMuteChanged(int flags) {
            if (this.mController == null) {
                return;
            }
            try {
                this.mController.masterMuteChanged(flags);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(TAG, "Error calling masterMuteChanged", e);
            }
        }

        public void setLayoutDirection(int layoutDirection) {
            if (this.mController == null) {
                return;
            }
            try {
                this.mController.setLayoutDirection(layoutDirection);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(TAG, "Error calling setLayoutDirection", e);
            }
        }

        public void postDismiss() {
            if (this.mController == null) {
                return;
            }
            try {
                this.mController.dismiss();
            } catch (android.os.RemoteException e) {
                android.util.Log.w(TAG, "Error calling dismiss", e);
            }
        }

        public void setA11yMode(int a11yMode) {
            if (this.mController == null) {
                return;
            }
            try {
                this.mController.setA11yMode(a11yMode);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(TAG, "Error calling setA11Mode", e);
            }
        }
    }

    final class AudioServiceInternal extends android.media.AudioManagerInternal {
        AudioServiceInternal() {
        }

        public void setRingerModeDelegate(android.media.AudioManagerInternal.RingerModeDelegate delegate) {
            com.android.server.audio.AudioService.this.mRingerModeDelegate = delegate;
            if (com.android.server.audio.AudioService.this.mRingerModeDelegate != null) {
                synchronized (com.android.server.audio.AudioService.this.mSettingsLock) {
                    com.android.server.audio.AudioService.this.updateRingerAndZenModeAffectedStreams();
                }
                setRingerModeInternal(getRingerModeInternal(), "AS.AudioService.setRingerModeDelegate");
            }
        }

        public int getRingerModeInternal() {
            return com.android.server.audio.AudioService.this.getRingerModeInternal();
        }

        public void setRingerModeInternal(int ringerMode, java.lang.String caller) {
            com.android.server.audio.AudioService.this.setRingerModeInternal(ringerMode, caller);
        }

        public void silenceRingerModeInternal(java.lang.String caller) {
            com.android.server.audio.AudioService.this.silenceRingerModeInternal(caller);
        }

        public void updateRingerModeAffectedStreamsInternal() {
            synchronized (com.android.server.audio.AudioService.this.mSettingsLock) {
                if (com.android.server.audio.AudioService.this.updateRingerAndZenModeAffectedStreams()) {
                    com.android.server.audio.AudioService.this.setRingerModeInt(getRingerModeInternal(), false);
                }
            }
        }

        public void addAssistantServiceUid(int uid) {
            com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, 44, 2, uid, 0, null, 0);
        }

        public void removeAssistantServiceUid(int uid) {
            com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, 45, 2, uid, 0, null, 0);
        }

        public void setActiveAssistantServicesUids(android.util.IntArray activeUids) {
            synchronized (com.android.server.audio.AudioService.this.mSettingsLock) {
                if (activeUids.size() == 0) {
                    com.android.server.audio.AudioService.this.mActiveAssistantServiceUids = com.android.server.audio.AudioService.NO_ACTIVE_ASSISTANT_SERVICE_UIDS;
                } else {
                    boolean changed = com.android.server.audio.AudioService.this.mActiveAssistantServiceUids == null || com.android.server.audio.AudioService.this.mActiveAssistantServiceUids.length != activeUids.size();
                    if (!changed) {
                        int i = 0;
                        while (true) {
                            if (i >= com.android.server.audio.AudioService.this.mActiveAssistantServiceUids.length) {
                                break;
                            }
                            if (activeUids.get(i) == com.android.server.audio.AudioService.this.mActiveAssistantServiceUids[i]) {
                                i++;
                            } else {
                                changed = true;
                                break;
                            }
                        }
                    }
                    if (changed) {
                        com.android.server.audio.AudioService.this.mActiveAssistantServiceUids = activeUids.toArray();
                    }
                }
            }
            com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, 46, 0, 0, 0, null, 0);
        }

        public void setAccessibilityServiceUids(android.util.IntArray uids) {
            if (com.android.server.audio.AudioService.this.isPlatformAutomotive()) {
                return;
            }
            synchronized (com.android.server.audio.AudioService.this.mAccessibilityServiceUidsLock) {
                if (uids.size() == 0) {
                    com.android.server.audio.AudioService.this.mAccessibilityServiceUids = null;
                } else {
                    boolean changed = com.android.server.audio.AudioService.this.mAccessibilityServiceUids == null || com.android.server.audio.AudioService.this.mAccessibilityServiceUids.length != uids.size();
                    if (!changed) {
                        int i = 0;
                        while (true) {
                            if (i >= com.android.server.audio.AudioService.this.mAccessibilityServiceUids.length) {
                                break;
                            }
                            if (uids.get(i) == com.android.server.audio.AudioService.this.mAccessibilityServiceUids[i]) {
                                i++;
                            } else {
                                changed = true;
                                break;
                            }
                        }
                    }
                    if (changed) {
                        com.android.server.audio.AudioService.this.mAccessibilityServiceUids = uids.toArray();
                    }
                }
                com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, 35, 0, 0, 0, null, 0);
            }
        }

        public void setInputMethodServiceUid(int uid) {
            synchronized (com.android.server.audio.AudioService.this.mInputMethodServiceUidLock) {
                if (com.android.server.audio.AudioService.this.mInputMethodServiceUid != uid) {
                    com.android.server.audio.AudioService.this.mAudioSystem.setCurrentImeUid(uid);
                    com.android.server.audio.AudioService.this.mInputMethodServiceUid = uid;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUpdateAccessibilityServiceUids() {
        int[] accessibilityServiceUids;
        synchronized (this.mAccessibilityServiceUidsLock) {
            accessibilityServiceUids = this.mAccessibilityServiceUids;
        }
        android.media.AudioSystem.setA11yServicesUids(accessibilityServiceUids);
    }

    public java.lang.String registerAudioPolicy(android.media.audiopolicy.AudioPolicyConfig policyConfig, android.media.audiopolicy.IAudioPolicyCallback pcb, boolean hasFocusListener, boolean isFocusPolicy, boolean isTestFocusPolicy, boolean isVolumeController, android.media.projection.IMediaProjection projection, android.content.AttributionSource attributionSource) throws android.os.RemoteException {
        java.util.HashMap<android.os.IBinder, com.android.server.audio.AudioService.AudioPolicyProxy> map;
        java.util.Objects.requireNonNull(attributionSource);
        android.media.AudioSystem.setDynamicPolicyCallback(this.mDynPolicyCallback);
        if (!isPolicyRegisterAllowed(policyConfig, isFocusPolicy || isTestFocusPolicy || hasFocusListener, isVolumeController, projection)) {
            android.util.Slog.w(TAG, "Permission denied to register audio policy for pid " + android.os.Binder.getCallingPid() + " / uid " + android.os.Binder.getCallingUid() + ", need system permission or a MediaProjection that can project audio");
            return null;
        }
        java.util.HashMap<android.os.IBinder, com.android.server.audio.AudioService.AudioPolicyProxy> map2 = this.mAudioPolicies;
        synchronized (map2) {
            try {
                try {
                    if (this.mAudioPolicies.containsKey(pcb.asBinder())) {
                        android.util.Slog.e(TAG, "Cannot re-register policy");
                        return null;
                    }
                    try {
                        map = map2;
                        try {
                            com.android.server.audio.AudioService.AudioPolicyProxy app = new com.android.server.audio.AudioService.AudioPolicyProxy(policyConfig, pcb, hasFocusListener, isFocusPolicy, isTestFocusPolicy, isVolumeController, projection, attributionSource);
                            pcb.asBinder().linkToDeath(app, 0);
                            this.mDynPolicyLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("registerAudioPolicy for " + pcb.asBinder() + " u/pid:" + android.os.Binder.getCallingUid() + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + android.os.Binder.getCallingPid() + " with config:" + app.toCompactLogString()).printLog(TAG));
                            app.setClientUid(android.os.Binder.getCallingUid());
                            java.lang.String regId = app.getRegistrationId();
                            this.mAudioPolicies.put(pcb.asBinder(), app);
                            return regId;
                        } catch (android.os.RemoteException e) {
                            e = e;
                            android.util.Slog.w(TAG, "Audio policy registration failed, could not link to " + pcb + " binder death", e);
                            return null;
                        } catch (java.lang.IllegalStateException e2) {
                            e = e2;
                            android.util.Slog.w(TAG, "Audio policy registration failed for binder " + pcb, e);
                            return null;
                        }
                    } catch (android.os.RemoteException e3) {
                        e = e3;
                        map = map2;
                    } catch (java.lang.IllegalStateException e4) {
                        e = e4;
                        map = map2;
                    }
                } catch (java.lang.Throwable th) {
                    e = th;
                    throw e;
                }
            } catch (java.lang.Throwable th2) {
                e = th2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPolicyClientDeath(java.util.List<java.lang.String> addresses) {
        for (java.lang.String address : addresses) {
            if (this.mPlaybackMonitor.hasActiveMediaPlaybackOnSubmixWithAddress(address)) {
                this.mDeviceBroker.postBroadcastBecomingNoisy();
                return;
            }
        }
    }

    private boolean isPolicyRegisterAllowed(android.media.audiopolicy.AudioPolicyConfig policyConfig, boolean hasFocusAccess, boolean isVolumeController, android.media.projection.IMediaProjection projection) {
        boolean requireValidProjection = false;
        boolean requireCaptureAudioOrMediaOutputPerm = false;
        boolean requireModifyRouting = false;
        boolean requireCallAudioInterception = false;
        java.util.ArrayList<android.media.audiopolicy.AudioMix> voiceCommunicationCaptureMixes = null;
        if (hasFocusAccess || isVolumeController || policyConfig.getMixes().isEmpty()) {
            requireModifyRouting = false | true;
        }
        for (android.media.audiopolicy.AudioMix mix : policyConfig.getMixes()) {
            if (mix.getRule().allowPrivilegedMediaPlaybackCapture()) {
                java.lang.String privilegedMediaCaptureError = android.media.audiopolicy.AudioMix.canBeUsedForPrivilegedMediaCapture(mix.getFormat());
                if (privilegedMediaCaptureError != null) {
                    android.util.Log.e(TAG, privilegedMediaCaptureError);
                    return false;
                }
                requireCaptureAudioOrMediaOutputPerm |= true;
            }
            if (mix.containsMatchAttributeRuleForUsage(2) && mix.getRouteFlags() == 3) {
                if (voiceCommunicationCaptureMixes == null) {
                    voiceCommunicationCaptureMixes = new java.util.ArrayList<>();
                }
                voiceCommunicationCaptureMixes.add(mix);
            }
            if (mix.getRouteFlags() == 3 && projection != null) {
                requireValidProjection |= true;
            } else if (mix.isForCallRedirection()) {
                requireCallAudioInterception |= true;
            } else if (mix.containsMatchAttributeRuleForUsage(2)) {
                requireModifyRouting |= true;
            }
        }
        if (requireCaptureAudioOrMediaOutputPerm && !callerHasPermission("android.permission.CAPTURE_MEDIA_OUTPUT") && !callerHasPermission("android.permission.CAPTURE_AUDIO_OUTPUT")) {
            android.util.Log.e(TAG, "Privileged audio capture requires CAPTURE_MEDIA_OUTPUT or CAPTURE_AUDIO_OUTPUT system permission");
            return false;
        }
        if (voiceCommunicationCaptureMixes != null && voiceCommunicationCaptureMixes.size() > 0) {
            if (!callerHasPermission("android.permission.CAPTURE_VOICE_COMMUNICATION_OUTPUT")) {
                android.util.Log.e(TAG, "Audio capture for voice communication requires CAPTURE_VOICE_COMMUNICATION_OUTPUT system permission");
                return false;
            }
            java.util.Iterator<android.media.audiopolicy.AudioMix> it = voiceCommunicationCaptureMixes.iterator();
            while (it.hasNext()) {
                it.next().getRule().setVoiceCommunicationCaptureAllowed(true);
            }
        }
        if (requireValidProjection && !canProjectAudio(projection)) {
            return false;
        }
        if (requireModifyRouting && !callerHasPermission("android.permission.MODIFY_AUDIO_ROUTING")) {
            android.util.Log.e(TAG, "Can not capture audio without MODIFY_AUDIO_ROUTING");
            return false;
        }
        if (!requireCallAudioInterception || callerHasPermission("android.permission.CALL_AUDIO_INTERCEPTION")) {
            return true;
        }
        android.util.Log.e(TAG, "Can not capture audio without CALL_AUDIO_INTERCEPTION");
        return false;
    }

    private boolean callerHasPermission(java.lang.String permission) {
        return this.mContext.checkCallingOrSelfPermission(permission) == 0;
    }

    private boolean canProjectAudio(android.media.projection.IMediaProjection projection) {
        if (projection == null) {
            android.util.Log.e(TAG, "MediaProjection is null");
            return false;
        }
        android.media.projection.IMediaProjectionManager projectionService = getProjectionService();
        if (projectionService == null) {
            android.util.Log.e(TAG, "Can't get service IMediaProjectionManager");
            return false;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            if (!projectionService.isCurrentProjection(projection)) {
                android.util.Log.w(TAG, "App passed invalid MediaProjection token");
                return false;
            }
            try {
                if (projection.canProjectAudio()) {
                    return true;
                }
                android.util.Log.w(TAG, "App passed MediaProjection that can not project audio");
                return false;
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Can't call .canProjectAudio() on valid IMediaProjection" + projection.asBinder(), e);
                return false;
            }
        } catch (android.os.RemoteException e2) {
            android.util.Log.e(TAG, "Can't call .isCurrentProjection() on IMediaProjectionManager" + projectionService.asBinder(), e2);
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
        android.os.Binder.restoreCallingIdentity(token);
    }

    private android.media.projection.IMediaProjectionManager getProjectionService() {
        if (this.mProjectionService == null) {
            android.os.IBinder b = android.os.ServiceManager.getService("media_projection");
            this.mProjectionService = android.media.projection.IMediaProjectionManager.Stub.asInterface(b);
        }
        return this.mProjectionService;
    }

    public void unregisterAudioPolicyAsync(android.media.audiopolicy.IAudioPolicyCallback pcb) {
        if (pcb == null) {
            return;
        }
        unregisterAudioPolicyInt(pcb, "unregisterAudioPolicyAsync");
    }

    public void unregisterAudioPolicy(android.media.audiopolicy.IAudioPolicyCallback pcb) {
        if (pcb == null) {
            return;
        }
        unregisterAudioPolicyInt(pcb, "unregisterAudioPolicy");
    }

    private void unregisterAudioPolicyInt(android.media.audiopolicy.IAudioPolicyCallback pcb, java.lang.String operationName) {
        this.mDynPolicyLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(operationName + " for " + pcb.asBinder()).printLog(TAG));
        synchronized (this.mAudioPolicies) {
            com.android.server.audio.AudioService.AudioPolicyProxy app = this.mAudioPolicies.remove(pcb.asBinder());
            if (app == null) {
                android.util.Slog.w(TAG, "Trying to unregister unknown audio policy for pid " + android.os.Binder.getCallingPid() + " / uid " + android.os.Binder.getCallingUid());
            } else {
                pcb.asBinder().unlinkToDeath(app, 0);
                app.release();
            }
        }
    }

    private com.android.server.audio.AudioService.AudioPolicyProxy checkUpdateForPolicy(android.media.audiopolicy.IAudioPolicyCallback pcb, java.lang.String errorMsg) {
        boolean hasPermissionForPolicy = this.mContext.checkCallingPermission("android.permission.MODIFY_AUDIO_ROUTING") == 0;
        if (!hasPermissionForPolicy) {
            android.util.Slog.w(TAG, errorMsg + " for pid " + android.os.Binder.getCallingPid() + " / uid " + android.os.Binder.getCallingUid() + ", need MODIFY_AUDIO_ROUTING");
            return null;
        }
        com.android.server.audio.AudioService.AudioPolicyProxy app = this.mAudioPolicies.get(pcb.asBinder());
        if (app == null) {
            android.util.Slog.w(TAG, errorMsg + " for pid " + android.os.Binder.getCallingPid() + " / uid " + android.os.Binder.getCallingUid() + ", unregistered policy");
            return null;
        }
        return app;
    }

    public java.util.List<android.media.audiopolicy.AudioMix> getRegisteredPolicyMixes() {
        java.util.List<android.media.audiopolicy.AudioMix> registeredPolicyMixes;
        if (!android.media.audiopolicy.Flags.audioMixTestApi()) {
            return java.util.Collections.emptyList();
        }
        synchronized (this.mAudioPolicies) {
            registeredPolicyMixes = this.mAudioSystem.getRegisteredPolicyMixes();
        }
        return registeredPolicyMixes;
    }

    public int addMixForPolicy(android.media.audiopolicy.AudioPolicyConfig policyConfig, android.media.audiopolicy.IAudioPolicyCallback pcb) {
        if (DEBUG_AP) {
            android.util.Log.d(TAG, "addMixForPolicy for " + pcb.asBinder() + " with config:" + policyConfig);
        }
        synchronized (this.mAudioPolicies) {
            com.android.server.audio.AudioService.AudioPolicyProxy app = checkUpdateForPolicy(pcb, "Cannot add AudioMix in audio policy");
            int i = -1;
            if (app == null) {
                return -1;
            }
            if (app.addMixes(policyConfig.getMixes()) == 0) {
                i = 0;
            }
            return i;
        }
    }

    public int removeMixForPolicy(android.media.audiopolicy.AudioPolicyConfig policyConfig, android.media.audiopolicy.IAudioPolicyCallback pcb) {
        if (DEBUG_AP) {
            android.util.Log.d(TAG, "removeMixForPolicy for " + pcb.asBinder() + " with config:" + policyConfig);
        }
        synchronized (this.mAudioPolicies) {
            com.android.server.audio.AudioService.AudioPolicyProxy app = checkUpdateForPolicy(pcb, "Cannot add AudioMix in audio policy");
            int i = -1;
            if (app == null) {
                return -1;
            }
            if (android.media.audiopolicy.Flags.audioMixOwnership()) {
                for (android.media.audiopolicy.AudioMix mix : policyConfig.getMixes()) {
                    if (!app.getMixes().contains(mix)) {
                        android.util.Slog.e(TAG, "removeMixForPolicy attempted to unregister AudioMix(es) not belonging to the AudioPolicy");
                        return -1;
                    }
                }
            }
            if (app.removeMixes(policyConfig.getMixes()) == 0) {
                i = 0;
            }
            return i;
        }
    }

    public int updateMixingRulesForPolicy(android.media.audiopolicy.AudioMix[] mixesToUpdate, android.media.audiopolicy.AudioMixingRule[] updatedMixingRules, android.media.audiopolicy.IAudioPolicyCallback pcb) {
        super.updateMixingRulesForPolicy_enforcePermission();
        java.util.Objects.requireNonNull(mixesToUpdate);
        java.util.Objects.requireNonNull(updatedMixingRules);
        java.util.Objects.requireNonNull(pcb);
        if (mixesToUpdate.length != updatedMixingRules.length) {
            android.util.Log.e(TAG, "Provided list of audio mixes to update and corresponding mixing rules have mismatching length (mixesToUpdate.length = " + mixesToUpdate.length + ", updatedMixingRules.length = " + updatedMixingRules.length + ").");
            return -1;
        }
        if (DEBUG_AP) {
            android.util.Log.d(TAG, "updateMixingRules for " + pcb.asBinder() + "with mix rules: ");
        }
        synchronized (this.mAudioPolicies) {
            com.android.server.audio.AudioService.AudioPolicyProxy app = checkUpdateForPolicy(pcb, "Cannot add AudioMix in audio policy");
            if (app == null) {
                return -1;
            }
            return app.updateMixingRules(mixesToUpdate, updatedMixingRules) == 0 ? 0 : -1;
        }
    }

    public int setUidDeviceAffinity(android.media.audiopolicy.IAudioPolicyCallback pcb, int uid, int[] deviceTypes, java.lang.String[] deviceAddresses) {
        if (DEBUG_AP) {
            android.util.Log.d(TAG, "setUidDeviceAffinity for " + pcb.asBinder() + " uid:" + uid);
        }
        synchronized (this.mAudioPolicies) {
            com.android.server.audio.AudioService.AudioPolicyProxy app = checkUpdateForPolicy(pcb, "Cannot change device affinity in audio policy");
            if (app == null) {
                return -1;
            }
            if (!app.hasMixRoutedToDevices(deviceTypes, deviceAddresses)) {
                return -1;
            }
            return app.setUidDeviceAffinities(uid, deviceTypes, deviceAddresses);
        }
    }

    public int setUserIdDeviceAffinity(android.media.audiopolicy.IAudioPolicyCallback pcb, int userId, int[] deviceTypes, java.lang.String[] deviceAddresses) {
        if (DEBUG_AP) {
            android.util.Log.d(TAG, "setUserIdDeviceAffinity for " + pcb.asBinder() + " user:" + userId);
        }
        synchronized (this.mAudioPolicies) {
            com.android.server.audio.AudioService.AudioPolicyProxy app = checkUpdateForPolicy(pcb, "Cannot change device affinity in audio policy");
            if (app == null) {
                return -1;
            }
            if (!app.hasMixRoutedToDevices(deviceTypes, deviceAddresses)) {
                return -1;
            }
            return app.setUserIdDeviceAffinities(userId, deviceTypes, deviceAddresses);
        }
    }

    public int removeUidDeviceAffinity(android.media.audiopolicy.IAudioPolicyCallback pcb, int uid) {
        if (DEBUG_AP) {
            android.util.Log.d(TAG, "removeUidDeviceAffinity for " + pcb.asBinder() + " uid:" + uid);
        }
        synchronized (this.mAudioPolicies) {
            com.android.server.audio.AudioService.AudioPolicyProxy app = checkUpdateForPolicy(pcb, "Cannot remove device affinity in audio policy");
            if (app == null) {
                return -1;
            }
            return app.removeUidDeviceAffinities(uid);
        }
    }

    public int removeUserIdDeviceAffinity(android.media.audiopolicy.IAudioPolicyCallback pcb, int userId) {
        if (DEBUG_AP) {
            android.util.Log.d(TAG, "removeUserIdDeviceAffinity for " + pcb.asBinder() + " userId:" + userId);
        }
        synchronized (this.mAudioPolicies) {
            com.android.server.audio.AudioService.AudioPolicyProxy app = checkUpdateForPolicy(pcb, "Cannot remove device affinity in audio policy");
            if (app == null) {
                return -1;
            }
            return app.removeUserIdDeviceAffinities(userId);
        }
    }

    public int setFocusPropertiesForPolicy(int duckingBehavior, android.media.audiopolicy.IAudioPolicyCallback pcb) {
        if (DEBUG_AP) {
            android.util.Log.d(TAG, "setFocusPropertiesForPolicy() duck behavior=" + duckingBehavior + " policy " + pcb.asBinder());
        }
        synchronized (this.mAudioPolicies) {
            com.android.server.audio.AudioService.AudioPolicyProxy app = checkUpdateForPolicy(pcb, "Cannot change audio policy focus properties");
            if (app == null) {
                return -1;
            }
            if (!this.mAudioPolicies.containsKey(pcb.asBinder())) {
                android.util.Slog.e(TAG, "Cannot change audio policy focus properties, unregistered policy");
                return -1;
            }
            boolean z = true;
            if (duckingBehavior == 1) {
                for (com.android.server.audio.AudioService.AudioPolicyProxy policy : this.mAudioPolicies.values()) {
                    if (policy.mFocusDuckBehavior == 1) {
                        android.util.Slog.e(TAG, "Cannot change audio policy ducking behavior, already handled");
                        return -1;
                    }
                }
            }
            app.mFocusDuckBehavior = duckingBehavior;
            com.android.server.audio.MediaFocusControl mediaFocusControl = this.mMediaFocusControl;
            if (duckingBehavior != 1) {
                z = false;
            }
            mediaFocusControl.setDuckingInExtPolicyAvailable(z);
            return 0;
        }
    }

    public java.util.List<android.media.AudioFocusInfo> getFocusStack() {
        super.getFocusStack_enforcePermission();
        return this.mMediaFocusControl.getFocusStack();
    }

    public boolean sendFocusLoss(android.media.AudioFocusInfo focusLoser, android.media.audiopolicy.IAudioPolicyCallback apcb) {
        java.util.Objects.requireNonNull(focusLoser);
        java.util.Objects.requireNonNull(apcb);
        enforceModifyAudioRoutingPermission();
        if (!this.mAudioPolicies.containsKey(apcb.asBinder())) {
            throw new java.lang.IllegalStateException("Only registered AudioPolicy can change focus");
        }
        if (!this.mAudioPolicies.get(apcb.asBinder()).mHasFocusListener) {
            throw new java.lang.IllegalStateException("AudioPolicy must have focus listener to change focus");
        }
        return this.mMediaFocusControl.sendFocusLoss(focusLoser);
    }

    public int setFadeManagerConfigurationForFocusLoss(android.media.FadeManagerConfiguration fmcForFocusLoss) {
        super.setFadeManagerConfigurationForFocusLoss_enforcePermission();
        ensureFadeManagerConfigIsEnabled();
        java.util.Objects.requireNonNull(fmcForFocusLoss, "Fade manager config for focus loss cannot be null");
        validateFadeManagerConfiguration(fmcForFocusLoss);
        return this.mPlaybackMonitor.setFadeManagerConfiguration(-1, fmcForFocusLoss);
    }

    public int clearFadeManagerConfigurationForFocusLoss() {
        super.clearFadeManagerConfigurationForFocusLoss_enforcePermission();
        ensureFadeManagerConfigIsEnabled();
        return this.mPlaybackMonitor.clearFadeManagerConfiguration(-1);
    }

    public android.media.FadeManagerConfiguration getFadeManagerConfigurationForFocusLoss() {
        super.getFadeManagerConfigurationForFocusLoss_enforcePermission();
        ensureFadeManagerConfigIsEnabled();
        return this.mPlaybackMonitor.getFadeManagerConfiguration(-1);
    }

    public android.media.AudioHalVersionInfo getHalVersion() {
        for (android.media.AudioHalVersionInfo version : android.media.AudioHalVersionInfo.VERSIONS) {
            try {
                java.lang.String versionStr = version.getMajorVersion() + "." + version.getMinorVersion();
                java.lang.String hidlStr = java.lang.String.format("android.hardware.audio@%s::IDevicesFactory", versionStr);
                if (android.os.ServiceManager.checkService("android.hardware.audio.core.IModule/default") != null) {
                    return version;
                }
                android.os.HwBinder.getService(hidlStr, "default");
                return version;
            } catch (android.os.RemoteException re) {
                android.util.Log.e(TAG, "Remote exception when getting hardware audio service:", re);
            } catch (java.util.NoSuchElementException e) {
            }
        }
        return null;
    }

    public boolean hasRegisteredDynamicPolicy() {
        boolean z;
        synchronized (this.mAudioPolicies) {
            z = !this.mAudioPolicies.isEmpty();
        }
        return z;
    }

    public int setPreferredMixerAttributes(android.media.AudioAttributes attributes, int portId, android.media.AudioMixerAttributes mixerAttributes) {
        java.util.Objects.requireNonNull(attributes);
        java.util.Objects.requireNonNull(mixerAttributes);
        if (!checkAudioSettingsPermission("setPreferredMixerAttributes()")) {
            return -4;
        }
        int uid = android.os.Binder.getCallingUid();
        int pid = android.os.Binder.getCallingPid();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            java.lang.String logString = android.text.TextUtils.formatSimple("setPreferredMixerAttributes u/pid:%d/%d attr:%s mixerAttributes:%s portId:%d", new java.lang.Object[]{java.lang.Integer.valueOf(uid), java.lang.Integer.valueOf(pid), attributes.toString(), mixerAttributes.toString(), java.lang.Integer.valueOf(portId)});
            sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(logString).printLog(TAG));
            int status = this.mAudioSystem.setPreferredMixerAttributes(attributes, portId, uid, mixerAttributes);
            if (status != 0) {
                android.util.Log.e(TAG, android.text.TextUtils.formatSimple("Error %d in %s)", new java.lang.Object[]{java.lang.Integer.valueOf(status), logString}));
            } else {
                dispatchPreferredMixerAttributesChanged(attributes, portId, mixerAttributes);
            }
            return status;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public int clearPreferredMixerAttributes(android.media.AudioAttributes attributes, int portId) {
        java.util.Objects.requireNonNull(attributes);
        if (!checkAudioSettingsPermission("clearPreferredMixerAttributes()")) {
            return -4;
        }
        int uid = android.os.Binder.getCallingUid();
        int pid = android.os.Binder.getCallingPid();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            java.lang.String logString = android.text.TextUtils.formatSimple("clearPreferredMixerAttributes u/pid:%d/%d attr:%s", new java.lang.Object[]{java.lang.Integer.valueOf(uid), java.lang.Integer.valueOf(pid), attributes.toString()});
            sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(logString).printLog(TAG));
            int status = this.mAudioSystem.clearPreferredMixerAttributes(attributes, portId, uid);
            if (status != 0) {
                android.util.Log.e(TAG, android.text.TextUtils.formatSimple("Error %d in %s)", new java.lang.Object[]{java.lang.Integer.valueOf(status), logString}));
            } else {
                dispatchPreferredMixerAttributesChanged(attributes, portId, null);
            }
            return status;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    void dispatchPreferredMixerAttributesChanged(android.media.AudioAttributes attr, int deviceId, android.media.AudioMixerAttributes mixerAttr) {
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putParcelable(KEY_AUDIO_ATTRIBUTES, attr);
        bundle.putParcelable(KEY_AUDIO_MIXER_ATTRIBUTES, mixerAttr);
        sendBundleMsg(this.mAudioHandler, 52, 2, deviceId, 0, null, bundle, 0);
    }

    public void registerPreferredMixerAttributesDispatcher(android.media.IPreferredMixerAttributesDispatcher dispatcher) {
        if (dispatcher == null) {
            return;
        }
        this.mPrefMixerAttrDispatcher.register(dispatcher);
    }

    public void unregisterPreferredMixerAttributesDispatcher(android.media.IPreferredMixerAttributesDispatcher dispatcher) {
        if (dispatcher == null) {
            return;
        }
        this.mPrefMixerAttrDispatcher.unregister(dispatcher);
    }

    protected void onDispatchPreferredMixerAttributesChanged(android.os.Bundle data, int deviceId) {
        int nbDispathers = this.mPrefMixerAttrDispatcher.beginBroadcast();
        android.media.AudioAttributes attr = (android.media.AudioAttributes) data.getParcelable(KEY_AUDIO_ATTRIBUTES, android.media.AudioAttributes.class);
        android.media.AudioMixerAttributes mixerAttr = (android.media.AudioMixerAttributes) data.getParcelable(KEY_AUDIO_MIXER_ATTRIBUTES, android.media.AudioMixerAttributes.class);
        for (int i = 0; i < nbDispathers; i++) {
            try {
                this.mPrefMixerAttrDispatcher.getBroadcastItem(i).dispatchPrefMixerAttributesChanged(attr, deviceId, mixerAttr);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Can't call dispatchPrefMixerAttributesChanged() IPreferredMixerAttributesDispatcher " + this.mPrefMixerAttrDispatcher.getBroadcastItem(i).asBinder(), e);
            }
        }
        this.mPrefMixerAttrDispatcher.finishBroadcast();
    }

    public boolean supportsBluetoothVariableLatency() {
        super.supportsBluetoothVariableLatency_enforcePermission();
        android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
        try {
            boolean zSupportsBluetoothVariableLatency = android.media.AudioSystem.supportsBluetoothVariableLatency();
            if (ignored != null) {
                ignored.close();
            }
            return zSupportsBluetoothVariableLatency;
        } catch (java.lang.Throwable th) {
            if (ignored != null) {
                try {
                    ignored.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public void setBluetoothVariableLatencyEnabled(boolean enabled) {
        super.setBluetoothVariableLatencyEnabled_enforcePermission();
        android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
        try {
            android.media.AudioSystem.setBluetoothVariableLatencyEnabled(enabled);
            if (ignored != null) {
                ignored.close();
            }
        } catch (java.lang.Throwable th) {
            if (ignored != null) {
                try {
                    ignored.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public boolean isBluetoothVariableLatencyEnabled() {
        super.isBluetoothVariableLatencyEnabled_enforcePermission();
        android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
        try {
            boolean zIsBluetoothVariableLatencyEnabled = android.media.AudioSystem.isBluetoothVariableLatencyEnabled();
            if (ignored != null) {
                ignored.close();
            }
            return zIsBluetoothVariableLatencyEnabled;
        } catch (java.lang.Throwable th) {
            if (ignored != null) {
                try {
                    ignored.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setExtVolumeController(android.media.audiopolicy.IAudioPolicyCallback apc) {
        if (!this.mContext.getResources().getBoolean(android.R.bool.config_faceAuthSupportsSelfIllumination)) {
            android.util.Log.e(TAG, "Cannot set external volume controller: device not set for volume keys handled in PhoneWindowManager");
            return;
        }
        synchronized (this.mExtVolumeControllerLock) {
            if (this.mExtVolumeController != null && !this.mExtVolumeController.asBinder().pingBinder()) {
                android.util.Log.e(TAG, "Cannot set external volume controller: existing controller");
            }
            this.mExtVolumeController = apc;
        }
    }

    private void dumpAudioPolicies(java.io.PrintWriter pw) {
        pw.println("\nAudio policies:");
        synchronized (this.mAudioPolicies) {
            for (com.android.server.audio.AudioService.AudioPolicyProxy policy : this.mAudioPolicies.values()) {
                pw.println(policy.toLogFriendlyString());
            }
        }
    }

    private void ensureFadeManagerConfigIsEnabled() {
        com.android.internal.util.Preconditions.checkState(android.media.audiopolicy.Flags.enableFadeManagerConfiguration(), "Fade manager configuration not supported");
    }

    private void validateFadeManagerConfiguration(android.media.FadeManagerConfiguration fmc) {
        java.util.List<android.media.AudioAttributes> attrs = fmc.getAudioAttributesWithVolumeShaperConfigs();
        for (int index = 0; index < attrs.size(); index++) {
            validateAudioAttributesUsage(attrs.get(index));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDynPolicyMixStateUpdate(java.lang.String regId, int state) {
        if (DEBUG_AP) {
            android.util.Log.d(TAG, "onDynamicPolicyMixStateUpdate(" + regId + ", " + state + ")");
        }
        synchronized (this.mAudioPolicies) {
            for (com.android.server.audio.AudioService.AudioPolicyProxy policy : this.mAudioPolicies.values()) {
                for (android.media.audiopolicy.AudioMix mix : policy.getMixes()) {
                    if (mix.getRegistration().equals(regId)) {
                        try {
                            policy.mPolicyCallback.notifyMixStateUpdate(regId, state);
                        } catch (android.os.RemoteException e) {
                            android.util.Log.e(TAG, "Can't call notifyMixStateUpdate() on IAudioPolicyCallback " + policy.mPolicyCallback.asBinder(), e);
                        }
                        return;
                    }
                }
            }
        }
    }

    public void registerRecordingCallback(android.media.IRecordingConfigDispatcher rcdb) {
        boolean isPrivileged = this.mContext.checkCallingPermission("android.permission.MODIFY_AUDIO_ROUTING") == 0;
        this.mRecordMonitor.registerRecordingCallback(rcdb, isPrivileged);
    }

    public void unregisterRecordingCallback(android.media.IRecordingConfigDispatcher rcdb) {
        this.mRecordMonitor.unregisterRecordingCallback(rcdb);
    }

    public java.util.List<android.media.AudioRecordingConfiguration> getActiveRecordingConfigurations() {
        boolean isPrivileged = android.os.Binder.getCallingUid() == 1000 || this.mContext.checkCallingPermission("android.permission.MODIFY_AUDIO_ROUTING") == 0;
        return this.mRecordMonitor.getActiveRecordingConfigurations(isPrivileged);
    }

    public int trackRecorder(android.os.IBinder recorder) {
        return this.mRecordMonitor.trackRecorder(recorder);
    }

    public void recorderEvent(int riid, int event) {
        this.mRecordMonitor.recorderEvent(riid, event);
    }

    public void releaseRecorder(int riid) {
        this.mRecordMonitor.releaseRecorder(riid);
    }

    public void registerPlaybackCallback(android.media.IPlaybackConfigDispatcher pcdb) {
        boolean isPrivileged = this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_AUDIO_ROUTING") == 0;
        this.mPlaybackMonitor.registerPlaybackCallback(pcdb, isPrivileged);
    }

    public void unregisterPlaybackCallback(android.media.IPlaybackConfigDispatcher pcdb) {
        this.mPlaybackMonitor.unregisterPlaybackCallback(pcdb);
    }

    public java.util.List<android.media.AudioPlaybackConfiguration> getActivePlaybackConfigurations() {
        boolean isPrivileged = this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_AUDIO_ROUTING") == 0;
        return this.mPlaybackMonitor.getActivePlaybackConfigurations(isPrivileged);
    }

    public int trackPlayer(android.media.PlayerBase.PlayerIdCard pic) {
        if (pic != null && pic.mAttributes != null) {
            validateAudioAttributesUsage(pic.mAttributes);
        }
        return this.mPlaybackMonitor.trackPlayer(pic);
    }

    public void playerAttributes(int piid, android.media.AudioAttributes attr) {
        if (attr != null) {
            validateAudioAttributesUsage(attr);
        }
        this.mPlaybackMonitor.playerAttributes(piid, attr, android.os.Binder.getCallingUid());
    }

    public void playerSessionId(int piid, int sessionId) {
        if (sessionId <= 0) {
            throw new java.lang.IllegalArgumentException("invalid session Id " + sessionId);
        }
        this.mPlaybackMonitor.playerSessionId(piid, sessionId, android.os.Binder.getCallingUid());
    }

    public void playerEvent(int piid, int event, int eventValue) {
        this.mPlaybackMonitor.playerEvent(piid, event, eventValue, android.os.Binder.getCallingUid());
    }

    public void portEvent(int portId, int event, android.os.PersistableBundle extras) {
        this.mPlaybackMonitor.portEvent(portId, event, extras, android.os.Binder.getCallingUid());
    }

    public void playerHasOpPlayAudio(int piid, boolean hasOpPlayAudio) {
        this.mPlaybackMonitor.playerHasOpPlayAudio(piid, hasOpPlayAudio, android.os.Binder.getCallingUid());
    }

    public void releasePlayer(int piid) {
        this.mPlaybackMonitor.releasePlayer(piid, android.os.Binder.getCallingUid());
    }

    public int setAllowedCapturePolicy(int capturePolicy) {
        int result;
        int callingUid = android.os.Binder.getCallingUid();
        int flags = android.media.AudioAttributes.capturePolicyToFlags(capturePolicy, 0);
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mPlaybackMonitor) {
                result = this.mAudioSystem.setAllowedCapturePolicy(callingUid, flags);
                if (result == 0) {
                    this.mPlaybackMonitor.setAllowedCapturePolicy(callingUid, capturePolicy);
                }
            }
            return result;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public int getAllowedCapturePolicy() {
        int callingUid = android.os.Binder.getCallingUid();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return this.mPlaybackMonitor.getAllowedCapturePolicy(callingUid);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    boolean isPlaybackActiveForUid(int uid) {
        return this.mPlaybackMonitor.isPlaybackActiveForUid(uid);
    }

    boolean isRecordingActiveForUid(int uid) {
        return this.mRecordMonitor.isRecordingActiveForUid(uid);
    }

    private static final class AudioDeviceArray {
        final java.lang.String[] mDeviceAddresses;
        final int[] mDeviceTypes;

        AudioDeviceArray(int[] types, java.lang.String[] addresses) {
            this.mDeviceTypes = types;
            this.mDeviceAddresses = addresses;
        }
    }

    public class AudioPolicyProxy extends android.media.audiopolicy.AudioPolicyConfig implements android.os.IBinder.DeathRecipient {
        private static final java.lang.String TAG = "AudioPolicyProxy";
        final android.content.AttributionSource mAttributionSource;
        int mClientUid;
        int mFocusDuckBehavior;
        final boolean mHasFocusListener;
        boolean mIsFocusPolicy;
        boolean mIsTestFocusPolicy;
        final boolean mIsVolumeController;
        final android.media.audiopolicy.IAudioPolicyCallback mPolicyCallback;
        final android.media.projection.IMediaProjection mProjection;
        com.android.server.audio.AudioService.AudioPolicyProxy.UnregisterOnStopCallback mProjectionCallback;
        final java.util.HashMap<java.lang.Integer, com.android.server.audio.AudioService.AudioDeviceArray> mUidDeviceAffinities;
        final java.util.HashMap<java.lang.Integer, com.android.server.audio.AudioService.AudioDeviceArray> mUserIdDeviceAffinities;

        private final class UnregisterOnStopCallback extends android.media.projection.IMediaProjectionCallback.Stub {
            private UnregisterOnStopCallback() {
            }

            public void onStop() {
                com.android.server.audio.AudioService.this.unregisterAudioPolicyAsync(com.android.server.audio.AudioService.AudioPolicyProxy.this.mPolicyCallback);
            }

            public void onCapturedContentResize(int width, int height) {
            }

            public void onCapturedContentVisibilityChanged(boolean isVisible) {
            }
        }

        AudioPolicyProxy(android.media.audiopolicy.AudioPolicyConfig config, android.media.audiopolicy.IAudioPolicyCallback token, boolean hasFocusListener, boolean isFocusPolicy, boolean isTestFocusPolicy, boolean isVolumeController, android.media.projection.IMediaProjection projection, android.content.AttributionSource attributionSource) {
            super(config);
            this.mUidDeviceAffinities = new java.util.HashMap<>();
            this.mUserIdDeviceAffinities = new java.util.HashMap<>();
            this.mFocusDuckBehavior = 0;
            this.mIsFocusPolicy = false;
            this.mIsTestFocusPolicy = false;
            this.mClientUid = -1;
            java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append(config.hashCode()).append(":ap:");
            int i = com.android.server.audio.AudioService.this.mAudioPolicyCounter;
            com.android.server.audio.AudioService.this.mAudioPolicyCounter = i + 1;
            setRegistration(new java.lang.String(sbAppend.append(i).toString()));
            this.mPolicyCallback = token;
            this.mAttributionSource = attributionSource;
            this.mHasFocusListener = hasFocusListener;
            this.mIsVolumeController = isVolumeController;
            this.mProjection = projection;
            if (this.mHasFocusListener) {
                com.android.server.audio.AudioService.this.mMediaFocusControl.addFocusFollower(this.mPolicyCallback);
                if (isFocusPolicy) {
                    this.mIsFocusPolicy = true;
                    this.mIsTestFocusPolicy = isTestFocusPolicy;
                    com.android.server.audio.AudioService.this.mMediaFocusControl.setFocusPolicy(this.mPolicyCallback, this.mIsTestFocusPolicy);
                }
            }
            if (this.mIsVolumeController) {
                com.android.server.audio.AudioService.this.setExtVolumeController(this.mPolicyCallback);
            }
            if (this.mProjection != null) {
                this.mProjectionCallback = new com.android.server.audio.AudioService.AudioPolicyProxy.UnregisterOnStopCallback();
                try {
                    this.mProjection.registerCallback(this.mProjectionCallback);
                } catch (android.os.RemoteException e) {
                    release();
                    throw new java.lang.IllegalStateException("MediaProjection callback registration failed, could not link to " + projection + " binder death", e);
                }
            }
            int status = connectMixes();
            if (status != 0) {
                release();
                throw new java.lang.IllegalStateException("Could not connect mix, error: " + status);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setClientUid(int uid) {
            this.mClientUid = uid;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.audio.AudioService.this.mDynPolicyLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("AudioPolicy " + this.mPolicyCallback.asBinder() + " died").printLog(TAG));
            java.util.List<java.lang.String> addresses = new java.util.ArrayList<>();
            for (android.media.audiopolicy.AudioMix mix : this.mMixes) {
                addresses.add(mix.getRegistration());
            }
            com.android.server.audio.AudioService.this.onPolicyClientDeath(addresses);
            release();
            com.android.server.audio.AudioService.this.mAsExt.resetExAudioFocusState(this.mClientUid);
        }

        java.lang.String getRegistrationId() {
            return getRegistration();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void release() {
            if (this.mIsFocusPolicy) {
                com.android.server.audio.AudioService.this.mMediaFocusControl.unsetFocusPolicy(this.mPolicyCallback, this.mIsTestFocusPolicy);
            }
            if (this.mFocusDuckBehavior == 1) {
                com.android.server.audio.AudioService.this.mMediaFocusControl.setDuckingInExtPolicyAvailable(false);
            }
            if (this.mHasFocusListener) {
                com.android.server.audio.AudioService.this.mMediaFocusControl.removeFocusFollower(this.mPolicyCallback);
            }
            if (this.mProjectionCallback != null) {
                try {
                    this.mProjection.unregisterCallback(this.mProjectionCallback);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(TAG, "Fail to unregister Audiopolicy callback from MediaProjection");
                }
            }
            if (this.mIsVolumeController) {
                synchronized (com.android.server.audio.AudioService.this.mExtVolumeControllerLock) {
                    com.android.server.audio.AudioService.this.mExtVolumeController = null;
                }
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                if (android.media.audiopolicy.Flags.audioMixOwnership()) {
                    synchronized (this.mMixes) {
                        removeMixes(new java.util.ArrayList<>(this.mMixes));
                    }
                } else {
                    com.android.server.audio.AudioService.this.mAudioSystem.registerPolicyMixes(this.mMixes, false);
                }
                android.os.Binder.restoreCallingIdentity(identity);
                synchronized (com.android.server.audio.AudioService.this.mAudioPolicies) {
                    com.android.server.audio.AudioService.this.mAudioPolicies.remove(this.mPolicyCallback.asBinder());
                }
                try {
                    this.mPolicyCallback.notifyUnregistration();
                } catch (android.os.RemoteException e2) {
                }
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(identity);
                throw th;
            }
        }

        boolean hasMixAffectingUsage(int usage, int excludedFlags) {
            for (android.media.audiopolicy.AudioMix mix : this.mMixes) {
                if (mix.isAffectingUsage(usage) && (mix.getRouteFlags() & excludedFlags) != excludedFlags) {
                    return true;
                }
            }
            return false;
        }

        boolean hasMixRoutedToDevices(int[] deviceTypes, java.lang.String[] deviceAddresses) {
            for (int i = 0; i < deviceTypes.length; i++) {
                boolean hasDevice = false;
                java.util.Iterator it = this.mMixes.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    android.media.audiopolicy.AudioMix mix = (android.media.audiopolicy.AudioMix) it.next();
                    if (mix.isRoutedToDevice(deviceTypes[i], deviceAddresses[i])) {
                        hasDevice = true;
                        break;
                    }
                }
                if (!hasDevice) {
                    return false;
                }
            }
            return true;
        }

        int addMixes(java.util.ArrayList<android.media.audiopolicy.AudioMix> mixes) {
            synchronized (this.mMixes) {
                if (android.media.audiopolicy.Flags.audioMixOwnership()) {
                    for (android.media.audiopolicy.AudioMix mix : mixes) {
                        setMixRegistration(mix);
                        mix.setVirtualDeviceId(this.mAttributionSource.getDeviceId());
                    }
                    int result = com.android.server.audio.AudioService.this.mAudioSystem.registerPolicyMixes(mixes, true);
                    if (result == 0) {
                        add(mixes);
                    }
                    return result;
                }
                add(mixes);
                return com.android.server.audio.AudioService.this.mAudioSystem.registerPolicyMixes(mixes, true);
            }
        }

        int removeMixes(java.util.ArrayList<android.media.audiopolicy.AudioMix> mixes) {
            int iRegisterPolicyMixes;
            synchronized (this.mMixes) {
                remove(mixes);
                iRegisterPolicyMixes = com.android.server.audio.AudioService.this.mAudioSystem.registerPolicyMixes(mixes, false);
            }
            return iRegisterPolicyMixes;
        }

        int connectMixes() {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                for (android.media.audiopolicy.AudioMix mix : this.mMixes) {
                    mix.setVirtualDeviceId(this.mAttributionSource.getDeviceId());
                }
                return com.android.server.audio.AudioService.this.mAudioSystem.registerPolicyMixes(this.mMixes, true);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        int updateMixingRules(android.media.audiopolicy.AudioMix[] mixesToUpdate, android.media.audiopolicy.AudioMixingRule[] updatedMixingRules) {
            int ret;
            java.util.Objects.requireNonNull(mixesToUpdate);
            java.util.Objects.requireNonNull(updatedMixingRules);
            for (android.media.audiopolicy.AudioMix mix : mixesToUpdate) {
                mix.setVirtualDeviceId(this.mAttributionSource.getDeviceId());
            }
            if (mixesToUpdate.length != updatedMixingRules.length) {
                android.util.Log.e(TAG, "Provided list of audio mixes to update and corresponding mixing rules have mismatching length (mixesToUpdate.length = " + mixesToUpdate.length + ", updatedMixingRules.length = " + updatedMixingRules.length + ").");
                return -2;
            }
            synchronized (this.mMixes) {
                android.media.permission.SafeCloseable unused = android.media.permission.ClearCallingIdentityContext.create();
                try {
                    ret = com.android.server.audio.AudioService.this.mAudioSystem.updateMixingRules(mixesToUpdate, updatedMixingRules);
                    if (ret == 0) {
                        for (int i = 0; i < mixesToUpdate.length; i++) {
                            final android.media.audiopolicy.AudioMix audioMixToUpdate = mixesToUpdate[i];
                            final android.media.audiopolicy.AudioMixingRule audioMixingRule = updatedMixingRules[i];
                            java.util.stream.Stream stream = this.mMixes.stream();
                            java.util.Objects.requireNonNull(audioMixToUpdate);
                            stream.filter(new java.util.function.Predicate() { // from class: com.android.server.audio.AudioService$AudioPolicyProxy$$ExternalSyntheticLambda0
                                @Override // java.util.function.Predicate
                                public final boolean test(java.lang.Object obj) {
                                    return audioMixToUpdate.equals((android.media.audiopolicy.AudioMix) obj);
                                }
                            }).findAny().ifPresent(new java.util.function.Consumer() { // from class: com.android.server.audio.AudioService$AudioPolicyProxy$$ExternalSyntheticLambda1
                                @Override // java.util.function.Consumer
                                public final void accept(java.lang.Object obj) {
                                    ((android.media.audiopolicy.AudioMix) obj).setAudioMixingRule(audioMixingRule);
                                }
                            });
                        }
                    }
                    if (unused != null) {
                        unused.close();
                    }
                } finally {
                }
            }
            return ret;
        }

        int setUidDeviceAffinities(int uid, int[] types, java.lang.String[] addresses) {
            java.lang.Integer Uid = new java.lang.Integer(uid);
            if (this.mUidDeviceAffinities.remove(Uid) != null && removeUidDeviceAffinitiesFromSystem(uid) != 0) {
                android.util.Log.e(TAG, "AudioSystem. removeUidDeviceAffinities(" + uid + ") failed,  cannot call AudioSystem.setUidDeviceAffinities");
                return -1;
            }
            com.android.server.audio.AudioService.AudioDeviceArray deviceArray = new com.android.server.audio.AudioService.AudioDeviceArray(types, addresses);
            if (setUidDeviceAffinitiesOnSystem(uid, deviceArray) == 0) {
                this.mUidDeviceAffinities.put(Uid, deviceArray);
                return 0;
            }
            android.util.Log.e(TAG, "AudioSystem. setUidDeviceAffinities(" + uid + ") failed");
            return -1;
        }

        int removeUidDeviceAffinities(int uid) {
            if (this.mUidDeviceAffinities.remove(new java.lang.Integer(uid)) != null && removeUidDeviceAffinitiesFromSystem(uid) == 0) {
                return 0;
            }
            android.util.Log.e(TAG, "AudioSystem. removeUidDeviceAffinities failed");
            return -1;
        }

        private int removeUidDeviceAffinitiesFromSystem(int uid) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.audio.AudioService.this.mAudioSystem.removeUidDeviceAffinities(uid);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        private int setUidDeviceAffinitiesOnSystem(int uid, com.android.server.audio.AudioService.AudioDeviceArray deviceArray) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.audio.AudioService.this.mAudioSystem.setUidDeviceAffinities(uid, deviceArray.mDeviceTypes, deviceArray.mDeviceAddresses);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        int setUserIdDeviceAffinities(int userId, int[] types, java.lang.String[] addresses) {
            java.lang.Integer UserId = new java.lang.Integer(userId);
            if (this.mUserIdDeviceAffinities.remove(UserId) != null && removeUserIdDeviceAffinitiesFromSystem(userId) != 0) {
                android.util.Log.e(TAG, "AudioSystem. removeUserIdDeviceAffinities(" + UserId + ") failed,  cannot call AudioSystem.setUserIdDeviceAffinities");
                return -1;
            }
            com.android.server.audio.AudioService.AudioDeviceArray audioDeviceArray = new com.android.server.audio.AudioService.AudioDeviceArray(types, addresses);
            if (setUserIdDeviceAffinitiesOnSystem(userId, audioDeviceArray) == 0) {
                this.mUserIdDeviceAffinities.put(UserId, audioDeviceArray);
                return 0;
            }
            android.util.Log.e(TAG, "AudioSystem.setUserIdDeviceAffinities(" + userId + ") failed");
            return -1;
        }

        int removeUserIdDeviceAffinities(int userId) {
            if (this.mUserIdDeviceAffinities.remove(new java.lang.Integer(userId)) != null && removeUserIdDeviceAffinitiesFromSystem(userId) == 0) {
                return 0;
            }
            android.util.Log.e(TAG, "AudioSystem.removeUserIdDeviceAffinities failed");
            return -1;
        }

        private int removeUserIdDeviceAffinitiesFromSystem(int userId) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.audio.AudioService.this.mAudioSystem.removeUserIdDeviceAffinities(userId);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        private int setUserIdDeviceAffinitiesOnSystem(int userId, com.android.server.audio.AudioService.AudioDeviceArray deviceArray) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.audio.AudioService.this.mAudioSystem.setUserIdDeviceAffinities(userId, deviceArray.mDeviceTypes, deviceArray.mDeviceAddresses);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        int setupDeviceAffinities() {
            for (java.util.Map.Entry<java.lang.Integer, com.android.server.audio.AudioService.AudioDeviceArray> uidEntry : this.mUidDeviceAffinities.entrySet()) {
                int uidStatus = removeUidDeviceAffinitiesFromSystem(uidEntry.getKey().intValue());
                if (uidStatus != 0) {
                    android.util.Log.e(TAG, "setupDeviceAffinities failed to remove device affinity for uid " + uidEntry.getKey());
                    return uidStatus;
                }
                int uidStatus2 = setUidDeviceAffinitiesOnSystem(uidEntry.getKey().intValue(), uidEntry.getValue());
                if (uidStatus2 != 0) {
                    android.util.Log.e(TAG, "setupDeviceAffinities failed to set device affinity for uid " + uidEntry.getKey());
                    return uidStatus2;
                }
            }
            for (java.util.Map.Entry<java.lang.Integer, com.android.server.audio.AudioService.AudioDeviceArray> userIdEntry : this.mUserIdDeviceAffinities.entrySet()) {
                int userIdStatus = removeUserIdDeviceAffinitiesFromSystem(userIdEntry.getKey().intValue());
                if (userIdStatus != 0) {
                    android.util.Log.e(TAG, "setupDeviceAffinities failed to remove device affinity for userId " + userIdEntry.getKey());
                    return userIdStatus;
                }
                int userIdStatus2 = setUserIdDeviceAffinitiesOnSystem(userIdEntry.getKey().intValue(), userIdEntry.getValue());
                if (userIdStatus2 != 0) {
                    android.util.Log.e(TAG, "setupDeviceAffinities failed to set device affinity for userId " + userIdEntry.getKey());
                    return userIdStatus2;
                }
            }
            return 0;
        }

        public java.lang.String toLogFriendlyString() {
            java.lang.String textDump = (((((super.toLogFriendlyString() + " Uid Device Affinities:\n") + logFriendlyAttributeDeviceArrayMap("Uid", this.mUidDeviceAffinities, "     ")) + " UserId Device Affinities:\n") + logFriendlyAttributeDeviceArrayMap("UserId", this.mUserIdDeviceAffinities, "     ")) + " Proxy:\n") + "   is focus policy= " + this.mIsFocusPolicy + "\n";
            if (this.mIsFocusPolicy) {
                textDump = ((textDump + "     focus duck behaviour= " + this.mFocusDuckBehavior + "\n") + "     is test focus policy= " + this.mIsTestFocusPolicy + "\n") + "     has focus listener= " + this.mHasFocusListener + "\n";
            }
            return textDump + "   media projection= " + this.mProjection + "\n";
        }

        private java.lang.String logFriendlyAttributeDeviceArrayMap(java.lang.String attribute, java.util.Map<java.lang.Integer, com.android.server.audio.AudioService.AudioDeviceArray> map, java.lang.String spacer) {
            java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
            for (java.util.Map.Entry<java.lang.Integer, com.android.server.audio.AudioService.AudioDeviceArray> mapEntry : map.entrySet()) {
                stringBuilder.append(spacer).append(attribute).append(": ").append(mapEntry.getKey()).append("\n");
                com.android.server.audio.AudioService.AudioDeviceArray deviceArray = mapEntry.getValue();
                java.lang.String deviceSpacer = spacer + "   ";
                for (int i = 0; i < deviceArray.mDeviceTypes.length; i++) {
                    stringBuilder.append(deviceSpacer).append("Type: 0x").append(java.lang.Integer.toHexString(deviceArray.mDeviceTypes[i])).append(" Address: ").append(deviceArray.mDeviceAddresses[i]).append("\n");
                }
            }
            return stringBuilder.toString();
        }
    }

    public int dispatchFocusChange(android.media.AudioFocusInfo afi, int focusChange, android.media.audiopolicy.IAudioPolicyCallback pcb) {
        int iDispatchFocusChange;
        if (afi == null) {
            throw new java.lang.IllegalArgumentException("Illegal null AudioFocusInfo");
        }
        if (pcb == null) {
            throw new java.lang.IllegalArgumentException("Illegal null AudioPolicy callback");
        }
        synchronized (this.mAudioPolicies) {
            if (!this.mAudioPolicies.containsKey(pcb.asBinder())) {
                throw new java.lang.IllegalStateException("Unregistered AudioPolicy for focus dispatch");
            }
            iDispatchFocusChange = this.mMediaFocusControl.dispatchFocusChange(afi, focusChange);
        }
        return iDispatchFocusChange;
    }

    public void setFocusRequestResultFromExtPolicy(android.media.AudioFocusInfo afi, int requestResult, android.media.audiopolicy.IAudioPolicyCallback pcb) {
        if (afi == null) {
            throw new java.lang.IllegalArgumentException("Illegal null AudioFocusInfo");
        }
        if (pcb == null) {
            throw new java.lang.IllegalArgumentException("Illegal null AudioPolicy callback");
        }
        synchronized (this.mAudioPolicies) {
            if (!this.mAudioPolicies.containsKey(pcb.asBinder())) {
                throw new java.lang.IllegalStateException("Unregistered AudioPolicy for external focus");
            }
            this.mMediaFocusControl.setFocusRequestResultFromExtPolicy(afi, requestResult);
        }
    }

    public int dispatchFocusChangeWithFade(android.media.AudioFocusInfo afi, int focusChange, android.media.audiopolicy.IAudioPolicyCallback pcb, java.util.List<android.media.AudioFocusInfo> otherActiveAfis, android.media.FadeManagerConfiguration transientFadeMgrConfig) {
        int status;
        super.dispatchFocusChangeWithFade_enforcePermission();
        ensureFadeManagerConfigIsEnabled();
        java.util.Objects.requireNonNull(afi, "AudioFocusInfo cannot be null");
        java.util.Objects.requireNonNull(pcb, "AudioPolicy callback cannot be null");
        java.util.Objects.requireNonNull(otherActiveAfis, "Other active AudioFocusInfo list cannot be null");
        if (transientFadeMgrConfig != null) {
            validateFadeManagerConfiguration(transientFadeMgrConfig);
        }
        synchronized (this.mAudioPolicies) {
            com.android.internal.util.Preconditions.checkState(this.mAudioPolicies.containsKey(pcb.asBinder()), "Unregistered AudioPolicy for focus dispatch with fade");
            if (transientFadeMgrConfig != null) {
                this.mPlaybackMonitor.setTransientFadeManagerConfiguration(focusChange, transientFadeMgrConfig);
            }
            status = this.mMediaFocusControl.dispatchFocusChangeWithFade(afi, focusChange, otherActiveAfis);
            if (transientFadeMgrConfig != null) {
                this.mPlaybackMonitor.clearTransientFadeManagerConfiguration(focusChange);
            }
        }
        return status;
    }

    public boolean shouldNotificationSoundPlay(android.media.AudioAttributes aa) {
        super.shouldNotificationSoundPlay_enforcePermission();
        java.util.Objects.requireNonNull(aa);
        int stream = android.media.AudioAttributes.toLegacyStreamType(aa);
        boolean mutingFromVolume = getStreamVolume(stream) == 0;
        if (mutingFromVolume) {
            android.util.Slog.i(TAG, "shouldNotificationSoundPlay false: muted stream:" + stream + " attr:" + aa);
            return false;
        }
        int uid = this.mMediaFocusControl.getExclusiveFocusOwnerUid();
        if (uid == -1) {
            return true;
        }
        boolean mutingFromFocusAndRecording = this.mRecordMonitor.isRecordingActiveForUid(uid);
        if (!mutingFromFocusAndRecording) {
            return true;
        }
        android.util.Slog.i(TAG, "shouldNotificationSoundPlay false: exclusive focus owner recording  uid:" + uid + " attr:" + aa);
        return false;
    }

    private class AsdProxy implements android.os.IBinder.DeathRecipient {
        private final android.media.IAudioServerStateDispatcher mAsd;

        AsdProxy(android.media.IAudioServerStateDispatcher asd) {
            this.mAsd = asd;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.audio.AudioService.this.mAudioServerStateListeners) {
                com.android.server.audio.AudioService.this.mAudioServerStateListeners.remove(this.mAsd.asBinder());
            }
        }

        android.media.IAudioServerStateDispatcher callback() {
            return this.mAsd;
        }
    }

    private void checkMonitorAudioServerStatePermission() {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") != 0 && this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_AUDIO_ROUTING") != 0) {
            throw new java.lang.SecurityException("Not allowed to monitor audioserver state");
        }
    }

    public void registerAudioServerStateDispatcher(android.media.IAudioServerStateDispatcher asd) {
        checkMonitorAudioServerStatePermission();
        synchronized (this.mAudioServerStateListeners) {
            if (this.mAudioServerStateListeners.containsKey(asd.asBinder())) {
                android.util.Slog.w(TAG, "Cannot re-register audio server state dispatcher");
                return;
            }
            com.android.server.audio.AudioService.AsdProxy asdp = new com.android.server.audio.AudioService.AsdProxy(asd);
            try {
                asd.asBinder().linkToDeath(asdp, 0);
            } catch (android.os.RemoteException e) {
            }
            this.mAudioServerStateListeners.put(asd.asBinder(), asdp);
        }
    }

    public void unregisterAudioServerStateDispatcher(android.media.IAudioServerStateDispatcher asd) {
        checkMonitorAudioServerStatePermission();
        synchronized (this.mAudioServerStateListeners) {
            com.android.server.audio.AudioService.AsdProxy asdp = this.mAudioServerStateListeners.remove(asd.asBinder());
            if (asdp == null) {
                android.util.Slog.w(TAG, "Trying to unregister unknown audioserver state dispatcher for pid " + android.os.Binder.getCallingPid() + " / uid " + android.os.Binder.getCallingUid());
            } else {
                asd.asBinder().unlinkToDeath(asdp, 0);
            }
        }
    }

    public boolean isAudioServerRunning() {
        checkMonitorAudioServerStatePermission();
        return android.media.AudioSystem.checkAudioFlinger() == 0;
    }

    private void getAudioAidlHalPids(java.util.HashSet<java.lang.Integer> pids) {
        try {
            android.os.ServiceDebugInfo[] infos = android.os.ServiceManager.getServiceDebugInfo();
            if (infos == null) {
                return;
            }
            for (android.os.ServiceDebugInfo info : infos) {
                if (info.debugPid > 0 && info.name.startsWith(AUDIO_HAL_SERVICE_PREFIX)) {
                    pids.add(java.lang.Integer.valueOf(info.debugPid));
                }
            }
        } catch (java.lang.RuntimeException e) {
        }
    }

    private void getAudioHalHidlPids(java.util.HashSet<java.lang.Integer> pids) {
        try {
            android.hidl.manager.V1_0.IServiceManager serviceManager = android.hidl.manager.V1_0.IServiceManager.getService();
            java.util.ArrayList<android.hidl.manager.V1_0.IServiceManager.InstanceDebugInfo> dump = serviceManager.debugDump();
            for (android.hidl.manager.V1_0.IServiceManager.InstanceDebugInfo info : dump) {
                if (info.pid != -1 && info.interfaceName != null && info.interfaceName.startsWith(AUDIO_HAL_SERVICE_PREFIX)) {
                    pids.add(java.lang.Integer.valueOf(info.pid));
                }
            }
        } catch (android.os.RemoteException | java.lang.RuntimeException e) {
        }
    }

    private java.util.Set<java.lang.Integer> getAudioHalPids() {
        java.util.HashSet<java.lang.Integer> pids = new java.util.HashSet<>();
        getAudioAidlHalPids(pids);
        getAudioHalHidlPids(pids);
        return pids;
    }

    private void updateAudioHalPids() {
        java.util.Set<java.lang.Integer> pidsSet = getAudioHalPids();
        if (pidsSet.isEmpty()) {
            android.util.Slog.w(TAG, "Could not retrieve audio HAL service pids");
        } else {
            int[] pidsArray = pidsSet.stream().mapToInt(new com.android.server.audio.AudioService$$ExternalSyntheticLambda0()).toArray();
            android.media.AudioSystem.setAudioHalPids(pidsArray);
        }
    }

    public void setMultiAudioFocusEnabled(boolean enabled) {
        super.setMultiAudioFocusEnabled_enforcePermission();
        if (this.mMediaFocusControl != null) {
            boolean mafEnabled = this.mMediaFocusControl.getMultiAudioFocusEnabled();
            if (mafEnabled != enabled) {
                this.mMediaFocusControl.updateMultiAudioFocus(enabled);
                if (!enabled) {
                    this.mDeviceBroker.postBroadcastBecomingNoisy();
                }
            }
        }
    }

    public void cacheParameters(java.lang.String keyValuePairs) {
        java.lang.String[] kvpairs = keyValuePairs.split(";");
        for (java.lang.String pair : kvpairs) {
            java.lang.String[] kv = pair.split("=");
            if (mCachedParams.containsKey(kv[0])) {
                java.lang.String oldVal = mCachedParams.put(kv[0], kv[1]);
                android.util.Slog.w(TAG, "Updated cached param " + kv[0] + " from " + oldVal + " to " + kv[1]);
            }
        }
    }

    public void cacheBinauralRecordParameters(java.lang.String kvpairs) {
        if (kvpairs != null) {
            java.lang.String[] kv = kvpairs.split("=");
            if (kv[0].equals("binaural_recording_switch")) {
                mBRStateOfCamera = kv[1].equals("1");
            }
        }
    }

    public boolean setAdditionalOutputDeviceDelay(android.media.AudioDeviceAttributes device, long delayMillis) {
        java.util.Objects.requireNonNull(device, "device must not be null");
        enforceModifyAudioRoutingPermission();
        android.media.AudioDeviceAttributes device2 = retrieveBluetoothAddress(device);
        java.lang.String getterKey = "additional_output_device_delay=" + device2.getInternalType() + "," + device2.getAddress();
        java.lang.String setterKey = getterKey + "," + delayMillis;
        return this.mRestorableParameters.setParameters(getterKey, setterKey) == 0;
    }

    public long getAdditionalOutputDeviceDelay(android.media.AudioDeviceAttributes device) {
        java.util.Objects.requireNonNull(device, "device must not be null");
        android.media.AudioDeviceAttributes device2 = retrieveBluetoothAddress(device);
        java.lang.String reply = android.media.AudioSystem.getParameters("additional_output_device_delay=" + device2.getInternalType() + "," + device2.getAddress());
        try {
            long delayMillis = java.lang.Long.parseLong(reply.substring("additional_output_device_delay".length() + 1));
            return delayMillis;
        } catch (java.lang.NullPointerException e) {
            return 0L;
        }
    }

    public long getMaxAdditionalOutputDeviceDelay(android.media.AudioDeviceAttributes device) {
        java.util.Objects.requireNonNull(device, "device must not be null");
        android.media.AudioDeviceAttributes device2 = retrieveBluetoothAddress(device);
        java.lang.String reply = android.media.AudioSystem.getParameters("max_additional_output_device_delay=" + device2.getInternalType() + "," + device2.getAddress());
        try {
            long delayMillis = java.lang.Long.parseLong(reply.substring("max_additional_output_device_delay".length() + 1));
            return delayMillis;
        } catch (java.lang.NullPointerException e) {
            return 0L;
        }
    }

    public void addAssistantServicesUids(int[] assistantUids) {
        super.addAssistantServicesUids_enforcePermission();
        java.util.Objects.requireNonNull(assistantUids);
        synchronized (this.mSettingsLock) {
            addAssistantServiceUidsLocked(assistantUids);
        }
    }

    public void removeAssistantServicesUids(int[] assistantUids) {
        super.removeAssistantServicesUids_enforcePermission();
        java.util.Objects.requireNonNull(assistantUids);
        synchronized (this.mSettingsLock) {
            removeAssistantServiceUidsLocked(assistantUids);
        }
    }

    public int[] getAssistantServicesUids() {
        int[] assistantUids;
        super.getAssistantServicesUids_enforcePermission();
        synchronized (this.mSettingsLock) {
            assistantUids = this.mAssistantUids.stream().mapToInt(new com.android.server.audio.AudioService$$ExternalSyntheticLambda0()).toArray();
        }
        return assistantUids;
    }

    public void setActiveAssistantServiceUids(int[] activeAssistantUids) {
        super.setActiveAssistantServiceUids_enforcePermission();
        java.util.Objects.requireNonNull(activeAssistantUids);
        synchronized (this.mSettingsLock) {
            this.mActiveAssistantServiceUids = activeAssistantUids;
        }
        updateActiveAssistantServiceUids();
    }

    public int[] getActiveAssistantServiceUids() {
        int[] activeAssistantUids;
        super.getActiveAssistantServiceUids_enforcePermission();
        synchronized (this.mSettingsLock) {
            activeAssistantUids = (int[]) this.mActiveAssistantServiceUids.clone();
        }
        return activeAssistantUids;
    }

    java.util.List<java.lang.String> getDeviceIdentityAddresses(android.media.AudioDeviceAttributes device) {
        return this.mDeviceBroker.getDeviceIdentityAddresses(device);
    }

    com.android.server.audio.MusicFxHelper getMusicFxHelper() {
        return this.mMusicFxHelper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isFixedVolumeDevice(int deviceType) {
        if (deviceType == 32768 && this.mRecordMonitor.isLegacyRemoteSubmixActive()) {
            return false;
        }
        return this.mFixedVolumeDevices.contains(java.lang.Integer.valueOf(deviceType));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isFullVolumeDevice(int deviceType) {
        if (deviceType == 32768 && this.mRecordMonitor.isLegacyRemoteSubmixActive()) {
            return false;
        }
        return this.mFullVolumeDevices.contains(java.lang.Integer.valueOf(deviceType));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAbsoluteVolumeDevice(int deviceType) {
        return this.mAbsoluteVolumeDeviceInfoMap.containsKey(java.lang.Integer.valueOf(deviceType));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isA2dpAbsoluteVolumeDevice(int deviceType) {
        return this.mAvrcpAbsVolSupported && android.media.AudioSystem.DEVICE_OUT_ALL_A2DP_SET.contains(java.lang.Integer.valueOf(deviceType));
    }

    private static java.lang.String getSettingsNameForDeviceVolumeBehavior(int deviceType) {
        return "AudioService_DeviceVolumeBehavior_" + android.media.AudioSystem.getOutputDeviceName(deviceType);
    }

    private void persistDeviceVolumeBehavior(int deviceType, int deviceVolumeBehavior) {
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "Persisting Volume Behavior for DeviceType: " + deviceType);
        }
        long callingIdentity = android.os.Binder.clearCallingIdentity();
        try {
            this.mSettings.putSystemIntForUser(this.mContentResolver, getSettingsNameForDeviceVolumeBehavior(deviceType), deviceVolumeBehavior, -2);
        } finally {
            android.os.Binder.restoreCallingIdentity(callingIdentity);
        }
    }

    private int retrieveStoredDeviceVolumeBehavior(int deviceType) {
        return this.mSettings.getSystemIntForUser(this.mContentResolver, getSettingsNameForDeviceVolumeBehavior(deviceType), -1, -2);
    }

    private void restoreDeviceVolumeBehavior() {
        java.util.Iterator it = android.media.AudioSystem.DEVICE_OUT_ALL_SET.iterator();
        while (it.hasNext()) {
            int deviceType = ((java.lang.Integer) it.next()).intValue();
            if (DEBUG_VOL) {
                android.util.Log.d(TAG, "Retrieving Volume Behavior for DeviceType: " + deviceType);
            }
            int deviceVolumeBehavior = retrieveStoredDeviceVolumeBehavior(deviceType);
            if (deviceVolumeBehavior == -1) {
                if (DEBUG_VOL) {
                    android.util.Log.d(TAG, "Skipping Setting Volume Behavior for DeviceType: " + deviceType);
                }
            } else {
                setDeviceVolumeBehaviorInternal(new android.media.AudioDeviceAttributes(deviceType, ""), deviceVolumeBehavior, "AudioService.restoreDeviceVolumeBehavior()");
            }
        }
    }

    private boolean hasDeviceVolumeBehavior(int audioSystemDeviceOut) {
        return retrieveStoredDeviceVolumeBehavior(audioSystemDeviceOut) != -1;
    }

    private boolean addAudioSystemDeviceOutToFixedVolumeDevices(int audioSystemDeviceOut) {
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "Adding DeviceType: 0x" + java.lang.Integer.toHexString(audioSystemDeviceOut) + " to mFixedVolumeDevices");
        }
        return this.mFixedVolumeDevices.add(java.lang.Integer.valueOf(audioSystemDeviceOut));
    }

    private boolean removeAudioSystemDeviceOutFromFixedVolumeDevices(int audioSystemDeviceOut) {
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "Removing DeviceType: 0x" + java.lang.Integer.toHexString(audioSystemDeviceOut) + " from mFixedVolumeDevices");
        }
        return this.mFixedVolumeDevices.remove(java.lang.Integer.valueOf(audioSystemDeviceOut));
    }

    private boolean addAudioSystemDeviceOutToFullVolumeDevices(int audioSystemDeviceOut) {
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "Adding DeviceType: 0x" + java.lang.Integer.toHexString(audioSystemDeviceOut) + " to mFullVolumeDevices");
        }
        return this.mFullVolumeDevices.add(java.lang.Integer.valueOf(audioSystemDeviceOut));
    }

    private boolean removeAudioSystemDeviceOutFromFullVolumeDevices(int audioSystemDeviceOut) {
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "Removing DeviceType: 0x" + java.lang.Integer.toHexString(audioSystemDeviceOut) + " from mFullVolumeDevices");
        }
        return this.mFullVolumeDevices.remove(java.lang.Integer.valueOf(audioSystemDeviceOut));
    }

    private void addAudioSystemDeviceOutToAbsVolumeDevices(int audioSystemDeviceOut, com.android.server.audio.AudioService.AbsoluteVolumeDeviceInfo info) {
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "Adding DeviceType: 0x" + java.lang.Integer.toHexString(audioSystemDeviceOut) + " to mAbsoluteVolumeDeviceInfoMap with behavior " + android.media.AudioDeviceVolumeManager.volumeBehaviorName(info.mDeviceVolumeBehavior));
        }
        this.mAbsoluteVolumeDeviceInfoMap.put(java.lang.Integer.valueOf(audioSystemDeviceOut), info);
    }

    private com.android.server.audio.AudioService.AbsoluteVolumeDeviceInfo removeAudioSystemDeviceOutFromAbsVolumeDevices(int audioSystemDeviceOut) {
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "Removing DeviceType: 0x" + java.lang.Integer.toHexString(audioSystemDeviceOut) + " from mAbsoluteVolumeDeviceInfoMap");
        }
        return this.mAbsoluteVolumeDeviceInfoMap.remove(java.lang.Integer.valueOf(audioSystemDeviceOut));
    }

    private boolean checkNoteAppOp(int op, int uid, java.lang.String packageName, java.lang.String attributionTag) {
        try {
            if (this.mAppOps.noteOp(op, uid, packageName, attributionTag, (java.lang.String) null) != 0) {
                return false;
            }
            return true;
        } catch (java.lang.Exception e) {
            android.util.Log.e(TAG, "Error noting op:" + op + " on uid:" + uid + " for package:" + packageName, e);
            return false;
        }
    }

    public int getBleCgVolume() {
        int leAudioVolIndex = 0;
        int leAudioVssVol = getVssVolumeForDevice(0, 536870912);
        if (leAudioVssVol > 0) {
            leAudioVolIndex = leAudioVssVol / 10;
        }
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "getBleCgVolume() index=" + leAudioVolIndex);
        }
        return leAudioVolIndex;
    }

    public int getLastHfpScoVolume() {
        android.media.AudioDeviceAttributes deviceAttributes = this.mDeviceBroker.getHeadsetAudioDevice();
        if (deviceAttributes == null && android.os.Build.isMtkPlatform() && this.mAsSocExt != null) {
            deviceAttributes = this.mAsSocExt.getLeAudioDevice();
        }
        if (deviceAttributes == null) {
            if (DEBUG_VOL) {
                android.util.Log.d(TAG, "getLastHfpScoVolume error, headset device is null.");
                return -1;
            }
            return -1;
        }
        int scoAudioVssVol = getVssVolumeForDevice(6, deviceAttributes.getInternalType());
        if (scoAudioVssVol > 0) {
            scoAudioVssVol /= 10;
        }
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "getLastHfpScoVolume() index=" + scoAudioVssVol);
        }
        return scoAudioVssVol;
    }

    public int rescaleCgVolumeIndexToHfpVolumeIndex(int index) {
        int rescaled = ((this.mStreamStates[6].getMaxIndex() * index) + (this.mStreamStates[0].getMaxIndex() / 2)) / this.mStreamStates[0].getMaxIndex();
        if (rescaled < this.mStreamStates[6].getMinIndex()) {
            rescaled = this.mStreamStates[6].getMinIndex();
        }
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "rescaleCgVolumeIndexToHfpVolumeIndex()" + index + "->" + rescaled);
        }
        this.mDeviceBroker.postSetVolumeIndexOnDevice(6, rescaled, 32, "rescaleCgVolumeIndexToHfpVolumeIndex");
        this.mDeviceBroker.postSetVolumeIndexOnDevice(6, rescaled, 16, "rescaleCgVolumeIndexToHfpVolumeIndex");
        this.mDeviceBroker.postSetVolumeIndexOnDevice(6, rescaled, 64, "rescaleCgVolumeIndexToHfpVolumeIndex");
        return rescaled;
    }

    public int rescaleHfpVolumeIndexToCgVolumeIndex(int index) {
        int rescaled = ((this.mStreamStates[0].getMaxIndex() * index) + (this.mStreamStates[6].getMaxIndex() / 2)) / this.mStreamStates[6].getMaxIndex();
        if (rescaled < this.mStreamStates[0].getMinIndex()) {
            rescaled = this.mStreamStates[0].getMinIndex();
        }
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "rescaleHfpVolumeIndexToCgVolumeIndex()" + index + "->" + rescaled);
        }
        this.mDeviceBroker.postSetVolumeIndexOnDevice(0, rescaled, 536870912, "rescaleCgVolumeIndexToHfpVolumeIndex");
        return rescaled;
    }

    private void setRescaleStreamVolume(int stream, int device, int index) {
        if (stream != 0 && stream != 6) {
            return;
        }
        if (device != 536870912 && !android.media.AudioSystem.isBluetoothScoOutDevice(device)) {
            return;
        }
        if (DEBUG_VOL) {
            android.util.Log.d(TAG, "setRescaleStreamVolume() stream: " + stream + " ,device: " + device + " ,index: " + index);
        }
        if (stream == 0 && device == 536870912) {
            rescaleCgVolumeIndexToHfpVolumeIndex(index);
        }
        if (stream == 6 && android.media.AudioSystem.isBluetoothScoOutDevice(device)) {
            rescaleHfpVolumeIndexToCgVolumeIndex(index);
        }
    }

    boolean isVendorBeforeAndroidU() {
        return this.mVendorBeforeAndroidU;
    }

    void handleRecordingConfigurationChanged(int event, int sampleRate, int uid, int source, android.media.AudioDeviceInfo deviceInfo) {
        if (deviceInfo == null) {
            android.util.Log.d(TAG, "deviceInfo is null");
            return;
        }
        android.util.Log.d(TAG, "handleRecordingConfigurationChanged: event = " + event + " ,sampleRate = " + sampleRate + " ,uid = " + uid + " ,source = " + source + " ,device = " + deviceInfo.getType());
        if (source == 7) {
            android.util.Log.d(TAG, "device is voiceCommunication, return");
        } else {
            this.mDeviceBroker.handleRecordingConfigurationChanged(event, sampleRate, uid, deviceInfo);
        }
    }

    public boolean isBluetoothLeCgOn() {
        boolean mBleCgstatus = false;
        if (this.mAsSocExt.isBluetoothLeTbsDeviceActive()) {
            mBleCgstatus = this.mAsSocExt.isBluetoothLeCgStateOn();
            if (DEBUG_MODE) {
                android.util.Log.d(TAG, "isBluetoothLeCgOn mBleCgstatus:" + mBleCgstatus);
            }
        }
        return mBleCgstatus;
    }

    public void setParameters(java.lang.String keyValuePairs) {
        int callingUid = android.os.Binder.getCallingUid();
        this.mAsExt.oplusSetParameters(keyValuePairs, callingUid);
    }

    public java.lang.String getParameters(java.lang.String keyValuePairs) {
        int callingUid = android.os.Binder.getCallingUid();
        return this.mAsExt.oplusGetParameters(keyValuePairs, callingUid);
    }

    public boolean setStreamVolumePermission() {
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        return this.mAsExt.oplusSetStreamVolumePermission(callingPid, callingUid);
    }

    public com.android.server.audio.IAudioServiceWrapper getWrapper() {
        return this.mAsWrapper;
    }

    private class AudioServiceWrapper implements com.android.server.audio.IAudioServiceWrapper {
        private AudioServiceWrapper() {
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public com.android.server.audio.IAudioServiceExt getExtImpl() {
            return com.android.server.audio.AudioService.this.mAsExt;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public com.android.server.audio.AudioDeviceBroker getDeviceBroker() {
            return com.android.server.audio.AudioService.this.mDeviceBroker;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public com.android.server.audio.AudioSystemAdapter getAudioSystem() {
            return com.android.server.audio.AudioService.this.mAudioSystem;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public com.android.server.audio.SystemServerAdapter getSystemServer() {
            return com.android.server.audio.AudioService.this.mSystemServer;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public void setDebugLog(boolean on) {
            com.android.server.audio.AudioService.mDebugLog = on;
            com.android.server.audio.AudioService.DEBUG_DEVICES = on;
            com.android.server.audio.AudioService.DEBUG_MODE = on;
            com.android.server.audio.AudioService.DEBUG_AP = on;
            com.android.server.audio.AudioService.DEBUG_VOL = on;
            com.android.server.audio.AudioService.DEBUG_DEVICES = on;
            com.android.server.audio.AudioService.DEBUG_COMM_RTE = on;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public int[] getMaxStreamVolume() {
            return com.android.server.audio.AudioService.MAX_STREAM_VOLUME;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public int[] getMinStreamVolume() {
            return com.android.server.audio.AudioService.MIN_STREAM_VOLUME;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public int getSetModeDeathHandlersLength() {
            return com.android.server.audio.AudioService.this.mSetModeDeathHandlers.size();
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public int getUidOfSetModeDeathHandler(int index) {
            return com.android.server.audio.AudioService.this.mSetModeDeathHandlers.get(index).getUid();
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public int getPidOfSetModeDeathHandler(int index) {
            return com.android.server.audio.AudioService.this.mSetModeDeathHandlers.get(index).getPid();
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public int getModeOfSetModeDeathHandler(int index) {
            com.android.server.audio.AudioService.SetModeDeathHandler h = com.android.server.audio.AudioService.this.mSetModeDeathHandlers.get(index);
            return h.getMode();
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public android.content.ContentResolver getContentResolver() {
            return com.android.server.audio.AudioService.this.mContentResolver;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public int getPlatformType() {
            return com.android.server.audio.AudioService.this.mPlatformType;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public int getFlagAdjustVolume() {
            return 1;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public android.media.VolumePolicy getVolumePolicy() {
            return com.android.server.audio.AudioService.this.mVolumePolicy;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public boolean getHasVibrator() {
            return com.android.server.audio.AudioService.this.mHasVibrator;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public void setPrevVolDirection(int direction) {
            com.android.server.audio.AudioService.this.mPrevVolDirection = direction;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public void sendMessage(int msg, int existingMsgPolicy, int arg1, int arg2, java.lang.Object obj, int delay) {
            com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, msg, existingMsgPolicy, arg1, arg2, obj, delay);
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public int[] getStreamVolumeAlias() {
            return com.android.server.audio.AudioService.mStreamVolumeAlias;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public com.android.server.audio.MediaFocusControl getMediaFocusControl() {
            return com.android.server.audio.AudioService.this.mMediaFocusControl;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public com.android.server.audio.RecordingActivityMonitor getRecordMonitor() {
            return com.android.server.audio.AudioService.this.mRecordMonitor;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public com.android.server.audio.PlaybackActivityMonitor getPlaybackMonitor() {
            return com.android.server.audio.AudioService.this.mPlaybackMonitor;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public void setRingerMode(int ringerMode, java.lang.String caller, boolean external) {
            com.android.server.audio.AudioService.this.setRingerMode(ringerMode, caller, external);
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public int getDeviceForStream(int stream) {
            return com.android.server.audio.AudioService.this.getDeviceForStream(stream);
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public void setStreamVolumeInt(int streamType, int index, int device, boolean force, java.lang.String caller, boolean hasModifyAudioSettings) {
            com.android.server.audio.AudioService.this.setStreamVolumeInt(streamType, index, device, force, caller, hasModifyAudioSettings);
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public int getRingerModeInternal() {
            return com.android.server.audio.AudioService.this.getRingerModeInternal();
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public void avrcpSupportsAbsoluteVolume(java.lang.String address, boolean support) {
            com.android.server.audio.AudioService.this.avrcpSupportsAbsoluteVolume(address, support);
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public boolean getAvrcpAbsVolSupported() {
            return com.android.server.audio.AudioService.this.mAvrcpAbsVolSupported;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public java.lang.String getVolumeIndexSettingNameForIsMutedForStream(int streamType) {
            if (com.android.server.audio.AudioService.this.mStreamStates[streamType] == null) {
                return null;
            }
            return com.android.server.audio.AudioService.this.mStreamStates[streamType].mVolumeIndexSettingNameForIsMuted;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public void sendMsgForStream(int msg, int existingMsgPolicy, int arg1, int arg2, int streamType, int delay) {
            com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, 10, 2, arg1, arg2, com.android.server.audio.AudioService.this.mStreamStates[streamType], delay);
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public void oplusSendMsg(int msg, int existingMsgPolicy, int arg1, int arg2, java.lang.Object obj, int delay) {
            com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, msg, existingMsgPolicy, arg1, arg2, obj, delay);
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public void updateDeviceChangeForMusic(int prevDevices, int newDevice) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = com.android.server.audio.AudioService.this.mStreamStates[3].mStreamDevicesChanged;
            args.arg2 = com.android.server.audio.AudioService.this.mStreamStates[3].mStreamDevicesChangedOptions;
            com.android.server.audio.AudioService.sendMsg(com.android.server.audio.AudioService.this.mAudioHandler, 32, 2, prevDevices, newDevice, args, 0);
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public int getMediaVolumeIndexByDevice(int device) {
            return com.android.server.audio.AudioService.this.mStreamStates[3].mMediaVolumeIndexMap.get(device, -1);
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public boolean setIndexForStream(int streamType, int index, int device, java.lang.String caller, boolean hasModifyAudioSettings) {
            return com.android.server.audio.AudioService.this.mStreamStates[streamType].setIndex(index, device, caller, hasModifyAudioSettings);
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public int getIndex(int streamType, int device) {
            return com.android.server.audio.AudioService.this.mStreamStates[streamType].getIndex(device);
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public boolean mutePhoneForStream(int streamType, boolean state) {
            return com.android.server.audio.AudioService.this.mStreamStates[streamType].muteForPhone(state);
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public int getStreamMaxIndex(int streamType) {
            return com.android.server.audio.AudioService.this.mStreamStates[streamType].getMaxIndex();
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public boolean getStreamMuteState(int streamType) {
            return com.android.server.audio.AudioService.this.mStreamStates[streamType].mIsMuted;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public boolean getAudioModeOwnerHandlerCheck() {
            return com.android.server.audio.AudioService.this.getAudioModeOwnerHandler() == null;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public int getCurrentAudioModeOwnerPid() {
            com.android.server.audio.AudioService.SetModeDeathHandler currentModeHandler = com.android.server.audio.AudioService.this.getAudioModeOwnerHandler();
            if (currentModeHandler != null) {
                return currentModeHandler.getPid();
            }
            return -1;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public int getCurrentAudioModeOwnerUid() {
            com.android.server.audio.AudioService.SetModeDeathHandler currentModeHandler = com.android.server.audio.AudioService.this.getAudioModeOwnerHandler();
            if (currentModeHandler != null) {
                return currentModeHandler.getUid();
            }
            return -1;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public java.lang.Object getCurrentAudioModeOwnerCb() {
            com.android.server.audio.AudioService.SetModeDeathHandler currentModeHandler = com.android.server.audio.AudioService.this.getAudioModeOwnerHandler();
            if (currentModeHandler != null) {
                return currentModeHandler.getBinder();
            }
            return null;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public java.lang.String getCurrentAudioModeOwnerPkgName() {
            synchronized (com.android.server.audio.AudioService.this.mDeviceBroker.mSetModeLock) {
                com.android.server.audio.AudioService.SetModeDeathHandler currentModeHandler = com.android.server.audio.AudioService.this.getAudioModeOwnerHandler();
                if (currentModeHandler == null) {
                    return null;
                }
                return currentModeHandler.getPackage();
            }
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public boolean getCurrentAudioModeOwnerRecordingStatus() {
            synchronized (com.android.server.audio.AudioService.this.mDeviceBroker.mSetModeLock) {
                com.android.server.audio.AudioService.SetModeDeathHandler currentModeHandler = com.android.server.audio.AudioService.this.getAudioModeOwnerHandler();
                if (currentModeHandler == null || com.android.server.audio.AudioService.this.mRecordMonitor == null) {
                    return false;
                }
                return com.android.server.audio.AudioService.this.mRecordMonitor.isRecordingActiveForUid(currentModeHandler.getUid());
            }
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public void setSpatializerEnabled(boolean enable) {
            com.android.server.audio.AudioService.this.setSpatializerEnabled(enable);
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public void setDesiredHeadTrackingMode(int mode) {
            com.android.server.audio.AudioService.this.setDesiredHeadTrackingMode(mode);
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public void setHeadTrackerEnabled(boolean enabled, android.media.AudioDeviceAttributes ada) {
            com.android.server.audio.AudioService.this.setHeadTrackerEnabled(enabled, ada);
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public int getDesiredHeadTrackingMode() {
            return com.android.server.audio.AudioService.this.getDesiredHeadTrackingMode();
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public com.android.server.audio.SoundDoseHelper getSoundDoseHelper() {
            return com.android.server.audio.AudioService.this.mSoundDoseHelper;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public void broadcastSafeVolume() {
            android.content.Intent intent = new android.content.Intent("android.media.action.SET_SAFE_VOLUME");
            intent.putExtra("android.intent.extra.user_handle", com.android.server.audio.AudioService.this.mSoundDoseHelper.getSafeVolumeIntentOwnerId());
            com.android.server.audio.AudioService.this.sendBroadcastToAll(intent, null);
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public void setHoloDeviceSupportState(boolean flag, boolean isBleDevice) {
            int state = 0;
            if (flag && (com.android.server.audio.AudioService.this.mAsExt.isCarkitBt() || com.android.server.audio.AudioService.this.mDeviceBroker.isLoudSpeakerBt())) {
                android.util.Log.d(com.android.server.audio.AudioService.TAG, "checkHoloDeviceAndSetValue : device unsupport.");
                state = 1;
            } else if (!flag || !isBleDevice || !com.android.server.audio.AudioService.this.mDeviceBroker.isUnsuHoloVoipDevice()) {
                android.util.Log.v(com.android.server.audio.AudioService.TAG, "checkHoloDeviceAndSetValue : device support.");
            } else {
                android.util.Log.d(com.android.server.audio.AudioService.TAG, "checkHoloDeviceAndSetValue : device unsupport voip.");
                state = 2;
            }
            java.lang.String param = com.android.server.audio.AudioService.HOLO_DEVICE_COMPAT_STATE + state;
            com.android.server.audio.AudioService.this.mAudioSystem.setParameters(param);
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public boolean isUnsupportHoloDevice(java.lang.String device) {
            boolean status = com.android.server.audio.AudioService.this.mAsExt.isUnsupportHoloDevice(device);
            return status;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public boolean getMicMuteFromApi() {
            return com.android.server.audio.AudioService.this.mMicMuteFromApi;
        }

        @Override // com.android.server.audio.IAudioServiceWrapper
        public int getCurrentDeviceRoute() {
            return com.android.server.audio.AudioService.mCurrentDeviceRoute;
        }
    }
}
