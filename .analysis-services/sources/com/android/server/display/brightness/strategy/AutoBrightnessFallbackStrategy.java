package com.android.server.display.brightness.strategy;

/* JADX INFO: loaded from: classes2.dex */
public final class AutoBrightnessFallbackStrategy implements com.android.server.display.brightness.strategy.DisplayBrightnessStrategy {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final com.android.server.display.brightness.strategy.AutoBrightnessFallbackStrategy.Injector mInjector;
    private boolean mIsEnabled;
    private int mLeadDisplayId;
    android.hardware.Sensor mScreenOffBrightnessSensor;
    private com.android.server.display.ScreenOffBrightnessSensorController mScreenOffBrightnessSensorController;

    /* JADX INFO: Access modifiers changed from: package-private */
    public interface Injector {
        android.hardware.Sensor getScreenOffBrightnessSensor(android.hardware.SensorManager sensorManager, com.android.server.display.DisplayDeviceConfig displayDeviceConfig);

        com.android.server.display.ScreenOffBrightnessSensorController getScreenOffBrightnessSensorController(android.hardware.SensorManager sensorManager, android.hardware.Sensor sensor, android.os.Handler handler, com.android.server.display.ScreenOffBrightnessSensorController.Clock clock, int[] iArr, com.android.server.display.BrightnessMappingStrategy brightnessMappingStrategy);
    }

    public AutoBrightnessFallbackStrategy(com.android.server.display.brightness.strategy.AutoBrightnessFallbackStrategy.Injector injector) {
        this.mInjector = injector == null ? new com.android.server.display.brightness.strategy.AutoBrightnessFallbackStrategy.RealInjector() : injector;
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public com.android.server.display.DisplayBrightnessState updateBrightness(com.android.server.display.brightness.StrategyExecutionRequest strategyExecutionRequest) {
        float brightness = this.mScreenOffBrightnessSensorController.getAutomaticScreenBrightness();
        com.android.server.display.brightness.BrightnessReason brightnessReason = new com.android.server.display.brightness.BrightnessReason();
        brightnessReason.setReason(9);
        return new com.android.server.display.DisplayBrightnessState.Builder().setBrightness(brightness).setSdrBrightness(brightness).setBrightnessReason(brightnessReason).setDisplayBrightnessStrategyName(getName()).setShouldUpdateScreenBrightnessSetting(brightness != strategyExecutionRequest.getCurrentScreenBrightness()).build();
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public java.lang.String getName() {
        return "AutoBrightnessFallbackStrategy";
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public int getReason() {
        return 9;
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public void dump(java.io.PrintWriter writer) {
        writer.println("AutoBrightnessFallbackStrategy:");
        writer.println("  mLeadDisplayId=" + this.mLeadDisplayId);
        writer.println("  mIsEnabled=" + this.mIsEnabled);
        if (this.mScreenOffBrightnessSensorController != null) {
            this.mScreenOffBrightnessSensorController.dump(new android.util.IndentingPrintWriter(writer, " "));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0026  */
    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void strategySelectionPostProcessor(com.android.server.display.brightness.StrategySelectionNotifyRequest r6) {
        /*
            r5 = this;
            com.android.server.display.ScreenOffBrightnessSensorController r0 = r5.mScreenOffBrightnessSensorController
            if (r0 == 0) goto L2a
            int r0 = r6.getTargetDisplayState()
            com.android.server.display.ScreenOffBrightnessSensorController r1 = r5.mScreenOffBrightnessSensorController
            boolean r2 = r6.isAutoBrightnessEnabled()
            if (r2 == 0) goto L26
            boolean r2 = r5.mIsEnabled
            if (r2 == 0) goto L26
            r2 = 1
            if (r0 == r2) goto L20
            r3 = 3
            if (r0 != r3) goto L26
            boolean r3 = r6.isAllowAutoBrightnessWhileDozingConfig()
            if (r3 != 0) goto L26
        L20:
            int r3 = r5.mLeadDisplayId
            r4 = -1
            if (r3 != r4) goto L26
            goto L27
        L26:
            r2 = 0
        L27:
            r1.setLightSensorEnabled(r2)
        L2a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.display.brightness.strategy.AutoBrightnessFallbackStrategy.strategySelectionPostProcessor(com.android.server.display.brightness.StrategySelectionNotifyRequest):void");
    }

    public com.android.server.display.ScreenOffBrightnessSensorController getScreenOffBrightnessSensorController() {
        return this.mScreenOffBrightnessSensorController;
    }

    public void setupAutoBrightnessFallbackSensor(android.hardware.SensorManager sensorManager, com.android.server.display.DisplayDeviceConfig displayDeviceConfig, android.os.Handler handler, com.android.server.display.BrightnessMappingStrategy brightnessMappingStrategy, boolean isEnabled, int leadDisplayId) {
        this.mIsEnabled = isEnabled;
        this.mLeadDisplayId = leadDisplayId;
        if (this.mScreenOffBrightnessSensorController != null) {
            this.mScreenOffBrightnessSensorController.stop();
            this.mScreenOffBrightnessSensorController = null;
        }
        loadScreenOffBrightnessSensor(sensorManager, displayDeviceConfig);
        int[] sensorValueToLux = displayDeviceConfig.getScreenOffBrightnessSensorValueToLux();
        if (this.mScreenOffBrightnessSensor != null && sensorValueToLux != null) {
            this.mScreenOffBrightnessSensorController = this.mInjector.getScreenOffBrightnessSensorController(sensorManager, this.mScreenOffBrightnessSensor, handler, new com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda2(), sensorValueToLux, brightnessMappingStrategy);
        }
    }

    public void stop() {
        if (this.mScreenOffBrightnessSensorController != null) {
            this.mScreenOffBrightnessSensorController.stop();
        }
    }

    public boolean isValid() {
        return this.mScreenOffBrightnessSensorController != null && com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(this.mScreenOffBrightnessSensorController.getAutomaticScreenBrightness());
    }

    private void loadScreenOffBrightnessSensor(android.hardware.SensorManager sensorManager, com.android.server.display.DisplayDeviceConfig displayDeviceConfig) {
        this.mScreenOffBrightnessSensor = this.mInjector.getScreenOffBrightnessSensor(sensorManager, displayDeviceConfig);
    }

    static class RealInjector implements com.android.server.display.brightness.strategy.AutoBrightnessFallbackStrategy.Injector {
        RealInjector() {
        }

        @Override // com.android.server.display.brightness.strategy.AutoBrightnessFallbackStrategy.Injector
        public android.hardware.Sensor getScreenOffBrightnessSensor(android.hardware.SensorManager sensorManager, com.android.server.display.DisplayDeviceConfig displayDeviceConfig) {
            return com.android.server.display.utils.SensorUtils.findSensor(sensorManager, displayDeviceConfig.getScreenOffBrightnessSensor(), 0);
        }

        @Override // com.android.server.display.brightness.strategy.AutoBrightnessFallbackStrategy.Injector
        public com.android.server.display.ScreenOffBrightnessSensorController getScreenOffBrightnessSensorController(android.hardware.SensorManager sensorManager, android.hardware.Sensor lightSensor, android.os.Handler handler, com.android.server.display.ScreenOffBrightnessSensorController.Clock clock, int[] sensorValueToLux, com.android.server.display.BrightnessMappingStrategy brightnessMapper) {
            return new com.android.server.display.ScreenOffBrightnessSensorController(sensorManager, lightSensor, handler, clock, sensorValueToLux, brightnessMapper);
        }
    }
}
