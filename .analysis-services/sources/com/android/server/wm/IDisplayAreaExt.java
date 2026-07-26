package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IDisplayAreaExt {
    default boolean shouldBlockOrientingWindowDuringFixedRotation(com.android.server.wm.WindowManagerService mWmService, com.android.server.wm.DisplayContent mDisplayContent, com.android.server.wm.WindowState w, int req) {
        return false;
    }
}
