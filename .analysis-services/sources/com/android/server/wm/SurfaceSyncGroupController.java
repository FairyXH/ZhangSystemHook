package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class SurfaceSyncGroupController {
    private static final java.lang.String TAG = "SurfaceSyncGroupController";
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.ArrayMap<android.os.IBinder, com.android.server.wm.SurfaceSyncGroupController.SurfaceSyncGroupData> mSurfaceSyncGroups = new android.util.ArrayMap<>();

    SurfaceSyncGroupController() {
    }

    boolean addToSyncGroup(android.os.IBinder syncGroupToken, boolean parentSyncGroupMerge, final android.window.ISurfaceSyncGroupCompletedListener completedListener, android.window.AddToSurfaceSyncGroupResult outAddToSyncGroupResult) {
        android.window.SurfaceSyncGroup root;
        synchronized (this.mLock) {
            com.android.server.wm.SurfaceSyncGroupController.SurfaceSyncGroupData syncGroupData = this.mSurfaceSyncGroups.get(syncGroupToken);
            if (syncGroupData == null) {
                root = new android.window.SurfaceSyncGroup("SurfaceSyncGroupController-" + syncGroupToken.hashCode());
                if (completedListener != null) {
                    root.addSyncCompleteCallback(new com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0(), new java.lang.Runnable() { // from class: com.android.server.wm.SurfaceSyncGroupController$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            completedListener.onSurfaceSyncGroupComplete();
                        }
                    });
                }
                this.mSurfaceSyncGroups.put(syncGroupToken, new com.android.server.wm.SurfaceSyncGroupController.SurfaceSyncGroupData(android.os.Binder.getCallingUid(), root));
            } else {
                root = syncGroupData.mSurfaceSyncGroup;
            }
        }
        android.window.ITransactionReadyCallback callback = root.createTransactionReadyCallback(parentSyncGroupMerge);
        if (callback == null) {
            return false;
        }
        outAddToSyncGroupResult.mParentSyncGroup = root.mISurfaceSyncGroup;
        outAddToSyncGroupResult.mTransactionReadyCallback = callback;
        return true;
    }

    void markSyncGroupReady(android.os.IBinder syncGroupToken) {
        android.window.SurfaceSyncGroup root;
        synchronized (this.mLock) {
            com.android.server.wm.SurfaceSyncGroupController.SurfaceSyncGroupData syncGroupData = this.mSurfaceSyncGroups.get(syncGroupToken);
            if (syncGroupData == null) {
                throw new java.lang.IllegalArgumentException("SurfaceSyncGroup Token has not been set up or has already been marked as ready");
            }
            if (syncGroupData.mOwningUid != android.os.Binder.getCallingUid()) {
                throw new java.lang.IllegalArgumentException("Only process that created the SurfaceSyncGroup can call markSyncGroupReady");
            }
            root = syncGroupData.mSurfaceSyncGroup;
            this.mSurfaceSyncGroups.remove(syncGroupToken);
        }
        root.markSyncReady();
    }

    private static class SurfaceSyncGroupData {
        final int mOwningUid;
        final android.window.SurfaceSyncGroup mSurfaceSyncGroup;

        private SurfaceSyncGroupData(int owningUid, android.window.SurfaceSyncGroup surfaceSyncGroup) {
            this.mOwningUid = owningUid;
            this.mSurfaceSyncGroup = surfaceSyncGroup;
        }
    }
}
