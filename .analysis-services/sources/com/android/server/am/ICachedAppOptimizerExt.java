package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface ICachedAppOptimizerExt {
    default boolean checkFreezeProc(com.android.server.am.ProcessRecord app) {
        return true;
    }

    default boolean checkUnfreezeProc(com.android.server.am.ProcessRecord app) {
        return true;
    }

    default void notifyTrimMemory(com.android.server.am.ProcessRecord app) {
    }
}
