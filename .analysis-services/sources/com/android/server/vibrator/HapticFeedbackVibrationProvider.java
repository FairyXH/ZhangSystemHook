package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
public final class HapticFeedbackVibrationProvider {
    private static final java.lang.String TAG = "HapticFeedbackVibrationProvider";
    private final android.util.SparseArray<android.os.VibrationEffect> mHapticCustomizations;
    private final com.android.server.vibrator.IHapticFeedbackVibrationProviderExt mHapticFeedbackVibrationProviderExt;
    private final boolean mHapticTextHandleEnabled;
    private float mKeyboardVibrationFixedAmplitude;
    private final android.os.VibrationEffect mSafeModeEnabledVibrationEffect;
    private final android.os.VibratorInfo mVibratorInfo;
    private static final android.os.VibrationAttributes TOUCH_VIBRATION_ATTRIBUTES = android.os.VibrationAttributes.createForUsage(18);
    private static final android.os.VibrationAttributes PHYSICAL_EMULATION_VIBRATION_ATTRIBUTES = android.os.VibrationAttributes.createForUsage(34);
    private static final android.os.VibrationAttributes HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES = android.os.VibrationAttributes.createForUsage(50);
    private static final android.os.VibrationAttributes COMMUNICATION_REQUEST_VIBRATION_ATTRIBUTES = android.os.VibrationAttributes.createForUsage(65);

    public HapticFeedbackVibrationProvider(android.content.res.Resources res, android.os.Vibrator vibrator) {
        this(res, vibrator.getInfo());
    }

    public HapticFeedbackVibrationProvider(android.content.res.Resources res, android.os.VibratorInfo vibratorInfo) {
        this(res, vibratorInfo, loadHapticCustomizations(res, vibratorInfo));
    }

    HapticFeedbackVibrationProvider(android.content.res.Resources res, android.os.VibratorInfo vibratorInfo, android.util.SparseArray<android.os.VibrationEffect> hapticCustomizations) {
        android.os.VibrationEffect vibrationEffectCreateEffectFromResource;
        this.mHapticFeedbackVibrationProviderExt = (com.android.server.vibrator.IHapticFeedbackVibrationProviderExt) system.ext.loader.core.ExtLoader.type(com.android.server.vibrator.IHapticFeedbackVibrationProviderExt.class).create();
        this.mVibratorInfo = vibratorInfo;
        this.mHapticTextHandleEnabled = res.getBoolean(android.R.bool.config_enableGeocoderOverlay);
        if (hapticCustomizations != null && hapticCustomizations.size() == 0) {
            hapticCustomizations = null;
        }
        this.mHapticCustomizations = hapticCustomizations;
        if (effectHasCustomization(10001)) {
            vibrationEffectCreateEffectFromResource = this.mHapticCustomizations.get(10001);
        } else {
            vibrationEffectCreateEffectFromResource = com.android.server.vibrator.VibrationSettings.createEffectFromResource(res, android.R.array.config_ringtoneEffectUris);
        }
        this.mSafeModeEnabledVibrationEffect = vibrationEffectCreateEffectFromResource;
        this.mKeyboardVibrationFixedAmplitude = res.getFloat(android.R.dimen.config_appTransitionAnimationDurationScaleDefault);
        if (this.mKeyboardVibrationFixedAmplitude < 0.0f || this.mKeyboardVibrationFixedAmplitude > 1.0f) {
            this.mKeyboardVibrationFixedAmplitude = -1.0f;
        }
        this.mHapticFeedbackVibrationProviderExt.init();
    }

    public android.os.VibrationEffect getVibrationForHapticFeedback(int effectId) {
        android.os.VibrationEffect effect = this.mHapticFeedbackVibrationProviderExt.getOverrideVibrationForHapticFeedback(effectId);
        if (effect != null) {
            return effect;
        }
        switch (effectId) {
            case 0:
            case 14:
            case 25:
            case 10003:
                return getVibration(effectId, 5);
            case 1:
            case 5:
            case 12:
            case 15:
            case 16:
            case 19:
            case 20:
            case 10004:
                return getVibration(effectId, 0);
            case 3:
            case 7:
                return getKeyboardVibration(effectId);
            case 4:
            case 27:
                break;
            case 6:
            case 13:
            case 18:
            case 23:
            case 26:
                return getVibration(effectId, 2);
            case 8:
            case 11:
                return getVibration(effectId, 2, false);
            case 9:
                if (!this.mHapticTextHandleEnabled) {
                    return null;
                }
                break;
            case 17:
            case 10005:
                return getVibration(effectId, 1);
            case 21:
                return getVibration(effectId, 7, 0.5f, 2);
            case 22:
                return getVibration(effectId, 8, 0.2f, 21);
            case 24:
                return getVibration(effectId, 7, 0.4f, 21);
            case 10001:
                return this.mSafeModeEnabledVibrationEffect;
            case 10002:
                return getAssistantButtonVibration();
            default:
                return null;
        }
        return getVibration(effectId, 21);
    }

    public android.os.VibrationAttributes getVibrationAttributesForHapticFeedback(int effectId, boolean bypassVibrationIntensitySetting, boolean fromIme) {
        android.os.VibrationAttributes attrs;
        switch (effectId) {
            case 3:
            case 7:
                attrs = createKeyboardVibrationAttributes(fromIme);
                break;
            case 14:
            case 15:
                attrs = PHYSICAL_EMULATION_VIBRATION_ATTRIBUTES;
                break;
            case 18:
            case 19:
            case 20:
            case 10002:
            case 10003:
                attrs = HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES;
                break;
            case 10004:
            case 10005:
                attrs = COMMUNICATION_REQUEST_VIBRATION_ATTRIBUTES;
                break;
            default:
                attrs = TOUCH_VIBRATION_ATTRIBUTES;
                break;
        }
        int flags = 0;
        if (bypassVibrationIntensitySetting) {
            flags = 0 | 2;
        }
        if (shouldBypassInterruptionPolicy(effectId)) {
            flags |= 1;
        }
        if (shouldBypassIntensityScale(effectId, fromIme)) {
            flags |= 16;
        }
        return flags == 0 ? attrs : new android.os.VibrationAttributes.Builder(attrs).setFlags(flags).build();
    }

    public boolean isRestrictedHapticFeedback(int effectId) {
        switch (effectId) {
            case 10004:
            case 10005:
                return true;
            default:
                return false;
        }
    }

    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.print("mHapticTextHandleEnabled=");
        pw.println(this.mHapticTextHandleEnabled);
    }

    private android.os.VibrationEffect getVibration(int effectId, int predefinedVibrationEffectId) {
        return getVibration(effectId, predefinedVibrationEffectId, true);
    }

    private android.os.VibrationEffect getVibration(int hapticFeedbackId, int predefinedVibrationEffectId, boolean fallbackForPredefinedEffect) {
        if (effectHasCustomization(hapticFeedbackId)) {
            return this.mHapticCustomizations.get(hapticFeedbackId);
        }
        return android.os.VibrationEffect.get(predefinedVibrationEffectId, fallbackForPredefinedEffect);
    }

    private android.os.VibrationEffect getVibration(int hapticFeedbackId, int primitiveId, float primitiveScale, int elsePredefinedVibrationEffectId) {
        if (effectHasCustomization(hapticFeedbackId)) {
            return this.mHapticCustomizations.get(hapticFeedbackId);
        }
        if (this.mVibratorInfo.isPrimitiveSupported(primitiveId)) {
            return android.os.VibrationEffect.startComposition().addPrimitive(primitiveId, primitiveScale).compose();
        }
        return android.os.VibrationEffect.get(elsePredefinedVibrationEffectId);
    }

    private android.os.VibrationEffect getAssistantButtonVibration() {
        if (effectHasCustomization(10002)) {
            return this.mHapticCustomizations.get(10002);
        }
        if (this.mVibratorInfo.isPrimitiveSupported(4) && this.mVibratorInfo.isPrimitiveSupported(7)) {
            return android.os.VibrationEffect.startComposition().addPrimitive(4, 0.25f).addPrimitive(7, 1.0f, 50).compose();
        }
        return android.os.VibrationEffect.get(5);
    }

    private boolean effectHasCustomization(int effectId) {
        return this.mHapticCustomizations != null && this.mHapticCustomizations.contains(effectId);
    }

    private android.os.VibrationEffect getKeyboardVibration(int effectId) {
        int primitiveId;
        int predefinedEffectId;
        boolean predefinedEffectFallback;
        if (effectHasCustomization(effectId)) {
            return this.mHapticCustomizations.get(effectId);
        }
        switch (effectId) {
            case 7:
                primitiveId = 7;
                predefinedEffectId = 2;
                predefinedEffectFallback = false;
                break;
            default:
                primitiveId = 1;
                predefinedEffectId = 0;
                predefinedEffectFallback = true;
                break;
        }
        if (android.os.vibrator.Flags.keyboardCategoryEnabled() && this.mKeyboardVibrationFixedAmplitude > 0.0f && this.mVibratorInfo.isPrimitiveSupported(primitiveId)) {
            return android.os.VibrationEffect.startComposition().addPrimitive(primitiveId, this.mKeyboardVibrationFixedAmplitude).compose();
        }
        return getVibration(effectId, predefinedEffectId, predefinedEffectFallback);
    }

    private boolean shouldBypassIntensityScale(int effectId, boolean isIme) {
        if (!android.os.vibrator.Flags.keyboardCategoryEnabled() || this.mKeyboardVibrationFixedAmplitude < 0.0f || !isIme) {
            return false;
        }
        switch (effectId) {
            case 3:
                return this.mVibratorInfo.isPrimitiveSupported(1);
            case 7:
                return this.mVibratorInfo.isPrimitiveSupported(7);
            default:
                return false;
        }
    }

    private android.os.VibrationAttributes createKeyboardVibrationAttributes(boolean fromIme) {
        if (!android.os.vibrator.Flags.keyboardCategoryEnabled() || !fromIme) {
            return TOUCH_VIBRATION_ATTRIBUTES;
        }
        return new android.os.VibrationAttributes.Builder(TOUCH_VIBRATION_ATTRIBUTES).setCategory(1).build();
    }

    private static android.util.SparseArray<android.os.VibrationEffect> loadHapticCustomizations(android.content.res.Resources res, android.os.VibratorInfo vibratorInfo) {
        try {
            return com.android.server.vibrator.HapticFeedbackCustomization.loadVibrations(res, vibratorInfo);
        } catch (com.android.server.vibrator.HapticFeedbackCustomization.CustomizationParserException | java.io.IOException e) {
            android.util.Slog.e(TAG, "Unable to load haptic customizations.", e);
            return null;
        }
    }

    private static boolean shouldBypassInterruptionPolicy(int effectId) {
        switch (effectId) {
            case 18:
            case 19:
            case 20:
                return android.view.flags.Flags.scrollFeedbackApi();
            default:
                return false;
        }
    }
}
