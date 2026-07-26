package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
class UiAutomationManager {
    private static final android.content.ComponentName COMPONENT_NAME = new android.content.ComponentName("com.android.server.accessibility", "UiAutomation");
    private static final java.lang.String LOG_TAG = "UiAutomationManager";
    private final java.lang.Object mLock;
    private com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport mSystemSupport;
    private int mUiAutomationFlags;
    private com.android.server.accessibility.UiAutomationManager.UiAutomationService mUiAutomationService;
    private android.os.IBinder mUiAutomationServiceOwner;
    private final android.os.IBinder.DeathRecipient mUiAutomationServiceOwnerDeathRecipient = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.accessibility.UiAutomationManager.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.accessibility.UiAutomationManager.this.mUiAutomationServiceOwner.unlinkToDeath(this, 0);
            com.android.server.accessibility.UiAutomationManager.this.mUiAutomationServiceOwner = null;
            com.android.server.accessibility.UiAutomationManager.this.destroyUiAutomationService();
            android.util.Slog.v(com.android.server.accessibility.UiAutomationManager.LOG_TAG, "UiAutomation service owner died");
        }
    };

    UiAutomationManager(java.lang.Object lock) {
        this.mLock = lock;
    }

    void registerUiTestAutomationServiceLocked(android.os.IBinder owner, android.accessibilityservice.IAccessibilityServiceClient serviceClient, android.content.Context context, android.accessibilityservice.AccessibilityServiceInfo accessibilityServiceInfo, int id, android.os.Handler mainHandler, com.android.server.accessibility.AccessibilitySecurityPolicy securityPolicy, com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport systemSupport, android.accessibilityservice.AccessibilityTrace trace, com.android.server.wm.WindowManagerInternal windowManagerInternal, com.android.server.accessibility.SystemActionPerformer systemActionPerformer, com.android.server.accessibility.AccessibilityWindowManager awm, int flags) {
        accessibilityServiceInfo.setComponentName(COMPONENT_NAME);
        com.android.server.utils.Slogf.i(LOG_TAG, "Registering UiTestAutomationService (id=%s, flags=0x%x) when called by user %d", accessibilityServiceInfo.getId(), java.lang.Integer.valueOf(flags), java.lang.Integer.valueOf(android.os.Binder.getCallingUserHandle().getIdentifier()));
        if (this.mUiAutomationService != null) {
            throw new java.lang.IllegalStateException("UiAutomationService " + this.mUiAutomationService.mServiceInterface + "already registered!");
        }
        try {
            owner.linkToDeath(this.mUiAutomationServiceOwnerDeathRecipient, 0);
            this.mUiAutomationFlags = flags;
            this.mSystemSupport = systemSupport;
            if (useAccessibility()) {
                this.mUiAutomationService = new com.android.server.accessibility.UiAutomationManager.UiAutomationService(context, accessibilityServiceInfo, id, mainHandler, this.mLock, securityPolicy, systemSupport, trace, windowManagerInternal, systemActionPerformer, awm);
                this.mUiAutomationServiceOwner = owner;
                this.mUiAutomationService.mServiceInterface = serviceClient;
                try {
                    this.mUiAutomationService.mServiceInterface.asBinder().linkToDeath(this.mUiAutomationService, 0);
                    this.mUiAutomationService.connectServiceUnknownThread();
                } catch (android.os.RemoteException re) {
                    android.util.Slog.e(LOG_TAG, "Failed registering death link: " + re);
                    destroyUiAutomationService();
                }
            }
        } catch (android.os.RemoteException re2) {
            android.util.Slog.e(LOG_TAG, "Couldn't register for the death of a UiTestAutomationService!", re2);
        }
    }

    void unregisterUiTestAutomationServiceLocked(android.accessibilityservice.IAccessibilityServiceClient serviceClient) {
        synchronized (this.mLock) {
            if (useAccessibility() && (this.mUiAutomationService == null || serviceClient == null || this.mUiAutomationService.mServiceInterface == null || serviceClient.asBinder() != this.mUiAutomationService.mServiceInterface.asBinder())) {
                throw new java.lang.IllegalStateException("UiAutomationService " + serviceClient + " not registered!");
            }
            destroyUiAutomationService();
        }
    }

    void sendAccessibilityEventLocked(android.view.accessibility.AccessibilityEvent event) {
        if (this.mUiAutomationService != null) {
            this.mUiAutomationService.notifyAccessibilityEvent(event);
        }
    }

    boolean isUiAutomationRunningLocked() {
        return (this.mUiAutomationService == null && useAccessibility()) ? false : true;
    }

    boolean suppressingAccessibilityServicesLocked() {
        return !(this.mUiAutomationService == null && useAccessibility()) && (this.mUiAutomationFlags & 1) == 0;
    }

    boolean useAccessibility() {
        return (this.mUiAutomationFlags & 2) == 0;
    }

    boolean canIntrospect() {
        return this.mUiAutomationService != null;
    }

    boolean isTouchExplorationEnabledLocked() {
        return this.mUiAutomationService != null && this.mUiAutomationService.mRequestTouchExplorationMode;
    }

    boolean canRetrieveInteractiveWindowsLocked() {
        return this.mUiAutomationService != null && this.mUiAutomationService.mRetrieveInteractiveWindows;
    }

    int getRequestedEventMaskLocked() {
        if (this.mUiAutomationService == null) {
            return 0;
        }
        return this.mUiAutomationService.mEventTypes;
    }

    int getRelevantEventTypes() {
        com.android.server.accessibility.UiAutomationManager.UiAutomationService uiAutomationService;
        synchronized (this.mLock) {
            uiAutomationService = this.mUiAutomationService;
        }
        if (uiAutomationService == null) {
            return 0;
        }
        return uiAutomationService.getRelevantEventTypes();
    }

    android.accessibilityservice.AccessibilityServiceInfo getServiceInfo() {
        com.android.server.accessibility.UiAutomationManager.UiAutomationService uiAutomationService;
        synchronized (this.mLock) {
            uiAutomationService = this.mUiAutomationService;
        }
        if (uiAutomationService == null) {
            return null;
        }
        return uiAutomationService.getServiceInfo();
    }

    void dumpUiAutomationService(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        com.android.server.accessibility.UiAutomationManager.UiAutomationService uiAutomationService;
        synchronized (this.mLock) {
            uiAutomationService = this.mUiAutomationService;
        }
        if (uiAutomationService != null) {
            uiAutomationService.dump(fd, pw, args);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroyUiAutomationService() {
        synchronized (this.mLock) {
            if (this.mUiAutomationService != null) {
                try {
                    this.mUiAutomationService.mServiceInterface.asBinder().unlinkToDeath(this.mUiAutomationService, 0);
                } catch (java.util.NoSuchElementException e) {
                    e.printStackTrace();
                }
                this.mUiAutomationService.onRemoved();
                this.mUiAutomationService.resetLocked();
                this.mUiAutomationService = null;
                if (this.mUiAutomationServiceOwner != null) {
                    this.mUiAutomationServiceOwner.unlinkToDeath(this.mUiAutomationServiceOwnerDeathRecipient, 0);
                    this.mUiAutomationServiceOwner = null;
                }
                this.mUiAutomationFlags = 0;
                this.mSystemSupport.onClientChangeLocked(false);
            } else {
                this.mUiAutomationFlags = 0;
                this.mSystemSupport.onClientChangeLocked(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class UiAutomationService extends com.android.server.accessibility.AbstractAccessibilityServiceConnection {
        private final android.os.Handler mMainHandler;

        UiAutomationService(android.content.Context context, android.accessibilityservice.AccessibilityServiceInfo accessibilityServiceInfo, int id, android.os.Handler mainHandler, java.lang.Object lock, com.android.server.accessibility.AccessibilitySecurityPolicy securityPolicy, com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport systemSupport, android.accessibilityservice.AccessibilityTrace trace, com.android.server.wm.WindowManagerInternal windowManagerInternal, com.android.server.accessibility.SystemActionPerformer systemActionPerformer, com.android.server.accessibility.AccessibilityWindowManager awm) {
            super(context, com.android.server.accessibility.UiAutomationManager.COMPONENT_NAME, accessibilityServiceInfo, id, mainHandler, lock, securityPolicy, systemSupport, trace, windowManagerInternal, systemActionPerformer, awm);
            boolean isMainHandler = mainHandler.getLooper() == android.os.Looper.getMainLooper();
            if (android.os.Build.IS_USERDEBUG || android.os.Build.IS_ENG) {
                com.android.internal.util.Preconditions.checkArgument(isMainHandler, "UiAutomationService must use the main handler");
            } else if (!isMainHandler) {
                android.util.Slog.e(com.android.server.accessibility.UiAutomationManager.LOG_TAG, "UiAutomationService must use the main handler");
            }
            this.mMainHandler = mainHandler;
            setDisplayTypes(3);
        }

        void connectServiceUnknownThread() {
            this.mMainHandler.post(new java.lang.Runnable() { // from class: com.android.server.accessibility.UiAutomationManager$UiAutomationService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$connectServiceUnknownThread$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$connectServiceUnknownThread$0() {
            android.accessibilityservice.IAccessibilityServiceClient serviceInterface;
            com.android.server.accessibility.UiAutomationManager.UiAutomationService uiAutomationService;
            try {
                synchronized (this.mLock) {
                    serviceInterface = this.mServiceInterface;
                    uiAutomationService = com.android.server.accessibility.UiAutomationManager.this.mUiAutomationService;
                    if (serviceInterface == null) {
                        this.mService = null;
                    } else {
                        this.mService = this.mServiceInterface.asBinder();
                        this.mService.linkToDeath(this, 0);
                    }
                }
                if (serviceInterface != null && uiAutomationService != null) {
                    uiAutomationService.addWindowTokensForAllDisplays();
                    if (this.mTrace.isA11yTracingEnabledForTypes(2L)) {
                        this.mTrace.logTrace("UiAutomationService.connectServiceUnknownThread", 2L, "serviceConnection=" + this + ";connectionId=" + this.mId + "windowToken=" + this.mOverlayWindowTokens.get(0));
                    }
                    serviceInterface.init(this, this.mId, this.mOverlayWindowTokens.get(0));
                }
            } catch (android.os.RemoteException re) {
                android.util.Slog.w(com.android.server.accessibility.UiAutomationManager.LOG_TAG, "Error initializing connection", re);
                com.android.server.accessibility.UiAutomationManager.this.destroyUiAutomationService();
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.accessibility.UiAutomationManager.this.destroyUiAutomationService();
        }

        @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
        protected boolean hasRightsToCurrentUserLocked() {
            return true;
        }

        @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
        protected boolean supportsFlagForNotImportantViews(android.accessibilityservice.AccessibilityServiceInfo info) {
            return true;
        }

        @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
        public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, com.android.server.accessibility.UiAutomationManager.LOG_TAG, pw)) {
                synchronized (this.mLock) {
                    pw.append((java.lang.CharSequence) ("Ui Automation[eventTypes=" + android.view.accessibility.AccessibilityEvent.eventTypeToString(this.mEventTypes)));
                    pw.append((java.lang.CharSequence) (", notificationTimeout=" + this.mNotificationTimeout));
                    pw.append("]");
                }
            }
        }

        public boolean setSoftKeyboardShowMode(int mode) {
            return false;
        }

        public int getSoftKeyboardShowMode() {
            return 0;
        }

        public boolean switchToInputMethod(java.lang.String imeId) {
            return false;
        }

        public int setInputMethodEnabled(java.lang.String imeId, boolean enabled) {
            return 2;
        }

        public boolean isAccessibilityButtonAvailable() {
            return false;
        }

        public void disableSelf() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName componentName, android.os.IBinder service) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName componentName) {
        }

        @Override // com.android.server.accessibility.FingerprintGestureDispatcher.FingerprintGestureClient
        public boolean isCapturingFingerprintGestures() {
            return false;
        }

        @Override // com.android.server.accessibility.FingerprintGestureDispatcher.FingerprintGestureClient
        public void onFingerprintGestureDetectionActiveChanged(boolean active) {
        }

        @Override // com.android.server.accessibility.FingerprintGestureDispatcher.FingerprintGestureClient
        public void onFingerprintGesture(int gesture) {
        }

        @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
        public void takeScreenshot(int displayId, android.os.RemoteCallback callback) {
        }
    }
}
