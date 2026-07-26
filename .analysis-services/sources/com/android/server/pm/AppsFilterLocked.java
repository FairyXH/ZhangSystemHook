package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
abstract class AppsFilterLocked extends com.android.server.pm.AppsFilterBase {
    protected final com.android.server.pm.PackageManagerTracedLock mForceQueryableLock = new com.android.server.pm.PackageManagerTracedLock();
    protected final com.android.server.pm.PackageManagerTracedLock mQueriesViaPackageLock = new com.android.server.pm.PackageManagerTracedLock();
    protected final com.android.server.pm.PackageManagerTracedLock mQueriesViaComponentLock = new com.android.server.pm.PackageManagerTracedLock();
    protected final com.android.server.pm.PackageManagerTracedLock mImplicitlyQueryableLock = new com.android.server.pm.PackageManagerTracedLock();
    protected final com.android.server.pm.PackageManagerTracedLock mQueryableViaUsesLibraryLock = new com.android.server.pm.PackageManagerTracedLock();
    protected final com.android.server.pm.PackageManagerTracedLock mProtectedBroadcastsLock = new com.android.server.pm.PackageManagerTracedLock();
    protected final com.android.server.pm.PackageManagerTracedLock mQueryableViaUsesPermissionLock = new com.android.server.pm.PackageManagerTracedLock();
    protected final com.android.server.pm.PackageManagerTracedLock mCacheLock = new com.android.server.pm.PackageManagerTracedLock();

    AppsFilterLocked() {
    }

    @Override // com.android.server.pm.AppsFilterBase
    protected boolean isForceQueryable(int appId) {
        boolean zIsForceQueryable;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mForceQueryableLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                zIsForceQueryable = super.isForceQueryable(appId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return zIsForceQueryable;
    }

    @Override // com.android.server.pm.AppsFilterBase
    protected boolean isQueryableViaPackage(int callingAppId, int targetAppId) {
        boolean zIsQueryableViaPackage;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mQueriesViaPackageLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                zIsQueryableViaPackage = super.isQueryableViaPackage(callingAppId, targetAppId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return zIsQueryableViaPackage;
    }

    @Override // com.android.server.pm.AppsFilterBase
    protected boolean isQueryableViaComponent(int callingAppId, int targetAppId) {
        boolean zIsQueryableViaComponent;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mQueriesViaComponentLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                zIsQueryableViaComponent = super.isQueryableViaComponent(callingAppId, targetAppId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return zIsQueryableViaComponent;
    }

    @Override // com.android.server.pm.AppsFilterBase
    protected boolean isImplicitlyQueryable(int callingUid, int targetUid) {
        boolean zIsImplicitlyQueryable;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mImplicitlyQueryableLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                zIsImplicitlyQueryable = super.isImplicitlyQueryable(callingUid, targetUid);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return zIsImplicitlyQueryable;
    }

    @Override // com.android.server.pm.AppsFilterBase
    protected boolean isRetainedImplicitlyQueryable(int callingUid, int targetUid) {
        boolean zIsRetainedImplicitlyQueryable;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mImplicitlyQueryableLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                zIsRetainedImplicitlyQueryable = super.isRetainedImplicitlyQueryable(callingUid, targetUid);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return zIsRetainedImplicitlyQueryable;
    }

    @Override // com.android.server.pm.AppsFilterBase
    protected boolean isQueryableViaUsesLibrary(int callingAppId, int targetAppId) {
        boolean zIsQueryableViaUsesLibrary;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mQueryableViaUsesLibraryLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                zIsQueryableViaUsesLibrary = super.isQueryableViaUsesLibrary(callingAppId, targetAppId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return zIsQueryableViaUsesLibrary;
    }

    @Override // com.android.server.pm.AppsFilterBase
    protected boolean isQueryableViaUsesPermission(int callingAppId, int targetAppId) {
        boolean zIsQueryableViaUsesPermission;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mQueryableViaUsesPermissionLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                zIsQueryableViaUsesPermission = super.isQueryableViaUsesPermission(callingAppId, targetAppId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return zIsQueryableViaUsesPermission;
    }

    @Override // com.android.server.pm.AppsFilterBase
    protected boolean shouldFilterApplicationUsingCache(int callingUid, int appId, int userId) {
        boolean zShouldFilterApplicationUsingCache;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mCacheLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                zShouldFilterApplicationUsingCache = super.shouldFilterApplicationUsingCache(callingUid, appId, userId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return zShouldFilterApplicationUsingCache;
    }

    @Override // com.android.server.pm.AppsFilterBase
    protected void dumpForceQueryable(java.io.PrintWriter pw, java.lang.Integer filteringAppId, com.android.server.pm.AppsFilterBase.ToString<java.lang.Integer> expandPackages) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mForceQueryableLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                super.dumpForceQueryable(pw, filteringAppId, expandPackages);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    @Override // com.android.server.pm.AppsFilterBase
    protected void dumpQueriesViaPackage(java.io.PrintWriter pw, java.lang.Integer filteringAppId, com.android.server.pm.AppsFilterBase.ToString<java.lang.Integer> expandPackages) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mQueriesViaPackageLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                super.dumpQueriesViaPackage(pw, filteringAppId, expandPackages);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    @Override // com.android.server.pm.AppsFilterBase
    protected void dumpQueriesViaComponent(java.io.PrintWriter pw, java.lang.Integer filteringAppId, com.android.server.pm.AppsFilterBase.ToString<java.lang.Integer> expandPackages) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mQueriesViaComponentLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                super.dumpQueriesViaComponent(pw, filteringAppId, expandPackages);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    @Override // com.android.server.pm.AppsFilterBase
    protected void dumpQueriesViaImplicitlyQueryable(java.io.PrintWriter pw, java.lang.Integer filteringAppId, int[] users, com.android.server.pm.AppsFilterBase.ToString<java.lang.Integer> expandPackages) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mImplicitlyQueryableLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                super.dumpQueriesViaImplicitlyQueryable(pw, filteringAppId, users, expandPackages);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    @Override // com.android.server.pm.AppsFilterBase
    protected void dumpQueriesViaUsesLibrary(java.io.PrintWriter pw, java.lang.Integer filteringAppId, com.android.server.pm.AppsFilterBase.ToString<java.lang.Integer> expandPackages) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mQueryableViaUsesLibraryLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                super.dumpQueriesViaUsesLibrary(pw, filteringAppId, expandPackages);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }
}
