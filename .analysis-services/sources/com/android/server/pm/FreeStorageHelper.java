package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class FreeStorageHelper {
    private static final long DEFAULT_MANDATORY_FSTRIM_INTERVAL = 259200000;
    private static final long FREE_STORAGE_UNUSED_STATIC_SHARED_LIB_MIN_CACHE_PERIOD = java.util.concurrent.TimeUnit.HOURS.toMillis(2);
    private final android.content.Context mContext;
    private final boolean mEnableFreeCacheV2;
    private final com.android.server.pm.PackageManagerServiceInjector mInjector;
    private final com.android.server.pm.PackageManagerService mPm;

    FreeStorageHelper(com.android.server.pm.PackageManagerService pm, com.android.server.pm.PackageManagerServiceInjector injector, android.content.Context context, boolean enableFreeCacheV2) {
        this.mPm = pm;
        this.mInjector = injector;
        this.mContext = context;
        this.mEnableFreeCacheV2 = enableFreeCacheV2;
    }

    FreeStorageHelper(com.android.server.pm.PackageManagerService pm) {
        this(pm, pm.mInjector, pm.mContext, android.os.SystemProperties.getBoolean("fw.free_cache_v2", true));
    }

    /* JADX WARN: Removed duplicated region for block: B:72:0x0118 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0119  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void freeStorage(java.lang.String r22, long r23, int r25) throws java.io.IOException {
        /*
            Method dump skipped, instruction units count: 425
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.FreeStorageHelper.freeStorage(java.lang.String, long, int):void");
    }

    int freeCacheForInstallation(int recommendedInstallLocation, android.content.pm.parsing.PackageLite pkgLite, java.lang.String resolvedPath, java.lang.String mPackageAbiOverride, int installFlags) throws java.lang.Throwable {
        android.os.storage.StorageManager storage = android.os.storage.StorageManager.from(this.mContext);
        long lowThreshold = storage.getStorageLowBytes(android.os.Environment.getDataDirectory());
        long sizeBytes = com.android.server.pm.PackageManagerServiceUtils.calculateInstalledSize(resolvedPath, mPackageAbiOverride);
        if (sizeBytes >= 0) {
            try {
                com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
                try {
                    try {
                        this.mPm.mInstaller.freeCache(null, sizeBytes + lowThreshold, 0);
                        try {
                            android.content.pm.PackageInfoLite pkgInfoLite = com.android.server.pm.PackageManagerServiceUtils.getMinimalPackageInfo(this.mContext, pkgLite, resolvedPath, installFlags, mPackageAbiOverride);
                            if (pkgInfoLite.recommendedInstallLocation == -6) {
                                pkgInfoLite.recommendedInstallLocation = -1;
                            }
                            int i = pkgInfoLite.recommendedInstallLocation;
                            if (installLock != null) {
                                installLock.close();
                            }
                            return i;
                        } catch (java.lang.Throwable th) {
                            th = th;
                            java.lang.Throwable th2 = th;
                            if (installLock == null) {
                                throw th2;
                            }
                            try {
                                installLock.close();
                                throw th2;
                            } catch (java.lang.Throwable th3) {
                                th2.addSuppressed(th3);
                                throw th2;
                            }
                        }
                    } catch (com.android.server.pm.Installer.InstallerException e) {
                        e = e;
                        android.util.Slog.w("PackageManager", "Failed to free cache", e);
                        return recommendedInstallLocation;
                    }
                } catch (java.lang.Throwable th4) {
                    th = th4;
                }
            } catch (com.android.server.pm.Installer.InstallerException e2) {
                e = e2;
            }
        }
        return recommendedInstallLocation;
    }

    void performFstrimIfNeeded() {
        com.android.server.pm.PackageManagerServiceUtils.enforceSystemOrRoot("Only the system can request fstrim");
        try {
            android.os.storage.IStorageManager sm = com.android.internal.content.InstallLocationUtils.getStorageManager();
            if (sm == null) {
                android.util.Slog.e("PackageManager", "storageManager service unavailable!");
                return;
            }
            boolean doTrim = false;
            long interval = android.provider.Settings.Global.getLong(this.mContext.getContentResolver(), "fstrim_mandatory_interval", DEFAULT_MANDATORY_FSTRIM_INTERVAL);
            if (interval > 0) {
                long timeSinceLast = java.lang.System.currentTimeMillis() - sm.lastMaintenance();
                if (timeSinceLast > interval) {
                    doTrim = true;
                    android.util.Slog.w("PackageManager", "No disk maintenance in " + timeSinceLast + "; running immediately");
                }
            }
            if (doTrim) {
                sm.runMaintenance();
            }
        } catch (android.os.RemoteException e) {
        }
    }
}
