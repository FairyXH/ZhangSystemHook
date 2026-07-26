package com.android.server.pm.parsing.library;

/* JADX INFO: loaded from: classes2.dex */
public class OrgApacheHttpLegacyUpdater extends com.android.server.pm.parsing.library.PackageSharedLibraryUpdater {
    private static boolean apkTargetsApiLevelLessThanOrEqualToOMR1(com.android.server.pm.pkg.AndroidPackage pkg) {
        return pkg.getTargetSdkVersion() < 28;
    }

    @Override // com.android.server.pm.parsing.library.PackageSharedLibraryUpdater
    public void updatePackage(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, boolean isSystemApp, boolean isUpdatedSystemApp) {
        if (apkTargetsApiLevelLessThanOrEqualToOMR1(parsedPackage)) {
            prefixRequiredLibrary(parsedPackage, com.android.server.pm.parsing.library.SharedLibraryNames.ORG_APACHE_HTTP_LEGACY);
        }
    }
}
