package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public abstract class StartingData {
    static final int AFTER_TRANSACTION_COPY_TO_CLIENT = 2;
    static final int AFTER_TRANSACTION_IDLE = 0;
    static final int AFTER_TRANSACTION_REMOVE_DIRECTLY = 1;
    com.android.server.wm.Task mAssociatedTask;
    boolean mIsDisplayed;
    boolean mIsTransitionForward;
    boolean mPrepareRemoveAnimation;
    int mRemoveAfterTransaction = 0;
    boolean mResizedFromTransfer;
    protected final com.android.server.wm.WindowManagerService mService;
    int mTransitionId;
    protected final int mTypeParams;
    boolean mWaitForSyncTransactionCommit;

    @interface AfterTransaction {
    }

    abstract com.android.server.wm.StartingSurfaceController.StartingSurface createStartingSurface(com.android.server.wm.ActivityRecord activityRecord);

    abstract boolean needRevealAnimation();

    protected StartingData(com.android.server.wm.WindowManagerService service, int typeParams) {
        this.mService = service;
        this.mTypeParams = typeParams;
    }

    boolean hasImeSurface() {
        return false;
    }

    public java.lang.String toString() {
        return getClass().getSimpleName() + "{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " waitForSyncTransactionCommit=" + this.mWaitForSyncTransactionCommit + " removeAfterTransaction= " + this.mRemoveAfterTransaction + "}";
    }
}
