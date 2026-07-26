package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusEyeProtectManager extends android.common.IOplusCommonFeature {
    public static final com.android.server.display.IOplusEyeProtectManager DEFAULT = new com.android.server.display.IOplusEyeProtectManager() { // from class: com.android.server.display.IOplusEyeProtectManager.1
    };
    public static final int LEVEL_COLOR_MATRIX_COLOR = 400;

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusEyeProtectManager;
    }

    default com.android.server.display.IOplusEyeProtectManager getDefault() {
        return DEFAULT;
    }

    default boolean needResetAnimationScaleSetting(android.content.Context context, int userId) {
        return false;
    }

    default void setColorMatrix(int level, float[] value, com.android.server.display.color.DisplayTransformManager dtm) {
        dtm.setColorMatrix(level, value);
    }

    default void setUp(android.content.Context context, int currentUser) {
    }

    default void tearDown() {
    }

    default void setGameEyeProtState(java.lang.String pkgName, int status) {
    }

    default boolean isEyeProtBlackListGame(java.lang.String pkgName) {
        return true;
    }

    default void setFatigueStatus(int status) {
    }
}
