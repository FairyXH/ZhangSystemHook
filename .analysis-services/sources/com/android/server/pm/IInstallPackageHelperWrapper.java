package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IInstallPackageHelperWrapper {
    default com.android.server.pm.pkg.AndroidPackage addForInitLI(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, int parseFlags, int scanFlags, android.os.UserHandle user, com.android.server.pm.ApexManager.ActiveApexInfo activeApexInfo) throws com.android.server.pm.PackageManagerException {
        throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "default impl of addForInitLI in wrapper");
    }
}
