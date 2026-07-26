package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class SnapshotController {
    final com.android.server.wm.ActivitySnapshotController mActivitySnapshotController;
    private com.android.server.wm.ISnapshotControllerExt mExt = (com.android.server.wm.ISnapshotControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ISnapshotControllerExt.class).base(this).create();
    private final com.android.server.wm.SnapshotPersistQueue mSnapshotPersistQueue = new com.android.server.wm.SnapshotPersistQueue();
    final com.android.server.wm.TaskSnapshotController mTaskSnapshotController;

    SnapshotController(com.android.server.wm.WindowManagerService wms) {
        this.mTaskSnapshotController = new com.android.server.wm.TaskSnapshotController(wms, this.mSnapshotPersistQueue);
        this.mActivitySnapshotController = new com.android.server.wm.ActivitySnapshotController(wms, this.mSnapshotPersistQueue);
    }

    void systemReady() {
        this.mSnapshotPersistQueue.systemReady();
    }

    void setPause(boolean paused) {
        this.mSnapshotPersistQueue.setPaused(paused);
    }

    void onAppRemoved(com.android.server.wm.ActivityRecord activity) {
        this.mTaskSnapshotController.onAppRemoved(activity);
        this.mActivitySnapshotController.onAppRemoved(activity);
    }

    void onAppDied(com.android.server.wm.ActivityRecord activity) {
        this.mTaskSnapshotController.onAppDied(activity);
        this.mActivitySnapshotController.onAppDied(activity);
    }

    void notifyAppVisibilityChanged(com.android.server.wm.ActivityRecord appWindowToken, boolean visible) {
        this.mActivitySnapshotController.notifyAppVisibilityChanged(appWindowToken, visible);
    }

    void onTransitionStarting(com.android.server.wm.DisplayContent displayContent) {
        this.mTaskSnapshotController.handleClosingApps(displayContent.mClosingApps);
    }

    void onTransactionReady(int type, java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> changeInfos) {
        boolean isTransitionOpen = isTransitionOpen(type);
        boolean isTransitionClose = isTransitionClose(type);
        if (!isTransitionOpen && !isTransitionClose && type < 1000) {
            return;
        }
        com.android.server.wm.SnapshotController.ActivitiesByTask activityTargets = null;
        for (int i = changeInfos.size() - 1; i >= 0; i--) {
            com.android.server.wm.Transition.ChangeInfo info = changeInfos.get(i);
            if (info.mWindowingMode != 2 && !info.mContainer.isActivityTypeHome()) {
                com.android.server.wm.Task task = info.mContainer.asTask();
                if (task != null && !task.mCreatedByOrganizer && !task.isVisibleRequested()) {
                    this.mTaskSnapshotController.recordSnapshot(task, info);
                }
                if (!isTransitionClose && (info.mContainer.asActivityRecord() != null || info.mContainer.asTaskFragment() != null)) {
                    com.android.server.wm.TaskFragment tf = info.mContainer.asTaskFragment();
                    com.android.server.wm.ActivityRecord ar = tf != null ? tf.getTopMostActivity() : info.mContainer.asActivityRecord();
                    if (ar != null && ar.getTask().isVisibleRequested()) {
                        if (activityTargets == null) {
                            activityTargets = new com.android.server.wm.SnapshotController.ActivitiesByTask();
                        }
                        activityTargets.put(ar);
                    }
                }
            }
        }
        if (activityTargets != null) {
            activityTargets.recordSnapshot(this.mActivitySnapshotController);
        }
    }

    private static class ActivitiesByTask {
        final android.util.ArrayMap<com.android.server.wm.Task, com.android.server.wm.SnapshotController.ActivitiesByTask.OpenCloseActivities> mActivitiesMap;

        private ActivitiesByTask() {
            this.mActivitiesMap = new android.util.ArrayMap<>();
        }

        void put(com.android.server.wm.ActivityRecord ar) {
            com.android.server.wm.SnapshotController.ActivitiesByTask.OpenCloseActivities activities = this.mActivitiesMap.get(ar.getTask());
            if (activities == null) {
                activities = new com.android.server.wm.SnapshotController.ActivitiesByTask.OpenCloseActivities();
                this.mActivitiesMap.put(ar.getTask(), activities);
            }
            activities.add(ar);
        }

        void recordSnapshot(com.android.server.wm.ActivitySnapshotController controller) {
            for (int i = this.mActivitiesMap.size() - 1; i >= 0; i--) {
                com.android.server.wm.SnapshotController.ActivitiesByTask.OpenCloseActivities pair = this.mActivitiesMap.valueAt(i);
                pair.recordSnapshot(controller);
            }
        }

        static class OpenCloseActivities {
            final java.util.ArrayList<com.android.server.wm.ActivityRecord> mOpenActivities = new java.util.ArrayList<>();
            final java.util.ArrayList<com.android.server.wm.ActivityRecord> mCloseActivities = new java.util.ArrayList<>();

            OpenCloseActivities() {
            }

            void add(com.android.server.wm.ActivityRecord ar) {
                if (ar.isVisibleRequested()) {
                    this.mOpenActivities.add(ar);
                } else {
                    this.mCloseActivities.add(ar);
                }
            }

            boolean allOpensOptInOnBackInvoked() {
                if (this.mOpenActivities.isEmpty()) {
                    return false;
                }
                for (int i = this.mOpenActivities.size() - 1; i >= 0; i--) {
                    if (!this.mOpenActivities.get(i).mOptInOnBackInvoked) {
                        return false;
                    }
                }
                return true;
            }

            void recordSnapshot(com.android.server.wm.ActivitySnapshotController controller) {
                if (!allOpensOptInOnBackInvoked() || this.mCloseActivities.isEmpty()) {
                    return;
                }
                controller.recordSnapshot(this.mCloseActivities);
            }
        }
    }

    void onTransitionFinish(com.android.server.wm.Transition finish, int type, java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> changeInfos) {
        boolean isTransitionOpen = isTransitionOpen(type);
        boolean isTransitionClose = isTransitionClose(type);
        if ((!isTransitionOpen && !isTransitionClose && type < 1000) || changeInfos.isEmpty()) {
            return;
        }
        android.os.Trace.traceBegin(32L, "SnapshotController_analysis");
        this.mActivitySnapshotController.beginSnapshotProcess();
        java.util.ArrayList<com.android.server.wm.WindowContainer> windows = new java.util.ArrayList<>();
        for (int i = changeInfos.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer wc = changeInfos.get(i).mContainer;
            if (wc.asTask() != null || wc.asTaskFragment() != null || wc.asActivityRecord() != null) {
                windows.add(wc);
            }
        }
        this.mActivitySnapshotController.handleTransitionFinish(windows);
        this.mActivitySnapshotController.endSnapshotProcess();
        for (int i2 = changeInfos.size() - 1; i2 >= 0; i2--) {
            com.android.server.wm.WindowContainer wc2 = changeInfos.get(i2).mContainer;
            com.android.server.wm.Task task = wc2.asTask();
            if (task != null && wc2.isVisibleRequested() && !isFlexibleTaskAndHasCaption(task) && !this.mExt.shouldSkipRemove(finish, wc2)) {
                android.window.TaskSnapshot snapshot = this.mTaskSnapshotController.getSnapshot(task.mTaskId, task.mUserId, false, false);
                if (snapshot != null) {
                    this.mTaskSnapshotController.removeAndDeleteSnapshot(task.mTaskId, task.mUserId);
                }
            }
        }
        android.os.Trace.traceEnd(32L);
    }

    private static boolean isTransitionOpen(int type) {
        return type == 1 || type == 3;
    }

    private static boolean isTransitionClose(int type) {
        return type == 2 || type == 4;
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        this.mTaskSnapshotController.dump(pw, prefix);
        this.mActivitySnapshotController.dump(pw, prefix);
        this.mSnapshotPersistQueue.dump(pw, prefix);
    }

    private boolean isFlexibleTaskAndHasCaption(com.android.server.wm.Task task) {
        if (task == null || task.getTaskDisplayArea() == null) {
            return false;
        }
        return task.getTaskDisplayArea().mTaskDisplayAreaExt.isFlexibleTask(task);
    }
}
