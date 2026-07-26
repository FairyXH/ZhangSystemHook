package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
public class StorageStatsService extends android.app.usage.IStorageStatsManager.Stub {
    private static final long DEFAULT_QUOTA = android.util.DataUnit.MEBIBYTES.toBytes(64);
    private static final long DELAY_CHECK_STORAGE_DELTA = 30000;
    private static final long DELAY_RECALCULATE_QUOTAS = 36000000;
    private static final java.lang.String PROP_DISABLE_QUOTA = "fw.disable_quota";
    private static final java.lang.String PROP_STORAGE_CRATES = "fw.storage_crates";
    private static final java.lang.String PROP_VERIFY_STORAGE = "fw.verify_storage";
    private static final int STORAGE_STATS_SIZE = 6;
    private static final java.lang.String TAG = "StorageStatsService";
    private final android.app.AppOpsManager mAppOps;
    private final android.content.Context mContext;
    private final com.android.server.usage.StorageStatsService.H mHandler;
    private final com.android.server.pm.Installer mInstaller;
    private final android.content.pm.PackageManager mPackage;
    private final android.os.storage.StorageManager mStorage;
    private final android.os.UserManager mUser;
    private final java.util.concurrent.CopyOnWriteArrayList<android.util.Pair<java.lang.String, com.android.server.usage.StorageStatsManagerLocal.StorageStatsAugmenter>> mStorageStatsAugmenters = new java.util.concurrent.CopyOnWriteArrayList<>();
    private final com.android.server.usage.IStorageStatsServiceExt mExt = (com.android.server.usage.IStorageStatsServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.usage.IStorageStatsServiceExt.class).create();
    private int mStorageThresholdPercentHigh = 20;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.ArrayMap<java.lang.String, android.util.SparseLongArray> mCacheQuotas = new android.util.ArrayMap<>();

    public static class Lifecycle extends com.android.server.SystemService {
        private com.android.server.usage.StorageStatsService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            this.mService = new com.android.server.usage.StorageStatsService(getContext());
            publishBinderService("storagestats", this.mService);
        }
    }

    public StorageStatsService(android.content.Context context) {
        this.mContext = (android.content.Context) com.android.internal.util.Preconditions.checkNotNull(context);
        this.mAppOps = (android.app.AppOpsManager) com.android.internal.util.Preconditions.checkNotNull((android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class));
        this.mUser = (android.os.UserManager) com.android.internal.util.Preconditions.checkNotNull((android.os.UserManager) context.getSystemService(android.os.UserManager.class));
        this.mPackage = (android.content.pm.PackageManager) com.android.internal.util.Preconditions.checkNotNull(context.getPackageManager());
        this.mStorage = (android.os.storage.StorageManager) com.android.internal.util.Preconditions.checkNotNull((android.os.storage.StorageManager) context.getSystemService(android.os.storage.StorageManager.class));
        this.mInstaller = new com.android.server.pm.Installer(context);
        this.mInstaller.onStart();
        invalidateMounts();
        this.mHandler = new com.android.server.usage.StorageStatsService.H(com.android.server.IoThread.get().getLooper());
        this.mHandler.sendEmptyMessage(101);
        this.mStorage.registerListener(new android.os.storage.StorageEventListener() { // from class: com.android.server.usage.StorageStatsService.1
            public void onVolumeStateChanged(android.os.storage.VolumeInfo vol, int oldState, int newState) {
                switch (vol.type) {
                    case 0:
                    case 1:
                    case 2:
                        if (newState == 2) {
                            com.android.server.usage.StorageStatsService.this.invalidateMounts();
                            com.android.server.usage.StorageStatsService.this.mExt.afterInvalidateMountsForMounted(vol, oldState, newState);
                        }
                        break;
                }
            }
        });
        com.android.server.LocalManagerRegistry.addManager(com.android.server.usage.StorageStatsManagerLocal.class, new com.android.server.usage.StorageStatsService.LocalService());
        android.content.IntentFilter prFilter = new android.content.IntentFilter();
        prFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        prFilter.addAction("android.intent.action.PACKAGE_FULLY_REMOVED");
        prFilter.addDataScheme("package");
        this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.usage.StorageStatsService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                java.lang.String action = intent.getAction();
                if ("android.intent.action.PACKAGE_REMOVED".equals(action) || "android.intent.action.PACKAGE_FULLY_REMOVED".equals(action)) {
                    com.android.server.usage.StorageStatsService.this.mHandler.removeMessages(103);
                    com.android.server.usage.StorageStatsService.this.mHandler.sendEmptyMessage(103);
                }
            }
        }, prFilter);
        updateConfig();
        android.provider.DeviceConfig.addOnPropertiesChangedListener("storage_native_boot", this.mContext.getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.usage.StorageStatsService$$ExternalSyntheticLambda3
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.lambda$new$0(properties);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(android.provider.DeviceConfig.Properties properties) {
        updateConfig();
    }

    private void updateConfig() {
        synchronized (this.mLock) {
            this.mStorageThresholdPercentHigh = android.provider.DeviceConfig.getInt("storage_native_boot", "storage_threshold_percent_high", 20);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateMounts() {
        try {
            this.mInstaller.invalidateMounts();
        } catch (com.android.server.pm.Installer.InstallerException e) {
            android.util.Slog.wtf(TAG, "Failed to invalidate mounts", e);
        }
    }

    private void enforceStatsPermission(int callingUid, java.lang.String callingPackage) {
        java.lang.String errMsg = checkStatsPermission(callingUid, callingPackage, true);
        if (errMsg != null) {
            throw new java.lang.SecurityException(errMsg);
        }
    }

    private java.lang.String checkStatsPermission(int callingUid, java.lang.String callingPackage, boolean noteOp) {
        int mode = noteOp ? this.mAppOps.noteOp(43, callingUid, callingPackage) : this.mAppOps.checkOp(43, callingUid, callingPackage);
        switch (mode) {
            case 0:
                return null;
            case 3:
                if (this.mContext.checkCallingOrSelfPermission("android.permission.PACKAGE_USAGE_STATS") == 0) {
                    return null;
                }
                return "Caller does not have android.permission.PACKAGE_USAGE_STATS; callingPackage=" + callingPackage + ", callingUid=" + callingUid;
            default:
                return "Package " + callingPackage + " from UID " + callingUid + " blocked by mode " + mode;
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    public boolean isQuotaSupported(java.lang.String volumeUuid, java.lang.String callingPackage) throws android.os.ParcelableException {
        try {
            return this.mInstaller.isQuotaSupported(volumeUuid);
        } catch (com.android.server.pm.Installer.InstallerException e) {
            throw new android.os.ParcelableException(new java.io.IOException(e.getMessage()));
        }
    }

    public boolean isReservedSupported(java.lang.String volumeUuid, java.lang.String callingPackage) {
        if (volumeUuid == android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL) {
            return android.os.SystemProperties.getBoolean("vold.has_reserved", false) || android.os.Build.IS_ARC;
        }
        return false;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    public long getTotalBytes(java.lang.String volumeUuid, java.lang.String callingPackage) throws android.os.ParcelableException {
        if (volumeUuid == android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL) {
            return android.os.FileUtils.roundStorageSize(this.mStorage.getPrimaryStorageSize());
        }
        android.os.storage.VolumeInfo vol = this.mStorage.findVolumeByUuid(volumeUuid);
        if (vol == null) {
            throw new android.os.ParcelableException(new java.io.IOException("Failed to find storage device for UUID " + volumeUuid));
        }
        return android.os.FileUtils.roundStorageSize(vol.disk.size);
    }

    public long getFreeBytes(java.lang.String volumeUuid, java.lang.String callingPackage) {
        long freeBytes;
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                java.io.File path = this.mStorage.findPathForUuid(volumeUuid);
                if (isQuotaSupported(volumeUuid, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME)) {
                    long cacheTotal = getCacheBytes(volumeUuid, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
                    long cacheReserved = this.mStorage.getStorageCacheBytes(path, 0);
                    long cacheClearable = java.lang.Math.max(0L, cacheTotal - cacheReserved);
                    freeBytes = path.getUsableSpace() + cacheClearable;
                } else {
                    freeBytes = path.getUsableSpace();
                }
                android.util.Slog.d(TAG, "getFreeBytes: " + freeBytes);
                return freeBytes;
            } catch (java.io.FileNotFoundException e) {
                throw new android.os.ParcelableException(e);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    public long getCacheBytes(java.lang.String volumeUuid, java.lang.String callingPackage) throws android.os.ParcelableException {
        enforceStatsPermission(android.os.Binder.getCallingUid(), callingPackage);
        long cacheBytes = 0;
        for (android.content.pm.UserInfo user : this.mUser.getUsers()) {
            android.app.usage.StorageStats stats = queryStatsForUser(volumeUuid, user.id, null);
            cacheBytes += stats.cacheBytes;
        }
        return cacheBytes;
    }

    public long getCacheQuotaBytes(java.lang.String volumeUuid, int uid, java.lang.String callingPackage) {
        enforceStatsPermission(android.os.Binder.getCallingUid(), callingPackage);
        if (this.mCacheQuotas.containsKey(volumeUuid)) {
            android.util.SparseLongArray uidMap = this.mCacheQuotas.get(volumeUuid);
            return uidMap.get(uid, DEFAULT_QUOTA);
        }
        return DEFAULT_QUOTA;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    public android.app.usage.StorageStats queryStatsForPackage(java.lang.String volumeUuid, final java.lang.String packageName, int userId, java.lang.String callingPackage) throws android.os.ParcelableException {
        boolean callerHasStatsPermission;
        java.lang.String[] codePaths;
        final android.content.pm.PackageStats stats;
        if (userId != android.os.UserHandle.getCallingUserId()) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", TAG);
        }
        try {
            android.content.pm.ApplicationInfo appInfo = this.mPackage.getApplicationInfoAsUser(packageName, 8192, userId);
            if (android.os.Binder.getCallingUid() != appInfo.uid) {
                enforceStatsPermission(android.os.Binder.getCallingUid(), callingPackage);
                callerHasStatsPermission = true;
            } else {
                callerHasStatsPermission = checkStatsPermission(android.os.Binder.getCallingUid(), callingPackage, false) == null;
            }
            if (com.android.internal.util.ArrayUtils.defeatNullable(this.mPackage.getPackagesForUid(appInfo.uid)).length == 1) {
                return queryStatsForUid(volumeUuid, appInfo.uid, callingPackage);
            }
            int appId = android.os.UserHandle.getAppId(appInfo.uid);
            java.lang.String[] packageNames = {packageName};
            long[] ceDataInodes = new long[1];
            java.lang.String[] codePaths2 = new java.lang.String[0];
            if ((!appInfo.isSystemApp() || appInfo.isUpdatedSystemApp()) && appInfo.getCodePath() != null) {
                codePaths = (java.lang.String[]) com.android.internal.util.ArrayUtils.appendElement(java.lang.String.class, codePaths2, appInfo.getCodePath());
            } else {
                codePaths = codePaths2;
            }
            android.content.pm.PackageStats stats2 = new android.content.pm.PackageStats(TAG);
            try {
                final boolean callerHasStatsPermission2 = callerHasStatsPermission;
                try {
                    this.mInstaller.getAppSize(volumeUuid, packageNames, userId, 0, appId, ceDataInodes, codePaths, stats2);
                    if (volumeUuid != android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL) {
                        stats = stats2;
                    } else {
                        final android.os.UserHandle userHandle = android.os.UserHandle.of(userId);
                        stats = stats2;
                        forEachStorageStatsAugmenter(new java.util.function.Consumer() { // from class: com.android.server.usage.StorageStatsService$$ExternalSyntheticLambda2
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                ((com.android.server.usage.StorageStatsManagerLocal.StorageStatsAugmenter) obj).augmentStatsForPackageForUser(stats, packageName, userHandle, callerHasStatsPermission2);
                            }
                        }, "queryStatsForPackage");
                    }
                    return translate(stats);
                } catch (com.android.server.pm.Installer.InstallerException e) {
                    e = e;
                    throw new android.os.ParcelableException(new java.io.IOException(e.getMessage()));
                }
            } catch (com.android.server.pm.Installer.InstallerException e2) {
                e = e2;
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e3) {
            throw new android.os.ParcelableException(e3);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    public android.app.usage.StorageStats queryStatsForUid(java.lang.String volumeUuid, final int uid, java.lang.String callingPackage) throws android.os.ParcelableException {
        boolean callerHasStatsPermission;
        android.content.pm.PackageStats manualStats;
        final android.content.pm.PackageStats stats;
        int userId = android.os.UserHandle.getUserId(uid);
        int appId = android.os.UserHandle.getAppId(uid);
        if (userId != android.os.UserHandle.getCallingUserId()) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", TAG);
        }
        if (android.os.Binder.getCallingUid() != uid) {
            enforceStatsPermission(android.os.Binder.getCallingUid(), callingPackage);
            callerHasStatsPermission = true;
        } else {
            callerHasStatsPermission = checkStatsPermission(android.os.Binder.getCallingUid(), callingPackage, false) == null;
        }
        java.lang.String[] packageNames = com.android.internal.util.ArrayUtils.defeatNullable(this.mPackage.getPackagesForUid(uid));
        long[] ceDataInodes = new long[packageNames.length];
        java.lang.String[] codePaths = new java.lang.String[0];
        android.content.pm.PackageStats stats2 = new android.content.pm.PackageStats(TAG);
        java.lang.String[] codePaths2 = codePaths;
        for (int i = 0; i < packageNames.length; i++) {
            try {
                android.content.pm.ApplicationInfo appInfo = this.mPackage.getApplicationInfoAsUser(packageNames[i], 8192, userId);
                if (!appInfo.isSystemApp() || appInfo.isUpdatedSystemApp()) {
                    if (appInfo.getCodePath() != null) {
                        codePaths2 = (java.lang.String[]) com.android.internal.util.ArrayUtils.appendElement(java.lang.String.class, codePaths2, appInfo.getCodePath());
                    }
                    if (android.app.usage.Flags.getAppBytesByDataTypeApi()) {
                        computeAppStatsByDataTypes(stats2, appInfo.sourceDir, packageNames[i]);
                    }
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                throw new android.os.ParcelableException(e);
            }
        }
        try {
            java.lang.String[] codePaths3 = codePaths2;
            final boolean callerHasStatsPermission2 = callerHasStatsPermission;
            try {
                this.mInstaller.getAppSize(volumeUuid, packageNames, userId, getDefaultFlags(), appId, ceDataInodes, codePaths3, stats2);
                if (!android.os.SystemProperties.getBoolean(PROP_VERIFY_STORAGE, false)) {
                    stats = stats2;
                } else {
                    try {
                        manualStats = new android.content.pm.PackageStats(TAG);
                        this.mInstaller.getAppSize(volumeUuid, packageNames, userId, 0, appId, ceDataInodes, codePaths3, manualStats);
                        stats = stats2;
                    } catch (com.android.server.pm.Installer.InstallerException e2) {
                        e = e2;
                    }
                    try {
                        checkEquals("UID " + uid, manualStats, stats);
                    } catch (com.android.server.pm.Installer.InstallerException e3) {
                        e = e3;
                        throw new android.os.ParcelableException(new java.io.IOException(e.getMessage()));
                    }
                }
                if (volumeUuid == android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL) {
                    forEachStorageStatsAugmenter(new java.util.function.Consumer() { // from class: com.android.server.usage.StorageStatsService$$ExternalSyntheticLambda1
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            ((com.android.server.usage.StorageStatsManagerLocal.StorageStatsAugmenter) obj).augmentStatsForUid(stats, uid, callerHasStatsPermission2);
                        }
                    }, "queryStatsForUid");
                }
                return translate(stats);
            } catch (com.android.server.pm.Installer.InstallerException e4) {
                e = e4;
            }
        } catch (com.android.server.pm.Installer.InstallerException e5) {
            e = e5;
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    public android.app.usage.StorageStats queryStatsForUser(java.lang.String volumeUuid, int userId, java.lang.String callingPackage) throws android.os.ParcelableException {
        if (userId != android.os.UserHandle.getCallingUserId()) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", TAG);
        }
        enforceStatsPermission(android.os.Binder.getCallingUid(), callingPackage);
        int[] appIds = getAppIds(userId);
        final android.content.pm.PackageStats stats = new android.content.pm.PackageStats(TAG);
        try {
            this.mInstaller.getUserSize(volumeUuid, userId, getDefaultFlags(), appIds, stats);
            if (android.os.SystemProperties.getBoolean(PROP_VERIFY_STORAGE, false)) {
                android.content.pm.PackageStats manualStats = new android.content.pm.PackageStats(TAG);
                this.mInstaller.getUserSize(volumeUuid, userId, 0, appIds, manualStats);
                checkEquals("User " + userId, manualStats, stats);
            }
            if (volumeUuid == android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL) {
                final android.os.UserHandle userHandle = android.os.UserHandle.of(userId);
                forEachStorageStatsAugmenter(new java.util.function.Consumer() { // from class: com.android.server.usage.StorageStatsService$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.usage.StorageStatsManagerLocal.StorageStatsAugmenter) obj).augmentStatsForUser(stats, userHandle);
                    }
                }, "queryStatsForUser");
            }
            return translate(stats);
        } catch (com.android.server.pm.Installer.InstallerException e) {
            throw new android.os.ParcelableException(new java.io.IOException(e.getMessage()));
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    public android.app.usage.ExternalStorageStats queryExternalStatsForUser(java.lang.String volumeUuid, int userId, java.lang.String callingPackage) throws android.os.ParcelableException {
        if (userId != android.os.UserHandle.getCallingUserId()) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", TAG);
        }
        enforceStatsPermission(android.os.Binder.getCallingUid(), callingPackage);
        int[] appIds = getAppIds(userId);
        try {
            long[] stats = this.mInstaller.getExternalSize(volumeUuid, userId, getDefaultFlags(), appIds);
            if (android.os.SystemProperties.getBoolean(PROP_VERIFY_STORAGE, false)) {
                long[] manualStats = this.mInstaller.getExternalSize(volumeUuid, userId, 0, appIds);
                checkEquals("External " + userId, manualStats, stats);
            }
            android.app.usage.ExternalStorageStats res = new android.app.usage.ExternalStorageStats();
            if (stats != null && stats.length >= 6) {
                res.totalBytes = stats[0];
                res.audioBytes = stats[1];
                res.videoBytes = stats[2];
                res.imageBytes = stats[3];
                res.appBytes = stats[4];
                res.obbBytes = stats[5];
            }
            return res;
        } catch (com.android.server.pm.Installer.InstallerException e) {
            throw new android.os.ParcelableException(new java.io.IOException(e.getMessage()));
        }
    }

    private int[] getAppIds(int userId) {
        int[] appIds = null;
        for (android.content.pm.ApplicationInfo app : this.mPackage.getInstalledApplicationsAsUser(8192, userId)) {
            int appId = android.os.UserHandle.getAppId(app.uid);
            if (!com.android.internal.util.ArrayUtils.contains(appIds, appId)) {
                appIds = com.android.internal.util.ArrayUtils.appendInt(appIds, appId);
            }
        }
        return appIds;
    }

    private static int getDefaultFlags() {
        return android.os.SystemProperties.getBoolean(PROP_DISABLE_QUOTA, false) ? 0 : 4096;
    }

    private static void checkEquals(java.lang.String msg, long[] a, long[] b) {
        for (int i = 0; i < a.length; i++) {
            checkEquals(msg + "[" + i + "]", a[i], b[i]);
        }
    }

    private static void checkEquals(java.lang.String msg, android.content.pm.PackageStats a, android.content.pm.PackageStats b) {
        checkEquals(msg + " codeSize", a.codeSize, b.codeSize);
        checkEquals(msg + " dataSize", a.dataSize, b.dataSize);
        checkEquals(msg + " cacheSize", a.cacheSize, b.cacheSize);
        checkEquals(msg + " externalCodeSize", a.externalCodeSize, b.externalCodeSize);
        checkEquals(msg + " externalDataSize", a.externalDataSize, b.externalDataSize);
        checkEquals(msg + " externalCacheSize", a.externalCacheSize, b.externalCacheSize);
    }

    private static void checkEquals(java.lang.String msg, long expected, long actual) {
        if (expected != actual) {
            android.util.Slog.e(TAG, msg + " expected " + expected + " actual " + actual);
        }
    }

    private static android.app.usage.StorageStats translate(android.content.pm.PackageStats stats) {
        android.app.usage.StorageStats res = new android.app.usage.StorageStats();
        res.codeBytes = stats.codeSize + stats.externalCodeSize;
        res.dataBytes = stats.dataSize + stats.externalDataSize;
        res.cacheBytes = stats.cacheSize + stats.externalCacheSize;
        res.dexoptBytes = stats.dexoptSize;
        res.curProfBytes = stats.curProfSize;
        res.refProfBytes = stats.refProfSize;
        res.apkBytes = stats.apkSize;
        res.libBytes = stats.libSize;
        res.dmBytes = stats.dmSize;
        res.externalCacheBytes = stats.externalCacheSize;
        return res;
    }

    private class H extends android.os.Handler {
        private static final boolean DEBUG = false;
        private static final long MINIMUM_CHANGE_DELTA_PERCENT_HIGH = 5;
        private static final long MINIMUM_CHANGE_DELTA_PERCENT_LOW = 2;
        private static final int MSG_CHECK_STORAGE_DELTA = 100;
        private static final int MSG_LOAD_CACHED_QUOTAS_FROM_FILE = 101;
        private static final int MSG_PACKAGE_REMOVED = 103;
        private static final int MSG_RECALCULATE_QUOTAS = 102;
        private static final int UNSET = -1;
        private long mPreviousBytes;
        private final android.os.StatFs mStats;
        private long mTotalBytes;

        public H(android.os.Looper looper) {
            super(looper);
            this.mStats = new android.os.StatFs(android.os.Environment.getDataDirectory().getAbsolutePath());
            this.mPreviousBytes = this.mStats.getAvailableBytes();
            this.mTotalBytes = this.mStats.getTotalBytes();
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            long bytesDeltaThreshold;
            if (!com.android.server.usage.StorageStatsService.isCacheQuotaCalculationsEnabled(com.android.server.usage.StorageStatsService.this.mContext.getContentResolver())) {
                return;
            }
            switch (msg.what) {
                case 100:
                    this.mStats.restat(android.os.Environment.getDataDirectory().getAbsolutePath());
                    long bytesDelta = java.lang.Math.abs(this.mPreviousBytes - this.mStats.getAvailableBytes());
                    synchronized (com.android.server.usage.StorageStatsService.this.mLock) {
                        if (this.mStats.getAvailableBytes() > (this.mTotalBytes * ((long) com.android.server.usage.StorageStatsService.this.mStorageThresholdPercentHigh)) / 100) {
                            bytesDeltaThreshold = (this.mTotalBytes * 5) / 100;
                        } else {
                            long bytesDeltaThreshold2 = this.mTotalBytes;
                            bytesDeltaThreshold = (bytesDeltaThreshold2 * 2) / 100;
                        }
                        break;
                    }
                    if (bytesDelta > bytesDeltaThreshold) {
                        this.mPreviousBytes = this.mStats.getAvailableBytes();
                        recalculateQuotas(getInitializedStrategy());
                        com.android.server.usage.StorageStatsService.this.notifySignificantDelta();
                    }
                    sendEmptyMessageDelayed(100, 30000L);
                    return;
                case 101:
                    com.android.server.storage.CacheQuotaStrategy strategy = getInitializedStrategy();
                    this.mPreviousBytes = -1L;
                    try {
                        this.mPreviousBytes = strategy.setupQuotasFromFile();
                        break;
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(com.android.server.usage.StorageStatsService.TAG, "An error occurred while reading the cache quota file.", e);
                    } catch (java.lang.IllegalStateException e2) {
                        android.util.Slog.e(com.android.server.usage.StorageStatsService.TAG, "Cache quota XML file is malformed?", e2);
                    }
                    if (this.mPreviousBytes < 0) {
                        this.mStats.restat(android.os.Environment.getDataDirectory().getAbsolutePath());
                        this.mPreviousBytes = this.mStats.getAvailableBytes();
                        recalculateQuotas(strategy);
                    }
                    sendEmptyMessageDelayed(100, 30000L);
                    sendEmptyMessageDelayed(102, com.android.server.usage.StorageStatsService.DELAY_RECALCULATE_QUOTAS);
                    return;
                case 102:
                    recalculateQuotas(getInitializedStrategy());
                    sendEmptyMessageDelayed(102, com.android.server.usage.StorageStatsService.DELAY_RECALCULATE_QUOTAS);
                    return;
                case 103:
                    recalculateQuotas(getInitializedStrategy());
                    return;
                default:
                    return;
            }
        }

        private void recalculateQuotas(com.android.server.storage.CacheQuotaStrategy strategy) {
            strategy.recalculateQuotas();
        }

        private com.android.server.storage.CacheQuotaStrategy getInitializedStrategy() {
            android.app.usage.UsageStatsManagerInternal usageStatsManager = (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
            return new com.android.server.storage.CacheQuotaStrategy(com.android.server.usage.StorageStatsService.this.mContext, usageStatsManager, com.android.server.usage.StorageStatsService.this.mInstaller, com.android.server.usage.StorageStatsService.this.mCacheQuotas);
        }
    }

    static boolean isCacheQuotaCalculationsEnabled(android.content.ContentResolver resolver) {
        return android.provider.Settings.Global.getInt(resolver, "enable_cache_quota_calculation", 1) != 0;
    }

    void notifySignificantDelta() {
        this.mContext.getContentResolver().notifyChange(android.net.Uri.parse("content://com.android.externalstorage.documents/"), (android.database.ContentObserver) null, false);
    }

    private static void checkCratesEnable() {
        boolean enable = android.os.SystemProperties.getBoolean(PROP_STORAGE_CRATES, false);
        if (!enable) {
            throw new java.lang.IllegalStateException("Storage Crate feature is disabled.");
        }
    }

    private void enforceCratesPermission(int callingUid, java.lang.String callingPackage) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_CRATES", callingPackage);
    }

    private static java.util.List<android.os.storage.CrateInfo> convertCrateInfoFrom(android.os.storage.CrateMetadata[] crateMetadatas) {
        android.os.storage.CrateInfo crateInfo;
        if (com.android.internal.util.ArrayUtils.isEmpty(crateMetadatas)) {
            return java.util.Collections.EMPTY_LIST;
        }
        java.util.ArrayList<android.os.storage.CrateInfo> crateInfos = new java.util.ArrayList<>();
        for (android.os.storage.CrateMetadata crateMetadata : crateMetadatas) {
            if (crateMetadata != null && !android.text.TextUtils.isEmpty(crateMetadata.id) && !android.text.TextUtils.isEmpty(crateMetadata.packageName) && (crateInfo = android.os.storage.CrateInfo.copyFrom(crateMetadata.uid, crateMetadata.packageName, crateMetadata.id)) != null) {
                crateInfos.add(crateInfo);
            }
        }
        return crateInfos;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    private android.content.pm.ParceledListSlice<android.os.storage.CrateInfo> getAppCrates(java.lang.String volumeUuid, java.lang.String[] packageNames, int userId) throws android.os.ParcelableException {
        try {
            android.os.storage.CrateMetadata[] crateMetadatas = this.mInstaller.getAppCrates(volumeUuid, packageNames, userId);
            return new android.content.pm.ParceledListSlice<>(convertCrateInfoFrom(crateMetadatas));
        } catch (com.android.server.pm.Installer.InstallerException e) {
            throw new android.os.ParcelableException(new java.io.IOException(e.getMessage()));
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    public android.content.pm.ParceledListSlice<android.os.storage.CrateInfo> queryCratesForPackage(java.lang.String volumeUuid, java.lang.String packageName, int userId, java.lang.String callingPackage) throws android.os.ParcelableException {
        checkCratesEnable();
        if (userId != android.os.UserHandle.getCallingUserId()) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", TAG);
        }
        try {
            android.content.pm.ApplicationInfo appInfo = this.mPackage.getApplicationInfoAsUser(packageName, 8192, userId);
            if (android.os.Binder.getCallingUid() != appInfo.uid) {
                enforceCratesPermission(android.os.Binder.getCallingUid(), callingPackage);
            }
            java.lang.String[] packageNames = {packageName};
            return getAppCrates(volumeUuid, packageNames, userId);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new android.os.ParcelableException(e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    public android.content.pm.ParceledListSlice<android.os.storage.CrateInfo> queryCratesForUid(java.lang.String volumeUuid, int uid, java.lang.String callingPackage) throws android.os.ParcelableException {
        checkCratesEnable();
        int userId = android.os.UserHandle.getUserId(uid);
        if (userId != android.os.UserHandle.getCallingUserId()) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", TAG);
        }
        if (android.os.Binder.getCallingUid() != uid) {
            enforceCratesPermission(android.os.Binder.getCallingUid(), callingPackage);
        }
        java.lang.String[] packageNames = com.android.internal.util.ArrayUtils.defeatNullable(this.mPackage.getPackagesForUid(uid));
        java.lang.String[] validatedPackageNames = new java.lang.String[0];
        for (java.lang.String packageName : packageNames) {
            if (!android.text.TextUtils.isEmpty(packageName)) {
                try {
                    android.content.pm.ApplicationInfo appInfo = this.mPackage.getApplicationInfoAsUser(packageName, 8192, userId);
                    if (appInfo != null) {
                        validatedPackageNames = (java.lang.String[]) com.android.internal.util.ArrayUtils.appendElement(java.lang.String.class, validatedPackageNames, packageName);
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    throw new android.os.ParcelableException(e);
                }
            }
        }
        return getAppCrates(volumeUuid, validatedPackageNames, userId);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    public android.content.pm.ParceledListSlice<android.os.storage.CrateInfo> queryCratesForUser(java.lang.String volumeUuid, int userId, java.lang.String callingPackage) throws android.os.ParcelableException {
        checkCratesEnable();
        if (userId != android.os.UserHandle.getCallingUserId()) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", TAG);
        }
        enforceCratesPermission(android.os.Binder.getCallingUid(), callingPackage);
        try {
            android.os.storage.CrateMetadata[] crateMetadatas = this.mInstaller.getUserCrates(volumeUuid, userId);
            return new android.content.pm.ParceledListSlice<>(convertCrateInfoFrom(crateMetadatas));
        } catch (com.android.server.pm.Installer.InstallerException e) {
            throw new android.os.ParcelableException(new java.io.IOException(e.getMessage()));
        }
    }

    void forEachStorageStatsAugmenter(java.util.function.Consumer<com.android.server.usage.StorageStatsManagerLocal.StorageStatsAugmenter> consumer, java.lang.String queryTag) {
        int count = this.mStorageStatsAugmenters.size();
        for (int i = 0; i < count; i++) {
            android.util.Pair<java.lang.String, com.android.server.usage.StorageStatsManagerLocal.StorageStatsAugmenter> pair = this.mStorageStatsAugmenters.get(i);
            java.lang.String augmenterTag = (java.lang.String) pair.first;
            com.android.server.usage.StorageStatsManagerLocal.StorageStatsAugmenter storageStatsAugmenter = (com.android.server.usage.StorageStatsManagerLocal.StorageStatsAugmenter) pair.second;
            android.os.Trace.traceBegin(524288L, queryTag + ":" + augmenterTag);
            try {
                consumer.accept(storageStatsAugmenter);
                android.os.Trace.traceEnd(524288L);
            } catch (java.lang.Throwable th) {
                android.os.Trace.traceEnd(524288L);
                throw th;
            }
        }
    }

    private class LocalService implements com.android.server.usage.StorageStatsManagerLocal {
        private LocalService() {
        }

        @Override // com.android.server.usage.StorageStatsManagerLocal
        public void registerStorageStatsAugmenter(com.android.server.usage.StorageStatsManagerLocal.StorageStatsAugmenter storageStatsAugmenter, java.lang.String tag) {
            com.android.server.usage.StorageStatsService.this.mStorageStatsAugmenters.add(android.util.Pair.create(tag, storageStatsAugmenter));
        }
    }

    private long getDirBytes(java.io.File dir) {
        if (!dir.isDirectory()) {
            return 0L;
        }
        long size = 0;
        try {
            for (java.io.File file : dir.listFiles()) {
                if (file.isFile()) {
                    size += file.length();
                } else if (file.isDirectory()) {
                    size += getDirBytes(file);
                }
            }
        } catch (java.lang.NullPointerException e) {
            android.util.Slog.w(TAG, "Failed to list directory " + dir.getName());
        }
        return size;
    }

    private long getFileBytesInDir(java.io.File dir, java.lang.String suffix) {
        if (!dir.isDirectory()) {
            return 0L;
        }
        long size = 0;
        try {
            for (java.io.File file : dir.listFiles()) {
                if (file.isFile() && file.getName().endsWith(suffix)) {
                    size += file.length();
                }
            }
        } catch (java.lang.NullPointerException e) {
            android.util.Slog.w(TAG, "Failed to list directory " + dir.getName());
        }
        return size;
    }

    private void computeAppStatsByDataTypes(android.content.pm.PackageStats stats, java.lang.String sourceDirName, java.lang.String packageName) {
        java.io.File srcDir = new java.io.File(sourceDirName);
        if (srcDir.isFile()) {
            sourceDirName = srcDir.getParent();
            srcDir = new java.io.File(sourceDirName);
        }
        stats.apkSize += getFileBytesInDir(srcDir, ".apk");
        stats.dmSize += getFileBytesInDir(srcDir, ".dm");
        stats.libSize += getDirBytes(new java.io.File(sourceDirName + "/lib/"));
        try {
            com.android.server.pm.PackageManagerLocal.FilteredSnapshot snapshot = com.android.server.pm.PackageManagerServiceUtils.getPackageManagerLocal().withFilteredSnapshot();
            try {
                com.android.server.art.model.ArtManagedFileStats artManagedFileStats = com.android.server.pm.DexOptHelper.getArtManagerLocal().getArtManagedFileStats(snapshot, packageName);
                if (snapshot != null) {
                    snapshot.close();
                }
                stats.dexoptSize += artManagedFileStats.getTotalSizeBytesByType(0);
                stats.refProfSize += artManagedFileStats.getTotalSizeBytesByType(1);
                stats.curProfSize += artManagedFileStats.getTotalSizeBytesByType(2);
            } finally {
            }
        } catch (java.lang.IllegalStateException e) {
            if (android.os.Build.MTK_HBT_ON_64BIT_ONLY_CHIP && e.getMessage().contains("Unsupported isa 'arm'")) {
                android.util.Slog.w(TAG, "Dexopt with art service is conflict with hbt_translator");
                return;
            }
            throw e;
        }
    }
}
