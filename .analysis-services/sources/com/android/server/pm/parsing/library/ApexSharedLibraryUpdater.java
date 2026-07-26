package com.android.server.pm.parsing.library;

/* JADX INFO: loaded from: classes2.dex */
public class ApexSharedLibraryUpdater extends com.android.server.pm.parsing.library.PackageSharedLibraryUpdater {
    private final android.util.ArrayMap<java.lang.String, com.android.server.SystemConfig.SharedLibraryEntry> mSharedLibraries;

    public ApexSharedLibraryUpdater(android.util.ArrayMap<java.lang.String, com.android.server.SystemConfig.SharedLibraryEntry> sharedLibraries) {
        this.mSharedLibraries = sharedLibraries;
    }

    @Override // com.android.server.pm.parsing.library.PackageSharedLibraryUpdater
    public void updatePackage(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, boolean isSystemApp, boolean isUpdatedSystemApp) {
        int builtInLibCount = this.mSharedLibraries.size();
        for (int i = 0; i < builtInLibCount; i++) {
            updateSharedLibraryForPackage(this.mSharedLibraries.valueAt(i), parsedPackage);
        }
    }

    private void updateSharedLibraryForPackage(com.android.server.SystemConfig.SharedLibraryEntry entry, com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage) {
        if (entry.onBootclasspathBefore != null && isTargetSdkAtMost(parsedPackage.getTargetSdkVersion(), entry.onBootclasspathBefore) && com.android.modules.utils.build.UnboundedSdkLevel.isAtLeast(entry.onBootclasspathBefore)) {
            prefixRequiredLibrary(parsedPackage, entry.name);
        }
        if (entry.canBeSafelyIgnored) {
            removeLibrary(parsedPackage, entry.name);
        }
    }

    private static boolean isTargetSdkAtMost(int targetSdk, java.lang.String onBcpBefore) {
        return isCodename(onBcpBefore) ? targetSdk < 10000 : targetSdk < java.lang.Integer.parseInt(onBcpBefore);
    }

    private static boolean isCodename(java.lang.String version) {
        if (version.length() == 0) {
            throw new java.lang.IllegalArgumentException();
        }
        return java.lang.Character.isUpperCase(version.charAt(0));
    }
}
