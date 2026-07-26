package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ITaskSnapshotControllerWrapper {
    default com.android.server.wm.ITaskSnapshotControllerExt getExtImpl() {
        return new com.android.server.wm.ITaskSnapshotControllerExt() { // from class: com.android.server.wm.ITaskSnapshotControllerWrapper.1
        };
    }

    default com.android.server.wm.ITaskSnapshotControllerExt.IStaticExt getStaticExtImpl() {
        return new com.android.server.wm.ITaskSnapshotControllerExt.IStaticExt() { // from class: com.android.server.wm.ITaskSnapshotControllerWrapper.2
        };
    }
}
