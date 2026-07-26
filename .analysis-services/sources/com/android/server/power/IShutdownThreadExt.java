package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public interface IShutdownThreadExt {
    default void checkShutdownTimeout(android.content.Context context, boolean reboot, java.lang.String reason, int shutdownVibrateInMs) {
    }

    default void delayForPlayAnimation(android.content.Context context) {
    }

    default void shutdownStorageManagerService(android.content.Context context) {
    }

    public interface IStaticExt {
        default boolean interceptShutdown(android.content.Context context, java.lang.String reason) {
            return false;
        }

        default boolean interceptReboot(android.content.Context context, java.lang.String reason) {
            return false;
        }

        default void beginShutdownSequence(android.content.Context context) {
        }

        default void doShutdownDetect(java.lang.String cout) {
        }

        default boolean shouldDoLowLevelShutdown(android.content.Context context) {
            return false;
        }

        default boolean hasFeatureOriginalShutdownAnimation() {
            return false;
        }

        default boolean rebootOrShutdownSubsystem() {
            return true;
        }
    }
}
