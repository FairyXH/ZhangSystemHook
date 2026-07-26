package com.android.server.compat.overrides;

/* JADX INFO: loaded from: classes.dex */
public final class AppCompatOverridesService {
    private static final java.util.List<java.lang.String> SUPPORTED_NAMESPACES = java.util.Arrays.asList("app_compat_overrides");
    private static final java.lang.String TAG = "AppCompatOverridesService";
    private final android.content.Context mContext;
    private final java.util.List<com.android.server.compat.overrides.AppCompatOverridesService.DeviceConfigListener> mDeviceConfigListeners;
    private final com.android.server.compat.overrides.AppCompatOverridesParser mOverridesParser;
    private final android.content.pm.PackageManager mPackageManager;
    private final com.android.server.compat.overrides.AppCompatOverridesService.PackageReceiver mPackageReceiver;
    private final com.android.internal.compat.IPlatformCompat mPlatformCompat;
    private final java.util.List<java.lang.String> mSupportedNamespaces;

    private AppCompatOverridesService(android.content.Context context) {
        this(context, com.android.internal.compat.IPlatformCompat.Stub.asInterface(android.os.ServiceManager.getService("platform_compat")), SUPPORTED_NAMESPACES);
    }

    /* JADX WARN: Multi-variable type inference failed */
    AppCompatOverridesService(android.content.Context context, com.android.internal.compat.IPlatformCompat iPlatformCompat, java.util.List<java.lang.String> list) {
        this.mContext = context;
        this.mPackageManager = this.mContext.getPackageManager();
        this.mPlatformCompat = iPlatformCompat;
        this.mSupportedNamespaces = list;
        this.mOverridesParser = new com.android.server.compat.overrides.AppCompatOverridesParser(this.mPackageManager);
        java.lang.Object[] objArr = 0;
        this.mPackageReceiver = new com.android.server.compat.overrides.AppCompatOverridesService.PackageReceiver(this.mContext);
        this.mDeviceConfigListeners = new java.util.ArrayList();
        java.util.Iterator<java.lang.String> it = this.mSupportedNamespaces.iterator();
        while (it.hasNext()) {
            this.mDeviceConfigListeners.add(new com.android.server.compat.overrides.AppCompatOverridesService.DeviceConfigListener(this.mContext, it.next()));
        }
    }

    public void finalize() {
        unregisterDeviceConfigListeners();
        unregisterPackageReceiver();
    }

    void registerDeviceConfigListeners() {
        for (com.android.server.compat.overrides.AppCompatOverridesService.DeviceConfigListener listener : this.mDeviceConfigListeners) {
            listener.register();
        }
    }

    private void unregisterDeviceConfigListeners() {
        for (com.android.server.compat.overrides.AppCompatOverridesService.DeviceConfigListener listener : this.mDeviceConfigListeners) {
            listener.unregister();
        }
    }

    void registerPackageReceiver() {
        this.mPackageReceiver.register();
    }

    private void unregisterPackageReceiver() {
        this.mPackageReceiver.unregister();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyAllOverrides(java.lang.String namespace, java.util.Set<java.lang.Long> ownedChangeIds, java.util.Map<java.lang.String, java.util.Set<java.lang.Long>> packageToChangeIdsToSkip) {
        applyOverrides(android.provider.DeviceConfig.getProperties(namespace, new java.lang.String[0]), ownedChangeIds, packageToChangeIdsToSkip);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyOverrides(android.provider.DeviceConfig.Properties properties, java.util.Set<java.lang.Long> ownedChangeIds, java.util.Map<java.lang.String, java.util.Set<java.lang.Long>> packageToChangeIdsToSkip) {
        java.util.Set<java.lang.String> packageNames = new android.util.ArraySet<>(properties.getKeyset());
        packageNames.remove("owned_change_ids");
        packageNames.remove("remove_overrides");
        java.util.Map<java.lang.String, com.android.internal.compat.CompatibilityOverrideConfig> packageNameToOverridesToAdd = new android.util.ArrayMap<>();
        java.util.Map<java.lang.String, com.android.internal.compat.CompatibilityOverridesToRemoveConfig> packageNameToOverridesToRemove = new android.util.ArrayMap<>();
        for (java.lang.String packageName : packageNames) {
            java.util.Set<java.lang.Long> changeIdsToSkip = packageToChangeIdsToSkip.getOrDefault(packageName, java.util.Collections.emptySet());
            java.util.Map<java.lang.Long, android.app.compat.PackageOverride> overridesToAdd = java.util.Collections.emptyMap();
            java.lang.Long versionCode = getVersionCodeOrNull(packageName);
            if (versionCode != null) {
                overridesToAdd = this.mOverridesParser.parsePackageOverrides(properties.getString(packageName, ""), packageName, versionCode.longValue(), changeIdsToSkip);
            }
            if (!overridesToAdd.isEmpty()) {
                packageNameToOverridesToAdd.put(packageName, new com.android.internal.compat.CompatibilityOverrideConfig(overridesToAdd));
            }
            java.util.Set<java.lang.Long> overridesToRemove = new android.util.ArraySet<>();
            for (java.lang.Long changeId : ownedChangeIds) {
                if (!overridesToAdd.containsKey(changeId) && !changeIdsToSkip.contains(changeId)) {
                    overridesToRemove.add(changeId);
                }
            }
            if (!overridesToRemove.isEmpty()) {
                packageNameToOverridesToRemove.put(packageName, new com.android.internal.compat.CompatibilityOverridesToRemoveConfig(overridesToRemove));
            }
        }
        putAllPackageOverrides(packageNameToOverridesToAdd);
        removeAllPackageOverrides(packageNameToOverridesToRemove);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addAllPackageOverrides(java.lang.String packageName) {
        java.lang.Long versionCode = getVersionCodeOrNull(packageName);
        if (versionCode == null) {
            return;
        }
        for (java.lang.String namespace : this.mSupportedNamespaces) {
            java.util.Set<java.lang.Long> ownedChangeIds = getOwnedChangeIds(namespace);
            putPackageOverrides(packageName, this.mOverridesParser.parsePackageOverrides(android.provider.DeviceConfig.getString(namespace, packageName, ""), packageName, versionCode.longValue(), getOverridesToRemove(namespace, ownedChangeIds).getOrDefault(packageName, java.util.Collections.emptySet())));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeAllPackageOverrides(java.lang.String packageName) {
        for (java.lang.String namespace : this.mSupportedNamespaces) {
            if (!android.provider.DeviceConfig.getString(namespace, packageName, "").isEmpty()) {
                removePackageOverrides(packageName, getOwnedChangeIds(namespace));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeOverrides(java.util.Map<java.lang.String, java.util.Set<java.lang.Long>> packageNameToOverridesToRemove) {
        java.util.Map<java.lang.String, com.android.internal.compat.CompatibilityOverridesToRemoveConfig> packageNameToConfig = new android.util.ArrayMap<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.Long>> packageNameAndChangeIds : packageNameToOverridesToRemove.entrySet()) {
            packageNameToConfig.put(packageNameAndChangeIds.getKey(), new com.android.internal.compat.CompatibilityOverridesToRemoveConfig(packageNameAndChangeIds.getValue()));
        }
        removeAllPackageOverrides(packageNameToConfig);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.Map<java.lang.String, java.util.Set<java.lang.Long>> getOverridesToRemove(java.lang.String namespace, java.util.Set<java.lang.Long> ownedChangeIds) {
        return this.mOverridesParser.parseRemoveOverrides(android.provider.DeviceConfig.getString(namespace, "remove_overrides", ""), ownedChangeIds);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.util.Set<java.lang.Long> getOwnedChangeIds(java.lang.String namespace) {
        return com.android.server.compat.overrides.AppCompatOverridesParser.parseOwnedChangeIds(android.provider.DeviceConfig.getString(namespace, "owned_change_ids", ""));
    }

    private void putAllPackageOverrides(java.util.Map<java.lang.String, com.android.internal.compat.CompatibilityOverrideConfig> packageNameToOverrides) {
        if (packageNameToOverrides.isEmpty()) {
            return;
        }
        com.android.internal.compat.CompatibilityOverridesByPackageConfig config = new com.android.internal.compat.CompatibilityOverridesByPackageConfig(packageNameToOverrides);
        try {
            this.mPlatformCompat.putAllOverridesOnReleaseBuilds(config);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to call IPlatformCompat#putAllOverridesOnReleaseBuilds", e);
        }
    }

    private void putPackageOverrides(java.lang.String packageName, java.util.Map<java.lang.Long, android.app.compat.PackageOverride> overridesToAdd) {
        if (overridesToAdd.isEmpty()) {
            return;
        }
        com.android.internal.compat.CompatibilityOverrideConfig config = new com.android.internal.compat.CompatibilityOverrideConfig(overridesToAdd);
        try {
            this.mPlatformCompat.putOverridesOnReleaseBuilds(config, packageName);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to call IPlatformCompat#putOverridesOnReleaseBuilds", e);
        }
    }

    private void removeAllPackageOverrides(java.util.Map<java.lang.String, com.android.internal.compat.CompatibilityOverridesToRemoveConfig> packageNameToOverridesToRemove) {
        if (packageNameToOverridesToRemove.isEmpty()) {
            return;
        }
        com.android.internal.compat.CompatibilityOverridesToRemoveByPackageConfig config = new com.android.internal.compat.CompatibilityOverridesToRemoveByPackageConfig(packageNameToOverridesToRemove);
        try {
            this.mPlatformCompat.removeAllOverridesOnReleaseBuilds(config);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to call IPlatformCompat#removeAllOverridesOnReleaseBuilds", e);
        }
    }

    private void removePackageOverrides(java.lang.String packageName, java.util.Set<java.lang.Long> overridesToRemove) {
        if (overridesToRemove.isEmpty()) {
            return;
        }
        com.android.internal.compat.CompatibilityOverridesToRemoveConfig config = new com.android.internal.compat.CompatibilityOverridesToRemoveConfig(overridesToRemove);
        try {
            this.mPlatformCompat.removeOverridesOnReleaseBuilds(config, packageName);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to call IPlatformCompat#removeOverridesOnReleaseBuilds", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInstalledForAnyUser(java.lang.String packageName) {
        return getVersionCodeOrNull(packageName) != null;
    }

    private java.lang.Long getVersionCodeOrNull(java.lang.String packageName) {
        try {
            android.content.pm.ApplicationInfo applicationInfo = this.mPackageManager.getApplicationInfo(packageName, 4194304);
            return java.lang.Long.valueOf(applicationInfo.longVersionCode);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static final class Lifecycle extends com.android.server.SystemService {
        private com.android.server.compat.overrides.AppCompatOverridesService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            this.mService = new com.android.server.compat.overrides.AppCompatOverridesService(getContext());
            this.mService.registerDeviceConfigListeners();
            this.mService.registerPackageReceiver();
        }
    }

    private final class DeviceConfigListener implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        private final android.content.Context mContext;
        private final java.lang.String mNamespace;

        private DeviceConfigListener(android.content.Context context, java.lang.String namespace) {
            this.mContext = context;
            this.mNamespace = namespace;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void register() {
            android.provider.DeviceConfig.addOnPropertiesChangedListener(this.mNamespace, this.mContext.getMainExecutor(), this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void unregister() {
            android.provider.DeviceConfig.removeOnPropertiesChangedListener(this);
        }

        public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
            boolean removeOverridesFlagChanged = properties.getKeyset().contains("remove_overrides");
            boolean ownedChangedIdsFlagChanged = properties.getKeyset().contains("owned_change_ids");
            java.util.Set<java.lang.Long> ownedChangeIds = com.android.server.compat.overrides.AppCompatOverridesService.getOwnedChangeIds(this.mNamespace);
            java.util.Map<java.lang.String, java.util.Set<java.lang.Long>> overridesToRemove = com.android.server.compat.overrides.AppCompatOverridesService.this.getOverridesToRemove(this.mNamespace, ownedChangeIds);
            if (removeOverridesFlagChanged || ownedChangedIdsFlagChanged) {
                com.android.server.compat.overrides.AppCompatOverridesService.this.removeOverrides(overridesToRemove);
            }
            if (removeOverridesFlagChanged) {
                com.android.server.compat.overrides.AppCompatOverridesService.this.applyAllOverrides(this.mNamespace, ownedChangeIds, overridesToRemove);
            } else {
                com.android.server.compat.overrides.AppCompatOverridesService.this.applyOverrides(properties, ownedChangeIds, overridesToRemove);
            }
        }
    }

    private final class PackageReceiver extends android.content.BroadcastReceiver {
        private final android.content.Context mContext;
        private final android.content.IntentFilter mIntentFilter;

        private PackageReceiver(android.content.Context context) {
            this.mContext = context;
            this.mIntentFilter = new android.content.IntentFilter();
            this.mIntentFilter.addAction("android.intent.action.PACKAGE_ADDED");
            this.mIntentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
            this.mIntentFilter.addCategory("oplusBrEx@android.intent.action.PACKAGE_CHANGED@PACKAGE=ENTIRE_PKG_CHANGED");
            this.mIntentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
            this.mIntentFilter.addDataScheme("package");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void register() {
            this.mContext.registerReceiverForAllUsers(this, this.mIntentFilter, null, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void unregister() {
            this.mContext.unregisterReceiver(this);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:22:0x0044  */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onReceive(android.content.Context r7, android.content.Intent r8) {
            /*
                r6 = this;
                android.net.Uri r0 = r8.getData()
                java.lang.String r1 = "AppCompatOverridesService"
                if (r0 != 0) goto Le
                java.lang.String r2 = "Failed to get package name in package receiver"
                android.util.Slog.w(r1, r2)
                return
            Le:
                java.lang.String r2 = r0.getSchemeSpecificPart()
                java.lang.String r3 = r8.getAction()
                if (r3 != 0) goto L1e
                java.lang.String r4 = "Failed to get action in package receiver"
                android.util.Slog.w(r1, r4)
                return
            L1e:
                int r4 = r3.hashCode()
                switch(r4) {
                    case 172491798: goto L3a;
                    case 525384130: goto L30;
                    case 1544582882: goto L26;
                    default: goto L25;
                }
            L25:
                goto L44
            L26:
                java.lang.String r4 = "android.intent.action.PACKAGE_ADDED"
                boolean r4 = r3.equals(r4)
                if (r4 == 0) goto L25
                r4 = 0
                goto L45
            L30:
                java.lang.String r4 = "android.intent.action.PACKAGE_REMOVED"
                boolean r4 = r3.equals(r4)
                if (r4 == 0) goto L25
                r4 = 2
                goto L45
            L3a:
                java.lang.String r4 = "android.intent.action.PACKAGE_CHANGED"
                boolean r4 = r3.equals(r4)
                if (r4 == 0) goto L25
                r4 = 1
                goto L45
            L44:
                r4 = -1
            L45:
                switch(r4) {
                    case 0: goto L6d;
                    case 1: goto L6d;
                    case 2: goto L5f;
                    default: goto L48;
                }
            L48:
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "Unsupported action in package receiver: "
                java.lang.StringBuilder r4 = r4.append(r5)
                java.lang.StringBuilder r4 = r4.append(r3)
                java.lang.String r4 = r4.toString()
                android.util.Slog.w(r1, r4)
                goto L73
            L5f:
                com.android.server.compat.overrides.AppCompatOverridesService r1 = com.android.server.compat.overrides.AppCompatOverridesService.this
                boolean r1 = com.android.server.compat.overrides.AppCompatOverridesService.m2859$$Nest$misInstalledForAnyUser(r1, r2)
                if (r1 != 0) goto L73
                com.android.server.compat.overrides.AppCompatOverridesService r1 = com.android.server.compat.overrides.AppCompatOverridesService.this
                com.android.server.compat.overrides.AppCompatOverridesService.m2860$$Nest$mremoveAllPackageOverrides(r1, r2)
                goto L73
            L6d:
                com.android.server.compat.overrides.AppCompatOverridesService r1 = com.android.server.compat.overrides.AppCompatOverridesService.this
                com.android.server.compat.overrides.AppCompatOverridesService.m2855$$Nest$maddAllPackageOverrides(r1, r2)
            L73:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.compat.overrides.AppCompatOverridesService.PackageReceiver.onReceive(android.content.Context, android.content.Intent):void");
        }
    }
}
