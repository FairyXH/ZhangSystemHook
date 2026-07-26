package com.android.server.display.brightness.clamper;

/* JADX INFO: loaded from: classes2.dex */
public class BrightnessClamperController {
    public static final float INVALID_LUX = -1.0f;
    private com.android.server.display.utils.AmbientFilter mAmbientFilter;
    private float mBrightnessCap;
    private boolean mClamperApplied;
    private final com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener mClamperChangeListenerExternal;
    private com.android.server.display.brightness.clamper.BrightnessClamper.Type mClamperType;
    private final java.util.List<com.android.server.display.brightness.clamper.BrightnessClamper<? super com.android.server.display.brightness.clamper.BrightnessClamperController.DisplayDeviceData>> mClampers;
    private float mCustomAnimationRate;
    private final com.android.server.display.feature.DeviceConfigParameterProvider mDeviceConfigParameterProvider;
    private final com.android.server.display.DisplayDeviceConfig mDisplayDeviceConfig;
    private com.android.server.display.IOplusDisplayPowerControllerExt mDpcExt;
    private final java.util.concurrent.Executor mExecutor;
    private final android.os.Handler mHandler;
    private final com.android.server.display.brightness.clamper.BrightnessClamperController.Injector mInjector;
    private android.hardware.Sensor mLightSensor;
    private final android.hardware.SensorEventListener mLightSensorListener;
    private java.lang.String mLightSensorName;
    private final int mLightSensorRate;
    private java.lang.String mLightSensorType;
    private final java.util.List<com.android.server.display.brightness.clamper.BrightnessStateModifier> mModifiers;
    private final android.provider.DeviceConfig.OnPropertiesChangedListener mOnPropertiesChangedListener;
    private android.hardware.Sensor mRegisteredLightSensor;
    private final android.content.res.Resources mResources;
    private final android.hardware.SensorManager mSensorManager;
    private static final java.lang.String TAG = "BrightnessClamperController";
    private static final boolean DEBUG = com.android.server.display.utils.DebugUtils.isDebuggable(TAG);

    public interface ClamperChangeListener {
        void onChanged();
    }

    public BrightnessClamperController(android.os.Handler handler, com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener clamperChangeListener, com.android.server.display.brightness.clamper.BrightnessClamperController.DisplayDeviceData data, android.content.Context context, com.android.server.display.feature.DisplayManagerFlags flags, android.hardware.SensorManager sensorManager, com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
        this(null, handler, clamperChangeListener, data, context, flags, sensorManager, dpcExt);
    }

    BrightnessClamperController(com.android.server.display.brightness.clamper.BrightnessClamperController.Injector injector, android.os.Handler handler, com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener clamperChangeListener, com.android.server.display.brightness.clamper.BrightnessClamperController.DisplayDeviceData data, android.content.Context context, com.android.server.display.feature.DisplayManagerFlags flags, android.hardware.SensorManager sensorManager, com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
        this.mBrightnessCap = 1.0f;
        this.mCustomAnimationRate = -1.0f;
        this.mClamperType = null;
        this.mRegisteredLightSensor = null;
        this.mClamperApplied = false;
        this.mInjector = injector == null ? new com.android.server.display.brightness.clamper.BrightnessClamperController.Injector() : injector;
        this.mDeviceConfigParameterProvider = this.mInjector.getDeviceConfigParameterProvider();
        this.mHandler = handler;
        this.mSensorManager = sensorManager;
        this.mDisplayDeviceConfig = data.mDisplayDeviceConfig;
        this.mLightSensorListener = new com.android.server.display.brightness.clamper.BrightnessClamperController.AnonymousClass1();
        this.mClamperChangeListenerExternal = clamperChangeListener;
        this.mExecutor = new android.os.HandlerExecutor(handler);
        this.mResources = context.getResources();
        this.mLightSensorRate = context.getResources().getInteger(android.R.integer.config_audio_notif_vol_steps);
        this.mDpcExt = dpcExt;
        boolean isPrimaryDisplay = dpcExt.isPrimaryDisplay(data.getUniqueDisplayId());
        final java.lang.Runnable clamperChangeRunnableInternal = new java.lang.Runnable() { // from class: com.android.server.display.brightness.clamper.BrightnessClamperController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.recalculateBrightnessCap();
            }
        };
        com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener clamperChangeListenerInternal = new com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener() { // from class: com.android.server.display.brightness.clamper.BrightnessClamperController$$ExternalSyntheticLambda4
            @Override // com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener
            public final void onChanged() {
                this.f$0.lambda$new$0(clamperChangeRunnableInternal);
            }
        };
        this.mClampers = this.mInjector.getClampers(handler, clamperChangeListenerInternal, data, flags, context);
        this.mModifiers = this.mInjector.getModifiers(flags, context, handler, clamperChangeListener, data.mDisplayDeviceConfig, this.mSensorManager, dpcExt, isPrimaryDisplay);
        this.mOnPropertiesChangedListener = new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.display.brightness.clamper.BrightnessClamperController$$ExternalSyntheticLambda5
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.lambda$new$1(properties);
            }
        };
        start();
    }

    /* JADX INFO: renamed from: com.android.server.display.brightness.clamper.BrightnessClamperController$1, reason: invalid class name */
    class AnonymousClass1 implements android.hardware.SensorEventListener {
        AnonymousClass1() {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(android.hardware.SensorEvent event) {
            long now = android.os.SystemClock.elapsedRealtime();
            com.android.server.display.brightness.clamper.BrightnessClamperController.this.mAmbientFilter.addValue(java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(event.timestamp), event.values[0]);
            final float lux = com.android.server.display.brightness.clamper.BrightnessClamperController.this.mAmbientFilter.getEstimate(now);
            com.android.server.display.brightness.clamper.BrightnessClamperController.this.mModifiers.forEach(new java.util.function.Consumer() { // from class: com.android.server.display.brightness.clamper.BrightnessClamperController$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.display.brightness.clamper.BrightnessStateModifier) obj).setAmbientLux(lux);
                }
            });
        }

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(java.lang.Runnable clamperChangeRunnableInternal) {
        if (!this.mHandler.hasCallbacks(clamperChangeRunnableInternal)) {
            this.mHandler.post(clamperChangeRunnableInternal);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(android.provider.DeviceConfig.Properties properties) {
        this.mClampers.forEach(new java.util.function.Consumer() { // from class: com.android.server.display.brightness.clamper.BrightnessClamperController$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.display.brightness.clamper.BrightnessClamper) obj).onDeviceConfigChanged();
            }
        });
    }

    public void onDisplayChanged(final com.android.server.display.brightness.clamper.BrightnessClamperController.DisplayDeviceData data) {
        this.mClampers.forEach(new java.util.function.Consumer() { // from class: com.android.server.display.brightness.clamper.BrightnessClamperController$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.display.brightness.clamper.BrightnessClamper) obj).onDisplayChanged(data);
            }
        });
    }

    public com.android.server.display.DisplayBrightnessState clamp(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest request, float brightnessValue, boolean slowChange, int displayState) {
        this.mBrightnessCap = this.mDpcExt.getTotalDisplayBrightness();
        float cappedBrightness = java.lang.Math.min(brightnessValue, this.mBrightnessCap);
        com.android.server.display.DisplayBrightnessState.Builder builder = com.android.server.display.DisplayBrightnessState.builder();
        builder.setIsSlowChange(slowChange);
        builder.setBrightness(cappedBrightness);
        builder.setMaxBrightness(this.mBrightnessCap);
        builder.setCustomAnimationRate(this.mCustomAnimationRate);
        if (this.mClamperType != null) {
            builder.getBrightnessReason().addModifier(8);
            if (!this.mClamperApplied) {
                builder.setIsSlowChange(false);
            }
            this.mClamperApplied = true;
        } else {
            this.mClamperApplied = false;
        }
        if (displayState != 2) {
            unregisterSensorListener();
        } else {
            maybeRegisterLightSensor();
        }
        for (int i = 0; i < this.mModifiers.size(); i++) {
            this.mModifiers.get(i).apply(request, builder);
        }
        return builder.build();
    }

    public int getBrightnessMaxReason() {
        if (this.mClamperType == null) {
            return 0;
        }
        if (this.mClamperType == com.android.server.display.brightness.clamper.BrightnessClamper.Type.THERMAL) {
            return 1;
        }
        if (this.mClamperType == com.android.server.display.brightness.clamper.BrightnessClamper.Type.POWER) {
            return 2;
        }
        if (this.mClamperType == com.android.server.display.brightness.clamper.BrightnessClamper.Type.WEAR_BEDTIME_MODE) {
            return 3;
        }
        android.util.Slog.wtf(TAG, "BrightnessMaxReason not mapped for type=" + this.mClamperType);
        return 0;
    }

    public void dump(java.io.PrintWriter writer) {
        writer.println("BrightnessClamperController:");
        writer.println("  mBrightnessCap: " + this.mBrightnessCap);
        writer.println("  mClamperType: " + this.mClamperType);
        writer.println("  mClamperApplied: " + this.mClamperApplied);
        writer.println("  mLightSensor=" + this.mLightSensor);
        writer.println("  mRegisteredLightSensor=" + this.mRegisteredLightSensor);
        final android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(writer, "    ");
        this.mClampers.forEach(new java.util.function.Consumer() { // from class: com.android.server.display.brightness.clamper.BrightnessClamperController$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.display.brightness.clamper.BrightnessClamper) obj).dump(ipw);
            }
        });
        this.mModifiers.forEach(new java.util.function.Consumer() { // from class: com.android.server.display.brightness.clamper.BrightnessClamperController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.display.brightness.clamper.BrightnessStateModifier) obj).dump(ipw);
            }
        });
    }

    public void stop() {
        this.mDeviceConfigParameterProvider.removeOnPropertiesChangedListener(this.mOnPropertiesChangedListener);
        this.mClampers.forEach(new java.util.function.Consumer() { // from class: com.android.server.display.brightness.clamper.BrightnessClamperController$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.display.brightness.clamper.BrightnessClamper) obj).stop();
            }
        });
        this.mModifiers.forEach(new java.util.function.Consumer() { // from class: com.android.server.display.brightness.clamper.BrightnessClamperController$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.display.brightness.clamper.BrightnessStateModifier) obj).stop();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recalculateBrightnessCap() {
        float brightnessCap = this.mDpcExt.getTotalDisplayBrightness();
        com.android.server.display.brightness.clamper.BrightnessClamper.Type clamperType = null;
        float customAnimationRate = -1.0f;
        com.android.server.display.brightness.clamper.BrightnessClamper<?> minClamper = this.mClampers.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.display.brightness.clamper.BrightnessClamperController$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.display.brightness.clamper.BrightnessClamper) obj).isActive();
            }
        }).min(new java.util.Comparator() { // from class: com.android.server.display.brightness.clamper.BrightnessClamperController$$ExternalSyntheticLambda12
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return java.lang.Float.compare(((com.android.server.display.brightness.clamper.BrightnessClamper) obj).getBrightnessCap(), ((com.android.server.display.brightness.clamper.BrightnessClamper) obj2).getBrightnessCap());
            }
        }).orElse(null);
        if (minClamper != null) {
            brightnessCap = minClamper.getBrightnessCap();
            clamperType = minClamper.getType();
            customAnimationRate = minClamper.getCustomAnimationRate();
        }
        if (this.mBrightnessCap != brightnessCap || this.mClamperType != clamperType || this.mCustomAnimationRate != customAnimationRate) {
            this.mBrightnessCap = brightnessCap;
            this.mClamperType = clamperType;
            this.mCustomAnimationRate = customAnimationRate;
            this.mClamperChangeListenerExternal.onChanged();
        }
    }

    private void start() {
        if (!this.mClampers.isEmpty()) {
            this.mDeviceConfigParameterProvider.addOnPropertiesChangedListener(this.mExecutor, this.mOnPropertiesChangedListener);
            reloadLightSensorData(this.mDisplayDeviceConfig);
            this.mLightSensor = this.mInjector.getLightSensor(this.mSensorManager, this.mLightSensorType, this.mLightSensorName);
            maybeRegisterLightSensor();
        }
    }

    static class Injector {
        Injector() {
        }

        com.android.server.display.feature.DeviceConfigParameterProvider getDeviceConfigParameterProvider() {
            return new com.android.server.display.feature.DeviceConfigParameterProvider(android.provider.DeviceConfigInterface.REAL);
        }

        java.util.List<com.android.server.display.brightness.clamper.BrightnessClamper<? super com.android.server.display.brightness.clamper.BrightnessClamperController.DisplayDeviceData>> getClampers(android.os.Handler handler, com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener clamperChangeListener, com.android.server.display.brightness.clamper.BrightnessClamperController.DisplayDeviceData data, com.android.server.display.feature.DisplayManagerFlags flags, android.content.Context context) {
            java.util.List<com.android.server.display.brightness.clamper.BrightnessClamper<? super com.android.server.display.brightness.clamper.BrightnessClamperController.DisplayDeviceData>> clampers = new java.util.ArrayList<>();
            clampers.add(new com.android.server.display.brightness.clamper.BrightnessThermalClamper(handler, clamperChangeListener, data));
            if (flags.isPowerThrottlingClamperEnabled()) {
                clampers.add(new com.android.server.display.brightness.clamper.BrightnessPowerClamper(handler, clamperChangeListener, data));
            }
            if (flags.isBrightnessWearBedtimeModeClamperEnabled()) {
                clampers.add(new com.android.server.display.brightness.clamper.BrightnessWearBedtimeModeClamper(handler, context, clamperChangeListener, data));
            }
            return clampers;
        }

        java.util.List<com.android.server.display.brightness.clamper.BrightnessStateModifier> getModifiers(com.android.server.display.feature.DisplayManagerFlags flags, android.content.Context context, android.os.Handler handler, com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener listener, com.android.server.display.DisplayDeviceConfig displayDeviceConfig, android.hardware.SensorManager sensorManager, com.android.server.display.IOplusDisplayPowerControllerExt dpcExt, boolean isPrimaryDisplay) {
            java.util.List<com.android.server.display.brightness.clamper.BrightnessStateModifier> modifiers = new java.util.ArrayList<>();
            modifiers.add(new com.android.server.display.brightness.clamper.DisplayDimModifier(context, dpcExt));
            modifiers.add(new com.android.server.display.brightness.clamper.BrightnessLowPowerModeModifier(dpcExt, isPrimaryDisplay));
            if (flags.isEvenDimmerEnabled() && displayDeviceConfig != null && displayDeviceConfig.isEvenDimmerAvailable()) {
                modifiers.add(new com.android.server.display.brightness.clamper.BrightnessLowLuxModifier(handler, listener, context, displayDeviceConfig));
            }
            return modifiers;
        }

        android.hardware.Sensor getLightSensor(android.hardware.SensorManager sensorManager, java.lang.String type, java.lang.String name) {
            return com.android.server.display.utils.SensorUtils.findSensor(sensorManager, type, name, 5);
        }
    }

    public static class DisplayDeviceData implements com.android.server.display.brightness.clamper.BrightnessThermalClamper.ThermalData, com.android.server.display.brightness.clamper.BrightnessPowerClamper.PowerData, com.android.server.display.brightness.clamper.BrightnessWearBedtimeModeClamper.WearBedtimeModeData {
        private final com.android.server.display.DisplayDeviceConfig mDisplayDeviceConfig;
        private final java.lang.String mPowerThrottlingDataId;
        private final java.lang.String mThermalThrottlingDataId;
        private final java.lang.String mUniqueDisplayId;

        public DisplayDeviceData(java.lang.String uniqueDisplayId, java.lang.String thermalThrottlingDataId, java.lang.String powerThrottlingDataId, com.android.server.display.DisplayDeviceConfig displayDeviceConfig) {
            this.mUniqueDisplayId = uniqueDisplayId;
            this.mThermalThrottlingDataId = thermalThrottlingDataId;
            this.mPowerThrottlingDataId = powerThrottlingDataId;
            this.mDisplayDeviceConfig = displayDeviceConfig;
        }

        @Override // com.android.server.display.brightness.clamper.BrightnessThermalClamper.ThermalData, com.android.server.display.brightness.clamper.BrightnessPowerClamper.PowerData
        public java.lang.String getUniqueDisplayId() {
            return this.mUniqueDisplayId;
        }

        @Override // com.android.server.display.brightness.clamper.BrightnessThermalClamper.ThermalData
        public java.lang.String getThermalThrottlingDataId() {
            return this.mThermalThrottlingDataId;
        }

        @Override // com.android.server.display.brightness.clamper.BrightnessThermalClamper.ThermalData
        public com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData getThermalBrightnessThrottlingData() {
            return this.mDisplayDeviceConfig.getThermalBrightnessThrottlingDataMapByThrottlingId().get(this.mThermalThrottlingDataId);
        }

        @Override // com.android.server.display.brightness.clamper.BrightnessPowerClamper.PowerData
        public java.lang.String getPowerThrottlingDataId() {
            return this.mPowerThrottlingDataId;
        }

        @Override // com.android.server.display.brightness.clamper.BrightnessPowerClamper.PowerData
        public com.android.server.display.DisplayDeviceConfig.PowerThrottlingData getPowerThrottlingData() {
            return this.mDisplayDeviceConfig.getPowerThrottlingDataMapByThrottlingId().get(this.mPowerThrottlingDataId);
        }

        @Override // com.android.server.display.brightness.clamper.BrightnessPowerClamper.PowerData
        public com.android.server.display.DisplayDeviceConfig.PowerThrottlingConfigData getPowerThrottlingConfigData() {
            return this.mDisplayDeviceConfig.getPowerThrottlingConfigData();
        }

        @Override // com.android.server.display.brightness.clamper.BrightnessWearBedtimeModeClamper.WearBedtimeModeData
        public float getBrightnessWearBedtimeModeCap() {
            return this.mDisplayDeviceConfig.getBrightnessCapForWearBedtimeMode();
        }

        @Override // com.android.server.display.brightness.clamper.BrightnessThermalClamper.ThermalData
        public com.android.server.display.config.SensorData getTempSensor() {
            return this.mDisplayDeviceConfig.getTempSensor();
        }
    }

    private void maybeRegisterLightSensor() {
        if (this.mModifiers.stream().noneMatch(new java.util.function.Predicate() { // from class: com.android.server.display.brightness.clamper.BrightnessClamperController$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.display.brightness.clamper.BrightnessStateModifier) obj).shouldListenToLightSensor();
            }
        }) || this.mRegisteredLightSensor == this.mLightSensor) {
            return;
        }
        if (this.mRegisteredLightSensor != null) {
            unregisterSensorListener();
        }
        this.mAmbientFilter = com.android.server.display.utils.AmbientFilterFactory.createBrightnessFilter(TAG, this.mResources);
        this.mSensorManager.registerListener(this.mLightSensorListener, this.mLightSensor, this.mLightSensorRate * 1000, this.mHandler);
        this.mRegisteredLightSensor = this.mLightSensor;
        if (DEBUG) {
            android.util.Slog.d(TAG, "maybeRegisterLightSensor");
        }
    }

    private void unregisterSensorListener() {
        this.mSensorManager.unregisterListener(this.mLightSensorListener);
        this.mRegisteredLightSensor = null;
        this.mModifiers.forEach(new java.util.function.Consumer() { // from class: com.android.server.display.brightness.clamper.BrightnessClamperController$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.display.brightness.clamper.BrightnessStateModifier) obj).setAmbientLux(-1.0f);
            }
        });
        if (DEBUG) {
            android.util.Slog.d(TAG, "unregisterSensorListener");
        }
    }

    private void reloadLightSensorData(com.android.server.display.DisplayDeviceConfig displayDeviceConfig) {
        if (displayDeviceConfig != null && displayDeviceConfig.getAmbientLightSensor() != null) {
            this.mLightSensorType = displayDeviceConfig.getAmbientLightSensor().type;
            this.mLightSensorName = displayDeviceConfig.getAmbientLightSensor().name;
        } else if (this.mLightSensorName == null && this.mLightSensorType == null) {
            this.mLightSensorType = this.mResources.getString(android.R.string.config_emergency_dialer_package);
            this.mLightSensorName = "";
        }
    }
}
