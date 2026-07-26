package com.android.server.display.brightness.clamper;

/* JADX INFO: loaded from: classes2.dex */
class BrightnessThermalClamper extends com.android.server.display.brightness.clamper.BrightnessClamper<com.android.server.display.brightness.clamper.BrightnessThermalClamper.ThermalData> {
    private static final java.lang.String TAG = "BrightnessThermalClamper";
    private final com.android.server.display.feature.DeviceConfigParameterProvider mConfigParameterProvider;
    private java.lang.String mDataId;
    private final java.util.function.BiFunction<java.lang.String, java.lang.String, com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel> mDataPointMapper;
    private final java.util.function.Function<java.util.List<com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel>, com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData> mDataSetMapper;
    private final com.android.server.display.brightness.clamper.BrightnessThermalClamper.ThermalStatusObserver mThermalStatusObserver;
    private com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData mThermalThrottlingDataActive;
    private com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData mThermalThrottlingDataFromDeviceConfig;
    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData>> mThermalThrottlingDataOverride;
    private int mThrottlingStatus;
    private java.lang.String mUniqueDisplayId;

    interface ThermalData {
        com.android.server.display.config.SensorData getTempSensor();

        com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData getThermalBrightnessThrottlingData();

        java.lang.String getThermalThrottlingDataId();

        java.lang.String getUniqueDisplayId();
    }

    static /* synthetic */ com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel lambda$new$0(java.lang.String key, java.lang.String value) {
        try {
            int status = com.android.server.display.utils.DeviceConfigParsingUtils.parseThermalStatus(key);
            float brightnessPoint = com.android.server.display.utils.DeviceConfigParsingUtils.parseBrightness(value);
            return new com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel(status, brightnessPoint);
        } catch (java.lang.IllegalArgumentException e) {
            return null;
        }
    }

    BrightnessThermalClamper(android.os.Handler handler, com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener listener, com.android.server.display.brightness.clamper.BrightnessThermalClamper.ThermalData thermalData) {
        this(new com.android.server.display.brightness.clamper.BrightnessThermalClamper.Injector(), handler, listener, thermalData);
    }

    BrightnessThermalClamper(com.android.server.display.brightness.clamper.BrightnessThermalClamper.Injector injector, android.os.Handler handler, com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener listener, final com.android.server.display.brightness.clamper.BrightnessThermalClamper.ThermalData thermalData) {
        super(handler, listener);
        this.mThermalThrottlingDataOverride = java.util.Map.of();
        this.mThermalThrottlingDataFromDeviceConfig = null;
        this.mThermalThrottlingDataActive = null;
        this.mUniqueDisplayId = null;
        this.mDataId = null;
        this.mThrottlingStatus = 0;
        this.mDataPointMapper = new java.util.function.BiFunction() { // from class: com.android.server.display.brightness.clamper.BrightnessThermalClamper$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.display.brightness.clamper.BrightnessThermalClamper.lambda$new$0((java.lang.String) obj, (java.lang.String) obj2);
            }
        };
        this.mDataSetMapper = new com.android.server.display.BrightnessThrottler$$ExternalSyntheticLambda2();
        this.mConfigParameterProvider = injector.getDeviceConfigParameterProvider();
        this.mThermalStatusObserver = new com.android.server.display.brightness.clamper.BrightnessThermalClamper.ThermalStatusObserver(injector, handler);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.brightness.clamper.BrightnessThermalClamper$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$1(thermalData);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(com.android.server.display.brightness.clamper.BrightnessThermalClamper.ThermalData thermalData) {
        setDisplayData(thermalData);
        loadOverrideData();
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessClamper
    com.android.server.display.brightness.clamper.BrightnessClamper.Type getType() {
        return com.android.server.display.brightness.clamper.BrightnessClamper.Type.THERMAL;
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessClamper
    void onDeviceConfigChanged() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.brightness.clamper.BrightnessThermalClamper$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onDeviceConfigChanged$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDeviceConfigChanged$2() {
        loadOverrideData();
        recalculateActiveData();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.display.brightness.clamper.BrightnessClamper
    public void onDisplayChanged(final com.android.server.display.brightness.clamper.BrightnessThermalClamper.ThermalData data) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.brightness.clamper.BrightnessThermalClamper$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onDisplayChanged$3(data);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDisplayChanged$3(com.android.server.display.brightness.clamper.BrightnessThermalClamper.ThermalData data) {
        setDisplayData(data);
        recalculateActiveData();
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessClamper
    void stop() {
        this.mThermalStatusObserver.stopObserving();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.display.brightness.clamper.BrightnessClamper
    public void dump(java.io.PrintWriter writer) {
        writer.println("BrightnessThermalClamper:");
        writer.println("  mThrottlingStatus: " + this.mThrottlingStatus);
        writer.println("  mUniqueDisplayId: " + this.mUniqueDisplayId);
        writer.println("  mDataId: " + this.mDataId);
        writer.println("  mDataOverride: " + this.mThermalThrottlingDataOverride);
        writer.println("  mDataFromDeviceConfig: " + this.mThermalThrottlingDataFromDeviceConfig);
        writer.println("  mDataActive: " + this.mThermalThrottlingDataActive);
        this.mThermalStatusObserver.dump(writer);
        super.dump(writer);
    }

    private void recalculateActiveData() {
        if (this.mUniqueDisplayId == null || this.mDataId == null) {
            return;
        }
        this.mThermalThrottlingDataActive = this.mThermalThrottlingDataOverride.getOrDefault(this.mUniqueDisplayId, java.util.Map.of()).getOrDefault(this.mDataId, this.mThermalThrottlingDataFromDeviceConfig);
        recalculateBrightnessCap();
    }

    private void loadOverrideData() {
        java.lang.String throttlingDataOverride = this.mConfigParameterProvider.getBrightnessThrottlingData();
        this.mThermalThrottlingDataOverride = com.android.server.display.utils.DeviceConfigParsingUtils.parseDeviceConfigMap(throttlingDataOverride, this.mDataPointMapper, this.mDataSetMapper);
    }

    private void setDisplayData(com.android.server.display.brightness.clamper.BrightnessThermalClamper.ThermalData data) {
        this.mUniqueDisplayId = data.getUniqueDisplayId();
        this.mDataId = data.getThermalThrottlingDataId();
        this.mThermalThrottlingDataFromDeviceConfig = data.getThermalBrightnessThrottlingData();
        if (this.mThermalThrottlingDataFromDeviceConfig == null && !"default".equals(this.mDataId)) {
            android.util.Slog.wtf(TAG, "Thermal throttling data is missing for thermalThrottlingDataId=" + this.mDataId);
        }
        this.mThermalStatusObserver.registerSensor(data.getTempSensor());
    }

    private void recalculateBrightnessCap() {
        float brightnessCap = 1.0f;
        boolean isActive = false;
        if (this.mThermalThrottlingDataActive != null) {
            for (com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel level : this.mThermalThrottlingDataActive.throttlingLevels) {
                if (level.thermalStatus > this.mThrottlingStatus) {
                    break;
                }
                brightnessCap = level.brightness;
                isActive = true;
            }
        }
        if (brightnessCap != this.mBrightnessCap || this.mIsActive != isActive) {
            this.mBrightnessCap = brightnessCap;
            this.mIsActive = isActive;
            this.mChangeListener.onChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void thermalStatusChanged(int status) {
        if (this.mThrottlingStatus != status) {
            this.mThrottlingStatus = status;
            recalculateBrightnessCap();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class ThermalStatusObserver extends android.os.IThermalEventListener.Stub {
        private final android.os.Handler mHandler;
        private final com.android.server.display.brightness.clamper.BrightnessThermalClamper.Injector mInjector;
        private com.android.server.display.config.SensorData mObserverTempSensor;
        private boolean mStarted = false;
        private android.os.IThermalService mThermalService;

        ThermalStatusObserver(com.android.server.display.brightness.clamper.BrightnessThermalClamper.Injector injector, android.os.Handler handler) {
            this.mInjector = injector;
            this.mHandler = handler;
        }

        void registerSensor(com.android.server.display.config.SensorData tempSensor) {
            if (!this.mStarted || this.mObserverTempSensor == null) {
                this.mObserverTempSensor = tempSensor;
                registerThermalListener();
                return;
            }
            java.lang.String curType = this.mObserverTempSensor.type;
            this.mObserverTempSensor = tempSensor;
            if (curType.equals(tempSensor.type)) {
                android.util.Slog.d(com.android.server.display.brightness.clamper.BrightnessThermalClamper.TAG, "Thermal status observer already started");
            } else {
                stopObserving();
                registerThermalListener();
            }
        }

        void registerThermalListener() {
            this.mThermalService = this.mInjector.getThermalService();
            if (this.mThermalService == null) {
                android.util.Slog.e(com.android.server.display.brightness.clamper.BrightnessThermalClamper.TAG, "Could not observe thermal status. Service not available");
                return;
            }
            int temperatureType = com.android.server.display.utils.SensorUtils.getSensorTemperatureType(this.mObserverTempSensor);
            try {
                this.mThermalService.registerThermalEventListenerWithType(this, temperatureType);
                this.mStarted = true;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.display.brightness.clamper.BrightnessThermalClamper.TAG, "Failed to register thermal status listener", e);
            }
        }

        public void notifyThrottling(android.os.Temperature temp) {
            android.util.Slog.d(com.android.server.display.brightness.clamper.BrightnessThermalClamper.TAG, "New thermal throttling status = " + temp.getStatus());
            if (this.mObserverTempSensor.name != null && !this.mObserverTempSensor.name.equals(temp.getName())) {
                android.util.Slog.i(com.android.server.display.brightness.clamper.BrightnessThermalClamper.TAG, "Skipping thermal throttling notification as monitored sensor: " + this.mObserverTempSensor.name + " != notified sensor: " + temp.getName());
            } else {
                final int status = temp.getStatus();
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.brightness.clamper.BrightnessThermalClamper$ThermalStatusObserver$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$notifyThrottling$0(status);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyThrottling$0(int status) {
            com.android.server.display.brightness.clamper.BrightnessThermalClamper.this.thermalStatusChanged(status);
        }

        void stopObserving() {
            if (!this.mStarted) {
                return;
            }
            try {
                this.mThermalService.unregisterThermalEventListener(this);
                this.mStarted = false;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.display.brightness.clamper.BrightnessThermalClamper.TAG, "Failed to unregister thermal status listener", e);
            }
            this.mThermalService = null;
        }

        void dump(java.io.PrintWriter writer) {
            writer.println("  ThermalStatusObserver:");
            writer.println("    mStarted: " + this.mStarted);
            writer.println("    mObserverTempSensor: " + this.mObserverTempSensor);
            if (this.mThermalService != null) {
                writer.println("    ThermalService available");
            } else {
                writer.println("    ThermalService not available");
            }
        }
    }

    static class Injector {
        Injector() {
        }

        android.os.IThermalService getThermalService() {
            return android.os.IThermalService.Stub.asInterface(android.os.ServiceManager.getService("thermalservice"));
        }

        com.android.server.display.feature.DeviceConfigParameterProvider getDeviceConfigParameterProvider() {
            return new com.android.server.display.feature.DeviceConfigParameterProvider(android.provider.DeviceConfigInterface.REAL);
        }
    }
}
