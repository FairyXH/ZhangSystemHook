package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class AppsFilterImpl extends com.android.server.pm.AppsFilterLocked implements com.android.server.utils.Watchable, com.android.server.utils.Snappable {
    private final android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.Integer>> mPermissionToUids;
    private final com.android.server.utils.SnapshotCache<com.android.server.pm.AppsFilterSnapshot> mSnapshot;
    private final android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.Integer>> mUsesPermissionToUids;
    private final com.android.server.utils.WatchableImpl mWatchable = new com.android.server.utils.WatchableImpl();

    @Override // com.android.server.utils.Watchable
    public void registerObserver(com.android.server.utils.Watcher observer) {
        this.mWatchable.registerObserver(observer);
    }

    @Override // com.android.server.utils.Watchable
    public void unregisterObserver(com.android.server.utils.Watcher observer) {
        this.mWatchable.unregisterObserver(observer);
    }

    @Override // com.android.server.utils.Watchable
    public boolean isRegisteredObserver(com.android.server.utils.Watcher observer) {
        return this.mWatchable.isRegisteredObserver(observer);
    }

    @Override // com.android.server.utils.Watchable
    public void dispatchChange(com.android.server.utils.Watchable what) {
        this.mWatchable.dispatchChange(what);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onChanged() {
        dispatchChange(this);
    }

    private void invalidateCache(java.lang.String reason) {
        if (this.mCacheValid.compareAndSet(true, false)) {
            android.util.Slog.i("AppsFilter", "Invalidating cache: " + reason);
        }
    }

    AppsFilterImpl(com.android.server.pm.FeatureConfig featureConfig, java.lang.String[] forceQueryableList, boolean systemAppsQueryable, com.android.server.om.OverlayReferenceMapper.Provider overlayProvider, android.os.Handler handler) {
        this.mFeatureConfig = featureConfig;
        this.mForceQueryableByDevicePackageNames = forceQueryableList;
        this.mSystemAppsQueryable = systemAppsQueryable;
        this.mOverlayReferenceMapper = new com.android.server.om.OverlayReferenceMapper(true, overlayProvider);
        this.mHandler = handler;
        this.mShouldFilterCache = new com.android.server.utils.WatchedSparseBooleanMatrix();
        this.mShouldFilterCacheSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mShouldFilterCache, this.mShouldFilterCache, "AppsFilter.mShouldFilterCache");
        this.mImplicitlyQueryable = new com.android.server.utils.WatchedSparseSetArray<>();
        this.mImplicitQueryableSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mImplicitlyQueryable, this.mImplicitlyQueryable, "AppsFilter.mImplicitlyQueryable");
        this.mRetainedImplicitlyQueryable = new com.android.server.utils.WatchedSparseSetArray<>();
        this.mRetainedImplicitlyQueryableSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mRetainedImplicitlyQueryable, this.mRetainedImplicitlyQueryable, "AppsFilter.mRetainedImplicitlyQueryable");
        this.mQueriesViaPackage = new com.android.server.utils.WatchedSparseSetArray<>();
        this.mQueriesViaPackageSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mQueriesViaPackage, this.mQueriesViaPackage, "AppsFilter.mQueriesViaPackage");
        this.mQueriesViaComponent = new com.android.server.utils.WatchedSparseSetArray<>();
        this.mQueriesViaComponentSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mQueriesViaComponent, this.mQueriesViaComponent, "AppsFilter.mQueriesViaComponent");
        this.mQueryableViaUsesLibrary = new com.android.server.utils.WatchedSparseSetArray<>();
        this.mQueryableViaUsesLibrarySnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mQueryableViaUsesLibrary, this.mQueryableViaUsesLibrary, "AppsFilter.mQueryableViaUsesLibrary");
        this.mQueryableViaUsesPermission = new com.android.server.utils.WatchedSparseSetArray<>();
        this.mQueryableViaUsesPermissionSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mQueryableViaUsesPermission, this.mQueryableViaUsesPermission, "AppsFilter.mQueryableViaUsesPermission");
        this.mForceQueryable = new com.android.server.utils.WatchedArraySet<>();
        this.mForceQueryableSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mForceQueryable, this.mForceQueryable, "AppsFilter.mForceQueryable");
        this.mProtectedBroadcasts = new com.android.server.utils.WatchedArraySet<>();
        this.mProtectedBroadcastsSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mProtectedBroadcasts, this.mProtectedBroadcasts, "AppsFilter.mProtectedBroadcasts");
        this.mPermissionToUids = new android.util.ArrayMap<>();
        this.mUsesPermissionToUids = new android.util.ArrayMap<>();
        this.mSnapshot = new com.android.server.utils.SnapshotCache<com.android.server.pm.AppsFilterSnapshot>(this, this) { // from class: com.android.server.pm.AppsFilterImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.android.server.utils.SnapshotCache
            public com.android.server.pm.AppsFilterSnapshot createSnapshot() {
                return new com.android.server.pm.AppsFilterSnapshotImpl(com.android.server.pm.AppsFilterImpl.this);
            }
        };
        readCacheEnabledSysProp();
        android.os.SystemProperties.addChangeCallback(new java.lang.Runnable() { // from class: com.android.server.pm.AppsFilterImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.readCacheEnabledSysProp();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readCacheEnabledSysProp() {
        this.mCacheEnabled = android.os.SystemProperties.getBoolean("debug.pm.use_app_filter_cache", true);
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.pm.AppsFilterSnapshot snapshot() {
        return this.mSnapshot.snapshot();
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class FeatureConfigImpl implements com.android.server.pm.FeatureConfig, com.android.server.compat.CompatChange.ChangeListener {
        private static final java.lang.String FILTERING_ENABLED_NAME = "package_query_filtering_enabled";
        private com.android.server.pm.AppsFilterImpl mAppsFilter;
        private final android.util.ArraySet<java.lang.String> mDisabledPackages;
        private volatile boolean mFeatureEnabled;
        private final com.android.server.pm.PackageManagerServiceInjector mInjector;
        private android.util.SparseBooleanArray mLoggingEnabled;
        private final android.content.pm.PackageManagerInternal mPmInternal;

        private FeatureConfigImpl(android.content.pm.PackageManagerInternal pmInternal, com.android.server.pm.PackageManagerServiceInjector injector) {
            this.mFeatureEnabled = true;
            this.mDisabledPackages = new android.util.ArraySet<>();
            this.mLoggingEnabled = null;
            this.mPmInternal = pmInternal;
            this.mInjector = injector;
        }

        FeatureConfigImpl(com.android.server.pm.AppsFilterImpl.FeatureConfigImpl orig) {
            this.mFeatureEnabled = true;
            this.mDisabledPackages = new android.util.ArraySet<>();
            this.mLoggingEnabled = null;
            this.mInjector = null;
            this.mPmInternal = null;
            this.mFeatureEnabled = orig.mFeatureEnabled;
            synchronized (orig.mDisabledPackages) {
                this.mDisabledPackages.addAll((android.util.ArraySet<? extends java.lang.String>) orig.mDisabledPackages);
            }
            this.mLoggingEnabled = orig.mLoggingEnabled;
        }

        public void setAppsFilter(com.android.server.pm.AppsFilterImpl filter) {
            this.mAppsFilter = filter;
        }

        @Override // com.android.server.pm.FeatureConfig
        public void onSystemReady() {
            this.mFeatureEnabled = android.provider.DeviceConfig.getBoolean("package_manager_service", FILTERING_ENABLED_NAME, true);
            android.provider.DeviceConfig.addOnPropertiesChangedListener("package_manager_service", com.android.server.FgThread.getExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.pm.AppsFilterImpl$FeatureConfigImpl$$ExternalSyntheticLambda0
                public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                    this.f$0.lambda$onSystemReady$0(properties);
                }
            });
            this.mInjector.getCompatibility().registerListener(135549675L, this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSystemReady$0(android.provider.DeviceConfig.Properties properties) {
            if (properties.getKeyset().contains(FILTERING_ENABLED_NAME)) {
                synchronized (this) {
                    this.mFeatureEnabled = properties.getBoolean(FILTERING_ENABLED_NAME, true);
                }
            }
        }

        @Override // com.android.server.pm.FeatureConfig
        public boolean isGloballyEnabled() {
            return this.mFeatureEnabled;
        }

        @Override // com.android.server.pm.FeatureConfig
        public boolean packageIsEnabled(com.android.server.pm.pkg.AndroidPackage pkg) {
            boolean z;
            synchronized (this.mDisabledPackages) {
                z = !this.mDisabledPackages.contains(pkg.getPackageName());
            }
            return z;
        }

        @Override // com.android.server.pm.FeatureConfig
        public boolean isLoggingEnabled(int uid) {
            return this.mLoggingEnabled != null && this.mLoggingEnabled.indexOfKey(uid) >= 0;
        }

        @Override // com.android.server.pm.FeatureConfig
        public void enableLogging(int appId, boolean enable) {
            int index;
            if (enable) {
                if (this.mLoggingEnabled == null) {
                    this.mLoggingEnabled = new android.util.SparseBooleanArray();
                }
                this.mLoggingEnabled.put(appId, true);
            } else if (this.mLoggingEnabled != null && (index = this.mLoggingEnabled.indexOfKey(appId)) >= 0) {
                this.mLoggingEnabled.removeAt(index);
                if (this.mLoggingEnabled.size() == 0) {
                    this.mLoggingEnabled = null;
                }
            }
        }

        @Override // com.android.server.compat.CompatChange.ChangeListener
        public void onCompatChange(java.lang.String packageName) {
            com.android.server.pm.Computer snapshot = (com.android.server.pm.Computer) this.mPmInternal.snapshot();
            com.android.server.pm.pkg.AndroidPackage pkg = snapshot.getPackage(packageName);
            if (pkg == null) {
                return;
            }
            long currentTimeUs = android.os.SystemClock.currentTimeMicro();
            updateEnabledState(pkg);
            this.mAppsFilter.updateShouldFilterCacheForPackage(snapshot, packageName);
            this.mAppsFilter.logCacheUpdated(4, android.os.SystemClock.currentTimeMicro() - currentTimeUs, snapshot.getUserInfos().length, snapshot.getPackageStates().size(), pkg.getUid());
        }

        private void updateEnabledState(com.android.server.pm.pkg.AndroidPackage pkg) {
            boolean enabled = this.mInjector.getCompatibility().isChangeEnabledInternalNoLogging(135549675L, com.android.server.pm.parsing.pkg.AndroidPackageUtils.generateAppInfoWithoutState(pkg));
            synchronized (this.mDisabledPackages) {
                if (enabled) {
                    this.mDisabledPackages.remove(pkg.getPackageName());
                } else {
                    this.mDisabledPackages.add(pkg.getPackageName());
                }
            }
            if (this.mAppsFilter != null) {
                this.mAppsFilter.onChanged();
            }
        }

        @Override // com.android.server.pm.FeatureConfig
        public void updatePackageState(com.android.server.pm.pkg.PackageStateInternal setting, boolean removed) {
            boolean enableLogging = (setting.getPkg() == null || removed || (!setting.getPkg().isTestOnly() && !setting.getPkg().isDebuggable())) ? false : true;
            enableLogging(setting.getAppId(), enableLogging);
            if (removed) {
                synchronized (this.mDisabledPackages) {
                    this.mDisabledPackages.remove(setting.getPackageName());
                }
                if (this.mAppsFilter != null) {
                    this.mAppsFilter.onChanged();
                    return;
                }
                return;
            }
            if (setting.getPkg() != null) {
                updateEnabledState(setting.getPkg());
            }
        }

        @Override // com.android.server.pm.FeatureConfig
        public com.android.server.pm.FeatureConfig snapshot() {
            return new com.android.server.pm.AppsFilterImpl.FeatureConfigImpl(this);
        }
    }

    public static com.android.server.pm.AppsFilterImpl create(com.android.server.pm.PackageManagerServiceInjector injector, android.content.pm.PackageManagerInternal pmInt) {
        java.lang.String[] forcedQueryablePackageNames;
        boolean forceSystemAppsQueryable = injector.getContext().getResources().getBoolean(android.R.bool.config_enable_puk_unlock_screen);
        com.android.server.pm.AppsFilterImpl.FeatureConfigImpl featureConfig = new com.android.server.pm.AppsFilterImpl.FeatureConfigImpl(pmInt, injector);
        if (forceSystemAppsQueryable) {
            forcedQueryablePackageNames = new java.lang.String[0];
        } else {
            java.lang.String[] forcedQueryablePackageNames2 = injector.getContext().getResources().getStringArray(android.R.array.config_foldedDeviceStates);
            for (int i = 0; i < forcedQueryablePackageNames2.length; i++) {
                forcedQueryablePackageNames2[i] = forcedQueryablePackageNames2[i].intern();
            }
            forcedQueryablePackageNames = forcedQueryablePackageNames2;
        }
        com.android.server.pm.AppsFilterImpl appsFilter = new com.android.server.pm.AppsFilterImpl(featureConfig, forcedQueryablePackageNames, forceSystemAppsQueryable, null, injector.getHandler());
        featureConfig.setAppsFilter(appsFilter);
        return appsFilter;
    }

    public com.android.server.pm.FeatureConfig getFeatureConfig() {
        return this.mFeatureConfig;
    }

    public boolean grantImplicitAccess(int recipientUid, int visibleUid, boolean retainOnUpdate) {
        boolean changed;
        if (recipientUid == visibleUid) {
            return false;
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mImplicitlyQueryableLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                if (retainOnUpdate) {
                    changed = this.mRetainedImplicitlyQueryable.add(recipientUid, java.lang.Integer.valueOf(visibleUid));
                } else {
                    changed = this.mImplicitlyQueryable.add(recipientUid, java.lang.Integer.valueOf(visibleUid));
                }
                if (!this.mCacheReady && changed) {
                    this.mNeedToUpdateCacheForImplicitAccess = true;
                }
            } finally {
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        if (this.mCacheReady) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mCacheLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock2) {
                try {
                    this.mShouldFilterCache.put(recipientUid, visibleUid, false);
                } finally {
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }
        if (changed) {
            onChanged();
        }
        return changed;
    }

    public void onSystemReady(android.content.pm.PackageManagerInternal pmInternal) {
        this.mOverlayReferenceMapper.rebuildIfDeferred();
        this.mFeatureConfig.onSystemReady();
        updateEntireShouldFilterCacheAsync(pmInternal, 1);
    }

    public void addPackage(com.android.server.pm.Computer snapshot, com.android.server.pm.pkg.PackageStateInternal newPkgSetting, boolean isReplace, boolean retainImplicitGrantOnReplace) throws java.lang.Throwable {
        int logType;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock;
        long currentTimeUs = android.os.SystemClock.currentTimeMicro();
        if (isReplace) {
            logType = 3;
        } else {
            logType = 1;
        }
        if (isReplace) {
            try {
                removePackageInternal(snapshot, newPkgSetting, true, retainImplicitGrantOnReplace);
            } catch (java.lang.Throwable th) {
                th = th;
                onChanged();
                throw th;
            }
        }
        android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> settings = snapshot.getPackageStates();
        android.content.pm.UserInfo[] users = snapshot.getUserInfos();
        android.util.ArraySet<java.lang.String> additionalChangedPackages = addPackageInternal(newPkgSetting, settings);
        try {
            if (!this.mCacheReady) {
                invalidateCache("addPackage: " + newPkgSetting.getPackageName());
            } else {
                com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mCacheLock;
                com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                synchronized (packageManagerTracedLock2) {
                    try {
                        packageManagerTracedLock = packageManagerTracedLock2;
                        int logType2 = logType;
                        try {
                            updateShouldFilterCacheForPackage(snapshot, null, newPkgSetting, settings, users, -1, settings.size());
                            if (additionalChangedPackages != null) {
                                for (int index = 0; index < additionalChangedPackages.size(); index++) {
                                    try {
                                        java.lang.String changedPackage = additionalChangedPackages.valueAt(index);
                                        com.android.server.pm.pkg.PackageStateInternal changedPkgSetting = settings.get(changedPackage);
                                        if (changedPkgSetting != null) {
                                            updateShouldFilterCacheForPackage(snapshot, null, changedPkgSetting, settings, users, -1, settings.size());
                                        }
                                    } catch (java.lang.Throwable th2) {
                                        th = th2;
                                        while (true) {
                                            try {
                                                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                                                throw th;
                                            } catch (java.lang.Throwable th3) {
                                                th = th3;
                                            }
                                        }
                                    }
                                }
                            }
                            try {
                                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                                logCacheUpdated(logType2, android.os.SystemClock.currentTimeMicro() - currentTimeUs, users.length, settings.size(), newPkgSetting.getAppId());
                            } catch (java.lang.Throwable th4) {
                                th = th4;
                                onChanged();
                                throw th;
                            }
                        } catch (java.lang.Throwable th5) {
                            th = th5;
                        }
                    } catch (java.lang.Throwable th6) {
                        th = th6;
                        packageManagerTracedLock = packageManagerTracedLock2;
                    }
                }
            }
            onChanged();
        } catch (java.lang.Throwable th7) {
            th = th7;
        }
    }

    private android.util.ArraySet<java.lang.String> addPackageInternal(com.android.server.pm.pkg.PackageStateInternal newPkgSetting, android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> existingSettings) {
        boolean newIsForceQueryable;
        boolean existingIsForceQueryable;
        int oldSize;
        if (java.util.Objects.equals(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, newPkgSetting.getPackageName())) {
            this.mSystemSigningDetails = newPkgSetting.getSigningDetails();
            for (com.android.server.pm.pkg.PackageStateInternal setting : existingSettings.values()) {
                if (isSystemSigned(this.mSystemSigningDetails, setting)) {
                    com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mForceQueryableLock;
                    com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                    synchronized (packageManagerTracedLock) {
                        try {
                            this.mForceQueryable.add(java.lang.Integer.valueOf(setting.getAppId()));
                        } finally {
                        }
                    }
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                }
            }
        }
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = newPkgSetting.getPkg();
        if (pkg == null) {
            return null;
        }
        java.util.List<java.lang.String> newBroadcasts = pkg.getProtectedBroadcasts();
        if (newBroadcasts.size() != 0) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mProtectedBroadcastsLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock2) {
                try {
                    int oldSize2 = this.mProtectedBroadcasts.size();
                    this.mProtectedBroadcasts.addAll(newBroadcasts);
                    oldSize = this.mProtectedBroadcasts.size() != oldSize2 ? 1 : 0;
                } finally {
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            if (oldSize != 0) {
                this.mQueriesViaComponentRequireRecompute.set(true);
            }
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock3 = this.mForceQueryableLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock3) {
            try {
                newIsForceQueryable = this.mForceQueryable.contains(java.lang.Integer.valueOf(newPkgSetting.getAppId())) || newPkgSetting.isForceQueryableOverride() || (newPkgSetting.isSystem() && (this.mSystemAppsQueryable || pkg.isForceQueryable() || com.android.internal.util.ArrayUtils.contains(this.mForceQueryableByDevicePackageNames, pkg.getPackageName())));
                if (newIsForceQueryable || (this.mSystemSigningDetails != null && isSystemSigned(this.mSystemSigningDetails, newPkgSetting))) {
                    this.mForceQueryable.add(java.lang.Integer.valueOf(newPkgSetting.getAppId()));
                }
            } finally {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        if (!pkg.getUsesPermissions().isEmpty()) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock4 = this.mQueryableViaUsesPermissionLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock4) {
                try {
                    for (com.android.internal.pm.pkg.component.ParsedUsesPermission usesPermission : pkg.getUsesPermissions()) {
                        java.lang.String usesPermissionName = usesPermission.getName();
                        if (this.mPermissionToUids.containsKey(usesPermissionName)) {
                            android.util.ArraySet<java.lang.Integer> permissionDefiners = this.mPermissionToUids.get(usesPermissionName);
                            for (int j = 0; j < permissionDefiners.size(); j++) {
                                int targetAppId = permissionDefiners.valueAt(j).intValue();
                                if (targetAppId != newPkgSetting.getAppId()) {
                                    this.mQueryableViaUsesPermission.add(newPkgSetting.getAppId(), java.lang.Integer.valueOf(targetAppId));
                                }
                            }
                        }
                        if (!this.mUsesPermissionToUids.containsKey(usesPermissionName)) {
                            this.mUsesPermissionToUids.put(usesPermissionName, new android.util.ArraySet<>());
                        }
                        this.mUsesPermissionToUids.get(usesPermissionName).add(java.lang.Integer.valueOf(newPkgSetting.getAppId()));
                    }
                } finally {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }
        if (!pkg.getPermissions().isEmpty()) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock5 = this.mQueryableViaUsesPermissionLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock5) {
                try {
                    for (com.android.internal.pm.pkg.component.ParsedPermission permission : pkg.getPermissions()) {
                        java.lang.String permissionName = permission.getName();
                        if (this.mUsesPermissionToUids.containsKey(permissionName)) {
                            android.util.ArraySet<java.lang.Integer> permissionUsers = this.mUsesPermissionToUids.get(permissionName);
                            for (int j2 = 0; j2 < permissionUsers.size(); j2++) {
                                int queryingAppId = permissionUsers.valueAt(j2).intValue();
                                if (queryingAppId != newPkgSetting.getAppId()) {
                                    this.mQueryableViaUsesPermission.add(queryingAppId, java.lang.Integer.valueOf(newPkgSetting.getAppId()));
                                }
                            }
                        }
                        if (!this.mPermissionToUids.containsKey(permissionName)) {
                            this.mPermissionToUids.put(permissionName, new android.util.ArraySet<>());
                        }
                        this.mPermissionToUids.get(permissionName).add(java.lang.Integer.valueOf(newPkgSetting.getAppId()));
                    }
                } finally {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }
        for (int i = existingSettings.size() - 1; i >= 0; i--) {
            com.android.server.pm.pkg.PackageStateInternal existingSetting = existingSettings.valueAt(i);
            if (existingSetting.getAppId() != newPkgSetting.getAppId() && existingSetting.getPkg() != null) {
                com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg2 = existingSetting.getPkg();
                if (!newIsForceQueryable) {
                    if (!this.mQueriesViaComponentRequireRecompute.get() && com.android.server.pm.AppsFilterUtils.canQueryViaComponents(pkg2, pkg, this.mProtectedBroadcasts)) {
                        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock6 = this.mQueriesViaComponentLock;
                        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                        synchronized (packageManagerTracedLock6) {
                            try {
                                this.mQueriesViaComponent.add(existingSetting.getAppId(), java.lang.Integer.valueOf(newPkgSetting.getAppId()));
                            } finally {
                                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            }
                        }
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    }
                    if (com.android.server.pm.AppsFilterUtils.canQueryViaPackage(pkg2, pkg) || com.android.server.pm.AppsFilterUtils.canQueryAsInstaller(existingSetting, pkg) || com.android.server.pm.AppsFilterUtils.canQueryAsUpdateOwner(existingSetting, pkg)) {
                        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock7 = this.mQueriesViaPackageLock;
                        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                        synchronized (packageManagerTracedLock7) {
                            try {
                                this.mQueriesViaPackage.add(existingSetting.getAppId(), java.lang.Integer.valueOf(newPkgSetting.getAppId()));
                            } finally {
                                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            }
                        }
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    }
                    if (com.android.server.pm.AppsFilterUtils.canQueryViaUsesLibrary(pkg2, pkg)) {
                        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock8 = this.mQueryableViaUsesLibraryLock;
                        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                        synchronized (packageManagerTracedLock8) {
                            try {
                                this.mQueryableViaUsesLibrary.add(existingSetting.getAppId(), java.lang.Integer.valueOf(newPkgSetting.getAppId()));
                            } finally {
                                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            }
                        }
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    }
                }
                com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock9 = this.mForceQueryableLock;
                com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                synchronized (packageManagerTracedLock9) {
                    try {
                        existingIsForceQueryable = this.mForceQueryable.contains(java.lang.Integer.valueOf(existingSetting.getAppId()));
                    } finally {
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    }
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                if (!existingIsForceQueryable) {
                    if (!this.mQueriesViaComponentRequireRecompute.get() && com.android.server.pm.AppsFilterUtils.canQueryViaComponents(pkg, pkg2, this.mProtectedBroadcasts)) {
                        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock10 = this.mQueriesViaComponentLock;
                        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                        synchronized (packageManagerTracedLock10) {
                            try {
                                this.mQueriesViaComponent.add(newPkgSetting.getAppId(), java.lang.Integer.valueOf(existingSetting.getAppId()));
                            } finally {
                                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            }
                        }
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    }
                    if (com.android.server.pm.AppsFilterUtils.canQueryViaPackage(pkg, pkg2) || com.android.server.pm.AppsFilterUtils.canQueryAsInstaller(newPkgSetting, pkg2) || com.android.server.pm.AppsFilterUtils.canQueryAsUpdateOwner(newPkgSetting, pkg2)) {
                        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock11 = this.mQueriesViaPackageLock;
                        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                        synchronized (packageManagerTracedLock11) {
                            try {
                                this.mQueriesViaPackage.add(newPkgSetting.getAppId(), java.lang.Integer.valueOf(existingSetting.getAppId()));
                            } finally {
                                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            }
                        }
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    }
                    if (com.android.server.pm.AppsFilterUtils.canQueryViaUsesLibrary(pkg, pkg2)) {
                        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock12 = this.mQueryableViaUsesLibraryLock;
                        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                        synchronized (packageManagerTracedLock12) {
                            try {
                                this.mQueryableViaUsesLibrary.add(newPkgSetting.getAppId(), java.lang.Integer.valueOf(existingSetting.getAppId()));
                            } finally {
                                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            }
                        }
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    }
                }
                if (newPkgSetting.getPkg() != null && existingSetting.getPkg() != null && (pkgInstruments(newPkgSetting.getPkg(), existingSetting.getPkg()) || pkgInstruments(existingSetting.getPkg(), newPkgSetting.getPkg()))) {
                    com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock13 = this.mQueriesViaPackageLock;
                    com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                    synchronized (packageManagerTracedLock13) {
                        try {
                            this.mQueriesViaPackage.add(newPkgSetting.getAppId(), java.lang.Integer.valueOf(existingSetting.getAppId()));
                            this.mQueriesViaPackage.add(existingSetting.getAppId(), java.lang.Integer.valueOf(newPkgSetting.getAppId()));
                        } finally {
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        }
                    }
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                }
            }
        }
        int existingSize = existingSettings.size();
        android.util.ArrayMap<java.lang.String, com.android.server.pm.pkg.AndroidPackage> existingPkgs = new android.util.ArrayMap<>(existingSize);
        for (int index = 0; index < existingSize; index++) {
            com.android.server.pm.pkg.PackageStateInternal pkgSetting = existingSettings.valueAt(index);
            if (pkgSetting.getPkg() != null) {
                existingPkgs.put(pkgSetting.getPackageName(), pkgSetting.getPkg());
            }
        }
        android.util.ArraySet<java.lang.String> changedPackages = this.mOverlayReferenceMapper.addPkg(newPkgSetting.getPkg(), existingPkgs);
        this.mFeatureConfig.updatePackageState(newPkgSetting, false);
        return changedPackages;
    }

    private void removeAppIdFromVisibilityCache(int appId) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mCacheLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            int i = 0;
            while (i < this.mShouldFilterCache.size()) {
                try {
                    if (android.os.UserHandle.getAppId(this.mShouldFilterCache.keyAt(i)) == appId) {
                        this.mShouldFilterCache.removeAt(i);
                        i--;
                    }
                    i++;
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    private void updateEntireShouldFilterCache(com.android.server.pm.Computer snapshot, int subjectUserId) {
        android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> settings = snapshot.getPackageStates();
        android.content.pm.UserInfo[] users = snapshot.getUserInfos();
        int userId = -10000;
        int u = 0;
        while (true) {
            if (u >= users.length) {
                break;
            }
            if (subjectUserId != users[u].id) {
                u++;
            } else {
                userId = subjectUserId;
                break;
            }
        }
        if (userId == -10000) {
            android.util.Slog.e("AppsFilter", "We encountered a new user that isn't a member of known users, updating the whole cache");
            userId = -1;
        }
        updateEntireShouldFilterCacheInner(snapshot, settings, users, userId);
        onChanged();
    }

    private void updateEntireShouldFilterCacheInner(com.android.server.pm.Computer snapshot, android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> settings, android.content.pm.UserInfo[] users, int subjectUserId) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mCacheLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            if (subjectUserId == -1) {
                try {
                    this.mShouldFilterCache.clear();
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            this.mShouldFilterCache.setCapacity(users.length * settings.size());
            for (int i = settings.size() - 1; i >= 0; i--) {
                updateShouldFilterCacheForPackage(snapshot, null, settings.valueAt(i), settings, users, subjectUserId, i);
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    private void updateEntireShouldFilterCacheAsync(android.content.pm.PackageManagerInternal pmInternal, int reason) {
        updateEntireShouldFilterCacheAsync(pmInternal, 10000L, reason);
    }

    private void updateEntireShouldFilterCacheAsync(final android.content.pm.PackageManagerInternal pmInternal, final long delayMs, final int reason) {
        this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.pm.AppsFilterImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateEntireShouldFilterCacheAsync$0(pmInternal, reason, delayMs);
            }
        }, delayMs);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateEntireShouldFilterCacheAsync$0(android.content.pm.PackageManagerInternal pmInternal, int reason, long delayMs) {
        if (!this.mCacheValid.compareAndSet(false, true)) {
            return;
        }
        long currentTimeUs = android.os.SystemClock.currentTimeMicro();
        android.util.ArrayMap<java.lang.String, com.android.server.pm.pkg.AndroidPackage> packagesCache = new android.util.ArrayMap<>();
        com.android.server.pm.Computer snapshot = (com.android.server.pm.Computer) pmInternal.snapshot();
        android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> settings = snapshot.getPackageStates();
        android.content.pm.UserInfo[] users = snapshot.getUserInfos();
        packagesCache.ensureCapacity(settings.size());
        android.content.pm.UserInfo[][] usersRef = {users};
        int max = settings.size();
        for (int i = 0; i < max; i++) {
            packagesCache.put(settings.keyAt(i), settings.valueAt(i).getPkg());
        }
        updateEntireShouldFilterCacheInner(snapshot, settings, usersRef[0], -1);
        logCacheRebuilt(reason, android.os.SystemClock.currentTimeMicro() - currentTimeUs, users.length, settings.size());
        if (this.mCacheValid.compareAndSet(true, true)) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mImplicitlyQueryableLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    if (this.mNeedToUpdateCacheForImplicitAccess) {
                        updateShouldFilterCacheForImplicitAccess();
                        this.mNeedToUpdateCacheForImplicitAccess = false;
                    }
                    this.mCacheReady = true;
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            onChanged();
            return;
        }
        android.util.Slog.i("AppsFilter", "Cache invalidated while building, retrying.");
        updateEntireShouldFilterCacheAsync(pmInternal, java.lang.Math.min(2 * delayMs, 10000L), reason);
    }

    public void onUserCreated(com.android.server.pm.Computer snapshot, int newUserId) {
        if (!this.mCacheReady) {
            return;
        }
        long currentTimeUs = android.os.SystemClock.currentTimeMicro();
        updateEntireShouldFilterCache(snapshot, newUserId);
        logCacheRebuilt(2, android.os.SystemClock.currentTimeMicro() - currentTimeUs, snapshot.getUserInfos().length, snapshot.getPackageStates().size());
    }

    public void onUserDeleted(com.android.server.pm.Computer snapshot, int userId) {
        if (!this.mCacheReady) {
            return;
        }
        long currentTimeUs = android.os.SystemClock.currentTimeMicro();
        removeShouldFilterCacheForUser(userId);
        onChanged();
        logCacheRebuilt(3, android.os.SystemClock.currentTimeMicro() - currentTimeUs, snapshot.getUserInfos().length, snapshot.getPackageStates().size());
    }

    private void updateShouldFilterCacheForImplicitAccess() {
        updateShouldFilterCacheForImplicitAccess(this.mRetainedImplicitlyQueryable);
        updateShouldFilterCacheForImplicitAccess(this.mImplicitlyQueryable);
    }

    private void updateShouldFilterCacheForImplicitAccess(com.android.server.utils.WatchedSparseSetArray<java.lang.Integer> queriesMap) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mCacheLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            for (int i = 0; i < queriesMap.size(); i++) {
                try {
                    java.lang.Integer callingUid = java.lang.Integer.valueOf(queriesMap.keyAt(i));
                    android.util.ArraySet<java.lang.Integer> targetUids = queriesMap.get(callingUid.intValue());
                    for (java.lang.Integer targetUid : targetUids) {
                        this.mShouldFilterCache.put(callingUid.intValue(), targetUid.intValue(), false);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateShouldFilterCacheForPackage(com.android.server.pm.Computer snapshot, java.lang.String packageName) {
        if (!this.mCacheReady) {
            return;
        }
        android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> settings = snapshot.getPackageStates();
        android.content.pm.UserInfo[] users = snapshot.getUserInfos();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mCacheLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                updateShouldFilterCacheForPackage(snapshot, null, settings.get(packageName), settings, users, -1, settings.size());
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        onChanged();
    }

    private void updateShouldFilterCacheForPackage(com.android.server.pm.Computer snapshot, java.lang.String skipPackageName, com.android.server.pm.pkg.PackageStateInternal subjectSetting, android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> allSettings, android.content.pm.UserInfo[] allUsers, int subjectUserId, int maxIndex) {
        for (int i = java.lang.Math.min(maxIndex, allSettings.size() - 1); i >= 0; i--) {
            com.android.server.pm.pkg.PackageStateInternal otherSetting = allSettings.valueAt(i);
            if (subjectSetting.getAppId() != otherSetting.getAppId() && subjectSetting.getPackageName() != skipPackageName && otherSetting.getPackageName() != skipPackageName) {
                if (subjectUserId == -1) {
                    for (android.content.pm.UserInfo userInfo : allUsers) {
                        updateShouldFilterCacheForUser(snapshot, subjectSetting, allUsers, otherSetting, userInfo.id);
                    }
                } else {
                    updateShouldFilterCacheForUser(snapshot, subjectSetting, allUsers, otherSetting, subjectUserId);
                }
            }
        }
    }

    private void updateShouldFilterCacheForUser(com.android.server.pm.Computer snapshot, com.android.server.pm.pkg.PackageStateInternal subjectSetting, android.content.pm.UserInfo[] allUsers, com.android.server.pm.pkg.PackageStateInternal otherSetting, int subjectUserId) {
        for (android.content.pm.UserInfo userInfo : allUsers) {
            int otherUser = userInfo.id;
            int subjectUid = android.os.UserHandle.getUid(subjectUserId, subjectSetting.getAppId());
            int otherUid = android.os.UserHandle.getUid(otherUser, otherSetting.getAppId());
            boolean shouldFilterSubjectToOther = shouldFilterApplicationInternal(snapshot, subjectUid, subjectSetting, otherSetting, otherUser);
            boolean shouldFilterOtherToSubject = shouldFilterApplicationInternal(snapshot, otherUid, otherSetting, subjectSetting, subjectUserId);
            this.mShouldFilterCache.put(subjectUid, otherUid, shouldFilterSubjectToOther);
            this.mShouldFilterCache.put(otherUid, subjectUid, shouldFilterOtherToSubject);
        }
    }

    private void removeShouldFilterCacheForUser(int userId) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mCacheLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                int[] cacheUids = this.mShouldFilterCache.keys();
                int size = cacheUids.length;
                int pos = java.util.Arrays.binarySearch(cacheUids, android.os.UserHandle.getUid(userId, 0));
                int fromIndex = pos >= 0 ? pos : ~pos;
                if (fromIndex < size && android.os.UserHandle.getUserId(cacheUids[fromIndex]) == userId) {
                    int pos2 = java.util.Arrays.binarySearch(cacheUids, android.os.UserHandle.getUid(userId + 1, 0) - 1);
                    int toIndex = pos2 >= 0 ? pos2 + 1 : ~pos2;
                    if (fromIndex < toIndex && android.os.UserHandle.getUserId(cacheUids[toIndex - 1]) == userId) {
                        this.mShouldFilterCache.removeRange(fromIndex, toIndex);
                        this.mShouldFilterCache.compact();
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        return;
                    }
                    android.util.Slog.w("AppsFilter", "Failed to remove should filter cache for user " + userId + ", fromIndex=" + fromIndex + ", toIndex=" + toIndex);
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return;
                }
                android.util.Slog.w("AppsFilter", "Failed to remove should filter cache for user " + userId + ", fromIndex=" + fromIndex);
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    private static boolean isSystemSigned(android.content.pm.SigningDetails sysSigningDetails, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        return pkgSetting.isSystem() && pkgSetting.getSigningDetails().signaturesMatchExactly(sysSigningDetails);
    }

    private void collectProtectedBroadcasts(android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> existingSettings, java.lang.String excludePackage) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mProtectedBroadcastsLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mProtectedBroadcasts.clear();
                for (int i = existingSettings.size() - 1; i >= 0; i--) {
                    com.android.server.pm.pkg.PackageStateInternal setting = existingSettings.valueAt(i);
                    if (setting.getPkg() != null && !setting.getPkg().getPackageName().equals(excludePackage)) {
                        java.util.List<java.lang.String> protectedBroadcasts = setting.getPkg().getProtectedBroadcasts();
                        if (!protectedBroadcasts.isEmpty()) {
                            this.mProtectedBroadcasts.addAll(protectedBroadcasts);
                        }
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    @Override // com.android.server.pm.AppsFilterBase
    protected boolean isQueryableViaComponentWhenRequireRecompute(android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> existingSettings, com.android.server.pm.pkg.PackageStateInternal callingPkgSetting, android.util.ArraySet<com.android.server.pm.pkg.PackageStateInternal> callingSharedPkgSettings, com.android.server.pm.pkg.AndroidPackage targetPkg, int callingAppId, int targetAppId) {
        recomputeComponentVisibility(existingSettings);
        return isQueryableViaComponent(callingAppId, targetAppId);
    }

    private void recomputeComponentVisibility(android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> existingSettings) {
        com.android.server.utils.WatchedArraySet<java.lang.String> protectedBroadcasts;
        android.util.ArraySet<java.lang.Integer> forceQueryable;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mProtectedBroadcastsLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                protectedBroadcasts = new com.android.server.utils.WatchedArraySet<>(this.mProtectedBroadcasts);
            } finally {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mForceQueryableLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock2) {
            try {
                forceQueryable = new android.util.ArraySet<>(this.mForceQueryable.untrackedStorage());
            } finally {
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        com.android.server.pm.AppsFilterUtils.ParallelComputeComponentVisibility computer = new com.android.server.pm.AppsFilterUtils.ParallelComputeComponentVisibility(existingSettings, forceQueryable, protectedBroadcasts);
        android.util.SparseSetArray<java.lang.Integer> queriesViaComponent = computer.execute();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock3 = this.mQueriesViaComponentLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock3) {
            try {
                this.mQueriesViaComponent = new com.android.server.utils.WatchedSparseSetArray<>(queriesViaComponent);
                this.mQueriesViaComponentSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mQueriesViaComponent, this.mQueriesViaComponent, "AppsFilter.mQueriesViaComponent");
            } finally {
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        this.mQueriesViaComponentRequireRecompute.set(false);
        onChanged();
    }

    public void addPackage(com.android.server.pm.Computer snapshot, com.android.server.pm.pkg.PackageStateInternal newPkgSetting) {
        addPackage(snapshot, newPkgSetting, false, false);
    }

    public void removePackage(com.android.server.pm.Computer snapshot, com.android.server.pm.pkg.PackageStateInternal setting) throws java.lang.Throwable {
        long currentTimeUs = android.os.SystemClock.currentTimeMicro();
        removePackageInternal(snapshot, setting, false, false);
        logCacheUpdated(2, android.os.SystemClock.currentTimeMicro() - currentTimeUs, snapshot.getUserInfos().length, snapshot.getPackageStates().size(), setting.getAppId());
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x0274  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x027c  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x029f  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x035a  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x024f  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0269  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:193:? -> B:149:0x0349). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:162:0x037c
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void removePackageInternal(com.android.server.pm.Computer r23, com.android.server.pm.pkg.PackageStateInternal r24, boolean r25, boolean r26) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 928
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.AppsFilterImpl.removePackageInternal(com.android.server.pm.Computer, com.android.server.pm.pkg.PackageStateInternal, boolean, boolean):void");
    }

    private static boolean pkgInstruments(com.android.server.pm.pkg.AndroidPackage source, com.android.server.pm.pkg.AndroidPackage target) {
        java.lang.String packageName = target.getPackageName();
        java.util.List<com.android.internal.pm.pkg.component.ParsedInstrumentation> inst = source.getInstrumentations();
        for (int i = com.android.internal.util.ArrayUtils.size(inst) - 1; i >= 0; i--) {
            if (java.util.Objects.equals(inst.get(i).getTargetPackage(), packageName)) {
                return true;
            }
        }
        return false;
    }

    private void logCacheRebuilt(int eventId, long latency, int userCount, int packageCount) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.PACKAGE_MANAGER_APPS_FILTER_CACHE_BUILD_REPORTED, eventId, latency, userCount, packageCount, this.mShouldFilterCache.size());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logCacheUpdated(int eventId, long latency, int userCount, int packageCount, int appId) {
        if (!this.mCacheReady) {
            return;
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.PACKAGE_MANAGER_APPS_FILTER_CACHE_UPDATE_REPORTED, eventId, appId, latency, userCount, packageCount, this.mShouldFilterCache.size());
    }
}
