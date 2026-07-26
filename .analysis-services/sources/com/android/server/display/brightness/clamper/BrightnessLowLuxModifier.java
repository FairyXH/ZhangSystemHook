package com.android.server.display.brightness.clamper;

/* JADX INFO: loaded from: classes2.dex */
public class BrightnessLowLuxModifier extends com.android.server.display.brightness.clamper.BrightnessModifier {
    private static final float MIN_NITS_DEFAULT = 0.2f;
    private float mAmbientLux;
    private float mBrightnessLowerBound;
    private final com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener mChangeListener;
    private final android.content.ContentResolver mContentResolver;
    private final com.android.server.display.DisplayDeviceConfig mDisplayDeviceConfig;
    private final android.os.Handler mHandler;
    private boolean mIsActive;
    private float mMinNitsAllowed;
    private int mReason;
    private final com.android.server.display.brightness.clamper.BrightnessLowLuxModifier.SettingsObserver mSettingsObserver;
    private static final java.lang.String TAG = "BrightnessLowLuxModifier";
    private static final boolean DEBUG = com.android.server.display.utils.DebugUtils.isDebuggable(TAG);

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier, com.android.server.display.brightness.clamper.BrightnessStateModifier
    public /* bridge */ /* synthetic */ void recoverRateType() {
        super.recoverRateType();
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier, com.android.server.display.brightness.clamper.BrightnessStateModifier
    public /* bridge */ /* synthetic */ void setAnimatingState(boolean z) {
        super.setAnimatingState(z);
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier, com.android.server.display.brightness.clamper.BrightnessStateModifier
    public /* bridge */ /* synthetic */ void setRateType() {
        super.setRateType();
    }

    BrightnessLowLuxModifier(android.os.Handler handler, com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener listener, android.content.Context context, com.android.server.display.DisplayDeviceConfig displayDeviceConfig) {
        this.mChangeListener = listener;
        this.mHandler = handler;
        this.mContentResolver = context.getContentResolver();
        this.mSettingsObserver = new com.android.server.display.brightness.clamper.BrightnessLowLuxModifier.SettingsObserver(this.mHandler);
        this.mDisplayDeviceConfig = displayDeviceConfig;
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.brightness.clamper.BrightnessLowLuxModifier$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        });
    }

    public void recalculateLowerBound() {
        float minBrightnessAllowed;
        int reason;
        int i;
        float settingNitsLowerBound = android.provider.Settings.Secure.getFloatForUser(this.mContentResolver, "even_dimmer_min_nits", MIN_NITS_DEFAULT, -2);
        boolean isActive = isSettingEnabled() && this.mAmbientLux != -1.0f;
        float minNitsAllowed = -1.0f;
        if (isActive) {
            float luxBasedNitsLowerBound = this.mDisplayDeviceConfig.getMinNitsFromLux(this.mAmbientLux);
            minNitsAllowed = java.lang.Math.max(settingNitsLowerBound, luxBasedNitsLowerBound);
            minBrightnessAllowed = getBrightnessFromNits(minNitsAllowed);
            if (settingNitsLowerBound > luxBasedNitsLowerBound) {
                i = 32;
            } else {
                i = 16;
            }
            reason = i;
        } else {
            minBrightnessAllowed = this.mDisplayDeviceConfig.getEvenDimmerTransitionPoint();
            reason = 0;
        }
        if (this.mBrightnessLowerBound != minBrightnessAllowed || this.mReason != reason || this.mIsActive != isActive) {
            this.mIsActive = isActive;
            this.mReason = reason;
            if (DEBUG) {
                android.util.Slog.i(TAG, "isActive: " + isActive + ", minBrightnessAllowed: " + minBrightnessAllowed + ", mAmbientLux: " + this.mAmbientLux + ", mReason: " + this.mReason + ", minNitsAllowed: " + minNitsAllowed);
            }
            this.mMinNitsAllowed = minNitsAllowed;
            this.mBrightnessLowerBound = minBrightnessAllowed;
            this.mChangeListener.onChanged();
        }
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessStateModifier
    public void setAmbientLux(float lux) {
        this.mAmbientLux = lux;
        recalculateLowerBound();
    }

    public boolean isActive() {
        return this.mIsActive;
    }

    public int getBrightnessReason() {
        return this.mReason;
    }

    public float getBrightnessLowerBound() {
        return this.mBrightnessLowerBound;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: start, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0() {
        recalculateLowerBound();
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier
    boolean shouldApply(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest request) {
        return this.mIsActive;
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier
    float getBrightnessAdjusted(float currentBrightness, android.hardware.display.DisplayManagerInternal.DisplayPowerRequest request) {
        return java.lang.Math.max(this.mBrightnessLowerBound, currentBrightness);
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier
    int getModifier() {
        return this.mReason;
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier, com.android.server.display.brightness.clamper.BrightnessStateModifier
    public void apply(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest request, com.android.server.display.DisplayBrightnessState.Builder stateBuilder) {
        stateBuilder.setMinBrightness(this.mBrightnessLowerBound);
        float boundedBrightness = java.lang.Math.max(this.mBrightnessLowerBound, stateBuilder.getBrightness());
        stateBuilder.setBrightness(boundedBrightness);
        if (com.android.internal.display.BrightnessSynchronizer.floatEquals(stateBuilder.getBrightness(), this.mBrightnessLowerBound)) {
            stateBuilder.getBrightnessReason().addModifier(this.mReason);
        }
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier, com.android.server.display.brightness.clamper.BrightnessStateModifier
    public void stop() {
        this.mContentResolver.unregisterContentObserver(this.mSettingsObserver);
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessStateModifier
    public boolean shouldListenToLightSensor() {
        return isSettingEnabled();
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier, com.android.server.display.brightness.clamper.BrightnessStateModifier
    public void dump(java.io.PrintWriter pw) {
        pw.println("BrightnessLowLuxModifier:");
        pw.println("  mIsActive=" + this.mIsActive);
        pw.println("  mBrightnessLowerBound=" + this.mBrightnessLowerBound);
        pw.println("  mReason=" + this.mReason);
        pw.println("  mAmbientLux=" + this.mAmbientLux);
        pw.println("  mMinNitsAllowed=" + this.mMinNitsAllowed);
    }

    private boolean isSettingEnabled() {
        return android.provider.Settings.Secure.getFloatForUser(this.mContentResolver, "even_dimmer_activated", 1.0f, -2) == 1.0f;
    }

    private float getBrightnessFromNits(float nits) {
        return this.mDisplayDeviceConfig.getBrightnessFromBacklight(this.mDisplayDeviceConfig.getBacklightFromNits(nits));
    }

    private final class SettingsObserver extends android.database.ContentObserver {
        SettingsObserver(android.os.Handler handler) {
            super(handler);
            com.android.server.display.brightness.clamper.BrightnessLowLuxModifier.this.mContentResolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("even_dimmer_min_nits"), false, this);
            com.android.server.display.brightness.clamper.BrightnessLowLuxModifier.this.mContentResolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("even_dimmer_activated"), false, this);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            com.android.server.display.brightness.clamper.BrightnessLowLuxModifier.this.recalculateLowerBound();
        }
    }
}
