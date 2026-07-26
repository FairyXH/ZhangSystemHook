package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public class ProxyManager {
    private static final boolean DEBUG;
    private static final java.lang.String LOG_TAG = "ProxyManager";
    static final java.lang.String PROXY_COMPONENT_CLASS_NAME = "ProxyClass";
    static final java.lang.String PROXY_COMPONENT_PACKAGE_NAME = "ProxyPackage";
    private com.android.server.accessibility.AccessibilityInputFilter mA11yInputFilter;
    private final com.android.server.accessibility.AccessibilityWindowManager mA11yWindowManager;
    private com.android.server.companion.virtual.VirtualDeviceManagerInternal.AppsOnVirtualDeviceListener mAppsOnVirtualDeviceListener;
    private final android.content.Context mContext;
    private final java.lang.Object mLock;
    private final android.os.Handler mMainHandler;
    private final com.android.server.accessibility.ProxyManager.SystemSupport mSystemSupport;
    private final com.android.server.accessibility.UiAutomationManager mUiAutomationManager;
    private android.companion.virtual.VirtualDeviceManager.VirtualDeviceListener mVirtualDeviceListener;
    private final android.util.SparseIntArray mLastStates = new android.util.SparseIntArray();
    private final android.util.SparseArray<com.android.server.accessibility.ProxyAccessibilityServiceConnection> mProxyA11yServiceConnections = new android.util.SparseArray<>();
    private com.android.server.companion.virtual.VirtualDeviceManagerInternal mLocalVdm = (com.android.server.companion.virtual.VirtualDeviceManagerInternal) com.android.server.LocalServices.getService(com.android.server.companion.virtual.VirtualDeviceManagerInternal.class);

    public interface SystemSupport {
        android.os.RemoteCallbackList<android.view.accessibility.IAccessibilityManagerClient> getCurrentUserClientsLocked();

        android.os.RemoteCallbackList<android.view.accessibility.IAccessibilityManagerClient> getGlobalClientsLocked();

        void notifyClearAccessibilityCacheLocked();

        void removeDeviceIdLocked(int i);

        void updateWindowsForAccessibilityCallbackLocked();
    }

    static {
        DEBUG = android.util.Log.isLoggable(LOG_TAG, 3) && android.os.Build.IS_DEBUGGABLE;
    }

    public ProxyManager(java.lang.Object lock, com.android.server.accessibility.AccessibilityWindowManager awm, android.content.Context context, android.os.Handler mainHandler, com.android.server.accessibility.UiAutomationManager uiAutomationManager, com.android.server.accessibility.ProxyManager.SystemSupport systemSupport) {
        this.mLock = lock;
        this.mA11yWindowManager = awm;
        this.mContext = context;
        this.mMainHandler = mainHandler;
        this.mUiAutomationManager = uiAutomationManager;
        this.mSystemSupport = systemSupport;
    }

    public void registerProxy(final android.accessibilityservice.IAccessibilityServiceClient client, final int displayId, int id, com.android.server.accessibility.AccessibilitySecurityPolicy securityPolicy, com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport systemSupport, android.accessibilityservice.AccessibilityTrace trace, com.android.server.wm.WindowManagerInternal windowManagerInternal) throws java.lang.Throwable {
        if (DEBUG) {
            android.util.Slog.v(LOG_TAG, "Register proxy for display id: " + displayId);
        }
        android.companion.virtual.VirtualDeviceManager vdm = (android.companion.virtual.VirtualDeviceManager) this.mContext.getSystemService(android.companion.virtual.VirtualDeviceManager.class);
        if (vdm == null) {
            return;
        }
        int deviceId = vdm.getDeviceIdForDisplayId(displayId);
        android.accessibilityservice.AccessibilityServiceInfo info = new android.accessibilityservice.AccessibilityServiceInfo();
        info.setCapabilities(3);
        java.lang.String componentClassDisplayName = PROXY_COMPONENT_CLASS_NAME + displayId;
        info.setComponentName(new android.content.ComponentName(PROXY_COMPONENT_PACKAGE_NAME, componentClassDisplayName));
        com.android.server.accessibility.ProxyAccessibilityServiceConnection connection = new com.android.server.accessibility.ProxyAccessibilityServiceConnection(this.mContext, info.getComponentName(), info, id, this.mMainHandler, this.mLock, securityPolicy, systemSupport, trace, windowManagerInternal, this.mA11yWindowManager, displayId, deviceId);
        synchronized (this.mLock) {
            try {
                this.mProxyA11yServiceConnections.put(displayId, connection);
                if (com.android.server.accessibility.Flags.proxyUseAppsOnVirtualDeviceListener() && this.mAppsOnVirtualDeviceListener == null) {
                    this.mAppsOnVirtualDeviceListener = new com.android.server.companion.virtual.VirtualDeviceManagerInternal.AppsOnVirtualDeviceListener() { // from class: com.android.server.accessibility.ProxyManager$$ExternalSyntheticLambda2
                        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal.AppsOnVirtualDeviceListener
                        public final void onAppsOnAnyVirtualDeviceChanged(java.util.Set set) {
                            this.f$0.lambda$registerProxy$0(set);
                        }
                    };
                    com.android.server.companion.virtual.VirtualDeviceManagerInternal localVdm = getLocalVdm();
                    if (localVdm != null) {
                        localVdm.registerAppsOnVirtualDeviceListener(this.mAppsOnVirtualDeviceListener);
                    }
                }
                if (this.mProxyA11yServiceConnections.size() == 1) {
                    registerVirtualDeviceListener();
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
        android.os.IBinder.DeathRecipient deathRecipient = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.accessibility.ProxyManager.1
            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                client.asBinder().unlinkToDeath(this, 0);
                com.android.server.accessibility.ProxyManager.this.clearConnectionAndUpdateState(displayId);
            }
        };
        client.asBinder().linkToDeath(deathRecipient, 0);
        this.mMainHandler.post(new java.lang.Runnable() { // from class: com.android.server.accessibility.ProxyManager$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$registerProxy$1(displayId);
            }
        });
        connection.initializeServiceInterface(client);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerProxy$1(int displayId) {
        if (this.mA11yInputFilter != null) {
            this.mA11yInputFilter.disableFeaturesForDisplayIfInstalled(displayId);
        }
    }

    private void registerVirtualDeviceListener() {
        android.companion.virtual.VirtualDeviceManager vdm = (android.companion.virtual.VirtualDeviceManager) this.mContext.getSystemService(android.companion.virtual.VirtualDeviceManager.class);
        if (vdm == null || !android.companion.virtual.flags.Flags.vdmPublicApis()) {
            return;
        }
        if (this.mVirtualDeviceListener == null) {
            this.mVirtualDeviceListener = new android.companion.virtual.VirtualDeviceManager.VirtualDeviceListener() { // from class: com.android.server.accessibility.ProxyManager.2
                public void onVirtualDeviceClosed(int deviceId) {
                    com.android.server.accessibility.ProxyManager.this.clearConnections(deviceId);
                }
            };
        }
        vdm.registerVirtualDeviceListener(this.mContext.getMainExecutor(), this.mVirtualDeviceListener);
    }

    private void unregisterVirtualDeviceListener() {
        android.companion.virtual.VirtualDeviceManager vdm = (android.companion.virtual.VirtualDeviceManager) this.mContext.getSystemService(android.companion.virtual.VirtualDeviceManager.class);
        if (vdm == null || !android.companion.virtual.flags.Flags.vdmPublicApis()) {
            return;
        }
        vdm.unregisterVirtualDeviceListener(this.mVirtualDeviceListener);
    }

    public boolean unregisterProxy(int displayId) {
        return clearConnectionAndUpdateState(displayId);
    }

    public void clearConnections(int deviceId) {
        android.util.IntArray displaysToClear = new android.util.IntArray();
        synchronized (this.mLock) {
            for (int i = 0; i < this.mProxyA11yServiceConnections.size(); i++) {
                com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy = this.mProxyA11yServiceConnections.valueAt(i);
                if (proxy != null && proxy.getDeviceId() == deviceId) {
                    displaysToClear.add(proxy.getDisplayId());
                }
            }
        }
        for (int i2 = 0; i2 < displaysToClear.size(); i2++) {
            clearConnectionAndUpdateState(displaysToClear.get(i2));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean clearConnectionAndUpdateState(int displayId) {
        boolean removedFromConnections = false;
        int deviceId = -1;
        synchronized (this.mLock) {
            if (this.mProxyA11yServiceConnections.contains(displayId)) {
                deviceId = this.mProxyA11yServiceConnections.get(displayId).getDeviceId();
                this.mProxyA11yServiceConnections.remove(displayId);
                removedFromConnections = true;
                if (this.mProxyA11yServiceConnections.size() == 0) {
                    unregisterVirtualDeviceListener();
                }
            }
        }
        if (removedFromConnections) {
            updateStateForRemovedDisplay(displayId, deviceId);
        }
        if (DEBUG) {
            android.util.Slog.v(LOG_TAG, "Unregistered proxy for display id " + displayId + ": " + removedFromConnections);
        }
        return removedFromConnections;
    }

    private void updateStateForRemovedDisplay(final int displayId, int deviceId) {
        com.android.server.companion.virtual.VirtualDeviceManagerInternal localVdm;
        this.mA11yWindowManager.stopTrackingDisplayProxy(displayId);
        this.mMainHandler.post(new java.lang.Runnable() { // from class: com.android.server.accessibility.ProxyManager$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateStateForRemovedDisplay$2(displayId);
            }
        });
        if (!isProxyedDeviceId(deviceId)) {
            synchronized (this.mLock) {
                if (com.android.server.accessibility.Flags.proxyUseAppsOnVirtualDeviceListener() && this.mProxyA11yServiceConnections.size() == 0 && (localVdm = getLocalVdm()) != null && this.mAppsOnVirtualDeviceListener != null) {
                    localVdm.unregisterAppsOnVirtualDeviceListener(this.mAppsOnVirtualDeviceListener);
                    this.mAppsOnVirtualDeviceListener = null;
                }
                this.mSystemSupport.removeDeviceIdLocked(deviceId);
                this.mLastStates.delete(deviceId);
            }
            return;
        }
        onProxyChanged(deviceId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateStateForRemovedDisplay$2(int displayId) {
        if (this.mA11yInputFilter != null) {
            android.hardware.display.DisplayManager displayManager = (android.hardware.display.DisplayManager) this.mContext.getSystemService("display");
            android.view.Display proxyDisplay = displayManager.getDisplay(displayId);
            if (proxyDisplay != null) {
                this.mA11yInputFilter.enableFeaturesForDisplayIfInstalled(proxyDisplay);
            }
        }
    }

    public boolean isProxyedDisplay(int displayId) {
        boolean tracked;
        synchronized (this.mLock) {
            tracked = this.mProxyA11yServiceConnections.contains(displayId);
            if (DEBUG) {
                android.util.Slog.v(LOG_TAG, "Tracking proxy display " + displayId + " : " + tracked);
            }
        }
        return tracked;
    }

    public boolean isProxyedDeviceId(int deviceId) {
        boolean isTrackingDeviceId;
        if (deviceId == 0 || deviceId == -1) {
            return false;
        }
        synchronized (this.mLock) {
            isTrackingDeviceId = getFirstProxyForDeviceIdLocked(deviceId) != null;
        }
        if (DEBUG) {
            android.util.Slog.v(LOG_TAG, "Tracking device " + deviceId + " : " + isTrackingDeviceId);
        }
        return isTrackingDeviceId;
    }

    public boolean displayBelongsToCaller(int callingUid, int proxyDisplayId) {
        android.companion.virtual.VirtualDeviceManager vdm = (android.companion.virtual.VirtualDeviceManager) this.mContext.getSystemService(android.companion.virtual.VirtualDeviceManager.class);
        com.android.server.companion.virtual.VirtualDeviceManagerInternal localVdm = getLocalVdm();
        if (vdm == null || localVdm == null) {
            return false;
        }
        java.util.List<android.companion.virtual.VirtualDevice> virtualDevices = vdm.getVirtualDevices();
        for (android.companion.virtual.VirtualDevice device : virtualDevices) {
            if (localVdm.getDisplayIdsForDevice(device.getDeviceId()).contains(java.lang.Integer.valueOf(proxyDisplayId))) {
                int ownerUid = localVdm.getDeviceOwnerUid(device.getDeviceId());
                if (callingUid == ownerUid) {
                    return true;
                }
            }
        }
        return false;
    }

    public void sendAccessibilityEventLocked(android.view.accessibility.AccessibilityEvent event) {
        com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy = this.mProxyA11yServiceConnections.get(event.getDisplayId());
        if (proxy != null) {
            if (DEBUG) {
                android.util.Slog.v(LOG_TAG, "Send proxy event " + event + " for display id " + event.getDisplayId());
            }
            proxy.notifyAccessibilityEvent(event);
        }
    }

    public boolean canRetrieveInteractiveWindowsLocked() {
        boolean observingWindows = false;
        int i = 0;
        while (true) {
            if (i >= this.mProxyA11yServiceConnections.size()) {
                break;
            }
            com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy = this.mProxyA11yServiceConnections.valueAt(i);
            if (!proxy.mRetrieveInteractiveWindows) {
                i++;
            } else {
                observingWindows = true;
                break;
            }
        }
        if (DEBUG) {
            android.util.Slog.v(LOG_TAG, "At least one proxy can retrieve windows: " + observingWindows);
        }
        return observingWindows;
    }

    public int getStateLocked(int deviceId) {
        int clientState = 0;
        boolean uiAutomationCanIntrospect = this.mUiAutomationManager.canIntrospect();
        if (uiAutomationCanIntrospect) {
            clientState = 0 | 1;
        }
        for (int i = 0; i < this.mProxyA11yServiceConnections.size(); i++) {
            com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy = this.mProxyA11yServiceConnections.valueAt(i);
            if (proxy != null && proxy.getDeviceId() == deviceId) {
                clientState |= getStateForDisplayIdLocked(proxy);
            }
        }
        if (DEBUG) {
            android.util.Slog.v(LOG_TAG, "For device id " + deviceId + " a11y is enabled: " + ((clientState & 1) != 0));
            android.util.Slog.v(LOG_TAG, "For device id " + deviceId + " touch exploration is enabled: " + ((clientState & 2) != 0));
        }
        return clientState;
    }

    private int getStateForDisplayIdLocked(com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy) {
        int clientState = 0;
        if (proxy != null) {
            clientState = 0 | 1;
            if (proxy.mRequestTouchExplorationMode) {
                clientState |= 2;
            }
        }
        if (DEBUG) {
            android.util.Slog.v(LOG_TAG, "Accessibility is enabled for all proxies: " + ((clientState & 1) != 0));
            android.util.Slog.v(LOG_TAG, "Touch exploration is enabled for all proxies: " + ((clientState & 2) != 0));
        }
        return clientState;
    }

    private int getLastSentStateLocked(int deviceId) {
        return this.mLastStates.get(deviceId, 0);
    }

    private void setLastStateLocked(int deviceId, int proxyState) {
        this.mLastStates.put(deviceId, proxyState);
    }

    private void updateRelevantEventTypesLocked(final int deviceId) {
        if (!isProxyedDeviceId(deviceId)) {
            return;
        }
        this.mMainHandler.post(new java.lang.Runnable() { // from class: com.android.server.accessibility.ProxyManager$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateRelevantEventTypesLocked$4(deviceId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateRelevantEventTypesLocked$4(final int deviceId) {
        synchronized (this.mLock) {
            broadcastToClientsLocked(com.android.internal.util.FunctionalUtils.ignoreRemoteException(new com.android.internal.util.FunctionalUtils.RemoteExceptionIgnoringConsumer() { // from class: com.android.server.accessibility.ProxyManager$$ExternalSyntheticLambda8
                public final void acceptOrThrow(java.lang.Object obj) throws android.os.RemoteException {
                    this.f$0.lambda$updateRelevantEventTypesLocked$3(deviceId, (com.android.server.accessibility.AccessibilityManagerService.Client) obj);
                }
            }));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateRelevantEventTypesLocked$3(int deviceId, com.android.server.accessibility.AccessibilityManagerService.Client client) throws android.os.RemoteException {
        int relevantEventTypes;
        if (client.mDeviceId == deviceId && client.mLastSentRelevantEventTypes != (relevantEventTypes = computeRelevantEventTypesLocked(client))) {
            client.mLastSentRelevantEventTypes = relevantEventTypes;
            client.mCallback.setRelevantEventTypes(relevantEventTypes);
        }
    }

    public int computeRelevantEventTypesLocked(com.android.server.accessibility.AccessibilityManagerService.Client client) {
        int relevantEventTypes;
        int relevantEventTypes2 = 0;
        for (int i = 0; i < this.mProxyA11yServiceConnections.size(); i++) {
            com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy = this.mProxyA11yServiceConnections.valueAt(i);
            if (proxy != null && proxy.getDeviceId() == client.mDeviceId) {
                int relevantEventTypes3 = relevantEventTypes2 | proxy.getRelevantEventTypes();
                if (com.android.server.accessibility.AccessibilityManagerService.isClientInPackageAllowlist(this.mUiAutomationManager.getServiceInfo(), client)) {
                    relevantEventTypes = this.mUiAutomationManager.getRelevantEventTypes();
                } else {
                    relevantEventTypes = 0;
                }
                relevantEventTypes2 = relevantEventTypes3 | relevantEventTypes;
            }
        }
        if (DEBUG) {
            android.util.Slog.v(LOG_TAG, "Relevant event types for device id " + client.mDeviceId + ": " + android.view.accessibility.AccessibilityEvent.eventTypeToString(relevantEventTypes2));
        }
        return relevantEventTypes2;
    }

    public void addServiceInterfacesLocked(java.util.List<android.accessibilityservice.IAccessibilityServiceClient> interfaces, int deviceId) {
        for (int i = 0; i < this.mProxyA11yServiceConnections.size(); i++) {
            com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy = this.mProxyA11yServiceConnections.valueAt(i);
            if (proxy != null && proxy.getDeviceId() == deviceId) {
                android.os.IBinder proxyBinder = proxy.mService;
                android.accessibilityservice.IAccessibilityServiceClient proxyInterface = proxy.mServiceInterface;
                if (proxyBinder != null && proxyInterface != null) {
                    interfaces.add(proxyInterface);
                }
            }
        }
    }

    public java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getInstalledAndEnabledServiceInfosLocked(int feedbackType, int deviceId) {
        java.util.List<android.accessibilityservice.AccessibilityServiceInfo> serviceInfos = new java.util.ArrayList<>();
        for (int i = 0; i < this.mProxyA11yServiceConnections.size(); i++) {
            com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy = this.mProxyA11yServiceConnections.valueAt(i);
            if (proxy != null && proxy.getDeviceId() == deviceId) {
                if (feedbackType == -1) {
                    serviceInfos.addAll(proxy.getInstalledAndEnabledServices());
                } else if ((proxy.mFeedbackType & feedbackType) != 0) {
                    java.util.List<android.accessibilityservice.AccessibilityServiceInfo> proxyInfos = proxy.getInstalledAndEnabledServices();
                    for (android.accessibilityservice.AccessibilityServiceInfo info : proxyInfos) {
                        if ((info.feedbackType & feedbackType) != 0) {
                            serviceInfos.add(info);
                        }
                    }
                }
            }
        }
        return serviceInfos;
    }

    private void onProxyChanged(int deviceId, boolean forceUpdate) {
        if (DEBUG) {
            android.util.Slog.v(LOG_TAG, "onProxyChanged called for deviceId: " + deviceId);
        }
        synchronized (this.mLock) {
            updateDeviceIdsIfNeededLocked(deviceId);
            this.mSystemSupport.updateWindowsForAccessibilityCallbackLocked();
            updateRelevantEventTypesLocked(deviceId);
            scheduleUpdateProxyClientsIfNeededLocked(deviceId, forceUpdate);
            scheduleNotifyProxyClientsOfServicesStateChangeLocked(deviceId);
            updateFocusAppearanceLocked(deviceId);
            this.mSystemSupport.notifyClearAccessibilityCacheLocked();
        }
    }

    public void onProxyChanged(int deviceId) {
        onProxyChanged(deviceId, false);
    }

    private void scheduleUpdateProxyClientsIfNeededLocked(final int deviceId, boolean forceUpdate) {
        final int proxyState = getStateLocked(deviceId);
        if (DEBUG) {
            android.util.Slog.v(LOG_TAG, "State for device id " + deviceId + " is " + proxyState);
            android.util.Slog.v(LOG_TAG, "Last state for device id " + deviceId + " is " + getLastSentStateLocked(deviceId));
            android.util.Slog.v(LOG_TAG, "force update: " + forceUpdate);
        }
        if (getLastSentStateLocked(deviceId) != proxyState || (com.android.server.accessibility.Flags.proxyUseAppsOnVirtualDeviceListener() && forceUpdate)) {
            setLastStateLocked(deviceId, proxyState);
            this.mMainHandler.post(new java.lang.Runnable() { // from class: com.android.server.accessibility.ProxyManager$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$scheduleUpdateProxyClientsIfNeededLocked$6(deviceId, proxyState);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleUpdateProxyClientsIfNeededLocked$6(final int deviceId, final int proxyState) {
        synchronized (this.mLock) {
            broadcastToClientsLocked(com.android.internal.util.FunctionalUtils.ignoreRemoteException(new com.android.internal.util.FunctionalUtils.RemoteExceptionIgnoringConsumer() { // from class: com.android.server.accessibility.ProxyManager$$ExternalSyntheticLambda0
                public final void acceptOrThrow(java.lang.Object obj) throws android.os.RemoteException {
                    com.android.server.accessibility.ProxyManager.lambda$scheduleUpdateProxyClientsIfNeededLocked$5(deviceId, proxyState, (com.android.server.accessibility.AccessibilityManagerService.Client) obj);
                }
            }));
        }
    }

    static /* synthetic */ void lambda$scheduleUpdateProxyClientsIfNeededLocked$5(int deviceId, int proxyState, com.android.server.accessibility.AccessibilityManagerService.Client client) throws android.os.RemoteException {
        if (client.mDeviceId == deviceId) {
            client.mCallback.setState(proxyState);
        }
    }

    private void scheduleNotifyProxyClientsOfServicesStateChangeLocked(final int deviceId) {
        if (DEBUG) {
            android.util.Slog.v(LOG_TAG, "Notify services state change at device id " + deviceId);
        }
        this.mMainHandler.post(new java.lang.Runnable() { // from class: com.android.server.accessibility.ProxyManager$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleNotifyProxyClientsOfServicesStateChangeLocked$8(deviceId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleNotifyProxyClientsOfServicesStateChangeLocked$8(final int deviceId) {
        broadcastToClientsLocked(com.android.internal.util.FunctionalUtils.ignoreRemoteException(new com.android.internal.util.FunctionalUtils.RemoteExceptionIgnoringConsumer() { // from class: com.android.server.accessibility.ProxyManager$$ExternalSyntheticLambda7
            public final void acceptOrThrow(java.lang.Object obj) throws android.os.RemoteException {
                this.f$0.lambda$scheduleNotifyProxyClientsOfServicesStateChangeLocked$7(deviceId, (com.android.server.accessibility.AccessibilityManagerService.Client) obj);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleNotifyProxyClientsOfServicesStateChangeLocked$7(int deviceId, com.android.server.accessibility.AccessibilityManagerService.Client client) throws android.os.RemoteException {
        if (client.mDeviceId == deviceId) {
            synchronized (this.mLock) {
                client.mCallback.notifyServicesStateChanged(getRecommendedTimeoutMillisLocked(deviceId));
            }
        }
    }

    private void updateFocusAppearanceLocked(int deviceId) {
        if (DEBUG) {
            android.util.Slog.v(LOG_TAG, "Update proxy focus appearance at device id " + deviceId);
        }
        final com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy = getFirstProxyForDeviceIdLocked(deviceId);
        if (proxy != null) {
            this.mMainHandler.post(new java.lang.Runnable() { // from class: com.android.server.accessibility.ProxyManager$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateFocusAppearanceLocked$10(proxy);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateFocusAppearanceLocked$10(final com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy) {
        broadcastToClientsLocked(com.android.internal.util.FunctionalUtils.ignoreRemoteException(new com.android.internal.util.FunctionalUtils.RemoteExceptionIgnoringConsumer() { // from class: com.android.server.accessibility.ProxyManager$$ExternalSyntheticLambda10
            public final void acceptOrThrow(java.lang.Object obj) throws android.os.RemoteException {
                com.android.server.accessibility.ProxyManager.lambda$updateFocusAppearanceLocked$9(proxy, (com.android.server.accessibility.AccessibilityManagerService.Client) obj);
            }
        }));
    }

    static /* synthetic */ void lambda$updateFocusAppearanceLocked$9(com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy, com.android.server.accessibility.AccessibilityManagerService.Client client) throws android.os.RemoteException {
        if (client.mDeviceId == proxy.getDeviceId()) {
            client.mCallback.setFocusAppearance(proxy.getFocusStrokeWidthLocked(), proxy.getFocusColorLocked());
        }
    }

    private com.android.server.accessibility.ProxyAccessibilityServiceConnection getFirstProxyForDeviceIdLocked(int deviceId) {
        for (int i = 0; i < this.mProxyA11yServiceConnections.size(); i++) {
            com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy = this.mProxyA11yServiceConnections.valueAt(i);
            if (proxy != null && proxy.getDeviceId() == deviceId) {
                return proxy;
            }
        }
        return null;
    }

    private void broadcastToClientsLocked(java.util.function.Consumer<com.android.server.accessibility.AccessibilityManagerService.Client> clientAction) {
        android.os.RemoteCallbackList<android.view.accessibility.IAccessibilityManagerClient> userClients = this.mSystemSupport.getCurrentUserClientsLocked();
        android.os.RemoteCallbackList<android.view.accessibility.IAccessibilityManagerClient> globalClients = this.mSystemSupport.getGlobalClientsLocked();
        userClients.broadcastForEachCookie(clientAction);
        globalClients.broadcastForEachCookie(clientAction);
    }

    public void updateTimeoutsIfNeeded(int nonInteractiveUiTimeout, int interactiveUiTimeout) {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mProxyA11yServiceConnections.size(); i++) {
                com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy = this.mProxyA11yServiceConnections.valueAt(i);
                if (proxy != null && proxy.updateTimeouts(nonInteractiveUiTimeout, interactiveUiTimeout)) {
                    scheduleNotifyProxyClientsOfServicesStateChangeLocked(proxy.getDeviceId());
                }
            }
        }
    }

    public long getRecommendedTimeoutMillisLocked(int deviceId) {
        int combinedInteractiveTimeout = 0;
        int combinedNonInteractiveTimeout = 0;
        for (int i = 0; i < this.mProxyA11yServiceConnections.size(); i++) {
            com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy = this.mProxyA11yServiceConnections.valueAt(i);
            if (proxy != null && proxy.getDeviceId() == deviceId) {
                int proxyInteractiveUiTimeout = proxy != null ? proxy.getInteractiveTimeout() : 0;
                int nonInteractiveUiTimeout = proxy != null ? proxy.getNonInteractiveTimeout() : 0;
                combinedInteractiveTimeout = java.lang.Math.max(proxyInteractiveUiTimeout, combinedInteractiveTimeout);
                combinedNonInteractiveTimeout = java.lang.Math.max(nonInteractiveUiTimeout, combinedNonInteractiveTimeout);
            }
        }
        return com.android.internal.util.IntPair.of(combinedInteractiveTimeout, combinedNonInteractiveTimeout);
    }

    public int getFocusStrokeWidthLocked(int deviceId) {
        com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy = getFirstProxyForDeviceIdLocked(deviceId);
        if (proxy != null) {
            return proxy.getFocusStrokeWidthLocked();
        }
        return 0;
    }

    public int getFocusColorLocked(int deviceId) {
        com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy = getFirstProxyForDeviceIdLocked(deviceId);
        if (proxy != null) {
            return proxy.getFocusColorLocked();
        }
        return 0;
    }

    public int getFirstDeviceIdForUidLocked(int callingUid) {
        com.android.server.companion.virtual.VirtualDeviceManagerInternal localVdm = getLocalVdm();
        if (localVdm == null) {
            return -1;
        }
        java.util.Set<java.lang.Integer> deviceIds = localVdm.getDeviceIdsForUid(callingUid);
        for (java.lang.Integer uidDeviceId : deviceIds) {
            if (uidDeviceId.intValue() != 0 && uidDeviceId.intValue() != -1) {
                int firstDeviceId = uidDeviceId.intValue();
                return firstDeviceId;
            }
        }
        return -1;
    }

    private void updateDeviceIdsIfNeededLocked(int deviceId) {
        android.os.RemoteCallbackList<android.view.accessibility.IAccessibilityManagerClient> userClients = this.mSystemSupport.getCurrentUserClientsLocked();
        android.os.RemoteCallbackList<android.view.accessibility.IAccessibilityManagerClient> globalClients = this.mSystemSupport.getGlobalClientsLocked();
        updateDeviceIdsIfNeededLocked(deviceId, userClients);
        updateDeviceIdsIfNeededLocked(deviceId, globalClients);
    }

    private void updateDeviceIdsIfNeededLocked(int deviceId, android.os.RemoteCallbackList<android.view.accessibility.IAccessibilityManagerClient> clients) {
        com.android.server.companion.virtual.VirtualDeviceManagerInternal localVdm = getLocalVdm();
        if (localVdm == null) {
            return;
        }
        for (int i = 0; i < clients.getRegisteredCallbackCount(); i++) {
            com.android.server.accessibility.AccessibilityManagerService.Client client = (com.android.server.accessibility.AccessibilityManagerService.Client) clients.getRegisteredCallbackCookie(i);
            if (com.android.server.accessibility.Flags.proxyUseAppsOnVirtualDeviceListener()) {
                if (deviceId != 0 && deviceId != -1) {
                    boolean uidBelongsToDevice = localVdm.getDeviceIdsForUid(client.mUid).contains(java.lang.Integer.valueOf(deviceId));
                    if (client.mDeviceId != deviceId && uidBelongsToDevice) {
                        if (DEBUG) {
                            android.util.Slog.v(LOG_TAG, "Packages moved to device id " + deviceId + " are " + java.util.Arrays.toString(client.mPackageNames));
                        }
                        client.mDeviceId = deviceId;
                    } else if (client.mDeviceId == deviceId && !uidBelongsToDevice) {
                        client.mDeviceId = 0;
                        if (DEBUG) {
                            android.util.Slog.v(LOG_TAG, "Packages moved to the default device from device id " + deviceId + " are " + java.util.Arrays.toString(client.mPackageNames));
                        }
                    }
                }
            } else if (deviceId != 0 && deviceId != -1 && localVdm.getDeviceIdsForUid(client.mUid).contains(java.lang.Integer.valueOf(deviceId))) {
                if (DEBUG) {
                    android.util.Slog.v(LOG_TAG, "Packages moved to device id " + deviceId + " are " + java.util.Arrays.toString(client.mPackageNames));
                }
                client.mDeviceId = deviceId;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: notifyProxyOfRunningAppsChange, reason: merged with bridge method [inline-methods] */
    public void lambda$registerProxy$0(java.util.Set<java.lang.Integer> allRunningUids) {
        if (DEBUG) {
            android.util.Slog.v(LOG_TAG, "notifyProxyOfRunningAppsChange: " + allRunningUids);
        }
        synchronized (this.mLock) {
            if (this.mProxyA11yServiceConnections.size() == 0) {
                return;
            }
            com.android.server.companion.virtual.VirtualDeviceManagerInternal localVdm = getLocalVdm();
            if (localVdm == null) {
                return;
            }
            android.util.ArraySet<java.lang.Integer> deviceIdsToUpdate = new android.util.ArraySet<>();
            for (int i = 0; i < this.mProxyA11yServiceConnections.size(); i++) {
                com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy = this.mProxyA11yServiceConnections.valueAt(i);
                if (proxy != null) {
                    int proxyDeviceId = proxy.getDeviceId();
                    for (java.lang.Integer uid : allRunningUids) {
                        if (localVdm.getDeviceIdsForUid(uid.intValue()).contains(java.lang.Integer.valueOf(proxyDeviceId))) {
                            deviceIdsToUpdate.add(java.lang.Integer.valueOf(proxyDeviceId));
                        }
                    }
                }
            }
            java.util.Iterator<java.lang.Integer> it = deviceIdsToUpdate.iterator();
            while (it.hasNext()) {
                onProxyChanged(it.next().intValue(), true);
            }
        }
    }

    public void clearCacheLocked() {
        for (int i = 0; i < this.mProxyA11yServiceConnections.size(); i++) {
            com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy = this.mProxyA11yServiceConnections.valueAt(i);
            proxy.notifyClearAccessibilityNodeInfoCache();
        }
    }

    public void setAccessibilityInputFilter(com.android.server.accessibility.AccessibilityInputFilter filter) {
        if (DEBUG) {
            android.util.Slog.v(LOG_TAG, "Set proxy input filter to " + filter);
        }
        this.mA11yInputFilter = filter;
    }

    private com.android.server.companion.virtual.VirtualDeviceManagerInternal getLocalVdm() {
        if (this.mLocalVdm == null) {
            this.mLocalVdm = (com.android.server.companion.virtual.VirtualDeviceManagerInternal) com.android.server.LocalServices.getService(com.android.server.companion.virtual.VirtualDeviceManagerInternal.class);
        }
        return this.mLocalVdm;
    }

    void setLocalVirtualDeviceManager(com.android.server.companion.virtual.VirtualDeviceManagerInternal localVdm) {
        this.mLocalVdm = localVdm;
    }

    void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        synchronized (this.mLock) {
            pw.println();
            pw.println("Proxy manager state:");
            pw.println("    Number of proxy connections: " + this.mProxyA11yServiceConnections.size());
            pw.println("    Registered proxy connections:");
            android.os.RemoteCallbackList<android.view.accessibility.IAccessibilityManagerClient> userClients = this.mSystemSupport.getCurrentUserClientsLocked();
            android.os.RemoteCallbackList<android.view.accessibility.IAccessibilityManagerClient> globalClients = this.mSystemSupport.getGlobalClientsLocked();
            for (int i = 0; i < this.mProxyA11yServiceConnections.size(); i++) {
                com.android.server.accessibility.ProxyAccessibilityServiceConnection proxy = this.mProxyA11yServiceConnections.valueAt(i);
                if (proxy != null) {
                    proxy.dump(fd, pw, args);
                }
                pw.println();
                pw.println("        User clients for proxy's virtual device id");
                printClientsForDeviceId(pw, userClients, proxy.getDeviceId());
                pw.println();
                pw.println("        Global clients for proxy's virtual device id");
                printClientsForDeviceId(pw, globalClients, proxy.getDeviceId());
            }
        }
    }

    private void printClientsForDeviceId(java.io.PrintWriter pw, android.os.RemoteCallbackList<android.view.accessibility.IAccessibilityManagerClient> clients, int deviceId) {
        if (clients != null) {
            for (int j = 0; j < clients.getRegisteredCallbackCount(); j++) {
                com.android.server.accessibility.AccessibilityManagerService.Client client = (com.android.server.accessibility.AccessibilityManagerService.Client) clients.getRegisteredCallbackCookie(j);
                if (client.mDeviceId == deviceId) {
                    pw.println("            " + java.util.Arrays.toString(client.mPackageNames) + "\n");
                }
            }
        }
    }
}
