package com.android.server.lights;

/* JADX INFO: loaded from: classes2.dex */
public interface ILightsServiceExt {
    default void init(android.content.Context context, android.os.Looper looper) {
    }

    default void onBootComplete(int phase) {
    }

    default boolean isPocketLightModeSupported() {
        return false;
    }

    default void setFakePocketLightMode(boolean enable) {
    }

    default void onSetLight(int id, int brightness, int brightnessMode) {
    }

    default void dumpStackTrace(java.lang.String msg) {
    }

    default boolean isOplusBreathingLight(int type) {
        return false;
    }

    default void dumpOplus(java.io.PrintWriter pw) {
    }

    default void setOplusLightUnchecked(int type, int id, int color, int mode, int onMS, int offMS, int brightnessMode) {
    }

    default void setBootAnimationLightInternal(boolean on, int color) {
    }
}
