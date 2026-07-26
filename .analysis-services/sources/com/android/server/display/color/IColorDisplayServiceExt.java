package com.android.server.display.color;

/* JADX INFO: loaded from: classes2.dex */
public interface IColorDisplayServiceExt {
    default void init(android.content.Context context) {
    }

    default void onBootComplete() {
    }

    default void onSetUp(int currentUser) {
    }

    default void onTearDown() {
    }

    default void setColorMatrix(int level, float[] value, com.android.server.display.color.DisplayTransformManager dtm) {
    }

    default int getColorMode() {
        return 0;
    }

    default int getWCGModeForAPP(java.lang.String packageName) {
        return 0;
    }

    default boolean isSupportWCGManager() {
        return false;
    }

    default java.util.List<java.lang.String> getForceWcgPackage() {
        return null;
    }

    default void applyAccessiblityInversionState(boolean isEnable) {
    }
}
