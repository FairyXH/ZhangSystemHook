package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ITaskSnapshotPersisterExt {
    default boolean reduceTaskSnapshotIfNeed() {
        return false;
    }
}
