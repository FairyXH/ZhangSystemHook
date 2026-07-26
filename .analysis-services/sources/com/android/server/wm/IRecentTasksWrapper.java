package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IRecentTasksWrapper {
    default java.util.ArrayList<com.android.server.wm.Task> getHiddenTasks() {
        return new java.util.ArrayList<>(0);
    }
}
