package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IPackageManagerServiceUtilsExt {
    default void afterCreateNewSettingInScanPackageOnlyLI(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, com.android.server.pm.PackageSetting pkgSetting, com.android.server.pm.ScanRequest request) {
    }

    default boolean skipSharedUserSigMismatchInReconcilePackage(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage) {
        return false;
    }

    default boolean shouldAllowNonPreloadsSystemSignature(java.lang.String packageName) {
        return false;
    }

    default void addBootEvent(java.lang.String bootEvent) {
    }

    default void waitForTranslatorState(java.util.List<com.android.server.pm.InstallRequest> requests) {
    }
}
