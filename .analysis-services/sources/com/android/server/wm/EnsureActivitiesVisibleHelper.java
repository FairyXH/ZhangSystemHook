package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class EnsureActivitiesVisibleHelper {
    private boolean mAboveTop;
    private boolean mBehindFullyOccludedContainer;
    private boolean mContainerShouldBeVisible;
    com.android.server.wm.IEnsureActivitiesVisibleHelperExt mHelperExt = (com.android.server.wm.IEnsureActivitiesVisibleHelperExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IEnsureActivitiesVisibleHelperExt.class).base(this).create();
    private boolean mNotifyClients;
    private com.android.server.wm.ActivityRecord mStarting;
    private final com.android.server.wm.TaskFragment mTaskFragment;
    private com.android.server.wm.ActivityRecord mTopRunningActivity;

    EnsureActivitiesVisibleHelper(com.android.server.wm.TaskFragment container) {
        this.mTaskFragment = container;
    }

    void reset(com.android.server.wm.ActivityRecord starting, boolean notifyClients) {
        this.mStarting = starting;
        this.mTopRunningActivity = this.mTaskFragment.topRunningActivity();
        this.mAboveTop = this.mTopRunningActivity != null;
        this.mContainerShouldBeVisible = this.mTaskFragment.shouldBeVisible(this.mStarting);
        this.mBehindFullyOccludedContainer = !this.mContainerShouldBeVisible;
        this.mNotifyClients = notifyClients;
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
    void process(com.android.server.wm.ActivityRecord starting, boolean notifyClients) {
        reset(starting, notifyClients);
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            android.util.Slog.v(com.android.server.wm.Task.TAG_VISIBILITY, "ensureActivitiesVisible behind " + this.mTopRunningActivity);
        }
        if (this.mTopRunningActivity != null && this.mTaskFragment.asTask() != null) {
            this.mTaskFragment.asTask().checkTranslucentActivityWaiting(this.mTopRunningActivity);
        }
        boolean resumeTopActivity = this.mTopRunningActivity != null && !this.mTopRunningActivity.mLaunchTaskBehind && this.mTaskFragment.canBeResumed(starting) && (starting == null || !starting.isDescendantOf(this.mTaskFragment));
        java.util.ArrayList<com.android.server.wm.TaskFragment> adjacentTaskFragments = null;
        for (int i = this.mTaskFragment.mChildren.size() - 1; i >= 0; i--) {
            if (i >= this.mTaskFragment.mChildren.size()) {
                android.util.Slog.e(com.android.server.wm.Task.TAG_VISIBILITY, "mTaskFragment.mChildren exception break");
                return;
            }
            com.android.server.wm.WindowContainer child = (com.android.server.wm.WindowContainer) this.mTaskFragment.mChildren.get(i);
            com.android.server.wm.TaskFragment childTaskFragment = child.asTaskFragment();
            if (childTaskFragment != null && childTaskFragment.getTopNonFinishingActivity() != null) {
                childTaskFragment.updateActivityVisibilities(starting, notifyClients);
                this.mBehindFullyOccludedContainer |= childTaskFragment.getBounds().equals(this.mTaskFragment.getBounds()) && !childTaskFragment.isTranslucent(starting);
                if (this.mAboveTop && this.mTopRunningActivity.getTaskFragment() == childTaskFragment) {
                    this.mAboveTop = false;
                }
                if (!this.mBehindFullyOccludedContainer) {
                    if (adjacentTaskFragments != null && adjacentTaskFragments.contains(childTaskFragment)) {
                        if (!childTaskFragment.isTranslucent(starting) && !childTaskFragment.getAdjacentTaskFragment().isTranslucent(starting)) {
                            this.mBehindFullyOccludedContainer = true;
                        }
                    } else {
                        com.android.server.wm.TaskFragment adjacentTaskFrag = childTaskFragment.getAdjacentTaskFragment();
                        if (adjacentTaskFrag != null) {
                            if (adjacentTaskFragments == null) {
                                adjacentTaskFragments = new java.util.ArrayList<>();
                            }
                            adjacentTaskFragments.add(adjacentTaskFrag);
                        }
                    }
                }
            } else if (child.asActivityRecord() != null) {
                setActivityVisibilityState(child.asActivityRecord(), starting, resumeTopActivity);
            }
        }
    }

    private void setActivityVisibilityState(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityRecord starting, boolean resumeTopActivity) {
        boolean z = false;
        boolean isTop = r == this.mTopRunningActivity;
        if (this.mAboveTop && !isTop) {
            r.makeInvisible();
            return;
        }
        this.mAboveTop = false;
        r.updateVisibilityIgnoringKeyguard(this.mBehindFullyOccludedContainer);
        boolean reallyVisible = r.shouldBeVisibleUnchecked();
        if (r.visibleIgnoringKeyguard) {
            if (r.occludesParent()) {
                if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                    android.util.Slog.v(com.android.server.wm.Task.TAG_VISIBILITY, "Fullscreen: at " + r + " containerVisible=" + this.mContainerShouldBeVisible + " behindFullyOccluded=" + this.mBehindFullyOccludedContainer);
                }
                this.mBehindFullyOccludedContainer = true;
            } else {
                this.mBehindFullyOccludedContainer = false;
            }
        } else if (r.isState(com.android.server.wm.ActivityRecord.State.INITIALIZING)) {
            r.cancelInitializing();
        }
        if (reallyVisible) {
            this.mHelperExt.updateVisibleTime(r);
        }
        if (reallyVisible) {
            if (r.finishing) {
                return;
            }
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                android.util.Slog.v(com.android.server.wm.Task.TAG_VISIBILITY, "Make visible? " + r + " finishing=" + r.finishing + " state=" + r.getState());
            }
            if (r != this.mStarting && this.mNotifyClients) {
                r.ensureActivityConfiguration(true);
            }
            if (!r.attachedToProcess()) {
                com.android.server.wm.ActivityRecord activityRecord = this.mStarting;
                if (resumeTopActivity && isTop) {
                    z = true;
                }
                makeVisibleAndRestartIfNeeded(activityRecord, z, r);
            } else if (r.isVisibleRequested()) {
                if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                    android.util.Slog.v(com.android.server.wm.Task.TAG_VISIBILITY, "Skipping: already visible at " + r);
                }
                if (r.mClientVisibilityDeferred && this.mNotifyClients) {
                    r.makeActiveIfNeeded(r.mClientVisibilityDeferred ? null : starting);
                    r.mClientVisibilityDeferred = false;
                }
                r.handleAlreadyVisible();
                if (this.mNotifyClients) {
                    r.makeActiveIfNeeded(this.mStarting);
                }
            } else {
                r.makeVisibleIfNeeded(this.mStarting, this.mNotifyClients);
            }
        } else {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                android.util.Slog.v(com.android.server.wm.Task.TAG_VISIBILITY, "Make invisible? " + r + " finishing=" + r.finishing + " state=" + r.getState() + " containerShouldBeVisible=" + this.mContainerShouldBeVisible + " behindFullyOccludedContainer=" + this.mBehindFullyOccludedContainer + " mLaunchTaskBehind=" + r.mLaunchTaskBehind);
            }
            r.makeInvisible();
        }
        if (!this.mBehindFullyOccludedContainer && this.mTaskFragment.isActivityTypeHome() && r.isRootOfTask()) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                android.util.Slog.v(com.android.server.wm.Task.TAG_VISIBILITY, "Home task: at " + this.mTaskFragment + " containerShouldBeVisible=" + this.mContainerShouldBeVisible + " behindOccludedParentContainer=" + this.mBehindFullyOccludedContainer);
            }
            this.mBehindFullyOccludedContainer = true;
        }
    }

    private void makeVisibleAndRestartIfNeeded(com.android.server.wm.ActivityRecord starting, boolean andResume, com.android.server.wm.ActivityRecord r) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            android.util.Slog.v(com.android.server.wm.Task.TAG_VISIBILITY, "Start and freeze screen for " + r);
        }
        if (!r.isVisibleRequested() || r.mLaunchTaskBehind) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                android.util.Slog.v(com.android.server.wm.Task.TAG_VISIBILITY, "Starting and making visible: " + r);
            }
            r.setVisibility(true);
        }
        if (r != starting) {
            this.mTaskFragment.mTaskSupervisor.startSpecificActivity(r, andResume, true);
        }
    }
}
