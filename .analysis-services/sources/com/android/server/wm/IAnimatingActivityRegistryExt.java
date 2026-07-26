package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IAnimatingActivityRegistryExt {
    default boolean shouldDeferAnimatingActivityFinished(com.android.server.wm.ActivityRecord record) {
        return false;
    }

    default void makeRunnableList(java.util.LinkedHashMap<com.android.server.wm.ActivityRecord, java.lang.Runnable> finishedTokens, java.util.ArrayList<java.lang.Runnable> runnableList) {
    }
}
