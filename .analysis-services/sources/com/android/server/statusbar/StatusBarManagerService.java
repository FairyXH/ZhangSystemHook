package com.android.server.statusbar;

/* JADX INFO: loaded from: classes3.dex */
public class StatusBarManagerService extends com.android.internal.statusbar.IStatusBarService.Stub implements android.hardware.display.DisplayManager.DisplayListener {
    private static final long LOCK_DOWN_COLLAPSE_STATUS_BAR = 173031413;
    static final long REQUEST_LISTENING_MUST_MATCH_PACKAGE = 172251878;
    static final long REQUEST_LISTENING_OTHER_USER_NOOP = 242194868;
    private static final long REQUEST_TIME_OUT = java.util.concurrent.TimeUnit.MINUTES.toNanos(5);
    private static final boolean SPEW = false;
    private static final java.lang.String TAG = "StatusBarManagerService";
    private final android.app.ActivityManagerInternal mActivityManagerInternal;
    private final com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManager;
    private volatile com.android.internal.statusbar.IStatusBar mBar;
    private android.hardware.biometrics.IBiometricContextListener mBiometricContextListener;
    private final android.content.Context mContext;
    private int mCurrentUserId;
    private com.android.server.policy.GlobalActionsProvider.GlobalActionsListener mGlobalActionListener;
    private com.android.server.notification.NotificationDelegate mNotificationDelegate;
    private android.content.om.IOverlayManager mOverlayManager;
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final com.android.server.statusbar.SessionMonitor mSessionMonitor;
    private final com.android.server.statusbar.TileRequestTracker mTileRequestTracker;
    private boolean mTracingEnabled;
    private android.hardware.fingerprint.IUdfpsRefreshRateRequestCallback mUdfpsRefreshRateRequestCallback;
    private final android.os.Handler mHandler = new android.os.Handler();
    private final android.util.ArrayMap<java.lang.String, com.android.internal.statusbar.StatusBarIcon> mIcons = new android.util.ArrayMap<>();
    private final java.util.ArrayList<com.android.server.statusbar.StatusBarManagerService.DisableRecord> mDisableRecords = new java.util.ArrayList<>();
    private final android.os.IBinder mSysUiVisToken = new android.os.Binder();
    private final java.lang.Object mLock = new java.lang.Object();
    private final com.android.server.statusbar.StatusBarManagerService.DeathRecipient mDeathRecipient = new com.android.server.statusbar.StatusBarManagerService.DeathRecipient();
    private int mLastSystemKey = -1;
    private final android.util.SparseArray<com.android.server.statusbar.StatusBarManagerService.UiState> mDisplayUiState = new android.util.SparseArray<>();
    private final android.util.ArrayMap<java.lang.String, java.lang.Long> mCurrentRequestAddTilePackages = new android.util.ArrayMap<>();
    private final com.android.server.statusbar.StatusBarManagerInternal mInternalService = new com.android.server.statusbar.StatusBarManagerInternal() { // from class: com.android.server.statusbar.StatusBarManagerService.1
        private boolean mNotificationLightOn;

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void setNotificationDelegate(com.android.server.notification.NotificationDelegate delegate) {
            com.android.server.statusbar.StatusBarManagerService.this.mNotificationDelegate = delegate;
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void showScreenPinningRequest(int taskId) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.showScreenPinningRequest(taskId);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void showAssistDisclosure() {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.showAssistDisclosure();
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void startAssist(android.os.Bundle args) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.startAssist(args);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void onCameraLaunchGestureDetected(int source) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.onCameraLaunchGestureDetected(source);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void onEmergencyActionLaunchGestureDetected() {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.onEmergencyActionLaunchGestureDetected();
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void setDisableFlags(int displayId, int flags, java.lang.String cause) {
            com.android.server.statusbar.StatusBarManagerService.this.setDisableFlags(displayId, flags, cause);
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void toggleSplitScreen() {
            com.android.server.statusbar.StatusBarManagerService.this.enforceStatusBarService();
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.toggleSplitScreen();
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void appTransitionFinished(int displayId) {
            com.android.server.statusbar.StatusBarManagerService.this.enforceStatusBarService();
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.appTransitionFinished(displayId);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void toggleTaskbar() {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.toggleTaskbar();
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void toggleRecentApps() {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.toggleRecentApps();
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void setCurrentUser(int newUserId) {
            com.android.server.statusbar.StatusBarManagerService.this.mCurrentUserId = newUserId;
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void preloadRecentApps() {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.preloadRecentApps();
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void cancelPreloadRecentApps() {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.cancelPreloadRecentApps();
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void showRecentApps(boolean triggeredFromAltTab) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.showRecentApps(triggeredFromAltTab);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void hideRecentApps(boolean triggeredFromAltTab, boolean triggeredFromHomeKey) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.hideRecentApps(triggeredFromAltTab, triggeredFromHomeKey);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void collapsePanels() {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.animateCollapsePanels();
                } catch (android.os.RemoteException e) {
                }
                com.android.server.statusbar.StatusBarManagerService.this.mSBMS.collapsePanels();
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void dismissKeyboardShortcutsMenu() {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.dismissKeyboardShortcutsMenu();
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void toggleKeyboardShortcutsMenu(int deviceId) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.toggleKeyboardShortcutsMenu(deviceId);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void setImeWindowStatus(int displayId, android.os.IBinder token, int vis, int backDisposition, boolean showImeSwitcher) {
            com.android.server.statusbar.StatusBarManagerService.this.setImeWindowStatus(displayId, token, vis, backDisposition, showImeSwitcher);
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void setIcon(java.lang.String slot, java.lang.String iconPackage, int iconId, int iconLevel, java.lang.String contentDescription) {
            com.android.server.statusbar.StatusBarManagerService.this.setIcon(slot, iconPackage, iconId, iconLevel, contentDescription);
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void setIconVisibility(java.lang.String slot, boolean visibility) {
            com.android.server.statusbar.StatusBarManagerService.this.setIconVisibility(slot, visibility);
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void showChargingAnimation(int batteryLevel) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.showWirelessChargingAnimation(batteryLevel);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void showPictureInPictureMenu() {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    com.android.server.statusbar.StatusBarManagerService.this.mBar.showPictureInPictureMenu();
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void setWindowState(int displayId, int window, int state) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.setWindowState(displayId, window, state);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void appTransitionPending(int displayId) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.appTransitionPending(displayId);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void appTransitionCancelled(int displayId) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.appTransitionCancelled(displayId);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void appTransitionStarting(int displayId, long statusBarAnimationsStartTime, long statusBarAnimationsDuration) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.appTransitionStarting(displayId, statusBarAnimationsStartTime, statusBarAnimationsDuration);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void setTopAppHidesStatusBar(boolean hidesStatusBar) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.setTopAppHidesStatusBar(hidesStatusBar);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public boolean showShutdownUi(boolean isReboot, java.lang.String reason) {
            com.android.internal.statusbar.IStatusBar bar;
            if (com.android.server.statusbar.StatusBarManagerService.this.mContext.getResources().getBoolean(android.R.bool.config_reverseDefaultRotation) && (bar = com.android.server.statusbar.StatusBarManagerService.this.mBar) != null) {
                try {
                    bar.showShutdownUi(isReboot, reason);
                    return true;
                } catch (android.os.RemoteException e) {
                }
            }
            return false;
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void confirmImmersivePrompt() {
            if (com.android.server.statusbar.StatusBarManagerService.this.mBar == null) {
                return;
            }
            try {
                com.android.server.statusbar.StatusBarManagerService.this.mBar.confirmImmersivePrompt();
            } catch (android.os.RemoteException e) {
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void immersiveModeChanged(int rootDisplayAreaId, boolean isImmersiveMode) {
            if (com.android.server.statusbar.StatusBarManagerService.this.mBar != null && !android.view.ViewRootImpl.CLIENT_TRANSIENT) {
                try {
                    com.android.server.statusbar.StatusBarManagerService.this.mBar.immersiveModeChanged(rootDisplayAreaId, isImmersiveMode);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void onProposedRotationChanged(int rotation, boolean isValid) {
            if (com.android.server.statusbar.StatusBarManagerService.this.mBar != null) {
                try {
                    com.android.server.statusbar.StatusBarManagerService.this.mBar.onProposedRotationChanged(rotation, isValid);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void onDisplayReady(int displayId) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.onDisplayReady(displayId);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void onRecentsAnimationStateChanged(boolean running) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.onRecentsAnimationStateChanged(running);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void onSystemBarAttributesChanged(int displayId, int appearance, com.android.internal.view.AppearanceRegion[] appearanceRegions, boolean navbarColorManagedByIme, int behavior, int requestedVisibleTypes, java.lang.String packageName, com.android.internal.statusbar.LetterboxDetails[] letterboxDetails) {
            com.android.server.statusbar.StatusBarManagerService.this.getUiState(displayId).setBarAttributes(appearance, appearanceRegions, navbarColorManagedByIme, behavior, requestedVisibleTypes, packageName, letterboxDetails);
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.onSystemBarAttributesChanged(displayId, appearance, appearanceRegions, navbarColorManagedByIme, behavior, requestedVisibleTypes, packageName, letterboxDetails);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void showTransient(int displayId, int types, boolean isGestureOnSystemBar) {
            com.android.server.statusbar.StatusBarManagerService.this.getUiState(displayId).showTransient(types);
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.showTransient(displayId, types, isGestureOnSystemBar);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void abortTransient(int displayId, int types) {
            com.android.server.statusbar.StatusBarManagerService.this.getUiState(displayId).clearTransient(types);
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.abortTransient(displayId, types);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void showToast(int uid, java.lang.String packageName, android.os.IBinder token, java.lang.CharSequence text, android.os.IBinder windowToken, int duration, android.app.ITransientNotificationCallback callback, int displayId) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.showToast(uid, packageName, token, text, windowToken, duration, callback, displayId);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void hideToast(java.lang.String packageName, android.os.IBinder token) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.hideToast(packageName, token);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public boolean requestMagnificationConnection(boolean request) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.requestMagnificationConnection(request);
                    return true;
                } catch (android.os.RemoteException e) {
                    return false;
                }
            }
            return false;
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void setNavigationBarLumaSamplingEnabled(int displayId, boolean enable) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.setNavigationBarLumaSamplingEnabled(displayId, enable);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void setUdfpsRefreshRateCallback(android.hardware.fingerprint.IUdfpsRefreshRateRequestCallback callback) {
            synchronized (com.android.server.statusbar.StatusBarManagerService.this.mLock) {
                com.android.server.statusbar.StatusBarManagerService.this.mUdfpsRefreshRateRequestCallback = callback;
            }
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.setUdfpsRefreshRateCallback(callback);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void showRearDisplayDialog(int currentBaseState) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.showRearDisplayDialog(currentBaseState);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void moveFocusedTaskToFullscreen(int displayId) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.moveFocusedTaskToFullscreen(displayId);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void moveFocusedTaskToStageSplit(int displayId, boolean leftOrTop) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.moveFocusedTaskToStageSplit(displayId, leftOrTop);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void setSplitscreenFocus(boolean leftOrTop) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.setSplitscreenFocus(leftOrTop);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void moveFocusedTaskToDesktop(int displayId) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.moveFocusedTaskToDesktop(displayId);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void showMediaOutputSwitcher(java.lang.String targetPackageName, android.os.UserHandle targetUserHandle) {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.showMediaOutputSwitcher(targetPackageName, targetUserHandle);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void addQsTileToFrontOrEnd(android.content.ComponentName tile, boolean end) {
            if (android.view.accessibility.Flags.a11yQsShortcut()) {
                com.android.server.statusbar.StatusBarManagerService.this.addQsTileToFrontOrEnd(tile, end);
            }
        }

        @Override // com.android.server.statusbar.StatusBarManagerInternal
        public void removeQsTile(android.content.ComponentName tile) {
            if (android.view.accessibility.Flags.a11yQsShortcut()) {
                com.android.server.statusbar.StatusBarManagerService.this.remTile(tile);
            }
        }
    };
    private final com.android.server.policy.GlobalActionsProvider mGlobalActionsProvider = new com.android.server.policy.GlobalActionsProvider() { // from class: com.android.server.statusbar.StatusBarManagerService.2
        @Override // com.android.server.policy.GlobalActionsProvider
        public boolean isGlobalActionsDisabled() {
            int disabled2 = ((com.android.server.statusbar.StatusBarManagerService.UiState) com.android.server.statusbar.StatusBarManagerService.this.mDisplayUiState.get(0)).getDisabled2();
            return (disabled2 & 8) != 0;
        }

        @Override // com.android.server.policy.GlobalActionsProvider
        public void setGlobalActionsListener(com.android.server.policy.GlobalActionsProvider.GlobalActionsListener listener) {
            com.android.server.statusbar.StatusBarManagerService.this.mGlobalActionListener = listener;
            com.android.server.statusbar.StatusBarManagerService.this.mGlobalActionListener.onGlobalActionsAvailableChanged(com.android.server.statusbar.StatusBarManagerService.this.mBar != null);
        }

        @Override // com.android.server.policy.GlobalActionsProvider
        public void showGlobalActions() {
            com.android.internal.statusbar.IStatusBar bar = com.android.server.statusbar.StatusBarManagerService.this.mBar;
            if (bar != null) {
                try {
                    bar.showGlobalActionsMenu();
                } catch (android.os.RemoteException e) {
                }
            }
        }
    };
    private com.android.server.statusbar.IStatusBarManagerServiceExt mSBMS = (com.android.server.statusbar.IStatusBarManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.statusbar.IStatusBarManagerServiceExt.class).base(this).create();

    private class DeathRecipient implements android.os.IBinder.DeathRecipient {
        private DeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.statusbar.StatusBarManagerService.this.mBar.asBinder().unlinkToDeath(this, 0);
            synchronized (com.android.server.statusbar.StatusBarManagerService.this.mLock) {
                com.android.server.statusbar.StatusBarManagerService.this.mBar = null;
            }
            com.android.server.statusbar.StatusBarManagerService.this.notifyBarAttachChanged();
            com.android.server.statusbar.StatusBarManagerService.this.mSBMS.maybeClearAllNotifications();
        }

        public void linkToDeath() {
            try {
                com.android.server.statusbar.StatusBarManagerService.this.mBar.asBinder().linkToDeath(com.android.server.statusbar.StatusBarManagerService.this.mDeathRecipient, 0);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.statusbar.StatusBarManagerService.TAG, "Unable to register Death Recipient for status bar", e);
            }
        }
    }

    private class DisableRecord implements android.os.IBinder.DeathRecipient {
        java.lang.String pkg;
        android.os.IBinder token;
        int userId;
        int what1;
        int what2;

        public DisableRecord(int userId, android.os.IBinder token) {
            this.userId = userId;
            this.token = token;
            try {
                token.linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            android.util.Slog.i(com.android.server.statusbar.StatusBarManagerService.TAG, "binder died for pkg=" + this.pkg);
            com.android.server.statusbar.StatusBarManagerService.this.disableForUser(0, this.token, this.pkg, this.userId);
            com.android.server.statusbar.StatusBarManagerService.this.disable2ForUser(0, this.token, this.pkg, this.userId);
            this.token.unlinkToDeath(this, 0);
        }

        public void setFlags(int what, int which, java.lang.String pkg) {
            switch (which) {
                case 1:
                    this.what1 = what;
                    break;
                case 2:
                    this.what2 = what;
                    break;
                default:
                    android.util.Slog.w(com.android.server.statusbar.StatusBarManagerService.TAG, "Can't set unsupported disable flag " + which + ": 0x" + java.lang.Integer.toHexString(what));
                    break;
            }
            this.pkg = pkg;
        }

        public int getFlags(int which) {
            switch (which) {
                case 1:
                    return this.what1;
                case 2:
                    return this.what2;
                default:
                    android.util.Slog.w(com.android.server.statusbar.StatusBarManagerService.TAG, "Can't get unsupported disable flag " + which);
                    return 0;
            }
        }

        public boolean isEmpty() {
            return this.what1 == 0 && this.what2 == 0;
        }

        public java.lang.String toString() {
            return java.lang.String.format("userId=%d what1=0x%08X what2=0x%08X pkg=%s token=%s", java.lang.Integer.valueOf(this.userId), java.lang.Integer.valueOf(this.what1), java.lang.Integer.valueOf(this.what2), this.pkg, this.token);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public StatusBarManagerService(android.content.Context context) {
        this.mContext = context;
        this.mSBMS.init(context);
        com.android.server.LocalServices.addService(com.android.server.statusbar.StatusBarManagerInternal.class, this.mInternalService);
        this.mDisplayUiState.put(0, new com.android.server.statusbar.StatusBarManagerService.UiState());
        ((android.hardware.display.DisplayManager) context.getSystemService("display")).registerDisplayListener(this, this.mHandler);
        this.mActivityTaskManager = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        this.mTileRequestTracker = new com.android.server.statusbar.TileRequestTracker(this.mContext);
        this.mSessionMonitor = new com.android.server.statusbar.SessionMonitor(this.mContext);
    }

    public void publishGlobalActionsProvider() {
        if (com.android.server.LocalServices.getService(com.android.server.policy.GlobalActionsProvider.class) == null) {
            com.android.server.LocalServices.addService(com.android.server.policy.GlobalActionsProvider.class, this.mGlobalActionsProvider);
        }
    }

    private android.content.om.IOverlayManager getOverlayManager() {
        if (this.mOverlayManager == null) {
            this.mOverlayManager = android.content.om.IOverlayManager.Stub.asInterface(android.os.ServiceManager.getService("overlay"));
            if (this.mOverlayManager == null) {
                android.util.Slog.w("StatusBarManager", "warning: no OVERLAY_SERVICE");
            }
        }
        return this.mOverlayManager;
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayAdded(int displayId) {
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayRemoved(int displayId) {
        synchronized (this.mLock) {
            this.mDisplayUiState.remove(displayId);
        }
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayChanged(int displayId) {
    }

    private boolean isDisable2FlagSet(int target2) {
        int disabled2 = this.mDisplayUiState.get(0).getDisabled2();
        return (disabled2 & target2) == target2;
    }

    public void expandNotificationsPanel() {
        enforceExpandStatusBar();
        if (!isDisable2FlagSet(4) && this.mBar != null) {
            try {
                this.mBar.animateExpandNotificationsPanel();
            } catch (android.os.RemoteException e) {
            }
            this.mSBMS.expandNotificationsPanel();
        }
    }

    public void collapsePanels() {
        if (checkCanCollapseStatusBar("collapsePanels") && this.mBar != null) {
            try {
                this.mBar.animateCollapsePanels();
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void togglePanel() {
        if (checkCanCollapseStatusBar("togglePanel") && !isDisable2FlagSet(4) && this.mBar != null) {
            try {
                this.mBar.toggleNotificationsPanel();
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void expandSettingsPanel(java.lang.String subPanel) {
        enforceExpandStatusBar();
        if (this.mBar != null) {
            try {
                this.mBar.animateExpandSettingsPanel(subPanel);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void addTile(android.content.ComponentName component) {
        if (android.view.accessibility.Flags.a11yQsShortcut()) {
            addQsTileToFrontOrEnd(component, false);
            return;
        }
        enforceStatusBarOrShell();
        if (this.mBar != null) {
            try {
                this.mBar.addQsTile(component);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addQsTileToFrontOrEnd(android.content.ComponentName tile, boolean end) {
        enforceStatusBarOrShell();
        if (this.mBar != null) {
            try {
                this.mBar.addQsTileToFrontOrEnd(tile, end);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void remTile(android.content.ComponentName component) {
        enforceStatusBarOrShell();
        if (this.mBar != null) {
            try {
                this.mBar.remQsTile(component);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void setTiles(java.lang.String tiles) {
        enforceStatusBarOrShell();
        if (this.mBar != null) {
            try {
                this.mBar.setQsTiles(tiles.split(","));
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void clickTile(android.content.ComponentName component) {
        enforceStatusBarOrShell();
        if (this.mBar != null) {
            try {
                this.mBar.clickQsTile(component);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void handleSystemKey(android.view.KeyEvent key) throws android.os.RemoteException {
        if (!checkCanCollapseStatusBar("handleSystemKey")) {
            return;
        }
        this.mLastSystemKey = key.getKeyCode();
        if (this.mBar != null) {
            try {
                this.mBar.handleSystemKey(key);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public int getLastSystemKey() {
        enforceStatusBar();
        return this.mLastSystemKey;
    }

    public void showPinningEnterExitToast(boolean entering) throws android.os.RemoteException {
        if (this.mBar != null) {
            try {
                this.mBar.showPinningEnterExitToast(entering);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void showPinningEscapeToast() throws android.os.RemoteException {
        if (this.mBar != null) {
            try {
                this.mBar.showPinningEscapeToast();
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void showAuthenticationDialog(android.hardware.biometrics.PromptInfo promptInfo, android.hardware.biometrics.IBiometricSysuiReceiver receiver, int[] sensorIds, boolean credentialAllowed, boolean requireConfirmation, int userId, long operationId, java.lang.String opPackageName, long requestId) {
        enforceBiometricDialog();
        if (this.mBar != null) {
            try {
                this.mBar.showAuthenticationDialog(promptInfo, receiver, sensorIds, credentialAllowed, requireConfirmation, userId, operationId, opPackageName, requestId);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void onBiometricAuthenticated(int modality) {
        enforceBiometricDialog();
        if (this.mBar != null) {
            try {
                this.mBar.onBiometricAuthenticated(modality);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void onBiometricHelp(int modality, java.lang.String message) {
        enforceBiometricDialog();
        if (this.mBar != null) {
            try {
                this.mBar.onBiometricHelp(modality, message);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void onBiometricError(int modality, int error, int vendorCode) {
        enforceBiometricDialog();
        if (this.mBar != null) {
            try {
                this.mBar.onBiometricError(modality, error, vendorCode);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void hideAuthenticationDialog(long requestId) {
        enforceBiometricDialog();
        if (this.mBar != null) {
            try {
                this.mBar.hideAuthenticationDialog(requestId);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void setBiometicContextListener(android.hardware.biometrics.IBiometricContextListener listener) {
        enforceStatusBarService();
        synchronized (this.mLock) {
            this.mBiometricContextListener = listener;
        }
        if (this.mBar != null) {
            try {
                this.mBar.setBiometicContextListener(listener);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void setUdfpsRefreshRateCallback(android.hardware.fingerprint.IUdfpsRefreshRateRequestCallback callback) {
        enforceStatusBarService();
        if (this.mBar != null) {
            try {
                this.mBar.setUdfpsRefreshRateCallback(callback);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void startTracing() {
        if (this.mBar != null) {
            try {
                this.mBar.startTracing();
                this.mTracingEnabled = true;
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void stopTracing() {
        if (this.mBar != null) {
            try {
                this.mTracingEnabled = false;
                this.mBar.stopTracing();
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public boolean isTracing() {
        return this.mTracingEnabled;
    }

    public void disable(int what, android.os.IBinder token, java.lang.String pkg) {
        android.util.Slog.i(TAG, "disable, what:" + what + ", pkg:" + pkg + " token:" + token);
        disableForUser(what, token, pkg, this.mCurrentUserId);
    }

    public void disableForUser(int what, android.os.IBinder token, java.lang.String pkg, int userId) {
        enforceStatusBar();
        synchronized (this.mLock) {
            disableLocked(0, userId, what, token, pkg, 1);
        }
    }

    public void disable2(int what, android.os.IBinder token, java.lang.String pkg) {
        disable2ForUser(what, token, pkg, this.mCurrentUserId);
    }

    public void disable2ForUser(int what, android.os.IBinder token, java.lang.String pkg, int userId) {
        enforceStatusBar();
        synchronized (this.mLock) {
            disableLocked(0, userId, what, token, pkg, 2);
        }
    }

    private void disableLocked(int displayId, int userId, int what, android.os.IBinder token, java.lang.String pkg, int whichFlag) {
        manageDisableListLocked(userId, what, token, pkg, whichFlag);
        final int net1 = gatherDisableActionsLocked(this.mCurrentUserId, 1);
        int net2 = gatherDisableActionsLocked(this.mCurrentUserId, 2);
        com.android.server.statusbar.StatusBarManagerService.UiState state = getUiState(displayId);
        if (!state.disableEquals(net1, net2)) {
            state.setDisabled(net1, net2);
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.statusbar.StatusBarManagerService$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$disableLocked$0(net1);
                }
            });
            com.android.internal.statusbar.IStatusBar bar = this.mBar;
            if (bar != null) {
                try {
                    android.util.Slog.i(TAG, "disableLocked, what:" + java.lang.Integer.toHexString(what) + ", pkg:" + pkg + " userId: " + userId + " token:" + token + " net1:" + java.lang.Integer.toHexString(net1) + " net2:" + java.lang.Integer.toHexString(net2));
                    bar.disable(displayId, net1, net2);
                } catch (android.os.RemoteException e) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$disableLocked$0(int net1) {
        this.mNotificationDelegate.onSetDisabled(net1);
    }

    public int[] getDisableFlags(android.os.IBinder token, int userId) {
        enforceStatusBar();
        int disable1 = 0;
        int disable2 = 0;
        synchronized (this.mLock) {
            com.android.server.statusbar.StatusBarManagerService.DisableRecord record = (com.android.server.statusbar.StatusBarManagerService.DisableRecord) findMatchingRecordLocked(token, userId).second;
            if (record != null) {
                disable1 = record.what1;
                disable2 = record.what2;
            }
        }
        return new int[]{disable1, disable2};
    }

    void runGcForTest() {
        if (!android.os.Build.IS_DEBUGGABLE) {
            throw new java.lang.SecurityException("runGcForTest requires a debuggable build");
        }
        com.android.internal.util.GcUtils.runGcAndFinalizersSync();
        if (this.mBar != null) {
            try {
                this.mBar.runGcForTest();
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void setIcon(java.lang.String slot, java.lang.String iconPackage, int iconId, int iconLevel, java.lang.String contentDescription) {
        enforceStatusBar();
        synchronized (this.mIcons) {
            com.android.internal.statusbar.StatusBarIcon icon = new com.android.internal.statusbar.StatusBarIcon(iconPackage, android.os.UserHandle.SYSTEM, iconId, iconLevel, 0, contentDescription, com.android.internal.statusbar.StatusBarIcon.Type.SystemIcon);
            this.mIcons.put(slot, icon);
            com.android.internal.statusbar.IStatusBar bar = this.mBar;
            if (bar != null) {
                try {
                    bar.setIcon(slot, icon);
                } catch (android.os.RemoteException e) {
                }
            }
        }
    }

    public void setIconVisibility(java.lang.String slot, boolean visibility) {
        enforceStatusBar();
        synchronized (this.mIcons) {
            com.android.internal.statusbar.StatusBarIcon icon = this.mIcons.get(slot);
            if (icon == null) {
                return;
            }
            if (icon.visible != visibility) {
                icon.visible = visibility;
                com.android.internal.statusbar.IStatusBar bar = this.mBar;
                if (bar != null) {
                    try {
                        bar.setIcon(slot, icon);
                    } catch (android.os.RemoteException e) {
                    }
                }
            }
        }
    }

    public void removeIcon(java.lang.String slot) {
        enforceStatusBar();
        synchronized (this.mIcons) {
            this.mIcons.remove(slot);
            com.android.internal.statusbar.IStatusBar bar = this.mBar;
            if (bar != null) {
                try {
                    bar.removeIcon(slot);
                } catch (android.os.RemoteException e) {
                }
            }
        }
    }

    public void setImeWindowStatus(final int displayId, final android.os.IBinder token, final int vis, final int backDisposition, final boolean showImeSwitcher) {
        enforceStatusBar();
        synchronized (this.mLock) {
            getUiState(displayId).setImeWindowState(vis, backDisposition, showImeSwitcher, token);
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.statusbar.StatusBarManagerService$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setImeWindowStatus$1(displayId, token, vis, backDisposition, showImeSwitcher);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setImeWindowStatus$1(int displayId, android.os.IBinder token, int vis, int backDisposition, boolean showImeSwitcher) {
        if (this.mBar == null) {
            return;
        }
        try {
            this.mBar.setImeWindowStatus(displayId, token, vis, backDisposition, showImeSwitcher);
        } catch (android.os.RemoteException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDisableFlags(int displayId, int flags, java.lang.String cause) {
        enforceStatusBarService();
        int unknownFlags = (-134152193) & flags;
        if (unknownFlags != 0) {
            android.util.Slog.e(TAG, "Unknown disable flags: 0x" + java.lang.Integer.toHexString(unknownFlags), new java.lang.RuntimeException());
        }
        synchronized (this.mLock) {
            disableLocked(displayId, this.mCurrentUserId, flags, this.mSysUiVisToken, cause, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.statusbar.StatusBarManagerService.UiState getUiState(int displayId) {
        com.android.server.statusbar.StatusBarManagerService.UiState state = this.mDisplayUiState.get(displayId);
        if (state == null) {
            com.android.server.statusbar.StatusBarManagerService.UiState state2 = new com.android.server.statusbar.StatusBarManagerService.UiState();
            this.mDisplayUiState.put(displayId, state2);
            return state2;
        }
        return state;
    }

    private static class UiState {
        private int mAppearance;
        private com.android.internal.view.AppearanceRegion[] mAppearanceRegions;
        private int mBehavior;
        private int mDisabled1;
        private int mDisabled2;
        private int mImeBackDisposition;
        private android.os.IBinder mImeToken;
        private int mImeWindowVis;
        private com.android.internal.statusbar.LetterboxDetails[] mLetterboxDetails;
        private boolean mNavbarColorManagedByIme;
        private java.lang.String mPackageName;
        private int mRequestedVisibleTypes;
        private boolean mShowImeSwitcher;
        private int mTransientBarTypes;

        private UiState() {
            this.mAppearance = 0;
            this.mAppearanceRegions = new com.android.internal.view.AppearanceRegion[0];
            this.mNavbarColorManagedByIme = false;
            this.mRequestedVisibleTypes = android.view.WindowInsets.Type.defaultVisible();
            this.mPackageName = "none";
            this.mDisabled1 = 0;
            this.mDisabled2 = 0;
            this.mImeWindowVis = 0;
            this.mImeBackDisposition = 0;
            this.mShowImeSwitcher = false;
            this.mImeToken = null;
            this.mLetterboxDetails = new com.android.internal.statusbar.LetterboxDetails[0];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setBarAttributes(int appearance, com.android.internal.view.AppearanceRegion[] appearanceRegions, boolean navbarColorManagedByIme, int behavior, int requestedVisibleTypes, java.lang.String packageName, com.android.internal.statusbar.LetterboxDetails[] letterboxDetails) {
            this.mAppearance = appearance;
            this.mAppearanceRegions = appearanceRegions;
            this.mNavbarColorManagedByIme = navbarColorManagedByIme;
            this.mBehavior = behavior;
            this.mRequestedVisibleTypes = requestedVisibleTypes;
            this.mPackageName = packageName;
            this.mLetterboxDetails = letterboxDetails;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void showTransient(int types) {
            this.mTransientBarTypes |= types;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearTransient(int types) {
            this.mTransientBarTypes &= ~types;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getDisabled1() {
            return this.mDisabled1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getDisabled2() {
            return this.mDisabled2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setDisabled(int disabled1, int disabled2) {
            this.mDisabled1 = disabled1;
            this.mDisabled2 = disabled2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean disableEquals(int disabled1, int disabled2) {
            return this.mDisabled1 == disabled1 && this.mDisabled2 == disabled2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setImeWindowState(int vis, int backDisposition, boolean showImeSwitcher, android.os.IBinder token) {
            this.mImeWindowVis = vis;
            this.mImeBackDisposition = backDisposition;
            this.mShowImeSwitcher = showImeSwitcher;
            this.mImeToken = token;
        }
    }

    private void enforceStatusBarOrShell() {
        if (android.os.Binder.getCallingUid() == 2000) {
            return;
        }
        enforceStatusBar();
    }

    private void enforceStatusBar() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.STATUS_BAR", TAG);
    }

    private void enforceExpandStatusBar() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.EXPAND_STATUS_BAR", TAG);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceStatusBarService() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.STATUS_BAR_SERVICE", TAG);
    }

    private void enforceBiometricDialog() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_BIOMETRIC_DIALOG", TAG);
    }

    private void enforceMediaContentControl() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MEDIA_CONTENT_CONTROL", TAG);
    }

    private void enforceControlDeviceStatePermission() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.CONTROL_DEVICE_STATE", TAG);
    }

    private boolean doesCallerHoldInteractAcrossUserPermission() {
        return this.mContext.checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL") == 0 || this.mContext.checkCallingPermission("android.permission.INTERACT_ACROSS_USERS") == 0;
    }

    private boolean checkCanCollapseStatusBar(java.lang.String method) {
        int uid = android.os.Binder.getCallingUid();
        int pid = android.os.Binder.getCallingPid();
        if (android.app.compat.CompatChanges.isChangeEnabled(LOCK_DOWN_COLLAPSE_STATUS_BAR, uid)) {
            enforceStatusBar();
            return true;
        }
        if (this.mContext.checkPermission("android.permission.STATUS_BAR", pid, uid) != 0) {
            enforceExpandStatusBar();
            if (!this.mActivityTaskManager.canCloseSystemDialogs(pid, uid)) {
                android.util.Slog.e(TAG, "Permission Denial: Method " + method + "() requires permission android.permission.STATUS_BAR, ignoring call.");
                return false;
            }
            return true;
        }
        return true;
    }

    public com.android.internal.statusbar.RegisterStatusBarResult registerStatusBar(com.android.internal.statusbar.IStatusBar bar) {
        android.util.ArrayMap<java.lang.String, com.android.internal.statusbar.StatusBarIcon> icons;
        com.android.internal.statusbar.RegisterStatusBarResult registerStatusBarResult;
        enforceStatusBarService();
        android.util.Slog.i(TAG, "registerStatusBar bar=" + bar);
        this.mBar = bar;
        this.mDeathRecipient.linkToDeath();
        notifyBarAttachChanged();
        synchronized (this.mIcons) {
            icons = new android.util.ArrayMap<>(this.mIcons);
        }
        synchronized (this.mLock) {
            com.android.server.statusbar.StatusBarManagerService.UiState state = this.mDisplayUiState.get(0);
            registerStatusBarResult = new com.android.internal.statusbar.RegisterStatusBarResult(icons, gatherDisableActionsLocked(this.mCurrentUserId, 1), state.mAppearance, state.mAppearanceRegions, state.mImeWindowVis, state.mImeBackDisposition, state.mShowImeSwitcher, gatherDisableActionsLocked(this.mCurrentUserId, 2), state.mImeToken, state.mNavbarColorManagedByIme, state.mBehavior, state.mRequestedVisibleTypes, state.mPackageName, state.mTransientBarTypes, state.mLetterboxDetails);
        }
        return registerStatusBarResult;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyBarAttachChanged() {
        com.android.server.UiThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.statusbar.StatusBarManagerService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyBarAttachChanged$2();
            }
        });
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.statusbar.StatusBarManagerService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyBarAttachChanged$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyBarAttachChanged$2() {
        if (this.mGlobalActionListener == null) {
            return;
        }
        this.mGlobalActionListener.onGlobalActionsAvailableChanged(this.mBar != null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyBarAttachChanged$3() {
        synchronized (this.mLock) {
            setUdfpsRefreshRateCallback(this.mUdfpsRefreshRateRequestCallback);
            setBiometicContextListener(this.mBiometricContextListener);
        }
    }

    void registerOverlayManager(android.content.om.IOverlayManager overlayManager) {
        this.mOverlayManager = overlayManager;
    }

    public void onPanelRevealed(boolean clearNotificationEffects, int numItems) {
        enforceStatusBarService();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.onPanelRevealed(clearNotificationEffects, numItems);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void clearNotificationEffects() throws android.os.RemoteException {
        enforceStatusBarService();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.clearEffects();
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onPanelHidden() throws android.os.RemoteException {
        enforceStatusBarService();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.onPanelHidden();
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void shutdown() {
        enforceStatusBarService();
        final java.lang.String reason = "userrequested";
        com.android.server.power.ShutdownCheckPoints.recordCheckPoint(android.os.Binder.getCallingPid(), "userrequested");
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.prepareForPossibleShutdown();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.statusbar.StatusBarManagerService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.power.ShutdownThread.shutdown(com.android.server.statusbar.StatusBarManagerService.getUiContext(), reason, false);
                }
            });
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void reboot(final boolean safeMode) {
        final java.lang.String reason;
        enforceStatusBarService();
        if (safeMode) {
            reason = "safemode";
        } else {
            reason = "userrequested";
        }
        com.android.server.power.ShutdownCheckPoints.recordCheckPoint(android.os.Binder.getCallingPid(), reason);
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.prepareForPossibleShutdown();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.statusbar.StatusBarManagerService$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.statusbar.StatusBarManagerService.lambda$reboot$5(safeMode, reason);
                }
            });
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    static /* synthetic */ void lambda$reboot$5(boolean safeMode, java.lang.String reason) {
        if (safeMode) {
            com.android.server.power.ShutdownThread.rebootSafeMode(getUiContext(), true);
        } else {
            com.android.server.power.ShutdownThread.reboot(getUiContext(), reason, false);
        }
    }

    public void restart() {
        enforceStatusBarService();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.statusbar.StatusBarManagerService$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$restart$6();
                }
            });
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$restart$6() {
        this.mActivityManagerInternal.restart();
    }

    public void onGlobalActionsShown() {
        enforceStatusBarService();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            if (this.mGlobalActionListener == null) {
                return;
            }
            this.mGlobalActionListener.onGlobalActionsShown();
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onGlobalActionsHidden() {
        enforceStatusBarService();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            if (this.mGlobalActionListener == null) {
                return;
            }
            this.mGlobalActionListener.onGlobalActionsDismissed();
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onNotificationClick(java.lang.String key, com.android.internal.statusbar.NotificationVisibility nv) {
        enforceStatusBarService();
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.onNotificationClick(callingUid, callingPid, key, nv);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onNotificationActionClick(java.lang.String key, int actionIndex, android.app.Notification.Action action, com.android.internal.statusbar.NotificationVisibility nv, boolean generatedByAssistant) {
        enforceStatusBarService();
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.onNotificationActionClick(callingUid, callingPid, key, actionIndex, action, nv, generatedByAssistant);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onNotificationError(java.lang.String pkg, java.lang.String tag, int id, int uid, int initialPid, java.lang.String message, int userId) {
        enforceStatusBarService();
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.onNotificationError(callingUid, callingPid, pkg, tag, id, uid, initialPid, message, userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onNotificationClear(java.lang.String pkg, int userId, java.lang.String key, int dismissalSurface, int dismissalSentiment, com.android.internal.statusbar.NotificationVisibility nv) {
        enforceStatusBarService();
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.onNotificationClear(callingUid, callingPid, pkg, userId, key, dismissalSurface, dismissalSentiment, nv);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onNotificationVisibilityChanged(com.android.internal.statusbar.NotificationVisibility[] newlyVisibleKeys, com.android.internal.statusbar.NotificationVisibility[] noLongerVisibleKeys) throws android.os.RemoteException {
        enforceStatusBarService();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.onNotificationVisibilityChanged(newlyVisibleKeys, noLongerVisibleKeys);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onNotificationExpansionChanged(java.lang.String key, boolean userAction, boolean expanded, int location) throws android.os.RemoteException {
        enforceStatusBarService();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.onNotificationExpansionChanged(key, userAction, expanded, location);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onNotificationDirectReplied(java.lang.String key) throws android.os.RemoteException {
        enforceStatusBarService();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.onNotificationDirectReplied(key);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onNotificationSmartSuggestionsAdded(java.lang.String key, int smartReplyCount, int smartActionCount, boolean generatedByAssistant, boolean editBeforeSending) {
        enforceStatusBarService();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.onNotificationSmartSuggestionsAdded(key, smartReplyCount, smartActionCount, generatedByAssistant, editBeforeSending);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onNotificationSmartReplySent(java.lang.String key, int replyIndex, java.lang.CharSequence reply, int notificationLocation, boolean modifiedBeforeSending) throws android.os.RemoteException {
        enforceStatusBarService();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.onNotificationSmartReplySent(key, replyIndex, reply, notificationLocation, modifiedBeforeSending);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onNotificationSettingsViewed(java.lang.String key) throws android.os.RemoteException {
        enforceStatusBarService();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.onNotificationSettingsViewed(key);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onClearAllNotifications(int userId) {
        enforceStatusBarService();
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.onClearAll(callingUid, callingPid, userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onNotificationBubbleChanged(java.lang.String key, boolean isBubble, int flags) {
        enforceStatusBarService();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.onNotificationBubbleChanged(key, isBubble, flags);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onBubbleMetadataFlagChanged(java.lang.String key, int flags) {
        enforceStatusBarService();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.onBubbleMetadataFlagChanged(key, flags);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void hideCurrentInputMethodForBubbles(int displayId) {
        enforceStatusBarService();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.inputmethod.InputMethodManagerInternal.get().hideAllInputMethods(20, displayId);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void grantInlineReplyUriPermission(java.lang.String key, android.net.Uri uri, android.os.UserHandle user, java.lang.String packageName) {
        enforceStatusBarService();
        int callingUid = android.os.Binder.getCallingUid();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.grantInlineReplyUriPermission(key, uri, user, packageName, callingUid);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void clearInlineReplyUriPermissions(java.lang.String key) {
        enforceStatusBarService();
        int callingUid = android.os.Binder.getCallingUid();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.clearInlineReplyUriPermissions(key, callingUid);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void onNotificationFeedbackReceived(java.lang.String key, android.os.Bundle feedback) {
        enforceStatusBarService();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mNotificationDelegate.onNotificationFeedbackReceived(key, feedback);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        new com.android.server.statusbar.StatusBarShellCommand(this, this.mContext).exec(this, in, out, err, args, callback, resultReceiver);
    }

    public void showInattentiveSleepWarning() {
        enforceStatusBarService();
        com.android.internal.statusbar.IStatusBar bar = this.mBar;
        if (bar != null) {
            try {
                bar.showInattentiveSleepWarning();
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void dismissInattentiveSleepWarning(boolean animated) {
        enforceStatusBarService();
        com.android.internal.statusbar.IStatusBar bar = this.mBar;
        if (bar != null) {
            try {
                bar.dismissInattentiveSleepWarning(animated);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void suppressAmbientDisplay(boolean suppress) {
        enforceStatusBarService();
        com.android.internal.statusbar.IStatusBar bar = this.mBar;
        if (bar != null) {
            try {
                bar.suppressAmbientDisplay(suppress);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    private void checkCallingUidPackage(java.lang.String packageName, int callingUid, int userId) {
        int packageUid = this.mPackageManagerInternal.getPackageUid(packageName, 0L, userId);
        if (android.os.UserHandle.getAppId(callingUid) != android.os.UserHandle.getAppId(packageUid)) {
            throw new java.lang.SecurityException("Package " + packageName + " does not belong to the calling uid " + callingUid);
        }
    }

    private android.content.pm.ResolveInfo isComponentValidTileService(android.content.ComponentName componentName, int userId) {
        android.content.Intent intent = new android.content.Intent("android.service.quicksettings.action.QS_TILE");
        intent.setComponent(componentName);
        android.content.pm.ResolveInfo r = this.mPackageManagerInternal.resolveService(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), 0L, userId, android.os.Process.myUid());
        int enabled = this.mPackageManagerInternal.getComponentEnabledSetting(componentName, android.os.Process.myUid(), userId);
        if (r != null && r.serviceInfo != null && resolveEnabledComponent(r.serviceInfo.enabled, enabled) && "android.permission.BIND_QUICK_SETTINGS_TILE".equals(r.serviceInfo.permission)) {
            return r;
        }
        return null;
    }

    private boolean resolveEnabledComponent(boolean defaultValue, int pmResult) {
        if (pmResult == 1) {
            return true;
        }
        if (pmResult == 0) {
            return defaultValue;
        }
        return false;
    }

    public void requestTileServiceListeningState(android.content.ComponentName componentName, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        java.lang.String packageName = componentName.getPackageName();
        boolean mustPerformChecks = android.app.compat.CompatChanges.isChangeEnabled(REQUEST_LISTENING_MUST_MATCH_PACKAGE, callingUid);
        if (mustPerformChecks) {
            int userId2 = this.mActivityManagerInternal.handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, false, 0, "requestTileServiceListeningState", packageName);
            checkCallingUidPackage(packageName, callingUid, userId2);
            int currentUser = this.mActivityManagerInternal.getCurrentUserId();
            if (userId2 != currentUser) {
                if (android.app.compat.CompatChanges.isChangeEnabled(REQUEST_LISTENING_OTHER_USER_NOOP, callingUid)) {
                    return;
                } else {
                    throw new java.lang.IllegalArgumentException("User " + userId2 + " is not the current user.");
                }
            }
        }
        com.android.internal.statusbar.IStatusBar bar = this.mBar;
        if (bar != null) {
            try {
                bar.requestTileServiceListeningState(componentName);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "requestTileServiceListeningState", e);
            }
        }
    }

    public void requestAddTile(final android.content.ComponentName componentName, java.lang.CharSequence label, android.graphics.drawable.Icon icon, final int userId, final com.android.internal.statusbar.IAddTileResultCallback callback) throws java.lang.Throwable {
        java.lang.String packageName;
        com.android.internal.statusbar.IAddTileResultCallback iAddTileResultCallback;
        int callingUid = android.os.Binder.getCallingUid();
        final java.lang.String packageName2 = componentName.getPackageName();
        this.mActivityManagerInternal.handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, false, 0, "requestAddTile", packageName2);
        checkCallingUidPackage(packageName2, callingUid, userId);
        int currentUser = this.mActivityManagerInternal.getCurrentUserId();
        if (userId != currentUser) {
            try {
                callback.onTileRequest(1003);
                return;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "requestAddTile", e);
                return;
            }
        }
        android.content.pm.ResolveInfo r = isComponentValidTileService(componentName, userId);
        if (r == null || !r.serviceInfo.exported) {
            com.android.internal.statusbar.IAddTileResultCallback iAddTileResultCallback2 = callback;
            try {
                iAddTileResultCallback2.onTileRequest(1002);
                return;
            } catch (android.os.RemoteException e2) {
                android.util.Slog.e(TAG, "requestAddTile", e2);
                return;
            }
        }
        int procState = this.mActivityManagerInternal.getUidProcessState(callingUid);
        if (android.app.ActivityManager.RunningAppProcessInfo.procStateToImportance(procState) != 100) {
            try {
                callback.onTileRequest(1004);
                return;
            } catch (android.os.RemoteException e3) {
                android.util.Slog.e(TAG, "requestAddTile", e3);
                return;
            }
        }
        synchronized (this.mCurrentRequestAddTilePackages) {
            try {
                java.lang.Long lastTime = this.mCurrentRequestAddTilePackages.get(packageName2);
                long currentTime = java.lang.System.nanoTime();
                if (lastTime != null) {
                    try {
                        if (currentTime - lastTime.longValue() < REQUEST_TIME_OUT) {
                            try {
                                callback.onTileRequest(1001);
                            } catch (android.os.RemoteException e4) {
                                android.util.Slog.e(TAG, "requestAddTile", e4);
                            }
                            return;
                        }
                    } catch (java.lang.Throwable th) {
                        th = th;
                        while (true) {
                            try {
                                throw th;
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                            }
                        }
                    }
                }
                if (lastTime != null) {
                    cancelRequestAddTileInternal(packageName2);
                }
                this.mCurrentRequestAddTilePackages.put(packageName2, java.lang.Long.valueOf(currentTime));
                if (this.mTileRequestTracker.shouldBeDenied(userId, componentName)) {
                    if (clearTileAddRequest(packageName2)) {
                        try {
                            callback.onTileRequest(0);
                            return;
                        } catch (android.os.RemoteException e5) {
                            android.util.Slog.e(TAG, "requestAddTile - callback", e5);
                            return;
                        }
                    }
                    return;
                }
                com.android.internal.statusbar.IAddTileResultCallback.Stub stub = new com.android.internal.statusbar.IAddTileResultCallback.Stub() { // from class: com.android.server.statusbar.StatusBarManagerService.3
                    public void onTileRequest(int i) {
                        if (i == 3) {
                            i = 0;
                        } else if (i == 0) {
                            com.android.server.statusbar.StatusBarManagerService.this.mTileRequestTracker.addDenial(userId, componentName);
                        } else if (i == 2) {
                            com.android.server.statusbar.StatusBarManagerService.this.mTileRequestTracker.resetRequests(userId, componentName);
                        }
                        if (com.android.server.statusbar.StatusBarManagerService.this.clearTileAddRequest(packageName2)) {
                            try {
                                callback.onTileRequest(i);
                            } catch (android.os.RemoteException e6) {
                                android.util.Slog.e(com.android.server.statusbar.StatusBarManagerService.TAG, "requestAddTile - callback", e6);
                            }
                        }
                    }
                };
                java.lang.CharSequence appName = r.serviceInfo.applicationInfo.loadLabel(this.mContext.getPackageManager());
                com.android.internal.statusbar.IStatusBar bar = this.mBar;
                if (bar != null) {
                    packageName = packageName2;
                    iAddTileResultCallback = callback;
                    try {
                        bar.requestAddTile(callingUid, componentName, appName, label, icon, stub);
                        return;
                    } catch (android.os.RemoteException e6) {
                        android.util.Slog.e(TAG, "requestAddTile", e6);
                    }
                } else {
                    packageName = packageName2;
                    iAddTileResultCallback = callback;
                }
                clearTileAddRequest(packageName);
                try {
                    iAddTileResultCallback.onTileRequest(1005);
                } catch (android.os.RemoteException e7) {
                    android.util.Slog.e(TAG, "requestAddTile", e7);
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    public void cancelRequestAddTile(java.lang.String packageName) {
        enforceStatusBar();
        cancelRequestAddTileInternal(packageName);
    }

    private void cancelRequestAddTileInternal(java.lang.String packageName) {
        clearTileAddRequest(packageName);
        com.android.internal.statusbar.IStatusBar bar = this.mBar;
        if (bar != null) {
            try {
                bar.cancelRequestAddTile(packageName);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "requestAddTile", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean clearTileAddRequest(java.lang.String packageName) {
        boolean z;
        synchronized (this.mCurrentRequestAddTilePackages) {
            z = this.mCurrentRequestAddTilePackages.remove(packageName) != null;
        }
        return z;
    }

    public void onSessionStarted(int sessionType, com.android.internal.logging.InstanceId instance) {
        this.mSessionMonitor.onSessionStarted(sessionType, instance);
    }

    public void onSessionEnded(int sessionType, com.android.internal.logging.InstanceId instance) {
        this.mSessionMonitor.onSessionEnded(sessionType, instance);
    }

    public void registerSessionListener(int sessionFlags, com.android.internal.statusbar.ISessionListener listener) {
        this.mSessionMonitor.registerSessionListener(sessionFlags, listener);
    }

    public void unregisterSessionListener(int sessionFlags, com.android.internal.statusbar.ISessionListener listener) {
        this.mSessionMonitor.unregisterSessionListener(sessionFlags, listener);
    }

    public java.lang.String[] getStatusBarIcons() {
        return this.mContext.getResources().getStringArray(android.R.array.config_serviceStateLocationAllowedPackages);
    }

    public void setNavBarMode(int navBarMode) {
        enforceStatusBar();
        if (navBarMode != 0 && navBarMode != 1) {
            throw new java.lang.IllegalArgumentException("Supplied navBarMode not supported: " + navBarMode);
        }
        int userId = this.mCurrentUserId;
        int callingUserId = android.os.UserHandle.getUserId(android.os.Binder.getCallingUid());
        if (this.mCurrentUserId != callingUserId && !doesCallerHoldInteractAcrossUserPermission()) {
            throw new java.lang.SecurityException("Calling user id: " + callingUserId + ", cannot call on behalf of current user id: " + this.mCurrentUserId + ".");
        }
        long userIdentity = android.os.Binder.clearCallingIdentity();
        try {
            try {
                android.provider.Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "nav_bar_kids_mode", navBarMode, userId);
                android.provider.Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "nav_bar_force_visible", navBarMode, userId);
                android.content.om.IOverlayManager overlayManager = getOverlayManager();
                if (overlayManager != null && navBarMode == 1 && isPackageSupported("com.android.internal.systemui.navbar.threebutton")) {
                    overlayManager.setEnabledExclusiveInCategory("com.android.internal.systemui.navbar.threebutton", userId);
                }
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(userIdentity);
        }
    }

    public int getNavBarMode() {
        enforceStatusBar();
        int userId = this.mCurrentUserId;
        long userIdentity = android.os.Binder.clearCallingIdentity();
        try {
            int navBarKidsMode = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "nav_bar_kids_mode", userId);
            return navBarKidsMode;
        } catch (android.provider.Settings.SettingNotFoundException e) {
            return 0;
        } finally {
            android.os.Binder.restoreCallingIdentity(userIdentity);
        }
    }

    private boolean isPackageSupported(java.lang.String packageName) {
        if (packageName == null) {
            return false;
        }
        try {
            return this.mContext.getPackageManager().getPackageInfo(packageName, android.content.pm.PackageManager.PackageInfoFlags.of(0L)) != null;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void updateMediaTapToTransferSenderDisplay(int displayState, android.media.MediaRoute2Info routeInfo, com.android.internal.statusbar.IUndoMediaTransferCallback undoCallback) {
        enforceMediaContentControl();
        com.android.internal.statusbar.IStatusBar bar = this.mBar;
        if (bar != null) {
            try {
                bar.updateMediaTapToTransferSenderDisplay(displayState, routeInfo, undoCallback);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "updateMediaTapToTransferSenderDisplay", e);
            }
        }
    }

    public void updateMediaTapToTransferReceiverDisplay(int displayState, android.media.MediaRoute2Info routeInfo, android.graphics.drawable.Icon appIcon, java.lang.CharSequence appName) {
        enforceMediaContentControl();
        com.android.internal.statusbar.IStatusBar bar = this.mBar;
        if (bar != null) {
            try {
                bar.updateMediaTapToTransferReceiverDisplay(displayState, routeInfo, appIcon, appName);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "updateMediaTapToTransferReceiverDisplay", e);
            }
        }
    }

    public void registerNearbyMediaDevicesProvider(android.media.INearbyMediaDevicesProvider provider) {
        enforceMediaContentControl();
        com.android.internal.statusbar.IStatusBar bar = this.mBar;
        if (bar != null) {
            try {
                bar.registerNearbyMediaDevicesProvider(provider);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "registerNearbyMediaDevicesProvider", e);
            }
        }
    }

    public void unregisterNearbyMediaDevicesProvider(android.media.INearbyMediaDevicesProvider provider) {
        enforceMediaContentControl();
        com.android.internal.statusbar.IStatusBar bar = this.mBar;
        if (bar != null) {
            try {
                bar.unregisterNearbyMediaDevicesProvider(provider);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "unregisterNearbyMediaDevicesProvider", e);
            }
        }
    }

    public void showRearDisplayDialog(int currentState) {
        enforceControlDeviceStatePermission();
        com.android.internal.statusbar.IStatusBar bar = this.mBar;
        if (bar != null) {
            try {
                bar.showRearDisplayDialog(currentState);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "showRearDisplayDialog", e);
            }
        }
    }

    public void passThroughShellCommand(java.lang.String[] args, java.io.FileDescriptor fd) {
        enforceStatusBarOrShell();
        if (this.mBar == null) {
            return;
        }
        try {
            com.android.internal.os.TransferPipe tp = new com.android.internal.os.TransferPipe();
            try {
                tp.setBufferPrefix("  ");
                this.mBar.passThroughShellCommand(args, tp.getWriteFd());
                tp.go(fd);
                tp.close();
            } finally {
            }
        } catch (java.lang.Throwable t) {
            android.util.Slog.e(TAG, "Error sending command to IStatusBar", t);
        }
    }

    void manageDisableListLocked(int userId, int what, android.os.IBinder token, java.lang.String pkg, int which) {
        android.util.Pair<java.lang.Integer, com.android.server.statusbar.StatusBarManagerService.DisableRecord> match = findMatchingRecordLocked(token, userId);
        int i = ((java.lang.Integer) match.first).intValue();
        com.android.server.statusbar.StatusBarManagerService.DisableRecord record = (com.android.server.statusbar.StatusBarManagerService.DisableRecord) match.second;
        if (!token.isBinderAlive()) {
            if (record != null) {
                this.mDisableRecords.remove(i);
                record.token.unlinkToDeath(record, 0);
                return;
            }
            return;
        }
        if (record != null) {
            record.setFlags(what, which, pkg);
            if (record.isEmpty()) {
                this.mDisableRecords.remove(i);
                record.token.unlinkToDeath(record, 0);
                return;
            }
            return;
        }
        com.android.server.statusbar.StatusBarManagerService.DisableRecord record2 = new com.android.server.statusbar.StatusBarManagerService.DisableRecord(userId, token);
        record2.setFlags(what, which, pkg);
        this.mDisableRecords.add(record2);
    }

    private android.util.Pair<java.lang.Integer, com.android.server.statusbar.StatusBarManagerService.DisableRecord> findMatchingRecordLocked(android.os.IBinder token, int userId) {
        int numRecords = this.mDisableRecords.size();
        com.android.server.statusbar.StatusBarManagerService.DisableRecord record = null;
        int i = 0;
        while (true) {
            if (i >= numRecords) {
                break;
            }
            com.android.server.statusbar.StatusBarManagerService.DisableRecord r = this.mDisableRecords.get(i);
            if (r.token != token || r.userId != userId) {
                i++;
            } else {
                record = r;
                break;
            }
        }
        return new android.util.Pair<>(java.lang.Integer.valueOf(i), record);
    }

    int gatherDisableActionsLocked(int userId, int which) {
        int N = this.mDisableRecords.size();
        int net = 0;
        for (int i = 0; i < N; i++) {
            com.android.server.statusbar.StatusBarManagerService.DisableRecord rec = this.mDisableRecords.get(i);
            if (rec.userId == userId) {
                net |= rec.getFlags(which);
            }
        }
        return net;
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        java.util.ArrayList<java.lang.String> requests;
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            boolean proto = false;
            for (java.lang.String str : args) {
                if ("--proto".equals(str)) {
                    proto = true;
                }
            }
            if (proto) {
                if (this.mBar == null) {
                    return;
                }
                try {
                    com.android.internal.os.TransferPipe tp = new com.android.internal.os.TransferPipe();
                    try {
                        this.mBar.dumpProto(args, tp.getWriteFd());
                        tp.go(fd);
                        tp.close();
                        return;
                    } finally {
                    }
                } catch (java.lang.Throwable t) {
                    android.util.Slog.e(TAG, "Error sending command to IStatusBar", t);
                    return;
                }
            }
            synchronized (this.mLock) {
                for (int i = 0; i < this.mDisplayUiState.size(); i++) {
                    int key = this.mDisplayUiState.keyAt(i);
                    com.android.server.statusbar.StatusBarManagerService.UiState state = this.mDisplayUiState.get(key);
                    pw.println("  displayId=" + key);
                    pw.println("    mDisabled1=0x" + java.lang.Integer.toHexString(state.getDisabled1()));
                    pw.println("    mDisabled2=0x" + java.lang.Integer.toHexString(state.getDisabled2()));
                }
                int N = this.mDisableRecords.size();
                pw.println("  mDisableRecords.size=" + N);
                for (int i2 = 0; i2 < N; i2++) {
                    com.android.server.statusbar.StatusBarManagerService.DisableRecord tok = this.mDisableRecords.get(i2);
                    pw.println("    [" + i2 + "] " + tok);
                }
                pw.println("  mCurrentUserId=" + this.mCurrentUserId);
                pw.println("  mIcons=");
                for (java.lang.String slot : this.mIcons.keySet()) {
                    pw.println("    ");
                    pw.print(slot);
                    pw.print(" -> ");
                    com.android.internal.statusbar.StatusBarIcon icon = this.mIcons.get(slot);
                    pw.print(icon);
                    if (!android.text.TextUtils.isEmpty(icon.contentDescription)) {
                        pw.print(" \"");
                        pw.print(icon.contentDescription);
                        pw.print("\"");
                    }
                    pw.println();
                }
                synchronized (this.mCurrentRequestAddTilePackages) {
                    requests = new java.util.ArrayList<>(this.mCurrentRequestAddTilePackages.keySet());
                }
                pw.println("  mCurrentRequestAddTilePackages=[");
                int reqN = requests.size();
                for (int i3 = 0; i3 < reqN; i3++) {
                    pw.println("    " + requests.get(i3) + ",");
                }
                pw.println("  ]");
                android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw, "  ");
                this.mTileRequestTracker.dump(fd, ipw.increaseIndent(), args);
            }
        }
    }

    private static final android.content.Context getUiContext() {
        return android.app.ActivityThread.currentActivityThread().getSystemUiContext();
    }
}
