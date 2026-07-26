package com.android.server.notification.toast;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ToastRecord {
    public final int displayId;
    public final boolean isSystemToast;
    private int mDuration;
    protected final com.android.server.notification.NotificationManagerService mNotificationManager;
    public final int pid;
    public final java.lang.String pkg;
    public final android.os.IBinder token;
    public final int uid;
    public final android.os.Binder windowToken;

    public abstract void hide();

    public abstract boolean isAppRendered();

    public abstract boolean show();

    protected ToastRecord(com.android.server.notification.NotificationManagerService notificationManager, int uid, int pid, java.lang.String pkg, boolean isSystemToast, android.os.IBinder token, int duration, android.os.Binder windowToken, int displayId) {
        this.mNotificationManager = notificationManager;
        this.uid = uid;
        this.pid = pid;
        this.pkg = pkg;
        this.isSystemToast = isSystemToast;
        this.token = token;
        this.windowToken = windowToken;
        this.displayId = displayId;
        this.mDuration = duration;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public void update(int duration) {
        this.mDuration = duration;
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix, com.android.server.notification.NotificationManagerService.DumpFilter filter) {
        if (filter != null && !filter.matches(this.pkg)) {
            return;
        }
        pw.println(prefix + this);
    }

    public boolean keepProcessAlive() {
        return false;
    }
}
