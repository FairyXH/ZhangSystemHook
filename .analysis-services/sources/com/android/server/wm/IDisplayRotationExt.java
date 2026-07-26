package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IDisplayRotationExt {
    default void continueRotation() {
    }

    default boolean stopRotationInGame(com.android.server.wm.WindowContainer windowContainer) {
        return false;
    }

    default boolean hasMaskAnimation() {
        return false;
    }

    default void onProposedRotationChanged(int rotation, int mUserRotationMode, com.android.server.wm.DisplayContent mDisplayContent) {
    }

    default int blockAllowAllRotationsInTable(int allowRotation, com.android.server.wm.DisplayContent displayContent) {
        return allowRotation;
    }

    default boolean enableRequestOrientationWhenDeviceFolding(com.android.server.wm.DisplayContent displayContent) {
        return false;
    }

    default void setSensorRotationChanged(com.android.server.wm.DisplayContent displayContent, boolean changed) {
    }

    default void updateOrientationSensorRunningState(boolean running) {
    }

    default void updateRotation(int rotation, com.android.server.wm.DisplayContent displayContent) {
    }

    default int shouldSuggestEnterBracketMode() {
        return -1;
    }

    default boolean shouldFreezeScreenOrientation() {
        return false;
    }

    default int getFixedRotationForOrientation(int orientation, com.android.server.wm.DisplayContent displayContent, int lastRotation) {
        return -1;
    }

    default int adjustRotationForReverseLandscape(com.android.server.wm.DisplayContent displayContent, int seascapeRotation) {
        return -1;
    }

    default boolean isValidRotationChoice(int preferredRotation, int upsideDownRotation) {
        return preferredRotation >= 0 && preferredRotation != upsideDownRotation;
    }

    default int modifyFreezeRotationWhenDeviceFolding(int rotation) {
        return rotation;
    }

    default boolean isForceAllowAllOrientation(com.android.server.wm.DisplayContent displayContent) {
        return false;
    }

    default boolean isSecondDisplay(com.android.server.wm.DisplayContent dc) {
        return false;
    }

    default boolean drawComplete(com.android.server.wm.DisplayContent dc, boolean keyguardDrawComplete, boolean windowManagerDrawComplete) {
        return false;
    }

    default void registerFoldStateListener(android.content.Context context, android.os.Handler handler, java.lang.Object lock) {
    }

    default int hookUpdateSensorRotation(int sensorRotation, com.android.server.wm.DisplayContent dc) {
        return sensorRotation;
    }

    default int hookActivityOrientation(int sensorRotation, int userRotationMode, int orientation, int rotation, com.android.server.wm.DisplayContent dc) {
        return rotation;
    }

    default boolean shouldEnableOrientationListener(com.android.server.wm.DisplayContent dc, boolean enabledWhileDreaming) {
        return false;
    }

    default boolean isDisplayEnable(com.android.server.wm.DisplayContent dc, boolean enabledWhileDreaming) {
        return false;
    }

    default boolean stopUpdateRotationUnchecked(com.android.server.wm.DisplayContent dc, boolean forceUpdate) {
        return false;
    }

    default boolean stopUpdateRotation(com.android.server.wm.DisplayContent dc, boolean forceUpdate) {
        return false;
    }

    default boolean omitRotationChange(com.android.server.wm.DisplayContent dc, int rotation) {
        return false;
    }

    default int forceLauncherRotate(int preferredRotation, com.android.server.wm.WindowContainer lastOrientationSrc) {
        return preferredRotation;
    }

    default int hookLockedRotation(int userRotation, com.android.server.wm.DisplayContent dc) {
        return userRotation;
    }

    default void forceUpdateRotationForCanvas(boolean shouldUpdateRotation) {
    }

    default boolean forceSeamlesslyRotated(com.android.server.wm.WindowState w, java.lang.String reason) {
        return false;
    }

    default boolean stopRotationInPutt(com.android.server.wm.WindowContainer windowContainer, int displayId) {
        return false;
    }

    default int getSuggestRotationForBracketMode() {
        return -1;
    }

    default boolean shouldKeepSensorRotationInFixRotation(com.android.server.wm.DisplayContent dc, int orientation, int sensorRotation, int lastSensorRotation) {
        return false;
    }

    default int resolvePreferredRotationInSecondary(int sensorRotation, int lastRotation, int orientation) {
        return -1;
    }

    default void setForceUpdateRotation(boolean force) {
    }

    default boolean checkForceUpdate(com.android.server.wm.DisplayContent dc) {
        return false;
    }

    default boolean skipSendProposedRotationChangeToStatusBar(int curAppOrientation, com.android.server.wm.DisplayContent dc) {
        return false;
    }

    default int getMirageFixedRotation(int displayId) {
        return -1;
    }

    default int getMirageInitialRotation(int displayId) {
        return -1;
    }

    default int getMirageDisplaySensorRotation(int displayId) {
        return -1;
    }

    default void pauseRotation(int deferredRotationPauseCount) {
    }

    default void resumeRotation(int deferredRotationPauseCount) {
    }

    default void dumpRotationPauseRecord(java.lang.String prefix, java.io.PrintWriter pw) {
    }

    default boolean hasFlexibleAnimation() {
        return false;
    }

    default boolean shouldDeferRotation(com.android.server.wm.DisplayContent dc, int lastOrientation) {
        return false;
    }

    default boolean abortPocketStudioCloseAnimation(com.android.server.wm.Transition transition) {
        return false;
    }
}
