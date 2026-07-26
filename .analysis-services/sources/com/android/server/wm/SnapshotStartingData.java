package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class SnapshotStartingData extends com.android.server.wm.StartingData {
    private final com.android.server.wm.WindowManagerService mService;
    private final android.window.TaskSnapshot mSnapshot;

    SnapshotStartingData(com.android.server.wm.WindowManagerService service, android.window.TaskSnapshot snapshot, int typeParams) {
        super(service, typeParams);
        this.mService = service;
        this.mSnapshot = snapshot;
    }

    @Override // com.android.server.wm.StartingData
    com.android.server.wm.StartingSurfaceController.StartingSurface createStartingSurface(com.android.server.wm.ActivityRecord activity) {
        return this.mService.mStartingSurfaceController.createTaskSnapshotSurface(activity, this.mSnapshot);
    }

    @Override // com.android.server.wm.StartingData
    boolean needRevealAnimation() {
        return false;
    }

    @Override // com.android.server.wm.StartingData
    boolean hasImeSurface() {
        return this.mSnapshot.hasImeSurface();
    }
}
