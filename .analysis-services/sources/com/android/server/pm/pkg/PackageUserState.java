package com.android.server.pm.pkg;

/* JADX INFO: loaded from: classes2.dex */
@android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
public interface PackageUserState {
    public static final com.android.server.pm.pkg.PackageUserState DEFAULT = com.android.server.pm.pkg.PackageUserStateInternal.DEFAULT;

    boolean dataExists();

    android.content.pm.overlay.OverlayPaths getAllOverlayPaths();

    com.android.server.pm.pkg.ArchiveState getArchiveState();

    long getCeDataInode();

    long getDeDataInode();

    android.util.ArraySet<java.lang.String> getDisabledComponents();

    int getDistractionFlags();

    android.util.ArraySet<java.lang.String> getEnabledComponents();

    int getEnabledState();

    long getFirstInstallTimeMillis();

    java.lang.String getHarmfulAppWarning();

    int getInstallReason();

    java.lang.String getLastDisableAppCaller();

    int getMinAspectRatio();

    int getOplusFreezeState();

    android.content.pm.overlay.OverlayPaths getOverlayPaths();

    java.util.Map<java.lang.String, android.content.pm.overlay.OverlayPaths> getSharedLibraryOverlayPaths();

    java.lang.String getSplashScreenTheme();

    int getUninstallReason();

    boolean ignorePackageDisabledInIsEnabled(int i, long j);

    boolean isComponentDisabled(java.lang.String str);

    boolean isComponentEnabled(java.lang.String str);

    boolean isHidden();

    boolean isInstalled();

    boolean isInstantApp();

    boolean isNotLaunched();

    boolean isQuarantined();

    boolean isStopped();

    boolean isSuspended();

    boolean isVirtualPreload();
}
