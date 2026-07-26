package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public interface IOplusShutdownFeature extends android.common.IOplusCommonFeature {
    public static final com.android.server.power.IOplusShutdownFeature DEFAULT = new com.android.server.power.IOplusShutdownFeature() { // from class: com.android.server.power.IOplusShutdownFeature.1
    };
    public static final java.lang.String NAME = "IOplusShutdownFeature";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusShutdownFeature;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void showShutdownBacktrace(boolean spew) {
        android.util.Log.d(NAME, "default showShutdownBacktrace");
    }

    default void setSpecialShutdownProperty(java.lang.String reason) {
        android.util.Log.d(NAME, "default setSpecialShutdownProperty");
    }

    default void resetBrightnessAdj(android.content.Context context) {
        android.util.Log.d(NAME, "default resetBrightnessAdj");
    }

    default void setBeginAnimationTime(long beginAnimTime, boolean isCmcc) {
        android.util.Log.d(NAME, "default setBeginAnimationTime");
    }

    default void shutdownService(android.content.Context context) {
        android.util.Log.d(NAME, "default shutdownService");
    }

    default void checkShutdownTimeout(android.content.Context context, boolean reboot, java.lang.String reason, int shutdonwVibrateInMs, android.media.AudioAttributes vibrateAttribute) {
        android.util.Log.d(NAME, "default checkShutdownTimeout");
    }

    default void delayForPlayAnimation() {
        android.util.Log.d(NAME, "default delayForPlayAnimation");
    }

    default boolean shouldDoLowLevelShutdown() {
        android.util.Log.d(NAME, "default shouldDoLowLevelShutdown");
        return true;
    }

    default void setMaxDelayTimeForCustomizeRebootanim(int timeout) {
        android.util.Log.d(NAME, "default setMaxDelayTimeForCustomizeRebootanim");
    }

    default int getMaxDelayTimeForCustomizeRebootanim() {
        android.util.Log.d(NAME, "default getMaxDelayTimeForCustomizeRebootanim");
        return 0;
    }

    default void shutdownStorageManagerService() {
        android.util.Log.d(NAME, "default shutdownStorageManagerService");
    }
}
