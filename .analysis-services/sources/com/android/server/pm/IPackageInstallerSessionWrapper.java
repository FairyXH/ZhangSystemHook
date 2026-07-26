package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IPackageInstallerSessionWrapper {
    default void extractNativeLibraries(android.content.pm.parsing.PackageLite packageLite, java.io.File packageDir, java.lang.String abiOverride, boolean inherit) throws com.android.server.pm.PackageManagerException {
    }

    default android.content.pm.parsing.PackageLite getPackageLite() {
        return null;
    }

    default com.android.server.pm.InstallSource getInstallSource() {
        return null;
    }

    default int getFinalStatus() {
        return 0;
    }

    default java.lang.String getFinalMessage() {
        return null;
    }
}
