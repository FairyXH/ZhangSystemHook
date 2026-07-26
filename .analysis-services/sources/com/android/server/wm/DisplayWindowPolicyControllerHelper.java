package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class DisplayWindowPolicyControllerHelper {
    private static final java.lang.String TAG = "DisplayWindowPolicyControllerHelper";
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private android.window.DisplayWindowPolicyController mDisplayWindowPolicyController;
    private com.android.server.wm.ActivityRecord mTopRunningActivity = null;
    private android.util.ArraySet<java.lang.Integer> mRunningUid = new android.util.ArraySet<>();

    DisplayWindowPolicyControllerHelper(com.android.server.wm.DisplayContent displayContent) {
        this.mDisplayContent = displayContent;
        this.mDisplayWindowPolicyController = this.mDisplayContent.mWmService.mDisplayManagerInternal.getDisplayWindowPolicyController(this.mDisplayContent.mDisplayId);
    }

    public boolean hasController() {
        return this.mDisplayWindowPolicyController != null;
    }

    public boolean canContainActivities(java.util.List<android.content.pm.ActivityInfo> activities, int windowingMode) {
        if (this.mDisplayWindowPolicyController == null) {
            for (int i = 0; i < activities.size(); i++) {
                if (hasDisplayCategory(activities.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return this.mDisplayWindowPolicyController.canContainActivities(activities, windowingMode);
    }

    public boolean canActivityBeLaunched(android.content.pm.ActivityInfo activityInfo, android.content.Intent intent, int windowingMode, int launchingFromDisplayId, boolean isNewTask) {
        if (this.mDisplayWindowPolicyController == null) {
            return !hasDisplayCategory(activityInfo);
        }
        return this.mDisplayWindowPolicyController.canActivityBeLaunched(activityInfo, intent, windowingMode, launchingFromDisplayId, isNewTask);
    }

    private boolean hasDisplayCategory(android.content.pm.ActivityInfo aInfo) {
        if (aInfo.requiredDisplayCategory != null) {
            android.util.Slog.d(TAG, java.lang.String.format("Checking activity launch with requiredDisplayCategory='%s' on display %d, which doesn't have a matching category.", aInfo.requiredDisplayCategory, java.lang.Integer.valueOf(this.mDisplayContent.mDisplayId)));
            return true;
        }
        return false;
    }

    boolean keepActivityOnWindowFlagsChanged(android.content.pm.ActivityInfo aInfo, int flagChanges, int privateFlagChanges, int flagValues, int privateFlagValues) {
        if (this.mDisplayWindowPolicyController != null && this.mDisplayWindowPolicyController.isInterestedWindowFlags(flagChanges, privateFlagChanges)) {
            return this.mDisplayWindowPolicyController.keepActivityOnWindowFlagsChanged(aInfo, flagValues, privateFlagValues);
        }
        return true;
    }

    void onRunningActivityChanged() {
        if (this.mDisplayWindowPolicyController == null) {
            return;
        }
        com.android.server.wm.ActivityRecord topActivity = this.mDisplayContent.getTopActivity(false, true);
        if (topActivity != this.mTopRunningActivity) {
            this.mTopRunningActivity = topActivity;
            if (topActivity == null) {
                this.mDisplayWindowPolicyController.onTopActivityChanged((android.content.ComponentName) null, -1, -10000);
            } else {
                this.mDisplayWindowPolicyController.onTopActivityChanged(topActivity.info.getComponentName(), topActivity.info.applicationInfo.uid, topActivity.mUserId);
            }
        }
        final boolean[] notifyChanged = {false};
        final android.util.ArraySet<java.lang.Integer> runningUids = new android.util.ArraySet<>();
        this.mDisplayContent.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayWindowPolicyControllerHelper$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.DisplayWindowPolicyControllerHelper.lambda$onRunningActivityChanged$0(notifyChanged, runningUids, (com.android.server.wm.ActivityRecord) obj);
            }
        });
        if (notifyChanged[0] || this.mRunningUid.size() != runningUids.size()) {
            this.mRunningUid = runningUids;
            this.mDisplayWindowPolicyController.onRunningAppsChanged(runningUids);
        }
    }

    static /* synthetic */ void lambda$onRunningActivityChanged$0(boolean[] notifyChanged, android.util.ArraySet runningUids, com.android.server.wm.ActivityRecord r) {
        if (!r.finishing) {
            notifyChanged[0] = notifyChanged[0] | runningUids.add(java.lang.Integer.valueOf(r.getUid()));
        }
    }

    public final boolean isWindowingModeSupported(int windowingMode) {
        if (this.mDisplayWindowPolicyController == null) {
            return true;
        }
        return this.mDisplayWindowPolicyController.isWindowingModeSupported(windowingMode);
    }

    public final boolean canShowTasksInHostDeviceRecents() {
        if (this.mDisplayWindowPolicyController == null) {
            return true;
        }
        return this.mDisplayWindowPolicyController.canShowTasksInHostDeviceRecents();
    }

    public final boolean isEnteringPipAllowed(int uid) {
        if (this.mDisplayWindowPolicyController == null) {
            return true;
        }
        return this.mDisplayWindowPolicyController.isEnteringPipAllowed(uid);
    }

    public android.content.ComponentName getCustomHomeComponent() {
        if (this.mDisplayWindowPolicyController == null) {
            return null;
        }
        return this.mDisplayWindowPolicyController.getCustomHomeComponent();
    }

    void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        if (this.mDisplayWindowPolicyController != null) {
            pw.println();
            this.mDisplayWindowPolicyController.dump(prefix, pw);
        }
    }
}
