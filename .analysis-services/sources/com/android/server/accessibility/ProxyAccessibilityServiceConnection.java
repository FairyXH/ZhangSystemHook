package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public class ProxyAccessibilityServiceConnection extends com.android.server.accessibility.AccessibilityServiceConnection {
    private static final java.lang.String LOG_TAG = "ProxyAccessibilityServiceConnection";
    private int mDeviceId;
    private int mDisplayId;
    private int mFocusColor;
    private int mFocusStrokeWidth;
    private java.util.List<android.accessibilityservice.AccessibilityServiceInfo> mInstalledAndEnabledServices;
    private int mInteractiveTimeout;
    private int mNonInteractiveTimeout;

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void attachAccessibilityOverlayToDisplay(int i, int i2, android.view.SurfaceControl surfaceControl, android.view.accessibility.IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback) {
        super.attachAccessibilityOverlayToDisplay(i, i2, surfaceControl, iAccessibilityInteractionConnectionCallback);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void attachAccessibilityOverlayToWindow(int i, int i2, android.view.SurfaceControl surfaceControl, android.view.accessibility.IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback) throws android.os.RemoteException {
        super.attachAccessibilityOverlayToWindow(i, i2, surfaceControl, iAccessibilityInteractionConnectionCallback);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void bindInputLocked() {
        super.bindInputLocked();
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void bindLocked() {
        super.bindLocked();
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ boolean canReceiveEventsLocked() {
        return super.canReceiveEventsLocked();
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection
    public /* bridge */ /* synthetic */ boolean canRetrieveInteractiveWindowsLocked() {
        return super.canRetrieveInteractiveWindowsLocked();
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection, com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void connectBluetoothBrailleDisplay(java.lang.String str, android.accessibilityservice.IBrailleDisplayController iBrailleDisplayController) {
        super.connectBluetoothBrailleDisplay(str, iBrailleDisplayController);
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection, com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void connectUsbBrailleDisplay(android.hardware.usb.UsbDevice usbDevice, android.accessibilityservice.IBrailleDisplayController iBrailleDisplayController) {
        super.connectUsbBrailleDisplay(usbDevice, iBrailleDisplayController);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void createImeSessionLocked() {
        super.createImeSessionLocked();
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ java.lang.String[] findAccessibilityNodeInfoByAccessibilityId(int i, long j, int i2, android.view.accessibility.IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, long j2, android.os.Bundle bundle) throws android.os.RemoteException {
        return super.findAccessibilityNodeInfoByAccessibilityId(i, j, i2, iAccessibilityInteractionConnectionCallback, i3, j2, bundle);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ java.lang.String[] findAccessibilityNodeInfosByText(int i, long j, java.lang.String str, int i2, android.view.accessibility.IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, long j2) throws android.os.RemoteException {
        return super.findAccessibilityNodeInfosByText(i, j, str, i2, iAccessibilityInteractionConnectionCallback, j2);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ java.lang.String[] findAccessibilityNodeInfosByViewId(int i, long j, java.lang.String str, int i2, android.view.accessibility.IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, long j2) throws android.os.RemoteException {
        return super.findAccessibilityNodeInfosByViewId(i, j, str, i2, iAccessibilityInteractionConnectionCallback, j2);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ java.lang.String[] findFocus(int i, long j, int i2, int i3, android.view.accessibility.IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, long j2) throws android.os.RemoteException {
        return super.findFocus(i, j, i2, i3, iAccessibilityInteractionConnectionCallback, j2);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ java.lang.String[] focusSearch(int i, long j, int i2, int i3, android.view.accessibility.IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, long j2) throws android.os.RemoteException {
        return super.focusSearch(i, j, i2, i3, iAccessibilityInteractionConnectionCallback, j2);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ int getCapabilities() {
        return super.getCapabilities();
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ android.content.ComponentName getComponentName() {
        return super.getComponentName();
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ android.os.IBinder getOverlayWindowToken(int i) {
        return super.getOverlayWindowToken(i);
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection, com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ android.accessibilityservice.AccessibilityServiceInfo getServiceInfo() {
        return super.getServiceInfo();
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ android.view.accessibility.AccessibilityWindowInfo getWindow(int i) {
        return super.getWindow(i);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ int getWindowIdForLeashToken(android.os.IBinder iBinder) {
        return super.getWindowIdForLeashToken(iBinder);
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection
    public /* bridge */ /* synthetic */ boolean isAccessibilityButtonAvailableLocked(com.android.server.accessibility.AccessibilityUserState accessibilityUserState) {
        return super.isAccessibilityButtonAvailableLocked(accessibilityUserState);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ boolean isConnectedLocked() {
        return super.isConnectedLocked();
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ boolean isMultiFingerGesturesEnabled() {
        return super.isMultiFingerGesturesEnabled();
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ boolean isSendMotionEventsEnabled() {
        return super.isSendMotionEventsEnabled();
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ boolean isServiceDetectsGesturesEnabled(int i) {
        return super.isServiceDetectsGesturesEnabled(i);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ boolean isServiceHandlesDoubleTapEnabled() {
        return super.isServiceHandlesDoubleTapEnabled();
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ boolean isTwoFingerPassthroughEnabled() {
        return super.isTwoFingerPassthroughEnabled();
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void logTrace(long j, java.lang.String str, long j2, java.lang.String str2, int i, long j3, int i2, android.os.Bundle bundle) {
        super.logTrace(j, str, j2, str2, i, j3, i2, bundle);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void notifyAccessibilityButtonAvailabilityChangedLocked(boolean z) {
        super.notifyAccessibilityButtonAvailabilityChangedLocked(z);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void notifyAccessibilityButtonClickedLocked(int i) {
        super.notifyAccessibilityButtonClickedLocked(i);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void notifyAccessibilityEvent(android.view.accessibility.AccessibilityEvent accessibilityEvent) {
        super.notifyAccessibilityEvent(accessibilityEvent);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void notifyClearAccessibilityNodeInfoCache() {
        super.notifyClearAccessibilityNodeInfoCache();
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void notifyGesture(android.accessibilityservice.AccessibilityGestureEvent accessibilityGestureEvent) {
        super.notifyGesture(accessibilityGestureEvent);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void notifyMagnificationChangedLocked(int i, android.graphics.Region region, android.accessibilityservice.MagnificationConfig magnificationConfig) {
        super.notifyMagnificationChangedLocked(i, region, magnificationConfig);
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void notifyMotionEvent(android.view.MotionEvent motionEvent) {
        super.notifyMotionEvent(motionEvent);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void notifySoftKeyboardShowModeChangedLocked(int i) {
        super.notifySoftKeyboardShowModeChangedLocked(i);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void notifySystemActionsChangedLocked() {
        super.notifySystemActionsChangedLocked();
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void notifyTouchState(int i, int i2) {
        super.notifyTouchState(i, i2);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void onDisplayRemoved(int i) {
        super.onDisplayRemoved(i);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void onRemoved() {
        super.onRemoved();
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ boolean performAccessibilityAction(int i, long j, int i2, android.os.Bundle bundle, int i3, android.view.accessibility.IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, long j2) throws android.os.RemoteException {
        return super.performAccessibilityAction(i, j, i2, bundle, i3, iAccessibilityInteractionConnectionCallback, j2);
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection
    public /* bridge */ /* synthetic */ boolean requestImeApis() {
        return super.requestImeApis();
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection, com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void resetLocked() {
        super.resetLocked();
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void setAttributionTag(java.lang.String str) {
        super.setAttributionTag(str);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void setCacheEnabled(boolean z) {
        super.setCacheEnabled(z);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void setDynamicallyConfigurableProperties(android.accessibilityservice.AccessibilityServiceInfo accessibilityServiceInfo) {
        super.setDynamicallyConfigurableProperties(accessibilityServiceInfo);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void setImeSessionEnabledLocked(com.android.internal.inputmethod.IAccessibilityInputMethodSession iAccessibilityInputMethodSession, boolean z) {
        super.setImeSessionEnabledLocked(iAccessibilityInputMethodSession, z);
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection, com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void setTestBrailleDisplayData(java.util.List list) {
        super.setTestBrailleDisplayData(list);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void startInputLocked(com.android.internal.inputmethod.IRemoteAccessibilityInputConnection iRemoteAccessibilityInputConnection, android.view.inputmethod.EditorInfo editorInfo, boolean z) {
        super.startInputLocked(iRemoteAccessibilityInputConnection, editorInfo, z);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void takeScreenshotOfWindow(int i, int i2, android.window.ScreenCapture.ScreenCaptureListener screenCaptureListener, android.view.accessibility.IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback) throws android.os.RemoteException {
        super.takeScreenshotOfWindow(i, i2, screenCaptureListener, iAccessibilityInteractionConnectionCallback);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void unbindInputLocked() {
        super.unbindInputLocked();
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection
    public /* bridge */ /* synthetic */ void unbindLocked() {
        super.unbindLocked();
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public /* bridge */ /* synthetic */ boolean wantsGenericMotionEvent(android.view.MotionEvent motionEvent) {
        return super.wantsGenericMotionEvent(motionEvent);
    }

    ProxyAccessibilityServiceConnection(android.content.Context context, android.content.ComponentName componentName, android.accessibilityservice.AccessibilityServiceInfo accessibilityServiceInfo, int id, android.os.Handler mainHandler, java.lang.Object lock, com.android.server.accessibility.AccessibilitySecurityPolicy securityPolicy, com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport systemSupport, android.accessibilityservice.AccessibilityTrace trace, com.android.server.wm.WindowManagerInternal windowManagerInternal, com.android.server.accessibility.AccessibilityWindowManager awm, int displayId, int deviceId) {
        super(null, context, componentName, accessibilityServiceInfo, id, mainHandler, lock, securityPolicy, systemSupport, trace, windowManagerInternal, null, awm, null);
        this.mDisplayId = displayId;
        setDisplayTypes(2);
        this.mFocusStrokeWidth = this.mContext.getResources().getDimensionPixelSize(android.R.dimen.accessibility_autoclick_scroll_panel_button_size);
        this.mFocusColor = this.mContext.getResources().getColor(android.R.color.accessibility_feature_background);
        this.mDeviceId = deviceId;
    }

    int getDisplayId() {
        return this.mDisplayId;
    }

    int getDeviceId() {
        return this.mDeviceId;
    }

    void initializeServiceInterface(android.accessibilityservice.IAccessibilityServiceClient serviceInterface) throws android.os.RemoteException {
        this.mServiceInterface = serviceInterface;
        this.mService = serviceInterface.asBinder();
        this.mServiceInterface.init(this, this.mId, this.mOverlayWindowTokens.get(this.mDisplayId));
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void setInstalledAndEnabledServices(java.util.List<android.accessibilityservice.AccessibilityServiceInfo> infos) throws java.lang.Throwable {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                try {
                    this.mInstalledAndEnabledServices = infos;
                    android.accessibilityservice.AccessibilityServiceInfo proxyInfo = this.mAccessibilityServiceInfo;
                    proxyInfo.flags = 0;
                    proxyInfo.eventTypes = 0;
                    proxyInfo.notificationTimeout = 0L;
                    java.util.Set<java.lang.String> packageNames = new java.util.HashSet<>();
                    boolean hasNullPackagesNames = false;
                    boolean isAccessibilityTool = false;
                    int interactiveUiTimeout = 0;
                    int nonInteractiveUiTimeout = 0;
                    for (android.accessibilityservice.AccessibilityServiceInfo info : infos) {
                        isAccessibilityTool |= info.isAccessibilityTool();
                        if (info.packageNames == null || info.packageNames.length == 0) {
                            hasNullPackagesNames = true;
                        } else if (!hasNullPackagesNames) {
                            packageNames.addAll(java.util.Arrays.asList(info.packageNames));
                        }
                        interactiveUiTimeout = java.lang.Math.max(interactiveUiTimeout, info.getInteractiveUiTimeoutMillis());
                        nonInteractiveUiTimeout = java.lang.Math.max(nonInteractiveUiTimeout, info.getNonInteractiveUiTimeoutMillis());
                        proxyInfo.notificationTimeout = java.lang.Math.max(proxyInfo.notificationTimeout, info.notificationTimeout);
                        proxyInfo.eventTypes |= info.eventTypes;
                        proxyInfo.feedbackType |= info.feedbackType;
                        proxyInfo.flags |= info.flags;
                        setDefaultPropertiesIfNullLocked(info);
                        packageNames = packageNames;
                    }
                    java.util.Set<java.lang.String> packageNames2 = packageNames;
                    proxyInfo.setAccessibilityTool(isAccessibilityTool);
                    proxyInfo.setInteractiveUiTimeoutMillis(interactiveUiTimeout);
                    proxyInfo.setNonInteractiveUiTimeoutMillis(nonInteractiveUiTimeout);
                    this.mInteractiveTimeout = interactiveUiTimeout;
                    this.mNonInteractiveTimeout = nonInteractiveUiTimeout;
                    if (hasNullPackagesNames) {
                        proxyInfo.packageNames = null;
                    } else {
                        proxyInfo.packageNames = (java.lang.String[]) packageNames2.toArray(new java.lang.String[0]);
                    }
                    setDynamicallyConfigurableProperties(proxyInfo);
                    this.mSystemSupport.onProxyChanged(this.mDeviceId);
                } catch (java.lang.Throwable th) {
                    try {
                        throw th;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        android.os.Binder.restoreCallingIdentity(identity);
                        throw th;
                    }
                }
            }
            android.os.Binder.restoreCallingIdentity(identity);
        } catch (java.lang.Throwable th3) {
            th = th3;
        }
    }

    private void setDefaultPropertiesIfNullLocked(android.accessibilityservice.AccessibilityServiceInfo info) {
        java.lang.String componentClassDisplayName = "ProxyClass" + this.mDisplayId;
        if (info.getResolveInfo() == null) {
            android.content.pm.ResolveInfo resolveInfo = new android.content.pm.ResolveInfo();
            android.content.pm.ServiceInfo serviceInfo = new android.content.pm.ServiceInfo();
            android.content.pm.ApplicationInfo applicationInfo = new android.content.pm.ApplicationInfo();
            serviceInfo.packageName = "ProxyPackage";
            serviceInfo.name = componentClassDisplayName;
            applicationInfo.processName = "ProxyPackage";
            applicationInfo.className = componentClassDisplayName;
            resolveInfo.serviceInfo = serviceInfo;
            serviceInfo.applicationInfo = applicationInfo;
            info.setResolveInfo(resolveInfo);
        }
        if (info.getComponentName() == null) {
            info.setComponentName(new android.content.ComponentName("ProxyPackage", componentClassDisplayName));
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getInstalledAndEnabledServices() {
        java.util.List<android.accessibilityservice.AccessibilityServiceInfo> listEmptyList;
        synchronized (this.mLock) {
            listEmptyList = this.mInstalledAndEnabledServices != null ? this.mInstalledAndEnabledServices : java.util.Collections.emptyList();
        }
        return listEmptyList;
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public android.view.accessibility.AccessibilityWindowInfo.WindowListSparseArray getWindows() {
        android.view.accessibility.AccessibilityWindowInfo.WindowListSparseArray allWindows = super.getWindows();
        android.view.accessibility.AccessibilityWindowInfo.WindowListSparseArray displayWindows = new android.view.accessibility.AccessibilityWindowInfo.WindowListSparseArray();
        displayWindows.put(this.mDisplayId, (java.util.List) allWindows.get(this.mDisplayId, java.util.Collections.emptyList()));
        return displayWindows;
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection, com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void setFocusAppearance(int strokeWidth, int color) {
        synchronized (this.mLock) {
            if (hasRightsToCurrentUserLocked()) {
                if (this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                    if (getFocusStrokeWidthLocked() == strokeWidth && getFocusColorLocked() == color) {
                        return;
                    }
                    this.mFocusStrokeWidth = strokeWidth;
                    this.mFocusColor = color;
                    this.mSystemSupport.onProxyChanged(this.mDeviceId);
                }
            }
        }
    }

    public int getFocusStrokeWidthLocked() {
        return this.mFocusStrokeWidth;
    }

    public int getFocusColorLocked() {
        return this.mFocusColor;
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    int resolveAccessibilityWindowIdForFindFocusLocked(int windowId, int focusType) {
        if (windowId == -2) {
            int focusedWindowId = this.mA11yWindowManager.getFocusedWindowId(focusType, this.mDisplayId);
            if (!this.mA11yWindowManager.windowIdBelongsToDisplayType(focusedWindowId, this.mDisplayTypes)) {
                return -1;
            }
            return focusedWindowId;
        }
        return windowId;
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection, android.os.IBinder.DeathRecipient
    public void binderDied() {
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    protected boolean supportsFlagForNotImportantViews(android.accessibilityservice.AccessibilityServiceInfo info) {
        return true;
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection, com.android.server.accessibility.AbstractAccessibilityServiceConnection
    protected boolean hasRightsToCurrentUserLocked() {
        return true;
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection, com.android.server.accessibility.KeyEventDispatcher.KeyEventFilter
    public boolean onKeyEvent(android.view.KeyEvent keyEvent, int sequenceNumber) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("onKeyEvent is not supported");
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection, com.android.server.accessibility.FingerprintGestureDispatcher.FingerprintGestureClient
    public boolean isCapturingFingerprintGestures() throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("isCapturingFingerprintGestures is not supported");
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection, com.android.server.accessibility.FingerprintGestureDispatcher.FingerprintGestureClient
    public void onFingerprintGestureDetectionActiveChanged(boolean active) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("onFingerprintGestureDetectionActiveChanged is not supported");
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection, com.android.server.accessibility.FingerprintGestureDispatcher.FingerprintGestureClient
    public void onFingerprintGesture(int gesture) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("onFingerprintGesture is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public boolean isFingerprintGestureDetectionAvailable() throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("isFingerprintGestureDetectionAvailable is not supported");
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection, android.content.ServiceConnection
    public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("onServiceConnected is not supported");
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection, android.content.ServiceConnection
    public void onServiceDisconnected(android.content.ComponentName name) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("onServiceDisconnected is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void setServiceInfo(android.accessibilityservice.AccessibilityServiceInfo info) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("setServiceInfo is not supported");
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection
    public void disableSelf() throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("disableSelf is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public boolean performGlobalAction(int action) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("performGlobalAction is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void setOnKeyEventResult(boolean handled, int sequence) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("setOnKeyEventResult is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public java.util.List<android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction> getSystemActions() throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("getSystemActions is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public android.accessibilityservice.MagnificationConfig getMagnificationConfig(int displayId) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("getMagnificationConfig is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public float getMagnificationScale(int displayId) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("getMagnificationScale is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public float getMagnificationCenterX(int displayId) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("getMagnificationCenterX is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public float getMagnificationCenterY(int displayId) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("getMagnificationCenterY is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public android.graphics.Region getMagnificationRegion(int displayId) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("getMagnificationRegion is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public android.graphics.Region getCurrentMagnificationRegion(int displayId) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("getCurrentMagnificationRegion is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public boolean resetMagnification(int displayId, boolean animate) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("resetMagnification is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public boolean resetCurrentMagnification(int displayId, boolean animate) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("resetCurrentMagnification is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public boolean setMagnificationConfig(int displayId, android.accessibilityservice.MagnificationConfig config, boolean animate) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("setMagnificationConfig is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void setMagnificationCallbackEnabled(int displayId, boolean enabled) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("setMagnificationCallbackEnabled is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public boolean isMagnificationCallbackEnabled(int displayId) {
        throw new java.lang.UnsupportedOperationException("isMagnificationCallbackEnabled is not supported");
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection
    public boolean setSoftKeyboardShowMode(int showMode) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("setSoftKeyboardShowMode is not supported");
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection
    public int getSoftKeyboardShowMode() throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("getSoftKeyboardShowMode is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void setSoftKeyboardCallbackEnabled(boolean enabled) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("setSoftKeyboardCallbackEnabled is not supported");
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection
    public boolean switchToInputMethod(java.lang.String imeId) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("switchToInputMethod is not supported");
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection
    public int setInputMethodEnabled(java.lang.String imeId, boolean enabled) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("setInputMethodEnabled is not supported");
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection
    public boolean isAccessibilityButtonAvailable() throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("isAccessibilityButtonAvailable is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void sendGesture(int sequence, android.content.pm.ParceledListSlice gestureSteps) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("sendGesture is not supported");
    }

    @Override // com.android.server.accessibility.AccessibilityServiceConnection, com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void dispatchGesture(int sequence, android.content.pm.ParceledListSlice gestureSteps, int displayId) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("dispatchGesture is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void takeScreenshot(int displayId, android.os.RemoteCallback callback) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("takeScreenshot is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void setGestureDetectionPassthroughRegion(int displayId, android.graphics.Region region) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("setGestureDetectionPassthroughRegion is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void setTouchExplorationPassthroughRegion(int displayId, android.graphics.Region region) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("setTouchExplorationPassthroughRegion is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void setServiceDetectsGesturesEnabled(int displayId, boolean mode) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("setServiceDetectsGesturesEnabled is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void requestTouchExploration(int displayId) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("requestTouchExploration is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void requestDragging(int displayId, int pointerId) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("requestDragging is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void requestDelegating(int displayId) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("requestDelegating is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void onDoubleTap(int displayId) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("onDoubleTap is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void onDoubleTapAndHold(int displayId) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("onDoubleTapAndHold is not supported");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void setAnimationScale(float scale) throws java.lang.UnsupportedOperationException {
        throw new java.lang.UnsupportedOperationException("setAnimationScale is not supported");
    }

    public int getInteractiveTimeout() {
        return this.mInteractiveTimeout;
    }

    public int getNonInteractiveTimeout() {
        return this.mNonInteractiveTimeout;
    }

    public boolean updateTimeouts(int nonInteractiveUiTimeout, int interactiveUiTimeout) {
        int newInteractiveUiTimeout;
        int newNonInteractiveUiTimeout;
        if (interactiveUiTimeout != 0) {
            newInteractiveUiTimeout = interactiveUiTimeout;
        } else {
            newInteractiveUiTimeout = this.mAccessibilityServiceInfo.getInteractiveUiTimeoutMillis();
        }
        if (nonInteractiveUiTimeout != 0) {
            newNonInteractiveUiTimeout = nonInteractiveUiTimeout;
        } else {
            newNonInteractiveUiTimeout = this.mAccessibilityServiceInfo.getNonInteractiveUiTimeoutMillis();
        }
        boolean updated = false;
        if (this.mInteractiveTimeout != newInteractiveUiTimeout) {
            this.mInteractiveTimeout = newInteractiveUiTimeout;
            updated = true;
        }
        if (this.mNonInteractiveTimeout != newNonInteractiveUiTimeout) {
            this.mNonInteractiveTimeout = newNonInteractiveUiTimeout;
            return true;
        }
        return updated;
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, LOG_TAG, pw)) {
            synchronized (this.mLock) {
                pw.append((java.lang.CharSequence) ("Proxy[displayId=" + this.mDisplayId));
                pw.append((java.lang.CharSequence) (", deviceId=" + this.mDeviceId));
                pw.append((java.lang.CharSequence) (", feedbackType" + android.accessibilityservice.AccessibilityServiceInfo.feedbackTypeToString(this.mFeedbackType)));
                pw.append((java.lang.CharSequence) (", capabilities=" + this.mAccessibilityServiceInfo.getCapabilities()));
                pw.append((java.lang.CharSequence) (", eventTypes=" + android.view.accessibility.AccessibilityEvent.eventTypeToString(this.mEventTypes)));
                pw.append((java.lang.CharSequence) (", notificationTimeout=" + this.mNotificationTimeout));
                pw.append(", nonInteractiveUiTimeout=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mNonInteractiveTimeout));
                pw.append(", interactiveUiTimeout=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mInteractiveTimeout));
                pw.append(", focusStrokeWidth=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mFocusStrokeWidth));
                pw.append(", focusColor=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mFocusColor));
                pw.append(", installedAndEnabledServiceCount=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mInstalledAndEnabledServices.size()));
                pw.append(", installedAndEnabledServices=").append((java.lang.CharSequence) this.mInstalledAndEnabledServices.toString());
                pw.append("]");
            }
        }
    }
}
