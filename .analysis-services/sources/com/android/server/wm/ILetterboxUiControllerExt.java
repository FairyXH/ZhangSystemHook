package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ILetterboxUiControllerExt {
    default void interceptLayoutLetterbox(android.graphics.Rect spaceToFill, android.graphics.Rect frame, android.graphics.Point tmpPoint, com.android.server.wm.WindowState w, com.android.server.wm.Letterbox letterbox) {
        letterbox.layout(spaceToFill, frame, tmpPoint);
    }

    default boolean shouldUseBlackLetterboxBackground(com.android.server.wm.ActivityRecord activityRecord) {
        return false;
    }
}
