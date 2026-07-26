package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class AccessibilityController {
    private static final int OPLUS_MIRAGE_TV_DISPLAY_ID = 2020;
    private final com.android.server.wm.AccessibilityController.AccessibilityControllerInternalImpl mAccessibilityTracing;
    private final com.android.server.wm.AccessibilityWindowsPopulator mAccessibilityWindowsPopulator;
    private final com.android.server.wm.WindowManagerService mService;
    private static final java.lang.String TAG = com.android.server.wm.AccessibilityController.class.getSimpleName();
    private static final java.lang.Object STATIC_LOCK = new java.lang.Object();
    private static final android.graphics.Rect EMPTY_RECT = new android.graphics.Rect();
    private static final float[] sTempFloats = new float[9];
    private final android.util.SparseArray<com.android.server.wm.AccessibilityController.DisplayMagnifier> mDisplayMagnifiers = new android.util.SparseArray<>();
    private final android.util.SparseArray<com.android.server.wm.AccessibilityController.WindowsForAccessibilityObserver> mWindowsForAccessibilityObserver = new android.util.SparseArray<>();
    private android.util.SparseArray<android.os.IBinder> mFocusedWindow = new android.util.SparseArray<>();
    private int mFocusedDisplay = -1;
    private final android.util.SparseBooleanArray mIsImeVisibleArray = new android.util.SparseBooleanArray();
    private boolean mAllObserversInitialized = true;

    static com.android.server.wm.AccessibilityController.AccessibilityControllerInternalImpl getAccessibilityControllerInternal(com.android.server.wm.WindowManagerService service) {
        return com.android.server.wm.AccessibilityController.AccessibilityControllerInternalImpl.getInstance(service);
    }

    AccessibilityController(com.android.server.wm.WindowManagerService service) {
        this.mService = service;
        this.mAccessibilityTracing = getAccessibilityControllerInternal(service);
        this.mAccessibilityWindowsPopulator = new com.android.server.wm.AccessibilityWindowsPopulator(this.mService, this);
    }

    boolean setMagnificationCallbacks(int displayId, com.android.server.wm.WindowManagerInternal.MagnificationCallbacks callbacks) {
        android.view.Display display;
        if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".setMagnificationCallbacks", 2048L, "displayId=" + displayId + "; callbacks={" + callbacks + "}");
        }
        if (callbacks != null) {
            if (this.mDisplayMagnifiers.get(displayId) != null) {
                throw new java.lang.IllegalStateException("Magnification callbacks already set!");
            }
            com.android.server.wm.DisplayContent dc = this.mService.mRoot.getDisplayContent(displayId);
            if (dc == null || (display = dc.getDisplay()) == null || display.getType() == 4) {
                return false;
            }
            com.android.server.wm.AccessibilityController.DisplayMagnifier magnifier = new com.android.server.wm.AccessibilityController.DisplayMagnifier(this.mService, dc, display, callbacks);
            magnifier.notifyImeWindowVisibilityChanged(this.mIsImeVisibleArray.get(displayId, false));
            this.mDisplayMagnifiers.put(displayId, magnifier);
            return true;
        }
        com.android.server.wm.AccessibilityController.DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(displayId);
        if (displayMagnifier == null) {
            throw new java.lang.IllegalStateException("Magnification callbacks already cleared!");
        }
        displayMagnifier.destroy();
        this.mDisplayMagnifiers.remove(displayId);
        return true;
    }

    void setWindowsForAccessibilityCallback(int displayId, com.android.server.wm.WindowManagerInternal.WindowsForAccessibilityCallback callback) {
        if (this.mAccessibilityTracing.isTracingEnabled(1024L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".setWindowsForAccessibilityCallback", 1024L, "displayId=" + displayId + "; callback={" + callback + "}");
        }
        if (displayId == 2020) {
            android.util.Slog.d("WindowManager", "Skip setWindowsForAccessibilityCallback for mirage tv display");
            return;
        }
        if (callback != null) {
            if (this.mWindowsForAccessibilityObserver.get(displayId) != null) {
                java.lang.String errorMessage = "Windows for accessibility callback of display " + displayId + " already set!";
                android.util.Slog.e(TAG, errorMessage);
                if (android.os.Build.IS_DEBUGGABLE) {
                    throw new java.lang.IllegalStateException(errorMessage);
                }
                this.mWindowsForAccessibilityObserver.remove(displayId);
            }
            this.mAccessibilityWindowsPopulator.setWindowsNotification(true);
            com.android.server.wm.AccessibilityController.WindowsForAccessibilityObserver observer = new com.android.server.wm.AccessibilityController.WindowsForAccessibilityObserver(this.mService, displayId, callback, this.mAccessibilityWindowsPopulator);
            this.mWindowsForAccessibilityObserver.put(displayId, observer);
            this.mAllObserversInitialized &= observer.mInitialized;
            return;
        }
        com.android.server.wm.AccessibilityController.WindowsForAccessibilityObserver windowsForA11yObserver = this.mWindowsForAccessibilityObserver.get(displayId);
        if (windowsForA11yObserver == null) {
            java.lang.String errorMessage2 = "Windows for accessibility callback of display " + displayId + " already cleared!";
            android.util.Slog.e(TAG, errorMessage2);
            if (android.os.Build.IS_DEBUGGABLE) {
                throw new java.lang.IllegalStateException(errorMessage2);
            }
        }
        this.mWindowsForAccessibilityObserver.remove(displayId);
        if (this.mWindowsForAccessibilityObserver.size() <= 0) {
            this.mAccessibilityWindowsPopulator.setWindowsNotification(false);
        }
    }

    void performComputeChangedWindowsNot(int displayId, boolean forceSend) {
        if (this.mAccessibilityTracing.isTracingEnabled(1024L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".performComputeChangedWindowsNot", 1024L, "displayId=" + displayId + "; forceSend=" + forceSend);
        }
        com.android.server.wm.AccessibilityController.WindowsForAccessibilityObserver observer = null;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.AccessibilityController.WindowsForAccessibilityObserver windowsForA11yObserver = this.mWindowsForAccessibilityObserver.get(displayId);
                if (windowsForA11yObserver != null) {
                    observer = windowsForA11yObserver;
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        if (observer != null) {
            observer.performComputeChangedWindows(forceSend);
        }
    }

    void setMagnificationSpec(int displayId, android.view.MagnificationSpec spec) {
        if (this.mAccessibilityTracing.isTracingEnabled(3072L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".setMagnificationSpec", 3072L, "displayId=" + displayId + "; spec={" + spec + "}");
        }
        this.mAccessibilityWindowsPopulator.setMagnificationSpec(displayId, spec);
        com.android.server.wm.AccessibilityController.DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(displayId);
        if (displayMagnifier != null) {
            displayMagnifier.setMagnificationSpec(spec);
        }
        com.android.server.wm.AccessibilityController.WindowsForAccessibilityObserver windowsForA11yObserver = this.mWindowsForAccessibilityObserver.get(displayId);
        if (windowsForA11yObserver != null) {
            windowsForA11yObserver.scheduleComputeChangedWindows();
        }
    }

    void getMagnificationRegion(int displayId, android.graphics.Region outMagnificationRegion) {
        if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".getMagnificationRegion", 2048L, "displayId=" + displayId + "; outMagnificationRegion={" + outMagnificationRegion + "}");
        }
        com.android.server.wm.AccessibilityController.DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(displayId);
        if (displayMagnifier != null) {
            displayMagnifier.getMagnificationRegion(outMagnificationRegion);
        }
    }

    android.view.Surface forceShowMagnifierSurface(int displayId) {
        com.android.server.wm.AccessibilityController.DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(displayId);
        if (displayMagnifier != null) {
            displayMagnifier.mMagnifiedViewport.mWindow.setAlpha(255);
            return displayMagnifier.mMagnifiedViewport.mWindow.mSurface;
        }
        return null;
    }

    void onWindowLayersChanged(int displayId) {
        if (this.mAccessibilityTracing.isTracingEnabled(3072L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".onWindowLayersChanged", 3072L, "displayId=" + displayId);
        }
        com.android.server.wm.AccessibilityController.DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(displayId);
        if (displayMagnifier != null) {
            displayMagnifier.onWindowLayersChanged();
        }
        com.android.server.wm.AccessibilityController.WindowsForAccessibilityObserver windowsForA11yObserver = this.mWindowsForAccessibilityObserver.get(displayId);
        if (windowsForA11yObserver != null) {
            windowsForA11yObserver.scheduleComputeChangedWindows();
        }
    }

    void onDisplaySizeChanged(com.android.server.wm.DisplayContent displayContent) {
        if (this.mAccessibilityTracing.isTracingEnabled(3072L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".onRotationChanged", 3072L, "displayContent={" + displayContent + "}");
        }
        int displayId = displayContent.getDisplayId();
        com.android.server.wm.AccessibilityController.DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(displayId);
        if (displayMagnifier != null) {
            displayMagnifier.onDisplaySizeChanged(displayContent);
        }
    }

    void onAppWindowTransition(int displayId, int transition) {
        if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".onAppWindowTransition", 2048L, "displayId=" + displayId + "; transition=" + transition);
        }
        com.android.server.wm.AccessibilityController.DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(displayId);
        if (displayMagnifier != null) {
            displayMagnifier.onAppWindowTransition(displayId, transition);
        }
    }

    void onWMTransition(int displayId, int type, int flags) {
        if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".onWMTransition", 2048L, "displayId=" + displayId + "; type=" + type + "; flags=" + flags);
        }
        com.android.server.wm.AccessibilityController.DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(displayId);
        if (displayMagnifier != null) {
            displayMagnifier.onWMTransition(displayId, type, flags);
        }
    }

    void onWindowTransition(com.android.server.wm.WindowState windowState, int transition) {
        if (this.mAccessibilityTracing.isTracingEnabled(3072L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".onWindowTransition", 3072L, "windowState={" + windowState + "}; transition=" + transition);
        }
        int displayId = windowState.getDisplayId();
        com.android.server.wm.AccessibilityController.DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(displayId);
        if (displayMagnifier != null) {
            displayMagnifier.onWindowTransition(windowState, transition);
        }
    }

    void onWindowFocusChangedNot(int displayId) {
        if (this.mAccessibilityTracing.isTracingEnabled(1024L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".onWindowFocusChangedNot", 1024L, "displayId=" + displayId);
        }
        com.android.server.wm.AccessibilityController.WindowsForAccessibilityObserver observer = null;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.AccessibilityController.WindowsForAccessibilityObserver windowsForA11yObserver = this.mWindowsForAccessibilityObserver.get(displayId);
                if (windowsForA11yObserver != null) {
                    observer = windowsForA11yObserver;
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        if (observer != null) {
            observer.performComputeChangedWindows(false);
        }
        sendCallbackToUninitializedObserversIfNeeded();
    }

    private void sendCallbackToUninitializedObserversIfNeeded() throws java.lang.Throwable {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mAllObserversInitialized) {
                    return;
                }
                if (this.mService.mRoot.getTopFocusedDisplayContent().mCurrentFocus == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                java.util.List<com.android.server.wm.AccessibilityController.WindowsForAccessibilityObserver> unInitializedObservers = new java.util.ArrayList<>();
                for (int i = this.mWindowsForAccessibilityObserver.size() - 1; i >= 0; i--) {
                    com.android.server.wm.AccessibilityController.WindowsForAccessibilityObserver observer = this.mWindowsForAccessibilityObserver.valueAt(i);
                    if (!observer.mInitialized) {
                        unInitializedObservers.add(observer);
                    }
                }
                this.mAllObserversInitialized = true;
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                boolean areAllObserversInitialized = true;
                for (int i2 = unInitializedObservers.size() - 1; i2 >= 0; i2--) {
                    com.android.server.wm.AccessibilityController.WindowsForAccessibilityObserver observer2 = unInitializedObservers.get(i2);
                    observer2.performComputeChangedWindows(true);
                    areAllObserversInitialized &= observer2.mInitialized;
                }
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = this.mService.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock2) {
                    try {
                        this.mAllObserversInitialized &= areAllObserversInitialized;
                    } finally {
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } finally {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            }
        }
    }

    void onSomeWindowResizedOrMoved(int... displayIds) {
        onSomeWindowResizedOrMovedWithCallingUid(android.os.Binder.getCallingUid(), displayIds);
    }

    void onSomeWindowResizedOrMovedWithCallingUid(int callingUid, int... displayIds) {
        if (this.mAccessibilityTracing.isTracingEnabled(1024L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".onSomeWindowResizedOrMoved", 1024L, "displayIds={" + java.util.Arrays.toString(displayIds) + "}", "".getBytes(), callingUid);
        }
        for (int i : displayIds) {
            com.android.server.wm.AccessibilityController.WindowsForAccessibilityObserver windowsForA11yObserver = this.mWindowsForAccessibilityObserver.get(i);
            if (windowsForA11yObserver != null) {
                windowsForA11yObserver.scheduleComputeChangedWindows();
            }
        }
    }

    void recomputeMagnifiedRegionAndDrawMagnifiedRegionBorderIfNeeded(int displayId) {
        if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".recomputeMagnifiedRegionAndDrawMagnifiedRegionBorderIfNeeded", 2048L, "displayId=" + displayId);
        }
        com.android.server.wm.AccessibilityController.DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(displayId);
        if (displayMagnifier != null) {
            displayMagnifier.recomputeMagnifiedRegionAndDrawMagnifiedRegionBorderIfNeeded();
        }
    }

    public android.util.Pair<android.graphics.Matrix, android.view.MagnificationSpec> getWindowTransformationMatrixAndMagnificationSpec(android.os.IBinder token) {
        android.util.Pair<android.graphics.Matrix, android.view.MagnificationSpec> pair;
        android.view.MagnificationSpec otherMagnificationSpec;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                android.graphics.Matrix transformationMatrix = new android.graphics.Matrix();
                android.view.MagnificationSpec magnificationSpec = new android.view.MagnificationSpec();
                com.android.server.wm.WindowState windowState = this.mService.mWindowMap.get(token);
                if (windowState != null) {
                    windowState.getTransformationMatrix(new float[9], transformationMatrix);
                    if (hasCallbacks() && (otherMagnificationSpec = getMagnificationSpecForWindow(windowState)) != null && !otherMagnificationSpec.isNop()) {
                        magnificationSpec.setTo(otherMagnificationSpec);
                    }
                }
                pair = new android.util.Pair<>(transformationMatrix, magnificationSpec);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return pair;
    }

    android.view.MagnificationSpec getMagnificationSpecForWindow(com.android.server.wm.WindowState windowState) {
        if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".getMagnificationSpecForWindow", 2048L, "windowState={" + windowState + "}");
        }
        int displayId = windowState.getDisplayId();
        com.android.server.wm.AccessibilityController.DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(displayId);
        if (displayMagnifier != null) {
            return displayMagnifier.getMagnificationSpecForWindow(windowState);
        }
        return null;
    }

    boolean hasCallbacks() {
        if (this.mAccessibilityTracing.isTracingEnabled(3072L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".hasCallbacks", 3072L);
        }
        return this.mDisplayMagnifiers.size() > 0 || this.mWindowsForAccessibilityObserver.size() > 0;
    }

    void setFullscreenMagnificationActivated(int displayId, boolean activated) {
        if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".setFullscreenMagnificationActivated", 2048L, "displayId=" + displayId + "; activated=" + activated);
        }
        com.android.server.wm.AccessibilityController.DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(displayId);
        if (displayMagnifier != null) {
            displayMagnifier.setFullscreenMagnificationActivated(activated);
        }
    }

    void updateImeVisibilityIfNeeded(int displayId, boolean shown) {
        if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".updateImeVisibilityIfNeeded", 2048L, "displayId=" + displayId + ";shown=" + shown);
        }
        boolean isDisplayImeVisible = this.mIsImeVisibleArray.get(displayId, false);
        if (isDisplayImeVisible == shown) {
            return;
        }
        this.mIsImeVisibleArray.put(displayId, shown);
        com.android.server.wm.AccessibilityController.DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(displayId);
        if (displayMagnifier != null) {
            displayMagnifier.notifyImeWindowVisibilityChanged(shown);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void populateTransformationMatrix(com.android.server.wm.WindowState windowState, android.graphics.Matrix outMatrix) {
        windowState.getTransformationMatrix(sTempFloats, outMatrix);
    }

    void dump(final java.io.PrintWriter pw, final java.lang.String prefix) {
        com.android.internal.util.DumpUtils.dumpSparseArray(pw, prefix, this.mDisplayMagnifiers, "magnification display", new com.android.internal.util.DumpUtils.KeyDumper() { // from class: com.android.server.wm.AccessibilityController$$ExternalSyntheticLambda0
            public final void dump(int i, int i2) {
                pw.printf("%sDisplay #%d:", prefix + "  ", java.lang.Integer.valueOf(i2));
            }
        }, new com.android.internal.util.DumpUtils.ValueDumper() { // from class: com.android.server.wm.AccessibilityController$$ExternalSyntheticLambda1
            public final void dump(java.lang.Object obj) {
                ((com.android.server.wm.AccessibilityController.DisplayMagnifier) obj).dump(pw, "");
            }
        });
        com.android.internal.util.DumpUtils.dumpSparseArrayValues(pw, prefix, this.mWindowsForAccessibilityObserver, "windows for accessibility observer");
        this.mAccessibilityWindowsPopulator.dump(pw, prefix);
    }

    void onFocusChanged(com.android.server.wm.InputTarget lastTarget, com.android.server.wm.InputTarget newTarget) {
        if (lastTarget != null) {
            this.mFocusedWindow.remove(lastTarget.getDisplayId());
            com.android.server.wm.AccessibilityController.DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(lastTarget.getDisplayId());
            if (displayMagnifier != null) {
                displayMagnifier.onFocusLost(lastTarget);
            }
        }
        if (newTarget != null) {
            int displayId = newTarget.getDisplayId();
            android.os.IBinder clientBinder = newTarget.getWindowToken();
            this.mFocusedWindow.put(displayId, clientBinder);
        }
    }

    public void onDisplayRemoved(int displayId) {
        this.mIsImeVisibleArray.delete(displayId);
        this.mFocusedWindow.remove(displayId);
    }

    public void setFocusedDisplay(int focusedDisplayId) {
        this.mFocusedDisplay = focusedDisplayId;
    }

    android.os.IBinder getFocusedWindowToken() {
        return this.mFocusedWindow.get(this.mFocusedDisplay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class DisplayMagnifier {
        private static final boolean DEBUG_DISPLAY_SIZE = false;
        private static final boolean DEBUG_LAYERS = false;
        private static final boolean DEBUG_RECTANGLE_REQUESTED = false;
        private static final boolean DEBUG_VIEWPORT_WINDOW = false;
        private static final boolean DEBUG_WINDOW_TRANSITIONS = false;
        private static final java.lang.String LOG_TAG = "WindowManager";
        private final com.android.server.wm.AccessibilityController.AccessibilityControllerInternalImpl mAccessibilityTracing;
        private final com.android.server.wm.WindowManagerInternal.MagnificationCallbacks mCallbacks;
        private final android.graphics.Path mCircularPath;
        private final android.view.Display mDisplay;
        private final com.android.server.wm.DisplayContent mDisplayContent;
        private final android.content.Context mDisplayContext;
        private final android.os.Handler mHandler;
        private final long mLongAnimationDuration;
        private final com.android.server.wm.AccessibilityController.DisplayMagnifier.MagnifiedViewport mMagnifiedViewport;
        private final com.android.server.wm.WindowManagerService mService;
        private final com.android.server.wm.AccessibilityController.DisplayMagnifier.UserContextChangedNotifier mUserContextChangedNotifier;
        private final android.graphics.Rect mTempRect1 = new android.graphics.Rect();
        private final android.graphics.Rect mTempRect2 = new android.graphics.Rect();
        private final android.graphics.Region mTempRegion1 = new android.graphics.Region();
        private final android.graphics.Region mTempRegion2 = new android.graphics.Region();
        private final android.graphics.Region mTempRegion3 = new android.graphics.Region();
        private final android.graphics.Region mTempRegion4 = new android.graphics.Region();
        private boolean mIsFullscreenMagnificationActivated = false;
        private final android.graphics.Region mMagnificationRegion = new android.graphics.Region();
        private final android.graphics.Region mOldMagnificationRegion = new android.graphics.Region();
        private final android.view.MagnificationSpec mMagnificationSpec = new android.view.MagnificationSpec();
        private int mTempLayer = 0;
        private final android.graphics.Point mScreenSize = new android.graphics.Point();
        private final android.util.SparseArray<com.android.server.wm.WindowState> mTempWindowStates = new android.util.SparseArray<>();
        private final android.graphics.RectF mTempRectF = new android.graphics.RectF();
        private final android.graphics.Matrix mTempMatrix = new android.graphics.Matrix();

        DisplayMagnifier(com.android.server.wm.WindowManagerService windowManagerService, com.android.server.wm.DisplayContent displayContent, android.view.Display display, com.android.server.wm.WindowManagerInternal.MagnificationCallbacks callbacks) {
            this.mDisplayContext = windowManagerService.mContext.createDisplayContext(display);
            this.mService = windowManagerService;
            this.mCallbacks = callbacks;
            this.mDisplayContent = displayContent;
            this.mDisplay = display;
            this.mHandler = new com.android.server.wm.AccessibilityController.DisplayMagnifier.MyHandler(this.mService.mH.getLooper());
            this.mUserContextChangedNotifier = new com.android.server.wm.AccessibilityController.DisplayMagnifier.UserContextChangedNotifier(this.mHandler);
            this.mMagnifiedViewport = com.android.window.flags.Flags.alwaysDrawMagnificationFullscreenBorder() ? null : new com.android.server.wm.AccessibilityController.DisplayMagnifier.MagnifiedViewport();
            this.mAccessibilityTracing = com.android.server.wm.AccessibilityController.getAccessibilityControllerInternal(this.mService);
            this.mLongAnimationDuration = this.mDisplayContext.getResources().getInteger(android.R.integer.config_longAnimTime);
            if (this.mDisplayContext.getResources().getConfiguration().isScreenRound()) {
                this.mCircularPath = new android.graphics.Path();
                getDisplaySizeLocked(this.mScreenSize);
                int centerXY = this.mScreenSize.x / 2;
                this.mCircularPath.addCircle(centerXY, centerXY, centerXY, android.graphics.Path.Direction.CW);
            } else {
                this.mCircularPath = null;
            }
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.DisplayMagnifier.constructor", 2048L, "windowManagerService={" + windowManagerService + "}; displayContent={" + displayContent + "}; display={" + display + "}; callbacks={" + callbacks + "}");
            }
            recomputeBounds();
        }

        void setMagnificationSpec(android.view.MagnificationSpec spec) {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.setMagnificationSpec", 2048L, "spec={" + spec + "}");
            }
            updateMagnificationSpec(spec);
            recomputeBounds();
            this.mService.applyMagnificationSpecLocked(this.mDisplay.getDisplayId(), spec);
            this.mService.scheduleAnimationLocked();
        }

        void updateMagnificationSpec(android.view.MagnificationSpec spec) {
            if (spec != null) {
                this.mMagnificationSpec.initialize(spec.scale, spec.offsetX, spec.offsetY);
            } else {
                this.mMagnificationSpec.clear();
            }
            if (!com.android.window.flags.Flags.alwaysDrawMagnificationFullscreenBorder()) {
                this.mMagnifiedViewport.setShowMagnifiedBorderIfNeeded();
            }
        }

        void setFullscreenMagnificationActivated(boolean activated) {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.setFullscreenMagnificationActivated", 2048L, "activated=" + activated);
            }
            this.mIsFullscreenMagnificationActivated = activated;
            if (!com.android.window.flags.Flags.alwaysDrawMagnificationFullscreenBorder()) {
                this.mMagnifiedViewport.setMagnifiedRegionBorderShown(activated, true);
                this.mMagnifiedViewport.showMagnificationBoundsIfNeeded();
            }
        }

        boolean isFullscreenMagnificationActivated() {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.isFullscreenMagnificationActivated", 2048L);
            }
            return this.mIsFullscreenMagnificationActivated;
        }

        void onWindowLayersChanged() {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.onWindowLayersChanged", 2048L);
            }
            recomputeBounds();
            this.mService.scheduleAnimationLocked();
        }

        void onDisplaySizeChanged(com.android.server.wm.DisplayContent displayContent) {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.onDisplaySizeChanged", 2048L, "displayContent={" + displayContent + "}");
            }
            recomputeBounds();
            if (!com.android.window.flags.Flags.alwaysDrawMagnificationFullscreenBorder()) {
                this.mMagnifiedViewport.onDisplaySizeChanged();
            }
            this.mHandler.sendEmptyMessage(4);
        }

        void onAppWindowTransition(int displayId, int transition) {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.onAppWindowTransition", 2048L, "displayId=" + displayId + "; transition=" + transition);
            }
            boolean isMagnifierActivated = isFullscreenMagnificationActivated();
            if (!isMagnifierActivated) {
            }
            switch (transition) {
                case 6:
                case 8:
                case 10:
                case 12:
                case 13:
                case 14:
                case 28:
                    this.mUserContextChangedNotifier.onAppWindowTransition(transition);
                    break;
            }
        }

        void onWMTransition(int displayId, int type, int flags) {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.onWMTransition", 2048L, "displayId=" + displayId + "; type=" + type + "; flags=" + flags);
            }
            boolean isMagnifierActivated = isFullscreenMagnificationActivated();
            if (!isMagnifierActivated) {
            }
            switch (type) {
                case 1:
                case 2:
                case 3:
                case 4:
                    this.mUserContextChangedNotifier.onWMTransition(type, flags);
                    break;
            }
        }

        void onWindowTransition(com.android.server.wm.WindowState windowState, int transition) {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.onWindowTransition", 2048L, "windowState={" + windowState + "}; transition=" + transition);
            }
            boolean isMagnifierActivated = isFullscreenMagnificationActivated();
            if (!isMagnifierActivated || !windowState.shouldMagnify()) {
                return;
            }
            this.mUserContextChangedNotifier.onWindowTransition(windowState, transition);
            int type = windowState.mAttrs.type;
            switch (transition) {
                case 1:
                case 3:
                    switch (type) {
                        case 2:
                        case 4:
                        case 1000:
                        case 1001:
                        case 1002:
                        case 1003:
                        case 1005:
                        case 2001:
                        case com.android.server.camera.ICameraServiceProxyExt.MSG_FLOAT_WINDOW_SHOW /* 2002 */:
                        case 2003:
                        case 2005:
                        case 2006:
                        case 2007:
                        case 2008:
                        case 2009:
                        case 2010:
                        case 2020:
                        case 2024:
                        case 2035:
                        case 2038:
                            android.graphics.Rect magnifiedRegionBounds = this.mTempRect2;
                            getMagnifiedFrameInContentCoords(magnifiedRegionBounds);
                            android.graphics.Rect touchableRegionBounds = this.mTempRect1;
                            windowState.getTouchableRegion(this.mTempRegion1);
                            this.mTempRegion1.getBounds(touchableRegionBounds);
                            if (!magnifiedRegionBounds.intersect(touchableRegionBounds)) {
                                this.mCallbacks.onRectangleOnScreenRequested(touchableRegionBounds.left, touchableRegionBounds.top, touchableRegionBounds.right, touchableRegionBounds.bottom);
                            }
                            break;
                    }
                    break;
            }
        }

        void onFocusLost(com.android.server.wm.InputTarget target) {
            boolean isMagnifierActivated = isFullscreenMagnificationActivated();
            if (!isMagnifierActivated) {
                return;
            }
            this.mUserContextChangedNotifier.onFocusLost(target);
        }

        void getMagnifiedFrameInContentCoords(android.graphics.Rect rect) {
            this.mMagnificationRegion.getBounds(rect);
            rect.offset((int) (-this.mMagnificationSpec.offsetX), (int) (-this.mMagnificationSpec.offsetY));
            rect.scale(1.0f / this.mMagnificationSpec.scale);
        }

        void notifyImeWindowVisibilityChanged(boolean z) {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.notifyImeWindowVisibilityChanged", 2048L, "shown=" + z);
            }
            this.mHandler.obtainMessage(6, z ? 1 : 0, 0).sendToTarget();
        }

        android.view.MagnificationSpec getMagnificationSpecForWindow(com.android.server.wm.WindowState windowState) {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.getMagnificationSpecForWindow", 2048L, "windowState={" + windowState + "}");
            }
            if (this.mMagnificationSpec != null && !this.mMagnificationSpec.isNop() && !windowState.shouldMagnify()) {
                return null;
            }
            return this.mMagnificationSpec;
        }

        void getMagnificationRegion(android.graphics.Region outMagnificationRegion) {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.getMagnificationRegion", 2048L, "outMagnificationRegion={" + outMagnificationRegion + "}");
            }
            recomputeBounds();
            outMagnificationRegion.set(this.mMagnificationRegion);
        }

        boolean isMagnifying() {
            return this.mMagnificationSpec.scale > 1.0f;
        }

        void destroy() {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.destroy", 2048L);
            }
            if (!com.android.window.flags.Flags.alwaysDrawMagnificationFullscreenBorder()) {
                this.mMagnifiedViewport.destroyWindow();
            }
        }

        void recomputeMagnifiedRegionAndDrawMagnifiedRegionBorderIfNeeded() {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.recomputeMagnifiedRegionAndDrawMagnifiedRegionBorderIfNeeded", 2048L);
            }
            recomputeBounds();
            if (!com.android.window.flags.Flags.alwaysDrawMagnificationFullscreenBorder()) {
                this.mMagnifiedViewport.drawWindowIfNeeded();
            }
        }

        void recomputeBounds() {
            getDisplaySizeLocked(this.mScreenSize);
            int screenWidth = this.mScreenSize.x;
            int screenHeight = this.mScreenSize.y;
            this.mMagnificationRegion.set(0, 0, 0, 0);
            android.graphics.Region availableBounds = this.mTempRegion1;
            availableBounds.set(0, 0, screenWidth, screenHeight);
            if (this.mCircularPath != null) {
                availableBounds.setPath(this.mCircularPath, availableBounds);
            }
            android.graphics.Region nonMagnifiedBounds = this.mTempRegion4;
            nonMagnifiedBounds.set(0, 0, 0, 0);
            android.util.SparseArray<com.android.server.wm.WindowState> visibleWindows = this.mTempWindowStates;
            visibleWindows.clear();
            populateWindowsOnScreen(visibleWindows);
            int visibleWindowCount = visibleWindows.size();
            for (int i = visibleWindowCount - 1; i >= 0; i--) {
                com.android.server.wm.WindowState windowState = visibleWindows.valueAt(i);
                int windowType = windowState.mAttrs.type;
                if (!isExcludedWindowType(windowType) && (windowState.mAttrs.privateFlags & 2097152) == 0 && (windowState.mAttrs.privateFlags & 1048576) == 0) {
                    android.graphics.Matrix matrix = this.mTempMatrix;
                    com.android.server.wm.AccessibilityController.populateTransformationMatrix(windowState, matrix);
                    android.graphics.Region touchableRegion = this.mTempRegion3;
                    windowState.getTouchableRegion(touchableRegion);
                    android.graphics.Region windowBounds = this.mTempRegion2;
                    touchableRegion.translate(-windowState.getFrame().left, -windowState.getFrame().top);
                    applyMatrixToRegion(matrix, touchableRegion);
                    windowBounds.set(touchableRegion);
                    android.graphics.Region portionOfWindowAlreadyAccountedFor = this.mTempRegion3;
                    portionOfWindowAlreadyAccountedFor.set(this.mMagnificationRegion);
                    portionOfWindowAlreadyAccountedFor.op(nonMagnifiedBounds, android.graphics.Region.Op.UNION);
                    windowBounds.op(portionOfWindowAlreadyAccountedFor, android.graphics.Region.Op.DIFFERENCE);
                    if (windowState.shouldMagnify()) {
                        this.mMagnificationRegion.op(windowBounds, android.graphics.Region.Op.UNION);
                        this.mMagnificationRegion.op(availableBounds, android.graphics.Region.Op.INTERSECT);
                    } else {
                        nonMagnifiedBounds.op(windowBounds, android.graphics.Region.Op.UNION);
                        availableBounds.op(windowBounds, android.graphics.Region.Op.DIFFERENCE);
                    }
                    if (com.android.server.wm.AccessibilityController.isUntouchableNavigationBar(windowState, this.mTempRegion3)) {
                        android.graphics.Rect navBarInsets = com.android.server.wm.AccessibilityController.getSystemBarInsetsFrame(windowState);
                        nonMagnifiedBounds.op(navBarInsets, android.graphics.Region.Op.UNION);
                        availableBounds.op(navBarInsets, android.graphics.Region.Op.DIFFERENCE);
                    }
                    if (windowState.areAppWindowBoundsLetterboxed()) {
                        android.graphics.Region letterboxBounds = getLetterboxBounds(windowState);
                        nonMagnifiedBounds.op(letterboxBounds, android.graphics.Region.Op.UNION);
                        availableBounds.op(letterboxBounds, android.graphics.Region.Op.DIFFERENCE);
                    }
                    android.graphics.Region accountedBounds = this.mTempRegion2;
                    accountedBounds.set(this.mMagnificationRegion);
                    accountedBounds.op(nonMagnifiedBounds, android.graphics.Region.Op.UNION);
                    accountedBounds.op(0, 0, screenWidth, screenHeight, android.graphics.Region.Op.INTERSECT);
                    if (accountedBounds.isRect()) {
                        android.graphics.Rect accountedFrame = this.mTempRect1;
                        accountedBounds.getBounds(accountedFrame);
                        if (accountedFrame.width() == screenWidth && accountedFrame.height() == screenHeight) {
                            break;
                        }
                    }
                }
            }
            visibleWindows.clear();
            if (!com.android.window.flags.Flags.alwaysDrawMagnificationFullscreenBorder()) {
                this.mMagnifiedViewport.intersectWithDrawBorderInset(screenWidth, screenHeight);
            }
            boolean magnifiedChanged = !this.mOldMagnificationRegion.equals(this.mMagnificationRegion);
            if (magnifiedChanged) {
                if (!com.android.window.flags.Flags.alwaysDrawMagnificationFullscreenBorder()) {
                    this.mMagnifiedViewport.updateBorderDrawingStatus(screenWidth, screenHeight);
                }
                this.mOldMagnificationRegion.set(this.mMagnificationRegion);
                com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
                args.arg1 = android.graphics.Region.obtain(this.mMagnificationRegion);
                this.mHandler.obtainMessage(1, args).sendToTarget();
            }
        }

        private android.graphics.Region getLetterboxBounds(com.android.server.wm.WindowState windowState) {
            com.android.server.wm.ActivityRecord appToken = windowState.mActivityRecord;
            if (appToken == null) {
                return new android.graphics.Region();
            }
            android.graphics.Rect boundsWithoutLetterbox = windowState.getBounds();
            android.graphics.Rect letterboxInsets = appToken.getLetterboxInsets();
            android.graphics.Rect boundsIncludingLetterbox = android.graphics.Rect.copyOrNull(boundsWithoutLetterbox);
            boundsIncludingLetterbox.inset(android.graphics.Insets.subtract(android.graphics.Insets.NONE, android.graphics.Insets.of(letterboxInsets)));
            android.graphics.Region letterboxBounds = new android.graphics.Region();
            letterboxBounds.set(boundsIncludingLetterbox);
            letterboxBounds.op(boundsWithoutLetterbox, android.graphics.Region.Op.DIFFERENCE);
            return letterboxBounds;
        }

        private boolean isExcludedWindowType(int windowType) {
            return windowType == 2027 || windowType == 2039;
        }

        private void applyMatrixToRegion(android.graphics.Matrix matrix, android.graphics.Region region) {
            float[] transformArray = com.android.server.wm.AccessibilityController.sTempFloats;
            matrix.getValues(transformArray);
            region.scale(transformArray[0]);
            region.translate((int) transformArray[2], (int) transformArray[5]);
        }

        private void populateWindowsOnScreen(final android.util.SparseArray<com.android.server.wm.WindowState> outWindows) {
            this.mTempLayer = 0;
            this.mDisplayContent.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.AccessibilityController$DisplayMagnifier$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$populateWindowsOnScreen$0(outWindows, (com.android.server.wm.WindowState) obj);
                }
            }, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$populateWindowsOnScreen$0(android.util.SparseArray outWindows, com.android.server.wm.WindowState w) {
            if (w.isOnScreen() && w.isVisible() && w.mAttrs.alpha != 0.0f) {
                this.mTempLayer++;
                outWindows.put(this.mTempLayer, w);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void getDisplaySizeLocked(android.graphics.Point outSize) {
            android.graphics.Rect bounds = this.mDisplayContent.getConfiguration().windowConfiguration.getBounds();
            outSize.set(bounds.width(), bounds.height());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            if (!com.android.window.flags.Flags.alwaysDrawMagnificationFullscreenBorder()) {
                this.mMagnifiedViewport.dump(pw, prefix);
            }
        }

        private final class MagnifiedViewport {
            private final float mBorderWidth;
            private final int mDrawBorderInset;
            private boolean mFullRedrawNeeded;
            private final int mHalfBorderWidth;
            private final com.android.server.wm.AccessibilityController.DisplayMagnifier.MagnifiedViewport.ViewportWindow mWindow;

            MagnifiedViewport() {
                this.mBorderWidth = com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mDisplayContext.getResources().getDimension(android.R.dimen.accessibility_autoclick_type_panel_divider_height);
                this.mHalfBorderWidth = (int) java.lang.Math.ceil(this.mBorderWidth / 2.0f);
                this.mDrawBorderInset = ((int) this.mBorderWidth) / 2;
                this.mWindow = new com.android.server.wm.AccessibilityController.DisplayMagnifier.MagnifiedViewport.ViewportWindow(com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mDisplayContext);
            }

            void updateBorderDrawingStatus(int screenWidth, int screenHeight) {
                this.mWindow.setBounds(com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mMagnificationRegion);
                android.graphics.Rect dirtyRect = com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mTempRect1;
                if (this.mFullRedrawNeeded) {
                    this.mFullRedrawNeeded = false;
                    dirtyRect.set(this.mDrawBorderInset, this.mDrawBorderInset, screenWidth - this.mDrawBorderInset, screenHeight - this.mDrawBorderInset);
                    this.mWindow.invalidate(dirtyRect);
                } else {
                    android.graphics.Region dirtyRegion = com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mTempRegion3;
                    dirtyRegion.set(com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mMagnificationRegion);
                    dirtyRegion.op(com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mOldMagnificationRegion, android.graphics.Region.Op.XOR);
                    dirtyRegion.getBounds(dirtyRect);
                    this.mWindow.invalidate(dirtyRect);
                }
            }

            void setShowMagnifiedBorderIfNeeded() {
                if (!com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mHandler.hasMessages(5)) {
                    setMagnifiedRegionBorderShown(com.android.server.wm.AccessibilityController.DisplayMagnifier.this.isFullscreenMagnificationActivated(), true);
                }
            }

            void showMagnificationBoundsIfNeeded() {
                if (com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                    com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mAccessibilityTracing.logTrace("WindowManager.showMagnificationBoundsIfNeeded", 2048L);
                }
                com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mHandler.obtainMessage(5).sendToTarget();
            }

            void intersectWithDrawBorderInset(int screenWidth, int screenHeight) {
                com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mMagnificationRegion.op(this.mDrawBorderInset, this.mDrawBorderInset, screenWidth - this.mDrawBorderInset, screenHeight - this.mDrawBorderInset, android.graphics.Region.Op.INTERSECT);
            }

            void onDisplaySizeChanged() {
                if (com.android.server.wm.AccessibilityController.DisplayMagnifier.this.isFullscreenMagnificationActivated()) {
                    setMagnifiedRegionBorderShown(false, false);
                    long delay = (long) (com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mLongAnimationDuration * com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mService.getWindowAnimationScaleLocked());
                    android.os.Message message = com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mHandler.obtainMessage(5);
                    com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mHandler.sendMessageDelayed(message, delay);
                }
                this.mWindow.updateSize();
            }

            void setMagnifiedRegionBorderShown(boolean shown, boolean animate) {
                if (this.mWindow.setShown(shown, animate)) {
                    this.mFullRedrawNeeded = true;
                    com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mOldMagnificationRegion.set(0, 0, 0, 0);
                }
            }

            void drawWindowIfNeeded() {
                this.mWindow.postDrawIfNeeded();
            }

            void destroyWindow() {
                this.mWindow.releaseSurface();
            }

            void dump(java.io.PrintWriter pw, java.lang.String prefix) {
                this.mWindow.dump(pw, prefix);
            }

            private final class ViewportWindow implements java.lang.Runnable {
                private static final java.lang.String SURFACE_TITLE = "Magnification Overlay";
                private int mAlpha;
                private final com.android.server.wm.AccessibilityController.DisplayMagnifier.MagnifiedViewport.ViewportWindow.AnimationController mAnimationController;
                private final android.graphics.BLASTBufferQueue mBlastBufferQueue;
                private volatile boolean mInvalidated;
                private boolean mLastSurfaceShown;
                private int mPreviousAlpha;
                private boolean mShown;
                private final android.view.Surface mSurface;
                private final android.view.SurfaceControl mSurfaceControl;
                private final android.view.SurfaceControl.Transaction mTransaction;
                private final android.graphics.Region mBounds = new android.graphics.Region();
                private final android.graphics.Rect mDirtyRect = new android.graphics.Rect();
                private final android.graphics.Paint mPaint = new android.graphics.Paint();

                ViewportWindow(android.content.Context context) {
                    android.view.SurfaceControl surfaceControl = null;
                    try {
                        surfaceControl = com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mDisplayContent.makeOverlay().setName(SURFACE_TITLE).setBLASTLayer().setFormat(-3).setCallsite("ViewportWindow").build();
                    } catch (android.view.Surface.OutOfResourcesException e) {
                    }
                    this.mSurfaceControl = surfaceControl;
                    com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mDisplay.getRealSize(com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mScreenSize);
                    this.mBlastBufferQueue = new android.graphics.BLASTBufferQueue(SURFACE_TITLE, this.mSurfaceControl, com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mScreenSize.x, com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mScreenSize.y, 1);
                    android.view.SurfaceControl.Transaction t = com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mService.mTransactionFactory.get();
                    int layer = com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mService.mPolicy.getWindowLayerFromTypeLw(2027) * 10000;
                    t.setLayer(this.mSurfaceControl, layer).setPosition(this.mSurfaceControl, 0.0f, 0.0f);
                    com.android.server.wm.InputMonitor.setTrustedOverlayInputInfo(this.mSurfaceControl, t, com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mDisplayContent.getDisplayId(), SURFACE_TITLE);
                    t.apply();
                    this.mTransaction = t;
                    this.mSurface = this.mBlastBufferQueue.createSurface();
                    this.mAnimationController = new com.android.server.wm.AccessibilityController.DisplayMagnifier.MagnifiedViewport.ViewportWindow.AnimationController(context, com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mService.mH.getLooper());
                    android.util.TypedValue typedValue = new android.util.TypedValue();
                    context.getTheme().resolveAttribute(android.R.attr.colorActivatedHighlight, typedValue, true);
                    int borderColor = context.getColor(typedValue.resourceId);
                    this.mPaint.setStyle(android.graphics.Paint.Style.STROKE);
                    this.mPaint.setStrokeWidth(com.android.server.wm.AccessibilityController.DisplayMagnifier.MagnifiedViewport.this.mBorderWidth);
                    this.mPaint.setColor(borderColor);
                    this.mInvalidated = true;
                }

                boolean setShown(boolean shown, boolean animate) {
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mService.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            if (this.mShown == shown) {
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                return false;
                            }
                            this.mShown = shown;
                            this.mAnimationController.onFrameShownStateChanged(shown, animate);
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return shown;
                        } catch (java.lang.Throwable th) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                }

                int getAlpha() {
                    int i;
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mService.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            i = this.mAlpha;
                        } catch (java.lang.Throwable th) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return i;
                }

                void setAlpha(int alpha) {
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mService.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            if (this.mAlpha == alpha) {
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                return;
                            }
                            this.mAlpha = alpha;
                            invalidate(null);
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        } catch (java.lang.Throwable th) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                }

                void setBounds(android.graphics.Region bounds) {
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mService.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            if (this.mBounds.equals(bounds)) {
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                return;
                            }
                            this.mBounds.set(bounds);
                            invalidate(this.mDirtyRect);
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        } catch (java.lang.Throwable th) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                }

                void updateSize() {
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mService.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            com.android.server.wm.AccessibilityController.DisplayMagnifier.this.getDisplaySizeLocked(com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mScreenSize);
                            this.mBlastBufferQueue.update(this.mSurfaceControl, com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mScreenSize.x, com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mScreenSize.y, 1);
                            invalidate(this.mDirtyRect);
                        } catch (java.lang.Throwable th) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                }

                void invalidate(android.graphics.Rect dirtyRect) {
                    if (dirtyRect != null) {
                        this.mDirtyRect.set(dirtyRect);
                    } else {
                        this.mDirtyRect.setEmpty();
                    }
                    this.mInvalidated = true;
                    com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mService.scheduleAnimationLocked();
                }

                void postDrawIfNeeded() {
                    if (this.mInvalidated) {
                        com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mService.mAnimationHandler.post(this);
                    }
                }

                @Override // java.lang.Runnable
                public void run() {
                    drawOrRemoveIfNeeded();
                }

                private void drawOrRemoveIfNeeded() {
                    android.graphics.Rect drawingRect = null;
                    android.graphics.Region drawingBounds = null;
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mService.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            if (this.mBlastBufferQueue.mNativeObject == 0) {
                                if (this.mSurface.isValid()) {
                                    this.mTransaction.remove(this.mSurfaceControl).apply();
                                    this.mSurface.release();
                                }
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                return;
                            }
                            if (!this.mInvalidated) {
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                return;
                            }
                            this.mInvalidated = false;
                            int alpha = this.mAlpha;
                            boolean redrawBounds = this.mAlpha > 0 || this.mPreviousAlpha > 0;
                            if (redrawBounds) {
                                drawingBounds = new android.graphics.Region(this.mBounds);
                                if (this.mDirtyRect.isEmpty()) {
                                    this.mBounds.getBounds(this.mDirtyRect);
                                }
                                this.mDirtyRect.inset(-com.android.server.wm.AccessibilityController.DisplayMagnifier.MagnifiedViewport.this.mHalfBorderWidth, -com.android.server.wm.AccessibilityController.DisplayMagnifier.MagnifiedViewport.this.mHalfBorderWidth);
                                drawingRect = new android.graphics.Rect(this.mDirtyRect);
                            }
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            if (redrawBounds) {
                                android.graphics.Canvas canvas = null;
                                try {
                                    canvas = this.mSurface.lockCanvas(drawingRect);
                                } catch (android.view.Surface.OutOfResourcesException | java.lang.IllegalArgumentException e) {
                                }
                                if (canvas != null) {
                                    canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
                                    this.mPaint.setAlpha(alpha);
                                    canvas.drawPath(drawingBounds.getBoundaryPath(), this.mPaint);
                                    this.mSurface.unlockCanvasAndPost(canvas);
                                    this.mPreviousAlpha = alpha;
                                } else {
                                    return;
                                }
                            }
                            boolean showSurface = alpha > 0;
                            if (showSurface && !this.mLastSurfaceShown) {
                                this.mTransaction.show(this.mSurfaceControl).apply();
                                this.mLastSurfaceShown = true;
                            } else if (!showSurface && this.mLastSurfaceShown) {
                                this.mTransaction.hide(this.mSurfaceControl).apply();
                                this.mLastSurfaceShown = false;
                            }
                        } catch (java.lang.Throwable th) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                }

                void releaseSurface() {
                    this.mBlastBufferQueue.destroy();
                    com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mService.mAnimationHandler.post(this);
                }

                void dump(java.io.PrintWriter pw, java.lang.String prefix) {
                    pw.println(prefix + " mBounds= " + this.mBounds + " mDirtyRect= " + this.mDirtyRect + " mWidth= " + com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mScreenSize.x + " mHeight= " + com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mScreenSize.y);
                }

                private final class AnimationController extends android.os.Handler {
                    private static final int MAX_ALPHA = 255;
                    private static final int MIN_ALPHA = 0;
                    private static final int MSG_FRAME_SHOWN_STATE_CHANGED = 1;
                    private static final java.lang.String PROPERTY_NAME_ALPHA = "alpha";
                    private final android.animation.ValueAnimator mShowHideFrameAnimator;

                    AnimationController(android.content.Context context, android.os.Looper looper) {
                        super(looper);
                        this.mShowHideFrameAnimator = android.animation.ObjectAnimator.ofInt(com.android.server.wm.AccessibilityController.DisplayMagnifier.MagnifiedViewport.ViewportWindow.this, PROPERTY_NAME_ALPHA, 0, 255);
                        android.view.animation.Interpolator interpolator = new android.view.animation.DecelerateInterpolator(2.5f);
                        long longAnimationDuration = context.getResources().getInteger(android.R.integer.config_longAnimTime);
                        this.mShowHideFrameAnimator.setInterpolator(interpolator);
                        this.mShowHideFrameAnimator.setDuration(longAnimationDuration);
                    }

                    void onFrameShownStateChanged(boolean z, boolean z2) {
                        obtainMessage(1, z ? 1 : 0, z2 ? 1 : 0).sendToTarget();
                    }

                    @Override // android.os.Handler
                    public void handleMessage(android.os.Message message) {
                        switch (message.what) {
                            case 1:
                                boolean shown = message.arg1 == 1;
                                boolean animate = message.arg2 == 1;
                                if (animate) {
                                    if (this.mShowHideFrameAnimator.isRunning()) {
                                        this.mShowHideFrameAnimator.reverse();
                                    } else if (shown) {
                                        this.mShowHideFrameAnimator.start();
                                    } else {
                                        this.mShowHideFrameAnimator.reverse();
                                    }
                                } else {
                                    this.mShowHideFrameAnimator.cancel();
                                    if (shown) {
                                        com.android.server.wm.AccessibilityController.DisplayMagnifier.MagnifiedViewport.ViewportWindow.this.setAlpha(255);
                                    } else {
                                        com.android.server.wm.AccessibilityController.DisplayMagnifier.MagnifiedViewport.ViewportWindow.this.setAlpha(0);
                                    }
                                }
                                break;
                        }
                    }
                }
            }
        }

        private class MyHandler extends android.os.Handler {
            public static final int MESSAGE_NOTIFY_DISPLAY_SIZE_CHANGED = 4;
            public static final int MESSAGE_NOTIFY_IME_WINDOW_VISIBILITY_CHANGED = 6;
            public static final int MESSAGE_NOTIFY_MAGNIFICATION_REGION_CHANGED = 1;
            public static final int MESSAGE_NOTIFY_USER_CONTEXT_CHANGED = 3;
            public static final int MESSAGE_SHOW_MAGNIFIED_REGION_BOUNDS_IF_NEEDED = 5;

            MyHandler(android.os.Looper looper) {
                super(looper);
            }

            @Override // android.os.Handler
            public void handleMessage(android.os.Message message) {
                switch (message.what) {
                    case 1:
                        com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) message.obj;
                        android.graphics.Region magnifiedBounds = (android.graphics.Region) args.arg1;
                        com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mCallbacks.onMagnificationRegionChanged(magnifiedBounds);
                        magnifiedBounds.recycle();
                        return;
                    case 2:
                    default:
                        return;
                    case 3:
                        com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mCallbacks.onUserContextChanged();
                        return;
                    case 4:
                        com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mCallbacks.onDisplaySizeChanged();
                        return;
                    case 5:
                        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mService.mGlobalLock;
                        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                        synchronized (windowManagerGlobalLock) {
                            try {
                                if (com.android.server.wm.AccessibilityController.DisplayMagnifier.this.isFullscreenMagnificationActivated()) {
                                    if (!com.android.window.flags.Flags.alwaysDrawMagnificationFullscreenBorder()) {
                                        com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mMagnifiedViewport.setMagnifiedRegionBorderShown(true, true);
                                    }
                                    com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mService.scheduleAnimationLocked();
                                }
                            } catch (java.lang.Throwable th) {
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                throw th;
                            }
                            break;
                        }
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    case 6:
                        boolean shown = message.arg1 == 1;
                        com.android.server.wm.AccessibilityController.DisplayMagnifier.this.mCallbacks.onImeWindowVisibilityChanged(shown);
                        return;
                }
            }
        }

        private class UserContextChangedNotifier {
            private final android.os.Handler mHandler;
            private boolean mHasDelayedNotificationForRecentsToFrontTransition;

            UserContextChangedNotifier(android.os.Handler handler) {
                this.mHandler = handler;
            }

            void onAppWindowTransition(int transition) {
                sendUserContextChangedNotification();
            }

            void onWMTransition(int type, int flags) {
                if (com.android.window.flags.Flags.delayNotificationToMagnificationWhenRecentsWindowToFrontTransition() && type == 3 && (flags & 128) != 0) {
                    this.mHasDelayedNotificationForRecentsToFrontTransition = true;
                } else {
                    sendUserContextChangedNotification();
                }
            }

            void onWindowTransition(com.android.server.wm.WindowState windowState, int transition) {
                if (transition == 2 && windowState.isActivityTypeHomeOrRecents() && this.mHasDelayedNotificationForRecentsToFrontTransition) {
                    this.mHasDelayedNotificationForRecentsToFrontTransition = false;
                }
            }

            void onFocusLost(com.android.server.wm.InputTarget target) {
                if (this.mHasDelayedNotificationForRecentsToFrontTransition) {
                    sendUserContextChangedNotification();
                }
            }

            private void sendUserContextChangedNotification() {
                this.mHasDelayedNotificationForRecentsToFrontTransition = false;
                this.mHandler.sendEmptyMessage(3);
            }
        }
    }

    static boolean isUntouchableNavigationBar(com.android.server.wm.WindowState windowState, android.graphics.Region touchableRegion) {
        if (windowState.mAttrs.type != 2019) {
            return false;
        }
        windowState.getTouchableRegion(touchableRegion);
        return touchableRegion.isEmpty();
    }

    static android.graphics.Rect getSystemBarInsetsFrame(com.android.server.wm.WindowState win) {
        if (win == null) {
            return EMPTY_RECT;
        }
        com.android.server.wm.InsetsSourceProvider provider = win.getControllableInsetProvider();
        return provider != null ? provider.getSource().getFrame() : EMPTY_RECT;
    }

    private static final class WindowsForAccessibilityObserver {
        private static final boolean DEBUG = false;
        private static final java.lang.String LOG_TAG = "WindowManager";
        private final com.android.server.wm.AccessibilityWindowsPopulator mA11yWindowsPopulator;
        private final com.android.server.wm.AccessibilityController.AccessibilityControllerInternalImpl mAccessibilityTracing;
        private final com.android.server.wm.WindowManagerInternal.WindowsForAccessibilityCallback mCallback;
        private final int mDisplayId;
        private final android.os.Handler mHandler;
        private boolean mInitialized;
        private final com.android.server.wm.WindowManagerService mService;
        private final java.util.Set<android.os.IBinder> mTempBinderSet = new android.util.ArraySet();
        private final android.graphics.Region mTempRegion = new android.graphics.Region();
        private final android.graphics.Region mTempRegion2 = new android.graphics.Region();
        private final long mRecurringAccessibilityEventsIntervalMillis = android.view.ViewConfiguration.getSendRecurringAccessibilityEventsInterval();

        WindowsForAccessibilityObserver(com.android.server.wm.WindowManagerService windowManagerService, int displayId, com.android.server.wm.WindowManagerInternal.WindowsForAccessibilityCallback callback, com.android.server.wm.AccessibilityWindowsPopulator accessibilityWindowsPopulator) throws java.lang.Throwable {
            this.mService = windowManagerService;
            this.mCallback = callback;
            this.mDisplayId = displayId;
            this.mHandler = new com.android.server.wm.AccessibilityController.WindowsForAccessibilityObserver.MyHandler(this.mService.mH.getLooper());
            this.mAccessibilityTracing = com.android.server.wm.AccessibilityController.getAccessibilityControllerInternal(this.mService);
            this.mA11yWindowsPopulator = accessibilityWindowsPopulator;
            computeChangedWindows(true);
        }

        void performComputeChangedWindows(boolean forceSend) throws java.lang.Throwable {
            if (this.mAccessibilityTracing.isTracingEnabled(1024L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.performComputeChangedWindows", 1024L, "forceSend=" + forceSend);
            }
            this.mHandler.removeMessages(1);
            computeChangedWindows(forceSend);
        }

        void scheduleComputeChangedWindows() {
            if (this.mAccessibilityTracing.isTracingEnabled(1024L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.scheduleComputeChangedWindows", 1024L);
            }
            if (!this.mHandler.hasMessages(1)) {
                this.mHandler.sendEmptyMessageDelayed(1, this.mRecurringAccessibilityEventsIntervalMillis);
            }
        }

        void computeChangedWindows(boolean forceSend) throws java.lang.Throwable {
            com.android.server.wm.WindowState topFocusedWindowState;
            if (this.mAccessibilityTracing.isTracingEnabled(1024L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.computeChangedWindows", 1024L, "forceSend=" + forceSend);
            }
            java.util.List<android.view.WindowInfo> windows = null;
            java.util.List<com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow> visibleWindows = new java.util.ArrayList<>();
            android.graphics.Point screenSize = new android.graphics.Point();
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.RecentsAnimationController controller = this.mService.getRecentsAnimationController();
                    if (controller != null) {
                        topFocusedWindowState = controller.getTargetAppMainWindow();
                    } else {
                        topFocusedWindowState = getTopFocusWindow();
                    }
                    if (topFocusedWindowState == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.DisplayContent dc = this.mService.mRoot.getDisplayContent(this.mDisplayId);
                    if (dc == null) {
                        android.util.Slog.w(LOG_TAG, "display content is null, should be created later");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    android.view.Display display = dc.getDisplay();
                    display.getRealSize(screenSize);
                    this.mA11yWindowsPopulator.populateVisibleWindowsOnScreenLocked(this.mDisplayId, visibleWindows);
                    if (!com.android.server.accessibility.Flags.computeWindowChangesOnA11yV2()) {
                        windows = buildWindowInfoListLocked(visibleWindows, screenSize);
                    }
                    int topFocusedDisplayId = this.mService.mRoot.getTopFocusedDisplayContent().getDisplayId();
                    android.os.IBinder topFocusedWindowToken = topFocusedWindowState.mClient.asBinder();
                    try {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        if (com.android.server.accessibility.Flags.computeWindowChangesOnA11yV2()) {
                            this.mCallback.onAccessibilityWindowsChanged(forceSend, topFocusedDisplayId, topFocusedWindowToken, screenSize, visibleWindows);
                        } else {
                            this.mCallback.onWindowsForAccessibilityChanged(forceSend, topFocusedDisplayId, topFocusedWindowToken, windows);
                        }
                        for (com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow window : visibleWindows) {
                            window.getWindowInfo().recycle();
                        }
                        this.mInitialized = true;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        }

        private java.util.List<android.view.WindowInfo> buildWindowInfoListLocked(java.util.List<com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow> visibleWindows, android.graphics.Point screenSize) {
            java.util.List<android.view.WindowInfo> windows = new java.util.ArrayList<>();
            java.util.Set<android.os.IBinder> addedWindows = this.mTempBinderSet;
            addedWindows.clear();
            boolean focusedWindowAdded = false;
            int visibleWindowCount = visibleWindows.size();
            android.graphics.Region unaccountedSpace = this.mTempRegion;
            unaccountedSpace.set(0, 0, screenSize.x, screenSize.y);
            for (int i = 0; i < visibleWindowCount; i++) {
                com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow a11yWindow = visibleWindows.get(i);
                android.graphics.Region regionInWindow = new android.graphics.Region();
                a11yWindow.getTouchableRegionInWindow(regionInWindow);
                if (windowMattersToAccessibility(a11yWindow, regionInWindow, unaccountedSpace)) {
                    addPopulatedWindowInfo(a11yWindow, regionInWindow, windows, addedWindows);
                    if (windowMattersToUnaccountedSpaceComputation(a11yWindow)) {
                        updateUnaccountedSpace(a11yWindow, unaccountedSpace);
                    }
                    focusedWindowAdded |= a11yWindow.isFocused();
                } else if (a11yWindow.isUntouchableNavigationBar()) {
                    unaccountedSpace.op(com.android.server.wm.AccessibilityController.getSystemBarInsetsFrame(this.mService.mWindowMap.get(a11yWindow.getWindowInfo().token)), unaccountedSpace, android.graphics.Region.Op.REVERSE_DIFFERENCE);
                }
                if (unaccountedSpace.isEmpty() && focusedWindowAdded) {
                    break;
                }
            }
            int windowCount = windows.size();
            for (int i2 = 0; i2 < windowCount; i2++) {
                android.view.WindowInfo window = windows.get(i2);
                if (!addedWindows.contains(window.parentToken)) {
                    window.parentToken = null;
                }
                if (window.childTokens != null) {
                    int childTokenCount = window.childTokens.size();
                    for (int j = childTokenCount - 1; j >= 0; j--) {
                        if (!addedWindows.contains(window.childTokens.get(j))) {
                            window.childTokens.remove(j);
                        }
                    }
                }
            }
            addedWindows.clear();
            return windows;
        }

        private boolean windowMattersToUnaccountedSpaceComputation(com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow a11yWindow) {
            return (a11yWindow.isTouchable() || a11yWindow.getType() == 2034 || !a11yWindow.isTrustedOverlay()) && a11yWindow.getType() != 2032;
        }

        private boolean windowMattersToAccessibility(com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow a11yWindow, android.graphics.Region regionInScreen, android.graphics.Region unaccountedSpace) {
            if (a11yWindow.ignoreRecentsAnimationForAccessibility()) {
                return false;
            }
            if (a11yWindow.isFocused()) {
                return true;
            }
            com.android.server.wm.WindowState windowState = this.mService.mWindowMap.get(a11yWindow.getWindowInfo().token);
            if (windowState == null || !windowState.getWrapper().getExtImpl().shouldAddSettingsWindowToA11y(windowState)) {
                return (a11yWindow.isTouchable() || a11yWindow.getType() == 2034 || a11yWindow.isPIPMenu()) && !unaccountedSpace.quickReject(regionInScreen) && isReportedWindowType(a11yWindow.getType());
            }
            return true;
        }

        private void updateUnaccountedSpace(com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow a11yWindow, android.graphics.Region unaccountedSpace) {
            if (a11yWindow.getType() != 2032) {
                android.graphics.Region touchableRegion = this.mTempRegion2;
                a11yWindow.getTouchableRegionInScreen(touchableRegion);
                unaccountedSpace.op(touchableRegion, unaccountedSpace, android.graphics.Region.Op.REVERSE_DIFFERENCE);
            }
        }

        private static void addPopulatedWindowInfo(com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow a11yWindow, android.graphics.Region regionInScreen, java.util.List<android.view.WindowInfo> out, java.util.Set<android.os.IBinder> tokenOut) {
            android.view.WindowInfo window = a11yWindow.getWindowInfo();
            if (window.token == null) {
                return;
            }
            window.regionInScreen.set(regionInScreen);
            window.layer = tokenOut.size();
            out.add(window);
            tokenOut.add(window.token);
        }

        private static boolean isReportedWindowType(int windowType) {
            return (windowType == 2013 || windowType == 2021 || windowType == 2026 || windowType == 2016 || windowType == 2022 || windowType == 2018 || windowType == 2027 || windowType == 1004 || windowType == 2015 || windowType == 2030) ? false : true;
        }

        private com.android.server.wm.WindowState getTopFocusWindow() {
            return this.mService.mRoot.getTopFocusedDisplayContent().mCurrentFocus;
        }

        public java.lang.String toString() {
            return "WindowsForAccessibilityObserver{mDisplayId=" + this.mDisplayId + ", mInitialized=" + this.mInitialized + '}';
        }

        private class MyHandler extends android.os.Handler {
            public static final int MESSAGE_COMPUTE_CHANGED_WINDOWS = 1;

            public MyHandler(android.os.Looper looper) {
                super(looper, null, false);
            }

            @Override // android.os.Handler
            public void handleMessage(android.os.Message message) throws java.lang.Throwable {
                switch (message.what) {
                    case 1:
                        com.android.server.wm.AccessibilityController.WindowsForAccessibilityObserver.this.computeChangedWindows(false);
                        break;
                }
            }
        }
    }

    static final class AccessibilityControllerInternalImpl implements com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal {
        private static com.android.server.wm.AccessibilityController.AccessibilityControllerInternalImpl sInstance;
        private com.android.server.wm.AccessibilityController.AccessibilityControllerInternalImpl.UiChangesForAccessibilityCallbacksDispatcher mCallbacksDispatcher;
        private volatile long mEnabledTracingFlags = 0;
        private final android.os.Looper mLooper;
        private final com.android.server.wm.AccessibilityController.AccessibilityTracing mTracing;

        static com.android.server.wm.AccessibilityController.AccessibilityControllerInternalImpl getInstance(com.android.server.wm.WindowManagerService service) {
            com.android.server.wm.AccessibilityController.AccessibilityControllerInternalImpl accessibilityControllerInternalImpl;
            synchronized (com.android.server.wm.AccessibilityController.STATIC_LOCK) {
                if (sInstance == null) {
                    sInstance = new com.android.server.wm.AccessibilityController.AccessibilityControllerInternalImpl(service);
                }
                accessibilityControllerInternalImpl = sInstance;
            }
            return accessibilityControllerInternalImpl;
        }

        private AccessibilityControllerInternalImpl(com.android.server.wm.WindowManagerService service) {
            this.mLooper = service.mH.getLooper();
            this.mTracing = com.android.server.wm.AccessibilityController.AccessibilityTracing.getInstance(service);
        }

        @Override // com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal
        public void startTrace(long loggingTypes) {
            this.mEnabledTracingFlags = loggingTypes;
            this.mTracing.startTrace();
        }

        @Override // com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal
        public void stopTrace() {
            this.mTracing.stopTrace();
            this.mEnabledTracingFlags = 0L;
        }

        @Override // com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal
        public boolean isAccessibilityTracingEnabled() {
            return this.mTracing.isEnabled();
        }

        boolean isTracingEnabled(long flags) {
            return (this.mEnabledTracingFlags & flags) != 0;
        }

        void logTrace(java.lang.String where, long loggingTypes) {
            logTrace(where, loggingTypes, "");
        }

        void logTrace(java.lang.String where, long loggingTypes, java.lang.String callingParams) {
            logTrace(where, loggingTypes, callingParams, "".getBytes(), android.os.Binder.getCallingUid());
        }

        void logTrace(java.lang.String where, long loggingTypes, java.lang.String callingParams, byte[] a11yDump, int callingUid) {
            this.mTracing.logState(where, loggingTypes, callingParams, a11yDump, callingUid, new java.util.HashSet(java.util.Arrays.asList("logTrace")));
        }

        @Override // com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal
        public void logTrace(java.lang.String where, long loggingTypes, java.lang.String callingParams, byte[] a11yDump, int callingUid, java.lang.StackTraceElement[] stackTrace, java.util.Set<java.lang.String> ignoreStackEntries) {
            this.mTracing.logState(where, loggingTypes, callingParams, a11yDump, callingUid, stackTrace, ignoreStackEntries);
        }

        @Override // com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal
        public void logTrace(java.lang.String where, long loggingTypes, java.lang.String callingParams, byte[] a11yDump, int callingUid, java.lang.StackTraceElement[] callStack, long timeStamp, int processId, long threadId, java.util.Set<java.lang.String> ignoreStackEntries) {
            this.mTracing.logState(where, loggingTypes, callingParams, a11yDump, callingUid, callStack, timeStamp, processId, threadId, ignoreStackEntries);
        }

        @Override // com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal
        public void setUiChangesForAccessibilityCallbacks(com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks callbacks) {
            if (isTracingEnabled(2048L)) {
                logTrace(com.android.server.wm.AccessibilityController.TAG + ".setAccessibilityWindowManagerCallbacks", 2048L, "callbacks={" + callbacks + "}");
            }
            if (callbacks != null) {
                if (this.mCallbacksDispatcher != null) {
                    throw new java.lang.IllegalStateException("Accessibility window manager callback already set!");
                }
                this.mCallbacksDispatcher = new com.android.server.wm.AccessibilityController.AccessibilityControllerInternalImpl.UiChangesForAccessibilityCallbacksDispatcher(this, this.mLooper, callbacks);
            } else {
                if (this.mCallbacksDispatcher == null) {
                    throw new java.lang.IllegalStateException("Accessibility window manager callback already cleared!");
                }
                this.mCallbacksDispatcher = null;
            }
        }

        public boolean hasWindowManagerEventDispatcher() {
            if (isTracingEnabled(3072L)) {
                logTrace(com.android.server.wm.AccessibilityController.TAG + ".hasCallbacks", 3072L);
            }
            return this.mCallbacksDispatcher != null;
        }

        public void onRectangleOnScreenRequested(int displayId, android.graphics.Rect rectangle) {
            if (isTracingEnabled(2048L)) {
                logTrace(com.android.server.wm.AccessibilityController.TAG + ".onRectangleOnScreenRequested", 2048L, "rectangle={" + rectangle + "}");
            }
            if (this.mCallbacksDispatcher != null) {
                this.mCallbacksDispatcher.onRectangleOnScreenRequested(displayId, rectangle);
            }
        }

        private static final class UiChangesForAccessibilityCallbacksDispatcher {
            private static final boolean DEBUG_RECTANGLE_REQUESTED = false;
            private static final java.lang.String LOG_TAG = "WindowManager";
            private final com.android.server.wm.AccessibilityController.AccessibilityControllerInternalImpl mAccessibilityTracing;
            private final com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks mCallbacks;
            private final android.os.Handler mHandler;

            UiChangesForAccessibilityCallbacksDispatcher(com.android.server.wm.AccessibilityController.AccessibilityControllerInternalImpl accessibilityControllerInternal, android.os.Looper looper, com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks callbacks) {
                this.mAccessibilityTracing = accessibilityControllerInternal;
                this.mCallbacks = callbacks;
                this.mHandler = new android.os.Handler(looper);
            }

            void onRectangleOnScreenRequested(int displayId, android.graphics.Rect rectangle) {
                if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                    this.mAccessibilityTracing.logTrace("WindowManager.onRectangleOnScreenRequested", 2048L, "rectangle={" + rectangle + "}");
                }
                final com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks uiChangesForAccessibilityCallbacks = this.mCallbacks;
                java.util.Objects.requireNonNull(uiChangesForAccessibilityCallbacks);
                android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuintConsumer() { // from class: com.android.server.wm.AccessibilityController$AccessibilityControllerInternalImpl$UiChangesForAccessibilityCallbacksDispatcher$$ExternalSyntheticLambda0
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5) {
                        uiChangesForAccessibilityCallbacks.onRectangleOnScreenRequested(((java.lang.Integer) obj).intValue(), ((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue(), ((java.lang.Integer) obj4).intValue(), ((java.lang.Integer) obj5).intValue());
                    }
                }, java.lang.Integer.valueOf(displayId), java.lang.Integer.valueOf(rectangle.left), java.lang.Integer.valueOf(rectangle.top), java.lang.Integer.valueOf(rectangle.right), java.lang.Integer.valueOf(rectangle.bottom));
                this.mHandler.sendMessage(m);
            }
        }
    }

    private static final class AccessibilityTracing {
        private static final int BUFFER_CAPACITY = 12582912;
        private static final int CPU_STATS_COUNT = 5;
        private static final long MAGIC_NUMBER_VALUE = 4846245196254490945L;
        private static final java.lang.String TAG = "AccessibilityTracing";
        private static final java.lang.String TRACE_FILENAME = "/data/misc/a11ytrace/a11y_trace.winscope";
        private static com.android.server.wm.AccessibilityController.AccessibilityTracing sInstance;
        private volatile boolean mEnabled;
        private final com.android.server.wm.AccessibilityController.AccessibilityTracing.LogHandler mHandler;
        private final com.android.server.wm.WindowManagerService mService;
        private final java.lang.Object mLock = new java.lang.Object();
        private final java.io.File mTraceFile = new java.io.File(TRACE_FILENAME);
        private final com.android.internal.util.TraceBuffer mBuffer = new com.android.internal.util.TraceBuffer(BUFFER_CAPACITY);

        static com.android.server.wm.AccessibilityController.AccessibilityTracing getInstance(com.android.server.wm.WindowManagerService service) {
            com.android.server.wm.AccessibilityController.AccessibilityTracing accessibilityTracing;
            synchronized (com.android.server.wm.AccessibilityController.STATIC_LOCK) {
                if (sInstance == null) {
                    sInstance = new com.android.server.wm.AccessibilityController.AccessibilityTracing(service);
                }
                accessibilityTracing = sInstance;
            }
            return accessibilityTracing;
        }

        AccessibilityTracing(com.android.server.wm.WindowManagerService service) {
            this.mService = service;
            android.os.HandlerThread workThread = new android.os.HandlerThread(TAG);
            workThread.start();
            this.mHandler = new com.android.server.wm.AccessibilityController.AccessibilityTracing.LogHandler(workThread.getLooper());
        }

        void startTrace() {
            if (android.os.Build.IS_USER) {
                android.util.Slog.e(TAG, "Error: Tracing is not supported on user builds.");
                return;
            }
            synchronized (this.mLock) {
                this.mEnabled = true;
                this.mBuffer.resetBuffer();
            }
        }

        void stopTrace() {
            if (android.os.Build.IS_USER) {
                android.util.Slog.e(TAG, "Error: Tracing is not supported on user builds.");
                return;
            }
            synchronized (this.mLock) {
                this.mEnabled = false;
                if (this.mEnabled) {
                    android.util.Slog.e(TAG, "Error: tracing enabled while waiting for flush.");
                } else {
                    writeTraceToFile();
                }
            }
        }

        boolean isEnabled() {
            return this.mEnabled;
        }

        void logState(java.lang.String where, long loggingTypes) {
            if (!this.mEnabled) {
                return;
            }
            logState(where, loggingTypes, "");
        }

        void logState(java.lang.String where, long loggingTypes, java.lang.String callingParams) {
            if (!this.mEnabled) {
                return;
            }
            logState(where, loggingTypes, callingParams, "".getBytes());
        }

        void logState(java.lang.String where, long loggingTypes, java.lang.String callingParams, byte[] a11yDump) {
            if (!this.mEnabled) {
                return;
            }
            logState(where, loggingTypes, callingParams, a11yDump, android.os.Binder.getCallingUid(), new java.util.HashSet(java.util.Arrays.asList("logState")));
        }

        void logState(java.lang.String where, long loggingTypes, java.lang.String callingParams, byte[] a11yDump, int callingUid, java.util.Set<java.lang.String> ignoreStackEntries) {
            if (!this.mEnabled) {
                return;
            }
            java.lang.StackTraceElement[] stackTraceElements = java.lang.Thread.currentThread().getStackTrace();
            ignoreStackEntries.add("logState");
            logState(where, loggingTypes, callingParams, a11yDump, callingUid, stackTraceElements, ignoreStackEntries);
        }

        void logState(java.lang.String where, long loggingTypes, java.lang.String callingParams, byte[] a11yDump, int callingUid, java.lang.StackTraceElement[] stackTrace, java.util.Set<java.lang.String> ignoreStackEntries) {
            if (!this.mEnabled) {
                return;
            }
            log(where, loggingTypes, callingParams, a11yDump, callingUid, stackTrace, android.os.SystemClock.elapsedRealtimeNanos(), android.os.Process.myPid() + ":" + android.app.Application.getProcessName(), java.lang.Thread.currentThread().getId() + ":" + java.lang.Thread.currentThread().getName(), ignoreStackEntries);
        }

        void logState(java.lang.String where, long loggingTypes, java.lang.String callingParams, byte[] a11yDump, int callingUid, java.lang.StackTraceElement[] callingStack, long timeStamp, int processId, long threadId, java.util.Set<java.lang.String> ignoreStackEntries) {
            if (!this.mEnabled) {
                return;
            }
            log(where, loggingTypes, callingParams, a11yDump, callingUid, callingStack, timeStamp, java.lang.String.valueOf(processId), java.lang.String.valueOf(threadId), ignoreStackEntries);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public java.lang.String toStackTraceString(java.lang.StackTraceElement[] stackTraceElements, java.util.Set<java.lang.String> ignoreStackEntries) {
            if (stackTraceElements == null) {
                return "";
            }
            java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
            int i = 0;
            int firstMatch = -1;
            while (i < stackTraceElements.length) {
                java.util.Iterator<java.lang.String> it = ignoreStackEntries.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    java.lang.String ele = it.next();
                    if (stackTraceElements[i].toString().contains(ele)) {
                        firstMatch = i;
                        break;
                    }
                }
                if (firstMatch >= 0) {
                    break;
                }
                i++;
            }
            int lastMatch = firstMatch;
            if (i < stackTraceElements.length) {
                do {
                    i++;
                    if (i >= stackTraceElements.length) {
                        break;
                    }
                    java.util.Iterator<java.lang.String> it2 = ignoreStackEntries.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        java.lang.String ele2 = it2.next();
                        if (stackTraceElements[i].toString().contains(ele2)) {
                            lastMatch = i;
                            break;
                        }
                    }
                } while (lastMatch == i);
            }
            for (int i2 = lastMatch + 1; i2 < stackTraceElements.length; i2++) {
                stringBuilder.append(stackTraceElements[i2].toString()).append("\n");
            }
            return stringBuilder.toString();
        }

        private void log(java.lang.String where, long loggingTypes, java.lang.String callingParams, byte[] a11yDump, int callingUid, java.lang.StackTraceElement[] callingStack, long timeStamp, java.lang.String processName, java.lang.String threadName, java.util.Set<java.lang.String> ignoreStackEntries) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.argl1 = timeStamp;
            args.argl2 = loggingTypes;
            args.arg1 = where;
            args.arg2 = processName;
            args.arg3 = threadName;
            args.arg4 = ignoreStackEntries;
            args.arg5 = callingParams;
            args.arg6 = callingStack;
            args.arg7 = a11yDump;
            this.mHandler.obtainMessage(1, callingUid, 0, args).sendToTarget();
        }

        void writeTraceToFile() {
            this.mHandler.sendEmptyMessage(2);
        }

        private class LogHandler extends android.os.Handler {
            public static final int MESSAGE_LOG_TRACE_ENTRY = 1;
            public static final int MESSAGE_WRITE_FILE = 2;

            LogHandler(android.os.Looper looper) {
                super(looper);
            }

            @Override // android.os.Handler
            public void handleMessage(android.os.Message message) throws java.lang.Throwable {
                switch (message.what) {
                    case 1:
                        com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) message.obj;
                        try {
                            android.util.proto.ProtoOutputStream os = new android.util.proto.ProtoOutputStream();
                            android.content.pm.PackageManagerInternal pmInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
                            long tokenOuter = os.start(2246267895810L);
                            long reportedTimeStampNanos = args.argl1;
                            long currentElapsedRealtimeNanos = android.os.SystemClock.elapsedRealtimeNanos();
                            long timeDiffNanos = currentElapsedRealtimeNanos - reportedTimeStampNanos;
                            long currentTimeMillis = new java.util.Date().getTime();
                            long reportedTimeMillis = currentTimeMillis - (timeDiffNanos / 1000000);
                            java.text.SimpleDateFormat fm = new java.text.SimpleDateFormat("MM-dd HH:mm:ss.SSS");
                            os.write(1125281431553L, reportedTimeStampNanos);
                            os.write(1138166333442L, fm.format(java.lang.Long.valueOf(reportedTimeMillis)).toString());
                            long loggingTypes = args.argl2;
                            java.util.List<java.lang.String> loggingTypeNames = android.accessibilityservice.AccessibilityTrace.getNamesOfLoggingTypes(loggingTypes);
                            for (java.lang.String type : loggingTypeNames) {
                                try {
                                    java.text.SimpleDateFormat fm2 = fm;
                                    long loggingTypes2 = loggingTypes;
                                    os.write(2237677961219L, type);
                                    fm = fm2;
                                    loggingTypes = loggingTypes2;
                                } catch (java.lang.Exception e) {
                                    e = e;
                                    android.util.Slog.e(com.android.server.wm.AccessibilityController.AccessibilityTracing.TAG, "Exception while tracing state", e);
                                    return;
                                }
                            }
                            os.write(1138166333446L, (java.lang.String) args.arg1);
                            os.write(1138166333444L, (java.lang.String) args.arg2);
                            os.write(1138166333445L, (java.lang.String) args.arg3);
                            os.write(1138166333447L, pmInternal.getNameForUid(message.arg1));
                            os.write(1138166333448L, (java.lang.String) args.arg5);
                            java.lang.String callingStack = com.android.server.wm.AccessibilityController.AccessibilityTracing.this.toStackTraceString((java.lang.StackTraceElement[]) args.arg6, (java.util.Set) args.arg4);
                            os.write(1138166333449L, callingStack);
                            os.write(1146756268042L, (byte[]) args.arg7);
                            long tokenInner = os.start(1146756268043L);
                            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.AccessibilityController.AccessibilityTracing.this.mService.mGlobalLock;
                            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                            try {
                                try {
                                    synchronized (windowManagerGlobalLock) {
                                        try {
                                            com.android.server.wm.AccessibilityController.AccessibilityTracing.this.mService.dumpDebugLocked(os, 0);
                                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                            os.end(tokenInner);
                                            os.write(1138166333452L, com.android.server.wm.AccessibilityController.AccessibilityTracing.this.printCpuStats(reportedTimeStampNanos));
                                            os.end(tokenOuter);
                                            synchronized (com.android.server.wm.AccessibilityController.AccessibilityTracing.this.mLock) {
                                                com.android.server.wm.AccessibilityController.AccessibilityTracing.this.mBuffer.add(os);
                                                break;
                                            }
                                            return;
                                        } catch (java.lang.Throwable th) {
                                            th = th;
                                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                            throw th;
                                        }
                                    }
                                } catch (java.lang.Exception e2) {
                                    e = e2;
                                    android.util.Slog.e(com.android.server.wm.AccessibilityController.AccessibilityTracing.TAG, "Exception while tracing state", e);
                                    return;
                                }
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                            }
                        } catch (java.lang.Exception e3) {
                            e = e3;
                        }
                        break;
                    case 2:
                        synchronized (com.android.server.wm.AccessibilityController.AccessibilityTracing.this.mLock) {
                            com.android.server.wm.AccessibilityController.AccessibilityTracing.this.writeTraceToFileInternal();
                            break;
                        }
                        return;
                    default:
                        return;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeTraceToFileInternal() {
            try {
                android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream();
                proto.write(1125281431553L, MAGIC_NUMBER_VALUE);
                long timeOffsetNs = java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(java.lang.System.currentTimeMillis()) - android.os.SystemClock.elapsedRealtimeNanos();
                proto.write(1125281431555L, timeOffsetNs);
                this.mBuffer.writeTraceToFile(this.mTraceFile, proto);
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "Unable to write buffer to file", e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public java.lang.String printCpuStats(long timeStampNanos) {
            android.util.Pair<java.lang.String, java.lang.String> stats = this.mService.mAmInternal.getAppProfileStatsForDebugging(timeStampNanos, 5);
            return ((java.lang.String) stats.first) + ((java.lang.String) stats.second);
        }
    }
}
