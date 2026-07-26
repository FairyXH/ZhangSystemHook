package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IDisplayPowerStateExt {
    default int getBootupBrightness() {
        return 400;
    }

    default boolean getAodStatus() {
        return false;
    }

    default void setAodStatus(boolean isAod) {
    }

    default android.util.Pair<java.lang.Float, java.lang.Float> screenUpdateExt(int screenState, float oriBrightnessState, float sdrBrightnessState, float screenBrightness, float colorFadeLevel, int displayId) {
        return android.util.Pair.create(java.lang.Float.valueOf(oriBrightnessState), java.lang.Float.valueOf(sdrBrightnessState));
    }

    default void setDisplayThreadSched(int tid, int prio) {
    }

    default void setUxThread() {
    }
}
