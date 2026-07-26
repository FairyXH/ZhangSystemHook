package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class SafeActivityOptions {
    private static final java.lang.String TAG = "ActivityTaskManager";
    private android.app.ActivityOptions mCallerOptions;
    public com.android.server.wm.ISafeActivityOptionsExt mExt;
    private final int mOriginalCallingPid;
    private final int mOriginalCallingUid;
    private final android.app.ActivityOptions mOriginalOptions;
    private int mRealCallingPid;
    private int mRealCallingUid;

    public static com.android.server.wm.SafeActivityOptions fromBundle(android.os.Bundle bOptions) {
        if (bOptions != null) {
            return new com.android.server.wm.SafeActivityOptions(android.app.ActivityOptions.fromBundle(bOptions));
        }
        return null;
    }

    static com.android.server.wm.SafeActivityOptions fromBundle(android.os.Bundle bOptions, int callingPid, int callingUid) {
        if (bOptions != null) {
            return new com.android.server.wm.SafeActivityOptions(android.app.ActivityOptions.fromBundle(bOptions), callingPid, callingUid);
        }
        return null;
    }

    public SafeActivityOptions(android.app.ActivityOptions options) {
        this.mExt = (com.android.server.wm.ISafeActivityOptionsExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ISafeActivityOptionsExt.class).base(this).create();
        this.mOriginalCallingPid = android.os.Binder.getCallingPid();
        this.mOriginalCallingUid = android.os.Binder.getCallingUid();
        this.mOriginalOptions = options;
    }

    private SafeActivityOptions(android.app.ActivityOptions options, int callingPid, int callingUid) {
        this.mExt = (com.android.server.wm.ISafeActivityOptionsExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ISafeActivityOptionsExt.class).base(this).create();
        this.mOriginalCallingPid = callingPid;
        this.mOriginalCallingUid = callingUid;
        this.mOriginalOptions = options;
    }

    com.android.server.wm.SafeActivityOptions selectiveCloneLaunchOptions() {
        android.app.ActivityOptions options = cloneLaunchingOptions(this.mOriginalOptions);
        android.app.ActivityOptions callerOptions = cloneLaunchingOptions(this.mCallerOptions);
        if (options == null && callerOptions == null) {
            return null;
        }
        com.android.server.wm.SafeActivityOptions safeOptions = new com.android.server.wm.SafeActivityOptions(options, this.mOriginalCallingPid, this.mOriginalCallingUid);
        safeOptions.mCallerOptions = callerOptions;
        safeOptions.mRealCallingPid = this.mRealCallingPid;
        safeOptions.mRealCallingUid = this.mRealCallingUid;
        return safeOptions;
    }

    private android.app.ActivityOptions cloneLaunchingOptions(android.app.ActivityOptions options) {
        if (options == null) {
            return null;
        }
        return android.app.ActivityOptions.makeBasic().setLaunchTaskDisplayArea(options.getLaunchTaskDisplayArea()).setLaunchDisplayId(options.getLaunchDisplayId()).setCallerDisplayId(options.getCallerDisplayId()).setLaunchRootTask(options.getLaunchRootTask()).setPendingIntentBackgroundActivityStartMode(options.getPendingIntentBackgroundActivityStartMode()).setPendingIntentCreatorBackgroundActivityStartMode(options.getPendingIntentCreatorBackgroundActivityStartMode()).setRemoteTransition(options.getRemoteTransition());
    }

    public void setCallerOptions(android.app.ActivityOptions options) {
        this.mRealCallingPid = android.os.Binder.getCallingPid();
        this.mRealCallingUid = android.os.Binder.getCallingUid();
        this.mCallerOptions = options;
    }

    android.app.ActivityOptions getOptions(com.android.server.wm.ActivityRecord r) throws java.lang.SecurityException {
        return getOptions(r.intent, r.info, r.app, r.mTaskSupervisor);
    }

    android.app.ActivityOptions getOptions(com.android.server.wm.ActivityTaskSupervisor supervisor) throws java.lang.SecurityException {
        return getOptions(null, null, null, supervisor);
    }

    android.app.ActivityOptions getOptions(android.content.Intent intent, android.content.pm.ActivityInfo aInfo, com.android.server.wm.WindowProcessController callerApp, com.android.server.wm.ActivityTaskSupervisor supervisor) throws java.lang.SecurityException {
        if (this.mOriginalOptions != null) {
            checkPermissions(intent, aInfo, callerApp, supervisor, this.mOriginalOptions, this.mOriginalCallingPid, this.mOriginalCallingUid);
            setCallingPidUidForRemoteAnimationAdapter(this.mOriginalOptions, this.mOriginalCallingPid, this.mOriginalCallingUid);
        }
        if (this.mCallerOptions != null) {
            checkPermissions(intent, aInfo, callerApp, supervisor, this.mCallerOptions, this.mRealCallingPid, this.mRealCallingUid);
            setCallingPidUidForRemoteAnimationAdapter(this.mCallerOptions, this.mRealCallingPid, this.mRealCallingUid);
        }
        return mergeActivityOptions(this.mOriginalOptions, this.mCallerOptions);
    }

    private void setCallingPidUidForRemoteAnimationAdapter(android.app.ActivityOptions options, int callingPid, int callingUid) {
        android.view.RemoteAnimationAdapter adapter = options.getRemoteAnimationAdapter();
        if (adapter == null) {
            return;
        }
        if (callingPid == com.android.server.wm.WindowManagerService.MY_PID) {
            android.util.Slog.wtf(TAG, "Safe activity options constructed after clearing calling id");
        } else {
            adapter.setCallingPidUid(callingPid, callingUid);
        }
    }

    android.app.ActivityOptions getOriginalOptions() {
        return this.mOriginalOptions;
    }

    android.os.Bundle popAppVerificationBundle() {
        if (this.mOriginalOptions != null) {
            return this.mOriginalOptions.popAppVerificationBundle();
        }
        return null;
    }

    private void abort() {
        if (this.mOriginalOptions != null) {
            android.app.ActivityOptions.abort(this.mOriginalOptions);
        }
        if (this.mCallerOptions != null) {
            android.app.ActivityOptions.abort(this.mCallerOptions);
        }
    }

    static void abort(com.android.server.wm.SafeActivityOptions options) {
        if (options != null) {
            options.abort();
        }
    }

    android.app.ActivityOptions mergeActivityOptions(android.app.ActivityOptions options1, android.app.ActivityOptions options2) {
        if (options1 == null) {
            return options2;
        }
        if (options2 == null) {
            return options1;
        }
        android.os.Bundle b1 = options1.toBundle();
        android.os.Bundle b2 = options2.toBundle();
        b1.putAll(b2);
        return android.app.ActivityOptions.fromBundle(b1);
    }

    private void checkPermissions(android.content.Intent intent, android.content.pm.ActivityInfo aInfo, com.android.server.wm.WindowProcessController callerApp, com.android.server.wm.ActivityTaskSupervisor supervisor, android.app.ActivityOptions options, int callingPid, int callingUid) {
        if ((options.getLaunchTaskId() != -1 || options.getDisableStartingWindow()) && !this.mExt.isPuttDisplay(options.getLaunchDisplayId()) && !supervisor.mRecentTasks.isCallerRecents(callingUid)) {
            int startInTaskPerm = com.android.server.wm.ActivityTaskManagerService.checkPermission("android.permission.START_TASKS_FROM_RECENTS", callingPid, callingUid);
            if (startInTaskPerm == -1) {
                java.lang.String msg = "Permission Denial: starting " + getIntentString(intent) + " from " + callerApp + " (pid=" + callingPid + ", uid=" + callingUid + ") with launchTaskId=" + options.getLaunchTaskId();
                android.util.Slog.w(TAG, msg);
                throw new java.lang.SecurityException(msg);
            }
        }
        if (!options.getTransientLaunch() || supervisor.mRecentTasks.isCallerRecents(callingUid) || com.android.server.wm.ActivityTaskManagerService.checkPermission("android.permission.MANAGE_ACTIVITY_TASKS", callingPid, callingUid) != -1) {
            com.android.server.wm.TaskDisplayArea taskDisplayArea = getLaunchTaskDisplayArea(options, supervisor);
            if (aInfo != null && taskDisplayArea != null && !supervisor.isCallerAllowedToLaunchOnTaskDisplayArea(callingPid, callingUid, taskDisplayArea, aInfo)) {
                java.lang.String msg2 = "Permission Denial: starting " + getIntentString(intent) + " from " + callerApp + " (pid=" + callingPid + ", uid=" + callingUid + ") with launchTaskDisplayArea=" + taskDisplayArea;
                android.util.Slog.w(TAG, msg2);
                throw new java.lang.SecurityException(msg2);
            }
            int launchDisplayId = options.getLaunchDisplayId();
            if (aInfo != null && launchDisplayId != -1 && !supervisor.isCallerAllowedToLaunchOnDisplay(callingPid, callingUid, launchDisplayId, aInfo)) {
                java.lang.String msg3 = "Permission Denial: starting " + getIntentString(intent) + " from " + callerApp + " (pid=" + callingPid + ", uid=" + callingUid + ") with launchDisplayId=" + launchDisplayId;
                android.util.Slog.w(TAG, msg3);
                throw new java.lang.SecurityException(msg3);
            }
            boolean lockTaskMode = options.getLockTaskMode();
            if (aInfo != null && lockTaskMode) {
                if (!supervisor.mService.getLockTaskController().isPackageAllowlisted(android.os.UserHandle.getUserId(callingUid), aInfo.packageName)) {
                    java.lang.String msg4 = "Permission Denial: starting " + getIntentString(intent) + " from " + callerApp + " (pid=" + callingPid + ", uid=" + callingUid + ") with lockTaskMode=true";
                    android.util.Slog.w(TAG, msg4);
                    throw new java.lang.SecurityException(msg4);
                }
            }
            boolean overrideTaskTransition = options.getOverrideTaskTransition();
            if (aInfo != null && overrideTaskTransition) {
                int startTasksFromRecentsPerm = com.android.server.wm.ActivityTaskManagerService.checkPermission("android.permission.START_TASKS_FROM_RECENTS", callingPid, callingUid);
                if (startTasksFromRecentsPerm != 0 && !isAssistant(supervisor.mService, callingUid)) {
                    java.lang.String msg5 = "Permission Denial: starting " + getIntentString(intent) + " from " + callerApp + " (pid=" + callingPid + ", uid=" + callingUid + ") with overrideTaskTransition=true";
                    android.util.Slog.w(TAG, msg5);
                    throw new java.lang.SecurityException(msg5);
                }
            }
            boolean dismissKeyguardIfInsecure = options.getDismissKeyguardIfInsecure();
            if (aInfo != null && dismissKeyguardIfInsecure) {
                int controlKeyguardPerm = com.android.server.wm.ActivityTaskManagerService.checkPermission("android.permission.CONTROL_KEYGUARD", callingPid, callingUid);
                if (controlKeyguardPerm != 0) {
                    java.lang.String msg6 = "Permission Denial: starting " + getIntentString(intent) + " from " + callerApp + " (pid=" + callingPid + ", uid=" + callingUid + ") with dismissKeyguardIfInsecure=true";
                    android.util.Slog.w(TAG, msg6);
                    throw new java.lang.SecurityException(msg6);
                }
            }
            android.view.RemoteAnimationAdapter adapter = options.getRemoteAnimationAdapter();
            if (adapter != null) {
                com.android.server.wm.ActivityTaskManagerService activityTaskManagerService = supervisor.mService;
                if (com.android.server.wm.ActivityTaskManagerService.checkPermission("android.permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS", callingPid, callingUid) != 0) {
                    java.lang.String msg7 = "Permission Denial: starting " + getIntentString(intent) + " from " + callerApp + " (pid=" + callingPid + ", uid=" + callingUid + ") with remoteAnimationAdapter";
                    android.util.Slog.w(TAG, msg7);
                    throw new java.lang.SecurityException(msg7);
                }
            }
            android.window.RemoteTransition transition = options.getRemoteTransition();
            if (transition != null) {
                com.android.server.wm.ActivityTaskManagerService activityTaskManagerService2 = supervisor.mService;
                if (com.android.server.wm.ActivityTaskManagerService.checkPermission("android.permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS", callingPid, callingUid) != 0) {
                    java.lang.String msg8 = "Permission Denial: starting " + getIntentString(intent) + " from " + callerApp + " (pid=" + callingPid + ", uid=" + callingUid + ") with remoteTransition";
                    android.util.Slog.w(TAG, msg8);
                    throw new java.lang.SecurityException(msg8);
                }
            }
            if (options.getLaunchedFromBubble() && !isSystemOrSystemUI(callingPid, callingUid)) {
                java.lang.String msg9 = "Permission Denial: starting " + getIntentString(intent) + " from " + callerApp + " (pid=" + callingPid + ", uid=" + callingUid + ") with launchedFromBubble=true";
                android.util.Slog.w(TAG, msg9);
                throw new java.lang.SecurityException(msg9);
            }
            int activityType = options.getLaunchActivityType();
            if (activityType != 0 && !isSystemOrSystemUI(callingPid, callingUid)) {
                boolean activityTypeGranted = false;
                if (activityType == 4 && isAssistant(supervisor.mService, callingUid)) {
                    activityTypeGranted = true;
                }
                if (!activityTypeGranted) {
                    java.lang.String msg10 = "Permission Denial: starting " + getIntentString(intent) + " from " + callerApp + " (pid=" + callingPid + ", uid=" + callingUid + ") with launchActivityType=" + android.app.WindowConfiguration.activityTypeToString(options.getLaunchActivityType());
                    android.util.Slog.w(TAG, msg10);
                    throw new java.lang.SecurityException(msg10);
                }
                return;
            }
            return;
        }
        java.lang.String msg11 = "Permission Denial: starting transient launch from " + callerApp + ", pid=" + callingPid + ", uid=" + callingUid;
        android.util.Slog.w(TAG, msg11);
        throw new java.lang.SecurityException(msg11);
    }

    com.android.server.wm.TaskDisplayArea getLaunchTaskDisplayArea(android.app.ActivityOptions options, com.android.server.wm.ActivityTaskSupervisor supervisor) {
        final int launchTaskDisplayAreaFeatureId;
        android.window.WindowContainerToken daToken = options.getLaunchTaskDisplayArea();
        com.android.server.wm.TaskDisplayArea taskDisplayArea = daToken != null ? (com.android.server.wm.TaskDisplayArea) com.android.server.wm.WindowContainer.fromBinder(daToken.asBinder()) : null;
        if (taskDisplayArea == null && (launchTaskDisplayAreaFeatureId = options.getLaunchTaskDisplayAreaFeatureId()) != -1) {
            int launchDisplayId = options.getLaunchDisplayId() == -1 ? 0 : options.getLaunchDisplayId();
            com.android.server.wm.DisplayContent dc = supervisor.mRootWindowContainer.getDisplayContent(launchDisplayId);
            if (dc != null) {
                return (com.android.server.wm.TaskDisplayArea) dc.getItemFromTaskDisplayAreas(new java.util.function.Function() { // from class: com.android.server.wm.SafeActivityOptions$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return com.android.server.wm.SafeActivityOptions.lambda$getLaunchTaskDisplayArea$0(launchTaskDisplayAreaFeatureId, (com.android.server.wm.TaskDisplayArea) obj);
                    }
                });
            }
            return taskDisplayArea;
        }
        return taskDisplayArea;
    }

    static /* synthetic */ com.android.server.wm.TaskDisplayArea lambda$getLaunchTaskDisplayArea$0(int launchTaskDisplayAreaFeatureId, com.android.server.wm.TaskDisplayArea tda) {
        if (tda.mFeatureId == launchTaskDisplayAreaFeatureId) {
            return tda;
        }
        return null;
    }

    private boolean isAssistant(com.android.server.wm.ActivityTaskManagerService atmService, int callingUid) {
        int uid;
        if (atmService.mActiveVoiceInteractionServiceComponent == null) {
            return false;
        }
        java.lang.String assistantPackage = atmService.mActiveVoiceInteractionServiceComponent.getPackageName();
        try {
            uid = android.app.AppGlobals.getPackageManager().getPackageUid(assistantPackage, 268435456L, android.os.UserHandle.getUserId(callingUid));
        } catch (android.os.RemoteException e) {
        }
        return uid == callingUid;
    }

    private boolean isSystemOrSystemUI(int callingPid, int callingUid) {
        if (callingUid == 1000) {
            return true;
        }
        int statusBarPerm = com.android.server.wm.ActivityTaskManagerService.checkPermission("android.permission.STATUS_BAR_SERVICE", callingPid, callingUid);
        return statusBarPerm == 0;
    }

    private java.lang.String getIntentString(android.content.Intent intent) {
        return intent != null ? intent.toString() : "(no intent)";
    }
}
