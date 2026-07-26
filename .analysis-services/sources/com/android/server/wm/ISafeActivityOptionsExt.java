package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ISafeActivityOptionsExt {
    default boolean isPuttDisplay(int displayId) {
        return false;
    }

    default android.app.ActivityOptions getActivityOptions() {
        return null;
    }
}
