package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IDisplayFramesExt {
    default void removeSecondaryDisplaySource(android.view.InsetsState state, int logicalWidth, int logicalHeight) {
    }

    default void setDisplayId(int displayId) {
    }

    default int getDisplayId() {
        return -1;
    }
}
