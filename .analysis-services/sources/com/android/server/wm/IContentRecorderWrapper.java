package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IContentRecorderWrapper {
    default android.graphics.Rect getRectBounds() {
        return null;
    }

    default com.android.server.wm.WindowContainer getRecordedWindowContainer() {
        return null;
    }

    default com.android.server.wm.DisplayContent getDisplayContent() {
        return null;
    }

    default android.graphics.Point fetchSurfaceSizeIfPresent() {
        return null;
    }
}
