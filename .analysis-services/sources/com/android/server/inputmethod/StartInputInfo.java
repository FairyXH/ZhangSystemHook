package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class StartInputInfo {
    private static final java.util.concurrent.atomic.AtomicInteger sSequenceNumber = new java.util.concurrent.atomic.AtomicInteger(0);
    final int mClientBindSequenceNumber;
    final android.view.inputmethod.EditorInfo mEditorInfo;
    final int mImeDisplayId;
    final java.lang.String mImeId;
    final android.os.IBinder mImeToken;
    final int mImeUserId;
    final boolean mRestarting;
    final int mStartInputReason;
    final int mTargetDisplayId;
    final int mTargetUserId;
    final android.os.IBinder mTargetWindow;
    final int mTargetWindowSoftInputMode;
    final int mSequenceNumber = sSequenceNumber.getAndIncrement();
    final long mTimestamp = android.os.SystemClock.uptimeMillis();
    final long mWallTime = java.lang.System.currentTimeMillis();

    StartInputInfo(int imeUserId, android.os.IBinder imeToken, int imeDisplayId, java.lang.String imeId, int startInputReason, boolean restarting, int targetUserId, int targetDisplayId, android.os.IBinder targetWindow, android.view.inputmethod.EditorInfo editorInfo, int targetWindowSoftInputMode, int clientBindSequenceNumber) {
        this.mImeUserId = imeUserId;
        this.mImeToken = imeToken;
        this.mImeDisplayId = imeDisplayId;
        this.mImeId = imeId;
        this.mStartInputReason = startInputReason;
        this.mRestarting = restarting;
        this.mTargetUserId = targetUserId;
        this.mTargetDisplayId = targetDisplayId;
        this.mTargetWindow = targetWindow;
        this.mEditorInfo = editorInfo;
        this.mTargetWindowSoftInputMode = targetWindowSoftInputMode;
        this.mClientBindSequenceNumber = clientBindSequenceNumber;
    }
}
