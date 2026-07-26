package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ITaskFragmentSocExt {
    default void hookTriggerActivityResume(com.android.server.wm.ActivityRecord next) {
    }

    default void initPerf() {
    }

    default void hookVendorHintAnimBoost(com.android.server.wm.ActivityRecord prev, com.android.server.wm.ActivityRecord next) {
    }

    default void hookTriggerActivityPause(com.android.server.wm.ActivityRecord prev) {
    }
}
