package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public interface IOplusFeatureAOD extends android.common.IOplusCommonFeature {
    public static final com.android.server.power.IOplusFeatureAOD DEFAULT = new com.android.server.power.IOplusFeatureAOD() { // from class: com.android.server.power.IOplusFeatureAOD.1
    };
    public static final java.lang.String NAME = "IOplusFeatureAOD";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusFeatureAOD;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void setDozeOverride(int screenState, int screenBrightness) {
    }

    default void setDozeOverrideFromDreamManager(int screenState, int screenBrightness) {
    }

    default int setDozeOverrideFromDreamManagerInternal(int screenState, int screenBrightness) {
        return screenState;
    }

    default void systemReady() {
    }

    default boolean isShouldGoAod() {
        return false;
    }

    default void setAodSettingStatus() {
    }

    default void handleAodChanged() {
    }

    default void onDisplayStateChange(android.service.dreams.DreamManagerInternal dreamManager, int state) {
    }

    default void notifySfUnBlockScreenOn() {
    }

    default void clearDozeStateMap() {
    }
}
