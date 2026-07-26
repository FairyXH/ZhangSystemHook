package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IPackageAbiHelperExt {

    public interface IStaticExt {
        default java.lang.String reconcileLibraryRootDir(java.lang.String codePath, java.io.File codeFile) {
            return new java.io.File(codeFile, "lib").getAbsolutePath();
        }

        default boolean isRemovableApkDir(java.lang.String codePath) {
            return false;
        }

        default boolean shouldAdjustPrimaryCpuAbi(java.lang.String packageName) {
            return false;
        }
    }
}
