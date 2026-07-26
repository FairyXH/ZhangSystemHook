package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AppsFilterBase implements com.android.server.pm.AppsFilterSnapshot {
    protected static final boolean CACHE_INVALID = false;
    protected static final int CACHE_REBUILD_DELAY_MAX_MS = 10000;
    protected static final int CACHE_REBUILD_DELAY_MIN_MS = 10000;
    protected static final boolean CACHE_VALID = true;
    protected static final boolean DEBUG_ALLOW_ALL = false;
    protected static final boolean DEBUG_LOGGING = false;
    public static final boolean DEBUG_TRACING = false;
    protected static final java.lang.String TAG = "AppsFilter";
    protected com.android.server.pm.FeatureConfig mFeatureConfig;

    @com.android.server.utils.Watched
    protected com.android.server.utils.WatchedArraySet<java.lang.Integer> mForceQueryable;
    protected java.lang.String[] mForceQueryableByDevicePackageNames;
    protected com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedArraySet<java.lang.Integer>> mForceQueryableSnapshot;
    protected android.os.Handler mHandler;
    protected com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedSparseSetArray<java.lang.Integer>> mImplicitQueryableSnapshot;

    @com.android.server.utils.Watched
    protected com.android.server.utils.WatchedSparseSetArray<java.lang.Integer> mImplicitlyQueryable;
    protected com.android.server.om.OverlayReferenceMapper mOverlayReferenceMapper;

    @com.android.server.utils.Watched
    protected com.android.server.utils.WatchedArraySet<java.lang.String> mProtectedBroadcasts;
    protected com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedArraySet<java.lang.String>> mProtectedBroadcastsSnapshot;

    @com.android.server.utils.Watched
    protected com.android.server.utils.WatchedSparseSetArray<java.lang.Integer> mQueriesViaComponent;
    protected com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedSparseSetArray<java.lang.Integer>> mQueriesViaComponentSnapshot;

    @com.android.server.utils.Watched
    protected com.android.server.utils.WatchedSparseSetArray<java.lang.Integer> mQueriesViaPackage;
    protected com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedSparseSetArray<java.lang.Integer>> mQueriesViaPackageSnapshot;

    @com.android.server.utils.Watched
    protected com.android.server.utils.WatchedSparseSetArray<java.lang.Integer> mQueryableViaUsesLibrary;
    protected com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedSparseSetArray<java.lang.Integer>> mQueryableViaUsesLibrarySnapshot;

    @com.android.server.utils.Watched
    protected com.android.server.utils.WatchedSparseSetArray<java.lang.Integer> mQueryableViaUsesPermission;
    protected com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedSparseSetArray<java.lang.Integer>> mQueryableViaUsesPermissionSnapshot;

    @com.android.server.utils.Watched
    protected com.android.server.utils.WatchedSparseSetArray<java.lang.Integer> mRetainedImplicitlyQueryable;
    protected com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedSparseSetArray<java.lang.Integer>> mRetainedImplicitlyQueryableSnapshot;

    @com.android.server.utils.Watched
    protected com.android.server.utils.WatchedSparseBooleanMatrix mShouldFilterCache;
    protected com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedSparseBooleanMatrix> mShouldFilterCacheSnapshot;
    protected boolean mSystemAppsQueryable;
    protected android.content.pm.SigningDetails mSystemSigningDetails;
    protected java.util.concurrent.atomic.AtomicBoolean mQueriesViaComponentRequireRecompute = new java.util.concurrent.atomic.AtomicBoolean(false);
    protected volatile boolean mCacheReady = false;
    protected volatile boolean mCacheEnabled = true;
    protected volatile boolean mNeedToUpdateCacheForImplicitAccess = false;
    protected final java.util.concurrent.atomic.AtomicBoolean mCacheValid = new java.util.concurrent.atomic.AtomicBoolean(false);

    protected interface ToString<T> {
        java.lang.String toString(T t);
    }

    protected boolean isForceQueryable(int callingAppId) {
        return this.mForceQueryable.contains(java.lang.Integer.valueOf(callingAppId));
    }

    protected boolean isQueryableViaPackage(int callingAppId, int targetAppId) {
        return this.mQueriesViaPackage.contains(callingAppId, java.lang.Integer.valueOf(targetAppId));
    }

    protected boolean isQueryableViaComponent(int callingAppId, int targetAppId) {
        return this.mQueriesViaComponent.contains(callingAppId, java.lang.Integer.valueOf(targetAppId));
    }

    protected boolean isImplicitlyQueryable(int callingUid, int targetUid) {
        return this.mImplicitlyQueryable.contains(callingUid, java.lang.Integer.valueOf(targetUid));
    }

    protected boolean isRetainedImplicitlyQueryable(int callingUid, int targetUid) {
        return this.mRetainedImplicitlyQueryable.contains(callingUid, java.lang.Integer.valueOf(targetUid));
    }

    protected boolean isQueryableViaUsesLibrary(int callingAppId, int targetAppId) {
        return this.mQueryableViaUsesLibrary.contains(callingAppId, java.lang.Integer.valueOf(targetAppId));
    }

    protected boolean isQueryableViaUsesPermission(int callingAppId, int targetAppId) {
        return this.mQueryableViaUsesPermission.contains(callingAppId, java.lang.Integer.valueOf(targetAppId));
    }

    protected boolean isQueryableViaComponentWhenRequireRecompute(android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> existingSettings, com.android.server.pm.pkg.PackageStateInternal callingPkgSetting, android.util.ArraySet<com.android.server.pm.pkg.PackageStateInternal> callingSharedPkgSettings, com.android.server.pm.pkg.AndroidPackage targetPkg, int callingAppId, int targetAppId) {
        if (callingPkgSetting == null) {
            for (int i = callingSharedPkgSettings.size() - 1; i >= 0; i--) {
                com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = callingSharedPkgSettings.valueAt(i).getPkg();
                if (pkg != null && com.android.server.pm.AppsFilterUtils.canQueryViaComponents(pkg, targetPkg, this.mProtectedBroadcasts)) {
                    return true;
                }
            }
            return false;
        }
        if (callingPkgSetting.getPkg() != null && com.android.server.pm.AppsFilterUtils.canQueryViaComponents(callingPkgSetting.getPkg(), targetPkg, this.mProtectedBroadcasts)) {
            return true;
        }
        return false;
    }

    @Override // com.android.server.pm.AppsFilterSnapshot
    public android.util.SparseArray<int[]> getVisibilityAllowList(com.android.server.pm.snapshot.PackageDataSnapshot snapshot, com.android.server.pm.pkg.PackageStateInternal setting, int[] users, android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> existingSettings) {
        int loc;
        int[] iArr = users;
        if (isForceQueryable(setting.getAppId())) {
            return null;
        }
        android.util.SparseArray<int[]> result = new android.util.SparseArray<>(iArr.length);
        int u = 0;
        while (u < iArr.length) {
            int userId = iArr[u];
            int[] appIds = new int[existingSettings.size()];
            int[] buffer = null;
            int allowListSize = 0;
            for (int i = existingSettings.size() - 1; i >= 0; i--) {
                com.android.server.pm.pkg.PackageStateInternal existingSetting = existingSettings.valueAt(i);
                int existingAppId = existingSetting.getAppId();
                if (existingAppId >= 10000 && (loc = java.util.Arrays.binarySearch(appIds, 0, allowListSize, existingAppId)) < 0) {
                    int existingUid = android.os.UserHandle.getUid(userId, existingAppId);
                    if (!shouldFilterApplication(snapshot, existingUid, existingSetting, setting, userId)) {
                        if (buffer == null) {
                            buffer = new int[appIds.length];
                        }
                        int insert = ~loc;
                        java.lang.System.arraycopy(appIds, insert, buffer, 0, allowListSize - insert);
                        appIds[insert] = existingAppId;
                        java.lang.System.arraycopy(buffer, 0, appIds, insert + 1, allowListSize - insert);
                        allowListSize++;
                    }
                }
            }
            result.put(userId, java.util.Arrays.copyOf(appIds, allowListSize));
            u++;
            iArr = users;
        }
        return result;
    }

    android.util.SparseArray<int[]> getVisibilityAllowList(com.android.server.pm.snapshot.PackageDataSnapshot snapshot, com.android.server.pm.pkg.PackageStateInternal setting, int[] users, com.android.server.utils.WatchedArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> existingSettings) {
        return getVisibilityAllowList(snapshot, setting, users, existingSettings.untrackedStorage());
    }

    private static boolean isQueryableBySdkSandbox(int callingUid, int targetUid) {
        return com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.allowSdkSandboxQueryIntentActivities() && targetUid == android.os.Process.getAppUidForSdkSandboxUid(callingUid);
    }

    @Override // com.android.server.pm.AppsFilterSnapshot
    public boolean shouldFilterApplication(com.android.server.pm.snapshot.PackageDataSnapshot snapshot, int callingUid, java.lang.Object callingSetting, com.android.server.pm.pkg.PackageStateInternal targetPkgSetting, int userId) {
        int callingAppId = android.os.UserHandle.getAppId(callingUid);
        if (callingAppId >= 10000 && targetPkgSetting.getAppId() >= 10000 && callingAppId != targetPkgSetting.getAppId()) {
            if (android.os.Process.isSdkSandboxUid(callingAppId)) {
                int targetAppId = targetPkgSetting.getAppId();
                int targetUid = android.os.UserHandle.getUid(userId, targetAppId);
                return (isForceQueryable(targetPkgSetting.getAppId()) || isImplicitlyQueryable(callingUid, targetUid) || isQueryableBySdkSandbox(callingUid, targetUid)) ? false : true;
            }
            if (this.mCacheReady && this.mCacheEnabled) {
                if (!shouldFilterApplicationUsingCache(callingUid, targetPkgSetting.getAppId(), userId)) {
                    return false;
                }
            } else if (!shouldFilterApplicationInternal((com.android.server.pm.Computer) snapshot, callingUid, callingSetting, targetPkgSetting, userId)) {
                return false;
            }
            if (this.mFeatureConfig.isLoggingEnabled(callingAppId)) {
                log(callingSetting, targetPkgSetting, "BLOCKED");
            }
            return true;
        }
        return false;
    }

    protected boolean shouldFilterApplicationUsingCache(int callingUid, int appId, int userId) {
        int callingIndex = this.mShouldFilterCache.indexOfKey(callingUid);
        if (callingIndex < 0) {
            android.util.Slog.wtf(TAG, "Encountered calling uid with no cached rules: " + callingUid);
            return true;
        }
        int targetUid = android.os.UserHandle.getUid(userId, appId);
        int targetIndex = this.mShouldFilterCache.indexOfKey(targetUid);
        if (targetIndex < 0) {
            android.util.Slog.w(TAG, "Encountered calling -> target with no cached rules: " + callingUid + " -> " + targetUid);
            return true;
        }
        return this.mShouldFilterCache.valueAt(callingIndex, targetIndex);
    }

    protected boolean shouldFilterApplicationInternal(com.android.server.pm.Computer snapshot, int callingUid, java.lang.Object callingSetting, com.android.server.pm.pkg.PackageStateInternal targetPkgSetting, int targetUserId) {
        com.android.server.pm.pkg.PackageStateInternal callingPkgSetting;
        com.android.server.pm.pkg.PackageStateInternal callingPkgSetting2;
        android.util.ArraySet<com.android.server.pm.pkg.PackageStateInternal> callingSharedPkgSettings;
        try {
            boolean featureEnabled = this.mFeatureConfig.isGloballyEnabled();
            if (!featureEnabled) {
                return false;
            }
            if (callingSetting == null) {
                android.util.Slog.wtf(TAG, "No setting found for non system uid " + callingUid);
                return true;
            }
            int callingAppId = android.os.UserHandle.getAppId(callingUid);
            int targetAppId = targetPkgSetting.getAppId();
            if (callingAppId != targetAppId && callingAppId >= 10000 && targetAppId >= 10000) {
                android.util.ArraySet<com.android.server.pm.pkg.PackageStateInternal> callingSharedPkgSettings2 = new android.util.ArraySet<>();
                if (callingSetting instanceof com.android.server.pm.pkg.PackageStateInternal) {
                    try {
                        com.android.server.pm.pkg.PackageStateInternal packageState = (com.android.server.pm.pkg.PackageStateInternal) callingSetting;
                        if (packageState.hasSharedUser()) {
                            callingPkgSetting = null;
                            com.android.server.pm.pkg.SharedUserApi sharedUserApi = snapshot.getSharedUser(packageState.getSharedUserAppId());
                            if (sharedUserApi != null) {
                                callingSharedPkgSettings2.addAll(sharedUserApi.getPackageStates());
                            }
                        } else {
                            callingPkgSetting = packageState;
                        }
                        callingPkgSetting2 = callingPkgSetting;
                    } catch (java.lang.Throwable th) {
                        throw th;
                    }
                } else {
                    callingSharedPkgSettings2.addAll(((com.android.server.pm.SharedUserSetting) callingSetting).getPackageStates());
                    callingPkgSetting2 = null;
                }
                if (callingPkgSetting2 == null) {
                    for (int i = callingSharedPkgSettings2.size() - 1; i >= 0; i--) {
                        com.android.server.pm.pkg.AndroidPackage pkg = callingSharedPkgSettings2.valueAt(i).getPkg();
                        if (pkg != null && !this.mFeatureConfig.packageIsEnabled(pkg)) {
                            return false;
                        }
                    }
                } else if (callingPkgSetting2.getPkg() != null && !this.mFeatureConfig.packageIsEnabled(callingPkgSetting2.getPkg())) {
                    return false;
                }
                if (callingPkgSetting2 == null) {
                    try {
                        for (int i2 = callingSharedPkgSettings2.size() - 1; i2 >= 0; i2--) {
                            com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg2 = callingSharedPkgSettings2.valueAt(i2).getPkg();
                            if (pkg2 != null && com.android.server.pm.AppsFilterUtils.requestsQueryAllPackages(pkg2)) {
                                return false;
                            }
                        }
                    } catch (java.lang.Throwable th2) {
                        throw th2;
                    }
                } else if (callingPkgSetting2.getPkg() != null && com.android.server.pm.AppsFilterUtils.requestsQueryAllPackages(callingPkgSetting2.getPkg())) {
                    return false;
                }
                com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg3 = targetPkgSetting.getPkg();
                if (pkg3 == null) {
                    return true;
                }
                if (pkg3.isStaticSharedLibrary()) {
                    return false;
                }
                if (isForceQueryable(targetAppId)) {
                    return false;
                }
                if (isQueryableViaPackage(callingAppId, targetAppId)) {
                    return false;
                }
                try {
                    if (!this.mQueriesViaComponentRequireRecompute.get()) {
                        if (isQueryableViaComponent(callingAppId, targetAppId)) {
                            return false;
                        }
                    } else {
                        try {
                            if (isQueryableViaComponentWhenRequireRecompute(snapshot.getPackageStates(), callingPkgSetting2, callingSharedPkgSettings2, pkg3, callingAppId, targetAppId)) {
                                return false;
                            }
                        } finally {
                        }
                    }
                    try {
                        int targetUid = android.os.UserHandle.getUid(targetUserId, targetAppId);
                        if (isImplicitlyQueryable(callingUid, targetUid)) {
                            return false;
                        }
                        try {
                            int targetUid2 = android.os.UserHandle.getUid(targetUserId, targetAppId);
                            if (isRetainedImplicitlyQueryable(callingUid, targetUid2)) {
                                return false;
                            }
                            try {
                                java.lang.String targetName = pkg3.getPackageName();
                                try {
                                    if (!callingSharedPkgSettings2.isEmpty()) {
                                        int size = callingSharedPkgSettings2.size();
                                        int index = 0;
                                        while (index < size) {
                                            com.android.server.pm.pkg.PackageStateInternal pkgSetting = callingSharedPkgSettings.valueAt(index);
                                            if (this.mOverlayReferenceMapper.isValidActor(targetName, pkgSetting.getPackageName())) {
                                                return false;
                                            }
                                            index++;
                                            callingSharedPkgSettings2 = callingSharedPkgSettings;
                                        }
                                    } else if (this.mOverlayReferenceMapper.isValidActor(targetName, callingPkgSetting2.getPackageName())) {
                                        return false;
                                    }
                                    if (isQueryableViaUsesLibrary(callingAppId, targetAppId)) {
                                        return false;
                                    }
                                    if (isQueryableViaUsesPermission(callingAppId, targetAppId)) {
                                        return false;
                                    }
                                    return true;
                                } finally {
                                }
                            } catch (java.lang.Throwable th3) {
                                throw th3;
                            }
                        } finally {
                        }
                    } finally {
                    }
                } catch (java.lang.Throwable th4) {
                    throw th4;
                }
            }
            return false;
        } catch (java.lang.Throwable th5) {
            throw th5;
        }
    }

    @Override // com.android.server.pm.AppsFilterSnapshot
    public boolean canQueryPackage(com.android.server.pm.pkg.AndroidPackage querying, java.lang.String potentialTarget) {
        int appId = android.os.UserHandle.getAppId(querying.getUid());
        if (appId >= 10000 && this.mFeatureConfig.packageIsEnabled(querying) && !com.android.server.pm.AppsFilterUtils.requestsQueryAllPackages(querying)) {
            return !querying.getQueriesPackages().isEmpty() && querying.getQueriesPackages().contains(potentialTarget);
        }
        return true;
    }

    private static void log(java.lang.Object callingSetting, com.android.server.pm.pkg.PackageStateInternal targetPkgSetting, java.lang.String description) {
        android.util.Slog.i(TAG, "interaction: " + (callingSetting == null ? "system" : callingSetting) + " -> " + targetPkgSetting + " " + description);
    }

    @Override // com.android.server.pm.AppsFilterSnapshot
    public void dumpQueries(java.io.PrintWriter pw, java.lang.Integer filteringAppId, com.android.server.pm.DumpState dumpState, final int[] users, final com.android.internal.util.function.QuadFunction<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Boolean, java.lang.String[]> getPackagesForUid) {
        final android.util.SparseArray<java.lang.String> cache = new android.util.SparseArray<>();
        com.android.server.pm.AppsFilterBase.ToString<java.lang.Integer> expandPackages = new com.android.server.pm.AppsFilterBase.ToString() { // from class: com.android.server.pm.AppsFilterBase$$ExternalSyntheticLambda0
            @Override // com.android.server.pm.AppsFilterBase.ToString
            public final java.lang.String toString(java.lang.Object obj) {
                return com.android.server.pm.AppsFilterBase.lambda$dumpQueries$0(cache, users, getPackagesForUid, (java.lang.Integer) obj);
            }
        };
        pw.println();
        pw.println("Queries:");
        dumpState.onTitlePrinted();
        if (!this.mFeatureConfig.isGloballyEnabled()) {
            pw.println("  DISABLED");
            return;
        }
        pw.println("  system apps queryable: " + this.mSystemAppsQueryable);
        dumpForceQueryable(pw, filteringAppId, expandPackages);
        dumpQueriesViaPackage(pw, filteringAppId, expandPackages);
        dumpQueriesViaComponent(pw, filteringAppId, expandPackages);
        dumpQueriesViaImplicitlyQueryable(pw, filteringAppId, users, expandPackages);
        dumpQueriesViaUsesLibrary(pw, filteringAppId, expandPackages);
    }

    static /* synthetic */ java.lang.String lambda$dumpQueries$0(android.util.SparseArray cache, int[] users, com.android.internal.util.function.QuadFunction getPackagesForUid, java.lang.Integer input) {
        java.lang.String cachedValue = (java.lang.String) cache.get(input.intValue());
        if (cachedValue == null) {
            int callingUid = android.os.Binder.getCallingUid();
            int appId = android.os.UserHandle.getAppId(input.intValue());
            java.lang.String[] packagesForUid = null;
            int size = users.length;
            for (int i = 0; packagesForUid == null && i < size; i++) {
                packagesForUid = (java.lang.String[]) getPackagesForUid.apply(java.lang.Integer.valueOf(callingUid), java.lang.Integer.valueOf(users[i]), java.lang.Integer.valueOf(appId), false);
            }
            if (packagesForUid == null) {
                cachedValue = "[app id " + input + " not installed]";
            } else {
                cachedValue = packagesForUid.length == 1 ? packagesForUid[0] : "[" + android.text.TextUtils.join(",", packagesForUid) + "]";
            }
            cache.put(input.intValue(), cachedValue);
        }
        return cachedValue;
    }

    protected void dumpForceQueryable(java.io.PrintWriter pw, java.lang.Integer filteringAppId, com.android.server.pm.AppsFilterBase.ToString<java.lang.Integer> expandPackages) {
        pw.println("  queries via forceQueryable:");
        dumpPackageSet(pw, filteringAppId, this.mForceQueryable.untrackedStorage(), "forceQueryable", "  ", expandPackages);
    }

    protected void dumpQueriesViaPackage(java.io.PrintWriter pw, java.lang.Integer filteringAppId, com.android.server.pm.AppsFilterBase.ToString<java.lang.Integer> expandPackages) {
        pw.println("  queries via package name:");
        dumpQueriesMap(pw, filteringAppId, this.mQueriesViaPackage, "    ", expandPackages);
    }

    protected void dumpQueriesViaComponent(java.io.PrintWriter pw, java.lang.Integer filteringAppId, com.android.server.pm.AppsFilterBase.ToString<java.lang.Integer> expandPackages) {
        pw.println("  queries via component:");
        dumpQueriesMap(pw, filteringAppId, this.mQueriesViaComponent, "    ", expandPackages);
    }

    protected void dumpQueriesViaImplicitlyQueryable(java.io.PrintWriter pw, java.lang.Integer filteringAppId, int[] users, com.android.server.pm.AppsFilterBase.ToString<java.lang.Integer> expandPackages) {
        pw.println("  queryable via interaction:");
        for (int user : users) {
            pw.append("    User ").append((java.lang.CharSequence) java.lang.Integer.toString(user)).println(":");
            java.lang.Integer numValueOf = null;
            dumpQueriesMap(pw, filteringAppId == null ? null : java.lang.Integer.valueOf(android.os.UserHandle.getUid(user, filteringAppId.intValue())), this.mImplicitlyQueryable, "      ", expandPackages);
            if (filteringAppId != null) {
                numValueOf = java.lang.Integer.valueOf(android.os.UserHandle.getUid(user, filteringAppId.intValue()));
            }
            dumpQueriesMap(pw, numValueOf, this.mRetainedImplicitlyQueryable, "      ", expandPackages);
        }
    }

    protected void dumpQueriesViaUsesLibrary(java.io.PrintWriter pw, java.lang.Integer filteringAppId, com.android.server.pm.AppsFilterBase.ToString<java.lang.Integer> expandPackages) {
        pw.println("  queryable via uses-library:");
        dumpQueriesMap(pw, filteringAppId, this.mQueryableViaUsesLibrary, "    ", expandPackages);
    }

    protected void dumpQueriesViaUsesPermission(java.io.PrintWriter pw, java.lang.Integer filteringAppId, com.android.server.pm.AppsFilterBase.ToString<java.lang.Integer> expandPackages) {
        pw.println("  queryable via uses-permission:");
        dumpQueriesMap(pw, filteringAppId, this.mQueryableViaUsesPermission, "    ", expandPackages);
    }

    private static void dumpQueriesMap(java.io.PrintWriter pw, java.lang.Integer filteringId, com.android.server.utils.WatchedSparseSetArray<java.lang.Integer> queriesMap, java.lang.String spacing, com.android.server.pm.AppsFilterBase.ToString<java.lang.Integer> toString) {
        java.lang.String string;
        java.lang.String string2;
        for (int i = 0; i < queriesMap.size(); i++) {
            java.lang.Integer callingId = java.lang.Integer.valueOf(queriesMap.keyAt(i));
            if (java.util.Objects.equals(callingId, filteringId)) {
                android.util.ArraySet<java.lang.Integer> arraySet = queriesMap.get(callingId.intValue());
                if (toString == null) {
                    string2 = callingId.toString();
                } else {
                    string2 = toString.toString(callingId);
                }
                dumpPackageSet(pw, null, arraySet, string2, spacing, toString);
            } else {
                android.util.ArraySet<java.lang.Integer> arraySet2 = queriesMap.get(callingId.intValue());
                if (toString == null) {
                    string = callingId.toString();
                } else {
                    string = toString.toString(callingId);
                }
                dumpPackageSet(pw, filteringId, arraySet2, string, spacing, toString);
            }
        }
    }

    private static <T> void dumpPackageSet(java.io.PrintWriter pw, T filteringId, android.util.ArraySet<T> targetPkgSet, java.lang.String subTitle, java.lang.String spacing, com.android.server.pm.AppsFilterBase.ToString<T> toString) {
        if (targetPkgSet != null && targetPkgSet.size() > 0) {
            if (filteringId == null || targetPkgSet.contains(filteringId)) {
                pw.append((java.lang.CharSequence) spacing).append((java.lang.CharSequence) subTitle).println(":");
                for (T item : targetPkgSet) {
                    if (filteringId == null || java.util.Objects.equals(filteringId, item)) {
                        pw.append((java.lang.CharSequence) spacing).append("  ").println(toString == null ? item : toString.toString(item));
                    }
                }
            }
        }
    }
}
