package com.android.server.compat;

/* JADX INFO: loaded from: classes.dex */
public class PlatformCompat extends com.android.internal.compat.IPlatformCompat.Stub {
    private static final java.lang.String TAG = "Compatibility";
    private final com.android.internal.compat.AndroidBuildClassifier mBuildClassifier;
    private final com.android.internal.compat.ChangeReporter mChangeReporter;
    private final com.android.server.compat.CompatConfig mCompatConfig;
    private final android.content.Context mContext;

    public PlatformCompat(android.content.Context context) {
        this.mContext = context;
        this.mChangeReporter = new com.android.internal.compat.ChangeReporter(2);
        this.mBuildClassifier = new com.android.internal.compat.AndroidBuildClassifier();
        this.mCompatConfig = com.android.server.compat.CompatConfig.create(this.mBuildClassifier, this.mContext);
    }

    PlatformCompat(android.content.Context context, com.android.server.compat.CompatConfig compatConfig, com.android.internal.compat.AndroidBuildClassifier buildClassifier) {
        this.mContext = context;
        this.mChangeReporter = new com.android.internal.compat.ChangeReporter(2);
        this.mCompatConfig = compatConfig;
        this.mBuildClassifier = buildClassifier;
        registerPackageReceiver(context);
    }

    public void reportChange(long changeId, android.content.pm.ApplicationInfo appInfo) {
        super.reportChange_enforcePermission();
        reportChangeInternal(changeId, appInfo.uid, 3);
    }

    public void reportChangeByPackageName(long changeId, java.lang.String packageName, int userId) {
        super.reportChangeByPackageName_enforcePermission();
        android.content.pm.ApplicationInfo appInfo = getApplicationInfo(packageName, userId);
        if (appInfo != null) {
            reportChangeInternal(changeId, appInfo.uid, 3);
        }
    }

    public void reportChangeByUid(long changeId, int uid) {
        super.reportChangeByUid_enforcePermission();
        reportChangeInternal(changeId, uid, 3);
    }

    private void reportChangeInternal(long changeId, int uid, int state) {
        this.mChangeReporter.reportChange(uid, changeId, state, true);
    }

    public boolean isChangeEnabled(long changeId, android.content.pm.ApplicationInfo appInfo) {
        super.isChangeEnabled_enforcePermission();
        return isChangeEnabledInternal(changeId, appInfo);
    }

    public boolean isChangeEnabledByPackageName(long changeId, java.lang.String packageName, int userId) {
        super.isChangeEnabledByPackageName_enforcePermission();
        android.content.pm.ApplicationInfo appInfo = getApplicationInfo(packageName, userId);
        if (appInfo == null) {
            return this.mCompatConfig.willChangeBeEnabled(changeId, packageName);
        }
        return isChangeEnabledInternal(changeId, appInfo);
    }

    public boolean isChangeEnabledByUid(long changeId, int uid) {
        super.isChangeEnabledByUid_enforcePermission();
        return isChangeEnabledByUidInternal(changeId, uid);
    }

    public boolean isChangeEnabledInternalNoLogging(long changeId, android.content.pm.ApplicationInfo appInfo) {
        return this.mCompatConfig.isChangeEnabled(changeId, appInfo);
    }

    public boolean isChangeEnabledInternal(long changeId, android.content.pm.ApplicationInfo appInfo) {
        com.android.server.compat.CompatChange c = this.mCompatConfig.getCompatChange(changeId);
        boolean enabled = this.mCompatConfig.isChangeEnabled(c, appInfo);
        int state = enabled ? 1 : 2;
        if (appInfo != null) {
            boolean isTargetingLatestSdk = this.mCompatConfig.isChangeTargetingLatestSdk(c, appInfo.targetSdkVersion);
            this.mChangeReporter.reportChange(appInfo.uid, changeId, state, isTargetingLatestSdk);
        }
        return enabled;
    }

    public boolean isChangeEnabledInternal(long changeId, java.lang.String packageName, int targetSdkVersion) {
        if (this.mCompatConfig.willChangeBeEnabled(changeId, packageName)) {
            android.content.pm.ApplicationInfo appInfo = new android.content.pm.ApplicationInfo();
            appInfo.packageName = packageName;
            appInfo.targetSdkVersion = targetSdkVersion;
            return isChangeEnabledInternalNoLogging(changeId, appInfo);
        }
        return false;
    }

    public boolean isChangeEnabledByUidInternal(long changeId, int uid) {
        java.lang.String[] packages = this.mContext.getPackageManager().getPackagesForUid(uid);
        if (packages == null || packages.length == 0) {
            return this.mCompatConfig.defaultChangeIdValue(changeId);
        }
        boolean enabled = true;
        int userId = android.os.UserHandle.getUserId(uid);
        for (java.lang.String packageName : packages) {
            android.content.pm.ApplicationInfo appInfo = getApplicationInfo(packageName, userId);
            enabled &= isChangeEnabledInternal(changeId, appInfo);
        }
        return enabled;
    }

    public boolean isChangeEnabledByUidInternalNoLogging(long changeId, int uid) {
        java.lang.String[] packages = this.mContext.getPackageManager().getPackagesForUid(uid);
        if (packages == null || packages.length == 0) {
            return this.mCompatConfig.defaultChangeIdValue(changeId);
        }
        boolean enabled = true;
        int userId = android.os.UserHandle.getUserId(uid);
        for (java.lang.String packageName : packages) {
            android.content.pm.ApplicationInfo appInfo = getApplicationInfo(packageName, userId);
            enabled &= isChangeEnabledInternalNoLogging(changeId, appInfo);
        }
        return enabled;
    }

    public void setOverrides(com.android.internal.compat.CompatibilityChangeConfig overrides, java.lang.String packageName) {
        super.setOverrides_enforcePermission();
        java.util.Map<java.lang.Long, android.app.compat.PackageOverride> overridesMap = new java.util.HashMap<>();
        java.util.Iterator it = overrides.enabledChanges().iterator();
        while (it.hasNext()) {
            long change = ((java.lang.Long) it.next()).longValue();
            overridesMap.put(java.lang.Long.valueOf(change), new android.app.compat.PackageOverride.Builder().setEnabled(true).build());
        }
        java.util.Iterator it2 = overrides.disabledChanges().iterator();
        while (it2.hasNext()) {
            long change2 = ((java.lang.Long) it2.next()).longValue();
            overridesMap.put(java.lang.Long.valueOf(change2), new android.app.compat.PackageOverride.Builder().setEnabled(false).build());
        }
        this.mCompatConfig.addPackageOverrides(new com.android.internal.compat.CompatibilityOverrideConfig(overridesMap), packageName, false);
        killPackage(packageName);
    }

    public void setOverridesForTest(com.android.internal.compat.CompatibilityChangeConfig overrides, java.lang.String packageName) {
        super.setOverridesForTest_enforcePermission();
        java.util.Map<java.lang.Long, android.app.compat.PackageOverride> overridesMap = new java.util.HashMap<>();
        java.util.Iterator it = overrides.enabledChanges().iterator();
        while (it.hasNext()) {
            long change = ((java.lang.Long) it.next()).longValue();
            overridesMap.put(java.lang.Long.valueOf(change), new android.app.compat.PackageOverride.Builder().setEnabled(true).build());
        }
        java.util.Iterator it2 = overrides.disabledChanges().iterator();
        while (it2.hasNext()) {
            long change2 = ((java.lang.Long) it2.next()).longValue();
            overridesMap.put(java.lang.Long.valueOf(change2), new android.app.compat.PackageOverride.Builder().setEnabled(false).build());
        }
        this.mCompatConfig.addPackageOverrides(new com.android.internal.compat.CompatibilityOverrideConfig(overridesMap), packageName, false);
    }

    public void putAllOverridesOnReleaseBuilds(com.android.internal.compat.CompatibilityOverridesByPackageConfig overridesByPackage) {
        super.putAllOverridesOnReleaseBuilds_enforcePermission();
        for (com.android.internal.compat.CompatibilityOverrideConfig overrides : overridesByPackage.packageNameToOverrides.values()) {
            checkAllCompatOverridesAreOverridable(overrides.overrides.keySet());
        }
        this.mCompatConfig.addAllPackageOverrides(overridesByPackage, true);
    }

    public void putOverridesOnReleaseBuilds(com.android.internal.compat.CompatibilityOverrideConfig overrides, java.lang.String packageName) {
        super.putOverridesOnReleaseBuilds_enforcePermission();
        checkAllCompatOverridesAreOverridable(overrides.overrides.keySet());
        this.mCompatConfig.addPackageOverrides(overrides, packageName, true);
    }

    public int enableTargetSdkChanges(java.lang.String packageName, int targetSdkVersion) {
        super.enableTargetSdkChanges_enforcePermission();
        int numChanges = this.mCompatConfig.enableTargetSdkChangesForPackage(packageName, targetSdkVersion);
        killPackage(packageName);
        return numChanges;
    }

    public int disableTargetSdkChanges(java.lang.String packageName, int targetSdkVersion) {
        super.disableTargetSdkChanges_enforcePermission();
        int numChanges = this.mCompatConfig.disableTargetSdkChangesForPackage(packageName, targetSdkVersion);
        killPackage(packageName);
        return numChanges;
    }

    public void clearOverrides(java.lang.String packageName) {
        super.clearOverrides_enforcePermission();
        this.mCompatConfig.removePackageOverrides(packageName);
        killPackage(packageName);
    }

    public void clearOverridesForTest(java.lang.String packageName) {
        super.clearOverridesForTest_enforcePermission();
        this.mCompatConfig.removePackageOverrides(packageName);
    }

    public boolean clearOverride(long changeId, java.lang.String packageName) {
        super.clearOverride_enforcePermission();
        boolean existed = this.mCompatConfig.removeOverride(changeId, packageName);
        killPackage(packageName);
        return existed;
    }

    public boolean clearOverrideForTest(long changeId, java.lang.String packageName) {
        super.clearOverrideForTest_enforcePermission();
        return this.mCompatConfig.removeOverride(changeId, packageName);
    }

    public void removeAllOverridesOnReleaseBuilds(com.android.internal.compat.CompatibilityOverridesToRemoveByPackageConfig overridesToRemoveByPackage) {
        super.removeAllOverridesOnReleaseBuilds_enforcePermission();
        for (com.android.internal.compat.CompatibilityOverridesToRemoveConfig overridesToRemove : overridesToRemoveByPackage.packageNameToOverridesToRemove.values()) {
            checkAllCompatOverridesAreOverridable(overridesToRemove.changeIds);
        }
        this.mCompatConfig.removeAllPackageOverrides(overridesToRemoveByPackage);
    }

    public void removeOverridesOnReleaseBuilds(com.android.internal.compat.CompatibilityOverridesToRemoveConfig overridesToRemove, java.lang.String packageName) {
        super.removeOverridesOnReleaseBuilds_enforcePermission();
        checkAllCompatOverridesAreOverridable(overridesToRemove.changeIds);
        this.mCompatConfig.removePackageOverrides(overridesToRemove, packageName);
    }

    public com.android.internal.compat.CompatibilityChangeConfig getAppConfig(android.content.pm.ApplicationInfo appInfo) {
        super.getAppConfig_enforcePermission();
        return this.mCompatConfig.getAppConfig(appInfo);
    }

    public com.android.internal.compat.CompatibilityChangeInfo[] listAllChanges() {
        super.listAllChanges_enforcePermission();
        return this.mCompatConfig.dumpChanges();
    }

    public com.android.internal.compat.CompatibilityChangeInfo[] listUIChanges() {
        return (com.android.internal.compat.CompatibilityChangeInfo[]) java.util.Arrays.stream(listAllChanges()).filter(new java.util.function.Predicate() { // from class: com.android.server.compat.PlatformCompat$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.isShownInUI((com.android.internal.compat.CompatibilityChangeInfo) obj);
            }
        }).toArray(new java.util.function.IntFunction() { // from class: com.android.server.compat.PlatformCompat$$ExternalSyntheticLambda1
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.compat.PlatformCompat.lambda$listUIChanges$0(i);
            }
        });
    }

    static /* synthetic */ com.android.internal.compat.CompatibilityChangeInfo[] lambda$listUIChanges$0(int x$0) {
        return new com.android.internal.compat.CompatibilityChangeInfo[x$0];
    }

    public boolean isKnownChangeId(long changeId) {
        return this.mCompatConfig.isKnownChangeId(changeId);
    }

    public long[] getDisabledChanges(android.content.pm.ApplicationInfo appInfo) {
        return this.mCompatConfig.getDisabledChanges(appInfo);
    }

    public long[] getLoggableChanges(android.content.pm.ApplicationInfo appInfo) {
        return this.mCompatConfig.getLoggableChanges(appInfo);
    }

    public long lookupChangeId(java.lang.String name) {
        return this.mCompatConfig.lookupChangeId(name);
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (!com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(this.mContext, "platform_compat", pw)) {
            return;
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_COMPAT_CHANGE_CONFIG", "Cannot read compat change");
        this.mContext.enforceCallingOrSelfPermission("android.permission.LOG_COMPAT_CHANGE", "Cannot read log compat change usage");
        this.mCompatConfig.dumpConfig(pw);
    }

    public com.android.internal.compat.IOverrideValidator getOverrideValidator() {
        return this.mCompatConfig.getOverrideValidator();
    }

    public void resetReporting(android.content.pm.ApplicationInfo appInfo) {
        this.mChangeReporter.resetReportedChanges(appInfo.uid);
    }

    private android.content.pm.ApplicationInfo getApplicationInfo(java.lang.String packageName, int userId) {
        return ((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class)).getApplicationInfo(packageName, 0L, android.os.Process.myUid(), userId);
    }

    private void killPackage(java.lang.String packageName) {
        int uid = ((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class)).getPackageUid(packageName, 0L, android.os.UserHandle.myUserId());
        if (uid < 0) {
            android.util.Slog.w(TAG, "Didn't find package " + packageName + " on device.");
        } else {
            android.util.Slog.d(TAG, "Killing package " + packageName + " (UID " + uid + ").");
            killUid(android.os.UserHandle.getAppId(uid));
        }
    }

    private void killUid(int appId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.app.IActivityManager am = android.app.ActivityManager.getService();
            if (am != null) {
                am.killUid(appId, -1, "PlatformCompat overrides");
            }
        } catch (android.os.RemoteException e) {
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identity);
            throw th;
        }
        android.os.Binder.restoreCallingIdentity(identity);
    }

    private void checkAllCompatOverridesAreOverridable(java.util.Collection<java.lang.Long> changeIds) {
        for (java.lang.Long changeId : changeIds) {
            if (isKnownChangeId(changeId.longValue()) && !this.mCompatConfig.isOverridable(changeId.longValue())) {
                throw new java.lang.SecurityException("Only change ids marked as Overridable can be overridden.");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isShownInUI(com.android.internal.compat.CompatibilityChangeInfo change) {
        if (change.getLoggingOnly() || change.getId() == 149391281) {
            return false;
        }
        if (change.getEnableSinceTargetSdk() > 0) {
            return change.getEnableSinceTargetSdk() >= 29 && change.getEnableSinceTargetSdk() <= this.mBuildClassifier.platformTargetSdk();
        }
        return true;
    }

    public boolean registerListener(long changeId, com.android.server.compat.CompatChange.ChangeListener listener) {
        return this.mCompatConfig.registerListener(changeId, listener);
    }

    public void registerPackageReceiver(android.content.Context context) {
        android.content.BroadcastReceiver receiver = new android.content.BroadcastReceiver() { // from class: com.android.server.compat.PlatformCompat.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                android.net.Uri packageData;
                java.lang.String packageName;
                if (intent == null || (packageData = intent.getData()) == null || (packageName = packageData.getSchemeSpecificPart()) == null) {
                    return;
                }
                com.android.server.compat.PlatformCompat.this.mCompatConfig.recheckOverrides(packageName);
            }
        };
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REPLACED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addDataScheme("package");
        context.registerReceiverForAllUsers(receiver, filter, null, null);
    }

    public void registerContentObserver() {
        this.mCompatConfig.registerContentObserver();
    }
}
