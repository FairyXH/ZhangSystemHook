package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class ReconcilePackageUtils {
    private static final boolean ALLOW_NON_PRELOADS_SYSTEM_SHAREDUIDS;

    ReconcilePackageUtils() {
    }

    static {
        ALLOW_NON_PRELOADS_SYSTEM_SHAREDUIDS = android.os.Build.IS_DEBUGGABLE || !com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.restrictNonpreloadsSystemShareduids();
    }

    /* JADX WARN: Removed duplicated region for block: B:173:0x03f7  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x04f2 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.List<com.android.server.pm.ReconciledPackage> reconcilePackages(java.util.List<com.android.server.pm.InstallRequest> r37, java.util.Map<java.lang.String, com.android.server.pm.pkg.AndroidPackage> r38, java.util.Map<java.lang.String, com.android.server.pm.Settings.VersionInfo> r39, com.android.server.pm.SharedLibrariesImpl r40, com.android.server.pm.KeySetManagerService r41, com.android.server.pm.Settings r42, com.android.server.SystemConfig r43) throws com.android.server.pm.ReconcileFailure {
        /*
            Method dump skipped, instruction units count: 1273
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ReconcilePackageUtils.reconcilePackages(java.util.List, java.util.Map, java.util.Map, com.android.server.pm.SharedLibrariesImpl, com.android.server.pm.KeySetManagerService, com.android.server.pm.Settings, com.android.server.SystemConfig):java.util.List");
    }

    public static boolean isCompatSignatureUpdateNeeded(com.android.server.pm.Settings.VersionInfo ver) {
        return ver.databaseVersion < 2;
    }

    public static boolean isRecoverSignatureUpdateNeeded(com.android.server.pm.Settings.VersionInfo ver) {
        return ver.databaseVersion < 3;
    }
}
