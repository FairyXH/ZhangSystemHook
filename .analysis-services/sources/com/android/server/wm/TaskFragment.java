package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class TaskFragment extends com.android.server.wm.WindowContainer<com.android.server.wm.WindowContainer> {
    static final int EMBEDDED_DIM_AREA_PARENT_TASK = 1;
    static final int EMBEDDED_DIM_AREA_TASK_FRAGMENT = 0;
    static final int EMBEDDING_ALLOWED = 0;
    static final int EMBEDDING_DISALLOWED_MIN_DIMENSION_VIOLATION = 2;
    static final int EMBEDDING_DISALLOWED_NEW_TASK = 3;
    static final int EMBEDDING_DISALLOWED_UNTRUSTED_HOST = 1;
    static final int FLAG_FORCE_HIDDEN_FOR_PINNED_TASK = 1;
    static final int FLAG_FORCE_HIDDEN_FOR_TASK_FRAGMENT_ORG = 4;
    static final int FLAG_FORCE_HIDDEN_FOR_TASK_ORG = 2;
    static final int INVALID_MIN_SIZE = -1;
    static final boolean SHOW_APP_STARTING_PREVIEW = true;
    static final int TASK_FRAGMENT_VISIBILITY_INVISIBLE = 2;
    static final int TASK_FRAGMENT_VISIBILITY_VISIBLE = 0;
    static final int TASK_FRAGMENT_VISIBILITY_VISIBLE_BEHIND_TRANSLUCENT = 1;
    private com.android.server.wm.TaskFragment mAdjacentTaskFragment;
    private boolean mAllowTransitionWhenEmpty;
    private android.window.TaskFragmentAnimationParams mAnimationParams;
    final com.android.server.wm.ActivityTaskManagerService mAtmService;
    boolean mClearedForReorderActivityToFront;
    boolean mClearedTaskForReuse;
    boolean mClearedTaskFragmentForPip;
    private com.android.server.wm.TaskFragment mCompanionTaskFragment;
    boolean mCreatedByOrganizer;
    private boolean mDelayLastActivityRemoval;
    private boolean mDelayOrganizedTaskFragmentSurfaceUpdate;
    com.android.server.wm.Dimmer mDimmer;
    private int mEmbeddedDimArea;
    private final com.android.server.wm.EnsureActivitiesVisibleHelper mEnsureActivitiesVisibleHelper;
    protected int mForceHiddenFlags;
    private boolean mForceTranslucent;
    private final android.os.IBinder mFragmentToken;
    private final boolean mIsEmbedded;
    private boolean mIsRemovalRequested;
    private boolean mIsolatedNav;
    com.android.server.wm.ActivityRecord mLastPausedActivity;
    final android.graphics.Point mLastSurfaceSize;
    int mMinHeight;
    int mMinWidth;
    private boolean mMoveToBottomIfClearWhenLaunch;
    private com.android.server.wm.ActivityRecord mPausingActivity;
    private boolean mPinned;
    private final android.graphics.Rect mRelativeEmbeddedBounds;
    private com.android.server.wm.ActivityRecord mResumedActivity;
    final com.android.server.wm.RootWindowContainer mRootWindowContainer;
    boolean mTaskFragmentAppearedSent;
    public com.android.server.wm.ITaskFragmentExt mTaskFragmentExt;
    private android.window.ITaskFragmentOrganizer mTaskFragmentOrganizer;
    private final com.android.server.wm.TaskFragmentOrganizerController mTaskFragmentOrganizerController;
    java.lang.String mTaskFragmentOrganizerProcessName;
    int mTaskFragmentOrganizerUid;
    public com.android.server.wm.ITaskFragmentSocExt mTaskFragmentSocExt;
    boolean mTaskFragmentVanishedSent;
    final com.android.server.wm.ActivityTaskSupervisor mTaskSupervisor;
    private final android.graphics.Rect mTmpAbsBounds;
    private final android.graphics.Rect mTmpBounds;
    private final android.graphics.Rect mTmpFullBounds;
    private final android.graphics.Rect mTmpNonDecorBounds;
    private final android.graphics.Rect mTmpStableBounds;
    private static final java.lang.String TAG = "ActivityTaskManager";
    private static final java.lang.String TAG_SWITCH = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_SWITCH;
    private static final java.lang.String TAG_RESULTS = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_RESULTS;
    private static final java.lang.String TAG_TRANSITION = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_TRANSITION;

    @interface EmbeddedDimArea {
    }

    @interface EmbeddingCheckResult {
    }

    @interface FlagForceHidden {
    }

    @interface TaskFragmentVisibility {
    }

    TaskFragment(com.android.server.wm.ActivityTaskManagerService atmService, android.os.IBinder fragmentToken, boolean createdByOrganizer) {
        this(atmService, fragmentToken, createdByOrganizer, true);
    }

    TaskFragment(com.android.server.wm.ActivityTaskManagerService atmService, android.os.IBinder fragmentToken, boolean createdByOrganizer, boolean isEmbedded) {
        super(atmService.mWindowManager);
        this.mTaskFragmentSocExt = (com.android.server.wm.ITaskFragmentSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ITaskFragmentSocExt.class).base(this).create();
        this.mDimmer = com.android.server.wm.Dimmer.DIMMER_REFACTOR ? new com.android.server.wm.SmoothDimmer(this) : new com.android.server.wm.LegacyDimmer(this);
        this.mEmbeddedDimArea = 0;
        this.mPausingActivity = null;
        this.mLastPausedActivity = null;
        this.mResumedActivity = null;
        this.mTaskFragmentOrganizerUid = -1;
        this.mAnimationParams = android.window.TaskFragmentAnimationParams.DEFAULT;
        this.mForceHiddenFlags = 0;
        this.mForceTranslucent = false;
        this.mLastSurfaceSize = new android.graphics.Point();
        this.mTmpBounds = new android.graphics.Rect();
        this.mTmpAbsBounds = new android.graphics.Rect();
        this.mTmpFullBounds = new android.graphics.Rect();
        this.mTmpStableBounds = new android.graphics.Rect();
        this.mTmpNonDecorBounds = new android.graphics.Rect();
        this.mEnsureActivitiesVisibleHelper = new com.android.server.wm.EnsureActivitiesVisibleHelper(this);
        this.mAtmService = atmService;
        this.mTaskSupervisor = this.mAtmService.mTaskSupervisor;
        this.mRootWindowContainer = this.mAtmService.mRootWindowContainer;
        this.mCreatedByOrganizer = createdByOrganizer;
        this.mIsEmbedded = isEmbedded;
        this.mRelativeEmbeddedBounds = isEmbedded ? new android.graphics.Rect() : null;
        this.mTaskFragmentOrganizerController = this.mAtmService.mWindowOrganizerController.mTaskFragmentOrganizerController;
        this.mFragmentToken = fragmentToken;
        this.mRemoteToken = new com.android.server.wm.WindowContainer.RemoteToken(this);
        this.mTaskFragmentExt = (com.android.server.wm.ITaskFragmentExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ITaskFragmentExt.class).base(this).create();
    }

    static com.android.server.wm.TaskFragment fromTaskFragmentToken(android.os.IBinder token, com.android.server.wm.ActivityTaskManagerService service) {
        if (token == null) {
            return null;
        }
        return service.mWindowOrganizerController.getTaskFragment(token);
    }

    void setAdjacentTaskFragment(com.android.server.wm.TaskFragment taskFragment) {
        if (this.mAdjacentTaskFragment == taskFragment) {
            return;
        }
        resetAdjacentTaskFragment();
        if (taskFragment != null) {
            this.mAdjacentTaskFragment = taskFragment;
            taskFragment.setAdjacentTaskFragment(this);
        }
    }

    void setCompanionTaskFragment(com.android.server.wm.TaskFragment companionTaskFragment) {
        this.mCompanionTaskFragment = companionTaskFragment;
    }

    com.android.server.wm.TaskFragment getCompanionTaskFragment() {
        return this.mCompanionTaskFragment;
    }

    void resetAdjacentTaskFragment() {
        if (this.mAdjacentTaskFragment != null && this.mAdjacentTaskFragment.mAdjacentTaskFragment == this) {
            this.mAdjacentTaskFragment.mAdjacentTaskFragment = null;
            this.mAdjacentTaskFragment.mDelayLastActivityRemoval = false;
        }
        this.mAdjacentTaskFragment = null;
        this.mDelayLastActivityRemoval = false;
    }

    void setTaskFragmentOrganizer(android.window.TaskFragmentOrganizerToken organizer, int uid, java.lang.String processName) {
        this.mTaskFragmentOrganizer = android.window.ITaskFragmentOrganizer.Stub.asInterface(organizer.asBinder());
        this.mTaskFragmentOrganizerUid = uid;
        this.mTaskFragmentOrganizerProcessName = processName;
    }

    void onTaskFragmentOrganizerRemoved() {
        this.mTaskFragmentOrganizer = null;
    }

    boolean hasTaskFragmentOrganizer(android.window.ITaskFragmentOrganizer organizer) {
        return (organizer == null || this.mTaskFragmentOrganizer == null || !organizer.asBinder().equals(this.mTaskFragmentOrganizer.asBinder())) ? false : true;
    }

    private com.android.server.wm.WindowProcessController getOrganizerProcessIfDifferent(com.android.server.wm.ActivityRecord r) {
        com.android.server.wm.Task task = getTask();
        if (r == null || task == null || task.mTaskFragmentHostProcessName == null) {
            return null;
        }
        if (task.mTaskFragmentHostProcessName.equals(r.processName) && task.mTaskFragmentHostUid == r.getUid()) {
            return null;
        }
        return this.mAtmService.getProcessController(task.mTaskFragmentHostProcessName, task.mTaskFragmentHostUid);
    }

    void setAnimationParams(android.window.TaskFragmentAnimationParams animationParams) {
        this.mAnimationParams = animationParams;
    }

    android.window.TaskFragmentAnimationParams getAnimationParams() {
        return this.mAnimationParams;
    }

    void setIsolatedNav(boolean isolatedNav) {
        if (!isEmbedded()) {
            return;
        }
        this.mIsolatedNav = isolatedNav;
    }

    void setPinned(boolean pinned) {
        if (!isEmbedded()) {
            return;
        }
        this.mPinned = pinned;
    }

    void setAllowTransitionWhenEmpty(boolean allowTransitionWhenEmpty) {
        if (!isEmbedded()) {
            return;
        }
        this.mAllowTransitionWhenEmpty = allowTransitionWhenEmpty;
    }

    boolean isIsolatedNav() {
        return isEmbedded() && this.mIsolatedNav;
    }

    boolean isPinned() {
        return isEmbedded() && this.mPinned;
    }

    com.android.server.wm.TaskFragment getAdjacentTaskFragment() {
        return this.mAdjacentTaskFragment;
    }

    com.android.server.wm.ActivityRecord getTopResumedActivity() {
        com.android.server.wm.WindowContainer<?> taskFragResumedActivity = getResumedActivity();
        for (int i = getChildCount() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer<?> child = getChildAt(i);
            com.android.server.wm.ActivityRecord topResumedActivity = null;
            if (taskFragResumedActivity != null && child == taskFragResumedActivity) {
                topResumedActivity = child.asActivityRecord();
            } else if (child.asTaskFragment() != null) {
                topResumedActivity = child.asTaskFragment().getTopResumedActivity();
            }
            if (topResumedActivity != null) {
                return topResumedActivity;
            }
        }
        return null;
    }

    com.android.server.wm.ActivityRecord getResumedActivity() {
        return this.mResumedActivity;
    }

    void setResumedActivity(com.android.server.wm.ActivityRecord r, java.lang.String reason) {
        if (this.mResumedActivity == r && r != null && !r.getWrapper().getExtImpl().updateActvityState(r)) {
            return;
        }
        this.mTaskFragmentExt.setForeAppInfo(r);
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_ROOT_TASK) {
            android.util.Slog.d(TAG, "setResumedActivity taskFrag:" + this + " + from: " + this.mResumedActivity + " to:" + r + " reason:" + reason);
        }
        if (r != null && this.mResumedActivity == null) {
            getTask().touchActiveTime();
        }
        this.mTaskFragmentExt.handleActivityResumed(r, getTask());
        com.android.server.wm.ActivityRecord prevR = this.mResumedActivity;
        this.mResumedActivity = r;
        this.mTaskFragmentExt.updateWaitActivityToAttachIfNeeded(this.mResumedActivity, prevR);
        com.android.server.wm.ActivityRecord topResumed = this.mTaskSupervisor.updateTopResumedActivityIfNeeded(reason);
        if (this.mResumedActivity != null && topResumed != null && topResumed.isEmbedded() && topResumed.getTaskFragment().getAdjacentTaskFragment() == this) {
            this.mAtmService.setLastResumedActivityUncheckLocked(this.mResumedActivity, reason);
        }
        if (r == null && prevR != null && prevR.mDisplayContent != null && prevR.mDisplayContent.getFocusedRootTask() == null) {
            prevR.mDisplayContent.onRunningActivityChanged();
        } else if (r != null) {
            r.mDisplayContent.onRunningActivityChanged();
        }
    }

    void setPausingActivity(com.android.server.wm.ActivityRecord pausing) {
        this.mPausingActivity = pausing;
    }

    com.android.server.wm.ActivityRecord getTopPausingActivity() {
        com.android.server.wm.WindowContainer<?> taskFragPausingActivity = getPausingActivity();
        for (int i = getChildCount() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer<?> child = getChildAt(i);
            com.android.server.wm.ActivityRecord topPausingActivity = null;
            if (taskFragPausingActivity != null && child == taskFragPausingActivity) {
                topPausingActivity = child.asActivityRecord();
            } else if (child.asTaskFragment() != null) {
                topPausingActivity = child.asTaskFragment().getTopPausingActivity();
            }
            if (topPausingActivity != null) {
                return topPausingActivity;
            }
        }
        return null;
    }

    com.android.server.wm.ActivityRecord getPausingActivity() {
        return this.mPausingActivity;
    }

    int getDisplayId() {
        com.android.server.wm.DisplayContent dc = getDisplayContent();
        if (dc != null) {
            return dc.mDisplayId;
        }
        return -1;
    }

    com.android.server.wm.Task getTask() {
        if (asTask() != null) {
            return asTask();
        }
        com.android.server.wm.TaskFragment parent = getParent() != null ? getParent().asTaskFragment() : null;
        if (parent != null) {
            return parent.getTask();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public com.android.server.wm.TaskDisplayArea getDisplayArea() {
        return (com.android.server.wm.TaskDisplayArea) super.getDisplayArea();
    }

    @Override // com.android.server.wm.WindowContainer
    public boolean isAttached() {
        com.android.server.wm.TaskDisplayArea taskDisplayArea = getDisplayArea();
        return (taskDisplayArea == null || taskDisplayArea.isRemoved()) ? false : true;
    }

    com.android.server.wm.TaskFragment getRootTaskFragment() {
        com.android.server.wm.TaskFragment parentTaskFragment;
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent != null && (parentTaskFragment = parent.asTaskFragment()) != null) {
            return parentTaskFragment.getRootTaskFragment();
        }
        return this;
    }

    com.android.server.wm.Task getRootTask() {
        return getRootTaskFragment().asTask();
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.TaskFragment asTaskFragment() {
        return this;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isEmbedded() {
        return this.mIsEmbedded;
    }

    int isAllowedToEmbedActivity(com.android.server.wm.ActivityRecord a) {
        return isAllowedToEmbedActivity(a, this.mTaskFragmentOrganizerUid);
    }

    int isAllowedToEmbedActivity(com.android.server.wm.ActivityRecord a, int uid) {
        if (!this.mTaskFragmentExt.isAllowedToEmbedActivity(a, uid)) {
            return 1;
        }
        if (!isAllowedToEmbedActivityInUntrustedMode(a) && !isAllowedToEmbedActivityInTrustedMode(a, uid)) {
            return 1;
        }
        if (smallerThanMinDimension(a)) {
            return 2;
        }
        return 0;
    }

    boolean smallerThanMinDimension(com.android.server.wm.ActivityRecord activity) {
        android.graphics.Point minDimensions;
        android.graphics.Rect taskFragBounds = getBounds();
        com.android.server.wm.Task task = getTask();
        if (task == null || taskFragBounds.equals(task.getBounds()) || (minDimensions = activity.getMinDimensions()) == null) {
            return false;
        }
        int minWidth = minDimensions.x;
        int minHeight = minDimensions.y;
        return taskFragBounds.width() < minWidth || taskFragBounds.height() < minHeight;
    }

    boolean isAllowedToEmbedActivityInUntrustedMode(com.android.server.wm.ActivityRecord a) {
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent == null || !parent.getBounds().contains(getBounds())) {
            return false;
        }
        return hasEmbedAnyAppInUntrustedModePermission(this.mTaskFragmentOrganizerUid) || (a.info.flags & 268435456) == 268435456;
    }

    boolean isAllowedToEmbedActivityInTrustedMode(com.android.server.wm.ActivityRecord a) {
        return isAllowedToEmbedActivityInTrustedMode(a, this.mTaskFragmentOrganizerUid);
    }

    boolean isAllowedToEmbedActivityInTrustedMode(com.android.server.wm.ActivityRecord a, int uid) {
        if (isFullyTrustedEmbedding(a, uid)) {
            return true;
        }
        com.android.server.pm.pkg.AndroidPackage hostPackage = this.mAtmService.getPackageManagerInternalLocked().getPackage(uid);
        return hostPackage != null && isAllowedToEmbedActivityInTrustedModeByHostPackage(a, hostPackage);
    }

    boolean isAllowedToEmbedActivityInTrustedModeByHostPackage(com.android.server.wm.ActivityRecord a, com.android.server.pm.pkg.AndroidPackage hostPackage) {
        java.util.Set<java.lang.String> knownActivityEmbeddingCerts = a.info.getKnownActivityEmbeddingCerts();
        if (knownActivityEmbeddingCerts.isEmpty()) {
            knownActivityEmbeddingCerts = a.info.applicationInfo.getKnownActivityEmbeddingCerts();
        }
        return hostPackage.getSigningDetails().hasAncestorOrSelfWithDigest(knownActivityEmbeddingCerts);
    }

    private static boolean isFullyTrustedEmbedding(com.android.server.wm.ActivityRecord a, int uid) {
        return android.os.UserHandle.getAppId(uid) == 1000 || a.isUid(uid) || hasManageTaskPermission(uid);
    }

    private static boolean hasManageTaskPermission(int uid) {
        return com.android.server.wm.ActivityTaskManagerService.checkPermission("android.permission.MANAGE_ACTIVITY_TASKS", -1, uid) == 0;
    }

    private static boolean hasEmbedAnyAppInUntrustedModePermission(int uid) {
        return com.android.window.flags.Flags.untrustedEmbeddingAnyAppPermission() && com.android.server.wm.ActivityTaskManagerService.checkPermission("android.permission.EMBED_ANY_APP_IN_UNTRUSTED_MODE", -1, uid) == 0;
    }

    static /* synthetic */ boolean lambda$isFullyTrustedEmbedding$0(int uid, com.android.server.wm.ActivityRecord r) {
        return !isFullyTrustedEmbedding(r, uid);
    }

    boolean isFullyTrustedEmbedding(final int uid) {
        return !forAllActivities(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskFragment$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.TaskFragment.lambda$isFullyTrustedEmbedding$0(uid, (com.android.server.wm.ActivityRecord) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$isAllowedToBeEmbeddedInTrustedMode$1(com.android.server.wm.ActivityRecord r) {
        return !isAllowedToEmbedActivityInTrustedMode(r);
    }

    boolean isAllowedToBeEmbeddedInTrustedMode() {
        return !forAllActivities(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskFragment$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$isAllowedToBeEmbeddedInTrustedMode$1((com.android.server.wm.ActivityRecord) obj);
            }
        });
    }

    com.android.server.wm.TaskFragment getOrganizedTaskFragment() {
        if (this.mTaskFragmentOrganizer != null) {
            return this;
        }
        com.android.server.wm.TaskFragment parentTaskFragment = getParent() != null ? getParent().asTaskFragment() : null;
        if (parentTaskFragment != null) {
            return parentTaskFragment.getOrganizedTaskFragment();
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
    boolean hasDirectChildActivities() {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            if (((com.android.server.wm.WindowContainer) this.mChildren.get(i)).asActivityRecord() != null) {
                return true;
            }
        }
        return false;
    }

    void cleanUpActivityReferences(com.android.server.wm.ActivityRecord r) {
        if (this.mPausingActivity != null && this.mPausingActivity == r) {
            this.mPausingActivity = null;
        }
        if (this.mResumedActivity != null && this.mResumedActivity == r) {
            setResumedActivity(null, "cleanUpActivityReferences");
        }
        r.removeTimeouts();
    }

    protected boolean isForceHidden() {
        return this.mForceHiddenFlags != 0;
    }

    boolean setForceHidden(int flags, boolean set) {
        int newFlags;
        int newFlags2 = this.mForceHiddenFlags;
        if (set) {
            newFlags = newFlags2 | flags;
        } else {
            newFlags = newFlags2 & (~flags);
        }
        if (this.mForceHiddenFlags == newFlags) {
            return false;
        }
        this.mForceHiddenFlags = newFlags;
        return true;
    }

    boolean isForceTranslucent() {
        return this.mForceTranslucent;
    }

    boolean setForceTranslucent(boolean set) {
        if (this.mForceTranslucent == set) {
            return false;
        }
        this.mForceTranslucent = set;
        return true;
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
    boolean isLeafTaskFragment() {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            if (((com.android.server.wm.WindowContainer) this.mChildren.get(i)).asTaskFragment() != null) {
                return false;
            }
        }
        return true;
    }

    void onActivityStateChanged(com.android.server.wm.ActivityRecord record, com.android.server.wm.ActivityRecord.State state, java.lang.String reason) {
        if (record == this.mResumedActivity && state != com.android.server.wm.ActivityRecord.State.RESUMED) {
            setResumedActivity(null, reason + " - onActivityStateChanged");
        }
        if (state == com.android.server.wm.ActivityRecord.State.RESUMED) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_ROOT_TASK) {
                android.util.Slog.v(TAG, "set resumed activity to:" + record + " reason:" + reason);
            }
            setResumedActivity(record, reason + " - onActivityStateChanged");
            this.mTaskFragmentExt.setPreloadTaskFocusedApp(getDisplayContent(), record);
            this.mTaskSupervisor.mRecentTasks.add(record.getTask());
        }
        this.mTaskFragmentExt.onRealActivityStateChanged(record, state);
        com.android.server.wm.WindowProcessController hostProcess = getOrganizerProcessIfDifferent(record);
        if (hostProcess != null) {
            this.mTaskSupervisor.onProcessActivityStateChanged(hostProcess, false);
            hostProcess.updateProcessInfo(false, true, true, false);
        }
    }

    boolean handleAppDied(com.android.server.wm.WindowProcessController app) {
        boolean isPausingDied = false;
        if (this.mPausingActivity != null && this.mPausingActivity.app == app) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mPausingActivity);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 646076184396185067L, 0, null, protoLogParam0);
            }
            this.mPausingActivity = null;
            isPausingDied = true;
        }
        if (this.mLastPausedActivity != null && this.mLastPausedActivity.app == app) {
            this.mLastPausedActivity = null;
        }
        return isPausingDied;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void awakeFromSleeping() {
        if (this.mPausingActivity != null) {
            android.util.Slog.d(TAG, "awakeFromSleeping: previously pausing activity didn't pause");
            this.mPausingActivity.activityPaused(true);
        }
    }

    boolean sleepIfPossible(boolean shuttingDown) {
        boolean shouldSleep = true;
        if (this.mResumedActivity != null && (!isEmbedded() || this.mPausingActivity == null || asTask() != null)) {
            if (!shuttingDown && this.mResumedActivity.canTurnScreenOn()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mResumedActivity);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -7596917112222697106L, 0, null, protoLogParam0);
                }
            } else {
                if (this.mTaskFragmentExt.isCompactMode(getWindowingMode())) {
                    this.mPausingActivity = null;
                }
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(this.mResumedActivity);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -8472961767591168851L, 0, null, protoLogParam02);
                }
                if (this.mPausingActivity == null) {
                    startPausing(true, null, "sleep");
                }
            }
            shouldSleep = false;
        } else if (this.mPausingActivity != null) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                java.lang.String protoLogParam03 = java.lang.String.valueOf(this.mPausingActivity);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -1472885369931482317L, 0, null, protoLogParam03);
            }
            shouldSleep = false;
        }
        if (!shuttingDown && containsStoppingActivity()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                long protoLogParam04 = this.mTaskSupervisor.mStoppingActivities.size();
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -2693016397674039814L, 1, null, java.lang.Long.valueOf(protoLogParam04));
            }
            this.mTaskSupervisor.scheduleIdle();
            shouldSleep = false;
        }
        if (shouldSleep) {
            updateActivityVisibilities(null, true);
        }
        return shouldSleep;
    }

    private boolean containsStoppingActivity() {
        for (int i = this.mTaskSupervisor.mStoppingActivities.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord r = this.mTaskSupervisor.mStoppingActivities.get(i);
            if (r.getTaskFragment() == this) {
                return true;
            }
        }
        return false;
    }

    boolean isTranslucent(com.android.server.wm.ActivityRecord starting) {
        return !isAttached() || isForceHidden() || isForceTranslucent() || this.mTaskSupervisor.mOpaqueActivityHelper.getVisibleOpaqueActivity(this, starting, true) == null;
    }

    boolean isTranslucentForTransition() {
        return !isAttached() || isForceHidden() || isForceTranslucent() || this.mTaskSupervisor.mOpaqueActivityHelper.getOpaqueActivity(this, true) == null;
    }

    boolean isTranslucentAndVisible() {
        return !isAttached() || isForceHidden() || isForceTranslucent() || this.mTaskSupervisor.mOpaqueActivityHelper.getVisibleOpaqueActivity(this, null, false) == null;
    }

    com.android.server.wm.ActivityRecord getTopNonFinishingActivity() {
        return getTopNonFinishingActivity(true, true);
    }

    com.android.server.wm.ActivityRecord getTopNonFinishingActivity(boolean includeOverlays, boolean includeLaunchedFromBubble) {
        if (includeOverlays) {
            if (includeLaunchedFromBubble) {
                return getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskFragment$$ExternalSyntheticLambda2
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.wm.TaskFragment.lambda$getTopNonFinishingActivity$2((com.android.server.wm.ActivityRecord) obj);
                    }
                });
            }
            return getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskFragment$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.TaskFragment.lambda$getTopNonFinishingActivity$3((com.android.server.wm.ActivityRecord) obj);
                }
            });
        }
        if (includeLaunchedFromBubble) {
            return getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskFragment$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.TaskFragment.lambda$getTopNonFinishingActivity$4((com.android.server.wm.ActivityRecord) obj);
                }
            });
        }
        return getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskFragment$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.TaskFragment.lambda$getTopNonFinishingActivity$5((com.android.server.wm.ActivityRecord) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$getTopNonFinishingActivity$2(com.android.server.wm.ActivityRecord r) {
        return !r.finishing;
    }

    static /* synthetic */ boolean lambda$getTopNonFinishingActivity$3(com.android.server.wm.ActivityRecord r) {
        return (r.finishing || r.getLaunchedFromBubble()) ? false : true;
    }

    static /* synthetic */ boolean lambda$getTopNonFinishingActivity$4(com.android.server.wm.ActivityRecord r) {
        return (r.finishing || r.isTaskOverlay()) ? false : true;
    }

    static /* synthetic */ boolean lambda$getTopNonFinishingActivity$5(com.android.server.wm.ActivityRecord r) {
        return (r.finishing || r.isTaskOverlay() || r.getLaunchedFromBubble()) ? false : true;
    }

    com.android.server.wm.ActivityRecord topRunningActivity() {
        return topRunningActivity(false);
    }

    com.android.server.wm.ActivityRecord topRunningActivity(boolean focusableOnly) {
        if (focusableOnly) {
            return getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskFragment$$ExternalSyntheticLambda10
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.TaskFragment.lambda$topRunningActivity$6((com.android.server.wm.ActivityRecord) obj);
                }
            });
        }
        return getActivity(new com.android.server.wm.ActivityStarter$$ExternalSyntheticLambda0());
    }

    static /* synthetic */ boolean lambda$topRunningActivity$6(com.android.server.wm.ActivityRecord r) {
        return r.canBeTopRunning() && r.isFocusable();
    }

    int getNonFinishingActivityCount() {
        final int[] runningActivityCount = new int[1];
        forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.TaskFragment$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.TaskFragment.lambda$getNonFinishingActivityCount$7(runningActivityCount, (com.android.server.wm.ActivityRecord) obj);
            }
        });
        return runningActivityCount[0];
    }

    static /* synthetic */ void lambda$getNonFinishingActivityCount$7(int[] runningActivityCount, com.android.server.wm.ActivityRecord a) {
        if (!a.finishing) {
            runningActivityCount[0] = runningActivityCount[0] + 1;
        }
    }

    boolean hasNonFinishingDirectActivity() {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord activity = getChildAt(i).asActivityRecord();
            if (activity != null && !activity.finishing) {
                return true;
            }
        }
        return false;
    }

    boolean isTopActivityFocusable() {
        com.android.server.wm.ActivityRecord r = topRunningActivity();
        return r != null ? r.isFocusable() : isFocusable() && getWindowConfiguration().canReceiveKeys();
    }

    /* JADX WARN: Code restructure failed: missing block: B:126:0x01a0, code lost:
    
        if (r8 != false) goto L128;
     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x01a2, code lost:
    
        return r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x01a3, code lost:
    
        if (r6 == false) goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x01a7, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:?, code lost:
    
        return r3;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    int getVisibility(com.android.server.wm.ActivityRecord r18) {
        /*
            Method dump skipped, instruction units count: 427
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.TaskFragment.getVisibility(com.android.server.wm.ActivityRecord):int");
    }

    private static boolean hasRunningActivity(com.android.server.wm.WindowContainer wc) {
        return wc.asTaskFragment() != null ? wc.asTaskFragment().topRunningActivity() != null : (wc.asActivityRecord() == null || wc.asActivityRecord().finishing) ? false : true;
    }

    private static boolean isTranslucent(com.android.server.wm.WindowContainer wc, com.android.server.wm.ActivityRecord starting) {
        if (wc.asTaskFragment() != null) {
            return wc.asTaskFragment().isTranslucent(starting) && !wc.asTaskFragment().mTaskFragmentExt.isTranslucentSplitTask(starting);
        }
        if (wc.asActivityRecord() != null) {
            return !wc.asActivityRecord().occludesParent();
        }
        return false;
    }

    private boolean isTopActivityLaunchedBehind() {
        com.android.server.wm.ActivityRecord top = topRunningActivity();
        return top != null && top.mLaunchTaskBehind;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void updateActivityVisibilities(com.android.server.wm.ActivityRecord starting, boolean notifyClients) {
        this.mTaskSupervisor.beginActivityVisibilityUpdate();
        try {
            this.mEnsureActivitiesVisibleHelper.process(starting, notifyClients);
        } finally {
            this.mTaskSupervisor.endActivityVisibilityUpdate();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:275:0x066d  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x0695  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x06b4  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x06b9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    final boolean resumeTopActivity(com.android.server.wm.ActivityRecord r30, android.app.ActivityOptions r31, boolean r32) {
        /*
            Method dump skipped, instruction units count: 1841
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.TaskFragment.resumeTopActivity(com.android.server.wm.ActivityRecord, android.app.ActivityOptions, boolean):boolean");
    }

    boolean shouldSleepOrShutDownActivities() {
        return shouldSleepActivities() || this.mAtmService.mShuttingDown;
    }

    boolean shouldBeVisible(com.android.server.wm.ActivityRecord starting) {
        return getVisibility(starting) != 2;
    }

    boolean canBeResumed(com.android.server.wm.ActivityRecord starting) {
        return isTopActivityFocusable() && getVisibility(starting) == 0;
    }

    boolean isFocusableAndVisible() {
        return isTopActivityFocusable() && shouldBeVisible(null);
    }

    final boolean startPausing(boolean uiSleeping, com.android.server.wm.ActivityRecord resuming, java.lang.String reason) {
        return startPausing(this.mTaskSupervisor.mUserLeaving, uiSleeping, resuming, reason);
    }

    /* JADX WARN: Removed duplicated region for block: B:53:0x011d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean startPausing(boolean r21, boolean r22, com.android.server.wm.ActivityRecord r23, java.lang.String r24) {
        /*
            Method dump skipped, instruction units count: 555
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.TaskFragment.startPausing(boolean, boolean, com.android.server.wm.ActivityRecord, java.lang.String):boolean");
    }

    void schedulePauseActivity(com.android.server.wm.ActivityRecord prev, boolean userLeaving, boolean pauseImmediately, boolean autoEnteringPip, java.lang.String reason) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(prev);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 1917394294249960915L, 0, null, protoLogParam0);
        }
        try {
            this.mTaskFragmentExt.hookSetBinderUxFlag(true, prev);
            prev.mPauseSchedulePendingForPip = false;
            com.android.server.wm.EventLogTags.writeWmPauseActivity(prev.mUserId, java.lang.System.identityHashCode(prev), prev.shortComponentName, "userLeaving=" + userLeaving, reason);
            android.os.Trace.traceBegin(32L, "cmz.mtk.schedulePauseActivity.activityPaused");
            this.mAtmService.mSocExt.onActivityStateChanged(prev, false);
            android.os.Trace.traceEnd(32L);
            this.mAtmService.getLifecycleManager().scheduleTransactionItem(prev.app.getThread(), android.app.servertransaction.PauseActivityItem.obtain(prev.token, prev.finishing, userLeaving, pauseImmediately, autoEnteringPip));
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Exception thrown during pause", e);
            this.mPausingActivity = null;
            this.mLastPausedActivity = null;
            this.mTaskSupervisor.mNoHistoryActivities.remove(prev);
        }
        this.mTaskFragmentExt.hookSetBinderUxFlag(false, prev);
    }

    void completePause(boolean resumeNext, com.android.server.wm.ActivityRecord resuming) {
        boolean canResumeImmediately = !this.mTaskFragmentExt.shouldDeferResumeUntilRecentsAnimFinished(this.mPausingActivity, resuming);
        boolean resumeNext2 = resumeNext & canResumeImmediately;
        com.android.server.wm.ActivityRecord prev = this.mPausingActivity;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(prev);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -8936154984341817384L, 0, null, protoLogParam0);
        }
        if (prev != null) {
            prev.setWillCloseOrEnterPip(false);
            boolean wasStopping = prev.isState(com.android.server.wm.ActivityRecord.State.STOPPING);
            prev.setState(com.android.server.wm.ActivityRecord.State.PAUSED, "completePausedLocked");
            this.mPausingActivity = null;
            if (prev.finishing) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(prev);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 4971958459026584561L, 0, null, protoLogParam02);
                }
                prev = prev.completeFinishing(false, "completePausedLocked");
            } else if (prev.attachedToProcess()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                    java.lang.String protoLogParam03 = java.lang.String.valueOf(prev);
                    boolean protoLogParam2 = prev.isVisibleRequested();
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -7113165071559345173L, 60, null, protoLogParam03, java.lang.Boolean.valueOf(wasStopping), java.lang.Boolean.valueOf(protoLogParam2));
                }
                if (wasStopping) {
                    prev.setState(com.android.server.wm.ActivityRecord.State.STOPPING, "completePausedLocked");
                } else if (!prev.isVisibleRequested() || shouldSleepOrShutDownActivities()) {
                    prev.setDeferHidingClient(false);
                    prev.addToStopping(true, false, "completePauseLocked");
                }
            } else {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                    java.lang.String protoLogParam04 = java.lang.String.valueOf(prev);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -3777748052684097788L, 0, null, protoLogParam04);
                }
                prev = null;
            }
            if (prev != null) {
                prev.stopFreezingScreen(true, true);
            }
        }
        if (resumeNext2) {
            this.mTaskFragmentExt.ensureVisibilityAndConfigIfPocketStudioExiting(prev);
            com.android.server.wm.Task topRootTask = this.mRootWindowContainer.getTopDisplayFocusedRootTask();
            if (topRootTask != null && !topRootTask.shouldSleepOrShutDownActivities()) {
                this.mRootWindowContainer.resumeFocusedTasksTopActivities(topRootTask, prev, null);
                setAppTransitionReadyInAdvance(this.mDisplayContent, topRootTask.topRunningActivity());
            } else {
                com.android.server.wm.ActivityRecord top = topRootTask != null ? topRootTask.topRunningActivity() : null;
                if (top == null || (prev != null && top != prev)) {
                    this.mRootWindowContainer.resumeFocusedTasksTopActivities();
                }
            }
        }
        if (prev != null) {
            prev.resumeKeyDispatchingLocked();
        }
        this.mRootWindowContainer.ensureActivitiesVisible(resuming);
        if (this.mTaskSupervisor.mAppVisibilitiesChangedSinceLastPause || (getDisplayArea() != null && getDisplayArea().hasPinnedTask())) {
            this.mAtmService.getTaskChangeNotificationController().notifyTaskStackChanged();
            this.mTaskSupervisor.mAppVisibilitiesChangedSinceLastPause = false;
        }
    }

    @Override // com.android.server.wm.WindowContainer
    int getOrientation(int candidate) {
        if (shouldReportOrientationUnspecified()) {
            if (this.mTaskFragmentExt.canSpecifyOrientationInActivityEmbedding()) {
                return super.getOrientation(candidate);
            }
            return -1;
        }
        if (canSpecifyOrientation()) {
            return super.getOrientation(candidate);
        }
        return -2;
    }

    @Override // com.android.server.wm.WindowContainer
    protected int getOverrideOrientation() {
        if (isEmbedded() && !isVisibleRequested()) {
            return -1;
        }
        return super.getOverrideOrientation();
    }

    boolean canSpecifyOrientation() {
        int windowingMode = getWindowingMode();
        int activityType = getActivityType();
        return windowingMode == 1 || windowingMode == 120 || activityType == 2 || activityType == 3 || activityType == 4;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean providesOrientation() {
        return super.providesOrientation() || shouldReportOrientationUnspecified() || getWindowingMode() == 120;
    }

    private boolean shouldReportOrientationUnspecified() {
        return getAdjacentTaskFragment() != null && isVisibleRequested();
    }

    @Override // com.android.server.wm.WindowContainer
    void forAllTaskFragments(java.util.function.Consumer<com.android.server.wm.TaskFragment> callback, boolean traverseTopToBottom) {
        super.forAllTaskFragments(callback, traverseTopToBottom);
        callback.accept(this);
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
    @Override // com.android.server.wm.WindowContainer
    void forAllLeafTaskFragments(java.util.function.Consumer<com.android.server.wm.TaskFragment> callback, boolean traverseTopToBottom) {
        int count = this.mChildren.size();
        boolean isLeafTaskFrag = true;
        if (traverseTopToBottom) {
            for (int i = count - 1; i >= 0; i--) {
                com.android.server.wm.TaskFragment child = ((com.android.server.wm.WindowContainer) this.mChildren.get(i)).asTaskFragment();
                if (child != null) {
                    isLeafTaskFrag = false;
                    child.forAllLeafTaskFragments(callback, traverseTopToBottom);
                }
            }
        } else {
            for (int i2 = 0; i2 < count; i2++) {
                com.android.server.wm.TaskFragment child2 = ((com.android.server.wm.WindowContainer) this.mChildren.get(i2)).asTaskFragment();
                if (child2 != null) {
                    isLeafTaskFrag = false;
                    child2.forAllLeafTaskFragments(callback, traverseTopToBottom);
                }
            }
        }
        if (isLeafTaskFrag) {
            callback.accept(this);
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
    @Override // com.android.server.wm.WindowContainer
    boolean forAllLeafTaskFragments(java.util.function.Predicate<com.android.server.wm.TaskFragment> callback) {
        boolean isLeafTaskFrag = true;
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.TaskFragment child = ((com.android.server.wm.WindowContainer) this.mChildren.get(i)).asTaskFragment();
            if (child != null) {
                isLeafTaskFrag = false;
                if (child.forAllLeafTaskFragments(callback)) {
                    return true;
                }
            }
        }
        if (isLeafTaskFrag) {
            return callback.test(this);
        }
        return false;
    }

    void addChild(com.android.server.wm.ActivityRecord r) {
        addChild(r, Integer.MAX_VALUE);
    }

    @Override // com.android.server.wm.WindowContainer
    void addChild(com.android.server.wm.WindowContainer child, int index) {
        boolean isAddingActivity;
        boolean taskHadActivity;
        topRunningActivity();
        this.mClearedTaskForReuse = false;
        this.mClearedTaskFragmentForPip = false;
        this.mClearedForReorderActivityToFront = false;
        com.android.server.wm.ActivityRecord addingActivity = child.asActivityRecord();
        if (addingActivity == null) {
            isAddingActivity = false;
        } else {
            isAddingActivity = true;
        }
        com.android.server.wm.Task task = isAddingActivity ? getTask() : null;
        if (task == null || task.getTopMostActivity() == null) {
            taskHadActivity = false;
        } else {
            taskHadActivity = true;
        }
        int activityType = task != null ? task.getActivityType() : 0;
        super.addChild(child, index);
        if (isAddingActivity && task != null) {
            this.mTaskFragmentExt.addChild(addingActivity);
            addingActivity.inHistory = true;
            task.onDescendantActivityAdded(taskHadActivity, activityType, addingActivity);
        }
        com.android.server.wm.WindowProcessController hostProcess = getOrganizerProcessIfDifferent(addingActivity);
        if (hostProcess != null) {
            hostProcess.addEmbeddedActivity(addingActivity);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void onChildPositionChanged(com.android.server.wm.WindowContainer child) {
        super.onChildPositionChanged(child);
        sendTaskFragmentInfoChanged();
    }

    void executeAppTransition(android.app.ActivityOptions options) {
    }

    @Override // com.android.server.wm.WindowContainer
    android.view.RemoteAnimationTarget createRemoteAnimationTarget(com.android.server.wm.RemoteAnimationController.RemoteAnimationRecord record) {
        com.android.server.wm.ActivityRecord activity;
        if (record.getMode() == 0) {
            activity = getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskFragment$$ExternalSyntheticLambda6
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return this.f$0.lambda$createRemoteAnimationTarget$8((com.android.server.wm.ActivityRecord) obj);
                }
            });
        } else {
            activity = getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskFragment$$ExternalSyntheticLambda7
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return this.f$0.lambda$createRemoteAnimationTarget$9((com.android.server.wm.ActivityRecord) obj);
                }
            });
        }
        if (activity != null) {
            return activity.createRemoteAnimationTarget(record);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createRemoteAnimationTarget$8(com.android.server.wm.ActivityRecord r) {
        return (!r.finishing && r.hasChild()) || forceCreateRemoteAnimationTarget(r);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createRemoteAnimationTarget$9(com.android.server.wm.ActivityRecord r) {
        return r.findMainWindow() != null || forceCreateRemoteAnimationTarget(r);
    }

    boolean forceCreateRemoteAnimationTarget(com.android.server.wm.ActivityRecord r) {
        return false;
    }

    void setAppTransitionReadyInAdvance(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.ActivityRecord activity) {
    }

    @Override // com.android.server.wm.WindowContainer
    boolean canCreateRemoteAnimationTarget() {
        return true;
    }

    boolean shouldSleepActivities() {
        com.android.server.wm.Task task = getRootTask();
        return task != null && task.shouldSleepActivities();
    }

    @Override // com.android.server.wm.ConfigurationContainer
    void resolveOverrideConfiguration(android.content.res.Configuration newParentConfig) {
        this.mTmpBounds.set(getResolvedOverrideConfiguration().windowConfiguration.getBounds());
        this.mTaskFragmentExt.setPrevWinMode(getResolvedOverrideConfiguration().windowConfiguration.getWindowingMode());
        super.resolveOverrideConfiguration(newParentConfig);
        android.content.res.Configuration resolvedConfig = getResolvedOverrideConfiguration();
        if (this.mRelativeEmbeddedBounds != null && !this.mRelativeEmbeddedBounds.isEmpty()) {
            resolvedConfig.windowConfiguration.setBounds(translateRelativeBoundsToAbsoluteBounds(this.mRelativeEmbeddedBounds, newParentConfig.windowConfiguration.getBounds()));
        }
        int windowingMode = resolvedConfig.windowConfiguration.getWindowingMode();
        int parentWindowingMode = newParentConfig.windowConfiguration.getWindowingMode();
        if (getActivityType() == 2 && windowingMode == 0) {
            windowingMode = 1;
            if (this.mTaskFragmentExt.isTaskLaunchedFromMultiSearch(getTask())) {
                windowingMode = 6;
            }
            resolvedConfig.windowConfiguration.setWindowingMode(windowingMode);
        }
        if (!supportsMultiWindow()) {
            int candidateWindowingMode = windowingMode != 0 ? windowingMode : parentWindowingMode;
            if (android.app.WindowConfiguration.inMultiWindowMode(candidateWindowingMode) && candidateWindowingMode != 2) {
                resolvedConfig.windowConfiguration.setWindowingMode(1);
            }
        }
        com.android.server.wm.Task thisTask = asTask();
        if (thisTask != null) {
            thisTask.resolveLeafTaskOnlyOverrideConfigs(newParentConfig, this.mTmpBounds);
        }
        computeConfigResourceOverrides(resolvedConfig, newParentConfig);
    }

    boolean supportsMultiWindow() {
        return supportsMultiWindowInDisplayArea(getDisplayArea());
    }

    boolean supportsMultiWindowInDisplayArea(com.android.server.wm.TaskDisplayArea tda) {
        com.android.server.wm.Task task;
        com.android.server.wm.Task containerTask;
        if (!this.mAtmService.mSupportsMultiWindow || tda == null || (task = getTask()) == null) {
            return false;
        }
        if (!task.isResizeable() && !tda.supportsNonResizableMultiWindow()) {
            return false;
        }
        com.android.server.wm.ActivityRecord rootActivity = task.getRootActivity();
        boolean res = tda.supportsActivityMinWidthHeightMultiWindow(this.mMinWidth, this.mMinHeight, rootActivity != null ? rootActivity.info : null);
        if (res && (containerTask = this.mTaskFragmentExt.getEmbeddedContainerTask(task)) != null) {
            return containerTask.supportsMultiWindowInDisplayArea(tda);
        }
        return res;
    }

    private int getTaskId() {
        if (getTask() != null) {
            return getTask().mTaskId;
        }
        return -1;
    }

    static class ConfigOverrideHint {
        android.graphics.Rect mParentAppBoundsOverride;
        com.android.server.wm.ActivityRecord.CompatDisplayInsets mTmpCompatInsets;
        int mTmpOverrideConfigOrientation;
        android.view.DisplayInfo mTmpOverrideDisplayInfo;
        boolean mUseOverrideInsetsForConfig;

        ConfigOverrideHint() {
        }

        void resolveTmpOverrides(com.android.server.wm.DisplayContent dc, android.content.res.Configuration parentConfig, boolean isFixedRotationTransforming) {
            resolveTmpOverrides(dc, parentConfig, isFixedRotationTransforming, null);
        }

        void resolveTmpOverrides(com.android.server.wm.DisplayContent dc, android.content.res.Configuration parentConfig, boolean isFixedRotationTransforming, com.android.server.wm.ActivityRecord record) {
            android.graphics.Insets insets;
            int rotation;
            this.mParentAppBoundsOverride = new android.graphics.Rect(parentConfig.windowConfiguration.getAppBounds());
            if (this.mUseOverrideInsetsForConfig && dc != null) {
                int rotation2 = parentConfig.windowConfiguration.getRotation();
                if (rotation2 == -1 && !isFixedRotationTransforming) {
                    rotation = dc.getRotation();
                } else {
                    rotation = rotation2;
                }
                boolean rotated = rotation == 1 || rotation == 3;
                int dw = rotated ? dc.mBaseDisplayHeight : dc.mBaseDisplayWidth;
                int dh = rotated ? dc.mBaseDisplayWidth : dc.mBaseDisplayHeight;
                com.android.server.wm.DisplayPolicy.DecorInsets.Info decorInsets = dc.getDisplayPolicy().getDecorInsetsInfo(rotation, dw, dh);
                android.graphics.Rect stableBounds = decorInsets.mOverrideConfigFrame;
                this.mTmpOverrideConfigOrientation = stableBounds.width() > stableBounds.height() ? 2 : 1;
                if (record != null) {
                    insets = record.getWrapper().getExtImpl().hookResolveTmpOverridesInsets(this, record, parentConfig, decorInsets, android.graphics.Insets.of(decorInsets.mOverrideNonDecorInsets));
                } else {
                    insets = android.graphics.Insets.of(decorInsets.mOverrideNonDecorInsets);
                }
            } else {
                insets = android.graphics.Insets.NONE;
            }
            this.mParentAppBoundsOverride.inset(insets);
        }

        void resetTmpOverrides() {
            this.mTmpOverrideDisplayInfo = null;
            this.mTmpCompatInsets = null;
            this.mTmpOverrideConfigOrientation = 0;
        }
    }

    void computeConfigResourceOverrides(android.content.res.Configuration inOutConfig, android.content.res.Configuration parentConfig) {
        computeConfigResourceOverrides(inOutConfig, parentConfig, null);
    }

    private static void invalidateAppBoundsConfig(android.content.res.Configuration inOutConfig) {
        android.graphics.Rect appBounds = inOutConfig.windowConfiguration.getAppBounds();
        if (appBounds != null) {
            appBounds.setEmpty();
        }
        inOutConfig.screenWidthDp = 0;
        inOutConfig.screenHeightDp = 0;
    }

    void computeConfigResourceOverrides(android.content.res.Configuration inOutConfig, android.content.res.Configuration parentConfig, com.android.server.wm.TaskFragment.ConfigOverrideHint overrideHint) {
        computeConfigResourceOverrides(inOutConfig, parentConfig, overrideHint, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:140:0x02f2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:141:0x02f4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void computeConfigResourceOverrides(android.content.res.Configuration r24, android.content.res.Configuration r25, com.android.server.wm.TaskFragment.ConfigOverrideHint r26, com.android.server.wm.ActivityRecord r27) {
        /*
            Method dump skipped, instruction units count: 914
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.TaskFragment.computeConfigResourceOverrides(android.content.res.Configuration, android.content.res.Configuration, com.android.server.wm.TaskFragment$ConfigOverrideHint, com.android.server.wm.ActivityRecord):void");
    }

    void calculateInsetFrames(android.graphics.Rect outNonDecorBounds, android.graphics.Rect outStableBounds, android.graphics.Rect bounds, android.view.DisplayInfo displayInfo, boolean useLegacyInsetsForStableBounds) {
        outNonDecorBounds.set(bounds);
        outStableBounds.set(bounds);
        if (this.mDisplayContent == null) {
            return;
        }
        this.mTmpBounds.set(0, 0, displayInfo.logicalWidth, displayInfo.logicalHeight);
        com.android.server.wm.DisplayPolicy policy = this.mDisplayContent.getDisplayPolicy();
        com.android.server.wm.DisplayPolicy.DecorInsets.Info info = policy.getDecorInsetsInfo(displayInfo.rotation, displayInfo.logicalWidth, displayInfo.logicalHeight);
        if (!useLegacyInsetsForStableBounds) {
            intersectWithInsetsIfFits(outStableBounds, this.mTmpBounds, info.mConfigInsets);
            intersectWithInsetsIfFits(outNonDecorBounds, this.mTmpBounds, info.mNonDecorInsets);
        } else {
            intersectWithInsetsIfFits(outStableBounds, this.mTmpBounds, info.mOverrideConfigInsets);
            intersectWithInsetsIfFits(outNonDecorBounds, this.mTmpBounds, info.mOverrideNonDecorInsets);
        }
    }

    static void intersectWithInsetsIfFits(android.graphics.Rect inOutBounds, android.graphics.Rect intersectBounds, android.graphics.Rect intersectInsets) {
        if (inOutBounds.right <= intersectBounds.right) {
            inOutBounds.right = java.lang.Math.min(intersectBounds.right - intersectInsets.right, inOutBounds.right);
        }
        if (inOutBounds.bottom <= intersectBounds.bottom) {
            inOutBounds.bottom = java.lang.Math.min(intersectBounds.bottom - intersectInsets.bottom, inOutBounds.bottom);
        }
        if (inOutBounds.left >= intersectBounds.left) {
            inOutBounds.left = java.lang.Math.max(intersectBounds.left + intersectInsets.left, inOutBounds.left);
        }
        if (inOutBounds.top >= intersectBounds.top) {
            inOutBounds.top = java.lang.Math.max(intersectBounds.top + intersectInsets.top, inOutBounds.top);
        }
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public int getActivityType() {
        int applicationType = super.getActivityType();
        if (applicationType != 0 || !hasChild()) {
            return applicationType;
        }
        com.android.server.wm.ActivityRecord activity = getTopMostActivity();
        return activity != null ? activity.getActivityType() : getTopChild().getActivityType();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void onConfigurationChanged(android.content.res.Configuration newParentConfig) {
        super.onConfigurationChanged(newParentConfig);
        updateOrganizedTaskFragmentSurface();
        sendTaskFragmentInfoChanged();
    }

    void deferOrganizedTaskFragmentSurfaceUpdate() {
        this.mDelayOrganizedTaskFragmentSurfaceUpdate = true;
    }

    void continueOrganizedTaskFragmentSurfaceUpdate() {
        this.mDelayOrganizedTaskFragmentSurfaceUpdate = false;
        updateOrganizedTaskFragmentSurface();
    }

    void updateOrganizedTaskFragmentSurface() {
        if (this.mDelayOrganizedTaskFragmentSurfaceUpdate || this.mTaskFragmentOrganizer == null) {
            return;
        }
        if (this.mTransitionController.isShellTransitionsEnabled() && !this.mTransitionController.isCollecting(this)) {
            updateOrganizedTaskFragmentSurfaceUnchecked();
        } else if (!this.mTransitionController.isShellTransitionsEnabled() && !isAnimating()) {
            updateOrganizedTaskFragmentSurfaceUnchecked();
        }
        if (this.mTransitionController.isShellTransitionsEnabled() && this.mTransitionController.isCollecting(this) && !this.mTransitionController.inPlayingTransition(this)) {
            updateSurfacePosition(getSyncTransaction());
            updateOrganizedTaskFragmentSurfaceSize(getSyncTransaction(), true);
        }
    }

    private void updateOrganizedTaskFragmentSurfaceUnchecked() {
        android.view.SurfaceControl.Transaction t = getSyncTransaction();
        updateSurfacePosition(t);
        updateOrganizedTaskFragmentSurfaceSize(t, false);
    }

    private void updateOrganizedTaskFragmentSurfaceSize(android.view.SurfaceControl.Transaction t, boolean forceUpdate) {
        android.graphics.Rect bounds;
        if (this.mTaskFragmentOrganizer == null || this.mSurfaceControl == null || this.mSurfaceAnimator.hasLeash() || this.mSurfaceFreezer.hasLeash()) {
            return;
        }
        if (isClosingWhenResizing()) {
            bounds = this.mDisplayContent.mClosingChangingContainers.get(this);
        } else {
            bounds = getBounds();
        }
        int width = bounds.width();
        int height = bounds.height();
        if (!forceUpdate && width == this.mLastSurfaceSize.x && height == this.mLastSurfaceSize.y) {
            return;
        }
        t.setWindowCrop(this.mSurfaceControl, width, height);
        this.mLastSurfaceSize.set(width, height);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public void onAnimationLeashCreated(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl leash) {
        super.onAnimationLeashCreated(t, leash);
        if (this.mTaskFragmentOrganizer != null) {
            if (this.mLastSurfaceSize.x != 0 || this.mLastSurfaceSize.y != 0) {
                t.setWindowCrop(this.mSurfaceControl, 0, 0);
                android.view.SurfaceControl.Transaction syncTransaction = getSyncTransaction();
                if (t != syncTransaction) {
                    syncTransaction.setWindowCrop(this.mSurfaceControl, 0, 0);
                }
                this.mLastSurfaceSize.set(0, 0);
            }
        }
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public void onAnimationLeashLost(android.view.SurfaceControl.Transaction t) {
        super.onAnimationLeashLost(t);
        if (this.mTaskFragmentOrganizer != null) {
            updateOrganizedTaskFragmentSurfaceSize(t, true);
        }
    }

    android.graphics.Rect getRelativeEmbeddedBounds() {
        if (this.mRelativeEmbeddedBounds == null) {
            throw new java.lang.IllegalStateException("The TaskFragment is not embedded");
        }
        return this.mRelativeEmbeddedBounds;
    }

    android.graphics.Rect translateRelativeBoundsToAbsoluteBounds(android.graphics.Rect relativeBounds, android.graphics.Rect parentBounds) {
        if (relativeBounds.isEmpty()) {
            this.mTmpAbsBounds.setEmpty();
            return this.mTmpAbsBounds;
        }
        this.mTmpAbsBounds.set(relativeBounds);
        this.mTmpAbsBounds.offset(parentBounds.left, parentBounds.top);
        if (!isAllowedToBeEmbeddedInTrustedMode() && !parentBounds.contains(this.mTmpAbsBounds)) {
            if (this.mTaskFragmentExt.isCreateForMagicWindow()) {
                return this.mTmpAbsBounds;
            }
            if (!this.mTmpAbsBounds.intersect(parentBounds)) {
                this.mTmpAbsBounds.setEmpty();
            }
        }
        return this.mTmpAbsBounds;
    }

    void recomputeConfiguration() {
        onRequestedOverrideConfigurationChanged(getRequestedOverrideConfiguration());
    }

    void setRelativeEmbeddedBounds(android.graphics.Rect relativeEmbeddedBounds) {
        if (this.mRelativeEmbeddedBounds == null) {
            throw new java.lang.IllegalStateException("The TaskFragment is not embedded");
        }
        if (this.mRelativeEmbeddedBounds.equals(relativeEmbeddedBounds)) {
            return;
        }
        this.mRelativeEmbeddedBounds.set(relativeEmbeddedBounds);
    }

    boolean shouldStartChangeTransition(android.graphics.Rect absStartBounds, android.graphics.Rect relStartBounds) {
        if (this.mTaskFragmentOrganizer == null || !canStartChangeTransition()) {
            return false;
        }
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            android.graphics.Rect endBounds = getConfiguration().windowConfiguration.getBounds();
            return (endBounds.width() == absStartBounds.width() && endBounds.height() == absStartBounds.height()) ? false : true;
        }
        return !relStartBounds.equals(this.mRelativeEmbeddedBounds);
    }

    @Override // com.android.server.wm.WindowContainer
    boolean canStartChangeTransition() {
        com.android.server.wm.Task task = getTask();
        return (task == null || task.isDragResizing() || !super.canStartChangeTransition()) ? false : true;
    }

    boolean setClosingChangingStartBoundsIfNeeded() {
        if (isOrganizedTaskFragment() && this.mDisplayContent != null && this.mDisplayContent.mChangingContainers.remove(this)) {
            this.mDisplayContent.mClosingChangingContainers.put(this, new android.graphics.Rect(this.mSurfaceFreezer.mFreezeBounds));
            return true;
        }
        return false;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isSyncFinished(com.android.server.wm.BLASTSyncEngine.SyncGroup group) {
        return super.isSyncFinished(group) && isReadyToTransit();
    }

    @Override // com.android.server.wm.WindowContainer
    void setSurfaceControl(android.view.SurfaceControl sc) {
        super.setSurfaceControl(sc);
        if (this.mTaskFragmentOrganizer != null) {
            updateOrganizedTaskFragmentSurfaceUnchecked();
            sendTaskFragmentAppeared();
        }
    }

    void sendTaskFragmentInfoChanged() {
        this.mTaskFragmentExt.onTaskFragmentInfoChanged(this);
        if (this.mTaskFragmentOrganizer != null) {
            this.mTaskFragmentOrganizerController.onTaskFragmentInfoChanged(this.mTaskFragmentOrganizer, this);
        }
    }

    void sendTaskFragmentParentInfoChanged() {
        com.android.server.wm.Task parentTask = getParent().asTask();
        if (this.mTaskFragmentOrganizer != null && parentTask != null) {
            this.mTaskFragmentOrganizerController.onTaskFragmentParentInfoChanged(this.mTaskFragmentOrganizer, parentTask);
        }
    }

    private void sendTaskFragmentAppeared() {
        if (this.mTaskFragmentOrganizer != null) {
            this.mTaskFragmentOrganizerController.onTaskFragmentAppeared(this.mTaskFragmentOrganizer, this);
        }
    }

    private void sendTaskFragmentVanished() {
        if (this.mTaskFragmentOrganizer != null) {
            this.mTaskFragmentOrganizerController.onTaskFragmentVanished(this.mTaskFragmentOrganizer, this);
        }
    }

    android.window.TaskFragmentInfo getTaskFragmentInfo() {
        java.util.List<android.os.IBinder> childActivities = new java.util.ArrayList<>();
        java.util.List<android.os.IBinder> inRequestedTaskFragmentActivities = new java.util.ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            com.android.server.wm.WindowContainer<?> wc = getChildAt(i);
            com.android.server.wm.ActivityRecord ar = wc.asActivityRecord();
            if (this.mTaskFragmentOrganizerUid != -1 && ar != null && ar.info.processName.equals(this.mTaskFragmentOrganizerProcessName) && ar.getUid() == this.mTaskFragmentOrganizerUid && !ar.finishing) {
                childActivities.add(ar.token);
                if (ar.mRequestedLaunchingTaskFragmentToken == this.mFragmentToken) {
                    inRequestedTaskFragmentActivities.add(ar.token);
                }
            }
        }
        android.graphics.Point positionInParent = new android.graphics.Point();
        getRelativePosition(positionInParent);
        return new android.window.TaskFragmentInfo(this.mFragmentToken, this.mRemoteToken.toWindowContainerToken(), getConfiguration(), getNonFinishingActivityCount(), shouldBeVisible(null), childActivities, inRequestedTaskFragmentActivities, positionInParent, this.mClearedTaskForReuse, this.mClearedTaskFragmentForPip, this.mClearedForReorderActivityToFront, calculateMinDimension());
    }

    android.graphics.Point calculateMinDimension() {
        final int[] maxMinWidth = new int[1];
        final int[] maxMinHeight = new int[1];
        forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.TaskFragment$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.TaskFragment.lambda$calculateMinDimension$10(maxMinWidth, maxMinHeight, (com.android.server.wm.ActivityRecord) obj);
            }
        });
        return new android.graphics.Point(maxMinWidth[0], maxMinHeight[0]);
    }

    static /* synthetic */ void lambda$calculateMinDimension$10(int[] maxMinWidth, int[] maxMinHeight, com.android.server.wm.ActivityRecord a) {
        android.graphics.Point minDimensions;
        if (a.finishing || (minDimensions = a.getMinDimensions()) == null) {
            return;
        }
        maxMinWidth[0] = java.lang.Math.max(maxMinWidth[0], minDimensions.x);
        maxMinHeight[0] = java.lang.Math.max(maxMinHeight[0], minDimensions.y);
    }

    android.os.IBinder getFragmentToken() {
        return this.mFragmentToken;
    }

    android.window.ITaskFragmentOrganizer getTaskFragmentOrganizer() {
        return this.mTaskFragmentOrganizer;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isOrganized() {
        return this.mTaskFragmentOrganizer != null;
    }

    final boolean isOrganizedTaskFragment() {
        return this.mTaskFragmentOrganizer != null;
    }

    boolean isEmbeddedWithBoundsOverride() {
        com.android.server.wm.Task task;
        if (!this.mIsEmbedded || (task = getTask()) == null) {
            return false;
        }
        android.graphics.Rect taskBounds = task.getBounds();
        android.graphics.Rect taskFragBounds = getBounds();
        return !taskBounds.equals(taskFragBounds) && taskBounds.contains(taskFragBounds);
    }

    boolean isTaskVisibleRequested() {
        com.android.server.wm.Task task = getTask();
        return task != null && task.isVisibleRequested();
    }

    boolean isReadyToTransit() {
        if (!isOrganizedTaskFragment() || getTopNonFinishingActivity() != null || this.mIsRemovalRequested || this.mAllowTransitionWhenEmpty || isEmbeddedTaskFragmentInPip()) {
            return true;
        }
        return this.mClearedTaskFragmentForPip && !isTaskVisibleRequested();
    }

    @Override // com.android.server.wm.WindowContainer
    boolean canCustomizeAppTransition() {
        return isEmbedded() && matchParentBounds();
    }

    void clearLastPausedActivity() {
        forAllTaskFragments(new java.util.function.Consumer() { // from class: com.android.server.wm.TaskFragment$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.wm.TaskFragment) obj).mLastPausedActivity = null;
            }
        });
    }

    void setMinDimensions(int minWidth, int minHeight) {
        if (asTask() != null) {
            throw new java.lang.UnsupportedOperationException("This method must not be used to Task. The  minimum dimension of Task should be passed from Task constructor.");
        }
        this.mMinWidth = minWidth;
        this.mMinHeight = minHeight;
    }

    boolean isEmbeddedTaskFragmentInPip() {
        return isOrganizedTaskFragment() && getTask() != null && getTask().inPinnedWindowingMode();
    }

    boolean shouldRemoveSelfOnLastChildRemoval() {
        if (this.mTaskFragmentExt.shouldRemoveOnLastChildRemoval()) {
            return !this.mCreatedByOrganizer || this.mIsRemovalRequested;
        }
        return false;
    }

    boolean isRemovalRequested() {
        return this.mIsRemovalRequested;
    }

    @Override // com.android.server.wm.WindowContainer
    void removeChild(com.android.server.wm.WindowContainer child) {
        removeChild(child, true);
    }

    void removeChild(com.android.server.wm.WindowContainer child, boolean removeSelfIfPossible) {
        super.removeChild(child);
        com.android.server.wm.ActivityRecord r = child.asActivityRecord();
        com.android.server.wm.WindowProcessController hostProcess = getOrganizerProcessIfDifferent(r);
        if (hostProcess != null) {
            hostProcess.removeEmbeddedActivity(r);
        }
        if (removeSelfIfPossible && shouldRemoveSelfOnLastChildRemoval() && !hasChild() && !this.mTaskFragmentExt.isPrimaryTopTaskFragment(this, child)) {
            removeImmediately("removeLastChild " + child);
        }
    }

    void remove(boolean withTransition, java.lang.String reason) {
        if (!hasChild()) {
            removeImmediately(reason);
            return;
        }
        this.mIsRemovalRequested = true;
        java.util.ArrayList<com.android.server.wm.ActivityRecord> removingActivities = new java.util.ArrayList<>();
        java.util.Objects.requireNonNull(removingActivities);
        forAllActivities(new com.android.server.wm.Task$$ExternalSyntheticLambda38(removingActivities));
        for (int i = removingActivities.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord r = removingActivities.get(i);
            if (withTransition && r.isVisible()) {
                r.finishIfPossible(reason, false);
            } else {
                r.destroyIfPossible(reason);
            }
        }
    }

    void setDelayLastActivityRemoval(boolean delay) {
        if (!this.mIsEmbedded) {
            android.util.Slog.w(TAG, "Set delaying last activity removal on a non-embedded TF.");
        }
        this.mDelayLastActivityRemoval = delay;
    }

    boolean isDelayLastActivityRemoval() {
        return this.mDelayLastActivityRemoval;
    }

    boolean shouldDeferRemoval() {
        if (!hasChild()) {
            return false;
        }
        return isExitAnimationRunningSelfOrChild();
    }

    @Override // com.android.server.wm.WindowContainer
    boolean handleCompleteDeferredRemoval() {
        if (shouldDeferRemoval()) {
            return true;
        }
        return super.handleCompleteDeferredRemoval();
    }

    void removeImmediately(java.lang.String reason) {
        android.util.Slog.d(TAG, "Remove task fragment: " + reason);
        removeImmediately();
    }

    @Override // com.android.server.wm.WindowContainer
    void removeImmediately() {
        if (asTask() == null) {
            com.android.server.wm.EventLogTags.writeWmTfRemoved(java.lang.System.identityHashCode(this), getTaskId());
        }
        boolean shouldExecuteAppTransition = false;
        this.mIsRemovalRequested = false;
        resetAdjacentTaskFragment();
        cleanUpEmbeddedTaskFragment();
        if (this.mClearedTaskFragmentForPip && isTaskVisibleRequested()) {
            shouldExecuteAppTransition = true;
        }
        super.removeImmediately();
        sendTaskFragmentVanished();
        if (shouldExecuteAppTransition && this.mDisplayContent != null) {
            this.mAtmService.addWindowLayoutReasons(2);
            this.mDisplayContent.executeAppTransition();
        }
    }

    private void cleanUpEmbeddedTaskFragment() {
        if (!this.mIsEmbedded) {
            return;
        }
        this.mAtmService.mWindowOrganizerController.cleanUpEmbeddedTaskFragment(this);
        com.android.server.wm.Task task = getTask();
        if (task == null) {
            return;
        }
        task.forAllLeafTaskFragments(new java.util.function.Consumer() { // from class: com.android.server.wm.TaskFragment$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$cleanUpEmbeddedTaskFragment$12((com.android.server.wm.TaskFragment) obj);
            }
        }, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanUpEmbeddedTaskFragment$12(com.android.server.wm.TaskFragment taskFragment) {
        if (taskFragment.getCompanionTaskFragment() == this) {
            taskFragment.setCompanionTaskFragment(null);
        }
    }

    boolean shouldBoostDimmer() {
        com.android.server.wm.TaskFragment adjacentTf;
        if (asTask() != null || !isDimmingOnParentTask() || (adjacentTf = getAdjacentTaskFragment()) == null || getParent().mChildren.indexOf(adjacentTf) < getParent().mChildren.indexOf(this)) {
            return false;
        }
        com.android.internal.util.ToBooleanFunction<com.android.server.wm.WindowState> getDimBehindWindow = new com.android.internal.util.ToBooleanFunction() { // from class: com.android.server.wm.TaskFragment$$ExternalSyntheticLambda14
            public final boolean apply(java.lang.Object obj) {
                return com.android.server.wm.TaskFragment.lambda$shouldBoostDimmer$13((com.android.server.wm.WindowState) obj);
            }
        };
        if (adjacentTf.forAllWindows(getDimBehindWindow, true)) {
            return false;
        }
        return forAllWindows(getDimBehindWindow, true);
    }

    static /* synthetic */ boolean lambda$shouldBoostDimmer$13(com.android.server.wm.WindowState w) {
        return (w.mAttrs.flags & 2) != 0 && w.mActivityRecord != null && w.mActivityRecord.isEmbedded() && (w.mActivityRecord.isVisibleRequested() || w.mActivityRecord.isVisible());
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.Dimmer getDimmer() {
        if (this.mIsEmbedded && !isDimmingOnParentTask()) {
            return this.mDimmer;
        }
        return super.getDimmer();
    }

    void getDimBounds(android.graphics.Rect out) {
        if (this.mIsEmbedded && isDimmingOnParentTask() && getDimmer().getDimBounds() != null) {
            out.set(getTask().getBounds());
        } else {
            out.set(getBounds());
        }
    }

    void setEmbeddedDimArea(int embeddedDimArea) {
        this.mEmbeddedDimArea = embeddedDimArea;
    }

    void setMoveToBottomIfClearWhenLaunch(boolean moveToBottomIfClearWhenLaunch) {
        this.mMoveToBottomIfClearWhenLaunch = moveToBottomIfClearWhenLaunch;
    }

    boolean isMoveToBottomIfClearWhenLaunch() {
        return this.mMoveToBottomIfClearWhenLaunch;
    }

    boolean isDimmingOnParentTask() {
        return this.mEmbeddedDimArea == 1;
    }

    @Override // com.android.server.wm.WindowContainer
    void prepareSurfaces() {
        if (asTask() != null) {
            super.prepareSurfaces();
            return;
        }
        this.mDimmer.resetDimStates();
        super.prepareSurfaces();
        android.graphics.Rect dimBounds = this.mDimmer.getDimBounds();
        if (dimBounds != null) {
            dimBounds.offsetTo(0, 0);
            getTask().getWrapper().getExtImpl().prepareDimBounds(this, dimBounds);
            if (this.mDimmer.updateDims(getSyncTransaction())) {
                scheduleAnimation();
            }
        }
        this.mTaskFragmentExt.onTaskFragmentPrepareSurface();
    }

    @Override // com.android.server.wm.WindowContainer
    boolean fillsParent() {
        return getWindowingMode() == 1 || matchParentBounds();
    }

    @Override // com.android.server.wm.WindowContainer
    protected boolean onChildVisibleRequestedChanged(com.android.server.wm.WindowContainer child) {
        if (!super.onChildVisibleRequestedChanged(child)) {
            return false;
        }
        sendTaskFragmentInfoChanged();
        return true;
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.TaskFragment getTaskFragment(java.util.function.Predicate<com.android.server.wm.TaskFragment> callback) {
        com.android.server.wm.TaskFragment taskFragment = super.getTaskFragment(callback);
        if (taskFragment != null) {
            return taskFragment;
        }
        if (callback.test(this)) {
            return this;
        }
        return null;
    }

    boolean moveChildToFront(com.android.server.wm.WindowContainer newTop) {
        int origDist = getDistanceFromTop(newTop);
        positionChildAt(Integer.MAX_VALUE, newTop, false);
        return getDistanceFromTop(newTop) != origDist;
    }

    java.lang.String toFullString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append(this);
        sb.setLength(sb.length() - 1);
        if (this.mTaskFragmentOrganizerUid != -1) {
            sb.append(" organizerUid=");
            sb.append(this.mTaskFragmentOrganizerUid);
        }
        if (this.mTaskFragmentOrganizerProcessName != null) {
            sb.append(" organizerProc=");
            sb.append(this.mTaskFragmentOrganizerProcessName);
        }
        if (this.mAdjacentTaskFragment != null) {
            sb.append(" adjacent=");
            sb.append(this.mAdjacentTaskFragment);
        }
        sb.append('}');
        return sb.toString();
    }

    public java.lang.String toString() {
        return "TaskFragment{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " mode=" + android.app.WindowConfiguration.windowingModeToString(getWindowingMode()) + "}";
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
    boolean dump(final java.lang.String prefix, java.io.FileDescriptor fd, final java.io.PrintWriter pw, final boolean dumpAll, boolean dumpClient, final java.lang.String dumpPackage, final boolean needSep, final java.lang.Runnable header) throws java.lang.Throwable {
        boolean printed = false;
        java.lang.Runnable headerPrinter = new java.lang.Runnable() { // from class: com.android.server.wm.TaskFragment$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dump$14(needSep, pw, header, prefix, dumpAll, dumpPackage);
            }
        };
        if (dumpPackage == null) {
            headerPrinter.run();
            headerPrinter = null;
            printed = true;
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer child = (com.android.server.wm.WindowContainer) this.mChildren.get(i);
            if (child.asTaskFragment() != null) {
                printed = child.asTaskFragment().dump(prefix + "  ", fd, pw, dumpAll, dumpClient, dumpPackage, needSep, headerPrinter) | printed;
            } else if (child.asActivityRecord() != null) {
                com.android.server.wm.ActivityRecord.dumpActivity(fd, pw, i, child.asActivityRecord(), prefix + "  ", "Hist ", true, !dumpAll, dumpClient, dumpPackage, false, headerPrinter, getTask());
            }
        }
        return printed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dump$14(boolean needSep, java.io.PrintWriter pw, java.lang.Runnable header, java.lang.String prefix, boolean dumpAll, java.lang.String dumpPackage) {
        if (needSep) {
            pw.println();
        }
        if (header != null) {
            header.run();
        }
        dumpInner(prefix, pw, dumpAll, dumpPackage);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpInner(java.lang.String prefix, java.io.PrintWriter pw, boolean dumpAll, java.lang.String dumpPackage) {
        pw.print(prefix);
        pw.print("* ");
        pw.println(toFullString());
        android.graphics.Rect bounds = getRequestedOverrideBounds();
        if (!bounds.isEmpty()) {
            pw.println(prefix + "  mBounds=" + bounds);
        }
        if (this.mIsRemovalRequested) {
            pw.println(prefix + "  mIsRemovalRequested=true");
        }
        if (dumpAll) {
            com.android.server.wm.ActivityTaskSupervisor.printThisActivity(pw, this.mLastPausedActivity, dumpPackage, false, prefix + "  mLastPausedActivity: ", null);
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
    @Override // com.android.server.wm.WindowContainer
    void dump(java.io.PrintWriter pw, java.lang.String prefix, boolean dumpAll) {
        super.dump(pw, prefix, dumpAll);
        pw.println(prefix + "bounds=" + getBounds().toShortString() + (this.mIsolatedNav ? ", isolatedNav" : ""));
        java.lang.String doublePrefix = prefix + "  ";
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer<?> child = (com.android.server.wm.WindowContainer) this.mChildren.get(i);
            com.android.server.wm.TaskFragment tf = child.asTaskFragment();
            pw.println(prefix + "* " + (tf != null ? tf.toFullString() : child));
            if (tf != null) {
                child.dump(pw, doublePrefix, dumpAll);
            }
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void writeIdentifierToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, java.lang.System.identityHashCode(this));
        com.android.server.wm.ActivityRecord topActivity = topRunningActivity();
        proto.write(1120986464258L, topActivity != null ? topActivity.mUserId : -10000);
        proto.write(1138166333443L, topActivity != null ? topActivity.intent.getComponent().flattenToShortString() : "TaskFragment");
        proto.end(token);
    }

    @Override // com.android.server.wm.WindowContainer
    long getProtoFieldId() {
        return 1146756268041L;
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId, int logLevel) {
        if (logLevel == 2 && !isVisible()) {
            return;
        }
        long token = proto.start(fieldId);
        super.dumpDebug(proto, 1146756268033L, logLevel);
        proto.write(1120986464258L, getDisplayId());
        proto.write(1120986464259L, getActivityType());
        proto.write(1120986464260L, this.mMinWidth);
        proto.write(1120986464261L, this.mMinHeight);
        proto.end(token);
    }
}
