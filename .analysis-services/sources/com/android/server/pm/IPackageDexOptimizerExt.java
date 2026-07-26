package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IPackageDexOptimizerExt {
    default java.lang.String configDexoptBeforDoing(java.lang.String packageName, java.lang.String compilerFilter, java.lang.String compilationReason) {
        return "speed-profile";
    }

    default void afterDexoptDone(int result, java.lang.String packageName, java.lang.String compilerFilter, long compiletime, int compilationReason) {
    }

    default boolean skipDexoptInDexOptPath(java.lang.String packageName, int compilationReason) {
        return false;
    }

    default boolean configGenerateCompactDex(int compilationReason, boolean generateCompactDex) {
        return generateCompactDex;
    }

    default boolean shouldInterceptDexOptSecondary() {
        return false;
    }
}
