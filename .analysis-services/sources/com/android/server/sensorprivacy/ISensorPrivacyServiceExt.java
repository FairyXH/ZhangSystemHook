package com.android.server.sensorprivacy;

/* JADX INFO: loaded from: classes3.dex */
public interface ISensorPrivacyServiceExt {
    public static final int LOCATION = 3;

    default boolean isStealthSecurityMode() {
        return false;
    }

    default boolean notifySystemUI(android.content.Context context, int sensor) {
        return false;
    }

    default boolean canSkipSetCheckForStealthMode(int callingPid) {
        return false;
    }

    default boolean disappearNotification(android.content.Context context, boolean enable, int notificationId) {
        return false;
    }

    default void enterEnmergencyCall() {
    }

    default void exitEnmergencyCall() {
    }
}
