package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ISnapshotControllerExt {
    default boolean shouldSkipRemove(com.android.server.wm.Transition finish, com.android.server.wm.WindowContainer target) {
        return false;
    }
}
