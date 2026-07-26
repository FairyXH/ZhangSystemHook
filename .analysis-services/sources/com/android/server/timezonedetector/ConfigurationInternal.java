package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
public final class ConfigurationInternal {
    public static final int DETECTION_MODE_GEO = 2;
    public static final int DETECTION_MODE_MANUAL = 1;
    public static final int DETECTION_MODE_TELEPHONY = 3;
    public static final int DETECTION_MODE_UNKNOWN = 0;
    private final boolean mAutoDetectionEnabledSetting;
    private final boolean mEnhancedMetricsCollectionEnabled;
    private final boolean mGeoDetectionEnabledSetting;
    private final boolean mGeoDetectionRunInBackgroundEnabled;
    private final boolean mGeoDetectionSupported;
    private final boolean mLocationEnabledSetting;
    private final boolean mTelephonyDetectionSupported;
    private final boolean mTelephonyFallbackSupported;
    private final boolean mUserConfigAllowed;
    private final int mUserId;

    @java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface DetectionMode {
    }

    private ConfigurationInternal(com.android.server.timezonedetector.ConfigurationInternal.Builder builder) {
        this.mTelephonyDetectionSupported = builder.mTelephonyDetectionSupported;
        this.mGeoDetectionSupported = builder.mGeoDetectionSupported;
        this.mTelephonyFallbackSupported = builder.mTelephonyFallbackSupported;
        this.mGeoDetectionRunInBackgroundEnabled = builder.mGeoDetectionRunInBackgroundEnabled;
        this.mEnhancedMetricsCollectionEnabled = builder.mEnhancedMetricsCollectionEnabled;
        this.mAutoDetectionEnabledSetting = builder.mAutoDetectionEnabledSetting;
        this.mUserId = ((java.lang.Integer) java.util.Objects.requireNonNull(builder.mUserId, "userId must be set")).intValue();
        this.mUserConfigAllowed = builder.mUserConfigAllowed;
        this.mLocationEnabledSetting = builder.mLocationEnabledSetting;
        this.mGeoDetectionEnabledSetting = builder.mGeoDetectionEnabledSetting;
    }

    public boolean isAutoDetectionSupported() {
        return this.mTelephonyDetectionSupported || this.mGeoDetectionSupported;
    }

    public boolean isTelephonyDetectionSupported() {
        return this.mTelephonyDetectionSupported;
    }

    public boolean isGeoDetectionSupported() {
        return this.mGeoDetectionSupported;
    }

    public boolean isTelephonyFallbackSupported() {
        return this.mTelephonyFallbackSupported;
    }

    boolean getGeoDetectionRunInBackgroundEnabledSetting() {
        return this.mGeoDetectionRunInBackgroundEnabled;
    }

    public boolean isEnhancedMetricsCollectionEnabled() {
        return this.mEnhancedMetricsCollectionEnabled;
    }

    public boolean getAutoDetectionEnabledSetting() {
        return this.mAutoDetectionEnabledSetting;
    }

    public boolean getAutoDetectionEnabledBehavior() {
        return isAutoDetectionSupported() && getAutoDetectionEnabledSetting();
    }

    public int getUserId() {
        return this.mUserId;
    }

    public android.os.UserHandle getUserHandle() {
        return android.os.UserHandle.of(this.mUserId);
    }

    public boolean isUserConfigAllowed() {
        return this.mUserConfigAllowed;
    }

    public boolean getLocationEnabledSetting() {
        return this.mLocationEnabledSetting;
    }

    public boolean getGeoDetectionEnabledSetting() {
        return this.mGeoDetectionEnabledSetting;
    }

    public int getDetectionMode() {
        if (!isAutoDetectionSupported() || !getAutoDetectionEnabledSetting()) {
            return 1;
        }
        if (getGeoDetectionEnabledBehavior()) {
            return 2;
        }
        if (isTelephonyDetectionSupported()) {
            return 3;
        }
        return 0;
    }

    private boolean getGeoDetectionEnabledBehavior() {
        if (isGeoDetectionSupported() && getLocationEnabledSetting()) {
            if (isTelephonyDetectionSupported()) {
                return getGeoDetectionEnabledSetting();
            }
            return true;
        }
        return false;
    }

    public boolean isGeoDetectionExecutionEnabled() {
        return getDetectionMode() == 2 || getGeoDetectionRunInBackgroundEnabledBehavior();
    }

    private boolean getGeoDetectionRunInBackgroundEnabledBehavior() {
        return isGeoDetectionSupported() && getLocationEnabledSetting() && getAutoDetectionEnabledSetting() && getGeoDetectionRunInBackgroundEnabledSetting();
    }

    public android.app.time.TimeZoneCapabilities asCapabilities(boolean bypassUserPolicyChecks) {
        int configureAutoDetectionEnabledCapability;
        int configureGeolocationDetectionEnabledCapability;
        int suggestManualTimeZoneCapability;
        android.os.UserHandle userHandle = android.os.UserHandle.of(this.mUserId);
        android.app.time.TimeZoneCapabilities.Builder builder = new android.app.time.TimeZoneCapabilities.Builder(userHandle);
        boolean allowConfigDateTime = isUserConfigAllowed() || bypassUserPolicyChecks;
        boolean deviceHasAutoTimeZoneDetection = isAutoDetectionSupported();
        if (!deviceHasAutoTimeZoneDetection) {
            configureAutoDetectionEnabledCapability = 10;
        } else if (!allowConfigDateTime) {
            configureAutoDetectionEnabledCapability = 20;
        } else {
            configureAutoDetectionEnabledCapability = 40;
        }
        builder.setConfigureAutoDetectionEnabledCapability(configureAutoDetectionEnabledCapability);
        builder.setUseLocationEnabled(this.mLocationEnabledSetting);
        boolean deviceHasLocationTimeZoneDetection = isGeoDetectionSupported();
        boolean deviceHasTelephonyDetection = isTelephonyDetectionSupported();
        if (!deviceHasLocationTimeZoneDetection || !deviceHasTelephonyDetection) {
            configureGeolocationDetectionEnabledCapability = 10;
        } else if (!this.mAutoDetectionEnabledSetting || !getLocationEnabledSetting()) {
            configureGeolocationDetectionEnabledCapability = 30;
        } else {
            configureGeolocationDetectionEnabledCapability = 40;
        }
        builder.setConfigureGeoDetectionEnabledCapability(configureGeolocationDetectionEnabledCapability);
        if (!allowConfigDateTime) {
            suggestManualTimeZoneCapability = 20;
        } else if (getAutoDetectionEnabledBehavior()) {
            suggestManualTimeZoneCapability = 30;
        } else {
            suggestManualTimeZoneCapability = 40;
        }
        builder.setSetManualTimeZoneCapability(suggestManualTimeZoneCapability);
        return builder.build();
    }

    public android.app.time.TimeZoneConfiguration asConfiguration() {
        return new android.app.time.TimeZoneConfiguration.Builder().setAutoDetectionEnabled(getAutoDetectionEnabledSetting()).setGeoDetectionEnabled(getGeoDetectionEnabledSetting()).build();
    }

    public com.android.server.timezonedetector.ConfigurationInternal merge(android.app.time.TimeZoneConfiguration newConfiguration) {
        com.android.server.timezonedetector.ConfigurationInternal.Builder builder = new com.android.server.timezonedetector.ConfigurationInternal.Builder(this);
        if (newConfiguration.hasIsAutoDetectionEnabled()) {
            builder.setAutoDetectionEnabledSetting(newConfiguration.isAutoDetectionEnabled());
        }
        if (newConfiguration.hasIsGeoDetectionEnabled()) {
            builder.setGeoDetectionEnabledSetting(newConfiguration.isGeoDetectionEnabled());
        }
        return builder.build();
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        com.android.server.timezonedetector.ConfigurationInternal that = (com.android.server.timezonedetector.ConfigurationInternal) o;
        if (this.mUserId == that.mUserId && this.mUserConfigAllowed == that.mUserConfigAllowed && this.mTelephonyDetectionSupported == that.mTelephonyDetectionSupported && this.mGeoDetectionSupported == that.mGeoDetectionSupported && this.mTelephonyFallbackSupported == that.mTelephonyFallbackSupported && this.mGeoDetectionRunInBackgroundEnabled == that.mGeoDetectionRunInBackgroundEnabled && this.mEnhancedMetricsCollectionEnabled == that.mEnhancedMetricsCollectionEnabled && this.mAutoDetectionEnabledSetting == that.mAutoDetectionEnabledSetting && this.mLocationEnabledSetting == that.mLocationEnabledSetting && this.mGeoDetectionEnabledSetting == that.mGeoDetectionEnabledSetting) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(this.mUserId), java.lang.Boolean.valueOf(this.mUserConfigAllowed), java.lang.Boolean.valueOf(this.mTelephonyDetectionSupported), java.lang.Boolean.valueOf(this.mGeoDetectionSupported), java.lang.Boolean.valueOf(this.mTelephonyFallbackSupported), java.lang.Boolean.valueOf(this.mGeoDetectionRunInBackgroundEnabled), java.lang.Boolean.valueOf(this.mEnhancedMetricsCollectionEnabled), java.lang.Boolean.valueOf(this.mAutoDetectionEnabledSetting), java.lang.Boolean.valueOf(this.mLocationEnabledSetting), java.lang.Boolean.valueOf(this.mGeoDetectionEnabledSetting));
    }

    public java.lang.String toString() {
        return "ConfigurationInternal{mUserId=" + this.mUserId + ", mUserConfigAllowed=" + this.mUserConfigAllowed + ", mTelephonyDetectionSupported=" + this.mTelephonyDetectionSupported + ", mGeoDetectionSupported=" + this.mGeoDetectionSupported + ", mTelephonyFallbackSupported=" + this.mTelephonyFallbackSupported + ", mGeoDetectionRunInBackgroundEnabled=" + this.mGeoDetectionRunInBackgroundEnabled + ", mEnhancedMetricsCollectionEnabled=" + this.mEnhancedMetricsCollectionEnabled + ", mAutoDetectionEnabledSetting=" + this.mAutoDetectionEnabledSetting + ", mLocationEnabledSetting=" + this.mLocationEnabledSetting + ", mGeoDetectionEnabledSetting=" + this.mGeoDetectionEnabledSetting + '}';
    }

    public static class Builder {
        private boolean mAutoDetectionEnabledSetting;
        private boolean mEnhancedMetricsCollectionEnabled;
        private boolean mGeoDetectionEnabledSetting;
        private boolean mGeoDetectionRunInBackgroundEnabled;
        private boolean mGeoDetectionSupported;
        private boolean mLocationEnabledSetting;
        private boolean mTelephonyDetectionSupported;
        private boolean mTelephonyFallbackSupported;
        private boolean mUserConfigAllowed;
        private java.lang.Integer mUserId;

        public Builder() {
        }

        public Builder(com.android.server.timezonedetector.ConfigurationInternal toCopy) {
            this.mUserId = java.lang.Integer.valueOf(toCopy.mUserId);
            this.mUserConfigAllowed = toCopy.mUserConfigAllowed;
            this.mTelephonyDetectionSupported = toCopy.mTelephonyDetectionSupported;
            this.mTelephonyFallbackSupported = toCopy.mTelephonyFallbackSupported;
            this.mGeoDetectionSupported = toCopy.mGeoDetectionSupported;
            this.mGeoDetectionRunInBackgroundEnabled = toCopy.mGeoDetectionRunInBackgroundEnabled;
            this.mEnhancedMetricsCollectionEnabled = toCopy.mEnhancedMetricsCollectionEnabled;
            this.mAutoDetectionEnabledSetting = toCopy.mAutoDetectionEnabledSetting;
            this.mLocationEnabledSetting = toCopy.mLocationEnabledSetting;
            this.mGeoDetectionEnabledSetting = toCopy.mGeoDetectionEnabledSetting;
        }

        public com.android.server.timezonedetector.ConfigurationInternal.Builder setUserId(int userId) {
            this.mUserId = java.lang.Integer.valueOf(userId);
            return this;
        }

        public com.android.server.timezonedetector.ConfigurationInternal.Builder setUserConfigAllowed(boolean configAllowed) {
            this.mUserConfigAllowed = configAllowed;
            return this;
        }

        public com.android.server.timezonedetector.ConfigurationInternal.Builder setTelephonyDetectionFeatureSupported(boolean supported) {
            this.mTelephonyDetectionSupported = supported;
            return this;
        }

        public com.android.server.timezonedetector.ConfigurationInternal.Builder setGeoDetectionFeatureSupported(boolean supported) {
            this.mGeoDetectionSupported = supported;
            return this;
        }

        public com.android.server.timezonedetector.ConfigurationInternal.Builder setTelephonyFallbackSupported(boolean supported) {
            this.mTelephonyFallbackSupported = supported;
            return this;
        }

        public com.android.server.timezonedetector.ConfigurationInternal.Builder setGeoDetectionRunInBackgroundEnabled(boolean enabled) {
            this.mGeoDetectionRunInBackgroundEnabled = enabled;
            return this;
        }

        public com.android.server.timezonedetector.ConfigurationInternal.Builder setEnhancedMetricsCollectionEnabled(boolean enabled) {
            this.mEnhancedMetricsCollectionEnabled = enabled;
            return this;
        }

        public com.android.server.timezonedetector.ConfigurationInternal.Builder setAutoDetectionEnabledSetting(boolean enabled) {
            this.mAutoDetectionEnabledSetting = enabled;
            return this;
        }

        public com.android.server.timezonedetector.ConfigurationInternal.Builder setLocationEnabledSetting(boolean enabled) {
            this.mLocationEnabledSetting = enabled;
            return this;
        }

        public com.android.server.timezonedetector.ConfigurationInternal.Builder setGeoDetectionEnabledSetting(boolean enabled) {
            this.mGeoDetectionEnabledSetting = enabled;
            return this;
        }

        public com.android.server.timezonedetector.ConfigurationInternal build() {
            return new com.android.server.timezonedetector.ConfigurationInternal(this);
        }
    }
}
