package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public abstract class PersistentConnection<T> {
    private static final boolean DEBUG = false;
    private boolean mBound;
    private final android.content.ComponentName mComponentName;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private boolean mIsConnected;
    private long mLastConnectedTime;
    private long mNextBackoffMs;
    private int mNumBindingDied;
    private int mNumConnected;
    private int mNumDisconnected;
    private final double mRebindBackoffIncrease;
    private final long mRebindBackoffMs;
    private final long mRebindMaxBackoffMs;
    private boolean mRebindScheduled;
    private long mReconnectTime;
    private final long mResetBackoffDelay;
    private T mService;
    private boolean mShouldBeBound;
    private final java.lang.String mTag;
    private final int mUserId;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.content.ServiceConnection mServiceConnection = new android.content.ServiceConnection() { // from class: com.android.server.am.PersistentConnection.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            synchronized (com.android.server.am.PersistentConnection.this.mLock) {
                if (!com.android.server.am.PersistentConnection.this.mBound) {
                    android.util.Log.w(com.android.server.am.PersistentConnection.this.mTag, "Connected: " + com.android.server.am.PersistentConnection.this.mComponentName.flattenToShortString() + " u" + com.android.server.am.PersistentConnection.this.mUserId + " but not bound, ignore.");
                    return;
                }
                android.util.Log.i(com.android.server.am.PersistentConnection.this.mTag, "Connected: " + com.android.server.am.PersistentConnection.this.mComponentName.flattenToShortString() + " u" + com.android.server.am.PersistentConnection.this.mUserId);
                com.android.server.am.PersistentConnection.this.mNumConnected++;
                com.android.server.am.PersistentConnection.this.mIsConnected = true;
                com.android.server.am.PersistentConnection.this.mLastConnectedTime = com.android.server.am.PersistentConnection.this.injectUptimeMillis();
                com.android.server.am.PersistentConnection.this.mService = com.android.server.am.PersistentConnection.this.asInterface(service);
                com.android.server.am.PersistentConnection.this.scheduleStableCheckLocked();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            synchronized (com.android.server.am.PersistentConnection.this.mLock) {
                android.util.Log.i(com.android.server.am.PersistentConnection.this.mTag, "Disconnected: " + com.android.server.am.PersistentConnection.this.mComponentName.flattenToShortString() + " u" + com.android.server.am.PersistentConnection.this.mUserId);
                com.android.server.am.PersistentConnection.this.mNumDisconnected++;
                com.android.server.am.PersistentConnection.this.cleanUpConnectionLocked();
            }
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(android.content.ComponentName name) {
            synchronized (com.android.server.am.PersistentConnection.this.mLock) {
                if (!com.android.server.am.PersistentConnection.this.mBound) {
                    android.util.Log.w(com.android.server.am.PersistentConnection.this.mTag, "Binding died: " + com.android.server.am.PersistentConnection.this.mComponentName.flattenToShortString() + " u" + com.android.server.am.PersistentConnection.this.mUserId + " but not bound, ignore.");
                    return;
                }
                android.util.Log.w(com.android.server.am.PersistentConnection.this.mTag, "Binding died: " + com.android.server.am.PersistentConnection.this.mComponentName.flattenToShortString() + " u" + com.android.server.am.PersistentConnection.this.mUserId);
                com.android.server.am.PersistentConnection.this.mNumBindingDied++;
                com.android.server.am.PersistentConnection.this.scheduleRebindLocked();
            }
        }
    };
    private final java.lang.Runnable mBindForBackoffRunnable = new java.lang.Runnable() { // from class: com.android.server.am.PersistentConnection$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$0();
        }
    };
    private final java.lang.Runnable mStableCheck = new java.lang.Runnable() { // from class: com.android.server.am.PersistentConnection$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.stableConnectionCheck();
        }
    };

    protected abstract T asInterface(android.os.IBinder iBinder);

    protected abstract int getBindFlags();

    public PersistentConnection(java.lang.String tag, android.content.Context context, android.os.Handler handler, int userId, android.content.ComponentName componentName, long rebindBackoffSeconds, double rebindBackoffIncrease, long rebindMaxBackoffSeconds, long resetBackoffDelay) {
        this.mTag = tag;
        this.mContext = context;
        this.mHandler = handler;
        this.mUserId = userId;
        this.mComponentName = componentName;
        this.mRebindBackoffMs = rebindBackoffSeconds * 1000;
        this.mRebindBackoffIncrease = rebindBackoffIncrease;
        this.mRebindMaxBackoffMs = rebindMaxBackoffSeconds * 1000;
        this.mResetBackoffDelay = 1000 * resetBackoffDelay;
        this.mNextBackoffMs = this.mRebindBackoffMs;
    }

    public final android.content.ComponentName getComponentName() {
        return this.mComponentName;
    }

    public final int getUserId() {
        return this.mUserId;
    }

    public final boolean isBound() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mBound;
        }
        return z;
    }

    public final boolean isRebindScheduled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mRebindScheduled;
        }
        return z;
    }

    public final boolean isConnected() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsConnected;
        }
        return z;
    }

    public final T getServiceBinder() {
        T t;
        synchronized (this.mLock) {
            t = this.mService;
        }
        return t;
    }

    public final void bind() {
        synchronized (this.mLock) {
            this.mShouldBeBound = true;
            bindInnerLocked(true);
        }
    }

    public long getNextBackoffMs() {
        long j;
        synchronized (this.mLock) {
            j = this.mNextBackoffMs;
        }
        return j;
    }

    public int getNumConnected() {
        int i;
        synchronized (this.mLock) {
            i = this.mNumConnected;
        }
        return i;
    }

    public int getNumDisconnected() {
        int i;
        synchronized (this.mLock) {
            i = this.mNumDisconnected;
        }
        return i;
    }

    public int getNumBindingDied() {
        int i;
        synchronized (this.mLock) {
            i = this.mNumBindingDied;
        }
        return i;
    }

    private void resetBackoffLocked() {
        if (this.mNextBackoffMs != this.mRebindBackoffMs) {
            this.mNextBackoffMs = this.mRebindBackoffMs;
            android.util.Log.i(this.mTag, "Backoff reset to " + this.mNextBackoffMs);
        }
    }

    public final void bindInnerLocked(boolean resetBackoff) {
        unscheduleRebindLocked();
        if (this.mBound) {
            return;
        }
        this.mBound = true;
        unscheduleStableCheckLocked();
        if (resetBackoff) {
            resetBackoffLocked();
        }
        android.content.Intent service = new android.content.Intent().setComponent(this.mComponentName);
        boolean success = this.mContext.bindServiceAsUser(service, this.mServiceConnection, getBindFlags() | 1, this.mHandler, android.os.UserHandle.of(this.mUserId));
        if (!success) {
            android.util.Log.e(this.mTag, "Binding: " + service.getComponent() + " u" + this.mUserId + " failed.");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: bindForBackoff, reason: merged with bridge method [inline-methods] */
    public final void lambda$new$0() {
        synchronized (this.mLock) {
            if (this.mShouldBeBound) {
                bindInnerLocked(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanUpConnectionLocked() {
        this.mIsConnected = false;
        this.mService = null;
        unscheduleStableCheckLocked();
    }

    public final void unbind() {
        synchronized (this.mLock) {
            this.mShouldBeBound = false;
            unbindLocked();
            unscheduleStableCheckLocked();
        }
    }

    private final void unbindLocked() {
        unscheduleRebindLocked();
        if (!this.mBound) {
            return;
        }
        android.util.Log.i(this.mTag, "Stopping: " + this.mComponentName.flattenToShortString() + " u" + this.mUserId);
        this.mBound = false;
        this.mContext.unbindService(this.mServiceConnection);
        cleanUpConnectionLocked();
    }

    void unscheduleRebindLocked() {
        injectRemoveCallbacks(this.mBindForBackoffRunnable);
        this.mRebindScheduled = false;
    }

    void scheduleRebindLocked() {
        unbindLocked();
        if (!this.mRebindScheduled) {
            android.util.Log.i(this.mTag, "Scheduling to reconnect in " + this.mNextBackoffMs + " ms (uptime)");
            this.mReconnectTime = injectUptimeMillis() + this.mNextBackoffMs;
            injectPostAtTime(this.mBindForBackoffRunnable, this.mReconnectTime);
            this.mNextBackoffMs = java.lang.Math.min(this.mRebindMaxBackoffMs, (long) (this.mNextBackoffMs * this.mRebindBackoffIncrease));
            this.mRebindScheduled = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stableConnectionCheck() {
        synchronized (this.mLock) {
            long now = injectUptimeMillis();
            long timeRemaining = (this.mLastConnectedTime + this.mResetBackoffDelay) - now;
            if (this.mBound && this.mIsConnected && timeRemaining <= 0) {
                resetBackoffLocked();
            }
        }
    }

    private void unscheduleStableCheckLocked() {
        injectRemoveCallbacks(this.mStableCheck);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleStableCheckLocked() {
        unscheduleStableCheckLocked();
        injectPostAtTime(this.mStableCheck, injectUptimeMillis() + this.mResetBackoffDelay);
    }

    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        synchronized (this.mLock) {
            pw.print(prefix);
            pw.print(this.mComponentName.flattenToShortString());
            pw.print(" u");
            pw.print(this.mUserId);
            pw.print(this.mBound ? " [bound]" : " [not bound]");
            pw.print(this.mIsConnected ? " [connected]" : " [not connected]");
            if (this.mRebindScheduled) {
                pw.print(" reconnect in ");
                android.util.TimeUtils.formatDuration(this.mReconnectTime - injectUptimeMillis(), pw);
            }
            pw.println();
            pw.print(prefix);
            pw.print("  Next backoff(sec): ");
            pw.print(this.mNextBackoffMs / 1000);
            pw.println();
            pw.print(prefix);
            pw.print("  Connected: ");
            pw.print(this.mNumConnected);
            pw.print("  Disconnected: ");
            pw.print(this.mNumDisconnected);
            pw.print("  Died: ");
            pw.print(this.mNumBindingDied);
            if (this.mIsConnected) {
                pw.print("  Duration: ");
                android.util.TimeUtils.formatDuration(injectUptimeMillis() - this.mLastConnectedTime, pw);
            }
            pw.println();
        }
    }

    void injectRemoveCallbacks(java.lang.Runnable r) {
        this.mHandler.removeCallbacks(r);
    }

    void injectPostAtTime(java.lang.Runnable r, long uptimeMillis) {
        this.mHandler.postAtTime(r, uptimeMillis);
    }

    long injectUptimeMillis() {
        return android.os.SystemClock.uptimeMillis();
    }

    long getNextBackoffMsForTest() {
        return this.mNextBackoffMs;
    }

    long getReconnectTimeForTest() {
        return this.mReconnectTime;
    }

    android.content.ServiceConnection getServiceConnectionForTest() {
        return this.mServiceConnection;
    }

    java.lang.Runnable getBindForBackoffRunnableForTest() {
        return this.mBindForBackoffRunnable;
    }

    java.lang.Runnable getStableCheckRunnableForTest() {
        return this.mStableCheck;
    }

    boolean shouldBeBoundForTest() {
        return this.mShouldBeBound;
    }
}
