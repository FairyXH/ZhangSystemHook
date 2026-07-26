package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class HdmiControlService extends com.android.server.SystemService {
    static final int DEVICE_CLEANUP_TIMEOUT = 5000;
    private static final int EARC_TRIGGER_START_ARC_ACTION_DELAY = 500;
    static final int INITIATED_BY_BOOT_UP = 1;
    static final int INITIATED_BY_ENABLE_CEC = 0;
    static final int INITIATED_BY_ENABLE_EARC = 6;
    static final int INITIATED_BY_HOTPLUG = 4;
    static final int INITIATED_BY_SCREEN_ON = 2;
    static final int INITIATED_BY_SOUNDBAR_MODE = 5;
    static final int INITIATED_BY_WAKE_UP_MESSAGE = 3;
    static final java.lang.String PERMISSION = "android.permission.HDMI_CEC";
    static final int STANDBY_SCREEN_OFF = 0;
    static final int STANDBY_SHUTDOWN = 1;
    private static final java.lang.String TAG = "HdmiControlService";
    static final int WAKE_UP_BOOT_UP = 1;
    static final int WAKE_UP_SCREEN_ON = 0;
    private com.android.server.hdmi.HdmiControlService.AbsoluteVolumeChangedListener mAbsoluteVolumeChangedListener;

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private int mActivePortId;
    protected final com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource mActiveSource;
    private boolean mAddressAllocated;
    private com.android.server.hdmi.HdmiCecAtomWriter mAtomWriter;
    private java.util.Map<android.media.AudioDeviceAttributes, java.lang.Integer> mAudioDeviceVolumeBehaviors;
    private com.android.server.hdmi.AudioDeviceVolumeManagerWrapper mAudioDeviceVolumeManager;
    private com.android.server.hdmi.AudioManagerWrapper mAudioManager;
    private com.android.server.hdmi.HdmiCecController mCecController;
    private final java.util.List<java.lang.Integer> mCecLocalDevices;
    private com.android.server.hdmi.CecMessageBuffer mCecMessageBuffer;
    private int mCecVersion;
    private com.android.server.hdmi.DeviceConfigWrapper mDeviceConfig;
    private final java.util.ArrayList<com.android.server.hdmi.HdmiControlService.DeviceEventListenerRecord> mDeviceEventListenerRecords;
    private android.hardware.display.DisplayManager mDisplayManager;
    private android.hardware.hdmi.IHdmiControlCallback mDisplayStatusCallback;
    private com.android.server.hdmi.HdmiEarcController mEarcController;
    private boolean mEarcEnabled;
    private com.android.server.hdmi.HdmiEarcLocalDevice mEarcLocalDevice;
    private int mEarcPortId;
    private boolean mEarcSupported;

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private boolean mEarcTxFeatureFlagEnabled;
    private final android.os.Handler mHandler;
    private com.android.server.hdmi.HdmiCecConfig mHdmiCecConfig;
    private com.android.server.hdmi.HdmiCecNetwork mHdmiCecNetwork;
    private final android.util.ArrayMap<java.lang.String, android.os.RemoteCallbackList<android.hardware.hdmi.IHdmiCecSettingChangeListener>> mHdmiCecSettingChangeListenerRecords;
    private int mHdmiCecVolumeControl;
    private final android.os.RemoteCallbackList<android.hardware.hdmi.IHdmiCecVolumeControlFeatureListener> mHdmiCecVolumeControlFeatureListenerRecords;
    private final com.android.server.hdmi.HdmiControlService.HdmiControlBroadcastReceiver mHdmiControlBroadcastReceiver;
    private int mHdmiControlEnabled;
    private final java.util.ArrayList<com.android.server.hdmi.HdmiControlService.HdmiControlStatusChangeListenerRecord> mHdmiControlStatusChangeListenerRecords;
    private final java.util.ArrayList<com.android.server.hdmi.HdmiControlService.HotplugEventListenerRecord> mHotplugEventListenerRecords;
    private com.android.server.hdmi.HdmiControlService.InputChangeListenerRecord mInputChangeListenerRecord;
    private android.os.Looper mIoLooper;
    private final android.os.HandlerThread mIoThread;
    private boolean mIsCecAvailable;

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private int mLastInputMhl;
    private final java.lang.Object mLock;

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private java.lang.String mMenuLanguage;
    private com.android.server.hdmi.HdmiMhlControllerStub mMhlController;
    private java.util.List<android.hardware.hdmi.HdmiDeviceInfo> mMhlDevices;
    private boolean mMhlInputChangeEnabled;
    private final java.util.ArrayList<com.android.server.hdmi.HdmiControlService.HdmiMhlVendorCommandListenerRecord> mMhlVendorCommandListenerRecords;

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private boolean mNumericSoundbarVolumeUiOnTvFeatureFlagEnabled;
    private android.hardware.hdmi.IHdmiControlCallback mOtpCallbackPendingAddressAllocation;
    private com.android.server.hdmi.PowerManagerWrapper mPowerManager;
    private com.android.server.hdmi.PowerManagerInternalWrapper mPowerManagerInternal;
    private com.android.server.hdmi.HdmiCecPowerStatusController mPowerStatusController;
    private boolean mProhibitMode;
    private com.android.server.hdmi.HdmiControlService.HdmiRecordListenerRecord mRecordListenerRecord;
    private final com.android.server.hdmi.SelectRequestBuffer mSelectRequestBuffer;
    private final java.util.concurrent.Executor mServiceThreadExecutor;
    private com.android.server.hdmi.HdmiCecConfig.SettingChangeListener mSettingChangeListener;
    private final com.android.server.hdmi.HdmiControlService.SettingsObserver mSettingsObserver;

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private boolean mSoundbarModeFeatureFlagEnabled;

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private boolean mStandbyMessageReceived;
    private int mStreamMusicMaxVolume;
    private boolean mSystemAudioActivated;
    private final java.util.ArrayList<com.android.server.hdmi.HdmiControlService.SystemAudioModeChangeListenerRecord> mSystemAudioModeChangeListenerRecords;

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private boolean mTransitionFromArcToEarcTxEnabled;
    private android.media.tv.TvInputManager mTvInputManager;
    private final java.util.ArrayList<com.android.server.hdmi.HdmiControlService.VendorCommandListenerRecord> mVendorCommandListenerRecords;
    private com.android.server.hdmi.WakeLockWrapper mWakeLock;

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private boolean mWakeUpMessageReceived;
    private static final java.util.Locale HONG_KONG = new java.util.Locale("zh", "HK");
    private static final java.util.Locale MACAU = new java.util.Locale("zh", "MO");
    private static final java.util.Map<java.lang.String, java.lang.String> sTerminologyToBibliographicMap = createsTerminologyToBibliographicMap();
    static final android.media.AudioDeviceAttributes AUDIO_OUTPUT_DEVICE_HDMI = new android.media.AudioDeviceAttributes(2, 9, "");
    static final android.media.AudioDeviceAttributes AUDIO_OUTPUT_DEVICE_HDMI_ARC = new android.media.AudioDeviceAttributes(2, 10, "");
    static final android.media.AudioDeviceAttributes AUDIO_OUTPUT_DEVICE_HDMI_EARC = new android.media.AudioDeviceAttributes(2, 29, "");
    private static final java.util.List<android.media.AudioDeviceAttributes> AVB_AUDIO_OUTPUT_DEVICES = java.util.List.of(AUDIO_OUTPUT_DEVICE_HDMI, AUDIO_OUTPUT_DEVICE_HDMI_ARC, AUDIO_OUTPUT_DEVICE_HDMI_EARC);
    private static final java.util.List<android.media.AudioDeviceAttributes> TV_AVB_AUDIO_OUTPUT_DEVICES = java.util.List.of(AUDIO_OUTPUT_DEVICE_HDMI_ARC, AUDIO_OUTPUT_DEVICE_HDMI_EARC);
    private static final java.util.List<android.media.AudioDeviceAttributes> PLAYBACK_AVB_AUDIO_OUTPUT_DEVICES = java.util.List.of(AUDIO_OUTPUT_DEVICE_HDMI);
    private static final java.util.List<java.lang.Integer> ABSOLUTE_VOLUME_BEHAVIORS = java.util.List.of(3, 5);
    private static final java.util.List<java.lang.Integer> FULL_AND_ABSOLUTE_VOLUME_BEHAVIORS = java.util.List.of(1, 3, 5);
    static final android.media.AudioAttributes STREAM_MUSIC_ATTRIBUTES = new android.media.AudioAttributes.Builder().setLegacyStreamType(3).build();

    interface DevicePollingCallback {
        void onPollingFinished(java.util.List<java.lang.Integer> list);
    }

    interface SendMessageCallback {
        void onSendCompleted(int i);
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface WakeReason {
    }

    private static java.util.Map<java.lang.String, java.lang.String> createsTerminologyToBibliographicMap() {
        java.util.Map<java.lang.String, java.lang.String> temp = new java.util.HashMap<>();
        temp.put("sqi", "alb");
        temp.put("hye", "arm");
        temp.put("eus", "baq");
        temp.put("mya", "bur");
        temp.put("ces", "cze");
        temp.put("nld", "dut");
        temp.put("kat", "geo");
        temp.put("deu", "ger");
        temp.put("ell", "gre");
        temp.put("fra", "fre");
        temp.put("isl", "ice");
        temp.put("mkd", "mac");
        temp.put("mri", "mao");
        temp.put("msa", "may");
        temp.put("fas", "per");
        temp.put("ron", "rum");
        temp.put("slk", "slo");
        temp.put("bod", "tib");
        temp.put("cym", "wel");
        return java.util.Collections.unmodifiableMap(temp);
    }

    static java.lang.String localeToMenuLanguage(java.util.Locale locale) {
        if (locale.equals(java.util.Locale.TAIWAN) || locale.equals(HONG_KONG) || locale.equals(MACAU)) {
            return "chi";
        }
        java.lang.String language = locale.getISO3Language();
        if (sTerminologyToBibliographicMap.containsKey(language)) {
            return sTerminologyToBibliographicMap.get(language);
        }
        return language;
    }

    java.util.concurrent.Executor getServiceThreadExecutor() {
        return this.mServiceThreadExecutor;
    }

    private class HdmiControlBroadcastReceiver extends android.content.BroadcastReceiver {
        private HdmiControlBroadcastReceiver() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:17:0x0048  */
        @Override // android.content.BroadcastReceiver
        @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onReceive(android.content.Context r6, android.content.Intent r7) {
            /*
                r5 = this;
                com.android.server.hdmi.HdmiControlService r0 = com.android.server.hdmi.HdmiControlService.this
                com.android.server.hdmi.HdmiControlService.m4137$$Nest$massertRunOnServiceThread(r0)
                java.lang.String r0 = "sys.shutdown.requested"
                java.lang.String r0 = android.os.SystemProperties.get(r0)
                java.lang.String r1 = "1"
                boolean r0 = r0.contains(r1)
                java.lang.String r1 = r7.getAction()
                int r2 = r1.hashCode()
                r3 = 1
                r4 = 0
                switch(r2) {
                    case -2128145023: goto L3e;
                    case -1454123155: goto L34;
                    case 158859398: goto L2a;
                    case 1947666138: goto L20;
                    default: goto L1f;
                }
            L1f:
                goto L48
            L20:
                java.lang.String r2 = "android.intent.action.ACTION_SHUTDOWN"
                boolean r1 = r1.equals(r2)
                if (r1 == 0) goto L1f
                r1 = 3
                goto L49
            L2a:
                java.lang.String r2 = "android.intent.action.CONFIGURATION_CHANGED"
                boolean r1 = r1.equals(r2)
                if (r1 == 0) goto L1f
                r1 = 2
                goto L49
            L34:
                java.lang.String r2 = "android.intent.action.SCREEN_ON"
                boolean r1 = r1.equals(r2)
                if (r1 == 0) goto L1f
                r1 = r3
                goto L49
            L3e:
                java.lang.String r2 = "android.intent.action.SCREEN_OFF"
                boolean r1 = r1.equals(r2)
                if (r1 == 0) goto L1f
                r1 = r4
                goto L49
            L48:
                r1 = -1
            L49:
                switch(r1) {
                    case 0: goto L85;
                    case 1: goto L77;
                    case 2: goto L5d;
                    case 3: goto L4d;
                    default: goto L4c;
                }
            L4c:
                goto L94
            L4d:
                com.android.server.hdmi.HdmiControlService r1 = com.android.server.hdmi.HdmiControlService.this
                boolean r1 = r1.isPowerOnOrTransient()
                if (r1 == 0) goto L94
                if (r0 != 0) goto L94
                com.android.server.hdmi.HdmiControlService r1 = com.android.server.hdmi.HdmiControlService.this
                r1.onStandby(r3)
                goto L94
            L5d:
                java.util.Locale r1 = java.util.Locale.getDefault()
                java.lang.String r1 = com.android.server.hdmi.HdmiControlService.localeToMenuLanguage(r1)
                com.android.server.hdmi.HdmiControlService r2 = com.android.server.hdmi.HdmiControlService.this
                java.lang.String r2 = com.android.server.hdmi.HdmiControlService.m4110$$Nest$fgetmMenuLanguage(r2)
                boolean r2 = r2.equals(r1)
                if (r2 != 0) goto L94
                com.android.server.hdmi.HdmiControlService r2 = com.android.server.hdmi.HdmiControlService.this
                com.android.server.hdmi.HdmiControlService.m4153$$Nest$monLanguageChanged(r2, r1)
                goto L94
            L77:
                com.android.server.hdmi.HdmiControlService r1 = com.android.server.hdmi.HdmiControlService.this
                boolean r1 = r1.isPowerStandbyOrTransient()
                if (r1 == 0) goto L94
                com.android.server.hdmi.HdmiControlService r1 = com.android.server.hdmi.HdmiControlService.this
                r1.onWakeUp(r4)
                goto L94
            L85:
                com.android.server.hdmi.HdmiControlService r1 = com.android.server.hdmi.HdmiControlService.this
                boolean r1 = r1.isPowerOnOrTransient()
                if (r1 == 0) goto L94
                if (r0 != 0) goto L94
                com.android.server.hdmi.HdmiControlService r1 = com.android.server.hdmi.HdmiControlService.this
                r1.onStandby(r4)
            L94:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.hdmi.HdmiControlService.HdmiControlBroadcastReceiver.onReceive(android.content.Context, android.content.Intent):void");
        }
    }

    HdmiControlService(android.content.Context context, java.util.List<java.lang.Integer> deviceTypes, com.android.server.hdmi.AudioManagerWrapper audioManager, com.android.server.hdmi.AudioDeviceVolumeManagerWrapper audioDeviceVolumeManager) {
        super(context);
        this.mServiceThreadExecutor = new java.util.concurrent.Executor() { // from class: com.android.server.hdmi.HdmiControlService.1
            @Override // java.util.concurrent.Executor
            public void execute(java.lang.Runnable r) {
                com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(r);
            }
        };
        this.mActiveSource = new com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource();
        this.mSystemAudioActivated = false;
        this.mAudioDeviceVolumeBehaviors = new java.util.HashMap();
        this.mIoThread = new android.os.HandlerThread("Hdmi Control Io Thread");
        this.mLock = new java.lang.Object();
        this.mHdmiControlStatusChangeListenerRecords = new java.util.ArrayList<>();
        this.mHdmiCecVolumeControlFeatureListenerRecords = new android.os.RemoteCallbackList<>();
        this.mHotplugEventListenerRecords = new java.util.ArrayList<>();
        this.mDeviceEventListenerRecords = new java.util.ArrayList<>();
        this.mVendorCommandListenerRecords = new java.util.ArrayList<>();
        this.mHdmiCecSettingChangeListenerRecords = new android.util.ArrayMap<>();
        this.mEarcPortId = -1;
        this.mSystemAudioModeChangeListenerRecords = new java.util.ArrayList<>();
        this.mHandler = new android.os.Handler();
        this.mHdmiControlBroadcastReceiver = new com.android.server.hdmi.HdmiControlService.HdmiControlBroadcastReceiver();
        this.mDisplayStatusCallback = null;
        this.mOtpCallbackPendingAddressAllocation = null;
        this.mMenuLanguage = localeToMenuLanguage(java.util.Locale.getDefault());
        this.mStandbyMessageReceived = false;
        this.mWakeUpMessageReceived = false;
        this.mSoundbarModeFeatureFlagEnabled = false;
        this.mEarcTxFeatureFlagEnabled = false;
        this.mNumericSoundbarVolumeUiOnTvFeatureFlagEnabled = false;
        this.mTransitionFromArcToEarcTxEnabled = false;
        this.mActivePortId = -1;
        this.mMhlVendorCommandListenerRecords = new java.util.ArrayList<>();
        this.mLastInputMhl = -1;
        this.mAddressAllocated = false;
        this.mIsCecAvailable = false;
        this.mAtomWriter = new com.android.server.hdmi.HdmiCecAtomWriter();
        this.mSelectRequestBuffer = new com.android.server.hdmi.SelectRequestBuffer();
        this.mSettingChangeListener = new com.android.server.hdmi.HdmiControlService.AnonymousClass33();
        this.mCecLocalDevices = deviceTypes;
        this.mSettingsObserver = new com.android.server.hdmi.HdmiControlService.SettingsObserver(this.mHandler);
        this.mHdmiCecConfig = new com.android.server.hdmi.HdmiCecConfig(context);
        this.mDeviceConfig = new com.android.server.hdmi.DeviceConfigWrapper();
        this.mAudioManager = audioManager;
        this.mAudioDeviceVolumeManager = audioDeviceVolumeManager;
    }

    public HdmiControlService(android.content.Context context) {
        super(context);
        this.mServiceThreadExecutor = new java.util.concurrent.Executor() { // from class: com.android.server.hdmi.HdmiControlService.1
            @Override // java.util.concurrent.Executor
            public void execute(java.lang.Runnable r) {
                com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(r);
            }
        };
        this.mActiveSource = new com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource();
        this.mSystemAudioActivated = false;
        this.mAudioDeviceVolumeBehaviors = new java.util.HashMap();
        this.mIoThread = new android.os.HandlerThread("Hdmi Control Io Thread");
        this.mLock = new java.lang.Object();
        this.mHdmiControlStatusChangeListenerRecords = new java.util.ArrayList<>();
        this.mHdmiCecVolumeControlFeatureListenerRecords = new android.os.RemoteCallbackList<>();
        this.mHotplugEventListenerRecords = new java.util.ArrayList<>();
        this.mDeviceEventListenerRecords = new java.util.ArrayList<>();
        this.mVendorCommandListenerRecords = new java.util.ArrayList<>();
        this.mHdmiCecSettingChangeListenerRecords = new android.util.ArrayMap<>();
        this.mEarcPortId = -1;
        this.mSystemAudioModeChangeListenerRecords = new java.util.ArrayList<>();
        this.mHandler = new android.os.Handler();
        this.mHdmiControlBroadcastReceiver = new com.android.server.hdmi.HdmiControlService.HdmiControlBroadcastReceiver();
        this.mDisplayStatusCallback = null;
        this.mOtpCallbackPendingAddressAllocation = null;
        this.mMenuLanguage = localeToMenuLanguage(java.util.Locale.getDefault());
        this.mStandbyMessageReceived = false;
        this.mWakeUpMessageReceived = false;
        this.mSoundbarModeFeatureFlagEnabled = false;
        this.mEarcTxFeatureFlagEnabled = false;
        this.mNumericSoundbarVolumeUiOnTvFeatureFlagEnabled = false;
        this.mTransitionFromArcToEarcTxEnabled = false;
        this.mActivePortId = -1;
        this.mMhlVendorCommandListenerRecords = new java.util.ArrayList<>();
        this.mLastInputMhl = -1;
        this.mAddressAllocated = false;
        this.mIsCecAvailable = false;
        this.mAtomWriter = new com.android.server.hdmi.HdmiCecAtomWriter();
        this.mSelectRequestBuffer = new com.android.server.hdmi.SelectRequestBuffer();
        this.mSettingChangeListener = new com.android.server.hdmi.HdmiControlService.AnonymousClass33();
        this.mCecLocalDevices = readDeviceTypes();
        this.mSettingsObserver = new com.android.server.hdmi.HdmiControlService.SettingsObserver(this.mHandler);
        this.mHdmiCecConfig = new com.android.server.hdmi.HdmiCecConfig(context);
        this.mDeviceConfig = new com.android.server.hdmi.DeviceConfigWrapper();
    }

    protected java.util.List<android.sysprop.HdmiProperties.cec_device_types_values> getCecDeviceTypes() {
        return android.sysprop.HdmiProperties.cec_device_types();
    }

    protected java.util.List<java.lang.Integer> getDeviceTypes() {
        return android.sysprop.HdmiProperties.device_type();
    }

    protected java.util.List<java.lang.Integer> readDeviceTypes() {
        java.util.List<android.sysprop.HdmiProperties.cec_device_types_values> cecDeviceTypes = getCecDeviceTypes();
        if (!cecDeviceTypes.isEmpty()) {
            if (cecDeviceTypes.contains(null)) {
                android.util.Slog.w(TAG, "Error parsing ro.hdmi.cec_device_types: " + android.os.SystemProperties.get("ro.hdmi.cec_device_types"));
            }
            return (java.util.List) cecDeviceTypes.stream().map(new java.util.function.Function() { // from class: com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda3
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.hdmi.HdmiControlService.enumToIntDeviceType((android.sysprop.HdmiProperties.cec_device_types_values) obj);
                }
            }).filter(new java.util.function.Predicate() { // from class: com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return java.util.Objects.nonNull((java.lang.Integer) obj);
                }
            }).collect(java.util.stream.Collectors.toList());
        }
        java.util.List<java.lang.Integer> deviceTypes = getDeviceTypes();
        if (deviceTypes.contains(null)) {
            android.util.Slog.w(TAG, "Error parsing ro.hdmi.device_type: " + android.os.SystemProperties.get("ro.hdmi.device_type"));
        }
        return (java.util.List) deviceTypes.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return java.util.Objects.nonNull((java.lang.Integer) obj);
            }
        }).collect(java.util.stream.Collectors.toList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.Integer enumToIntDeviceType(android.sysprop.HdmiProperties.cec_device_types_values cecDeviceType) {
        if (cecDeviceType == null) {
            return null;
        }
        switch (com.android.server.hdmi.HdmiControlService.AnonymousClass37.$SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values[cecDeviceType.ordinal()]) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            default:
                android.util.Slog.w(TAG, "Unrecognized device type in ro.hdmi.cec_device_types: " + cecDeviceType.getPropValue());
                break;
        }
        return null;
    }

    /* JADX INFO: renamed from: com.android.server.hdmi.HdmiControlService$37, reason: invalid class name */
    static /* synthetic */ class AnonymousClass37 {
        static final /* synthetic */ int[] $SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values = new int[android.sysprop.HdmiProperties.cec_device_types_values.values().length];

        static {
            try {
                $SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values[android.sysprop.HdmiProperties.cec_device_types_values.TV.ordinal()] = 1;
            } catch (java.lang.NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values[android.sysprop.HdmiProperties.cec_device_types_values.RECORDING_DEVICE.ordinal()] = 2;
            } catch (java.lang.NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values[android.sysprop.HdmiProperties.cec_device_types_values.RESERVED.ordinal()] = 3;
            } catch (java.lang.NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values[android.sysprop.HdmiProperties.cec_device_types_values.TUNER.ordinal()] = 4;
            } catch (java.lang.NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values[android.sysprop.HdmiProperties.cec_device_types_values.PLAYBACK_DEVICE.ordinal()] = 5;
            } catch (java.lang.NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values[android.sysprop.HdmiProperties.cec_device_types_values.AUDIO_SYSTEM.ordinal()] = 6;
            } catch (java.lang.NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values[android.sysprop.HdmiProperties.cec_device_types_values.PURE_CEC_SWITCH.ordinal()] = 7;
            } catch (java.lang.NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values[android.sysprop.HdmiProperties.cec_device_types_values.VIDEO_PROCESSOR.ordinal()] = 8;
            } catch (java.lang.NoSuchFieldError e8) {
            }
        }
    }

    protected static java.util.List<java.lang.Integer> getIntList(java.lang.String string) {
        java.util.ArrayList<java.lang.Integer> list = new java.util.ArrayList<>();
        android.text.TextUtils.SimpleStringSplitter splitter = new android.text.TextUtils.SimpleStringSplitter(',');
        splitter.setString(string);
        for (java.lang.String item : splitter) {
            try {
                list.add(java.lang.Integer.valueOf(java.lang.Integer.parseInt(item)));
            } catch (java.lang.NumberFormatException e) {
                android.util.Slog.w(TAG, "Can't parseInt: " + item);
            }
        }
        return java.util.Collections.unmodifiableList(list);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        initService();
        publishBinderService("hdmi_control", new com.android.server.hdmi.HdmiControlService.BinderService());
        if (this.mCecController != null) {
            android.content.IntentFilter filter = new android.content.IntentFilter();
            filter.addAction("android.intent.action.SCREEN_OFF");
            filter.addAction("android.intent.action.SCREEN_ON");
            filter.addAction("android.intent.action.ACTION_SHUTDOWN");
            filter.addAction("android.intent.action.CONFIGURATION_CHANGED");
            getContext().registerReceiver(this.mHdmiControlBroadcastReceiver, filter);
            registerContentObserver();
        }
        this.mMhlController.setOption(104, 1);
    }

    void initService() {
        if (this.mIoLooper == null) {
            this.mIoThread.start();
            this.mIoLooper = this.mIoThread.getLooper();
        }
        if (this.mPowerStatusController == null) {
            this.mPowerStatusController = new com.android.server.hdmi.HdmiCecPowerStatusController(this);
        }
        this.mPowerStatusController.setPowerStatus(getInitialPowerStatus());
        setProhibitMode(false);
        this.mHdmiControlEnabled = this.mHdmiCecConfig.getIntValue("hdmi_cec_enabled");
        this.mSoundbarModeFeatureFlagEnabled = this.mDeviceConfig.getBoolean("enable_soundbar_mode", true);
        this.mEarcTxFeatureFlagEnabled = this.mDeviceConfig.getBoolean("enable_earc_tx", true);
        this.mTransitionFromArcToEarcTxEnabled = this.mDeviceConfig.getBoolean("transition_arc_to_earc_tx", true);
        this.mNumericSoundbarVolumeUiOnTvFeatureFlagEnabled = this.mDeviceConfig.getBoolean("enable_numeric_soundbar_volume_ui_on_tv", true);
        synchronized (this.mLock) {
            this.mEarcEnabled = this.mHdmiCecConfig.getIntValue("earc_enabled") == 1;
            if (isTvDevice()) {
                this.mEarcEnabled &= this.mEarcTxFeatureFlagEnabled;
            }
        }
        setHdmiCecVolumeControlEnabledInternal(getHdmiCecConfig().getIntValue("volume_control_enabled"));
        this.mMhlInputChangeEnabled = readBooleanSetting("mhl_input_switching_enabled", true);
        if (this.mCecMessageBuffer == null) {
            this.mCecMessageBuffer = new com.android.server.hdmi.CecMessageBuffer(this);
        }
        if (this.mCecController == null) {
            this.mCecController = com.android.server.hdmi.HdmiCecController.create(this, getAtomWriter());
        }
        if (this.mCecController == null) {
            android.util.Slog.i(TAG, "Device does not support HDMI-CEC.");
            return;
        }
        if (this.mMhlController == null) {
            this.mMhlController = com.android.server.hdmi.HdmiMhlControllerStub.create(this);
        }
        if (!this.mMhlController.isReady()) {
            android.util.Slog.i(TAG, "Device does not support MHL-control.");
        }
        if (this.mEarcController == null) {
            this.mEarcController = com.android.server.hdmi.HdmiEarcController.create(this);
        }
        if (this.mEarcController == null) {
            android.util.Slog.i(TAG, "Device does not support eARC.");
        }
        this.mHdmiCecNetwork = new com.android.server.hdmi.HdmiCecNetwork(this, this.mCecController, this.mMhlController);
        if (!isCecControlEnabled()) {
            this.mCecController.enableCec(false);
        } else {
            initializeCec(1);
        }
        synchronized (this.mLock) {
            this.mMhlDevices = java.util.Collections.emptyList();
        }
        this.mHdmiCecNetwork.initPortInfo();
        java.util.List<android.hardware.hdmi.HdmiPortInfo> ports = getPortInfo();
        synchronized (this.mLock) {
            this.mEarcSupported = false;
            java.util.Iterator<android.hardware.hdmi.HdmiPortInfo> it = ports.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                android.hardware.hdmi.HdmiPortInfo port = it.next();
                boolean earcSupportedOnPort = port.isEarcSupported();
                if (earcSupportedOnPort && this.mEarcSupported) {
                    android.util.Slog.e(TAG, "HDMI eARC supported on more than 1 port.");
                    this.mEarcSupported = false;
                    this.mEarcPortId = -1;
                    break;
                } else if (earcSupportedOnPort) {
                    this.mEarcPortId = port.getId();
                    this.mEarcSupported = earcSupportedOnPort;
                }
            }
            this.mEarcSupported &= this.mEarcController != null;
        }
        if (isEarcSupported()) {
            if (isEarcEnabled()) {
                initializeEarc(1);
            } else {
                setEarcEnabledInHal(false, false);
            }
        }
        this.mHdmiCecConfig.registerChangeListener("hdmi_cec_enabled", new com.android.server.hdmi.HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.2
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(java.lang.String setting) {
                int enabled = com.android.server.hdmi.HdmiControlService.this.mHdmiCecConfig.getIntValue("hdmi_cec_enabled");
                com.android.server.hdmi.HdmiControlService.this.setCecEnabled(enabled);
            }
        }, this.mServiceThreadExecutor);
        this.mHdmiCecConfig.registerChangeListener("hdmi_cec_version", new com.android.server.hdmi.HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.3
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(java.lang.String setting) {
                com.android.server.hdmi.HdmiControlService.this.initializeCec(0);
            }
        }, this.mServiceThreadExecutor);
        this.mHdmiCecConfig.registerChangeListener("routing_control", new com.android.server.hdmi.HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.4
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(java.lang.String setting) {
                boolean enabled = com.android.server.hdmi.HdmiControlService.this.mHdmiCecConfig.getIntValue("routing_control") == 1;
                if (com.android.server.hdmi.HdmiControlService.this.isAudioSystemDevice()) {
                    if (com.android.server.hdmi.HdmiControlService.this.audioSystem() == null) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "Switch device has not registered yet. Can't turn routing on.");
                    } else {
                        com.android.server.hdmi.HdmiControlService.this.audioSystem().setRoutingControlFeatureEnabled(enabled);
                    }
                }
            }
        }, this.mServiceThreadExecutor);
        this.mHdmiCecConfig.registerChangeListener("system_audio_control", new com.android.server.hdmi.HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.5
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(java.lang.String setting) {
                boolean enabled = com.android.server.hdmi.HdmiControlService.this.mHdmiCecConfig.getIntValue("system_audio_control") == 1;
                if (com.android.server.hdmi.HdmiControlService.this.isTvDeviceEnabled()) {
                    com.android.server.hdmi.HdmiControlService.this.tv().setSystemAudioControlFeatureEnabled(enabled);
                }
                if (com.android.server.hdmi.HdmiControlService.this.isAudioSystemDevice()) {
                    if (com.android.server.hdmi.HdmiControlService.this.audioSystem() == null) {
                        android.util.Slog.e(com.android.server.hdmi.HdmiControlService.TAG, "Audio System device has not registered yet. Can't turn system audio mode on.");
                    } else {
                        com.android.server.hdmi.HdmiControlService.this.audioSystem().onSystemAudioControlFeatureSupportChanged(enabled);
                    }
                }
            }
        }, this.mServiceThreadExecutor);
        this.mHdmiCecConfig.registerChangeListener("volume_control_enabled", new com.android.server.hdmi.HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.6
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(java.lang.String setting) {
                com.android.server.hdmi.HdmiControlService.this.setHdmiCecVolumeControlEnabledInternal(com.android.server.hdmi.HdmiControlService.this.getHdmiCecConfig().getIntValue("volume_control_enabled"));
            }
        }, this.mServiceThreadExecutor);
        this.mHdmiCecConfig.registerChangeListener("tv_wake_on_one_touch_play", new com.android.server.hdmi.HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.7
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(java.lang.String setting) {
                if (com.android.server.hdmi.HdmiControlService.this.isTvDeviceEnabled()) {
                    com.android.server.hdmi.HdmiControlService.this.mCecController.enableWakeupByOtp(com.android.server.hdmi.HdmiControlService.this.tv().getAutoWakeup());
                }
            }
        }, this.mServiceThreadExecutor);
        this.mHdmiCecConfig.registerChangeListener("rc_profile_tv", new com.android.server.hdmi.HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.8
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(java.lang.String setting) {
                com.android.server.hdmi.HdmiControlService.this.reportFeatures(true);
            }
        }, this.mServiceThreadExecutor);
        this.mHdmiCecConfig.registerChangeListener("rc_profile_source_handles_root_menu", new com.android.server.hdmi.HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.9
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(java.lang.String setting) {
                com.android.server.hdmi.HdmiControlService.this.reportFeatures(false);
            }
        }, this.mServiceThreadExecutor);
        this.mHdmiCecConfig.registerChangeListener("rc_profile_source_handles_setup_menu", new com.android.server.hdmi.HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.10
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(java.lang.String setting) {
                com.android.server.hdmi.HdmiControlService.this.reportFeatures(false);
            }
        }, this.mServiceThreadExecutor);
        this.mHdmiCecConfig.registerChangeListener("rc_profile_source_handles_contents_menu", new com.android.server.hdmi.HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.11
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(java.lang.String setting) {
                com.android.server.hdmi.HdmiControlService.this.reportFeatures(false);
            }
        }, this.mServiceThreadExecutor);
        this.mHdmiCecConfig.registerChangeListener("rc_profile_source_handles_top_menu", new com.android.server.hdmi.HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.12
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(java.lang.String setting) {
                com.android.server.hdmi.HdmiControlService.this.reportFeatures(false);
            }
        }, this.mServiceThreadExecutor);
        this.mHdmiCecConfig.registerChangeListener("rc_profile_source_handles_media_context_sensitive_menu", new com.android.server.hdmi.HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.13
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(java.lang.String setting) {
                com.android.server.hdmi.HdmiControlService.this.reportFeatures(false);
            }
        }, this.mServiceThreadExecutor);
        if (isTvDevice()) {
            this.mDeviceConfig.addOnPropertiesChangedListener(getContext().getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.hdmi.HdmiControlService.14
                public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                    com.android.server.hdmi.HdmiControlService.this.mEarcTxFeatureFlagEnabled = properties.getBoolean("enable_earc_tx", true);
                    boolean earcEnabledSetting = com.android.server.hdmi.HdmiControlService.this.mHdmiCecConfig.getIntValue("earc_enabled") == 1;
                    com.android.server.hdmi.HdmiControlService.this.setEarcEnabled((earcEnabledSetting && com.android.server.hdmi.HdmiControlService.this.mEarcTxFeatureFlagEnabled) ? 1 : 0);
                }
            });
        }
        this.mHdmiCecConfig.registerChangeListener("earc_enabled", new com.android.server.hdmi.HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.15
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(java.lang.String setting) {
                if (com.android.server.hdmi.HdmiControlService.this.isTvDevice()) {
                    int i = 0;
                    boolean earcEnabledSetting = com.android.server.hdmi.HdmiControlService.this.mHdmiCecConfig.getIntValue("earc_enabled") == 1;
                    com.android.server.hdmi.HdmiControlService hdmiControlService = com.android.server.hdmi.HdmiControlService.this;
                    if (earcEnabledSetting && com.android.server.hdmi.HdmiControlService.this.mEarcTxFeatureFlagEnabled) {
                        i = 1;
                    }
                    hdmiControlService.setEarcEnabled(i);
                    return;
                }
                com.android.server.hdmi.HdmiControlService.this.setEarcEnabled(com.android.server.hdmi.HdmiControlService.this.mHdmiCecConfig.getIntValue("earc_enabled"));
            }
        }, this.mServiceThreadExecutor);
        this.mDeviceConfig.addOnPropertiesChangedListener(getContext().getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.hdmi.HdmiControlService.16
            public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                com.android.server.hdmi.HdmiControlService.this.mSoundbarModeFeatureFlagEnabled = properties.getBoolean("enable_soundbar_mode", true);
                boolean soundbarModeSetting = com.android.server.hdmi.HdmiControlService.this.mHdmiCecConfig.getIntValue("soundbar_mode") == 1;
                com.android.server.hdmi.HdmiControlService.this.setSoundbarMode((soundbarModeSetting && com.android.server.hdmi.HdmiControlService.this.mSoundbarModeFeatureFlagEnabled) ? 1 : 0);
            }
        });
        this.mHdmiCecConfig.registerChangeListener("soundbar_mode", new com.android.server.hdmi.HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.17
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(java.lang.String setting) {
                int i = 0;
                boolean soundbarModeSetting = com.android.server.hdmi.HdmiControlService.this.mHdmiCecConfig.getIntValue("soundbar_mode") == 1;
                com.android.server.hdmi.HdmiControlService hdmiControlService = com.android.server.hdmi.HdmiControlService.this;
                if (soundbarModeSetting && com.android.server.hdmi.HdmiControlService.this.mSoundbarModeFeatureFlagEnabled) {
                    i = 1;
                }
                hdmiControlService.setSoundbarMode(i);
            }
        }, this.mServiceThreadExecutor);
        this.mDeviceConfig.addOnPropertiesChangedListener(getContext().getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.hdmi.HdmiControlService.18
            public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                com.android.server.hdmi.HdmiControlService.this.mTransitionFromArcToEarcTxEnabled = properties.getBoolean("transition_arc_to_earc_tx", true);
            }
        });
        this.mDeviceConfig.addOnPropertiesChangedListener(getContext().getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.hdmi.HdmiControlService.19
            public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                com.android.server.hdmi.HdmiControlService.this.mNumericSoundbarVolumeUiOnTvFeatureFlagEnabled = properties.getBoolean("enable_numeric_soundbar_volume_ui_on_tv", true);
                com.android.server.hdmi.HdmiControlService.this.checkAndUpdateAbsoluteVolumeBehavior();
            }
        });
    }

    boolean isScreenOff() {
        return this.mDisplayManager.getDisplay(0).getState() == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bootCompleted() {
        if (this.mPowerManager.isInteractive() && isPowerStandbyOrTransient()) {
            this.mPowerStatusController.setPowerStatus(0);
            if (this.mAddressAllocated) {
                for (com.android.server.hdmi.HdmiCecLocalDevice localDevice : getAllCecLocalDevices()) {
                    localDevice.startQueuedActions();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportFeatures(boolean isTvDeviceSetting) {
        if (getCecVersion() < 6) {
            return;
        }
        if (isTvDeviceSetting) {
            if (isTvDeviceEnabled()) {
                tv().reportFeatures();
            }
        } else {
            com.android.server.hdmi.HdmiCecLocalDeviceSource source = isAudioSystemDevice() ? audioSystem() : playback();
            if (source != null) {
                source.reportFeatures();
            }
        }
    }

    int getInitialPowerStatus() {
        return 3;
    }

    void setCecController(com.android.server.hdmi.HdmiCecController cecController) {
        this.mCecController = cecController;
    }

    void setEarcController(com.android.server.hdmi.HdmiEarcController earcController) {
        this.mEarcController = earcController;
    }

    void setHdmiCecNetwork(com.android.server.hdmi.HdmiCecNetwork hdmiCecNetwork) {
        this.mHdmiCecNetwork = hdmiCecNetwork;
    }

    void setHdmiCecConfig(com.android.server.hdmi.HdmiCecConfig hdmiCecConfig) {
        this.mHdmiCecConfig = hdmiCecConfig;
    }

    public com.android.server.hdmi.HdmiCecNetwork getHdmiCecNetwork() {
        return this.mHdmiCecNetwork;
    }

    void setHdmiMhlController(com.android.server.hdmi.HdmiMhlControllerStub hdmiMhlController) {
        this.mMhlController = hdmiMhlController;
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            this.mDisplayManager = (android.hardware.display.DisplayManager) getContext().getSystemService(android.hardware.display.DisplayManager.class);
            this.mTvInputManager = (android.media.tv.TvInputManager) getContext().getSystemService("tv_input");
            this.mPowerManager = new com.android.server.hdmi.PowerManagerWrapper(getContext());
            this.mPowerManagerInternal = new com.android.server.hdmi.PowerManagerInternalWrapper();
            if (this.mAudioManager == null) {
                this.mAudioManager = new com.android.server.hdmi.DefaultAudioManagerWrapper(getContext());
            }
            this.mStreamMusicMaxVolume = getAudioManager().getStreamMaxVolume(3);
            if (this.mAudioDeviceVolumeManager == null) {
                this.mAudioDeviceVolumeManager = new com.android.server.hdmi.DefaultAudioDeviceVolumeManagerWrapper(getContext());
            }
            getAudioDeviceVolumeManager().addOnDeviceVolumeBehaviorChangedListener(this.mServiceThreadExecutor, new android.media.AudioDeviceVolumeManager.OnDeviceVolumeBehaviorChangedListener() { // from class: com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda0
                public final void onDeviceVolumeBehaviorChanged(android.media.AudioDeviceAttributes audioDeviceAttributes, int i) {
                    this.f$0.onDeviceVolumeBehaviorChanged(audioDeviceAttributes, i);
                }
            });
            return;
        }
        if (phase == 1000) {
            runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.bootCompleted();
                }
            });
        }
    }

    android.media.tv.TvInputManager getTvInputManager() {
        return this.mTvInputManager;
    }

    void registerTvInputCallback(android.media.tv.TvInputManager.TvInputCallback callback) {
        if (this.mTvInputManager == null) {
            return;
        }
        this.mTvInputManager.registerCallback(callback, this.mHandler);
    }

    void unregisterTvInputCallback(android.media.tv.TvInputManager.TvInputCallback callback) {
        if (this.mTvInputManager == null) {
            return;
        }
        this.mTvInputManager.unregisterCallback(callback);
    }

    void setDeviceConfig(com.android.server.hdmi.DeviceConfigWrapper deviceConfig) {
        this.mDeviceConfig = deviceConfig;
    }

    void setPowerManager(com.android.server.hdmi.PowerManagerWrapper powerManager) {
        this.mPowerManager = powerManager;
    }

    void setPowerManagerInternal(com.android.server.hdmi.PowerManagerInternalWrapper powerManagerInternal) {
        this.mPowerManagerInternal = powerManagerInternal;
    }

    com.android.server.hdmi.DeviceConfigWrapper getDeviceConfig() {
        return this.mDeviceConfig;
    }

    com.android.server.hdmi.PowerManagerWrapper getPowerManager() {
        return this.mPowerManager;
    }

    com.android.server.hdmi.PowerManagerInternalWrapper getPowerManagerInternal() {
        return this.mPowerManagerInternal;
    }

    public void setSoundbarMode(int settingValue) {
        boolean isArcSupported = isArcSupported();
        com.android.server.hdmi.HdmiCecLocalDevicePlayback playback = playback();
        com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem audioSystem = audioSystem();
        getAtomWriter().dsmStatusChanged(isArcSupported, settingValue == 1, 2);
        if (playback == null) {
            android.util.Slog.w(TAG, "Device type not compatible to change soundbar mode.");
            return;
        }
        if (!isArcSupported) {
            android.util.Slog.w(TAG, "Device type doesn't support ARC.");
            return;
        }
        boolean isArcEnabled = false;
        if (settingValue == 0 && audioSystem != null) {
            isArcEnabled = audioSystem.isArcEnabled();
            if (isSystemAudioActivated()) {
                audioSystem.terminateSystemAudioMode();
            }
            if (isArcEnabled) {
                if (audioSystem.hasAction(com.android.server.hdmi.ArcTerminationActionFromAvr.class)) {
                    audioSystem.removeAction(com.android.server.hdmi.ArcTerminationActionFromAvr.class);
                }
                audioSystem.addAndStartAction(new com.android.server.hdmi.ArcTerminationActionFromAvr(audioSystem, new android.hardware.hdmi.IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiControlService.20
                    public void onComplete(int result) {
                        com.android.server.hdmi.HdmiControlService.this.mAddressAllocated = false;
                        com.android.server.hdmi.HdmiControlService.this.initializeCecLocalDevices(5);
                    }
                }));
            }
        }
        if (!isArcEnabled) {
            this.mAddressAllocated = false;
            initializeCecLocalDevices(5);
        }
    }

    public boolean isDeviceDiscoveryHandledByPlayback() {
        com.android.server.hdmi.HdmiCecLocalDevicePlayback playback = playback();
        if (playback != null) {
            if (playback.hasAction(com.android.server.hdmi.DeviceDiscoveryAction.class) || playback.hasAction(com.android.server.hdmi.HotplugDetectionAction.class)) {
                return true;
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInitializeCecComplete(int initiatedBy) {
        updatePowerStatusOnInitializeCecComplete();
        this.mWakeUpMessageReceived = false;
        if (isTvDeviceEnabled()) {
            this.mCecController.enableWakeupByOtp(tv().getAutoWakeup());
        }
        int reason = -1;
        switch (initiatedBy) {
            case 0:
                reason = 1;
                break;
            case 1:
                reason = 0;
                break;
            case 2:
                reason = 2;
                java.util.List<com.android.server.hdmi.HdmiCecLocalDevice> devices = getAllCecLocalDevices();
                for (com.android.server.hdmi.HdmiCecLocalDevice device : devices) {
                    device.onInitializeCecComplete(initiatedBy);
                }
                break;
            case 3:
                reason = 2;
                break;
        }
        if (reason != -1) {
            invokeVendorCommandListenersOnControlStateChanged(true, reason);
            announceHdmiControlStatusChange(1);
        }
    }

    private void updatePowerStatusOnInitializeCecComplete() {
        if (this.mPowerStatusController.isPowerStatusTransientToOn()) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updatePowerStatusOnInitializeCecComplete$0();
                }
            });
        } else if (this.mPowerStatusController.isPowerStatusTransientToStandby()) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updatePowerStatusOnInitializeCecComplete$1();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePowerStatusOnInitializeCecComplete$0() {
        this.mPowerStatusController.setPowerStatus(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePowerStatusOnInitializeCecComplete$1() {
        this.mPowerStatusController.setPowerStatus(1);
    }

    private void registerContentObserver() {
        android.content.ContentResolver resolver = getContext().getContentResolver();
        java.lang.String[] settings = {"mhl_input_switching_enabled", "mhl_power_charge_enabled", "device_name"};
        for (java.lang.String s : settings) {
            resolver.registerContentObserver(android.provider.Settings.Global.getUriFor(s), false, this.mSettingsObserver, -1);
        }
    }

    private class SettingsObserver extends android.database.ContentObserver {
        public SettingsObserver(android.os.Handler handler) {
            super(handler);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:14:0x0032  */
        @Override // android.database.ContentObserver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onChange(boolean r6, android.net.Uri r7) {
            /*
                r5 = this;
                java.lang.String r0 = r7.getLastPathSegment()
                com.android.server.hdmi.HdmiControlService r1 = com.android.server.hdmi.HdmiControlService.this
                r2 = 1
                boolean r1 = r1.readBooleanSetting(r0, r2)
                int r3 = r0.hashCode()
                switch(r3) {
                    case -1543071020: goto L28;
                    case -1262529811: goto L1d;
                    case -885757826: goto L13;
                    default: goto L12;
                }
            L12:
                goto L32
            L13:
                java.lang.String r3 = "mhl_power_charge_enabled"
                boolean r3 = r0.equals(r3)
                if (r3 == 0) goto L12
                goto L33
            L1d:
                java.lang.String r2 = "mhl_input_switching_enabled"
                boolean r2 = r0.equals(r2)
                if (r2 == 0) goto L12
                r2 = 0
                goto L33
            L28:
                java.lang.String r2 = "device_name"
                boolean r2 = r0.equals(r2)
                if (r2 == 0) goto L12
                r2 = 2
                goto L33
            L32:
                r2 = -1
            L33:
                switch(r2) {
                    case 0: goto L55;
                    case 1: goto L45;
                    case 2: goto L37;
                    default: goto L36;
                }
            L36:
                goto L5b
            L37:
                com.android.server.hdmi.HdmiControlService r2 = com.android.server.hdmi.HdmiControlService.this
                java.lang.String r3 = android.os.Build.MODEL
                java.lang.String r2 = r2.readStringSetting(r0, r3)
                com.android.server.hdmi.HdmiControlService r3 = com.android.server.hdmi.HdmiControlService.this
                com.android.server.hdmi.HdmiControlService.m4159$$Nest$msetDisplayName(r3, r2)
                goto L5b
            L45:
                com.android.server.hdmi.HdmiControlService r2 = com.android.server.hdmi.HdmiControlService.this
                com.android.server.hdmi.HdmiMhlControllerStub r2 = com.android.server.hdmi.HdmiControlService.m4111$$Nest$fgetmMhlController(r2)
                r3 = 102(0x66, float:1.43E-43)
                int r4 = com.android.server.hdmi.HdmiControlService.m4162$$Nest$smtoInt(r1)
                r2.setOption(r3, r4)
                goto L5b
            L55:
                com.android.server.hdmi.HdmiControlService r2 = com.android.server.hdmi.HdmiControlService.this
                r2.setMhlInputChangeEnabled(r1)
            L5b:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.hdmi.HdmiControlService.SettingsObserver.onChange(boolean, android.net.Uri):void");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int toInt(boolean z) {
        return z ? 1 : 0;
    }

    boolean readBooleanSetting(java.lang.String key, boolean defVal) {
        android.content.ContentResolver cr = getContext().getContentResolver();
        return android.provider.Settings.Global.getInt(cr, key, toInt(defVal)) == 1;
    }

    int readIntSetting(java.lang.String key, int defVal) {
        android.content.ContentResolver cr = getContext().getContentResolver();
        return android.provider.Settings.Global.getInt(cr, key, defVal);
    }

    void writeBooleanSetting(java.lang.String key, boolean value) {
        android.content.ContentResolver cr = getContext().getContentResolver();
        android.provider.Settings.Global.putInt(cr, key, toInt(value));
    }

    protected void writeStringSystemProperty(java.lang.String key, java.lang.String value) {
        android.os.SystemProperties.set(key, value);
    }

    boolean readBooleanSystemProperty(java.lang.String key, boolean defVal) {
        return android.os.SystemProperties.getBoolean(key, defVal);
    }

    java.lang.String readStringSetting(java.lang.String key, java.lang.String defVal) {
        android.content.ContentResolver cr = getContext().getContentResolver();
        java.lang.String content = android.provider.Settings.Global.getString(cr, key);
        if (android.text.TextUtils.isEmpty(content)) {
            return defVal;
        }
        return content;
    }

    void writeStringSetting(java.lang.String key, java.lang.String value) {
        android.content.ContentResolver cr = getContext().getContentResolver();
        android.provider.Settings.Global.putString(cr, key, value);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initializeCec(int initiatedBy) {
        this.mAddressAllocated = false;
        int settingsCecVersion = getHdmiCecConfig().getIntValue("hdmi_cec_version");
        int supportedCecVersion = this.mCecController.getVersion();
        this.mCecVersion = java.lang.Math.max(5, java.lang.Math.min(settingsCecVersion, supportedCecVersion));
        this.mCecController.enableSystemCecControl(true);
        this.mCecController.setLanguage(this.mMenuLanguage);
        initializeCecLocalDevices(initiatedBy);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private java.util.List<java.lang.Integer> getCecLocalDeviceTypes() {
        java.util.ArrayList<java.lang.Integer> allLocalDeviceTypes = new java.util.ArrayList<>(this.mCecLocalDevices);
        if (isDsmEnabled() && !allLocalDeviceTypes.contains(5) && isArcSupported() && this.mSoundbarModeFeatureFlagEnabled) {
            allLocalDeviceTypes.add(5);
        }
        return allLocalDeviceTypes;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void initializeCecLocalDevices(int initiatedBy) {
        assertRunOnServiceThread();
        java.util.ArrayList<com.android.server.hdmi.HdmiCecLocalDevice> localDevices = new java.util.ArrayList<>();
        java.util.Iterator<java.lang.Integer> it = getCecLocalDeviceTypes().iterator();
        while (it.hasNext()) {
            int type = it.next().intValue();
            com.android.server.hdmi.HdmiCecLocalDevice localDevice = this.mHdmiCecNetwork.getLocalDevice(type);
            if (localDevice == null) {
                localDevice = com.android.server.hdmi.HdmiCecLocalDevice.create(this, type);
            }
            localDevice.init();
            localDevices.add(localDevice);
        }
        this.mHdmiCecNetwork.clearDeviceList();
        allocateLogicalAddress(localDevices, initiatedBy);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void allocateLogicalAddress(final java.util.ArrayList<com.android.server.hdmi.HdmiCecLocalDevice> allocatingDevices, final int initiatedBy) {
        assertRunOnServiceThread();
        this.mCecController.clearLogicalAddress();
        final java.util.ArrayList<com.android.server.hdmi.HdmiCecLocalDevice> allocatedDevices = new java.util.ArrayList<>();
        final int[] finished = new int[1];
        this.mAddressAllocated = allocatingDevices.isEmpty();
        this.mSelectRequestBuffer.clear();
        for (final com.android.server.hdmi.HdmiCecLocalDevice localDevice : allocatingDevices) {
            this.mCecController.allocateLogicalAddress(localDevice.getType(), localDevice.getPreferredAddress(), new com.android.server.hdmi.HdmiCecController.AllocateAddressCallback() { // from class: com.android.server.hdmi.HdmiControlService.21
                @Override // com.android.server.hdmi.HdmiCecController.AllocateAddressCallback
                public void onAllocated(int deviceType, int logicalAddress) {
                    if (logicalAddress == 15) {
                        android.util.Slog.e(com.android.server.hdmi.HdmiControlService.TAG, "Failed to allocate address:[device_type:" + deviceType + "]");
                        com.android.server.hdmi.HdmiControlService.this.mHdmiCecNetwork.removeLocalDeviceWithType(deviceType);
                    } else {
                        android.hardware.hdmi.HdmiDeviceInfo deviceInfo = com.android.server.hdmi.HdmiControlService.this.createDeviceInfo(logicalAddress, deviceType, 0, com.android.server.hdmi.HdmiControlService.this.getCecVersion());
                        localDevice.setDeviceInfo(deviceInfo);
                        com.android.server.hdmi.HdmiControlService.this.mHdmiCecNetwork.addLocalDevice(deviceType, localDevice);
                        com.android.server.hdmi.HdmiControlService.this.mHdmiCecNetwork.addCecDevice(localDevice.getDeviceInfo());
                        com.android.server.hdmi.HdmiControlService.this.mCecController.addLogicalAddress(logicalAddress);
                        allocatedDevices.add(localDevice);
                    }
                    int size = allocatingDevices.size();
                    int[] iArr = finished;
                    int i = iArr[0] + 1;
                    iArr[0] = i;
                    if (size == i) {
                        if (initiatedBy != 4 && initiatedBy != 5) {
                            com.android.server.hdmi.HdmiControlService.this.onInitializeCecComplete(initiatedBy);
                        } else if (initiatedBy == 4 && com.android.server.hdmi.HdmiControlService.this.mDisplayStatusCallback == null) {
                            synchronized (com.android.server.hdmi.HdmiControlService.this.mLock) {
                                com.android.server.hdmi.HdmiControlService.this.announceHdmiControlStatusChange(com.android.server.hdmi.HdmiControlService.this.mHdmiControlEnabled);
                            }
                        }
                        com.android.server.hdmi.HdmiControlService.this.mHdmiCecNetwork.removeUnusedLocalDevices(allocatedDevices);
                        com.android.server.hdmi.HdmiControlService.this.mAddressAllocated = true;
                        com.android.server.hdmi.HdmiControlService.this.notifyAddressAllocated(allocatedDevices, initiatedBy);
                        if (com.android.server.hdmi.HdmiControlService.this.mDisplayStatusCallback != null) {
                            com.android.server.hdmi.HdmiControlService.this.queryDisplayStatus(com.android.server.hdmi.HdmiControlService.this.mDisplayStatusCallback);
                            com.android.server.hdmi.HdmiControlService.this.mDisplayStatusCallback = null;
                        }
                        if (com.android.server.hdmi.HdmiControlService.this.mOtpCallbackPendingAddressAllocation != null) {
                            com.android.server.hdmi.HdmiControlService.this.oneTouchPlay(com.android.server.hdmi.HdmiControlService.this.mOtpCallbackPendingAddressAllocation);
                            com.android.server.hdmi.HdmiControlService.this.mOtpCallbackPendingAddressAllocation = null;
                        }
                        com.android.server.hdmi.HdmiControlService.this.mCecMessageBuffer.processMessages();
                    }
                }
            });
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void notifyAddressAllocated(java.util.ArrayList<com.android.server.hdmi.HdmiCecLocalDevice> devices, int initiatedBy) {
        assertRunOnServiceThread();
        if (devices == null || devices.isEmpty()) {
            android.util.Slog.w(TAG, "No local device to notify.");
            return;
        }
        java.util.List<com.android.server.hdmi.HdmiCecMessage> bufferedMessages = this.mCecMessageBuffer.getBuffer();
        for (com.android.server.hdmi.HdmiCecLocalDevice device : devices) {
            int address = device.getDeviceInfo().getLogicalAddress();
            device.handleAddressAllocated(address, bufferedMessages, initiatedBy);
        }
        if (isTvDeviceEnabled()) {
            tv().setSelectRequestBuffer(this.mSelectRequestBuffer);
        }
    }

    boolean isAddressAllocated() {
        return this.mAddressAllocated;
    }

    java.util.List<android.hardware.hdmi.HdmiPortInfo> getPortInfo() {
        java.util.List<android.hardware.hdmi.HdmiPortInfo> portInfo;
        synchronized (this.mLock) {
            portInfo = this.mHdmiCecNetwork.getPortInfo();
        }
        return portInfo;
    }

    android.hardware.hdmi.HdmiPortInfo getPortInfo(int portId) {
        return this.mHdmiCecNetwork.getPortInfo(portId);
    }

    int portIdToPath(int portId) {
        return this.mHdmiCecNetwork.portIdToPath(portId);
    }

    int pathToPortId(int path) {
        return this.mHdmiCecNetwork.physicalAddressToPortId(path);
    }

    boolean isValidPortId(int portId) {
        return this.mHdmiCecNetwork.getPortInfo(portId) != null;
    }

    protected android.os.Looper getIoLooper() {
        return this.mIoLooper;
    }

    void setIoLooper(android.os.Looper ioLooper) {
        this.mIoLooper = ioLooper;
    }

    void setCecMessageBuffer(com.android.server.hdmi.CecMessageBuffer cecMessageBuffer) {
        this.mCecMessageBuffer = cecMessageBuffer;
    }

    protected android.os.Looper getServiceLooper() {
        return this.mHandler.getLooper();
    }

    int getPhysicalAddress() {
        return this.mHdmiCecNetwork.getPhysicalAddress();
    }

    int getVendorId() {
        return this.mCecController.getVendorId();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    android.hardware.hdmi.HdmiDeviceInfo getDeviceInfo(int logicalAddress) {
        assertRunOnServiceThread();
        return this.mHdmiCecNetwork.getCecDeviceInfo(logicalAddress);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    android.hardware.hdmi.HdmiDeviceInfo getDeviceInfoByPort(int port) {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiMhlLocalDeviceStub info = this.mMhlController.getLocalDevice(port);
        if (info != null) {
            return info.getInfo();
        }
        return null;
    }

    protected int getCecVersion() {
        return this.mCecVersion;
    }

    boolean isConnectedToArcPort(int physicalAddress) {
        return this.mHdmiCecNetwork.isConnectedToArcPort(physicalAddress);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    boolean isConnected(int portId) {
        assertRunOnServiceThread();
        return this.mCecController.isConnected(portId);
    }

    void runOnServiceThread(java.lang.Runnable runnable) {
        this.mHandler.post(new com.android.server.hdmi.WorkSourceUidPreservingRunnable(runnable));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void assertRunOnServiceThread() {
        if (android.os.Looper.myLooper() != this.mHandler.getLooper()) {
            throw new java.lang.IllegalStateException("Should run on service thread.");
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void sendCecCommand(com.android.server.hdmi.HdmiCecMessage command) {
        sendCecCommand(command, null);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void sendCecCommand(com.android.server.hdmi.HdmiCecMessage command, com.android.server.hdmi.HdmiControlService.SendMessageCallback callback) {
        switch (command.getOpcode()) {
            case 4:
            case 13:
            case 128:
            case 130:
            case 134:
            case 157:
                if (isTvDeviceEnabled()) {
                    tv().removeAction(com.android.server.hdmi.RequestActiveSourceAction.class);
                }
                sendCecCommandWithRetries(command, callback);
                break;
            default:
                sendCecCommandWithoutRetries(command, callback);
                break;
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void sendCecCommandWithRetries(final com.android.server.hdmi.HdmiCecMessage command, final com.android.server.hdmi.HdmiControlService.SendMessageCallback callback) {
        assertRunOnServiceThread();
        final com.android.server.hdmi.HdmiCecLocalDevice localDevice = getAllCecLocalDevices().get(0);
        if (localDevice != null) {
            sendCecCommandWithoutRetries(command, new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.HdmiControlService.22
                @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
                public void onSendCompleted(int result) {
                    if (result != 0) {
                        localDevice.addAndStartAction(new com.android.server.hdmi.ResendCecCommandAction(localDevice, command, callback));
                    }
                }
            });
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void sendCecCommandWithoutRetries(com.android.server.hdmi.HdmiCecMessage command, com.android.server.hdmi.HdmiControlService.SendMessageCallback callback) {
        assertRunOnServiceThread();
        if (command.getValidationResult() == 0 && verifyPhysicalAddresses(command)) {
            this.mCecController.sendCommand(command, callback);
            return;
        }
        com.android.server.hdmi.HdmiLogger.error("Invalid message type:" + command, new java.lang.Object[0]);
        if (callback != null) {
            callback.onSendCompleted(3);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void maySendFeatureAbortCommand(com.android.server.hdmi.HdmiCecMessage command, int reason) {
        assertRunOnServiceThread();
        this.mCecController.maySendFeatureAbortCommand(command, reason);
    }

    boolean verifyPhysicalAddresses(com.android.server.hdmi.HdmiCecMessage message) {
        byte[] params = message.getParams();
        switch (message.getOpcode()) {
            case 112:
                return params.length == 0 || verifyPhysicalAddress(params, 0);
            case 128:
                return verifyPhysicalAddress(params, 0) && verifyPhysicalAddress(params, 2);
            case 129:
            case 130:
            case 132:
            case 134:
            case 157:
                return verifyPhysicalAddress(params, 0);
            case 161:
            case 162:
                return verifyExternalSourcePhysicalAddress(params, 7);
            default:
                return true;
        }
    }

    private boolean verifyPhysicalAddress(byte[] params, int offset) {
        if (!isTvDevice()) {
            return true;
        }
        if (params.length < offset + 2) {
            return false;
        }
        int path = com.android.server.hdmi.HdmiUtils.twoBytesToInt(params, offset);
        if (path != 65535 && path == getPhysicalAddress()) {
            return true;
        }
        int portId = pathToPortId(path);
        return portId != -1;
    }

    private boolean verifyExternalSourcePhysicalAddress(byte[] params, int offset) {
        int externalSourceSpecifier = params[offset];
        int offset2 = offset + 1;
        if (externalSourceSpecifier != 5 || params.length - offset2 < 2) {
            return true;
        }
        return verifyPhysicalAddress(params, offset2);
    }

    private boolean sourceAddressIsLocal(com.android.server.hdmi.HdmiCecMessage message) {
        for (com.android.server.hdmi.HdmiCecLocalDevice device : getAllCecLocalDevices()) {
            if (message.getSource() == device.getDeviceInfo().getLogicalAddress() && message.getSource() != 15) {
                com.android.server.hdmi.HdmiLogger.warning("Unexpected source: message sent from device itself, " + message, new java.lang.Object[0]);
                return true;
            }
        }
        return false;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleCecCommand(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int validationResult = message.getValidationResult();
        if (validationResult == 3 || validationResult == 5 || !verifyPhysicalAddresses(message)) {
            return 3;
        }
        if (validationResult != 0 || sourceAddressIsLocal(message)) {
            return -1;
        }
        getHdmiCecNetwork().handleCecMessage(message);
        int handleMessageResult = dispatchMessageToLocalDevice(message);
        if (!this.mAddressAllocated && this.mCecMessageBuffer.bufferMessage(message)) {
            return -1;
        }
        return handleMessageResult;
    }

    void enableAudioReturnChannel(int portId, boolean enabled) {
        if (!this.mTransitionFromArcToEarcTxEnabled && enabled && this.mEarcController != null) {
            setEarcEnabledInHal(false, false);
        }
        this.mCecController.enableAudioReturnChannel(portId, enabled);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int dispatchMessageToLocalDevice(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        for (com.android.server.hdmi.HdmiCecLocalDevice device : this.mHdmiCecNetwork.getLocalDeviceList()) {
            int messageResult = device.dispatchMessage(message);
            if (messageResult != -2 && message.getDestination() != 15) {
                return messageResult;
            }
        }
        if (message.getDestination() == 15) {
            return -1;
        }
        com.android.server.hdmi.HdmiLogger.warning("Unhandled cec command:" + message, new java.lang.Object[0]);
        return -2;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void onHotplug(int portId, boolean connected) {
        assertRunOnServiceThread();
        this.mHdmiCecNetwork.initPortInfo();
        android.hardware.hdmi.HdmiPortInfo portInfo = getPortInfo(portId);
        if (connected && !isTvDevice() && portInfo != null && portInfo.getType() == 1) {
            java.util.ArrayList<com.android.server.hdmi.HdmiCecLocalDevice> localDevices = new java.util.ArrayList<>();
            java.util.Iterator<java.lang.Integer> it = getCecLocalDeviceTypes().iterator();
            while (it.hasNext()) {
                int type = it.next().intValue();
                com.android.server.hdmi.HdmiCecLocalDevice localDevice = this.mHdmiCecNetwork.getLocalDevice(type);
                if (localDevice == null) {
                    localDevice = com.android.server.hdmi.HdmiCecLocalDevice.create(this, type);
                    localDevice.init();
                }
                localDevices.add(localDevice);
            }
            allocateLogicalAddress(localDevices, 4);
        }
        for (com.android.server.hdmi.HdmiCecLocalDevice device : this.mHdmiCecNetwork.getLocalDeviceList()) {
            device.onHotplug(portId, connected);
        }
        announceHotplugEvent(portId, connected);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void pollDevices(com.android.server.hdmi.HdmiControlService.DevicePollingCallback callback, int sourceAddress, int pickStrategy, int retryCount, long pollingMessageInterval) {
        assertRunOnServiceThread();
        this.mCecController.pollDevices(callback, sourceAddress, checkPollStrategy(pickStrategy), retryCount, pollingMessageInterval);
    }

    private int checkPollStrategy(int pickStrategy) {
        int strategy = pickStrategy & 3;
        if (strategy == 0) {
            throw new java.lang.IllegalArgumentException("Invalid poll strategy:" + pickStrategy);
        }
        int iterationStrategy = 196608 & pickStrategy;
        if (iterationStrategy == 0) {
            throw new java.lang.IllegalArgumentException("Invalid iteration strategy:" + pickStrategy);
        }
        return strategy | iterationStrategy;
    }

    java.util.List<com.android.server.hdmi.HdmiCecLocalDevice> getAllCecLocalDevices() {
        assertRunOnServiceThread();
        return this.mHdmiCecNetwork.getLocalDeviceList();
    }

    protected void checkLogicalAddressConflictAndReallocate(int logicalAddress, int physicalAddress) {
        if (physicalAddress == getPhysicalAddress()) {
            return;
        }
        for (com.android.server.hdmi.HdmiCecLocalDevice device : getAllCecLocalDevices()) {
            if (device.getDeviceInfo().getLogicalAddress() == logicalAddress) {
                com.android.server.hdmi.HdmiLogger.debug("allocate logical address for " + device.getDeviceInfo(), new java.lang.Object[0]);
                java.util.ArrayList<com.android.server.hdmi.HdmiCecLocalDevice> localDevices = new java.util.ArrayList<>();
                localDevices.add(device);
                allocateLogicalAddress(localDevices, 4);
                return;
            }
        }
    }

    java.lang.Object getServiceLock() {
        return this.mLock;
    }

    void setAudioStatus(boolean mute, int volume) {
        if (!isTvDeviceEnabled() || !tv().isSystemAudioActivated() || !tv().isArcEstablished() || getHdmiCecVolumeControl() == 0) {
            return;
        }
        com.android.server.hdmi.AudioManagerWrapper audioManager = getAudioManager();
        boolean muted = audioManager.isStreamMute(3);
        if (mute) {
            if (!muted) {
                audioManager.setStreamMute(3, true);
                return;
            }
            return;
        }
        if (muted) {
            audioManager.setStreamMute(3, false);
        }
        if (volume >= 0 && volume <= 100) {
            android.util.Slog.i(TAG, "volume: " + volume);
            int flag = 1 | 256;
            audioManager.setStreamVolume(3, volume, flag);
        }
    }

    void announceSystemAudioModeChange(boolean enabled) {
        synchronized (this.mLock) {
            for (com.android.server.hdmi.HdmiControlService.SystemAudioModeChangeListenerRecord record : this.mSystemAudioModeChangeListenerRecords) {
                invokeSystemAudioModeChangeLocked(record.mListener, enabled);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.hardware.hdmi.HdmiDeviceInfo createDeviceInfo(int logicalAddress, int deviceType, int powerStatus, int cecVersion) {
        java.lang.String displayName = readStringSetting("device_name", android.os.Build.MODEL);
        return android.hardware.hdmi.HdmiDeviceInfo.cecDeviceBuilder().setLogicalAddress(logicalAddress).setPhysicalAddress(getPhysicalAddress()).setPortId(pathToPortId(getPhysicalAddress())).setDeviceType(deviceType).setVendorId(getVendorId()).setDisplayName(displayName).setDevicePowerStatus(powerStatus).setCecVersion(cecVersion).build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDisplayName(java.lang.String newDisplayName) {
        for (com.android.server.hdmi.HdmiCecLocalDevice device : getAllCecLocalDevices()) {
            android.hardware.hdmi.HdmiDeviceInfo deviceInfo = device.getDeviceInfo();
            if (!deviceInfo.getDisplayName().equals(newDisplayName)) {
                device.setDeviceInfo(deviceInfo.toBuilder().setDisplayName(newDisplayName).build());
                sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildSetOsdNameCommand(deviceInfo.getLogicalAddress(), 0, newDisplayName));
            }
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void handleMhlHotplugEvent(int portId, boolean connected) {
        assertRunOnServiceThread();
        if (connected) {
            com.android.server.hdmi.HdmiMhlLocalDeviceStub newDevice = new com.android.server.hdmi.HdmiMhlLocalDeviceStub(this, portId);
            com.android.server.hdmi.HdmiMhlLocalDeviceStub oldDevice = this.mMhlController.addLocalDevice(newDevice);
            if (oldDevice != null) {
                oldDevice.onDeviceRemoved();
                android.util.Slog.i(TAG, "Old device of port " + portId + " is removed");
            }
            invokeDeviceEventListeners(newDevice.getInfo(), 1);
            updateSafeMhlInput();
        } else {
            com.android.server.hdmi.HdmiMhlLocalDeviceStub device = this.mMhlController.removeLocalDevice(portId);
            if (device == null) {
                android.util.Slog.w(TAG, "No device to remove:[portId=" + portId);
            } else {
                device.onDeviceRemoved();
                invokeDeviceEventListeners(device.getInfo(), 2);
                updateSafeMhlInput();
            }
        }
        announceHotplugEvent(portId, connected);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void handleMhlBusModeChanged(int portId, int busmode) {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiMhlLocalDeviceStub device = this.mMhlController.getLocalDevice(portId);
        if (device != null) {
            device.setBusMode(busmode);
        } else {
            android.util.Slog.w(TAG, "No mhl device exists for bus mode change[portId:" + portId + ", busmode:" + busmode + "]");
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void handleMhlBusOvercurrent(int portId, boolean on) {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiMhlLocalDeviceStub device = this.mMhlController.getLocalDevice(portId);
        if (device != null) {
            device.onBusOvercurrentDetected(on);
        } else {
            android.util.Slog.w(TAG, "No mhl device exists for bus overcurrent event[portId:" + portId + "]");
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void handleMhlDeviceStatusChanged(int portId, int adopterId, int deviceId) {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiMhlLocalDeviceStub device = this.mMhlController.getLocalDevice(portId);
        if (device != null) {
            device.setDeviceStatusChange(adopterId, deviceId);
        } else {
            android.util.Slog.w(TAG, "No mhl device exists for device status event[portId:" + portId + ", adopterId:" + adopterId + ", deviceId:" + deviceId + "]");
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void updateSafeMhlInput() {
        assertRunOnServiceThread();
        java.util.List<android.hardware.hdmi.HdmiDeviceInfo> inputs = java.util.Collections.emptyList();
        android.util.SparseArray<com.android.server.hdmi.HdmiMhlLocalDeviceStub> devices = this.mMhlController.getAllLocalDevices();
        for (int i = 0; i < devices.size(); i++) {
            com.android.server.hdmi.HdmiMhlLocalDeviceStub device = devices.valueAt(i);
            android.hardware.hdmi.HdmiDeviceInfo info = device.getInfo();
            if (info != null) {
                if (inputs.isEmpty()) {
                    inputs = new java.util.ArrayList();
                }
                inputs.add(device.getInfo());
            }
        }
        synchronized (this.mLock) {
            this.mMhlDevices = inputs;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.hardware.hdmi.HdmiDeviceInfo> getMhlDevicesLocked() {
        return this.mMhlDevices;
    }

    private class HdmiMhlVendorCommandListenerRecord implements android.os.IBinder.DeathRecipient {
        private final android.hardware.hdmi.IHdmiMhlVendorCommandListener mListener;

        public HdmiMhlVendorCommandListenerRecord(android.hardware.hdmi.IHdmiMhlVendorCommandListener listener) {
            this.mListener = listener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.hdmi.HdmiControlService.this.mMhlVendorCommandListenerRecords.remove(this);
        }
    }

    private final class HdmiControlStatusChangeListenerRecord implements android.os.IBinder.DeathRecipient {
        private final android.hardware.hdmi.IHdmiControlStatusChangeListener mListener;

        HdmiControlStatusChangeListenerRecord(android.hardware.hdmi.IHdmiControlStatusChangeListener listener) {
            this.mListener = listener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.hdmi.HdmiControlService.this.mLock) {
                com.android.server.hdmi.HdmiControlService.this.mHdmiControlStatusChangeListenerRecords.remove(this);
            }
        }

        public boolean equals(java.lang.Object obj) {
            if (!(obj instanceof com.android.server.hdmi.HdmiControlService.HdmiControlStatusChangeListenerRecord)) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            com.android.server.hdmi.HdmiControlService.HdmiControlStatusChangeListenerRecord other = (com.android.server.hdmi.HdmiControlService.HdmiControlStatusChangeListenerRecord) obj;
            return other.mListener == this.mListener;
        }

        public int hashCode() {
            return this.mListener.hashCode();
        }
    }

    private final class HotplugEventListenerRecord implements android.os.IBinder.DeathRecipient {
        private final android.hardware.hdmi.IHdmiHotplugEventListener mListener;

        public HotplugEventListenerRecord(android.hardware.hdmi.IHdmiHotplugEventListener listener) {
            this.mListener = listener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.hdmi.HdmiControlService.this.mLock) {
                com.android.server.hdmi.HdmiControlService.this.mHotplugEventListenerRecords.remove(this);
            }
        }

        public boolean equals(java.lang.Object obj) {
            if (!(obj instanceof com.android.server.hdmi.HdmiControlService.HotplugEventListenerRecord)) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            com.android.server.hdmi.HdmiControlService.HotplugEventListenerRecord other = (com.android.server.hdmi.HdmiControlService.HotplugEventListenerRecord) obj;
            return other.mListener == this.mListener;
        }

        public int hashCode() {
            return this.mListener.hashCode();
        }
    }

    private final class DeviceEventListenerRecord implements android.os.IBinder.DeathRecipient {
        private final android.hardware.hdmi.IHdmiDeviceEventListener mListener;

        public DeviceEventListenerRecord(android.hardware.hdmi.IHdmiDeviceEventListener listener) {
            this.mListener = listener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.hdmi.HdmiControlService.this.mLock) {
                com.android.server.hdmi.HdmiControlService.this.mDeviceEventListenerRecords.remove(this);
            }
        }
    }

    private final class SystemAudioModeChangeListenerRecord implements android.os.IBinder.DeathRecipient {
        private final android.hardware.hdmi.IHdmiSystemAudioModeChangeListener mListener;

        public SystemAudioModeChangeListenerRecord(android.hardware.hdmi.IHdmiSystemAudioModeChangeListener listener) {
            this.mListener = listener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.hdmi.HdmiControlService.this.mLock) {
                com.android.server.hdmi.HdmiControlService.this.mSystemAudioModeChangeListenerRecords.remove(this);
            }
        }
    }

    class VendorCommandListenerRecord implements android.os.IBinder.DeathRecipient {
        private final android.hardware.hdmi.IHdmiVendorCommandListener mListener;
        private final int mVendorId;

        VendorCommandListenerRecord(android.hardware.hdmi.IHdmiVendorCommandListener listener, int vendorId) {
            this.mListener = listener;
            this.mVendorId = vendorId;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.hdmi.HdmiControlService.this.mLock) {
                com.android.server.hdmi.HdmiControlService.this.mVendorCommandListenerRecords.remove(this);
            }
        }
    }

    private class HdmiRecordListenerRecord implements android.os.IBinder.DeathRecipient {
        private final android.hardware.hdmi.IHdmiRecordListener mListener;

        public HdmiRecordListenerRecord(android.hardware.hdmi.IHdmiRecordListener listener) {
            this.mListener = listener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.hdmi.HdmiControlService.this.mLock) {
                if (com.android.server.hdmi.HdmiControlService.this.mRecordListenerRecord == this) {
                    com.android.server.hdmi.HdmiControlService.this.mRecordListenerRecord = null;
                }
            }
        }
    }

    private void setWorkSourceUidToCallingUid() {
        android.os.Binder.setCallingWorkSourceUid(android.os.Binder.getCallingUid());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceAccessPermission() {
        getContext().enforceCallingOrSelfPermission(PERMISSION, TAG);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initBinderCall() {
        enforceAccessPermission();
        setWorkSourceUidToCallingUid();
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class BinderService extends android.hardware.hdmi.IHdmiControlService.Stub {
        private BinderService() {
        }

        public int[] getSupportedTypes() {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            int[] localDevices = new int[com.android.server.hdmi.HdmiControlService.this.mCecLocalDevices.size()];
            for (int i = 0; i < localDevices.length; i++) {
                localDevices[i] = ((java.lang.Integer) com.android.server.hdmi.HdmiControlService.this.mCecLocalDevices.get(i)).intValue();
            }
            return localDevices;
        }

        public android.hardware.hdmi.HdmiDeviceInfo getActiveSource() {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            return com.android.server.hdmi.HdmiControlService.this.getActiveSource();
        }

        public void deviceSelect(final int deviceId, final android.hardware.hdmi.IHdmiControlCallback callback) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.1
                @Override // java.lang.Runnable
                public void run() {
                    if (callback == null) {
                        android.util.Slog.e(com.android.server.hdmi.HdmiControlService.TAG, "Callback cannot be null");
                        return;
                    }
                    com.android.server.hdmi.HdmiCecLocalDeviceTv tv = com.android.server.hdmi.HdmiControlService.this.tv();
                    com.android.server.hdmi.HdmiCecLocalDevicePlayback playback = com.android.server.hdmi.HdmiControlService.this.playback();
                    if (tv == null && playback == null) {
                        if (!com.android.server.hdmi.HdmiControlService.this.mAddressAllocated) {
                            com.android.server.hdmi.HdmiControlService.this.mSelectRequestBuffer.set(com.android.server.hdmi.SelectRequestBuffer.newDeviceSelect(com.android.server.hdmi.HdmiControlService.this, deviceId, callback));
                            return;
                        } else if (com.android.server.hdmi.HdmiControlService.this.isTvDevice()) {
                            android.util.Slog.e(com.android.server.hdmi.HdmiControlService.TAG, "Local tv device not available");
                            return;
                        } else {
                            com.android.server.hdmi.HdmiControlService.this.invokeCallback(callback, 2);
                            return;
                        }
                    }
                    if (tv != null) {
                        com.android.server.hdmi.HdmiMhlLocalDeviceStub device = com.android.server.hdmi.HdmiControlService.this.mMhlController.getLocalDeviceById(deviceId);
                        if (device != null) {
                            if (device.getPortId() == tv.getActivePortId()) {
                                com.android.server.hdmi.HdmiControlService.this.invokeCallback(callback, 0);
                                return;
                            } else {
                                device.turnOn(callback);
                                tv.doManualPortSwitching(device.getPortId(), null);
                                return;
                            }
                        }
                        tv.deviceSelect(deviceId, callback);
                        return;
                    }
                    playback.deviceSelect(deviceId, callback);
                }
            });
        }

        public void portSelect(final int portId, final android.hardware.hdmi.IHdmiControlCallback callback) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.2
                @Override // java.lang.Runnable
                public void run() {
                    if (callback == null) {
                        android.util.Slog.e(com.android.server.hdmi.HdmiControlService.TAG, "Callback cannot be null");
                        return;
                    }
                    com.android.server.hdmi.HdmiCecLocalDeviceTv tv = com.android.server.hdmi.HdmiControlService.this.tv();
                    if (tv != null) {
                        tv.doManualPortSwitching(portId, callback);
                        return;
                    }
                    com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem audioSystem = com.android.server.hdmi.HdmiControlService.this.audioSystem();
                    if (audioSystem != null) {
                        audioSystem.doManualPortSwitching(portId, callback);
                    } else if (!com.android.server.hdmi.HdmiControlService.this.mAddressAllocated) {
                        com.android.server.hdmi.HdmiControlService.this.mSelectRequestBuffer.set(com.android.server.hdmi.SelectRequestBuffer.newPortSelect(com.android.server.hdmi.HdmiControlService.this, portId, callback));
                    } else {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "Local device not available");
                        com.android.server.hdmi.HdmiControlService.this.invokeCallback(callback, 2);
                    }
                }
            });
        }

        public void sendKeyEvent(final int deviceType, final int keyCode, final boolean isPressed) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.3
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.hdmi.HdmiMhlLocalDeviceStub device = com.android.server.hdmi.HdmiControlService.this.mMhlController.getLocalDevice(com.android.server.hdmi.HdmiControlService.this.mActivePortId);
                    if (device != null) {
                        device.sendKeyEvent(keyCode, isPressed);
                        return;
                    }
                    if (com.android.server.hdmi.HdmiControlService.this.mCecController != null) {
                        com.android.server.hdmi.HdmiCecLocalDevice localDevice = com.android.server.hdmi.HdmiControlService.this.mHdmiCecNetwork.getLocalDevice(deviceType);
                        if (localDevice == null) {
                            android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "Local device not available to send key event.");
                        } else {
                            localDevice.sendKeyEvent(keyCode, isPressed);
                        }
                    }
                }
            });
        }

        public void sendVolumeKeyEvent(final int deviceType, final int keyCode, final boolean isPressed) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.4
                @Override // java.lang.Runnable
                public void run() {
                    if (com.android.server.hdmi.HdmiControlService.this.mCecController == null) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "CEC controller not available to send volume key event.");
                        return;
                    }
                    com.android.server.hdmi.HdmiCecLocalDevice localDevice = com.android.server.hdmi.HdmiControlService.this.mHdmiCecNetwork.getLocalDevice(deviceType);
                    if (localDevice == null) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "Local device " + deviceType + " not available to send volume key event.");
                    } else {
                        localDevice.sendVolumeKeyEvent(keyCode, isPressed);
                    }
                }
            });
        }

        public void oneTouchPlay(final android.hardware.hdmi.IHdmiControlCallback callback) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            int pid = android.os.Binder.getCallingPid();
            android.util.Slog.d(com.android.server.hdmi.HdmiControlService.TAG, "Process pid: " + pid + " is calling oneTouchPlay.");
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.5
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.hdmi.HdmiControlService.this.oneTouchPlay(callback);
                }
            });
        }

        public void toggleAndFollowTvPower() {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            int pid = android.os.Binder.getCallingPid();
            android.util.Slog.d(com.android.server.hdmi.HdmiControlService.TAG, "Process pid: " + pid + " is calling toggleAndFollowTvPower.");
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.6
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.hdmi.HdmiControlService.this.toggleAndFollowTvPower();
                }
            });
        }

        public boolean shouldHandleTvPowerKey() {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            return com.android.server.hdmi.HdmiControlService.this.shouldHandleTvPowerKey();
        }

        public void queryDisplayStatus(final android.hardware.hdmi.IHdmiControlCallback callback) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.7
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.hdmi.HdmiControlService.this.queryDisplayStatus(callback);
                }
            });
        }

        public void addHdmiControlStatusChangeListener(android.hardware.hdmi.IHdmiControlStatusChangeListener listener) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.addHdmiControlStatusChangeListener(listener);
        }

        public void removeHdmiControlStatusChangeListener(android.hardware.hdmi.IHdmiControlStatusChangeListener listener) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.removeHdmiControlStatusChangeListener(listener);
        }

        public void addHdmiCecVolumeControlFeatureListener(android.hardware.hdmi.IHdmiCecVolumeControlFeatureListener listener) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.addHdmiCecVolumeControlFeatureListener(listener);
        }

        public void removeHdmiCecVolumeControlFeatureListener(android.hardware.hdmi.IHdmiCecVolumeControlFeatureListener listener) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.removeHdmiControlVolumeControlStatusChangeListener(listener);
        }

        public void addHotplugEventListener(android.hardware.hdmi.IHdmiHotplugEventListener listener) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.addHotplugEventListener(listener);
        }

        public void removeHotplugEventListener(android.hardware.hdmi.IHdmiHotplugEventListener listener) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.removeHotplugEventListener(listener);
        }

        public void addDeviceEventListener(android.hardware.hdmi.IHdmiDeviceEventListener listener) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.addDeviceEventListener(listener);
        }

        public java.util.List<android.hardware.hdmi.HdmiPortInfo> getPortInfo() {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            if (com.android.server.hdmi.HdmiControlService.this.getPortInfo() == null) {
                return java.util.Collections.emptyList();
            }
            return com.android.server.hdmi.HdmiControlService.this.getPortInfo();
        }

        public boolean canChangeSystemAudioMode() {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiCecLocalDeviceTv tv = com.android.server.hdmi.HdmiControlService.this.tv();
            if (tv == null) {
                return false;
            }
            return tv.hasSystemAudioDevice();
        }

        public boolean getSystemAudioMode() {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiCecLocalDeviceTv tv = com.android.server.hdmi.HdmiControlService.this.tv();
            com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem audioSystem = com.android.server.hdmi.HdmiControlService.this.audioSystem();
            return (tv != null && tv.isSystemAudioActivated()) || (audioSystem != null && audioSystem.isSystemAudioActivated());
        }

        public int getPhysicalAddress() {
            int physicalAddress;
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            synchronized (com.android.server.hdmi.HdmiControlService.this.mLock) {
                physicalAddress = com.android.server.hdmi.HdmiControlService.this.mHdmiCecNetwork.getPhysicalAddress();
            }
            return physicalAddress;
        }

        public void setSystemAudioMode(final boolean enabled, final android.hardware.hdmi.IHdmiControlCallback callback) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.8
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.hdmi.HdmiCecLocalDeviceTv tv = com.android.server.hdmi.HdmiControlService.this.tv();
                    if (tv == null) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "Local tv device not available");
                        com.android.server.hdmi.HdmiControlService.this.invokeCallback(callback, 2);
                    } else {
                        tv.changeSystemAudioMode(enabled, callback);
                    }
                }
            });
        }

        public void addSystemAudioModeChangeListener(android.hardware.hdmi.IHdmiSystemAudioModeChangeListener listener) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.addSystemAudioModeChangeListner(listener);
        }

        public void removeSystemAudioModeChangeListener(android.hardware.hdmi.IHdmiSystemAudioModeChangeListener listener) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.removeSystemAudioModeChangeListener(listener);
        }

        public void setInputChangeListener(android.hardware.hdmi.IHdmiInputChangeListener listener) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.setInputChangeListener(listener);
        }

        public java.util.List<android.hardware.hdmi.HdmiDeviceInfo> getInputDevices() {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            return com.android.server.hdmi.HdmiUtils.mergeToUnmodifiableList(com.android.server.hdmi.HdmiControlService.this.mHdmiCecNetwork.getSafeExternalInputsLocked(), com.android.server.hdmi.HdmiControlService.this.getMhlDevicesLocked());
        }

        public java.util.List<android.hardware.hdmi.HdmiDeviceInfo> getDeviceList() {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            return com.android.server.hdmi.HdmiControlService.this.mHdmiCecNetwork.getSafeCecDevicesLocked();
        }

        public void powerOffRemoteDevice(final int logicalAddress, final int powerStatus) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.9
                @Override // java.lang.Runnable
                public void run() {
                    android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "Device " + logicalAddress + " power status is " + powerStatus + " before standby command sent out");
                    com.android.server.hdmi.HdmiControlService.this.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildStandby(com.android.server.hdmi.HdmiControlService.this.getRemoteControlSourceAddress(), logicalAddress));
                }
            });
        }

        public void powerOnRemoteDevice(final int logicalAddress, final int powerStatus) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.10
                @Override // java.lang.Runnable
                public void run() {
                    android.util.Slog.i(com.android.server.hdmi.HdmiControlService.TAG, "Device " + logicalAddress + " power status is " + powerStatus + " before power on command sent out");
                    if (com.android.server.hdmi.HdmiControlService.this.getSwitchDevice() != null) {
                        com.android.server.hdmi.HdmiControlService.this.getSwitchDevice().sendUserControlPressedAndReleased(logicalAddress, 109);
                    } else {
                        android.util.Slog.e(com.android.server.hdmi.HdmiControlService.TAG, "Can't get the correct local device to handle routing.");
                    }
                }
            });
        }

        public void askRemoteDeviceToBecomeActiveSource(final int physicalAddress) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.11
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.hdmi.HdmiCecMessage setStreamPath = com.android.server.hdmi.HdmiCecMessageBuilder.buildSetStreamPath(com.android.server.hdmi.HdmiControlService.this.getRemoteControlSourceAddress(), physicalAddress);
                    if (com.android.server.hdmi.HdmiControlService.this.pathToPortId(physicalAddress) != -1) {
                        if (com.android.server.hdmi.HdmiControlService.this.getSwitchDevice() != null) {
                            com.android.server.hdmi.HdmiControlService.this.getSwitchDevice().handleSetStreamPath(setStreamPath);
                        } else {
                            android.util.Slog.e(com.android.server.hdmi.HdmiControlService.TAG, "Can't get the correct local device to handle routing.");
                        }
                    }
                    com.android.server.hdmi.HdmiControlService.this.sendCecCommand(setStreamPath);
                }
            });
        }

        public void setSystemAudioVolume(final int oldIndex, final int newIndex, final int maxIndex) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.12
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.hdmi.HdmiCecLocalDeviceTv tv = com.android.server.hdmi.HdmiControlService.this.tv();
                    if (tv == null) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "Local tv device not available");
                    } else {
                        tv.changeVolume(oldIndex, newIndex - oldIndex, maxIndex);
                    }
                }
            });
        }

        public void setSystemAudioMute(final boolean mute) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.13
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.hdmi.HdmiCecLocalDeviceTv tv = com.android.server.hdmi.HdmiControlService.this.tv();
                    if (tv == null) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "Local tv device not available");
                    } else {
                        tv.changeMute(mute);
                    }
                }
            });
        }

        public void setArcMode(final boolean enabled) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.14
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.hdmi.HdmiCecLocalDeviceTv tv = com.android.server.hdmi.HdmiControlService.this.tv();
                    if (tv == null) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "Local tv device not available to change arc mode.");
                    } else {
                        tv.startArcAction(enabled);
                    }
                }
            });
        }

        public void setProhibitMode(boolean enabled) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            if (!com.android.server.hdmi.HdmiControlService.this.isTvDevice()) {
                return;
            }
            com.android.server.hdmi.HdmiControlService.this.setProhibitMode(enabled);
        }

        public void addVendorCommandListener(android.hardware.hdmi.IHdmiVendorCommandListener listener, int vendorId) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.addVendorCommandListener(listener, vendorId);
        }

        public void sendVendorCommand(final int deviceType, final int targetAddress, final byte[] params, final boolean hasVendorId) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.15
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.hdmi.HdmiCecLocalDevice device = com.android.server.hdmi.HdmiControlService.this.mHdmiCecNetwork.getLocalDevice(deviceType);
                    if (device == null) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "Local device not available");
                    } else if (hasVendorId) {
                        com.android.server.hdmi.HdmiControlService.this.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildVendorCommandWithId(device.getDeviceInfo().getLogicalAddress(), targetAddress, com.android.server.hdmi.HdmiControlService.this.getVendorId(), params));
                    } else {
                        com.android.server.hdmi.HdmiControlService.this.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildVendorCommand(device.getDeviceInfo().getLogicalAddress(), targetAddress, params));
                    }
                }
            });
        }

        public void sendStandby(final int deviceType, final int deviceId) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.16
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.hdmi.HdmiMhlLocalDeviceStub mhlDevice = com.android.server.hdmi.HdmiControlService.this.mMhlController.getLocalDeviceById(deviceId);
                    if (mhlDevice != null) {
                        mhlDevice.sendStandby();
                        return;
                    }
                    com.android.server.hdmi.HdmiCecLocalDevice device = com.android.server.hdmi.HdmiControlService.this.mHdmiCecNetwork.getLocalDevice(deviceType);
                    if (device == null) {
                        device = com.android.server.hdmi.HdmiControlService.this.audioSystem();
                    }
                    if (device == null) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "Local device not available");
                    } else {
                        device.sendStandby(deviceId);
                    }
                }
            });
        }

        public void setHdmiRecordListener(android.hardware.hdmi.IHdmiRecordListener listener) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.setHdmiRecordListener(listener);
        }

        public void startOneTouchRecord(final int recorderAddress, final byte[] recordSource) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.17
                @Override // java.lang.Runnable
                public void run() {
                    if (!com.android.server.hdmi.HdmiControlService.this.isTvDeviceEnabled()) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "TV device is not enabled.");
                    } else {
                        com.android.server.hdmi.HdmiControlService.this.tv().startOneTouchRecord(recorderAddress, recordSource);
                    }
                }
            });
        }

        public void stopOneTouchRecord(final int recorderAddress) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.18
                @Override // java.lang.Runnable
                public void run() {
                    if (!com.android.server.hdmi.HdmiControlService.this.isTvDeviceEnabled()) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "TV device is not enabled.");
                    } else {
                        com.android.server.hdmi.HdmiControlService.this.tv().stopOneTouchRecord(recorderAddress);
                    }
                }
            });
        }

        public void startTimerRecording(final int recorderAddress, final int sourceType, final byte[] recordSource) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.19
                @Override // java.lang.Runnable
                public void run() {
                    if (!com.android.server.hdmi.HdmiControlService.this.isTvDeviceEnabled()) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "TV device is not enabled.");
                    } else {
                        com.android.server.hdmi.HdmiControlService.this.tv().startTimerRecording(recorderAddress, sourceType, recordSource);
                    }
                }
            });
        }

        public void clearTimerRecording(final int recorderAddress, final int sourceType, final byte[] recordSource) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.20
                @Override // java.lang.Runnable
                public void run() {
                    if (!com.android.server.hdmi.HdmiControlService.this.isTvDeviceEnabled()) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "TV device is not enabled.");
                    } else {
                        com.android.server.hdmi.HdmiControlService.this.tv().clearTimerRecording(recorderAddress, sourceType, recordSource);
                    }
                }
            });
        }

        public void sendMhlVendorCommand(final int portId, final int offset, final int length, final byte[] data) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.21
                @Override // java.lang.Runnable
                public void run() {
                    if (!com.android.server.hdmi.HdmiControlService.this.isCecControlEnabled()) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "Hdmi control is disabled.");
                        return;
                    }
                    com.android.server.hdmi.HdmiMhlLocalDeviceStub device = com.android.server.hdmi.HdmiControlService.this.mMhlController.getLocalDevice(portId);
                    if (device == null) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "Invalid port id:" + portId);
                    } else {
                        com.android.server.hdmi.HdmiControlService.this.mMhlController.sendVendorCommand(portId, offset, length, data);
                    }
                }
            });
        }

        public void addHdmiMhlVendorCommandListener(android.hardware.hdmi.IHdmiMhlVendorCommandListener listener) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.addHdmiMhlVendorCommandListener(listener);
        }

        public void setStandbyMode(final boolean isStandbyModeOn) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.22
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.hdmi.HdmiControlService.this.setStandbyMode(isStandbyModeOn);
                }
            });
        }

        public void reportAudioStatus(final int deviceType, int volume, int maxVolume, boolean isMute) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.23
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.hdmi.HdmiCecLocalDevice device = com.android.server.hdmi.HdmiControlService.this.mHdmiCecNetwork.getLocalDevice(deviceType);
                    if (device == null) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "Local device not available");
                        return;
                    }
                    if (com.android.server.hdmi.HdmiControlService.this.audioSystem() == null) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "audio system is not available");
                    } else if (!com.android.server.hdmi.HdmiControlService.this.audioSystem().isSystemAudioActivated()) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "audio system is not in system audio mode");
                    } else {
                        com.android.server.hdmi.HdmiControlService.this.audioSystem().reportAudioStatus(0);
                    }
                }
            });
        }

        public void setSystemAudioModeOnForAudioOnlySource() {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            com.android.server.hdmi.HdmiControlService.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.24
                @Override // java.lang.Runnable
                public void run() {
                    if (!com.android.server.hdmi.HdmiControlService.this.isAudioSystemDevice()) {
                        android.util.Slog.e(com.android.server.hdmi.HdmiControlService.TAG, "Not an audio system device. Won't set system audio mode on");
                        return;
                    }
                    if (com.android.server.hdmi.HdmiControlService.this.audioSystem() == null) {
                        android.util.Slog.e(com.android.server.hdmi.HdmiControlService.TAG, "Audio System local device is not registered");
                    } else if (!com.android.server.hdmi.HdmiControlService.this.audioSystem().checkSupportAndSetSystemAudioMode(true)) {
                        android.util.Slog.e(com.android.server.hdmi.HdmiControlService.TAG, "System Audio Mode is not supported.");
                    } else {
                        com.android.server.hdmi.HdmiControlService.this.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildSetSystemAudioMode(com.android.server.hdmi.HdmiControlService.this.audioSystem().getDeviceInfo().getLogicalAddress(), 15, true));
                    }
                }
            });
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) throws android.os.RemoteException {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            new com.android.server.hdmi.HdmiControlShellCommand(this).exec(this, in, out, err, args, callback, resultReceiver);
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.hdmi.HdmiControlService.this.getContext(), com.android.server.hdmi.HdmiControlService.TAG, writer)) {
                com.android.internal.util.IndentingPrintWriter pw = new com.android.internal.util.IndentingPrintWriter(writer, "  ");
                synchronized (com.android.server.hdmi.HdmiControlService.this.mLock) {
                    pw.println("mProhibitMode: " + com.android.server.hdmi.HdmiControlService.this.mProhibitMode);
                }
                pw.println("mPowerStatus: " + com.android.server.hdmi.HdmiControlService.this.mPowerStatusController.getPowerStatus());
                pw.println("mIsCecAvailable: " + com.android.server.hdmi.HdmiControlService.this.mIsCecAvailable);
                pw.println("mCecVersion: " + com.android.server.hdmi.HdmiControlService.this.mCecVersion);
                pw.println("mIsAbsoluteVolumeBehaviorEnabled: " + com.android.server.hdmi.HdmiControlService.this.isAbsoluteVolumeBehaviorEnabled());
                pw.println("System_settings:");
                pw.increaseIndent();
                pw.println("mMhlInputChangeEnabled: " + com.android.server.hdmi.HdmiControlService.this.isMhlInputChangeEnabled());
                pw.println("mSystemAudioActivated: " + com.android.server.hdmi.HdmiControlService.this.isSystemAudioActivated());
                pw.println("mHdmiCecVolumeControlEnabled: " + com.android.server.hdmi.HdmiControlService.this.getHdmiCecVolumeControl());
                pw.decreaseIndent();
                pw.println("CEC settings:");
                pw.increaseIndent();
                com.android.server.hdmi.HdmiCecConfig hdmiCecConfig = com.android.server.hdmi.HdmiControlService.this.getHdmiCecConfig();
                java.util.List<java.lang.String> allSettings = hdmiCecConfig.getAllSettings();
                java.util.Set<java.lang.String> userSettings = new java.util.HashSet<>(hdmiCecConfig.getUserSettings());
                for (java.lang.String setting : allSettings) {
                    if (hdmiCecConfig.isStringValueType(setting)) {
                        pw.println(setting + " (string): " + hdmiCecConfig.getStringValue(setting) + " (default: " + hdmiCecConfig.getDefaultStringValue(setting) + ")" + (userSettings.contains(setting) ? " [modifiable]" : ""));
                    } else if (hdmiCecConfig.isIntValueType(setting)) {
                        pw.println(setting + " (int): " + hdmiCecConfig.getIntValue(setting) + " (default: " + hdmiCecConfig.getDefaultIntValue(setting) + ")" + (userSettings.contains(setting) ? " [modifiable]" : ""));
                    }
                }
                pw.decreaseIndent();
                pw.println("mMhlController: ");
                pw.increaseIndent();
                com.android.server.hdmi.HdmiControlService.this.mMhlController.dump(pw);
                pw.decreaseIndent();
                pw.print("eARC local device: ");
                pw.increaseIndent();
                if (com.android.server.hdmi.HdmiControlService.this.mEarcLocalDevice == null) {
                    pw.println("None. eARC is either disabled or not available.");
                } else {
                    com.android.server.hdmi.HdmiControlService.this.mEarcLocalDevice.dump(pw);
                }
                pw.decreaseIndent();
                com.android.server.hdmi.HdmiControlService.this.mHdmiCecNetwork.dump(pw);
                if (com.android.server.hdmi.HdmiControlService.this.mCecController != null) {
                    pw.println("mCecController: ");
                    pw.increaseIndent();
                    com.android.server.hdmi.HdmiControlService.this.mCecController.dump(pw);
                    pw.decreaseIndent();
                }
            }
        }

        public boolean setMessageHistorySize(int newSize) {
            com.android.server.hdmi.HdmiControlService.this.enforceAccessPermission();
            if (com.android.server.hdmi.HdmiControlService.this.mCecController == null) {
                return false;
            }
            return com.android.server.hdmi.HdmiControlService.this.mCecController.setMessageHistorySize(newSize);
        }

        public int getMessageHistorySize() {
            com.android.server.hdmi.HdmiControlService.this.enforceAccessPermission();
            if (com.android.server.hdmi.HdmiControlService.this.mCecController != null) {
                return com.android.server.hdmi.HdmiControlService.this.mCecController.getMessageHistorySize();
            }
            return 0;
        }

        public void addCecSettingChangeListener(java.lang.String name, android.hardware.hdmi.IHdmiCecSettingChangeListener listener) {
            com.android.server.hdmi.HdmiControlService.this.enforceAccessPermission();
            com.android.server.hdmi.HdmiControlService.this.addCecSettingChangeListener(name, listener);
        }

        public void removeCecSettingChangeListener(java.lang.String name, android.hardware.hdmi.IHdmiCecSettingChangeListener listener) {
            com.android.server.hdmi.HdmiControlService.this.enforceAccessPermission();
            com.android.server.hdmi.HdmiControlService.this.removeCecSettingChangeListener(name, listener);
        }

        public java.util.List<java.lang.String> getUserCecSettings() {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.hdmi.HdmiControlService.this.getHdmiCecConfig().getUserSettings();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public java.util.List<java.lang.String> getAllowedCecSettingStringValues(java.lang.String name) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.hdmi.HdmiControlService.this.getHdmiCecConfig().getAllowedStringValues(name);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public int[] getAllowedCecSettingIntValues(java.lang.String name) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                java.util.List<java.lang.Integer> allowedValues = com.android.server.hdmi.HdmiControlService.this.getHdmiCecConfig().getAllowedIntValues(name);
                return allowedValues.stream().mapToInt(new java.util.function.ToIntFunction() { // from class: com.android.server.hdmi.HdmiControlService$BinderService$$ExternalSyntheticLambda0
                    @Override // java.util.function.ToIntFunction
                    public final int applyAsInt(java.lang.Object obj) {
                        return ((java.lang.Integer) obj).intValue();
                    }
                }).toArray();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public java.lang.String getCecSettingStringValue(java.lang.String name) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.hdmi.HdmiControlService.this.getHdmiCecConfig().getStringValue(name);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setCecSettingStringValue(java.lang.String name, java.lang.String value) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.hdmi.HdmiControlService.this.getHdmiCecConfig().setStringValue(name, value);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public int getCecSettingIntValue(java.lang.String name) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.hdmi.HdmiControlService.this.getHdmiCecConfig().getIntValue(name);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setCecSettingIntValue(java.lang.String name, int value) {
            com.android.server.hdmi.HdmiControlService.this.initBinderCall();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.hdmi.HdmiControlService.this.getHdmiCecConfig().setIntValue(name, value);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }
    }

    void setHdmiCecVolumeControlEnabledInternal(int hdmiCecVolumeControl) {
        this.mHdmiCecVolumeControl = hdmiCecVolumeControl;
        announceHdmiCecVolumeControlFeatureChange(hdmiCecVolumeControl);
        runOnServiceThread(new com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda2(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getRemoteControlSourceAddress() {
        if (isAudioSystemDevice()) {
            return audioSystem().getDeviceInfo().getLogicalAddress();
        }
        if (isPlaybackDevice()) {
            return playback().getDeviceInfo().getLogicalAddress();
        }
        return 15;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.hdmi.HdmiCecLocalDeviceSource getSwitchDevice() {
        if (isAudioSystemDevice()) {
            return audioSystem();
        }
        if (isPlaybackDevice()) {
            return playback();
        }
        return null;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void oneTouchPlay(android.hardware.hdmi.IHdmiControlCallback callback) {
        assertRunOnServiceThread();
        if (!this.mAddressAllocated) {
            this.mOtpCallbackPendingAddressAllocation = callback;
            android.util.Slog.d(TAG, "Local device is under address allocation. Save OTP callback for later process.");
            return;
        }
        com.android.server.hdmi.HdmiCecLocalDeviceSource source = playback();
        if (source == null) {
            source = audioSystem();
        }
        if (source == null) {
            android.util.Slog.w(TAG, "Local source device not available");
            invokeCallback(callback, 2);
        } else {
            source.oneTouchPlay(callback);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void toggleAndFollowTvPower() {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiCecLocalDeviceSource source = playback();
        if (source == null) {
            source = audioSystem();
        }
        if (source == null) {
            android.util.Slog.w(TAG, "Local source device not available");
        } else {
            source.toggleAndFollowTvPower();
        }
    }

    protected boolean shouldHandleTvPowerKey() {
        if (isTvDevice()) {
            return false;
        }
        java.lang.String powerControlMode = getHdmiCecConfig().getStringValue("power_control_mode");
        if (powerControlMode.equals("none")) {
            return false;
        }
        int hdmiCecEnabled = getHdmiCecConfig().getIntValue("hdmi_cec_enabled");
        if (hdmiCecEnabled != 1) {
            return false;
        }
        return this.mIsCecAvailable;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void queryDisplayStatus(android.hardware.hdmi.IHdmiControlCallback callback) {
        assertRunOnServiceThread();
        if (!this.mAddressAllocated) {
            this.mDisplayStatusCallback = callback;
            android.util.Slog.d(TAG, "Local device is under address allocation. Queue display callback for later process.");
            return;
        }
        com.android.server.hdmi.HdmiCecLocalDeviceSource source = playback();
        if (source == null) {
            source = audioSystem();
        }
        if (source == null) {
            android.util.Slog.w(TAG, "Local source device not available");
            invokeCallback(callback, -1);
        } else {
            source.queryDisplayStatus(callback);
        }
    }

    protected android.hardware.hdmi.HdmiDeviceInfo getActiveSource() {
        int activePath;
        if (playback() != null && playback().isActiveSource()) {
            return playback().getDeviceInfo();
        }
        com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource activeSource = getLocalActiveSource();
        if (activeSource.isValid()) {
            android.hardware.hdmi.HdmiDeviceInfo activeSourceInfo = this.mHdmiCecNetwork.getSafeCecDeviceInfo(activeSource.logicalAddress);
            if (activeSourceInfo != null) {
                return activeSourceInfo;
            }
            return android.hardware.hdmi.HdmiDeviceInfo.hardwarePort(activeSource.physicalAddress, pathToPortId(activeSource.physicalAddress));
        }
        if (tv() != null && (activePath = tv().getActivePath()) != 65535) {
            android.hardware.hdmi.HdmiDeviceInfo info = this.mHdmiCecNetwork.getSafeDeviceInfoByPath(activePath);
            return info != null ? info : android.hardware.hdmi.HdmiDeviceInfo.hardwarePort(activePath, tv().getActivePortId());
        }
        return null;
    }

    void addHdmiControlStatusChangeListener(final android.hardware.hdmi.IHdmiControlStatusChangeListener listener) {
        final com.android.server.hdmi.HdmiControlService.HdmiControlStatusChangeListenerRecord record = new com.android.server.hdmi.HdmiControlService.HdmiControlStatusChangeListenerRecord(listener);
        try {
            listener.asBinder().linkToDeath(record, 0);
            synchronized (this.mLock) {
                this.mHdmiControlStatusChangeListenerRecords.add(record);
            }
            runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.23
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (com.android.server.hdmi.HdmiControlService.this.mLock) {
                        if (com.android.server.hdmi.HdmiControlService.this.mHdmiControlStatusChangeListenerRecords.contains(record)) {
                            synchronized (com.android.server.hdmi.HdmiControlService.this.mLock) {
                                com.android.server.hdmi.HdmiControlService.this.invokeHdmiControlStatusChangeListenerLocked(listener, com.android.server.hdmi.HdmiControlService.this.mHdmiControlEnabled);
                            }
                        }
                    }
                }
            });
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Listener already died");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeHdmiControlStatusChangeListener(android.hardware.hdmi.IHdmiControlStatusChangeListener listener) {
        synchronized (this.mLock) {
            java.util.Iterator<com.android.server.hdmi.HdmiControlService.HdmiControlStatusChangeListenerRecord> it = this.mHdmiControlStatusChangeListenerRecords.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                com.android.server.hdmi.HdmiControlService.HdmiControlStatusChangeListenerRecord record = it.next();
                if (record.mListener.asBinder() == listener.asBinder()) {
                    listener.asBinder().unlinkToDeath(record, 0);
                    this.mHdmiControlStatusChangeListenerRecords.remove(record);
                    break;
                }
            }
        }
    }

    void addHdmiCecVolumeControlFeatureListener(final android.hardware.hdmi.IHdmiCecVolumeControlFeatureListener listener) {
        this.mHdmiCecVolumeControlFeatureListenerRecords.register(listener);
        runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.24
            @Override // java.lang.Runnable
            public void run() {
                synchronized (com.android.server.hdmi.HdmiControlService.this.mLock) {
                    try {
                        listener.onHdmiCecVolumeControlFeature(com.android.server.hdmi.HdmiControlService.this.mHdmiCecVolumeControl);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.hdmi.HdmiControlService.TAG, "Failed to report HdmiControlVolumeControlStatusChange: " + com.android.server.hdmi.HdmiControlService.this.mHdmiCecVolumeControl, e);
                    }
                }
            }
        });
    }

    void removeHdmiControlVolumeControlStatusChangeListener(android.hardware.hdmi.IHdmiCecVolumeControlFeatureListener listener) {
        this.mHdmiCecVolumeControlFeatureListenerRecords.unregister(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addHotplugEventListener(final android.hardware.hdmi.IHdmiHotplugEventListener listener) {
        final com.android.server.hdmi.HdmiControlService.HotplugEventListenerRecord record = new com.android.server.hdmi.HdmiControlService.HotplugEventListenerRecord(listener);
        try {
            listener.asBinder().linkToDeath(record, 0);
            synchronized (this.mLock) {
                this.mHotplugEventListenerRecords.add(record);
            }
            runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.25
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (com.android.server.hdmi.HdmiControlService.this.mLock) {
                        if (com.android.server.hdmi.HdmiControlService.this.mHotplugEventListenerRecords.contains(record)) {
                            for (android.hardware.hdmi.HdmiPortInfo port : com.android.server.hdmi.HdmiControlService.this.getPortInfo()) {
                                android.hardware.hdmi.HdmiHotplugEvent event = new android.hardware.hdmi.HdmiHotplugEvent(port.getId(), com.android.server.hdmi.HdmiControlService.this.mCecController.isConnected(port.getId()));
                                synchronized (com.android.server.hdmi.HdmiControlService.this.mLock) {
                                    com.android.server.hdmi.HdmiControlService.this.invokeHotplugEventListenerLocked(listener, event);
                                }
                            }
                        }
                    }
                }
            });
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Listener already died");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeHotplugEventListener(android.hardware.hdmi.IHdmiHotplugEventListener listener) {
        synchronized (this.mLock) {
            java.util.Iterator<com.android.server.hdmi.HdmiControlService.HotplugEventListenerRecord> it = this.mHotplugEventListenerRecords.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                com.android.server.hdmi.HdmiControlService.HotplugEventListenerRecord record = it.next();
                if (record.mListener.asBinder() == listener.asBinder()) {
                    listener.asBinder().unlinkToDeath(record, 0);
                    this.mHotplugEventListenerRecords.remove(record);
                    break;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addDeviceEventListener(android.hardware.hdmi.IHdmiDeviceEventListener listener) {
        com.android.server.hdmi.HdmiControlService.DeviceEventListenerRecord record = new com.android.server.hdmi.HdmiControlService.DeviceEventListenerRecord(listener);
        try {
            listener.asBinder().linkToDeath(record, 0);
            synchronized (this.mLock) {
                this.mDeviceEventListenerRecords.add(record);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Listener already died");
        }
    }

    void invokeDeviceEventListeners(android.hardware.hdmi.HdmiDeviceInfo device, int status) {
        synchronized (this.mLock) {
            for (com.android.server.hdmi.HdmiControlService.DeviceEventListenerRecord record : this.mDeviceEventListenerRecords) {
                try {
                    record.mListener.onStatusChanged(device, status);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Failed to report device event:" + e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addSystemAudioModeChangeListner(android.hardware.hdmi.IHdmiSystemAudioModeChangeListener listener) {
        com.android.server.hdmi.HdmiControlService.SystemAudioModeChangeListenerRecord record = new com.android.server.hdmi.HdmiControlService.SystemAudioModeChangeListenerRecord(listener);
        try {
            listener.asBinder().linkToDeath(record, 0);
            synchronized (this.mLock) {
                this.mSystemAudioModeChangeListenerRecords.add(record);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Listener already died");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeSystemAudioModeChangeListener(android.hardware.hdmi.IHdmiSystemAudioModeChangeListener listener) {
        synchronized (this.mLock) {
            java.util.Iterator<com.android.server.hdmi.HdmiControlService.SystemAudioModeChangeListenerRecord> it = this.mSystemAudioModeChangeListenerRecords.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                com.android.server.hdmi.HdmiControlService.SystemAudioModeChangeListenerRecord record = it.next();
                if (record.mListener.asBinder() == listener) {
                    listener.asBinder().unlinkToDeath(record, 0);
                    this.mSystemAudioModeChangeListenerRecords.remove(record);
                    break;
                }
            }
        }
    }

    private final class InputChangeListenerRecord implements android.os.IBinder.DeathRecipient {
        private final android.hardware.hdmi.IHdmiInputChangeListener mListener;

        public InputChangeListenerRecord(android.hardware.hdmi.IHdmiInputChangeListener listener) {
            this.mListener = listener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.hdmi.HdmiControlService.this.mLock) {
                if (com.android.server.hdmi.HdmiControlService.this.mInputChangeListenerRecord == this) {
                    com.android.server.hdmi.HdmiControlService.this.mInputChangeListenerRecord = null;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setInputChangeListener(android.hardware.hdmi.IHdmiInputChangeListener listener) {
        synchronized (this.mLock) {
            this.mInputChangeListenerRecord = new com.android.server.hdmi.HdmiControlService.InputChangeListenerRecord(listener);
            try {
                listener.asBinder().linkToDeath(this.mInputChangeListenerRecord, 0);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Listener already died");
            }
        }
    }

    void invokeInputChangeListener(android.hardware.hdmi.HdmiDeviceInfo info) {
        synchronized (this.mLock) {
            if (this.mInputChangeListenerRecord != null) {
                try {
                    this.mInputChangeListenerRecord.mListener.onChanged(info);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Exception thrown by IHdmiInputChangeListener: " + e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setHdmiRecordListener(android.hardware.hdmi.IHdmiRecordListener listener) {
        synchronized (this.mLock) {
            this.mRecordListenerRecord = new com.android.server.hdmi.HdmiControlService.HdmiRecordListenerRecord(listener);
            try {
                listener.asBinder().linkToDeath(this.mRecordListenerRecord, 0);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Listener already died.", e);
            }
        }
    }

    byte[] invokeRecordRequestListener(int recorderAddress) {
        synchronized (this.mLock) {
            if (this.mRecordListenerRecord != null) {
                try {
                    return this.mRecordListenerRecord.mListener.getOneTouchRecordSource(recorderAddress);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Failed to start record.", e);
                }
            }
            return libcore.util.EmptyArray.BYTE;
        }
    }

    void invokeOneTouchRecordResult(int recorderAddress, int result) {
        synchronized (this.mLock) {
            if (this.mRecordListenerRecord != null) {
                try {
                    this.mRecordListenerRecord.mListener.onOneTouchRecordResult(recorderAddress, result);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Failed to call onOneTouchRecordResult.", e);
                }
            }
        }
    }

    void invokeTimerRecordingResult(int recorderAddress, int result) {
        synchronized (this.mLock) {
            if (this.mRecordListenerRecord != null) {
                try {
                    this.mRecordListenerRecord.mListener.onTimerRecordingResult(recorderAddress, result);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Failed to call onTimerRecordingResult.", e);
                }
            }
        }
    }

    void invokeClearTimerRecordingResult(int recorderAddress, int result) {
        synchronized (this.mLock) {
            if (this.mRecordListenerRecord != null) {
                try {
                    this.mRecordListenerRecord.mListener.onClearTimerRecordingResult(recorderAddress, result);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Failed to call onClearTimerRecordingResult.", e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeCallback(android.hardware.hdmi.IHdmiControlCallback callback, int result) {
        if (callback == null) {
            return;
        }
        try {
            callback.onComplete(result);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Invoking callback failed:" + e);
        }
    }

    private void invokeSystemAudioModeChangeLocked(android.hardware.hdmi.IHdmiSystemAudioModeChangeListener listener, boolean enabled) {
        try {
            listener.onStatusChanged(enabled);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Invoking callback failed:" + e);
        }
    }

    private void announceHotplugEvent(int portId, boolean connected) {
        android.hardware.hdmi.HdmiHotplugEvent event = new android.hardware.hdmi.HdmiHotplugEvent(portId, connected);
        synchronized (this.mLock) {
            for (com.android.server.hdmi.HdmiControlService.HotplugEventListenerRecord record : this.mHotplugEventListenerRecords) {
                invokeHotplugEventListenerLocked(record.mListener, event);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeHotplugEventListenerLocked(android.hardware.hdmi.IHdmiHotplugEventListener listener, android.hardware.hdmi.HdmiHotplugEvent event) {
        try {
            listener.onReceived(event);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to report hotplug event:" + event.toString(), e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void announceHdmiControlStatusChange(int isEnabled) {
        assertRunOnServiceThread();
        synchronized (this.mLock) {
            java.util.List<android.hardware.hdmi.IHdmiControlStatusChangeListener> listeners = new java.util.ArrayList<>(this.mHdmiControlStatusChangeListenerRecords.size());
            for (com.android.server.hdmi.HdmiControlService.HdmiControlStatusChangeListenerRecord record : this.mHdmiControlStatusChangeListenerRecords) {
                listeners.add(record.mListener);
            }
            invokeHdmiControlStatusChangeListenerLocked(listeners, isEnabled);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeHdmiControlStatusChangeListenerLocked(android.hardware.hdmi.IHdmiControlStatusChangeListener listener, int isEnabled) {
        invokeHdmiControlStatusChangeListenerLocked(java.util.Collections.singletonList(listener), isEnabled);
    }

    private void invokeHdmiControlStatusChangeListenerLocked(final java.util.Collection<android.hardware.hdmi.IHdmiControlStatusChangeListener> listeners, final int isEnabled) {
        if (isEnabled == 1) {
            queryDisplayStatus(new android.hardware.hdmi.IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiControlService.26
                public void onComplete(int status) {
                    com.android.server.hdmi.HdmiControlService.this.mIsCecAvailable = status != -1;
                    if (!listeners.isEmpty()) {
                        com.android.server.hdmi.HdmiControlService.this.invokeHdmiControlStatusChangeListenerLocked(listeners, isEnabled, com.android.server.hdmi.HdmiControlService.this.mIsCecAvailable);
                    }
                }
            });
            return;
        }
        this.mIsCecAvailable = false;
        if (!listeners.isEmpty()) {
            invokeHdmiControlStatusChangeListenerLocked(listeners, isEnabled, this.mIsCecAvailable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeHdmiControlStatusChangeListenerLocked(java.util.Collection<android.hardware.hdmi.IHdmiControlStatusChangeListener> listeners, int isEnabled, boolean isCecAvailable) {
        for (android.hardware.hdmi.IHdmiControlStatusChangeListener listener : listeners) {
            try {
                listener.onStatusChange(isEnabled, isCecAvailable);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Failed to report HdmiControlStatusChange: " + isEnabled + " isAvailable: " + isCecAvailable, e);
            }
        }
    }

    private void announceHdmiCecVolumeControlFeatureChange(final int hdmiCecVolumeControl) {
        assertRunOnServiceThread();
        synchronized (this.mLock) {
            this.mHdmiCecVolumeControlFeatureListenerRecords.broadcast(new java.util.function.Consumer() { // from class: com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda7
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.hdmi.HdmiControlService.lambda$announceHdmiCecVolumeControlFeatureChange$2(hdmiCecVolumeControl, (android.hardware.hdmi.IHdmiCecVolumeControlFeatureListener) obj);
                }
            });
        }
    }

    static /* synthetic */ void lambda$announceHdmiCecVolumeControlFeatureChange$2(int hdmiCecVolumeControl, android.hardware.hdmi.IHdmiCecVolumeControlFeatureListener listener) {
        try {
            listener.onHdmiCecVolumeControlFeature(hdmiCecVolumeControl);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to report HdmiControlVolumeControlStatusChange: " + hdmiCecVolumeControl);
        }
    }

    public com.android.server.hdmi.HdmiCecLocalDeviceTv tv() {
        return (com.android.server.hdmi.HdmiCecLocalDeviceTv) this.mHdmiCecNetwork.getLocalDevice(0);
    }

    boolean isTvDevice() {
        return this.mCecLocalDevices.contains(0);
    }

    boolean isAudioSystemDevice() {
        return this.mCecLocalDevices.contains(5);
    }

    boolean isPlaybackDevice() {
        return this.mCecLocalDevices.contains(4);
    }

    boolean isSwitchDevice() {
        return ((java.lang.Boolean) android.sysprop.HdmiProperties.is_switch().orElse(false)).booleanValue();
    }

    boolean isTvDeviceEnabled() {
        return isTvDevice() && tv() != null;
    }

    protected com.android.server.hdmi.HdmiCecLocalDevicePlayback playback() {
        return (com.android.server.hdmi.HdmiCecLocalDevicePlayback) this.mHdmiCecNetwork.getLocalDevice(4);
    }

    public com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem audioSystem() {
        return (com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem) this.mHdmiCecNetwork.getLocalDevice(5);
    }

    com.android.server.hdmi.AudioManagerWrapper getAudioManager() {
        return this.mAudioManager;
    }

    private com.android.server.hdmi.AudioDeviceVolumeManagerWrapper getAudioDeviceVolumeManager() {
        return this.mAudioDeviceVolumeManager;
    }

    boolean isCecControlEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = true;
            if (this.mHdmiControlEnabled != 1) {
                z = false;
            }
        }
        return z;
    }

    public boolean isEarcEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mEarcEnabled;
        }
        return z;
    }

    protected boolean isEarcSupported() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mEarcSupported;
        }
        return z;
    }

    private boolean isDsmEnabled() {
        return this.mHdmiCecConfig.getIntValue("soundbar_mode") == 1;
    }

    protected boolean isArcSupported() {
        return android.os.SystemProperties.getBoolean("persist.sys.hdmi.property_arc_support", true);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    int getPowerStatus() {
        assertRunOnServiceThread();
        return this.mPowerStatusController.getPowerStatus();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void setPowerStatus(int powerStatus) {
        assertRunOnServiceThread();
        this.mPowerStatusController.setPowerStatus(powerStatus);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    boolean isPowerOnOrTransient() {
        assertRunOnServiceThread();
        return this.mPowerStatusController.isPowerStatusOn() || this.mPowerStatusController.isPowerStatusTransientToOn();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    boolean isPowerStandbyOrTransient() {
        assertRunOnServiceThread();
        return this.mPowerStatusController.isPowerStatusStandby() || this.mPowerStatusController.isPowerStatusTransientToStandby();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    boolean isPowerStandby() {
        assertRunOnServiceThread();
        return this.mPowerStatusController.isPowerStatusStandby();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void wakeUp() {
        assertRunOnServiceThread();
        this.mWakeUpMessageReceived = true;
        this.mPowerManager.wakeUp(android.os.SystemClock.uptimeMillis(), 8, "android.server.hdmi:WAKE");
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void standby() {
        assertRunOnServiceThread();
        if (!canGoToStandby()) {
            return;
        }
        this.mStandbyMessageReceived = true;
        this.mPowerManager.goToSleep(android.os.SystemClock.uptimeMillis(), 5, 0);
    }

    boolean isWakeUpMessageReceived() {
        return this.mWakeUpMessageReceived;
    }

    protected boolean isStandbyMessageReceived() {
        return this.mStandbyMessageReceived;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void onWakeUp(int wakeUpAction) {
        int startReason;
        int startReason2;
        assertRunOnServiceThread();
        this.mPowerStatusController.setPowerStatus(2, false);
        if (this.mCecController != null) {
            if (isCecControlEnabled()) {
                switch (wakeUpAction) {
                    case 0:
                        startReason2 = 2;
                        if (this.mWakeUpMessageReceived) {
                            startReason2 = 3;
                        }
                        break;
                    case 1:
                        startReason2 = 1;
                        break;
                    default:
                        android.util.Slog.e(TAG, "wakeUpAction " + wakeUpAction + " not defined.");
                        return;
                }
                initializeCec(startReason2);
            }
        } else {
            android.util.Slog.i(TAG, "Device does not support HDMI-CEC.");
        }
        if (isEarcSupported()) {
            if (isEarcEnabled()) {
                switch (wakeUpAction) {
                    case 0:
                        startReason = 2;
                        break;
                    case 1:
                        startReason = 1;
                        break;
                    default:
                        android.util.Slog.e(TAG, "wakeUpAction " + wakeUpAction + " not defined.");
                        return;
                }
                initializeEarc(startReason);
            } else {
                setEarcEnabledInHal(false, false);
            }
        }
        if (isTvDevice()) {
            int earcStatus = getEarcStatus();
            getAtomWriter().earcStatusChanged(isEarcSupported(), isEarcEnabled(), earcStatus, earcStatus, 1);
        } else if (isPlaybackDevice()) {
            getAtomWriter().dsmStatusChanged(isArcSupported(), isDsmEnabled(), 1);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void onStandby(final int standbyAction) {
        if (shouldAcquireWakeLockOnStandby()) {
            acquireWakeLock();
        }
        this.mWakeUpMessageReceived = false;
        assertRunOnServiceThread();
        this.mPowerStatusController.setPowerStatus(3, false);
        invokeVendorCommandListenersOnControlStateChanged(false, 3);
        final java.util.List<com.android.server.hdmi.HdmiCecLocalDevice> devices = getAllCecLocalDevices();
        if (!isStandbyMessageReceived() && !canGoToStandby()) {
            this.mPowerStatusController.setPowerStatus(1);
            for (com.android.server.hdmi.HdmiCecLocalDevice device : devices) {
                device.onStandby(this.mStandbyMessageReceived, standbyAction);
            }
            return;
        }
        disableCecLocalDevices(new com.android.server.hdmi.HdmiCecLocalDevice.PendingActionClearedCallback() { // from class: com.android.server.hdmi.HdmiControlService.27
            @Override // com.android.server.hdmi.HdmiCecLocalDevice.PendingActionClearedCallback
            public void onCleared(com.android.server.hdmi.HdmiCecLocalDevice device2) {
                android.util.Slog.v(com.android.server.hdmi.HdmiControlService.TAG, "On standby-action cleared:" + device2.mDeviceType);
                devices.remove(device2);
                if (devices.isEmpty()) {
                    com.android.server.hdmi.HdmiControlService.this.onPendingActionsCleared(standbyAction);
                }
            }
        });
        checkAndUpdateAbsoluteVolumeBehavior();
    }

    boolean canGoToStandby() {
        for (com.android.server.hdmi.HdmiCecLocalDevice device : this.mHdmiCecNetwork.getLocalDeviceList()) {
            if (!device.canGoToStandby()) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void onLanguageChanged(java.lang.String language) {
        assertRunOnServiceThread();
        this.mMenuLanguage = language;
        if (isTvDeviceEnabled()) {
            tv().broadcastMenuLanguage(language);
            this.mCecController.setLanguage(language);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    java.lang.String getLanguage() {
        assertRunOnServiceThread();
        return this.mMenuLanguage;
    }

    protected void disableCecLocalDevices(com.android.server.hdmi.HdmiCecLocalDevice.PendingActionClearedCallback callback) {
        if (this.mCecController != null) {
            for (com.android.server.hdmi.HdmiCecLocalDevice device : this.mHdmiCecNetwork.getLocalDeviceList()) {
                device.disableDevice(this.mStandbyMessageReceived, callback);
            }
        }
        this.mMhlController.clearAllLocalDevices();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void clearCecLocalDevices() {
        assertRunOnServiceThread();
        if (this.mCecController == null) {
            return;
        }
        this.mCecController.clearLogicalAddress();
        this.mHdmiCecNetwork.clearLocalDevices();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void onPendingActionsCleared(int standbyAction) {
        assertRunOnServiceThread();
        android.util.Slog.v(TAG, "onPendingActionsCleared");
        final int localDevicesCount = getAllCecLocalDevices().size();
        final int[] countStandbyCompletedDevices = new int[1];
        com.android.server.hdmi.HdmiCecLocalDevice.StandbyCompletedCallback callback = new com.android.server.hdmi.HdmiCecLocalDevice.StandbyCompletedCallback() { // from class: com.android.server.hdmi.HdmiControlService.28
            @Override // com.android.server.hdmi.HdmiCecLocalDevice.StandbyCompletedCallback
            public void onStandbyCompleted() {
                int i = localDevicesCount;
                int[] iArr = countStandbyCompletedDevices;
                int i2 = iArr[0] + 1;
                iArr[0] = i2;
                if (i < i2) {
                    return;
                }
                com.android.server.hdmi.HdmiControlService.this.releaseWakeLock();
                if (com.android.server.hdmi.HdmiControlService.this.isAudioSystemDevice() || !com.android.server.hdmi.HdmiControlService.this.isPowerStandby()) {
                    return;
                }
                com.android.server.hdmi.HdmiControlService.this.mCecController.enableSystemCecControl(false);
                com.android.server.hdmi.HdmiControlService.this.mMhlController.setOption(104, 0);
            }
        };
        if (this.mPowerStatusController.isPowerStatusTransientToStandby()) {
            this.mPowerStatusController.setPowerStatus(1);
            for (com.android.server.hdmi.HdmiCecLocalDevice device : this.mHdmiCecNetwork.getLocalDeviceList()) {
                device.onStandby(this.mStandbyMessageReceived, standbyAction, callback);
            }
        }
        this.mStandbyMessageReceived = false;
    }

    private boolean shouldAcquireWakeLockOnStandby() {
        boolean sendStandbyOnSleep = false;
        if (tv() != null) {
            sendStandbyOnSleep = this.mHdmiCecConfig.getIntValue("tv_send_standby_on_sleep") == 1;
        } else if (playback() != null) {
            sendStandbyOnSleep = !this.mHdmiCecConfig.getStringValue("power_control_mode").equals("none");
        }
        return isCecControlEnabled() && isPowerOnOrTransient() && sendStandbyOnSleep;
    }

    protected void acquireWakeLock() {
        releaseWakeLock();
        this.mWakeLock = this.mPowerManager.newWakeLock(1, TAG);
        this.mWakeLock.acquire(5000L);
    }

    protected void releaseWakeLock() {
        if (this.mWakeLock != null) {
            try {
                if (this.mWakeLock.isHeld()) {
                    this.mWakeLock.release();
                }
            } catch (java.lang.RuntimeException e) {
                android.util.Slog.w(TAG, "Exception when releasing wake lock.");
            }
            this.mWakeLock = null;
        }
    }

    void addVendorCommandListener(android.hardware.hdmi.IHdmiVendorCommandListener listener, int vendorId) {
        com.android.server.hdmi.HdmiControlService.VendorCommandListenerRecord record = new com.android.server.hdmi.HdmiControlService.VendorCommandListenerRecord(listener, vendorId);
        try {
            listener.asBinder().linkToDeath(record, 0);
            synchronized (this.mLock) {
                this.mVendorCommandListenerRecords.add(record);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Listener already died");
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(9:11|(3:13|(1:31)|30)|27|16|17|20|32|30|9) */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0047, code lost:
    
        r5 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0048, code lost:
    
        android.util.Slog.e(com.android.server.hdmi.HdmiControlService.TAG, "Failed to notify vendor command reception", r5);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean invokeVendorCommandListenersOnReceived(int r9, int r10, int r11, byte[] r12, boolean r13) {
        /*
            r8 = this;
            java.lang.Object r0 = r8.mLock
            monitor-enter(r0)
            java.util.ArrayList<com.android.server.hdmi.HdmiControlService$VendorCommandListenerRecord> r1 = r8.mVendorCommandListenerRecords     // Catch: java.lang.Throwable -> L52
            boolean r1 = r1.isEmpty()     // Catch: java.lang.Throwable -> L52
            r2 = 0
            if (r1 == 0) goto Le
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L52
            return r2
        Le:
            r1 = 0
            java.util.ArrayList<com.android.server.hdmi.HdmiControlService$VendorCommandListenerRecord> r3 = r8.mVendorCommandListenerRecords     // Catch: java.lang.Throwable -> L52
            java.util.Iterator r3 = r3.iterator()     // Catch: java.lang.Throwable -> L52
        L15:
            boolean r4 = r3.hasNext()     // Catch: java.lang.Throwable -> L52
            if (r4 == 0) goto L50
            java.lang.Object r4 = r3.next()     // Catch: java.lang.Throwable -> L52
            com.android.server.hdmi.HdmiControlService$VendorCommandListenerRecord r4 = (com.android.server.hdmi.HdmiControlService.VendorCommandListenerRecord) r4     // Catch: java.lang.Throwable -> L52
            if (r13 == 0) goto L3e
            r5 = r12[r2]     // Catch: java.lang.Throwable -> L52
            r5 = r5 & 255(0xff, float:3.57E-43)
            int r5 = r5 << 16
            r6 = 1
            r6 = r12[r6]     // Catch: java.lang.Throwable -> L52
            r6 = r6 & 255(0xff, float:3.57E-43)
            int r6 = r6 << 8
            int r5 = r5 + r6
            r6 = 2
            r6 = r12[r6]     // Catch: java.lang.Throwable -> L52
            r6 = r6 & 255(0xff, float:3.57E-43)
            int r5 = r5 + r6
            int r6 = com.android.server.hdmi.HdmiControlService.VendorCommandListenerRecord.m4171$$Nest$fgetmVendorId(r4)     // Catch: java.lang.Throwable -> L52
            if (r6 == r5) goto L3e
            goto L15
        L3e:
            android.hardware.hdmi.IHdmiVendorCommandListener r5 = com.android.server.hdmi.HdmiControlService.VendorCommandListenerRecord.m4170$$Nest$fgetmListener(r4)     // Catch: android.os.RemoteException -> L47 java.lang.Throwable -> L52
            r5.onReceived(r10, r11, r12, r13)     // Catch: android.os.RemoteException -> L47 java.lang.Throwable -> L52
            r1 = 1
            goto L4f
        L47:
            r5 = move-exception
            java.lang.String r6 = "HdmiControlService"
            java.lang.String r7 = "Failed to notify vendor command reception"
            android.util.Slog.e(r6, r7, r5)     // Catch: java.lang.Throwable -> L52
        L4f:
            goto L15
        L50:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L52
            return r1
        L52:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L52
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.hdmi.HdmiControlService.invokeVendorCommandListenersOnReceived(int, int, int, byte[], boolean):boolean");
    }

    boolean invokeVendorCommandListenersOnControlStateChanged(boolean enabled, int reason) {
        synchronized (this.mLock) {
            if (this.mVendorCommandListenerRecords.isEmpty()) {
                return false;
            }
            for (com.android.server.hdmi.HdmiControlService.VendorCommandListenerRecord record : this.mVendorCommandListenerRecords) {
                try {
                    record.mListener.onControlStateChanged(enabled, reason);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Failed to notify control-state-changed to vendor handler", e);
                }
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addHdmiMhlVendorCommandListener(android.hardware.hdmi.IHdmiMhlVendorCommandListener listener) {
        com.android.server.hdmi.HdmiControlService.HdmiMhlVendorCommandListenerRecord record = new com.android.server.hdmi.HdmiControlService.HdmiMhlVendorCommandListenerRecord(listener);
        try {
            listener.asBinder().linkToDeath(record, 0);
            synchronized (this.mLock) {
                this.mMhlVendorCommandListenerRecords.add(record);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Listener already died.");
        }
    }

    void invokeMhlVendorCommandListeners(int portId, int offest, int length, byte[] data) {
        synchronized (this.mLock) {
            for (com.android.server.hdmi.HdmiControlService.HdmiMhlVendorCommandListenerRecord record : this.mMhlVendorCommandListenerRecords) {
                try {
                    record.mListener.onReceived(portId, offest, length, data);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Failed to notify MHL vendor command", e);
                }
            }
        }
    }

    void setStandbyMode(boolean isStandbyModeOn) {
        assertRunOnServiceThread();
        if (isPowerOnOrTransient() && isStandbyModeOn) {
            this.mPowerManager.goToSleep(android.os.SystemClock.uptimeMillis(), 5, 0);
            if (playback() != null) {
                playback().sendStandby(0);
                return;
            }
            return;
        }
        if (isPowerStandbyOrTransient() && !isStandbyModeOn) {
            this.mPowerManager.wakeUp(android.os.SystemClock.uptimeMillis(), 8, "android.server.hdmi:WAKE");
            if (playback() != null) {
                oneTouchPlay(new android.hardware.hdmi.IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiControlService.29
                    public void onComplete(int result) {
                        if (result != 0) {
                            android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "Failed to complete 'one touch play'. result=" + result);
                        }
                    }
                });
            }
        }
    }

    int getHdmiCecVolumeControl() {
        int i;
        synchronized (this.mLock) {
            i = this.mHdmiCecVolumeControl;
        }
        return i;
    }

    boolean isProhibitMode() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mProhibitMode;
        }
        return z;
    }

    void setProhibitMode(boolean enabled) {
        synchronized (this.mLock) {
            this.mProhibitMode = enabled;
        }
    }

    boolean isSystemAudioActivated() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSystemAudioActivated;
        }
        return z;
    }

    void setSystemAudioActivated(boolean on) {
        synchronized (this.mLock) {
            this.mSystemAudioActivated = on;
        }
        runOnServiceThread(new com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda2(this));
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void setCecEnabled(int enabled) {
        assertRunOnServiceThread();
        synchronized (this.mLock) {
            this.mHdmiControlEnabled = enabled;
        }
        if (enabled == 1) {
            onEnableCec();
            setHdmiCecVolumeControlEnabledInternal(getHdmiCecConfig().getIntValue("volume_control_enabled"));
        } else {
            setHdmiCecVolumeControlEnabledInternal(0);
            invokeVendorCommandListenersOnControlStateChanged(false, 1);
            runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.30
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.hdmi.HdmiControlService.this.onDisableCec();
                }
            });
            announceHdmiControlStatusChange(enabled);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void onEnableCec() {
        this.mCecController.enableCec(true);
        this.mCecController.enableSystemCecControl(true);
        this.mMhlController.setOption(103, 1);
        initializeCec(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void onDisableCec() {
        disableCecLocalDevices(new com.android.server.hdmi.HdmiCecLocalDevice.PendingActionClearedCallback() { // from class: com.android.server.hdmi.HdmiControlService.31
            @Override // com.android.server.hdmi.HdmiCecLocalDevice.PendingActionClearedCallback
            public void onCleared(com.android.server.hdmi.HdmiCecLocalDevice device) {
                com.android.server.hdmi.HdmiControlService.this.assertRunOnServiceThread();
                com.android.server.hdmi.HdmiControlService.this.mCecController.flush(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.31.1
                    @Override // java.lang.Runnable
                    public void run() {
                        com.android.server.hdmi.HdmiControlService.this.mCecController.enableCec(false);
                        com.android.server.hdmi.HdmiControlService.this.mCecController.enableSystemCecControl(false);
                        com.android.server.hdmi.HdmiControlService.this.mMhlController.setOption(103, 0);
                        com.android.server.hdmi.HdmiControlService.this.clearCecLocalDevices();
                    }
                });
            }
        });
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void setActivePortId(int portId) {
        assertRunOnServiceThread();
        this.mActivePortId = portId;
        setLastInputForMhl(-1);
    }

    com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource getLocalActiveSource() {
        com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource activeSource;
        synchronized (this.mLock) {
            activeSource = this.mActiveSource;
        }
        return activeSource;
    }

    void pauseActiveMediaSessions() {
        android.media.session.MediaSessionManager mediaSessionManager = (android.media.session.MediaSessionManager) getContext().getSystemService(android.media.session.MediaSessionManager.class);
        java.util.List<android.media.session.MediaController> mediaControllers = mediaSessionManager.getActiveSessions(null);
        for (android.media.session.MediaController mediaController : mediaControllers) {
            mediaController.getTransportControls().pause();
        }
    }

    void setActiveSource(int logicalAddress, int physicalAddress, java.lang.String caller) {
        synchronized (this.mLock) {
            this.mActiveSource.logicalAddress = logicalAddress;
            this.mActiveSource.physicalAddress = physicalAddress;
        }
        getAtomWriter().activeSourceChanged(logicalAddress, physicalAddress, com.android.server.hdmi.HdmiUtils.pathRelationship(getPhysicalAddress(), physicalAddress));
        for (com.android.server.hdmi.HdmiCecLocalDevice device : getAllCecLocalDevices()) {
            boolean deviceIsActiveSource = logicalAddress == device.getDeviceInfo().getLogicalAddress() && physicalAddress == getPhysicalAddress();
            device.addActiveSourceHistoryItem(new com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource(logicalAddress, physicalAddress), deviceIsActiveSource, caller);
        }
        runOnServiceThread(new com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda2(this));
    }

    protected void setAndBroadcastActiveSource(int physicalAddress, int deviceType, int source, java.lang.String caller) {
        if (deviceType == 4) {
            com.android.server.hdmi.HdmiCecLocalDevicePlayback playback = playback();
            playback.dismissUiOnActiveSourceStatusRecovered();
            playback.setActiveSource(playback.getDeviceInfo().getLogicalAddress(), physicalAddress, caller);
            playback.wakeUpIfActiveSource();
            playback.maySendActiveSource(source);
            playback.mDelayedStandbyOnActiveSourceLostHandler.removeCallbacksAndMessages(null);
        }
        if (deviceType == 5) {
            com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem audioSystem = audioSystem();
            if (playback() == null) {
                audioSystem.setActiveSource(audioSystem.getDeviceInfo().getLogicalAddress(), physicalAddress, caller);
                audioSystem.wakeUpIfActiveSource();
                audioSystem.maySendActiveSource(source);
            }
        }
    }

    protected void setAndBroadcastActiveSourceFromOneDeviceType(int sourceAddress, int physicalAddress, java.lang.String caller) {
        com.android.server.hdmi.HdmiCecLocalDevicePlayback playback = playback();
        com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem audioSystem = audioSystem();
        if (playback != null) {
            playback.setActiveSource(playback.getDeviceInfo().getLogicalAddress(), physicalAddress, caller);
            playback.wakeUpIfActiveSource();
            playback.maySendActiveSource(sourceAddress);
        } else if (audioSystem != null) {
            audioSystem.setActiveSource(audioSystem.getDeviceInfo().getLogicalAddress(), physicalAddress, caller);
            audioSystem.wakeUpIfActiveSource();
            audioSystem.maySendActiveSource(sourceAddress);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void setLastInputForMhl(int portId) {
        assertRunOnServiceThread();
        this.mLastInputMhl = portId;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    int getLastInputForMhl() {
        assertRunOnServiceThread();
        return this.mLastInputMhl;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void changeInputForMhl(int portId, boolean contentOn) {
        assertRunOnServiceThread();
        if (tv() == null) {
            return;
        }
        final int lastInput = contentOn ? tv().getActivePortId() : -1;
        if (portId != -1) {
            tv().doManualPortSwitching(portId, new android.hardware.hdmi.IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiControlService.32
                public void onComplete(int result) throws android.os.RemoteException {
                    com.android.server.hdmi.HdmiControlService.this.setLastInputForMhl(lastInput);
                }
            });
        }
        tv().setActivePortId(portId);
        com.android.server.hdmi.HdmiMhlLocalDeviceStub device = this.mMhlController.getLocalDevice(portId);
        android.hardware.hdmi.HdmiDeviceInfo info = device != null ? device.getInfo() : this.mHdmiCecNetwork.getDeviceForPortId(portId);
        invokeInputChangeListener(info);
    }

    void setMhlInputChangeEnabled(boolean enabled) {
        this.mMhlController.setOption(101, toInt(enabled));
        synchronized (this.mLock) {
            this.mMhlInputChangeEnabled = enabled;
        }
    }

    protected com.android.server.hdmi.HdmiCecAtomWriter getAtomWriter() {
        return this.mAtomWriter;
    }

    boolean isMhlInputChangeEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mMhlInputChangeEnabled;
        }
        return z;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void displayOsd(int messageId) {
        assertRunOnServiceThread();
        android.content.Intent intent = new android.content.Intent("android.hardware.hdmi.action.OSD_MESSAGE");
        intent.putExtra("android.hardware.hdmi.extra.MESSAGE_ID", messageId);
        sendBroadcastAsUser(intent);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void displayOsd(int messageId, int extra) {
        assertRunOnServiceThread();
        android.content.Intent intent = new android.content.Intent("android.hardware.hdmi.action.OSD_MESSAGE");
        intent.putExtra("android.hardware.hdmi.extra.MESSAGE_ID", messageId);
        intent.putExtra("android.hardware.hdmi.extra.MESSAGE_EXTRA_PARAM1", extra);
        sendBroadcastAsUser(intent);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void sendBroadcastAsUser(android.content.Intent intent) {
        assertRunOnServiceThread();
        getContext().sendBroadcastAsUser(intent, android.os.UserHandle.ALL, PERMISSION);
    }

    protected com.android.server.hdmi.HdmiCecConfig getHdmiCecConfig() {
        return this.mHdmiCecConfig;
    }

    /* JADX INFO: renamed from: com.android.server.hdmi.HdmiControlService$33, reason: invalid class name */
    class AnonymousClass33 implements com.android.server.hdmi.HdmiCecConfig.SettingChangeListener {
        AnonymousClass33() {
        }

        @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
        public void onChange(final java.lang.String name) {
            synchronized (com.android.server.hdmi.HdmiControlService.this.mLock) {
                if (com.android.server.hdmi.HdmiControlService.this.mHdmiCecSettingChangeListenerRecords.containsKey(name)) {
                    ((android.os.RemoteCallbackList) com.android.server.hdmi.HdmiControlService.this.mHdmiCecSettingChangeListenerRecords.get(name)).broadcast(new java.util.function.Consumer() { // from class: com.android.server.hdmi.HdmiControlService$33$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$onChange$0(name, (android.hardware.hdmi.IHdmiCecSettingChangeListener) obj);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onChange$0(java.lang.String name, android.hardware.hdmi.IHdmiCecSettingChangeListener listener) {
            com.android.server.hdmi.HdmiControlService.this.invokeCecSettingChangeListenerLocked(name, listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addCecSettingChangeListener(java.lang.String name, android.hardware.hdmi.IHdmiCecSettingChangeListener listener) {
        synchronized (this.mLock) {
            if (!this.mHdmiCecSettingChangeListenerRecords.containsKey(name)) {
                this.mHdmiCecSettingChangeListenerRecords.put(name, new android.os.RemoteCallbackList<>());
                this.mHdmiCecConfig.registerChangeListener(name, this.mSettingChangeListener);
            }
            this.mHdmiCecSettingChangeListenerRecords.get(name).register(listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeCecSettingChangeListener(java.lang.String name, android.hardware.hdmi.IHdmiCecSettingChangeListener listener) {
        synchronized (this.mLock) {
            if (this.mHdmiCecSettingChangeListenerRecords.containsKey(name)) {
                this.mHdmiCecSettingChangeListenerRecords.get(name).unregister(listener);
                if (this.mHdmiCecSettingChangeListenerRecords.get(name).getRegisteredCallbackCount() == 0) {
                    this.mHdmiCecSettingChangeListenerRecords.remove(name);
                    this.mHdmiCecConfig.removeChangeListener(name, this.mSettingChangeListener);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeCecSettingChangeListenerLocked(java.lang.String name, android.hardware.hdmi.IHdmiCecSettingChangeListener listener) {
        try {
            listener.onChange(name);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to report setting change", e);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void onDeviceVolumeBehaviorChanged(android.media.AudioDeviceAttributes device, int volumeBehavior) {
        assertRunOnServiceThread();
        if (AVB_AUDIO_OUTPUT_DEVICES.contains(device)) {
            synchronized (this.mLock) {
                this.mAudioDeviceVolumeBehaviors.put(device, java.lang.Integer.valueOf(volumeBehavior));
            }
            checkAndUpdateAbsoluteVolumeBehavior();
        }
    }

    private int getDeviceVolumeBehavior(android.media.AudioDeviceAttributes device) {
        if (AVB_AUDIO_OUTPUT_DEVICES.contains(device)) {
            synchronized (this.mLock) {
                if (this.mAudioDeviceVolumeBehaviors.containsKey(device)) {
                    return this.mAudioDeviceVolumeBehaviors.get(device).intValue();
                }
            }
        }
        return getAudioManager().getDeviceVolumeBehavior(device);
    }

    public boolean isAbsoluteVolumeBehaviorEnabled() {
        if (!isTvDevice() && !isPlaybackDevice()) {
            return false;
        }
        for (android.media.AudioDeviceAttributes device : getAvbCapableAudioOutputDevices()) {
            if (ABSOLUTE_VOLUME_BEHAVIORS.contains(java.lang.Integer.valueOf(getDeviceVolumeBehavior(device)))) {
                return true;
            }
        }
        return false;
    }

    private java.util.List<android.media.AudioDeviceAttributes> getAvbCapableAudioOutputDevices() {
        if (tv() != null) {
            return TV_AVB_AUDIO_OUTPUT_DEVICES;
        }
        if (playback() != null) {
            return PLAYBACK_AVB_AUDIO_OUTPUT_DEVICES;
        }
        return java.util.Collections.emptyList();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void checkAndUpdateAbsoluteVolumeBehavior() {
        com.android.server.hdmi.HdmiCecLocalDevice localCecDevice;
        assertRunOnServiceThread();
        if (getAudioManager() == null) {
        }
        if (this.mPowerStatusController != null && isPowerStandbyOrTransient()) {
            switchToFullVolumeBehavior();
            return;
        }
        if (isTvDevice() && tv() != null) {
            localCecDevice = tv();
            if (!isSystemAudioActivated()) {
                switchToFullVolumeBehavior();
                return;
            }
        } else if (isPlaybackDevice() && playback() != null) {
            localCecDevice = playback();
        } else {
            return;
        }
        android.hardware.hdmi.HdmiDeviceInfo systemAudioDeviceInfo = getDeviceInfo(localCecDevice.findAudioReceiverAddress());
        int currentVolumeBehavior = getDeviceVolumeBehavior(getAvbCapableAudioOutputDevices().get(0));
        boolean alreadyUsingFullOrAbsoluteVolume = FULL_AND_ABSOLUTE_VOLUME_BEHAVIORS.contains(java.lang.Integer.valueOf(currentVolumeBehavior));
        boolean cecVolumeEnabled = getHdmiCecVolumeControl() == 1;
        if (!cecVolumeEnabled || !alreadyUsingFullOrAbsoluteVolume) {
            switchToFullVolumeBehavior();
            return;
        }
        if (systemAudioDeviceInfo == null) {
            switchToFullVolumeBehavior();
            return;
        }
        switch (systemAudioDeviceInfo.getDeviceFeatures().getSetAudioVolumeLevelSupport()) {
            case 0:
                if (tv() != null && this.mNumericSoundbarVolumeUiOnTvFeatureFlagEnabled) {
                    if (currentVolumeBehavior != 5) {
                        if (currentVolumeBehavior == 3) {
                            for (android.media.AudioDeviceAttributes device : getAvbCapableAudioOutputDevices()) {
                                getAudioManager().setDeviceVolumeBehavior(device, 1);
                            }
                        }
                        localCecDevice.startNewAvbAudioStatusAction(systemAudioDeviceInfo.getLogicalAddress());
                    }
                } else {
                    switchToFullVolumeBehavior();
                }
                break;
            case 1:
                if (currentVolumeBehavior != 3) {
                    localCecDevice.startNewAvbAudioStatusAction(systemAudioDeviceInfo.getLogicalAddress());
                }
                break;
            case 2:
                if (currentVolumeBehavior == 3) {
                    switchToFullVolumeBehavior();
                }
                localCecDevice.querySetAudioVolumeLevelSupport(systemAudioDeviceInfo.getLogicalAddress());
                break;
        }
    }

    private void switchToFullVolumeBehavior() {
        android.util.Slog.d(TAG, "Switching to full volume behavior");
        if (playback() != null) {
            playback().removeAvbAudioStatusAction();
        } else if (tv() != null) {
            tv().removeAvbAudioStatusAction();
        }
        for (android.media.AudioDeviceAttributes device : getAvbCapableAudioOutputDevices()) {
            if (ABSOLUTE_VOLUME_BEHAVIORS.contains(java.lang.Integer.valueOf(getDeviceVolumeBehavior(device)))) {
                getAudioManager().setDeviceVolumeBehavior(device, 1);
            }
        }
    }

    void enableAbsoluteVolumeBehavior(com.android.server.hdmi.AudioStatus audioStatus) {
        com.android.server.hdmi.HdmiCecLocalDevice localDevice = isPlaybackDevice() ? playback() : tv();
        android.hardware.hdmi.HdmiDeviceInfo systemAudioDevice = getDeviceInfo(localDevice.findAudioReceiverAddress());
        android.media.VolumeInfo volumeInfo = new android.media.VolumeInfo.Builder(3).setMuted(audioStatus.getMute()).setVolumeIndex(audioStatus.getVolume()).setMaxVolumeIndex(100).setMinVolumeIndex(0).build();
        this.mAbsoluteVolumeChangedListener = new com.android.server.hdmi.HdmiControlService.AbsoluteVolumeChangedListener(localDevice, systemAudioDevice);
        notifyAvbMuteChange(audioStatus.getMute());
        if (systemAudioDevice.getDeviceFeatures().getSetAudioVolumeLevelSupport() == 1) {
            android.util.Slog.d(TAG, "Enabling absolute volume behavior");
            for (android.media.AudioDeviceAttributes device : getAvbCapableAudioOutputDevices()) {
                getAudioDeviceVolumeManager().setDeviceAbsoluteVolumeBehavior(device, volumeInfo, this.mServiceThreadExecutor, this.mAbsoluteVolumeChangedListener, true);
            }
            return;
        }
        if (tv() != null) {
            android.util.Slog.d(TAG, "Enabling adjust-only absolute volume behavior");
            for (android.media.AudioDeviceAttributes device2 : getAvbCapableAudioOutputDevices()) {
                getAudioDeviceVolumeManager().setDeviceAbsoluteVolumeAdjustOnlyBehavior(device2, volumeInfo, this.mServiceThreadExecutor, this.mAbsoluteVolumeChangedListener, true);
            }
        }
    }

    com.android.server.hdmi.HdmiControlService.AbsoluteVolumeChangedListener getAbsoluteVolumeChangedListener() {
        return this.mAbsoluteVolumeChangedListener;
    }

    class AbsoluteVolumeChangedListener implements android.media.AudioDeviceVolumeManager.OnAudioDeviceVolumeChangedListener {
        private com.android.server.hdmi.HdmiCecLocalDevice mLocalDevice;
        private android.hardware.hdmi.HdmiDeviceInfo mSystemAudioDevice;

        private AbsoluteVolumeChangedListener(com.android.server.hdmi.HdmiCecLocalDevice localDevice, android.hardware.hdmi.HdmiDeviceInfo systemAudioDevice) {
            this.mLocalDevice = localDevice;
            this.mSystemAudioDevice = systemAudioDevice;
        }

        public void onAudioDeviceVolumeChanged(android.media.AudioDeviceAttributes audioDevice, final android.media.VolumeInfo volumeInfo) {
            final int localDeviceAddress = this.mLocalDevice.getDeviceInfo().getLogicalAddress();
            if (this.mSystemAudioDevice.getDeviceFeatures().getSetAudioVolumeLevelSupport() != 1) {
                com.android.server.hdmi.HdmiCecLocalDevice avbDevice = com.android.server.hdmi.HdmiControlService.this.isTvDevice() ? com.android.server.hdmi.HdmiControlService.this.tv() : com.android.server.hdmi.HdmiControlService.this.playback();
                avbDevice.updateAvbVolume(volumeInfo.getVolumeIndex());
                com.android.server.hdmi.HdmiControlService.this.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildGiveAudioStatus(localDeviceAddress, this.mSystemAudioDevice.getLogicalAddress()));
                return;
            }
            com.android.server.hdmi.HdmiControlService.this.sendCecCommand(com.android.server.hdmi.SetAudioVolumeLevelMessage.build(localDeviceAddress, this.mSystemAudioDevice.getLogicalAddress(), volumeInfo.getVolumeIndex()), new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.HdmiControlService$AbsoluteVolumeChangedListener$$ExternalSyntheticLambda0
                @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
                public final void onSendCompleted(int i) {
                    this.f$0.lambda$onAudioDeviceVolumeChanged$0(volumeInfo, localDeviceAddress, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAudioDeviceVolumeChanged$0(android.media.VolumeInfo volumeInfo, int localDeviceAddress, int errorCode) {
            if (errorCode == 0) {
                com.android.server.hdmi.HdmiCecLocalDevice avbDevice = com.android.server.hdmi.HdmiControlService.this.isTvDevice() ? com.android.server.hdmi.HdmiControlService.this.tv() : com.android.server.hdmi.HdmiControlService.this.playback();
                avbDevice.updateAvbVolume(volumeInfo.getVolumeIndex());
            } else {
                com.android.server.hdmi.HdmiControlService.this.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildGiveAudioStatus(localDeviceAddress, this.mSystemAudioDevice.getLogicalAddress()));
            }
        }

        public void onAudioDeviceVolumeAdjusted(android.media.AudioDeviceAttributes audioDevice, android.media.VolumeInfo volumeInfo, int direction, int mode) {
            int keyCode;
            switch (direction) {
                case com.android.server.wm.ITaskExt.SCREEN_ORIENTATION_UNFIXED /* -100 */:
                case 100:
                case 101:
                    keyCode = 164;
                    break;
                case -1:
                    keyCode = 25;
                    break;
                case 0:
                    if (com.android.server.hdmi.HdmiControlService.this.tv() != null) {
                        com.android.server.hdmi.HdmiControlService.this.tv().requestAndUpdateAvbAudioStatus();
                        return;
                    }
                    return;
                case 1:
                    keyCode = 24;
                    break;
                default:
            }
            switch (mode) {
                case 0:
                    this.mLocalDevice.sendVolumeKeyEvent(keyCode, true);
                    this.mLocalDevice.sendVolumeKeyEvent(keyCode, false);
                    break;
                case 1:
                    this.mLocalDevice.sendVolumeKeyEvent(keyCode, true);
                    break;
                case 2:
                    this.mLocalDevice.sendVolumeKeyEvent(keyCode, false);
                    break;
            }
        }
    }

    void notifyAvbVolumeChange(int volume) {
        if (isAbsoluteVolumeBehaviorEnabled()) {
            java.util.List<android.media.AudioDeviceAttributes> streamMusicDevices = getAudioManager().getDevicesForAttributes(STREAM_MUSIC_ATTRIBUTES);
            for (android.media.AudioDeviceAttributes streamMusicDevice : streamMusicDevices) {
                if (getAvbCapableAudioOutputDevices().contains(streamMusicDevice)) {
                    int flags = 8192;
                    if (isTvDevice()) {
                        flags = 8192 | 1;
                    }
                    setStreamMusicVolume(volume, flags);
                    return;
                }
            }
        }
    }

    void notifyAvbMuteChange(boolean mute) {
        if (isAbsoluteVolumeBehaviorEnabled()) {
            java.util.List<android.media.AudioDeviceAttributes> streamMusicDevices = getAudioManager().getDevicesForAttributes(STREAM_MUSIC_ATTRIBUTES);
            for (android.media.AudioDeviceAttributes streamMusicDevice : streamMusicDevices) {
                if (getAvbCapableAudioOutputDevices().contains(streamMusicDevice)) {
                    int direction = mute ? -100 : 100;
                    int flags = 8192;
                    if (isTvDevice()) {
                        flags = 8192 | 1;
                    }
                    getAudioManager().adjustStreamVolume(3, direction, flags);
                    return;
                }
            }
        }
    }

    void setStreamMusicVolume(int volume, int flags) {
        getAudioManager().setStreamVolume(3, (this.mStreamMusicMaxVolume * volume) / 100, flags);
    }

    private void initializeEarc(int initiatedBy) {
        android.util.Slog.i(TAG, "eARC initialized, reason = " + initiatedBy);
        initializeEarcLocalDevice(initiatedBy);
        if (initiatedBy == 6) {
            setEarcEnabledInHal(true, true);
        } else {
            setEarcEnabledInHal(true, false);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void initializeEarcLocalDevice(int initiatedBy) {
        assertRunOnServiceThread();
        if (this.mEarcLocalDevice == null) {
            this.mEarcLocalDevice = com.android.server.hdmi.HdmiEarcLocalDevice.create(this, 0);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void setEarcEnabled(int enabled) {
        assertRunOnServiceThread();
        synchronized (this.mLock) {
            this.mEarcEnabled = enabled == 1;
            if (!isEarcSupported()) {
                android.util.Slog.i(TAG, "Enabled/disabled eARC setting, but the hardware doesn´t support eARC. This settings change doesn´t have an effect.");
            } else if (this.mEarcEnabled) {
                onEnableEarc();
            } else {
                runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.34
                    @Override // java.lang.Runnable
                    public void run() {
                        com.android.server.hdmi.HdmiControlService.this.onDisableEarc();
                    }
                });
            }
        }
    }

    protected void setEarcSupported(boolean supported) {
        synchronized (this.mLock) {
            this.mEarcSupported = supported;
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void onEnableEarc() {
        initializeEarc(6);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void onDisableEarc() {
        disableEarcLocalDevice();
        setEarcEnabledInHal(false, false);
        clearEarcLocalDevice();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void clearEarcLocalDevice() {
        assertRunOnServiceThread();
        this.mEarcLocalDevice = null;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void addEarcLocalDevice(com.android.server.hdmi.HdmiEarcLocalDevice localDevice) {
        assertRunOnServiceThread();
        this.mEarcLocalDevice = localDevice;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private int getEarcStatus() {
        int i;
        assertRunOnServiceThread();
        if (this.mEarcLocalDevice != null) {
            synchronized (this.mLock) {
                i = this.mEarcLocalDevice.mEarcStatus;
            }
            return i;
        }
        return -1;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    com.android.server.hdmi.HdmiEarcLocalDevice getEarcLocalDevice() {
        assertRunOnServiceThread();
        return this.mEarcLocalDevice;
    }

    private void disableEarcLocalDevice() {
        if (this.mEarcLocalDevice == null) {
            return;
        }
        this.mEarcLocalDevice.disableDevice();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void setEarcEnabledInHal(final boolean z, boolean z2) {
        assertRunOnServiceThread();
        if (z2) {
            startArcAction(false, new android.hardware.hdmi.IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiControlService.35
                public void onComplete(int result) throws android.os.RemoteException {
                    if (result != 0) {
                        android.util.Slog.w(com.android.server.hdmi.HdmiControlService.TAG, "ARC termination before enabling eARC in the HAL failed with result: " + result);
                    }
                    com.android.server.hdmi.HdmiControlService.this.mEarcController.setEarcEnabled(z);
                    com.android.server.hdmi.HdmiControlService.this.mCecController.setHpdSignalType(z ? 1 : 0, com.android.server.hdmi.HdmiControlService.this.mEarcPortId);
                }
            });
            return;
        }
        this.mEarcController.setEarcEnabled(z);
        this.mCecController.setHpdSignalType(z ? 1 : 0, this.mEarcPortId);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void handleEarcStateChange(int status, int portId) {
        assertRunOnServiceThread();
        int oldEarcStatus = getEarcStatus();
        if (!getPortInfo(portId).isEarcSupported()) {
            android.util.Slog.w(TAG, "Tried to update eARC status on a port that doesn't support eARC.");
            getAtomWriter().earcStatusChanged(isEarcSupported(), isEarcEnabled(), oldEarcStatus, status, 3);
            return;
        }
        if (this.mEarcLocalDevice != null) {
            this.mEarcLocalDevice.handleEarcStateChange(status);
            getAtomWriter().earcStatusChanged(isEarcSupported(), isEarcEnabled(), oldEarcStatus, status, 2);
        } else {
            if (status == 2) {
                com.android.server.hdmi.HdmiLogger.debug("eARC state change [new: HDMI_EARC_STATUS_ARC_PENDING(2)]", new java.lang.Object[0]);
                notifyEarcStatusToAudioService(false, new java.util.ArrayList());
                this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiControlService.36
                    @Override // java.lang.Runnable
                    public void run() {
                        com.android.server.hdmi.HdmiControlService.this.startArcAction(true, null);
                    }
                }, 500L);
                getAtomWriter().earcStatusChanged(isEarcSupported(), isEarcEnabled(), oldEarcStatus, status, 2);
                return;
            }
            getAtomWriter().earcStatusChanged(isEarcSupported(), isEarcEnabled(), oldEarcStatus, status, 4);
        }
    }

    protected void notifyEarcStatusToAudioService(boolean z, java.util.List<android.media.AudioDescriptor> list) {
        android.media.AudioDeviceAttributes audioDeviceAttributes = new android.media.AudioDeviceAttributes(2, 29, "", "", new java.util.ArrayList(), list);
        if (!isCecControlEnabled()) {
            setSystemAudioActivated(true);
        }
        getAudioManager().setWiredDeviceConnectionState(audioDeviceAttributes, z ? 1 : 0);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void handleEarcCapabilitiesReported(byte[] rawCapabilities, int portId) {
        assertRunOnServiceThread();
        if (!getPortInfo(portId).isEarcSupported()) {
            android.util.Slog.w(TAG, "Tried to process eARC capabilities from a port that doesn't support eARC.");
        } else if (this.mEarcLocalDevice != null) {
            this.mEarcLocalDevice.handleEarcCapabilitiesReported(rawCapabilities);
        }
    }

    protected boolean earcBlocksArcConnection() {
        boolean z;
        if (this.mEarcLocalDevice == null) {
            return false;
        }
        synchronized (this.mLock) {
            z = this.mEarcLocalDevice.mEarcStatus != 2;
        }
        return z;
    }

    protected void startArcAction(boolean enabled, android.hardware.hdmi.IHdmiControlCallback callback) {
        if (!isTvDeviceEnabled()) {
            invokeCallback(callback, 6);
        } else {
            tv().startArcAction(enabled, callback);
        }
    }
}
