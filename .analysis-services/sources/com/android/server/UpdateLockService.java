package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class UpdateLockService extends android.os.IUpdateLock.Stub {
    static final boolean DEBUG = false;
    static final java.lang.String PERMISSION = "android.permission.UPDATE_LOCK";
    static final java.lang.String TAG = "UpdateLockService";
    android.content.Context mContext;
    com.android.server.UpdateLockService.LockWatcher mLocks = new com.android.server.UpdateLockService.LockWatcher(new android.os.Handler(), "UpdateLocks");

    class LockWatcher extends android.os.TokenWatcher {
        LockWatcher(android.os.Handler h, java.lang.String tag) {
            super(h, tag);
        }

        @Override // android.os.TokenWatcher
        public void acquired() {
            com.android.server.UpdateLockService.this.sendLockChangedBroadcast(false);
        }

        @Override // android.os.TokenWatcher
        public void released() {
            com.android.server.UpdateLockService.this.sendLockChangedBroadcast(true);
        }
    }

    UpdateLockService(android.content.Context context) {
        this.mContext = context;
        sendLockChangedBroadcast(true);
    }

    void sendLockChangedBroadcast(boolean state) {
        long oldIdent = android.os.Binder.clearCallingIdentity();
        try {
            android.content.Intent intent = new android.content.Intent("android.os.UpdateLock.UPDATE_LOCK_CHANGED").putExtra("nowisconvenient", state).putExtra(com.android.server.net.watchlist.WatchlistLoggingHandler.WatchlistEventKeys.TIMESTAMP, java.lang.System.currentTimeMillis()).addFlags(67108864);
            this.mContext.sendStickyBroadcastAsUser(intent, android.os.UserHandle.ALL);
        } finally {
            android.os.Binder.restoreCallingIdentity(oldIdent);
        }
    }

    public void acquireUpdateLock(android.os.IBinder token, java.lang.String tag) throws android.os.RemoteException {
        this.mContext.enforceCallingOrSelfPermission(PERMISSION, "acquireUpdateLock");
        this.mLocks.acquire(token, makeTag(tag));
    }

    public void releaseUpdateLock(android.os.IBinder token) throws android.os.RemoteException {
        this.mContext.enforceCallingOrSelfPermission(PERMISSION, "releaseUpdateLock");
        this.mLocks.release(token);
    }

    private java.lang.String makeTag(java.lang.String tag) {
        return "{tag=" + tag + " uid=" + android.os.Binder.getCallingUid() + " pid=" + android.os.Binder.getCallingPid() + '}';
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            this.mLocks.dump(pw);
        }
    }
}
