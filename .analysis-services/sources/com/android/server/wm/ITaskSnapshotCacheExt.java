package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ITaskSnapshotCacheExt {
    default com.android.server.wm.ActivityRecord reviseTopActivity(com.android.server.wm.Task task, android.window.TaskSnapshot snapshot) {
        return null;
    }
}
