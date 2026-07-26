package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class TaskLaunchParamsModifier implements com.android.server.wm.LaunchParamsController.LaunchParamsModifier {
    private static final int BOUNDS_CONFLICT_THRESHOLD = 4;
    private static final int CASCADING_OFFSET_DP = 75;
    private static final boolean DEBUG = false;
    private static final int EPSILON = 2;
    private static final int MINIMAL_STEP = 1;
    private static final int STEP_DENOMINATOR = 16;
    private static final java.lang.String TAG = "ActivityTaskManager";
    private java.lang.StringBuilder mLogBuilder;
    private final com.android.server.wm.ActivityTaskSupervisor mSupervisor;
    private com.android.server.wm.TaskDisplayArea mTmpDisplayArea;
    private final android.graphics.Rect mTmpBounds = new android.graphics.Rect();
    private final android.graphics.Rect mTmpStableBounds = new android.graphics.Rect();
    private final int[] mTmpDirections = new int[2];
    private com.android.server.wm.ITaskLaunchParamsModifierExt mModifierExt = (com.android.server.wm.ITaskLaunchParamsModifierExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ITaskLaunchParamsModifierExt.class).base(this).create();

    TaskLaunchParamsModifier(com.android.server.wm.ActivityTaskSupervisor supervisor) {
        this.mSupervisor = supervisor;
    }

    @Override // com.android.server.wm.LaunchParamsController.LaunchParamsModifier
    public int onCalculate(com.android.server.wm.Task task, android.content.pm.ActivityInfo.WindowLayout layout, com.android.server.wm.ActivityRecord activity, com.android.server.wm.ActivityRecord source, android.app.ActivityOptions options, com.android.server.wm.ActivityStarter.Request request, int phase, com.android.server.wm.LaunchParamsController.LaunchParams currentParams, com.android.server.wm.LaunchParamsController.LaunchParams outParams) {
        initLogBuilder(task, activity);
        int result = calculate(task, layout, activity, source, options, request, phase, currentParams, outParams);
        outputLog();
        return result;
    }

    /* JADX WARN: Removed duplicated region for block: B:109:0x01d3  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0263  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x026c  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0271 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0273  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int calculate(com.android.server.wm.Task r35, android.content.pm.ActivityInfo.WindowLayout r36, com.android.server.wm.ActivityRecord r37, com.android.server.wm.ActivityRecord r38, android.app.ActivityOptions r39, com.android.server.wm.ActivityStarter.Request r40, int r41, com.android.server.wm.LaunchParamsController.LaunchParams r42, com.android.server.wm.LaunchParamsController.LaunchParams r43) {
        /*
            Method dump skipped, instruction units count: 1039
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.TaskLaunchParamsModifier.calculate(com.android.server.wm.Task, android.content.pm.ActivityInfo$WindowLayout, com.android.server.wm.ActivityRecord, com.android.server.wm.ActivityRecord, android.app.ActivityOptions, com.android.server.wm.ActivityStarter$Request, int, com.android.server.wm.LaunchParamsController$LaunchParams, com.android.server.wm.LaunchParamsController$LaunchParams):int");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$calculate$0(int resolvedMode, int activityType, com.android.server.wm.TaskDisplayArea displayArea) {
        com.android.server.wm.Task launchRoot = displayArea.getLaunchRootTask(resolvedMode, activityType, null, null, 0);
        if (launchRoot == null) {
            return false;
        }
        this.mTmpDisplayArea = displayArea;
        return true;
    }

    private boolean shouldUpdateExistingTaskWindowingMode(com.android.server.wm.Task task, int launchMode) {
        return (task == null || task.getRequestedOverrideWindowingMode() == 0 || task.getRequestedOverrideWindowingMode() == 2 || launchMode == task.getRequestedOverrideWindowingMode()) ? false : true;
    }

    private com.android.server.wm.TaskDisplayArea getPreferredLaunchTaskDisplayArea(com.android.server.wm.Task task, android.app.ActivityOptions options, com.android.server.wm.ActivityRecord source, com.android.server.wm.LaunchParamsController.LaunchParams currentParams, com.android.server.wm.ActivityRecord activityRecord, com.android.server.wm.ActivityStarter.Request request) {
        android.window.WindowContainerToken launchTaskDisplayArea;
        com.android.server.wm.TaskDisplayArea taskDisplayArea;
        com.android.server.wm.DisplayContent dc;
        final int launchTaskDisplayAreaFeatureId;
        com.android.server.wm.Task rootTask = null;
        if (options == null) {
            launchTaskDisplayArea = null;
        } else {
            launchTaskDisplayArea = options.getLaunchTaskDisplayArea();
        }
        android.window.WindowContainerToken optionLaunchTaskDisplayAreaToken = launchTaskDisplayArea;
        com.android.server.wm.TaskDisplayArea taskDisplayArea2 = optionLaunchTaskDisplayAreaToken != null ? (com.android.server.wm.TaskDisplayArea) com.android.server.wm.WindowContainer.fromBinder(optionLaunchTaskDisplayAreaToken.asBinder()) : null;
        if (taskDisplayArea2 == null && options != null && (launchTaskDisplayAreaFeatureId = options.getLaunchTaskDisplayAreaFeatureId()) != -1) {
            int launchDisplayId = options.getLaunchDisplayId() == -1 ? 0 : options.getLaunchDisplayId();
            com.android.server.wm.DisplayContent dc2 = this.mSupervisor.mRootWindowContainer.getDisplayContent(launchDisplayId);
            if (dc2 != null) {
                taskDisplayArea2 = (com.android.server.wm.TaskDisplayArea) dc2.getItemFromTaskDisplayAreas(new java.util.function.Function() { // from class: com.android.server.wm.TaskLaunchParamsModifier$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return com.android.server.wm.TaskLaunchParamsModifier.lambda$getPreferredLaunchTaskDisplayArea$1(launchTaskDisplayAreaFeatureId, (com.android.server.wm.TaskDisplayArea) obj);
                    }
                });
            }
        }
        if (taskDisplayArea2 == null) {
            int optionLaunchId = options != null ? options.getLaunchDisplayId() : -1;
            if (optionLaunchId != -1 && (dc = this.mSupervisor.mRootWindowContainer.getDisplayContent(optionLaunchId)) != null) {
                taskDisplayArea2 = dc.getDefaultTaskDisplayArea();
            }
        }
        if (taskDisplayArea2 == null && source != null && source.noDisplay && (taskDisplayArea2 = source.mHandoverTaskDisplayArea) == null) {
            int displayId = source.mHandoverLaunchDisplayId;
            com.android.server.wm.DisplayContent dc3 = this.mSupervisor.mRootWindowContainer.getDisplayContent(displayId);
            if (dc3 != null) {
                taskDisplayArea2 = dc3.getDefaultTaskDisplayArea();
            }
        }
        if (taskDisplayArea2 == null && source != null) {
            com.android.server.wm.TaskDisplayArea sourceDisplayArea = source.getDisplayArea();
            taskDisplayArea2 = sourceDisplayArea;
        }
        com.android.server.wm.TaskDisplayArea taskDisplayArea3 = this.mModifierExt.modifierTaskDisplayAreaIfNeed(activityRecord, options, this.mModifierExt.modifierTaskDisplayAreaIfNeed(this.mSupervisor, taskDisplayArea2, task, source), task, source);
        if (taskDisplayArea3 == null && task != null) {
            rootTask = task.getRootTask();
        }
        if (rootTask == null) {
            taskDisplayArea = taskDisplayArea3;
        } else {
            taskDisplayArea = rootTask.getDisplayArea();
        }
        com.android.server.wm.TaskDisplayArea taskDisplayArea4 = this.mModifierExt.modifierTaskDisplayAreaIfNeed(this.mSupervisor, taskDisplayArea, activityRecord, false, rootTask);
        if (taskDisplayArea4 == null && options != null) {
            int callerDisplayId = options.getCallerDisplayId();
            com.android.server.wm.DisplayContent dc4 = this.mSupervisor.mRootWindowContainer.getDisplayContent(callerDisplayId);
            if (dc4 != null) {
                taskDisplayArea4 = dc4.getDefaultTaskDisplayArea();
            }
        }
        if (taskDisplayArea4 == null && currentParams != null) {
            taskDisplayArea4 = currentParams.mPreferredTaskDisplayArea;
        }
        if (taskDisplayArea4 != null && !this.mSupervisor.mService.mSupportsMultiDisplay && taskDisplayArea4.getDisplayId() != 0) {
            taskDisplayArea4 = this.mSupervisor.mRootWindowContainer.getDefaultTaskDisplayArea();
        }
        if (taskDisplayArea4 != null && activityRecord != null && activityRecord.isActivityTypeHome() && !this.mSupervisor.mRootWindowContainer.canStartHomeOnDisplayArea(activityRecord.info, taskDisplayArea4, false)) {
            taskDisplayArea4 = this.mSupervisor.mRootWindowContainer.getDefaultTaskDisplayArea();
        }
        com.android.server.wm.TaskDisplayArea taskDisplayArea5 = this.mModifierExt.modifierTaskDisplayAreaIfNeed(this.mSupervisor, taskDisplayArea4, activityRecord);
        return taskDisplayArea5 != null ? taskDisplayArea5 : getFallbackDisplayAreaForActivity(activityRecord, request);
    }

    static /* synthetic */ com.android.server.wm.TaskDisplayArea lambda$getPreferredLaunchTaskDisplayArea$1(int launchTaskDisplayAreaFeatureId, com.android.server.wm.TaskDisplayArea tda) {
        if (tda.mFeatureId == launchTaskDisplayAreaFeatureId) {
            return tda;
        }
        return null;
    }

    private com.android.server.wm.TaskDisplayArea getFallbackDisplayAreaForActivity(com.android.server.wm.ActivityRecord activityRecord, com.android.server.wm.ActivityStarter.Request request) {
        com.android.server.wm.WindowProcessController controllerFromRequest;
        com.android.server.wm.TaskDisplayArea displayAreaFromSourceProcess;
        com.android.server.wm.TaskDisplayArea taskDisplayAreaForLaunchingRecord;
        if (activityRecord != null) {
            com.android.server.wm.WindowProcessController controllerFromLaunchingRecord = this.mSupervisor.mService.getProcessController(activityRecord.launchedFromPid, activityRecord.launchedFromUid);
            if (controllerFromLaunchingRecord != null && (taskDisplayAreaForLaunchingRecord = controllerFromLaunchingRecord.getTopActivityDisplayArea()) != null) {
                return taskDisplayAreaForLaunchingRecord;
            }
            com.android.server.wm.WindowProcessController controllerFromProcess = this.mSupervisor.mService.getProcessController(activityRecord.getProcessName(), activityRecord.getUid());
            if (controllerFromProcess != null) {
                com.android.server.wm.TaskDisplayArea displayAreaForRecord = this.mModifierExt.modifierTaskDisplayAreaIfNeed(this.mSupervisor, controllerFromProcess.getTopActivityDisplayArea(), activityRecord, true, (com.android.server.wm.Task) null);
                if (displayAreaForRecord != null) {
                    return displayAreaForRecord;
                }
            }
        }
        if (request != null && (controllerFromRequest = this.mSupervisor.mService.getProcessController(request.realCallingPid, request.realCallingUid)) != null && (displayAreaFromSourceProcess = controllerFromRequest.getTopActivityDisplayArea()) != null) {
            return displayAreaFromSourceProcess;
        }
        com.android.server.wm.TaskDisplayArea defaultTaskDisplayArea = this.mSupervisor.mRootWindowContainer.getDefaultTaskDisplayArea();
        return defaultTaskDisplayArea;
    }

    private boolean canInheritWindowingModeFromSource(com.android.server.wm.DisplayContent display, com.android.server.wm.TaskDisplayArea suggestedDisplayArea, com.android.server.wm.ActivityRecord source) {
        if (source == null || suggestedDisplayArea.inFreeformWindowingMode()) {
            return false;
        }
        int sourceWindowingMode = source.getTask().getWindowingMode();
        if ((sourceWindowingMode != 1 && sourceWindowingMode != 5) || display.getDisplayId() != source.getDisplayId()) {
            return false;
        }
        return true;
    }

    private boolean canCalculateBoundsForFullscreenTask(com.android.server.wm.TaskDisplayArea displayArea, int launchMode) {
        return this.mSupervisor.mService.mSupportsFreeformWindowManagement && ((displayArea.getWindowingMode() == 1 && launchMode == 0) || launchMode == 1);
    }

    private boolean canApplyFreeformWindowPolicy(com.android.server.wm.TaskDisplayArea suggestedDisplayArea, int launchMode) {
        return this.mSupervisor.mService.mSupportsFreeformWindowManagement && ((suggestedDisplayArea.inFreeformWindowingMode() && launchMode == 0) || launchMode == 5);
    }

    private boolean canApplyPipWindowPolicy(int launchMode) {
        return this.mSupervisor.mService.mSupportsPictureInPicture && launchMode == 2;
    }

    private void getLayoutBounds(com.android.server.wm.TaskDisplayArea displayArea, com.android.server.wm.ActivityRecord root, android.content.pm.ActivityInfo.WindowLayout windowLayout, android.graphics.Rect inOutBounds) {
        int verticalGravity = windowLayout.gravity & 112;
        int horizontalGravity = windowLayout.gravity & 7;
        if (!windowLayout.hasSpecifiedSize() && verticalGravity == 0 && horizontalGravity == 0) {
            inOutBounds.setEmpty();
            return;
        }
        android.graphics.Rect stableBounds = this.mTmpStableBounds;
        displayArea.getStableRect(stableBounds);
        if (windowLayout.hasSpecifiedSize()) {
            com.android.server.wm.LaunchParamsUtil.calculateLayoutBounds(stableBounds, windowLayout, inOutBounds, null);
        } else if (inOutBounds.isEmpty()) {
            getTaskBounds(root, displayArea, windowLayout, 5, false, inOutBounds);
        }
        com.android.server.wm.LaunchParamsUtil.applyLayoutGravity(verticalGravity, horizontalGravity, inOutBounds, stableBounds);
    }

    private boolean shouldLaunchUnresizableAppInFreeform(com.android.server.wm.ActivityRecord activity, com.android.server.wm.TaskDisplayArea displayArea, android.app.ActivityOptions options) {
        if ((options != null && options.getLaunchWindowingMode() == 1) || !activity.supportsFreeformInDisplayArea(displayArea) || activity.isResizeable()) {
            return false;
        }
        int displayOrientation = orientationFromBounds(displayArea.getBounds());
        int activityOrientation = resolveOrientation(activity, displayArea, displayArea.getBounds());
        return displayArea.getWindowingMode() == 5 && displayOrientation != activityOrientation;
    }

    private int resolveOrientation(com.android.server.wm.ActivityRecord activity) {
        int orientation = activity.info.screenOrientation;
        switch (orientation) {
            case 0:
            case 6:
            case 8:
            case 11:
                return 0;
            case 1:
            case 7:
            case 9:
            case 12:
                return 1;
            case 2:
            case 3:
            case 4:
            case 10:
            case 13:
            default:
                return -1;
            case 5:
            case 14:
                return 14;
        }
    }

    private void cascadeBounds(android.graphics.Rect srcBounds, com.android.server.wm.TaskDisplayArea displayArea, android.graphics.Rect outBounds) {
        outBounds.set(srcBounds);
        float density = displayArea.getConfiguration().densityDpi / 160.0f;
        int defaultOffset = (int) ((75.0f * density) + 0.5f);
        displayArea.getBounds(this.mTmpBounds);
        int dx = java.lang.Math.min(defaultOffset, java.lang.Math.max(0, this.mTmpBounds.right - srcBounds.right));
        int dy = java.lang.Math.min(defaultOffset, java.lang.Math.max(0, this.mTmpBounds.bottom - srcBounds.bottom));
        outBounds.offset(dx, dy);
    }

    private void getTaskBounds(com.android.server.wm.ActivityRecord root, com.android.server.wm.TaskDisplayArea displayArea, android.content.pm.ActivityInfo.WindowLayout layout, int resolvedMode, boolean hasInitialBounds, android.graphics.Rect inOutBounds) {
        if (resolvedMode != 5 && resolvedMode != 1) {
            return;
        }
        int orientation = resolveOrientation(root, displayArea, inOutBounds);
        if (orientation != 1 && orientation != 0) {
            throw new java.lang.IllegalStateException("Orientation must be one of portrait or landscape, but it's " + android.content.pm.ActivityInfo.screenOrientationToString(orientation));
        }
        displayArea.getStableRect(this.mTmpStableBounds);
        android.util.Size defaultSize = com.android.server.wm.LaunchParamsUtil.getDefaultFreeformSize(root, displayArea, layout, orientation, this.mTmpStableBounds);
        this.mTmpBounds.set(0, 0, defaultSize.getWidth(), defaultSize.getHeight());
        if (hasInitialBounds || sizeMatches(inOutBounds, this.mTmpBounds)) {
            if (orientation != orientationFromBounds(inOutBounds)) {
                com.android.server.wm.LaunchParamsUtil.centerBounds(displayArea, inOutBounds.height(), inOutBounds.width(), inOutBounds);
            }
        } else {
            adjustBoundsToFitInDisplayArea(displayArea, layout, this.mTmpBounds);
            inOutBounds.setEmpty();
            com.android.server.wm.LaunchParamsUtil.centerBounds(displayArea, this.mTmpBounds.width(), this.mTmpBounds.height(), inOutBounds);
        }
        adjustBoundsToAvoidConflictInDisplayArea(displayArea, inOutBounds);
    }

    private int convertOrientationToScreenOrientation(int orientation) {
        switch (orientation) {
            case 1:
                return 1;
            case 2:
                return 0;
            default:
                return -1;
        }
    }

    private int resolveOrientation(com.android.server.wm.ActivityRecord root, com.android.server.wm.TaskDisplayArea displayArea, android.graphics.Rect bounds) {
        int iOrientationFromBounds;
        int orientation = resolveOrientation(root);
        if (orientation == 14) {
            if (bounds.isEmpty()) {
                iOrientationFromBounds = convertOrientationToScreenOrientation(displayArea.getConfiguration().orientation);
            } else {
                iOrientationFromBounds = orientationFromBounds(bounds);
            }
            orientation = iOrientationFromBounds;
        }
        if (orientation == -1) {
            return bounds.isEmpty() ? 1 : orientationFromBounds(bounds);
        }
        return orientation;
    }

    private void adjustBoundsToFitInDisplayArea(com.android.server.wm.TaskDisplayArea displayArea, android.content.pm.ActivityInfo.WindowLayout layout, android.graphics.Rect inOutBounds) {
        int layoutDirection = this.mSupervisor.mRootWindowContainer.getConfiguration().getLayoutDirection();
        com.android.server.wm.LaunchParamsUtil.adjustBoundsToFitInDisplayArea(displayArea, layoutDirection, layout, inOutBounds);
    }

    private void adjustBoundsToAvoidConflictInDisplayArea(com.android.server.wm.TaskDisplayArea displayArea, android.graphics.Rect inOutBounds) {
        final java.util.List<android.graphics.Rect> taskBoundsToCheck = new java.util.ArrayList<>();
        displayArea.forAllRootTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.TaskLaunchParamsModifier$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.TaskLaunchParamsModifier.lambda$adjustBoundsToAvoidConflictInDisplayArea$2(taskBoundsToCheck, (com.android.server.wm.Task) obj);
            }
        }, false);
        adjustBoundsToAvoidConflict(displayArea.getBounds(), taskBoundsToCheck, inOutBounds);
    }

    static /* synthetic */ void lambda$adjustBoundsToAvoidConflictInDisplayArea$2(java.util.List taskBoundsToCheck, com.android.server.wm.Task task) {
        if (!task.inFreeformWindowingMode()) {
            return;
        }
        for (int j = 0; j < task.getChildCount(); j++) {
            taskBoundsToCheck.add(task.getChildAt(j).getBounds());
        }
    }

    void adjustBoundsToAvoidConflict(android.graphics.Rect displayAreaBounds, java.util.List<android.graphics.Rect> taskBoundsToCheck, android.graphics.Rect inOutBounds) {
        if (!displayAreaBounds.contains(inOutBounds) || !boundsConflict(taskBoundsToCheck, inOutBounds)) {
            return;
        }
        calculateCandidateShiftDirections(displayAreaBounds, inOutBounds);
        for (int direction : this.mTmpDirections) {
            if (direction != 0) {
                this.mTmpBounds.set(inOutBounds);
                while (boundsConflict(taskBoundsToCheck, this.mTmpBounds) && displayAreaBounds.contains(this.mTmpBounds)) {
                    shiftBounds(direction, displayAreaBounds, this.mTmpBounds);
                }
                if (!boundsConflict(taskBoundsToCheck, this.mTmpBounds) && displayAreaBounds.contains(this.mTmpBounds)) {
                    inOutBounds.set(this.mTmpBounds);
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void calculateCandidateShiftDirections(android.graphics.Rect availableBounds, android.graphics.Rect initialBounds) {
        for (int i = 0; i < this.mTmpDirections.length; i++) {
            this.mTmpDirections[i] = 0;
        }
        int i2 = availableBounds.left;
        int oneThirdWidth = ((i2 * 2) + availableBounds.right) / 3;
        int twoThirdWidth = (availableBounds.left + (availableBounds.right * 2)) / 3;
        int centerX = initialBounds.centerX();
        if (centerX < oneThirdWidth) {
            this.mTmpDirections[0] = 5;
            return;
        }
        if (centerX > twoThirdWidth) {
            this.mTmpDirections[0] = 3;
            return;
        }
        int oneThirdHeight = ((availableBounds.top * 2) + availableBounds.bottom) / 3;
        int twoThirdHeight = (availableBounds.top + (availableBounds.bottom * 2)) / 3;
        int centerY = initialBounds.centerY();
        if (centerY < oneThirdHeight || centerY > twoThirdHeight) {
            this.mTmpDirections[0] = 5;
            this.mTmpDirections[1] = 3;
        } else {
            this.mTmpDirections[0] = 85;
            this.mTmpDirections[1] = 51;
        }
    }

    private boolean boundsConflict(java.util.List<android.graphics.Rect> taskBoundsToCheck, android.graphics.Rect candidateBounds) {
        java.util.Iterator<android.graphics.Rect> it = taskBoundsToCheck.iterator();
        while (true) {
            if (!it.hasNext()) {
                return false;
            }
            android.graphics.Rect taskBounds = it.next();
            boolean leftClose = java.lang.Math.abs(taskBounds.left - candidateBounds.left) < 4;
            boolean topClose = java.lang.Math.abs(taskBounds.top - candidateBounds.top) < 4;
            boolean rightClose = java.lang.Math.abs(taskBounds.right - candidateBounds.right) < 4;
            boolean bottomClose = java.lang.Math.abs(taskBounds.bottom - candidateBounds.bottom) < 4;
            if ((leftClose && topClose) || ((leftClose && bottomClose) || ((rightClose && topClose) || (rightClose && bottomClose)))) {
                break;
            }
        }
        return true;
    }

    private void shiftBounds(int direction, android.graphics.Rect availableRect, android.graphics.Rect inOutBounds) {
        int horizontalOffset;
        int verticalOffset;
        switch (direction & 7) {
            case 3:
                horizontalOffset = -java.lang.Math.max(1, availableRect.width() / 16);
                break;
            case 4:
            default:
                horizontalOffset = 0;
                break;
            case 5:
                int horizontalOffset2 = availableRect.width();
                horizontalOffset = java.lang.Math.max(1, horizontalOffset2 / 16);
                break;
        }
        switch (direction & 112) {
            case 48:
                verticalOffset = -java.lang.Math.max(1, availableRect.height() / 16);
                break;
            case 80:
                verticalOffset = java.lang.Math.max(1, availableRect.height() / 16);
                break;
            default:
                verticalOffset = 0;
                break;
        }
        inOutBounds.offset(horizontalOffset, verticalOffset);
    }

    private void initLogBuilder(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord activity) {
    }

    private void appendLog(java.lang.String log) {
    }

    private void outputLog() {
    }

    private static int orientationFromBounds(android.graphics.Rect bounds) {
        return bounds.width() > bounds.height() ? 0 : 1;
    }

    private static boolean sizeMatches(android.graphics.Rect left, android.graphics.Rect right) {
        return java.lang.Math.abs(right.width() - left.width()) < 2 && java.lang.Math.abs(right.height() - left.height()) < 2;
    }
}
