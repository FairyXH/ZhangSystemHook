package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public class FoldSettingProvider {
    private static final java.lang.String SETTING_VALUE_DEFAULT = "selective_stay_awake_key";
    public static final java.lang.String SETTING_VALUE_SELECTIVE_STAY_AWAKE = "selective_stay_awake_key";
    private static final java.lang.String TAG = "FoldSettingProvider";
    private final android.content.ContentResolver mContentResolver;
    private final com.android.internal.foldables.FoldLockSettingAvailabilityProvider mFoldLockSettingAvailabilityProvider;
    private final com.android.internal.util.SettingsWrapper mSettingsWrapper;
    public static final java.lang.String SETTING_VALUE_STAY_AWAKE_ON_FOLD = "stay_awake_on_fold_key";
    public static final java.lang.String SETTING_VALUE_SLEEP_ON_FOLD = "sleep_on_fold_key";
    private static final java.util.Set<java.lang.String> SETTING_VALUES = java.util.Set.of(SETTING_VALUE_STAY_AWAKE_ON_FOLD, "selective_stay_awake_key", SETTING_VALUE_SLEEP_ON_FOLD);

    public FoldSettingProvider(android.content.Context context, com.android.internal.util.SettingsWrapper settingsWrapper, com.android.internal.foldables.FoldLockSettingAvailabilityProvider foldLockSettingAvailabilityProvider) {
        this.mContentResolver = context.getContentResolver();
        this.mSettingsWrapper = settingsWrapper;
        this.mFoldLockSettingAvailabilityProvider = foldLockSettingAvailabilityProvider;
    }

    public boolean shouldStayAwakeOnFold() {
        return getFoldSettingValue().equals(SETTING_VALUE_STAY_AWAKE_ON_FOLD);
    }

    public boolean shouldSelectiveStayAwakeOnFold() {
        return getFoldSettingValue().equals("selective_stay_awake_key");
    }

    public boolean shouldSleepOnFold() {
        return getFoldSettingValue().equals(SETTING_VALUE_SLEEP_ON_FOLD);
    }

    private java.lang.String getFoldSettingValue() {
        boolean isFoldLockBehaviorAvailable = this.mFoldLockSettingAvailabilityProvider.isFoldLockBehaviorAvailable();
        if (!isFoldLockBehaviorAvailable) {
            return "selective_stay_awake_key";
        }
        java.lang.String foldSettingValue = this.mSettingsWrapper.getStringForUser(this.mContentResolver, "fold_lock_behavior_setting", -2);
        java.lang.String foldSettingValue2 = foldSettingValue != null ? foldSettingValue : "selective_stay_awake_key";
        if (!SETTING_VALUES.contains(foldSettingValue2)) {
            android.util.Log.e(TAG, "getFoldSettingValue: Invalid setting value, returning default setting value");
            return "selective_stay_awake_key";
        }
        return foldSettingValue2;
    }
}
