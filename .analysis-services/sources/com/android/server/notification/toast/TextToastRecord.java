package com.android.server.notification.toast;

/* JADX INFO: loaded from: classes2.dex */
public class TextToastRecord extends com.android.server.notification.toast.ToastRecord {
    private static final java.lang.String TAG = "NotificationService";
    private final android.app.ITransientNotificationCallback mCallback;
    private final com.android.server.statusbar.StatusBarManagerInternal mStatusBar;
    public final java.lang.CharSequence text;

    public TextToastRecord(com.android.server.notification.NotificationManagerService notificationManager, com.android.server.statusbar.StatusBarManagerInternal statusBarManager, int uid, int pid, java.lang.String packageName, boolean isSystemToast, android.os.IBinder token, java.lang.CharSequence text, int duration, android.os.Binder windowToken, int displayId, android.app.ITransientNotificationCallback callback) {
        super(notificationManager, uid, pid, packageName, isSystemToast, token, duration, windowToken, displayId);
        this.mStatusBar = statusBarManager;
        this.mCallback = callback;
        this.text = (java.lang.CharSequence) com.android.internal.util.Preconditions.checkNotNull(text);
    }

    @Override // com.android.server.notification.toast.ToastRecord
    public boolean show() {
        if (com.android.server.notification.NotificationManagerService.DBG) {
            android.util.Slog.d("NotificationService", "Show pkg=" + this.pkg + " text=" + ((java.lang.Object) this.text));
        }
        if (this.mStatusBar == null) {
            android.util.Slog.w("NotificationService", "StatusBar not available to show text toast for package " + this.pkg);
            return false;
        }
        this.mStatusBar.showToast(this.uid, this.pkg, this.token, this.text, this.windowToken, getDuration(), this.mCallback, this.displayId);
        return true;
    }

    @Override // com.android.server.notification.toast.ToastRecord
    public void hide() {
        com.android.internal.util.Preconditions.checkNotNull(this.mStatusBar, "Cannot hide toast that wasn't shown");
        this.mStatusBar.hideToast(this.pkg, this.token);
    }

    @Override // com.android.server.notification.toast.ToastRecord
    public boolean isAppRendered() {
        return false;
    }

    public java.lang.String toString() {
        return "TextToastRecord{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " " + this.pid + ":" + this.pkg + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + android.os.UserHandle.formatUid(this.uid) + " isSystemToast=" + this.isSystemToast + " token=" + this.token + " text=" + ((java.lang.Object) this.text) + " duration=" + getDuration() + "}";
    }
}
