package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public final class DeviceStateProviderImpl implements com.android.server.devicestate.DeviceStateProvider, com.android.server.input.InputManagerInternal.LidSwitchCallback, android.hardware.SensorEventListener, android.os.PowerManager.OnThermalStatusChangedListener {
    private static final java.lang.String CONFIG_FILE_NAME = "device_state_configuration.xml";
    private static final java.lang.String DATA_CONFIG_FILE_PATH = "system/devicestate/";
    private static final java.lang.String ODM_CONFIG_FILE_NAME = "device_state_configuration_private.xml";
    private static final java.lang.String ODM_CONFIG_FILE_PATH = "etc/devicestate/";
    private static final java.lang.String PROPERTY_APP_INACCESSIBLE = "com.android.server.policy.PROPERTY_APP_INACCESSIBLE";
    private static final java.lang.String PROPERTY_EMULATED_ONLY = "com.android.server.policy.PROPERTY_EMULATED_ONLY";
    private static final java.lang.String PROPERTY_EXTENDED_DEVICE_STATE_EXTERNAL_DISPLAY = "com.android.server.policy.PROPERTY_EXTENDED_DEVICE_STATE_EXTERNAL_DISPLAY";
    private static final java.lang.String PROPERTY_FEATURE_DUAL_DISPLAY_INTERNAL_DEFAULT = "com.android.server.policy.PROPERTY_FEATURE_DUAL_DISPLAY_INTERNAL_DEFAULT";
    private static final java.lang.String PROPERTY_FEATURE_REAR_DISPLAY = "com.android.server.policy.PROPERTY_FEATURE_REAR_DISPLAY";
    private static final java.lang.String PROPERTY_FOLDABLE_DISPLAY_CONFIGURATION_INNER_PRIMARY = "com.android.server.policy.PROPERTY_FOLDABLE_DISPLAY_CONFIGURATION_INNER_PRIMARY";
    private static final java.lang.String PROPERTY_FOLDABLE_DISPLAY_CONFIGURATION_OUTER_PRIMARY = "com.android.server.policy.PROPERTY_FOLDABLE_DISPLAY_CONFIGURATION_OUTER_PRIMARY";
    private static final java.lang.String PROPERTY_FOLDABLE_HARDWARE_CONFIGURATION_FOLD_IN_CLOSED = "com.android.server.policy.PROPERTY_FOLDABLE_HARDWARE_CONFIGURATION_FOLD_IN_CLOSED";
    private static final java.lang.String PROPERTY_FOLDABLE_HARDWARE_CONFIGURATION_FOLD_IN_HALF_OPEN = "com.android.server.policy.PROPERTY_FOLDABLE_HARDWARE_CONFIGURATION_FOLD_IN_HALF_OPEN";
    private static final java.lang.String PROPERTY_FOLDABLE_HARDWARE_CONFIGURATION_FOLD_IN_OPEN = "com.android.server.policy.PROPERTY_FOLDABLE_HARDWARE_CONFIGURATION_FOLD_IN_OPEN";
    private static final java.lang.String PROPERTY_POLICY_AVAILABLE_FOR_APP_REQUEST = "com.android.server.policy.PROPERTY_POLICY_AVAILABLE_FOR_APP_REQUEST";
    private static final java.lang.String PROPERTY_POLICY_CANCEL_OVERRIDE_REQUESTS = "com.android.server.policy.PROPERTY_POLICY_CANCEL_OVERRIDE_REQUESTS";
    private static final java.lang.String PROPERTY_POLICY_CANCEL_WHEN_REQUESTER_NOT_ON_TOP = "com.android.server.policy.PROPERTY_POLICY_CANCEL_WHEN_REQUESTER_NOT_ON_TOP";
    private static final java.lang.String PROPERTY_POLICY_UNSUPPORTED_WHEN_POWER_SAVE_MODE = "com.android.server.policy.PROPERTY_POLICY_UNSUPPORTED_WHEN_POWER_SAVE_MODE";
    private static final java.lang.String PROPERTY_POLICY_UNSUPPORTED_WHEN_THERMAL_STATUS_CRITICAL = "com.android.server.policy.PROPERTY_POLICY_UNSUPPORTED_WHEN_THERMAL_STATUS_CRITICAL";
    private static final java.lang.String PROPERTY_POWER_CONFIGURATION_TRIGGER_SLEEP = "com.android.server.policy.PROPERTY_POWER_CONFIGURATION_TRIGGER_SLEEP";
    private static final java.lang.String PROPERTY_POWER_CONFIGURATION_TRIGGER_WAKE = "com.android.server.policy.PROPERTY_POWER_CONFIGURATION_TRIGGER_WAKE";
    private static final java.lang.String TAG = "DeviceStateProviderImpl";
    private static final java.lang.String TYPE_SENSOR_HINGE_DETECT = "qti.sensor.hinge_detect";
    private static final java.lang.String VENDOR_CONFIG_FILE_PATH = "etc/devicestate/";
    private com.android.server.policy.DeviceStateProviderImpl.CameraAvailabilityCallback mCameraAvailabilityCallback;
    private android.hardware.camera2.CameraManager mCameraManager;
    private boolean mCameraOpen;
    private final android.content.Context mContext;
    private java.lang.Boolean mIsLidOpen;
    private boolean mKeyguardShow;
    private final android.hardware.devicestate.DeviceState[] mOrderedStates;
    private boolean mPowerSaveModeEnabled;
    private java.lang.String mSystemCameraName;
    private static final boolean DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final java.util.function.BooleanSupplier TRUE_BOOLEAN_SUPPLIER = new java.util.function.BooleanSupplier() { // from class: com.android.server.policy.DeviceStateProviderImpl$$ExternalSyntheticLambda0
        @Override // java.util.function.BooleanSupplier
        public final boolean getAsBoolean() {
            return com.android.server.policy.DeviceStateProviderImpl.lambda$static$0();
        }
    };
    private static final java.util.function.BooleanSupplier FALSE_BOOLEAN_SUPPLIER = new java.util.function.BooleanSupplier() { // from class: com.android.server.policy.DeviceStateProviderImpl$$ExternalSyntheticLambda1
        @Override // java.util.function.BooleanSupplier
        public final boolean getAsBoolean() {
            return com.android.server.policy.DeviceStateProviderImpl.lambda$static$1();
        }
    };
    static final android.hardware.devicestate.DeviceState DEFAULT_DEVICE_STATE = new android.hardware.devicestate.DeviceState(new android.hardware.devicestate.DeviceState.Configuration.Builder(0, "DEFAULT").build());
    public static com.android.server.policy.IDeviceStateProviderImplExt sExtImpl = (com.android.server.policy.IDeviceStateProviderImplExt) system.ext.loader.core.ExtLoader.type(com.android.server.policy.IDeviceStateProviderImplExt.class).create();
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArray<java.util.function.BooleanSupplier> mStateConditions = new android.util.SparseArray<>();
    private com.android.server.devicestate.DeviceStateProvider.Listener mListener = null;
    private int mLastReportedState = -1;
    private final java.util.Map<android.hardware.Sensor, android.hardware.SensorEvent> mLatestSensorEvent = new android.util.ArrayMap();
    private int mThermalStatus = 0;
    private android.hardware.Sensor mHingeSensor = null;
    private android.hardware.Sensor mGSensor = null;
    private com.android.server.policy.DeviceStateProviderImpl.DeviceStateProviderImplWrapper mDsplWrapper = new com.android.server.policy.DeviceStateProviderImpl.DeviceStateProviderImplWrapper();

    interface ReadableConfig {
        java.io.InputStream openRead() throws java.io.IOException;
    }

    static /* synthetic */ boolean lambda$static$0() {
        return true;
    }

    static /* synthetic */ boolean lambda$static$1() {
        return false;
    }

    public static com.android.server.policy.DeviceStateProviderImpl create(android.content.Context context) {
        java.io.File configFile = getConfigurationFile();
        if (configFile == null) {
            return createFromConfig(context, null);
        }
        return createFromConfig(context, new com.android.server.policy.DeviceStateProviderImpl.ReadableFileConfig(configFile));
    }

    static com.android.server.policy.DeviceStateProviderImpl createFromConfig(android.content.Context context, com.android.server.policy.DeviceStateProviderImpl.ReadableConfig readableConfig) {
        com.android.server.policy.devicestate.config.DeviceStateConfig config;
        java.util.List<android.hardware.devicestate.DeviceState> deviceStateList = new java.util.ArrayList<>();
        java.util.List<com.android.server.policy.devicestate.config.Conditions> conditionsList = new java.util.ArrayList<>();
        if (readableConfig != null && (config = parseConfig(readableConfig)) != null) {
            for (com.android.server.policy.devicestate.config.DeviceState stateConfig : config.getDeviceState()) {
                int state = stateConfig.getIdentifier().intValue();
                java.lang.String name = stateConfig.getName() == null ? "" : stateConfig.getName();
                java.util.Set<java.lang.Integer> systemProperties = new java.util.HashSet<>();
                java.util.Set<java.lang.Integer> physicalProperties = new java.util.HashSet<>();
                com.android.server.policy.devicestate.config.Properties configFlags = stateConfig.getProperties();
                if (configFlags != null) {
                    java.util.List<java.lang.String> configPropertyStrings = configFlags.getProperty();
                    for (int i = 0; i < configPropertyStrings.size(); i++) {
                        java.lang.String configPropertyString = configPropertyStrings.get(i);
                        addPropertyByString(configPropertyString, systemProperties, physicalProperties);
                    }
                }
                android.hardware.devicestate.DeviceState.Configuration deviceStateConfiguration = new android.hardware.devicestate.DeviceState.Configuration.Builder(state, name).setSystemProperties(systemProperties).setPhysicalProperties(physicalProperties).build();
                deviceStateList.add(new android.hardware.devicestate.DeviceState(deviceStateConfiguration));
                com.android.server.policy.devicestate.config.Conditions condition = stateConfig.getConditions();
                conditionsList.add(condition);
            }
        }
        if (deviceStateList.isEmpty()) {
            deviceStateList.add(DEFAULT_DEVICE_STATE);
            conditionsList.add(null);
        }
        return new com.android.server.policy.DeviceStateProviderImpl(context, deviceStateList, conditionsList);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00e2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void addPropertyByString(java.lang.String r20, java.util.Set<java.lang.Integer> r21, java.util.Set<java.lang.Integer> r22) {
        /*
            Method dump skipped, instruction units count: 522
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.policy.DeviceStateProviderImpl.addPropertyByString(java.lang.String, java.util.Set, java.util.Set):void");
    }

    private DeviceStateProviderImpl(android.content.Context context, java.util.List<android.hardware.devicestate.DeviceState> deviceStates, java.util.List<com.android.server.policy.devicestate.config.Conditions> stateConditions) {
        this.mSystemCameraName = null;
        com.android.internal.util.Preconditions.checkArgument(deviceStates.size() == stateConditions.size(), "Number of device states must be equal to the number of device state conditions.");
        this.mContext = context;
        android.hardware.devicestate.DeviceState[] orderedStates = (android.hardware.devicestate.DeviceState[]) deviceStates.toArray(new android.hardware.devicestate.DeviceState[deviceStates.size()]);
        java.util.Arrays.sort(orderedStates, java.util.Comparator.comparingInt(new java.util.function.ToIntFunction() { // from class: com.android.server.policy.DeviceStateProviderImpl$$ExternalSyntheticLambda2
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(java.lang.Object obj) {
                return ((android.hardware.devicestate.DeviceState) obj).getIdentifier();
            }
        }));
        this.mOrderedStates = orderedStates;
        setStateConditions(deviceStates, stateConditions);
        sExtImpl.init(this, this.mContext);
        this.mSystemCameraName = android.os.SystemProperties.get("ro.oplus.system.camera.name", "");
        final android.os.PowerManager powerManager = (android.os.PowerManager) context.getSystemService(android.os.PowerManager.class);
        if (powerManager != null) {
            if (hasThermalSensitiveState(deviceStates)) {
                powerManager.addThermalStatusListener(this);
            }
            if (hasPowerSaveSensitiveState(deviceStates)) {
                android.content.IntentFilter filter = new android.content.IntentFilter("android.os.action.POWER_SAVE_MODE_CHANGED_INTERNAL");
                android.content.BroadcastReceiver receiver = new android.content.BroadcastReceiver() { // from class: com.android.server.policy.DeviceStateProviderImpl.1
                    @Override // android.content.BroadcastReceiver
                    public void onReceive(android.content.Context context2, android.content.Intent intent) {
                        if ("android.os.action.POWER_SAVE_MODE_CHANGED_INTERNAL".equals(intent.getAction())) {
                            com.android.server.policy.DeviceStateProviderImpl.this.onPowerSaveModeChanged(powerManager.isPowerSaveMode());
                        }
                    }
                };
                this.mContext.registerReceiver(receiver, filter);
            }
        }
    }

    @Override // com.android.server.devicestate.DeviceStateProvider
    public void registerSensor() {
        sExtImpl.registerSensor(this);
    }

    private void setStateConditions(java.util.List<android.hardware.devicestate.DeviceState> deviceStates, java.util.List<com.android.server.policy.devicestate.config.Conditions> stateConditions) {
        android.util.ArraySet<android.hardware.Sensor> sensorsToListenTo;
        int i;
        int state;
        boolean allRequiredComponentsFound;
        android.util.ArraySet<android.hardware.Sensor> sensorsToListenTo2;
        android.util.ArraySet<android.hardware.Sensor> sensorsToListenTo3;
        int state2;
        java.util.List<android.hardware.devicestate.DeviceState> list = deviceStates;
        boolean shouldListenToLidSwitch = false;
        android.util.ArraySet<android.hardware.Sensor> sensorsToListenTo4 = new android.util.ArraySet<>();
        int i2 = 0;
        while (i2 < stateConditions.size()) {
            int state3 = list.get(i2).getIdentifier();
            if (DEBUG) {
                android.util.Slog.d(TAG, "Evaluating conditions for device state " + state3 + " (" + list.get(i2).getName() + ")");
            }
            com.android.server.policy.devicestate.config.Conditions conditions = stateConditions.get(i2);
            if (conditions == null) {
                if (list.get(i2).hasProperty(10)) {
                    this.mStateConditions.put(state3, FALSE_BOOLEAN_SUPPLIER);
                    sensorsToListenTo2 = sensorsToListenTo4;
                    i = i2;
                } else {
                    this.mStateConditions.put(state3, TRUE_BOOLEAN_SUPPLIER);
                    sensorsToListenTo2 = sensorsToListenTo4;
                    i = i2;
                }
            } else {
                boolean allRequiredComponentsFound2 = true;
                boolean lidSwitchRequired = false;
                android.util.ArraySet<android.hardware.Sensor> sensorsRequired = new android.util.ArraySet<>();
                java.util.List<java.util.function.BooleanSupplier> suppliers = new java.util.ArrayList<>();
                com.android.server.policy.devicestate.config.LidSwitchCondition lidSwitchCondition = conditions.getLidSwitch();
                if (lidSwitchCondition != null) {
                    suppliers.add(new com.android.server.policy.DeviceStateProviderImpl.LidSwitchBooleanSupplier(lidSwitchCondition.getOpen()));
                    lidSwitchRequired = true;
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Lid switch required");
                    }
                }
                java.util.List<com.android.server.policy.devicestate.config.SensorCondition> sensorConditions = conditions.getSensor();
                int j = 0;
                while (true) {
                    boolean allRequiredComponentsFound3 = allRequiredComponentsFound2;
                    com.android.server.policy.devicestate.config.LidSwitchCondition lidSwitchCondition2 = lidSwitchCondition;
                    if (j >= sensorConditions.size()) {
                        sensorsToListenTo = sensorsToListenTo4;
                        i = i2;
                        state = state3;
                        allRequiredComponentsFound = allRequiredComponentsFound3;
                        break;
                    }
                    com.android.server.policy.devicestate.config.SensorCondition sensorCondition = sensorConditions.get(j);
                    java.lang.String expectedSensorType = sensorCondition.getType();
                    java.util.List<com.android.server.policy.devicestate.config.SensorCondition> sensorConditions2 = sensorConditions;
                    java.lang.String expectedSensorName = sensorCondition.getName();
                    i = i2;
                    android.hardware.Sensor foundSensor = findSensor(expectedSensorType, expectedSensorName);
                    if (foundSensor == null) {
                        android.util.Slog.e(TAG, "Failed to find Sensor with type: " + expectedSensorType + " and name: " + expectedSensorName);
                        sensorsToListenTo = sensorsToListenTo4;
                        state = state3;
                        allRequiredComponentsFound = false;
                        break;
                    }
                    if (!DEBUG) {
                        sensorsToListenTo3 = sensorsToListenTo4;
                        state2 = state3;
                    } else {
                        state2 = state3;
                        sensorsToListenTo3 = sensorsToListenTo4;
                        android.util.Slog.d(TAG, "Found sensor with type: " + expectedSensorType + " (" + expectedSensorName + ")");
                    }
                    suppliers.add(new com.android.server.policy.DeviceStateProviderImpl.SensorBooleanSupplier(foundSensor, sensorCondition.getValue(), sensorCondition.getUnregister()));
                    if (sExtImpl.isNeedAddSubGravitySensor(expectedSensorType)) {
                        suppliers.add(new com.android.server.policy.DeviceStateProviderImpl.SubGravitySensorBooleanSupplier(foundSensor, sensorCondition.getValue(), true));
                    }
                    sensorsRequired.add(foundSensor);
                    j++;
                    allRequiredComponentsFound2 = allRequiredComponentsFound3;
                    lidSwitchCondition = lidSwitchCondition2;
                    sensorConditions = sensorConditions2;
                    i2 = i;
                    state3 = state2;
                    sensorsToListenTo4 = sensorsToListenTo3;
                }
                com.android.server.policy.devicestate.config.KeyguardCondition keyguardCondition = conditions.getKeyguard();
                if (keyguardCondition != null) {
                    suppliers.add(new com.android.server.policy.DeviceStateProviderImpl.KeyguardBooleanSupplier(keyguardCondition.getShow()));
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "keguard required");
                    }
                }
                com.android.server.policy.devicestate.config.CameraCondition cameraCondition = conditions.getCamera();
                if (cameraCondition != null) {
                    suppliers.add(new com.android.server.policy.DeviceStateProviderImpl.CameraBooleanSupplier(cameraCondition.getOpen()));
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "camera required");
                    }
                }
                com.android.server.policy.devicestate.config.DisplayCondition displayCondition = conditions.getDisplay();
                if (displayCondition != null) {
                    suppliers.add(new com.android.server.policy.DeviceStateProviderImpl.DisplayBooleanSupplier(displayCondition.getDisplayOn(), displayCondition.getDisplayId()));
                }
                if (!allRequiredComponentsFound) {
                    sensorsToListenTo2 = sensorsToListenTo;
                    this.mStateConditions.put(state, FALSE_BOOLEAN_SUPPLIER);
                } else {
                    shouldListenToLidSwitch |= lidSwitchRequired;
                    sensorsToListenTo2 = sensorsToListenTo;
                    sensorsToListenTo2.addAll((android.util.ArraySet<? extends android.hardware.Sensor>) sensorsRequired);
                    if (suppliers.size() > 1) {
                        this.mStateConditions.put(state, new com.android.server.policy.DeviceStateProviderImpl.AndBooleanSupplier(suppliers));
                    } else {
                        int state4 = state;
                        if (suppliers.size() > 0) {
                            this.mStateConditions.put(state4, suppliers.get(0));
                        } else {
                            this.mStateConditions.put(state4, TRUE_BOOLEAN_SUPPLIER);
                        }
                    }
                }
            }
            i2 = i + 1;
            list = deviceStates;
            sensorsToListenTo4 = sensorsToListenTo2;
        }
        android.util.ArraySet<android.hardware.Sensor> sensorsToListenTo5 = sensorsToListenTo4;
        if (shouldListenToLidSwitch) {
            com.android.server.input.InputManagerInternal inputManager = (com.android.server.input.InputManagerInternal) com.android.server.LocalServices.getService(com.android.server.input.InputManagerInternal.class);
            inputManager.registerLidSwitchCallback(this);
        }
        sExtImpl.setNeededSensors(sensorsToListenTo5);
    }

    private android.hardware.Sensor findSensor(java.lang.String type, java.lang.String name) {
        android.hardware.SensorManager sensorManager = (android.hardware.SensorManager) this.mContext.getSystemService(android.hardware.SensorManager.class);
        java.util.List<android.hardware.Sensor> sensors = sensorManager.getSensorList(-1);
        for (int sensorIndex = 0; sensorIndex < sensors.size(); sensorIndex++) {
            android.hardware.Sensor sensor = sensors.get(sensorIndex);
            java.lang.String sensorType = sensor.getStringType();
            java.lang.String sensorName = sensor.getName();
            if (sensorType != null && sensorName != null && sensorType.equals(type) && sensorName.equals(name)) {
                if (this.mHingeSensor == null && sensorType.equals(TYPE_SENSOR_HINGE_DETECT)) {
                    this.mHingeSensor = sensor;
                } else if (this.mGSensor == null && sensorType.equals("android.sensor.gravity")) {
                    this.mGSensor = sensor;
                }
                return sensor;
            }
        }
        return null;
    }

    @Override // com.android.server.devicestate.DeviceStateProvider
    public void setListener(com.android.server.devicestate.DeviceStateProvider.Listener listener) {
        synchronized (this.mLock) {
            if (this.mListener != null) {
                throw new java.lang.RuntimeException("Provider already has a listener set.");
            }
            this.mListener = listener;
        }
        notifySupportedStatesChanged(1);
        notifyDeviceStateChangedIfNeeded();
    }

    @Override // com.android.server.devicestate.DeviceStateProvider
    public void notifyKeyguardShowOrSleep(boolean show) {
        synchronized (this.mLock) {
            this.mKeyguardShow = show;
        }
        android.util.Slog.d(TAG, "keyguard show or sleep: " + show + " mLastReportedState " + this.mLastReportedState);
        notifyDeviceStateChangedIfNeeded();
        android.hardware.SensorManager sensorManager = (android.hardware.SensorManager) this.mContext.getSystemService(android.hardware.SensorManager.class);
        if (show) {
            if (!sExtImpl.unregisterSensorsIfLockStateChanged(this.mLastReportedState, show)) {
                return;
            }
            if (this.mHingeSensor != null) {
                sensorManager.unregisterListener(this, this.mHingeSensor);
                synchronized (this.mLock) {
                    this.mLatestSensorEvent.put(this.mHingeSensor, null);
                }
            }
            if (this.mGSensor != null) {
                sensorManager.unregisterListener(this, this.mGSensor);
                synchronized (this.mLock) {
                    this.mLatestSensorEvent.put(this.mGSensor, null);
                }
            }
        } else {
            if (this.mHingeSensor != null) {
                sensorManager.registerListener(this, this.mHingeSensor, 0);
            }
            if (this.mGSensor != null) {
                sensorManager.registerListener(this, this.mGSensor, 3);
            }
        }
        initCameraCallbackIfNeeded();
    }

    private void initCameraCallbackIfNeeded() {
        if (this.mCameraManager == null) {
            this.mCameraManager = (android.hardware.camera2.CameraManager) this.mContext.getSystemService("camera");
            if (this.mCameraManager != null && this.mCameraAvailabilityCallback == null) {
                this.mCameraAvailabilityCallback = new com.android.server.policy.DeviceStateProviderImpl.CameraAvailabilityCallback();
                this.mCameraManager.registerAvailabilityCallback(this.mCameraAvailabilityCallback, new android.os.Handler(com.android.server.DisplayThread.get().getLooper()));
            }
        }
    }

    private void notifySupportedStatesChanged(int reason) {
        java.util.List<android.hardware.devicestate.DeviceState> supportedStates = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            if (this.mListener == null) {
                return;
            }
            com.android.server.devicestate.DeviceStateProvider.Listener listener = this.mListener;
            for (android.hardware.devicestate.DeviceState deviceState : this.mOrderedStates) {
                if ((!isThermalStatusCriticalOrAbove(this.mThermalStatus) || !deviceState.hasProperty(6)) && (!this.mPowerSaveModeEnabled || !deviceState.hasProperty(7))) {
                    supportedStates.add(deviceState);
                }
            }
            listener.onSupportedDeviceStatesChanged((android.hardware.devicestate.DeviceState[]) supportedStates.toArray(new android.hardware.devicestate.DeviceState[supportedStates.size()]), reason);
        }
    }

    void notifyDeviceStateChangedIfNeeded() {
        boolean conditionSatisfied;
        int stateToReport = -1;
        synchronized (this.mLock) {
            if (this.mListener == null) {
                return;
            }
            int newState = -1;
            int i = 0;
            while (true) {
                if (i >= this.mOrderedStates.length) {
                    break;
                }
                int state = this.mOrderedStates[i].getIdentifier();
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Checking conditions for " + this.mOrderedStates[i].getName() + "(" + i + ")");
                }
                try {
                    conditionSatisfied = this.mStateConditions.get(state).getAsBoolean();
                } catch (java.lang.IllegalStateException e) {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Unable to check current state", e);
                    }
                }
                if (!conditionSatisfied) {
                    i++;
                } else {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Device State conditions satisfied, transition to " + state);
                    }
                    if (state > 3) {
                        state = -1;
                    }
                    newState = state;
                }
            }
            if (newState == -1) {
                android.util.Slog.e(TAG, "No declared device states match any of the required conditions.");
                dumpSensorValues();
            }
            if (newState != -1 && newState != this.mLastReportedState) {
                if (sExtImpl.isNeedInterceptDeviceState(this.mLastReportedState, newState)) {
                    return;
                }
                this.mLastReportedState = newState;
                stateToReport = newState;
            }
            if (stateToReport != -1) {
                this.mListener.onStateChanged(stateToReport);
                synchronized (this.mLock) {
                    if (DEBUG) {
                        java.lang.StringBuilder builder = new java.lang.StringBuilder();
                        for (android.hardware.SensorEvent sensorEvent : this.mLatestSensorEvent.values()) {
                            if (sensorEvent != null) {
                                if (sensorEvent.sensor == this.mHingeSensor) {
                                    builder.append(" Hinge:").append(sensorEvent.values[0]);
                                } else if (sensorEvent.sensor == this.mGSensor) {
                                    builder.append(" Gravity:").append(sensorEvent.values[0]).append(" ").append(sensorEvent.values[1]).append(" ").append(sensorEvent.values[2]);
                                } else {
                                    builder.append(" Hall:").append(sensorEvent.values[0]);
                                }
                            }
                        }
                        builder.append(" KeyguardShow:").append(this.mKeyguardShow);
                        builder.append(" Camera:").append(this.mCameraOpen);
                        android.util.Slog.d(TAG, "StateChanged to " + stateToReport + " " + builder.toString());
                    }
                }
            }
        }
    }

    @Override // com.android.server.input.InputManagerInternal.LidSwitchCallback
    public void notifyLidSwitchChanged(long whenNanos, boolean lidOpen) {
        synchronized (this.mLock) {
            this.mIsLidOpen = java.lang.Boolean.valueOf(lidOpen);
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Lid switch state: " + (lidOpen ? "open" : "closed"));
        }
        notifyDeviceStateChangedIfNeeded();
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(android.hardware.SensorEvent event) {
        android.hardware.SensorEvent tmp = new android.hardware.SensorEvent(event.sensor, event.accuracy, event.timestamp, (float[]) event.values.clone());
        synchronized (this.mLock) {
            if (shouldProcessSensorChange(tmp)) {
                this.mLatestSensorEvent.put(tmp.sensor, tmp);
                if (event.sensor == this.mHingeSensor) {
                    android.util.Slog.i(TAG, "onSensorChanged Hinge value:" + event.values[0] + "  hingeAccuracy: " + event.values[1]);
                    if (!sExtImpl.isRemapDisabledDisplay()) {
                        android.util.Slog.i(TAG, "onSensorChanged subGravity values:" + event.values[5] + " " + event.values[6] + " " + event.values[7]);
                    }
                } else if (event.sensor == this.mGSensor) {
                    if (DEBUG) {
                        android.util.Slog.i(TAG, "onSensorChanged Gravity values:" + event.values[0] + " " + event.values[1] + " " + event.values[2]);
                    }
                } else {
                    android.util.Slog.i(TAG, "onSensorChanged Hall value:" + event.values[0]);
                }
                notifyDeviceStateChangedIfNeeded();
            }
        }
    }

    boolean shouldProcessSensorChange(android.hardware.SensorEvent event) {
        android.hardware.SensorEvent latestEvent = this.mLatestSensorEvent.get(event.sensor);
        if (latestEvent != null) {
            if (event.sensor != this.mHingeSensor) {
                if (event.sensor == this.mGSensor) {
                    float curX = event.values[0];
                    float curY = event.values[1];
                    float curZ = event.values[2];
                    float lastX = latestEvent.values[0];
                    float lastY = latestEvent.values[1];
                    float lastZ = latestEvent.values[2];
                    boolean availableX = true;
                    boolean availableY = true;
                    boolean availableZ = true;
                    if (java.lang.Math.abs(lastX - curX) < 0.3f) {
                        availableX = false;
                        event.values[0] = lastX;
                    }
                    if (java.lang.Math.abs(lastY - curY) < 0.3f) {
                        availableY = false;
                        event.values[1] = lastY;
                    }
                    if (java.lang.Math.abs(lastZ - curZ) < 0.3f) {
                        availableZ = false;
                        event.values[2] = lastZ;
                    }
                    return availableX || availableY || availableZ;
                }
                return true;
            }
            boolean isShouldProcessChange = false;
            if (java.lang.Math.abs(event.values[0] - latestEvent.values[0]) >= 5.0f) {
                isShouldProcessChange = true;
            }
            if (!sExtImpl.isRemapDisabledDisplay()) {
                float curX2 = event.values[5];
                float curY2 = event.values[6];
                float curZ2 = event.values[7];
                float lastX2 = latestEvent.values[5];
                float lastY2 = latestEvent.values[6];
                float lastZ2 = latestEvent.values[7];
                boolean availableX2 = true;
                boolean availableY2 = true;
                boolean availableZ2 = true;
                if (java.lang.Math.abs(lastX2 - curX2) < 0.3f) {
                    availableX2 = false;
                    event.values[5] = lastX2;
                }
                if (java.lang.Math.abs(lastY2 - curY2) < 0.3f) {
                    availableY2 = false;
                    event.values[6] = lastY2;
                }
                if (java.lang.Math.abs(lastZ2 - curZ2) < 0.3f) {
                    availableZ2 = false;
                    event.values[7] = lastZ2;
                }
                boolean isShouldProcessChange2 = isShouldProcessChange || availableX2 || availableY2 || availableZ2;
                return isShouldProcessChange2;
            }
            return isShouldProcessChange;
        }
        return true;
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
    }

    @Override // android.util.Dumpable
    public void dump(java.io.PrintWriter writer, java.lang.String[] args) {
        writer.println(TAG);
        synchronized (this.mLock) {
            writer.println("  mLastReportedState = " + this.mLastReportedState);
            writer.println("  mPowerSaveModeEnabled = " + this.mPowerSaveModeEnabled);
            writer.println("  mThermalStatus = " + this.mThermalStatus);
            writer.println("  mIsLidOpen = " + this.mIsLidOpen);
            writer.println("  Sensor values:");
            for (android.hardware.Sensor sensor : this.mLatestSensorEvent.keySet()) {
                android.hardware.SensorEvent sensorEvent = this.mLatestSensorEvent.get(sensor);
                writer.println("   - " + toSensorValueString(sensor, sensorEvent));
            }
        }
    }

    private final class LidSwitchBooleanSupplier implements java.util.function.BooleanSupplier {
        private final boolean mExpectedOpen;

        LidSwitchBooleanSupplier(boolean expectedOpen) {
            this.mExpectedOpen = expectedOpen;
        }

        @Override // java.util.function.BooleanSupplier
        public boolean getAsBoolean() {
            boolean z;
            synchronized (com.android.server.policy.DeviceStateProviderImpl.this.mLock) {
                if (com.android.server.policy.DeviceStateProviderImpl.this.mIsLidOpen == null) {
                    throw new java.lang.IllegalStateException("Have not received lid switch value.");
                }
                z = com.android.server.policy.DeviceStateProviderImpl.this.mIsLidOpen.booleanValue() == this.mExpectedOpen;
            }
            return z;
        }
    }

    private final class KeyguardBooleanSupplier implements java.util.function.BooleanSupplier {
        private final boolean mShow;

        KeyguardBooleanSupplier(boolean expectedShow) {
            this.mShow = expectedShow;
        }

        @Override // java.util.function.BooleanSupplier
        public boolean getAsBoolean() {
            boolean z;
            synchronized (com.android.server.policy.DeviceStateProviderImpl.this.mLock) {
                android.util.Slog.d(com.android.server.policy.DeviceStateProviderImpl.TAG, "value: " + com.android.server.policy.DeviceStateProviderImpl.this.mKeyguardShow + ", constraint KeyguardShow: " + this.mShow);
                z = this.mShow == com.android.server.policy.DeviceStateProviderImpl.this.mKeyguardShow;
            }
            return z;
        }
    }

    private final class CameraBooleanSupplier implements java.util.function.BooleanSupplier {
        private final boolean mOpen;

        CameraBooleanSupplier(boolean expectedOpen) {
            this.mOpen = expectedOpen;
        }

        @Override // java.util.function.BooleanSupplier
        public boolean getAsBoolean() {
            synchronized (com.android.server.policy.DeviceStateProviderImpl.this.mLock) {
                boolean z = true;
                if (com.android.server.policy.DeviceStateProviderImpl.this.mLastReportedState == 1) {
                    return true;
                }
                if (this.mOpen != com.android.server.policy.DeviceStateProviderImpl.this.mCameraOpen) {
                    z = false;
                }
                return z;
            }
        }
    }

    private final class CameraAvailabilityCallback extends android.hardware.camera2.CameraManager.AvailabilityCallback {
        private java.lang.String mCameraOwner;

        private CameraAvailabilityCallback() {
            this.mCameraOwner = null;
        }

        public void onCameraOpened(java.lang.String cameraId, java.lang.String packageId) {
            super.onCameraOpened(cameraId, packageId);
            this.mCameraOwner = packageId;
            if (this.mCameraOwner != null && this.mCameraOwner.equals(com.android.server.policy.DeviceStateProviderImpl.this.mSystemCameraName)) {
                synchronized (com.android.server.policy.DeviceStateProviderImpl.this.mLock) {
                    com.android.server.policy.DeviceStateProviderImpl.this.mCameraOpen = true;
                }
            }
            com.android.server.policy.DeviceStateProviderImpl.this.notifyDeviceStateChangedIfNeeded();
        }

        public void onCameraClosed(java.lang.String cameraId) {
            super.onCameraClosed(cameraId);
            synchronized (com.android.server.policy.DeviceStateProviderImpl.this.mLock) {
                com.android.server.policy.DeviceStateProviderImpl.this.mCameraOpen = false;
            }
            this.mCameraOwner = null;
            com.android.server.policy.DeviceStateProviderImpl.this.notifyDeviceStateChangedIfNeeded();
        }
    }

    private final class DisplayBooleanSupplier implements java.util.function.BooleanSupplier {
        private final int mDisplayId;
        private final boolean mDisplayOn;

        DisplayBooleanSupplier(boolean expectedValue, java.math.BigInteger displayId) {
            this.mDisplayOn = expectedValue;
            this.mDisplayId = displayId.intValue();
        }

        @Override // java.util.function.BooleanSupplier
        public boolean getAsBoolean() {
            boolean z;
            synchronized (com.android.server.policy.DeviceStateProviderImpl.this.mLock) {
                android.util.Slog.d(com.android.server.policy.DeviceStateProviderImpl.TAG, "value: " + com.android.server.policy.DeviceStateProviderImpl.sExtImpl.getDisplayOn(this.mDisplayId) + ", constraint DisplayOn: " + this.mDisplayOn);
                z = this.mDisplayOn == com.android.server.policy.DeviceStateProviderImpl.sExtImpl.getDisplayOn(this.mDisplayId);
            }
            return z;
        }
    }

    private final class SensorBooleanSupplier implements java.util.function.BooleanSupplier {
        private final java.util.List<com.android.server.policy.devicestate.config.NumericRange> mExpectedValues;
        private final android.hardware.Sensor mSensor;
        private boolean mUnregister;

        SensorBooleanSupplier(android.hardware.Sensor sensor, java.util.List<com.android.server.policy.devicestate.config.NumericRange> expectedValues, boolean unregister) {
            this.mSensor = sensor;
            this.mExpectedValues = expectedValues;
            this.mUnregister = unregister;
        }

        @Override // java.util.function.BooleanSupplier
        public boolean getAsBoolean() {
            synchronized (com.android.server.policy.DeviceStateProviderImpl.this.mLock) {
                android.hardware.SensorEvent latestEvent = (android.hardware.SensorEvent) com.android.server.policy.DeviceStateProviderImpl.this.mLatestSensorEvent.get(this.mSensor);
                if (latestEvent == null) {
                    android.util.Slog.d(com.android.server.policy.DeviceStateProviderImpl.TAG, "no event for sensor: " + this.mSensor + "=mUnregister=" + this.mUnregister);
                    return this.mUnregister;
                }
                if (latestEvent.values.length < this.mExpectedValues.size()) {
                    throw new java.lang.RuntimeException("Number of supplied numeric range(s) does not match the number of values in the latest sensor event for sensor: " + this.mSensor);
                }
                for (int i = 0; i < this.mExpectedValues.size(); i++) {
                    if (!adheresToRange(latestEvent.values[i], this.mExpectedValues.get(i))) {
                        return false;
                    }
                }
                return true;
            }
        }

        private boolean adheresToRange(float value, com.android.server.policy.devicestate.config.NumericRange range) {
            java.math.BigDecimal min = range.getMin_optional();
            if (min != null) {
                if (com.android.server.policy.DeviceStateProviderImpl.DEBUG) {
                    android.util.Slog.d(com.android.server.policy.DeviceStateProviderImpl.TAG, "value: " + value + ", constraint min: " + min.floatValue());
                }
                if (value <= min.floatValue()) {
                    return false;
                }
            }
            java.math.BigDecimal minInclusive = range.getMinInclusive_optional();
            if (minInclusive != null) {
                if (com.android.server.policy.DeviceStateProviderImpl.DEBUG) {
                    android.util.Slog.d(com.android.server.policy.DeviceStateProviderImpl.TAG, "value: " + value + ", constraint min-inclusive: " + minInclusive.floatValue());
                }
                if (value < minInclusive.floatValue()) {
                    return false;
                }
            }
            java.math.BigDecimal max = range.getMax_optional();
            if (max != null) {
                if (com.android.server.policy.DeviceStateProviderImpl.DEBUG) {
                    android.util.Slog.d(com.android.server.policy.DeviceStateProviderImpl.TAG, "value: " + value + ", constraint max: " + max.floatValue());
                }
                if (value >= max.floatValue()) {
                    return false;
                }
            }
            java.math.BigDecimal maxInclusive = range.getMaxInclusive_optional();
            if (maxInclusive != null) {
                if (com.android.server.policy.DeviceStateProviderImpl.DEBUG) {
                    android.util.Slog.d(com.android.server.policy.DeviceStateProviderImpl.TAG, "value: " + value + ", constraint max-inclusive: " + maxInclusive.floatValue());
                }
                if (value > maxInclusive.floatValue()) {
                    return false;
                }
                return true;
            }
            return true;
        }
    }

    private final class SubGravitySensorBooleanSupplier implements java.util.function.BooleanSupplier {
        private final android.hardware.Sensor mSensor;
        private boolean mUnregister;
        private final java.util.List<com.android.server.policy.devicestate.config.NumericRange> mExpectedValues = new java.util.ArrayList();
        private final int VALUE_START_SUBGRAVITY = 5;
        private final java.math.BigDecimal zero = new java.math.BigDecimal("0");

        SubGravitySensorBooleanSupplier(android.hardware.Sensor sensor, java.util.List<com.android.server.policy.devicestate.config.NumericRange> expectedValues, boolean unregister) {
            this.mSensor = com.android.server.policy.DeviceStateProviderImpl.this.mHingeSensor;
            translateToSubGravityValue(expectedValues);
            this.mUnregister = unregister;
        }

        private void translateToSubGravityValue(java.util.List<com.android.server.policy.devicestate.config.NumericRange> expectedValues) {
            com.android.server.policy.devicestate.config.NumericRange range = expectedValues.get(0);
            java.math.BigDecimal min = range.getMin_optional();
            java.math.BigDecimal max = range.getMax_optional();
            com.android.server.policy.devicestate.config.NumericRange subrange = new com.android.server.policy.devicestate.config.NumericRange();
            subrange.setMin_optional(this.zero.subtract(max));
            subrange.setMax_optional(this.zero.subtract(min));
            this.mExpectedValues.add(subrange);
            for (int i = 1; i < expectedValues.size(); i++) {
                this.mExpectedValues.add(expectedValues.get(i));
            }
        }

        @Override // java.util.function.BooleanSupplier
        public boolean getAsBoolean() {
            synchronized (com.android.server.policy.DeviceStateProviderImpl.this.mLock) {
                android.hardware.SensorEvent latestEvent = (android.hardware.SensorEvent) com.android.server.policy.DeviceStateProviderImpl.this.mLatestSensorEvent.get(this.mSensor);
                if (latestEvent == null) {
                    android.util.Slog.d(com.android.server.policy.DeviceStateProviderImpl.TAG, "no event for sensor: " + this.mSensor + "=mUnregister=" + this.mUnregister);
                    return this.mUnregister;
                }
                if (latestEvent.values.length < this.mExpectedValues.size()) {
                    throw new java.lang.RuntimeException("Number of supplied numeric range(s) does not match the number of values in the latest sensor event for sensor: " + this.mSensor);
                }
                for (int i = 0; i < this.mExpectedValues.size(); i++) {
                    if (!adheresToRange(latestEvent.values[i + 5], this.mExpectedValues.get(i))) {
                        return false;
                    }
                }
                return true;
            }
        }

        private boolean adheresToRange(float value, com.android.server.policy.devicestate.config.NumericRange range) {
            java.math.BigDecimal min = range.getMin_optional();
            if (min != null) {
                if (com.android.server.policy.DeviceStateProviderImpl.DEBUG) {
                    android.util.Slog.d(com.android.server.policy.DeviceStateProviderImpl.TAG, "value: " + value + ", constraint min: " + min.floatValue());
                }
                if (value <= min.floatValue()) {
                    return false;
                }
            }
            java.math.BigDecimal minInclusive = range.getMinInclusive_optional();
            if (minInclusive != null) {
                if (com.android.server.policy.DeviceStateProviderImpl.DEBUG) {
                    android.util.Slog.d(com.android.server.policy.DeviceStateProviderImpl.TAG, "value: " + value + ", constraint min-inclusive: " + minInclusive.floatValue());
                }
                if (value < minInclusive.floatValue()) {
                    return false;
                }
            }
            java.math.BigDecimal max = range.getMax_optional();
            if (max != null) {
                if (com.android.server.policy.DeviceStateProviderImpl.DEBUG) {
                    android.util.Slog.d(com.android.server.policy.DeviceStateProviderImpl.TAG, "value: " + value + ", constraint max: " + max.floatValue());
                }
                if (value >= max.floatValue()) {
                    return false;
                }
            }
            java.math.BigDecimal maxInclusive = range.getMaxInclusive_optional();
            if (maxInclusive != null) {
                if (com.android.server.policy.DeviceStateProviderImpl.DEBUG) {
                    android.util.Slog.d(com.android.server.policy.DeviceStateProviderImpl.TAG, "value: " + value + ", constraint max-inclusive: " + maxInclusive.floatValue());
                }
                if (value > maxInclusive.floatValue()) {
                    return false;
                }
                return true;
            }
            return true;
        }
    }

    private static final class AndBooleanSupplier implements java.util.function.BooleanSupplier {
        java.util.List<java.util.function.BooleanSupplier> mBooleanSuppliers;

        AndBooleanSupplier(java.util.List<java.util.function.BooleanSupplier> booleanSuppliers) {
            this.mBooleanSuppliers = booleanSuppliers;
        }

        @Override // java.util.function.BooleanSupplier
        public boolean getAsBoolean() {
            for (int i = 0; i < this.mBooleanSuppliers.size(); i++) {
                if (!this.mBooleanSuppliers.get(i).getAsBoolean()) {
                    return false;
                }
            }
            return true;
        }
    }

    private static java.io.File getConfigurationFile() {
        java.io.File configFileFromOdmDir = android.os.Environment.buildPath(android.os.Environment.getOdmDirectory(), new java.lang.String[]{"etc/devicestate/", ODM_CONFIG_FILE_NAME});
        if (!configFileFromOdmDir.exists()) {
            configFileFromOdmDir = android.os.Environment.buildPath(android.os.Environment.getOdmDirectory(), new java.lang.String[]{"etc/devicestate/", CONFIG_FILE_NAME});
        }
        if (configFileFromOdmDir.exists()) {
            android.util.Slog.d(TAG, "configFileFromOdmDir :" + configFileFromOdmDir.toString() + " is exists");
            return configFileFromOdmDir;
        }
        java.io.File configFileFromDataDir = android.os.Environment.buildPath(android.os.Environment.getDataDirectory(), new java.lang.String[]{DATA_CONFIG_FILE_PATH, CONFIG_FILE_NAME});
        if (configFileFromDataDir.exists()) {
            return configFileFromDataDir;
        }
        java.io.File configFileFromVendorDir = android.os.Environment.buildPath(android.os.Environment.getVendorDirectory(), new java.lang.String[]{"etc/devicestate/", CONFIG_FILE_NAME});
        if (configFileFromVendorDir.exists()) {
            return configFileFromVendorDir;
        }
        return null;
    }

    private void dumpSensorValues() {
        android.util.Slog.i(TAG, "Sensor values:");
        for (android.hardware.Sensor sensor : this.mLatestSensorEvent.keySet()) {
            android.hardware.SensorEvent sensorEvent = this.mLatestSensorEvent.get(sensor);
            android.util.Slog.i(TAG, toSensorValueString(sensor, sensorEvent));
        }
    }

    private java.lang.String toSensorValueString(android.hardware.Sensor sensor, android.hardware.SensorEvent event) {
        java.lang.String sensorString = sensor == null ? "null" : sensor.getName();
        java.lang.String eventValues = event != null ? java.util.Arrays.toString(event.values) : "null";
        return sensorString + " : " + eventValues;
    }

    private static com.android.server.policy.devicestate.config.DeviceStateConfig parseConfig(com.android.server.policy.DeviceStateProviderImpl.ReadableConfig readableConfig) {
        try {
            java.io.InputStream in = readableConfig.openRead();
            try {
                java.io.InputStream bin = new java.io.BufferedInputStream(in);
                try {
                    com.android.server.policy.devicestate.config.DeviceStateConfig deviceStateConfig = com.android.server.policy.devicestate.config.XmlParser.read(bin);
                    bin.close();
                    if (in != null) {
                        in.close();
                    }
                    return deviceStateConfig;
                } finally {
                }
            } catch (java.lang.Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (java.io.IOException | javax.xml.datatype.DatatypeConfigurationException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.e(TAG, "Encountered an error while reading device state config", e);
            return null;
        }
    }

    private static final class ReadableFileConfig implements com.android.server.policy.DeviceStateProviderImpl.ReadableConfig {
        private final java.io.File mFile;

        private ReadableFileConfig(java.io.File file) {
            this.mFile = file;
        }

        @Override // com.android.server.policy.DeviceStateProviderImpl.ReadableConfig
        public java.io.InputStream openRead() throws java.io.IOException {
            return new java.io.FileInputStream(this.mFile);
        }
    }

    public com.android.server.policy.IDeviceStateProviderImplWrapper getWrapper() {
        return this.mDsplWrapper;
    }

    private class DeviceStateProviderImplWrapper implements com.android.server.policy.IDeviceStateProviderImplWrapper {
        private DeviceStateProviderImplWrapper() {
        }

        @Override // com.android.server.policy.IDeviceStateProviderImplWrapper
        public void notifyDeviceStateChangedIfNeeded() {
            com.android.server.policy.DeviceStateProviderImpl.this.notifyDeviceStateChangedIfNeeded();
        }

        @Override // com.android.server.policy.IDeviceStateProviderImplWrapper
        public java.lang.Object getLock() {
            return com.android.server.policy.DeviceStateProviderImpl.this.mLock;
        }
    }

    void onPowerSaveModeChanged(boolean isPowerSaveModeEnabled) {
        synchronized (this.mLock) {
            if (this.mPowerSaveModeEnabled != isPowerSaveModeEnabled) {
                this.mPowerSaveModeEnabled = isPowerSaveModeEnabled;
                notifySupportedStatesChanged(isPowerSaveModeEnabled ? 4 : 5);
            }
        }
    }

    @Override // android.os.PowerManager.OnThermalStatusChangedListener
    public void onThermalStatusChanged(int thermalStatus) {
        int previousThermalStatus;
        int i;
        synchronized (this.mLock) {
            previousThermalStatus = this.mThermalStatus;
            this.mThermalStatus = thermalStatus;
        }
        boolean isThermalStatusCriticalOrAbove = isThermalStatusCriticalOrAbove(thermalStatus);
        boolean isPreviousThermalStatusCriticalOrAbove = isThermalStatusCriticalOrAbove(previousThermalStatus);
        if (isThermalStatusCriticalOrAbove != isPreviousThermalStatusCriticalOrAbove) {
            android.util.Slog.i(TAG, "Updating supported device states due to thermal status change. isThermalStatusCriticalOrAbove: " + isThermalStatusCriticalOrAbove);
            if (isThermalStatusCriticalOrAbove) {
                i = 3;
            } else {
                i = 2;
            }
            notifySupportedStatesChanged(i);
        }
    }

    private static boolean isThermalStatusCriticalOrAbove(int thermalStatus) {
        switch (thermalStatus) {
            case 4:
            case 5:
            case 6:
                return true;
            default:
                return false;
        }
    }

    private static boolean hasThermalSensitiveState(java.util.List<android.hardware.devicestate.DeviceState> deviceStates) {
        for (int i = 0; i < deviceStates.size(); i++) {
            if (deviceStates.get(i).hasProperty(6)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasPowerSaveSensitiveState(java.util.List<android.hardware.devicestate.DeviceState> deviceStates) {
        for (int i = 0; i < deviceStates.size(); i++) {
            if (deviceStates.get(i).hasProperty(7)) {
                return true;
            }
        }
        return false;
    }
}
