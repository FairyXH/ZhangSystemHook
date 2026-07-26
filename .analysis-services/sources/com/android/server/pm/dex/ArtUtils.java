package com.android.server.pm.dex;

/* JADX INFO: loaded from: classes2.dex */
public final class ArtUtils {
    private ArtUtils() {
    }

    public static com.android.server.pm.dex.ArtPackageInfo createArtPackageInfo(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.PackageState packageState) {
        return new com.android.server.pm.dex.ArtPackageInfo(pkg.getPackageName(), java.util.Arrays.asList(com.android.server.pm.InstructionSets.getAppDexInstructionSets(packageState.getPrimaryCpuAbi(), packageState.getSecondaryCpuAbi())), com.android.server.pm.parsing.pkg.AndroidPackageUtils.getAllCodePaths(pkg), getOatDir(pkg, packageState));
    }

    private static java.lang.String getOatDir(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.PackageState packageState) {
        if (!com.android.server.pm.parsing.pkg.AndroidPackageUtils.canHaveOatDir(packageState, pkg)) {
            return null;
        }
        java.io.File codePath = new java.io.File(pkg.getPath());
        if (codePath.isDirectory()) {
            return com.android.server.pm.PackageDexOptimizer.getOatDir(codePath).getAbsolutePath();
        }
        return null;
    }
}
