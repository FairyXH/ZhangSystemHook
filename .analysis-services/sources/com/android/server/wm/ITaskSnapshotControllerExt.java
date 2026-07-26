package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ITaskSnapshotControllerExt {

    public interface IStaticExt {
        default boolean reduceTaskSnapshotIfNeed() {
            return false;
        }
    }

    default void getClosingTasks(com.android.server.wm.ActivityRecord activity) {
    }

    default boolean shouldDisableSnapshots() {
        return false;
    }

    default boolean shouldSnapShot(com.android.server.wm.Task task, com.android.server.wm.TaskSnapshotController taskSnapshotController) {
        return true;
    }

    default boolean isSecondScreenOn(com.android.server.policy.WindowManagerPolicy policy) {
        return false;
    }

    default boolean skipSnapShotForSleeping(com.android.server.wm.Task task, com.android.server.wm.TaskSnapshotController controller) {
        return false;
    }

    default boolean canUse16BitFormat(com.android.server.wm.Task task, boolean isIMEScene) {
        return true;
    }
}
