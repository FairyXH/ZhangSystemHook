package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class MultiPackageVerifyingSession {
    private final java.util.List<com.android.server.pm.VerifyingSession> mChildVerifyingSessions;
    private final android.content.pm.IPackageInstallObserver2 mObserver;
    private final android.os.UserHandle mUser;
    private final java.util.Set<com.android.server.pm.VerifyingSession> mVerificationState;

    MultiPackageVerifyingSession(com.android.server.pm.VerifyingSession parent, java.util.List<com.android.server.pm.VerifyingSession> children) throws com.android.server.pm.PackageManagerException {
        this.mUser = parent.getUser();
        if (children.size() == 0) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("No child sessions found!", -21);
        }
        this.mChildVerifyingSessions = children;
        for (int i = 0; i < children.size(); i++) {
            com.android.server.pm.VerifyingSession childVerifyingSession = children.get(i);
            childVerifyingSession.mParentVerifyingSession = this;
        }
        this.mVerificationState = new android.util.ArraySet(this.mChildVerifyingSessions.size());
        this.mObserver = parent.mObserver;
    }

    public void start() {
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Slog.i("PackageManager", "start " + this.mUser + ": " + this);
        }
        android.os.Trace.asyncTraceEnd(262144L, "queueVerify", java.lang.System.identityHashCode(this));
        android.os.Trace.traceBegin(262144L, "startVerify");
        for (com.android.server.pm.VerifyingSession childVerifyingSession : this.mChildVerifyingSessions) {
            childVerifyingSession.handleStartVerify();
        }
        for (com.android.server.pm.VerifyingSession childVerifyingSession2 : this.mChildVerifyingSessions) {
            childVerifyingSession2.handleReturnCode();
        }
        android.os.Trace.traceEnd(262144L);
    }

    public void trySendVerificationCompleteNotification(com.android.server.pm.VerifyingSession child) {
        this.mVerificationState.add(child);
        if (this.mVerificationState.size() != this.mChildVerifyingSessions.size()) {
            return;
        }
        int completeStatus = 1;
        java.lang.String errorMsg = null;
        java.util.Iterator<com.android.server.pm.VerifyingSession> it = this.mVerificationState.iterator();
        while (true) {
            if (it.hasNext()) {
                com.android.server.pm.VerifyingSession childVerifyingSession = it.next();
                int status = childVerifyingSession.getRet();
                if (status != 1) {
                    completeStatus = status;
                    errorMsg = childVerifyingSession.getErrorMessage();
                    break;
                }
            }
        }
        try {
            this.mObserver.onPackageInstalled((java.lang.String) null, completeStatus, errorMsg, new android.os.Bundle());
        } catch (android.os.RemoteException e) {
            android.util.Slog.i("PackageManager", "Observer no longer exists.");
        }
    }

    public java.lang.String toString() {
        return "MultiPackageVerifyingSession{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + "}";
    }
}
