package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public class AccessibilityWindowManager {
    private static final boolean AGINGTEST = "1".equals(android.os.SystemProperties.get("persist.sys.agingtest", "0"));
    private static final boolean DEBUG;
    private static final java.lang.String LOG_TAG = "AccessibilityWindowManager";
    private static final boolean VERBOSE = false;
    private static int sNextWindowId;
    private final com.android.server.accessibility.AccessibilityWindowManager.AccessibilityEventSender mAccessibilityEventSender;
    private final com.android.server.accessibility.AccessibilitySecurityPolicy.AccessibilityUserManager mAccessibilityUserManager;
    private final android.os.Handler mHandler;
    private boolean mHasProxy;
    private int mLastNonProxyTopFocusedDisplayId;
    private final java.lang.Object mLock;
    private com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection mPictureInPictureActionReplacingConnection;
    private final com.android.server.accessibility.AccessibilitySecurityPolicy mSecurityPolicy;
    private int mTopFocusedDisplayId;
    private android.os.IBinder mTopFocusedWindowToken;
    private boolean mTouchInteractionInProgress;
    private final com.android.server.accessibility.AccessibilityTraceManager mTraceManager;
    private final com.android.server.wm.WindowManagerInternal mWindowManagerInternal;
    private final android.graphics.Region mTmpRegion = new android.graphics.Region();
    private final android.util.SparseArray<com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection> mGlobalInteractionConnections = new android.util.SparseArray<>();
    private final android.util.SparseArray<android.os.IBinder> mGlobalWindowTokens = new android.util.SparseArray<>();
    private final android.util.SparseArray<android.util.SparseArray<com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection>> mInteractionConnections = new android.util.SparseArray<>();
    private final android.util.SparseArray<android.util.SparseArray<android.os.IBinder>> mWindowTokens = new android.util.SparseArray<>();
    private int mActiveWindowId = -1;
    private int mTopFocusedWindowId = -1;
    private int mAccessibilityFocusedWindowId = -1;
    private long mAccessibilityFocusNodeId = 2147483647L;
    private int mAccessibilityFocusedDisplayId = -1;
    private final android.util.SparseArray<com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver> mDisplayWindowsObservers = new android.util.SparseArray<>();
    private final android.util.ArrayMap<android.os.IBinder, android.os.IBinder> mHostEmbeddedMap = new android.util.ArrayMap<>();
    private final android.util.SparseArray<android.os.IBinder> mWindowIdMap = new android.util.SparseArray<>();
    private final android.util.SparseArray<android.view.accessibility.AccessibilityWindowAttributes> mWindowAttributes = new android.util.SparseArray<>();

    public interface AccessibilityEventSender {
        void sendAccessibilityEventForCurrentUserLocked(android.view.accessibility.AccessibilityEvent accessibilityEvent);
    }

    static {
        boolean z = false;
        if (!AGINGTEST && (android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false) || android.os.SystemProperties.getBoolean("persist.sys.alwayson.enable", false))) {
            z = true;
        }
        DEBUG = z;
    }

    public void setAccessibilityWindowAttributes(int displayId, int windowId, int userId, android.view.accessibility.AccessibilityWindowAttributes attributes) {
        synchronized (this.mLock) {
            int resolvedUserId = this.mSecurityPolicy.resolveCallingUserIdEnforcingPermissionsLocked(userId);
            if (getWindowTokenForUserAndWindowIdLocked(resolvedUserId, windowId) == null) {
                return;
            }
            this.mWindowAttributes.put(windowId, attributes);
            boolean shouldComputeWindows = findWindowInfoByIdLocked(windowId) != null;
            if (shouldComputeWindows) {
                this.mWindowManagerInternal.computeWindowsForAccessibility(displayId);
            }
        }
    }

    public boolean windowIdBelongsToDisplayType(int focusedWindowId, int displayTypes) {
        boolean z = true;
        if (!this.mHasProxy || (displayTypes & 3) == 3) {
            return true;
        }
        synchronized (this.mLock) {
            int count = this.mDisplayWindowsObservers.size();
            for (int i = 0; i < count; i++) {
                com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = this.mDisplayWindowsObservers.valueAt(i);
                if (observer != null && observer.findA11yWindowInfoByIdLocked(focusedWindowId) != null) {
                    if (observer.mIsProxy) {
                        if ((displayTypes & 2) == 0) {
                            z = false;
                        }
                    } else if ((displayTypes & 1) == 0) {
                        z = false;
                    }
                    return z;
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class DisplayWindowsObserver implements com.android.server.wm.WindowManagerInternal.WindowsForAccessibilityCallback {
        private final int mDisplayId;
        private boolean mHasWatchOutsideTouchWindow;
        private boolean mIsProxy;
        private java.util.List<android.view.accessibility.AccessibilityWindowInfo> mWindows;
        private final android.util.SparseArray<android.view.accessibility.AccessibilityWindowInfo> mA11yWindowInfoById = new android.util.SparseArray<>();
        private final android.util.SparseArray<android.view.WindowInfo> mWindowInfoById = new android.util.SparseArray<>();
        private final java.util.List<android.view.WindowInfo> mCachedWindowInfos = new java.util.ArrayList();
        private boolean mTrackingWindows = false;
        private int mProxyDisplayAccessibilityFocusedWindow = -1;

        DisplayWindowsObserver(int displayId) {
            if (com.android.server.accessibility.AccessibilityWindowManager.DEBUG) {
                com.android.server.utils.Slogf.d(com.android.server.accessibility.AccessibilityWindowManager.LOG_TAG, "Creating DisplayWindowsObserver for displayId %d", java.lang.Integer.valueOf(displayId));
            }
            this.mDisplayId = displayId;
        }

        void startTrackingWindowsLocked() {
            if (!this.mTrackingWindows) {
                this.mTrackingWindows = true;
                if (com.android.server.accessibility.AccessibilityWindowManager.this.traceWMEnabled()) {
                    com.android.server.accessibility.AccessibilityWindowManager.this.logTraceWM("setWindowsForAccessibilityCallback", "displayId=" + this.mDisplayId + ";callback=" + this);
                }
                com.android.server.accessibility.AccessibilityWindowManager.this.mWindowManagerInternal.setWindowsForAccessibilityCallback(this.mDisplayId, this);
            }
        }

        void stopTrackingWindowsLocked() {
            if (this.mTrackingWindows) {
                if (com.android.server.accessibility.AccessibilityWindowManager.this.traceWMEnabled()) {
                    com.android.server.accessibility.AccessibilityWindowManager.this.logTraceWM("setWindowsForAccessibilityCallback", "displayId=" + this.mDisplayId + ";callback=null");
                }
                com.android.server.accessibility.AccessibilityWindowManager.this.mWindowManagerInternal.setWindowsForAccessibilityCallback(this.mDisplayId, null);
                this.mTrackingWindows = false;
                clearWindowsLocked();
            }
        }

        boolean isTrackingWindowsLocked() {
            return this.mTrackingWindows;
        }

        java.util.List<android.view.accessibility.AccessibilityWindowInfo> getWindowListLocked() {
            return this.mWindows;
        }

        android.view.accessibility.AccessibilityWindowInfo findA11yWindowInfoByIdLocked(int windowId) {
            return this.mA11yWindowInfoById.get(windowId);
        }

        android.view.WindowInfo findWindowInfoByIdLocked(int windowId) {
            return this.mWindowInfoById.get(windowId);
        }

        android.view.accessibility.AccessibilityWindowInfo getPictureInPictureWindowLocked() {
            if (this.mWindows != null) {
                int windowCount = this.mWindows.size();
                for (int i = 0; i < windowCount; i++) {
                    android.view.accessibility.AccessibilityWindowInfo window = this.mWindows.get(i);
                    if (window.isInPictureInPictureMode()) {
                        return window;
                    }
                }
                return null;
            }
            return null;
        }

        boolean setActiveWindowLocked(int windowId) {
            boolean foundWindow = false;
            if (this.mWindows != null) {
                int windowCount = this.mWindows.size();
                for (int i = 0; i < windowCount; i++) {
                    android.view.accessibility.AccessibilityWindowInfo window = this.mWindows.get(i);
                    if (window.getId() == windowId) {
                        window.setActive(true);
                        foundWindow = true;
                    } else {
                        window.setActive(false);
                    }
                }
            }
            return foundWindow;
        }

        boolean setAccessibilityFocusedWindowLocked(int windowId) {
            boolean foundWindow = false;
            if (this.mWindows != null) {
                int windowCount = this.mWindows.size();
                for (int i = 0; i < windowCount; i++) {
                    android.view.accessibility.AccessibilityWindowInfo window = this.mWindows.get(i);
                    if (window.getId() == windowId) {
                        window.setAccessibilityFocused(true);
                        foundWindow = true;
                    } else {
                        window.setAccessibilityFocused(false);
                    }
                }
            }
            return foundWindow;
        }

        boolean computePartialInteractiveRegionForWindowLocked(int windowId, boolean forceComputeRegion, android.graphics.Region outRegion) {
            if (this.mWindows == null) {
                return false;
            }
            android.graphics.Region windowInteractiveRegion = null;
            boolean windowInteractiveRegionChanged = false;
            int windowCount = this.mWindows.size();
            android.graphics.Region currentWindowRegions = new android.graphics.Region();
            for (int i = windowCount - 1; i >= 0; i--) {
                android.view.accessibility.AccessibilityWindowInfo currentWindow = this.mWindows.get(i);
                if (windowInteractiveRegion == null) {
                    if (currentWindow.getId() == windowId) {
                        currentWindow.getRegionInScreen(currentWindowRegions);
                        outRegion.set(currentWindowRegions);
                        windowInteractiveRegion = outRegion;
                        if (forceComputeRegion) {
                            windowInteractiveRegionChanged = true;
                        }
                    }
                } else if (currentWindow.getType() != 4) {
                    currentWindow.getRegionInScreen(currentWindowRegions);
                    if (windowInteractiveRegion.op(currentWindowRegions, android.graphics.Region.Op.DIFFERENCE)) {
                        windowInteractiveRegionChanged = true;
                    }
                }
            }
            return windowInteractiveRegionChanged;
        }

        java.util.List<java.lang.Integer> getWatchOutsideTouchWindowIdLocked(int targetWindowId) {
            android.view.WindowInfo targetWindow = this.mWindowInfoById.get(targetWindowId);
            if (targetWindow != null && this.mHasWatchOutsideTouchWindow) {
                java.util.List<java.lang.Integer> outsideWindowsId = new java.util.ArrayList<>();
                for (int i = 0; i < this.mWindowInfoById.size(); i++) {
                    android.view.WindowInfo window = this.mWindowInfoById.valueAt(i);
                    if (window != null && window.layer < targetWindow.layer && window.hasFlagWatchOutsideTouch) {
                        outsideWindowsId.add(java.lang.Integer.valueOf(this.mWindowInfoById.keyAt(i)));
                    }
                }
                return outsideWindowsId;
            }
            return java.util.Collections.emptyList();
        }

        @Override // com.android.server.wm.WindowManagerInternal.WindowsForAccessibilityCallback
        public void onWindowsForAccessibilityChanged(boolean forceSend, int topFocusedDisplayId, android.os.IBinder topFocusedWindowToken, java.util.List<android.view.WindowInfo> windows) {
            synchronized (com.android.server.accessibility.AccessibilityWindowManager.this.mLock) {
                if (!com.android.server.accessibility.Flags.computeWindowChangesOnA11yV2()) {
                    updateWindowsByWindowAttributesLocked(windows);
                }
                if (com.android.server.accessibility.AccessibilityWindowManager.DEBUG) {
                    com.android.server.utils.Slogf.i(com.android.server.accessibility.AccessibilityWindowManager.LOG_TAG, "mDisplayId=%d, topFocusedDisplayId=%d, currentUserId=%d, visibleBgUsers=%s", java.lang.Integer.valueOf(this.mDisplayId), java.lang.Integer.valueOf(topFocusedDisplayId), java.lang.Integer.valueOf(com.android.server.accessibility.AccessibilityWindowManager.this.mAccessibilityUserManager.getCurrentUserIdLocked()), com.android.server.accessibility.AccessibilityWindowManager.this.mAccessibilityUserManager.getVisibleUserIdsLocked());
                    java.util.List<java.lang.String> windowsInfo = (java.util.List) windows.stream().map(new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityWindowManager$DisplayWindowsObserver$$ExternalSyntheticLambda0
                        @Override // java.util.function.Function
                        public final java.lang.Object apply(java.lang.Object obj) {
                            return com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver.lambda$onWindowsForAccessibilityChanged$0((android.view.WindowInfo) obj);
                        }
                    }).collect(java.util.stream.Collectors.toList());
                    com.android.server.utils.Slogf.i(com.android.server.accessibility.AccessibilityWindowManager.LOG_TAG, "%d windows changed: %s", java.lang.Integer.valueOf(windows.size()), windowsInfo);
                }
                if (shouldUpdateWindowsLocked(forceSend, windows)) {
                    com.android.server.accessibility.AccessibilityWindowManager.this.mTopFocusedDisplayId = topFocusedDisplayId;
                    if (!com.android.server.accessibility.AccessibilityWindowManager.this.isProxyed(topFocusedDisplayId)) {
                        com.android.server.accessibility.AccessibilityWindowManager.this.mLastNonProxyTopFocusedDisplayId = topFocusedDisplayId;
                    }
                    com.android.server.accessibility.AccessibilityWindowManager.this.mTopFocusedWindowToken = topFocusedWindowToken;
                    if (com.android.server.accessibility.AccessibilityWindowManager.DEBUG) {
                        com.android.server.utils.Slogf.d(com.android.server.accessibility.AccessibilityWindowManager.LOG_TAG, "onWindowsForAccessibilityChanged(): updating windows for display %d and token %s", java.lang.Integer.valueOf(topFocusedDisplayId), topFocusedWindowToken);
                    }
                    cacheWindows(windows);
                    updateWindowsLocked(com.android.server.accessibility.AccessibilityWindowManager.this.mAccessibilityUserManager.getCurrentUserIdLocked(), windows);
                    com.android.server.accessibility.AccessibilityWindowManager.this.mLock.notifyAll();
                } else if (com.android.server.accessibility.AccessibilityWindowManager.DEBUG) {
                    com.android.server.utils.Slogf.d(com.android.server.accessibility.AccessibilityWindowManager.LOG_TAG, "onWindowsForAccessibilityChanged(): NOT updating windows for display %d and token %s", java.lang.Integer.valueOf(topFocusedDisplayId), topFocusedWindowToken);
                }
            }
        }

        static /* synthetic */ java.lang.String lambda$onWindowsForAccessibilityChanged$0(android.view.WindowInfo w) {
            return "{displayId=" + w.displayId + ", title=" + ((java.lang.Object) w.title) + "}";
        }

        @Override // com.android.server.wm.WindowManagerInternal.WindowsForAccessibilityCallback
        public void onAccessibilityWindowsChanged(boolean forceSend, int topFocusedDisplayId, android.os.IBinder topFocusedWindowToken, android.graphics.Point screenSize, java.util.List<com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow> windows) {
            synchronized (com.android.server.accessibility.AccessibilityWindowManager.this.mLock) {
                java.util.List<android.view.WindowInfo> windowInfoList = createWindowInfoListLocked(screenSize, windows);
                onWindowsForAccessibilityChanged(forceSend, topFocusedDisplayId, topFocusedWindowToken, windowInfoList);
            }
        }

        private java.util.List<android.view.WindowInfo> createWindowInfoListLocked(android.graphics.Point screenSize, java.util.List<com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow> visibleWindows) {
            int windowId;
            java.util.Set<android.os.IBinder> addedWindows = new android.util.ArraySet<>();
            java.util.List<android.view.WindowInfo> windows = new java.util.ArrayList<>();
            android.graphics.Region regionInWindow = new android.graphics.Region();
            android.graphics.Region touchableRegionInScreen = new android.graphics.Region();
            int userId = com.android.server.accessibility.AccessibilityWindowManager.this.mAccessibilityUserManager.getCurrentUserIdLocked();
            boolean focusedWindowAdded = false;
            android.graphics.Region unaccountedSpace = new android.graphics.Region(0, 0, screenSize.x, screenSize.y);
            for (com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow a11yWindow : visibleWindows) {
                a11yWindow.getTouchableRegionInWindow(regionInWindow);
                android.view.WindowInfo window = a11yWindow.getWindowInfo();
                if (window.token != null) {
                    windowId = com.android.server.accessibility.AccessibilityWindowManager.this.findWindowIdLocked(userId, window.token);
                } else {
                    windowId = -1;
                }
                if (windowMattersToAccessibilityLocked(a11yWindow, windowId, regionInWindow, unaccountedSpace)) {
                    if (windowId >= 0) {
                        window.regionInScreen.set(regionInWindow);
                        window.layer = addedWindows.size();
                        updateWindowWithWindowAttributes(window, (android.view.accessibility.AccessibilityWindowAttributes) com.android.server.accessibility.AccessibilityWindowManager.this.mWindowAttributes.get(windowId));
                        windows.add(window);
                        addedWindows.add(window.token);
                    }
                    if (windowMattersToUnaccountedSpaceComputation(a11yWindow)) {
                        a11yWindow.getTouchableRegionInScreen(touchableRegionInScreen);
                        unaccountedSpace.op(touchableRegionInScreen, unaccountedSpace, android.graphics.Region.Op.REVERSE_DIFFERENCE);
                    }
                    focusedWindowAdded |= a11yWindow.isFocused();
                } else if (a11yWindow.isUntouchableNavigationBar() && a11yWindow.getSystemBarInsetsFrame() != null) {
                    unaccountedSpace.op(a11yWindow.getSystemBarInsetsFrame(), unaccountedSpace, android.graphics.Region.Op.REVERSE_DIFFERENCE);
                }
                if (unaccountedSpace.isEmpty() && focusedWindowAdded) {
                    break;
                }
            }
            for (android.view.WindowInfo window2 : windows) {
                if (!addedWindows.contains(window2.parentToken)) {
                    window2.parentToken = null;
                }
                if (window2.childTokens != null) {
                    int childTokenCount = window2.childTokens.size();
                    for (int j = childTokenCount - 1; j >= 0; j--) {
                        if (!addedWindows.contains(window2.childTokens.get(j))) {
                            window2.childTokens.remove(j);
                        }
                    }
                }
            }
            return windows;
        }

        private boolean windowMattersToAccessibilityLocked(com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow a11yWindow, int windowId, android.graphics.Region regionInScreen, android.graphics.Region unaccountedSpace) {
            if (a11yWindow.ignoreRecentsAnimationForAccessibility()) {
                return false;
            }
            if (a11yWindow.isFocused()) {
                return true;
            }
            return (a11yWindow.isTouchable() || a11yWindow.getType() == 2034 || a11yWindow.isPIPMenu()) && !com.android.server.accessibility.AccessibilityWindowManager.this.isEmbeddedHierarchyWindowsLocked(windowId) && com.android.server.accessibility.AccessibilityWindowManager.this.mTmpRegion.op(unaccountedSpace, regionInScreen, android.graphics.Region.Op.INTERSECT) && isReportedWindowType(a11yWindow.getType());
        }

        private static boolean isReportedWindowType(int windowType) {
            return (windowType == 2013 || windowType == 2021 || windowType == 2026 || windowType == 2016 || windowType == 2022 || windowType == 2018 || windowType == 2027 || windowType == 1004 || windowType == 2015 || windowType == 2030) ? false : true;
        }

        private static boolean windowMattersToUnaccountedSpaceComputation(com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow a11yWindow) {
            return (a11yWindow.isTouchable() || a11yWindow.getType() == 2034 || !a11yWindow.isTrustedOverlay()) && a11yWindow.getType() != 2032;
        }

        private void updateWindowsByWindowAttributesLocked(java.util.List<android.view.WindowInfo> windows) {
            for (int i = windows.size() - 1; i >= 0; i--) {
                android.view.WindowInfo windowInfo = windows.get(i);
                android.os.IBinder token = windowInfo.token;
                int windowId = com.android.server.accessibility.AccessibilityWindowManager.this.findWindowIdLocked(com.android.server.accessibility.AccessibilityWindowManager.this.mAccessibilityUserManager.getCurrentUserIdLocked(), token);
                updateWindowWithWindowAttributes(windowInfo, (android.view.accessibility.AccessibilityWindowAttributes) com.android.server.accessibility.AccessibilityWindowManager.this.mWindowAttributes.get(windowId));
            }
        }

        private void updateWindowWithWindowAttributes(android.view.WindowInfo windowInfo, android.view.accessibility.AccessibilityWindowAttributes attributes) {
            if (attributes == null) {
                return;
            }
            windowInfo.title = attributes.getWindowTitle();
            windowInfo.locales = attributes.getLocales();
        }

        private boolean shouldUpdateWindowsLocked(boolean forceSend, java.util.List<android.view.WindowInfo> windows) {
            int windowCount;
            if (forceSend || this.mCachedWindowInfos.size() != (windowCount = windows.size())) {
                return true;
            }
            if (!this.mCachedWindowInfos.isEmpty() || !windows.isEmpty()) {
                for (int i = 0; i < windowCount; i++) {
                    android.view.WindowInfo oldWindow = this.mCachedWindowInfos.get(i);
                    android.view.WindowInfo newWindow = windows.get(i);
                    if (windowChangedNoLayer(oldWindow, newWindow)) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        private void cacheWindows(java.util.List<android.view.WindowInfo> windows) {
            int oldWindowCount = this.mCachedWindowInfos.size();
            for (int i = oldWindowCount - 1; i >= 0; i--) {
                this.mCachedWindowInfos.remove(i).recycle();
            }
            int newWindowCount = windows.size();
            for (int i2 = 0; i2 < newWindowCount; i2++) {
                android.view.WindowInfo newWindow = windows.get(i2);
                this.mCachedWindowInfos.add(android.view.WindowInfo.obtain(newWindow));
            }
        }

        private boolean windowChangedNoLayer(android.view.WindowInfo oldWindow, android.view.WindowInfo newWindow) {
            if (oldWindow == newWindow) {
                return false;
            }
            if (oldWindow == null || newWindow == null || oldWindow.type != newWindow.type || oldWindow.focused != newWindow.focused) {
                return true;
            }
            if (oldWindow.token == null) {
                if (newWindow.token != null) {
                    return true;
                }
            } else if (!oldWindow.token.equals(newWindow.token)) {
                return true;
            }
            if (oldWindow.parentToken == null) {
                if (newWindow.parentToken != null) {
                    return true;
                }
            } else if (!oldWindow.parentToken.equals(newWindow.parentToken)) {
                return true;
            }
            if (oldWindow.activityToken == null) {
                if (newWindow.activityToken != null) {
                    return true;
                }
            } else if (!oldWindow.activityToken.equals(newWindow.activityToken)) {
                return true;
            }
            if (!oldWindow.regionInScreen.equals(newWindow.regionInScreen)) {
                return true;
            }
            if ((oldWindow.childTokens == null || newWindow.childTokens == null || oldWindow.childTokens.equals(newWindow.childTokens)) && android.text.TextUtils.equals(oldWindow.title, newWindow.title) && oldWindow.accessibilityIdOfAnchor == newWindow.accessibilityIdOfAnchor && oldWindow.inPictureInPicture == newWindow.inPictureInPicture && oldWindow.hasFlagWatchOutsideTouch == newWindow.hasFlagWatchOutsideTouch && oldWindow.displayId == newWindow.displayId && oldWindow.taskId == newWindow.taskId && java.util.Arrays.equals(oldWindow.mTransformMatrix, newWindow.mTransformMatrix)) {
                return false;
            }
            return true;
        }

        private void clearWindowsLocked() {
            java.util.List<android.view.WindowInfo> windows = java.util.Collections.emptyList();
            int activeWindowId = com.android.server.accessibility.AccessibilityWindowManager.this.mActiveWindowId;
            updateWindowsLocked(com.android.server.accessibility.AccessibilityWindowManager.this.mAccessibilityUserManager.getCurrentUserIdLocked(), windows);
            com.android.server.accessibility.AccessibilityWindowManager.this.mActiveWindowId = activeWindowId;
            this.mWindows = null;
        }

        private void updateWindowsLocked(int userId, java.util.List<android.view.WindowInfo> windows) {
            int a11yFocusedWindowId;
            android.view.accessibility.AccessibilityWindowInfo window;
            boolean shouldClearAccessibilityFocus;
            int i = userId;
            if (this.mWindows == null) {
                this.mWindows = new java.util.ArrayList();
            }
            java.util.List<android.view.accessibility.AccessibilityWindowInfo> oldWindowList = new java.util.ArrayList<>(this.mWindows);
            android.util.SparseArray<android.view.accessibility.AccessibilityWindowInfo> oldWindowsById = this.mA11yWindowInfoById.clone();
            boolean shouldClearAccessibilityFocus2 = false;
            this.mWindows.clear();
            this.mA11yWindowInfoById.clear();
            for (int i2 = 0; i2 < this.mWindowInfoById.size(); i2++) {
                this.mWindowInfoById.valueAt(i2).recycle();
            }
            this.mWindowInfoById.clear();
            this.mHasWatchOutsideTouchWindow = false;
            int windowCount = windows.size();
            boolean isTopFocusedDisplay = this.mDisplayId == com.android.server.accessibility.AccessibilityWindowManager.this.mTopFocusedDisplayId;
            boolean isAccessibilityFocusedDisplay = this.mDisplayId == com.android.server.accessibility.AccessibilityWindowManager.this.mAccessibilityFocusedDisplayId || (this.mIsProxy && this.mProxyDisplayAccessibilityFocusedWindow != -1);
            if (isTopFocusedDisplay) {
                if (windowCount > 0) {
                    com.android.server.accessibility.AccessibilityWindowManager.this.mTopFocusedWindowId = com.android.server.accessibility.AccessibilityWindowManager.this.findWindowIdLocked(i, com.android.server.accessibility.AccessibilityWindowManager.this.mTopFocusedWindowToken);
                } else {
                    com.android.server.accessibility.AccessibilityWindowManager.this.mTopFocusedWindowId = -1;
                }
                if (!com.android.server.accessibility.AccessibilityWindowManager.this.mTouchInteractionInProgress) {
                    com.android.server.accessibility.AccessibilityWindowManager.this.mActiveWindowId = -1;
                }
            }
            boolean activeWindowGone = true;
            if (this.mIsProxy) {
                a11yFocusedWindowId = this.mProxyDisplayAccessibilityFocusedWindow;
            } else {
                a11yFocusedWindowId = com.android.server.accessibility.AccessibilityWindowManager.this.mAccessibilityFocusedWindowId;
            }
            if (isAccessibilityFocusedDisplay) {
                shouldClearAccessibilityFocus2 = a11yFocusedWindowId != -1;
            }
            boolean hasWindowIgnore = false;
            if (windowCount > 0) {
                int i3 = 0;
                while (i3 < windowCount) {
                    android.view.WindowInfo windowInfo = windows.get(i3);
                    if (this.mTrackingWindows) {
                        window = populateReportedWindowLocked(i, windowInfo, oldWindowsById);
                        if (window == null) {
                            hasWindowIgnore = true;
                        }
                    } else {
                        window = null;
                    }
                    if (window == null) {
                        shouldClearAccessibilityFocus = shouldClearAccessibilityFocus2;
                    } else {
                        window.setLayer((windowCount - 1) - window.getLayer());
                        int windowId = window.getId();
                        if (window.isFocused() && isTopFocusedDisplay) {
                            if (!com.android.server.accessibility.AccessibilityWindowManager.this.mTouchInteractionInProgress) {
                                com.android.server.accessibility.AccessibilityWindowManager.this.mActiveWindowId = windowId;
                                window.setActive(true);
                            } else if (windowId == com.android.server.accessibility.AccessibilityWindowManager.this.mActiveWindowId) {
                                activeWindowGone = false;
                            }
                        }
                        if (!this.mHasWatchOutsideTouchWindow && windowInfo.hasFlagWatchOutsideTouch) {
                            this.mHasWatchOutsideTouchWindow = true;
                        }
                        this.mWindows.add(window);
                        this.mA11yWindowInfoById.put(windowId, window);
                        shouldClearAccessibilityFocus = shouldClearAccessibilityFocus2;
                        this.mWindowInfoById.put(windowId, android.view.WindowInfo.obtain(windowInfo));
                    }
                    i3++;
                    i = userId;
                    shouldClearAccessibilityFocus2 = shouldClearAccessibilityFocus;
                }
                boolean shouldClearAccessibilityFocus3 = shouldClearAccessibilityFocus2;
                int accessibilityWindowCount = this.mWindows.size();
                if (hasWindowIgnore) {
                    for (int i4 = 0; i4 < accessibilityWindowCount; i4++) {
                        this.mWindows.get(i4).setLayer((accessibilityWindowCount - 1) - i4);
                    }
                }
                if (isTopFocusedDisplay) {
                    if (com.android.server.accessibility.AccessibilityWindowManager.this.mTouchInteractionInProgress && activeWindowGone) {
                        com.android.server.accessibility.AccessibilityWindowManager.this.mActiveWindowId = com.android.server.accessibility.AccessibilityWindowManager.this.mTopFocusedWindowId;
                    }
                    for (int i5 = 0; i5 < accessibilityWindowCount; i5++) {
                        android.view.accessibility.AccessibilityWindowInfo window2 = this.mWindows.get(i5);
                        if (window2.getId() == com.android.server.accessibility.AccessibilityWindowManager.this.mActiveWindowId) {
                            window2.setActive(true);
                        }
                    }
                }
                if (isAccessibilityFocusedDisplay) {
                    for (int i6 = 0; i6 < accessibilityWindowCount; i6++) {
                        android.view.accessibility.AccessibilityWindowInfo window3 = this.mWindows.get(i6);
                        if (window3.getId() == a11yFocusedWindowId) {
                            window3.setAccessibilityFocused(true);
                            shouldClearAccessibilityFocus2 = false;
                            break;
                        }
                    }
                    shouldClearAccessibilityFocus2 = shouldClearAccessibilityFocus3;
                } else {
                    shouldClearAccessibilityFocus2 = shouldClearAccessibilityFocus3;
                }
            }
            sendEventsForChangedWindowsLocked(oldWindowList, oldWindowsById);
            int oldWindowCount = oldWindowList.size();
            for (int i7 = oldWindowCount - 1; i7 >= 0; i7--) {
                oldWindowList.remove(i7).recycle();
            }
            if (shouldClearAccessibilityFocus2) {
                com.android.server.accessibility.AccessibilityWindowManager.this.clearAccessibilityFocusLocked(a11yFocusedWindowId);
            }
        }

        private void sendEventsForChangedWindowsLocked(java.util.List<android.view.accessibility.AccessibilityWindowInfo> oldWindows, android.util.SparseArray<android.view.accessibility.AccessibilityWindowInfo> oldWindowsById) {
            java.util.List<android.view.accessibility.AccessibilityEvent> events = new java.util.ArrayList<>();
            int oldWindowsCount = oldWindows.size();
            for (int i = 0; i < oldWindowsCount; i++) {
                android.view.accessibility.AccessibilityWindowInfo window = oldWindows.get(i);
                if (this.mA11yWindowInfoById.get(window.getId()) == null) {
                    events.add(android.view.accessibility.AccessibilityEvent.obtainWindowsChangedEvent(this.mDisplayId, window.getId(), 2));
                }
            }
            int newWindowCount = this.mWindows.size();
            for (int i2 = 0; i2 < newWindowCount; i2++) {
                try {
                    android.view.accessibility.AccessibilityWindowInfo newWindow = this.mWindows.get(i2);
                    android.view.accessibility.AccessibilityWindowInfo oldWindow = oldWindowsById.get(newWindow.getId());
                    if (oldWindow == null) {
                        events.add(android.view.accessibility.AccessibilityEvent.obtainWindowsChangedEvent(this.mDisplayId, newWindow.getId(), 1));
                    } else {
                        int changes = newWindow.differenceFrom(oldWindow);
                        if (changes != 0) {
                            events.add(android.view.accessibility.AccessibilityEvent.obtainWindowsChangedEvent(this.mDisplayId, newWindow.getId(), changes));
                        }
                    }
                } catch (java.lang.IllegalArgumentException exception) {
                    android.util.Slog.e(com.android.server.accessibility.AccessibilityWindowManager.LOG_TAG, "sendEventsForChangedWindowsLocked exception ：" + exception);
                }
            }
            int numEvents = events.size();
            for (int i3 = 0; i3 < numEvents; i3++) {
                com.android.server.accessibility.AccessibilityWindowManager.this.mAccessibilityEventSender.sendAccessibilityEventForCurrentUserLocked(events.get(i3));
            }
        }

        private android.view.accessibility.AccessibilityWindowInfo populateReportedWindowLocked(int userId, android.view.WindowInfo window, android.util.SparseArray<android.view.accessibility.AccessibilityWindowInfo> oldWindowsById) {
            int windowId = com.android.server.accessibility.AccessibilityWindowManager.this.findWindowIdLocked(userId, window.token);
            if (!com.android.server.accessibility.Flags.computeWindowChangesOnA11yV2() && (windowId < 0 || com.android.server.accessibility.AccessibilityWindowManager.this.isEmbeddedHierarchyWindowsLocked(windowId))) {
                return null;
            }
            android.view.accessibility.AccessibilityWindowInfo reportedWindow = android.view.accessibility.AccessibilityWindowInfo.obtain();
            reportedWindow.setId(windowId);
            reportedWindow.setType(getTypeForWindowManagerWindowType(window.type));
            reportedWindow.setLayer(window.layer);
            reportedWindow.setFocused(window.focused);
            reportedWindow.setRegionInScreen(window.regionInScreen);
            reportedWindow.setTitle(window.title);
            reportedWindow.setAnchorId(window.accessibilityIdOfAnchor);
            reportedWindow.setPictureInPicture(window.inPictureInPicture);
            reportedWindow.setDisplayId(window.displayId);
            reportedWindow.setTaskId(window.taskId);
            reportedWindow.setLocales(window.locales);
            int parentId = com.android.server.accessibility.AccessibilityWindowManager.this.findWindowIdLocked(userId, window.parentToken);
            if (parentId >= 0) {
                reportedWindow.setParentId(parentId);
            }
            if (window.childTokens != null) {
                int childCount = window.childTokens.size();
                for (int i = 0; i < childCount; i++) {
                    android.os.IBinder childToken = (android.os.IBinder) window.childTokens.get(i);
                    int childId = com.android.server.accessibility.AccessibilityWindowManager.this.findWindowIdLocked(userId, childToken);
                    if (childId >= 0) {
                        reportedWindow.addChild(childId);
                    }
                }
            }
            android.view.accessibility.AccessibilityWindowInfo oldWindowInfo = oldWindowsById.get(windowId);
            if (oldWindowInfo == null) {
                reportedWindow.setTransitionTimeMillis(android.os.SystemClock.uptimeMillis());
            } else {
                android.graphics.Region oldTouchRegion = new android.graphics.Region();
                oldWindowInfo.getRegionInScreen(oldTouchRegion);
                if (oldTouchRegion.equals(window.regionInScreen)) {
                    reportedWindow.setTransitionTimeMillis(oldWindowInfo.getTransitionTimeMillis());
                } else {
                    reportedWindow.setTransitionTimeMillis(android.os.SystemClock.uptimeMillis());
                }
            }
            return reportedWindow;
        }

        private int getTypeForWindowManagerWindowType(int windowType) {
            switch (windowType) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 1000:
                case 1001:
                case 1002:
                case 1003:
                case 1005:
                case com.android.server.camera.ICameraServiceProxyExt.MSG_FLOAT_WINDOW_SHOW /* 2002 */:
                case 2005:
                case 2007:
                case 2012:
                    return 1;
                case 2000:
                case 2001:
                case 2003:
                case 2006:
                case 2008:
                case 2009:
                case 2010:
                case 2017:
                case 2019:
                case 2020:
                case 2024:
                case 2036:
                case 2038:
                case 2040:
                case 2041:
                case 2098:
                case 2099:
                    return 3;
                case 2011:
                    return 2;
                case 2032:
                    return 4;
                case 2034:
                    return 5;
                case 2039:
                    return 6;
                default:
                    return -1;
            }
        }

        void dumpLocked(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (this.mIsProxy) {
                pw.println("Proxy accessibility focused window = " + this.mProxyDisplayAccessibilityFocusedWindow);
                pw.println();
            }
            if (this.mWindows != null) {
                int windowCount = this.mWindows.size();
                for (int j = 0; j < windowCount; j++) {
                    if (j == 0) {
                        pw.append("Display[");
                        pw.append((java.lang.CharSequence) java.lang.Integer.toString(this.mDisplayId));
                        pw.append("] : ");
                        pw.println();
                    }
                    if (j > 0) {
                        pw.append(',');
                        pw.println();
                    }
                    pw.append("A11yWindow[");
                    android.view.accessibility.AccessibilityWindowInfo window = this.mWindows.get(j);
                    pw.append((java.lang.CharSequence) window.toString());
                    pw.append(']');
                    pw.println();
                    android.view.WindowInfo windowInfo = findWindowInfoByIdLocked(window.getId());
                    if (windowInfo != null) {
                        pw.append("WindowInfo[");
                        pw.append((java.lang.CharSequence) windowInfo.toString());
                        pw.append("]");
                        pw.println();
                    }
                }
                pw.println();
            }
        }
    }

    public final class RemoteAccessibilityConnection implements android.os.IBinder.DeathRecipient {
        private final android.view.accessibility.IAccessibilityInteractionConnection mConnection;
        private final java.lang.String mPackageName;
        private final int mUid;
        private final int mUserId;
        private final int mWindowId;

        RemoteAccessibilityConnection(int windowId, android.view.accessibility.IAccessibilityInteractionConnection connection, java.lang.String packageName, int uid, int userId) {
            this.mWindowId = windowId;
            this.mPackageName = packageName;
            this.mUid = uid;
            this.mUserId = userId;
            this.mConnection = connection;
        }

        int getUid() {
            return this.mUid;
        }

        java.lang.String getPackageName() {
            return this.mPackageName;
        }

        android.view.accessibility.IAccessibilityInteractionConnection getRemote() {
            return this.mConnection;
        }

        void linkToDeath() throws android.os.RemoteException {
            this.mConnection.asBinder().linkToDeath(this, 0);
        }

        void unlinkToDeath() {
            this.mConnection.asBinder().unlinkToDeath(this, 0);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            unlinkToDeath();
            synchronized (com.android.server.accessibility.AccessibilityWindowManager.this.mLock) {
                com.android.server.accessibility.AccessibilityWindowManager.this.removeAccessibilityInteractionConnectionLocked(this.mWindowId, this.mUserId);
            }
        }
    }

    public AccessibilityWindowManager(java.lang.Object lock, android.os.Handler handler, com.android.server.wm.WindowManagerInternal windowManagerInternal, com.android.server.accessibility.AccessibilityWindowManager.AccessibilityEventSender accessibilityEventSender, com.android.server.accessibility.AccessibilitySecurityPolicy securityPolicy, com.android.server.accessibility.AccessibilitySecurityPolicy.AccessibilityUserManager accessibilityUserManager, com.android.server.accessibility.AccessibilityTraceManager traceManager) {
        this.mLock = lock;
        this.mHandler = handler;
        this.mWindowManagerInternal = windowManagerInternal;
        this.mAccessibilityEventSender = accessibilityEventSender;
        this.mSecurityPolicy = securityPolicy;
        this.mAccessibilityUserManager = accessibilityUserManager;
        this.mTraceManager = traceManager;
    }

    public void startTrackingWindows(int displayId, boolean proxyed) {
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = this.mDisplayWindowsObservers.get(displayId);
            if (observer == null) {
                observer = new com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver(displayId);
            }
            if (proxyed && !observer.mIsProxy) {
                observer.mIsProxy = true;
                this.mHasProxy = true;
            }
            if (observer.isTrackingWindowsLocked()) {
                return;
            }
            observer.startTrackingWindowsLocked();
            this.mDisplayWindowsObservers.put(displayId, observer);
        }
    }

    public void stopTrackingWindows(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = this.mDisplayWindowsObservers.get(displayId);
            if (observer != null) {
                observer.stopTrackingWindowsLocked();
                this.mDisplayWindowsObservers.remove(displayId);
            }
            resetHasProxyIfNeededLocked();
        }
    }

    public void stopTrackingDisplayProxy(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver proxyObserver = this.mDisplayWindowsObservers.get(displayId);
            if (proxyObserver != null) {
                proxyObserver.mIsProxy = false;
            }
            resetHasProxyIfNeededLocked();
        }
    }

    private void resetHasProxyIfNeededLocked() {
        boolean hasProxy = false;
        int count = this.mDisplayWindowsObservers.size();
        for (int i = 0; i < count; i++) {
            com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = this.mDisplayWindowsObservers.valueAt(i);
            if (observer != null && observer.mIsProxy) {
                hasProxy = true;
            }
        }
        this.mHasProxy = hasProxy;
    }

    public boolean isTrackingWindowsLocked() {
        int count = this.mDisplayWindowsObservers.size();
        if (count > 0) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isProxyed(int displayId) {
        com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = this.mDisplayWindowsObservers.get(displayId);
        return observer != null && observer.mIsProxy;
    }

    void moveNonProxyTopFocusedDisplayToTopIfNeeded() {
        if (this.mHasProxy && this.mLastNonProxyTopFocusedDisplayId != this.mTopFocusedDisplayId) {
            this.mWindowManagerInternal.moveDisplayToTopIfAllowed(this.mLastNonProxyTopFocusedDisplayId);
        }
    }

    int getLastNonProxyTopFocusedDisplayId() {
        return this.mLastNonProxyTopFocusedDisplayId;
    }

    public boolean isTrackingWindowsLocked(int displayId) {
        com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = this.mDisplayWindowsObservers.get(displayId);
        if (observer != null) {
            return observer.isTrackingWindowsLocked();
        }
        return false;
    }

    public java.util.List<android.view.accessibility.AccessibilityWindowInfo> getWindowListLocked(int displayId) {
        com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = this.mDisplayWindowsObservers.get(displayId);
        if (observer != null) {
            return observer.getWindowListLocked();
        }
        return null;
    }

    public int addAccessibilityInteractionConnection(android.view.IWindow window, android.os.IBinder leashToken, android.view.accessibility.IAccessibilityInteractionConnection connection, java.lang.String packageName, int userId) throws java.lang.Throwable {
        java.lang.Object obj;
        java.lang.String packageName2;
        int windowId;
        int displayId;
        android.os.IBinder token;
        boolean shouldComputeWindows;
        android.os.IBinder token2 = window.asBinder();
        if (traceWMEnabled()) {
            logTraceWM("getDisplayIdForWindow", "token=" + token2);
        }
        int displayId2 = this.mWindowManagerInternal.getDisplayIdForWindow(token2);
        java.lang.Object obj2 = this.mLock;
        synchronized (obj2) {
            try {
                int resolvedUserId = this.mSecurityPolicy.resolveCallingUserIdEnforcingPermissionsLocked(userId);
                int resolvedUid = android.os.UserHandle.getUid(resolvedUserId, android.os.UserHandle.getCallingAppId());
                try {
                    java.lang.String packageName3 = this.mSecurityPolicy.resolveValidReportedPackageLocked(packageName, android.os.UserHandle.getCallingAppId(), resolvedUserId, android.os.Binder.getCallingPid());
                    try {
                        int windowId2 = sNextWindowId;
                        sNextWindowId = windowId2 + 1;
                        if (this.mSecurityPolicy.isCallerInteractingAcrossUsers(userId)) {
                            try {
                                com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection wrapper = new com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection(windowId2, connection, packageName3, resolvedUid, -1);
                                wrapper.linkToDeath();
                                this.mGlobalInteractionConnections.put(windowId2, wrapper);
                                this.mGlobalWindowTokens.put(windowId2, token2);
                                if (DEBUG) {
                                    android.util.Slog.i(LOG_TAG, "Added global connection for pid:" + android.os.Binder.getCallingPid() + " with windowId: " + windowId2 + " and token: " + token2);
                                }
                                packageName2 = packageName3;
                                windowId = windowId2;
                                obj = obj2;
                                displayId = displayId2;
                                token = token2;
                            } catch (java.lang.Throwable th) {
                                th = th;
                                obj = obj2;
                                while (true) {
                                    try {
                                        throw th;
                                    } catch (java.lang.Throwable th2) {
                                        th = th2;
                                    }
                                }
                            }
                        } else {
                            windowId = windowId2;
                            obj = obj2;
                            displayId = displayId2;
                            packageName2 = packageName3;
                            token = token2;
                            try {
                                com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection wrapper2 = new com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection(windowId2, connection, packageName3, resolvedUid, resolvedUserId);
                                wrapper2.linkToDeath();
                                getInteractionConnectionsForUserLocked(resolvedUserId).put(windowId, wrapper2);
                                getWindowTokensForUserLocked(resolvedUserId).put(windowId, token);
                                if (DEBUG) {
                                    android.util.Slog.i(LOG_TAG, "Added user connection for pid:" + android.os.Binder.getCallingPid() + " with windowId: " + windowId + " and  token: " + token);
                                }
                            } catch (java.lang.Throwable th3) {
                                th = th3;
                                while (true) {
                                    throw th;
                                }
                            }
                        }
                        shouldComputeWindows = isTrackingWindowsLocked(displayId);
                    } catch (java.lang.Throwable th4) {
                        th = th4;
                        obj = obj2;
                    }
                } catch (java.lang.Throwable th5) {
                    th = th5;
                    obj = obj2;
                    while (true) {
                        throw th;
                    }
                }
            } catch (java.lang.Throwable th6) {
                th = th6;
            }
            try {
                registerIdLocked(leashToken, windowId);
                if (shouldComputeWindows) {
                    if (traceWMEnabled()) {
                        logTraceWM("computeWindowsForAccessibility", "displayId=" + displayId);
                    }
                    this.mWindowManagerInternal.computeWindowsForAccessibility(displayId);
                }
                if (traceWMEnabled()) {
                    logTraceWM("setAccessibilityIdToSurfaceMetadata", "token=" + token + ";windowId=" + windowId);
                }
                this.mWindowManagerInternal.setAccessibilityIdToSurfaceMetadata(token, windowId);
                return windowId;
            } catch (java.lang.Throwable th7) {
                th = th7;
                while (true) {
                    throw th;
                }
            }
        }
    }

    public void removeAccessibilityInteractionConnection(android.view.IWindow window) {
        synchronized (this.mLock) {
            this.mSecurityPolicy.resolveCallingUserIdEnforcingPermissionsLocked(android.os.UserHandle.getCallingUserId());
            android.os.IBinder token = window.asBinder();
            int removedWindowId = removeAccessibilityInteractionConnectionInternalLocked(token, this.mGlobalWindowTokens, this.mGlobalInteractionConnections);
            if (removedWindowId >= 0) {
                onAccessibilityInteractionConnectionRemovedLocked(removedWindowId, token);
                if (DEBUG) {
                    android.util.Slog.i(LOG_TAG, "Removed global connection for pid:" + android.os.Binder.getCallingPid() + " with windowId: " + removedWindowId + " and token: " + window.asBinder());
                }
                return;
            }
            int userCount = this.mWindowTokens.size();
            for (int i = 0; i < userCount; i++) {
                int userId = this.mWindowTokens.keyAt(i);
                int removedWindowIdForUser = removeAccessibilityInteractionConnectionInternalLocked(token, getWindowTokensForUserLocked(userId), getInteractionConnectionsForUserLocked(userId));
                if (removedWindowIdForUser >= 0) {
                    onAccessibilityInteractionConnectionRemovedLocked(removedWindowIdForUser, token);
                    if (DEBUG) {
                        android.util.Slog.i(LOG_TAG, "Removed user connection for pid:" + android.os.Binder.getCallingPid() + " with windowId: " + removedWindowIdForUser + " and userId:" + userId + " and token: " + window.asBinder());
                    }
                    return;
                }
            }
        }
    }

    public com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection getConnectionLocked(int userId, int windowId) {
        com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection connection = this.mGlobalInteractionConnections.get(windowId);
        if (connection == null && isValidUserForInteractionConnectionsLocked(userId)) {
            connection = getInteractionConnectionsForUserLocked(userId).get(windowId);
        }
        if (connection != null && connection.getRemote() != null) {
            return connection;
        }
        if (DEBUG) {
            android.util.Slog.e(LOG_TAG, "No interaction connection to window: " + windowId);
            return null;
        }
        return null;
    }

    private int removeAccessibilityInteractionConnectionInternalLocked(android.os.IBinder windowToken, android.util.SparseArray<android.os.IBinder> windowTokens, android.util.SparseArray<com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection> interactionConnections) {
        int count = windowTokens.size();
        for (int i = 0; i < count; i++) {
            if (windowTokens.valueAt(i) == windowToken) {
                int windowId = windowTokens.keyAt(i);
                windowTokens.removeAt(i);
                com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection wrapper = interactionConnections.get(windowId);
                wrapper.unlinkToDeath();
                interactionConnections.remove(windowId);
                return windowId;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeAccessibilityInteractionConnectionLocked(int windowId, int userId) {
        android.os.IBinder window = null;
        if (userId == -1) {
            android.os.IBinder window2 = this.mGlobalWindowTokens.get(windowId);
            window = window2;
            this.mGlobalWindowTokens.remove(windowId);
            this.mGlobalInteractionConnections.remove(windowId);
        } else {
            if (isValidUserForWindowTokensLocked(userId)) {
                android.os.IBinder window3 = getWindowTokensForUserLocked(userId).get(windowId);
                window = window3;
                getWindowTokensForUserLocked(userId).remove(windowId);
            }
            if (isValidUserForInteractionConnectionsLocked(userId)) {
                getInteractionConnectionsForUserLocked(userId).remove(windowId);
            }
        }
        onAccessibilityInteractionConnectionRemovedLocked(windowId, window);
        if (DEBUG) {
            android.util.Slog.i(LOG_TAG, "Removing interaction connection to windowId: " + windowId);
        }
    }

    private void onAccessibilityInteractionConnectionRemovedLocked(int windowId, android.os.IBinder binder) {
        if (!isTrackingWindowsLocked() && windowId >= 0 && this.mActiveWindowId == windowId) {
            this.mActiveWindowId = -1;
        }
        if (binder != null) {
            if (traceWMEnabled()) {
                logTraceWM("setAccessibilityIdToSurfaceMetadata", "token=" + binder + ";windowId=AccessibilityWindowInfo.UNDEFINED_WINDOW_ID");
            }
            this.mWindowManagerInternal.setAccessibilityIdToSurfaceMetadata(binder, -1);
        }
        unregisterIdLocked(windowId);
        this.mWindowAttributes.remove(windowId);
    }

    public android.os.IBinder getWindowTokenForUserAndWindowIdLocked(int userId, int windowId) {
        android.os.IBinder windowToken = this.mGlobalWindowTokens.get(windowId);
        if (windowToken == null && isValidUserForWindowTokensLocked(userId)) {
            return getWindowTokensForUserLocked(userId).get(windowId);
        }
        return windowToken;
    }

    public int getWindowOwnerUserId(android.os.IBinder windowToken) {
        if (traceWMEnabled()) {
            logTraceWM("getWindowOwnerUserId", "token=" + windowToken);
        }
        return this.mWindowManagerInternal.getWindowOwnerUserId(windowToken);
    }

    public int findWindowIdLocked(int userId, android.os.IBinder token) {
        int userIndex;
        int globalIndex = this.mGlobalWindowTokens.indexOfValue(token);
        if (globalIndex >= 0) {
            return this.mGlobalWindowTokens.keyAt(globalIndex);
        }
        if (isValidUserForWindowTokensLocked(userId) && (userIndex = getWindowTokensForUserLocked(userId).indexOfValue(token)) >= 0) {
            return getWindowTokensForUserLocked(userId).keyAt(userIndex);
        }
        return -1;
    }

    public void associateEmbeddedHierarchyLocked(android.os.IBinder host, android.os.IBinder embedded) {
        associateLocked(embedded, host);
    }

    public void disassociateEmbeddedHierarchyLocked(android.os.IBinder token) {
        disassociateLocked(token);
    }

    public int resolveParentWindowIdLocked(int windowId) {
        android.os.IBinder token = getLeashTokenLocked(windowId);
        if (token == null) {
            return windowId;
        }
        android.os.IBinder resolvedToken = resolveTopParentTokenLocked(token);
        int resolvedWindowId = getWindowIdLocked(resolvedToken);
        return resolvedWindowId != -1 ? resolvedWindowId : windowId;
    }

    private android.os.IBinder resolveTopParentTokenLocked(android.os.IBinder token) {
        android.os.IBinder hostToken = getHostTokenLocked(token);
        if (hostToken == null) {
            return token;
        }
        return resolveTopParentTokenLocked(hostToken);
    }

    public boolean computePartialInteractiveRegionForWindowLocked(int windowId, android.graphics.Region outRegion) {
        int parentWindowId = resolveParentWindowIdLocked(windowId);
        com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = getDisplayWindowObserverByWindowIdLocked(parentWindowId);
        if (observer != null) {
            return observer.computePartialInteractiveRegionForWindowLocked(parentWindowId, parentWindowId != windowId, outRegion);
        }
        return false;
    }

    public void updateActiveAndAccessibilityFocusedWindowLocked(int userId, int windowId, long nodeId, int eventType, int eventAction) {
        switch (eventType) {
            case 32:
                synchronized (this.mLock) {
                    if (!isTrackingWindowsLocked()) {
                        this.mTopFocusedWindowId = findFocusedWindowId(userId);
                        if (windowId == this.mTopFocusedWindowId) {
                            this.mActiveWindowId = windowId;
                        }
                    }
                    break;
                }
                return;
            case 128:
                synchronized (this.mLock) {
                    if (this.mTouchInteractionInProgress && this.mActiveWindowId != windowId) {
                        setActiveWindowLocked(windowId);
                    }
                    break;
                }
                return;
            case 32768:
                synchronized (this.mLock) {
                    if (this.mHasProxy && setProxyFocusLocked(windowId)) {
                        return;
                    }
                    if (this.mAccessibilityFocusedWindowId != windowId) {
                        clearAccessibilityFocusLocked(this.mAccessibilityFocusedWindowId);
                        setAccessibilityFocusedWindowLocked(windowId);
                    }
                    this.mAccessibilityFocusNodeId = nodeId;
                    return;
                }
            case 65536:
                synchronized (this.mLock) {
                    if (this.mHasProxy && clearProxyFocusLocked(windowId, eventAction)) {
                        return;
                    }
                    if (this.mAccessibilityFocusNodeId == nodeId) {
                        this.mAccessibilityFocusNodeId = 2147483647L;
                    }
                    if (this.mAccessibilityFocusNodeId == 2147483647L && this.mAccessibilityFocusedWindowId == windowId && eventAction != 64) {
                        this.mAccessibilityFocusedWindowId = -1;
                        this.mAccessibilityFocusedDisplayId = -1;
                    }
                    return;
                }
            default:
                return;
        }
    }

    public void onTouchInteractionStart() {
        synchronized (this.mLock) {
            this.mTouchInteractionInProgress = true;
        }
    }

    public void onTouchInteractionEnd() {
        synchronized (this.mLock) {
            this.mTouchInteractionInProgress = false;
            int oldActiveWindow = this.mActiveWindowId;
            setActiveWindowLocked(this.mTopFocusedWindowId);
            if (oldActiveWindow != this.mActiveWindowId && this.mAccessibilityFocusedWindowId == oldActiveWindow && accessibilityFocusOnlyInActiveWindowLocked()) {
                clearAccessibilityFocusLocked(oldActiveWindow);
            }
        }
    }

    public int getActiveWindowId(int userId) {
        if (this.mActiveWindowId == -1 && !this.mTouchInteractionInProgress) {
            this.mActiveWindowId = findFocusedWindowId(userId);
        }
        return this.mActiveWindowId;
    }

    private void setActiveWindowLocked(int windowId) {
        com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer;
        if (this.mActiveWindowId != windowId) {
            java.util.List<android.view.accessibility.AccessibilityEvent> events = new java.util.ArrayList<>(2);
            if (this.mActiveWindowId != -1 && (observer = getDisplayWindowObserverByWindowIdLocked(this.mActiveWindowId)) != null) {
                events.add(android.view.accessibility.AccessibilityEvent.obtainWindowsChangedEvent(observer.mDisplayId, this.mActiveWindowId, 32));
            }
            this.mActiveWindowId = windowId;
            int count = this.mDisplayWindowsObservers.size();
            for (int i = 0; i < count; i++) {
                com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer2 = this.mDisplayWindowsObservers.valueAt(i);
                if (observer2 != null && observer2.setActiveWindowLocked(windowId)) {
                    events.add(android.view.accessibility.AccessibilityEvent.obtainWindowsChangedEvent(observer2.mDisplayId, windowId, 32));
                }
            }
            for (android.view.accessibility.AccessibilityEvent event : events) {
                this.mAccessibilityEventSender.sendAccessibilityEventForCurrentUserLocked(event);
            }
        }
    }

    private void setAccessibilityFocusedWindowLocked(int windowId) {
        if (this.mAccessibilityFocusedWindowId != windowId) {
            java.util.List<android.view.accessibility.AccessibilityEvent> events = new java.util.ArrayList<>(2);
            if (this.mAccessibilityFocusedDisplayId != -1 && this.mAccessibilityFocusedWindowId != -1) {
                events.add(android.view.accessibility.AccessibilityEvent.obtainWindowsChangedEvent(this.mAccessibilityFocusedDisplayId, this.mAccessibilityFocusedWindowId, 128));
            }
            this.mAccessibilityFocusedWindowId = windowId;
            int count = this.mDisplayWindowsObservers.size();
            for (int i = 0; i < count; i++) {
                com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = this.mDisplayWindowsObservers.valueAt(i);
                if (observer != null && observer.setAccessibilityFocusedWindowLocked(windowId)) {
                    this.mAccessibilityFocusedDisplayId = observer.mDisplayId;
                    events.add(android.view.accessibility.AccessibilityEvent.obtainWindowsChangedEvent(observer.mDisplayId, windowId, 128));
                }
            }
            for (android.view.accessibility.AccessibilityEvent event : events) {
                this.mAccessibilityEventSender.sendAccessibilityEventForCurrentUserLocked(event);
            }
        }
    }

    public android.view.accessibility.AccessibilityWindowInfo findA11yWindowInfoByIdLocked(int windowId) {
        int windowId2 = resolveParentWindowIdLocked(windowId);
        com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = getDisplayWindowObserverByWindowIdLocked(windowId2);
        if (observer != null) {
            return observer.findA11yWindowInfoByIdLocked(windowId2);
        }
        return null;
    }

    public android.view.WindowInfo findWindowInfoByIdLocked(int windowId) {
        int windowId2 = resolveParentWindowIdLocked(windowId);
        com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = getDisplayWindowObserverByWindowIdLocked(windowId2);
        if (observer != null) {
            return observer.findWindowInfoByIdLocked(windowId2);
        }
        return null;
    }

    public int getFocusedWindowId(int focusType) {
        return getFocusedWindowId(focusType, -1);
    }

    public int getFocusedWindowId(int focusType, int displayId) {
        if (displayId == -1 || displayId == 0 || !this.mHasProxy) {
            return getDefaultFocus(focusType);
        }
        com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = this.mDisplayWindowsObservers.get(displayId);
        if (observer != null && observer.mIsProxy) {
            return getProxyFocus(focusType, observer);
        }
        return getDefaultFocus(focusType);
    }

    private int getDefaultFocus(int focusType) {
        if (focusType == 1) {
            return this.mTopFocusedWindowId;
        }
        if (focusType == 2) {
            return this.mAccessibilityFocusedWindowId;
        }
        return -1;
    }

    private int getProxyFocus(int focusType, com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer) {
        if (focusType == 1) {
            return this.mTopFocusedWindowId;
        }
        if (focusType == 2) {
            return observer.mProxyDisplayAccessibilityFocusedWindow;
        }
        return -1;
    }

    public android.view.accessibility.AccessibilityWindowInfo getPictureInPictureWindowLocked() {
        android.view.accessibility.AccessibilityWindowInfo windowInfo = null;
        int count = this.mDisplayWindowsObservers.size();
        for (int i = 0; i < count; i++) {
            com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = this.mDisplayWindowsObservers.valueAt(i);
            if (observer != null) {
                android.view.accessibility.AccessibilityWindowInfo pictureInPictureWindowLocked = observer.getPictureInPictureWindowLocked();
                windowInfo = pictureInPictureWindowLocked;
                if (pictureInPictureWindowLocked != null) {
                    break;
                }
            }
        }
        return windowInfo;
    }

    public void setPictureInPictureActionReplacingConnection(android.view.accessibility.IAccessibilityInteractionConnection connection) throws android.os.RemoteException {
        synchronized (this.mLock) {
            if (this.mPictureInPictureActionReplacingConnection != null) {
                this.mPictureInPictureActionReplacingConnection.unlinkToDeath();
                this.mPictureInPictureActionReplacingConnection = null;
            }
            if (connection != null) {
                com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection wrapper = new com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection(-3, connection, "foo.bar.baz", 1000, -1);
                this.mPictureInPictureActionReplacingConnection = wrapper;
                wrapper.linkToDeath();
            }
        }
    }

    public com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection getPictureInPictureActionReplacingConnection() {
        return this.mPictureInPictureActionReplacingConnection;
    }

    public void notifyOutsideTouch(int userId, int targetWindowId) {
        java.util.List<com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection> connectionList = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = getDisplayWindowObserverByWindowIdLocked(targetWindowId);
            if (observer != null) {
                java.util.List<java.lang.Integer> outsideWindowsIds = observer.getWatchOutsideTouchWindowIdLocked(targetWindowId);
                for (int i = 0; i < outsideWindowsIds.size(); i++) {
                    connectionList.add(getConnectionLocked(userId, outsideWindowsIds.get(i).intValue()));
                }
            }
        }
        for (int i2 = 0; i2 < connectionList.size(); i2++) {
            com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection connection = connectionList.get(i2);
            if (connection != null) {
                if (traceIntConnEnabled()) {
                    logTraceIntConn("notifyOutsideTouch");
                }
                try {
                    connection.getRemote().notifyOutsideTouch();
                } catch (android.os.RemoteException e) {
                    if (DEBUG) {
                        android.util.Slog.e(LOG_TAG, "Error calling notifyOutsideTouch()");
                    }
                }
            }
        }
    }

    public int getDisplayIdByUserIdAndWindowId(int userId, int windowId) {
        android.os.IBinder windowToken;
        synchronized (this.mLock) {
            windowToken = getWindowTokenForUserAndWindowIdLocked(userId, windowId);
        }
        if (traceWMEnabled()) {
            logTraceWM("getDisplayIdForWindow", "token=" + windowToken);
        }
        int displayId = this.mWindowManagerInternal.getDisplayIdForWindow(windowToken);
        return displayId;
    }

    public java.util.ArrayList<java.lang.Integer> getDisplayListLocked(int displayTypes) {
        java.util.ArrayList<java.lang.Integer> displayList = new java.util.ArrayList<>();
        int count = this.mDisplayWindowsObservers.size();
        for (int i = 0; i < count; i++) {
            com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = this.mDisplayWindowsObservers.valueAt(i);
            if (observer != null) {
                if (!observer.mIsProxy && (displayTypes & 1) != 0) {
                    displayList.add(java.lang.Integer.valueOf(observer.mDisplayId));
                } else if (observer.mIsProxy && (displayTypes & 2) != 0) {
                    displayList.add(java.lang.Integer.valueOf(observer.mDisplayId));
                }
            }
        }
        return displayList;
    }

    boolean accessibilityFocusOnlyInActiveWindowLocked() {
        return !isTrackingWindowsLocked();
    }

    private int findFocusedWindowId(int userId) {
        int iFindWindowIdLocked;
        if (traceWMEnabled()) {
            logTraceWM("getFocusedWindowToken", "");
        }
        android.os.IBinder token = this.mWindowManagerInternal.getFocusedWindowTokenFromWindowStates();
        synchronized (this.mLock) {
            iFindWindowIdLocked = findWindowIdLocked(userId, token);
        }
        return iFindWindowIdLocked;
    }

    private boolean isValidUserForInteractionConnectionsLocked(int userId) {
        return this.mInteractionConnections.indexOfKey(userId) >= 0;
    }

    private boolean isValidUserForWindowTokensLocked(int userId) {
        return this.mWindowTokens.indexOfKey(userId) >= 0;
    }

    private android.util.SparseArray<com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection> getInteractionConnectionsForUserLocked(int userId) {
        android.util.SparseArray<com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection> connection = this.mInteractionConnections.get(userId);
        if (connection == null) {
            android.util.SparseArray<com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection> connection2 = new android.util.SparseArray<>();
            this.mInteractionConnections.put(userId, connection2);
            return connection2;
        }
        return connection;
    }

    private android.util.SparseArray<android.os.IBinder> getWindowTokensForUserLocked(int userId) {
        android.util.SparseArray<android.os.IBinder> windowTokens = this.mWindowTokens.get(userId);
        if (windowTokens == null) {
            android.util.SparseArray<android.os.IBinder> windowTokens2 = new android.util.SparseArray<>();
            this.mWindowTokens.put(userId, windowTokens2);
            return windowTokens2;
        }
        return windowTokens;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearAccessibilityFocusLocked(int windowId) {
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.accessibility.AccessibilityWindowManager$$ExternalSyntheticLambda0
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                ((com.android.server.accessibility.AccessibilityWindowManager) obj).clearAccessibilityFocusMainThread(((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue());
            }
        }, this, java.lang.Integer.valueOf(this.mAccessibilityUserManager.getCurrentUserIdLocked()), java.lang.Integer.valueOf(windowId)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearAccessibilityFocusMainThread(int userId, int windowId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityWindowManager.RemoteAccessibilityConnection connection = getConnectionLocked(userId, windowId);
            if (connection == null) {
                return;
            }
            if (traceIntConnEnabled()) {
                logTraceIntConn("notifyOutsideTouch");
            }
            try {
                connection.getRemote().clearAccessibilityFocus();
            } catch (android.os.RemoteException e) {
                if (DEBUG) {
                    android.util.Slog.e(LOG_TAG, "Error calling clearAccessibilityFocus()");
                }
            }
        }
    }

    private com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver getDisplayWindowObserverByWindowIdLocked(int windowId) {
        int count = this.mDisplayWindowsObservers.size();
        for (int i = 0; i < count; i++) {
            com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = this.mDisplayWindowsObservers.valueAt(i);
            if (observer != null && observer.findWindowInfoByIdLocked(windowId) != null) {
                return this.mDisplayWindowsObservers.get(observer.mDisplayId);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean traceWMEnabled() {
        return this.mTraceManager.isA11yTracingEnabledForTypes(512L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logTraceWM(java.lang.String methodName, java.lang.String params) {
        this.mTraceManager.logTrace("WindowManagerInternal." + methodName, 512L, params);
    }

    private boolean traceIntConnEnabled() {
        return this.mTraceManager.isA11yTracingEnabledForTypes(16L);
    }

    private void logTraceIntConn(java.lang.String methodName) {
        this.mTraceManager.logTrace("AccessibilityWindowManager." + methodName, 16L);
    }

    void associateLocked(android.os.IBinder embedded, android.os.IBinder host) {
        this.mHostEmbeddedMap.put(embedded, host);
    }

    void disassociateLocked(android.os.IBinder token) {
        this.mHostEmbeddedMap.remove(token);
        for (int i = this.mHostEmbeddedMap.size() - 1; i >= 0; i--) {
            if (this.mHostEmbeddedMap.valueAt(i).equals(token)) {
                this.mHostEmbeddedMap.removeAt(i);
            }
        }
    }

    void registerIdLocked(android.os.IBinder token, int windowId) {
        this.mWindowIdMap.put(windowId, token);
    }

    void unregisterIdLocked(int windowId) {
        android.os.IBinder token = this.mWindowIdMap.get(windowId);
        if (token == null) {
            return;
        }
        disassociateLocked(token);
        this.mWindowIdMap.remove(windowId);
    }

    android.os.IBinder getLeashTokenLocked(int windowId) {
        return this.mWindowIdMap.get(windowId);
    }

    int getWindowIdLocked(android.os.IBinder token) {
        int index = this.mWindowIdMap.indexOfValue(token);
        if (index == -1) {
            return index;
        }
        return this.mWindowIdMap.keyAt(index);
    }

    android.os.IBinder getHostTokenLocked(android.os.IBinder token) {
        return this.mHostEmbeddedMap.get(token);
    }

    boolean isEmbeddedHierarchyWindowsLocked(int windowId) {
        android.os.IBinder leashToken;
        if (this.mHostEmbeddedMap.size() == 0 || (leashToken = getLeashTokenLocked(windowId)) == null) {
            return false;
        }
        return this.mHostEmbeddedMap.containsKey(leashToken);
    }

    private boolean clearProxyFocusLocked(int focusClearedWindowId, int eventAction) {
        if (eventAction == 64) {
            return false;
        }
        for (int i = 0; i < this.mDisplayWindowsObservers.size(); i++) {
            com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = this.mDisplayWindowsObservers.get(i);
            if (observer != null && observer.mWindows != null && observer.mIsProxy) {
                int windowCount = observer.mWindows.size();
                for (int j = 0; j < windowCount; j++) {
                    android.view.accessibility.AccessibilityWindowInfo window = (android.view.accessibility.AccessibilityWindowInfo) observer.mWindows.get(j);
                    if (window.getId() == focusClearedWindowId) {
                        observer.mProxyDisplayAccessibilityFocusedWindow = -1;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean setProxyFocusLocked(int focusedWindowId) {
        for (int i = 0; i < this.mDisplayWindowsObservers.size(); i++) {
            com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = this.mDisplayWindowsObservers.valueAt(i);
            if (observer != null && observer.mIsProxy && observer.setAccessibilityFocusedWindowLocked(focusedWindowId)) {
                int previouslyFocusedWindowId = observer.mProxyDisplayAccessibilityFocusedWindow;
                if (previouslyFocusedWindowId == focusedWindowId) {
                    return true;
                }
                if (previouslyFocusedWindowId != -1) {
                    clearAccessibilityFocusLocked(previouslyFocusedWindowId);
                    this.mAccessibilityEventSender.sendAccessibilityEventForCurrentUserLocked(android.view.accessibility.AccessibilityEvent.obtainWindowsChangedEvent(observer.mDisplayId, previouslyFocusedWindowId, 128));
                }
                observer.mProxyDisplayAccessibilityFocusedWindow = focusedWindowId;
                this.mAccessibilityEventSender.sendAccessibilityEventForCurrentUserLocked(android.view.accessibility.AccessibilityEvent.obtainWindowsChangedEvent(observer.mDisplayId, observer.mProxyDisplayAccessibilityFocusedWindow, 128));
                return true;
            }
        }
        return false;
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        pw.append("Global Info [ ");
        pw.println("Top focused display Id = " + this.mTopFocusedDisplayId);
        pw.println("     Active Window Id = " + this.mActiveWindowId);
        pw.println("     Top Focused Window Id = " + this.mTopFocusedWindowId);
        pw.println("     Accessibility Focused Window Id = " + this.mAccessibilityFocusedWindowId + " ]");
        pw.println();
        int count = this.mDisplayWindowsObservers.size();
        for (int i = 0; i < count; i++) {
            com.android.server.accessibility.AccessibilityWindowManager.DisplayWindowsObserver observer = this.mDisplayWindowsObservers.valueAt(i);
            if (observer != null) {
                observer.dumpLocked(fd, pw, args);
            }
        }
        pw.println();
        pw.append("Window attributes:[");
        pw.append((java.lang.CharSequence) this.mWindowAttributes.toString());
        pw.append("]");
        pw.println();
    }
}
