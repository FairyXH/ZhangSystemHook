package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusFeatureHDREnhanceBrightness extends android.common.IOplusCommonFeature {
    public static final com.android.server.display.IOplusFeatureHDREnhanceBrightness DEFAULT = new com.android.server.display.IOplusFeatureHDREnhanceBrightness() { // from class: com.android.server.display.IOplusFeatureHDREnhanceBrightness.1
    };
    public static final java.lang.String NAME = "IOplusFeatureHDREnhanceBrightness";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusFeatureHDREnhanceBrightness;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default boolean enhanceBrightness(int brightness, int rate, int displayId) {
        return false;
    }

    default int getBrightness(int displayId) {
        return 0;
    }

    default int getRate(int displayId) {
        return 0;
    }

    default void registerByNewImpl() {
    }

    default void unregisterByNewImpl() {
    }
}
