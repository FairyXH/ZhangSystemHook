package com.android.server.policy.keyguard;

/* JADX INFO: loaded from: classes3.dex */
public class KeyguardStateMonitor extends com.android.internal.policy.IKeyguardStateCallback.Stub {
    private static final java.lang.String TAG = "KeyguardStateMonitor";
    private final com.android.server.policy.keyguard.KeyguardStateMonitor.StateCallback mCallback;
    private final com.android.internal.widget.LockPatternUtils mLockPatternUtils;
    private volatile boolean mIsShowing = true;
    private volatile boolean mSimSecure = true;
    private volatile boolean mInputRestricted = true;
    private volatile boolean mTrusted = false;
    private com.android.server.policy.IKeyguardStateMonitorExt mExt = (com.android.server.policy.IKeyguardStateMonitorExt) system.ext.loader.core.ExtLoader.type(com.android.server.policy.IKeyguardStateMonitorExt.class).create();
    private int mCurrentUserId = android.app.ActivityManager.getCurrentUser();

    public interface StateCallback {
        void onShowingChanged();

        void onTrustedChanged();
    }

    public KeyguardStateMonitor(android.content.Context context, com.android.internal.policy.IKeyguardService service, com.android.server.policy.keyguard.KeyguardStateMonitor.StateCallback callback) {
        this.mLockPatternUtils = new com.android.internal.widget.LockPatternUtils(context);
        this.mCallback = callback;
        try {
            service.addStateMonitorCallback(this);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Remote Exception", e);
        }
    }

    public boolean isShowing() {
        return this.mIsShowing;
    }

    public boolean isSecure(int userId) {
        return this.mLockPatternUtils.isSecure(userId) || this.mSimSecure;
    }

    public boolean isInputRestricted() {
        return this.mInputRestricted;
    }

    public boolean isTrusted() {
        return this.mTrusted;
    }

    public int getCurrentUser() {
        return this.mCurrentUserId;
    }

    public void onShowingStateChanged(boolean showing, int userId) {
        if (userId != this.mCurrentUserId) {
            return;
        }
        this.mIsShowing = showing;
        this.mCallback.onShowingChanged();
    }

    public void onSimSecureStateChanged(boolean simSecure) {
        this.mSimSecure = simSecure;
    }

    public synchronized void setCurrentUser(int userId) {
        this.mCurrentUserId = userId;
    }

    public void onInputRestrictedStateChanged(boolean inputRestricted) {
        this.mInputRestricted = inputRestricted;
    }

    public void onTrustedChanged(boolean trusted) {
        this.mTrusted = trusted;
        if (this.mExt.hasFSDFeature()) {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                this.mCallback.onTrustedChanged();
                return;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }
        this.mCallback.onTrustedChanged();
    }

    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.println(prefix + TAG);
        java.lang.String prefix2 = prefix + "  ";
        pw.println(prefix2 + "mIsShowing=" + this.mIsShowing);
        pw.println(prefix2 + "mSimSecure=" + this.mSimSecure);
        pw.println(prefix2 + "mInputRestricted=" + this.mInputRestricted);
        pw.println(prefix2 + "mTrusted=" + this.mTrusted);
        pw.println(prefix2 + "mCurrentUserId=" + this.mCurrentUserId);
    }
}
