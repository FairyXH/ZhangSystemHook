package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class RescueParty {
    static final long DEFAULT_FACTORY_RESET_THROTTLE_DURATION_MIN = 1440;
    static final long DEFAULT_OBSERVING_DURATION_MS = java.util.concurrent.TimeUnit.DAYS.toMillis(2);
    static final int DEFAULT_RESCUE_NON_REBOOT_LEVEL_LIMIT = 2;
    static final int DEVICE_CONFIG_RESET_MODE = 4;
    static final int LEVEL_FACTORY_RESET = 5;
    static final int LEVEL_NONE = 0;
    static final int LEVEL_RESET_SETTINGS_TRUSTED_DEFAULTS = 3;
    static final int LEVEL_RESET_SETTINGS_UNTRUSTED_CHANGES = 2;
    static final int LEVEL_RESET_SETTINGS_UNTRUSTED_DEFAULTS = 1;
    static final int LEVEL_WARM_REBOOT = 4;
    private static final java.lang.String NAME = "rescue-party-observer";
    static final java.lang.String NAMESPACE_CONFIGURATION = "configuration";
    static final java.lang.String NAMESPACE_TO_PACKAGE_MAPPING_FLAG = "namespace_to_package_mapping";
    private static final int PERSISTENT_MASK = 9;
    private static final java.lang.String PROP_DEVICE_CONFIG_DISABLE_FLAG = "persist.device_config.configuration.disable_rescue_party";
    private static final java.lang.String PROP_DISABLE_FACTORY_RESET_FLAG = "persist.device_config.configuration.disable_rescue_party_factory_reset";
    private static final java.lang.String PROP_DISABLE_RESCUE = "persist.sys.disable_rescue";
    static final java.lang.String PROP_ENABLE_RESCUE = "persist.sys.enable_rescue";
    private static final java.lang.String PROP_THROTTLE_DURATION_MIN_FLAG = "persist.device_config.configuration.rescue_party_throttle_duration_min";
    private static final java.lang.String PROP_VIRTUAL_DEVICE = "ro.hardware.virtual_device";
    static final int RESCUE_LEVEL_ALL_DEVICE_CONFIG_RESET = 2;
    static final int RESCUE_LEVEL_FACTORY_RESET = 7;
    static final int RESCUE_LEVEL_NONE = 0;
    static final int RESCUE_LEVEL_RESET_SETTINGS_TRUSTED_DEFAULTS = 6;
    static final int RESCUE_LEVEL_RESET_SETTINGS_UNTRUSTED_CHANGES = 5;
    static final int RESCUE_LEVEL_RESET_SETTINGS_UNTRUSTED_DEFAULTS = 4;
    static final int RESCUE_LEVEL_SCOPED_DEVICE_CONFIG_RESET = 1;
    static final int RESCUE_LEVEL_WARM_REBOOT = 3;
    static final java.lang.String RESCUE_NON_REBOOT_LEVEL_LIMIT = "persist.sys.rescue_non_reboot_level_limit";
    static final java.lang.String TAG = "RescueParty";

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface RescueLevels {
    }

    public static void registerHealthObserver(android.content.Context context) {
        com.android.server.PackageWatchdog.getInstance(context).registerHealthObserver(com.android.server.RescueParty.RescuePartyObserver.getInstance(context));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isDisabled() {
        if (android.os.SystemProperties.getBoolean(PROP_ENABLE_RESCUE, false)) {
            return false;
        }
        if (android.os.SystemProperties.getBoolean(PROP_DEVICE_CONFIG_DISABLE_FLAG, false)) {
            android.util.Slog.v(TAG, "Disabled because of DeviceConfig flag");
            return true;
        }
        if (android.os.Build.TYPE.equals("eng")) {
            android.util.Slog.v(TAG, "Disabled because of eng build");
            return true;
        }
        if (android.os.Build.TYPE.equals("userdebug") && isUsbActive()) {
            android.util.Slog.v(TAG, "Disabled because of active USB connection");
            return true;
        }
        if (!android.os.SystemProperties.getBoolean(PROP_DISABLE_RESCUE, false)) {
            return false;
        }
        android.util.Slog.v(TAG, "Disabled because of manual property");
        return true;
    }

    public static boolean isRecoveryTriggeredReboot() {
        return isFactoryResetPropertySet() || isRebootPropertySet();
    }

    static boolean isFactoryResetPropertySet() {
        return ((java.lang.Boolean) android.sysprop.CrashRecoveryProperties.attemptingFactoryReset().orElse(false)).booleanValue();
    }

    static boolean isRebootPropertySet() {
        return ((java.lang.Boolean) android.sysprop.CrashRecoveryProperties.attemptingReboot().orElse(false)).booleanValue();
    }

    protected static long getLastFactoryResetTimeMs() {
        return ((java.lang.Long) android.sysprop.CrashRecoveryProperties.lastFactoryResetTimeMs().orElse(0L)).longValue();
    }

    protected static int getMaxRescueLevelAttempted() {
        return ((java.lang.Integer) android.sysprop.CrashRecoveryProperties.maxRescueLevelAttempted().orElse(0)).intValue();
    }

    protected static void setFactoryResetProperty(boolean value) {
        android.sysprop.CrashRecoveryProperties.attemptingFactoryReset(java.lang.Boolean.valueOf(value));
    }

    protected static void setRebootProperty(boolean value) {
        android.sysprop.CrashRecoveryProperties.attemptingReboot(java.lang.Boolean.valueOf(value));
    }

    protected static void setLastFactoryResetTimeMs(long value) {
        android.sysprop.CrashRecoveryProperties.lastFactoryResetTimeMs(java.lang.Long.valueOf(value));
    }

    protected static void setMaxRescueLevelAttempted(int level) {
        android.sysprop.CrashRecoveryProperties.maxRescueLevelAttempted(java.lang.Integer.valueOf(level));
    }

    public static void onSettingsProviderPublished(android.content.Context context) {
        handleNativeRescuePartyResets();
        android.content.ContentResolver contentResolver = context.getContentResolver();
        android.provider.DeviceConfig.setMonitorCallback(contentResolver, java.util.concurrent.Executors.newSingleThreadExecutor(), new com.android.server.RescueParty.RescuePartyMonitorCallback(context));
    }

    public static void resetDeviceConfigForPackages(java.util.List<java.lang.String> packageNames) {
        if (packageNames == null) {
            return;
        }
        java.util.Set<java.lang.String> namespacesToReset = new android.util.ArraySet<>();
        com.android.server.RescueParty.RescuePartyObserver rescuePartyObserver = com.android.server.RescueParty.RescuePartyObserver.getInstanceIfCreated();
        if (rescuePartyObserver != null) {
            for (java.lang.String packageName : packageNames) {
                java.util.Set<java.lang.String> runtimeAffectedNamespaces = rescuePartyObserver.getAffectedNamespaceSet(packageName);
                if (runtimeAffectedNamespaces != null) {
                    namespacesToReset.addAll(runtimeAffectedNamespaces);
                }
            }
        }
        java.util.Set<java.lang.String> presetAffectedNamespaces = getPresetNamespacesForPackages(packageNames);
        if (presetAffectedNamespaces != null) {
            namespacesToReset.addAll(presetAffectedNamespaces);
        }
        for (java.lang.String namespaceToReset : namespacesToReset) {
            android.provider.DeviceConfig.Properties properties = new android.provider.DeviceConfig.Properties.Builder(namespaceToReset).build();
            try {
                if (!android.provider.DeviceConfig.setProperties(properties)) {
                    com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(6, "Failed to clear properties under " + namespaceToReset + ". Running `device_config get_sync_disabled_for_tests` will confirm if config-bulk-update is enabled.");
                }
            } catch (android.provider.DeviceConfig.BadConfigException e) {
                com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(5, "namespace " + namespaceToReset + " is already banned, skip reset.");
            }
        }
    }

    private static java.util.Set<java.lang.String> getPresetNamespacesForPackages(java.util.List<java.lang.String> packageNames) {
        java.lang.String[] mappingEntries;
        int i;
        java.util.Set<java.lang.String> resultSet = new android.util.ArraySet<>();
        try {
            try {
                java.lang.String flagVal = android.provider.DeviceConfig.getString(NAMESPACE_CONFIGURATION, NAMESPACE_TO_PACKAGE_MAPPING_FLAG, "");
                mappingEntries = flagVal.split(",");
            } catch (java.lang.Exception e) {
                resultSet.clear();
                android.util.Slog.e(TAG, "Failed to read preset package to namespaces mapping.", e);
            }
        } catch (java.lang.Throwable th) {
        }
        for (i = 0; i < mappingEntries.length; i++) {
            if (!android.text.TextUtils.isEmpty(mappingEntries[i])) {
                java.lang.String[] splittedEntry = mappingEntries[i].split(":");
                if (splittedEntry.length != 2) {
                    throw new java.lang.RuntimeException("Invalid mapping entry: " + mappingEntries[i]);
                }
                java.lang.String namespace = splittedEntry[0];
                java.lang.String packageName = splittedEntry[1];
                if (packageNames.contains(packageName)) {
                    resultSet.add(namespace);
                }
                return resultSet;
            }
        }
        return resultSet;
    }

    static long getElapsedRealtime() {
        return android.os.SystemClock.elapsedRealtime();
    }

    private static class RescuePartyMonitorCallback implements android.provider.DeviceConfig.MonitorCallback {
        android.content.Context mContext;

        RescuePartyMonitorCallback(android.content.Context context) {
            this.mContext = context;
        }

        public void onNamespaceUpdate(java.lang.String updatedNamespace) {
            com.android.server.RescueParty.startObservingPackages(this.mContext, updatedNamespace);
        }

        public void onDeviceConfigAccess(java.lang.String callingPackage, java.lang.String namespace) {
            com.android.server.RescueParty.RescuePartyObserver.getInstance(this.mContext).recordDeviceConfigAccess(callingPackage, namespace);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void startObservingPackages(android.content.Context context, java.lang.String updatedNamespace) {
        com.android.server.RescueParty.RescuePartyObserver rescuePartyObserver = com.android.server.RescueParty.RescuePartyObserver.getInstance(context);
        java.util.Collection<? extends java.lang.String> callingPackages = rescuePartyObserver.getCallingPackagesSet(updatedNamespace);
        if (callingPackages == null) {
            return;
        }
        java.util.List<java.lang.String> callingPackageList = new java.util.ArrayList<>();
        callingPackageList.addAll(callingPackages);
        android.util.Slog.i(TAG, "Starting to observe: " + callingPackageList + ", updated namespace: " + updatedNamespace);
        com.android.server.PackageWatchdog.getInstance(context).startObservingHealth(rescuePartyObserver, callingPackageList, DEFAULT_OBSERVING_DURATION_MS);
    }

    private static void handleNativeRescuePartyResets() {
        if (com.android.server.am.SettingsToPropertiesMapper.isNativeFlagsResetPerformed()) {
            java.lang.String[] resetNativeCategories = com.android.server.am.SettingsToPropertiesMapper.getResetNativeCategories();
            for (int i = 0; i < resetNativeCategories.length; i++) {
                if (!NAMESPACE_CONFIGURATION.equals(resetNativeCategories[i])) {
                    android.provider.DeviceConfig.resetToDefaults(4, resetNativeCategories[i]);
                }
            }
        }
    }

    private static int getMaxRescueLevel(boolean mayPerformReboot) {
        if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
            if (!mayPerformReboot || android.os.SystemProperties.getBoolean(PROP_DISABLE_FACTORY_RESET_FLAG, false)) {
                return android.os.SystemProperties.getInt(RESCUE_NON_REBOOT_LEVEL_LIMIT, 2);
            }
            return 7;
        }
        if (!mayPerformReboot || android.os.SystemProperties.getBoolean(PROP_DISABLE_FACTORY_RESET_FLAG, false)) {
            return 3;
        }
        return 5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getRescueLevel(int mitigationCount, boolean mayPerformReboot) {
        if (mitigationCount == 1) {
            return 1;
        }
        if (mitigationCount == 2) {
            return 2;
        }
        if (mitigationCount == 3) {
            return 3;
        }
        if (mitigationCount == 4) {
            return java.lang.Math.min(getMaxRescueLevel(mayPerformReboot), 4);
        }
        if (mitigationCount >= 5) {
            return java.lang.Math.min(getMaxRescueLevel(mayPerformReboot), 5);
        }
        android.util.Slog.w(TAG, "Expected positive mitigation count, was " + mitigationCount);
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getRescueLevel(int mitigationCount, boolean mayPerformReboot, android.content.pm.VersionedPackage failedPackage) {
        if (failedPackage == null && mitigationCount > 0) {
            mitigationCount++;
        }
        if (mitigationCount == 1) {
            return 1;
        }
        if (mitigationCount == 2) {
            return 2;
        }
        if (mitigationCount == 3) {
            return java.lang.Math.min(getMaxRescueLevel(mayPerformReboot), 3);
        }
        if (mitigationCount == 4) {
            return java.lang.Math.min(getMaxRescueLevel(mayPerformReboot), 4);
        }
        if (mitigationCount == 5) {
            return java.lang.Math.min(getMaxRescueLevel(mayPerformReboot), 5);
        }
        if (mitigationCount == 6) {
            return java.lang.Math.min(getMaxRescueLevel(mayPerformReboot), 6);
        }
        if (mitigationCount >= 7) {
            return java.lang.Math.min(getMaxRescueLevel(mayPerformReboot), 7);
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void executeRescueLevel(android.content.Context context, java.lang.String failedPackage, int level) {
        android.util.Slog.w(TAG, "Attempting rescue level " + levelToString(level));
        try {
            executeRescueLevelInternal(context, level, failedPackage);
            com.android.server.EventLogTags.writeRescueSuccess(level);
            java.lang.String successMsg = "Finished rescue level " + levelToString(level);
            if (!android.text.TextUtils.isEmpty(failedPackage)) {
                successMsg = successMsg + " for package " + failedPackage;
            }
            com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(3, successMsg);
        } catch (java.lang.Throwable t) {
            logRescueException(level, failedPackage, t);
        }
    }

    private static void executeRescueLevelInternal(android.content.Context context, int level, java.lang.String failedPackage) throws java.lang.Exception {
        if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
            executeRescueLevelInternalNew(context, level, failedPackage);
        } else {
            executeRescueLevelInternalOld(context, level, failedPackage);
        }
    }

    private static void executeRescueLevelInternalOld(android.content.Context context, int level, java.lang.String failedPackage) throws java.lang.Exception {
        if (!com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.allowRescuePartyFlagResets() && level <= 3) {
            return;
        }
        com.android.server.crashrecovery.proto.CrashRecoveryStatsLog.write(122, level, levelToString(level));
        java.lang.Exception res = null;
        switch (level) {
            case 1:
                try {
                    resetAllSettingsIfNecessary(context, 2, level);
                } catch (java.lang.Exception e) {
                    res = e;
                }
                try {
                    resetDeviceConfig(context, true, failedPackage);
                } catch (java.lang.Exception e2) {
                    res = e2;
                }
                break;
            case 2:
                try {
                    resetAllSettingsIfNecessary(context, 3, level);
                } catch (java.lang.Exception e3) {
                    res = e3;
                }
                try {
                    resetDeviceConfig(context, true, failedPackage);
                } catch (java.lang.Exception e4) {
                    res = e4;
                }
                break;
            case 3:
                try {
                    resetAllSettingsIfNecessary(context, 4, level);
                } catch (java.lang.Exception e5) {
                    res = e5;
                }
                try {
                    resetDeviceConfig(context, false, failedPackage);
                } catch (java.lang.Exception e6) {
                    res = e6;
                }
                break;
            case 4:
                executeWarmReboot(context, level, failedPackage);
                break;
            case 5:
                if (isRebootPropertySet()) {
                    return;
                }
                executeFactoryReset(context, level, failedPackage);
                break;
                break;
        }
        if (res != null) {
            throw res;
        }
    }

    private static void executeRescueLevelInternalNew(android.content.Context context, int level, java.lang.String failedPackage) throws java.lang.Exception {
        com.android.server.crashrecovery.proto.CrashRecoveryStatsLog.write(122, level, levelToString(level));
        switch (level) {
            case 1:
                if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.allowRescuePartyFlagResets()) {
                    resetDeviceConfig(context, true, failedPackage);
                }
                break;
            case 2:
                if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.allowRescuePartyFlagResets()) {
                    resetDeviceConfig(context, false, failedPackage);
                }
                break;
            case 3:
                executeWarmReboot(context, level, failedPackage);
                break;
            case 4:
                resetAllSettingsIfNecessary(context, 2, level);
                break;
            case 5:
                resetAllSettingsIfNecessary(context, 3, level);
                break;
            case 6:
                resetAllSettingsIfNecessary(context, 4, level);
                break;
            case 7:
                if (!isRebootPropertySet()) {
                    executeFactoryReset(context, level, failedPackage);
                    break;
                }
                break;
        }
    }

    private static void executeWarmReboot(final android.content.Context context, final int level, final java.lang.String failedPackage) {
        setRebootProperty(true);
        java.lang.Runnable runnable = new java.lang.Runnable() { // from class: com.android.server.RescueParty$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.RescueParty.lambda$executeWarmReboot$0(context, level, failedPackage);
            }
        };
        java.lang.Thread thread = new java.lang.Thread(runnable);
        thread.start();
    }

    static /* synthetic */ void lambda$executeWarmReboot$0(android.content.Context context, int level, java.lang.String failedPackage) {
        try {
            android.os.PowerManager pm = (android.os.PowerManager) context.getSystemService(android.os.PowerManager.class);
            if (pm != null) {
                pm.reboot(TAG);
            }
        } catch (java.lang.Throwable t) {
            logRescueException(level, failedPackage, t);
        }
    }

    private static void executeFactoryReset(final android.content.Context context, final int level, final java.lang.String failedPackage) {
        setFactoryResetProperty(true);
        long now = java.lang.System.currentTimeMillis();
        setLastFactoryResetTimeMs(now);
        java.lang.Runnable runnable = new java.lang.Runnable() { // from class: com.android.server.RescueParty.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    android.os.RecoverySystem.rebootPromptAndWipeUserData(context, com.android.server.RescueParty.TAG);
                } catch (java.lang.Throwable t) {
                    com.android.server.RescueParty.logRescueException(level, failedPackage, t);
                }
            }
        };
        java.lang.Thread thread = new java.lang.Thread(runnable);
        thread.start();
    }

    private static java.lang.String getCompleteMessage(java.lang.Throwable t) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append(t.getMessage());
        while (true) {
            java.lang.Throwable cause = t.getCause();
            t = cause;
            if (cause != null) {
                builder.append(": ").append(t.getMessage());
            } else {
                return builder.toString();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logRescueException(int level, java.lang.String failedPackageName, java.lang.Throwable t) {
        java.lang.String msg = getCompleteMessage(t);
        com.android.server.EventLogTags.writeRescueFailure(level, msg);
        java.lang.String failureMsg = "Failed rescue level " + levelToString(level);
        if (!android.text.TextUtils.isEmpty(failedPackageName)) {
            failureMsg = failureMsg + " for package " + failedPackageName;
        }
        com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(6, failureMsg + ": " + msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int mapRescueLevelToUserImpact(int rescueLevel) {
        if (!com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
            switch (rescueLevel) {
                case 1:
                case 2:
                    return 10;
                case 3:
                case 4:
                    return 50;
                case 5:
                    return 100;
                default:
                    return 0;
            }
        }
        switch (rescueLevel) {
            case 1:
                return 10;
            case 2:
                return 40;
            case 3:
                return 50;
            case 4:
                return 71;
            case 5:
                return 75;
            case 6:
                return 80;
            case 7:
                return 100;
            default:
                return 0;
        }
    }

    private static void resetAllSettingsIfNecessary(android.content.Context context, int mode, int level) throws java.lang.Exception {
        if (getMaxRescueLevelAttempted() >= level) {
            return;
        }
        setMaxRescueLevelAttempted(level);
        java.lang.Exception res = null;
        android.content.ContentResolver resolver = context.getContentResolver();
        try {
            android.provider.Settings.Global.resetToDefaultsAsUser(resolver, null, mode, android.os.UserHandle.SYSTEM.getIdentifier());
        } catch (java.lang.Exception e) {
            res = new java.lang.RuntimeException("Failed to reset global settings", e);
        }
        for (int userId : getAllUserIds()) {
            try {
                android.provider.Settings.Secure.resetToDefaultsAsUser(resolver, null, mode, userId);
            } catch (java.lang.Exception e2) {
                res = new java.lang.RuntimeException("Failed to reset secure settings for " + userId, e2);
            }
        }
        if (res != null) {
            throw res;
        }
    }

    private static void resetDeviceConfig(android.content.Context context, boolean isScoped, java.lang.String failedPackage) throws java.lang.Exception {
        context.getContentResolver();
        try {
            if (!isScoped || failedPackage == null) {
                resetAllAffectedNamespaces(context);
            } else {
                performScopedReset(context, failedPackage);
            }
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException("Failed to reset config settings", e);
        }
    }

    private static void resetAllAffectedNamespaces(android.content.Context context) {
        com.android.server.RescueParty.RescuePartyObserver rescuePartyObserver = com.android.server.RescueParty.RescuePartyObserver.getInstance(context);
        java.util.Set<java.lang.String> allAffectedNamespaces = rescuePartyObserver.getAllAffectedNamespaceSet();
        android.util.Slog.w(TAG, "Performing reset for all affected namespaces: " + java.util.Arrays.toString(allAffectedNamespaces.toArray()));
        for (java.lang.String namespace : allAffectedNamespaces) {
            if (!NAMESPACE_CONFIGURATION.equals(namespace)) {
                android.provider.DeviceConfig.resetToDefaults(4, namespace);
            }
        }
    }

    private static void performScopedReset(android.content.Context context, java.lang.String failedPackage) {
        com.android.server.RescueParty.RescuePartyObserver rescuePartyObserver = com.android.server.RescueParty.RescuePartyObserver.getInstance(context);
        java.util.Set<java.lang.String> affectedNamespaces = rescuePartyObserver.getAffectedNamespaceSet(failedPackage);
        if (affectedNamespaces != null) {
            android.util.Slog.w(TAG, "Performing scoped reset for package: " + failedPackage + ", affected namespaces: " + java.util.Arrays.toString(affectedNamespaces.toArray()));
            for (java.lang.String namespace : affectedNamespaces) {
                if (!NAMESPACE_CONFIGURATION.equals(namespace)) {
                    android.provider.DeviceConfig.resetToDefaults(4, namespace);
                }
            }
        }
    }

    public static class RescuePartyObserver implements com.android.server.PackageWatchdog.PackageHealthObserver {
        static com.android.server.RescueParty.RescuePartyObserver sRescuePartyObserver;
        private final android.content.Context mContext;
        private final java.util.Map<java.lang.String, java.util.Set<java.lang.String>> mCallingPackageNamespaceSetMap = new java.util.HashMap();
        private final java.util.Map<java.lang.String, java.util.Set<java.lang.String>> mNamespaceCallingPackageSetMap = new java.util.HashMap();

        private RescuePartyObserver(android.content.Context context) {
            this.mContext = context;
        }

        public static com.android.server.RescueParty.RescuePartyObserver getInstance(android.content.Context context) {
            com.android.server.RescueParty.RescuePartyObserver rescuePartyObserver;
            synchronized (com.android.server.RescueParty.RescuePartyObserver.class) {
                if (sRescuePartyObserver == null) {
                    sRescuePartyObserver = new com.android.server.RescueParty.RescuePartyObserver(context);
                }
                rescuePartyObserver = sRescuePartyObserver;
            }
            return rescuePartyObserver;
        }

        public static com.android.server.RescueParty.RescuePartyObserver getInstanceIfCreated() {
            com.android.server.RescueParty.RescuePartyObserver rescuePartyObserver;
            synchronized (com.android.server.RescueParty.RescuePartyObserver.class) {
                rescuePartyObserver = sRescuePartyObserver;
            }
            return rescuePartyObserver;
        }

        static void reset() {
            synchronized (com.android.server.RescueParty.RescuePartyObserver.class) {
                sRescuePartyObserver = null;
            }
        }

        @Override // com.android.server.PackageWatchdog.PackageHealthObserver
        public int onHealthCheckFailed(android.content.pm.VersionedPackage failedPackage, int failureReason, int mitigationCount) {
            if (com.android.server.RescueParty.isDisabled()) {
                return 0;
            }
            if (failureReason == 3 || failureReason == 4) {
                if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
                    return com.android.server.RescueParty.mapRescueLevelToUserImpact(com.android.server.RescueParty.getRescueLevel(mitigationCount, mayPerformReboot(failedPackage), failedPackage));
                }
                return com.android.server.RescueParty.mapRescueLevelToUserImpact(com.android.server.RescueParty.getRescueLevel(mitigationCount, mayPerformReboot(failedPackage)));
            }
            return 0;
        }

        @Override // com.android.server.PackageWatchdog.PackageHealthObserver
        public boolean execute(android.content.pm.VersionedPackage failedPackage, int failureReason, int mitigationCount) {
            int level;
            if (com.android.server.RescueParty.isDisabled()) {
                return false;
            }
            if (failureReason != 3 && failureReason != 4) {
                return false;
            }
            if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
                level = com.android.server.RescueParty.getRescueLevel(mitigationCount, mayPerformReboot(failedPackage), failedPackage);
            } else {
                level = com.android.server.RescueParty.getRescueLevel(mitigationCount, mayPerformReboot(failedPackage));
            }
            com.android.server.RescueParty.executeRescueLevel(this.mContext, failedPackage == null ? null : failedPackage.getPackageName(), level);
            return true;
        }

        @Override // com.android.server.PackageWatchdog.PackageHealthObserver
        public boolean isPersistent() {
            return true;
        }

        @Override // com.android.server.PackageWatchdog.PackageHealthObserver
        public boolean mayObservePackage(java.lang.String packageName) {
            android.content.pm.PackageManager pm = this.mContext.getPackageManager();
            try {
                if (pm.getModuleInfo(packageName, 0) != null) {
                    return true;
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException | java.lang.IllegalStateException e) {
            }
            return isPersistentSystemApp(packageName);
        }

        @Override // com.android.server.PackageWatchdog.PackageHealthObserver
        public int onBootLoop(int mitigationCount) {
            if (com.android.server.RescueParty.isDisabled()) {
                return 0;
            }
            if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
                return com.android.server.RescueParty.mapRescueLevelToUserImpact(com.android.server.RescueParty.getRescueLevel(mitigationCount, true, null));
            }
            return com.android.server.RescueParty.mapRescueLevelToUserImpact(com.android.server.RescueParty.getRescueLevel(mitigationCount, true));
        }

        @Override // com.android.server.PackageWatchdog.PackageHealthObserver
        public boolean executeBootLoopMitigation(int mitigationCount) {
            if (com.android.server.RescueParty.isDisabled()) {
                return false;
            }
            boolean mayPerformReboot = !shouldThrottleReboot();
            int level = com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection() ? com.android.server.RescueParty.getRescueLevel(mitigationCount, mayPerformReboot, null) : com.android.server.RescueParty.getRescueLevel(mitigationCount, mayPerformReboot);
            com.android.server.RescueParty.executeRescueLevel(this.mContext, null, level);
            return true;
        }

        @Override // com.android.server.PackageWatchdog.PackageHealthObserver
        public java.lang.String getName() {
            return com.android.server.RescueParty.NAME;
        }

        private boolean mayPerformReboot(android.content.pm.VersionedPackage failingPackage) {
            if (failingPackage == null || shouldThrottleReboot()) {
                return false;
            }
            return isPersistentSystemApp(failingPackage.getPackageName());
        }

        private boolean shouldThrottleReboot() {
            java.lang.Long lastResetTime = java.lang.Long.valueOf(com.android.server.RescueParty.getLastFactoryResetTimeMs());
            long now = java.lang.System.currentTimeMillis();
            long throttleDurationMin = android.os.SystemProperties.getLong(com.android.server.RescueParty.PROP_THROTTLE_DURATION_MIN_FLAG, com.android.server.RescueParty.DEFAULT_FACTORY_RESET_THROTTLE_DURATION_MIN);
            return now < lastResetTime.longValue() + java.util.concurrent.TimeUnit.MINUTES.toMillis(throttleDurationMin);
        }

        private boolean isPersistentSystemApp(java.lang.String packageName) {
            android.content.pm.PackageManager pm = this.mContext.getPackageManager();
            try {
                android.content.pm.ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
                return (info.flags & 9) == 9;
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void recordDeviceConfigAccess(java.lang.String callingPackage, java.lang.String namespace) {
            java.util.Set<java.lang.String> namespaceSet = this.mCallingPackageNamespaceSetMap.get(callingPackage);
            if (namespaceSet == null) {
                namespaceSet = new android.util.ArraySet();
                this.mCallingPackageNamespaceSetMap.put(callingPackage, namespaceSet);
            }
            namespaceSet.add(namespace);
            java.util.Set<java.lang.String> callingPackageSet = this.mNamespaceCallingPackageSetMap.get(namespace);
            if (callingPackageSet == null) {
                callingPackageSet = new android.util.ArraySet();
            }
            callingPackageSet.add(callingPackage);
            this.mNamespaceCallingPackageSetMap.put(namespace, callingPackageSet);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized java.util.Set<java.lang.String> getAffectedNamespaceSet(java.lang.String failedPackage) {
            return this.mCallingPackageNamespaceSetMap.get(failedPackage);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized java.util.Set<java.lang.String> getAllAffectedNamespaceSet() {
            return new java.util.HashSet(this.mNamespaceCallingPackageSetMap.keySet());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized java.util.Set<java.lang.String> getCallingPackagesSet(java.lang.String namespace) {
            return this.mNamespaceCallingPackageSetMap.get(namespace);
        }
    }

    private static int[] getAllUserIds() {
        int systemUserId = android.os.UserHandle.SYSTEM.getIdentifier();
        int[] userIds = {systemUserId};
        try {
            for (java.io.File file : android.os.FileUtils.listFilesOrEmpty(android.os.Environment.getDataSystemDeDirectory())) {
                try {
                    int userId = java.lang.Integer.parseInt(file.getName());
                    if (userId != systemUserId) {
                        userIds = com.android.internal.util.ArrayUtils.appendInt(userIds, userId);
                    }
                } catch (java.lang.NumberFormatException e) {
                }
            }
        } catch (java.lang.Throwable t) {
            android.util.Slog.w(TAG, "Trouble discovering users", t);
        }
        return userIds;
    }

    private static boolean isUsbActive() {
        if (android.os.SystemProperties.getBoolean(PROP_VIRTUAL_DEVICE, false)) {
            android.util.Slog.v(TAG, "Assuming virtual device is connected over USB");
            return true;
        }
        try {
            java.lang.String state = android.os.FileUtils.readTextFile(new java.io.File("/sys/class/android_usb/android0/state"), 128, "");
            return "CONFIGURED".equals(state.trim());
        } catch (java.lang.Throwable t) {
            android.util.Slog.w(TAG, "Failed to determine if device was on USB", t);
            return false;
        }
    }

    private static java.lang.String levelToString(int level) {
        if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
            switch (level) {
                case 0:
                    return "NONE";
                case 1:
                    return "SCOPED_DEVICE_CONFIG_RESET";
                case 2:
                    return "ALL_DEVICE_CONFIG_RESET";
                case 3:
                    return "WARM_REBOOT";
                case 4:
                    return "RESET_SETTINGS_UNTRUSTED_DEFAULTS";
                case 5:
                    return "RESET_SETTINGS_UNTRUSTED_CHANGES";
                case 6:
                    return "RESET_SETTINGS_TRUSTED_DEFAULTS";
                case 7:
                    return "FACTORY_RESET";
                default:
                    return java.lang.Integer.toString(level);
            }
        }
        switch (level) {
            case 0:
                return "NONE";
            case 1:
                return "RESET_SETTINGS_UNTRUSTED_DEFAULTS";
            case 2:
                return "RESET_SETTINGS_UNTRUSTED_CHANGES";
            case 3:
                return "RESET_SETTINGS_TRUSTED_DEFAULTS";
            case 4:
                return "WARM_REBOOT";
            case 5:
                return "FACTORY_RESET";
            default:
                return java.lang.Integer.toString(level);
        }
    }
}
