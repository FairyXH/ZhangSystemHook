package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IRunningTasksExt {
    default com.android.server.wm.Task replaceByMultiSearchIfNeed(com.android.server.wm.Task task, java.util.ArrayList<com.android.server.wm.Task> mTmpSortedSet) {
        return task;
    }
}
