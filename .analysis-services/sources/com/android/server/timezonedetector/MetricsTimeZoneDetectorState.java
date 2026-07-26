package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
public final class MetricsTimeZoneDetectorState {
    public static final int DETECTION_MODE_GEO = 2;
    public static final int DETECTION_MODE_MANUAL = 1;
    public static final int DETECTION_MODE_TELEPHONY = 3;
    public static final int DETECTION_MODE_UNKNOWN = 0;
    private final com.android.server.timezonedetector.ConfigurationInternal mConfigurationInternal;
    private final java.lang.String mDeviceTimeZoneId;
    private final int mDeviceTimeZoneIdOrdinal;
    private final com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion mLatestGeolocationSuggestion;
    private final com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion mLatestManualSuggestion;
    private final com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion mLatestTelephonySuggestion;

    @java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface DetectionMode {
    }

    private MetricsTimeZoneDetectorState(com.android.server.timezonedetector.ConfigurationInternal configurationInternal, int deviceTimeZoneIdOrdinal, java.lang.String deviceTimeZoneId, com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion latestManualSuggestion, com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion latestTelephonySuggestion, com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion latestGeolocationSuggestion) {
        this.mConfigurationInternal = (com.android.server.timezonedetector.ConfigurationInternal) java.util.Objects.requireNonNull(configurationInternal);
        this.mDeviceTimeZoneIdOrdinal = deviceTimeZoneIdOrdinal;
        this.mDeviceTimeZoneId = deviceTimeZoneId;
        this.mLatestManualSuggestion = latestManualSuggestion;
        this.mLatestTelephonySuggestion = latestTelephonySuggestion;
        this.mLatestGeolocationSuggestion = latestGeolocationSuggestion;
    }

    public static com.android.server.timezonedetector.MetricsTimeZoneDetectorState create(com.android.server.timezonedetector.OrdinalGenerator<java.lang.String> tzIdOrdinalGenerator, com.android.server.timezonedetector.ConfigurationInternal configurationInternal, java.lang.String deviceTimeZoneId, android.app.timezonedetector.ManualTimeZoneSuggestion latestManualSuggestion, android.app.timezonedetector.TelephonyTimeZoneSuggestion latestTelephonySuggestion, com.android.server.timezonedetector.LocationAlgorithmEvent latestLocationAlgorithmEvent) {
        com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion latestCanonicalGeolocationSuggestion;
        boolean includeZoneIds = configurationInternal.isEnhancedMetricsCollectionEnabled();
        java.lang.String metricDeviceTimeZoneId = includeZoneIds ? deviceTimeZoneId : null;
        int deviceTimeZoneIdOrdinal = tzIdOrdinalGenerator.ordinal((java.lang.String) java.util.Objects.requireNonNull(deviceTimeZoneId));
        com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion latestCanonicalManualSuggestion = createMetricsTimeZoneSuggestion(tzIdOrdinalGenerator, latestManualSuggestion, includeZoneIds);
        com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion latestCanonicalTelephonySuggestion = createMetricsTimeZoneSuggestion(tzIdOrdinalGenerator, latestTelephonySuggestion, includeZoneIds);
        if (latestLocationAlgorithmEvent == null) {
            latestCanonicalGeolocationSuggestion = null;
        } else {
            com.android.server.timezonedetector.GeolocationTimeZoneSuggestion suggestion = latestLocationAlgorithmEvent.getSuggestion();
            com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion latestCanonicalGeolocationSuggestion2 = createMetricsTimeZoneSuggestion(tzIdOrdinalGenerator, suggestion, includeZoneIds);
            latestCanonicalGeolocationSuggestion = latestCanonicalGeolocationSuggestion2;
        }
        return new com.android.server.timezonedetector.MetricsTimeZoneDetectorState(configurationInternal, deviceTimeZoneIdOrdinal, metricDeviceTimeZoneId, latestCanonicalManualSuggestion, latestCanonicalTelephonySuggestion, latestCanonicalGeolocationSuggestion);
    }

    public boolean isTelephonyDetectionSupported() {
        return this.mConfigurationInternal.isTelephonyDetectionSupported();
    }

    public boolean isGeoDetectionSupported() {
        return this.mConfigurationInternal.isGeoDetectionSupported();
    }

    public boolean isTelephonyTimeZoneFallbackSupported() {
        return this.mConfigurationInternal.isTelephonyFallbackSupported();
    }

    public boolean getGeoDetectionRunInBackgroundEnabled() {
        return this.mConfigurationInternal.getGeoDetectionRunInBackgroundEnabledSetting();
    }

    public boolean isEnhancedMetricsCollectionEnabled() {
        return this.mConfigurationInternal.isEnhancedMetricsCollectionEnabled();
    }

    public boolean getUserLocationEnabledSetting() {
        return this.mConfigurationInternal.getLocationEnabledSetting();
    }

    public boolean getGeoDetectionEnabledSetting() {
        return this.mConfigurationInternal.getGeoDetectionEnabledSetting();
    }

    public boolean getAutoDetectionEnabledSetting() {
        return this.mConfigurationInternal.getAutoDetectionEnabledSetting();
    }

    public int getDetectionMode() {
        switch (this.mConfigurationInternal.getDetectionMode()) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return 0;
        }
    }

    public int getDeviceTimeZoneIdOrdinal() {
        return this.mDeviceTimeZoneIdOrdinal;
    }

    public java.lang.String getDeviceTimeZoneId() {
        return this.mDeviceTimeZoneId;
    }

    public com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion getLatestManualSuggestion() {
        return this.mLatestManualSuggestion;
    }

    public com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion getLatestTelephonySuggestion() {
        return this.mLatestTelephonySuggestion;
    }

    public com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion getLatestGeolocationSuggestion() {
        return this.mLatestGeolocationSuggestion;
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        com.android.server.timezonedetector.MetricsTimeZoneDetectorState that = (com.android.server.timezonedetector.MetricsTimeZoneDetectorState) o;
        if (this.mDeviceTimeZoneIdOrdinal == that.mDeviceTimeZoneIdOrdinal && java.util.Objects.equals(this.mDeviceTimeZoneId, that.mDeviceTimeZoneId) && this.mConfigurationInternal.equals(that.mConfigurationInternal) && java.util.Objects.equals(this.mLatestManualSuggestion, that.mLatestManualSuggestion) && java.util.Objects.equals(this.mLatestTelephonySuggestion, that.mLatestTelephonySuggestion) && java.util.Objects.equals(this.mLatestGeolocationSuggestion, that.mLatestGeolocationSuggestion)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return java.util.Objects.hash(this.mConfigurationInternal, java.lang.Integer.valueOf(this.mDeviceTimeZoneIdOrdinal), this.mDeviceTimeZoneId, this.mLatestManualSuggestion, this.mLatestTelephonySuggestion, this.mLatestGeolocationSuggestion);
    }

    public java.lang.String toString() {
        return "MetricsTimeZoneDetectorState{mConfigurationInternal=" + this.mConfigurationInternal + ", mDeviceTimeZoneIdOrdinal=" + this.mDeviceTimeZoneIdOrdinal + ", mDeviceTimeZoneId=" + this.mDeviceTimeZoneId + ", mLatestManualSuggestion=" + this.mLatestManualSuggestion + ", mLatestTelephonySuggestion=" + this.mLatestTelephonySuggestion + ", mLatestGeolocationSuggestion=" + this.mLatestGeolocationSuggestion + '}';
    }

    private static com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion createMetricsTimeZoneSuggestion(com.android.server.timezonedetector.OrdinalGenerator<java.lang.String> zoneIdOrdinalGenerator, android.app.timezonedetector.ManualTimeZoneSuggestion manualSuggestion, boolean includeFullZoneIds) {
        if (manualSuggestion == null) {
            return null;
        }
        java.lang.String suggestionZoneId = manualSuggestion.getZoneId();
        java.lang.String[] metricZoneIds = includeFullZoneIds ? new java.lang.String[]{suggestionZoneId} : null;
        int[] zoneIdOrdinals = {zoneIdOrdinalGenerator.ordinal(suggestionZoneId)};
        return com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion.createCertain(metricZoneIds, zoneIdOrdinals);
    }

    private static com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion createMetricsTimeZoneSuggestion(com.android.server.timezonedetector.OrdinalGenerator<java.lang.String> zoneIdOrdinalGenerator, android.app.timezonedetector.TelephonyTimeZoneSuggestion telephonySuggestion, boolean includeFullZoneIds) {
        if (telephonySuggestion == null) {
            return null;
        }
        java.lang.String suggestionZoneId = telephonySuggestion.getZoneId();
        if (suggestionZoneId == null) {
            return com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion.createUncertain();
        }
        java.lang.String[] metricZoneIds = includeFullZoneIds ? new java.lang.String[]{suggestionZoneId} : null;
        int[] zoneIdOrdinals = {zoneIdOrdinalGenerator.ordinal(suggestionZoneId)};
        return com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion.createCertain(metricZoneIds, zoneIdOrdinals);
    }

    private static com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion createMetricsTimeZoneSuggestion(com.android.server.timezonedetector.OrdinalGenerator<java.lang.String> zoneIdOrdinalGenerator, com.android.server.timezonedetector.GeolocationTimeZoneSuggestion geolocationSuggestion, boolean includeFullZoneIds) {
        if (geolocationSuggestion == null) {
            return null;
        }
        java.util.List<java.lang.String> zoneIds = geolocationSuggestion.getZoneIds();
        if (zoneIds == null) {
            return com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion.createUncertain();
        }
        java.lang.String[] metricZoneIds = includeFullZoneIds ? (java.lang.String[]) zoneIds.toArray(new java.lang.String[0]) : null;
        int[] zoneIdOrdinals = zoneIdOrdinalGenerator.ordinals(zoneIds);
        return com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion.createCertain(metricZoneIds, zoneIdOrdinals);
    }

    public static final class MetricsTimeZoneSuggestion {
        private final int[] mZoneIdOrdinals;
        private final java.lang.String[] mZoneIds;

        private MetricsTimeZoneSuggestion(java.lang.String[] zoneIds, int[] zoneIdOrdinals) {
            this.mZoneIds = zoneIds;
            this.mZoneIdOrdinals = zoneIdOrdinals;
        }

        static com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion createUncertain() {
            return new com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion(null, null);
        }

        static com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion createCertain(java.lang.String[] zoneIds, int[] zoneIdOrdinals) {
            return new com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion(zoneIds, zoneIdOrdinals);
        }

        public boolean isCertain() {
            return this.mZoneIdOrdinals != null;
        }

        public int[] getZoneIdOrdinals() {
            return this.mZoneIdOrdinals;
        }

        public java.lang.String[] getZoneIds() {
            return this.mZoneIds;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion that = (com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion) o;
            if (java.util.Arrays.equals(this.mZoneIdOrdinals, that.mZoneIdOrdinals) && java.util.Arrays.equals(this.mZoneIds, that.mZoneIds)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int result = java.util.Arrays.hashCode(this.mZoneIds);
            return (result * 31) + java.util.Arrays.hashCode(this.mZoneIdOrdinals);
        }

        public java.lang.String toString() {
            return "MetricsTimeZoneSuggestion{mZoneIdOrdinals=" + java.util.Arrays.toString(this.mZoneIdOrdinals) + ", mZoneIds=" + java.util.Arrays.toString(this.mZoneIds) + '}';
        }
    }
}
