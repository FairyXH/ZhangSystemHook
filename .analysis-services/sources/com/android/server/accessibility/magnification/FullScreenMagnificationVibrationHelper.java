package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
public class FullScreenMagnificationVibrationHelper {
    private static final int VIBRATION_AMPLITUDE = 127;
    private static final long VIBRATION_DURATION_MS = 10;
    private final android.content.ContentResolver mContentResolver;
    private final android.os.Vibrator mVibrator;
    private final android.os.VibrationEffect mVibrationEffect = android.os.VibrationEffect.get(0);
    com.android.server.accessibility.magnification.FullScreenMagnificationVibrationHelper.VibrationEffectSupportedProvider mIsVibrationEffectSupportedProvider = new com.android.server.accessibility.magnification.FullScreenMagnificationVibrationHelper.VibrationEffectSupportedProvider() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationVibrationHelper$$ExternalSyntheticLambda0
        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationVibrationHelper.VibrationEffectSupportedProvider
        public final boolean isVibrationEffectSupported() {
            return this.f$0.lambda$new$0();
        }
    };

    interface VibrationEffectSupportedProvider {
        boolean isVibrationEffectSupported();
    }

    public FullScreenMagnificationVibrationHelper(android.content.Context context) {
        this.mContentResolver = context.getContentResolver();
        this.mVibrator = (android.os.Vibrator) context.getSystemService(android.os.Vibrator.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0() {
        return this.mVibrator != null && this.mVibrator.areAllEffectsSupported(0) == 1;
    }

    void vibrateIfSettingEnabled() {
        if (this.mVibrator != null && this.mVibrator.hasVibrator() && isEdgeHapticSettingEnabled()) {
            if (this.mIsVibrationEffectSupportedProvider.isVibrationEffectSupported()) {
                this.mVibrator.vibrate(this.mVibrationEffect);
            } else {
                this.mVibrator.vibrate(android.os.VibrationEffect.createOneShot(VIBRATION_DURATION_MS, 127));
            }
        }
    }

    private boolean isEdgeHapticSettingEnabled() {
        return android.provider.Settings.Secure.getIntForUser(this.mContentResolver, "accessibility_display_magnification_edge_haptic_enabled", 0, -2) == 1;
    }
}
