package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public interface IWakeLockExt {
    default long getActiveSince() {
        return 0L;
    }

    default void setActiveSince(long activeSince) {
    }

    default long getTotalTime() {
        return 0L;
    }

    default void setTotalTime(long totalTime) {
    }

    default boolean getDisabledByHans() {
        return false;
    }

    default void setDisableByHans(boolean value) {
    }
}
