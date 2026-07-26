package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IDisplayPolicySocExt {
    default void hookOnVerticalFling(int duration) {
    }

    default void hookOnHorizontalFling(int duration) {
    }

    default void hookOnScroll(boolean started) {
    }

    default void hookOnDown() {
    }

    default java.lang.String getAppPackageName() {
        return null;
    }

    default boolean isTopAppGame(java.lang.String currentPackage, android.util.BoostFramework BoostType) {
        return false;
    }

    default void loadConfig() {
    }

    default boolean isSupportPerfBoost() {
        return false;
    }
}
