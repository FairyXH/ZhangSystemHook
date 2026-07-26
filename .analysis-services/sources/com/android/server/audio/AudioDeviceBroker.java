package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class AudioDeviceBroker {
    private static final long BROKER_WAKELOCK_TIMEOUT_MS = 5000;
    static final int BTA2DP_DOCK_TIMEOUT_MS = 8000;
    private static final int BTA2DP_MUTE_CHECK_DELAY_MS = 100;
    private static final int BTA2DP_MUTE_CHECK_DELAY_MS_INCREASE = 400;
    private static final int BTA2DP_MUTE_MSG_DELAY_MS_INCREASE = 160;
    private static final int BT_CONFIG_UPDATE_RETRY_DELAY_CNT = 10;
    private static final int BT_CONFIG_UPDATE_RETRY_DELAY_MS = 40;
    private static final int BT_DEVICE_CONNECTED_EVENT_DELAY_MS = 200;
    static final int BT_HEADSET_CNCT_TIMEOUT_MS = 3000;
    private static final int CHECK_CLIENT_STATE_DELAY_MS = 6000;
    private static final int MSG_BROADCAST_AUDIO_BECOMING_NOISY = 12;
    private static final int MSG_BT_HEADSET_CNCT_FAILED = 9;
    private static final int MSG_CHECK_COMMUNICATION_ROUTE_CLIENT_STATE = 56;
    private static final int MSG_CHECK_MUTE_MUSIC = 35;
    private static final int MSG_IIL_BTLEAUDIO_TIMEOUT = 49;
    private static final int MSG_IIL_SET_FORCE_USE = 4;
    private static final int MSG_II_SET_HEARING_AID_VOLUME = 14;
    private static final int MSG_II_SET_LE_AUDIO_OUT_VOLUME = 46;
    private static final int MSG_IL_BTA2DP_TIMEOUT = 10;
    private static final int MSG_IL_BT_SERVICE_CONNECTED_PROFILE = 23;
    private static final int MSG_IL_SAVE_NDEF_DEVICE_FOR_STRATEGY = 47;
    private static final int MSG_IL_SAVE_PREF_DEVICES_FOR_CAPTURE_PRESET = 37;
    private static final int MSG_IL_SAVE_PREF_DEVICES_FOR_STRATEGY = 32;
    private static final int MSG_IL_SAVE_REMOVE_NDEF_DEVICE_FOR_STRATEGY = 48;
    private static final int MSG_IL_SET_PREF_DEVICES_FOR_STRATEGY = 113;
    private static final int MSG_IL_UPDATED_ADI_DEVICE_STATE = 59;
    private static final int MSG_IL_UPDATE_COMMUNICATION_ROUTE_CLIENT = 43;
    private static final int MSG_I_BLE_CG_STATE_CHANGE = 131;
    private static final int MSG_I_BROADCAST_BT_CONNECTION_STATE = 3;
    private static final int MSG_I_BT_CONFIG_UPDATE_RETRY = 125;
    private static final int MSG_I_BT_DEVICE_CONNECTED_EVENT = 78;
    private static final int MSG_I_BT_SERVICE_DISCONNECTED_PROFILE = 22;
    private static final int MSG_I_SAVE_CLEAR_PREF_DEVICES_FOR_CAPTURE_PRESET = 38;
    private static final int MSG_I_SAVE_REMOVE_PREF_DEVICES_FOR_STRATEGY = 33;
    private static final int MSG_I_SET_AVRCP_ABSOLUTE_VOLUME = 15;
    private static final int MSG_I_SET_MODE_OWNER = 16;
    private static final int MSG_I_SET_RESTART_SCO_AUDIO = 114;
    private static final int MSG_I_UPDATE_LE_AUDIO_GROUP_ADDRESSES = 57;
    private static final int MSG_L_A2DP_ACTIVE_DEVICE_CHANGE_EXT = 64;
    private static final int MSG_L_A2DP_DEVICE_CONFIG_CHANGE_SHO = 77;
    private static final int MSG_L_A2DP_DEVICE_CONNECTION_CHANGE_EXT = 29;
    private static final int MSG_L_BLUETOOTH_DEVICE_CONFIG_CHANGE = 11;
    private static final int MSG_L_BT_ACTIVE_DEVICE_CHANGE_EXT = 45;
    private static final int MSG_L_CHECK_COMMUNICATION_DEVICE_REMOVAL = 53;
    private static final int MSG_L_COMMUNICATION_ROUTE_CLIENT_DIED = 34;
    private static final int MSG_L_HEARING_AID_DEVICE_CONNECTION_CHANGE_EXT = 31;
    private static final int MSG_L_NOTIFY_PREFERRED_AUDIOPROFILE_APPLIED = 52;
    private static final int MSG_L_RECEIVED_BT_EVENT = 55;
    private static final int MSG_L_SET_BT_ACTIVE_DEVICE = 7;
    private static final int MSG_L_SET_COMMUNICATION_DEVICE_FOR_CLIENT = 42;
    private static final int MSG_L_SET_FORCE_BT_A2DP_USE = 5;
    private static final int MSG_L_SET_FORCE_BT_A2DP_USE_NO_MUTE = 60;
    private static final int MSG_L_SET_WIRED_DEVICE_CONNECTION_STATE = 2;
    private static final int MSG_L_SYNCHRONIZE_ADI_DEVICES_IN_INVENTORY = 58;
    private static final int MSG_L_UPDATE_COMMUNICATION_ROUTE = 39;
    private static final int MSG_PERSIST_AUDIO_DEVICE_SETTINGS = 54;
    private static final int MSG_PERSIST_AUDIO_HEADPHONE_SPAT_ENABLED = 130;
    private static final int MSG_REPORT_NEW_ROUTES = 13;
    private static final int MSG_REPORT_NEW_ROUTES_A2DP = 36;
    private static final int MSG_RESTORE_DEVICES = 1;
    private static final int MSG_TOGGLE_HDMI = 6;
    private static final java.lang.String NAME_HEADPHONE_SPAT_ENABLED = "headphone_spat_enabled";
    private static final int SENDMSG_NOOP = 1;
    private static final int SENDMSG_QUEUE = 2;
    private static final int SENDMSG_REPLACE = 0;
    private static final long SET_COMMUNICATION_DEVICE_TIMEOUT_MS = 3000;
    private static final java.lang.String TAG = "AS.AudioDeviceBroker";
    public static final long USE_SET_COMMUNICATION_DEVICE = 243827847;
    private static final int VALUE_HEADPHONE_SPAT_OFF = 0;
    private static final int VALUE_HEADPHONE_SPAT_ON = 1;
    private int mAccessibilityStrategyId;
    android.media.AudioDeviceInfo mActiveCommunicationDevice;
    private com.android.server.audio.IAudioDeviceBrokerExt mAdbExt;
    private com.android.server.audio.AudioDeviceBroker.AudioDeviceBrokerWrapper mAdbWrapper;
    private com.android.server.audio.AudioDeviceBroker.AudioModeInfo mAudioModeOwner;
    private final com.android.server.audio.AudioService mAudioService;
    private final com.android.server.audio.AudioSystemAdapter mAudioSystem;
    private java.util.concurrent.atomic.AtomicBoolean mBluetoothA2dpEnabled;
    private boolean mBluetoothA2dpSuspendedApplied;
    private boolean mBluetoothA2dpSuspendedExt;
    private boolean mBluetoothA2dpSuspendedInt;
    private final java.lang.Object mBluetoothAudioStateLock;
    private boolean mBluetoothLeSuspendedApplied;
    private boolean mBluetoothLeSuspendedExt;
    private boolean mBluetoothLeSuspendedInt;
    private boolean mBluetoothScoOn;
    private boolean mBluetoothScoOnApplied;
    private android.os.PowerManager.WakeLock mBrokerEventWakeLock;
    private com.android.server.audio.AudioDeviceBroker.BrokerHandler mBrokerHandler;
    private com.android.server.audio.AudioDeviceBroker.BrokerThread mBrokerThread;
    private final com.android.server.audio.BtHelper mBtHelper;
    final android.os.RemoteCallbackList<android.media.ICommunicationDeviceDispatcher> mCommDevDispatchers;
    private final java.lang.Object mCommunicationDeviceLock;
    private int mCommunicationDeviceUpdateCount;
    private final java.util.LinkedList<com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient> mCommunicationRouteClients;
    int mCommunicationStrategyId;
    private final android.content.Context mContext;
    private android.media.AudioDeviceAttributes mCurCommunicationDevice;
    int mCurCommunicationPortId;
    private final com.android.server.audio.AudioDeviceInventory mDeviceInventory;
    private final java.lang.Object mDeviceStateLock;
    private java.util.concurrent.atomic.AtomicBoolean mMusicMuted;
    private int mOldStreamType;
    private android.media.AudioDeviceAttributes mPreCommunicationDevice;
    private android.media.AudioDeviceAttributes mPreferredCommunicationDevice;
    private java.util.List<java.lang.Integer> mSampleRateHD;
    private java.util.List<java.lang.Integer> mSampleRateNormal;
    private final boolean mScoManagedByAudio;
    final java.lang.Object mSetModeLock;
    private final com.android.server.audio.SystemServerAdapter mSystemServer;
    private static final java.lang.Object sLastDeviceConnectionMsgTimeLock = new java.lang.Object();
    private static long sLastDeviceConnectMsgTime = 0;
    private static final int[] VALID_COMMUNICATION_DEVICE_TYPES = {2, 7, 3, 22, 1, 4, 23, 26, 11, 27, 5, 9, 19, 20};
    private static final java.util.Set<java.lang.Integer> MESSAGES_MUTE_MUSIC = new java.util.HashSet();

    static {
        MESSAGES_MUTE_MUSIC.add(7);
        MESSAGES_MUTE_MUSIC.add(11);
        MESSAGES_MUTE_MUSIC.add(45);
        MESSAGES_MUTE_MUSIC.add(77);
        MESSAGES_MUTE_MUSIC.add(29);
        MESSAGES_MUTE_MUSIC.add(5);
        boolean isMtk = android.os.Build.isMtkPlatform();
        if (isMtk) {
            MESSAGES_MUTE_MUSIC.add(2);
        }
        MESSAGES_MUTE_MUSIC.add(131);
    }

    static final class AudioModeInfo {
        final int mMode;
        final int mPid;
        final int mUid;

        AudioModeInfo(int mode, int pid, int uid) {
            this.mMode = mode;
            this.mPid = pid;
            this.mUid = uid;
        }

        public java.lang.String toString() {
            return "AudioModeInfo: mMode=" + android.media.AudioSystem.modeToString(this.mMode) + ", mPid=" + this.mPid + ", mUid=" + this.mUid;
        }
    }

    boolean isScoManagedByAudio() {
        return this.mScoManagedByAudio;
    }

    AudioDeviceBroker(android.content.Context context, com.android.server.audio.AudioService service, com.android.server.audio.AudioSystemAdapter audioSystem) {
        this.mCommunicationStrategyId = -1;
        this.mAccessibilityStrategyId = -1;
        this.mDeviceStateLock = new java.lang.Object();
        boolean z = false;
        this.mBluetoothA2dpEnabled = new java.util.concurrent.atomic.AtomicBoolean(false);
        this.mOldStreamType = -1;
        this.mSetModeLock = new java.lang.Object();
        this.mAudioModeOwner = new com.android.server.audio.AudioDeviceBroker.AudioModeInfo(0, 0, 0);
        this.mAdbWrapper = new com.android.server.audio.AudioDeviceBroker.AudioDeviceBrokerWrapper();
        this.mAdbExt = (com.android.server.audio.IAudioDeviceBrokerExt) system.ext.loader.core.ExtLoader.type(com.android.server.audio.IAudioDeviceBrokerExt.class).base(this).create();
        this.mCommunicationDeviceLock = new java.lang.Object();
        this.mCommunicationDeviceUpdateCount = 0;
        this.mBluetoothAudioStateLock = new java.lang.Object();
        this.mCommDevDispatchers = new android.os.RemoteCallbackList<>();
        this.mCurCommunicationPortId = -1;
        this.mMusicMuted = new java.util.concurrent.atomic.AtomicBoolean(false);
        this.mCommunicationRouteClients = new java.util.LinkedList<>();
        this.mSampleRateNormal = new java.util.ArrayList();
        this.mSampleRateHD = new java.util.ArrayList();
        this.mContext = context;
        this.mAudioService = service;
        this.mBtHelper = new com.android.server.audio.BtHelper(this, context);
        this.mDeviceInventory = new com.android.server.audio.AudioDeviceInventory(this);
        this.mSystemServer = com.android.server.audio.SystemServerAdapter.getDefaultAdapter(this.mContext);
        this.mAudioSystem = audioSystem;
        if (android.media.audio.Flags.scoManagedByAudio() && ((java.lang.Boolean) android.sysprop.BluetoothProperties.isScoManagedByAudioEnabled().orElse(false)).booleanValue()) {
            z = true;
        }
        this.mScoManagedByAudio = z;
        init();
        this.mAdbExt.initAdbExtInner(context, this);
    }

    AudioDeviceBroker(android.content.Context context, com.android.server.audio.AudioService service, com.android.server.audio.AudioDeviceInventory mockDeviceInventory, com.android.server.audio.SystemServerAdapter mockSystemServer, com.android.server.audio.AudioSystemAdapter audioSystem) {
        this.mCommunicationStrategyId = -1;
        this.mAccessibilityStrategyId = -1;
        this.mDeviceStateLock = new java.lang.Object();
        boolean z = false;
        this.mBluetoothA2dpEnabled = new java.util.concurrent.atomic.AtomicBoolean(false);
        this.mOldStreamType = -1;
        this.mSetModeLock = new java.lang.Object();
        this.mAudioModeOwner = new com.android.server.audio.AudioDeviceBroker.AudioModeInfo(0, 0, 0);
        this.mAdbWrapper = new com.android.server.audio.AudioDeviceBroker.AudioDeviceBrokerWrapper();
        this.mAdbExt = (com.android.server.audio.IAudioDeviceBrokerExt) system.ext.loader.core.ExtLoader.type(com.android.server.audio.IAudioDeviceBrokerExt.class).base(this).create();
        this.mCommunicationDeviceLock = new java.lang.Object();
        this.mCommunicationDeviceUpdateCount = 0;
        this.mBluetoothAudioStateLock = new java.lang.Object();
        this.mCommDevDispatchers = new android.os.RemoteCallbackList<>();
        this.mCurCommunicationPortId = -1;
        this.mMusicMuted = new java.util.concurrent.atomic.AtomicBoolean(false);
        this.mCommunicationRouteClients = new java.util.LinkedList<>();
        this.mSampleRateNormal = new java.util.ArrayList();
        this.mSampleRateHD = new java.util.ArrayList();
        this.mContext = context;
        this.mAudioService = service;
        this.mBtHelper = new com.android.server.audio.BtHelper(this, context);
        this.mDeviceInventory = mockDeviceInventory;
        this.mSystemServer = mockSystemServer;
        this.mAudioSystem = audioSystem;
        if (android.media.audio.Flags.scoManagedByAudio() && ((java.lang.Boolean) android.sysprop.BluetoothProperties.isScoManagedByAudioEnabled().orElse(false)).booleanValue()) {
            z = true;
        }
        this.mScoManagedByAudio = z;
        init();
        this.mAdbExt.initAdbExtInner(context, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initRoutingStrategyIds() {
        java.util.List<android.media.audiopolicy.AudioProductStrategy> strategies = android.media.audiopolicy.AudioProductStrategy.getAudioProductStrategies();
        this.mCommunicationStrategyId = -1;
        this.mAccessibilityStrategyId = -1;
        for (android.media.audiopolicy.AudioProductStrategy strategy : strategies) {
            if (this.mCommunicationStrategyId == -1 && strategy.getAudioAttributesForLegacyStreamType(0) != null) {
                this.mCommunicationStrategyId = strategy.getId();
            }
            if (this.mAccessibilityStrategyId == -1 && strategy.getAudioAttributesForLegacyStreamType(10) != null) {
                this.mAccessibilityStrategyId = strategy.getId();
            }
        }
    }

    private void init() {
        setupMessaging(this.mContext);
        initAudioHalBluetoothState();
        initRoutingStrategyIds();
        this.mPreferredCommunicationDevice = null;
        updateActiveCommunicationDevice();
        this.mAdbExt.oplusHeadsetFadeInstantiate(this.mContext, this.mAudioService);
        this.mSystemServer.registerUserStartedReceiver(this.mContext);
        if (this.mAudioService.getWrapper().getExtImpl().getAudioEffectCombined()) {
            android.media.AudioSystem.setParameters("headphone_spat_enabled=" + getWrapper().getAudioHeadPhoneStateFromSettings());
        }
    }

    android.content.Context getContext() {
        return this.mContext;
    }

    void onSystemReady() {
        synchronized (this.mSetModeLock) {
            synchronized (this.mDeviceStateLock) {
                this.mAudioModeOwner = this.mAudioService.getAudioModeOwner();
                this.mBtHelper.onSystemReady();
            }
        }
    }

    int getAudioModeOwnerMode() {
        return this.mAudioModeOwner.mMode;
    }

    void onAudioServerDied() {
        sendMsgNoDelay(1, 0);
    }

    void setForceUse_Async(int useCase, int config, java.lang.String eventSource) {
        sendIILMsgNoDelay(4, 2, useCase, config, eventSource);
    }

    void toggleHdmiIfConnected_Async() {
        sendMsgNoDelay(6, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onReceiveBtEvent(android.content.Intent intent) {
        this.mBtHelper.onReceiveBtEvent(intent);
    }

    void onSetBtScoActiveDevice(android.bluetooth.BluetoothDevice btDevice) {
        this.mBtHelper.onSetBtScoActiveDevice(btDevice);
    }

    void setBluetoothA2dpOn_Async(boolean on, java.lang.String source) {
        boolean wasOn = this.mBluetoothA2dpEnabled.getAndSet(on);
        sendLMsgNoDelay(wasOn == on ? 60 : 5, 0, source);
    }

    void setSpeakerphoneOn(android.os.IBinder cb, int uid, boolean on, boolean isPrivileged, java.lang.String eventSource) {
        if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
            android.util.Log.v(TAG, "setSpeakerphoneOn, on: " + on + " uid: " + uid);
        }
        postSetCommunicationDeviceForClient(new com.android.server.audio.AudioDeviceBroker.CommunicationDeviceInfo(cb, uid, new android.media.AudioDeviceAttributes(2, ""), on, -1, eventSource, isPrivileged));
    }

    private boolean hasCommunicationRouteClientForUidInt(int uid, int deviceType) {
        android.media.AudioDeviceAttributes device;
        for (com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient cl : this.mCommunicationRouteClients) {
            if (cl.getUid() == uid && (device = cl.getDevice()) != null && device.getType() == deviceType) {
                return true;
            }
        }
        return false;
    }

    private void removeCommunicationRouteClientsForUidInt(android.os.IBinder exceptcb, int uid, int deviceType) {
        android.media.AudioDeviceAttributes device;
        java.util.Iterator<com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient> i = this.mCommunicationRouteClients.iterator();
        while (i.hasNext()) {
            com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient cl = i.next();
            if (cl.getUid() == uid && cl.getBinder() != exceptcb && (device = cl.getDevice()) != null && device.getType() == deviceType) {
                android.util.Log.d(TAG, "removeSpeakerClientsForUid uid: " + uid + " deviceType: " + deviceType);
                cl.unregisterDeathRecipient();
                i.remove();
            }
        }
    }

    boolean hasCommunicationRouteClientForUid(int uid, int deviceType) {
        boolean zHasCommunicationRouteClientForUidInt;
        synchronized (this.mDeviceStateLock) {
            zHasCommunicationRouteClientForUidInt = hasCommunicationRouteClientForUidInt(uid, deviceType);
        }
        return zHasCommunicationRouteClientForUidInt;
    }

    android.os.IBinder getCommunicationRouteBluetoothScoCbForUid(int uid) {
        synchronized (this.mDeviceStateLock) {
            com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient cl = getCommunicationRouteClientForUid(uid);
            android.media.AudioDeviceAttributes device = cl.getDevice();
            if (cl == null || device == null || device.getType() != 7) {
                return null;
            }
            return cl.getBinder();
        }
    }

    boolean setCommunicationDevice(android.os.IBinder cb, int uid, android.media.AudioDeviceInfo device, boolean isPrivileged, java.lang.String eventSource) {
        android.media.AudioDeviceAttributes audioDeviceAttributes;
        if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
            android.util.Log.v(TAG, "setCommunicationDevice, device: " + device + ", uid: " + uid);
        }
        synchronized (this.mCommunicationDeviceLock) {
            if (device == null) {
                audioDeviceAttributes = null;
            } else {
                try {
                    audioDeviceAttributes = new android.media.AudioDeviceAttributes(device);
                } catch (java.lang.Throwable th) {
                    throw th;
                }
            }
            android.media.AudioDeviceAttributes deviceAttr = audioDeviceAttributes;
            com.android.server.audio.AudioDeviceBroker.CommunicationDeviceInfo deviceInfo = new com.android.server.audio.AudioDeviceBroker.CommunicationDeviceInfo(cb, uid, deviceAttr, device != null, -1, eventSource, isPrivileged);
            postSetCommunicationDeviceForClient(deviceInfo);
            this.mCommunicationDeviceUpdateCount++;
        }
        return true;
    }

    void onSetCommunicationDeviceForClient(com.android.server.audio.AudioDeviceBroker.CommunicationDeviceInfo deviceInfo) {
        if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
            android.util.Log.v(TAG, "onSetCommunicationDeviceForClient: " + deviceInfo);
        }
        if (!deviceInfo.mOn) {
            com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient client = getCommunicationRouteClientForUid(deviceInfo.mUid);
            if (client != null) {
                if (deviceInfo.mDevice == null || deviceInfo.mDevice.equals(client.getDevice()) || deviceInfo.mDevice.getType() == 2) {
                    if (deviceInfo.mDevice != null && deviceInfo.mDevice.getType() == 2 && client.getDevice().getType() != 2) {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
        android.media.AudioDeviceAttributes device = deviceInfo.mOn ? deviceInfo.mDevice : null;
        if (!deviceInfo.mEventSource.equals(com.android.server.audio.IAudioDeviceBrokerExt.REMOVE_INACTIVE_ROUTE_CLIENT)) {
            this.mAdbExt.addAudioRouteEventTrack(deviceInfo.mUid, 1, -1, device != null ? device.getType() : 0);
        }
        if (!deviceInfo.mEventSource.equals("startBluetoothSco()") && this.mAudioService.getWrapper().getExtImpl().isAudioRouteSupported()) {
            this.mAudioService.getWrapper().getExtImpl().selectRouteSetting(2, 1, 0, null);
        }
        setCommunicationRouteForClient(deviceInfo.mCb, deviceInfo.mUid, device, deviceInfo.mScoAudioMode, deviceInfo.mIsPrivileged, deviceInfo.mEventSource);
        if (!deviceInfo.mEventSource.equals("startBluetoothSco()") && this.mAudioService.getWrapper().getExtImpl().isAudioRouteSupported()) {
            this.mAudioService.getWrapper().getExtImpl().selectRouteSetting(2, 0, 0, null);
        }
    }

    void setCommunicationRouteForClient(android.os.IBinder cb, int uid, android.media.AudioDeviceAttributes device, int scoAudioMode, boolean isPrivileged, java.lang.String eventSource) {
        com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient client;
        if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
            android.util.Log.v(TAG, "setCommunicationRouteForClient: device: " + device + ", eventSource: " + eventSource);
        }
        com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("setCommunicationRouteForClient for uid: " + uid + " device: " + device + " isPrivileged: " + isPrivileged + " from API: " + eventSource).printLog(TAG));
        boolean wasBtScoRequested = isBluetoothScoRequested();
        android.media.AudioDeviceAttributes prevClientDevice = null;
        boolean prevPrivileged = false;
        com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient client2 = getCommunicationRouteClientForUid(uid);
        if (client2 != null) {
            prevClientDevice = client2.getDevice();
            prevPrivileged = client2.isPrivileged();
        }
        if (device != null) {
            if (this.mAudioService.getWrapper().getExtImpl().isAudioRouteSupported()) {
                getWrapper().clearRedundancyClient(uid, cb);
            }
            client = addCommunicationRouteClient(cb, uid, device, isPrivileged);
            if (client == null) {
                android.util.Log.w(TAG, "setCommunicationRouteForClient: could not add client for uid: " + uid + " and device: " + device);
            }
        } else {
            client = removeCommunicationRouteClient(cb, true);
        }
        if (client == null) {
            return;
        }
        if (!this.mScoManagedByAudio) {
            boolean isBtScoRequested = isBluetoothScoRequested();
            boolean isBluetoothScoOnNegate = true;
            if (android.os.Build.isMtkPlatform()) {
                boolean isBluetoothScoOn = this.mBtHelper.isBluetoothScoOn();
                isBluetoothScoOnNegate = !isBluetoothScoOn;
            }
            if (isBtScoRequested && ((!wasBtScoRequested || !isBluetoothScoActive()) && isBluetoothScoOnNegate)) {
                if (!this.mBtHelper.startBluetoothSco(scoAudioMode, eventSource)) {
                    android.util.Log.w(TAG, "setCommunicationRouteForClient: failure to start BT SCO for uid: " + uid);
                    if (prevClientDevice == null) {
                        removeCommunicationRouteClient(cb, true);
                    } else {
                        addCommunicationRouteClient(cb, uid, prevClientDevice, prevPrivileged);
                    }
                    postBroadcastScoConnectionState(0);
                }
            } else if (isBtScoRequested && wasBtScoRequested && !this.mBtHelper.isBluetoothScoOn()) {
                android.util.Log.w(TAG, "setCommunicationRouteForClient: when isBtScoRequested &: wasBtScoRequestedare true and BT SCO is off" + uid);
                if (!this.mBtHelper.startBluetoothSco(scoAudioMode, eventSource)) {
                    android.util.Log.w(TAG, "setCommunicationRouteForClient: failure to start BT SCO for uid: " + uid);
                    if (prevClientDevice == null) {
                        removeCommunicationRouteClient(cb, true);
                    } else {
                        addCommunicationRouteClient(cb, uid, prevClientDevice, prevPrivileged);
                    }
                    postBroadcastScoConnectionState(0);
                }
            } else if (!isBtScoRequested && wasBtScoRequested) {
                this.mBtHelper.stopBluetoothSco(eventSource);
            }
        }
        if (isBluetoothLeAudioRequested() && device != null) {
            if (getWrapper().getBluetoothVolSyncSupported() && device != null && (device.getInternalType() == 536870912 || device.getInternalType() == 536870913)) {
                if (isLeConnected()) {
                    int streamType = this.mAudioService.getBluetoothContextualVolumeStream();
                    if (streamType == 0) {
                        streamType = 6;
                    }
                    int leAudioVolIndex = getVssVolumeForDevice(streamType, device.getInternalType());
                    int leAudioMaxVolIndex = getMaxVssVolumeForStream(streamType);
                    if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
                        android.util.Log.v(TAG, "setCommunicationRouteForClient restoring LE Audio device volume lvl.");
                    }
                    postSetLeAudioVolumeIndex(leAudioVolIndex, leAudioMaxVolIndex, streamType);
                }
            } else {
                int streamType2 = this.mAudioService.getBluetoothContextualVolumeStream();
                int leAudioVolIndex2 = getVssVolumeForDevice(streamType2, device.getInternalType());
                int leAudioMaxVolIndex2 = getMaxVssVolumeForStream(streamType2);
                if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
                    android.util.Log.v(TAG, "setCommunicationRouteForClient restoring LE Audio device volume lvl.");
                }
                postSetLeAudioVolumeIndex(leAudioVolIndex2, leAudioMaxVolIndex2, streamType2);
            }
        }
        updateCommunicationRoute(eventSource);
    }

    private com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient topCommunicationRouteClient() {
        for (com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient crc : this.mCommunicationRouteClients) {
            if (crc.getUid() == this.mAudioModeOwner.mUid) {
                return crc;
            }
        }
        if (this.mCommunicationRouteClients.isEmpty() || this.mAudioModeOwner.mUid != 0) {
            return null;
        }
        if (!this.mAudioService.getWrapper().getExtImpl().isAudioRouteSupported() || ((android.os.Build.isMtkPlatform() && this.mCommunicationRouteClients.get(0).getDevice().getType() == 26) || !this.mAudioService.getWrapper().getExtImpl().selectRouteSetting(1, this.mCommunicationRouteClients.get(0).getUid(), 0, null))) {
            return this.mCommunicationRouteClients.get(0);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.media.AudioDeviceAttributes requestedCommunicationDevice() {
        com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient crc = topCommunicationRouteClient();
        android.media.AudioDeviceAttributes device = crc != null ? crc.getDevice() : null;
        if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
            android.util.Log.v(TAG, "requestedCommunicationDevice: " + device + " mAudioModeOwner: " + this.mAudioModeOwner.toString());
        }
        return device;
    }

    static boolean isValidCommunicationDevice(android.media.AudioDeviceInfo device) {
        java.util.Objects.requireNonNull(device, "device must not be null");
        return device.isSink() && isValidCommunicationDeviceType(device.getType());
    }

    private static boolean isValidCommunicationDeviceType(int deviceType) {
        for (int type : VALID_COMMUNICATION_DEVICE_TYPES) {
            if (deviceType == type) {
                return true;
            }
        }
        return false;
    }

    void postCheckCommunicationDeviceRemoval(android.media.AudioDeviceAttributes device) {
        if (!isValidCommunicationDeviceType(android.media.AudioDeviceInfo.convertInternalDeviceToDeviceType(device.getInternalType()))) {
            return;
        }
        sendLMsgNoDelay(53, 2, device);
    }

    void onCheckCommunicationDeviceRemoval(android.media.AudioDeviceAttributes device) {
        if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
            android.util.Log.v(TAG, "onCheckCommunicationDeviceRemoval device: " + device.toString());
        }
        for (com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient crc : this.mCommunicationRouteClients) {
            if (device.equals(crc.getDevice())) {
                if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
                    android.util.Log.v(TAG, "onCheckCommunicationDeviceRemoval removing client: " + crc.toString());
                }
                com.android.server.audio.AudioDeviceBroker.CommunicationDeviceInfo deviceInfo = new com.android.server.audio.AudioDeviceBroker.CommunicationDeviceInfo(crc.getBinder(), crc.getUid(), device, false, -1, "onCheckCommunicationDeviceRemoval", crc.isPrivileged());
                postSetCommunicationDeviceForClient(deviceInfo);
            }
        }
    }

    void postCheckCommunicationRouteClientState(int i, boolean z, int i2) {
        com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient communicationRouteClientForUid = getCommunicationRouteClientForUid(i);
        if (communicationRouteClientForUid != null) {
            sendMsgForCheckClientState(56, 0, i, z ? 1 : 0, communicationRouteClientForUid, i2);
        }
    }

    void onCheckCommunicationRouteClientState(int uid, boolean wasActive) {
        com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient client = getCommunicationRouteClientForUid(uid);
        if (client == null) {
            return;
        }
        updateCommunicationRouteClientState(client, wasActive);
    }

    void updateCommunicationRouteClientState(com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient client, boolean wasActive) {
        boolean wasBtScoRequested = isBluetoothScoRequested();
        client.setPlaybackActive(this.mAudioService.isPlaybackActiveForUid(client.getUid()));
        client.setRecordingActive(this.mAudioService.isRecordingActiveForUid(client.getUid()));
        if (wasActive != client.isActive()) {
            postUpdateCommunicationRouteClient(wasBtScoRequested, "updateCommunicationRouteClientState");
        }
    }

    void setForceCommunicationClientStateAndDelayedCheck(com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient client, boolean forcePlaybackActive, boolean forceRecordingActive) {
        if (client == null) {
            return;
        }
        if (forcePlaybackActive) {
            client.setPlaybackActive(true);
        }
        if (forceRecordingActive) {
            client.setRecordingActive(true);
        }
        postCheckCommunicationRouteClientState(client.getUid(), client.isActive(), 6000);
    }

    static java.util.List<android.media.AudioDeviceInfo> getAvailableCommunicationDevices() {
        java.util.ArrayList<android.media.AudioDeviceInfo> commDevices = new java.util.ArrayList<>();
        android.media.AudioDeviceInfo[] allDevices = android.media.AudioManager.getDevicesStatic(2);
        for (android.media.AudioDeviceInfo device : allDevices) {
            if (isValidCommunicationDevice(device)) {
                commDevices.add(device);
            }
        }
        return commDevices;
    }

    private android.media.AudioDeviceInfo getCommunicationDeviceOfType(final int type) {
        return getAvailableCommunicationDevices().stream().filter(new java.util.function.Predicate() { // from class: com.android.server.audio.AudioDeviceBroker$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.audio.AudioDeviceBroker.lambda$getCommunicationDeviceOfType$0(type, (android.media.AudioDeviceInfo) obj);
            }
        }).findFirst().orElse(null);
    }

    static /* synthetic */ boolean lambda$getCommunicationDeviceOfType$0(int type, android.media.AudioDeviceInfo d) {
        return d.getType() == type;
    }

    android.media.AudioDeviceInfo getCommunicationDevice() {
        android.media.AudioDeviceInfo communicationDeviceInt;
        synchronized (this.mCommunicationDeviceLock) {
            long start = java.lang.System.currentTimeMillis();
            long elapsed = 0;
            while (true) {
                if (this.mCommunicationDeviceUpdateCount <= 0) {
                    break;
                }
                try {
                    this.mCommunicationDeviceLock.wait(3000 - elapsed);
                } catch (java.lang.InterruptedException e) {
                    android.util.Log.w(TAG, "Interrupted while waiting for communication device update.");
                }
                elapsed = java.lang.System.currentTimeMillis() - start;
                if (elapsed >= 3000) {
                    android.util.Log.e(TAG, "Timeout waiting for communication device update.");
                    break;
                }
            }
        }
        synchronized (this.mDeviceStateLock) {
            communicationDeviceInt = getCommunicationDeviceInt();
        }
        return communicationDeviceInt;
    }

    private android.media.AudioDeviceInfo getCommunicationDeviceInt() {
        updateActiveCommunicationDevice();
        android.media.AudioDeviceInfo device = this.mActiveCommunicationDevice;
        if (device != null && device.getType() == 13) {
            device = getCommunicationDeviceOfType(2);
        }
        if (device == null || !isValidCommunicationDevice(device)) {
            android.media.AudioDeviceInfo device2 = getCommunicationDeviceOfType(1);
            if (device2 == null) {
                java.util.List<android.media.AudioDeviceInfo> commDevices = getAvailableCommunicationDevices();
                if (!commDevices.isEmpty()) {
                    return commDevices.get(0);
                }
                return device2;
            }
            return device2;
        }
        return device;
    }

    android.media.AudioDeviceInfo getCommunicationDeviceForClient() {
        android.media.AudioDeviceInfo audioDeviceInfo;
        synchronized (this.mDeviceStateLock) {
            android.media.AudioDeviceAttributes device = requestedCommunicationDevice();
            if (device != null) {
                if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
                    android.util.Log.d(TAG, "getCommunicationDeviceForClient, device=" + android.media.AudioSystem.getOutputDeviceName(device.getInternalType()));
                }
                this.mActiveCommunicationDevice = android.media.AudioManager.getDeviceInfoFromTypeAndAddress(device.getType(), device.getAddress());
            } else {
                if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
                    android.util.Log.d(TAG, "getCommunicationDeviceForClient, updateActiveCommunicationDevice");
                }
                updateActiveCommunicationDevice();
            }
            audioDeviceInfo = this.mActiveCommunicationDevice;
        }
        return audioDeviceInfo;
    }

    void updateActiveCommunicationDevice() {
        android.media.AudioDeviceAttributes device = preferredCommunicationDevice();
        if (device == null) {
            android.media.AudioAttributes attr = android.media.audiopolicy.AudioProductStrategy.getAudioAttributesForStrategyWithLegacyStreamType(0);
            java.util.List<android.media.AudioDeviceAttributes> devices = this.mAudioSystem.getDevicesForAttributes(attr, false);
            if (devices.isEmpty()) {
                if (this.mAudioService.isPlatformVoice()) {
                    android.util.Log.w(TAG, "updateActiveCommunicationDevice(): no device for phone strategy");
                }
                this.mActiveCommunicationDevice = null;
                return;
            }
            device = devices.get(0);
        }
        this.mActiveCommunicationDevice = android.media.AudioManager.getDeviceInfoFromTypeAndAddress(device.getType(), device.getAddress());
    }

    private boolean isDeviceRequestedForCommunication(int deviceType) {
        boolean z;
        synchronized (this.mDeviceStateLock) {
            android.media.AudioDeviceAttributes device = requestedCommunicationDevice();
            z = device != null && device.getType() == deviceType;
        }
        return z;
    }

    private boolean isDeviceOnForCommunication(int deviceType) {
        boolean z;
        synchronized (this.mDeviceStateLock) {
            android.media.AudioDeviceAttributes device = preferredCommunicationDevice();
            z = device != null && device.getType() == deviceType;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDeviceActiveForCommunication(int deviceType) {
        return this.mActiveCommunicationDevice != null && this.mActiveCommunicationDevice.getType() == deviceType && this.mPreferredCommunicationDevice != null && this.mPreferredCommunicationDevice.getType() == deviceType;
    }

    boolean isSpeakerphoneOn() {
        return isDeviceOnForCommunication(2);
    }

    private boolean isSpeakerphoneActive() {
        return isDeviceActiveForCommunication(2);
    }

    boolean isBluetoothScoRequested() {
        return isDeviceRequestedForCommunication(7);
    }

    boolean isBluetoothLeAudioRequested() {
        return isDeviceRequestedForCommunication(26) || isDeviceRequestedForCommunication(27);
    }

    boolean isBluetoothScoOn() {
        return isDeviceOnForCommunication(7) || isDeviceOnForCommunication(26);
    }

    boolean isBluetoothScoActive() {
        return isDeviceActiveForCommunication(7);
    }

    boolean isDeviceConnected(android.media.AudioDeviceAttributes device) {
        boolean zIsDeviceConnected;
        synchronized (this.mDeviceStateLock) {
            zIsDeviceConnected = this.mDeviceInventory.isDeviceConnected(device);
        }
        return zIsDeviceConnected;
    }

    void setWiredDeviceConnectionState(android.media.AudioDeviceAttributes attributes, int state, java.lang.String caller) {
        synchronized (this.mDeviceStateLock) {
            if (android.os.Build.isMtkPlatform()) {
                boolean suppressNoisyIntent = false;
                if (state == 0) {
                    synchronized (this.mDeviceStateLock) {
                        suppressNoisyIntent = this.mBtHelper.isNextBtActiveDeviceAvailableForMusic();
                    }
                }
                this.mDeviceInventory.setWiredDeviceConnectionState(attributes, state, caller, suppressNoisyIntent);
            } else {
                this.mDeviceInventory.setWiredDeviceConnectionState(attributes, state, caller);
            }
        }
    }

    void setWiredDeviceConnectionState(android.media.AudioDeviceAttributes attributes, int state, java.lang.String caller, boolean suppressNoisyIntent) {
        synchronized (this.mDeviceStateLock) {
            this.mDeviceInventory.setWiredDeviceConnectionState(attributes, state, caller, suppressNoisyIntent);
        }
    }

    void setTestDeviceConnectionState(android.media.AudioDeviceAttributes device, int state) {
        synchronized (this.mDeviceStateLock) {
            this.mDeviceInventory.setTestDeviceConnectionState(device, state);
        }
    }

    void restartScoInVoipCall() {
        this.mAudioService.mAsSocExt.restartScoInVoipCall();
    }

    static final class BleVolumeInfo {
        final int mIndex;
        final int mMaxIndex;
        final int mStreamType;

        BleVolumeInfo(int index, int maxIndex, int streamType) {
            this.mIndex = index;
            this.mMaxIndex = maxIndex;
            this.mStreamType = streamType;
        }
    }

    static final class BtDeviceChangedData {
        final java.lang.String mEventSource;
        final android.media.BluetoothProfileConnectionInfo mInfo;
        final android.bluetooth.BluetoothDevice mNewDevice;
        final android.bluetooth.BluetoothDevice mPreviousDevice;

        BtDeviceChangedData(android.bluetooth.BluetoothDevice newDevice, android.bluetooth.BluetoothDevice previousDevice, android.media.BluetoothProfileConnectionInfo info, java.lang.String eventSource) {
            this.mNewDevice = newDevice;
            this.mPreviousDevice = previousDevice;
            this.mInfo = info;
            this.mEventSource = eventSource;
        }

        public java.lang.String toString() {
            return "BtDeviceChangedData profile=" + android.bluetooth.BluetoothProfile.getProfileName(this.mInfo.getProfile()) + ", switch device: [" + this.mPreviousDevice + "] -> [" + this.mNewDevice + "]";
        }
    }

    static final class oplusBtDeviceInfo {
        final android.bluetooth.BluetoothDevice mDevice;
        int mProfile;

        oplusBtDeviceInfo(android.bluetooth.BluetoothDevice device, int profile) {
            this.mDevice = device;
            this.mProfile = profile;
        }
    }

    public com.android.server.audio.AudioDeviceBroker.oplusBtDeviceInfo createOplusBtDeviceInfo(android.bluetooth.BluetoothDevice device, int profile) {
        return new com.android.server.audio.AudioDeviceBroker.oplusBtDeviceInfo(device, profile);
    }

    static final class BtDeviceInfo {
        final int mAudioSystemDevice;
        final android.bluetooth.BluetoothDevice mDevice;
        final java.lang.String mEventSource;
        final boolean mIsLeOutput;
        final int mMusicDevice;
        final int mProfile;
        final int mState;
        final boolean mSupprNoisy;
        final int mVolume;

        BtDeviceInfo(com.android.server.audio.AudioDeviceBroker.BtDeviceChangedData d, android.bluetooth.BluetoothDevice device, int state, int audioDevice, int codec) {
            this.mDevice = device;
            this.mState = state;
            this.mProfile = d.mInfo.getProfile();
            this.mSupprNoisy = d.mInfo.isSuppressNoisyIntent();
            this.mVolume = d.mInfo.getVolume();
            this.mIsLeOutput = d.mInfo.isLeOutput();
            this.mEventSource = d.mEventSource;
            this.mAudioSystemDevice = audioDevice;
            this.mMusicDevice = 0;
        }

        BtDeviceInfo(android.bluetooth.BluetoothDevice device, int profile) {
            this.mDevice = device;
            this.mProfile = profile;
            this.mEventSource = "";
            this.mMusicDevice = 0;
            this.mAudioSystemDevice = 0;
            this.mState = 0;
            this.mSupprNoisy = false;
            this.mVolume = -1;
            this.mIsLeOutput = false;
        }

        BtDeviceInfo(android.bluetooth.BluetoothDevice device, int profile, int state, int musicDevice, int audioSystemDevice) {
            this.mDevice = device;
            this.mProfile = profile;
            this.mEventSource = "";
            this.mMusicDevice = musicDevice;
            this.mAudioSystemDevice = audioSystemDevice;
            this.mState = state;
            this.mSupprNoisy = false;
            this.mVolume = -1;
            this.mIsLeOutput = false;
        }

        BtDeviceInfo(com.android.server.audio.AudioDeviceBroker.BtDeviceInfo src, int state) {
            this.mDevice = src.mDevice;
            this.mState = state;
            this.mProfile = src.mProfile;
            this.mSupprNoisy = src.mSupprNoisy;
            this.mVolume = src.mVolume;
            this.mIsLeOutput = src.mIsLeOutput;
            this.mEventSource = src.mEventSource;
            this.mAudioSystemDevice = src.mAudioSystemDevice;
            this.mMusicDevice = src.mMusicDevice;
        }

        public boolean equals(java.lang.Object o) {
            if (o == null) {
                return false;
            }
            if (this == o) {
                return true;
            }
            if (!(o instanceof com.android.server.audio.AudioDeviceBroker.BtDeviceInfo) || this.mProfile != ((com.android.server.audio.AudioDeviceBroker.BtDeviceInfo) o).mProfile || !this.mDevice.equals(((com.android.server.audio.AudioDeviceBroker.BtDeviceInfo) o).mDevice)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.mProfile), this.mDevice);
        }

        public java.lang.String toString() {
            return "BtDeviceInfo: device=" + this.mDevice.toString() + " state=" + this.mState + " prof=" + this.mProfile + " supprNoisy=" + this.mSupprNoisy + " volume=" + this.mVolume + " isLeOutput=" + this.mIsLeOutput + " eventSource=" + this.mEventSource + " audioSystemDevice=" + this.mAudioSystemDevice + " musicDevice=" + this.mMusicDevice;
        }
    }

    com.android.server.audio.AudioDeviceBroker.BtDeviceInfo createBtDeviceInfo(com.android.server.audio.AudioDeviceBroker.BtDeviceChangedData d, android.bluetooth.BluetoothDevice device, int state) {
        int audioDevice;
        switch (d.mInfo.getProfile()) {
            case 1:
                audioDevice = 16;
                break;
            case 2:
                audioDevice = 128;
                break;
            case 11:
                audioDevice = -2147352576;
                break;
            case 21:
                audioDevice = 134217728;
                break;
            case 22:
                if (d.mInfo.isLeOutput()) {
                    audioDevice = 536870912;
                } else {
                    audioDevice = -1610612736;
                }
                break;
            case 26:
                audioDevice = 536870914;
                break;
            default:
                throw new java.lang.IllegalArgumentException("Invalid profile " + d.mInfo.getProfile());
        }
        return new com.android.server.audio.AudioDeviceBroker.BtDeviceInfo(d, device, state, audioDevice, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void btMediaMetricRecord(android.bluetooth.BluetoothDevice device, java.lang.String state, com.android.server.audio.AudioDeviceBroker.BtDeviceChangedData data) {
        java.lang.String name = android.text.TextUtils.emptyIfNull(device.getName());
        new android.media.MediaMetrics.Item("audio.device.queueOnBluetoothActiveDeviceChanged").set(android.media.MediaMetrics.Property.STATE, state).set(android.media.MediaMetrics.Property.STATUS, java.lang.Integer.valueOf(data.mInfo.getProfile())).set(android.media.MediaMetrics.Property.NAME, name).record();
    }

    void queueOnBluetoothActiveDeviceChanged(com.android.server.audio.AudioDeviceBroker.BtDeviceChangedData data) {
        boolean isQcom = android.os.Build.isQcomPlatform();
        if (data.mInfo.getProfile() == 2 && data.mPreviousDevice != null && ((!isQcom && data.mPreviousDevice.equals(data.mNewDevice)) || (isQcom && data.mNewDevice != null))) {
            if (this.mAdbExt != null && !this.mAdbExt.checkPreviousDeviceIsConnected(data.mPreviousDevice, data.mInfo.getProfile())) {
                sendILMsg(125, 2, 10, data, 40);
                android.util.Log.d(TAG, "mPreviousDevice not connect retry after 40ms");
                return;
            }
            java.lang.String name = android.text.TextUtils.emptyIfNull(data.mNewDevice.getName());
            new android.media.MediaMetrics.Item("audio.device.queueOnBluetoothActiveDeviceChanged_update").set(android.media.MediaMetrics.Property.NAME, name).set(android.media.MediaMetrics.Property.STATUS, java.lang.Integer.valueOf(data.mInfo.getProfile())).record();
            synchronized (this.mDeviceStateLock) {
                postBluetoothDeviceConfigChange(createBtDeviceInfo(data, data.mNewDevice, 2));
            }
            return;
        }
        synchronized (this.mDeviceStateLock) {
            if (data.mPreviousDevice != null) {
                btMediaMetricRecord(data.mPreviousDevice, "disconnected", data);
                sendLMsgNoDelay(45, 2, createBtDeviceInfo(data, data.mPreviousDevice, 0));
            }
            if (data.mNewDevice != null) {
                btMediaMetricRecord(data.mNewDevice, "connected", data);
                sendLMsgNoDelay(45, 2, createBtDeviceInfo(data, data.mNewDevice, 2));
            }
        }
    }

    void resetBluetoothScoOfApp() {
        if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
            android.util.Log.v(TAG, "resetBluetoothScoOfApp");
        }
        this.mAudioService.resetBluetoothScoOfApp();
    }

    private void initAudioHalBluetoothState() {
        synchronized (this.mBluetoothAudioStateLock) {
            this.mBluetoothScoOnApplied = false;
            this.mBluetoothA2dpSuspendedApplied = false;
            this.mBluetoothLeSuspendedApplied = false;
            reapplyAudioHalBluetoothState();
        }
    }

    private void updateAudioHalBluetoothState() {
        if (this.mBluetoothScoOn != this.mBluetoothScoOnApplied) {
            if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
                android.util.Log.v(TAG, "updateAudioHalBluetoothState() mBluetoothScoOn: " + this.mBluetoothScoOn + ", mBluetoothScoOnApplied: " + this.mBluetoothScoOnApplied);
            }
            if (this.mBluetoothScoOn) {
                if (!this.mBluetoothA2dpSuspendedApplied) {
                    android.media.AudioSystem.setParameters("A2dpSuspended=true");
                    this.mBluetoothA2dpSuspendedApplied = true;
                }
                boolean isLeVoIPOngoing = (android.os.Build.isMtkPlatform() || !this.mAdbExt.isSupportFakeHfp() || this.mBtHelper.isAudioConnected()) ? false : true;
                if (isLeVoIPOngoing) {
                    android.util.Log.v(TAG, "skip set LeAudioSuspended to true when LEA VoIP was ongoing");
                } else if (!this.mBluetoothLeSuspendedApplied) {
                    android.media.AudioSystem.setParameters("LeAudioSuspended=true");
                    this.mBluetoothLeSuspendedApplied = true;
                }
                if (!isLeVoIPOngoing) {
                    android.media.AudioSystem.setParameters("BT_SCO=on");
                }
                this.mAudioService.setBinauralRecordParameters(false);
            } else {
                android.media.AudioSystem.setParameters("BT_SCO=off");
                this.mAudioService.setBinauralRecordParameters(true);
            }
            this.mBluetoothScoOnApplied = this.mBluetoothScoOn;
        }
        if (!this.mBluetoothScoOnApplied) {
            if ((this.mBluetoothA2dpSuspendedExt || this.mBluetoothA2dpSuspendedInt) != this.mBluetoothA2dpSuspendedApplied) {
                if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
                    android.util.Log.v(TAG, "updateAudioHalBluetoothState() mBluetoothA2dpSuspendedExt: " + this.mBluetoothA2dpSuspendedExt + ", mBluetoothA2dpSuspendedInt: " + this.mBluetoothA2dpSuspendedInt + ", mBluetoothA2dpSuspendedApplied: " + this.mBluetoothA2dpSuspendedApplied);
                }
                this.mBluetoothA2dpSuspendedApplied = this.mBluetoothA2dpSuspendedExt || this.mBluetoothA2dpSuspendedInt;
                if (this.mBluetoothA2dpSuspendedApplied) {
                    android.media.AudioSystem.setParameters("A2dpSuspended=true");
                } else {
                    android.media.AudioSystem.setParameters("A2dpSuspended=false");
                }
            }
            if ((this.mBluetoothLeSuspendedExt || this.mBluetoothLeSuspendedInt) != this.mBluetoothLeSuspendedApplied) {
                if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
                    android.util.Log.v(TAG, "updateAudioHalBluetoothState() mBluetoothLeSuspendedExt: " + this.mBluetoothLeSuspendedExt + ", mBluetoothLeSuspendedInt: " + this.mBluetoothLeSuspendedInt + ", mBluetoothLeSuspendedApplied: " + this.mBluetoothLeSuspendedApplied);
                }
                this.mBluetoothLeSuspendedApplied = this.mBluetoothLeSuspendedExt || this.mBluetoothLeSuspendedInt;
                if (this.mBluetoothLeSuspendedApplied) {
                    android.media.AudioSystem.setParameters("LeAudioSuspended=true");
                } else {
                    android.media.AudioSystem.setParameters("LeAudioSuspended=false");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reapplyAudioHalBluetoothState() {
        if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
            android.util.Log.v(TAG, "reapplyAudioHalBluetoothState() mBluetoothScoOnApplied: " + this.mBluetoothScoOnApplied + ", mBluetoothA2dpSuspendedApplied: " + this.mBluetoothA2dpSuspendedApplied + ", mBluetoothLeSuspendedApplied: " + this.mBluetoothLeSuspendedApplied);
        }
        if (this.mBluetoothScoOnApplied) {
            android.media.AudioSystem.setParameters("A2dpSuspended=true");
            android.media.AudioSystem.setParameters("LeAudioSuspended=true");
            android.media.AudioSystem.setParameters("BT_SCO=on");
            return;
        }
        android.media.AudioSystem.setParameters("BT_SCO=off");
        if (this.mBluetoothA2dpSuspendedApplied) {
            android.media.AudioSystem.setParameters("A2dpSuspended=true");
        } else {
            android.media.AudioSystem.setParameters("A2dpSuspended=false");
        }
        if (this.mBluetoothLeSuspendedApplied) {
            android.media.AudioSystem.setParameters("LeAudioSuspended=true");
        } else {
            android.media.AudioSystem.setParameters("LeAudioSuspended=false");
        }
    }

    void setBluetoothScoOn(boolean on, java.lang.String eventSource) {
        if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
            android.util.Log.v(TAG, "setBluetoothScoOn: " + on + " " + eventSource);
        }
        if (!on) {
            this.mAudioService.resetBluetoothScoOfApp();
        }
        boolean isBTScoReq = isBluetoothScoRequested();
        synchronized (this.mBluetoothAudioStateLock) {
            this.mBluetoothScoOn = on;
            if (!android.os.Build.isMtkPlatform() && this.mAdbExt.isSupportFakeHfp() && on && !this.mBtHelper.isAudioConnected()) {
                android.util.Log.v(TAG, "skip updateAudioHalBluetoothState if SCO is not on");
            } else {
                updateAudioHalBluetoothState();
            }
            postUpdateCommunicationRouteClient(isBTScoReq, eventSource);
        }
    }

    void setA2dpSuspended(boolean enable, boolean internal, java.lang.String eventSource) {
        synchronized (this.mBluetoothAudioStateLock) {
            if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
                android.util.Log.v(TAG, "setA2dpSuspended source: " + eventSource + ", enable: " + enable + ", internal: " + internal + ", mBluetoothA2dpSuspendedInt: " + this.mBluetoothA2dpSuspendedInt + ", mBluetoothA2dpSuspendedExt: " + this.mBluetoothA2dpSuspendedExt);
            }
            if (internal) {
                this.mBluetoothA2dpSuspendedInt = enable;
            } else {
                this.mBluetoothA2dpSuspendedExt = enable;
            }
            updateAudioHalBluetoothState();
        }
    }

    void clearA2dpSuspended(boolean internalOnly) {
        if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
            android.util.Log.v(TAG, "clearA2dpSuspended, internalOnly: " + internalOnly);
        }
        synchronized (this.mBluetoothAudioStateLock) {
            this.mBluetoothA2dpSuspendedInt = false;
            if (!internalOnly) {
                this.mBluetoothA2dpSuspendedExt = false;
            }
            updateAudioHalBluetoothState();
        }
    }

    void setLeAudioSuspended(boolean enable, boolean internal, java.lang.String eventSource) {
        synchronized (this.mBluetoothAudioStateLock) {
            if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
                android.util.Log.v(TAG, "setLeAudioSuspended source: " + eventSource + ", enable: " + enable + ", internal: " + internal + ", mBluetoothLeSuspendedInt: " + this.mBluetoothA2dpSuspendedInt + ", mBluetoothLeSuspendedExt: " + this.mBluetoothA2dpSuspendedExt);
            }
            if (internal) {
                this.mBluetoothLeSuspendedInt = enable;
            } else {
                this.mBluetoothLeSuspendedExt = enable;
            }
            updateAudioHalBluetoothState();
        }
    }

    void clearLeAudioSuspended(boolean internalOnly) {
        if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
            android.util.Log.v(TAG, "clearLeAudioSuspended, internalOnly: " + internalOnly);
        }
        synchronized (this.mBluetoothAudioStateLock) {
            this.mBluetoothLeSuspendedInt = false;
            if (!internalOnly) {
                this.mBluetoothLeSuspendedExt = false;
            }
            updateAudioHalBluetoothState();
        }
    }

    android.media.AudioRoutesInfo startWatchingRoutes(android.media.IAudioRoutesObserver observer) {
        android.media.AudioRoutesInfo audioRoutesInfoStartWatchingRoutes;
        synchronized (this.mDeviceStateLock) {
            audioRoutesInfoStartWatchingRoutes = this.mDeviceInventory.startWatchingRoutes(observer);
        }
        return audioRoutesInfoStartWatchingRoutes;
    }

    android.media.AudioRoutesInfo getCurAudioRoutes() {
        android.media.AudioRoutesInfo curAudioRoutes;
        synchronized (this.mDeviceStateLock) {
            curAudioRoutes = this.mDeviceInventory.getCurAudioRoutes();
        }
        return curAudioRoutes;
    }

    boolean isAvrcpAbsoluteVolumeSupported() {
        boolean zIsAvrcpAbsoluteVolumeSupported;
        synchronized (this.mDeviceStateLock) {
            zIsAvrcpAbsoluteVolumeSupported = this.mBtHelper.isAvrcpAbsoluteVolumeSupported();
        }
        return zIsAvrcpAbsoluteVolumeSupported;
    }

    boolean isBluetoothA2dpOn() {
        return this.mBluetoothA2dpEnabled.get();
    }

    void postSetAvrcpAbsoluteVolumeIndex(int index) {
        sendIMsgNoDelay(15, 0, index);
    }

    void postSetHearingAidVolumeIndex(int index, int streamType) {
        sendIIMsgNoDelay(14, 0, index, streamType);
    }

    void postSetLeAudioVolumeIndex(int index, int maxIndex, int streamType) {
        com.android.server.audio.AudioDeviceBroker.BleVolumeInfo info = new com.android.server.audio.AudioDeviceBroker.BleVolumeInfo(index, maxIndex, streamType);
        if (this.mOldStreamType != streamType && getWrapper().getBluetoothVolSyncSupported()) {
            if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
                android.util.Log.v(TAG, "oldStreamType: " + this.mOldStreamType + " streamType:" + streamType);
            }
            sendLMsgNoDelay(46, 2, info);
            this.mOldStreamType = streamType;
            return;
        }
        sendLMsgNoDelay(46, 0, info);
    }

    void postSetModeOwner(int mode, int pid, int uid) {
        sendLMsgNoDelay(16, 0, new com.android.server.audio.AudioDeviceBroker.AudioModeInfo(mode, pid, uid));
    }

    void postBluetoothDeviceConfigChange(com.android.server.audio.AudioDeviceBroker.BtDeviceInfo info) {
        sendLMsgNoDelay(11, 2, info);
    }

    void postBluetoothA2dpDeviceConfigChange(com.android.server.audio.AudioDeviceBroker.BtDeviceInfo info) {
        sendLMsgNoDelay(77, 2, info);
    }

    void startBluetoothScoForClient(android.os.IBinder cb, int uid, int scoAudioMode, boolean isPrivileged, java.lang.String eventSource) {
        if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
            android.util.Log.v(TAG, "startBluetoothScoForClient, uid: " + uid);
        }
        if (!android.os.Build.isMtkPlatform() && this.mDeviceInventory.isLeConnected()) {
            android.media.AudioDeviceInfo[] devices = android.media.AudioManager.getDevicesStatic(2);
            for (android.media.AudioDeviceInfo device : devices) {
                if (device.getType() == 26) {
                    setCommunicationDevice(cb, uid, device, isPrivileged, eventSource);
                    android.util.Log.d(TAG, "startBluetoothLe, uid: " + uid);
                    return;
                }
            }
        }
        postSetCommunicationDeviceForClient(new com.android.server.audio.AudioDeviceBroker.CommunicationDeviceInfo(cb, uid, new android.media.AudioDeviceAttributes(16, ""), true, scoAudioMode, eventSource, isPrivileged));
    }

    void stopBluetoothScoForClient(android.os.IBinder cb, int uid, boolean isPrivileged, java.lang.String eventSource) {
        if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
            android.util.Log.v(TAG, "stopBluetoothScoForClient, uid: " + uid);
        }
        if (!android.os.Build.isMtkPlatform()) {
            boolean isStopBluetoothLe = false;
            synchronized (this.mSetModeLock) {
                synchronized (this.mDeviceStateLock) {
                    com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient client = getCommunicationRouteClientForUid(uid);
                    if (client == null) {
                        return;
                    }
                    android.media.AudioDeviceAttributes device = client.getDevice();
                    if (device != null && device.getType() == 26) {
                        isStopBluetoothLe = true;
                    }
                    if (isStopBluetoothLe) {
                        setCommunicationDevice(cb, uid, null, isPrivileged, eventSource);
                        android.util.Log.d(TAG, "stopBluetoothLe, uid: " + uid);
                        return;
                    }
                }
            }
        }
        postSetCommunicationDeviceForClient(new com.android.server.audio.AudioDeviceBroker.CommunicationDeviceInfo(cb, uid, new android.media.AudioDeviceAttributes(16, ""), false, -1, eventSource, isPrivileged));
    }

    void startBluetoothBleForClient(android.os.IBinder cb, int uid, int scoAudioMode, boolean isPrivileged, java.lang.String eventSource) {
        if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
            android.util.Log.v(TAG, "startBluetoothBleForClient, uid: " + uid);
        }
        postSetCommunicationDeviceForClient(new com.android.server.audio.AudioDeviceBroker.CommunicationDeviceInfo(cb, uid, new android.media.AudioDeviceAttributes(536870912, ""), true, scoAudioMode, eventSource, isPrivileged));
    }

    void stopBluetoothBleForClient(android.os.IBinder cb, int uid, boolean isPrivileged, java.lang.String eventSource) {
        if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
            android.util.Log.v(TAG, "stopBluetoothBleForClient, uid: " + uid);
        }
        postSetCommunicationDeviceForClient(new com.android.server.audio.AudioDeviceBroker.CommunicationDeviceInfo(cb, uid, new android.media.AudioDeviceAttributes(536870912, ""), false, -1, eventSource, isPrivileged));
    }

    int setPreferredDevicesForStrategySync(int strategy, java.util.List<android.media.AudioDeviceAttributes> devices) {
        return this.mDeviceInventory.setPreferredDevicesForStrategyAndSave(strategy, devices);
    }

    int removePreferredDevicesForStrategySync(int strategy) {
        return this.mDeviceInventory.removePreferredDevicesForStrategyAndSave(strategy);
    }

    int setDeviceAsNonDefaultForStrategySync(int strategy, android.media.AudioDeviceAttributes device) {
        return this.mDeviceInventory.setDeviceAsNonDefaultForStrategyAndSave(strategy, device);
    }

    int removeDeviceAsNonDefaultForStrategySync(int strategy, android.media.AudioDeviceAttributes device) {
        return this.mDeviceInventory.removeDeviceAsNonDefaultForStrategyAndSave(strategy, device);
    }

    void registerStrategyPreferredDevicesDispatcher(android.media.IStrategyPreferredDevicesDispatcher dispatcher, boolean isPrivileged) {
        this.mDeviceInventory.registerStrategyPreferredDevicesDispatcher(dispatcher, isPrivileged);
    }

    void unregisterStrategyPreferredDevicesDispatcher(android.media.IStrategyPreferredDevicesDispatcher dispatcher) {
        this.mDeviceInventory.unregisterStrategyPreferredDevicesDispatcher(dispatcher);
    }

    void registerStrategyNonDefaultDevicesDispatcher(android.media.IStrategyNonDefaultDevicesDispatcher dispatcher, boolean isPrivileged) {
        this.mDeviceInventory.registerStrategyNonDefaultDevicesDispatcher(dispatcher, isPrivileged);
    }

    void unregisterStrategyNonDefaultDevicesDispatcher(android.media.IStrategyNonDefaultDevicesDispatcher dispatcher) {
        this.mDeviceInventory.unregisterStrategyNonDefaultDevicesDispatcher(dispatcher);
    }

    int setPreferredDevicesForCapturePresetSync(int capturePreset, java.util.List<android.media.AudioDeviceAttributes> devices) {
        return this.mDeviceInventory.setPreferredDevicesForCapturePresetAndSave(capturePreset, devices);
    }

    int clearPreferredDevicesForCapturePresetSync(int capturePreset) {
        return this.mDeviceInventory.clearPreferredDevicesForCapturePresetAndSave(capturePreset);
    }

    void registerCapturePresetDevicesRoleDispatcher(android.media.ICapturePresetDevicesRoleDispatcher dispatcher, boolean isPrivileged) {
        this.mDeviceInventory.registerCapturePresetDevicesRoleDispatcher(dispatcher, isPrivileged);
    }

    void unregisterCapturePresetDevicesRoleDispatcher(android.media.ICapturePresetDevicesRoleDispatcher dispatcher) {
        this.mDeviceInventory.unregisterCapturePresetDevicesRoleDispatcher(dispatcher);
    }

    java.util.List<android.media.AudioDeviceAttributes> anonymizeAudioDeviceAttributesListUnchecked(java.util.List<android.media.AudioDeviceAttributes> devices) {
        return this.mAudioService.anonymizeAudioDeviceAttributesListUnchecked(devices);
    }

    void registerCommunicationDeviceDispatcher(android.media.ICommunicationDeviceDispatcher dispatcher) {
        this.mCommDevDispatchers.register(dispatcher);
    }

    void unregisterCommunicationDeviceDispatcher(android.media.ICommunicationDeviceDispatcher dispatcher) {
        this.mCommDevDispatchers.unregister(dispatcher);
    }

    private void dispatchCommunicationDevice() {
        android.media.AudioDeviceInfo device = getCommunicationDeviceInt();
        int portId = device != null ? device.getId() : 0;
        if (portId == this.mCurCommunicationPortId) {
            return;
        }
        this.mCurCommunicationPortId = portId;
        int nbDispatchers = this.mCommDevDispatchers.beginBroadcast();
        for (int i = 0; i < nbDispatchers; i++) {
            try {
                this.mCommDevDispatchers.getBroadcastItem(i).dispatchCommunicationDeviceChanged(portId);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "dispatchCommunicationDevice error", e);
            }
        }
        this.mCommDevDispatchers.finishBroadcast();
    }

    void postAccessoryPlugMediaUnmute(int device) {
        this.mAudioService.postAccessoryPlugMediaUnmute(device);
    }

    int getVssVolumeForDevice(int streamType, int device) {
        return this.mAudioService.getVssVolumeForDevice(streamType, device);
    }

    int getMaxVssVolumeForStream(int streamType) {
        return this.mAudioService.getMaxVssVolumeForStream(streamType);
    }

    int getDeviceForStream(int streamType) {
        return this.mAudioService.getDeviceForStream(streamType);
    }

    void postApplyVolumeOnDevice(int streamType, int device, java.lang.String caller) {
        this.mAudioService.postApplyVolumeOnDevice(streamType, device, caller);
    }

    void postSetVolumeIndexOnDevice(int streamType, int vssVolIndex, int device, java.lang.String caller) {
        this.mAudioService.postSetVolumeIndexOnDevice(streamType, vssVolIndex, device, caller);
    }

    void postObserveDevicesForAllStreams() {
        this.mAudioService.postObserveDevicesForAllStreams();
    }

    boolean isInCommunication() {
        return this.mAudioService.isInCommunication();
    }

    boolean hasMediaDynamicPolicy() {
        return this.mAudioService.hasMediaDynamicPolicy();
    }

    android.content.ContentResolver getContentResolver() {
        return this.mAudioService.getContentResolver();
    }

    void checkMusicActive(int deviceType, java.lang.String caller) {
        this.mAudioService.checkMusicActive(deviceType, caller);
    }

    void checkVolumeCecOnHdmiConnection(int state, java.lang.String caller) {
        this.mAudioService.postCheckVolumeCecOnHdmiConnection(state, caller);
    }

    boolean hasAudioFocusUsers() {
        return this.mAudioService.hasAudioFocusUsers();
    }

    void postInitSpatializerHeadTrackingSensors() {
        this.mAudioService.postInitSpatializerHeadTrackingSensors();
    }

    void postBroadcastScoConnectionState(int state) {
        sendIMsgNoDelay(3, 2, state);
    }

    void postBroadcastBecomingNoisy() {
        sendMsgNoDelay(12, 0);
    }

    void postBluetoothActiveDevice(com.android.server.audio.AudioDeviceBroker.BtDeviceInfo info, int delay) {
        sendLMsg(7, 2, info, delay);
    }

    void postSetWiredDeviceConnectionState(com.android.server.audio.AudioDeviceInventory.WiredDeviceConnectionState connectionState, int delay) {
        sendLMsg(2, 2, connectionState, delay);
    }

    void postBtProfileDisconnected(int profile) {
        sendIMsgNoDelay(22, 2, profile);
    }

    void postBtProfileConnected(int profile, android.bluetooth.BluetoothProfile proxy) {
        sendILMsgNoDelay(23, 2, profile, proxy);
    }

    void postCommunicationRouteClientDied(com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient client) {
        sendLMsgNoDelay(34, 2, client);
    }

    void postSaveSetPreferredDevicesForStrategy(int strategy, java.util.List<android.media.AudioDeviceAttributes> devices) {
        sendILMsgNoDelay(32, 2, strategy, devices);
    }

    void postSaveRemovePreferredDevicesForStrategy(int strategy) {
        sendIMsgNoDelay(33, 2, strategy);
    }

    void postSaveSetDeviceAsNonDefaultForStrategy(int strategy, android.media.AudioDeviceAttributes device) {
        sendILMsgNoDelay(47, 2, strategy, device);
    }

    void postSaveRemoveDeviceAsNonDefaultForStrategy(int strategy, android.media.AudioDeviceAttributes device) {
        sendILMsgNoDelay(48, 2, strategy, device);
    }

    void postSaveSetPreferredDevicesForCapturePreset(int capturePreset, java.util.List<android.media.AudioDeviceAttributes> devices) {
        sendILMsgNoDelay(37, 2, capturePreset, devices);
    }

    void postSaveClearPreferredDevicesForCapturePreset(int capturePreset) {
        sendIMsgNoDelay(38, 2, capturePreset);
    }

    void postBtDeviceConnectedEvent(android.bluetooth.BluetoothDevice BtDevice, int delay) {
        sendLMsg(78, 0, BtDevice, delay);
    }

    void postUpdateCommunicationRouteClient(boolean z, java.lang.String str) {
        sendILMsgNoDelay(43, 2, z ? 1 : 0, str);
    }

    void postUpdateCommunicationRoute(java.lang.String eventSource) {
        sendLMsgNoDelay(39, 2, eventSource);
    }

    void postSetCommunicationDeviceForClient(com.android.server.audio.AudioDeviceBroker.CommunicationDeviceInfo info) {
        sendLMsgNoDelay(42, 2, info);
    }

    void postNotifyPreferredAudioProfileApplied(android.bluetooth.BluetoothDevice btDevice) {
        sendLMsgNoDelay(52, 2, btDevice);
    }

    void postReceiveBtEvent(android.content.Intent intent) {
        sendLMsgNoDelay(55, 2, intent);
    }

    void postUpdateLeAudioGroupAddresses(int groupId) {
        sendIMsgNoDelay(57, 2, groupId);
    }

    void postSynchronizeAdiDevicesInInventory(com.android.server.audio.AdiDeviceState deviceState) {
        sendLMsgNoDelay(58, 2, deviceState);
    }

    void postUpdatedAdiDeviceState(com.android.server.audio.AdiDeviceState adiDeviceState, boolean z) {
        sendILMsgNoDelay(59, 2, z ? 1 : 0, adiDeviceState);
    }

    void postBleCgStateChange(int isCgOn, int delay) {
        sendIILMsg(131, 2, isCgOn, 0, null, delay);
    }

    static final class CommunicationDeviceInfo {
        final android.os.IBinder mCb;
        final android.media.AudioDeviceAttributes mDevice;
        final java.lang.String mEventSource;
        final boolean mIsPrivileged;
        final boolean mOn;
        final int mScoAudioMode;
        final int mUid;

        CommunicationDeviceInfo(android.os.IBinder cb, int uid, android.media.AudioDeviceAttributes device, boolean on, int scoAudioMode, java.lang.String eventSource, boolean isPrivileged) {
            this.mCb = cb;
            this.mUid = uid;
            this.mDevice = device;
            this.mOn = on;
            this.mScoAudioMode = scoAudioMode;
            this.mIsPrivileged = isPrivileged;
            this.mEventSource = eventSource;
        }

        public boolean equals(java.lang.Object o) {
            if (o == null) {
                return false;
            }
            if (this == o) {
                return true;
            }
            if (!(o instanceof com.android.server.audio.AudioDeviceBroker.CommunicationDeviceInfo) || !this.mCb.equals(((com.android.server.audio.AudioDeviceBroker.CommunicationDeviceInfo) o).mCb) || this.mUid != ((com.android.server.audio.AudioDeviceBroker.CommunicationDeviceInfo) o).mUid) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.mCb.hashCode()), java.lang.Integer.valueOf(this.mUid));
        }

        public java.lang.String toString() {
            return "CommunicationDeviceInfo mCb=" + this.mCb.toString() + " mUid=" + this.mUid + " mDevice=[" + (this.mDevice != null ? this.mDevice.toString() : "null") + "] mOn=" + this.mOn + " mScoAudioMode=" + this.mScoAudioMode + " mIsPrivileged=" + this.mIsPrivileged + " mEventSource=" + this.mEventSource;
        }
    }

    void setBluetoothA2dpOnInt(boolean on, boolean fromA2dp, java.lang.String source) {
        java.lang.String eventSource = "setBluetoothA2dpOn(" + on + ") from u/pid:" + android.os.Binder.getCallingUid() + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + android.os.Binder.getCallingPid() + " src:" + source;
        this.mBluetoothA2dpEnabled.set(on);
        onSetForceUse(1, on ? 0 : 10, fromA2dp, eventSource);
    }

    boolean handleDeviceConnection(android.media.AudioDeviceAttributes attributes, boolean connect, android.bluetooth.BluetoothDevice btDevice) {
        boolean zHandleDeviceConnection;
        synchronized (this.mDeviceStateLock) {
            zHandleDeviceConnection = this.mDeviceInventory.handleDeviceConnection(attributes, connect, false, btDevice);
        }
        return zHandleDeviceConnection;
    }

    void handleFailureToConnectToBtHeadsetService(int delay) {
        sendMsg(9, 0, delay);
    }

    void handleCancelFailureToConnectToBtHeadsetService() {
        this.mBrokerHandler.removeMessages(9);
    }

    void postReportNewRoutes(boolean fromA2dp) {
        sendMsgNoDelay(fromA2dp ? 36 : 13, 1);
    }

    boolean hasScheduledA2dpConnection(android.bluetooth.BluetoothDevice btDevice) {
        com.android.server.audio.AudioDeviceBroker.BtDeviceInfo devInfoToCheck = new com.android.server.audio.AudioDeviceBroker.BtDeviceInfo(btDevice, 2);
        return this.mBrokerHandler.hasEqualMessages(7, devInfoToCheck);
    }

    void setA2dpTimeout(java.lang.String address, int a2dpCodec, int delayMs) {
        sendILMsg(10, 2, a2dpCodec, address, delayMs);
    }

    void setLeAudioTimeout(java.lang.String address, int device, int codec, int delayMs) {
        sendIILMsg(49, 2, device, codec, address, delayMs);
    }

    void setAvrcpAbsoluteVolumeSupported(boolean supported) {
        boolean isMtk = android.os.Build.isMtkPlatform();
        android.util.Log.d(TAG, "isMtk" + isMtk);
        if (isMtk) {
            this.mBtHelper.setAvrcpAbsoluteVolumeSupported(supported);
            return;
        }
        synchronized (this.mDeviceStateLock) {
            this.mBtHelper.setAvrcpAbsoluteVolumeSupported(supported);
        }
    }

    void clearAvrcpAbsoluteVolumeSupported() {
        setAvrcpAbsoluteVolumeSupported(false);
        this.mAudioService.setAvrcpAbsoluteVolumeSupported(false);
    }

    boolean getBluetoothA2dpEnabled() {
        return this.mBluetoothA2dpEnabled.get();
    }

    int getLeAudioDeviceGroupId(android.bluetooth.BluetoothDevice device) {
        return this.mBtHelper.getLeAudioDeviceGroupId(device);
    }

    java.util.List<android.util.Pair<java.lang.String, java.lang.String>> getLeAudioGroupAddresses(int groupId) {
        return this.mBtHelper.getLeAudioGroupAddresses(groupId);
    }

    void broadcastStickyIntentToCurrentProfileGroup(android.content.Intent intent) {
        this.mSystemServer.broadcastStickyIntentToCurrentProfileGroup(intent);
    }

    void dump(final java.io.PrintWriter pw, final java.lang.String prefix) {
        if (this.mBrokerHandler != null) {
            pw.println(prefix + "Message handler (watch for unhandled messages):");
            this.mBrokerHandler.dump(new android.util.PrintWriterPrinter(pw), prefix + "  ");
        } else {
            pw.println("Message handler is null");
        }
        this.mDeviceInventory.dump(pw, prefix);
        pw.println("\n" + prefix + "Communication route clients:");
        this.mCommunicationRouteClients.forEach(new java.util.function.Consumer() { // from class: com.android.server.audio.AudioDeviceBroker$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                pw.println("  " + prefix + ((com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient) obj).toString());
            }
        });
        pw.println("\n" + prefix + "Computed Preferred communication device: " + preferredCommunicationDevice());
        pw.println("\n" + prefix + "Applied Preferred communication device: " + this.mPreferredCommunicationDevice);
        pw.println(prefix + "Active communication device: " + ((java.lang.Object) (this.mActiveCommunicationDevice == null ? com.android.server.input.KeyboardMetricsCollector.DEFAULT_LANGUAGE_TAG : new android.media.AudioDeviceAttributes(this.mActiveCommunicationDevice))));
        pw.println(prefix + "mCommunicationStrategyId: " + this.mCommunicationStrategyId);
        pw.println(prefix + "mAccessibilityStrategyId: " + this.mAccessibilityStrategyId);
        pw.println("\n" + prefix + "mAudioModeOwner: " + this.mAudioModeOwner);
        pw.println("\n" + prefix + "mScoManagedByAudio: " + this.mScoManagedByAudio);
        this.mBtHelper.dump(pw, prefix);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSetForceUse(int useCase, int config, boolean fromA2dp, java.lang.String eventSource) {
        if (useCase == 1) {
            postReportNewRoutes(fromA2dp);
        }
        com.android.server.audio.AudioService.sForceUseLogger.enqueue(new com.android.server.audio.AudioServiceEvents.ForceUseEvent(useCase, config, eventSource));
        new android.media.MediaMetrics.Item("audio.forceUse." + android.media.AudioSystem.forceUseUsageToString(useCase)).set(android.media.MediaMetrics.Property.EVENT, "onSetForceUse").set(android.media.MediaMetrics.Property.FORCE_USE_DUE_TO, eventSource).set(android.media.MediaMetrics.Property.FORCE_USE_MODE, android.media.AudioSystem.forceUseConfigToString(config)).record();
        if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
            android.util.Log.v(TAG, "onSetForceUse(useCase<" + android.media.AudioSystem.forceUseUsageToString(useCase) + ">, config<" + android.media.AudioSystem.forceUseConfigToString(config) + ">, fromA2dp<" + fromA2dp + ">, eventSource<" + eventSource + ">)");
        }
        this.mAudioSystem.setForceUse(useCase, config);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSendBecomingNoisyIntent() {
        com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("broadcast ACTION_AUDIO_BECOMING_NOISY").printLog(TAG));
        this.mSystemServer.sendDeviceBecomingNoisyIntent();
    }

    private void setupMessaging(android.content.Context ctxt) {
        android.os.PowerManager pm = (android.os.PowerManager) ctxt.getSystemService("power");
        this.mBrokerEventWakeLock = pm.newWakeLock(1, "handleAudioDeviceEvent");
        this.mBrokerThread = new com.android.server.audio.AudioDeviceBroker.BrokerThread();
        this.mBrokerThread.start();
        waitForBrokerHandlerCreation();
    }

    private void waitForBrokerHandlerCreation() {
        synchronized (this) {
            while (this.mBrokerHandler == null) {
                try {
                    wait();
                } catch (java.lang.InterruptedException e) {
                    android.util.Log.e(TAG, "Interruption while waiting on BrokerHandler");
                }
            }
        }
    }

    private class BrokerThread extends java.lang.Thread {
        android.os.Looper mLooper;

        BrokerThread() {
            super("AudioDeviceBroker");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            android.os.Looper.prepare();
            synchronized (com.android.server.audio.AudioDeviceBroker.this) {
                com.android.server.audio.AudioDeviceBroker.this.mBrokerHandler = new com.android.server.audio.AudioDeviceBroker.BrokerHandler();
                this.mLooper = android.os.Looper.myLooper();
                com.android.server.audio.AudioDeviceBroker.this.notify();
            }
            android.os.Looper.loop();
        }

        public android.os.Looper getLooper() {
            return this.mLooper;
        }
    }

    private class BrokerHandler extends android.os.Handler {
        private BrokerHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            android.media.AudioDeviceAttributes device;
            int bluetoothContextualVolumeStream = -1;
            switch (msg.what) {
                case 1:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mSetModeLock) {
                        synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                            com.android.server.audio.AudioDeviceBroker.this.initRoutingStrategyIds();
                            com.android.server.audio.AudioDeviceBroker.this.updateActiveCommunicationDevice();
                            com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.onRestoreDevices();
                            synchronized (com.android.server.audio.AudioDeviceBroker.this.mBluetoothAudioStateLock) {
                                com.android.server.audio.AudioDeviceBroker.this.reapplyAudioHalBluetoothState();
                                break;
                            }
                            com.android.server.audio.AudioDeviceBroker.this.mBtHelper.onAudioServerDiedRestoreA2dp();
                            com.android.server.audio.AudioDeviceBroker.this.updateCommunicationRoute("MSG_RESTORE_DEVICES");
                        }
                        break;
                    }
                    break;
                case 2:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                        com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.onSetWiredDeviceConnectionState((com.android.server.audio.AudioDeviceInventory.WiredDeviceConnectionState) msg.obj);
                        break;
                    }
                    break;
                case 3:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                        com.android.server.audio.AudioDeviceBroker.this.mBtHelper.onBroadcastScoConnectionState(msg.arg1);
                        break;
                    }
                    break;
                case 4:
                    com.android.server.audio.AudioDeviceBroker.this.onSetForceUse(msg.arg1, msg.arg2, false, (java.lang.String) msg.obj);
                    break;
                case 5:
                case 60:
                    int forcedUsage = com.android.server.audio.AudioDeviceBroker.this.mBluetoothA2dpEnabled.get() ? 0 : 10;
                    com.android.server.audio.AudioDeviceBroker.this.onSetForceUse(1, forcedUsage, true, (java.lang.String) msg.obj);
                    break;
                case 6:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                        com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.onToggleHdmi();
                        break;
                    }
                    break;
                case 7:
                    com.android.server.audio.AudioDeviceBroker.BtDeviceInfo btInfo = (com.android.server.audio.AudioDeviceBroker.BtDeviceInfo) msg.obj;
                    if (btInfo.mState == 2 && !com.android.server.audio.AudioDeviceBroker.this.mBtHelper.isProfilePoxyConnected(btInfo.mProfile)) {
                        com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("msg: MSG_L_SET_BT_ACTIVE_DEVICE received with null profile proxy: " + btInfo).printLog(com.android.server.audio.AudioDeviceBroker.TAG));
                    } else {
                        android.util.Pair<java.lang.Integer, java.lang.Boolean> codecAndChanged = com.android.server.audio.AudioDeviceBroker.this.mBtHelper.getCodecWithFallback(btInfo.mDevice, btInfo.mProfile, btInfo.mIsLeOutput, "MSG_L_SET_BT_ACTIVE_DEVICE");
                        synchronized (com.android.server.audio.AudioDeviceBroker.this.mSetModeLock) {
                            synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                                com.android.server.audio.AudioDeviceInventory audioDeviceInventory = com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory;
                                int iIntValue = ((java.lang.Integer) codecAndChanged.first).intValue();
                                if (btInfo.mProfile != 22 || btInfo.mIsLeOutput) {
                                    bluetoothContextualVolumeStream = com.android.server.audio.AudioDeviceBroker.this.mAudioService.getBluetoothContextualVolumeStream();
                                }
                                audioDeviceInventory.onSetBtActiveDevice(btInfo, iIntValue, bluetoothContextualVolumeStream);
                                if (btInfo.mProfile == 22 || btInfo.mProfile == 21 || (com.android.server.audio.AudioDeviceBroker.this.mScoManagedByAudio && btInfo.mProfile == 1)) {
                                    com.android.server.audio.AudioDeviceBroker.this.onUpdateCommunicationRouteClient(com.android.server.audio.AudioDeviceBroker.this.isBluetoothScoRequested(), com.android.server.audio.IAudioDeviceBrokerExt.SET_BLUETOOTH_ACTIVE_DEVICE);
                                }
                            }
                        }
                    }
                    break;
                case 9:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mSetModeLock) {
                        synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                            com.android.server.audio.AudioDeviceBroker.this.mBtHelper.resetBluetoothSco();
                            break;
                        }
                        break;
                    }
                    break;
                case 10:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                        com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.onMakeA2dpDeviceUnavailableNow((java.lang.String) msg.obj, msg.arg1);
                        break;
                    }
                    break;
                case 11:
                    com.android.server.audio.AudioDeviceBroker.BtDeviceInfo btInfo2 = (com.android.server.audio.AudioDeviceBroker.BtDeviceInfo) msg.obj;
                    android.util.Pair<java.lang.Integer, java.lang.Boolean> codecAndChanged2 = com.android.server.audio.AudioDeviceBroker.this.mBtHelper.getCodecWithFallback(btInfo2.mDevice, btInfo2.mProfile, btInfo2.mIsLeOutput, "MSG_L_BLUETOOTH_DEVICE_CONFIG_CHANGE");
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                        int iOnBluetoothDeviceConfigChange = 100 + com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.onBluetoothDeviceConfigChange(btInfo2, ((java.lang.Integer) codecAndChanged2.first).intValue(), ((java.lang.Boolean) codecAndChanged2.second).booleanValue(), 0);
                        break;
                    }
                    break;
                case 12:
                    com.android.server.audio.AudioDeviceBroker.this.onSendBecomingNoisyIntent();
                    break;
                case 13:
                case 36:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                        com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.onReportNewRoutes();
                        break;
                    }
                    break;
                case 14:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                        com.android.server.audio.AudioDeviceBroker.this.mBtHelper.setHearingAidVolume(msg.arg1, msg.arg2, com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.isHearingAidConnected());
                        break;
                    }
                    break;
                case 15:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                        com.android.server.audio.AudioDeviceBroker.this.mBtHelper.setAvrcpAbsoluteVolumeIndex(msg.arg1);
                        break;
                    }
                    break;
                case 16:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mSetModeLock) {
                        synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                            boolean wasBtScoRequested = com.android.server.audio.AudioDeviceBroker.this.isBluetoothScoRequested();
                            com.android.server.audio.AudioDeviceBroker.this.mAudioModeOwner = (com.android.server.audio.AudioDeviceBroker.AudioModeInfo) msg.obj;
                            if (com.android.server.audio.AudioDeviceBroker.this.mAudioModeOwner.mMode != 1) {
                                com.android.server.audio.AudioDeviceBroker.this.onUpdateCommunicationRouteClient(wasBtScoRequested, "setNewModeOwner");
                            }
                            if (com.android.server.audio.AudioDeviceBroker.this.mAudioModeOwner.mMode == 0) {
                                if (com.android.server.audio.AudioDeviceBroker.this.mAudioService.isVendorBeforeAndroidU() && (com.android.server.audio.AudioDeviceBroker.this.isBleHDRecordActive() || !com.android.server.audio.AudioDeviceBroker.this.isBleRecordingIdle())) {
                                    com.android.server.audio.AudioDeviceBroker.this.mAudioService.mAsSocExt.restartBleRecord();
                                } else if (com.android.server.audio.AudioDeviceBroker.this.isBleRecordingIdle() && !com.android.server.audio.AudioDeviceBroker.this.mAudioService.isVendorBeforeAndroidU() && ((device = com.android.server.audio.AudioDeviceBroker.this.requestedCommunicationDevice()) == null || device.getType() != 26)) {
                                    com.android.server.audio.AudioDeviceBroker.this.mBtHelper.updateBleCGStateToBt(false);
                                    com.android.server.audio.AudioDeviceBroker.this.mAudioService.mAsSocExt.notifyCgState(false);
                                }
                            }
                            break;
                        }
                        break;
                    }
                    break;
                case 22:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mSetModeLock) {
                        synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                            com.android.server.audio.AudioDeviceBroker.this.mBtHelper.onBtProfileDisconnected(msg.arg1);
                            com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.onBtProfileDisconnected(msg.arg1);
                            break;
                        }
                        break;
                    }
                    break;
                case 23:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mSetModeLock) {
                        synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                            com.android.server.audio.AudioDeviceBroker.this.mBtHelper.onBtProfileConnected(msg.arg1, (android.bluetooth.BluetoothProfile) msg.obj);
                            break;
                        }
                        break;
                    }
                    break;
                case 32:
                    int strategy = msg.arg1;
                    java.util.List<android.media.AudioDeviceAttributes> devices = (java.util.List) msg.obj;
                    com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.onSaveSetPreferredDevices(strategy, devices);
                    break;
                case 33:
                    int strategy2 = msg.arg1;
                    com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.onSaveRemovePreferredDevices(strategy2);
                    break;
                case 34:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mSetModeLock) {
                        synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                            com.android.server.audio.AudioDeviceBroker.this.onCommunicationRouteClientDied((com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient) msg.obj);
                            break;
                        }
                        break;
                    }
                    break;
                case 35:
                    com.android.server.audio.AudioDeviceBroker.this.checkMessagesMuteMusic(0);
                    break;
                case 37:
                    int capturePreset = msg.arg1;
                    java.util.List<android.media.AudioDeviceAttributes> devices2 = (java.util.List) msg.obj;
                    com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.onSaveSetPreferredDevicesForCapturePreset(capturePreset, devices2);
                    break;
                case 38:
                    int capturePreset2 = msg.arg1;
                    com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.onSaveClearPreferredDevicesForCapturePreset(capturePreset2);
                    break;
                case 39:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mSetModeLock) {
                        synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                            com.android.server.audio.AudioDeviceBroker.this.updateCommunicationRoute((java.lang.String) msg.obj);
                            break;
                        }
                        break;
                    }
                    break;
                case 42:
                    com.android.server.audio.AudioDeviceBroker.CommunicationDeviceInfo deviceInfo = (com.android.server.audio.AudioDeviceBroker.CommunicationDeviceInfo) msg.obj;
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mSetModeLock) {
                        synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                            com.android.server.audio.AudioDeviceBroker.this.onSetCommunicationDeviceForClient(deviceInfo);
                            break;
                        }
                    }
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mCommunicationDeviceLock) {
                        if (com.android.server.audio.AudioDeviceBroker.this.mCommunicationDeviceUpdateCount > 0) {
                            com.android.server.audio.AudioDeviceBroker.this.mCommunicationDeviceUpdateCount--;
                        } else {
                            android.util.Log.e(com.android.server.audio.AudioDeviceBroker.TAG, "mCommunicationDeviceUpdateCount already 0 in MSG_L_SET_COMMUNICATION_DEVICE_FOR_CLIENT");
                        }
                        com.android.server.audio.AudioDeviceBroker.this.mCommunicationDeviceLock.notify();
                        break;
                    }
                    break;
                case 43:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mSetModeLock) {
                        synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                            com.android.server.audio.AudioDeviceBroker audioDeviceBroker = com.android.server.audio.AudioDeviceBroker.this;
                            if (msg.arg1 != 1) {
                                z = false;
                            }
                            audioDeviceBroker.onUpdateCommunicationRouteClient(z, (java.lang.String) msg.obj);
                            break;
                        }
                        break;
                    }
                    break;
                case 45:
                    com.android.server.audio.AudioDeviceBroker.BtDeviceInfo btInfo3 = (com.android.server.audio.AudioDeviceBroker.BtDeviceInfo) msg.obj;
                    if (btInfo3.mDevice != null) {
                        com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("msg: MSG_L_BT_ACTIVE_DEVICE_CHANGE_EXT " + btInfo3).printLog(com.android.server.audio.AudioDeviceBroker.TAG));
                        synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                            com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.setBluetoothActiveDevice(btInfo3);
                        }
                    }
                    break;
                case 46:
                    com.android.server.audio.AudioDeviceBroker.BleVolumeInfo info = (com.android.server.audio.AudioDeviceBroker.BleVolumeInfo) msg.obj;
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                        if (com.android.server.audio.AudioDeviceBroker.this.getWrapper().getBluetoothVolSyncSupported()) {
                            int forceStream = (info.mStreamType == 0 || info.mStreamType == 6 || info.mStreamType == 2) ? 0 : 3;
                            android.util.Log.d(com.android.server.audio.AudioDeviceBroker.TAG, "info.mStreamType " + info.mStreamType + " forceStream " + forceStream + "  blue stream " + com.android.server.audio.AudioDeviceBroker.this.mAudioService.getBluetoothContextualVolumeStream());
                            com.android.server.audio.AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().setBleForceStream(forceStream);
                        }
                        com.android.server.audio.AudioDeviceBroker.this.mBtHelper.setLeAudioVolume(info.mIndex, info.mMaxIndex, info.mStreamType);
                        break;
                    }
                    break;
                case 47:
                    int strategy3 = msg.arg1;
                    com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.onSaveSetDeviceAsNonDefault(strategy3, (android.media.AudioDeviceAttributes) msg.obj);
                    break;
                case 48:
                    int strategy4 = msg.arg1;
                    com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.onSaveRemoveDeviceAsNonDefault(strategy4, (android.media.AudioDeviceAttributes) msg.obj);
                    break;
                case 49:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                        com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.onMakeLeAudioDeviceUnavailableNow((java.lang.String) msg.obj, msg.arg1, msg.arg2);
                        break;
                    }
                    break;
                case 52:
                    android.bluetooth.BluetoothDevice btDevice = (android.bluetooth.BluetoothDevice) msg.obj;
                    com.android.server.audio.BtHelper.onNotifyPreferredAudioProfileApplied(btDevice);
                    break;
                case 53:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mSetModeLock) {
                        synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                            com.android.server.audio.AudioDeviceBroker.this.onCheckCommunicationDeviceRemoval((android.media.AudioDeviceAttributes) msg.obj);
                            break;
                        }
                        break;
                    }
                    break;
                case 54:
                    com.android.server.audio.AudioDeviceBroker.this.onPersistAudioDeviceSettings();
                    break;
                case 55:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mSetModeLock) {
                        synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                            com.android.server.audio.AudioDeviceBroker.this.onReceiveBtEvent((android.content.Intent) msg.obj);
                            break;
                        }
                        break;
                    }
                    break;
                case 56:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                        com.android.server.audio.AudioDeviceBroker audioDeviceBroker2 = com.android.server.audio.AudioDeviceBroker.this;
                        int i = msg.arg1;
                        if (msg.arg2 != 1) {
                            z = false;
                        }
                        audioDeviceBroker2.onCheckCommunicationRouteClientState(i, z);
                        break;
                    }
                    break;
                case 57:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mSetModeLock) {
                        synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                            com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.onUpdateLeAudioGroupAddresses(msg.arg1);
                            break;
                        }
                        break;
                    }
                    break;
                case 58:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mSetModeLock) {
                        synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                            com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.onSynchronizeAdiDevicesInInventory((com.android.server.audio.AdiDeviceState) msg.obj);
                            break;
                        }
                        break;
                    }
                    break;
                case 59:
                    com.android.server.audio.AudioDeviceBroker.this.mAudioService.onUpdatedAdiDeviceState((com.android.server.audio.AdiDeviceState) msg.obj, msg.arg1 == 1);
                    break;
                case 64:
                    com.android.server.audio.AudioDeviceBroker.BtDeviceInfo info2 = (com.android.server.audio.AudioDeviceBroker.BtDeviceInfo) msg.obj;
                    com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("handleBluetoothA2dpActiveDeviceChangeExt  state=" + info2.mState + " prof=" + info2.mProfile + " supprNoisy=" + info2.mSupprNoisy + " vol=" + info2.mVolume).printLog(com.android.server.audio.AudioDeviceBroker.TAG));
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                        com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.handleBluetoothA2dpActiveDeviceChangeExt(info2.mDevice, info2.mState, info2.mProfile, info2.mSupprNoisy, info2.mVolume);
                        break;
                    }
                    break;
                case 77:
                    com.android.server.audio.AudioDeviceBroker.BtDeviceInfo btDeviceInfo = (com.android.server.audio.AudioDeviceBroker.BtDeviceInfo) msg.obj;
                    if (btDeviceInfo.mDevice != null) {
                        android.util.Pair<java.lang.Integer, java.lang.Boolean> codecAndChanged3 = com.android.server.audio.AudioDeviceBroker.this.mBtHelper.getCodecWithFallback(btDeviceInfo.mDevice, btDeviceInfo.mProfile, btDeviceInfo.mIsLeOutput, "MSG_L_A2DP_DEVICE_CONFIG_CHANGE_SHO");
                        synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                            com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.onBluetoothDeviceConfigChange(btDeviceInfo, ((java.lang.Integer) codecAndChanged3.first).intValue(), ((java.lang.Boolean) codecAndChanged3.second).booleanValue(), 0);
                        }
                    }
                    break;
                case 78:
                    com.android.server.audio.AudioDeviceBroker.this.mAdbExt.sendBtDeviceConnectedEvent((android.bluetooth.BluetoothDevice) msg.obj);
                    break;
                case 81:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                        com.android.server.audio.AudioDeviceBroker.this.getWrapper().getExtImpl().addAudioRouteEventTrack(0, 2, -1, 0);
                        break;
                    }
                    break;
                case 82:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mSetModeLock) {
                        synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                            com.android.server.audio.AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().clearRouteSettingCheck(1, 0, 0, null);
                            break;
                        }
                        break;
                    }
                    break;
                case 113:
                    android.media.AudioDeviceAttributes deviceAttributes = com.android.server.audio.AudioDeviceBroker.this.getHeadsetAudioDevice();
                    if (deviceAttributes != null) {
                        com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.setPreferredDevicesForStrategyInt(com.android.server.audio.AudioDeviceBroker.this.mCommunicationStrategyId, java.util.Arrays.asList(deviceAttributes));
                    } else if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                        android.util.Log.d(com.android.server.audio.AudioDeviceBroker.TAG, "HFP inband ring tone, prefer device is null");
                    }
                    break;
                case 125:
                    com.android.server.audio.AudioDeviceBroker.BtDeviceChangedData data = (com.android.server.audio.AudioDeviceBroker.BtDeviceChangedData) msg.obj;
                    if (com.android.server.audio.AudioDeviceBroker.this.mAdbExt != null && !com.android.server.audio.AudioDeviceBroker.this.mAdbExt.checkPreviousDeviceIsConnected(data.mPreviousDevice, data.mInfo.getProfile())) {
                        if (msg.arg1 - 1 != 0) {
                            com.android.server.audio.AudioDeviceBroker.this.sendILMsg(125, 2, msg.arg1 - 1, data, 40);
                            android.util.Log.d(com.android.server.audio.AudioDeviceBroker.TAG, "mPreviousDevice not connect retry after 40ms");
                        } else {
                            synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                                com.android.server.audio.AudioDeviceBroker.BtDeviceInfo info3 = com.android.server.audio.AudioDeviceBroker.this.createBtDeviceInfo(data, data.mNewDevice, 2);
                                com.android.server.audio.AudioDeviceBroker.this.mBrokerHandler.removeEqualMessages(45, info3);
                                com.android.server.audio.AudioDeviceBroker.this.mBrokerHandler.removeEqualMessages(7, info3);
                                android.util.Log.d(com.android.server.audio.AudioDeviceBroker.TAG, "restart connect the a2dp");
                                com.android.server.audio.AudioDeviceBroker.this.btMediaMetricRecord(data.mNewDevice, "connected", data);
                                com.android.server.audio.AudioDeviceBroker.this.sendLMsgNoDelay(45, 2, info3);
                            }
                        }
                    } else {
                        java.lang.String name = android.text.TextUtils.emptyIfNull(data.mNewDevice.getName());
                        new android.media.MediaMetrics.Item("audio.device.queueOnBluetoothActiveDeviceChanged_update").set(android.media.MediaMetrics.Property.NAME, name).set(android.media.MediaMetrics.Property.STATUS, java.lang.Integer.valueOf(data.mInfo.getProfile())).record();
                        synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                            com.android.server.audio.AudioDeviceBroker.this.postBluetoothDeviceConfigChange(com.android.server.audio.AudioDeviceBroker.this.createBtDeviceInfo(data, data.mNewDevice, 2));
                        }
                    }
                    break;
                case 130:
                    synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                        com.android.server.audio.AudioDeviceBroker.this.getWrapper().onPersistAudioHeadPhoneSettings(((java.lang.Boolean) msg.obj).booleanValue());
                        break;
                    }
                    break;
                default:
                    if ((com.android.server.audio.AudioDeviceBroker.this.mAudioService.isVendorBeforeAndroidU() && com.android.server.audio.AudioDeviceBroker.this.mAudioService.mAsSocExt.isBleAudioFeatureSupported()) || msg.what == 114) {
                        com.android.server.audio.AudioDeviceBroker.this.mAudioService.mAsSocExt.handleMessageExt(msg);
                    } else {
                        android.util.Log.w(com.android.server.audio.AudioDeviceBroker.TAG, "Invalid message " + msg.what);
                    }
                    break;
            }
            if (com.android.server.audio.AudioDeviceBroker.MESSAGES_MUTE_MUSIC.contains(java.lang.Integer.valueOf(msg.what))) {
                int delay = 0;
                if (msg.what == 7 || msg.what == 11 || msg.what == 29 || msg.what == 2 || msg.what == 77 || msg.what == 131) {
                    delay = 400;
                }
                if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                    int totalDelay = delay + 100;
                    android.util.Log.d(com.android.server.audio.AudioDeviceBroker.TAG, "Music stream's unmute delay is increased by " + totalDelay + " msfor A2DP/LE/Wired connection changes, msgid=" + msg.what);
                }
                com.android.server.audio.AudioDeviceBroker.this.sendMsg(35, 0, delay + 100);
            }
            boolean isMtk = android.os.Build.isMtkPlatform();
            if (isMtk && msg.what == 55) {
                android.content.Intent intent = (android.content.Intent) msg.obj;
                java.lang.String action = intent.getAction();
                if (action.equals("android.bluetooth.headset.profile.action.ACTIVE_DEVICE_CHANGED")) {
                    android.bluetooth.BluetoothDevice btDevice2 = (android.bluetooth.BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE", android.bluetooth.BluetoothDevice.class);
                    if (btDevice2 != null) {
                        com.android.server.audio.AudioDeviceBroker.this.sendMsg(35, 0, 400 + 100);
                    }
                }
            }
            if (com.android.server.audio.AudioDeviceBroker.isMessageHandledUnderWakelock(msg.what)) {
                try {
                    com.android.server.audio.AudioDeviceBroker.this.mBrokerEventWakeLock.release();
                } catch (java.lang.Exception e) {
                    android.util.Log.e(com.android.server.audio.AudioDeviceBroker.TAG, "Exception releasing wakelock", e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isMessageHandledUnderWakelock(int msgId) {
        switch (msgId) {
            case 2:
            case 6:
            case 7:
            case 10:
            case 11:
            case 29:
            case 31:
            case 35:
            case 49:
            case 64:
                return true;
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMsg(int msg, int existingMsgPolicy, int delay) {
        sendIILMsg(msg, existingMsgPolicy, 0, 0, null, delay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendILMsg(int msg, int existingMsgPolicy, int arg, java.lang.Object obj, int delay) {
        sendIILMsg(msg, existingMsgPolicy, arg, 0, obj, delay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendLMsg(int msg, int existingMsgPolicy, java.lang.Object obj, int delay) {
        sendIILMsg(msg, existingMsgPolicy, 0, 0, obj, delay);
    }

    private void sendMsgNoDelay(int msg, int existingMsgPolicy) {
        sendIILMsg(msg, existingMsgPolicy, 0, 0, null, 0);
    }

    private void sendIMsgNoDelay(int msg, int existingMsgPolicy, int arg) {
        sendIILMsg(msg, existingMsgPolicy, arg, 0, null, 0);
    }

    private void sendIIMsgNoDelay(int msg, int existingMsgPolicy, int arg1, int arg2) {
        sendIILMsg(msg, existingMsgPolicy, arg1, arg2, null, 0);
    }

    private void sendILMsgNoDelay(int msg, int existingMsgPolicy, int arg, java.lang.Object obj) {
        sendIILMsg(msg, existingMsgPolicy, arg, 0, obj, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendLMsgNoDelay(int msg, int existingMsgPolicy, java.lang.Object obj) {
        sendIILMsg(msg, existingMsgPolicy, 0, 0, obj, 0);
    }

    private void sendIILMsgNoDelay(int msg, int existingMsgPolicy, int arg1, int arg2, java.lang.Object obj) {
        sendIILMsg(msg, existingMsgPolicy, arg1, arg2, obj, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendIILMsg(int msg, int existingMsgPolicy, int arg1, int arg2, java.lang.Object obj, int delay) {
        if (existingMsgPolicy == 0) {
            this.mBrokerHandler.removeMessages(msg);
        } else if (existingMsgPolicy == 1 && this.mBrokerHandler.hasMessages(msg)) {
            return;
        }
        if (isMessageHandledUnderWakelock(msg)) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                try {
                    this.mBrokerEventWakeLock.acquire(BROKER_WAKELOCK_TIMEOUT_MS);
                } catch (java.lang.Exception e) {
                    android.util.Log.e(TAG, "Exception acquiring wakelock", e);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
        if (MESSAGES_MUTE_MUSIC.contains(java.lang.Integer.valueOf(msg))) {
            boolean isMtk = android.os.Build.isMtkPlatform();
            if (isMtk && msg == 2) {
                com.android.server.audio.AudioDeviceInventory.WiredDeviceConnectionState wdcs = (com.android.server.audio.AudioDeviceInventory.WiredDeviceConnectionState) obj;
                if (wdcs.mState == 1 || ((wdcs.mAttributes != null && wdcs.mAttributes.getInternalType() == -2146959360) || wdcs.mAttributes.getInternalType() == 8388608)) {
                    if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                        android.util.Log.d(TAG, "wirde device connected,do not mute");
                    }
                } else if (android.media.AudioSystem.isStreamActive(3, 0) && this.mAudioService.getDeviceForStream(3) == wdcs.mAttributes.getInternalType()) {
                    delay += 160;
                    checkMessagesMuteMusic(msg);
                }
            } else {
                delay += 160;
                checkMessagesMuteMusic(msg);
            }
        }
        synchronized (sLastDeviceConnectionMsgTimeLock) {
            long time = android.os.SystemClock.uptimeMillis() + ((long) delay);
            switch (msg) {
                case 2:
                case 7:
                case 10:
                case 11:
                case 49:
                case 77:
                    if (sLastDeviceConnectMsgTime >= time) {
                        time = sLastDeviceConnectMsgTime + 30;
                    }
                    sLastDeviceConnectMsgTime = time;
                    break;
            }
            this.mBrokerHandler.sendMessageAtTime(this.mBrokerHandler.obtainMessage(msg, arg1, arg2, obj), time);
        }
    }

    private void removeMsgForCheckClientState(int uid) {
        com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient crc = getCommunicationRouteClientForUid(uid);
        if (crc != null) {
            this.mBrokerHandler.removeEqualMessages(56, crc);
        }
    }

    private void sendMsgForCheckClientState(int msg, int existingMsgPolicy, int arg1, int arg2, java.lang.Object obj, int delay) {
        if (existingMsgPolicy == 0 && obj != null) {
            this.mBrokerHandler.removeEqualMessages(msg, obj);
        }
        long time = android.os.SystemClock.uptimeMillis() + ((long) delay);
        this.mBrokerHandler.sendMessageAtTime(this.mBrokerHandler.obtainMessage(msg, arg1, arg2, obj), time);
    }

    private static <T> boolean hasIntersection(java.util.Set<T> a, java.util.Set<T> b) {
        for (T e : a) {
            if (b.contains(e)) {
                return true;
            }
        }
        return false;
    }

    boolean messageMutesMusic(int message) {
        if (message == 0) {
            return false;
        }
        boolean isQcom = android.os.Build.isQcomPlatform();
        if ((message == 7 || message == 29 || message == 11 || (isQcom && message == 77)) && android.media.AudioSystem.isStreamActive(3, 0) && hasIntersection(com.android.server.audio.AudioDeviceInventory.DEVICE_OVERRIDE_A2DP_ROUTE_ON_PLUG_SET, this.mAudioService.getDeviceSetForStream(3))) {
            return false;
        }
        return true;
    }

    void postCheckMessagesMuteMusic() {
        android.util.Log.d(TAG, "postCheckMessagesMuteMusic() true");
        checkMessagesMuteMusic(7);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkMessagesMuteMusic(int message) {
        boolean mute = messageMutesMusic(message);
        if (!mute) {
            java.util.Iterator<java.lang.Integer> it = MESSAGES_MUTE_MUSIC.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                int msg = it.next().intValue();
                if (this.mBrokerHandler.hasMessages(msg) && messageMutesMusic(msg)) {
                    mute = true;
                    break;
                }
            }
        }
        if (mute != this.mMusicMuted.getAndSet(mute)) {
            this.mAudioService.setMusicMute(mute);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class CommunicationRouteClient implements android.os.IBinder.DeathRecipient {
        private final android.os.IBinder mCb;
        private android.media.AudioDeviceAttributes mDevice;
        private final boolean mIsPrivileged;
        private boolean mPlaybackActive;
        private boolean mRecordingActive;
        private final int mUid;

        CommunicationRouteClient(android.os.IBinder cb, int uid, android.media.AudioDeviceAttributes device, boolean isPrivileged) {
            this.mCb = cb;
            this.mUid = uid;
            this.mDevice = device;
            this.mIsPrivileged = isPrivileged;
            this.mPlaybackActive = com.android.server.audio.AudioDeviceBroker.this.mAudioService.isPlaybackActiveForUid(uid);
            this.mRecordingActive = com.android.server.audio.AudioDeviceBroker.this.mAudioService.isRecordingActiveForUid(uid);
        }

        public boolean registerDeathRecipient() {
            try {
                this.mCb.linkToDeath(this, 0);
                return true;
            } catch (android.os.RemoteException e) {
                android.util.Log.w(com.android.server.audio.AudioDeviceBroker.TAG, "CommunicationRouteClient could not link to " + this.mCb + " binder death");
                return false;
            }
        }

        public void unregisterDeathRecipient() {
            try {
                this.mCb.unlinkToDeath(this, 0);
            } catch (java.util.NoSuchElementException e) {
                android.util.Log.w(com.android.server.audio.AudioDeviceBroker.TAG, "CommunicationRouteClient could not unlink to " + this.mCb + " binder death");
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.audio.AudioDeviceBroker.this.postCommunicationRouteClientDied(this);
        }

        android.os.IBinder getBinder() {
            return this.mCb;
        }

        int getUid() {
            return this.mUid;
        }

        boolean isPrivileged() {
            return this.mIsPrivileged;
        }

        android.media.AudioDeviceAttributes getDevice() {
            return this.mDevice;
        }

        public void setPlaybackActive(boolean active) {
            this.mPlaybackActive = active;
        }

        public void setRecordingActive(boolean active) {
            this.mRecordingActive = active;
        }

        public boolean isActive() {
            return this.mIsPrivileged || this.mRecordingActive || this.mPlaybackActive;
        }

        public java.lang.String toString() {
            return "[CommunicationRouteClient: mUid: " + this.mUid + " @" + java.lang.Integer.toHexString(hashCode()) + " mCb: " + this.mCb + " mDevice: " + this.mDevice.toString() + " mIsPrivileged: " + this.mIsPrivileged + " mPlaybackActive: " + this.mPlaybackActive + " mRecordingActive: " + this.mRecordingActive + "]";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCommunicationRouteClientDied(com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient client) {
        if (client == null) {
            return;
        }
        android.util.Log.w(TAG, "Communication client died");
        setCommunicationRouteForClient(client.getBinder(), client.getUid(), null, -1, client.isPrivileged(), "onCommunicationRouteClientDied");
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0031  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.media.AudioDeviceAttributes preferredCommunicationDevice() {
        /*
            r5 = this;
            com.android.server.audio.AudioService r0 = r5.mAudioService
            boolean r0 = r0.isVendorBeforeAndroidU()
            r1 = 1
            if (r0 != r1) goto L1e
            com.android.server.audio.AudioService r0 = r5.mAudioService
            com.android.server.audio.IAudioServiceSocExt r0 = r0.mAsSocExt
            boolean r0 = r0.isBleAudioFeatureSupported()
            if (r0 != r1) goto L1e
            com.android.server.audio.AudioService r0 = r5.mAudioService
            com.android.server.audio.IAudioServiceSocExt r0 = r0.mAsSocExt
            android.media.AudioDeviceAttributes r0 = r0.preferredCommunicationDevice()
            if (r0 == 0) goto L1e
            return r0
        L1e:
            com.android.server.audio.BtHelper r0 = r5.mBtHelper
            boolean r0 = r0.isBluetoothScoOn()
            java.lang.Object r2 = r5.mBluetoothAudioStateLock
            monitor-enter(r2)
            if (r0 == 0) goto L31
            boolean r3 = r5.mBluetoothScoOn     // Catch: java.lang.Throwable -> L2f
            if (r3 == 0) goto L31
            r3 = r1
            goto L32
        L2f:
            r1 = move-exception
            goto L74
        L31:
            r3 = 0
        L32:
            r0 = r3
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L2f
            if (r0 == 0) goto L4d
            r2 = 0
            boolean r3 = android.os.Build.isQcomPlatform()
            if (r3 == 0) goto L44
            com.android.server.audio.BtHelper r3 = r5.mBtHelper
            android.media.AudioDeviceAttributes r2 = r3.getHeadsetAudioDummyDevice()
            goto L4a
        L44:
            com.android.server.audio.BtHelper r3 = r5.mBtHelper
            android.media.AudioDeviceAttributes r2 = r3.getHeadsetAudioDevice()
        L4a:
            if (r2 == 0) goto L4d
            return r2
        L4d:
            android.media.AudioDeviceAttributes r2 = r5.requestedCommunicationDevice()
            if (r2 == 0) goto L72
            int r3 = r2.getType()
            r4 = 7
            if (r3 == r4) goto L72
            boolean r3 = android.os.Build.isMtkPlatform()
            if (r3 == 0) goto L71
            com.android.server.audio.AudioService r3 = r5.mAudioService
            boolean r3 = r3.isVendorBeforeAndroidU()
            if (r3 != r1) goto L71
            int r1 = r2.getType()
            r3 = 26
            if (r1 != r3) goto L71
            goto L72
        L71:
            return r2
        L72:
            r1 = 0
            return r1
        L74:
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L2f
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.AudioDeviceBroker.preferredCommunicationDevice():android.media.AudioDeviceAttributes");
    }

    android.media.AudioDeviceAttributes getHeadsetAudioDevice() {
        android.media.AudioDeviceAttributes headsetAudioDevice;
        synchronized (this.mDeviceStateLock) {
            headsetAudioDevice = this.mBtHelper.getHeadsetAudioDevice();
        }
        return headsetAudioDevice;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCommunicationRoute(java.lang.String eventSource) {
        android.media.AudioDeviceAttributes audioDevice;
        android.media.AudioDeviceAttributes preferredCommunicationDevice = preferredCommunicationDevice();
        if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
            android.util.Log.v(TAG, "updateCommunicationRoute, preferredCommunicationDevice: " + preferredCommunicationDevice + " eventSource: " + eventSource);
        }
        com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("updateCommunicationRoute, preferredCommunicationDevice: " + preferredCommunicationDevice + " eventSource: " + eventSource));
        this.mPreCommunicationDevice = this.mCurCommunicationDevice;
        if (preferredCommunicationDevice == null) {
            android.media.AudioDeviceAttributes defaultDevice = getDefaultCommunicationDevice();
            if (android.os.Build.isMtkPlatform() && defaultDevice != null && defaultDevice.getType() == 26) {
                android.media.AudioDeviceAttributes device = this.mAudioService.mAsSocExt.getLeAudioDevice();
                if (device != null) {
                    defaultDevice = device;
                }
                if (this.mAudioService.isVendorBeforeAndroidU() && !this.mAudioService.mAsSocExt.isBluetoothLeCgStateOn()) {
                    this.mAudioService.mAsSocExt.startBluetoothLeCg(this.mAudioModeOwner.mPid, this.mAudioModeOwner.mUid, 3, this.mAudioService.mAsSocExt.getModeCb());
                }
                if (getWrapper().getBluetoothVolSyncSupported() && device != null && (device.getInternalType() == 536870912 || device.getInternalType() == 536870913)) {
                    int streamType = this.mAudioService.getBluetoothContextualVolumeStream();
                    if (streamType == 0) {
                        streamType = 6;
                    }
                    int leAudioVolIndex = getVssVolumeForDevice(streamType, device.getInternalType());
                    int leAudioMaxVolIndex = getMaxVssVolumeForStream(streamType);
                    if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
                        android.util.Log.v(TAG, "setCommunicationRouteForClient restoring LE Audio device volume lvl.");
                    }
                    postSetLeAudioVolumeIndex(leAudioVolIndex, leAudioMaxVolIndex, streamType);
                }
            }
            this.mCurCommunicationDevice = defaultDevice;
            if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
                android.util.Log.v(TAG, "updateCommunicationRoute, mPreCommunicationDevice: " + this.mPreCommunicationDevice + " mCurCommunicationDevice: " + this.mCurCommunicationDevice);
            }
            if (!this.mAudioService.isVendorBeforeAndroidU() && this.mPreCommunicationDevice != this.mCurCommunicationDevice) {
                if (this.mCurCommunicationDevice != null && this.mCurCommunicationDevice.getType() == 26) {
                    this.mBtHelper.updateBleCGStateToBt(true);
                } else if (this.mPreCommunicationDevice != null && this.mPreCommunicationDevice.getType() == 26 && isBleRecordingIdle()) {
                    this.mBtHelper.updateBleCGStateToBt(false);
                }
            }
            if (defaultDevice != null) {
                this.mDeviceInventory.setPreferredDevicesForStrategyInt(this.mCommunicationStrategyId, java.util.Arrays.asList(defaultDevice));
                this.mDeviceInventory.setPreferredDevicesForStrategyInt(this.mAccessibilityStrategyId, java.util.Arrays.asList(defaultDevice));
            } else {
                this.mDeviceInventory.removePreferredDevicesForStrategyInt(this.mCommunicationStrategyId);
                this.mDeviceInventory.removePreferredDevicesForStrategyInt(this.mAccessibilityStrategyId);
            }
            this.mDeviceInventory.applyConnectedDevicesRoles();
            this.mDeviceInventory.reapplyExternalDevicesRoles();
        } else {
            if (android.os.Build.isMtkPlatform() && preferredCommunicationDevice.getType() == 26 && (audioDevice = this.mAudioService.mAsSocExt.getLeAudioDevice()) != null) {
                preferredCommunicationDevice = audioDevice;
            }
            this.mCurCommunicationDevice = preferredCommunicationDevice;
            if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
                android.util.Log.v(TAG, "updateCommunicationRoute, mPreCommunicationDevice: " + this.mPreCommunicationDevice + " mCurCommunicationDevice: " + this.mCurCommunicationDevice);
            }
            if (!this.mAudioService.isVendorBeforeAndroidU() && this.mPreCommunicationDevice != this.mCurCommunicationDevice) {
                if (this.mCurCommunicationDevice != null && this.mCurCommunicationDevice.getType() == 26) {
                    this.mBtHelper.updateBleCGStateToBt(true);
                    if (android.os.Build.isMtkPlatform()) {
                        this.mAudioService.mAsSocExt.notifyCgState(true);
                    }
                } else if (this.mPreCommunicationDevice != null && this.mPreCommunicationDevice.getType() == 26 && isBleRecordingIdle()) {
                    this.mBtHelper.updateBleCGStateToBt(false);
                    if (android.os.Build.isMtkPlatform()) {
                        this.mAudioService.mAsSocExt.notifyCgState(false);
                    }
                }
            }
            this.mDeviceInventory.setPreferredDevicesForStrategyInt(this.mCommunicationStrategyId, java.util.Arrays.asList(preferredCommunicationDevice));
            this.mDeviceInventory.setPreferredDevicesForStrategyInt(this.mAccessibilityStrategyId, java.util.Arrays.asList(preferredCommunicationDevice));
        }
        onUpdatePhoneStrategyDevice(preferredCommunicationDevice);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUpdateCommunicationRouteClient(boolean wasBtScoRequested, java.lang.String eventSource) {
        com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient crc = topCommunicationRouteClient();
        if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
            android.util.Log.v(TAG, "onUpdateCommunicationRouteClient, crc: " + crc + " wasBtScoRequested: " + wasBtScoRequested + " eventSource: " + eventSource);
        }
        if (crc != null) {
            if (eventSource.equals(com.android.server.audio.IAudioDeviceBrokerExt.SET_BLUETOOTH_ACTIVE_DEVICE)) {
                android.media.AudioDeviceAttributes device = this.mAdbExt.checkWhetherAnotherLeDevice(crc.getDevice());
                setCommunicationRouteForClient(crc.getBinder(), crc.getUid(), device, -1, crc.isPrivileged(), eventSource);
                return;
            } else {
                setCommunicationRouteForClient(crc.getBinder(), crc.getUid(), crc.getDevice(), -1, crc.isPrivileged(), eventSource);
                return;
            }
        }
        if (!this.mScoManagedByAudio && !isBluetoothScoRequested() && wasBtScoRequested) {
            this.mBtHelper.stopBluetoothSco(eventSource);
        }
        updateCommunicationRoute(eventSource);
    }

    private void onUpdatePhoneStrategyDevice(android.media.AudioDeviceAttributes device) {
        boolean wasSpeakerphoneActive = isSpeakerphoneActive();
        this.mPreferredCommunicationDevice = device;
        if (this.mAudioService.getWrapper().getExtImpl().isAudioRouteSupported()) {
            this.mAudioService.getWrapper().getExtImpl().updatePreferredCommunicationDevice(this.mPreferredCommunicationDevice);
        }
        updateActiveCommunicationDevice();
        if (wasSpeakerphoneActive != isSpeakerphoneActive()) {
            try {
                this.mContext.sendBroadcastAsUser(new android.content.Intent("android.media.action.SPEAKERPHONE_STATE_CHANGED").setFlags(1073741824), android.os.UserHandle.ALL);
            } catch (java.lang.Exception e) {
                android.util.Log.w(TAG, "failed to broadcast ACTION_SPEAKERPHONE_STATE_CHANGED: " + e);
            }
        }
        this.mAudioService.postUpdateRingerModeServiceInt();
        dispatchCommunicationDevice();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient removeCommunicationRouteClient(android.os.IBinder cb, boolean unregister) {
        for (com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient cl : this.mCommunicationRouteClients) {
            if (cl.getBinder() == cb) {
                if (unregister) {
                    cl.unregisterDeathRecipient();
                }
                removeMsgForCheckClientState(cl.getUid());
                this.mCommunicationRouteClients.remove(cl);
                if (this.mAudioService.getWrapper().getExtImpl().isAudioRouteSupported()) {
                    this.mAudioService.getWrapper().getExtImpl().removeRouteClientActiveState(cl.getUid());
                }
                return cl;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient addCommunicationRouteClient(android.os.IBinder cb, int uid, android.media.AudioDeviceAttributes device, boolean isPrivileged) {
        removeCommunicationRouteClient(cb, true);
        com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient client = new com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient(cb, uid, device, isPrivileged);
        if (client.registerDeathRecipient()) {
            this.mCommunicationRouteClients.add(0, client);
            if (this.mAudioService.getWrapper().getExtImpl().isAudioRouteSupported()) {
                this.mAudioService.getWrapper().getExtImpl().addRouteClientActiveState(uid, device.getType());
            }
            if (!client.isActive() && !this.mAudioService.getWrapper().getExtImpl().isAudioRouteSupported()) {
                setForceCommunicationClientStateAndDelayedCheck(client, !this.mAudioService.isPlaybackActiveForUid(client.getUid()), true ^ this.mAudioService.isRecordingActiveForUid(client.getUid()));
            }
            return client;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient getCommunicationRouteClientForUid(int uid) {
        for (com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient cl : this.mCommunicationRouteClients) {
            if (cl.getUid() == uid) {
                return cl;
            }
        }
        return null;
    }

    public int getCurrentConnectedScoDevices() {
        int currentConnectedScoDevices;
        synchronized (this.mDeviceStateLock) {
            currentConnectedScoDevices = this.mDeviceInventory.getCurrentConnectedScoDevices();
        }
        return currentConnectedScoDevices;
    }

    public boolean isLeConnected() {
        boolean zIsLeConnected;
        synchronized (this.mDeviceStateLock) {
            zIsLeConnected = this.mDeviceInventory.isLeConnected();
        }
        return zIsLeConnected;
    }

    private boolean communnicationDeviceLeAudioCompatOn() {
        return (this.mAudioModeOwner.mMode != 3 || android.app.compat.CompatChanges.isChangeEnabled(USE_SET_COMMUNICATION_DEVICE, this.mAudioModeOwner.mUid) || this.mAudioModeOwner.mUid == 1000) ? false : true;
    }

    private boolean communnicationDeviceHaCompatOn() {
        return this.mAudioModeOwner.mMode == 3 && this.mAudioModeOwner.mUid != 1000;
    }

    android.media.AudioDeviceAttributes getDefaultCommunicationDevice() {
        android.media.AudioDeviceAttributes device = null;
        if (0 != 0 && communnicationDeviceHaCompatOn()) {
            device = this.mDeviceInventory.getDeviceOfType(134217728);
        }
        if (android.os.Build.isMtkPlatform() && device == null && isSupportFakeHfp(this.mAudioModeOwner.mUid)) {
            return null;
        }
        if (device == null && 0 != 0 && communnicationDeviceLeAudioCompatOn()) {
            return this.mDeviceInventory.getDeviceOfType(536870912);
        }
        return device;
    }

    public boolean isSupportFakeHfp(int uid) {
        if (uid == 0) {
            if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
                android.util.Log.v(TAG, "isSupportFakeHfp, uid = 0");
                return false;
            }
            return false;
        }
        return this.mAdbExt.isSupportFakeHfp();
    }

    void updateCommunicationRouteClientsActivity(java.util.List<android.media.AudioPlaybackConfiguration> playbackConfigs, java.util.List<android.media.AudioRecordingConfiguration> recordConfigs) {
        synchronized (this.mSetModeLock) {
            synchronized (this.mDeviceStateLock) {
                for (com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient crc : this.mCommunicationRouteClients) {
                    boolean wasActive = crc.isActive();
                    boolean updateClientState = false;
                    boolean z = true;
                    if (playbackConfigs != null) {
                        crc.setPlaybackActive(false);
                        java.util.Iterator<android.media.AudioPlaybackConfiguration> it = playbackConfigs.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            android.media.AudioPlaybackConfiguration config = it.next();
                            if (config.getClientUid() == crc.getUid() && config.isActive()) {
                                crc.setPlaybackActive(true);
                                updateClientState = true;
                                break;
                            }
                        }
                    }
                    if (recordConfigs != null) {
                        crc.setRecordingActive(false);
                        java.util.Iterator<android.media.AudioRecordingConfiguration> it2 = recordConfigs.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                break;
                            }
                            android.media.AudioRecordingConfiguration config2 = it2.next();
                            if (config2.getClientUid() == crc.getUid() && !config2.isClientSilenced()) {
                                crc.setRecordingActive(true);
                                updateClientState = true;
                                break;
                            }
                        }
                    }
                    if (updateClientState) {
                        removeMsgForCheckClientState(crc.getUid());
                        updateCommunicationRouteClientState(crc, wasActive);
                    } else if (wasActive && !this.mAudioService.getWrapper().getExtImpl().isAudioRouteSupported()) {
                        boolean z2 = playbackConfigs != null;
                        if (recordConfigs == null) {
                            z = false;
                        }
                        setForceCommunicationClientStateAndDelayedCheck(crc, z2, z);
                    }
                }
            }
        }
    }

    java.util.List<java.lang.String> getDeviceIdentityAddresses(android.media.AudioDeviceAttributes device) {
        java.util.List<java.lang.String> deviceIdentityAddresses;
        synchronized (this.mDeviceStateLock) {
            deviceIdentityAddresses = this.mDeviceInventory.getDeviceIdentityAddresses(device);
        }
        return deviceIdentityAddresses;
    }

    void dispatchPreferredMixerAttributesChangedCausedByDeviceRemoved(android.media.AudioDeviceInfo info) {
        this.mAudioService.dispatchPreferredMixerAttributesChanged(new android.media.AudioAttributes.Builder().setUsage(1).build(), info.getId(), null);
    }

    public void postPersistAudioDeviceSettings() {
        sendMsg(54, 0, 1000);
    }

    void onPersistAudioDeviceSettings() {
        java.lang.String deviceSettings = this.mDeviceInventory.getDeviceSettings();
        android.util.Log.v(TAG, "onPersistAudioDeviceSettings AdiDeviceState: " + deviceSettings);
        java.lang.String currentSettings = readDeviceSettings();
        if (deviceSettings.equals(currentSettings)) {
            return;
        }
        com.android.server.audio.SettingsAdapter settingsAdapter = this.mAudioService.getSettings();
        try {
            boolean res = settingsAdapter.putSecureStringForUser(this.mAudioService.getContentResolver(), "audio_device_inventory", deviceSettings, -2);
            if (!res) {
                android.util.Log.e(TAG, "error saving AdiDeviceState: " + deviceSettings);
            }
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Log.e(TAG, "error saving AdiDeviceState: " + deviceSettings, e);
        }
    }

    private java.lang.String readDeviceSettings() {
        com.android.server.audio.SettingsAdapter settingsAdapter = this.mAudioService.getSettings();
        android.content.ContentResolver contentResolver = this.mAudioService.getContentResolver();
        return settingsAdapter.getSecureStringForUser(contentResolver, "audio_device_inventory", -2);
    }

    void onReadAudioDeviceSettings() {
        com.android.server.audio.SettingsAdapter settingsAdapter = this.mAudioService.getSettings();
        android.content.ContentResolver contentResolver = this.mAudioService.getContentResolver();
        java.lang.String settings = readDeviceSettings();
        if (settings == null) {
            android.util.Log.i(TAG, "reading AdiDeviceState from legacy keyspatial_audio_enabled");
            settings = settingsAdapter.getSecureStringForUser(contentResolver, "spatial_audio_enabled", -2);
            if (settings == null) {
                android.util.Log.i(TAG, "no AdiDeviceState stored with legacy key");
            } else if (!settings.equals("")) {
                if (!settingsAdapter.putSecureStringForUser(contentResolver, "spatial_audio_enabled", "", -2)) {
                    android.util.Log.w(TAG, "cannot erase the legacy AdiDeviceState with key spatial_audio_enabled");
                }
                if (!settingsAdapter.putSecureStringForUser(contentResolver, "audio_device_inventory", settings, -2)) {
                    android.util.Log.e(TAG, "error updating the new AdiDeviceState with key audio_device_inventory");
                }
            }
        }
        if (settings != null && !settings.equals("")) {
            setDeviceSettings(settings);
        }
    }

    void reInitSAState(com.android.server.audio.AdiDeviceState device) {
        this.mAudioService.reInitSAState(device);
    }

    void setDeviceSettings(java.lang.String settings) {
        this.mDeviceInventory.setDeviceSettings(settings);
    }

    java.lang.String getDeviceSettings() {
        return this.mDeviceInventory.getDeviceSettings();
    }

    java.util.Collection<com.android.server.audio.AdiDeviceState> getImmutableDeviceInventory() {
        return this.mDeviceInventory.getImmutableDeviceInventory();
    }

    void addOrUpdateDeviceSAStateInInventory(com.android.server.audio.AdiDeviceState deviceState, boolean syncInventory) {
        this.mDeviceInventory.addOrUpdateDeviceSAStateInInventory(deviceState, syncInventory);
    }

    void addOrUpdateBtAudioDeviceCategoryInInventory(com.android.server.audio.AdiDeviceState deviceState, boolean syncInventory) {
        this.mDeviceInventory.addOrUpdateAudioDeviceCategoryInInventory(deviceState, syncInventory);
    }

    com.android.server.audio.AdiDeviceState findDeviceStateForAudioDeviceAttributes(android.media.AudioDeviceAttributes ada, int canonicalType) {
        return this.mDeviceInventory.findDeviceStateForAudioDeviceAttributes(ada, canonicalType);
    }

    com.android.server.audio.AdiDeviceState findBtDeviceStateForAddress(java.lang.String address, int deviceType) {
        return this.mDeviceInventory.findBtDeviceStateForAddress(address, deviceType);
    }

    void addAudioDeviceWithCategoryInInventoryIfNeeded(java.lang.String address, int btAudioDeviceCategory) {
        this.mDeviceInventory.addAudioDeviceWithCategoryInInventoryIfNeeded(address, btAudioDeviceCategory);
    }

    int getAndUpdateBtAdiDeviceStateCategoryForAddress(java.lang.String address) {
        return this.mDeviceInventory.getAndUpdateBtAdiDeviceStateCategoryForAddress(address);
    }

    boolean isBluetoothAudioDeviceCategoryFixed(java.lang.String address) {
        return this.mDeviceInventory.isBluetoothAudioDeviceCategoryFixed(address);
    }

    boolean isSADevice(com.android.server.audio.AdiDeviceState deviceState) {
        return this.mAudioService.isSADevice(deviceState);
    }

    void clearDeviceInventory() {
        this.mDeviceInventory.clearDeviceInventory();
    }

    boolean isInbandRingingEnabled() {
        return this.mBtHelper.isInbandRingingEnabled();
    }

    void handleRecordingConfigurationChanged(int event, int sampleRate, int uid, android.media.AudioDeviceInfo deviceInfo) {
        java.lang.Integer integerUid = java.lang.Integer.valueOf(uid);
        switch (event) {
            case 1:
                handleStop(integerUid, sampleRate, uid);
                break;
            case 2:
                if (deviceInfo.getType() == 26) {
                    handleUpdate(integerUid, sampleRate, uid);
                } else if (this.mSampleRateHD.contains(integerUid) || this.mSampleRateNormal.contains(integerUid)) {
                    handleStop(integerUid, sampleRate, uid);
                }
                break;
        }
    }

    private void handleUpdate(java.lang.Integer integerUid, int sampleRate, int uid) {
        if (this.mSampleRateHD.contains(integerUid) || this.mSampleRateNormal.contains(integerUid)) {
            return;
        }
        if (this.mAudioService.isVendorBeforeAndroidU()) {
            this.mAudioService.mAsSocExt.startBluetoothLeCgForRecord(new android.os.Binder(), uid, sampleRate);
        }
        if (sampleRate >= 48000) {
            this.mSampleRateHD.add(integerUid);
        } else {
            this.mSampleRateNormal.add(integerUid);
        }
        if (!this.mAudioService.isVendorBeforeAndroidU()) {
            this.mBtHelper.updateBleCGStateToBt(true);
            this.mAudioService.mAsSocExt.notifyCgState(true);
        }
    }

    private void handleStop(java.lang.Integer integerUid, int sampleRate, int uid) {
        if (!this.mSampleRateHD.contains(integerUid) && !this.mSampleRateNormal.contains(integerUid)) {
            if (com.android.server.audio.AudioService.DEBUG_COMM_RTE) {
                android.util.Log.d(TAG, "handleStop, hd and normal record do not contain this uid");
                return;
            }
            return;
        }
        if (sampleRate >= 48000) {
            this.mSampleRateHD.remove(integerUid);
        } else {
            this.mSampleRateNormal.remove(integerUid);
        }
        com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient client = getCommunicationRouteClientForUid(uid);
        android.os.IBinder cb = null;
        if (client != null) {
            cb = client.getBinder();
        }
        if (isBleRecordingIdle() && !isInCommunication()) {
            if (this.mAudioService.isVendorBeforeAndroidU() && cb != null) {
                this.mAudioService.mAsSocExt.stopBluetoothLeCgForRecord(cb, uid);
            } else {
                this.mBtHelper.updateBleCGStateToBt(false);
                this.mAudioService.mAsSocExt.notifyCgState(false);
            }
        } else {
            android.util.Log.d(TAG, "Don't stop cg, isBleRecordingIdle: " + isBleRecordingIdle());
        }
        if (cb != null) {
            removeCommunicationRouteClient(cb, true);
        }
    }

    boolean isBleRecordingIdle() {
        return this.mSampleRateHD.isEmpty() && this.mSampleRateNormal.isEmpty();
    }

    boolean isBleHDRecordActive() {
        return !this.mSampleRateHD.isEmpty();
    }

    public boolean isLoudSpeakerBt() {
        return this.mBtHelper.isLoudSpeakerBt();
    }

    public boolean isUnsuHoloVoipDevice() {
        return this.mBtHelper.isUnsuHoloVoipDevice();
    }

    public boolean isUnsupportHoloDevice(java.lang.String device) {
        return this.mAudioService.getWrapper().isUnsupportHoloDevice(device);
    }

    public boolean isHoloVoipSupport() {
        return this.mDeviceInventory.isHoloVoipSupport();
    }

    public boolean isHoloBLeDeviceConnected() {
        boolean zIsHoloBLeDeviceConnected;
        synchronized (this.mDeviceStateLock) {
            zIsHoloBLeDeviceConnected = this.mDeviceInventory.isHoloBLeDeviceConnected();
        }
        return zIsHoloBLeDeviceConnected;
    }

    public com.android.server.audio.IAudioDeviceBrokerWrapper getWrapper() {
        return this.mAdbWrapper;
    }

    private class AudioDeviceBrokerWrapper implements com.android.server.audio.IAudioDeviceBrokerWrapper {
        private AudioDeviceBrokerWrapper() {
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public com.android.server.audio.IAudioDeviceBrokerExt getExtImpl() {
            return com.android.server.audio.AudioDeviceBroker.this.mAdbExt;
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public com.android.server.audio.AudioDeviceInventory getDeviceInventory() {
            return com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory;
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public boolean isSpeakerA2dpDevice() {
            return com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.getWrapper().getExtImpl().isSpeakerA2dpDevice();
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public int getA2dpVolume(boolean cmpToSafeVolume, int a2dpVolume) {
            return com.android.server.audio.AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().getA2dpVolume(cmpToSafeVolume, a2dpVolume);
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void sendIILMsg(int msg, int existingMsgPolicy, int arg1, int arg2, java.lang.Object obj, int delay) {
            com.android.server.audio.AudioDeviceBroker.this.sendIILMsg(msg, existingMsgPolicy, arg1, arg2, obj, delay);
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public int getSetAvrcpAbsVolMsg() {
            return 15;
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public boolean isDeviceConnected(android.media.AudioDeviceAttributes device) {
            return com.android.server.audio.AudioDeviceBroker.this.isDeviceConnected(device);
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public boolean isAudioRouteSupported() {
            return com.android.server.audio.AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().isAudioRouteSupported();
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void removeInactiveRouteClientForUid(int uid) {
            com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient cl = com.android.server.audio.AudioDeviceBroker.this.getCommunicationRouteClientForUid(uid);
            if (cl != null) {
                android.util.Log.d(com.android.server.audio.AudioDeviceBroker.TAG, "uid " + cl.getUid() + " has no active audio for a long time ,remove it");
                com.android.server.audio.AudioDeviceBroker.this.postSetCommunicationDeviceForClient(new com.android.server.audio.AudioDeviceBroker.CommunicationDeviceInfo(cl.getBinder(), uid, cl.getDevice(), false, -1, com.android.server.audio.IAudioDeviceBrokerExt.REMOVE_INACTIVE_ROUTE_CLIENT, false));
            }
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void checkClearSpeakerDevice(int uid) {
            com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient cl = com.android.server.audio.AudioDeviceBroker.this.getCommunicationRouteClientForUid(uid);
            if (cl == null) {
                com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.removePreferredDevicesForStrategyInt(com.android.server.audio.AudioDeviceBroker.this.mCommunicationStrategyId);
                com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.removePreferredDevicesForStrategyInt(com.android.server.audio.AudioDeviceBroker.this.mAccessibilityStrategyId);
                android.util.Log.d(com.android.server.audio.AudioDeviceBroker.TAG, "checkClearSpeakerDevice remove speaker");
            }
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void removeRouteClientForUid(int uid) {
            synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient cl = com.android.server.audio.AudioDeviceBroker.this.getCommunicationRouteClientForUid(uid);
                if (cl != null) {
                    com.android.server.audio.AudioDeviceBroker.this.removeCommunicationRouteClient(cl.getBinder(), true);
                }
            }
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void stopBluethoothScoToBT(java.lang.String eventSource) {
            com.android.server.audio.AudioDeviceBroker.this.mBtHelper.stopBluetoothSco(eventSource);
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void clearRedundancyClient(int uid, android.os.IBinder cb) {
            synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient client = com.android.server.audio.AudioDeviceBroker.this.getCommunicationRouteClientForUid(uid);
                if (client != null && client.getBinder() != cb) {
                    com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient client2 = com.android.server.audio.AudioDeviceBroker.this.removeCommunicationRouteClient(client.getBinder(), true);
                    boolean privileged = client2.isPrivileged();
                    com.android.server.audio.AudioDeviceBroker.this.addCommunicationRouteClient(cb, uid, client2.getDevice(), privileged);
                    android.util.Log.d(com.android.server.audio.AudioDeviceBroker.TAG, "clearRedundancyClient for uid " + uid);
                }
            }
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void checkBuildRouteForSco(int uid, java.lang.Object obj) {
            if (uid == -1 || obj == null) {
                return;
            }
            android.os.IBinder cb = (android.os.IBinder) obj;
            if (com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.getWrapper().isBluetoothScoDeviceConnected() && com.android.server.audio.AudioDeviceBroker.this.getCommunicationRouteClientForUid(uid) == null) {
                synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                    android.media.AudioDeviceAttributes device = new android.media.AudioDeviceAttributes(16, "");
                    com.android.server.audio.AudioDeviceBroker.CommunicationRouteClient client = com.android.server.audio.AudioDeviceBroker.this.getCommunicationRouteClientForUid(uid);
                    if (client != null) {
                        boolean privileged = client.isPrivileged();
                        com.android.server.audio.AudioDeviceBroker.this.addCommunicationRouteClient(cb, uid, device, privileged);
                    }
                    android.util.Log.d(com.android.server.audio.AudioDeviceBroker.TAG, "Build SCO Route for uid " + uid);
                }
            }
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void checkTimeoutInactiveRouteClient() {
            synchronized (com.android.server.audio.AudioDeviceBroker.this.mSetModeLock) {
                synchronized (com.android.server.audio.AudioDeviceBroker.this.mDeviceStateLock) {
                    com.android.server.audio.AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().checkTimeoutInactiveRouteClient();
                }
            }
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public int getUidByPid(int pid) {
            return com.android.server.audio.AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().getUidByPid(pid);
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public int getLatestPreferredDeviceType() {
            return com.android.server.audio.AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().getLatestPreferredDeviceType();
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public int getLatestModeOwnerUid() {
            return com.android.server.audio.AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().getLatestModeOwnerUid();
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public int getLatestModeOwnerPid() {
            return com.android.server.audio.AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().getLatestModeOwnerPid();
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public android.os.Looper getBrokerLooper() {
            return com.android.server.audio.AudioDeviceBroker.this.mBrokerThread.getLooper();
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public android.bluetooth.BluetoothDevice getBluetoothDevice() {
            return com.android.server.audio.AudioDeviceBroker.this.mBtHelper.getBtDevice();
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void setBluetoothDevice(com.android.server.audio.AudioDeviceBroker.oplusBtDeviceInfo btDevice) {
            com.android.server.audio.AudioDeviceBroker.this.mBtHelper.setBtDevice(btDevice);
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public int getBleVolume(boolean cmpToSafeVolume, int bleVolume) {
            return com.android.server.audio.AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().getBleVolume(cmpToSafeVolume, bleVolume);
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public boolean getBluetoothVolSyncSupported() {
            return com.android.server.audio.AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().getBluetoothVolSyncSupported();
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void checkHoloDeviceSupportState(boolean isWiredHeadSet, boolean isConnect, boolean isBleDevice) {
            boolean isNeedCheck = false;
            if ((isConnect && !isWiredHeadSet) || ((isConnect && isWiredHeadSet && com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.isHoloBLeDeviceConnected()) || (!isConnect && com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.isHoloBtDeviceConnected()))) {
                isNeedCheck = true;
            }
            if (com.android.server.audio.AudioDeviceBroker.this.mDeviceInventory.isHoloVoipSupport() && isNeedCheck && (!isConnect || isWiredHeadSet)) {
                isBleDevice = com.android.server.audio.AudioDeviceBroker.this.mBtHelper.getActiveBtProfile() == 22;
            }
            com.android.server.audio.AudioDeviceBroker.this.mAudioService.getWrapper().setHoloDeviceSupportState(isNeedCheck, isBleDevice);
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public boolean isBluetoothLeTbsDeviceActive() {
            if (com.android.server.audio.AudioDeviceBroker.this.mAudioService != null && com.android.server.audio.AudioDeviceBroker.this.mAudioService.mAsSocExt != null) {
                return com.android.server.audio.AudioDeviceBroker.this.mAudioService.mAsSocExt.isBluetoothLeTbsDeviceActive();
            }
            return false;
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public boolean isVendorBeforeAndroidU() {
            return com.android.server.audio.AudioDeviceBroker.this.mAudioService.isVendorBeforeAndroidU();
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public boolean isDeviceActiveForCommunication(int deviceType) {
            return com.android.server.audio.AudioDeviceBroker.this.isDeviceActiveForCommunication(deviceType);
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void postPersistAudioHeadPhoneSettings(boolean headphoneEnabled) {
            if (com.android.server.audio.AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().getAudioEffectCombined()) {
                android.media.AudioSystem.setParameters("headphone_spat_enabled=" + headphoneEnabled);
            }
            com.android.server.audio.AudioDeviceBroker.this.sendLMsg(130, 0, java.lang.Boolean.valueOf(headphoneEnabled), 1000);
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void onPersistAudioHeadPhoneSettings(boolean z) {
            android.util.Log.v(com.android.server.audio.AudioDeviceBroker.TAG, "onPersistAudioHeadPhoneSettings headphoneState: " + (z ? 1 : 0));
            if (z == readAudioHeadPhoneSettings()) {
                return;
            }
            try {
                if (!com.android.server.audio.AudioDeviceBroker.this.mAudioService.getSettings().putSecureIntForUser(com.android.server.audio.AudioDeviceBroker.this.mAudioService.getContentResolver(), com.android.server.audio.AudioDeviceBroker.NAME_HEADPHONE_SPAT_ENABLED, z ? 1 : 0, -2)) {
                    android.util.Log.e(com.android.server.audio.AudioDeviceBroker.TAG, "error saving headphoneState: " + (z ? 1 : 0));
                }
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Log.e(com.android.server.audio.AudioDeviceBroker.TAG, "error saving headphoneState: " + (z ? 1 : 0), e);
            }
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public int readAudioHeadPhoneSettings() {
            com.android.server.audio.SettingsAdapter settingsAdapter = com.android.server.audio.AudioDeviceBroker.this.mAudioService.getSettings();
            android.content.ContentResolver contentResolver = com.android.server.audio.AudioDeviceBroker.this.mAudioService.getContentResolver();
            return settingsAdapter.getSecureIntForUser(contentResolver, com.android.server.audio.AudioDeviceBroker.NAME_HEADPHONE_SPAT_ENABLED, 0, -2);
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public boolean getAudioHeadPhoneStateFromSettings() {
            com.android.server.audio.SettingsAdapter settingsAdapter = com.android.server.audio.AudioDeviceBroker.this.mAudioService.getSettings();
            android.content.ContentResolver contentResolver = com.android.server.audio.AudioDeviceBroker.this.mAudioService.getContentResolver();
            int ret = settingsAdapter.getSecureIntForUser(contentResolver, com.android.server.audio.AudioDeviceBroker.NAME_HEADPHONE_SPAT_ENABLED, 0, -2);
            return ret == 1;
        }
    }
}
