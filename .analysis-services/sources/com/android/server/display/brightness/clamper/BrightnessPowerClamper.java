package com.android.server.display.brightness.clamper;

/* JADX INFO: loaded from: classes2.dex */
class BrightnessPowerClamper extends com.android.server.display.brightness.clamper.BrightnessClamper<com.android.server.display.brightness.clamper.BrightnessPowerClamper.PowerData> {
    private static final java.lang.String TAG = "BrightnessPowerClamper";
    private final com.android.server.display.feature.DeviceConfigParameterProvider mConfigParameterProvider;
    private float mCurrentAvgPowerConsumed;
    private int mCurrentThermalLevel;
    private java.lang.String mDataId;
    private final java.util.function.BiFunction<java.lang.String, java.lang.String, com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.ThrottlingLevel> mDataPointMapper;
    private final java.util.function.Function<java.util.List<com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.ThrottlingLevel>, com.android.server.display.DisplayDeviceConfig.PowerThrottlingData> mDataSetMapper;
    private final com.android.server.display.brightness.clamper.BrightnessPowerClamper.Injector mInjector;
    private com.android.server.display.brightness.clamper.PmicMonitor mPmicMonitor;
    private com.android.server.display.DisplayDeviceConfig.PowerThrottlingConfigData mPowerThrottlingConfigData;
    private com.android.server.display.DisplayDeviceConfig.PowerThrottlingData mPowerThrottlingDataActive;
    private com.android.server.display.DisplayDeviceConfig.PowerThrottlingData mPowerThrottlingDataFromDDC;
    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, com.android.server.display.DisplayDeviceConfig.PowerThrottlingData>> mPowerThrottlingDataOverride;
    private java.lang.String mUniqueDisplayId;

    @java.lang.FunctionalInterface
    public interface PowerChangeListener {
        void onChanged(float f, int i);
    }

    public interface PowerData {
        com.android.server.display.DisplayDeviceConfig.PowerThrottlingConfigData getPowerThrottlingConfigData();

        com.android.server.display.DisplayDeviceConfig.PowerThrottlingData getPowerThrottlingData();

        java.lang.String getPowerThrottlingDataId();

        java.lang.String getUniqueDisplayId();
    }

    static /* synthetic */ com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.ThrottlingLevel lambda$new$0(java.lang.String key, java.lang.String value) {
        try {
            int status = com.android.server.display.utils.DeviceConfigParsingUtils.parseThermalStatus(key);
            float powerQuota = java.lang.Float.parseFloat(value);
            return new com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.ThrottlingLevel(status, powerQuota);
        } catch (java.lang.IllegalArgumentException e) {
            return null;
        }
    }

    BrightnessPowerClamper(android.os.Handler handler, com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener listener, com.android.server.display.brightness.clamper.BrightnessPowerClamper.PowerData powerData) {
        this(new com.android.server.display.brightness.clamper.BrightnessPowerClamper.Injector(), handler, listener, powerData);
    }

    BrightnessPowerClamper(com.android.server.display.brightness.clamper.BrightnessPowerClamper.Injector injector, android.os.Handler handler, com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener listener, final com.android.server.display.brightness.clamper.BrightnessPowerClamper.PowerData powerData) {
        super(handler, listener);
        this.mPowerThrottlingDataOverride = java.util.Map.of();
        this.mPowerThrottlingDataFromDDC = null;
        this.mPowerThrottlingDataActive = null;
        this.mPowerThrottlingConfigData = null;
        this.mCurrentThermalLevel = 0;
        this.mCurrentAvgPowerConsumed = 0.0f;
        this.mUniqueDisplayId = null;
        this.mDataId = null;
        this.mDataPointMapper = new java.util.function.BiFunction() { // from class: com.android.server.display.brightness.clamper.BrightnessPowerClamper$$ExternalSyntheticLambda3
            @Override // java.util.function.BiFunction
            public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.display.brightness.clamper.BrightnessPowerClamper.lambda$new$0((java.lang.String) obj, (java.lang.String) obj2);
            }
        };
        this.mDataSetMapper = new java.util.function.Function() { // from class: com.android.server.display.brightness.clamper.BrightnessPowerClamper$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.create((java.util.List) obj);
            }
        };
        this.mInjector = injector;
        this.mConfigParameterProvider = injector.getDeviceConfigParameterProvider();
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.brightness.clamper.BrightnessPowerClamper$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$1(powerData);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(com.android.server.display.brightness.clamper.BrightnessPowerClamper.PowerData powerData) {
        setDisplayData(powerData);
        loadOverrideData();
        start();
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessClamper
    com.android.server.display.brightness.clamper.BrightnessClamper.Type getType() {
        return com.android.server.display.brightness.clamper.BrightnessClamper.Type.POWER;
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessClamper
    void onDeviceConfigChanged() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.brightness.clamper.BrightnessPowerClamper$$ExternalSyntheticLambda6
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
    public void onDisplayChanged(final com.android.server.display.brightness.clamper.BrightnessPowerClamper.PowerData data) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.brightness.clamper.BrightnessPowerClamper$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onDisplayChanged$3(data);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDisplayChanged$3(com.android.server.display.brightness.clamper.BrightnessPowerClamper.PowerData data) {
        setDisplayData(data);
        recalculateActiveData();
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessClamper
    void stop() {
        if (this.mPmicMonitor != null) {
            this.mPmicMonitor.shutdown();
        }
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessClamper
    public void dump(java.io.PrintWriter pw) {
        pw.println("BrightnessPowerClamper:");
        pw.println("  mCurrentAvgPowerConsumed=" + this.mCurrentAvgPowerConsumed);
        pw.println("  mUniqueDisplayId=" + this.mUniqueDisplayId);
        pw.println("  mCurrentThermalLevel=" + this.mCurrentThermalLevel);
        pw.println("  mPowerThrottlingDataFromDDC=" + (this.mPowerThrottlingDataFromDDC == null ? "null" : this.mPowerThrottlingDataFromDDC.toString()));
        super.dump(pw);
    }

    private void recalculateActiveData() {
        if (this.mUniqueDisplayId == null || this.mDataId == null) {
            return;
        }
        this.mPowerThrottlingDataActive = this.mPowerThrottlingDataOverride.getOrDefault(this.mUniqueDisplayId, java.util.Map.of()).getOrDefault(this.mDataId, this.mPowerThrottlingDataFromDDC);
        if (this.mPowerThrottlingDataActive != null) {
            if (this.mPmicMonitor != null) {
                this.mPmicMonitor.stop();
                this.mPmicMonitor.start();
            }
        } else if (this.mPmicMonitor != null) {
            this.mPmicMonitor.stop();
        }
        recalculateBrightnessCap();
    }

    private void loadOverrideData() {
        java.lang.String throttlingDataOverride = this.mConfigParameterProvider.getPowerThrottlingData();
        this.mPowerThrottlingDataOverride = com.android.server.display.utils.DeviceConfigParsingUtils.parseDeviceConfigMap(throttlingDataOverride, this.mDataPointMapper, this.mDataSetMapper);
    }

    private void setDisplayData(com.android.server.display.brightness.clamper.BrightnessPowerClamper.PowerData data) {
        this.mUniqueDisplayId = data.getUniqueDisplayId();
        this.mDataId = data.getPowerThrottlingDataId();
        this.mPowerThrottlingDataFromDDC = data.getPowerThrottlingData();
        if (this.mPowerThrottlingDataFromDDC == null && !"default".equals(this.mDataId)) {
            android.util.Slog.wtf(TAG, "Power throttling data is missing for powerThrottlingDataId=" + this.mDataId);
        }
        this.mPowerThrottlingConfigData = data.getPowerThrottlingConfigData();
        if (this.mPowerThrottlingConfigData == null) {
            android.util.Slog.d(TAG, "Power throttling data is missing for configuration data.");
        }
    }

    private void recalculateBrightnessCap() {
        boolean isActive = false;
        float targetBrightnessCap = 1.0f;
        float powerQuota = getPowerQuotaForThermalStatus(this.mCurrentThermalLevel);
        if (this.mPowerThrottlingDataActive == null) {
            return;
        }
        if (powerQuota > 0.0f && this.mCurrentAvgPowerConsumed > powerQuota) {
            isActive = true;
            targetBrightnessCap = java.lang.Math.max((powerQuota / this.mCurrentAvgPowerConsumed) * 1.0f, this.mPowerThrottlingConfigData.brightnessLowestCapAllowed);
        }
        if (this.mBrightnessCap != targetBrightnessCap || this.mIsActive != isActive) {
            this.mIsActive = isActive;
            this.mBrightnessCap = targetBrightnessCap;
            this.mChangeListener.onChanged();
        }
    }

    private float getPowerQuotaForThermalStatus(int thermalStatus) {
        float powerQuota = 0.0f;
        if (this.mPowerThrottlingDataActive != null) {
            for (com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.ThrottlingLevel level : this.mPowerThrottlingDataActive.throttlingLevels) {
                if (level.thermalStatus > thermalStatus) {
                    break;
                }
                powerQuota = level.powerQuotaMilliWatts;
            }
        }
        return powerQuota;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: recalculatePowerQuotaChange, reason: merged with bridge method [inline-methods] */
    public void lambda$start$5(final float avgPowerConsumed, final int thermalStatus) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.brightness.clamper.BrightnessPowerClamper$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$recalculatePowerQuotaChange$4(thermalStatus, avgPowerConsumed);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$recalculatePowerQuotaChange$4(int thermalStatus, float avgPowerConsumed) {
        this.mCurrentThermalLevel = thermalStatus;
        this.mCurrentAvgPowerConsumed = avgPowerConsumed;
        recalculateBrightnessCap();
    }

    private void start() {
        if (this.mPowerThrottlingConfigData == null) {
            return;
        }
        com.android.server.display.brightness.clamper.BrightnessPowerClamper.PowerChangeListener listener = new com.android.server.display.brightness.clamper.BrightnessPowerClamper.PowerChangeListener() { // from class: com.android.server.display.brightness.clamper.BrightnessPowerClamper$$ExternalSyntheticLambda2
            @Override // com.android.server.display.brightness.clamper.BrightnessPowerClamper.PowerChangeListener
            public final void onChanged(float f, int i) {
                this.f$0.lambda$start$5(f, i);
            }
        };
        this.mPmicMonitor = this.mInjector.getPmicMonitor(listener, this.mPowerThrottlingConfigData.pollingWindowMillis);
        this.mPmicMonitor.start();
    }

    static class Injector {
        Injector() {
        }

        com.android.server.display.brightness.clamper.PmicMonitor getPmicMonitor(com.android.server.display.brightness.clamper.BrightnessPowerClamper.PowerChangeListener listener, int pollingTime) {
            return new com.android.server.display.brightness.clamper.PmicMonitor(listener, pollingTime);
        }

        com.android.server.display.feature.DeviceConfigParameterProvider getDeviceConfigParameterProvider() {
            return new com.android.server.display.feature.DeviceConfigParameterProvider(android.provider.DeviceConfigInterface.REAL);
        }
    }
}
