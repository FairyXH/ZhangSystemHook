package com.android.server.app;

/* JADX INFO: loaded from: classes.dex */
final class GameTaskInfo {
    final android.content.ComponentName mComponentName;
    final boolean mIsGameTask;
    final int mTaskId;

    GameTaskInfo(int taskId, boolean isGameTask, android.content.ComponentName componentName) {
        this.mTaskId = taskId;
        this.mIsGameTask = isGameTask;
        this.mComponentName = componentName;
    }

    public java.lang.String toString() {
        return "GameTaskInfo{mTaskId=" + this.mTaskId + ", mIsGameTask=" + this.mIsGameTask + ", mComponentName=" + this.mComponentName + '}';
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof com.android.server.app.GameTaskInfo)) {
            return false;
        }
        com.android.server.app.GameTaskInfo that = (com.android.server.app.GameTaskInfo) o;
        return this.mTaskId == that.mTaskId && this.mIsGameTask == that.mIsGameTask && this.mComponentName.equals(that.mComponentName);
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(this.mTaskId), java.lang.Boolean.valueOf(this.mIsGameTask), this.mComponentName);
    }
}
