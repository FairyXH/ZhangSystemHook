package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class PackageFreezer implements java.lang.AutoCloseable {
    private final dalvik.system.CloseGuard mCloseGuard;
    private final java.util.concurrent.atomic.AtomicBoolean mClosed;
    private com.android.server.pm.InstallRequest mInstallRequest;
    private final java.lang.String mPackageName;
    private final com.android.server.pm.PackageManagerService mPm;

    PackageFreezer(com.android.server.pm.PackageManagerService pm, com.android.server.pm.InstallRequest request) {
        this.mClosed = new java.util.concurrent.atomic.AtomicBoolean();
        this.mCloseGuard = dalvik.system.CloseGuard.get();
        this.mPm = pm;
        this.mPackageName = null;
        this.mClosed.set(true);
        this.mCloseGuard.open("close");
        this.mInstallRequest = request;
        if (this.mInstallRequest != null) {
            this.mInstallRequest.onFreezeStarted();
        }
    }

    PackageFreezer(java.lang.String packageName, int userId, java.lang.String killReason, com.android.server.pm.PackageManagerService pm, int exitInfoReason, com.android.server.pm.InstallRequest request) {
        this(packageName, userId, killReason, pm, exitInfoReason, request, false);
    }

    PackageFreezer(java.lang.String packageName, int userId, java.lang.String killReason, com.android.server.pm.PackageManagerService pm, int exitInfoReason, com.android.server.pm.InstallRequest request, boolean waitAppKilled) {
        com.android.server.pm.PackageSetting ps;
        this.mClosed = new java.util.concurrent.atomic.AtomicBoolean();
        this.mCloseGuard = dalvik.system.CloseGuard.get();
        this.mPm = pm;
        this.mPackageName = packageName;
        this.mInstallRequest = request;
        if (this.mInstallRequest != null) {
            this.mInstallRequest.onFreezeStarted();
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                int refCounts = this.mPm.mFrozenPackages.getOrDefault(this.mPackageName, 0).intValue() + 1;
                this.mPm.mFrozenPackages.put(this.mPackageName, java.lang.Integer.valueOf(refCounts));
                ps = this.mPm.mSettings.getPackageLPr(this.mPackageName);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        if (ps != null) {
            if (waitAppKilled && com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.waitApplicationKilled()) {
                this.mPm.killApplicationSync(ps.getPackageName(), ps.getAppId(), userId, killReason, exitInfoReason);
            } else {
                this.mPm.killApplication(ps.getPackageName(), ps.getAppId(), userId, killReason, exitInfoReason);
            }
        }
        this.mCloseGuard.open("close");
    }

    protected void finalize() throws java.lang.Throwable {
        try {
            this.mCloseGuard.warnIfOpen();
            close();
        } finally {
            super.finalize();
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mCloseGuard.close();
        if (this.mClosed.compareAndSet(false, true)) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    int refCounts = this.mPm.mFrozenPackages.getOrDefault(this.mPackageName, 0).intValue() - 1;
                    if (refCounts > 0) {
                        this.mPm.mFrozenPackages.put(this.mPackageName, java.lang.Integer.valueOf(refCounts));
                    } else {
                        this.mPm.mFrozenPackages.remove(this.mPackageName);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }
        if (this.mInstallRequest != null) {
            this.mInstallRequest.onFreezeCompleted();
            this.mInstallRequest = null;
        }
    }
}
