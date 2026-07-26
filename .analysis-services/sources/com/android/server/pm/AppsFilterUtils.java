package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class AppsFilterUtils {
    AppsFilterUtils() {
    }

    public static boolean requestsQueryAllPackages(com.android.server.pm.pkg.AndroidPackage pkg) {
        return pkg.getRequestedPermissions().contains("android.permission.QUERY_ALL_PACKAGES");
    }

    public static boolean canQueryViaComponents(com.android.server.pm.pkg.AndroidPackage querying, com.android.server.pm.pkg.AndroidPackage potentialTarget, com.android.server.utils.WatchedArraySet<java.lang.String> protectedBroadcasts) {
        if (!querying.getQueriesIntents().isEmpty()) {
            for (android.content.Intent intent : querying.getQueriesIntents()) {
                if (matchesPackage(intent, potentialTarget, protectedBroadcasts)) {
                    return true;
                }
            }
        }
        return !querying.getQueriesProviders().isEmpty() && matchesProviders(querying.getQueriesProviders(), potentialTarget);
    }

    public static boolean canQueryViaPackage(com.android.server.pm.pkg.AndroidPackage querying, com.android.server.pm.pkg.AndroidPackage potentialTarget) {
        return !querying.getQueriesPackages().isEmpty() && querying.getQueriesPackages().contains(potentialTarget.getPackageName());
    }

    public static boolean canQueryAsInstaller(com.android.server.pm.pkg.PackageStateInternal querying, com.android.server.pm.pkg.AndroidPackage potentialTarget) {
        com.android.server.pm.InstallSource installSource = querying.getInstallSource();
        if (potentialTarget.getPackageName().equals(installSource.mInstallerPackageName)) {
            return true;
        }
        return !installSource.mIsInitiatingPackageUninstalled && potentialTarget.getPackageName().equals(installSource.mInitiatingPackageName);
    }

    public static boolean canQueryAsUpdateOwner(com.android.server.pm.pkg.PackageStateInternal querying, com.android.server.pm.pkg.AndroidPackage potentialTarget) {
        com.android.server.pm.InstallSource installSource = querying.getInstallSource();
        if (potentialTarget.getPackageName().equals(installSource.mUpdateOwnerPackageName)) {
            return true;
        }
        return false;
    }

    public static boolean canQueryViaUsesLibrary(com.android.server.pm.pkg.AndroidPackage querying, com.android.server.pm.pkg.AndroidPackage potentialTarget) {
        if (potentialTarget.getLibraryNames().isEmpty()) {
            return false;
        }
        java.util.List<java.lang.String> libNames = potentialTarget.getLibraryNames();
        int size = libNames.size();
        for (int i = 0; i < size; i++) {
            java.lang.String libName = libNames.get(i);
            if (querying.getUsesLibraries().contains(libName) || querying.getUsesOptionalLibraries().contains(libName)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matchesProviders(java.util.Set<java.lang.String> queriesAuthorities, com.android.server.pm.pkg.AndroidPackage potentialTarget) {
        for (int p = com.android.internal.util.ArrayUtils.size(potentialTarget.getProviders()) - 1; p >= 0; p--) {
            com.android.internal.pm.pkg.component.ParsedProvider provider = (com.android.internal.pm.pkg.component.ParsedProvider) potentialTarget.getProviders().get(p);
            if (provider.isExported() && provider.getAuthority() != null) {
                java.util.StringTokenizer authorities = new java.util.StringTokenizer(provider.getAuthority(), ";", false);
                while (authorities.hasMoreElements()) {
                    if (queriesAuthorities.contains(authorities.nextToken())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean matchesPackage(android.content.Intent intent, com.android.server.pm.pkg.AndroidPackage potentialTarget, com.android.server.utils.WatchedArraySet<java.lang.String> protectedBroadcasts) {
        return matchesAnyComponents(intent, potentialTarget.getServices(), null) || matchesAnyComponents(intent, potentialTarget.getActivities(), null) || matchesAnyComponents(intent, potentialTarget.getReceivers(), protectedBroadcasts) || matchesAnyComponents(intent, potentialTarget.getProviders(), null);
    }

    private static boolean matchesAnyComponents(android.content.Intent intent, java.util.List<? extends com.android.internal.pm.pkg.component.ParsedMainComponent> components, com.android.server.utils.WatchedArraySet<java.lang.String> protectedBroadcasts) {
        for (int i = com.android.internal.util.ArrayUtils.size(components) - 1; i >= 0; i--) {
            com.android.internal.pm.pkg.component.ParsedMainComponent component = components.get(i);
            if (component.isExported() && matchesAnyFilter(intent, component, protectedBroadcasts)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matchesAnyFilter(android.content.Intent intent, com.android.internal.pm.pkg.component.ParsedComponent component, com.android.server.utils.WatchedArraySet<java.lang.String> protectedBroadcasts) {
        java.util.List<com.android.internal.pm.pkg.component.ParsedIntentInfo> intents = component.getIntents();
        for (int i = com.android.internal.util.ArrayUtils.size(intents) - 1; i >= 0; i--) {
            android.content.IntentFilter intentFilter = intents.get(i).getIntentFilter();
            if (matchesIntentFilter(intent, intentFilter, protectedBroadcasts)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matchesIntentFilter(android.content.Intent intent, android.content.IntentFilter intentFilter, com.android.server.utils.WatchedArraySet<java.lang.String> protectedBroadcasts) {
        return intentFilter.match(intent.getAction(), intent.getType(), intent.getScheme(), intent.getData(), intent.getCategories(), "AppsFilter", true, protectedBroadcasts != null ? protectedBroadcasts.untrackedStorage() : null) > 0;
    }

    public static final class ParallelComputeComponentVisibility {
        private static final int MAX_THREADS = 4;
        private static final java.util.concurrent.ExecutorService sComputeExecutor;
        private final android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> mExistingSettings;
        private final android.util.ArraySet<java.lang.Integer> mForceQueryable;
        private final com.android.server.utils.WatchedArraySet<java.lang.String> mProtectedBroadcasts;

        static {
            android.util.IAsyncTaskSchedulers schedulers = (android.util.IAsyncTaskSchedulers) system.ext.loader.core.ExtLoader.type(android.util.IAsyncTaskSchedulers.class).create();
            sComputeExecutor = schedulers == null ? null : schedulers.computeScheduler();
        }

        ParallelComputeComponentVisibility(android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> existingSettings, android.util.ArraySet<java.lang.Integer> forceQueryable, com.android.server.utils.WatchedArraySet<java.lang.String> protectedBroadcasts) {
            this.mExistingSettings = existingSettings;
            this.mForceQueryable = forceQueryable;
            this.mProtectedBroadcasts = protectedBroadcasts;
        }

        android.util.SparseSetArray<java.lang.Integer> execute() {
            java.util.concurrent.ExecutorService pool;
            android.util.SparseSetArray<java.lang.Integer> queriesViaComponent = new android.util.SparseSetArray<>();
            if (sComputeExecutor != null) {
                pool = sComputeExecutor;
            } else {
                pool = com.android.internal.util.ConcurrentUtils.newFixedThreadPool(4, com.android.server.pm.AppsFilterUtils.ParallelComputeComponentVisibility.class.getSimpleName(), 0);
            }
            android.os.Trace.traceBegin(262144L, "ParallelComputeComponentVisibility:" + this.mExistingSettings.size());
            try {
                java.util.List<android.util.Pair<com.android.server.pm.pkg.PackageState, java.util.concurrent.Future<android.util.ArraySet<java.lang.Integer>>>> futures = new java.util.ArrayList<>();
                for (int i = this.mExistingSettings.size() - 1; i >= 0; i--) {
                    final com.android.server.pm.pkg.PackageStateInternal setting = this.mExistingSettings.valueAt(i);
                    com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = setting.getPkg();
                    if (pkg != null && !com.android.server.pm.AppsFilterUtils.requestsQueryAllPackages(pkg) && (!pkg.getQueriesIntents().isEmpty() || !pkg.getQueriesProviders().isEmpty())) {
                        futures.add(new android.util.Pair<>(setting, pool.submit(new java.util.concurrent.Callable() { // from class: com.android.server.pm.AppsFilterUtils$ParallelComputeComponentVisibility$$ExternalSyntheticLambda0
                            @Override // java.util.concurrent.Callable
                            public final java.lang.Object call() {
                                return this.f$0.lambda$execute$0(setting);
                            }
                        })));
                    }
                }
                for (int i2 = 0; i2 < futures.size(); i2++) {
                    int appId = ((com.android.server.pm.pkg.PackageState) futures.get(i2).first).getAppId();
                    java.util.concurrent.Future<android.util.ArraySet<java.lang.Integer>> future = (java.util.concurrent.Future) futures.get(i2).second;
                    try {
                        android.util.ArraySet<java.lang.Integer> visibleList = future.get();
                        if (visibleList.size() != 0) {
                            queriesViaComponent.addAll(appId, visibleList);
                        }
                    } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException e) {
                        throw new java.lang.IllegalStateException(e);
                    }
                }
                return queriesViaComponent;
            } finally {
                if (pool != sComputeExecutor) {
                    pool.shutdownNow();
                }
                android.os.Trace.traceEnd(262144L);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: getVisibleListOfQueryViaComponents, reason: merged with bridge method [inline-methods] */
        public android.util.ArraySet<java.lang.Integer> lambda$execute$0(com.android.server.pm.pkg.PackageStateInternal setting) {
            android.os.Trace.traceBegin(262144L, "getVisibleListOfQueryViaComponents");
            try {
                android.util.ArraySet<java.lang.Integer> result = new android.util.ArraySet<>();
                for (int i = this.mExistingSettings.size() - 1; i >= 0; i--) {
                    com.android.server.pm.pkg.PackageStateInternal otherSetting = this.mExistingSettings.valueAt(i);
                    if (setting.getAppId() != otherSetting.getAppId() && otherSetting.getPkg() != null && !this.mForceQueryable.contains(java.lang.Integer.valueOf(otherSetting.getAppId()))) {
                        boolean canQuery = com.android.server.pm.AppsFilterUtils.canQueryViaComponents(setting.getPkg(), otherSetting.getPkg(), this.mProtectedBroadcasts);
                        if (canQuery) {
                            result.add(java.lang.Integer.valueOf(otherSetting.getAppId()));
                        }
                    }
                }
                return result;
            } finally {
                android.os.Trace.traceEnd(262144L);
            }
        }
    }
}
