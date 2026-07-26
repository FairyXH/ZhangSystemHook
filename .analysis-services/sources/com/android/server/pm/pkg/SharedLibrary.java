package com.android.server.pm.pkg;

/* JADX INFO: loaded from: classes2.dex */
@android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
public interface SharedLibrary {
    java.util.List<java.lang.String> getAllCodePaths();

    android.content.pm.VersionedPackage getDeclaringPackage();

    java.util.List<com.android.server.pm.pkg.SharedLibrary> getDependencies();

    java.util.List<android.content.pm.VersionedPackage> getDependentPackages();

    java.lang.String getName();

    java.lang.String getPackageName();

    java.lang.String getPath();

    int getType();

    long getVersion();

    boolean isNative();
}
