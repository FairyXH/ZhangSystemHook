package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
@java.lang.Deprecated
class BrightnessThrottler {
    private static final int THROTTLING_INVALID = -1;
    private float mBrightnessCap;
    private int mBrightnessMaxReason;
    private final com.android.server.display.feature.DeviceConfigParameterProvider mConfigParameterProvider;
    private final java.util.function.BiFunction<java.lang.String, java.lang.String, com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel> mDataPointMapper;
    private final java.util.function.Function<java.util.List<com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel>, com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData> mDataSetMapper;
    private java.util.Map<java.lang.String, com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData> mDdcThermalThrottlingDataMap;
    private final android.os.Handler mDeviceConfigHandler;
    private final com.android.server.display.BrightnessThrottler.DeviceConfigListener mDeviceConfigListener;
    private final android.os.Handler mHandler;
    private final com.android.server.display.BrightnessThrottler.Injector mInjector;
    private final com.android.server.display.BrightnessThrottler.SkinThermalStatusObserver mSkinThermalStatusObserver;
    private com.android.server.display.config.SensorData mTempSensor;
    private java.lang.String mThermalBrightnessThrottlingDataId;
    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData>> mThermalBrightnessThrottlingDataOverride;
    private java.lang.String mThermalBrightnessThrottlingDataString;
    private com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData mThermalThrottlingData;
    private final java.lang.Runnable mThrottlingChangeCallback;
    private int mThrottlingStatus;
    private java.lang.String mUniqueDisplayId;
    private static final java.lang.String TAG = "BrightnessThrottler";
    private static final boolean DEBUG = com.android.server.display.utils.DebugUtils.isDebuggable(TAG);

    static /* synthetic */ com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel lambda$new$0(java.lang.String key, java.lang.String value) {
        try {
            int status = com.android.server.display.utils.DeviceConfigParsingUtils.parseThermalStatus(key);
            float brightnessPoint = com.android.server.display.utils.DeviceConfigParsingUtils.parseBrightness(value);
            return new com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel(status, brightnessPoint);
        } catch (java.lang.IllegalArgumentException e) {
            return null;
        }
    }

    BrightnessThrottler(android.os.Handler handler, java.lang.Runnable throttlingChangeCallback, java.lang.String uniqueDisplayId, java.lang.String throttlingDataId, com.android.server.display.DisplayDeviceConfig displayDeviceConfig) {
        this(new com.android.server.display.BrightnessThrottler.Injector(), handler, handler, throttlingChangeCallback, uniqueDisplayId, throttlingDataId, displayDeviceConfig.getThermalBrightnessThrottlingDataMapByThrottlingId(), displayDeviceConfig.getTempSensor());
    }

    BrightnessThrottler(com.android.server.display.BrightnessThrottler.Injector injector, android.os.Handler handler, android.os.Handler deviceConfigHandler, java.lang.Runnable throttlingChangeCallback, java.lang.String uniqueDisplayId, java.lang.String throttlingDataId, java.util.Map<java.lang.String, com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData> thermalBrightnessThrottlingDataMap, com.android.server.display.config.SensorData tempSensor) {
        this.mBrightnessCap = 1.0f;
        this.mBrightnessMaxReason = 0;
        this.mThermalBrightnessThrottlingDataOverride = new java.util.HashMap();
        this.mDataPointMapper = new java.util.function.BiFunction() { // from class: com.android.server.display.BrightnessThrottler$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.display.BrightnessThrottler.lambda$new$0((java.lang.String) obj, (java.lang.String) obj2);
            }
        };
        this.mDataSetMapper = new com.android.server.display.BrightnessThrottler$$ExternalSyntheticLambda2();
        this.mInjector = injector;
        this.mHandler = handler;
        this.mDeviceConfigHandler = deviceConfigHandler;
        this.mDdcThermalThrottlingDataMap = thermalBrightnessThrottlingDataMap;
        this.mThrottlingChangeCallback = throttlingChangeCallback;
        this.mSkinThermalStatusObserver = new com.android.server.display.BrightnessThrottler.SkinThermalStatusObserver(this.mInjector, this.mHandler);
        this.mUniqueDisplayId = uniqueDisplayId;
        this.mConfigParameterProvider = new com.android.server.display.feature.DeviceConfigParameterProvider(injector.getDeviceConfig());
        this.mDeviceConfigListener = new com.android.server.display.BrightnessThrottler.DeviceConfigListener();
        this.mThermalBrightnessThrottlingDataId = throttlingDataId;
        this.mDdcThermalThrottlingDataMap = thermalBrightnessThrottlingDataMap;
        loadThermalBrightnessThrottlingDataFromDeviceConfig();
        loadThermalBrightnessThrottlingDataFromDisplayDeviceConfig(this.mDdcThermalThrottlingDataMap, tempSensor, this.mThermalBrightnessThrottlingDataId, this.mUniqueDisplayId);
    }

    boolean deviceSupportsThrottling() {
        return this.mThermalThrottlingData != null;
    }

    float getBrightnessCap() {
        return this.mBrightnessCap;
    }

    int getBrightnessMaxReason() {
        return this.mBrightnessMaxReason;
    }

    boolean isThrottled() {
        return this.mBrightnessMaxReason != 0;
    }

    void stop() {
        this.mSkinThermalStatusObserver.stopObserving();
        this.mConfigParameterProvider.removeOnPropertiesChangedListener(this.mDeviceConfigListener);
        this.mBrightnessCap = 1.0f;
        this.mBrightnessMaxReason = 0;
        this.mThrottlingStatus = -1;
    }

    void loadThermalBrightnessThrottlingDataFromDisplayDeviceConfig(java.util.Map<java.lang.String, com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData> ddcThrottlingDataMap, com.android.server.display.config.SensorData tempSensor, java.lang.String brightnessThrottlingDataId, java.lang.String uniqueDisplayId) {
        this.mDdcThermalThrottlingDataMap = ddcThrottlingDataMap;
        this.mThermalBrightnessThrottlingDataId = brightnessThrottlingDataId;
        this.mUniqueDisplayId = uniqueDisplayId;
        this.mTempSensor = tempSensor;
        resetThermalThrottlingData();
    }

    private float verifyAndConstrainBrightnessCap(float brightness) {
        if (brightness < 0.0f) {
            android.util.Slog.e(TAG, "brightness " + brightness + " is lower than the minimum possible brightness 0.0");
            brightness = 0.0f;
        }
        if (brightness > 1.0f) {
            android.util.Slog.e(TAG, "brightness " + brightness + " is higher than the maximum possible brightness 1.0");
            return 1.0f;
        }
        return brightness;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void thermalStatusChanged(int newStatus) {
        if (this.mThrottlingStatus != newStatus) {
            this.mThrottlingStatus = newStatus;
            updateThermalThrottling();
        }
    }

    private void updateThermalThrottling() {
        if (!deviceSupportsThrottling()) {
            return;
        }
        float brightnessCap = 1.0f;
        int brightnessMaxReason = 0;
        if (this.mThrottlingStatus != -1 && this.mThermalThrottlingData != null) {
            for (com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel level : this.mThermalThrottlingData.throttlingLevels) {
                if (level.thermalStatus > this.mThrottlingStatus) {
                    break;
                }
                brightnessCap = level.brightness;
                brightnessMaxReason = 1;
            }
        }
        if (this.mBrightnessCap != brightnessCap || this.mBrightnessMaxReason != brightnessMaxReason) {
            this.mBrightnessCap = verifyAndConstrainBrightnessCap(brightnessCap);
            this.mBrightnessMaxReason = brightnessMaxReason;
            if (DEBUG) {
                android.util.Slog.d(TAG, "State changed: mBrightnessCap = " + this.mBrightnessCap + ", mBrightnessMaxReason = " + android.hardware.display.BrightnessInfo.briMaxReasonToString(this.mBrightnessMaxReason));
            }
            if (this.mThrottlingChangeCallback != null) {
                this.mThrottlingChangeCallback.run();
            }
        }
    }

    void dump(final java.io.PrintWriter pw) {
        this.mHandler.runWithScissors(new java.lang.Runnable() { // from class: com.android.server.display.BrightnessThrottler$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dump$1(pw);
            }
        }, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: dumpLocal, reason: merged with bridge method [inline-methods] */
    public void lambda$dump$1(java.io.PrintWriter pw) {
        pw.println("BrightnessThrottler:");
        pw.println("  mThermalBrightnessThrottlingDataId=" + this.mThermalBrightnessThrottlingDataId);
        pw.println("  mThermalThrottlingData=" + this.mThermalThrottlingData);
        pw.println("  mUniqueDisplayId=" + this.mUniqueDisplayId);
        pw.println("  mThrottlingStatus=" + this.mThrottlingStatus);
        pw.println("  mBrightnessCap=" + this.mBrightnessCap);
        pw.println("  mBrightnessMaxReason=" + android.hardware.display.BrightnessInfo.briMaxReasonToString(this.mBrightnessMaxReason));
        pw.println("  mDdcThermalThrottlingDataMap=" + this.mDdcThermalThrottlingDataMap);
        pw.println("  mThermalBrightnessThrottlingDataOverride=" + this.mThermalBrightnessThrottlingDataOverride);
        pw.println("  mThermalBrightnessThrottlingDataString=" + this.mThermalBrightnessThrottlingDataString);
        this.mSkinThermalStatusObserver.dump(pw);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadThermalBrightnessThrottlingDataFromDeviceConfig() {
        this.mThermalBrightnessThrottlingDataString = this.mConfigParameterProvider.getBrightnessThrottlingData();
        this.mThermalBrightnessThrottlingDataOverride.clear();
        if (this.mThermalBrightnessThrottlingDataString != null) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData>> tempThrottlingData = com.android.server.display.utils.DeviceConfigParsingUtils.parseDeviceConfigMap(this.mThermalBrightnessThrottlingDataString, this.mDataPointMapper, this.mDataSetMapper);
            this.mThermalBrightnessThrottlingDataOverride.putAll(tempThrottlingData);
        } else {
            android.util.Slog.w(TAG, "DeviceConfig ThermalBrightnessThrottlingData is null");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetThermalThrottlingData() {
        stop();
        this.mDeviceConfigListener.startListening();
        this.mThermalThrottlingData = getConfigFromId(this.mThermalBrightnessThrottlingDataId);
        if (!"default".equals(this.mThermalBrightnessThrottlingDataId) && this.mThermalThrottlingData == null) {
            this.mThermalThrottlingData = getConfigFromId("default");
            android.util.Slog.d(TAG, "Falling back to default throttling Id");
        }
        if (deviceSupportsThrottling()) {
            this.mSkinThermalStatusObserver.startObserving(this.mTempSensor);
        }
    }

    private com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData getConfigFromId(java.lang.String id) {
        com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData returnValue;
        com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData thermalBrightnessThrottlingData;
        if (this.mThermalBrightnessThrottlingDataOverride.get(this.mUniqueDisplayId) == null) {
            returnValue = null;
        } else {
            returnValue = this.mThermalBrightnessThrottlingDataOverride.get(this.mUniqueDisplayId).get(id);
        }
        if (returnValue == null) {
            thermalBrightnessThrottlingData = this.mDdcThermalThrottlingDataMap.get(id);
        } else {
            thermalBrightnessThrottlingData = returnValue;
        }
        com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData returnValue2 = thermalBrightnessThrottlingData;
        return returnValue2;
    }

    public class DeviceConfigListener implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        public java.util.concurrent.Executor mExecutor;

        public DeviceConfigListener() {
            this.mExecutor = new android.os.HandlerExecutor(com.android.server.display.BrightnessThrottler.this.mDeviceConfigHandler);
        }

        public void startListening() {
            com.android.server.display.BrightnessThrottler.this.mConfigParameterProvider.addOnPropertiesChangedListener(this.mExecutor, this);
        }

        public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
            com.android.server.display.BrightnessThrottler.this.loadThermalBrightnessThrottlingDataFromDeviceConfig();
            com.android.server.display.BrightnessThrottler.this.resetThermalThrottlingData();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class SkinThermalStatusObserver extends android.os.IThermalEventListener.Stub {
        private final android.os.Handler mHandler;
        private final com.android.server.display.BrightnessThrottler.Injector mInjector;
        private com.android.server.display.config.SensorData mObserverTempSensor;
        private boolean mStarted;
        private android.os.IThermalService mThermalService;

        SkinThermalStatusObserver(com.android.server.display.BrightnessThrottler.Injector injector, android.os.Handler handler) {
            this.mInjector = injector;
            this.mHandler = handler;
        }

        public void notifyThrottling(final android.os.Temperature temp) {
            if (com.android.server.display.BrightnessThrottler.DEBUG) {
                android.util.Slog.d(com.android.server.display.BrightnessThrottler.TAG, "New thermal throttling status = " + temp.getStatus());
            }
            if (this.mObserverTempSensor.name != null && !this.mObserverTempSensor.name.equals(temp.getName())) {
                android.util.Slog.i(com.android.server.display.BrightnessThrottler.TAG, "Skipping thermal throttling notification as monitored sensor: " + this.mObserverTempSensor.name + " != notified sensor: " + temp.getName());
            } else {
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.BrightnessThrottler$SkinThermalStatusObserver$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$notifyThrottling$0(temp);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyThrottling$0(android.os.Temperature temp) {
            int status = temp.getStatus();
            com.android.server.display.BrightnessThrottler.this.thermalStatusChanged(status);
        }

        void startObserving(com.android.server.display.config.SensorData tempSensor) {
            if (!this.mStarted || this.mObserverTempSensor == null) {
                this.mObserverTempSensor = tempSensor;
                registerThermalListener();
                return;
            }
            java.lang.String curType = this.mObserverTempSensor.type;
            this.mObserverTempSensor = tempSensor;
            if (curType.equals(tempSensor.type)) {
                if (com.android.server.display.BrightnessThrottler.DEBUG) {
                    android.util.Slog.d(com.android.server.display.BrightnessThrottler.TAG, "Thermal status observer already started");
                }
            } else {
                stopObserving();
                registerThermalListener();
            }
        }

        void registerThermalListener() {
            this.mThermalService = this.mInjector.getThermalService();
            if (this.mThermalService == null) {
                android.util.Slog.e(com.android.server.display.BrightnessThrottler.TAG, "Could not observe thermal status. Service not available");
                return;
            }
            int temperatureType = com.android.server.display.utils.SensorUtils.getSensorTemperatureType(this.mObserverTempSensor);
            try {
                this.mThermalService.registerThermalEventListenerWithType(this, temperatureType);
                this.mStarted = true;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.display.BrightnessThrottler.TAG, "Failed to register thermal status listener", e);
            }
        }

        void stopObserving() {
            if (!this.mStarted) {
                if (com.android.server.display.BrightnessThrottler.DEBUG) {
                    android.util.Slog.d(com.android.server.display.BrightnessThrottler.TAG, "Stop skipped because thermal status observer not started");
                }
            } else {
                try {
                    this.mThermalService.unregisterThermalEventListener(this);
                    this.mStarted = false;
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.display.BrightnessThrottler.TAG, "Failed to unregister thermal status listener", e);
                }
                this.mThermalService = null;
            }
        }

        void dump(java.io.PrintWriter writer) {
            writer.println("  SkinThermalStatusObserver:");
            writer.println("    mStarted: " + this.mStarted);
            writer.println("    mObserverTempSensor: " + this.mObserverTempSensor);
            if (this.mThermalService != null) {
                writer.println("    ThermalService available");
            } else {
                writer.println("    ThermalService not available");
            }
        }
    }

    public static class Injector {
        public android.os.IThermalService getThermalService() {
            return android.os.IThermalService.Stub.asInterface(android.os.ServiceManager.getService("thermalservice"));
        }

        public android.provider.DeviceConfigInterface getDeviceConfig() {
            return android.provider.DeviceConfigInterface.REAL;
        }
    }
}
