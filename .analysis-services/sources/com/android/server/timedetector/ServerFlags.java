package com.android.server.timedetector;

/* JADX INFO: loaded from: classes3.dex */
public final class ServerFlags {
    public static final java.lang.String KEY_ENHANCED_METRICS_COLLECTION_ENABLED = "enhanced_metrics_collection_enabled";
    public static final java.lang.String KEY_LOCATION_TIME_ZONE_DETECTION_FEATURE_SUPPORTED = "location_time_zone_detection_feature_supported";
    public static final java.lang.String KEY_LOCATION_TIME_ZONE_DETECTION_RUN_IN_BACKGROUND_ENABLED = "location_time_zone_detection_run_in_background_enabled";
    public static final java.lang.String KEY_LOCATION_TIME_ZONE_DETECTION_SETTING_ENABLED_DEFAULT = "location_time_zone_detection_setting_enabled_default";
    public static final java.lang.String KEY_LOCATION_TIME_ZONE_DETECTION_SETTING_ENABLED_OVERRIDE = "location_time_zone_detection_setting_enabled_override";
    public static final java.lang.String KEY_LOCATION_TIME_ZONE_DETECTION_UNCERTAINTY_DELAY_MILLIS = "location_time_zone_detection_uncertainty_delay_millis";
    public static final java.lang.String KEY_LTZP_EVENT_FILTERING_AGE_THRESHOLD_MILLIS = "ltzp_event_filtering_age_threshold_millis";
    public static final java.lang.String KEY_LTZP_INITIALIZATION_TIMEOUT_FUZZ_MILLIS = "ltzp_init_timeout_fuzz_millis";
    public static final java.lang.String KEY_LTZP_INITIALIZATION_TIMEOUT_MILLIS = "ltzp_init_timeout_millis";
    public static final java.lang.String KEY_PRIMARY_LTZP_MODE_OVERRIDE = "primary_location_time_zone_provider_mode_override";
    public static final java.lang.String KEY_SECONDARY_LTZP_MODE_OVERRIDE = "secondary_location_time_zone_provider_mode_override";
    public static final java.lang.String KEY_TIME_DETECTOR_LOWER_BOUND_MILLIS_OVERRIDE = "time_detector_lower_bound_millis_override";
    public static final java.lang.String KEY_TIME_DETECTOR_ORIGIN_PRIORITIES_OVERRIDE = "time_detector_origin_priorities_override";
    public static final java.lang.String KEY_TIME_ZONE_DETECTOR_AUTO_DETECTION_ENABLED_DEFAULT = "time_zone_detector_auto_detection_enabled_default";
    public static final java.lang.String KEY_TIME_ZONE_DETECTOR_TELEPHONY_FALLBACK_SUPPORTED = "time_zone_detector_telephony_fallback_supported";
    private static com.android.server.timedetector.ServerFlags sInstance;
    private final android.util.ArrayMap<com.android.server.timezonedetector.StateChangeListener, java.util.HashSet<java.lang.String>> mListeners = new android.util.ArrayMap<>();
    private static final java.util.Optional<java.lang.Boolean> OPTIONAL_TRUE = java.util.Optional.of(true);
    private static final java.util.Optional<java.lang.Boolean> OPTIONAL_FALSE = java.util.Optional.of(false);
    private static final java.lang.Object SLOCK = new java.lang.Object();

    @java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface DeviceConfigKey {
    }

    private ServerFlags(android.content.Context context) {
        android.provider.DeviceConfig.addOnPropertiesChangedListener("system_time", context.getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.timedetector.ServerFlags$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.handlePropertiesChanged(properties);
            }
        });
    }

    public static com.android.server.timedetector.ServerFlags getInstance(android.content.Context context) {
        com.android.server.timedetector.ServerFlags serverFlags;
        synchronized (SLOCK) {
            if (sInstance == null) {
                sInstance = new com.android.server.timedetector.ServerFlags(context);
            }
            serverFlags = sInstance;
        }
        return serverFlags;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePropertiesChanged(android.provider.DeviceConfig.Properties properties) {
        java.util.List<com.android.server.timezonedetector.StateChangeListener> listenersToNotify;
        synchronized (this.mListeners) {
            listenersToNotify = new java.util.ArrayList<>(this.mListeners.size());
            for (java.util.Map.Entry<com.android.server.timezonedetector.StateChangeListener, java.util.HashSet<java.lang.String>> listenerEntry : this.mListeners.entrySet()) {
                java.util.HashSet<java.lang.String> monitoredKeys = listenerEntry.getValue();
                java.lang.Iterable<java.lang.String> modifiedKeys = properties.getKeyset();
                if (containsAny(monitoredKeys, modifiedKeys)) {
                    listenersToNotify.add(listenerEntry.getKey());
                }
            }
        }
        for (com.android.server.timezonedetector.StateChangeListener listener : listenersToNotify) {
            listener.onChange();
        }
    }

    private static boolean containsAny(java.util.Set<java.lang.String> haystack, java.lang.Iterable<java.lang.String> needles) {
        for (java.lang.String needle : needles) {
            if (haystack.contains(needle)) {
                return true;
            }
        }
        return false;
    }

    public void addListener(com.android.server.timezonedetector.StateChangeListener listener, java.util.Set<java.lang.String> keys) {
        java.util.Objects.requireNonNull(listener);
        java.util.Objects.requireNonNull(keys);
        java.util.HashSet<java.lang.String> keysCopy = new java.util.HashSet<>(keys);
        synchronized (this.mListeners) {
            this.mListeners.put(listener, keysCopy);
        }
    }

    public java.util.Optional<java.lang.String> getOptionalString(java.lang.String key) {
        java.lang.String value = android.provider.DeviceConfig.getProperty("system_time", key);
        return java.util.Optional.ofNullable(value);
    }

    public java.util.Optional<java.lang.String[]> getOptionalStringArray(java.lang.String key) {
        java.util.Optional<java.lang.String> optionalString = getOptionalString(key);
        if (!optionalString.isPresent()) {
            return java.util.Optional.empty();
        }
        java.lang.String value = optionalString.get();
        if ("_[]_".equals(value)) {
            return java.util.Optional.of(new java.lang.String[0]);
        }
        return java.util.Optional.of(value.split(","));
    }

    public java.util.Optional<java.time.Instant> getOptionalInstant(java.lang.String key) {
        java.lang.String value = android.provider.DeviceConfig.getProperty("system_time", key);
        if (value == null) {
            return java.util.Optional.empty();
        }
        try {
            long millis = java.lang.Long.parseLong(value);
            return java.util.Optional.of(java.time.Instant.ofEpochMilli(millis));
        } catch (java.lang.NumberFormatException | java.time.DateTimeException e) {
            return java.util.Optional.empty();
        }
    }

    public java.util.Optional<java.lang.Boolean> getOptionalBoolean(java.lang.String key) {
        java.lang.String value = android.provider.DeviceConfig.getProperty("system_time", key);
        return parseOptionalBoolean(value);
    }

    private static java.util.Optional<java.lang.Boolean> parseOptionalBoolean(java.lang.String value) {
        if (value == null) {
            return java.util.Optional.empty();
        }
        return java.lang.Boolean.parseBoolean(value) ? OPTIONAL_TRUE : OPTIONAL_FALSE;
    }

    public boolean getBoolean(java.lang.String key, boolean defaultValue) {
        return android.provider.DeviceConfig.getBoolean("system_time", key, defaultValue);
    }

    public java.time.Duration getDurationFromMillis(java.lang.String key, java.time.Duration defaultValue) {
        long deviceConfigValue = android.provider.DeviceConfig.getLong("system_time", key, -1L);
        if (deviceConfigValue < 0) {
            return defaultValue;
        }
        return java.time.Duration.ofMillis(deviceConfigValue);
    }
}
