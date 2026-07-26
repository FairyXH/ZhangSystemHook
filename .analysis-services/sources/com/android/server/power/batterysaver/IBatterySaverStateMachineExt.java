package com.android.server.power.batterysaver;

/* JADX INFO: loaded from: classes3.dex */
public interface IBatterySaverStateMachineExt {
    default void init(android.content.Context context) {
    }

    default void onBootCompleted(boolean settingBatterySaverEnabledSticky) {
    }

    default boolean isOplusFeatureDisalbed() {
        return true;
    }

    default void onSetBatterySaverEnabledManually(boolean enabled) {
    }
}
