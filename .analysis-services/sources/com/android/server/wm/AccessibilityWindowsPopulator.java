package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public final class AccessibilityWindowsPopulator extends android.window.WindowInfosListener {
    private static final int SURFACE_FLINGER_CALLBACK_WINDOWS_STABLE_TIMES_MS = 35;
    private static final int WINDOWS_CHANGED_NOTIFICATION_MAX_DURATION_TIMES_MS = 450;
    private final com.android.server.wm.AccessibilityController mAccessibilityController;
    private final android.os.Handler mHandler;
    private final com.android.server.wm.WindowManagerService mService;
    private static final java.lang.String TAG = com.android.server.wm.AccessibilityWindowsPopulator.class.getSimpleName();
    private static final float[] sTempFloats = new float[9];
    private static final boolean AGINGTEST = "1".equals(android.os.SystemProperties.get("persist.sys.agingtest", "0"));
    private final android.util.SparseArray<java.util.List<android.view.InputWindowHandle>> mInputWindowHandlesOnDisplays = new android.util.SparseArray<>();
    private final android.util.SparseArray<android.graphics.Matrix> mMagnificationSpecInverseMatrix = new android.util.SparseArray<>();
    private final android.util.SparseArray<android.window.WindowInfosListener.DisplayInfo> mDisplayInfos = new android.util.SparseArray<>();
    private final android.util.SparseArray<android.view.MagnificationSpec> mCurrentMagnificationSpec = new android.util.SparseArray<>();
    private final android.util.SparseArray<android.view.MagnificationSpec> mPreviousMagnificationSpec = new android.util.SparseArray<>();
    private final java.util.List<android.view.InputWindowHandle> mVisibleWindows = new java.util.ArrayList();
    private boolean mWindowsNotificationEnabled = false;
    private final java.util.Map<android.os.IBinder, android.graphics.Matrix> mWindowsTransformMatrixMap = new java.util.HashMap();
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.graphics.Matrix mTempMatrix1 = new android.graphics.Matrix();
    private final android.graphics.Matrix mTempMatrix2 = new android.graphics.Matrix();
    private final float[] mTempFloat1 = new float[9];
    private final float[] mTempFloat2 = new float[9];
    private final float[] mTempFloat3 = new float[9];

    AccessibilityWindowsPopulator(com.android.server.wm.WindowManagerService service, com.android.server.wm.AccessibilityController accessibilityController) {
        this.mService = service;
        this.mAccessibilityController = accessibilityController;
        this.mHandler = new com.android.server.wm.AccessibilityWindowsPopulator.MyHandler(this.mService.mH.getLooper());
    }

    public void populateVisibleWindowsOnScreenLocked(int displayId, java.util.List<com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow> outWindows) {
        android.graphics.Matrix inverseMatrix = new android.graphics.Matrix();
        android.graphics.Matrix displayMatrix = new android.graphics.Matrix();
        synchronized (this.mLock) {
            java.util.List<android.view.InputWindowHandle> inputWindowHandles = this.mInputWindowHandlesOnDisplays.get(displayId);
            if (inputWindowHandles == null) {
                outWindows.clear();
                return;
            }
            inverseMatrix.set(this.mMagnificationSpecInverseMatrix.get(displayId));
            android.window.WindowInfosListener.DisplayInfo displayInfo = this.mDisplayInfos.get(displayId);
            if (displayInfo != null) {
                displayMatrix.set(displayInfo.mTransform);
            } else {
                android.util.Slog.w(TAG, "The displayInfo of this displayId (" + displayId + ") called back from the surface fligner is null");
            }
            com.android.server.wm.DisplayContent dc = this.mService.mRoot.getDisplayContent(displayId);
            com.android.server.wm.ShellRoot shellroot = dc.mShellRoots.get(1);
            android.os.IBinder pipMenuIBinder = shellroot != null ? shellroot.getAccessibilityWindowToken() : null;
            for (android.view.InputWindowHandle windowHandle : inputWindowHandles) {
                com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow accessibilityWindow = com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow.initializeData(this.mService, windowHandle, inverseMatrix, pipMenuIBinder, displayMatrix);
                outWindows.add(accessibilityWindow);
            }
        }
    }

    public void onWindowInfosChanged(final android.view.InputWindowHandle[] windowHandles, final android.window.WindowInfosListener.DisplayInfo[] displayInfos) {
        if (AGINGTEST) {
            lambda$onWindowInfosChanged$0(windowHandles, displayInfos);
        } else if (com.android.server.accessibility.Flags.removeOnWindowInfosChangedHandler()) {
            lambda$onWindowInfosChanged$0(windowHandles, displayInfos);
        } else {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.AccessibilityWindowsPopulator$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onWindowInfosChanged$0(windowHandles, displayInfos);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onWindowInfosChangedInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$onWindowInfosChanged$0(android.view.InputWindowHandle[] windowHandles, android.window.WindowInfosListener.DisplayInfo[] displayInfos) {
        java.util.List<android.view.InputWindowHandle> tempVisibleWindows = new java.util.ArrayList<>();
        for (android.view.InputWindowHandle window : windowHandles) {
            boolean visible = (window.inputConfig & 2) == 0;
            boolean isNotClone = (window.inputConfig & 65536) == 0;
            boolean hasTouchableRegion = !window.touchableRegion.isEmpty();
            boolean hasNonEmptyFrame = true ^ window.frame.isEmpty();
            if (visible && isNotClone && hasTouchableRegion && hasNonEmptyFrame) {
                tempVisibleWindows.add(window);
            }
        }
        java.util.HashMap<android.os.IBinder, android.graphics.Matrix> windowsTransformMatrixMap = getWindowsTransformMatrix(tempVisibleWindows);
        synchronized (this.mLock) {
            this.mWindowsTransformMatrixMap.clear();
            this.mWindowsTransformMatrixMap.putAll(windowsTransformMatrixMap);
            this.mVisibleWindows.clear();
            this.mVisibleWindows.addAll(tempVisibleWindows);
            this.mDisplayInfos.clear();
            for (android.window.WindowInfosListener.DisplayInfo displayInfo : displayInfos) {
                this.mDisplayInfos.put(displayInfo.mDisplayId, displayInfo);
            }
            if (this.mWindowsNotificationEnabled) {
                if (!this.mHandler.hasMessages(3)) {
                    this.mHandler.sendEmptyMessageDelayed(3, 450L);
                }
                populateVisibleWindowHandlesAndNotifyWindowsChangeIfNeeded();
            }
        }
    }

    private java.util.HashMap<android.os.IBinder, android.graphics.Matrix> getWindowsTransformMatrix(java.util.List<android.view.InputWindowHandle> windows) {
        java.util.HashMap<android.os.IBinder, android.graphics.Matrix> windowsTransformMatrixMap;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                windowsTransformMatrixMap = new java.util.HashMap<>();
                for (android.view.InputWindowHandle inputWindowHandle : windows) {
                    android.os.IBinder iWindow = inputWindowHandle.getWindowToken();
                    com.android.server.wm.WindowState windowState = iWindow != null ? this.mService.mWindowMap.get(iWindow) : null;
                    if (windowState != null && windowState.shouldMagnify()) {
                        android.graphics.Matrix transformMatrix = new android.graphics.Matrix();
                        windowState.getTransformationMatrix(sTempFloats, transformMatrix);
                        windowsTransformMatrixMap.put(iWindow, transformMatrix);
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return windowsTransformMatrixMap;
    }

    public void setWindowsNotification(boolean register) {
        synchronized (this.mLock) {
            if (this.mWindowsNotificationEnabled == register) {
                return;
            }
            this.mWindowsNotificationEnabled = register;
            if (this.mWindowsNotificationEnabled) {
                android.util.Pair<android.view.InputWindowHandle[], android.window.WindowInfosListener.DisplayInfo[]> info = register();
                lambda$onWindowInfosChanged$0((android.view.InputWindowHandle[]) info.first, (android.window.WindowInfosListener.DisplayInfo[]) info.second);
            } else {
                unregister();
                releaseResources();
            }
        }
    }

    public void setMagnificationSpec(int displayId, android.view.MagnificationSpec spec) {
        synchronized (this.mLock) {
            android.view.MagnificationSpec currentMagnificationSpec = this.mCurrentMagnificationSpec.get(displayId);
            if (currentMagnificationSpec == null) {
                android.view.MagnificationSpec currentMagnificationSpec2 = new android.view.MagnificationSpec();
                currentMagnificationSpec2.setTo(spec);
                this.mCurrentMagnificationSpec.put(displayId, currentMagnificationSpec2);
            } else {
                android.view.MagnificationSpec previousMagnificationSpec = this.mPreviousMagnificationSpec.get(displayId);
                if (previousMagnificationSpec == null) {
                    previousMagnificationSpec = new android.view.MagnificationSpec();
                    this.mPreviousMagnificationSpec.put(displayId, previousMagnificationSpec);
                }
                previousMagnificationSpec.setTo(currentMagnificationSpec);
                currentMagnificationSpec.setTo(spec);
            }
        }
    }

    private void populateVisibleWindowHandlesAndNotifyWindowsChangeIfNeeded() {
        android.util.SparseArray<java.util.List<android.view.InputWindowHandle>> tempWindowHandleList = new android.util.SparseArray<>();
        synchronized (this.mLock) {
            for (android.view.InputWindowHandle windowHandle : this.mVisibleWindows) {
                java.util.List<android.view.InputWindowHandle> inputWindowHandles = tempWindowHandleList.get(windowHandle.displayId);
                if (inputWindowHandles == null) {
                    inputWindowHandles = new java.util.ArrayList();
                    tempWindowHandleList.put(windowHandle.displayId, inputWindowHandles);
                }
                inputWindowHandles.add(windowHandle);
            }
        }
        findMagnificationSpecInverseMatrixIfNeeded(tempWindowHandleList);
        java.util.List<java.lang.Integer> displayIdsForWindowsChanged = new java.util.ArrayList<>();
        getDisplaysForWindowsChanged(displayIdsForWindowsChanged, tempWindowHandleList, this.mInputWindowHandlesOnDisplays);
        this.mInputWindowHandlesOnDisplays.clear();
        for (int i = 0; i < tempWindowHandleList.size(); i++) {
            int displayId = tempWindowHandleList.keyAt(i);
            this.mInputWindowHandlesOnDisplays.put(displayId, tempWindowHandleList.get(displayId));
        }
        if (!displayIdsForWindowsChanged.isEmpty()) {
            if (!this.mHandler.hasMessages(1)) {
                this.mHandler.obtainMessage(1, displayIdsForWindowsChanged).sendToTarget();
            }
        } else {
            this.mHandler.removeMessages(2);
            this.mHandler.sendEmptyMessageDelayed(2, 35L);
        }
    }

    private static void getDisplaysForWindowsChanged(java.util.List<java.lang.Integer> outDisplayIdsForWindowsChanged, android.util.SparseArray<java.util.List<android.view.InputWindowHandle>> newWindowsList, android.util.SparseArray<java.util.List<android.view.InputWindowHandle>> oldWindowsList) {
        for (int i = 0; i < newWindowsList.size(); i++) {
            int displayId = newWindowsList.keyAt(i);
            java.util.List<android.view.InputWindowHandle> newWindows = newWindowsList.get(displayId);
            java.util.List<android.view.InputWindowHandle> oldWindows = oldWindowsList.get(displayId);
            if (hasWindowsChanged(newWindows, oldWindows)) {
                outDisplayIdsForWindowsChanged.add(java.lang.Integer.valueOf(displayId));
            }
        }
    }

    private static boolean hasWindowsChanged(java.util.List<android.view.InputWindowHandle> newWindows, java.util.List<android.view.InputWindowHandle> oldWindows) {
        if (oldWindows == null || oldWindows.size() != newWindows.size()) {
            return true;
        }
        int windowsCount = newWindows.size();
        int i = 0;
        while (true) {
            if (i >= windowsCount) {
                return false;
            }
            android.os.IBinder newWindowToken = newWindows.get(i).getWindowToken();
            android.os.IBinder oldWindowToken = oldWindows.get(i).getWindowToken();
            boolean hasNewWindowToken = newWindowToken != null;
            boolean hasOldWindowToken = oldWindowToken != null;
            if (hasNewWindowToken != hasOldWindowToken) {
                return true;
            }
            if (hasNewWindowToken && hasOldWindowToken && !newWindowToken.equals(oldWindowToken)) {
                return true;
            }
            i++;
        }
    }

    private void findMagnificationSpecInverseMatrixIfNeeded(android.util.SparseArray<java.util.List<android.view.InputWindowHandle>> windowHandleList) {
        for (int i = 0; i < windowHandleList.size(); i++) {
            int displayId = windowHandleList.keyAt(i);
            java.util.List<android.view.InputWindowHandle> inputWindowHandles = windowHandleList.get(displayId);
            android.view.MagnificationSpec currentSpec = this.mCurrentMagnificationSpec.get(displayId);
            if (currentSpec != null) {
                android.view.MagnificationSpec currentMagnificationSpec = new android.view.MagnificationSpec();
                currentMagnificationSpec.setTo(currentSpec);
                android.view.MagnificationSpec previousSpec = this.mPreviousMagnificationSpec.get(displayId);
                if (previousSpec == null) {
                    android.graphics.Matrix inverseMatrixForCurrentSpec = new android.graphics.Matrix();
                    generateInverseMatrix(currentMagnificationSpec, inverseMatrixForCurrentSpec);
                    this.mMagnificationSpecInverseMatrix.put(displayId, inverseMatrixForCurrentSpec);
                } else {
                    android.view.MagnificationSpec previousMagnificationSpec = new android.view.MagnificationSpec();
                    previousMagnificationSpec.setTo(previousSpec);
                    generateInverseMatrixBasedOnProperMagnificationSpecForDisplay(inputWindowHandles, currentMagnificationSpec, previousMagnificationSpec);
                }
            }
        }
    }

    private void generateInverseMatrixBasedOnProperMagnificationSpecForDisplay(java.util.List<android.view.InputWindowHandle> inputWindowHandles, android.view.MagnificationSpec currentMagnificationSpec, android.view.MagnificationSpec previousMagnificationSpec) {
        for (int index = inputWindowHandles.size() - 1; index >= 0; index--) {
            android.graphics.Matrix windowTransformMatrix = this.mTempMatrix2;
            android.view.InputWindowHandle windowHandle = inputWindowHandles.get(index);
            android.os.IBinder iBinder = windowHandle.getWindowToken();
            if (getWindowTransformMatrix(iBinder, windowTransformMatrix)) {
                generateMagnificationSpecInverseMatrix(windowHandle, currentMagnificationSpec, previousMagnificationSpec, windowTransformMatrix);
                return;
            }
        }
    }

    private boolean getWindowTransformMatrix(android.os.IBinder iBinder, android.graphics.Matrix outTransform) {
        android.graphics.Matrix windowMatrix = iBinder != null ? this.mWindowsTransformMatrixMap.get(iBinder) : null;
        if (windowMatrix == null) {
            return false;
        }
        outTransform.set(windowMatrix);
        return true;
    }

    private void generateMagnificationSpecInverseMatrix(android.view.InputWindowHandle inputWindowHandle, android.view.MagnificationSpec currentMagnificationSpec, android.view.MagnificationSpec previousMagnificationSpec, android.graphics.Matrix transformMatrix) {
        float[] identityMatrixFloatsForCurrentSpec = this.mTempFloat1;
        computeIdentityMatrix(inputWindowHandle, currentMagnificationSpec, transformMatrix, identityMatrixFloatsForCurrentSpec);
        float[] identityMatrixFloatsForPreviousSpec = this.mTempFloat2;
        computeIdentityMatrix(inputWindowHandle, previousMagnificationSpec, transformMatrix, identityMatrixFloatsForPreviousSpec);
        android.graphics.Matrix inverseMatrixForMagnificationSpec = new android.graphics.Matrix();
        if (selectProperMagnificationSpecByComparingIdentityDegree(identityMatrixFloatsForCurrentSpec, identityMatrixFloatsForPreviousSpec)) {
            generateInverseMatrix(currentMagnificationSpec, inverseMatrixForMagnificationSpec);
            this.mPreviousMagnificationSpec.remove(inputWindowHandle.displayId);
            if (currentMagnificationSpec.isNop()) {
                this.mCurrentMagnificationSpec.remove(inputWindowHandle.displayId);
                this.mMagnificationSpecInverseMatrix.remove(inputWindowHandle.displayId);
                return;
            }
        } else {
            generateInverseMatrix(previousMagnificationSpec, inverseMatrixForMagnificationSpec);
        }
        this.mMagnificationSpecInverseMatrix.put(inputWindowHandle.displayId, inverseMatrixForMagnificationSpec);
    }

    private void computeIdentityMatrix(android.view.InputWindowHandle inputWindowHandle, android.view.MagnificationSpec magnificationSpec, android.graphics.Matrix transformMatrix, float[] magnifyMatrixFloats) {
        android.graphics.Matrix specMatrix = this.mTempMatrix1;
        transformMagnificationSpecToMatrix(magnificationSpec, specMatrix);
        android.graphics.Matrix resultMatrix = new android.graphics.Matrix(inputWindowHandle.transform);
        resultMatrix.preConcat(specMatrix);
        resultMatrix.preConcat(transformMatrix);
        resultMatrix.getValues(magnifyMatrixFloats);
    }

    private boolean selectProperMagnificationSpecByComparingIdentityDegree(float[] magnifyMatrixFloatsForSpecOne, float[] magnifyMatrixFloatsForSpecTwo) {
        float[] IdentityMatrixValues = this.mTempFloat3;
        android.graphics.Matrix.IDENTITY_MATRIX.getValues(IdentityMatrixValues);
        float scaleDiffForSpecOne = java.lang.Math.abs(IdentityMatrixValues[0] - magnifyMatrixFloatsForSpecOne[0]);
        float scaleDiffForSpecTwo = java.lang.Math.abs(IdentityMatrixValues[0] - magnifyMatrixFloatsForSpecTwo[0]);
        float offsetXDiffForSpecOne = java.lang.Math.abs(IdentityMatrixValues[2] - magnifyMatrixFloatsForSpecOne[2]);
        float offsetXDiffForSpecTwo = java.lang.Math.abs(IdentityMatrixValues[2] - magnifyMatrixFloatsForSpecTwo[2]);
        float offsetYDiffForSpecOne = java.lang.Math.abs(IdentityMatrixValues[5] - magnifyMatrixFloatsForSpecOne[5]);
        float offsetYDiffForSpecTwo = java.lang.Math.abs(IdentityMatrixValues[5] - magnifyMatrixFloatsForSpecTwo[5]);
        float offsetDiffForSpecOne = offsetXDiffForSpecOne + offsetYDiffForSpecOne;
        float offsetDiffForSpecTwo = offsetXDiffForSpecTwo + offsetYDiffForSpecTwo;
        return java.lang.Float.compare(scaleDiffForSpecTwo, scaleDiffForSpecOne) > 0 || (java.lang.Float.compare(scaleDiffForSpecTwo, scaleDiffForSpecOne) == 0 && java.lang.Float.compare(offsetDiffForSpecTwo, offsetDiffForSpecOne) > 0);
    }

    private static void generateInverseMatrix(android.view.MagnificationSpec spec, android.graphics.Matrix outMatrix) {
        outMatrix.reset();
        android.graphics.Matrix tempMatrix = new android.graphics.Matrix();
        transformMagnificationSpecToMatrix(spec, tempMatrix);
        boolean result = tempMatrix.invert(outMatrix);
        if (!result) {
            android.util.Slog.e(TAG, "Can't inverse the magnification spec matrix with the magnification spec = " + spec);
            outMatrix.reset();
        }
    }

    private static void transformMagnificationSpecToMatrix(android.view.MagnificationSpec spec, android.graphics.Matrix outMatrix) {
        outMatrix.reset();
        outMatrix.postScale(spec.scale, spec.scale);
        outMatrix.postTranslate(spec.offsetX, spec.offsetY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyWindowsChanged(java.util.List<java.lang.Integer> displayIdsForWindowsChanged) {
        this.mHandler.removeMessages(3);
        for (int i = 0; i < displayIdsForWindowsChanged.size(); i++) {
            this.mAccessibilityController.performComputeChangedWindowsNot(displayIdsForWindowsChanged.get(i).intValue(), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forceUpdateWindows() {
        java.util.List<java.lang.Integer> displayIdsForWindowsChanged = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            for (int i = 0; i < this.mInputWindowHandlesOnDisplays.size(); i++) {
                int displayId = this.mInputWindowHandlesOnDisplays.keyAt(i);
                displayIdsForWindowsChanged.add(java.lang.Integer.valueOf(displayId));
            }
        }
        notifyWindowsChanged(displayIdsForWindowsChanged);
    }

    void dump(final java.io.PrintWriter pw, final java.lang.String prefix) throws java.lang.Throwable {
        synchronized (this.mLock) {
            try {
                try {
                    pw.print(prefix);
                    pw.println("AccessibilityWindowsPopulator");
                    java.lang.String prefix2 = prefix + "  ";
                    pw.print(prefix2);
                    pw.print("mWindowsNotificationEnabled: ");
                    pw.println(this.mWindowsNotificationEnabled);
                    if (this.mVisibleWindows.isEmpty()) {
                        pw.print(prefix2);
                        pw.println("No visible windows");
                    } else {
                        pw.print(prefix2);
                        pw.print(this.mVisibleWindows.size());
                        pw.print(" visible windows: ");
                        pw.println(this.mVisibleWindows);
                    }
                    com.android.internal.util.DumpUtils.KeyDumper noKeyDumper = new com.android.internal.util.DumpUtils.KeyDumper() { // from class: com.android.server.wm.AccessibilityWindowsPopulator$$ExternalSyntheticLambda1
                        public final void dump(int i, int i2) {
                            com.android.server.wm.AccessibilityWindowsPopulator.lambda$dump$1(i, i2);
                        }
                    };
                    com.android.internal.util.DumpUtils.KeyDumper displayDumper = new com.android.internal.util.DumpUtils.KeyDumper() { // from class: com.android.server.wm.AccessibilityWindowsPopulator$$ExternalSyntheticLambda2
                        public final void dump(int i, int i2) {
                            pw.printf("%sDisplay #%d: ", prefix, java.lang.Integer.valueOf(i2));
                        }
                    };
                    com.android.internal.util.DumpUtils.ValueDumper<android.view.MagnificationSpec> magnificationSpecDumper = new com.android.internal.util.DumpUtils.ValueDumper() { // from class: com.android.server.wm.AccessibilityWindowsPopulator$$ExternalSyntheticLambda3
                        public final void dump(java.lang.Object obj) {
                            pw.print((android.view.MagnificationSpec) obj);
                        }
                    };
                    com.android.internal.util.DumpUtils.dumpSparseArray(pw, prefix2, this.mDisplayInfos, "display info", noKeyDumper, new com.android.internal.util.DumpUtils.ValueDumper() { // from class: com.android.server.wm.AccessibilityWindowsPopulator$$ExternalSyntheticLambda4
                        public final void dump(java.lang.Object obj) {
                            pw.print((android.window.WindowInfosListener.DisplayInfo) obj);
                        }
                    });
                    com.android.internal.util.DumpUtils.dumpSparseArray(pw, prefix2, this.mInputWindowHandlesOnDisplays, "window handles on display", displayDumper, new com.android.internal.util.DumpUtils.ValueDumper() { // from class: com.android.server.wm.AccessibilityWindowsPopulator$$ExternalSyntheticLambda5
                        public final void dump(java.lang.Object obj) {
                            pw.print((java.util.List) obj);
                        }
                    });
                    com.android.internal.util.DumpUtils.dumpSparseArray(pw, prefix2, this.mMagnificationSpecInverseMatrix, "magnification spec matrix", noKeyDumper, new com.android.internal.util.DumpUtils.ValueDumper() { // from class: com.android.server.wm.AccessibilityWindowsPopulator$$ExternalSyntheticLambda6
                        public final void dump(java.lang.Object obj) {
                            ((android.graphics.Matrix) obj).dump(pw);
                        }
                    });
                    com.android.internal.util.DumpUtils.dumpSparseArray(pw, prefix2, this.mCurrentMagnificationSpec, "current magnification spec", noKeyDumper, magnificationSpecDumper);
                    com.android.internal.util.DumpUtils.dumpSparseArray(pw, prefix2, this.mPreviousMagnificationSpec, "previous magnification spec", noKeyDumper, magnificationSpecDumper);
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    static /* synthetic */ void lambda$dump$1(int i, int k) {
    }

    private void releaseResources() {
        this.mInputWindowHandlesOnDisplays.clear();
        this.mMagnificationSpecInverseMatrix.clear();
        synchronized (this.mLock) {
            this.mVisibleWindows.clear();
        }
        this.mDisplayInfos.clear();
        this.mCurrentMagnificationSpec.clear();
        this.mPreviousMagnificationSpec.clear();
        this.mWindowsTransformMatrixMap.clear();
        this.mWindowsNotificationEnabled = false;
        this.mHandler.removeCallbacksAndMessages(null);
    }

    private class MyHandler extends android.os.Handler {
        public static final int MESSAGE_NOTIFY_WINDOWS_CHANGED = 1;
        public static final int MESSAGE_NOTIFY_WINDOWS_CHANGED_BY_TIMEOUT = 3;
        public static final int MESSAGE_NOTIFY_WINDOWS_CHANGED_BY_UI_STABLE = 2;

        MyHandler(android.os.Looper looper) {
            super(looper, null, false);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case 1:
                    java.util.List<java.lang.Integer> displayIdsForWindowsChanged = (java.util.List) message.obj;
                    com.android.server.wm.AccessibilityWindowsPopulator.this.notifyWindowsChanged(displayIdsForWindowsChanged);
                    break;
                case 2:
                    com.android.server.wm.AccessibilityWindowsPopulator.this.forceUpdateWindows();
                    break;
                case 3:
                    android.util.Slog.w(com.android.server.wm.AccessibilityWindowsPopulator.TAG, "Windows change within in 2 frames continuously over 500 ms and notify windows changed immediately");
                    com.android.server.wm.AccessibilityWindowsPopulator.this.mHandler.removeMessages(2);
                    com.android.server.wm.AccessibilityWindowsPopulator.this.forceUpdateWindows();
                    break;
            }
        }
    }

    public static class AccessibilityWindow {
        private int mDisplayId;
        private boolean mIgnoreDuetoRecentsAnimation;
        private int mInputConfig;
        private boolean mIsFocused;
        private boolean mIsPIPMenu;
        private int mPrivateFlags;
        private boolean mShouldMagnify;
        private int mType;
        private android.os.IBinder mWindow;
        private android.view.WindowInfo mWindowInfo;
        private final android.graphics.Region mTouchableRegionInScreen = new android.graphics.Region();
        private final android.graphics.Region mTouchableRegionInWindow = new android.graphics.Region();
        private android.graphics.Rect mSystemBarInsetFrame = null;

        public static com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow initializeData(com.android.server.wm.WindowManagerService service, android.view.InputWindowHandle inputWindowHandle, android.graphics.Matrix magnificationInverseMatrix, android.os.IBinder pipIBinder, android.graphics.Matrix displayMatrix) {
            com.android.server.wm.InsetsSourceProvider provider;
            android.os.IBinder window = inputWindowHandle.getWindowToken();
            com.android.server.wm.WindowState windowState = window != null ? service.mWindowMap.get(window) : null;
            com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow instance = new com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow();
            instance.mWindow = window;
            instance.mDisplayId = inputWindowHandle.displayId;
            instance.mInputConfig = inputWindowHandle.inputConfig;
            instance.mType = inputWindowHandle.layoutParamsType;
            instance.mIsPIPMenu = window != null && window.equals(pipIBinder);
            instance.mPrivateFlags = windowState != null ? windowState.mAttrs.privateFlags : 0;
            instance.mIsFocused = windowState != null && windowState.isFocused();
            instance.mShouldMagnify = windowState == null || windowState.shouldMagnify();
            com.android.server.wm.RecentsAnimationController controller = service.getRecentsAnimationController();
            instance.mIgnoreDuetoRecentsAnimation = (windowState == null || controller == null || !controller.shouldIgnoreForAccessibility(windowState)) ? false : true;
            android.graphics.Rect windowFrame = new android.graphics.Rect(inputWindowHandle.frame);
            getTouchableRegionInWindow(instance.mShouldMagnify, inputWindowHandle.touchableRegion, instance.mTouchableRegionInWindow, windowFrame, magnificationInverseMatrix, displayMatrix);
            getUnMagnifiedTouchableRegion(instance.mShouldMagnify, inputWindowHandle.touchableRegion, instance.mTouchableRegionInScreen, magnificationInverseMatrix, displayMatrix);
            instance.mWindowInfo = windowState != null ? windowState.getWindowInfo() : getWindowInfoForWindowlessWindows(instance);
            android.graphics.Matrix inverseTransform = new android.graphics.Matrix();
            inputWindowHandle.transform.invert(inverseTransform);
            inverseTransform.postConcat(displayMatrix);
            inverseTransform.getValues(instance.mWindowInfo.mTransformMatrix);
            android.graphics.Matrix magnificationSpecMatrix = new android.graphics.Matrix();
            if (instance.shouldMagnify() && magnificationInverseMatrix != null && !magnificationInverseMatrix.isIdentity()) {
                if (magnificationInverseMatrix.invert(magnificationSpecMatrix)) {
                    magnificationSpecMatrix.getValues(com.android.server.wm.AccessibilityWindowsPopulator.sTempFloats);
                    android.view.MagnificationSpec spec = instance.mWindowInfo.mMagnificationSpec;
                    spec.scale = com.android.server.wm.AccessibilityWindowsPopulator.sTempFloats[0];
                    spec.offsetX = com.android.server.wm.AccessibilityWindowsPopulator.sTempFloats[2];
                    spec.offsetY = com.android.server.wm.AccessibilityWindowsPopulator.sTempFloats[5];
                } else {
                    android.util.Slog.w(com.android.server.wm.AccessibilityWindowsPopulator.TAG, "can't find spec");
                }
            }
            if (com.android.server.accessibility.Flags.computeWindowChangesOnA11yV2() && windowState != null && instance.isUntouchableNavigationBar() && (provider = windowState.getControllableInsetProvider()) != null) {
                instance.mSystemBarInsetFrame = provider.getSource().getFrame();
            }
            return instance;
        }

        public void getTouchableRegionInScreen(android.graphics.Region outRegion) {
            outRegion.set(this.mTouchableRegionInScreen);
        }

        public void getTouchableRegionInWindow(android.graphics.Region outRegion) {
            outRegion.set(this.mTouchableRegionInWindow);
        }

        public int getType() {
            return this.mType;
        }

        public int getPrivateFlag() {
            return this.mPrivateFlags;
        }

        public android.view.WindowInfo getWindowInfo() {
            return this.mWindowInfo;
        }

        public boolean shouldMagnify() {
            return this.mShouldMagnify;
        }

        public boolean isFocused() {
            return this.mIsFocused;
        }

        public boolean ignoreRecentsAnimationForAccessibility() {
            return this.mIgnoreDuetoRecentsAnimation;
        }

        public boolean isTrustedOverlay() {
            return (this.mInputConfig & 256) != 0;
        }

        public boolean isTouchable() {
            return (this.mInputConfig & 8) == 0;
        }

        public boolean isUntouchableNavigationBar() {
            if (this.mType != 2019) {
                return false;
            }
            return this.mTouchableRegionInScreen.isEmpty();
        }

        public boolean isPIPMenu() {
            return this.mIsPIPMenu;
        }

        public android.graphics.Rect getSystemBarInsetsFrame() {
            return this.mSystemBarInsetFrame;
        }

        private static void getTouchableRegionInWindow(boolean shouldMagnify, android.graphics.Region inRegion, android.graphics.Region outRegion, android.graphics.Rect frame, android.graphics.Matrix inverseMatrix, android.graphics.Matrix displayMatrix) {
            android.graphics.Region touchRegion = new android.graphics.Region();
            touchRegion.set(inRegion);
            touchRegion.op(frame, android.graphics.Region.Op.INTERSECT);
            getUnMagnifiedTouchableRegion(shouldMagnify, touchRegion, outRegion, inverseMatrix, displayMatrix);
        }

        private static void getUnMagnifiedTouchableRegion(boolean shouldMagnify, android.graphics.Region inRegion, final android.graphics.Region outRegion, final android.graphics.Matrix inverseMatrix, final android.graphics.Matrix displayMatrix) {
            if ((!shouldMagnify || inverseMatrix.isIdentity()) && displayMatrix.isIdentity()) {
                outRegion.set(inRegion);
            } else {
                com.android.server.wm.utils.RegionUtils.forEachRect(inRegion, new java.util.function.Consumer() { // from class: com.android.server.wm.AccessibilityWindowsPopulator$AccessibilityWindow$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow.lambda$getUnMagnifiedTouchableRegion$0(displayMatrix, inverseMatrix, outRegion, (android.graphics.Rect) obj);
                    }
                });
            }
        }

        static /* synthetic */ void lambda$getUnMagnifiedTouchableRegion$0(android.graphics.Matrix displayMatrix, android.graphics.Matrix inverseMatrix, android.graphics.Region outRegion, android.graphics.Rect rect) {
            android.graphics.RectF windowFrame = new android.graphics.RectF(rect);
            displayMatrix.mapRect(windowFrame);
            inverseMatrix.mapRect(windowFrame);
            outRegion.union(new android.graphics.Rect((int) windowFrame.left, (int) windowFrame.top, (int) windowFrame.right, (int) windowFrame.bottom));
        }

        private static android.view.WindowInfo getWindowInfoForWindowlessWindows(com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow window) {
            android.view.WindowInfo windowInfo = android.view.WindowInfo.obtain();
            windowInfo.displayId = window.mDisplayId;
            windowInfo.type = window.mType;
            windowInfo.token = window.mWindow;
            windowInfo.hasFlagWatchOutsideTouch = (window.mInputConfig & 512) != 0;
            windowInfo.inPictureInPicture = window.mIsPIPMenu;
            return windowInfo;
        }

        public java.lang.String toString() {
            java.lang.String windowToken = this.mWindow != null ? this.mWindow.toString() : "(no window token)";
            return "A11yWindow=[" + windowToken + ", displayId=" + this.mDisplayId + ", inputConfig=0x" + java.lang.Integer.toHexString(this.mInputConfig) + ", type=" + this.mType + ", privateFlag=0x" + java.lang.Integer.toHexString(this.mPrivateFlags) + ", focused=" + this.mIsFocused + ", shouldMagnify=" + this.mShouldMagnify + ", ignoreDuetoRecentsAnimation=" + this.mIgnoreDuetoRecentsAnimation + ", isTrustedOverlay=" + isTrustedOverlay() + ", regionInScreen=" + this.mTouchableRegionInScreen + ", touchableRegion=" + this.mTouchableRegionInWindow + ", isPIPMenu=" + this.mIsPIPMenu + ", windowInfo=" + this.mWindowInfo + "]";
        }
    }
}
