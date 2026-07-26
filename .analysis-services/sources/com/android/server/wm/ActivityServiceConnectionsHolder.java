package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityServiceConnectionsHolder<T> {
    private final com.android.server.wm.ActivityRecord mActivity;
    private android.util.ArraySet<T> mConnections;
    private volatile boolean mIsDisconnecting;

    ActivityServiceConnectionsHolder(com.android.server.wm.ActivityRecord activity) {
        this.mActivity = activity;
    }

    public void addConnection(T c) {
        synchronized (this.mActivity) {
            if (this.mIsDisconnecting) {
                if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_CLEANUP) {
                    android.util.Slog.e("ActivityTaskManager", "Skip adding connection " + c + " to a disconnecting holder of " + this.mActivity);
                }
            } else {
                if (this.mConnections == null) {
                    this.mConnections = new android.util.ArraySet<>();
                }
                this.mConnections.add(c);
            }
        }
    }

    public void removeConnection(T c) {
        synchronized (this.mActivity) {
            if (this.mConnections == null) {
                return;
            }
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_CLEANUP && this.mIsDisconnecting) {
                android.util.Slog.v("ActivityTaskManager", "Remove pending disconnecting " + c + " of " + this.mActivity);
            }
            this.mConnections.remove(c);
        }
    }

    public boolean isActivityVisible() {
        return this.mActivity.mVisibleForServiceConnection;
    }

    public int getActivityPid() {
        com.android.server.wm.WindowProcessController wpc = this.mActivity.app;
        if (wpc != null) {
            return wpc.getPid();
        }
        return -1;
    }

    public void forEachConnection(java.util.function.Consumer<T> consumer) {
        synchronized (this.mActivity) {
            if (this.mConnections != null && !this.mConnections.isEmpty()) {
                android.util.ArraySet<T> connections = new android.util.ArraySet<>(this.mConnections);
                for (int i = connections.size() - 1; i >= 0; i--) {
                    consumer.accept(connections.valueAt(i));
                }
            }
        }
    }

    void disconnectActivityFromServices() {
        if (this.mConnections == null || this.mConnections.isEmpty() || this.mIsDisconnecting) {
            return;
        }
        this.mIsDisconnecting = true;
        this.mActivity.mAtmService.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityServiceConnectionsHolder$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$disconnectActivityFromServices$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$disconnectActivityFromServices$0() {
        this.mActivity.mAtmService.mAmInternal.disconnectActivityFromServices(this);
        this.mIsDisconnecting = false;
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println(prefix + "activity=" + this.mActivity);
    }

    public java.lang.String toString() {
        return java.lang.String.valueOf(this.mConnections);
    }
}
