package com.android.server.contentcapture;

/* JADX INFO: loaded from: classes.dex */
final class ContentCaptureServerSession {
    private static final java.lang.String TAG = com.android.server.contentcapture.ContentCaptureServerSession.class.getSimpleName();
    public final android.content.ComponentName appComponentName;
    final android.os.IBinder mActivityToken;
    private final android.view.contentcapture.ContentCaptureContext mContentCaptureContext;
    private final int mId;
    private final java.lang.Object mLock;
    private final com.android.server.contentcapture.ContentCapturePerUserService mService;
    private final com.android.internal.os.IResultReceiver mSessionStateReceiver;
    private final int mUid;

    ContentCaptureServerSession(java.lang.Object lock, android.os.IBinder activityToken, android.app.assist.ActivityId activityId, com.android.server.contentcapture.ContentCapturePerUserService service, android.content.ComponentName appComponentName, com.android.internal.os.IResultReceiver sessionStateReceiver, int taskId, int displayId, int sessionId, int uid, int flags) {
        com.android.internal.util.Preconditions.checkArgument(sessionId != 0);
        this.mLock = lock;
        this.mActivityToken = activityToken;
        this.appComponentName = appComponentName;
        this.mService = service;
        this.mId = sessionId;
        this.mUid = uid;
        this.mContentCaptureContext = new android.view.contentcapture.ContentCaptureContext(null, activityId, appComponentName, displayId, activityToken, flags);
        this.mSessionStateReceiver = sessionStateReceiver;
        try {
            sessionStateReceiver.asBinder().linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.contentcapture.ContentCaptureServerSession$$ExternalSyntheticLambda0
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    this.f$0.lambda$new$0();
                }
            }, 0);
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "could not register DeathRecipient for " + activityToken);
        }
    }

    boolean isActivitySession(android.os.IBinder activityToken) {
        return this.mActivityToken.equals(activityToken);
    }

    public void notifySessionStartedLocked(com.android.internal.os.IResultReceiver clientReceiver) {
        if (this.mService.mRemoteService == null) {
            android.util.Slog.w(TAG, "notifySessionStartedLocked(): no remote service");
        } else {
            this.mService.mRemoteService.onSessionStarted(this.mContentCaptureContext, this.mId, this.mUid, clientReceiver, 2);
        }
    }

    public void setContentCaptureEnabledLocked(boolean enabled) {
        try {
            android.os.Bundle extras = new android.os.Bundle();
            int i = 1;
            extras.putBoolean(com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED, true);
            com.android.internal.os.IResultReceiver iResultReceiver = this.mSessionStateReceiver;
            if (!enabled) {
                i = 2;
            }
            iResultReceiver.send(i, extras);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Error async reporting result to client: " + e);
        }
    }

    public void sendActivitySnapshotLocked(android.service.contentcapture.SnapshotData snapshotData) {
        android.util.LocalLog logHistory = this.mService.getMaster().mRequestsHistory;
        if (logHistory != null) {
            logHistory.log("snapshot: id=" + this.mId);
        }
        if (this.mService.mRemoteService == null) {
            android.util.Slog.w(TAG, "sendActivitySnapshotLocked(): no remote service");
        } else {
            this.mService.mRemoteService.onActivitySnapshotRequest(this.mId, snapshotData);
        }
    }

    public void removeSelfLocked(boolean notifyRemoteService) {
        try {
            destroyLocked(notifyRemoteService);
        } finally {
            this.mService.removeSessionLocked(this.mId);
        }
    }

    public void destroyLocked(boolean notifyRemoteService) {
        if (this.mService.isVerbose()) {
            android.util.Slog.v(TAG, "destroy(notifyRemoteService=" + notifyRemoteService + ")");
        }
        if (notifyRemoteService) {
            if (this.mService.mRemoteService == null) {
                android.util.Slog.w(TAG, "destroyLocked(): no remote service");
            } else {
                this.mService.mRemoteService.onSessionFinished(this.mId);
            }
        }
    }

    public void resurrectLocked() {
        com.android.server.contentcapture.RemoteContentCaptureService remoteService = this.mService.mRemoteService;
        if (remoteService == null) {
            android.util.Slog.w(TAG, "destroyLocked(: no remote service");
            return;
        }
        if (this.mService.isVerbose()) {
            android.util.Slog.v(TAG, "resurrecting " + this.mActivityToken + " on " + remoteService);
        }
        remoteService.onSessionStarted(new android.view.contentcapture.ContentCaptureContext(this.mContentCaptureContext, 4), this.mId, this.mUid, this.mSessionStateReceiver, 4098);
    }

    public void pauseLocked() {
        if (this.mService.isVerbose()) {
            android.util.Slog.v(TAG, "pausing " + this.mActivityToken);
        }
        android.service.contentcapture.ContentCaptureService.setClientState(this.mSessionStateReceiver, 2052, (android.os.IBinder) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onClientDeath, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0() {
        if (this.mService.isVerbose()) {
            android.util.Slog.v(TAG, "onClientDeath(" + this.mActivityToken + "): removing session " + this.mId);
        }
        synchronized (this.mLock) {
            removeSelfLocked(true);
        }
    }

    public void dumpLocked(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.print(prefix);
        pw.print("id: ");
        pw.print(this.mId);
        pw.println();
        pw.print(prefix);
        pw.print("uid: ");
        pw.print(this.mUid);
        pw.println();
        pw.print(prefix);
        pw.print("context: ");
        this.mContentCaptureContext.dump(pw);
        pw.println();
        pw.print(prefix);
        pw.print("activity token: ");
        pw.println(this.mActivityToken);
        pw.print(prefix);
        pw.print("app component: ");
        pw.println(this.appComponentName);
        pw.print(prefix);
        pw.print("has autofill callback: ");
    }

    java.lang.String toShortString() {
        return this.mId + ":" + this.mActivityToken;
    }

    public java.lang.String toString() {
        return "ContentCaptureSession[id=" + this.mId + ", act=" + this.mActivityToken + "]";
    }
}
