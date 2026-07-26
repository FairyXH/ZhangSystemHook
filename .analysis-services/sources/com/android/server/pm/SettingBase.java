package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public abstract class SettingBase implements com.android.server.utils.Watchable, com.android.server.utils.Snappable {
    private int mPkgFlags;
    private int mPkgPrivateFlags;
    private final com.android.server.utils.Watchable mWatchable = new com.android.server.utils.WatchableImpl();

    @java.lang.Deprecated
    protected final com.android.server.pm.permission.LegacyPermissionState mLegacyPermissionsState = new com.android.server.pm.permission.LegacyPermissionState();

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

    public void onChanged() {
        com.android.server.pm.pkg.mutate.PackageStateMutator.onPackageStateChanged();
        dispatchChange(this);
    }

    SettingBase(int pkgFlags, int pkgPrivateFlags) {
        setFlags(pkgFlags);
        setPrivateFlags(pkgPrivateFlags);
    }

    SettingBase(com.android.server.pm.SettingBase orig) {
        if (orig != null) {
            copySettingBase(orig);
        }
    }

    public final void copySettingBase(com.android.server.pm.SettingBase orig) {
        this.mPkgFlags = orig.mPkgFlags;
        this.mPkgPrivateFlags = orig.mPkgPrivateFlags;
        this.mLegacyPermissionsState.copyFrom(orig.mLegacyPermissionsState);
        onChanged();
    }

    @java.lang.Deprecated
    public com.android.server.pm.permission.LegacyPermissionState getLegacyPermissionState() {
        return this.mLegacyPermissionsState;
    }

    public com.android.server.pm.SettingBase setFlags(int pkgFlags) {
        this.mPkgFlags = pkgFlags;
        onChanged();
        return this;
    }

    public com.android.server.pm.SettingBase setPrivateFlags(int pkgPrivateFlags) {
        this.mPkgPrivateFlags = pkgPrivateFlags;
        onChanged();
        return this;
    }

    public int getFlags() {
        return this.mPkgFlags;
    }

    public int getPrivateFlags() {
        return this.mPkgPrivateFlags;
    }
}
