package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IWindowTokenExt {
    public static final float SCALE_DEFAULT = 1.0f;

    default void makeSurface(android.view.SurfaceControl.Builder builder, int windowType) {
    }

    default boolean judgeWindowModeZoom() {
        return false;
    }

    default boolean autoResolutionEnable(com.android.server.wm.WindowToken token) {
        return false;
    }

    default void setScale(float scale) {
    }

    default float getScale() {
        return 1.0f;
    }

    default float getCurrScale() {
        return 1.0f;
    }

    default void setCurrScale(float scale) {
    }

    default void updateSurfaceIfNeed() {
    }

    default void onConfigurationChanged(android.content.res.Configuration newParentConfig) {
    }

    default void resolveScreenConfigInSecondary(com.android.server.wm.WindowToken windowToken, android.content.res.Configuration outConfig, android.view.DisplayInfo displayInfo) {
    }

    default void recoveryFixedRotationConfig(com.android.server.wm.WindowToken windowToken, android.content.res.Configuration outConfig) {
    }

    default boolean canAssigFingerPrintLayer(com.android.server.wm.WindowToken windowToken, int windowType) {
        return false;
    }
}
