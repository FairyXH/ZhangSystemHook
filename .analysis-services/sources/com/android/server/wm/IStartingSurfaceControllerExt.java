package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IStartingSurfaceControllerExt {
    default boolean canIgnoreNoMainWindow(com.android.server.wm.ActivityRecord activityRecord) {
        return false;
    }
}
