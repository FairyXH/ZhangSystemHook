package com.android.server.lights;

/* JADX INFO: loaded from: classes2.dex */
public interface ILightsServiceWrapper {
    default boolean getDebug() {
        return false;
    }

    default java.lang.Object getLightsByType() {
        return null;
    }

    default void setLightUnchecked(java.lang.Object object, int color, int mode, int onMS, int offMS, int brightnessMode) {
    }

    default void setLightLocked(java.lang.Object object, int color, int mode, int onMS, int offMS, int brightnessMode) {
    }
}
