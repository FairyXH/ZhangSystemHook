package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IBLASTSyncEngineExt {
    default boolean onSurfacePlacement(int syncId, com.android.server.wm.WindowContainer windowContainer, android.util.ArraySet<com.android.server.wm.WindowContainer> rootMembers) {
        return false;
    }

    default boolean tryFinishAheadIfNeed(int syncId, com.android.server.wm.BLASTSyncEngine.SyncGroup group, android.util.ArraySet<com.android.server.wm.WindowContainer> rootMembers) {
        return false;
    }

    default boolean skipAddToSync(com.android.server.wm.WindowContainer wc, com.android.server.wm.BLASTSyncEngine.SyncGroup currSG, com.android.server.wm.BLASTSyncEngine.SyncGroup dependSG) {
        return false;
    }

    default void onTimeout(com.android.server.wm.WindowManagerService wms) {
    }

    default void logOutUnfinishedcontainerInfo(com.android.server.wm.BLASTSyncEngine.SyncGroup group, com.android.server.wm.WindowContainer wc) {
    }

    default void onTimeout(com.android.server.wm.WindowManagerService wms, com.android.server.wm.BLASTSyncEngine.SyncGroup timeoutGroup) {
    }

    default void dump(java.io.PrintWriter pw, java.lang.String prefix, com.android.server.wm.BLASTSyncEngine syncEngine, java.util.ArrayList<com.android.server.wm.BLASTSyncEngine.SyncGroup> activeSyncs) {
    }

    default void syncTransitionCommitTimeout(android.util.ArraySet<com.android.server.wm.WindowContainer> wcAwaitingCommit, int syncGroupId, android.view.SurfaceControl.Transaction merged) {
    }

    default boolean shouldSkipSyncFinishCheck(int syncGroupId, com.android.server.wm.WindowContainer wc) {
        return false;
    }

    default boolean shouldSkipFinishNowForQuickLaunch(int syncGroupId, android.util.ArraySet<com.android.server.wm.WindowContainer> rootMembers) {
        return false;
    }

    default boolean shouldSetReady(com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default void showStartingSurface(com.android.server.wm.ActivityRecord ar, android.view.SurfaceControl.Transaction merged) {
    }

    default long adjustSyncTimeout(long timeoutMs, int syncGroupId, com.android.server.wm.WindowManagerService wms) {
        return timeoutMs;
    }
}
