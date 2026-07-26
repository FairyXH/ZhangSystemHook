package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IThreadPriorityBoosterExt {
    default boolean setLockOwnerThreadBoost(int tid, boolean enable) {
        return false;
    }

    default void setEnable(boolean enable) {
    }
}
