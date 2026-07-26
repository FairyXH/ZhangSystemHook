package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
class SystemMediaRoute2Provider extends com.android.server.media.MediaRoute2Provider {
    static final java.lang.String SYSTEM_SESSION_ID = "SYSTEM_SESSION";
    private final android.media.AudioManager mAudioManager;
    private final com.android.server.media.SystemMediaRoute2Provider.AudioManagerBroadcastReceiver mAudioReceiver;
    private final com.android.server.media.BluetoothRouteController mBluetoothRouteController;
    private final android.content.Context mContext;
    android.media.MediaRoute2Info mDefaultRoute;
    android.media.RoutingSessionInfo mDefaultSessionInfo;
    private final com.android.server.media.DeviceRouteController mDeviceRouteController;
    private final android.os.Handler mHandler;
    private volatile com.android.server.media.MediaRoute2Provider.SessionCreationOrTransferRequest mPendingSessionCreationOrTransferRequest;
    private volatile com.android.server.media.MediaRoute2Provider.SessionCreationOrTransferRequest mPendingTransferRequest;
    private final java.lang.Object mRequestLock;
    private java.lang.String mSelectedRouteId;
    private final java.lang.Object mTransferLock;
    private final android.os.UserHandle mUser;
    static final java.lang.String TAG = "MR2SystemProvider";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private static final android.content.ComponentName COMPONENT_NAME = new android.content.ComponentName(com.android.server.media.SystemMediaRoute2Provider.class.getPackage().getName(), com.android.server.media.SystemMediaRoute2Provider.class.getName());

    SystemMediaRoute2Provider(android.content.Context context, android.os.UserHandle user, android.os.Looper looper) {
        super(COMPONENT_NAME);
        this.mAudioReceiver = new com.android.server.media.SystemMediaRoute2Provider.AudioManagerBroadcastReceiver();
        this.mRequestLock = new java.lang.Object();
        this.mTransferLock = new java.lang.Object();
        this.mIsSystemRouteProvider = true;
        this.mContext = context;
        this.mUser = user;
        this.mHandler = new android.os.Handler(looper);
        this.mAudioManager = (android.media.AudioManager) context.getSystemService("audio");
        this.mBluetoothRouteController = com.android.server.media.BluetoothRouteController.createInstance(context, new com.android.server.media.BluetoothRouteController.BluetoothRoutesUpdatedListener() { // from class: com.android.server.media.SystemMediaRoute2Provider$$ExternalSyntheticLambda3
            @Override // com.android.server.media.BluetoothRouteController.BluetoothRoutesUpdatedListener
            public final void onBluetoothRoutesUpdated() {
                this.f$0.lambda$new$0();
            }
        });
        this.mDeviceRouteController = com.android.server.media.DeviceRouteController.createInstance(context, looper, new com.android.server.media.DeviceRouteController.OnDeviceRouteChangedListener() { // from class: com.android.server.media.SystemMediaRoute2Provider$$ExternalSyntheticLambda4
            @Override // com.android.server.media.DeviceRouteController.OnDeviceRouteChangedListener
            public final void onDeviceRouteChanged() {
                this.f$0.lambda$new$2();
            }
        });
        updateProviderState();
        updateSessionInfosIfNeeded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        publishProviderState();
        if (updateSessionInfosIfNeeded()) {
            notifySessionInfoUpdated();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.SystemMediaRoute2Provider$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        publishProviderState();
        if (updateSessionInfosIfNeeded()) {
            notifySessionInfoUpdated();
        }
    }

    public void start() {
        android.content.IntentFilter intentFilter = new android.content.IntentFilter("android.media.VOLUME_CHANGED_ACTION");
        intentFilter.addAction("android.media.STREAM_DEVICES_CHANGED_ACTION");
        this.mContext.registerReceiverAsUser(this.mAudioReceiver, this.mUser, intentFilter, null, null);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.SystemMediaRoute2Provider$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$start$3();
            }
        });
        updateVolume();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$3() {
        this.mDeviceRouteController.start(this.mUser);
        this.mBluetoothRouteController.start(this.mUser);
    }

    public void stop() {
        this.mContext.unregisterReceiver(this.mAudioReceiver);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.SystemMediaRoute2Provider$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$stop$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stop$4() {
        this.mBluetoothRouteController.stop();
        this.mDeviceRouteController.stop();
        notifyProviderState();
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void setCallback(com.android.server.media.MediaRoute2Provider.Callback callback) {
        super.setCallback(callback);
        notifyProviderState();
        notifySessionInfoUpdated();
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void requestCreateSession(long requestId, java.lang.String packageName, java.lang.String routeOriginalId, android.os.Bundle sessionHints, int transferReason, android.os.UserHandle transferInitiatorUserHandle, java.lang.String transferInitiatorPackageName) {
        android.media.RoutingSessionInfo currentSessionInfo;
        if (android.text.TextUtils.equals(routeOriginalId, "DEFAULT_ROUTE")) {
            this.mCallback.onSessionCreated(this, requestId, this.mDefaultSessionInfo);
            return;
        }
        if (!com.android.media.flags.Flags.enableBuiltInSpeakerRouteSuitabilityStatuses() && android.text.TextUtils.equals(routeOriginalId, this.mSelectedRouteId)) {
            synchronized (this.mLock) {
                currentSessionInfo = this.mSessionInfos.get(0);
            }
            this.mCallback.onSessionCreated(this, requestId, currentSessionInfo);
            return;
        }
        synchronized (this.mRequestLock) {
            if (this.mPendingSessionCreationOrTransferRequest != null) {
                this.mCallback.onRequestFailed(this, this.mPendingSessionCreationOrTransferRequest.mRequestId, 0);
            }
            this.mPendingSessionCreationOrTransferRequest = new com.android.server.media.MediaRoute2Provider.SessionCreationOrTransferRequest(requestId, routeOriginalId, 0, transferInitiatorUserHandle, transferInitiatorPackageName);
        }
        transferToRoute(requestId, transferInitiatorUserHandle, transferInitiatorPackageName, SYSTEM_SESSION_ID, routeOriginalId, transferReason);
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void releaseSession(long requestId, java.lang.String sessionId) {
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void updateDiscoveryPreference(java.util.Set<java.lang.String> activelyScanningPackages, android.media.RouteDiscoveryPreference discoveryPreference) {
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void selectRoute(long requestId, java.lang.String sessionId, java.lang.String routeId) {
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void deselectRoute(long requestId, java.lang.String sessionId, java.lang.String routeId) {
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void transferToRoute(long requestId, android.os.UserHandle transferInitiatorUserHandle, java.lang.String transferInitiatorPackageName, java.lang.String sessionOriginalId, java.lang.String routeOriginalId, int transferReason) {
        java.lang.String selectedDeviceRouteId = this.mDeviceRouteController.getSelectedRoute().getId();
        java.lang.String routeOriginalId2 = routeOriginalId;
        if (android.text.TextUtils.equals(routeOriginalId2, "DEFAULT_ROUTE")) {
            if (com.android.media.flags.Flags.enableBuiltInSpeakerRouteSuitabilityStatuses()) {
                routeOriginalId2 = selectedDeviceRouteId;
            } else {
                android.util.Log.w(TAG, "Ignoring transfer to DEFAULT_ROUTE");
                return;
            }
        }
        if (com.android.media.flags.Flags.enableBuiltInSpeakerRouteSuitabilityStatuses()) {
            synchronized (this.mTransferLock) {
                this.mPendingTransferRequest = new com.android.server.media.MediaRoute2Provider.SessionCreationOrTransferRequest(requestId, routeOriginalId2, transferReason, transferInitiatorUserHandle, transferInitiatorPackageName);
            }
        }
        final java.lang.String finalRouteId = routeOriginalId2;
        boolean isAvailableDeviceRoute = this.mDeviceRouteController.getAvailableRoutes().stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.media.SystemMediaRoute2Provider$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((android.media.MediaRoute2Info) obj).getId().equals(finalRouteId);
            }
        });
        boolean isSelectedDeviceRoute = android.text.TextUtils.equals(routeOriginalId2, selectedDeviceRouteId);
        if (!isSelectedDeviceRoute && !isAvailableDeviceRoute) {
            this.mDeviceRouteController.transferTo(null);
            this.mBluetoothRouteController.transferTo(routeOriginalId2);
        } else {
            this.mDeviceRouteController.transferTo(routeOriginalId2);
            this.mBluetoothRouteController.transferTo(null);
        }
        if (com.android.media.flags.Flags.enableBuiltInSpeakerRouteSuitabilityStatuses() && updateSessionInfosIfNeeded()) {
            notifySessionInfoUpdated();
        }
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void setRouteVolume(long requestId, java.lang.String routeOriginalId, int volume) {
        if (!android.text.TextUtils.equals(routeOriginalId, this.mSelectedRouteId)) {
            return;
        }
        this.mAudioManager.setStreamVolume(3, volume, 0);
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void setSessionVolume(long requestId, java.lang.String sessionOriginalId, int volume) {
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void prepareReleaseSession(java.lang.String sessionUniqueId) {
    }

    public android.media.MediaRoute2Info getDefaultRoute() {
        return this.mDefaultRoute;
    }

    public android.media.RoutingSessionInfo getDefaultSessionInfo() {
        return this.mDefaultSessionInfo;
    }

    public android.media.RoutingSessionInfo generateDeviceRouteSelectedSessionInfo(java.lang.String packageName) {
        synchronized (this.mLock) {
            if (this.mSessionInfos.isEmpty()) {
                return null;
            }
            android.media.MediaRoute2Info selectedDeviceRoute = this.mDeviceRouteController.getSelectedRoute();
            android.media.RoutingSessionInfo.Builder builder = new android.media.RoutingSessionInfo.Builder(SYSTEM_SESSION_ID, packageName).setSystemSession(true);
            builder.addSelectedRoute(selectedDeviceRoute.getId());
            java.util.Iterator<android.media.MediaRoute2Info> it = this.mBluetoothRouteController.getAllBluetoothRoutes().iterator();
            while (it.hasNext()) {
                builder.addTransferableRoute(it.next().getId());
            }
            if (com.android.media.flags.Flags.enableAudioPoliciesDeviceAndBluetoothController()) {
                for (android.media.MediaRoute2Info route : this.mDeviceRouteController.getAvailableRoutes()) {
                    if (!android.text.TextUtils.equals(selectedDeviceRoute.getId(), route.getId())) {
                        builder.addTransferableRoute(route.getId());
                    }
                }
            }
            if (com.android.media.flags.Flags.enableBuiltInSpeakerRouteSuitabilityStatuses()) {
                android.media.RoutingSessionInfo oldSessionInfo = this.mSessionInfos.get(0);
                builder.setTransferReason(oldSessionInfo.getTransferReason()).setTransferInitiator(oldSessionInfo.getTransferInitiatorUserHandle(), oldSessionInfo.getTransferInitiatorPackageName());
            }
            return builder.setProviderId(this.mUniqueId).build();
        }
    }

    private void updateProviderState() {
        android.media.MediaRoute2ProviderInfo.Builder builder = new android.media.MediaRoute2ProviderInfo.Builder();
        if (com.android.media.flags.Flags.enableAudioPoliciesDeviceAndBluetoothController()) {
            java.util.List<android.media.MediaRoute2Info> deviceRoutes = this.mDeviceRouteController.getAvailableRoutes();
            for (android.media.MediaRoute2Info route : deviceRoutes) {
                builder.addRoute(route);
            }
            setProviderState(builder.build());
        } else {
            builder.addRoute(this.mDeviceRouteController.getSelectedRoute());
        }
        for (android.media.MediaRoute2Info route2 : this.mBluetoothRouteController.getAllBluetoothRoutes()) {
            builder.addRoute(route2);
        }
        android.media.MediaRoute2ProviderInfo providerInfo = builder.build();
        setProviderState(providerInfo);
        if (DEBUG) {
            android.util.Slog.d(TAG, "Updating system provider info : " + providerInfo);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:74:0x013a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean updateSessionInfosIfNeeded() {
        /*
            Method dump skipped, instruction units count: 426
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.media.SystemMediaRoute2Provider.updateSessionInfosIfNeeded():boolean");
    }

    private void reportPendingSessionRequestResultLockedIfNeeded(android.media.RoutingSessionInfo newSessionInfo) {
        if (this.mPendingSessionCreationOrTransferRequest == null) {
            return;
        }
        long pendingRequestId = this.mPendingSessionCreationOrTransferRequest.mRequestId;
        if (this.mPendingSessionCreationOrTransferRequest.mTargetOriginalRouteId.equals(this.mSelectedRouteId)) {
            if (DEBUG) {
                android.util.Slog.w(TAG, "Session creation success to route " + this.mPendingSessionCreationOrTransferRequest.mTargetOriginalRouteId);
            }
            this.mPendingSessionCreationOrTransferRequest = null;
            this.mCallback.onSessionCreated(this, pendingRequestId, newSessionInfo);
            return;
        }
        boolean isRequestedRouteConnectedBtRoute = isRequestedRouteConnectedBtRoute();
        if (!com.android.media.flags.Flags.enableWaitingStateForSystemSessionCreationRequest() || !isRequestedRouteConnectedBtRoute) {
            if (DEBUG) {
                android.util.Slog.w(TAG, "Session creation failed to route " + this.mPendingSessionCreationOrTransferRequest.mTargetOriginalRouteId);
            }
            this.mPendingSessionCreationOrTransferRequest = null;
            this.mCallback.onRequestFailed(this, pendingRequestId, 0);
            return;
        }
        if (DEBUG) {
            android.util.Slog.w(TAG, "Session creation waiting state to route " + this.mPendingSessionCreationOrTransferRequest.mTargetOriginalRouteId);
        }
    }

    private boolean isRequestedRouteConnectedBtRoute() {
        for (android.media.MediaRoute2Info btRoute : this.mBluetoothRouteController.getAllBluetoothRoutes()) {
            if (android.text.TextUtils.equals(btRoute.getId(), this.mPendingSessionCreationOrTransferRequest.mTargetOriginalRouteId)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsSelectedRouteWithId(android.media.RoutingSessionInfo sessionInfo, java.lang.String selectedRouteId) {
        if (sessionInfo == null) {
            return false;
        }
        java.util.List<java.lang.String> selectedRoutes = sessionInfo.getSelectedRoutes();
        if (selectedRoutes.size() != 1) {
            throw new java.lang.IllegalStateException("Selected routes list should contain only 1 route id.");
        }
        java.lang.String oldSelectedRouteId = android.media.MediaRouter2Utils.getOriginalId(selectedRoutes.get(0));
        return oldSelectedRouteId != null && oldSelectedRouteId.equals(selectedRouteId);
    }

    void publishProviderState() {
        updateProviderState();
        notifyProviderState();
    }

    void notifySessionInfoUpdated() {
        android.media.RoutingSessionInfo sessionInfo;
        if (this.mCallback == null) {
            return;
        }
        synchronized (this.mLock) {
            sessionInfo = this.mSessionInfos.get(0);
        }
        this.mCallback.onSessionUpdated(this, sessionInfo);
    }

    @Override // com.android.server.media.MediaRoute2Provider
    protected java.lang.String getDebugString() {
        return android.text.TextUtils.formatSimple("SystemMR2Provider - package: %s, selected route id: %s, bluetooth impl: %s", new java.lang.Object[]{this.mComponentName.getPackageName(), this.mSelectedRouteId, this.mBluetoothRouteController.getClass().getSimpleName()});
    }

    void updateVolume() {
        int devices = this.mAudioManager.getDevicesForStream(3);
        int volume = this.mAudioManager.getStreamVolume(3);
        if (this.mDefaultRoute.getVolume() != volume) {
            this.mDefaultRoute = new android.media.MediaRoute2Info.Builder(this.mDefaultRoute).setVolume(volume).build();
        }
        if (android.os.Build.isMtkPlatform()) {
            int allBluetoothRoutesSize = this.mBluetoothRouteController.getAllBluetoothRoutes().size();
            if (allBluetoothRoutesSize > 0 && this.mBluetoothRouteController.updateVolumeForDevices(devices, volume)) {
                return;
            }
        } else if (this.mBluetoothRouteController.updateVolumeForDevices(devices, volume)) {
            return;
        }
        this.mDeviceRouteController.updateVolume(volume);
        publishProviderState();
    }

    private class AudioManagerBroadcastReceiver extends android.content.BroadcastReceiver {
        private AudioManagerBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (!intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION") && !intent.getAction().equals("android.media.STREAM_DEVICES_CHANGED_ACTION")) {
                return;
            }
            int streamType = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
            if (streamType != 3) {
                return;
            }
            if (com.android.media.flags.Flags.enableMr2ServiceNonMainBgThread()) {
                android.os.Handler handler = com.android.server.media.SystemMediaRoute2Provider.this.mHandler;
                final com.android.server.media.SystemMediaRoute2Provider systemMediaRoute2Provider = com.android.server.media.SystemMediaRoute2Provider.this;
                handler.post(new java.lang.Runnable() { // from class: com.android.server.media.SystemMediaRoute2Provider$AudioManagerBroadcastReceiver$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        systemMediaRoute2Provider.updateVolume();
                    }
                });
                return;
            }
            com.android.server.media.SystemMediaRoute2Provider.this.updateVolume();
        }
    }
}
