package com.android.server.om;

/* JADX INFO: loaded from: classes2.dex */
interface PackageManagerHelper {
    boolean doesTargetDefineOverlayable(java.lang.String str, int i) throws java.io.IOException;

    void enforcePermission(java.lang.String str, java.lang.String str2) throws java.lang.SecurityException;

    java.lang.String getConfigSignaturePackage();

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getNamedActors();

    android.content.om.OverlayableInfo getOverlayableForTarget(java.lang.String str, java.lang.String str2, int i) throws java.io.IOException;

    com.android.server.pm.pkg.PackageState getPackageStateForUser(java.lang.String str, int i);

    java.lang.String[] getPackagesForUid(int i);

    android.util.ArrayMap<java.lang.String, com.android.server.pm.pkg.PackageState> initializeForUser(int i);

    boolean isInstantApp(java.lang.String str, int i);

    boolean signaturesMatching(java.lang.String str, java.lang.String str2, int i);
}
