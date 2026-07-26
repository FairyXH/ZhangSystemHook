package com.android.server.pm.pkg;

/* JADX INFO: loaded from: classes2.dex */
public interface PackageUserStateInternal extends com.android.server.pm.pkg.PackageUserState, android.content.pm.pkg.FrameworkPackageUserState {
    public static final com.android.server.pm.pkg.PackageUserStateInternal DEFAULT = new com.android.server.pm.pkg.PackageUserStateDefault();

    com.android.server.utils.WatchedArraySet<java.lang.String> getDisabledComponentsNoCopy();

    com.android.server.utils.WatchedArraySet<java.lang.String> getEnabledComponentsNoCopy();

    android.util.Pair<java.lang.String, java.lang.Integer> getOverrideLabelIconForComponent(android.content.ComponentName componentName);

    com.android.server.utils.WatchedArrayMap<android.content.pm.UserPackage, com.android.server.pm.pkg.SuspendParams> getSuspendParams();
}
