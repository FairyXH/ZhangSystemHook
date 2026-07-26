package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class AudioDeviceInventory {
    private static final java.util.Set<java.lang.Integer> BECOMING_NOISY_INTENT_DEVICES_SET;
    private static final java.lang.String BIRECORD_DEVICE_NAME_PARAM = "binaural_recording_bt_name=";
    static final int BT_CONFIG_CHANGE_MUTE_DELAY_MS = 500;
    static final int[] CAPTURE_PRESETS;
    private static final java.lang.String CONNECT_INTENT_KEY_ADDRESS = "address";
    private static final java.lang.String CONNECT_INTENT_KEY_PORT_NAME = "portName";
    private static final java.lang.String CONNECT_INTENT_KEY_STATE = "state";
    private static final java.lang.String EXIT_GAME_MODE_PARAM = "AudioGameMode=0";
    private static final int MAX_SETTINGS_LENGTH_PER_STRING = 32768;
    private static final java.lang.String SETTING_DEVICE_SEPARATOR = "\\|";
    private static final java.lang.String SETTING_DEVICE_SEPARATOR_CHAR = "|";
    private static final java.lang.String TAG = "AS.AudioDeviceInventory";
    private static final java.lang.String mMetricsId = "audio.device.";
    private com.android.server.audio.IAudioDeviceInventoryExt mAdiExt;
    private com.android.server.audio.AudioDeviceInventory.AudioDeviceInventoryWrapper mAdiWrapper;
    private final android.util.ArrayMap<java.lang.Integer, java.lang.String> mApmConnectedDevices;
    private final android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.Integer>, java.util.List<android.media.AudioDeviceAttributes>> mAppliedPresetRoles;
    private final android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.Integer>, java.util.List<android.media.AudioDeviceAttributes>> mAppliedPresetRolesInt;
    private final android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.Integer>, java.util.List<android.media.AudioDeviceAttributes>> mAppliedStrategyRoles;
    private final android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.Integer>, java.util.List<android.media.AudioDeviceAttributes>> mAppliedStrategyRolesInt;
    private final com.android.server.audio.AudioSystemAdapter mAudioSystem;
    final boolean mBluetoothDualModeEnabled;
    private final java.util.LinkedHashMap<java.lang.String, com.android.server.audio.AudioDeviceInventory.DeviceInfo> mConnectedDevices;
    final android.media.AudioRoutesInfo mCurAudioRoutes;
    final android.os.RemoteCallbackList<android.media.ICapturePresetDevicesRoleDispatcher> mDevRoleCapturePresetDispatchers;
    private com.android.server.audio.AudioDeviceBroker mDeviceBroker;
    private final java.util.LinkedHashMap<android.util.Pair<java.lang.Integer, java.lang.String>, com.android.server.audio.AdiDeviceState> mDeviceInventory;
    private final java.lang.Object mDeviceInventoryLock;
    private final java.lang.Object mDevicesLock;
    private int mLastMusicBTdeviceConnected;
    final android.os.RemoteCallbackList<android.media.IStrategyNonDefaultDevicesDispatcher> mNonDefDevDispatchers;
    private final android.util.ArrayMap<java.lang.Integer, java.util.List<android.media.AudioDeviceAttributes>> mNonDefaultDevices;
    final android.os.RemoteCallbackList<android.media.IStrategyPreferredDevicesDispatcher> mPrefDevDispatchers;
    private final android.util.ArrayMap<java.lang.Integer, java.util.List<android.media.AudioDeviceAttributes>> mPreferredDevices;
    private final android.util.ArrayMap<java.lang.Integer, java.util.List<android.media.AudioDeviceAttributes>> mPreferredDevicesForCapturePreset;
    final android.os.RemoteCallbackList<android.media.IAudioRoutesObserver> mRoutesObservers;
    final java.util.List<android.media.audiopolicy.AudioProductStrategy> mStrategies;
    private static final int MAX_DEVICE_INVENTORY_ENTRIES = 32768 / com.android.server.audio.AdiDeviceState.getPeristedMaxSize();
    static final java.util.Set<java.lang.Integer> DEVICE_OVERRIDE_A2DP_ROUTE_ON_PLUG_SET = new java.util.HashSet();

    interface AudioSystemInterface {
        int deviceRoleAction(int i, int i2, java.util.List<android.media.AudioDeviceAttributes> list);
    }

    static {
        DEVICE_OVERRIDE_A2DP_ROUTE_ON_PLUG_SET.add(4);
        DEVICE_OVERRIDE_A2DP_ROUTE_ON_PLUG_SET.add(8);
        DEVICE_OVERRIDE_A2DP_ROUTE_ON_PLUG_SET.add(131072);
        DEVICE_OVERRIDE_A2DP_ROUTE_ON_PLUG_SET.addAll(android.media.AudioSystem.DEVICE_OUT_ALL_USB_SET);
        CAPTURE_PRESETS = new int[]{1, 5, 6, 7, 9, 10, 1999};
        BECOMING_NOISY_INTENT_DEVICES_SET = new java.util.HashSet();
        BECOMING_NOISY_INTENT_DEVICES_SET.add(4);
        BECOMING_NOISY_INTENT_DEVICES_SET.add(8);
        BECOMING_NOISY_INTENT_DEVICES_SET.add(1024);
        BECOMING_NOISY_INTENT_DEVICES_SET.add(2048);
        BECOMING_NOISY_INTENT_DEVICES_SET.add(131072);
        BECOMING_NOISY_INTENT_DEVICES_SET.add(134217728);
        BECOMING_NOISY_INTENT_DEVICES_SET.add(536870912);
        BECOMING_NOISY_INTENT_DEVICES_SET.add(536870914);
        BECOMING_NOISY_INTENT_DEVICES_SET.addAll(android.media.AudioSystem.DEVICE_OUT_ALL_A2DP_SET);
        BECOMING_NOISY_INTENT_DEVICES_SET.addAll(android.media.AudioSystem.DEVICE_OUT_ALL_USB_SET);
        BECOMING_NOISY_INTENT_DEVICES_SET.addAll(android.media.AudioSystem.DEVICE_OUT_ALL_BLE_SET);
        BECOMING_NOISY_INTENT_DEVICES_SET.add(8388608);
    }

    java.util.Collection<com.android.server.audio.AdiDeviceState> getImmutableDeviceInventory() {
        java.util.List<com.android.server.audio.AdiDeviceState> newList;
        synchronized (this.mDeviceInventoryLock) {
            newList = new java.util.ArrayList<>(this.mDeviceInventory.values());
        }
        return newList;
    }

    void addOrUpdateDeviceSAStateInInventory(com.android.server.audio.AdiDeviceState deviceState, boolean syncInventory) {
        synchronized (this.mDeviceInventoryLock) {
            this.mDeviceInventory.merge(deviceState.getDeviceId(), deviceState, new java.util.function.BiFunction() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda22
                @Override // java.util.function.BiFunction
                public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                    return com.android.server.audio.AudioDeviceInventory.lambda$addOrUpdateDeviceSAStateInInventory$0((com.android.server.audio.AdiDeviceState) obj, (com.android.server.audio.AdiDeviceState) obj2);
                }
            });
            checkDeviceInventorySize_l();
        }
        if (syncInventory) {
            this.mDeviceBroker.postSynchronizeAdiDevicesInInventory(deviceState);
        }
    }

    static /* synthetic */ com.android.server.audio.AdiDeviceState lambda$addOrUpdateDeviceSAStateInInventory$0(com.android.server.audio.AdiDeviceState oldState, com.android.server.audio.AdiDeviceState newState) {
        oldState.setHasHeadTracker(newState.hasHeadTracker());
        oldState.setHeadTrackerEnabled(newState.isHeadTrackerEnabled());
        oldState.setSAEnabled(newState.isSAEnabled());
        return oldState;
    }

    void addAudioDeviceInInventoryIfNeeded(int deviceType, java.lang.String address, java.lang.String peerAddress, int category, boolean userDefined) {
        if (!android.media.AudioSystem.isBluetoothOutDevice(deviceType)) {
            return;
        }
        synchronized (this.mDeviceInventoryLock) {
            com.android.server.audio.AdiDeviceState ads = findBtDeviceStateForAddress(address, deviceType);
            if (ads == null && peerAddress != null) {
                ads = findBtDeviceStateForAddress(peerAddress, deviceType);
            }
            if (ads != null) {
                if (ads.getAudioDeviceCategory() != category && (userDefined || category != 0)) {
                    ads.setAudioDeviceCategory(category);
                    this.mDeviceBroker.postUpdatedAdiDeviceState(ads, false);
                    this.mDeviceBroker.postPersistAudioDeviceSettings();
                }
                this.mDeviceBroker.postSynchronizeAdiDevicesInInventory(ads);
                return;
            }
            com.android.server.audio.AdiDeviceState ads2 = new com.android.server.audio.AdiDeviceState(android.media.AudioDeviceInfo.convertInternalDeviceToDeviceType(deviceType), deviceType, address);
            ads2.setAudioDeviceCategory(category);
            this.mDeviceInventory.put(ads2.getDeviceId(), ads2);
            checkDeviceInventorySize_l();
            this.mDeviceBroker.postUpdatedAdiDeviceState(ads2, true);
            this.mDeviceBroker.postPersistAudioDeviceSettings();
        }
    }

    void addOrUpdateAudioDeviceCategoryInInventory(com.android.server.audio.AdiDeviceState deviceState, boolean syncInventory) {
        com.android.server.audio.AdiDeviceState deviceState2;
        final java.util.concurrent.atomic.AtomicBoolean updatedCategory = new java.util.concurrent.atomic.AtomicBoolean(false);
        synchronized (this.mDeviceInventoryLock) {
            if (android.media.audio.Flags.automaticBtDeviceType() && deviceState.updateAudioDeviceCategory()) {
                updatedCategory.set(true);
            }
            deviceState2 = this.mDeviceInventory.merge(deviceState.getDeviceId(), deviceState, new java.util.function.BiFunction() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda7
                @Override // java.util.function.BiFunction
                public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                    return com.android.server.audio.AudioDeviceInventory.lambda$addOrUpdateAudioDeviceCategoryInInventory$1(updatedCategory, (com.android.server.audio.AdiDeviceState) obj, (com.android.server.audio.AdiDeviceState) obj2);
                }
            });
            checkDeviceInventorySize_l();
        }
        if (updatedCategory.get()) {
            this.mDeviceBroker.postUpdatedAdiDeviceState(deviceState2, false);
        }
        if (syncInventory) {
            this.mDeviceBroker.postSynchronizeAdiDevicesInInventory(deviceState2);
        }
    }

    static /* synthetic */ com.android.server.audio.AdiDeviceState lambda$addOrUpdateAudioDeviceCategoryInInventory$1(java.util.concurrent.atomic.AtomicBoolean updatedCategory, com.android.server.audio.AdiDeviceState oldState, com.android.server.audio.AdiDeviceState newState) {
        if (oldState.getAudioDeviceCategory() != newState.getAudioDeviceCategory()) {
            oldState.setAudioDeviceCategory(newState.getAudioDeviceCategory());
            updatedCategory.set(true);
        }
        return oldState;
    }

    void addAudioDeviceWithCategoryInInventoryIfNeeded(java.lang.String address, int btAudioDeviceCategory) {
        addAudioDeviceInInventoryIfNeeded(536870912, address, "", btAudioDeviceCategory, true);
        addAudioDeviceInInventoryIfNeeded(128, address, "", btAudioDeviceCategory, true);
    }

    int getAndUpdateBtAdiDeviceStateCategoryForAddress(java.lang.String address) {
        int btCategory = 0;
        boolean bleCategoryFound = false;
        com.android.server.audio.AdiDeviceState deviceState = findBtDeviceStateForAddress(address, 536870912);
        if (deviceState != null) {
            addOrUpdateAudioDeviceCategoryInInventory(deviceState, true);
            btCategory = deviceState.getAudioDeviceCategory();
            bleCategoryFound = true;
        }
        com.android.server.audio.AdiDeviceState deviceState2 = findBtDeviceStateForAddress(address, 128);
        if (deviceState2 != null) {
            addOrUpdateAudioDeviceCategoryInInventory(deviceState2, true);
            int a2dpCategory = deviceState2.getAudioDeviceCategory();
            if (bleCategoryFound && a2dpCategory != btCategory) {
                android.util.Log.w(TAG, "Found different audio device category for A2DP and BLE profiles with address " + address);
            }
            return a2dpCategory;
        }
        return btCategory;
    }

    boolean isBluetoothAudioDeviceCategoryFixed(java.lang.String address) {
        com.android.server.audio.AdiDeviceState deviceState = findBtDeviceStateForAddress(address, 536870912);
        if (deviceState != null) {
            return deviceState.isBtDeviceCategoryFixed();
        }
        com.android.server.audio.AdiDeviceState deviceState2 = findBtDeviceStateForAddress(address, 128);
        if (deviceState2 != null) {
            return deviceState2.isBtDeviceCategoryFixed();
        }
        return false;
    }

    void onSynchronizeAdiDevicesInInventory(com.android.server.audio.AdiDeviceState updatedDevice) {
        synchronized (this.mDevicesLock) {
            synchronized (this.mDeviceInventoryLock) {
                if (updatedDevice != null) {
                    onSynchronizeAdiDeviceInInventory_l(updatedDevice);
                } else {
                    for (com.android.server.audio.AdiDeviceState ads : this.mDeviceInventory.values()) {
                        onSynchronizeAdiDeviceInInventory_l(ads);
                    }
                }
            }
        }
    }

    void onSynchronizeAdiDeviceInInventory_l(com.android.server.audio.AdiDeviceState updatedDevice) {
        boolean found = false | synchronizeBleDeviceInInventory(updatedDevice);
        if (android.media.audio.Flags.automaticBtDeviceType()) {
            found |= synchronizeDeviceProfilesInInventory(updatedDevice);
        }
        if (found) {
            this.mDeviceBroker.postPersistAudioDeviceSettings();
        }
    }

    private void checkDeviceInventorySize_l() {
        if (this.mDeviceInventory.size() > MAX_DEVICE_INVENTORY_ENTRIES) {
            java.util.Iterator<java.util.Map.Entry<android.util.Pair<java.lang.Integer, java.lang.String>, com.android.server.audio.AdiDeviceState>> iterator = this.mDeviceInventory.entrySet().iterator();
            if (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
        }
    }

    private boolean synchronizeBleDeviceInInventory(com.android.server.audio.AdiDeviceState updatedDevice) {
        for (com.android.server.audio.AudioDeviceInventory.DeviceInfo di : this.mConnectedDevices.values()) {
            if (di.mDeviceType == updatedDevice.getInternalDeviceType()) {
                if (di.mDeviceAddress.equals(updatedDevice.getDeviceAddress())) {
                    for (com.android.server.audio.AdiDeviceState ads2 : this.mDeviceInventory.values()) {
                        if (di.mDeviceType == ads2.getInternalDeviceType() && di.mPeerDeviceAddress.equals(ads2.getDeviceAddress())) {
                            if (this.mDeviceBroker.isSADevice(updatedDevice) == this.mDeviceBroker.isSADevice(ads2)) {
                                ads2.setHasHeadTracker(updatedDevice.hasHeadTracker());
                                ads2.setHeadTrackerEnabled(updatedDevice.isHeadTrackerEnabled());
                                ads2.setSAEnabled(updatedDevice.isSAEnabled());
                            }
                            ads2.setAudioDeviceCategory(updatedDevice.getAudioDeviceCategory());
                            this.mDeviceBroker.postUpdatedAdiDeviceState(ads2, false);
                            com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("synchronizeBleDeviceInInventory synced device pair ads1=" + updatedDevice + " ads2=" + ads2).printLog(TAG));
                            return true;
                        }
                    }
                }
                if (di.mPeerDeviceAddress.equals(updatedDevice.getDeviceAddress())) {
                    for (com.android.server.audio.AdiDeviceState ads22 : this.mDeviceInventory.values()) {
                        if (di.mDeviceType == ads22.getInternalDeviceType() && di.mDeviceAddress.equals(ads22.getDeviceAddress())) {
                            if (this.mDeviceBroker.isSADevice(updatedDevice) == this.mDeviceBroker.isSADevice(ads22)) {
                                ads22.setHasHeadTracker(updatedDevice.hasHeadTracker());
                                ads22.setHeadTrackerEnabled(updatedDevice.isHeadTrackerEnabled());
                                ads22.setSAEnabled(updatedDevice.isSAEnabled());
                            }
                            ads22.setAudioDeviceCategory(updatedDevice.getAudioDeviceCategory());
                            this.mDeviceBroker.postUpdatedAdiDeviceState(ads22, false);
                            com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("synchronizeBleDeviceInInventory synced device pair ads1=" + updatedDevice + " peer ads2=" + ads22).printLog(TAG));
                            return true;
                        }
                    }
                } else {
                    continue;
                }
            }
        }
        return false;
    }

    private boolean synchronizeDeviceProfilesInInventory(com.android.server.audio.AdiDeviceState updatedDevice) {
        for (com.android.server.audio.AdiDeviceState ads : this.mDeviceInventory.values()) {
            if (updatedDevice.getInternalDeviceType() == ads.getInternalDeviceType() && updatedDevice.getDeviceAddress().equals(ads.getDeviceAddress())) {
                if (this.mDeviceBroker.isSADevice(updatedDevice) == this.mDeviceBroker.isSADevice(ads)) {
                    ads.setHasHeadTracker(updatedDevice.hasHeadTracker());
                    ads.setHeadTrackerEnabled(updatedDevice.isHeadTrackerEnabled());
                    ads.setSAEnabled(updatedDevice.isSAEnabled());
                }
                ads.setAudioDeviceCategory(updatedDevice.getAudioDeviceCategory());
                this.mDeviceBroker.postUpdatedAdiDeviceState(ads, false);
                com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("synchronizeDeviceProfilesInInventory synced device pair ads1=" + updatedDevice + " ads2=" + ads).printLog(TAG));
                return true;
            }
        }
        return false;
    }

    public com.android.server.audio.AdiDeviceState findBtDeviceStateForAddress(java.lang.String address, int deviceType) {
        java.util.Set<java.lang.Integer> deviceSet;
        if (android.media.AudioSystem.isBluetoothA2dpOutDevice(deviceType)) {
            deviceSet = android.media.AudioSystem.DEVICE_OUT_ALL_A2DP_SET;
        } else if (android.media.AudioSystem.isBluetoothLeOutDevice(deviceType)) {
            deviceSet = android.media.AudioSystem.DEVICE_OUT_ALL_BLE_SET;
        } else if (android.media.AudioSystem.isBluetoothScoOutDevice(deviceType)) {
            deviceSet = android.media.AudioSystem.DEVICE_OUT_ALL_SCO_SET;
        } else {
            if (deviceType != 134217728) {
                return null;
            }
            java.util.Set<java.lang.Integer> deviceSet2 = new java.util.HashSet<>();
            deviceSet2.add(134217728);
            deviceSet = deviceSet2;
        }
        synchronized (this.mDeviceInventoryLock) {
            for (java.lang.Integer internalType : deviceSet) {
                com.android.server.audio.AdiDeviceState deviceState = this.mDeviceInventory.get(new android.util.Pair(internalType, address));
                if (deviceState != null) {
                    return deviceState;
                }
            }
            return null;
        }
    }

    com.android.server.audio.AdiDeviceState findDeviceStateForAudioDeviceAttributes(android.media.AudioDeviceAttributes ada, int canonicalDeviceType) {
        boolean isWireless = android.media.AudioSystem.isBluetoothDevice(ada.getInternalType());
        synchronized (this.mDeviceInventoryLock) {
            for (com.android.server.audio.AdiDeviceState deviceState : this.mDeviceInventory.values()) {
                if (deviceState.getDeviceType() == canonicalDeviceType && (!isWireless || ada.getAddress().equals(deviceState.getDeviceAddress()))) {
                    return deviceState;
                }
            }
            return null;
        }
    }

    void clearDeviceInventory() {
        synchronized (this.mDeviceInventoryLock) {
            this.mDeviceInventory.clear();
        }
    }

    AudioDeviceInventory(com.android.server.audio.AudioDeviceBroker broker) {
        this(broker, com.android.server.audio.AudioSystemAdapter.getDefaultAdapter());
    }

    AudioDeviceInventory(com.android.server.audio.AudioSystemAdapter audioSystem) {
        this(null, audioSystem);
    }

    private AudioDeviceInventory(com.android.server.audio.AudioDeviceBroker broker, com.android.server.audio.AudioSystemAdapter audioSystem) {
        this.mDevicesLock = new java.lang.Object();
        this.mLastMusicBTdeviceConnected = 0;
        this.mAdiWrapper = new com.android.server.audio.AudioDeviceInventory.AudioDeviceInventoryWrapper();
        this.mAdiExt = (com.android.server.audio.IAudioDeviceInventoryExt) system.ext.loader.core.ExtLoader.type(com.android.server.audio.IAudioDeviceInventoryExt.class).base(this).create();
        this.mDeviceInventoryLock = new java.lang.Object();
        this.mDeviceInventory = new java.util.LinkedHashMap<>();
        this.mConnectedDevices = new java.util.LinkedHashMap<java.lang.String, com.android.server.audio.AudioDeviceInventory.DeviceInfo>() { // from class: com.android.server.audio.AudioDeviceInventory.1
            @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
            public com.android.server.audio.AudioDeviceInventory.DeviceInfo put(java.lang.String key, com.android.server.audio.AudioDeviceInventory.DeviceInfo value) {
                com.android.server.audio.AudioDeviceInventory.DeviceInfo result = (com.android.server.audio.AudioDeviceInventory.DeviceInfo) super.put(key, value);
                record("put", true, value);
                return result;
            }

            @Override // java.util.HashMap, java.util.Map
            public com.android.server.audio.AudioDeviceInventory.DeviceInfo putIfAbsent(java.lang.String key, com.android.server.audio.AudioDeviceInventory.DeviceInfo value) {
                com.android.server.audio.AudioDeviceInventory.DeviceInfo result = (com.android.server.audio.AudioDeviceInventory.DeviceInfo) super.putIfAbsent(key, value);
                if (result == null) {
                    record("putIfAbsent", true, value);
                }
                return result;
            }

            @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
            public com.android.server.audio.AudioDeviceInventory.DeviceInfo remove(java.lang.Object key) {
                com.android.server.audio.AudioDeviceInventory.DeviceInfo result = (com.android.server.audio.AudioDeviceInventory.DeviceInfo) super.remove(key);
                if (result != null) {
                    record("remove", false, result);
                }
                return result;
            }

            @Override // java.util.HashMap, java.util.Map
            public boolean remove(java.lang.Object key, java.lang.Object value) {
                boolean result = super.remove(key, value);
                if (result) {
                    record("remove", false, (com.android.server.audio.AudioDeviceInventory.DeviceInfo) value);
                }
                return result;
            }

            private void record(java.lang.String event, boolean connected, com.android.server.audio.AudioDeviceInventory.DeviceInfo value) {
                new android.media.MediaMetrics.Item(com.android.server.audio.AudioDeviceInventory.mMetricsId + android.media.AudioSystem.getDeviceName(value.mDeviceType)).set(android.media.MediaMetrics.Property.ADDRESS, value.mDeviceAddress).set(android.media.MediaMetrics.Property.EVENT, event).set(android.media.MediaMetrics.Property.NAME, value.mDeviceName).set(android.media.MediaMetrics.Property.STATE, connected ? "connected" : "disconnected").record();
            }
        };
        this.mApmConnectedDevices = new android.util.ArrayMap<>();
        this.mPreferredDevices = new android.util.ArrayMap<>();
        this.mNonDefaultDevices = new android.util.ArrayMap<>();
        this.mPreferredDevicesForCapturePreset = new android.util.ArrayMap<>();
        this.mCurAudioRoutes = new android.media.AudioRoutesInfo();
        this.mRoutesObservers = new android.os.RemoteCallbackList<>();
        this.mPrefDevDispatchers = new android.os.RemoteCallbackList<>();
        this.mNonDefDevDispatchers = new android.os.RemoteCallbackList<>();
        this.mDevRoleCapturePresetDispatchers = new android.os.RemoteCallbackList<>();
        this.mAppliedStrategyRoles = new android.util.ArrayMap<>();
        this.mAppliedStrategyRolesInt = new android.util.ArrayMap<>();
        this.mAppliedPresetRoles = new android.util.ArrayMap<>();
        this.mAppliedPresetRolesInt = new android.util.ArrayMap<>();
        this.mDeviceBroker = broker;
        this.mAudioSystem = audioSystem;
        this.mAdiExt.init(this.mDeviceBroker);
        this.mStrategies = android.media.audiopolicy.AudioProductStrategy.getAudioProductStrategies();
        this.mBluetoothDualModeEnabled = android.os.SystemProperties.getBoolean("persist.bluetooth.enable_dual_mode_audio", false);
    }

    void setDeviceBroker(com.android.server.audio.AudioDeviceBroker broker) {
        this.mDeviceBroker = broker;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class DeviceInfo {
        final java.lang.String mDeviceAddress;
        int mDeviceCodecFormat;
        java.lang.String mDeviceIdentityAddress;
        final java.lang.String mDeviceName;
        final int mDeviceType;
        android.util.ArraySet<java.lang.String> mDisabledModes;
        final int mGroupId;
        java.lang.String mPeerDeviceAddress;
        java.lang.String mPeerIdentityDeviceAddress;

        DeviceInfo(int deviceType, java.lang.String deviceName, java.lang.String address, java.lang.String identityAddress, int codecFormat, int groupId, java.lang.String peerAddress, java.lang.String peerIdentityAddress) {
            this.mDisabledModes = new android.util.ArraySet<>(0);
            this.mDeviceType = deviceType;
            this.mDeviceName = android.text.TextUtils.emptyIfNull(deviceName);
            this.mDeviceAddress = android.text.TextUtils.emptyIfNull(address);
            this.mDeviceIdentityAddress = android.text.TextUtils.emptyIfNull(identityAddress);
            if (this.mDeviceIdentityAddress.isEmpty()) {
                this.mDeviceIdentityAddress = this.mDeviceAddress;
            }
            this.mDeviceCodecFormat = codecFormat;
            this.mGroupId = groupId;
            this.mPeerDeviceAddress = android.text.TextUtils.emptyIfNull(peerAddress);
            this.mPeerIdentityDeviceAddress = android.text.TextUtils.emptyIfNull(peerIdentityAddress);
        }

        DeviceInfo(int deviceType, java.lang.String deviceName, java.lang.String address) {
            this(deviceType, deviceName, address, null, 0);
        }

        DeviceInfo(int deviceType, java.lang.String deviceName, java.lang.String address, java.lang.String identityAddress, int codecFormat) {
            this(deviceType, deviceName, address, identityAddress, codecFormat, -1, null, null);
        }

        void setModeDisabled(java.lang.String mode) {
            this.mDisabledModes.add(mode);
        }

        void setModeEnabled(java.lang.String mode) {
            this.mDisabledModes.remove(mode);
        }

        boolean isModeEnabled(java.lang.String mode) {
            return !this.mDisabledModes.contains(mode);
        }

        boolean isOutputOnlyModeEnabled() {
            return isModeEnabled("audio_mode_output_only");
        }

        boolean isDuplexModeEnabled() {
            return isModeEnabled("audio_mode_duplex");
        }

        public java.lang.String toString() {
            return "[DeviceInfo: type:0x" + java.lang.Integer.toHexString(this.mDeviceType) + " (" + android.media.AudioSystem.getDeviceName(this.mDeviceType) + ") name:" + this.mDeviceName + " addr:" + android.media.Utils.anonymizeBluetoothAddress(this.mDeviceType, this.mDeviceAddress) + " identity addr:" + android.media.Utils.anonymizeBluetoothAddress(this.mDeviceType, this.mDeviceIdentityAddress) + " codec: " + java.lang.Integer.toHexString(this.mDeviceCodecFormat) + " group:" + this.mGroupId + " peer addr:" + android.media.Utils.anonymizeBluetoothAddress(this.mDeviceType, this.mPeerDeviceAddress) + " peer identity addr:" + android.media.Utils.anonymizeBluetoothAddress(this.mDeviceType, this.mPeerIdentityDeviceAddress) + " disabled modes: " + this.mDisabledModes + "]";
        }

        java.lang.String getKey() {
            return makeDeviceListKey(this.mDeviceType, this.mDeviceAddress);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static java.lang.String makeDeviceListKey(int device, java.lang.String deviceAddress) {
            return "0x" + java.lang.Integer.toHexString(device) + ":" + deviceAddress;
        }
    }

    static class WiredDeviceConnectionState {
        public final android.media.AudioDeviceAttributes mAttributes;
        public final java.lang.String mCaller;
        public boolean mForTest = false;
        public final int mState;

        WiredDeviceConnectionState(android.media.AudioDeviceAttributes attributes, int state, java.lang.String caller) {
            this.mAttributes = attributes;
            this.mState = state;
            this.mCaller = caller;
        }
    }

    void dump(final java.io.PrintWriter pw, final java.lang.String prefix) {
        pw.println("\n" + prefix + "BECOMING_NOISY_INTENT_DEVICES_SET=");
        BECOMING_NOISY_INTENT_DEVICES_SET.forEach(new java.util.function.Consumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                pw.print(" 0x" + java.lang.Integer.toHexString(((java.lang.Integer) obj).intValue()));
            }
        });
        pw.println("\n" + prefix + "Preferred devices for strategy:");
        this.mPreferredDevices.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda12
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                pw.println("  " + prefix + "strategy:" + ((java.lang.Integer) obj) + " device:" + ((java.util.List) obj2));
            }
        });
        pw.println("\n" + prefix + "Non-default devices for strategy:");
        this.mNonDefaultDevices.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda13
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                pw.println("  " + prefix + "strategy:" + ((java.lang.Integer) obj) + " device:" + ((java.util.List) obj2));
            }
        });
        pw.println("\n" + prefix + "Connected devices:");
        this.mConnectedDevices.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda14
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                pw.println("  " + prefix + ((com.android.server.audio.AudioDeviceInventory.DeviceInfo) obj2).toString());
            }
        });
        pw.println("\n" + prefix + "APM Connected device (A2DP sink only):");
        this.mApmConnectedDevices.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda15
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                java.lang.Integer num = (java.lang.Integer) obj;
                pw.println("  " + prefix + " type:0x" + java.lang.Integer.toHexString(num.intValue()) + " (" + android.media.AudioSystem.getDeviceName(num.intValue()) + ") addr:" + android.media.Utils.anonymizeBluetoothAddress(num.intValue(), (java.lang.String) obj2));
            }
        });
        pw.println("\n" + prefix + "Preferred devices for capture preset:");
        this.mPreferredDevicesForCapturePreset.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda16
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                pw.println("  " + prefix + "capturePreset:" + ((java.lang.Integer) obj) + " devices:" + ((java.util.List) obj2));
            }
        });
        pw.println("\n" + prefix + "Applied devices roles for strategies (from API):");
        this.mAppliedStrategyRoles.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda17
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                android.util.Pair pair = (android.util.Pair) obj;
                pw.println("  " + prefix + "strategy: " + pair.first + " role:" + pair.second + " devices:" + ((java.util.List) obj2));
            }
        });
        pw.println("\n" + prefix + "Applied devices roles for strategies (internal):");
        this.mAppliedStrategyRolesInt.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda18
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                android.util.Pair pair = (android.util.Pair) obj;
                pw.println("  " + prefix + "strategy: " + pair.first + " role:" + pair.second + " devices:" + ((java.util.List) obj2));
            }
        });
        pw.println("\n" + prefix + "Applied devices roles for presets (from API):");
        this.mAppliedPresetRoles.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda19
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                android.util.Pair pair = (android.util.Pair) obj;
                pw.println("  " + prefix + "preset: " + pair.first + " role:" + pair.second + " devices:" + ((java.util.List) obj2));
            }
        });
        pw.println("\n" + prefix + "Applied devices roles for presets (internal:");
        this.mAppliedPresetRolesInt.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda20
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                android.util.Pair pair = (android.util.Pair) obj;
                pw.println("  " + prefix + "preset: " + pair.first + " role:" + pair.second + " devices:" + ((java.util.List) obj2));
            }
        });
        pw.println("\ndevices:\n");
        synchronized (this.mDeviceInventoryLock) {
            for (com.android.server.audio.AdiDeviceState device : this.mDeviceInventory.values()) {
                pw.println("\t" + device + "\n");
            }
        }
    }

    void onRestoreDevices() {
        synchronized (this.mDevicesLock) {
            for (com.android.server.audio.AudioDeviceInventory.DeviceInfo di : this.mConnectedDevices.values()) {
                this.mAudioSystem.setDeviceConnectionState(new android.media.AudioDeviceAttributes(di.mDeviceType, di.mDeviceAddress, di.mDeviceName), 1, di.mDeviceCodecFormat);
            }
            this.mAppliedStrategyRolesInt.clear();
            this.mAppliedPresetRolesInt.clear();
            applyConnectedDevicesRoles_l();
        }
        reapplyExternalDevicesRoles();
    }

    void reapplyExternalDevicesRoles() {
        synchronized (this.mDevicesLock) {
            this.mAppliedStrategyRoles.clear();
            this.mAppliedPresetRoles.clear();
        }
        synchronized (this.mPreferredDevices) {
            this.mPreferredDevices.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda36
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    this.f$0.lambda$reapplyExternalDevicesRoles$12((java.lang.Integer) obj, (java.util.List) obj2);
                }
            });
        }
        synchronized (this.mNonDefaultDevices) {
            this.mNonDefaultDevices.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda37
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    this.f$0.lambda$reapplyExternalDevicesRoles$13((java.lang.Integer) obj, (java.util.List) obj2);
                }
            });
        }
        synchronized (this.mPreferredDevicesForCapturePreset) {
            this.mPreferredDevicesForCapturePreset.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda38
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    this.f$0.lambda$reapplyExternalDevicesRoles$14((java.lang.Integer) obj, (java.util.List) obj2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reapplyExternalDevicesRoles$12(java.lang.Integer strategy, java.util.List devices) {
        setPreferredDevicesForStrategy(strategy.intValue(), devices);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reapplyExternalDevicesRoles$13(java.lang.Integer strategy, java.util.List devices) {
        addDevicesRoleForStrategy(strategy.intValue(), 2, devices, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reapplyExternalDevicesRoles$14(java.lang.Integer capturePreset, java.util.List devices) {
        setDevicesRoleForCapturePreset(capturePreset.intValue(), 1, devices);
    }

    public void onSetBtActiveDevice(com.android.server.audio.AudioDeviceBroker.BtDeviceInfo btInfo, int codec, int streamType) {
        java.lang.String address;
        if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
            android.util.Log.d(TAG, "onSetBtActiveDevice, btDevice=" + btInfo.mDevice + ", profile=" + android.bluetooth.BluetoothProfile.getProfileName(btInfo.mProfile) + ", state=" + android.bluetooth.BluetoothProfile.getConnectionStateName(btInfo.mState) + ", device=0x" + java.lang.Integer.toHexString(btInfo.mAudioSystemDevice) + ", vol=" + btInfo.mVolume + ", streamType=" + streamType);
        }
        java.lang.String address2 = btInfo.mDevice.getAddress();
        if (android.bluetooth.BluetoothAdapter.checkBluetoothAddress(address2)) {
            address = address2;
        } else {
            address = "";
        }
        if (btInfo.mVolume != -1) {
            this.mDeviceBroker.getWrapper().setBluetoothDevice(this.mDeviceBroker.createOplusBtDeviceInfo(btInfo.mDevice, btInfo.mProfile));
        }
        com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("BT connected:" + btInfo + " codec=" + android.media.AudioSystem.audioFormatToString(codec)));
        new android.media.MediaMetrics.Item("audio.device.onSetBtActiveDevice").set(android.media.MediaMetrics.Property.STATUS, java.lang.Integer.valueOf(btInfo.mProfile)).set(android.media.MediaMetrics.Property.DEVICE, android.media.AudioSystem.getDeviceName(btInfo.mAudioSystemDevice)).set(android.media.MediaMetrics.Property.ADDRESS, address).set(android.media.MediaMetrics.Property.ENCODING, android.media.AudioSystem.audioFormatToString(codec)).set(android.media.MediaMetrics.Property.EVENT, "onSetBtActiveDevice").set(android.media.MediaMetrics.Property.STREAM_TYPE, android.media.AudioSystem.streamToString(streamType)).set(android.media.MediaMetrics.Property.STATE, btInfo.mState == 2 ? "connected" : "disconnected").record();
        synchronized (this.mDevicesLock) {
            java.lang.String key = com.android.server.audio.AudioDeviceInventory.DeviceInfo.makeDeviceListKey(btInfo.mAudioSystemDevice, address);
            com.android.server.audio.AudioDeviceInventory.DeviceInfo di = this.mConnectedDevices.get(key);
            boolean z = true;
            boolean isConnected = di != null;
            boolean switchToUnavailable = isConnected && btInfo.mState != 2;
            if (isConnected || btInfo.mState != 2) {
                z = false;
            }
            boolean switchToAvailable = z;
            switch (btInfo.mProfile) {
                case 1:
                    if (this.mDeviceBroker.isScoManagedByAudio()) {
                        if (switchToUnavailable) {
                            this.mDeviceBroker.onSetBtScoActiveDevice(null);
                        } else if (switchToAvailable) {
                            this.mDeviceBroker.onSetBtScoActiveDevice(btInfo.mDevice);
                        }
                    }
                    break;
                case 2:
                    if (switchToUnavailable) {
                        makeA2dpDeviceUnavailableNow(address, di.mDeviceCodecFormat);
                        this.mAdiExt.postSyncA2dpVolume(false);
                    } else if (switchToAvailable) {
                        int a2dpVolume = btInfo.mVolume;
                        if (android.os.Build.isMtkPlatform() && this.mLastMusicBTdeviceConnected == 536870912 && btInfo.mVolume == -1) {
                            a2dpVolume = this.mDeviceBroker.getVssVolumeForDevice(streamType, 536870912) / 10;
                        }
                        if (a2dpVolume != -1) {
                            int a2dpVolume2 = this.mAdiExt.getFinalA2dpVolume(btInfo.mVolume);
                            this.mDeviceBroker.postSetVolumeIndexOnDevice(3, a2dpVolume2 * 10, btInfo.mAudioSystemDevice, "onSetBtActiveDevice");
                            if (a2dpVolume2 != btInfo.mVolume) {
                                this.mAdiExt.postAbsoluteA2dpVolume(a2dpVolume2);
                            }
                        }
                        makeA2dpDeviceAvailable(btInfo, codec, "onSetBtActiveDevice");
                    }
                    break;
                case 11:
                    if (switchToUnavailable) {
                        lambda$disconnectA2dpSink$32(address);
                    } else if (switchToAvailable) {
                        makeA2dpSrcAvailable(address, codec);
                    }
                    break;
                case 21:
                    if (switchToUnavailable) {
                        lambda$disconnectHearingAid$34(address);
                    } else if (switchToAvailable) {
                        makeHearingAidDeviceAvailable(address, com.android.server.audio.BtHelper.getName(btInfo.mDevice), streamType, "onSetBtActiveDevice");
                    }
                    break;
                case 22:
                case 26:
                    if (switchToUnavailable) {
                        makeLeAudioDeviceUnavailableNow(address, btInfo.mAudioSystemDevice, di.mDeviceCodecFormat);
                    } else if (switchToAvailable) {
                        makeLeAudioDeviceAvailable(btInfo, streamType, codec, "onSetBtActiveDevice");
                        if (!this.mDeviceBroker.getWrapper().getBluetoothVolSyncSupported()) {
                            if (android.os.Build.isMtkPlatform()) {
                                if (streamType == -1) {
                                    return;
                                }
                                int leAudioVolIndex = 0;
                                if (this.mLastMusicBTdeviceConnected == 128 && btInfo.mAudioSystemDevice == 536870912 && btInfo.mVolume == -1) {
                                    leAudioVolIndex = this.mDeviceBroker.getVssVolumeForDevice(streamType, 128);
                                }
                                if (btInfo.mAudioSystemDevice == 536870912) {
                                    this.mLastMusicBTdeviceConnected = btInfo.mAudioSystemDevice;
                                }
                                if (btInfo.mVolume != -1) {
                                    leAudioVolIndex = btInfo.mVolume * 10;
                                }
                                if (leAudioVolIndex != -1) {
                                    int maxIndex = this.mDeviceBroker.getMaxVssVolumeForStream(streamType);
                                    this.mDeviceBroker.postSetLeAudioVolumeIndex(leAudioVolIndex, maxIndex, streamType);
                                    if (streamType == 3) {
                                        this.mDeviceBroker.postSetVolumeIndexOnDevice(streamType, leAudioVolIndex, btInfo.mAudioSystemDevice, "onSetBtActiveDevice");
                                    }
                                } else {
                                    int leAudioVolIndex2 = this.mDeviceBroker.getVssVolumeForDevice(streamType, btInfo.mAudioSystemDevice);
                                    int maxIndex2 = this.mDeviceBroker.getMaxVssVolumeForStream(streamType);
                                    this.mDeviceBroker.postSetLeAudioVolumeIndex(leAudioVolIndex2, maxIndex2, streamType);
                                    this.mDeviceBroker.postApplyVolumeOnDevice(streamType, btInfo.mAudioSystemDevice, "onSetBtActiveDevice");
                                }
                            }
                        } else {
                            if (streamType == -1) {
                                return;
                            }
                            if (btInfo.mVolume != -1) {
                                int leAudioVolIndex3 = this.mAdiExt.getFinalBleVolume(btInfo.mVolume * 10);
                                this.mDeviceBroker.postSetVolumeIndexOnDevice(3, leAudioVolIndex3, btInfo.mAudioSystemDevice, "onSetBtActiveDevice");
                                this.mAdiExt.postAbsoluteBleVolume(leAudioVolIndex3);
                            } else {
                                int leAudioVolIndex4 = this.mDeviceBroker.getVssVolumeForDevice(3, btInfo.mAudioSystemDevice);
                                int maxIndex3 = this.mDeviceBroker.getMaxVssVolumeForStream(3);
                                this.mDeviceBroker.postSetLeAudioVolumeIndex(leAudioVolIndex4, maxIndex3, 3);
                                this.mDeviceBroker.postApplyVolumeOnDevice(3, btInfo.mAudioSystemDevice, "onSetBtActiveDevice");
                            }
                        }
                    }
                    break;
                default:
                    throw new java.lang.IllegalArgumentException("Invalid profile " + android.bluetooth.BluetoothProfile.getProfileName(btInfo.mProfile));
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x010e, code lost:
    
        r23.mConnectedDevices.remove(r4.getKey());
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0125, code lost:
    
        r19 = r6;
        r9 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0137, code lost:
    
        r23.mConnectedDevices.put(r9, new com.android.server.audio.AudioDeviceInventory.DeviceInfo(128, com.android.server.audio.BtHelper.getName(r12), r14, r24.mDevice.getIdentityAddress(), r25));
        r0 = r23.mConnectedDevices.get(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0148, code lost:
    
        r10.set(android.media.MediaMetrics.Property.ADDRESS, r14).set(android.media.MediaMetrics.Property.ENCODING, android.media.AudioSystem.audioFormatToString(r25)).set(android.media.MediaMetrics.Property.INDEX, java.lang.Integer.valueOf(r13)).set(android.media.MediaMetrics.Property.NAME, r0.mDeviceName);
        r23.mApmConnectedDevices.replace(128, r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0175, code lost:
    
        if (r13 == (-1)) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0177, code lost:
    
        r23.mDeviceBroker.getWrapper().setBluetoothDevice(r23.mDeviceBroker.createOplusBtDeviceInfo(r12, r24.mProfile));
        r0 = r23.mAdiExt.getFinalA2dpVolume(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x018f, code lost:
    
        r23.mDeviceBroker.postSetVolumeIndexOnDevice(3, r0 * 10, 128, "onBluetoothA2dpDeviceConfigChange");
        r23.mAdiExt.postAbsoluteA2dpVolume(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x01a1, code lost:
    
        r16 = r0;
        r13 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x01a5, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x01aa, code lost:
    
        r16 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x01ad, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x01d8, code lost:
    
        r0 = th;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    int onBluetoothDeviceConfigChange(com.android.server.audio.AudioDeviceBroker.BtDeviceInfo r24, int r25, boolean r26, int r27) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 821
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.AudioDeviceInventory.onBluetoothDeviceConfigChange(com.android.server.audio.AudioDeviceBroker$BtDeviceInfo, int, boolean, int):int");
    }

    void onMakeA2dpDeviceUnavailableNow(java.lang.String address, int a2dpCodec) {
        synchronized (this.mDevicesLock) {
            makeA2dpDeviceUnavailableNow(address, a2dpCodec);
        }
    }

    void onMakeLeAudioDeviceUnavailableNow(java.lang.String address, int device, int codec) {
        synchronized (this.mDevicesLock) {
            makeLeAudioDeviceUnavailableNow(address, device, codec);
        }
    }

    void onUpdateLeAudioGroupAddresses(int groupId) {
        synchronized (this.mDevicesLock) {
            java.util.List<android.util.Pair<java.lang.String, java.lang.String>> addresses = new java.util.ArrayList<>();
            for (com.android.server.audio.AudioDeviceInventory.DeviceInfo di : this.mConnectedDevices.values()) {
                if (di.mGroupId == groupId) {
                    if (addresses.isEmpty()) {
                        addresses = this.mDeviceBroker.getLeAudioGroupAddresses(groupId);
                    }
                    if (di.mPeerDeviceAddress.equals("")) {
                        java.util.Iterator<android.util.Pair<java.lang.String, java.lang.String>> it = addresses.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            android.util.Pair<java.lang.String, java.lang.String> addr = it.next();
                            if (!di.mDeviceAddress.equals(addr.first)) {
                                di.mPeerDeviceAddress = android.text.TextUtils.emptyIfNull((java.lang.String) addr.first);
                                di.mPeerIdentityDeviceAddress = android.text.TextUtils.emptyIfNull((java.lang.String) addr.second);
                                break;
                            }
                        }
                    } else if (!addresses.contains(new android.util.Pair(di.mPeerDeviceAddress, di.mPeerIdentityDeviceAddress))) {
                        di.mPeerDeviceAddress = "";
                        di.mPeerIdentityDeviceAddress = "";
                    }
                    if (di.mDeviceIdentityAddress.equals("")) {
                        java.util.Iterator<android.util.Pair<java.lang.String, java.lang.String>> it2 = addresses.iterator();
                        while (true) {
                            if (it2.hasNext()) {
                                android.util.Pair<java.lang.String, java.lang.String> addr2 = it2.next();
                                if (di.mDeviceAddress.equals(addr2.first)) {
                                    di.mDeviceIdentityAddress = android.text.TextUtils.emptyIfNull((java.lang.String) addr2.second);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    void onReportNewRoutes() {
        android.media.AudioRoutesInfo routes;
        int n = this.mRoutesObservers.beginBroadcast();
        if (n > 0) {
            new android.media.MediaMetrics.Item("audio.device.onReportNewRoutes").set(android.media.MediaMetrics.Property.OBSERVERS, java.lang.Integer.valueOf(n)).record();
            synchronized (this.mCurAudioRoutes) {
                routes = new android.media.AudioRoutesInfo(this.mCurAudioRoutes);
            }
            while (n > 0) {
                n--;
                android.media.IAudioRoutesObserver obs = this.mRoutesObservers.getBroadcastItem(n);
                try {
                    obs.dispatchAudioRoutesChanged(routes);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(TAG, "onReportNewRoutes", e);
                }
            }
        }
        this.mRoutesObservers.finishBroadcast();
        this.mDeviceBroker.postObserveDevicesForAllStreams();
    }

    void onSetWiredDeviceConnectionState(com.android.server.audio.AudioDeviceInventory.WiredDeviceConnectionState wdcs) {
        int type = wdcs.mAttributes.getInternalType();
        com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.audio.AudioServiceEvents.WiredDevConnectEvent(wdcs));
        android.media.MediaMetrics.Item mmi = new android.media.MediaMetrics.Item("audio.device.onSetWiredDeviceConnectionState").set(android.media.MediaMetrics.Property.ADDRESS, wdcs.mAttributes.getAddress()).set(android.media.MediaMetrics.Property.DEVICE, android.media.AudioSystem.getDeviceName(type)).set(android.media.MediaMetrics.Property.STATE, wdcs.mState == 0 ? "disconnected" : "connected");
        android.media.AudioDeviceInfo info = null;
        if (wdcs.mState == 0 && android.media.AudioSystem.DEVICE_OUT_ALL_USB_SET.contains(java.lang.Integer.valueOf(wdcs.mAttributes.getInternalType()))) {
            android.media.AudioDeviceInfo[] devicesStatic = android.media.AudioManager.getDevicesStatic(2);
            int length = devicesStatic.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                android.media.AudioDeviceInfo deviceInfo = devicesStatic[i];
                if (deviceInfo.getInternalType() != wdcs.mAttributes.getInternalType()) {
                    i++;
                } else {
                    info = deviceInfo;
                    break;
                }
            }
        }
        synchronized (this.mDevicesLock) {
            boolean z = true;
            if (wdcs.mState == 0 && DEVICE_OVERRIDE_A2DP_ROUTE_ON_PLUG_SET.contains(java.lang.Integer.valueOf(type))) {
                this.mDeviceBroker.setBluetoothA2dpOnInt(true, false, "onSetWiredDeviceConnectionState state DISCONNECTED");
            }
            android.media.AudioDeviceAttributes audioDeviceAttributes = wdcs.mAttributes;
            if (wdcs.mState != 1) {
                z = false;
            }
            if (!handleDeviceConnection(audioDeviceAttributes, z, wdcs.mForTest, null)) {
                mmi.set(android.media.MediaMetrics.Property.EARLY_RETURN, "change of connection state failed").record();
                return;
            }
            if (wdcs.mState != 0) {
                if (DEVICE_OVERRIDE_A2DP_ROUTE_ON_PLUG_SET.contains(java.lang.Integer.valueOf(type))) {
                    this.mDeviceBroker.setBluetoothA2dpOnInt(false, false, "onSetWiredDeviceConnectionState state not DISCONNECTED");
                }
                this.mDeviceBroker.checkMusicActive(type, wdcs.mCaller);
            }
            if (type == 1024) {
                this.mDeviceBroker.checkVolumeCecOnHdmiConnection(wdcs.mState, wdcs.mCaller);
            }
            if (wdcs.mState == 0 && android.media.AudioSystem.DEVICE_OUT_ALL_USB_SET.contains(java.lang.Integer.valueOf(wdcs.mAttributes.getInternalType()))) {
                if (info != null) {
                    this.mDeviceBroker.dispatchPreferredMixerAttributesChangedCausedByDeviceRemoved(info);
                } else {
                    android.util.Log.e(TAG, "Didn't find AudioDeviceInfo to notify preferred mixer attributes change for type=" + wdcs.mAttributes.getType());
                }
            }
            sendDeviceConnectionIntent(type, wdcs.mState, wdcs.mAttributes.getAddress(), wdcs.mAttributes.getName());
            updateAudioRoutes(type, wdcs.mState);
            mmi.record();
        }
    }

    void onToggleHdmi() {
        android.media.MediaMetrics.Item mmi = new android.media.MediaMetrics.Item("audio.device.onToggleHdmi").set(android.media.MediaMetrics.Property.DEVICE, android.media.AudioSystem.getDeviceName(1024));
        synchronized (this.mDevicesLock) {
            java.lang.String key = com.android.server.audio.AudioDeviceInventory.DeviceInfo.makeDeviceListKey(1024, "");
            com.android.server.audio.AudioDeviceInventory.DeviceInfo di = this.mConnectedDevices.get(key);
            if (di == null) {
                android.util.Log.e(TAG, "invalid null DeviceInfo in onToggleHdmi");
                mmi.set(android.media.MediaMetrics.Property.EARLY_RETURN, "invalid null DeviceInfo").record();
                return;
            }
            if (android.os.Build.isMtkPlatform()) {
                setWiredDeviceConnectionState(new android.media.AudioDeviceAttributes(1024, ""), 0, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, false);
                setWiredDeviceConnectionState(new android.media.AudioDeviceAttributes(1024, ""), 1, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, false);
            } else {
                setWiredDeviceConnectionState(new android.media.AudioDeviceAttributes(1024, ""), 0, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
                setWiredDeviceConnectionState(new android.media.AudioDeviceAttributes(1024, ""), 1, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
            }
            mmi.record();
        }
    }

    void onSaveSetPreferredDevices(int strategy, java.util.List<android.media.AudioDeviceAttributes> devices) {
        this.mPreferredDevices.put(java.lang.Integer.valueOf(strategy), devices);
        java.util.List<android.media.AudioDeviceAttributes> nonDefaultDevices = this.mNonDefaultDevices.get(java.lang.Integer.valueOf(strategy));
        if (nonDefaultDevices != null) {
            nonDefaultDevices.removeAll(devices);
            if (nonDefaultDevices.isEmpty()) {
                this.mNonDefaultDevices.remove(java.lang.Integer.valueOf(strategy));
            } else {
                this.mNonDefaultDevices.put(java.lang.Integer.valueOf(strategy), nonDefaultDevices);
            }
            dispatchNonDefaultDevice(strategy, nonDefaultDevices);
        }
        dispatchPreferredDevice(strategy, devices);
    }

    void onSaveRemovePreferredDevices(int strategy) {
        this.mPreferredDevices.remove(java.lang.Integer.valueOf(strategy));
        dispatchPreferredDevice(strategy, new java.util.ArrayList());
    }

    void onSaveSetDeviceAsNonDefault(int strategy, android.media.AudioDeviceAttributes device) {
        java.util.List<android.media.AudioDeviceAttributes> nonDefaultDevices = this.mNonDefaultDevices.get(java.lang.Integer.valueOf(strategy));
        if (nonDefaultDevices == null) {
            nonDefaultDevices = new java.util.ArrayList();
        }
        if (!nonDefaultDevices.contains(device)) {
            nonDefaultDevices.add(device);
        }
        this.mNonDefaultDevices.put(java.lang.Integer.valueOf(strategy), nonDefaultDevices);
        dispatchNonDefaultDevice(strategy, nonDefaultDevices);
        java.util.List<android.media.AudioDeviceAttributes> preferredDevices = this.mPreferredDevices.get(java.lang.Integer.valueOf(strategy));
        if (preferredDevices != null) {
            preferredDevices.remove(device);
            this.mPreferredDevices.put(java.lang.Integer.valueOf(strategy), preferredDevices);
            dispatchPreferredDevice(strategy, preferredDevices);
        }
    }

    void onSaveRemoveDeviceAsNonDefault(int strategy, android.media.AudioDeviceAttributes device) {
        java.util.List<android.media.AudioDeviceAttributes> nonDefaultDevices = this.mNonDefaultDevices.get(java.lang.Integer.valueOf(strategy));
        if (nonDefaultDevices != null) {
            nonDefaultDevices.remove(device);
            this.mNonDefaultDevices.put(java.lang.Integer.valueOf(strategy), nonDefaultDevices);
            dispatchNonDefaultDevice(strategy, nonDefaultDevices);
        }
    }

    void onSaveSetPreferredDevicesForCapturePreset(int capturePreset, java.util.List<android.media.AudioDeviceAttributes> devices) {
        this.mPreferredDevicesForCapturePreset.put(java.lang.Integer.valueOf(capturePreset), devices);
        dispatchDevicesRoleForCapturePreset(capturePreset, 1, devices);
    }

    void onSaveClearPreferredDevicesForCapturePreset(int capturePreset) {
        this.mPreferredDevicesForCapturePreset.remove(java.lang.Integer.valueOf(capturePreset));
        dispatchDevicesRoleForCapturePreset(capturePreset, 1, new java.util.ArrayList());
    }

    int setPreferredDevicesForStrategyAndSave(int strategy, java.util.List<android.media.AudioDeviceAttributes> devices) {
        int status = setPreferredDevicesForStrategy(strategy, devices);
        if (status == 0) {
            this.mDeviceBroker.postSaveSetPreferredDevicesForStrategy(strategy, devices);
        }
        return status;
    }

    int setPreferredDevicesForStrategy(int strategy, java.util.List<android.media.AudioDeviceAttributes> devices) {
        android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
        try {
            int status = setDevicesRoleForStrategy(strategy, 1, devices, false);
            if (ignored != null) {
                ignored.close();
            }
            return status;
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

    int setPreferredDevicesForStrategyInt(int strategy, java.util.List<android.media.AudioDeviceAttributes> devices) {
        if (android.os.Build.isMtkPlatform()) {
            com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("setPreferredDevicesForStrategyInt, strategy: " + strategy + " devices: ").printLog(TAG));
            long diff = android.os.SystemClock.uptimeMillis();
            int status = setDevicesRoleForStrategy(strategy, 1, devices, true);
            if (status == 0) {
                com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("setPreferredDevicesForStrategyInt, strategy: " + strategy + ", APM made devices: preferred device in" + (android.os.SystemClock.uptimeMillis() - diff) + "ms").printLog(TAG));
            } else {
                com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("setPreferredDevicesForStrategyInt, strategy: " + strategy + ", APM fail to set devices: ").printLog(TAG));
            }
            return status;
        }
        return setDevicesRoleForStrategy(strategy, 1, devices, true);
    }

    int removePreferredDevicesForStrategyAndSave(int strategy) {
        int status = removePreferredDevicesForStrategy(strategy);
        if (status == 0) {
            this.mDeviceBroker.postSaveRemovePreferredDevicesForStrategy(strategy);
        }
        return status;
    }

    int removePreferredDevicesForStrategy(int strategy) {
        android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
        try {
            int status = clearDevicesRoleForStrategy(strategy, 1, false);
            if (ignored != null) {
                ignored.close();
            }
            return status;
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

    int removePreferredDevicesForStrategyInt(int strategy) {
        if (android.os.Build.isMtkPlatform()) {
            com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("removePreferredDevicesForStrategyInt, strategy: " + strategy).printLog(TAG));
            long diff = android.os.SystemClock.uptimeMillis();
            int status = clearDevicesRoleForStrategy(strategy, 1, true);
            if (status == 0) {
                com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("removePreferredDevicesForStrategyInt APM removed, strategy: " + strategy + ", " + (android.os.SystemClock.uptimeMillis() - diff) + "ms").printLog(TAG));
            } else {
                com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("removePreferredDevicesForStrategyInt APM fail to removed, strategy: " + strategy + ", " + (android.os.SystemClock.uptimeMillis() - diff) + "ms").printLog(TAG));
            }
            return status;
        }
        return clearDevicesRoleForStrategy(strategy, 1, true);
    }

    int setDeviceAsNonDefaultForStrategyAndSave(int strategy, android.media.AudioDeviceAttributes device) {
        android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
        try {
            java.util.List<android.media.AudioDeviceAttributes> devices = new java.util.ArrayList<>();
            devices.add(device);
            int status = addDevicesRoleForStrategy(strategy, 2, devices, false);
            if (ignored != null) {
                ignored.close();
            }
            if (status == 0) {
                this.mDeviceBroker.postSaveSetDeviceAsNonDefaultForStrategy(strategy, device);
            }
            return status;
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

    int removeDeviceAsNonDefaultForStrategyAndSave(int strategy, android.media.AudioDeviceAttributes device) {
        android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
        try {
            java.util.List<android.media.AudioDeviceAttributes> devices = new java.util.ArrayList<>();
            devices.add(device);
            int status = removeDevicesRoleForStrategy(strategy, 2, devices, false);
            if (ignored != null) {
                ignored.close();
            }
            if (status == 0) {
                this.mDeviceBroker.postSaveRemoveDeviceAsNonDefaultForStrategy(strategy, device);
            }
            return status;
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

    void registerStrategyPreferredDevicesDispatcher(android.media.IStrategyPreferredDevicesDispatcher dispatcher, boolean isPrivileged) {
        this.mPrefDevDispatchers.register(dispatcher, java.lang.Boolean.valueOf(isPrivileged));
    }

    void unregisterStrategyPreferredDevicesDispatcher(android.media.IStrategyPreferredDevicesDispatcher dispatcher) {
        this.mPrefDevDispatchers.unregister(dispatcher);
    }

    void registerStrategyNonDefaultDevicesDispatcher(android.media.IStrategyNonDefaultDevicesDispatcher dispatcher, boolean isPrivileged) {
        this.mNonDefDevDispatchers.register(dispatcher, java.lang.Boolean.valueOf(isPrivileged));
    }

    void unregisterStrategyNonDefaultDevicesDispatcher(android.media.IStrategyNonDefaultDevicesDispatcher dispatcher) {
        this.mNonDefDevDispatchers.unregister(dispatcher);
    }

    int setPreferredDevicesForCapturePresetAndSave(int capturePreset, java.util.List<android.media.AudioDeviceAttributes> devices) {
        int status = setPreferredDevicesForCapturePreset(capturePreset, devices);
        if (status == 0) {
            this.mDeviceBroker.postSaveSetPreferredDevicesForCapturePreset(capturePreset, devices);
        }
        return status;
    }

    private int setPreferredDevicesForCapturePreset(int capturePreset, java.util.List<android.media.AudioDeviceAttributes> devices) {
        android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
        try {
            int status = setDevicesRoleForCapturePreset(capturePreset, 1, devices);
            if (ignored != null) {
                ignored.close();
            }
            return status;
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

    int clearPreferredDevicesForCapturePresetAndSave(int capturePreset) {
        int status = clearPreferredDevicesForCapturePreset(capturePreset);
        if (status == 0) {
            this.mDeviceBroker.postSaveClearPreferredDevicesForCapturePreset(capturePreset);
        }
        return status;
    }

    private int clearPreferredDevicesForCapturePreset(int capturePreset) {
        android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
        try {
            int status = clearDevicesRoleForCapturePreset(capturePreset, 1);
            if (ignored != null) {
                ignored.close();
            }
            return status;
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

    private int addDevicesRoleForCapturePresetInt(int capturePreset, int role, java.util.List<android.media.AudioDeviceAttributes> devices) {
        return addDevicesRole(this.mAppliedPresetRolesInt, new com.android.server.audio.AudioDeviceInventory.AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda21
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i, int i2, java.util.List list) {
                return this.f$0.lambda$addDevicesRoleForCapturePresetInt$15(i, i2, list);
            }
        }, capturePreset, role, devices);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$addDevicesRoleForCapturePresetInt$15(int p, int r, java.util.List d) {
        return this.mAudioSystem.addDevicesRoleForCapturePreset(p, r, d);
    }

    private int removeDevicesRoleForCapturePresetInt(int capturePreset, int role, java.util.List<android.media.AudioDeviceAttributes> devices) {
        return removeDevicesRole(this.mAppliedPresetRolesInt, new com.android.server.audio.AudioDeviceInventory.AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda27
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i, int i2, java.util.List list) {
                return this.f$0.lambda$removeDevicesRoleForCapturePresetInt$16(i, i2, list);
            }
        }, capturePreset, role, devices);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$removeDevicesRoleForCapturePresetInt$16(int p, int r, java.util.List d) {
        return this.mAudioSystem.removeDevicesRoleForCapturePreset(p, r, d);
    }

    private int setDevicesRoleForCapturePreset(int capturePreset, int role, java.util.List<android.media.AudioDeviceAttributes> devices) {
        return setDevicesRole(this.mAppliedPresetRoles, new com.android.server.audio.AudioDeviceInventory.AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda5
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i, int i2, java.util.List list) {
                return this.f$0.lambda$setDevicesRoleForCapturePreset$17(i, i2, list);
            }
        }, new com.android.server.audio.AudioDeviceInventory.AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda6
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i, int i2, java.util.List list) {
                return this.f$0.lambda$setDevicesRoleForCapturePreset$18(i, i2, list);
            }
        }, capturePreset, role, devices);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$setDevicesRoleForCapturePreset$17(int p, int r, java.util.List d) {
        return this.mAudioSystem.addDevicesRoleForCapturePreset(p, r, d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$setDevicesRoleForCapturePreset$18(int p, int r, java.util.List d) {
        return this.mAudioSystem.clearDevicesRoleForCapturePreset(p, r);
    }

    private int clearDevicesRoleForCapturePreset(int capturePreset, int role) {
        return clearDevicesRole(this.mAppliedPresetRoles, new com.android.server.audio.AudioDeviceInventory.AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda10
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i, int i2, java.util.List list) {
                return this.f$0.lambda$clearDevicesRoleForCapturePreset$19(i, i2, list);
            }
        }, capturePreset, role);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$clearDevicesRoleForCapturePreset$19(int p, int r, java.util.List d) {
        return this.mAudioSystem.clearDevicesRoleForCapturePreset(p, r);
    }

    void registerCapturePresetDevicesRoleDispatcher(android.media.ICapturePresetDevicesRoleDispatcher dispatcher, boolean isPrivileged) {
        this.mDevRoleCapturePresetDispatchers.register(dispatcher, java.lang.Boolean.valueOf(isPrivileged));
    }

    void unregisterCapturePresetDevicesRoleDispatcher(android.media.ICapturePresetDevicesRoleDispatcher dispatcher) {
        this.mDevRoleCapturePresetDispatchers.unregister(dispatcher);
    }

    private int addDevicesRoleForStrategy(int strategy, int role, java.util.List<android.media.AudioDeviceAttributes> devices, boolean internal) {
        return addDevicesRole(internal ? this.mAppliedStrategyRolesInt : this.mAppliedStrategyRoles, new com.android.server.audio.AudioDeviceInventory.AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda35
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i, int i2, java.util.List list) {
                return this.f$0.lambda$addDevicesRoleForStrategy$20(i, i2, list);
            }
        }, strategy, role, devices);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$addDevicesRoleForStrategy$20(int s, int r, java.util.List d) {
        return this.mAudioSystem.setDevicesRoleForStrategy(s, r, d);
    }

    private int removeDevicesRoleForStrategy(int strategy, int role, java.util.List<android.media.AudioDeviceAttributes> devices, boolean internal) {
        return removeDevicesRole(internal ? this.mAppliedStrategyRolesInt : this.mAppliedStrategyRoles, new com.android.server.audio.AudioDeviceInventory.AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda4
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i, int i2, java.util.List list) {
                return this.f$0.lambda$removeDevicesRoleForStrategy$21(i, i2, list);
            }
        }, strategy, role, devices);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$removeDevicesRoleForStrategy$21(int s, int r, java.util.List d) {
        return this.mAudioSystem.removeDevicesRoleForStrategy(s, r, d);
    }

    private int setDevicesRoleForStrategy(int strategy, int role, java.util.List<android.media.AudioDeviceAttributes> devices, boolean internal) {
        return setDevicesRole(internal ? this.mAppliedStrategyRolesInt : this.mAppliedStrategyRoles, new com.android.server.audio.AudioDeviceInventory.AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda2
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i, int i2, java.util.List list) {
                return this.f$0.lambda$setDevicesRoleForStrategy$22(i, i2, list);
            }
        }, new com.android.server.audio.AudioDeviceInventory.AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda3
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i, int i2, java.util.List list) {
                return this.f$0.lambda$setDevicesRoleForStrategy$23(i, i2, list);
            }
        }, strategy, role, devices);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$setDevicesRoleForStrategy$22(int s, int r, java.util.List d) {
        return this.mAudioSystem.setDevicesRoleForStrategy(s, r, d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$setDevicesRoleForStrategy$23(int s, int r, java.util.List d) {
        return this.mAudioSystem.clearDevicesRoleForStrategy(s, r);
    }

    private int clearDevicesRoleForStrategy(int strategy, int role, boolean internal) {
        return clearDevicesRole(internal ? this.mAppliedStrategyRolesInt : this.mAppliedStrategyRoles, new com.android.server.audio.AudioDeviceInventory.AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda24
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i, int i2, java.util.List list) {
                return this.f$0.lambda$clearDevicesRoleForStrategy$24(i, i2, list);
            }
        }, strategy, role);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$clearDevicesRoleForStrategy$24(int s, int r, java.util.List d) {
        return this.mAudioSystem.clearDevicesRoleForStrategy(s, r);
    }

    private int addDevicesRole(android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.Integer>, java.util.List<android.media.AudioDeviceAttributes>> rolesMap, com.android.server.audio.AudioDeviceInventory.AudioSystemInterface asi, int useCase, int role, java.util.List<android.media.AudioDeviceAttributes> devices) {
        synchronized (rolesMap) {
            android.util.Pair<java.lang.Integer, java.lang.Integer> key = new android.util.Pair<>(java.lang.Integer.valueOf(useCase), java.lang.Integer.valueOf(role));
            java.util.List<android.media.AudioDeviceAttributes> roleDevices = new java.util.ArrayList<>();
            java.util.List<android.media.AudioDeviceAttributes> appliedDevices = new java.util.ArrayList<>();
            if (rolesMap.containsKey(key)) {
                roleDevices = rolesMap.get(key);
                for (android.media.AudioDeviceAttributes device : devices) {
                    if (!roleDevices.contains(device)) {
                        appliedDevices.add(device);
                    }
                }
            } else {
                appliedDevices.addAll(devices);
            }
            if (appliedDevices.isEmpty()) {
                return 0;
            }
            int status = asi.deviceRoleAction(useCase, role, appliedDevices);
            if (status == 0) {
                roleDevices.addAll(appliedDevices);
                rolesMap.put(key, roleDevices);
            }
            return status;
        }
    }

    private int removeDevicesRole(android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.Integer>, java.util.List<android.media.AudioDeviceAttributes>> rolesMap, com.android.server.audio.AudioDeviceInventory.AudioSystemInterface asi, int useCase, int role, java.util.List<android.media.AudioDeviceAttributes> devices) {
        synchronized (rolesMap) {
            android.util.Pair<java.lang.Integer, java.lang.Integer> key = new android.util.Pair<>(java.lang.Integer.valueOf(useCase), java.lang.Integer.valueOf(role));
            if (!rolesMap.containsKey(key)) {
                return -2;
            }
            java.util.List<android.media.AudioDeviceAttributes> roleDevices = rolesMap.get(key);
            java.util.List<android.media.AudioDeviceAttributes> appliedDevices = new java.util.ArrayList<>();
            for (android.media.AudioDeviceAttributes device : devices) {
                if (roleDevices.contains(device)) {
                    appliedDevices.add(device);
                }
            }
            if (appliedDevices.isEmpty()) {
                return 0;
            }
            int status = asi.deviceRoleAction(useCase, role, appliedDevices);
            if (status == 0) {
                roleDevices.removeAll(appliedDevices);
                if (roleDevices.isEmpty()) {
                    rolesMap.remove(key);
                } else {
                    rolesMap.put(key, roleDevices);
                }
            }
            return status;
        }
    }

    private static boolean devicesListEqual(java.util.List<android.media.AudioDeviceAttributes> list1, java.util.List<android.media.AudioDeviceAttributes> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        for (android.media.AudioDeviceAttributes d1 : list1) {
            boolean found = false;
            java.util.Iterator<android.media.AudioDeviceAttributes> it = list2.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                android.media.AudioDeviceAttributes d2 = it.next();
                if (d1.equalTypeAddress(d2)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    private int setDevicesRole(android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.Integer>, java.util.List<android.media.AudioDeviceAttributes>> rolesMap, com.android.server.audio.AudioDeviceInventory.AudioSystemInterface addOp, com.android.server.audio.AudioDeviceInventory.AudioSystemInterface clearOp, int useCase, int role, java.util.List<android.media.AudioDeviceAttributes> devices) {
        int status;
        synchronized (rolesMap) {
            android.util.Pair<java.lang.Integer, java.lang.Integer> key = new android.util.Pair<>(java.lang.Integer.valueOf(useCase), java.lang.Integer.valueOf(role));
            if (rolesMap.containsKey(key)) {
                if (devicesListEqual(devices, rolesMap.get(key))) {
                    return 0;
                }
            } else if (devices.isEmpty()) {
                return 0;
            }
            if (devices.isEmpty()) {
                status = clearOp.deviceRoleAction(useCase, role, null);
                if (status == 0) {
                    rolesMap.remove(key);
                }
            } else {
                status = addOp.deviceRoleAction(useCase, role, devices);
                if (status == 0) {
                    rolesMap.put(key, new java.util.ArrayList(devices));
                }
            }
            return status;
        }
    }

    private int clearDevicesRole(android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.Integer>, java.util.List<android.media.AudioDeviceAttributes>> rolesMap, com.android.server.audio.AudioDeviceInventory.AudioSystemInterface asi, int useCase, int role) {
        synchronized (rolesMap) {
            android.util.Pair<java.lang.Integer, java.lang.Integer> key = new android.util.Pair<>(java.lang.Integer.valueOf(useCase), java.lang.Integer.valueOf(role));
            if (!rolesMap.containsKey(key)) {
                return -2;
            }
            int status = asi.deviceRoleAction(useCase, role, null);
            if (status == 0) {
                rolesMap.remove(key);
            }
            return status;
        }
    }

    private void purgeDevicesRoles_l() {
        purgeRoles(this.mAppliedStrategyRolesInt, new com.android.server.audio.AudioDeviceInventory.AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda25
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i, int i2, java.util.List list) {
                return this.f$0.lambda$purgeDevicesRoles_l$25(i, i2, list);
            }
        });
        purgeRoles(this.mAppliedPresetRolesInt, new com.android.server.audio.AudioDeviceInventory.AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda26
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i, int i2, java.util.List list) {
                return this.f$0.lambda$purgeDevicesRoles_l$26(i, i2, list);
            }
        });
        reapplyExternalDevicesRoles();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$purgeDevicesRoles_l$25(int s, int r, java.util.List d) {
        return this.mAudioSystem.removeDevicesRoleForStrategy(s, r, d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$purgeDevicesRoles_l$26(int p, int r, java.util.List d) {
        return this.mAudioSystem.removeDevicesRoleForCapturePreset(p, r, d);
    }

    private void purgeRoles(android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.Integer>, java.util.List<android.media.AudioDeviceAttributes>> rolesMap, com.android.server.audio.AudioDeviceInventory.AudioSystemInterface asi) {
        synchronized (rolesMap) {
            android.media.AudioDeviceInfo[] connectedDevices = android.media.AudioManager.getDevicesStatic(3);
            java.util.Iterator<java.util.Map.Entry<android.util.Pair<java.lang.Integer, java.lang.Integer>, java.util.List<android.media.AudioDeviceAttributes>>> itRole = rolesMap.entrySet().iterator();
            while (itRole.hasNext()) {
                java.util.Map.Entry<android.util.Pair<java.lang.Integer, java.lang.Integer>, java.util.List<android.media.AudioDeviceAttributes>> entry = itRole.next();
                android.util.Pair<java.lang.Integer, java.lang.Integer> keyRole = entry.getKey();
                java.util.Iterator<android.media.AudioDeviceAttributes> itDev = rolesMap.get(keyRole).iterator();
                while (itDev.hasNext()) {
                    final android.media.AudioDeviceAttributes ada = itDev.next();
                    android.media.AudioDeviceInfo device = (android.media.AudioDeviceInfo) java.util.stream.Stream.of((java.lang.Object[]) connectedDevices).filter(new java.util.function.Predicate() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda31
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return com.android.server.audio.AudioDeviceInventory.lambda$purgeRoles$27(ada, (android.media.AudioDeviceInfo) obj);
                        }
                    }).filter(new java.util.function.Predicate() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda32
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return com.android.server.audio.AudioDeviceInventory.lambda$purgeRoles$28(ada, (android.media.AudioDeviceInfo) obj);
                        }
                    }).findFirst().orElse(null);
                    if (device == null) {
                        if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                            android.util.Slog.i(TAG, "purgeRoles() removing device: " + ada.toString() + ", for strategy: " + keyRole.first + " and role: " + keyRole.second);
                        }
                        asi.deviceRoleAction(((java.lang.Integer) keyRole.first).intValue(), ((java.lang.Integer) keyRole.second).intValue(), java.util.Arrays.asList(ada));
                        itDev.remove();
                    }
                }
                if (rolesMap.get(keyRole).isEmpty()) {
                    itRole.remove();
                }
            }
        }
    }

    static /* synthetic */ boolean lambda$purgeRoles$27(android.media.AudioDeviceAttributes ada, android.media.AudioDeviceInfo d) {
        return d.getInternalType() == ada.getInternalType();
    }

    static /* synthetic */ boolean lambda$purgeRoles$28(android.media.AudioDeviceAttributes ada, android.media.AudioDeviceInfo d) {
        return !android.media.AudioSystem.isBluetoothDevice(d.getInternalType()) || d.getAddress().equals(ada.getAddress());
    }

    public boolean isDeviceConnected(android.media.AudioDeviceAttributes device) {
        boolean z;
        java.lang.String key = com.android.server.audio.AudioDeviceInventory.DeviceInfo.makeDeviceListKey(device.getInternalType(), device.getAddress());
        synchronized (this.mDevicesLock) {
            z = this.mConnectedDevices.get(key) != null;
        }
        return z;
    }

    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:119)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    /* JADX WARN: Not initialized variable reg: 17, insn: 0x030a: MOVE (r2 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r17 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('status' boolean)]), block:B:69:0x030a */
    /* JADX WARN: Type inference failed for: r17v1 */
    boolean handleDeviceConnection(android.media.AudioDeviceAttributes audioDeviceAttributes, boolean z, boolean z2, android.bluetooth.BluetoothDevice bluetoothDevice) throws java.lang.Throwable {
        boolean z3;
        boolean z4;
        com.android.server.audio.AudioDeviceInventory.DeviceInfo deviceInfo;
        boolean z5;
        int i;
        boolean z6;
        int deviceConnectionState;
        java.lang.String outputDeviceName;
        int internalType = audioDeviceAttributes.getInternalType();
        java.lang.String address = audioDeviceAttributes.getAddress();
        java.lang.String name = audioDeviceAttributes.getName();
        if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
            android.util.Slog.i(TAG, "handleDeviceConnection(" + z + " dev:" + java.lang.Integer.toHexString(internalType) + " address:" + address + " name:" + name + ")");
            if ((internalType & Integer.MIN_VALUE) == Integer.MIN_VALUE) {
                outputDeviceName = android.media.AudioSystem.getInputDeviceName(internalType);
            } else {
                outputDeviceName = android.media.AudioSystem.getOutputDeviceName(internalType);
            }
            android.util.Log.i(TAG, "handleDeviceConnection(" + z + " dev:" + java.lang.Integer.toHexString(internalType) + "[" + outputDeviceName + "] address:" + address + " name:" + name + ")");
        }
        android.media.MediaMetrics.Item item = new android.media.MediaMetrics.Item("audio.device.handleDeviceConnection").set(android.media.MediaMetrics.Property.ADDRESS, address).set(android.media.MediaMetrics.Property.DEVICE, android.media.AudioSystem.getDeviceName(internalType)).set(android.media.MediaMetrics.Property.MODE, z ? "connect" : "disconnect").set(android.media.MediaMetrics.Property.NAME, name);
        synchronized (this.mDevicesLock) {
            try {
                try {
                    java.lang.String strMakeDeviceListKey = com.android.server.audio.AudioDeviceInventory.DeviceInfo.makeDeviceListKey(internalType, address);
                    if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                        android.util.Slog.i(TAG, "deviceKey:" + strMakeDeviceListKey);
                    }
                    com.android.server.audio.AudioDeviceInventory.DeviceInfo deviceInfo2 = this.mConnectedDevices.get(strMakeDeviceListKey);
                    boolean z7 = deviceInfo2 != null;
                    if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                        android.util.Slog.i(TAG, "deviceInfo:" + deviceInfo2 + " is(already)Connected:" + z7);
                    }
                    if (z && z7) {
                        android.util.Log.e(TAG, "handleDeviceConnection device already connect ,disconnected frist dev:" + java.lang.Integer.toHexString(internalType));
                        int deviceConnectionState2 = this.mAudioSystem.setDeviceConnectionState(audioDeviceAttributes, 0, 0);
                        if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                            android.util.Log.i(TAG, "handleDeviceConnection(disconnected dev:" + java.lang.Integer.toHexString(internalType) + ", res=" + deviceConnectionState2 + ")");
                        }
                        this.mConnectedDevices.remove(strMakeDeviceListKey);
                        item.set(android.media.MediaMetrics.Property.EVENT, "device already connect");
                        item.set(android.media.MediaMetrics.Property.STATE, "disconnected").record();
                        z3 = false;
                    } else {
                        z3 = z7;
                    }
                    try {
                        if (!z || z3) {
                            z4 = false;
                            deviceInfo = deviceInfo2;
                            if (!z && z3) {
                                int deviceConnectionState3 = this.mAudioSystem.setDeviceConnectionState(audioDeviceAttributes, 0, 0);
                                if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                                    android.util.Log.i(TAG, "handleDeviceConnection(disconnected dev:" + java.lang.Integer.toHexString(internalType) + ", res=" + deviceConnectionState3 + ")");
                                }
                                this.mConnectedDevices.remove(strMakeDeviceListKey);
                                this.mDeviceBroker.postCheckCommunicationDeviceRemoval(audioDeviceAttributes);
                                i = internalType;
                                z4 = true;
                                z5 = false;
                            } else if (z || z3) {
                                z5 = false;
                                i = internalType;
                            } else {
                                if ((internalType & 16) == 16) {
                                    internalType = 32;
                                } else if ((internalType & 32) == 32) {
                                    internalType = 16;
                                }
                                java.lang.String strMakeDeviceListKey2 = com.android.server.audio.AudioDeviceInventory.DeviceInfo.makeDeviceListKey(internalType, address);
                                if (this.mConnectedDevices.get(strMakeDeviceListKey2) == null) {
                                    z5 = false;
                                    i = internalType;
                                } else {
                                    z5 = false;
                                    this.mAudioSystem.setDeviceConnectionState(new android.media.AudioDeviceAttributes(internalType, address, name), 0, 0);
                                    this.mConnectedDevices.remove(strMakeDeviceListKey2);
                                    item.set(android.media.MediaMetrics.Property.STATE, "connected").record();
                                    i = internalType;
                                    z4 = true;
                                }
                            }
                        } else {
                            boolean zOplusHeadsetFadeInit = this.mDeviceBroker.getWrapper().getExtImpl().oplusHeadsetFadeInit(internalType);
                            if (z2) {
                                deviceConnectionState = 0;
                            } else {
                                deviceConnectionState = this.mAudioSystem.setDeviceConnectionState(audioDeviceAttributes, 1, 0);
                                if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                                    android.util.Log.i(TAG, "handleDeviceConnection(connected dev:" + java.lang.Integer.toHexString(internalType) + ", res=" + deviceConnectionState + ")");
                                }
                            }
                            if (deviceConnectionState != 0) {
                                java.lang.String str = "not connecting device 0x" + java.lang.Integer.toHexString(internalType) + " due to command error " + deviceConnectionState;
                                if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                                    android.util.Slog.e(TAG, str);
                                }
                                item.set(android.media.MediaMetrics.Property.EARLY_RETURN, str).set(android.media.MediaMetrics.Property.STATE, "disconnected").record();
                                com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("APM failed to make available device 0x" + java.lang.Integer.toHexString(internalType) + "addr=" + address + " error=" + deviceConnectionState).printSlog(1, TAG));
                                if (zOplusHeadsetFadeInit) {
                                    this.mDeviceBroker.getWrapper().getExtImpl().oplusHeadsetFadeSkipFadeIn(internalType);
                                }
                                if (internalType == -2147483640) {
                                    disconnectSco();
                                }
                                return false;
                            }
                            deviceInfo = deviceInfo2;
                            this.mConnectedDevices.put(strMakeDeviceListKey, new com.android.server.audio.AudioDeviceInventory.DeviceInfo(internalType, name, address));
                            if (zOplusHeadsetFadeInit) {
                                this.mDeviceBroker.getWrapper().getExtImpl().oplusHeadsetFadeBeginFadeIn();
                            }
                            i = internalType;
                            z4 = true;
                            z5 = false;
                        }
                        try {
                            if (z4) {
                                if (android.media.AudioSystem.isBluetoothScoDevice(i)) {
                                    updateBluetoothPreferredModes_l(z ? bluetoothDevice : null);
                                    if (!z) {
                                        purgeDevicesRoles_l();
                                        z6 = z5;
                                    } else {
                                        z6 = z5;
                                        addAudioDeviceInInventoryIfNeeded(i, address, "", com.android.server.audio.BtHelper.getBtDeviceCategory(address), false);
                                    }
                                    com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("SCO " + (android.media.AudioSystem.isInputDevice(i) ? "source" : "sink") + " device addr=" + address + (z ? " now available" : " made unavailable")).printSlog(z6 ? 1 : 0, TAG));
                                } else {
                                    z6 = z5;
                                }
                                item.set(android.media.MediaMetrics.Property.STATE, "connected").record();
                            } else {
                                z6 = z5;
                                android.util.Log.w(TAG, "handleDeviceConnection() failed, deviceKey=" + strMakeDeviceListKey + ", deviceSpec=" + deviceInfo + ", connect=" + z);
                                item.set(android.media.MediaMetrics.Property.STATE, "disconnected").record();
                            }
                            if (this.mAdiExt.isMetaAudioSupport() && bluetoothDevice == null) {
                                this.mDeviceBroker.getWrapper().checkHoloDeviceSupportState(true, z, z6);
                            }
                            return z4;
                        } catch (java.lang.Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
        }
    }

    private void disconnectA2dp() {
        synchronized (this.mDevicesLock) {
            final android.util.ArraySet<java.lang.String> toRemove = new android.util.ArraySet<>();
            this.mConnectedDevices.values().forEach(new java.util.function.Consumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda8
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.audio.AudioDeviceInventory.lambda$disconnectA2dp$29(toRemove, (com.android.server.audio.AudioDeviceInventory.DeviceInfo) obj);
                }
            });
            new android.media.MediaMetrics.Item("audio.device.disconnectA2dp").set(android.media.MediaMetrics.Property.EVENT, "disconnectA2dp").record();
            if (toRemove.size() > 0) {
                final int delay = checkSendBecomingNoisyIntentInt(128, 0, 0);
                toRemove.stream().forEach(new java.util.function.Consumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda9
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$disconnectA2dp$30(delay, (java.lang.String) obj);
                    }
                });
            }
        }
    }

    static /* synthetic */ void lambda$disconnectA2dp$29(android.util.ArraySet toRemove, com.android.server.audio.AudioDeviceInventory.DeviceInfo deviceInfo) {
        if (deviceInfo.mDeviceType == 128) {
            toRemove.add(deviceInfo.mDeviceAddress);
        }
    }

    private void disconnectA2dpSink() {
        synchronized (this.mDevicesLock) {
            final android.util.ArraySet<java.lang.String> toRemove = new android.util.ArraySet<>();
            this.mConnectedDevices.values().forEach(new java.util.function.Consumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda29
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.audio.AudioDeviceInventory.lambda$disconnectA2dpSink$31(toRemove, (com.android.server.audio.AudioDeviceInventory.DeviceInfo) obj);
                }
            });
            new android.media.MediaMetrics.Item("audio.device.disconnectA2dpSink").set(android.media.MediaMetrics.Property.EVENT, "disconnectA2dpSink").record();
            toRemove.stream().forEach(new java.util.function.Consumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda30
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$disconnectA2dpSink$32((java.lang.String) obj);
                }
            });
        }
    }

    static /* synthetic */ void lambda$disconnectA2dpSink$31(android.util.ArraySet toRemove, com.android.server.audio.AudioDeviceInventory.DeviceInfo deviceInfo) {
        if (deviceInfo.mDeviceType == -2147352576) {
            toRemove.add(deviceInfo.mDeviceAddress);
        }
    }

    private void disconnectHearingAid() {
        synchronized (this.mDevicesLock) {
            final android.util.ArraySet<java.lang.String> toRemove = new android.util.ArraySet<>();
            this.mConnectedDevices.values().forEach(new java.util.function.Consumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda33
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.audio.AudioDeviceInventory.lambda$disconnectHearingAid$33(toRemove, (com.android.server.audio.AudioDeviceInventory.DeviceInfo) obj);
                }
            });
            new android.media.MediaMetrics.Item("audio.device.disconnectHearingAid").set(android.media.MediaMetrics.Property.EVENT, "disconnectHearingAid").record();
            if (toRemove.size() > 0) {
                checkSendBecomingNoisyIntentInt(134217728, 0, 0);
                toRemove.stream().forEach(new java.util.function.Consumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda34
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$disconnectHearingAid$34((java.lang.String) obj);
                    }
                });
            }
        }
    }

    static /* synthetic */ void lambda$disconnectHearingAid$33(android.util.ArraySet toRemove, com.android.server.audio.AudioDeviceInventory.DeviceInfo deviceInfo) {
        if (deviceInfo.mDeviceType == 134217728) {
            toRemove.add(deviceInfo.mDeviceAddress);
        }
    }

    void onBtProfileDisconnected(int profile) {
        switch (profile) {
            case 1:
                disconnectHeadset();
                break;
            case 2:
                disconnectA2dp();
                break;
            case 11:
                disconnectA2dpSink();
                break;
            case 21:
                disconnectHearingAid();
                break;
            case 22:
                disconnectLeAudioUnicast();
                break;
            case 26:
                disconnectLeAudioBroadcast();
                break;
            default:
                android.util.Log.e(TAG, "onBtProfileDisconnected: Not a valid profile to disconnect " + android.bluetooth.BluetoothProfile.getProfileName(profile));
                break;
        }
    }

    void disconnectLeAudio(final int device) {
        final int delay;
        if (device != 536870912 && device != -1610612736 && device != 536870913 && device != 536870914) {
            android.util.Log.e(TAG, "disconnectLeAudio: Can't disconnect not LE Audio device " + device);
            return;
        }
        synchronized (this.mDevicesLock) {
            final android.util.ArraySet<android.util.Pair<java.lang.String, java.lang.Integer>> toRemove = new android.util.ArraySet<>();
            this.mConnectedDevices.values().forEach(new java.util.function.Consumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.audio.AudioDeviceInventory.lambda$disconnectLeAudio$35(device, toRemove, (com.android.server.audio.AudioDeviceInventory.DeviceInfo) obj);
                }
            });
            new android.media.MediaMetrics.Item("audio.device.disconnectLeAudio").set(android.media.MediaMetrics.Property.EVENT, "disconnectLeAudio").record();
            if (toRemove.size() > 0) {
                if (device != -1610612736) {
                    delay = checkSendBecomingNoisyIntentInt(device, 0, 0);
                } else {
                    delay = 0;
                }
                toRemove.stream().forEach(new java.util.function.Consumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$disconnectLeAudio$36(device, delay, (android.util.Pair) obj);
                    }
                });
            }
        }
    }

    static /* synthetic */ void lambda$disconnectLeAudio$35(int device, android.util.ArraySet toRemove, com.android.server.audio.AudioDeviceInventory.DeviceInfo deviceInfo) {
        if (deviceInfo.mDeviceType == device) {
            toRemove.add(new android.util.Pair(deviceInfo.mDeviceAddress, java.lang.Integer.valueOf(deviceInfo.mDeviceCodecFormat)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$disconnectLeAudio$36(int device, int delay, android.util.Pair entry) {
        makeLeAudioDeviceUnavailableLater((java.lang.String) entry.first, device, ((java.lang.Integer) entry.second).intValue(), delay);
    }

    void disconnectLeAudioUnicast() {
        disconnectLeAudio(536870912);
        disconnectLeAudio(-1610612736);
        disconnectLeAudio(536870913);
    }

    void disconnectLeAudioBroadcast() {
        disconnectLeAudio(536870914);
    }

    private void disconnectHeadset() {
        boolean disconnect = false;
        synchronized (this.mDevicesLock) {
            java.util.Iterator<com.android.server.audio.AudioDeviceInventory.DeviceInfo> it = this.mConnectedDevices.values().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                com.android.server.audio.AudioDeviceInventory.DeviceInfo di = it.next();
                if (android.media.AudioSystem.isBluetoothScoDevice(di.mDeviceType)) {
                    disconnect = true;
                    break;
                }
            }
        }
        if (disconnect) {
            this.mDeviceBroker.onSetBtScoActiveDevice(null);
        }
    }

    int checkSendBecomingNoisyIntent(int device, int state, int musicDevice) {
        int iCheckSendBecomingNoisyIntentInt;
        synchronized (this.mDevicesLock) {
            iCheckSendBecomingNoisyIntentInt = checkSendBecomingNoisyIntentInt(device, state, musicDevice);
        }
        return iCheckSendBecomingNoisyIntentInt;
    }

    android.media.AudioRoutesInfo startWatchingRoutes(android.media.IAudioRoutesObserver observer) {
        android.media.AudioRoutesInfo routes;
        synchronized (this.mCurAudioRoutes) {
            routes = new android.media.AudioRoutesInfo(this.mCurAudioRoutes);
            this.mRoutesObservers.register(observer);
        }
        return routes;
    }

    android.media.AudioRoutesInfo getCurAudioRoutes() {
        return this.mCurAudioRoutes;
    }

    public int setBluetoothActiveDevice(com.android.server.audio.AudioDeviceBroker.BtDeviceInfo info) {
        int asState;
        int asState2;
        synchronized (this.mDevicesLock) {
            if (!info.mSupprNoisy && (((info.mProfile == 22 || info.mProfile == 26) && info.mIsLeOutput) || info.mProfile == 21 || info.mProfile == 2)) {
                if (info.mState == 2) {
                    asState2 = 1;
                } else {
                    asState2 = 0;
                }
                asState = checkSendBecomingNoisyIntentInt(info.mAudioSystemDevice, asState2, info.mMusicDevice);
            } else {
                asState = 0;
            }
            if (info.mState == 2) {
                android.util.Log.d(TAG, "20H setBluetoothA2dpDeviceConnectionState checkMusicActive");
                this.mDeviceBroker.checkMusicActive(128, TAG);
            }
            if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                android.util.Log.i(TAG, "setBluetoothActiveDevice " + info.toString() + " delay(ms): " + asState);
            }
            this.mDeviceBroker.postBluetoothActiveDevice(info, asState);
        }
        return asState;
    }

    void handleBluetoothA2dpActiveDeviceChangeExt(android.bluetooth.BluetoothDevice device, int state, int profile, boolean suppressNoisyIntent, int a2dpVolume) {
        this.mAdiExt.setActiveA2dpDeviceClass(device, state);
        if (state == 0) {
            this.mDeviceBroker.queueOnBluetoothActiveDeviceChanged(new com.android.server.audio.AudioDeviceBroker.BtDeviceChangedData(null, device, new android.media.BluetoothProfileConnectionInfo(profile), "AudioDeviceInventory"));
            com.android.server.audio.BtHelper.SetA2dpActiveDevice(null);
            return;
        }
        if (state == 2 && profile == 11) {
            this.mDeviceBroker.queueOnBluetoothActiveDeviceChanged(new com.android.server.audio.AudioDeviceBroker.BtDeviceChangedData(device, null, new android.media.BluetoothProfileConnectionInfo(profile), "AudioDeviceInventory"));
            return;
        }
        synchronized (this.mConnectedDevices) {
            java.lang.String address = device.getAddress();
            com.android.server.audio.BtHelper btHelper = new com.android.server.audio.BtHelper(this.mDeviceBroker, this.mDeviceBroker.getContext());
            android.util.Pair<java.lang.Integer, java.lang.Boolean> codecAndChanged = btHelper.getCodecWithFallback(device, profile, false, "MSG_L_A2DP_ACTIVE_DEVICE_CHANGE_EXT");
            int a2dpCodec = ((java.lang.Integer) codecAndChanged.first).intValue();
            java.lang.String deviceKey = com.android.server.audio.AudioDeviceInventory.DeviceInfo.makeDeviceListKey(128, address);
            com.android.server.audio.AudioDeviceInventory.DeviceInfo deviceInfo = this.mConnectedDevices.get(deviceKey);
            if (deviceInfo != null && a2dpCodec != deviceInfo.mDeviceCodecFormat) {
                this.mDeviceBroker.postBluetoothDeviceConfigChange(new com.android.server.audio.AudioDeviceBroker.BtDeviceInfo(device, profile));
                return;
            }
            for (java.util.Map.Entry<java.lang.String, com.android.server.audio.AudioDeviceInventory.DeviceInfo> existingDevice : this.mConnectedDevices.entrySet()) {
                if (existingDevice.getValue().mDeviceType == 128) {
                    this.mConnectedDevices.remove(existingDevice.getKey());
                    this.mConnectedDevices.put(deviceKey, new com.android.server.audio.AudioDeviceInventory.DeviceInfo(128, com.android.server.audio.BtHelper.getName(device), address, "", a2dpCodec));
                    if (com.android.server.audio.BtHelper.isTwsPlusSwitch(device, existingDevice.getValue().mDeviceAddress)) {
                        com.android.server.audio.BtHelper.SetA2dpActiveDevice(device);
                        if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                            android.util.Log.d(TAG, "TWS+ device switch");
                        }
                        return;
                    }
                    return;
                }
            }
            this.mDeviceBroker.queueOnBluetoothActiveDeviceChanged(new com.android.server.audio.AudioDeviceBroker.BtDeviceChangedData(device, null, new android.media.BluetoothProfileConnectionInfo(profile), "AudioDeviceInventory"));
        }
    }

    int setWiredDeviceConnectionState(android.media.AudioDeviceAttributes attributes, int state, java.lang.String caller) {
        int delay;
        synchronized (this.mDevicesLock) {
            if (state == 2) {
                state = 0;
            }
            delay = checkSendBecomingNoisyIntentInt(attributes.getInternalType(), state, 0);
            this.mDeviceBroker.postSetWiredDeviceConnectionState(new com.android.server.audio.AudioDeviceInventory.WiredDeviceConnectionState(attributes, state, caller), delay);
        }
        return delay;
    }

    int setWiredDeviceConnectionState(android.media.AudioDeviceAttributes attributes, int state, java.lang.String caller, boolean suppressNoisyIntent) {
        int delay;
        synchronized (this.mDevicesLock) {
            if (state == 2) {
                state = 0;
            }
            delay = 0;
            if (!suppressNoisyIntent) {
                delay = checkSendBecomingNoisyIntentInt(attributes.getInternalType(), state, 0);
                android.util.Log.i(TAG, "setWiredDeviceConnectionState(), " + suppressNoisyIntent);
                this.mDeviceBroker.postSetWiredDeviceConnectionState(new com.android.server.audio.AudioDeviceInventory.WiredDeviceConnectionState(attributes, state, caller), delay);
            } else {
                android.util.Log.i(TAG, "setWiredDeviceConnectionState(), " + suppressNoisyIntent);
                this.mDeviceBroker.postSetWiredDeviceConnectionState(new com.android.server.audio.AudioDeviceInventory.WiredDeviceConnectionState(attributes, state, caller), delay);
            }
        }
        return delay;
    }

    void setTestDeviceConnectionState(android.media.AudioDeviceAttributes device, int state) {
        com.android.server.audio.AudioDeviceInventory.WiredDeviceConnectionState connection = new com.android.server.audio.AudioDeviceInventory.WiredDeviceConnectionState(device, state, "com.android.server.audio");
        connection.mForTest = true;
        onSetWiredDeviceConnectionState(connection);
    }

    private void makeA2dpDeviceAvailable(com.android.server.audio.AudioDeviceBroker.BtDeviceInfo btInfo, int codec, java.lang.String eventSource) {
        java.lang.String address = btInfo.mDevice.getAddress();
        java.lang.String name = com.android.server.audio.BtHelper.getName(btInfo.mDevice);
        this.mDeviceBroker.setBluetoothA2dpOnInt(true, true, eventSource);
        android.os.SystemClock.uptimeMillis();
        android.media.AudioDeviceAttributes ada = new android.media.AudioDeviceAttributes(128, address, name);
        int res = this.mAudioSystem.setDeviceConnectionState(ada, 1, codec);
        if (res != 0) {
            com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("APM failed to make available A2DP device addr=" + android.media.Utils.anonymizeBluetoothAddress(address) + " error=" + res).printSlog(1, TAG));
            if (res != 100) {
                return;
            }
        } else {
            com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("A2DP sink device addr=" + android.media.Utils.anonymizeBluetoothAddress(address) + " now available").printSlog(0, TAG));
            this.mAudioSystem.setParameters(BIRECORD_DEVICE_NAME_PARAM + name);
        }
        if (android.os.Build.isMtkPlatform()) {
            java.lang.String scoEnable = android.media.AudioSystem.getParameters("BTAudiosuspend");
            java.lang.String[] strEnable = null;
            if (scoEnable != null) {
                if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                    android.util.Log.i(TAG, "SCO status at BT end" + scoEnable);
                }
                strEnable = scoEnable.split("=");
            }
            if ((strEnable != null && strEnable.length >= 2 && (strEnable[1].equals("true") || strEnable[1].equals("1"))) || this.mDeviceBroker.isBluetoothScoActive()) {
                android.util.Log.i(TAG, "SCO is  on");
            } else {
                this.mAudioSystem.setParameters("A2dpsuspendonly=false");
            }
        }
        this.mDeviceBroker.clearA2dpSuspended(true);
        com.android.server.audio.AudioDeviceInventory.DeviceInfo di = new com.android.server.audio.AudioDeviceInventory.DeviceInfo(128, name, address, btInfo.mDevice.getIdentityAddress(), codec);
        java.lang.String diKey = di.getKey();
        this.mConnectedDevices.put(diKey, di);
        this.mApmConnectedDevices.put(128, diKey);
        this.mDeviceBroker.postAccessoryPlugMediaUnmute(128);
        setCurrentAudioRouteNameIfPossible(name, true);
        updateBluetoothPreferredModes_l(btInfo.mDevice);
        addAudioDeviceInInventoryIfNeeded(128, address, "", com.android.server.audio.BtHelper.getBtDeviceCategory(address), false);
        if (this.mAdiExt.isMetaAudioSupport()) {
            this.mDeviceBroker.getWrapper().checkHoloDeviceSupportState(false, true, false);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:83:0x019a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void applyConnectedDevicesRoles_l() {
        /*
            Method dump skipped, instruction units count: 678
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.AudioDeviceInventory.applyConnectedDevicesRoles_l():void");
    }

    void applyConnectedDevicesRoles() {
        synchronized (this.mDevicesLock) {
            applyConnectedDevicesRoles_l();
        }
    }

    int checkProfileIsConnected(int profile) {
        switch (profile) {
            case 1:
                if (getFirstConnectedDeviceOfTypes(android.media.AudioSystem.DEVICE_OUT_ALL_SCO_SET) != null || getFirstConnectedDeviceOfTypes(android.media.AudioSystem.DEVICE_IN_ALL_SCO_SET) != null) {
                }
                break;
            case 2:
                if (getFirstConnectedDeviceOfTypes(android.media.AudioSystem.DEVICE_OUT_ALL_A2DP_SET) != null) {
                }
                break;
            case 22:
            case 26:
                if (getFirstConnectedDeviceOfTypes(android.media.AudioSystem.DEVICE_OUT_ALL_BLE_SET) != null || getFirstConnectedDeviceOfTypes(android.media.AudioSystem.DEVICE_IN_ALL_BLE_SET) != null) {
                }
                break;
        }
        return profile;
    }

    private void updateBluetoothPreferredModes_l(android.bluetooth.BluetoothDevice connectedDevice) {
        int profile;
        if (!this.mBluetoothDualModeEnabled) {
            return;
        }
        java.util.HashSet<java.lang.String> processedAddresses = new java.util.HashSet<>(0);
        for (com.android.server.audio.AudioDeviceInventory.DeviceInfo di : this.mConnectedDevices.values()) {
            if (android.media.AudioSystem.isBluetoothDevice(di.mDeviceType) && !processedAddresses.contains(di.mDeviceAddress)) {
                android.os.Bundle preferredProfiles = com.android.server.audio.BtHelper.getPreferredAudioProfiles(di.mDeviceAddress);
                if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                    android.util.Log.i(TAG, "updateBluetoothPreferredModes_l processing device address: " + di.mDeviceAddress + ", preferredProfiles: " + preferredProfiles);
                }
                for (com.android.server.audio.AudioDeviceInventory.DeviceInfo di2 : this.mConnectedDevices.values()) {
                    if (android.media.AudioSystem.isBluetoothDevice(di2.mDeviceType) && di.mDeviceAddress.equals(di2.mDeviceAddress) && (profile = com.android.server.audio.BtHelper.getProfileFromType(di2.mDeviceType)) != 0) {
                        int preferredProfile = checkProfileIsConnected(preferredProfiles.getInt("audio_mode_duplex"));
                        if (preferredProfile == profile || preferredProfile == 0) {
                            di2.setModeEnabled("audio_mode_duplex");
                        } else {
                            di2.setModeDisabled("audio_mode_duplex");
                        }
                        int preferredProfile2 = checkProfileIsConnected(preferredProfiles.getInt("audio_mode_output_only"));
                        if (preferredProfile2 == profile || preferredProfile2 == 0) {
                            di2.setModeEnabled("audio_mode_output_only");
                        } else {
                            di2.setModeDisabled("audio_mode_output_only");
                        }
                    }
                }
                processedAddresses.add(di.mDeviceAddress);
            }
        }
        applyConnectedDevicesRoles_l();
        if (connectedDevice != null) {
            this.mDeviceBroker.postNotifyPreferredAudioProfileApplied(connectedDevice);
        }
    }

    private void makeA2dpDeviceUnavailableNow(java.lang.String address, int codec) {
        android.media.MediaMetrics.Item mmi = new android.media.MediaMetrics.Item("audio.device.a2dp." + address).set(android.media.MediaMetrics.Property.ENCODING, android.media.AudioSystem.audioFormatToString(codec)).set(android.media.MediaMetrics.Property.EVENT, "makeA2dpDeviceUnavailableNow");
        if (address == null) {
            mmi.set(android.media.MediaMetrics.Property.EARLY_RETURN, "address null").record();
            return;
        }
        java.lang.String deviceToRemoveKey = com.android.server.audio.AudioDeviceInventory.DeviceInfo.makeDeviceListKey(128, address);
        this.mConnectedDevices.remove(deviceToRemoveKey);
        if (!deviceToRemoveKey.equals(this.mApmConnectedDevices.get(128))) {
            com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("A2DP device " + android.media.Utils.anonymizeBluetoothAddress(address) + " made unavailable, was not used").printSlog(0, TAG));
            mmi.set(android.media.MediaMetrics.Property.EARLY_RETURN, "A2DP device made unavailable, was not used").record();
        }
        this.mDeviceBroker.getWrapper().getExtImpl().clearAvrcpAbsoluteVolumeSupportedwithAddr(address);
        android.os.SystemClock.uptimeMillis();
        android.media.AudioDeviceAttributes ada = new android.media.AudioDeviceAttributes(128, address);
        int res = this.mAudioSystem.setDeviceConnectionState(ada, 0, codec);
        if (res != 0) {
            com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("APM failed to make unavailable A2DP device addr=" + android.media.Utils.anonymizeBluetoothAddress(address) + " error=" + res).printSlog(1, TAG));
        } else {
            com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("A2DP device addr=" + android.media.Utils.anonymizeBluetoothAddress(address) + " made unavailable").printSlog(0, TAG));
        }
        this.mAudioSystem.setParameters(EXIT_GAME_MODE_PARAM);
        this.mApmConnectedDevices.remove(128);
        setCurrentAudioRouteNameIfPossible(null, true);
        mmi.record();
        updateBluetoothPreferredModes_l(null);
        purgeDevicesRoles_l();
        this.mDeviceBroker.postCheckCommunicationDeviceRemoval(ada);
        if (this.mAdiExt.isMetaAudioSupport()) {
            this.mDeviceBroker.getWrapper().checkHoloDeviceSupportState(false, false, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: makeA2dpDeviceUnavailableLater, reason: merged with bridge method [inline-methods] */
    public void lambda$disconnectA2dp$30(java.lang.String address, int delayMs) {
        if (android.os.Build.isMtkPlatform()) {
            this.mAudioSystem.setParameters("A2dpsuspendonly=true");
        } else if (!this.mAdiExt.isDhpResetting()) {
            this.mDeviceBroker.setA2dpSuspended(true, true, "makeA2dpDeviceUnavailableLater");
        }
        java.lang.String deviceKey = com.android.server.audio.AudioDeviceInventory.DeviceInfo.makeDeviceListKey(128, address);
        com.android.server.audio.AudioDeviceInventory.DeviceInfo deviceInfo = this.mConnectedDevices.get(deviceKey);
        int a2dpCodec = deviceInfo != null ? deviceInfo.mDeviceCodecFormat : 0;
        this.mConnectedDevices.remove(deviceKey);
        this.mDeviceBroker.setA2dpTimeout(address, a2dpCodec, delayMs);
    }

    private void makeA2dpSrcAvailable(java.lang.String address, int a2dpCodec) {
        int res = this.mAudioSystem.setDeviceConnectionState(new android.media.AudioDeviceAttributes(android.hardware.audio.common.V2_0.AudioDevice.IN_BLUETOOTH_A2DP, address), 1, a2dpCodec);
        if (res != 0) {
            com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("APM failed to make available A2DP source device addr=" + android.media.Utils.anonymizeBluetoothAddress(address) + " error=" + res).printSlog(1, TAG));
        } else {
            com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("A2DP source device addr=" + android.media.Utils.anonymizeBluetoothAddress(address) + " now available").printSlog(0, TAG));
        }
        this.mConnectedDevices.put(com.android.server.audio.AudioDeviceInventory.DeviceInfo.makeDeviceListKey(android.hardware.audio.common.V2_0.AudioDevice.IN_BLUETOOTH_A2DP, address), new com.android.server.audio.AudioDeviceInventory.DeviceInfo(android.hardware.audio.common.V2_0.AudioDevice.IN_BLUETOOTH_A2DP, "", address, "", a2dpCodec));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: makeA2dpSrcUnavailable, reason: merged with bridge method [inline-methods] */
    public void lambda$disconnectA2dpSink$32(java.lang.String address) {
        java.lang.String deviceKey = com.android.server.audio.AudioDeviceInventory.DeviceInfo.makeDeviceListKey(android.hardware.audio.common.V2_0.AudioDevice.IN_BLUETOOTH_A2DP, address);
        com.android.server.audio.AudioDeviceInventory.DeviceInfo deviceInfo = this.mConnectedDevices.get(deviceKey);
        int a2dpCodec = deviceInfo != null ? deviceInfo.mDeviceCodecFormat : 0;
        android.media.AudioDeviceAttributes ada = new android.media.AudioDeviceAttributes(android.hardware.audio.common.V2_0.AudioDevice.IN_BLUETOOTH_A2DP, address);
        this.mAudioSystem.setDeviceConnectionState(ada, 0, a2dpCodec);
        this.mConnectedDevices.remove(com.android.server.audio.AudioDeviceInventory.DeviceInfo.makeDeviceListKey(android.hardware.audio.common.V2_0.AudioDevice.IN_BLUETOOTH_A2DP, address));
        this.mDeviceBroker.postCheckCommunicationDeviceRemoval(ada);
    }

    private void makeHearingAidDeviceAvailable(java.lang.String address, java.lang.String name, int streamType, java.lang.String eventSource) {
        int hearingAidVolIndex = this.mDeviceBroker.getVssVolumeForDevice(streamType, 134217728);
        this.mDeviceBroker.postSetHearingAidVolumeIndex(hearingAidVolIndex, streamType);
        this.mDeviceBroker.setBluetoothA2dpOnInt(true, false, eventSource);
        long diff = android.os.SystemClock.uptimeMillis();
        android.media.AudioDeviceAttributes ada = new android.media.AudioDeviceAttributes(134217728, address, name);
        int res = this.mAudioSystem.setDeviceConnectionState(ada, 1, 0);
        if (res != 0) {
            com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("APM failed to make available hearing AID device addr=" + address + " error=" + res).printLog(TAG));
        } else {
            com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("Hearing Aid device addr=" + address + " now available" + (android.os.SystemClock.uptimeMillis() - diff) + "ms").printLog(TAG));
        }
        this.mConnectedDevices.put(com.android.server.audio.AudioDeviceInventory.DeviceInfo.makeDeviceListKey(134217728, address), new com.android.server.audio.AudioDeviceInventory.DeviceInfo(134217728, name, address));
        this.mDeviceBroker.postAccessoryPlugMediaUnmute(134217728);
        this.mDeviceBroker.postApplyVolumeOnDevice(streamType, 134217728, "makeHearingAidDeviceAvailable");
        setCurrentAudioRouteNameIfPossible(name, false);
        addAudioDeviceInInventoryIfNeeded(134217728, address, "", com.android.server.audio.BtHelper.getBtDeviceCategory(address), false);
        new android.media.MediaMetrics.Item("audio.device.makeHearingAidDeviceAvailable").set(android.media.MediaMetrics.Property.ADDRESS, address != null ? address : "").set(android.media.MediaMetrics.Property.DEVICE, android.media.AudioSystem.getDeviceName(134217728)).set(android.media.MediaMetrics.Property.NAME, name).set(android.media.MediaMetrics.Property.STREAM_TYPE, android.media.AudioSystem.streamToString(streamType)).record();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: makeHearingAidDeviceUnavailable, reason: merged with bridge method [inline-methods] */
    public void lambda$disconnectHearingAid$34(java.lang.String address) {
        android.media.AudioDeviceAttributes ada = new android.media.AudioDeviceAttributes(134217728, address);
        this.mAudioSystem.setDeviceConnectionState(ada, 0, 0);
        this.mConnectedDevices.remove(com.android.server.audio.AudioDeviceInventory.DeviceInfo.makeDeviceListKey(134217728, address));
        setCurrentAudioRouteNameIfPossible(null, false);
        new android.media.MediaMetrics.Item("audio.device.makeHearingAidDeviceUnavailable").set(android.media.MediaMetrics.Property.ADDRESS, address != null ? address : "").set(android.media.MediaMetrics.Property.DEVICE, android.media.AudioSystem.getDeviceName(134217728)).record();
        this.mDeviceBroker.postCheckCommunicationDeviceRemoval(ada);
    }

    boolean isHearingAidConnected() {
        return getFirstConnectedDeviceOfTypes(com.google.android.collect.Sets.newHashSet(new java.lang.Integer[]{134217728})) != null;
    }

    private com.android.server.audio.AudioDeviceInventory.DeviceInfo getFirstConnectedDeviceOfTypes(java.util.Set<java.lang.Integer> internalTypes) {
        java.util.List<com.android.server.audio.AudioDeviceInventory.DeviceInfo> devices = getConnectedDevicesOfTypes(internalTypes);
        if (devices.isEmpty()) {
            return null;
        }
        return devices.get(0);
    }

    private java.util.List<com.android.server.audio.AudioDeviceInventory.DeviceInfo> getConnectedDevicesOfTypes(java.util.Set<java.lang.Integer> internalTypes) {
        java.util.ArrayList<com.android.server.audio.AudioDeviceInventory.DeviceInfo> devices = new java.util.ArrayList<>();
        synchronized (this.mDevicesLock) {
            for (com.android.server.audio.AudioDeviceInventory.DeviceInfo di : this.mConnectedDevices.values()) {
                if (internalTypes.contains(java.lang.Integer.valueOf(di.mDeviceType))) {
                    devices.add(di);
                }
            }
        }
        return devices;
    }

    android.media.AudioDeviceAttributes getDeviceOfType(int type) {
        com.android.server.audio.AudioDeviceInventory.DeviceInfo di = getFirstConnectedDeviceOfTypes(com.google.android.collect.Sets.newHashSet(new java.lang.Integer[]{java.lang.Integer.valueOf(type)}));
        if (di == null) {
            return null;
        }
        return new android.media.AudioDeviceAttributes(di.mDeviceType, di.mDeviceAddress, di.mDeviceName);
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x007c  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00ab  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00f3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void makeLeAudioDeviceAvailable(com.android.server.audio.AudioDeviceBroker.BtDeviceInfo r25, int r26, int r27, java.lang.String r28) {
        /*
            Method dump skipped, instruction units count: 439
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.AudioDeviceInventory.makeLeAudioDeviceAvailable(com.android.server.audio.AudioDeviceBroker$BtDeviceInfo, int, int, java.lang.String):void");
    }

    private void makeLeAudioDeviceUnavailableNow(java.lang.String address, int device, int codec) {
        android.media.AudioDeviceAttributes ada = null;
        if (device != 0) {
            ada = new android.media.AudioDeviceAttributes(device, address);
            int res = android.media.AudioSystem.setDeviceConnectionState(ada, 0, codec);
            if (res != 0) {
                com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("APM failed to make unavailable LE Audio device addr=" + address + " error=" + res).printSlog(1, TAG));
            } else {
                com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("LE Audio device addr=" + android.media.Utils.anonymizeBluetoothAddress(address) + " made unavailable").printSlog(0, TAG));
            }
            this.mAudioSystem.setParameters(EXIT_GAME_MODE_PARAM);
            this.mConnectedDevices.remove(com.android.server.audio.AudioDeviceInventory.DeviceInfo.makeDeviceListKey(device, address));
        }
        setCurrentAudioRouteNameIfPossible(null, false);
        updateBluetoothPreferredModes_l(null);
        purgeDevicesRoles_l();
        if (ada != null) {
            this.mDeviceBroker.postCheckCommunicationDeviceRemoval(ada);
        }
        if (this.mAdiExt.isHoloVoipSupport() && isHoloBleOutDevice(device)) {
            this.mDeviceBroker.getWrapper().checkHoloDeviceSupportState(false, false, true);
        }
    }

    private void makeLeAudioDeviceUnavailableLater(java.lang.String address, int device, int codec, int delayMs) {
        this.mDeviceBroker.setLeAudioSuspended(true, true, "makeLeAudioDeviceUnavailableLater");
        this.mConnectedDevices.remove(com.android.server.audio.AudioDeviceInventory.DeviceInfo.makeDeviceListKey(device, address));
        this.mDeviceBroker.setLeAudioTimeout(address, device, codec, delayMs);
    }

    private void setCurrentAudioRouteNameIfPossible(java.lang.String name, boolean fromA2dp) {
        synchronized (this.mCurAudioRoutes) {
            if (android.text.TextUtils.equals(this.mCurAudioRoutes.bluetoothName, name)) {
                return;
            }
            if (name != null || !isCurrentDeviceConnected()) {
                this.mCurAudioRoutes.bluetoothName = name;
                this.mDeviceBroker.postReportNewRoutes(fromA2dp);
            }
        }
    }

    private boolean isCurrentDeviceConnected() {
        return this.mConnectedDevices.values().stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda23
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$isCurrentDeviceConnected$37((com.android.server.audio.AudioDeviceInventory.DeviceInfo) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$isCurrentDeviceConnected$37(com.android.server.audio.AudioDeviceInventory.DeviceInfo deviceInfo) {
        return android.text.TextUtils.equals(deviceInfo.mDeviceName, this.mCurAudioRoutes.bluetoothName);
    }

    private int checkSendBecomingNoisyIntentInt(int device, int state, int musicDevice) {
        android.media.MediaMetrics.Item mmi = new android.media.MediaMetrics.Item("audio.device.checkSendBecomingNoisyIntentInt").set(android.media.MediaMetrics.Property.DEVICE, android.media.AudioSystem.getDeviceName(device)).set(android.media.MediaMetrics.Property.STATE, state == 1 ? "connected" : "disconnected");
        if (state != 0) {
            android.util.Log.i(TAG, "not sending NOISY: state=" + state);
            mmi.set(android.media.MediaMetrics.Property.DELAY_MS, 0).record();
            return 0;
        }
        if (!BECOMING_NOISY_INTENT_DEVICES_SET.contains(java.lang.Integer.valueOf(device))) {
            android.util.Log.i(TAG, "not sending NOISY: device=0x" + java.lang.Integer.toHexString(device) + " not in set " + BECOMING_NOISY_INTENT_DEVICES_SET);
            mmi.set(android.media.MediaMetrics.Property.DELAY_MS, 0).record();
            return 0;
        }
        int delay = 0;
        java.util.Set<java.lang.Integer> devices = new java.util.HashSet<>();
        for (com.android.server.audio.AudioDeviceInventory.DeviceInfo di : this.mConnectedDevices.values()) {
            if (!android.media.AudioSystem.isInputDevice(di.mDeviceType) && BECOMING_NOISY_INTENT_DEVICES_SET.contains(java.lang.Integer.valueOf(di.mDeviceType))) {
                devices.add(java.lang.Integer.valueOf(di.mDeviceType));
                android.util.Log.i(TAG, "NOISY: adding 0x" + java.lang.Integer.toHexString(di.mDeviceType));
            }
        }
        if (musicDevice == 0) {
            musicDevice = this.mDeviceBroker.getDeviceForStream(3);
            android.util.Log.i(TAG, "NOISY: musicDevice changing from NONE to 0x" + java.lang.Integer.toHexString(musicDevice));
        }
        this.mAdiExt.setAudioDeviceDisconnect(device);
        boolean inCommunication = this.mDeviceBroker.isInCommunication();
        boolean singleAudioDeviceType = android.media.AudioSystem.isSingleAudioDeviceType(devices, device);
        boolean hasMediaDynamicPolicy = this.mDeviceBroker.hasMediaDynamicPolicy();
        if ((device == musicDevice || inCommunication || ((android.media.AudioSystem.DEVICE_OUT_ALL_A2DP_SET.contains(java.lang.Integer.valueOf(device)) && android.media.AudioSystem.DEVICE_OUT_ALL_SCO_SET.contains(java.lang.Integer.valueOf(musicDevice))) || device == 536870912)) && singleAudioDeviceType && ((!hasMediaDynamicPolicy || (device == 8388608 && hasMediaDynamicPolicy)) && musicDevice != 32768)) {
            if (!this.mAudioSystem.isStreamActive(3, 0) && !this.mDeviceBroker.hasAudioFocusUsers()) {
                com.android.server.audio.AudioService.sDeviceLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("dropping ACTION_AUDIO_BECOMING_NOISY").printLog(TAG));
                mmi.set(android.media.MediaMetrics.Property.DELAY_MS, 0).record();
                return 0;
            }
            this.mDeviceBroker.postBroadcastBecomingNoisy();
            delay = android.os.SystemProperties.getInt("audio.sys.noisy.broadcast.delay", com.android.server.am.ProcessList.PREVIOUS_APP_ADJ);
        } else {
            android.util.Log.i(TAG, "not sending NOISY: device:0x" + java.lang.Integer.toHexString(device) + " musicDevice:0x" + java.lang.Integer.toHexString(musicDevice) + " inComm:" + inCommunication + " mediaPolicy:" + hasMediaDynamicPolicy + " singleDevice:" + singleAudioDeviceType);
        }
        mmi.set(android.media.MediaMetrics.Property.DELAY_MS, java.lang.Integer.valueOf(delay)).record();
        return delay;
    }

    private void sendDeviceConnectionIntent(int device, int state, java.lang.String address, java.lang.String deviceName) {
        java.lang.String mDeviceInString;
        if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
            android.util.Slog.i(TAG, "sendDeviceConnectionIntent(dev:0x" + java.lang.Integer.toHexString(device) + " state:0x" + java.lang.Integer.toHexString(state) + " address:" + address + " name:" + deviceName + ");");
            if ((device & Integer.MIN_VALUE) == Integer.MIN_VALUE) {
                mDeviceInString = android.media.AudioSystem.getInputDeviceName(device);
            } else {
                mDeviceInString = android.media.AudioSystem.getOutputDeviceName(device);
            }
            android.util.Log.i(TAG, "sendDeviceConnectionIntent(dev:0x" + java.lang.Integer.toHexString(device) + " state:0x" + java.lang.Integer.toHexString(state) + "[" + mDeviceInString + "] address:" + address + " name:" + deviceName + ");");
        }
        android.content.Intent intent = new android.content.Intent();
        int i = 0;
        switch (device) {
            case android.hardware.audio.common.V2_0.AudioDevice.IN_USB_HEADSET /* -2113929216 */:
                if (android.media.AudioSystem.getDeviceConnectionState(67108864, "") == 1) {
                    intent.setAction("android.intent.action.HEADSET_PLUG");
                    intent.putExtra("microphone", 1);
                } else {
                    return;
                }
                break;
            case 4:
                intent.setAction("android.intent.action.HEADSET_PLUG");
                intent.putExtra("microphone", 1);
                break;
            case 8:
            case 131072:
                intent.setAction("android.intent.action.HEADSET_PLUG");
                intent.putExtra("microphone", 0);
                break;
            case 1024:
            case 262144:
            case 262145:
                configureHdmiPlugIntent(intent, state);
                break;
            case 67108864:
                intent.setAction("android.intent.action.HEADSET_PLUG");
                if (android.media.AudioSystem.getDeviceConnectionState(android.hardware.audio.common.V2_0.AudioDevice.IN_USB_HEADSET, "") == 1) {
                    i = 1;
                }
                intent.putExtra("microphone", i);
                break;
        }
        if (intent.getAction() == null) {
            if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                android.util.Log.e(TAG, "Headset Plugged-out broadcast is not send.Action is null");
                return;
            }
            return;
        }
        if (state == 0 && intent.getAction() == "android.intent.action.HEADSET_PLUG" && (android.media.AudioSystem.getDeviceConnectionState(67108864, "") == 1 || android.media.AudioSystem.getDeviceConnectionState(8, "") == 1 || android.media.AudioSystem.getDeviceConnectionState(4, "") == 1)) {
            if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                android.util.Log.e(TAG, "Headset Plugged-out broadcast is not send.Still headset is Plugged-in");
                return;
            }
            return;
        }
        intent.putExtra("state", state);
        intent.putExtra(CONNECT_INTENT_KEY_ADDRESS, address);
        intent.putExtra(CONNECT_INTENT_KEY_PORT_NAME, deviceName);
        intent.addFlags(1073741824);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mDeviceBroker.broadcastStickyIntentToCurrentProfileGroup(intent);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private void updateAudioRoutes(int device, int state) {
        int newConn;
        int connType = 0;
        switch (device) {
            case 4:
                connType = 1;
                break;
            case 8:
            case 131072:
                connType = 2;
                break;
            case 1024:
            case 262144:
            case 262145:
                connType = 8;
                break;
            case 16384:
            case 67108864:
                connType = 16;
                break;
        }
        synchronized (this.mCurAudioRoutes) {
            if (connType == 0) {
                return;
            }
            int newConn2 = this.mCurAudioRoutes.mainType;
            if (state != 0) {
                newConn = newConn2 | connType;
            } else {
                newConn = newConn2 & (~connType);
            }
            if (newConn != this.mCurAudioRoutes.mainType) {
                this.mCurAudioRoutes.mainType = newConn;
                this.mDeviceBroker.postReportNewRoutes(false);
            }
        }
    }

    private void configureHdmiPlugIntent(android.content.Intent intent, int state) {
        intent.setAction("android.media.action.HDMI_AUDIO_PLUG");
        intent.putExtra("android.media.extra.AUDIO_PLUG_STATE", state);
        if (state != 1) {
            return;
        }
        java.util.ArrayList<android.media.AudioPort> ports = new java.util.ArrayList<>();
        int[] portGeneration = new int[1];
        int status = android.media.AudioSystem.listAudioPorts(ports, portGeneration);
        if (status != 0) {
            android.util.Log.e(TAG, "listAudioPorts error " + status + " in configureHdmiPlugIntent");
            return;
        }
        for (android.media.AudioPort audioPort : ports) {
            if (audioPort instanceof android.media.AudioDevicePort) {
                android.media.AudioDevicePort devicePort = (android.media.AudioDevicePort) audioPort;
                if (devicePort.type() == 1024 || devicePort.type() == 262144 || devicePort.type() == 262145) {
                    int[] formats = android.media.AudioFormat.filterPublicFormats(devicePort.formats());
                    if (formats.length > 0) {
                        java.util.ArrayList<java.lang.Integer> encodingList = new java.util.ArrayList<>(1);
                        for (int format : formats) {
                            if (format != 0) {
                                encodingList.add(java.lang.Integer.valueOf(format));
                            }
                        }
                        int[] encodingArray = encodingList.stream().mapToInt(new java.util.function.ToIntFunction() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda28
                            @Override // java.util.function.ToIntFunction
                            public final int applyAsInt(java.lang.Object obj) {
                                return ((java.lang.Integer) obj).intValue();
                            }
                        }).toArray();
                        intent.putExtra("android.media.extra.ENCODINGS", encodingArray);
                    }
                    int maxChannels = 0;
                    for (int mask : devicePort.channelMasks()) {
                        int channelCount = android.media.AudioFormat.channelCountFromOutChannelMask(mask);
                        if (channelCount > maxChannels) {
                            maxChannels = channelCount;
                        }
                    }
                    intent.putExtra("android.media.extra.MAX_CHANNEL_COUNT", maxChannels);
                }
            }
        }
    }

    private void dispatchPreferredDevice(int strategy, java.util.List<android.media.AudioDeviceAttributes> devices) {
        int nbDispatchers = this.mPrefDevDispatchers.beginBroadcast();
        for (int i = 0; i < nbDispatchers; i++) {
            try {
                if (!((java.lang.Boolean) this.mPrefDevDispatchers.getBroadcastCookie(i)).booleanValue()) {
                    devices = this.mDeviceBroker.anonymizeAudioDeviceAttributesListUnchecked(devices);
                }
                this.mPrefDevDispatchers.getBroadcastItem(i).dispatchPrefDevicesChanged(strategy, devices);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "dispatchPreferredDevice ", e);
            }
        }
        this.mPrefDevDispatchers.finishBroadcast();
    }

    private void dispatchNonDefaultDevice(int strategy, java.util.List<android.media.AudioDeviceAttributes> devices) {
        int nbDispatchers = this.mNonDefDevDispatchers.beginBroadcast();
        for (int i = 0; i < nbDispatchers; i++) {
            try {
                if (!((java.lang.Boolean) this.mNonDefDevDispatchers.getBroadcastCookie(i)).booleanValue()) {
                    devices = this.mDeviceBroker.anonymizeAudioDeviceAttributesListUnchecked(devices);
                }
                this.mNonDefDevDispatchers.getBroadcastItem(i).dispatchNonDefDevicesChanged(strategy, devices);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "dispatchNonDefaultDevice ", e);
            }
        }
        this.mNonDefDevDispatchers.finishBroadcast();
    }

    private void dispatchDevicesRoleForCapturePreset(int capturePreset, int role, java.util.List<android.media.AudioDeviceAttributes> devices) {
        int nbDispatchers = this.mDevRoleCapturePresetDispatchers.beginBroadcast();
        for (int i = 0; i < nbDispatchers; i++) {
            try {
                if (!((java.lang.Boolean) this.mDevRoleCapturePresetDispatchers.getBroadcastCookie(i)).booleanValue()) {
                    devices = this.mDeviceBroker.anonymizeAudioDeviceAttributesListUnchecked(devices);
                }
                this.mDevRoleCapturePresetDispatchers.getBroadcastItem(i).dispatchDevicesRoleChanged(capturePreset, role, devices);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "dispatchDevicesRoleForCapturePreset ", e);
            }
        }
        this.mDevRoleCapturePresetDispatchers.finishBroadcast();
    }

    java.util.List<java.lang.String> getDeviceIdentityAddresses(android.media.AudioDeviceAttributes device) {
        java.util.List<java.lang.String> addresses = new java.util.ArrayList<>();
        java.lang.String key = com.android.server.audio.AudioDeviceInventory.DeviceInfo.makeDeviceListKey(device.getInternalType(), device.getAddress());
        if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
            android.util.Log.d(TAG, "getDeviceSensorUuid, key = " + key);
        }
        synchronized (this.mDevicesLock) {
            com.android.server.audio.AudioDeviceInventory.DeviceInfo di = this.mConnectedDevices.get(key);
            if (di != null) {
                if (!di.mDeviceIdentityAddress.isEmpty()) {
                    addresses.add(di.mDeviceIdentityAddress);
                }
                if (!di.mPeerIdentityDeviceAddress.isEmpty() && !di.mPeerIdentityDeviceAddress.equals(di.mDeviceIdentityAddress)) {
                    addresses.add(di.mPeerIdentityDeviceAddress);
                }
            }
        }
        return addresses;
    }

    java.lang.String getDeviceSettings() {
        java.lang.String string;
        synchronized (this.mDeviceInventoryLock) {
            int deviceCatalogSize = this.mDeviceInventory.size();
            java.lang.StringBuilder settingsBuilder = new java.lang.StringBuilder(com.android.server.audio.AdiDeviceState.getPeristedMaxSize() * deviceCatalogSize);
            java.util.Iterator<com.android.server.audio.AdiDeviceState> iterator = this.mDeviceInventory.values().iterator();
            if (iterator.hasNext()) {
                settingsBuilder.append(iterator.next().toPersistableString());
            }
            while (iterator.hasNext()) {
                settingsBuilder.append(SETTING_DEVICE_SEPARATOR_CHAR);
                settingsBuilder.append(iterator.next().toPersistableString());
            }
            string = settingsBuilder.toString();
        }
        return string;
    }

    void setDeviceSettings(java.lang.String settings) {
        clearDeviceInventory();
        java.lang.String[] devSettings = android.text.TextUtils.split((java.lang.String) java.util.Objects.requireNonNull(settings), SETTING_DEVICE_SEPARATOR);
        for (java.lang.String setting : devSettings) {
            com.android.server.audio.AdiDeviceState devState = com.android.server.audio.AdiDeviceState.fromPersistedString(setting);
            if (devState != null) {
                if (!devState.isSAEnabled() && (android.media.AudioSystem.isBluetoothA2dpOutDevice(devState.getInternalDeviceType()) || android.media.AudioSystem.isBluetoothLeOutDevice(devState.getInternalDeviceType()))) {
                    android.util.Log.d(TAG, "setDeviceSettings: reInitSAState.");
                    this.mDeviceBroker.reInitSAState(devState);
                }
                addOrUpdateDeviceSAStateInInventory(devState, false);
                addOrUpdateAudioDeviceCategoryInInventory(devState, false);
            }
        }
    }

    public boolean isA2dpDeviceConnected(android.bluetooth.BluetoothDevice device) {
        for (com.android.server.audio.AudioDeviceInventory.DeviceInfo di : getConnectedDevicesOfTypes(com.google.android.collect.Sets.newHashSet(new java.lang.Integer[]{128}))) {
            if (di.mDeviceAddress.equals(device.getAddress())) {
                return true;
            }
        }
        return false;
    }

    public boolean isLeConnected() {
        for (com.android.server.audio.AudioDeviceInventory.DeviceInfo device : this.mConnectedDevices.values()) {
            if (device.mDeviceType == 536870912) {
                return true;
            }
        }
        if (android.os.Build.isMtkPlatform() && this.mDeviceBroker != null && this.mDeviceBroker.getWrapper() != null) {
            return this.mDeviceBroker.getWrapper().isBluetoothLeTbsDeviceActive();
        }
        return false;
    }

    public boolean isHoloBtDeviceConnected() {
        for (com.android.server.audio.AudioDeviceInventory.DeviceInfo device : this.mConnectedDevices.values()) {
            if (device.mDeviceType == 128 || device.mDeviceType == 256 || device.mDeviceType == 536870912 || device.mDeviceType == 536870914) {
                return true;
            }
        }
        return false;
    }

    public boolean isHoloVoipSupport() {
        return this.mAdiExt.isHoloVoipSupport();
    }

    public boolean isHoloBLeDeviceConnected() {
        for (com.android.server.audio.AudioDeviceInventory.DeviceInfo device : this.mConnectedDevices.values()) {
            if (device.mDeviceType == 536870912 || device.mDeviceType == 536870914) {
                return true;
            }
        }
        return false;
    }

    public boolean isHoloBleOutDevice(int device) {
        if (device == 536870912 || device == 536870914) {
            return true;
        }
        return false;
    }

    private void disconnectSco() {
        com.android.server.audio.AudioDeviceInventory.DeviceInfo scodi = null;
        java.lang.String deviceKey = null;
        int connectedScoCount = 0;
        android.util.Log.d(TAG, "ConnectedDevices size : " + this.mConnectedDevices.size());
        for (com.android.server.audio.AudioDeviceInventory.DeviceInfo di : this.mConnectedDevices.values()) {
            android.util.Log.d(TAG, "mDeviceType :" + di.mDeviceType + " connectedScoCount:" + connectedScoCount);
            if (di.mDeviceType == 16 || di.mDeviceType == 32) {
                connectedScoCount++;
                scodi = di;
                deviceKey = com.android.server.audio.AudioDeviceInventory.DeviceInfo.makeDeviceListKey(scodi.mDeviceType, scodi.mDeviceAddress);
            }
        }
        if (connectedScoCount == 1 && scodi != null && deviceKey != null) {
            android.media.AudioDeviceAttributes attr = new android.media.AudioDeviceAttributes(scodi.mDeviceType, scodi.mDeviceAddress, scodi.mDeviceName);
            android.media.AudioSystem.setDeviceConnectionState(attr, 0, 0);
            this.mConnectedDevices.remove(deviceKey);
        }
    }

    public int getCurrentConnectedScoDevices() {
        for (com.android.server.audio.AudioDeviceInventory.DeviceInfo di : this.mConnectedDevices.values()) {
            if (di.mDeviceType == 32) {
                return 32;
            }
            if (di.mDeviceType == 16) {
                return 16;
            }
        }
        return 32;
    }

    public com.android.server.audio.IAudioDeviceInventoryWrapper getWrapper() {
        return this.mAdiWrapper;
    }

    private class AudioDeviceInventoryWrapper implements com.android.server.audio.IAudioDeviceInventoryWrapper {
        private AudioDeviceInventoryWrapper() {
        }

        @Override // com.android.server.audio.IAudioDeviceInventoryWrapper
        public com.android.server.audio.IAudioDeviceInventoryExt getExtImpl() {
            return com.android.server.audio.AudioDeviceInventory.this.mAdiExt;
        }

        @Override // com.android.server.audio.IAudioDeviceInventoryWrapper
        public java.lang.String getConnectedDevices() {
            java.lang.StringBuilder deviceInfo = new java.lang.StringBuilder();
            if (!com.android.server.audio.AudioDeviceInventory.this.mConnectedDevices.isEmpty()) {
                for (com.android.server.audio.AudioDeviceInventory.DeviceInfo di : com.android.server.audio.AudioDeviceInventory.this.mConnectedDevices.values()) {
                    if ((di.mDeviceType & Integer.MIN_VALUE) != Integer.MIN_VALUE) {
                        if (deviceInfo.length() == 0) {
                            deviceInfo.append("0x" + java.lang.Integer.toHexString(di.mDeviceType));
                        } else {
                            deviceInfo.append("|0x" + java.lang.Integer.toHexString(di.mDeviceType));
                        }
                    }
                }
                return deviceInfo.toString();
            }
            return deviceInfo.append("null").toString();
        }

        @Override // com.android.server.audio.IAudioDeviceInventoryWrapper
        public boolean isBluetoothScoDeviceConnected() {
            for (com.android.server.audio.AudioDeviceInventory.DeviceInfo di : com.android.server.audio.AudioDeviceInventory.this.mConnectedDevices.values()) {
                if (di.mDeviceType == 16 || di.mDeviceType == 32) {
                    return true;
                }
            }
            return false;
        }
    }
}
