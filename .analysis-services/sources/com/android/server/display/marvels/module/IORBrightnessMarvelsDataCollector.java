package com.android.server.display.marvels.module;

/* JADX INFO: loaded from: classes2.dex */
public interface IORBrightnessMarvelsDataCollector extends android.common.IOplusCommonFeature {
    public static final float BAD_VALUE = -1.0f;
    public static final com.android.server.display.marvels.module.IORBrightnessMarvelsDataCollector DEFAULT = new com.android.server.display.marvels.module.IORBrightnessMarvelsDataCollector() { // from class: com.android.server.display.marvels.module.IORBrightnessMarvelsDataCollector.1
    };
    public static final java.lang.String NAME = "IORBrightnessMarvelsDataCollector";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IORBrightnessMarvelsDataCollector;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void setMarvelsHandler(java.lang.Object caller, android.os.Handler handler) {
    }

    default void setLux(float lux) {
    }

    default void setStableLux(float lux) {
    }

    default void setDrag(boolean drag) {
    }

    default void setTargetBrightness(int brightness) {
    }

    default void setCurrentBrightness(int brightness) {
    }

    default void setLowPower(int lowPower) {
    }

    default void setEyesProtect(int eyesProtect) {
    }

    default void setBrightnessMode(int mode) {
    }

    default void setMotion(java.lang.String state) {
    }

    default void setTimerTask(java.lang.String action) {
    }

    default void release(java.lang.Object caller) {
    }

    default void keepTheCoreProgramRelease(java.lang.Object caller) {
    }

    default void setDarkestBaseBrightness(float brightness) {
    }

    default void setReason(int reason) {
    }
}
