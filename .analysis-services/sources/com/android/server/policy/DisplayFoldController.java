package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
class DisplayFoldController {
    private final int mDisplayId;
    private final android.hardware.display.DisplayManagerInternal mDisplayManagerInternal;
    private java.lang.String mFocusedApp;
    private java.lang.Boolean mFolded;
    private final android.graphics.Rect mFoldedArea;
    private final android.os.Handler mHandler;
    private final com.android.server.wm.WindowManagerInternal mWindowManagerInternal;
    private android.graphics.Rect mOverrideFoldedArea = new android.graphics.Rect();
    private final android.view.DisplayInfo mNonOverrideDisplayInfo = new android.view.DisplayInfo();
    private final android.os.RemoteCallbackList<android.view.IDisplayFoldListener> mListeners = new android.os.RemoteCallbackList<>();
    private final com.android.server.policy.DisplayFoldDurationLogger mDurationLogger = new com.android.server.policy.DisplayFoldDurationLogger();

    DisplayFoldController(android.content.Context context, com.android.server.wm.WindowManagerInternal windowManagerInternal, android.hardware.display.DisplayManagerInternal displayManagerInternal, int displayId, android.graphics.Rect foldedArea, android.os.Handler handler) {
        this.mWindowManagerInternal = windowManagerInternal;
        this.mDisplayManagerInternal = displayManagerInternal;
        this.mDisplayId = displayId;
        this.mFoldedArea = new android.graphics.Rect(foldedArea);
        this.mHandler = handler;
        android.hardware.devicestate.DeviceStateManager deviceStateManager = (android.hardware.devicestate.DeviceStateManager) context.getSystemService(android.hardware.devicestate.DeviceStateManager.class);
        deviceStateManager.registerCallback(new android.os.HandlerExecutor(handler), new android.hardware.devicestate.DeviceStateManager.FoldStateListener(context, new java.util.function.Consumer() { // from class: com.android.server.policy.DisplayFoldController$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$new$0((java.lang.Boolean) obj);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(java.lang.Boolean folded) {
        setDeviceFolded(folded.booleanValue());
    }

    void finishedGoingToSleep() {
        this.mDurationLogger.onFinishedGoingToSleep();
    }

    void finishedWakingUp() {
        this.mDurationLogger.onFinishedWakingUp(this.mFolded);
    }

    private void setDeviceFolded(boolean folded) {
        android.graphics.Rect foldedArea;
        if (this.mFolded != null && this.mFolded.booleanValue() == folded) {
            return;
        }
        if (!this.mOverrideFoldedArea.isEmpty()) {
            foldedArea = this.mOverrideFoldedArea;
        } else {
            android.graphics.Rect foldedArea2 = this.mFoldedArea;
            if (!foldedArea2.isEmpty()) {
                foldedArea = this.mFoldedArea;
            } else {
                foldedArea = null;
            }
        }
        if (foldedArea != null) {
            if (folded) {
                this.mDisplayManagerInternal.getNonOverrideDisplayInfo(this.mDisplayId, this.mNonOverrideDisplayInfo);
                int dx = ((this.mNonOverrideDisplayInfo.logicalWidth - foldedArea.width()) / 2) - foldedArea.left;
                int dy = ((this.mNonOverrideDisplayInfo.logicalHeight - foldedArea.height()) / 2) - foldedArea.top;
                this.mDisplayManagerInternal.setDisplayScalingDisabled(this.mDisplayId, true);
                this.mWindowManagerInternal.setForcedDisplaySize(this.mDisplayId, foldedArea.width(), foldedArea.height());
                this.mDisplayManagerInternal.setDisplayOffsets(this.mDisplayId, -dx, -dy);
            } else {
                this.mDisplayManagerInternal.setDisplayScalingDisabled(this.mDisplayId, false);
                this.mWindowManagerInternal.clearForcedDisplaySize(this.mDisplayId);
                this.mDisplayManagerInternal.setDisplayOffsets(this.mDisplayId, 0, 0);
            }
        }
        this.mDurationLogger.setDeviceFolded(folded);
        this.mDurationLogger.logFocusedAppWithFoldState(folded, this.mFocusedApp);
        this.mFolded = java.lang.Boolean.valueOf(folded);
        int n = this.mListeners.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                this.mListeners.getBroadcastItem(i).onDisplayFoldChanged(this.mDisplayId, folded);
            } catch (android.os.RemoteException e) {
            }
        }
        this.mListeners.finishBroadcast();
    }

    void registerDisplayFoldListener(final android.view.IDisplayFoldListener listener) {
        this.mListeners.register(listener);
        if (this.mFolded == null) {
            return;
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.policy.DisplayFoldController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$registerDisplayFoldListener$1(listener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerDisplayFoldListener$1(android.view.IDisplayFoldListener listener) {
        try {
            listener.onDisplayFoldChanged(this.mDisplayId, this.mFolded.booleanValue());
        } catch (android.os.RemoteException e) {
        }
    }

    void unregisterDisplayFoldListener(android.view.IDisplayFoldListener listener) {
        this.mListeners.unregister(listener);
    }

    void setOverrideFoldedArea(android.graphics.Rect area) {
        this.mOverrideFoldedArea.set(area);
    }

    android.graphics.Rect getFoldedArea() {
        if (!this.mOverrideFoldedArea.isEmpty()) {
            return this.mOverrideFoldedArea;
        }
        return this.mFoldedArea;
    }

    void onDefaultDisplayFocusChanged(java.lang.String pkg) {
        this.mFocusedApp = pkg;
    }

    static com.android.server.policy.DisplayFoldController create(android.content.Context context, int displayId) {
        android.graphics.Rect foldedArea;
        com.android.server.wm.WindowManagerInternal windowManagerService = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
        android.hardware.display.DisplayManagerInternal displayService = (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class);
        java.lang.String configFoldedArea = context.getResources().getString(android.R.string.config_headlineFontFamily);
        if (configFoldedArea == null || configFoldedArea.isEmpty()) {
            foldedArea = new android.graphics.Rect();
        } else {
            foldedArea = android.graphics.Rect.unflattenFromString(configFoldedArea);
        }
        return new com.android.server.policy.DisplayFoldController(context, windowManagerService, displayService, displayId, foldedArea, com.android.server.DisplayThread.getHandler());
    }
}
