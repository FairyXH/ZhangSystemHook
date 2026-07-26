package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface PackageAbiHelper {
    com.android.server.pm.PackageAbiHelper.NativeLibraryPaths deriveNativeLibraryPaths(com.android.server.pm.pkg.AndroidPackage androidPackage, boolean z, boolean z2, java.io.File file);

    android.util.Pair<com.android.server.pm.PackageAbiHelper.Abis, com.android.server.pm.PackageAbiHelper.NativeLibraryPaths> derivePackageAbi(com.android.server.pm.pkg.AndroidPackage androidPackage, boolean z, boolean z2, java.lang.String str, java.io.File file) throws com.android.server.pm.PackageManagerException;

    java.lang.String getAdjustedAbiForSharedUser(android.util.ArraySet<? extends com.android.server.pm.pkg.PackageStateInternal> arraySet, com.android.server.pm.pkg.AndroidPackage androidPackage);

    com.android.server.pm.PackageAbiHelper.Abis getBundledAppAbis(com.android.server.pm.pkg.AndroidPackage androidPackage);

    public static final class NativeLibraryPaths {
        public final java.lang.String nativeLibraryDir;
        public final java.lang.String nativeLibraryRootDir;
        public final boolean nativeLibraryRootRequiresIsa;
        public final java.lang.String secondaryNativeLibraryDir;

        NativeLibraryPaths(java.lang.String nativeLibraryRootDir, boolean nativeLibraryRootRequiresIsa, java.lang.String nativeLibraryDir, java.lang.String secondaryNativeLibraryDir) {
            this.nativeLibraryRootDir = nativeLibraryRootDir;
            this.nativeLibraryRootRequiresIsa = nativeLibraryRootRequiresIsa;
            this.nativeLibraryDir = nativeLibraryDir;
            this.secondaryNativeLibraryDir = secondaryNativeLibraryDir;
        }

        public void applyTo(com.android.internal.pm.parsing.pkg.ParsedPackage pkg) {
            pkg.setNativeLibraryRootDir(this.nativeLibraryRootDir).setNativeLibraryRootRequiresIsa(this.nativeLibraryRootRequiresIsa).setNativeLibraryDir(this.nativeLibraryDir).setSecondaryNativeLibraryDir(this.secondaryNativeLibraryDir);
        }
    }

    public static final class Abis {
        public final java.lang.String primary;
        public final java.lang.String secondary;

        Abis(java.lang.String primary, java.lang.String secondary) {
            this.primary = primary;
            this.secondary = secondary;
        }

        public void applyTo(com.android.internal.pm.parsing.pkg.ParsedPackage pkg) {
            pkg.setPrimaryCpuAbi(this.primary).setSecondaryCpuAbi(this.secondary);
        }

        public void applyTo(com.android.server.pm.PackageSetting pkgSetting) {
            if (pkgSetting != null) {
                pkgSetting.setPrimaryCpuAbi(this.primary);
                pkgSetting.setSecondaryCpuAbi(this.secondary);
            }
        }
    }
}
