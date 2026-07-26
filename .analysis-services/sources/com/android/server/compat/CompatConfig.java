package com.android.server.compat;

/* JADX INFO: loaded from: classes.dex */
final class CompatConfig {
    private static final java.lang.String APP_COMPAT_DATA_DIR = "/data/misc/appcompat";
    private static final java.lang.String OVERRIDES_FILE = "compat_framework_overrides.xml";
    private static final java.lang.String STATIC_OVERRIDES_PRODUCT_DIR = "/product/etc/appcompat";
    private static final java.lang.String TAG = "CompatConfig";
    private final com.android.internal.compat.AndroidBuildClassifier mAndroidBuildClassifier;
    private java.io.File mBackupOverridesFile;
    private android.content.Context mContext;
    private final com.android.server.compat.OverrideValidatorImpl mOverrideValidator;
    private java.io.File mOverridesFile;
    private final java.util.concurrent.ConcurrentHashMap<java.lang.Long, com.android.server.compat.CompatChange> mChanges = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.lang.Object mOverridesFileLock = new java.lang.Object();

    CompatConfig(com.android.internal.compat.AndroidBuildClassifier androidBuildClassifier, android.content.Context context) {
        this.mOverrideValidator = new com.android.server.compat.OverrideValidatorImpl(androidBuildClassifier, context, this);
        this.mAndroidBuildClassifier = androidBuildClassifier;
        this.mContext = context;
    }

    static com.android.server.compat.CompatConfig create(com.android.internal.compat.AndroidBuildClassifier androidBuildClassifier, android.content.Context context) {
        com.android.server.compat.CompatConfig config = new com.android.server.compat.CompatConfig(androidBuildClassifier, context);
        config.initConfigFromLib(android.os.Environment.buildPath(android.os.Environment.getRootDirectory(), new java.lang.String[]{"etc", "compatconfig"}));
        config.initConfigFromLib(android.os.Environment.buildPath(android.os.Environment.getRootDirectory(), new java.lang.String[]{"system_ext", "etc", "compatconfig"}));
        java.util.List<com.android.server.pm.ApexManager.ActiveApexInfo> apexes = com.android.server.pm.ApexManager.getInstance().getActiveApexInfos();
        for (com.android.server.pm.ApexManager.ActiveApexInfo apex : apexes) {
            config.initConfigFromLib(android.os.Environment.buildPath(apex.apexDirectory, new java.lang.String[]{"etc", "compatconfig"}));
        }
        config.initOverrides();
        config.invalidateCache();
        return config;
    }

    void addChange(com.android.server.compat.CompatChange change) {
        this.mChanges.put(java.lang.Long.valueOf(change.getId()), change);
    }

    long[] getDisabledChanges(android.content.pm.ApplicationInfo app) {
        android.util.LongArray disabled = new android.util.LongArray();
        for (com.android.server.compat.CompatChange c : this.mChanges.values()) {
            if (!c.isEnabled(app, this.mAndroidBuildClassifier)) {
                disabled.add(c.getId());
            }
        }
        long[] sortedChanges = disabled.toArray();
        java.util.Arrays.sort(sortedChanges);
        return sortedChanges;
    }

    long[] getLoggableChanges(android.content.pm.ApplicationInfo app) {
        android.util.LongArray loggable = new android.util.LongArray(this.mChanges.size());
        for (com.android.server.compat.CompatChange c : this.mChanges.values()) {
            long changeId = c.getId();
            boolean isLatestSdk = isChangeTargetingLatestSdk(c, app.targetSdkVersion);
            if (c.isEnabled(app, this.mAndroidBuildClassifier) && isLatestSdk) {
                loggable.add(changeId);
            }
        }
        long[] sortedChanges = loggable.toArray();
        java.util.Arrays.sort(sortedChanges);
        return sortedChanges;
    }

    boolean isChangeTargetingLatestSdk(com.android.server.compat.CompatChange c, int appSdkVersion) {
        int maxTargetSdk = maxTargetSdkForCompatChange(c) + 1;
        if (maxTargetSdk <= 0) {
            return false;
        }
        return maxTargetSdk == 10000 || maxTargetSdk == appSdkVersion;
    }

    com.android.server.compat.CompatChange getCompatChange(long changeId) {
        return this.mChanges.get(java.lang.Long.valueOf(changeId));
    }

    long lookupChangeId(java.lang.String name) {
        for (com.android.server.compat.CompatChange c : this.mChanges.values()) {
            if (android.text.TextUtils.equals(c.getName(), name)) {
                return c.getId();
            }
        }
        return -1L;
    }

    boolean isChangeEnabled(long changeId, android.content.pm.ApplicationInfo app) {
        com.android.server.compat.CompatChange c = this.mChanges.get(java.lang.Long.valueOf(changeId));
        return isChangeEnabled(c, app);
    }

    boolean isChangeEnabled(com.android.server.compat.CompatChange c, android.content.pm.ApplicationInfo app) {
        if (c == null) {
            return true;
        }
        return c.isEnabled(app, this.mAndroidBuildClassifier);
    }

    boolean willChangeBeEnabled(long changeId, java.lang.String packageName) {
        com.android.server.compat.CompatChange c = this.mChanges.get(java.lang.Long.valueOf(changeId));
        if (c == null) {
            return true;
        }
        return c.willBeEnabled(packageName);
    }

    synchronized boolean addOverride(long changeId, java.lang.String packageName, boolean enabled) {
        boolean alreadyKnown;
        alreadyKnown = addOverrideUnsafe(changeId, packageName, new android.app.compat.PackageOverride.Builder().setEnabled(enabled).build());
        saveOverrides();
        invalidateCache();
        return alreadyKnown;
    }

    synchronized void addAllPackageOverrides(com.android.internal.compat.CompatibilityOverridesByPackageConfig overridesByPackage, boolean skipUnknownChangeIds) {
        for (java.lang.String packageName : overridesByPackage.packageNameToOverrides.keySet()) {
            addPackageOverridesWithoutSaving((com.android.internal.compat.CompatibilityOverrideConfig) overridesByPackage.packageNameToOverrides.get(packageName), packageName, skipUnknownChangeIds);
        }
        saveOverrides();
        invalidateCache();
    }

    synchronized void addPackageOverrides(com.android.internal.compat.CompatibilityOverrideConfig overrides, java.lang.String packageName, boolean skipUnknownChangeIds) {
        addPackageOverridesWithoutSaving(overrides, packageName, skipUnknownChangeIds);
        saveOverrides();
        invalidateCache();
    }

    private void addPackageOverridesWithoutSaving(com.android.internal.compat.CompatibilityOverrideConfig overrides, java.lang.String packageName, boolean skipUnknownChangeIds) {
        for (java.lang.Long changeId : overrides.overrides.keySet()) {
            if (skipUnknownChangeIds && !isKnownChangeId(changeId.longValue())) {
                android.util.Slog.w(TAG, "Trying to add overrides for unknown Change ID " + changeId + ". Skipping Change ID.");
            } else {
                addOverrideUnsafe(changeId.longValue(), packageName, (android.app.compat.PackageOverride) overrides.overrides.get(changeId));
            }
        }
    }

    private boolean addOverrideUnsafe(final long changeId, java.lang.String packageName, android.app.compat.PackageOverride overrides) {
        final java.util.concurrent.atomic.AtomicBoolean alreadyKnown = new java.util.concurrent.atomic.AtomicBoolean(true);
        com.android.internal.compat.OverrideAllowedState allowedState = this.mOverrideValidator.getOverrideAllowedState(changeId, packageName);
        allowedState.enforce(changeId, packageName);
        java.lang.Long versionCode = getVersionCodeOrNull(packageName);
        com.android.server.compat.CompatChange c = this.mChanges.computeIfAbsent(java.lang.Long.valueOf(changeId), new java.util.function.Function() { // from class: com.android.server.compat.CompatConfig$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.compat.CompatConfig.lambda$addOverrideUnsafe$0(alreadyKnown, changeId, (java.lang.Long) obj);
            }
        });
        c.addPackageOverride(packageName, overrides, allowedState, versionCode);
        android.util.Slog.d(TAG, (overrides.isEnabled() ? "Enabled" : "Disabled") + " change " + changeId + (c.getName() != null ? " [" + c.getName() + "]" : "") + " for " + packageName);
        invalidateCache();
        return alreadyKnown.get();
    }

    static /* synthetic */ com.android.server.compat.CompatChange lambda$addOverrideUnsafe$0(java.util.concurrent.atomic.AtomicBoolean alreadyKnown, long changeId, java.lang.Long key) {
        alreadyKnown.set(false);
        return new com.android.server.compat.CompatChange(changeId);
    }

    boolean isKnownChangeId(long changeId) {
        return this.mChanges.containsKey(java.lang.Long.valueOf(changeId));
    }

    int maxTargetSdkForChangeIdOptIn(long changeId) {
        com.android.server.compat.CompatChange c = this.mChanges.get(java.lang.Long.valueOf(changeId));
        return maxTargetSdkForCompatChange(c);
    }

    int maxTargetSdkForCompatChange(com.android.server.compat.CompatChange c) {
        if (c == null || c.getEnableSinceTargetSdk() == -1) {
            return -1;
        }
        return c.getEnableSinceTargetSdk() - 1;
    }

    boolean isLoggingOnly(long changeId) {
        com.android.server.compat.CompatChange c = this.mChanges.get(java.lang.Long.valueOf(changeId));
        return c != null && c.getLoggingOnly();
    }

    boolean isDisabled(long changeId) {
        com.android.server.compat.CompatChange c = this.mChanges.get(java.lang.Long.valueOf(changeId));
        return c != null && c.getDisabled();
    }

    boolean isOverridable(long changeId) {
        com.android.server.compat.CompatChange c = this.mChanges.get(java.lang.Long.valueOf(changeId));
        return c != null && c.getOverridable();
    }

    synchronized boolean removeOverride(long changeId, java.lang.String packageName) {
        boolean overrideExists;
        overrideExists = removeOverrideUnsafe(changeId, packageName);
        if (overrideExists) {
            saveOverrides();
            invalidateCache();
        }
        return overrideExists;
    }

    private boolean removeOverrideUnsafe(long changeId, java.lang.String packageName) {
        java.lang.Long versionCode = getVersionCodeOrNull(packageName);
        com.android.server.compat.CompatChange c = this.mChanges.get(java.lang.Long.valueOf(changeId));
        if (c != null) {
            return removeOverrideUnsafe(c, packageName, versionCode);
        }
        return false;
    }

    private boolean removeOverrideUnsafe(com.android.server.compat.CompatChange change, java.lang.String packageName, java.lang.Long versionCode) {
        long changeId = change.getId();
        com.android.internal.compat.OverrideAllowedState allowedState = this.mOverrideValidator.getOverrideAllowedState(changeId, packageName);
        boolean overrideExists = change.removePackageOverride(packageName, allowedState, versionCode);
        if (overrideExists) {
            android.util.Slog.d(TAG, "Reset change " + changeId + (change.getName() != null ? " [" + change.getName() + "]" : "") + " for " + packageName + " to default value.");
        }
        return overrideExists;
    }

    synchronized void removeAllPackageOverrides(com.android.internal.compat.CompatibilityOverridesToRemoveByPackageConfig overridesToRemoveByPackage) {
        boolean shouldInvalidateCache = false;
        for (java.lang.String packageName : overridesToRemoveByPackage.packageNameToOverridesToRemove.keySet()) {
            shouldInvalidateCache |= removePackageOverridesWithoutSaving((com.android.internal.compat.CompatibilityOverridesToRemoveConfig) overridesToRemoveByPackage.packageNameToOverridesToRemove.get(packageName), packageName);
        }
        if (shouldInvalidateCache) {
            saveOverrides();
            invalidateCache();
        }
    }

    synchronized void removePackageOverrides(java.lang.String packageName) {
        java.lang.Long versionCode = getVersionCodeOrNull(packageName);
        boolean shouldInvalidateCache = false;
        for (com.android.server.compat.CompatChange change : this.mChanges.values()) {
            shouldInvalidateCache |= removeOverrideUnsafe(change, packageName, versionCode);
        }
        if (shouldInvalidateCache) {
            saveOverrides();
            invalidateCache();
        }
    }

    synchronized void removePackageOverrides(com.android.internal.compat.CompatibilityOverridesToRemoveConfig overridesToRemove, java.lang.String packageName) {
        boolean shouldInvalidateCache = removePackageOverridesWithoutSaving(overridesToRemove, packageName);
        if (shouldInvalidateCache) {
            saveOverrides();
            invalidateCache();
        }
    }

    private boolean removePackageOverridesWithoutSaving(com.android.internal.compat.CompatibilityOverridesToRemoveConfig overridesToRemove, java.lang.String packageName) {
        boolean shouldInvalidateCache = false;
        for (java.lang.Long changeId : overridesToRemove.changeIds) {
            if (!isKnownChangeId(changeId.longValue())) {
                android.util.Slog.w(TAG, "Trying to remove overrides for unknown Change ID " + changeId + ". Skipping Change ID.");
            } else {
                shouldInvalidateCache |= removeOverrideUnsafe(changeId.longValue(), packageName);
            }
        }
        return shouldInvalidateCache;
    }

    private long[] getAllowedChangesSinceTargetSdkForPackage(java.lang.String packageName, int targetSdkVersion) {
        android.util.LongArray allowed = new android.util.LongArray();
        for (com.android.server.compat.CompatChange change : this.mChanges.values()) {
            if (change.getEnableSinceTargetSdk() == targetSdkVersion) {
                com.android.internal.compat.OverrideAllowedState allowedState = this.mOverrideValidator.getOverrideAllowedState(change.getId(), packageName);
                if (allowedState.state == 0) {
                    allowed.add(change.getId());
                }
            }
        }
        return allowed.toArray();
    }

    int enableTargetSdkChangesForPackage(java.lang.String packageName, int targetSdkVersion) {
        long[] changes = getAllowedChangesSinceTargetSdkForPackage(packageName, targetSdkVersion);
        boolean shouldInvalidateCache = false;
        for (long changeId : changes) {
            shouldInvalidateCache |= addOverrideUnsafe(changeId, packageName, new android.app.compat.PackageOverride.Builder().setEnabled(true).build());
        }
        if (shouldInvalidateCache) {
            saveOverrides();
            invalidateCache();
        }
        return changes.length;
    }

    int disableTargetSdkChangesForPackage(java.lang.String packageName, int targetSdkVersion) {
        long[] changes = getAllowedChangesSinceTargetSdkForPackage(packageName, targetSdkVersion);
        boolean shouldInvalidateCache = false;
        for (long changeId : changes) {
            shouldInvalidateCache |= addOverrideUnsafe(changeId, packageName, new android.app.compat.PackageOverride.Builder().setEnabled(false).build());
        }
        if (shouldInvalidateCache) {
            saveOverrides();
            invalidateCache();
        }
        return changes.length;
    }

    boolean registerListener(final long changeId, com.android.server.compat.CompatChange.ChangeListener listener) {
        final java.util.concurrent.atomic.AtomicBoolean alreadyKnown = new java.util.concurrent.atomic.AtomicBoolean(true);
        com.android.server.compat.CompatChange c = this.mChanges.computeIfAbsent(java.lang.Long.valueOf(changeId), new java.util.function.Function() { // from class: com.android.server.compat.CompatConfig$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.lambda$registerListener$1(alreadyKnown, changeId, (java.lang.Long) obj);
            }
        });
        c.registerListener(listener);
        return alreadyKnown.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.compat.CompatChange lambda$registerListener$1(java.util.concurrent.atomic.AtomicBoolean alreadyKnown, long changeId, java.lang.Long key) {
        alreadyKnown.set(false);
        invalidateCache();
        return new com.android.server.compat.CompatChange(changeId);
    }

    boolean defaultChangeIdValue(long changeId) {
        com.android.server.compat.CompatChange c = this.mChanges.get(java.lang.Long.valueOf(changeId));
        if (c == null) {
            return true;
        }
        return c.defaultValue();
    }

    void forceNonDebuggableFinalForTest(boolean value) {
        this.mOverrideValidator.forceNonDebuggableFinalForTest(value);
    }

    void clearChanges() {
        this.mChanges.clear();
    }

    void dumpConfig(java.io.PrintWriter pw) {
        if (this.mChanges.size() == 0) {
            pw.println("No compat overrides.");
            return;
        }
        for (com.android.server.compat.CompatChange c : this.mChanges.values()) {
            pw.println(c.toString());
        }
    }

    com.android.internal.compat.CompatibilityChangeConfig getAppConfig(android.content.pm.ApplicationInfo applicationInfo) {
        java.util.Set<java.lang.Long> enabled = new java.util.HashSet<>();
        java.util.Set<java.lang.Long> disabled = new java.util.HashSet<>();
        for (com.android.server.compat.CompatChange c : this.mChanges.values()) {
            if (c.isEnabled(applicationInfo, this.mAndroidBuildClassifier)) {
                enabled.add(java.lang.Long.valueOf(c.getId()));
            } else {
                disabled.add(java.lang.Long.valueOf(c.getId()));
            }
        }
        return new com.android.internal.compat.CompatibilityChangeConfig(new android.compat.Compatibility.ChangeConfig(enabled, disabled));
    }

    com.android.internal.compat.CompatibilityChangeInfo[] dumpChanges() {
        com.android.internal.compat.CompatibilityChangeInfo[] changeInfos = new com.android.internal.compat.CompatibilityChangeInfo[this.mChanges.size()];
        int i = 0;
        for (com.android.server.compat.CompatChange change : this.mChanges.values()) {
            changeInfos[i] = new com.android.internal.compat.CompatibilityChangeInfo(change);
            i++;
        }
        return changeInfos;
    }

    void initConfigFromLib(java.io.File libraryDir) {
        if (!libraryDir.exists() || !libraryDir.isDirectory()) {
            android.util.Slog.d(TAG, "No directory " + libraryDir + ", skipping");
            return;
        }
        for (java.io.File f : libraryDir.listFiles()) {
            android.util.Slog.d(TAG, "Found a config file: " + f.getPath());
            readConfig(f);
        }
    }

    private void readConfig(java.io.File configFile) {
        java.io.InputStream in;
        try {
            try {
                in = new java.io.BufferedInputStream(new java.io.FileInputStream(configFile));
            } catch (java.io.IOException | javax.xml.datatype.DatatypeConfigurationException | org.xmlpull.v1.XmlPullParserException e) {
                android.util.Slog.e(TAG, "Encountered an error while reading/parsing compat config file", e);
            }
            try {
                com.android.server.compat.config.Config config = com.android.server.compat.config.XmlParser.read(in);
                for (com.android.server.compat.config.Change change : config.getCompatChange()) {
                    android.util.Slog.d(TAG, "Adding: " + change.toString());
                    this.mChanges.put(java.lang.Long.valueOf(change.getId()), new com.android.server.compat.CompatChange(change));
                }
                in.close();
            } catch (java.lang.Throwable th) {
                try {
                    in.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } finally {
            invalidateCache();
        }
    }

    private void initOverrides() {
        initOverrides(new java.io.File(APP_COMPAT_DATA_DIR, OVERRIDES_FILE), new java.io.File(STATIC_OVERRIDES_PRODUCT_DIR, OVERRIDES_FILE));
    }

    void initOverrides(java.io.File dynamicOverridesFile, java.io.File staticOverridesFile) {
        for (com.android.server.compat.CompatChange c : this.mChanges.values()) {
            c.clearOverrides();
        }
        loadOverrides(staticOverridesFile);
        synchronized (this.mOverridesFileLock) {
            this.mOverridesFile = dynamicOverridesFile;
            this.mBackupOverridesFile = makeBackupFile(dynamicOverridesFile);
            if (this.mBackupOverridesFile.exists()) {
                this.mOverridesFile.delete();
                this.mBackupOverridesFile.renameTo(this.mOverridesFile);
            }
            loadOverrides(this.mOverridesFile);
        }
        if (staticOverridesFile.exists()) {
            saveOverrides();
        }
    }

    private java.io.File makeBackupFile(java.io.File overridesFile) {
        return new java.io.File(overridesFile.getPath() + ".bak");
    }

    private void loadOverrides(java.io.File overridesFile) {
        if (!overridesFile.exists()) {
            return;
        }
        try {
            java.io.InputStream in = new java.io.BufferedInputStream(new java.io.FileInputStream(overridesFile));
            try {
                com.android.server.compat.overrides.Overrides overrides = com.android.server.compat.overrides.XmlParser.read(in);
                if (overrides == null) {
                    android.util.Slog.w(TAG, "Parsing " + overridesFile.getPath() + " failed");
                    in.close();
                    return;
                }
                for (com.android.server.compat.overrides.ChangeOverrides changeOverrides : overrides.getChangeOverrides()) {
                    long changeId = changeOverrides.getChangeId();
                    com.android.server.compat.CompatChange compatChange = this.mChanges.get(java.lang.Long.valueOf(changeId));
                    if (compatChange == null) {
                        android.util.Slog.w(TAG, "Change ID " + changeId + " not found. Skipping overrides for it.");
                    } else {
                        compatChange.loadOverrides(changeOverrides);
                    }
                }
                in.close();
            } catch (java.lang.Throwable th) {
                try {
                    in.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (java.io.IOException | javax.xml.datatype.DatatypeConfigurationException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.w(TAG, "Error processing " + overridesFile + " " + e.toString());
        }
    }

    void saveOverrides() {
        synchronized (this.mOverridesFileLock) {
            if (this.mOverridesFile != null && this.mBackupOverridesFile != null) {
                com.android.server.compat.overrides.Overrides overrides = new com.android.server.compat.overrides.Overrides();
                java.util.List<com.android.server.compat.overrides.ChangeOverrides> changeOverridesList = overrides.getChangeOverrides();
                for (com.android.server.compat.CompatChange c : this.mChanges.values()) {
                    com.android.server.compat.overrides.ChangeOverrides changeOverrides = c.saveOverrides();
                    if (changeOverrides != null) {
                        changeOverridesList.add(changeOverrides);
                    }
                }
                if (this.mOverridesFile.exists()) {
                    if (this.mBackupOverridesFile.exists()) {
                        this.mOverridesFile.delete();
                    } else if (!this.mOverridesFile.renameTo(this.mBackupOverridesFile)) {
                        android.util.Slog.e(TAG, "Couldn't rename file " + this.mOverridesFile + " to " + this.mBackupOverridesFile);
                        return;
                    }
                }
                try {
                    this.mOverridesFile.createNewFile();
                    try {
                        java.io.PrintWriter out = new java.io.PrintWriter(this.mOverridesFile);
                        try {
                            com.android.server.compat.overrides.XmlWriter writer = new com.android.server.compat.overrides.XmlWriter(out);
                            com.android.server.compat.overrides.XmlWriter.write(writer, overrides);
                            out.close();
                        } catch (java.lang.Throwable th) {
                            try {
                                out.close();
                            } catch (java.lang.Throwable th2) {
                                th.addSuppressed(th2);
                            }
                            throw th;
                        }
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(TAG, e.toString());
                    }
                    this.mBackupOverridesFile.delete();
                } catch (java.io.IOException e2) {
                    android.util.Slog.e(TAG, "Could not create override config file: " + e2.toString());
                }
            }
        }
    }

    com.android.internal.compat.IOverrideValidator getOverrideValidator() {
        return this.mOverrideValidator;
    }

    private void invalidateCache() {
        android.app.compat.ChangeIdStateCache.invalidate();
    }

    void recheckOverrides(java.lang.String packageName) {
        java.lang.Long versionCode = getVersionCodeOrNull(packageName);
        boolean shouldInvalidateCache = false;
        for (com.android.server.compat.CompatChange c : this.mChanges.values()) {
            com.android.internal.compat.OverrideAllowedState allowedState = this.mOverrideValidator.getOverrideAllowedStateForRecheck(c.getId(), packageName);
            shouldInvalidateCache |= c.recheckOverride(packageName, allowedState, versionCode);
        }
        if (shouldInvalidateCache) {
            invalidateCache();
        }
    }

    private java.lang.Long getVersionCodeOrNull(java.lang.String packageName) {
        try {
            android.content.pm.ApplicationInfo applicationInfo = this.mContext.getPackageManager().getApplicationInfo(packageName, 4194304);
            return java.lang.Long.valueOf(applicationInfo.longVersionCode);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    void registerContentObserver() {
        this.mOverrideValidator.registerContentObserver();
    }
}
