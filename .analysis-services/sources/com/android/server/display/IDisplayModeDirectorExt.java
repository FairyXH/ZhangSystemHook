package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IDisplayModeDirectorExt {
    default boolean isAdfrEnabled() {
        return false;
    }

    default int getVrrPolicy(float max) {
        return 0;
    }

    default java.lang.String getVrrPolicyStr(int vrrPolicy) {
        return "POLICY_DEFAULT";
    }

    default void registerResolutionChangeListener(java.lang.Runnable runnable) {
    }

    default int getWidth(int width) {
        return width;
    }

    default int getHeight(int height) {
        return height;
    }

    default boolean isHighLoadRefreshRateEnabled() {
        return false;
    }
}
