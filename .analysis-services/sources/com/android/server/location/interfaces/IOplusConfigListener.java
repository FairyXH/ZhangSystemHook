package com.android.server.location.interfaces;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusConfigListener {
    default void onDebugLevelChanged(int level) {
    }

    default void onRusChanged() {
    }

    default void onDeepSleepDisNetTypeChanged(int type) {
    }

    default void onPhysicalDisNetChanged(boolean disNet) {
    }

    default void onLogicalDisNetChanged(boolean disNet) {
    }
}
