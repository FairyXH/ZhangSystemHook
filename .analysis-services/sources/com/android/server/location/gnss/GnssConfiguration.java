package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
public class GnssConfiguration {
    private static final java.lang.String CONFIG_A_GLONASS_POS_PROTOCOL_SELECT = "A_GLONASS_POS_PROTOCOL_SELECT";
    private static final java.lang.String CONFIG_C2K_HOST = "C2K_HOST";
    private static final java.lang.String CONFIG_C2K_PORT = "C2K_PORT";
    private static final java.lang.String CONFIG_ENABLE_ACTIVE_SIM_EMERGENCY_SUPL = "ENABLE_ACTIVE_SIM_EMERGENCY_SUPL";
    private static final java.lang.String CONFIG_ENABLE_NI_SUPL_MESSAGE_INJECTION = "ENABLE_NI_SUPL_MESSAGE_INJECTION";
    private static final java.lang.String CONFIG_ENABLE_PSDS_PERIODIC_DOWNLOAD = "ENABLE_PSDS_PERIODIC_DOWNLOAD";
    private static final java.lang.String CONFIG_ES_EXTENSION_SEC = "ES_EXTENSION_SEC";
    private static final java.lang.String CONFIG_GPS_LOCK = "GPS_LOCK";
    static final java.lang.String CONFIG_LONGTERM_PSDS_SERVER_1 = "LONGTERM_PSDS_SERVER_1";
    static final java.lang.String CONFIG_LONGTERM_PSDS_SERVER_2 = "LONGTERM_PSDS_SERVER_2";
    static final java.lang.String CONFIG_LONGTERM_PSDS_SERVER_3 = "LONGTERM_PSDS_SERVER_3";
    private static final java.lang.String CONFIG_LPP_PROFILE = "LPP_PROFILE";
    static final java.lang.String CONFIG_NFW_PROXY_APPS = "NFW_PROXY_APPS";
    static final java.lang.String CONFIG_NORMAL_PSDS_SERVER = "NORMAL_PSDS_SERVER";
    static final java.lang.String CONFIG_REALTIME_PSDS_SERVER = "REALTIME_PSDS_SERVER";
    private static final java.lang.String CONFIG_SUPL_ES = "SUPL_ES";
    private static final java.lang.String CONFIG_SUPL_HOST = "SUPL_HOST";
    private static final java.lang.String CONFIG_SUPL_MODE = "SUPL_MODE";
    private static final java.lang.String CONFIG_SUPL_PORT = "SUPL_PORT";
    private static final java.lang.String CONFIG_SUPL_VER = "SUPL_VER";
    private static final java.lang.String CONFIG_USE_EMERGENCY_PDN_FOR_EMERGENCY_SUPL = "USE_EMERGENCY_PDN_FOR_EMERGENCY_SUPL";
    private static final java.lang.String DEBUG_PROPERTIES_SYSTEM_FILE = "/etc/gps_debug.conf";
    private static final java.lang.String DEBUG_PROPERTIES_VENDOR_FILE = "/vendor/etc/gps_debug.conf";
    static final java.lang.String LPP_PROFILE = "persist.sys.gps.lpp";
    private static final int MAX_EMERGENCY_MODE_EXTENSION_SECONDS = 300;
    private final android.content.Context mContext;
    private int mEsExtensionSec = 0;
    private final java.util.Properties mProperties = new java.util.Properties();
    private static final java.lang.String TAG = "GnssConfiguration";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);

    interface SetCarrierProperty {
        boolean set(int i);
    }

    private static native com.android.server.location.gnss.GnssConfiguration.HalInterfaceVersion native_get_gnss_configuration_version();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_set_emergency_supl_pdn(int i);

    private static native boolean native_set_es_extension_sec(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_set_gnss_pos_protocol_select(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_set_gps_lock(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_set_lpp_profile(int i);

    private static native boolean native_set_satellite_blocklist(int[] iArr, int[] iArr2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_set_supl_es(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_set_supl_mode(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_set_supl_version(int i);

    static class HalInterfaceVersion {
        static final int AIDL_INTERFACE = 3;
        final int mMajor;
        final int mMinor;

        HalInterfaceVersion(int major, int minor) {
            this.mMajor = major;
            this.mMinor = minor;
        }
    }

    public GnssConfiguration(android.content.Context context) {
        this.mContext = context;
    }

    java.util.Properties getProperties() {
        return this.mProperties;
    }

    public int getEsExtensionSec() {
        return this.mEsExtensionSec;
    }

    java.lang.String getSuplHost() {
        return this.mProperties.getProperty(CONFIG_SUPL_HOST);
    }

    int getSuplPort(int defaultPort) {
        return getIntConfig(CONFIG_SUPL_PORT, defaultPort);
    }

    java.lang.String getC2KHost() {
        return this.mProperties.getProperty(CONFIG_C2K_HOST);
    }

    int getC2KPort(int defaultPort) {
        return getIntConfig(CONFIG_C2K_PORT, defaultPort);
    }

    int getSuplMode(int defaultMode) {
        return getIntConfig(CONFIG_SUPL_MODE, defaultMode);
    }

    public int getSuplEs(int defaultSuplEs) {
        return getIntConfig(CONFIG_SUPL_ES, defaultSuplEs);
    }

    java.lang.String getLppProfile() {
        return this.mProperties.getProperty(CONFIG_LPP_PROFILE);
    }

    java.util.List<java.lang.String> getProxyApps() {
        java.lang.String proxyAppsStr = this.mProperties.getProperty(CONFIG_NFW_PROXY_APPS);
        if (android.text.TextUtils.isEmpty(proxyAppsStr)) {
            return java.util.Collections.emptyList();
        }
        java.lang.String[] proxyAppsArray = proxyAppsStr.trim().split("\\s+");
        if (proxyAppsArray.length == 0) {
            return java.util.Collections.emptyList();
        }
        return java.util.Arrays.asList(proxyAppsArray);
    }

    boolean isPsdsPeriodicDownloadEnabled() {
        return getBooleanConfig(CONFIG_ENABLE_PSDS_PERIODIC_DOWNLOAD, false);
    }

    boolean isActiveSimEmergencySuplEnabled() {
        return getBooleanConfig(CONFIG_ENABLE_ACTIVE_SIM_EMERGENCY_SUPL, false);
    }

    boolean isNiSuplMessageInjectionEnabled() {
        return getBooleanConfig(CONFIG_ENABLE_NI_SUPL_MESSAGE_INJECTION, false);
    }

    boolean isLongTermPsdsServerConfigured() {
        return (this.mProperties.getProperty(CONFIG_LONGTERM_PSDS_SERVER_1) == null && this.mProperties.getProperty(CONFIG_LONGTERM_PSDS_SERVER_2) == null && this.mProperties.getProperty(CONFIG_LONGTERM_PSDS_SERVER_3) == null) ? false : true;
    }

    void setSatelliteBlocklist(int[] constellations, int[] svids) {
        native_set_satellite_blocklist(constellations, svids);
    }

    com.android.server.location.gnss.GnssConfiguration.HalInterfaceVersion getHalInterfaceVersion() {
        return native_get_gnss_configuration_version();
    }

    void reloadGpsProperties() {
        reloadGpsProperties(false, -1);
    }

    void reloadGpsProperties(boolean inEmergency, int activeSubId) {
        if (DEBUG) {
            android.util.Log.d(TAG, "Reset GPS properties, previous size = " + this.mProperties.size() + ", inEmergency:" + inEmergency + ", activeSubId=" + activeSubId);
        }
        loadPropertiesFromCarrierConfig(inEmergency, activeSubId);
        if (android.location.flags.Flags.gnssConfigurationFromResource()) {
            loadPropertiesFromResource(this.mContext, this.mProperties);
        }
        if (isSimAbsent(this.mContext)) {
            java.lang.String lpp_prof = android.os.SystemProperties.get(LPP_PROFILE);
            if (!android.text.TextUtils.isEmpty(lpp_prof)) {
                this.mProperties.setProperty(CONFIG_LPP_PROFILE, lpp_prof);
            }
        }
        loadPropertiesFromGpsDebugConfig(this.mProperties, DEBUG_PROPERTIES_VENDOR_FILE);
        loadPropertiesFromGpsDebugConfig(this.mProperties, DEBUG_PROPERTIES_SYSTEM_FILE);
        this.mEsExtensionSec = getRangeCheckedConfigEsExtensionSec();
        logConfigurations();
        com.android.server.location.gnss.GnssConfiguration.HalInterfaceVersion gnssConfigurationIfaceVersion = getHalInterfaceVersion();
        if (gnssConfigurationIfaceVersion != null) {
            if (isConfigEsExtensionSecSupported(gnssConfigurationIfaceVersion) && !native_set_es_extension_sec(this.mEsExtensionSec)) {
                android.util.Log.e(TAG, "Unable to set ES_EXTENSION_SEC: " + this.mEsExtensionSec);
            }
            java.util.Map<java.lang.String, com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty> map = new java.util.HashMap<>();
            map.put(CONFIG_SUPL_VER, new com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty() { // from class: com.android.server.location.gnss.GnssConfiguration$$ExternalSyntheticLambda0
                @Override // com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty
                public final boolean set(int i) {
                    return com.android.server.location.gnss.GnssConfiguration.native_set_supl_version(i);
                }
            });
            map.put(CONFIG_SUPL_MODE, new com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty() { // from class: com.android.server.location.gnss.GnssConfiguration$$ExternalSyntheticLambda1
                @Override // com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty
                public final boolean set(int i) {
                    return com.android.server.location.gnss.GnssConfiguration.native_set_supl_mode(i);
                }
            });
            if (isConfigSuplEsSupported(gnssConfigurationIfaceVersion)) {
                map.put(CONFIG_SUPL_ES, new com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty() { // from class: com.android.server.location.gnss.GnssConfiguration$$ExternalSyntheticLambda2
                    @Override // com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty
                    public final boolean set(int i) {
                        return com.android.server.location.gnss.GnssConfiguration.native_set_supl_es(i);
                    }
                });
            }
            map.put(CONFIG_LPP_PROFILE, new com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty() { // from class: com.android.server.location.gnss.GnssConfiguration$$ExternalSyntheticLambda3
                @Override // com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty
                public final boolean set(int i) {
                    return com.android.server.location.gnss.GnssConfiguration.native_set_lpp_profile(i);
                }
            });
            map.put(CONFIG_A_GLONASS_POS_PROTOCOL_SELECT, new com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty() { // from class: com.android.server.location.gnss.GnssConfiguration$$ExternalSyntheticLambda4
                @Override // com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty
                public final boolean set(int i) {
                    return com.android.server.location.gnss.GnssConfiguration.native_set_gnss_pos_protocol_select(i);
                }
            });
            map.put(CONFIG_USE_EMERGENCY_PDN_FOR_EMERGENCY_SUPL, new com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty() { // from class: com.android.server.location.gnss.GnssConfiguration$$ExternalSyntheticLambda5
                @Override // com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty
                public final boolean set(int i) {
                    return com.android.server.location.gnss.GnssConfiguration.native_set_emergency_supl_pdn(i);
                }
            });
            if (isConfigGpsLockSupported(gnssConfigurationIfaceVersion)) {
                map.put(CONFIG_GPS_LOCK, new com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty() { // from class: com.android.server.location.gnss.GnssConfiguration$$ExternalSyntheticLambda6
                    @Override // com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty
                    public final boolean set(int i) {
                        return com.android.server.location.gnss.GnssConfiguration.native_set_gps_lock(i);
                    }
                });
            }
            for (java.util.Map.Entry<java.lang.String, com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty> entry : map.entrySet()) {
                java.lang.String propertyName = entry.getKey();
                java.lang.String propertyValueString = this.mProperties.getProperty(propertyName);
                if (propertyValueString != null) {
                    try {
                        int propertyValueInt = java.lang.Integer.decode(propertyValueString).intValue();
                        boolean result = entry.getValue().set(propertyValueInt);
                        if (!result) {
                            android.util.Log.e(TAG, "Unable to set " + propertyName);
                        }
                    } catch (java.lang.NumberFormatException e) {
                        android.util.Log.e(TAG, "Unable to parse propertyName: " + propertyValueString);
                    }
                }
            }
            return;
        }
        if (DEBUG) {
            android.util.Log.d(TAG, "Skipped configuration update because GNSS configuration in GPS HAL is not supported");
        }
    }

    private void logConfigurations() {
        com.android.internal.util.FrameworkStatsLog.write(132, getSuplHost(), getSuplPort(0), getC2KHost(), getC2KPort(0), getIntConfig(CONFIG_SUPL_VER, 0), getSuplMode(0), getSuplEs(0) == 1, getIntConfig(CONFIG_LPP_PROFILE, 0), getIntConfig(CONFIG_A_GLONASS_POS_PROTOCOL_SELECT, 0), getIntConfig(CONFIG_USE_EMERGENCY_PDN_FOR_EMERGENCY_SUPL, 0) == 1, getIntConfig(CONFIG_GPS_LOCK, 0), getEsExtensionSec(), this.mProperties.getProperty(CONFIG_NFW_PROXY_APPS));
    }

    void loadPropertiesFromCarrierConfig(boolean inEmergency, int activeSubId) {
        android.telephony.CarrierConfigManager configManager = (android.telephony.CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        if (configManager == null) {
            return;
        }
        int subId = android.telephony.SubscriptionManager.getDefaultDataSubscriptionId();
        if (inEmergency && activeSubId >= 0) {
            subId = activeSubId;
        }
        android.os.PersistableBundle configs = android.telephony.SubscriptionManager.isValidSubscriptionId(subId) ? configManager.getConfigForSubId(subId) : configManager.getConfig();
        if (configs == null) {
            if (DEBUG) {
                android.util.Log.d(TAG, "SIM not ready, use default carrier config.");
            }
            configs = android.telephony.CarrierConfigManager.getDefaultConfig();
        }
        for (java.lang.String configKey : configs.keySet()) {
            if (configKey != null && configKey.startsWith("gps.")) {
                java.lang.String key = configKey.substring("gps.".length()).toUpperCase(java.util.Locale.ROOT);
                java.lang.Object value = configs.get(configKey);
                if (DEBUG) {
                    android.util.Log.d(TAG, "Gps config: " + key + " = " + value);
                }
                if (value instanceof java.lang.String) {
                    this.mProperties.setProperty(key, (java.lang.String) value);
                } else if (value != null) {
                    this.mProperties.setProperty(key, value.toString());
                }
            }
        }
    }

    private void loadPropertiesFromGpsDebugConfig(java.util.Properties properties, java.lang.String filePath) {
        try {
            java.io.File file = new java.io.File(filePath);
            java.io.FileInputStream stream = null;
            try {
                stream = new java.io.FileInputStream(file);
                properties.load(stream);
                libcore.io.IoUtils.closeQuietly(stream);
            } catch (java.lang.Throwable th) {
                libcore.io.IoUtils.closeQuietly(stream);
                throw th;
            }
        } catch (java.io.IOException e) {
            if (DEBUG) {
                android.util.Log.d(TAG, "Could not open GPS configuration file " + filePath);
            }
        }
    }

    private void loadPropertiesFromResource(android.content.Context context, java.util.Properties properties) {
        java.lang.String[] configValues = context.getResources().getStringArray(android.R.array.config_force_cellular_transport_capabilities);
        for (java.lang.String item : configValues) {
            if (DEBUG) {
                android.util.Log.d(TAG, "GnssParamsResource: " + item);
            }
            int index = item.indexOf("=");
            if (index > 0 && index + 1 < item.length()) {
                java.lang.String key = item.substring(0, index);
                java.lang.String value = item.substring(index + 1);
                properties.setProperty(key.trim().toUpperCase(java.util.Locale.ROOT), value);
            } else {
                android.util.Log.w(TAG, "malformed contents: " + item);
            }
        }
    }

    private int getRangeCheckedConfigEsExtensionSec() {
        int emergencyExtensionSeconds = getIntConfig(CONFIG_ES_EXTENSION_SEC, 0);
        if (emergencyExtensionSeconds > 300) {
            android.util.Log.w(TAG, "ES_EXTENSION_SEC: " + emergencyExtensionSeconds + " too high, reset to 300");
            return 300;
        }
        if (emergencyExtensionSeconds < 0) {
            android.util.Log.w(TAG, "ES_EXTENSION_SEC: " + emergencyExtensionSeconds + " is negative, reset to zero.");
            return 0;
        }
        return emergencyExtensionSeconds;
    }

    private int getIntConfig(java.lang.String configParameter, int defaultValue) {
        java.lang.String valueString = this.mProperties.getProperty(configParameter);
        if (android.text.TextUtils.isEmpty(valueString)) {
            return defaultValue;
        }
        try {
            return java.lang.Integer.decode(valueString).intValue();
        } catch (java.lang.NumberFormatException e) {
            android.util.Log.e(TAG, "Unable to parse config parameter " + configParameter + " value: " + valueString + ". Using default value: " + defaultValue);
            return defaultValue;
        }
    }

    private boolean getBooleanConfig(java.lang.String configParameter, boolean defaultValue) {
        java.lang.String valueString = this.mProperties.getProperty(configParameter);
        if (android.text.TextUtils.isEmpty(valueString)) {
            return defaultValue;
        }
        return java.lang.Boolean.parseBoolean(valueString);
    }

    private static boolean isConfigEsExtensionSecSupported(com.android.server.location.gnss.GnssConfiguration.HalInterfaceVersion gnssConfiguartionIfaceVersion) {
        return gnssConfiguartionIfaceVersion.mMajor >= 2;
    }

    private static boolean isConfigSuplEsSupported(com.android.server.location.gnss.GnssConfiguration.HalInterfaceVersion gnssConfiguartionIfaceVersion) {
        return gnssConfiguartionIfaceVersion.mMajor < 2;
    }

    private static boolean isConfigGpsLockSupported(com.android.server.location.gnss.GnssConfiguration.HalInterfaceVersion gnssConfiguartionIfaceVersion) {
        return gnssConfiguartionIfaceVersion.mMajor < 2;
    }

    private static boolean isSimAbsent(android.content.Context context) {
        android.telephony.TelephonyManager phone = (android.telephony.TelephonyManager) context.getSystemService(com.android.server.autofill.HintsHelper.AUTOFILL_HINT_PHONE);
        return phone.getSimState() == 1;
    }
}
