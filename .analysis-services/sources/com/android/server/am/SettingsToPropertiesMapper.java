package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class SettingsToPropertiesMapper {
    private static final java.lang.String GLOBAL_SETTINGS_CATEGORY = "global_settings";
    public static final java.lang.String NAMESPACE_LOCAL_OVERRIDES = "device_config_overrides";
    public static final java.lang.String NAMESPACE_REBOOT_STAGING = "staged";
    public static final java.lang.String NAMESPACE_REBOOT_STAGING_DELIMITER = "*";
    private static final java.lang.String RESET_PERFORMED_PROPERTY = "device_config.reset_performed";
    private static final java.lang.String RESET_RECORD_FILE_PATH = "/data/server_configurable_flags/reset_flags";
    private static final java.lang.String SYSTEM_PROPERTY_INVALID_SUBSTRING = "..";
    private static final int SYSTEM_PROPERTY_MAX_LENGTH = 92;
    private static final java.lang.String SYSTEM_PROPERTY_PREFIX = "persist.device_config.";
    private static final java.lang.String SYSTEM_PROPERTY_VALID_CHARACTERS_REGEX = "^[\\w\\.\\-@:]*$";
    private static final java.lang.String TAG = "SettingsToPropertiesMapper";
    private final android.content.ContentResolver mContentResolver;
    private final java.lang.String[] mDeviceConfigAconfigScopes;
    private final java.lang.String[] mDeviceConfigScopes;
    private final java.lang.String[] mGlobalSettings;
    static final java.lang.String[] sGlobalSettings = {"native_flags_health_check_enabled"};
    private static final java.lang.String NAMESPACE_TETHERING_U_OR_LATER_NATIVE = "tethering_u_or_later_native";
    static final java.lang.String[] sDeviceConfigScopes = {"activity_manager_native_boot", "camera_native", "configuration", "connectivity", "edgetpu_native", "input_native_boot", "intelligence_content_suggestions", "lmkd_native", "media_native", "mglru_native", "netd_native", "nnapi_native", "profcollect_native_boot", "remote_key_provisioning_native", "runtime_native", "runtime_native_boot", "statsd_native", "statsd_native_boot", "storage_native_boot", "surface_flinger_native_boot", "swcodec_native", "vendor_system_native", "vendor_system_native_boot", "virtualization_framework_native", "window_manager_native_boot", "memory_safety_native_boot", "memory_safety_native", "hdmi_control", NAMESPACE_TETHERING_U_OR_LATER_NATIVE};
    static final java.lang.String[] sDeviceConfigAconfigScopes = {"accessibility", "android_core_networking", "android_stylus", "aoc", "app_widgets", "arc_next", "art_mainline", "art_performance", "attack_tools", "avic", "biometrics", "biometrics_framework", "biometrics_integration", "bluetooth", "brownout_mitigation_audio", "brownout_mitigation_modem", "build", "camera_hal", "camera_platform", "car_framework", "car_perception", "car_security", "car_telemetry", "codec_fwk", "companion", "com_android_adbd", "content_protection", "context_hub", "core_experiments_team_internal", "core_graphics", "core_libraries", "crumpet", "dck_framework", "devoptions_settings", "game", "gpu", "haptics", "hardware_backed_security_mainline", com.android.server.am.IOplusSceneManager.APP_SCENE_DEFAULT_INPUT, "llvm_and_toolchains", "lse_desktop_experience", "machine_learning", "mainline_modularization", "mainline_sdk", "make_pixel_haptics", "media_audio", "media_drm", "media_reliability", "media_solutions", "media_tv", "nearby", "nfc", "oplus_framework", "pdf_viewer", "perfetto", "pixel_audio_android", "pixel_biometrics_face", "pixel_bluetooth", "pixel_connectivity_gps", "pixel_continuity", "pixel_sensors", "pixel_system_sw_video", "pixel_watch", "platform_compat", "platform_security", "pmw", "power", "preload_safety", "printing", "privacy_infra_policy", "resource_manager", "responsible_apis", "rust", "safety_center", "sensors", "spoon", "statsd", "system_performance", "system_sw_touch", "system_sw_usb", "test_suites", "text", "threadnetwork", "treble", "tv_system_ui", "usb", "vibrator", "virtual_devices", "virtualization", "wallet_integration", "wear_calling_messaging", "wear_connectivity", "wear_esim_carriers", "wear_frameworks", "wear_health_services", "wear_media", "wear_offload", "wear_security", "wear_system_health", "wear_systems", "wear_sysui", "window_surfaces", "windowing_frontend"};

    protected SettingsToPropertiesMapper(android.content.ContentResolver contentResolver, java.lang.String[] globalSettings, java.lang.String[] deviceConfigScopes, java.lang.String[] deviceConfigAconfigScopes) {
        this.mContentResolver = contentResolver;
        this.mGlobalSettings = globalSettings;
        this.mDeviceConfigScopes = deviceConfigScopes;
        this.mDeviceConfigAconfigScopes = deviceConfigAconfigScopes;
    }

    void updatePropertiesFromSettings() {
        for (final java.lang.String globalSetting : this.mGlobalSettings) {
            android.net.Uri settingUri = android.provider.Settings.Global.getUriFor(globalSetting);
            final java.lang.String propName = makePropertyName(GLOBAL_SETTINGS_CATEGORY, globalSetting);
            if (settingUri == null) {
                logErr("setting uri is null for globalSetting " + globalSetting);
            } else if (propName == null) {
                logErr("invalid prop name for globalSetting " + globalSetting);
            } else {
                android.database.ContentObserver co2 = new android.database.ContentObserver(null) { // from class: com.android.server.am.SettingsToPropertiesMapper.1
                    @Override // android.database.ContentObserver
                    public void onChange(boolean selfChange) {
                        com.android.server.am.SettingsToPropertiesMapper.this.updatePropertyFromSetting(globalSetting, propName);
                    }
                };
                if (!isNativeFlagsResetPerformed()) {
                    updatePropertyFromSetting(globalSetting, propName);
                }
                this.mContentResolver.registerContentObserver(settingUri, false, co2);
            }
        }
        for (java.lang.String deviceConfigScope : this.mDeviceConfigScopes) {
            android.provider.DeviceConfig.addOnPropertiesChangedListener(deviceConfigScope, android.os.AsyncTask.THREAD_POOL_EXECUTOR, new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.SettingsToPropertiesMapper$$ExternalSyntheticLambda0
                public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                    this.f$0.lambda$updatePropertiesFromSettings$0(properties);
                }
            });
        }
        for (java.lang.String deviceConfigAconfigScope : this.mDeviceConfigAconfigScopes) {
            android.provider.DeviceConfig.addOnPropertiesChangedListener(deviceConfigAconfigScope, android.os.AsyncTask.THREAD_POOL_EXECUTOR, new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.SettingsToPropertiesMapper$$ExternalSyntheticLambda1
                public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                    this.f$0.lambda$updatePropertiesFromSettings$1(properties);
                }
            });
        }
        android.provider.DeviceConfig.addOnPropertiesChangedListener(NAMESPACE_REBOOT_STAGING, android.os.AsyncTask.THREAD_POOL_EXECUTOR, new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.SettingsToPropertiesMapper$$ExternalSyntheticLambda2
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.lambda$updatePropertiesFromSettings$2(properties);
            }
        });
        android.provider.DeviceConfig.addOnPropertiesChangedListener(NAMESPACE_LOCAL_OVERRIDES, android.os.AsyncTask.THREAD_POOL_EXECUTOR, new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.SettingsToPropertiesMapper$$ExternalSyntheticLambda3
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                com.android.server.am.SettingsToPropertiesMapper.lambda$updatePropertiesFromSettings$3(properties);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePropertiesFromSettings$0(android.provider.DeviceConfig.Properties properties) {
        java.lang.String scope = properties.getNamespace();
        for (java.lang.String key : properties.getKeyset()) {
            java.lang.String propertyName = makePropertyName(scope, key);
            if (propertyName == null) {
                logErr("unable to construct system property for " + scope + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + key);
                return;
            }
            setProperty(propertyName, properties.getString(key, (java.lang.String) null));
            java.lang.String aconfigPropertyName = makeAconfigFlagPropertyName(scope, key);
            if (aconfigPropertyName == null) {
                logErr("unable to construct system property for " + scope + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + key);
                return;
            }
            setProperty(aconfigPropertyName, properties.getString(key, (java.lang.String) null));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePropertiesFromSettings$1(android.provider.DeviceConfig.Properties properties) {
        java.lang.String scope = properties.getNamespace();
        for (java.lang.String key : properties.getKeyset()) {
            java.lang.String aconfigPropertyName = makeAconfigFlagPropertyName(scope, key);
            if (aconfigPropertyName == null) {
                logErr("unable to construct system property for " + scope + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + key);
                return;
            }
            setProperty(aconfigPropertyName, properties.getString(key, (java.lang.String) null));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePropertiesFromSettings$2(android.provider.DeviceConfig.Properties properties) {
        for (java.lang.String flagName : properties.getKeyset()) {
            java.lang.String flagValue = properties.getString(flagName, (java.lang.String) null);
            if (flagName != null && flagValue != null) {
                int idx = flagName.indexOf(NAMESPACE_REBOOT_STAGING_DELIMITER);
                if (idx == -1 || idx == flagName.length() - 1 || idx == 0) {
                    logErr("invalid staged flag: " + flagName);
                } else {
                    java.lang.String actualNamespace = flagName.substring(0, idx);
                    java.lang.String actualFlagName = flagName.substring(idx + 1);
                    java.lang.String propertyName = "next_boot." + makeAconfigFlagPropertyName(actualNamespace, actualFlagName);
                    setProperty(propertyName, flagValue);
                }
            }
        }
        if (com.android.aconfig_new_storage.Flags.enableAconfigStorageDaemon()) {
            stageFlagsInNewStorage(properties);
        }
    }

    static /* synthetic */ void lambda$updatePropertiesFromSettings$3(android.provider.DeviceConfig.Properties properties) {
        if (com.android.aconfig_new_storage.Flags.enableAconfigStorageDaemon()) {
            setLocalOverridesInNewStorage(properties);
        }
    }

    static android.util.proto.ProtoInputStream sendAconfigdRequests(android.util.proto.ProtoOutputStream requests) {
        android.net.LocalSocket client = new android.net.LocalSocket();
        try {
            client.connect(new android.net.LocalSocketAddress("aconfigd", android.net.LocalSocketAddress.Namespace.RESERVED));
            android.util.Slog.d(TAG, "connected to aconfigd socket");
            try {
                java.io.DataInputStream inputStream = new java.io.DataInputStream(client.getInputStream());
                java.io.DataOutputStream outputStream = new java.io.DataOutputStream(client.getOutputStream());
                try {
                    byte[] requests_bytes = requests.getBytes();
                    outputStream.writeInt(requests_bytes.length);
                    outputStream.write(requests_bytes, 0, requests_bytes.length);
                    android.util.Slog.d(TAG, "flag override requests sent to aconfigd");
                    try {
                        int num_bytes = inputStream.readInt();
                        android.util.proto.ProtoInputStream returns = new android.util.proto.ProtoInputStream(inputStream);
                        android.util.Slog.d(TAG, "received " + num_bytes + " bytes back from aconfigd");
                        return returns;
                    } catch (java.io.IOException ioe) {
                        logErr("failed to read requests return from aconfigd", ioe);
                        return null;
                    }
                } catch (java.io.IOException ioe2) {
                    logErr("failed to send requests to aconfigd", ioe2);
                    return null;
                }
            } catch (java.io.IOException ioe3) {
                logErr("failed to get local socket iostreams", ioe3);
                return null;
            }
        } catch (java.io.IOException ioe4) {
            logErr("failed to connect to aconfigd socket", ioe4);
            return null;
        }
    }

    static void writeFlagOverrideRequest(android.util.proto.ProtoOutputStream proto, java.lang.String packageName, java.lang.String flagName, java.lang.String flagValue, boolean isLocal) {
        long msgsToken = proto.start(2246267895809L);
        long msgToken = proto.start(1146756268034L);
        proto.write(1138166333441L, packageName);
        proto.write(1138166333442L, flagName);
        proto.write(1138166333443L, flagValue);
        proto.write(1133871366148L, isLocal);
        proto.end(msgToken);
        proto.end(msgsToken);
    }

    static void parseAndLogAconfigdReturn(android.util.proto.ProtoInputStream proto) throws java.io.IOException {
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    return;
                case 0:
                default:
                    logErr("invalid message type, expect storage return message");
                    break;
                case 1:
                    long msgsToken = proto.start(2246267895809L);
                    switch (proto.nextField()) {
                        case -1:
                            break;
                        case 2:
                            android.util.Slog.d(TAG, "successfully handled override requests");
                            long msgToken = proto.start(1146756268034L);
                            proto.end(msgToken);
                            break;
                        case 8:
                            java.lang.String errmsg = proto.readString(1138166333448L);
                            android.util.Slog.d(TAG, "override request failed: " + errmsg);
                            break;
                        default:
                            logErr("invalid message type, expecting only flag override return or error message");
                            break;
                    }
                    proto.end(msgsToken);
                    break;
            }
        }
    }

    static void setLocalOverridesInNewStorage(android.provider.DeviceConfig.Properties props) {
        int num_requests = 0;
        android.util.proto.ProtoOutputStream requests = new android.util.proto.ProtoOutputStream();
        for (java.lang.String flagName : props.getKeyset()) {
            java.lang.String flagValue = props.getString(flagName, (java.lang.String) null);
            if (flagName != null && flagValue != null) {
                int idx = flagName.indexOf(":");
                if (idx == -1 || idx == flagName.length() - 1 || idx == 0) {
                    logErr("invalid local flag override: " + flagName);
                } else {
                    flagName.substring(0, idx);
                    java.lang.String fullFlagName = flagName.substring(idx + 1);
                    int idx2 = fullFlagName.lastIndexOf(".");
                    if (idx2 == -1) {
                        logErr("invalid flag name: " + fullFlagName);
                    } else {
                        java.lang.String packageName = fullFlagName.substring(0, idx2);
                        java.lang.String realFlagName = fullFlagName.substring(idx2 + 1);
                        writeFlagOverrideRequest(requests, packageName, realFlagName, flagValue, true);
                        num_requests++;
                    }
                }
            }
        }
        if (num_requests == 0) {
            return;
        }
        android.util.proto.ProtoInputStream returns = sendAconfigdRequests(requests);
        try {
            parseAndLogAconfigdReturn(returns);
        } catch (java.io.IOException ioe) {
            logErr("failed to parse aconfigd return", ioe);
        }
    }

    public static com.android.server.am.SettingsToPropertiesMapper start(android.content.ContentResolver contentResolver) {
        com.android.server.am.SettingsToPropertiesMapper mapper = new com.android.server.am.SettingsToPropertiesMapper(contentResolver, sGlobalSettings, sDeviceConfigScopes, sDeviceConfigAconfigScopes);
        mapper.updatePropertiesFromSettings();
        return mapper;
    }

    public static boolean isNativeFlagsResetPerformed() {
        java.lang.String value = android.os.SystemProperties.get(RESET_PERFORMED_PROPERTY);
        return "true".equals(value);
    }

    public static java.lang.String[] getResetNativeCategories() {
        if (!isNativeFlagsResetPerformed()) {
            return new java.lang.String[0];
        }
        java.lang.String content = getResetFlagsFileContent();
        if (android.text.TextUtils.isEmpty(content)) {
            return new java.lang.String[0];
        }
        java.lang.String[] property_names = content.split(";");
        java.util.HashSet<java.lang.String> categories = new java.util.HashSet<>();
        for (java.lang.String property_name : property_names) {
            java.lang.String[] segments = property_name.split("\\.");
            if (segments.length < 3) {
                logErr("failed to extract category name from property " + property_name);
            } else {
                categories.add(segments[2]);
            }
        }
        return (java.lang.String[]) categories.toArray(new java.lang.String[0]);
    }

    static java.lang.String makePropertyName(java.lang.String categoryName, java.lang.String flagName) {
        java.lang.String propertyName = SYSTEM_PROPERTY_PREFIX + categoryName + "." + flagName;
        if (!propertyName.matches(SYSTEM_PROPERTY_VALID_CHARACTERS_REGEX) || propertyName.contains(SYSTEM_PROPERTY_INVALID_SUBSTRING)) {
            return null;
        }
        return propertyName;
    }

    static void stageFlagsInNewStorage(android.provider.DeviceConfig.Properties props) {
        int num_requests = 0;
        android.util.proto.ProtoOutputStream requests = new android.util.proto.ProtoOutputStream();
        for (java.lang.String flagName : props.getKeyset()) {
            java.lang.String flagValue = props.getString(flagName, (java.lang.String) null);
            if (flagName != null && flagValue != null) {
                int idx = flagName.indexOf(NAMESPACE_REBOOT_STAGING_DELIMITER);
                if (idx == -1 || idx == flagName.length() - 1 || idx == 0) {
                    logErr("invalid local flag override: " + flagName);
                } else {
                    flagName.substring(0, idx);
                    java.lang.String fullFlagName = flagName.substring(idx + 1);
                    int idx2 = fullFlagName.lastIndexOf(".");
                    if (idx2 == -1) {
                        logErr("invalid flag name: " + fullFlagName);
                    } else {
                        java.lang.String packageName = fullFlagName.substring(0, idx2);
                        java.lang.String realFlagName = fullFlagName.substring(idx2 + 1);
                        writeFlagOverrideRequest(requests, packageName, realFlagName, flagValue, false);
                        num_requests++;
                    }
                }
            }
        }
        if (num_requests == 0) {
            return;
        }
        android.util.proto.ProtoInputStream returns = sendAconfigdRequests(requests);
        try {
            parseAndLogAconfigdReturn(returns);
        } catch (java.io.IOException ioe) {
            logErr("failed to parse aconfigd return", ioe);
        }
    }

    static java.lang.String makeAconfigFlagPropertyName(java.lang.String categoryName, java.lang.String flagName) {
        java.lang.String propertyName = "persist.device_config.aconfig_flags." + categoryName + "." + flagName;
        if (!propertyName.matches(SYSTEM_PROPERTY_VALID_CHARACTERS_REGEX) || propertyName.contains(SYSTEM_PROPERTY_INVALID_SUBSTRING)) {
            return null;
        }
        return propertyName;
    }

    private void setProperty(java.lang.String key, java.lang.String value) {
        if (value == null) {
            if (android.text.TextUtils.isEmpty(android.os.SystemProperties.get(key))) {
                return;
            } else {
                value = "";
            }
        } else if (value.length() > 92) {
            logErr("key=" + key + " value=" + value + " exceeds system property max length.");
            return;
        }
        try {
            android.os.SystemProperties.set(key, value);
        } catch (java.lang.Exception e) {
            logErr("Unable to set property " + key + " value '" + value + "'", e);
        }
    }

    private static void logErr(java.lang.String msg, java.lang.Exception e) {
        if (android.os.Build.IS_DEBUGGABLE) {
            android.util.Slog.wtf(TAG, msg, e);
        } else {
            android.util.Slog.e(TAG, msg, e);
        }
    }

    private static void logErr(java.lang.String msg) {
        if (android.os.Build.IS_DEBUGGABLE) {
            android.util.Slog.wtf(TAG, msg);
        } else {
            android.util.Slog.e(TAG, msg);
        }
    }

    static java.lang.String getResetFlagsFileContent() {
        java.lang.String content = null;
        try {
            java.io.File reset_flag_file = new java.io.File(RESET_RECORD_FILE_PATH);
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(reset_flag_file));
            content = br.readLine();
            br.close();
            return content;
        } catch (java.io.IOException ioe) {
            logErr("failed to read file /data/server_configurable_flags/reset_flags", ioe);
            return content;
        }
    }

    void updatePropertyFromSetting(java.lang.String settingName, java.lang.String propName) {
        java.lang.String settingValue = android.provider.Settings.Global.getString(this.mContentResolver, settingName);
        setProperty(propName, settingValue);
    }
}
