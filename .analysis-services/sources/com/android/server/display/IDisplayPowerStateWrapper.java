package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IDisplayPowerStateWrapper {
    default boolean getDebug() {
        return false;
    }

    default boolean getColorFadePrepared() {
        return false;
    }

    default void setLoggingEnabled(boolean debug) {
    }

    default void setScreenReady(boolean ready) {
    }

    default void scheduleScreenUpdate() {
    }
}
