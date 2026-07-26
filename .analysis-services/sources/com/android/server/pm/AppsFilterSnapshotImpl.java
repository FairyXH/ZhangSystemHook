package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class AppsFilterSnapshotImpl extends com.android.server.pm.AppsFilterBase {
    AppsFilterSnapshotImpl(com.android.server.pm.AppsFilterImpl orig) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = orig.mImplicitlyQueryableLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mImplicitlyQueryable = orig.mImplicitQueryableSnapshot.snapshot();
                this.mRetainedImplicitlyQueryable = orig.mRetainedImplicitlyQueryableSnapshot.snapshot();
            } finally {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        this.mImplicitQueryableSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        this.mRetainedImplicitlyQueryableSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = orig.mQueriesViaPackageLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock2) {
            try {
                this.mQueriesViaPackage = orig.mQueriesViaPackageSnapshot.snapshot();
            } finally {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        this.mQueriesViaPackageSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock3 = orig.mQueriesViaComponentLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock3) {
            try {
                this.mQueriesViaComponent = orig.mQueriesViaComponentSnapshot.snapshot();
            } finally {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        this.mQueriesViaComponentSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock4 = orig.mQueryableViaUsesLibraryLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock4) {
            try {
                this.mQueryableViaUsesLibrary = orig.mQueryableViaUsesLibrarySnapshot.snapshot();
            } finally {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        this.mQueryableViaUsesLibrarySnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock5 = orig.mQueryableViaUsesPermissionLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock5) {
            try {
                this.mQueryableViaUsesPermission = orig.mQueryableViaUsesPermissionSnapshot.snapshot();
            } finally {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        this.mQueryableViaUsesPermissionSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock6 = orig.mForceQueryableLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock6) {
            try {
                this.mForceQueryable = orig.mForceQueryableSnapshot.snapshot();
            } finally {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        this.mForceQueryableSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock7 = orig.mProtectedBroadcastsLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock7) {
            try {
                this.mProtectedBroadcasts = orig.mProtectedBroadcastsSnapshot.snapshot();
            } finally {
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        this.mProtectedBroadcastsSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        this.mQueriesViaComponentRequireRecompute = orig.mQueriesViaComponentRequireRecompute;
        this.mForceQueryableByDevicePackageNames = (java.lang.String[]) java.util.Arrays.copyOf(orig.mForceQueryableByDevicePackageNames, orig.mForceQueryableByDevicePackageNames.length);
        this.mSystemAppsQueryable = orig.mSystemAppsQueryable;
        this.mFeatureConfig = orig.mFeatureConfig.snapshot();
        this.mOverlayReferenceMapper = orig.mOverlayReferenceMapper;
        this.mSystemSigningDetails = orig.mSystemSigningDetails;
        this.mCacheReady = orig.mCacheReady;
        if (this.mCacheReady) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock8 = orig.mCacheLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock8) {
                try {
                    this.mShouldFilterCache = orig.mShouldFilterCacheSnapshot.snapshot();
                } finally {
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        } else {
            this.mShouldFilterCache = new com.android.server.utils.WatchedSparseBooleanMatrix();
        }
        this.mCacheEnabled = orig.mCacheEnabled;
        this.mShouldFilterCacheSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        this.mHandler = null;
    }
}
