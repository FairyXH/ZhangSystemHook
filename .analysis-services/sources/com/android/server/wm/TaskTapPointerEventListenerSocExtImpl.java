package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class TaskTapPointerEventListenerSocExtImpl implements com.android.server.wm.ITaskTapPointerEventListenerSocExt {
    public android.util.BoostFramework mPerfObj;

    public TaskTapPointerEventListenerSocExtImpl(java.lang.Object listener) {
        this.mPerfObj = null;
        if (this.mPerfObj == null) {
            this.mPerfObj = new android.util.BoostFramework();
        }
    }

    @Override // com.android.server.wm.ITaskTapPointerEventListenerSocExt
    public void onPointerEventCheck() {
        if (com.android.server.wm.ActivityTaskSupervisor.mIsPerfBoostAcquired && this.mPerfObj != null) {
            if (com.android.server.wm.ActivityTaskSupervisor.mPerfHandle > 0) {
                this.mPerfObj.perfLockReleaseHandler(com.android.server.wm.ActivityTaskSupervisor.mPerfHandle);
                com.android.server.wm.ActivityTaskSupervisor.mPerfHandle = -1;
            }
            com.android.server.wm.ActivityTaskSupervisor.mIsPerfBoostAcquired = false;
        }
        if (com.android.server.wm.ActivityTaskSupervisor.mPerfSendTapHint && this.mPerfObj != null) {
            this.mPerfObj.perfHint(4163, (java.lang.String) null);
            com.android.server.wm.ActivityTaskSupervisor.mPerfSendTapHint = false;
        }
        if (com.android.server.wm.RootWindowContainer.mIsPerfBoostAcquired && this.mPerfObj != null) {
            if (com.android.server.wm.RootWindowContainer.mPerfHandle > 0) {
                this.mPerfObj.perfLockReleaseHandler(com.android.server.wm.RootWindowContainer.mPerfHandle);
                com.android.server.wm.RootWindowContainer.mPerfHandle = -1;
            }
            com.android.server.wm.RootWindowContainer.mIsPerfBoostAcquired = false;
        }
        if (com.android.server.wm.RootWindowContainer.mPerfSendTapHint && this.mPerfObj != null) {
            this.mPerfObj.perfHint(4163, (java.lang.String) null);
            com.android.server.wm.RootWindowContainer.mPerfSendTapHint = false;
        }
    }
}
