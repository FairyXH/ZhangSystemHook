package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IEnsureActivitiesVisibleHelperExt {
    default void makeVisibleAndRestartIfNeeded(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityRecord starting) {
    }

    default boolean isScreenOffPlay(com.android.server.wm.Task task) {
        return false;
    }

    default void updateVisibleTime(com.android.server.wm.ActivityRecord r) {
    }
}
