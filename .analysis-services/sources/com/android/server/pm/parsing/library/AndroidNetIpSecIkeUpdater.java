package com.android.server.pm.parsing.library;

/* JADX INFO: loaded from: classes2.dex */
public class AndroidNetIpSecIkeUpdater extends com.android.server.pm.parsing.library.PackageSharedLibraryUpdater {
    private static final java.lang.String LIBRARY_NAME = "android.net.ipsec.ike";

    @Override // com.android.server.pm.parsing.library.PackageSharedLibraryUpdater
    public void updatePackage(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, boolean isSystemApp, boolean isUpdatedSystemApp) {
        removeLibrary(parsedPackage, LIBRARY_NAME);
    }
}
