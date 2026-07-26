package com.android.server.statusbar;

/* JADX INFO: loaded from: classes3.dex */
public interface StatusBarManagerInternal {
    void abortTransient(int i, int i2);

    void addQsTileToFrontOrEnd(android.content.ComponentName componentName, boolean z);

    void appTransitionCancelled(int i);

    void appTransitionFinished(int i);

    void appTransitionPending(int i);

    void appTransitionStarting(int i, long j, long j2);

    void cancelPreloadRecentApps();

    void collapsePanels();

    void confirmImmersivePrompt();

    void dismissKeyboardShortcutsMenu();

    void hideRecentApps(boolean z, boolean z2);

    void hideToast(java.lang.String str, android.os.IBinder iBinder);

    void immersiveModeChanged(int i, boolean z);

    void moveFocusedTaskToDesktop(int i);

    void moveFocusedTaskToFullscreen(int i);

    void moveFocusedTaskToStageSplit(int i, boolean z);

    void onCameraLaunchGestureDetected(int i);

    void onDisplayReady(int i);

    void onEmergencyActionLaunchGestureDetected();

    void onProposedRotationChanged(int i, boolean z);

    void onRecentsAnimationStateChanged(boolean z);

    void onSystemBarAttributesChanged(int i, int i2, com.android.internal.view.AppearanceRegion[] appearanceRegionArr, boolean z, int i3, int i4, java.lang.String str, com.android.internal.statusbar.LetterboxDetails[] letterboxDetailsArr);

    void preloadRecentApps();

    void removeQsTile(android.content.ComponentName componentName);

    boolean requestMagnificationConnection(boolean z);

    void setCurrentUser(int i);

    void setDisableFlags(int i, int i2, java.lang.String str);

    void setIcon(java.lang.String str, java.lang.String str2, int i, int i2, java.lang.String str3);

    void setIconVisibility(java.lang.String str, boolean z);

    void setImeWindowStatus(int i, android.os.IBinder iBinder, int i2, int i3, boolean z);

    void setNavigationBarLumaSamplingEnabled(int i, boolean z);

    void setNotificationDelegate(com.android.server.notification.NotificationDelegate notificationDelegate);

    void setSplitscreenFocus(boolean z);

    void setTopAppHidesStatusBar(boolean z);

    void setUdfpsRefreshRateCallback(android.hardware.fingerprint.IUdfpsRefreshRateRequestCallback iUdfpsRefreshRateRequestCallback);

    void setWindowState(int i, int i2, int i3);

    void showAssistDisclosure();

    void showChargingAnimation(int i);

    void showMediaOutputSwitcher(java.lang.String str, android.os.UserHandle userHandle);

    void showPictureInPictureMenu();

    void showRearDisplayDialog(int i);

    void showRecentApps(boolean z);

    void showScreenPinningRequest(int i);

    boolean showShutdownUi(boolean z, java.lang.String str);

    void showToast(int i, java.lang.String str, android.os.IBinder iBinder, java.lang.CharSequence charSequence, android.os.IBinder iBinder2, int i2, android.app.ITransientNotificationCallback iTransientNotificationCallback, int i3);

    void showTransient(int i, int i2, boolean z);

    void startAssist(android.os.Bundle bundle);

    void toggleKeyboardShortcutsMenu(int i);

    void toggleRecentApps();

    void toggleSplitScreen();

    void toggleTaskbar();
}
