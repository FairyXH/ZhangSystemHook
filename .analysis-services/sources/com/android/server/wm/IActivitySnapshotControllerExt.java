package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IActivitySnapshotControllerExt {

    public interface IStaticExt {
        default boolean reduceTaskSnapshotIfNeed() {
            return false;
        }
    }
}
