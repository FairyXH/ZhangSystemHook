package com.android.server.pm.parsing.library;

/* JADX INFO: loaded from: classes2.dex */
public class AndroidHidlUpdater extends com.android.server.pm.parsing.library.PackageSharedLibraryUpdater {
    @Override // com.android.server.pm.parsing.library.PackageSharedLibraryUpdater
    public void updatePackage(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, boolean isSystemApp, boolean isUpdatedSystemApp) {
        boolean isLegacy = parsedPackage.getTargetSdkVersion() <= 28;
        if (isLegacy && (isSystemApp || isUpdatedSystemApp)) {
            prefixRequiredLibrary(parsedPackage, "android.hidl.base-V1.0-java");
            prefixRequiredLibrary(parsedPackage, "android.hidl.manager-V1.0-java");
        } else {
            removeLibrary(parsedPackage, "android.hidl.base-V1.0-java");
            removeLibrary(parsedPackage, "android.hidl.manager-V1.0-java");
        }
    }
}
