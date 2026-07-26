package com.android.server.pm.parsing.library;

/* JADX INFO: loaded from: classes2.dex */
public class PackageBackwardCompatibility extends com.android.server.pm.parsing.library.PackageSharedLibraryUpdater {
    private static final com.android.server.pm.parsing.library.PackageBackwardCompatibility INSTANCE;
    private static final java.lang.String TAG = com.android.server.pm.parsing.library.PackageBackwardCompatibility.class.getSimpleName();
    private final boolean mBootClassPathContainsATB;
    private final com.android.server.pm.parsing.library.PackageSharedLibraryUpdater[] mPackageUpdaters;

    static {
        java.util.List<com.android.server.pm.parsing.library.PackageSharedLibraryUpdater> packageUpdaters = new java.util.ArrayList<>();
        packageUpdaters.add(new com.android.server.pm.parsing.library.AndroidNetIpSecIkeUpdater());
        packageUpdaters.add(new com.android.server.pm.parsing.library.ComGoogleAndroidMapsUpdater());
        packageUpdaters.add(new com.android.server.pm.parsing.library.OrgApacheHttpLegacyUpdater());
        packageUpdaters.add(new com.android.server.pm.parsing.library.AndroidHidlUpdater());
        packageUpdaters.add(new com.android.server.pm.parsing.library.PackageBackwardCompatibility.AndroidTestRunnerSplitUpdater());
        boolean bootClassPathContainsATB = !addUpdaterForAndroidTestBase(packageUpdaters);
        packageUpdaters.add(new com.android.server.pm.parsing.library.ApexSharedLibraryUpdater(com.android.server.SystemConfig.getInstance().getSharedLibraries()));
        com.android.server.pm.parsing.library.PackageSharedLibraryUpdater[] updaterArray = (com.android.server.pm.parsing.library.PackageSharedLibraryUpdater[]) packageUpdaters.toArray(new com.android.server.pm.parsing.library.PackageSharedLibraryUpdater[0]);
        INSTANCE = new com.android.server.pm.parsing.library.PackageBackwardCompatibility(bootClassPathContainsATB, updaterArray);
    }

    private static boolean addUpdaterForAndroidTestBase(java.util.List<com.android.server.pm.parsing.library.PackageSharedLibraryUpdater> packageUpdaters) {
        boolean hasClass = false;
        try {
            hasClass = com.android.internal.pm.pkg.parsing.ParsingPackage.class.getClassLoader().loadClass("android.content.pm.AndroidTestBaseUpdater") != null;
            android.util.Log.i(TAG, "Loaded android.content.pm.AndroidTestBaseUpdater");
        } catch (java.lang.ClassNotFoundException e) {
            android.util.Log.i(TAG, "Could not find android.content.pm.AndroidTestBaseUpdater, ignoring");
        }
        if (hasClass) {
            packageUpdaters.add(new com.android.server.pm.parsing.library.AndroidTestBaseUpdater());
        } else {
            packageUpdaters.add(new com.android.server.pm.parsing.library.PackageBackwardCompatibility.RemoveUnnecessaryAndroidTestBaseLibrary());
        }
        return hasClass;
    }

    public static com.android.server.pm.parsing.library.PackageSharedLibraryUpdater getInstance() {
        return INSTANCE;
    }

    com.android.server.pm.parsing.library.PackageSharedLibraryUpdater[] getPackageUpdaters() {
        return this.mPackageUpdaters;
    }

    private PackageBackwardCompatibility(boolean bootClassPathContainsATB, com.android.server.pm.parsing.library.PackageSharedLibraryUpdater[] packageUpdaters) {
        this.mBootClassPathContainsATB = bootClassPathContainsATB;
        this.mPackageUpdaters = packageUpdaters;
    }

    public static void modifySharedLibraries(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, boolean isSystemApp, boolean isUpdatedSystemApp) {
        INSTANCE.updatePackage(parsedPackage, isSystemApp, isUpdatedSystemApp);
    }

    @Override // com.android.server.pm.parsing.library.PackageSharedLibraryUpdater
    public void updatePackage(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, boolean isSystemApp, boolean isUpdatedSystemApp) {
        for (com.android.server.pm.parsing.library.PackageSharedLibraryUpdater packageUpdater : this.mPackageUpdaters) {
            packageUpdater.updatePackage(parsedPackage, isSystemApp, isUpdatedSystemApp);
        }
    }

    public static boolean bootClassPathContainsATB() {
        return INSTANCE.mBootClassPathContainsATB;
    }

    public static class AndroidTestRunnerSplitUpdater extends com.android.server.pm.parsing.library.PackageSharedLibraryUpdater {
        @Override // com.android.server.pm.parsing.library.PackageSharedLibraryUpdater
        public void updatePackage(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, boolean isSystemApp, boolean isUpdatedSystemApp) {
            prefixImplicitDependency(parsedPackage, "android.test.runner", "android.test.mock");
        }
    }

    public static class RemoveUnnecessaryOrgApacheHttpLegacyLibrary extends com.android.server.pm.parsing.library.PackageSharedLibraryUpdater {
        @Override // com.android.server.pm.parsing.library.PackageSharedLibraryUpdater
        public void updatePackage(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, boolean isSystemApp, boolean isUpdatedSystemApp) {
            removeLibrary(parsedPackage, com.android.server.pm.parsing.library.SharedLibraryNames.ORG_APACHE_HTTP_LEGACY);
        }
    }

    public static class RemoveUnnecessaryAndroidTestBaseLibrary extends com.android.server.pm.parsing.library.PackageSharedLibraryUpdater {
        @Override // com.android.server.pm.parsing.library.PackageSharedLibraryUpdater
        public void updatePackage(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, boolean isSystemApp, boolean isUpdatedSystemApp) {
            removeLibrary(parsedPackage, "android.test.base");
        }
    }
}
