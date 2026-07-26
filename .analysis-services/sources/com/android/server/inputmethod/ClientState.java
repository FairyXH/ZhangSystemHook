package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class ClientState {
    android.util.SparseArray<com.android.server.inputmethod.InputMethodManagerService.AccessibilitySessionState> mAccessibilitySessions = new android.util.SparseArray<>();
    final android.view.inputmethod.InputBinding mBinding;
    final com.android.server.inputmethod.IInputMethodClientInvoker mClient;
    final android.os.IBinder.DeathRecipient mClientDeathRecipient;
    com.android.server.inputmethod.InputMethodManagerService.SessionState mCurSession;
    final com.android.internal.inputmethod.IRemoteInputConnection mFallbackInputConnection;
    final int mPid;
    final int mSelfReportedDisplayId;
    boolean mSessionRequested;
    boolean mSessionRequestedForAccessibility;
    int mTouchDeviceId;
    final int mUid;

    public java.lang.String toString() {
        return "ClientState{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " mUid=" + this.mUid + " mPid=" + this.mPid + " mSelfReportedDisplayId=" + this.mSelfReportedDisplayId + "}";
    }

    ClientState(com.android.server.inputmethod.IInputMethodClientInvoker client, com.android.internal.inputmethod.IRemoteInputConnection fallbackInputConnection, int uid, int pid, int selfReportedDisplayId, android.os.IBinder.DeathRecipient clientDeathRecipient) {
        this.mClient = client;
        this.mFallbackInputConnection = fallbackInputConnection;
        this.mUid = uid;
        this.mPid = pid;
        this.mSelfReportedDisplayId = selfReportedDisplayId;
        this.mBinding = new android.view.inputmethod.InputBinding(null, this.mFallbackInputConnection.asBinder(), this.mUid, this.mPid);
        this.mClientDeathRecipient = clientDeathRecipient;
    }
}
