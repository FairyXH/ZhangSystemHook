package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
class AccessCheckDelegateHelper {
    private com.android.server.pm.permission.AccessCheckDelegate mAccessCheckDelegate;
    private final java.util.List<com.android.server.am.ActiveInstrumentation> mActiveInstrumentation;
    private final com.android.server.appop.AppOpsService mAppOpsService;
    private final com.android.server.pm.permission.PermissionManagerServiceInternal mPermissionManagerInternal;
    private final com.android.server.am.ActivityManagerGlobalLock mProcLock;

    AccessCheckDelegateHelper(com.android.server.am.ActivityManagerGlobalLock procLock, java.util.List<com.android.server.am.ActiveInstrumentation> activeInstrumentation, com.android.server.appop.AppOpsService appOpsService, com.android.server.pm.permission.PermissionManagerServiceInternal permissionManagerInternal) {
        this.mProcLock = procLock;
        this.mActiveInstrumentation = activeInstrumentation;
        this.mAppOpsService = appOpsService;
        this.mPermissionManagerInternal = permissionManagerInternal;
    }

    private com.android.server.pm.permission.AccessCheckDelegate getAccessCheckDelegateLPr(boolean create) {
        if (create && this.mAccessCheckDelegate == null) {
            this.mAccessCheckDelegate = new com.android.server.pm.permission.AccessCheckDelegate.AccessCheckDelegateImpl();
            this.mAppOpsService.setCheckOpsDelegate(this.mAccessCheckDelegate);
            this.mPermissionManagerInternal.setCheckPermissionDelegate(this.mAccessCheckDelegate);
        }
        return this.mAccessCheckDelegate;
    }

    private void removeAccessCheckDelegateLPr() {
        this.mAccessCheckDelegate = null;
        this.mAppOpsService.setCheckOpsDelegate(null);
        this.mPermissionManagerInternal.setCheckPermissionDelegate(null);
    }

    void startDelegateShellPermissionIdentity(int delegateUid, java.lang.String[] permissions) {
        if (android.os.UserHandle.getCallingAppId() != 2000 && android.os.UserHandle.getCallingAppId() != 0) {
            throw new java.lang.SecurityException("Only the shell can delegate its permissions");
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                com.android.server.pm.permission.AccessCheckDelegate delegate = getAccessCheckDelegateLPr(false);
                if (delegate != null && !delegate.isDelegateAndOwnerUid(delegateUid)) {
                    throw new java.lang.SecurityException("Shell can delegate permissions only to one instrumentation at a time");
                }
                int instrCount = this.mActiveInstrumentation.size();
                for (int i = 0; i < instrCount; i++) {
                    com.android.server.am.ActiveInstrumentation instr = this.mActiveInstrumentation.get(i);
                    if (instr.mTargetInfo.uid == delegateUid) {
                        if (instr.mUiAutomationConnection == null) {
                            throw new java.lang.SecurityException("Shell can delegate its permissions only to an instrumentation started from the shell");
                        }
                        java.lang.String packageName = instr.mTargetInfo.packageName;
                        getAccessCheckDelegateLPr(true).setShellPermissionDelegate(delegateUid, packageName, permissions);
                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        return;
                    }
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
    }

    void stopDelegateShellPermissionIdentity() {
        if (android.os.UserHandle.getCallingAppId() != 2000 && android.os.UserHandle.getCallingAppId() != 0) {
            throw new java.lang.SecurityException("Only the shell can delegate its permissions");
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                com.android.server.pm.permission.AccessCheckDelegate delegate = getAccessCheckDelegateLPr(false);
                if (delegate == null) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return;
                }
                if (!delegate.hasShellPermissionDelegate()) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return;
                }
                delegate.removeShellPermissionDelegate();
                if (!delegate.hasDelegateOrOverrides()) {
                    removeAccessCheckDelegateLPr();
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
    }

    java.util.List<java.lang.String> getDelegatedShellPermissions() {
        if (android.os.UserHandle.getCallingAppId() != 2000 && android.os.UserHandle.getCallingAppId() != 0) {
            throw new java.lang.SecurityException("Only the shell can get delegated permissions");
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                com.android.server.pm.permission.AccessCheckDelegate delegate = getAccessCheckDelegateLPr(false);
                if (delegate == null) {
                    java.util.List<java.lang.String> list = java.util.Collections.EMPTY_LIST;
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return list;
                }
                java.util.List<java.lang.String> delegatedPermissionNames = delegate.getDelegatedPermissionNames();
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                return delegatedPermissionNames;
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
    }

    void addOverridePermissionState(int originatingUid, int uid, java.lang.String permission, int result) {
        if (android.os.UserHandle.getCallingAppId() != 0) {
            throw new java.lang.SecurityException("Only root can override permissions");
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                int instrCount = this.mActiveInstrumentation.size();
                for (int i = 0; i < instrCount; i++) {
                    com.android.server.am.ActiveInstrumentation instr = this.mActiveInstrumentation.get(i);
                    if (instr.mTargetInfo.uid == originatingUid) {
                        if (instr.mSourceUid != 0 || instr.mUiAutomationConnection == null) {
                            throw new java.lang.SecurityException("Root can only override permissions only if the owning app was instrumented from root.");
                        }
                        com.android.server.pm.permission.AccessCheckDelegate delegate = getAccessCheckDelegateLPr(true);
                        if (delegate.hasOverriddenPermissions() && !delegate.isDelegateAndOwnerUid(originatingUid)) {
                            throw new java.lang.SecurityException("Only one instrumentation to grant overrides is allowed at a time.");
                        }
                        delegate.addOverridePermissionState(originatingUid, uid, permission, result);
                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        return;
                    }
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
    }

    void removeOverridePermissionState(int originatingUid, int uid, java.lang.String permission) {
        if (android.os.UserHandle.getCallingAppId() != 0) {
            throw new java.lang.SecurityException("Only root can override permissions.");
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                com.android.server.pm.permission.AccessCheckDelegate delegate = getAccessCheckDelegateLPr(false);
                if (delegate == null) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return;
                }
                if (!delegate.isDelegateAndOwnerUid(originatingUid)) {
                    if (delegate.hasOverriddenPermissions()) {
                        throw new java.lang.SecurityException("Only the granter of current overrides can remove them.");
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                } else {
                    delegate.removeOverridePermissionState(uid, permission);
                    if (!delegate.hasDelegateOrOverrides()) {
                        removeAccessCheckDelegateLPr();
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
    }

    void clearOverridePermissionStates(int originatingUid, int uid) {
        if (android.os.UserHandle.getCallingAppId() != 0) {
            throw new java.lang.SecurityException("Only root can override permissions.");
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                com.android.server.pm.permission.AccessCheckDelegate delegate = getAccessCheckDelegateLPr(false);
                if (delegate == null) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return;
                }
                if (!delegate.isDelegateAndOwnerUid(originatingUid)) {
                    if (delegate.hasOverriddenPermissions()) {
                        throw new java.lang.SecurityException("Only the granter of current overrides can remove them.");
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                } else {
                    delegate.clearOverridePermissionStates(uid);
                    if (!delegate.hasDelegateOrOverrides()) {
                        removeAccessCheckDelegateLPr();
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
    }

    void clearAllOverridePermissionStates(int originatingUid) {
        if (android.os.UserHandle.getCallingAppId() != 0) {
            throw new java.lang.SecurityException("Only root can override permissions.");
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                com.android.server.pm.permission.AccessCheckDelegate delegate = getAccessCheckDelegateLPr(false);
                if (delegate == null) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return;
                }
                if (!delegate.isDelegateAndOwnerUid(originatingUid)) {
                    if (delegate.hasOverriddenPermissions()) {
                        throw new java.lang.SecurityException("Only the granter of current overrides can remove them.");
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                } else {
                    delegate.clearAllOverridePermissionStates();
                    if (!delegate.hasDelegateOrOverrides()) {
                        removeAccessCheckDelegateLPr();
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
    }

    void onInstrumentationFinished(int uid, java.lang.String packageName) {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                com.android.server.pm.permission.AccessCheckDelegate delegate = getAccessCheckDelegateLPr(false);
                if (delegate != null) {
                    if (delegate.isDelegatePackage(uid, packageName)) {
                        delegate.removeShellPermissionDelegate();
                    }
                    if (delegate.isDelegateAndOwnerUid(uid)) {
                        delegate.clearAllOverridePermissionStates();
                    }
                    if (!delegate.hasDelegateOrOverrides()) {
                        removeAccessCheckDelegateLPr();
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
    }
}
