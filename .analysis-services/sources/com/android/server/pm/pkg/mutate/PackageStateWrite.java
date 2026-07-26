package com.android.server.pm.pkg.mutate;

/* JADX INFO: loaded from: classes2.dex */
public interface PackageStateWrite {
    void onChanged();

    com.android.server.pm.pkg.mutate.PackageStateWrite setCategoryOverride(int i);

    com.android.server.pm.pkg.mutate.PackageStateWrite setHiddenUntilInstalled(boolean z);

    com.android.server.pm.pkg.mutate.PackageStateWrite setInstaller(java.lang.String str, int i);

    com.android.server.pm.pkg.mutate.PackageStateWrite setLastPackageUsageTime(int i, long j);

    com.android.server.pm.pkg.mutate.PackageStateWrite setLoadingCompletedTime(long j);

    com.android.server.pm.pkg.mutate.PackageStateWrite setLoadingProgress(float f);

    com.android.server.pm.pkg.mutate.PackageStateWrite setMimeGroup(java.lang.String str, android.util.ArraySet<java.lang.String> arraySet);

    com.android.server.pm.pkg.mutate.PackageStateWrite setOverrideSeInfo(java.lang.String str);

    com.android.server.pm.pkg.mutate.PackageStateWrite setRequiredForSystemUser(boolean z);

    com.android.server.pm.pkg.mutate.PackageStateWrite setUpdateAvailable(boolean z);

    com.android.server.pm.pkg.mutate.PackageStateWrite setUpdateOwner(java.lang.String str);

    com.android.server.pm.pkg.mutate.PackageUserStateWrite userState(int i);
}
