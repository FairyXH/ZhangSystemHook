package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class TaskDisplayArea extends com.android.server.wm.DisplayArea<com.android.server.wm.WindowContainer> {
    private com.android.server.wm.ActivityTaskManagerService mAtmService;
    private int mBackgroundColor;
    private final boolean mCanHostHomeTask;
    private int mColorLayerCounter;
    final boolean mCreatedByOrganizer;
    com.android.server.wm.DisplayContent mDisplayContent;
    com.android.server.wm.Task mLastFocusedRootTask;
    private int mLastLeafTaskToFrontId;
    com.android.server.wm.Task mLaunchAdjacentFlagRootTask;
    private final java.util.ArrayList<com.android.server.wm.TaskDisplayArea.LaunchRootTaskDef> mLaunchRootTasks;
    com.android.server.wm.Task mPreferredTopFocusableRootTask;
    private boolean mRemoved;
    private com.android.server.wm.Task mRootHomeTask;
    private com.android.server.wm.Task mRootPinnedTask;
    private java.util.ArrayList<com.android.server.wm.TaskDisplayArea.OnRootTaskOrderChangedListener> mRootTaskOrderChangedCallbacks;
    private com.android.server.wm.RootWindowContainer mRootWindowContainer;
    com.android.server.wm.ITaskDisplayAreaExt mTaskDisplayAreaExt;
    private final android.content.res.Configuration mTempConfiguration;
    private final java.util.ArrayList<com.android.server.wm.WindowContainer> mTmpAlwaysOnTopChildren;
    private final java.util.ArrayList<com.android.server.wm.WindowContainer> mTmpHomeChildren;
    private final android.util.IntArray mTmpNeedsZBoostIndexes;
    private final java.util.ArrayList<com.android.server.wm.WindowContainer> mTmpNormalChildren;
    private java.util.ArrayList<com.android.server.wm.Task> mTmpTasks;

    interface OnRootTaskOrderChangedListener {
        void onRootTaskOrderChanged(com.android.server.wm.Task task);
    }

    private static class LaunchRootTaskDef {
        int[] activityTypes;
        com.android.server.wm.Task task;
        int[] windowingModes;

        private LaunchRootTaskDef() {
        }

        boolean contains(int windowingMode, int activityType) {
            return com.android.internal.util.ArrayUtils.contains(this.windowingModes, windowingMode) && com.android.internal.util.ArrayUtils.contains(this.activityTypes, activityType);
        }
    }

    TaskDisplayArea(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.WindowManagerService service, java.lang.String name, int displayAreaFeature) {
        this(displayContent, service, name, displayAreaFeature, false, true);
    }

    TaskDisplayArea(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.WindowManagerService service, java.lang.String name, int displayAreaFeature, boolean createdByOrganizer) {
        this(displayContent, service, name, displayAreaFeature, createdByOrganizer, true);
    }

    TaskDisplayArea(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.WindowManagerService service, java.lang.String name, int displayAreaFeature, boolean createdByOrganizer, boolean canHostHomeTask) {
        super(service, com.android.server.wm.DisplayArea.Type.ANY, name, displayAreaFeature);
        this.mBackgroundColor = 0;
        this.mColorLayerCounter = 0;
        this.mTmpAlwaysOnTopChildren = new java.util.ArrayList<>();
        this.mTmpNormalChildren = new java.util.ArrayList<>();
        this.mTmpHomeChildren = new java.util.ArrayList<>();
        this.mTmpNeedsZBoostIndexes = new android.util.IntArray();
        this.mTmpTasks = new java.util.ArrayList<>();
        this.mLaunchRootTasks = new java.util.ArrayList<>();
        this.mRootTaskOrderChangedCallbacks = new java.util.ArrayList<>();
        this.mTempConfiguration = new android.content.res.Configuration();
        this.mDisplayContent = displayContent;
        this.mRootWindowContainer = service.mRoot;
        this.mAtmService = service.mAtmService;
        this.mCreatedByOrganizer = createdByOrganizer;
        this.mCanHostHomeTask = canHostHomeTask;
        this.mTaskDisplayAreaExt = (com.android.server.wm.ITaskDisplayAreaExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ITaskDisplayAreaExt.class).base(this).create();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public com.android.server.wm.Task getRootTask(final int windowingMode, final int activityType) {
        if (activityType == 2) {
            return this.mRootHomeTask;
        }
        if (windowingMode == 2) {
            return this.mRootPinnedTask;
        }
        return getRootTask(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.TaskDisplayArea.lambda$getRootTask$0(activityType, windowingMode, (com.android.server.wm.Task) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$getRootTask$0(int activityType, int windowingMode, com.android.server.wm.Task rootTask) {
        if (activityType == 0 && windowingMode == rootTask.getWindowingMode()) {
            return true;
        }
        return rootTask.isCompatible(windowingMode, activityType);
    }

    com.android.server.wm.Task getTopRootTask() {
        return getRootTask(alwaysTruePredicate());
    }

    com.android.server.wm.Task getRootHomeTask() {
        return this.mRootHomeTask;
    }

    com.android.server.wm.Task getRootPinnedTask() {
        return this.mRootPinnedTask;
    }

    java.util.ArrayList<com.android.server.wm.Task> getVisibleTasks() {
        final java.util.ArrayList<com.android.server.wm.Task> visibleTasks = new java.util.ArrayList<>();
        forAllTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.TaskDisplayArea.lambda$getVisibleTasks$1(visibleTasks, (com.android.server.wm.Task) obj);
            }
        });
        return visibleTasks;
    }

    static /* synthetic */ void lambda$getVisibleTasks$1(java.util.ArrayList visibleTasks, com.android.server.wm.Task task) {
        if (task.isLeafTask() && task.isVisible()) {
            visibleTasks.add(task);
        }
    }

    void onRootTaskWindowingModeChanged(com.android.server.wm.Task rootTask) {
        removeRootTaskReferenceIfNeeded(rootTask);
        addRootTaskReferenceIfNeeded(rootTask);
        if (rootTask == this.mRootPinnedTask && getTopRootTask() != rootTask) {
            positionChildAt(Integer.MAX_VALUE, rootTask, false);
        }
    }

    void addRootTaskReferenceIfNeeded(com.android.server.wm.Task rootTask) {
        if (rootTask.isActivityTypeHome()) {
            if (this.mRootHomeTask != null) {
                if (!rootTask.isDescendantOf(this.mRootHomeTask)) {
                    throw new java.lang.IllegalArgumentException("addRootTaskReferenceIfNeeded: root home task=" + this.mRootHomeTask + " already exist on display=" + this + " rootTask=" + rootTask);
                }
            } else {
                this.mRootHomeTask = rootTask;
            }
        } else if (this.mTaskDisplayAreaExt.isMultiSearchActivityType(rootTask.getActivityType())) {
            this.mTaskDisplayAreaExt.setMultiSearchTask(rootTask);
        }
        if (!rootTask.isRootTask()) {
            return;
        }
        int windowingMode = rootTask.getWindowingMode();
        if (windowingMode == 2) {
            if (this.mRootPinnedTask != null) {
                throw new java.lang.IllegalArgumentException("addRootTaskReferenceIfNeeded: root pinned task=" + this.mRootPinnedTask + " already exist on display=" + this + " rootTask=" + rootTask);
            }
            this.mRootPinnedTask = rootTask;
        }
    }

    void removeRootTaskReferenceIfNeeded(com.android.server.wm.Task rootTask) {
        if (rootTask == this.mRootHomeTask) {
            this.mRootHomeTask = null;
        } else if (rootTask == this.mRootPinnedTask) {
            this.mRootPinnedTask = null;
        } else if (this.mTaskDisplayAreaExt.isMultiSearchTask(rootTask)) {
            this.mTaskDisplayAreaExt.setMultiSearchTask(null);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void setInitialSurfaceControlProperties(android.view.SurfaceControl.Builder b) {
        b.setEffectLayer();
        super.setInitialSurfaceControlProperties(b);
    }

    @Override // com.android.server.wm.WindowContainer
    void addChild(com.android.server.wm.WindowContainer child, int position) {
        if (child.asTaskDisplayArea() != null) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_ROOT_TASK) {
                android.util.Slog.d("WindowManager", "Set TaskDisplayArea=" + child + " on taskDisplayArea=" + this);
            }
            super.addChild(child, position);
        } else {
            if (child.asTask() != null) {
                addChildTask(child.asTask(), position);
                return;
            }
            throw new java.lang.IllegalArgumentException("TaskDisplayArea can only add Task and TaskDisplayArea, but found " + child);
        }
    }

    private void addChildTask(com.android.server.wm.Task task, int position) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_ROOT_TASK) {
            android.util.Slog.d("WindowManager", "Set task=" + task + " on taskDisplayArea=" + this);
        }
        addRootTaskReferenceIfNeeded(task);
        super.addChild(task, findPositionForRootTask(position, task, true));
        if (this.mPreferredTopFocusableRootTask != null && task.isFocusable() && this.mPreferredTopFocusableRootTask.compareTo((com.android.server.wm.WindowContainer) task) < 0) {
            this.mPreferredTopFocusableRootTask = null;
        }
        this.mAtmService.mTaskSupervisor.updateTopResumedActivityIfNeeded("addChildTask");
        this.mAtmService.updateSleepIfNeededLocked();
        onRootTaskOrderChanged(task);
        ((com.android.server.wm.IMirageWindowManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IMirageWindowManagerExt.class).create()).onTaskAdded(getDisplayId(), task);
    }

    @Override // com.android.server.wm.WindowContainer
    protected void removeChild(com.android.server.wm.WindowContainer child) {
        if (child.asTaskDisplayArea() != null) {
            super.removeChild(child);
        } else {
            if (child.asTask() != null) {
                removeChildTask(child.asTask());
                return;
            }
            throw new java.lang.IllegalArgumentException("TaskDisplayArea can only remove Task and TaskDisplayArea, but found " + child);
        }
    }

    private void removeChildTask(com.android.server.wm.Task task) {
        ((com.android.server.wm.IMirageWindowManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IMirageWindowManagerExt.class).create()).onTaskRemoved(getDisplayId(), task);
        super.removeChild(task);
        onRootTaskRemoved(task);
        this.mAtmService.updateSleepIfNeededLocked();
        removeRootTaskReferenceIfNeeded(task);
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isOnTop() {
        return true;
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    void positionChildAt(int position, com.android.server.wm.WindowContainer child, boolean includingParents) {
        if (child.asTaskDisplayArea() != null) {
            super.positionChildAt(position, child, includingParents);
        } else {
            if (child.asTask() != null) {
                positionChildTaskAt(position, child.asTask(), includingParents);
                return;
            }
            throw new java.lang.IllegalArgumentException("TaskDisplayArea can only position Task and TaskDisplayArea, but found " + child);
        }
    }

    private void positionChildTaskAt(int position, com.android.server.wm.Task child, boolean includingParents) {
        boolean moveToTop = position >= getChildCount() - 1;
        boolean moveToBottom = position <= 0;
        int oldPosition = this.mChildren.indexOf(child);
        if (child.isAlwaysOnTop() && !moveToTop) {
            android.util.Slog.w("WindowManager", "Ignoring move of always-on-top root task=" + this + " to bottom");
            super.positionChildAt(oldPosition, child, false);
            return;
        }
        if ((!this.mDisplayContent.isTrusted() || this.mDisplayContent.mDontMoveToTop) && !getParent().isOnTop()) {
            includingParents = false;
        }
        int targetPosition = findPositionForRootTask(position, child, false);
        super.positionChildAt(targetPosition, child, false);
        if (includingParents && getParent() != null && (moveToTop || moveToBottom)) {
            getParent().positionChildAt(moveToTop ? Integer.MAX_VALUE : Integer.MIN_VALUE, this, true);
        }
        child.updateTaskMovement(moveToTop, moveToBottom, targetPosition);
        boolean isTopFocusableTask = moveToTop && child != this.mRootPinnedTask && child.isTopActivityFocusable();
        if (isTopFocusableTask) {
            this.mPreferredTopFocusableRootTask = child.shouldBeVisible(null) ? child : null;
        } else if (this.mPreferredTopFocusableRootTask == child) {
            this.mPreferredTopFocusableRootTask = null;
        }
        this.mAtmService.mTaskSupervisor.updateTopResumedActivityIfNeeded("positionChildTaskAt");
        if (this.mChildren.indexOf(child) != oldPosition) {
            onRootTaskOrderChanged(child);
        }
    }

    void onLeafTaskRemoved(int taskId) {
        if (this.mLastLeafTaskToFrontId == taskId) {
            this.mLastLeafTaskToFrontId = -1;
        }
    }

    void onLeafTaskMoved(com.android.server.wm.Task t, boolean toTop, boolean toBottom) {
        if (toBottom) {
            this.mAtmService.getTaskChangeNotificationController().notifyTaskMovedToBack(t.getTaskInfo());
        }
        if (!toTop) {
            if (t.mTaskId == this.mLastLeafTaskToFrontId) {
                this.mLastLeafTaskToFrontId = -1;
                com.android.server.wm.ActivityRecord topMost = getTopMostActivity();
                if (topMost != null) {
                    this.mAtmService.getTaskChangeNotificationController().notifyTaskMovedToFront(this.mTaskDisplayAreaExt.replaceByMultiSearchIfNeed(topMost.getTask()).getTaskInfo());
                    return;
                }
                return;
            }
            return;
        }
        if ((t.mTaskId == this.mLastLeafTaskToFrontId && !this.mTaskDisplayAreaExt.isAppUnlockPasswordActivity(t.topRunningActivity())) || t.topRunningActivityLocked() == null) {
            return;
        }
        com.android.server.wm.ActivityRecord topAr = t.topRunningActivity();
        if (topAr != null && topAr.intent != null && (topAr.intent.getOplusFlags() & 262144) != 0) {
            android.util.Slog.i(com.android.server.wm.ActivityTaskManagerService.TAG_ROOT_TASK, "skip notify for task which belong task view");
            return;
        }
        this.mLastLeafTaskToFrontId = t.mTaskId;
        com.android.server.wm.EventLogTags.writeWmTaskToFront(t.mUserId, t.mTaskId, getDisplayId());
        if (this.mTaskDisplayAreaExt.isActivityPreloadDisplay(this.mDisplayContent)) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_ROOT_TASK) {
                android.util.Slog.v(com.android.server.wm.ActivityTaskManagerService.TAG_ROOT_TASK, "Tasks move to front in actpreload display( " + this.mDisplayContent.mDisplayId + ") do not notify");
                return;
            }
            return;
        }
        this.mAtmService.getTaskChangeNotificationController().notifyTaskMovedToFront(this.mTaskDisplayAreaExt.replaceByMultiSearchIfNeed(t).getTaskInfo());
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    void onChildPositionChanged(com.android.server.wm.WindowContainer child) {
        super.onChildPositionChanged(child);
        this.mRootWindowContainer.invalidateTaskLayers();
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    boolean forAllTaskDisplayAreas(java.util.function.Predicate<com.android.server.wm.TaskDisplayArea> callback, boolean traverseTopToBottom) {
        return traverseTopToBottom ? super.forAllTaskDisplayAreas(callback, traverseTopToBottom) || callback.test(this) : callback.test(this) || super.forAllTaskDisplayAreas(callback, traverseTopToBottom);
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    void forAllTaskDisplayAreas(java.util.function.Consumer<com.android.server.wm.TaskDisplayArea> callback, boolean traverseTopToBottom) {
        if (traverseTopToBottom) {
            super.forAllTaskDisplayAreas(callback, traverseTopToBottom);
            callback.accept(this);
        } else {
            callback.accept(this);
            super.forAllTaskDisplayAreas(callback, traverseTopToBottom);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    <R> R reduceOnAllTaskDisplayAreas(java.util.function.BiFunction<com.android.server.wm.TaskDisplayArea, R, R> biFunction, R r, boolean z) {
        if (z) {
            return (R) biFunction.apply(this, super.reduceOnAllTaskDisplayAreas(biFunction, r, z));
        }
        return (R) super.reduceOnAllTaskDisplayAreas(biFunction, biFunction.apply(this, r), z);
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    <R> R getItemFromTaskDisplayAreas(java.util.function.Function<com.android.server.wm.TaskDisplayArea, R> function, boolean z) {
        if (z) {
            R r = (R) super.getItemFromTaskDisplayAreas(function, z);
            return r != null ? r : function.apply(this);
        }
        R rApply = function.apply(this);
        if (rApply != null) {
            return rApply;
        }
        return (R) super.getItemFromTaskDisplayAreas(function, z);
    }

    private int getPriority(com.android.server.wm.WindowContainer child) {
        com.android.server.wm.TaskDisplayArea tda = child.asTaskDisplayArea();
        if (tda != null) {
            return tda.getPriority(tda.getTopChild());
        }
        com.android.server.wm.Task rootTask = child.asTask();
        if (this.mWmService.mAssistantOnTopOfDream && rootTask.isActivityTypeAssistant()) {
            return 4;
        }
        if (rootTask.isActivityTypeDream()) {
            return 3;
        }
        if (rootTask.inPinnedWindowingMode()) {
            return 2;
        }
        return rootTask.isAlwaysOnTop() ? 1 : 0;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private int findMinPositionForRootTask(com.android.server.wm.Task rootTask) {
        int currentIndex;
        int minPosition = Integer.MIN_VALUE;
        for (int i = 0; i < this.mChildren.size() && getPriority((com.android.server.wm.WindowContainer) this.mChildren.get(i)) < getPriority(rootTask); i++) {
            minPosition = i;
        }
        if (rootTask.isAlwaysOnTop() && (currentIndex = this.mChildren.indexOf(rootTask)) > minPosition) {
            return currentIndex;
        }
        return minPosition;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private int findMaxPositionForRootTask(com.android.server.wm.Task rootTask) {
        int i = this.mChildren.size() - 1;
        while (true) {
            if (i < 0) {
                return 0;
            }
            com.android.server.wm.WindowContainer curr = (com.android.server.wm.WindowContainer) this.mChildren.get(i);
            boolean sameRootTask = curr == rootTask;
            if (getPriority(curr) <= getPriority(rootTask) && !sameRootTask && (!rootTask.isAlwaysOnTop() || !this.mTaskDisplayAreaExt.isFlexibleTaskPriorityLower(curr, rootTask))) {
                break;
            }
            i--;
        }
        return i;
    }

    private int findPositionForRootTask(int requestedPosition, com.android.server.wm.Task rootTask, boolean adding) {
        int maxPosition = findMaxPositionForRootTask(rootTask);
        int maxPosition2 = this.mTaskDisplayAreaExt.adjustMaxPositionForSplitRootTask(rootTask, maxPosition);
        int minPosition = findMinPositionForRootTask(rootTask);
        if (requestedPosition == Integer.MAX_VALUE) {
            requestedPosition = this.mChildren.size();
        } else if (requestedPosition == Integer.MIN_VALUE) {
            requestedPosition = 0;
        }
        int targetPosition = java.lang.Math.max(java.lang.Math.min(requestedPosition, maxPosition2), minPosition);
        int prevPosition = this.mChildren.indexOf(rootTask);
        if (targetPosition == requestedPosition) {
            return targetPosition;
        }
        if (adding || targetPosition < prevPosition) {
            return targetPosition + 1;
        }
        return targetPosition;
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    int getOrientation(final int candidate) {
        int orientation = super.getOrientation(candidate);
        if (!canSpecifyOrientation(orientation)) {
            this.mLastOrientationSource = null;
            return ((java.lang.Integer) reduceOnAllTaskDisplayAreas(new java.util.function.BiFunction() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda5
                @Override // java.util.function.BiFunction
                public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                    return this.f$0.lambda$getOrientation$2(candidate, (com.android.server.wm.TaskDisplayArea) obj, (java.lang.Integer) obj2);
                }
            }, -2)).intValue();
        }
        if (this.mTaskDisplayAreaExt.shouldIgnoreRotationForSplitMini()) {
            android.util.Slog.d("WindowManager", "IsSplitScreenMini, getOrientation UNSPECIFIED.");
            return -1;
        }
        if (orientation == -2 || orientation == 3) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                long protoLogParam0 = this.mDisplayContent.getLastOrientation();
                long protoLogParam1 = this.mDisplayContent.mDisplayId;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 2005499548343677845L, 5, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1));
            }
            return this.mDisplayContent.getLastOrientation();
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
            long protoLogParam02 = orientation;
            long protoLogParam12 = this.mDisplayContent.mDisplayId;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 7378236902389922467L, 5, null, java.lang.Long.valueOf(protoLogParam02), java.lang.Long.valueOf(protoLogParam12));
        }
        return orientation;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$getOrientation$2(int candidate, com.android.server.wm.TaskDisplayArea taskDisplayArea, java.lang.Integer taskOrientation) {
        if (taskDisplayArea == this || taskOrientation.intValue() != -2) {
            return taskOrientation;
        }
        return java.lang.Integer.valueOf(taskDisplayArea.getOrientation(candidate));
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    void assignChildLayers(android.view.SurfaceControl.Transaction t) {
        assignRootTaskOrdering(t);
        for (int i = 0; i < this.mChildren.size(); i++) {
            ((com.android.server.wm.WindowContainer) this.mChildren.get(i)).assignChildLayers(t);
        }
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    void assignRootTaskOrdering(android.view.SurfaceControl.Transaction t) {
        if (getParent() == null) {
            return;
        }
        this.mTmpAlwaysOnTopChildren.clear();
        this.mTmpHomeChildren.clear();
        this.mTmpNormalChildren.clear();
        this.mTaskDisplayAreaExt.clearZoomChildren();
        for (int i = 0; i < this.mChildren.size(); i++) {
            com.android.server.wm.WindowContainer child = (com.android.server.wm.WindowContainer) this.mChildren.get(i);
            com.android.server.wm.TaskDisplayArea childTda = child.asTaskDisplayArea();
            if (childTda != null) {
                com.android.server.wm.Task childTdaTopRootTask = childTda.getTopRootTask();
                if (childTdaTopRootTask == null) {
                    this.mTmpNormalChildren.add(childTda);
                } else if (childTdaTopRootTask.isAlwaysOnTop()) {
                    this.mTmpAlwaysOnTopChildren.add(childTda);
                } else if (childTdaTopRootTask.isActivityTypeHome()) {
                    this.mTmpHomeChildren.add(childTda);
                } else {
                    this.mTmpNormalChildren.add(childTda);
                }
            } else {
                com.android.server.wm.Task childTask = child.asTask();
                if (childTask.isAlwaysOnTop()) {
                    this.mTmpAlwaysOnTopChildren.add(childTask);
                    if (this.mTaskDisplayAreaExt.isZoomMode(childTask.getWindowingMode()) || (this.mTaskDisplayAreaExt.isFlexibleTask(childTask) && !this.mTaskDisplayAreaExt.isFlexibleTaskSink(childTask))) {
                        this.mTaskDisplayAreaExt.addZoomChildren(childTask);
                    }
                } else if (childTask.isActivityTypeHome()) {
                    this.mTmpHomeChildren.add(childTask);
                } else if (this.mTaskDisplayAreaExt.isZoomMode(childTask.getWindowingMode()) || (this.mTaskDisplayAreaExt.isFlexibleTask(childTask) && !this.mTaskDisplayAreaExt.isFlexibleTaskSink(childTask))) {
                    this.mTaskDisplayAreaExt.addZoomChildren(childTask);
                } else {
                    this.mTmpNormalChildren.add(childTask);
                }
            }
        }
        int layer = adjustRootTaskLayer(t, this.mTmpHomeChildren, 0);
        int layer2 = adjustRootTaskLayer(t, this.mTmpAlwaysOnTopChildren, adjustRootTaskLayer(t, this.mTmpNormalChildren, layer));
        java.util.ArrayList<com.android.server.wm.WindowContainer> tmpFloatChildren = new java.util.ArrayList<>();
        tmpFloatChildren.addAll(this.mTaskDisplayAreaExt.getZoomChildren());
        java.util.ArrayList<com.android.server.wm.WindowContainer> tmpFloatChildren2 = new java.util.ArrayList<>(tmpFloatChildren.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$assignRootTaskOrdering$3((com.android.server.wm.WindowContainer) obj);
            }
        }).toList());
        if (hasPinnedTask()) {
            tmpFloatChildren2.add(getRootPinnedTask());
        }
        adjustRootTaskLayer(t, tmpFloatChildren2, layer2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$assignRootTaskOrdering$3(com.android.server.wm.WindowContainer tmp) {
        return (tmp == null || !tmp.isAlwaysOnTop() || this.mTaskDisplayAreaExt.isFlexibleTaskSink(tmp.asTask())) ? false : true;
    }

    private int adjustRootTaskLayer(android.view.SurfaceControl.Transaction t, java.util.ArrayList<com.android.server.wm.WindowContainer> children, int startLayer) {
        boolean childNeedsZBoost;
        this.mTmpNeedsZBoostIndexes.clear();
        int childCount = children.size();
        int i = 0;
        while (true) {
            if (i >= childCount) {
                break;
            }
            if (i >= childCount) {
                android.util.Slog.e("WindowManager", "childCount OutOfBoundsException break");
                break;
            }
            com.android.server.wm.WindowContainer child = children.get(i);
            com.android.server.wm.TaskDisplayArea childTda = child.asTaskDisplayArea();
            if (childTda != null) {
                childNeedsZBoost = childTda.childrenNeedZBoost();
            } else {
                childNeedsZBoost = child.needsZBoost();
            }
            if (childNeedsZBoost) {
                this.mTmpNeedsZBoostIndexes.add(i);
            } else {
                child.assignLayer(t, startLayer);
                startLayer++;
            }
            i++;
        }
        int zBoostSize = this.mTmpNeedsZBoostIndexes.size();
        int i2 = 0;
        while (i2 < zBoostSize) {
            children.get(this.mTmpNeedsZBoostIndexes.get(i2)).assignLayer(t, startLayer);
            i2++;
            startLayer++;
        }
        return startLayer;
    }

    private boolean childrenNeedZBoost() {
        final boolean[] needsZBoost = new boolean[1];
        forAllRootTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.TaskDisplayArea.lambda$childrenNeedZBoost$4(needsZBoost, (com.android.server.wm.Task) obj);
            }
        });
        return needsZBoost[0];
    }

    static /* synthetic */ void lambda$childrenNeedZBoost$4(boolean[] needsZBoost, com.android.server.wm.Task task) {
        needsZBoost[0] = needsZBoost[0] | task.needsZBoost();
    }

    @Override // com.android.server.wm.WindowContainer
    android.view.RemoteAnimationTarget createRemoteAnimationTarget(com.android.server.wm.RemoteAnimationController.RemoteAnimationRecord record) {
        com.android.server.wm.ActivityRecord activity = getTopMostActivity();
        if (activity != null) {
            return activity.createRemoteAnimationTarget(record);
        }
        return null;
    }

    void setBackgroundColor(int colorInt) {
        setBackgroundColor(colorInt, false);
    }

    void setBackgroundColor(int colorInt, boolean restore) {
        this.mBackgroundColor = colorInt;
        android.graphics.Color color = android.graphics.Color.valueOf(colorInt);
        if (!restore) {
            this.mColorLayerCounter++;
        }
        if (this.mSurfaceControl != null) {
            getPendingTransaction().setColor(this.mSurfaceControl, new float[]{color.red(), color.green(), color.blue()});
            scheduleAnimation();
        }
    }

    void clearBackgroundColor() {
        this.mColorLayerCounter--;
        if (this.mColorLayerCounter == 0 && this.mSurfaceControl != null) {
            getPendingTransaction().unsetColor(this.mSurfaceControl);
            scheduleAnimation();
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void migrateToNewSurfaceControl(android.view.SurfaceControl.Transaction t) {
        super.migrateToNewSurfaceControl(t);
        if (this.mColorLayerCounter > 0) {
            setBackgroundColor(this.mBackgroundColor, true);
        }
        reassignLayer(t);
        scheduleAnimation();
    }

    void onRootTaskRemoved(com.android.server.wm.Task rootTask) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_ROOT_TASK) {
            android.util.Slog.v(com.android.server.wm.ActivityTaskManagerService.TAG_ROOT_TASK, "onRootTaskRemoved: detaching " + rootTask + " from displayId=" + this.mDisplayContent.mDisplayId);
        }
        this.mTaskDisplayAreaExt.onRootTaskRemoved(rootTask);
        if (this.mPreferredTopFocusableRootTask == rootTask) {
            this.mPreferredTopFocusableRootTask = null;
        }
        if (this.mLaunchAdjacentFlagRootTask == rootTask) {
            this.mLaunchAdjacentFlagRootTask = null;
        }
        this.mDisplayContent.releaseSelfIfNeeded();
        onRootTaskOrderChanged(rootTask);
    }

    void positionTaskBehindHome(com.android.server.wm.Task task) {
        com.android.server.wm.Task home = getOrCreateRootHomeTask();
        com.android.server.wm.WindowContainer homeParent = home.getParent();
        com.android.server.wm.Task homeParentTask = homeParent != null ? homeParent.asTask() : null;
        if (homeParentTask == null) {
            if (task.getParent() == this) {
                positionChildAt(Integer.MIN_VALUE, task, false);
                return;
            } else {
                task.reparent(this, false);
                return;
            }
        }
        if (homeParentTask == task.getParent()) {
            homeParentTask.positionChildAtBottom(task);
        } else {
            task.reparent(homeParentTask, false, 2, false, false, "positionTaskBehindHome");
        }
    }

    com.android.server.wm.Task getOrCreateRootTask(int windowingMode, int activityType, boolean onTop) {
        return getOrCreateRootTask(windowingMode, activityType, onTop, null, null, null, 0);
    }

    com.android.server.wm.Task getOrCreateRootTask(int windowingMode, int activityType, boolean onTop, com.android.server.wm.Task candidateTask, com.android.server.wm.Task sourceTask, android.app.ActivityOptions options, int launchFlags) {
        int resolvedWindowingMode = windowingMode == 0 ? getWindowingMode() : windowingMode;
        if (!com.android.server.wm.DisplayContent.alwaysCreateRootTask(resolvedWindowingMode, activityType)) {
            com.android.server.wm.Task rootTask = getRootTask(resolvedWindowingMode, activityType);
            if (rootTask != null) {
                return rootTask;
            }
        } else if (candidateTask != null) {
            int position = onTop ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            com.android.server.wm.Task launchParentTask = getLaunchRootTask(resolvedWindowingMode, activityType, options, sourceTask, launchFlags, candidateTask);
            if (launchParentTask == null) {
                if (candidateTask.getDisplayArea() != this || candidateTask.getRootTask().mReparentLeafTaskIfRelaunch) {
                    if (candidateTask.getParent() == null) {
                        addChild(candidateTask, position);
                    } else {
                        candidateTask.reparent(this, onTop);
                    }
                }
            } else if (candidateTask.getParent() == null) {
                launchParentTask.addChild(candidateTask, position);
            } else if (candidateTask.getParent() != launchParentTask) {
                candidateTask.reparent(launchParentTask, position);
            }
            if (windowingMode != 0 && candidateTask.isRootTask() && candidateTask.getWindowingMode() != windowingMode) {
                candidateTask.mTransitionController.collect(candidateTask);
                candidateTask.setWindowingMode(windowingMode);
            }
            return candidateTask.getRootTask();
        }
        return new com.android.server.wm.Task.Builder(this.mAtmService).setWindowingMode(windowingMode).setActivityType(activityType).setOnTop(onTop).setParent(this).setSourceTask(sourceTask).setActivityOptions(options).setLaunchFlags(launchFlags).build();
    }

    com.android.server.wm.Task getOrCreateRootTask(com.android.server.wm.ActivityRecord r, android.app.ActivityOptions options, com.android.server.wm.Task candidateTask, com.android.server.wm.Task sourceTask, com.android.server.wm.LaunchParamsController.LaunchParams launchParams, int launchFlags, int activityType, boolean onTop) {
        int windowingMode = 0;
        if (launchParams != null) {
            windowingMode = launchParams.mWindowingMode;
        } else if (options != null) {
            windowingMode = options.getLaunchWindowingMode();
        }
        if (this.mTaskDisplayAreaExt.isComactValidWindowingMode(windowingMode) && candidateTask != null) {
            windowingMode = 0;
        }
        return getOrCreateRootTask(validateWindowingMode(windowingMode, r, candidateTask), activityType, onTop, candidateTask, sourceTask, options, launchFlags);
    }

    int getNextRootTaskId() {
        return this.mAtmService.mTaskSupervisor.getNextTaskIdForUser();
    }

    com.android.server.wm.Task createRootTask(int windowingMode, int activityType, boolean onTop) {
        return createRootTask(windowingMode, activityType, onTop, null);
    }

    com.android.server.wm.Task createRootTask(int windowingMode, int activityType, boolean onTop, android.app.ActivityOptions opts) {
        return new com.android.server.wm.Task.Builder(this.mAtmService).setWindowingMode(windowingMode).setActivityType(activityType).setParent(this).setOnTop(onTop).setActivityOptions(opts).build();
    }

    void setLaunchRootTask(com.android.server.wm.Task rootTask, int[] windowingModes, int[] activityTypes) {
        if (!rootTask.mCreatedByOrganizer) {
            throw new java.lang.IllegalArgumentException("Can't set not mCreatedByOrganizer as launch root tr=" + rootTask);
        }
        com.android.server.wm.TaskDisplayArea.LaunchRootTaskDef def = getLaunchRootTaskDef(rootTask);
        if (def != null) {
            this.mLaunchRootTasks.remove(def);
        } else {
            def = new com.android.server.wm.TaskDisplayArea.LaunchRootTaskDef();
            def.task = rootTask;
        }
        def.activityTypes = activityTypes;
        def.windowingModes = windowingModes;
        if (!com.android.internal.util.ArrayUtils.isEmpty(windowingModes) || !com.android.internal.util.ArrayUtils.isEmpty(activityTypes)) {
            this.mLaunchRootTasks.add(def);
        }
    }

    void removeLaunchRootTask(com.android.server.wm.Task rootTask) {
        com.android.server.wm.TaskDisplayArea.LaunchRootTaskDef def = getLaunchRootTaskDef(rootTask);
        if (def != null) {
            this.mLaunchRootTasks.remove(def);
        }
    }

    void setLaunchAdjacentFlagRootTask(com.android.server.wm.Task adjacentFlagRootTask) {
        if (adjacentFlagRootTask != null) {
            if (!adjacentFlagRootTask.mCreatedByOrganizer) {
                throw new java.lang.IllegalArgumentException("Can't set not mCreatedByOrganizer as launch adjacent flag root tr=" + adjacentFlagRootTask);
            }
            if (adjacentFlagRootTask.getAdjacentTaskFragment() == null) {
                throw new java.lang.UnsupportedOperationException("Can't set non-adjacent root as launch adjacent flag root tr=" + adjacentFlagRootTask);
            }
        }
        this.mLaunchAdjacentFlagRootTask = adjacentFlagRootTask;
    }

    private com.android.server.wm.TaskDisplayArea.LaunchRootTaskDef getLaunchRootTaskDef(com.android.server.wm.Task rootTask) {
        for (int i = this.mLaunchRootTasks.size() - 1; i >= 0; i--) {
            if (this.mLaunchRootTasks.get(i).task.mTaskId == rootTask.mTaskId) {
                com.android.server.wm.TaskDisplayArea.LaunchRootTaskDef def = this.mLaunchRootTasks.get(i);
                return def;
            }
        }
        return null;
    }

    com.android.server.wm.Task getLaunchRootTask(int windowingMode, int activityType, android.app.ActivityOptions options, com.android.server.wm.Task sourceTask, int launchFlags) {
        return getLaunchRootTask(windowingMode, activityType, options, sourceTask, launchFlags, null);
    }

    com.android.server.wm.Task getLaunchRootTask(int windowingMode, int activityType, android.app.ActivityOptions options, com.android.server.wm.Task sourceTask, int launchFlags, com.android.server.wm.Task candidateTask) {
        com.android.server.wm.Task adjacentTarget;
        com.android.server.wm.Task launchRootTask;
        if (options != null && (launchRootTask = com.android.server.wm.Task.fromWindowContainerToken(options.getLaunchRootTask())) != null && launchRootTask.mCreatedByOrganizer) {
            return launchRootTask;
        }
        if ((launchFlags & 4096) != 0 && this.mLaunchAdjacentFlagRootTask != null && (sourceTask == null || sourceTask != candidateTask)) {
            if (sourceTask != null && this.mLaunchAdjacentFlagRootTask.getAdjacentTask() != null && (sourceTask == this.mLaunchAdjacentFlagRootTask || sourceTask.isDescendantOf(this.mLaunchAdjacentFlagRootTask))) {
                return this.mLaunchAdjacentFlagRootTask.getAdjacentTask();
            }
            return this.mLaunchAdjacentFlagRootTask;
        }
        int i = this.mLaunchRootTasks.size();
        do {
            i--;
            if (i < 0) {
                if (sourceTask == null || ((candidateTask != null && candidateTask.getWindowingMode() == 2) || (adjacentTarget = sourceTask.getAdjacentTask()) == null)) {
                    return null;
                }
                if (candidateTask != null && (candidateTask == adjacentTarget || candidateTask.isDescendantOf(adjacentTarget))) {
                    com.android.server.wm.Task candidateRoot = candidateTask.getCreatedByOrganizerTask();
                    if (candidateRoot == null && sourceTask.getWindowingMode() == 6 && this.mTaskDisplayAreaExt.isZoomMode(candidateTask.getWindowingMode())) {
                        return candidateRoot;
                    }
                    return adjacentTarget;
                }
                return sourceTask.getCreatedByOrganizerTask();
            }
        } while (!this.mLaunchRootTasks.get(i).contains(windowingMode, activityType));
        com.android.server.wm.Task launchRootTask2 = this.mLaunchRootTasks.get(i).task;
        com.android.server.wm.Task adjacentRootTask = launchRootTask2 != null ? launchRootTask2.getAdjacentTask() : null;
        if (sourceTask != null && adjacentRootTask != null && (sourceTask == adjacentRootTask || sourceTask.isDescendantOf(adjacentRootTask))) {
            return adjacentRootTask;
        }
        return launchRootTask2;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    com.android.server.wm.Task getFocusedRootTask() {
        if (this.mPreferredTopFocusableRootTask != null) {
            return this.mPreferredTopFocusableRootTask;
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer child = (com.android.server.wm.WindowContainer) this.mChildren.get(i);
            if (child.asTaskDisplayArea() != null) {
                com.android.server.wm.Task rootTask = child.asTaskDisplayArea().getFocusedRootTask();
                if (rootTask != null) {
                    return rootTask;
                }
            } else {
                com.android.server.wm.Task rootTask2 = ((com.android.server.wm.WindowContainer) this.mChildren.get(i)).asTask();
                if (rootTask2.isFocusableAndVisible() && !this.mTaskDisplayAreaExt.isShouldSkipZoomRootTask(rootTask2)) {
                    return rootTask2;
                }
            }
        }
        return null;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    com.android.server.wm.Task getNextFocusableRootTask(com.android.server.wm.Task currentFocus, boolean ignoreCurrent) {
        if (currentFocus != null) {
            currentFocus.getWindowingMode();
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer child = (com.android.server.wm.WindowContainer) this.mChildren.get(i);
            if (child.asTaskDisplayArea() != null) {
                com.android.server.wm.Task rootTask = child.asTaskDisplayArea().getNextFocusableRootTask(currentFocus, ignoreCurrent);
                if (rootTask != null) {
                    return rootTask;
                }
            } else {
                com.android.server.wm.Task rootTask2 = ((com.android.server.wm.WindowContainer) this.mChildren.get(i)).asTask();
                if ((!ignoreCurrent || rootTask2 != currentFocus) && rootTask2.isFocusableAndVisible()) {
                    return rootTask2;
                }
            }
        }
        return null;
    }

    com.android.server.wm.ActivityRecord getFocusedActivity() {
        com.android.server.wm.Task focusedRootTask = getFocusedRootTask();
        if (focusedRootTask == null) {
            return null;
        }
        com.android.server.wm.ActivityRecord resumedActivity = focusedRootTask.getTopResumedActivity();
        if (resumedActivity == null || resumedActivity.app == null) {
            com.android.server.wm.ActivityRecord resumedActivity2 = focusedRootTask.getTopPausingActivity();
            if (resumedActivity2 == null || resumedActivity2.app == null) {
                return focusedRootTask.topRunningActivity(true);
            }
            return resumedActivity2;
        }
        return resumedActivity;
    }

    com.android.server.wm.Task getLastFocusedRootTask() {
        return this.mLastFocusedRootTask;
    }

    void updateLastFocusedRootTask(com.android.server.wm.Task prevFocusedTask, java.lang.String updateLastFocusedTaskReason) {
        com.android.server.wm.Task currentFocusedTask;
        if (updateLastFocusedTaskReason == null || (currentFocusedTask = getFocusedRootTask()) == prevFocusedTask) {
            return;
        }
        if (this.mDisplayContent.isSleeping() && currentFocusedTask != null) {
            currentFocusedTask.clearLastPausedActivity();
        }
        this.mLastFocusedRootTask = prevFocusedTask;
        if (updateLastFocusedTaskReason != null && currentFocusedTask != null && currentFocusedTask.getTopActivity(false, false) != null && currentFocusedTask.getTopActivity(false, false).app != null && !inFreeformWindowingMode() && !updateLastFocusedTaskReason.contains("finish-top")) {
            this.mTaskDisplayAreaExt.notifySysActivityStackChange(com.android.server.wm.TaskDisplayArea.class, currentFocusedTask.getTopActivity(false, false).mActivityComponent);
        }
        com.android.server.wm.EventLogTags.writeWmFocusedRootTask(this.mRootWindowContainer.mCurrentUser, this.mDisplayContent.mDisplayId, currentFocusedTask == null ? -1 : currentFocusedTask.getRootTaskId(), this.mLastFocusedRootTask != null ? this.mLastFocusedRootTask.getRootTaskId() : -1, updateLastFocusedTaskReason);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    boolean allResumedActivitiesComplete() {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer child = (com.android.server.wm.WindowContainer) this.mChildren.get(i);
            if (child.asTaskDisplayArea() != null) {
                if (!child.asTaskDisplayArea().allResumedActivitiesComplete()) {
                    return false;
                }
            } else {
                com.android.server.wm.ActivityRecord r = ((com.android.server.wm.WindowContainer) this.mChildren.get(i)).asTask().getTopResumedActivity();
                if (r != null && !r.isState(com.android.server.wm.ActivityRecord.State.RESUMED)) {
                    return false;
                }
            }
        }
        com.android.server.wm.Task currentFocusedRootTask = getFocusedRootTask();
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_ROOT_TASK) {
            android.util.Slog.d(com.android.server.wm.ActivityTaskManagerService.TAG_ROOT_TASK, "allResumedActivitiesComplete: currentFocusedRootTask changing from=" + this.mLastFocusedRootTask + " to=" + currentFocusedRootTask);
        }
        this.mTaskDisplayAreaExt.onFocusedTaskChanged(this.mLastFocusedRootTask, currentFocusedRootTask);
        this.mLastFocusedRootTask = currentFocusedRootTask;
        return true;
    }

    boolean pauseBackTasks(final com.android.server.wm.ActivityRecord resuming) {
        final int[] someActivityPaused = {0};
        forAllLeafTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.TaskDisplayArea.lambda$pauseBackTasks$5(resuming, someActivityPaused, (com.android.server.wm.Task) obj);
            }
        }, true);
        return someActivityPaused[0] > 0;
    }

    static /* synthetic */ void lambda$pauseBackTasks$5(com.android.server.wm.ActivityRecord resuming, int[] someActivityPaused, com.android.server.wm.Task leafTask) {
        if (leafTask.pauseActivityIfNeeded(resuming, "pauseBackTasks")) {
            someActivityPaused[0] = someActivityPaused[0] + 1;
        }
    }

    static boolean isWindowingModeSupported(int windowingMode, boolean supportsMultiWindow, boolean supportsFreeform, boolean supportsPip) {
        if (windowingMode == 0 || windowingMode == 1) {
            return true;
        }
        if (!supportsMultiWindow) {
            return false;
        }
        if (windowingMode == 6) {
            return true;
        }
        if (!supportsFreeform && windowingMode == 5) {
            return false;
        }
        if (supportsPip || windowingMode != 2) {
            return true;
        }
        return false;
    }

    int resolveWindowingMode(com.android.server.wm.ActivityRecord r, android.app.ActivityOptions options, com.android.server.wm.Task task) {
        int windowingMode = options != null ? options.getLaunchWindowingMode() : 0;
        if (windowingMode == 0) {
            if (task != null) {
                windowingMode = task.getWindowingMode();
            }
            if (windowingMode == 0 && r != null) {
                windowingMode = r.getWindowingMode();
            }
            if (windowingMode == 0) {
                windowingMode = getWindowingMode();
            }
        }
        int windowingMode2 = validateWindowingMode(windowingMode, r, task);
        if (windowingMode2 != 0) {
            return windowingMode2;
        }
        return 1;
    }

    boolean isValidWindowingMode(int windowingMode, com.android.server.wm.ActivityRecord r, com.android.server.wm.Task task) {
        if (this.mTaskDisplayAreaExt.isValidWindowingMode(windowingMode, r, task)) {
            return windowingMode != 0;
        }
        if (this.mTaskDisplayAreaExt.isComactValidWindowingMode(windowingMode) || this.mTaskDisplayAreaExt.isBracketValidWindowingMode(windowingMode)) {
            return true;
        }
        boolean supportsMultiWindow = this.mAtmService.mSupportsMultiWindow;
        boolean supportsFreeform = this.mAtmService.mSupportsFreeformWindowManagement;
        boolean supportsPip = this.mAtmService.mSupportsPictureInPicture;
        if (supportsMultiWindow) {
            if (task != null) {
                supportsFreeform = task.supportsFreeformInDisplayArea(this);
                supportsMultiWindow = task.supportsMultiWindowInDisplayArea(this) || (windowingMode == 2 && supportsPip);
            } else if (r != null) {
                supportsFreeform = r.supportsFreeformInDisplayArea(this);
                supportsPip = r.supportsPictureInPicture();
                supportsMultiWindow = r.supportsMultiWindowInDisplayArea(this);
            }
        }
        return windowingMode != 0 && isWindowingModeSupported(windowingMode, supportsMultiWindow, supportsFreeform, supportsPip);
    }

    int validateWindowingMode(int windowingMode, com.android.server.wm.ActivityRecord r, com.android.server.wm.Task task) {
        if (!isValidWindowingMode(windowingMode, r, task)) {
            return 0;
        }
        return windowingMode;
    }

    boolean supportsNonResizableMultiWindow() {
        int configSupportsNonResizableMultiWindow = this.mAtmService.mSupportsNonResizableMultiWindow;
        if (this.mAtmService.mDevEnableNonResizableMultiWindow || configSupportsNonResizableMultiWindow == 1) {
            return true;
        }
        if (configSupportsNonResizableMultiWindow == -1) {
            return false;
        }
        return isLargeEnoughForMultiWindow();
    }

    boolean supportsActivityMinWidthHeightMultiWindow(int minWidth, int minHeight, android.content.pm.ActivityInfo activityInfo) {
        int configRespectsActivityMinWidthHeightMultiWindow;
        if (activityInfo != null && !activityInfo.shouldCheckMinWidthHeightForMultiWindow()) {
            return true;
        }
        if ((minWidth <= 0 && minHeight <= 0) || (configRespectsActivityMinWidthHeightMultiWindow = this.mAtmService.mRespectsActivityMinWidthHeightMultiWindow) == -1) {
            return true;
        }
        if (configRespectsActivityMinWidthHeightMultiWindow == 0 && isLargeEnoughForMultiWindow()) {
            return true;
        }
        android.content.res.Configuration config = getConfiguration();
        int orientation = config.orientation;
        if (orientation == 2) {
            int maxSupportMinWidth = (int) (this.mAtmService.mMinPercentageMultiWindowSupportWidth * config.screenWidthDp * this.mDisplayContent.getDisplayMetrics().density);
            return minWidth <= maxSupportMinWidth;
        }
        int maxSupportMinHeight = (int) (this.mAtmService.mMinPercentageMultiWindowSupportHeight * config.screenHeightDp * this.mDisplayContent.getDisplayMetrics().density);
        return minHeight <= maxSupportMinHeight;
    }

    private boolean isLargeEnoughForMultiWindow() {
        return getConfiguration().smallestScreenWidthDp >= 600;
    }

    boolean isTopRootTask(com.android.server.wm.Task rootTask) {
        return rootTask == getTopRootTask();
    }

    com.android.server.wm.ActivityRecord topRunningActivity() {
        return topRunningActivity(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public com.android.server.wm.ActivityRecord topRunningActivity(boolean considerKeyguardState) {
        com.android.server.wm.ActivityRecord topRunning = null;
        com.android.server.wm.Task focusedRootTask = getFocusedRootTask();
        if (focusedRootTask != null) {
            topRunning = focusedRootTask.topRunningActivity();
        }
        if (topRunning == null) {
            for (int i = this.mChildren.size() - 1; i >= 0; i--) {
                com.android.server.wm.WindowContainer child = (com.android.server.wm.WindowContainer) this.mChildren.get(i);
                if (child.asTaskDisplayArea() != null) {
                    topRunning = child.asTaskDisplayArea().topRunningActivity(considerKeyguardState);
                    if (topRunning != null) {
                        break;
                    }
                } else {
                    com.android.server.wm.Task rootTask = ((com.android.server.wm.WindowContainer) this.mChildren.get(i)).asTask();
                    if (rootTask != focusedRootTask && rootTask.isTopActivityFocusable() && (topRunning = rootTask.topRunningActivity()) != null) {
                        break;
                    }
                }
            }
        }
        if (topRunning != null && considerKeyguardState && this.mRootWindowContainer.mTaskSupervisor.getKeyguardController().isKeyguardLocked(topRunning.getDisplayId()) && !topRunning.canShowWhenLocked()) {
            return null;
        }
        return topRunning;
    }

    protected int getRootTaskCount() {
        final int[] count = new int[1];
        forAllRootTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.TaskDisplayArea.lambda$getRootTaskCount$6(count, (com.android.server.wm.Task) obj);
            }
        });
        return count[0];
    }

    static /* synthetic */ void lambda$getRootTaskCount$6(int[] count, com.android.server.wm.Task task) {
        count[0] = count[0] + 1;
    }

    com.android.server.wm.Task getOrCreateRootHomeTask() {
        return getOrCreateRootHomeTask(false);
    }

    com.android.server.wm.Task getOrCreateRootHomeTask(boolean onTop) {
        com.android.server.wm.Task homeTask = getRootHomeTask();
        if (homeTask == null && canHostHomeTask()) {
            return createRootTask(0, 2, onTop);
        }
        return homeTask;
    }

    com.android.server.wm.Task getTopRootTaskInWindowingMode(int windowingMode) {
        return getRootTask(windowingMode, 0);
    }

    void moveHomeRootTaskToFront(java.lang.String reason) {
        com.android.server.wm.Task homeRootTask = getOrCreateRootHomeTask();
        if (homeRootTask != null) {
            homeRootTask.moveToFront(reason);
        }
    }

    void moveHomeActivityToTop(java.lang.String reason) {
        com.android.server.wm.ActivityRecord top = getHomeActivity();
        if (top == null) {
            moveHomeRootTaskToFront(reason);
        } else {
            top.moveFocusableActivityToTop(reason);
        }
    }

    com.android.server.wm.ActivityRecord getHomeActivity() {
        return getHomeActivityForUser(this.mRootWindowContainer.mCurrentUser);
    }

    com.android.server.wm.ActivityRecord getHomeActivityForUser(int userId) {
        com.android.server.wm.Task rootHomeTask = getRootHomeTask();
        if (rootHomeTask == null) {
            return null;
        }
        com.android.internal.util.function.pooled.PooledPredicate p = com.android.internal.util.function.pooled.PooledLambda.obtainPredicate(new java.util.function.BiPredicate() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda2
            @Override // java.util.function.BiPredicate
            public final boolean test(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.wm.TaskDisplayArea.isHomeActivityForUser((com.android.server.wm.ActivityRecord) obj, ((java.lang.Integer) obj2).intValue());
            }
        }, com.android.internal.util.function.pooled.PooledLambda.__(com.android.server.wm.ActivityRecord.class), java.lang.Integer.valueOf(userId));
        com.android.server.wm.ActivityRecord r = rootHomeTask.getActivity(p);
        p.recycle();
        return r;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isHomeActivityForUser(com.android.server.wm.ActivityRecord r, int userId) {
        return r.isActivityTypeHome() && (userId == -1 || r.mUserId == userId);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    void moveRootTaskBehindBottomMostVisibleRootTask(com.android.server.wm.Task rootTask) {
        com.android.server.wm.Task s;
        if (rootTask.shouldBeVisible(null)) {
            return;
        }
        rootTask.getParent().positionChildAt(Integer.MIN_VALUE, rootTask, false);
        boolean isRootTask = rootTask.isRootTask();
        int numRootTasks = isRootTask ? this.mChildren.size() : rootTask.getParent().getChildCount();
        for (int rootTaskNdx = 0; rootTaskNdx < numRootTasks; rootTaskNdx++) {
            if (isRootTask) {
                com.android.server.wm.WindowContainer child = (com.android.server.wm.WindowContainer) this.mChildren.get(rootTaskNdx);
                if (child.asTaskDisplayArea() != null) {
                    s = child.asTaskDisplayArea().getBottomMostVisibleRootTask(rootTask);
                } else {
                    s = child.asTask();
                }
            } else {
                s = rootTask.getParent().getChildAt(rootTaskNdx).asTask();
            }
            if (s != rootTask && s != null) {
                int winMode = s.getWindowingMode();
                boolean isValidWindowingMode = true;
                if (winMode != 1 && !this.mTaskDisplayAreaExt.isComactValidWindowingMode(winMode)) {
                    isValidWindowingMode = false;
                }
                if (s.shouldBeVisible(null) && isValidWindowingMode) {
                    int position = java.lang.Math.max(0, rootTaskNdx - 1);
                    rootTask.getParent().positionChildAt(position, rootTask, false);
                    return;
                }
            }
        }
    }

    private com.android.server.wm.Task getBottomMostVisibleRootTask(com.android.server.wm.Task excludeRootTask) {
        return getRootTask(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.TaskDisplayArea.lambda$getBottomMostVisibleRootTask$7((com.android.server.wm.Task) obj);
            }
        }, false);
    }

    static /* synthetic */ boolean lambda$getBottomMostVisibleRootTask$7(com.android.server.wm.Task task) {
        int winMode = task.getWindowingMode();
        boolean isValidWindowingMode = winMode == 1;
        return task.shouldBeVisible(null) && isValidWindowingMode;
    }

    void moveRootTaskBehindRootTask(com.android.server.wm.Task rootTask, com.android.server.wm.Task behindRootTask) {
        com.android.server.wm.WindowContainer parent;
        if (behindRootTask == null || behindRootTask == rootTask || (parent = rootTask.getParent()) == null || parent != behindRootTask.getParent()) {
            return;
        }
        int rootTaskIndex = parent.mChildren.indexOf(rootTask);
        int behindRootTaskIndex = parent.mChildren.indexOf(behindRootTask);
        int insertIndex = rootTaskIndex <= behindRootTaskIndex ? behindRootTaskIndex - 1 : behindRootTaskIndex;
        int position = java.lang.Math.max(0, insertIndex);
        parent.positionChildAt(position, rootTask, false);
    }

    boolean hasPinnedTask() {
        return getRootPinnedTask() != null;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    static com.android.server.wm.Task getRootTaskAbove(com.android.server.wm.Task rootTask) {
        int index;
        com.android.server.wm.WindowContainer wc = rootTask.getParent();
        if (wc != null && (index = wc.mChildren.indexOf(rootTask) + 1) < wc.mChildren.size()) {
            return (com.android.server.wm.Task) wc.mChildren.get(index);
        }
        return null;
    }

    boolean isRootTaskVisible(int windowingMode) {
        com.android.server.wm.Task rootTask = getTopRootTaskInWindowingMode(windowingMode);
        return rootTask != null && rootTask.isVisible();
    }

    void removeRootTask(com.android.server.wm.Task rootTask) {
        removeChild(rootTask);
    }

    int getDisplayId() {
        return this.mDisplayContent.getDisplayId();
    }

    boolean isRemoved() {
        return this.mRemoved;
    }

    void registerRootTaskOrderChangedListener(com.android.server.wm.TaskDisplayArea.OnRootTaskOrderChangedListener listener) {
        if (!this.mRootTaskOrderChangedCallbacks.contains(listener)) {
            this.mRootTaskOrderChangedCallbacks.add(listener);
        }
    }

    void unregisterRootTaskOrderChangedListener(com.android.server.wm.TaskDisplayArea.OnRootTaskOrderChangedListener listener) {
        this.mRootTaskOrderChangedCallbacks.remove(listener);
    }

    void onRootTaskOrderChanged(com.android.server.wm.Task rootTask) {
        for (int i = this.mRootTaskOrderChangedCallbacks.size() - 1; i >= 0; i--) {
            this.mRootTaskOrderChangedCallbacks.get(i).onRootTaskOrderChanged(rootTask);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    boolean canCreateRemoteAnimationTarget() {
        return com.android.server.wm.WindowManagerService.sEnableShellTransitions;
    }

    boolean canHostHomeTask() {
        return this.mDisplayContent.isHomeSupported() && this.mCanHostHomeTask;
    }

    void ensureActivitiesVisible(final com.android.server.wm.ActivityRecord starting, final boolean notifyClients) {
        this.mAtmService.mTaskSupervisor.beginActivityVisibilityUpdate();
        try {
            forAllRootTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda7
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.wm.Task) obj).ensureActivitiesVisible(starting, notifyClients);
                }
            });
        } finally {
            this.mAtmService.mTaskSupervisor.endActivityVisibilityUpdate();
        }
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    com.android.server.wm.Task remove() {
        this.mPreferredTopFocusableRootTask = null;
        boolean destroyContentOnRemoval = this.mDisplayContent.shouldDestroyContentOnRemove();
        com.android.server.wm.TaskDisplayArea toDisplayArea = this.mRootWindowContainer.getDefaultTaskDisplayArea();
        int numRootTasks = this.mChildren.size();
        com.android.server.wm.Task lastReparentedRootTask = null;
        int numRootTasks2 = numRootTasks;
        int i = 0;
        while (i < numRootTasks2) {
            com.android.server.wm.WindowContainer child = (com.android.server.wm.WindowContainer) this.mChildren.get(i);
            if (child.asTaskDisplayArea() != null) {
                com.android.server.wm.Task lastReparentedRootTask2 = child.asTaskDisplayArea().remove();
                lastReparentedRootTask = lastReparentedRootTask2;
            } else {
                com.android.server.wm.Task task = ((com.android.server.wm.WindowContainer) this.mChildren.get(i)).asTask();
                if (destroyContentOnRemoval || !task.isActivityTypeStandardOrUndefined() || task.mCreatedByOrganizer) {
                    task.remove(false, "removeTaskDisplayArea");
                } else {
                    com.android.server.wm.WindowContainer launchRoot = toDisplayArea.getLaunchRootTask(task.getWindowingMode(), task.getActivityType(), null, null, 0);
                    task.reparent(launchRoot == null ? toDisplayArea : launchRoot, Integer.MAX_VALUE);
                    boolean keepWindowingMode = launchRoot == null && task.getRequestedOverrideWindowingMode() == 1 && toDisplayArea.getWindowingMode() != 1;
                    if (!keepWindowingMode) {
                        task.setWindowingMode(0);
                    }
                    lastReparentedRootTask = task;
                }
                i -= numRootTasks2 - this.mChildren.size();
                numRootTasks2 = this.mChildren.size();
            }
            i++;
        }
        if (lastReparentedRootTask != null && !lastReparentedRootTask.isRootTask()) {
            lastReparentedRootTask.getRootTask().moveToFront("display-removed");
        }
        this.mRemoved = true;
        return lastReparentedRootTask;
    }

    boolean canSpecifyOrientation(int orientation) {
        return this.mDisplayContent.getOrientationRequestingTaskDisplayArea() == this && !shouldIgnoreOrientationRequest(orientation);
    }

    void clearPreferredTopFocusableRootTask() {
        this.mPreferredTopFocusableRootTask = null;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public void setWindowingMode(int windowingMode) {
        this.mTempConfiguration.setTo(getRequestedOverrideConfiguration());
        android.app.WindowConfiguration tempRequestWindowConfiguration = this.mTempConfiguration.windowConfiguration;
        tempRequestWindowConfiguration.setWindowingMode(windowingMode);
        onRequestedOverrideConfigurationChanged(this.mTempConfiguration);
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.TaskDisplayArea getTaskDisplayArea() {
        return this;
    }

    @Override // com.android.server.wm.DisplayArea
    boolean isTaskDisplayArea() {
        return true;
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.TaskDisplayArea asTaskDisplayArea() {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    public void dump(java.io.PrintWriter pw, java.lang.String prefix, boolean dumpAll) {
        pw.println(prefix + "TaskDisplayArea " + getName());
        java.lang.String doublePrefix = prefix + "  ";
        super.dump(pw, doublePrefix, dumpAll);
        if (this.mPreferredTopFocusableRootTask != null) {
            pw.println(doublePrefix + "mPreferredTopFocusableRootTask=" + this.mPreferredTopFocusableRootTask);
        }
        if (this.mLastFocusedRootTask != null) {
            pw.println(doublePrefix + "mLastFocusedRootTask=" + this.mLastFocusedRootTask);
        }
        java.lang.String triplePrefix = doublePrefix + "  ";
        if (this.mLaunchRootTasks.size() > 0) {
            pw.println(doublePrefix + "mLaunchRootTasks:");
            for (int i = this.mLaunchRootTasks.size() - 1; i >= 0; i--) {
                com.android.server.wm.TaskDisplayArea.LaunchRootTaskDef def = this.mLaunchRootTasks.get(i);
                pw.println(triplePrefix + java.util.Arrays.toString(def.activityTypes) + " " + java.util.Arrays.toString(def.windowingModes) + "  task=" + def.task);
            }
        }
        pw.println(doublePrefix + "Application tokens in top down Z order:");
        for (int index = getChildCount() - 1; index >= 0; index--) {
            com.android.server.wm.WindowContainer child = getChildAt(index);
            if (child.asTaskDisplayArea() != null) {
                child.dump(pw, doublePrefix, dumpAll);
            } else {
                com.android.server.wm.Task rootTask = child.asTask();
                pw.println(doublePrefix + "* " + rootTask.toFullString());
                rootTask.dump(pw, triplePrefix, dumpAll);
            }
        }
    }
}
