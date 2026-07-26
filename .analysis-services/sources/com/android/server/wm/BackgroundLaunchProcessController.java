package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class BackgroundLaunchProcessController {
    private static final long DEFAULT_RESCIND_BAL_FG_PRIVILEGES_BOUND_SERVICE = 261072174;
    private static final java.lang.String TAG = "ActivityTaskManager";
    private final com.android.server.wm.BackgroundActivityStartCallback mBackgroundActivityStartCallback;
    public com.android.server.wm.IBackgroundLaunchProcessControllerExt mBackgroundLaunchProcessControllerExt = (com.android.server.wm.IBackgroundLaunchProcessControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IBackgroundLaunchProcessControllerExt.class).base(this).create();
    private android.util.ArrayMap<android.os.Binder, android.app.BackgroundStartPrivileges> mBackgroundStartPrivileges;
    private android.util.IntArray mBalOptInBoundClientUids;
    private final java.util.function.IntPredicate mUidHasActiveVisibleWindowPredicate;

    BackgroundLaunchProcessController(java.util.function.IntPredicate uidHasActiveVisibleWindowPredicate, com.android.server.wm.BackgroundActivityStartCallback callback) {
        this.mUidHasActiveVisibleWindowPredicate = uidHasActiveVisibleWindowPredicate;
        this.mBackgroundActivityStartCallback = callback;
    }

    com.android.server.wm.BackgroundActivityStartController.BalVerdict areBackgroundActivityStartsAllowed(int pid, int uid, java.lang.String packageName, int appSwitchState, boolean isCheckingForFgsStart, boolean hasActivityInVisibleTask, boolean hasBackgroundActivityStartPrivileges, long lastStopAppSwitchesTime, long lastActivityLaunchTime, long lastActivityFinishTime) {
        boolean allowBoundByForegroundUid;
        if (hasBackgroundActivityStartPrivileges) {
            this.mBackgroundLaunchProcessControllerExt.monitorActivityStartInfo("S_instrumentingWithBackgroundActivityStartPrivileges", isCheckingForFgsStart);
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(6, true, "process instrumenting with background activity starts privileges");
        }
        if (isBackgroundStartAllowedByToken(uid, packageName, isCheckingForFgsStart)) {
            this.mBackgroundLaunchProcessControllerExt.monitorActivityStartInfo("S_CallerisBackgroundStartAllowedByToken", isCheckingForFgsStart);
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(6, true, "process allowed by token");
        }
        if (com.android.window.flags.Flags.balRespectAppSwitchStateWhenCheckBoundByForegroundUid()) {
            allowBoundByForegroundUid = appSwitchState != 0 && isBoundByForegroundUid();
        } else {
            allowBoundByForegroundUid = isBoundByForegroundUid();
        }
        if (allowBoundByForegroundUid) {
            this.mBackgroundLaunchProcessControllerExt.monitorActivityStartInfo("S_CallerIsBoundByForegroundUid", isCheckingForFgsStart);
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(4, false, "process bound by foreground uid");
        }
        if (hasActivityInVisibleTask && appSwitchState != 0) {
            this.mBackgroundLaunchProcessControllerExt.monitorActivityStartInfo("S_CallerHasActivityInVisibleTask", isCheckingForFgsStart);
            return new com.android.server.wm.BackgroundActivityStartController.BalVerdict(9, false, "process has activity in foreground task");
        }
        if (appSwitchState == 2) {
            long now = android.os.SystemClock.uptimeMillis();
            if (now - lastActivityLaunchTime < 10000 || now - lastActivityFinishTime < 10000) {
                if (lastActivityLaunchTime > lastStopAppSwitchesTime || lastActivityFinishTime > lastStopAppSwitchesTime) {
                    this.mBackgroundLaunchProcessControllerExt.monitorActivityStartInfo("S_CallerActivityStartOrFinishRecently", isCheckingForFgsStart);
                    com.android.server.wm.BackgroundActivityStartController.BalVerdict balVerdict = new com.android.server.wm.BackgroundActivityStartController.BalVerdict(8, true, "within 10000ms grace period");
                    balVerdict.setBlockType(1);
                    return balVerdict;
                }
                if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_ACTIVITY_STARTS) {
                    android.util.Slog.d(TAG, "[Process(" + pid + ")] Activity start within 10000ms grace period but also within stop app switch window");
                }
            }
        }
        return com.android.server.wm.BackgroundActivityStartController.BalVerdict.BLOCK;
    }

    private boolean isBackgroundStartAllowedByToken(int uid, java.lang.String packageName, boolean isCheckingForFgsStart) {
        synchronized (this) {
            if (this.mBackgroundStartPrivileges != null && !this.mBackgroundStartPrivileges.isEmpty()) {
                if (isCheckingForFgsStart) {
                    int i = this.mBackgroundStartPrivileges.size();
                    while (true) {
                        int i2 = i - 1;
                        if (i <= 0) {
                            return false;
                        }
                        if (this.mBackgroundStartPrivileges.valueAt(i2).allowsBackgroundFgsStarts()) {
                            return true;
                        }
                        i = i2;
                    }
                } else if (this.mBackgroundActivityStartCallback == null) {
                    int i3 = this.mBackgroundStartPrivileges.size();
                    while (true) {
                        int i4 = i3 - 1;
                        if (i3 <= 0) {
                            return false;
                        }
                        if (this.mBackgroundStartPrivileges.valueAt(i4).allowsBackgroundActivityStarts()) {
                            return true;
                        }
                        i3 = i4;
                    }
                } else {
                    java.util.List<android.os.IBinder> binderTokens = getOriginatingTokensThatAllowBal();
                    if (binderTokens.isEmpty()) {
                        return false;
                    }
                    return this.mBackgroundActivityStartCallback.isActivityStartAllowed(binderTokens, uid, packageName);
                }
            }
            return false;
        }
    }

    private java.util.List<android.os.IBinder> getOriginatingTokensThatAllowBal() {
        java.util.List<android.os.IBinder> originatingTokens = new java.util.ArrayList<>();
        int i = this.mBackgroundStartPrivileges.size();
        while (true) {
            int i2 = i - 1;
            if (i > 0) {
                android.app.BackgroundStartPrivileges privilege = this.mBackgroundStartPrivileges.valueAt(i2);
                if (privilege.allowsBackgroundActivityStarts()) {
                    originatingTokens.add(privilege.getOriginatingToken());
                }
                i = i2;
            } else {
                return originatingTokens;
            }
        }
    }

    private boolean isBoundByForegroundUid() {
        synchronized (this) {
            if (this.mBalOptInBoundClientUids != null) {
                for (int i = this.mBalOptInBoundClientUids.size() - 1; i >= 0; i--) {
                    if (this.mUidHasActiveVisibleWindowPredicate.test(this.mBalOptInBoundClientUids.get(i))) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    void clearBalOptInBoundClientUids() {
        synchronized (this) {
            if (this.mBalOptInBoundClientUids == null) {
                this.mBalOptInBoundClientUids = new android.util.IntArray();
            } else {
                this.mBalOptInBoundClientUids.clear();
            }
        }
    }

    void addBoundClientUid(int clientUid, java.lang.String clientPackageName, long bindFlags) {
        if (!android.app.compat.CompatChanges.isChangeEnabled(DEFAULT_RESCIND_BAL_FG_PRIVILEGES_BOUND_SERVICE, clientPackageName, android.os.UserHandle.getUserHandleForUid(clientUid)) || (512 & bindFlags) != 0) {
            if (this.mBalOptInBoundClientUids == null) {
                this.mBalOptInBoundClientUids = new android.util.IntArray();
            }
            if (this.mBalOptInBoundClientUids.indexOf(clientUid) == -1) {
                this.mBalOptInBoundClientUids.add(clientUid);
            }
        }
    }

    void addOrUpdateAllowBackgroundStartPrivileges(android.os.Binder entity, android.app.BackgroundStartPrivileges backgroundStartPrivileges) {
        java.util.Objects.requireNonNull(entity, "entity");
        java.util.Objects.requireNonNull(backgroundStartPrivileges, "backgroundStartPrivileges");
        com.android.internal.util.Preconditions.checkArgument(backgroundStartPrivileges.allowsAny(), "backgroundStartPrivileges does not allow anything");
        synchronized (this) {
            if (this.mBackgroundStartPrivileges == null) {
                this.mBackgroundStartPrivileges = new android.util.ArrayMap<>();
            }
            this.mBackgroundStartPrivileges.put(entity, backgroundStartPrivileges);
        }
    }

    void removeAllowBackgroundStartPrivileges(android.os.Binder entity) {
        java.util.Objects.requireNonNull(entity, "entity");
        synchronized (this) {
            if (this.mBackgroundStartPrivileges != null) {
                this.mBackgroundStartPrivileges.remove(entity);
            }
        }
    }

    boolean canCloseSystemDialogsByToken(int uid) {
        if (this.mBackgroundActivityStartCallback == null) {
            return false;
        }
        synchronized (this) {
            if (this.mBackgroundStartPrivileges != null && !this.mBackgroundStartPrivileges.isEmpty()) {
                return this.mBackgroundActivityStartCallback.canCloseSystemDialogs(getOriginatingTokensThatAllowBal(), uid);
            }
            return false;
        }
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        synchronized (this) {
            if (this.mBackgroundStartPrivileges != null && !this.mBackgroundStartPrivileges.isEmpty()) {
                pw.print(prefix);
                pw.println("Background activity start tokens (token: originating token):");
                for (int i = this.mBackgroundStartPrivileges.size() - 1; i >= 0; i--) {
                    pw.print(prefix);
                    pw.print("  - ");
                    pw.print(this.mBackgroundStartPrivileges.keyAt(i));
                    pw.print(": ");
                    pw.println(this.mBackgroundStartPrivileges.valueAt(i));
                }
            }
            if (this.mBalOptInBoundClientUids != null && this.mBalOptInBoundClientUids.size() > 0) {
                pw.print(prefix);
                pw.print("BoundClientUids:");
                pw.println(java.util.Arrays.toString(this.mBalOptInBoundClientUids.toArray()));
            }
        }
    }
}
