package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class LaunchParamsController {
    private final com.android.server.wm.LaunchParamsPersister mPersister;
    private final com.android.server.wm.ActivityTaskManagerService mService;
    private final java.util.List<com.android.server.wm.LaunchParamsController.LaunchParamsModifier> mModifiers = new java.util.ArrayList();
    private final com.android.server.wm.LaunchParamsController.LaunchParams mTmpParams = new com.android.server.wm.LaunchParamsController.LaunchParams();
    private final com.android.server.wm.LaunchParamsController.LaunchParams mTmpCurrent = new com.android.server.wm.LaunchParamsController.LaunchParams();
    private final com.android.server.wm.LaunchParamsController.LaunchParams mTmpResult = new com.android.server.wm.LaunchParamsController.LaunchParams();

    interface LaunchParamsModifier {
        public static final int PHASE_BOUNDS = 3;
        public static final int PHASE_DISPLAY = 0;
        public static final int PHASE_DISPLAY_AREA = 2;
        public static final int PHASE_WINDOWING_MODE = 1;
        public static final int RESULT_CONTINUE = 2;
        public static final int RESULT_DONE = 1;
        public static final int RESULT_SKIP = 0;

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        public @interface Phase {
        }

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        public @interface Result {
        }

        int onCalculate(com.android.server.wm.Task task, android.content.pm.ActivityInfo.WindowLayout windowLayout, com.android.server.wm.ActivityRecord activityRecord, com.android.server.wm.ActivityRecord activityRecord2, android.app.ActivityOptions activityOptions, com.android.server.wm.ActivityStarter.Request request, int i, com.android.server.wm.LaunchParamsController.LaunchParams launchParams, com.android.server.wm.LaunchParamsController.LaunchParams launchParams2);
    }

    LaunchParamsController(com.android.server.wm.ActivityTaskManagerService service, com.android.server.wm.LaunchParamsPersister persister) {
        this.mService = service;
        this.mPersister = persister;
    }

    void registerDefaultModifiers(com.android.server.wm.ActivityTaskSupervisor supervisor) {
        registerModifier(new com.android.server.wm.TaskLaunchParamsModifier(supervisor));
        registerModifier(new com.android.server.wm.DesktopModeLaunchParamsModifier(this.mService.mContext));
    }

    void calculate(com.android.server.wm.Task task, android.content.pm.ActivityInfo.WindowLayout layout, com.android.server.wm.ActivityRecord activity, com.android.server.wm.ActivityRecord source, android.app.ActivityOptions options, com.android.server.wm.ActivityStarter.Request request, int phase, com.android.server.wm.LaunchParamsController.LaunchParams result) {
        com.android.server.wm.TaskDisplayArea area;
        result.reset();
        if (task != null || activity != null) {
            this.mPersister.getLaunchParams(task, activity, result);
        }
        for (int i = this.mModifiers.size() - 1; i >= 0; i--) {
            this.mTmpCurrent.set(result);
            this.mTmpResult.reset();
            com.android.server.wm.LaunchParamsController.LaunchParamsModifier modifier = this.mModifiers.get(i);
            switch (modifier.onCalculate(task, layout, activity, source, options, request, phase, this.mTmpCurrent, this.mTmpResult)) {
                case 1:
                    result.set(this.mTmpResult);
                    return;
                case 2:
                    result.set(this.mTmpResult);
                    break;
            }
        }
        if (activity != null && activity.requestedVrComponent != null) {
            result.mPreferredTaskDisplayArea = this.mService.mRootWindowContainer.getDefaultTaskDisplayArea();
        } else if (this.mService.mVr2dDisplayId != -1) {
            result.mPreferredTaskDisplayArea = this.mService.mRootWindowContainer.getDisplayContent(this.mService.mVr2dDisplayId).getDefaultTaskDisplayArea();
        }
        if (!com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE && this.mService.getWrapper().getExtImpl().getRemoteTaskManager().isDisplaySwitchDetected() && (area = this.mService.getWrapper().getExtImpl().getRemoteTaskManager().getFinalPreferredTaskDisplayArea()) != null) {
            result.mPreferredTaskDisplayArea = area;
        }
    }

    boolean layoutTask(com.android.server.wm.Task task, android.content.pm.ActivityInfo.WindowLayout layout) {
        return layoutTask(task, layout, null, null, null);
    }

    boolean layoutTask(com.android.server.wm.Task task, android.content.pm.ActivityInfo.WindowLayout layout, com.android.server.wm.ActivityRecord activity, com.android.server.wm.ActivityRecord source, android.app.ActivityOptions options) {
        calculate(task, layout, activity, source, options, null, 3, this.mTmpParams);
        if (this.mTmpParams.isEmpty()) {
            return false;
        }
        this.mService.deferWindowLayout();
        try {
            if (this.mTmpParams.mBounds.isEmpty()) {
                return false;
            }
            if (!task.getRootTask().inMultiWindowMode() && !task.getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])) {
                task.setLastNonFullscreenBounds(this.mTmpParams.mBounds);
                return false;
            }
            task.setBounds(this.mTmpParams.mBounds);
            this.mService.continueWindowLayout();
            return true;
        } finally {
            this.mService.continueWindowLayout();
        }
    }

    void registerModifier(com.android.server.wm.LaunchParamsController.LaunchParamsModifier modifier) {
        if (this.mModifiers.contains(modifier)) {
            return;
        }
        this.mModifiers.add(modifier);
    }

    static class LaunchParams {
        final android.graphics.Rect mBounds = new android.graphics.Rect();
        com.android.server.wm.TaskDisplayArea mPreferredTaskDisplayArea;
        int mWindowingMode;

        LaunchParams() {
        }

        void reset() {
            this.mBounds.setEmpty();
            this.mPreferredTaskDisplayArea = null;
            this.mWindowingMode = 0;
        }

        void set(com.android.server.wm.LaunchParamsController.LaunchParams params) {
            this.mBounds.set(params.mBounds);
            this.mPreferredTaskDisplayArea = params.mPreferredTaskDisplayArea;
            this.mWindowingMode = params.mWindowingMode;
        }

        boolean isEmpty() {
            return this.mBounds.isEmpty() && this.mPreferredTaskDisplayArea == null && this.mWindowingMode == 0;
        }

        boolean hasWindowingMode() {
            return this.mWindowingMode != 0;
        }

        boolean hasPreferredTaskDisplayArea() {
            return this.mPreferredTaskDisplayArea != null;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            com.android.server.wm.LaunchParamsController.LaunchParams that = (com.android.server.wm.LaunchParamsController.LaunchParams) o;
            if (this.mPreferredTaskDisplayArea != that.mPreferredTaskDisplayArea || this.mWindowingMode != that.mWindowingMode) {
                return false;
            }
            if (this.mBounds != null) {
                return this.mBounds.equals(that.mBounds);
            }
            if (that.mBounds == null) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int result = this.mBounds != null ? this.mBounds.hashCode() : 0;
            return (((result * 31) + (this.mPreferredTaskDisplayArea != null ? this.mPreferredTaskDisplayArea.hashCode() : 0)) * 31) + this.mWindowingMode;
        }
    }
}
