package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public abstract class SettingsHelper {

    public interface UserSettingChangedListener {
        void onSettingChanged(int i);
    }

    public abstract void addAdasAllowlistChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener globalSettingChangedListener);

    public abstract void addIgnoreSettingsAllowlistChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener globalSettingChangedListener);

    public abstract void addOnBackgroundThrottleIntervalChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener globalSettingChangedListener);

    public abstract void addOnBackgroundThrottlePackageWhitelistChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener globalSettingChangedListener);

    public abstract void addOnGnssMeasurementsFullTrackingEnabledChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener globalSettingChangedListener);

    public abstract void addOnLocationEnabledChangedListener(com.android.server.location.injector.SettingsHelper.UserSettingChangedListener userSettingChangedListener);

    public abstract void addOnLocationPackageBlacklistChangedListener(com.android.server.location.injector.SettingsHelper.UserSettingChangedListener userSettingChangedListener);

    public abstract void dump(java.io.FileDescriptor fileDescriptor, android.util.IndentingPrintWriter indentingPrintWriter, java.lang.String[] strArr);

    public abstract android.os.PackageTagsList getAdasAllowlist();

    public abstract long getBackgroundThrottleIntervalMs();

    public abstract java.util.Set<java.lang.String> getBackgroundThrottlePackageWhitelist();

    public abstract long getBackgroundThrottleProximityAlertIntervalMs();

    public abstract float getCoarseLocationAccuracyM();

    public abstract android.os.PackageTagsList getIgnoreSettingsAllowlist();

    public abstract boolean isGnssMeasurementsFullTrackingEnabled();

    public abstract boolean isLocationEnabled(int i);

    public abstract boolean isLocationPackageBlacklisted(int i, java.lang.String str);

    public abstract void removeAdasAllowlistChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener globalSettingChangedListener);

    public abstract void removeIgnoreSettingsAllowlistChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener globalSettingChangedListener);

    public abstract void removeOnBackgroundThrottleIntervalChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener globalSettingChangedListener);

    public abstract void removeOnBackgroundThrottlePackageWhitelistChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener globalSettingChangedListener);

    public abstract void removeOnGnssMeasurementsFullTrackingEnabledChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener globalSettingChangedListener);

    public abstract void removeOnLocationEnabledChangedListener(com.android.server.location.injector.SettingsHelper.UserSettingChangedListener userSettingChangedListener);

    public abstract void removeOnLocationPackageBlacklistChangedListener(com.android.server.location.injector.SettingsHelper.UserSettingChangedListener userSettingChangedListener);

    public abstract void setLocationEnabled(boolean z, int i);

    public interface GlobalSettingChangedListener extends com.android.server.location.injector.SettingsHelper.UserSettingChangedListener {
        void onSettingChanged();

        @Override // com.android.server.location.injector.SettingsHelper.UserSettingChangedListener
        default void onSettingChanged(int userId) {
            onSettingChanged();
        }
    }
}
