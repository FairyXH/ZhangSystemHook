package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
class AccessibilityServiceConnection extends com.android.server.accessibility.AbstractAccessibilityServiceConnection {
    private static final boolean DEBUG_ACCESSBILITY;
    private static final java.lang.String LOG_TAG = "AccessibilityServiceConnection";
    final com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManagerService;
    private com.android.server.accessibility.BrailleDisplayConnection mBrailleDisplayConnection;
    final android.content.Intent mIntent;
    private final android.os.Handler mMainHandler;
    private java.util.List<android.os.Bundle> mTestBrailleDisplays;
    final int mUserId;
    final java.lang.ref.WeakReference<com.android.server.accessibility.AccessibilityUserState> mUserStateWeakReference;

    static {
        DEBUG_ACCESSBILITY = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false) || android.os.SystemProperties.getBoolean("persist.sys.alwayson.enable", false) || "1".equals(android.os.SystemProperties.get("persist.sys.agingtest", "0"));
    }

    private static final class AccessibilityInputMethodSessionCallback extends com.android.internal.inputmethod.IAccessibilityInputMethodSessionCallback.Stub {
        private final int mUserId;

        AccessibilityInputMethodSessionCallback(int userId) {
            this.mUserId = userId;
        }

        public void sessionCreated(com.android.internal.inputmethod.IAccessibilityInputMethodSession session, int id) {
            android.os.Trace.traceBegin(32L, "ASC.sessionCreated");
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.inputmethod.InputMethodManagerInternal.get().onSessionForAccessibilityCreated(id, session, this.mUserId);
                android.os.Binder.restoreCallingIdentity(ident);
                android.os.Trace.traceEnd(32L);
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(ident);
                throw th;
            }
        }
    }

    AccessibilityServiceConnection(com.android.server.accessibility.AccessibilityUserState userState, android.content.Context context, android.content.ComponentName componentName, android.accessibilityservice.AccessibilityServiceInfo accessibilityServiceInfo, int id, android.os.Handler mainHandler, java.lang.Object lock, com.android.server.accessibility.AccessibilitySecurityPolicy securityPolicy, com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport systemSupport, android.accessibilityservice.AccessibilityTrace trace, com.android.server.wm.WindowManagerInternal windowManagerInternal, com.android.server.accessibility.SystemActionPerformer systemActionPerfomer, com.android.server.accessibility.AccessibilityWindowManager awm, com.android.server.wm.ActivityTaskManagerInternal activityTaskManagerService) {
        super(context, componentName, accessibilityServiceInfo, id, mainHandler, lock, securityPolicy, systemSupport, trace, windowManagerInternal, systemActionPerfomer, awm);
        this.mTestBrailleDisplays = null;
        this.mUserStateWeakReference = new java.lang.ref.WeakReference<>(userState);
        this.mUserId = userState == null ? -10000 : userState.mUserId;
        this.mIntent = new android.content.Intent().setComponent(this.mComponentName);
        this.mMainHandler = mainHandler;
        this.mIntent.putExtra("android.intent.extra.client_label", android.R.string.ThreeWCMmi);
        this.mActivityTaskManagerService = activityTaskManagerService;
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mIntent.putExtra("android.intent.extra.client_intent", this.mSystemSupport.getPendingIntentActivity(this.mContext, 0, new android.content.Intent("android.settings.ACCESSIBILITY_SETTINGS"), 67108864));
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void bindLocked() {
        com.android.server.accessibility.AccessibilityUserState userState = this.mUserStateWeakReference.get();
        if (userState == null) {
            return;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            int flags = userState.getBindInstantServiceAllowedLocked() ? 34607105 | 4194304 : 34607105;
            if (this.mService == null && this.mContext.bindServiceAsUser(this.mIntent, this, flags, new android.os.UserHandle(userState.mUserId))) {
                userState.getBindingServicesLocked().add(this.mComponentName);
            }
            android.os.Binder.restoreCallingIdentity(identity);
            this.mActivityTaskManagerService.setAllowAppSwitches(this.mComponentName.flattenToString(), this.mAccessibilityServiceInfo.getResolveInfo().serviceInfo.applicationInfo.uid, userState.mUserId);
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identity);
            throw th;
        }
    }

    public void unbindLocked() {
        if (requestImeApis()) {
            this.mSystemSupport.unbindImeLocked(this);
        }
        this.mContext.unbindService(this);
        com.android.server.accessibility.AccessibilityUserState userState = this.mUserStateWeakReference.get();
        if (userState == null) {
            return;
        }
        userState.removeServiceLocked(this);
        this.mSystemSupport.getMagnificationProcessor().resetAllIfNeeded(this.mId);
        this.mActivityTaskManagerService.setAllowAppSwitches(this.mComponentName.flattenToString(), -1, userState.mUserId);
        resetLocked();
    }

    public boolean canRetrieveInteractiveWindowsLocked() {
        return this.mSecurityPolicy.canRetrieveWindowContentLocked(this) && this.mRetrieveInteractiveWindows;
    }

    public void disableSelf() {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("disableSelf", "");
        }
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityUserState userState = this.mUserStateWeakReference.get();
            if (userState == null) {
                return;
            }
            if (userState.getEnabledServicesLocked().remove(this.mComponentName)) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    this.mSystemSupport.persistComponentNamesToSettingLocked("enabled_accessibility_services", userState.getEnabledServicesLocked(), userState.mUserId);
                    this.mSystemSupport.onClientChangeLocked(false);
                    android.os.Binder.restoreCallingIdentity(identity);
                } catch (java.lang.Throwable th) {
                    android.os.Binder.restoreCallingIdentity(identity);
                    throw th;
                }
            }
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(android.content.ComponentName componentName, android.os.IBinder service) {
        if (DEBUG_ACCESSBILITY) {
            android.util.Slog.d(LOG_TAG, "[onServiceConnected]componentName = " + componentName);
        }
        com.android.server.accessibility.AccessibilityUserState userState = this.mUserStateWeakReference.get();
        if (userState != null) {
            addWindowTokensForAllDisplays();
        }
        synchronized (this.mLock) {
            if (this.mService != service) {
                if (this.mService != null) {
                    this.mService.unlinkToDeath(this, 0);
                }
                this.mService = service;
                try {
                    this.mService.linkToDeath(this, 0);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(LOG_TAG, "Failed registering death link");
                    binderDied();
                    return;
                }
            }
            this.mServiceInterface = android.accessibilityservice.IAccessibilityServiceClient.Stub.asInterface(service);
            if (userState == null) {
                return;
            }
            userState.addServiceLocked(this);
            this.mSystemSupport.onClientChangeLocked(false);
            this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.accessibility.AccessibilityServiceConnection$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.accessibility.AccessibilityServiceConnection) obj).initializeService();
                }
            }, this));
            if (requestImeApis()) {
                this.mSystemSupport.requestImeLocked(this);
            }
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public android.accessibilityservice.AccessibilityServiceInfo getServiceInfo() {
        return this.mAccessibilityServiceInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initializeService() {
        android.accessibilityservice.IAccessibilityServiceClient serviceInterface = null;
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityUserState userState = this.mUserStateWeakReference.get();
            if (userState == null) {
                return;
            }
            java.util.Set<android.content.ComponentName> bindingServices = userState.getBindingServicesLocked();
            java.util.Set<android.content.ComponentName> crashedServices = userState.getCrashedServicesLocked();
            if (bindingServices.contains(this.mComponentName) || crashedServices.contains(this.mComponentName)) {
                bindingServices.remove(this.mComponentName);
                crashedServices.remove(this.mComponentName);
                this.mAccessibilityServiceInfo.crashed = false;
                serviceInterface = this.mServiceInterface;
            }
            if (serviceInterface != null && !userState.getEnabledServicesLocked().contains(this.mComponentName)) {
                this.mSystemSupport.onClientChangeLocked(false);
                return;
            }
            if (serviceInterface == null) {
                binderDied();
                return;
            }
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("init", this + "," + this.mId + "," + this.mOverlayWindowTokens.get(0));
                }
                serviceInterface.init(this, this.mId, this.mOverlayWindowTokens.get(0));
            } catch (android.os.RemoteException re) {
                android.util.Slog.w(LOG_TAG, "Error while setting connection for service: " + serviceInterface, re);
                binderDied();
            }
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(android.content.ComponentName componentName) {
        if (DEBUG_ACCESSBILITY) {
            android.util.Slog.d(LOG_TAG, "[onServiceDisconnected]componentName = " + componentName);
        }
        binderDied();
        com.android.server.accessibility.AccessibilityUserState userState = this.mUserStateWeakReference.get();
        if (userState != null) {
            this.mActivityTaskManagerService.setAllowAppSwitches(this.mComponentName.flattenToString(), -1, userState.mUserId);
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    protected boolean hasRightsToCurrentUserLocked() {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid == 0 || callingUid == 1000 || callingUid == 2000 || this.mSecurityPolicy.resolveProfileParentLocked(android.os.UserHandle.getUserId(callingUid)) == this.mSystemSupport.getCurrentUserIdLocked() || this.mSecurityPolicy.hasPermission("android.permission.INTERACT_ACROSS_USERS") || this.mSecurityPolicy.hasPermission("android.permission.INTERACT_ACROSS_USERS_FULL")) {
            return true;
        }
        return false;
    }

    public boolean setSoftKeyboardShowMode(int showMode) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setSoftKeyboardShowMode", "showMode=" + showMode);
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return false;
            }
            com.android.server.accessibility.AccessibilityUserState userState = this.mUserStateWeakReference.get();
            if (userState == null) {
                return false;
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return userState.setSoftKeyboardModeLocked(showMode, this.mComponentName);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public int getSoftKeyboardShowMode() {
        int softKeyboardShowModeLocked;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getSoftKeyboardShowMode", "");
        }
        com.android.server.accessibility.AccessibilityUserState userState = this.mUserStateWeakReference.get();
        long identity = android.os.Binder.clearCallingIdentity();
        if (userState == null) {
            softKeyboardShowModeLocked = 0;
        } else {
            try {
                softKeyboardShowModeLocked = userState.getSoftKeyboardShowModeLocked();
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
        return softKeyboardShowModeLocked;
    }

    public boolean switchToInputMethod(java.lang.String imeId) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("switchToInputMethod", "imeId=" + imeId);
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return false;
            }
            int callingUserId = android.os.UserHandle.getCallingUserId();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                boolean result = com.android.server.inputmethod.InputMethodManagerInternal.get().switchToInputMethod(imeId, callingUserId);
                return result;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public int setInputMethodEnabled(java.lang.String imeId, boolean enabled) throws java.lang.SecurityException {
        int checkResult;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("switchToInputMethod", "imeId=" + imeId);
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return 2;
            }
            int callingUserId = android.os.UserHandle.getCallingUserId();
            com.android.server.inputmethod.InputMethodManagerInternal inputMethodManagerInternal = com.android.server.inputmethod.InputMethodManagerInternal.get();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (this.mLock) {
                    checkResult = this.mSecurityPolicy.canEnableDisableInputMethod(imeId, this);
                }
                if (checkResult != 0) {
                    return checkResult;
                }
                if (!inputMethodManagerInternal.setInputMethodEnabled(imeId, enabled, callingUserId)) {
                    return 2;
                }
                android.os.Binder.restoreCallingIdentity(identity);
                return 0;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public boolean isAccessibilityButtonAvailable() {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("isAccessibilityButtonAvailable", "");
        }
        synchronized (this.mLock) {
            boolean z = false;
            if (!hasRightsToCurrentUserLocked()) {
                return false;
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.accessibility.AccessibilityUserState userState = this.mUserStateWeakReference.get();
                if (userState != null) {
                    if (isAccessibilityButtonAvailableLocked(userState)) {
                        z = true;
                    }
                }
                return z;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        synchronized (this.mLock) {
            if (isConnectedLocked()) {
                if (requestImeApis()) {
                    this.mSystemSupport.unbindImeLocked(this);
                }
                this.mAccessibilityServiceInfo.crashed = true;
                com.android.server.accessibility.AccessibilityUserState userState = this.mUserStateWeakReference.get();
                if (userState != null) {
                    userState.serviceDisconnectedLocked(this);
                }
                resetLocked();
                this.mSystemSupport.getMagnificationProcessor().resetAllIfNeeded(this.mId);
                this.mSystemSupport.onClientChangeLocked(false);
                if (this.mAccessibilityServiceInfo.getResolveInfo() != null && this.mAccessibilityServiceInfo.getResolveInfo().serviceInfo != null) {
                    android.util.Slog.d(LOG_TAG, "[binderDied]applicationInfo = " + this.mAccessibilityServiceInfo.getResolveInfo().serviceInfo.applicationInfo);
                }
            }
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void resetLocked() {
        super.resetLocked();
        if (android.view.accessibility.Flags.brailleDisplayHid() && this.mBrailleDisplayConnection != null) {
            this.mBrailleDisplayConnection.disconnect();
        }
    }

    public boolean isAccessibilityButtonAvailableLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        return this.mRequestAccessibilityButton && this.mSystemSupport.isAccessibilityButtonShown();
    }

    @Override // com.android.server.accessibility.FingerprintGestureDispatcher.FingerprintGestureClient
    public boolean isCapturingFingerprintGestures() {
        return this.mServiceInterface != null && this.mSecurityPolicy.canCaptureFingerprintGestures(this) && this.mCaptureFingerprintGestures;
    }

    @Override // com.android.server.accessibility.FingerprintGestureDispatcher.FingerprintGestureClient
    public void onFingerprintGestureDetectionActiveChanged(boolean active) {
        android.accessibilityservice.IAccessibilityServiceClient serviceInterface;
        if (!isCapturingFingerprintGestures()) {
            return;
        }
        synchronized (this.mLock) {
            serviceInterface = this.mServiceInterface;
        }
        if (serviceInterface != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("onFingerprintCapturingGesturesChanged", java.lang.String.valueOf(active));
                }
                this.mServiceInterface.onFingerprintCapturingGesturesChanged(active);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    @Override // com.android.server.accessibility.FingerprintGestureDispatcher.FingerprintGestureClient
    public void onFingerprintGesture(int gesture) {
        android.accessibilityservice.IAccessibilityServiceClient serviceInterface;
        if (!isCapturingFingerprintGestures()) {
            return;
        }
        synchronized (this.mLock) {
            serviceInterface = this.mServiceInterface;
        }
        if (serviceInterface != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("onFingerprintGesture", java.lang.String.valueOf(gesture));
                }
                this.mServiceInterface.onFingerprintGesture(gesture);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void dispatchGesture(int sequence, android.content.pm.ParceledListSlice gestureSteps, int displayId) {
        synchronized (this.mLock) {
            if (this.mServiceInterface != null && this.mSecurityPolicy.canPerformGestures(this)) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.accessibility.MotionEventInjector motionEventInjector = this.mSystemSupport.getMotionEventInjectorForDisplayLocked(displayId);
                    if (wmTracingEnabled()) {
                        logTraceWM("isTouchOrFaketouchDevice", "");
                    }
                    if (motionEventInjector != null && this.mWindowManagerService.isTouchOrFaketouchDevice()) {
                        motionEventInjector.injectEvents(gestureSteps.getList(), this.mServiceInterface, sequence, displayId);
                    } else {
                        try {
                            if (svcClientTracingEnabled()) {
                                logTraceSvcClient("onPerformGestureResult", sequence + ", false");
                            }
                            this.mServiceInterface.onPerformGestureResult(sequence, false);
                        } catch (android.os.RemoteException re) {
                            android.util.Slog.e(LOG_TAG, "Error sending motion event injection failure to " + this.mServiceInterface, re);
                        }
                    }
                    android.os.Binder.restoreCallingIdentity(identity);
                } catch (java.lang.Throwable th) {
                    android.os.Binder.restoreCallingIdentity(identity);
                    throw th;
                }
            }
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void setFocusAppearance(int strokeWidth, int color) {
        com.android.server.accessibility.AccessibilityUserState userState = this.mUserStateWeakReference.get();
        if (userState == null) {
            return;
        }
        synchronized (this.mLock) {
            if (hasRightsToCurrentUserLocked()) {
                if (this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                    if (userState.getFocusStrokeWidthLocked() == strokeWidth && userState.getFocusColorLocked() == color) {
                        return;
                    }
                    long identity = android.os.Binder.clearCallingIdentity();
                    try {
                        userState.setFocusAppearanceLocked(strokeWidth, color);
                        this.mSystemSupport.onClientChangeLocked(false);
                    } finally {
                        android.os.Binder.restoreCallingIdentity(identity);
                    }
                }
            }
        }
    }

    public void notifyMotionEvent(android.view.MotionEvent event) {
        android.os.Message msg = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.AccessibilityServiceConnection$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.accessibility.AccessibilityServiceConnection) obj).notifyMotionEventInternal((android.view.MotionEvent) obj2);
            }
        }, this, event);
        this.mMainHandler.sendMessage(msg);
    }

    public void notifyTouchState(int displayId, int state) {
        android.os.Message msg = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.accessibility.AccessibilityServiceConnection$$ExternalSyntheticLambda2
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                ((com.android.server.accessibility.AccessibilityServiceConnection) obj).notifyTouchStateInternal(((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue());
            }
        }, this, java.lang.Integer.valueOf(displayId), java.lang.Integer.valueOf(state));
        this.mMainHandler.sendMessage(msg);
    }

    public boolean requestImeApis() {
        return this.mRequestImeApis;
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    protected void createImeSessionInternal() {
        android.accessibilityservice.IAccessibilityServiceClient listener = getServiceInterfaceSafely();
        if (listener != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("createImeSession", "");
                }
                com.android.server.accessibility.AccessibilityServiceConnection.AccessibilityInputMethodSessionCallback callback = new com.android.server.accessibility.AccessibilityServiceConnection.AccessibilityInputMethodSessionCallback(this.mUserId);
                listener.createImeSession(callback);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error requesting IME session from " + this.mService, re);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyMotionEventInternal(android.view.MotionEvent event) {
        android.accessibilityservice.IAccessibilityServiceClient listener = getServiceInterfaceSafely();
        if (listener != null) {
            try {
                if (this.mTrace.isA11yTracingEnabled()) {
                    logTraceSvcClient(".onMotionEvent ", event.toString());
                }
                listener.onMotionEvent(event);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error sending motion event to" + this.mService, re);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyTouchStateInternal(int displayId, int state) {
        android.accessibilityservice.IAccessibilityServiceClient listener = getServiceInterfaceSafely();
        if (listener != null) {
            try {
                if (this.mTrace.isA11yTracingEnabled()) {
                    logTraceSvcClient(".onTouchStateChanged ", android.accessibilityservice.TouchInteractionController.stateToString(state));
                }
                listener.onTouchStateChanged(displayId, state);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error sending motion event to" + this.mService, re);
            }
        }
    }

    private void checkAccessibilityAccessLocked() {
        if (!hasRightsToCurrentUserLocked() || !this.mSecurityPolicy.checkAccessibilityAccess(this)) {
            throw new java.lang.SecurityException("Caller does not have accessibility access");
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void connectBluetoothBrailleDisplay(final java.lang.String bluetoothAddress, android.accessibilityservice.IBrailleDisplayController controller) {
        connectBluetoothBrailleDisplay_enforcePermission();
        if (!android.view.accessibility.Flags.brailleDisplayHid()) {
            throw new java.lang.IllegalStateException("Flag BRAILLE_DISPLAY_HID not enabled");
        }
        java.util.Objects.requireNonNull(bluetoothAddress);
        java.util.Objects.requireNonNull(controller);
        if (!android.bluetooth.BluetoothAdapter.checkBluetoothAddress(bluetoothAddress)) {
            throw new java.lang.IllegalArgumentException(bluetoothAddress + " is not a valid Bluetooth address");
        }
        android.bluetooth.BluetoothManager bluetoothManager = (android.bluetooth.BluetoothManager) this.mContext.getSystemService(android.bluetooth.BluetoothManager.class);
        java.lang.String bluetoothDeviceName = bluetoothManager != null ? (java.lang.String) bluetoothManager.getAdapter().getBondedDevices().stream().filter(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityServiceConnection$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((android.bluetooth.BluetoothDevice) obj).getAddress().equalsIgnoreCase(bluetoothAddress);
            }
        }).map(new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityServiceConnection$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((android.bluetooth.BluetoothDevice) obj).getName();
            }
        }).findFirst().orElse(null) : null;
        synchronized (this.mLock) {
            checkAccessibilityAccessLocked();
            if (this.mBrailleDisplayConnection != null) {
                throw new java.lang.IllegalStateException("This service already has a connected Braille display");
            }
            com.android.server.accessibility.BrailleDisplayConnection connection = new com.android.server.accessibility.BrailleDisplayConnection(this.mLock, this);
            if (this.mTestBrailleDisplays != null) {
                connection.setTestData(this.mTestBrailleDisplays);
            }
            connection.connectLocked(bluetoothAddress, bluetoothDeviceName, 5, controller);
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void connectUsbBrailleDisplay(android.hardware.usb.UsbDevice usbDevice, android.accessibilityservice.IBrailleDisplayController controller) {
        if (!android.view.accessibility.Flags.brailleDisplayHid()) {
            throw new java.lang.IllegalStateException("Flag BRAILLE_DISPLAY_HID not enabled");
        }
        java.util.Objects.requireNonNull(usbDevice);
        java.util.Objects.requireNonNull(controller);
        android.hardware.usb.UsbManager usbManager = (android.hardware.usb.UsbManager) this.mContext.getSystemService("usb");
        int uid = android.os.Binder.getCallingUid();
        int pid = android.os.Binder.getCallingPid();
        long identity = android.os.Binder.clearCallingIdentity();
        if (usbManager != null) {
            try {
                if (usbManager.hasPermission(usbDevice, this.mComponentName.getPackageName(), pid, uid)) {
                    java.lang.String usbSerialNumber = usbDevice.getSerialNumber();
                    if (android.text.TextUtils.isEmpty(usbSerialNumber)) {
                        try {
                            controller.onConnectionFailed(2);
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.e(LOG_TAG, "Error calling onConnectionFailed", e);
                        }
                        return;
                    }
                    android.os.Binder.restoreCallingIdentity(identity);
                    synchronized (this.mLock) {
                        checkAccessibilityAccessLocked();
                        if (this.mBrailleDisplayConnection != null) {
                            throw new java.lang.IllegalStateException("This service already has a connected Braille display");
                        }
                        com.android.server.accessibility.BrailleDisplayConnection connection = new com.android.server.accessibility.BrailleDisplayConnection(this.mLock, this);
                        if (this.mTestBrailleDisplays != null) {
                            connection.setTestData(this.mTestBrailleDisplays);
                        }
                        connection.connectLocked(usbSerialNumber, usbDevice.getProductName(), 3, controller);
                    }
                    return;
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
        throw new java.lang.SecurityException("Caller does not have permission to access this UsbDevice");
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
    public void setTestBrailleDisplayData(java.util.List<android.os.Bundle> brailleDisplays) {
        setTestBrailleDisplayData_enforcePermission();
        this.mTestBrailleDisplays = brailleDisplays;
    }

    void onBrailleDisplayConnectedLocked(com.android.server.accessibility.BrailleDisplayConnection connection) {
        this.mBrailleDisplayConnection = connection;
    }

    void onBrailleDisplayDisconnectedLocked() {
        this.mBrailleDisplayConnection = null;
    }
}
