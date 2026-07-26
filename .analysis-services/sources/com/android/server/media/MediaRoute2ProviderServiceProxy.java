package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
final class MediaRoute2ProviderServiceProxy extends com.android.server.media.MediaRoute2Provider {
    private com.android.server.media.MediaRoute2ProviderServiceProxy.Connection mActiveConnection;
    private boolean mBound;
    private boolean mConnectionReady;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private boolean mIsManagerScanning;
    private final boolean mIsSelfScanOnlyProvider;
    private android.media.RouteDiscoveryPreference mLastDiscoveryPreference;
    private boolean mLastDiscoveryPreferenceIncludesThisPackage;
    private final java.util.List<android.media.RoutingSessionInfo> mReleasingSessions;
    private final android.util.LongSparseArray<com.android.server.media.MediaRoute2Provider.SessionCreationOrTransferRequest> mRequestIdToSessionCreationRequest;
    private boolean mRunning;
    private final android.content.ServiceConnection mServiceConnection;
    private final java.util.Map<java.lang.String, com.android.server.media.MediaRoute2Provider.SessionCreationOrTransferRequest> mSessionOriginalIdToTransferRequest;
    private final int mUserId;
    private static final java.lang.String TAG = "MR2ProviderSvcProxy";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);

    MediaRoute2ProviderServiceProxy(android.content.Context context, android.os.Looper looper, android.content.ComponentName componentName, boolean isSelfScanOnlyProvider, int userId) {
        super(componentName);
        this.mServiceConnection = new com.android.server.media.MediaRoute2ProviderServiceProxy.ServiceConnectionImpl();
        this.mLastDiscoveryPreference = null;
        this.mLastDiscoveryPreferenceIncludesThisPackage = false;
        this.mReleasingSessions = new java.util.ArrayList();
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context, "Context must not be null.");
        this.mRequestIdToSessionCreationRequest = new android.util.LongSparseArray<>();
        this.mSessionOriginalIdToTransferRequest = new java.util.HashMap();
        this.mIsSelfScanOnlyProvider = isSelfScanOnlyProvider;
        this.mUserId = userId;
        this.mHandler = new android.os.Handler(looper);
    }

    public void setManagerScanning(boolean managerScanning) {
        if (this.mIsManagerScanning != managerScanning) {
            this.mIsManagerScanning = managerScanning;
            updateBinding();
        }
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void requestCreateSession(long requestId, java.lang.String packageName, java.lang.String routeOriginalId, android.os.Bundle sessionHints, int transferReason, android.os.UserHandle transferInitiatorUserHandle, java.lang.String transferInitiatorPackageName) throws java.lang.Throwable {
        if (this.mConnectionReady) {
            if (com.android.media.flags.Flags.enableBuiltInSpeakerRouteSuitabilityStatuses()) {
                synchronized (this.mLock) {
                    try {
                        try {
                            this.mRequestIdToSessionCreationRequest.put(requestId, new com.android.server.media.MediaRoute2Provider.SessionCreationOrTransferRequest(requestId, routeOriginalId, transferReason, transferInitiatorUserHandle, transferInitiatorPackageName));
                        } catch (java.lang.Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
            }
            this.mActiveConnection.requestCreateSession(requestId, packageName, routeOriginalId, sessionHints);
            updateBinding();
        }
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void releaseSession(long requestId, java.lang.String sessionId) {
        if (this.mConnectionReady) {
            if (com.android.media.flags.Flags.enableBuiltInSpeakerRouteSuitabilityStatuses()) {
                synchronized (this.mLock) {
                    this.mSessionOriginalIdToTransferRequest.remove(sessionId);
                }
            }
            this.mActiveConnection.releaseSession(requestId, sessionId);
            updateBinding();
        }
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void updateDiscoveryPreference(java.util.Set<java.lang.String> activelyScanningPackages, android.media.RouteDiscoveryPreference discoveryPreference) {
        this.mLastDiscoveryPreference = discoveryPreference;
        this.mLastDiscoveryPreferenceIncludesThisPackage = activelyScanningPackages.contains(this.mComponentName.getPackageName());
        if (this.mConnectionReady) {
            this.mActiveConnection.updateDiscoveryPreference(discoveryPreference);
        }
        updateBinding();
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void selectRoute(long requestId, java.lang.String sessionId, java.lang.String routeId) {
        if (this.mConnectionReady) {
            this.mActiveConnection.selectRoute(requestId, sessionId, routeId);
        }
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void deselectRoute(long requestId, java.lang.String sessionId, java.lang.String routeId) {
        if (this.mConnectionReady) {
            this.mActiveConnection.deselectRoute(requestId, sessionId, routeId);
        }
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void transferToRoute(long requestId, android.os.UserHandle transferInitiatorUserHandle, java.lang.String transferInitiatorPackageName, java.lang.String sessionOriginalId, java.lang.String routeOriginalId, int transferReason) {
        if (this.mConnectionReady) {
            if (com.android.media.flags.Flags.enableBuiltInSpeakerRouteSuitabilityStatuses()) {
                synchronized (this.mLock) {
                    this.mSessionOriginalIdToTransferRequest.put(sessionOriginalId, new com.android.server.media.MediaRoute2Provider.SessionCreationOrTransferRequest(requestId, routeOriginalId, transferReason, transferInitiatorUserHandle, transferInitiatorPackageName));
                }
            }
            this.mActiveConnection.transferToRoute(requestId, sessionOriginalId, routeOriginalId);
        }
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void setRouteVolume(long requestId, java.lang.String routeOriginalId, int volume) {
        if (this.mConnectionReady) {
            this.mActiveConnection.setRouteVolume(requestId, routeOriginalId, volume);
            updateBinding();
        }
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void setSessionVolume(long requestId, java.lang.String sessionOriginalId, int volume) {
        if (this.mConnectionReady) {
            this.mActiveConnection.setSessionVolume(requestId, sessionOriginalId, volume);
            updateBinding();
        }
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void prepareReleaseSession(java.lang.String sessionUniqueId) {
        synchronized (this.mLock) {
            java.util.Iterator<android.media.RoutingSessionInfo> it = this.mSessionInfos.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                android.media.RoutingSessionInfo session = it.next();
                if (android.text.TextUtils.equals(session.getId(), sessionUniqueId)) {
                    this.mSessionInfos.remove(session);
                    this.mReleasingSessions.add(session);
                    break;
                }
            }
        }
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public boolean hasComponentName(java.lang.String packageName, java.lang.String className) {
        return this.mComponentName.getPackageName().equals(packageName) && this.mComponentName.getClassName().equals(className);
    }

    public void start(boolean rebindIfDisconnected) {
        if (!this.mRunning) {
            if (DEBUG) {
                android.util.Slog.d(TAG, this + ": Starting");
            }
            this.mRunning = true;
            if (!com.android.media.flags.Flags.enablePreventionOfKeepAliveRouteProviders()) {
                updateBinding();
            }
        }
        if (rebindIfDisconnected && this.mActiveConnection == null && shouldBind()) {
            unbind();
            bind();
        }
    }

    public void stop() {
        if (this.mRunning) {
            if (DEBUG) {
                android.util.Slog.d(TAG, this + ": Stopping");
            }
            this.mRunning = false;
            updateBinding();
        }
    }

    private void updateBinding() {
        if (shouldBind()) {
            bind();
        } else {
            unbind();
        }
    }

    private boolean shouldBind() {
        if (!this.mRunning) {
            return false;
        }
        boolean bindDueToManagerScan = this.mIsManagerScanning && !com.android.media.flags.Flags.enablePreventionOfManagerScansWhenNoAppsScan();
        if (!getSessionInfos().isEmpty() || bindDueToManagerScan) {
            return true;
        }
        boolean anAppIsScanning = (this.mLastDiscoveryPreference == null || this.mLastDiscoveryPreference.getPreferredFeatures().isEmpty()) ? false : true;
        if (anAppIsScanning) {
            return this.mLastDiscoveryPreferenceIncludesThisPackage || !this.mIsSelfScanOnlyProvider;
        }
        return false;
    }

    private void bind() {
        if (!this.mBound) {
            if (DEBUG) {
                android.util.Slog.d(TAG, this + ": Binding");
            }
            android.content.Intent service = new android.content.Intent("android.media.MediaRoute2ProviderService");
            service.setComponent(this.mComponentName);
            try {
                this.mBound = this.mContext.bindServiceAsUser(service, this.mServiceConnection, android.hardware.audio.common.V2_0.AudioFormat.AAC_MAIN, new android.os.UserHandle(this.mUserId));
                if (!this.mBound && DEBUG) {
                    android.util.Slog.d(TAG, this + ": Bind failed");
                }
            } catch (java.lang.SecurityException ex) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, this + ": Bind failed", ex);
                }
            }
        }
    }

    private void unbind() {
        if (this.mBound) {
            if (DEBUG) {
                android.util.Slog.d(TAG, this + ": Unbinding");
            }
            this.mBound = false;
            disconnect();
            this.mContext.unbindService(this.mServiceConnection);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onServiceConnectedInternal(android.os.IBinder service) {
        if (DEBUG) {
            android.util.Slog.d(TAG, this + ": Connected");
        }
        if (this.mBound) {
            disconnect();
            android.media.IMediaRoute2ProviderService serviceBinder = android.media.IMediaRoute2ProviderService.Stub.asInterface(service);
            if (serviceBinder != null) {
                com.android.server.media.MediaRoute2ProviderServiceProxy.Connection connection = new com.android.server.media.MediaRoute2ProviderServiceProxy.Connection(serviceBinder);
                if (connection.register()) {
                    this.mActiveConnection = connection;
                    return;
                } else {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, this + ": Registration failed");
                        return;
                    }
                    return;
                }
            }
            android.util.Slog.e(TAG, this + ": Service returned invalid binder");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onServiceDisconnectedInternal() {
        if (DEBUG) {
            android.util.Slog.d(TAG, this + ": Service disconnected");
        }
        disconnect();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBindingDiedInternal(android.content.ComponentName name) {
        unbind();
        if (com.android.media.flags.Flags.enablePreventionOfKeepAliveRouteProviders()) {
            android.util.Slog.w(TAG, android.text.TextUtils.formatSimple("Route provider service (%s) binding died, but we did not rebind.", new java.lang.Object[]{name.toString()}));
        } else if (shouldBind()) {
            android.util.Slog.w(TAG, android.text.TextUtils.formatSimple("Rebound to provider service (%s) after binding died.", new java.lang.Object[]{name.toString()}));
            bind();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConnectionReady(com.android.server.media.MediaRoute2ProviderServiceProxy.Connection connection) {
        java.util.Set<java.lang.String> setOf;
        if (this.mActiveConnection == connection) {
            this.mConnectionReady = true;
            if (this.mLastDiscoveryPreference != null) {
                if (this.mLastDiscoveryPreferenceIncludesThisPackage) {
                    setOf = java.util.Set.of(this.mComponentName.getPackageName());
                } else {
                    setOf = java.util.Set.of();
                }
                updateDiscoveryPreference(setOf, this.mLastDiscoveryPreference);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConnectionDied(com.android.server.media.MediaRoute2ProviderServiceProxy.Connection connection) {
        if (this.mActiveConnection == connection) {
            if (DEBUG) {
                android.util.Slog.d(TAG, this + ": Service connection died");
            }
            disconnect();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onProviderUpdated(com.android.server.media.MediaRoute2ProviderServiceProxy.Connection connection, android.media.MediaRoute2ProviderInfo providerInfo) {
        if (this.mActiveConnection != connection) {
            return;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, this + ": updated");
        }
        setAndNotifyProviderState(providerInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSessionCreated(com.android.server.media.MediaRoute2ProviderServiceProxy.Connection connection, long requestId, android.media.RoutingSessionInfo newSession) {
        if (this.mActiveConnection != connection) {
            return;
        }
        if (newSession == null) {
            android.util.Slog.w(TAG, "onSessionCreated: Ignoring null session sent from " + this.mComponentName);
            return;
        }
        android.media.RoutingSessionInfo newSession2 = assignProviderIdForSession(newSession);
        final java.lang.String newSessionId = newSession2.getId();
        synchronized (this.mLock) {
            if (com.android.media.flags.Flags.enableBuiltInSpeakerRouteSuitabilityStatuses()) {
                newSession2 = createSessionWithPopulatedTransferInitiationDataLocked(requestId, null, newSession2);
            }
            if (!this.mSessionInfos.stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.media.MediaRoute2ProviderServiceProxy$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return android.text.TextUtils.equals(((android.media.RoutingSessionInfo) obj).getId(), newSessionId);
                }
            }) && !this.mReleasingSessions.stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.media.MediaRoute2ProviderServiceProxy$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return android.text.TextUtils.equals(((android.media.RoutingSessionInfo) obj).getId(), newSessionId);
                }
            })) {
                this.mSessionInfos.add(newSession2);
                this.mCallback.onSessionCreated(this, requestId, newSession2);
                return;
            }
            android.util.Slog.w(TAG, "onSessionCreated: Duplicate session already exists. Ignoring.");
        }
    }

    private int findSessionByIdLocked(android.media.RoutingSessionInfo session) {
        for (int i = 0; i < this.mSessionInfos.size(); i++) {
            if (android.text.TextUtils.equals(this.mSessionInfos.get(i).getId(), session.getId())) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSessionsUpdated(com.android.server.media.MediaRoute2ProviderServiceProxy.Connection connection, java.util.List<android.media.RoutingSessionInfo> sessions) throws java.lang.Throwable {
        java.lang.Throwable th;
        if (this.mActiveConnection != connection) {
            return;
        }
        int targetIndex = 0;
        synchronized (this.mLock) {
            try {
                try {
                    for (android.media.RoutingSessionInfo session : sessions) {
                        if (session != null) {
                            android.media.RoutingSessionInfo session2 = assignProviderIdForSession(session);
                            int sourceIndex = findSessionByIdLocked(session2);
                            if (sourceIndex < 0) {
                                int targetIndex2 = targetIndex + 1;
                                try {
                                    this.mSessionInfos.add(targetIndex, session2);
                                    dispatchSessionCreated(0L, session2);
                                    targetIndex = targetIndex2;
                                } catch (java.lang.Throwable th2) {
                                    th = th2;
                                    throw th;
                                }
                            } else if (sourceIndex < targetIndex) {
                                android.util.Slog.w(TAG, "Ignoring duplicate session ID: " + session2.getId());
                            } else {
                                if (com.android.media.flags.Flags.enableBuiltInSpeakerRouteSuitabilityStatuses()) {
                                    android.media.RoutingSessionInfo oldSessionInfo = this.mSessionInfos.get(sourceIndex);
                                    session2 = createSessionWithPopulatedTransferInitiationDataLocked(0L, oldSessionInfo, session2);
                                }
                                this.mSessionInfos.set(sourceIndex, session2);
                                int targetIndex3 = targetIndex + 1;
                                java.util.Collections.swap(this.mSessionInfos, sourceIndex, targetIndex);
                                dispatchSessionUpdated(session2);
                                targetIndex = targetIndex3;
                            }
                        }
                    }
                    for (int i = this.mSessionInfos.size() - 1; i >= targetIndex; i--) {
                        android.media.RoutingSessionInfo releasedSession = this.mSessionInfos.remove(i);
                        this.mSessionOriginalIdToTransferRequest.remove(releasedSession.getId());
                        dispatchSessionReleased(releasedSession);
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
        }
    }

    private android.media.RoutingSessionInfo createSessionWithPopulatedTransferInitiationDataLocked(long requestId, android.media.RoutingSessionInfo oldSessionInfo, android.media.RoutingSessionInfo newSessionInfo) {
        com.android.server.media.MediaRoute2Provider.SessionCreationOrTransferRequest pendingRequest;
        int transferReason;
        android.os.UserHandle transferInitiatorUserHandle;
        java.lang.String transferInitiatorPackageName;
        if (oldSessionInfo != null) {
            pendingRequest = this.mSessionOriginalIdToTransferRequest.get(newSessionInfo.getOriginalId());
        } else {
            pendingRequest = this.mRequestIdToSessionCreationRequest.get(requestId);
        }
        boolean pendingTargetRouteInSelectedRoutes = pendingRequest != null && pendingRequest.isTargetRouteIdInRouteUniqueIdList(newSessionInfo.getSelectedRoutes());
        boolean pendingTargetRouteInTransferableRoutes = pendingRequest != null && pendingRequest.isTargetRouteIdInRouteUniqueIdList(newSessionInfo.getTransferableRoutes());
        if (pendingTargetRouteInSelectedRoutes) {
            transferReason = pendingRequest.mTransferReason;
            transferInitiatorUserHandle = pendingRequest.mTransferInitiatorUserHandle;
            transferInitiatorPackageName = pendingRequest.mTransferInitiatorPackageName;
        } else if (oldSessionInfo != null) {
            transferReason = oldSessionInfo.getTransferReason();
            transferInitiatorUserHandle = oldSessionInfo.getTransferInitiatorUserHandle();
            transferInitiatorPackageName = oldSessionInfo.getTransferInitiatorPackageName();
        } else {
            transferReason = 0;
            transferInitiatorUserHandle = android.os.UserHandle.of(this.mUserId);
            transferInitiatorPackageName = newSessionInfo.getClientPackageName();
        }
        if (pendingTargetRouteInSelectedRoutes || !pendingTargetRouteInTransferableRoutes) {
            if (oldSessionInfo != null) {
                this.mSessionOriginalIdToTransferRequest.remove(newSessionInfo.getId());
            } else if (pendingRequest != null) {
                this.mRequestIdToSessionCreationRequest.remove(pendingRequest.mRequestId);
            }
        }
        return new android.media.RoutingSessionInfo.Builder(newSessionInfo).setTransferInitiator(transferInitiatorUserHandle, transferInitiatorPackageName).setTransferReason(transferReason).build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSessionReleased(com.android.server.media.MediaRoute2ProviderServiceProxy.Connection connection, android.media.RoutingSessionInfo releasedSession) {
        if (this.mActiveConnection != connection) {
            return;
        }
        if (releasedSession == null) {
            android.util.Slog.w(TAG, "onSessionReleased: Ignoring null session sent from " + this.mComponentName);
            return;
        }
        android.media.RoutingSessionInfo releasedSession2 = assignProviderIdForSession(releasedSession);
        boolean found = false;
        synchronized (this.mLock) {
            this.mSessionOriginalIdToTransferRequest.remove(releasedSession2.getId());
            java.util.Iterator<android.media.RoutingSessionInfo> it = this.mSessionInfos.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                android.media.RoutingSessionInfo session = it.next();
                if (android.text.TextUtils.equals(session.getId(), releasedSession2.getId())) {
                    this.mSessionInfos.remove(session);
                    found = true;
                    break;
                }
            }
            if (!found) {
                for (android.media.RoutingSessionInfo session2 : this.mReleasingSessions) {
                    if (android.text.TextUtils.equals(session2.getId(), releasedSession2.getId())) {
                        this.mReleasingSessions.remove(session2);
                        return;
                    }
                }
            }
            if (!found) {
                android.util.Slog.w(TAG, "onSessionReleased: Matching session info not found");
            } else {
                this.mCallback.onSessionReleased(this, releasedSession2);
            }
        }
    }

    private void dispatchSessionCreated(long requestId, android.media.RoutingSessionInfo session) {
        android.os.Handler handler = this.mHandler;
        final com.android.server.media.MediaRoute2Provider.Callback callback = this.mCallback;
        java.util.Objects.requireNonNull(callback);
        handler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.media.MediaRoute2ProviderServiceProxy$$ExternalSyntheticLambda4
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                callback.onSessionCreated((com.android.server.media.MediaRoute2ProviderServiceProxy) obj, ((java.lang.Long) obj2).longValue(), (android.media.RoutingSessionInfo) obj3);
            }
        }, this, java.lang.Long.valueOf(requestId), session));
    }

    private void dispatchSessionUpdated(android.media.RoutingSessionInfo session) {
        android.os.Handler handler = this.mHandler;
        final com.android.server.media.MediaRoute2Provider.Callback callback = this.mCallback;
        java.util.Objects.requireNonNull(callback);
        handler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.media.MediaRoute2ProviderServiceProxy$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                callback.onSessionUpdated((com.android.server.media.MediaRoute2ProviderServiceProxy) obj, (android.media.RoutingSessionInfo) obj2);
            }
        }, this, session));
    }

    private void dispatchSessionReleased(android.media.RoutingSessionInfo session) {
        android.os.Handler handler = this.mHandler;
        final com.android.server.media.MediaRoute2Provider.Callback callback = this.mCallback;
        java.util.Objects.requireNonNull(callback);
        handler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.media.MediaRoute2ProviderServiceProxy$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                callback.onSessionReleased((com.android.server.media.MediaRoute2ProviderServiceProxy) obj, (android.media.RoutingSessionInfo) obj2);
            }
        }, this, session));
    }

    private android.media.RoutingSessionInfo assignProviderIdForSession(android.media.RoutingSessionInfo sessionInfo) {
        return new android.media.RoutingSessionInfo.Builder(sessionInfo).setOwnerPackageName(this.mComponentName.getPackageName()).setProviderId(getUniqueId()).build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRequestFailed(com.android.server.media.MediaRoute2ProviderServiceProxy.Connection connection, long requestId, int reason) {
        if (com.android.media.flags.Flags.enableBuiltInSpeakerRouteSuitabilityStatuses()) {
            synchronized (this.mLock) {
                this.mRequestIdToSessionCreationRequest.remove(requestId);
            }
        }
        if (this.mActiveConnection != connection) {
            return;
        }
        if (requestId == 0) {
            android.util.Slog.w(TAG, "onRequestFailed: Ignoring requestId REQUEST_ID_NONE");
        } else {
            this.mCallback.onRequestFailed(this, requestId, reason);
        }
    }

    private void disconnect() {
        if (this.mActiveConnection != null) {
            this.mConnectionReady = false;
            this.mActiveConnection.dispose();
            this.mActiveConnection = null;
            setAndNotifyProviderState(null);
            synchronized (this.mLock) {
                for (android.media.RoutingSessionInfo sessionInfo : this.mSessionInfos) {
                    this.mCallback.onSessionReleased(this, sessionInfo);
                }
                this.mSessionInfos.clear();
                this.mReleasingSessions.clear();
                this.mRequestIdToSessionCreationRequest.clear();
                this.mSessionOriginalIdToTransferRequest.clear();
            }
        }
    }

    @Override // com.android.server.media.MediaRoute2Provider
    protected java.lang.String getDebugString() {
        int pendingSessionCreationCount;
        int pendingTransferCount;
        synchronized (this.mLock) {
            pendingSessionCreationCount = this.mRequestIdToSessionCreationRequest.size();
            pendingTransferCount = this.mSessionOriginalIdToTransferRequest.size();
        }
        return android.text.TextUtils.formatSimple("ProviderServiceProxy - package: %s, bound: %b, connection (active:%b, ready:%b), pending (session creations: %d, transfers: %d)", new java.lang.Object[]{this.mComponentName.getPackageName(), java.lang.Boolean.valueOf(this.mBound), java.lang.Boolean.valueOf(this.mActiveConnection != null), java.lang.Boolean.valueOf(this.mConnectionReady), java.lang.Integer.valueOf(pendingSessionCreationCount), java.lang.Integer.valueOf(pendingTransferCount)});
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class ServiceConnectionImpl implements android.content.ServiceConnection {
        private ServiceConnectionImpl() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, final android.os.IBinder service) {
            if (com.android.media.flags.Flags.enableMr2ServiceNonMainBgThread()) {
                com.android.server.media.MediaRoute2ProviderServiceProxy.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.MediaRoute2ProviderServiceProxy$ServiceConnectionImpl$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onServiceConnected$0(service);
                    }
                });
            } else {
                com.android.server.media.MediaRoute2ProviderServiceProxy.this.onServiceConnectedInternal(service);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceConnected$0(android.os.IBinder service) {
            com.android.server.media.MediaRoute2ProviderServiceProxy.this.onServiceConnectedInternal(service);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            if (com.android.media.flags.Flags.enableMr2ServiceNonMainBgThread()) {
                com.android.server.media.MediaRoute2ProviderServiceProxy.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.MediaRoute2ProviderServiceProxy$ServiceConnectionImpl$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onServiceDisconnected$1();
                    }
                });
            } else {
                com.android.server.media.MediaRoute2ProviderServiceProxy.this.onServiceDisconnectedInternal();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceDisconnected$1() {
            com.android.server.media.MediaRoute2ProviderServiceProxy.this.onServiceDisconnectedInternal();
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(final android.content.ComponentName name) {
            if (com.android.media.flags.Flags.enableMr2ServiceNonMainBgThread()) {
                com.android.server.media.MediaRoute2ProviderServiceProxy.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.MediaRoute2ProviderServiceProxy$ServiceConnectionImpl$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onBindingDied$2(name);
                    }
                });
            } else {
                com.android.server.media.MediaRoute2ProviderServiceProxy.this.onBindingDiedInternal(name);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindingDied$2(android.content.ComponentName name) {
            com.android.server.media.MediaRoute2ProviderServiceProxy.this.onBindingDiedInternal(name);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class Connection implements android.os.IBinder.DeathRecipient {
        private final com.android.server.media.MediaRoute2ProviderServiceProxy.ServiceCallbackStub mCallbackStub = new com.android.server.media.MediaRoute2ProviderServiceProxy.ServiceCallbackStub(this);
        private final android.media.IMediaRoute2ProviderService mService;

        Connection(android.media.IMediaRoute2ProviderService serviceBinder) {
            this.mService = serviceBinder;
        }

        public boolean register() {
            try {
                this.mService.asBinder().linkToDeath(this, 0);
                this.mService.setCallback(this.mCallbackStub);
                com.android.server.media.MediaRoute2ProviderServiceProxy.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.MediaRoute2ProviderServiceProxy$Connection$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$register$0();
                    }
                });
                return true;
            } catch (android.os.RemoteException e) {
                binderDied();
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$register$0() {
            com.android.server.media.MediaRoute2ProviderServiceProxy.this.onConnectionReady(this);
        }

        public void dispose() {
            this.mService.asBinder().unlinkToDeath(this, 0);
            this.mCallbackStub.dispose();
        }

        public void requestCreateSession(long requestId, java.lang.String packageName, java.lang.String routeId, android.os.Bundle sessionHints) {
            try {
                this.mService.requestCreateSession(requestId, packageName, routeId, sessionHints);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaRoute2ProviderServiceProxy.TAG, "requestCreateSession: Failed to deliver request.");
            }
        }

        public void releaseSession(long requestId, java.lang.String sessionId) {
            try {
                this.mService.releaseSession(requestId, sessionId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaRoute2ProviderServiceProxy.TAG, "releaseSession: Failed to deliver request.");
            }
        }

        public void updateDiscoveryPreference(android.media.RouteDiscoveryPreference discoveryPreference) {
            try {
                this.mService.updateDiscoveryPreference(discoveryPreference);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaRoute2ProviderServiceProxy.TAG, "updateDiscoveryPreference: Failed to deliver request.");
            }
        }

        public void selectRoute(long requestId, java.lang.String sessionId, java.lang.String routeId) {
            try {
                this.mService.selectRoute(requestId, sessionId, routeId);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(com.android.server.media.MediaRoute2ProviderServiceProxy.TAG, "selectRoute: Failed to deliver request.", ex);
            }
        }

        public void deselectRoute(long requestId, java.lang.String sessionId, java.lang.String routeId) {
            try {
                this.mService.deselectRoute(requestId, sessionId, routeId);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(com.android.server.media.MediaRoute2ProviderServiceProxy.TAG, "deselectRoute: Failed to deliver request.", ex);
            }
        }

        public void transferToRoute(long requestId, java.lang.String sessionId, java.lang.String routeId) {
            try {
                this.mService.transferToRoute(requestId, sessionId, routeId);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(com.android.server.media.MediaRoute2ProviderServiceProxy.TAG, "transferToRoute: Failed to deliver request.", ex);
            }
        }

        public void setRouteVolume(long requestId, java.lang.String routeId, int volume) {
            try {
                this.mService.setRouteVolume(requestId, routeId, volume);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(com.android.server.media.MediaRoute2ProviderServiceProxy.TAG, "setRouteVolume: Failed to deliver request.", ex);
            }
        }

        public void setSessionVolume(long requestId, java.lang.String sessionId, int volume) {
            try {
                this.mService.setSessionVolume(requestId, sessionId, volume);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(com.android.server.media.MediaRoute2ProviderServiceProxy.TAG, "setSessionVolume: Failed to deliver request.", ex);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$binderDied$1() {
            com.android.server.media.MediaRoute2ProviderServiceProxy.this.onConnectionDied(this);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.media.MediaRoute2ProviderServiceProxy.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.MediaRoute2ProviderServiceProxy$Connection$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$binderDied$1();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$postProviderUpdated$2(android.media.MediaRoute2ProviderInfo providerInfo) {
            com.android.server.media.MediaRoute2ProviderServiceProxy.this.onProviderUpdated(this, providerInfo);
        }

        void postProviderUpdated(final android.media.MediaRoute2ProviderInfo providerInfo) {
            com.android.server.media.MediaRoute2ProviderServiceProxy.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.MediaRoute2ProviderServiceProxy$Connection$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$postProviderUpdated$2(providerInfo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$postSessionCreated$3(long requestId, android.media.RoutingSessionInfo sessionInfo) {
            com.android.server.media.MediaRoute2ProviderServiceProxy.this.onSessionCreated(this, requestId, sessionInfo);
        }

        void postSessionCreated(final long requestId, final android.media.RoutingSessionInfo sessionInfo) {
            com.android.server.media.MediaRoute2ProviderServiceProxy.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.MediaRoute2ProviderServiceProxy$Connection$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$postSessionCreated$3(requestId, sessionInfo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$postSessionsUpdated$4(java.util.List sessionInfo) throws java.lang.Throwable {
            com.android.server.media.MediaRoute2ProviderServiceProxy.this.onSessionsUpdated(this, sessionInfo);
        }

        void postSessionsUpdated(final java.util.List<android.media.RoutingSessionInfo> sessionInfo) {
            com.android.server.media.MediaRoute2ProviderServiceProxy.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.MediaRoute2ProviderServiceProxy$Connection$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() throws java.lang.Throwable {
                    this.f$0.lambda$postSessionsUpdated$4(sessionInfo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$postSessionReleased$5(android.media.RoutingSessionInfo sessionInfo) {
            com.android.server.media.MediaRoute2ProviderServiceProxy.this.onSessionReleased(this, sessionInfo);
        }

        void postSessionReleased(final android.media.RoutingSessionInfo sessionInfo) {
            com.android.server.media.MediaRoute2ProviderServiceProxy.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.MediaRoute2ProviderServiceProxy$Connection$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$postSessionReleased$5(sessionInfo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$postRequestFailed$6(long requestId, int reason) {
            com.android.server.media.MediaRoute2ProviderServiceProxy.this.onRequestFailed(this, requestId, reason);
        }

        void postRequestFailed(final long requestId, final int reason) {
            com.android.server.media.MediaRoute2ProviderServiceProxy.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.MediaRoute2ProviderServiceProxy$Connection$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$postRequestFailed$6(requestId, reason);
                }
            });
        }
    }

    private static final class ServiceCallbackStub extends android.media.IMediaRoute2ProviderServiceCallback.Stub {
        private final java.lang.ref.WeakReference<com.android.server.media.MediaRoute2ProviderServiceProxy.Connection> mConnectionRef;

        ServiceCallbackStub(com.android.server.media.MediaRoute2ProviderServiceProxy.Connection connection) {
            this.mConnectionRef = new java.lang.ref.WeakReference<>(connection);
        }

        public void dispose() {
            this.mConnectionRef.clear();
        }

        public void notifyProviderUpdated(android.media.MediaRoute2ProviderInfo providerInfo) {
            java.util.Objects.requireNonNull(providerInfo, "providerInfo must not be null");
            for (android.media.MediaRoute2Info route : providerInfo.getRoutes()) {
                if (route.isSystemRoute()) {
                    throw new java.lang.SecurityException("Only the system is allowed to publish system routes. Disallowed route: " + route);
                }
                if (route.getSuitabilityStatus() == 2) {
                    throw new java.lang.SecurityException("Only the system is allowed to set not suitable for transfer status. Disallowed route: " + route);
                }
                if (route.isSystemRouteType()) {
                    throw new java.lang.SecurityException("Only the system is allowed to publish routes with system route types. Disallowed route: " + route);
                }
            }
            com.android.server.media.MediaRoute2ProviderServiceProxy.Connection connection = this.mConnectionRef.get();
            if (connection != null) {
                connection.postProviderUpdated(providerInfo);
            }
        }

        public void notifySessionCreated(long requestId, android.media.RoutingSessionInfo sessionInfo) {
            com.android.server.media.MediaRoute2ProviderServiceProxy.Connection connection = this.mConnectionRef.get();
            if (connection != null) {
                connection.postSessionCreated(requestId, sessionInfo);
            }
        }

        public void notifySessionsUpdated(java.util.List<android.media.RoutingSessionInfo> sessionInfo) {
            com.android.server.media.MediaRoute2ProviderServiceProxy.Connection connection = this.mConnectionRef.get();
            if (connection != null) {
                connection.postSessionsUpdated(sessionInfo);
            }
        }

        public void notifySessionReleased(android.media.RoutingSessionInfo sessionInfo) {
            com.android.server.media.MediaRoute2ProviderServiceProxy.Connection connection = this.mConnectionRef.get();
            if (connection != null) {
                connection.postSessionReleased(sessionInfo);
            }
        }

        public void notifyRequestFailed(long requestId, int reason) {
            com.android.server.media.MediaRoute2ProviderServiceProxy.Connection connection = this.mConnectionRef.get();
            if (connection != null) {
                connection.postRequestFailed(requestId, reason);
            }
        }
    }
}
