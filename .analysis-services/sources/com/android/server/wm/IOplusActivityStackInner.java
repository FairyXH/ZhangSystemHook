package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IOplusActivityStackInner {
    default boolean canEnterPipOnTaskSwitch(com.android.server.wm.ActivityRecord pipCandidate, com.android.server.wm.Task toFrontTask, com.android.server.wm.ActivityRecord toFrontActivity, android.app.ActivityOptions opts) {
        return false;
    }
}
