package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class BtHelper {
    private static final int BT_DEVICE_NAME_LENGTH_MAX = 127;
    private static final int BT_HEARING_AID_GAIN_MIN = -128;
    private static final int BT_LE_AUDIO_MAX_VOL = 255;
    private static final int BT_LE_AUDIO_MIN_VOL = 0;
    private static final int CG_STATE_CHANGE_MSG_DELAY = 300;
    private static final java.lang.String DEVICE_TYPE_CARKIT = "Carkit";
    private static final java.lang.String DEVICE_TYPE_HEADSET = "Headset";
    private static final java.lang.String DEVICE_TYPE_HEARING_AID = "HearingAid";
    private static final java.lang.String DEVICE_TYPE_SPEAKER = "Speaker";
    static final int EVENT_DEVICE_CONFIG_CHANGE = 0;
    private static final int GROUP_ID_END = 15;
    private static final int GROUP_ID_START = 0;
    private static final int SCO_MODE_MAX = 2;
    static final int SCO_MODE_UNDEFINED = -1;
    static final int SCO_MODE_VIRTUAL_CALL = 0;
    private static final int SCO_MODE_VR = 2;
    private static final int SCO_STATE_ACTIVATE_REQ = 1;
    private static final int SCO_STATE_ACTIVE_EXTERNAL = 2;
    private static final int SCO_STATE_ACTIVE_INTERNAL = 3;
    private static final int SCO_STATE_DEACTIVATE_REQ = 4;
    private static final int SCO_STATE_DEACTIVATING = 5;
    private static final int SCO_STATE_INACTIVE = 0;
    private static final java.lang.String TAG = "AS.BtHelper";
    private static final java.lang.String UNNAMED_BLUETOOTH_DEVICE = "Unnamed Bluetooth Device";
    private static android.bluetooth.BluetoothDevice mBluetoothA2dpActiveDevice;
    private android.bluetooth.BluetoothCodecConfig mA2dpCodecConfig;
    private com.android.server.audio.AudioDeviceBroker.oplusBtDeviceInfo mBluetoothDevice;
    private android.bluetooth.BluetoothHeadset mBluetoothHeadset;
    private android.bluetooth.BluetoothDevice mBluetoothHeadsetDevice;
    private android.bluetooth.BluetoothDevice mBluetoothHeadsetDummyDevice;
    private com.soc.bt.BluetoothLeCallControl mBluetoothLeCallControl;
    private final android.content.Context mContext;
    private final com.android.server.audio.AudioDeviceBroker mDeviceBroker;
    private boolean mIsCgon;
    private android.bluetooth.BluetoothLeAudioCodecConfig mLeAudioCodecConfig;
    private int mScoAudioMode;
    private int mScoAudioState;
    private int mScoConnectionState;
    private final java.util.Map<android.bluetooth.BluetoothDevice, android.media.AudioDeviceAttributes> mResolvedScoAudioDevices = new java.util.HashMap();
    private android.bluetooth.BluetoothHearingAid mHearingAid = null;
    private android.bluetooth.BluetoothLeAudio mLeAudio = null;
    private android.bluetooth.BluetoothA2dp mA2dp = null;
    private int mLeAudioBroadcastCodec = 0;
    private boolean mAvrcpAbsVolSupported = false;
    private boolean mIsBluetoothHeadsetCreated = false;
    private final java.util.ArrayList<java.lang.String> mUnsupHoloVoipDeList = new java.util.ArrayList<>(java.util.Arrays.asList("OnePlus Buds Pro 2"));
    public com.android.server.audio.IBtHelperSocExt mBthSocExt = (com.android.server.audio.IBtHelperSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.audio.IBtHelperSocExt.class).base(this).create();
    private com.android.server.audio.IAudioDeviceInventoryExt mAdiExt = (com.android.server.audio.IAudioDeviceInventoryExt) system.ext.loader.core.ExtLoader.type(com.android.server.audio.IAudioDeviceInventoryExt.class).base(this).create();
    private java.util.HashMap<android.bluetooth.BluetoothDevice, java.lang.Integer> mScoClientDevices = new java.util.HashMap<>();
    com.android.server.audio.BtHelper.MyLeAudioCallback mLeAudioCallback = null;
    private android.bluetooth.BluetoothProfile.ServiceListener mBluetoothProfileServiceListener = new android.bluetooth.BluetoothProfile.ServiceListener() { // from class: com.android.server.audio.BtHelper.1
        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int profile, android.bluetooth.BluetoothProfile proxy) {
            android.util.Log.i(com.android.server.audio.BtHelper.TAG, "In onServiceConnected(), profile: " + profile + ", proxy: " + proxy);
            switch (profile) {
                case 1:
                case 2:
                case 11:
                case 21:
                case 22:
                case 26:
                case 27:
                    com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("BT profile service: connecting " + android.bluetooth.BluetoothProfile.getProfileName(profile) + " profile").printLog(com.android.server.audio.BtHelper.TAG));
                    com.android.server.audio.BtHelper.this.mDeviceBroker.postBtProfileConnected(profile, proxy);
                    break;
            }
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int profile) {
            switch (profile) {
                case 1:
                case 2:
                case 11:
                case 21:
                case 22:
                case 26:
                case 27:
                    com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("BT profile service: disconnecting " + android.bluetooth.BluetoothProfile.getProfileName(profile) + " profile").printLog(com.android.server.audio.BtHelper.TAG));
                    com.android.server.audio.BtHelper.this.mDeviceBroker.postBtProfileDisconnected(profile);
                    break;
            }
        }
    };

    BtHelper(com.android.server.audio.AudioDeviceBroker broker, android.content.Context context) {
        this.mDeviceBroker = broker;
        this.mContext = context;
    }

    public static java.lang.String scoAudioModeToString(int scoAudioMode) {
        switch (scoAudioMode) {
            case -1:
                return "SCO_MODE_UNDEFINED";
            case 0:
                return "SCO_MODE_VIRTUAL_CALL";
            case 1:
            default:
                return "SCO_MODE_(" + scoAudioMode + ")";
            case 2:
                return "SCO_MODE_VR";
        }
    }

    public static java.lang.String scoAudioStateToString(int scoAudioState) {
        switch (scoAudioState) {
            case 0:
                return "SCO_STATE_INACTIVE";
            case 1:
                return "SCO_STATE_ACTIVATE_REQ";
            case 2:
                return "SCO_STATE_ACTIVE_EXTERNAL";
            case 3:
                return "SCO_STATE_ACTIVE_INTERNAL";
            case 4:
            default:
                return "SCO_STATE_(" + scoAudioState + ")";
            case 5:
                return "SCO_STATE_DEACTIVATING";
        }
    }

    static java.lang.String deviceEventToString(int event) {
        switch (event) {
            case 0:
                return "DEVICE_CONFIG_CHANGE";
            default:
                return new java.lang.String("invalid event:" + event);
        }
    }

    static java.lang.String getName(android.bluetooth.BluetoothDevice device) {
        java.lang.String deviceName = device.getName();
        if (deviceName == null) {
            return "";
        }
        if (deviceName.getBytes().length > 127) {
            return UNNAMED_BLUETOOTH_DEVICE;
        }
        return deviceName;
    }

    static void SetA2dpActiveDevice(android.bluetooth.BluetoothDevice device) {
        android.util.Log.w(TAG, "SetA2dpActiveDevice for TWS+ pair as " + device);
        mBluetoothA2dpActiveDevice = device;
    }

    static boolean isTwsPlusSwitch(android.bluetooth.BluetoothDevice device, java.lang.String address) {
        android.bluetooth.BluetoothAdapter adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        android.bluetooth.BluetoothDevice connDevice = adapter.getRemoteDevice(address);
        if (device == null || connDevice == null || device.getTwsPlusPeerAddress() == null || !device.isTwsPlusDevice() || !connDevice.isTwsPlusDevice() || !device.getTwsPlusPeerAddress().equals(address)) {
            return false;
        }
        if (mBluetoothA2dpActiveDevice == null) {
            android.util.Log.w(TAG, "Not a TwsPlusSwitch as previous active device was null");
            return false;
        }
        android.util.Log.i(TAG, "isTwsPlusSwitch true");
        return true;
    }

    synchronized void onSystemReady() {
        this.mScoConnectionState = -1;
        android.util.Log.i(TAG, "In onSystemReady(), calling resetBluetoothSco()");
        resetBluetoothSco();
        getBluetoothHeadset();
        android.content.Intent newIntent = new android.content.Intent("android.media.SCO_AUDIO_STATE_CHANGED");
        newIntent.putExtra("android.media.extra.SCO_AUDIO_STATE", 0);
        sendStickyBroadcastToAll(newIntent);
        android.bluetooth.BluetoothAdapter adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            adapter.getProfileProxy(this.mDeviceBroker.getContext(), this.mBluetoothProfileServiceListener, 2);
            adapter.getProfileProxy(this.mDeviceBroker.getContext(), this.mBluetoothProfileServiceListener, 11);
            adapter.getProfileProxy(this.mDeviceBroker.getContext(), this.mBluetoothProfileServiceListener, 21);
            adapter.getProfileProxy(this.mDeviceBroker.getContext(), this.mBluetoothProfileServiceListener, 22);
            if (!android.os.Build.isMtkPlatform() || !this.mDeviceBroker.getWrapper().isVendorBeforeAndroidU()) {
                adapter.getProfileProxy(this.mDeviceBroker.getContext(), this.mBluetoothProfileServiceListener, 27);
            }
            adapter.getProfileProxy(this.mDeviceBroker.getContext(), this.mBluetoothProfileServiceListener, 26);
        }
    }

    synchronized void onAudioServerDiedRestoreA2dp() {
        int forMed = this.mDeviceBroker.getBluetoothA2dpEnabled() ? 0 : 10;
        this.mDeviceBroker.setForceUse_Async(1, forMed, "onAudioServerDied()");
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x000b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    synchronized boolean isAvrcpAbsoluteVolumeSupported() {
        /*
            r1 = this;
            monitor-enter(r1)
            android.bluetooth.BluetoothA2dp r0 = r1.mA2dp     // Catch: java.lang.Throwable -> Le
            if (r0 == 0) goto Lb
            boolean r0 = r1.mAvrcpAbsVolSupported     // Catch: java.lang.Throwable -> Le
            if (r0 == 0) goto Lb
            r0 = 1
            goto Lc
        Lb:
            r0 = 0
        Lc:
            monitor-exit(r1)
            return r0
        Le:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.BtHelper.isAvrcpAbsoluteVolumeSupported():boolean");
    }

    synchronized void setAvrcpAbsoluteVolumeSupported(boolean supported) {
        this.mAvrcpAbsVolSupported = supported;
        android.util.Log.i(TAG, "setAvrcpAbsoluteVolumeSupported supported=" + supported);
        this.mAdiExt.postSyncA2dpVolume(supported);
    }

    synchronized void setAvrcpAbsoluteVolumeIndex(int index) {
        if (this.mA2dp == null) {
            if (com.android.server.audio.AudioService.DEBUG_VOL) {
                com.android.server.audio.AudioService.sVolumeLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("setAvrcpAbsoluteVolumeIndex: bailing due to null mA2dp").printLog(TAG));
            }
        } else {
            if (!this.mAvrcpAbsVolSupported) {
                com.android.server.audio.AudioService.sVolumeLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("setAvrcpAbsoluteVolumeIndex: abs vol not supported ").printLog(TAG));
                return;
            }
            if (com.android.server.audio.AudioService.DEBUG_VOL) {
                android.util.Log.i(TAG, "setAvrcpAbsoluteVolumeIndex index=" + index);
            }
            com.android.server.audio.AudioService.sVolumeLogger.enqueue(new com.android.server.audio.AudioServiceEvents.VolumeEvent(4, index));
            try {
                this.mA2dp.setAvrcpAbsoluteVolume(index);
            } catch (java.lang.Exception e) {
                android.util.Log.e(TAG, "Exception while changing abs volume", e);
            }
        }
    }

    synchronized boolean isNextBtActiveDeviceAvailableForMusic() {
        boolean status;
        status = this.mBthSocExt.isNextBtActiveDeviceAvailableForMusic(this.mA2dp, this.mLeAudio);
        return status;
    }

    private synchronized android.util.Pair<java.lang.Integer, java.lang.Boolean> getCodec(android.bluetooth.BluetoothDevice device, int profile) {
        boolean changed = true;
        switch (profile) {
            case 2:
                boolean changed2 = this.mA2dpCodecConfig != null;
                if (this.mA2dp == null) {
                    this.mA2dpCodecConfig = null;
                    return new android.util.Pair<>(0, java.lang.Boolean.valueOf(changed2));
                }
                android.bluetooth.BluetoothCodecStatus btCodecStatus = null;
                try {
                    btCodecStatus = this.mA2dp.getCodecStatus(device);
                } catch (java.lang.Exception e) {
                    android.util.Log.e(TAG, "Exception while getting status of " + device, e);
                }
                if (btCodecStatus == null) {
                    android.util.Log.e(TAG, "getCodec, null A2DP codec status for device: " + device);
                    this.mA2dpCodecConfig = null;
                    return new android.util.Pair<>(0, java.lang.Boolean.valueOf(changed2));
                }
                android.bluetooth.BluetoothCodecConfig btCodecConfig = btCodecStatus.getCodecConfig();
                if (btCodecConfig == null) {
                    this.mA2dpCodecConfig = null;
                    return new android.util.Pair<>(0, java.lang.Boolean.valueOf(changed2));
                }
                if (btCodecConfig.equals(this.mA2dpCodecConfig)) {
                    changed = false;
                }
                this.mA2dpCodecConfig = btCodecConfig;
                return new android.util.Pair<>(java.lang.Integer.valueOf(android.media.AudioSystem.bluetoothA2dpCodecToAudioFormat(btCodecConfig.getCodecType())), java.lang.Boolean.valueOf(changed));
            case 22:
                boolean changed3 = this.mLeAudioCodecConfig != null;
                if (this.mLeAudio == null) {
                    this.mLeAudioCodecConfig = null;
                    return new android.util.Pair<>(0, java.lang.Boolean.valueOf(changed3));
                }
                android.bluetooth.BluetoothLeAudioCodecStatus btLeCodecStatus = null;
                int groupId = this.mLeAudio.getGroupId(device);
                try {
                    btLeCodecStatus = this.mLeAudio.getCodecStatus(groupId);
                } catch (java.lang.Exception e2) {
                    android.util.Log.e(TAG, "Exception while getting status of " + device, e2);
                }
                if (btLeCodecStatus == null) {
                    android.util.Log.e(TAG, "getCodec, null LE codec status for device: " + device);
                    this.mLeAudioCodecConfig = null;
                    return new android.util.Pair<>(0, java.lang.Boolean.valueOf(changed3));
                }
                android.bluetooth.BluetoothLeAudioCodecConfig btLeCodecConfig = btLeCodecStatus.getOutputCodecConfig();
                if (btLeCodecConfig == null) {
                    this.mLeAudioCodecConfig = null;
                    return new android.util.Pair<>(0, java.lang.Boolean.valueOf(changed3));
                }
                if (btLeCodecConfig.equals(this.mLeAudioCodecConfig)) {
                    changed = false;
                }
                this.mLeAudioCodecConfig = btLeCodecConfig;
                return new android.util.Pair<>(java.lang.Integer.valueOf(android.media.AudioSystem.bluetoothLeCodecToAudioFormat(btLeCodecConfig.getCodecType())), java.lang.Boolean.valueOf(changed));
            case 26:
                if (this.mLeAudioBroadcastCodec == 721420288) {
                    changed = false;
                }
                this.mLeAudioBroadcastCodec = 721420288;
                return new android.util.Pair<>(java.lang.Integer.valueOf(this.mLeAudioBroadcastCodec), java.lang.Boolean.valueOf(changed));
            default:
                return new android.util.Pair<>(0, false);
        }
    }

    synchronized android.util.Pair<java.lang.Integer, java.lang.Boolean> getCodecWithFallback(android.bluetooth.BluetoothDevice device, int profile, boolean isLeOutput, java.lang.String source) {
        if (profile != 2 && (!isLeOutput || (profile != 22 && profile != 26))) {
            return new android.util.Pair<>(0, false);
        }
        android.util.Pair<java.lang.Integer, java.lang.Boolean> codecAndChanged = getCodec(device, profile);
        if (((java.lang.Integer) codecAndChanged.first).intValue() != 0) {
            return codecAndChanged;
        }
        com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("getCodec DEFAULT from " + source + " fallback to " + (profile == 2 ? "SBC" : "LC3")));
        return new android.util.Pair<>(java.lang.Integer.valueOf(profile == 2 ? android.hardware.audio.common.V2_0.AudioFormat.SBC : 721420288), true);
    }

    private void updateTwsPlusScoState(android.bluetooth.BluetoothDevice device, java.lang.Integer state) {
        if (this.mScoClientDevices.containsKey(device)) {
            java.lang.Integer prevState = this.mScoClientDevices.get(device);
            android.util.Log.i(TAG, "updateTwsPlusScoState: prevState: " + prevState + "state: " + state);
            if (state != prevState) {
                this.mScoClientDevices.remove(device);
                this.mScoClientDevices.put(device, state);
                return;
            }
            return;
        }
        this.mScoClientDevices.put(device, state);
    }

    private boolean isAudioPathUp() {
        boolean ret = false;
        this.mScoClientDevices.entrySet().iterator();
        java.util.Iterator<java.lang.Integer> it = this.mScoClientDevices.values().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            java.lang.Integer value = it.next();
            if (value.intValue() == 12) {
                ret = true;
                break;
            }
        }
        android.util.Log.d(TAG, "isAudioPathUp returns" + ret);
        return ret;
    }

    private boolean checkAndUpdatTwsPlusScoState(android.content.Intent intent, java.lang.Integer state) {
        boolean ret = true;
        android.bluetooth.BluetoothDevice device = (android.bluetooth.BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
        android.util.Log.i(TAG, "device:" + device);
        if (device == null) {
            android.util.Log.e(TAG, "checkAndUpdatTwsPlusScoState: device is null");
            return true;
        }
        if (device.isTwsPlusDevice()) {
            if (state.intValue() == 12) {
                if (isAudioPathUp()) {
                    android.util.Log.i(TAG, "No need to bringup audio-path");
                    ret = false;
                }
                updateTwsPlusScoState(device, state);
            } else {
                updateTwsPlusScoState(device, state);
                if (isAudioPathUp()) {
                    android.util.Log.i(TAG, "not good to tear down audio-path");
                    ret = false;
                }
            }
        }
        android.util.Log.i(TAG, "checkAndUpdatTwsPlusScoState returns " + ret);
        return ret;
    }

    private boolean isGroupDevice(android.bluetooth.BluetoothDevice device) {
        int type = device.getDeviceType();
        boolean ret = false;
        android.util.Log.i(TAG, "Bluetooth device type: " + type);
        if (type >= 0 && type <= 15) {
            ret = true;
        }
        android.util.Log.i(TAG, "isGroupDevice return " + ret);
        return ret;
    }

    private void updateGroupScoState(android.bluetooth.BluetoothDevice device, java.lang.Integer state) {
        if (this.mScoClientDevices.containsKey(device)) {
            java.lang.Integer prevState = this.mScoClientDevices.get(device);
            android.util.Log.i(TAG, "updateGroupScoState: prevState: " + prevState + "state: " + state);
            if (state != prevState) {
                this.mScoClientDevices.remove(device);
                this.mScoClientDevices.put(device, state);
                return;
            }
            return;
        }
        this.mScoClientDevices.put(device, state);
    }

    private boolean checkAndUpdateGroupScoState(android.content.Intent intent, java.lang.Integer state) {
        boolean ret = true;
        android.bluetooth.BluetoothDevice device = (android.bluetooth.BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
        android.util.Log.i(TAG, "device:" + device);
        if (device == null) {
            android.util.Log.e(TAG, "checkAndUpdateGroupScoState: device is null");
            return true;
        }
        if (isGroupDevice(device)) {
            if (state.intValue() == 12) {
                if (isAudioPathUp()) {
                    android.util.Log.i(TAG, "No need to bringup audio-path");
                    ret = false;
                }
                updateGroupScoState(device, state);
            } else {
                updateGroupScoState(device, state);
                if (isAudioPathUp()) {
                    android.util.Log.i(TAG, "not good to tear down audio-path");
                    ret = false;
                }
            }
        }
        android.util.Log.i(TAG, "checkAndUpdateGroupScoState returns " + ret);
        return ret;
    }

    synchronized void onReceiveBtEvent(android.content.Intent intent) {
        java.lang.String action = intent.getAction();
        android.util.Log.i(TAG, "onReceiveBtEvent action: " + action + " mScoAudioState: " + this.mScoAudioState);
        if (action.equals("android.bluetooth.headset.profile.action.ACTIVE_DEVICE_CHANGED")) {
            android.bluetooth.BluetoothDevice btDevice = (android.bluetooth.BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE", android.bluetooth.BluetoothDevice.class);
            java.lang.String broadcastType = intent.getStringExtra("android.bluetooth.device.extra.NAME");
            if (android.os.Build.isMtkPlatform() && broadcastType != null && "fake_hfp_broadcast".equals(broadcastType)) {
                if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                    android.util.Log.d(TAG, "Fake HFP active device broadcast,return");
                }
            } else {
                if (btDevice != null && !isProfilePoxyConnected(1)) {
                    com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("onReceiveBtEvent ACTION_ACTIVE_DEVICE_CHANGED received with null profile proxy for device: " + btDevice).printLog(TAG));
                    return;
                }
                onSetBtScoActiveDevice(btDevice);
            }
        } else if (action.equals("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED")) {
            int btState = intent.getIntExtra("android.bluetooth.profile.extra.STATE", -1);
            java.lang.String btStateInfo = btState == 12 ? "AudioConnected" : "AudioDisconnected";
            android.util.Log.i(TAG, "receiveBtEvent ACTION_AUDIO_STATE_CHANGED: btState=" + btState + "{" + btStateInfo + "}");
            if (android.os.Build.isQcomPlatform()) {
                if (checkAndUpdatTwsPlusScoState(intent, java.lang.Integer.valueOf(btState)) && checkAndUpdateGroupScoState(intent, java.lang.Integer.valueOf(btState))) {
                    onScoAudioStateChanged(btState);
                }
            } else {
                onScoAudioStateChanged(btState);
            }
        }
    }

    private void onScoAudioStateChanged(int state) {
        boolean broadcast = false;
        int scoAudioState = -1;
        if (this.mDeviceBroker.isScoManagedByAudio()) {
            switch (state) {
                case 10:
                    this.mDeviceBroker.setBluetoothScoOn(false, "BtHelper.onScoAudioStateChanged");
                    scoAudioState = 0;
                    broadcast = true;
                    break;
                case 12:
                    this.mDeviceBroker.setBluetoothScoOn(true, "BtHelper.onScoAudioStateChanged");
                    scoAudioState = 1;
                    broadcast = true;
                    break;
            }
        } else {
            switch (state) {
                case 10:
                    this.mDeviceBroker.setBluetoothScoOn(false, "BtHelper.onScoAudioStateChanged");
                    scoAudioState = 0;
                    if (this.mScoAudioState == 1 && this.mBluetoothHeadset != null && this.mBluetoothHeadsetDevice != null && connectBluetoothScoAudioHelper(this.mBluetoothHeadset, this.mBluetoothHeadsetDevice, this.mScoAudioMode)) {
                        this.mScoAudioState = 3;
                        scoAudioState = 2;
                        broadcast = true;
                    } else {
                        if (this.mScoAudioState != 2) {
                            broadcast = true;
                        }
                        this.mScoAudioState = 0;
                    }
                    break;
                case 11:
                    if (this.mScoAudioState != 3 && this.mScoAudioState != 4) {
                        this.mScoAudioState = 2;
                    }
                    break;
                case 12:
                    scoAudioState = 1;
                    if (this.mScoAudioState != 3 && this.mScoAudioState != 4) {
                        this.mScoAudioState = 2;
                    } else if (this.mDeviceBroker.isBluetoothScoRequested()) {
                        broadcast = true;
                    }
                    this.mDeviceBroker.setBluetoothScoOn(true, "BtHelper.onScoAudioStateChanged");
                    break;
            }
        }
        if (broadcast) {
            broadcastScoConnectionState(scoAudioState);
            if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                android.util.Log.d(TAG, "receiveBtEvent(): BR SCOAudioStateChanged, scoAudioState=" + scoAudioState);
            }
            android.content.Intent newIntent = new android.content.Intent("android.media.SCO_AUDIO_STATE_CHANGED");
            newIntent.putExtra("android.media.extra.SCO_AUDIO_STATE", scoAudioState);
            sendStickyBroadcastToAll(newIntent);
        }
    }

    boolean isBluetoothAudioNotConnectedToEarbud() {
        java.lang.String pDevAddr;
        boolean ret = true;
        if (this.mBluetoothHeadsetDevice != null && this.mBluetoothHeadsetDevice.isTwsPlusDevice() && (pDevAddr = this.mBluetoothHeadsetDevice.getTwsPlusPeerAddress()) != null) {
            android.bluetooth.BluetoothAdapter adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
            android.bluetooth.BluetoothDevice peerDev = adapter.getRemoteDevice(pDevAddr);
            android.util.Log.d(TAG, "peer device audio State: " + this.mBluetoothHeadset.getAudioState(peerDev));
            if (this.mBluetoothHeadset.getAudioState(peerDev) == 12 || this.mBluetoothHeadset.getAudioState(this.mBluetoothHeadsetDevice) == 12) {
                android.util.Log.w(TAG, "TwsPLus Case: one of eb SCO is connected");
                ret = false;
            }
        }
        android.util.Log.d(TAG, "isBluetoothAudioConnectedToEarbud returns: " + ret);
        return ret;
    }

    synchronized boolean isBluetoothScoOn() {
        if (this.mBluetoothHeadset != null && this.mBluetoothHeadsetDevice != null) {
            return this.mBluetoothHeadset.getAudioState(this.mBluetoothHeadsetDevice) == 12;
        }
        return false;
    }

    boolean isAudioConnected() {
        return (this.mBluetoothHeadset == null || this.mBluetoothHeadsetDevice == null || this.mBluetoothHeadset.getAudioState(this.mBluetoothHeadsetDevice) != 12) ? false : true;
    }

    synchronized boolean isInbandRingingEnabled() {
        boolean status;
        status = false;
        if (this.mBluetoothHeadset != null && this.mBluetoothHeadsetDevice != null) {
            status = this.mBluetoothHeadset.isInbandRingingEnabled();
        }
        if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
            android.util.Log.d(TAG, "isInbandRingingEnabled() = " + status);
        }
        return status;
    }

    synchronized boolean startBluetoothSco(int scoAudioMode, java.lang.String eventSource) {
        com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(eventSource));
        return requestScoState(12, scoAudioMode);
    }

    synchronized boolean stopBluetoothSco(java.lang.String eventSource) {
        com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(eventSource));
        return requestScoState(10, 0);
    }

    void updateBleCGStateToBt(boolean z) {
        boolean z2 = false;
        if (this.mLeAudio == null || this.mBluetoothLeCallControl == null) {
            android.util.Log.d(TAG, "mLeAudio: null mLeAudio or null mBluetoothLeCallControl");
            this.mIsCgon = false;
            return;
        }
        if (com.android.server.audio.AudioService.DEBUG_VOL) {
            android.util.Log.i(TAG, "updateBleCGStateToBt, isCgOn:" + z + ", mIsCgon:" + this.mIsCgon);
        }
        if (z != this.mIsCgon) {
            this.mLeAudio.notifyBluetoothCallState(z);
            z2 = true;
        }
        this.mIsCgon = z;
        if (!android.os.Build.isMtkPlatform() || !this.mDeviceBroker.getWrapper().isVendorBeforeAndroidU()) {
            if (com.android.server.audio.AudioService.DEBUG_VOL) {
                android.util.Log.d(TAG, "updateBleCGStateToBt: isCgOn" + z);
            }
            if (z2) {
                this.mDeviceBroker.postBleCgStateChange(z ? 1 : 0, 300);
            }
            if (z) {
                this.mBluetoothLeCallControl.connectCgAudio();
            } else {
                this.mBluetoothLeCallControl.disconnectCgAudio();
            }
        }
    }

    synchronized void setLeAudioVolume(int index, int maxIndex, int streamType) {
        if (this.mLeAudio == null) {
            if (com.android.server.audio.AudioService.DEBUG_VOL) {
                android.util.Log.i(TAG, "setLeAudioVolume: null mLeAudio");
            }
            return;
        }
        int volume = (int) java.lang.Math.round((((double) index) * 255.0d) / ((double) maxIndex));
        if (com.android.server.audio.AudioService.DEBUG_VOL) {
            android.util.Log.i(TAG, "setLeAudioVolume: calling mLeAudio.setVolume idx=" + index + " volume=" + volume);
        }
        com.android.server.audio.AudioService.sVolumeLogger.enqueue(new com.android.server.audio.AudioServiceEvents.VolumeEvent(10, streamType, index, maxIndex, (java.lang.String) null));
        try {
            this.mLeAudio.setVolume(volume);
        } catch (java.lang.Exception e) {
            android.util.Log.e(TAG, "Exception while setting LE volume", e);
        }
    }

    synchronized void setHearingAidVolume(int index, int streamType, boolean isHeadAidConnected) {
        if (this.mHearingAid == null) {
            if (com.android.server.audio.AudioService.DEBUG_VOL) {
                android.util.Log.i(TAG, "setHearingAidVolume: null mHearingAid");
            }
            return;
        }
        int gainDB = (int) android.media.AudioSystem.getStreamVolumeDB(streamType, index / 10, 134217728);
        if (gainDB < -128) {
            gainDB = -128;
        }
        if (com.android.server.audio.AudioService.DEBUG_VOL) {
            android.util.Log.i(TAG, "setHearingAidVolume: calling mHearingAid.setVolume idx=" + index + " gain=" + gainDB);
        }
        if (isHeadAidConnected) {
            com.android.server.audio.AudioService.sVolumeLogger.enqueue(new com.android.server.audio.AudioServiceEvents.VolumeEvent(3, index, gainDB));
        }
        try {
            this.mHearingAid.setVolume(gainDB);
        } catch (java.lang.Exception e) {
            android.util.Log.i(TAG, "Exception while setting hearing aid volume", e);
        }
    }

    synchronized void onBroadcastScoConnectionState(int state) {
        if (state == this.mScoConnectionState) {
            return;
        }
        android.content.Intent newIntent = new android.content.Intent("android.media.ACTION_SCO_AUDIO_STATE_UPDATED");
        newIntent.putExtra("android.media.extra.SCO_AUDIO_STATE", state);
        newIntent.putExtra("android.media.extra.SCO_AUDIO_PREVIOUS_STATE", this.mScoConnectionState);
        sendStickyBroadcastToAll(newIntent);
        this.mScoConnectionState = state;
    }

    synchronized void resetBluetoothSco() {
        android.util.Log.i(TAG, "In resetBluetoothSco(), calling clearAllScoClients()");
        this.mScoAudioState = 0;
        broadcastScoConnectionState(0);
        if (android.os.Build.isMtkPlatform()) {
            android.media.AudioSystem.setParameters("BTAudiosuspend=false");
        }
        this.mDeviceBroker.clearA2dpSuspended(false);
        this.mDeviceBroker.clearLeAudioSuspended(false);
        if (android.os.Build.isQcomPlatform()) {
            this.mScoClientDevices.clear();
        }
        this.mDeviceBroker.setBluetoothScoOn(false, "resetBluetoothSco");
    }

    synchronized void onBtProfileDisconnected(int profile) {
        com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("BT profile " + android.bluetooth.BluetoothProfile.getProfileName(profile) + " disconnected").printLog(TAG));
        switch (profile) {
            case 1:
                this.mBluetoothHeadset = null;
                break;
            case 2:
                this.mA2dp = null;
                this.mA2dpCodecConfig = null;
                break;
            case 11:
                break;
            case 21:
                this.mHearingAid = null;
                break;
            case 22:
                if (this.mLeAudio != null && this.mLeAudioCallback != null) {
                    this.mLeAudio.unregisterCallback(this.mLeAudioCallback);
                }
                this.mLeAudio = null;
                this.mLeAudioCallback = null;
                this.mLeAudioCodecConfig = null;
                break;
            case 26:
                this.mLeAudioBroadcastCodec = 0;
                break;
            case 27:
                this.mBluetoothLeCallControl = null;
                break;
            default:
                android.util.Log.e(TAG, "onBtProfileDisconnected: Not a valid profile to disconnect " + android.bluetooth.BluetoothProfile.getProfileName(profile));
                break;
        }
    }

    class MyLeAudioCallback implements android.bluetooth.BluetoothLeAudio.Callback {
        MyLeAudioCallback() {
        }

        public void onCodecConfigChanged(int groupId, android.bluetooth.BluetoothLeAudioCodecStatus status) {
        }

        public void onGroupNodeAdded(android.bluetooth.BluetoothDevice device, int groupId) {
            com.android.server.audio.BtHelper.this.mDeviceBroker.postUpdateLeAudioGroupAddresses(groupId);
        }

        public void onGroupNodeRemoved(android.bluetooth.BluetoothDevice device, int groupId) {
            com.android.server.audio.BtHelper.this.mDeviceBroker.postUpdateLeAudioGroupAddresses(groupId);
        }

        public void onGroupStatusChanged(int groupId, int groupStatus) {
            com.android.server.audio.BtHelper.this.mDeviceBroker.postUpdateLeAudioGroupAddresses(groupId);
        }
    }

    synchronized void onBtProfileConnected(int profile, android.bluetooth.BluetoothProfile proxy) {
        com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("BT profile " + android.bluetooth.BluetoothProfile.getProfileName(profile) + " connected to proxy " + proxy).printLog(TAG));
        if (proxy == null) {
            android.util.Log.e(TAG, "onBtProfileConnected: null proxy for profile: " + profile);
            return;
        }
        switch (profile) {
            case 1:
                onHeadsetProfileConnected((android.bluetooth.BluetoothHeadset) proxy);
                return;
            case 2:
                if (((android.bluetooth.BluetoothA2dp) proxy).equals(this.mA2dp)) {
                    return;
                } else {
                    this.mA2dp = (android.bluetooth.BluetoothA2dp) proxy;
                }
                break;
            case 11:
            case 26:
                return;
            case 21:
                if (((android.bluetooth.BluetoothHearingAid) proxy).equals(this.mHearingAid)) {
                    return;
                } else {
                    this.mHearingAid = (android.bluetooth.BluetoothHearingAid) proxy;
                }
                break;
            case 22:
                if (((android.bluetooth.BluetoothLeAudio) proxy).equals(this.mLeAudio)) {
                    return;
                }
                if (this.mLeAudio != null && this.mLeAudioCallback != null) {
                    this.mLeAudio.unregisterCallback(this.mLeAudioCallback);
                }
                this.mLeAudio = (android.bluetooth.BluetoothLeAudio) proxy;
                this.mLeAudioCallback = new com.android.server.audio.BtHelper.MyLeAudioCallback();
                this.mLeAudio.registerCallback(this.mContext.getMainExecutor(), this.mLeAudioCallback);
                break;
            case 27:
                if (com.android.server.audio.AudioService.DEBUG_VOL) {
                    android.util.Log.i(TAG, "BLE CALL CONTROL Profile Connected, proxy=" + proxy);
                }
                this.mBluetoothLeCallControl = (com.soc.bt.BluetoothLeCallControl) proxy;
                break;
            default:
                android.util.Log.e(TAG, "onBtProfileConnected: Not a valid profile to connect " + android.bluetooth.BluetoothProfile.getProfileName(profile));
                return;
        }
        if (profile != 4 && profile != 27) {
            android.bluetooth.BluetoothAdapter adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
            if (adapter == null) {
                android.util.Log.e(TAG, "onBtProfileConnected: Null BluetoothAdapter when connecting profile: " + android.bluetooth.BluetoothProfile.getProfileName(profile));
                return;
            }
            java.util.List<android.bluetooth.BluetoothDevice> activeDevices = adapter.getActiveDevices(profile);
            if (!activeDevices.isEmpty() && activeDevices.get(0) != null) {
                android.bluetooth.BluetoothDevice device = activeDevices.get(0);
                switch (profile) {
                    case 2:
                        android.media.BluetoothProfileConnectionInfo bpci = android.media.BluetoothProfileConnectionInfo.createA2dpInfo(false, -1);
                        postBluetoothActiveDevice(device, bpci);
                        return;
                    case 21:
                        android.media.BluetoothProfileConnectionInfo bpci2 = android.media.BluetoothProfileConnectionInfo.createHearingAidInfo(false);
                        postBluetoothActiveDevice(device, bpci2);
                        return;
                    case 22:
                        int groupId = this.mLeAudio.getGroupId(device);
                        android.bluetooth.BluetoothLeAudioCodecStatus btLeCodecStatus = null;
                        try {
                            btLeCodecStatus = this.mLeAudio.getCodecStatus(groupId);
                        } catch (java.lang.Exception e) {
                            android.util.Log.e(TAG, "Exception while getting status of " + device, e);
                        }
                        if (btLeCodecStatus == null) {
                            android.util.Log.i(TAG, "onBtProfileConnected null LE codec status for groupId: " + groupId + ", device: " + device);
                        } else {
                            java.util.List<android.bluetooth.BluetoothLeAudioCodecConfig> outputCodecConfigs = btLeCodecStatus.getOutputCodecSelectableCapabilities();
                            if (!outputCodecConfigs.isEmpty()) {
                                android.media.BluetoothProfileConnectionInfo bpci3 = android.media.BluetoothProfileConnectionInfo.createLeAudioInfo(false, true);
                                postBluetoothActiveDevice(device, bpci3);
                            }
                            java.util.List<android.bluetooth.BluetoothLeAudioCodecConfig> inputCodecConfigs = btLeCodecStatus.getInputCodecSelectableCapabilities();
                            if (!inputCodecConfigs.isEmpty()) {
                                android.media.BluetoothProfileConnectionInfo bpci4 = android.media.BluetoothProfileConnectionInfo.createLeAudioInfo(false, false);
                                postBluetoothActiveDevice(device, bpci4);
                            }
                        }
                        return;
                    default:
                        android.util.Log.wtf(TAG, "Invalid profile! onBtProfileConnected");
                        return;
                }
            }
        }
    }

    private void postBluetoothActiveDevice(android.bluetooth.BluetoothDevice device, android.media.BluetoothProfileConnectionInfo bpci) {
        com.android.server.audio.AudioDeviceBroker.BtDeviceChangedData data = new com.android.server.audio.AudioDeviceBroker.BtDeviceChangedData(device, null, bpci, "mBluetoothProfileServiceListener");
        com.android.server.audio.AudioDeviceBroker.BtDeviceInfo info = this.mDeviceBroker.createBtDeviceInfo(data, device, 2);
        this.mDeviceBroker.postBluetoothActiveDevice(info, 0);
    }

    synchronized boolean isProfilePoxyConnected(int profile) {
        switch (profile) {
            case 1:
                return this.mBluetoothHeadset != null;
            case 2:
                return this.mA2dp != null;
            case 21:
                return this.mHearingAid != null;
            case 22:
                return this.mLeAudio != null;
            default:
                return true;
        }
    }

    private void onHeadsetProfileConnected(android.bluetooth.BluetoothHeadset headset) {
        this.mDeviceBroker.handleCancelFailureToConnectToBtHeadsetService();
        this.mBluetoothHeadset = headset;
        android.bluetooth.BluetoothAdapter adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            java.util.List<android.bluetooth.BluetoothDevice> activeDevices = adapter.getActiveDevices(1);
            for (android.bluetooth.BluetoothDevice device : activeDevices) {
                if (device != null) {
                    onSetBtScoActiveDevice(device);
                }
            }
        } else {
            android.util.Log.e(TAG, "onHeadsetProfileConnected: Null BluetoothAdapter");
        }
        checkScoAudioState();
        if (this.mScoAudioState != 1 && this.mScoAudioState != 4) {
            return;
        }
        boolean status = false;
        if (this.mBluetoothHeadsetDevice != null) {
            switch (this.mScoAudioState) {
                case 1:
                    status = connectBluetoothScoAudioHelper(this.mBluetoothHeadset, this.mBluetoothHeadsetDevice, this.mScoAudioMode);
                    if (status) {
                        this.mScoAudioState = 3;
                    }
                    break;
                case 4:
                    status = disconnectBluetoothScoAudioHelper(this.mBluetoothHeadset, this.mBluetoothHeadsetDevice, this.mScoAudioMode);
                    if (status) {
                        this.mScoAudioState = 5;
                    }
                    break;
            }
        }
        if (!status) {
            this.mScoAudioState = 0;
            broadcastScoConnectionState(0);
        }
    }

    private void broadcastScoConnectionState(int state) {
        this.mDeviceBroker.postBroadcastScoConnectionState(state);
    }

    android.media.AudioDeviceAttributes getHeadsetAudioDevice() {
        if (this.mBluetoothHeadsetDevice == null) {
            return null;
        }
        return getHeadsetAudioDevice(this.mBluetoothHeadsetDevice);
    }

    private android.media.AudioDeviceAttributes getHeadsetAudioDevice(android.bluetooth.BluetoothDevice btDevice) {
        android.media.AudioDeviceAttributes deviceAttr = this.mResolvedScoAudioDevices.get(btDevice);
        if (deviceAttr != null) {
            return deviceAttr;
        }
        return btHeadsetDeviceToAudioDevice(btDevice);
    }

    android.media.AudioDeviceAttributes getHeadsetAudioDummyDevice() {
        if (this.mBluetoothHeadsetDummyDevice == null) {
            return null;
        }
        return btHeadsetDeviceToAudioDevice(this.mBluetoothHeadsetDummyDevice);
    }

    private static android.media.AudioDeviceAttributes btHeadsetDeviceToAudioDevice(android.bluetooth.BluetoothDevice btDevice) {
        android.bluetooth.BluetoothClass btClass;
        if (btDevice == null) {
            return new android.media.AudioDeviceAttributes(16, "");
        }
        java.lang.String address = btDevice.getAddress();
        java.lang.String name = "";
        if (!address.equals("00:00:00:00:00:00")) {
            name = getName(btDevice);
        }
        if (!android.bluetooth.BluetoothAdapter.checkBluetoothAddress(address)) {
            address = "";
        }
        if (android.os.Build.isQcomPlatform()) {
            btClass = "00:00:00:00:00:00".equals(address) ? null : btDevice.getBluetoothClass();
        } else {
            btClass = btDevice.getBluetoothClass();
        }
        int nativeType = 16;
        if (btClass != null) {
            switch (btClass.getDeviceClass()) {
                case 1028:
                case 1032:
                    nativeType = 32;
                    break;
                case 1056:
                    nativeType = 64;
                    break;
            }
        }
        if (com.android.server.audio.AudioService.DEBUG_DEVICES && android.os.Build.isQcomPlatform()) {
            android.util.Log.i(TAG, "btHeadsetDeviceToAudioDevice btDevice: " + btDevice + " btClass: " + (btClass != null ? btClass : "Unknown") + " nativeType: " + nativeType + " address: " + address);
        } else if (android.os.Build.isMtkPlatform()) {
            android.util.Log.i(TAG, "btHeadsetDeviceToAudioDevice btDevice: " + btDevice.getAnonymizedAddress() + " btClass: " + (btClass != null ? btClass : "Unknown") + " nativeType: " + nativeType);
        }
        return new android.media.AudioDeviceAttributes(nativeType, address, name);
    }

    private boolean handleBtScoActiveDeviceChange(android.bluetooth.BluetoothDevice btDevice, boolean isActive) {
        if (btDevice == null) {
            return true;
        }
        if (android.os.Build.isQcomPlatform()) {
            java.lang.String address = btDevice.getAddress();
            if (!"00:00:00:00:00:00".equals(address)) {
                btDevice.getBluetoothClass();
            }
        }
        android.media.AudioDeviceAttributes audioDevice = btHeadsetDeviceToAudioDevice(btDevice);
        java.lang.String btDeviceName = getName(btDevice);
        if (android.os.Build.isQcomPlatform() && btDeviceName == null) {
            android.util.Log.i(TAG, "handleBtScoActiveDeviceChange: btDeviceName is null, sending empty string");
        }
        boolean result = false;
        if (isActive) {
            result = false | this.mDeviceBroker.handleDeviceConnection(audioDevice, isActive, btDevice);
        } else {
            int[] outDeviceTypes = {16, 32, 64};
            for (int outDeviceType : outDeviceTypes) {
                result |= this.mDeviceBroker.handleDeviceConnection(new android.media.AudioDeviceAttributes(outDeviceType, audioDevice.getAddress(), audioDevice.getName()), isActive, btDevice);
            }
        }
        boolean result2 = this.mDeviceBroker.handleDeviceConnection(new android.media.AudioDeviceAttributes(-2147483640, audioDevice.getAddress(), audioDevice.getName()), isActive, btDevice) && result;
        if (result2) {
            if (isActive) {
                this.mResolvedScoAudioDevices.put(btDevice, audioDevice);
            } else {
                this.mResolvedScoAudioDevices.remove(btDevice);
            }
        }
        return result2;
    }

    private java.lang.String getAnonymizedAddress(android.bluetooth.BluetoothDevice btDevice) {
        return btDevice == null ? "(null)" : btDevice.getAnonymizedAddress();
    }

    synchronized void onSetBtScoActiveDevice(android.bluetooth.BluetoothDevice btDevice) {
        android.util.Log.i(TAG, "onSetBtScoActiveDevice: " + getAnonymizedAddress(this.mBluetoothHeadsetDevice) + " -> " + getAnonymizedAddress(btDevice));
        android.bluetooth.BluetoothDevice previousActiveDevice = this.mBluetoothHeadsetDevice;
        if (java.util.Objects.equals(btDevice, previousActiveDevice)) {
            return;
        }
        if (android.os.Build.isQcomPlatform()) {
            if (this.mBluetoothHeadsetDevice != null && this.mBluetoothHeadsetDevice.isTwsPlusDevice() && btDevice != null && java.util.Objects.equals(this.mBluetoothHeadsetDevice.getTwsPlusPeerAddress(), btDevice.getAddress())) {
                android.util.Log.i(TAG, "setBtScoActiveDevice: Active device switch between twsplus devices");
                return;
            }
            android.bluetooth.BluetoothAdapter adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
            if (adapter == null) {
                android.util.Log.i(TAG, "adapter is null, returning from setBtScoActiveDevice");
                return;
            }
            this.mBluetoothHeadsetDummyDevice = adapter.getRemoteDevice("00:00:00:00:00:00");
            if (this.mBluetoothHeadsetDevice == null && btDevice != null && !handleBtScoActiveDeviceChange(this.mBluetoothHeadsetDummyDevice, true)) {
                android.util.Log.e(TAG, "setBtScoActiveDevice() failed to add new device " + btDevice);
                btDevice = null;
            }
            if (this.mBluetoothHeadsetDevice != null && btDevice == null && !handleBtScoActiveDeviceChange(this.mBluetoothHeadsetDummyDevice, false)) {
                android.util.Log.w(TAG, "setBtScoActiveDevice() failed to remove previous device " + previousActiveDevice);
            }
            this.mBluetoothHeadsetDevice = btDevice;
            if (this.mBluetoothHeadsetDevice == null) {
                this.mDeviceBroker.getWrapper().sendIILMsg(81, 2, 0, 0, null, 0);
                this.mBluetoothHeadsetDummyDevice = null;
                android.util.Log.i(TAG, "In setBtScoActiveDevice(), calling resetBluetoothSco()");
                resetBluetoothSco();
            }
        } else {
            if (android.os.Build.isMtkPlatform() && !java.util.Objects.equals(btDevice, previousActiveDevice)) {
                this.mDeviceBroker.resetBluetoothScoOfApp();
            }
            if (!handleBtScoActiveDeviceChange(previousActiveDevice, false)) {
                android.util.Log.w(TAG, "setBtScoActiveDevice() failed to remove previous device " + getAnonymizedAddress(previousActiveDevice));
            }
            if (!handleBtScoActiveDeviceChange(btDevice, true)) {
                android.util.Log.e(TAG, "setBtScoActiveDevice() failed to add new device " + getAnonymizedAddress(btDevice));
                btDevice = null;
            }
            this.mBluetoothHeadsetDevice = btDevice;
            if (this.mBluetoothHeadsetDevice == null) {
                this.mDeviceBroker.getWrapper().sendIILMsg(81, 2, 0, 0, null, 0);
                if (android.os.Build.isMtkPlatform()) {
                    this.mDeviceBroker.resetBluetoothScoOfApp();
                }
                resetBluetoothSco();
            } else if (android.os.Build.isMtkPlatform()) {
                this.mDeviceBroker.restartScoInVoipCall();
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:69:0x01c5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean requestScoState(int r9, int r10) {
        /*
            Method dump skipped, instruction units count: 508
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.BtHelper.requestScoState(int, int):boolean");
    }

    private void sendStickyBroadcastToAll(android.content.Intent intent) {
        intent.addFlags(268435456);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mDeviceBroker.getContext().sendStickyBroadcastAsUser(intent, android.os.UserHandle.ALL);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private static boolean disconnectBluetoothScoAudioHelper(android.bluetooth.BluetoothHeadset bluetoothHeadset, android.bluetooth.BluetoothDevice device, int scoAudioMode) {
        android.util.Log.i(TAG, "In disconnectBluetoothScoAudioHelper(), scoAudioMode: " + scoAudioMode + ", bluetoothHeadset: " + bluetoothHeadset + ", BluetoothDevice: " + device);
        switch (scoAudioMode) {
            case 0:
                android.util.Log.i(TAG, "In disconnectBluetoothScoAudioHelper(), calling stopScoUsingVirtualVoiceCall()");
                return bluetoothHeadset.stopScoUsingVirtualVoiceCall();
            case 1:
            default:
                return false;
            case 2:
                android.util.Log.i(TAG, "In disconnectBluetoothScoAudioHelper(), calling stopVoiceRecognition()");
                return bluetoothHeadset.stopVoiceRecognition(device);
        }
    }

    private static boolean connectBluetoothScoAudioHelper(android.bluetooth.BluetoothHeadset bluetoothHeadset, android.bluetooth.BluetoothDevice device, int scoAudioMode) {
        android.util.Log.i(TAG, "In connectBluetoothScoAudioHelper(), scoAudioMode: " + scoAudioMode + ", bluetoothHeadset: " + bluetoothHeadset + ", BluetoothDevice: " + device);
        switch (scoAudioMode) {
            case 0:
                android.util.Log.i(TAG, "In connectBluetoothScoAudioHelper(), calling startScoUsingVirtualVoiceCall()");
                return bluetoothHeadset.startScoUsingVirtualVoiceCall();
            case 1:
            default:
                return false;
            case 2:
                android.util.Log.i(TAG, "In connectBluetoothScoAudioHelper(), calling startVoiceRecognition()");
                return bluetoothHeadset.startVoiceRecognition(device);
        }
    }

    private void checkScoAudioState() {
        if (this.mBluetoothHeadset != null && this.mBluetoothHeadsetDevice != null && this.mScoAudioState == 0 && this.mBluetoothHeadset.getAudioState(this.mBluetoothHeadsetDevice) != 10) {
            this.mScoAudioState = 2;
        }
        android.util.Log.i(TAG, "In checkScoAudioState(), mScoAudioState: " + this.mScoAudioState);
    }

    private boolean getBluetoothHeadset() {
        android.bluetooth.BluetoothAdapter adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && !this.mIsBluetoothHeadsetCreated) {
            this.mIsBluetoothHeadsetCreated = adapter.getProfileProxy(this.mDeviceBroker.getContext(), this.mBluetoothProfileServiceListener, 1);
        }
        this.mDeviceBroker.handleFailureToConnectToBtHeadsetService(this.mIsBluetoothHeadsetCreated ? 3000 : 0);
        return this.mIsBluetoothHeadsetCreated;
    }

    int getLeAudioDeviceGroupId(android.bluetooth.BluetoothDevice device) {
        if (this.mLeAudio == null || device == null) {
            return -1;
        }
        return this.mLeAudio.getGroupId(device);
    }

    java.util.List<android.util.Pair<java.lang.String, java.lang.String>> getLeAudioGroupAddresses(int groupId) {
        java.util.List<android.util.Pair<java.lang.String, java.lang.String>> addresses = new java.util.ArrayList<>();
        android.bluetooth.BluetoothAdapter adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        if (adapter == null || this.mLeAudio == null) {
            return addresses;
        }
        java.util.List<android.bluetooth.BluetoothDevice> activeDevices = adapter.getActiveDevices(22);
        for (android.bluetooth.BluetoothDevice device : activeDevices) {
            if (device != null && this.mLeAudio.getGroupId(device) == groupId) {
                addresses.add(new android.util.Pair<>(device.getAddress(), device.getIdentityAddress()));
            }
        }
        return addresses;
    }

    public static java.lang.String bluetoothCodecToEncodingString(int btCodecType) {
        switch (btCodecType) {
            case 0:
                return "ENCODING_SBC";
            case 1:
                return "ENCODING_AAC";
            case 2:
                return "ENCODING_APTX";
            case 3:
                return "ENCODING_APTX_HD";
            case 4:
                return "ENCODING_LDAC";
            case 6:
                return "ENCODING_OPUS";
            case 18:
            case 19:
                return "ENCODING_LHDC";
            default:
                return "ENCODING_BT_CODEC_TYPE(" + btCodecType + ")";
        }
    }

    static int getProfileFromType(int deviceType) {
        if (android.media.AudioSystem.isBluetoothA2dpOutDevice(deviceType)) {
            return 2;
        }
        if (android.media.AudioSystem.isBluetoothScoDevice(deviceType)) {
            return 1;
        }
        if (android.media.AudioSystem.isBluetoothLeDevice(deviceType)) {
            return 22;
        }
        return 0;
    }

    static android.os.Bundle getPreferredAudioProfiles(java.lang.String address) {
        android.bluetooth.BluetoothAdapter adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        return adapter.getPreferredAudioProfiles(adapter.getRemoteDevice(address));
    }

    static android.bluetooth.BluetoothDevice getBluetoothDevice(java.lang.String address) {
        android.bluetooth.BluetoothAdapter adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        if (adapter == null || !android.bluetooth.BluetoothAdapter.checkBluetoothAddress(address)) {
            return null;
        }
        return adapter.getRemoteDevice(address);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0070  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static int getBtDeviceCategory(java.lang.String r10) {
        /*
            Method dump skipped, instruction units count: 218
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.BtHelper.getBtDeviceCategory(java.lang.String):int");
    }

    public static void onNotifyPreferredAudioProfileApplied(android.bluetooth.BluetoothDevice btDevice) {
        android.bluetooth.BluetoothAdapter.getDefaultAdapter().notifyActiveDeviceChangeApplied(btDevice);
    }

    public static java.lang.String btDeviceClassToString(int btDeviceClass) {
        switch (btDeviceClass) {
            case 1024:
                return "AUDIO_VIDEO_UNCATEGORIZED";
            case 1028:
                return "AUDIO_VIDEO_WEARABLE_HEADSET";
            case 1032:
                return "AUDIO_VIDEO_HANDSFREE";
            case 1036:
                return "AUDIO_VIDEO_RESERVED_0x040C";
            case 1040:
                return "AUDIO_VIDEO_MICROPHONE";
            case 1044:
                return "AUDIO_VIDEO_LOUDSPEAKER";
            case 1048:
                return "AUDIO_VIDEO_HEADPHONES";
            case 1052:
                return "AUDIO_VIDEO_PORTABLE_AUDIO";
            case 1056:
                return "AUDIO_VIDEO_CAR_AUDIO";
            case 1060:
                return "AUDIO_VIDEO_SET_TOP_BOX";
            case 1064:
                return "AUDIO_VIDEO_HIFI_AUDIO";
            case 1068:
                return "AUDIO_VIDEO_VCR";
            case 1072:
                return "AUDIO_VIDEO_VIDEO_CAMERA";
            case 1076:
                return "AUDIO_VIDEO_CAMCORDER";
            case 1080:
                return "AUDIO_VIDEO_VIDEO_MONITOR";
            case 1084:
                return "AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER";
            case 1088:
                return "AUDIO_VIDEO_VIDEO_CONFERENCING";
            case 1092:
                return "AUDIO_VIDEO_RESERVED_0x0444";
            case 1096:
                return "AUDIO_VIDEO_VIDEO_GAMING_TOY";
            default:
                return android.text.TextUtils.formatSimple("0x%04x", new java.lang.Object[]{java.lang.Integer.valueOf(btDeviceClass)});
        }
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        android.bluetooth.BluetoothClass bluetoothClass;
        pw.println("\n" + prefix + "mBluetoothHeadset: " + this.mBluetoothHeadset);
        pw.println(prefix + "mBluetoothHeadsetDevice: " + this.mBluetoothHeadsetDevice);
        if (this.mBluetoothHeadsetDevice != null && (bluetoothClass = this.mBluetoothHeadsetDevice.getBluetoothClass()) != null) {
            pw.println(prefix + "mBluetoothHeadsetDevice.DeviceClass: " + btDeviceClassToString(bluetoothClass.getDeviceClass()));
        }
        pw.println(prefix + "mScoAudioState: " + scoAudioStateToString(this.mScoAudioState));
        pw.println(prefix + "mScoAudioMode: " + scoAudioModeToString(this.mScoAudioMode));
        pw.println("\n" + prefix + "mHearingAid: " + this.mHearingAid);
        pw.println("\n" + prefix + "mLeAudio: " + this.mLeAudio);
        pw.println(prefix + "mA2dp: " + this.mA2dp);
        pw.println(prefix + "mAvrcpAbsVolSupported: " + this.mAvrcpAbsVolSupported);
    }

    public int getScoAudioState() {
        return this.mScoAudioState;
    }

    android.bluetooth.BluetoothDevice getBtDevice() {
        if (this.mBluetoothHeadset == null || this.mBluetoothDevice == null) {
            return null;
        }
        return this.mBluetoothDevice.mDevice;
    }

    void setBtDevice(com.android.server.audio.AudioDeviceBroker.oplusBtDeviceInfo btDevice) {
        if (this.mBluetoothHeadset == null) {
            this.mBluetoothDevice = null;
        } else {
            this.mBluetoothDevice = btDevice;
        }
    }

    public boolean isLoudSpeakerBt() {
        android.bluetooth.BluetoothDevice btDevice = getBtDevice();
        if (btDevice == null) {
            return false;
        }
        android.bluetooth.BluetoothClass bluetoothClass = btDevice.getBluetoothClass();
        java.lang.String deviceName = btDevice.getName();
        if (bluetoothClass == null || android.text.TextUtils.isEmpty(deviceName)) {
            return false;
        }
        int deviceClass = bluetoothClass.getDeviceClass();
        if (deviceClass == 1044 || deviceClass == 1084 || this.mDeviceBroker.isUnsupportHoloDevice(deviceName)) {
            android.util.Log.d(TAG, "HoloAudio: isLoudSpeakerBt=false");
            return true;
        }
        return false;
    }

    public boolean isUnsuHoloVoipDevice() {
        android.bluetooth.BluetoothDevice btDevice = getBtDevice();
        if (btDevice == null) {
            return false;
        }
        btDevice.getName();
        return this.mUnsupHoloVoipDeList.contains(btDevice.getName());
    }

    public int getActiveBtProfile() {
        if (this.mBluetoothDevice != null) {
            return this.mBluetoothDevice.mProfile;
        }
        return -1;
    }
}
