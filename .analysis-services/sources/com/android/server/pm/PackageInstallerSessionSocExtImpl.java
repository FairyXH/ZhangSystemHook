package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PackageInstallerSessionSocExtImpl implements com.android.server.pm.IPackageInstallerSessionSocExt {
    private com.android.server.pm.PackageInstallerSession mBase;
    private android.util.BoostFramework mPerfBoostInstall = null;
    private boolean mIsPerfLockAcquired = false;
    private final int MAX_INSTALL_DURATION = 20000;

    public PackageInstallerSessionSocExtImpl(java.lang.Object base) {
        this.mBase = (com.android.server.pm.PackageInstallerSession) base;
    }

    @Override // com.android.server.pm.IPackageInstallerSessionSocExt
    public void boostBeforeOpenWrite() {
        if (this.mPerfBoostInstall == null) {
            this.mPerfBoostInstall = new android.util.BoostFramework();
        }
        if (this.mPerfBoostInstall != null && !this.mIsPerfLockAcquired) {
            this.mPerfBoostInstall.perfHint(4232, (java.lang.String) null, 20000, -1);
            this.mIsPerfLockAcquired = true;
        }
    }

    @Override // com.android.server.pm.IPackageInstallerSessionSocExt
    public void boostBeforeCommit() {
        if (this.mIsPerfLockAcquired && this.mPerfBoostInstall != null) {
            this.mPerfBoostInstall.perfLockRelease();
            this.mIsPerfLockAcquired = false;
        }
    }

    @Override // com.android.server.pm.IPackageInstallerSessionSocExt
    public void boostBeforeAbandon() {
        if (this.mIsPerfLockAcquired && this.mPerfBoostInstall != null) {
            this.mPerfBoostInstall.perfLockRelease();
            this.mIsPerfLockAcquired = false;
        }
    }
}
