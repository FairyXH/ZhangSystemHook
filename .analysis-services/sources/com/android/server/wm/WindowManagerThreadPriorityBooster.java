package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class WindowManagerThreadPriorityBooster extends com.android.server.ThreadPriorityBooster {
    private final int mAnimationThreadId;
    private boolean mAppTransitionRunning;
    private boolean mBoundsAnimationRunning;
    private final java.lang.Object mLock;
    private final int mSurfaceAnimationThreadId;

    WindowManagerThreadPriorityBooster() {
        super(-4, 5);
        this.mLock = new java.lang.Object();
        this.mAnimationThreadId = com.android.server.AnimationThread.get().getThreadId();
        this.mSurfaceAnimationThreadId = com.android.server.wm.SurfaceAnimationThread.get().getThreadId();
    }

    @Override // com.android.server.ThreadPriorityBooster
    public void boost() {
        int myTid = android.os.Process.myTid();
        if (myTid == this.mAnimationThreadId || myTid == this.mSurfaceAnimationThreadId) {
            return;
        }
        super.boost();
    }

    @Override // com.android.server.ThreadPriorityBooster
    public void reset() {
        int myTid = android.os.Process.myTid();
        if (myTid == this.mAnimationThreadId || myTid == this.mSurfaceAnimationThreadId) {
            return;
        }
        super.reset();
    }

    void setAppTransitionRunning(boolean running) {
        synchronized (this.mLock) {
            if (this.mAppTransitionRunning != running) {
                this.mAppTransitionRunning = running;
                updatePriorityLocked();
            }
        }
    }

    void setBoundsAnimationRunning(boolean running) {
        synchronized (this.mLock) {
            if (this.mBoundsAnimationRunning != running) {
                this.mBoundsAnimationRunning = running;
                updatePriorityLocked();
            }
        }
    }

    private void updatePriorityLocked() {
        int priority = (this.mAppTransitionRunning || this.mBoundsAnimationRunning) ? -10 : -4;
        setBoostToPriority(priority);
        android.os.Process.setThreadPriority(this.mAnimationThreadId, priority);
        android.os.Process.setThreadPriority(this.mSurfaceAnimationThreadId, priority);
    }
}
