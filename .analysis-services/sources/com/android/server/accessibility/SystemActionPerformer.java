package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public class SystemActionPerformer {
    private static final java.lang.String TAG = "SystemActionPerformer";
    private final android.content.Context mContext;
    private final com.android.server.accessibility.SystemActionPerformer.DisplayUpdateCallBack mDisplayUpdateCallBack;
    private final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction mLegacyBackAction;
    private final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction mLegacyHomeAction;
    private final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction mLegacyLockScreenAction;
    private final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction mLegacyNotificationsAction;
    private final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction mLegacyPowerDialogAction;
    private final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction mLegacyQuickSettingsAction;
    private final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction mLegacyRecentsAction;
    private final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction mLegacyTakeScreenshotAction;
    private final com.android.server.accessibility.SystemActionPerformer.SystemActionsChangedListener mListener;
    private final java.util.Map<java.lang.Integer, android.app.RemoteAction> mRegisteredSystemActions;
    private java.util.function.Supplier<com.android.internal.util.ScreenshotHelper> mScreenshotHelperSupplier;
    private final java.lang.Object mSystemActionLock;
    private final com.android.server.wm.WindowManagerInternal mWindowManagerService;

    interface DisplayUpdateCallBack {
        int getLastNonProxyTopFocusedDisplayId();

        void moveNonProxyTopFocusedDisplayToTopIfNeeded();
    }

    interface SystemActionsChangedListener {
        void onSystemActionsChanged();
    }

    public SystemActionPerformer(android.content.Context context, com.android.server.wm.WindowManagerInternal windowManagerInternal) {
        this(context, windowManagerInternal, null, null, null);
    }

    public SystemActionPerformer(android.content.Context context, com.android.server.wm.WindowManagerInternal windowManagerInternal, java.util.function.Supplier<com.android.internal.util.ScreenshotHelper> screenshotHelperSupplier) {
        this(context, windowManagerInternal, screenshotHelperSupplier, null, null);
    }

    public SystemActionPerformer(android.content.Context context, com.android.server.wm.WindowManagerInternal windowManagerInternal, java.util.function.Supplier<com.android.internal.util.ScreenshotHelper> screenshotHelperSupplier, com.android.server.accessibility.SystemActionPerformer.SystemActionsChangedListener listener, com.android.server.accessibility.SystemActionPerformer.DisplayUpdateCallBack callback) {
        this.mSystemActionLock = new java.lang.Object();
        this.mRegisteredSystemActions = new android.util.ArrayMap();
        this.mContext = context;
        this.mWindowManagerService = windowManagerInternal;
        this.mListener = listener;
        this.mDisplayUpdateCallBack = callback;
        this.mScreenshotHelperSupplier = screenshotHelperSupplier;
        this.mLegacyHomeAction = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(2, this.mContext.getResources().getString(android.R.string.accessibility_shortcut_off));
        this.mLegacyBackAction = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(1, this.mContext.getResources().getString(android.R.string.accessibility_service_screen_control_title));
        this.mLegacyRecentsAction = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(3, this.mContext.getResources().getString(android.R.string.accessibility_system_action_back_label));
        this.mLegacyNotificationsAction = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(4, this.mContext.getResources().getString(android.R.string.accessibility_shortcut_single_service_warning));
        this.mLegacyQuickSettingsAction = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(5, this.mContext.getResources().getString(android.R.string.accessibility_shortcut_warning_dialog_title));
        this.mLegacyPowerDialogAction = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(6, this.mContext.getResources().getString(android.R.string.accessibility_shortcut_toogle_warning));
        this.mLegacyLockScreenAction = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(8, this.mContext.getResources().getString(android.R.string.accessibility_shortcut_on));
        this.mLegacyTakeScreenshotAction = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(9, this.mContext.getResources().getString(android.R.string.accessibility_system_action_dismiss_notification_shade));
    }

    public void registerSystemAction(int id, android.app.RemoteAction action) {
        synchronized (this.mSystemActionLock) {
            this.mRegisteredSystemActions.put(java.lang.Integer.valueOf(id), action);
        }
        if (this.mListener != null) {
            this.mListener.onSystemActionsChanged();
        }
    }

    public void unregisterSystemAction(int id) {
        synchronized (this.mSystemActionLock) {
            this.mRegisteredSystemActions.remove(java.lang.Integer.valueOf(id));
        }
        if (this.mListener != null) {
            this.mListener.onSystemActionsChanged();
        }
    }

    public java.util.List<android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction> getSystemActions() {
        java.util.List<android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction> systemActions = new java.util.ArrayList<>();
        synchronized (this.mSystemActionLock) {
            for (java.util.Map.Entry<java.lang.Integer, android.app.RemoteAction> entry : this.mRegisteredSystemActions.entrySet()) {
                android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction systemAction = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(entry.getKey().intValue(), entry.getValue().getTitle());
                systemActions.add(systemAction);
            }
            addLegacySystemActions(systemActions);
        }
        return systemActions;
    }

    private void addLegacySystemActions(java.util.List<android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction> systemActions) {
        if (!this.mRegisteredSystemActions.containsKey(1)) {
            systemActions.add(this.mLegacyBackAction);
        }
        if (!this.mRegisteredSystemActions.containsKey(2)) {
            systemActions.add(this.mLegacyHomeAction);
        }
        if (!this.mRegisteredSystemActions.containsKey(3)) {
            systemActions.add(this.mLegacyRecentsAction);
        }
        if (!this.mRegisteredSystemActions.containsKey(4)) {
            systemActions.add(this.mLegacyNotificationsAction);
        }
        if (!this.mRegisteredSystemActions.containsKey(5)) {
            systemActions.add(this.mLegacyQuickSettingsAction);
        }
        if (!this.mRegisteredSystemActions.containsKey(6)) {
            systemActions.add(this.mLegacyPowerDialogAction);
        }
        if (!this.mRegisteredSystemActions.containsKey(8)) {
            systemActions.add(this.mLegacyLockScreenAction);
        }
        if (!this.mRegisteredSystemActions.containsKey(9)) {
            systemActions.add(this.mLegacyTakeScreenshotAction);
        }
    }

    public boolean performSystemAction(int actionId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mSystemActionLock) {
                this.mDisplayUpdateCallBack.moveNonProxyTopFocusedDisplayToTopIfNeeded();
                android.app.RemoteAction registeredAction = this.mRegisteredSystemActions.get(java.lang.Integer.valueOf(actionId));
                if (registeredAction != null) {
                    try {
                        registeredAction.getActionIntent().send();
                        return true;
                    } catch (android.app.PendingIntent.CanceledException ex) {
                        android.util.Slog.e(TAG, "canceled PendingIntent for global action " + ((java.lang.Object) registeredAction.getTitle()), ex);
                        return false;
                    }
                }
                switch (actionId) {
                    case 1:
                        sendDownAndUpKeyEvents(4, 257);
                        return true;
                    case 2:
                        sendDownAndUpKeyEvents(3, 257);
                        return true;
                    case 3:
                        return openRecents();
                    case 4:
                        expandNotifications();
                        return true;
                    case 5:
                        expandQuickSettings();
                        return true;
                    case 6:
                        showGlobalActions();
                        return true;
                    case 7:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    default:
                        android.util.Slog.e(TAG, "Invalid action id: " + actionId);
                        return false;
                    case 8:
                        return lockScreen();
                    case 9:
                        return takeScreenshot();
                    case 10:
                        if (!com.android.internal.accessibility.util.AccessibilityUtils.interceptHeadsetHookForActiveCall(this.mContext)) {
                            sendDownAndUpKeyEvents(79, 257);
                        }
                        return true;
                    case 16:
                        sendDownAndUpKeyEvents(19, com.android.server.usb.descriptors.UsbTerminalTypes.TERMINAL_OUT_SPEAKER);
                        return true;
                    case 17:
                        sendDownAndUpKeyEvents(20, com.android.server.usb.descriptors.UsbTerminalTypes.TERMINAL_OUT_SPEAKER);
                        return true;
                    case 18:
                        sendDownAndUpKeyEvents(21, com.android.server.usb.descriptors.UsbTerminalTypes.TERMINAL_OUT_SPEAKER);
                        return true;
                    case 19:
                        sendDownAndUpKeyEvents(22, com.android.server.usb.descriptors.UsbTerminalTypes.TERMINAL_OUT_SPEAKER);
                        return true;
                    case 20:
                        sendDownAndUpKeyEvents(23, com.android.server.usb.descriptors.UsbTerminalTypes.TERMINAL_OUT_SPEAKER);
                        return true;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private void sendDownAndUpKeyEvents(int keyCode, int source) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            long downTime = android.os.SystemClock.uptimeMillis();
            sendKeyEventIdentityCleared(keyCode, 0, downTime, downTime, source);
            sendKeyEventIdentityCleared(keyCode, 1, downTime, android.os.SystemClock.uptimeMillis(), source);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private void sendKeyEventIdentityCleared(int keyCode, int action, long downTime, long time, int source) {
        android.view.KeyEvent event = android.view.KeyEvent.obtain(downTime, time, action, keyCode, 0, 0, -1, 0, 8, source, this.mDisplayUpdateCallBack.getLastNonProxyTopFocusedDisplayId(), null);
        ((android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class)).injectInputEvent(event, 0);
        event.recycle();
    }

    private void expandNotifications() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.app.StatusBarManager statusBarManager = (android.app.StatusBarManager) this.mContext.getSystemService("statusbar");
            statusBarManager.expandNotificationsPanel();
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private void expandQuickSettings() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.app.StatusBarManager statusBarManager = (android.app.StatusBarManager) this.mContext.getSystemService("statusbar");
            statusBarManager.expandSettingsPanel();
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private boolean openRecents() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.statusbar.StatusBarManagerInternal statusBarService = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
            if (statusBarService != null) {
                statusBarService.toggleRecentApps();
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            }
            android.os.Binder.restoreCallingIdentity(token);
            return false;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    private void showGlobalActions() {
        this.mWindowManagerService.showGlobalActions();
    }

    private boolean lockScreen() {
        ((android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class)).goToSleep(android.os.SystemClock.uptimeMillis(), 7, 0);
        this.mWindowManagerService.lockNow();
        return true;
    }

    private boolean takeScreenshot() {
        com.android.internal.util.ScreenshotHelper screenshotHelper = this.mScreenshotHelperSupplier != null ? this.mScreenshotHelperSupplier.get() : new com.android.internal.util.ScreenshotHelper(this.mContext);
        screenshotHelper.takeScreenshot(4, new android.os.Handler(android.os.Looper.getMainLooper()), (java.util.function.Consumer) null);
        return true;
    }
}
