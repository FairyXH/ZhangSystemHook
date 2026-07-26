package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
public interface ServiceConfigAccessor {
    public static final java.lang.String PROVIDER_MODE_DISABLED = "disabled";
    public static final java.lang.String PROVIDER_MODE_ENABLED = "enabled";

    @java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ProviderMode {
    }

    void addConfigurationInternalChangeListener(com.android.server.timezonedetector.StateChangeListener stateChangeListener);

    void addLocationTimeZoneManagerConfigListener(com.android.server.timezonedetector.StateChangeListener stateChangeListener);

    com.android.server.timezonedetector.ConfigurationInternal getConfigurationInternal(int i);

    com.android.server.timezonedetector.ConfigurationInternal getCurrentUserConfigurationInternal();

    java.util.Optional<java.lang.Boolean> getGeoDetectionSettingEnabledOverride();

    java.time.Duration getLocationTimeZoneProviderEventFilteringAgeThreshold();

    java.time.Duration getLocationTimeZoneProviderInitializationTimeout();

    java.time.Duration getLocationTimeZoneProviderInitializationTimeoutFuzz();

    java.time.Duration getLocationTimeZoneUncertaintyDelay();

    java.lang.String getPrimaryLocationTimeZoneProviderMode();

    java.lang.String getPrimaryLocationTimeZoneProviderPackageName();

    boolean getRecordStateChangesForTests();

    java.lang.String getSecondaryLocationTimeZoneProviderMode();

    java.lang.String getSecondaryLocationTimeZoneProviderPackageName();

    boolean isGeoDetectionEnabledForUsersByDefault();

    boolean isGeoTimeZoneDetectionFeatureSupported();

    boolean isGeoTimeZoneDetectionFeatureSupportedInConfig();

    boolean isTelephonyTimeZoneDetectionFeatureSupported();

    boolean isTestPrimaryLocationTimeZoneProvider();

    boolean isTestSecondaryLocationTimeZoneProvider();

    void removeConfigurationInternalChangeListener(com.android.server.timezonedetector.StateChangeListener stateChangeListener);

    void resetVolatileTestConfig();

    void setRecordStateChangesForTests(boolean z);

    void setTestPrimaryLocationTimeZoneProviderPackageName(java.lang.String str);

    void setTestSecondaryLocationTimeZoneProviderPackageName(java.lang.String str);

    boolean updateConfiguration(int i, android.app.time.TimeZoneConfiguration timeZoneConfiguration, boolean z);
}
