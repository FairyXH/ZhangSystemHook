package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IBackgroundDexOptServiceExt {
    default java.util.ArrayList<java.lang.String> adjustPkgOrderInOptimizePackages(java.util.List<java.lang.String> pkgs) {
        return new java.util.ArrayList<>(pkgs);
    }

    default void beforeScheduleJob(android.content.Context context, com.android.server.pm.PackageManagerService pm) {
    }

    default void notifyTriggerFastIdle() {
    }

    default boolean isEnableFastIdle() {
        return false;
    }

    default boolean breakAndReturnInPostBootUpdate() {
        return false;
    }

    default void beforeOptInIdleOptimization() {
    }

    default void afterOptInIdleOptimization() {
    }

    default int breakAndReturnInOptimizePackages(boolean isPostBootUpdate) {
        return 0;
    }

    default int adjustDexoptFlagsInOptimizePackage(int dexoptFlags, java.lang.String pkgName) {
        return dexoptFlags;
    }

    default boolean skipOnStartJob() {
        return false;
    }

    default boolean needSkipIdleOptimization() {
        return false;
    }

    default boolean parseResultAfterIdleOptimization(int result) {
        return false;
    }

    default void updateIdleOptimizeRecord(java.lang.String key, java.lang.String value) {
    }
}
