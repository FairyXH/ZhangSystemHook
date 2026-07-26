package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class SpatializerHelper {
    private static final boolean DEBUG = true;
    private static final boolean DEBUG_MORE = false;
    private static final java.lang.String METRICS_DEVICE_PREFIX = "audio.spatializer.device.";
    private static final java.lang.String OPLUS_SPATIALIZER_USERSPACE_STATE = "OPLUS_SPATIALIZER_USERSPACE_STATE";
    static final int STATE_DISABLED_AVAILABLE = 6;
    static final int STATE_DISABLED_UNAVAILABLE = 3;
    static final int STATE_ENABLED_AVAILABLE = 5;
    static final int STATE_ENABLED_UNAVAILABLE = 4;
    static final int STATE_NOT_SUPPORTED = 1;
    static final int STATE_UNINITIALIZED = 0;
    private static final java.lang.String TAG = "AS.SpatializerHelper";
    private static final int USER_STATE_DEFAULT = -1;
    private static final int USER_STATE_DISENABLED = 0;
    private static final int USER_STATE_ENABLED = 1;
    private final com.android.server.audio.AudioSystemAdapter mASA;
    private final com.android.server.audio.AudioService mAudioService;
    boolean mBinauralEnabledDefault;
    private final com.android.server.audio.AudioDeviceBroker mDeviceBroker;
    private com.android.server.audio.SpatializerHelper.HelperDynamicSensorCallback mDynSensorCallback;
    boolean mHeadTrackingEnabledDefault;
    private android.hardware.SensorManager mSensorManager;
    private android.media.ISpatializer mSpat;
    private com.android.server.audio.SpatializerHelper.SpatializerCallback mSpatCallback;
    boolean mTransauralEnabledDefault;
    static final android.util.SparseIntArray SPAT_MODE_FOR_DEVICE_TYPE = new android.util.SparseIntArray(14) { // from class: com.android.server.audio.SpatializerHelper.1
        {
            append(2, 1);
            append(24, 1);
            append(3, 0);
            append(4, 0);
            append(8, 0);
            append(13, 1);
            append(12, 1);
            append(11, 1);
            append(22, 0);
            append(5, 1);
            append(6, 1);
            append(19, 1);
            append(26, 0);
            append(27, 1);
            append(30, 0);
        }
    };
    private static boolean mSpatialzerForSpeakerSupport = false;
    private static boolean mAudioEffectCombinedSupport = false;
    private static final android.media.AudioAttributes DEFAULT_ATTRIBUTES = new android.media.AudioAttributes.Builder().setUsage(1).build();
    private static final android.media.AudioFormat DEFAULT_FORMAT = new android.media.AudioFormat.Builder().setEncoding(2).setSampleRate(48000).setChannelMask(android.hardware.audio.common.V2_0.AudioChannelMask.IN_6).build();
    private static java.util.ArrayList<android.media.AudioDeviceAttributes> sRoutingDevices = new java.util.ArrayList<>(0);
    private int mState = 0;
    private boolean mFeatureEnabled = false;
    private int mUserStateOfSpeaker = 0;
    private android.media.AudioDeviceAttributes newWirelessDevice = null;
    private boolean mHeadphoneSpatializerState = false;
    private int mSpatLevel = 0;
    private int mCapableSpatLevel = 0;
    private boolean mTransauralSupported = false;
    private boolean mBinauralSupported = false;
    private boolean mIsHeadTrackingSupported = false;
    private int[] mSupportedHeadTrackingModes = new int[0];
    private int mActualHeadTrackingMode = -2;
    private int mDesiredHeadTrackingMode = 1;
    private boolean mHeadTrackerAvailable = false;
    private int mDesiredHeadTrackingModeWhenEnabled = 1;
    private int mSpatOutput = 0;
    private com.android.server.audio.SpatializerHelper.SpatializerHeadTrackingCallback mSpatHeadTrackingCallback = new com.android.server.audio.SpatializerHelper.SpatializerHeadTrackingCallback();
    private boolean mSpatialAudioState = false;
    private java.lang.String mDeviceAddress = "";
    private final java.util.ArrayList<java.lang.Integer> mSACapableDeviceTypes = new java.util.ArrayList<>(0);
    final android.os.RemoteCallbackList<android.media.ISpatializerCallback> mStateCallbacks = new android.os.RemoteCallbackList<>();
    final android.os.RemoteCallbackList<android.media.ISpatializerHeadTrackingModeCallback> mHeadTrackingModeCallbacks = new android.os.RemoteCallbackList<>();
    final android.os.RemoteCallbackList<android.media.ISpatializerHeadTrackerAvailableCallback> mHeadTrackerCallbacks = new android.os.RemoteCallbackList<>();
    final android.os.RemoteCallbackList<android.media.ISpatializerHeadToSoundStagePoseCallback> mHeadPoseCallbacks = new android.os.RemoteCallbackList<>();
    final android.os.RemoteCallbackList<android.media.ISpatializerOutputCallback> mOutputCallbacks = new android.os.RemoteCallbackList<>();

    private static void logd(java.lang.String s) {
        android.util.Log.i(TAG, s);
    }

    SpatializerHelper(com.android.server.audio.AudioService mother, com.android.server.audio.AudioSystemAdapter asa, com.android.server.audio.AudioDeviceBroker deviceBroker, boolean binauralEnabledDefault, boolean transauralEnabledDefault, boolean headTrackingEnabledDefault) {
        this.mAudioService = mother;
        this.mASA = asa;
        this.mDeviceBroker = deviceBroker;
        this.mBinauralEnabledDefault = binauralEnabledDefault;
        this.mTransauralEnabledDefault = transauralEnabledDefault;
        this.mHeadTrackingEnabledDefault = headTrackingEnabledDefault;
    }

    /* JADX WARN: Removed duplicated region for block: B:117:0x0257 A[Catch: all -> 0x0292, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x001b, B:8:0x0025, B:10:0x0029, B:12:0x0039, B:15:0x0043, B:17:0x008f, B:19:0x0093, B:20:0x00c5, B:68:0x01be, B:95:0x022e, B:115:0x0253, B:117:0x0257, B:121:0x025c, B:101:0x023e, B:112:0x024e, B:126:0x026b, B:130:0x0271, B:131:0x0272, B:132:0x0291), top: B:136:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:120:0x025b  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x026b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    synchronized void init(boolean r15) {
        /*
            Method dump skipped, instruction units count: 682
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.SpatializerHelper.init(boolean):void");
    }

    synchronized void reset(boolean featureEnabled) {
        loglogi("Resetting featureEnabled=" + featureEnabled);
        releaseSpat();
        this.mState = 0;
        this.mSpatLevel = 0;
        this.mActualHeadTrackingMode = -2;
        init(true);
        if (mAudioEffectCombinedSupport) {
            if (isSpeakerDevice()) {
                setSpatializerEnabledInt(featureEnabled);
            } else {
                setSpatializerEnabledInt(this.mHeadphoneSpatializerState);
            }
        } else {
            setSpatializerEnabledInt(featureEnabled, false);
        }
    }

    private void resetCapabilities() {
        this.mCapableSpatLevel = 0;
        this.mBinauralSupported = false;
        this.mTransauralSupported = false;
        this.mIsHeadTrackingSupported = false;
        this.mSupportedHeadTrackingModes = new int[0];
    }

    synchronized void onRoutingUpdated() {
        boolean enabled;
        switch (this.mState) {
            case 0:
            case 1:
                return;
            default:
                sRoutingDevices = getRoutingDevices(DEFAULT_ATTRIBUTES);
                if (sRoutingDevices.isEmpty()) {
                    logloge("onRoutingUpdated: no device, no Spatial Audio");
                    setDispatchAvailableState(false);
                    return;
                }
                android.media.AudioDeviceAttributes currentDevice = sRoutingDevices.get(0);
                if (android.media.AudioSystem.isBluetoothDevice(currentDevice.getInternalType())) {
                    addWirelessDeviceIfNew(currentDevice);
                }
                android.util.Pair<java.lang.Boolean, java.lang.Boolean> enabledAvailable = evaluateState(currentDevice);
                boolean able = false;
                if (((java.lang.Boolean) enabledAvailable.second).booleanValue()) {
                    able = canBeSpatializedOnDevice(DEFAULT_ATTRIBUTES, DEFAULT_FORMAT, sRoutingDevices);
                    loglogi("onRoutingUpdated: can spatialize media 5.1:" + able + " on device:" + currentDevice);
                    setDispatchAvailableState(able);
                } else {
                    loglogi("onRoutingUpdated: device:" + currentDevice + " not available for Spatial Audio");
                    setDispatchAvailableState(false);
                }
                com.android.server.audio.AdiDeviceState deviceState = this.mDeviceBroker.findDeviceStateForAudioDeviceAttributes(currentDevice, getCanonicalDeviceType(currentDevice.getType(), currentDevice.getInternalType()));
                if (!mAudioEffectCombinedSupport) {
                    enabled = this.mFeatureEnabled && able && ((java.lang.Boolean) enabledAvailable.first).booleanValue() && deviceState != null && deviceState.isUserEnabled();
                } else {
                    enabled = (this.mFeatureEnabled && able && ((java.lang.Boolean) enabledAvailable.first).booleanValue() && this.mHeadphoneSpatializerState) || isAndSupportSpeakerDevice();
                    loglogi("mFeatureEnabled = " + this.mFeatureEnabled + ", able = " + able + ", enabledAvailable.first = " + enabledAvailable.first + ", mHeadphoneSpatializerState = " + this.mHeadphoneSpatializerState + ", isAndSupportSpeakerDevice() = " + isAndSupportSpeakerDevice());
                }
                if (enabled) {
                    loglogi("Enabling Spatial Audio since enabled for media device:" + currentDevice);
                } else {
                    loglogi("Disabling Spatial Audio since disabled for media device:" + currentDevice);
                }
                if (this.mSpat == null && this.mDesiredHeadTrackingMode != -2) {
                    loglogi("onRoutingUpdated() recreate Spat, mode " + this.mDesiredHeadTrackingMode);
                    createSpat();
                }
                if (this.mSpat != null) {
                    byte level = enabled ? (byte) 1 : (byte) 0;
                    loglogi("Setting spatialization level to: " + ((int) level));
                    if (currentDevice.getType() != 7 && currentDevice.getType() != 1) {
                        int spaEnable = enabled ? 1 : 0;
                        java.lang.String keyValuePairsSpatializerState = "OPLUS_SPATIALIZER_USERSPACE_STATE=" + spaEnable;
                        android.media.AudioSystem.setParameters(keyValuePairsSpatializerState);
                    }
                    try {
                        this.mSpat.setLevel(level);
                        if (enabled && isHeadTrackerEnabled(currentDevice)) {
                            loglogi("setSpatialAudioState: true");
                            this.mAudioService.getWrapper().getExtImpl().setSpatialAudioState(currentDevice.getAddress(), true);
                        }
                    } catch (android.os.RemoteException e) {
                        android.util.Log.e(TAG, "onRoutingUpdated() Can't set spatializer level", e);
                        postReset();
                        return;
                    }
                    break;
                }
                setDispatchFeatureEnabledState(enabled, "onRoutingUpdated");
                if (deviceState != null && deviceState.isHeadTrackerEnabled() && this.mDesiredHeadTrackingMode != 1) {
                    this.mDesiredHeadTrackingMode = 1;
                    loglogi("Device headtrack is enabled, change mode to :" + this.mDesiredHeadTrackingMode);
                }
                if (this.mDesiredHeadTrackingMode == 1 && (!mAudioEffectCombinedSupport || this.mHeadphoneSpatializerState)) {
                    this.mFeatureEnabled = true;
                    loglogi("mode:" + this.mDesiredHeadTrackingMode + ", need set feature enabled.");
                }
                if (this.mDesiredHeadTrackingMode != -2 && this.mDesiredHeadTrackingMode != -1) {
                    postInitSensors();
                }
                return;
        }
    }

    private void postReset() {
        this.mAudioService.postResetSpatializer();
    }

    private final class SpatializerCallback extends android.media.INativeSpatializerCallback.Stub {
        private SpatializerCallback() {
        }

        public void onLevelChanged(byte level) {
            com.android.server.audio.SpatializerHelper.loglogi("SpatializerCallback.onLevelChanged level:" + ((int) level));
            synchronized (com.android.server.audio.SpatializerHelper.this) {
                com.android.server.audio.SpatializerHelper.this.mSpatLevel = com.android.server.audio.SpatializerHelper.spatializationLevelToSpatializerInt(level);
            }
            com.android.server.audio.SpatializerHelper.this.postInitSensors();
        }

        public void onOutputChanged(int output) {
            int oldOutput;
            com.android.server.audio.SpatializerHelper.loglogi("SpatializerCallback.onOutputChanged output:" + output);
            synchronized (com.android.server.audio.SpatializerHelper.this) {
                oldOutput = com.android.server.audio.SpatializerHelper.this.mSpatOutput;
                com.android.server.audio.SpatializerHelper.this.mSpatOutput = output;
            }
            if (oldOutput != output) {
                com.android.server.audio.SpatializerHelper.this.dispatchOutputUpdate(output);
            }
        }
    }

    private final class SpatializerHeadTrackingCallback extends android.media.ISpatializerHeadTrackingCallback.Stub {
        private SpatializerHeadTrackingCallback() {
        }

        public void onHeadTrackingModeChanged(byte mode) {
            int oldMode;
            int newMode;
            synchronized (this) {
                oldMode = com.android.server.audio.SpatializerHelper.this.mActualHeadTrackingMode;
                com.android.server.audio.SpatializerHelper.this.mActualHeadTrackingMode = com.android.server.audio.SpatializerHelper.headTrackingModeTypeToSpatializerInt(mode);
                newMode = com.android.server.audio.SpatializerHelper.this.mActualHeadTrackingMode;
            }
            com.android.server.audio.SpatializerHelper.loglogi("SpatializerHeadTrackingCallback.onHeadTrackingModeChanged mode:" + android.media.Spatializer.headtrackingModeToString(newMode));
            if (oldMode != newMode) {
                com.android.server.audio.SpatializerHelper.this.dispatchActualHeadTrackingMode(newMode);
            }
        }

        public void onHeadToSoundStagePoseUpdated(float[] headToStage) {
            if (headToStage == null) {
                android.util.Log.e(com.android.server.audio.SpatializerHelper.TAG, "SpatializerHeadTrackingCallback.onHeadToStagePoseUpdatednull transform");
            } else if (headToStage.length != 6) {
                android.util.Log.e(com.android.server.audio.SpatializerHelper.TAG, "SpatializerHeadTrackingCallback.onHeadToStagePoseUpdated invalid transform length" + headToStage.length);
            } else {
                com.android.server.audio.SpatializerHelper.this.dispatchPoseUpdate(headToStage);
            }
        }
    }

    private final class HelperDynamicSensorCallback extends android.hardware.SensorManager.DynamicSensorCallback {
        private HelperDynamicSensorCallback() {
        }

        @Override // android.hardware.SensorManager.DynamicSensorCallback
        public void onDynamicSensorConnected(android.hardware.Sensor sensor) {
            synchronized (com.android.server.audio.SpatializerHelper.this) {
                boolean init = com.android.server.audio.SpatializerHelper.this.mFeatureEnabled && com.android.server.audio.SpatializerHelper.this.mSpatLevel != 0;
                if (!init && com.android.server.audio.SpatializerHelper.this.newWirelessDevice == null && !com.android.server.audio.SpatializerHelper.sRoutingDevices.isEmpty()) {
                    com.android.server.audio.SpatializerHelper.this.newWirelessDevice = (android.media.AudioDeviceAttributes) com.android.server.audio.SpatializerHelper.sRoutingDevices.get(0);
                    android.util.Log.d(com.android.server.audio.SpatializerHelper.TAG, "onDynamicSensorConnected, spatializer is not enable, but we check the ht support of device first!");
                }
            }
            com.android.server.audio.SpatializerHelper.this.postInitSensors();
        }

        @Override // android.hardware.SensorManager.DynamicSensorCallback
        public void onDynamicSensorDisconnected(android.hardware.Sensor sensor) {
            com.android.server.audio.SpatializerHelper.this.postInitSensors();
        }
    }

    synchronized java.util.List<android.media.AudioDeviceAttributes> getCompatibleAudioDevices() {
        java.util.ArrayList<android.media.AudioDeviceAttributes> compatList;
        compatList = new java.util.ArrayList<>();
        for (com.android.server.audio.AdiDeviceState deviceState : this.mDeviceBroker.getImmutableDeviceInventory()) {
            if (deviceState.isSAEnabled() && isSADevice(deviceState)) {
                compatList.add(deviceState.getAudioDeviceAttributes());
            }
        }
        return compatList;
    }

    synchronized void addCompatibleAudioDevice(android.media.AudioDeviceAttributes ada) {
        addCompatibleAudioDevice(ada, true, false);
    }

    private void addCompatibleAudioDevice(android.media.AudioDeviceAttributes ada, boolean forceEnable, boolean forceInit) {
        if (!isDeviceCompatibleWithSpatializationModes(ada)) {
            return;
        }
        loglogi("addCompatibleAudioDevice: dev=" + ada);
        com.android.server.audio.AdiDeviceState deviceState = findSACompatibleDeviceStateForAudioDeviceAttributes(ada);
        com.android.server.audio.AdiDeviceState updatedDevice = null;
        if (deviceState != null) {
            if (forceInit) {
                initSAState(deviceState);
            }
            if (forceEnable && !deviceState.isSAEnabled()) {
                updatedDevice = deviceState;
                updatedDevice.setSAEnabled(true);
            }
        } else {
            int canonicalDeviceType = getCanonicalDeviceType(ada.getType(), ada.getInternalType());
            if (canonicalDeviceType == 0) {
                android.util.Log.e(TAG, "addCompatibleAudioDevice with incompatible AudioDeviceAttributes " + ada);
                return;
            } else {
                updatedDevice = new com.android.server.audio.AdiDeviceState(canonicalDeviceType, ada.getInternalType(), ada.getAddress());
                initSAState(updatedDevice);
                this.mDeviceBroker.addOrUpdateDeviceSAStateInInventory(updatedDevice, true);
            }
        }
        if (updatedDevice != null) {
            onRoutingUpdated();
            this.mDeviceBroker.postPersistAudioDeviceSettings();
            logDeviceState(updatedDevice, "addCompatibleAudioDevice");
        }
    }

    void reInitSAState(com.android.server.audio.AdiDeviceState device) {
        initSAState(device);
    }

    private void initSAState(com.android.server.audio.AdiDeviceState device) {
        boolean z;
        if (device == null) {
            return;
        }
        int spatMode = SPAT_MODE_FOR_DEVICE_TYPE.get(device.getDeviceType(), Integer.MIN_VALUE);
        if (spatMode == 0) {
            z = this.mBinauralEnabledDefault;
        } else if (spatMode == 1) {
            z = this.mTransauralEnabledDefault;
        } else {
            z = false;
        }
        device.setSAEnabled(z);
        device.setHeadTrackerEnabled(this.mHeadTrackingEnabledDefault);
    }

    static void logDeviceState(com.android.server.audio.AdiDeviceState deviceState, java.lang.String event) {
        int deviceType = android.media.AudioDeviceInfo.convertDeviceTypeToInternalDevice(deviceState.getDeviceType());
        java.lang.String deviceName = android.media.AudioSystem.getDeviceName(deviceType);
        new android.media.MediaMetrics.Item(METRICS_DEVICE_PREFIX + deviceName).set(android.media.MediaMetrics.Property.ADDRESS, deviceState.getDeviceAddress()).set(android.media.MediaMetrics.Property.ENABLED, deviceState.isSAEnabled() ? "true" : "false").set(android.media.MediaMetrics.Property.EVENT, android.text.TextUtils.emptyIfNull(event)).set(android.media.MediaMetrics.Property.HAS_HEAD_TRACKER, deviceState.hasHeadTracker() ? "true" : "false").set(android.media.MediaMetrics.Property.HEAD_TRACKER_ENABLED, deviceState.isHeadTrackerEnabled() ? "true" : "false").record();
    }

    synchronized void removeCompatibleAudioDevice(android.media.AudioDeviceAttributes ada) {
        loglogi("removeCompatibleAudioDevice: dev=" + ada);
        com.android.server.audio.AdiDeviceState deviceState = findSACompatibleDeviceStateForAudioDeviceAttributes(ada);
        if (deviceState != null && deviceState.isSAEnabled()) {
            deviceState.setSAEnabled(false);
            onRoutingUpdated();
            this.mDeviceBroker.postPersistAudioDeviceSettings();
            logDeviceState(deviceState, "removeCompatibleAudioDevice");
        }
    }

    private static int getCanonicalDeviceType(int deviceType, int internalDeviceType) {
        if (android.media.AudioSystem.isBluetoothDevice(internalDeviceType)) {
            return deviceType;
        }
        int spatMode = SPAT_MODE_FOR_DEVICE_TYPE.get(deviceType, Integer.MIN_VALUE);
        if (spatMode == 1) {
            return 2;
        }
        if (spatMode == 0) {
            return 4;
        }
        return 0;
    }

    private com.android.server.audio.AdiDeviceState findSACompatibleDeviceStateForAudioDeviceAttributes(android.media.AudioDeviceAttributes ada) {
        com.android.server.audio.AdiDeviceState deviceState = this.mDeviceBroker.findDeviceStateForAudioDeviceAttributes(ada, getCanonicalDeviceType(ada.getType(), ada.getInternalType()));
        if (deviceState == null || !isSADevice(deviceState)) {
            return null;
        }
        return deviceState;
    }

    private synchronized android.util.Pair<java.lang.Boolean, java.lang.Boolean> evaluateState(android.media.AudioDeviceAttributes ada) {
        int deviceType = ada.getType();
        if (!this.mSACapableDeviceTypes.contains(java.lang.Integer.valueOf(deviceType))) {
            android.util.Log.i(TAG, "Device incompatible with Spatial Audio dev:" + ada);
            return new android.util.Pair<>(false, false);
        }
        int spatMode = SPAT_MODE_FOR_DEVICE_TYPE.get(deviceType, Integer.MIN_VALUE);
        if (spatMode == Integer.MIN_VALUE) {
            android.util.Log.e(TAG, "no spatialization mode found for device type:" + deviceType);
            return new android.util.Pair<>(false, false);
        }
        com.android.server.audio.AdiDeviceState deviceState = findSACompatibleDeviceStateForAudioDeviceAttributes(ada);
        if (deviceState == null) {
            android.util.Log.i(TAG, "no spatialization device state found for Spatial Audio device:" + ada);
            return new android.util.Pair<>(false, false);
        }
        boolean available = true;
        if (android.media.AudioSystem.isBluetoothDevice(deviceType)) {
            if (deviceState.getAudioDeviceCategory() == 0 || deviceState.getAudioDeviceCategory() == 3) {
                available = spatMode == 0 && this.mBinauralSupported;
            } else {
                available = false;
            }
        }
        if (!mSpatialzerForSpeakerSupport) {
            updateUserEnableState(this.mUserStateOfSpeaker == 1, true);
        }
        android.util.Log.d(TAG, "Spatial Audio device type:" + deviceType + ", mEnabled:" + deviceState.isSAEnabled() + ", mUserEnable:" + deviceState.isUserEnabled() + ", mUserStateOfSpeaker:" + this.mUserStateOfSpeaker);
        return new android.util.Pair<>(java.lang.Boolean.valueOf(deviceState.isSAEnabled()), java.lang.Boolean.valueOf(available));
    }

    private synchronized void addWirelessDeviceIfNew(android.media.AudioDeviceAttributes ada) {
        if (isDeviceCompatibleWithSpatializationModes(ada)) {
            if (findSACompatibleDeviceStateForAudioDeviceAttributes(ada) == null) {
                int canonicalDeviceType = getCanonicalDeviceType(ada.getType(), ada.getInternalType());
                if (canonicalDeviceType == 0) {
                    android.util.Log.e(TAG, "addWirelessDeviceIfNew with incompatible AudioDeviceAttributes " + ada);
                    return;
                }
                com.android.server.audio.AdiDeviceState deviceState = new com.android.server.audio.AdiDeviceState(canonicalDeviceType, ada.getInternalType(), ada.getAddress());
                initSAState(deviceState);
                this.mDeviceBroker.addOrUpdateDeviceSAStateInInventory(deviceState, true);
                this.mDeviceBroker.postPersistAudioDeviceSettings();
                logDeviceState(deviceState, "addWirelessDeviceIfNew");
                if (this.mDesiredHeadTrackingMode != -2) {
                    android.util.Log.d(TAG, "addWirelessDeviceIfNew postInitSensors");
                    this.newWirelessDevice = ada;
                    postInitSensors();
                }
            }
        }
    }

    synchronized boolean isEnabled() {
        switch (this.mState) {
            case 0:
            case 1:
            case 3:
            case 6:
                loglogi("isEnabled false");
                return false;
            case 2:
            case 4:
            case 5:
            default:
                loglogi("isEnabled true");
                return true;
        }
    }

    synchronized boolean isAvailable() {
        switch (this.mState) {
            case 0:
            case 1:
            case 3:
            case 4:
                loglogi("isAvailable false");
                return false;
            case 2:
            default:
                loglogi("isAvailable true");
                return true;
        }
    }

    synchronized void refreshDevice(android.media.AudioDeviceAttributes ada, boolean initState) {
        com.android.server.audio.AdiDeviceState deviceState = findSACompatibleDeviceStateForAudioDeviceAttributes(ada);
        if (isAvailableForAdiDeviceState(deviceState)) {
            addCompatibleAudioDevice(ada, deviceState.isSAEnabled(), initState);
            setHeadTrackerEnabled(deviceState.isHeadTrackerEnabled(), ada);
        } else {
            removeCompatibleAudioDevice(ada);
        }
    }

    synchronized boolean isAvailableForDevice(android.media.AudioDeviceAttributes ada) {
        if (ada.getRole() != 2) {
            return false;
        }
        return isAvailableForAdiDeviceState(findSACompatibleDeviceStateForAudioDeviceAttributes(ada));
    }

    private boolean isAvailableForAdiDeviceState(com.android.server.audio.AdiDeviceState deviceState) {
        if (deviceState == null) {
            return false;
        }
        if (android.media.AudioSystem.isBluetoothDevice(deviceState.getInternalDeviceType()) && deviceState.getAudioDeviceCategory() != 0 && deviceState.getAudioDeviceCategory() != 3) {
            return false;
        }
        return true;
    }

    private synchronized boolean canBeSpatializedOnDevice(android.media.AudioAttributes attributes, android.media.AudioFormat format, java.util.ArrayList<android.media.AudioDeviceAttributes> devices) {
        if (devices.isEmpty()) {
            return false;
        }
        if (!isDeviceCompatibleWithSpatializationModes(devices.get(0))) {
            return false;
        }
        android.media.AudioDeviceAttributes[] devArray = new android.media.AudioDeviceAttributes[devices.size()];
        return android.media.AudioSystem.canBeSpatialized(attributes, format, (android.media.AudioDeviceAttributes[]) devices.toArray(devArray));
    }

    private boolean isDeviceCompatibleWithSpatializationModes(android.media.AudioDeviceAttributes ada) {
        byte modeForDevice = (byte) SPAT_MODE_FOR_DEVICE_TYPE.get(ada.getType(), -1);
        if ((modeForDevice == 0 && this.mBinauralSupported) || (modeForDevice == 1 && this.mTransauralSupported)) {
            return true;
        }
        return false;
    }

    boolean isSADevice(com.android.server.audio.AdiDeviceState deviceState) {
        return deviceState.getDeviceType() == getCanonicalDeviceType(deviceState.getDeviceType(), deviceState.getInternalDeviceType()) && isDeviceCompatibleWithSpatializationModes(deviceState.getAudioDeviceAttributes());
    }

    synchronized void setFeatureEnabled(boolean enabled) {
        loglogi("setFeatureEnabled(" + enabled + ") was featureEnabled:" + this.mFeatureEnabled);
        if (this.mFeatureEnabled == enabled) {
            if (sRoutingDevices.isEmpty()) {
                logloge("setFeatureEnabled: no device");
                return;
            } else if (isAndSupportSpeakerDevice() && this.mFeatureEnabled) {
                return;
            }
        }
        this.mFeatureEnabled = enabled;
        if (this.mFeatureEnabled) {
            if (this.mState == 1) {
                android.util.Log.e(TAG, "Can't enabled Spatial Audio, unsupported");
            } else {
                if (this.mState == 0) {
                    init(true);
                }
                setSpatializerEnabledInt(true);
            }
        } else {
            setSpatializerEnabledInt(false);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0064 A[Catch: all -> 0x0104, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x002b, B:7:0x0033, B:10:0x003b, B:12:0x0043, B:14:0x0051, B:20:0x005c, B:21:0x0064, B:23:0x0090, B:25:0x0096, B:29:0x009e, B:31:0x00a2, B:35:0x00a9, B:37:0x00c7, B:40:0x00d7, B:41:0x00da, B:42:0x00dd, B:44:0x00e1, B:45:0x00e3, B:47:0x00e7, B:49:0x00eb, B:52:0x00f4, B:54:0x00f8, B:55:0x00fb, B:56:0x00ff), top: B:62:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    synchronized void setFeatureEnabled(boolean r8, boolean r9) {
        /*
            Method dump skipped, instruction units count: 263
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.SpatializerHelper.setFeatureEnabled(boolean, boolean):void");
    }

    synchronized void setSpatializerEnabledInt(boolean enabled) {
        switch (this.mState) {
            case 0:
                if (enabled) {
                    throw new java.lang.IllegalStateException("Can't enable when uninitialized");
                }
                break;
            case 1:
                if (enabled) {
                    android.util.Log.e(TAG, "Can't enable when unsupported");
                }
                break;
            case 3:
            case 6:
                if (enabled) {
                    createSpat();
                    android.util.Log.i(TAG, "setSpatializerEnabledInt() : before postPersistAudioHeadPhoneSettings, enabled = " + enabled);
                    if (!isSpeakerDevice()) {
                        this.mHeadphoneSpatializerState = true;
                        this.mDeviceBroker.getWrapper().postPersistAudioHeadPhoneSettings(this.mHeadphoneSpatializerState);
                    }
                    onRoutingUpdated();
                }
                break;
            case 4:
            case 5:
                if (!enabled) {
                    releaseSpat();
                    setDispatchFeatureEnabledState(false, "setSpatializerEnabledInt");
                    if (!isSpeakerDevice()) {
                        this.mHeadphoneSpatializerState = false;
                        android.util.Log.i(TAG, "setSpatializerEnabledInt() : before postPersistAudioHeadPhoneSettings, enabled = " + enabled);
                        this.mDeviceBroker.getWrapper().postPersistAudioHeadPhoneSettings(this.mHeadphoneSpatializerState);
                    }
                }
                break;
        }
    }

    synchronized void setSpatializerEnabledInt(boolean enabled, boolean fromUser) {
        switch (this.mState) {
            case 0:
                if (enabled) {
                    throw new java.lang.IllegalStateException("Can't enable when uninitialized");
                }
                break;
            case 1:
                if (enabled) {
                    android.util.Log.e(TAG, "Can't enable when unsupported");
                }
                break;
            case 3:
            case 6:
                if (enabled) {
                    createSpat();
                    if (fromUser) {
                        updateUserEnableState(true, false);
                    }
                    onRoutingUpdated();
                }
                break;
            case 4:
            case 5:
                if (!enabled) {
                    releaseSpat();
                    setDispatchFeatureEnabledState(false, "setSpatializerEnabledInt");
                    if (fromUser) {
                        updateUserEnableState(false, false);
                        if (!mSpatialzerForSpeakerSupport) {
                            updateUserEnableState(false, true);
                        }
                    }
                }
                break;
        }
    }

    synchronized void updateUserEnableState(boolean enable, boolean handleSpeakerState) {
        com.android.server.audio.AdiDeviceState deviceState;
        if (handleSpeakerState) {
            for (com.android.server.audio.AdiDeviceState device : this.mDeviceBroker.getImmutableDeviceInventory()) {
                if (device != null && device.getDeviceType() == 2) {
                    device.setUserEnabled(enable);
                }
            }
        } else if (!sRoutingDevices.isEmpty() && sRoutingDevices.get(0) != null && (deviceState = this.mDeviceBroker.findDeviceStateForAudioDeviceAttributes(sRoutingDevices.get(0), getCanonicalDeviceType(sRoutingDevices.get(0).getType(), sRoutingDevices.get(0).getInternalType()))) != null) {
            deviceState.setUserEnabled(enable);
        }
        this.mDeviceBroker.postPersistAudioDeviceSettings();
    }

    synchronized boolean isAndSupportSpeakerDevice() {
        if (!mSpatialzerForSpeakerSupport) {
            return false;
        }
        return isSpeakerDevice();
    }

    synchronized boolean isSpeakerDevice() {
        if (sRoutingDevices.isEmpty() || sRoutingDevices.get(0) == null) {
            return false;
        }
        return sRoutingDevices.get(0).getType() == 2;
    }

    synchronized boolean getHeadphoneSpatializerState() {
        return this.mHeadphoneSpatializerState;
    }

    synchronized int getCapableImmersiveAudioLevel() {
        return this.mCapableSpatLevel;
    }

    synchronized void registerStateCallback(android.media.ISpatializerCallback callback) {
        this.mStateCallbacks.register(callback);
    }

    synchronized void unregisterStateCallback(android.media.ISpatializerCallback callback) {
        this.mStateCallbacks.unregister(callback);
    }

    private synchronized void setDispatchFeatureEnabledState(boolean featureEnabled, java.lang.String source) {
        if (featureEnabled) {
            switch (this.mState) {
                case 3:
                    this.mState = 4;
                    break;
                case 4:
                case 5:
                    loglogi("setDispatchFeatureEnabledState(" + featureEnabled + ") no dispatch: mState:" + spatStateString(this.mState) + " src:" + source);
                    return;
                case 6:
                    this.mState = 5;
                    break;
                default:
                    throw new java.lang.IllegalStateException("Invalid mState:" + this.mState + " for enabled true");
            }
        } else {
            switch (this.mState) {
                case 3:
                case 6:
                    loglogi("setDispatchFeatureEnabledState(" + featureEnabled + ") no dispatch: mState:" + spatStateString(this.mState) + " src:" + source);
                    return;
                case 4:
                    this.mState = 3;
                    break;
                case 5:
                    this.mState = 6;
                    break;
                default:
                    throw new java.lang.IllegalStateException("Invalid mState:" + this.mState + " for enabled false");
            }
        }
        loglogi("setDispatchFeatureEnabledState(" + featureEnabled + ") mState:" + spatStateString(this.mState));
        int nbCallbacks = this.mStateCallbacks.beginBroadcast();
        for (int i = 0; i < nbCallbacks; i++) {
            try {
                this.mStateCallbacks.getBroadcastItem(i).dispatchSpatializerEnabledChanged(featureEnabled);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Error in dispatchSpatializerEnabledChanged", e);
            }
        }
        this.mStateCallbacks.finishBroadcast();
    }

    private synchronized void setDispatchAvailableState(boolean available) {
        switch (this.mState) {
            case 0:
            case 1:
                throw new java.lang.IllegalStateException("Should not update available state in state:" + this.mState);
            case 3:
                if (available) {
                    this.mState = 6;
                } else {
                    loglogi("setDispatchAvailableState(" + available + ") no dispatch: mState:" + spatStateString(this.mState));
                    return;
                }
                break;
            case 4:
                if (available) {
                    this.mState = 5;
                } else {
                    loglogi("setDispatchAvailableState(" + available + ") no dispatch: mState:" + spatStateString(this.mState));
                    return;
                }
                break;
            case 5:
                if (available) {
                    loglogi("setDispatchAvailableState(" + available + ") no dispatch: mState:" + spatStateString(this.mState));
                    return;
                }
                this.mState = 4;
                break;
            case 6:
                if (available) {
                    loglogi("setDispatchAvailableState(" + available + ") no dispatch: mState:" + spatStateString(this.mState));
                    return;
                }
                this.mState = 3;
                break;
        }
        loglogi("setDispatchAvailableState(" + available + ") mState:" + spatStateString(this.mState));
        int nbCallbacks = this.mStateCallbacks.beginBroadcast();
        for (int i = 0; i < nbCallbacks; i++) {
            try {
                this.mStateCallbacks.getBroadcastItem(i).dispatchSpatializerAvailableChanged(available);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Error in dispatchSpatializerEnabledChanged", e);
            }
        }
        this.mStateCallbacks.finishBroadcast();
    }

    private void createSpat() {
        if (this.mSpat == null) {
            this.mSpatCallback = new com.android.server.audio.SpatializerHelper.SpatializerCallback();
            this.mSpat = android.media.AudioSystem.getSpatializer(this.mSpatCallback);
            if (this.mSpat == null) {
                android.util.Log.e(TAG, "createSpat(): No Spatializer found");
                postReset();
                return;
            }
            try {
                if (this.mIsHeadTrackingSupported && this.mSpat != null) {
                    this.mActualHeadTrackingMode = headTrackingModeTypeToSpatializerInt(this.mSpat.getActualHeadTrackingMode());
                    this.mSpat.registerHeadTrackingCallback(this.mSpatHeadTrackingCallback);
                }
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Can't configure head tracking", e);
                this.mState = 1;
                this.mCapableSpatLevel = 0;
                this.mActualHeadTrackingMode = -2;
            }
        }
    }

    private void releaseSpat() {
        if (this.mSpat != null) {
            this.mSpatCallback = null;
            try {
                if (this.mIsHeadTrackingSupported) {
                    this.mSpat.registerHeadTrackingCallback((android.media.ISpatializerHeadTrackingCallback) null);
                }
                this.mHeadTrackerAvailable = false;
                this.mSpat.release();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Can't set release spatializer cleanly", e);
            }
            this.mSpat = null;
        }
    }

    synchronized boolean canBeSpatialized(android.media.AudioAttributes attributes, android.media.AudioFormat format) {
        switch (this.mState) {
            case 0:
            case 1:
            case 3:
            case 4:
                logd("canBeSpatialized false due to state:" + this.mState);
                return false;
            case 2:
            default:
                switch (attributes.getUsage()) {
                    case 1:
                    case 14:
                        java.util.ArrayList<android.media.AudioDeviceAttributes> devices = getRoutingDevices(attributes);
                        if (devices.isEmpty()) {
                            logloge("canBeSpatialized got no device for " + attributes);
                            return false;
                        }
                        boolean able = canBeSpatializedOnDevice(attributes, format, devices);
                        logd("canBeSpatialized usage:" + attributes.getUsage() + " format:" + format.toLogFriendlyString() + " returning " + able);
                        return able;
                    default:
                        logd("canBeSpatialized false due to usage:" + attributes.getUsage());
                        return false;
                }
        }
    }

    synchronized void registerHeadTrackingModeCallback(android.media.ISpatializerHeadTrackingModeCallback callback) {
        this.mHeadTrackingModeCallbacks.register(callback);
    }

    synchronized void unregisterHeadTrackingModeCallback(android.media.ISpatializerHeadTrackingModeCallback callback) {
        this.mHeadTrackingModeCallbacks.unregister(callback);
    }

    synchronized void registerHeadTrackerAvailableCallback(android.media.ISpatializerHeadTrackerAvailableCallback cb, boolean register) {
        if (register) {
            this.mHeadTrackerCallbacks.register(cb);
        } else {
            this.mHeadTrackerCallbacks.unregister(cb);
        }
    }

    synchronized int[] getSupportedHeadTrackingModes() {
        return this.mSupportedHeadTrackingModes;
    }

    synchronized int getActualHeadTrackingMode() {
        return this.mActualHeadTrackingMode;
    }

    synchronized int getDesiredHeadTrackingMode() {
        return this.mDesiredHeadTrackingMode;
    }

    synchronized void setGlobalTransform(float[] transform) {
        if (transform.length != 6) {
            throw new java.lang.IllegalArgumentException("invalid array size" + transform.length);
        }
        if (checkSpatializerForHeadTracking("setGlobalTransform")) {
            try {
                this.mSpat.setGlobalTransform(transform);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Error calling setGlobalTransform", e);
            }
        }
    }

    synchronized void recenterHeadTracker() {
        if (checkSpatializerForHeadTracking("recenterHeadTracker")) {
            try {
                this.mSpat.recenterHeadTracker();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Error calling recenterHeadTracker", e);
            }
        }
    }

    synchronized void setDisplayOrientation(float displayOrientation) {
        if (checkSpatializer("setDisplayOrientation")) {
            try {
                this.mSpat.setDisplayOrientation(displayOrientation);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Error calling setDisplayOrientation", e);
            }
        }
    }

    synchronized void setFoldState(boolean folded) {
        if (checkSpatializer("setFoldState")) {
            try {
                this.mSpat.setFoldState(folded);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Error calling setFoldState", e);
            }
        }
    }

    synchronized void setDesiredHeadTrackingMode(int mode) {
        if (checkSpatializerForHeadTracking("setDesiredHeadTrackingMode")) {
            if (mode != -1) {
                this.mDesiredHeadTrackingModeWhenEnabled = mode;
            }
            try {
                if (this.mDesiredHeadTrackingMode != mode) {
                    this.mDesiredHeadTrackingMode = mode;
                    dispatchDesiredHeadTrackingMode(mode);
                }
                android.util.Log.i(TAG, "setDesiredHeadTrackingMode(" + android.media.Spatializer.headtrackingModeToString(mode) + ")");
                this.mSpat.setDesiredHeadTrackingMode(spatializerIntToHeadTrackingModeType(mode));
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Error calling setDesiredHeadTrackingMode", e);
            }
        }
    }

    synchronized void setHeadTrackerEnabled(boolean enabled, android.media.AudioDeviceAttributes ada) {
        if (!this.mIsHeadTrackingSupported) {
            android.util.Log.v(TAG, "no headtracking support, ignoring setHeadTrackerEnabled to " + enabled + " for " + ada);
        }
        com.android.server.audio.AdiDeviceState deviceState = findSACompatibleDeviceStateForAudioDeviceAttributes(ada);
        if (deviceState == null) {
            return;
        }
        if (!deviceState.hasHeadTracker()) {
            android.util.Log.e(TAG, "Called setHeadTrackerEnabled enabled:" + enabled + " device:" + ada + " on a device without headtracker");
            return;
        }
        android.util.Log.i(TAG, "setHeadTrackerEnabled enabled:" + enabled + " device:" + ada);
        deviceState.setHeadTrackerEnabled(enabled);
        this.mDeviceBroker.postPersistAudioDeviceSettings();
        logDeviceState(deviceState, "setHeadTrackerEnabled");
        if (sRoutingDevices.isEmpty()) {
            logloge("setHeadTrackerEnabled: no device, bailing");
            return;
        }
        android.media.AudioDeviceAttributes currentDevice = sRoutingDevices.get(0);
        if (currentDevice.getType() == ada.getType() && currentDevice.getAddress().equals(ada.getAddress())) {
            setDesiredHeadTrackingMode(enabled ? this.mDesiredHeadTrackingModeWhenEnabled : -1);
            this.mAudioService.getWrapper().getExtImpl().setSpatialAudioState(currentDevice.getAddress(), enabled);
            this.mSpatialAudioState = enabled;
            if (enabled && !this.mHeadTrackerAvailable) {
                postInitSensors();
            }
        }
    }

    synchronized boolean hasHeadTracker(android.media.AudioDeviceAttributes ada) {
        boolean z = false;
        if (!this.mIsHeadTrackingSupported) {
            android.util.Log.v(TAG, "no headtracking support, hasHeadTracker always false for " + ada);
            return false;
        }
        com.android.server.audio.AdiDeviceState deviceState = findSACompatibleDeviceStateForAudioDeviceAttributes(ada);
        if (deviceState != null && deviceState.hasHeadTracker()) {
            z = true;
        }
        return z;
    }

    synchronized boolean setHasHeadTracker(android.media.AudioDeviceAttributes ada) {
        if (!this.mIsHeadTrackingSupported) {
            android.util.Log.v(TAG, "no headtracking support, setHasHeadTracker always false for " + ada);
            return false;
        }
        com.android.server.audio.AdiDeviceState deviceState = findSACompatibleDeviceStateForAudioDeviceAttributes(ada);
        if (deviceState != null) {
            if (!deviceState.hasHeadTracker()) {
                deviceState.setHasHeadTracker(true);
                this.mDeviceBroker.postPersistAudioDeviceSettings();
                logDeviceState(deviceState, "setHasHeadTracker");
            }
            return deviceState.isHeadTrackerEnabled();
        }
        android.util.Log.e(TAG, "setHasHeadTracker: device not found for:" + ada);
        return false;
    }

    synchronized boolean isHeadTrackerEnabled(android.media.AudioDeviceAttributes ada) {
        boolean z = false;
        if (!this.mIsHeadTrackingSupported) {
            android.util.Log.v(TAG, "no headtracking support, isHeadTrackerEnabled always false for " + ada);
            return false;
        }
        com.android.server.audio.AdiDeviceState deviceState = findSACompatibleDeviceStateForAudioDeviceAttributes(ada);
        if (deviceState != null && deviceState.hasHeadTracker() && deviceState.isHeadTrackerEnabled()) {
            z = true;
        }
        return z;
    }

    synchronized boolean isHeadTrackerAvailable() {
        return this.mHeadTrackerAvailable;
    }

    private boolean checkSpatializer(java.lang.String funcName) {
        switch (this.mState) {
            case 3:
            case 4:
            case 5:
            case 6:
                if (this.mSpat == null) {
                    if (this.mFeatureEnabled) {
                        android.util.Log.e(TAG, "checkSpatializer(): called from " + funcName + "(), native spatializer should not be null in state: " + this.mState);
                        postReset();
                    }
                }
                break;
        }
        return false;
    }

    private boolean checkSpatializerForHeadTracking(java.lang.String funcName) {
        return checkSpatializer(funcName) && this.mIsHeadTrackingSupported;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchActualHeadTrackingMode(int newMode) {
        int nbCallbacks = this.mHeadTrackingModeCallbacks.beginBroadcast();
        for (int i = 0; i < nbCallbacks; i++) {
            try {
                this.mHeadTrackingModeCallbacks.getBroadcastItem(i).dispatchSpatializerActualHeadTrackingModeChanged(newMode);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Error in dispatchSpatializerActualHeadTrackingModeChanged(" + newMode + ")", e);
            }
        }
        this.mHeadTrackingModeCallbacks.finishBroadcast();
    }

    private void dispatchDesiredHeadTrackingMode(int newMode) {
        int nbCallbacks = this.mHeadTrackingModeCallbacks.beginBroadcast();
        for (int i = 0; i < nbCallbacks; i++) {
            try {
                this.mHeadTrackingModeCallbacks.getBroadcastItem(i).dispatchSpatializerDesiredHeadTrackingModeChanged(newMode);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Error in dispatchSpatializerDesiredHeadTrackingModeChanged(" + newMode + ")", e);
            }
        }
        this.mHeadTrackingModeCallbacks.finishBroadcast();
    }

    private void dispatchHeadTrackerAvailable(boolean available) {
        int nbCallbacks = this.mHeadTrackerCallbacks.beginBroadcast();
        for (int i = 0; i < nbCallbacks; i++) {
            try {
                this.mHeadTrackerCallbacks.getBroadcastItem(i).dispatchSpatializerHeadTrackerAvailable(available);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Error in dispatchSpatializerHeadTrackerAvailable(" + available + ")", e);
            }
        }
        this.mHeadTrackerCallbacks.finishBroadcast();
    }

    synchronized void registerHeadToSoundstagePoseCallback(android.media.ISpatializerHeadToSoundStagePoseCallback callback) {
        this.mHeadPoseCallbacks.register(callback);
    }

    synchronized void unregisterHeadToSoundstagePoseCallback(android.media.ISpatializerHeadToSoundStagePoseCallback callback) {
        this.mHeadPoseCallbacks.unregister(callback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchPoseUpdate(float[] pose) {
        int nbCallbacks = this.mHeadPoseCallbacks.beginBroadcast();
        for (int i = 0; i < nbCallbacks; i++) {
            try {
                this.mHeadPoseCallbacks.getBroadcastItem(i).dispatchPoseChanged(pose);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Error in dispatchPoseChanged", e);
            }
        }
        this.mHeadPoseCallbacks.finishBroadcast();
    }

    synchronized void setEffectParameter(int key, byte[] value) {
        switch (this.mState) {
            case 0:
            case 1:
                throw new java.lang.IllegalStateException("Can't set parameter key:" + key + " without a spatializer");
            case 3:
            case 4:
            case 5:
            case 6:
                if (this.mSpat == null) {
                    android.util.Log.e(TAG, "setParameter(" + key + "): null spatializer in state: " + this.mState);
                    return;
                }
                break;
        }
        try {
            this.mSpat.setParameter(key, value);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Error in setParameter for key:" + key, e);
        }
    }

    synchronized void getEffectParameter(int key, byte[] value) {
        switch (this.mState) {
            case 0:
            case 1:
                throw new java.lang.IllegalStateException("Can't get parameter key:" + key + " without a spatializer");
            case 3:
            case 4:
            case 5:
            case 6:
                if (this.mSpat == null) {
                    android.util.Log.e(TAG, "getParameter(" + key + "): null spatializer in state: " + this.mState);
                    return;
                }
                break;
        }
        try {
            this.mSpat.getParameter(key, value);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Error in getParameter for key:" + key, e);
        }
    }

    synchronized int getOutput() {
        switch (this.mState) {
            case 0:
            case 1:
                throw new java.lang.IllegalStateException("Can't get output without a spatializer");
            case 3:
            case 4:
            case 5:
            case 6:
                if (this.mSpat == null) {
                    throw new java.lang.IllegalStateException("null Spatializer for getOutput");
                }
                break;
                break;
        }
        try {
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Error in getOutput", e);
            return 0;
        }
        return this.mSpat.getOutput();
    }

    synchronized void registerSpatializerOutputCallback(android.media.ISpatializerOutputCallback callback) {
        this.mOutputCallbacks.register(callback);
    }

    synchronized void unregisterSpatializerOutputCallback(android.media.ISpatializerOutputCallback callback) {
        this.mOutputCallbacks.unregister(callback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchOutputUpdate(int output) {
        int nbCallbacks = this.mOutputCallbacks.beginBroadcast();
        for (int i = 0; i < nbCallbacks; i++) {
            try {
                this.mOutputCallbacks.getBroadcastItem(i).dispatchSpatializerOutputChanged(output);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Error in dispatchOutputUpdate", e);
            }
        }
        this.mOutputCallbacks.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postInitSensors() {
        this.mAudioService.postInitSpatializerHeadTrackingSensors();
    }

    synchronized void onInitSensors() {
        boolean z = true;
        boolean init = this.mFeatureEnabled && this.mSpatLevel != 0;
        java.lang.String action = init ? "initializing" : "releasing";
        if (this.mSpat == null) {
            logloge("not " + action + " sensors, null spatializer");
            return;
        }
        if (!this.mIsHeadTrackingSupported) {
            logloge("not " + action + " sensors, spatializer doesn't support headtracking");
            return;
        }
        int headHandle = -1;
        int screenHandle = -1;
        boolean isFirstAddDevice = false;
        if (this.newWirelessDevice != null && !sRoutingDevices.isEmpty() && sRoutingDevices.get(0) != null && sRoutingDevices.get(0).getType() == this.newWirelessDevice.getType() && sRoutingDevices.get(0).getAddress().equals(this.newWirelessDevice.getAddress())) {
            android.util.Log.d(TAG, "onInitSensors, newWirelessDevice first add check headtracker");
            this.newWirelessDevice = null;
            isFirstAddDevice = true;
        }
        if (init || isFirstAddDevice) {
            if (this.mSensorManager == null) {
                try {
                    this.mSensorManager = (android.hardware.SensorManager) this.mAudioService.mContext.getSystemService(com.android.server.am.IOplusSceneManager.APP_SCENE_SENSOR);
                    this.mDynSensorCallback = new com.android.server.audio.SpatializerHelper.HelperDynamicSensorCallback();
                    this.mSensorManager.registerDynamicSensorCallback(this.mDynSensorCallback);
                } catch (java.lang.Exception e) {
                    android.util.Log.e(TAG, "Error with SensorManager, can't initialize sensors", e);
                    this.mSensorManager = null;
                    this.mDynSensorCallback = null;
                    return;
                }
            }
            headHandle = getHeadSensorHandleUpdateTracker();
            loglogi("head tracker sensor handle initialized to " + headHandle);
            screenHandle = getScreenSensorHandle();
            android.util.Log.i(TAG, "found screen sensor handle initialized to " + screenHandle);
        } else if (this.mSensorManager != null && this.mDynSensorCallback != null) {
            this.mSensorManager.unregisterDynamicSensorCallback(this.mDynSensorCallback);
            this.mSensorManager = null;
            this.mDynSensorCallback = null;
        }
        try {
            android.util.Log.i(TAG, "setScreenSensor:" + screenHandle);
            this.mSpat.setScreenSensor(screenHandle);
        } catch (java.lang.Exception e2) {
            android.util.Log.e(TAG, "Error calling setScreenSensor:" + screenHandle, e2);
        }
        try {
            android.util.Log.i(TAG, "setHeadSensor:" + headHandle);
            this.mSpat.setHeadSensor(headHandle);
            if (this.mHeadTrackerAvailable != (headHandle != -1)) {
                if (headHandle == -1) {
                    z = false;
                }
                this.mHeadTrackerAvailable = z;
                dispatchHeadTrackerAvailable(this.mHeadTrackerAvailable);
            }
        } catch (java.lang.Exception e3) {
            android.util.Log.e(TAG, "Error calling setHeadSensor:" + headHandle, e3);
        }
        setDesiredHeadTrackingMode(this.mDesiredHeadTrackingMode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int headTrackingModeTypeToSpatializerInt(byte mode) {
        switch (mode) {
            case 0:
                return 0;
            case 1:
                return -1;
            case 2:
                return 1;
            case 3:
                return 2;
            default:
                throw new java.lang.IllegalArgumentException("Unexpected head tracking mode:" + ((int) mode));
        }
    }

    private static byte spatializerIntToHeadTrackingModeType(int sdkMode) {
        switch (sdkMode) {
            case -1:
                return (byte) 1;
            case 0:
                return (byte) 0;
            case 1:
                return (byte) 2;
            case 2:
                return (byte) 3;
            default:
                throw new java.lang.IllegalArgumentException("Unexpected head tracking mode:" + sdkMode);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int spatializationLevelToSpatializerInt(byte level) {
        switch (level) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                throw new java.lang.IllegalArgumentException("Unexpected spatializer level:" + ((int) level));
        }
    }

    void dump(java.io.PrintWriter pw) {
        pw.println("SpatializerHelper:");
        pw.println("\tmState:" + this.mState);
        pw.println("\tmSpatLevel:" + this.mSpatLevel);
        pw.println("\tmCapableSpatLevel:" + this.mCapableSpatLevel);
        pw.println("\tmIsHeadTrackingSupported:" + this.mIsHeadTrackingSupported);
        java.lang.StringBuilder modesString = new java.lang.StringBuilder();
        for (int mode : this.mSupportedHeadTrackingModes) {
            modesString.append(android.media.Spatializer.headtrackingModeToString(mode)).append(" ");
        }
        pw.println("\tsupported head tracking modes:" + ((java.lang.Object) modesString));
        pw.println("\tmDesiredHeadTrackingMode:" + android.media.Spatializer.headtrackingModeToString(this.mDesiredHeadTrackingMode));
        pw.println("\tmActualHeadTrackingMode:" + android.media.Spatializer.headtrackingModeToString(this.mActualHeadTrackingMode));
        pw.println("\theadtracker available:" + this.mHeadTrackerAvailable);
        pw.println("\tsupports binaural:" + this.mBinauralSupported + " / transaural:" + this.mTransauralSupported);
        pw.println("\tmSpatOutput:" + this.mSpatOutput);
        pw.println("\thas FEATURE_AUDIO_SPATIAL_HEADTRACKING_LOW_LATENCY:" + this.mAudioService.mContext.getPackageManager().hasSystemFeature("android.hardware.audio.spatial.headtracking.low_latency"));
    }

    private static java.lang.String spatStateString(int state) {
        switch (state) {
            case 0:
                return "STATE_UNINITIALIZED";
            case 1:
                return "STATE_NOT_SUPPORTED";
            case 2:
            default:
                return "invalid state";
            case 3:
                return "STATE_DISABLED_UNAVAILABLE";
            case 4:
                return "STATE_ENABLED_UNAVAILABLE";
            case 5:
                return "STATE_ENABLED_AVAILABLE";
            case 6:
                return "STATE_DISABLED_AVAILABLE";
        }
    }

    private int getHeadSensorHandleUpdateTracker() {
        java.util.List<java.lang.String> deviceAddresses;
        java.util.Iterator<java.lang.String> it;
        android.hardware.Sensor htSensor = null;
        if (sRoutingDevices.isEmpty()) {
            logloge("getHeadSensorHandleUpdateTracker: no device, no head tracker");
            return -1;
        }
        android.media.AudioDeviceAttributes currentDevice = sRoutingDevices.get(0);
        java.util.List<java.lang.String> deviceAddresses2 = this.mAudioService.getDeviceIdentityAddresses(currentDevice);
        java.util.List<android.hardware.Sensor> sensors = this.mSensorManager.getDynamicSensorList(37);
        java.util.Iterator<java.lang.String> it2 = deviceAddresses2.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            java.lang.String address = it2.next();
            java.util.UUID routingDeviceUuid = com.android.server.audio.UuidUtils.uuidFromAudioDeviceAttributes(new android.media.AudioDeviceAttributes(currentDevice.getInternalType(), address));
            if (com.android.media.audio.Flags.dsaOverBtLeAudio()) {
                for (android.hardware.Sensor sensor : sensors) {
                    java.util.UUID uuid = sensor.getUuid();
                    if (!uuid.equals(routingDeviceUuid)) {
                        deviceAddresses = deviceAddresses2;
                        it = it2;
                    } else {
                        com.android.server.audio.SpatializerHelper.HeadtrackerInfo info = new com.android.server.audio.SpatializerHelper.HeadtrackerInfo(sensor);
                        if (android.media.AudioSystem.isBluetoothLeDevice(currentDevice.getInternalType())) {
                            deviceAddresses = deviceAddresses2;
                            it = it2;
                            if (info.getMajorVersion() == 2) {
                                htSensor = sensor;
                                break;
                            }
                            htSensor = sensor;
                        } else {
                            deviceAddresses = deviceAddresses2;
                            it = it2;
                            if (info.getMajorVersion() == 1) {
                                htSensor = sensor;
                                break;
                            }
                            htSensor = sensor;
                        }
                    }
                    if (htSensor == null && uuid.equals(com.android.server.audio.UuidUtils.STANDALONE_UUID)) {
                        htSensor = sensor;
                    }
                    deviceAddresses2 = deviceAddresses;
                    it2 = it;
                }
                deviceAddresses = deviceAddresses2;
                it = it2;
                if (htSensor != null) {
                    boolean enable = setHasHeadTracker(currentDevice);
                    java.lang.String tempAddress = currentDevice.getAddress() + "_" + currentDevice.getInternalType();
                    java.lang.String addressSensor = getSensorAddress(htSensor.getUuid());
                    android.util.Log.i(TAG, "getHeadSensorHandleUpdateTracker, enable1:" + enable + " mSpatialAudioState:" + this.mSpatialAudioState + " tempAddress:" + tempAddress + " mDeviceAddress:" + this.mDeviceAddress);
                    if (!tempAddress.equals(this.mDeviceAddress) || this.mSpatialAudioState != enable) {
                        this.mAudioService.getWrapper().getExtImpl().setSpatialAudioState(addressSensor, enable);
                    }
                    this.mSpatialAudioState = enable;
                    this.mDeviceAddress = tempAddress;
                    if (!enable) {
                        htSensor = null;
                    }
                } else {
                    android.util.Log.d(TAG, "getHeadSensorHandleUpdateTracker htSensor is null, need setHasHeadTracker false.");
                    com.android.server.audio.AdiDeviceState deviceState = findSACompatibleDeviceStateForAudioDeviceAttributes(currentDevice);
                    if (deviceState != null && deviceState.hasHeadTracker()) {
                        deviceState.setHasHeadTracker(false);
                        this.mDeviceBroker.postPersistAudioDeviceSettings();
                        logDeviceState(deviceState, "removeHasHeadTracker");
                    }
                    deviceAddresses2 = deviceAddresses;
                    it2 = it;
                }
            } else {
                deviceAddresses = deviceAddresses2;
                it = it2;
                java.util.Iterator<android.hardware.Sensor> it3 = sensors.iterator();
                while (true) {
                    if (!it3.hasNext()) {
                        break;
                    }
                    android.hardware.Sensor sensor2 = it3.next();
                    java.util.UUID uuid2 = sensor2.getUuid();
                    if (uuid2.equals(routingDeviceUuid)) {
                        htSensor = sensor2;
                        break;
                    }
                    if (uuid2.equals(com.android.server.audio.UuidUtils.STANDALONE_UUID)) {
                        htSensor = sensor2;
                    }
                }
                boolean enable2 = setHasHeadTracker(currentDevice);
                java.lang.String tempAddress2 = currentDevice.getAddress() + "_" + currentDevice.getInternalType();
                java.lang.String addressSensor2 = getSensorAddress(htSensor.getUuid());
                android.util.Log.i(TAG, "getHeadSensorHandleUpdateTracker, enable2:" + enable2 + " mSpatialAudioState:" + this.mSpatialAudioState + " tempAddress:" + tempAddress2 + " mDeviceAddress:" + this.mDeviceAddress);
                if (!tempAddress2.equals(this.mDeviceAddress) || this.mSpatialAudioState != enable2) {
                    this.mAudioService.getWrapper().getExtImpl().setSpatialAudioState(addressSensor2, enable2);
                }
                this.mSpatialAudioState = enable2;
                this.mDeviceAddress = tempAddress2;
                if (htSensor != null && !enable2) {
                    htSensor = null;
                }
                if (htSensor != null) {
                    break;
                }
                deviceAddresses2 = deviceAddresses;
                it2 = it;
            }
        }
        if (htSensor != null) {
            return htSensor.getHandle();
        }
        return -1;
    }

    private java.lang.String getSensorAddress(java.util.UUID uuid) {
        java.lang.String[] sensorStrs = null;
        if (uuid != null) {
            sensorStrs = uuid.toString().split("-");
        }
        java.lang.String sensorStr = "";
        if (sensorStrs.length > 0) {
            sensorStr = sensorStrs[sensorStrs.length - 1];
        }
        int index = 0;
        java.lang.StringBuilder addresSensor = new java.lang.StringBuilder();
        while (index < sensorStr.length()) {
            addresSensor.append(sensorStr.substring(index, index + 2).toUpperCase());
            index += 2;
            if (index < sensorStr.length()) {
                addresSensor.append(":");
            }
        }
        return addresSensor.toString();
    }

    void resetSpatialAudioState() {
        this.mSpatialAudioState = false;
        this.mDeviceAddress = "";
    }

    private static class HeadtrackerInfo {
        private final int mVersion;

        HeadtrackerInfo(android.hardware.Sensor sensor) {
            this.mVersion = sensor.getVersion();
        }

        int getMajorVersion() {
            return (this.mVersion & android.hardware.audio.common.V2_0.AudioFormat.MAIN_MASK) >> 24;
        }

        int getMinorVersion() {
            return (this.mVersion & 16711680) >> 16;
        }

        boolean hasAclTransport() {
            return getMajorVersion() == 2 && (this.mVersion & 1) != 0;
        }

        boolean hasIsoTransport() {
            return getMajorVersion() == 2 && (this.mVersion & 2) != 0;
        }
    }

    private int getScreenSensorHandle() {
        android.hardware.Sensor screenSensor = this.mSensorManager.getDefaultSensor(11);
        if (screenSensor == null) {
            return -1;
        }
        int screenHandle = screenSensor.getHandle();
        return screenHandle;
    }

    private java.util.ArrayList<android.media.AudioDeviceAttributes> getRoutingDevices(android.media.AudioAttributes aa) {
        java.util.ArrayList<android.media.AudioDeviceAttributes> devices = this.mASA.getDevicesForAttributes(aa, false);
        for (android.media.AudioDeviceAttributes ada : devices) {
            if (ada == null) {
                return new java.util.ArrayList<>(0);
            }
        }
        return devices;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loglogi(java.lang.String msg) {
        com.android.server.audio.AudioService.sSpatialLogger.enqueueAndLog(msg, 0, TAG);
    }

    private static java.lang.String logloge(java.lang.String msg) {
        com.android.server.audio.AudioService.sSpatialLogger.enqueueAndLog(msg, 1, TAG);
        return msg;
    }

    synchronized void forceStateForTest(int state) {
        this.mState = state;
    }

    synchronized void initForTest(boolean hasBinaural, boolean hasTransaural) {
        this.mBinauralSupported = hasBinaural;
        this.mTransauralSupported = hasTransaural;
    }
}
