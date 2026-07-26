package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
class BrightnessRangeController {
    private final com.android.server.display.HighBrightnessModeController mHbmController;
    private final com.android.server.display.brightness.clamper.HdrClamper mHdrClamper;
    private final java.lang.Runnable mModeChangeCallback;
    private final com.android.server.display.NormalBrightnessModeController mNormalBrightnessModeController;
    private final boolean mUseHdrClamper;
    private final boolean mUseNbmController;

    BrightnessRangeController(com.android.server.display.HighBrightnessModeController hbmController, java.lang.Runnable modeChangeCallback, com.android.server.display.DisplayDeviceConfig displayDeviceConfig, android.os.Handler handler, com.android.server.display.feature.DisplayManagerFlags flags, android.os.IBinder displayToken, com.android.server.display.DisplayDeviceInfo info) {
        com.android.server.display.NormalBrightnessModeController normalBrightnessModeController = new com.android.server.display.NormalBrightnessModeController();
        java.util.Objects.requireNonNull(modeChangeCallback);
        this(hbmController, modeChangeCallback, displayDeviceConfig, normalBrightnessModeController, new com.android.server.display.brightness.clamper.HdrClamper(new com.android.server.display.BrightnessRangeController$$ExternalSyntheticLambda5(modeChangeCallback), new android.os.Handler(handler.getLooper())), flags, displayToken, info);
    }

    BrightnessRangeController(com.android.server.display.HighBrightnessModeController hbmController, java.lang.Runnable modeChangeCallback, com.android.server.display.DisplayDeviceConfig displayDeviceConfig, com.android.server.display.NormalBrightnessModeController normalBrightnessModeController, com.android.server.display.brightness.clamper.HdrClamper hdrClamper, com.android.server.display.feature.DisplayManagerFlags flags, android.os.IBinder displayToken, com.android.server.display.DisplayDeviceInfo info) {
        this.mHbmController = hbmController;
        this.mModeChangeCallback = modeChangeCallback;
        this.mHdrClamper = hdrClamper;
        this.mNormalBrightnessModeController = normalBrightnessModeController;
        this.mUseHdrClamper = flags.isHdrClamperEnabled();
        this.mUseNbmController = flags.isNbmControllerEnabled();
        if (this.mUseNbmController) {
            this.mNormalBrightnessModeController.resetNbmData(displayDeviceConfig.getLuxThrottlingData());
        }
        updateHdrClamper(info, displayToken, displayDeviceConfig);
    }

    void dump(java.io.PrintWriter pw) {
        pw.println("BrightnessRangeController:");
        pw.println("  mUseNormalBrightnessController=" + this.mUseNbmController);
        pw.println("  mUseHdrClamper=" + this.mUseHdrClamper);
        this.mHbmController.dump(pw);
        this.mNormalBrightnessModeController.dump(pw);
        this.mHdrClamper.dump(pw);
    }

    void onAmbientLuxChange(final float ambientLux) {
        applyChanges(new java.util.function.BooleanSupplier() { // from class: com.android.server.display.BrightnessRangeController$$ExternalSyntheticLambda2
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return this.f$0.lambda$onAmbientLuxChange$0(ambientLux);
            }
        }, new java.lang.Runnable() { // from class: com.android.server.display.BrightnessRangeController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onAmbientLuxChange$1(ambientLux);
            }
        });
        if (this.mUseHdrClamper) {
            this.mHdrClamper.onAmbientLuxChange(ambientLux);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onAmbientLuxChange$0(float ambientLux) {
        return this.mNormalBrightnessModeController.onAmbientLuxChange(ambientLux);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAmbientLuxChange$1(float ambientLux) {
        this.mHbmController.onAmbientLuxChange(ambientLux);
    }

    float getNormalBrightnessMax() {
        return this.mHbmController.getNormalBrightnessMax();
    }

    void loadFromConfig(final com.android.server.display.HighBrightnessModeMetadata hbmMetadata, final android.os.IBinder token, final com.android.server.display.DisplayDeviceInfo info, final com.android.server.display.DisplayDeviceConfig displayDeviceConfig) {
        applyChanges(new java.util.function.BooleanSupplier() { // from class: com.android.server.display.BrightnessRangeController$$ExternalSyntheticLambda0
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return this.f$0.lambda$loadFromConfig$2(displayDeviceConfig);
            }
        }, new java.lang.Runnable() { // from class: com.android.server.display.BrightnessRangeController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$loadFromConfig$3(hbmMetadata, info, token, displayDeviceConfig);
            }
        });
        updateHdrClamper(info, token, displayDeviceConfig);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$loadFromConfig$2(com.android.server.display.DisplayDeviceConfig displayDeviceConfig) {
        return this.mNormalBrightnessModeController.resetNbmData(displayDeviceConfig.getLuxThrottlingData());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadFromConfig$3(com.android.server.display.HighBrightnessModeMetadata hbmMetadata, com.android.server.display.DisplayDeviceInfo info, android.os.IBinder token, final com.android.server.display.DisplayDeviceConfig displayDeviceConfig) {
        this.mHbmController.setHighBrightnessModeMetadata(hbmMetadata);
        com.android.server.display.HighBrightnessModeController highBrightnessModeController = this.mHbmController;
        int i = info.width;
        int i2 = info.height;
        java.lang.String str = info.uniqueId;
        com.android.server.display.DisplayDeviceConfig.HighBrightnessModeData highBrightnessModeData = displayDeviceConfig.getHighBrightnessModeData();
        java.util.Objects.requireNonNull(displayDeviceConfig);
        highBrightnessModeController.resetHbmData(i, i2, token, str, highBrightnessModeData, new com.android.server.display.HighBrightnessModeController.HdrBrightnessDeviceConfig() { // from class: com.android.server.display.BrightnessRangeController$$ExternalSyntheticLambda4
            @Override // com.android.server.display.HighBrightnessModeController.HdrBrightnessDeviceConfig
            public final float getHdrBrightnessFromSdr(float f, float f2) {
                return displayDeviceConfig.getHdrBrightnessFromSdr(f, f2);
            }
        });
    }

    void stop() {
        this.mHbmController.stop();
        this.mHdrClamper.stop();
    }

    void setAutoBrightnessEnabled(final int state) {
        applyChanges(new java.util.function.BooleanSupplier() { // from class: com.android.server.display.BrightnessRangeController$$ExternalSyntheticLambda6
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return this.f$0.lambda$setAutoBrightnessEnabled$4(state);
            }
        }, new java.lang.Runnable() { // from class: com.android.server.display.BrightnessRangeController$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setAutoBrightnessEnabled$5(state);
            }
        });
        this.mHdrClamper.setAutoBrightnessState(state);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setAutoBrightnessEnabled$4(int state) {
        return this.mNormalBrightnessModeController.setAutoBrightnessState(state);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setAutoBrightnessEnabled$5(int state) {
        this.mHbmController.setAutoBrightnessEnabled(state);
    }

    void onBrightnessChanged(float brightness, float unthrottledBrightness, int throttlingReason) {
        this.mHbmController.onBrightnessChanged(brightness, unthrottledBrightness, throttlingReason);
    }

    float getCurrentBrightnessMin() {
        return this.mHbmController.getCurrentBrightnessMin();
    }

    float getCurrentBrightnessMax() {
        if (this.mUseNbmController && (!this.mHbmController.deviceSupportsHbm() || !this.mHbmController.isHbmCurrentlyAllowed())) {
            return java.lang.Math.min(this.mHbmController.getCurrentBrightnessMax(), this.mNormalBrightnessModeController.getCurrentBrightnessMax());
        }
        return this.mHbmController.getCurrentBrightnessMax();
    }

    int getHighBrightnessMode() {
        return this.mHbmController.getHighBrightnessMode();
    }

    float getHdrBrightnessValue() {
        float hdrBrightness = this.mHbmController.getHdrBrightnessValue();
        return this.mUseHdrClamper ? this.mHdrClamper.clamp(hdrBrightness) : hdrBrightness;
    }

    float getTransitionPoint() {
        return this.mHbmController.getTransitionPoint();
    }

    private void updateHdrClamper(com.android.server.display.DisplayDeviceInfo info, android.os.IBinder token, com.android.server.display.DisplayDeviceConfig displayDeviceConfig) {
        if (this.mUseHdrClamper) {
            com.android.server.display.DisplayDeviceConfig.HighBrightnessModeData hbmData = displayDeviceConfig.getHighBrightnessModeData();
            float minimumHdrPercentOfScreen = hbmData == null ? -1.0f : hbmData.minimumHdrPercentOfScreen;
            this.mHdrClamper.resetHdrConfig(displayDeviceConfig.getHdrBrightnessData(), info.width, info.height, minimumHdrPercentOfScreen, token);
        }
    }

    private void applyChanges(java.util.function.BooleanSupplier nbmChangesFunc, java.lang.Runnable hbmChangesFunc) {
        if (this.mUseNbmController) {
            boolean nbmTransitionChanged = nbmChangesFunc.getAsBoolean();
            hbmChangesFunc.run();
            if (nbmTransitionChanged) {
                this.mModeChangeCallback.run();
                return;
            }
            return;
        }
        hbmChangesFunc.run();
    }

    public float getHdrTransitionRate() {
        if (this.mUseHdrClamper) {
            return this.mHdrClamper.getTransitionRate();
        }
        return -1.0f;
    }
}
