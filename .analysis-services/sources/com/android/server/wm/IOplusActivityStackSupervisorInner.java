package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IOplusActivityStackSupervisorInner {
    default <T extends com.android.server.wm.Task> T getStack(int windowingMode, int activityType) {
        return null;
    }
}
