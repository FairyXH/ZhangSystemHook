package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IDisplayDeviceExt {
    default void cacheSurfaceForDisplay(com.android.server.display.DisplayDevice device, android.view.Surface surface) {
    }

    default void setMirageSetSurfaceNull(boolean surfaceNull) {
    }

    default boolean getMirageSetSurfaceNull() {
        return false;
    }

    default boolean shouldSetDisplayDeviceSurface(com.android.server.display.DisplayDevice device) {
        return true;
    }

    default void updatePowerModeChanged(boolean changed) {
    }

    default boolean isPowerModeChanged() {
        return false;
    }

    default void setPhysicalDisplayId(long physicalDisplayId, boolean isFirstDisplay) {
    }

    default void requestDisplayStateChanged(int state, float brightnessState, float sdrBrightnessState) {
    }

    default void setDisplayState(int oldState, int state) {
    }

    default void setDisplayBrightness(float newBrightnessState, float backlight, float nits, float brightnessState, float sdrBrightnessState) {
    }

    default boolean isDualRampOpt(int edrType, int dcThreshold) {
        return false;
    }

    default void setLayerStack(int layerStack) {
    }

    default int getLayerStack() {
        return -1;
    }

    default void setProjectionLocked(android.graphics.Rect displayRect, com.android.server.display.DisplayDevice device) {
    }

    default boolean isMirageDisplayChangeToMirror(int boundDisplayId) {
        return false;
    }
}
