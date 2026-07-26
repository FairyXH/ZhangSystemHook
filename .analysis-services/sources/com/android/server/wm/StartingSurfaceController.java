package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class StartingSurfaceController {
    private static final long ALLOW_COPY_SOLID_COLOR_VIEW = 205907456;
    private static final java.lang.String TAG = "WindowManager";
    private boolean mDeferringAddStartingWindow;
    boolean mInitNewTask;
    boolean mInitProcessRunning;
    boolean mInitTaskSwitch;
    private final com.android.server.wm.WindowManagerService mService;
    private final com.android.server.wm.SplashScreenExceptionList mSplashScreenExceptionsList;
    private final java.util.ArrayList<com.android.server.wm.StartingSurfaceController.DeferringStartingWindowRecord> mDeferringAddStartActivities = new java.util.ArrayList<>();
    private com.android.server.wm.IStartingSurfaceControllerExt mStartingSurfaceControllerExt = (com.android.server.wm.IStartingSurfaceControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IStartingSurfaceControllerExt.class).base(this).create();

    public StartingSurfaceController(com.android.server.wm.WindowManagerService wm) {
        this.mService = wm;
        this.mSplashScreenExceptionsList = new com.android.server.wm.SplashScreenExceptionList(wm.mContext.getMainExecutor());
    }

    com.android.server.wm.StartingSurfaceController.StartingSurface createSplashScreenStartingSurface(com.android.server.wm.ActivityRecord activity, int theme) {
        com.android.server.wm.Task task = activity.getTask();
        com.android.server.wm.TaskOrganizerController controller = this.mService.mAtmService.mTaskOrganizerController;
        if (task == null || !controller.addStartingWindow(task, activity, theme, null)) {
            return null;
        }
        return new com.android.server.wm.StartingSurfaceController.StartingSurface(task, controller.getTaskOrganizer());
    }

    boolean isExceptionApp(java.lang.String packageName, int targetSdk, java.util.function.Supplier<android.content.pm.ApplicationInfo> infoProvider) {
        return this.mSplashScreenExceptionsList.isException(packageName, targetSdk, infoProvider);
    }

    static int makeStartingWindowTypeParameter(boolean newTask, boolean taskSwitch, boolean processRunning, boolean allowTaskSnapshot, boolean activityCreated, boolean isSolidColor, boolean useLegacy, boolean activityDrawn, int startingWindowType, boolean appPrefersIcon, java.lang.String packageName, int userId) {
        int parameter = 0;
        if (newTask) {
            parameter = 0 | 1;
        }
        if (taskSwitch) {
            parameter |= 2;
        }
        if (processRunning) {
            parameter |= 4;
        }
        if (allowTaskSnapshot) {
            parameter |= 8;
        }
        if (activityCreated || startingWindowType == 1) {
            parameter |= 16;
        }
        if (isSolidColor) {
            parameter |= 32;
        }
        if (useLegacy) {
            parameter |= Integer.MIN_VALUE;
        }
        if (activityDrawn) {
            parameter |= 64;
        }
        if (startingWindowType == 2 && android.app.compat.CompatChanges.isChangeEnabled(ALLOW_COPY_SOLID_COLOR_VIEW, packageName, android.os.UserHandle.of(userId))) {
            parameter |= 128;
        }
        if (appPrefersIcon) {
            return parameter | 512;
        }
        return parameter;
    }

    com.android.server.wm.StartingSurfaceController.StartingSurface createTaskSnapshotSurface(com.android.server.wm.ActivityRecord activity, android.window.TaskSnapshot taskSnapshot) {
        com.android.server.wm.Task task = activity.getTask();
        if (task == null) {
            android.util.Slog.w(TAG, "TaskSnapshotSurface.create: Failed to find task for activity=" + activity);
            return null;
        }
        com.android.server.wm.WindowState mainWindow = activity.findMainWindow(false);
        if (mainWindow == null) {
            android.util.Slog.w(TAG, "TaskSnapshotSurface.create: no main window in " + activity);
            if (!this.mStartingSurfaceControllerExt.canIgnoreNoMainWindow(activity)) {
                return null;
            }
        }
        if (activity.mDisplayContent.getRotation() != taskSnapshot.getRotation()) {
            activity.mDisplayContent.handleTopActivityLaunchingInDifferentOrientation(activity, false);
        }
        com.android.server.wm.TaskOrganizerController controller = this.mService.mAtmService.mTaskOrganizerController;
        if (!controller.addStartingWindow(task, activity, 0, taskSnapshot)) {
            return null;
        }
        return new com.android.server.wm.StartingSurfaceController.StartingSurface(task, controller.getTaskOrganizer());
    }

    private static final class DeferringStartingWindowRecord {
        final com.android.server.wm.ActivityRecord mDeferring;
        final com.android.server.wm.ActivityRecord mPrev;
        final com.android.server.wm.ActivityRecord mSource;

        DeferringStartingWindowRecord(com.android.server.wm.ActivityRecord deferring, com.android.server.wm.ActivityRecord prev, com.android.server.wm.ActivityRecord source) {
            this.mDeferring = deferring;
            this.mPrev = prev;
            this.mSource = source;
        }
    }

    void showStartingWindow(com.android.server.wm.ActivityRecord target, com.android.server.wm.ActivityRecord prev, boolean newTask, boolean isTaskSwitch, com.android.server.wm.ActivityRecord source) {
        if (this.mDeferringAddStartingWindow) {
            addDeferringRecord(target, prev, newTask, isTaskSwitch, source);
        } else {
            target.showStartingWindow(prev, newTask, isTaskSwitch, true, source);
        }
    }

    private void addDeferringRecord(com.android.server.wm.ActivityRecord deferring, com.android.server.wm.ActivityRecord prev, boolean newTask, boolean isTaskSwitch, com.android.server.wm.ActivityRecord source) {
        if (this.mDeferringAddStartActivities.isEmpty()) {
            this.mInitProcessRunning = deferring.isProcessRunning();
            this.mInitNewTask = newTask;
            this.mInitTaskSwitch = isTaskSwitch;
        }
        this.mDeferringAddStartActivities.add(new com.android.server.wm.StartingSurfaceController.DeferringStartingWindowRecord(deferring, prev, source));
    }

    private void showStartingWindowFromDeferringActivities(android.app.ActivityOptions topOptions) {
        for (int i = this.mDeferringAddStartActivities.size() - 1; i >= 0; i--) {
            com.android.server.wm.StartingSurfaceController.DeferringStartingWindowRecord next = this.mDeferringAddStartActivities.get(i);
            if (next.mDeferring.getTask() == null) {
                android.util.Slog.e(TAG, "No task exists: " + next.mDeferring.shortComponentName + " parent: " + next.mDeferring.getParent());
            } else {
                next.mDeferring.showStartingWindow(next.mPrev, this.mInitNewTask, this.mInitTaskSwitch, this.mInitProcessRunning, true, next.mSource, topOptions);
                if (next.mDeferring.mStartingData != null) {
                    break;
                }
            }
        }
        this.mDeferringAddStartActivities.clear();
    }

    void beginDeferAddStartingWindow() {
        this.mDeferringAddStartingWindow = true;
    }

    void endDeferAddStartingWindow(android.app.ActivityOptions topOptions) {
        this.mDeferringAddStartingWindow = false;
        showStartingWindowFromDeferringActivities(topOptions);
    }

    public final class StartingSurface {
        private final com.android.server.wm.Task mTask;
        final android.window.ITaskOrganizer mTaskOrganizer;

        StartingSurface(com.android.server.wm.Task task, android.window.ITaskOrganizer taskOrganizer) {
            this.mTask = task;
            this.mTaskOrganizer = taskOrganizer;
        }

        public void remove(boolean animate, boolean hasImeSurface) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.StartingSurfaceController.this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.StartingSurfaceController.this.mService.mAtmService.mTaskOrganizerController.removeStartingWindow(this.mTask, this.mTaskOrganizer, animate, hasImeSurface);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }
    }
}
