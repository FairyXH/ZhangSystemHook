package com.android.server.pm.pkg.mutate;

/* JADX INFO: loaded from: classes2.dex */
public interface PackageUserStateWrite {
    com.android.server.pm.pkg.mutate.PackageUserStateWrite putSuspendParams(android.content.pm.UserPackage userPackage, com.android.server.pm.pkg.SuspendParams suspendParams);

    com.android.server.pm.pkg.mutate.PackageUserStateWrite removeSuspension(android.content.pm.UserPackage userPackage);

    com.android.server.pm.pkg.mutate.PackageUserStateWrite setComponentLabelIcon(android.content.ComponentName componentName, java.lang.String str, java.lang.Integer num);

    com.android.server.pm.pkg.mutate.PackageUserStateWrite setDistractionFlags(int i);

    com.android.server.pm.pkg.mutate.PackageUserStateWrite setHarmfulAppWarning(java.lang.String str);

    com.android.server.pm.pkg.mutate.PackageUserStateWrite setHidden(boolean z);

    com.android.server.pm.pkg.mutate.PackageUserStateWrite setInstalled(boolean z);

    com.android.server.pm.pkg.mutate.PackageUserStateWrite setMinAspectRatio(int i);

    com.android.server.pm.pkg.mutate.PackageUserStateWrite setNotLaunched(boolean z);

    com.android.server.pm.pkg.mutate.PackageUserStateWrite setOverlayPaths(android.content.pm.overlay.OverlayPaths overlayPaths);

    com.android.server.pm.pkg.mutate.PackageUserStateWrite setOverlayPathsForLibrary(java.lang.String str, android.content.pm.overlay.OverlayPaths overlayPaths);

    com.android.server.pm.pkg.mutate.PackageUserStateWrite setSplashScreenTheme(java.lang.String str);

    com.android.server.pm.pkg.mutate.PackageUserStateWrite setStopped(boolean z);

    com.android.server.pm.pkg.mutate.PackageUserStateWrite setUninstallReason(int i);
}
