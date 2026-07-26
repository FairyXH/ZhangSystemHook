package com.android.server.pm.parsing.library;

/* JADX INFO: loaded from: classes2.dex */
public class ComGoogleAndroidMapsUpdater extends com.android.server.pm.parsing.library.PackageSharedLibraryUpdater {
    private static final java.lang.String LIBRARY_NAME = "com.google.android.maps";

    @Override // com.android.server.pm.parsing.library.PackageSharedLibraryUpdater
    public void updatePackage(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, boolean isSystemApp, boolean isUpdatedSystemApp) {
        parsedPackage.removeUsesLibrary(LIBRARY_NAME);
        parsedPackage.removeUsesOptionalLibrary(LIBRARY_NAME);
    }
}
