package com.android.server.pm.pkg;

/* JADX INFO: loaded from: classes2.dex */
class PackageUserStateDefault implements com.android.server.pm.pkg.PackageUserStateInternal {
    PackageUserStateDefault() {
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public int getEnabledState() {
        return 0;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public int getInstallReason() {
        return 0;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public java.util.Map<java.lang.String, android.content.pm.overlay.OverlayPaths> getSharedLibraryOverlayPaths() {
        return java.util.Collections.emptyMap();
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public int getUninstallReason() {
        return 0;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isInstalled() {
        return true;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    /* JADX INFO: renamed from: getDisabledComponents, reason: merged with bridge method [inline-methods] */
    public android.util.ArraySet<java.lang.String> m8021getDisabledComponents() {
        return new android.util.ArraySet<>();
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    /* JADX INFO: renamed from: getEnabledComponents, reason: merged with bridge method [inline-methods] */
    public android.util.ArraySet<java.lang.String> m8022getEnabledComponents() {
        return new android.util.ArraySet<>();
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public long getCeDataInode() {
        return 0L;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public long getDeDataInode() {
        return 0L;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public int getDistractionFlags() {
        return 0;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public java.lang.String getHarmfulAppWarning() {
        return null;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public java.lang.String getLastDisableAppCaller() {
        return null;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public android.content.pm.overlay.OverlayPaths getOverlayPaths() {
        return null;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isHidden() {
        return false;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isInstantApp() {
        return false;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isNotLaunched() {
        return false;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isStopped() {
        return false;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isSuspended() {
        return false;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isVirtualPreload() {
        return false;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isQuarantined() {
        return false;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public java.lang.String getSplashScreenTheme() {
        return null;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public int getMinAspectRatio() {
        return 0;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public long getFirstInstallTimeMillis() {
        return 0L;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isComponentEnabled(java.lang.String componentName) {
        return false;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isComponentDisabled(java.lang.String componentName) {
        return false;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public android.content.pm.overlay.OverlayPaths getAllOverlayPaths() {
        return null;
    }

    @Override // com.android.server.pm.pkg.PackageUserStateInternal
    public com.android.server.utils.WatchedArrayMap<android.content.pm.UserPackage, com.android.server.pm.pkg.SuspendParams> getSuspendParams() {
        return null;
    }

    @Override // com.android.server.pm.pkg.PackageUserStateInternal
    public com.android.server.utils.WatchedArraySet<java.lang.String> getDisabledComponentsNoCopy() {
        return null;
    }

    @Override // com.android.server.pm.pkg.PackageUserStateInternal
    public com.android.server.utils.WatchedArraySet<java.lang.String> getEnabledComponentsNoCopy() {
        return null;
    }

    @Override // com.android.server.pm.pkg.PackageUserStateInternal
    public android.util.Pair<java.lang.String, java.lang.Integer> getOverrideLabelIconForComponent(android.content.ComponentName componentName) {
        return null;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public com.android.server.pm.pkg.ArchiveState getArchiveState() {
        return null;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean dataExists() {
        return true;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean ignorePackageDisabledInIsEnabled(int enabled, long flags) {
        return false;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public int getOplusFreezeState() {
        return 0;
    }
}
